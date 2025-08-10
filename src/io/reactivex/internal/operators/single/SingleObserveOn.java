/*    */ package io.reactivex.internal.operators.single;
/*    */ 
/*    */ import io.reactivex.Scheduler;
/*    */ import io.reactivex.Single;
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
/*    */ public final class SingleObserveOn<T>
/*    */   extends Single<T>
/*    */ {
/*    */   final SingleSource<T> source;
/*    */   final Scheduler scheduler;
/*    */   
/*    */   public SingleObserveOn(SingleSource<T> source, Scheduler scheduler) {
/* 29 */     this.source = source;
/* 30 */     this.scheduler = scheduler;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super T> observer) {
/* 35 */     this.source.subscribe(new ObserveOnSingleObserver<T>(observer, this.scheduler));
/*    */   }
/*    */ 
/*    */   
/*    */   static final class ObserveOnSingleObserver<T>
/*    */     extends AtomicReference<Disposable>
/*    */     implements SingleObserver<T>, Disposable, Runnable
/*    */   {
/*    */     private static final long serialVersionUID = 3528003840217436037L;
/*    */     final SingleObserver<? super T> downstream;
/*    */     final Scheduler scheduler;
/*    */     T value;
/*    */     Throwable error;
/*    */     
/*    */     ObserveOnSingleObserver(SingleObserver<? super T> actual, Scheduler scheduler) {
/* 50 */       this.downstream = actual;
/* 51 */       this.scheduler = scheduler;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 56 */       if (DisposableHelper.setOnce(this, d)) {
/* 57 */         this.downstream.onSubscribe(this);
/*    */       }
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(T value) {
/* 63 */       this.value = value;
/* 64 */       Disposable d = this.scheduler.scheduleDirect(this);
/* 65 */       DisposableHelper.replace(this, d);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 70 */       this.error = e;
/* 71 */       Disposable d = this.scheduler.scheduleDirect(this);
/* 72 */       DisposableHelper.replace(this, d);
/*    */     }
/*    */ 
/*    */     
/*    */     public void run() {
/* 77 */       Throwable ex = this.error;
/* 78 */       if (ex != null) {
/* 79 */         this.downstream.onError(ex);
/*    */       } else {
/* 81 */         this.downstream.onSuccess(this.value);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 87 */       DisposableHelper.dispose(this);
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 92 */       return DisposableHelper.isDisposed(get());
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleObserveOn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */