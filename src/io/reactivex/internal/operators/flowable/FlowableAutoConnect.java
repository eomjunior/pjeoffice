/*    */ package io.reactivex.internal.operators.flowable;
/*    */ 
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.flowables.ConnectableFlowable;
/*    */ import io.reactivex.functions.Consumer;
/*    */ import java.util.concurrent.atomic.AtomicInteger;
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
/*    */ public final class FlowableAutoConnect<T>
/*    */   extends Flowable<T>
/*    */ {
/*    */   final ConnectableFlowable<? extends T> source;
/*    */   final int numberOfSubscribers;
/*    */   final Consumer<? super Disposable> connection;
/*    */   final AtomicInteger clients;
/*    */   
/*    */   public FlowableAutoConnect(ConnectableFlowable<? extends T> source, int numberOfSubscribers, Consumer<? super Disposable> connection) {
/* 40 */     this.source = source;
/* 41 */     this.numberOfSubscribers = numberOfSubscribers;
/* 42 */     this.connection = connection;
/* 43 */     this.clients = new AtomicInteger();
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(Subscriber<? super T> child) {
/* 48 */     this.source.subscribe(child);
/* 49 */     if (this.clients.incrementAndGet() == this.numberOfSubscribers)
/* 50 */       this.source.connect(this.connection); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableAutoConnect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */