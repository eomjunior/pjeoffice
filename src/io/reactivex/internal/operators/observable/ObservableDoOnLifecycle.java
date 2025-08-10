/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.Observable;
/*    */ import io.reactivex.ObservableSource;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.functions.Action;
/*    */ import io.reactivex.functions.Consumer;
/*    */ import io.reactivex.internal.observers.DisposableLambdaObserver;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ObservableDoOnLifecycle<T>
/*    */   extends AbstractObservableWithUpstream<T, T>
/*    */ {
/*    */   private final Consumer<? super Disposable> onSubscribe;
/*    */   private final Action onDispose;
/*    */   
/*    */   public ObservableDoOnLifecycle(Observable<T> upstream, Consumer<? super Disposable> onSubscribe, Action onDispose) {
/* 26 */     super((ObservableSource<T>)upstream);
/* 27 */     this.onSubscribe = onSubscribe;
/* 28 */     this.onDispose = onDispose;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Observer<? super T> observer) {
/* 33 */     this.source.subscribe((Observer)new DisposableLambdaObserver(observer, this.onSubscribe, this.onDispose));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableDoOnLifecycle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */