/*     */ package io.reactivex.internal.operators.parallel;
/*     */ 
/*     */ import io.reactivex.exceptions.CompositeException;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.BiFunction;
/*     */ import io.reactivex.functions.Function;
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
/*     */ 
/*     */ public final class ParallelMapTry<T, R>
/*     */   extends ParallelFlowable<R>
/*     */ {
/*     */   final ParallelFlowable<T> source;
/*     */   final Function<? super T, ? extends R> mapper;
/*     */   final BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler;
/*     */   
/*     */   public ParallelMapTry(ParallelFlowable<T> source, Function<? super T, ? extends R> mapper, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler) {
/*  44 */     this.source = source;
/*  45 */     this.mapper = mapper;
/*  46 */     this.errorHandler = errorHandler;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribe(Subscriber<? super R>[] subscribers) {
/*  51 */     if (!validate((Subscriber[])subscribers)) {
/*     */       return;
/*     */     }
/*     */     
/*  55 */     int n = subscribers.length;
/*     */     
/*  57 */     Subscriber[] arrayOfSubscriber = new Subscriber[n];
/*     */     
/*  59 */     for (int i = 0; i < n; i++) {
/*  60 */       Subscriber<? super R> a = subscribers[i];
/*  61 */       if (a instanceof ConditionalSubscriber) {
/*  62 */         arrayOfSubscriber[i] = (Subscriber)new ParallelMapTryConditionalSubscriber<T, R>((ConditionalSubscriber<? super R>)a, this.mapper, this.errorHandler);
/*     */       } else {
/*  64 */         arrayOfSubscriber[i] = (Subscriber)new ParallelMapTrySubscriber<T, R>(a, this.mapper, this.errorHandler);
/*     */       } 
/*     */     } 
/*     */     
/*  68 */     this.source.subscribe(arrayOfSubscriber);
/*     */   }
/*     */ 
/*     */   
/*     */   public int parallelism() {
/*  73 */     return this.source.parallelism();
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ParallelMapTrySubscriber<T, R>
/*     */     implements ConditionalSubscriber<T>, Subscription
/*     */   {
/*     */     final Subscriber<? super R> downstream;
/*     */     
/*     */     final Function<? super T, ? extends R> mapper;
/*     */     
/*     */     final BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler;
/*     */     
/*     */     Subscription upstream;
/*     */     boolean done;
/*     */     
/*     */     ParallelMapTrySubscriber(Subscriber<? super R> actual, Function<? super T, ? extends R> mapper, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler) {
/*  90 */       this.downstream = actual;
/*  91 */       this.mapper = mapper;
/*  92 */       this.errorHandler = errorHandler;
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/*  97 */       this.upstream.request(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 102 */       this.upstream.cancel();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 107 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 108 */         this.upstream = s;
/*     */         
/* 110 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 116 */       if (!tryOnNext(t) && !this.done) {
/* 117 */         this.upstream.request(1L);
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean tryOnNext(T t) {
/*     */       R v;
/* 123 */       if (this.done) {
/* 124 */         return false;
/*     */       }
/* 126 */       long retries = 0L;
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/*     */         try {
/* 132 */           v = (R)ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null value");
/* 133 */         } catch (Throwable ex) {
/* 134 */           ParallelFailureHandling h; Exceptions.throwIfFatal(ex);
/*     */ 
/*     */ 
/*     */           
/*     */           try {
/* 139 */             h = (ParallelFailureHandling)ObjectHelper.requireNonNull(this.errorHandler.apply(Long.valueOf(++retries), ex), "The errorHandler returned a null item");
/* 140 */           } catch (Throwable exc) {
/* 141 */             Exceptions.throwIfFatal(exc);
/* 142 */             cancel();
/* 143 */             onError((Throwable)new CompositeException(new Throwable[] { ex, exc }));
/* 144 */             return false;
/*     */           } 
/*     */           
/* 147 */           switch (h) {
/*     */             case RETRY:
/*     */               continue;
/*     */             case SKIP:
/* 151 */               return false;
/*     */             case STOP:
/* 153 */               cancel();
/* 154 */               onComplete();
/* 155 */               return false;
/*     */           } 
/* 157 */           cancel();
/* 158 */           onError(ex);
/* 159 */           return false;
/*     */         } 
/*     */         break;
/*     */       } 
/* 163 */       this.downstream.onNext(v);
/* 164 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 170 */       if (this.done) {
/* 171 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 174 */       this.done = true;
/* 175 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 180 */       if (this.done) {
/*     */         return;
/*     */       }
/* 183 */       this.done = true;
/* 184 */       this.downstream.onComplete();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ParallelMapTryConditionalSubscriber<T, R>
/*     */     implements ConditionalSubscriber<T>, Subscription
/*     */   {
/*     */     final ConditionalSubscriber<? super R> downstream;
/*     */     
/*     */     final Function<? super T, ? extends R> mapper;
/*     */     
/*     */     final BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler;
/*     */     
/*     */     Subscription upstream;
/*     */     
/*     */     boolean done;
/*     */     
/*     */     ParallelMapTryConditionalSubscriber(ConditionalSubscriber<? super R> actual, Function<? super T, ? extends R> mapper, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler) {
/* 203 */       this.downstream = actual;
/* 204 */       this.mapper = mapper;
/* 205 */       this.errorHandler = errorHandler;
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 210 */       this.upstream.request(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 215 */       this.upstream.cancel();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 220 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 221 */         this.upstream = s;
/*     */         
/* 223 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 229 */       if (!tryOnNext(t) && !this.done) {
/* 230 */         this.upstream.request(1L);
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean tryOnNext(T t) {
/*     */       R v;
/* 236 */       if (this.done) {
/* 237 */         return false;
/*     */       }
/* 239 */       long retries = 0L;
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/*     */         try {
/* 245 */           v = (R)ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null value");
/* 246 */         } catch (Throwable ex) {
/* 247 */           ParallelFailureHandling h; Exceptions.throwIfFatal(ex);
/*     */ 
/*     */ 
/*     */           
/*     */           try {
/* 252 */             h = (ParallelFailureHandling)ObjectHelper.requireNonNull(this.errorHandler.apply(Long.valueOf(++retries), ex), "The errorHandler returned a null item");
/* 253 */           } catch (Throwable exc) {
/* 254 */             Exceptions.throwIfFatal(exc);
/* 255 */             cancel();
/* 256 */             onError((Throwable)new CompositeException(new Throwable[] { ex, exc }));
/* 257 */             return false;
/*     */           } 
/*     */           
/* 260 */           switch (h) {
/*     */             case RETRY:
/*     */               continue;
/*     */             case SKIP:
/* 264 */               return false;
/*     */             case STOP:
/* 266 */               cancel();
/* 267 */               onComplete();
/* 268 */               return false;
/*     */           } 
/* 270 */           cancel();
/* 271 */           onError(ex);
/* 272 */           return false;
/*     */         } 
/*     */         break;
/*     */       } 
/* 276 */       return this.downstream.tryOnNext(v);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 282 */       if (this.done) {
/* 283 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 286 */       this.done = true;
/* 287 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 292 */       if (this.done) {
/*     */         return;
/*     */       }
/* 295 */       this.done = true;
/* 296 */       this.downstream.onComplete();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/parallel/ParallelMapTry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */