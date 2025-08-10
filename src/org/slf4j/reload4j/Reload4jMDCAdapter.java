/*     */ package org.slf4j.reload4j;
/*     */ 
/*     */ import java.util.Deque;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.log4j.MDC;
/*     */ import org.slf4j.helpers.ThreadLocalMapOfStacks;
/*     */ import org.slf4j.spi.MDCAdapter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Reload4jMDCAdapter
/*     */   implements MDCAdapter
/*     */ {
/*  36 */   private final ThreadLocalMapOfStacks threadLocalMapOfDeques = new ThreadLocalMapOfStacks();
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/*  41 */     Map map = MDC.getContext();
/*  42 */     if (map != null) {
/*  43 */       map.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String get(String key) {
/*  49 */     return (String)MDC.get(key);
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
/*     */   public void put(String key, String val) {
/*  66 */     MDC.put(key, val);
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(String key) {
/*  71 */     MDC.remove(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map getCopyOfContextMap() {
/*  76 */     Map<?, ?> old = MDC.getContext();
/*  77 */     if (old != null) {
/*  78 */       return new HashMap<>(old);
/*     */     }
/*  80 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContextMap(Map<String, String> contextMap) {
/*  87 */     Map<String, String> old = MDC.getContext();
/*     */ 
/*     */     
/*  90 */     if (contextMap == null) {
/*  91 */       if (old != null) {
/*  92 */         old.clear();
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/*  97 */     if (old == null) {
/*  98 */       for (Map.Entry<String, String> mapEntry : contextMap.entrySet()) {
/*  99 */         MDC.put(mapEntry.getKey(), mapEntry.getValue());
/*     */       }
/*     */     } else {
/* 102 */       old.clear();
/* 103 */       old.putAll(contextMap);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void pushByKey(String key, String value) {
/* 109 */     this.threadLocalMapOfDeques.pushByKey(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public String popByKey(String key) {
/* 114 */     return this.threadLocalMapOfDeques.popByKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public Deque<String> getCopyOfDequeByKey(String key) {
/* 119 */     return this.threadLocalMapOfDeques.getCopyOfDequeByKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearDequeByKey(String key) {
/* 124 */     this.threadLocalMapOfDeques.clearDequeByKey(key);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/slf4j/reload4j/Reload4jMDCAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */