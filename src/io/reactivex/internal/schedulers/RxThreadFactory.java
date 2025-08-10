/*    */ package io.reactivex.internal.schedulers;
/*    */ 
/*    */ import java.util.concurrent.ThreadFactory;
/*    */ import java.util.concurrent.atomic.AtomicLong;
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
/*    */ public final class RxThreadFactory
/*    */   extends AtomicLong
/*    */   implements ThreadFactory
/*    */ {
/*    */   private static final long serialVersionUID = -7789753024099756196L;
/*    */   final String prefix;
/*    */   final int priority;
/*    */   final boolean nonBlocking;
/*    */   
/*    */   public RxThreadFactory(String prefix) {
/* 36 */     this(prefix, 5, false);
/*    */   }
/*    */   
/*    */   public RxThreadFactory(String prefix, int priority) {
/* 40 */     this(prefix, priority, false);
/*    */   }
/*    */   
/*    */   public RxThreadFactory(String prefix, int priority, boolean nonBlocking) {
/* 44 */     this.prefix = prefix;
/* 45 */     this.priority = priority;
/* 46 */     this.nonBlocking = nonBlocking;
/*    */   }
/*    */ 
/*    */   
/*    */   public Thread newThread(Runnable r) {
/* 51 */     StringBuilder nameBuilder = (new StringBuilder(this.prefix)).append('-').append(incrementAndGet());
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
/* 73 */     String name = nameBuilder.toString();
/* 74 */     Thread t = this.nonBlocking ? new RxCustomThread(r, name) : new Thread(r, name);
/* 75 */     t.setPriority(this.priority);
/* 76 */     t.setDaemon(true);
/* 77 */     return t;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 82 */     return "RxThreadFactory[" + this.prefix + "]";
/*    */   }
/*    */   
/*    */   static final class RxCustomThread extends Thread implements NonBlockingThread {
/*    */     RxCustomThread(Runnable run, String name) {
/* 87 */       super(run, name);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/schedulers/RxThreadFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */