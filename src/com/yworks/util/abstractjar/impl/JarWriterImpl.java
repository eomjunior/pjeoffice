/*    */ package com.yworks.util.abstractjar.impl;
/*    */ 
/*    */ import com.yworks.util.abstractjar.ArchiveWriter;
/*    */ import java.io.BufferedOutputStream;
/*    */ import java.io.File;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.util.jar.JarEntry;
/*    */ import java.util.jar.JarOutputStream;
/*    */ import java.util.jar.Manifest;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JarWriterImpl
/*    */   extends ArchiveWriter
/*    */ {
/*    */   private JarOutputStream jos;
/*    */   
/*    */   public JarWriterImpl(File archive, Manifest man) throws IOException {
/* 22 */     super(man);
/*    */     
/* 24 */     this.jos = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(archive)), man);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setComment(String comment) {
/* 29 */     this.jos.setComment(comment);
/*    */   }
/*    */ 
/*    */   
/*    */   public void addDirectory(String path) throws IOException {
/* 34 */     ensureValid(path);
/* 35 */     newEntry(this.jos, ensureDirName(path));
/* 36 */     this.jos.closeEntry();
/*    */   }
/*    */ 
/*    */   
/*    */   public void addFile(String path, byte[] data) throws IOException {
/* 41 */     ensureValid(path);
/* 42 */     newEntry(this.jos, path);
/* 43 */     this.jos.write(data);
/* 44 */     this.jos.closeEntry();
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 49 */     if (this.jos != null) {
/* 50 */       this.jos.flush();
/* 51 */       this.jos.close();
/* 52 */       this.jos = null;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static void newEntry(JarOutputStream jos, String path) throws IOException {
/* 60 */     jos.putNextEntry(new JarEntry(path));
/*    */   }
/*    */   
/*    */   private static String ensureDirName(String path) {
/* 64 */     return path.endsWith("/") ? path : (path + '/');
/*    */   }
/*    */   
/*    */   private static void ensureValid(String path) {
/* 68 */     if (path == null)
/* 69 */       throw new NullPointerException("path"); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/util/abstractjar/impl/JarWriterImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */