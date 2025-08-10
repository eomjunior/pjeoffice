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
/*     */ 
/*     */ 
/*     */ public final class ZipLong
/*     */   implements Cloneable
/*     */ {
/*     */   private static final int BYTE_1 = 1;
/*     */   private static final int BYTE_1_MASK = 65280;
/*     */   private static final int BYTE_1_SHIFT = 8;
/*     */   private static final int BYTE_2 = 2;
/*     */   private static final int BYTE_2_MASK = 16711680;
/*     */   private static final int BYTE_2_SHIFT = 16;
/*     */   private static final int BYTE_3 = 3;
/*     */   private static final long BYTE_3_MASK = 4278190080L;
/*     */   private static final int BYTE_3_SHIFT = 24;
/*     */   private final long value;
/*  52 */   public static final ZipLong CFH_SIG = new ZipLong(33639248L);
/*     */ 
/*     */   
/*  55 */   public static final ZipLong LFH_SIG = new ZipLong(67324752L);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   public static final ZipLong DD_SIG = new ZipLong(134695760L);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   static final ZipLong ZIP64_MAGIC = new ZipLong(4294967295L);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipLong(long value) {
/*  75 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipLong(byte[] bytes) {
/*  85 */     this(bytes, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipLong(byte[] bytes, int offset) {
/*  96 */     this.value = getValue(bytes, offset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getBytes() {
/* 106 */     return getBytes(this.value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getValue() {
/* 116 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] getBytes(long value) {
/* 126 */     byte[] result = new byte[4];
/* 127 */     result[0] = (byte)(int)(value & 0xFFL);
/* 128 */     result[1] = (byte)(int)((value & 0xFF00L) >> 8L);
/* 129 */     result[2] = (byte)(int)((value & 0xFF0000L) >> 16L);
/* 130 */     result[3] = (byte)(int)((value & 0xFF000000L) >> 24L);
/* 131 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long getValue(byte[] bytes, int offset) {
/* 142 */     long value = (bytes[offset + 3] << 24) & 0xFF000000L;
/* 143 */     value += (bytes[offset + 2] << 16 & 0xFF0000);
/* 144 */     value += (bytes[offset + 1] << 8 & 0xFF00);
/* 145 */     value += (bytes[offset] & 0xFF);
/* 146 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long getValue(byte[] bytes) {
/* 156 */     return getValue(bytes, 0);
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
/* 168 */     if (o == null || !(o instanceof ZipLong)) {
/* 169 */       return false;
/*     */     }
/* 171 */     return (this.value == ((ZipLong)o).getValue());
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
/* 182 */     return (int)this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() {
/*     */     try {
/* 188 */       return super.clone();
/*     */     }
/* 190 */     catch (CloneNotSupportedException cnfe) {
/*     */       
/* 192 */       throw new RuntimeException(cnfe);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 198 */     return "ZipLong value: " + this.value;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/extra/ZipLong.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */