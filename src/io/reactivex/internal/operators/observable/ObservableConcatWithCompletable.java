/*    */ package io.reactivex.internal.operators.observable;
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
/*    */ public final class ObservableConcatWithCompletable<T>
/*    */   extends AbstractObservableWithUpstream<T, T>
/*    */ {
/*    */   final CompletableSource other;
/*    */   
/*    */   public ObservableConcatWithCompletable(Observable<T> source, CompletableSource other) {
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
/*    */     implements Observer<T>, CompletableObserver, Disposable
/*    */   {
/*    */     private static final long serialVersionUID = -1953724749712440952L;
/*    */     
/*    */     final Observer<? super T> downstream;
/*    */     
/*    */     CompletableSource other;
/*    */     boolean inCompletable;
/*    */     
/*    */     ConcatWithObserver(Observer<? super T> actual, CompletableSource other) {
/* 56 */       this.downstream = actual;
/* 57 */       this.other = other;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 62 */       if (DisposableHelper.setOnce(this, d) && !this.inCompletable) {
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
/*    */     public void onError(Throwable e) {
/* 74 */       this.downstream.onError(e);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 79 */       if (this.inCompletable) {
/* 80 */         this.downstream.onComplete();
/*    */       } else {
/* 82 */         this.inCompletable = true;
/* 83 */         DisposableHelper.replace(this, null);
/* 84 */         CompletableSource cs = this.other;
/* 85 */         this.other = null;
/* 86 */         cs.subscribe(this);
/*    */       } 
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


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableConcatWithCompletable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */