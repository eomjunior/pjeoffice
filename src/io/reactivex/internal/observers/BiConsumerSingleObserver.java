/*    */ package io.reactivex.internal.observers;
/*    */ 
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.exceptions.CompositeException;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.functions.BiConsumer;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
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
/*    */ public final class BiConsumerSingleObserver<T>
/*    */   extends AtomicReference<Disposable>
/*    */   implements SingleObserver<T>, Disposable
/*    */ {
/*    */   private static final long serialVersionUID = 4943102778943297569L;
/*    */   final BiConsumer<? super T, ? super Throwable> onCallback;
/*    */   
/*    */   public BiConsumerSingleObserver(BiConsumer<? super T, ? super Throwable> onCallback) {
/* 33 */     this.onCallback = onCallback;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onError(Throwable e) {
/*    */     try {
/* 39 */       lazySet((Disposable)DisposableHelper.DISPOSED);
/* 40 */       this.onCallback.accept(null, e);
/* 41 */     } catch (Throwable ex) {
/* 42 */       Exceptions.throwIfFatal(ex);
/* 43 */       RxJavaPlugins.onError((Throwable)new CompositeException(new Throwable[] { e, ex }));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onSubscribe(Disposable d) {
/* 49 */     DisposableHelper.setOnce(this, d);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onSuccess(T value) {
/*    */     try {
/* 55 */       lazySet((Disposable)DisposableHelper.DISPOSED);
/* 56 */       this.onCallback.accept(value, null);
/* 57 */     } catch (Throwable ex) {
/* 58 */       Exceptions.throwIfFatal(ex);
/* 59 */       RxJavaPlugins.onError(ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void dispose() {
/* 65 */     DisposableHelper.dispose(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDisposed() {
/* 70 */     return (get() == DisposableHelper.DISPOSED);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/observers/BiConsumerSingleObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */