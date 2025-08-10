/*    */ package org.zeroturnaround.zip;
/*    */ 
/*    */ import java.io.BufferedInputStream;
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
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
/*    */ public class FileSource
/*    */   implements ZipEntrySource
/*    */ {
/*    */   private final String path;
/*    */   private final File file;
/*    */   
/*    */   public FileSource(String path, File file) {
/* 36 */     this.path = path;
/* 37 */     this.file = file;
/*    */   }
/*    */   
/*    */   public String getPath() {
/* 41 */     return this.path;
/*    */   }
/*    */   
/*    */   public ZipEntry getEntry() {
/* 45 */     ZipEntry entry = ZipEntryUtil.fromFile(this.path, this.file);
/* 46 */     return entry;
/*    */   }
/*    */   
/*    */   public InputStream getInputStream() throws IOException {
/* 50 */     if (this.file.isDirectory()) {
/* 51 */       return null;
/*    */     }
/*    */     
/* 54 */     return new BufferedInputStream(new FileInputStream(this.file));
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 59 */     return "FileSource[" + this.path + ", " + this.file + "]";
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
/*    */ 
/*    */ 
/*    */   
/*    */   public static FileSource[] pair(File[] files, String[] names) {
/* 76 */     if (files.length > names.length) {
/* 77 */       throw new IllegalArgumentException("names array must contain at least the same amount of items as files array or more");
/*    */     }
/*    */ 
/*    */     
/* 81 */     FileSource[] result = new FileSource[files.length];
/* 82 */     for (int i = 0; i < files.length; i++) {
/* 83 */       result[i] = new FileSource(names[i], files[i]);
/*    */     }
/* 85 */     return result;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/FileSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */