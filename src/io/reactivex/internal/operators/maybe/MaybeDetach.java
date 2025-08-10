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
/*    */ public final class MaybeDetach<T>
/*    */   extends AbstractMaybeWithUpstream<T, T>
/*    */ {
/*    */   public MaybeDetach(MaybeSource<T> source) {
/* 28 */     super(source);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/* 33 */     this.source.subscribe(new DetachMaybeObserver<T>(observer));
/*    */   }
/*    */   
/*    */   static final class DetachMaybeObserver<T>
/*    */     implements MaybeObserver<T>, Disposable
/*    */   {
/*    */     MaybeObserver<? super T> downstream;
/*    */     Disposable upstream;
/*    */     
/*    */     DetachMaybeObserver(MaybeObserver<? super T> downstream) {
/* 43 */       this.downstream = downstream;
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 48 */       this.downstream = null;
/* 49 */       this.upstream.dispose();
/* 50 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
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
/* 69 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 70 */       MaybeObserver<? super T> a = this.downstream;
/* 71 */       if (a != null) {
/* 72 */         this.downstream = null;
/* 73 */         a.onSuccess(value);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 79 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 80 */       MaybeObserver<? super T> a = this.downstream;
/* 81 */       if (a != null) {
/* 82 */         this.downstream = null;
/* 83 */         a.onError(e);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 89 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 90 */       MaybeObserver<? super T> a = this.downstream;
/* 91 */       if (a != null) {
/* 92 */         this.downstream = null;
/* 93 */         a.onComplete();
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeDetach.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */