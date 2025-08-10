/*    */ package io.reactivex.internal.operators.completable;
/*    */ 
/*    */ import io.reactivex.Completable;
/*    */ import io.reactivex.CompletableObserver;
/*    */ import io.reactivex.CompletableSource;
/*    */ import io.reactivex.Scheduler;
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
/*    */ public final class CompletableSubscribeOn
/*    */   extends Completable
/*    */ {
/*    */   final CompletableSource source;
/*    */   final Scheduler scheduler;
/*    */   
/*    */   public CompletableSubscribeOn(CompletableSource source, Scheduler scheduler) {
/* 28 */     this.source = source;
/* 29 */     this.scheduler = scheduler;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void subscribeActual(CompletableObserver observer) {
/* 35 */     SubscribeOnObserver parent = new SubscribeOnObserver(observer, this.source);
/* 36 */     observer.onSubscribe(parent);
/*    */     
/* 38 */     Disposable f = this.scheduler.scheduleDirect(parent);
/*    */     
/* 40 */     parent.task.replace(f);
/*    */   }
/*    */ 
/*    */   
/*    */   static final class SubscribeOnObserver
/*    */     extends AtomicReference<Disposable>
/*    */     implements CompletableObserver, Disposable, Runnable
/*    */   {
/*    */     private static final long serialVersionUID = 7000911171163930287L;
/*    */     
/*    */     final CompletableObserver downstream;
/*    */     
/*    */     final SequentialDisposable task;
/*    */     
/*    */     final CompletableSource source;
/*    */     
/*    */     SubscribeOnObserver(CompletableObserver actual, CompletableSource source) {
/* 57 */       this.downstream = actual;
/* 58 */       this.source = source;
/* 59 */       this.task = new SequentialDisposable();
/*    */     }
/*    */ 
/*    */     
/*    */     public void run() {
/* 64 */       this.source.subscribe(this);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 69 */       DisposableHelper.setOnce(this, d);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 74 */       this.downstream.onError(e);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 79 */       this.downstream.onComplete();
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 84 */       DisposableHelper.dispose(this);
/* 85 */       this.task.dispose();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 90 */       return DisposableHelper.isDisposed(get());
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableSubscribeOn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */