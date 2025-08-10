/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.Maybe;
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.BiFunction;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.FuseToFlowable;
/*     */ import io.reactivex.internal.fuseable.HasUpstreamPublisher;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import org.reactivestreams.Publisher;
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
/*     */ public final class FlowableReduceMaybe<T>
/*     */   extends Maybe<T>
/*     */   implements HasUpstreamPublisher<T>, FuseToFlowable<T>
/*     */ {
/*     */   final Flowable<T> source;
/*     */   final BiFunction<T, T, T> reducer;
/*     */   
/*     */   public FlowableReduceMaybe(Flowable<T> source, BiFunction<T, T, T> reducer) {
/*  41 */     this.source = source;
/*  42 */     this.reducer = reducer;
/*     */   }
/*     */ 
/*     */   
/*     */   public Publisher<T> source() {
/*  47 */     return (Publisher<T>)this.source;
/*     */   }
/*     */ 
/*     */   
/*     */   public Flowable<T> fuseToFlowable() {
/*  52 */     return RxJavaPlugins.onAssembly(new FlowableReduce<T>(this.source, this.reducer));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/*  57 */     this.source.subscribe(new ReduceSubscriber<T>(observer, this.reducer));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ReduceSubscriber<T>
/*     */     implements FlowableSubscriber<T>, Disposable
/*     */   {
/*     */     final MaybeObserver<? super T> downstream;
/*     */     
/*     */     final BiFunction<T, T, T> reducer;
/*     */     T value;
/*     */     Subscription upstream;
/*     */     boolean done;
/*     */     
/*     */     ReduceSubscriber(MaybeObserver<? super T> actual, BiFunction<T, T, T> reducer) {
/*  72 */       this.downstream = actual;
/*  73 */       this.reducer = reducer;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  78 */       this.upstream.cancel();
/*  79 */       this.done = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  84 */       return this.done;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  89 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  90 */         this.upstream = s;
/*     */         
/*  92 */         this.downstream.onSubscribe(this);
/*     */         
/*  94 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 100 */       if (this.done) {
/*     */         return;
/*     */       }
/* 103 */       T v = this.value;
/* 104 */       if (v == null) {
/* 105 */         this.value = t;
/*     */       } else {
/*     */         try {
/* 108 */           this.value = (T)ObjectHelper.requireNonNull(this.reducer.apply(v, t), "The reducer returned a null value");
/* 109 */         } catch (Throwable ex) {
/* 110 */           Exceptions.throwIfFatal(ex);
/* 111 */           this.upstream.cancel();
/* 112 */           onError(ex);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 119 */       if (this.done) {
/* 120 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 123 */       this.done = true;
/* 124 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 129 */       if (this.done) {
/*     */         return;
/*     */       }
/* 132 */       this.done = true;
/* 133 */       T v = this.value;
/* 134 */       if (v != null) {
/*     */         
/* 136 */         this.downstream.onSuccess(v);
/*     */       } else {
/* 138 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableReduceMaybe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */