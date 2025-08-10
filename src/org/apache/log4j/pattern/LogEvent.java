/*     */ package org.apache.log4j.pattern;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.log4j.Category;
/*     */ import org.apache.log4j.Level;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.apache.log4j.MDC;
/*     */ import org.apache.log4j.NDC;
/*     */ import org.apache.log4j.Priority;
/*     */ import org.apache.log4j.helpers.Loader;
/*     */ import org.apache.log4j.helpers.LogLog;
/*     */ import org.apache.log4j.spi.LocationInfo;
/*     */ import org.apache.log4j.spi.LoggerRepository;
/*     */ import org.apache.log4j.spi.RendererSupport;
/*     */ import org.apache.log4j.spi.ThrowableInformation;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LogEvent
/*     */   implements Serializable
/*     */ {
/*  56 */   private static long startTime = System.currentTimeMillis();
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
/* 149 */   static final Integer[] PARAM_ARRAY = new Integer[1];
/*     */   static final String TO_LEVEL = "toLevel";
/* 151 */   static final Class[] TO_LEVEL_PARAMS = new Class[] { int.class };
/* 152 */   static final Hashtable methodCache = new Hashtable<Object, Object>(3);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LogEvent(String fqnOfCategoryClass, Category logger, Priority level, Object message, Throwable throwable) {
/* 168 */     this.fqnOfCategoryClass = fqnOfCategoryClass;
/* 169 */     this.logger = logger;
/* 170 */     this.categoryName = logger.getName();
/* 171 */     this.level = level;
/* 172 */     this.message = message;
/* 173 */     if (throwable != null) {
/* 174 */       this.throwableInfo = new ThrowableInformation(throwable);
/*     */     }
/* 176 */     this.timeStamp = System.currentTimeMillis();
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
/*     */   public LogEvent(String fqnOfCategoryClass, Category logger, long timeStamp, Priority level, Object message, Throwable throwable) {
/* 195 */     this.fqnOfCategoryClass = fqnOfCategoryClass;
/* 196 */     this.logger = logger;
/* 197 */     this.categoryName = logger.getName();
/* 198 */     this.level = level;
/* 199 */     this.message = message;
/* 200 */     if (throwable != null) {
/* 201 */       this.throwableInfo = new ThrowableInformation(throwable);
/*     */     }
/*     */     
/* 204 */     this.timeStamp = timeStamp;
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
/*     */   public LogEvent(String fqnOfCategoryClass, Logger logger, long timeStamp, Level level, Object message, String threadName, ThrowableInformation throwable, String ndc, LocationInfo info, Map<?, ?> properties) {
/* 227 */     this.fqnOfCategoryClass = fqnOfCategoryClass;
/* 228 */     this.logger = (Category)logger;
/* 229 */     if (logger != null) {
/* 230 */       this.categoryName = logger.getName();
/*     */     } else {
/* 232 */       this.categoryName = null;
/*     */     } 
/* 234 */     this.level = (Priority)level;
/* 235 */     this.message = message;
/* 236 */     if (throwable != null) {
/* 237 */       this.throwableInfo = throwable;
/*     */     }
/*     */     
/* 240 */     this.timeStamp = timeStamp;
/* 241 */     this.threadName = threadName;
/* 242 */     this.ndcLookupRequired = false;
/* 243 */     this.ndc = ndc;
/* 244 */     this.locationInfo = info;
/* 245 */     this.mdcCopyLookupRequired = false;
/* 246 */     if (properties != null) {
/* 247 */       this.mdcCopy = new Hashtable<Object, Object>(properties);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LocationInfo getLocationInformation() {
/* 256 */     if (this.locationInfo == null) {
/* 257 */       this.locationInfo = new LocationInfo(new Throwable(), this.fqnOfCategoryClass);
/*     */     }
/* 259 */     return this.locationInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Level getLevel() {
/* 267 */     return (Level)this.level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLoggerName() {
/* 275 */     return this.categoryName;
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
/* 289 */     if (this.message != null) {
/* 290 */       return this.message;
/*     */     }
/* 292 */     return getRenderedMessage();
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
/* 303 */     if (this.ndcLookupRequired) {
/* 304 */       this.ndcLookupRequired = false;
/* 305 */       this.ndc = NDC.get();
/*     */     } 
/* 307 */     return this.ndc;
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
/* 326 */     if (this.mdcCopy != null) {
/* 327 */       Object r = this.mdcCopy.get(key);
/* 328 */       if (r != null) {
/* 329 */         return r;
/*     */       }
/*     */     } 
/* 332 */     return MDC.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void getMDCCopy() {
/* 340 */     if (this.mdcCopyLookupRequired) {
/* 341 */       this.mdcCopyLookupRequired = false;
/*     */ 
/*     */       
/* 344 */       Hashtable t = MDC.getContext();
/* 345 */       if (t != null) {
/* 346 */         this.mdcCopy = (Hashtable)t.clone();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getRenderedMessage() {
/* 352 */     if (this.renderedMessage == null && this.message != null) {
/* 353 */       if (this.message instanceof String) {
/* 354 */         this.renderedMessage = (String)this.message;
/*     */       } else {
/* 356 */         LoggerRepository repository = this.logger.getLoggerRepository();
/*     */         
/* 358 */         if (repository instanceof RendererSupport) {
/* 359 */           RendererSupport rs = (RendererSupport)repository;
/* 360 */           this.renderedMessage = rs.getRendererMap().findAndRender(this.message);
/*     */         } else {
/* 362 */           this.renderedMessage = this.message.toString();
/*     */         } 
/*     */       } 
/*     */     }
/* 366 */     return this.renderedMessage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long getStartTime() {
/* 374 */     return startTime;
/*     */   }
/*     */   
/*     */   public String getThreadName() {
/* 378 */     if (this.threadName == null)
/* 379 */       this.threadName = Thread.currentThread().getName(); 
/* 380 */     return this.threadName;
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
/* 394 */     return this.throwableInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getThrowableStrRep() {
/* 402 */     if (this.throwableInfo == null) {
/* 403 */       return null;
/*     */     }
/* 405 */     return this.throwableInfo.getThrowableStrRep();
/*     */   }
/*     */ 
/*     */   
/*     */   private void readLevel(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/* 410 */     int p = ois.readInt();
/*     */     try {
/* 412 */       String className = (String)ois.readObject();
/* 413 */       if (className == null) {
/* 414 */         this.level = (Priority)Level.toLevel(p);
/*     */       } else {
/* 416 */         Method m = (Method)methodCache.get(className);
/* 417 */         if (m == null) {
/* 418 */           Class clazz = Loader.loadClass(className);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 425 */           m = clazz.getDeclaredMethod("toLevel", TO_LEVEL_PARAMS);
/* 426 */           methodCache.put(className, m);
/*     */         } 
/* 428 */         PARAM_ARRAY[0] = new Integer(p);
/* 429 */         this.level = (Priority)m.invoke(null, (Object[])PARAM_ARRAY);
/*     */       } 
/* 431 */     } catch (Exception e) {
/* 432 */       LogLog.warn("Level deserialization failed, reverting to default.", e);
/* 433 */       this.level = (Priority)Level.toLevel(p);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/* 438 */     ois.defaultReadObject();
/* 439 */     readLevel(ois);
/*     */ 
/*     */     
/* 442 */     if (this.locationInfo == null) {
/* 443 */       this.locationInfo = new LocationInfo(null, null);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream oos) throws IOException {
/* 449 */     getThreadName();
/*     */ 
/*     */     
/* 452 */     getRenderedMessage();
/*     */ 
/*     */ 
/*     */     
/* 456 */     getNDC();
/*     */ 
/*     */ 
/*     */     
/* 460 */     getMDCCopy();
/*     */ 
/*     */     
/* 463 */     getThrowableStrRep();
/*     */     
/* 465 */     oos.defaultWriteObject();
/*     */ 
/*     */     
/* 468 */     writeLevel(oos);
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeLevel(ObjectOutputStream oos) throws IOException {
/* 473 */     oos.writeInt(this.level.toInt());
/*     */     
/* 475 */     Class<?> clazz = this.level.getClass();
/* 476 */     if (clazz == Level.class) {
/* 477 */       oos.writeObject(null);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 482 */       oos.writeObject(clazz.getName());
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
/* 496 */     if (this.mdcCopy == null) {
/* 497 */       getMDCCopy();
/*     */     }
/* 499 */     if (this.mdcCopy == null) {
/* 500 */       this.mdcCopy = new Hashtable<Object, Object>();
/*     */     }
/* 502 */     this.mdcCopy.put(propName, propValue);
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
/* 516 */     Object value = getMDC(key);
/* 517 */     String retval = null;
/* 518 */     if (value != null) {
/* 519 */       retval = value.toString();
/*     */     }
/* 521 */     return retval;
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
/* 532 */     return (this.locationInfo != null);
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
/* 544 */     return this.timeStamp;
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
/* 558 */     return getProperties().keySet();
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
/* 572 */     getMDCCopy();
/*     */     
/* 574 */     if (this.mdcCopy == null) {
/* 575 */       properties = new HashMap<Object, Object>();
/*     */     } else {
/* 577 */       properties = this.mdcCopy;
/*     */     } 
/* 579 */     return Collections.unmodifiableMap(properties);
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
/* 590 */     return this.fqnOfCategoryClass;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/pattern/LogEvent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */