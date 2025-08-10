/*    */ package org.apache.hc.core5.http2.impl;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ import org.apache.hc.core5.http.impl.BasicHttpTransportMetrics;
/*    */ import org.apache.hc.core5.http2.H2TransportMetrics;
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
/*    */ public class BasicH2TransportMetrics
/*    */   extends BasicHttpTransportMetrics
/*    */   implements H2TransportMetrics
/*    */ {
/* 45 */   private final AtomicLong framesTransferred = new AtomicLong(0L);
/*    */ 
/*    */ 
/*    */   
/*    */   public long getFramesTransferred() {
/* 50 */     return this.framesTransferred.get();
/*    */   }
/*    */   
/*    */   public void incrementFramesTransferred() {
/* 54 */     this.framesTransferred.incrementAndGet();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/BasicH2TransportMetrics.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */