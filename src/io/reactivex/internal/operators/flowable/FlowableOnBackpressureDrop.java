/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Consumer;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicLong;
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
/*     */ public final class FlowableOnBackpressureDrop<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */   implements Consumer<T>
/*     */ {
/*     */   final Consumer<? super T> onDrop;
/*     */   
/*     */   public FlowableOnBackpressureDrop(Flowable<T> source) {
/*  32 */     super(source);
/*  33 */     this.onDrop = this;
/*     */   }
/*     */   
/*     */   public FlowableOnBackpressureDrop(Flowable<T> source, Consumer<? super T> onDrop) {
/*  37 */     super(source);
/*  38 */     this.onDrop = onDrop;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void accept(T t) {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  48 */     this.source.subscribe(new BackpressureDropSubscriber<T>(s, this.onDrop));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class BackpressureDropSubscriber<T>
/*     */     extends AtomicLong
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = -6246093802440953054L;
/*     */     
/*     */     final Subscriber<? super T> downstream;
/*     */     final Consumer<? super T> onDrop;
/*     */     Subscription upstream;
/*     */     boolean done;
/*     */     
/*     */     BackpressureDropSubscriber(Subscriber<? super T> actual, Consumer<? super T> onDrop) {
/*  64 */       this.downstream = actual;
/*  65 */       this.onDrop = onDrop;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  70 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  71 */         this.upstream = s;
/*  72 */         this.downstream.onSubscribe(this);
/*  73 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  79 */       if (this.done) {
/*     */         return;
/*     */       }
/*  82 */       long r = get();
/*  83 */       if (r != 0L) {
/*  84 */         this.downstream.onNext(t);
/*  85 */         BackpressureHelper.produced(this, 1L);
/*     */       } else {
/*     */         try {
/*  88 */           this.onDrop.accept(t);
/*  89 */         } catch (Throwable e) {
/*  90 */           Exceptions.throwIfFatal(e);
/*  91 */           cancel();
/*  92 */           onError(e);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  99 */       if (this.done) {
/* 100 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 103 */       this.done = true;
/* 104 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 109 */       if (this.done) {
/*     */         return;
/*     */       }
/* 112 */       this.done = true;
/* 113 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 118 */       if (SubscriptionHelper.validate(n)) {
/* 119 */         BackpressureHelper.add(this, n);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 125 */       this.upstream.cancel();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableOnBackpressureDrop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */