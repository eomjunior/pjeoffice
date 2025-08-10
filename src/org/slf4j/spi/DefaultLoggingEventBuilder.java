/*     */ package org.slf4j.spi;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Supplier;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.Marker;
/*     */ import org.slf4j.event.DefaultLoggingEvent;
/*     */ import org.slf4j.event.KeyValuePair;
/*     */ import org.slf4j.event.Level;
/*     */ import org.slf4j.event.LoggingEvent;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultLoggingEventBuilder
/*     */   implements LoggingEventBuilder, CallerBoundaryAware
/*     */ {
/*  44 */   static String DLEB_FQCN = DefaultLoggingEventBuilder.class.getName();
/*     */   
/*     */   protected DefaultLoggingEvent loggingEvent;
/*     */   protected Logger logger;
/*     */   
/*     */   public DefaultLoggingEventBuilder(Logger logger, Level level) {
/*  50 */     this.logger = logger;
/*  51 */     this.loggingEvent = new DefaultLoggingEvent(level, logger);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggingEventBuilder addMarker(Marker marker) {
/*  63 */     this.loggingEvent.addMarker(marker);
/*  64 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggingEventBuilder setCause(Throwable t) {
/*  69 */     this.loggingEvent.setThrowable(t);
/*  70 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggingEventBuilder addArgument(Object p) {
/*  75 */     this.loggingEvent.addArgument(p);
/*  76 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggingEventBuilder addArgument(Supplier<?> objectSupplier) {
/*  81 */     this.loggingEvent.addArgument(objectSupplier.get());
/*  82 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggingEventBuilder addKeyValue(String key, Object value) {
/*  87 */     this.loggingEvent.addKeyValue(key, value);
/*  88 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggingEventBuilder addKeyValue(String key, Supplier<Object> value) {
/*  93 */     this.loggingEvent.addKeyValue(key, value.get());
/*  94 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCallerBoundary(String fqcn) {
/*  99 */     this.loggingEvent.setCallerBoundary(fqcn);
/*     */   }
/*     */ 
/*     */   
/*     */   public void log() {
/* 104 */     log((LoggingEvent)this.loggingEvent);
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggingEventBuilder setMessage(String message) {
/* 109 */     this.loggingEvent.setMessage(message);
/* 110 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggingEventBuilder setMessage(Supplier<String> messageSupplier) {
/* 115 */     this.loggingEvent.setMessage(messageSupplier.get());
/* 116 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(String message) {
/* 121 */     this.loggingEvent.setMessage(message);
/* 122 */     log((LoggingEvent)this.loggingEvent);
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(String message, Object arg) {
/* 127 */     this.loggingEvent.setMessage(message);
/* 128 */     this.loggingEvent.addArgument(arg);
/* 129 */     log((LoggingEvent)this.loggingEvent);
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(String message, Object arg0, Object arg1) {
/* 134 */     this.loggingEvent.setMessage(message);
/* 135 */     this.loggingEvent.addArgument(arg0);
/* 136 */     this.loggingEvent.addArgument(arg1);
/* 137 */     log((LoggingEvent)this.loggingEvent);
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(String message, Object... args) {
/* 142 */     this.loggingEvent.setMessage(message);
/* 143 */     this.loggingEvent.addArguments(args);
/*     */     
/* 145 */     log((LoggingEvent)this.loggingEvent);
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(Supplier<String> messageSupplier) {
/* 150 */     if (messageSupplier == null) {
/* 151 */       log((String)null);
/*     */     } else {
/* 153 */       log(messageSupplier.get());
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void log(LoggingEvent aLoggingEvent) {
/* 158 */     if (aLoggingEvent.getCallerBoundary() == null) {
/* 159 */       setCallerBoundary(DLEB_FQCN);
/*     */     }
/*     */     
/* 162 */     if (this.logger instanceof LoggingEventAware) {
/* 163 */       ((LoggingEventAware)this.logger).log(aLoggingEvent);
/* 164 */     } else if (this.logger instanceof LocationAwareLogger) {
/* 165 */       logViaLocationAwareLoggerAPI((LocationAwareLogger)this.logger, aLoggingEvent);
/*     */     } else {
/* 167 */       logViaPublicSLF4JLoggerAPI(aLoggingEvent);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void logViaLocationAwareLoggerAPI(LocationAwareLogger locationAwareLogger, LoggingEvent aLoggingEvent) {
/* 172 */     String msg = aLoggingEvent.getMessage();
/* 173 */     List<Marker> markerList = aLoggingEvent.getMarkers();
/* 174 */     String mergedMessage = mergeMarkersAndKeyValuePairsAndMessage(aLoggingEvent);
/* 175 */     locationAwareLogger.log(null, aLoggingEvent.getCallerBoundary(), aLoggingEvent.getLevel().toInt(), mergedMessage, aLoggingEvent
/*     */         
/* 177 */         .getArgumentArray(), aLoggingEvent.getThrowable());
/*     */   }
/*     */   
/*     */   private void logViaPublicSLF4JLoggerAPI(LoggingEvent aLoggingEvent) {
/* 181 */     Object[] argArray = aLoggingEvent.getArgumentArray();
/* 182 */     int argLen = (argArray == null) ? 0 : argArray.length;
/*     */     
/* 184 */     Throwable t = aLoggingEvent.getThrowable();
/* 185 */     int tLen = (t == null) ? 0 : 1;
/*     */     
/* 187 */     Object[] combinedArguments = new Object[argLen + tLen];
/*     */     
/* 189 */     if (argArray != null) {
/* 190 */       System.arraycopy(argArray, 0, combinedArguments, 0, argLen);
/*     */     }
/* 192 */     if (t != null) {
/* 193 */       combinedArguments[argLen] = t;
/*     */     }
/*     */     
/* 196 */     String mergedMessage = mergeMarkersAndKeyValuePairsAndMessage(aLoggingEvent);
/*     */ 
/*     */     
/* 199 */     switch (aLoggingEvent.getLevel()) {
/*     */       case TRACE:
/* 201 */         this.logger.trace(mergedMessage, combinedArguments);
/*     */         break;
/*     */       case DEBUG:
/* 204 */         this.logger.debug(mergedMessage, combinedArguments);
/*     */         break;
/*     */       case INFO:
/* 207 */         this.logger.info(mergedMessage, combinedArguments);
/*     */         break;
/*     */       case WARN:
/* 210 */         this.logger.warn(mergedMessage, combinedArguments);
/*     */         break;
/*     */       case ERROR:
/* 213 */         this.logger.error(mergedMessage, combinedArguments);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String mergeMarkersAndKeyValuePairsAndMessage(LoggingEvent aLoggingEvent) {
/* 227 */     StringBuilder sb = mergeMarkers(aLoggingEvent.getMarkers(), null);
/* 228 */     sb = mergeKeyValuePairs(aLoggingEvent.getKeyValuePairs(), sb);
/* 229 */     String mergedMessage = mergeMessage(aLoggingEvent.getMessage(), sb);
/* 230 */     return mergedMessage;
/*     */   }
/*     */   
/*     */   private StringBuilder mergeMarkers(List<Marker> markerList, StringBuilder sb) {
/* 234 */     if (markerList == null || markerList.isEmpty()) {
/* 235 */       return sb;
/*     */     }
/* 237 */     if (sb == null) {
/* 238 */       sb = new StringBuilder();
/*     */     }
/* 240 */     for (Marker marker : markerList) {
/* 241 */       sb.append(marker);
/* 242 */       sb.append(' ');
/*     */     } 
/* 244 */     return sb;
/*     */   }
/*     */   
/*     */   private StringBuilder mergeKeyValuePairs(List<KeyValuePair> keyValuePairList, StringBuilder sb) {
/* 248 */     if (keyValuePairList == null || keyValuePairList.isEmpty()) {
/* 249 */       return sb;
/*     */     }
/* 251 */     if (sb == null) {
/* 252 */       sb = new StringBuilder();
/*     */     }
/* 254 */     for (KeyValuePair kvp : keyValuePairList) {
/* 255 */       sb.append(kvp.key);
/* 256 */       sb.append('=');
/* 257 */       sb.append(kvp.value);
/* 258 */       sb.append(' ');
/*     */     } 
/* 260 */     return sb;
/*     */   }
/*     */   
/*     */   private String mergeMessage(String msg, StringBuilder sb) {
/* 264 */     if (sb != null) {
/* 265 */       sb.append(msg);
/* 266 */       return sb.toString();
/*     */     } 
/* 268 */     return msg;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/slf4j/spi/DefaultLoggingEventBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */