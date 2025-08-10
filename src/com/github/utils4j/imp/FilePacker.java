/*     */ package com.github.utils4j.imp;
/*     */ 
/*     */ import com.github.utils4j.IFilePacker;
/*     */ import com.sun.nio.file.SensitivityWatchEventModifier;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.nio.file.FileSystems;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardWatchEventKinds;
/*     */ import java.nio.file.WatchEvent;
/*     */ import java.nio.file.WatchKey;
/*     */ import java.nio.file.WatchService;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ 
/*     */ 
/*     */ public class FilePacker<E extends Exception>
/*     */   extends ThreadContext<E>
/*     */   implements IFilePacker<E>
/*     */ {
/*     */   private static final long TIMEOUT_TIMER = 2000L;
/*     */   private long startTime;
/*     */   private final File lockFile;
/*     */   private final Path folderWatching;
/*     */   private WatchService watchService;
/*  60 */   private RandomAccessFile lock = null;
/*     */   
/*  62 */   private List<File> pathPool = new LinkedList<>();
/*     */   
/*  64 */   private final List<List<File>> availableFiles = new LinkedList<>();
/*     */   
/*     */   public FilePacker(Path folderWatching) {
/*  67 */     super("shell-packer");
/*  68 */     this.folderWatching = Args.<Path>requireNonNull(folderWatching, "folderWatching is null");
/*  69 */     this.lockFile = folderWatching.resolve(".lock").toFile();
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() {
/*  74 */     cleanFolder();
/*  75 */     synchronized (this.availableFiles) {
/*  76 */       this.availableFiles.forEach(List::clear);
/*  77 */       this.availableFiles.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void beforeRun() throws IOException {
/*  83 */     install();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void afterRun() {
/*  88 */     uninstall();
/*     */   }
/*     */   
/*     */   private void install() throws IOException {
/*  92 */     Directory.mkDir(this.folderWatching);
/*  93 */     this.lockFile.createNewFile();
/*  94 */     this.lock = new RandomAccessFile(this.lockFile, "r");
/*  95 */     this.watchService = FileSystems.getDefault().newWatchService();
/*  96 */     this.folderWatching.register(this.watchService, (WatchEvent.Kind<?>[])new WatchEvent.Kind[] { StandardWatchEventKinds.ENTRY_CREATE }, new WatchEvent.Modifier[] { SensitivityWatchEventModifier.HIGH });
/*     */   }
/*     */   
/*     */   private void uninstall() {
/* 100 */     closeService();
/* 101 */     reset();
/* 102 */     releaseLock();
/* 103 */     this.pathPool.clear();
/*     */   }
/*     */   
/*     */   private void cleanFolder() {
/* 107 */     Throwables.quietly(() -> Directory.rmDir(this.folderWatching, ()));
/*     */   }
/*     */   
/*     */   private void closeService() {
/* 111 */     if (this.watchService != null) {
/* 112 */       Throwables.quietly(this.watchService::close);
/* 113 */       this.watchService = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void releaseLock() {
/* 118 */     if (this.lock != null) {
/* 119 */       Throwables.quietly(this.lock::close);
/* 120 */       this.lockFile.delete();
/* 121 */       this.lock = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void pack(File file) {
/* 126 */     if (!file.isDirectory()) {
/* 127 */       this.startTime = System.currentTimeMillis();
/* 128 */       this.pathPool.add(file);
/* 129 */       onPacked(file);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPacked(File file) {}
/*     */ 
/*     */   
/*     */   public List<File> filesPackage() throws InterruptedException {
/* 138 */     checkIsAlive();
/* 139 */     synchronized (this.availableFiles) {
/* 140 */       while (this.availableFiles.isEmpty()) {
/* 141 */         this.availableFiles.wait();
/*     */       }
/* 143 */       return this.availableFiles.remove(0);
/*     */     } 
/*     */   }
/*     */   
/*     */   private List<File> block() {
/* 148 */     List<File> newBlock = this.pathPool;
/* 149 */     this.pathPool = new LinkedList<>();
/* 150 */     return newBlock;
/*     */   }
/*     */   
/*     */   private boolean hasTimeout() {
/* 154 */     return (System.currentTimeMillis() - this.startTime > 2000L);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doRun() throws Exception {
/*     */     while (true) {
/*     */       WatchKey key;
/* 161 */       while ((key = this.watchService.poll(250L, TimeUnit.MILLISECONDS)) == null) {
/* 162 */         if (!this.pathPool.isEmpty() && hasTimeout()) {
/* 163 */           offer(block());
/*     */         }
/*     */       } 
/*     */       
/* 167 */       try { for (WatchEvent<?> e : key.pollEvents()) {
/* 168 */           if (e.count() <= 1) {
/*     */             
/* 170 */             WatchEvent<Path> event = (WatchEvent)e;
/* 171 */             Path folder = (Path)key.watchable();
/* 172 */             Path fileName = event.context();
/* 173 */             Path file = folder.resolve(fileName);
/* 174 */             pack(file.toFile());
/*     */           } 
/*     */         } 
/*     */         
/* 178 */         if (this.pathPool.isEmpty() || !hasTimeout())
/*     */         
/*     */         { 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 185 */           key.reset(); continue; }  offer(block()); } finally { key.reset(); }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void offer(List<File> block) throws InterruptedException {
/* 192 */     if (block != null)
/* 193 */       synchronized (this.availableFiles) {
/* 194 */         this.availableFiles.add(block);
/* 195 */         this.availableFiles.notifyAll();
/* 196 */         onAvailable(block);
/*     */       }  
/*     */   }
/*     */   
/*     */   protected void onAvailable(List<File> block) {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/FilePacker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */