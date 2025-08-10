/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import java.util.concurrent.ThreadFactory;
/*    */ import java.util.concurrent.atomic.AtomicInteger;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CustomThreadFactory
/*    */   implements ThreadFactory
/*    */ {
/* 34 */   private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
/*    */   private final ThreadGroup group;
/* 36 */   private final AtomicInteger threadNumber = new AtomicInteger(1);
/*    */   private final String namePrefix;
/*    */   
/*    */   public CustomThreadFactory(String baseName) {
/* 40 */     SecurityManager s = System.getSecurityManager();
/* 41 */     this.group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
/* 42 */     this.namePrefix = baseName + "-pool-" + POOL_NUMBER.getAndIncrement() + "-thread-";
/*    */   }
/*    */   
/*    */   public Thread newThread(Runnable r) {
/* 46 */     Thread t = new Thread(this.group, r, this.namePrefix + this.threadNumber.getAndIncrement(), 0L);
/* 47 */     customize(t);
/* 48 */     return t;
/*    */   }
/*    */   
/*    */   protected void customize(Thread thread) {
/* 52 */     if (thread.isDaemon())
/* 53 */       thread.setDaemon(false); 
/* 54 */     if (thread.getPriority() != 5)
/* 55 */       thread.setPriority(5); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/CustomThreadFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */