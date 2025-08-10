/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.util.Collections;
/*    */ import java.util.Comparator;
/*    */ import java.util.Enumeration;
/*    */ import java.util.Map;
/*    */ import java.util.Properties;
/*    */ import java.util.Set;
/*    */ import java.util.TreeSet;
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
/*    */ public class SortedStoredProperties
/*    */   extends Properties
/*    */ {
/*    */   public void storeSorted(OutputStream out, String comments) throws IOException {
/* 43 */     Properties sortedProps = new Properties()
/*    */       {
/*    */         public Set<Map.Entry<Object, Object>> entrySet() {
/* 46 */           Set<Map.Entry<Object, Object>> sortedSet = new TreeSet<>(new Comparator<Map.Entry<Object, Object>>()
/*    */               {
/*    */                 public int compare(Map.Entry<Object, Object> o1, Map.Entry<Object, Object> o2) {
/* 49 */                   return o1.getKey().toString().compareTo(o2.getKey().toString());
/*    */                 }
/*    */               });
/* 52 */           sortedSet.addAll(super.entrySet());
/* 53 */           return sortedSet;
/*    */         }
/*    */ 
/*    */         
/*    */         public Set<Object> keySet() {
/* 58 */           return new TreeSet(super.keySet());
/*    */         }
/*    */ 
/*    */         
/*    */         public synchronized Enumeration<Object> keys() {
/* 63 */           return Collections.enumeration(new TreeSet(super.keySet()));
/*    */         }
/*    */       };
/* 66 */     sortedProps.putAll(this);
/* 67 */     sortedProps.store(out, comments);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/SortedStoredProperties.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */