/*     */ package com.github.progress4j.imp;
/*     */ 
/*     */ import com.github.progress4j.IProgress;
/*     */ import com.github.progress4j.IStage;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.DownloadStatus;
/*     */ import com.github.utils4j.imp.Sizes;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import java.io.File;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProgressStatus
/*     */   extends DownloadStatus
/*     */ {
/*     */   private long total;
/*  17 */   private long increment = 1L;
/*     */   
/*     */   private final IStage stage;
/*     */   
/*     */   private final IProgress progress;
/*     */   
/*     */   public ProgressStatus(IProgress progress, String stage) {
/*  24 */     this(progress, new Stage(stage), (File)null);
/*     */   }
/*     */   
/*     */   public ProgressStatus(IProgress progress, IStage stage) {
/*  28 */     this(progress, stage, (File)null);
/*     */   }
/*     */   
/*     */   public ProgressStatus(IProgress progress, String stage, File saveHere) {
/*  32 */     this(progress, stage, saveHere, true);
/*     */   }
/*     */   
/*     */   public ProgressStatus(IProgress progress, IStage stage, File saveHere) {
/*  36 */     this(progress, stage, saveHere, false);
/*     */   }
/*     */   
/*     */   public ProgressStatus(IProgress progress, String stage, boolean rejectEmpty) {
/*  40 */     this(progress, new Stage(stage), rejectEmpty);
/*     */   }
/*     */   
/*     */   public ProgressStatus(IProgress progress, IStage stage, boolean rejectEmpty) {
/*  44 */     this(progress, stage, (File)null, rejectEmpty);
/*     */   }
/*     */   
/*     */   public ProgressStatus(IProgress progress, String stage, File saveHere, boolean rejectEmpty) {
/*  48 */     this(progress, new Stage(stage), saveHere, rejectEmpty);
/*     */   }
/*     */   
/*     */   public ProgressStatus(IProgress progress, IStage stage, File saveHere, boolean rejectEmpty) {
/*  52 */     super(rejectEmpty, saveHere);
/*  53 */     this.stage = (IStage)Args.requireNonNull(stage, "stage is null");
/*  54 */     this.progress = (IProgress)Args.requireNonNull(progress, "progress is null");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onStepStart(long total) throws InterruptedException {
/*  59 */     this.total = total;
/*  60 */     this.increment = 1L;
/*  61 */     if (total > 0L) {
/*  62 */       this.progress.begin(this.stage, 100);
/*     */     } else {
/*  64 */       this.progress.begin(this.stage);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void onStepEnd() throws InterruptedException {
/*  69 */     this.progress.end();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onStepFail(Throwable e) {
/*  75 */     super.onStepFail(e);
/*  76 */     Throwables.quietly(this::onStepEnd);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onStepStatus(long written) throws InterruptedException {
/*  81 */     if (this.total > 0L) {
/*  82 */       handlePositive(written);
/*     */     } else {
/*  84 */       handleNegative(written);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleNegative(long written) throws InterruptedException {
/*  89 */     long now = System.currentTimeMillis();
/*  90 */     if (now - this.increment > 2000L) {
/*  91 */       this.progress.info("Baixados " + Sizes.defaultFormat(written), new Object[0]);
/*  92 */       this.increment = now;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handlePositive(long written) throws InterruptedException {
/*  97 */     float percent = 100.0F * (float)written / (float)this.total;
/*  98 */     if (percent >= (float)this.increment) {
/*  99 */       this.progress.step("Baixados %d%%", new Object[] { Long.valueOf(this.increment++) });
/*     */     }
/* 101 */     if ((float)this.increment <= percent) {
/* 102 */       long diff = (long)(percent - (float)this.increment + 1.0F);
/* 103 */       this.progress.skip(diff);
/* 104 */       this.increment += diff;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/imp/ProgressStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */