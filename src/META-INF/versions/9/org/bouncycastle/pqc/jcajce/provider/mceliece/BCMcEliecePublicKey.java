/*     */ package META-INF.versions.9.org.bouncycastle.pqc.jcajce.provider.mceliece;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.PublicKey;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
/*     */ import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
/*     */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
/*     */ import org.bouncycastle.pqc.asn1.McEliecePublicKey;
/*     */ import org.bouncycastle.pqc.asn1.PQCObjectIdentifiers;
/*     */ import org.bouncycastle.pqc.crypto.mceliece.McEliecePublicKeyParameters;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.GF2Matrix;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BCMcEliecePublicKey
/*     */   implements PublicKey
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private McEliecePublicKeyParameters params;
/*     */   
/*     */   public BCMcEliecePublicKey(McEliecePublicKeyParameters paramMcEliecePublicKeyParameters) {
/*  28 */     this.params = paramMcEliecePublicKeyParameters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAlgorithm() {
/*  38 */     return "McEliece";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getN() {
/*  46 */     return this.params.getN();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getK() {
/*  54 */     return this.params.getK();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getT() {
/*  62 */     return this.params.getT();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GF2Matrix getG() {
/*  70 */     return this.params.getG();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  78 */     String str = "McEliecePublicKey:\n";
/*  79 */     str = str + " length of the code         : " + str + "\n";
/*  80 */     str = str + " error correction capability: " + str + "\n";
/*  81 */     str = str + " generator matrix           : " + str;
/*  82 */     return str;
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
/*  93 */     if (paramObject instanceof org.bouncycastle.pqc.jcajce.provider.mceliece.BCMcEliecePublicKey) {
/*     */       
/*  95 */       org.bouncycastle.pqc.jcajce.provider.mceliece.BCMcEliecePublicKey bCMcEliecePublicKey = (org.bouncycastle.pqc.jcajce.provider.mceliece.BCMcEliecePublicKey)paramObject;
/*     */       
/*  97 */       return (this.params.getN() == bCMcEliecePublicKey.getN() && this.params.getT() == bCMcEliecePublicKey.getT() && this.params.getG().equals(bCMcEliecePublicKey.getG()));
/*     */     } 
/*     */     
/* 100 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 108 */     return 37 * (this.params.getN() + 37 * this.params.getT()) + this.params.getG().hashCode();
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
/*     */   public byte[] getEncoded() {
/* 127 */     McEliecePublicKey mcEliecePublicKey = new McEliecePublicKey(this.params.getN(), this.params.getT(), this.params.getG());
/* 128 */     AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PQCObjectIdentifiers.mcEliece);
/*     */ 
/*     */     
/*     */     try {
/* 132 */       SubjectPublicKeyInfo subjectPublicKeyInfo = new SubjectPublicKeyInfo(algorithmIdentifier, (ASN1Encodable)mcEliecePublicKey);
/*     */       
/* 134 */       return subjectPublicKeyInfo.getEncoded();
/*     */     }
/* 136 */     catch (IOException iOException) {
/*     */       
/* 138 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFormat() {
/* 144 */     return "X.509";
/*     */   }
/*     */ 
/*     */   
/*     */   AsymmetricKeyParameter getKeyParams() {
/* 149 */     return (AsymmetricKeyParameter)this.params;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/jcajce/provider/mceliece/BCMcEliecePublicKey.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */