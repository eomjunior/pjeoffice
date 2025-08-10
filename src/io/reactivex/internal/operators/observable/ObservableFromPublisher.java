/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.FlowableSubscriber;
/*    */ import io.reactivex.Observable;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*    */ import org.reactivestreams.Publisher;
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
/*    */ public final class ObservableFromPublisher<T>
/*    */   extends Observable<T>
/*    */ {
/*    */   final Publisher<? extends T> source;
/*    */   
/*    */   public ObservableFromPublisher(Publisher<? extends T> publisher) {
/* 26 */     this.source = publisher;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Observer<? super T> o) {
/* 31 */     this.source.subscribe((Subscriber)new PublisherSubscriber<T>(o));
/*    */   }
/*    */   
/*    */   static final class PublisherSubscriber<T>
/*    */     implements FlowableSubscriber<T>, Disposable
/*    */   {
/*    */     final Observer<? super T> downstream;
/*    */     Subscription upstream;
/*    */     
/*    */     PublisherSubscriber(Observer<? super T> o) {
/* 41 */       this.downstream = o;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 46 */       this.downstream.onComplete();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 51 */       this.downstream.onError(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(T t) {
/* 56 */       this.downstream.onNext(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Subscription s) {
/* 61 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 62 */         this.upstream = s;
/* 63 */         this.downstream.onSubscribe(this);
/* 64 */         s.request(Long.MAX_VALUE);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 70 */       this.upstream.cancel();
/* 71 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 76 */       return (this.upstream == SubscriptionHelper.CANCELLED);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableFromPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */