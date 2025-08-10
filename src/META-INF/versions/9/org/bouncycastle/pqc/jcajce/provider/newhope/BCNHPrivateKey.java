/*     */ package META-INF.versions.9.org.bouncycastle.pqc.jcajce.provider.newhope;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import org.bouncycastle.asn1.ASN1Set;
/*     */ import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
/*     */ import org.bouncycastle.crypto.CipherParameters;
/*     */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
/*     */ import org.bouncycastle.pqc.crypto.newhope.NHPrivateKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.util.PrivateKeyFactory;
/*     */ import org.bouncycastle.pqc.crypto.util.PrivateKeyInfoFactory;
/*     */ import org.bouncycastle.pqc.jcajce.interfaces.NHPrivateKey;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BCNHPrivateKey
/*     */   implements NHPrivateKey
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private transient NHPrivateKeyParameters params;
/*     */   private transient ASN1Set attributes;
/*     */   
/*     */   public BCNHPrivateKey(NHPrivateKeyParameters paramNHPrivateKeyParameters) {
/*  27 */     this.params = paramNHPrivateKeyParameters;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BCNHPrivateKey(PrivateKeyInfo paramPrivateKeyInfo) throws IOException {
/*  33 */     init(paramPrivateKeyInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void init(PrivateKeyInfo paramPrivateKeyInfo) throws IOException {
/*  39 */     this.attributes = paramPrivateKeyInfo.getAttributes();
/*  40 */     this.params = (NHPrivateKeyParameters)PrivateKeyFactory.createKey(paramPrivateKeyInfo);
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
/*  51 */     if (!(paramObject instanceof org.bouncycastle.pqc.jcajce.provider.newhope.BCNHPrivateKey))
/*     */     {
/*  53 */       return false;
/*     */     }
/*  55 */     org.bouncycastle.pqc.jcajce.provider.newhope.BCNHPrivateKey bCNHPrivateKey = (org.bouncycastle.pqc.jcajce.provider.newhope.BCNHPrivateKey)paramObject;
/*     */     
/*  57 */     return Arrays.areEqual(this.params.getSecData(), bCNHPrivateKey.params.getSecData());
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  62 */     return Arrays.hashCode(this.params.getSecData());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getAlgorithm() {
/*  70 */     return "NH";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getEncoded() {
/*     */     try {
/*  77 */       PrivateKeyInfo privateKeyInfo = PrivateKeyInfoFactory.createPrivateKeyInfo((AsymmetricKeyParameter)this.params, this.attributes);
/*     */       
/*  79 */       return privateKeyInfo.getEncoded();
/*     */     }
/*  81 */     catch (IOException iOException) {
/*     */       
/*  83 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFormat() {
/*  89 */     return "PKCS#8";
/*     */   }
/*     */ 
/*     */   
/*     */   public short[] getSecretData() {
/*  94 */     return this.params.getSecData();
/*     */   }
/*     */ 
/*     */   
/*     */   CipherParameters getKeyParams() {
/*  99 */     return (CipherParameters)this.params;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
/* 106 */     paramObjectInputStream.defaultReadObject();
/*     */     
/* 108 */     byte[] arrayOfByte = (byte[])paramObjectInputStream.readObject();
/*     */     
/* 110 */     init(PrivateKeyInfo.getInstance(arrayOfByte));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
/* 117 */     paramObjectOutputStream.defaultWriteObject();
/*     */     
/* 119 */     paramObjectOutputStream.writeObject(getEncoded());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/jcajce/provider/newhope/BCNHPrivateKey.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */