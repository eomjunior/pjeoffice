/*    */ package org.slf4j.helpers;
/*    */ 
/*    */ import java.util.Deque;
/*    */ import java.util.Map;
/*    */ import org.slf4j.spi.MDCAdapter;
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
/*    */ public class NOPMDCAdapter
/*    */   implements MDCAdapter
/*    */ {
/*    */   public void clear() {}
/*    */   
/*    */   public String get(String key) {
/* 47 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void put(String key, String val) {}
/*    */ 
/*    */   
/*    */   public void remove(String key) {}
/*    */   
/*    */   public Map<String, String> getCopyOfContextMap() {
/* 57 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setContextMap(Map<String, String> contextMap) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void pushByKey(String key, String value) {}
/*    */ 
/*    */   
/*    */   public String popByKey(String key) {
/* 70 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Deque<String> getCopyOfDequeByKey(String key) {
/* 75 */     return null;
/*    */   }
/*    */   
/*    */   public void clearDequeByKey(String key) {}
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/slf4j/helpers/NOPMDCAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */