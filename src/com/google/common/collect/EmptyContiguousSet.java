/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(emulated = true)
/*     */ final class EmptyContiguousSet<C extends Comparable>
/*     */   extends ContiguousSet<C>
/*     */ {
/*     */   EmptyContiguousSet(DiscreteDomain<C> domain) {
/*  36 */     super(domain);
/*     */   }
/*     */ 
/*     */   
/*     */   public C first() {
/*  41 */     throw new NoSuchElementException();
/*     */   }
/*     */ 
/*     */   
/*     */   public C last() {
/*  46 */     throw new NoSuchElementException();
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  51 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public ContiguousSet<C> intersection(ContiguousSet<C> other) {
/*  56 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Range<C> range() {
/*  61 */     throw new NoSuchElementException();
/*     */   }
/*     */ 
/*     */   
/*     */   public Range<C> range(BoundType lowerBoundType, BoundType upperBoundType) {
/*  66 */     throw new NoSuchElementException();
/*     */   }
/*     */ 
/*     */   
/*     */   ContiguousSet<C> headSetImpl(C toElement, boolean inclusive) {
/*  71 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   ContiguousSet<C> subSetImpl(C fromElement, boolean fromInclusive, C toElement, boolean toInclusive) {
/*  77 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   ContiguousSet<C> tailSetImpl(C fromElement, boolean fromInclusive) {
/*  82 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(@CheckForNull Object object) {
/*  87 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   int indexOf(@CheckForNull Object target) {
/*  93 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public UnmodifiableIterator<C> iterator() {
/*  98 */     return Iterators.emptyIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public UnmodifiableIterator<C> descendingIterator() {
/* 104 */     return Iterators.emptyIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 109 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 114 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableList<C> asList() {
/* 119 */     return ImmutableList.of();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 124 */     return "[]";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@CheckForNull Object object) {
/* 129 */     if (object instanceof Set) {
/* 130 */       Set<?> that = (Set)object;
/* 131 */       return that.isEmpty();
/*     */     } 
/* 133 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   boolean isHashCodeFast() {
/* 139 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 144 */     return 0;
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   private static final class SerializedForm<C extends Comparable> implements Serializable {
/*     */     private final DiscreteDomain<C> domain;
/*     */     
/*     */     private SerializedForm(DiscreteDomain<C> domain) {
/* 153 */       this.domain = domain;
/*     */     }
/*     */     private static final long serialVersionUID = 0L;
/*     */     private Object readResolve() {
/* 157 */       return new EmptyContiguousSet<>(this.domain);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   Object writeReplace() {
/* 167 */     return new SerializedForm<>(this.domain);
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 173 */     throw new InvalidObjectException("Use SerializedForm");
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   ImmutableSortedSet<C> createDescendingSet() {
/* 179 */     return ImmutableSortedSet.emptySet(Ordering.<Comparable>natural().reverse());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/EmptyContiguousSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */