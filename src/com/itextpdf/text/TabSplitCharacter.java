/*    */ package com.itextpdf.text;
/*    */ 
/*    */ import com.itextpdf.text.pdf.PdfChunk;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TabSplitCharacter
/*    */   implements SplitCharacter
/*    */ {
/* 51 */   public static final SplitCharacter TAB = new TabSplitCharacter();
/*    */   
/*    */   public boolean isSplitCharacter(int start, int current, int end, char[] cc, PdfChunk[] ck) {
/* 54 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/TabSplitCharacter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */