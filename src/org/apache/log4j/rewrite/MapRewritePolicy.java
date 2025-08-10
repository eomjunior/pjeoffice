/*    */ package org.apache.log4j.rewrite;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import org.apache.log4j.Category;
/*    */ import org.apache.log4j.Logger;
/*    */ import org.apache.log4j.spi.LoggingEvent;
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
/*    */ public class MapRewritePolicy
/*    */   implements RewritePolicy
/*    */ {
/*    */   public LoggingEvent rewrite(LoggingEvent source) {
/* 44 */     Object msg = source.getMessage();
/* 45 */     if (msg instanceof Map) {
/* 46 */       Map<Object, Object> props = new HashMap<Object, Object>(source.getProperties());
/* 47 */       Map eventProps = (Map)msg;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 53 */       Object newMsg = eventProps.get("message");
/* 54 */       if (newMsg == null) {
/* 55 */         newMsg = msg;
/*    */       }
/*    */       
/* 58 */       for (Iterator<Map.Entry> iter = eventProps.entrySet().iterator(); iter.hasNext(); ) {
/* 59 */         Map.Entry entry = iter.next();
/* 60 */         if (!"message".equals(entry.getKey())) {
/* 61 */           props.put(entry.getKey(), entry.getValue());
/*    */         }
/*    */       } 
/*    */       
/* 65 */       return new LoggingEvent(source.getFQNOfLoggerClass(), 
/* 66 */           (source.getLogger() != null) ? source.getLogger() : (Category)Logger.getLogger(source.getLoggerName()), source
/* 67 */           .getTimeStamp(), source.getLevel(), newMsg, source.getThreadName(), source
/* 68 */           .getThrowableInformation(), source.getNDC(), source.getLocationInformation(), props);
/*    */     } 
/* 70 */     return source;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/rewrite/MapRewritePolicy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */