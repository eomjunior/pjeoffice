/*    */ package org.apache.tools.zip;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UnicodeCommentExtraField
/*    */   extends AbstractUnicodeExtraField
/*    */ {
/* 33 */   public static final ZipShort UCOM_ID = new ZipShort(25461);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public UnicodeCommentExtraField() {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public UnicodeCommentExtraField(String text, byte[] bytes, int off, int len) {
/* 50 */     super(text, bytes, off, len);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public UnicodeCommentExtraField(String comment, byte[] bytes) {
/* 61 */     super(comment, bytes);
/*    */   }
/*    */ 
/*    */   
/*    */   public ZipShort getHeaderId() {
/* 66 */     return UCOM_ID;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/zip/UnicodeCommentExtraField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */