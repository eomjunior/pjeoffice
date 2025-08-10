/*     */ package io.reactivex.internal.operators.completable;
/*     */ 
/*     */ import io.reactivex.Completable;
/*     */ import io.reactivex.CompletableObserver;
/*     */ import io.reactivex.CompletableSource;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ public final class CompletableDelay
/*     */   extends Completable
/*     */ {
/*     */   final CompletableSource source;
/*     */   final long delay;
/*     */   final TimeUnit unit;
/*     */   final Scheduler scheduler;
/*     */   final boolean delayError;
/*     */   
/*     */   public CompletableDelay(CompletableSource source, long delay, TimeUnit unit, Scheduler scheduler, boolean delayError) {
/*  36 */     this.source = source;
/*  37 */     this.delay = delay;
/*  38 */     this.unit = unit;
/*  39 */     this.scheduler = scheduler;
/*  40 */     this.delayError = delayError;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(CompletableObserver observer) {
/*  45 */     this.source.subscribe(new Delay(observer, this.delay, this.unit, this.scheduler, this.delayError));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class Delay
/*     */     extends AtomicReference<Disposable>
/*     */     implements CompletableObserver, Runnable, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 465972761105851022L;
/*     */     
/*     */     final CompletableObserver downstream;
/*     */     
/*     */     final long delay;
/*     */     
/*     */     final TimeUnit unit;
/*     */     
/*     */     final Scheduler scheduler;
/*     */     final boolean delayError;
/*     */     Throwable error;
/*     */     
/*     */     Delay(CompletableObserver downstream, long delay, TimeUnit unit, Scheduler scheduler, boolean delayError) {
/*  66 */       this.downstream = downstream;
/*  67 */       this.delay = delay;
/*  68 */       this.unit = unit;
/*  69 */       this.scheduler = scheduler;
/*  70 */       this.delayError = delayError;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  75 */       if (DisposableHelper.setOnce(this, d)) {
/*  76 */         this.downstream.onSubscribe(this);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  82 */       DisposableHelper.replace(this, this.scheduler.scheduleDirect(this, this.delay, this.unit));
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*  87 */       this.error = e;
/*  88 */       DisposableHelper.replace(this, this.scheduler.scheduleDirect(this, this.delayError ? this.delay : 0L, this.unit));
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  93 */       DisposableHelper.dispose(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  98 */       return DisposableHelper.isDisposed(get());
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 103 */       Throwable e = this.error;
/* 104 */       this.error = null;
/* 105 */       if (e != null) {
/* 106 */         this.downstream.onError(e);
/*     */       } else {
/* 108 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableDelay.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */