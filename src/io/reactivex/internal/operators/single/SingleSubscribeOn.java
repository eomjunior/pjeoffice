/*    */ package io.reactivex.internal.operators.single;
/*    */ 
/*    */ import io.reactivex.Scheduler;
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.SingleSource;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import io.reactivex.internal.disposables.SequentialDisposable;
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
/*    */ public final class SingleSubscribeOn<T>
/*    */   extends Single<T>
/*    */ {
/*    */   final SingleSource<? extends T> source;
/*    */   final Scheduler scheduler;
/*    */   
/*    */   public SingleSubscribeOn(SingleSource<? extends T> source, Scheduler scheduler) {
/* 28 */     this.source = source;
/* 29 */     this.scheduler = scheduler;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super T> observer) {
/* 34 */     SubscribeOnObserver<T> parent = new SubscribeOnObserver<T>(observer, this.source);
/* 35 */     observer.onSubscribe(parent);
/*    */     
/* 37 */     Disposable f = this.scheduler.scheduleDirect(parent);
/*    */     
/* 39 */     parent.task.replace(f);
/*    */   }
/*    */ 
/*    */   
/*    */   static final class SubscribeOnObserver<T>
/*    */     extends AtomicReference<Disposable>
/*    */     implements SingleObserver<T>, Disposable, Runnable
/*    */   {
/*    */     private static final long serialVersionUID = 7000911171163930287L;
/*    */     
/*    */     final SingleObserver<? super T> downstream;
/*    */     
/*    */     final SequentialDisposable task;
/*    */     
/*    */     final SingleSource<? extends T> source;
/*    */     
/*    */     SubscribeOnObserver(SingleObserver<? super T> actual, SingleSource<? extends T> source) {
/* 56 */       this.downstream = actual;
/* 57 */       this.source = source;
/* 58 */       this.task = new SequentialDisposable();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 63 */       DisposableHelper.setOnce(this, d);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(T value) {
/* 68 */       this.downstream.onSuccess(value);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 73 */       this.downstream.onError(e);
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 78 */       DisposableHelper.dispose(this);
/* 79 */       this.task.dispose();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 84 */       return DisposableHelper.isDisposed(get());
/*    */     }
/*    */ 
/*    */     
/*    */     public void run() {
/* 89 */       this.source.subscribe(this);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleSubscribeOn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */