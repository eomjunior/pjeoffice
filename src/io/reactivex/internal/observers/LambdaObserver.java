/*     */ package io.reactivex.internal.observers;
/*     */ 
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.CompositeException;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Action;
/*     */ import io.reactivex.functions.Consumer;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.Functions;
/*     */ import io.reactivex.observers.LambdaConsumerIntrospection;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ public final class LambdaObserver<T>
/*     */   extends AtomicReference<Disposable>
/*     */   implements Observer<T>, Disposable, LambdaConsumerIntrospection
/*     */ {
/*     */   private static final long serialVersionUID = -7251123623727029452L;
/*     */   final Consumer<? super T> onNext;
/*     */   final Consumer<? super Throwable> onError;
/*     */   final Action onComplete;
/*     */   final Consumer<? super Disposable> onSubscribe;
/*     */   
/*     */   public LambdaObserver(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete, Consumer<? super Disposable> onSubscribe) {
/*  40 */     this.onNext = onNext;
/*  41 */     this.onError = onError;
/*  42 */     this.onComplete = onComplete;
/*  43 */     this.onSubscribe = onSubscribe;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSubscribe(Disposable d) {
/*  48 */     if (DisposableHelper.setOnce(this, d)) {
/*     */       try {
/*  50 */         this.onSubscribe.accept(this);
/*  51 */       } catch (Throwable ex) {
/*  52 */         Exceptions.throwIfFatal(ex);
/*  53 */         d.dispose();
/*  54 */         onError(ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNext(T t) {
/*  61 */     if (!isDisposed()) {
/*     */       try {
/*  63 */         this.onNext.accept(t);
/*  64 */       } catch (Throwable e) {
/*  65 */         Exceptions.throwIfFatal(e);
/*  66 */         get().dispose();
/*  67 */         onError(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onError(Throwable t) {
/*  74 */     if (!isDisposed()) {
/*  75 */       lazySet((Disposable)DisposableHelper.DISPOSED);
/*     */       try {
/*  77 */         this.onError.accept(t);
/*  78 */       } catch (Throwable e) {
/*  79 */         Exceptions.throwIfFatal(e);
/*  80 */         RxJavaPlugins.onError((Throwable)new CompositeException(new Throwable[] { t, e }));
/*     */       } 
/*     */     } else {
/*  83 */       RxJavaPlugins.onError(t);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onComplete() {
/*  89 */     if (!isDisposed()) {
/*  90 */       lazySet((Disposable)DisposableHelper.DISPOSED);
/*     */       try {
/*  92 */         this.onComplete.run();
/*  93 */       } catch (Throwable e) {
/*  94 */         Exceptions.throwIfFatal(e);
/*  95 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 102 */     DisposableHelper.dispose(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDisposed() {
/* 107 */     return (get() == DisposableHelper.DISPOSED);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasCustomOnError() {
/* 112 */     return (this.onError != Functions.ON_ERROR_MISSING);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/observers/LambdaObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */