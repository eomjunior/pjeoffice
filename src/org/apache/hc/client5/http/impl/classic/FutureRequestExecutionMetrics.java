/*     */ package org.apache.hc.client5.http.impl.classic;
/*     */ 
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FutureRequestExecutionMetrics
/*     */ {
/*  36 */   private final AtomicLong activeConnections = new AtomicLong();
/*  37 */   private final AtomicLong scheduledConnections = new AtomicLong();
/*  38 */   private final DurationCounter successfulConnections = new DurationCounter();
/*  39 */   private final DurationCounter failedConnections = new DurationCounter();
/*  40 */   private final DurationCounter requests = new DurationCounter();
/*  41 */   private final DurationCounter tasks = new DurationCounter();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   AtomicLong getActiveConnections() {
/*  47 */     return this.activeConnections;
/*     */   }
/*     */   
/*     */   AtomicLong getScheduledConnections() {
/*  51 */     return this.scheduledConnections;
/*     */   }
/*     */   
/*     */   DurationCounter getSuccessfulConnections() {
/*  55 */     return this.successfulConnections;
/*     */   }
/*     */   
/*     */   DurationCounter getFailedConnections() {
/*  59 */     return this.failedConnections;
/*     */   }
/*     */   
/*     */   DurationCounter getRequests() {
/*  63 */     return this.requests;
/*     */   }
/*     */   
/*     */   DurationCounter getTasks() {
/*  67 */     return this.tasks;
/*     */   }
/*     */   
/*     */   public long getActiveConnectionCount() {
/*  71 */     return this.activeConnections.get();
/*     */   }
/*     */   
/*     */   public long getScheduledConnectionCount() {
/*  75 */     return this.scheduledConnections.get();
/*     */   }
/*     */   
/*     */   public long getSuccessfulConnectionCount() {
/*  79 */     return this.successfulConnections.count();
/*     */   }
/*     */   
/*     */   public long getSuccessfulConnectionAverageDuration() {
/*  83 */     return this.successfulConnections.averageDuration();
/*     */   }
/*     */   
/*     */   public long getFailedConnectionCount() {
/*  87 */     return this.failedConnections.count();
/*     */   }
/*     */   
/*     */   public long getFailedConnectionAverageDuration() {
/*  91 */     return this.failedConnections.averageDuration();
/*     */   }
/*     */   
/*     */   public long getRequestCount() {
/*  95 */     return this.requests.count();
/*     */   }
/*     */   
/*     */   public long getRequestAverageDuration() {
/*  99 */     return this.requests.averageDuration();
/*     */   }
/*     */   
/*     */   public long getTaskCount() {
/* 103 */     return this.tasks.count();
/*     */   }
/*     */   
/*     */   public long getTaskAverageDuration() {
/* 107 */     return this.tasks.averageDuration();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 112 */     StringBuilder builder = new StringBuilder();
/* 113 */     builder.append("[activeConnections=").append(this.activeConnections)
/* 114 */       .append(", scheduledConnections=").append(this.scheduledConnections)
/* 115 */       .append(", successfulConnections=").append(this.successfulConnections)
/* 116 */       .append(", failedConnections=").append(this.failedConnections)
/* 117 */       .append(", requests=").append(this.requests)
/* 118 */       .append(", tasks=").append(this.tasks)
/* 119 */       .append("]");
/* 120 */     return builder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class DurationCounter
/*     */   {
/* 128 */     private final AtomicLong count = new AtomicLong(0L);
/* 129 */     private final AtomicLong cumulativeDuration = new AtomicLong(0L);
/*     */     
/*     */     public void increment(long startTime) {
/* 132 */       this.count.incrementAndGet();
/* 133 */       this.cumulativeDuration.addAndGet(System.currentTimeMillis() - startTime);
/*     */     }
/*     */     
/*     */     public long count() {
/* 137 */       return this.count.get();
/*     */     }
/*     */     
/*     */     public long averageDuration() {
/* 141 */       long counter = this.count.get();
/* 142 */       return (counter > 0L) ? (this.cumulativeDuration.get() / counter) : 0L;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 147 */       StringBuilder builder = new StringBuilder();
/* 148 */       builder.append("[count=").append(count())
/* 149 */         .append(", averageDuration=").append(averageDuration())
/* 150 */         .append("]");
/* 151 */       return builder.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/classic/FutureRequestExecutionMetrics.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */