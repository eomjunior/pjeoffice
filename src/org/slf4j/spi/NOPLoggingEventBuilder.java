/*    */ package org.slf4j.spi;
/*    */ 
/*    */ import java.util.function.Supplier;
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
/*    */ public class NOPLoggingEventBuilder
/*    */   implements LoggingEventBuilder
/*    */ {
/* 21 */   static final NOPLoggingEventBuilder SINGLETON = new NOPLoggingEventBuilder();
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
/*    */   public static LoggingEventBuilder singleton() {
/* 33 */     return SINGLETON;
/*    */   }
/*    */ 
/*    */   
/*    */   public LoggingEventBuilder addMarker(Marker marker) {
/* 38 */     return singleton();
/*    */   }
/*    */ 
/*    */   
/*    */   public LoggingEventBuilder addArgument(Object p) {
/* 43 */     return singleton();
/*    */   }
/*    */ 
/*    */   
/*    */   public LoggingEventBuilder addArgument(Supplier<?> objectSupplier) {
/* 48 */     return singleton();
/*    */   }
/*    */ 
/*    */   
/*    */   public LoggingEventBuilder addKeyValue(String key, Object value) {
/* 53 */     return singleton();
/*    */   }
/*    */ 
/*    */   
/*    */   public LoggingEventBuilder addKeyValue(String key, Supplier<Object> value) {
/* 58 */     return singleton();
/*    */   }
/*    */ 
/*    */   
/*    */   public LoggingEventBuilder setCause(Throwable cause) {
/* 63 */     return singleton();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void log() {}
/*    */ 
/*    */   
/*    */   public LoggingEventBuilder setMessage(String message) {
/* 72 */     return this;
/*    */   }
/*    */   
/*    */   public LoggingEventBuilder setMessage(Supplier<String> messageSupplier) {
/* 76 */     return this;
/*    */   }
/*    */   
/*    */   public void log(String message) {}
/*    */   
/*    */   public void log(Supplier<String> messageSupplier) {}
/*    */   
/*    */   public void log(String message, Object arg) {}
/*    */   
/*    */   public void log(String message, Object arg0, Object arg1) {}
/*    */   
/*    */   public void log(String message, Object... args) {}
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/slf4j/spi/NOPLoggingEventBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */