/*    */ package org.slf4j.event;
/*    */ 
/*    */ import java.util.Queue;
/*    */ import org.slf4j.Marker;
/*    */ import org.slf4j.helpers.LegacyAbstractLogger;
/*    */ import org.slf4j.helpers.SubstituteLogger;
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
/*    */ public class EventRecordingLogger
/*    */   extends LegacyAbstractLogger
/*    */ {
/*    */   private static final long serialVersionUID = -176083308134819629L;
/*    */   String name;
/*    */   SubstituteLogger logger;
/*    */   Queue<SubstituteLoggingEvent> eventQueue;
/*    */   static final boolean RECORD_ALL_EVENTS = true;
/*    */   
/*    */   public EventRecordingLogger(SubstituteLogger logger, Queue<SubstituteLoggingEvent> eventQueue) {
/* 31 */     this.logger = logger;
/* 32 */     this.name = logger.getName();
/* 33 */     this.eventQueue = eventQueue;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 37 */     return this.name;
/*    */   }
/*    */   
/*    */   public boolean isTraceEnabled() {
/* 41 */     return true;
/*    */   }
/*    */   
/*    */   public boolean isDebugEnabled() {
/* 45 */     return true;
/*    */   }
/*    */   
/*    */   public boolean isInfoEnabled() {
/* 49 */     return true;
/*    */   }
/*    */   
/*    */   public boolean isWarnEnabled() {
/* 53 */     return true;
/*    */   }
/*    */   
/*    */   public boolean isErrorEnabled() {
/* 57 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void handleNormalizedLoggingCall(Level level, Marker marker, String msg, Object[] args, Throwable throwable) {
/* 62 */     SubstituteLoggingEvent loggingEvent = new SubstituteLoggingEvent();
/* 63 */     loggingEvent.setTimeStamp(System.currentTimeMillis());
/* 64 */     loggingEvent.setLevel(level);
/* 65 */     loggingEvent.setLogger(this.logger);
/* 66 */     loggingEvent.setLoggerName(this.name);
/* 67 */     if (marker != null) {
/* 68 */       loggingEvent.addMarker(marker);
/*    */     }
/* 70 */     loggingEvent.setMessage(msg);
/* 71 */     loggingEvent.setThreadName(Thread.currentThread().getName());
/*    */     
/* 73 */     loggingEvent.setArgumentArray(args);
/* 74 */     loggingEvent.setThrowable(throwable);
/*    */     
/* 76 */     this.eventQueue.add(loggingEvent);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected String getFullyQualifiedCallerName() {
/* 82 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/slf4j/event/EventRecordingLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */