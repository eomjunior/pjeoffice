/*      */ package org.apache.log4j;
/*      */ 
/*      */ import java.text.MessageFormat;
/*      */ import java.util.Enumeration;
/*      */ import java.util.MissingResourceException;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.Vector;
/*      */ import org.apache.log4j.helpers.AppenderAttachableImpl;
/*      */ import org.apache.log4j.helpers.NullEnumeration;
/*      */ import org.apache.log4j.spi.AppenderAttachable;
/*      */ import org.apache.log4j.spi.HierarchyEventListener;
/*      */ import org.apache.log4j.spi.LoggerRepository;
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
/*      */ public class Category
/*      */   implements AppenderAttachable
/*      */ {
/*      */   protected String name;
/*      */   protected volatile Level level;
/*      */   protected volatile Category parent;
/*  121 */   private static final String FQCN = Category.class.getName();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ResourceBundle resourceBundle;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected LoggerRepository repository;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   AppenderAttachableImpl aai;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean additive = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Category(String name) {
/*  151 */     this.name = name;
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
/*      */   public synchronized void addAppender(Appender newAppender) {
/*  163 */     if (this.aai == null) {
/*  164 */       this.aai = new AppenderAttachableImpl();
/*      */     }
/*  166 */     this.aai.addAppender(newAppender);
/*  167 */     this.repository.fireAddAppenderEvent(this, newAppender);
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
/*      */   public void assertLog(boolean assertion, String msg) {
/*  184 */     if (!assertion) {
/*  185 */       error(msg);
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
/*      */   public void callAppenders(LoggingEvent event) {
/*  200 */     int writes = 0;
/*      */     
/*  202 */     for (Category c = this; c != null; c = c.parent) {
/*      */       
/*  204 */       synchronized (c) {
/*  205 */         if (c.aai != null) {
/*  206 */           writes += c.aai.appendLoopOnAppenders(event);
/*      */         }
/*  208 */         if (!c.additive) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  214 */     if (writes == 0) {
/*  215 */       this.repository.emitNoAppenderWarning(this);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   synchronized void closeNestedAppenders() {
/*  225 */     Enumeration<Appender> enumeration = getAllAppenders();
/*  226 */     if (enumeration != null) {
/*  227 */       while (enumeration.hasMoreElements()) {
/*  228 */         Appender a = enumeration.nextElement();
/*  229 */         if (a instanceof AppenderAttachable) {
/*  230 */           a.close();
/*      */         }
/*      */       } 
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void debug(Object message) {
/*  256 */     if (this.repository.isDisabled(10000))
/*      */       return; 
/*  258 */     if (Level.DEBUG.isGreaterOrEqual(getEffectiveLevel())) {
/*  259 */       forcedLog(FQCN, Level.DEBUG, message, null);
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
/*      */   public void debug(Object message, Throwable t) {
/*  274 */     if (this.repository.isDisabled(10000))
/*      */       return; 
/*  276 */     if (Level.DEBUG.isGreaterOrEqual(getEffectiveLevel())) {
/*  277 */       forcedLog(FQCN, Level.DEBUG, message, t);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(Object message) {
/*  300 */     if (this.repository.isDisabled(40000))
/*      */       return; 
/*  302 */     if (Level.ERROR.isGreaterOrEqual(getEffectiveLevel())) {
/*  303 */       forcedLog(FQCN, Level.ERROR, message, null);
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
/*      */   public void error(Object message, Throwable t) {
/*  317 */     if (this.repository.isDisabled(40000))
/*      */       return; 
/*  319 */     if (Level.ERROR.isGreaterOrEqual(getEffectiveLevel())) {
/*  320 */       forcedLog(FQCN, Level.ERROR, message, t);
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
/*      */   public static Logger exists(String name) {
/*  333 */     return LogManager.exists(name);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(Object message) {
/*  356 */     if (this.repository.isDisabled(50000))
/*      */       return; 
/*  358 */     if (Level.FATAL.isGreaterOrEqual(getEffectiveLevel())) {
/*  359 */       forcedLog(FQCN, Level.FATAL, message, null);
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
/*      */   public void fatal(Object message, Throwable t) {
/*  373 */     if (this.repository.isDisabled(50000))
/*      */       return; 
/*  375 */     if (Level.FATAL.isGreaterOrEqual(getEffectiveLevel())) {
/*  376 */       forcedLog(FQCN, Level.FATAL, message, t);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void forcedLog(String fqcn, Priority level, Object message, Throwable t) {
/*  384 */     callAppenders(new LoggingEvent(fqcn, this, level, message, t));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getAdditivity() {
/*  391 */     return this.additive;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Enumeration getAllAppenders() {
/*  401 */     if (this.aai == null) {
/*  402 */       return (Enumeration)NullEnumeration.getInstance();
/*      */     }
/*  404 */     return this.aai.getAllAppenders();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Appender getAppender(String name) {
/*  415 */     if (this.aai == null || name == null) {
/*  416 */       return null;
/*      */     }
/*  418 */     return this.aai.getAppender(name);
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
/*      */   public Level getEffectiveLevel() {
/*  430 */     for (Category c = this; c != null; c = c.parent) {
/*  431 */       if (c.level != null)
/*  432 */         return c.level; 
/*      */     } 
/*  434 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Priority getChainedPriority() {
/*  442 */     for (Category c = this; c != null; c = c.parent) {
/*  443 */       if (c.level != null)
/*  444 */         return c.level; 
/*      */     } 
/*  446 */     return null;
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
/*      */   public static Enumeration getCurrentCategories() {
/*  460 */     return LogManager.getCurrentLoggers();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static LoggerRepository getDefaultHierarchy() {
/*  471 */     return LogManager.getLoggerRepository();
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
/*      */   public LoggerRepository getHierarchy() {
/*  483 */     return this.repository;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public LoggerRepository getLoggerRepository() {
/*  493 */     return this.repository;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Category getInstance(String name) {
/*  500 */     return LogManager.getLogger(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Category getInstance(Class clazz) {
/*  507 */     return LogManager.getLogger(clazz);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getName() {
/*  514 */     return this.name;
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
/*      */   public final Category getParent() {
/*  527 */     return this.parent;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Level getLevel() {
/*  536 */     return this.level;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Level getPriority() {
/*  543 */     return this.level;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Category getRoot() {
/*  550 */     return LogManager.getRootLogger();
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
/*      */   public ResourceBundle getResourceBundle() {
/*  565 */     for (Category c = this; c != null; c = c.parent) {
/*  566 */       if (c.resourceBundle != null) {
/*  567 */         return c.resourceBundle;
/*      */       }
/*      */     } 
/*  570 */     return null;
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
/*      */   protected String getResourceBundleString(String key) {
/*  582 */     ResourceBundle rb = getResourceBundle();
/*      */ 
/*      */     
/*  585 */     if (rb == null)
/*      */     {
/*      */ 
/*      */ 
/*      */       
/*  590 */       return null;
/*      */     }
/*      */     try {
/*  593 */       return rb.getString(key);
/*  594 */     } catch (MissingResourceException mre) {
/*  595 */       error("No resource is associated with key \"" + key + "\".");
/*  596 */       return null;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void info(Object message) {
/*  621 */     if (this.repository.isDisabled(20000))
/*      */       return; 
/*  623 */     if (Level.INFO.isGreaterOrEqual(getEffectiveLevel())) {
/*  624 */       forcedLog(FQCN, Level.INFO, message, null);
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
/*      */   public void info(Object message, Throwable t) {
/*  638 */     if (this.repository.isDisabled(20000))
/*      */       return; 
/*  640 */     if (Level.INFO.isGreaterOrEqual(getEffectiveLevel())) {
/*  641 */       forcedLog(FQCN, Level.INFO, message, t);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAttached(Appender appender) {
/*  648 */     if (appender == null || this.aai == null) {
/*  649 */       return false;
/*      */     }
/*  651 */     return this.aai.isAttached(appender);
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
/*      */   public boolean isDebugEnabled() {
/*  694 */     if (this.repository.isDisabled(10000))
/*  695 */       return false; 
/*  696 */     return Level.DEBUG.isGreaterOrEqual(getEffectiveLevel());
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
/*      */   public boolean isEnabledFor(Priority level) {
/*  708 */     if (this.repository.isDisabled(level.level))
/*  709 */       return false; 
/*  710 */     return level.isGreaterOrEqual(getEffectiveLevel());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isInfoEnabled() {
/*  721 */     if (this.repository.isDisabled(20000))
/*  722 */       return false; 
/*  723 */     return Level.INFO.isGreaterOrEqual(getEffectiveLevel());
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
/*      */   public void l7dlog(Priority priority, String key, Throwable t) {
/*  735 */     if (this.repository.isDisabled(priority.level)) {
/*      */       return;
/*      */     }
/*  738 */     if (priority.isGreaterOrEqual(getEffectiveLevel())) {
/*  739 */       String msg = getResourceBundleString(key);
/*      */ 
/*      */       
/*  742 */       if (msg == null) {
/*  743 */         msg = key;
/*      */       }
/*  745 */       forcedLog(FQCN, priority, msg, t);
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
/*      */   public void l7dlog(Priority priority, String key, Object[] params, Throwable t) {
/*  759 */     if (this.repository.isDisabled(priority.level)) {
/*      */       return;
/*      */     }
/*  762 */     if (priority.isGreaterOrEqual(getEffectiveLevel())) {
/*  763 */       String msg, pattern = getResourceBundleString(key);
/*      */       
/*  765 */       if (pattern == null) {
/*  766 */         msg = key;
/*      */       } else {
/*  768 */         msg = MessageFormat.format(pattern, params);
/*  769 */       }  forcedLog(FQCN, priority, msg, t);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(Priority priority, Object message, Throwable t) {
/*  777 */     if (this.repository.isDisabled(priority.level)) {
/*      */       return;
/*      */     }
/*  780 */     if (priority.isGreaterOrEqual(getEffectiveLevel())) {
/*  781 */       forcedLog(FQCN, priority, message, t);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(Priority priority, Object message) {
/*  788 */     if (this.repository.isDisabled(priority.level)) {
/*      */       return;
/*      */     }
/*  791 */     if (priority.isGreaterOrEqual(getEffectiveLevel())) {
/*  792 */       forcedLog(FQCN, priority, message, null);
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
/*      */   public void log(String callerFQCN, Priority level, Object message, Throwable t) {
/*  806 */     if (this.repository.isDisabled(level.level)) {
/*      */       return;
/*      */     }
/*  809 */     if (level.isGreaterOrEqual(getEffectiveLevel())) {
/*  810 */       forcedLog(callerFQCN, level, message, t);
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
/*      */   private void fireRemoveAppenderEvent(Appender appender) {
/*  822 */     if (appender != null) {
/*  823 */       if (this.repository instanceof Hierarchy) {
/*  824 */         ((Hierarchy)this.repository).fireRemoveAppenderEvent(this, appender);
/*  825 */       } else if (this.repository instanceof HierarchyEventListener) {
/*  826 */         ((HierarchyEventListener)this.repository).removeAppenderEvent(this, appender);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void removeAllAppenders() {
/*  838 */     if (this.aai != null) {
/*  839 */       Vector appenders = new Vector(); Enumeration<Appender> iter;
/*  840 */       for (iter = this.aai.getAllAppenders(); iter != null && iter.hasMoreElements();) {
/*  841 */         appenders.add(iter.nextElement());
/*      */       }
/*  843 */       this.aai.removeAllAppenders();
/*  844 */       for (iter = appenders.elements(); iter.hasMoreElements();) {
/*  845 */         fireRemoveAppenderEvent(iter.nextElement());
/*      */       }
/*  847 */       this.aai = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void removeAppender(Appender appender) {
/*  857 */     if (appender == null || this.aai == null)
/*      */       return; 
/*  859 */     boolean wasAttached = this.aai.isAttached(appender);
/*  860 */     this.aai.removeAppender(appender);
/*  861 */     if (wasAttached) {
/*  862 */       fireRemoveAppenderEvent(appender);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void removeAppender(String name) {
/*  873 */     if (name == null || this.aai == null)
/*      */       return; 
/*  875 */     Appender appender = this.aai.getAppender(name);
/*  876 */     this.aai.removeAppender(name);
/*  877 */     if (appender != null) {
/*  878 */       fireRemoveAppenderEvent(appender);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAdditivity(boolean additive) {
/*  888 */     this.additive = additive;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void setHierarchy(LoggerRepository repository) {
/*  896 */     this.repository = repository;
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLevel(Level level) {
/*  917 */     this.level = level;
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
/*      */   public void setPriority(Priority priority) {
/*  929 */     this.level = (Level)priority;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setResourceBundle(ResourceBundle bundle) {
/*  940 */     this.resourceBundle = bundle;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void shutdown() {
/*  962 */     LogManager.shutdown();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void warn(Object message) {
/*  986 */     if (this.repository.isDisabled(30000)) {
/*      */       return;
/*      */     }
/*  989 */     if (Level.WARN.isGreaterOrEqual(getEffectiveLevel())) {
/*  990 */       forcedLog(FQCN, Level.WARN, message, null);
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
/*      */   public void warn(Object message, Throwable t) {
/* 1004 */     if (this.repository.isDisabled(30000))
/*      */       return; 
/* 1006 */     if (Level.WARN.isGreaterOrEqual(getEffectiveLevel()))
/* 1007 */       forcedLog(FQCN, Level.WARN, message, t); 
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/Category.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */