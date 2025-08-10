/*    */ package com.itextpdf.text.pdf.security;
/*    */ 
/*    */ import com.itextpdf.text.pdf.PdfDictionary;
/*    */ import com.itextpdf.text.pdf.PdfName;
/*    */ import com.itextpdf.text.pdf.PdfObject;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PdfSignatureBuildProperties
/*    */   extends PdfDictionary
/*    */ {
/*    */   public void setSignatureCreator(String name) {
/* 67 */     getPdfSignatureAppProperty().setSignatureCreator(name);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private PdfSignatureAppDictionary getPdfSignatureAppProperty() {
/* 78 */     PdfSignatureAppDictionary appPropDic = (PdfSignatureAppDictionary)getAsDict(PdfName.APP);
/* 79 */     if (appPropDic == null) {
/* 80 */       appPropDic = new PdfSignatureAppDictionary();
/* 81 */       put(PdfName.APP, (PdfObject)appPropDic);
/*    */     } 
/* 83 */     return appPropDic;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/security/PdfSignatureBuildProperties.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */