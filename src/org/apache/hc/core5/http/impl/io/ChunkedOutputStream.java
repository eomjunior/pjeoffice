/*     */ package org.apache.hc.core5.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.function.Supplier;
/*     */ import org.apache.hc.core5.http.FormattedHeader;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.StreamClosedException;
/*     */ import org.apache.hc.core5.http.io.SessionOutputBuffer;
/*     */ import org.apache.hc.core5.http.message.BasicLineFormatter;
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
/*     */ public class ChunkedOutputStream
/*     */   extends OutputStream
/*     */ {
/*     */   private final SessionOutputBuffer buffer;
/*     */   private final OutputStream outputStream;
/*     */   private final byte[] cache;
/*     */   private int cachePosition;
/*     */   private boolean wroteLastChunk;
/*     */   private boolean closed;
/*     */   private final CharArrayBuffer lineBuffer;
/*     */   private final Supplier<List<? extends Header>> trailerSupplier;
/*     */   
/*     */   public ChunkedOutputStream(SessionOutputBuffer buffer, OutputStream outputStream, byte[] chunkCache, Supplier<List<? extends Header>> trailerSupplier) {
/*  83 */     this.buffer = (SessionOutputBuffer)Args.notNull(buffer, "Session output buffer");
/*  84 */     this.outputStream = (OutputStream)Args.notNull(outputStream, "Output stream");
/*  85 */     this.cache = (byte[])Args.notNull(chunkCache, "Chunk cache");
/*  86 */     this.lineBuffer = new CharArrayBuffer(32);
/*  87 */     this.trailerSupplier = trailerSupplier;
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
/*     */   public ChunkedOutputStream(SessionOutputBuffer buffer, OutputStream outputStream, int chunkSizeHint, Supplier<List<? extends Header>> trailerSupplier) {
/* 105 */     this(buffer, outputStream, new byte[(chunkSizeHint > 0) ? chunkSizeHint : 8192], trailerSupplier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChunkedOutputStream(SessionOutputBuffer buffer, OutputStream outputStream, int chunkSizeHint) {
/* 116 */     this(buffer, outputStream, chunkSizeHint, (Supplier<List<? extends Header>>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void flushCache() throws IOException {
/* 123 */     if (this.cachePosition > 0) {
/* 124 */       this.lineBuffer.clear();
/* 125 */       this.lineBuffer.append(Integer.toHexString(this.cachePosition));
/* 126 */       this.buffer.writeLine(this.lineBuffer, this.outputStream);
/* 127 */       this.buffer.write(this.cache, 0, this.cachePosition, this.outputStream);
/* 128 */       this.lineBuffer.clear();
/* 129 */       this.buffer.writeLine(this.lineBuffer, this.outputStream);
/* 130 */       this.cachePosition = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void flushCacheWithAppend(byte[] bufferToAppend, int off, int len) throws IOException {
/* 139 */     this.lineBuffer.clear();
/* 140 */     this.lineBuffer.append(Integer.toHexString(this.cachePosition + len));
/* 141 */     this.buffer.writeLine(this.lineBuffer, this.outputStream);
/* 142 */     this.buffer.write(this.cache, 0, this.cachePosition, this.outputStream);
/* 143 */     this.buffer.write(bufferToAppend, off, len, this.outputStream);
/* 144 */     this.lineBuffer.clear();
/* 145 */     this.buffer.writeLine(this.lineBuffer, this.outputStream);
/* 146 */     this.cachePosition = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeClosingChunk() throws IOException {
/* 151 */     this.lineBuffer.clear();
/* 152 */     this.lineBuffer.append('0');
/* 153 */     this.buffer.writeLine(this.lineBuffer, this.outputStream);
/* 154 */     writeTrailers();
/* 155 */     this.lineBuffer.clear();
/* 156 */     this.buffer.writeLine(this.lineBuffer, this.outputStream);
/*     */   }
/*     */   
/*     */   private void writeTrailers() throws IOException {
/* 160 */     List<? extends Header> trailers = (this.trailerSupplier != null) ? (List<? extends Header>)this.trailerSupplier.get() : null;
/* 161 */     if (trailers != null) {
/* 162 */       for (int i = 0; i < trailers.size(); i++) {
/* 163 */         Header header = trailers.get(i);
/* 164 */         if (header instanceof FormattedHeader) {
/* 165 */           CharArrayBuffer chbuffer = ((FormattedHeader)header).getBuffer();
/* 166 */           this.buffer.writeLine(chbuffer, this.outputStream);
/*     */         } else {
/* 168 */           this.lineBuffer.clear();
/* 169 */           BasicLineFormatter.INSTANCE.formatHeader(this.lineBuffer, header);
/* 170 */           this.buffer.writeLine(this.lineBuffer, this.outputStream);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void finish() throws IOException {
/* 183 */     if (!this.wroteLastChunk) {
/* 184 */       flushCache();
/* 185 */       writeClosingChunk();
/* 186 */       this.wroteLastChunk = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(int b) throws IOException {
/* 193 */     if (this.closed) {
/* 194 */       throw new StreamClosedException();
/*     */     }
/* 196 */     this.cache[this.cachePosition] = (byte)b;
/* 197 */     this.cachePosition++;
/* 198 */     if (this.cachePosition == this.cache.length) {
/* 199 */       flushCache();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(byte[] b) throws IOException {
/* 209 */     write(b, 0, b.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(byte[] src, int off, int len) throws IOException {
/* 218 */     if (this.closed) {
/* 219 */       throw new StreamClosedException();
/*     */     }
/* 221 */     if (len >= this.cache.length - this.cachePosition) {
/* 222 */       flushCacheWithAppend(src, off, len);
/*     */     } else {
/* 224 */       System.arraycopy(src, off, this.cache, this.cachePosition, len);
/* 225 */       this.cachePosition += len;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 234 */     flushCache();
/* 235 */     this.buffer.flush(this.outputStream);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 243 */     if (!this.closed) {
/* 244 */       this.closed = true;
/* 245 */       finish();
/* 246 */       this.buffer.flush(this.outputStream);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/io/ChunkedOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */