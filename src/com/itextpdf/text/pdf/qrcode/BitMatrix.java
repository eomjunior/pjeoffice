/*     */ package com.itextpdf.text.pdf.qrcode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BitMatrix
/*     */ {
/*     */   public final int width;
/*     */   public final int height;
/*     */   public final int rowSize;
/*     */   public final int[] bits;
/*     */   
/*     */   public BitMatrix(int dimension) {
/*  45 */     this(dimension, dimension);
/*     */   }
/*     */   
/*     */   public BitMatrix(int width, int height) {
/*  49 */     if (width < 1 || height < 1) {
/*  50 */       throw new IllegalArgumentException("Both dimensions must be greater than 0");
/*     */     }
/*  52 */     this.width = width;
/*  53 */     this.height = height;
/*  54 */     int rowSize = width >> 5;
/*  55 */     if ((width & 0x1F) != 0) {
/*  56 */       rowSize++;
/*     */     }
/*  58 */     this.rowSize = rowSize;
/*  59 */     this.bits = new int[rowSize * height];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean get(int x, int y) {
/*  70 */     int offset = y * this.rowSize + (x >> 5);
/*  71 */     return ((this.bits[offset] >>> (x & 0x1F) & 0x1) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(int x, int y) {
/*  81 */     int offset = y * this.rowSize + (x >> 5);
/*  82 */     this.bits[offset] = this.bits[offset] | 1 << (x & 0x1F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flip(int x, int y) {
/*  92 */     int offset = y * this.rowSize + (x >> 5);
/*  93 */     this.bits[offset] = this.bits[offset] ^ 1 << (x & 0x1F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 100 */     int max = this.bits.length;
/* 101 */     for (int i = 0; i < max; i++) {
/* 102 */       this.bits[i] = 0;
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
/*     */   public void setRegion(int left, int top, int width, int height) {
/* 115 */     if (top < 0 || left < 0) {
/* 116 */       throw new IllegalArgumentException("Left and top must be nonnegative");
/*     */     }
/* 118 */     if (height < 1 || width < 1) {
/* 119 */       throw new IllegalArgumentException("Height and width must be at least 1");
/*     */     }
/* 121 */     int right = left + width;
/* 122 */     int bottom = top + height;
/* 123 */     if (bottom > this.height || right > this.width) {
/* 124 */       throw new IllegalArgumentException("The region must fit inside the matrix");
/*     */     }
/* 126 */     for (int y = top; y < bottom; y++) {
/* 127 */       int offset = y * this.rowSize;
/* 128 */       for (int x = left; x < right; x++) {
/* 129 */         this.bits[offset + (x >> 5)] = this.bits[offset + (x >> 5)] | 1 << (x & 0x1F);
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
/*     */ 
/*     */   
/*     */   public BitArray getRow(int y, BitArray row) {
/* 143 */     if (row == null || row.getSize() < this.width) {
/* 144 */       row = new BitArray(this.width);
/*     */     }
/* 146 */     int offset = y * this.rowSize;
/* 147 */     for (int x = 0; x < this.rowSize; x++) {
/* 148 */       row.setBulk(x << 5, this.bits[offset + x]);
/*     */     }
/* 150 */     return row;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getWidth() {
/* 157 */     return this.width;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getHeight() {
/* 164 */     return this.height;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDimension() {
/* 174 */     if (this.width != this.height) {
/* 175 */       throw new RuntimeException("Can't call getDimension() on a non-square matrix");
/*     */     }
/* 177 */     return this.width;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 181 */     StringBuffer result = new StringBuffer(this.height * (this.width + 1));
/* 182 */     for (int y = 0; y < this.height; y++) {
/* 183 */       for (int x = 0; x < this.width; x++) {
/* 184 */         result.append(get(x, y) ? "X " : "  ");
/*     */       }
/* 186 */       result.append('\n');
/*     */     } 
/* 188 */     return result.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/qrcode/BitMatrix.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */