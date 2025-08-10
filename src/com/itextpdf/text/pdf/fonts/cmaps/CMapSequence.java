/*    */ package com.itextpdf.text.pdf.fonts.cmaps;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CMapSequence
/*    */ {
/*    */   public byte[] seq;
/*    */   public int off;
/*    */   public int len;
/*    */   
/*    */   public CMapSequence() {}
/*    */   
/*    */   public CMapSequence(byte[] seq, int off, int len) {
/* 55 */     this.seq = seq;
/* 56 */     this.off = off;
/* 57 */     this.len = len;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/fonts/cmaps/CMapSequence.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */