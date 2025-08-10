/*     */ package com.itextpdf.text.pdf.hyphenation;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CharVector
/*     */   implements Cloneable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -4875768298308363544L;
/*     */   private static final int DEFAULT_BLOCK_SIZE = 2048;
/*     */   private int blockSize;
/*     */   private char[] array;
/*     */   private int n;
/*     */   
/*     */   public CharVector() {
/*  47 */     this(2048);
/*     */   }
/*     */   
/*     */   public CharVector(int capacity) {
/*  51 */     if (capacity > 0) {
/*  52 */       this.blockSize = capacity;
/*     */     } else {
/*  54 */       this.blockSize = 2048;
/*     */     } 
/*  56 */     this.array = new char[this.blockSize];
/*  57 */     this.n = 0;
/*     */   }
/*     */   
/*     */   public CharVector(char[] a) {
/*  61 */     this.blockSize = 2048;
/*  62 */     this.array = a;
/*  63 */     this.n = a.length;
/*     */   }
/*     */   
/*     */   public CharVector(char[] a, int capacity) {
/*  67 */     if (capacity > 0) {
/*  68 */       this.blockSize = capacity;
/*     */     } else {
/*  70 */       this.blockSize = 2048;
/*     */     } 
/*  72 */     this.array = a;
/*  73 */     this.n = a.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/*  80 */     this.n = 0;
/*     */   }
/*     */   
/*     */   public Object clone() {
/*  84 */     CharVector cv = new CharVector((char[])this.array.clone(), this.blockSize);
/*  85 */     cv.n = this.n;
/*  86 */     return cv;
/*     */   }
/*     */   
/*     */   public char[] getArray() {
/*  90 */     return this.array;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/*  97 */     return this.n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int capacity() {
/* 104 */     return this.array.length;
/*     */   }
/*     */   
/*     */   public void put(int index, char val) {
/* 108 */     this.array[index] = val;
/*     */   }
/*     */   
/*     */   public char get(int index) {
/* 112 */     return this.array[index];
/*     */   }
/*     */   
/*     */   public int alloc(int size) {
/* 116 */     int index = this.n;
/* 117 */     int len = this.array.length;
/* 118 */     if (this.n + size >= len) {
/* 119 */       char[] aux = new char[len + this.blockSize];
/* 120 */       System.arraycopy(this.array, 0, aux, 0, len);
/* 121 */       this.array = aux;
/*     */     } 
/* 123 */     this.n += size;
/* 124 */     return index;
/*     */   }
/*     */   
/*     */   public void trimToSize() {
/* 128 */     if (this.n < this.array.length) {
/* 129 */       char[] aux = new char[this.n];
/* 130 */       System.arraycopy(this.array, 0, aux, 0, this.n);
/* 131 */       this.array = aux;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/hyphenation/CharVector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */