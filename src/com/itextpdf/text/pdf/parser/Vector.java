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
/*     */ public class Vector
/*     */ {
/*     */   public static final int I1 = 0;
/*     */   public static final int I2 = 1;
/*     */   public static final int I3 = 2;
/*  66 */   private final float[] vals = new float[] { 0.0F, 0.0F, 0.0F };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector(float x, float y, float z) {
/*  77 */     this.vals[0] = x;
/*  78 */     this.vals[1] = y;
/*  79 */     this.vals[2] = z;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float get(int index) {
/*  88 */     return this.vals[index];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector cross(Matrix by) {
/*  98 */     float x = this.vals[0] * by.get(0) + this.vals[1] * by.get(3) + this.vals[2] * by.get(6);
/*  99 */     float y = this.vals[0] * by.get(1) + this.vals[1] * by.get(4) + this.vals[2] * by.get(7);
/* 100 */     float z = this.vals[0] * by.get(2) + this.vals[1] * by.get(5) + this.vals[2] * by.get(8);
/*     */     
/* 102 */     return new Vector(x, y, z);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector subtract(Vector v) {
/* 111 */     float x = this.vals[0] - v.vals[0];
/* 112 */     float y = this.vals[1] - v.vals[1];
/* 113 */     float z = this.vals[2] - v.vals[2];
/*     */     
/* 115 */     return new Vector(x, y, z);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector cross(Vector with) {
/* 124 */     float x = this.vals[1] * with.vals[2] - this.vals[2] * with.vals[1];
/* 125 */     float y = this.vals[2] * with.vals[0] - this.vals[0] * with.vals[2];
/* 126 */     float z = this.vals[0] * with.vals[1] - this.vals[1] * with.vals[0];
/*     */     
/* 128 */     return new Vector(x, y, z);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector normalize() {
/* 137 */     float l = length();
/* 138 */     float x = this.vals[0] / l;
/* 139 */     float y = this.vals[1] / l;
/* 140 */     float z = this.vals[2] / l;
/* 141 */     return new Vector(x, y, z);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector multiply(float by) {
/* 151 */     float x = this.vals[0] * by;
/* 152 */     float y = this.vals[1] * by;
/* 153 */     float z = this.vals[2] * by;
/* 154 */     return new Vector(x, y, z);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float dot(Vector with) {
/* 163 */     return this.vals[0] * with.vals[0] + this.vals[1] * with.vals[1] + this.vals[2] * with.vals[2];
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
/*     */ 
/*     */   
/*     */   public float length() {
/* 180 */     return (float)Math.sqrt(lengthSquared());
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
/*     */   public float lengthSquared() {
/* 194 */     return this.vals[0] * this.vals[0] + this.vals[1] * this.vals[1] + this.vals[2] * this.vals[2];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 202 */     return this.vals[0] + "," + this.vals[1] + "," + this.vals[2];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 210 */     int prime = 31;
/* 211 */     int result = 1;
/* 212 */     result = 31 * result + Arrays.hashCode(this.vals);
/* 213 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 221 */     if (this == obj) {
/* 222 */       return true;
/*     */     }
/* 224 */     if (obj == null) {
/* 225 */       return false;
/*     */     }
/* 227 */     if (getClass() != obj.getClass()) {
/* 228 */       return false;
/*     */     }
/* 230 */     Vector other = (Vector)obj;
/* 231 */     if (!Arrays.equals(this.vals, other.vals)) {
/* 232 */       return false;
/*     */     }
/* 234 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/Vector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */