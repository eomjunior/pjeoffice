/*     */ package com.yworks.util.abstractjar.impl;
/*     */ 
/*     */ import com.yworks.util.abstractjar.Entry;
/*     */ import com.yworks.util.abstractjar.StreamProvider;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.SimpleFileVisitor;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ public class DirectoryStreamProvider
/*     */   extends SimpleFileVisitor<Path>
/*     */   implements StreamProvider
/*     */ {
/*     */   private File directory;
/*  25 */   private List<Entry> entries = new ArrayList<>();
/*     */ 
/*     */   
/*     */   private Iterator<Entry> entryIterator;
/*     */ 
/*     */   
/*     */   private Entry currentEntry;
/*     */ 
/*     */ 
/*     */   
/*     */   public DirectoryStreamProvider(File directory) throws IOException {
/*  36 */     this.directory = directory;
/*  37 */     Files.walkFileTree(directory.toPath(), this);
/*  38 */     this.entryIterator = this.entries.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
/*  43 */     if (attrs.isRegularFile()) {
/*  44 */       this.entries.add(new FileEntryWrapper(path.toFile(), this.directory.toPath().relativize(path).toString()));
/*     */     }
/*  46 */     return FileVisitResult.CONTINUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public DataInputStream getNextClassEntryStream() throws IOException {
/*  51 */     FileEntryWrapper entry = null;
/*     */     
/*  53 */     while (this.entryIterator.hasNext()) {
/*  54 */       entry = (FileEntryWrapper)this.entryIterator.next();
/*  55 */       if (entry.getName().endsWith(".class")) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/*  60 */     if (entry != null && entry.getName().endsWith(".class")) {
/*  61 */       this.currentEntry = entry;
/*  62 */       return new DataInputStream(new BufferedInputStream(new FileInputStream(entry.getFile())));
/*     */     } 
/*  64 */     this.currentEntry = null;
/*  65 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DataInputStream getNextResourceEntryStream() throws IOException {
/*  71 */     FileEntryWrapper entry = null;
/*     */     
/*  73 */     while (this.entryIterator.hasNext()) {
/*  74 */       entry = (FileEntryWrapper)this.entryIterator.next();
/*  75 */       if (!entry.getName().endsWith(".class")) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/*  80 */     if (entry != null && !entry.getName().endsWith(".class") && !entry.isDirectory()) {
/*  81 */       this.currentEntry = entry;
/*  82 */       return new DataInputStream(new BufferedInputStream(new FileInputStream(entry.getFile())));
/*     */     } 
/*  84 */     this.currentEntry = null;
/*  85 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Entry getCurrentEntry() {
/*  91 */     return this.currentEntry;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCurrentEntryName() {
/*  96 */     if (this.currentEntry != null) {
/*  97 */       FileEntryWrapper entryWrapper = (FileEntryWrapper)this.currentEntry;
/*  98 */       return this.directory.toPath().relativize(entryWrapper.getFile().toPath()).toString().replace("\\", "/");
/*     */     } 
/* 100 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCurrentDir() {
/* 105 */     if (this.currentEntry != null) {
/* 106 */       FileEntryWrapper entryWrapper = (FileEntryWrapper)this.currentEntry;
/*     */       
/* 108 */       return this.directory.toPath().relativize(entryWrapper.getFile().toPath().getParent()).toString();
/*     */     } 
/* 110 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCurrentFilename() {
/* 115 */     return (this.currentEntry != null) ? this.currentEntry.getName() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() {
/* 120 */     this.entryIterator = this.entries.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 125 */     this.entries = null;
/* 126 */     this.entryIterator = null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/util/abstractjar/impl/DirectoryStreamProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */