/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LinkedHashtable<K, V>
/*     */   extends Hashtable<K, V>
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final LinkedHashMap<K, V> map;
/*     */   
/*     */   public LinkedHashtable() {
/*  47 */     this.map = new LinkedHashMap<>();
/*     */   }
/*     */   
/*     */   public LinkedHashtable(int initialCapacity) {
/*  51 */     this.map = new LinkedHashMap<>(initialCapacity);
/*     */   }
/*     */   
/*     */   public LinkedHashtable(int initialCapacity, float loadFactor) {
/*  55 */     this.map = new LinkedHashMap<>(initialCapacity, loadFactor);
/*     */   }
/*     */   
/*     */   public LinkedHashtable(Map<K, V> m) {
/*  59 */     this.map = new LinkedHashMap<>(m);
/*     */   }
/*     */   
/*     */   public synchronized void clear() {
/*  63 */     this.map.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object value) {
/*  68 */     return containsKey(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean containsKey(Object value) {
/*  73 */     return this.map.containsKey(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean containsValue(Object value) {
/*  78 */     return this.map.containsValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public Enumeration<V> elements() {
/*  83 */     return Collections.enumeration(values());
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Set<Map.Entry<K, V>> entrySet() {
/*  88 */     return this.map.entrySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean equals(Object o) {
/*  93 */     return this.map.equals(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized V get(Object k) {
/*  98 */     return this.map.get(k);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int hashCode() {
/* 103 */     return this.map.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean isEmpty() {
/* 108 */     return this.map.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Enumeration<K> keys() {
/* 113 */     return Collections.enumeration(keySet());
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Set<K> keySet() {
/* 118 */     return this.map.keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized V put(K k, V v) {
/* 123 */     return this.map.put(k, v);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void putAll(Map<? extends K, ? extends V> m) {
/* 128 */     this.map.putAll(m);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized V remove(Object k) {
/* 133 */     return this.map.remove(k);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int size() {
/* 138 */     return this.map.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized String toString() {
/* 143 */     return this.map.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Collection<V> values() {
/* 148 */     return this.map.values();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/LinkedHashtable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */