/*    */ package org.apache.log4j.rewrite;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import java.util.StringTokenizer;
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
/*    */ public class PropertyRewritePolicy
/*    */   implements RewritePolicy
/*    */ {
/* 37 */   private Map properties = Collections.EMPTY_MAP;
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
/*    */   public void setProperties(String props) {
/* 50 */     Map<Object, Object> hashTable = new HashMap<Object, Object>();
/* 51 */     StringTokenizer pairs = new StringTokenizer(props, ",");
/* 52 */     while (pairs.hasMoreTokens()) {
/* 53 */       StringTokenizer entry = new StringTokenizer(pairs.nextToken(), "=");
/* 54 */       hashTable.put(entry.nextElement().toString().trim(), entry.nextElement().toString().trim());
/*    */     } 
/* 56 */     synchronized (this) {
/* 57 */       this.properties = hashTable;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LoggingEvent rewrite(LoggingEvent source) {
/* 65 */     if (!this.properties.isEmpty()) {
/* 66 */       Map<Object, Object> rewriteProps = new HashMap<Object, Object>(source.getProperties());
/* 67 */       for (Iterator<Map.Entry> iter = this.properties.entrySet().iterator(); iter.hasNext(); ) {
/* 68 */         Map.Entry entry = iter.next();
/* 69 */         if (!rewriteProps.containsKey(entry.getKey())) {
/* 70 */           rewriteProps.put(entry.getKey(), entry.getValue());
/*    */         }
/*    */       } 
/*    */       
/* 74 */       return new LoggingEvent(source.getFQNOfLoggerClass(), 
/* 75 */           (source.getLogger() != null) ? source.getLogger() : (Category)Logger.getLogger(source.getLoggerName()), source
/* 76 */           .getTimeStamp(), source.getLevel(), source.getMessage(), source.getThreadName(), source
/* 77 */           .getThrowableInformation(), source.getNDC(), source.getLocationInformation(), rewriteProps);
/*    */     } 
/* 79 */     return source;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/rewrite/PropertyRewritePolicy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */