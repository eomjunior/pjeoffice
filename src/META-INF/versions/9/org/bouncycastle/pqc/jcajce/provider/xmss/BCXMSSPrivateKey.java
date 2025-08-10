/*     */ package META-INF.versions.9.org.bouncycastle.pqc.jcajce.provider.xmss;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.security.PrivateKey;
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1Set;
/*     */ import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
/*     */ import org.bouncycastle.crypto.CipherParameters;
/*     */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
/*     */ import org.bouncycastle.pqc.asn1.XMSSKeyParams;
/*     */ import org.bouncycastle.pqc.crypto.util.PrivateKeyFactory;
/*     */ import org.bouncycastle.pqc.crypto.util.PrivateKeyInfoFactory;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSPrivateKeyParameters;
/*     */ import org.bouncycastle.pqc.jcajce.interfaces.XMSSPrivateKey;
/*     */ import org.bouncycastle.pqc.jcajce.provider.xmss.DigestUtil;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BCXMSSPrivateKey
/*     */   implements PrivateKey, XMSSPrivateKey
/*     */ {
/*     */   private static final long serialVersionUID = 8568701712864512338L;
/*     */   private transient XMSSPrivateKeyParameters keyParams;
/*     */   private transient ASN1ObjectIdentifier treeDigest;
/*     */   private transient ASN1Set attributes;
/*     */   
/*     */   public BCXMSSPrivateKey(ASN1ObjectIdentifier paramASN1ObjectIdentifier, XMSSPrivateKeyParameters paramXMSSPrivateKeyParameters) {
/*  32 */     this.treeDigest = paramASN1ObjectIdentifier;
/*  33 */     this.keyParams = paramXMSSPrivateKeyParameters;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BCXMSSPrivateKey(PrivateKeyInfo paramPrivateKeyInfo) throws IOException {
/*  39 */     init(paramPrivateKeyInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void init(PrivateKeyInfo paramPrivateKeyInfo) throws IOException {
/*  45 */     this.attributes = paramPrivateKeyInfo.getAttributes();
/*  46 */     XMSSKeyParams xMSSKeyParams = XMSSKeyParams.getInstance(paramPrivateKeyInfo.getPrivateKeyAlgorithm().getParameters());
/*  47 */     this.treeDigest = xMSSKeyParams.getTreeDigest().getAlgorithm();
/*  48 */     this.keyParams = (XMSSPrivateKeyParameters)PrivateKeyFactory.createKey(paramPrivateKeyInfo);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getIndex() {
/*  53 */     if (getUsagesRemaining() == 0L)
/*     */     {
/*  55 */       throw new IllegalStateException("key exhausted");
/*     */     }
/*  57 */     return this.keyParams.getIndex();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getUsagesRemaining() {
/*  62 */     return this.keyParams.getUsagesRemaining();
/*     */   }
/*     */ 
/*     */   
/*     */   public XMSSPrivateKey extractKeyShard(int paramInt) {
/*  67 */     return new org.bouncycastle.pqc.jcajce.provider.xmss.BCXMSSPrivateKey(this.treeDigest, this.keyParams.extractKeyShard(paramInt));
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAlgorithm() {
/*  72 */     return "XMSS";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFormat() {
/*  77 */     return "PKCS#8";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getEncoded() {
/*     */     try {
/*  84 */       PrivateKeyInfo privateKeyInfo = PrivateKeyInfoFactory.createPrivateKeyInfo((AsymmetricKeyParameter)this.keyParams, this.attributes);
/*     */       
/*  86 */       return privateKeyInfo.getEncoded();
/*     */     }
/*  88 */     catch (IOException iOException) {
/*     */       
/*  90 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  96 */     if (paramObject == this)
/*     */     {
/*  98 */       return true;
/*     */     }
/*     */     
/* 101 */     if (paramObject instanceof org.bouncycastle.pqc.jcajce.provider.xmss.BCXMSSPrivateKey) {
/*     */       
/* 103 */       org.bouncycastle.pqc.jcajce.provider.xmss.BCXMSSPrivateKey bCXMSSPrivateKey = (org.bouncycastle.pqc.jcajce.provider.xmss.BCXMSSPrivateKey)paramObject;
/*     */       
/* 105 */       return (this.treeDigest.equals((ASN1Primitive)bCXMSSPrivateKey.treeDigest) && Arrays.areEqual(this.keyParams.toByteArray(), bCXMSSPrivateKey.keyParams.toByteArray()));
/*     */     } 
/*     */     
/* 108 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 113 */     return this.treeDigest.hashCode() + 37 * Arrays.hashCode(this.keyParams.toByteArray());
/*     */   }
/*     */ 
/*     */   
/*     */   CipherParameters getKeyParams() {
/* 118 */     return (CipherParameters)this.keyParams;
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1ObjectIdentifier getTreeDigestOID() {
/* 123 */     return this.treeDigest;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/* 128 */     return this.keyParams.getParameters().getHeight();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTreeDigest() {
/* 133 */     return DigestUtil.getXMSSDigestName(this.treeDigest);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
/* 140 */     paramObjectInputStream.defaultReadObject();
/*     */     
/* 142 */     byte[] arrayOfByte = (byte[])paramObjectInputStream.readObject();
/*     */     
/* 144 */     init(PrivateKeyInfo.getInstance(arrayOfByte));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
/* 151 */     paramObjectOutputStream.defaultWriteObject();
/*     */     
/* 153 */     paramObjectOutputStream.writeObject(getEncoded());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/jcajce/provider/xmss/BCXMSSPrivateKey.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */