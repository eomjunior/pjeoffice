/*     */ package io.reactivex.internal.subscribers;
/*     */ 
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.CompositeException;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Action;
/*     */ import io.reactivex.functions.Consumer;
/*     */ import io.reactivex.functions.Predicate;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
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
/*     */ 
/*     */ public final class ForEachWhileSubscriber<T>
/*     */   extends AtomicReference<Subscription>
/*     */   implements FlowableSubscriber<T>, Disposable
/*     */ {
/*     */   private static final long serialVersionUID = -4403180040475402120L;
/*     */   final Predicate<? super T> onNext;
/*     */   final Consumer<? super Throwable> onError;
/*     */   final Action onComplete;
/*     */   boolean done;
/*     */   
/*     */   public ForEachWhileSubscriber(Predicate<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete) {
/*  43 */     this.onNext = onNext;
/*  44 */     this.onError = onError;
/*  45 */     this.onComplete = onComplete;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSubscribe(Subscription s) {
/*  50 */     SubscriptionHelper.setOnce(this, s, Long.MAX_VALUE);
/*     */   }
/*     */   
/*     */   public void onNext(T t) {
/*     */     boolean b;
/*  55 */     if (this.done) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/*  61 */       b = this.onNext.test(t);
/*  62 */     } catch (Throwable ex) {
/*  63 */       Exceptions.throwIfFatal(ex);
/*  64 */       dispose();
/*  65 */       onError(ex);
/*     */       
/*     */       return;
/*     */     } 
/*  69 */     if (!b) {
/*  70 */       dispose();
/*  71 */       onComplete();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onError(Throwable t) {
/*  77 */     if (this.done) {
/*  78 */       RxJavaPlugins.onError(t);
/*     */       return;
/*     */     } 
/*  81 */     this.done = true;
/*     */     try {
/*  83 */       this.onError.accept(t);
/*  84 */     } catch (Throwable ex) {
/*  85 */       Exceptions.throwIfFatal(ex);
/*  86 */       RxJavaPlugins.onError((Throwable)new CompositeException(new Throwable[] { t, ex }));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onComplete() {
/*  92 */     if (this.done) {
/*     */       return;
/*     */     }
/*  95 */     this.done = true;
/*     */     try {
/*  97 */       this.onComplete.run();
/*  98 */     } catch (Throwable ex) {
/*  99 */       Exceptions.throwIfFatal(ex);
/* 100 */       RxJavaPlugins.onError(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 106 */     SubscriptionHelper.cancel(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDisposed() {
/* 111 */     return (get() == SubscriptionHelper.CANCELLED);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/subscribers/ForEachWhileSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */