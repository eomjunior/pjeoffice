/*    */ package io.reactivex.internal.operators.single;
/*    */ 
/*    */ import io.reactivex.Observable;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.SingleSource;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import io.reactivex.internal.observers.DeferredScalarDisposable;
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
/*    */ public final class SingleToObservable<T>
/*    */   extends Observable<T>
/*    */ {
/*    */   final SingleSource<? extends T> source;
/*    */   
/*    */   public SingleToObservable(SingleSource<? extends T> source) {
/* 30 */     this.source = source;
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(Observer<? super T> observer) {
/* 35 */     this.source.subscribe(create(observer));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> SingleObserver<T> create(Observer<? super T> downstream) {
/* 47 */     return new SingleToObservableObserver<T>(downstream);
/*    */   }
/*    */   
/*    */   static final class SingleToObservableObserver<T>
/*    */     extends DeferredScalarDisposable<T>
/*    */     implements SingleObserver<T>
/*    */   {
/*    */     private static final long serialVersionUID = 3786543492451018833L;
/*    */     Disposable upstream;
/*    */     
/*    */     SingleToObservableObserver(Observer<? super T> downstream) {
/* 58 */       super(downstream);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 63 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 64 */         this.upstream = d;
/*    */         
/* 66 */         this.downstream.onSubscribe((Disposable)this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(T value) {
/* 72 */       complete(value);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 77 */       error(e);
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 82 */       super.dispose();
/* 83 */       this.upstream.dispose();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleToObservable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */