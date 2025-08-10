/*    */ package io.reactivex.internal.operators.flowable;
/*    */ 
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.functions.Function;
/*    */ import io.reactivex.internal.functions.ObjectHelper;
/*    */ import io.reactivex.internal.subscriptions.EmptySubscription;
/*    */ import io.reactivex.processors.FlowableProcessor;
/*    */ import io.reactivex.processors.UnicastProcessor;
/*    */ import io.reactivex.subscribers.SerializedSubscriber;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class FlowableRetryWhen<T>
/*    */   extends AbstractFlowableWithUpstream<T, T>
/*    */ {
/*    */   final Function<? super Flowable<Throwable>, ? extends Publisher<?>> handler;
/*    */   
/*    */   public FlowableRetryWhen(Flowable<T> source, Function<? super Flowable<Throwable>, ? extends Publisher<?>> handler) {
/* 32 */     super(source);
/* 33 */     this.handler = handler;
/*    */   }
/*    */   
/*    */   public void subscribeActual(Subscriber<? super T> s) {
/*    */     Publisher<?> when;
/* 38 */     SerializedSubscriber<T> z = new SerializedSubscriber(s);
/*    */     
/* 40 */     FlowableProcessor<Throwable> processor = UnicastProcessor.create(8).toSerialized();
/*    */ 
/*    */ 
/*    */     
/*    */     try {
/* 45 */       when = (Publisher)ObjectHelper.requireNonNull(this.handler.apply(processor), "handler returned a null Publisher");
/* 46 */     } catch (Throwable ex) {
/* 47 */       Exceptions.throwIfFatal(ex);
/* 48 */       EmptySubscription.error(ex, s);
/*    */       
/*    */       return;
/*    */     } 
/* 52 */     FlowableRepeatWhen.WhenReceiver<T, Throwable> receiver = new FlowableRepeatWhen.WhenReceiver<T, Throwable>((Publisher<T>)this.source);
/*    */     
/* 54 */     RetryWhenSubscriber<T> subscriber = new RetryWhenSubscriber<T>((Subscriber<? super T>)z, processor, receiver);
/*    */     
/* 56 */     receiver.subscriber = subscriber;
/*    */     
/* 58 */     s.onSubscribe((Subscription)subscriber);
/*    */     
/* 60 */     when.subscribe((Subscriber)receiver);
/*    */     
/* 62 */     receiver.onNext(Integer.valueOf(0));
/*    */   }
/*    */   
/*    */   static final class RetryWhenSubscriber<T>
/*    */     extends FlowableRepeatWhen.WhenSourceSubscriber<T, Throwable>
/*    */   {
/*    */     private static final long serialVersionUID = -2680129890138081029L;
/*    */     
/*    */     RetryWhenSubscriber(Subscriber<? super T> actual, FlowableProcessor<Throwable> processor, Subscription receiver) {
/* 71 */       super(actual, processor, receiver);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 76 */       again(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 81 */       this.receiver.cancel();
/* 82 */       this.downstream.onComplete();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableRetryWhen.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */