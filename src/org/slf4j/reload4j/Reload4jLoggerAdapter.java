/*     */ package org.slf4j.reload4j;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.log4j.Category;
/*     */ import org.apache.log4j.Level;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.apache.log4j.Priority;
/*     */ import org.apache.log4j.spi.LocationInfo;
/*     */ import org.apache.log4j.spi.LoggingEvent;
/*     */ import org.apache.log4j.spi.ThrowableInformation;
/*     */ import org.slf4j.Marker;
/*     */ import org.slf4j.event.DefaultLoggingEvent;
/*     */ import org.slf4j.event.Level;
/*     */ import org.slf4j.event.LoggingEvent;
/*     */ import org.slf4j.helpers.AbstractLogger;
/*     */ import org.slf4j.helpers.LegacyAbstractLogger;
/*     */ import org.slf4j.helpers.MessageFormatter;
/*     */ import org.slf4j.helpers.NormalizedParameters;
/*     */ import org.slf4j.spi.DefaultLoggingEventBuilder;
/*     */ import org.slf4j.spi.LocationAwareLogger;
/*     */ import org.slf4j.spi.LoggingEventAware;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Reload4jLoggerAdapter
/*     */   extends LegacyAbstractLogger
/*     */   implements LocationAwareLogger, LoggingEventAware, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 6989384227325275811L;
/*     */   final transient Logger logger;
/*  69 */   static final String FQCN_NOMINAL = AbstractLogger.class.getName();
/*  70 */   static final String FQCN_SUBSTITUE = FQCN_NOMINAL;
/*  71 */   static final String FQCN_FLUENT = DefaultLoggingEventBuilder.class.getName();
/*     */ 
/*     */ 
/*     */   
/*     */   Reload4jLoggerAdapter(Logger logger) {
/*  76 */     this.logger = logger;
/*  77 */     this.name = logger.getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTraceEnabled() {
/*  86 */     return this.logger.isTraceEnabled();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDebugEnabled() {
/*  95 */     return this.logger.isDebugEnabled();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInfoEnabled() {
/* 104 */     return this.logger.isInfoEnabled();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWarnEnabled() {
/* 113 */     return this.logger.isEnabledFor((Priority)Level.WARN);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isErrorEnabled() {
/* 122 */     return this.logger.isEnabledFor((Priority)Level.ERROR);
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(Marker marker, String callerFQCN, int level, String msg, Object[] arguments, Throwable t) {
/* 127 */     Level log4jLevel = toLog4jLevel(level);
/* 128 */     NormalizedParameters np = NormalizedParameters.normalize(msg, arguments, t);
/* 129 */     String formattedMessage = MessageFormatter.basicArrayFormat(np.getMessage(), np.getArguments());
/* 130 */     this.logger.log(callerFQCN, (Priority)log4jLevel, formattedMessage, np.getThrowable());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void handleNormalizedLoggingCall(Level level, Marker marker, String msg, Object[] arguments, Throwable throwable) {
/* 135 */     Level log4jLevel = toLog4jLevel(level.toInt());
/* 136 */     String formattedMessage = MessageFormatter.basicArrayFormat(msg, arguments);
/* 137 */     this.logger.log(getFullyQualifiedCallerName(), (Priority)log4jLevel, formattedMessage, throwable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void log(LoggingEvent event) {
/* 145 */     Level log4jLevel = toLog4jLevel(event.getLevel().toInt());
/* 146 */     if (!this.logger.isEnabledFor((Priority)log4jLevel)) {
/*     */       return;
/*     */     }
/* 149 */     LoggingEvent log4jevent = event2Log4jEvent(event, log4jLevel);
/* 150 */     this.logger.callAppenders(log4jevent);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private LoggingEvent event2Log4jEvent(LoggingEvent event, Level log4jLevel) {
/* 156 */     String formattedMessage = MessageFormatter.basicArrayFormat(event.getMessage(), event.getArgumentArray());
/*     */     
/* 158 */     LocationInfo locationInfo = null;
/* 159 */     String fqcn = null;
/*     */     
/* 161 */     if (event instanceof org.slf4j.event.SubstituteLoggingEvent) {
/* 162 */       locationInfo = new LocationInfo("NA/SubstituteLogger", "NA/SubstituteLogger", "NA/SubstituteLogger", "0");
/* 163 */       fqcn = FQCN_SUBSTITUE;
/*     */     } else {
/* 165 */       fqcn = FQCN_FLUENT;
/*     */     } 
/*     */     
/* 168 */     ThrowableInformation ti = null;
/* 169 */     Throwable t = event.getThrowable();
/* 170 */     if (t != null) {
/* 171 */       ti = new ThrowableInformation(t);
/*     */     }
/* 173 */     if (event instanceof DefaultLoggingEvent) {
/* 174 */       DefaultLoggingEvent defaultLoggingEvent = (DefaultLoggingEvent)event;
/* 175 */       defaultLoggingEvent.setTimeStamp(System.currentTimeMillis());
/*     */     } 
/*     */ 
/*     */     
/* 179 */     LoggingEvent log4jEvent = new LoggingEvent(fqcn, (Category)this.logger, event.getTimeStamp(), log4jLevel, formattedMessage, event.getThreadName(), ti, null, locationInfo, null);
/*     */     
/* 181 */     return log4jEvent;
/*     */   }
/*     */   
/*     */   private Level toLog4jLevel(int slf4jLevelInt) {
/*     */     Level log4jLevel;
/* 186 */     switch (slf4jLevelInt) {
/*     */       case 0:
/* 188 */         log4jLevel = Level.TRACE;
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
/* 205 */         return log4jLevel;case 10: log4jLevel = Level.DEBUG; return log4jLevel;case 20: log4jLevel = Level.INFO; return log4jLevel;case 30: log4jLevel = Level.WARN; return log4jLevel;case 40: log4jLevel = Level.ERROR; return log4jLevel;
/*     */     } 
/*     */     throw new IllegalStateException("Level number " + slf4jLevelInt + " is not recognized.");
/*     */   }
/*     */   protected String getFullyQualifiedCallerName() {
/* 210 */     return FQCN_NOMINAL;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/slf4j/reload4j/Reload4jLoggerAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */