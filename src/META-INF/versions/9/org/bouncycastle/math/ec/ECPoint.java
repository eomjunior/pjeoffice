/*     */ package META-INF.versions.9.org.bouncycastle.math.ec;
/*     */ import java.math.BigInteger;
/*     */ import java.security.SecureRandom;
/*     */ import java.util.Hashtable;
/*     */ import org.bouncycastle.crypto.CryptoServicesRegistrar;
/*     */ import org.bouncycastle.math.ec.ECAlgorithms;
/*     */ import org.bouncycastle.math.ec.ECConstants;
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.PreCompCallback;
/*     */ import org.bouncycastle.math.ec.ValidityPrecompInfo;
/*     */ 
/*     */ public abstract class ECPoint {
/*  14 */   protected static final ECFieldElement[] EMPTY_ZS = new ECFieldElement[0];
/*     */   protected ECCurve curve;
/*     */   protected ECFieldElement x;
/*     */   
/*     */   protected static ECFieldElement[] getInitialZCoords(ECCurve paramECCurve) {
/*  19 */     boolean bool = (null == paramECCurve) ? false : paramECCurve.getCoordinateSystem();
/*     */     
/*  21 */     switch (bool) {
/*     */       
/*     */       case false:
/*     */       case true:
/*  25 */         return EMPTY_ZS;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  30 */     ECFieldElement eCFieldElement = paramECCurve.fromBigInteger(ECConstants.ONE);
/*     */     
/*  32 */     switch (bool) {
/*     */       
/*     */       case true:
/*     */       case true:
/*     */       case true:
/*  37 */         return new ECFieldElement[] { eCFieldElement };
/*     */       case true:
/*  39 */         return new ECFieldElement[] { eCFieldElement, eCFieldElement, eCFieldElement };
/*     */       case true:
/*  41 */         return new ECFieldElement[] { eCFieldElement, paramECCurve.getA() };
/*     */     } 
/*  43 */     throw new IllegalArgumentException("unknown coordinate system");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected ECFieldElement y;
/*     */ 
/*     */   
/*     */   protected ECFieldElement[] zs;
/*     */   
/*  53 */   protected Hashtable preCompTable = null;
/*     */ 
/*     */   
/*     */   protected ECPoint(ECCurve paramECCurve, ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2) {
/*  57 */     this(paramECCurve, paramECFieldElement1, paramECFieldElement2, getInitialZCoords(paramECCurve));
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint(ECCurve paramECCurve, ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement[] paramArrayOfECFieldElement) {
/*  62 */     this.curve = paramECCurve;
/*  63 */     this.x = paramECFieldElement1;
/*  64 */     this.y = paramECFieldElement2;
/*  65 */     this.zs = paramArrayOfECFieldElement;
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract boolean satisfiesCurveEquation();
/*     */   
/*     */   protected boolean satisfiesOrder() {
/*  72 */     if (ECConstants.ONE.equals(this.curve.getCofactor()))
/*     */     {
/*  74 */       return true;
/*     */     }
/*     */     
/*  77 */     BigInteger bigInteger = this.curve.getOrder();
/*     */ 
/*     */ 
/*     */     
/*  81 */     return (bigInteger == null || ECAlgorithms.referenceMultiply(this, bigInteger).isInfinity());
/*     */   }
/*     */ 
/*     */   
/*     */   public final org.bouncycastle.math.ec.ECPoint getDetachedPoint() {
/*  86 */     return normalize().detach();
/*     */   }
/*     */ 
/*     */   
/*     */   public ECCurve getCurve() {
/*  91 */     return this.curve;
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract org.bouncycastle.math.ec.ECPoint detach();
/*     */ 
/*     */   
/*     */   protected int getCurveCoordinateSystem() {
/*  99 */     return (null == this.curve) ? 0 : this.curve.getCoordinateSystem();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ECFieldElement getAffineXCoord() {
/* 110 */     checkNormalized();
/* 111 */     return getXCoord();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ECFieldElement getAffineYCoord() {
/* 122 */     checkNormalized();
/* 123 */     return getYCoord();
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
/*     */   public ECFieldElement getXCoord() {
/* 138 */     return this.x;
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
/*     */   public ECFieldElement getYCoord() {
/* 153 */     return this.y;
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement getZCoord(int paramInt) {
/* 158 */     return (paramInt < 0 || paramInt >= this.zs.length) ? null : this.zs[paramInt];
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement[] getZCoords() {
/* 163 */     int i = this.zs.length;
/* 164 */     if (i == 0)
/*     */     {
/* 166 */       return EMPTY_ZS;
/*     */     }
/* 168 */     ECFieldElement[] arrayOfECFieldElement = new ECFieldElement[i];
/* 169 */     System.arraycopy(this.zs, 0, arrayOfECFieldElement, 0, i);
/* 170 */     return arrayOfECFieldElement;
/*     */   }
/*     */ 
/*     */   
/*     */   public final ECFieldElement getRawXCoord() {
/* 175 */     return this.x;
/*     */   }
/*     */ 
/*     */   
/*     */   public final ECFieldElement getRawYCoord() {
/* 180 */     return this.y;
/*     */   }
/*     */ 
/*     */   
/*     */   protected final ECFieldElement[] getRawZCoords() {
/* 185 */     return this.zs;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void checkNormalized() {
/* 190 */     if (!isNormalized())
/*     */     {
/* 192 */       throw new IllegalStateException("point not in normal form");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isNormalized() {
/* 198 */     int i = getCurveCoordinateSystem();
/*     */     
/* 200 */     return (i == 0 || i == 5 || 
/*     */       
/* 202 */       isInfinity() || this.zs[0]
/* 203 */       .isOne());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public org.bouncycastle.math.ec.ECPoint normalize() {
/* 214 */     if (isInfinity())
/*     */     {
/* 216 */       return this;
/*     */     }
/*     */     
/* 219 */     switch (getCurveCoordinateSystem()) {
/*     */ 
/*     */       
/*     */       case 0:
/*     */       case 5:
/* 224 */         return this;
/*     */     } 
/*     */ 
/*     */     
/* 228 */     ECFieldElement eCFieldElement1 = getZCoord(0);
/* 229 */     if (eCFieldElement1.isOne())
/*     */     {
/* 231 */       return this;
/*     */     }
/*     */     
/* 234 */     if (null == this.curve)
/*     */     {
/* 236 */       throw new IllegalStateException("Detached points must be in affine coordinates");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 249 */     SecureRandom secureRandom = CryptoServicesRegistrar.getSecureRandom();
/* 250 */     ECFieldElement eCFieldElement2 = this.curve.randomFieldElementMult(secureRandom);
/* 251 */     ECFieldElement eCFieldElement3 = eCFieldElement1.multiply(eCFieldElement2).invert().multiply(eCFieldElement2);
/* 252 */     return normalize(eCFieldElement3);
/*     */   }
/*     */ 
/*     */   
/*     */   org.bouncycastle.math.ec.ECPoint normalize(ECFieldElement paramECFieldElement) {
/*     */     ECFieldElement eCFieldElement1;
/*     */     ECFieldElement eCFieldElement2;
/* 259 */     switch (getCurveCoordinateSystem()) {
/*     */ 
/*     */       
/*     */       case 1:
/*     */       case 6:
/* 264 */         return createScaledPoint(paramECFieldElement, paramECFieldElement);
/*     */ 
/*     */       
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/* 270 */         eCFieldElement1 = paramECFieldElement.square(); eCFieldElement2 = eCFieldElement1.multiply(paramECFieldElement);
/* 271 */         return createScaledPoint(eCFieldElement1, eCFieldElement2);
/*     */     } 
/*     */ 
/*     */     
/* 275 */     throw new IllegalStateException("not a projective coordinate system");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected org.bouncycastle.math.ec.ECPoint createScaledPoint(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2) {
/* 282 */     return getCurve().createRawPoint(getRawXCoord().multiply(paramECFieldElement1), getRawYCoord().multiply(paramECFieldElement2));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInfinity() {
/* 287 */     return (this.x == null || this.y == null || (this.zs.length > 0 && this.zs[0].isZero()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValid() {
/* 292 */     return implIsValid(false, true);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isValidPartial() {
/* 297 */     return implIsValid(false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean implIsValid(boolean paramBoolean1, boolean paramBoolean2) {
/* 302 */     if (isInfinity())
/*     */     {
/* 304 */       return true;
/*     */     }
/*     */     
/* 307 */     ValidityPrecompInfo validityPrecompInfo = (ValidityPrecompInfo)getCurve().precompute(this, "bc_validity", (PreCompCallback)new Object(this, paramBoolean1, paramBoolean2));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 343 */     return !validityPrecompInfo.hasFailed();
/*     */   }
/*     */ 
/*     */   
/*     */   public org.bouncycastle.math.ec.ECPoint scaleX(ECFieldElement paramECFieldElement) {
/* 348 */     return isInfinity() ? 
/* 349 */       this : 
/* 350 */       getCurve().createRawPoint(getRawXCoord().multiply(paramECFieldElement), getRawYCoord(), getRawZCoords());
/*     */   }
/*     */ 
/*     */   
/*     */   public org.bouncycastle.math.ec.ECPoint scaleXNegateY(ECFieldElement paramECFieldElement) {
/* 355 */     return isInfinity() ? 
/* 356 */       this : 
/* 357 */       getCurve().createRawPoint(getRawXCoord().multiply(paramECFieldElement), getRawYCoord().negate(), getRawZCoords());
/*     */   }
/*     */ 
/*     */   
/*     */   public org.bouncycastle.math.ec.ECPoint scaleY(ECFieldElement paramECFieldElement) {
/* 362 */     return isInfinity() ? 
/* 363 */       this : 
/* 364 */       getCurve().createRawPoint(getRawXCoord(), getRawYCoord().multiply(paramECFieldElement), getRawZCoords());
/*     */   }
/*     */ 
/*     */   
/*     */   public org.bouncycastle.math.ec.ECPoint scaleYNegateX(ECFieldElement paramECFieldElement) {
/* 369 */     return isInfinity() ? 
/* 370 */       this : 
/* 371 */       getCurve().createRawPoint(getRawXCoord().negate(), getRawYCoord().multiply(paramECFieldElement), getRawZCoords());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(org.bouncycastle.math.ec.ECPoint paramECPoint) {
/* 376 */     if (null == paramECPoint)
/*     */     {
/* 378 */       return false;
/*     */     }
/*     */     
/* 381 */     ECCurve eCCurve1 = getCurve(), eCCurve2 = paramECPoint.getCurve();
/* 382 */     boolean bool1 = (null == eCCurve1) ? true : false, bool2 = (null == eCCurve2) ? true : false;
/* 383 */     boolean bool3 = isInfinity(), bool4 = paramECPoint.isInfinity();
/*     */     
/* 385 */     if (bool3 || bool4)
/*     */     {
/* 387 */       return (bool3 && bool4 && (bool1 || bool2 || eCCurve1.equals(eCCurve2)));
/*     */     }
/*     */     
/* 390 */     org.bouncycastle.math.ec.ECPoint eCPoint1 = this, eCPoint2 = paramECPoint;
/* 391 */     if (!bool1 || !bool2)
/*     */     {
/*     */ 
/*     */       
/* 395 */       if (bool1) {
/*     */         
/* 397 */         eCPoint2 = eCPoint2.normalize();
/*     */       }
/* 399 */       else if (bool2) {
/*     */         
/* 401 */         eCPoint1 = eCPoint1.normalize();
/*     */       } else {
/* 403 */         if (!eCCurve1.equals(eCCurve2))
/*     */         {
/* 405 */           return false;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 411 */         org.bouncycastle.math.ec.ECPoint[] arrayOfECPoint = { this, eCCurve1.importPoint(eCPoint2) };
/*     */ 
/*     */         
/* 414 */         eCCurve1.normalizeAll(arrayOfECPoint);
/*     */         
/* 416 */         eCPoint1 = arrayOfECPoint[0];
/* 417 */         eCPoint2 = arrayOfECPoint[1];
/*     */       } 
/*     */     }
/* 420 */     return (eCPoint1.getXCoord().equals(eCPoint2.getXCoord()) && eCPoint1.getYCoord().equals(eCPoint2.getYCoord()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 425 */     if (paramObject == this)
/*     */     {
/* 427 */       return true;
/*     */     }
/*     */     
/* 430 */     if (!(paramObject instanceof org.bouncycastle.math.ec.ECPoint))
/*     */     {
/* 432 */       return false;
/*     */     }
/*     */     
/* 435 */     return equals((org.bouncycastle.math.ec.ECPoint)paramObject);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 440 */     ECCurve eCCurve = getCurve();
/* 441 */     int i = (null == eCCurve) ? 0 : (eCCurve.hashCode() ^ 0xFFFFFFFF);
/*     */     
/* 443 */     if (!isInfinity()) {
/*     */ 
/*     */ 
/*     */       
/* 447 */       org.bouncycastle.math.ec.ECPoint eCPoint = normalize();
/*     */       
/* 449 */       i ^= eCPoint.getXCoord().hashCode() * 17;
/* 450 */       i ^= eCPoint.getYCoord().hashCode() * 257;
/*     */     } 
/*     */     
/* 453 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 458 */     if (isInfinity())
/*     */     {
/* 460 */       return "INF";
/*     */     }
/*     */     
/* 463 */     StringBuffer stringBuffer = new StringBuffer();
/* 464 */     stringBuffer.append('(');
/* 465 */     stringBuffer.append(getRawXCoord());
/* 466 */     stringBuffer.append(',');
/* 467 */     stringBuffer.append(getRawYCoord());
/* 468 */     for (byte b = 0; b < this.zs.length; b++) {
/*     */       
/* 470 */       stringBuffer.append(',');
/* 471 */       stringBuffer.append(this.zs[b]);
/*     */     } 
/* 473 */     stringBuffer.append(')');
/* 474 */     return stringBuffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getEncoded(boolean paramBoolean) {
/* 485 */     if (isInfinity())
/*     */     {
/* 487 */       return new byte[1];
/*     */     }
/*     */     
/* 490 */     org.bouncycastle.math.ec.ECPoint eCPoint = normalize();
/*     */     
/* 492 */     byte[] arrayOfByte1 = eCPoint.getXCoord().getEncoded();
/*     */     
/* 494 */     if (paramBoolean) {
/*     */       
/* 496 */       byte[] arrayOfByte = new byte[arrayOfByte1.length + 1];
/* 497 */       arrayOfByte[0] = (byte)(eCPoint.getCompressionYTilde() ? 3 : 2);
/* 498 */       System.arraycopy(arrayOfByte1, 0, arrayOfByte, 1, arrayOfByte1.length);
/* 499 */       return arrayOfByte;
/*     */     } 
/*     */     
/* 502 */     byte[] arrayOfByte2 = eCPoint.getYCoord().getEncoded();
/*     */     
/* 504 */     byte[] arrayOfByte3 = new byte[arrayOfByte1.length + arrayOfByte2.length + 1];
/* 505 */     arrayOfByte3[0] = 4;
/* 506 */     System.arraycopy(arrayOfByte1, 0, arrayOfByte3, 1, arrayOfByte1.length);
/* 507 */     System.arraycopy(arrayOfByte2, 0, arrayOfByte3, arrayOfByte1.length + 1, arrayOfByte2.length);
/* 508 */     return arrayOfByte3;
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract boolean getCompressionYTilde();
/*     */   
/*     */   public abstract org.bouncycastle.math.ec.ECPoint add(org.bouncycastle.math.ec.ECPoint paramECPoint);
/*     */   
/*     */   public abstract org.bouncycastle.math.ec.ECPoint negate();
/*     */   
/*     */   public abstract org.bouncycastle.math.ec.ECPoint subtract(org.bouncycastle.math.ec.ECPoint paramECPoint);
/*     */   
/*     */   public org.bouncycastle.math.ec.ECPoint timesPow2(int paramInt) {
/* 521 */     if (paramInt < 0)
/*     */     {
/* 523 */       throw new IllegalArgumentException("'e' cannot be negative");
/*     */     }
/*     */     
/* 526 */     org.bouncycastle.math.ec.ECPoint eCPoint = this;
/* 527 */     while (--paramInt >= 0)
/*     */     {
/* 529 */       eCPoint = eCPoint.twice();
/*     */     }
/* 531 */     return eCPoint;
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract org.bouncycastle.math.ec.ECPoint twice();
/*     */   
/*     */   public org.bouncycastle.math.ec.ECPoint twicePlus(org.bouncycastle.math.ec.ECPoint paramECPoint) {
/* 538 */     return twice().add(paramECPoint);
/*     */   }
/*     */ 
/*     */   
/*     */   public org.bouncycastle.math.ec.ECPoint threeTimes() {
/* 543 */     return twicePlus(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public org.bouncycastle.math.ec.ECPoint multiply(BigInteger paramBigInteger) {
/* 553 */     return getCurve().getMultiplier().multiply(this, paramBigInteger);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/ECPoint.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */