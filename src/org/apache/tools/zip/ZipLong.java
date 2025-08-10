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
/*  48 */   public static final ZipLong CFH_SIG = new ZipLong(33639248L);
/*     */ 
/*     */   
/*  51 */   public static final ZipLong LFH_SIG = new ZipLong(67324752L);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   public static final ZipLong DD_SIG = new ZipLong(134695760L);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   static final ZipLong ZIP64_MAGIC = new ZipLong(4294967295L);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipLong(long value) {
/*  70 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipLong(byte[] bytes) {
/*  79 */     this(bytes, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipLong(byte[] bytes, int offset) {
/*  89 */     this.value = getValue(bytes, offset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getBytes() {
/*  98 */     return getBytes(this.value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getValue() {
/* 107 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] getBytes(long value) {
/* 116 */     byte[] result = new byte[4];
/* 117 */     putLong(value, result, 0);
/* 118 */     return result;
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
/*     */   public static void putLong(long value, byte[] buf, int offset) {
/* 130 */     buf[offset++] = (byte)(int)(value & 0xFFL);
/* 131 */     buf[offset++] = (byte)(int)((value & 0xFF00L) >> 8L);
/* 132 */     buf[offset++] = (byte)(int)((value & 0xFF0000L) >> 16L);
/* 133 */     buf[offset] = (byte)(int)((value & 0xFF000000L) >> 24L);
/*     */   }
/*     */   
/*     */   public void putLong(byte[] buf, int offset) {
/* 137 */     putLong(this.value, buf, offset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long getValue(byte[] bytes, int offset) {
/* 147 */     long value = (bytes[offset + 3] << 24) & 0xFF000000L;
/* 148 */     value += (bytes[offset + 2] << 16 & 0xFF0000);
/* 149 */     value += (bytes[offset + 1] << 8 & 0xFF00);
/* 150 */     value += (bytes[offset] & 0xFF);
/* 151 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long getValue(byte[] bytes) {
/* 160 */     return getValue(bytes, 0);
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
/* 171 */     return (o instanceof ZipLong && this.value == ((ZipLong)o).getValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 181 */     return (int)this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() {
/*     */     try {
/* 187 */       return super.clone();
/* 188 */     } catch (CloneNotSupportedException cnfe) {
/*     */       
/* 190 */       throw new RuntimeException(cnfe);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 196 */     return "ZipLong value: " + this.value;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/zip/ZipLong.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */