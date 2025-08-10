/*     */ package com.github.signer4j.imp;
/*     */ 
/*     */ import com.github.signer4j.IByteProcessor;
/*     */ import com.github.signer4j.IByteProcessorBuilder;
/*     */ import com.github.signer4j.ICMSConfigSetup;
/*     */ import com.github.signer4j.ICMSSigner;
/*     */ import com.github.signer4j.ICMSSignerBuilder;
/*     */ import com.github.signer4j.ICertificateChooser;
/*     */ import com.github.signer4j.IChoice;
/*     */ import com.github.signer4j.IFileSignerBuilder;
/*     */ import com.github.signer4j.IPersonalData;
/*     */ import com.github.signer4j.ISignatureAlgorithm;
/*     */ import com.github.signer4j.ISignatureType;
/*     */ import com.github.signer4j.ISignedData;
/*     */ import com.github.signer4j.imp.exception.Signer4JException;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Directory;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.bouncycastle.cert.jcajce.JcaCertStore;
/*     */ import org.bouncycastle.cms.CMSProcessableByteRangeArray;
/*     */ import org.bouncycastle.cms.CMSProcessableFile;
/*     */ import org.bouncycastle.cms.CMSSignedData;
/*     */ import org.bouncycastle.cms.CMSSignedDataGenerator;
/*     */ import org.bouncycastle.cms.CMSTypedData;
/*     */ import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoGeneratorBuilder;
/*     */ import org.bouncycastle.util.Store;
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
/*     */ class CMSSigner
/*     */   extends ByteProcessor
/*     */   implements ICMSSigner
/*     */ {
/*     */   private long memoryLimit;
/*     */   private ISignatureAlgorithm algorithm;
/*     */   private ISignatureType signatureType;
/*     */   private ICMSConfigSetup config;
/*     */   private boolean hasNoSignedAttributes;
/*     */   private String provider;
/*     */   
/*     */   private CMSSigner(ICertificateChooser chooser, Runnable dispose, Optional<ReentrantLock> lock) {
/*  77 */     super(chooser, dispose, lock);
/*     */   }
/*     */ 
/*     */   
/*     */   public ICMSSigner usingAttributes(boolean hasSignedAttributes) {
/*  82 */     this.hasNoSignedAttributes = !hasSignedAttributes;
/*  83 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public IByteProcessor config(Object param) {
/*  88 */     this.config.call(this, param);
/*  89 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ISignedData process(byte[] content, int offset, int length) throws Signer4JException {
/*  94 */     Args.requireNonNull(content, "content is null");
/*  95 */     Args.requireZeroPositive(offset, "offset is negative");
/*  96 */     Args.requirePositive(length, "length is not positive");
/*  97 */     return process((CMSTypedData)new CMSProcessableByteRangeArray(content, offset, length), length);
/*     */   }
/*     */ 
/*     */   
/*     */   public ISignedData process(File content) throws Signer4JException {
/* 102 */     Args.requireNonNull(content, "content is null");
/* 103 */     return process((CMSTypedData)new CMSProcessableFile(content), content.length());
/*     */   }
/*     */   
/*     */   private ISignedData process(CMSTypedData content, long length) throws Signer4JException {
/* 107 */     return invoke(() -> {
/*     */           IChoice choice = select();
/*     */           JcaSimpleSignerInfoGeneratorBuilder builder = (new JcaSimpleSignerInfoGeneratorBuilder()).setDirectSignature(this.hasNoSignedAttributes);
/*     */           if (this.provider != null) {
/*     */             builder.setProvider(this.provider);
/*     */           }
/*     */           CMSSignedDataGenerator generator = new CMSSignedDataGenerator();
/*     */           generator.addSignerInfoGenerator(builder.build(this.algorithm.getName(), choice.getPrivateKey(), (X509Certificate)choice.getCertificate()));
/*     */           generator.addCertificates((Store)new JcaCertStore(choice.getCertificateChain()));
/*     */           CMSSignedData data = generator.generate(content, SignatureType.ATTACHED.equals(this.signatureType));
/*     */           if (length <= this.memoryLimit) {
/*     */             return SignedData.from(data.getEncoded("DER"), (IPersonalData)choice);
/*     */           }
/*     */           File tmp = Directory.createTempFile("pje_office_tmp", ".pjeoffice");
/*     */           try {
/*     */             try (OutputStream out = new BufferedOutputStream(new FileOutputStream(tmp), 32768)) {
/*     */               data.toASN1Structure().encodeTo(out, "DER");
/*     */             } finally {
/*     */               data = null;
/*     */               generator = null;
/*     */               System.gc();
/*     */             } 
/*     */             return SignedData.from(Files.readAllBytes(tmp.toPath()), (IPersonalData)choice);
/*     */           } finally {
/*     */             tmp.delete();
/*     */             tmp = null;
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
/*     */   public static class Builder
/*     */     implements ICMSSignerBuilder
/*     */   {
/* 177 */     private String provider = null;
/*     */     
/*     */     private final Runnable dispose;
/*     */     
/*     */     private final ICertificateChooser chooser;
/*     */     
/* 183 */     private long memoryLimit = 52428800L;
/*     */     
/*     */     private boolean hasNoSignedAttributes = false;
/*     */     private ICMSConfigSetup config = (p, o) -> {
/*     */       
/*     */       };
/* 189 */     private Optional<ReentrantLock> lock = Optional.empty();
/*     */     
/* 191 */     private ISignatureType signatureType = SignatureType.ATTACHED;
/*     */     
/* 193 */     private ISignatureAlgorithm algorithm = SignatureAlgorithm.SHA256withRSA;
/*     */     
/*     */     public Builder(ICertificateChooser chooser, Runnable dispose) {
/* 196 */       this.chooser = (ICertificateChooser)Args.requireNonNull(chooser, "chooser is null");
/* 197 */       this.dispose = (Runnable)Args.requireNonNull(dispose, "dispose is null");
/*     */     }
/*     */ 
/*     */     
/*     */     public final ICMSSignerBuilder usingLock(ReentrantLock lock) {
/* 202 */       this.lock = Optional.ofNullable(lock);
/* 203 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public final ICMSSignerBuilder usingMemoryLimit(long memoryLimit) {
/* 208 */       this.memoryLimit = Args.requireZeroPositive(memoryLimit, "memory limit is invalid");
/* 209 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public final ICMSSignerBuilder usingConfig(ICMSConfigSetup config) {
/* 214 */       this.config = (ICMSConfigSetup)Args.requireNonNull(config, "config is null");
/* 215 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public final ICMSSignerBuilder usingSignatureAlgorithm(ISignatureAlgorithm algorithm) {
/* 220 */       this.algorithm = (ISignatureAlgorithm)Args.requireNonNull(algorithm, "Unabled to using null algorithm");
/* 221 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public final ICMSSignerBuilder usingSignatureType(ISignatureType signatureType) {
/* 226 */       this.signatureType = (ISignatureType)Args.requireNonNull(signatureType, "signatureType is null");
/* 227 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public final ICMSSignerBuilder usingProvider(String provider) {
/* 232 */       this.provider = Args.requireText(provider, "provider is empty");
/* 233 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public final ICMSSignerBuilder usingAttributes(boolean hasSignedAttributes) {
/* 238 */       this.hasNoSignedAttributes = !hasSignedAttributes;
/* 239 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public final ICMSSigner build() {
/* 244 */       CMSSigner signer = new CMSSigner(this.chooser, this.dispose, this.lock);
/* 245 */       signer.provider = this.provider;
/* 246 */       signer.config = this.config;
/* 247 */       signer.memoryLimit = this.memoryLimit;
/* 248 */       signer.algorithm = this.algorithm;
/* 249 */       signer.signatureType = this.signatureType;
/* 250 */       signer.hasNoSignedAttributes = this.hasNoSignedAttributes;
/* 251 */       return signer;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/CMSSigner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */