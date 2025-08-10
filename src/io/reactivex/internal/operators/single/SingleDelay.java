/*    */ package io.reactivex.internal.operators.single;
/*    */ 
/*    */ import io.reactivex.Scheduler;
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.SingleSource;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.SequentialDisposable;
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
/*    */ public final class SingleDelay<T>
/*    */   extends Single<T>
/*    */ {
/*    */   final SingleSource<? extends T> source;
/*    */   final long time;
/*    */   final TimeUnit unit;
/*    */   final Scheduler scheduler;
/*    */   final boolean delayError;
/*    */   
/*    */   public SingleDelay(SingleSource<? extends T> source, long time, TimeUnit unit, Scheduler scheduler, boolean delayError) {
/* 31 */     this.source = source;
/* 32 */     this.time = time;
/* 33 */     this.unit = unit;
/* 34 */     this.scheduler = scheduler;
/* 35 */     this.delayError = delayError;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super T> observer) {
/* 41 */     SequentialDisposable sd = new SequentialDisposable();
/* 42 */     observer.onSubscribe((Disposable)sd);
/* 43 */     this.source.subscribe(new Delay(sd, observer));
/*    */   }
/*    */   
/*    */   final class Delay implements SingleObserver<T> {
/*    */     private final SequentialDisposable sd;
/*    */     final SingleObserver<? super T> downstream;
/*    */     
/*    */     Delay(SequentialDisposable sd, SingleObserver<? super T> observer) {
/* 51 */       this.sd = sd;
/* 52 */       this.downstream = observer;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 57 */       this.sd.replace(d);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(T value) {
/* 62 */       this.sd.replace(SingleDelay.this.scheduler.scheduleDirect(new OnSuccess(value), SingleDelay.this.time, SingleDelay.this.unit));
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 67 */       this.sd.replace(SingleDelay.this.scheduler.scheduleDirect(new OnError(e), SingleDelay.this.delayError ? SingleDelay.this.time : 0L, SingleDelay.this.unit));
/*    */     }
/*    */     
/*    */     final class OnSuccess implements Runnable {
/*    */       private final T value;
/*    */       
/*    */       OnSuccess(T value) {
/* 74 */         this.value = value;
/*    */       }
/*    */ 
/*    */       
/*    */       public void run() {
/* 79 */         SingleDelay.Delay.this.downstream.onSuccess(this.value);
/*    */       }
/*    */     }
/*    */     
/*    */     final class OnError implements Runnable {
/*    */       private final Throwable e;
/*    */       
/*    */       OnError(Throwable e) {
/* 87 */         this.e = e;
/*    */       }
/*    */ 
/*    */       
/*    */       public void run() {
/* 92 */         SingleDelay.Delay.this.downstream.onError(this.e);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleDelay.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */