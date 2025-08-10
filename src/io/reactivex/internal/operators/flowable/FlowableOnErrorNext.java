/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.CompositeException;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionArbiter;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
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
/*     */ public final class FlowableOnErrorNext<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final Function<? super Throwable, ? extends Publisher<? extends T>> nextSupplier;
/*     */   final boolean allowFatal;
/*     */   
/*     */   public FlowableOnErrorNext(Flowable<T> source, Function<? super Throwable, ? extends Publisher<? extends T>> nextSupplier, boolean allowFatal) {
/*  31 */     super(source);
/*  32 */     this.nextSupplier = nextSupplier;
/*  33 */     this.allowFatal = allowFatal;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  38 */     OnErrorNextSubscriber<T> parent = new OnErrorNextSubscriber<T>(s, this.nextSupplier, this.allowFatal);
/*  39 */     s.onSubscribe((Subscription)parent);
/*  40 */     this.source.subscribe(parent);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class OnErrorNextSubscriber<T>
/*     */     extends SubscriptionArbiter
/*     */     implements FlowableSubscriber<T>
/*     */   {
/*     */     private static final long serialVersionUID = 4063763155303814625L;
/*     */     
/*     */     final Subscriber<? super T> downstream;
/*     */     
/*     */     final Function<? super Throwable, ? extends Publisher<? extends T>> nextSupplier;
/*     */     
/*     */     final boolean allowFatal;
/*     */     
/*     */     boolean once;
/*     */     boolean done;
/*     */     long produced;
/*     */     
/*     */     OnErrorNextSubscriber(Subscriber<? super T> actual, Function<? super Throwable, ? extends Publisher<? extends T>> nextSupplier, boolean allowFatal) {
/*  61 */       super(false);
/*  62 */       this.downstream = actual;
/*  63 */       this.nextSupplier = nextSupplier;
/*  64 */       this.allowFatal = allowFatal;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  69 */       setSubscription(s);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  74 */       if (this.done) {
/*     */         return;
/*     */       }
/*  77 */       if (!this.once) {
/*  78 */         this.produced++;
/*     */       }
/*  80 */       this.downstream.onNext(t);
/*     */     }
/*     */     
/*     */     public void onError(Throwable t) {
/*     */       Publisher<? extends T> p;
/*  85 */       if (this.once) {
/*  86 */         if (this.done) {
/*  87 */           RxJavaPlugins.onError(t);
/*     */           return;
/*     */         } 
/*  90 */         this.downstream.onError(t);
/*     */         return;
/*     */       } 
/*  93 */       this.once = true;
/*     */       
/*  95 */       if (this.allowFatal && !(t instanceof Exception)) {
/*  96 */         this.downstream.onError(t);
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/*     */       try {
/* 103 */         p = (Publisher<? extends T>)ObjectHelper.requireNonNull(this.nextSupplier.apply(t), "The nextSupplier returned a null Publisher");
/* 104 */       } catch (Throwable e) {
/* 105 */         Exceptions.throwIfFatal(e);
/* 106 */         this.downstream.onError((Throwable)new CompositeException(new Throwable[] { t, e }));
/*     */         
/*     */         return;
/*     */       } 
/* 110 */       long mainProduced = this.produced;
/* 111 */       if (mainProduced != 0L) {
/* 112 */         produced(mainProduced);
/*     */       }
/*     */       
/* 115 */       p.subscribe((Subscriber)this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 120 */       if (this.done) {
/*     */         return;
/*     */       }
/* 123 */       this.done = true;
/* 124 */       this.once = true;
/* 125 */       this.downstream.onComplete();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableOnErrorNext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */