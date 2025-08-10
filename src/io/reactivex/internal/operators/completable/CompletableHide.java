/*    */ package io.reactivex.internal.operators.completable;
/*    */ 
/*    */ import io.reactivex.Completable;
/*    */ import io.reactivex.CompletableObserver;
/*    */ import io.reactivex.CompletableSource;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class CompletableHide
/*    */   extends Completable
/*    */ {
/*    */   final CompletableSource source;
/*    */   
/*    */   public CompletableHide(CompletableSource source) {
/* 30 */     this.source = source;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(CompletableObserver observer) {
/* 35 */     this.source.subscribe(new HideCompletableObserver(observer));
/*    */   }
/*    */   
/*    */   static final class HideCompletableObserver
/*    */     implements CompletableObserver, Disposable
/*    */   {
/*    */     final CompletableObserver downstream;
/*    */     Disposable upstream;
/*    */     
/*    */     HideCompletableObserver(CompletableObserver downstream) {
/* 45 */       this.downstream = downstream;
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 50 */       this.upstream.dispose();
/* 51 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 56 */       return this.upstream.isDisposed();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 61 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 62 */         this.upstream = d;
/*    */         
/* 64 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 70 */       this.downstream.onError(e);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 75 */       this.downstream.onComplete();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableHide.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */