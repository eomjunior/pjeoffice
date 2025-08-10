/*     */ package META-INF.versions.9.org.bouncycastle.pqc.jcajce.provider.mceliece;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.PrivateKey;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
/*     */ import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
/*     */ import org.bouncycastle.crypto.CipherParameters;
/*     */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
/*     */ import org.bouncycastle.pqc.asn1.McEliecePrivateKey;
/*     */ import org.bouncycastle.pqc.asn1.PQCObjectIdentifiers;
/*     */ import org.bouncycastle.pqc.crypto.mceliece.McEliecePrivateKeyParameters;
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
/*     */ public class BCMcEliecePrivateKey
/*     */   implements CipherParameters, PrivateKey
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private McEliecePrivateKeyParameters params;
/*     */   
/*     */   public BCMcEliecePrivateKey(McEliecePrivateKeyParameters paramMcEliecePrivateKeyParameters) {
/*  32 */     this.params = paramMcEliecePrivateKeyParameters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAlgorithm() {
/*  42 */     return "McEliece";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getN() {
/*  50 */     return this.params.getN();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getK() {
/*  58 */     return this.params.getK();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GF2mField getField() {
/*  66 */     return this.params.getField();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PolynomialGF2mSmallM getGoppaPoly() {
/*  74 */     return this.params.getGoppaPoly();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GF2Matrix getSInv() {
/*  82 */     return this.params.getSInv();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Permutation getP1() {
/*  90 */     return this.params.getP1();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Permutation getP2() {
/*  98 */     return this.params.getP2();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GF2Matrix getH() {
/* 106 */     return this.params.getH();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PolynomialGF2mSmallM[] getQInv() {
/* 114 */     return this.params.getQInv();
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
/*     */   public boolean equals(Object paramObject) {
/* 140 */     if (!(paramObject instanceof org.bouncycastle.pqc.jcajce.provider.mceliece.BCMcEliecePrivateKey))
/*     */     {
/* 142 */       return false;
/*     */     }
/* 144 */     org.bouncycastle.pqc.jcajce.provider.mceliece.BCMcEliecePrivateKey bCMcEliecePrivateKey = (org.bouncycastle.pqc.jcajce.provider.mceliece.BCMcEliecePrivateKey)paramObject;
/*     */     
/* 146 */     return (getN() == bCMcEliecePrivateKey.getN() && getK() == bCMcEliecePrivateKey.getK() && 
/* 147 */       getField().equals(bCMcEliecePrivateKey.getField()) && 
/* 148 */       getGoppaPoly().equals(bCMcEliecePrivateKey.getGoppaPoly()) && 
/* 149 */       getSInv().equals(bCMcEliecePrivateKey.getSInv()) && getP1().equals(bCMcEliecePrivateKey.getP1()) && 
/* 150 */       getP2().equals(bCMcEliecePrivateKey.getP2()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 158 */     int i = this.params.getK();
/*     */     
/* 160 */     i = i * 37 + this.params.getN();
/* 161 */     i = i * 37 + this.params.getField().hashCode();
/* 162 */     i = i * 37 + this.params.getGoppaPoly().hashCode();
/* 163 */     i = i * 37 + this.params.getP1().hashCode();
/* 164 */     i = i * 37 + this.params.getP2().hashCode();
/*     */     
/* 166 */     return i * 37 + this.params.getSInv().hashCode();
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
/*     */   public byte[] getEncoded() {
/*     */     PrivateKeyInfo privateKeyInfo;
/* 192 */     McEliecePrivateKey mcEliecePrivateKey = new McEliecePrivateKey(this.params.getN(), this.params.getK(), this.params.getField(), this.params.getGoppaPoly(), this.params.getP1(), this.params.getP2(), this.params.getSInv());
/*     */ 
/*     */     
/*     */     try {
/* 196 */       AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PQCObjectIdentifiers.mcEliece);
/* 197 */       privateKeyInfo = new PrivateKeyInfo(algorithmIdentifier, (ASN1Encodable)mcEliecePrivateKey);
/*     */     }
/* 199 */     catch (IOException iOException) {
/*     */       
/* 201 */       return null;
/*     */     } 
/*     */     
/*     */     try {
/* 205 */       return privateKeyInfo.getEncoded();
/*     */     
/*     */     }
/* 208 */     catch (IOException iOException) {
/*     */       
/* 210 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFormat() {
/* 216 */     return "PKCS#8";
/*     */   }
/*     */ 
/*     */   
/*     */   AsymmetricKeyParameter getKeyParams() {
/* 221 */     return (AsymmetricKeyParameter)this.params;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/jcajce/provider/mceliece/BCMcEliecePrivateKey.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */