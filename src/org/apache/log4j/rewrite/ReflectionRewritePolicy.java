/*    */ package org.apache.log4j.rewrite;
/*    */ 
/*    */ import java.beans.Introspector;
/*    */ import java.beans.PropertyDescriptor;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.apache.log4j.Category;
/*    */ import org.apache.log4j.Logger;
/*    */ import org.apache.log4j.helpers.LogLog;
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
/*    */ public class ReflectionRewritePolicy
/*    */   implements RewritePolicy
/*    */ {
/*    */   public LoggingEvent rewrite(LoggingEvent source) {
/* 44 */     Object msg = source.getMessage();
/* 45 */     if (!(msg instanceof String)) {
/* 46 */       Object newMsg = msg;
/* 47 */       Map<Object, Object> rewriteProps = new HashMap<Object, Object>(source.getProperties());
/*    */ 
/*    */       
/*    */       try {
/* 51 */         PropertyDescriptor[] props = Introspector.getBeanInfo(msg.getClass(), Object.class).getPropertyDescriptors();
/* 52 */         if (props.length > 0) {
/* 53 */           for (int i = 0; i < props.length; i++) {
/*    */             try {
/* 55 */               Object propertyValue = props[i].getReadMethod().invoke(msg, (Object[])null);
/* 56 */               if ("message".equalsIgnoreCase(props[i].getName())) {
/* 57 */                 newMsg = propertyValue;
/*    */               } else {
/* 59 */                 rewriteProps.put(props[i].getName(), propertyValue);
/*    */               } 
/* 61 */             } catch (Exception e) {
/* 62 */               LogLog.warn("Unable to evaluate property " + props[i].getName(), e);
/*    */             } 
/*    */           } 
/* 65 */           return new LoggingEvent(source.getFQNOfLoggerClass(), 
/* 66 */               (source.getLogger() != null) ? source.getLogger() : (Category)Logger.getLogger(source.getLoggerName()), source
/* 67 */               .getTimeStamp(), source.getLevel(), newMsg, source.getThreadName(), source
/* 68 */               .getThrowableInformation(), source.getNDC(), source.getLocationInformation(), rewriteProps);
/*    */         }
/*    */       
/* 71 */       } catch (Exception e) {
/* 72 */         LogLog.warn("Unable to get property descriptors", e);
/*    */       } 
/*    */     } 
/*    */     
/* 76 */     return source;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/rewrite/ReflectionRewritePolicy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */