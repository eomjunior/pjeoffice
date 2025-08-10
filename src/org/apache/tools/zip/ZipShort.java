/*     */ package org.apache.tools.zip;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*  40 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipShort(byte[] bytes) {
/*  49 */     this(bytes, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipShort(byte[] bytes, int offset) {
/*  59 */     this.value = getValue(bytes, offset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getBytes() {
/*  68 */     byte[] result = new byte[2];
/*  69 */     putShort(this.value, result, 0);
/*  70 */     return result;
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
/*     */   public static void putShort(int value, byte[] buf, int offset) {
/*  82 */     buf[offset] = (byte)(value & 0xFF);
/*  83 */     buf[offset + 1] = (byte)((value & 0xFF00) >> 8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getValue() {
/*  92 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] getBytes(int value) {
/* 101 */     byte[] result = new byte[2];
/* 102 */     result[0] = (byte)(value & 0xFF);
/* 103 */     result[1] = (byte)((value & 0xFF00) >> 8);
/* 104 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getValue(byte[] bytes, int offset) {
/* 114 */     int value = bytes[offset + 1] << 8 & 0xFF00;
/* 115 */     value += bytes[offset] & 0xFF;
/* 116 */     return value;
/*     */   }
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
/*     */   public boolean equals(Object o) {
/* 136 */     return (o instanceof ZipShort && this.value == ((ZipShort)o).getValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 146 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() {
/*     */     try {
/* 152 */       return super.clone();
/* 153 */     } catch (CloneNotSupportedException cnfe) {
/*     */       
/* 155 */       throw new RuntimeException(cnfe);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 161 */     return "ZipShort value: " + this.value;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/zip/ZipShort.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */