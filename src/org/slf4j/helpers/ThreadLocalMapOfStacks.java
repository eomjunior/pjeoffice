/*    */ package org.slf4j.helpers;
/*    */ 
/*    */ import java.util.ArrayDeque;
/*    */ import java.util.Deque;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ThreadLocalMapOfStacks
/*    */ {
/* 21 */   final ThreadLocal<Map<String, Deque<String>>> tlMapOfStacks = new ThreadLocal<>();
/*    */   
/*    */   public void pushByKey(String key, String value) {
/* 24 */     if (key == null) {
/*    */       return;
/*    */     }
/* 27 */     Map<String, Deque<String>> map = this.tlMapOfStacks.get();
/*    */     
/* 29 */     if (map == null) {
/* 30 */       map = new HashMap<>();
/* 31 */       this.tlMapOfStacks.set(map);
/*    */     } 
/*    */     
/* 34 */     Deque<String> deque = map.get(key);
/* 35 */     if (deque == null) {
/* 36 */       deque = new ArrayDeque<>();
/*    */     }
/* 38 */     deque.push(value);
/* 39 */     map.put(key, deque);
/*    */   }
/*    */   
/*    */   public String popByKey(String key) {
/* 43 */     if (key == null) {
/* 44 */       return null;
/*    */     }
/* 46 */     Map<String, Deque<String>> map = this.tlMapOfStacks.get();
/* 47 */     if (map == null)
/* 48 */       return null; 
/* 49 */     Deque<String> deque = map.get(key);
/* 50 */     if (deque == null)
/* 51 */       return null; 
/* 52 */     return deque.pop();
/*    */   }
/*    */   
/*    */   public Deque<String> getCopyOfDequeByKey(String key) {
/* 56 */     if (key == null) {
/* 57 */       return null;
/*    */     }
/* 59 */     Map<String, Deque<String>> map = this.tlMapOfStacks.get();
/* 60 */     if (map == null)
/* 61 */       return null; 
/* 62 */     Deque<String> deque = map.get(key);
/* 63 */     if (deque == null) {
/* 64 */       return null;
/*    */     }
/* 66 */     return new ArrayDeque<>(deque);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void clearDequeByKey(String key) {
/* 77 */     if (key == null) {
/*    */       return;
/*    */     }
/* 80 */     Map<String, Deque<String>> map = this.tlMapOfStacks.get();
/* 81 */     if (map == null)
/*    */       return; 
/* 83 */     Deque<String> deque = map.get(key);
/* 84 */     if (deque == null)
/*    */       return; 
/* 86 */     deque.clear();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/slf4j/helpers/ThreadLocalMapOfStacks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */