/*     */ package com.github.utils4j.imp;
/*     */ 
/*     */ import com.github.utils4j.IDownloadStatus;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Optional;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ public class DownloadStatus
/*     */   implements IDownloadStatus
/*     */ {
/*  43 */   protected static final Logger LOGGER = LoggerFactory.getLogger(IDownloadStatus.class);
/*     */   
/*     */   private File output;
/*     */   
/*     */   private OutputStream out;
/*     */   
/*     */   private boolean online = false;
/*     */   
/*     */   private final boolean rejectEmpty;
/*     */   
/*     */   public DownloadStatus() {
/*  54 */     this(true);
/*     */   }
/*     */   
/*     */   public DownloadStatus(boolean rejectEmpty) {
/*  58 */     this(rejectEmpty, null);
/*     */   }
/*     */   
/*     */   public DownloadStatus(File output) {
/*  62 */     this(true, output);
/*     */   }
/*     */   
/*     */   public DownloadStatus(boolean rejectEmpty, File output) {
/*  66 */     this.rejectEmpty = rejectEmpty;
/*  67 */     this.output = output;
/*     */   }
/*     */   
/*     */   private void checkIfOffline() {
/*  71 */     throwIfOnlineIs(true, "status is online");
/*     */   }
/*     */   
/*     */   private void checkIfOnline() {
/*  75 */     throwIfOnlineIs(false, "status is offline");
/*     */   }
/*     */   
/*     */   private void throwIfOnlineIs(boolean status, String message) {
/*  79 */     if (this.online == status) {
/*  80 */       throw new IllegalStateException(message);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final OutputStream onNewTry() throws IOException {
/*  86 */     checkIfOffline();
/*  87 */     if (this.output == null) {
/*  88 */       this.output = Directory.createTempFile("downloaded");
/*     */     }
/*  90 */     this.out = new FileOutputStream(this.output)
/*     */       {
/*     */         public void close() throws IOException {
/*     */           try {
/*  94 */             super.close();
/*     */           } finally {
/*  96 */             DownloadStatus.this.out = null;
/*  97 */             DownloadStatus.this.checkIfEmpty(false);
/*  98 */             DownloadStatus.this.online = false;
/*     */           } 
/*     */         }
/*     */       };
/* 102 */     this.online = true;
/* 103 */     return this.out;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void onStartDownload(long total) throws InterruptedException {
/* 108 */     checkIfOnline();
/* 109 */     onStepStart(total);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void onStatus(long total, long written) throws InterruptedException {
/* 114 */     checkIfOnline();
/* 115 */     onStepStatus(written);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void onDownloadFail(Throwable e) {
/* 120 */     Streams.closeQuietly(this.out);
/* 121 */     checkIfEmpty(true);
/* 122 */     onStepFail(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void onEndDownload() throws InterruptedException {
/* 127 */     Streams.closeQuietly(this.out);
/* 128 */     onStepEnd();
/*     */   }
/*     */   
/*     */   private void checkIfEmpty(boolean force) {
/* 132 */     if (this.output != null && (
/* 133 */       force || (this.rejectEmpty && this.output.length() == 0L))) {
/* 134 */       this.output.delete();
/* 135 */       this.output = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Optional<File> getDownloadedFile() {
/* 142 */     checkIfOffline();
/* 143 */     return Optional.ofNullable(this.output);
/*     */   }
/*     */   
/*     */   protected void onStepStart(long total) throws InterruptedException {}
/*     */   
/*     */   protected void onStepStatus(long written) throws InterruptedException {}
/*     */   
/*     */   protected void onStepEnd() throws InterruptedException {}
/*     */   
/*     */   protected void onStepFail(Throwable e) {
/* 153 */     LOGGER.warn("step fail", e);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/DownloadStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */