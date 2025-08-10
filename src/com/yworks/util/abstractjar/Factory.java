/*    */ package com.yworks.util.abstractjar;
/*    */ 
/*    */ import com.yworks.util.abstractjar.impl.DirectoryStreamProvider;
/*    */ import com.yworks.util.abstractjar.impl.DirectoryWrapper;
/*    */ import com.yworks.util.abstractjar.impl.DirectoryWriterImpl;
/*    */ import com.yworks.util.abstractjar.impl.JarFileWrapper;
/*    */ import com.yworks.util.abstractjar.impl.JarStreamProvider;
/*    */ import com.yworks.util.abstractjar.impl.JarWriterImpl;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.util.jar.Manifest;
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
/*    */ public class Factory
/*    */ {
/*    */   public static Archive newArchive(File archive) throws IOException {
/* 25 */     return archive.isDirectory() ? 
/* 26 */       (Archive)new DirectoryWrapper(archive) : 
/* 27 */       (Archive)new JarFileWrapper(archive);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static StreamProvider newStreamProvider(File archive) throws IOException {
/* 33 */     return archive.isDirectory() ? 
/* 34 */       (StreamProvider)new DirectoryStreamProvider(archive) : 
/* 35 */       (StreamProvider)new JarStreamProvider(archive);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static ArchiveWriter newArchiveWriter(File archive, Manifest manifest) throws IOException {
/* 41 */     return archive.isDirectory() ? 
/* 42 */       (ArchiveWriter)new DirectoryWriterImpl(archive, manifest) : 
/* 43 */       (ArchiveWriter)new JarWriterImpl(archive, manifest);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/util/abstractjar/Factory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */