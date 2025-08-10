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
/*     */ public final class GF256
/*     */ {
/*  32 */   public static final GF256 QR_CODE_FIELD = new GF256(285);
/*  33 */   public static final GF256 DATA_MATRIX_FIELD = new GF256(301);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   private final int[] expTable = new int[256];
/*  49 */   private final int[] logTable = new int[256]; private final GF256Poly zero; private GF256(int primitive) {
/*  50 */     int x = 1; int i;
/*  51 */     for (i = 0; i < 256; i++) {
/*  52 */       this.expTable[i] = x;
/*  53 */       x <<= 1;
/*  54 */       if (x >= 256) {
/*  55 */         x ^= primitive;
/*     */       }
/*     */     } 
/*  58 */     for (i = 0; i < 255; i++) {
/*  59 */       this.logTable[this.expTable[i]] = i;
/*     */     }
/*     */     
/*  62 */     this.zero = new GF256Poly(this, new int[] { 0 });
/*  63 */     this.one = new GF256Poly(this, new int[] { 1 });
/*     */   }
/*     */   private final GF256Poly one;
/*     */   GF256Poly getZero() {
/*  67 */     return this.zero;
/*     */   }
/*     */   
/*     */   GF256Poly getOne() {
/*  71 */     return this.one;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   GF256Poly buildMonomial(int degree, int coefficient) {
/*  78 */     if (degree < 0) {
/*  79 */       throw new IllegalArgumentException();
/*     */     }
/*  81 */     if (coefficient == 0) {
/*  82 */       return this.zero;
/*     */     }
/*  84 */     int[] coefficients = new int[degree + 1];
/*  85 */     coefficients[0] = coefficient;
/*  86 */     return new GF256Poly(this, coefficients);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int addOrSubtract(int a, int b) {
/*  95 */     return a ^ b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int exp(int a) {
/* 102 */     return this.expTable[a];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int log(int a) {
/* 109 */     if (a == 0) {
/* 110 */       throw new IllegalArgumentException();
/*     */     }
/* 112 */     return this.logTable[a];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int inverse(int a) {
/* 119 */     if (a == 0) {
/* 120 */       throw new ArithmeticException();
/*     */     }
/* 122 */     return this.expTable[255 - this.logTable[a]];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int multiply(int a, int b) {
/* 131 */     if (a == 0 || b == 0) {
/* 132 */       return 0;
/*     */     }
/* 134 */     if (a == 1) {
/* 135 */       return b;
/*     */     }
/* 137 */     if (b == 1) {
/* 138 */       return a;
/*     */     }
/* 140 */     return this.expTable[(this.logTable[a] + this.logTable[b]) % 255];
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/qrcode/GF256.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */