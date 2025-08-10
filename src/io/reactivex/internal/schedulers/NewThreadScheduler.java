/*    */ package io.reactivex.internal.schedulers;
/*    */ 
/*    */ import io.reactivex.Scheduler;
/*    */ import io.reactivex.annotations.NonNull;
/*    */ import java.util.concurrent.ThreadFactory;
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
/*    */ 
/*    */ public final class NewThreadScheduler
/*    */   extends Scheduler
/*    */ {
/*    */   final ThreadFactory threadFactory;
/*    */   private static final String THREAD_NAME_PREFIX = "RxNewThreadScheduler";
/*    */   private static final RxThreadFactory THREAD_FACTORY;
/*    */   private static final String KEY_NEWTHREAD_PRIORITY = "rx2.newthread-priority";
/*    */   
/*    */   static {
/* 38 */     int priority = Math.max(1, Math.min(10, 
/* 39 */           Integer.getInteger("rx2.newthread-priority", 5).intValue()));
/*    */     
/* 41 */     THREAD_FACTORY = new RxThreadFactory("RxNewThreadScheduler", priority);
/*    */   }
/*    */   
/*    */   public NewThreadScheduler() {
/* 45 */     this(THREAD_FACTORY);
/*    */   }
/*    */   
/*    */   public NewThreadScheduler(ThreadFactory threadFactory) {
/* 49 */     this.threadFactory = threadFactory;
/*    */   }
/*    */ 
/*    */   
/*    */   @NonNull
/*    */   public Scheduler.Worker createWorker() {
/* 55 */     return new NewThreadWorker(this.threadFactory);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/schedulers/NewThreadScheduler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */