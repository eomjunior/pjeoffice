/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.Observable;
/*    */ import io.reactivex.ObservableSource;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.observers.SerializedObserver;
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
/*    */ public final class ObservableSerialized<T>
/*    */   extends AbstractObservableWithUpstream<T, T>
/*    */ {
/*    */   public ObservableSerialized(Observable<T> upstream) {
/* 21 */     super((ObservableSource<T>)upstream);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Observer<? super T> observer) {
/* 26 */     this.source.subscribe((Observer)new SerializedObserver(observer));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableSerialized.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */