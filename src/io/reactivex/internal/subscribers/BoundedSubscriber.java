/*     */ package io.reactivex.internal.subscribers;
/*     */ 
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.CompositeException;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Action;
/*     */ import io.reactivex.functions.Consumer;
/*     */ import io.reactivex.internal.functions.Functions;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.observers.LambdaConsumerIntrospection;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ public final class BoundedSubscriber<T>
/*     */   extends AtomicReference<Subscription>
/*     */   implements FlowableSubscriber<T>, Subscription, Disposable, LambdaConsumerIntrospection
/*     */ {
/*     */   private static final long serialVersionUID = -7251123623727029452L;
/*     */   final Consumer<? super T> onNext;
/*     */   final Consumer<? super Throwable> onError;
/*     */   final Action onComplete;
/*     */   final Consumer<? super Subscription> onSubscribe;
/*     */   final int bufferSize;
/*     */   int consumed;
/*     */   final int limit;
/*     */   
/*     */   public BoundedSubscriber(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete, Consumer<? super Subscription> onSubscribe, int bufferSize) {
/*  46 */     this.onNext = onNext;
/*  47 */     this.onError = onError;
/*  48 */     this.onComplete = onComplete;
/*  49 */     this.onSubscribe = onSubscribe;
/*  50 */     this.bufferSize = bufferSize;
/*  51 */     this.limit = bufferSize - (bufferSize >> 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSubscribe(Subscription s) {
/*  56 */     if (SubscriptionHelper.setOnce(this, s)) {
/*     */       try {
/*  58 */         this.onSubscribe.accept(this);
/*  59 */       } catch (Throwable e) {
/*  60 */         Exceptions.throwIfFatal(e);
/*  61 */         s.cancel();
/*  62 */         onError(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNext(T t) {
/*  69 */     if (!isDisposed()) {
/*     */       try {
/*  71 */         this.onNext.accept(t);
/*     */         
/*  73 */         int c = this.consumed + 1;
/*  74 */         if (c == this.limit) {
/*  75 */           this.consumed = 0;
/*  76 */           get().request(this.limit);
/*     */         } else {
/*  78 */           this.consumed = c;
/*     */         } 
/*  80 */       } catch (Throwable e) {
/*  81 */         Exceptions.throwIfFatal(e);
/*  82 */         get().cancel();
/*  83 */         onError(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onError(Throwable t) {
/*  90 */     if (get() != SubscriptionHelper.CANCELLED) {
/*  91 */       lazySet((Subscription)SubscriptionHelper.CANCELLED);
/*     */       try {
/*  93 */         this.onError.accept(t);
/*  94 */       } catch (Throwable e) {
/*  95 */         Exceptions.throwIfFatal(e);
/*  96 */         RxJavaPlugins.onError((Throwable)new CompositeException(new Throwable[] { t, e }));
/*     */       } 
/*     */     } else {
/*  99 */       RxJavaPlugins.onError(t);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onComplete() {
/* 105 */     if (get() != SubscriptionHelper.CANCELLED) {
/* 106 */       lazySet((Subscription)SubscriptionHelper.CANCELLED);
/*     */       try {
/* 108 */         this.onComplete.run();
/* 109 */       } catch (Throwable e) {
/* 110 */         Exceptions.throwIfFatal(e);
/* 111 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 118 */     cancel();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDisposed() {
/* 123 */     return (get() == SubscriptionHelper.CANCELLED);
/*     */   }
/*     */ 
/*     */   
/*     */   public void request(long n) {
/* 128 */     get().request(n);
/*     */   }
/*     */ 
/*     */   
/*     */   public void cancel() {
/* 133 */     SubscriptionHelper.cancel(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasCustomOnError() {
/* 138 */     return (this.onError != Functions.ON_ERROR_MISSING);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/subscribers/BoundedSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */