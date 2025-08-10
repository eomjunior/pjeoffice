/*    */ package io.reactivex.internal.operators.completable;
/*    */ 
/*    */ import io.reactivex.CompletableObserver;
/*    */ import io.reactivex.CompletableSource;
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.internal.observers.SubscriberCompletableObserver;
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
/*    */ public final class CompletableToFlowable<T>
/*    */   extends Flowable<T>
/*    */ {
/*    */   final CompletableSource source;
/*    */   
/*    */   public CompletableToFlowable(CompletableSource source) {
/* 26 */     this.source = source;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Subscriber<? super T> s) {
/* 31 */     SubscriberCompletableObserver<T> os = new SubscriberCompletableObserver(s);
/* 32 */     this.source.subscribe((CompletableObserver)os);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableToFlowable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */