/*    */ package io.reactivex.internal.schedulers;
/*    */ 
/*    */ import java.util.concurrent.Callable;
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
/*    */ public final class ScheduledDirectTask
/*    */   extends AbstractDirectTask
/*    */   implements Callable<Void>
/*    */ {
/*    */   private static final long serialVersionUID = 1811839108042568751L;
/*    */   
/*    */   public ScheduledDirectTask(Runnable runnable) {
/* 31 */     super(runnable);
/*    */   }
/*    */ 
/*    */   
/*    */   public Void call() throws Exception {
/* 36 */     this.runner = Thread.currentThread();
/*    */     try {
/* 38 */       this.runnable.run();
/*    */     } finally {
/* 40 */       lazySet(FINISHED);
/* 41 */       this.runner = null;
/*    */     } 
/* 43 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/schedulers/ScheduledDirectTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */