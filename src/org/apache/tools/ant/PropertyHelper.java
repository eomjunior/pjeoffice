/*      */ package org.apache.tools.ant;
/*      */ 
/*      */ import java.text.ParsePosition;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashSet;
/*      */ import java.util.Hashtable;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.Vector;
/*      */ import org.apache.tools.ant.property.GetProperty;
/*      */ import org.apache.tools.ant.property.ParseNextProperty;
/*      */ import org.apache.tools.ant.property.ParseProperties;
/*      */ import org.apache.tools.ant.property.PropertyExpander;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class PropertyHelper
/*      */   implements GetProperty
/*      */ {
/*  184 */   private static final PropertyEvaluator TO_STRING = new PropertyEvaluator() {
/*  185 */       private final String PREFIX = "toString:";
/*  186 */       private final int PREFIX_LEN = "toString:".length();
/*      */       
/*      */       public Object evaluate(String property, PropertyHelper propertyHelper) {
/*  189 */         Object o = null;
/*  190 */         if (property.startsWith("toString:") && propertyHelper.getProject() != null) {
/*  191 */           o = propertyHelper.getProject().getReference(property.substring(this.PREFIX_LEN));
/*      */         }
/*  193 */         return (o == null) ? null : o.toString();
/*      */       }
/*      */     };
/*      */   static {
/*  197 */     DEFAULT_EXPANDER = ((s, pos, notUsed) -> {
/*      */         int index = pos.getIndex();
/*      */         
/*      */         if (s.length() - index >= 3 && '$' == s.charAt(index) && '{' == s.charAt(index + 1)) {
/*      */           int start = index + 2;
/*      */           
/*      */           int end = s.indexOf('}', start);
/*      */           
/*      */           if (end < 0) {
/*      */             throw new BuildException("Syntax error in property: " + s.substring(index));
/*      */           }
/*      */           
/*      */           pos.setIndex(end + 1);
/*      */           
/*      */           return (start == end) ? "" : s.substring(start, end);
/*      */         } 
/*      */         
/*      */         return null;
/*      */       });
/*      */     
/*  217 */     SKIP_DOUBLE_DOLLAR = ((s, pos, notUsed) -> {
/*      */         int index = pos.getIndex();
/*      */         if (s.length() - index >= 2) {
/*      */           if ('$' == s.charAt(index) && '$' == s.charAt(++index)) {
/*      */             pos.setIndex(index);
/*      */           }
/*      */         }
/*      */         return null;
/*      */       });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final PropertyExpander DEFAULT_EXPANDER;
/*      */ 
/*      */   
/*      */   private static final PropertyExpander SKIP_DOUBLE_DOLLAR;
/*      */ 
/*      */   
/*  237 */   private static final PropertyEvaluator FROM_REF = new PropertyEvaluator() {
/*  238 */       private final String PREFIX = "ant.refid:";
/*  239 */       private final int PREFIX_LEN = "ant.refid:".length();
/*      */       
/*      */       public Object evaluate(String prop, PropertyHelper helper) {
/*  242 */         return (prop.startsWith("ant.refid:") && helper.getProject() != null) ? 
/*  243 */           helper.getProject().getReference(prop.substring(this.PREFIX_LEN)) : 
/*  244 */           null;
/*      */       }
/*      */     };
/*      */   
/*      */   private Project project;
/*      */   private PropertyHelper next;
/*  250 */   private final Hashtable<Class<? extends Delegate>, List<Delegate>> delegates = new Hashtable<>();
/*      */ 
/*      */   
/*  253 */   private final Hashtable<String, Object> properties = new Hashtable<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  260 */   private final Hashtable<String, Object> userProperties = new Hashtable<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  267 */   private final Hashtable<String, Object> inheritedProperties = new Hashtable<>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected PropertyHelper() {
/*  273 */     add(FROM_REF);
/*  274 */     add(TO_STRING);
/*  275 */     add((Delegate)SKIP_DOUBLE_DOLLAR);
/*  276 */     add((Delegate)DEFAULT_EXPANDER);
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
/*      */   public static Object getProperty(Project project, String name) {
/*  294 */     return getPropertyHelper(project)
/*  295 */       .getProperty(name);
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
/*      */   public static void setProperty(Project project, String name, Object value) {
/*  307 */     getPropertyHelper(project)
/*  308 */       .setProperty(name, value, true);
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
/*      */   public static void setNewProperty(Project project, String name, Object value) {
/*  321 */     getPropertyHelper(project)
/*  322 */       .setNewProperty(name, value);
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
/*      */   public void setProject(Project p) {
/*  335 */     this.project = p;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Project getProject() {
/*  343 */     return this.project;
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
/*      */   @Deprecated
/*      */   public void setNext(PropertyHelper next) {
/*  364 */     this.next = next;
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
/*      */   @Deprecated
/*      */   public PropertyHelper getNext() {
/*  380 */     return this.next;
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
/*      */   public static synchronized PropertyHelper getPropertyHelper(Project project) {
/*  394 */     PropertyHelper helper = null;
/*  395 */     if (project != null) {
/*  396 */       helper = project.<PropertyHelper>getReference("ant.PropertyHelper");
/*      */     }
/*  398 */     if (helper != null) {
/*  399 */       return helper;
/*      */     }
/*      */     
/*  402 */     helper = new PropertyHelper();
/*  403 */     helper.setProject(project);
/*      */     
/*  405 */     if (project != null) {
/*  406 */       project.addReference("ant.PropertyHelper", helper);
/*      */     }
/*      */     
/*  409 */     return helper;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<PropertyExpander> getExpanders() {
/*  418 */     return getDelegates(PropertyExpander.class);
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
/*      */   @Deprecated
/*      */   public boolean setPropertyHook(String ns, String name, Object value, boolean inherited, boolean user, boolean isNew) {
/*  453 */     if (getNext() != null)
/*      */     {
/*  455 */       return getNext().setPropertyHook(ns, name, value, inherited, user, isNew);
/*      */     }
/*  457 */     return false;
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
/*      */   @Deprecated
/*      */   public Object getPropertyHook(String ns, String name, boolean user) {
/*  475 */     if (getNext() != null) {
/*  476 */       Object o = getNext().getPropertyHook(ns, name, user);
/*  477 */       if (o != null) {
/*  478 */         return o;
/*      */       }
/*      */     } 
/*      */     
/*  482 */     if (this.project != null && name.startsWith("toString:")) {
/*  483 */       name = name.substring("toString:".length());
/*  484 */       Object v = this.project.getReference(name);
/*  485 */       return (v == null) ? null : v.toString();
/*      */     } 
/*  487 */     return null;
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
/*      */   @Deprecated
/*      */   public void parsePropertyString(String value, Vector<String> fragments, Vector<String> propertyRefs) throws BuildException {
/*  524 */     parsePropertyStringDefault(value, fragments, propertyRefs);
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
/*      */   public String replaceProperties(String ns, String value, Hashtable<String, Object> keys) throws BuildException {
/*  550 */     return replaceProperties(value);
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
/*      */   public String replaceProperties(String value) throws BuildException {
/*  568 */     Object o = parseProperties(value);
/*  569 */     return (o == null || o instanceof String) ? (String)o : o.toString();
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
/*      */   public Object parseProperties(String value) throws BuildException {
/*  588 */     return (new ParseProperties(getProject(), getExpanders(), this))
/*  589 */       .parseProperties(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean containsProperties(String value) {
/*  598 */     return (new ParseProperties(getProject(), getExpanders(), this))
/*  599 */       .containsProperties(value);
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
/*      */   @Deprecated
/*      */   public boolean setProperty(String ns, String name, Object value, boolean verbose) {
/*  623 */     return setProperty(name, value, verbose);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setProperty(String name, Object value, boolean verbose) {
/*  634 */     for (PropertySetter setter : getDelegates(PropertySetter.class)) {
/*  635 */       if (setter.set(name, value, this)) {
/*  636 */         return true;
/*      */       }
/*      */     } 
/*  639 */     synchronized (this) {
/*      */       
/*  641 */       if (this.userProperties.containsKey(name)) {
/*  642 */         if (this.project != null && verbose) {
/*  643 */           this.project.log("Override ignored for user property \"" + name + "\"", 3);
/*      */         }
/*      */         
/*  646 */         return false;
/*      */       } 
/*  648 */       if (this.project != null && verbose) {
/*  649 */         if (this.properties.containsKey(name)) {
/*  650 */           this.project.log("Overriding previous definition of property \"" + name + "\"", 3);
/*      */         }
/*      */         
/*  653 */         this.project.log("Setting project property: " + name + " -> " + value, 4);
/*      */       } 
/*      */       
/*  656 */       if (name != null && value != null) {
/*  657 */         this.properties.put(name, value);
/*      */       }
/*  659 */       return true;
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
/*      */   @Deprecated
/*      */   public void setNewProperty(String ns, String name, Object value) {
/*  681 */     setNewProperty(name, value);
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
/*      */   public void setNewProperty(String name, Object value) {
/*  696 */     for (PropertySetter setter : getDelegates(PropertySetter.class)) {
/*  697 */       if (setter.setNew(name, value, this)) {
/*      */         return;
/*      */       }
/*      */     } 
/*  701 */     synchronized (this) {
/*  702 */       if (this.project != null && this.properties.containsKey(name)) {
/*  703 */         this.project.log("Override ignored for property \"" + name + "\"", 3);
/*      */         
/*      */         return;
/*      */       } 
/*  707 */       if (this.project != null) {
/*  708 */         this.project.log("Setting project property: " + name + " -> " + value, 4);
/*      */       }
/*      */       
/*  711 */       if (name != null && value != null) {
/*  712 */         this.properties.put(name, value);
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
/*      */   @Deprecated
/*      */   public void setUserProperty(String ns, String name, Object value) {
/*  733 */     setUserProperty(name, value);
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
/*      */   public void setUserProperty(String name, Object value) {
/*  748 */     if (this.project != null) {
/*  749 */       this.project.log("Setting ro project property: " + name + " -> " + value, 4);
/*      */     }
/*      */     
/*  752 */     synchronized (this) {
/*  753 */       this.userProperties.put(name, value);
/*  754 */       this.properties.put(name, value);
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
/*      */   @Deprecated
/*      */   public void setInheritedProperty(String ns, String name, Object value) {
/*  776 */     setInheritedProperty(name, value);
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
/*      */   public void setInheritedProperty(String name, Object value) {
/*  793 */     if (this.project != null) {
/*  794 */       this.project.log("Setting ro project property: " + name + " -> " + value, 4);
/*      */     }
/*      */ 
/*      */     
/*  798 */     synchronized (this) {
/*  799 */       this.inheritedProperties.put(name, value);
/*  800 */       this.userProperties.put(name, value);
/*  801 */       this.properties.put(name, value);
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
/*      */   @Deprecated
/*      */   public Object getProperty(String ns, String name) {
/*  823 */     return getProperty(name);
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
/*      */   public Object getProperty(String name) {
/*  844 */     if (name == null) {
/*  845 */       return null;
/*      */     }
/*  847 */     for (PropertyEvaluator evaluator : getDelegates(PropertyEvaluator.class)) {
/*  848 */       Object o = evaluator.evaluate(name, this);
/*  849 */       if (o == null) {
/*      */         continue;
/*      */       }
/*  852 */       return (o instanceof org.apache.tools.ant.property.NullReturn) ? null : o;
/*      */     } 
/*  854 */     return this.properties.get(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<String> getPropertyNames() {
/*  863 */     Set<String> names = new HashSet<>(this.properties.keySet());
/*  864 */     getDelegates(PropertyEnumerator.class)
/*  865 */       .forEach(e -> names.addAll(e.getPropertyNames()));
/*  866 */     return Collections.unmodifiableSet(names);
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
/*      */   @Deprecated
/*      */   public Object getUserProperty(String ns, String name) {
/*  884 */     return getUserProperty(name);
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
/*      */   public Object getUserProperty(String name) {
/*  899 */     if (name == null) {
/*  900 */       return null;
/*      */     }
/*  902 */     return this.userProperties.get(name);
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
/*      */   public Hashtable<String, Object> getProperties() {
/*  920 */     synchronized (this.properties) {
/*  921 */       return new Hashtable<>(this.properties);
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
/*      */   public Hashtable<String, Object> getUserProperties() {
/*  937 */     synchronized (this.userProperties) {
/*  938 */       return new Hashtable<>(this.userProperties);
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
/*      */   public Hashtable<String, Object> getInheritedProperties() {
/*  952 */     synchronized (this.inheritedProperties) {
/*  953 */       return new Hashtable<>(this.inheritedProperties);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Hashtable<String, Object> getInternalProperties() {
/*  962 */     return this.properties;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Hashtable<String, Object> getInternalUserProperties() {
/*  971 */     return this.userProperties;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Hashtable<String, Object> getInternalInheritedProperties() {
/*  980 */     return this.inheritedProperties;
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
/*      */   public void copyInheritedProperties(Project other) {
/* 1000 */     synchronized (this.inheritedProperties) {
/* 1001 */       for (Map.Entry<String, Object> entry : this.inheritedProperties.entrySet()) {
/* 1002 */         String arg = entry.getKey();
/* 1003 */         if (other.getUserProperty(arg) == null) {
/* 1004 */           other.setInheritedProperty(arg, entry.getValue().toString());
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
/*      */   public void copyUserProperties(Project other) {
/* 1027 */     synchronized (this.userProperties) {
/* 1028 */       for (Map.Entry<String, Object> entry : this.userProperties.entrySet()) {
/* 1029 */         String arg = entry.getKey();
/* 1030 */         if (!this.inheritedProperties.containsKey(arg)) {
/* 1031 */           other.setUserProperty(arg, entry.getValue().toString());
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
/*      */   static void parsePropertyStringDefault(String value, Vector<String> fragments, Vector<String> propertyRefs) throws BuildException {
/* 1048 */     int prev = 0;
/*      */     
/*      */     int pos;
/* 1051 */     while ((pos = value.indexOf('$', prev)) >= 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1057 */       if (pos > 0) {
/* 1058 */         fragments.addElement(value.substring(prev, pos));
/*      */       }
/*      */ 
/*      */       
/* 1062 */       if (pos == value.length() - 1) {
/* 1063 */         fragments.addElement("$");
/* 1064 */         prev = pos + 1; continue;
/* 1065 */       }  if (value.charAt(pos + 1) != '{') {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1072 */         if (value.charAt(pos + 1) == '$') {
/*      */           
/* 1074 */           fragments.addElement("$");
/*      */         } else {
/*      */           
/* 1077 */           fragments.addElement(value.substring(pos, pos + 2));
/*      */         } 
/* 1079 */         prev = pos + 2;
/*      */         continue;
/*      */       } 
/* 1082 */       int endName = value.indexOf('}', pos);
/* 1083 */       if (endName < 0) {
/* 1084 */         throw new BuildException("Syntax error in property: " + value);
/*      */       }
/* 1086 */       String propertyName = value.substring(pos + 2, endName);
/* 1087 */       fragments.addElement(null);
/* 1088 */       propertyRefs.addElement(propertyName);
/* 1089 */       prev = endName + 1;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1094 */     if (prev < value.length()) {
/* 1095 */       fragments.addElement(value.substring(prev));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void add(Delegate delegate) {
/* 1106 */     synchronized (this.delegates) {
/* 1107 */       for (Class<? extends Delegate> key : getDelegateInterfaces(delegate)) {
/* 1108 */         List<Delegate> list = this.delegates.get(key);
/* 1109 */         if (list == null) {
/* 1110 */           list = new ArrayList<>();
/*      */         } else {
/*      */           
/* 1113 */           list = new ArrayList<>(list);
/* 1114 */           list.remove(delegate);
/*      */         } 
/* 1116 */         list.add(0, delegate);
/* 1117 */         this.delegates.put(key, Collections.unmodifiableList(list));
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
/*      */   protected <D extends Delegate> List<D> getDelegates(Class<D> type) {
/* 1133 */     List<D> result = (List<D>)this.delegates.get(type);
/* 1134 */     return (result == null) ? Collections.<D>emptyList() : result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static Set<Class<? extends Delegate>> getDelegateInterfaces(Delegate d) {
/* 1145 */     HashSet<Class<? extends Delegate>> result = new HashSet<>();
/* 1146 */     Class<?> c = d.getClass();
/* 1147 */     while (c != null) {
/* 1148 */       for (Class<?> ifc : c.getInterfaces()) {
/* 1149 */         if (Delegate.class.isAssignableFrom(ifc)) {
/* 1150 */           result.add(ifc);
/*      */         }
/*      */       } 
/* 1153 */       c = c.getSuperclass();
/*      */     } 
/* 1155 */     result.remove(Delegate.class);
/* 1156 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Boolean toBoolean(Object value) {
/* 1167 */     if (value instanceof Boolean) {
/* 1168 */       return (Boolean)value;
/*      */     }
/* 1170 */     if (value instanceof String) {
/* 1171 */       String s = (String)value;
/* 1172 */       if (Project.toBoolean(s)) {
/* 1173 */         return Boolean.TRUE;
/*      */       }
/* 1175 */       if ("off".equalsIgnoreCase(s) || "false"
/* 1176 */         .equalsIgnoreCase(s) || "no"
/* 1177 */         .equalsIgnoreCase(s)) {
/* 1178 */         return Boolean.FALSE;
/*      */       }
/*      */     } 
/* 1181 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean nullOrEmpty(Object value) {
/* 1192 */     return (value == null || "".equals(value));
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
/*      */   private boolean evalAsBooleanOrPropertyName(Object value) {
/* 1204 */     Boolean b = toBoolean(value);
/* 1205 */     if (b != null) {
/* 1206 */       return b.booleanValue();
/*      */     }
/* 1208 */     return (getProperty(String.valueOf(value)) != null);
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
/*      */   public boolean testIfCondition(Object value) {
/* 1220 */     return (nullOrEmpty(value) || evalAsBooleanOrPropertyName(value));
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
/*      */   public boolean testUnlessCondition(Object value) {
/* 1232 */     return (nullOrEmpty(value) || !evalAsBooleanOrPropertyName(value));
/*      */   }
/*      */   
/*      */   public static interface Delegate {}
/*      */   
/*      */   public static interface PropertyEvaluator extends Delegate {
/*      */     Object evaluate(String param1String, PropertyHelper param1PropertyHelper);
/*      */   }
/*      */   
/*      */   public static interface PropertySetter extends Delegate {
/*      */     boolean setNew(String param1String, Object param1Object, PropertyHelper param1PropertyHelper);
/*      */     
/*      */     boolean set(String param1String, Object param1Object, PropertyHelper param1PropertyHelper);
/*      */   }
/*      */   
/*      */   public static interface PropertyEnumerator extends Delegate {
/*      */     Set<String> getPropertyNames();
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/PropertyHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */