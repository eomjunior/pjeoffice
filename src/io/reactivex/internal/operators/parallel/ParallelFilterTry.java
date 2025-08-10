/*     */ package io.reactivex.internal.operators.parallel;
/*     */ 
/*     */ import io.reactivex.exceptions.CompositeException;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.BiFunction;
/*     */ import io.reactivex.functions.Predicate;
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
/*     */ public final class ParallelFilterTry<T>
/*     */   extends ParallelFlowable<T>
/*     */ {
/*     */   final ParallelFlowable<T> source;
/*     */   final Predicate<? super T> predicate;
/*     */   final BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler;
/*     */   
/*     */   public ParallelFilterTry(ParallelFlowable<T> source, Predicate<? super T> predicate, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler) {
/*  41 */     this.source = source;
/*  42 */     this.predicate = predicate;
/*  43 */     this.errorHandler = errorHandler;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribe(Subscriber<? super T>[] subscribers) {
/*  48 */     if (!validate((Subscriber[])subscribers)) {
/*     */       return;
/*     */     }
/*     */     
/*  52 */     int n = subscribers.length;
/*     */     
/*  54 */     Subscriber[] arrayOfSubscriber = new Subscriber[n];
/*     */     
/*  56 */     for (int i = 0; i < n; i++) {
/*  57 */       Subscriber<? super T> a = subscribers[i];
/*  58 */       if (a instanceof ConditionalSubscriber) {
/*  59 */         arrayOfSubscriber[i] = (Subscriber)new ParallelFilterConditionalSubscriber<T>((ConditionalSubscriber<? super T>)a, this.predicate, this.errorHandler);
/*     */       } else {
/*  61 */         arrayOfSubscriber[i] = (Subscriber)new ParallelFilterSubscriber<T>(a, this.predicate, this.errorHandler);
/*     */       } 
/*     */     } 
/*     */     
/*  65 */     this.source.subscribe(arrayOfSubscriber);
/*     */   }
/*     */ 
/*     */   
/*     */   public int parallelism() {
/*  70 */     return this.source.parallelism();
/*     */   }
/*     */ 
/*     */   
/*     */   static abstract class BaseFilterSubscriber<T>
/*     */     implements ConditionalSubscriber<T>, Subscription
/*     */   {
/*     */     final Predicate<? super T> predicate;
/*     */     final BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler;
/*     */     Subscription upstream;
/*     */     boolean done;
/*     */     
/*     */     BaseFilterSubscriber(Predicate<? super T> predicate, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler) {
/*  83 */       this.predicate = predicate;
/*  84 */       this.errorHandler = errorHandler;
/*     */     }
/*     */ 
/*     */     
/*     */     public final void request(long n) {
/*  89 */       this.upstream.request(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public final void cancel() {
/*  94 */       this.upstream.cancel();
/*     */     }
/*     */ 
/*     */     
/*     */     public final void onNext(T t) {
/*  99 */       if (!tryOnNext(t) && !this.done)
/* 100 */         this.upstream.request(1L); 
/*     */     }
/*     */   }
/*     */   
/*     */   static final class ParallelFilterSubscriber<T>
/*     */     extends BaseFilterSubscriber<T>
/*     */   {
/*     */     final Subscriber<? super T> downstream;
/*     */     
/*     */     ParallelFilterSubscriber(Subscriber<? super T> actual, Predicate<? super T> predicate, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler) {
/* 110 */       super(predicate, errorHandler);
/* 111 */       this.downstream = actual;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 116 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 117 */         this.upstream = s;
/*     */         
/* 119 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryOnNext(T t) {
/* 125 */       if (!this.done) {
/* 126 */         boolean b; long retries = 0L;
/*     */ 
/*     */ 
/*     */         
/*     */         while (true) {
/*     */           try {
/* 132 */             b = this.predicate.test(t);
/* 133 */           } catch (Throwable ex) {
/* 134 */             ParallelFailureHandling h; Exceptions.throwIfFatal(ex);
/*     */ 
/*     */ 
/*     */             
/*     */             try {
/* 139 */               h = (ParallelFailureHandling)ObjectHelper.requireNonNull(this.errorHandler.apply(Long.valueOf(++retries), ex), "The errorHandler returned a null item");
/* 140 */             } catch (Throwable exc) {
/* 141 */               Exceptions.throwIfFatal(exc);
/* 142 */               cancel();
/* 143 */               onError((Throwable)new CompositeException(new Throwable[] { ex, exc }));
/* 144 */               return false;
/*     */             } 
/*     */             
/* 147 */             switch (h) {
/*     */               case RETRY:
/*     */                 continue;
/*     */               case SKIP:
/* 151 */                 return false;
/*     */               case STOP:
/* 153 */                 cancel();
/* 154 */                 onComplete();
/* 155 */                 return false;
/*     */             } 
/* 157 */             cancel();
/* 158 */             onError(ex);
/* 159 */             return false;
/*     */           } 
/*     */           break;
/*     */         } 
/* 163 */         if (b) {
/* 164 */           this.downstream.onNext(t);
/* 165 */           return true;
/*     */         } 
/* 167 */         return false;
/*     */       } 
/*     */       
/* 170 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 175 */       if (this.done) {
/* 176 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 179 */       this.done = true;
/* 180 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 185 */       if (!this.done) {
/* 186 */         this.done = true;
/* 187 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ParallelFilterConditionalSubscriber<T>
/*     */     extends BaseFilterSubscriber<T>
/*     */   {
/*     */     final ConditionalSubscriber<? super T> downstream;
/*     */     
/*     */     ParallelFilterConditionalSubscriber(ConditionalSubscriber<? super T> actual, Predicate<? super T> predicate, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler) {
/* 199 */       super(predicate, errorHandler);
/* 200 */       this.downstream = actual;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 205 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 206 */         this.upstream = s;
/*     */         
/* 208 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryOnNext(T t) {
/* 214 */       if (!this.done) {
/* 215 */         boolean b; long retries = 0L;
/*     */ 
/*     */ 
/*     */         
/*     */         while (true) {
/*     */           try {
/* 221 */             b = this.predicate.test(t);
/* 222 */           } catch (Throwable ex) {
/* 223 */             ParallelFailureHandling h; Exceptions.throwIfFatal(ex);
/*     */ 
/*     */ 
/*     */             
/*     */             try {
/* 228 */               h = (ParallelFailureHandling)ObjectHelper.requireNonNull(this.errorHandler.apply(Long.valueOf(++retries), ex), "The errorHandler returned a null item");
/* 229 */             } catch (Throwable exc) {
/* 230 */               Exceptions.throwIfFatal(exc);
/* 231 */               cancel();
/* 232 */               onError((Throwable)new CompositeException(new Throwable[] { ex, exc }));
/* 233 */               return false;
/*     */             } 
/*     */             
/* 236 */             switch (h) {
/*     */               case RETRY:
/*     */                 continue;
/*     */               case SKIP:
/* 240 */                 return false;
/*     */               case STOP:
/* 242 */                 cancel();
/* 243 */                 onComplete();
/* 244 */                 return false;
/*     */             } 
/* 246 */             cancel();
/* 247 */             onError(ex);
/* 248 */             return false;
/*     */           } 
/*     */           break;
/*     */         } 
/* 252 */         return (b && this.downstream.tryOnNext(t));
/*     */       } 
/*     */       
/* 255 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 260 */       if (this.done) {
/* 261 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 264 */       this.done = true;
/* 265 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 270 */       if (!this.done) {
/* 271 */         this.done = true;
/* 272 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/parallel/ParallelFilterTry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */