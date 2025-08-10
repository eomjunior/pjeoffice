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
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSPublicKeyParameters;
/*     */ import org.bouncycastle.pqc.jcajce.interfaces.XMSSKey;
/*     */ import org.bouncycastle.pqc.jcajce.provider.xmss.DigestUtil;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BCXMSSPublicKey
/*     */   implements PublicKey, XMSSKey
/*     */ {
/*     */   private static final long serialVersionUID = -5617456225328969766L;
/*     */   private transient XMSSPublicKeyParameters keyParams;
/*     */   private transient ASN1ObjectIdentifier treeDigest;
/*     */   
/*     */   public BCXMSSPublicKey(ASN1ObjectIdentifier paramASN1ObjectIdentifier, XMSSPublicKeyParameters paramXMSSPublicKeyParameters) {
/*  29 */     this.treeDigest = paramASN1ObjectIdentifier;
/*  30 */     this.keyParams = paramXMSSPublicKeyParameters;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BCXMSSPublicKey(SubjectPublicKeyInfo paramSubjectPublicKeyInfo) throws IOException {
/*  36 */     init(paramSubjectPublicKeyInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void init(SubjectPublicKeyInfo paramSubjectPublicKeyInfo) throws IOException {
/*  42 */     this.keyParams = (XMSSPublicKeyParameters)PublicKeyFactory.createKey(paramSubjectPublicKeyInfo);
/*  43 */     this.treeDigest = DigestUtil.getDigestOID(this.keyParams.getTreeDigest());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getAlgorithm() {
/*  51 */     return "XMSS";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getEncoded() {
/*     */     try {
/*  58 */       SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfoFactory.createSubjectPublicKeyInfo((AsymmetricKeyParameter)this.keyParams);
/*  59 */       return subjectPublicKeyInfo.getEncoded();
/*     */     }
/*  61 */     catch (IOException iOException) {
/*     */       
/*  63 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFormat() {
/*  69 */     return "X.509";
/*     */   }
/*     */ 
/*     */   
/*     */   CipherParameters getKeyParams() {
/*  74 */     return (CipherParameters)this.keyParams;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  79 */     if (paramObject == this)
/*     */     {
/*  81 */       return true;
/*     */     }
/*     */     
/*  84 */     if (paramObject instanceof org.bouncycastle.pqc.jcajce.provider.xmss.BCXMSSPublicKey) {
/*     */       
/*  86 */       org.bouncycastle.pqc.jcajce.provider.xmss.BCXMSSPublicKey bCXMSSPublicKey = (org.bouncycastle.pqc.jcajce.provider.xmss.BCXMSSPublicKey)paramObject;
/*     */ 
/*     */       
/*     */       try {
/*  90 */         return (this.treeDigest.equals((ASN1Primitive)bCXMSSPublicKey.treeDigest) && Arrays.areEqual(this.keyParams.getEncoded(), bCXMSSPublicKey.keyParams.getEncoded()));
/*     */       }
/*  92 */       catch (IOException iOException) {
/*     */         
/*  94 */         return false;
/*     */       } 
/*     */     } 
/*     */     
/*  98 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*     */     try {
/* 105 */       return this.treeDigest.hashCode() + 37 * Arrays.hashCode(this.keyParams.getEncoded());
/*     */     }
/* 107 */     catch (IOException iOException) {
/*     */ 
/*     */       
/* 110 */       return this.treeDigest.hashCode();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/* 116 */     return this.keyParams.getParameters().getHeight();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTreeDigest() {
/* 121 */     return DigestUtil.getXMSSDigestName(this.treeDigest);
/*     */   }
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


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/jcajce/provider/xmss/BCXMSSPublicKey.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */