/*     */ package org.zeroturnaround.zip.extra;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ZipShort
/*     */   implements Cloneable
/*     */ {
/*     */   private static final int BYTE_1_MASK = 65280;
/*     */   private static final int BYTE_1_SHIFT = 8;
/*     */   private final int value;
/*     */   
/*     */   public ZipShort(int value) {
/*  45 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipShort(byte[] bytes) {
/*  55 */     this(bytes, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipShort(byte[] bytes, int offset) {
/*  66 */     this.value = getValue(bytes, offset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getBytes() {
/*  76 */     byte[] result = new byte[2];
/*  77 */     result[0] = (byte)(this.value & 0xFF);
/*  78 */     result[1] = (byte)((this.value & 0xFF00) >> 8);
/*  79 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getValue() {
/*  89 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] getBytes(int value) {
/*  99 */     byte[] result = new byte[2];
/* 100 */     result[0] = (byte)(value & 0xFF);
/* 101 */     result[1] = (byte)((value & 0xFF00) >> 8);
/* 102 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getValue(byte[] bytes, int offset) {
/* 113 */     int value = bytes[offset + 1] << 8 & 0xFF00;
/* 114 */     value += bytes[offset] & 0xFF;
/* 115 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getValue(byte[] bytes) {
/* 125 */     return getValue(bytes, 0);
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
/*     */   public boolean equals(Object o) {
/* 137 */     if (o == null || !(o instanceof ZipShort)) {
/* 138 */       return false;
/*     */     }
/* 140 */     return (this.value == ((ZipShort)o).getValue());
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
/* 151 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() {
/*     */     try {
/* 157 */       return super.clone();
/*     */     }
/* 159 */     catch (CloneNotSupportedException cnfe) {
/*     */       
/* 161 */       throw new RuntimeException(cnfe);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 167 */     return "ZipShort value: " + this.value;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/extra/ZipShort.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */