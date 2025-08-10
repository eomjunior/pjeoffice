/*     */ package org.slf4j.helpers;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Queue;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.Marker;
/*     */ import org.slf4j.event.EventRecordingLogger;
/*     */ import org.slf4j.event.Level;
/*     */ import org.slf4j.event.LoggingEvent;
/*     */ import org.slf4j.event.SubstituteLoggingEvent;
/*     */ import org.slf4j.spi.LoggingEventBuilder;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SubstituteLogger
/*     */   implements Logger
/*     */ {
/*     */   private final String name;
/*     */   private volatile Logger _delegate;
/*     */   private Boolean delegateEventAware;
/*     */   private Method logMethodCache;
/*     */   private EventRecordingLogger eventRecordingLogger;
/*     */   private final Queue<SubstituteLoggingEvent> eventQueue;
/*     */   public final boolean createdPostInitialization;
/*     */   
/*     */   public SubstituteLogger(String name, Queue<SubstituteLoggingEvent> eventQueue, boolean createdPostInitialization) {
/*  61 */     this.name = name;
/*  62 */     this.eventQueue = eventQueue;
/*  63 */     this.createdPostInitialization = createdPostInitialization;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  68 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggingEventBuilder makeLoggingEventBuilder(Level level) {
/*  73 */     return delegate().makeLoggingEventBuilder(level);
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggingEventBuilder atLevel(Level level) {
/*  78 */     return delegate().atLevel(level);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabledForLevel(Level level) {
/*  83 */     return delegate().isEnabledForLevel(level);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTraceEnabled() {
/*  88 */     return delegate().isTraceEnabled();
/*     */   }
/*     */ 
/*     */   
/*     */   public void trace(String msg) {
/*  93 */     delegate().trace(msg);
/*     */   }
/*     */ 
/*     */   
/*     */   public void trace(String format, Object arg) {
/*  98 */     delegate().trace(format, arg);
/*     */   }
/*     */ 
/*     */   
/*     */   public void trace(String format, Object arg1, Object arg2) {
/* 103 */     delegate().trace(format, arg1, arg2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void trace(String format, Object... arguments) {
/* 108 */     delegate().trace(format, arguments);
/*     */   }
/*     */ 
/*     */   
/*     */   public void trace(String msg, Throwable t) {
/* 113 */     delegate().trace(msg, t);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTraceEnabled(Marker marker) {
/* 118 */     return delegate().isTraceEnabled(marker);
/*     */   }
/*     */ 
/*     */   
/*     */   public void trace(Marker marker, String msg) {
/* 123 */     delegate().trace(marker, msg);
/*     */   }
/*     */ 
/*     */   
/*     */   public void trace(Marker marker, String format, Object arg) {
/* 128 */     delegate().trace(marker, format, arg);
/*     */   }
/*     */ 
/*     */   
/*     */   public void trace(Marker marker, String format, Object arg1, Object arg2) {
/* 133 */     delegate().trace(marker, format, arg1, arg2);
/*     */   }
/*     */   
/*     */   public void trace(Marker marker, String format, Object... arguments) {
/* 137 */     delegate().trace(marker, format, arguments);
/*     */   }
/*     */   
/*     */   public void trace(Marker marker, String msg, Throwable t) {
/* 141 */     delegate().trace(marker, msg, t);
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggingEventBuilder atTrace() {
/* 146 */     return delegate().atTrace();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDebugEnabled() {
/* 151 */     return delegate().isDebugEnabled();
/*     */   }
/*     */ 
/*     */   
/*     */   public void debug(String msg) {
/* 156 */     delegate().debug(msg);
/*     */   }
/*     */ 
/*     */   
/*     */   public void debug(String format, Object arg) {
/* 161 */     delegate().debug(format, arg);
/*     */   }
/*     */ 
/*     */   
/*     */   public void debug(String format, Object arg1, Object arg2) {
/* 166 */     delegate().debug(format, arg1, arg2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void debug(String format, Object... arguments) {
/* 171 */     delegate().debug(format, arguments);
/*     */   }
/*     */ 
/*     */   
/*     */   public void debug(String msg, Throwable t) {
/* 176 */     delegate().debug(msg, t);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDebugEnabled(Marker marker) {
/* 181 */     return delegate().isDebugEnabled(marker);
/*     */   }
/*     */ 
/*     */   
/*     */   public void debug(Marker marker, String msg) {
/* 186 */     delegate().debug(marker, msg);
/*     */   }
/*     */ 
/*     */   
/*     */   public void debug(Marker marker, String format, Object arg) {
/* 191 */     delegate().debug(marker, format, arg);
/*     */   }
/*     */ 
/*     */   
/*     */   public void debug(Marker marker, String format, Object arg1, Object arg2) {
/* 196 */     delegate().debug(marker, format, arg1, arg2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void debug(Marker marker, String format, Object... arguments) {
/* 201 */     delegate().debug(marker, format, arguments);
/*     */   }
/*     */ 
/*     */   
/*     */   public void debug(Marker marker, String msg, Throwable t) {
/* 206 */     delegate().debug(marker, msg, t);
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggingEventBuilder atDebug() {
/* 211 */     return delegate().atDebug();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInfoEnabled() {
/* 216 */     return delegate().isInfoEnabled();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void info(String msg) {
/* 222 */     delegate().info(msg);
/*     */   }
/*     */ 
/*     */   
/*     */   public void info(String format, Object arg) {
/* 227 */     delegate().info(format, arg);
/*     */   }
/*     */ 
/*     */   
/*     */   public void info(String format, Object arg1, Object arg2) {
/* 232 */     delegate().info(format, arg1, arg2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void info(String format, Object... arguments) {
/* 237 */     delegate().info(format, arguments);
/*     */   }
/*     */ 
/*     */   
/*     */   public void info(String msg, Throwable t) {
/* 242 */     delegate().info(msg, t);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInfoEnabled(Marker marker) {
/* 247 */     return delegate().isInfoEnabled(marker);
/*     */   }
/*     */ 
/*     */   
/*     */   public void info(Marker marker, String msg) {
/* 252 */     delegate().info(marker, msg);
/*     */   }
/*     */ 
/*     */   
/*     */   public void info(Marker marker, String format, Object arg) {
/* 257 */     delegate().info(marker, format, arg);
/*     */   }
/*     */ 
/*     */   
/*     */   public void info(Marker marker, String format, Object arg1, Object arg2) {
/* 262 */     delegate().info(marker, format, arg1, arg2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void info(Marker marker, String format, Object... arguments) {
/* 267 */     delegate().info(marker, format, arguments);
/*     */   }
/*     */ 
/*     */   
/*     */   public void info(Marker marker, String msg, Throwable t) {
/* 272 */     delegate().info(marker, msg, t);
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggingEventBuilder atInfo() {
/* 277 */     return delegate().atInfo();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWarnEnabled() {
/* 283 */     return delegate().isWarnEnabled();
/*     */   }
/*     */ 
/*     */   
/*     */   public void warn(String msg) {
/* 288 */     delegate().warn(msg);
/*     */   }
/*     */ 
/*     */   
/*     */   public void warn(String format, Object arg) {
/* 293 */     delegate().warn(format, arg);
/*     */   }
/*     */ 
/*     */   
/*     */   public void warn(String format, Object arg1, Object arg2) {
/* 298 */     delegate().warn(format, arg1, arg2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void warn(String format, Object... arguments) {
/* 303 */     delegate().warn(format, arguments);
/*     */   }
/*     */ 
/*     */   
/*     */   public void warn(String msg, Throwable t) {
/* 308 */     delegate().warn(msg, t);
/*     */   }
/*     */   
/*     */   public boolean isWarnEnabled(Marker marker) {
/* 312 */     return delegate().isWarnEnabled(marker);
/*     */   }
/*     */ 
/*     */   
/*     */   public void warn(Marker marker, String msg) {
/* 317 */     delegate().warn(marker, msg);
/*     */   }
/*     */ 
/*     */   
/*     */   public void warn(Marker marker, String format, Object arg) {
/* 322 */     delegate().warn(marker, format, arg);
/*     */   }
/*     */ 
/*     */   
/*     */   public void warn(Marker marker, String format, Object arg1, Object arg2) {
/* 327 */     delegate().warn(marker, format, arg1, arg2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void warn(Marker marker, String format, Object... arguments) {
/* 332 */     delegate().warn(marker, format, arguments);
/*     */   }
/*     */ 
/*     */   
/*     */   public void warn(Marker marker, String msg, Throwable t) {
/* 337 */     delegate().warn(marker, msg, t);
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggingEventBuilder atWarn() {
/* 342 */     return delegate().atWarn();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isErrorEnabled() {
/* 349 */     return delegate().isErrorEnabled();
/*     */   }
/*     */ 
/*     */   
/*     */   public void error(String msg) {
/* 354 */     delegate().error(msg);
/*     */   }
/*     */ 
/*     */   
/*     */   public void error(String format, Object arg) {
/* 359 */     delegate().error(format, arg);
/*     */   }
/*     */ 
/*     */   
/*     */   public void error(String format, Object arg1, Object arg2) {
/* 364 */     delegate().error(format, arg1, arg2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void error(String format, Object... arguments) {
/* 369 */     delegate().error(format, arguments);
/*     */   }
/*     */ 
/*     */   
/*     */   public void error(String msg, Throwable t) {
/* 374 */     delegate().error(msg, t);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isErrorEnabled(Marker marker) {
/* 379 */     return delegate().isErrorEnabled(marker);
/*     */   }
/*     */ 
/*     */   
/*     */   public void error(Marker marker, String msg) {
/* 384 */     delegate().error(marker, msg);
/*     */   }
/*     */ 
/*     */   
/*     */   public void error(Marker marker, String format, Object arg) {
/* 389 */     delegate().error(marker, format, arg);
/*     */   }
/*     */ 
/*     */   
/*     */   public void error(Marker marker, String format, Object arg1, Object arg2) {
/* 394 */     delegate().error(marker, format, arg1, arg2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void error(Marker marker, String format, Object... arguments) {
/* 399 */     delegate().error(marker, format, arguments);
/*     */   }
/*     */ 
/*     */   
/*     */   public void error(Marker marker, String msg, Throwable t) {
/* 404 */     delegate().error(marker, msg, t);
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggingEventBuilder atError() {
/* 409 */     return delegate().atError();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 414 */     if (this == o)
/* 415 */       return true; 
/* 416 */     if (o == null || getClass() != o.getClass()) {
/* 417 */       return false;
/*     */     }
/* 419 */     SubstituteLogger that = (SubstituteLogger)o;
/*     */     
/* 421 */     if (!this.name.equals(that.name)) {
/* 422 */       return false;
/*     */     }
/* 424 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 429 */     return this.name.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Logger delegate() {
/* 437 */     if (this._delegate != null) {
/* 438 */       return this._delegate;
/*     */     }
/* 440 */     if (this.createdPostInitialization) {
/* 441 */       return NOPLogger.NOP_LOGGER;
/*     */     }
/* 443 */     return getEventRecordingLogger();
/*     */   }
/*     */ 
/*     */   
/*     */   private Logger getEventRecordingLogger() {
/* 448 */     if (this.eventRecordingLogger == null) {
/* 449 */       this.eventRecordingLogger = new EventRecordingLogger(this, this.eventQueue);
/*     */     }
/* 451 */     return (Logger)this.eventRecordingLogger;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDelegate(Logger delegate) {
/* 459 */     this._delegate = delegate;
/*     */   }
/*     */   
/*     */   public boolean isDelegateEventAware() {
/* 463 */     if (this.delegateEventAware != null) {
/* 464 */       return this.delegateEventAware.booleanValue();
/*     */     }
/*     */     try {
/* 467 */       this.logMethodCache = this._delegate.getClass().getMethod("log", new Class[] { LoggingEvent.class });
/* 468 */       this.delegateEventAware = Boolean.TRUE;
/* 469 */     } catch (NoSuchMethodException e) {
/* 470 */       this.delegateEventAware = Boolean.FALSE;
/*     */     } 
/* 472 */     return this.delegateEventAware.booleanValue();
/*     */   }
/*     */   
/*     */   public void log(LoggingEvent event) {
/* 476 */     if (isDelegateEventAware()) {
/*     */       
/* 478 */       try { this.logMethodCache.invoke(this._delegate, new Object[] { event }); }
/* 479 */       catch (IllegalAccessException illegalAccessException) {  }
/* 480 */       catch (IllegalArgumentException illegalArgumentException) {  }
/* 481 */       catch (InvocationTargetException invocationTargetException) {}
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDelegateNull() {
/* 487 */     return (this._delegate == null);
/*     */   }
/*     */   
/*     */   public boolean isDelegateNOP() {
/* 491 */     return this._delegate instanceof NOPLogger;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/slf4j/helpers/SubstituteLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */