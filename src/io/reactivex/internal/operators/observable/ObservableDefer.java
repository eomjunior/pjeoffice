/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.Observable;
/*    */ import io.reactivex.ObservableSource;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.internal.disposables.EmptyDisposable;
/*    */ import io.reactivex.internal.functions.ObjectHelper;
/*    */ import java.util.concurrent.Callable;
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
/*    */ public final class ObservableDefer<T>
/*    */   extends Observable<T>
/*    */ {
/*    */   final Callable<? extends ObservableSource<? extends T>> supplier;
/*    */   
/*    */   public ObservableDefer(Callable<? extends ObservableSource<? extends T>> supplier) {
/* 26 */     this.supplier = supplier;
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(Observer<? super T> observer) {
/*    */     ObservableSource<? extends T> pub;
/*    */     try {
/* 33 */       pub = (ObservableSource<? extends T>)ObjectHelper.requireNonNull(this.supplier.call(), "null ObservableSource supplied");
/* 34 */     } catch (Throwable t) {
/* 35 */       Exceptions.throwIfFatal(t);
/* 36 */       EmptyDisposable.error(t, observer);
/*    */       
/*    */       return;
/*    */     } 
/* 40 */     pub.subscribe(observer);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableDefer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */