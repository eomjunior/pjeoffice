/*    */ package io.reactivex.internal.operators.flowable;
/*    */ 
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.FlowableSubscriber;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.functions.Predicate;
/*    */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
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
/*    */ public final class FlowableSkipWhile<T>
/*    */   extends AbstractFlowableWithUpstream<T, T>
/*    */ {
/*    */   final Predicate<? super T> predicate;
/*    */   
/*    */   public FlowableSkipWhile(Flowable<T> source, Predicate<? super T> predicate) {
/* 26 */     super(source);
/* 27 */     this.predicate = predicate;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Subscriber<? super T> s) {
/* 32 */     this.source.subscribe(new SkipWhileSubscriber<T>(s, this.predicate));
/*    */   }
/*    */   
/*    */   static final class SkipWhileSubscriber<T>
/*    */     implements FlowableSubscriber<T>, Subscription {
/*    */     final Subscriber<? super T> downstream;
/*    */     final Predicate<? super T> predicate;
/*    */     
/*    */     SkipWhileSubscriber(Subscriber<? super T> actual, Predicate<? super T> predicate) {
/* 41 */       this.downstream = actual;
/* 42 */       this.predicate = predicate;
/*    */     }
/*    */     Subscription upstream; boolean notSkipping;
/*    */     
/*    */     public void onSubscribe(Subscription s) {
/* 47 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 48 */         this.upstream = s;
/* 49 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(T t) {
/* 55 */       if (this.notSkipping) {
/* 56 */         this.downstream.onNext(t);
/*    */       } else {
/*    */         boolean b;
/*    */         try {
/* 60 */           b = this.predicate.test(t);
/* 61 */         } catch (Throwable e) {
/* 62 */           Exceptions.throwIfFatal(e);
/* 63 */           this.upstream.cancel();
/* 64 */           this.downstream.onError(e);
/*    */           return;
/*    */         } 
/* 67 */         if (b) {
/* 68 */           this.upstream.request(1L);
/*    */         } else {
/* 70 */           this.notSkipping = true;
/* 71 */           this.downstream.onNext(t);
/*    */         } 
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 78 */       this.downstream.onError(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 83 */       this.downstream.onComplete();
/*    */     }
/*    */ 
/*    */     
/*    */     public void request(long n) {
/* 88 */       this.upstream.request(n);
/*    */     }
/*    */ 
/*    */     
/*    */     public void cancel() {
/* 93 */       this.upstream.cancel();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableSkipWhile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */