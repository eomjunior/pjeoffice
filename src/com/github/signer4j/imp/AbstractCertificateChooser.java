/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.ICertificate;
/*    */ import com.github.signer4j.ICertificateChooser;
/*    */ import com.github.signer4j.ICertificates;
/*    */ import com.github.signer4j.IChoice;
/*    */ import com.github.signer4j.IDevice;
/*    */ import com.github.signer4j.IKeyStoreAccess;
/*    */ import com.github.signer4j.imp.exception.Signer4JException;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Collectors;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractCertificateChooser
/*    */   implements ICertificateChooser
/*    */ {
/*    */   private final IKeyStoreAccess keyStore;
/*    */   private final ICertificates certificates;
/*    */   private List<DefaultCertificateEntry> options;
/*    */   
/*    */   protected AbstractCertificateChooser(IKeyStoreAccess keyStore, ICertificates certificates) {
/* 53 */     this.keyStore = (IKeyStoreAccess)Args.requireNonNull(keyStore, "keystore is null");
/* 54 */     this.certificates = (ICertificates)Args.requireNonNull(certificates, "certificates is null");
/*    */   }
/*    */   
/*    */   private IDevice getDevice() {
/* 58 */     return this.keyStore.getDevice();
/*    */   }
/*    */ 
/*    */   
/*    */   public final IChoice choose() throws Signer4JException, SwitchRepositoryException {
/* 63 */     if (this.options == null) {
/* 64 */       this.options = (List<DefaultCertificateEntry>)this.certificates.stream().filter(getPredicate()).map(c -> new DefaultCertificateEntry(getDevice(), c)).collect(Collectors.toList());
/*    */     }
/* 66 */     return doChoose(this.options);
/*    */   }
/*    */   
/*    */   protected Predicate<ICertificate> getPredicate() {
/* 70 */     return c -> c.getKeyUsage().isDigitalSignature();
/*    */   }
/*    */   
/*    */   protected final IChoice toChoice(DefaultCertificateEntry choice) throws Signer4JException {
/* 74 */     return Choice.from(this.keyStore, choice.getCertificate().getAlias().orElse(""));
/*    */   }
/*    */   
/*    */   protected abstract IChoice doChoose(List<DefaultCertificateEntry> paramList) throws Signer4JException, SwitchRepositoryException;
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/AbstractCertificateChooser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */