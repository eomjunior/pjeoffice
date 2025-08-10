/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.ICertificateChooser;
/*    */ import com.github.signer4j.ICertificateChooserFactory;
/*    */ import com.github.signer4j.ICertificates;
/*    */ import com.github.signer4j.IChoice;
/*    */ import com.github.signer4j.IKeyStoreAccess;
/*    */ import com.github.signer4j.imp.exception.Signer4JException;
/*    */ import com.github.utils4j.imp.Args;
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
/*    */ public final class SingleCertificateChooserFactory
/*    */   implements ICertificateChooserFactory
/*    */ {
/*    */   private final String chosenAlias;
/*    */   
/*    */   public static ICertificateChooserFactory get(String chosenAlias) {
/* 38 */     return new SingleCertificateChooserFactory(chosenAlias);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private SingleCertificateChooserFactory(String chosenAlias) {
/* 44 */     this.chosenAlias = (String)Args.requireNonNull(chosenAlias, "chosenAlias is null");
/*    */   }
/*    */ 
/*    */   
/*    */   public ICertificateChooser apply(IKeyStoreAccess ks, ICertificates certs) {
/* 49 */     return () -> Choice.from(ks, this.chosenAlias);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/SingleCertificateChooserFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */