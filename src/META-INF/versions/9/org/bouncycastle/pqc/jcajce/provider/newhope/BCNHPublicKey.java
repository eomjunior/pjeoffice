/*     */ package META-INF.versions.9.org.bouncycastle.pqc.jcajce.provider.newhope;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
/*     */ import org.bouncycastle.crypto.CipherParameters;
/*     */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
/*     */ import org.bouncycastle.pqc.crypto.newhope.NHPublicKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.util.PublicKeyFactory;
/*     */ import org.bouncycastle.pqc.crypto.util.SubjectPublicKeyInfoFactory;
/*     */ import org.bouncycastle.pqc.jcajce.interfaces.NHPublicKey;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BCNHPublicKey
/*     */   implements NHPublicKey
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private transient NHPublicKeyParameters params;
/*     */   
/*     */   public BCNHPublicKey(NHPublicKeyParameters paramNHPublicKeyParameters) {
/*  25 */     this.params = paramNHPublicKeyParameters;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BCNHPublicKey(SubjectPublicKeyInfo paramSubjectPublicKeyInfo) throws IOException {
/*  31 */     init(paramSubjectPublicKeyInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void init(SubjectPublicKeyInfo paramSubjectPublicKeyInfo) throws IOException {
/*  37 */     this.params = (NHPublicKeyParameters)PublicKeyFactory.createKey(paramSubjectPublicKeyInfo);
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
/*  48 */     if (paramObject == null || !(paramObject instanceof org.bouncycastle.pqc.jcajce.provider.newhope.BCNHPublicKey))
/*     */     {
/*  50 */       return false;
/*     */     }
/*  52 */     org.bouncycastle.pqc.jcajce.provider.newhope.BCNHPublicKey bCNHPublicKey = (org.bouncycastle.pqc.jcajce.provider.newhope.BCNHPublicKey)paramObject;
/*     */     
/*  54 */     return Arrays.areEqual(this.params.getPubData(), bCNHPublicKey.params.getPubData());
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  59 */     return Arrays.hashCode(this.params.getPubData());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getAlgorithm() {
/*  67 */     return "NH";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getEncoded() {
/*     */     try {
/*  74 */       SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfoFactory.createSubjectPublicKeyInfo((AsymmetricKeyParameter)this.params);
/*     */       
/*  76 */       return subjectPublicKeyInfo.getEncoded();
/*     */     }
/*  78 */     catch (IOException iOException) {
/*     */       
/*  80 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFormat() {
/*  86 */     return "X.509";
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getPublicData() {
/*  91 */     return this.params.getPubData();
/*     */   }
/*     */ 
/*     */   
/*     */   CipherParameters getKeyParams() {
/*  96 */     return (CipherParameters)this.params;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
/* 103 */     paramObjectInputStream.defaultReadObject();
/*     */     
/* 105 */     byte[] arrayOfByte = (byte[])paramObjectInputStream.readObject();
/*     */     
/* 107 */     init(SubjectPublicKeyInfo.getInstance(arrayOfByte));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
/* 114 */     paramObjectOutputStream.defaultWriteObject();
/*     */     
/* 116 */     paramObjectOutputStream.writeObject(getEncoded());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/jcajce/provider/newhope/BCNHPublicKey.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */