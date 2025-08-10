/*    */ package org.apache.hc.core5.http.impl;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ import org.apache.hc.core5.http.HttpConnectionMetrics;
/*    */ import org.apache.hc.core5.http.io.HttpTransportMetrics;
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
/*    */ public final class BasicHttpConnectionMetrics
/*    */   implements HttpConnectionMetrics
/*    */ {
/*    */   private final HttpTransportMetrics inTransportMetric;
/*    */   private final HttpTransportMetrics outTransportMetric;
/*    */   private final AtomicLong requestCount;
/*    */   private final AtomicLong responseCount;
/*    */   
/*    */   public BasicHttpConnectionMetrics(HttpTransportMetrics inTransportMetric, HttpTransportMetrics outTransportMetric) {
/* 51 */     this.inTransportMetric = inTransportMetric;
/* 52 */     this.outTransportMetric = outTransportMetric;
/* 53 */     this.requestCount = new AtomicLong(0L);
/* 54 */     this.responseCount = new AtomicLong(0L);
/*    */   }
/*    */ 
/*    */   
/*    */   public long getReceivedBytesCount() {
/* 59 */     if (this.inTransportMetric != null) {
/* 60 */       return this.inTransportMetric.getBytesTransferred();
/*    */     }
/* 62 */     return -1L;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getSentBytesCount() {
/* 67 */     if (this.outTransportMetric != null) {
/* 68 */       return this.outTransportMetric.getBytesTransferred();
/*    */     }
/* 70 */     return -1L;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getRequestCount() {
/* 75 */     return this.requestCount.get();
/*    */   }
/*    */   
/*    */   public void incrementRequestCount() {
/* 79 */     this.requestCount.incrementAndGet();
/*    */   }
/*    */ 
/*    */   
/*    */   public long getResponseCount() {
/* 84 */     return this.responseCount.get();
/*    */   }
/*    */   
/*    */   public void incrementResponseCount() {
/* 88 */     this.responseCount.incrementAndGet();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/BasicHttpConnectionMetrics.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */