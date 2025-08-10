/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.djb;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.SecureRandom;
/*     */ import org.bouncycastle.math.ec.ECConstants;
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.ECFieldElement;
/*     */ import org.bouncycastle.math.ec.ECLookupTable;
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ import org.bouncycastle.math.ec.custom.djb.Curve25519Field;
/*     */ import org.bouncycastle.math.ec.custom.djb.Curve25519FieldElement;
/*     */ import org.bouncycastle.math.ec.custom.djb.Curve25519Point;
/*     */ import org.bouncycastle.math.raw.Nat256;
/*     */ import org.bouncycastle.util.encoders.Hex;
/*     */ 
/*     */ public class Curve25519 extends ECCurve.AbstractFp {
/*  17 */   public static final BigInteger q = Curve25519FieldElement.Q;
/*     */   
/*  19 */   private static final BigInteger C_a = new BigInteger(1, Hex.decodeStrict("2AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA984914A144"));
/*  20 */   private static final BigInteger C_b = new BigInteger(1, Hex.decodeStrict("7B425ED097B425ED097B425ED097B425ED097B425ED097B4260B5E9C7710C864"));
/*     */   
/*     */   private static final int CURVE25519_DEFAULT_COORDS = 4;
/*  23 */   private static final ECFieldElement[] CURVE25519_AFFINE_ZS = new ECFieldElement[] { (ECFieldElement)new Curve25519FieldElement(ECConstants.ONE), (ECFieldElement)new Curve25519FieldElement(C_a) };
/*     */ 
/*     */   
/*     */   protected Curve25519Point infinity;
/*     */ 
/*     */   
/*     */   public Curve25519() {
/*  30 */     super(q);
/*     */     
/*  32 */     this.infinity = new Curve25519Point((ECCurve)this, null, null);
/*     */     
/*  34 */     this.a = fromBigInteger(C_a);
/*  35 */     this.b = fromBigInteger(C_b);
/*  36 */     this.order = new BigInteger(1, Hex.decodeStrict("1000000000000000000000000000000014DEF9DEA2F79CD65812631A5CF5D3ED"));
/*  37 */     this.cofactor = BigInteger.valueOf(8L);
/*     */     
/*  39 */     this.coord = 4;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECCurve cloneCurve() {
/*  44 */     return (ECCurve)new org.bouncycastle.math.ec.custom.djb.Curve25519();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsCoordinateSystem(int paramInt) {
/*  49 */     switch (paramInt) {
/*     */       
/*     */       case 4:
/*  52 */         return true;
/*     */     } 
/*  54 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BigInteger getQ() {
/*  60 */     return q;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFieldSize() {
/*  65 */     return q.bitLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement fromBigInteger(BigInteger paramBigInteger) {
/*  70 */     return (ECFieldElement)new Curve25519FieldElement(paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint createRawPoint(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2) {
/*  75 */     return (ECPoint)new Curve25519Point((ECCurve)this, paramECFieldElement1, paramECFieldElement2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ECPoint createRawPoint(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement[] paramArrayOfECFieldElement) {
/*  80 */     return (ECPoint)new Curve25519Point((ECCurve)this, paramECFieldElement1, paramECFieldElement2, paramArrayOfECFieldElement);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint getInfinity() {
/*  85 */     return (ECPoint)this.infinity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ECLookupTable createCacheSafeLookupTable(ECPoint[] paramArrayOfECPoint, int paramInt1, int paramInt2) {
/*  92 */     int[] arrayOfInt = new int[paramInt2 * 8 * 2];
/*     */     
/*  94 */     boolean bool = false;
/*  95 */     for (byte b = 0; b < paramInt2; b++) {
/*     */       
/*  97 */       ECPoint eCPoint = paramArrayOfECPoint[paramInt1 + b];
/*  98 */       Nat256.copy(((Curve25519FieldElement)eCPoint.getRawXCoord()).x, 0, arrayOfInt, bool); bool += true;
/*  99 */       Nat256.copy(((Curve25519FieldElement)eCPoint.getRawYCoord()).x, 0, arrayOfInt, bool); bool += true;
/*     */     } 
/*     */ 
/*     */     
/* 103 */     return (ECLookupTable)new Object(this, paramInt2, arrayOfInt);
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
/* 154 */     int[] arrayOfInt = Nat256.create();
/* 155 */     Curve25519Field.random(paramSecureRandom, arrayOfInt);
/* 156 */     return (ECFieldElement)new Curve25519FieldElement(arrayOfInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECFieldElement randomFieldElementMult(SecureRandom paramSecureRandom) {
/* 161 */     int[] arrayOfInt = Nat256.create();
/* 162 */     Curve25519Field.randomMult(paramSecureRandom, arrayOfInt);
/* 163 */     return (ECFieldElement)new Curve25519FieldElement(arrayOfInt);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/djb/Curve25519.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */