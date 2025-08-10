/*     */ package META-INF.versions.9.org.bouncycastle.pqc.math.linearalgebra;
/*     */ 
/*     */ import org.bouncycastle.pqc.math.linearalgebra.IntegerFunctions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BigEndianConversions
/*     */ {
/*     */   public static byte[] I2OSP(int paramInt) {
/*  30 */     byte[] arrayOfByte = new byte[4];
/*  31 */     arrayOfByte[0] = (byte)(paramInt >>> 24);
/*  32 */     arrayOfByte[1] = (byte)(paramInt >>> 16);
/*  33 */     arrayOfByte[2] = (byte)(paramInt >>> 8);
/*  34 */     arrayOfByte[3] = (byte)paramInt;
/*  35 */     return arrayOfByte;
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
/*     */   
/*     */   public static byte[] I2OSP(int paramInt1, int paramInt2) throws ArithmeticException {
/*  53 */     if (paramInt1 < 0)
/*     */     {
/*  55 */       return null;
/*     */     }
/*  57 */     int i = IntegerFunctions.ceilLog256(paramInt1);
/*  58 */     if (i > paramInt2)
/*     */     {
/*  60 */       throw new ArithmeticException("Cannot encode given integer into specified number of octets.");
/*     */     }
/*     */     
/*  63 */     byte[] arrayOfByte = new byte[paramInt2];
/*  64 */     for (int j = paramInt2 - 1; j >= paramInt2 - i; j--)
/*     */     {
/*  66 */       arrayOfByte[j] = (byte)(paramInt1 >>> 8 * (paramInt2 - 1 - j));
/*     */     }
/*  68 */     return arrayOfByte;
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
/*     */   public static void I2OSP(int paramInt1, byte[] paramArrayOfbyte, int paramInt2) {
/*  81 */     paramArrayOfbyte[paramInt2++] = (byte)(paramInt1 >>> 24);
/*  82 */     paramArrayOfbyte[paramInt2++] = (byte)(paramInt1 >>> 16);
/*  83 */     paramArrayOfbyte[paramInt2++] = (byte)(paramInt1 >>> 8);
/*  84 */     paramArrayOfbyte[paramInt2] = (byte)paramInt1;
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
/*     */   public static byte[] I2OSP(long paramLong) {
/*  96 */     byte[] arrayOfByte = new byte[8];
/*  97 */     arrayOfByte[0] = (byte)(int)(paramLong >>> 56L);
/*  98 */     arrayOfByte[1] = (byte)(int)(paramLong >>> 48L);
/*  99 */     arrayOfByte[2] = (byte)(int)(paramLong >>> 40L);
/* 100 */     arrayOfByte[3] = (byte)(int)(paramLong >>> 32L);
/* 101 */     arrayOfByte[4] = (byte)(int)(paramLong >>> 24L);
/* 102 */     arrayOfByte[5] = (byte)(int)(paramLong >>> 16L);
/* 103 */     arrayOfByte[6] = (byte)(int)(paramLong >>> 8L);
/* 104 */     arrayOfByte[7] = (byte)(int)paramLong;
/* 105 */     return arrayOfByte;
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
/*     */   public static void I2OSP(long paramLong, byte[] paramArrayOfbyte, int paramInt) {
/* 118 */     paramArrayOfbyte[paramInt++] = (byte)(int)(paramLong >>> 56L);
/* 119 */     paramArrayOfbyte[paramInt++] = (byte)(int)(paramLong >>> 48L);
/* 120 */     paramArrayOfbyte[paramInt++] = (byte)(int)(paramLong >>> 40L);
/* 121 */     paramArrayOfbyte[paramInt++] = (byte)(int)(paramLong >>> 32L);
/* 122 */     paramArrayOfbyte[paramInt++] = (byte)(int)(paramLong >>> 24L);
/* 123 */     paramArrayOfbyte[paramInt++] = (byte)(int)(paramLong >>> 16L);
/* 124 */     paramArrayOfbyte[paramInt++] = (byte)(int)(paramLong >>> 8L);
/* 125 */     paramArrayOfbyte[paramInt] = (byte)(int)paramLong;
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
/*     */   public static void I2OSP(int paramInt1, byte[] paramArrayOfbyte, int paramInt2, int paramInt3) {
/* 141 */     for (int i = paramInt3 - 1; i >= 0; i--)
/*     */     {
/* 143 */       paramArrayOfbyte[paramInt2 + i] = (byte)(paramInt1 >>> 8 * (paramInt3 - 1 - i));
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int OS2IP(byte[] paramArrayOfbyte) {
/* 159 */     if (paramArrayOfbyte.length > 4)
/*     */     {
/* 161 */       throw new ArithmeticException("invalid input length");
/*     */     }
/* 163 */     if (paramArrayOfbyte.length == 0)
/*     */     {
/* 165 */       return 0;
/*     */     }
/* 167 */     int i = 0;
/* 168 */     for (byte b = 0; b < paramArrayOfbyte.length; b++)
/*     */     {
/* 170 */       i |= (paramArrayOfbyte[b] & 0xFF) << 8 * (paramArrayOfbyte.length - 1 - b);
/*     */     }
/* 172 */     return i;
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
/*     */   public static int OS2IP(byte[] paramArrayOfbyte, int paramInt) {
/* 185 */     int i = (paramArrayOfbyte[paramInt++] & 0xFF) << 24;
/* 186 */     i |= (paramArrayOfbyte[paramInt++] & 0xFF) << 16;
/* 187 */     i |= (paramArrayOfbyte[paramInt++] & 0xFF) << 8;
/* 188 */     i |= paramArrayOfbyte[paramInt] & 0xFF;
/* 189 */     return i;
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
/*     */   public static int OS2IP(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/* 206 */     if (paramArrayOfbyte.length == 0 || paramArrayOfbyte.length < paramInt1 + paramInt2 - 1)
/*     */     {
/* 208 */       return 0;
/*     */     }
/* 210 */     int i = 0;
/* 211 */     for (byte b = 0; b < paramInt2; b++)
/*     */     {
/* 213 */       i |= (paramArrayOfbyte[paramInt1 + b] & 0xFF) << 8 * (paramInt2 - b - 1);
/*     */     }
/* 215 */     return i;
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
/* 228 */     long l = (paramArrayOfbyte[paramInt++] & 0xFFL) << 56L;
/* 229 */     l |= (paramArrayOfbyte[paramInt++] & 0xFFL) << 48L;
/* 230 */     l |= (paramArrayOfbyte[paramInt++] & 0xFFL) << 40L;
/* 231 */     l |= (paramArrayOfbyte[paramInt++] & 0xFFL) << 32L;
/* 232 */     l |= (paramArrayOfbyte[paramInt++] & 0xFFL) << 24L;
/* 233 */     l |= ((paramArrayOfbyte[paramInt++] & 0xFF) << 16);
/* 234 */     l |= ((paramArrayOfbyte[paramInt++] & 0xFF) << 8);
/* 235 */     l |= (paramArrayOfbyte[paramInt] & 0xFF);
/* 236 */     return l;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] toByteArray(int[] paramArrayOfint) {
/* 247 */     byte[] arrayOfByte = new byte[paramArrayOfint.length << 2];
/* 248 */     for (byte b = 0; b < paramArrayOfint.length; b++)
/*     */     {
/* 250 */       I2OSP(paramArrayOfint[b], arrayOfByte, b << 2);
/*     */     }
/* 252 */     return arrayOfByte;
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
/* 266 */     int i = paramArrayOfint.length;
/* 267 */     byte[] arrayOfByte = new byte[paramInt];
/* 268 */     byte b1 = 0;
/* 269 */     for (byte b2 = 0; b2 <= i - 2; b2++, b1 += true)
/*     */     {
/* 271 */       I2OSP(paramArrayOfint[b2], arrayOfByte, b1);
/*     */     }
/* 273 */     I2OSP(paramArrayOfint[i - 1], arrayOfByte, b1, paramInt - b1);
/* 274 */     return arrayOfByte;
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
/* 285 */     int i = (paramArrayOfbyte.length + 3) / 4;
/* 286 */     int j = paramArrayOfbyte.length & 0x3;
/* 287 */     int[] arrayOfInt = new int[i];
/*     */     
/* 289 */     boolean bool = false;
/* 290 */     for (byte b = 0; b <= i - 2; b++, bool += true)
/*     */     {
/* 292 */       arrayOfInt[b] = OS2IP(paramArrayOfbyte, bool);
/*     */     }
/* 294 */     if (j != 0) {
/*     */       
/* 296 */       arrayOfInt[i - 1] = OS2IP(paramArrayOfbyte, bool, j);
/*     */     }
/*     */     else {
/*     */       
/* 300 */       arrayOfInt[i - 1] = OS2IP(paramArrayOfbyte, bool);
/*     */     } 
/*     */     
/* 303 */     return arrayOfInt;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/math/linearalgebra/BigEndianConversions.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */