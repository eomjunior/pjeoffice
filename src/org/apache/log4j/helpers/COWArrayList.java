/*     */ package org.apache.log4j.helpers;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ public class COWArrayList<E>
/*     */   implements List<E>
/*     */ {
/*  66 */   AtomicBoolean fresh = new AtomicBoolean(false);
/*  67 */   CopyOnWriteArrayList<E> underlyingList = new CopyOnWriteArrayList<E>();
/*     */   E[] ourCopy;
/*     */   final E[] modelArray;
/*     */   
/*     */   public COWArrayList(E[] modelArray) {
/*  72 */     this.modelArray = modelArray;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  77 */     return this.underlyingList.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  82 */     return this.underlyingList.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object o) {
/*  87 */     return this.underlyingList.contains(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/*  92 */     return this.underlyingList.iterator();
/*     */   }
/*     */   
/*     */   private void refreshCopyIfNecessary() {
/*  96 */     if (!isFresh()) {
/*  97 */       refreshCopy();
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isFresh() {
/* 102 */     return this.fresh.get();
/*     */   }
/*     */   
/*     */   private void refreshCopy() {
/* 106 */     this.ourCopy = this.underlyingList.toArray(this.modelArray);
/* 107 */     this.fresh.set(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 112 */     refreshCopyIfNecessary();
/* 113 */     return (Object[])this.ourCopy;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T[] toArray(T[] a) {
/* 119 */     refreshCopyIfNecessary();
/* 120 */     return (T[])this.ourCopy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E[] asTypedArray() {
/* 131 */     refreshCopyIfNecessary();
/* 132 */     return this.ourCopy;
/*     */   }
/*     */   
/*     */   private void markAsStale() {
/* 136 */     this.fresh.set(false);
/*     */   }
/*     */   
/*     */   public void addIfAbsent(E e) {
/* 140 */     this.underlyingList.addIfAbsent(e);
/* 141 */     markAsStale();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean add(E e) {
/* 146 */     boolean result = this.underlyingList.add(e);
/* 147 */     markAsStale();
/* 148 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object o) {
/* 153 */     boolean result = this.underlyingList.remove(o);
/* 154 */     markAsStale();
/* 155 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> c) {
/* 160 */     return this.underlyingList.containsAll(c);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(Collection<? extends E> c) {
/* 165 */     markAsStale();
/* 166 */     boolean result = this.underlyingList.addAll(c);
/* 167 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(int index, Collection<? extends E> col) {
/* 172 */     markAsStale();
/* 173 */     boolean result = this.underlyingList.addAll(index, col);
/* 174 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeAll(Collection<?> col) {
/* 179 */     markAsStale();
/* 180 */     boolean result = this.underlyingList.removeAll(col);
/* 181 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean retainAll(Collection<?> col) {
/* 186 */     markAsStale();
/* 187 */     boolean result = this.underlyingList.retainAll(col);
/* 188 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 193 */     markAsStale();
/* 194 */     this.underlyingList.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public E get(int index) {
/* 199 */     refreshCopyIfNecessary();
/* 200 */     return this.ourCopy[index];
/*     */   }
/*     */ 
/*     */   
/*     */   public E set(int index, E element) {
/* 205 */     markAsStale();
/* 206 */     E e = this.underlyingList.set(index, element);
/* 207 */     return e;
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(int index, E element) {
/* 212 */     markAsStale();
/* 213 */     this.underlyingList.add(index, element);
/*     */   }
/*     */ 
/*     */   
/*     */   public E remove(int index) {
/* 218 */     markAsStale();
/* 219 */     E e = this.underlyingList.remove(index);
/* 220 */     return e;
/*     */   }
/*     */ 
/*     */   
/*     */   public int indexOf(Object o) {
/* 225 */     return this.underlyingList.indexOf(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public int lastIndexOf(Object o) {
/* 230 */     return this.underlyingList.lastIndexOf(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListIterator<E> listIterator() {
/* 235 */     return this.underlyingList.listIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public ListIterator<E> listIterator(int index) {
/* 240 */     return this.underlyingList.listIterator(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<E> subList(int fromIndex, int toIndex) {
/* 245 */     return this.underlyingList.subList(fromIndex, toIndex);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/helpers/COWArrayList.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */