/*    */ package io.reactivex.internal.operators.mixed;
/*    */ 
/*    */ import io.reactivex.CompletableObserver;
/*    */ import io.reactivex.CompletableSource;
/*    */ import io.reactivex.Observable;
/*    */ import io.reactivex.ObservableSource;
/*    */ import io.reactivex.Observer;
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
/*    */ 
/*    */ 
/*    */ public final class CompletableAndThenObservable<R>
/*    */   extends Observable<R>
/*    */ {
/*    */   final CompletableSource source;
/*    */   final ObservableSource<? extends R> other;
/*    */   
/*    */   public CompletableAndThenObservable(CompletableSource source, ObservableSource<? extends R> other) {
/* 37 */     this.source = source;
/* 38 */     this.other = other;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Observer<? super R> observer) {
/* 43 */     AndThenObservableObserver<R> parent = new AndThenObservableObserver<R>(observer, this.other);
/* 44 */     observer.onSubscribe(parent);
/* 45 */     this.source.subscribe(parent);
/*    */   }
/*    */ 
/*    */   
/*    */   static final class AndThenObservableObserver<R>
/*    */     extends AtomicReference<Disposable>
/*    */     implements Observer<R>, CompletableObserver, Disposable
/*    */   {
/*    */     private static final long serialVersionUID = -8948264376121066672L;
/*    */     
/*    */     final Observer<? super R> downstream;
/*    */     ObservableSource<? extends R> other;
/*    */     
/*    */     AndThenObservableObserver(Observer<? super R> downstream, ObservableSource<? extends R> other) {
/* 59 */       this.other = other;
/* 60 */       this.downstream = downstream;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(R t) {
/* 65 */       this.downstream.onNext(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 70 */       this.downstream.onError(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 75 */       ObservableSource<? extends R> o = this.other;
/* 76 */       if (o == null) {
/* 77 */         this.downstream.onComplete();
/*    */       } else {
/* 79 */         this.other = null;
/* 80 */         o.subscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 86 */       DisposableHelper.dispose(this);
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 91 */       return DisposableHelper.isDisposed(get());
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 96 */       DisposableHelper.replace(this, d);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/mixed/CompletableAndThenObservable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */