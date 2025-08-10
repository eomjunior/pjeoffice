/*     */ package com.itextpdf.text.pdf.parser;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Matrix
/*     */ {
/*     */   public static final int I11 = 0;
/*     */   public static final int I12 = 1;
/*     */   public static final int I13 = 2;
/*     */   public static final int I21 = 3;
/*     */   public static final int I22 = 4;
/*     */   public static final int I23 = 5;
/*     */   public static final int I31 = 6;
/*     */   public static final int I32 = 7;
/*     */   public static final int I33 = 8;
/*  80 */   private final float[] vals = new float[] { 1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix(float tx, float ty) {
/*  98 */     this.vals[6] = tx;
/*  99 */     this.vals[7] = ty;
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
/*     */   public Matrix(float a, float b, float c, float d, float e, float f) {
/* 112 */     this.vals[0] = a;
/* 113 */     this.vals[1] = b;
/* 114 */     this.vals[2] = 0.0F;
/* 115 */     this.vals[3] = c;
/* 116 */     this.vals[4] = d;
/* 117 */     this.vals[5] = 0.0F;
/* 118 */     this.vals[6] = e;
/* 119 */     this.vals[7] = f;
/* 120 */     this.vals[8] = 1.0F;
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
/*     */   public float get(int index) {
/* 135 */     return this.vals[index];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix multiply(Matrix by) {
/* 145 */     Matrix rslt = new Matrix();
/*     */     
/* 147 */     float[] a = this.vals;
/* 148 */     float[] b = by.vals;
/* 149 */     float[] c = rslt.vals;
/*     */     
/* 151 */     c[0] = a[0] * b[0] + a[1] * b[3] + a[2] * b[6];
/* 152 */     c[1] = a[0] * b[1] + a[1] * b[4] + a[2] * b[7];
/* 153 */     c[2] = a[0] * b[2] + a[1] * b[5] + a[2] * b[8];
/* 154 */     c[3] = a[3] * b[0] + a[4] * b[3] + a[5] * b[6];
/* 155 */     c[4] = a[3] * b[1] + a[4] * b[4] + a[5] * b[7];
/* 156 */     c[5] = a[3] * b[2] + a[4] * b[5] + a[5] * b[8];
/* 157 */     c[6] = a[6] * b[0] + a[7] * b[3] + a[8] * b[6];
/* 158 */     c[7] = a[6] * b[1] + a[7] * b[4] + a[8] * b[7];
/* 159 */     c[8] = a[6] * b[2] + a[7] * b[5] + a[8] * b[8];
/*     */     
/* 161 */     return rslt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix subtract(Matrix arg) {
/* 170 */     Matrix rslt = new Matrix();
/*     */     
/* 172 */     float[] a = this.vals;
/* 173 */     float[] b = arg.vals;
/* 174 */     float[] c = rslt.vals;
/*     */     
/* 176 */     c[0] = a[0] - b[0];
/* 177 */     c[1] = a[1] - b[1];
/* 178 */     c[2] = a[2] - b[2];
/* 179 */     c[3] = a[3] - b[3];
/* 180 */     c[4] = a[4] - b[4];
/* 181 */     c[5] = a[5] - b[5];
/* 182 */     c[6] = a[6] - b[6];
/* 183 */     c[7] = a[7] - b[7];
/* 184 */     c[8] = a[8] - b[8];
/*     */     
/* 186 */     return rslt;
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
/*     */   public float getDeterminant() {
/* 198 */     return this.vals[0] * this.vals[4] * this.vals[8] + this.vals[1] * this.vals[5] * this.vals[6] + this.vals[2] * this.vals[3] * this.vals[7] - this.vals[0] * this.vals[5] * this.vals[7] - this.vals[1] * this.vals[3] * this.vals[8] - this.vals[2] * this.vals[4] * this.vals[6];
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
/*     */   public boolean equals(Object obj) {
/* 213 */     if (!(obj instanceof Matrix)) {
/* 214 */       return false;
/*     */     }
/* 216 */     return Arrays.equals(this.vals, ((Matrix)obj).vals);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 227 */     int result = 1;
/* 228 */     for (int i = 0; i < this.vals.length; i++) {
/* 229 */       result = 31 * result + Float.floatToIntBits(this.vals[i]);
/*     */     }
/* 231 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 240 */     return this.vals[0] + "\t" + this.vals[1] + "\t" + this.vals[2] + "\n" + this.vals[3] + "\t" + this.vals[4] + "\t" + this.vals[2] + "\n" + this.vals[6] + "\t" + this.vals[7] + "\t" + this.vals[8];
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/Matrix.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */