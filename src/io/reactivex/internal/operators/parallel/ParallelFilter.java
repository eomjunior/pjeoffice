/*     */ package io.reactivex.internal.operators.parallel;
/*     */ 
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Predicate;
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
/*     */ public final class ParallelFilter<T>
/*     */   extends ParallelFlowable<T>
/*     */ {
/*     */   final ParallelFlowable<T> source;
/*     */   final Predicate<? super T> predicate;
/*     */   
/*     */   public ParallelFilter(ParallelFlowable<T> source, Predicate<? super T> predicate) {
/*  37 */     this.source = source;
/*  38 */     this.predicate = predicate;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribe(Subscriber<? super T>[] subscribers) {
/*  43 */     if (!validate((Subscriber[])subscribers)) {
/*     */       return;
/*     */     }
/*     */     
/*  47 */     int n = subscribers.length;
/*     */     
/*  49 */     Subscriber[] arrayOfSubscriber = new Subscriber[n];
/*     */     
/*  51 */     for (int i = 0; i < n; i++) {
/*  52 */       Subscriber<? super T> a = subscribers[i];
/*  53 */       if (a instanceof ConditionalSubscriber) {
/*  54 */         arrayOfSubscriber[i] = (Subscriber)new ParallelFilterConditionalSubscriber<T>((ConditionalSubscriber<? super T>)a, this.predicate);
/*     */       } else {
/*  56 */         arrayOfSubscriber[i] = (Subscriber)new ParallelFilterSubscriber<T>(a, this.predicate);
/*     */       } 
/*     */     } 
/*     */     
/*  60 */     this.source.subscribe(arrayOfSubscriber);
/*     */   }
/*     */ 
/*     */   
/*     */   public int parallelism() {
/*  65 */     return this.source.parallelism();
/*     */   }
/*     */   
/*     */   static abstract class BaseFilterSubscriber<T>
/*     */     implements ConditionalSubscriber<T>, Subscription
/*     */   {
/*     */     final Predicate<? super T> predicate;
/*     */     Subscription upstream;
/*     */     boolean done;
/*     */     
/*     */     BaseFilterSubscriber(Predicate<? super T> predicate) {
/*  76 */       this.predicate = predicate;
/*     */     }
/*     */ 
/*     */     
/*     */     public final void request(long n) {
/*  81 */       this.upstream.request(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public final void cancel() {
/*  86 */       this.upstream.cancel();
/*     */     }
/*     */ 
/*     */     
/*     */     public final void onNext(T t) {
/*  91 */       if (!tryOnNext(t) && !this.done)
/*  92 */         this.upstream.request(1L); 
/*     */     }
/*     */   }
/*     */   
/*     */   static final class ParallelFilterSubscriber<T>
/*     */     extends BaseFilterSubscriber<T>
/*     */   {
/*     */     final Subscriber<? super T> downstream;
/*     */     
/*     */     ParallelFilterSubscriber(Subscriber<? super T> actual, Predicate<? super T> predicate) {
/* 102 */       super(predicate);
/* 103 */       this.downstream = actual;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 108 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 109 */         this.upstream = s;
/*     */         
/* 111 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryOnNext(T t) {
/* 117 */       if (!this.done) {
/*     */         boolean b;
/*     */         
/*     */         try {
/* 121 */           b = this.predicate.test(t);
/* 122 */         } catch (Throwable ex) {
/* 123 */           Exceptions.throwIfFatal(ex);
/* 124 */           cancel();
/* 125 */           onError(ex);
/* 126 */           return false;
/*     */         } 
/*     */         
/* 129 */         if (b) {
/* 130 */           this.downstream.onNext(t);
/* 131 */           return true;
/*     */         } 
/*     */       } 
/* 134 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 139 */       if (this.done) {
/* 140 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 143 */       this.done = true;
/* 144 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 149 */       if (!this.done) {
/* 150 */         this.done = true;
/* 151 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static final class ParallelFilterConditionalSubscriber<T>
/*     */     extends BaseFilterSubscriber<T> {
/*     */     final ConditionalSubscriber<? super T> downstream;
/*     */     
/*     */     ParallelFilterConditionalSubscriber(ConditionalSubscriber<? super T> actual, Predicate<? super T> predicate) {
/* 161 */       super(predicate);
/* 162 */       this.downstream = actual;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 167 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 168 */         this.upstream = s;
/*     */         
/* 170 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryOnNext(T t) {
/* 176 */       if (!this.done) {
/*     */         boolean b;
/*     */         
/*     */         try {
/* 180 */           b = this.predicate.test(t);
/* 181 */         } catch (Throwable ex) {
/* 182 */           Exceptions.throwIfFatal(ex);
/* 183 */           cancel();
/* 184 */           onError(ex);
/* 185 */           return false;
/*     */         } 
/*     */         
/* 188 */         if (b) {
/* 189 */           return this.downstream.tryOnNext(t);
/*     */         }
/*     */       } 
/* 192 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 197 */       if (this.done) {
/* 198 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 201 */       this.done = true;
/* 202 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 207 */       if (!this.done) {
/* 208 */         this.done = true;
/* 209 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/parallel/ParallelFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */