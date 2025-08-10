/*     */ package io.reactivex.internal.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.RandomAccess;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ public final class VolatileSizeArrayList<T>
/*     */   extends AtomicInteger
/*     */   implements List<T>, RandomAccess
/*     */ {
/*     */   private static final long serialVersionUID = 3972397474470203923L;
/*     */   final ArrayList<T> list;
/*     */   
/*     */   public VolatileSizeArrayList() {
/*  32 */     this.list = new ArrayList<T>();
/*     */   }
/*     */   
/*     */   public VolatileSizeArrayList(int initialCapacity) {
/*  36 */     this.list = new ArrayList<T>(initialCapacity);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  41 */     return get();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  46 */     return (get() == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object o) {
/*  51 */     return this.list.contains(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<T> iterator() {
/*  56 */     return this.list.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/*  61 */     return this.list.toArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public <E> E[] toArray(E[] a) {
/*  66 */     return this.list.toArray(a);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean add(T e) {
/*  71 */     boolean b = this.list.add(e);
/*  72 */     lazySet(this.list.size());
/*  73 */     return b;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object o) {
/*  78 */     boolean b = this.list.remove(o);
/*  79 */     lazySet(this.list.size());
/*  80 */     return b;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> c) {
/*  85 */     return this.list.containsAll(c);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(Collection<? extends T> c) {
/*  90 */     boolean b = this.list.addAll(c);
/*  91 */     lazySet(this.list.size());
/*  92 */     return b;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(int index, Collection<? extends T> c) {
/*  97 */     boolean b = this.list.addAll(index, c);
/*  98 */     lazySet(this.list.size());
/*  99 */     return b;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeAll(Collection<?> c) {
/* 104 */     boolean b = this.list.removeAll(c);
/* 105 */     lazySet(this.list.size());
/* 106 */     return b;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean retainAll(Collection<?> c) {
/* 111 */     boolean b = this.list.retainAll(c);
/* 112 */     lazySet(this.list.size());
/* 113 */     return b;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 118 */     this.list.clear();
/* 119 */     lazySet(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public T get(int index) {
/* 124 */     return this.list.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public T set(int index, T element) {
/* 129 */     return this.list.set(index, element);
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(int index, T element) {
/* 134 */     this.list.add(index, element);
/* 135 */     lazySet(this.list.size());
/*     */   }
/*     */ 
/*     */   
/*     */   public T remove(int index) {
/* 140 */     T v = this.list.remove(index);
/* 141 */     lazySet(this.list.size());
/* 142 */     return v;
/*     */   }
/*     */ 
/*     */   
/*     */   public int indexOf(Object o) {
/* 147 */     return this.list.indexOf(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public int lastIndexOf(Object o) {
/* 152 */     return this.list.lastIndexOf(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListIterator<T> listIterator() {
/* 157 */     return this.list.listIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public ListIterator<T> listIterator(int index) {
/* 162 */     return this.list.listIterator(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<T> subList(int fromIndex, int toIndex) {
/* 167 */     return this.list.subList(fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 172 */     if (obj instanceof VolatileSizeArrayList) {
/* 173 */       return this.list.equals(((VolatileSizeArrayList)obj).list);
/*     */     }
/* 175 */     return this.list.equals(obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 180 */     return this.list.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 185 */     return this.list.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/util/VolatileSizeArrayList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */