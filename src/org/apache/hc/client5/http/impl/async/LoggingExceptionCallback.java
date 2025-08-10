/*    */ package org.apache.hc.client5.http.impl.async;
/*    */ 
/*    */ import org.apache.hc.core5.function.Callback;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
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
/*    */ class LoggingExceptionCallback
/*    */   implements Callback<Exception>
/*    */ {
/* 36 */   static final LoggingExceptionCallback INSTANCE = new LoggingExceptionCallback();
/*    */   
/* 38 */   private static final Logger LOG = LoggerFactory.getLogger("org.apache.hc.client5.http.impl.async");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(Exception ex) {
/* 45 */     LOG.error(ex.getMessage(), ex);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/async/LoggingExceptionCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */