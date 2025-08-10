/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.gm;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.SecureRandom;
/*     */ import org.bouncycastle.math.ec.ECConstants;
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.ECLookupTable;
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ import org.bouncycastle.math.ec.custom.gm.SM2P256V1Field;
/*     */ import org.bouncycastle.math.ec.custom.gm.SM2P256V1FieldElement;
/*     */ import org.bouncycastle.math.ec.custom.gm.SM2P256V1Point;
/*     */ import org.bouncycastle.math.raw.Nat256;
/*     */ import org.bouncycastle.util.encoders.Hex;
/*     */ 
/*     */ public class SM2P256V1Curve extends ECCurve.AbstractFp {
/*  17 */   public static final BigInteger q = SM2P256V1FieldElement.Q;
/*     */   
/*     */   private static final int SM2P256V1_DEFAULT_COORDS = 2;
/*  20 */   private static final ECFieldElement[] SM2P256V1_AFFINE_ZS = new ECFieldElement[] { (ECFieldElement)new SM2P256V1FieldElement(ECConstants.ONE) };
/*     */   
/*     */   protected SM2P256V1Point infinity;
/*     */ 
/*     */   
/*     */   public SM2P256V1Curve() {
/*  26 */     super(q);
/*     */     
/*  28 */     this.infinity = new SM2P256V1Point((ECCurve)this, null, null);
/*     */     
/*  30 */     this.a = fromBigInteger(new BigInteger(1, 
/*  31 */           Hex.decodeStrict("FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFC")));
/*  32 */     this.b = fromBigInteger(new BigInteger(1, 
/*  33 */           Hex.decodeStrict("28E9FA9E9D9F5E344D5A9E4BCF6509A7F39789F515AB8F92DDBCBD414D940E93")));
/*  34 */     this.order = new BigInteger(1, Hex.decodeStrict("FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFF7203DF6B21C6052B53BBF40939D54123"));
/*  35 */     this.cofactor = BigInteger.valueOf(1L);
/*     */     
/*  37 */     this.coord = 2;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECCurve cloneCurve() {
/*  42 */     return (ECCurve)new org.bouncycastle.math.ec.custom.gm.SM2P256V1Curve();
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
/*  68 */     return (ECFieldElement)new SM2P256V1FieldElement(paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint createRawPoint(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2) {
/*  73 */     return (ECPoint)new SM2P256V1Point((ECCurve)this, paramECFieldElement1, paramECFieldElement2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint createRawPoint(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement[] paramArrayOfECFieldElement) {
/*  78 */     return (ECPoint)new SM2P256V1Point((ECCurve)this, paramECFieldElement1, paramECFieldElement2, paramArrayOfECFieldElement);
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
/*  90 */     int[] arrayOfInt = new int[paramInt2 * 8 * 2];
/*     */     
/*  92 */     boolean bool = false;
/*  93 */     for (byte b = 0; b < paramInt2; b++) {
/*     */       
/*  95 */       ECPoint eCPoint = paramArrayOfECPoint[paramInt1 + b];
/*  96 */       Nat256.copy(((SM2P256V1FieldElement)eCPoint.getRawXCoord()).x, 0, arrayOfInt, bool); bool += true;
/*  97 */       Nat256.copy(((SM2P256V1FieldElement)eCPoint.getRawYCoord()).x, 0, arrayOfInt, bool); bool += true;
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
/* 152 */     int[] arrayOfInt = Nat256.create();
/* 153 */     SM2P256V1Field.random(paramSecureRandom, arrayOfInt);
/* 154 */     return (ECFieldElement)new SM2P256V1FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement randomFieldElementMult(SecureRandom paramSecureRandom) {
/* 159 */     int[] arrayOfInt = Nat256.create();
/* 160 */     SM2P256V1Field.randomMult(paramSecureRandom, arrayOfInt);
/* 161 */     return (ECFieldElement)new SM2P256V1FieldElement(arrayOfInt);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/gm/SM2P256V1Curve.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */