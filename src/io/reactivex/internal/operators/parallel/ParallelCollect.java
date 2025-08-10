/*     */ package io.reactivex.internal.operators.parallel;
/*     */ 
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.BiConsumer;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.subscribers.DeferredScalarSubscriber;
/*     */ import io.reactivex.internal.subscriptions.EmptySubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.parallel.ParallelFlowable;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.Callable;
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
/*     */ 
/*     */ public final class ParallelCollect<T, C>
/*     */   extends ParallelFlowable<C>
/*     */ {
/*     */   final ParallelFlowable<? extends T> source;
/*     */   final Callable<? extends C> initialCollection;
/*     */   final BiConsumer<? super C, ? super T> collector;
/*     */   
/*     */   public ParallelCollect(ParallelFlowable<? extends T> source, Callable<? extends C> initialCollection, BiConsumer<? super C, ? super T> collector) {
/*  44 */     this.source = source;
/*  45 */     this.initialCollection = initialCollection;
/*  46 */     this.collector = collector;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribe(Subscriber<? super C>[] subscribers) {
/*  51 */     if (!validate((Subscriber[])subscribers)) {
/*     */       return;
/*     */     }
/*     */     
/*  55 */     int n = subscribers.length;
/*     */     
/*  57 */     Subscriber[] arrayOfSubscriber = new Subscriber[n];
/*     */     
/*  59 */     for (int i = 0; i < n; i++) {
/*     */       C initialValue;
/*     */ 
/*     */       
/*     */       try {
/*  64 */         initialValue = (C)ObjectHelper.requireNonNull(this.initialCollection.call(), "The initialSupplier returned a null value");
/*  65 */       } catch (Throwable ex) {
/*  66 */         Exceptions.throwIfFatal(ex);
/*  67 */         reportError((Subscriber<?>[])subscribers, ex);
/*     */         
/*     */         return;
/*     */       } 
/*  71 */       arrayOfSubscriber[i] = (Subscriber)new ParallelCollectSubscriber<T, C>(subscribers[i], initialValue, this.collector);
/*     */     } 
/*     */     
/*  74 */     this.source.subscribe(arrayOfSubscriber);
/*     */   }
/*     */   
/*     */   void reportError(Subscriber<?>[] subscribers, Throwable ex) {
/*  78 */     for (Subscriber<?> s : subscribers) {
/*  79 */       EmptySubscription.error(ex, s);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int parallelism() {
/*  85 */     return this.source.parallelism();
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ParallelCollectSubscriber<T, C>
/*     */     extends DeferredScalarSubscriber<T, C>
/*     */   {
/*     */     private static final long serialVersionUID = -4767392946044436228L;
/*     */     
/*     */     final BiConsumer<? super C, ? super T> collector;
/*     */     
/*     */     C collection;
/*     */     boolean done;
/*     */     
/*     */     ParallelCollectSubscriber(Subscriber<? super C> subscriber, C initialValue, BiConsumer<? super C, ? super T> collector) {
/* 100 */       super(subscriber);
/* 101 */       this.collection = initialValue;
/* 102 */       this.collector = collector;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 107 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 108 */         this.upstream = s;
/*     */         
/* 110 */         this.downstream.onSubscribe((Subscription)this);
/*     */         
/* 112 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 118 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       
/*     */       try {
/* 123 */         this.collector.accept(this.collection, t);
/* 124 */       } catch (Throwable ex) {
/* 125 */         Exceptions.throwIfFatal(ex);
/* 126 */         cancel();
/* 127 */         onError(ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 133 */       if (this.done) {
/* 134 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 137 */       this.done = true;
/* 138 */       this.collection = null;
/* 139 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 144 */       if (this.done) {
/*     */         return;
/*     */       }
/* 147 */       this.done = true;
/* 148 */       C c = this.collection;
/* 149 */       this.collection = null;
/* 150 */       complete(c);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 155 */       super.cancel();
/* 156 */       this.upstream.cancel();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/parallel/ParallelCollect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */