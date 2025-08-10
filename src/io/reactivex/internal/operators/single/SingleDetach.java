/*    */ package io.reactivex.internal.operators.single;
/*    */ 
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.SingleSource;
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
/*    */ 
/*    */ public final class SingleDetach<T>
/*    */   extends Single<T>
/*    */ {
/*    */   final SingleSource<T> source;
/*    */   
/*    */   public SingleDetach(SingleSource<T> source) {
/* 31 */     this.source = source;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super T> observer) {
/* 36 */     this.source.subscribe(new DetachSingleObserver<T>(observer));
/*    */   }
/*    */   
/*    */   static final class DetachSingleObserver<T>
/*    */     implements SingleObserver<T>, Disposable
/*    */   {
/*    */     SingleObserver<? super T> downstream;
/*    */     Disposable upstream;
/*    */     
/*    */     DetachSingleObserver(SingleObserver<? super T> downstream) {
/* 46 */       this.downstream = downstream;
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 51 */       this.downstream = null;
/* 52 */       this.upstream.dispose();
/* 53 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 58 */       return this.upstream.isDisposed();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 63 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 64 */         this.upstream = d;
/*    */         
/* 66 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(T value) {
/* 72 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 73 */       SingleObserver<? super T> a = this.downstream;
/* 74 */       if (a != null) {
/* 75 */         this.downstream = null;
/* 76 */         a.onSuccess(value);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 82 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 83 */       SingleObserver<? super T> a = this.downstream;
/* 84 */       if (a != null) {
/* 85 */         this.downstream = null;
/* 86 */         a.onError(e);
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleDetach.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */