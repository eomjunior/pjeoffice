/*    */ package io.reactivex.internal.operators.maybe;
/*    */ 
/*    */ import io.reactivex.MaybeObserver;
/*    */ import io.reactivex.MaybeSource;
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import io.reactivex.internal.functions.ObjectHelper;
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
/*    */ 
/*    */ 
/*    */ public final class MaybeContains<T>
/*    */   extends Single<Boolean>
/*    */   implements HasUpstreamMaybeSource<T>
/*    */ {
/*    */   final MaybeSource<T> source;
/*    */   final Object value;
/*    */   
/*    */   public MaybeContains(MaybeSource<T> source, Object value) {
/* 35 */     this.source = source;
/* 36 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public MaybeSource<T> source() {
/* 41 */     return this.source;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super Boolean> observer) {
/* 46 */     this.source.subscribe(new ContainsMaybeObserver(observer, this.value));
/*    */   }
/*    */ 
/*    */   
/*    */   static final class ContainsMaybeObserver
/*    */     implements MaybeObserver<Object>, Disposable
/*    */   {
/*    */     final SingleObserver<? super Boolean> downstream;
/*    */     final Object value;
/*    */     Disposable upstream;
/*    */     
/*    */     ContainsMaybeObserver(SingleObserver<? super Boolean> actual, Object value) {
/* 58 */       this.downstream = actual;
/* 59 */       this.value = value;
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 64 */       this.upstream.dispose();
/* 65 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 70 */       return this.upstream.isDisposed();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 75 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 76 */         this.upstream = d;
/* 77 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(Object value) {
/* 83 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 84 */       this.downstream.onSuccess(Boolean.valueOf(ObjectHelper.equals(value, this.value)));
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 89 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 90 */       this.downstream.onError(e);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 95 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 96 */       this.downstream.onSuccess(Boolean.valueOf(false));
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeContains.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */