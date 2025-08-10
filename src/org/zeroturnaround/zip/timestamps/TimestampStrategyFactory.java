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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TimestampStrategyFactory
/*    */ {
/* 28 */   public static boolean HAS_ZIP_ENTRY_FILE_TIME_METHODS = hasZipEntryFileTimeMethods();
/*    */   
/* 30 */   private static TimestampStrategy INSTANCE = getStrategy();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static boolean hasZipEntryFileTimeMethods() {
/*    */     try {
/* 37 */       ZipEntry.class.getDeclaredMethod("getCreationTime", new Class[0]);
/* 38 */       return true;
/*    */     }
/* 40 */     catch (Exception e) {
/* 41 */       return false;
/*    */     } 
/*    */   }
/*    */   
/*    */   private static TimestampStrategy getStrategy() {
/* 46 */     if (HAS_ZIP_ENTRY_FILE_TIME_METHODS) {
/* 47 */       return new Java8TimestampStrategy();
/*    */     }
/*    */     
/* 50 */     return new PreJava8TimestampStrategy();
/*    */   }
/*    */ 
/*    */   
/*    */   public static TimestampStrategy getInstance() {
/* 55 */     return INSTANCE;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/timestamps/TimestampStrategyFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */