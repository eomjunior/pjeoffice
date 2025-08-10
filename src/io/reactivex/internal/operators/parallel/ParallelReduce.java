/*     */ package io.reactivex.internal.operators.parallel;
/*     */ 
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.BiFunction;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.subscribers.DeferredScalarSubscriber;
/*     */ import io.reactivex.internal.subscriptions.EmptySubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.parallel.ParallelFlowable;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.Callable;
/*     */ import org.reactivestreams.Subscriber;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ParallelReduce<T, R>
/*     */   extends ParallelFlowable<R>
/*     */ {
/*     */   final ParallelFlowable<? extends T> source;
/*     */   final Callable<R> initialSupplier;
/*     */   final BiFunction<R, ? super T, R> reducer;
/*     */   
/*     */   public ParallelReduce(ParallelFlowable<? extends T> source, Callable<R> initialSupplier, BiFunction<R, ? super T, R> reducer) {
/*  43 */     this.source = source;
/*  44 */     this.initialSupplier = initialSupplier;
/*  45 */     this.reducer = reducer;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribe(Subscriber<? super R>[] subscribers) {
/*  50 */     if (!validate((Subscriber[])subscribers)) {
/*     */       return;
/*     */     }
/*     */     
/*  54 */     int n = subscribers.length;
/*     */     
/*  56 */     Subscriber[] arrayOfSubscriber = new Subscriber[n];
/*     */     
/*  58 */     for (int i = 0; i < n; i++) {
/*     */       R initialValue;
/*     */ 
/*     */       
/*     */       try {
/*  63 */         initialValue = (R)ObjectHelper.requireNonNull(this.initialSupplier.call(), "The initialSupplier returned a null value");
/*  64 */       } catch (Throwable ex) {
/*  65 */         Exceptions.throwIfFatal(ex);
/*  66 */         reportError((Subscriber<?>[])subscribers, ex);
/*     */         
/*     */         return;
/*     */       } 
/*  70 */       arrayOfSubscriber[i] = (Subscriber)new ParallelReduceSubscriber<T, R>(subscribers[i], initialValue, this.reducer);
/*     */     } 
/*     */     
/*  73 */     this.source.subscribe(arrayOfSubscriber);
/*     */   }
/*     */   
/*     */   void reportError(Subscriber<?>[] subscribers, Throwable ex) {
/*  77 */     for (Subscriber<?> s : subscribers) {
/*  78 */       EmptySubscription.error(ex, s);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int parallelism() {
/*  84 */     return this.source.parallelism();
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ParallelReduceSubscriber<T, R>
/*     */     extends DeferredScalarSubscriber<T, R>
/*     */   {
/*     */     private static final long serialVersionUID = 8200530050639449080L;
/*     */     
/*     */     final BiFunction<R, ? super T, R> reducer;
/*     */     R accumulator;
/*     */     boolean done;
/*     */     
/*     */     ParallelReduceSubscriber(Subscriber<? super R> subscriber, R initialValue, BiFunction<R, ? super T, R> reducer) {
/*  98 */       super(subscriber);
/*  99 */       this.accumulator = initialValue;
/* 100 */       this.reducer = reducer;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 105 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 106 */         this.upstream = s;
/*     */         
/* 108 */         this.downstream.onSubscribe((Subscription)this);
/*     */         
/* 110 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 116 */       if (!this.done) {
/*     */         R v;
/*     */         
/*     */         try {
/* 120 */           v = (R)ObjectHelper.requireNonNull(this.reducer.apply(this.accumulator, t), "The reducer returned a null value");
/* 121 */         } catch (Throwable ex) {
/* 122 */           Exceptions.throwIfFatal(ex);
/* 123 */           cancel();
/* 124 */           onError(ex);
/*     */           
/*     */           return;
/*     */         } 
/* 128 */         this.accumulator = v;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 134 */       if (this.done) {
/* 135 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 138 */       this.done = true;
/* 139 */       this.accumulator = null;
/* 140 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 145 */       if (!this.done) {
/* 146 */         this.done = true;
/*     */         
/* 148 */         R a = this.accumulator;
/* 149 */         this.accumulator = null;
/* 150 */         complete(a);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 156 */       super.cancel();
/* 157 */       this.upstream.cancel();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/parallel/ParallelReduce.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */