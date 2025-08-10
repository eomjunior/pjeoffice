/*     */ package io.reactivex.internal.subscribers;
/*     */ 
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
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
/*     */ import org.reactivestreams.Subscription;
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
/*     */ public final class FutureSubscriber<T>
/*     */   extends CountDownLatch
/*     */   implements FlowableSubscriber<T>, Future<T>, Subscription
/*     */ {
/*     */   T value;
/*     */   Throwable error;
/*     */   final AtomicReference<Subscription> upstream;
/*     */   
/*     */   public FutureSubscriber() {
/*  44 */     super(1);
/*  45 */     this.upstream = new AtomicReference<Subscription>();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean cancel(boolean mayInterruptIfRunning) {
/*     */     while (true) {
/*  51 */       Subscription a = this.upstream.get();
/*  52 */       if (a == this || a == SubscriptionHelper.CANCELLED) {
/*  53 */         return false;
/*     */       }
/*     */       
/*  56 */       if (this.upstream.compareAndSet(a, SubscriptionHelper.CANCELLED)) {
/*  57 */         if (a != null) {
/*  58 */           a.cancel();
/*     */         }
/*  60 */         countDown();
/*  61 */         return true;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCancelled() {
/*  68 */     return (this.upstream.get() == SubscriptionHelper.CANCELLED);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDone() {
/*  73 */     return (getCount() == 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   public T get() throws InterruptedException, ExecutionException {
/*  78 */     if (getCount() != 0L) {
/*  79 */       BlockingHelper.verifyNonBlocking();
/*  80 */       await();
/*     */     } 
/*     */     
/*  83 */     if (isCancelled()) {
/*  84 */       throw new CancellationException();
/*     */     }
/*  86 */     Throwable ex = this.error;
/*  87 */     if (ex != null) {
/*  88 */       throw new ExecutionException(ex);
/*     */     }
/*  90 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/*  95 */     if (getCount() != 0L) {
/*  96 */       BlockingHelper.verifyNonBlocking();
/*  97 */       if (!await(timeout, unit)) {
/*  98 */         throw new TimeoutException(ExceptionHelper.timeoutMessage(timeout, unit));
/*     */       }
/*     */     } 
/*     */     
/* 102 */     if (isCancelled()) {
/* 103 */       throw new CancellationException();
/*     */     }
/*     */     
/* 106 */     Throwable ex = this.error;
/* 107 */     if (ex != null) {
/* 108 */       throw new ExecutionException(ex);
/*     */     }
/* 110 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSubscribe(Subscription s) {
/* 115 */     SubscriptionHelper.setOnce(this.upstream, s, Long.MAX_VALUE);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNext(T t) {
/* 120 */     if (this.value != null) {
/* 121 */       ((Subscription)this.upstream.get()).cancel();
/* 122 */       onError(new IndexOutOfBoundsException("More than one element received"));
/*     */       return;
/*     */     } 
/* 125 */     this.value = t;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onError(Throwable t) {
/*     */     while (true) {
/* 131 */       Subscription a = this.upstream.get();
/* 132 */       if (a == this || a == SubscriptionHelper.CANCELLED) {
/* 133 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 136 */       this.error = t;
/* 137 */       if (this.upstream.compareAndSet(a, this)) {
/* 138 */         countDown();
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onComplete() {
/* 146 */     if (this.value == null) {
/* 147 */       onError(new NoSuchElementException("The source is empty"));
/*     */       return;
/*     */     } 
/*     */     while (true) {
/* 151 */       Subscription a = this.upstream.get();
/* 152 */       if (a == this || a == SubscriptionHelper.CANCELLED) {
/*     */         return;
/*     */       }
/* 155 */       if (this.upstream.compareAndSet(a, this)) {
/* 156 */         countDown();
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void cancel() {}
/*     */   
/*     */   public void request(long n) {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/subscribers/FutureSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */