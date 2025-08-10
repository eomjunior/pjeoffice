/*    */ package org.zeroturnaround.zip.commons;
/*    */ 
/*    */ import java.io.BufferedInputStream;
/*    */ import java.io.BufferedOutputStream;
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
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
/*    */ public class FileUtils
/*    */   extends FileUtilsV2_2
/*    */ {
/*    */   public static void copy(File file, OutputStream out) throws IOException {
/* 34 */     FileInputStream in = new FileInputStream(file);
/*    */     try {
/* 36 */       IOUtils.copy(new BufferedInputStream(in), out);
/*    */     } finally {
/*    */       
/* 39 */       IOUtils.closeQuietly(in);
/*    */     } 
/*    */   }
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
/*    */   public static void copy(InputStream in, File file) throws IOException {
/* 54 */     OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
/*    */     try {
/* 56 */       IOUtils.copy(in, out);
/*    */     } finally {
/*    */       
/* 59 */       IOUtils.closeQuietly(out);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static File getTempFileFor(File file) {
/* 70 */     File parent = file.getParentFile();
/* 71 */     String name = file.getName();
/*    */     
/* 73 */     int index = 0;
/*    */     while (true) {
/* 75 */       File result = new File(parent, name + "_" + index++);
/*    */       
/* 77 */       if (!result.exists())
/* 78 */         return result; 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/commons/FileUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */