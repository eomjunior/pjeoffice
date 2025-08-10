/*    */ package org.apache.hc.client5.http.impl.async;
/*    */ 
/*    */ import org.apache.hc.core5.function.Decorator;
/*    */ import org.apache.hc.core5.reactor.IOSession;
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
/*    */ final class LoggingIOSessionDecorator
/*    */   implements Decorator<IOSession>
/*    */ {
/* 37 */   public static final LoggingIOSessionDecorator INSTANCE = new LoggingIOSessionDecorator();
/*    */   
/* 39 */   private static final Logger WIRE_LOG = LoggerFactory.getLogger("org.apache.hc.client5.http.wire");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public IOSession decorate(IOSession ioSession) {
/* 46 */     Logger sessionLog = LoggerFactory.getLogger(ioSession.getClass());
/* 47 */     if (sessionLog.isDebugEnabled() || WIRE_LOG.isDebugEnabled()) {
/* 48 */       return new LoggingIOSession(ioSession, sessionLog, WIRE_LOG);
/*    */     }
/* 50 */     return ioSession;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/async/LoggingIOSessionDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */