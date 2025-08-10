/*    */ package io.reactivex.internal.operators.maybe;
/*    */ 
/*    */ import io.reactivex.Maybe;
/*    */ import io.reactivex.MaybeObserver;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.SingleSource;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import io.reactivex.internal.fuseable.HasUpstreamSingleSource;
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
/*    */ public final class MaybeFromSingle<T>
/*    */   extends Maybe<T>
/*    */   implements HasUpstreamSingleSource<T>
/*    */ {
/*    */   final SingleSource<T> source;
/*    */   
/*    */   public MaybeFromSingle(SingleSource<T> source) {
/* 31 */     this.source = source;
/*    */   }
/*    */ 
/*    */   
/*    */   public SingleSource<T> source() {
/* 36 */     return this.source;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/* 41 */     this.source.subscribe(new FromSingleObserver<T>(observer));
/*    */   }
/*    */   
/*    */   static final class FromSingleObserver<T>
/*    */     implements SingleObserver<T>, Disposable {
/*    */     final MaybeObserver<? super T> downstream;
/*    */     Disposable upstream;
/*    */     
/*    */     FromSingleObserver(MaybeObserver<? super T> downstream) {
/* 50 */       this.downstream = downstream;
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 55 */       this.upstream.dispose();
/* 56 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 61 */       return this.upstream.isDisposed();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 66 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 67 */         this.upstream = d;
/*    */         
/* 69 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(T value) {
/* 75 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 76 */       this.downstream.onSuccess(value);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 81 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 82 */       this.downstream.onError(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeFromSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */