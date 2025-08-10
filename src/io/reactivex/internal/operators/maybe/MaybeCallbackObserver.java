/*     */ package io.reactivex.internal.operators.maybe;
/*     */ 
/*     */ import io.reactivex.MaybeObserver;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MaybeCallbackObserver<T>
/*     */   extends AtomicReference<Disposable>
/*     */   implements MaybeObserver<T>, Disposable, LambdaConsumerIntrospection
/*     */ {
/*     */   private static final long serialVersionUID = -6076952298809384986L;
/*     */   final Consumer<? super T> onSuccess;
/*     */   final Consumer<? super Throwable> onError;
/*     */   final Action onComplete;
/*     */   
/*     */   public MaybeCallbackObserver(Consumer<? super T> onSuccess, Consumer<? super Throwable> onError, Action onComplete) {
/*  47 */     this.onSuccess = onSuccess;
/*  48 */     this.onError = onError;
/*  49 */     this.onComplete = onComplete;
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/*  54 */     DisposableHelper.dispose(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDisposed() {
/*  59 */     return DisposableHelper.isDisposed(get());
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSubscribe(Disposable d) {
/*  64 */     DisposableHelper.setOnce(this, d);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSuccess(T value) {
/*  69 */     lazySet((Disposable)DisposableHelper.DISPOSED);
/*     */     try {
/*  71 */       this.onSuccess.accept(value);
/*  72 */     } catch (Throwable ex) {
/*  73 */       Exceptions.throwIfFatal(ex);
/*  74 */       RxJavaPlugins.onError(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onError(Throwable e) {
/*  80 */     lazySet((Disposable)DisposableHelper.DISPOSED);
/*     */     try {
/*  82 */       this.onError.accept(e);
/*  83 */     } catch (Throwable ex) {
/*  84 */       Exceptions.throwIfFatal(ex);
/*  85 */       RxJavaPlugins.onError((Throwable)new CompositeException(new Throwable[] { e, ex }));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onComplete() {
/*  91 */     lazySet((Disposable)DisposableHelper.DISPOSED);
/*     */     try {
/*  93 */       this.onComplete.run();
/*  94 */     } catch (Throwable ex) {
/*  95 */       Exceptions.throwIfFatal(ex);
/*  96 */       RxJavaPlugins.onError(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasCustomOnError() {
/* 102 */     return (this.onError != Functions.ON_ERROR_MISSING);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeCallbackObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */