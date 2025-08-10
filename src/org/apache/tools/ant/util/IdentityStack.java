/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Stack;
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
/*     */ public class IdentityStack<E>
/*     */   extends Stack<E>
/*     */ {
/*     */   private static final long serialVersionUID = -5555522620060077046L;
/*     */   
/*     */   public static <E> IdentityStack<E> getInstance(Stack<E> s) {
/*  41 */     if (s instanceof IdentityStack) {
/*  42 */       return (IdentityStack<E>)s;
/*     */     }
/*  44 */     IdentityStack<E> result = new IdentityStack<>();
/*  45 */     if (s != null) {
/*  46 */       result.addAll(s);
/*     */     }
/*  48 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IdentityStack() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IdentityStack(E o) {
/*  64 */     push(o);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean contains(Object o) {
/*  75 */     return (indexOf(o) >= 0);
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
/*     */   public synchronized int indexOf(Object o, int pos) {
/*  87 */     int size = size();
/*  88 */     for (int i = pos; i < size; i++) {
/*  89 */       if (get(i) == o) {
/*  90 */         return i;
/*     */       }
/*     */     } 
/*  93 */     return -1;
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
/*     */   public synchronized int lastIndexOf(Object o, int pos) {
/* 105 */     for (int i = pos; i >= 0; i--) {
/* 106 */       if (get(i) == o) {
/* 107 */         return i;
/*     */       }
/*     */     } 
/* 110 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean removeAll(Collection<?> c) {
/* 115 */     if (!(c instanceof java.util.Set)) {
/* 116 */       c = new HashSet(c);
/*     */     }
/* 118 */     return super.removeAll(c);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean retainAll(Collection<?> c) {
/* 123 */     if (!(c instanceof java.util.Set)) {
/* 124 */       c = new HashSet(c);
/*     */     }
/* 126 */     return super.retainAll(c);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean containsAll(Collection<?> c) {
/* 131 */     IdentityHashMap<Object, Boolean> map = new IdentityHashMap<>();
/* 132 */     for (E e : this) {
/* 133 */       map.put(e, Boolean.TRUE);
/*     */     }
/* 135 */     return map.keySet().containsAll(c);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/IdentityStack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */