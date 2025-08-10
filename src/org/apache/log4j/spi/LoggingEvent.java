/*     */ package org.apache.log4j.spi;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.log4j.Category;
/*     */ import org.apache.log4j.Level;
/*     */ import org.apache.log4j.MDC;
/*     */ import org.apache.log4j.NDC;
/*     */ import org.apache.log4j.Priority;
/*     */ import org.apache.log4j.helpers.Loader;
/*     */ import org.apache.log4j.helpers.LogLog;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LoggingEvent
/*     */   implements Serializable
/*     */ {
/*  58 */   private static long startTime = System.currentTimeMillis();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final transient String fqnOfCategoryClass;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private transient Category logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String categoryName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public transient Priority level;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String ndc;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Hashtable mdcCopy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean ndcLookupRequired = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean mdcCopyLookupRequired = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private transient Object message;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String renderedMessage;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String threadName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ThrowableInformation throwableInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final long timeStamp;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private LocationInfo locationInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final long serialVersionUID = -868428216207166145L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 151 */   static final Integer[] PARAM_ARRAY = new Integer[1];
/*     */   static final String TO_LEVEL = "toLevel";
/* 153 */   static final Class[] TO_LEVEL_PARAMS = new Class[] { int.class };
/* 154 */   static final Hashtable methodCache = new Hashtable<Object, Object>(3);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggingEvent(String fqnOfCategoryClass, Category logger, Priority level, Object message, Throwable throwable) {
/* 171 */     this.fqnOfCategoryClass = fqnOfCategoryClass;
/* 172 */     this.logger = logger;
/* 173 */     this.categoryName = logger.getName();
/* 174 */     this.level = level;
/* 175 */     this.message = message;
/* 176 */     if (throwable != null) {
/* 177 */       this.throwableInfo = new ThrowableInformation(throwable, logger);
/*     */     }
/* 179 */     this.timeStamp = System.currentTimeMillis();
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
/*     */   public LoggingEvent(String fqnOfCategoryClass, Category logger, long timeStamp, Priority level, Object message, Throwable throwable) {
/* 198 */     this.fqnOfCategoryClass = fqnOfCategoryClass;
/* 199 */     this.logger = logger;
/* 200 */     this.categoryName = logger.getName();
/* 201 */     this.level = level;
/* 202 */     this.message = message;
/* 203 */     if (throwable != null) {
/* 204 */       this.throwableInfo = new ThrowableInformation(throwable, logger);
/*     */     }
/*     */     
/* 207 */     this.timeStamp = timeStamp;
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
/*     */   public LoggingEvent(String fqnOfCategoryClass, Category logger, long timeStamp, Level level, Object message, String threadName, ThrowableInformation throwable, String ndc, LocationInfo info, Map<?, ?> properties) {
/* 230 */     this.fqnOfCategoryClass = fqnOfCategoryClass;
/* 231 */     this.logger = logger;
/* 232 */     if (logger != null) {
/* 233 */       this.categoryName = logger.getName();
/*     */     } else {
/* 235 */       this.categoryName = null;
/*     */     } 
/* 237 */     this.level = (Priority)level;
/* 238 */     this.message = message;
/* 239 */     if (throwable != null) {
/* 240 */       this.throwableInfo = throwable;
/*     */     }
/*     */     
/* 243 */     this.timeStamp = timeStamp;
/* 244 */     this.threadName = threadName;
/* 245 */     this.ndcLookupRequired = false;
/* 246 */     this.ndc = ndc;
/* 247 */     this.locationInfo = info;
/* 248 */     this.mdcCopyLookupRequired = false;
/* 249 */     if (properties != null) {
/* 250 */       this.mdcCopy = new Hashtable<Object, Object>(properties);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LocationInfo getLocationInformation() {
/* 259 */     if (this.locationInfo == null) {
/* 260 */       this.locationInfo = new LocationInfo(new Throwable(), this.fqnOfCategoryClass);
/*     */     }
/* 262 */     return this.locationInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Level getLevel() {
/* 270 */     return (Level)this.level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLoggerName() {
/* 278 */     return this.categoryName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Category getLogger() {
/* 287 */     return this.logger;
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
/*     */   public Object getMessage() {
/* 301 */     if (this.message != null) {
/* 302 */       return this.message;
/*     */     }
/* 304 */     return getRenderedMessage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNDC() {
/* 315 */     if (this.ndcLookupRequired) {
/* 316 */       this.ndcLookupRequired = false;
/* 317 */       this.ndc = NDC.get();
/*     */     } 
/* 319 */     return this.ndc;
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
/*     */   public Object getMDC(String key) {
/* 338 */     if (this.mdcCopy != null) {
/* 339 */       Object r = this.mdcCopy.get(key);
/* 340 */       if (r != null) {
/* 341 */         return r;
/*     */       }
/*     */     } 
/* 344 */     return MDC.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void getMDCCopy() {
/* 352 */     if (this.mdcCopyLookupRequired) {
/* 353 */       this.mdcCopyLookupRequired = false;
/*     */ 
/*     */       
/* 356 */       Hashtable t = MDC.getContext();
/* 357 */       if (t != null) {
/* 358 */         this.mdcCopy = (Hashtable)t.clone();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getRenderedMessage() {
/* 364 */     if (this.renderedMessage == null && this.message != null) {
/* 365 */       if (this.message instanceof String) {
/* 366 */         this.renderedMessage = (String)this.message;
/*     */       } else {
/* 368 */         LoggerRepository repository = this.logger.getLoggerRepository();
/*     */         
/* 370 */         if (repository instanceof RendererSupport) {
/* 371 */           RendererSupport rs = (RendererSupport)repository;
/* 372 */           this.renderedMessage = rs.getRendererMap().findAndRender(this.message);
/*     */         } else {
/* 374 */           this.renderedMessage = this.message.toString();
/*     */         } 
/*     */       } 
/*     */     }
/* 378 */     return this.renderedMessage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long getStartTime() {
/* 386 */     return startTime;
/*     */   }
/*     */   
/*     */   public String getThreadName() {
/* 390 */     if (this.threadName == null)
/* 391 */       this.threadName = Thread.currentThread().getName(); 
/* 392 */     return this.threadName;
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
/*     */   public ThrowableInformation getThrowableInformation() {
/* 406 */     return this.throwableInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getThrowableStrRep() {
/* 414 */     if (this.throwableInfo == null) {
/* 415 */       return null;
/*     */     }
/* 417 */     return this.throwableInfo.getThrowableStrRep();
/*     */   }
/*     */ 
/*     */   
/*     */   private void readLevel(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/* 422 */     int p = ois.readInt();
/*     */     try {
/* 424 */       String className = (String)ois.readObject();
/* 425 */       if (className == null) {
/* 426 */         this.level = (Priority)Level.toLevel(p);
/*     */       } else {
/* 428 */         Method m = (Method)methodCache.get(className);
/* 429 */         if (m == null) {
/* 430 */           Class clazz = Loader.loadClass(className);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 437 */           m = clazz.getDeclaredMethod("toLevel", TO_LEVEL_PARAMS);
/* 438 */           methodCache.put(className, m);
/*     */         } 
/* 440 */         this.level = (Priority)m.invoke(null, (Object[])new Integer[] { new Integer(p) });
/*     */       } 
/* 442 */     } catch (InvocationTargetException e) {
/* 443 */       if (e.getTargetException() instanceof InterruptedException || e
/* 444 */         .getTargetException() instanceof java.io.InterruptedIOException) {
/* 445 */         Thread.currentThread().interrupt();
/*     */       }
/* 447 */       LogLog.warn("Level deserialization failed, reverting to default.", e);
/* 448 */       this.level = (Priority)Level.toLevel(p);
/* 449 */     } catch (NoSuchMethodException e) {
/* 450 */       LogLog.warn("Level deserialization failed, reverting to default.", e);
/* 451 */       this.level = (Priority)Level.toLevel(p);
/* 452 */     } catch (IllegalAccessException e) {
/* 453 */       LogLog.warn("Level deserialization failed, reverting to default.", e);
/* 454 */       this.level = (Priority)Level.toLevel(p);
/* 455 */     } catch (RuntimeException e) {
/* 456 */       LogLog.warn("Level deserialization failed, reverting to default.", e);
/* 457 */       this.level = (Priority)Level.toLevel(p);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/* 462 */     ois.defaultReadObject();
/* 463 */     readLevel(ois);
/*     */ 
/*     */     
/* 466 */     if (this.locationInfo == null) {
/* 467 */       this.locationInfo = new LocationInfo(null, null);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream oos) throws IOException {
/* 473 */     getThreadName();
/*     */ 
/*     */     
/* 476 */     getRenderedMessage();
/*     */ 
/*     */ 
/*     */     
/* 480 */     getNDC();
/*     */ 
/*     */ 
/*     */     
/* 484 */     getMDCCopy();
/*     */ 
/*     */     
/* 487 */     getThrowableStrRep();
/*     */     
/* 489 */     oos.defaultWriteObject();
/*     */ 
/*     */     
/* 492 */     writeLevel(oos);
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeLevel(ObjectOutputStream oos) throws IOException {
/* 497 */     oos.writeInt(this.level.toInt());
/*     */     
/* 499 */     Class<?> clazz = this.level.getClass();
/* 500 */     if (clazz == Level.class) {
/* 501 */       oos.writeObject(null);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 506 */       oos.writeObject(clazz.getName());
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
/*     */   public final void setProperty(String propName, String propValue) {
/* 520 */     if (this.mdcCopy == null) {
/* 521 */       getMDCCopy();
/*     */     }
/* 523 */     if (this.mdcCopy == null) {
/* 524 */       this.mdcCopy = new Hashtable<Object, Object>();
/*     */     }
/* 526 */     this.mdcCopy.put(propName, propValue);
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
/*     */   public final String getProperty(String key) {
/* 540 */     Object value = getMDC(key);
/* 541 */     String retval = null;
/* 542 */     if (value != null) {
/* 543 */       retval = value.toString();
/*     */     }
/* 545 */     return retval;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean locationInformationExists() {
/* 556 */     return (this.locationInfo != null);
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
/*     */   public final long getTimeStamp() {
/* 568 */     return this.timeStamp;
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
/*     */   public Set getPropertyKeySet() {
/* 582 */     return getProperties().keySet();
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
/*     */   public Map getProperties() {
/*     */     Map<?, ?> properties;
/* 596 */     getMDCCopy();
/*     */     
/* 598 */     if (this.mdcCopy == null) {
/* 599 */       properties = new HashMap<Object, Object>();
/*     */     } else {
/* 601 */       properties = this.mdcCopy;
/*     */     } 
/* 603 */     return Collections.unmodifiableMap(properties);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFQNOfLoggerClass() {
/* 614 */     return this.fqnOfCategoryClass;
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
/*     */   public Object removeProperty(String propName) {
/* 626 */     if (this.mdcCopy == null) {
/* 627 */       getMDCCopy();
/*     */     }
/* 629 */     if (this.mdcCopy == null) {
/* 630 */       this.mdcCopy = new Hashtable<Object, Object>();
/*     */     }
/* 632 */     return this.mdcCopy.remove(propName);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/spi/LoggingEvent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */