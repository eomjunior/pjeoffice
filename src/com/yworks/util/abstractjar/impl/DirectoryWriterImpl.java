/*    */ package com.yworks.util.abstractjar.impl;
/*    */ 
/*    */ import com.yworks.util.abstractjar.ArchiveWriter;
/*    */ import java.io.BufferedOutputStream;
/*    */ import java.io.File;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.util.jar.Manifest;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DirectoryWriterImpl
/*    */   extends ArchiveWriter
/*    */ {
/*    */   private final File archive;
/*    */   
/*    */   public DirectoryWriterImpl(File archive, Manifest man) throws IOException {
/* 24 */     super(man);
/*    */     
/* 26 */     this.archive = archive;
/*    */     
/* 28 */     writeManifest(man);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setComment(String comment) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void addDirectory(String path) throws IOException {
/* 38 */     File file = new File(this.archive, path);
/* 39 */     makeDirs(file);
/*    */   }
/*    */ 
/*    */   
/*    */   public void addFile(String path, byte[] data) throws IOException {
/* 44 */     File tgt = new File(this.archive, path);
/* 45 */     ensurePath(tgt);
/* 46 */     OutputStream os = new FileOutputStream(tgt);
/*    */     try {
/* 48 */       os.write(data);
/* 49 */       os.flush();
/*    */     } finally {
/* 51 */       os.close();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() throws IOException {}
/*    */ 
/*    */ 
/*    */   
/*    */   private void writeManifest(Manifest manifest) throws IOException {
/* 62 */     File tgt = new File(this.archive, "META-INF/MANIFEST.MF");
/* 63 */     ensurePath(tgt);
/* 64 */     OutputStream os = new BufferedOutputStream(new FileOutputStream(tgt));
/*    */     try {
/* 66 */       manifest.write(os);
/* 67 */       os.flush();
/*    */     } finally {
/* 69 */       os.close();
/*    */     } 
/*    */   }
/*    */   
/*    */   private static void ensurePath(File file) throws IOException {
/* 74 */     makeDirs(file.getParentFile());
/*    */   }
/*    */   
/*    */   private static void makeDirs(File file) throws IOException {
/* 78 */     if (!file.isDirectory() && 
/* 79 */       !file.mkdirs())
/* 80 */       throw new IOException("Could not create directory " + file.getAbsolutePath() + '.'); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/util/abstractjar/impl/DirectoryWriterImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */