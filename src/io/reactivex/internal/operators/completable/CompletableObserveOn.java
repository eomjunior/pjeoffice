/*    */ package io.reactivex.internal.operators.completable;
/*    */ 
/*    */ import io.reactivex.Completable;
/*    */ import io.reactivex.CompletableObserver;
/*    */ import io.reactivex.CompletableSource;
/*    */ import io.reactivex.Scheduler;
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
/*    */ public final class CompletableObserveOn
/*    */   extends Completable
/*    */ {
/*    */   final CompletableSource source;
/*    */   final Scheduler scheduler;
/*    */   
/*    */   public CompletableObserveOn(CompletableSource source, Scheduler scheduler) {
/* 28 */     this.source = source;
/* 29 */     this.scheduler = scheduler;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(CompletableObserver observer) {
/* 34 */     this.source.subscribe(new ObserveOnCompletableObserver(observer, this.scheduler));
/*    */   }
/*    */ 
/*    */   
/*    */   static final class ObserveOnCompletableObserver
/*    */     extends AtomicReference<Disposable>
/*    */     implements CompletableObserver, Disposable, Runnable
/*    */   {
/*    */     private static final long serialVersionUID = 8571289934935992137L;
/*    */     
/*    */     final CompletableObserver downstream;
/*    */     
/*    */     final Scheduler scheduler;
/*    */     Throwable error;
/*    */     
/*    */     ObserveOnCompletableObserver(CompletableObserver actual, Scheduler scheduler) {
/* 50 */       this.downstream = actual;
/* 51 */       this.scheduler = scheduler;
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 56 */       DisposableHelper.dispose(this);
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 61 */       return DisposableHelper.isDisposed(get());
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 66 */       if (DisposableHelper.setOnce(this, d)) {
/* 67 */         this.downstream.onSubscribe(this);
/*    */       }
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 73 */       this.error = e;
/* 74 */       DisposableHelper.replace(this, this.scheduler.scheduleDirect(this));
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 79 */       DisposableHelper.replace(this, this.scheduler.scheduleDirect(this));
/*    */     }
/*    */ 
/*    */     
/*    */     public void run() {
/* 84 */       Throwable ex = this.error;
/* 85 */       if (ex != null) {
/* 86 */         this.error = null;
/* 87 */         this.downstream.onError(ex);
/*    */       } else {
/* 89 */         this.downstream.onComplete();
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableObserveOn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */