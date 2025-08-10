/*    */ package io.reactivex.internal.operators.single;
/*    */ 
/*    */ import io.reactivex.ObservableSource;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.SingleSource;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import io.reactivex.internal.observers.ResumeSingleObserver;
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
/*    */ public final class SingleDelayWithObservable<T, U>
/*    */   extends Single<T>
/*    */ {
/*    */   final SingleSource<T> source;
/*    */   final ObservableSource<U> other;
/*    */   
/*    */   public SingleDelayWithObservable(SingleSource<T> source, ObservableSource<U> other) {
/* 31 */     this.source = source;
/* 32 */     this.other = other;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super T> observer) {
/* 37 */     this.other.subscribe(new OtherSubscriber<T, Object>(observer, this.source));
/*    */   }
/*    */ 
/*    */   
/*    */   static final class OtherSubscriber<T, U>
/*    */     extends AtomicReference<Disposable>
/*    */     implements Observer<U>, Disposable
/*    */   {
/*    */     private static final long serialVersionUID = -8565274649390031272L;
/*    */     
/*    */     final SingleObserver<? super T> downstream;
/*    */     
/*    */     final SingleSource<T> source;
/*    */     boolean done;
/*    */     
/*    */     OtherSubscriber(SingleObserver<? super T> actual, SingleSource<T> source) {
/* 53 */       this.downstream = actual;
/* 54 */       this.source = source;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 59 */       if (DisposableHelper.set(this, d))
/*    */       {
/* 61 */         this.downstream.onSubscribe(this);
/*    */       }
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(U value) {
/* 67 */       get().dispose();
/* 68 */       onComplete();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 73 */       if (this.done) {
/* 74 */         RxJavaPlugins.onError(e);
/*    */         return;
/*    */       } 
/* 77 */       this.done = true;
/* 78 */       this.downstream.onError(e);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 83 */       if (this.done) {
/*    */         return;
/*    */       }
/* 86 */       this.done = true;
/* 87 */       this.source.subscribe((SingleObserver)new ResumeSingleObserver(this, this.downstream));
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 92 */       DisposableHelper.dispose(this);
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 97 */       return DisposableHelper.isDisposed(get());
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleDelayWithObservable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */