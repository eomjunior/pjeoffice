/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.SecureRandom;
/*     */ import org.bouncycastle.math.ec.ECConstants;
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.ECLookupTable;
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecP192K1Field;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecP192K1FieldElement;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecP192K1Point;
/*     */ import org.bouncycastle.math.raw.Nat192;
/*     */ import org.bouncycastle.util.encoders.Hex;
/*     */ 
/*     */ public class SecP192K1Curve extends ECCurve.AbstractFp {
/*  17 */   public static final BigInteger q = SecP192K1FieldElement.Q;
/*     */   
/*     */   private static final int SECP192K1_DEFAULT_COORDS = 2;
/*  20 */   private static final ECFieldElement[] SECP192K1_AFFINE_ZS = new ECFieldElement[] { (ECFieldElement)new SecP192K1FieldElement(ECConstants.ONE) };
/*     */   
/*     */   protected SecP192K1Point infinity;
/*     */ 
/*     */   
/*     */   public SecP192K1Curve() {
/*  26 */     super(q);
/*     */     
/*  28 */     this.infinity = new SecP192K1Point((ECCurve)this, null, null);
/*     */     
/*  30 */     this.a = fromBigInteger(ECConstants.ZERO);
/*  31 */     this.b = fromBigInteger(BigInteger.valueOf(3L));
/*  32 */     this.order = new BigInteger(1, Hex.decodeStrict("FFFFFFFFFFFFFFFFFFFFFFFE26F2FC170F69466A74DEFD8D"));
/*  33 */     this.cofactor = BigInteger.valueOf(1L);
/*     */     
/*  35 */     this.coord = 2;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECCurve cloneCurve() {
/*  40 */     return (ECCurve)new org.bouncycastle.math.ec.custom.sec.SecP192K1Curve();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsCoordinateSystem(int paramInt) {
/*  45 */     switch (paramInt) {
/*     */       
/*     */       case 2:
/*  48 */         return true;
/*     */     } 
/*  50 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BigInteger getQ() {
/*  56 */     return q;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFieldSize() {
/*  61 */     return q.bitLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement fromBigInteger(BigInteger paramBigInteger) {
/*  66 */     return (ECFieldElement)new SecP192K1FieldElement(paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint createRawPoint(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2) {
/*  71 */     return (ECPoint)new SecP192K1Point((ECCurve)this, paramECFieldElement1, paramECFieldElement2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint createRawPoint(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement[] paramArrayOfECFieldElement) {
/*  76 */     return (ECPoint)new SecP192K1Point((ECCurve)this, paramECFieldElement1, paramECFieldElement2, paramArrayOfECFieldElement);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint getInfinity() {
/*  81 */     return (ECPoint)this.infinity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ECLookupTable createCacheSafeLookupTable(ECPoint[] paramArrayOfECPoint, int paramInt1, int paramInt2) {
/*  88 */     int[] arrayOfInt = new int[paramInt2 * 6 * 2];
/*     */     
/*  90 */     boolean bool = false;
/*  91 */     for (byte b = 0; b < paramInt2; b++) {
/*     */       
/*  93 */       ECPoint eCPoint = paramArrayOfECPoint[paramInt1 + b];
/*  94 */       Nat192.copy(((SecP192K1FieldElement)eCPoint.getRawXCoord()).x, 0, arrayOfInt, bool); bool += true;
/*  95 */       Nat192.copy(((SecP192K1FieldElement)eCPoint.getRawYCoord()).x, 0, arrayOfInt, bool); bool += true;
/*     */     } 
/*     */ 
/*     */     
/*  99 */     return (ECLookupTable)new Object(this, paramInt2, arrayOfInt);
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
/*     */   public ECFieldElement randomFieldElement(SecureRandom paramSecureRandom) {
/* 150 */     int[] arrayOfInt = Nat192.create();
/* 151 */     SecP192K1Field.random(paramSecureRandom, arrayOfInt);
/* 152 */     return (ECFieldElement)new SecP192K1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement randomFieldElementMult(SecureRandom paramSecureRandom) {
/* 157 */     int[] arrayOfInt = Nat192.create();
/* 158 */     SecP192K1Field.randomMult(paramSecureRandom, arrayOfInt);
/* 159 */     return (ECFieldElement)new SecP192K1FieldElement(arrayOfInt);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecP192K1Curve.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */