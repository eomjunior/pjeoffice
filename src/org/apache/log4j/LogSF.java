/*      */ package org.apache.log4j;
/*      */ 
/*      */ import java.util.ResourceBundle;
/*      */ import org.apache.log4j.spi.LoggingEvent;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class LogSF
/*      */   extends LogXF
/*      */ {
/*      */   private static String format(String pattern, Object[] arguments) {
/*   49 */     if (pattern != null) {
/*   50 */       String retval = "";
/*   51 */       int count = 0;
/*   52 */       int prev = 0;
/*   53 */       int pos = pattern.indexOf("{");
/*   54 */       while (pos >= 0) {
/*   55 */         if (pos == 0 || pattern.charAt(pos - 1) != '\\') {
/*   56 */           retval = retval + pattern.substring(prev, pos);
/*   57 */           if (pos + 1 < pattern.length() && pattern.charAt(pos + 1) == '}') {
/*   58 */             if (arguments != null && count < arguments.length) {
/*   59 */               retval = retval + arguments[count++];
/*      */             } else {
/*   61 */               retval = retval + "{}";
/*      */             } 
/*   63 */             prev = pos + 2;
/*      */           } else {
/*   65 */             retval = retval + "{";
/*   66 */             prev = pos + 1;
/*      */           } 
/*      */         } else {
/*   69 */           retval = retval + pattern.substring(prev, pos - 1) + "{";
/*   70 */           prev = pos + 1;
/*      */         } 
/*   72 */         pos = pattern.indexOf("{", prev);
/*      */       } 
/*   74 */       return retval + pattern.substring(prev);
/*      */     } 
/*   76 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String format(String pattern, Object arg0) {
/*   87 */     if (pattern != null) {
/*      */ 
/*      */       
/*   90 */       if (pattern.indexOf("\\{") >= 0) {
/*   91 */         return format(pattern, new Object[] { arg0 });
/*      */       }
/*   93 */       int pos = pattern.indexOf("{}");
/*   94 */       if (pos >= 0) {
/*   95 */         return pattern.substring(0, pos) + arg0 + pattern.substring(pos + 2);
/*      */       }
/*      */     } 
/*   98 */     return pattern;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String format(String resourceBundleName, String key, Object[] arguments) {
/*      */     String pattern;
/*  111 */     if (resourceBundleName != null) {
/*      */       try {
/*  113 */         ResourceBundle bundle = ResourceBundle.getBundle(resourceBundleName);
/*  114 */         pattern = bundle.getString(key);
/*  115 */       } catch (Exception ex) {
/*  116 */         pattern = key;
/*      */       } 
/*      */     } else {
/*  119 */       pattern = key;
/*      */     } 
/*  121 */     return format(pattern, arguments);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  127 */   private static final String FQCN = LogSF.class.getName();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void forcedLog(Logger logger, Level level, String msg) {
/*  137 */     logger.callAppenders(new LoggingEvent(FQCN, logger, level, msg, null));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void forcedLog(Logger logger, Level level, String msg, Throwable t) {
/*  149 */     logger.callAppenders(new LoggingEvent(FQCN, logger, level, msg, t));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void trace(Logger logger, String pattern, Object[] arguments) {
/*  160 */     if (logger.isEnabledFor(TRACE)) {
/*  161 */       forcedLog(logger, TRACE, format(pattern, arguments));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void debug(Logger logger, String pattern, Object[] arguments) {
/*  173 */     if (logger.isDebugEnabled()) {
/*  174 */       forcedLog(logger, Level.DEBUG, format(pattern, arguments));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void info(Logger logger, String pattern, Object[] arguments) {
/*  186 */     if (logger.isInfoEnabled()) {
/*  187 */       forcedLog(logger, Level.INFO, format(pattern, arguments));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void warn(Logger logger, String pattern, Object[] arguments) {
/*  199 */     if (logger.isEnabledFor(Level.WARN)) {
/*  200 */       forcedLog(logger, Level.WARN, format(pattern, arguments));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void error(Logger logger, String pattern, Object[] arguments) {
/*  212 */     if (logger.isEnabledFor(Level.ERROR)) {
/*  213 */       forcedLog(logger, Level.ERROR, format(pattern, arguments));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void fatal(Logger logger, String pattern, Object[] arguments) {
/*  225 */     if (logger.isEnabledFor(Level.FATAL)) {
/*  226 */       forcedLog(logger, Level.FATAL, format(pattern, arguments));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void trace(Logger logger, Throwable t, String pattern, Object[] arguments) {
/*  239 */     if (logger.isEnabledFor(TRACE)) {
/*  240 */       forcedLog(logger, TRACE, format(pattern, arguments), t);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void debug(Logger logger, Throwable t, String pattern, Object[] arguments) {
/*  253 */     if (logger.isDebugEnabled()) {
/*  254 */       forcedLog(logger, Level.DEBUG, format(pattern, arguments), t);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void info(Logger logger, Throwable t, String pattern, Object[] arguments) {
/*  267 */     if (logger.isInfoEnabled()) {
/*  268 */       forcedLog(logger, Level.INFO, format(pattern, arguments), t);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void warn(Logger logger, Throwable t, String pattern, Object[] arguments) {
/*  281 */     if (logger.isEnabledFor(Level.WARN)) {
/*  282 */       forcedLog(logger, Level.WARN, format(pattern, arguments), t);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void error(Logger logger, Throwable t, String pattern, Object[] arguments) {
/*  295 */     if (logger.isEnabledFor(Level.ERROR)) {
/*  296 */       forcedLog(logger, Level.ERROR, format(pattern, arguments), t);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void fatal(Logger logger, Throwable t, String pattern, Object[] arguments) {
/*  309 */     if (logger.isEnabledFor(Level.FATAL)) {
/*  310 */       forcedLog(logger, Level.FATAL, format(pattern, arguments), t);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void trace(Logger logger, String pattern, boolean argument) {
/*  322 */     if (logger.isEnabledFor(TRACE)) {
/*  323 */       forcedLog(logger, TRACE, format(pattern, valueOf(argument)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void trace(Logger logger, String pattern, char argument) {
/*  335 */     if (logger.isEnabledFor(TRACE)) {
/*  336 */       forcedLog(logger, TRACE, format(pattern, valueOf(argument)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void trace(Logger logger, String pattern, byte argument) {
/*  348 */     if (logger.isEnabledFor(TRACE)) {
/*  349 */       forcedLog(logger, TRACE, format(pattern, valueOf(argument)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void trace(Logger logger, String pattern, short argument) {
/*  361 */     if (logger.isEnabledFor(TRACE)) {
/*  362 */       forcedLog(logger, TRACE, format(pattern, valueOf(argument)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void trace(Logger logger, String pattern, int argument) {
/*  374 */     if (logger.isEnabledFor(TRACE)) {
/*  375 */       forcedLog(logger, TRACE, format(pattern, valueOf(argument)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void trace(Logger logger, String pattern, long argument) {
/*  387 */     if (logger.isEnabledFor(TRACE)) {
/*  388 */       forcedLog(logger, TRACE, format(pattern, valueOf(argument)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void trace(Logger logger, String pattern, float argument) {
/*  400 */     if (logger.isEnabledFor(TRACE)) {
/*  401 */       forcedLog(logger, TRACE, format(pattern, valueOf(argument)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void trace(Logger logger, String pattern, double argument) {
/*  413 */     if (logger.isEnabledFor(TRACE)) {
/*  414 */       forcedLog(logger, TRACE, format(pattern, valueOf(argument)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void trace(Logger logger, String pattern, Object argument) {
/*  426 */     if (logger.isEnabledFor(TRACE)) {
/*  427 */       forcedLog(logger, TRACE, format(pattern, argument));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void trace(Logger logger, String pattern, Object arg0, Object arg1) {
/*  440 */     if (logger.isEnabledFor(TRACE)) {
/*  441 */       forcedLog(logger, TRACE, format(pattern, toArray(arg0, arg1)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void trace(Logger logger, String pattern, Object arg0, Object arg1, Object arg2) {
/*  456 */     if (logger.isEnabledFor(TRACE)) {
/*  457 */       forcedLog(logger, TRACE, format(pattern, toArray(arg0, arg1, arg2)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void trace(Logger logger, String pattern, Object arg0, Object arg1, Object arg2, Object arg3) {
/*  473 */     if (logger.isEnabledFor(TRACE)) {
/*  474 */       forcedLog(logger, TRACE, format(pattern, toArray(arg0, arg1, arg2, arg3)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void debug(Logger logger, String pattern, boolean argument) {
/*  486 */     if (logger.isDebugEnabled()) {
/*  487 */       forcedLog(logger, Level.DEBUG, format(pattern, valueOf(argument)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void debug(Logger logger, String pattern, char argument) {
/*  499 */     if (logger.isDebugEnabled()) {
/*  500 */       forcedLog(logger, Level.DEBUG, format(pattern, valueOf(argument)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void debug(Logger logger, String pattern, byte argument) {
/*  512 */     if (logger.isDebugEnabled()) {
/*  513 */       forcedLog(logger, Level.DEBUG, format(pattern, valueOf(argument)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void debug(Logger logger, String pattern, short argument) {
/*  525 */     if (logger.isDebugEnabled()) {
/*  526 */       forcedLog(logger, Level.DEBUG, format(pattern, valueOf(argument)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void debug(Logger logger, String pattern, int argument) {
/*  538 */     if (logger.isDebugEnabled()) {
/*  539 */       forcedLog(logger, Level.DEBUG, format(pattern, valueOf(argument)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void debug(Logger logger, String pattern, long argument) {
/*  551 */     if (logger.isDebugEnabled()) {
/*  552 */       forcedLog(logger, Level.DEBUG, format(pattern, valueOf(argument)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void debug(Logger logger, String pattern, float argument) {
/*  564 */     if (logger.isDebugEnabled()) {
/*  565 */       forcedLog(logger, Level.DEBUG, format(pattern, valueOf(argument)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void debug(Logger logger, String pattern, double argument) {
/*  577 */     if (logger.isDebugEnabled()) {
/*  578 */       forcedLog(logger, Level.DEBUG, format(pattern, valueOf(argument)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void debug(Logger logger, String pattern, Object argument) {
/*  590 */     if (logger.isDebugEnabled()) {
/*  591 */       forcedLog(logger, Level.DEBUG, format(pattern, argument));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void debug(Logger logger, String pattern, Object arg0, Object arg1) {
/*  604 */     if (logger.isDebugEnabled()) {
/*  605 */       forcedLog(logger, Level.DEBUG, format(pattern, toArray(arg0, arg1)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void debug(Logger logger, String pattern, Object arg0, Object arg1, Object arg2) {
/*  620 */     if (logger.isDebugEnabled()) {
/*  621 */       forcedLog(logger, Level.DEBUG, format(pattern, toArray(arg0, arg1, arg2)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void debug(Logger logger, String pattern, Object arg0, Object arg1, Object arg2, Object arg3) {
/*  637 */     if (logger.isDebugEnabled()) {
/*  638 */       forcedLog(logger, Level.DEBUG, format(pattern, toArray(arg0, arg1, arg2, arg3)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void info(Logger logger, String pattern, boolean argument) {
/*  650 */     if (logger.isInfoEnabled()) {
/*  651 */       forcedLog(logger, Level.INFO, format(pattern, valueOf(argument)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void info(Logger logger, String pattern, char argument) {
/*  663 */     if (logger.isInfoEnabled()) {
/*  664 */       forcedLog(logger, Level.INFO, format(pattern, valueOf(argument)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void info(Logger logger, String pattern, byte argument) {
/*  676 */     if (logger.isInfoEnabled()) {
/*  677 */       forcedLog(logger, Level.INFO, format(pattern, valueOf(argument)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void info(Logger logger, String pattern, short argument) {
/*  689 */     if (logger.isInfoEnabled()) {
/*  690 */       forcedLog(logger, Level.INFO, format(pattern, valueOf(argument)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void info(Logger logger, String pattern, int argument) {
/*  702 */     if (logger.isInfoEnabled()) {
/*  703 */       forcedLog(logger, Level.INFO, format(pattern, valueOf(argument)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void info(Logger logger, String pattern, long argument) {
/*  715 */     if (logger.isInfoEnabled()) {
/*  716 */       forcedLog(logger, Level.INFO, format(pattern, valueOf(argument)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void info(Logger logger, String pattern, float argument) {
/*  728 */     if (logger.isInfoEnabled()) {
/*  729 */       forcedLog(logger, Level.INFO, format(pattern, valueOf(argument)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void info(Logger logger, String pattern, double argument) {
/*  741 */     if (logger.isInfoEnabled()) {
/*  742 */       forcedLog(logger, Level.INFO, format(pattern, valueOf(argument)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void info(Logger logger, String pattern, Object argument) {
/*  754 */     if (logger.isInfoEnabled()) {
/*  755 */       forcedLog(logger, Level.INFO, format(pattern, argument));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void info(Logger logger, String pattern, Object arg0, Object arg1) {
/*  768 */     if (logger.isInfoEnabled()) {
/*  769 */       forcedLog(logger, Level.INFO, format(pattern, toArray(arg0, arg1)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void info(Logger logger, String pattern, Object arg0, Object arg1, Object arg2) {
/*  784 */     if (logger.isInfoEnabled()) {
/*  785 */       forcedLog(logger, Level.INFO, format(pattern, toArray(arg0, arg1, arg2)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void info(Logger logger, String pattern, Object arg0, Object arg1, Object arg2, Object arg3) {
/*  801 */     if (logger.isInfoEnabled()) {
/*  802 */       forcedLog(logger, Level.INFO, format(pattern, toArray(arg0, arg1, arg2, arg3)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void warn(Logger logger, String pattern, boolean argument) {
/*  814 */     if (logger.isEnabledFor(Level.WARN)) {
/*  815 */       forcedLog(logger, Level.WARN, format(pattern, valueOf(argument)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void warn(Logger logger, String pattern, char argument) {
/*  827 */     if (logger.isEnabledFor(Level.WARN)) {
/*  828 */       forcedLog(logger, Level.WARN, format(pattern, valueOf(argument)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void warn(Logger logger, String pattern, byte argument) {
/*  840 */     if (logger.isEnabledFor(Level.WARN)) {
/*  841 */       forcedLog(logger, Level.WARN, format(pattern, valueOf(argument)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void warn(Logger logger, String pattern, short argument) {
/*  853 */     if (logger.isEnabledFor(Level.WARN)) {
/*  854 */       forcedLog(logger, Level.WARN, format(pattern, valueOf(argument)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void warn(Logger logger, String pattern, int argument) {
/*  866 */     if (logger.isEnabledFor(Level.WARN)) {
/*  867 */       forcedLog(logger, Level.WARN, format(pattern, valueOf(argument)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void warn(Logger logger, String pattern, long argument) {
/*  879 */     if (logger.isEnabledFor(Level.WARN)) {
/*  880 */       forcedLog(logger, Level.WARN, format(pattern, valueOf(argument)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void warn(Logger logger, String pattern, float argument) {
/*  892 */     if (logger.isEnabledFor(Level.WARN)) {
/*  893 */       forcedLog(logger, Level.WARN, format(pattern, valueOf(argument)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void warn(Logger logger, String pattern, double argument) {
/*  905 */     if (logger.isEnabledFor(Level.WARN)) {
/*  906 */       forcedLog(logger, Level.WARN, format(pattern, valueOf(argument)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void warn(Logger logger, String pattern, Object argument) {
/*  918 */     if (logger.isEnabledFor(Level.WARN)) {
/*  919 */       forcedLog(logger, Level.WARN, format(pattern, argument));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void warn(Logger logger, String pattern, Object arg0, Object arg1) {
/*  932 */     if (logger.isEnabledFor(Level.WARN)) {
/*  933 */       forcedLog(logger, Level.WARN, format(pattern, toArray(arg0, arg1)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void warn(Logger logger, String pattern, Object arg0, Object arg1, Object arg2) {
/*  948 */     if (logger.isEnabledFor(Level.WARN)) {
/*  949 */       forcedLog(logger, Level.WARN, format(pattern, toArray(arg0, arg1, arg2)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void warn(Logger logger, String pattern, Object arg0, Object arg1, Object arg2, Object arg3) {
/*  965 */     if (logger.isEnabledFor(Level.WARN)) {
/*  966 */       forcedLog(logger, Level.WARN, format(pattern, toArray(arg0, arg1, arg2, arg3)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void log(Logger logger, Level level, String pattern, Object[] parameters) {
/*  979 */     if (logger.isEnabledFor(level)) {
/*  980 */       forcedLog(logger, level, format(pattern, parameters));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void log(Logger logger, Level level, Throwable t, String pattern, Object[] parameters) {
/*  995 */     if (logger.isEnabledFor(level)) {
/*  996 */       forcedLog(logger, level, format(pattern, parameters), t);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void log(Logger logger, Level level, String pattern, Object param1) {
/* 1009 */     if (logger.isEnabledFor(level)) {
/* 1010 */       forcedLog(logger, level, format(pattern, toArray(param1)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void log(Logger logger, Level level, String pattern, boolean param1) {
/* 1023 */     if (logger.isEnabledFor(level)) {
/* 1024 */       forcedLog(logger, level, format(pattern, toArray(valueOf(param1))));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void log(Logger logger, Level level, String pattern, byte param1) {
/* 1037 */     if (logger.isEnabledFor(level)) {
/* 1038 */       forcedLog(logger, level, format(pattern, toArray(valueOf(param1))));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void log(Logger logger, Level level, String pattern, char param1) {
/* 1051 */     if (logger.isEnabledFor(level)) {
/* 1052 */       forcedLog(logger, level, format(pattern, toArray(valueOf(param1))));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void log(Logger logger, Level level, String pattern, short param1) {
/* 1065 */     if (logger.isEnabledFor(level)) {
/* 1066 */       forcedLog(logger, level, format(pattern, toArray(valueOf(param1))));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void log(Logger logger, Level level, String pattern, int param1) {
/* 1079 */     if (logger.isEnabledFor(level)) {
/* 1080 */       forcedLog(logger, level, format(pattern, toArray(valueOf(param1))));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void log(Logger logger, Level level, String pattern, long param1) {
/* 1093 */     if (logger.isEnabledFor(level)) {
/* 1094 */       forcedLog(logger, level, format(pattern, toArray(valueOf(param1))));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void log(Logger logger, Level level, String pattern, float param1) {
/* 1107 */     if (logger.isEnabledFor(level)) {
/* 1108 */       forcedLog(logger, level, format(pattern, toArray(valueOf(param1))));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void log(Logger logger, Level level, String pattern, double param1) {
/* 1121 */     if (logger.isEnabledFor(level)) {
/* 1122 */       forcedLog(logger, level, format(pattern, toArray(valueOf(param1))));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void log(Logger logger, Level level, String pattern, Object arg0, Object arg1) {
/* 1137 */     if (logger.isEnabledFor(level)) {
/* 1138 */       forcedLog(logger, level, format(pattern, toArray(arg0, arg1)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void log(Logger logger, Level level, String pattern, Object arg0, Object arg1, Object arg2) {
/* 1154 */     if (logger.isEnabledFor(level)) {
/* 1155 */       forcedLog(logger, level, format(pattern, toArray(arg0, arg1, arg2)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void log(Logger logger, Level level, String pattern, Object arg0, Object arg1, Object arg2, Object arg3) {
/* 1172 */     if (logger.isEnabledFor(level)) {
/* 1173 */       forcedLog(logger, level, format(pattern, toArray(arg0, arg1, arg2, arg3)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void logrb(Logger logger, Level level, String bundleName, String key, Object[] parameters) {
/* 1188 */     if (logger.isEnabledFor(level)) {
/* 1189 */       forcedLog(logger, level, format(bundleName, key, parameters));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void logrb(Logger logger, Level level, Throwable t, String bundleName, String key, Object[] parameters) {
/* 1205 */     if (logger.isEnabledFor(level)) {
/* 1206 */       forcedLog(logger, level, format(bundleName, key, parameters), t);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void logrb(Logger logger, Level level, String bundleName, String key, Object param1) {
/* 1221 */     if (logger.isEnabledFor(level)) {
/* 1222 */       forcedLog(logger, level, format(bundleName, key, toArray(param1)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void logrb(Logger logger, Level level, String bundleName, String key, boolean param1) {
/* 1237 */     if (logger.isEnabledFor(level)) {
/* 1238 */       forcedLog(logger, level, format(bundleName, key, toArray(valueOf(param1))));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void logrb(Logger logger, Level level, String bundleName, String key, char param1) {
/* 1253 */     if (logger.isEnabledFor(level)) {
/* 1254 */       forcedLog(logger, level, format(bundleName, key, toArray(valueOf(param1))));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void logrb(Logger logger, Level level, String bundleName, String key, byte param1) {
/* 1269 */     if (logger.isEnabledFor(level)) {
/* 1270 */       forcedLog(logger, level, format(bundleName, key, toArray(valueOf(param1))));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void logrb(Logger logger, Level level, String bundleName, String key, short param1) {
/* 1285 */     if (logger.isEnabledFor(level)) {
/* 1286 */       forcedLog(logger, level, format(bundleName, key, toArray(valueOf(param1))));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void logrb(Logger logger, Level level, String bundleName, String key, int param1) {
/* 1301 */     if (logger.isEnabledFor(level)) {
/* 1302 */       forcedLog(logger, level, format(bundleName, key, toArray(valueOf(param1))));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void logrb(Logger logger, Level level, String bundleName, String key, long param1) {
/* 1317 */     if (logger.isEnabledFor(level)) {
/* 1318 */       forcedLog(logger, level, format(bundleName, key, toArray(valueOf(param1))));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void logrb(Logger logger, Level level, String bundleName, String key, float param1) {
/* 1333 */     if (logger.isEnabledFor(level)) {
/* 1334 */       forcedLog(logger, level, format(bundleName, key, toArray(valueOf(param1))));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void logrb(Logger logger, Level level, String bundleName, String key, double param1) {
/* 1349 */     if (logger.isEnabledFor(level)) {
/* 1350 */       forcedLog(logger, level, format(bundleName, key, toArray(valueOf(param1))));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void logrb(Logger logger, Level level, String bundleName, String key, Object param0, Object param1) {
/* 1366 */     if (logger.isEnabledFor(level)) {
/* 1367 */       forcedLog(logger, level, format(bundleName, key, toArray(param0, param1)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void logrb(Logger logger, Level level, String bundleName, String key, Object param0, Object param1, Object param2) {
/* 1384 */     if (logger.isEnabledFor(level)) {
/* 1385 */       forcedLog(logger, level, format(bundleName, key, toArray(param0, param1, param2)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void logrb(Logger logger, Level level, String bundleName, String key, Object param0, Object param1, Object param2, Object param3) {
/* 1403 */     if (logger.isEnabledFor(level))
/* 1404 */       forcedLog(logger, level, format(bundleName, key, toArray(param0, param1, param2, param3))); 
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/LogSF.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */