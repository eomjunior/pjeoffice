/*     */ package org.apache.tools.ant.property;
/*     */ 
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.Collections;
/*     */ import java.util.Deque;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collector;
/*     */ import org.apache.tools.ant.PropertyHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LocalPropertyStack
/*     */ {
/*  38 */   private final Deque<Map<String, Object>> stack = new LinkedList<>();
/*  39 */   private final Object LOCK = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addLocal(String property) {
/*  52 */     synchronized (this.LOCK) {
/*  53 */       Map<String, Object> map = this.stack.peek();
/*  54 */       if (map != null) {
/*  55 */         map.put(property, NullReturn.NULL);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterScope() {
/*  64 */     synchronized (this.LOCK) {
/*  65 */       this.stack.addFirst(new ConcurrentHashMap<>());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void exitScope() {
/*  73 */     synchronized (this.LOCK) {
/*  74 */       ((Map)this.stack.removeFirst()).clear();
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
/*     */   public LocalPropertyStack copy() {
/*  89 */     synchronized (this.LOCK) {
/*  90 */       LocalPropertyStack ret = new LocalPropertyStack();
/*  91 */       ret.stack.addAll(this.stack);
/*  92 */       return ret;
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
/*     */   public Object evaluate(String property, PropertyHelper helper) {
/* 109 */     synchronized (this.LOCK) {
/* 110 */       for (Map<String, Object> map : this.stack) {
/* 111 */         Object ret = map.get(property);
/* 112 */         if (ret != null) {
/* 113 */           return ret;
/*     */         }
/*     */       } 
/*     */     } 
/* 117 */     return null;
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
/*     */   public boolean setNew(String property, Object value, PropertyHelper propertyHelper) {
/* 129 */     Map<String, Object> map = getMapForProperty(property);
/* 130 */     if (map == null) {
/* 131 */       return false;
/*     */     }
/* 133 */     Object currValue = map.get(property);
/* 134 */     if (currValue == NullReturn.NULL) {
/* 135 */       map.put(property, value);
/*     */     }
/* 137 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean set(String property, Object value, PropertyHelper propertyHelper) {
/* 148 */     Map<String, Object> map = getMapForProperty(property);
/* 149 */     if (map == null) {
/* 150 */       return false;
/*     */     }
/* 152 */     map.put(property, value);
/* 153 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getPropertyNames() {
/* 163 */     Set<String> names = (Set<String>)this.stack.stream().map(Map::keySet).collect(Collector.of(HashSet::new, AbstractCollection::addAll, (ns1, ns2) -> {
/*     */             ns1.addAll(ns2);
/*     */             return ns1;
/*     */           }new Collector.Characteristics[] { Collector.Characteristics.UNORDERED, Collector.Characteristics.IDENTITY_FINISH }));
/* 167 */     return Collections.unmodifiableSet(names);
/*     */   }
/*     */   
/*     */   private Map<String, Object> getMapForProperty(String property) {
/* 171 */     synchronized (this.LOCK) {
/* 172 */       for (Map<String, Object> map : this.stack) {
/* 173 */         if (map.get(property) != null) {
/* 174 */           return map;
/*     */         }
/*     */       } 
/*     */     } 
/* 178 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/property/LocalPropertyStack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */