/*    */ package io.reactivex.internal.operators.single;
/*    */ 
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.SingleSource;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
/*    */ import org.reactivestreams.Subscriber;
/*    */ import org.reactivestreams.Subscription;
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
/*    */ public final class SingleToFlowable<T>
/*    */   extends Flowable<T>
/*    */ {
/*    */   final SingleSource<? extends T> source;
/*    */   
/*    */   public SingleToFlowable(SingleSource<? extends T> source) {
/* 32 */     this.source = source;
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(Subscriber<? super T> s) {
/* 37 */     this.source.subscribe(new SingleToFlowableObserver<T>(s));
/*    */   }
/*    */   
/*    */   static final class SingleToFlowableObserver<T>
/*    */     extends DeferredScalarSubscription<T>
/*    */     implements SingleObserver<T>
/*    */   {
/*    */     private static final long serialVersionUID = 187782011903685568L;
/*    */     Disposable upstream;
/*    */     
/*    */     SingleToFlowableObserver(Subscriber<? super T> downstream) {
/* 48 */       super(downstream);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 53 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 54 */         this.upstream = d;
/*    */         
/* 56 */         this.downstream.onSubscribe((Subscription)this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(T value) {
/* 62 */       complete(value);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 67 */       this.downstream.onError(e);
/*    */     }
/*    */ 
/*    */     
/*    */     public void cancel() {
/* 72 */       super.cancel();
/* 73 */       this.upstream.dispose();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleToFlowable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */