/*     */ package org.slf4j;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.ServiceConfigurationError;
/*     */ import java.util.ServiceLoader;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import org.slf4j.event.LoggingEvent;
/*     */ import org.slf4j.event.SubstituteLoggingEvent;
/*     */ import org.slf4j.helpers.NOP_FallbackServiceProvider;
/*     */ import org.slf4j.helpers.Reporter;
/*     */ import org.slf4j.helpers.SubstituteLogger;
/*     */ import org.slf4j.helpers.SubstituteServiceProvider;
/*     */ import org.slf4j.helpers.Util;
/*     */ import org.slf4j.spi.SLF4JServiceProvider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class LoggerFactory
/*     */ {
/*     */   static final String CODES_PREFIX = "https://www.slf4j.org/codes.html";
/*     */   static final String NO_PROVIDERS_URL = "https://www.slf4j.org/codes.html#noProviders";
/*     */   static final String IGNORED_BINDINGS_URL = "https://www.slf4j.org/codes.html#ignoredBindings";
/*     */   static final String MULTIPLE_BINDINGS_URL = "https://www.slf4j.org/codes.html#multiple_bindings";
/*     */   static final String VERSION_MISMATCH = "https://www.slf4j.org/codes.html#version_mismatch";
/*     */   static final String SUBSTITUTE_LOGGER_URL = "https://www.slf4j.org/codes.html#substituteLogger";
/*     */   static final String LOGGER_NAME_MISMATCH_URL = "https://www.slf4j.org/codes.html#loggerNameMismatch";
/*     */   static final String REPLAY_URL = "https://www.slf4j.org/codes.html#replay";
/*     */   static final String UNSUCCESSFUL_INIT_URL = "https://www.slf4j.org/codes.html#unsuccessfulInit";
/*     */   static final String UNSUCCESSFUL_INIT_MSG = "org.slf4j.LoggerFactory in failed state. Original exception was thrown EARLIER. See also https://www.slf4j.org/codes.html#unsuccessfulInit";
/*     */   public static final String PROVIDER_PROPERTY_KEY = "slf4j.provider";
/*     */   static final int UNINITIALIZED = 0;
/*     */   static final int ONGOING_INITIALIZATION = 1;
/*     */   static final int FAILED_INITIALIZATION = 2;
/*     */   static final int SUCCESSFUL_INITIALIZATION = 3;
/*     */   static final int NOP_FALLBACK_INITIALIZATION = 4;
/*  99 */   static volatile int INITIALIZATION_STATE = 0;
/* 100 */   static final SubstituteServiceProvider SUBST_PROVIDER = new SubstituteServiceProvider();
/* 101 */   static final NOP_FallbackServiceProvider NOP_FALLBACK_SERVICE_PROVIDER = new NOP_FallbackServiceProvider();
/*     */   
/*     */   static final String DETECT_LOGGER_NAME_MISMATCH_PROPERTY = "slf4j.detectLoggerNameMismatch";
/*     */   
/*     */   static final String JAVA_VENDOR_PROPERTY = "java.vendor.url";
/*     */   
/* 107 */   static boolean DETECT_LOGGER_NAME_MISMATCH = Util.safeGetBooleanSystemProperty("slf4j.detectLoggerNameMismatch");
/*     */   
/*     */   static volatile SLF4JServiceProvider PROVIDER;
/*     */ 
/*     */   
/*     */   static List<SLF4JServiceProvider> findServiceProviders() {
/* 113 */     List<SLF4JServiceProvider> providerList = new ArrayList<>();
/*     */ 
/*     */ 
/*     */     
/* 117 */     ClassLoader classLoaderOfLoggerFactory = LoggerFactory.class.getClassLoader();
/*     */     
/* 119 */     SLF4JServiceProvider explicitProvider = loadExplicitlySpecified(classLoaderOfLoggerFactory);
/* 120 */     if (explicitProvider != null) {
/* 121 */       providerList.add(explicitProvider);
/* 122 */       return providerList;
/*     */     } 
/*     */ 
/*     */     
/* 126 */     ServiceLoader<SLF4JServiceProvider> serviceLoader = getServiceLoader(classLoaderOfLoggerFactory);
/*     */     
/* 128 */     Iterator<SLF4JServiceProvider> iterator = serviceLoader.iterator();
/* 129 */     while (iterator.hasNext()) {
/* 130 */       safelyInstantiate(providerList, iterator);
/*     */     }
/* 132 */     return providerList;
/*     */   }
/*     */   
/*     */   private static ServiceLoader<SLF4JServiceProvider> getServiceLoader(ClassLoader classLoaderOfLoggerFactory) {
/*     */     ServiceLoader<SLF4JServiceProvider> serviceLoader;
/* 137 */     SecurityManager securityManager = System.getSecurityManager();
/* 138 */     if (securityManager == null) {
/* 139 */       serviceLoader = ServiceLoader.load(SLF4JServiceProvider.class, classLoaderOfLoggerFactory);
/*     */     } else {
/* 141 */       PrivilegedAction<ServiceLoader<SLF4JServiceProvider>> action = () -> ServiceLoader.load(SLF4JServiceProvider.class, classLoaderOfLoggerFactory);
/* 142 */       serviceLoader = AccessController.<ServiceLoader<SLF4JServiceProvider>>doPrivileged(action);
/*     */     } 
/* 144 */     return serviceLoader;
/*     */   }
/*     */   
/*     */   private static void safelyInstantiate(List<SLF4JServiceProvider> providerList, Iterator<SLF4JServiceProvider> iterator) {
/*     */     try {
/* 149 */       SLF4JServiceProvider provider = iterator.next();
/* 150 */       providerList.add(provider);
/* 151 */     } catch (ServiceConfigurationError e) {
/* 152 */       Reporter.error("A service provider failed to instantiate:\n" + e.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 161 */   private static final String[] API_COMPATIBILITY_LIST = new String[] { "2.0" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String STATIC_LOGGER_BINDER_PATH = "org/slf4j/impl/StaticLoggerBinder.class";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void reset() {
/* 179 */     INITIALIZATION_STATE = 0;
/*     */   }
/*     */   
/*     */   private static final void performInitialization() {
/* 183 */     bind();
/* 184 */     if (INITIALIZATION_STATE == 3) {
/* 185 */       versionSanityCheck();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final void bind() {
/*     */     try {
/* 191 */       List<SLF4JServiceProvider> providersList = findServiceProviders();
/* 192 */       reportMultipleBindingAmbiguity(providersList);
/* 193 */       if (providersList != null && !providersList.isEmpty()) {
/* 194 */         PROVIDER = providersList.get(0);
/*     */         
/* 196 */         PROVIDER.initialize();
/* 197 */         INITIALIZATION_STATE = 3;
/* 198 */         reportActualBinding(providersList);
/*     */       } else {
/* 200 */         INITIALIZATION_STATE = 4;
/* 201 */         Reporter.warn("No SLF4J providers were found.");
/* 202 */         Reporter.warn("Defaulting to no-operation (NOP) logger implementation");
/* 203 */         Reporter.warn("See https://www.slf4j.org/codes.html#noProviders for further details.");
/*     */         
/* 205 */         Set<URL> staticLoggerBinderPathSet = findPossibleStaticLoggerBinderPathSet();
/* 206 */         reportIgnoredStaticLoggerBinders(staticLoggerBinderPathSet);
/*     */       } 
/* 208 */       postBindCleanUp();
/* 209 */     } catch (Exception e) {
/* 210 */       failedBinding(e);
/* 211 */       throw new IllegalStateException("Unexpected initialization failure", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   static SLF4JServiceProvider loadExplicitlySpecified(ClassLoader classLoader) {
/* 216 */     String explicitlySpecified = System.getProperty("slf4j.provider");
/* 217 */     if (null == explicitlySpecified || explicitlySpecified.isEmpty()) {
/* 218 */       return null;
/*     */     }
/*     */     try {
/* 221 */       String message = String.format("Attempting to load provider \"%s\" specified via \"%s\" system property", new Object[] { explicitlySpecified, "slf4j.provider" });
/* 222 */       Reporter.info(message);
/* 223 */       Class<?> clazz = classLoader.loadClass(explicitlySpecified);
/* 224 */       Constructor<?> constructor = clazz.getConstructor(new Class[0]);
/* 225 */       Object provider = constructor.newInstance(new Object[0]);
/* 226 */       return (SLF4JServiceProvider)provider;
/* 227 */     } catch (ClassNotFoundException|NoSuchMethodException|InstantiationException|IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
/* 228 */       String message = String.format("Failed to instantiate the specified SLF4JServiceProvider (%s)", new Object[] { explicitlySpecified });
/* 229 */       Reporter.error(message, e);
/* 230 */       return null;
/* 231 */     } catch (ClassCastException e) {
/* 232 */       String message = String.format("Specified SLF4JServiceProvider (%s) does not implement SLF4JServiceProvider interface", new Object[] { explicitlySpecified });
/* 233 */       Reporter.error(message, e);
/* 234 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void reportIgnoredStaticLoggerBinders(Set<URL> staticLoggerBinderPathSet) {
/* 239 */     if (staticLoggerBinderPathSet.isEmpty()) {
/*     */       return;
/*     */     }
/* 242 */     Reporter.warn("Class path contains SLF4J bindings targeting slf4j-api versions 1.7.x or earlier.");
/*     */     
/* 244 */     for (URL path : staticLoggerBinderPathSet) {
/* 245 */       Reporter.warn("Ignoring binding found at [" + path + "]");
/*     */     }
/* 247 */     Reporter.warn("See https://www.slf4j.org/codes.html#ignoredBindings for an explanation.");
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
/*     */   static Set<URL> findPossibleStaticLoggerBinderPathSet() {
/* 259 */     Set<URL> staticLoggerBinderPathSet = new LinkedHashSet<>(); try {
/*     */       Enumeration<URL> paths;
/* 261 */       ClassLoader loggerFactoryClassLoader = LoggerFactory.class.getClassLoader();
/*     */       
/* 263 */       if (loggerFactoryClassLoader == null) {
/* 264 */         paths = ClassLoader.getSystemResources("org/slf4j/impl/StaticLoggerBinder.class");
/*     */       } else {
/* 266 */         paths = loggerFactoryClassLoader.getResources("org/slf4j/impl/StaticLoggerBinder.class");
/*     */       } 
/* 268 */       while (paths.hasMoreElements()) {
/* 269 */         URL path = paths.nextElement();
/* 270 */         staticLoggerBinderPathSet.add(path);
/*     */       } 
/* 272 */     } catch (IOException ioe) {
/* 273 */       Reporter.error("Error getting resources from path", ioe);
/*     */     } 
/* 275 */     return staticLoggerBinderPathSet;
/*     */   }
/*     */   
/*     */   private static void postBindCleanUp() {
/* 279 */     fixSubstituteLoggers();
/* 280 */     replayEvents();
/*     */     
/* 282 */     SUBST_PROVIDER.getSubstituteLoggerFactory().clear();
/*     */   }
/*     */   
/*     */   private static void fixSubstituteLoggers() {
/* 286 */     synchronized (SUBST_PROVIDER) {
/* 287 */       SUBST_PROVIDER.getSubstituteLoggerFactory().postInitialization();
/* 288 */       for (SubstituteLogger substLogger : SUBST_PROVIDER.getSubstituteLoggerFactory().getLoggers()) {
/* 289 */         Logger logger = getLogger(substLogger.getName());
/* 290 */         substLogger.setDelegate(logger);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static void failedBinding(Throwable t) {
/* 297 */     INITIALIZATION_STATE = 2;
/* 298 */     Reporter.error("Failed to instantiate SLF4J LoggerFactory", t);
/*     */   }
/*     */   
/*     */   private static void replayEvents() {
/* 302 */     LinkedBlockingQueue<SubstituteLoggingEvent> queue = SUBST_PROVIDER.getSubstituteLoggerFactory().getEventQueue();
/* 303 */     int queueSize = queue.size();
/* 304 */     int count = 0;
/* 305 */     int maxDrain = 128;
/* 306 */     List<SubstituteLoggingEvent> eventList = new ArrayList<>(128);
/*     */     while (true) {
/* 308 */       int numDrained = queue.drainTo(eventList, 128);
/* 309 */       if (numDrained == 0)
/*     */         break; 
/* 311 */       for (SubstituteLoggingEvent event : eventList) {
/* 312 */         replaySingleEvent(event);
/* 313 */         if (count++ == 0)
/* 314 */           emitReplayOrSubstituionWarning(event, queueSize); 
/*     */       } 
/* 316 */       eventList.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void emitReplayOrSubstituionWarning(SubstituteLoggingEvent event, int queueSize) {
/* 321 */     if (event.getLogger().isDelegateEventAware()) {
/* 322 */       emitReplayWarning(queueSize);
/* 323 */     } else if (!event.getLogger().isDelegateNOP()) {
/*     */ 
/*     */       
/* 326 */       emitSubstitutionWarning();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void replaySingleEvent(SubstituteLoggingEvent event) {
/* 331 */     if (event == null) {
/*     */       return;
/*     */     }
/* 334 */     SubstituteLogger substLogger = event.getLogger();
/* 335 */     String loggerName = substLogger.getName();
/* 336 */     if (substLogger.isDelegateNull()) {
/* 337 */       throw new IllegalStateException("Delegate logger cannot be null at this state.");
/*     */     }
/*     */     
/* 340 */     if (!substLogger.isDelegateNOP())
/*     */     {
/* 342 */       if (substLogger.isDelegateEventAware()) {
/* 343 */         if (substLogger.isEnabledForLevel(event.getLevel())) {
/* 344 */           substLogger.log((LoggingEvent)event);
/*     */         }
/*     */       } else {
/* 347 */         Reporter.warn(loggerName);
/*     */       }  } 
/*     */   }
/*     */   
/*     */   private static void emitSubstitutionWarning() {
/* 352 */     Reporter.warn("The following set of substitute loggers may have been accessed");
/* 353 */     Reporter.warn("during the initialization phase. Logging calls during this");
/* 354 */     Reporter.warn("phase were not honored. However, subsequent logging calls to these");
/* 355 */     Reporter.warn("loggers will work as normally expected.");
/* 356 */     Reporter.warn("See also https://www.slf4j.org/codes.html#substituteLogger");
/*     */   }
/*     */   
/*     */   private static void emitReplayWarning(int eventCount) {
/* 360 */     Reporter.warn("A number (" + eventCount + ") of logging calls during the initialization phase have been intercepted and are");
/* 361 */     Reporter.warn("now being replayed. These are subject to the filtering rules of the underlying logging system.");
/* 362 */     Reporter.warn("See also https://www.slf4j.org/codes.html#replay");
/*     */   }
/*     */   
/*     */   private static final void versionSanityCheck() {
/*     */     try {
/* 367 */       String requested = PROVIDER.getRequestedApiVersion();
/*     */       
/* 369 */       boolean match = false;
/* 370 */       for (String aAPI_COMPATIBILITY_LIST : API_COMPATIBILITY_LIST) {
/* 371 */         if (requested.startsWith(aAPI_COMPATIBILITY_LIST)) {
/* 372 */           match = true;
/*     */         }
/*     */       } 
/* 375 */       if (!match) {
/* 376 */         Reporter.warn("The requested version " + requested + " by your slf4j provider is not compatible with " + 
/* 377 */             Arrays.<String>asList(API_COMPATIBILITY_LIST).toString());
/* 378 */         Reporter.warn("See https://www.slf4j.org/codes.html#version_mismatch for further details.");
/*     */       } 
/* 380 */     } catch (Throwable e) {
/*     */       
/* 382 */       Reporter.error("Unexpected problem occurred during version sanity check", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isAmbiguousProviderList(List<SLF4JServiceProvider> providerList) {
/* 387 */     return (providerList.size() > 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void reportMultipleBindingAmbiguity(List<SLF4JServiceProvider> providerList) {
/* 396 */     if (isAmbiguousProviderList(providerList)) {
/* 397 */       Reporter.warn("Class path contains multiple SLF4J providers.");
/* 398 */       for (SLF4JServiceProvider provider : providerList) {
/* 399 */         Reporter.warn("Found provider [" + provider + "]");
/*     */       }
/* 401 */       Reporter.warn("See https://www.slf4j.org/codes.html#multiple_bindings for an explanation.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void reportActualBinding(List<SLF4JServiceProvider> providerList) {
/* 407 */     if (!providerList.isEmpty() && isAmbiguousProviderList(providerList)) {
/* 408 */       Reporter.info("Actual provider is of type [" + providerList.get(0) + "]");
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
/*     */   public static Logger getLogger(String name) {
/* 421 */     ILoggerFactory iLoggerFactory = getILoggerFactory();
/* 422 */     return iLoggerFactory.getLogger(name);
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
/*     */   public static Logger getLogger(Class<?> clazz) {
/* 447 */     Logger logger = getLogger(clazz.getName());
/* 448 */     if (DETECT_LOGGER_NAME_MISMATCH) {
/* 449 */       Class<?> autoComputedCallingClass = Util.getCallingClass();
/* 450 */       if (autoComputedCallingClass != null && nonMatchingClasses(clazz, autoComputedCallingClass)) {
/* 451 */         Reporter.warn(String.format("Detected logger name mismatch. Given name: \"%s\"; computed name: \"%s\".", new Object[] { logger.getName(), autoComputedCallingClass
/* 452 */                 .getName() }));
/* 453 */         Reporter.warn("See https://www.slf4j.org/codes.html#loggerNameMismatch for an explanation");
/*     */       } 
/*     */     } 
/* 456 */     return logger;
/*     */   }
/*     */   
/*     */   private static boolean nonMatchingClasses(Class<?> clazz, Class<?> autoComputedCallingClass) {
/* 460 */     return !autoComputedCallingClass.isAssignableFrom(clazz);
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
/*     */   public static ILoggerFactory getILoggerFactory() {
/* 472 */     return getProvider().getLoggerFactory();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static SLF4JServiceProvider getProvider() {
/* 482 */     if (INITIALIZATION_STATE == 0) {
/* 483 */       synchronized (LoggerFactory.class) {
/* 484 */         if (INITIALIZATION_STATE == 0) {
/* 485 */           INITIALIZATION_STATE = 1;
/* 486 */           performInitialization();
/*     */         } 
/*     */       } 
/*     */     }
/* 490 */     switch (INITIALIZATION_STATE) {
/*     */       case 3:
/* 492 */         return PROVIDER;
/*     */       case 4:
/* 494 */         return (SLF4JServiceProvider)NOP_FALLBACK_SERVICE_PROVIDER;
/*     */       case 2:
/* 496 */         throw new IllegalStateException("org.slf4j.LoggerFactory in failed state. Original exception was thrown EARLIER. See also https://www.slf4j.org/codes.html#unsuccessfulInit");
/*     */ 
/*     */       
/*     */       case 1:
/* 500 */         return (SLF4JServiceProvider)SUBST_PROVIDER;
/*     */     } 
/* 502 */     throw new IllegalStateException("Unreachable code");
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/slf4j/LoggerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */