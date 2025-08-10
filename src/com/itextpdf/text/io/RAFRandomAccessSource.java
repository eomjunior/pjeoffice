/*     */ package com.itextpdf.text.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class RAFRandomAccessSource
/*     */   implements RandomAccessSource
/*     */ {
/*     */   private final RandomAccessFile raf;
/*     */   private final long length;
/*     */   
/*     */   public RAFRandomAccessSource(RandomAccessFile raf) throws IOException {
/*  70 */     this.raf = raf;
/*  71 */     this.length = raf.length();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int get(long position) throws IOException {
/*  79 */     if (position > this.raf.length()) {
/*  80 */       return -1;
/*     */     }
/*     */     
/*  83 */     this.raf.seek(position);
/*     */     
/*  85 */     return this.raf.read();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int get(long position, byte[] bytes, int off, int len) throws IOException {
/*  92 */     if (position > this.length) {
/*  93 */       return -1;
/*     */     }
/*     */     
/*  96 */     this.raf.seek(position);
/*     */     
/*  98 */     return this.raf.read(bytes, off, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long length() {
/* 107 */     return this.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 114 */     this.raf.close();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/io/RAFRandomAccessSource.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */