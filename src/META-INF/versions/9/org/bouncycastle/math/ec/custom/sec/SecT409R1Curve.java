/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.ec.ECConstants;
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.ECLookupTable;
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecT409FieldElement;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecT409R1Point;
/*     */ import org.bouncycastle.math.raw.Nat448;
/*     */ import org.bouncycastle.util.encoders.Hex;
/*     */ 
/*     */ public class SecT409R1Curve
/*     */   extends ECCurve.AbstractF2m
/*     */ {
/*     */   private static final int SECT409R1_DEFAULT_COORDS = 6;
/*  18 */   private static final ECFieldElement[] SECT409R1_AFFINE_ZS = new ECFieldElement[] { (ECFieldElement)new SecT409FieldElement(ECConstants.ONE) };
/*     */   
/*     */   protected SecT409R1Point infinity;
/*     */ 
/*     */   
/*     */   public SecT409R1Curve() {
/*  24 */     super(409, 87, 0, 0);
/*     */     
/*  26 */     this.infinity = new SecT409R1Point((ECCurve)this, null, null);
/*     */     
/*  28 */     this.a = fromBigInteger(BigInteger.valueOf(1L));
/*  29 */     this.b = fromBigInteger(new BigInteger(1, Hex.decodeStrict("0021A5C2C8EE9FEB5C4B9A753B7B476B7FD6422EF1F3DD674761FA99D6AC27C8A9A197B272822F6CD57A55AA4F50AE317B13545F")));
/*  30 */     this.order = new BigInteger(1, Hex.decodeStrict("010000000000000000000000000000000000000000000000000001E2AAD6A612F33307BE5FA47C3C9E052F838164CD37D9A21173"));
/*  31 */     this.cofactor = BigInteger.valueOf(2L);
/*     */     
/*  33 */     this.coord = 6;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECCurve cloneCurve() {
/*  38 */     return (ECCurve)new org.bouncycastle.math.ec.custom.sec.SecT409R1Curve();
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
/*  54 */     return 409;
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement fromBigInteger(BigInteger paramBigInteger) {
/*  59 */     return (ECFieldElement)new SecT409FieldElement(paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint createRawPoint(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2) {
/*  64 */     return (ECPoint)new SecT409R1Point((ECCurve)this, paramECFieldElement1, paramECFieldElement2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint createRawPoint(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement[] paramArrayOfECFieldElement) {
/*  69 */     return (ECPoint)new SecT409R1Point((ECCurve)this, paramECFieldElement1, paramECFieldElement2, paramArrayOfECFieldElement);
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
/*  84 */     return 409;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTrinomial() {
/*  89 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getK1() {
/*  94 */     return 87;
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
/* 111 */     long[] arrayOfLong = new long[paramInt2 * 7 * 2];
/*     */     
/* 113 */     boolean bool = false;
/* 114 */     for (byte b = 0; b < paramInt2; b++) {
/*     */       
/* 116 */       ECPoint eCPoint = paramArrayOfECPoint[paramInt1 + b];
/* 117 */       Nat448.copy64(((SecT409FieldElement)eCPoint.getRawXCoord()).x, 0, arrayOfLong, bool); bool += true;
/* 118 */       Nat448.copy64(((SecT409FieldElement)eCPoint.getRawYCoord()).x, 0, arrayOfLong, bool); bool += true;
/*     */     } 
/*     */ 
/*     */     
/* 122 */     return (ECLookupTable)new Object(this, paramInt2, arrayOfLong);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecT409R1Curve.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */