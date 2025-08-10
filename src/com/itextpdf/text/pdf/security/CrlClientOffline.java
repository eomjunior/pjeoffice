/*    */ package com.itextpdf.text.pdf.security;
/*    */ 
/*    */ import com.itextpdf.text.ExceptionConverter;
/*    */ import java.security.cert.CRL;
/*    */ import java.security.cert.X509CRL;
/*    */ import java.security.cert.X509Certificate;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
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
/*    */ public class CrlClientOffline
/*    */   implements CrlClient
/*    */ {
/* 61 */   private ArrayList<byte[]> crls = (ArrayList)new ArrayList<byte>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CrlClientOffline(byte[] crlEncoded) {
/* 69 */     this.crls.add(crlEncoded);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CrlClientOffline(CRL crl) {
/*    */     try {
/* 79 */       this.crls.add(((X509CRL)crl).getEncoded());
/*    */     }
/* 81 */     catch (Exception ex) {
/* 82 */       throw new ExceptionConverter(ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Collection<byte[]> getEncoded(X509Certificate checkCert, String url) {
/* 91 */     return (Collection<byte[]>)this.crls;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/security/CrlClientOffline.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */