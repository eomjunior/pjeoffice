/*     */ package META-INF.versions.9.org.bouncycastle.pqc.math.linearalgebra;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class LittleEndianConversions
/*     */ {
/*     */   public static int OS2IP(byte[] paramArrayOfbyte) {
/*  30 */     return paramArrayOfbyte[0] & 0xFF | (paramArrayOfbyte[1] & 0xFF) << 8 | (paramArrayOfbyte[2] & 0xFF) << 16 | (paramArrayOfbyte[3] & 0xFF) << 24;
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
/*     */   public static int OS2IP(byte[] paramArrayOfbyte, int paramInt) {
/*  44 */     int i = paramArrayOfbyte[paramInt++] & 0xFF;
/*  45 */     i |= (paramArrayOfbyte[paramInt++] & 0xFF) << 8;
/*  46 */     i |= (paramArrayOfbyte[paramInt++] & 0xFF) << 16;
/*  47 */     i |= (paramArrayOfbyte[paramInt] & 0xFF) << 24;
/*  48 */     return i;
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
/*     */   public static int OS2IP(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/*  62 */     int i = 0;
/*  63 */     for (int j = paramInt2 - 1; j >= 0; j--)
/*     */     {
/*  65 */       i |= (paramArrayOfbyte[paramInt1 + j] & 0xFF) << 8 * j;
/*     */     }
/*  67 */     return i;
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
/*     */   public static long OS2LIP(byte[] paramArrayOfbyte, int paramInt) {
/*  80 */     long l = (paramArrayOfbyte[paramInt++] & 0xFF);
/*  81 */     l |= ((paramArrayOfbyte[paramInt++] & 0xFF) << 8);
/*  82 */     l |= ((paramArrayOfbyte[paramInt++] & 0xFF) << 16);
/*  83 */     l |= (paramArrayOfbyte[paramInt++] & 0xFFL) << 24L;
/*  84 */     l |= (paramArrayOfbyte[paramInt++] & 0xFFL) << 32L;
/*  85 */     l |= (paramArrayOfbyte[paramInt++] & 0xFFL) << 40L;
/*  86 */     l |= (paramArrayOfbyte[paramInt++] & 0xFFL) << 48L;
/*  87 */     l |= (paramArrayOfbyte[paramInt++] & 0xFFL) << 56L;
/*  88 */     return l;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] I2OSP(int paramInt) {
/*  99 */     byte[] arrayOfByte = new byte[4];
/* 100 */     arrayOfByte[0] = (byte)paramInt;
/* 101 */     arrayOfByte[1] = (byte)(paramInt >>> 8);
/* 102 */     arrayOfByte[2] = (byte)(paramInt >>> 16);
/* 103 */     arrayOfByte[3] = (byte)(paramInt >>> 24);
/* 104 */     return arrayOfByte;
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
/*     */   public static void I2OSP(int paramInt1, byte[] paramArrayOfbyte, int paramInt2) {
/* 116 */     paramArrayOfbyte[paramInt2++] = (byte)paramInt1;
/* 117 */     paramArrayOfbyte[paramInt2++] = (byte)(paramInt1 >>> 8);
/* 118 */     paramArrayOfbyte[paramInt2++] = (byte)(paramInt1 >>> 16);
/* 119 */     paramArrayOfbyte[paramInt2++] = (byte)(paramInt1 >>> 24);
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
/*     */   public static void I2OSP(int paramInt1, byte[] paramArrayOfbyte, int paramInt2, int paramInt3) {
/* 134 */     for (int i = paramInt3 - 1; i >= 0; i--)
/*     */     {
/* 136 */       paramArrayOfbyte[paramInt2 + i] = (byte)(paramInt1 >>> 8 * i);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] I2OSP(long paramLong) {
/* 148 */     byte[] arrayOfByte = new byte[8];
/* 149 */     arrayOfByte[0] = (byte)(int)paramLong;
/* 150 */     arrayOfByte[1] = (byte)(int)(paramLong >>> 8L);
/* 151 */     arrayOfByte[2] = (byte)(int)(paramLong >>> 16L);
/* 152 */     arrayOfByte[3] = (byte)(int)(paramLong >>> 24L);
/* 153 */     arrayOfByte[4] = (byte)(int)(paramLong >>> 32L);
/* 154 */     arrayOfByte[5] = (byte)(int)(paramLong >>> 40L);
/* 155 */     arrayOfByte[6] = (byte)(int)(paramLong >>> 48L);
/* 156 */     arrayOfByte[7] = (byte)(int)(paramLong >>> 56L);
/* 157 */     return arrayOfByte;
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
/*     */   public static void I2OSP(long paramLong, byte[] paramArrayOfbyte, int paramInt) {
/* 169 */     paramArrayOfbyte[paramInt++] = (byte)(int)paramLong;
/* 170 */     paramArrayOfbyte[paramInt++] = (byte)(int)(paramLong >>> 8L);
/* 171 */     paramArrayOfbyte[paramInt++] = (byte)(int)(paramLong >>> 16L);
/* 172 */     paramArrayOfbyte[paramInt++] = (byte)(int)(paramLong >>> 24L);
/* 173 */     paramArrayOfbyte[paramInt++] = (byte)(int)(paramLong >>> 32L);
/* 174 */     paramArrayOfbyte[paramInt++] = (byte)(int)(paramLong >>> 40L);
/* 175 */     paramArrayOfbyte[paramInt++] = (byte)(int)(paramLong >>> 48L);
/* 176 */     paramArrayOfbyte[paramInt] = (byte)(int)(paramLong >>> 56L);
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
/*     */   public static byte[] toByteArray(int[] paramArrayOfint, int paramInt) {
/* 190 */     int i = paramArrayOfint.length;
/* 191 */     byte[] arrayOfByte = new byte[paramInt];
/* 192 */     byte b1 = 0;
/* 193 */     for (byte b2 = 0; b2 <= i - 2; b2++, b1 += true)
/*     */     {
/* 195 */       I2OSP(paramArrayOfint[b2], arrayOfByte, b1);
/*     */     }
/* 197 */     I2OSP(paramArrayOfint[i - 1], arrayOfByte, b1, paramInt - b1);
/* 198 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int[] toIntArray(byte[] paramArrayOfbyte) {
/* 209 */     int i = (paramArrayOfbyte.length + 3) / 4;
/* 210 */     int j = paramArrayOfbyte.length & 0x3;
/* 211 */     int[] arrayOfInt = new int[i];
/*     */     
/* 213 */     boolean bool = false;
/* 214 */     for (byte b = 0; b <= i - 2; b++, bool += true)
/*     */     {
/* 216 */       arrayOfInt[b] = OS2IP(paramArrayOfbyte, bool);
/*     */     }
/* 218 */     if (j != 0) {
/*     */       
/* 220 */       arrayOfInt[i - 1] = OS2IP(paramArrayOfbyte, bool, j);
/*     */     }
/*     */     else {
/*     */       
/* 224 */       arrayOfInt[i - 1] = OS2IP(paramArrayOfbyte, bool);
/*     */     } 
/*     */     
/* 227 */     return arrayOfInt;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/math/linearalgebra/LittleEndianConversions.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */