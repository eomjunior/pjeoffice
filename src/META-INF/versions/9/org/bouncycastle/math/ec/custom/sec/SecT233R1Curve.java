/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.ec.ECConstants;
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.ECLookupTable;
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecT233FieldElement;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecT233R1Point;
/*     */ import org.bouncycastle.math.raw.Nat256;
/*     */ import org.bouncycastle.util.encoders.Hex;
/*     */ 
/*     */ public class SecT233R1Curve
/*     */   extends ECCurve.AbstractF2m
/*     */ {
/*     */   private static final int SECT233R1_DEFAULT_COORDS = 6;
/*  18 */   private static final ECFieldElement[] SECT233R1_AFFINE_ZS = new ECFieldElement[] { (ECFieldElement)new SecT233FieldElement(ECConstants.ONE) };
/*     */   
/*     */   protected SecT233R1Point infinity;
/*     */ 
/*     */   
/*     */   public SecT233R1Curve() {
/*  24 */     super(233, 74, 0, 0);
/*     */     
/*  26 */     this.infinity = new SecT233R1Point((ECCurve)this, null, null);
/*     */     
/*  28 */     this.a = fromBigInteger(BigInteger.valueOf(1L));
/*  29 */     this.b = fromBigInteger(new BigInteger(1, Hex.decodeStrict("0066647EDE6C332C7F8C0923BB58213B333B20E9CE4281FE115F7D8F90AD")));
/*  30 */     this.order = new BigInteger(1, Hex.decodeStrict("01000000000000000000000000000013E974E72F8A6922031D2603CFE0D7"));
/*  31 */     this.cofactor = BigInteger.valueOf(2L);
/*     */     
/*  33 */     this.coord = 6;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECCurve cloneCurve() {
/*  38 */     return (ECCurve)new org.bouncycastle.math.ec.custom.sec.SecT233R1Curve();
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
/*  54 */     return 233;
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement fromBigInteger(BigInteger paramBigInteger) {
/*  59 */     return (ECFieldElement)new SecT233FieldElement(paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint createRawPoint(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2) {
/*  64 */     return (ECPoint)new SecT233R1Point((ECCurve)this, paramECFieldElement1, paramECFieldElement2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint createRawPoint(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement[] paramArrayOfECFieldElement) {
/*  69 */     return (ECPoint)new SecT233R1Point((ECCurve)this, paramECFieldElement1, paramECFieldElement2, paramArrayOfECFieldElement);
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
/*  84 */     return 233;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTrinomial() {
/*  89 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getK1() {
/*  94 */     return 74;
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
/* 111 */     long[] arrayOfLong = new long[paramInt2 * 4 * 2];
/*     */     
/* 113 */     boolean bool = false;
/* 114 */     for (byte b = 0; b < paramInt2; b++) {
/*     */       
/* 116 */       ECPoint eCPoint = paramArrayOfECPoint[paramInt1 + b];
/* 117 */       Nat256.copy64(((SecT233FieldElement)eCPoint.getRawXCoord()).x, 0, arrayOfLong, bool); bool += true;
/* 118 */       Nat256.copy64(((SecT233FieldElement)eCPoint.getRawYCoord()).x, 0, arrayOfLong, bool); bool += true;
/*     */     } 
/*     */ 
/*     */     
/* 122 */     return (ECLookupTable)new Object(this, paramInt2, arrayOfLong);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecT233R1Curve.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */