/*     */ package com.github.progress4j.imp;
/*     */ 
/*     */ import com.github.progress4j.IProgress;
/*     */ import com.github.progress4j.IProgressView;
/*     */ import com.github.progress4j.IStage;
/*     */ import com.github.progress4j.IStageEvent;
/*     */ import com.github.progress4j.IState;
/*     */ import com.github.progress4j.IStepEvent;
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.subjects.BehaviorSubject;
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
/*     */ public enum ProgressIdle
/*     */   implements IProgressView
/*     */ {
/*  43 */   INSTANCE;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile boolean interrupted = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   private Throwable abortCause = null; private BehaviorSubject<IStepEvent> stepSubject;
/*     */   
/*     */   ProgressIdle() {
/*  56 */     reset();
/*     */   }
/*     */   private BehaviorSubject<IStageEvent> stageSubject; private BehaviorSubject<IProgress> disposeSubject;
/*     */   
/*     */   public final String getName() {
/*  61 */     return INSTANCE.name();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void begin(String stage) throws InterruptedException {
/*  66 */     checkInterrupted();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void begin(String stage, int total) throws InterruptedException {
/*  71 */     checkInterrupted();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void begin(IStage stage) throws InterruptedException {
/*  76 */     checkInterrupted();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void begin(IStage stage, int total) throws InterruptedException {
/*  81 */     checkInterrupted();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void step(String message, Object... args) throws InterruptedException {
/*  86 */     checkInterrupted();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void skip(long steps) throws InterruptedException {
/*  91 */     checkInterrupted();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void info(String message, Object... args) throws InterruptedException {
/*  96 */     checkInterrupted();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void end() throws InterruptedException {
/* 101 */     checkInterrupted();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void interrupt() {
/* 106 */     this.interrupted = true;
/*     */   }
/*     */   
/*     */   private void checkInterrupted() throws InterruptedException {
/* 110 */     if (this.interrupted) {
/* 111 */       Thread.currentThread().interrupt();
/* 112 */       this.interrupted = false;
/*     */     } 
/* 114 */     if (Thread.currentThread().isInterrupted()) {
/* 115 */       throw (InterruptedException)abort(new InterruptedException("A thread foi interrompida!"));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final <T extends Throwable> T abort(T exception) {
/* 121 */     this.abortCause = (Throwable)exception;
/* 122 */     return exception;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Throwable getAbortCause() {
/* 127 */     return this.abortCause;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isClosed() {
/* 132 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Observable<IStepEvent> stepObservable() {
/* 137 */     return (Observable<IStepEvent>)this.stepSubject;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Observable<IStageEvent> stageObservable() {
/* 142 */     return (Observable<IStageEvent>)this.stageSubject;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Observable<IProgress> disposeObservable() {
/* 147 */     return (Observable<IProgress>)this.disposeSubject;
/*     */   }
/*     */ 
/*     */   
/*     */   public final IProgress stackTracer(Consumer<IState> consumer) {
/* 152 */     return (IProgress)this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void dispose() {
/* 157 */     this.disposeSubject.onNext(this);
/* 158 */     complete();
/* 159 */     reset();
/*     */   }
/*     */   
/*     */   private void complete() {
/*     */     try {
/* 164 */       this.stepSubject.onComplete();
/*     */     } finally {
/*     */       try {
/* 167 */         this.stageSubject.onComplete();
/*     */       } finally {
/* 169 */         this.disposeSubject.onComplete();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void cancelCode(Runnable code) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void display() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void undisplay() {}
/*     */ 
/*     */   
/*     */   public IProgressView reset() {
/* 188 */     this.stepSubject = BehaviorSubject.create();
/* 189 */     this.stageSubject = BehaviorSubject.create();
/* 190 */     this.disposeSubject = BehaviorSubject.create();
/* 191 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void throwIfInterrupted() throws InterruptedException {
/* 196 */     Throwable cause = getAbortCause();
/* 197 */     if (cause instanceof InterruptedException) {
/* 198 */       throw (InterruptedException)cause;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void cancel() {
/* 204 */     interrupt();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFrom(Thread thread) {
/* 209 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/imp/ProgressIdle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */