/*     */ package META-INF.versions.9.org.bouncycastle.pqc.jcajce.provider.qtesla;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.security.PrivateKey;
/*     */ import org.bouncycastle.asn1.ASN1Set;
/*     */ import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
/*     */ import org.bouncycastle.crypto.CipherParameters;
/*     */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
/*     */ import org.bouncycastle.pqc.crypto.qtesla.QTESLAPrivateKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.qtesla.QTESLASecurityCategory;
/*     */ import org.bouncycastle.pqc.crypto.util.PrivateKeyFactory;
/*     */ import org.bouncycastle.pqc.crypto.util.PrivateKeyInfoFactory;
/*     */ import org.bouncycastle.pqc.jcajce.interfaces.QTESLAKey;
/*     */ import org.bouncycastle.pqc.jcajce.spec.QTESLAParameterSpec;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BCqTESLAPrivateKey
/*     */   implements PrivateKey, QTESLAKey
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private transient QTESLAPrivateKeyParameters keyParams;
/*     */   private transient ASN1Set attributes;
/*     */   
/*     */   public BCqTESLAPrivateKey(QTESLAPrivateKeyParameters paramQTESLAPrivateKeyParameters) {
/*  30 */     this.keyParams = paramQTESLAPrivateKeyParameters;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BCqTESLAPrivateKey(PrivateKeyInfo paramPrivateKeyInfo) throws IOException {
/*  36 */     init(paramPrivateKeyInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void init(PrivateKeyInfo paramPrivateKeyInfo) throws IOException {
/*  42 */     this.attributes = paramPrivateKeyInfo.getAttributes();
/*  43 */     this.keyParams = (QTESLAPrivateKeyParameters)PrivateKeyFactory.createKey(paramPrivateKeyInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getAlgorithm() {
/*  51 */     return QTESLASecurityCategory.getName(this.keyParams.getSecurityCategory());
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFormat() {
/*  56 */     return "PKCS#8";
/*     */   }
/*     */ 
/*     */   
/*     */   public QTESLAParameterSpec getParams() {
/*  61 */     return new QTESLAParameterSpec(getAlgorithm());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getEncoded() {
/*     */     try {
/*  69 */       PrivateKeyInfo privateKeyInfo = PrivateKeyInfoFactory.createPrivateKeyInfo((AsymmetricKeyParameter)this.keyParams, this.attributes);
/*     */       
/*  71 */       return privateKeyInfo.getEncoded();
/*     */     }
/*  73 */     catch (IOException iOException) {
/*     */       
/*  75 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  81 */     if (paramObject == this)
/*     */     {
/*  83 */       return true;
/*     */     }
/*     */     
/*  86 */     if (paramObject instanceof org.bouncycastle.pqc.jcajce.provider.qtesla.BCqTESLAPrivateKey) {
/*     */       
/*  88 */       org.bouncycastle.pqc.jcajce.provider.qtesla.BCqTESLAPrivateKey bCqTESLAPrivateKey = (org.bouncycastle.pqc.jcajce.provider.qtesla.BCqTESLAPrivateKey)paramObject;
/*     */       
/*  90 */       return (this.keyParams.getSecurityCategory() == bCqTESLAPrivateKey.keyParams.getSecurityCategory() && 
/*  91 */         Arrays.areEqual(this.keyParams.getSecret(), bCqTESLAPrivateKey.keyParams.getSecret()));
/*     */     } 
/*     */     
/*  94 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  99 */     return this.keyParams.getSecurityCategory() + 37 * Arrays.hashCode(this.keyParams.getSecret());
/*     */   }
/*     */ 
/*     */   
/*     */   CipherParameters getKeyParams() {
/* 104 */     return (CipherParameters)this.keyParams;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
/* 111 */     paramObjectInputStream.defaultReadObject();
/*     */     
/* 113 */     byte[] arrayOfByte = (byte[])paramObjectInputStream.readObject();
/*     */     
/* 115 */     init(PrivateKeyInfo.getInstance(arrayOfByte));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
/* 122 */     paramObjectOutputStream.defaultWriteObject();
/*     */     
/* 124 */     paramObjectOutputStream.writeObject(getEncoded());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/jcajce/provider/qtesla/BCqTESLAPrivateKey.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */