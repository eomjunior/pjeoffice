/*    */ package com.yworks.util.abstractjar.impl;
/*    */ 
/*    */ import com.yworks.util.abstractjar.Archive;
/*    */ import com.yworks.util.abstractjar.Entry;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.Collections;
/*    */ import java.util.Enumeration;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.jar.JarEntry;
/*    */ import java.util.jar.JarFile;
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
/*    */ public class JarFileWrapper
/*    */   implements Archive
/*    */ {
/*    */   JarFile jarFile;
/* 28 */   Map<Entry, JarEntry> entries = new HashMap<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JarFileWrapper(File file) throws IOException {
/* 37 */     this.jarFile = new JarFile(file);
/* 38 */     Enumeration<JarEntry> jarEntryEnumeration = this.jarFile.entries();
/* 39 */     while (jarEntryEnumeration.hasMoreElements()) {
/* 40 */       JarEntry jarEntry = jarEntryEnumeration.nextElement();
/* 41 */       this.entries.put(new JarEntryWrapper(jarEntry), jarEntry);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 47 */     return this.jarFile.getName();
/*    */   }
/*    */ 
/*    */   
/*    */   public Enumeration<Entry> getEntries() {
/* 52 */     return Collections.enumeration(this.entries.keySet());
/*    */   }
/*    */ 
/*    */   
/*    */   public Manifest getManifest() throws IOException {
/* 57 */     return this.jarFile.getManifest();
/*    */   }
/*    */ 
/*    */   
/*    */   public InputStream getInputStream(Entry entry) throws IOException {
/* 62 */     return this.jarFile.getInputStream(this.entries.get(entry));
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 67 */     this.jarFile.close();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/util/abstractjar/impl/JarFileWrapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */