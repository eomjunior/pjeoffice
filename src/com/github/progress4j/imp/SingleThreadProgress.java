/*     */ package com.github.progress4j.imp;
/*     */ 
/*     */ import com.github.progress4j.IProgress;
/*     */ import com.github.progress4j.IStage;
/*     */ import com.github.progress4j.IStageEvent;
/*     */ import com.github.progress4j.IState;
/*     */ import com.github.progress4j.IStepEvent;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import io.reactivex.Observable;
/*     */ import java.util.function.Consumer;
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
/*     */ public class SingleThreadProgress
/*     */   extends ProgressWrapper
/*     */ {
/*     */   private final Thread progressOwner;
/*     */   
/*     */   public static IProgress wrap(IProgress progress) {
/*  44 */     return new SingleThreadProgress(progress);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private SingleThreadProgress(IProgress progress) {
/*  50 */     this(progress, Thread.currentThread());
/*     */   }
/*     */   
/*     */   private SingleThreadProgress(IProgress progress, Thread progressOwner) {
/*  54 */     super(progress);
/*  55 */     this.progressOwner = (Thread)Args.requireNonNull(progressOwner, "progressOwner is null");
/*     */   }
/*     */   
/*     */   protected final boolean isOwner() {
/*  59 */     return (Thread.currentThread() == this.progressOwner);
/*     */   }
/*     */   
/*     */   private void checkOwner() {
/*  63 */     if (!isOwner()) {
/*  64 */       throw new IllegalStateException("Unabled to use this progress instance on this thread. Create your own");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final void begin(IStage stage) throws InterruptedException {
/*  70 */     if (isOwner()) {
/*  71 */       this.progress.begin(stage);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final void begin(IStage stage, int total) throws InterruptedException {
/*  77 */     if (isOwner()) {
/*  78 */       this.progress.begin(stage, total);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final void step(String mensagem, Object... params) throws InterruptedException {
/*  84 */     if (isOwner()) {
/*  85 */       this.progress.step(mensagem, params);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final void info(String mensagem, Object... params) throws InterruptedException {
/*  91 */     if (isOwner()) {
/*  92 */       this.progress.info(mensagem, params);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final void end() throws InterruptedException {
/*  98 */     if (isOwner()) {
/*  99 */       this.progress.end();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final <T extends Throwable> T abort(T e) {
/* 105 */     checkOwner();
/* 106 */     return (T)this.progress.abort((Throwable)e);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isClosed() {
/* 111 */     checkOwner();
/* 112 */     return this.progress.isClosed();
/*     */   }
/*     */ 
/*     */   
/*     */   public final IProgress stackTracer(Consumer<IState> consumer) {
/* 117 */     checkOwner();
/* 118 */     this.progress.stackTracer(consumer);
/* 119 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final IProgress reset() {
/* 124 */     checkOwner();
/* 125 */     this.progress.reset();
/* 126 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Observable<IStepEvent> stepObservable() {
/* 131 */     checkOwner();
/* 132 */     return this.progress.stepObservable();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Observable<IStageEvent> stageObservable() {
/* 137 */     checkOwner();
/* 138 */     return this.progress.stageObservable();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Observable<IProgress> disposeObservable() {
/* 143 */     checkOwner();
/* 144 */     return this.progress.disposeObservable();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void dispose() {
/* 149 */     checkOwner();
/* 150 */     this.progress.dispose();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Throwable getAbortCause() {
/* 155 */     checkOwner();
/* 156 */     return this.progress.getAbortCause();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/imp/SingleThreadProgress.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */