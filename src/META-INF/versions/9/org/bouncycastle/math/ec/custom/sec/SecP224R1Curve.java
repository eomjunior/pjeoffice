/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.SecureRandom;
/*     */ import org.bouncycastle.math.ec.ECConstants;
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.ECLookupTable;
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecP224R1Field;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecP224R1FieldElement;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecP224R1Point;
/*     */ import org.bouncycastle.math.raw.Nat224;
/*     */ import org.bouncycastle.util.encoders.Hex;
/*     */ 
/*     */ public class SecP224R1Curve extends ECCurve.AbstractFp {
/*  17 */   public static final BigInteger q = SecP224R1FieldElement.Q;
/*     */   
/*     */   private static final int SECP224R1_DEFAULT_COORDS = 2;
/*  20 */   private static final ECFieldElement[] SECP224R1_AFFINE_ZS = new ECFieldElement[] { (ECFieldElement)new SecP224R1FieldElement(ECConstants.ONE) };
/*     */   
/*     */   protected SecP224R1Point infinity;
/*     */ 
/*     */   
/*     */   public SecP224R1Curve() {
/*  26 */     super(q);
/*     */     
/*  28 */     this.infinity = new SecP224R1Point((ECCurve)this, null, null);
/*     */     
/*  30 */     this.a = fromBigInteger(new BigInteger(1, 
/*  31 */           Hex.decodeStrict("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFE")));
/*  32 */     this.b = fromBigInteger(new BigInteger(1, 
/*  33 */           Hex.decodeStrict("B4050A850C04B3ABF54132565044B0B7D7BFD8BA270B39432355FFB4")));
/*  34 */     this.order = new BigInteger(1, Hex.decodeStrict("FFFFFFFFFFFFFFFFFFFFFFFFFFFF16A2E0B8F03E13DD29455C5C2A3D"));
/*  35 */     this.cofactor = BigInteger.valueOf(1L);
/*     */     
/*  37 */     this.coord = 2;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECCurve cloneCurve() {
/*  42 */     return (ECCurve)new org.bouncycastle.math.ec.custom.sec.SecP224R1Curve();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsCoordinateSystem(int paramInt) {
/*  47 */     switch (paramInt) {
/*     */       
/*     */       case 2:
/*  50 */         return true;
/*     */     } 
/*  52 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BigInteger getQ() {
/*  58 */     return q;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFieldSize() {
/*  63 */     return q.bitLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement fromBigInteger(BigInteger paramBigInteger) {
/*  68 */     return (ECFieldElement)new SecP224R1FieldElement(paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint createRawPoint(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2) {
/*  73 */     return (ECPoint)new SecP224R1Point((ECCurve)this, paramECFieldElement1, paramECFieldElement2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint createRawPoint(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement[] paramArrayOfECFieldElement) {
/*  78 */     return (ECPoint)new SecP224R1Point((ECCurve)this, paramECFieldElement1, paramECFieldElement2, paramArrayOfECFieldElement);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint getInfinity() {
/*  83 */     return (ECPoint)this.infinity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ECLookupTable createCacheSafeLookupTable(ECPoint[] paramArrayOfECPoint, int paramInt1, int paramInt2) {
/*  90 */     int[] arrayOfInt = new int[paramInt2 * 7 * 2];
/*     */     
/*  92 */     boolean bool = false;
/*  93 */     for (byte b = 0; b < paramInt2; b++) {
/*     */       
/*  95 */       ECPoint eCPoint = paramArrayOfECPoint[paramInt1 + b];
/*  96 */       Nat224.copy(((SecP224R1FieldElement)eCPoint.getRawXCoord()).x, 0, arrayOfInt, bool); bool += true;
/*  97 */       Nat224.copy(((SecP224R1FieldElement)eCPoint.getRawYCoord()).x, 0, arrayOfInt, bool); bool += true;
/*     */     } 
/*     */ 
/*     */     
/* 101 */     return (ECLookupTable)new Object(this, paramInt2, arrayOfInt);
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
/* 152 */     int[] arrayOfInt = Nat224.create();
/* 153 */     SecP224R1Field.random(paramSecureRandom, arrayOfInt);
/* 154 */     return (ECFieldElement)new SecP224R1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement randomFieldElementMult(SecureRandom paramSecureRandom) {
/* 159 */     int[] arrayOfInt = Nat224.create();
/* 160 */     SecP224R1Field.randomMult(paramSecureRandom, arrayOfInt);
/* 161 */     return (ECFieldElement)new SecP224R1FieldElement(arrayOfInt);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecP224R1Curve.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */