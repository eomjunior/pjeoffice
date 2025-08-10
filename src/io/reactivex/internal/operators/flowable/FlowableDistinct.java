/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.subscribers.BasicFuseableSubscriber;
/*     */ import io.reactivex.internal.subscriptions.EmptySubscription;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.Callable;
/*     */ import org.reactivestreams.Subscriber;
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
/*     */ public final class FlowableDistinct<T, K>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final Function<? super T, K> keySelector;
/*     */   final Callable<? extends Collection<? super K>> collectionSupplier;
/*     */   
/*     */   public FlowableDistinct(Flowable<T> source, Function<? super T, K> keySelector, Callable<? extends Collection<? super K>> collectionSupplier) {
/*  38 */     super(source);
/*  39 */     this.keySelector = keySelector;
/*  40 */     this.collectionSupplier = collectionSupplier;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> subscriber) {
/*     */     Collection<? super K> collection;
/*     */     try {
/*  48 */       collection = (Collection<? super K>)ObjectHelper.requireNonNull(this.collectionSupplier.call(), "The collectionSupplier returned a null collection. Null values are generally not allowed in 2.x operators and sources.");
/*  49 */     } catch (Throwable ex) {
/*  50 */       Exceptions.throwIfFatal(ex);
/*  51 */       EmptySubscription.error(ex, subscriber);
/*     */       
/*     */       return;
/*     */     } 
/*  55 */     this.source.subscribe((FlowableSubscriber)new DistinctSubscriber<T, K>(subscriber, this.keySelector, collection));
/*     */   }
/*     */   
/*     */   static final class DistinctSubscriber<T, K>
/*     */     extends BasicFuseableSubscriber<T, T>
/*     */   {
/*     */     final Collection<? super K> collection;
/*     */     final Function<? super T, K> keySelector;
/*     */     
/*     */     DistinctSubscriber(Subscriber<? super T> actual, Function<? super T, K> keySelector, Collection<? super K> collection) {
/*  65 */       super(actual);
/*  66 */       this.keySelector = keySelector;
/*  67 */       this.collection = collection;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T value) {
/*  72 */       if (this.done) {
/*     */         return;
/*     */       }
/*  75 */       if (this.sourceMode == 0) {
/*     */         boolean b;
/*     */ 
/*     */         
/*     */         try {
/*  80 */           K key = (K)ObjectHelper.requireNonNull(this.keySelector.apply(value), "The keySelector returned a null key");
/*  81 */           b = this.collection.add(key);
/*  82 */         } catch (Throwable ex) {
/*  83 */           fail(ex);
/*     */           
/*     */           return;
/*     */         } 
/*  87 */         if (b) {
/*  88 */           this.downstream.onNext(value);
/*     */         } else {
/*  90 */           this.upstream.request(1L);
/*     */         } 
/*     */       } else {
/*  93 */         this.downstream.onNext(null);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*  99 */       if (this.done) {
/* 100 */         RxJavaPlugins.onError(e);
/*     */       } else {
/* 102 */         this.done = true;
/* 103 */         this.collection.clear();
/* 104 */         this.downstream.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 110 */       if (!this.done) {
/* 111 */         this.done = true;
/* 112 */         this.collection.clear();
/* 113 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int requestFusion(int mode) {
/* 119 */       return transitiveBoundaryFusion(mode);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public T poll() throws Exception {
/*     */       while (true) {
/* 126 */         T v = (T)this.qs.poll();
/*     */         
/* 128 */         if (v == null || this.collection.add((K)ObjectHelper.requireNonNull(this.keySelector.apply(v), "The keySelector returned a null key"))) {
/* 129 */           return v;
/*     */         }
/* 131 */         if (this.sourceMode == 2) {
/* 132 */           this.upstream.request(1L);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void clear() {
/* 140 */       this.collection.clear();
/* 141 */       super.clear();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableDistinct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */