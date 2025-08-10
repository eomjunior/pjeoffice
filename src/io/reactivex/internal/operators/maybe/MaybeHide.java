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
/*    */ public final class MaybeHide<T>
/*    */   extends AbstractMaybeWithUpstream<T, T>
/*    */ {
/*    */   public MaybeHide(MaybeSource<T> source) {
/* 28 */     super(source);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/* 33 */     this.source.subscribe(new HideMaybeObserver<T>(observer));
/*    */   }
/*    */   
/*    */   static final class HideMaybeObserver<T>
/*    */     implements MaybeObserver<T>, Disposable
/*    */   {
/*    */     final MaybeObserver<? super T> downstream;
/*    */     Disposable upstream;
/*    */     
/*    */     HideMaybeObserver(MaybeObserver<? super T> downstream) {
/* 43 */       this.downstream = downstream;
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 48 */       this.upstream.dispose();
/* 49 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 54 */       return this.upstream.isDisposed();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 59 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 60 */         this.upstream = d;
/*    */         
/* 62 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(T value) {
/* 68 */       this.downstream.onSuccess(value);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 73 */       this.downstream.onError(e);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 78 */       this.downstream.onComplete();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeHide.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */