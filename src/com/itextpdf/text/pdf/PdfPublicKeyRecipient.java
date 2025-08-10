/*    */ package com.itextpdf.text.pdf;
/*    */ 
/*    */ import java.security.cert.Certificate;
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
/*    */ public class PdfPublicKeyRecipient
/*    */ {
/* 50 */   private Certificate certificate = null;
/*    */   
/* 52 */   private int permission = 0;
/*    */   
/* 54 */   protected byte[] cms = null;
/*    */ 
/*    */   
/*    */   public PdfPublicKeyRecipient(Certificate certificate, int permission) {
/* 58 */     this.certificate = certificate;
/* 59 */     this.permission = permission;
/*    */   }
/*    */   
/*    */   public Certificate getCertificate() {
/* 63 */     return this.certificate;
/*    */   }
/*    */   
/*    */   public int getPermission() {
/* 67 */     return this.permission;
/*    */   }
/*    */   
/*    */   protected void setCms(byte[] cms) {
/* 71 */     this.cms = cms;
/*    */   }
/*    */   
/*    */   protected byte[] getCms() {
/* 75 */     return this.cms;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfPublicKeyRecipient.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */