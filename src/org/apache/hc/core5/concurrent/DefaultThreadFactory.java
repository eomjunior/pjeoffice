/*    */ package org.apache.hc.core5.concurrent;
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
/*    */ public class DefaultThreadFactory
/*    */   implements ThreadFactory
/*    */ {
/*    */   private final String namePrefix;
/*    */   private final ThreadGroup group;
/*    */   private final AtomicLong count;
/*    */   private final boolean daemon;
/*    */   
/*    */   public DefaultThreadFactory(String namePrefix, ThreadGroup group, boolean daemon) {
/* 45 */     this.namePrefix = namePrefix;
/* 46 */     this.group = group;
/* 47 */     this.daemon = daemon;
/* 48 */     this.count = new AtomicLong();
/*    */   }
/*    */   
/*    */   public DefaultThreadFactory(String namePrefix, boolean daemon) {
/* 52 */     this(namePrefix, null, daemon);
/*    */   }
/*    */   
/*    */   public DefaultThreadFactory(String namePrefix) {
/* 56 */     this(namePrefix, null, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public Thread newThread(Runnable target) {
/* 61 */     Thread thread = new Thread(this.group, target, this.namePrefix + "-" + this.count.incrementAndGet());
/* 62 */     thread.setDaemon(this.daemon);
/* 63 */     return thread;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/concurrent/DefaultThreadFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */