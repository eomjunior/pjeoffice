/*     */ package com.yworks.yshrink.model;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassDescriptor
/*     */   extends AbstractDescriptor
/*     */ {
/*     */   private String name;
/*     */   private String superName;
/*     */   private String[] interfaces;
/*     */   private String enclosingClass;
/*     */   private AbstractMap.SimpleEntry<Object, Object> enclosingMethod;
/*     */   private boolean hasNestMembers = false;
/*     */   private Map<AbstractMap.SimpleEntry<Object, Object>, MethodDescriptor> methods;
/*     */   private Map<String, FieldDescriptor> fields;
/*     */   private Set<String> allInterfaces;
/*     */   private Set<String> allAncestors;
/*     */   private Object newNode;
/*     */   private boolean hasExternalAncestors = false;
/*  32 */   private Set<String> attributesToKeep = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ClassDescriptor(String name, int access, Object newNode, File sourceJar) {
/*  44 */     super(access, sourceJar);
/*     */     
/*  46 */     this.name = name;
/*  47 */     this.newNode = newNode;
/*  48 */     this.methods = new HashMap<>();
/*  49 */     this.fields = new HashMap<>();
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
/*     */   protected ClassDescriptor(String name, String superName, String[] interfaces, int access, Object newNode, File sourceJar) {
/*  64 */     this(name, access, newNode, sourceJar);
/*  65 */     this.superName = superName;
/*  66 */     this.interfaces = interfaces;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnclosingClass(String enclosingClass) {
/*  75 */     this.enclosingClass = enclosingClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnclosingMethod(String methodName, String methodDesc) {
/*  85 */     this.enclosingMethod = new AbstractMap.SimpleEntry<>(methodName, methodDesc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEnclosingClass() {
/*  94 */     return this.enclosingClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractMap.SimpleEntry<Object, Object> getEnclosingMethod() {
/* 103 */     return this.enclosingMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMethod(MethodDescriptor method) {
/* 112 */     this.methods.put(new AbstractMap.SimpleEntry<>(method.getName(), method.getDesc()), method);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addField(FieldDescriptor field) {
/* 121 */     this.fields.put(field.getName(), field);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHasExternalAncestors(boolean hasExternalAncestors) {
/* 130 */     this.hasExternalAncestors = hasExternalAncestors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 139 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getShortName() {
/* 148 */     int i = this.name.lastIndexOf('/');
/* 149 */     if (i != -1) {
/* 150 */       return this.name.substring(i + 1, this.name.length());
/*     */     }
/* 152 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSuperName() {
/* 162 */     return this.superName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSuperName(String superName) {
/* 171 */     this.superName = superName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInterfaces(String[] interfaces) {
/* 180 */     this.interfaces = interfaces;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getInterfaces() {
/* 189 */     return this.interfaces;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodDescriptor getMethod(String name, String desc) {
/* 200 */     return this.methods.get(new AbstractMap.SimpleEntry<>(name, desc));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodDescriptor getMethod(AbstractMap.SimpleEntry<Object, Object> method) {
/* 210 */     return this.methods.get(method);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldDescriptor getField(String name) {
/* 220 */     return this.fields.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<MethodDescriptor> getMethods() {
/* 229 */     return this.methods.values();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInterface() {
/* 238 */     return ((this.access & 0x200) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnum() {
/* 247 */     return ((this.access & 0x4000) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAnnotation() {
/* 256 */     return ((this.access & 0x2000) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInnerClass() {
/* 265 */     return (this.enclosingClass != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean implementsMethod(String methodName, String methodDesc) {
/* 276 */     return this.methods.containsKey(new AbstractMap.SimpleEntry<>(methodName, methodDesc));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean declaresField(String fieldName) {
/* 286 */     return this.fields.containsKey(fieldName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<FieldDescriptor> getFields() {
/* 295 */     return this.fields.values();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getAllImplementedInterfaces(Model model) {
/* 305 */     if (null != this.allInterfaces) {
/* 306 */       return this.allInterfaces;
/*     */     }
/* 308 */     this.allInterfaces = new HashSet<>(3);
/* 309 */     model.getAllImplementedInterfaces(getName(), this.allInterfaces);
/*     */     
/* 311 */     return this.allInterfaces;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getAllAncestorClasses(Model model) {
/* 321 */     if (null != this.allAncestors) {
/* 322 */       return this.allAncestors;
/*     */     }
/* 324 */     this.allAncestors = new HashSet<>(3);
/* 325 */     model.getAllAncestorClasses(getName(), this.allAncestors);
/*     */     
/* 327 */     return this.allAncestors;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 331 */     return "ClassDescriptor{name='" + this.name + '\'' + ", enclosingClass='" + this.enclosingClass + '\'' + ", enclosingMethod=" + this.enclosingMethod + '}';
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
/*     */   public Object getNewNode() {
/* 344 */     return this.newNode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRetainAttribute(String attr) {
/* 353 */     this.attributesToKeep.add(attr);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getRetainAttribute(String attr) {
/* 363 */     return this.attributesToKeep.contains(attr);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getHasNestMembers() {
/* 372 */     return this.hasNestMembers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHasNestMembers(boolean nestMembers) {
/* 381 */     this.hasNestMembers = nestMembers;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/model/ClassDescriptor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */