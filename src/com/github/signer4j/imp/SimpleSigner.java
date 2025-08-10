/*     */ package com.github.signer4j.imp;
/*     */ 
/*     */ import com.github.signer4j.IByteProcessor;
/*     */ import com.github.signer4j.IByteProcessorBuilder;
/*     */ import com.github.signer4j.ICertificateChooser;
/*     */ import com.github.signer4j.IChoice;
/*     */ import com.github.signer4j.IPersonalData;
/*     */ import com.github.signer4j.ISignatureAlgorithm;
/*     */ import com.github.signer4j.ISignedData;
/*     */ import com.github.signer4j.ISignerBuilder;
/*     */ import com.github.signer4j.ISimpleSigner;
/*     */ import com.github.signer4j.imp.exception.Signer4JException;
/*     */ import com.github.signer4j.provider.ProviderInstaller;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import java.security.Signature;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.locks.ReentrantLock;
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
/*     */ class SimpleSigner
/*     */   extends ByteProcessor
/*     */   implements ISimpleSigner
/*     */ {
/*     */   private ISignatureAlgorithm algorithm;
/*     */   
/*     */   private SimpleSigner(ICertificateChooser chooser, Runnable logout, Optional<ReentrantLock> lock) {
/*  52 */     super(chooser, logout, lock);
/*     */   }
/*     */ 
/*     */   
/*     */   public final ISignedData process(byte[] content, int offset, int length) throws Signer4JException {
/*  57 */     Args.requireNonEmpty(content, "content is null");
/*  58 */     Args.requireZeroPositive(offset, "offset is negative");
/*  59 */     Args.requirePositive(length, "length is not positive");
/*  60 */     return invoke(() -> {
/*     */           IChoice choice = select();
/*     */           return (ProviderInstaller.MSCAPI.is(choice.getProvider()) ? new Builder.MSCAPISigner(this.algorithm, choice, this.logout) : new Builder.NativeSigner(this.algorithm, choice, this.logout)).sign(content, offset, length);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */     implements ISignerBuilder
/*     */   {
/*     */     private final Runnable logout;
/*     */ 
/*     */ 
/*     */     
/*     */     private final ICertificateChooser chooser;
/*     */ 
/*     */ 
/*     */     
/*  81 */     private ISignatureAlgorithm algorithm = SignatureAlgorithm.SHA256withRSA;
/*     */     
/*  83 */     private Optional<ReentrantLock> lock = Optional.empty();
/*     */     
/*     */     public Builder(ICertificateChooser chooser, Runnable logout) {
/*  86 */       this.chooser = (ICertificateChooser)Args.requireNonNull(chooser, "chooser is null");
/*  87 */       this.logout = (Runnable)Args.requireNonNull(logout, "logout is null");
/*     */     }
/*     */ 
/*     */     
/*     */     public final ISignerBuilder usingAlgorithm(ISignatureAlgorithm algorithm) {
/*  92 */       this.algorithm = (ISignatureAlgorithm)Args.requireNonNull(algorithm, "Unabled to using null algorithm");
/*  93 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public final ISignerBuilder usingLock(ReentrantLock lock) {
/*  98 */       this.lock = Optional.ofNullable(lock);
/*  99 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public final ISimpleSigner build() {
/* 104 */       SimpleSigner signer = new SimpleSigner(this.chooser, this.logout, this.lock);
/* 105 */       signer.algorithm = this.algorithm;
/* 106 */       return signer;
/*     */     }
/*     */     
/*     */     private static interface ISigner {
/*     */       ISignedData sign(byte[] param2ArrayOfbyte, int param2Int1, int param2Int2) throws Exception; }
/*     */     
/*     */     private static class DefaultSigner implements ISigner {
/*     */       protected final IChoice choice;
/*     */       protected final Runnable logout;
/*     */       protected final Signature signature;
/*     */       
/*     */       protected DefaultSigner(Signature signature, IChoice choice, Runnable logout) {
/* 118 */         this.signature = signature;
/* 119 */         this.choice = choice;
/* 120 */         this.logout = logout;
/*     */       }
/*     */ 
/*     */       
/*     */       public final ISignedData sign(byte[] content, int offset, int length) throws Exception {
/* 125 */         this.signature.initSign(this.choice.getPrivateKey());
/* 126 */         this.signature.update(content, offset, length);
/* 127 */         return doSign();
/*     */       }
/*     */       
/*     */       protected ISignedData doSign() throws Exception {
/* 131 */         return SignedData.from(this.signature.sign(), (IPersonalData)this.choice);
/*     */       }
/*     */     }
/*     */     
/*     */     private static class MSCAPISigner
/*     */       extends DefaultSigner {
/* 137 */       private static final ReentrantLock SERIAL_LOCK = new ReentrantLock();
/*     */       
/*     */       MSCAPISigner(ISignatureAlgorithm signature, IChoice choice, Runnable dispose) throws Exception {
/* 140 */         super(signature.toSignature(ProviderInstaller.MSCAPI.defaultName()), choice, dispose);
/*     */       }
/*     */ 
/*     */       
/*     */       protected ISignedData doSign() throws Exception {
/* 145 */         return TokenAbort.HANDLER.<ISignedData>handle(() -> (ISignedData)Signer4JInvoker.SIGNER4J.invoke(()), SERIAL_LOCK, this.logout);
/*     */       }
/*     */     }
/*     */     
/*     */     private static class NativeSigner extends DefaultSigner
/*     */     {
/* 151 */       NativeSigner(ISignatureAlgorithm signature, IChoice choice, Runnable logout) throws Exception { super(signature.toSignature(), choice, logout); } } } private static class DefaultSigner implements Builder.ISigner { protected final IChoice choice; protected final Runnable logout; protected final Signature signature; protected DefaultSigner(Signature signature, IChoice choice, Runnable logout) { this.signature = signature; this.choice = choice; this.logout = logout; } public final ISignedData sign(byte[] content, int offset, int length) throws Exception { this.signature.initSign(this.choice.getPrivateKey()); this.signature.update(content, offset, length); return doSign(); } protected ISignedData doSign() throws Exception { return SignedData.from(this.signature.sign(), (IPersonalData)this.choice); } } private static class MSCAPISigner extends Builder.DefaultSigner { private static final ReentrantLock SERIAL_LOCK = new ReentrantLock(); MSCAPISigner(ISignatureAlgorithm signature, IChoice choice, Runnable dispose) throws Exception { super(signature.toSignature(ProviderInstaller.MSCAPI.defaultName()), choice, dispose); } protected ISignedData doSign() throws Exception { return TokenAbort.HANDLER.<ISignedData>handle(() -> (ISignedData)Signer4JInvoker.SIGNER4J.invoke(()), SERIAL_LOCK, this.logout); } } private static class NativeSigner extends Builder.DefaultSigner { NativeSigner(ISignatureAlgorithm signature, IChoice choice, Runnable logout) throws Exception { super(signature.toSignature(), choice, logout); }
/*     */      }
/*     */ 
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/SimpleSigner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */