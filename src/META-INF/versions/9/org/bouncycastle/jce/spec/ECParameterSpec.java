/*     */ package META-INF.versions.9.org.bouncycastle.jce.spec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.spec.AlgorithmParameterSpec;
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ECParameterSpec
/*     */   implements AlgorithmParameterSpec
/*     */ {
/*     */   private ECCurve curve;
/*     */   private byte[] seed;
/*     */   private ECPoint G;
/*     */   private BigInteger n;
/*     */   private BigInteger h;
/*     */   
/*     */   public ECParameterSpec(ECCurve paramECCurve, ECPoint paramECPoint, BigInteger paramBigInteger) {
/*  26 */     this.curve = paramECCurve;
/*  27 */     this.G = paramECPoint.normalize();
/*  28 */     this.n = paramBigInteger;
/*  29 */     this.h = BigInteger.valueOf(1L);
/*  30 */     this.seed = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ECParameterSpec(ECCurve paramECCurve, ECPoint paramECPoint, BigInteger paramBigInteger1, BigInteger paramBigInteger2) {
/*  39 */     this.curve = paramECCurve;
/*  40 */     this.G = paramECPoint.normalize();
/*  41 */     this.n = paramBigInteger1;
/*  42 */     this.h = paramBigInteger2;
/*  43 */     this.seed = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ECParameterSpec(ECCurve paramECCurve, ECPoint paramECPoint, BigInteger paramBigInteger1, BigInteger paramBigInteger2, byte[] paramArrayOfbyte) {
/*  53 */     this.curve = paramECCurve;
/*  54 */     this.G = paramECPoint.normalize();
/*  55 */     this.n = paramBigInteger1;
/*  56 */     this.h = paramBigInteger2;
/*  57 */     this.seed = paramArrayOfbyte;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ECCurve getCurve() {
/*  66 */     return this.curve;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ECPoint getG() {
/*  75 */     return this.G;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BigInteger getN() {
/*  84 */     return this.n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BigInteger getH() {
/*  93 */     return this.h;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getSeed() {
/* 102 */     return this.seed;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 107 */     if (!(paramObject instanceof org.bouncycastle.jce.spec.ECParameterSpec))
/*     */     {
/* 109 */       return false;
/*     */     }
/*     */     
/* 112 */     org.bouncycastle.jce.spec.ECParameterSpec eCParameterSpec = (org.bouncycastle.jce.spec.ECParameterSpec)paramObject;
/*     */     
/* 114 */     return (getCurve().equals(eCParameterSpec.getCurve()) && getG().equals(eCParameterSpec.getG()));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 119 */     return getCurve().hashCode() ^ getG().hashCode();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/jce/spec/ECParameterSpec.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */