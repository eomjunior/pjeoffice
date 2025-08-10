/*    */ package org.zeroturnaround.zip;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileFilter;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
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
/*    */ public final class ZTFileUtil
/*    */ {
/*    */   public static Collection<File> listFiles(File dir) {
/* 28 */     return listFiles(dir, null);
/*    */   }
/*    */   
/*    */   public static Collection<File> listFiles(File dir, FileFilter filter) {
/* 32 */     Collection<File> accumulator = new ArrayList<File>();
/*    */     
/* 34 */     if (dir.isFile()) {
/* 35 */       return accumulator;
/*    */     }
/*    */     
/* 38 */     if (filter == null)
/*    */     {
/* 40 */       filter = new FileFilter() {
/*    */           public boolean accept(File pathname) {
/* 42 */             return true;
/*    */           }
/*    */         };
/*    */     }
/*    */     
/* 47 */     innerListFiles(dir, accumulator, filter);
/* 48 */     return accumulator;
/*    */   }
/*    */ 
/*    */   
/*    */   private static void innerListFiles(File dir, Collection<File> accumulator, FileFilter filter) {
/* 53 */     String[] filenames = dir.list();
/*    */     
/* 55 */     if (filenames != null)
/* 56 */       for (int i = 0; i < filenames.length; i++) {
/* 57 */         File file = new File(dir, filenames[i]);
/* 58 */         if (file.isDirectory()) {
/* 59 */           innerListFiles(file, accumulator, filter);
/*    */         
/*    */         }
/* 62 */         else if (filter != null && filter.accept(file)) {
/* 63 */           accumulator.add(file);
/*    */         } 
/*    */       }  
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/ZTFileUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */