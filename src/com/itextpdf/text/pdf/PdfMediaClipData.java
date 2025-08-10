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
/*    */ public class PdfMediaClipData
/*    */   extends PdfDictionary
/*    */ {
/*    */   PdfMediaClipData(String file, PdfFileSpecification fs, String mimeType) throws IOException {
/* 51 */     put(PdfName.TYPE, new PdfName("MediaClip"));
/* 52 */     put(PdfName.S, new PdfName("MCD"));
/* 53 */     put(PdfName.N, new PdfString("Media clip for " + file));
/* 54 */     put(new PdfName("CT"), new PdfString(mimeType));
/* 55 */     PdfDictionary dic = new PdfDictionary();
/* 56 */     dic.put(new PdfName("TF"), new PdfString("TEMPACCESS"));
/* 57 */     put(new PdfName("P"), dic);
/* 58 */     put(PdfName.D, fs.getReference());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfMediaClipData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */