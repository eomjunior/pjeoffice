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
/*     */ public final class BitArray
/*     */ {
/*     */   public int[] bits;
/*     */   public final int size;
/*     */   
/*     */   public BitArray(int size) {
/*  35 */     if (size < 1) {
/*  36 */       throw new IllegalArgumentException("size must be at least 1");
/*     */     }
/*  38 */     this.size = size;
/*  39 */     this.bits = makeArray(size);
/*     */   }
/*     */   
/*     */   public int getSize() {
/*  43 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean get(int i) {
/*  51 */     return ((this.bits[i >> 5] & 1 << (i & 0x1F)) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(int i) {
/*  60 */     this.bits[i >> 5] = this.bits[i >> 5] | 1 << (i & 0x1F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flip(int i) {
/*  69 */     this.bits[i >> 5] = this.bits[i >> 5] ^ 1 << (i & 0x1F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBulk(int i, int newBits) {
/*  80 */     this.bits[i >> 5] = newBits;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/*  87 */     int max = this.bits.length;
/*  88 */     for (int i = 0; i < max; i++) {
/*  89 */       this.bits[i] = 0;
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
/*     */   public boolean isRange(int start, int end, boolean value) {
/* 103 */     if (end < start) {
/* 104 */       throw new IllegalArgumentException();
/*     */     }
/* 106 */     if (end == start) {
/* 107 */       return true;
/*     */     }
/* 109 */     end--;
/* 110 */     int firstInt = start >> 5;
/* 111 */     int lastInt = end >> 5;
/* 112 */     for (int i = firstInt; i <= lastInt; i++) {
/* 113 */       int mask, firstBit = (i > firstInt) ? 0 : (start & 0x1F);
/* 114 */       int lastBit = (i < lastInt) ? 31 : (end & 0x1F);
/*     */       
/* 116 */       if (firstBit == 0 && lastBit == 31) {
/* 117 */         mask = -1;
/*     */       } else {
/* 119 */         mask = 0;
/* 120 */         for (int j = firstBit; j <= lastBit; j++) {
/* 121 */           mask |= 1 << j;
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 127 */       if ((this.bits[i] & mask) != (value ? mask : 0)) {
/* 128 */         return false;
/*     */       }
/*     */     } 
/* 131 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getBitArray() {
/* 139 */     return this.bits;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reverse() {
/* 146 */     int[] newBits = new int[this.bits.length];
/* 147 */     int size = this.size;
/* 148 */     for (int i = 0; i < size; i++) {
/* 149 */       if (get(size - i - 1)) {
/* 150 */         newBits[i >> 5] = newBits[i >> 5] | 1 << (i & 0x1F);
/*     */       }
/*     */     } 
/* 153 */     this.bits = newBits;
/*     */   }
/*     */   
/*     */   private static int[] makeArray(int size) {
/* 157 */     int arraySize = size >> 5;
/* 158 */     if ((size & 0x1F) != 0) {
/* 159 */       arraySize++;
/*     */     }
/* 161 */     return new int[arraySize];
/*     */   }
/*     */   
/*     */   public String toString() {
/* 165 */     StringBuffer result = new StringBuffer(this.size);
/* 166 */     for (int i = 0; i < this.size; i++) {
/* 167 */       if ((i & 0x7) == 0) {
/* 168 */         result.append(' ');
/*     */       }
/* 170 */       result.append(get(i) ? 88 : 46);
/*     */     } 
/* 172 */     return result.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/qrcode/BitArray.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */