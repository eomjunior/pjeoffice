/*     */ package org.apache.log4j;
/*     */ 
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ import org.apache.log4j.config.PropertySetter;
/*     */ import org.apache.log4j.helpers.LogLog;
/*     */ import org.apache.log4j.helpers.OptionConverter;
/*     */ import org.apache.log4j.or.RendererMap;
/*     */ import org.apache.log4j.spi.Configurator;
/*     */ import org.apache.log4j.spi.ErrorHandler;
/*     */ import org.apache.log4j.spi.Filter;
/*     */ import org.apache.log4j.spi.LoggerFactory;
/*     */ import org.apache.log4j.spi.LoggerRepository;
/*     */ import org.apache.log4j.spi.RendererSupport;
/*     */ import org.apache.log4j.spi.ThrowableRenderer;
/*     */ import org.apache.log4j.spi.ThrowableRendererSupport;
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
/*     */ public class PropertyConfigurator
/*     */   implements Configurator
/*     */ {
/*  99 */   protected Hashtable registry = new Hashtable<Object, Object>(11);
/*     */   private LoggerRepository repository;
/* 101 */   protected LoggerFactory loggerFactory = new DefaultCategoryFactory();
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
/*     */   static final String CATEGORY_PREFIX = "log4j.category.";
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
/*     */   static final String LOGGER_PREFIX = "log4j.logger.";
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
/*     */   static final String FACTORY_PREFIX = "log4j.factory";
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
/*     */   static final String ADDITIVITY_PREFIX = "log4j.additivity.";
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
/*     */   static final String ROOT_CATEGORY_PREFIX = "log4j.rootCategory";
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
/*     */   static final String ROOT_LOGGER_PREFIX = "log4j.rootLogger";
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
/*     */   static final String APPENDER_PREFIX = "log4j.appender.";
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
/*     */   static final String RENDERER_PREFIX = "log4j.renderer.";
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
/*     */   static final String THRESHOLD_PREFIX = "log4j.threshold";
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
/*     */   private static final String THROWABLE_RENDERER_PREFIX = "log4j.throwableRenderer";
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
/*     */   private static final String LOGGER_REF = "logger-ref";
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
/*     */   private static final String ROOT_REF = "root-ref";
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
/*     */   private static final String APPENDER_REF_TAG = "appender-ref";
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
/*     */   public static final String LOGGER_FACTORY_KEY = "log4j.loggerFactory";
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
/*     */   private static final String RESET_KEY = "log4j.reset";
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
/*     */   private static final String INTERNAL_ROOT_NAME = "root";
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
/*     */   public void doConfigure(String configFileName, LoggerRepository hierarchy) {
/* 396 */     Properties props = new Properties();
/* 397 */     FileInputStream istream = null;
/*     */     try {
/* 399 */       istream = new FileInputStream(configFileName);
/* 400 */       props.load(istream);
/* 401 */       istream.close();
/* 402 */     } catch (Exception e) {
/* 403 */       if (e instanceof InterruptedIOException || e instanceof InterruptedException) {
/* 404 */         Thread.currentThread().interrupt();
/*     */       }
/* 406 */       LogLog.error("Could not read configuration file [" + configFileName + "].", e);
/* 407 */       LogLog.error("Ignoring configuration file [" + configFileName + "].");
/*     */       return;
/*     */     } finally {
/* 410 */       if (istream != null) {
/*     */         try {
/* 412 */           istream.close();
/* 413 */         } catch (InterruptedIOException ignore) {
/* 414 */           Thread.currentThread().interrupt();
/* 415 */         } catch (Throwable throwable) {}
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 421 */     doConfigure(props, hierarchy);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void configure(String configFilename) {
/* 427 */     (new PropertyConfigurator()).doConfigure(configFilename, LogManager.getLoggerRepository());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void configure(URL configURL) {
/* 436 */     (new PropertyConfigurator()).doConfigure(configURL, LogManager.getLoggerRepository());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void configure(InputStream inputStream) {
/* 445 */     (new PropertyConfigurator()).doConfigure(inputStream, LogManager.getLoggerRepository());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void configure(Properties properties) {
/* 454 */     (new PropertyConfigurator()).doConfigure(properties, LogManager.getLoggerRepository());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void configureAndWatch(String configFilename) {
/* 465 */     configureAndWatch(configFilename, 60000L);
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
/*     */ 
/*     */   
/*     */   public static void configureAndWatch(String configFilename, long delay) {
/* 479 */     PropertyWatchdog pdog = new PropertyWatchdog(configFilename);
/* 480 */     pdog.setDelay(delay);
/* 481 */     pdog.start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doConfigure(Properties properties, LoggerRepository hierarchy) {
/* 490 */     this.repository = hierarchy;
/* 491 */     String value = properties.getProperty("log4j.debug");
/* 492 */     if (value == null) {
/* 493 */       value = properties.getProperty("log4j.configDebug");
/* 494 */       if (value != null) {
/* 495 */         LogLog.warn("[log4j.configDebug] is deprecated. Use [log4j.debug] instead.");
/*     */       }
/*     */     } 
/* 498 */     if (value != null) {
/* 499 */       LogLog.setInternalDebugging(OptionConverter.toBoolean(value, true));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 505 */     String reset = properties.getProperty("log4j.reset");
/* 506 */     if (reset != null && OptionConverter.toBoolean(reset, false)) {
/* 507 */       hierarchy.resetConfiguration();
/*     */     }
/*     */     
/* 510 */     String thresholdStr = OptionConverter.findAndSubst("log4j.threshold", properties);
/* 511 */     if (thresholdStr != null) {
/* 512 */       hierarchy.setThreshold(OptionConverter.toLevel(thresholdStr, Level.ALL));
/* 513 */       LogLog.debug("Hierarchy threshold set to [" + hierarchy.getThreshold() + "].");
/*     */     } 
/*     */     
/* 516 */     configureRootCategory(properties, hierarchy);
/* 517 */     configureLoggerFactory(properties);
/* 518 */     parseCatsAndRenderers(properties, hierarchy);
/*     */     
/* 520 */     LogLog.debug("Finished configuring.");
/*     */ 
/*     */     
/* 523 */     this.registry.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doConfigure(InputStream inputStream, LoggerRepository hierarchy) {
/* 532 */     Properties props = new Properties();
/*     */     try {
/* 534 */       props.load(inputStream);
/* 535 */     } catch (IOException e) {
/* 536 */       if (e instanceof InterruptedIOException) {
/* 537 */         Thread.currentThread().interrupt();
/*     */       }
/* 539 */       LogLog.error("Could not read configuration file from InputStream [" + inputStream + "].", e);
/* 540 */       LogLog.error("Ignoring configuration InputStream [" + inputStream + "].");
/*     */       return;
/*     */     } 
/* 543 */     doConfigure(props, hierarchy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doConfigure(URL configURL, LoggerRepository hierarchy) {
/* 550 */     Properties props = new Properties();
/* 551 */     LogLog.debug("Reading configuration from URL " + configURL);
/* 552 */     InputStream istream = null;
/* 553 */     URLConnection uConn = null;
/*     */     try {
/* 555 */       uConn = configURL.openConnection();
/* 556 */       uConn.setUseCaches(false);
/* 557 */       istream = uConn.getInputStream();
/* 558 */       props.load(istream);
/* 559 */     } catch (Exception e) {
/* 560 */       if (e instanceof InterruptedIOException || e instanceof InterruptedException) {
/* 561 */         Thread.currentThread().interrupt();
/*     */       }
/* 563 */       LogLog.error("Could not read configuration file from URL [" + configURL + "].", e);
/* 564 */       LogLog.error("Ignoring configuration file [" + configURL + "].");
/*     */       return;
/*     */     } finally {
/* 567 */       if (istream != null) {
/*     */         
/* 569 */         try { istream.close(); }
/* 570 */         catch (InterruptedIOException ignore)
/* 571 */         { Thread.currentThread().interrupt(); }
/* 572 */         catch (IOException iOException) {  }
/* 573 */         catch (RuntimeException runtimeException) {}
/*     */       }
/*     */     } 
/*     */     
/* 577 */     doConfigure(props, hierarchy);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void configureLoggerFactory(Properties props) {
/* 594 */     String factoryClassName = OptionConverter.findAndSubst("log4j.loggerFactory", props);
/* 595 */     if (factoryClassName != null) {
/* 596 */       LogLog.debug("Setting category factory to [" + factoryClassName + "].");
/* 597 */       this.loggerFactory = (LoggerFactory)OptionConverter.instantiateByClassName(factoryClassName, LoggerFactory.class, this.loggerFactory);
/*     */       
/* 599 */       PropertySetter.setProperties(this.loggerFactory, props, "log4j.factory.");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void configureRootCategory(Properties props, LoggerRepository hierarchy) {
/* 617 */     String effectiveFrefix = "log4j.rootLogger";
/* 618 */     String value = OptionConverter.findAndSubst("log4j.rootLogger", props);
/*     */     
/* 620 */     if (value == null) {
/* 621 */       value = OptionConverter.findAndSubst("log4j.rootCategory", props);
/* 622 */       effectiveFrefix = "log4j.rootCategory";
/*     */     } 
/*     */     
/* 625 */     if (value == null) {
/* 626 */       LogLog.debug("Could not find root logger information. Is this OK?");
/*     */     } else {
/* 628 */       Logger root = hierarchy.getRootLogger();
/* 629 */       synchronized (root) {
/* 630 */         parseCategory(props, root, effectiveFrefix, "root", value);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void parseCatsAndRenderers(Properties props, LoggerRepository hierarchy) {
/* 639 */     Enumeration<?> enumeration = props.propertyNames();
/* 640 */     while (enumeration.hasMoreElements()) {
/* 641 */       String key = (String)enumeration.nextElement();
/* 642 */       if (key.startsWith("log4j.category.") || key.startsWith("log4j.logger.")) {
/* 643 */         String loggerName = null;
/* 644 */         if (key.startsWith("log4j.category.")) {
/* 645 */           loggerName = key.substring("log4j.category.".length());
/* 646 */         } else if (key.startsWith("log4j.logger.")) {
/* 647 */           loggerName = key.substring("log4j.logger.".length());
/*     */         } 
/* 649 */         String value = OptionConverter.findAndSubst(key, props);
/* 650 */         Logger logger = hierarchy.getLogger(loggerName, this.loggerFactory);
/* 651 */         synchronized (logger) {
/* 652 */           parseCategory(props, logger, key, loggerName, value);
/* 653 */           parseAdditivityForLogger(props, logger, loggerName);
/*     */         }  continue;
/* 655 */       }  if (key.startsWith("log4j.renderer.")) {
/* 656 */         String renderedClass = key.substring("log4j.renderer.".length());
/* 657 */         String renderingClass = OptionConverter.findAndSubst(key, props);
/* 658 */         if (hierarchy instanceof RendererSupport)
/* 659 */           RendererMap.addRenderer((RendererSupport)hierarchy, renderedClass, renderingClass);  continue;
/*     */       } 
/* 661 */       if (key.equals("log4j.throwableRenderer") && 
/* 662 */         hierarchy instanceof ThrowableRendererSupport) {
/* 663 */         ThrowableRenderer tr = (ThrowableRenderer)OptionConverter.instantiateByKey(props, "log4j.throwableRenderer", ThrowableRenderer.class, null);
/*     */         
/* 665 */         if (tr == null) {
/* 666 */           LogLog.error("Could not instantiate throwableRenderer."); continue;
/*     */         } 
/* 668 */         PropertySetter setter = new PropertySetter(tr);
/* 669 */         setter.setProperties(props, "log4j.throwableRenderer.");
/* 670 */         ((ThrowableRendererSupport)hierarchy).setThrowableRenderer(tr);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void parseAdditivityForLogger(Properties props, Logger cat, String loggerName) {
/* 682 */     String value = OptionConverter.findAndSubst("log4j.additivity." + loggerName, props);
/* 683 */     LogLog.debug("Handling log4j.additivity." + loggerName + "=[" + value + "]");
/*     */     
/* 685 */     if (value != null && !value.equals("")) {
/* 686 */       boolean additivity = OptionConverter.toBoolean(value, true);
/* 687 */       LogLog.debug("Setting additivity for \"" + loggerName + "\" to " + additivity);
/* 688 */       cat.setAdditivity(additivity);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void parseCategory(Properties props, Logger logger, String optionKey, String loggerName, String value) {
/* 697 */     LogLog.debug("Parsing for [" + loggerName + "] with value=[" + value + "].");
/*     */     
/* 699 */     StringTokenizer st = new StringTokenizer(value, ",");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 704 */     if (!value.startsWith(",") && !value.equals("")) {
/*     */ 
/*     */       
/* 707 */       if (!st.hasMoreTokens()) {
/*     */         return;
/*     */       }
/* 710 */       String levelStr = st.nextToken();
/* 711 */       LogLog.debug("Level token is [" + levelStr + "].");
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 716 */       if ("inherited".equalsIgnoreCase(levelStr) || "null".equalsIgnoreCase(levelStr)) {
/* 717 */         if (loggerName.equals("root")) {
/* 718 */           LogLog.warn("The root logger cannot be set to null.");
/*     */         } else {
/* 720 */           logger.setLevel(null);
/*     */         } 
/*     */       } else {
/* 723 */         logger.setLevel(OptionConverter.toLevel(levelStr, Level.DEBUG));
/*     */       } 
/* 725 */       LogLog.debug("Category " + loggerName + " set to " + logger.getLevel());
/*     */     } 
/*     */ 
/*     */     
/* 729 */     logger.removeAllAppenders();
/*     */ 
/*     */ 
/*     */     
/* 733 */     while (st.hasMoreTokens()) {
/* 734 */       String appenderName = st.nextToken().trim();
/* 735 */       if (appenderName == null || appenderName.equals(","))
/*     */         continue; 
/* 737 */       LogLog.debug("Parsing appender named \"" + appenderName + "\".");
/* 738 */       Appender appender = parseAppender(props, appenderName);
/* 739 */       if (appender != null) {
/* 740 */         logger.addAppender(appender);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   Appender parseAppender(Properties props, String appenderName) {
/* 746 */     Appender appender = registryGet(appenderName);
/* 747 */     if (appender != null) {
/* 748 */       LogLog.debug("Appender \"" + appenderName + "\" was already parsed.");
/* 749 */       return appender;
/*     */     } 
/*     */     
/* 752 */     String prefix = "log4j.appender." + appenderName;
/* 753 */     String layoutPrefix = prefix + ".layout";
/*     */     
/* 755 */     appender = (Appender)OptionConverter.instantiateByKey(props, prefix, Appender.class, null);
/* 756 */     if (appender == null) {
/* 757 */       LogLog.error("Could not instantiate appender named \"" + appenderName + "\".");
/* 758 */       return null;
/*     */     } 
/* 760 */     appender.setName(appenderName);
/*     */     
/* 762 */     if (appender instanceof org.apache.log4j.spi.OptionHandler) {
/* 763 */       if (appender.requiresLayout()) {
/* 764 */         Layout layout = (Layout)OptionConverter.instantiateByKey(props, layoutPrefix, Layout.class, null);
/* 765 */         if (layout != null) {
/* 766 */           appender.setLayout(layout);
/* 767 */           LogLog.debug("Parsing layout options for \"" + appenderName + "\".");
/*     */           
/* 769 */           PropertySetter.setProperties(layout, props, layoutPrefix + ".");
/* 770 */           LogLog.debug("End of parsing for \"" + appenderName + "\".");
/*     */         } 
/*     */       } 
/* 773 */       String errorHandlerPrefix = prefix + ".errorhandler";
/* 774 */       String errorHandlerClass = OptionConverter.findAndSubst(errorHandlerPrefix, props);
/* 775 */       if (errorHandlerClass != null) {
/* 776 */         ErrorHandler eh = (ErrorHandler)OptionConverter.instantiateByKey(props, errorHandlerPrefix, ErrorHandler.class, null);
/*     */         
/* 778 */         if (eh != null) {
/* 779 */           appender.setErrorHandler(eh);
/* 780 */           LogLog.debug("Parsing errorhandler options for \"" + appenderName + "\".");
/* 781 */           parseErrorHandler(eh, errorHandlerPrefix, props, this.repository);
/* 782 */           Properties edited = new Properties();
/* 783 */           String[] keys = { errorHandlerPrefix + "." + "root-ref", errorHandlerPrefix + "." + "logger-ref", errorHandlerPrefix + "." + "appender-ref" };
/*     */           
/* 785 */           for (Iterator<Map.Entry<Object, Object>> iter = props.entrySet().iterator(); iter.hasNext(); ) {
/* 786 */             Map.Entry entry = iter.next();
/* 787 */             int i = 0;
/* 788 */             for (; i < keys.length && 
/* 789 */               !keys[i].equals(entry.getKey()); i++);
/*     */ 
/*     */             
/* 792 */             if (i == keys.length) {
/* 793 */               edited.put(entry.getKey(), entry.getValue());
/*     */             }
/*     */           } 
/* 796 */           PropertySetter.setProperties(eh, edited, errorHandlerPrefix + ".");
/* 797 */           LogLog.debug("End of errorhandler parsing for \"" + appenderName + "\".");
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 802 */       PropertySetter.setProperties(appender, props, prefix + ".");
/* 803 */       LogLog.debug("Parsed \"" + appenderName + "\" options.");
/*     */     } 
/* 805 */     parseAppenderFilters(props, appenderName, appender);
/* 806 */     registryPut(appender);
/* 807 */     return appender;
/*     */   }
/*     */ 
/*     */   
/*     */   private void parseErrorHandler(ErrorHandler eh, String errorHandlerPrefix, Properties props, LoggerRepository hierarchy) {
/* 812 */     boolean rootRef = OptionConverter.toBoolean(OptionConverter.findAndSubst(errorHandlerPrefix + "root-ref", props), false);
/*     */     
/* 814 */     if (rootRef) {
/* 815 */       eh.setLogger(hierarchy.getRootLogger());
/*     */     }
/* 817 */     String loggerName = OptionConverter.findAndSubst(errorHandlerPrefix + "logger-ref", props);
/* 818 */     if (loggerName != null) {
/*     */       
/* 820 */       Logger logger = (this.loggerFactory == null) ? hierarchy.getLogger(loggerName) : hierarchy.getLogger(loggerName, this.loggerFactory);
/* 821 */       eh.setLogger(logger);
/*     */     } 
/* 823 */     String appenderName = OptionConverter.findAndSubst(errorHandlerPrefix + "appender-ref", props);
/* 824 */     if (appenderName != null) {
/* 825 */       Appender backup = parseAppender(props, appenderName);
/* 826 */       if (backup != null) {
/* 827 */         eh.setBackupAppender(backup);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void parseAppenderFilters(Properties props, String appenderName, Appender appender) {
/* 836 */     String filterPrefix = "log4j.appender." + appenderName + ".filter.";
/* 837 */     int fIdx = filterPrefix.length();
/* 838 */     Hashtable<Object, Object> filters = new Hashtable<Object, Object>();
/* 839 */     Enumeration<Object> e = props.keys();
/* 840 */     String name = "";
/* 841 */     while (e.hasMoreElements()) {
/* 842 */       String key = (String)e.nextElement();
/* 843 */       if (key.startsWith(filterPrefix)) {
/* 844 */         int dotIdx = key.indexOf('.', fIdx);
/* 845 */         String filterKey = key;
/* 846 */         if (dotIdx != -1) {
/* 847 */           filterKey = key.substring(0, dotIdx);
/* 848 */           name = key.substring(dotIdx + 1);
/*     */         } 
/* 850 */         Vector<NameValue> filterOpts = (Vector)filters.get(filterKey);
/* 851 */         if (filterOpts == null) {
/* 852 */           filterOpts = new Vector();
/* 853 */           filters.put(filterKey, filterOpts);
/*     */         } 
/* 855 */         if (dotIdx != -1) {
/* 856 */           String value = OptionConverter.findAndSubst(key, props);
/* 857 */           filterOpts.add(new NameValue(name, value));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 864 */     Enumeration<String> g = new SortedKeyEnumeration(filters);
/* 865 */     while (g.hasMoreElements()) {
/* 866 */       String key = g.nextElement();
/* 867 */       String clazz = props.getProperty(key);
/* 868 */       if (clazz != null) {
/* 869 */         LogLog.debug("Filter key: [" + key + "] class: [" + props
/* 870 */             .getProperty(key) + "] props: " + filters.get(key));
/* 871 */         Filter filter = (Filter)OptionConverter.instantiateByClassName(clazz, Filter.class, null);
/* 872 */         if (filter != null) {
/* 873 */           PropertySetter propSetter = new PropertySetter(filter);
/* 874 */           Vector v = (Vector)filters.get(key);
/* 875 */           Enumeration<NameValue> filterProps = v.elements();
/* 876 */           while (filterProps.hasMoreElements()) {
/* 877 */             NameValue kv = filterProps.nextElement();
/* 878 */             propSetter.setProperty(kv.key, kv.value);
/*     */           } 
/* 880 */           propSetter.activate();
/* 881 */           LogLog.debug("Adding filter of type [" + filter.getClass() + "] to appender named [" + appender
/* 882 */               .getName() + "].");
/* 883 */           appender.addFilter(filter);
/*     */         }  continue;
/*     */       } 
/* 886 */       LogLog.warn("Missing class definition for filter: [" + key + "]");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void registryPut(Appender appender) {
/* 892 */     this.registry.put(appender.getName(), appender);
/*     */   }
/*     */   
/*     */   Appender registryGet(String name) {
/* 896 */     return (Appender)this.registry.get(name);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/PropertyConfigurator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */