/*    */ package org.zeroturnaround.zip.timestamps;
/*    */ 
/*    */ import java.nio.file.attribute.FileTime;
/*    */ import java.util.zip.ZipEntry;
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
/*    */ public class Java8TimestampStrategy
/*    */   implements TimestampStrategy
/*    */ {
/*    */   public void setTime(ZipEntry newInstance, ZipEntry oldInstance) {
/* 31 */     FileTime time = oldInstance.getCreationTime();
/* 32 */     if (time != null) {
/* 33 */       newInstance.setCreationTime(time);
/*    */     }
/*    */ 
/*    */     
/* 37 */     time = oldInstance.getLastModifiedTime();
/* 38 */     if (time != null) {
/* 39 */       newInstance.setLastModifiedTime(time);
/*    */     }
/*    */ 
/*    */     
/* 43 */     time = oldInstance.getLastAccessTime();
/* 44 */     if (time != null)
/* 45 */       newInstance.setLastAccessTime(time); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/timestamps/Java8TimestampStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */