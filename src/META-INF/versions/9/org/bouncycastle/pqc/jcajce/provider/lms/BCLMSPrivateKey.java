/*     */ package META-INF.versions.9.org.bouncycastle.pqc.jcajce.provider.lms;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.security.PrivateKey;
/*     */ import org.bouncycastle.asn1.ASN1Set;
/*     */ import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
/*     */ import org.bouncycastle.crypto.CipherParameters;
/*     */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
/*     */ import org.bouncycastle.pqc.crypto.lms.HSSPrivateKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSPrivateKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.util.PrivateKeyFactory;
/*     */ import org.bouncycastle.pqc.crypto.util.PrivateKeyInfoFactory;
/*     */ import org.bouncycastle.pqc.jcajce.interfaces.LMSPrivateKey;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BCLMSPrivateKey
/*     */   implements PrivateKey, LMSPrivateKey
/*     */ {
/*     */   private static final long serialVersionUID = 8568701712864512338L;
/*     */   private transient LMSKeyParameters keyParams;
/*     */   private transient ASN1Set attributes;
/*     */   
/*     */   public BCLMSPrivateKey(LMSKeyParameters paramLMSKeyParameters) {
/*  30 */     this.keyParams = paramLMSKeyParameters;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BCLMSPrivateKey(PrivateKeyInfo paramPrivateKeyInfo) throws IOException {
/*  36 */     init(paramPrivateKeyInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void init(PrivateKeyInfo paramPrivateKeyInfo) throws IOException {
/*  42 */     this.attributes = paramPrivateKeyInfo.getAttributes();
/*  43 */     this.keyParams = (LMSKeyParameters)PrivateKeyFactory.createKey(paramPrivateKeyInfo);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getIndex() {
/*  48 */     if (getUsagesRemaining() == 0L)
/*     */     {
/*  50 */       throw new IllegalStateException("key exhausted");
/*     */     }
/*     */     
/*  53 */     if (this.keyParams instanceof LMSPrivateKeyParameters)
/*     */     {
/*  55 */       return ((LMSPrivateKeyParameters)this.keyParams).getIndex();
/*     */     }
/*  57 */     return ((HSSPrivateKeyParameters)this.keyParams).getIndex();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getUsagesRemaining() {
/*  62 */     if (this.keyParams instanceof LMSPrivateKeyParameters)
/*     */     {
/*  64 */       return ((LMSPrivateKeyParameters)this.keyParams).getUsagesRemaining();
/*     */     }
/*  66 */     return ((HSSPrivateKeyParameters)this.keyParams).getUsagesRemaining();
/*     */   }
/*     */ 
/*     */   
/*     */   public LMSPrivateKey extractKeyShard(int paramInt) {
/*  71 */     if (this.keyParams instanceof LMSPrivateKeyParameters)
/*     */     {
/*  73 */       return new org.bouncycastle.pqc.jcajce.provider.lms.BCLMSPrivateKey((LMSKeyParameters)((LMSPrivateKeyParameters)this.keyParams).extractKeyShard(paramInt));
/*     */     }
/*  75 */     return new org.bouncycastle.pqc.jcajce.provider.lms.BCLMSPrivateKey((LMSKeyParameters)((HSSPrivateKeyParameters)this.keyParams).extractKeyShard(paramInt));
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAlgorithm() {
/*  80 */     return "LMS";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFormat() {
/*  85 */     return "PKCS#8";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getEncoded() {
/*     */     try {
/*  92 */       PrivateKeyInfo privateKeyInfo = PrivateKeyInfoFactory.createPrivateKeyInfo((AsymmetricKeyParameter)this.keyParams, this.attributes);
/*     */       
/*  94 */       return privateKeyInfo.getEncoded();
/*     */     }
/*  96 */     catch (IOException iOException) {
/*     */       
/*  98 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 104 */     if (paramObject == this)
/*     */     {
/* 106 */       return true;
/*     */     }
/*     */     
/* 109 */     if (paramObject instanceof org.bouncycastle.pqc.jcajce.provider.lms.BCLMSPrivateKey) {
/*     */       
/* 111 */       org.bouncycastle.pqc.jcajce.provider.lms.BCLMSPrivateKey bCLMSPrivateKey = (org.bouncycastle.pqc.jcajce.provider.lms.BCLMSPrivateKey)paramObject;
/*     */ 
/*     */       
/*     */       try {
/* 115 */         return Arrays.areEqual(this.keyParams.getEncoded(), bCLMSPrivateKey.keyParams.getEncoded());
/*     */       }
/* 117 */       catch (IOException iOException) {
/*     */         
/* 119 */         throw new IllegalStateException("unable to perform equals");
/*     */       } 
/*     */     } 
/*     */     
/* 123 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*     */     try {
/* 130 */       return Arrays.hashCode(this.keyParams.getEncoded());
/*     */     }
/* 132 */     catch (IOException iOException) {
/*     */       
/* 134 */       throw new IllegalStateException("unable to calculate hashCode");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   CipherParameters getKeyParams() {
/* 140 */     return (CipherParameters)this.keyParams;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLevels() {
/* 145 */     if (this.keyParams instanceof LMSPrivateKeyParameters)
/*     */     {
/* 147 */       return 1;
/*     */     }
/*     */ 
/*     */     
/* 151 */     return ((HSSPrivateKeyParameters)this.keyParams).getL();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
/* 159 */     paramObjectInputStream.defaultReadObject();
/*     */     
/* 161 */     byte[] arrayOfByte = (byte[])paramObjectInputStream.readObject();
/*     */     
/* 163 */     init(PrivateKeyInfo.getInstance(arrayOfByte));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
/* 170 */     paramObjectOutputStream.defaultWriteObject();
/*     */     
/* 172 */     paramObjectOutputStream.writeObject(getEncoded());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/jcajce/provider/lms/BCLMSPrivateKey.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */