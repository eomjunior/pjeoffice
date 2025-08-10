/*    */ package com.github.utils4j.gui.imp;
/*    */ 
/*    */ import javax.swing.text.AttributeSet;
/*    */ import javax.swing.text.BadLocationException;
/*    */ import javax.swing.text.PlainDocument;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FixedLengthDocument
/*    */   extends PlainDocument
/*    */ {
/*    */   private int maxLength;
/*    */   
/*    */   public FixedLengthDocument(int maxLength) {
/* 38 */     this.maxLength = maxLength;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
/* 44 */     if (str != null && str.length() + getLength() <= this.maxLength)
/* 45 */       super.insertString(offset, str, a); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/gui/imp/FixedLengthDocument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */