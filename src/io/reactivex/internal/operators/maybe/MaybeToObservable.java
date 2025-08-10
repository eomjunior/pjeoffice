/*    */ package io.reactivex.internal.operators.maybe;
/*    */ 
/*    */ import io.reactivex.MaybeObserver;
/*    */ import io.reactivex.MaybeSource;
/*    */ import io.reactivex.Observable;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;
/*    */ import io.reactivex.internal.observers.DeferredScalarDisposable;
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
/*    */ public final class MaybeToObservable<T>
/*    */   extends Observable<T>
/*    */   implements HasUpstreamMaybeSource<T>
/*    */ {
/*    */   final MaybeSource<T> source;
/*    */   
/*    */   public MaybeToObservable(MaybeSource<T> source) {
/* 33 */     this.source = source;
/*    */   }
/*    */ 
/*    */   
/*    */   public MaybeSource<T> source() {
/* 38 */     return this.source;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Observer<? super T> observer) {
/* 43 */     this.source.subscribe(create(observer));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> MaybeObserver<T> create(Observer<? super T> downstream) {
/* 55 */     return new MaybeToObservableObserver<T>(downstream);
/*    */   }
/*    */   
/*    */   static final class MaybeToObservableObserver<T>
/*    */     extends DeferredScalarDisposable<T>
/*    */     implements MaybeObserver<T>
/*    */   {
/*    */     private static final long serialVersionUID = 7603343402964826922L;
/*    */     Disposable upstream;
/*    */     
/*    */     MaybeToObservableObserver(Observer<? super T> downstream) {
/* 66 */       super(downstream);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 71 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 72 */         this.upstream = d;
/*    */         
/* 74 */         this.downstream.onSubscribe((Disposable)this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(T value) {
/* 80 */       complete(value);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 85 */       error(e);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 90 */       complete();
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 95 */       super.dispose();
/* 96 */       this.upstream.dispose();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeToObservable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */