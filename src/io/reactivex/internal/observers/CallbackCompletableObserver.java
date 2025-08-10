/*    */ package io.reactivex.internal.observers;
/*    */ 
/*    */ import io.reactivex.CompletableObserver;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.exceptions.OnErrorNotImplementedException;
/*    */ import io.reactivex.functions.Action;
/*    */ import io.reactivex.functions.Consumer;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
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
/*    */ public final class CallbackCompletableObserver
/*    */   extends AtomicReference<Disposable>
/*    */   implements CompletableObserver, Disposable, Consumer<Throwable>, LambdaConsumerIntrospection
/*    */ {
/*    */   private static final long serialVersionUID = -4361286194466301354L;
/*    */   final Consumer<? super Throwable> onError;
/*    */   final Action onComplete;
/*    */   
/*    */   public CallbackCompletableObserver(Action onComplete) {
/* 36 */     this.onError = this;
/* 37 */     this.onComplete = onComplete;
/*    */   }
/*    */   
/*    */   public CallbackCompletableObserver(Consumer<? super Throwable> onError, Action onComplete) {
/* 41 */     this.onError = onError;
/* 42 */     this.onComplete = onComplete;
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept(Throwable e) {
/* 47 */     RxJavaPlugins.onError((Throwable)new OnErrorNotImplementedException(e));
/*    */   }
/*    */ 
/*    */   
/*    */   public void onComplete() {
/*    */     try {
/* 53 */       this.onComplete.run();
/* 54 */     } catch (Throwable ex) {
/* 55 */       Exceptions.throwIfFatal(ex);
/* 56 */       RxJavaPlugins.onError(ex);
/*    */     } 
/* 58 */     lazySet((Disposable)DisposableHelper.DISPOSED);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onError(Throwable e) {
/*    */     try {
/* 64 */       this.onError.accept(e);
/* 65 */     } catch (Throwable ex) {
/* 66 */       Exceptions.throwIfFatal(ex);
/* 67 */       RxJavaPlugins.onError(ex);
/*    */     } 
/* 69 */     lazySet((Disposable)DisposableHelper.DISPOSED);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onSubscribe(Disposable d) {
/* 74 */     DisposableHelper.setOnce(this, d);
/*    */   }
/*    */ 
/*    */   
/*    */   public void dispose() {
/* 79 */     DisposableHelper.dispose(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDisposed() {
/* 84 */     return (get() == DisposableHelper.DISPOSED);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasCustomOnError() {
/* 89 */     return (this.onError != this);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/observers/CallbackCompletableObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */