/*     */ package com.itextpdf.text.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GetBufferedRandomAccessSource
/*     */   implements RandomAccessSource
/*     */ {
/*     */   private final RandomAccessSource source;
/*     */   private final byte[] getBuffer;
/*  56 */   private long getBufferStart = -1L;
/*  57 */   private long getBufferEnd = -1L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GetBufferedRandomAccessSource(RandomAccessSource source) {
/*  64 */     this.source = source;
/*     */     
/*  66 */     this.getBuffer = new byte[(int)Math.min(Math.max(source.length() / 4L, 1L), 4096L)];
/*  67 */     this.getBufferStart = -1L;
/*  68 */     this.getBufferEnd = -1L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int get(long position) throws IOException {
/*  76 */     if (position < this.getBufferStart || position > this.getBufferEnd) {
/*  77 */       int count = this.source.get(position, this.getBuffer, 0, this.getBuffer.length);
/*  78 */       if (count == -1)
/*  79 */         return -1; 
/*  80 */       this.getBufferStart = position;
/*  81 */       this.getBufferEnd = position + count - 1L;
/*     */     } 
/*  83 */     int bufPos = (int)(position - this.getBufferStart);
/*  84 */     return 0xFF & this.getBuffer[bufPos];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int get(long position, byte[] bytes, int off, int len) throws IOException {
/*  91 */     return this.source.get(position, bytes, off, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long length() {
/*  98 */     return this.source.length();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 105 */     this.source.close();
/* 106 */     this.getBufferStart = -1L;
/* 107 */     this.getBufferEnd = -1L;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/io/GetBufferedRandomAccessSource.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */