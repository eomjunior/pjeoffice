/*     */ package META-INF.versions.9.org.bouncycastle.crypto.params;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.asn1.x9.X9ECParameters;
/*     */ import org.bouncycastle.math.ec.ECAlgorithms;
/*     */ import org.bouncycastle.math.ec.ECConstants;
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ import org.bouncycastle.util.BigIntegers;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ECDomainParameters
/*     */   implements ECConstants
/*     */ {
/*     */   private final ECCurve curve;
/*     */   private final byte[] seed;
/*     */   private final ECPoint G;
/*     */   private final BigInteger n;
/*     */   private final BigInteger h;
/*  22 */   private BigInteger hInv = null;
/*     */ 
/*     */   
/*     */   public ECDomainParameters(X9ECParameters paramX9ECParameters) {
/*  26 */     this(paramX9ECParameters.getCurve(), paramX9ECParameters.getG(), paramX9ECParameters.getN(), paramX9ECParameters.getH(), paramX9ECParameters.getSeed());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ECDomainParameters(ECCurve paramECCurve, ECPoint paramECPoint, BigInteger paramBigInteger) {
/*  34 */     this(paramECCurve, paramECPoint, paramBigInteger, ONE, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ECDomainParameters(ECCurve paramECCurve, ECPoint paramECPoint, BigInteger paramBigInteger1, BigInteger paramBigInteger2) {
/*  43 */     this(paramECCurve, paramECPoint, paramBigInteger1, paramBigInteger2, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ECDomainParameters(ECCurve paramECCurve, ECPoint paramECPoint, BigInteger paramBigInteger1, BigInteger paramBigInteger2, byte[] paramArrayOfbyte) {
/*  53 */     if (paramECCurve == null)
/*     */     {
/*  55 */       throw new NullPointerException("curve");
/*     */     }
/*  57 */     if (paramBigInteger1 == null)
/*     */     {
/*  59 */       throw new NullPointerException("n");
/*     */     }
/*     */ 
/*     */     
/*  63 */     this.curve = paramECCurve;
/*  64 */     this.G = validatePublicPoint(paramECCurve, paramECPoint);
/*  65 */     this.n = paramBigInteger1;
/*  66 */     this.h = paramBigInteger2;
/*  67 */     this.seed = Arrays.clone(paramArrayOfbyte);
/*     */   }
/*     */ 
/*     */   
/*     */   public ECCurve getCurve() {
/*  72 */     return this.curve;
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint getG() {
/*  77 */     return this.G;
/*     */   }
/*     */ 
/*     */   
/*     */   public BigInteger getN() {
/*  82 */     return this.n;
/*     */   }
/*     */ 
/*     */   
/*     */   public BigInteger getH() {
/*  87 */     return this.h;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized BigInteger getHInv() {
/*  92 */     if (this.hInv == null)
/*     */     {
/*  94 */       this.hInv = BigIntegers.modOddInverseVar(this.n, this.h);
/*     */     }
/*  96 */     return this.hInv;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getSeed() {
/* 101 */     return Arrays.clone(this.seed);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 107 */     if (this == paramObject)
/*     */     {
/* 109 */       return true;
/*     */     }
/*     */     
/* 112 */     if (!(paramObject instanceof org.bouncycastle.crypto.params.ECDomainParameters))
/*     */     {
/* 114 */       return false;
/*     */     }
/*     */     
/* 117 */     org.bouncycastle.crypto.params.ECDomainParameters eCDomainParameters = (org.bouncycastle.crypto.params.ECDomainParameters)paramObject;
/*     */     
/* 119 */     return (this.curve.equals(eCDomainParameters.curve) && this.G
/* 120 */       .equals(eCDomainParameters.G) && this.n
/* 121 */       .equals(eCDomainParameters.n));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 127 */     int i = 4;
/* 128 */     i *= 257;
/* 129 */     i ^= this.curve.hashCode();
/* 130 */     i *= 257;
/* 131 */     i ^= this.G.hashCode();
/* 132 */     i *= 257;
/* 133 */     i ^= this.n.hashCode();
/* 134 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public BigInteger validatePrivateScalar(BigInteger paramBigInteger) {
/* 139 */     if (null == paramBigInteger)
/*     */     {
/* 141 */       throw new NullPointerException("Scalar cannot be null");
/*     */     }
/*     */     
/* 144 */     if (paramBigInteger.compareTo(ECConstants.ONE) < 0 || paramBigInteger.compareTo(getN()) >= 0)
/*     */     {
/* 146 */       throw new IllegalArgumentException("Scalar is not in the interval [1, n - 1]");
/*     */     }
/*     */     
/* 149 */     return paramBigInteger;
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint validatePublicPoint(ECPoint paramECPoint) {
/* 154 */     return validatePublicPoint(getCurve(), paramECPoint);
/*     */   }
/*     */ 
/*     */   
/*     */   static ECPoint validatePublicPoint(ECCurve paramECCurve, ECPoint paramECPoint) {
/* 159 */     if (null == paramECPoint)
/*     */     {
/* 161 */       throw new NullPointerException("Point cannot be null");
/*     */     }
/*     */     
/* 164 */     paramECPoint = ECAlgorithms.importPoint(paramECCurve, paramECPoint).normalize();
/*     */     
/* 166 */     if (paramECPoint.isInfinity())
/*     */     {
/* 168 */       throw new IllegalArgumentException("Point at infinity");
/*     */     }
/*     */     
/* 171 */     if (!paramECPoint.isValid())
/*     */     {
/* 173 */       throw new IllegalArgumentException("Point not on curve");
/*     */     }
/*     */     
/* 176 */     return paramECPoint;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/crypto/params/ECDomainParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */