/*     */ package META-INF.versions.9.org.bouncycastle.math.ec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.SecureRandom;
/*     */ import java.util.Hashtable;
/*     */ import org.bouncycastle.math.ec.ECAlgorithms;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.ECLookupTable;
/*     */ import org.bouncycastle.math.ec.ECMultiplier;
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ import org.bouncycastle.math.ec.GLVMultiplier;
/*     */ import org.bouncycastle.math.ec.PreCompCallback;
/*     */ import org.bouncycastle.math.ec.PreCompInfo;
/*     */ import org.bouncycastle.math.ec.WNafL2RMultiplier;
/*     */ import org.bouncycastle.math.ec.endo.ECEndomorphism;
/*     */ import org.bouncycastle.math.ec.endo.GLVEndomorphism;
/*     */ import org.bouncycastle.math.field.FiniteField;
/*     */ import org.bouncycastle.util.BigIntegers;
/*     */ import org.bouncycastle.util.Integers;
/*     */ 
/*     */ 
/*     */ public abstract class ECCurve
/*     */ {
/*     */   public static final int COORD_AFFINE = 0;
/*     */   public static final int COORD_HOMOGENEOUS = 1;
/*     */   public static final int COORD_JACOBIAN = 2;
/*     */   public static final int COORD_JACOBIAN_CHUDNOVSKY = 3;
/*     */   public static final int COORD_JACOBIAN_MODIFIED = 4;
/*     */   public static final int COORD_LAMBDA_AFFINE = 5;
/*     */   
/*     */   public static int[] getAllCoordinateSystems() {
/*  32 */     return new int[] { 0, 1, 2, 3, 4, 5, 6, 7 };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int COORD_LAMBDA_PROJECTIVE = 6;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int COORD_SKEWED = 7;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FiniteField field;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ECFieldElement a;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ECFieldElement b;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BigInteger order;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BigInteger cofactor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  96 */   protected int coord = 0;
/*  97 */   protected ECEndomorphism endomorphism = null;
/*  98 */   protected ECMultiplier multiplier = null;
/*     */ 
/*     */   
/*     */   protected ECCurve(FiniteField paramFiniteField) {
/* 102 */     this.field = paramFiniteField;
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract int getFieldSize();
/*     */   
/*     */   public abstract ECFieldElement fromBigInteger(BigInteger paramBigInteger);
/*     */   
/*     */   public abstract boolean isValidFieldElement(BigInteger paramBigInteger);
/*     */   
/*     */   public abstract ECFieldElement randomFieldElement(SecureRandom paramSecureRandom);
/*     */   
/*     */   public abstract ECFieldElement randomFieldElementMult(SecureRandom paramSecureRandom);
/*     */   
/*     */   public synchronized Config configure() {
/* 117 */     return new Config(this, this.coord, this.endomorphism, this.multiplier);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint validatePoint(BigInteger paramBigInteger1, BigInteger paramBigInteger2) {
/* 122 */     ECPoint eCPoint = createPoint(paramBigInteger1, paramBigInteger2);
/* 123 */     if (!eCPoint.isValid())
/*     */     {
/* 125 */       throw new IllegalArgumentException("Invalid point coordinates");
/*     */     }
/* 127 */     return eCPoint;
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint createPoint(BigInteger paramBigInteger1, BigInteger paramBigInteger2) {
/* 132 */     return createRawPoint(fromBigInteger(paramBigInteger1), fromBigInteger(paramBigInteger2));
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract org.bouncycastle.math.ec.ECCurve cloneCurve();
/*     */   
/*     */   protected abstract ECPoint createRawPoint(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2);
/*     */   
/*     */   protected abstract ECPoint createRawPoint(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement[] paramArrayOfECFieldElement);
/*     */   
/*     */   protected ECMultiplier createDefaultMultiplier() {
/* 143 */     if (this.endomorphism instanceof GLVEndomorphism)
/*     */     {
/* 145 */       return (ECMultiplier)new GLVMultiplier(this, (GLVEndomorphism)this.endomorphism);
/*     */     }
/*     */     
/* 148 */     return (ECMultiplier)new WNafL2RMultiplier();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsCoordinateSystem(int paramInt) {
/* 153 */     return (paramInt == 0);
/*     */   }
/*     */   
/*     */   public PreCompInfo getPreCompInfo(ECPoint paramECPoint, String paramString) {
/*     */     Hashtable hashtable;
/* 158 */     checkPoint(paramECPoint);
/*     */ 
/*     */     
/* 161 */     synchronized (paramECPoint) {
/*     */       
/* 163 */       hashtable = paramECPoint.preCompTable;
/*     */     } 
/*     */     
/* 166 */     if (null == hashtable)
/*     */     {
/* 168 */       return null;
/*     */     }
/*     */     
/* 171 */     synchronized (hashtable) {
/*     */       
/* 173 */       return (PreCompInfo)hashtable.get(paramString);
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
/*     */   
/*     */   public PreCompInfo precompute(ECPoint paramECPoint, String paramString, PreCompCallback paramPreCompCallback) {
/*     */     Hashtable<Object, Object> hashtable;
/* 191 */     checkPoint(paramECPoint);
/*     */ 
/*     */     
/* 194 */     synchronized (paramECPoint) {
/*     */       
/* 196 */       hashtable = paramECPoint.preCompTable;
/* 197 */       if (null == hashtable)
/*     */       {
/* 199 */         paramECPoint.preCompTable = hashtable = new Hashtable<>(4);
/*     */       }
/*     */     } 
/*     */     
/* 203 */     synchronized (hashtable) {
/*     */       
/* 205 */       PreCompInfo preCompInfo1 = (PreCompInfo)hashtable.get(paramString);
/* 206 */       PreCompInfo preCompInfo2 = paramPreCompCallback.precompute(preCompInfo1);
/*     */       
/* 208 */       if (preCompInfo2 != preCompInfo1)
/*     */       {
/* 210 */         hashtable.put(paramString, preCompInfo2);
/*     */       }
/*     */       
/* 213 */       return preCompInfo2;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint importPoint(ECPoint paramECPoint) {
/* 219 */     if (this == paramECPoint.getCurve())
/*     */     {
/* 221 */       return paramECPoint;
/*     */     }
/* 223 */     if (paramECPoint.isInfinity())
/*     */     {
/* 225 */       return getInfinity();
/*     */     }
/*     */ 
/*     */     
/* 229 */     paramECPoint = paramECPoint.normalize();
/*     */     
/* 231 */     return createPoint(paramECPoint.getXCoord().toBigInteger(), paramECPoint.getYCoord().toBigInteger());
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
/*     */   public void normalizeAll(ECPoint[] paramArrayOfECPoint) {
/* 246 */     normalizeAll(paramArrayOfECPoint, 0, paramArrayOfECPoint.length, null);
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
/*     */   public void normalizeAll(ECPoint[] paramArrayOfECPoint, int paramInt1, int paramInt2, ECFieldElement paramECFieldElement) {
/* 269 */     checkPoints(paramArrayOfECPoint, paramInt1, paramInt2);
/*     */     
/* 271 */     switch (getCoordinateSystem()) {
/*     */ 
/*     */       
/*     */       case 0:
/*     */       case 5:
/* 276 */         if (paramECFieldElement != null)
/*     */         {
/* 278 */           throw new IllegalArgumentException("'iso' not valid for affine coordinates");
/*     */         }
/*     */         return;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 287 */     ECFieldElement[] arrayOfECFieldElement = new ECFieldElement[paramInt2];
/* 288 */     int[] arrayOfInt = new int[paramInt2];
/* 289 */     byte b1 = 0; byte b2;
/* 290 */     for (b2 = 0; b2 < paramInt2; b2++) {
/*     */       
/* 292 */       ECPoint eCPoint = paramArrayOfECPoint[paramInt1 + b2];
/* 293 */       if (null != eCPoint && (paramECFieldElement != null || !eCPoint.isNormalized())) {
/*     */         
/* 295 */         arrayOfECFieldElement[b1] = eCPoint.getZCoord(0);
/* 296 */         arrayOfInt[b1++] = paramInt1 + b2;
/*     */       } 
/*     */     } 
/*     */     
/* 300 */     if (b1 == 0) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 305 */     ECAlgorithms.montgomeryTrick(arrayOfECFieldElement, 0, b1, paramECFieldElement);
/*     */     
/* 307 */     for (b2 = 0; b2 < b1; b2++) {
/*     */       
/* 309 */       int i = arrayOfInt[b2];
/* 310 */       paramArrayOfECPoint[i] = paramArrayOfECPoint[i].normalize(arrayOfECFieldElement[b2]);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract ECPoint getInfinity();
/*     */   
/*     */   public FiniteField getField() {
/* 318 */     return this.field;
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement getA() {
/* 323 */     return this.a;
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement getB() {
/* 328 */     return this.b;
/*     */   }
/*     */ 
/*     */   
/*     */   public BigInteger getOrder() {
/* 333 */     return this.order;
/*     */   }
/*     */ 
/*     */   
/*     */   public BigInteger getCofactor() {
/* 338 */     return this.cofactor;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCoordinateSystem() {
/* 343 */     return this.coord;
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract ECPoint decompressPoint(int paramInt, BigInteger paramBigInteger);
/*     */   
/*     */   public ECEndomorphism getEndomorphism() {
/* 350 */     return this.endomorphism;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ECMultiplier getMultiplier() {
/* 360 */     if (this.multiplier == null)
/*     */     {
/* 362 */       this.multiplier = createDefaultMultiplier();
/*     */     }
/* 364 */     return this.multiplier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ECPoint decodePoint(byte[] paramArrayOfbyte) {
/*     */     int j;
/*     */     BigInteger bigInteger1, bigInteger2;
/* 375 */     ECPoint eCPoint = null;
/* 376 */     int i = (getFieldSize() + 7) / 8;
/*     */     
/* 378 */     byte b = paramArrayOfbyte[0];
/* 379 */     switch (b) {
/*     */ 
/*     */       
/*     */       case 0:
/* 383 */         if (paramArrayOfbyte.length != 1)
/*     */         {
/* 385 */           throw new IllegalArgumentException("Incorrect length for infinity encoding");
/*     */         }
/*     */         
/* 388 */         eCPoint = getInfinity();
/*     */         break;
/*     */ 
/*     */       
/*     */       case 2:
/*     */       case 3:
/* 394 */         if (paramArrayOfbyte.length != i + 1)
/*     */         {
/* 396 */           throw new IllegalArgumentException("Incorrect length for compressed encoding");
/*     */         }
/*     */         
/* 399 */         j = b & 0x1;
/* 400 */         bigInteger2 = BigIntegers.fromUnsignedByteArray(paramArrayOfbyte, 1, i);
/*     */         
/* 402 */         eCPoint = decompressPoint(j, bigInteger2);
/* 403 */         if (!eCPoint.implIsValid(true, true))
/*     */         {
/* 405 */           throw new IllegalArgumentException("Invalid point");
/*     */         }
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 4:
/* 412 */         if (paramArrayOfbyte.length != 2 * i + 1)
/*     */         {
/* 414 */           throw new IllegalArgumentException("Incorrect length for uncompressed encoding");
/*     */         }
/*     */         
/* 417 */         bigInteger1 = BigIntegers.fromUnsignedByteArray(paramArrayOfbyte, 1, i);
/* 418 */         bigInteger2 = BigIntegers.fromUnsignedByteArray(paramArrayOfbyte, 1 + i, i);
/*     */         
/* 420 */         eCPoint = validatePoint(bigInteger1, bigInteger2);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 6:
/*     */       case 7:
/* 426 */         if (paramArrayOfbyte.length != 2 * i + 1)
/*     */         {
/* 428 */           throw new IllegalArgumentException("Incorrect length for hybrid encoding");
/*     */         }
/*     */         
/* 431 */         bigInteger1 = BigIntegers.fromUnsignedByteArray(paramArrayOfbyte, 1, i);
/* 432 */         bigInteger2 = BigIntegers.fromUnsignedByteArray(paramArrayOfbyte, 1 + i, i);
/*     */         
/* 434 */         if (bigInteger2.testBit(0) != ((b == 7)))
/*     */         {
/* 436 */           throw new IllegalArgumentException("Inconsistent Y coordinate in hybrid encoding");
/*     */         }
/*     */         
/* 439 */         eCPoint = validatePoint(bigInteger1, bigInteger2);
/*     */         break;
/*     */       
/*     */       default:
/* 443 */         throw new IllegalArgumentException("Invalid point encoding 0x" + Integer.toString(b, 16));
/*     */     } 
/*     */     
/* 446 */     if (b != 0 && eCPoint.isInfinity())
/*     */     {
/* 448 */       throw new IllegalArgumentException("Invalid infinity encoding");
/*     */     }
/*     */     
/* 451 */     return eCPoint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ECLookupTable createCacheSafeLookupTable(ECPoint[] paramArrayOfECPoint, int paramInt1, int paramInt2) {
/* 460 */     int i = getFieldSize() + 7 >>> 3;
/*     */     
/* 462 */     byte[] arrayOfByte = new byte[paramInt2 * i * 2];
/*     */     
/* 464 */     int j = 0;
/* 465 */     for (byte b = 0; b < paramInt2; b++) {
/*     */       
/* 467 */       ECPoint eCPoint = paramArrayOfECPoint[paramInt1 + b];
/* 468 */       byte[] arrayOfByte1 = eCPoint.getRawXCoord().toBigInteger().toByteArray();
/* 469 */       byte[] arrayOfByte2 = eCPoint.getRawYCoord().toBigInteger().toByteArray();
/*     */       
/* 471 */       byte b1 = (arrayOfByte1.length > i) ? 1 : 0; int k = arrayOfByte1.length - b1;
/* 472 */       byte b2 = (arrayOfByte2.length > i) ? 1 : 0; int m = arrayOfByte2.length - b2;
/*     */       
/* 474 */       System.arraycopy(arrayOfByte1, b1, arrayOfByte, j + i - k, k); j += i;
/* 475 */       System.arraycopy(arrayOfByte2, b2, arrayOfByte, j + i - m, m); j += i;
/*     */     } 
/*     */ 
/*     */     
/* 479 */     return (ECLookupTable)new Object(this, paramInt2, i, arrayOfByte);
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
/*     */   protected void checkPoint(ECPoint paramECPoint) {
/* 530 */     if (null == paramECPoint || this != paramECPoint.getCurve())
/*     */     {
/* 532 */       throw new IllegalArgumentException("'point' must be non-null and on this curve");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void checkPoints(ECPoint[] paramArrayOfECPoint) {
/* 538 */     checkPoints(paramArrayOfECPoint, 0, paramArrayOfECPoint.length);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void checkPoints(ECPoint[] paramArrayOfECPoint, int paramInt1, int paramInt2) {
/* 543 */     if (paramArrayOfECPoint == null)
/*     */     {
/* 545 */       throw new IllegalArgumentException("'points' cannot be null");
/*     */     }
/* 547 */     if (paramInt1 < 0 || paramInt2 < 0 || paramInt1 > paramArrayOfECPoint.length - paramInt2)
/*     */     {
/* 549 */       throw new IllegalArgumentException("invalid range specified for 'points'");
/*     */     }
/*     */     
/* 552 */     for (byte b = 0; b < paramInt2; b++) {
/*     */       
/* 554 */       ECPoint eCPoint = paramArrayOfECPoint[paramInt1 + b];
/* 555 */       if (null != eCPoint && this != eCPoint.getCurve())
/*     */       {
/* 557 */         throw new IllegalArgumentException("'points' entries must be null or on this curve");
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(org.bouncycastle.math.ec.ECCurve paramECCurve) {
/* 564 */     return (this == paramECCurve || (null != paramECCurve && 
/*     */       
/* 566 */       getField().equals(paramECCurve.getField()) && 
/* 567 */       getA().toBigInteger().equals(paramECCurve.getA().toBigInteger()) && 
/* 568 */       getB().toBigInteger().equals(paramECCurve.getB().toBigInteger())));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 573 */     return (this == paramObject || (paramObject instanceof org.bouncycastle.math.ec.ECCurve && equals((org.bouncycastle.math.ec.ECCurve)paramObject)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 578 */     return getField().hashCode() ^ 
/* 579 */       Integers.rotateLeft(getA().toBigInteger().hashCode(), 8) ^ 
/* 580 */       Integers.rotateLeft(getB().toBigInteger().hashCode(), 16);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/ECCurve.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */