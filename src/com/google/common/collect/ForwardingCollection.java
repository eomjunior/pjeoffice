/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import javax.annotation.CheckForNull;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ public abstract class ForwardingCollection<E>
/*     */   extends ForwardingObject
/*     */   implements Collection<E>
/*     */ {
/*     */   public Iterator<E> iterator() {
/*  63 */     return delegate().iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  68 */     return delegate().size();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean removeAll(Collection<?> collection) {
/*  74 */     return delegate().removeAll(collection);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  79 */     return delegate().isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(@CheckForNull Object object) {
/*  84 */     return delegate().contains(object);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean add(@ParametricNullness E element) {
/*  90 */     return delegate().add(element);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean remove(@CheckForNull Object object) {
/*  96 */     return delegate().remove(object);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> collection) {
/* 101 */     return delegate().containsAll(collection);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean addAll(Collection<? extends E> collection) {
/* 107 */     return delegate().addAll(collection);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean retainAll(Collection<?> collection) {
/* 113 */     return delegate().retainAll(collection);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 118 */     delegate().clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 123 */     return delegate().toArray();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public <T> T[] toArray(T[] array) {
/* 130 */     return delegate().toArray(array);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardContains(@CheckForNull Object object) {
/* 141 */     return Iterators.contains(iterator(), object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardContainsAll(Collection<?> collection) {
/* 152 */     return Collections2.containsAllImpl(this, collection);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardAddAll(Collection<? extends E> collection) {
/* 162 */     return Iterators.addAll(this, collection.iterator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardRemove(@CheckForNull Object object) {
/* 173 */     Iterator<E> iterator = iterator();
/* 174 */     while (iterator.hasNext()) {
/* 175 */       if (Objects.equal(iterator.next(), object)) {
/* 176 */         iterator.remove();
/* 177 */         return true;
/*     */       } 
/*     */     } 
/* 180 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardRemoveAll(Collection<?> collection) {
/* 191 */     return Iterators.removeAll(iterator(), collection);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardRetainAll(Collection<?> collection) {
/* 202 */     return Iterators.retainAll(iterator(), collection);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void standardClear() {
/* 213 */     Iterators.clear(iterator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardIsEmpty() {
/* 224 */     return !iterator().hasNext();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String standardToString() {
/* 235 */     return Collections2.toStringImpl(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object[] standardToArray() {
/* 246 */     Object[] newArray = new Object[size()];
/* 247 */     return toArray(newArray);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T> T[] standardToArray(T[] array) {
/* 258 */     return ObjectArrays.toArrayImpl(this, array);
/*     */   }
/*     */   
/*     */   protected abstract Collection<E> delegate();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ForwardingCollection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */