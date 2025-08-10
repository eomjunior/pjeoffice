/*    */ package io.reactivex.internal.util;
/*    */ 
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.plugins.RxJavaPlugins;
/*    */ import java.util.concurrent.CountDownLatch;
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
/*    */ public final class BlockingHelper
/*    */ {
/*    */   private BlockingHelper() {
/* 28 */     throw new IllegalStateException("No instances!");
/*    */   }
/*    */   
/*    */   public static void awaitForComplete(CountDownLatch latch, Disposable subscription) {
/* 32 */     if (latch.getCount() == 0L) {
/*    */       return;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     try {
/* 39 */       verifyNonBlocking();
/* 40 */       latch.await();
/* 41 */     } catch (InterruptedException e) {
/* 42 */       subscription.dispose();
/*    */ 
/*    */       
/* 45 */       Thread.currentThread().interrupt();
/*    */       
/* 47 */       throw new IllegalStateException("Interrupted while waiting for subscription to complete.", e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void verifyNonBlocking() {
/* 57 */     if (RxJavaPlugins.isFailOnNonBlockingScheduler() && (
/* 58 */       Thread.currentThread() instanceof io.reactivex.internal.schedulers.NonBlockingThread || 
/* 59 */       RxJavaPlugins.onBeforeBlocking()))
/* 60 */       throw new IllegalStateException("Attempt to block on a Scheduler " + Thread.currentThread().getName() + " that doesn't support blocking operators as they may lead to deadlock"); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/util/BlockingHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */