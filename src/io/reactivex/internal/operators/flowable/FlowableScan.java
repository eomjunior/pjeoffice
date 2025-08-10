/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.BiFunction;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
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
/*     */ public final class FlowableScan<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final BiFunction<T, T, T> accumulator;
/*     */   
/*     */   public FlowableScan(Flowable<T> source, BiFunction<T, T, T> accumulator) {
/*  28 */     super(source);
/*  29 */     this.accumulator = accumulator;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  34 */     this.source.subscribe(new ScanSubscriber<T>(s, this.accumulator));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ScanSubscriber<T>
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     final Subscriber<? super T> downstream;
/*     */     final BiFunction<T, T, T> accumulator;
/*     */     Subscription upstream;
/*     */     T value;
/*     */     boolean done;
/*     */     
/*     */     ScanSubscriber(Subscriber<? super T> actual, BiFunction<T, T, T> accumulator) {
/*  48 */       this.downstream = actual;
/*  49 */       this.accumulator = accumulator;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  54 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  55 */         this.upstream = s;
/*  56 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  62 */       if (this.done) {
/*     */         return;
/*     */       }
/*  65 */       Subscriber<? super T> a = this.downstream;
/*  66 */       T v = this.value;
/*  67 */       if (v == null) {
/*  68 */         this.value = t;
/*  69 */         a.onNext(t);
/*     */       } else {
/*     */         T u;
/*     */         
/*     */         try {
/*  74 */           u = (T)ObjectHelper.requireNonNull(this.accumulator.apply(v, t), "The value returned by the accumulator is null");
/*  75 */         } catch (Throwable e) {
/*  76 */           Exceptions.throwIfFatal(e);
/*  77 */           this.upstream.cancel();
/*  78 */           onError(e);
/*     */           
/*     */           return;
/*     */         } 
/*  82 */         this.value = u;
/*  83 */         a.onNext(u);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  89 */       if (this.done) {
/*  90 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/*  93 */       this.done = true;
/*  94 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  99 */       if (this.done) {
/*     */         return;
/*     */       }
/* 102 */       this.done = true;
/* 103 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 108 */       this.upstream.request(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 113 */       this.upstream.cancel();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableScan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */