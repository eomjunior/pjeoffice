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
/*     */ import org.bouncycastle.pqc.asn1.XMSSMTKeyParams;
/*     */ import org.bouncycastle.pqc.crypto.util.PrivateKeyFactory;
/*     */ import org.bouncycastle.pqc.crypto.util.PrivateKeyInfoFactory;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSMTPrivateKeyParameters;
/*     */ import org.bouncycastle.pqc.jcajce.interfaces.XMSSMTPrivateKey;
/*     */ import org.bouncycastle.pqc.jcajce.provider.xmss.DigestUtil;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BCXMSSMTPrivateKey
/*     */   implements PrivateKey, XMSSMTPrivateKey
/*     */ {
/*     */   private static final long serialVersionUID = 7682140473044521395L;
/*     */   private transient ASN1ObjectIdentifier treeDigest;
/*     */   private transient XMSSMTPrivateKeyParameters keyParams;
/*     */   private transient ASN1Set attributes;
/*     */   
/*     */   public BCXMSSMTPrivateKey(ASN1ObjectIdentifier paramASN1ObjectIdentifier, XMSSMTPrivateKeyParameters paramXMSSMTPrivateKeyParameters) {
/*  32 */     this.treeDigest = paramASN1ObjectIdentifier;
/*  33 */     this.keyParams = paramXMSSMTPrivateKeyParameters;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BCXMSSMTPrivateKey(PrivateKeyInfo paramPrivateKeyInfo) throws IOException {
/*  39 */     init(paramPrivateKeyInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void init(PrivateKeyInfo paramPrivateKeyInfo) throws IOException {
/*  45 */     this.attributes = paramPrivateKeyInfo.getAttributes();
/*  46 */     XMSSMTKeyParams xMSSMTKeyParams = XMSSMTKeyParams.getInstance(paramPrivateKeyInfo.getPrivateKeyAlgorithm().getParameters());
/*  47 */     this.treeDigest = xMSSMTKeyParams.getTreeDigest().getAlgorithm();
/*  48 */     this.keyParams = (XMSSMTPrivateKeyParameters)PrivateKeyFactory.createKey(paramPrivateKeyInfo);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getIndex() {
/*  53 */     if (getUsagesRemaining() == 0L)
/*     */     {
/*  55 */       throw new IllegalStateException("key exhausted");
/*     */     }
/*     */     
/*  58 */     return this.keyParams.getIndex();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getUsagesRemaining() {
/*  63 */     return this.keyParams.getUsagesRemaining();
/*     */   }
/*     */ 
/*     */   
/*     */   public XMSSMTPrivateKey extractKeyShard(int paramInt) {
/*  68 */     return new org.bouncycastle.pqc.jcajce.provider.xmss.BCXMSSMTPrivateKey(this.treeDigest, this.keyParams.extractKeyShard(paramInt));
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAlgorithm() {
/*  73 */     return "XMSSMT";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFormat() {
/*  78 */     return "PKCS#8";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getEncoded() {
/*     */     try {
/*  85 */       PrivateKeyInfo privateKeyInfo = PrivateKeyInfoFactory.createPrivateKeyInfo((AsymmetricKeyParameter)this.keyParams, this.attributes);
/*     */       
/*  87 */       return privateKeyInfo.getEncoded();
/*     */     }
/*  89 */     catch (IOException iOException) {
/*     */       
/*  91 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   CipherParameters getKeyParams() {
/*  97 */     return (CipherParameters)this.keyParams;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 102 */     if (paramObject == this)
/*     */     {
/* 104 */       return true;
/*     */     }
/*     */     
/* 107 */     if (paramObject instanceof org.bouncycastle.pqc.jcajce.provider.xmss.BCXMSSMTPrivateKey) {
/*     */       
/* 109 */       org.bouncycastle.pqc.jcajce.provider.xmss.BCXMSSMTPrivateKey bCXMSSMTPrivateKey = (org.bouncycastle.pqc.jcajce.provider.xmss.BCXMSSMTPrivateKey)paramObject;
/*     */       
/* 111 */       return (this.treeDigest.equals((ASN1Primitive)bCXMSSMTPrivateKey.treeDigest) && Arrays.areEqual(this.keyParams.toByteArray(), bCXMSSMTPrivateKey.keyParams.toByteArray()));
/*     */     } 
/*     */     
/* 114 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 119 */     return this.treeDigest.hashCode() + 37 * Arrays.hashCode(this.keyParams.toByteArray());
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1ObjectIdentifier getTreeDigestOID() {
/* 124 */     return this.treeDigest;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/* 129 */     return this.keyParams.getParameters().getHeight();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLayers() {
/* 134 */     return this.keyParams.getParameters().getLayers();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTreeDigest() {
/* 139 */     return DigestUtil.getXMSSDigestName(this.treeDigest);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
/* 146 */     paramObjectInputStream.defaultReadObject();
/*     */     
/* 148 */     byte[] arrayOfByte = (byte[])paramObjectInputStream.readObject();
/*     */     
/* 150 */     init(PrivateKeyInfo.getInstance(arrayOfByte));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
/* 157 */     paramObjectOutputStream.defaultWriteObject();
/*     */     
/* 159 */     paramObjectOutputStream.writeObject(getEncoded());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/jcajce/provider/xmss/BCXMSSMTPrivateKey.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */