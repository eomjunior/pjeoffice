/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.ec.ECConstants;
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.ECLookupTable;
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecT113FieldElement;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecT113R2Point;
/*     */ import org.bouncycastle.math.raw.Nat128;
/*     */ import org.bouncycastle.util.encoders.Hex;
/*     */ 
/*     */ public class SecT113R2Curve
/*     */   extends ECCurve.AbstractF2m
/*     */ {
/*     */   private static final int SECT113R2_DEFAULT_COORDS = 6;
/*  18 */   private static final ECFieldElement[] SECT113R2_AFFINE_ZS = new ECFieldElement[] { (ECFieldElement)new SecT113FieldElement(ECConstants.ONE) };
/*     */   
/*     */   protected SecT113R2Point infinity;
/*     */ 
/*     */   
/*     */   public SecT113R2Curve() {
/*  24 */     super(113, 9, 0, 0);
/*     */     
/*  26 */     this.infinity = new SecT113R2Point((ECCurve)this, null, null);
/*     */     
/*  28 */     this.a = fromBigInteger(new BigInteger(1, Hex.decodeStrict("00689918DBEC7E5A0DD6DFC0AA55C7")));
/*  29 */     this.b = fromBigInteger(new BigInteger(1, Hex.decodeStrict("0095E9A9EC9B297BD4BF36E059184F")));
/*  30 */     this.order = new BigInteger(1, Hex.decodeStrict("010000000000000108789B2496AF93"));
/*  31 */     this.cofactor = BigInteger.valueOf(2L);
/*     */     
/*  33 */     this.coord = 6;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECCurve cloneCurve() {
/*  38 */     return (ECCurve)new org.bouncycastle.math.ec.custom.sec.SecT113R2Curve();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsCoordinateSystem(int paramInt) {
/*  43 */     switch (paramInt) {
/*     */       
/*     */       case 6:
/*  46 */         return true;
/*     */     } 
/*  48 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFieldSize() {
/*  54 */     return 113;
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement fromBigInteger(BigInteger paramBigInteger) {
/*  59 */     return (ECFieldElement)new SecT113FieldElement(paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint createRawPoint(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2) {
/*  64 */     return (ECPoint)new SecT113R2Point((ECCurve)this, paramECFieldElement1, paramECFieldElement2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint createRawPoint(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement[] paramArrayOfECFieldElement) {
/*  69 */     return (ECPoint)new SecT113R2Point((ECCurve)this, paramECFieldElement1, paramECFieldElement2, paramArrayOfECFieldElement);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint getInfinity() {
/*  74 */     return (ECPoint)this.infinity;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isKoblitz() {
/*  79 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getM() {
/*  84 */     return 113;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTrinomial() {
/*  89 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getK1() {
/*  94 */     return 9;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getK2() {
/*  99 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getK3() {
/* 104 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ECLookupTable createCacheSafeLookupTable(ECPoint[] paramArrayOfECPoint, int paramInt1, int paramInt2) {
/* 111 */     long[] arrayOfLong = new long[paramInt2 * 2 * 2];
/*     */     
/* 113 */     boolean bool = false;
/* 114 */     for (byte b = 0; b < paramInt2; b++) {
/*     */       
/* 116 */       ECPoint eCPoint = paramArrayOfECPoint[paramInt1 + b];
/* 117 */       Nat128.copy64(((SecT113FieldElement)eCPoint.getRawXCoord()).x, 0, arrayOfLong, bool); bool += true;
/* 118 */       Nat128.copy64(((SecT113FieldElement)eCPoint.getRawYCoord()).x, 0, arrayOfLong, bool); bool += true;
/*     */     } 
/*     */ 
/*     */     
/* 122 */     return (ECLookupTable)new Object(this, paramInt2, arrayOfLong);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecT113R2Curve.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */