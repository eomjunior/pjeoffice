/*      */ package org.apache.log4j.xml;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.Reader;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.net.URL;
/*      */ import java.net.URLConnection;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Properties;
/*      */ import javax.xml.parsers.DocumentBuilder;
/*      */ import javax.xml.parsers.DocumentBuilderFactory;
/*      */ import javax.xml.parsers.FactoryConfigurationError;
/*      */ import org.apache.log4j.Appender;
/*      */ import org.apache.log4j.Layout;
/*      */ import org.apache.log4j.Level;
/*      */ import org.apache.log4j.LogManager;
/*      */ import org.apache.log4j.Logger;
/*      */ import org.apache.log4j.config.PropertySetter;
/*      */ import org.apache.log4j.helpers.Loader;
/*      */ import org.apache.log4j.helpers.LogLog;
/*      */ import org.apache.log4j.helpers.OptionConverter;
/*      */ import org.apache.log4j.or.RendererMap;
/*      */ import org.apache.log4j.spi.AppenderAttachable;
/*      */ import org.apache.log4j.spi.Configurator;
/*      */ import org.apache.log4j.spi.ErrorHandler;
/*      */ import org.apache.log4j.spi.Filter;
/*      */ import org.apache.log4j.spi.LoggerFactory;
/*      */ import org.apache.log4j.spi.LoggerRepository;
/*      */ import org.apache.log4j.spi.RendererSupport;
/*      */ import org.apache.log4j.spi.ThrowableRenderer;
/*      */ import org.apache.log4j.spi.ThrowableRendererSupport;
/*      */ import org.w3c.dom.Document;
/*      */ import org.w3c.dom.Element;
/*      */ import org.w3c.dom.NamedNodeMap;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.NodeList;
/*      */ import org.xml.sax.InputSource;
/*      */ import org.xml.sax.SAXException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class DOMConfigurator
/*      */   implements Configurator
/*      */ {
/*      */   static final String CONFIGURATION_TAG = "log4j:configuration";
/*      */   static final String OLD_CONFIGURATION_TAG = "configuration";
/*      */   static final String RENDERER_TAG = "renderer";
/*      */   private static final String THROWABLE_RENDERER_TAG = "throwableRenderer";
/*      */   static final String APPENDER_TAG = "appender";
/*      */   static final String APPENDER_REF_TAG = "appender-ref";
/*      */   static final String PARAM_TAG = "param";
/*      */   static final String LAYOUT_TAG = "layout";
/*      */   static final String CATEGORY = "category";
/*      */   static final String LOGGER = "logger";
/*      */   static final String LOGGER_REF = "logger-ref";
/*      */   static final String CATEGORY_FACTORY_TAG = "categoryFactory";
/*      */   static final String LOGGER_FACTORY_TAG = "loggerFactory";
/*      */   static final String NAME_ATTR = "name";
/*      */   static final String CLASS_ATTR = "class";
/*      */   static final String VALUE_ATTR = "value";
/*      */   static final String ROOT_TAG = "root";
/*      */   static final String ROOT_REF = "root-ref";
/*      */   static final String LEVEL_TAG = "level";
/*      */   static final String PRIORITY_TAG = "priority";
/*      */   static final String FILTER_TAG = "filter";
/*      */   static final String ERROR_HANDLER_TAG = "errorHandler";
/*      */   static final String REF_ATTR = "ref";
/*      */   static final String ADDITIVITY_ATTR = "additivity";
/*      */   static final String THRESHOLD_ATTR = "threshold";
/*      */   static final String CONFIG_DEBUG_ATTR = "configDebug";
/*      */   static final String INTERNAL_DEBUG_ATTR = "debug";
/*      */   private static final String RESET_ATTR = "reset";
/*      */   static final String RENDERING_CLASS_ATTR = "renderingClass";
/*      */   static final String RENDERED_CLASS_ATTR = "renderedClass";
/*      */   static final String EMPTY_STR = "";
/*  127 */   static final Class[] ONE_STRING_PARAM = new Class[] { String.class };
/*      */   
/*      */   static final String dbfKey = "javax.xml.parsers.DocumentBuilderFactory";
/*      */   
/*      */   Hashtable appenderBag;
/*      */   
/*      */   Properties props;
/*      */   
/*      */   LoggerRepository repository;
/*      */   
/*  137 */   protected LoggerFactory catFactory = null;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DOMConfigurator() {
/*  143 */     this.appenderBag = new Hashtable<Object, Object>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Appender findAppenderByName(Document doc, String appenderName) {
/*  150 */     Appender appender = (Appender)this.appenderBag.get(appenderName);
/*      */     
/*  152 */     if (appender != null) {
/*  153 */       return appender;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  159 */     Element element = null;
/*  160 */     NodeList list = doc.getElementsByTagName("appender");
/*  161 */     for (int t = 0; t < list.getLength(); t++) {
/*  162 */       Node node = list.item(t);
/*  163 */       NamedNodeMap map = node.getAttributes();
/*  164 */       Node attrNode = map.getNamedItem("name");
/*  165 */       if (appenderName.equals(attrNode.getNodeValue())) {
/*  166 */         element = (Element)node;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*      */     
/*  172 */     if (element == null) {
/*  173 */       LogLog.error("No appender named [" + appenderName + "] could be found.");
/*  174 */       return null;
/*      */     } 
/*  176 */     appender = parseAppender(element);
/*  177 */     if (appender != null) {
/*  178 */       this.appenderBag.put(appenderName, appender);
/*      */     }
/*  180 */     return appender;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Appender findAppenderByReference(Element appenderRef) {
/*  189 */     String appenderName = subst(appenderRef.getAttribute("ref"));
/*  190 */     Document doc = appenderRef.getOwnerDocument();
/*  191 */     return findAppenderByName(doc, appenderName);
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
/*      */   private static void parseUnrecognizedElement(Object instance, Element element, Properties props) throws Exception {
/*  207 */     boolean recognized = false;
/*  208 */     if (instance instanceof UnrecognizedElementHandler) {
/*  209 */       recognized = ((UnrecognizedElementHandler)instance).parseUnrecognizedElement(element, props);
/*      */     }
/*  211 */     if (!recognized) {
/*  212 */       LogLog.warn("Unrecognized element " + element.getNodeName());
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
/*      */   private static void quietParseUnrecognizedElement(Object instance, Element element, Properties props) {
/*      */     try {
/*  228 */       parseUnrecognizedElement(instance, element, props);
/*  229 */     } catch (Exception ex) {
/*  230 */       if (ex instanceof InterruptedException || ex instanceof java.io.InterruptedIOException) {
/*  231 */         Thread.currentThread().interrupt();
/*      */       }
/*  233 */       LogLog.error("Error in extension content: ", ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Appender parseAppender(Element appenderElement) {
/*  241 */     String className = subst(appenderElement.getAttribute("class"));
/*  242 */     LogLog.debug("Class name: [" + className + ']');
/*      */     try {
/*  244 */       Object instance = Loader.loadClass(className).newInstance();
/*  245 */       Appender appender = (Appender)instance;
/*  246 */       PropertySetter propSetter = new PropertySetter(appender);
/*      */       
/*  248 */       appender.setName(subst(appenderElement.getAttribute("name")));
/*      */       
/*  250 */       NodeList children = appenderElement.getChildNodes();
/*  251 */       int length = children.getLength();
/*      */       
/*  253 */       for (int loop = 0; loop < length; loop++) {
/*  254 */         Node currentNode = children.item(loop);
/*      */ 
/*      */         
/*  257 */         if (currentNode.getNodeType() == 1) {
/*  258 */           Element currentElement = (Element)currentNode;
/*      */ 
/*      */           
/*  261 */           if (currentElement.getTagName().equals("param")) {
/*  262 */             setParameter(currentElement, propSetter);
/*      */           
/*      */           }
/*  265 */           else if (currentElement.getTagName().equals("layout")) {
/*  266 */             appender.setLayout(parseLayout(currentElement));
/*      */           
/*      */           }
/*  269 */           else if (currentElement.getTagName().equals("filter")) {
/*  270 */             parseFilters(currentElement, appender);
/*  271 */           } else if (currentElement.getTagName().equals("errorHandler")) {
/*  272 */             parseErrorHandler(currentElement, appender);
/*  273 */           } else if (currentElement.getTagName().equals("appender-ref")) {
/*  274 */             String refName = subst(currentElement.getAttribute("ref"));
/*  275 */             if (appender instanceof AppenderAttachable) {
/*  276 */               AppenderAttachable aa = (AppenderAttachable)appender;
/*  277 */               LogLog.debug("Attaching appender named [" + refName + "] to appender named [" + appender
/*  278 */                   .getName() + "].");
/*  279 */               aa.addAppender(findAppenderByReference(currentElement));
/*      */             } else {
/*  281 */               LogLog.error("Requesting attachment of appender named [" + refName + "] to appender named [" + appender
/*  282 */                   .getName() + "] which does not implement org.apache.log4j.spi.AppenderAttachable.");
/*      */             } 
/*      */           } else {
/*      */             
/*  286 */             parseUnrecognizedElement(instance, currentElement, this.props);
/*      */           } 
/*      */         } 
/*      */       } 
/*  290 */       propSetter.activate();
/*  291 */       return appender;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*  297 */     catch (Exception oops) {
/*  298 */       if (oops instanceof InterruptedException || oops instanceof java.io.InterruptedIOException) {
/*  299 */         Thread.currentThread().interrupt();
/*      */       }
/*  301 */       LogLog.error("Could not create an Appender. Reported error follows.", oops);
/*  302 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void parseErrorHandler(Element element, Appender appender) {
/*  310 */     ErrorHandler eh = (ErrorHandler)OptionConverter.instantiateByClassName(subst(element.getAttribute("class")), ErrorHandler.class, null);
/*      */ 
/*      */     
/*  313 */     if (eh != null) {
/*  314 */       eh.setAppender(appender);
/*      */       
/*  316 */       PropertySetter propSetter = new PropertySetter(eh);
/*  317 */       NodeList children = element.getChildNodes();
/*  318 */       int length = children.getLength();
/*      */       
/*  320 */       for (int loop = 0; loop < length; loop++) {
/*  321 */         Node currentNode = children.item(loop);
/*  322 */         if (currentNode.getNodeType() == 1) {
/*  323 */           Element currentElement = (Element)currentNode;
/*  324 */           String tagName = currentElement.getTagName();
/*  325 */           if (tagName.equals("param")) {
/*  326 */             setParameter(currentElement, propSetter);
/*  327 */           } else if (tagName.equals("appender-ref")) {
/*  328 */             eh.setBackupAppender(findAppenderByReference(currentElement));
/*  329 */           } else if (tagName.equals("logger-ref")) {
/*  330 */             String loggerName = currentElement.getAttribute("ref");
/*      */             
/*  332 */             Logger logger = (this.catFactory == null) ? this.repository.getLogger(loggerName) : this.repository.getLogger(loggerName, this.catFactory);
/*  333 */             eh.setLogger(logger);
/*  334 */           } else if (tagName.equals("root-ref")) {
/*  335 */             Logger root = this.repository.getRootLogger();
/*  336 */             eh.setLogger(root);
/*      */           } else {
/*  338 */             quietParseUnrecognizedElement(eh, currentElement, this.props);
/*      */           } 
/*      */         } 
/*      */       } 
/*  342 */       propSetter.activate();
/*  343 */       appender.setErrorHandler(eh);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void parseFilters(Element element, Appender appender) {
/*  351 */     String clazz = subst(element.getAttribute("class"));
/*  352 */     Filter filter = (Filter)OptionConverter.instantiateByClassName(clazz, Filter.class, null);
/*      */     
/*  354 */     if (filter != null) {
/*  355 */       PropertySetter propSetter = new PropertySetter(filter);
/*  356 */       NodeList children = element.getChildNodes();
/*  357 */       int length = children.getLength();
/*      */       
/*  359 */       for (int loop = 0; loop < length; loop++) {
/*  360 */         Node currentNode = children.item(loop);
/*  361 */         if (currentNode.getNodeType() == 1) {
/*  362 */           Element currentElement = (Element)currentNode;
/*  363 */           String tagName = currentElement.getTagName();
/*  364 */           if (tagName.equals("param")) {
/*  365 */             setParameter(currentElement, propSetter);
/*      */           } else {
/*  367 */             quietParseUnrecognizedElement(filter, currentElement, this.props);
/*      */           } 
/*      */         } 
/*      */       } 
/*  371 */       propSetter.activate();
/*  372 */       LogLog.debug("Adding filter of type [" + filter.getClass() + "] to appender named [" + appender.getName() + "].");
/*      */       
/*  374 */       appender.addFilter(filter);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void parseCategory(Element loggerElement) {
/*      */     Logger cat;
/*  383 */     String catName = subst(loggerElement.getAttribute("name"));
/*      */ 
/*      */ 
/*      */     
/*  387 */     String className = subst(loggerElement.getAttribute("class"));
/*      */     
/*  389 */     if ("".equals(className)) {
/*  390 */       LogLog.debug("Retreiving an instance of org.apache.log4j.Logger.");
/*  391 */       cat = (this.catFactory == null) ? this.repository.getLogger(catName) : this.repository.getLogger(catName, this.catFactory);
/*      */     } else {
/*  393 */       LogLog.debug("Desired logger sub-class: [" + className + ']');
/*      */       try {
/*  395 */         Class clazz = Loader.loadClass(className);
/*  396 */         Method getInstanceMethod = clazz.getMethod("getLogger", ONE_STRING_PARAM);
/*  397 */         cat = (Logger)getInstanceMethod.invoke(null, new Object[] { catName });
/*  398 */       } catch (InvocationTargetException oops) {
/*  399 */         if (oops.getTargetException() instanceof InterruptedException || oops
/*  400 */           .getTargetException() instanceof java.io.InterruptedIOException) {
/*  401 */           Thread.currentThread().interrupt();
/*      */         }
/*  403 */         LogLog.error("Could not retrieve category [" + catName + "]. Reported error follows.", oops);
/*      */         return;
/*  405 */       } catch (Exception oops) {
/*  406 */         LogLog.error("Could not retrieve category [" + catName + "]. Reported error follows.", oops);
/*      */ 
/*      */         
/*      */         return;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  414 */     synchronized (cat) {
/*  415 */       boolean additivity = OptionConverter.toBoolean(subst(loggerElement.getAttribute("additivity")), true);
/*      */       
/*  417 */       LogLog.debug("Setting [" + cat.getName() + "] additivity to [" + additivity + "].");
/*  418 */       cat.setAdditivity(additivity);
/*  419 */       parseChildrenOfLoggerElement(loggerElement, cat, false);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void parseCategoryFactory(Element factoryElement) {
/*  427 */     String className = subst(factoryElement.getAttribute("class"));
/*      */     
/*  429 */     if ("".equals(className)) {
/*  430 */       LogLog.error("Category Factory tag class attribute not found.");
/*  431 */       LogLog.debug("No Category Factory configured.");
/*      */     } else {
/*  433 */       LogLog.debug("Desired category factory: [" + className + ']');
/*  434 */       Object factory = OptionConverter.instantiateByClassName(className, LoggerFactory.class, null);
/*  435 */       if (factory instanceof LoggerFactory) {
/*  436 */         this.catFactory = (LoggerFactory)factory;
/*      */       } else {
/*  438 */         LogLog.error("Category Factory class " + className + " does not implement org.apache.log4j.LoggerFactory");
/*      */       } 
/*      */       
/*  441 */       PropertySetter propSetter = new PropertySetter(factory);
/*      */       
/*  443 */       Element currentElement = null;
/*  444 */       Node currentNode = null;
/*  445 */       NodeList children = factoryElement.getChildNodes();
/*  446 */       int length = children.getLength();
/*      */       
/*  448 */       for (int loop = 0; loop < length; loop++) {
/*  449 */         currentNode = children.item(loop);
/*  450 */         if (currentNode.getNodeType() == 1) {
/*  451 */           currentElement = (Element)currentNode;
/*  452 */           if (currentElement.getTagName().equals("param")) {
/*  453 */             setParameter(currentElement, propSetter);
/*      */           } else {
/*  455 */             quietParseUnrecognizedElement(factory, currentElement, this.props);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void parseRoot(Element rootElement) {
/*  466 */     Logger root = this.repository.getRootLogger();
/*      */     
/*  468 */     synchronized (root) {
/*  469 */       parseChildrenOfLoggerElement(rootElement, root, true);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void parseChildrenOfLoggerElement(Element catElement, Logger cat, boolean isRoot) {
/*  478 */     PropertySetter propSetter = new PropertySetter(cat);
/*      */ 
/*      */ 
/*      */     
/*  482 */     cat.removeAllAppenders();
/*      */     
/*  484 */     NodeList children = catElement.getChildNodes();
/*  485 */     int length = children.getLength();
/*      */     
/*  487 */     for (int loop = 0; loop < length; loop++) {
/*  488 */       Node currentNode = children.item(loop);
/*      */       
/*  490 */       if (currentNode.getNodeType() == 1) {
/*  491 */         Element currentElement = (Element)currentNode;
/*  492 */         String tagName = currentElement.getTagName();
/*      */         
/*  494 */         if (tagName.equals("appender-ref")) {
/*  495 */           Element appenderRef = (Element)currentNode;
/*  496 */           Appender appender = findAppenderByReference(appenderRef);
/*  497 */           String refName = subst(appenderRef.getAttribute("ref"));
/*  498 */           if (appender != null) {
/*  499 */             LogLog.debug("Adding appender named [" + refName + "] to category [" + cat.getName() + "].");
/*      */           } else {
/*  501 */             LogLog.debug("Appender named [" + refName + "] not found.");
/*      */           } 
/*  503 */           cat.addAppender(appender);
/*      */         }
/*  505 */         else if (tagName.equals("level")) {
/*  506 */           parseLevel(currentElement, cat, isRoot);
/*  507 */         } else if (tagName.equals("priority")) {
/*  508 */           parseLevel(currentElement, cat, isRoot);
/*  509 */         } else if (tagName.equals("param")) {
/*  510 */           setParameter(currentElement, propSetter);
/*      */         } else {
/*  512 */           quietParseUnrecognizedElement(cat, currentElement, this.props);
/*      */         } 
/*      */       } 
/*      */     } 
/*  516 */     propSetter.activate();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Layout parseLayout(Element layout_element) {
/*  523 */     String className = subst(layout_element.getAttribute("class"));
/*  524 */     LogLog.debug("Parsing layout of class: \"" + className + "\"");
/*      */     try {
/*  526 */       Object instance = Loader.loadClass(className).newInstance();
/*  527 */       Layout layout = (Layout)instance;
/*  528 */       PropertySetter propSetter = new PropertySetter(layout);
/*      */       
/*  530 */       NodeList params = layout_element.getChildNodes();
/*  531 */       int length = params.getLength();
/*      */       
/*  533 */       for (int loop = 0; loop < length; loop++) {
/*  534 */         Node currentNode = params.item(loop);
/*  535 */         if (currentNode.getNodeType() == 1) {
/*  536 */           Element currentElement = (Element)currentNode;
/*  537 */           String tagName = currentElement.getTagName();
/*  538 */           if (tagName.equals("param")) {
/*  539 */             setParameter(currentElement, propSetter);
/*      */           } else {
/*  541 */             parseUnrecognizedElement(instance, currentElement, this.props);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  546 */       propSetter.activate();
/*  547 */       return layout;
/*  548 */     } catch (Exception oops) {
/*  549 */       if (oops instanceof InterruptedException || oops instanceof java.io.InterruptedIOException) {
/*  550 */         Thread.currentThread().interrupt();
/*      */       }
/*  552 */       LogLog.error("Could not create the Layout. Reported error follows.", oops);
/*  553 */       return null;
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void parseRenderer(Element element) {
/*  558 */     String renderingClass = subst(element.getAttribute("renderingClass"));
/*  559 */     String renderedClass = subst(element.getAttribute("renderedClass"));
/*  560 */     if (this.repository instanceof RendererSupport) {
/*  561 */       RendererMap.addRenderer((RendererSupport)this.repository, renderedClass, renderingClass);
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
/*      */   protected ThrowableRenderer parseThrowableRenderer(Element element) {
/*  573 */     String className = subst(element.getAttribute("class"));
/*  574 */     LogLog.debug("Parsing throwableRenderer of class: \"" + className + "\"");
/*      */     try {
/*  576 */       Object instance = Loader.loadClass(className).newInstance();
/*  577 */       ThrowableRenderer tr = (ThrowableRenderer)instance;
/*  578 */       PropertySetter propSetter = new PropertySetter(tr);
/*      */       
/*  580 */       NodeList params = element.getChildNodes();
/*  581 */       int length = params.getLength();
/*      */       
/*  583 */       for (int loop = 0; loop < length; loop++) {
/*  584 */         Node currentNode = params.item(loop);
/*  585 */         if (currentNode.getNodeType() == 1) {
/*  586 */           Element currentElement = (Element)currentNode;
/*  587 */           String tagName = currentElement.getTagName();
/*  588 */           if (tagName.equals("param")) {
/*  589 */             setParameter(currentElement, propSetter);
/*      */           } else {
/*  591 */             parseUnrecognizedElement(instance, currentElement, this.props);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  596 */       propSetter.activate();
/*  597 */       return tr;
/*  598 */     } catch (Exception oops) {
/*  599 */       if (oops instanceof InterruptedException || oops instanceof java.io.InterruptedIOException) {
/*  600 */         Thread.currentThread().interrupt();
/*      */       }
/*  602 */       LogLog.error("Could not create the ThrowableRenderer. Reported error follows.", oops);
/*  603 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void parseLevel(Element element, Logger logger, boolean isRoot) {
/*  611 */     String catName = logger.getName();
/*  612 */     if (isRoot) {
/*  613 */       catName = "root";
/*      */     }
/*      */     
/*  616 */     String priStr = subst(element.getAttribute("value"));
/*  617 */     LogLog.debug("Level value for " + catName + " is  [" + priStr + "].");
/*      */     
/*  619 */     if ("inherited".equalsIgnoreCase(priStr) || "null".equalsIgnoreCase(priStr)) {
/*  620 */       if (isRoot) {
/*  621 */         LogLog.error("Root level cannot be inherited. Ignoring directive.");
/*      */       } else {
/*  623 */         logger.setLevel(null);
/*      */       } 
/*      */     } else {
/*  626 */       String className = subst(element.getAttribute("class"));
/*  627 */       if ("".equals(className)) {
/*  628 */         logger.setLevel(OptionConverter.toLevel(priStr, Level.DEBUG));
/*      */       } else {
/*  630 */         LogLog.debug("Desired Level sub-class: [" + className + ']');
/*      */         try {
/*  632 */           Class clazz = Loader.loadClass(className);
/*  633 */           Method toLevelMethod = clazz.getMethod("toLevel", ONE_STRING_PARAM);
/*  634 */           Level pri = (Level)toLevelMethod.invoke(null, new Object[] { priStr });
/*  635 */           logger.setLevel(pri);
/*  636 */         } catch (Exception oops) {
/*  637 */           if (oops instanceof InterruptedException || oops instanceof java.io.InterruptedIOException) {
/*  638 */             Thread.currentThread().interrupt();
/*      */           }
/*  640 */           LogLog.error("Could not create level [" + priStr + "]. Reported error follows.", oops);
/*      */           return;
/*      */         } 
/*      */       } 
/*      */     } 
/*  645 */     LogLog.debug(catName + " level set to " + logger.getLevel());
/*      */   }
/*      */   
/*      */   protected void setParameter(Element elem, PropertySetter propSetter) {
/*  649 */     String name = subst(elem.getAttribute("name"));
/*  650 */     String value = elem.getAttribute("value");
/*  651 */     value = subst(OptionConverter.convertSpecialChars(value));
/*  652 */     propSetter.setProperty(name, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void configure(Element element) {
/*  661 */     DOMConfigurator configurator = new DOMConfigurator();
/*  662 */     configurator.doConfigure(element, LogManager.getLoggerRepository());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void configureAndWatch(String configFilename) {
/*  673 */     configureAndWatch(configFilename, 60000L);
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
/*      */   public static void configureAndWatch(String configFilename, long delay) {
/*  687 */     XMLWatchdog xdog = new XMLWatchdog(configFilename);
/*  688 */     xdog.setDelay(delay);
/*  689 */     xdog.start();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void doConfigure(final String filename, LoggerRepository repository) {
/*  697 */     ParseAction action = new ParseAction() {
/*      */         public Document parse(DocumentBuilder parser) throws SAXException, IOException {
/*  699 */           return parser.parse(new File(filename));
/*      */         }
/*      */         
/*      */         public String toString() {
/*  703 */           return "file [" + filename + "]";
/*      */         }
/*      */       };
/*  706 */     doConfigure(action, repository);
/*      */   }
/*      */   
/*      */   public void doConfigure(final URL url, LoggerRepository repository) {
/*  710 */     ParseAction action = new ParseAction() {
/*      */         public Document parse(DocumentBuilder parser) throws SAXException, IOException {
/*  712 */           URLConnection uConn = url.openConnection();
/*  713 */           uConn.setUseCaches(false);
/*  714 */           InputStream stream = uConn.getInputStream();
/*      */           try {
/*  716 */             InputSource src = new InputSource(stream);
/*  717 */             src.setSystemId(url.toString());
/*  718 */             return parser.parse(src);
/*      */           } finally {
/*  720 */             stream.close();
/*      */           } 
/*      */         }
/*      */         
/*      */         public String toString() {
/*  725 */           return "url [" + url.toString() + "]";
/*      */         }
/*      */       };
/*  728 */     doConfigure(action, repository);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void doConfigure(final InputStream inputStream, LoggerRepository repository) throws FactoryConfigurationError {
/*  737 */     ParseAction action = new ParseAction() {
/*      */         public Document parse(DocumentBuilder parser) throws SAXException, IOException {
/*  739 */           InputSource inputSource = new InputSource(inputStream);
/*  740 */           inputSource.setSystemId("dummy://log4j.dtd");
/*  741 */           return parser.parse(inputSource);
/*      */         }
/*      */         
/*      */         public String toString() {
/*  745 */           return "input stream [" + inputStream.toString() + "]";
/*      */         }
/*      */       };
/*  748 */     doConfigure(action, repository);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void doConfigure(final Reader reader, LoggerRepository repository) throws FactoryConfigurationError {
/*  756 */     ParseAction action = new ParseAction() {
/*      */         public Document parse(DocumentBuilder parser) throws SAXException, IOException {
/*  758 */           InputSource inputSource = new InputSource(reader);
/*  759 */           inputSource.setSystemId("dummy://log4j.dtd");
/*  760 */           return parser.parse(inputSource);
/*      */         }
/*      */         
/*      */         public String toString() {
/*  764 */           return "reader [" + reader.toString() + "]";
/*      */         }
/*      */       };
/*  767 */     doConfigure(action, repository);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void doConfigure(final InputSource inputSource, LoggerRepository repository) throws FactoryConfigurationError {
/*  776 */     if (inputSource.getSystemId() == null) {
/*  777 */       inputSource.setSystemId("dummy://log4j.dtd");
/*      */     }
/*  779 */     ParseAction action = new ParseAction() {
/*      */         public Document parse(DocumentBuilder parser) throws SAXException, IOException {
/*  781 */           return parser.parse(inputSource);
/*      */         }
/*      */         
/*      */         public String toString() {
/*  785 */           return "input source [" + inputSource.toString() + "]";
/*      */         }
/*      */       };
/*  788 */     doConfigure(action, repository);
/*      */   }
/*      */ 
/*      */   
/*      */   private final void doConfigure(ParseAction action, LoggerRepository repository) throws FactoryConfigurationError {
/*  793 */     DocumentBuilderFactory dbf = null;
/*  794 */     this.repository = repository;
/*      */     try {
/*  796 */       LogLog.debug("System property is :" + OptionConverter.getSystemProperty("javax.xml.parsers.DocumentBuilderFactory", null));
/*  797 */       dbf = DocumentBuilderFactory.newInstance();
/*  798 */       LogLog.debug("Standard DocumentBuilderFactory search succeded.");
/*  799 */       LogLog.debug("DocumentBuilderFactory is: " + dbf.getClass().getName());
/*  800 */     } catch (FactoryConfigurationError fce) {
/*  801 */       Exception e = fce.getException();
/*  802 */       LogLog.debug("Could not instantiate a DocumentBuilderFactory.", e);
/*  803 */       throw fce;
/*      */     } 
/*      */     
/*      */     try {
/*  807 */       dbf.setValidating(true);
/*      */       
/*  809 */       dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
/*  810 */       dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
/*      */       
/*  812 */       DocumentBuilder docBuilder = dbf.newDocumentBuilder();
/*      */       
/*  814 */       docBuilder.setErrorHandler(new SAXErrorHandler());
/*  815 */       docBuilder.setEntityResolver(new Log4jEntityResolver());
/*      */       
/*  817 */       Document doc = action.parse(docBuilder);
/*  818 */       parse(doc.getDocumentElement());
/*  819 */     } catch (Exception e) {
/*  820 */       if (e instanceof InterruptedException || e instanceof java.io.InterruptedIOException) {
/*  821 */         Thread.currentThread().interrupt();
/*      */       }
/*  823 */       LogLog.error("Could not parse " + action.toString() + ".", e);
/*  824 */     } catch (AbstractMethodError e) {
/*  825 */       LogLog.error("Failed to parse XML file. Missing DocumentBuilderFactory.setFeature() method?", e);
/*  826 */       throw e;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void doConfigure(Element element, LoggerRepository repository) {
/*  834 */     this.repository = repository;
/*  835 */     parse(element);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void configure(String filename) throws FactoryConfigurationError {
/*  842 */     (new DOMConfigurator()).doConfigure(filename, LogManager.getLoggerRepository());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void configure(URL url) throws FactoryConfigurationError {
/*  849 */     (new DOMConfigurator()).doConfigure(url, LogManager.getLoggerRepository());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void parse(Element element) {
/*  859 */     String rootElementName = element.getTagName();
/*      */     
/*  861 */     if (!rootElementName.equals("log4j:configuration")) {
/*  862 */       if (rootElementName.equals("configuration")) {
/*  863 */         LogLog.warn("The <configuration> element has been deprecated.");
/*  864 */         LogLog.warn("Use the <log4j:configuration> element instead.");
/*      */       } else {
/*  866 */         LogLog.error("DOM element is - not a <log4j:configuration> element.");
/*      */         
/*      */         return;
/*      */       } 
/*      */     }
/*  871 */     String debugAttrib = subst(element.getAttribute("debug"));
/*      */     
/*  873 */     LogLog.debug("debug attribute= \"" + debugAttrib + "\".");
/*      */ 
/*      */     
/*  876 */     if (!debugAttrib.equals("") && !debugAttrib.equals("null")) {
/*  877 */       LogLog.setInternalDebugging(OptionConverter.toBoolean(debugAttrib, true));
/*      */     } else {
/*  879 */       LogLog.debug("Ignoring debug attribute.");
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  886 */     String resetAttrib = subst(element.getAttribute("reset"));
/*  887 */     LogLog.debug("reset attribute= \"" + resetAttrib + "\".");
/*  888 */     if (!"".equals(resetAttrib) && 
/*  889 */       OptionConverter.toBoolean(resetAttrib, false)) {
/*  890 */       this.repository.resetConfiguration();
/*      */     }
/*      */ 
/*      */     
/*  894 */     String confDebug = subst(element.getAttribute("configDebug"));
/*  895 */     if (!confDebug.equals("") && !confDebug.equals("null")) {
/*  896 */       LogLog.warn("The \"configDebug\" attribute is deprecated.");
/*  897 */       LogLog.warn("Use the \"debug\" attribute instead.");
/*  898 */       LogLog.setInternalDebugging(OptionConverter.toBoolean(confDebug, true));
/*      */     } 
/*      */     
/*  901 */     String thresholdStr = subst(element.getAttribute("threshold"));
/*  902 */     LogLog.debug("Threshold =\"" + thresholdStr + "\".");
/*  903 */     if (!"".equals(thresholdStr) && !"null".equals(thresholdStr)) {
/*  904 */       this.repository.setThreshold(thresholdStr);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  918 */     String tagName = null;
/*  919 */     Element currentElement = null;
/*  920 */     Node currentNode = null;
/*  921 */     NodeList children = element.getChildNodes();
/*  922 */     int length = children.getLength();
/*      */     int loop;
/*  924 */     for (loop = 0; loop < length; loop++) {
/*  925 */       currentNode = children.item(loop);
/*  926 */       if (currentNode.getNodeType() == 1) {
/*  927 */         currentElement = (Element)currentNode;
/*  928 */         tagName = currentElement.getTagName();
/*      */         
/*  930 */         if (tagName.equals("categoryFactory") || tagName.equals("loggerFactory")) {
/*  931 */           parseCategoryFactory(currentElement);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  936 */     for (loop = 0; loop < length; loop++) {
/*  937 */       currentNode = children.item(loop);
/*  938 */       if (currentNode.getNodeType() == 1) {
/*  939 */         currentElement = (Element)currentNode;
/*  940 */         tagName = currentElement.getTagName();
/*      */         
/*  942 */         if (tagName.equals("category") || tagName.equals("logger")) {
/*  943 */           parseCategory(currentElement);
/*  944 */         } else if (tagName.equals("root")) {
/*  945 */           parseRoot(currentElement);
/*  946 */         } else if (tagName.equals("renderer")) {
/*  947 */           parseRenderer(currentElement);
/*  948 */         } else if (tagName.equals("throwableRenderer")) {
/*  949 */           if (this.repository instanceof ThrowableRendererSupport) {
/*  950 */             ThrowableRenderer tr = parseThrowableRenderer(currentElement);
/*  951 */             if (tr != null) {
/*  952 */               ((ThrowableRendererSupport)this.repository).setThrowableRenderer(tr);
/*      */             }
/*      */           } 
/*  955 */         } else if (!tagName.equals("appender") && !tagName.equals("categoryFactory") && 
/*  956 */           !tagName.equals("loggerFactory")) {
/*  957 */           quietParseUnrecognizedElement(this.repository, currentElement, this.props);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected String subst(String value) {
/*  964 */     return subst(value, this.props);
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
/*      */   public static String subst(String value, Properties props) {
/*      */     try {
/*  979 */       return OptionConverter.substVars(value, props);
/*  980 */     } catch (IllegalArgumentException e) {
/*  981 */       LogLog.warn("Could not perform variable substitution.", e);
/*  982 */       return value;
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
/*      */   public static void setParameter(Element elem, PropertySetter propSetter, Properties props) {
/*  995 */     String name = subst(elem.getAttribute("name"), props);
/*  996 */     String value = elem.getAttribute("value");
/*  997 */     value = subst(OptionConverter.convertSpecialChars(value), props);
/*  998 */     propSetter.setProperty(name, value);
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
/*      */   public static Object parseElement(Element element, Properties props, Class expectedClass) throws Exception {
/* 1017 */     String clazz = subst(element.getAttribute("class"), props);
/* 1018 */     Object instance = OptionConverter.instantiateByClassName(clazz, expectedClass, null);
/*      */     
/* 1020 */     if (instance != null) {
/* 1021 */       PropertySetter propSetter = new PropertySetter(instance);
/* 1022 */       NodeList children = element.getChildNodes();
/* 1023 */       int length = children.getLength();
/*      */       
/* 1025 */       for (int loop = 0; loop < length; loop++) {
/* 1026 */         Node currentNode = children.item(loop);
/* 1027 */         if (currentNode.getNodeType() == 1) {
/* 1028 */           Element currentElement = (Element)currentNode;
/* 1029 */           String tagName = currentElement.getTagName();
/* 1030 */           if (tagName.equals("param")) {
/* 1031 */             setParameter(currentElement, propSetter, props);
/*      */           } else {
/* 1033 */             parseUnrecognizedElement(instance, currentElement, props);
/*      */           } 
/*      */         } 
/*      */       } 
/* 1037 */       return instance;
/*      */     } 
/* 1039 */     return null;
/*      */   }
/*      */   
/*      */   private static interface ParseAction {
/*      */     Document parse(DocumentBuilder param1DocumentBuilder) throws SAXException, IOException;
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/xml/DOMConfigurator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */