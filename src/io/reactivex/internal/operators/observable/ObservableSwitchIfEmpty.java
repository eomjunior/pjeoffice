/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.ObservableSource;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.SequentialDisposable;
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
/*    */ public final class ObservableSwitchIfEmpty<T>
/*    */   extends AbstractObservableWithUpstream<T, T>
/*    */ {
/*    */   final ObservableSource<? extends T> other;
/*    */   
/*    */   public ObservableSwitchIfEmpty(ObservableSource<T> source, ObservableSource<? extends T> other) {
/* 23 */     super(source);
/* 24 */     this.other = other;
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(Observer<? super T> t) {
/* 29 */     SwitchIfEmptyObserver<T> parent = new SwitchIfEmptyObserver<T>(t, this.other);
/* 30 */     t.onSubscribe((Disposable)parent.arbiter);
/* 31 */     this.source.subscribe(parent);
/*    */   }
/*    */   
/*    */   static final class SwitchIfEmptyObserver<T>
/*    */     implements Observer<T> {
/*    */     final Observer<? super T> downstream;
/*    */     final ObservableSource<? extends T> other;
/*    */     final SequentialDisposable arbiter;
/*    */     boolean empty;
/*    */     
/*    */     SwitchIfEmptyObserver(Observer<? super T> actual, ObservableSource<? extends T> other) {
/* 42 */       this.downstream = actual;
/* 43 */       this.other = other;
/* 44 */       this.empty = true;
/* 45 */       this.arbiter = new SequentialDisposable();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 50 */       this.arbiter.update(d);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(T t) {
/* 55 */       if (this.empty) {
/* 56 */         this.empty = false;
/*    */       }
/* 58 */       this.downstream.onNext(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 63 */       this.downstream.onError(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 68 */       if (this.empty) {
/* 69 */         this.empty = false;
/* 70 */         this.other.subscribe(this);
/*    */       } else {
/* 72 */         this.downstream.onComplete();
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableSwitchIfEmpty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */