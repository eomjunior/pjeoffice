/*     */ package io.reactivex.internal.schedulers;
/*     */ 
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.functions.Functions;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.FutureTask;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ final class InstantPeriodicTask
/*     */   implements Callable<Void>, Disposable
/*     */ {
/*     */   final Runnable task;
/*     */   final AtomicReference<Future<?>> rest;
/*     */   final AtomicReference<Future<?>> first;
/*     */   final ExecutorService executor;
/*     */   Thread runner;
/*  41 */   static final FutureTask<Void> CANCELLED = new FutureTask<Void>(Functions.EMPTY_RUNNABLE, null);
/*     */ 
/*     */   
/*     */   InstantPeriodicTask(Runnable task, ExecutorService executor) {
/*  45 */     this.task = task;
/*  46 */     this.first = new AtomicReference<Future<?>>();
/*  47 */     this.rest = new AtomicReference<Future<?>>();
/*  48 */     this.executor = executor;
/*     */   }
/*     */ 
/*     */   
/*     */   public Void call() throws Exception {
/*  53 */     this.runner = Thread.currentThread();
/*     */     try {
/*  55 */       this.task.run();
/*  56 */       setRest(this.executor.submit(this));
/*  57 */       this.runner = null;
/*  58 */     } catch (Throwable ex) {
/*  59 */       this.runner = null;
/*  60 */       RxJavaPlugins.onError(ex);
/*     */     } 
/*  62 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/*  67 */     Future<?> current = this.first.getAndSet(CANCELLED);
/*  68 */     if (current != null && current != CANCELLED) {
/*  69 */       current.cancel((this.runner != Thread.currentThread()));
/*     */     }
/*  71 */     current = this.rest.getAndSet(CANCELLED);
/*  72 */     if (current != null && current != CANCELLED) {
/*  73 */       current.cancel((this.runner != Thread.currentThread()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDisposed() {
/*  79 */     return (this.first.get() == CANCELLED);
/*     */   }
/*     */   void setFirst(Future<?> f) {
/*     */     Future<?> current;
/*     */     do {
/*  84 */       current = this.first.get();
/*  85 */       if (current == CANCELLED) {
/*  86 */         f.cancel((this.runner != Thread.currentThread()));
/*     */         return;
/*     */       } 
/*  89 */     } while (!this.first.compareAndSet(current, f));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void setRest(Future<?> f) {
/*     */     Future<?> current;
/*     */     do {
/*  97 */       current = this.rest.get();
/*  98 */       if (current == CANCELLED) {
/*  99 */         f.cancel((this.runner != Thread.currentThread()));
/*     */         return;
/*     */       } 
/* 102 */     } while (!this.rest.compareAndSet(current, f));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/schedulers/InstantPeriodicTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */