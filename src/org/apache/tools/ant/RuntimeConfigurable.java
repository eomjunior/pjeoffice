/*     */ package org.apache.tools.ant;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.tools.ant.attribute.EnableAttribute;
/*     */ import org.apache.tools.ant.taskdefs.MacroDef;
/*     */ import org.apache.tools.ant.taskdefs.MacroInstance;
/*     */ import org.xml.sax.AttributeList;
/*     */ import org.xml.sax.helpers.AttributeListImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RuntimeConfigurable
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  47 */   private String elementTag = null;
/*     */ 
/*     */ 
/*     */   
/*  51 */   private List<RuntimeConfigurable> children = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   private transient Object wrappedObject = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   private transient AttributeList attributes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private transient boolean namespacedAttribute = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   private LinkedHashMap<String, Object> attributeMap = null;
/*     */ 
/*     */   
/*  81 */   private StringBuffer characters = null;
/*     */ 
/*     */   
/*     */   private boolean proxyConfigured = false;
/*     */ 
/*     */   
/*  87 */   private String polyType = null;
/*     */ 
/*     */   
/*  90 */   private String id = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RuntimeConfigurable(Object proxy, String elementTag) {
/*  99 */     setProxy(proxy);
/* 100 */     setElementTag(elementTag);
/*     */     
/* 102 */     if (proxy instanceof Task) {
/* 103 */       ((Task)proxy).setRuntimeConfigurableWrapper(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setProxy(Object proxy) {
/* 113 */     this.wrappedObject = proxy;
/* 114 */     this.proxyConfigured = false;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class EnableAttributeConsumer
/*     */   {
/*     */     private EnableAttributeConsumer() {}
/*     */ 
/*     */     
/*     */     public void add(EnableAttribute b) {}
/*     */   }
/*     */ 
/*     */   
/*     */   private static class AttributeComponentInformation
/*     */   {
/*     */     String componentName;
/*     */     
/*     */     boolean restricted;
/*     */     
/*     */     private AttributeComponentInformation(String componentName, boolean restricted) {
/* 134 */       this.componentName = componentName;
/* 135 */       this.restricted = restricted;
/*     */     }
/*     */     
/*     */     public String getComponentName() {
/* 139 */       return this.componentName;
/*     */     }
/*     */     
/*     */     public boolean isRestricted() {
/* 143 */       return this.restricted;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private AttributeComponentInformation isRestrictedAttribute(String name, ComponentHelper componentHelper) {
/* 154 */     if (!name.contains(":")) {
/* 155 */       return new AttributeComponentInformation(null, false);
/*     */     }
/* 157 */     String componentName = attrToComponent(name);
/* 158 */     String ns = ProjectHelper.extractUriFromComponentName(componentName);
/* 159 */     if (componentHelper.getRestrictedDefinitions(
/* 160 */         ProjectHelper.nsToComponentName(ns)) == null) {
/* 161 */       return new AttributeComponentInformation(null, false);
/*     */     }
/* 163 */     return new AttributeComponentInformation(componentName, true);
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
/*     */   public boolean isEnabled(UnknownElement owner) {
/* 178 */     if (!this.namespacedAttribute) {
/* 179 */       return true;
/*     */     }
/*     */     
/* 182 */     ComponentHelper componentHelper = ComponentHelper.getComponentHelper(owner.getProject());
/*     */     
/* 184 */     IntrospectionHelper ih = IntrospectionHelper.getHelper(owner.getProject(), EnableAttributeConsumer.class);
/*     */     
/* 186 */     for (Map.Entry<String, Object> entry : this.attributeMap.entrySet()) {
/*     */       
/* 188 */       AttributeComponentInformation attributeComponentInformation = isRestrictedAttribute(entry.getKey(), componentHelper);
/* 189 */       if (!attributeComponentInformation.isRestricted()) {
/*     */         continue;
/*     */       }
/* 192 */       String value = (String)entry.getValue();
/* 193 */       EnableAttribute enable = null;
/*     */       
/*     */       try {
/* 196 */         enable = (EnableAttribute)ih.createElement(owner
/* 197 */             .getProject(), new EnableAttributeConsumer(), attributeComponentInformation
/* 198 */             .getComponentName());
/* 199 */       } catch (BuildException ex) {
/* 200 */         throw new BuildException("Unsupported attribute " + attributeComponentInformation
/* 201 */             .getComponentName());
/*     */       } 
/* 203 */       if (enable == null) {
/*     */         continue;
/*     */       }
/* 206 */       value = owner.getProject().replaceProperties(value);
/* 207 */       if (!enable.isEnabled(owner, value)) {
/* 208 */         return false;
/*     */       }
/*     */     } 
/* 211 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private String attrToComponent(String a) {
/* 216 */     int p1 = a.lastIndexOf(':');
/* 217 */     int p2 = a.lastIndexOf(':', p1 - 1);
/* 218 */     return a.substring(0, p2) + a.substring(p1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   synchronized void setCreator(IntrospectionHelper.Creator creator) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Object getProxy() {
/* 237 */     return this.wrappedObject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized String getId() {
/* 245 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized String getPolyType() {
/* 253 */     return this.polyType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setPolyType(String polyType) {
/* 261 */     this.polyType = polyType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public synchronized void setAttributes(AttributeList attributes) {
/* 273 */     this.attributes = new AttributeListImpl(attributes);
/* 274 */     for (int i = 0; i < attributes.getLength(); i++) {
/* 275 */       setAttribute(attributes.getName(i), attributes.getValue(i));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setAttribute(String name, String value) {
/* 286 */     if (name.contains(":")) {
/* 287 */       this.namespacedAttribute = true;
/*     */     }
/* 289 */     setAttribute(name, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setAttribute(String name, Object value) {
/* 300 */     if (name.equalsIgnoreCase("ant-type")) {
/* 301 */       this.polyType = (value == null) ? null : value.toString();
/*     */     } else {
/* 303 */       if (this.attributeMap == null) {
/* 304 */         this.attributeMap = new LinkedHashMap<>();
/*     */       }
/* 306 */       if ("refid".equalsIgnoreCase(name) && !this.attributeMap.isEmpty()) {
/* 307 */         LinkedHashMap<String, Object> newAttributeMap = new LinkedHashMap<>();
/* 308 */         newAttributeMap.put(name, value);
/* 309 */         newAttributeMap.putAll(this.attributeMap);
/* 310 */         this.attributeMap = newAttributeMap;
/*     */       } else {
/* 312 */         this.attributeMap.put(name, value);
/*     */       } 
/* 314 */       if ("id".equals(name)) {
/* 315 */         this.id = (value == null) ? null : value.toString();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void removeAttribute(String name) {
/* 325 */     this.attributeMap.remove(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Hashtable<String, Object> getAttributeMap() {
/* 335 */     return new Hashtable<>((this.attributeMap == null) ? Collections.emptyMap() : this.attributeMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public synchronized AttributeList getAttributes() {
/* 347 */     return this.attributes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void addChild(RuntimeConfigurable child) {
/* 357 */     this.children = (this.children == null) ? new ArrayList<>() : this.children;
/* 358 */     this.children.add(child);
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
/*     */   synchronized RuntimeConfigurable getChild(int index) {
/* 370 */     return this.children.get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Enumeration<RuntimeConfigurable> getChildren() {
/* 379 */     return (this.children == null) ? Collections.<RuntimeConfigurable>emptyEnumeration() : 
/* 380 */       Collections.<RuntimeConfigurable>enumeration(this.children);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void addText(String data) {
/* 390 */     if (data.isEmpty()) {
/*     */       return;
/*     */     }
/* 393 */     this
/* 394 */       .characters = (this.characters == null) ? new StringBuffer(data) : this.characters.append(data);
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
/*     */   public synchronized void addText(char[] buf, int start, int count) {
/* 407 */     if (count == 0) {
/*     */       return;
/*     */     }
/* 410 */     this
/* 411 */       .characters = ((this.characters == null) ? new StringBuffer(count) : this.characters).append(buf, start, count);
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
/*     */   public synchronized StringBuffer getText() {
/* 423 */     return (this.characters == null) ? new StringBuffer(0) : this.characters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setElementTag(String elementTag) {
/* 431 */     this.elementTag = elementTag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized String getElementTag() {
/* 441 */     return this.elementTag;
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
/*     */   public void maybeConfigure(Project p) throws BuildException {
/* 463 */     maybeConfigure(p, true);
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
/*     */   public synchronized void maybeConfigure(Project p, boolean configureChildren) throws BuildException {
/* 487 */     if (this.proxyConfigured) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 493 */     Object target = (this.wrappedObject instanceof TypeAdapter) ? ((TypeAdapter)this.wrappedObject).getProxy() : this.wrappedObject;
/*     */ 
/*     */     
/* 496 */     IntrospectionHelper ih = IntrospectionHelper.getHelper(p, target.getClass());
/* 497 */     ComponentHelper componentHelper = ComponentHelper.getComponentHelper(p);
/* 498 */     if (this.attributeMap != null) {
/* 499 */       for (Map.Entry<String, Object> entry : this.attributeMap.entrySet()) {
/* 500 */         Object object1; String name = entry.getKey();
/*     */         
/* 502 */         AttributeComponentInformation attributeComponentInformation = isRestrictedAttribute(name, componentHelper);
/* 503 */         if (attributeComponentInformation.isRestricted()) {
/*     */           continue;
/*     */         }
/* 506 */         Object value = entry.getValue();
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 511 */         if (value instanceof Evaluable) {
/* 512 */           object1 = ((Evaluable)value).eval();
/*     */         } else {
/* 514 */           object1 = PropertyHelper.getPropertyHelper(p).parseProperties(value.toString());
/*     */         } 
/* 516 */         if (target instanceof MacroInstance) {
/* 517 */           for (MacroDef.Attribute attr : ((MacroInstance)target).getMacroDef().getAttributes()) {
/* 518 */             if (attr.getName().equals(name)) {
/* 519 */               if (!attr.isDoubleExpanding()) {
/* 520 */                 object1 = value;
/*     */               }
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         }
/*     */         try {
/* 527 */           ih.setAttribute(p, target, name, object1);
/* 528 */         } catch (UnsupportedAttributeException be) {
/*     */           
/* 530 */           if (!"id".equals(name)) {
/* 531 */             if (getElementTag() == null) {
/* 532 */               throw be;
/*     */             }
/* 534 */             throw new BuildException(
/* 535 */                 getElementTag() + " doesn't support the \"" + be
/* 536 */                 .getAttribute() + "\" attribute", be);
/*     */           }
/*     */         
/* 539 */         } catch (BuildException be) {
/* 540 */           if (!"id".equals(name)) {
/* 541 */             throw be;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 550 */     if (this.characters != null) {
/* 551 */       ProjectHelper.addText(p, this.wrappedObject, this.characters.substring(0));
/*     */     }
/*     */     
/* 554 */     if (this.id != null) {
/* 555 */       p.addReference(this.id, this.wrappedObject);
/*     */     }
/* 557 */     this.proxyConfigured = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reconfigure(Project p) {
/* 566 */     this.proxyConfigured = false;
/* 567 */     maybeConfigure(p);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void applyPreSet(RuntimeConfigurable r) {
/* 578 */     if (r.attributeMap != null) {
/* 579 */       for (String name : r.attributeMap.keySet()) {
/* 580 */         if (this.attributeMap == null || this.attributeMap.get(name) == null) {
/* 581 */           setAttribute(name, (String)r.attributeMap.get(name));
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 587 */     this.polyType = (this.polyType == null) ? r.polyType : this.polyType;
/*     */ 
/*     */     
/* 590 */     if (r.children != null) {
/* 591 */       List<RuntimeConfigurable> newChildren = new ArrayList<>(r.children);
/* 592 */       if (this.children != null) {
/* 593 */         newChildren.addAll(this.children);
/*     */       }
/* 595 */       this.children = newChildren;
/*     */     } 
/*     */ 
/*     */     
/* 599 */     if (r.characters != null && (
/* 600 */       this.characters == null || this.characters
/* 601 */       .toString().trim().isEmpty()))
/* 602 */       this.characters = new StringBuffer(r.characters.toString()); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/RuntimeConfigurable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */