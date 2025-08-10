/*    */ package com.github.filehandler4j.imp;
/*    */ 
/*    */ import com.github.filehandler4j.IInputFile;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import com.github.utils4j.imp.Directory;
/*    */ import java.io.File;
/*    */ import java.nio.file.Path;
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
/*    */ public class FileWrapper
/*    */   implements IInputFile
/*    */ {
/*    */   private final File file;
/*    */   
/*    */   public FileWrapper(File file) {
/* 42 */     this.file = (File)Args.requireNonNull(file, "file is null");
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAbsolutePath() {
/* 47 */     return Directory.stringPath(this.file);
/*    */   }
/*    */ 
/*    */   
/*    */   public long length() {
/* 52 */     return this.file.length();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean exists() {
/* 57 */     return this.file.exists();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 62 */     return this.file.getName();
/*    */   }
/*    */ 
/*    */   
/*    */   public Path toPath() {
/* 67 */     return this.file.toPath();
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getShortName() {
/* 72 */     String name = this.file.getName();
/* 73 */     int dot = name.lastIndexOf('.');
/* 74 */     return name.substring(0, (dot > 0) ? dot : name.length());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/filehandler4j/imp/FileWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */