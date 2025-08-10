/*    */ package br.jus.cnj.pje.office.gui.certlist;
/*    */ 
/*    */ import com.github.signer4j.ICertificate;
/*    */ import com.github.signer4j.ICertificates;
/*    */ import com.github.signer4j.IChoice;
/*    */ import com.github.signer4j.IKeyStoreAccess;
/*    */ import com.github.signer4j.imp.DefaultCertificateEntry;
/*    */ import com.github.signer4j.imp.DefaultChooser;
/*    */ import com.github.signer4j.imp.SwitchRepositoryException;
/*    */ import com.github.signer4j.imp.exception.Signer4JException;
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
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
/*    */ public class PjeCertificateListAcessor
/*    */   extends DefaultChooser
/*    */ {
/*    */   public static final Predicate<ICertificate> SUPPORTED_CERTIFICATE;
/*    */   
/*    */   static {
/* 44 */     SUPPORTED_CERTIFICATE = (c -> (c.getKeyUsage().isDigitalSignature() && (c.hasCertificatePF() || c.hasCertificatePJ())));
/*    */   }
/*    */   public PjeCertificateListAcessor(IKeyStoreAccess keyStore, ICertificates certificates) {
/* 47 */     super(keyStore, certificates);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Predicate<ICertificate> getPredicate() {
/* 52 */     return SUPPORTED_CERTIFICATE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected IChoice doChoose(List<DefaultCertificateEntry> options) throws Signer4JException, SwitchRepositoryException {
/* 57 */     if (options.size() == 1) {
/* 58 */       DefaultCertificateEntry c = options.get(0);
/* 59 */       if (!c.isExpired()) {
/* 60 */         return toChoice(c);
/*    */       }
/*    */     } 
/* 63 */     return super.doChoose(options);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/gui/certlist/PjeCertificateListAcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */