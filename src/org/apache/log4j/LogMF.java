/*      */ package org.apache.log4j;
/*      */ 
/*      */ import java.text.DateFormat;
/*      */ import java.text.MessageFormat;
/*      */ import java.text.NumberFormat;
/*      */ import java.util.Locale;
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
/*      */ public final class LogMF
/*      */   extends LogXF
/*      */ {
/*   52 */   private static NumberFormat numberFormat = null;
/*      */ 
/*      */ 
/*      */   
/*   56 */   private static Locale numberLocale = null;
/*      */ 
/*      */ 
/*      */   
/*   60 */   private static DateFormat dateFormat = null;
/*      */ 
/*      */ 
/*      */   
/*   64 */   private static Locale dateLocale = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static synchronized String formatNumber(Object n) {
/*   73 */     Locale currentLocale = Locale.getDefault();
/*   74 */     if (currentLocale != numberLocale || numberFormat == null) {
/*   75 */       numberLocale = currentLocale;
/*   76 */       numberFormat = NumberFormat.getInstance(currentLocale);
/*      */     } 
/*   78 */     return numberFormat.format(n);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static synchronized String formatDate(Object d) {
/*   88 */     Locale currentLocale = Locale.getDefault();
/*   89 */     if (currentLocale != dateLocale || dateFormat == null) {
/*   90 */       dateLocale = currentLocale;
/*   91 */       dateFormat = DateFormat.getDateTimeInstance(3, 3, currentLocale);
/*      */     } 
/*   93 */     return dateFormat.format(d);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String formatObject(Object arg0) {
/*  103 */     if (arg0 instanceof String)
/*  104 */       return arg0.toString(); 
/*  105 */     if (arg0 instanceof Double || arg0 instanceof Float)
/*  106 */       return formatNumber(arg0); 
/*  107 */     if (arg0 instanceof java.util.Date) {
/*  108 */       return formatDate(arg0);
/*      */     }
/*  110 */     return String.valueOf(arg0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isSimple(String pattern) {
/*  120 */     if (pattern.indexOf('\'') != -1) {
/*  121 */       return false;
/*      */     }
/*  123 */     for (int pos = pattern.indexOf('{'); pos != -1; pos = pattern.indexOf('{', pos + 1)) {
/*  124 */       if (pos + 2 >= pattern.length() || pattern.charAt(pos + 2) != '}' || pattern.charAt(pos + 1) < '0' || pattern
/*  125 */         .charAt(pos + 1) > '9') {
/*  126 */         return false;
/*      */       }
/*      */     } 
/*  129 */     return true;
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
/*      */   private static String format(String pattern, Object[] arguments) {
/*  141 */     if (pattern == null)
/*  142 */       return null; 
/*  143 */     if (isSimple(pattern)) {
/*  144 */       String[] formatted = new String[10];
/*  145 */       int prev = 0;
/*  146 */       String retval = "";
/*  147 */       int pos = pattern.indexOf('{');
/*  148 */       while (pos >= 0) {
/*  149 */         if (pos + 2 < pattern.length() && pattern.charAt(pos + 2) == '}' && pattern.charAt(pos + 1) >= '0' && pattern
/*  150 */           .charAt(pos + 1) <= '9') {
/*  151 */           int index = pattern.charAt(pos + 1) - 48;
/*  152 */           retval = retval + pattern.substring(prev, pos);
/*  153 */           if (formatted[index] == null) {
/*  154 */             if (arguments == null || index >= arguments.length) {
/*  155 */               formatted[index] = pattern.substring(pos, pos + 3);
/*      */             } else {
/*  157 */               formatted[index] = formatObject(arguments[index]);
/*      */             } 
/*      */           }
/*  160 */           retval = retval + formatted[index];
/*  161 */           prev = pos + 3;
/*  162 */           pos = pattern.indexOf('{', prev); continue;
/*      */         } 
/*  164 */         pos = pattern.indexOf('{', pos + 1);
/*      */       } 
/*      */       
/*  167 */       retval = retval + pattern.substring(prev);
/*  168 */       return retval;
/*      */     } 
/*      */     try {
/*  171 */       return MessageFormat.format(pattern, arguments);
/*  172 */     } catch (IllegalArgumentException ex) {
/*  173 */       return pattern;
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
/*      */   private static String format(String pattern, Object arg0) {
/*  185 */     if (pattern == null)
/*  186 */       return null; 
/*  187 */     if (isSimple(pattern)) {
/*  188 */       String formatted = null;
/*  189 */       int prev = 0;
/*  190 */       String retval = "";
/*  191 */       int pos = pattern.indexOf('{');
/*  192 */       while (pos >= 0) {
/*  193 */         if (pos + 2 < pattern.length() && pattern.charAt(pos + 2) == '}' && pattern.charAt(pos + 1) >= '0' && pattern
/*  194 */           .charAt(pos + 1) <= '9') {
/*  195 */           int index = pattern.charAt(pos + 1) - 48;
/*  196 */           retval = retval + pattern.substring(prev, pos);
/*  197 */           if (index != 0) {
/*  198 */             retval = retval + pattern.substring(pos, pos + 3);
/*      */           } else {
/*  200 */             if (formatted == null) {
/*  201 */               formatted = formatObject(arg0);
/*      */             }
/*  203 */             retval = retval + formatted;
/*      */           } 
/*  205 */           prev = pos + 3;
/*  206 */           pos = pattern.indexOf('{', prev); continue;
/*      */         } 
/*  208 */         pos = pattern.indexOf('{', pos + 1);
/*      */       } 
/*      */       
/*  211 */       retval = retval + pattern.substring(prev);
/*  212 */       return retval;
/*      */     } 
/*      */     try {
/*  215 */       return MessageFormat.format(pattern, new Object[] { arg0 });
/*  216 */     } catch (IllegalArgumentException ex) {
/*  217 */       return pattern;
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
/*      */   private static String format(String resourceBundleName, String key, Object[] arguments) {
/*      */     String pattern;
/*  231 */     if (resourceBundleName != null) {
/*      */       try {
/*  233 */         ResourceBundle bundle = ResourceBundle.getBundle(resourceBundleName);
/*  234 */         pattern = bundle.getString(key);
/*  235 */       } catch (Exception ex) {
/*  236 */         pattern = key;
/*      */       } 
/*      */     } else {
/*  239 */       pattern = key;
/*      */     } 
/*  241 */     return format(pattern, arguments);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  247 */   private static final String FQCN = LogMF.class.getName();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void forcedLog(Logger logger, Level level, String msg) {
/*  257 */     logger.callAppenders(new LoggingEvent(FQCN, logger, level, msg, null));
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
/*  269 */     logger.callAppenders(new LoggingEvent(FQCN, logger, level, msg, t));
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
/*  280 */     if (logger.isEnabledFor(TRACE)) {
/*  281 */       forcedLog(logger, TRACE, format(pattern, arguments));
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
/*  293 */     if (logger.isDebugEnabled()) {
/*  294 */       forcedLog(logger, Level.DEBUG, format(pattern, arguments));
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
/*  306 */     if (logger.isInfoEnabled()) {
/*  307 */       forcedLog(logger, Level.INFO, format(pattern, arguments));
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
/*  319 */     if (logger.isEnabledFor(Level.WARN)) {
/*  320 */       forcedLog(logger, Level.WARN, format(pattern, arguments));
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
/*  332 */     if (logger.isEnabledFor(Level.ERROR)) {
/*  333 */       forcedLog(logger, Level.ERROR, format(pattern, arguments));
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
/*  345 */     if (logger.isEnabledFor(Level.FATAL)) {
/*  346 */       forcedLog(logger, Level.FATAL, format(pattern, arguments));
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
/*  359 */     if (logger.isEnabledFor(TRACE)) {
/*  360 */       forcedLog(logger, TRACE, format(pattern, arguments), t);
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
/*  373 */     if (logger.isDebugEnabled()) {
/*  374 */       forcedLog(logger, Level.DEBUG, format(pattern, arguments), t);
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
/*  387 */     if (logger.isInfoEnabled()) {
/*  388 */       forcedLog(logger, Level.INFO, format(pattern, arguments), t);
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
/*  401 */     if (logger.isEnabledFor(Level.WARN)) {
/*  402 */       forcedLog(logger, Level.WARN, format(pattern, arguments), t);
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
/*  415 */     if (logger.isEnabledFor(Level.ERROR)) {
/*  416 */       forcedLog(logger, Level.ERROR, format(pattern, arguments), t);
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
/*  429 */     if (logger.isEnabledFor(Level.FATAL)) {
/*  430 */       forcedLog(logger, Level.FATAL, format(pattern, arguments), t);
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
/*  442 */     if (logger.isEnabledFor(TRACE)) {
/*  443 */       forcedLog(logger, TRACE, format(pattern, valueOf(argument)));
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
/*  455 */     if (logger.isEnabledFor(TRACE)) {
/*  456 */       forcedLog(logger, TRACE, format(pattern, valueOf(argument)));
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
/*  468 */     if (logger.isEnabledFor(TRACE)) {
/*  469 */       forcedLog(logger, TRACE, format(pattern, valueOf(argument)));
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
/*  481 */     if (logger.isEnabledFor(TRACE)) {
/*  482 */       forcedLog(logger, TRACE, format(pattern, valueOf(argument)));
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
/*  494 */     if (logger.isEnabledFor(TRACE)) {
/*  495 */       forcedLog(logger, TRACE, format(pattern, valueOf(argument)));
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
/*  507 */     if (logger.isEnabledFor(TRACE)) {
/*  508 */       forcedLog(logger, TRACE, format(pattern, valueOf(argument)));
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
/*  520 */     if (logger.isEnabledFor(TRACE)) {
/*  521 */       forcedLog(logger, TRACE, format(pattern, valueOf(argument)));
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
/*  533 */     if (logger.isEnabledFor(TRACE)) {
/*  534 */       forcedLog(logger, TRACE, format(pattern, valueOf(argument)));
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
/*  546 */     if (logger.isEnabledFor(TRACE)) {
/*  547 */       forcedLog(logger, TRACE, format(pattern, argument));
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
/*  560 */     if (logger.isEnabledFor(TRACE)) {
/*  561 */       forcedLog(logger, TRACE, format(pattern, toArray(arg0, arg1)));
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
/*  576 */     if (logger.isEnabledFor(TRACE)) {
/*  577 */       forcedLog(logger, TRACE, format(pattern, toArray(arg0, arg1, arg2)));
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
/*  593 */     if (logger.isEnabledFor(TRACE)) {
/*  594 */       forcedLog(logger, TRACE, format(pattern, toArray(arg0, arg1, arg2, arg3)));
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
/*  606 */     if (logger.isDebugEnabled()) {
/*  607 */       forcedLog(logger, Level.DEBUG, format(pattern, valueOf(argument)));
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
/*  619 */     if (logger.isDebugEnabled()) {
/*  620 */       forcedLog(logger, Level.DEBUG, format(pattern, valueOf(argument)));
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
/*  632 */     if (logger.isDebugEnabled()) {
/*  633 */       forcedLog(logger, Level.DEBUG, format(pattern, valueOf(argument)));
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
/*  645 */     if (logger.isDebugEnabled()) {
/*  646 */       forcedLog(logger, Level.DEBUG, format(pattern, valueOf(argument)));
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
/*  658 */     if (logger.isDebugEnabled()) {
/*  659 */       forcedLog(logger, Level.DEBUG, format(pattern, valueOf(argument)));
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
/*  671 */     if (logger.isDebugEnabled()) {
/*  672 */       forcedLog(logger, Level.DEBUG, format(pattern, valueOf(argument)));
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
/*  684 */     if (logger.isDebugEnabled()) {
/*  685 */       forcedLog(logger, Level.DEBUG, format(pattern, valueOf(argument)));
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
/*  697 */     if (logger.isDebugEnabled()) {
/*  698 */       forcedLog(logger, Level.DEBUG, format(pattern, valueOf(argument)));
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
/*  710 */     if (logger.isDebugEnabled()) {
/*  711 */       forcedLog(logger, Level.DEBUG, format(pattern, argument));
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
/*  724 */     if (logger.isDebugEnabled()) {
/*  725 */       forcedLog(logger, Level.DEBUG, format(pattern, toArray(arg0, arg1)));
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
/*  740 */     if (logger.isDebugEnabled()) {
/*  741 */       forcedLog(logger, Level.DEBUG, format(pattern, toArray(arg0, arg1, arg2)));
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
/*  757 */     if (logger.isDebugEnabled()) {
/*  758 */       forcedLog(logger, Level.DEBUG, format(pattern, toArray(arg0, arg1, arg2, arg3)));
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
/*  770 */     if (logger.isInfoEnabled()) {
/*  771 */       forcedLog(logger, Level.INFO, format(pattern, valueOf(argument)));
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
/*  783 */     if (logger.isInfoEnabled()) {
/*  784 */       forcedLog(logger, Level.INFO, format(pattern, valueOf(argument)));
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
/*  796 */     if (logger.isInfoEnabled()) {
/*  797 */       forcedLog(logger, Level.INFO, format(pattern, valueOf(argument)));
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
/*  809 */     if (logger.isInfoEnabled()) {
/*  810 */       forcedLog(logger, Level.INFO, format(pattern, valueOf(argument)));
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
/*  822 */     if (logger.isInfoEnabled()) {
/*  823 */       forcedLog(logger, Level.INFO, format(pattern, valueOf(argument)));
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
/*  835 */     if (logger.isInfoEnabled()) {
/*  836 */       forcedLog(logger, Level.INFO, format(pattern, valueOf(argument)));
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
/*  848 */     if (logger.isInfoEnabled()) {
/*  849 */       forcedLog(logger, Level.INFO, format(pattern, valueOf(argument)));
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
/*  861 */     if (logger.isInfoEnabled()) {
/*  862 */       forcedLog(logger, Level.INFO, format(pattern, valueOf(argument)));
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
/*  874 */     if (logger.isInfoEnabled()) {
/*  875 */       forcedLog(logger, Level.INFO, format(pattern, argument));
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
/*  888 */     if (logger.isInfoEnabled()) {
/*  889 */       forcedLog(logger, Level.INFO, format(pattern, toArray(arg0, arg1)));
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
/*  904 */     if (logger.isInfoEnabled()) {
/*  905 */       forcedLog(logger, Level.INFO, format(pattern, toArray(arg0, arg1, arg2)));
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
/*  921 */     if (logger.isInfoEnabled()) {
/*  922 */       forcedLog(logger, Level.INFO, format(pattern, toArray(arg0, arg1, arg2, arg3)));
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
/*  934 */     if (logger.isEnabledFor(Level.WARN)) {
/*  935 */       forcedLog(logger, Level.WARN, format(pattern, valueOf(argument)));
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
/*  947 */     if (logger.isEnabledFor(Level.WARN)) {
/*  948 */       forcedLog(logger, Level.WARN, format(pattern, valueOf(argument)));
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
/*  960 */     if (logger.isEnabledFor(Level.WARN)) {
/*  961 */       forcedLog(logger, Level.WARN, format(pattern, valueOf(argument)));
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
/*  973 */     if (logger.isEnabledFor(Level.WARN)) {
/*  974 */       forcedLog(logger, Level.WARN, format(pattern, valueOf(argument)));
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
/*  986 */     if (logger.isEnabledFor(Level.WARN)) {
/*  987 */       forcedLog(logger, Level.WARN, format(pattern, valueOf(argument)));
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
/*  999 */     if (logger.isEnabledFor(Level.WARN)) {
/* 1000 */       forcedLog(logger, Level.WARN, format(pattern, valueOf(argument)));
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
/* 1012 */     if (logger.isEnabledFor(Level.WARN)) {
/* 1013 */       forcedLog(logger, Level.WARN, format(pattern, valueOf(argument)));
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
/* 1025 */     if (logger.isEnabledFor(Level.WARN)) {
/* 1026 */       forcedLog(logger, Level.WARN, format(pattern, valueOf(argument)));
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
/* 1038 */     if (logger.isEnabledFor(Level.WARN)) {
/* 1039 */       forcedLog(logger, Level.WARN, format(pattern, argument));
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
/* 1052 */     if (logger.isEnabledFor(Level.WARN)) {
/* 1053 */       forcedLog(logger, Level.WARN, format(pattern, toArray(arg0, arg1)));
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
/* 1068 */     if (logger.isEnabledFor(Level.WARN)) {
/* 1069 */       forcedLog(logger, Level.WARN, format(pattern, toArray(arg0, arg1, arg2)));
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
/* 1085 */     if (logger.isEnabledFor(Level.WARN)) {
/* 1086 */       forcedLog(logger, Level.WARN, format(pattern, toArray(arg0, arg1, arg2, arg3)));
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
/* 1099 */     if (logger.isEnabledFor(level)) {
/* 1100 */       forcedLog(logger, level, format(pattern, parameters));
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
/* 1115 */     if (logger.isEnabledFor(level)) {
/* 1116 */       forcedLog(logger, level, format(pattern, parameters), t);
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
/* 1129 */     if (logger.isEnabledFor(level)) {
/* 1130 */       forcedLog(logger, level, format(pattern, toArray(param1)));
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
/* 1143 */     if (logger.isEnabledFor(level)) {
/* 1144 */       forcedLog(logger, level, format(pattern, toArray(valueOf(param1))));
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
/* 1157 */     if (logger.isEnabledFor(level)) {
/* 1158 */       forcedLog(logger, level, format(pattern, toArray(valueOf(param1))));
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
/* 1171 */     if (logger.isEnabledFor(level)) {
/* 1172 */       forcedLog(logger, level, format(pattern, toArray(valueOf(param1))));
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
/* 1185 */     if (logger.isEnabledFor(level)) {
/* 1186 */       forcedLog(logger, level, format(pattern, toArray(valueOf(param1))));
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
/* 1199 */     if (logger.isEnabledFor(level)) {
/* 1200 */       forcedLog(logger, level, format(pattern, toArray(valueOf(param1))));
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
/* 1213 */     if (logger.isEnabledFor(level)) {
/* 1214 */       forcedLog(logger, level, format(pattern, toArray(valueOf(param1))));
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
/* 1227 */     if (logger.isEnabledFor(level)) {
/* 1228 */       forcedLog(logger, level, format(pattern, toArray(valueOf(param1))));
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
/* 1241 */     if (logger.isEnabledFor(level)) {
/* 1242 */       forcedLog(logger, level, format(pattern, toArray(valueOf(param1))));
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
/* 1257 */     if (logger.isEnabledFor(level)) {
/* 1258 */       forcedLog(logger, level, format(pattern, toArray(arg0, arg1)));
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
/* 1274 */     if (logger.isEnabledFor(level)) {
/* 1275 */       forcedLog(logger, level, format(pattern, toArray(arg0, arg1, arg2)));
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
/* 1292 */     if (logger.isEnabledFor(level)) {
/* 1293 */       forcedLog(logger, level, format(pattern, toArray(arg0, arg1, arg2, arg3)));
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
/* 1308 */     if (logger.isEnabledFor(level)) {
/* 1309 */       forcedLog(logger, level, format(bundleName, key, parameters));
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
/* 1325 */     if (logger.isEnabledFor(level)) {
/* 1326 */       forcedLog(logger, level, format(bundleName, key, parameters), t);
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
/* 1341 */     if (logger.isEnabledFor(level)) {
/* 1342 */       forcedLog(logger, level, format(bundleName, key, toArray(param1)));
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
/* 1357 */     if (logger.isEnabledFor(level)) {
/* 1358 */       forcedLog(logger, level, format(bundleName, key, toArray(valueOf(param1))));
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
/* 1373 */     if (logger.isEnabledFor(level)) {
/* 1374 */       forcedLog(logger, level, format(bundleName, key, toArray(valueOf(param1))));
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
/* 1389 */     if (logger.isEnabledFor(level)) {
/* 1390 */       forcedLog(logger, level, format(bundleName, key, toArray(valueOf(param1))));
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
/* 1405 */     if (logger.isEnabledFor(level)) {
/* 1406 */       forcedLog(logger, level, format(bundleName, key, toArray(valueOf(param1))));
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
/* 1421 */     if (logger.isEnabledFor(level)) {
/* 1422 */       forcedLog(logger, level, format(bundleName, key, toArray(valueOf(param1))));
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
/* 1437 */     if (logger.isEnabledFor(level)) {
/* 1438 */       forcedLog(logger, level, format(bundleName, key, toArray(valueOf(param1))));
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
/* 1453 */     if (logger.isEnabledFor(level)) {
/* 1454 */       forcedLog(logger, level, format(bundleName, key, toArray(valueOf(param1))));
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
/* 1469 */     if (logger.isEnabledFor(level)) {
/* 1470 */       forcedLog(logger, level, format(bundleName, key, toArray(valueOf(param1))));
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
/* 1486 */     if (logger.isEnabledFor(level)) {
/* 1487 */       forcedLog(logger, level, format(bundleName, key, toArray(param0, param1)));
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
/* 1504 */     if (logger.isEnabledFor(level)) {
/* 1505 */       forcedLog(logger, level, format(bundleName, key, toArray(param0, param1, param2)));
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
/* 1523 */     if (logger.isEnabledFor(level))
/* 1524 */       forcedLog(logger, level, format(bundleName, key, toArray(param0, param1, param2, param3))); 
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/LogMF.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */