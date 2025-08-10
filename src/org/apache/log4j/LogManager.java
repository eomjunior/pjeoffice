/*     */ package org.apache.log4j;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.Enumeration;
/*     */ import org.apache.log4j.helpers.Loader;
/*     */ import org.apache.log4j.helpers.LogLog;
/*     */ import org.apache.log4j.helpers.OptionConverter;
/*     */ import org.apache.log4j.spi.DefaultRepositorySelector;
/*     */ import org.apache.log4j.spi.LoggerFactory;
/*     */ import org.apache.log4j.spi.LoggerRepository;
/*     */ import org.apache.log4j.spi.NOPLoggerRepository;
/*     */ import org.apache.log4j.spi.RepositorySelector;
/*     */ import org.apache.log4j.spi.RootLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LogManager
/*     */ {
/*     */   public static final String DEFAULT_CONFIGURATION_FILE = "log4j.properties";
/*     */   static final String DEFAULT_XML_CONFIGURATION_FILE = "log4j.xml";
/*     */   public static final String DEFAULT_CONFIGURATION_KEY = "log4j.configuration";
/*     */   public static final String CONFIGURATOR_CLASS_KEY = "log4j.configuratorClass";
/*     */   public static final String DEFAULT_INIT_OVERRIDE_KEY = "log4j.defaultInitOverride";
/*  74 */   private static Object guard = null;
/*     */   
/*     */   private static RepositorySelector repositorySelector;
/*     */   
/*     */   static {
/*  79 */     Hierarchy h = new Hierarchy((Logger)new RootLogger(Level.DEBUG));
/*  80 */     repositorySelector = (RepositorySelector)new DefaultRepositorySelector(h);
/*     */ 
/*     */     
/*  83 */     String override = OptionConverter.getSystemProperty("log4j.defaultInitOverride", null);
/*     */ 
/*     */ 
/*     */     
/*  87 */     if (override == null || "false".equalsIgnoreCase(override)) {
/*     */       
/*  89 */       String configurationOptionStr = OptionConverter.getSystemProperty("log4j.configuration", null);
/*     */       
/*  91 */       String configuratorClassName = OptionConverter.getSystemProperty("log4j.configuratorClass", null);
/*     */       
/*  93 */       URL url = null;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  98 */       if (configurationOptionStr == null) {
/*  99 */         url = Loader.getResource("log4j.xml");
/* 100 */         if (url == null) {
/* 101 */           url = Loader.getResource("log4j.properties");
/*     */         }
/*     */       } else {
/*     */         try {
/* 105 */           url = new URL(configurationOptionStr);
/* 106 */         } catch (MalformedURLException ex) {
/*     */ 
/*     */           
/* 109 */           url = Loader.getResource(configurationOptionStr);
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 116 */       if (url != null) {
/* 117 */         LogLog.debug("Using URL [" + url + "] for automatic log4j configuration.");
/*     */         try {
/* 119 */           OptionConverter.selectAndConfigure(url, configuratorClassName, getLoggerRepository());
/* 120 */         } catch (NoClassDefFoundError e) {
/* 121 */           LogLog.warn("Error during default initialization", e);
/*     */         } 
/*     */       } else {
/* 124 */         LogLog.debug("Could not find resource: [" + configurationOptionStr + "].");
/*     */       } 
/*     */     } else {
/* 127 */       LogLog.debug("Default initialization of overridden by log4j.defaultInitOverrideproperty.");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setRepositorySelector(RepositorySelector selector, Object guard) throws IllegalArgumentException {
/* 153 */     if (LogManager.guard != null && LogManager.guard != guard) {
/* 154 */       throw new IllegalArgumentException("Attempted to reset the LoggerFactory without possessing the guard.");
/*     */     }
/*     */     
/* 157 */     if (selector == null) {
/* 158 */       throw new IllegalArgumentException("RepositorySelector must be non-null.");
/*     */     }
/*     */     
/* 161 */     LogManager.guard = guard;
/* 162 */     repositorySelector = selector;
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
/*     */   private static boolean isLikelySafeScenario(Exception ex) {
/* 174 */     StringWriter stringWriter = new StringWriter();
/* 175 */     ex.printStackTrace(new PrintWriter(stringWriter));
/* 176 */     String msg = stringWriter.toString();
/* 177 */     return (msg.indexOf("org.apache.catalina.loader.WebappClassLoader.stop") != -1);
/*     */   }
/*     */   
/*     */   public static LoggerRepository getLoggerRepository() {
/* 181 */     if (repositorySelector == null) {
/* 182 */       repositorySelector = (RepositorySelector)new DefaultRepositorySelector((LoggerRepository)new NOPLoggerRepository());
/* 183 */       guard = null;
/* 184 */       Exception ex = new IllegalStateException("Class invariant violation");
/* 185 */       String msg = "log4j called after unloading, see http://logging.apache.org/log4j/1.2/faq.html#unload.";
/* 186 */       if (isLikelySafeScenario(ex)) {
/* 187 */         LogLog.debug(msg, ex);
/*     */       } else {
/* 189 */         LogLog.error(msg, ex);
/*     */       } 
/*     */     } 
/* 192 */     return repositorySelector.getLoggerRepository();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Logger getRootLogger() {
/* 200 */     return getLoggerRepository().getRootLogger();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Logger getLogger(String name) {
/* 208 */     return getLoggerRepository().getLogger(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Logger getLogger(Class clazz) {
/* 216 */     return getLoggerRepository().getLogger(clazz.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Logger getLogger(String name, LoggerFactory factory) {
/* 224 */     return getLoggerRepository().getLogger(name, factory);
/*     */   }
/*     */   
/*     */   public static Logger exists(String name) {
/* 228 */     return getLoggerRepository().exists(name);
/*     */   }
/*     */   
/*     */   public static Enumeration getCurrentLoggers() {
/* 232 */     return getLoggerRepository().getCurrentLoggers();
/*     */   }
/*     */   
/*     */   public static void shutdown() {
/* 236 */     getLoggerRepository().shutdown();
/*     */   }
/*     */   
/*     */   public static void resetConfiguration() {
/* 240 */     getLoggerRepository().resetConfiguration();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/LogManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */