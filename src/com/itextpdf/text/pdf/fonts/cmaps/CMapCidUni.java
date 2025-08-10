/*    */ package com.itextpdf.text.pdf.fonts.cmaps;
/*    */ 
/*    */ import com.itextpdf.text.Utilities;
/*    */ import com.itextpdf.text.pdf.IntHashtable;
/*    */ import com.itextpdf.text.pdf.PdfNumber;
/*    */ import com.itextpdf.text.pdf.PdfObject;
/*    */ import com.itextpdf.text.pdf.PdfString;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CMapCidUni
/*    */   extends AbstractCMap
/*    */ {
/* 57 */   private IntHashtable map = new IntHashtable(65537);
/*    */   
/*    */   void addChar(PdfString mark, PdfObject code) {
/*    */     int codepoint;
/* 61 */     if (!(code instanceof PdfNumber)) {
/*    */       return;
/*    */     }
/* 64 */     String s = decodeStringToUnicode(mark);
/* 65 */     if (Utilities.isSurrogatePair(s, 0)) {
/* 66 */       codepoint = Utilities.convertToUtf32(s, 0);
/*    */     } else {
/* 68 */       codepoint = s.charAt(0);
/* 69 */     }  this.map.put(((PdfNumber)code).intValue(), codepoint);
/*    */   }
/*    */   
/*    */   public int lookup(int character) {
/* 73 */     return this.map.get(character);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/fonts/cmaps/CMapCidUni.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */