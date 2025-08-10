/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.CompositeException;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Consumer;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.subscriptions.EmptySubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ public final class FlowableUsing<T, D>
/*     */   extends Flowable<T>
/*     */ {
/*     */   final Callable<? extends D> resourceSupplier;
/*     */   final Function<? super D, ? extends Publisher<? extends T>> sourceSupplier;
/*     */   final Consumer<? super D> disposer;
/*     */   final boolean eager;
/*     */   
/*     */   public FlowableUsing(Callable<? extends D> resourceSupplier, Function<? super D, ? extends Publisher<? extends T>> sourceSupplier, Consumer<? super D> disposer, boolean eager) {
/*  38 */     this.resourceSupplier = resourceSupplier;
/*  39 */     this.sourceSupplier = sourceSupplier;
/*  40 */     this.disposer = disposer;
/*  41 */     this.eager = eager;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Subscriber<? super T> s) {
/*     */     D resource;
/*     */     Publisher<? extends T> source;
/*     */     try {
/*  49 */       resource = this.resourceSupplier.call();
/*  50 */     } catch (Throwable e) {
/*  51 */       Exceptions.throwIfFatal(e);
/*  52 */       EmptySubscription.error(e, s);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*     */     try {
/*  58 */       source = (Publisher<? extends T>)ObjectHelper.requireNonNull(this.sourceSupplier.apply(resource), "The sourceSupplier returned a null Publisher");
/*  59 */     } catch (Throwable e) {
/*  60 */       Exceptions.throwIfFatal(e);
/*     */       try {
/*  62 */         this.disposer.accept(resource);
/*  63 */       } catch (Throwable ex) {
/*  64 */         Exceptions.throwIfFatal(ex);
/*  65 */         EmptySubscription.error((Throwable)new CompositeException(new Throwable[] { e, ex }, ), s);
/*     */         return;
/*     */       } 
/*  68 */       EmptySubscription.error(e, s);
/*     */       
/*     */       return;
/*     */     } 
/*  72 */     UsingSubscriber<T, D> us = new UsingSubscriber<T, D>(s, resource, this.disposer, this.eager);
/*     */     
/*  74 */     source.subscribe((Subscriber)us);
/*     */   }
/*     */   
/*     */   static final class UsingSubscriber<T, D>
/*     */     extends AtomicBoolean
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = 5904473792286235046L;
/*     */     final Subscriber<? super T> downstream;
/*     */     final D resource;
/*     */     final Consumer<? super D> disposer;
/*     */     final boolean eager;
/*     */     Subscription upstream;
/*     */     
/*     */     UsingSubscriber(Subscriber<? super T> actual, D resource, Consumer<? super D> disposer, boolean eager) {
/*  89 */       this.downstream = actual;
/*  90 */       this.resource = resource;
/*  91 */       this.disposer = disposer;
/*  92 */       this.eager = eager;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  97 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  98 */         this.upstream = s;
/*  99 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 105 */       this.downstream.onNext(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 110 */       if (this.eager) {
/* 111 */         Throwable innerError = null;
/* 112 */         if (compareAndSet(false, true)) {
/*     */           try {
/* 114 */             this.disposer.accept(this.resource);
/* 115 */           } catch (Throwable e) {
/* 116 */             Exceptions.throwIfFatal(e);
/* 117 */             innerError = e;
/*     */           } 
/*     */         }
/*     */         
/* 121 */         this.upstream.cancel();
/* 122 */         if (innerError != null) {
/* 123 */           this.downstream.onError((Throwable)new CompositeException(new Throwable[] { t, innerError }));
/*     */         } else {
/* 125 */           this.downstream.onError(t);
/*     */         } 
/*     */       } else {
/* 128 */         this.downstream.onError(t);
/* 129 */         this.upstream.cancel();
/* 130 */         disposeAfter();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 136 */       if (this.eager) {
/* 137 */         if (compareAndSet(false, true)) {
/*     */           try {
/* 139 */             this.disposer.accept(this.resource);
/* 140 */           } catch (Throwable e) {
/* 141 */             Exceptions.throwIfFatal(e);
/* 142 */             this.downstream.onError(e);
/*     */             
/*     */             return;
/*     */           } 
/*     */         }
/* 147 */         this.upstream.cancel();
/* 148 */         this.downstream.onComplete();
/*     */       } else {
/* 150 */         this.downstream.onComplete();
/* 151 */         this.upstream.cancel();
/* 152 */         disposeAfter();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 158 */       this.upstream.request(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 163 */       disposeAfter();
/* 164 */       this.upstream.cancel();
/*     */     }
/*     */     
/*     */     void disposeAfter() {
/* 168 */       if (compareAndSet(false, true))
/*     */         try {
/* 170 */           this.disposer.accept(this.resource);
/* 171 */         } catch (Throwable e) {
/* 172 */           Exceptions.throwIfFatal(e);
/*     */           
/* 174 */           RxJavaPlugins.onError(e);
/*     */         }  
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableUsing.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */