/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.ec.ECConstants;
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.ECLookupTable;
/*     */ import org.bouncycastle.math.ec.ECMultiplier;
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ import org.bouncycastle.math.ec.WTauNafMultiplier;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecT239FieldElement;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecT239K1Point;
/*     */ import org.bouncycastle.math.raw.Nat256;
/*     */ import org.bouncycastle.util.encoders.Hex;
/*     */ 
/*     */ public class SecT239K1Curve
/*     */   extends ECCurve.AbstractF2m
/*     */ {
/*     */   private static final int SECT239K1_DEFAULT_COORDS = 6;
/*  20 */   private static final ECFieldElement[] SECT239K1_AFFINE_ZS = new ECFieldElement[] { (ECFieldElement)new SecT239FieldElement(ECConstants.ONE) };
/*     */   
/*     */   protected SecT239K1Point infinity;
/*     */ 
/*     */   
/*     */   public SecT239K1Curve() {
/*  26 */     super(239, 158, 0, 0);
/*     */     
/*  28 */     this.infinity = new SecT239K1Point((ECCurve)this, null, null);
/*     */     
/*  30 */     this.a = fromBigInteger(BigInteger.valueOf(0L));
/*  31 */     this.b = fromBigInteger(BigInteger.valueOf(1L));
/*  32 */     this.order = new BigInteger(1, Hex.decodeStrict("2000000000000000000000000000005A79FEC67CB6E91F1C1DA800E478A5"));
/*  33 */     this.cofactor = BigInteger.valueOf(4L);
/*     */     
/*  35 */     this.coord = 6;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECCurve cloneCurve() {
/*  40 */     return (ECCurve)new org.bouncycastle.math.ec.custom.sec.SecT239K1Curve();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsCoordinateSystem(int paramInt) {
/*  45 */     switch (paramInt) {
/*     */       
/*     */       case 6:
/*  48 */         return true;
/*     */     } 
/*  50 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected ECMultiplier createDefaultMultiplier() {
/*  56 */     return (ECMultiplier)new WTauNafMultiplier();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFieldSize() {
/*  61 */     return 239;
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement fromBigInteger(BigInteger paramBigInteger) {
/*  66 */     return (ECFieldElement)new SecT239FieldElement(paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint createRawPoint(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2) {
/*  71 */     return (ECPoint)new SecT239K1Point((ECCurve)this, paramECFieldElement1, paramECFieldElement2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint createRawPoint(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement[] paramArrayOfECFieldElement) {
/*  76 */     return (ECPoint)new SecT239K1Point((ECCurve)this, paramECFieldElement1, paramECFieldElement2, paramArrayOfECFieldElement);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint getInfinity() {
/*  81 */     return (ECPoint)this.infinity;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isKoblitz() {
/*  86 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getM() {
/*  91 */     return 239;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTrinomial() {
/*  96 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getK1() {
/* 101 */     return 158;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getK2() {
/* 106 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getK3() {
/* 111 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ECLookupTable createCacheSafeLookupTable(ECPoint[] paramArrayOfECPoint, int paramInt1, int paramInt2) {
/* 118 */     long[] arrayOfLong = new long[paramInt2 * 4 * 2];
/*     */     
/* 120 */     boolean bool = false;
/* 121 */     for (byte b = 0; b < paramInt2; b++) {
/*     */       
/* 123 */       ECPoint eCPoint = paramArrayOfECPoint[paramInt1 + b];
/* 124 */       Nat256.copy64(((SecT239FieldElement)eCPoint.getRawXCoord()).x, 0, arrayOfLong, bool); bool += true;
/* 125 */       Nat256.copy64(((SecT239FieldElement)eCPoint.getRawYCoord()).x, 0, arrayOfLong, bool); bool += true;
/*     */     } 
/*     */ 
/*     */     
/* 129 */     return (ECLookupTable)new Object(this, paramInt2, arrayOfLong);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecT239K1Curve.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */