/*    */ package com.itextpdf.text.pdf.security;
/*    */ 
/*    */ import com.itextpdf.text.pdf.PdfDictionary;
/*    */ import com.itextpdf.text.pdf.PdfName;
/*    */ import com.itextpdf.text.pdf.PdfObject;
/*    */ import java.io.InputStream;
/*    */ import java.security.GeneralSecurityException;
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
/*    */ public class ExternalBlankSignatureContainer
/*    */   implements ExternalSignatureContainer
/*    */ {
/*    */   private PdfDictionary sigDic;
/*    */   
/*    */   public ExternalBlankSignatureContainer(PdfDictionary sigDic) {
/* 60 */     this.sigDic = sigDic;
/*    */   }
/*    */   
/*    */   public ExternalBlankSignatureContainer(PdfName filter, PdfName subFilter) {
/* 64 */     this.sigDic = new PdfDictionary();
/* 65 */     this.sigDic.put(PdfName.FILTER, (PdfObject)filter);
/* 66 */     this.sigDic.put(PdfName.SUBFILTER, (PdfObject)subFilter);
/*    */   }
/*    */   
/*    */   public byte[] sign(InputStream data) throws GeneralSecurityException {
/* 70 */     return new byte[0];
/*    */   }
/*    */   
/*    */   public void modifySigningDictionary(PdfDictionary signDic) {
/* 74 */     signDic.putAll(this.sigDic);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/security/ExternalBlankSignatureContainer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */