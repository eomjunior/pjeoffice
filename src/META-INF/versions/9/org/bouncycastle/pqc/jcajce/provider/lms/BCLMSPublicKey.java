/*     */ package META-INF.versions.9.org.bouncycastle.pqc.jcajce.provider.lms;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.security.PublicKey;
/*     */ import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
/*     */ import org.bouncycastle.crypto.CipherParameters;
/*     */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
/*     */ import org.bouncycastle.pqc.crypto.lms.HSSPublicKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.util.PublicKeyFactory;
/*     */ import org.bouncycastle.pqc.crypto.util.SubjectPublicKeyInfoFactory;
/*     */ import org.bouncycastle.pqc.jcajce.interfaces.LMSKey;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BCLMSPublicKey
/*     */   implements PublicKey, LMSKey
/*     */ {
/*     */   private static final long serialVersionUID = -5617456225328969766L;
/*     */   private transient LMSKeyParameters keyParams;
/*     */   
/*     */   public BCLMSPublicKey(LMSKeyParameters paramLMSKeyParameters) {
/*  29 */     this.keyParams = paramLMSKeyParameters;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BCLMSPublicKey(SubjectPublicKeyInfo paramSubjectPublicKeyInfo) throws IOException {
/*  35 */     init(paramSubjectPublicKeyInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void init(SubjectPublicKeyInfo paramSubjectPublicKeyInfo) throws IOException {
/*  41 */     this.keyParams = (LMSKeyParameters)PublicKeyFactory.createKey(paramSubjectPublicKeyInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getAlgorithm() {
/*  49 */     return "LMS";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getEncoded() {
/*     */     try {
/*  56 */       SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfoFactory.createSubjectPublicKeyInfo((AsymmetricKeyParameter)this.keyParams);
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
/*     */   CipherParameters getKeyParams() {
/*  72 */     return (CipherParameters)this.keyParams;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  77 */     if (paramObject == this)
/*     */     {
/*  79 */       return true;
/*     */     }
/*     */     
/*  82 */     if (paramObject instanceof org.bouncycastle.pqc.jcajce.provider.lms.BCLMSPublicKey) {
/*     */       
/*  84 */       org.bouncycastle.pqc.jcajce.provider.lms.BCLMSPublicKey bCLMSPublicKey = (org.bouncycastle.pqc.jcajce.provider.lms.BCLMSPublicKey)paramObject;
/*     */ 
/*     */       
/*     */       try {
/*  88 */         return Arrays.areEqual(this.keyParams.getEncoded(), bCLMSPublicKey.keyParams.getEncoded());
/*     */       }
/*  90 */       catch (IOException iOException) {
/*     */         
/*  92 */         return false;
/*     */       } 
/*     */     } 
/*     */     
/*  96 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*     */     try {
/* 103 */       return Arrays.hashCode(this.keyParams.getEncoded());
/*     */     }
/* 105 */     catch (IOException iOException) {
/*     */ 
/*     */       
/* 108 */       return -1;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLevels() {
/* 114 */     if (this.keyParams instanceof org.bouncycastle.pqc.crypto.lms.LMSPublicKeyParameters)
/*     */     {
/* 116 */       return 1;
/*     */     }
/*     */ 
/*     */     
/* 120 */     return ((HSSPublicKeyParameters)this.keyParams).getL();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
/* 128 */     paramObjectInputStream.defaultReadObject();
/*     */     
/* 130 */     byte[] arrayOfByte = (byte[])paramObjectInputStream.readObject();
/*     */     
/* 132 */     init(SubjectPublicKeyInfo.getInstance(arrayOfByte));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
/* 139 */     paramObjectOutputStream.defaultWriteObject();
/*     */     
/* 141 */     paramObjectOutputStream.writeObject(getEncoded());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/jcajce/provider/lms/BCLMSPublicKey.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */