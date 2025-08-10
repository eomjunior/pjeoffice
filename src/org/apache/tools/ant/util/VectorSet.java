/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Vector;
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
/*     */ public final class VectorSet<E>
/*     */   extends Vector<E>
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  43 */   private final HashSet<E> set = new HashSet<>();
/*     */ 
/*     */   
/*     */   public VectorSet() {}
/*     */ 
/*     */   
/*     */   public VectorSet(int initialCapacity) {
/*  50 */     super(initialCapacity);
/*     */   }
/*     */   
/*     */   public VectorSet(int initialCapacity, int capacityIncrement) {
/*  54 */     super(initialCapacity, capacityIncrement);
/*     */   }
/*     */   
/*     */   public VectorSet(Collection<? extends E> c) {
/*  58 */     if (c != null) {
/*  59 */       addAll(c);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean add(E o) {
/*  65 */     if (!this.set.contains(o)) {
/*  66 */       doAdd(size(), o);
/*  67 */       return true;
/*     */     } 
/*  69 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(int index, E o) {
/*  78 */     doAdd(index, o);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized void doAdd(int index, E o) {
/*  84 */     if (this.set.add(o)) {
/*  85 */       int count = size();
/*  86 */       ensureCapacity(count + 1);
/*  87 */       if (index != count) {
/*  88 */         System.arraycopy(this.elementData, index, this.elementData, index + 1, count - index);
/*     */       }
/*     */       
/*  91 */       this.elementData[index] = o;
/*  92 */       this.elementCount++;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void addElement(E o) {
/*  98 */     doAdd(size(), o);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean addAll(Collection<? extends E> c) {
/* 103 */     boolean changed = false;
/* 104 */     for (E e : c) {
/* 105 */       changed |= add(e);
/*     */     }
/* 107 */     return changed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean addAll(int index, Collection<? extends E> c) {
/* 116 */     LinkedList<E> toAdd = new LinkedList<>();
/* 117 */     for (E e : c) {
/* 118 */       if (this.set.add(e)) {
/* 119 */         toAdd.add(e);
/*     */       }
/*     */     } 
/* 122 */     if (toAdd.isEmpty()) {
/* 123 */       return false;
/*     */     }
/* 125 */     int count = size();
/* 126 */     ensureCapacity(count + toAdd.size());
/* 127 */     if (index != count) {
/* 128 */       System.arraycopy(this.elementData, index, this.elementData, index + toAdd.size(), count - index);
/*     */     }
/*     */     
/* 131 */     for (E o : toAdd) {
/* 132 */       this.elementData[index++] = o;
/*     */     }
/* 134 */     this.elementCount += toAdd.size();
/* 135 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void clear() {
/* 140 */     super.clear();
/* 141 */     this.set.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() {
/* 147 */     VectorSet<E> vs = (VectorSet<E>)super.clone();
/* 148 */     vs.set.addAll(this.set);
/* 149 */     return vs;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean contains(Object o) {
/* 154 */     return this.set.contains(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean containsAll(Collection<?> c) {
/* 159 */     return this.set.containsAll(c);
/*     */   }
/*     */ 
/*     */   
/*     */   public void insertElementAt(E o, int index) {
/* 164 */     doAdd(index, o);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized E remove(int index) {
/* 169 */     E o = get(index);
/* 170 */     remove(o);
/* 171 */     return o;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object o) {
/* 176 */     return doRemove(o);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized boolean doRemove(Object o) {
/* 182 */     if (this.set.remove(o)) {
/* 183 */       int index = indexOf(o);
/* 184 */       if (index < this.elementData.length - 1) {
/* 185 */         System.arraycopy(this.elementData, index + 1, this.elementData, index, this.elementData.length - index - 1);
/*     */       }
/*     */       
/* 188 */       this.elementCount--;
/* 189 */       return true;
/*     */     } 
/* 191 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean removeAll(Collection<?> c) {
/* 196 */     boolean changed = false;
/* 197 */     for (Object o : c) {
/* 198 */       changed |= remove(o);
/*     */     }
/* 200 */     return changed;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void removeAllElements() {
/* 205 */     this.set.clear();
/* 206 */     super.removeAllElements();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeElement(Object o) {
/* 211 */     return doRemove(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void removeElementAt(int index) {
/* 216 */     remove(get(index));
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void removeRange(int fromIndex, int toIndex) {
/* 221 */     while (toIndex > fromIndex) {
/* 222 */       remove(--toIndex);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean retainAll(Collection<?> c) {
/* 228 */     if (!(c instanceof java.util.Set)) {
/* 229 */       c = new HashSet(c);
/*     */     }
/* 231 */     LinkedList<E> l = new LinkedList<>();
/* 232 */     for (E o : this) {
/* 233 */       if (!c.contains(o)) {
/* 234 */         l.addLast(o);
/*     */       }
/*     */     } 
/* 237 */     if (!l.isEmpty()) {
/* 238 */       removeAll(l);
/* 239 */       return true;
/*     */     } 
/* 241 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized E set(int index, E o) {
/* 246 */     E orig = get(index);
/* 247 */     if (this.set.add(o)) {
/* 248 */       this.elementData[index] = o;
/* 249 */       this.set.remove(orig);
/*     */     } else {
/* 251 */       int oldIndexOfO = indexOf(o);
/* 252 */       remove(o);
/* 253 */       remove(orig);
/* 254 */       add((oldIndexOfO > index) ? index : (index - 1), o);
/*     */     } 
/* 256 */     return orig;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setElementAt(E o, int index) {
/* 261 */     set(index, o);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/VectorSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */