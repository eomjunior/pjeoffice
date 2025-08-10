/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.BiFunction;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.subscriptions.EmptySubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.Iterator;
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
/*     */ public final class FlowableZipIterable<T, U, V>
/*     */   extends AbstractFlowableWithUpstream<T, V>
/*     */ {
/*     */   final Iterable<U> other;
/*     */   final BiFunction<? super T, ? super U, ? extends V> zipper;
/*     */   
/*     */   public FlowableZipIterable(Flowable<T> source, Iterable<U> other, BiFunction<? super T, ? super U, ? extends V> zipper) {
/*  34 */     super(source);
/*  35 */     this.other = other;
/*  36 */     this.zipper = zipper;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Subscriber<? super V> t) {
/*     */     Iterator<U> it;
/*     */     boolean b;
/*     */     try {
/*  44 */       it = (Iterator<U>)ObjectHelper.requireNonNull(this.other.iterator(), "The iterator returned by other is null");
/*  45 */     } catch (Throwable e) {
/*  46 */       Exceptions.throwIfFatal(e);
/*  47 */       EmptySubscription.error(e, t);
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*     */     try {
/*  54 */       b = it.hasNext();
/*  55 */     } catch (Throwable e) {
/*  56 */       Exceptions.throwIfFatal(e);
/*  57 */       EmptySubscription.error(e, t);
/*     */       
/*     */       return;
/*     */     } 
/*  61 */     if (!b) {
/*  62 */       EmptySubscription.complete(t);
/*     */       
/*     */       return;
/*     */     } 
/*  66 */     this.source.subscribe(new ZipIterableSubscriber<T, U, V>(t, it, this.zipper));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ZipIterableSubscriber<T, U, V>
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     final Subscriber<? super V> downstream;
/*     */     final Iterator<U> iterator;
/*     */     final BiFunction<? super T, ? super U, ? extends V> zipper;
/*     */     Subscription upstream;
/*     */     boolean done;
/*     */     
/*     */     ZipIterableSubscriber(Subscriber<? super V> actual, Iterator<U> iterator, BiFunction<? super T, ? super U, ? extends V> zipper) {
/*  80 */       this.downstream = actual;
/*  81 */       this.iterator = iterator;
/*  82 */       this.zipper = zipper;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  87 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  88 */         this.upstream = s;
/*  89 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     } public void onNext(T t) {
/*     */       U u;
/*     */       V v;
/*     */       boolean b;
/*  95 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 102 */         u = (U)ObjectHelper.requireNonNull(this.iterator.next(), "The iterator returned a null value");
/* 103 */       } catch (Throwable e) {
/* 104 */         error(e);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/*     */       try {
/* 110 */         v = (V)ObjectHelper.requireNonNull(this.zipper.apply(t, u), "The zipper function returned a null value");
/* 111 */       } catch (Throwable e) {
/* 112 */         error(e);
/*     */         
/*     */         return;
/*     */       } 
/* 116 */       this.downstream.onNext(v);
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 121 */         b = this.iterator.hasNext();
/* 122 */       } catch (Throwable e) {
/* 123 */         error(e);
/*     */         
/*     */         return;
/*     */       } 
/* 127 */       if (!b) {
/* 128 */         this.done = true;
/* 129 */         this.upstream.cancel();
/* 130 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */     
/*     */     void error(Throwable e) {
/* 135 */       Exceptions.throwIfFatal(e);
/* 136 */       this.done = true;
/* 137 */       this.upstream.cancel();
/* 138 */       this.downstream.onError(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 143 */       if (this.done) {
/* 144 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 147 */       this.done = true;
/* 148 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 153 */       if (this.done) {
/*     */         return;
/*     */       }
/* 156 */       this.done = true;
/* 157 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 162 */       this.upstream.request(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 167 */       this.upstream.cancel();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableZipIterable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */