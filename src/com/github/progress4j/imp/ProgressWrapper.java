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
/*     */ 
/*     */ 
/*     */ public class ProgressWrapper
/*     */   implements IProgress
/*     */ {
/*     */   protected final IProgress progress;
/*     */   
/*     */   protected ProgressWrapper(IProgress progress) {
/*  46 */     this.progress = (IProgress)Args.requireNonNull(progress, "progress is null");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  51 */     return this.progress.getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public void begin(String stage) throws InterruptedException {
/*  56 */     this.progress.begin(stage);
/*     */   }
/*     */ 
/*     */   
/*     */   public void begin(String stage, int total) throws InterruptedException {
/*  61 */     this.progress.begin(stage, total);
/*     */   }
/*     */ 
/*     */   
/*     */   public void begin(IStage stage) throws InterruptedException {
/*  66 */     this.progress.begin(stage);
/*     */   }
/*     */ 
/*     */   
/*     */   public void begin(IStage stage, int total) throws InterruptedException {
/*  71 */     this.progress.begin(stage, total);
/*     */   }
/*     */ 
/*     */   
/*     */   public void step(String mensagem, Object... params) throws InterruptedException {
/*  76 */     this.progress.step(mensagem, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public void info(String mensagem, Object... params) throws InterruptedException {
/*  81 */     this.progress.info(mensagem, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public void end() throws InterruptedException {
/*  86 */     this.progress.end();
/*     */   }
/*     */ 
/*     */   
/*     */   public void interrupt() {
/*  91 */     this.progress.interrupt();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends Throwable> T abort(T e) {
/*  96 */     return (T)this.progress.abort((Throwable)e);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isClosed() {
/* 101 */     return this.progress.isClosed();
/*     */   }
/*     */ 
/*     */   
/*     */   public IProgress stackTracer(Consumer<IState> consumer) {
/* 106 */     this.progress.stackTracer(consumer);
/* 107 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public IProgress reset() {
/* 112 */     this.progress.reset();
/* 113 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Observable<IStepEvent> stepObservable() {
/* 118 */     return this.progress.stepObservable();
/*     */   }
/*     */ 
/*     */   
/*     */   public Observable<IStageEvent> stageObservable() {
/* 123 */     return this.progress.stageObservable();
/*     */   }
/*     */ 
/*     */   
/*     */   public Observable<IProgress> disposeObservable() {
/* 128 */     return this.progress.disposeObservable();
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 133 */     this.progress.dispose();
/*     */   }
/*     */ 
/*     */   
/*     */   public Throwable getAbortCause() {
/* 138 */     return this.progress.getAbortCause();
/*     */   }
/*     */ 
/*     */   
/*     */   public void skip(long steps) throws InterruptedException {
/* 143 */     this.progress.skip(steps);
/*     */   }
/*     */ 
/*     */   
/*     */   public void throwIfInterrupted() throws InterruptedException {
/* 148 */     this.progress.throwIfInterrupted();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/imp/ProgressWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */