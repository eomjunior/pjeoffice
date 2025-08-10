/*    */ package org.zeroturnaround.zip.timestamps;
/*    */ 
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
/*    */ public class PreJava8TimestampStrategy
/*    */   implements TimestampStrategy
/*    */ {
/*    */   public void setTime(ZipEntry newInstance, ZipEntry oldInstance) {
/* 27 */     long time = oldInstance.getTime();
/* 28 */     if (time != -1L)
/* 29 */       newInstance.setTime(time); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/timestamps/PreJava8TimestampStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */