/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(emulated = true)
/*     */ public abstract class ForwardingSortedMultiset<E>
/*     */   extends ForwardingMultiset<E>
/*     */   implements SortedMultiset<E>
/*     */ {
/*     */   public NavigableSet<E> elementSet() {
/*  58 */     return delegate().elementSet();
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected class StandardElementSet
/*     */     extends SortedMultisets.NavigableElementSet<E>
/*     */   {
/*     */     public StandardElementSet(ForwardingSortedMultiset<E> this$0) {
/*  77 */       super(this$0);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Comparator<? super E> comparator() {
/*  83 */     return delegate().comparator();
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> descendingMultiset() {
/*  88 */     return delegate().descendingMultiset();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract class StandardDescendingMultiset
/*     */     extends DescendingMultiset<E>
/*     */   {
/*     */     SortedMultiset<E> forwardMultiset() {
/* 109 */       return ForwardingSortedMultiset.this;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Multiset.Entry<E> firstEntry() {
/* 116 */     return delegate().firstEntry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   protected Multiset.Entry<E> standardFirstEntry() {
/* 127 */     Iterator<Multiset.Entry<E>> entryIterator = entrySet().iterator();
/* 128 */     if (!entryIterator.hasNext()) {
/* 129 */       return null;
/*     */     }
/* 131 */     Multiset.Entry<E> entry = entryIterator.next();
/* 132 */     return Multisets.immutableEntry(entry.getElement(), entry.getCount());
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Multiset.Entry<E> lastEntry() {
/* 138 */     return delegate().lastEntry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   protected Multiset.Entry<E> standardLastEntry() {
/* 150 */     Iterator<Multiset.Entry<E>> entryIterator = descendingMultiset().entrySet().iterator();
/* 151 */     if (!entryIterator.hasNext()) {
/* 152 */       return null;
/*     */     }
/* 154 */     Multiset.Entry<E> entry = entryIterator.next();
/* 155 */     return Multisets.immutableEntry(entry.getElement(), entry.getCount());
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Multiset.Entry<E> pollFirstEntry() {
/* 161 */     return delegate().pollFirstEntry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   protected Multiset.Entry<E> standardPollFirstEntry() {
/* 172 */     Iterator<Multiset.Entry<E>> entryIterator = entrySet().iterator();
/* 173 */     if (!entryIterator.hasNext()) {
/* 174 */       return null;
/*     */     }
/* 176 */     Multiset.Entry<E> entry = entryIterator.next();
/* 177 */     entry = Multisets.immutableEntry(entry.getElement(), entry.getCount());
/* 178 */     entryIterator.remove();
/* 179 */     return entry;
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Multiset.Entry<E> pollLastEntry() {
/* 185 */     return delegate().pollLastEntry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   protected Multiset.Entry<E> standardPollLastEntry() {
/* 197 */     Iterator<Multiset.Entry<E>> entryIterator = descendingMultiset().entrySet().iterator();
/* 198 */     if (!entryIterator.hasNext()) {
/* 199 */       return null;
/*     */     }
/* 201 */     Multiset.Entry<E> entry = entryIterator.next();
/* 202 */     entry = Multisets.immutableEntry(entry.getElement(), entry.getCount());
/* 203 */     entryIterator.remove();
/* 204 */     return entry;
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> headMultiset(@ParametricNullness E upperBound, BoundType boundType) {
/* 209 */     return delegate().headMultiset(upperBound, boundType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> subMultiset(@ParametricNullness E lowerBound, BoundType lowerBoundType, @ParametricNullness E upperBound, BoundType upperBoundType) {
/* 218 */     return delegate().subMultiset(lowerBound, lowerBoundType, upperBound, upperBoundType);
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected SortedMultiset<E> standardSubMultiset(@ParametricNullness E lowerBound, BoundType lowerBoundType, @ParametricNullness E upperBound, BoundType upperBoundType) {
/* 234 */     return tailMultiset(lowerBound, lowerBoundType).headMultiset(upperBound, upperBoundType);
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> tailMultiset(@ParametricNullness E lowerBound, BoundType boundType) {
/* 239 */     return delegate().tailMultiset(lowerBound, boundType);
/*     */   }
/*     */   
/*     */   protected abstract SortedMultiset<E> delegate();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ForwardingSortedMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */