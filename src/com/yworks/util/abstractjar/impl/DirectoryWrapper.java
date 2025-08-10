/*    */ package com.yworks.util.abstractjar.impl;
/*    */ 
/*    */ import com.yworks.util.abstractjar.Archive;
/*    */ import com.yworks.util.abstractjar.Entry;
/*    */ import java.io.BufferedInputStream;
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.nio.file.FileVisitResult;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.SimpleFileVisitor;
/*    */ import java.nio.file.attribute.BasicFileAttributes;
/*    */ import java.util.Collections;
/*    */ import java.util.Enumeration;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.jar.Manifest;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DirectoryWrapper
/*    */   extends SimpleFileVisitor<Path>
/*    */   implements Archive
/*    */ {
/*    */   private File directory;
/* 28 */   private Map<Entry, File> entries = new HashMap<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DirectoryWrapper(File directory) throws IOException {
/* 37 */     this.directory = directory;
/* 38 */     Files.walkFileTree(directory.toPath(), this);
/*    */   }
/*    */ 
/*    */   
/*    */   public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
/* 43 */     if (attrs.isRegularFile()) {
/* 44 */       this.entries.put(new FileEntryWrapper(path.toFile(), this.directory.toPath().relativize(path).toString()), path.toFile());
/*    */     }
/* 46 */     return FileVisitResult.CONTINUE;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 51 */     return this.directory.getName();
/*    */   }
/*    */ 
/*    */   
/*    */   public Enumeration<Entry> getEntries() {
/* 56 */     return Collections.enumeration(this.entries.keySet());
/*    */   }
/*    */ 
/*    */   
/*    */   public Manifest getManifest() throws IOException {
/* 61 */     File manifestFile = new File(this.directory, "META-INF/MANIFEST.MF");
/* 62 */     if (manifestFile.exists()) {
/* 63 */       BufferedInputStream is = new BufferedInputStream(new FileInputStream(manifestFile)); 
/* 64 */       try { Manifest manifest = new Manifest(is);
/* 65 */         is.close(); return manifest; } catch (Throwable throwable) { try { is.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }
/*    */     
/* 67 */     }  return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public InputStream getInputStream(Entry entry) throws IOException {
/* 72 */     return new FileInputStream(this.entries.get(entry));
/*    */   }
/*    */   
/*    */   public void close() throws IOException {}
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/util/abstractjar/impl/DirectoryWrapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */