/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.j2objc.annotations.Weak;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.SortedSet;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(emulated = true)
/*     */ final class SortedMultisets
/*     */ {
/*     */   static class ElementSet<E>
/*     */     extends Multisets.ElementSet<E>
/*     */     implements SortedSet<E>
/*     */   {
/*     */     @Weak
/*     */     private final SortedMultiset<E> multiset;
/*     */     
/*     */     ElementSet(SortedMultiset<E> multiset) {
/*  51 */       this.multiset = multiset;
/*     */     }
/*     */ 
/*     */     
/*     */     final SortedMultiset<E> multiset() {
/*  56 */       return this.multiset;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<E> iterator() {
/*  61 */       return Multisets.elementIterator(multiset().entrySet().iterator());
/*     */     }
/*     */ 
/*     */     
/*     */     public Comparator<? super E> comparator() {
/*  66 */       return multiset().comparator();
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedSet<E> subSet(@ParametricNullness E fromElement, @ParametricNullness E toElement) {
/*  71 */       return multiset().subMultiset(fromElement, BoundType.CLOSED, toElement, BoundType.OPEN).elementSet();
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedSet<E> headSet(@ParametricNullness E toElement) {
/*  76 */       return multiset().headMultiset(toElement, BoundType.OPEN).elementSet();
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedSet<E> tailSet(@ParametricNullness E fromElement) {
/*  81 */       return multiset().tailMultiset(fromElement, BoundType.CLOSED).elementSet();
/*     */     }
/*     */ 
/*     */     
/*     */     @ParametricNullness
/*     */     public E first() {
/*  87 */       return SortedMultisets.getElementOrThrow(multiset().firstEntry());
/*     */     }
/*     */ 
/*     */     
/*     */     @ParametricNullness
/*     */     public E last() {
/*  93 */       return SortedMultisets.getElementOrThrow(multiset().lastEntry());
/*     */     }
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   static class NavigableElementSet<E>
/*     */     extends ElementSet<E>
/*     */     implements NavigableSet<E> {
/*     */     NavigableElementSet(SortedMultiset<E> multiset) {
/* 102 */       super(multiset);
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public E lower(@ParametricNullness E e) {
/* 108 */       return SortedMultisets.getElementOrNull(multiset().headMultiset(e, BoundType.OPEN).lastEntry());
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public E floor(@ParametricNullness E e) {
/* 114 */       return SortedMultisets.getElementOrNull(multiset().headMultiset(e, BoundType.CLOSED).lastEntry());
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public E ceiling(@ParametricNullness E e) {
/* 120 */       return SortedMultisets.getElementOrNull(multiset().tailMultiset(e, BoundType.CLOSED).firstEntry());
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public E higher(@ParametricNullness E e) {
/* 126 */       return SortedMultisets.getElementOrNull(multiset().tailMultiset(e, BoundType.OPEN).firstEntry());
/*     */     }
/*     */ 
/*     */     
/*     */     public NavigableSet<E> descendingSet() {
/* 131 */       return new NavigableElementSet(multiset().descendingMultiset());
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<E> descendingIterator() {
/* 136 */       return descendingSet().iterator();
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public E pollFirst() {
/* 142 */       return SortedMultisets.getElementOrNull(multiset().pollFirstEntry());
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public E pollLast() {
/* 148 */       return SortedMultisets.getElementOrNull(multiset().pollLastEntry());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public NavigableSet<E> subSet(@ParametricNullness E fromElement, boolean fromInclusive, @ParametricNullness E toElement, boolean toInclusive) {
/* 157 */       return new NavigableElementSet(
/* 158 */           multiset()
/* 159 */           .subMultiset(fromElement, 
/* 160 */             BoundType.forBoolean(fromInclusive), toElement, 
/* 161 */             BoundType.forBoolean(toInclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public NavigableSet<E> headSet(@ParametricNullness E toElement, boolean inclusive) {
/* 166 */       return new NavigableElementSet(
/* 167 */           multiset().headMultiset(toElement, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public NavigableSet<E> tailSet(@ParametricNullness E fromElement, boolean inclusive) {
/* 172 */       return new NavigableElementSet(
/* 173 */           multiset().tailMultiset(fromElement, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */   }
/*     */   
/*     */   private static <E> E getElementOrThrow(@CheckForNull Multiset.Entry<E> entry) {
/* 178 */     if (entry == null) {
/* 179 */       throw new NoSuchElementException();
/*     */     }
/* 181 */     return entry.getElement();
/*     */   }
/*     */   
/*     */   @CheckForNull
/*     */   private static <E> E getElementOrNull(@CheckForNull Multiset.Entry<E> entry) {
/* 186 */     return (entry == null) ? null : entry.getElement();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/SortedMultisets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */