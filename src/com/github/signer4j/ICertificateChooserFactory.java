/*    */ package com.github.signer4j;
/*    */ 
/*    */ import com.github.signer4j.imp.ConsoleChooser;
/*    */ import com.github.signer4j.imp.DefaultChooser;
/*    */ import com.github.signer4j.imp.SingleCertificateChooserFactory;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Supplier;
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
/*    */ public interface ICertificateChooserFactory
/*    */   extends BiFunction<IKeyStoreAccess, ICertificates, ICertificateChooser>
/*    */ {
/*    */   public static final ICertificateChooserFactory CONSOLE;
/*    */   public static final ICertificateChooserFactory DEFAULT;
/*    */   
/*    */   static {
/* 40 */     CONSOLE = ((k, c) -> new ConsoleChooser(k, c));
/* 41 */     DEFAULT = ((k, c) -> new DefaultChooser(k, c));
/*    */   }
/*    */   static ICertificateChooserFactory fromCertificate(Optional<ICertificate> certificate) {
/* 44 */     return fromCertificate(certificate, DEFAULT);
/*    */   }
/*    */   
/*    */   static ICertificateChooserFactory fromCertificate(Optional<ICertificate> certificate, ICertificateChooserFactory defaultIfNot) {
/* 48 */     Args.requireNonNull(certificate, "certificate is null");
/* 49 */     Args.requireNonNull(defaultIfNot, "factory is null");
/* 50 */     return certificate.isPresent() ? SingleCertificateChooserFactory.get(((ICertificate)certificate.get()).getAlias().<Throwable>orElseThrow(com.github.signer4j.imp.exception.CertificateAliasNotFoundException::new)) : defaultIfNot;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/ICertificateChooserFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */