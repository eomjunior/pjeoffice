/*    */ package io.reactivex.internal.operators.flowable;
/*    */ 
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.FlowableSubscriber;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.SingleSource;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import io.reactivex.internal.subscribers.SinglePostCompleteSubscriber;
/*    */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*    */ import java.util.concurrent.atomic.AtomicReference;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class FlowableConcatWithSingle<T>
/*    */   extends AbstractFlowableWithUpstream<T, T>
/*    */ {
/*    */   final SingleSource<? extends T> other;
/*    */   
/*    */   public FlowableConcatWithSingle(Flowable<T> source, SingleSource<? extends T> other) {
/* 38 */     super(source);
/* 39 */     this.other = other;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Subscriber<? super T> s) {
/* 44 */     this.source.subscribe((FlowableSubscriber)new ConcatWithSubscriber<T>(s, this.other));
/*    */   }
/*    */ 
/*    */   
/*    */   static final class ConcatWithSubscriber<T>
/*    */     extends SinglePostCompleteSubscriber<T, T>
/*    */     implements SingleObserver<T>
/*    */   {
/*    */     private static final long serialVersionUID = -7346385463600070225L;
/*    */     
/*    */     final AtomicReference<Disposable> otherDisposable;
/*    */     SingleSource<? extends T> other;
/*    */     
/*    */     ConcatWithSubscriber(Subscriber<? super T> actual, SingleSource<? extends T> other) {
/* 58 */       super(actual);
/* 59 */       this.other = other;
/* 60 */       this.otherDisposable = new AtomicReference<Disposable>();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 65 */       DisposableHelper.setOnce(this.otherDisposable, d);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(T t) {
/* 70 */       this.produced++;
/* 71 */       this.downstream.onNext(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 76 */       this.downstream.onError(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(T t) {
/* 81 */       complete(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 86 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/* 87 */       SingleSource<? extends T> ss = this.other;
/* 88 */       this.other = null;
/* 89 */       ss.subscribe(this);
/*    */     }
/*    */ 
/*    */     
/*    */     public void cancel() {
/* 94 */       super.cancel();
/* 95 */       DisposableHelper.dispose(this.otherDisposable);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableConcatWithSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */