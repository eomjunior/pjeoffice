/*     */ package io.reactivex.internal.operators.single;
/*     */ 
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.Single;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.SingleSource;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
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
/*     */ public final class SingleTimeout<T>
/*     */   extends Single<T>
/*     */ {
/*     */   final SingleSource<T> source;
/*     */   final long timeout;
/*     */   final TimeUnit unit;
/*     */   final Scheduler scheduler;
/*     */   final SingleSource<? extends T> other;
/*     */   
/*     */   public SingleTimeout(SingleSource<T> source, long timeout, TimeUnit unit, Scheduler scheduler, SingleSource<? extends T> other) {
/*  40 */     this.source = source;
/*  41 */     this.timeout = timeout;
/*  42 */     this.unit = unit;
/*  43 */     this.scheduler = scheduler;
/*  44 */     this.other = other;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void subscribeActual(SingleObserver<? super T> observer) {
/*  50 */     TimeoutMainObserver<T> parent = new TimeoutMainObserver<T>(observer, this.other, this.timeout, this.unit);
/*  51 */     observer.onSubscribe(parent);
/*     */     
/*  53 */     DisposableHelper.replace(parent.task, this.scheduler.scheduleDirect(parent, this.timeout, this.unit));
/*     */     
/*  55 */     this.source.subscribe(parent);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class TimeoutMainObserver<T>
/*     */     extends AtomicReference<Disposable>
/*     */     implements SingleObserver<T>, Runnable, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 37497744973048446L;
/*     */     
/*     */     final SingleObserver<? super T> downstream;
/*     */     
/*     */     final AtomicReference<Disposable> task;
/*     */     
/*     */     final TimeoutFallbackObserver<T> fallback;
/*     */     SingleSource<? extends T> other;
/*     */     final long timeout;
/*     */     final TimeUnit unit;
/*     */     
/*     */     static final class TimeoutFallbackObserver<T>
/*     */       extends AtomicReference<Disposable>
/*     */       implements SingleObserver<T>
/*     */     {
/*     */       private static final long serialVersionUID = 2071387740092105509L;
/*     */       final SingleObserver<? super T> downstream;
/*     */       
/*     */       TimeoutFallbackObserver(SingleObserver<? super T> downstream) {
/*  82 */         this.downstream = downstream;
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/*  87 */         DisposableHelper.setOnce(this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSuccess(T t) {
/*  92 */         this.downstream.onSuccess(t);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/*  97 */         this.downstream.onError(e);
/*     */       }
/*     */     }
/*     */     
/*     */     TimeoutMainObserver(SingleObserver<? super T> actual, SingleSource<? extends T> other, long timeout, TimeUnit unit) {
/* 102 */       this.downstream = actual;
/* 103 */       this.other = other;
/* 104 */       this.timeout = timeout;
/* 105 */       this.unit = unit;
/* 106 */       this.task = new AtomicReference<Disposable>();
/* 107 */       if (other != null) {
/* 108 */         this.fallback = new TimeoutFallbackObserver<T>(actual);
/*     */       } else {
/* 110 */         this.fallback = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 116 */       Disposable d = get();
/* 117 */       if (d != DisposableHelper.DISPOSED && compareAndSet(d, (Disposable)DisposableHelper.DISPOSED)) {
/* 118 */         if (d != null) {
/* 119 */           d.dispose();
/*     */         }
/* 121 */         SingleSource<? extends T> other = this.other;
/* 122 */         if (other == null) {
/* 123 */           this.downstream.onError(new TimeoutException(ExceptionHelper.timeoutMessage(this.timeout, this.unit)));
/*     */         } else {
/* 125 */           this.other = null;
/* 126 */           other.subscribe(this.fallback);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 133 */       DisposableHelper.setOnce(this, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(T t) {
/* 138 */       Disposable d = get();
/* 139 */       if (d != DisposableHelper.DISPOSED && compareAndSet(d, (Disposable)DisposableHelper.DISPOSED)) {
/* 140 */         DisposableHelper.dispose(this.task);
/* 141 */         this.downstream.onSuccess(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 147 */       Disposable d = get();
/* 148 */       if (d != DisposableHelper.DISPOSED && compareAndSet(d, (Disposable)DisposableHelper.DISPOSED)) {
/* 149 */         DisposableHelper.dispose(this.task);
/* 150 */         this.downstream.onError(e);
/*     */       } else {
/* 152 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 158 */       DisposableHelper.dispose(this);
/* 159 */       DisposableHelper.dispose(this.task);
/* 160 */       if (this.fallback != null) {
/* 161 */         DisposableHelper.dispose(this.fallback);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 167 */       return DisposableHelper.isDisposed(get());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleTimeout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */