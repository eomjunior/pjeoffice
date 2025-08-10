/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.Observable;
/*    */ import io.reactivex.ObservableSource;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.SingleSource;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
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
/*    */ 
/*    */ public final class ObservableConcatWithSingle<T>
/*    */   extends AbstractObservableWithUpstream<T, T>
/*    */ {
/*    */   final SingleSource<? extends T> other;
/*    */   
/*    */   public ObservableConcatWithSingle(Observable<T> source, SingleSource<? extends T> other) {
/* 34 */     super((ObservableSource<T>)source);
/* 35 */     this.other = other;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Observer<? super T> observer) {
/* 40 */     this.source.subscribe(new ConcatWithObserver<T>(observer, this.other));
/*    */   }
/*    */ 
/*    */   
/*    */   static final class ConcatWithObserver<T>
/*    */     extends AtomicReference<Disposable>
/*    */     implements Observer<T>, SingleObserver<T>, Disposable
/*    */   {
/*    */     private static final long serialVersionUID = -1953724749712440952L;
/*    */     
/*    */     final Observer<? super T> downstream;
/*    */     
/*    */     SingleSource<? extends T> other;
/*    */     boolean inSingle;
/*    */     
/*    */     ConcatWithObserver(Observer<? super T> actual, SingleSource<? extends T> other) {
/* 56 */       this.downstream = actual;
/* 57 */       this.other = other;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 62 */       if (DisposableHelper.setOnce(this, d) && !this.inSingle) {
/* 63 */         this.downstream.onSubscribe(this);
/*    */       }
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(T t) {
/* 69 */       this.downstream.onNext(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(T t) {
/* 74 */       this.downstream.onNext(t);
/* 75 */       this.downstream.onComplete();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 80 */       this.downstream.onError(e);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 85 */       this.inSingle = true;
/* 86 */       DisposableHelper.replace(this, null);
/* 87 */       SingleSource<? extends T> ss = this.other;
/* 88 */       this.other = null;
/* 89 */       ss.subscribe(this);
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 94 */       DisposableHelper.dispose(this);
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 99 */       return DisposableHelper.isDisposed(get());
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableConcatWithSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */