/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.Single;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.BiConsumer;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.FuseToFlowable;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.Callable;
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
/*     */ public final class FlowableCollectSingle<T, U>
/*     */   extends Single<U>
/*     */   implements FuseToFlowable<U>
/*     */ {
/*     */   final Flowable<T> source;
/*     */   final Callable<? extends U> initialSupplier;
/*     */   final BiConsumer<? super U, ? super T> collector;
/*     */   
/*     */   public FlowableCollectSingle(Flowable<T> source, Callable<? extends U> initialSupplier, BiConsumer<? super U, ? super T> collector) {
/*  37 */     this.source = source;
/*  38 */     this.initialSupplier = initialSupplier;
/*  39 */     this.collector = collector;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(SingleObserver<? super U> observer) {
/*     */     U u;
/*     */     try {
/*  46 */       u = (U)ObjectHelper.requireNonNull(this.initialSupplier.call(), "The initialSupplier returned a null value");
/*  47 */     } catch (Throwable e) {
/*  48 */       EmptyDisposable.error(e, observer);
/*     */       
/*     */       return;
/*     */     } 
/*  52 */     this.source.subscribe(new CollectSubscriber<T, U>(observer, u, this.collector));
/*     */   }
/*     */ 
/*     */   
/*     */   public Flowable<U> fuseToFlowable() {
/*  57 */     return RxJavaPlugins.onAssembly(new FlowableCollect<T, U>(this.source, this.initialSupplier, this.collector));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class CollectSubscriber<T, U>
/*     */     implements FlowableSubscriber<T>, Disposable
/*     */   {
/*     */     final SingleObserver<? super U> downstream;
/*     */     
/*     */     final BiConsumer<? super U, ? super T> collector;
/*     */     
/*     */     final U u;
/*     */     Subscription upstream;
/*     */     boolean done;
/*     */     
/*     */     CollectSubscriber(SingleObserver<? super U> actual, U u, BiConsumer<? super U, ? super T> collector) {
/*  73 */       this.downstream = actual;
/*  74 */       this.collector = collector;
/*  75 */       this.u = u;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  80 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  81 */         this.upstream = s;
/*  82 */         this.downstream.onSubscribe(this);
/*  83 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  89 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       try {
/*  93 */         this.collector.accept(this.u, t);
/*  94 */       } catch (Throwable e) {
/*  95 */         Exceptions.throwIfFatal(e);
/*  96 */         this.upstream.cancel();
/*  97 */         onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 103 */       if (this.done) {
/* 104 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 107 */       this.done = true;
/* 108 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/* 109 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 114 */       if (this.done) {
/*     */         return;
/*     */       }
/* 117 */       this.done = true;
/* 118 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/* 119 */       this.downstream.onSuccess(this.u);
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 124 */       this.upstream.cancel();
/* 125 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 130 */       return (this.upstream == SubscriptionHelper.CANCELLED);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableCollectSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */