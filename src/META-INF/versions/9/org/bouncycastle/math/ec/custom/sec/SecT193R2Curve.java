/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.ec.ECConstants;
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.ECLookupTable;
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecT193FieldElement;
/*     */ import org.bouncycastle.math.ec.custom.sec.SecT193R2Point;
/*     */ import org.bouncycastle.math.raw.Nat256;
/*     */ import org.bouncycastle.util.encoders.Hex;
/*     */ 
/*     */ public class SecT193R2Curve
/*     */   extends ECCurve.AbstractF2m
/*     */ {
/*     */   private static final int SECT193R2_DEFAULT_COORDS = 6;
/*  18 */   private static final ECFieldElement[] SECT193R2_AFFINE_ZS = new ECFieldElement[] { (ECFieldElement)new SecT193FieldElement(ECConstants.ONE) };
/*     */   
/*     */   protected SecT193R2Point infinity;
/*     */ 
/*     */   
/*     */   public SecT193R2Curve() {
/*  24 */     super(193, 15, 0, 0);
/*     */     
/*  26 */     this.infinity = new SecT193R2Point((ECCurve)this, null, null);
/*     */     
/*  28 */     this.a = fromBigInteger(new BigInteger(1, Hex.decodeStrict("0163F35A5137C2CE3EA6ED8667190B0BC43ECD69977702709B")));
/*  29 */     this.b = fromBigInteger(new BigInteger(1, Hex.decodeStrict("00C9BB9E8927D4D64C377E2AB2856A5B16E3EFB7F61D4316AE")));
/*  30 */     this.order = new BigInteger(1, Hex.decodeStrict("010000000000000000000000015AAB561B005413CCD4EE99D5"));
/*  31 */     this.cofactor = BigInteger.valueOf(2L);
/*     */     
/*  33 */     this.coord = 6;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECCurve cloneCurve() {
/*  38 */     return (ECCurve)new org.bouncycastle.math.ec.custom.sec.SecT193R2Curve();
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
/*  54 */     return 193;
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement fromBigInteger(BigInteger paramBigInteger) {
/*  59 */     return (ECFieldElement)new SecT193FieldElement(paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint createRawPoint(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2) {
/*  64 */     return (ECPoint)new SecT193R2Point((ECCurve)this, paramECFieldElement1, paramECFieldElement2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint createRawPoint(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement[] paramArrayOfECFieldElement) {
/*  69 */     return (ECPoint)new SecT193R2Point((ECCurve)this, paramECFieldElement1, paramECFieldElement2, paramArrayOfECFieldElement);
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
/*  84 */     return 193;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTrinomial() {
/*  89 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getK1() {
/*  94 */     return 15;
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
/* 117 */       Nat256.copy64(((SecT193FieldElement)eCPoint.getRawXCoord()).x, 0, arrayOfLong, bool); bool += true;
/* 118 */       Nat256.copy64(((SecT193FieldElement)eCPoint.getRawYCoord()).x, 0, arrayOfLong, bool); bool += true;
/*     */     } 
/*     */ 
/*     */     
/* 122 */     return (ECLookupTable)new Object(this, paramInt2, arrayOfLong);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecT193R2Curve.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */