/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.SecureRandom;
/*     */ import org.bouncycastle.math.ec.ECConstants;
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.ECLookupTable;
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecP256K1Field;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecP256K1FieldElement;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecP256K1Point;
/*     */ import org.bouncycastle.math.raw.Nat256;
/*     */ import org.bouncycastle.util.encoders.Hex;
/*     */ 
/*     */ public class SecP256K1Curve extends ECCurve.AbstractFp {
/*  17 */   public static final BigInteger q = SecP256K1FieldElement.Q;
/*     */   
/*     */   private static final int SECP256K1_DEFAULT_COORDS = 2;
/*  20 */   private static final ECFieldElement[] SECP256K1_AFFINE_ZS = new ECFieldElement[] { (ECFieldElement)new SecP256K1FieldElement(ECConstants.ONE) };
/*     */   
/*     */   protected SecP256K1Point infinity;
/*     */ 
/*     */   
/*     */   public SecP256K1Curve() {
/*  26 */     super(q);
/*     */     
/*  28 */     this.infinity = new SecP256K1Point((ECCurve)this, null, null);
/*     */     
/*  30 */     this.a = fromBigInteger(ECConstants.ZERO);
/*  31 */     this.b = fromBigInteger(BigInteger.valueOf(7L));
/*  32 */     this.order = new BigInteger(1, Hex.decodeStrict("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364141"));
/*  33 */     this.cofactor = BigInteger.valueOf(1L);
/*  34 */     this.coord = 2;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECCurve cloneCurve() {
/*  39 */     return (ECCurve)new org.bouncycastle.math.ec.custom.sec.SecP256K1Curve();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsCoordinateSystem(int paramInt) {
/*  44 */     switch (paramInt) {
/*     */       
/*     */       case 2:
/*  47 */         return true;
/*     */     } 
/*  49 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BigInteger getQ() {
/*  55 */     return q;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFieldSize() {
/*  60 */     return q.bitLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement fromBigInteger(BigInteger paramBigInteger) {
/*  65 */     return (ECFieldElement)new SecP256K1FieldElement(paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint createRawPoint(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2) {
/*  70 */     return (ECPoint)new SecP256K1Point((ECCurve)this, paramECFieldElement1, paramECFieldElement2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint createRawPoint(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement[] paramArrayOfECFieldElement) {
/*  75 */     return (ECPoint)new SecP256K1Point((ECCurve)this, paramECFieldElement1, paramECFieldElement2, paramArrayOfECFieldElement);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint getInfinity() {
/*  80 */     return (ECPoint)this.infinity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ECLookupTable createCacheSafeLookupTable(ECPoint[] paramArrayOfECPoint, int paramInt1, int paramInt2) {
/*  87 */     int[] arrayOfInt = new int[paramInt2 * 8 * 2];
/*     */     
/*  89 */     boolean bool = false;
/*  90 */     for (byte b = 0; b < paramInt2; b++) {
/*     */       
/*  92 */       ECPoint eCPoint = paramArrayOfECPoint[paramInt1 + b];
/*  93 */       Nat256.copy(((SecP256K1FieldElement)eCPoint.getRawXCoord()).x, 0, arrayOfInt, bool); bool += true;
/*  94 */       Nat256.copy(((SecP256K1FieldElement)eCPoint.getRawYCoord()).x, 0, arrayOfInt, bool); bool += true;
/*     */     } 
/*     */ 
/*     */     
/*  98 */     return (ECLookupTable)new Object(this, paramInt2, arrayOfInt);
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
/* 149 */     int[] arrayOfInt = Nat256.create();
/* 150 */     SecP256K1Field.random(paramSecureRandom, arrayOfInt);
/* 151 */     return (ECFieldElement)new SecP256K1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement randomFieldElementMult(SecureRandom paramSecureRandom) {
/* 156 */     int[] arrayOfInt = Nat256.create();
/* 157 */     SecP256K1Field.randomMult(paramSecureRandom, arrayOfInt);
/* 158 */     return (ECFieldElement)new SecP256K1FieldElement(arrayOfInt);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecP256K1Curve.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */