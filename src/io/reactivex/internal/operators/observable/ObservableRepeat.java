/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.Observable;
/*    */ import io.reactivex.ObservableSource;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.SequentialDisposable;
/*    */ import java.util.concurrent.atomic.AtomicInteger;
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
/*    */ public final class ObservableRepeat<T>
/*    */   extends AbstractObservableWithUpstream<T, T>
/*    */ {
/*    */   final long count;
/*    */   
/*    */   public ObservableRepeat(Observable<T> source, long count) {
/* 25 */     super((ObservableSource<T>)source);
/* 26 */     this.count = count;
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(Observer<? super T> observer) {
/* 31 */     SequentialDisposable sd = new SequentialDisposable();
/* 32 */     observer.onSubscribe((Disposable)sd);
/*    */     
/* 34 */     RepeatObserver<T> rs = new RepeatObserver<T>(observer, (this.count != Long.MAX_VALUE) ? (this.count - 1L) : Long.MAX_VALUE, sd, this.source);
/* 35 */     rs.subscribeNext();
/*    */   }
/*    */   
/*    */   static final class RepeatObserver<T>
/*    */     extends AtomicInteger implements Observer<T> {
/*    */     private static final long serialVersionUID = -7098360935104053232L;
/*    */     final Observer<? super T> downstream;
/*    */     final SequentialDisposable sd;
/*    */     final ObservableSource<? extends T> source;
/*    */     long remaining;
/*    */     
/*    */     RepeatObserver(Observer<? super T> actual, long count, SequentialDisposable sd, ObservableSource<? extends T> source) {
/* 47 */       this.downstream = actual;
/* 48 */       this.sd = sd;
/* 49 */       this.source = source;
/* 50 */       this.remaining = count;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 55 */       this.sd.replace(d);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(T t) {
/* 60 */       this.downstream.onNext(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 65 */       this.downstream.onError(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 70 */       long r = this.remaining;
/* 71 */       if (r != Long.MAX_VALUE) {
/* 72 */         this.remaining = r - 1L;
/*    */       }
/* 74 */       if (r != 0L) {
/* 75 */         subscribeNext();
/*    */       } else {
/* 77 */         this.downstream.onComplete();
/*    */       } 
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     void subscribeNext() {
/* 85 */       if (getAndIncrement() == 0) {
/* 86 */         int missed = 1;
/*    */         do {
/* 88 */           if (this.sd.isDisposed()) {
/*    */             return;
/*    */           }
/* 91 */           this.source.subscribe(this);
/*    */           
/* 93 */           missed = addAndGet(-missed);
/* 94 */         } while (missed != 0);
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableRepeat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */