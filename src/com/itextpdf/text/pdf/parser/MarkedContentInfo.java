/*    */ package com.itextpdf.text.pdf.parser;
/*    */ 
/*    */ import com.itextpdf.text.pdf.PdfDictionary;
/*    */ import com.itextpdf.text.pdf.PdfName;
/*    */ import com.itextpdf.text.pdf.PdfNumber;
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
/*    */ public class MarkedContentInfo
/*    */ {
/*    */   private final PdfName tag;
/*    */   private final PdfDictionary dictionary;
/*    */   
/*    */   public MarkedContentInfo(PdfName tag, PdfDictionary dictionary) {
/* 59 */     this.tag = tag;
/* 60 */     this.dictionary = (dictionary != null) ? dictionary : new PdfDictionary();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PdfName getTag() {
/* 68 */     return this.tag;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean hasMcid() {
/* 76 */     return this.dictionary.contains(PdfName.MCID);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getMcid() {
/* 86 */     PdfNumber id = this.dictionary.getAsNumber(PdfName.MCID);
/* 87 */     if (id == null) {
/* 88 */       throw new IllegalStateException("MarkedContentInfo does not contain MCID");
/*    */     }
/* 90 */     return id.intValue();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/MarkedContentInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */