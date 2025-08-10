/*     */ package io.reactivex.internal.operators.single;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.SingleSource;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
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
/*     */ public final class SingleFlatMapPublisher<T, R>
/*     */   extends Flowable<R>
/*     */ {
/*     */   final SingleSource<T> source;
/*     */   final Function<? super T, ? extends Publisher<? extends R>> mapper;
/*     */   
/*     */   public SingleFlatMapPublisher(SingleSource<T> source, Function<? super T, ? extends Publisher<? extends R>> mapper) {
/*  60 */     this.source = source;
/*  61 */     this.mapper = mapper;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super R> downstream) {
/*  66 */     this.source.subscribe(new SingleFlatMapPublisherObserver<T, R>(downstream, this.mapper));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class SingleFlatMapPublisherObserver<S, T>
/*     */     extends AtomicLong
/*     */     implements SingleObserver<S>, FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = 7759721921468635667L;
/*     */     final Subscriber<? super T> downstream;
/*     */     final Function<? super S, ? extends Publisher<? extends T>> mapper;
/*     */     final AtomicReference<Subscription> parent;
/*     */     Disposable disposable;
/*     */     
/*     */     SingleFlatMapPublisherObserver(Subscriber<? super T> actual, Function<? super S, ? extends Publisher<? extends T>> mapper) {
/*  81 */       this.downstream = actual;
/*  82 */       this.mapper = mapper;
/*  83 */       this.parent = new AtomicReference<Subscription>();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  88 */       this.disposable = d;
/*  89 */       this.downstream.onSubscribe(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(S value) {
/*     */       Publisher<? extends T> f;
/*     */       try {
/*  96 */         f = (Publisher<? extends T>)ObjectHelper.requireNonNull(this.mapper.apply(value), "the mapper returned a null Publisher");
/*  97 */       } catch (Throwable e) {
/*  98 */         Exceptions.throwIfFatal(e);
/*  99 */         this.downstream.onError(e);
/*     */         return;
/*     */       } 
/* 102 */       f.subscribe((Subscriber)this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 107 */       SubscriptionHelper.deferredSetOnce(this.parent, this, s);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 112 */       this.downstream.onNext(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 117 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 122 */       this.downstream.onError(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 127 */       SubscriptionHelper.deferredRequest(this.parent, this, n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 132 */       this.disposable.dispose();
/* 133 */       SubscriptionHelper.cancel(this.parent);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleFlatMapPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */