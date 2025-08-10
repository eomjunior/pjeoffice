/*     */ package org.apache.hc.core5.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.apache.hc.core5.http.ConnectionClosedException;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.MalformedChunkCodingException;
/*     */ import org.apache.hc.core5.http.StreamClosedException;
/*     */ import org.apache.hc.core5.http.TruncatedChunkException;
/*     */ import org.apache.hc.core5.http.config.Http1Config;
/*     */ import org.apache.hc.core5.http.io.SessionInputBuffer;
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
/*     */ public class ChunkedInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private static final int BUFFER_SIZE = 2048;
/*     */   
/*     */   private enum State
/*     */   {
/*  63 */     CHUNK_LEN, CHUNK_DATA, CHUNK_CRLF, CHUNK_INVALID;
/*     */   }
/*     */ 
/*     */   
/*  67 */   private static final Header[] EMPTY_FOOTERS = new Header[0];
/*     */ 
/*     */   
/*     */   private final SessionInputBuffer buffer;
/*     */ 
/*     */   
/*     */   private final InputStream inputStream;
/*     */   
/*     */   private final CharArrayBuffer lineBuffer;
/*     */   
/*     */   private final Http1Config http1Config;
/*     */   
/*     */   private State state;
/*     */   
/*     */   private long chunkSize;
/*     */   
/*     */   private long pos;
/*     */   
/*     */   private boolean eof;
/*     */   
/*     */   private boolean closed;
/*     */   
/*  89 */   private Header[] footers = EMPTY_FOOTERS;
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
/*     */   public ChunkedInputStream(SessionInputBuffer buffer, InputStream inputStream, Http1Config http1Config) {
/* 102 */     this.buffer = (SessionInputBuffer)Args.notNull(buffer, "Session input buffer");
/* 103 */     this.inputStream = (InputStream)Args.notNull(inputStream, "Input stream");
/* 104 */     this.pos = 0L;
/* 105 */     this.lineBuffer = new CharArrayBuffer(16);
/* 106 */     this.http1Config = (http1Config != null) ? http1Config : Http1Config.DEFAULT;
/* 107 */     this.state = State.CHUNK_LEN;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChunkedInputStream(SessionInputBuffer buffer, InputStream inputStream) {
/* 117 */     this(buffer, inputStream, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() throws IOException {
/* 122 */     int len = this.buffer.length();
/* 123 */     return (int)Math.min(len, this.chunkSize - this.pos);
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
/*     */   public int read() throws IOException {
/* 140 */     if (this.closed) {
/* 141 */       throw new StreamClosedException();
/*     */     }
/* 143 */     if (this.eof) {
/* 144 */       return -1;
/*     */     }
/* 146 */     if (this.state != State.CHUNK_DATA) {
/* 147 */       nextChunk();
/* 148 */       if (this.eof) {
/* 149 */         return -1;
/*     */       }
/*     */     } 
/* 152 */     int b = this.buffer.read(this.inputStream);
/* 153 */     if (b != -1) {
/* 154 */       this.pos++;
/* 155 */       if (this.pos >= this.chunkSize) {
/* 156 */         this.state = State.CHUNK_CRLF;
/*     */       }
/*     */     } 
/* 159 */     return b;
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
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/* 175 */     if (this.closed) {
/* 176 */       throw new StreamClosedException();
/*     */     }
/*     */     
/* 179 */     if (this.eof) {
/* 180 */       return -1;
/*     */     }
/* 182 */     if (this.state != State.CHUNK_DATA) {
/* 183 */       nextChunk();
/* 184 */       if (this.eof) {
/* 185 */         return -1;
/*     */       }
/*     */     } 
/* 188 */     int bytesRead = this.buffer.read(b, off, (int)Math.min(len, this.chunkSize - this.pos), this.inputStream);
/* 189 */     if (bytesRead != -1) {
/* 190 */       this.pos += bytesRead;
/* 191 */       if (this.pos >= this.chunkSize) {
/* 192 */         this.state = State.CHUNK_CRLF;
/*     */       }
/* 194 */       return bytesRead;
/*     */     } 
/* 196 */     this.eof = true;
/* 197 */     throw new TruncatedChunkException("Truncated chunk (expected size: %d; actual size: %d)", new Object[] {
/* 198 */           Long.valueOf(this.chunkSize), Long.valueOf(this.pos)
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(byte[] b) throws IOException {
/* 210 */     return read(b, 0, b.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void nextChunk() throws IOException {
/* 218 */     if (this.state == State.CHUNK_INVALID) {
/* 219 */       throw new MalformedChunkCodingException("Corrupt data stream");
/*     */     }
/*     */     try {
/* 222 */       this.chunkSize = getChunkSize();
/* 223 */       if (this.chunkSize < 0L) {
/* 224 */         throw new MalformedChunkCodingException("Negative chunk size");
/*     */       }
/* 226 */       this.state = State.CHUNK_DATA;
/* 227 */       this.pos = 0L;
/* 228 */       if (this.chunkSize == 0L) {
/* 229 */         this.eof = true;
/* 230 */         parseTrailerHeaders();
/*     */       } 
/* 232 */     } catch (MalformedChunkCodingException ex) {
/* 233 */       this.state = State.CHUNK_INVALID;
/* 234 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long getChunkSize() throws IOException {
/*     */     int bytesRead1, bytesRead2, separator;
/*     */     String s;
/* 244 */     State st = this.state;
/* 245 */     switch (st) {
/*     */       case CHUNK_CRLF:
/* 247 */         this.lineBuffer.clear();
/* 248 */         bytesRead1 = this.buffer.readLine(this.lineBuffer, this.inputStream);
/* 249 */         if (bytesRead1 == -1) {
/* 250 */           throw new MalformedChunkCodingException("CRLF expected at end of chunk");
/*     */         }
/*     */         
/* 253 */         if (!this.lineBuffer.isEmpty()) {
/* 254 */           throw new MalformedChunkCodingException("Unexpected content at the end of chunk");
/*     */         }
/*     */         
/* 257 */         this.state = State.CHUNK_LEN;
/*     */       
/*     */       case CHUNK_LEN:
/* 260 */         this.lineBuffer.clear();
/* 261 */         bytesRead2 = this.buffer.readLine(this.lineBuffer, this.inputStream);
/* 262 */         if (bytesRead2 == -1) {
/* 263 */           throw new ConnectionClosedException("Premature end of chunk coded message body: closing chunk expected");
/*     */         }
/*     */         
/* 266 */         separator = this.lineBuffer.indexOf(59);
/* 267 */         if (separator < 0) {
/* 268 */           separator = this.lineBuffer.length();
/*     */         }
/* 270 */         s = this.lineBuffer.substringTrimmed(0, separator);
/*     */         try {
/* 272 */           return Long.parseLong(s, 16);
/* 273 */         } catch (NumberFormatException e) {
/* 274 */           throw new MalformedChunkCodingException("Bad chunk header: " + s);
/*     */         } 
/*     */     } 
/* 277 */     throw new IllegalStateException("Inconsistent codec state");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void parseTrailerHeaders() throws IOException {
/*     */     try {
/* 287 */       this.footers = AbstractMessageParser.parseHeaders(this.buffer, this.inputStream, this.http1Config
/* 288 */           .getMaxHeaderCount(), this.http1Config
/* 289 */           .getMaxLineLength(), null);
/*     */     }
/* 291 */     catch (HttpException ex) {
/*     */       
/* 293 */       MalformedChunkCodingException malformedChunkCodingException = new MalformedChunkCodingException("Invalid trailing header: " + ex.getMessage());
/* 294 */       malformedChunkCodingException.initCause((Throwable)ex);
/* 295 */       throw malformedChunkCodingException;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 307 */     if (!this.closed) {
/*     */       try {
/* 309 */         if (!this.eof && this.state != State.CHUNK_INVALID) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 314 */           if (this.chunkSize == this.pos && this.chunkSize > 0L && read() == -1) {
/*     */             return;
/*     */           }
/*     */           
/* 318 */           byte[] buff = new byte[2048];
/* 319 */           while (read(buff) >= 0);
/*     */         } 
/*     */       } finally {
/*     */         
/* 323 */         this.eof = true;
/* 324 */         this.closed = true;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public Header[] getFooters() {
/* 330 */     return (this.footers.length > 0) ? (Header[])this.footers.clone() : EMPTY_FOOTERS;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/io/ChunkedInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */