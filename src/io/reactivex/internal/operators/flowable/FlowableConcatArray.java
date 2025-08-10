/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.CompositeException;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionArbiter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ public final class FlowableConcatArray<T>
/*     */   extends Flowable<T>
/*     */ {
/*     */   final Publisher<? extends T>[] sources;
/*     */   final boolean delayError;
/*     */   
/*     */   public FlowableConcatArray(Publisher<? extends T>[] sources, boolean delayError) {
/*  31 */     this.sources = sources;
/*  32 */     this.delayError = delayError;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  37 */     ConcatArraySubscriber<T> parent = new ConcatArraySubscriber<T>(this.sources, this.delayError, s);
/*  38 */     s.onSubscribe((Subscription)parent);
/*     */     
/*  40 */     parent.onComplete();
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ConcatArraySubscriber<T>
/*     */     extends SubscriptionArbiter
/*     */     implements FlowableSubscriber<T>
/*     */   {
/*     */     private static final long serialVersionUID = -8158322871608889516L;
/*     */     
/*     */     final Subscriber<? super T> downstream;
/*     */     
/*     */     final Publisher<? extends T>[] sources;
/*     */     
/*     */     final boolean delayError;
/*     */     
/*     */     final AtomicInteger wip;
/*     */     int index;
/*     */     List<Throwable> errors;
/*     */     long produced;
/*     */     
/*     */     ConcatArraySubscriber(Publisher<? extends T>[] sources, boolean delayError, Subscriber<? super T> downstream) {
/*  62 */       super(false);
/*  63 */       this.downstream = downstream;
/*  64 */       this.sources = sources;
/*  65 */       this.delayError = delayError;
/*  66 */       this.wip = new AtomicInteger();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  71 */       setSubscription(s);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  76 */       this.produced++;
/*  77 */       this.downstream.onNext(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  82 */       if (this.delayError) {
/*  83 */         List<Throwable> list = this.errors;
/*  84 */         if (list == null) {
/*  85 */           list = new ArrayList<Throwable>(this.sources.length - this.index + 1);
/*  86 */           this.errors = list;
/*     */         } 
/*  88 */         list.add(t);
/*  89 */         onComplete();
/*     */       } else {
/*  91 */         this.downstream.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  97 */       if (this.wip.getAndIncrement() == 0) {
/*  98 */         Publisher<? extends T>[] sources = this.sources;
/*  99 */         int n = sources.length;
/* 100 */         int i = this.index;
/*     */         
/*     */         while (true) {
/* 103 */           if (i == n) {
/* 104 */             List<Throwable> list = this.errors;
/* 105 */             if (list != null) {
/* 106 */               if (list.size() == 1) {
/* 107 */                 this.downstream.onError(list.get(0));
/*     */               } else {
/* 109 */                 this.downstream.onError((Throwable)new CompositeException(list));
/*     */               } 
/*     */             } else {
/* 112 */               this.downstream.onComplete();
/*     */             } 
/*     */             
/*     */             return;
/*     */           } 
/* 117 */           Publisher<? extends T> p = sources[i];
/*     */           
/* 119 */           if (p == null) {
/* 120 */             Throwable ex = new NullPointerException("A Publisher entry is null");
/* 121 */             if (this.delayError) {
/* 122 */               List<Throwable> list = this.errors;
/* 123 */               if (list == null) {
/* 124 */                 list = new ArrayList<Throwable>(n - i + 1);
/* 125 */                 this.errors = list;
/*     */               } 
/* 127 */               list.add(ex);
/* 128 */               i++;
/*     */               continue;
/*     */             } 
/* 131 */             this.downstream.onError(ex);
/*     */             
/*     */             return;
/*     */           } 
/* 135 */           long r = this.produced;
/* 136 */           if (r != 0L) {
/* 137 */             this.produced = 0L;
/* 138 */             produced(r);
/*     */           } 
/* 140 */           p.subscribe((Subscriber)this);
/*     */ 
/*     */           
/* 143 */           this.index = ++i;
/*     */           
/* 145 */           if (this.wip.decrementAndGet() == 0)
/*     */             break; 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableConcatArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */