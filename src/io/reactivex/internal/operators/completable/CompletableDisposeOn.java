/*    */ package io.reactivex.internal.operators.completable;
/*    */ 
/*    */ import io.reactivex.Completable;
/*    */ import io.reactivex.CompletableObserver;
/*    */ import io.reactivex.CompletableSource;
/*    */ import io.reactivex.Scheduler;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import io.reactivex.plugins.RxJavaPlugins;
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
/*    */ public final class CompletableDisposeOn
/*    */   extends Completable
/*    */ {
/*    */   final CompletableSource source;
/*    */   final Scheduler scheduler;
/*    */   
/*    */   public CompletableDisposeOn(CompletableSource source, Scheduler scheduler) {
/* 28 */     this.source = source;
/* 29 */     this.scheduler = scheduler;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(CompletableObserver observer) {
/* 34 */     this.source.subscribe(new DisposeOnObserver(observer, this.scheduler));
/*    */   }
/*    */ 
/*    */   
/*    */   static final class DisposeOnObserver
/*    */     implements CompletableObserver, Disposable, Runnable
/*    */   {
/*    */     final CompletableObserver downstream;
/*    */     final Scheduler scheduler;
/*    */     Disposable upstream;
/*    */     volatile boolean disposed;
/*    */     
/*    */     DisposeOnObserver(CompletableObserver observer, Scheduler scheduler) {
/* 47 */       this.downstream = observer;
/* 48 */       this.scheduler = scheduler;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 53 */       if (this.disposed) {
/*    */         return;
/*    */       }
/* 56 */       this.downstream.onComplete();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 61 */       if (this.disposed) {
/* 62 */         RxJavaPlugins.onError(e);
/*    */         return;
/*    */       } 
/* 65 */       this.downstream.onError(e);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 70 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 71 */         this.upstream = d;
/*    */         
/* 73 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 79 */       this.disposed = true;
/* 80 */       this.scheduler.scheduleDirect(this);
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 85 */       return this.disposed;
/*    */     }
/*    */ 
/*    */     
/*    */     public void run() {
/* 90 */       this.upstream.dispose();
/* 91 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableDisposeOn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */