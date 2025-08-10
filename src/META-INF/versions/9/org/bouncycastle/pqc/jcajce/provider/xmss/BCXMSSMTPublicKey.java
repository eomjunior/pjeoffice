/*     */ package META-INF.versions.9.org.bouncycastle.pqc.jcajce.provider.xmss;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.security.PublicKey;
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
/*     */ import org.bouncycastle.crypto.CipherParameters;
/*     */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
/*     */ import org.bouncycastle.pqc.crypto.util.PublicKeyFactory;
/*     */ import org.bouncycastle.pqc.crypto.util.SubjectPublicKeyInfoFactory;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSMTPublicKeyParameters;
/*     */ import org.bouncycastle.pqc.jcajce.interfaces.XMSSMTKey;
/*     */ import org.bouncycastle.pqc.jcajce.provider.xmss.DigestUtil;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ 
/*     */ public class BCXMSSMTPublicKey
/*     */   implements PublicKey, XMSSMTKey
/*     */ {
/*     */   private static final long serialVersionUID = 3230324130542413475L;
/*     */   private transient ASN1ObjectIdentifier treeDigest;
/*     */   private transient XMSSMTPublicKeyParameters keyParams;
/*     */   
/*     */   public BCXMSSMTPublicKey(ASN1ObjectIdentifier paramASN1ObjectIdentifier, XMSSMTPublicKeyParameters paramXMSSMTPublicKeyParameters) {
/*  27 */     this.treeDigest = paramASN1ObjectIdentifier;
/*  28 */     this.keyParams = paramXMSSMTPublicKeyParameters;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BCXMSSMTPublicKey(SubjectPublicKeyInfo paramSubjectPublicKeyInfo) throws IOException {
/*  34 */     init(paramSubjectPublicKeyInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void init(SubjectPublicKeyInfo paramSubjectPublicKeyInfo) throws IOException {
/*  40 */     this.keyParams = (XMSSMTPublicKeyParameters)PublicKeyFactory.createKey(paramSubjectPublicKeyInfo);
/*  41 */     this.treeDigest = DigestUtil.getDigestOID(this.keyParams.getTreeDigest());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  46 */     if (paramObject == this)
/*     */     {
/*  48 */       return true;
/*     */     }
/*     */     
/*  51 */     if (paramObject instanceof org.bouncycastle.pqc.jcajce.provider.xmss.BCXMSSMTPublicKey) {
/*     */       
/*  53 */       org.bouncycastle.pqc.jcajce.provider.xmss.BCXMSSMTPublicKey bCXMSSMTPublicKey = (org.bouncycastle.pqc.jcajce.provider.xmss.BCXMSSMTPublicKey)paramObject;
/*     */       
/*  55 */       return (this.treeDigest.equals((ASN1Primitive)bCXMSSMTPublicKey.treeDigest) && Arrays.areEqual(this.keyParams.toByteArray(), bCXMSSMTPublicKey.keyParams.toByteArray()));
/*     */     } 
/*     */     
/*  58 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  63 */     return this.treeDigest.hashCode() + 37 * Arrays.hashCode(this.keyParams.toByteArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getAlgorithm() {
/*  71 */     return "XMSSMT";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getEncoded() {
/*     */     try {
/*  78 */       SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfoFactory.createSubjectPublicKeyInfo((AsymmetricKeyParameter)this.keyParams);
/*     */       
/*  80 */       return subjectPublicKeyInfo.getEncoded();
/*     */     }
/*  82 */     catch (IOException iOException) {
/*     */       
/*  84 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFormat() {
/*  90 */     return "X.509";
/*     */   }
/*     */ 
/*     */   
/*     */   CipherParameters getKeyParams() {
/*  95 */     return (CipherParameters)this.keyParams;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/* 100 */     return this.keyParams.getParameters().getHeight();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLayers() {
/* 105 */     return this.keyParams.getParameters().getLayers();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTreeDigest() {
/* 110 */     return DigestUtil.getXMSSDigestName(this.treeDigest);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
/* 117 */     paramObjectInputStream.defaultReadObject();
/*     */     
/* 119 */     byte[] arrayOfByte = (byte[])paramObjectInputStream.readObject();
/*     */     
/* 121 */     init(SubjectPublicKeyInfo.getInstance(arrayOfByte));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
/* 128 */     paramObjectOutputStream.defaultWriteObject();
/*     */     
/* 130 */     paramObjectOutputStream.writeObject(getEncoded());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/jcajce/provider/xmss/BCXMSSMTPublicKey.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */