/*     */ package org.apache.hc.core5.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import org.apache.hc.core5.http.MessageConstraintException;
/*     */ import org.apache.hc.core5.http.impl.BasicHttpTransportMetrics;
/*     */ import org.apache.hc.core5.http.io.HttpTransportMetrics;
/*     */ import org.apache.hc.core5.http.io.SessionInputBuffer;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.ByteArrayBuffer;
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
/*     */ public class SessionInputBufferImpl
/*     */   implements SessionInputBuffer
/*     */ {
/*     */   private final BasicHttpTransportMetrics metrics;
/*     */   private final byte[] buffer;
/*     */   private final ByteArrayBuffer lineBuffer;
/*     */   private final int minChunkLimit;
/*     */   private final int maxLineLen;
/*     */   private final CharsetDecoder decoder;
/*     */   private int bufferPos;
/*     */   private int bufferLen;
/*     */   private CharBuffer cbuf;
/*     */   
/*     */   public SessionInputBufferImpl(BasicHttpTransportMetrics metrics, int bufferSize, int minChunkLimit, int maxLineLen, CharsetDecoder charDecoder) {
/*  90 */     Args.notNull(metrics, "HTTP transport metrics");
/*  91 */     Args.positive(bufferSize, "Buffer size");
/*  92 */     this.metrics = metrics;
/*  93 */     this.buffer = new byte[bufferSize];
/*  94 */     this.bufferPos = 0;
/*  95 */     this.bufferLen = 0;
/*  96 */     this.minChunkLimit = (minChunkLimit >= 0) ? minChunkLimit : 512;
/*  97 */     this.maxLineLen = Math.max(maxLineLen, 0);
/*  98 */     this.lineBuffer = new ByteArrayBuffer(bufferSize);
/*  99 */     this.decoder = charDecoder;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SessionInputBufferImpl(BasicHttpTransportMetrics metrics, int bufferSize) {
/* 105 */     this(metrics, bufferSize, bufferSize, 0, null);
/*     */   }
/*     */   
/*     */   public SessionInputBufferImpl(int bufferSize, int maxLineLen) {
/* 109 */     this(new BasicHttpTransportMetrics(), bufferSize, bufferSize, maxLineLen, null);
/*     */   }
/*     */   
/*     */   public SessionInputBufferImpl(int bufferSize, CharsetDecoder decoder) {
/* 113 */     this(new BasicHttpTransportMetrics(), bufferSize, bufferSize, 0, decoder);
/*     */   }
/*     */   
/*     */   public SessionInputBufferImpl(int bufferSize) {
/* 117 */     this(new BasicHttpTransportMetrics(), bufferSize, bufferSize, 0, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public int capacity() {
/* 122 */     return this.buffer.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public int length() {
/* 127 */     return this.bufferLen - this.bufferPos;
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() {
/* 132 */     return capacity() - length();
/*     */   }
/*     */   
/*     */   public int fillBuffer(InputStream inputStream) throws IOException {
/* 136 */     Args.notNull(inputStream, "Input stream");
/*     */     
/* 138 */     if (this.bufferPos > 0) {
/* 139 */       int i = this.bufferLen - this.bufferPos;
/* 140 */       if (i > 0) {
/* 141 */         System.arraycopy(this.buffer, this.bufferPos, this.buffer, 0, i);
/*     */       }
/* 143 */       this.bufferPos = 0;
/* 144 */       this.bufferLen = i;
/*     */     } 
/*     */     
/* 147 */     int off = this.bufferLen;
/* 148 */     int len = this.buffer.length - off;
/* 149 */     int readLen = inputStream.read(this.buffer, off, len);
/* 150 */     if (readLen == -1) {
/* 151 */       return -1;
/*     */     }
/* 153 */     this.bufferLen = off + readLen;
/* 154 */     this.metrics.incrementBytesTransferred(readLen);
/* 155 */     return readLen;
/*     */   }
/*     */   
/*     */   public boolean hasBufferedData() {
/* 159 */     return (this.bufferPos < this.bufferLen);
/*     */   }
/*     */   
/*     */   public void clear() {
/* 163 */     this.bufferPos = 0;
/* 164 */     this.bufferLen = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(InputStream inputStream) throws IOException {
/* 169 */     Args.notNull(inputStream, "Input stream");
/*     */     
/* 171 */     while (!hasBufferedData()) {
/* 172 */       int readLen = fillBuffer(inputStream);
/* 173 */       if (readLen == -1) {
/* 174 */         return -1;
/*     */       }
/*     */     } 
/* 177 */     return this.buffer[this.bufferPos++] & 0xFF;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int off, int len, InputStream inputStream) throws IOException {
/* 182 */     Args.notNull(inputStream, "Input stream");
/* 183 */     if (b == null) {
/* 184 */       return 0;
/*     */     }
/* 186 */     if (hasBufferedData()) {
/* 187 */       int i = Math.min(len, this.bufferLen - this.bufferPos);
/* 188 */       System.arraycopy(this.buffer, this.bufferPos, b, off, i);
/* 189 */       this.bufferPos += i;
/* 190 */       return i;
/*     */     } 
/*     */ 
/*     */     
/* 194 */     if (len > this.minChunkLimit) {
/* 195 */       int read = inputStream.read(b, off, len);
/* 196 */       if (read > 0) {
/* 197 */         this.metrics.incrementBytesTransferred(read);
/*     */       }
/* 199 */       return read;
/*     */     } 
/*     */     
/* 202 */     while (!hasBufferedData()) {
/* 203 */       int readLen = fillBuffer(inputStream);
/* 204 */       if (readLen == -1) {
/* 205 */         return -1;
/*     */       }
/*     */     } 
/* 208 */     int chunk = Math.min(len, this.bufferLen - this.bufferPos);
/* 209 */     System.arraycopy(this.buffer, this.bufferPos, b, off, chunk);
/* 210 */     this.bufferPos += chunk;
/* 211 */     return chunk;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b, InputStream inputStream) throws IOException {
/* 216 */     if (b == null) {
/* 217 */       return 0;
/*     */     }
/* 219 */     return read(b, 0, b.length, inputStream);
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
/*     */   public int readLine(CharArrayBuffer charBuffer, InputStream inputStream) throws IOException {
/* 241 */     Args.notNull(charBuffer, "Char array buffer");
/* 242 */     Args.notNull(inputStream, "Input stream");
/* 243 */     int readLen = 0;
/* 244 */     boolean retry = true;
/* 245 */     while (retry) {
/*     */       
/* 247 */       int pos = -1;
/* 248 */       for (int i = this.bufferPos; i < this.bufferLen; i++) {
/* 249 */         if (this.buffer[i] == 10) {
/* 250 */           pos = i;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 255 */       if (this.maxLineLen > 0) {
/* 256 */         int currentLen = this.lineBuffer.length() + ((pos >= 0) ? pos : this.bufferLen) - this.bufferPos;
/*     */         
/* 258 */         if (currentLen >= this.maxLineLen) {
/* 259 */           throw new MessageConstraintException("Maximum line length limit exceeded");
/*     */         }
/*     */       } 
/*     */       
/* 263 */       if (pos != -1) {
/*     */         
/* 265 */         if (this.lineBuffer.isEmpty())
/*     */         {
/* 267 */           return lineFromReadBuffer(charBuffer, pos);
/*     */         }
/* 269 */         retry = false;
/* 270 */         int len = pos + 1 - this.bufferPos;
/* 271 */         this.lineBuffer.append(this.buffer, this.bufferPos, len);
/* 272 */         this.bufferPos = pos + 1;
/*     */         continue;
/*     */       } 
/* 275 */       if (hasBufferedData()) {
/* 276 */         int len = this.bufferLen - this.bufferPos;
/* 277 */         this.lineBuffer.append(this.buffer, this.bufferPos, len);
/* 278 */         this.bufferPos = this.bufferLen;
/*     */       } 
/* 280 */       readLen = fillBuffer(inputStream);
/* 281 */       if (readLen == -1) {
/* 282 */         retry = false;
/*     */       }
/*     */     } 
/*     */     
/* 286 */     if (readLen == -1 && this.lineBuffer.isEmpty())
/*     */     {
/* 288 */       return -1;
/*     */     }
/* 290 */     return lineFromLineBuffer(charBuffer);
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
/*     */   private int lineFromLineBuffer(CharArrayBuffer charBuffer) throws IOException {
/* 309 */     int len = this.lineBuffer.length();
/* 310 */     if (len > 0) {
/* 311 */       if (this.lineBuffer.byteAt(len - 1) == 10) {
/* 312 */         len--;
/*     */       }
/*     */       
/* 315 */       if (len > 0 && this.lineBuffer.byteAt(len - 1) == 13) {
/* 316 */         len--;
/*     */       }
/*     */     } 
/* 319 */     if (this.decoder == null) {
/* 320 */       charBuffer.append(this.lineBuffer, 0, len);
/*     */     } else {
/* 322 */       ByteBuffer bbuf = ByteBuffer.wrap(this.lineBuffer.array(), 0, len);
/* 323 */       len = appendDecoded(charBuffer, bbuf);
/*     */     } 
/* 325 */     this.lineBuffer.clear();
/* 326 */     return len;
/*     */   }
/*     */ 
/*     */   
/*     */   private int lineFromReadBuffer(CharArrayBuffer charbuffer, int position) throws IOException {
/* 331 */     int pos = position;
/* 332 */     int off = this.bufferPos;
/*     */     
/* 334 */     this.bufferPos = pos + 1;
/* 335 */     if (pos > off && this.buffer[pos - 1] == 13)
/*     */     {
/* 337 */       pos--;
/*     */     }
/* 339 */     int len = pos - off;
/* 340 */     if (this.decoder == null) {
/* 341 */       charbuffer.append(this.buffer, off, len);
/*     */     } else {
/* 343 */       ByteBuffer bbuf = ByteBuffer.wrap(this.buffer, off, len);
/* 344 */       len = appendDecoded(charbuffer, bbuf);
/*     */     } 
/* 346 */     return len;
/*     */   }
/*     */ 
/*     */   
/*     */   private int appendDecoded(CharArrayBuffer charbuffer, ByteBuffer bbuf) throws IOException {
/* 351 */     if (!bbuf.hasRemaining()) {
/* 352 */       return 0;
/*     */     }
/* 354 */     if (this.cbuf == null) {
/* 355 */       this.cbuf = CharBuffer.allocate(1024);
/*     */     }
/* 357 */     this.decoder.reset();
/* 358 */     int len = 0;
/* 359 */     while (bbuf.hasRemaining()) {
/* 360 */       CoderResult coderResult = this.decoder.decode(bbuf, this.cbuf, true);
/* 361 */       len += handleDecodingResult(coderResult, charbuffer);
/*     */     } 
/* 363 */     CoderResult result = this.decoder.flush(this.cbuf);
/* 364 */     len += handleDecodingResult(result, charbuffer);
/* 365 */     this.cbuf.clear();
/* 366 */     return len;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int handleDecodingResult(CoderResult result, CharArrayBuffer charBuffer) throws IOException {
/* 372 */     if (result.isError()) {
/* 373 */       result.throwException();
/*     */     }
/* 375 */     this.cbuf.flip();
/* 376 */     int len = this.cbuf.remaining();
/* 377 */     while (this.cbuf.hasRemaining()) {
/* 378 */       charBuffer.append(this.cbuf.get());
/*     */     }
/* 380 */     this.cbuf.compact();
/* 381 */     return len;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpTransportMetrics getMetrics() {
/* 386 */     return (HttpTransportMetrics)this.metrics;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/io/SessionInputBufferImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */