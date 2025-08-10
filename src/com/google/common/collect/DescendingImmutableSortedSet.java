/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import java.util.Iterator;
/*     */ import java.util.NavigableSet;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtIncompatible
/*     */ final class DescendingImmutableSortedSet<E>
/*     */   extends ImmutableSortedSet<E>
/*     */ {
/*     */   private final ImmutableSortedSet<E> forward;
/*     */   
/*     */   DescendingImmutableSortedSet(ImmutableSortedSet<E> forward) {
/*  34 */     super(Ordering.from(forward.comparator()).reverse());
/*  35 */     this.forward = forward;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(@CheckForNull Object object) {
/*  40 */     return this.forward.contains(object);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  45 */     return this.forward.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public UnmodifiableIterator<E> iterator() {
/*  50 */     return this.forward.descendingIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSortedSet<E> headSetImpl(E toElement, boolean inclusive) {
/*  55 */     return this.forward.tailSet(toElement, inclusive).descendingSet();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableSortedSet<E> subSetImpl(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/*  61 */     return this.forward.subSet(toElement, toInclusive, fromElement, fromInclusive).descendingSet();
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSortedSet<E> tailSetImpl(E fromElement, boolean inclusive) {
/*  66 */     return this.forward.headSet(fromElement, inclusive).descendingSet();
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible("NavigableSet")
/*     */   public ImmutableSortedSet<E> descendingSet() {
/*  72 */     return this.forward;
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible("NavigableSet")
/*     */   public UnmodifiableIterator<E> descendingIterator() {
/*  78 */     return this.forward.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible("NavigableSet")
/*     */   ImmutableSortedSet<E> createDescendingSet() {
/*  84 */     throw new AssertionError("should never be called");
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public E lower(E element) {
/*  90 */     return this.forward.higher(element);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public E floor(E element) {
/*  96 */     return this.forward.ceiling(element);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public E ceiling(E element) {
/* 102 */     return this.forward.floor(element);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public E higher(E element) {
/* 108 */     return this.forward.lower(element);
/*     */   }
/*     */ 
/*     */   
/*     */   int indexOf(@CheckForNull Object target) {
/* 113 */     int index = this.forward.indexOf(target);
/* 114 */     if (index == -1) {
/* 115 */       return index;
/*     */     }
/* 117 */     return size() - 1 - index;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 123 */     return this.forward.isPartialView();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   Object writeReplace() {
/* 131 */     return super.writeReplace();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/DescendingImmutableSortedSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */