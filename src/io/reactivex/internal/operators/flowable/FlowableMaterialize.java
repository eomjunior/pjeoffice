/*    */ package io.reactivex.internal.operators.flowable;
/*    */ 
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.FlowableSubscriber;
/*    */ import io.reactivex.Notification;
/*    */ import io.reactivex.internal.subscribers.SinglePostCompleteSubscriber;
/*    */ import io.reactivex.plugins.RxJavaPlugins;
/*    */ import org.reactivestreams.Subscriber;
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
/*    */ public final class FlowableMaterialize<T>
/*    */   extends AbstractFlowableWithUpstream<T, Notification<T>>
/*    */ {
/*    */   public FlowableMaterialize(Flowable<T> source) {
/* 25 */     super(source);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Subscriber<? super Notification<T>> s) {
/* 30 */     this.source.subscribe((FlowableSubscriber)new MaterializeSubscriber<T>(s));
/*    */   }
/*    */   
/*    */   static final class MaterializeSubscriber<T>
/*    */     extends SinglePostCompleteSubscriber<T, Notification<T>> {
/*    */     private static final long serialVersionUID = -3740826063558713822L;
/*    */     
/*    */     MaterializeSubscriber(Subscriber<? super Notification<T>> downstream) {
/* 38 */       super(downstream);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(T t) {
/* 43 */       this.produced++;
/* 44 */       this.downstream.onNext(Notification.createOnNext(t));
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 49 */       complete(Notification.createOnError(t));
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 54 */       complete(Notification.createOnComplete());
/*    */     }
/*    */ 
/*    */     
/*    */     protected void onDrop(Notification<T> n) {
/* 59 */       if (n.isOnError())
/* 60 */         RxJavaPlugins.onError(n.getError()); 
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableMaterialize.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */