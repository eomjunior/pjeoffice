/*     */ package META-INF.versions.9.org.bouncycastle.math.ec;
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ import org.bouncycastle.math.ec.PreCompInfo;
/*     */ import org.bouncycastle.math.ec.WNafPreCompInfo;
/*     */ 
/*     */ public abstract class WNafUtil {
/*   9 */   private static final int[] DEFAULT_WINDOW_SIZE_CUTOFFS = new int[] { 13, 41, 121, 337, 897, 2305 };
/*     */   public static final String PRECOMP_NAME = "bc_wnaf";
/*     */   private static final int MAX_WIDTH = 16;
/*  12 */   private static final byte[] EMPTY_BYTES = new byte[0];
/*  13 */   private static final int[] EMPTY_INTS = new int[0];
/*  14 */   private static final ECPoint[] EMPTY_POINTS = new ECPoint[0];
/*     */ 
/*     */   
/*     */   public static void configureBasepoint(ECPoint paramECPoint) {
/*  18 */     ECCurve eCCurve = paramECPoint.getCurve();
/*  19 */     if (null == eCCurve) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  24 */     BigInteger bigInteger = eCCurve.getOrder();
/*  25 */     int i = (null == bigInteger) ? (eCCurve.getFieldSize() + 1) : bigInteger.bitLength();
/*  26 */     int j = Math.min(16, getWindowSize(i) + 3);
/*     */     
/*  28 */     eCCurve.precompute(paramECPoint, "bc_wnaf", (PreCompCallback)new Object(j));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int[] generateCompactNaf(BigInteger paramBigInteger) {
/*  60 */     if (paramBigInteger.bitLength() >>> 16 != 0)
/*     */     {
/*  62 */       throw new IllegalArgumentException("'k' must have bitlength < 2^16");
/*     */     }
/*  64 */     if (paramBigInteger.signum() == 0)
/*     */     {
/*  66 */       return EMPTY_INTS;
/*     */     }
/*     */     
/*  69 */     BigInteger bigInteger1 = paramBigInteger.shiftLeft(1).add(paramBigInteger);
/*     */     
/*  71 */     int i = bigInteger1.bitLength();
/*  72 */     int[] arrayOfInt = new int[i >> 1];
/*     */     
/*  74 */     BigInteger bigInteger2 = bigInteger1.xor(paramBigInteger);
/*     */     
/*  76 */     int j = i - 1; byte b1 = 0, b2 = 0;
/*  77 */     for (byte b3 = 1; b3 < j; b3++) {
/*     */       
/*  79 */       if (!bigInteger2.testBit(b3)) {
/*     */         
/*  81 */         b2++;
/*     */       }
/*     */       else {
/*     */         
/*  85 */         byte b = paramBigInteger.testBit(b3) ? -1 : 1;
/*  86 */         arrayOfInt[b1++] = b << 16 | b2;
/*  87 */         b2 = 1;
/*  88 */         b3++;
/*     */       } 
/*     */     } 
/*  91 */     arrayOfInt[b1++] = 0x10000 | b2;
/*     */     
/*  93 */     if (arrayOfInt.length > b1)
/*     */     {
/*  95 */       arrayOfInt = trim(arrayOfInt, b1);
/*     */     }
/*     */     
/*  98 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int[] generateCompactWindowNaf(int paramInt, BigInteger paramBigInteger) {
/* 103 */     if (paramInt == 2)
/*     */     {
/* 105 */       return generateCompactNaf(paramBigInteger);
/*     */     }
/*     */     
/* 108 */     if (paramInt < 2 || paramInt > 16)
/*     */     {
/* 110 */       throw new IllegalArgumentException("'width' must be in the range [2, 16]");
/*     */     }
/* 112 */     if (paramBigInteger.bitLength() >>> 16 != 0)
/*     */     {
/* 114 */       throw new IllegalArgumentException("'k' must have bitlength < 2^16");
/*     */     }
/* 116 */     if (paramBigInteger.signum() == 0)
/*     */     {
/* 118 */       return EMPTY_INTS;
/*     */     }
/*     */     
/* 121 */     int[] arrayOfInt = new int[paramBigInteger.bitLength() / paramInt + 1];
/*     */ 
/*     */     
/* 124 */     int i = 1 << paramInt;
/* 125 */     int j = i - 1;
/* 126 */     int k = i >>> 1;
/*     */     
/* 128 */     boolean bool = false;
/* 129 */     byte b = 0; int m = 0;
/*     */     
/* 131 */     while (m <= paramBigInteger.bitLength()) {
/*     */       
/* 133 */       if (paramBigInteger.testBit(m) == bool) {
/*     */         
/* 135 */         m++;
/*     */         
/*     */         continue;
/*     */       } 
/* 139 */       paramBigInteger = paramBigInteger.shiftRight(m);
/*     */       
/* 141 */       int n = paramBigInteger.intValue() & j;
/* 142 */       if (bool)
/*     */       {
/* 144 */         n++;
/*     */       }
/*     */       
/* 147 */       bool = ((n & k) != 0);
/* 148 */       if (bool)
/*     */       {
/* 150 */         n -= i;
/*     */       }
/*     */       
/* 153 */       boolean bool1 = b ? (m - 1) : m;
/* 154 */       arrayOfInt[b++] = n << 16 | bool1;
/* 155 */       m = paramInt;
/*     */     } 
/*     */ 
/*     */     
/* 159 */     if (arrayOfInt.length > b)
/*     */     {
/* 161 */       arrayOfInt = trim(arrayOfInt, b);
/*     */     }
/*     */     
/* 164 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] generateJSF(BigInteger paramBigInteger1, BigInteger paramBigInteger2) {
/* 169 */     int i = Math.max(paramBigInteger1.bitLength(), paramBigInteger2.bitLength()) + 1;
/* 170 */     byte[] arrayOfByte = new byte[i];
/*     */     
/* 172 */     BigInteger bigInteger1 = paramBigInteger1, bigInteger2 = paramBigInteger2;
/* 173 */     byte b1 = 0; int j = 0, k = 0;
/*     */     
/* 175 */     byte b2 = 0;
/* 176 */     while ((j | k) != 0 || bigInteger1.bitLength() > b2 || bigInteger2.bitLength() > b2) {
/*     */       
/* 178 */       int m = (bigInteger1.intValue() >>> b2) + j & 0x7, n = (bigInteger2.intValue() >>> b2) + k & 0x7;
/*     */       
/* 180 */       int i1 = m & 0x1;
/* 181 */       if (i1 != 0) {
/*     */         
/* 183 */         i1 -= m & 0x2;
/* 184 */         if (m + i1 == 4 && (n & 0x3) == 2)
/*     */         {
/* 186 */           i1 = -i1;
/*     */         }
/*     */       } 
/*     */       
/* 190 */       int i2 = n & 0x1;
/* 191 */       if (i2 != 0) {
/*     */         
/* 193 */         i2 -= n & 0x2;
/* 194 */         if (n + i2 == 4 && (m & 0x3) == 2)
/*     */         {
/* 196 */           i2 = -i2;
/*     */         }
/*     */       } 
/*     */       
/* 200 */       if (j << 1 == 1 + i1)
/*     */       {
/* 202 */         j ^= 0x1;
/*     */       }
/* 204 */       if (k << 1 == 1 + i2)
/*     */       {
/* 206 */         k ^= 0x1;
/*     */       }
/*     */       
/* 209 */       if (++b2 == 30) {
/*     */         
/* 211 */         b2 = 0;
/* 212 */         bigInteger1 = bigInteger1.shiftRight(30);
/* 213 */         bigInteger2 = bigInteger2.shiftRight(30);
/*     */       } 
/*     */       
/* 216 */       arrayOfByte[b1++] = (byte)(i1 << 4 | i2 & 0xF);
/*     */     } 
/*     */ 
/*     */     
/* 220 */     if (arrayOfByte.length > b1)
/*     */     {
/* 222 */       arrayOfByte = trim(arrayOfByte, b1);
/*     */     }
/*     */     
/* 225 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] generateNaf(BigInteger paramBigInteger) {
/* 230 */     if (paramBigInteger.signum() == 0)
/*     */     {
/* 232 */       return EMPTY_BYTES;
/*     */     }
/*     */     
/* 235 */     BigInteger bigInteger1 = paramBigInteger.shiftLeft(1).add(paramBigInteger);
/*     */     
/* 237 */     int i = bigInteger1.bitLength() - 1;
/* 238 */     byte[] arrayOfByte = new byte[i];
/*     */     
/* 240 */     BigInteger bigInteger2 = bigInteger1.xor(paramBigInteger);
/*     */     
/* 242 */     for (byte b = 1; b < i; b++) {
/*     */       
/* 244 */       if (bigInteger2.testBit(b)) {
/*     */         
/* 246 */         arrayOfByte[b - 1] = (byte)(paramBigInteger.testBit(b) ? -1 : 1);
/* 247 */         b++;
/*     */       } 
/*     */     } 
/*     */     
/* 251 */     arrayOfByte[i - 1] = 1;
/*     */     
/* 253 */     return arrayOfByte;
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
/*     */   public static byte[] generateWindowNaf(int paramInt, BigInteger paramBigInteger) {
/* 270 */     if (paramInt == 2)
/*     */     {
/* 272 */       return generateNaf(paramBigInteger);
/*     */     }
/*     */     
/* 275 */     if (paramInt < 2 || paramInt > 8)
/*     */     {
/* 277 */       throw new IllegalArgumentException("'width' must be in the range [2, 8]");
/*     */     }
/* 279 */     if (paramBigInteger.signum() == 0)
/*     */     {
/* 281 */       return EMPTY_BYTES;
/*     */     }
/*     */     
/* 284 */     byte[] arrayOfByte = new byte[paramBigInteger.bitLength() + 1];
/*     */ 
/*     */     
/* 287 */     int i = 1 << paramInt;
/* 288 */     int j = i - 1;
/* 289 */     int k = i >>> 1;
/*     */     
/* 291 */     boolean bool = false;
/* 292 */     int m = 0, n = 0;
/*     */     
/* 294 */     while (n <= paramBigInteger.bitLength()) {
/*     */       
/* 296 */       if (paramBigInteger.testBit(n) == bool) {
/*     */         
/* 298 */         n++;
/*     */         
/*     */         continue;
/*     */       } 
/* 302 */       paramBigInteger = paramBigInteger.shiftRight(n);
/*     */       
/* 304 */       int i1 = paramBigInteger.intValue() & j;
/* 305 */       if (bool)
/*     */       {
/* 307 */         i1++;
/*     */       }
/*     */       
/* 310 */       bool = ((i1 & k) != 0);
/* 311 */       if (bool)
/*     */       {
/* 313 */         i1 -= i;
/*     */       }
/*     */       
/* 316 */       m += m ? (n - 1) : n;
/* 317 */       arrayOfByte[m++] = (byte)i1;
/* 318 */       n = paramInt;
/*     */     } 
/*     */ 
/*     */     
/* 322 */     if (arrayOfByte.length > m)
/*     */     {
/* 324 */       arrayOfByte = trim(arrayOfByte, m);
/*     */     }
/*     */     
/* 327 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getNafWeight(BigInteger paramBigInteger) {
/* 332 */     if (paramBigInteger.signum() == 0)
/*     */     {
/* 334 */       return 0;
/*     */     }
/*     */     
/* 337 */     BigInteger bigInteger1 = paramBigInteger.shiftLeft(1).add(paramBigInteger);
/* 338 */     BigInteger bigInteger2 = bigInteger1.xor(paramBigInteger);
/*     */     
/* 340 */     return bigInteger2.bitCount();
/*     */   }
/*     */ 
/*     */   
/*     */   public static WNafPreCompInfo getWNafPreCompInfo(ECPoint paramECPoint) {
/* 345 */     return getWNafPreCompInfo(paramECPoint.getCurve().getPreCompInfo(paramECPoint, "bc_wnaf"));
/*     */   }
/*     */ 
/*     */   
/*     */   public static WNafPreCompInfo getWNafPreCompInfo(PreCompInfo paramPreCompInfo) {
/* 350 */     return (paramPreCompInfo instanceof WNafPreCompInfo) ? (WNafPreCompInfo)paramPreCompInfo : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getWindowSize(int paramInt) {
/* 361 */     return getWindowSize(paramInt, DEFAULT_WINDOW_SIZE_CUTOFFS, 16);
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
/*     */   public static int getWindowSize(int paramInt1, int paramInt2) {
/* 373 */     return getWindowSize(paramInt1, DEFAULT_WINDOW_SIZE_CUTOFFS, paramInt2);
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
/*     */   public static int getWindowSize(int paramInt, int[] paramArrayOfint) {
/* 385 */     return getWindowSize(paramInt, paramArrayOfint, 16);
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
/*     */   public static int getWindowSize(int paramInt1, int[] paramArrayOfint, int paramInt2) {
/* 398 */     byte b = 0;
/* 399 */     for (; b < paramArrayOfint.length; b++) {
/*     */       
/* 401 */       if (paramInt1 < paramArrayOfint[b]) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 407 */     return Math.max(2, Math.min(paramInt2, b + 2));
/*     */   }
/*     */ 
/*     */   
/*     */   public static WNafPreCompInfo precompute(ECPoint paramECPoint, int paramInt, boolean paramBoolean) {
/* 412 */     ECCurve eCCurve = paramECPoint.getCurve();
/*     */     
/* 414 */     return (WNafPreCompInfo)eCCurve.precompute(paramECPoint, "bc_wnaf", (PreCompCallback)new Object(paramInt, paramBoolean, paramECPoint, eCCurve));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static WNafPreCompInfo precomputeWithPointMap(ECPoint paramECPoint, ECPointMap paramECPointMap, WNafPreCompInfo paramWNafPreCompInfo, boolean paramBoolean) {
/* 592 */     ECCurve eCCurve = paramECPoint.getCurve();
/*     */     
/* 594 */     return (WNafPreCompInfo)eCCurve.precompute(paramECPoint, "bc_wnaf", (PreCompCallback)new Object(paramWNafPreCompInfo, paramBoolean, paramECPointMap));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static byte[] trim(byte[] paramArrayOfbyte, int paramInt) {
/* 663 */     byte[] arrayOfByte = new byte[paramInt];
/* 664 */     System.arraycopy(paramArrayOfbyte, 0, arrayOfByte, 0, arrayOfByte.length);
/* 665 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int[] trim(int[] paramArrayOfint, int paramInt) {
/* 670 */     int[] arrayOfInt = new int[paramInt];
/* 671 */     System.arraycopy(paramArrayOfint, 0, arrayOfInt, 0, arrayOfInt.length);
/* 672 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   
/*     */   private static ECPoint[] resizeTable(ECPoint[] paramArrayOfECPoint, int paramInt) {
/* 677 */     ECPoint[] arrayOfECPoint = new ECPoint[paramInt];
/* 678 */     System.arraycopy(paramArrayOfECPoint, 0, arrayOfECPoint, 0, paramArrayOfECPoint.length);
/* 679 */     return arrayOfECPoint;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/WNafUtil.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */