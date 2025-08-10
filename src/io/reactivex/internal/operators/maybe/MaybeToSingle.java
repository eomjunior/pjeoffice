/*    */ package io.reactivex.internal.operators.maybe;
/*    */ 
/*    */ import io.reactivex.MaybeObserver;
/*    */ import io.reactivex.MaybeSource;
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;
/*    */ import java.util.NoSuchElementException;
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
/*    */ public final class MaybeToSingle<T>
/*    */   extends Single<T>
/*    */   implements HasUpstreamMaybeSource<T>
/*    */ {
/*    */   final MaybeSource<T> source;
/*    */   final T defaultValue;
/*    */   
/*    */   public MaybeToSingle(MaybeSource<T> source, T defaultValue) {
/* 35 */     this.source = source;
/* 36 */     this.defaultValue = defaultValue;
/*    */   }
/*    */ 
/*    */   
/*    */   public MaybeSource<T> source() {
/* 41 */     return this.source;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super T> observer) {
/* 46 */     this.source.subscribe(new ToSingleMaybeSubscriber<T>(observer, this.defaultValue));
/*    */   }
/*    */   
/*    */   static final class ToSingleMaybeSubscriber<T>
/*    */     implements MaybeObserver<T>, Disposable {
/*    */     final SingleObserver<? super T> downstream;
/*    */     final T defaultValue;
/*    */     Disposable upstream;
/*    */     
/*    */     ToSingleMaybeSubscriber(SingleObserver<? super T> actual, T defaultValue) {
/* 56 */       this.downstream = actual;
/* 57 */       this.defaultValue = defaultValue;
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 62 */       this.upstream.dispose();
/* 63 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 68 */       return this.upstream.isDisposed();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 73 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 74 */         this.upstream = d;
/*    */         
/* 76 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(T value) {
/* 82 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 83 */       this.downstream.onSuccess(value);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 88 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 89 */       this.downstream.onError(e);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 94 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 95 */       if (this.defaultValue != null) {
/* 96 */         this.downstream.onSuccess(this.defaultValue);
/*    */       } else {
/* 98 */         this.downstream.onError(new NoSuchElementException("The MaybeSource is empty"));
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeToSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */