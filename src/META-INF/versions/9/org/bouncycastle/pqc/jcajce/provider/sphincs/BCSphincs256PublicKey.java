/*     */ package META-INF.versions.9.org.bouncycastle.pqc.jcajce.provider.sphincs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.security.PublicKey;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
/*     */ import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
/*     */ import org.bouncycastle.crypto.CipherParameters;
/*     */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
/*     */ import org.bouncycastle.pqc.asn1.PQCObjectIdentifiers;
/*     */ import org.bouncycastle.pqc.asn1.SPHINCS256KeyParams;
/*     */ import org.bouncycastle.pqc.crypto.sphincs.SPHINCSPublicKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.util.PublicKeyFactory;
/*     */ import org.bouncycastle.pqc.crypto.util.SubjectPublicKeyInfoFactory;
/*     */ import org.bouncycastle.pqc.jcajce.interfaces.SPHINCSKey;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BCSphincs256PublicKey
/*     */   implements PublicKey, SPHINCSKey
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private transient ASN1ObjectIdentifier treeDigest;
/*     */   private transient SPHINCSPublicKeyParameters params;
/*     */   
/*     */   public BCSphincs256PublicKey(ASN1ObjectIdentifier paramASN1ObjectIdentifier, SPHINCSPublicKeyParameters paramSPHINCSPublicKeyParameters) {
/*  32 */     this.treeDigest = paramASN1ObjectIdentifier;
/*  33 */     this.params = paramSPHINCSPublicKeyParameters;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BCSphincs256PublicKey(SubjectPublicKeyInfo paramSubjectPublicKeyInfo) throws IOException {
/*  39 */     init(paramSubjectPublicKeyInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void init(SubjectPublicKeyInfo paramSubjectPublicKeyInfo) throws IOException {
/*  45 */     this.treeDigest = SPHINCS256KeyParams.getInstance(paramSubjectPublicKeyInfo.getAlgorithm().getParameters()).getTreeDigest().getAlgorithm();
/*  46 */     this.params = (SPHINCSPublicKeyParameters)PublicKeyFactory.createKey(paramSubjectPublicKeyInfo);
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
/*  57 */     if (paramObject == this)
/*     */     {
/*  59 */       return true;
/*     */     }
/*     */     
/*  62 */     if (paramObject instanceof org.bouncycastle.pqc.jcajce.provider.sphincs.BCSphincs256PublicKey) {
/*     */       
/*  64 */       org.bouncycastle.pqc.jcajce.provider.sphincs.BCSphincs256PublicKey bCSphincs256PublicKey = (org.bouncycastle.pqc.jcajce.provider.sphincs.BCSphincs256PublicKey)paramObject;
/*     */       
/*  66 */       return (this.treeDigest.equals((ASN1Primitive)bCSphincs256PublicKey.treeDigest) && Arrays.areEqual(this.params.getKeyData(), bCSphincs256PublicKey.params.getKeyData()));
/*     */     } 
/*     */     
/*  69 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  74 */     return this.treeDigest.hashCode() + 37 * Arrays.hashCode(this.params.getKeyData());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getAlgorithm() {
/*  82 */     return "SPHINCS-256";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getEncoded() {
/*     */     try {
/*     */       SubjectPublicKeyInfo subjectPublicKeyInfo;
/*  91 */       if (this.params.getTreeDigest() != null) {
/*     */         
/*  93 */         subjectPublicKeyInfo = SubjectPublicKeyInfoFactory.createSubjectPublicKeyInfo((AsymmetricKeyParameter)this.params);
/*     */       }
/*     */       else {
/*     */         
/*  97 */         AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PQCObjectIdentifiers.sphincs256, (ASN1Encodable)new SPHINCS256KeyParams(new AlgorithmIdentifier(this.treeDigest)));
/*  98 */         subjectPublicKeyInfo = new SubjectPublicKeyInfo(algorithmIdentifier, this.params.getKeyData());
/*     */       } 
/*     */       
/* 101 */       return subjectPublicKeyInfo.getEncoded();
/*     */     }
/* 103 */     catch (IOException iOException) {
/*     */       
/* 105 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFormat() {
/* 111 */     return "X.509";
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getKeyData() {
/* 116 */     return this.params.getKeyData();
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1ObjectIdentifier getTreeDigest() {
/* 121 */     return this.treeDigest;
/*     */   }
/*     */ 
/*     */   
/*     */   CipherParameters getKeyParams() {
/* 126 */     return (CipherParameters)this.params;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
/* 133 */     paramObjectInputStream.defaultReadObject();
/*     */     
/* 135 */     byte[] arrayOfByte = (byte[])paramObjectInputStream.readObject();
/*     */     
/* 137 */     init(SubjectPublicKeyInfo.getInstance(arrayOfByte));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
/* 144 */     paramObjectOutputStream.defaultWriteObject();
/*     */     
/* 146 */     paramObjectOutputStream.writeObject(getEncoded());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/jcajce/provider/sphincs/BCSphincs256PublicKey.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */