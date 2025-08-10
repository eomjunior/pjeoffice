/*     */ package org.apache.hc.core5.http.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.http.ConnectionClosedException;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.MalformedChunkCodingException;
/*     */ import org.apache.hc.core5.http.MessageConstraintException;
/*     */ import org.apache.hc.core5.http.ParseException;
/*     */ import org.apache.hc.core5.http.TruncatedChunkException;
/*     */ import org.apache.hc.core5.http.config.Http1Config;
/*     */ import org.apache.hc.core5.http.impl.BasicHttpTransportMetrics;
/*     */ import org.apache.hc.core5.http.message.BufferedHeader;
/*     */ import org.apache.hc.core5.http.nio.SessionInputBuffer;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.CharArrayBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChunkDecoder
/*     */   extends AbstractContentDecoder
/*     */ {
/*     */   private State state;
/*     */   private boolean endOfChunk;
/*     */   private boolean endOfStream;
/*     */   private CharArrayBuffer lineBuf;
/*     */   private long chunkSize;
/*     */   private long pos;
/*     */   private final Http1Config http1Config;
/*     */   private final List<CharArrayBuffer> trailerBufs;
/*     */   private final List<Header> trailers;
/*     */   
/*     */   private enum State
/*     */   {
/*  58 */     READ_CONTENT, READ_FOOTERS, COMPLETED;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChunkDecoder(ReadableByteChannel channel, SessionInputBuffer buffer, Http1Config http1Config, BasicHttpTransportMetrics metrics) {
/*  81 */     super(channel, buffer, metrics);
/*  82 */     this.state = State.READ_CONTENT;
/*  83 */     this.chunkSize = -1L;
/*  84 */     this.pos = 0L;
/*  85 */     this.endOfChunk = false;
/*  86 */     this.endOfStream = false;
/*  87 */     this.http1Config = (http1Config != null) ? http1Config : Http1Config.DEFAULT;
/*  88 */     this.trailerBufs = new ArrayList<>();
/*  89 */     this.trailers = new ArrayList<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChunkDecoder(ReadableByteChannel channel, SessionInputBuffer buffer, BasicHttpTransportMetrics metrics) {
/*  96 */     this(channel, buffer, (Http1Config)null, metrics);
/*     */   }
/*     */   
/*     */   private void readChunkHead() throws IOException {
/* 100 */     if (this.lineBuf == null) {
/* 101 */       this.lineBuf = new CharArrayBuffer(32);
/*     */     } else {
/* 103 */       this.lineBuf.clear();
/*     */     } 
/* 105 */     if (this.endOfChunk) {
/* 106 */       if (this.buffer.readLine(this.lineBuf, this.endOfStream)) {
/* 107 */         if (!this.lineBuf.isEmpty()) {
/* 108 */           throw new MalformedChunkCodingException("CRLF expected at end of chunk");
/*     */         }
/*     */       } else {
/* 111 */         if (this.buffer.length() > 2 || this.endOfStream) {
/* 112 */           throw new MalformedChunkCodingException("CRLF expected at end of chunk");
/*     */         }
/*     */         return;
/*     */       } 
/* 116 */       this.endOfChunk = false;
/*     */     } 
/* 118 */     boolean lineComplete = this.buffer.readLine(this.lineBuf, this.endOfStream);
/* 119 */     int maxLineLen = this.http1Config.getMaxLineLength();
/* 120 */     if (maxLineLen > 0 && (this.lineBuf
/* 121 */       .length() > maxLineLen || (!lineComplete && this.buffer
/* 122 */       .length() > maxLineLen))) {
/* 123 */       throw new MessageConstraintException("Maximum line length limit exceeded");
/*     */     }
/* 125 */     if (lineComplete) {
/* 126 */       int separator = this.lineBuf.indexOf(59);
/* 127 */       if (separator < 0) {
/* 128 */         separator = this.lineBuf.length();
/*     */       }
/* 130 */       String s = this.lineBuf.substringTrimmed(0, separator);
/*     */       try {
/* 132 */         this.chunkSize = Long.parseLong(s, 16);
/* 133 */       } catch (NumberFormatException e) {
/* 134 */         throw new MalformedChunkCodingException("Bad chunk header: " + s);
/*     */       } 
/* 136 */       this.pos = 0L;
/* 137 */     } else if (this.endOfStream) {
/* 138 */       throw new ConnectionClosedException("Premature end of chunk coded message body: closing chunk expected");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void parseHeader() throws IOException {
/* 144 */     CharArrayBuffer current = this.lineBuf;
/* 145 */     int count = this.trailerBufs.size();
/* 146 */     if ((this.lineBuf.charAt(0) == ' ' || this.lineBuf.charAt(0) == '\t') && count > 0) {
/*     */       
/* 148 */       CharArrayBuffer previous = this.trailerBufs.get(count - 1);
/* 149 */       int i = 0;
/* 150 */       while (i < current.length()) {
/* 151 */         char ch = current.charAt(i);
/* 152 */         if (ch != ' ' && ch != '\t') {
/*     */           break;
/*     */         }
/* 155 */         i++;
/*     */       } 
/* 157 */       int maxLineLen = this.http1Config.getMaxLineLength();
/* 158 */       if (maxLineLen > 0 && previous.length() + 1 + current.length() - i > maxLineLen) {
/* 159 */         throw new MessageConstraintException("Maximum line length limit exceeded");
/*     */       }
/* 161 */       previous.append(' ');
/* 162 */       previous.append(current, i, current.length() - i);
/*     */     } else {
/* 164 */       this.trailerBufs.add(current);
/* 165 */       this.lineBuf = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void processFooters() throws IOException {
/* 170 */     int count = this.trailerBufs.size();
/* 171 */     if (count > 0) {
/* 172 */       this.trailers.clear();
/* 173 */       for (int i = 0; i < this.trailerBufs.size(); i++) {
/*     */         try {
/* 175 */           this.trailers.add(new BufferedHeader(this.trailerBufs.get(i)));
/* 176 */         } catch (ParseException ex) {
/* 177 */           throw new IOException(ex);
/*     */         } 
/*     */       } 
/*     */     } 
/* 181 */     this.trailerBufs.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(ByteBuffer dst) throws IOException {
/* 186 */     Args.notNull(dst, "Byte buffer");
/* 187 */     if (this.state == State.COMPLETED) {
/* 188 */       return -1;
/*     */     }
/*     */     
/* 191 */     int totalRead = 0;
/* 192 */     while (this.state != State.COMPLETED) {
/*     */       long maxLen; int len;
/* 194 */       if (!this.buffer.hasData() || this.chunkSize == -1L) {
/* 195 */         int bytesRead = fillBufferFromChannel();
/* 196 */         if (bytesRead == -1) {
/* 197 */           this.endOfStream = true;
/*     */         }
/*     */       } 
/*     */       
/* 201 */       switch (this.state) {
/*     */         
/*     */         case READ_CONTENT:
/* 204 */           if (this.chunkSize == -1L) {
/* 205 */             readChunkHead();
/* 206 */             if (this.chunkSize == -1L)
/*     */             {
/* 208 */               return totalRead;
/*     */             }
/* 210 */             if (this.chunkSize == 0L) {
/*     */               
/* 212 */               this.chunkSize = -1L;
/* 213 */               this.state = State.READ_FOOTERS;
/*     */               continue;
/*     */             } 
/*     */           } 
/* 217 */           maxLen = this.chunkSize - this.pos;
/* 218 */           len = this.buffer.read(dst, (int)Math.min(maxLen, 2147483647L));
/* 219 */           if (len > 0) {
/* 220 */             this.pos += len;
/* 221 */             totalRead += len;
/*     */           }
/* 223 */           else if (!this.buffer.hasData() && this.endOfStream) {
/* 224 */             this.state = State.COMPLETED;
/* 225 */             setCompleted();
/* 226 */             throw new TruncatedChunkException("Truncated chunk (expected size: %d; actual size: %d)", new Object[] {
/*     */                   
/* 228 */                   Long.valueOf(this.chunkSize), Long.valueOf(this.pos)
/*     */                 });
/*     */           } 
/*     */           
/* 232 */           if (this.pos == this.chunkSize) {
/*     */             
/* 234 */             this.chunkSize = -1L;
/* 235 */             this.pos = 0L;
/* 236 */             this.endOfChunk = true;
/*     */             continue;
/*     */           } 
/* 239 */           return totalRead;
/*     */         case READ_FOOTERS:
/* 241 */           if (this.lineBuf == null) {
/* 242 */             this.lineBuf = new CharArrayBuffer(32);
/*     */           } else {
/* 244 */             this.lineBuf.clear();
/*     */           } 
/* 246 */           if (!this.buffer.readLine(this.lineBuf, this.endOfStream)) {
/*     */             
/* 248 */             if (this.endOfStream) {
/* 249 */               this.state = State.COMPLETED;
/* 250 */               setCompleted();
/*     */             } 
/* 252 */             return totalRead;
/*     */           } 
/* 254 */           if (this.lineBuf.length() > 0) {
/* 255 */             int maxHeaderCount = this.http1Config.getMaxHeaderCount();
/* 256 */             if (maxHeaderCount > 0 && this.trailerBufs.size() >= maxHeaderCount) {
/* 257 */               throw new MessageConstraintException("Maximum header count exceeded");
/*     */             }
/* 259 */             parseHeader(); continue;
/*     */           } 
/* 261 */           this.state = State.COMPLETED;
/* 262 */           setCompleted();
/* 263 */           processFooters();
/*     */       } 
/*     */ 
/*     */ 
/*     */     
/*     */     } 
/* 269 */     return totalRead;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<? extends Header> getTrailers() {
/* 274 */     return this.trailers.isEmpty() ? null : new ArrayList<>(this.trailers);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 279 */     StringBuilder sb = new StringBuilder();
/* 280 */     sb.append("[chunk-coded; completed: ");
/* 281 */     sb.append(this.completed);
/* 282 */     sb.append("]");
/* 283 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/nio/ChunkDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */