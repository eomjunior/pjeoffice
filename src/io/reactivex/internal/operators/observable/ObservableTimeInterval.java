/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.ObservableSource;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.Scheduler;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import io.reactivex.schedulers.Timed;
/*    */ import java.util.concurrent.TimeUnit;
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
/*    */ public final class ObservableTimeInterval<T>
/*    */   extends AbstractObservableWithUpstream<T, Timed<T>>
/*    */ {
/*    */   final Scheduler scheduler;
/*    */   final TimeUnit unit;
/*    */   
/*    */   public ObservableTimeInterval(ObservableSource<T> source, TimeUnit unit, Scheduler scheduler) {
/* 28 */     super(source);
/* 29 */     this.scheduler = scheduler;
/* 30 */     this.unit = unit;
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(Observer<? super Timed<T>> t) {
/* 35 */     this.source.subscribe(new TimeIntervalObserver<T>(t, this.unit, this.scheduler));
/*    */   }
/*    */   
/*    */   static final class TimeIntervalObserver<T>
/*    */     implements Observer<T>, Disposable
/*    */   {
/*    */     final Observer<? super Timed<T>> downstream;
/*    */     final TimeUnit unit;
/*    */     final Scheduler scheduler;
/*    */     long lastTime;
/*    */     Disposable upstream;
/*    */     
/*    */     TimeIntervalObserver(Observer<? super Timed<T>> actual, TimeUnit unit, Scheduler scheduler) {
/* 48 */       this.downstream = actual;
/* 49 */       this.scheduler = scheduler;
/* 50 */       this.unit = unit;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 55 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 56 */         this.upstream = d;
/* 57 */         this.lastTime = this.scheduler.now(this.unit);
/* 58 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 64 */       this.upstream.dispose();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 69 */       return this.upstream.isDisposed();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(T t) {
/* 74 */       long now = this.scheduler.now(this.unit);
/* 75 */       long last = this.lastTime;
/* 76 */       this.lastTime = now;
/* 77 */       long delta = now - last;
/* 78 */       this.downstream.onNext(new Timed(t, delta, this.unit));
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 83 */       this.downstream.onError(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 88 */       this.downstream.onComplete();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableTimeInterval.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */