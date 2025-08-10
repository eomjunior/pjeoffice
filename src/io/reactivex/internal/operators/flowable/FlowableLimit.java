/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.internal.subscriptions.EmptySubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FlowableLimit<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final long n;
/*     */   
/*     */   public FlowableLimit(Flowable<T> source, long n) {
/*  35 */     super(source);
/*  36 */     this.n = n;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  41 */     this.source.subscribe(new LimitSubscriber<T>(s, this.n));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class LimitSubscriber<T>
/*     */     extends AtomicLong
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = 2288246011222124525L;
/*     */     
/*     */     final Subscriber<? super T> downstream;
/*     */     
/*     */     long remaining;
/*     */     Subscription upstream;
/*     */     
/*     */     LimitSubscriber(Subscriber<? super T> actual, long remaining) {
/*  57 */       this.downstream = actual;
/*  58 */       this.remaining = remaining;
/*  59 */       lazySet(remaining);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  64 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  65 */         if (this.remaining == 0L) {
/*  66 */           s.cancel();
/*  67 */           EmptySubscription.complete(this.downstream);
/*     */         } else {
/*  69 */           this.upstream = s;
/*  70 */           this.downstream.onSubscribe(this);
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  77 */       long r = this.remaining;
/*     */       
/*  79 */       this.remaining = --r;
/*  80 */       this.downstream.onNext(t);
/*  81 */       if (r > 0L && r == 0L) {
/*  82 */         this.upstream.cancel();
/*  83 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  90 */       if (this.remaining > 0L) {
/*  91 */         this.remaining = 0L;
/*  92 */         this.downstream.onError(t);
/*     */       } else {
/*  94 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 100 */       if (this.remaining > 0L) {
/* 101 */         this.remaining = 0L;
/* 102 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 108 */       if (SubscriptionHelper.validate(n)) {
/*     */         while (true) {
/* 110 */           long toRequest, r = get();
/* 111 */           if (r == 0L) {
/*     */             break;
/*     */           }
/*     */           
/* 115 */           if (r <= n) {
/* 116 */             toRequest = r;
/*     */           } else {
/* 118 */             toRequest = n;
/*     */           } 
/* 120 */           long u = r - toRequest;
/* 121 */           if (compareAndSet(r, u)) {
/* 122 */             this.upstream.request(toRequest);
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 131 */       this.upstream.cancel();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableLimit.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */