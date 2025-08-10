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
/*     */ public final class LambdaSubscriber<T>
/*     */   extends AtomicReference<Subscription>
/*     */   implements FlowableSubscriber<T>, Subscription, Disposable, LambdaConsumerIntrospection
/*     */ {
/*     */   private static final long serialVersionUID = -7251123623727029452L;
/*     */   final Consumer<? super T> onNext;
/*     */   final Consumer<? super Throwable> onError;
/*     */   final Action onComplete;
/*     */   final Consumer<? super Subscription> onSubscribe;
/*     */   
/*     */   public LambdaSubscriber(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete, Consumer<? super Subscription> onSubscribe) {
/*  42 */     this.onNext = onNext;
/*  43 */     this.onError = onError;
/*  44 */     this.onComplete = onComplete;
/*  45 */     this.onSubscribe = onSubscribe;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSubscribe(Subscription s) {
/*  50 */     if (SubscriptionHelper.setOnce(this, s)) {
/*     */       try {
/*  52 */         this.onSubscribe.accept(this);
/*  53 */       } catch (Throwable ex) {
/*  54 */         Exceptions.throwIfFatal(ex);
/*  55 */         s.cancel();
/*  56 */         onError(ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNext(T t) {
/*  63 */     if (!isDisposed()) {
/*     */       try {
/*  65 */         this.onNext.accept(t);
/*  66 */       } catch (Throwable e) {
/*  67 */         Exceptions.throwIfFatal(e);
/*  68 */         get().cancel();
/*  69 */         onError(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onError(Throwable t) {
/*  76 */     if (get() != SubscriptionHelper.CANCELLED) {
/*  77 */       lazySet((Subscription)SubscriptionHelper.CANCELLED);
/*     */       try {
/*  79 */         this.onError.accept(t);
/*  80 */       } catch (Throwable e) {
/*  81 */         Exceptions.throwIfFatal(e);
/*  82 */         RxJavaPlugins.onError((Throwable)new CompositeException(new Throwable[] { t, e }));
/*     */       } 
/*     */     } else {
/*  85 */       RxJavaPlugins.onError(t);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onComplete() {
/*  91 */     if (get() != SubscriptionHelper.CANCELLED) {
/*  92 */       lazySet((Subscription)SubscriptionHelper.CANCELLED);
/*     */       try {
/*  94 */         this.onComplete.run();
/*  95 */       } catch (Throwable e) {
/*  96 */         Exceptions.throwIfFatal(e);
/*  97 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 104 */     cancel();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDisposed() {
/* 109 */     return (get() == SubscriptionHelper.CANCELLED);
/*     */   }
/*     */ 
/*     */   
/*     */   public void request(long n) {
/* 114 */     get().request(n);
/*     */   }
/*     */ 
/*     */   
/*     */   public void cancel() {
/* 119 */     SubscriptionHelper.cancel(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasCustomOnError() {
/* 124 */     return (this.onError != Functions.ON_ERROR_MISSING);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/subscribers/LambdaSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */