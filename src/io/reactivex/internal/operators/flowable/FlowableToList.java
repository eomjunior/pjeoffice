/*    */ package io.reactivex.internal.operators.flowable;
/*    */ 
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.FlowableSubscriber;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.internal.functions.ObjectHelper;
/*    */ import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
/*    */ import io.reactivex.internal.subscriptions.EmptySubscription;
/*    */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*    */ import java.util.Collection;
/*    */ import java.util.concurrent.Callable;
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
/*    */ public final class FlowableToList<T, U extends Collection<? super T>>
/*    */   extends AbstractFlowableWithUpstream<T, U>
/*    */ {
/*    */   final Callable<U> collectionSupplier;
/*    */   
/*    */   public FlowableToList(Flowable<T> source, Callable<U> collectionSupplier) {
/* 30 */     super(source);
/* 31 */     this.collectionSupplier = collectionSupplier;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Subscriber<? super U> s) {
/*    */     Collection collection;
/*    */     try {
/* 38 */       collection = (Collection)ObjectHelper.requireNonNull(this.collectionSupplier.call(), "The collectionSupplier returned a null collection. Null values are generally not allowed in 2.x operators and sources.");
/* 39 */     } catch (Throwable e) {
/* 40 */       Exceptions.throwIfFatal(e);
/* 41 */       EmptySubscription.error(e, s);
/*    */       return;
/*    */     } 
/* 44 */     this.source.subscribe(new ToListSubscriber<Object, U>(s, (U)collection));
/*    */   }
/*    */   
/*    */   static final class ToListSubscriber<T, U extends Collection<? super T>>
/*    */     extends DeferredScalarSubscription<U>
/*    */     implements FlowableSubscriber<T>, Subscription
/*    */   {
/*    */     private static final long serialVersionUID = -8134157938864266736L;
/*    */     Subscription upstream;
/*    */     
/*    */     ToListSubscriber(Subscriber<? super U> actual, U collection) {
/* 55 */       super(actual);
/* 56 */       this.value = collection;
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
/*    */     public void onNext(T t) {
/* 70 */       Collection<T> collection = (Collection)this.value;
/* 71 */       if (collection != null) {
/* 72 */         collection.add(t);
/*    */       }
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 78 */       this.value = null;
/* 79 */       this.downstream.onError(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 84 */       complete(this.value);
/*    */     }
/*    */ 
/*    */     
/*    */     public void cancel() {
/* 89 */       super.cancel();
/* 90 */       this.upstream.cancel();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableToList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */