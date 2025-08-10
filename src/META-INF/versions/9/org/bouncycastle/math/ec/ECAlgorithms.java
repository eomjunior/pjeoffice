/*     */ package META-INF.versions.9.org.bouncycastle.math.ec;
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ import org.bouncycastle.math.ec.FixedPointPreCompInfo;
/*     */ import org.bouncycastle.math.ec.FixedPointUtil;
/*     */ import org.bouncycastle.math.ec.WNafPreCompInfo;
/*     */ import org.bouncycastle.math.ec.WNafUtil;
/*     */ import org.bouncycastle.math.ec.endo.ECEndomorphism;
/*     */ import org.bouncycastle.math.ec.endo.GLVEndomorphism;
/*     */ import org.bouncycastle.math.field.FiniteField;
/*     */ 
/*     */ public class ECAlgorithms {
/*     */   public static boolean isF2mCurve(ECCurve paramECCurve) {
/*  16 */     return isF2mField(paramECCurve.getField());
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isF2mField(FiniteField paramFiniteField) {
/*  21 */     return (paramFiniteField.getDimension() > 1 && paramFiniteField.getCharacteristic().equals(ECConstants.TWO) && paramFiniteField instanceof org.bouncycastle.math.field.PolynomialExtensionField);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isFpCurve(ECCurve paramECCurve) {
/*  27 */     return isFpField(paramECCurve.getField());
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isFpField(FiniteField paramFiniteField) {
/*  32 */     return (paramFiniteField.getDimension() == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ECPoint sumOfMultiplies(ECPoint[] paramArrayOfECPoint, BigInteger[] paramArrayOfBigInteger) {
/*  37 */     if (paramArrayOfECPoint == null || paramArrayOfBigInteger == null || paramArrayOfECPoint.length != paramArrayOfBigInteger.length || paramArrayOfECPoint.length < 1)
/*     */     {
/*  39 */       throw new IllegalArgumentException("point and scalar arrays should be non-null, and of equal, non-zero, length");
/*     */     }
/*     */     
/*  42 */     int i = paramArrayOfECPoint.length;
/*  43 */     switch (i) {
/*     */       
/*     */       case 1:
/*  46 */         return paramArrayOfECPoint[0].multiply(paramArrayOfBigInteger[0]);
/*     */       case 2:
/*  48 */         return sumOfTwoMultiplies(paramArrayOfECPoint[0], paramArrayOfBigInteger[0], paramArrayOfECPoint[1], paramArrayOfBigInteger[1]);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  53 */     ECPoint eCPoint = paramArrayOfECPoint[0];
/*  54 */     ECCurve eCCurve = eCPoint.getCurve();
/*     */     
/*  56 */     ECPoint[] arrayOfECPoint = new ECPoint[i];
/*  57 */     arrayOfECPoint[0] = eCPoint;
/*  58 */     for (byte b = 1; b < i; b++)
/*     */     {
/*  60 */       arrayOfECPoint[b] = importPoint(eCCurve, paramArrayOfECPoint[b]);
/*     */     }
/*     */     
/*  63 */     ECEndomorphism eCEndomorphism = eCCurve.getEndomorphism();
/*  64 */     if (eCEndomorphism instanceof GLVEndomorphism)
/*     */     {
/*  66 */       return implCheckResult(implSumOfMultipliesGLV(arrayOfECPoint, paramArrayOfBigInteger, (GLVEndomorphism)eCEndomorphism));
/*     */     }
/*     */     
/*  69 */     return implCheckResult(implSumOfMultiplies(arrayOfECPoint, paramArrayOfBigInteger));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static ECPoint sumOfTwoMultiplies(ECPoint paramECPoint1, BigInteger paramBigInteger1, ECPoint paramECPoint2, BigInteger paramBigInteger2) {
/*  75 */     ECCurve eCCurve = paramECPoint1.getCurve();
/*  76 */     paramECPoint2 = importPoint(eCCurve, paramECPoint2);
/*     */ 
/*     */     
/*  79 */     if (eCCurve instanceof ECCurve.AbstractF2m) {
/*     */       
/*  81 */       ECCurve.AbstractF2m abstractF2m = (ECCurve.AbstractF2m)eCCurve;
/*  82 */       if (abstractF2m.isKoblitz())
/*     */       {
/*  84 */         return implCheckResult(paramECPoint1.multiply(paramBigInteger1).add(paramECPoint2.multiply(paramBigInteger2)));
/*     */       }
/*     */     } 
/*     */     
/*  88 */     ECEndomorphism eCEndomorphism = eCCurve.getEndomorphism();
/*  89 */     if (eCEndomorphism instanceof GLVEndomorphism)
/*     */     {
/*  91 */       return implCheckResult(
/*  92 */           implSumOfMultipliesGLV(new ECPoint[] { paramECPoint1, paramECPoint2 }, new BigInteger[] { paramBigInteger1, paramBigInteger2 }, (GLVEndomorphism)eCEndomorphism));
/*     */     }
/*     */     
/*  95 */     return implCheckResult(implShamirsTrickWNaf(paramECPoint1, paramBigInteger1, paramECPoint2, paramBigInteger2));
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
/*     */   public static ECPoint shamirsTrick(ECPoint paramECPoint1, BigInteger paramBigInteger1, ECPoint paramECPoint2, BigInteger paramBigInteger2) {
/* 120 */     ECCurve eCCurve = paramECPoint1.getCurve();
/* 121 */     paramECPoint2 = importPoint(eCCurve, paramECPoint2);
/*     */     
/* 123 */     return implCheckResult(implShamirsTrickJsf(paramECPoint1, paramBigInteger1, paramECPoint2, paramBigInteger2));
/*     */   }
/*     */ 
/*     */   
/*     */   public static ECPoint importPoint(ECCurve paramECCurve, ECPoint paramECPoint) {
/* 128 */     ECCurve eCCurve = paramECPoint.getCurve();
/* 129 */     if (!paramECCurve.equals(eCCurve))
/*     */     {
/* 131 */       throw new IllegalArgumentException("Point must be on the same curve");
/*     */     }
/* 133 */     return paramECCurve.importPoint(paramECPoint);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void montgomeryTrick(ECFieldElement[] paramArrayOfECFieldElement, int paramInt1, int paramInt2) {
/* 138 */     montgomeryTrick(paramArrayOfECFieldElement, paramInt1, paramInt2, null);
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
/*     */   public static void montgomeryTrick(ECFieldElement[] paramArrayOfECFieldElement, int paramInt1, int paramInt2, ECFieldElement paramECFieldElement) {
/* 150 */     ECFieldElement[] arrayOfECFieldElement = new ECFieldElement[paramInt2];
/* 151 */     arrayOfECFieldElement[0] = paramArrayOfECFieldElement[paramInt1];
/*     */     
/* 153 */     byte b = 0;
/* 154 */     while (++b < paramInt2)
/*     */     {
/* 156 */       arrayOfECFieldElement[b] = arrayOfECFieldElement[b - 1].multiply(paramArrayOfECFieldElement[paramInt1 + b]);
/*     */     }
/*     */     
/* 159 */     b--;
/*     */     
/* 161 */     if (paramECFieldElement != null)
/*     */     {
/* 163 */       arrayOfECFieldElement[b] = arrayOfECFieldElement[b].multiply(paramECFieldElement);
/*     */     }
/*     */     
/* 166 */     ECFieldElement eCFieldElement = arrayOfECFieldElement[b].invert();
/*     */     
/* 168 */     while (b > 0) {
/*     */       
/* 170 */       int i = paramInt1 + b--;
/* 171 */       ECFieldElement eCFieldElement1 = paramArrayOfECFieldElement[i];
/* 172 */       paramArrayOfECFieldElement[i] = arrayOfECFieldElement[b].multiply(eCFieldElement);
/* 173 */       eCFieldElement = eCFieldElement.multiply(eCFieldElement1);
/*     */     } 
/*     */     
/* 176 */     paramArrayOfECFieldElement[paramInt1] = eCFieldElement;
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
/*     */   public static ECPoint referenceMultiply(ECPoint paramECPoint, BigInteger paramBigInteger) {
/* 192 */     BigInteger bigInteger = paramBigInteger.abs();
/* 193 */     ECPoint eCPoint = paramECPoint.getCurve().getInfinity();
/* 194 */     int i = bigInteger.bitLength();
/* 195 */     if (i > 0) {
/*     */       
/* 197 */       if (bigInteger.testBit(0))
/*     */       {
/* 199 */         eCPoint = paramECPoint;
/*     */       }
/* 201 */       for (byte b = 1; b < i; b++) {
/*     */         
/* 203 */         paramECPoint = paramECPoint.twice();
/* 204 */         if (bigInteger.testBit(b))
/*     */         {
/* 206 */           eCPoint = eCPoint.add(paramECPoint);
/*     */         }
/*     */       } 
/*     */     } 
/* 210 */     return (paramBigInteger.signum() < 0) ? eCPoint.negate() : eCPoint;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ECPoint validatePoint(ECPoint paramECPoint) {
/* 215 */     if (!paramECPoint.isValid())
/*     */     {
/* 217 */       throw new IllegalStateException("Invalid point");
/*     */     }
/*     */     
/* 220 */     return paramECPoint;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ECPoint cleanPoint(ECCurve paramECCurve, ECPoint paramECPoint) {
/* 225 */     ECCurve eCCurve = paramECPoint.getCurve();
/* 226 */     if (!paramECCurve.equals(eCCurve))
/*     */     {
/* 228 */       throw new IllegalArgumentException("Point must be on the same curve");
/*     */     }
/*     */     
/* 231 */     return paramECCurve.decodePoint(paramECPoint.getEncoded(false));
/*     */   }
/*     */ 
/*     */   
/*     */   static ECPoint implCheckResult(ECPoint paramECPoint) {
/* 236 */     if (!paramECPoint.isValidPartial())
/*     */     {
/* 238 */       throw new IllegalStateException("Invalid result");
/*     */     }
/*     */     
/* 241 */     return paramECPoint;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static ECPoint implShamirsTrickJsf(ECPoint paramECPoint1, BigInteger paramBigInteger1, ECPoint paramECPoint2, BigInteger paramBigInteger2) {
/* 247 */     ECCurve eCCurve = paramECPoint1.getCurve();
/* 248 */     ECPoint eCPoint1 = eCCurve.getInfinity();
/*     */ 
/*     */     
/* 251 */     ECPoint eCPoint2 = paramECPoint1.add(paramECPoint2);
/* 252 */     ECPoint eCPoint3 = paramECPoint1.subtract(paramECPoint2);
/*     */     
/* 254 */     ECPoint[] arrayOfECPoint1 = { paramECPoint2, eCPoint3, paramECPoint1, eCPoint2 };
/* 255 */     eCCurve.normalizeAll(arrayOfECPoint1);
/*     */ 
/*     */ 
/*     */     
/* 259 */     ECPoint[] arrayOfECPoint2 = { arrayOfECPoint1[3].negate(), arrayOfECPoint1[2].negate(), arrayOfECPoint1[1].negate(), arrayOfECPoint1[0].negate(), eCPoint1, arrayOfECPoint1[0], arrayOfECPoint1[1], arrayOfECPoint1[2], arrayOfECPoint1[3] };
/*     */ 
/*     */     
/* 262 */     byte[] arrayOfByte = WNafUtil.generateJSF(paramBigInteger1, paramBigInteger2);
/*     */     
/* 264 */     ECPoint eCPoint4 = eCPoint1;
/*     */     
/* 266 */     int i = arrayOfByte.length;
/* 267 */     while (--i >= 0) {
/*     */       
/* 269 */       byte b = arrayOfByte[i];
/*     */ 
/*     */       
/* 272 */       int j = b << 24 >> 28, k = b << 28 >> 28;
/*     */       
/* 274 */       int m = 4 + j * 3 + k;
/* 275 */       eCPoint4 = eCPoint4.twicePlus(arrayOfECPoint2[m]);
/*     */     } 
/*     */     
/* 278 */     return eCPoint4;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static ECPoint implShamirsTrickWNaf(ECPoint paramECPoint1, BigInteger paramBigInteger1, ECPoint paramECPoint2, BigInteger paramBigInteger2) {
/* 284 */     boolean bool1 = (paramBigInteger1.signum() < 0) ? true : false, bool2 = (paramBigInteger2.signum() < 0) ? true : false;
/*     */     
/* 286 */     BigInteger bigInteger1 = paramBigInteger1.abs(), bigInteger2 = paramBigInteger2.abs();
/*     */     
/* 288 */     int i = WNafUtil.getWindowSize(bigInteger1.bitLength(), 8);
/* 289 */     int j = WNafUtil.getWindowSize(bigInteger2.bitLength(), 8);
/*     */     
/* 291 */     WNafPreCompInfo wNafPreCompInfo1 = WNafUtil.precompute(paramECPoint1, i, true);
/* 292 */     WNafPreCompInfo wNafPreCompInfo2 = WNafUtil.precompute(paramECPoint2, j, true);
/*     */ 
/*     */ 
/*     */     
/* 296 */     ECCurve eCCurve = paramECPoint1.getCurve();
/* 297 */     int m = FixedPointUtil.getCombSize(eCCurve);
/* 298 */     if (!bool1 && !bool2 && paramBigInteger1
/* 299 */       .bitLength() <= m && paramBigInteger2.bitLength() <= m && wNafPreCompInfo1
/* 300 */       .isPromoted() && wNafPreCompInfo2.isPromoted())
/*     */     {
/* 302 */       return implShamirsTrickFixedPoint(paramECPoint1, paramBigInteger1, paramECPoint2, paramBigInteger2);
/*     */     }
/*     */ 
/*     */     
/* 306 */     int k = Math.min(8, wNafPreCompInfo1.getWidth());
/* 307 */     m = Math.min(8, wNafPreCompInfo2.getWidth());
/*     */     
/* 309 */     ECPoint[] arrayOfECPoint1 = bool1 ? wNafPreCompInfo1.getPreCompNeg() : wNafPreCompInfo1.getPreComp();
/* 310 */     ECPoint[] arrayOfECPoint2 = bool2 ? wNafPreCompInfo2.getPreCompNeg() : wNafPreCompInfo2.getPreComp();
/* 311 */     ECPoint[] arrayOfECPoint3 = bool1 ? wNafPreCompInfo1.getPreComp() : wNafPreCompInfo1.getPreCompNeg();
/* 312 */     ECPoint[] arrayOfECPoint4 = bool2 ? wNafPreCompInfo2.getPreComp() : wNafPreCompInfo2.getPreCompNeg();
/*     */     
/* 314 */     byte[] arrayOfByte1 = WNafUtil.generateWindowNaf(k, bigInteger1);
/* 315 */     byte[] arrayOfByte2 = WNafUtil.generateWindowNaf(m, bigInteger2);
/*     */     
/* 317 */     return implShamirsTrickWNaf(arrayOfECPoint1, arrayOfECPoint3, arrayOfByte1, arrayOfECPoint2, arrayOfECPoint4, arrayOfByte2);
/*     */   }
/*     */ 
/*     */   
/*     */   static ECPoint implShamirsTrickWNaf(ECEndomorphism paramECEndomorphism, ECPoint paramECPoint, BigInteger paramBigInteger1, BigInteger paramBigInteger2) {
/* 322 */     boolean bool1 = (paramBigInteger1.signum() < 0) ? true : false, bool2 = (paramBigInteger2.signum() < 0) ? true : false;
/*     */     
/* 324 */     paramBigInteger1 = paramBigInteger1.abs();
/* 325 */     paramBigInteger2 = paramBigInteger2.abs();
/*     */     
/* 327 */     int i = WNafUtil.getWindowSize(Math.max(paramBigInteger1.bitLength(), paramBigInteger2.bitLength()), 8);
/*     */     
/* 329 */     WNafPreCompInfo wNafPreCompInfo1 = WNafUtil.precompute(paramECPoint, i, true);
/* 330 */     ECPoint eCPoint = EndoUtil.mapPoint(paramECEndomorphism, paramECPoint);
/* 331 */     WNafPreCompInfo wNafPreCompInfo2 = WNafUtil.precomputeWithPointMap(eCPoint, paramECEndomorphism.getPointMap(), wNafPreCompInfo1, true);
/*     */     
/* 333 */     int j = Math.min(8, wNafPreCompInfo1.getWidth());
/* 334 */     int k = Math.min(8, wNafPreCompInfo2.getWidth());
/*     */     
/* 336 */     ECPoint[] arrayOfECPoint1 = bool1 ? wNafPreCompInfo1.getPreCompNeg() : wNafPreCompInfo1.getPreComp();
/* 337 */     ECPoint[] arrayOfECPoint2 = bool2 ? wNafPreCompInfo2.getPreCompNeg() : wNafPreCompInfo2.getPreComp();
/* 338 */     ECPoint[] arrayOfECPoint3 = bool1 ? wNafPreCompInfo1.getPreComp() : wNafPreCompInfo1.getPreCompNeg();
/* 339 */     ECPoint[] arrayOfECPoint4 = bool2 ? wNafPreCompInfo2.getPreComp() : wNafPreCompInfo2.getPreCompNeg();
/*     */     
/* 341 */     byte[] arrayOfByte1 = WNafUtil.generateWindowNaf(j, paramBigInteger1);
/* 342 */     byte[] arrayOfByte2 = WNafUtil.generateWindowNaf(k, paramBigInteger2);
/*     */     
/* 344 */     return implShamirsTrickWNaf(arrayOfECPoint1, arrayOfECPoint3, arrayOfByte1, arrayOfECPoint2, arrayOfECPoint4, arrayOfByte2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static ECPoint implShamirsTrickWNaf(ECPoint[] paramArrayOfECPoint1, ECPoint[] paramArrayOfECPoint2, byte[] paramArrayOfbyte1, ECPoint[] paramArrayOfECPoint3, ECPoint[] paramArrayOfECPoint4, byte[] paramArrayOfbyte2) {
/* 350 */     int i = Math.max(paramArrayOfbyte1.length, paramArrayOfbyte2.length);
/*     */     
/* 352 */     ECCurve eCCurve = paramArrayOfECPoint1[0].getCurve();
/* 353 */     ECPoint eCPoint1 = eCCurve.getInfinity();
/*     */     
/* 355 */     ECPoint eCPoint2 = eCPoint1;
/* 356 */     byte b = 0;
/*     */     
/* 358 */     for (int j = i - 1; j >= 0; j--) {
/*     */       
/* 360 */       boolean bool1 = (j < paramArrayOfbyte1.length) ? paramArrayOfbyte1[j] : false;
/* 361 */       boolean bool2 = (j < paramArrayOfbyte2.length) ? paramArrayOfbyte2[j] : false;
/*     */       
/* 363 */       if ((bool1 | bool2) == 0) {
/*     */         
/* 365 */         b++;
/*     */       }
/*     */       else {
/*     */         
/* 369 */         ECPoint eCPoint = eCPoint1;
/* 370 */         if (bool1) {
/*     */           
/* 372 */           int k = Math.abs(bool1);
/* 373 */           ECPoint[] arrayOfECPoint = bool1 ? paramArrayOfECPoint2 : paramArrayOfECPoint1;
/* 374 */           eCPoint = eCPoint.add(arrayOfECPoint[k >>> 1]);
/*     */         } 
/* 376 */         if (bool2) {
/*     */           
/* 378 */           int k = Math.abs(bool2);
/* 379 */           ECPoint[] arrayOfECPoint = bool2 ? paramArrayOfECPoint4 : paramArrayOfECPoint3;
/* 380 */           eCPoint = eCPoint.add(arrayOfECPoint[k >>> 1]);
/*     */         } 
/*     */         
/* 383 */         if (b > 0) {
/*     */           
/* 385 */           eCPoint2 = eCPoint2.timesPow2(b);
/* 386 */           b = 0;
/*     */         } 
/*     */         
/* 389 */         eCPoint2 = eCPoint2.twicePlus(eCPoint);
/*     */       } 
/*     */     } 
/* 392 */     if (b > 0)
/*     */     {
/* 394 */       eCPoint2 = eCPoint2.timesPow2(b);
/*     */     }
/*     */     
/* 397 */     return eCPoint2;
/*     */   }
/*     */ 
/*     */   
/*     */   static ECPoint implSumOfMultiplies(ECPoint[] paramArrayOfECPoint, BigInteger[] paramArrayOfBigInteger) {
/* 402 */     int i = paramArrayOfECPoint.length;
/* 403 */     boolean[] arrayOfBoolean = new boolean[i];
/* 404 */     WNafPreCompInfo[] arrayOfWNafPreCompInfo = new WNafPreCompInfo[i];
/* 405 */     byte[][] arrayOfByte = new byte[i][];
/*     */     
/* 407 */     for (byte b = 0; b < i; b++) {
/*     */       
/* 409 */       BigInteger bigInteger = paramArrayOfBigInteger[b]; arrayOfBoolean[b] = (bigInteger.signum() < 0); bigInteger = bigInteger.abs();
/*     */       
/* 411 */       int j = WNafUtil.getWindowSize(bigInteger.bitLength(), 8);
/* 412 */       WNafPreCompInfo wNafPreCompInfo = WNafUtil.precompute(paramArrayOfECPoint[b], j, true);
/*     */       
/* 414 */       int k = Math.min(8, wNafPreCompInfo.getWidth());
/*     */       
/* 416 */       arrayOfWNafPreCompInfo[b] = wNafPreCompInfo;
/* 417 */       arrayOfByte[b] = WNafUtil.generateWindowNaf(k, bigInteger);
/*     */     } 
/*     */     
/* 420 */     return implSumOfMultiplies(arrayOfBoolean, arrayOfWNafPreCompInfo, arrayOfByte);
/*     */   }
/*     */ 
/*     */   
/*     */   static ECPoint implSumOfMultipliesGLV(ECPoint[] paramArrayOfECPoint, BigInteger[] paramArrayOfBigInteger, GLVEndomorphism paramGLVEndomorphism) {
/* 425 */     BigInteger bigInteger = paramArrayOfECPoint[0].getCurve().getOrder();
/*     */     
/* 427 */     int i = paramArrayOfECPoint.length;
/*     */     
/* 429 */     BigInteger[] arrayOfBigInteger = new BigInteger[i << 1]; byte b2;
/* 430 */     for (byte b1 = 0; b1 < i; b1++) {
/*     */       
/* 432 */       BigInteger[] arrayOfBigInteger1 = paramGLVEndomorphism.decomposeScalar(paramArrayOfBigInteger[b1].mod(bigInteger));
/* 433 */       arrayOfBigInteger[b2++] = arrayOfBigInteger1[0];
/* 434 */       arrayOfBigInteger[b2++] = arrayOfBigInteger1[1];
/*     */     } 
/*     */     
/* 437 */     if (paramGLVEndomorphism.hasEfficientPointMap())
/*     */     {
/* 439 */       return implSumOfMultiplies((ECEndomorphism)paramGLVEndomorphism, paramArrayOfECPoint, arrayOfBigInteger);
/*     */     }
/*     */     
/* 442 */     ECPoint[] arrayOfECPoint = new ECPoint[i << 1]; byte b3;
/* 443 */     for (b2 = 0, b3 = 0; b2 < i; b2++) {
/*     */       
/* 445 */       ECPoint eCPoint1 = paramArrayOfECPoint[b2];
/* 446 */       ECPoint eCPoint2 = EndoUtil.mapPoint((ECEndomorphism)paramGLVEndomorphism, eCPoint1);
/* 447 */       arrayOfECPoint[b3++] = eCPoint1;
/* 448 */       arrayOfECPoint[b3++] = eCPoint2;
/*     */     } 
/*     */     
/* 451 */     return implSumOfMultiplies(arrayOfECPoint, arrayOfBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   static ECPoint implSumOfMultiplies(ECEndomorphism paramECEndomorphism, ECPoint[] paramArrayOfECPoint, BigInteger[] paramArrayOfBigInteger) {
/* 456 */     int i = paramArrayOfECPoint.length, j = i << 1;
/*     */     
/* 458 */     boolean[] arrayOfBoolean = new boolean[j];
/* 459 */     WNafPreCompInfo[] arrayOfWNafPreCompInfo = new WNafPreCompInfo[j];
/* 460 */     byte[][] arrayOfByte = new byte[j][];
/*     */     
/* 462 */     ECPointMap eCPointMap = paramECEndomorphism.getPointMap();
/*     */     
/* 464 */     for (byte b = 0; b < i; b++) {
/*     */       
/* 466 */       int k = b << 1, m = k + 1;
/*     */       
/* 468 */       BigInteger bigInteger1 = paramArrayOfBigInteger[k]; arrayOfBoolean[k] = (bigInteger1.signum() < 0); bigInteger1 = bigInteger1.abs();
/* 469 */       BigInteger bigInteger2 = paramArrayOfBigInteger[m]; arrayOfBoolean[m] = (bigInteger2.signum() < 0); bigInteger2 = bigInteger2.abs();
/*     */       
/* 471 */       int n = WNafUtil.getWindowSize(Math.max(bigInteger1.bitLength(), bigInteger2.bitLength()), 8);
/*     */       
/* 473 */       ECPoint eCPoint1 = paramArrayOfECPoint[b];
/* 474 */       WNafPreCompInfo wNafPreCompInfo1 = WNafUtil.precompute(eCPoint1, n, true);
/* 475 */       ECPoint eCPoint2 = EndoUtil.mapPoint(paramECEndomorphism, eCPoint1);
/* 476 */       WNafPreCompInfo wNafPreCompInfo2 = WNafUtil.precomputeWithPointMap(eCPoint2, eCPointMap, wNafPreCompInfo1, true);
/*     */       
/* 478 */       int i1 = Math.min(8, wNafPreCompInfo1.getWidth());
/* 479 */       int i2 = Math.min(8, wNafPreCompInfo2.getWidth());
/*     */       
/* 481 */       arrayOfWNafPreCompInfo[k] = wNafPreCompInfo1;
/* 482 */       arrayOfWNafPreCompInfo[m] = wNafPreCompInfo2;
/* 483 */       arrayOfByte[k] = WNafUtil.generateWindowNaf(i1, bigInteger1);
/* 484 */       arrayOfByte[m] = WNafUtil.generateWindowNaf(i2, bigInteger2);
/*     */     } 
/*     */     
/* 487 */     return implSumOfMultiplies(arrayOfBoolean, arrayOfWNafPreCompInfo, arrayOfByte);
/*     */   }
/*     */ 
/*     */   
/*     */   private static ECPoint implSumOfMultiplies(boolean[] paramArrayOfboolean, WNafPreCompInfo[] paramArrayOfWNafPreCompInfo, byte[][] paramArrayOfbyte) {
/* 492 */     int i = 0, j = paramArrayOfbyte.length;
/* 493 */     for (byte b1 = 0; b1 < j; b1++)
/*     */     {
/* 495 */       i = Math.max(i, (paramArrayOfbyte[b1]).length);
/*     */     }
/*     */     
/* 498 */     ECCurve eCCurve = paramArrayOfWNafPreCompInfo[0].getPreComp()[0].getCurve();
/* 499 */     ECPoint eCPoint1 = eCCurve.getInfinity();
/*     */     
/* 501 */     ECPoint eCPoint2 = eCPoint1;
/* 502 */     byte b2 = 0;
/*     */     
/* 504 */     for (int k = i - 1; k >= 0; k--) {
/*     */       
/* 506 */       ECPoint eCPoint = eCPoint1;
/*     */       
/* 508 */       for (byte b = 0; b < j; b++) {
/*     */         
/* 510 */         byte[] arrayOfByte = paramArrayOfbyte[b];
/* 511 */         boolean bool = (k < arrayOfByte.length) ? arrayOfByte[k] : false;
/* 512 */         if (bool) {
/*     */           
/* 514 */           int m = Math.abs(bool);
/* 515 */           WNafPreCompInfo wNafPreCompInfo = paramArrayOfWNafPreCompInfo[b];
/* 516 */           ECPoint[] arrayOfECPoint = ((bool) == paramArrayOfboolean[b]) ? wNafPreCompInfo.getPreComp() : wNafPreCompInfo.getPreCompNeg();
/* 517 */           eCPoint = eCPoint.add(arrayOfECPoint[m >>> 1]);
/*     */         } 
/*     */       } 
/*     */       
/* 521 */       if (eCPoint == eCPoint1) {
/*     */         
/* 523 */         b2++;
/*     */       }
/*     */       else {
/*     */         
/* 527 */         if (b2 > 0) {
/*     */           
/* 529 */           eCPoint2 = eCPoint2.timesPow2(b2);
/* 530 */           b2 = 0;
/*     */         } 
/*     */         
/* 533 */         eCPoint2 = eCPoint2.twicePlus(eCPoint);
/*     */       } 
/*     */     } 
/* 536 */     if (b2 > 0)
/*     */     {
/* 538 */       eCPoint2 = eCPoint2.timesPow2(b2);
/*     */     }
/*     */     
/* 541 */     return eCPoint2;
/*     */   }
/*     */ 
/*     */   
/*     */   private static ECPoint implShamirsTrickFixedPoint(ECPoint paramECPoint1, BigInteger paramBigInteger1, ECPoint paramECPoint2, BigInteger paramBigInteger2) {
/* 546 */     ECCurve eCCurve = paramECPoint1.getCurve();
/* 547 */     int i = FixedPointUtil.getCombSize(eCCurve);
/*     */     
/* 549 */     if (paramBigInteger1.bitLength() > i || paramBigInteger2.bitLength() > i)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 557 */       throw new IllegalStateException("fixed-point comb doesn't support scalars larger than the curve order");
/*     */     }
/*     */     
/* 560 */     FixedPointPreCompInfo fixedPointPreCompInfo1 = FixedPointUtil.precompute(paramECPoint1);
/* 561 */     FixedPointPreCompInfo fixedPointPreCompInfo2 = FixedPointUtil.precompute(paramECPoint2);
/*     */     
/* 563 */     ECLookupTable eCLookupTable1 = fixedPointPreCompInfo1.getLookupTable();
/* 564 */     ECLookupTable eCLookupTable2 = fixedPointPreCompInfo2.getLookupTable();
/*     */     
/* 566 */     int j = fixedPointPreCompInfo1.getWidth();
/* 567 */     int k = fixedPointPreCompInfo2.getWidth();
/*     */ 
/*     */     
/* 570 */     if (j != k) {
/*     */       
/* 572 */       FixedPointCombMultiplier fixedPointCombMultiplier = new FixedPointCombMultiplier();
/* 573 */       ECPoint eCPoint1 = fixedPointCombMultiplier.multiply(paramECPoint1, paramBigInteger1);
/* 574 */       ECPoint eCPoint2 = fixedPointCombMultiplier.multiply(paramECPoint2, paramBigInteger2);
/* 575 */       return eCPoint1.add(eCPoint2);
/*     */     } 
/*     */     
/* 578 */     int m = j;
/*     */     
/* 580 */     int n = (i + m - 1) / m;
/*     */     
/* 582 */     ECPoint eCPoint = eCCurve.getInfinity();
/*     */     
/* 584 */     int i1 = n * m;
/* 585 */     int[] arrayOfInt1 = Nat.fromBigInteger(i1, paramBigInteger1);
/* 586 */     int[] arrayOfInt2 = Nat.fromBigInteger(i1, paramBigInteger2);
/*     */     
/* 588 */     int i2 = i1 - 1;
/* 589 */     for (byte b = 0; b < n; b++) {
/*     */       
/* 591 */       int i3 = 0, i4 = 0;
/*     */       int i5;
/* 593 */       for (i5 = i2 - b; i5 >= 0; i5 -= n) {
/*     */         
/* 595 */         int i6 = arrayOfInt1[i5 >>> 5] >>> (i5 & 0x1F);
/* 596 */         i3 ^= i6 >>> 1;
/* 597 */         i3 <<= 1;
/* 598 */         i3 ^= i6;
/*     */         
/* 600 */         int i7 = arrayOfInt2[i5 >>> 5] >>> (i5 & 0x1F);
/* 601 */         i4 ^= i7 >>> 1;
/* 602 */         i4 <<= 1;
/* 603 */         i4 ^= i7;
/*     */       } 
/*     */       
/* 606 */       ECPoint eCPoint1 = eCLookupTable1.lookupVar(i3);
/* 607 */       ECPoint eCPoint2 = eCLookupTable2.lookupVar(i4);
/*     */       
/* 609 */       ECPoint eCPoint3 = eCPoint1.add(eCPoint2);
/*     */       
/* 611 */       eCPoint = eCPoint.twicePlus(eCPoint3);
/*     */     } 
/*     */     
/* 614 */     return eCPoint.add(fixedPointPreCompInfo1.getOffset()).add(fixedPointPreCompInfo2.getOffset());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/ECAlgorithms.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */