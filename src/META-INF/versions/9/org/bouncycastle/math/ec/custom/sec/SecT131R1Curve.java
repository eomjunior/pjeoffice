/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.ec.ECConstants;
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.ECLookupTable;
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecT131FieldElement;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecT131R1Point;
/*     */ import org.bouncycastle.math.raw.Nat192;
/*     */ import org.bouncycastle.util.encoders.Hex;
/*     */ 
/*     */ public class SecT131R1Curve
/*     */   extends ECCurve.AbstractF2m
/*     */ {
/*     */   private static final int SECT131R1_DEFAULT_COORDS = 6;
/*  18 */   private static final ECFieldElement[] SECT131R1_AFFINE_ZS = new ECFieldElement[] { (ECFieldElement)new SecT131FieldElement(ECConstants.ONE) };
/*     */   
/*     */   protected SecT131R1Point infinity;
/*     */ 
/*     */   
/*     */   public SecT131R1Curve() {
/*  24 */     super(131, 2, 3, 8);
/*     */     
/*  26 */     this.infinity = new SecT131R1Point((ECCurve)this, null, null);
/*     */     
/*  28 */     this.a = fromBigInteger(new BigInteger(1, Hex.decodeStrict("07A11B09A76B562144418FF3FF8C2570B8")));
/*  29 */     this.b = fromBigInteger(new BigInteger(1, Hex.decodeStrict("0217C05610884B63B9C6C7291678F9D341")));
/*  30 */     this.order = new BigInteger(1, Hex.decodeStrict("0400000000000000023123953A9464B54D"));
/*  31 */     this.cofactor = BigInteger.valueOf(2L);
/*     */     
/*  33 */     this.coord = 6;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECCurve cloneCurve() {
/*  38 */     return (ECCurve)new org.bouncycastle.math.ec.custom.sec.SecT131R1Curve();
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
/*  54 */     return 131;
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement fromBigInteger(BigInteger paramBigInteger) {
/*  59 */     return (ECFieldElement)new SecT131FieldElement(paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint createRawPoint(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2) {
/*  64 */     return (ECPoint)new SecT131R1Point((ECCurve)this, paramECFieldElement1, paramECFieldElement2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint createRawPoint(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement[] paramArrayOfECFieldElement) {
/*  69 */     return (ECPoint)new SecT131R1Point((ECCurve)this, paramECFieldElement1, paramECFieldElement2, paramArrayOfECFieldElement);
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
/*  84 */     return 131;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTrinomial() {
/*  89 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getK1() {
/*  94 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getK2() {
/*  99 */     return 3;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getK3() {
/* 104 */     return 8;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ECLookupTable createCacheSafeLookupTable(ECPoint[] paramArrayOfECPoint, int paramInt1, int paramInt2) {
/* 111 */     long[] arrayOfLong = new long[paramInt2 * 3 * 2];
/*     */     
/* 113 */     boolean bool = false;
/* 114 */     for (byte b = 0; b < paramInt2; b++) {
/*     */       
/* 116 */       ECPoint eCPoint = paramArrayOfECPoint[paramInt1 + b];
/* 117 */       Nat192.copy64(((SecT131FieldElement)eCPoint.getRawXCoord()).x, 0, arrayOfLong, bool); bool += true;
/* 118 */       Nat192.copy64(((SecT131FieldElement)eCPoint.getRawYCoord()).x, 0, arrayOfLong, bool); bool += true;
/*     */     } 
/*     */ 
/*     */     
/* 122 */     return (ECLookupTable)new Object(this, paramInt2, arrayOfLong);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecT131R1Curve.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */