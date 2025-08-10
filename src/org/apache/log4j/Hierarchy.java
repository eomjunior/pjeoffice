/*     */ package org.apache.log4j;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ import org.apache.log4j.helpers.LogLog;
/*     */ import org.apache.log4j.or.ObjectRenderer;
/*     */ import org.apache.log4j.or.RendererMap;
/*     */ import org.apache.log4j.spi.HierarchyEventListener;
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
/*     */ public class Hierarchy
/*     */   implements LoggerRepository, RendererSupport, ThrowableRendererSupport
/*     */ {
/*     */   private LoggerFactory defaultFactory;
/*     */   private Vector listeners;
/*     */   Hashtable ht;
/*     */   Logger root;
/*     */   RendererMap rendererMap;
/*     */   int thresholdInt;
/*     */   Level threshold;
/*     */   boolean emittedNoAppenderWarning = false;
/*     */   boolean emittedNoResourceBundleWarning = false;
/*  81 */   private ThrowableRenderer throwableRenderer = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Hierarchy(Logger root) {
/*  90 */     this.ht = new Hashtable<Object, Object>();
/*  91 */     this.listeners = new Vector(1);
/*  92 */     this.root = root;
/*     */     
/*  94 */     setThreshold(Level.ALL);
/*  95 */     this.root.setHierarchy(this);
/*  96 */     this.rendererMap = new RendererMap();
/*  97 */     this.defaultFactory = new DefaultCategoryFactory();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addRenderer(Class classToRender, ObjectRenderer or) {
/* 104 */     this.rendererMap.put(classToRender, or);
/*     */   }
/*     */   
/*     */   public void addHierarchyEventListener(HierarchyEventListener listener) {
/* 108 */     if (this.listeners.contains(listener)) {
/* 109 */       LogLog.warn("Ignoring attempt to add an existent listener.");
/*     */     } else {
/* 111 */       this.listeners.addElement(listener);
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
/*     */   public void clear() {
/* 127 */     this.ht.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void emitNoAppenderWarning(Category cat) {
/* 132 */     if (!this.emittedNoAppenderWarning) {
/* 133 */       LogLog.warn("No appenders could be found for logger (" + cat.getName() + ").");
/* 134 */       LogLog.warn("Please initialize the log4j system properly.");
/* 135 */       LogLog.warn("See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.");
/* 136 */       this.emittedNoAppenderWarning = true;
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
/*     */   public Logger exists(String name) {
/* 148 */     Object o = this.ht.get(new CategoryKey(name));
/* 149 */     if (o instanceof Logger) {
/* 150 */       return (Logger)o;
/*     */     }
/* 152 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThreshold(String levelStr) {
/* 160 */     Level l = Level.toLevel(levelStr, (Level)null);
/* 161 */     if (l != null) {
/* 162 */       setThreshold(l);
/*     */     } else {
/* 164 */       LogLog.warn("Could not convert [" + levelStr + "] to Level.");
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
/*     */   public void setThreshold(Level l) {
/* 176 */     if (l != null) {
/* 177 */       this.thresholdInt = l.level;
/* 178 */       this.threshold = l;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void fireAddAppenderEvent(Category logger, Appender appender) {
/* 183 */     if (this.listeners != null) {
/* 184 */       int size = this.listeners.size();
/*     */       
/* 186 */       for (int i = 0; i < size; i++) {
/* 187 */         HierarchyEventListener listener = this.listeners.elementAt(i);
/* 188 */         listener.addAppenderEvent(logger, appender);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   void fireRemoveAppenderEvent(Category logger, Appender appender) {
/* 194 */     if (this.listeners != null) {
/* 195 */       int size = this.listeners.size();
/*     */       
/* 197 */       for (int i = 0; i < size; i++) {
/* 198 */         HierarchyEventListener listener = this.listeners.elementAt(i);
/* 199 */         listener.removeAppenderEvent(logger, appender);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Level getThreshold() {
/* 210 */     return this.threshold;
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
/*     */   
/*     */   public Logger getLogger(String name) {
/* 236 */     return getLogger(name, this.defaultFactory);
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
/*     */   public Logger getLogger(String name, LoggerFactory factory) {
/* 254 */     CategoryKey key = new CategoryKey(name);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 260 */     synchronized (this.ht) {
/* 261 */       Object o = this.ht.get(key);
/* 262 */       if (o == null) {
/* 263 */         Logger logger = factory.makeNewLoggerInstance(name);
/* 264 */         logger.setHierarchy(this);
/* 265 */         this.ht.put(key, logger);
/* 266 */         updateParents(logger);
/* 267 */         return logger;
/* 268 */       }  if (o instanceof Logger)
/* 269 */         return (Logger)o; 
/* 270 */       if (o instanceof ProvisionNode) {
/*     */         
/* 272 */         Logger logger = factory.makeNewLoggerInstance(name);
/* 273 */         logger.setHierarchy(this);
/* 274 */         this.ht.put(key, logger);
/* 275 */         updateChildren((ProvisionNode)o, logger);
/* 276 */         updateParents(logger);
/* 277 */         return logger;
/*     */       } 
/*     */       
/* 280 */       return null;
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
/*     */   public Enumeration getCurrentLoggers() {
/* 296 */     Vector<Object> v = new Vector(this.ht.size());
/*     */     
/* 298 */     Enumeration elems = this.ht.elements();
/* 299 */     while (elems.hasMoreElements()) {
/* 300 */       Object o = elems.nextElement();
/* 301 */       if (o instanceof Logger) {
/* 302 */         v.addElement(o);
/*     */       }
/*     */     } 
/* 305 */     return v.elements();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration getCurrentCategories() {
/* 312 */     return getCurrentLoggers();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RendererMap getRendererMap() {
/* 319 */     return this.rendererMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Logger getRootLogger() {
/* 328 */     return this.root;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDisabled(int level) {
/* 337 */     return (this.thresholdInt > level);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void overrideAsNeeded(String override) {
/* 344 */     LogLog.warn("The Hiearchy.overrideAsNeeded method has been deprecated.");
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
/*     */   public void resetConfiguration() {
/* 366 */     getRootLogger().setLevel(Level.DEBUG);
/* 367 */     this.root.setResourceBundle(null);
/* 368 */     setThreshold(Level.ALL);
/*     */ 
/*     */ 
/*     */     
/* 372 */     synchronized (this.ht) {
/* 373 */       shutdown();
/*     */       
/* 375 */       Enumeration<Logger> cats = getCurrentLoggers();
/* 376 */       while (cats.hasMoreElements()) {
/* 377 */         Logger c = cats.nextElement();
/* 378 */         c.setLevel(null);
/* 379 */         c.setAdditivity(true);
/* 380 */         c.setResourceBundle(null);
/*     */       } 
/*     */     } 
/* 383 */     this.rendererMap.clear();
/* 384 */     this.throwableRenderer = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDisableOverride(String override) {
/* 393 */     LogLog.warn("The Hiearchy.setDisableOverride method has been deprecated.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRenderer(Class renderedClass, ObjectRenderer renderer) {
/* 400 */     this.rendererMap.put(renderedClass, renderer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThrowableRenderer(ThrowableRenderer renderer) {
/* 407 */     this.throwableRenderer = renderer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThrowableRenderer getThrowableRenderer() {
/* 414 */     return this.throwableRenderer;
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
/*     */   public void shutdown() {
/* 435 */     Logger root = getRootLogger();
/*     */ 
/*     */     
/* 438 */     root.closeNestedAppenders();
/*     */     
/* 440 */     synchronized (this.ht) {
/* 441 */       Enumeration<Logger> cats = getCurrentLoggers();
/* 442 */       while (cats.hasMoreElements()) {
/* 443 */         Logger c = cats.nextElement();
/* 444 */         c.closeNestedAppenders();
/*     */       } 
/*     */ 
/*     */       
/* 448 */       root.removeAllAppenders();
/* 449 */       cats = getCurrentLoggers();
/* 450 */       while (cats.hasMoreElements()) {
/* 451 */         Logger c = cats.nextElement();
/* 452 */         c.removeAllAppenders();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final void updateParents(Logger cat) {
/* 477 */     String name = cat.name;
/* 478 */     int length = name.length();
/* 479 */     boolean parentFound = false;
/*     */ 
/*     */     
/*     */     int i;
/*     */     
/* 484 */     for (i = name.lastIndexOf('.', length - 1); i >= 0; i = name.lastIndexOf('.', i - 1)) {
/* 485 */       String substr = name.substring(0, i);
/*     */ 
/*     */       
/* 488 */       CategoryKey key = new CategoryKey(substr);
/* 489 */       Object o = this.ht.get(key);
/*     */       
/* 491 */       if (o == null)
/*     */       
/* 493 */       { ProvisionNode pn = new ProvisionNode(cat);
/* 494 */         this.ht.put(key, pn); }
/* 495 */       else { if (o instanceof Category) {
/* 496 */           parentFound = true;
/* 497 */           cat.parent = (Category)o;
/*     */           break;
/*     */         } 
/* 500 */         if (o instanceof ProvisionNode) {
/* 501 */           ((ProvisionNode)o).addElement((E)cat);
/*     */         } else {
/* 503 */           Exception e = new IllegalStateException("unexpected object type " + o.getClass() + " in ht.");
/* 504 */           e.printStackTrace();
/*     */         }  }
/*     */     
/*     */     } 
/* 508 */     if (!parentFound) {
/* 509 */       cat.parent = this.root;
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
/*     */   private final void updateChildren(ProvisionNode pn, Logger logger) {
/* 528 */     int last = pn.size();
/*     */     
/* 530 */     for (int i = 0; i < last; i++) {
/* 531 */       Logger l = (Logger)pn.elementAt(i);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 536 */       if (!l.parent.name.startsWith(logger.name)) {
/* 537 */         logger.parent = l.parent;
/* 538 */         l.parent = logger;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/Hierarchy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */