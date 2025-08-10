/*     */ package com.github.utils4j.gui.imp;
/*     */ 
/*     */ import com.github.utils4j.imp.Media;
/*     */ import com.github.utils4j.imp.States;
/*     */ import com.github.utils4j.imp.Threads;
/*     */ import java.io.File;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Supplier;
/*     */ import javax.swing.filechooser.FileNameExtensionFilter;
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
/*     */ public enum DelayedFileChooser
/*     */ {
/*  44 */   DIALOG;
/*     */   
/*  46 */   private volatile DejavuFileChooser cache = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   DelayedFileChooser() {
/*  54 */     Threads.startDaemon(() -> this.cache = new DejavuFileChooser());
/*     */   }
/*     */   
/*     */   public DelayedFileChooser filesOnly() {
/*  58 */     States.requireNonNull(this.cache, "filechooser ainda não inicializado");
/*  59 */     this.cache.setFileSelectionMode(0);
/*  60 */     return this;
/*     */   }
/*     */   
/*     */   public DelayedFileChooser folderOnly() {
/*  64 */     States.requireNonNull(this.cache, "filechooser ainda não inicializado");
/*  65 */     this.cache.setFileSelectionMode(1);
/*  66 */     return this;
/*     */   }
/*     */   
/*     */   public Optional<File> select(String title, Media media) {
/*  70 */     return select(title, media, null);
/*     */   }
/*     */   
/*     */   public Optional<File> select(String title, Media media, File current) {
/*  74 */     States.requireNonNull(this.cache, "filechooser ainda não inicializado");
/*  75 */     return this.cache.singleSelect(title, media, current);
/*     */   }
/*     */   
/*     */   public Optional<File[]> multiSelect(String title, Media media) {
/*  79 */     States.requireNonNull(this.cache, "filechooser ainda não inicializado");
/*  80 */     return this.cache.multiSelect(title, media, (File)null);
/*     */   }
/*     */   
/*     */   public Optional<File[]> multiSelect(String title, Media media, File current) {
/*  84 */     States.requireNonNull(this.cache, "filechooser ainda não inicializado");
/*  85 */     return this.cache.multiSelect(title, media, current);
/*     */   }
/*     */   
/*     */   private static class DejavuFileChooser
/*     */     extends DefaultFileChooser {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     private DejavuFileChooser() {
/*  93 */       setMultiSelectionEnabled(false);
/*  94 */       setAcceptAllFileFilterUsed(false);
/*  95 */       setFileSelectionMode(0);
/*     */     }
/*     */     
/*     */     private Optional<File> singleSelect(String title, Media media, File current) {
/*  99 */       setMultiSelectionEnabled(false);
/* 100 */       return doSelect(title, media, current, () -> getSelectedFile());
/*     */     }
/*     */     
/*     */     private Optional<File[]> multiSelect(String title, Media media, File current) {
/* 104 */       setMultiSelectionEnabled(true);
/* 105 */       return (Optional)doSelect(title, media, current, () -> getSelectedFiles());
/*     */     }
/*     */     
/*     */     private <T> Optional<T> doSelect(String title, Media media, File current, Supplier<T> supplier) {
/* 109 */       resetChoosableFileFilters();
/* 110 */       setCurrentDirectory(current);
/* 111 */       setSelectedFile(new File(""));
/* 112 */       setDialogTitle("Selecione para " + title.toLowerCase());
/* 113 */       setFileFilter(new FileNameExtensionFilter("Arquivos " + media.name(), new String[] { media.getExtension() }));
/* 114 */       if (showOpenDialog(null) == 1)
/* 115 */         return Optional.empty(); 
/* 116 */       return Optional.of(supplier.get());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/gui/imp/DelayedFileChooser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */