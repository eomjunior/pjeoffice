/*    */ package org.apache.hc.core5.http.impl.io;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*    */ import org.apache.hc.core5.http.io.HttpClientConnection;
/*    */ import org.apache.hc.core5.http.io.ResponseOutOfOrderStrategy;
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
/*    */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*    */ public final class NoResponseOutOfOrderStrategy
/*    */   implements ResponseOutOfOrderStrategy
/*    */ {
/* 50 */   public static final NoResponseOutOfOrderStrategy INSTANCE = new NoResponseOutOfOrderStrategy();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isEarlyResponseDetected(ClassicHttpRequest request, HttpClientConnection connection, InputStream inputStream, long totalBytesSent, long nextWriteSize) {
/* 59 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/io/NoResponseOutOfOrderStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */