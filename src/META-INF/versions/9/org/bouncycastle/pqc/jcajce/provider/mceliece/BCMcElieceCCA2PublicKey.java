/*     */ package META-INF.versions.9.org.bouncycastle.pqc.jcajce.provider.mceliece;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.PublicKey;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
/*     */ import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
/*     */ import org.bouncycastle.crypto.CipherParameters;
/*     */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
/*     */ import org.bouncycastle.pqc.asn1.McElieceCCA2PublicKey;
/*     */ import org.bouncycastle.pqc.asn1.PQCObjectIdentifiers;
/*     */ import org.bouncycastle.pqc.crypto.mceliece.McElieceCCA2PublicKeyParameters;
/*     */ import org.bouncycastle.pqc.jcajce.provider.mceliece.Utils;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.GF2Matrix;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BCMcElieceCCA2PublicKey
/*     */   implements CipherParameters, PublicKey
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private McElieceCCA2PublicKeyParameters params;
/*     */   
/*     */   public BCMcElieceCCA2PublicKey(McElieceCCA2PublicKeyParameters paramMcElieceCCA2PublicKeyParameters) {
/*  30 */     this.params = paramMcElieceCCA2PublicKeyParameters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAlgorithm() {
/*  40 */     return "McEliece-CCA2";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getN() {
/*  48 */     return this.params.getN();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getK() {
/*  56 */     return this.params.getK();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getT() {
/*  64 */     return this.params.getT();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GF2Matrix getG() {
/*  72 */     return this.params.getG();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  80 */     String str = "McEliecePublicKey:\n";
/*  81 */     str = str + " length of the code         : " + str + "\n";
/*  82 */     str = str + " error correction capability: " + str + "\n";
/*  83 */     str = str + " generator matrix           : " + str;
/*  84 */     return str;
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
/*  95 */     if (paramObject == null || !(paramObject instanceof org.bouncycastle.pqc.jcajce.provider.mceliece.BCMcElieceCCA2PublicKey))
/*     */     {
/*  97 */       return false;
/*     */     }
/*     */     
/* 100 */     org.bouncycastle.pqc.jcajce.provider.mceliece.BCMcElieceCCA2PublicKey bCMcElieceCCA2PublicKey = (org.bouncycastle.pqc.jcajce.provider.mceliece.BCMcElieceCCA2PublicKey)paramObject;
/*     */     
/* 102 */     return (this.params.getN() == bCMcElieceCCA2PublicKey.getN() && this.params.getT() == bCMcElieceCCA2PublicKey.getT() && this.params.getG().equals(bCMcElieceCCA2PublicKey.getG()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 110 */     return 37 * (this.params.getN() + 37 * this.params.getT()) + this.params.getG().hashCode();
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
/*     */   public byte[] getEncoded() {
/* 128 */     McElieceCCA2PublicKey mcElieceCCA2PublicKey = new McElieceCCA2PublicKey(this.params.getN(), this.params.getT(), this.params.getG(), Utils.getDigAlgId(this.params.getDigest()));
/* 129 */     AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PQCObjectIdentifiers.mcElieceCca2);
/*     */ 
/*     */     
/*     */     try {
/* 133 */       SubjectPublicKeyInfo subjectPublicKeyInfo = new SubjectPublicKeyInfo(algorithmIdentifier, (ASN1Encodable)mcElieceCCA2PublicKey);
/*     */       
/* 135 */       return subjectPublicKeyInfo.getEncoded();
/*     */     }
/* 137 */     catch (IOException iOException) {
/*     */       
/* 139 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/* 146 */     return "X.509";
/*     */   }
/*     */ 
/*     */   
/*     */   AsymmetricKeyParameter getKeyParams() {
/* 151 */     return (AsymmetricKeyParameter)this.params;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/jcajce/provider/mceliece/BCMcElieceCCA2PublicKey.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */