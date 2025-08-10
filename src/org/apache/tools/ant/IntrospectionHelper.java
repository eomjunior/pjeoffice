/*      */ package org.apache.tools.ant;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.nio.file.Path;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.Hashtable;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import org.apache.tools.ant.taskdefs.PreSetDef;
/*      */ import org.apache.tools.ant.types.EnumeratedAttribute;
/*      */ import org.apache.tools.ant.types.Resource;
/*      */ import org.apache.tools.ant.types.resources.FileProvider;
/*      */ import org.apache.tools.ant.types.resources.FileResource;
/*      */ import org.apache.tools.ant.util.StringUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class IntrospectionHelper
/*      */ {
/*   67 */   private static final Map<String, IntrospectionHelper> HELPERS = new Hashtable<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   75 */   private static final Map<Class<?>, Class<?>> PRIMITIVE_TYPE_MAP = new HashMap<>(8); private static final int MAX_REPORT_NESTED_TEXT = 20;
/*      */   private static final String ELLIPSIS = "...";
/*      */   
/*      */   static {
/*   79 */     Class<?>[] primitives = new Class[] { boolean.class, byte.class, char.class, short.class, int.class, long.class, float.class, double.class };
/*      */     
/*   81 */     Class<?>[] wrappers = new Class[] { Boolean.class, Byte.class, Character.class, Short.class, Integer.class, Long.class, Float.class, Double.class };
/*      */     
/*   83 */     for (int i = 0; i < primitives.length; i++) {
/*   84 */       PRIMITIVE_TYPE_MAP.put(primitives[i], wrappers[i]);
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
/*   95 */   private final Map<String, Class<?>> attributeTypes = new Hashtable<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  101 */   private final Map<String, AttributeSetter> attributeSetters = new Hashtable<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  107 */   private final Map<String, Class<?>> nestedTypes = new Hashtable<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  113 */   private final Map<String, NestedCreator> nestedCreators = new Hashtable<>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  118 */   private final List<Method> addTypeMethods = new ArrayList<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final Method addText;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final Class<?> bean;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static final String NOT_SUPPORTED_CHILD_PREFIX = " doesn't support the nested \"";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static final String NOT_SUPPORTED_CHILD_POSTFIX = "\" element.";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private IntrospectionHelper(Class<?> bean) {
/*  181 */     this.bean = bean;
/*  182 */     Method addTextMethod = null;
/*  183 */     for (Method m : bean.getMethods()) {
/*  184 */       String name = m.getName();
/*  185 */       Class<?> returnType = m.getReturnType();
/*  186 */       Class<?>[] args = m.getParameterTypes();
/*      */ 
/*      */       
/*  189 */       if (args.length == 1 && void.class.equals(returnType) && ("add"
/*  190 */         .equals(name) || "addConfigured".equals(name))) {
/*  191 */         insertAddTypeMethod(m);
/*      */         
/*      */         continue;
/*      */       } 
/*  195 */       if (ProjectComponent.class.isAssignableFrom(bean) && args.length == 1 && 
/*  196 */         isHiddenSetMethod(name, args[0])) {
/*      */         continue;
/*      */       }
/*      */       
/*  200 */       if (isContainer() && args.length == 1 && "addTask".equals(name) && Task.class
/*  201 */         .equals(args[0])) {
/*      */         continue;
/*      */       }
/*  204 */       if ("addText".equals(name) && void.class.equals(returnType) && args.length == 1 && String.class
/*  205 */         .equals(args[0])) {
/*  206 */         addTextMethod = m; continue;
/*  207 */       }  if (name.startsWith("set") && void.class.equals(returnType) && args.length == 1 && 
/*  208 */         !args[0].isArray()) {
/*  209 */         String propName = getPropertyName(name, "set");
/*  210 */         AttributeSetter as = this.attributeSetters.get(propName);
/*  211 */         if (as != null) {
/*  212 */           if (String.class.equals(args[0])) {
/*      */             continue;
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  221 */           if (File.class.equals(args[0]))
/*      */           {
/*  223 */             if (Resource.class.equals(as.type) || FileProvider.class.equals(as.type)) {
/*      */               continue;
/*      */             }
/*      */           }
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  237 */         as = createAttributeSetter(m, args[0], propName);
/*  238 */         if (as != null) {
/*  239 */           this.attributeTypes.put(propName, args[0]);
/*  240 */           this.attributeSetters.put(propName, as);
/*      */         }  continue;
/*  242 */       }  if (name.startsWith("create") && !returnType.isArray() && 
/*  243 */         !returnType.isPrimitive() && args.length == 0) {
/*      */         
/*  245 */         String propName = getPropertyName(name, "create");
/*      */ 
/*      */         
/*  248 */         if (this.nestedCreators.get(propName) == null) {
/*  249 */           this.nestedTypes.put(propName, returnType);
/*  250 */           this.nestedCreators.put(propName, new CreateNestedCreator(m));
/*      */         } 
/*  252 */       } else if (name.startsWith("addConfigured") && void.class
/*  253 */         .equals(returnType) && args.length == 1 && 
/*  254 */         !String.class.equals(args[0]) && 
/*  255 */         !args[0].isArray() && !args[0].isPrimitive()) {
/*      */         try {
/*  257 */           Constructor<?> constructor = null;
/*      */           try {
/*  259 */             constructor = args[0].getConstructor(new Class[0]);
/*  260 */           } catch (NoSuchMethodException ex) {
/*  261 */             constructor = args[0].getConstructor(new Class[] { Project.class });
/*      */           } 
/*  263 */           String propName = getPropertyName(name, "addConfigured");
/*  264 */           this.nestedTypes.put(propName, args[0]);
/*  265 */           this.nestedCreators.put(propName, new AddNestedCreator(m, constructor, 2));
/*      */         }
/*  267 */         catch (NoSuchMethodException noSuchMethodException) {}
/*      */       
/*      */       }
/*  270 */       else if (name.startsWith("add") && void.class
/*  271 */         .equals(returnType) && args.length == 1 && 
/*  272 */         !String.class.equals(args[0]) && 
/*  273 */         !args[0].isArray() && !args[0].isPrimitive()) {
/*      */         try {
/*  275 */           Constructor<?> constructor = null;
/*      */           try {
/*  277 */             constructor = args[0].getConstructor(new Class[0]);
/*  278 */           } catch (NoSuchMethodException ex) {
/*  279 */             constructor = args[0].getConstructor(new Class[] { Project.class });
/*      */           } 
/*  281 */           String propName = getPropertyName(name, "add");
/*  282 */           if (this.nestedTypes.get(propName) == null) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  290 */             this.nestedTypes.put(propName, args[0]);
/*  291 */             this.nestedCreators.put(propName, new AddNestedCreator(m, constructor, 1));
/*      */           } 
/*  293 */         } catch (NoSuchMethodException noSuchMethodException) {}
/*      */       } 
/*      */       
/*      */       continue;
/*      */     } 
/*  298 */     this.addText = addTextMethod;
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
/*      */   private boolean isHiddenSetMethod(String name, Class<?> type) {
/*  310 */     return (("setLocation".equals(name) && Location.class.equals(type)) || ("setTaskType"
/*  311 */       .equals(name) && String.class.equals(type)));
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
/*      */   public static IntrospectionHelper getHelper(Class<?> c) {
/*  324 */     return getHelper(null, c);
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
/*      */   public static IntrospectionHelper getHelper(Project p, Class<?> c) {
/*  341 */     if (p == null)
/*      */     {
/*      */       
/*  344 */       return new IntrospectionHelper(c);
/*      */     }
/*  346 */     IntrospectionHelper ih = HELPERS.get(c.getName());
/*  347 */     if (ih != null && ih.bean == c) {
/*  348 */       return ih;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  355 */     ih = new IntrospectionHelper(c);
/*  356 */     synchronized (HELPERS) {
/*  357 */       IntrospectionHelper cached = HELPERS.get(c.getName());
/*  358 */       if (cached != null && cached.bean == c) {
/*  359 */         return cached;
/*      */       }
/*      */       
/*  362 */       HELPERS.put(c.getName(), ih);
/*  363 */       return ih;
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
/*      */   public void setAttribute(Project p, Object element, String attributeName, Object value) throws BuildException {
/*  387 */     AttributeSetter as = this.attributeSetters.get(attributeName
/*  388 */         .toLowerCase(Locale.ENGLISH));
/*  389 */     if (as == null && value != null) {
/*  390 */       if (element instanceof DynamicAttributeNS) {
/*  391 */         DynamicAttributeNS dc = (DynamicAttributeNS)element;
/*  392 */         String uriPlusPrefix = ProjectHelper.extractUriFromComponentName(attributeName);
/*  393 */         String uri = ProjectHelper.extractUriFromComponentName(uriPlusPrefix);
/*  394 */         String localName = ProjectHelper.extractNameFromComponentName(attributeName);
/*  395 */         String qName = uri.isEmpty() ? localName : (uri + ":" + localName);
/*  396 */         dc.setDynamicAttribute(uri, localName, qName, value.toString());
/*      */         return;
/*      */       } 
/*  399 */       if (element instanceof DynamicObjectAttribute) {
/*  400 */         DynamicObjectAttribute dc = (DynamicObjectAttribute)element;
/*  401 */         dc.setDynamicAttribute(attributeName.toLowerCase(Locale.ENGLISH), value);
/*      */         return;
/*      */       } 
/*  404 */       if (element instanceof DynamicAttribute) {
/*  405 */         DynamicAttribute dc = (DynamicAttribute)element;
/*  406 */         dc.setDynamicAttribute(attributeName.toLowerCase(Locale.ENGLISH), value.toString());
/*      */         return;
/*      */       } 
/*  409 */       if (attributeName.contains(":")) {
/*      */         return;
/*      */       }
/*  412 */       String msg = getElementName(p, element) + " doesn't support the \"" + attributeName + "\" attribute.";
/*      */       
/*  414 */       throw new UnsupportedAttributeException(msg, attributeName);
/*      */     } 
/*  416 */     if (as != null) {
/*      */       try {
/*  418 */         as.setObject(p, element, value);
/*  419 */       } catch (IllegalAccessException ie) {
/*      */         
/*  421 */         throw new BuildException(ie);
/*  422 */       } catch (InvocationTargetException ite) {
/*  423 */         throw extractBuildException(ite);
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
/*      */   public void setAttribute(Project p, Object element, String attributeName, String value) throws BuildException {
/*  448 */     setAttribute(p, element, attributeName, value);
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
/*      */   public void addText(Project project, Object element, String text) throws BuildException {
/*  470 */     if (this.addText == null) {
/*  471 */       text = text.trim();
/*      */       
/*  473 */       if (text.isEmpty()) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/*  478 */       throw new BuildException(project.getElementName(element) + " doesn't support nested text data (\"" + 
/*  479 */           condenseText(text) + "\").");
/*      */     } 
/*      */     try {
/*  482 */       this.addText.invoke(element, new Object[] { text });
/*  483 */     } catch (IllegalAccessException ie) {
/*      */       
/*  485 */       throw new BuildException(ie);
/*  486 */     } catch (InvocationTargetException ite) {
/*  487 */       throw extractBuildException(ite);
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
/*      */ 
/*      */   
/*      */   public void throwNotSupported(Project project, Object parent, String elementName) {
/*  514 */     String msg = project.getElementName(parent) + " doesn't support the nested \"" + elementName + "\" element.";
/*      */ 
/*      */     
/*  517 */     throw new UnsupportedElementException(msg, elementName);
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
/*      */   private NestedCreator getNestedCreator(Project project, String parentUri, Object parent, String elementName, UnknownElement child) throws BuildException {
/*  535 */     String uri = ProjectHelper.extractUriFromComponentName(elementName);
/*  536 */     String name = ProjectHelper.extractNameFromComponentName(elementName);
/*      */     
/*  538 */     if (uri.equals("antlib:org.apache.tools.ant")) {
/*  539 */       uri = "";
/*      */     }
/*  541 */     if (parentUri.equals("antlib:org.apache.tools.ant")) {
/*  542 */       parentUri = "";
/*      */     }
/*  544 */     NestedCreator nc = null;
/*  545 */     if (uri.equals(parentUri) || uri.isEmpty()) {
/*  546 */       nc = this.nestedCreators.get(name.toLowerCase(Locale.ENGLISH));
/*      */     }
/*  548 */     if (nc == null) {
/*  549 */       nc = createAddTypeCreator(project, parent, elementName);
/*      */     }
/*  551 */     if (nc == null && (parent instanceof DynamicElementNS || parent instanceof DynamicElement)) {
/*      */       
/*  553 */       String qName = (child == null) ? name : child.getQName();
/*  554 */       final Object nestedElement = createDynamicElement(parent, 
/*  555 */           (child == null) ? "" : child.getNamespace(), name, qName);
/*  556 */       if (nestedElement != null) {
/*  557 */         nc = new NestedCreator(null)
/*      */           {
/*      */             Object create(Project project, Object parent, Object ignore) {
/*  560 */               return nestedElement;
/*      */             }
/*      */           };
/*      */       }
/*      */     } 
/*  565 */     if (nc == null) {
/*  566 */       throwNotSupported(project, parent, elementName);
/*      */     }
/*  568 */     return nc;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Object createDynamicElement(Object parent, String ns, String localName, String qName) {
/*  579 */     Object nestedElement = null;
/*  580 */     if (parent instanceof DynamicElementNS) {
/*  581 */       DynamicElementNS dc = (DynamicElementNS)parent;
/*  582 */       nestedElement = dc.createDynamicElement(ns, localName, qName);
/*      */     } 
/*  584 */     if (nestedElement == null && parent instanceof DynamicElement) {
/*  585 */       DynamicElement dc = (DynamicElement)parent;
/*      */       
/*  587 */       nestedElement = dc.createDynamicElement(localName.toLowerCase(Locale.ENGLISH));
/*      */     } 
/*  589 */     return nestedElement;
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
/*      */   @Deprecated
/*      */   public Object createElement(Project project, Object parent, String elementName) throws BuildException {
/*  617 */     NestedCreator nc = getNestedCreator(project, "", parent, elementName, null);
/*      */     try {
/*  619 */       Object nestedElement = nc.create(project, parent, null);
/*  620 */       if (project != null) {
/*  621 */         project.setProjectReference(nestedElement);
/*      */       }
/*  623 */       return nestedElement;
/*  624 */     } catch (IllegalAccessException|InstantiationException ie) {
/*      */       
/*  626 */       throw new BuildException(ie);
/*  627 */     } catch (InvocationTargetException ite) {
/*  628 */       throw extractBuildException(ite);
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
/*      */   public Creator getElementCreator(Project project, String parentUri, Object parent, String elementName, UnknownElement ue) {
/*  646 */     NestedCreator nc = getNestedCreator(project, parentUri, parent, elementName, ue);
/*  647 */     return new Creator(project, parent, nc);
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
/*      */   public boolean isDynamic() {
/*  662 */     return (DynamicElement.class.isAssignableFrom(this.bean) || DynamicElementNS.class
/*  663 */       .isAssignableFrom(this.bean));
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
/*      */   public boolean isContainer() {
/*  677 */     return TaskContainer.class.isAssignableFrom(this.bean);
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
/*      */   public boolean supportsNestedElement(String elementName) {
/*  689 */     return supportsNestedElement("", elementName);
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
/*      */   public boolean supportsNestedElement(String parentUri, String elementName) {
/*  708 */     return (isDynamic() || !this.addTypeMethods.isEmpty() || 
/*  709 */       supportsReflectElement(parentUri, elementName));
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
/*      */   public boolean supportsNestedElement(String parentUri, String elementName, Project project, Object parent) {
/*  731 */     return ((!this.addTypeMethods.isEmpty() && 
/*  732 */       createAddTypeCreator(project, parent, elementName) != null) || 
/*  733 */       isDynamic() || supportsReflectElement(parentUri, elementName));
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
/*      */   public boolean supportsReflectElement(String parentUri, String elementName) {
/*  747 */     String name = ProjectHelper.extractNameFromComponentName(elementName);
/*  748 */     if (!this.nestedCreators.containsKey(name.toLowerCase(Locale.ENGLISH))) {
/*  749 */       return false;
/*      */     }
/*  751 */     String uri = ProjectHelper.extractUriFromComponentName(elementName);
/*  752 */     if (uri.equals("antlib:org.apache.tools.ant") || uri.isEmpty()) {
/*  753 */       return true;
/*      */     }
/*  755 */     if (parentUri.equals("antlib:org.apache.tools.ant")) {
/*  756 */       parentUri = "";
/*      */     }
/*  758 */     return uri.equals(parentUri);
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
/*      */   public void storeElement(Project project, Object parent, Object child, String elementName) throws BuildException {
/*  783 */     if (elementName == null) {
/*      */       return;
/*      */     }
/*  786 */     NestedCreator ns = this.nestedCreators.get(elementName.toLowerCase(Locale.ENGLISH));
/*  787 */     if (ns == null) {
/*      */       return;
/*      */     }
/*      */     try {
/*  791 */       ns.store(parent, child);
/*  792 */     } catch (IllegalAccessException|InstantiationException ie) {
/*      */       
/*  794 */       throw new BuildException(ie);
/*  795 */     } catch (InvocationTargetException ite) {
/*  796 */       throw extractBuildException(ite);
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
/*      */   private static BuildException extractBuildException(InvocationTargetException ite) {
/*  808 */     Throwable t = ite.getTargetException();
/*  809 */     if (t instanceof BuildException) {
/*  810 */       return (BuildException)t;
/*      */     }
/*  812 */     return new BuildException(t);
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
/*      */   public Class<?> getElementType(String elementName) throws BuildException {
/*  828 */     Class<?> nt = this.nestedTypes.get(elementName);
/*  829 */     if (nt == null) {
/*  830 */       throw new UnsupportedElementException("Class " + this.bean
/*  831 */           .getName() + " doesn't support the nested \"" + elementName + "\" element.", elementName);
/*      */     }
/*      */     
/*  834 */     return nt;
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
/*      */   public Class<?> getAttributeType(String attributeName) throws BuildException {
/*  850 */     Class<?> at = this.attributeTypes.get(attributeName);
/*  851 */     if (at == null) {
/*  852 */       throw new UnsupportedAttributeException("Class " + this.bean
/*  853 */           .getName() + " doesn't support the \"" + attributeName + "\" attribute.", attributeName);
/*      */     }
/*      */     
/*  856 */     return at;
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
/*      */   public Method getAddTextMethod() throws BuildException {
/*  870 */     if (!supportsCharacters()) {
/*  871 */       throw new BuildException("Class " + this.bean.getName() + " doesn't support nested text data.");
/*      */     }
/*      */     
/*  874 */     return this.addText;
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
/*      */   public Method getElementMethod(String elementName) throws BuildException {
/*  890 */     NestedCreator creator = this.nestedCreators.get(elementName);
/*  891 */     if (creator == null) {
/*  892 */       throw new UnsupportedElementException("Class " + this.bean
/*  893 */           .getName() + " doesn't support the nested \"" + elementName + "\" element.", elementName);
/*      */     }
/*      */     
/*  896 */     return creator.method;
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
/*      */   public Method getAttributeMethod(String attributeName) throws BuildException {
/*  911 */     AttributeSetter setter = this.attributeSetters.get(attributeName);
/*  912 */     if (setter == null) {
/*  913 */       throw new UnsupportedAttributeException("Class " + this.bean
/*  914 */           .getName() + " doesn't support the \"" + attributeName + "\" attribute.", attributeName);
/*      */     }
/*      */     
/*  917 */     return setter.method;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean supportsCharacters() {
/*  926 */     return (this.addText != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Enumeration<String> getAttributes() {
/*  936 */     return Collections.enumeration(this.attributeSetters.keySet());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<String, Class<?>> getAttributeMap() {
/*  947 */     return this.attributeTypes.isEmpty() ? 
/*  948 */       Collections.<String, Class<?>>emptyMap() : Collections.<String, Class<?>>unmodifiableMap(this.attributeTypes);
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
/*      */   public Enumeration<String> getNestedElements() {
/*  960 */     return Collections.enumeration(this.nestedTypes.keySet());
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
/*      */   public Map<String, Class<?>> getNestedElementMap() {
/*  972 */     return this.nestedTypes.isEmpty() ? 
/*  973 */       Collections.<String, Class<?>>emptyMap() : Collections.<String, Class<?>>unmodifiableMap(this.nestedTypes);
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
/*      */   public List<Method> getExtensionPoints() {
/*  994 */     return this.addTypeMethods.isEmpty() ? 
/*  995 */       Collections.<Method>emptyList() : Collections.<Method>unmodifiableList(this.addTypeMethods);
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
/*      */   private AttributeSetter createAttributeSetter(final Method m, Class<?> arg, final String attrName) {
/*      */     boolean includeProject;
/*      */     Constructor<?> c;
/* 1036 */     final Class<?> reflectedArg = PRIMITIVE_TYPE_MAP.getOrDefault(arg, arg);
/*      */ 
/*      */     
/* 1039 */     if (Object.class == reflectedArg) {
/* 1040 */       return new AttributeSetter(m, arg)
/*      */         {
/*      */           
/*      */           public void set(Project p, Object parent, String value) throws InvocationTargetException, IllegalAccessException
/*      */           {
/* 1045 */             throw new BuildException("Internal ant problem - this should not get called");
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/* 1051 */     if (String.class.equals(reflectedArg)) {
/* 1052 */       return new AttributeSetter(m, arg)
/*      */         {
/*      */           public void set(Project p, Object parent, String value) throws InvocationTargetException, IllegalAccessException
/*      */           {
/* 1056 */             (new String[1])[0] = value; m.invoke(parent, (Object[])new String[1]);
/*      */           }
/*      */         };
/*      */     }
/*      */     
/* 1061 */     if (Character.class.equals(reflectedArg)) {
/* 1062 */       return new AttributeSetter(m, arg)
/*      */         {
/*      */           public void set(Project p, Object parent, String value) throws InvocationTargetException, IllegalAccessException
/*      */           {
/* 1066 */             if (value.isEmpty()) {
/* 1067 */               throw new BuildException("The value \"\" is not a legal value for attribute \"" + attrName + "\"");
/*      */             }
/*      */             
/* 1070 */             (new Character[1])[0] = Character.valueOf(value.charAt(0)); m.invoke(parent, (Object[])new Character[1]);
/*      */           }
/*      */         };
/*      */     }
/*      */     
/* 1075 */     if (Boolean.class.equals(reflectedArg)) {
/* 1076 */       return new AttributeSetter(m, arg)
/*      */         {
/*      */           public void set(Project p, Object parent, String value) throws InvocationTargetException, IllegalAccessException
/*      */           {
/* 1080 */             (new Boolean[1])[0] = 
/* 1081 */               Project.toBoolean(value) ? Boolean.TRUE : Boolean.FALSE;
/*      */             m.invoke(parent, (Object[])new Boolean[1]);
/*      */           }
/*      */         };
/*      */     }
/* 1086 */     if (Class.class.equals(reflectedArg)) {
/* 1087 */       return new AttributeSetter(m, arg)
/*      */         {
/*      */           public void set(Project p, Object parent, String value) throws InvocationTargetException, IllegalAccessException, BuildException
/*      */           {
/*      */             try {
/* 1092 */               m.invoke(parent, new Object[] { Class.forName(value) });
/* 1093 */             } catch (ClassNotFoundException ce) {
/* 1094 */               throw new BuildException(ce);
/*      */             } 
/*      */           }
/*      */         };
/*      */     }
/*      */     
/* 1100 */     if (File.class.equals(reflectedArg)) {
/* 1101 */       return new AttributeSetter(m, arg)
/*      */         {
/*      */           public void set(Project p, Object parent, String value) throws InvocationTargetException, IllegalAccessException
/*      */           {
/* 1105 */             m.invoke(parent, new Object[] { p.resolveFile(value) });
/*      */           }
/*      */         };
/*      */     }
/*      */     
/* 1110 */     if (Path.class.equals(reflectedArg)) {
/* 1111 */       return new AttributeSetter(m, arg)
/*      */         {
/*      */           public void set(Project p, Object parent, String value) throws InvocationTargetException, IllegalAccessException
/*      */           {
/* 1115 */             m.invoke(parent, new Object[] { p.resolveFile(value).toPath() });
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/* 1121 */     if (Resource.class.equals(reflectedArg) || FileProvider.class.equals(reflectedArg)) {
/* 1122 */       return new AttributeSetter(m, arg)
/*      */         {
/*      */           void set(Project p, Object parent, String value) throws InvocationTargetException, IllegalAccessException, BuildException
/*      */           {
/* 1126 */             m.invoke(parent, new Object[] { new FileResource(p, p.resolveFile(value)) });
/*      */           }
/*      */         };
/*      */     }
/*      */     
/* 1131 */     if (EnumeratedAttribute.class.isAssignableFrom(reflectedArg)) {
/* 1132 */       return new AttributeSetter(m, arg)
/*      */         {
/*      */           
/*      */           public void set(Project p, Object parent, String value) throws InvocationTargetException, IllegalAccessException, BuildException
/*      */           {
/*      */             try {
/* 1138 */               EnumeratedAttribute ea = reflectedArg.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
/* 1139 */               ea.setValue(value);
/* 1140 */               m.invoke(parent, new Object[] { ea });
/* 1141 */             } catch (InstantiationException|NoSuchMethodException ie) {
/* 1142 */               throw new BuildException(ie);
/*      */             } 
/*      */           }
/*      */         };
/*      */     }
/*      */     
/* 1148 */     AttributeSetter setter = getEnumSetter(reflectedArg, m, arg);
/* 1149 */     if (setter != null) {
/* 1150 */       return setter;
/*      */     }
/*      */     
/* 1153 */     if (Long.class.equals(reflectedArg)) {
/* 1154 */       return new AttributeSetter(m, arg)
/*      */         {
/*      */           public void set(Project p, Object parent, String value) throws InvocationTargetException, IllegalAccessException, BuildException
/*      */           {
/*      */             try {
/* 1159 */               m.invoke(parent, new Object[] { Long.valueOf(StringUtils.parseHumanSizes(value)) });
/* 1160 */             } catch (NumberFormatException e) {
/* 1161 */               throw new BuildException("Can't assign non-numeric value '" + value + "' to attribute " + attrName);
/*      */             
/*      */             }
/* 1164 */             catch (InvocationTargetException|IllegalAccessException e) {
/* 1165 */               throw e;
/* 1166 */             } catch (Exception e) {
/* 1167 */               throw new BuildException(e);
/*      */             } 
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1180 */       c = reflectedArg.getConstructor(new Class[] { Project.class, String.class });
/* 1181 */       includeProject = true;
/* 1182 */     } catch (NoSuchMethodException nme) {
/*      */       
/*      */       try {
/* 1185 */         c = reflectedArg.getConstructor(new Class[] { String.class });
/* 1186 */         includeProject = false;
/* 1187 */       } catch (NoSuchMethodException nme2) {
/*      */         
/* 1189 */         return null;
/*      */       } 
/*      */     } 
/* 1192 */     final boolean finalIncludeProject = includeProject;
/* 1193 */     final Constructor<?> finalConstructor = c;
/*      */     
/* 1195 */     return new AttributeSetter(m, arg)
/*      */       {
/*      */         
/*      */         public void set(Project p, Object parent, String value) throws InvocationTargetException, IllegalAccessException, BuildException
/*      */         {
/*      */           try {
/* 1201 */             (new Object[2])[0] = p; (new Object[2])[1] = value; (new Object[1])[0] = value; Object[] args = finalIncludeProject ? new Object[2] : new Object[1];
/*      */             
/* 1203 */             Object attribute = finalConstructor.newInstance(args);
/* 1204 */             if (p != null) {
/* 1205 */               p.setProjectReference(attribute);
/*      */             }
/* 1207 */             m.invoke(parent, new Object[] { attribute });
/* 1208 */           } catch (InvocationTargetException e) {
/* 1209 */             Throwable cause = e.getCause();
/* 1210 */             if (cause instanceof IllegalArgumentException) {
/* 1211 */               throw new BuildException("Can't assign value '" + value + "' to attribute " + attrName + ", reason: " + cause
/*      */ 
/*      */                   
/* 1214 */                   .getClass() + " with message '" + cause
/*      */                   
/* 1216 */                   .getMessage() + "'");
/*      */             }
/* 1218 */             throw e;
/* 1219 */           } catch (InstantiationException ie) {
/* 1220 */             throw new BuildException(ie);
/*      */           } 
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   private AttributeSetter getEnumSetter(final Class<?> reflectedArg, final Method m, Class<?> arg) {
/* 1228 */     if (reflectedArg.isEnum()) {
/* 1229 */       return new AttributeSetter(m, arg)
/*      */         {
/*      */           public void set(Project p, Object parent, String value) throws InvocationTargetException, IllegalAccessException, BuildException
/*      */           {
/*      */             Enum<?> setValue;
/*      */ 
/*      */             
/*      */             try {
/* 1237 */               Enum<?> enumValue = Enum.valueOf(reflectedArg, value);
/*      */               
/* 1239 */               setValue = enumValue;
/* 1240 */             } catch (IllegalArgumentException e) {
/*      */ 
/*      */               
/* 1243 */               throw new BuildException("'" + value + "' is not a permitted value for " + reflectedArg
/* 1244 */                   .getName());
/*      */             } 
/* 1246 */             m.invoke(parent, new Object[] { setValue });
/*      */           }
/*      */         };
/*      */     }
/* 1250 */     return null;
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
/*      */   private String getElementName(Project project, Object element) {
/* 1265 */     return project.getElementName(element);
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
/*      */   private static String getPropertyName(String methodName, String prefix) {
/* 1280 */     return methodName.substring(prefix.length()).toLowerCase(Locale.ENGLISH);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final class Creator
/*      */   {
/*      */     private final IntrospectionHelper.NestedCreator nestedCreator;
/*      */ 
/*      */ 
/*      */     
/*      */     private final Object parent;
/*      */ 
/*      */ 
/*      */     
/*      */     private final Project project;
/*      */ 
/*      */ 
/*      */     
/*      */     private Object nestedObject;
/*      */ 
/*      */ 
/*      */     
/*      */     private String polyType;
/*      */ 
/*      */ 
/*      */     
/*      */     private Creator(Project project, Object parent, IntrospectionHelper.NestedCreator nestedCreator) {
/* 1309 */       this.project = project;
/* 1310 */       this.parent = parent;
/* 1311 */       this.nestedCreator = nestedCreator;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setPolyType(String polyType) {
/* 1320 */       this.polyType = polyType;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Object create() {
/* 1329 */       if (this.polyType != null) {
/* 1330 */         if (!this.nestedCreator.isPolyMorphic()) {
/* 1331 */           throw new BuildException("Not allowed to use the polymorphic form for this element");
/*      */         }
/*      */         
/* 1334 */         ComponentHelper helper = ComponentHelper.getComponentHelper(this.project);
/* 1335 */         this.nestedObject = helper.createComponent(this.polyType);
/* 1336 */         if (this.nestedObject == null) {
/* 1337 */           throw new BuildException("Unable to create object of type " + this.polyType);
/*      */         }
/*      */       } 
/*      */       try {
/* 1341 */         this.nestedObject = this.nestedCreator.create(this.project, this.parent, this.nestedObject);
/* 1342 */         if (this.project != null) {
/* 1343 */           this.project.setProjectReference(this.nestedObject);
/*      */         }
/* 1345 */         return this.nestedObject;
/* 1346 */       } catch (IllegalAccessException|InstantiationException ex) {
/* 1347 */         throw new BuildException(ex);
/* 1348 */       } catch (IllegalArgumentException ex) {
/* 1349 */         if (this.polyType == null) {
/* 1350 */           throw ex;
/*      */         }
/* 1352 */         throw new BuildException("Invalid type used " + this.polyType);
/* 1353 */       } catch (InvocationTargetException ex) {
/* 1354 */         throw IntrospectionHelper.extractBuildException(ex);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Object getRealObject() {
/* 1362 */       return this.nestedCreator.getRealObject();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void store() {
/*      */       try {
/* 1371 */         this.nestedCreator.store(this.parent, this.nestedObject);
/* 1372 */       } catch (IllegalAccessException|InstantiationException ex) {
/* 1373 */         throw new BuildException(ex);
/* 1374 */       } catch (IllegalArgumentException ex) {
/* 1375 */         if (this.polyType == null) {
/* 1376 */           throw ex;
/*      */         }
/* 1378 */         throw new BuildException("Invalid type used " + this.polyType);
/* 1379 */       } catch (InvocationTargetException ex) {
/* 1380 */         throw IntrospectionHelper.extractBuildException(ex);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static abstract class NestedCreator
/*      */   {
/*      */     private final Method method;
/*      */ 
/*      */     
/*      */     protected NestedCreator(Method m) {
/* 1393 */       this.method = m;
/*      */     }
/*      */     Method getMethod() {
/* 1396 */       return this.method;
/*      */     }
/*      */     boolean isPolyMorphic() {
/* 1399 */       return false;
/*      */     }
/*      */     Object getRealObject() {
/* 1402 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     void store(Object parent, Object child) throws InvocationTargetException, IllegalAccessException, InstantiationException {}
/*      */     
/*      */     abstract Object create(Project param1Project, Object param1Object1, Object param1Object2) throws InvocationTargetException, IllegalAccessException, InstantiationException;
/*      */   }
/*      */   
/*      */   private static class CreateNestedCreator
/*      */     extends NestedCreator
/*      */   {
/*      */     CreateNestedCreator(Method m) {
/* 1415 */       super(m);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Object create(Project project, Object parent, Object ignore) throws InvocationTargetException, IllegalAccessException {
/* 1421 */       return getMethod().invoke(parent, new Object[0]);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class AddNestedCreator
/*      */     extends NestedCreator
/*      */   {
/*      */     static final int ADD = 1;
/*      */     static final int ADD_CONFIGURED = 2;
/*      */     private final Constructor<?> constructor;
/*      */     private final int behavior;
/*      */     
/*      */     AddNestedCreator(Method m, Constructor<?> c, int behavior) {
/* 1435 */       super(m);
/* 1436 */       this.constructor = c;
/* 1437 */       this.behavior = behavior;
/*      */     }
/*      */ 
/*      */     
/*      */     boolean isPolyMorphic() {
/* 1442 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Object create(Project project, Object parent, Object child) throws InvocationTargetException, IllegalAccessException, InstantiationException {
/* 1448 */       if (child == null) {
/*      */ 
/*      */         
/* 1451 */         (new Object[1])[0] = project; child = this.constructor.newInstance(((this.constructor.getParameterTypes()).length == 0) ? new Object[0] : new Object[1]);
/*      */       } 
/* 1453 */       if (child instanceof PreSetDef.PreSetDefinition) {
/* 1454 */         child = ((PreSetDef.PreSetDefinition)child).createObject(project);
/*      */       }
/* 1456 */       if (this.behavior == 1) {
/* 1457 */         istore(parent, child);
/*      */       }
/* 1459 */       return child;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     void store(Object parent, Object child) throws InvocationTargetException, IllegalAccessException, InstantiationException {
/* 1465 */       if (this.behavior == 2) {
/* 1466 */         istore(parent, child);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     private void istore(Object parent, Object child) throws InvocationTargetException, IllegalAccessException {
/* 1472 */       getMethod().invoke(parent, new Object[] { child });
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static abstract class AttributeSetter
/*      */   {
/*      */     private final Method method;
/*      */     
/*      */     private final Class<?> type;
/*      */     
/*      */     protected AttributeSetter(Method m, Class<?> type) {
/* 1484 */       this.method = m;
/* 1485 */       this.type = type;
/*      */     }
/*      */     
/*      */     void setObject(Project p, Object parent, Object value) throws InvocationTargetException, IllegalAccessException, BuildException {
/* 1489 */       if (this.type != null) {
/* 1490 */         Class<?> useType = this.type;
/* 1491 */         if (this.type.isPrimitive()) {
/* 1492 */           if (value == null) {
/* 1493 */             throw new BuildException("Attempt to set primitive " + IntrospectionHelper
/*      */                 
/* 1495 */                 .getPropertyName(this.method.getName(), "set") + " to null on " + parent);
/*      */           }
/*      */           
/* 1498 */           useType = (Class)IntrospectionHelper.PRIMITIVE_TYPE_MAP.get(this.type);
/*      */         } 
/* 1500 */         if (value == null || useType.isInstance(value)) {
/* 1501 */           this.method.invoke(parent, new Object[] { value });
/*      */           return;
/*      */         } 
/*      */       } 
/* 1505 */       set(p, parent, value.toString());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     abstract void set(Project param1Project, Object param1Object, String param1String) throws InvocationTargetException, IllegalAccessException, BuildException;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void clearCache() {
/* 1515 */     HELPERS.clear();
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
/*      */   private NestedCreator createAddTypeCreator(Project project, Object parent, String elementName) throws BuildException {
/* 1529 */     if (this.addTypeMethods.isEmpty()) {
/* 1530 */       return null;
/*      */     }
/* 1532 */     ComponentHelper helper = ComponentHelper.getComponentHelper(project);
/*      */     
/* 1534 */     MethodAndObject restricted = createRestricted(helper, elementName, this.addTypeMethods);
/* 1535 */     MethodAndObject topLevel = createTopLevel(helper, elementName, this.addTypeMethods);
/*      */     
/* 1537 */     if (restricted == null && topLevel == null) {
/* 1538 */       return null;
/*      */     }
/*      */     
/* 1541 */     if (restricted != null && topLevel != null) {
/* 1542 */       throw new BuildException("ambiguous: type and component definitions for " + elementName);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1547 */     MethodAndObject methodAndObject = (restricted == null) ? topLevel : restricted;
/*      */     
/* 1549 */     Object rObject = methodAndObject.object;
/* 1550 */     if (methodAndObject.object instanceof PreSetDef.PreSetDefinition)
/*      */     {
/* 1552 */       rObject = ((PreSetDef.PreSetDefinition)methodAndObject.object).createObject(project);
/*      */     }
/* 1554 */     final Object nestedObject = methodAndObject.object;
/* 1555 */     final Object realObject = rObject;
/*      */     
/* 1557 */     return new NestedCreator(methodAndObject.method)
/*      */       {
/*      */         Object create(Project project, Object parent, Object ignore) throws InvocationTargetException, IllegalAccessException
/*      */         {
/* 1561 */           if (!getMethod().getName().endsWith("Configured")) {
/* 1562 */             getMethod().invoke(parent, new Object[] { this.val$realObject });
/*      */           }
/* 1564 */           return nestedObject;
/*      */         }
/*      */ 
/*      */         
/*      */         Object getRealObject() {
/* 1569 */           return realObject;
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*      */         void store(Object parent, Object child) throws InvocationTargetException, IllegalAccessException, InstantiationException {
/* 1575 */           if (getMethod().getName().endsWith("Configured")) {
/* 1576 */             getMethod().invoke(parent, new Object[] { this.val$realObject });
/*      */           }
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void insertAddTypeMethod(Method method) {
/* 1590 */     Class<?> argClass = method.getParameterTypes()[0];
/* 1591 */     int size = this.addTypeMethods.size();
/* 1592 */     for (int c = 0; c < size; c++) {
/* 1593 */       Method current = this.addTypeMethods.get(c);
/* 1594 */       if (current.getParameterTypes()[0].equals(argClass)) {
/* 1595 */         if ("addConfigured".equals(method.getName()))
/*      */         {
/* 1597 */           this.addTypeMethods.set(c, method);
/*      */         }
/*      */         return;
/*      */       } 
/* 1601 */       if (current.getParameterTypes()[0].isAssignableFrom(argClass)) {
/* 1602 */         this.addTypeMethods.add(c, method);
/*      */         return;
/*      */       } 
/*      */     } 
/* 1606 */     this.addTypeMethods.add(method);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Method findMatchingMethod(Class<?> paramClass, List<Method> methods) {
/* 1617 */     if (paramClass == null) {
/* 1618 */       return null;
/*      */     }
/* 1620 */     Class<?> matchedClass = null;
/* 1621 */     Method matchedMethod = null;
/*      */     
/* 1623 */     for (Method method : methods) {
/* 1624 */       Class<?> methodClass = method.getParameterTypes()[0];
/* 1625 */       if (methodClass.isAssignableFrom(paramClass)) {
/* 1626 */         if (matchedClass == null) {
/* 1627 */           matchedClass = methodClass;
/* 1628 */           matchedMethod = method; continue;
/* 1629 */         }  if (!methodClass.isAssignableFrom(matchedClass)) {
/* 1630 */           throw new BuildException("ambiguous: types " + matchedClass.getName() + " and " + methodClass
/* 1631 */               .getName() + " match " + paramClass.getName());
/*      */         }
/*      */       } 
/*      */     } 
/* 1635 */     return matchedMethod;
/*      */   }
/*      */   
/*      */   private String condenseText(String text) {
/* 1639 */     if (text.length() <= 20) {
/* 1640 */       return text;
/*      */     }
/* 1642 */     int ends = (20 - "...".length()) / 2;
/* 1643 */     return (new StringBuffer(text)).replace(ends, text.length() - ends, "...").toString();
/*      */   }
/*      */   
/*      */   private static class MethodAndObject {
/*      */     private final Method method;
/*      */     private final Object object;
/*      */     
/*      */     public MethodAndObject(Method method, Object object) {
/* 1651 */       this.method = method;
/* 1652 */       this.object = object;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private AntTypeDefinition findRestrictedDefinition(ComponentHelper helper, String componentName, List<Method> methods) {
/* 1661 */     AntTypeDefinition definition = null;
/* 1662 */     Class<?> matchedDefinitionClass = null;
/*      */     
/* 1664 */     List<AntTypeDefinition> definitions = helper.getRestrictedDefinitions(componentName);
/* 1665 */     if (definitions == null) {
/* 1666 */       return null;
/*      */     }
/* 1668 */     synchronized (definitions) {
/* 1669 */       for (AntTypeDefinition d : definitions) {
/* 1670 */         Class<?> exposedClass = d.getExposedClass(helper.getProject());
/* 1671 */         if (exposedClass == null) {
/*      */           continue;
/*      */         }
/* 1674 */         Method method = findMatchingMethod(exposedClass, methods);
/* 1675 */         if (method == null) {
/*      */           continue;
/*      */         }
/* 1678 */         if (matchedDefinitionClass != null) {
/* 1679 */           throw new BuildException("ambiguous: restricted definitions for " + componentName + " " + matchedDefinitionClass + " and " + exposedClass);
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 1684 */         matchedDefinitionClass = exposedClass;
/* 1685 */         definition = d;
/*      */       } 
/*      */     } 
/* 1688 */     return definition;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private MethodAndObject createRestricted(ComponentHelper helper, String elementName, List<Method> addTypeMethods) {
/* 1694 */     Project project = helper.getProject();
/*      */ 
/*      */     
/* 1697 */     AntTypeDefinition restrictedDefinition = findRestrictedDefinition(helper, elementName, addTypeMethods);
/*      */     
/* 1699 */     if (restrictedDefinition == null) {
/* 1700 */       return null;
/*      */     }
/*      */     
/* 1703 */     Method addMethod = findMatchingMethod(restrictedDefinition
/* 1704 */         .getExposedClass(project), addTypeMethods);
/* 1705 */     if (addMethod == null) {
/* 1706 */       throw new BuildException("Ant Internal Error - contract mismatch for " + elementName);
/*      */     }
/*      */     
/* 1709 */     Object addedObject = restrictedDefinition.create(project);
/* 1710 */     if (addedObject == null) {
/* 1711 */       throw new BuildException("Failed to create object " + elementName + " of type " + restrictedDefinition
/*      */           
/* 1713 */           .getTypeClass(project));
/*      */     }
/* 1715 */     return new MethodAndObject(addMethod, addedObject);
/*      */   }
/*      */ 
/*      */   
/*      */   private MethodAndObject createTopLevel(ComponentHelper helper, String elementName, List<Method> methods) {
/* 1720 */     Class<?> clazz = helper.getComponentClass(elementName);
/* 1721 */     if (clazz == null) {
/* 1722 */       return null;
/*      */     }
/* 1724 */     Method addMethod = findMatchingMethod(clazz, this.addTypeMethods);
/* 1725 */     if (addMethod == null) {
/* 1726 */       return null;
/*      */     }
/* 1728 */     Object addedObject = helper.createComponent(elementName);
/* 1729 */     return new MethodAndObject(addMethod, addedObject);
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/IntrospectionHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */