/*     */ package io.reactivex.internal.operators.parallel;
/*     */ 
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.ConditionalSubscriber;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
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
/*     */ public final class ParallelMap<T, R>
/*     */   extends ParallelFlowable<R>
/*     */ {
/*     */   final ParallelFlowable<T> source;
/*     */   final Function<? super T, ? extends R> mapper;
/*     */   
/*     */   public ParallelMap(ParallelFlowable<T> source, Function<? super T, ? extends R> mapper) {
/*  40 */     this.source = source;
/*  41 */     this.mapper = mapper;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribe(Subscriber<? super R>[] subscribers) {
/*  46 */     if (!validate((Subscriber[])subscribers)) {
/*     */       return;
/*     */     }
/*     */     
/*  50 */     int n = subscribers.length;
/*     */     
/*  52 */     Subscriber[] arrayOfSubscriber = new Subscriber[n];
/*     */     
/*  54 */     for (int i = 0; i < n; i++) {
/*  55 */       Subscriber<? super R> a = subscribers[i];
/*  56 */       if (a instanceof ConditionalSubscriber) {
/*  57 */         arrayOfSubscriber[i] = (Subscriber)new ParallelMapConditionalSubscriber<T, R>((ConditionalSubscriber<? super R>)a, this.mapper);
/*     */       } else {
/*  59 */         arrayOfSubscriber[i] = (Subscriber)new ParallelMapSubscriber<T, R>(a, this.mapper);
/*     */       } 
/*     */     } 
/*     */     
/*  63 */     this.source.subscribe(arrayOfSubscriber);
/*     */   }
/*     */ 
/*     */   
/*     */   public int parallelism() {
/*  68 */     return this.source.parallelism();
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ParallelMapSubscriber<T, R>
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     final Subscriber<? super R> downstream;
/*     */     
/*     */     final Function<? super T, ? extends R> mapper;
/*     */     Subscription upstream;
/*     */     boolean done;
/*     */     
/*     */     ParallelMapSubscriber(Subscriber<? super R> actual, Function<? super T, ? extends R> mapper) {
/*  82 */       this.downstream = actual;
/*  83 */       this.mapper = mapper;
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/*  88 */       this.upstream.request(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/*  93 */       this.upstream.cancel();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  98 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  99 */         this.upstream = s;
/*     */         
/* 101 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/*     */       R v;
/* 107 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/* 113 */         v = (R)ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null value");
/* 114 */       } catch (Throwable ex) {
/* 115 */         Exceptions.throwIfFatal(ex);
/* 116 */         cancel();
/* 117 */         onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/* 121 */       this.downstream.onNext(v);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 126 */       if (this.done) {
/* 127 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 130 */       this.done = true;
/* 131 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 136 */       if (this.done) {
/*     */         return;
/*     */       }
/* 139 */       this.done = true;
/* 140 */       this.downstream.onComplete();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ParallelMapConditionalSubscriber<T, R>
/*     */     implements ConditionalSubscriber<T>, Subscription
/*     */   {
/*     */     final ConditionalSubscriber<? super R> downstream;
/*     */     
/*     */     final Function<? super T, ? extends R> mapper;
/*     */     Subscription upstream;
/*     */     boolean done;
/*     */     
/*     */     ParallelMapConditionalSubscriber(ConditionalSubscriber<? super R> actual, Function<? super T, ? extends R> mapper) {
/* 155 */       this.downstream = actual;
/* 156 */       this.mapper = mapper;
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 161 */       this.upstream.request(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 166 */       this.upstream.cancel();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 171 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 172 */         this.upstream = s;
/*     */         
/* 174 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/*     */       R v;
/* 180 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/* 186 */         v = (R)ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null value");
/* 187 */       } catch (Throwable ex) {
/* 188 */         Exceptions.throwIfFatal(ex);
/* 189 */         cancel();
/* 190 */         onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/* 194 */       this.downstream.onNext(v);
/*     */     }
/*     */     
/*     */     public boolean tryOnNext(T t) {
/*     */       R v;
/* 199 */       if (this.done) {
/* 200 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/* 205 */         v = (R)ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null value");
/* 206 */       } catch (Throwable ex) {
/* 207 */         Exceptions.throwIfFatal(ex);
/* 208 */         cancel();
/* 209 */         onError(ex);
/* 210 */         return false;
/*     */       } 
/*     */       
/* 213 */       return this.downstream.tryOnNext(v);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 218 */       if (this.done) {
/* 219 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 222 */       this.done = true;
/* 223 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 228 */       if (this.done) {
/*     */         return;
/*     */       }
/* 231 */       this.done = true;
/* 232 */       this.downstream.onComplete();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/parallel/ParallelMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */