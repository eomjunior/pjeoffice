/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.Set;
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
/*     */ @GwtIncompatible
/*     */ public abstract class ForwardingNavigableSet<E>
/*     */   extends ForwardingSortedSet<E>
/*     */   implements NavigableSet<E>
/*     */ {
/*     */   @CheckForNull
/*     */   public E lower(@ParametricNullness E e) {
/*  66 */     return delegate().lower(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   protected E standardLower(@ParametricNullness E e) {
/*  76 */     return Iterators.getNext(headSet(e, false).descendingIterator(), null);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public E floor(@ParametricNullness E e) {
/*  82 */     return delegate().floor(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   protected E standardFloor(@ParametricNullness E e) {
/*  92 */     return Iterators.getNext(headSet(e, true).descendingIterator(), null);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public E ceiling(@ParametricNullness E e) {
/*  98 */     return delegate().ceiling(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   protected E standardCeiling(@ParametricNullness E e) {
/* 108 */     return Iterators.getNext(tailSet(e, true).iterator(), null);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public E higher(@ParametricNullness E e) {
/* 114 */     return delegate().higher(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   protected E standardHigher(@ParametricNullness E e) {
/* 124 */     return Iterators.getNext(tailSet(e, false).iterator(), null);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public E pollFirst() {
/* 130 */     return delegate().pollFirst();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   protected E standardPollFirst() {
/* 140 */     return Iterators.pollNext(iterator());
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public E pollLast() {
/* 146 */     return delegate().pollLast();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   protected E standardPollLast() {
/* 156 */     return Iterators.pollNext(descendingIterator());
/*     */   }
/*     */   
/*     */   @ParametricNullness
/*     */   protected E standardFirst() {
/* 161 */     return iterator().next();
/*     */   }
/*     */   
/*     */   @ParametricNullness
/*     */   protected E standardLast() {
/* 166 */     return descendingIterator().next();
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<E> descendingSet() {
/* 171 */     return delegate().descendingSet();
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
/*     */   protected class StandardDescendingSet
/*     */     extends Sets.DescendingSet<E>
/*     */   {
/*     */     public StandardDescendingSet(ForwardingNavigableSet<E> this$0) {
/* 186 */       super(this$0);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> descendingIterator() {
/* 192 */     return delegate().descendingIterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NavigableSet<E> subSet(@ParametricNullness E fromElement, boolean fromInclusive, @ParametricNullness E toElement, boolean toInclusive) {
/* 201 */     return delegate().subSet(fromElement, fromInclusive, toElement, toInclusive);
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
/*     */   protected NavigableSet<E> standardSubSet(@ParametricNullness E fromElement, boolean fromInclusive, @ParametricNullness E toElement, boolean toInclusive) {
/* 214 */     return tailSet(fromElement, fromInclusive).headSet(toElement, toInclusive);
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
/*     */   protected SortedSet<E> standardSubSet(@ParametricNullness E fromElement, @ParametricNullness E toElement) {
/* 226 */     return subSet(fromElement, true, toElement, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<E> headSet(@ParametricNullness E toElement, boolean inclusive) {
/* 231 */     return delegate().headSet(toElement, inclusive);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SortedSet<E> standardHeadSet(@ParametricNullness E toElement) {
/* 240 */     return headSet(toElement, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<E> tailSet(@ParametricNullness E fromElement, boolean inclusive) {
/* 245 */     return delegate().tailSet(fromElement, inclusive);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SortedSet<E> standardTailSet(@ParametricNullness E fromElement) {
/* 254 */     return tailSet(fromElement, true);
/*     */   }
/*     */   
/*     */   protected abstract NavigableSet<E> delegate();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ForwardingNavigableSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */