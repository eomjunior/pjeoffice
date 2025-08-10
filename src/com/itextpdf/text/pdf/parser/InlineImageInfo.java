/*    */ package com.itextpdf.text.pdf.parser;
/*    */ 
/*    */ import com.itextpdf.text.pdf.PdfDictionary;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InlineImageInfo
/*    */ {
/*    */   private final byte[] samples;
/*    */   private final PdfDictionary imageDictionary;
/*    */   
/*    */   public InlineImageInfo(byte[] samples, PdfDictionary imageDictionary) {
/* 57 */     this.samples = samples;
/* 58 */     this.imageDictionary = imageDictionary;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PdfDictionary getImageDictionary() {
/* 65 */     return this.imageDictionary;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] getSamples() {
/* 72 */     return this.samples;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/InlineImageInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */