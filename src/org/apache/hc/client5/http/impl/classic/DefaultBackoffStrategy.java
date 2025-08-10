/*    */ package org.apache.hc.client5.http.impl.classic;
/*    */ 
/*    */ import org.apache.hc.client5.http.classic.ConnectionBackoffStrategy;
/*    */ import org.apache.hc.core5.annotation.Experimental;
/*    */ import org.apache.hc.core5.http.HttpResponse;
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
/*    */ 
/*    */ @Experimental
/*    */ public class DefaultBackoffStrategy
/*    */   implements ConnectionBackoffStrategy
/*    */ {
/*    */   public boolean shouldBackoff(Throwable t) {
/* 49 */     return (t instanceof java.net.SocketTimeoutException || t instanceof java.net.ConnectException);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldBackoff(HttpResponse response) {
/* 54 */     return (response.getCode() == 429 || response
/* 55 */       .getCode() == 503);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/classic/DefaultBackoffStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */