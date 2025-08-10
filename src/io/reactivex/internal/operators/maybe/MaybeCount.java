/*    */ package io.reactivex.internal.operators.maybe;
/*    */ 
/*    */ import io.reactivex.MaybeObserver;
/*    */ import io.reactivex.MaybeSource;
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;
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
/*    */ public final class MaybeCount<T>
/*    */   extends Single<Long>
/*    */   implements HasUpstreamMaybeSource<T>
/*    */ {
/*    */   final MaybeSource<T> source;
/*    */   
/*    */   public MaybeCount(MaybeSource<T> source) {
/* 31 */     this.source = source;
/*    */   }
/*    */ 
/*    */   
/*    */   public MaybeSource<T> source() {
/* 36 */     return this.source;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super Long> observer) {
/* 41 */     this.source.subscribe(new CountMaybeObserver(observer));
/*    */   }
/*    */   
/*    */   static final class CountMaybeObserver
/*    */     implements MaybeObserver<Object>, Disposable {
/*    */     final SingleObserver<? super Long> downstream;
/*    */     Disposable upstream;
/*    */     
/*    */     CountMaybeObserver(SingleObserver<? super Long> downstream) {
/* 50 */       this.downstream = downstream;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 55 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 56 */         this.upstream = d;
/*    */         
/* 58 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(Object value) {
/* 64 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 65 */       this.downstream.onSuccess(Long.valueOf(1L));
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 70 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 71 */       this.downstream.onError(e);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 76 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 77 */       this.downstream.onSuccess(Long.valueOf(0L));
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 82 */       return this.upstream.isDisposed();
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 87 */       this.upstream.dispose();
/* 88 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeCount.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */