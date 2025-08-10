/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ public abstract class ForwardingList<E>
/*     */   extends ForwardingCollection<E>
/*     */   implements List<E>
/*     */ {
/*     */   public void add(int index, @ParametricNullness E element) {
/*  67 */     delegate().add(index, element);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean addAll(int index, Collection<? extends E> elements) {
/*  73 */     return delegate().addAll(index, elements);
/*     */   }
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   public E get(int index) {
/*  79 */     return delegate().get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public int indexOf(@CheckForNull Object element) {
/*  84 */     return delegate().indexOf(element);
/*     */   }
/*     */ 
/*     */   
/*     */   public int lastIndexOf(@CheckForNull Object element) {
/*  89 */     return delegate().lastIndexOf(element);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListIterator<E> listIterator() {
/*  94 */     return delegate().listIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public ListIterator<E> listIterator(int index) {
/*  99 */     return delegate().listIterator(index);
/*     */   }
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   @CanIgnoreReturnValue
/*     */   public E remove(int index) {
/* 106 */     return delegate().remove(index);
/*     */   }
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   @CanIgnoreReturnValue
/*     */   public E set(int index, @ParametricNullness E element) {
/* 113 */     return delegate().set(index, element);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<E> subList(int fromIndex, int toIndex) {
/* 118 */     return delegate().subList(fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@CheckForNull Object object) {
/* 123 */     return (object == this || delegate().equals(object));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 128 */     return delegate().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardAdd(@ParametricNullness E element) {
/* 139 */     add(size(), element);
/* 140 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardAddAll(int index, Iterable<? extends E> elements) {
/* 151 */     return Lists.addAllImpl(this, index, elements);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int standardIndexOf(@CheckForNull Object element) {
/* 162 */     return Lists.indexOfImpl(this, element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int standardLastIndexOf(@CheckForNull Object element) {
/* 173 */     return Lists.lastIndexOfImpl(this, element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Iterator<E> standardIterator() {
/* 184 */     return listIterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ListIterator<E> standardListIterator() {
/* 195 */     return listIterator(0);
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
/*     */   protected ListIterator<E> standardListIterator(int start) {
/* 207 */     return Lists.listIteratorImpl(this, start);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<E> standardSubList(int fromIndex, int toIndex) {
/* 217 */     return Lists.subListImpl(this, fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardEquals(@CheckForNull Object object) {
/* 228 */     return Lists.equalsImpl(this, object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int standardHashCode() {
/* 239 */     return Lists.hashCodeImpl(this);
/*     */   }
/*     */   
/*     */   protected abstract List<E> delegate();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ForwardingList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */