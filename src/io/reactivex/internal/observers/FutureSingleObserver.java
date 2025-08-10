/*     */ package io.reactivex.internal.observers;
/*     */ 
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.util.BlockingHelper;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.CancellationException;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
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
/*     */ 
/*     */ public final class FutureSingleObserver<T>
/*     */   extends CountDownLatch
/*     */   implements SingleObserver<T>, Future<T>, Disposable
/*     */ {
/*     */   T value;
/*     */   Throwable error;
/*     */   final AtomicReference<Disposable> upstream;
/*     */   
/*     */   public FutureSingleObserver() {
/*  42 */     super(1);
/*  43 */     this.upstream = new AtomicReference<Disposable>();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean cancel(boolean mayInterruptIfRunning) {
/*     */     while (true) {
/*  49 */       Disposable a = this.upstream.get();
/*  50 */       if (a == this || a == DisposableHelper.DISPOSED) {
/*  51 */         return false;
/*     */       }
/*     */       
/*  54 */       if (this.upstream.compareAndSet(a, DisposableHelper.DISPOSED)) {
/*  55 */         if (a != null) {
/*  56 */           a.dispose();
/*     */         }
/*  58 */         countDown();
/*  59 */         return true;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCancelled() {
/*  66 */     return DisposableHelper.isDisposed(this.upstream.get());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDone() {
/*  71 */     return (getCount() == 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   public T get() throws InterruptedException, ExecutionException {
/*  76 */     if (getCount() != 0L) {
/*  77 */       BlockingHelper.verifyNonBlocking();
/*  78 */       await();
/*     */     } 
/*     */     
/*  81 */     if (isCancelled()) {
/*  82 */       throw new CancellationException();
/*     */     }
/*  84 */     Throwable ex = this.error;
/*  85 */     if (ex != null) {
/*  86 */       throw new ExecutionException(ex);
/*     */     }
/*  88 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/*  93 */     if (getCount() != 0L) {
/*  94 */       BlockingHelper.verifyNonBlocking();
/*  95 */       if (!await(timeout, unit)) {
/*  96 */         throw new TimeoutException(ExceptionHelper.timeoutMessage(timeout, unit));
/*     */       }
/*     */     } 
/*     */     
/* 100 */     if (isCancelled()) {
/* 101 */       throw new CancellationException();
/*     */     }
/*     */     
/* 104 */     Throwable ex = this.error;
/* 105 */     if (ex != null) {
/* 106 */       throw new ExecutionException(ex);
/*     */     }
/* 108 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSubscribe(Disposable d) {
/* 113 */     DisposableHelper.setOnce(this.upstream, d);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSuccess(T t) {
/* 118 */     Disposable a = this.upstream.get();
/* 119 */     if (a == DisposableHelper.DISPOSED) {
/*     */       return;
/*     */     }
/* 122 */     this.value = t;
/* 123 */     this.upstream.compareAndSet(a, this);
/* 124 */     countDown();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onError(Throwable t) {
/*     */     while (true) {
/* 130 */       Disposable a = this.upstream.get();
/* 131 */       if (a == DisposableHelper.DISPOSED) {
/* 132 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 135 */       this.error = t;
/* 136 */       if (this.upstream.compareAndSet(a, this)) {
/* 137 */         countDown();
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void dispose() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDisposed() {
/* 150 */     return isDone();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/observers/FutureSingleObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */