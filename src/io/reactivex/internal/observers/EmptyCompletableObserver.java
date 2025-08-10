/*    */ package io.reactivex.internal.observers;
/*    */ 
/*    */ import io.reactivex.CompletableObserver;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.exceptions.OnErrorNotImplementedException;
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
/*    */ 
/*    */ 
/*    */ public final class EmptyCompletableObserver
/*    */   extends AtomicReference<Disposable>
/*    */   implements CompletableObserver, Disposable, LambdaConsumerIntrospection
/*    */ {
/*    */   private static final long serialVersionUID = -7545121636549663526L;
/*    */   
/*    */   public void dispose() {
/* 33 */     DisposableHelper.dispose(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDisposed() {
/* 38 */     return (get() == DisposableHelper.DISPOSED);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onComplete() {
/* 44 */     lazySet((Disposable)DisposableHelper.DISPOSED);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onError(Throwable e) {
/* 49 */     lazySet((Disposable)DisposableHelper.DISPOSED);
/* 50 */     RxJavaPlugins.onError((Throwable)new OnErrorNotImplementedException(e));
/*    */   }
/*    */ 
/*    */   
/*    */   public void onSubscribe(Disposable d) {
/* 55 */     DisposableHelper.setOnce(this, d);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasCustomOnError() {
/* 60 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/observers/EmptyCompletableObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */