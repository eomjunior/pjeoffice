/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class MemoryLimitsAwareOutputStream
/*     */   extends ByteArrayOutputStream
/*     */ {
/*     */   private static final int DEFAULT_MAX_STREAM_SIZE = 2147483639;
/*  65 */   private int maxStreamSize = 2147483639;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MemoryLimitsAwareOutputStream() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MemoryLimitsAwareOutputStream(int size) {
/*  83 */     super(size);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getMaxStreamSize() {
/*  92 */     return this.maxStreamSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MemoryLimitsAwareOutputStream setMaxStreamSize(int maxStreamSize) {
/* 102 */     this.maxStreamSize = maxStreamSize;
/* 103 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void write(byte[] b, int off, int len) {
/* 111 */     if (off < 0 || off > b.length || len < 0 || off + len - b.length > 0)
/*     */     {
/* 113 */       throw new IndexOutOfBoundsException();
/*     */     }
/*     */     
/* 116 */     int minCapacity = this.count + len;
/* 117 */     if (minCapacity < 0) {
/* 118 */       throw new MemoryLimitsAwareException("During decompression a single stream occupied more than a maximum integer value. Please check your pdf.");
/*     */     }
/* 120 */     if (minCapacity > this.maxStreamSize) {
/* 121 */       throw new MemoryLimitsAwareException("During decompression a single stream occupied more memory than allowed. Please either check your pdf or increase the allowed multiple decompressed pdf streams maximum size value by setting the appropriate parameter of ReaderProperties's MemoryLimitsAwareHandler.");
/*     */     }
/*     */ 
/*     */     
/* 125 */     int oldCapacity = this.buf.length;
/* 126 */     int newCapacity = oldCapacity << 1;
/* 127 */     if (newCapacity - minCapacity < 0) {
/* 128 */       newCapacity = minCapacity;
/*     */     }
/* 130 */     if (newCapacity < 0) {
/* 131 */       throw new MemoryLimitsAwareException("During decompression a single stream occupied more than a maximum integer value. Please check your pdf.");
/*     */     }
/* 133 */     if (newCapacity - this.maxStreamSize > 0) {
/* 134 */       newCapacity = this.maxStreamSize;
/* 135 */       byte[] copy = new byte[newCapacity];
/* 136 */       System.arraycopy(this.buf, 0, copy, 0, 
/* 137 */           Math.min(this.buf.length, newCapacity));
/* 138 */       this.buf = copy;
/*     */     } 
/* 140 */     super.write(b, off, len);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/MemoryLimitsAwareOutputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */