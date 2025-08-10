/*     */ package org.apache.tools.ant.types.resources;
/*     */ 
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.WeakHashMap;
/*     */ import org.apache.tools.ant.types.Resource;
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
/*     */ class FailFast
/*     */   implements Iterator<Resource>
/*     */ {
/*  35 */   private static final WeakHashMap<Object, Set<FailFast>> MAP = new WeakHashMap<>();
/*     */   
/*     */   private final Object parent;
/*     */   
/*     */   private Iterator<Resource> wrapped;
/*     */   
/*     */   static synchronized void invalidate(Object o) {
/*  42 */     Set<FailFast> s = MAP.get(o);
/*  43 */     if (s != null) {
/*  44 */       s.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   private static synchronized void add(FailFast f) {
/*  49 */     ((Set<FailFast>)MAP.computeIfAbsent(f.parent, k -> new HashSet())).add(f);
/*     */   }
/*     */   
/*     */   private static synchronized void remove(FailFast f) {
/*  53 */     Set<FailFast> s = MAP.get(f.parent);
/*  54 */     if (s != null) {
/*  55 */       s.remove(f);
/*     */     }
/*     */   }
/*     */   
/*     */   private static synchronized void failFast(FailFast f) {
/*  60 */     Set<FailFast> s = MAP.get(f.parent);
/*  61 */     if (!s.contains(f)) {
/*  62 */       throw new ConcurrentModificationException();
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
/*     */   FailFast(Object o, Iterator<Resource> i) {
/*  76 */     if (o == null) {
/*  77 */       throw new IllegalArgumentException("parent object is null");
/*     */     }
/*  79 */     if (i == null) {
/*  80 */       throw new IllegalArgumentException("cannot wrap null iterator");
/*     */     }
/*  82 */     this.parent = o;
/*  83 */     if (i.hasNext()) {
/*  84 */       this.wrapped = i;
/*  85 */       add(this);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasNext() {
/*  95 */     if (this.wrapped == null) {
/*  96 */       return false;
/*     */     }
/*  98 */     failFast(this);
/*  99 */     return this.wrapped.hasNext();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Resource next() {
/* 109 */     if (this.wrapped == null || !this.wrapped.hasNext()) {
/* 110 */       throw new NoSuchElementException();
/*     */     }
/* 112 */     failFast(this);
/*     */     try {
/* 114 */       return this.wrapped.next();
/*     */     } finally {
/* 116 */       if (!this.wrapped.hasNext()) {
/* 117 */         this.wrapped = null;
/* 118 */         remove(this);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove() {
/* 129 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/FailFast.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */