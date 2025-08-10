/*     */ package org.slf4j.event;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.Marker;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultLoggingEvent
/*     */   implements LoggingEvent
/*     */ {
/*     */   Logger logger;
/*     */   Level level;
/*     */   String message;
/*     */   List<Marker> markers;
/*     */   List<Object> arguments;
/*     */   List<KeyValuePair> keyValuePairs;
/*     */   Throwable throwable;
/*     */   String threadName;
/*     */   long timeStamp;
/*     */   String callerBoundary;
/*     */   
/*     */   public DefaultLoggingEvent(Level level, Logger logger) {
/*  34 */     this.logger = logger;
/*  35 */     this.level = level;
/*     */   }
/*     */   
/*     */   public void addMarker(Marker marker) {
/*  39 */     if (this.markers == null) {
/*  40 */       this.markers = new ArrayList<>(2);
/*     */     }
/*  42 */     this.markers.add(marker);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Marker> getMarkers() {
/*  47 */     return this.markers;
/*     */   }
/*     */   
/*     */   public void addArgument(Object p) {
/*  51 */     getNonNullArguments().add(p);
/*     */   }
/*     */   
/*     */   public void addArguments(Object... args) {
/*  55 */     getNonNullArguments().addAll(Arrays.asList(args));
/*     */   }
/*     */   
/*     */   private List<Object> getNonNullArguments() {
/*  59 */     if (this.arguments == null) {
/*  60 */       this.arguments = new ArrayList(3);
/*     */     }
/*  62 */     return this.arguments;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Object> getArguments() {
/*  67 */     return this.arguments;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] getArgumentArray() {
/*  72 */     if (this.arguments == null)
/*  73 */       return null; 
/*  74 */     return this.arguments.toArray();
/*     */   }
/*     */   
/*     */   public void addKeyValue(String key, Object value) {
/*  78 */     getNonnullKeyValuePairs().add(new KeyValuePair(key, value));
/*     */   }
/*     */   
/*     */   private List<KeyValuePair> getNonnullKeyValuePairs() {
/*  82 */     if (this.keyValuePairs == null) {
/*  83 */       this.keyValuePairs = new ArrayList<>(4);
/*     */     }
/*  85 */     return this.keyValuePairs;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<KeyValuePair> getKeyValuePairs() {
/*  90 */     return this.keyValuePairs;
/*     */   }
/*     */   
/*     */   public void setThrowable(Throwable cause) {
/*  94 */     this.throwable = cause;
/*     */   }
/*     */ 
/*     */   
/*     */   public Level getLevel() {
/*  99 */     return this.level;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLoggerName() {
/* 104 */     return this.logger.getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMessage() {
/* 109 */     return this.message;
/*     */   }
/*     */   
/*     */   public void setMessage(String message) {
/* 113 */     this.message = message;
/*     */   }
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/* 118 */     return this.throwable;
/*     */   }
/*     */   
/*     */   public String getThreadName() {
/* 122 */     return this.threadName;
/*     */   }
/*     */   
/*     */   public long getTimeStamp() {
/* 126 */     return this.timeStamp;
/*     */   }
/*     */   
/*     */   public void setTimeStamp(long timeStamp) {
/* 130 */     this.timeStamp = timeStamp;
/*     */   }
/*     */   
/*     */   public void setCallerBoundary(String fqcn) {
/* 134 */     this.callerBoundary = fqcn;
/*     */   }
/*     */   
/*     */   public String getCallerBoundary() {
/* 138 */     return this.callerBoundary;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/slf4j/event/DefaultLoggingEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */