/*     */ package org.apache.hc.core5.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import org.apache.hc.core5.http.impl.BasicHttpTransportMetrics;
/*     */ import org.apache.hc.core5.http.io.HttpTransportMetrics;
/*     */ import org.apache.hc.core5.http.io.SessionOutputBuffer;
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
/*     */ public class SessionOutputBufferImpl
/*     */   implements SessionOutputBuffer
/*     */ {
/*  57 */   private static final byte[] CRLF = new byte[] { 13, 10 };
/*     */ 
/*     */ 
/*     */   
/*     */   private final BasicHttpTransportMetrics metrics;
/*     */ 
/*     */ 
/*     */   
/*     */   private final ByteArrayBuffer buffer;
/*     */ 
/*     */ 
/*     */   
/*     */   private final int fragmentSizeHint;
/*     */ 
/*     */ 
/*     */   
/*     */   private final CharsetEncoder encoder;
/*     */ 
/*     */ 
/*     */   
/*     */   private ByteBuffer bbuf;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SessionOutputBufferImpl(BasicHttpTransportMetrics metrics, int bufferSize, int fragmentSizeHint, CharsetEncoder charEncoder) {
/*  83 */     Args.positive(bufferSize, "Buffer size");
/*  84 */     Args.notNull(metrics, "HTTP transport metrics");
/*  85 */     this.metrics = metrics;
/*  86 */     this.buffer = new ByteArrayBuffer(bufferSize);
/*  87 */     this.fragmentSizeHint = (fragmentSizeHint >= 0) ? fragmentSizeHint : bufferSize;
/*  88 */     this.encoder = charEncoder;
/*     */   }
/*     */   
/*     */   public SessionOutputBufferImpl(int bufferSize) {
/*  92 */     this(new BasicHttpTransportMetrics(), bufferSize, bufferSize, null);
/*     */   }
/*     */   
/*     */   public SessionOutputBufferImpl(int bufferSize, CharsetEncoder encoder) {
/*  96 */     this(new BasicHttpTransportMetrics(), bufferSize, bufferSize, encoder);
/*     */   }
/*     */ 
/*     */   
/*     */   public int capacity() {
/* 101 */     return this.buffer.capacity();
/*     */   }
/*     */ 
/*     */   
/*     */   public int length() {
/* 106 */     return this.buffer.length();
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() {
/* 111 */     return capacity() - length();
/*     */   }
/*     */   
/*     */   private void flushBuffer(OutputStream outputStream) throws IOException {
/* 115 */     int len = this.buffer.length();
/* 116 */     if (len > 0) {
/* 117 */       outputStream.write(this.buffer.array(), 0, len);
/* 118 */       this.buffer.clear();
/* 119 */       this.metrics.incrementBytesTransferred(len);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush(OutputStream outputStream) throws IOException {
/* 125 */     Args.notNull(outputStream, "Output stream");
/* 126 */     flushBuffer(outputStream);
/* 127 */     outputStream.flush();
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b, int off, int len, OutputStream outputStream) throws IOException {
/* 132 */     if (b == null) {
/*     */       return;
/*     */     }
/* 135 */     Args.notNull(outputStream, "Output stream");
/*     */ 
/*     */ 
/*     */     
/* 139 */     if (len > this.fragmentSizeHint || len > this.buffer.capacity()) {
/*     */       
/* 141 */       flushBuffer(outputStream);
/*     */       
/* 143 */       outputStream.write(b, off, len);
/* 144 */       this.metrics.incrementBytesTransferred(len);
/*     */     } else {
/*     */       
/* 147 */       int freecapacity = this.buffer.capacity() - this.buffer.length();
/* 148 */       if (len > freecapacity)
/*     */       {
/* 150 */         flushBuffer(outputStream);
/*     */       }
/*     */       
/* 153 */       this.buffer.append(b, off, len);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b, OutputStream outputStream) throws IOException {
/* 159 */     if (b == null) {
/*     */       return;
/*     */     }
/* 162 */     write(b, 0, b.length, outputStream);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(int b, OutputStream outputStream) throws IOException {
/* 167 */     Args.notNull(outputStream, "Output stream");
/* 168 */     if (this.fragmentSizeHint > 0) {
/* 169 */       if (this.buffer.isFull()) {
/* 170 */         flushBuffer(outputStream);
/*     */       }
/* 172 */       this.buffer.append(b);
/*     */     } else {
/* 174 */       flushBuffer(outputStream);
/* 175 */       outputStream.write(b);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeLine(CharArrayBuffer charbuffer, OutputStream outputStream) throws IOException {
/* 190 */     if (charbuffer == null) {
/*     */       return;
/*     */     }
/* 193 */     Args.notNull(outputStream, "Output stream");
/* 194 */     if (this.encoder == null) {
/* 195 */       int off = 0;
/* 196 */       int remaining = charbuffer.length();
/* 197 */       while (remaining > 0) {
/* 198 */         int chunk = this.buffer.capacity() - this.buffer.length();
/* 199 */         chunk = Math.min(chunk, remaining);
/* 200 */         if (chunk > 0) {
/* 201 */           this.buffer.append(charbuffer, off, chunk);
/*     */         }
/* 203 */         if (this.buffer.isFull()) {
/* 204 */           flushBuffer(outputStream);
/*     */         }
/* 206 */         off += chunk;
/* 207 */         remaining -= chunk;
/*     */       } 
/*     */     } else {
/* 210 */       CharBuffer cbuf = CharBuffer.wrap(charbuffer.array(), 0, charbuffer.length());
/* 211 */       writeEncoded(cbuf, outputStream);
/*     */     } 
/* 213 */     write(CRLF, outputStream);
/*     */   }
/*     */   
/*     */   private void writeEncoded(CharBuffer cbuf, OutputStream outputStream) throws IOException {
/* 217 */     if (!cbuf.hasRemaining()) {
/*     */       return;
/*     */     }
/* 220 */     if (this.bbuf == null) {
/* 221 */       this.bbuf = ByteBuffer.allocate(1024);
/*     */     }
/* 223 */     this.encoder.reset();
/* 224 */     while (cbuf.hasRemaining()) {
/* 225 */       CoderResult coderResult = this.encoder.encode(cbuf, this.bbuf, true);
/* 226 */       handleEncodingResult(coderResult, outputStream);
/*     */     } 
/* 228 */     CoderResult result = this.encoder.flush(this.bbuf);
/* 229 */     handleEncodingResult(result, outputStream);
/* 230 */     this.bbuf.clear();
/*     */   }
/*     */   
/*     */   private void handleEncodingResult(CoderResult result, OutputStream outputStream) throws IOException {
/* 234 */     if (result.isError()) {
/* 235 */       result.throwException();
/*     */     }
/* 237 */     this.bbuf.flip();
/* 238 */     while (this.bbuf.hasRemaining()) {
/* 239 */       write(this.bbuf.get(), outputStream);
/*     */     }
/* 241 */     this.bbuf.compact();
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpTransportMetrics getMetrics() {
/* 246 */     return (HttpTransportMetrics)this.metrics;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/io/SessionOutputBufferImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */