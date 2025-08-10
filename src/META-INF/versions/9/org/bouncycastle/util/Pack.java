/*     */ package META-INF.versions.9.org.bouncycastle.util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Pack
/*     */ {
/*     */   public static short bigEndianToShort(byte[] paramArrayOfbyte, int paramInt) {
/*  10 */     int i = (paramArrayOfbyte[paramInt] & 0xFF) << 8;
/*  11 */     i |= paramArrayOfbyte[++paramInt] & 0xFF;
/*  12 */     return (short)i;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int bigEndianToInt(byte[] paramArrayOfbyte, int paramInt) {
/*  17 */     int i = paramArrayOfbyte[paramInt] << 24;
/*  18 */     i |= (paramArrayOfbyte[++paramInt] & 0xFF) << 16;
/*  19 */     i |= (paramArrayOfbyte[++paramInt] & 0xFF) << 8;
/*  20 */     i |= paramArrayOfbyte[++paramInt] & 0xFF;
/*  21 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void bigEndianToInt(byte[] paramArrayOfbyte, int paramInt, int[] paramArrayOfint) {
/*  26 */     for (byte b = 0; b < paramArrayOfint.length; b++) {
/*     */       
/*  28 */       paramArrayOfint[b] = bigEndianToInt(paramArrayOfbyte, paramInt);
/*  29 */       paramInt += 4;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void bigEndianToInt(byte[] paramArrayOfbyte, int paramInt1, int[] paramArrayOfint, int paramInt2, int paramInt3) {
/*  35 */     for (byte b = 0; b < paramInt3; b++) {
/*     */       
/*  37 */       paramArrayOfint[paramInt2 + b] = bigEndianToInt(paramArrayOfbyte, paramInt1);
/*  38 */       paramInt1 += 4;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] intToBigEndian(int paramInt) {
/*  44 */     byte[] arrayOfByte = new byte[4];
/*  45 */     intToBigEndian(paramInt, arrayOfByte, 0);
/*  46 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void intToBigEndian(int paramInt1, byte[] paramArrayOfbyte, int paramInt2) {
/*  51 */     paramArrayOfbyte[paramInt2] = (byte)(paramInt1 >>> 24);
/*  52 */     paramArrayOfbyte[++paramInt2] = (byte)(paramInt1 >>> 16);
/*  53 */     paramArrayOfbyte[++paramInt2] = (byte)(paramInt1 >>> 8);
/*  54 */     paramArrayOfbyte[++paramInt2] = (byte)paramInt1;
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] intToBigEndian(int[] paramArrayOfint) {
/*  59 */     byte[] arrayOfByte = new byte[4 * paramArrayOfint.length];
/*  60 */     intToBigEndian(paramArrayOfint, arrayOfByte, 0);
/*  61 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void intToBigEndian(int[] paramArrayOfint, byte[] paramArrayOfbyte, int paramInt) {
/*  66 */     for (byte b = 0; b < paramArrayOfint.length; b++) {
/*     */       
/*  68 */       intToBigEndian(paramArrayOfint[b], paramArrayOfbyte, paramInt);
/*  69 */       paramInt += 4;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void intToBigEndian(int[] paramArrayOfint, int paramInt1, int paramInt2, byte[] paramArrayOfbyte, int paramInt3) {
/*  75 */     for (byte b = 0; b < paramInt2; b++) {
/*     */       
/*  77 */       intToBigEndian(paramArrayOfint[paramInt1 + b], paramArrayOfbyte, paramInt3);
/*  78 */       paramInt3 += 4;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static long bigEndianToLong(byte[] paramArrayOfbyte, int paramInt) {
/*  84 */     int i = bigEndianToInt(paramArrayOfbyte, paramInt);
/*  85 */     int j = bigEndianToInt(paramArrayOfbyte, paramInt + 4);
/*  86 */     return (i & 0xFFFFFFFFL) << 32L | j & 0xFFFFFFFFL;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void bigEndianToLong(byte[] paramArrayOfbyte, int paramInt, long[] paramArrayOflong) {
/*  91 */     for (byte b = 0; b < paramArrayOflong.length; b++) {
/*     */       
/*  93 */       paramArrayOflong[b] = bigEndianToLong(paramArrayOfbyte, paramInt);
/*  94 */       paramInt += 8;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void bigEndianToLong(byte[] paramArrayOfbyte, int paramInt1, long[] paramArrayOflong, int paramInt2, int paramInt3) {
/* 100 */     for (byte b = 0; b < paramInt3; b++) {
/*     */       
/* 102 */       paramArrayOflong[paramInt2 + b] = bigEndianToLong(paramArrayOfbyte, paramInt1);
/* 103 */       paramInt1 += 8;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] longToBigEndian(long paramLong) {
/* 109 */     byte[] arrayOfByte = new byte[8];
/* 110 */     longToBigEndian(paramLong, arrayOfByte, 0);
/* 111 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void longToBigEndian(long paramLong, byte[] paramArrayOfbyte, int paramInt) {
/* 116 */     intToBigEndian((int)(paramLong >>> 32L), paramArrayOfbyte, paramInt);
/* 117 */     intToBigEndian((int)(paramLong & 0xFFFFFFFFL), paramArrayOfbyte, paramInt + 4);
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] longToBigEndian(long[] paramArrayOflong) {
/* 122 */     byte[] arrayOfByte = new byte[8 * paramArrayOflong.length];
/* 123 */     longToBigEndian(paramArrayOflong, arrayOfByte, 0);
/* 124 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void longToBigEndian(long[] paramArrayOflong, byte[] paramArrayOfbyte, int paramInt) {
/* 129 */     for (byte b = 0; b < paramArrayOflong.length; b++) {
/*     */       
/* 131 */       longToBigEndian(paramArrayOflong[b], paramArrayOfbyte, paramInt);
/* 132 */       paramInt += 8;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void longToBigEndian(long[] paramArrayOflong, int paramInt1, int paramInt2, byte[] paramArrayOfbyte, int paramInt3) {
/* 138 */     for (byte b = 0; b < paramInt2; b++) {
/*     */       
/* 140 */       longToBigEndian(paramArrayOflong[paramInt1 + b], paramArrayOfbyte, paramInt3);
/* 141 */       paramInt3 += 8;
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
/*     */   public static void longToBigEndian(long paramLong, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/* 155 */     for (int i = paramInt2 - 1; i >= 0; i--) {
/*     */       
/* 157 */       paramArrayOfbyte[i + paramInt1] = (byte)(int)(paramLong & 0xFFL);
/* 158 */       paramLong >>>= 8L;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static short littleEndianToShort(byte[] paramArrayOfbyte, int paramInt) {
/* 164 */     int i = paramArrayOfbyte[paramInt] & 0xFF;
/* 165 */     i |= (paramArrayOfbyte[++paramInt] & 0xFF) << 8;
/* 166 */     return (short)i;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int littleEndianToInt(byte[] paramArrayOfbyte, int paramInt) {
/* 171 */     int i = paramArrayOfbyte[paramInt] & 0xFF;
/* 172 */     i |= (paramArrayOfbyte[++paramInt] & 0xFF) << 8;
/* 173 */     i |= (paramArrayOfbyte[++paramInt] & 0xFF) << 16;
/* 174 */     i |= paramArrayOfbyte[++paramInt] << 24;
/* 175 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void littleEndianToInt(byte[] paramArrayOfbyte, int paramInt, int[] paramArrayOfint) {
/* 180 */     for (byte b = 0; b < paramArrayOfint.length; b++) {
/*     */       
/* 182 */       paramArrayOfint[b] = littleEndianToInt(paramArrayOfbyte, paramInt);
/* 183 */       paramInt += 4;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void littleEndianToInt(byte[] paramArrayOfbyte, int paramInt1, int[] paramArrayOfint, int paramInt2, int paramInt3) {
/* 189 */     for (byte b = 0; b < paramInt3; b++) {
/*     */       
/* 191 */       paramArrayOfint[paramInt2 + b] = littleEndianToInt(paramArrayOfbyte, paramInt1);
/* 192 */       paramInt1 += 4;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static int[] littleEndianToInt(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/* 198 */     int[] arrayOfInt = new int[paramInt2];
/* 199 */     for (byte b = 0; b < arrayOfInt.length; b++) {
/*     */       
/* 201 */       arrayOfInt[b] = littleEndianToInt(paramArrayOfbyte, paramInt1);
/* 202 */       paramInt1 += 4;
/*     */     } 
/* 204 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] shortToLittleEndian(short paramShort) {
/* 209 */     byte[] arrayOfByte = new byte[2];
/* 210 */     shortToLittleEndian(paramShort, arrayOfByte, 0);
/* 211 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void shortToLittleEndian(short paramShort, byte[] paramArrayOfbyte, int paramInt) {
/* 216 */     paramArrayOfbyte[paramInt] = (byte)paramShort;
/* 217 */     paramArrayOfbyte[++paramInt] = (byte)(paramShort >>> 8);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] shortToBigEndian(short paramShort) {
/* 223 */     byte[] arrayOfByte = new byte[2];
/* 224 */     shortToBigEndian(paramShort, arrayOfByte, 0);
/* 225 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void shortToBigEndian(short paramShort, byte[] paramArrayOfbyte, int paramInt) {
/* 230 */     paramArrayOfbyte[paramInt] = (byte)(paramShort >>> 8);
/* 231 */     paramArrayOfbyte[++paramInt] = (byte)paramShort;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] intToLittleEndian(int paramInt) {
/* 237 */     byte[] arrayOfByte = new byte[4];
/* 238 */     intToLittleEndian(paramInt, arrayOfByte, 0);
/* 239 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void intToLittleEndian(int paramInt1, byte[] paramArrayOfbyte, int paramInt2) {
/* 244 */     paramArrayOfbyte[paramInt2] = (byte)paramInt1;
/* 245 */     paramArrayOfbyte[++paramInt2] = (byte)(paramInt1 >>> 8);
/* 246 */     paramArrayOfbyte[++paramInt2] = (byte)(paramInt1 >>> 16);
/* 247 */     paramArrayOfbyte[++paramInt2] = (byte)(paramInt1 >>> 24);
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] intToLittleEndian(int[] paramArrayOfint) {
/* 252 */     byte[] arrayOfByte = new byte[4 * paramArrayOfint.length];
/* 253 */     intToLittleEndian(paramArrayOfint, arrayOfByte, 0);
/* 254 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void intToLittleEndian(int[] paramArrayOfint, byte[] paramArrayOfbyte, int paramInt) {
/* 259 */     for (byte b = 0; b < paramArrayOfint.length; b++) {
/*     */       
/* 261 */       intToLittleEndian(paramArrayOfint[b], paramArrayOfbyte, paramInt);
/* 262 */       paramInt += 4;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void intToLittleEndian(int[] paramArrayOfint, int paramInt1, int paramInt2, byte[] paramArrayOfbyte, int paramInt3) {
/* 268 */     for (byte b = 0; b < paramInt2; b++) {
/*     */       
/* 270 */       intToLittleEndian(paramArrayOfint[paramInt1 + b], paramArrayOfbyte, paramInt3);
/* 271 */       paramInt3 += 4;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static long littleEndianToLong(byte[] paramArrayOfbyte, int paramInt) {
/* 277 */     int i = littleEndianToInt(paramArrayOfbyte, paramInt);
/* 278 */     int j = littleEndianToInt(paramArrayOfbyte, paramInt + 4);
/* 279 */     return (j & 0xFFFFFFFFL) << 32L | i & 0xFFFFFFFFL;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void littleEndianToLong(byte[] paramArrayOfbyte, int paramInt, long[] paramArrayOflong) {
/* 284 */     for (byte b = 0; b < paramArrayOflong.length; b++) {
/*     */       
/* 286 */       paramArrayOflong[b] = littleEndianToLong(paramArrayOfbyte, paramInt);
/* 287 */       paramInt += 8;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void littleEndianToLong(byte[] paramArrayOfbyte, int paramInt1, long[] paramArrayOflong, int paramInt2, int paramInt3) {
/* 293 */     for (byte b = 0; b < paramInt3; b++) {
/*     */       
/* 295 */       paramArrayOflong[paramInt2 + b] = littleEndianToLong(paramArrayOfbyte, paramInt1);
/* 296 */       paramInt1 += 8;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] longToLittleEndian(long paramLong) {
/* 302 */     byte[] arrayOfByte = new byte[8];
/* 303 */     longToLittleEndian(paramLong, arrayOfByte, 0);
/* 304 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void longToLittleEndian(long paramLong, byte[] paramArrayOfbyte, int paramInt) {
/* 309 */     intToLittleEndian((int)(paramLong & 0xFFFFFFFFL), paramArrayOfbyte, paramInt);
/* 310 */     intToLittleEndian((int)(paramLong >>> 32L), paramArrayOfbyte, paramInt + 4);
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] longToLittleEndian(long[] paramArrayOflong) {
/* 315 */     byte[] arrayOfByte = new byte[8 * paramArrayOflong.length];
/* 316 */     longToLittleEndian(paramArrayOflong, arrayOfByte, 0);
/* 317 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void longToLittleEndian(long[] paramArrayOflong, byte[] paramArrayOfbyte, int paramInt) {
/* 322 */     for (byte b = 0; b < paramArrayOflong.length; b++) {
/*     */       
/* 324 */       longToLittleEndian(paramArrayOflong[b], paramArrayOfbyte, paramInt);
/* 325 */       paramInt += 8;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void longToLittleEndian(long[] paramArrayOflong, int paramInt1, int paramInt2, byte[] paramArrayOfbyte, int paramInt3) {
/* 331 */     for (byte b = 0; b < paramInt2; b++) {
/*     */       
/* 333 */       longToLittleEndian(paramArrayOflong[paramInt1 + b], paramArrayOfbyte, paramInt3);
/* 334 */       paramInt3 += 8;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/util/Pack.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */