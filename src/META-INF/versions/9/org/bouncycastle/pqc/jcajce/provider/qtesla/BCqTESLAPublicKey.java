/*     */ package META-INF.versions.9.org.bouncycastle.pqc.jcajce.provider.qtesla;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.security.PublicKey;
/*     */ import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
/*     */ import org.bouncycastle.crypto.CipherParameters;
/*     */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
/*     */ import org.bouncycastle.pqc.crypto.qtesla.QTESLAPublicKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.qtesla.QTESLASecurityCategory;
/*     */ import org.bouncycastle.pqc.crypto.util.PublicKeyFactory;
/*     */ import org.bouncycastle.pqc.crypto.util.SubjectPublicKeyInfoFactory;
/*     */ import org.bouncycastle.pqc.jcajce.interfaces.QTESLAKey;
/*     */ import org.bouncycastle.pqc.jcajce.spec.QTESLAParameterSpec;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BCqTESLAPublicKey
/*     */   implements PublicKey, QTESLAKey
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private transient QTESLAPublicKeyParameters keyParams;
/*     */   
/*     */   public BCqTESLAPublicKey(QTESLAPublicKeyParameters paramQTESLAPublicKeyParameters) {
/*  28 */     this.keyParams = paramQTESLAPublicKeyParameters;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BCqTESLAPublicKey(SubjectPublicKeyInfo paramSubjectPublicKeyInfo) throws IOException {
/*  34 */     init(paramSubjectPublicKeyInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void init(SubjectPublicKeyInfo paramSubjectPublicKeyInfo) throws IOException {
/*  40 */     this.keyParams = (QTESLAPublicKeyParameters)PublicKeyFactory.createKey(paramSubjectPublicKeyInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getAlgorithm() {
/*  48 */     return QTESLASecurityCategory.getName(this.keyParams.getSecurityCategory());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getEncoded() {
/*     */     try {
/*  55 */       SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfoFactory.createSubjectPublicKeyInfo((AsymmetricKeyParameter)this.keyParams);
/*     */       
/*  57 */       return subjectPublicKeyInfo.getEncoded();
/*     */     }
/*  59 */     catch (IOException iOException) {
/*     */       
/*  61 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFormat() {
/*  67 */     return "X.509";
/*     */   }
/*     */ 
/*     */   
/*     */   public QTESLAParameterSpec getParams() {
/*  72 */     return new QTESLAParameterSpec(getAlgorithm());
/*     */   }
/*     */ 
/*     */   
/*     */   CipherParameters getKeyParams() {
/*  77 */     return (CipherParameters)this.keyParams;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  82 */     if (paramObject == this)
/*     */     {
/*  84 */       return true;
/*     */     }
/*     */     
/*  87 */     if (paramObject instanceof org.bouncycastle.pqc.jcajce.provider.qtesla.BCqTESLAPublicKey) {
/*     */       
/*  89 */       org.bouncycastle.pqc.jcajce.provider.qtesla.BCqTESLAPublicKey bCqTESLAPublicKey = (org.bouncycastle.pqc.jcajce.provider.qtesla.BCqTESLAPublicKey)paramObject;
/*     */       
/*  91 */       return (this.keyParams.getSecurityCategory() == bCqTESLAPublicKey.keyParams.getSecurityCategory() && 
/*  92 */         Arrays.areEqual(this.keyParams.getPublicData(), bCqTESLAPublicKey.keyParams.getPublicData()));
/*     */     } 
/*     */     
/*  95 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 100 */     return this.keyParams.getSecurityCategory() + 37 * Arrays.hashCode(this.keyParams.getPublicData());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
/* 107 */     paramObjectInputStream.defaultReadObject();
/*     */     
/* 109 */     byte[] arrayOfByte = (byte[])paramObjectInputStream.readObject();
/*     */     
/* 111 */     init(SubjectPublicKeyInfo.getInstance(arrayOfByte));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
/* 118 */     paramObjectOutputStream.defaultWriteObject();
/*     */     
/* 120 */     paramObjectOutputStream.writeObject(getEncoded());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/jcajce/provider/qtesla/BCqTESLAPublicKey.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */