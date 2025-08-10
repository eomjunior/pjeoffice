/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.ICertificateListUI;
/*    */ import com.github.signer4j.ICertificates;
/*    */ import com.github.signer4j.IChoice;
/*    */ import com.github.signer4j.IKeyStoreAccess;
/*    */ import com.github.signer4j.gui.CertificateListDialog;
/*    */ import com.github.signer4j.imp.exception.Signer4JException;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
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
/*    */ public class DefaultChooser
/*    */   extends AbstractCertificateChooser
/*    */ {
/*    */   public DefaultChooser(IKeyStoreAccess keyStore, ICertificates certificates) {
/* 43 */     super(keyStore, certificates);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected IChoice doChoose(List<DefaultCertificateEntry> options) throws Signer4JException, SwitchRepositoryException {
/* 49 */     Optional<ICertificateListUI.ICertificateEntry> ce = (Optional<ICertificateListUI.ICertificateEntry>)CertificateListDialog.display(options).get();
/* 50 */     if (!ce.isPresent()) {
/* 51 */       return Choice.CANCEL;
/*    */     }
/* 53 */     return toChoice((DefaultCertificateEntry)ce.get());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/DefaultChooser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */