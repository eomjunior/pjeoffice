/*     */ package META-INF.versions.9.org.bouncycastle.pqc.jcajce.provider.mceliece;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.PrivateKey;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
/*     */ import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
/*     */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
/*     */ import org.bouncycastle.pqc.asn1.McElieceCCA2PrivateKey;
/*     */ import org.bouncycastle.pqc.asn1.PQCObjectIdentifiers;
/*     */ import org.bouncycastle.pqc.crypto.mceliece.McElieceCCA2PrivateKeyParameters;
/*     */ import org.bouncycastle.pqc.jcajce.provider.mceliece.Utils;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.GF2Matrix;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.GF2mField;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.Permutation;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BCMcElieceCCA2PrivateKey
/*     */   implements PrivateKey
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private McElieceCCA2PrivateKeyParameters params;
/*     */   
/*     */   public BCMcElieceCCA2PrivateKey(McElieceCCA2PrivateKeyParameters paramMcElieceCCA2PrivateKeyParameters) {
/*  33 */     this.params = paramMcElieceCCA2PrivateKeyParameters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAlgorithm() {
/*  43 */     return "McEliece-CCA2";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getN() {
/*  51 */     return this.params.getN();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getK() {
/*  59 */     return this.params.getK();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getT() {
/*  67 */     return this.params.getGoppaPoly().getDegree();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GF2mField getField() {
/*  75 */     return this.params.getField();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PolynomialGF2mSmallM getGoppaPoly() {
/*  83 */     return this.params.getGoppaPoly();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Permutation getP() {
/*  91 */     return this.params.getP();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GF2Matrix getH() {
/*  99 */     return this.params.getH();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PolynomialGF2mSmallM[] getQInv() {
/* 107 */     return this.params.getQInv();
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
/*     */   public boolean equals(Object paramObject) {
/* 131 */     if (paramObject == null || !(paramObject instanceof org.bouncycastle.pqc.jcajce.provider.mceliece.BCMcElieceCCA2PrivateKey))
/*     */     {
/* 133 */       return false;
/*     */     }
/*     */     
/* 136 */     org.bouncycastle.pqc.jcajce.provider.mceliece.BCMcElieceCCA2PrivateKey bCMcElieceCCA2PrivateKey = (org.bouncycastle.pqc.jcajce.provider.mceliece.BCMcElieceCCA2PrivateKey)paramObject;
/*     */     
/* 138 */     return (getN() == bCMcElieceCCA2PrivateKey.getN() && getK() == bCMcElieceCCA2PrivateKey.getK() && 
/* 139 */       getField().equals(bCMcElieceCCA2PrivateKey.getField()) && 
/* 140 */       getGoppaPoly().equals(bCMcElieceCCA2PrivateKey.getGoppaPoly()) && getP().equals(bCMcElieceCCA2PrivateKey.getP()) && 
/* 141 */       getH().equals(bCMcElieceCCA2PrivateKey.getH()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 149 */     int i = this.params.getK();
/*     */     
/* 151 */     i = i * 37 + this.params.getN();
/* 152 */     i = i * 37 + this.params.getField().hashCode();
/* 153 */     i = i * 37 + this.params.getGoppaPoly().hashCode();
/* 154 */     i = i * 37 + this.params.getP().hashCode();
/*     */     
/* 156 */     return i * 37 + this.params.getH().hashCode();
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
/*     */   public byte[] getEncoded() {
/*     */     try {
/* 181 */       McElieceCCA2PrivateKey mcElieceCCA2PrivateKey = new McElieceCCA2PrivateKey(getN(), getK(), getField(), getGoppaPoly(), getP(), Utils.getDigAlgId(this.params.getDigest()));
/* 182 */       AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PQCObjectIdentifiers.mcElieceCca2);
/*     */       
/* 184 */       PrivateKeyInfo privateKeyInfo = new PrivateKeyInfo(algorithmIdentifier, (ASN1Encodable)mcElieceCCA2PrivateKey);
/*     */       
/* 186 */       return privateKeyInfo.getEncoded();
/*     */     }
/* 188 */     catch (IOException iOException) {
/*     */       
/* 190 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFormat() {
/* 196 */     return "PKCS#8";
/*     */   }
/*     */ 
/*     */   
/*     */   AsymmetricKeyParameter getKeyParams() {
/* 201 */     return (AsymmetricKeyParameter)this.params;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/jcajce/provider/mceliece/BCMcElieceCCA2PrivateKey.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */