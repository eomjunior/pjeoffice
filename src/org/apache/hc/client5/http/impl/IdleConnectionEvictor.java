/*    */ package org.apache.hc.client5.http.impl;
/*    */ 
/*    */ import java.util.concurrent.ThreadFactory;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.concurrent.DefaultThreadFactory;
/*    */ import org.apache.hc.core5.pool.ConnPoolControl;
/*    */ import org.apache.hc.core5.util.Args;
/*    */ import org.apache.hc.core5.util.TimeValue;
/*    */ import org.apache.hc.core5.util.Timeout;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Contract(threading = ThreadingBehavior.SAFE_CONDITIONAL)
/*    */ public final class IdleConnectionEvictor
/*    */ {
/*    */   private final ThreadFactory threadFactory;
/*    */   private final Thread thread;
/*    */   
/*    */   public IdleConnectionEvictor(ConnPoolControl<?> connectionManager, ThreadFactory threadFactory, TimeValue sleepTime, TimeValue maxIdleTime) {
/* 54 */     Args.notNull(connectionManager, "Connection manager");
/* 55 */     this.threadFactory = (threadFactory != null) ? threadFactory : (ThreadFactory)new DefaultThreadFactory("idle-connection-evictor", true);
/* 56 */     TimeValue localSleepTime = (sleepTime != null) ? sleepTime : TimeValue.ofSeconds(5L);
/* 57 */     this.thread = this.threadFactory.newThread(() -> {
/*    */           try {
/*    */             while (!Thread.currentThread().isInterrupted()) {
/*    */               localSleepTime.sleep();
/*    */               connectionManager.closeExpired();
/*    */               if (maxIdleTime != null) {
/*    */                 connectionManager.closeIdle(maxIdleTime);
/*    */               }
/*    */             } 
/* 66 */           } catch (InterruptedException ex) {
/*    */             Thread.currentThread().interrupt();
/* 68 */           } catch (Exception exception) {}
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public IdleConnectionEvictor(ConnPoolControl<?> connectionManager, TimeValue sleepTime, TimeValue maxIdleTime) {
/* 75 */     this(connectionManager, null, sleepTime, maxIdleTime);
/*    */   }
/*    */   
/*    */   public IdleConnectionEvictor(ConnPoolControl<?> connectionManager, TimeValue maxIdleTime) {
/* 79 */     this(connectionManager, null, maxIdleTime, maxIdleTime);
/*    */   }
/*    */   
/*    */   public void start() {
/* 83 */     this.thread.start();
/*    */   }
/*    */   
/*    */   public void shutdown() {
/* 87 */     this.thread.interrupt();
/*    */   }
/*    */   
/*    */   public boolean isRunning() {
/* 91 */     return this.thread.isAlive();
/*    */   }
/*    */   
/*    */   public void awaitTermination(Timeout timeout) throws InterruptedException {
/* 95 */     this.thread.join((timeout != null) ? timeout.toMilliseconds() : Long.MAX_VALUE);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/IdleConnectionEvictor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */