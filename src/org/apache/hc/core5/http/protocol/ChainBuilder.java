/*     */ package org.apache.hc.core5.http.protocol;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
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
/*     */ final class ChainBuilder<E>
/*     */ {
/*  49 */   private final LinkedList<E> list = new LinkedList<>();
/*  50 */   private final Map<Class<?>, E> uniqueClasses = new HashMap<>();
/*     */ 
/*     */   
/*     */   private void ensureUnique(E e) {
/*  54 */     E previous = this.uniqueClasses.remove(e.getClass());
/*  55 */     if (previous != null) {
/*  56 */       this.list.remove(previous);
/*     */     }
/*  58 */     this.uniqueClasses.put(e.getClass(), e);
/*     */   }
/*     */   
/*     */   public ChainBuilder<E> addFirst(E e) {
/*  62 */     if (e == null) {
/*  63 */       return this;
/*     */     }
/*  65 */     ensureUnique(e);
/*  66 */     this.list.addFirst(e);
/*  67 */     return this;
/*     */   }
/*     */   
/*     */   public ChainBuilder<E> addLast(E e) {
/*  71 */     if (e == null) {
/*  72 */       return this;
/*     */     }
/*  74 */     ensureUnique(e);
/*  75 */     this.list.addLast(e);
/*  76 */     return this;
/*     */   }
/*     */   
/*     */   public ChainBuilder<E> addAllFirst(Collection<E> c) {
/*  80 */     if (c == null) {
/*  81 */       return this;
/*     */     }
/*  83 */     for (E e : c) {
/*  84 */       addFirst(e);
/*     */     }
/*  86 */     return this;
/*     */   }
/*     */   
/*     */   @SafeVarargs
/*     */   public final ChainBuilder<E> addAllFirst(E... c) {
/*  91 */     if (c == null) {
/*  92 */       return this;
/*     */     }
/*  94 */     for (E e : c) {
/*  95 */       addFirst(e);
/*     */     }
/*  97 */     return this;
/*     */   }
/*     */   
/*     */   public ChainBuilder<E> addAllLast(Collection<E> c) {
/* 101 */     if (c == null) {
/* 102 */       return this;
/*     */     }
/* 104 */     for (E e : c) {
/* 105 */       addLast(e);
/*     */     }
/* 107 */     return this;
/*     */   }
/*     */   
/*     */   @SafeVarargs
/*     */   public final ChainBuilder<E> addAllLast(E... c) {
/* 112 */     if (c == null) {
/* 113 */       return this;
/*     */     }
/* 115 */     for (E e : c) {
/* 116 */       addLast(e);
/*     */     }
/* 118 */     return this;
/*     */   }
/*     */   
/*     */   public LinkedList<E> build() {
/* 122 */     return new LinkedList<>(this.list);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/protocol/ChainBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */