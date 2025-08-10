/*    */ package io.reactivex.internal.operators.maybe;
/*    */ 
/*    */ import io.reactivex.MaybeObserver;
/*    */ import io.reactivex.MaybeSource;
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
/*    */ public final class MaybeIgnoreElement<T>
/*    */   extends AbstractMaybeWithUpstream<T, T>
/*    */ {
/*    */   public MaybeIgnoreElement(MaybeSource<T> source) {
/* 28 */     super(source);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/* 33 */     this.source.subscribe(new IgnoreMaybeObserver<T>(observer));
/*    */   }
/*    */   
/*    */   static final class IgnoreMaybeObserver<T>
/*    */     implements MaybeObserver<T>, Disposable
/*    */   {
/*    */     final MaybeObserver<? super T> downstream;
/*    */     Disposable upstream;
/*    */     
/*    */     IgnoreMaybeObserver(MaybeObserver<? super T> downstream) {
/* 43 */       this.downstream = downstream;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 48 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 49 */         this.upstream = d;
/*    */         
/* 51 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(T value) {
/* 57 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 58 */       this.downstream.onComplete();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 63 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 64 */       this.downstream.onError(e);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 69 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 70 */       this.downstream.onComplete();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 75 */       return this.upstream.isDisposed();
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 80 */       this.upstream.dispose();
/* 81 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeIgnoreElement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */