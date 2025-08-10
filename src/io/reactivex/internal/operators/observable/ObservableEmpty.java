/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.Observable;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.internal.disposables.EmptyDisposable;
/*    */ import io.reactivex.internal.fuseable.ScalarCallable;
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
/*    */ public final class ObservableEmpty
/*    */   extends Observable<Object>
/*    */   implements ScalarCallable<Object>
/*    */ {
/* 21 */   public static final Observable<Object> INSTANCE = new ObservableEmpty();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Observer<? super Object> o) {
/* 28 */     EmptyDisposable.complete(o);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object call() {
/* 33 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableEmpty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */