/*     */ package io.reactivex.internal.observers;
/*     */ 
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.util.BlockingHelper;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.NoSuchElementException;
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
/*     */ public final class FutureObserver<T>
/*     */   extends CountDownLatch
/*     */   implements Observer<T>, Future<T>, Disposable
/*     */ {
/*     */   T value;
/*     */   Throwable error;
/*     */   final AtomicReference<Disposable> upstream;
/*     */   
/*     */   public FutureObserver() {
/*  43 */     super(1);
/*  44 */     this.upstream = new AtomicReference<Disposable>();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean cancel(boolean mayInterruptIfRunning) {
/*     */     while (true) {
/*  50 */       Disposable a = this.upstream.get();
/*  51 */       if (a == this || a == DisposableHelper.DISPOSED) {
/*  52 */         return false;
/*     */       }
/*     */       
/*  55 */       if (this.upstream.compareAndSet(a, DisposableHelper.DISPOSED)) {
/*  56 */         if (a != null) {
/*  57 */           a.dispose();
/*     */         }
/*  59 */         countDown();
/*  60 */         return true;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCancelled() {
/*  67 */     return DisposableHelper.isDisposed(this.upstream.get());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDone() {
/*  72 */     return (getCount() == 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   public T get() throws InterruptedException, ExecutionException {
/*  77 */     if (getCount() != 0L) {
/*  78 */       BlockingHelper.verifyNonBlocking();
/*  79 */       await();
/*     */     } 
/*     */     
/*  82 */     if (isCancelled()) {
/*  83 */       throw new CancellationException();
/*     */     }
/*  85 */     Throwable ex = this.error;
/*  86 */     if (ex != null) {
/*  87 */       throw new ExecutionException(ex);
/*     */     }
/*  89 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/*  94 */     if (getCount() != 0L) {
/*  95 */       BlockingHelper.verifyNonBlocking();
/*  96 */       if (!await(timeout, unit)) {
/*  97 */         throw new TimeoutException(ExceptionHelper.timeoutMessage(timeout, unit));
/*     */       }
/*     */     } 
/*     */     
/* 101 */     if (isCancelled()) {
/* 102 */       throw new CancellationException();
/*     */     }
/*     */     
/* 105 */     Throwable ex = this.error;
/* 106 */     if (ex != null) {
/* 107 */       throw new ExecutionException(ex);
/*     */     }
/* 109 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSubscribe(Disposable d) {
/* 114 */     DisposableHelper.setOnce(this.upstream, d);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNext(T t) {
/* 119 */     if (this.value != null) {
/* 120 */       ((Disposable)this.upstream.get()).dispose();
/* 121 */       onError(new IndexOutOfBoundsException("More than one element received"));
/*     */       return;
/*     */     } 
/* 124 */     this.value = t;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onError(Throwable t) {
/* 129 */     if (this.error == null) {
/* 130 */       this.error = t;
/*     */       
/*     */       while (true) {
/* 133 */         Disposable a = this.upstream.get();
/* 134 */         if (a == this || a == DisposableHelper.DISPOSED) {
/* 135 */           RxJavaPlugins.onError(t);
/*     */           return;
/*     */         } 
/* 138 */         if (this.upstream.compareAndSet(a, this)) {
/* 139 */           countDown();
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/* 144 */     RxJavaPlugins.onError(t);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onComplete() {
/* 150 */     if (this.value == null) {
/* 151 */       onError(new NoSuchElementException("The source is empty"));
/*     */       return;
/*     */     } 
/*     */     while (true) {
/* 155 */       Disposable a = this.upstream.get();
/* 156 */       if (a == this || a == DisposableHelper.DISPOSED) {
/*     */         return;
/*     */       }
/* 159 */       if (this.upstream.compareAndSet(a, this)) {
/* 160 */         countDown();
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
/* 173 */     return isDone();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/observers/FutureObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */