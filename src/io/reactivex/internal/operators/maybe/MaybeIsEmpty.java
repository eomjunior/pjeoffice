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
/*    */ 
/*    */ public final class MaybeIsEmpty<T>
/*    */   extends AbstractMaybeWithUpstream<T, Boolean>
/*    */ {
/*    */   public MaybeIsEmpty(MaybeSource<T> source) {
/* 29 */     super(source);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(MaybeObserver<? super Boolean> observer) {
/* 34 */     this.source.subscribe(new IsEmptyMaybeObserver(observer));
/*    */   }
/*    */ 
/*    */   
/*    */   static final class IsEmptyMaybeObserver<T>
/*    */     implements MaybeObserver<T>, Disposable
/*    */   {
/*    */     final MaybeObserver<? super Boolean> downstream;
/*    */     Disposable upstream;
/*    */     
/*    */     IsEmptyMaybeObserver(MaybeObserver<? super Boolean> downstream) {
/* 45 */       this.downstream = downstream;
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 50 */       this.upstream.dispose();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 55 */       return this.upstream.isDisposed();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 60 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 61 */         this.upstream = d;
/*    */         
/* 63 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(T value) {
/* 69 */       this.downstream.onSuccess(Boolean.valueOf(false));
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 74 */       this.downstream.onError(e);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 79 */       this.downstream.onSuccess(Boolean.valueOf(true));
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeIsEmpty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */