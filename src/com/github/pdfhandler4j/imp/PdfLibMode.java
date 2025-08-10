/*    */ package com.github.pdfhandler4j.imp;
/*    */ 
/*    */ import java.io.File;
/*    */ 
/*    */ enum PdfLibMode {
/*  6 */   ITEXT
/*    */   {
/*    */     public ICloseablePdfDocument createDocument(File file) throws Exception {
/*  9 */       return new CloseableITextDocumentImp(file);
/*    */     }
/*    */ 
/*    */     
/*    */     public ICloseablePdfReader createReader(File file) throws Exception {
/* 14 */       return new CloseableITextReaderImp(file);
/*    */     }
/*    */   };
/*    */ 
/*    */ 
/*    */   
/*    */   public static final PdfLibMode DEFAULT;
/*    */ 
/*    */ 
/*    */   
/*    */   abstract ICloseablePdfReader createReader(File paramFile) throws Exception;
/*    */ 
/*    */ 
/*    */   
/*    */   abstract ICloseablePdfDocument createDocument(File paramFile) throws Exception;
/*    */ 
/*    */   
/*    */   static {
/* 32 */     DEFAULT = ITEXT;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/pdfhandler4j/imp/PdfLibMode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */