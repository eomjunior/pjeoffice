/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.BiConsumer;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
/*     */ import io.reactivex.internal.subscriptions.EmptySubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
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
/*     */ public final class FlowableCollect<T, U>
/*     */   extends AbstractFlowableWithUpstream<T, U>
/*     */ {
/*     */   final Callable<? extends U> initialSupplier;
/*     */   final BiConsumer<? super U, ? super T> collector;
/*     */   
/*     */   public FlowableCollect(Flowable<T> source, Callable<? extends U> initialSupplier, BiConsumer<? super U, ? super T> collector) {
/*  32 */     super(source);
/*  33 */     this.initialSupplier = initialSupplier;
/*  34 */     this.collector = collector;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super U> s) {
/*     */     U u;
/*     */     try {
/*  41 */       u = (U)ObjectHelper.requireNonNull(this.initialSupplier.call(), "The initial value supplied is null");
/*  42 */     } catch (Throwable e) {
/*  43 */       EmptySubscription.error(e, s);
/*     */       
/*     */       return;
/*     */     } 
/*  47 */     this.source.subscribe(new CollectSubscriber<T, U>(s, u, this.collector));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class CollectSubscriber<T, U>
/*     */     extends DeferredScalarSubscription<U>
/*     */     implements FlowableSubscriber<T>
/*     */   {
/*     */     private static final long serialVersionUID = -3589550218733891694L;
/*     */     
/*     */     final BiConsumer<? super U, ? super T> collector;
/*     */     final U u;
/*     */     Subscription upstream;
/*     */     boolean done;
/*     */     
/*     */     CollectSubscriber(Subscriber<? super U> actual, U u, BiConsumer<? super U, ? super T> collector) {
/*  63 */       super(actual);
/*  64 */       this.collector = collector;
/*  65 */       this.u = u;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  70 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  71 */         this.upstream = s;
/*  72 */         this.downstream.onSubscribe((Subscription)this);
/*  73 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  79 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       try {
/*  83 */         this.collector.accept(this.u, t);
/*  84 */       } catch (Throwable e) {
/*  85 */         Exceptions.throwIfFatal(e);
/*  86 */         this.upstream.cancel();
/*  87 */         onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  93 */       if (this.done) {
/*  94 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/*  97 */       this.done = true;
/*  98 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 103 */       if (this.done) {
/*     */         return;
/*     */       }
/* 106 */       this.done = true;
/* 107 */       complete(this.u);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 112 */       super.cancel();
/* 113 */       this.upstream.cancel();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableCollect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */