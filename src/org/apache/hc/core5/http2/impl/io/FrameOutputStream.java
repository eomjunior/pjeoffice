/*     */ package org.apache.hc.core5.http2.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class FrameOutputStream
/*     */   extends OutputStream
/*     */ {
/*     */   private final OutputStream outputStream;
/*     */   private final byte[] cache;
/*     */   private int cachePosition;
/*     */   
/*     */   public FrameOutputStream(int minChunkSize, OutputStream outputStream) {
/*  48 */     this.outputStream = (OutputStream)Args.notNull(outputStream, "Output stream");
/*  49 */     this.cache = new byte[minChunkSize];
/*     */   }
/*     */   
/*     */   protected abstract void write(ByteBuffer paramByteBuffer, boolean paramBoolean, OutputStream paramOutputStream) throws IOException;
/*     */   
/*     */   private void flushCache(boolean endStream) throws IOException {
/*  55 */     if (this.cachePosition > 0) {
/*  56 */       write(ByteBuffer.wrap(this.cache, 0, this.cachePosition), endStream, this.outputStream);
/*  57 */       this.cachePosition = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(int b) throws IOException {
/*  63 */     this.cache[this.cachePosition] = (byte)b;
/*  64 */     this.cachePosition++;
/*  65 */     if (this.cachePosition == this.cache.length) {
/*  66 */       flushCache(false);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b) throws IOException {
/*  72 */     write(b, 0, b.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] src, int off, int len) throws IOException {
/*  77 */     if (len >= this.cache.length - this.cachePosition) {
/*  78 */       flushCache(false);
/*  79 */       write(ByteBuffer.wrap(src, off, len), false, this.outputStream);
/*     */     } else {
/*  81 */       System.arraycopy(src, off, this.cache, this.cachePosition, len);
/*  82 */       this.cachePosition += len;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/*  88 */     flushCache(false);
/*  89 */     this.outputStream.flush();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  94 */     if (this.cachePosition > 0) {
/*  95 */       flushCache(true);
/*     */     } else {
/*  97 */       write((ByteBuffer)null, true, this.outputStream);
/*     */     } 
/*  99 */     flushCache(true);
/* 100 */     this.outputStream.flush();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/io/FrameOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */