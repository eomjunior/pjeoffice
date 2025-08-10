/*    */ package io.reactivex.internal.schedulers;
/*    */ 
/*    */ import io.reactivex.plugins.RxJavaPlugins;
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
/*    */ public final class ScheduledDirectPeriodicTask
/*    */   extends AbstractDirectTask
/*    */   implements Runnable
/*    */ {
/*    */   private static final long serialVersionUID = 1811839108042568751L;
/*    */   
/*    */   public ScheduledDirectPeriodicTask(Runnable runnable) {
/* 31 */     super(runnable);
/*    */   }
/*    */ 
/*    */   
/*    */   public void run() {
/* 36 */     this.runner = Thread.currentThread();
/*    */     try {
/* 38 */       this.runnable.run();
/* 39 */       this.runner = null;
/* 40 */     } catch (Throwable ex) {
/* 41 */       this.runner = null;
/* 42 */       lazySet(FINISHED);
/* 43 */       RxJavaPlugins.onError(ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/schedulers/ScheduledDirectPeriodicTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */