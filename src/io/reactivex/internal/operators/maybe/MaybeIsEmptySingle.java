/*    */ package io.reactivex.internal.operators.maybe;
/*    */ 
/*    */ import io.reactivex.Maybe;
/*    */ import io.reactivex.MaybeObserver;
/*    */ import io.reactivex.MaybeSource;
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import io.reactivex.internal.fuseable.FuseToMaybe;
/*    */ import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class MaybeIsEmptySingle<T>
/*    */   extends Single<Boolean>
/*    */   implements HasUpstreamMaybeSource<T>, FuseToMaybe<Boolean>
/*    */ {
/*    */   final MaybeSource<T> source;
/*    */   
/*    */   public MaybeIsEmptySingle(MaybeSource<T> source) {
/* 34 */     this.source = source;
/*    */   }
/*    */ 
/*    */   
/*    */   public MaybeSource<T> source() {
/* 39 */     return this.source;
/*    */   }
/*    */ 
/*    */   
/*    */   public Maybe<Boolean> fuseToMaybe() {
/* 44 */     return RxJavaPlugins.onAssembly(new MaybeIsEmpty<T>(this.source));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super Boolean> observer) {
/* 49 */     this.source.subscribe(new IsEmptyMaybeObserver(observer));
/*    */   }
/*    */ 
/*    */   
/*    */   static final class IsEmptyMaybeObserver<T>
/*    */     implements MaybeObserver<T>, Disposable
/*    */   {
/*    */     final SingleObserver<? super Boolean> downstream;
/*    */     Disposable upstream;
/*    */     
/*    */     IsEmptyMaybeObserver(SingleObserver<? super Boolean> downstream) {
/* 60 */       this.downstream = downstream;
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 65 */       this.upstream.dispose();
/* 66 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 71 */       return this.upstream.isDisposed();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 76 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 77 */         this.upstream = d;
/*    */         
/* 79 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(T value) {
/* 85 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 86 */       this.downstream.onSuccess(Boolean.valueOf(false));
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 91 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 92 */       this.downstream.onError(e);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 97 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 98 */       this.downstream.onSuccess(Boolean.valueOf(true));
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeIsEmptySingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */