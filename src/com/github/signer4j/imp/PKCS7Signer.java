/*     */ package com.github.signer4j.imp;
/*     */ 
/*     */ import com.github.signer4j.IByteProcessor;
/*     */ import com.github.signer4j.IByteProcessorBuilder;
/*     */ import com.github.signer4j.ICertificateChooser;
/*     */ import com.github.signer4j.IChoice;
/*     */ import com.github.signer4j.IFileSignerBuilder;
/*     */ import com.github.signer4j.IPKCS7Signer;
/*     */ import com.github.signer4j.IPKCS7SignerBuilder;
/*     */ import com.github.signer4j.IPersonalData;
/*     */ import com.github.signer4j.ISignatureAlgorithm;
/*     */ import com.github.signer4j.ISignatureType;
/*     */ import com.github.signer4j.ISignedData;
/*     */ import com.github.signer4j.imp.exception.Signer4JException;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Containers;
/*     */ import com.github.utils4j.imp.OpenByteArrayOutputStream;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import java.io.OutputStream;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.Signature;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ import sun.security.pkcs.ContentInfo;
/*     */ import sun.security.pkcs.PKCS7;
/*     */ import sun.security.pkcs.PKCS9Attribute;
/*     */ import sun.security.pkcs.PKCS9Attributes;
/*     */ import sun.security.pkcs.SignerInfo;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.x509.AlgorithmId;
/*     */ import sun.security.x509.X500Name;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class PKCS7Signer
/*     */   extends ByteProcessor
/*     */   implements IPKCS7Signer
/*     */ {
/*     */   private Signature signature;
/*     */   private MessageDigest messageDigest;
/*     */   private ISignatureType signatureType;
/*     */   private String[] emailAddress;
/*     */   private String[] unstructuredName;
/*     */   private String challengePassword;
/*     */   private String[] unstructuredAddress;
/*     */   private byte[] signatureTimestamp;
/*     */   
/*     */   private PKCS7Signer(ICertificateChooser chooser, Runnable dispose, Optional<ReentrantLock> lock) {
/*  85 */     super(chooser, dispose, lock);
/*     */   }
/*     */ 
/*     */   
/*     */   public ISignedData process(byte[] content, int offset, int length) throws Signer4JException {
/*  90 */     return invoke(() -> {
/*     */           this.messageDigest.update(content, offset, length);
/*     */           
/*     */           byte[] hashContent = this.messageDigest.digest();
/*     */           
/*     */           List<PKCS9Attribute> attrList = new ArrayList<>();
/*     */           
/*     */           attrList.add(new PKCS9Attribute(PKCS9Attribute.CONTENT_TYPE_OID, ContentInfo.DATA_OID));
/*     */           
/*     */           attrList.add(new PKCS9Attribute(PKCS9Attribute.SIGNING_TIME_OID, new Date()));
/*     */           
/*     */           attrList.add(new PKCS9Attribute(PKCS9Attribute.MESSAGE_DIGEST_OID, hashContent));
/*     */           
/*     */           if (!Containers.isEmpty((Object[])this.emailAddress)) {
/*     */             attrList.add(new PKCS9Attribute(PKCS9Attribute.EMAIL_ADDRESS_OID, this.emailAddress));
/*     */           }
/*     */           
/*     */           if (!Containers.isEmpty((Object[])this.unstructuredName)) {
/*     */             attrList.add(new PKCS9Attribute(PKCS9Attribute.UNSTRUCTURED_NAME_OID, this.unstructuredName));
/*     */           }
/*     */           
/*     */           if (Strings.hasText(this.challengePassword)) {
/*     */             attrList.add(new PKCS9Attribute(PKCS9Attribute.CHALLENGE_PASSWORD_OID, this.challengePassword));
/*     */           }
/*     */           
/*     */           if (!Containers.isEmpty((Object[])this.unstructuredAddress)) {
/*     */             attrList.add(new PKCS9Attribute(PKCS9Attribute.UNSTRUCTURED_ADDRESS_OID, this.unstructuredAddress));
/*     */           }
/*     */           
/*     */           if (!Containers.isEmpty(this.signatureTimestamp)) {
/*     */             attrList.add(new PKCS9Attribute(PKCS9Attribute.SIGNATURE_TIMESTAMP_TOKEN_OID, this.signatureTimestamp));
/*     */           }
/*     */           
/*     */           PKCS9Attribute[] attributesArray = attrList.<PKCS9Attribute>toArray(new PKCS9Attribute[attrList.size()]);
/*     */           
/*     */           PKCS9Attributes attributes = new PKCS9Attributes(attributesArray);
/*     */           
/*     */           IChoice choice = select();
/*     */           
/*     */           X509Certificate certificate = (X509Certificate)choice.getCertificate();
/*     */           
/*     */           PrivateKey privateKey = choice.getPrivateKey();
/*     */           
/*     */           this.signature.initSign(privateKey);
/*     */           this.signature.update(attributes.getDerEncoding());
/*     */           AlgorithmId hashAlgorithm = AlgorithmId.get(this.messageDigest.getAlgorithm());
/*     */           SignerInfo signerInfo = new SignerInfo(new X500Name(certificate.getIssuerX500Principal().getName()), certificate.getSerialNumber(), hashAlgorithm, attributes, AlgorithmId.get(this.signature.getAlgorithm()), this.signature.sign(), null);
/*     */           DerValue value = null;
/*     */           if (SignatureType.ATTACHED.equals(this.signatureType)) {
/* 139 */             value = new DerValue((byte)4, (offset == 0 && content.length == length) ? content : Arrays.copyOfRange(content, offset, offset + length));
/*     */           }
/*     */           PKCS7 p7 = new PKCS7(new AlgorithmId[] { hashAlgorithm }, new ContentInfo(ContentInfo.DATA_OID, value), new X509Certificate[] { certificate }, new SignerInfo[] { signerInfo });
/*     */           try (OpenByteArrayOutputStream output = new OpenByteArrayOutputStream()) {
/*     */             p7.encodeSignedData((OutputStream)output);
/*     */             return SignedData.from(output.toByteArray(), (IPersonalData)choice);
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */     implements IPKCS7SignerBuilder
/*     */   {
/* 162 */     private byte[] signatureTimestamp = new byte[0];
/*     */     
/* 164 */     private String challengePassword = Strings.empty();
/*     */     
/* 166 */     private String[] emailAddress = Strings.emptyArray();
/*     */     
/* 168 */     private String[] unstructuredName = Strings.emptyArray();
/*     */     
/* 170 */     private String[] unstructuredAddress = Strings.emptyArray();
/*     */     
/* 172 */     private ISignatureType signatureType = SignatureType.ATTACHED;
/*     */     
/* 174 */     private ISignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.SHA256withRSA;
/*     */     
/*     */     private final Runnable dispose;
/*     */     
/*     */     private final ICertificateChooser chooser;
/*     */     
/* 180 */     private Optional<ReentrantLock> lock = Optional.empty();
/*     */     
/*     */     public Builder(ICertificateChooser chooser, Runnable dispose) {
/* 183 */       this.chooser = (ICertificateChooser)Args.requireNonNull(chooser, "chooser is null");
/* 184 */       this.dispose = (Runnable)Args.requireNonNull(dispose, "dispose is null");
/*     */     }
/*     */ 
/*     */     
/*     */     public IPKCS7SignerBuilder usingSignatureAlgorithm(ISignatureAlgorithm signatureAlgorithm) {
/* 189 */       this.signatureAlgorithm = (ISignatureAlgorithm)Args.requireNonNull(signatureAlgorithm, "signatureAlgorithm is null");
/* 190 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public IPKCS7SignerBuilder usingSignatureType(ISignatureType signatureType) {
/* 195 */       this.signatureType = (ISignatureType)Args.requireNonNull(signatureType, "signatureType is null");
/* 196 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public IPKCS7SignerBuilder usingEmailAddress(String... emailAddress) {
/* 201 */       this.emailAddress = emailAddress;
/* 202 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public IPKCS7SignerBuilder usingUnstructuredName(String... unstructuredName) {
/* 207 */       this.unstructuredName = unstructuredName;
/* 208 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public IPKCS7SignerBuilder usingChallengePassword(String challengePassword) {
/* 213 */       this.challengePassword = challengePassword;
/* 214 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public IPKCS7SignerBuilder usingUnstructuredAddress(String... unstructuredAddress) {
/* 219 */       this.unstructuredAddress = unstructuredAddress;
/* 220 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public IPKCS7SignerBuilder usingSignatureTimestamp(byte[] signatureTimestamp) {
/* 225 */       this.signatureTimestamp = signatureTimestamp;
/* 226 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public final IPKCS7SignerBuilder usingLock(ReentrantLock lock) {
/* 231 */       this.lock = Optional.ofNullable(lock);
/* 232 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public final IPKCS7Signer build() {
/* 237 */       PKCS7Signer signer = new PKCS7Signer(this.chooser, this.dispose, this.lock);
/* 238 */       String signatureAlgorithm = this.signatureAlgorithm.getName();
/* 239 */       signer.signature = (Signature)Throwables.runtime(() -> Signature.getInstance(signatureAlgorithm), "Algorítimo " + signatureAlgorithm + " é desconhecido");
/*     */ 
/*     */ 
/*     */       
/* 243 */       String hashAlgorithm = this.signatureAlgorithm.getHashAlgorithm().getStandardName();
/* 244 */       signer.messageDigest = (MessageDigest)Throwables.runtime(() -> MessageDigest.getInstance(hashAlgorithm), "Algorítimo " + hashAlgorithm + " é desconhecido");
/*     */ 
/*     */ 
/*     */       
/* 248 */       signer.signatureType = this.signatureType;
/* 249 */       signer.challengePassword = this.challengePassword;
/* 250 */       signer.emailAddress = this.emailAddress;
/* 251 */       signer.signatureTimestamp = this.signatureTimestamp;
/* 252 */       signer.unstructuredAddress = this.unstructuredAddress;
/* 253 */       signer.unstructuredName = this.unstructuredName;
/* 254 */       return signer;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/PKCS7Signer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */