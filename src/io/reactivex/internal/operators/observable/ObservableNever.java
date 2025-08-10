/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.Observable;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.EmptyDisposable;
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
/*    */ public final class ObservableNever
/*    */   extends Observable<Object>
/*    */ {
/* 20 */   public static final Observable<Object> INSTANCE = new ObservableNever();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Observer<? super Object> o) {
/* 27 */     o.onSubscribe((Disposable)EmptyDisposable.NEVER);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableNever.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */