/*     */ package com.itextpdf.awt.geom.misc;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class HashCode
/*     */ {
/*     */   public static final int EMPTY_HASH_CODE = 1;
/*  75 */   private int hashCode = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/*  81 */     return this.hashCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int combine(int hashCode, boolean value) {
/*  91 */     int v = value ? 1231 : 1237;
/*  92 */     return combine(hashCode, v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int combine(int hashCode, long value) {
/* 102 */     int v = (int)(value ^ value >>> 32L);
/* 103 */     return combine(hashCode, v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int combine(int hashCode, float value) {
/* 113 */     int v = Float.floatToIntBits(value);
/* 114 */     return combine(hashCode, v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int combine(int hashCode, double value) {
/* 124 */     long v = Double.doubleToLongBits(value);
/* 125 */     return combine(hashCode, v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int combine(int hashCode, Object value) {
/* 135 */     return combine(hashCode, value.hashCode());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int combine(int hashCode, int value) {
/* 145 */     return 31 * hashCode + value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HashCode append(int value) {
/* 154 */     this.hashCode = combine(this.hashCode, value);
/* 155 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HashCode append(long value) {
/* 164 */     this.hashCode = combine(this.hashCode, value);
/* 165 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HashCode append(float value) {
/* 174 */     this.hashCode = combine(this.hashCode, value);
/* 175 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HashCode append(double value) {
/* 184 */     this.hashCode = combine(this.hashCode, value);
/* 185 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HashCode append(boolean value) {
/* 194 */     this.hashCode = combine(this.hashCode, value);
/* 195 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HashCode append(Object value) {
/* 204 */     this.hashCode = combine(this.hashCode, value);
/* 205 */     return this;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/awt/geom/misc/HashCode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */