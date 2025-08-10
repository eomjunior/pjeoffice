/*     */ package org.apache.hc.core5.http.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import org.apache.hc.core5.http.MessageConstraintException;
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
/*     */ class SessionInputBufferImpl
/*     */   extends ExpandableBuffer
/*     */   implements SessionInputBuffer
/*     */ {
/*     */   private final CharsetDecoder charDecoder;
/*     */   private final int lineBuffersize;
/*     */   private final int maxLineLen;
/*     */   private CharBuffer charbuffer;
/*     */   
/*     */   public SessionInputBufferImpl(int bufferSize, int lineBuffersize, int maxLineLen, CharsetDecoder charDecoder) {
/*  70 */     super(bufferSize);
/*  71 */     this.lineBuffersize = Args.positive(lineBuffersize, "Line buffer size");
/*  72 */     this.maxLineLen = Math.max(maxLineLen, 0);
/*  73 */     this.charDecoder = charDecoder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SessionInputBufferImpl(int bufferSize, int lineBuffersize, int maxLineLen, Charset charset) {
/*  84 */     this(bufferSize, lineBuffersize, maxLineLen, (charset != null) ? charset.newDecoder() : null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SessionInputBufferImpl(int bufferSize, int lineBuffersize, int maxLineLen) {
/*  94 */     this(bufferSize, lineBuffersize, maxLineLen, (CharsetDecoder)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SessionInputBufferImpl(int bufferSize, int lineBuffersize) {
/* 103 */     this(bufferSize, lineBuffersize, 0, (CharsetDecoder)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SessionInputBufferImpl(int bufferSize) {
/* 110 */     this(bufferSize, 256);
/*     */   }
/*     */ 
/*     */   
/*     */   public int length() {
/* 115 */     return super.length();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasData() {
/* 120 */     return super.hasData();
/*     */   }
/*     */ 
/*     */   
/*     */   public int capacity() {
/* 125 */     return super.capacity();
/*     */   }
/*     */   
/*     */   public void put(ByteBuffer src) {
/* 129 */     if (src != null && src.hasRemaining()) {
/* 130 */       setInputMode();
/* 131 */       ensureAdjustedCapacity(buffer().position() + src.remaining());
/* 132 */       buffer().put(src);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int fill(ReadableByteChannel channel) throws IOException {
/* 138 */     Args.notNull(channel, "Channel");
/* 139 */     setInputMode();
/* 140 */     if (!buffer().hasRemaining()) {
/* 141 */       expand();
/*     */     }
/* 143 */     return channel.read(buffer());
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() {
/* 148 */     setOutputMode();
/* 149 */     return buffer().get() & 0xFF;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(ByteBuffer dst, int maxLen) {
/* 154 */     if (dst == null) {
/* 155 */       return 0;
/*     */     }
/* 157 */     setOutputMode();
/* 158 */     int len = Math.min(dst.remaining(), maxLen);
/* 159 */     int chunk = Math.min(buffer().remaining(), len);
/* 160 */     if (buffer().remaining() > chunk) {
/* 161 */       int oldLimit = buffer().limit();
/* 162 */       int newLimit = buffer().position() + chunk;
/* 163 */       buffer().limit(newLimit);
/* 164 */       dst.put(buffer());
/* 165 */       buffer().limit(oldLimit);
/* 166 */       return len;
/*     */     } 
/* 168 */     dst.put(buffer());
/* 169 */     return chunk;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(ByteBuffer dst) {
/* 174 */     if (dst == null) {
/* 175 */       return 0;
/*     */     }
/* 177 */     return read(dst, dst.remaining());
/*     */   }
/*     */   
/*     */   public int read(WritableByteChannel dst, int maxLen) throws IOException {
/*     */     int bytesRead;
/* 182 */     if (dst == null) {
/* 183 */       return 0;
/*     */     }
/* 185 */     setOutputMode();
/*     */     
/* 187 */     if (buffer().remaining() > maxLen) {
/* 188 */       int oldLimit = buffer().limit();
/* 189 */       int newLimit = oldLimit - buffer().remaining() - maxLen;
/* 190 */       buffer().limit(newLimit);
/* 191 */       bytesRead = dst.write(buffer());
/* 192 */       buffer().limit(oldLimit);
/*     */     } else {
/* 194 */       bytesRead = dst.write(buffer());
/*     */     } 
/* 196 */     return bytesRead;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(WritableByteChannel dst) throws IOException {
/* 201 */     if (dst == null) {
/* 202 */       return 0;
/*     */     }
/* 204 */     setOutputMode();
/* 205 */     return dst.write(buffer());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean readLine(CharArrayBuffer lineBuffer, boolean endOfStream) throws IOException {
/* 213 */     setOutputMode();
/*     */     
/* 215 */     int pos = -1;
/* 216 */     for (int i = buffer().position(); i < buffer().limit(); i++) {
/* 217 */       int b = buffer().get(i);
/* 218 */       if (b == 10) {
/* 219 */         pos = i + 1;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 224 */     if (this.maxLineLen > 0) {
/* 225 */       int currentLen = ((pos > 0) ? pos : buffer().limit()) - buffer().position();
/* 226 */       if (currentLen >= this.maxLineLen) {
/* 227 */         throw new MessageConstraintException("Maximum line length limit exceeded");
/*     */       }
/*     */     } 
/*     */     
/* 231 */     if (pos == -1) {
/* 232 */       if (endOfStream && buffer().hasRemaining()) {
/*     */         
/* 234 */         pos = buffer().limit();
/*     */       }
/*     */       else {
/*     */         
/* 238 */         return false;
/*     */       } 
/*     */     }
/* 241 */     int origLimit = buffer().limit();
/* 242 */     buffer().limit(pos);
/*     */     
/* 244 */     int requiredCapacity = buffer().limit() - buffer().position();
/*     */     
/* 246 */     lineBuffer.ensureCapacity(requiredCapacity);
/*     */     
/* 248 */     if (this.charDecoder == null) {
/* 249 */       if (buffer().hasArray()) {
/* 250 */         byte[] b = buffer().array();
/* 251 */         int off = buffer().position();
/* 252 */         int len = buffer().remaining();
/* 253 */         lineBuffer.append(b, buffer().arrayOffset() + off, len);
/* 254 */         buffer().position(off + len);
/*     */       } else {
/* 256 */         while (buffer().hasRemaining())
/* 257 */           lineBuffer.append((char)(buffer().get() & 0xFF)); 
/*     */       } 
/*     */     } else {
/*     */       CoderResult result;
/* 261 */       if (this.charbuffer == null) {
/* 262 */         this.charbuffer = CharBuffer.allocate(this.lineBuffersize);
/*     */       }
/* 264 */       this.charDecoder.reset();
/*     */       
/*     */       do {
/* 267 */         result = this.charDecoder.decode(
/* 268 */             buffer(), this.charbuffer, true);
/*     */ 
/*     */         
/* 271 */         if (result.isError()) {
/* 272 */           result.throwException();
/*     */         }
/* 274 */         if (!result.isOverflow())
/* 275 */           continue;  this.charbuffer.flip();
/* 276 */         lineBuffer.append(this.charbuffer
/* 277 */             .array(), this.charbuffer
/* 278 */             .arrayOffset() + this.charbuffer.position(), this.charbuffer
/* 279 */             .remaining());
/* 280 */         this.charbuffer.clear();
/*     */       }
/* 282 */       while (!result.isUnderflow());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 288 */       this.charDecoder.flush(this.charbuffer);
/* 289 */       this.charbuffer.flip();
/*     */       
/* 291 */       if (this.charbuffer.hasRemaining()) {
/* 292 */         lineBuffer.append(this.charbuffer
/* 293 */             .array(), this.charbuffer
/* 294 */             .arrayOffset() + this.charbuffer.position(), this.charbuffer
/* 295 */             .remaining());
/*     */       }
/*     */     } 
/*     */     
/* 299 */     buffer().limit(origLimit);
/*     */ 
/*     */     
/* 302 */     int l = lineBuffer.length();
/* 303 */     if (l > 0) {
/* 304 */       if (lineBuffer.charAt(l - 1) == '\n') {
/* 305 */         l--;
/* 306 */         lineBuffer.setLength(l);
/*     */       } 
/*     */       
/* 309 */       if (l > 0 && lineBuffer.charAt(l - 1) == '\r') {
/* 310 */         l--;
/* 311 */         lineBuffer.setLength(l);
/*     */       } 
/*     */     } 
/* 314 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/nio/SessionInputBufferImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */