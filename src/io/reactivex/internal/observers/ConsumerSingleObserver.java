/*    */ package io.reactivex.internal.observers;
/*    */ 
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.exceptions.CompositeException;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.functions.Consumer;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import io.reactivex.internal.functions.Functions;
/*    */ import io.reactivex.observers.LambdaConsumerIntrospection;
/*    */ import io.reactivex.plugins.RxJavaPlugins;
/*    */ import java.util.concurrent.atomic.AtomicReference;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ConsumerSingleObserver<T>
/*    */   extends AtomicReference<Disposable>
/*    */   implements SingleObserver<T>, Disposable, LambdaConsumerIntrospection
/*    */ {
/*    */   private static final long serialVersionUID = -7012088219455310787L;
/*    */   final Consumer<? super T> onSuccess;
/*    */   final Consumer<? super Throwable> onError;
/*    */   
/*    */   public ConsumerSingleObserver(Consumer<? super T> onSuccess, Consumer<? super Throwable> onError) {
/* 38 */     this.onSuccess = onSuccess;
/* 39 */     this.onError = onError;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onError(Throwable e) {
/* 44 */     lazySet((Disposable)DisposableHelper.DISPOSED);
/*    */     try {
/* 46 */       this.onError.accept(e);
/* 47 */     } catch (Throwable ex) {
/* 48 */       Exceptions.throwIfFatal(ex);
/* 49 */       RxJavaPlugins.onError((Throwable)new CompositeException(new Throwable[] { e, ex }));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onSubscribe(Disposable d) {
/* 55 */     DisposableHelper.setOnce(this, d);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onSuccess(T value) {
/* 60 */     lazySet((Disposable)DisposableHelper.DISPOSED);
/*    */     try {
/* 62 */       this.onSuccess.accept(value);
/* 63 */     } catch (Throwable ex) {
/* 64 */       Exceptions.throwIfFatal(ex);
/* 65 */       RxJavaPlugins.onError(ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void dispose() {
/* 71 */     DisposableHelper.dispose(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDisposed() {
/* 76 */     return (get() == DisposableHelper.DISPOSED);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasCustomOnError() {
/* 81 */     return (this.onError != Functions.ON_ERROR_MISSING);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/observers/ConsumerSingleObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */