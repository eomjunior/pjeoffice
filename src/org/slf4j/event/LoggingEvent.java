/*    */ package org.slf4j.event;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.slf4j.Marker;
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
/*    */ public interface LoggingEvent
/*    */ {
/*    */   Level getLevel();
/*    */   
/*    */   String getLoggerName();
/*    */   
/*    */   String getMessage();
/*    */   
/*    */   List<Object> getArguments();
/*    */   
/*    */   Object[] getArgumentArray();
/*    */   
/*    */   List<Marker> getMarkers();
/*    */   
/*    */   List<KeyValuePair> getKeyValuePairs();
/*    */   
/*    */   Throwable getThrowable();
/*    */   
/*    */   long getTimeStamp();
/*    */   
/*    */   String getThreadName();
/*    */   
/*    */   default String getCallerBoundary() {
/* 47 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/slf4j/event/LoggingEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */