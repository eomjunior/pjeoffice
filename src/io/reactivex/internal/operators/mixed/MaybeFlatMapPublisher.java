/*     */ package io.reactivex.internal.operators.mixed;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
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
/*     */ public final class MaybeFlatMapPublisher<T, R>
/*     */   extends Flowable<R>
/*     */ {
/*     */   final MaybeSource<T> source;
/*     */   final Function<? super T, ? extends Publisher<? extends R>> mapper;
/*     */   
/*     */   public MaybeFlatMapPublisher(MaybeSource<T> source, Function<? super T, ? extends Publisher<? extends R>> mapper) {
/*  44 */     this.source = source;
/*  45 */     this.mapper = mapper;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super R> s) {
/*  50 */     this.source.subscribe(new FlatMapPublisherSubscriber<T, R>(s, this.mapper));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class FlatMapPublisherSubscriber<T, R>
/*     */     extends AtomicReference<Subscription>
/*     */     implements FlowableSubscriber<R>, MaybeObserver<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = -8948264376121066672L;
/*     */     
/*     */     final Subscriber<? super R> downstream;
/*     */     
/*     */     final Function<? super T, ? extends Publisher<? extends R>> mapper;
/*     */     
/*     */     Disposable upstream;
/*     */     final AtomicLong requested;
/*     */     
/*     */     FlatMapPublisherSubscriber(Subscriber<? super R> downstream, Function<? super T, ? extends Publisher<? extends R>> mapper) {
/*  68 */       this.downstream = downstream;
/*  69 */       this.mapper = mapper;
/*  70 */       this.requested = new AtomicLong();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(R t) {
/*  75 */       this.downstream.onNext(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  80 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  85 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/*  90 */       SubscriptionHelper.deferredRequest(this, this.requested, n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/*  95 */       this.upstream.dispose();
/*  96 */       SubscriptionHelper.cancel(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 101 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 102 */         this.upstream = d;
/* 103 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onSuccess(T t) {
/*     */       Publisher<? extends R> p;
/*     */       try {
/* 112 */         p = (Publisher<? extends R>)ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null Publisher");
/* 113 */       } catch (Throwable ex) {
/* 114 */         Exceptions.throwIfFatal(ex);
/* 115 */         this.downstream.onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/* 119 */       p.subscribe((Subscriber)this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 124 */       SubscriptionHelper.deferredSetOnce(this, this.requested, s);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/mixed/MaybeFlatMapPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */