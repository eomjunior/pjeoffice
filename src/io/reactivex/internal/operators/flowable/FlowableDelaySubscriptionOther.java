/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.reactivestreams.Publisher;
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
/*     */ public final class FlowableDelaySubscriptionOther<T, U>
/*     */   extends Flowable<T>
/*     */ {
/*     */   final Publisher<? extends T> main;
/*     */   final Publisher<U> other;
/*     */   
/*     */   public FlowableDelaySubscriptionOther(Publisher<? extends T> main, Publisher<U> other) {
/*  34 */     this.main = main;
/*  35 */     this.other = other;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Subscriber<? super T> child) {
/*  40 */     MainSubscriber<T> parent = new MainSubscriber<T>(child, this.main);
/*  41 */     child.onSubscribe(parent);
/*  42 */     this.other.subscribe((Subscriber)parent.other);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class MainSubscriber<T>
/*     */     extends AtomicLong
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = 2259811067697317255L;
/*     */     
/*     */     final Subscriber<? super T> downstream;
/*     */     final Publisher<? extends T> main;
/*     */     final OtherSubscriber other;
/*     */     final AtomicReference<Subscription> upstream;
/*     */     
/*     */     MainSubscriber(Subscriber<? super T> downstream, Publisher<? extends T> main) {
/*  58 */       this.downstream = downstream;
/*  59 */       this.main = main;
/*  60 */       this.other = new OtherSubscriber();
/*  61 */       this.upstream = new AtomicReference<Subscription>();
/*     */     }
/*     */     
/*     */     void next() {
/*  65 */       this.main.subscribe((Subscriber)this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  70 */       this.downstream.onNext(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  75 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  80 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/*  85 */       if (SubscriptionHelper.validate(n)) {
/*  86 */         SubscriptionHelper.deferredRequest(this.upstream, this, n);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/*  92 */       SubscriptionHelper.cancel(this.other);
/*  93 */       SubscriptionHelper.cancel(this.upstream);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  98 */       SubscriptionHelper.deferredSetOnce(this.upstream, this, s);
/*     */     }
/*     */     
/*     */     final class OtherSubscriber
/*     */       extends AtomicReference<Subscription>
/*     */       implements FlowableSubscriber<Object> {
/*     */       private static final long serialVersionUID = -3892798459447644106L;
/*     */       
/*     */       public void onSubscribe(Subscription s) {
/* 107 */         if (SubscriptionHelper.setOnce(this, s)) {
/* 108 */           s.request(Long.MAX_VALUE);
/*     */         }
/*     */       }
/*     */ 
/*     */       
/*     */       public void onNext(Object t) {
/* 114 */         Subscription s = get();
/* 115 */         if (s != SubscriptionHelper.CANCELLED) {
/* 116 */           lazySet((Subscription)SubscriptionHelper.CANCELLED);
/* 117 */           s.cancel();
/* 118 */           FlowableDelaySubscriptionOther.MainSubscriber.this.next();
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable t) {
/* 124 */         Subscription s = get();
/* 125 */         if (s != SubscriptionHelper.CANCELLED) {
/* 126 */           FlowableDelaySubscriptionOther.MainSubscriber.this.downstream.onError(t);
/*     */         } else {
/* 128 */           RxJavaPlugins.onError(t);
/*     */         } 
/*     */       }
/*     */       
/*     */       public void onComplete()
/*     */       {
/* 134 */         Subscription s = get();
/* 135 */         if (s != SubscriptionHelper.CANCELLED)
/* 136 */           FlowableDelaySubscriptionOther.MainSubscriber.this.next();  } } } final class OtherSubscriber extends AtomicReference<Subscription> implements FlowableSubscriber<Object> { public void onComplete() { Subscription s = get(); if (s != SubscriptionHelper.CANCELLED) FlowableDelaySubscriptionOther.MainSubscriber.this.next();  }
/*     */ 
/*     */     
/*     */     private static final long serialVersionUID = -3892798459447644106L;
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*     */       if (SubscriptionHelper.setOnce(this, s))
/*     */         s.request(Long.MAX_VALUE); 
/*     */     }
/*     */     
/*     */     public void onNext(Object t) {
/*     */       Subscription s = get();
/*     */       if (s != SubscriptionHelper.CANCELLED) {
/*     */         lazySet((Subscription)SubscriptionHelper.CANCELLED);
/*     */         s.cancel();
/*     */         FlowableDelaySubscriptionOther.MainSubscriber.this.next();
/*     */       } 
/*     */     }
/*     */     
/*     */     public void onError(Throwable t) {
/*     */       Subscription s = get();
/*     */       if (s != SubscriptionHelper.CANCELLED) {
/*     */         FlowableDelaySubscriptionOther.MainSubscriber.this.downstream.onError(t);
/*     */       } else {
/*     */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableDelaySubscriptionOther.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */