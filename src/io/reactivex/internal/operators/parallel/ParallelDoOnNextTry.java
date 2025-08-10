/*     */ package io.reactivex.internal.operators.parallel;
/*     */ 
/*     */ import io.reactivex.exceptions.CompositeException;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.BiFunction;
/*     */ import io.reactivex.functions.Consumer;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.ConditionalSubscriber;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.parallel.ParallelFailureHandling;
/*     */ import io.reactivex.parallel.ParallelFlowable;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
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
/*     */ public final class ParallelDoOnNextTry<T>
/*     */   extends ParallelFlowable<T>
/*     */ {
/*     */   final ParallelFlowable<T> source;
/*     */   final Consumer<? super T> onNext;
/*     */   final BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler;
/*     */   
/*     */   public ParallelDoOnNextTry(ParallelFlowable<T> source, Consumer<? super T> onNext, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler) {
/*  43 */     this.source = source;
/*  44 */     this.onNext = onNext;
/*  45 */     this.errorHandler = errorHandler;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribe(Subscriber<? super T>[] subscribers) {
/*  50 */     if (!validate((Subscriber[])subscribers)) {
/*     */       return;
/*     */     }
/*     */     
/*  54 */     int n = subscribers.length;
/*     */     
/*  56 */     Subscriber[] arrayOfSubscriber = new Subscriber[n];
/*     */     
/*  58 */     for (int i = 0; i < n; i++) {
/*  59 */       Subscriber<? super T> a = subscribers[i];
/*  60 */       if (a instanceof ConditionalSubscriber) {
/*  61 */         arrayOfSubscriber[i] = (Subscriber)new ParallelDoOnNextConditionalSubscriber<T>((ConditionalSubscriber<? super T>)a, this.onNext, this.errorHandler);
/*     */       } else {
/*  63 */         arrayOfSubscriber[i] = (Subscriber)new ParallelDoOnNextSubscriber<T>(a, this.onNext, this.errorHandler);
/*     */       } 
/*     */     } 
/*     */     
/*  67 */     this.source.subscribe(arrayOfSubscriber);
/*     */   }
/*     */ 
/*     */   
/*     */   public int parallelism() {
/*  72 */     return this.source.parallelism();
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ParallelDoOnNextSubscriber<T>
/*     */     implements ConditionalSubscriber<T>, Subscription
/*     */   {
/*     */     final Subscriber<? super T> downstream;
/*     */     
/*     */     final Consumer<? super T> onNext;
/*     */     
/*     */     final BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler;
/*     */     
/*     */     Subscription upstream;
/*     */     boolean done;
/*     */     
/*     */     ParallelDoOnNextSubscriber(Subscriber<? super T> actual, Consumer<? super T> onNext, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler) {
/*  89 */       this.downstream = actual;
/*  90 */       this.onNext = onNext;
/*  91 */       this.errorHandler = errorHandler;
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/*  96 */       this.upstream.request(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 101 */       this.upstream.cancel();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 106 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 107 */         this.upstream = s;
/*     */         
/* 109 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 115 */       if (!tryOnNext(t)) {
/* 116 */         this.upstream.request(1L);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryOnNext(T t) {
/* 122 */       if (this.done) {
/* 123 */         return false;
/*     */       }
/* 125 */       long retries = 0L;
/*     */       
/*     */       while (true) {
/*     */         try {
/* 129 */           this.onNext.accept(t);
/* 130 */         } catch (Throwable ex) {
/* 131 */           ParallelFailureHandling h; Exceptions.throwIfFatal(ex);
/*     */ 
/*     */ 
/*     */           
/*     */           try {
/* 136 */             h = (ParallelFailureHandling)ObjectHelper.requireNonNull(this.errorHandler.apply(Long.valueOf(++retries), ex), "The errorHandler returned a null item");
/* 137 */           } catch (Throwable exc) {
/* 138 */             Exceptions.throwIfFatal(exc);
/* 139 */             cancel();
/* 140 */             onError((Throwable)new CompositeException(new Throwable[] { ex, exc }));
/* 141 */             return false;
/*     */           } 
/*     */           
/* 144 */           switch (h) {
/*     */             case RETRY:
/*     */               continue;
/*     */             case SKIP:
/* 148 */               return false;
/*     */             case STOP:
/* 150 */               cancel();
/* 151 */               onComplete();
/* 152 */               return false;
/*     */           } 
/* 154 */           cancel();
/* 155 */           onError(ex);
/* 156 */           return false;
/*     */         } 
/*     */         break;
/*     */       } 
/* 160 */       this.downstream.onNext(t);
/* 161 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 167 */       if (this.done) {
/* 168 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 171 */       this.done = true;
/* 172 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 177 */       if (this.done) {
/*     */         return;
/*     */       }
/* 180 */       this.done = true;
/* 181 */       this.downstream.onComplete();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ParallelDoOnNextConditionalSubscriber<T>
/*     */     implements ConditionalSubscriber<T>, Subscription
/*     */   {
/*     */     final ConditionalSubscriber<? super T> downstream;
/*     */     
/*     */     final Consumer<? super T> onNext;
/*     */     
/*     */     final BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler;
/*     */     
/*     */     Subscription upstream;
/*     */     
/*     */     boolean done;
/*     */     
/*     */     ParallelDoOnNextConditionalSubscriber(ConditionalSubscriber<? super T> actual, Consumer<? super T> onNext, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler) {
/* 200 */       this.downstream = actual;
/* 201 */       this.onNext = onNext;
/* 202 */       this.errorHandler = errorHandler;
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 207 */       this.upstream.request(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 212 */       this.upstream.cancel();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 217 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 218 */         this.upstream = s;
/*     */         
/* 220 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 226 */       if (!tryOnNext(t) && !this.done) {
/* 227 */         this.upstream.request(1L);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryOnNext(T t) {
/* 233 */       if (this.done) {
/* 234 */         return false;
/*     */       }
/* 236 */       long retries = 0L;
/*     */       
/*     */       while (true) {
/*     */         try {
/* 240 */           this.onNext.accept(t);
/* 241 */         } catch (Throwable ex) {
/* 242 */           ParallelFailureHandling h; Exceptions.throwIfFatal(ex);
/*     */ 
/*     */ 
/*     */           
/*     */           try {
/* 247 */             h = (ParallelFailureHandling)ObjectHelper.requireNonNull(this.errorHandler.apply(Long.valueOf(++retries), ex), "The errorHandler returned a null item");
/* 248 */           } catch (Throwable exc) {
/* 249 */             Exceptions.throwIfFatal(exc);
/* 250 */             cancel();
/* 251 */             onError((Throwable)new CompositeException(new Throwable[] { ex, exc }));
/* 252 */             return false;
/*     */           } 
/*     */           
/* 255 */           switch (h) {
/*     */             case RETRY:
/*     */               continue;
/*     */             case SKIP:
/* 259 */               return false;
/*     */             case STOP:
/* 261 */               cancel();
/* 262 */               onComplete();
/* 263 */               return false;
/*     */           } 
/* 265 */           cancel();
/* 266 */           onError(ex);
/* 267 */           return false;
/*     */         } 
/*     */         break;
/*     */       } 
/* 271 */       return this.downstream.tryOnNext(t);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 277 */       if (this.done) {
/* 278 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 281 */       this.done = true;
/* 282 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 287 */       if (this.done) {
/*     */         return;
/*     */       }
/* 290 */       this.done = true;
/* 291 */       this.downstream.onComplete();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/parallel/ParallelDoOnNextTry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */