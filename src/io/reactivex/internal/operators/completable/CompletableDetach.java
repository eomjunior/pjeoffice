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
/*    */ public final class CompletableDetach
/*    */   extends Completable
/*    */ {
/*    */   final CompletableSource source;
/*    */   
/*    */   public CompletableDetach(CompletableSource source) {
/* 30 */     this.source = source;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(CompletableObserver observer) {
/* 35 */     this.source.subscribe(new DetachCompletableObserver(observer));
/*    */   }
/*    */   
/*    */   static final class DetachCompletableObserver
/*    */     implements CompletableObserver, Disposable
/*    */   {
/*    */     CompletableObserver downstream;
/*    */     Disposable upstream;
/*    */     
/*    */     DetachCompletableObserver(CompletableObserver downstream) {
/* 45 */       this.downstream = downstream;
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 50 */       this.downstream = null;
/* 51 */       this.upstream.dispose();
/* 52 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 57 */       return this.upstream.isDisposed();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 62 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 63 */         this.upstream = d;
/*    */         
/* 65 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 71 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 72 */       CompletableObserver a = this.downstream;
/* 73 */       if (a != null) {
/* 74 */         this.downstream = null;
/* 75 */         a.onError(e);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 81 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 82 */       CompletableObserver a = this.downstream;
/* 83 */       if (a != null) {
/* 84 */         this.downstream = null;
/* 85 */         a.onComplete();
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableDetach.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */