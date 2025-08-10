/*     */ package META-INF.versions.9.org.bouncycastle.pqc.jcajce.provider.rainbow;
/*     */ 
/*     */ import java.security.PublicKey;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.DERNull;
/*     */ import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
/*     */ import org.bouncycastle.pqc.asn1.PQCObjectIdentifiers;
/*     */ import org.bouncycastle.pqc.asn1.RainbowPublicKey;
/*     */ import org.bouncycastle.pqc.crypto.rainbow.RainbowParameters;
/*     */ import org.bouncycastle.pqc.crypto.rainbow.RainbowPublicKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.rainbow.util.RainbowUtil;
/*     */ import org.bouncycastle.pqc.jcajce.provider.util.KeyUtil;
/*     */ import org.bouncycastle.pqc.jcajce.spec.RainbowPublicKeySpec;
/*     */ import org.bouncycastle.util.Arrays;
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
/*     */ public class BCRainbowPublicKey
/*     */   implements PublicKey
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private short[][] coeffquadratic;
/*     */   private short[][] coeffsingular;
/*     */   private short[] coeffscalar;
/*     */   private int docLength;
/*     */   private RainbowParameters rainbowParams;
/*     */   
/*     */   public BCRainbowPublicKey(int paramInt, short[][] paramArrayOfshort1, short[][] paramArrayOfshort2, short[] paramArrayOfshort) {
/*  57 */     this.docLength = paramInt;
/*  58 */     this.coeffquadratic = paramArrayOfshort1;
/*  59 */     this.coeffsingular = paramArrayOfshort2;
/*  60 */     this.coeffscalar = paramArrayOfshort;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BCRainbowPublicKey(RainbowPublicKeySpec paramRainbowPublicKeySpec) {
/*  70 */     this(paramRainbowPublicKeySpec.getDocLength(), paramRainbowPublicKeySpec.getCoeffQuadratic(), paramRainbowPublicKeySpec
/*  71 */         .getCoeffSingular(), paramRainbowPublicKeySpec.getCoeffScalar());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BCRainbowPublicKey(RainbowPublicKeyParameters paramRainbowPublicKeyParameters) {
/*  77 */     this(paramRainbowPublicKeyParameters.getDocLength(), paramRainbowPublicKeyParameters.getCoeffQuadratic(), paramRainbowPublicKeyParameters.getCoeffSingular(), paramRainbowPublicKeyParameters.getCoeffScalar());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDocLength() {
/*  85 */     return this.docLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short[][] getCoeffQuadratic() {
/*  93 */     return this.coeffquadratic;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short[][] getCoeffSingular() {
/* 101 */     short[][] arrayOfShort = new short[this.coeffsingular.length][];
/*     */     
/* 103 */     for (byte b = 0; b != this.coeffsingular.length; b++)
/*     */     {
/* 105 */       arrayOfShort[b] = Arrays.clone(this.coeffsingular[b]);
/*     */     }
/*     */     
/* 108 */     return arrayOfShort;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short[] getCoeffScalar() {
/* 117 */     return Arrays.clone(this.coeffscalar);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 128 */     if (paramObject == null || !(paramObject instanceof org.bouncycastle.pqc.jcajce.provider.rainbow.BCRainbowPublicKey))
/*     */     {
/* 130 */       return false;
/*     */     }
/* 132 */     org.bouncycastle.pqc.jcajce.provider.rainbow.BCRainbowPublicKey bCRainbowPublicKey = (org.bouncycastle.pqc.jcajce.provider.rainbow.BCRainbowPublicKey)paramObject;
/*     */     
/* 134 */     return (this.docLength == bCRainbowPublicKey.getDocLength() && 
/* 135 */       RainbowUtil.equals(this.coeffquadratic, bCRainbowPublicKey.getCoeffQuadratic()) && 
/* 136 */       RainbowUtil.equals(this.coeffsingular, bCRainbowPublicKey.getCoeffSingular()) && 
/* 137 */       RainbowUtil.equals(this.coeffscalar, bCRainbowPublicKey.getCoeffScalar()));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 142 */     int i = this.docLength;
/*     */     
/* 144 */     i = i * 37 + Arrays.hashCode(this.coeffquadratic);
/* 145 */     i = i * 37 + Arrays.hashCode(this.coeffsingular);
/* 146 */     i = i * 37 + Arrays.hashCode(this.coeffscalar);
/*     */     
/* 148 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getAlgorithm() {
/* 156 */     return "Rainbow";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFormat() {
/* 161 */     return "X.509";
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getEncoded() {
/* 166 */     RainbowPublicKey rainbowPublicKey = new RainbowPublicKey(this.docLength, this.coeffquadratic, this.coeffsingular, this.coeffscalar);
/* 167 */     AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PQCObjectIdentifiers.rainbow, (ASN1Encodable)DERNull.INSTANCE);
/*     */     
/* 169 */     return KeyUtil.getEncodedSubjectPublicKeyInfo(algorithmIdentifier, (ASN1Encodable)rainbowPublicKey);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/jcajce/provider/rainbow/BCRainbowPublicKey.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */