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
/*    */ public class UnicodePathExtraField
/*    */   extends AbstractUnicodeExtraField
/*    */ {
/* 32 */   public static final ZipShort UPATH_ID = new ZipShort(28789);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public UnicodePathExtraField() {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public UnicodePathExtraField(String text, byte[] bytes, int off, int len) {
/* 48 */     super(text, bytes, off, len);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public UnicodePathExtraField(String name, byte[] bytes) {
/* 59 */     super(name, bytes);
/*    */   }
/*    */ 
/*    */   
/*    */   public ZipShort getHeaderId() {
/* 64 */     return UPATH_ID;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/zip/UnicodePathExtraField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */