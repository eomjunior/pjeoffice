/*    */ package com.itextpdf.text.pdf;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PdfRendition
/*    */   extends PdfDictionary
/*    */ {
/*    */   PdfRendition(String file, PdfFileSpecification fs, String mimeType) throws IOException {
/* 53 */     put(PdfName.S, new PdfName("MR"));
/* 54 */     put(PdfName.N, new PdfString("Rendition for " + file));
/* 55 */     put(PdfName.C, new PdfMediaClipData(file, fs, mimeType));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfRendition.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */