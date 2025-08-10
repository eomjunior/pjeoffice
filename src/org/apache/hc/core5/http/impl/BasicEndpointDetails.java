/*    */ package org.apache.hc.core5.http.impl;
/*    */ 
/*    */ import java.net.SocketAddress;
/*    */ import org.apache.hc.core5.http.EndpointDetails;
/*    */ import org.apache.hc.core5.http.HttpConnectionMetrics;
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
/*    */ 
/*    */ public final class BasicEndpointDetails
/*    */   extends EndpointDetails
/*    */ {
/*    */   private final HttpConnectionMetrics metrics;
/*    */   
/*    */   public BasicEndpointDetails(SocketAddress remoteAddress, SocketAddress localAddress, HttpConnectionMetrics metrics, Timeout socketTimeout) {
/* 50 */     super(remoteAddress, localAddress, socketTimeout);
/* 51 */     this.metrics = metrics;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getRequestCount() {
/* 56 */     return (this.metrics != null) ? this.metrics.getRequestCount() : 0L;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getResponseCount() {
/* 61 */     return (this.metrics != null) ? this.metrics.getResponseCount() : 0L;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getSentBytesCount() {
/* 66 */     return (this.metrics != null) ? this.metrics.getSentBytesCount() : 0L;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getReceivedBytesCount() {
/* 71 */     return (this.metrics != null) ? this.metrics.getReceivedBytesCount() : 0L;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/BasicEndpointDetails.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */