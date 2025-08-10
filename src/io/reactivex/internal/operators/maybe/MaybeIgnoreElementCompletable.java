/*    */ package io.reactivex.internal.operators.maybe;
/*    */ 
/*    */ import io.reactivex.Completable;
/*    */ import io.reactivex.CompletableObserver;
/*    */ import io.reactivex.Maybe;
/*    */ import io.reactivex.MaybeObserver;
/*    */ import io.reactivex.MaybeSource;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import io.reactivex.internal.fuseable.FuseToMaybe;
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
/*    */ public final class MaybeIgnoreElementCompletable<T>
/*    */   extends Completable
/*    */   implements FuseToMaybe<T>
/*    */ {
/*    */   final MaybeSource<T> source;
/*    */   
/*    */   public MaybeIgnoreElementCompletable(MaybeSource<T> source) {
/* 32 */     this.source = source;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(CompletableObserver observer) {
/* 37 */     this.source.subscribe(new IgnoreMaybeObserver(observer));
/*    */   }
/*    */ 
/*    */   
/*    */   public Maybe<T> fuseToMaybe() {
/* 42 */     return RxJavaPlugins.onAssembly(new MaybeIgnoreElement<T>(this.source));
/*    */   }
/*    */   
/*    */   static final class IgnoreMaybeObserver<T>
/*    */     implements MaybeObserver<T>, Disposable
/*    */   {
/*    */     final CompletableObserver downstream;
/*    */     Disposable upstream;
/*    */     
/*    */     IgnoreMaybeObserver(CompletableObserver downstream) {
/* 52 */       this.downstream = downstream;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 57 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 58 */         this.upstream = d;
/*    */         
/* 60 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(T value) {
/* 66 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 67 */       this.downstream.onComplete();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 72 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 73 */       this.downstream.onError(e);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 78 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 79 */       this.downstream.onComplete();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 84 */       return this.upstream.isDisposed();
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 89 */       this.upstream.dispose();
/* 90 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeIgnoreElementCompletable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */