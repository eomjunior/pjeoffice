/*     */ package META-INF.versions.9.org.bouncycastle.pqc.jcajce.provider.sphincs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.security.PrivateKey;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1Set;
/*     */ import org.bouncycastle.asn1.DEROctetString;
/*     */ import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
/*     */ import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
/*     */ import org.bouncycastle.crypto.CipherParameters;
/*     */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
/*     */ import org.bouncycastle.pqc.asn1.PQCObjectIdentifiers;
/*     */ import org.bouncycastle.pqc.asn1.SPHINCS256KeyParams;
/*     */ import org.bouncycastle.pqc.crypto.sphincs.SPHINCSPrivateKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.util.PrivateKeyFactory;
/*     */ import org.bouncycastle.pqc.crypto.util.PrivateKeyInfoFactory;
/*     */ import org.bouncycastle.pqc.jcajce.interfaces.SPHINCSKey;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BCSphincs256PrivateKey
/*     */   implements PrivateKey, SPHINCSKey
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private transient ASN1ObjectIdentifier treeDigest;
/*     */   private transient SPHINCSPrivateKeyParameters params;
/*     */   private transient ASN1Set attributes;
/*     */   
/*     */   public BCSphincs256PrivateKey(ASN1ObjectIdentifier paramASN1ObjectIdentifier, SPHINCSPrivateKeyParameters paramSPHINCSPrivateKeyParameters) {
/*  35 */     this.treeDigest = paramASN1ObjectIdentifier;
/*  36 */     this.params = paramSPHINCSPrivateKeyParameters;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BCSphincs256PrivateKey(PrivateKeyInfo paramPrivateKeyInfo) throws IOException {
/*  42 */     init(paramPrivateKeyInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void init(PrivateKeyInfo paramPrivateKeyInfo) throws IOException {
/*  48 */     this.attributes = paramPrivateKeyInfo.getAttributes();
/*  49 */     this.treeDigest = SPHINCS256KeyParams.getInstance(paramPrivateKeyInfo.getPrivateKeyAlgorithm().getParameters()).getTreeDigest().getAlgorithm();
/*  50 */     this.params = (SPHINCSPrivateKeyParameters)PrivateKeyFactory.createKey(paramPrivateKeyInfo);
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
/*  61 */     if (paramObject == this)
/*     */     {
/*  63 */       return true;
/*     */     }
/*     */     
/*  66 */     if (paramObject instanceof org.bouncycastle.pqc.jcajce.provider.sphincs.BCSphincs256PrivateKey) {
/*     */       
/*  68 */       org.bouncycastle.pqc.jcajce.provider.sphincs.BCSphincs256PrivateKey bCSphincs256PrivateKey = (org.bouncycastle.pqc.jcajce.provider.sphincs.BCSphincs256PrivateKey)paramObject;
/*     */       
/*  70 */       return (this.treeDigest.equals((ASN1Primitive)bCSphincs256PrivateKey.treeDigest) && Arrays.areEqual(this.params.getKeyData(), bCSphincs256PrivateKey.params.getKeyData()));
/*     */     } 
/*     */     
/*  73 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  78 */     return this.treeDigest.hashCode() + 37 * Arrays.hashCode(this.params.getKeyData());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getAlgorithm() {
/*  86 */     return "SPHINCS-256";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getEncoded() {
/*     */     try {
/*     */       PrivateKeyInfo privateKeyInfo;
/*  95 */       if (this.params.getTreeDigest() != null) {
/*     */         
/*  97 */         privateKeyInfo = PrivateKeyInfoFactory.createPrivateKeyInfo((AsymmetricKeyParameter)this.params, this.attributes);
/*     */       }
/*     */       else {
/*     */         
/* 101 */         AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PQCObjectIdentifiers.sphincs256, (ASN1Encodable)new SPHINCS256KeyParams(new AlgorithmIdentifier(this.treeDigest)));
/*     */         
/* 103 */         privateKeyInfo = new PrivateKeyInfo(algorithmIdentifier, (ASN1Encodable)new DEROctetString(this.params.getKeyData()), this.attributes);
/*     */       } 
/*     */       
/* 106 */       return privateKeyInfo.getEncoded();
/*     */     }
/* 108 */     catch (IOException iOException) {
/*     */       
/* 110 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFormat() {
/* 116 */     return "PKCS#8";
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1ObjectIdentifier getTreeDigest() {
/* 121 */     return this.treeDigest;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getKeyData() {
/* 126 */     return this.params.getKeyData();
/*     */   }
/*     */ 
/*     */   
/*     */   CipherParameters getKeyParams() {
/* 131 */     return (CipherParameters)this.params;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
/* 138 */     paramObjectInputStream.defaultReadObject();
/*     */     
/* 140 */     byte[] arrayOfByte = (byte[])paramObjectInputStream.readObject();
/*     */     
/* 142 */     init(PrivateKeyInfo.getInstance(arrayOfByte));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
/* 149 */     paramObjectOutputStream.defaultWriteObject();
/*     */     
/* 151 */     paramObjectOutputStream.writeObject(getEncoded());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/jcajce/provider/sphincs/BCSphincs256PrivateKey.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */