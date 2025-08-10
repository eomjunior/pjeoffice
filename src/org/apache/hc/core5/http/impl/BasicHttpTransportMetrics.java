/*    */ package org.apache.hc.core5.http.impl;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicLong;
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
/*    */ public class BasicHttpTransportMetrics
/*    */   implements HttpTransportMetrics
/*    */ {
/* 44 */   private final AtomicLong bytesTransferred = new AtomicLong(0L);
/*    */ 
/*    */ 
/*    */   
/*    */   public long getBytesTransferred() {
/* 49 */     return this.bytesTransferred.get();
/*    */   }
/*    */   
/*    */   public void incrementBytesTransferred(long count) {
/* 53 */     this.bytesTransferred.addAndGet(count);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/BasicHttpTransportMetrics.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */