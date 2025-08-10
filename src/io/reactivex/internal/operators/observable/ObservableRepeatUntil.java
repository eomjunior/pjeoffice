/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.Observable;
/*    */ import io.reactivex.ObservableSource;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.functions.BooleanSupplier;
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
/*    */ public final class ObservableRepeatUntil<T>
/*    */   extends AbstractObservableWithUpstream<T, T>
/*    */ {
/*    */   final BooleanSupplier until;
/*    */   
/*    */   public ObservableRepeatUntil(Observable<T> source, BooleanSupplier until) {
/* 27 */     super((ObservableSource<T>)source);
/* 28 */     this.until = until;
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(Observer<? super T> observer) {
/* 33 */     SequentialDisposable sd = new SequentialDisposable();
/* 34 */     observer.onSubscribe((Disposable)sd);
/*    */     
/* 36 */     RepeatUntilObserver<T> rs = new RepeatUntilObserver<T>(observer, this.until, sd, this.source);
/* 37 */     rs.subscribeNext();
/*    */   }
/*    */   
/*    */   static final class RepeatUntilObserver<T>
/*    */     extends AtomicInteger implements Observer<T> {
/*    */     private static final long serialVersionUID = -7098360935104053232L;
/*    */     final Observer<? super T> downstream;
/*    */     final SequentialDisposable upstream;
/*    */     final ObservableSource<? extends T> source;
/*    */     final BooleanSupplier stop;
/*    */     
/*    */     RepeatUntilObserver(Observer<? super T> actual, BooleanSupplier until, SequentialDisposable sd, ObservableSource<? extends T> source) {
/* 49 */       this.downstream = actual;
/* 50 */       this.upstream = sd;
/* 51 */       this.source = source;
/* 52 */       this.stop = until;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 57 */       this.upstream.replace(d);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(T t) {
/* 62 */       this.downstream.onNext(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 67 */       this.downstream.onError(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/*    */       boolean b;
/*    */       try {
/* 74 */         b = this.stop.getAsBoolean();
/* 75 */       } catch (Throwable e) {
/* 76 */         Exceptions.throwIfFatal(e);
/* 77 */         this.downstream.onError(e);
/*    */         return;
/*    */       } 
/* 80 */       if (b) {
/* 81 */         this.downstream.onComplete();
/*    */       } else {
/* 83 */         subscribeNext();
/*    */       } 
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     void subscribeNext() {
/* 91 */       if (getAndIncrement() == 0) {
/* 92 */         int missed = 1;
/*    */         do {
/* 94 */           this.source.subscribe(this);
/*    */           
/* 96 */           missed = addAndGet(-missed);
/* 97 */         } while (missed != 0);
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableRepeatUntil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */