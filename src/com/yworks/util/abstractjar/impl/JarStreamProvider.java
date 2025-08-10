/*     */ package com.yworks.util.abstractjar.impl;
/*     */ 
/*     */ import com.yworks.util.abstractjar.Entry;
/*     */ import com.yworks.util.abstractjar.StreamProvider;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JarStreamProvider
/*     */   implements StreamProvider
/*     */ {
/*     */   private JarFile f;
/*     */   private Enumeration<? extends JarEntry> en;
/*     */   JarEntry currentEntry;
/*     */   private String currentEntryName;
/*     */   private String currentDir;
/*     */   private String currentFilename;
/*     */   
/*     */   public JarStreamProvider(File jarFile) throws IOException {
/*  39 */     if (!jarFile.exists()) {
/*  40 */       throw new IllegalArgumentException("jar file not found: " + jarFile.toString());
/*     */     }
/*  42 */     this.f = new JarFile(jarFile);
/*  43 */     this.en = this.f.entries();
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() {
/*  48 */     this.en = this.f.entries();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DataInputStream getNextClassEntryStream() throws IOException {
/*  54 */     JarEntry entry = null;
/*     */     
/*  56 */     while (this.en.hasMoreElements()) {
/*     */       
/*  58 */       entry = this.en.nextElement();
/*  59 */       if (entry.getName().endsWith(".class")) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/*  64 */     if (entry != null && entry.getName().endsWith(".class")) {
/*  65 */       setCurrentEntry(entry);
/*  66 */       return new DataInputStream(new BufferedInputStream(this.f.getInputStream(entry)));
/*     */     } 
/*  68 */     setCurrentEntry(null);
/*  69 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DataInputStream getNextResourceEntryStream() throws IOException {
/*  75 */     JarEntry entry = null;
/*     */     
/*  77 */     while (this.en.hasMoreElements()) {
/*     */       
/*  79 */       entry = this.en.nextElement();
/*  80 */       if (!entry.getName().endsWith(".class") && !entry.isDirectory()) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/*  85 */     if (entry != null && !entry.getName().endsWith(".class") && !entry.isDirectory()) {
/*  86 */       setCurrentEntry(entry);
/*  87 */       return new DataInputStream(new BufferedInputStream(this.f.getInputStream(entry)));
/*     */     } 
/*  89 */     setCurrentEntry(null);
/*  90 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Entry getCurrentEntry() {
/*  96 */     return new JarEntryWrapper(this.currentEntry);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCurrentEntryName() {
/* 101 */     return this.currentEntryName;
/*     */   }
/*     */   
/*     */   public String getCurrentDir() {
/* 105 */     return this.currentDir;
/*     */   }
/*     */   
/*     */   public String getCurrentFilename() {
/* 109 */     return this.currentFilename;
/*     */   }
/*     */ 
/*     */   
/*     */   private void setCurrentEntry(JarEntry entry) {
/* 114 */     if (null != entry) {
/* 115 */       this.currentEntry = entry;
/* 116 */       this.currentEntryName = this.currentEntry.getName();
/* 117 */       File entryFile = new File(this.currentEntryName);
/* 118 */       this.currentDir = (entryFile.getParent() != null) ? entryFile.getParent() : "";
/* 119 */       this.currentFilename = entryFile.getName();
/*     */     } else {
/* 121 */       this.currentEntry = null;
/* 122 */       this.currentDir = null;
/* 123 */       this.currentFilename = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 129 */     JarFile archive = this.f;
/*     */     
/* 131 */     this.f = null;
/* 132 */     this.en = null;
/*     */     
/* 134 */     if (archive != null)
/* 135 */       archive.close(); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/util/abstractjar/impl/JarStreamProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */