/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.SecureRandom;
/*     */ import org.bouncycastle.math.ec.ECConstants;
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.ECLookupTable;
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecP160R2Field;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecP160R2FieldElement;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecP160R2Point;
/*     */ import org.bouncycastle.math.raw.Nat160;
/*     */ import org.bouncycastle.util.encoders.Hex;
/*     */ 
/*     */ public class SecP160R2Curve extends ECCurve.AbstractFp {
/*  17 */   public static final BigInteger q = SecP160R2FieldElement.Q;
/*     */   
/*     */   private static final int SECP160R2_DEFAULT_COORDS = 2;
/*  20 */   private static final ECFieldElement[] SECP160R2_AFFINE_ZS = new ECFieldElement[] { (ECFieldElement)new SecP160R2FieldElement(ECConstants.ONE) };
/*     */   
/*     */   protected SecP160R2Point infinity;
/*     */ 
/*     */   
/*     */   public SecP160R2Curve() {
/*  26 */     super(q);
/*     */     
/*  28 */     this.infinity = new SecP160R2Point((ECCurve)this, null, null);
/*     */     
/*  30 */     this.a = fromBigInteger(new BigInteger(1, 
/*  31 */           Hex.decodeStrict("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFAC70")));
/*  32 */     this.b = fromBigInteger(new BigInteger(1, 
/*  33 */           Hex.decodeStrict("B4E134D3FB59EB8BAB57274904664D5AF50388BA")));
/*  34 */     this.order = new BigInteger(1, Hex.decodeStrict("0100000000000000000000351EE786A818F3A1A16B"));
/*  35 */     this.cofactor = BigInteger.valueOf(1L);
/*     */     
/*  37 */     this.coord = 2;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECCurve cloneCurve() {
/*  42 */     return (ECCurve)new org.bouncycastle.math.ec.custom.sec.SecP160R2Curve();
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
/*  68 */     return (ECFieldElement)new SecP160R2FieldElement(paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint createRawPoint(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2) {
/*  73 */     return (ECPoint)new SecP160R2Point((ECCurve)this, paramECFieldElement1, paramECFieldElement2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint createRawPoint(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement[] paramArrayOfECFieldElement) {
/*  78 */     return (ECPoint)new SecP160R2Point((ECCurve)this, paramECFieldElement1, paramECFieldElement2, paramArrayOfECFieldElement);
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
/*  90 */     int[] arrayOfInt = new int[paramInt2 * 5 * 2];
/*     */     
/*  92 */     boolean bool = false;
/*  93 */     for (byte b = 0; b < paramInt2; b++) {
/*     */       
/*  95 */       ECPoint eCPoint = paramArrayOfECPoint[paramInt1 + b];
/*  96 */       Nat160.copy(((SecP160R2FieldElement)eCPoint.getRawXCoord()).x, 0, arrayOfInt, bool); bool += true;
/*  97 */       Nat160.copy(((SecP160R2FieldElement)eCPoint.getRawYCoord()).x, 0, arrayOfInt, bool); bool += true;
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
/* 152 */     int[] arrayOfInt = Nat160.create();
/* 153 */     SecP160R2Field.random(paramSecureRandom, arrayOfInt);
/* 154 */     return (ECFieldElement)new SecP160R2FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement randomFieldElementMult(SecureRandom paramSecureRandom) {
/* 159 */     int[] arrayOfInt = Nat160.create();
/* 160 */     SecP160R2Field.randomMult(paramSecureRandom, arrayOfInt);
/* 161 */     return (ECFieldElement)new SecP160R2FieldElement(arrayOfInt);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecP160R2Curve.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */