/*    */ package org.zeroturnaround.zip.transform;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ZipEntryTransformerEntry
/*    */ {
/*    */   private final String path;
/*    */   private final ZipEntryTransformer transformer;
/*    */   
/*    */   public ZipEntryTransformerEntry(String path, ZipEntryTransformer transformer) {
/* 15 */     this.path = path;
/* 16 */     this.transformer = transformer;
/*    */   }
/*    */   
/*    */   public String getPath() {
/* 20 */     return this.path;
/*    */   }
/*    */   
/*    */   public ZipEntryTransformer getTransformer() {
/* 24 */     return this.transformer;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 28 */     return this.path + "=" + this.transformer;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/transform/ZipEntryTransformerEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */