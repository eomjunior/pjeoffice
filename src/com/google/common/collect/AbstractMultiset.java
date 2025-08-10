/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ abstract class AbstractMultiset<E>
/*     */   extends AbstractCollection<E>
/*     */   implements Multiset<E>
/*     */ {
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   private transient Set<E> elementSet;
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   private transient Set<Multiset.Entry<E>> entrySet;
/*     */   
/*     */   public boolean isEmpty() {
/*  53 */     return entrySet().isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(@CheckForNull Object element) {
/*  58 */     return (count(element) > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final boolean add(@ParametricNullness E element) {
/*  65 */     add(element, 1);
/*  66 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int add(@ParametricNullness E element, int occurrences) {
/*  72 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final boolean remove(@CheckForNull Object element) {
/*  78 */     return (remove(element, 1) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int remove(@CheckForNull Object element, int occurrences) {
/*  84 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int setCount(@ParametricNullness E element, int count) {
/*  90 */     return Multisets.setCountImpl(this, element, count);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean setCount(@ParametricNullness E element, int oldCount, int newCount) {
/*  96 */     return Multisets.setCountImpl(this, element, oldCount, newCount);
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
/*     */   @CanIgnoreReturnValue
/*     */   public final boolean addAll(Collection<? extends E> elementsToAdd) {
/* 110 */     return Multisets.addAllImpl(this, elementsToAdd);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final boolean removeAll(Collection<?> elementsToRemove) {
/* 116 */     return Multisets.removeAllImpl(this, elementsToRemove);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final boolean retainAll(Collection<?> elementsToRetain) {
/* 122 */     return Multisets.retainAllImpl(this, elementsToRetain);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void clear();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<E> elementSet() {
/* 134 */     Set<E> result = this.elementSet;
/* 135 */     if (result == null) {
/* 136 */       this.elementSet = result = createElementSet();
/*     */     }
/* 138 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Set<E> createElementSet() {
/* 146 */     return new ElementSet();
/*     */   }
/*     */   
/*     */   abstract Iterator<E> elementIterator();
/*     */   
/*     */   class ElementSet extends Multisets.ElementSet<E> {
/*     */     Multiset<E> multiset() {
/* 153 */       return AbstractMultiset.this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<E> iterator() {
/* 158 */       return AbstractMultiset.this.elementIterator();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Multiset.Entry<E>> entrySet() {
/* 168 */     Set<Multiset.Entry<E>> result = this.entrySet;
/* 169 */     if (result == null) {
/* 170 */       this.entrySet = result = createEntrySet();
/*     */     }
/* 172 */     return result;
/*     */   }
/*     */   
/*     */   class EntrySet
/*     */     extends Multisets.EntrySet<E>
/*     */   {
/*     */     Multiset<E> multiset() {
/* 179 */       return AbstractMultiset.this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<Multiset.Entry<E>> iterator() {
/* 184 */       return AbstractMultiset.this.entryIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 189 */       return AbstractMultiset.this.distinctElements();
/*     */     }
/*     */   }
/*     */   
/*     */   Set<Multiset.Entry<E>> createEntrySet() {
/* 194 */     return new EntrySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract Iterator<Multiset.Entry<E>> entryIterator();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract int distinctElements();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean equals(@CheckForNull Object object) {
/* 211 */     return Multisets.equalsImpl(this, object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/* 221 */     return entrySet().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String toString() {
/* 232 */     return entrySet().toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/AbstractMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */