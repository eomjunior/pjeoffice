/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.ec.ECConstants;
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.ECLookupTable;
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecT571FieldElement;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecT571R1Point;
/*     */ import org.bouncycastle.math.raw.Nat576;
/*     */ import org.bouncycastle.util.encoders.Hex;
/*     */ 
/*     */ public class SecT571R1Curve
/*     */   extends ECCurve.AbstractF2m
/*     */ {
/*     */   private static final int SECT571R1_DEFAULT_COORDS = 6;
/*  18 */   private static final ECFieldElement[] SECT571R1_AFFINE_ZS = new ECFieldElement[] { (ECFieldElement)new SecT571FieldElement(ECConstants.ONE) };
/*     */   
/*     */   protected SecT571R1Point infinity;
/*     */   
/*  22 */   static final SecT571FieldElement SecT571R1_B = new SecT571FieldElement(new BigInteger(1, 
/*  23 */         Hex.decodeStrict("02F40E7E2221F295DE297117B7F3D62F5C6A97FFCB8CEFF1CD6BA8CE4A9A18AD84FFABBD8EFA59332BE7AD6756A66E294AFD185A78FF12AA520E4DE739BACA0C7FFEFF7F2955727A")));
/*  24 */   static final SecT571FieldElement SecT571R1_B_SQRT = (SecT571FieldElement)SecT571R1_B.sqrt();
/*     */ 
/*     */   
/*     */   public SecT571R1Curve() {
/*  28 */     super(571, 2, 5, 10);
/*     */     
/*  30 */     this.infinity = new SecT571R1Point((ECCurve)this, null, null);
/*     */     
/*  32 */     this.a = fromBigInteger(BigInteger.valueOf(1L));
/*  33 */     this.b = (ECFieldElement)SecT571R1_B;
/*  34 */     this.order = new BigInteger(1, Hex.decodeStrict("03FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFE661CE18FF55987308059B186823851EC7DD9CA1161DE93D5174D66E8382E9BB2FE84E47"));
/*  35 */     this.cofactor = BigInteger.valueOf(2L);
/*     */     
/*  37 */     this.coord = 6;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECCurve cloneCurve() {
/*  42 */     return (ECCurve)new org.bouncycastle.math.ec.custom.sec.SecT571R1Curve();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsCoordinateSystem(int paramInt) {
/*  47 */     switch (paramInt) {
/*     */       
/*     */       case 6:
/*  50 */         return true;
/*     */     } 
/*  52 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFieldSize() {
/*  58 */     return 571;
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement fromBigInteger(BigInteger paramBigInteger) {
/*  63 */     return (ECFieldElement)new SecT571FieldElement(paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint createRawPoint(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2) {
/*  68 */     return (ECPoint)new SecT571R1Point((ECCurve)this, paramECFieldElement1, paramECFieldElement2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint createRawPoint(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement[] paramArrayOfECFieldElement) {
/*  73 */     return (ECPoint)new SecT571R1Point((ECCurve)this, paramECFieldElement1, paramECFieldElement2, paramArrayOfECFieldElement);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint getInfinity() {
/*  78 */     return (ECPoint)this.infinity;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isKoblitz() {
/*  83 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getM() {
/*  88 */     return 571;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTrinomial() {
/*  93 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getK1() {
/*  98 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getK2() {
/* 103 */     return 5;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getK3() {
/* 108 */     return 10;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ECLookupTable createCacheSafeLookupTable(ECPoint[] paramArrayOfECPoint, int paramInt1, int paramInt2) {
/* 115 */     long[] arrayOfLong = new long[paramInt2 * 9 * 2];
/*     */     
/* 117 */     boolean bool = false;
/* 118 */     for (byte b = 0; b < paramInt2; b++) {
/*     */       
/* 120 */       ECPoint eCPoint = paramArrayOfECPoint[paramInt1 + b];
/* 121 */       Nat576.copy64(((SecT571FieldElement)eCPoint.getRawXCoord()).x, 0, arrayOfLong, bool); bool += true;
/* 122 */       Nat576.copy64(((SecT571FieldElement)eCPoint.getRawYCoord()).x, 0, arrayOfLong, bool); bool += true;
/*     */     } 
/*     */ 
/*     */     
/* 126 */     return (ECLookupTable)new Object(this, paramInt2, arrayOfLong);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecT571R1Curve.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */