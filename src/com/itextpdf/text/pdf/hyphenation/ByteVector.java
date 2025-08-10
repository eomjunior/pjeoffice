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
/*     */ public class ByteVector
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -1096301185375029343L;
/*     */   private static final int DEFAULT_BLOCK_SIZE = 2048;
/*     */   private int blockSize;
/*     */   private byte[] array;
/*     */   private int n;
/*     */   
/*     */   public ByteVector() {
/*  47 */     this(2048);
/*     */   }
/*     */   
/*     */   public ByteVector(int capacity) {
/*  51 */     if (capacity > 0) {
/*  52 */       this.blockSize = capacity;
/*     */     } else {
/*  54 */       this.blockSize = 2048;
/*     */     } 
/*  56 */     this.array = new byte[this.blockSize];
/*  57 */     this.n = 0;
/*     */   }
/*     */   
/*     */   public ByteVector(byte[] a) {
/*  61 */     this.blockSize = 2048;
/*  62 */     this.array = a;
/*  63 */     this.n = 0;
/*     */   }
/*     */   
/*     */   public ByteVector(byte[] a, int capacity) {
/*  67 */     if (capacity > 0) {
/*  68 */       this.blockSize = capacity;
/*     */     } else {
/*  70 */       this.blockSize = 2048;
/*     */     } 
/*  72 */     this.array = a;
/*  73 */     this.n = 0;
/*     */   }
/*     */   
/*     */   public byte[] getArray() {
/*  77 */     return this.array;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/*  84 */     return this.n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int capacity() {
/*  91 */     return this.array.length;
/*     */   }
/*     */   
/*     */   public void put(int index, byte val) {
/*  95 */     this.array[index] = val;
/*     */   }
/*     */   
/*     */   public byte get(int index) {
/*  99 */     return this.array[index];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int alloc(int size) {
/* 106 */     int index = this.n;
/* 107 */     int len = this.array.length;
/* 108 */     if (this.n + size >= len) {
/* 109 */       byte[] aux = new byte[len + this.blockSize];
/* 110 */       System.arraycopy(this.array, 0, aux, 0, len);
/* 111 */       this.array = aux;
/*     */     } 
/* 113 */     this.n += size;
/* 114 */     return index;
/*     */   }
/*     */   
/*     */   public void trimToSize() {
/* 118 */     if (this.n < this.array.length) {
/* 119 */       byte[] aux = new byte[this.n];
/* 120 */       System.arraycopy(this.array, 0, aux, 0, this.n);
/* 121 */       this.array = aux;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/hyphenation/ByteVector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */