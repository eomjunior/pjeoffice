/*    */ package io.reactivex.internal.subscribers;
/*    */ 
/*    */ import io.reactivex.FlowableSubscriber;
/*    */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*    */ import io.reactivex.internal.util.BlockingHelper;
/*    */ import io.reactivex.internal.util.ExceptionHelper;
/*    */ import java.util.concurrent.CountDownLatch;
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
/*    */ public abstract class BlockingBaseSubscriber<T>
/*    */   extends CountDownLatch
/*    */   implements FlowableSubscriber<T>
/*    */ {
/*    */   T value;
/*    */   Throwable error;
/*    */   Subscription upstream;
/*    */   volatile boolean cancelled;
/*    */   
/*    */   public BlockingBaseSubscriber() {
/* 34 */     super(1);
/*    */   }
/*    */ 
/*    */   
/*    */   public final void onSubscribe(Subscription s) {
/* 39 */     if (SubscriptionHelper.validate(this.upstream, s)) {
/* 40 */       this.upstream = s;
/* 41 */       if (!this.cancelled) {
/* 42 */         s.request(Long.MAX_VALUE);
/* 43 */         if (this.cancelled) {
/* 44 */           this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/* 45 */           s.cancel();
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public final void onComplete() {
/* 53 */     countDown();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final T blockingGet() {
/* 62 */     if (getCount() != 0L) {
/*    */       try {
/* 64 */         BlockingHelper.verifyNonBlocking();
/* 65 */         await();
/* 66 */       } catch (InterruptedException ex) {
/* 67 */         Subscription s = this.upstream;
/* 68 */         this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/* 69 */         if (s != null) {
/* 70 */           s.cancel();
/*    */         }
/* 72 */         throw ExceptionHelper.wrapOrThrow(ex);
/*    */       } 
/*    */     }
/*    */     
/* 76 */     Throwable e = this.error;
/* 77 */     if (e != null) {
/* 78 */       throw ExceptionHelper.wrapOrThrow(e);
/*    */     }
/* 80 */     return this.value;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/subscribers/BlockingBaseSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */