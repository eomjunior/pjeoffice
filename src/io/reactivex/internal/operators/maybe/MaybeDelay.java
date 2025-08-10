/*     */ package io.reactivex.internal.operators.maybe;
/*     */ 
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MaybeDelay<T>
/*     */   extends AbstractMaybeWithUpstream<T, T>
/*     */ {
/*     */   final long delay;
/*     */   final TimeUnit unit;
/*     */   final Scheduler scheduler;
/*     */   
/*     */   public MaybeDelay(MaybeSource<T> source, long delay, TimeUnit unit, Scheduler scheduler) {
/*  37 */     super(source);
/*  38 */     this.delay = delay;
/*  39 */     this.unit = unit;
/*  40 */     this.scheduler = scheduler;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/*  45 */     this.source.subscribe(new DelayMaybeObserver<T>(observer, this.delay, this.unit, this.scheduler));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class DelayMaybeObserver<T>
/*     */     extends AtomicReference<Disposable>
/*     */     implements MaybeObserver<T>, Disposable, Runnable
/*     */   {
/*     */     private static final long serialVersionUID = 5566860102500855068L;
/*     */     
/*     */     final MaybeObserver<? super T> downstream;
/*     */     
/*     */     final long delay;
/*     */     
/*     */     final TimeUnit unit;
/*     */     
/*     */     final Scheduler scheduler;
/*     */     
/*     */     T value;
/*     */     Throwable error;
/*     */     
/*     */     DelayMaybeObserver(MaybeObserver<? super T> actual, long delay, TimeUnit unit, Scheduler scheduler) {
/*  67 */       this.downstream = actual;
/*  68 */       this.delay = delay;
/*  69 */       this.unit = unit;
/*  70 */       this.scheduler = scheduler;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*  75 */       Throwable ex = this.error;
/*  76 */       if (ex != null) {
/*  77 */         this.downstream.onError(ex);
/*     */       } else {
/*  79 */         T v = this.value;
/*  80 */         if (v != null) {
/*  81 */           this.downstream.onSuccess(v);
/*     */         } else {
/*  83 */           this.downstream.onComplete();
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  90 */       DisposableHelper.dispose(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  95 */       return DisposableHelper.isDisposed(get());
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 100 */       if (DisposableHelper.setOnce(this, d)) {
/* 101 */         this.downstream.onSubscribe(this);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/* 107 */       this.value = value;
/* 108 */       schedule();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 113 */       this.error = e;
/* 114 */       schedule();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 119 */       schedule();
/*     */     }
/*     */     
/*     */     void schedule() {
/* 123 */       DisposableHelper.replace(this, this.scheduler.scheduleDirect(this, this.delay, this.unit));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeDelay.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */