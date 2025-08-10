/*    */ package org.apache.hc.core5.http.impl.bootstrap;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.concurrent.BlockingQueue;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.concurrent.ThreadFactory;
/*    */ import java.util.concurrent.ThreadPoolExecutor;
/*    */ import java.util.concurrent.TimeUnit;
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
/*    */ class WorkerPoolExecutor
/*    */   extends ThreadPoolExecutor
/*    */ {
/*    */   private final Map<Worker, Boolean> workerSet;
/*    */   
/*    */   public WorkerPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
/* 49 */     super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
/* 50 */     this.workerSet = new ConcurrentHashMap<>();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void beforeExecute(Thread t, Runnable r) {
/* 55 */     if (r instanceof Worker) {
/* 56 */       this.workerSet.put((Worker)r, Boolean.TRUE);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected void afterExecute(Runnable r, Throwable t) {
/* 62 */     if (r instanceof Worker) {
/* 63 */       this.workerSet.remove(r);
/*    */     }
/*    */   }
/*    */   
/*    */   public Set<Worker> getWorkers() {
/* 68 */     return new HashSet<>(this.workerSet.keySet());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/bootstrap/WorkerPoolExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */