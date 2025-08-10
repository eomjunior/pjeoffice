/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import java.util.Comparator;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ import java.util.function.ObjIntConsumer;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtIncompatible
/*     */ final class RegularImmutableSortedMultiset<E>
/*     */   extends ImmutableSortedMultiset<E>
/*     */ {
/*  38 */   private static final long[] ZERO_CUMULATIVE_COUNTS = new long[] { 0L };
/*     */   
/*  40 */   static final ImmutableSortedMultiset<Comparable> NATURAL_EMPTY_MULTISET = new RegularImmutableSortedMultiset(
/*  41 */       Ordering.natural());
/*     */   @VisibleForTesting
/*     */   final transient RegularImmutableSortedSet<E> elementSet;
/*     */   private final transient long[] cumulativeCounts;
/*     */   private final transient int offset;
/*     */   private final transient int length;
/*     */   
/*     */   RegularImmutableSortedMultiset(Comparator<? super E> comparator) {
/*  49 */     this.elementSet = ImmutableSortedSet.emptySet(comparator);
/*  50 */     this.cumulativeCounts = ZERO_CUMULATIVE_COUNTS;
/*  51 */     this.offset = 0;
/*  52 */     this.length = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   RegularImmutableSortedMultiset(RegularImmutableSortedSet<E> elementSet, long[] cumulativeCounts, int offset, int length) {
/*  57 */     this.elementSet = elementSet;
/*  58 */     this.cumulativeCounts = cumulativeCounts;
/*  59 */     this.offset = offset;
/*  60 */     this.length = length;
/*     */   }
/*     */   
/*     */   private int getCount(int index) {
/*  64 */     return (int)(this.cumulativeCounts[this.offset + index + 1] - this.cumulativeCounts[this.offset + index]);
/*     */   }
/*     */ 
/*     */   
/*     */   Multiset.Entry<E> getEntry(int index) {
/*  69 */     return Multisets.immutableEntry(this.elementSet.asList().get(index), getCount(index));
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEachEntry(ObjIntConsumer<? super E> action) {
/*  74 */     Preconditions.checkNotNull(action);
/*  75 */     for (int i = 0; i < this.length; i++) {
/*  76 */       action.accept(this.elementSet.asList().get(i), getCount(i));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Multiset.Entry<E> firstEntry() {
/*  83 */     return isEmpty() ? null : getEntry(0);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Multiset.Entry<E> lastEntry() {
/*  89 */     return isEmpty() ? null : getEntry(this.length - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public int count(@CheckForNull Object element) {
/*  94 */     int index = this.elementSet.indexOf(element);
/*  95 */     return (index >= 0) ? getCount(index) : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 100 */     long size = this.cumulativeCounts[this.offset + this.length] - this.cumulativeCounts[this.offset];
/* 101 */     return Ints.saturatedCast(size);
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSortedSet<E> elementSet() {
/* 106 */     return this.elementSet;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSortedMultiset<E> headMultiset(E upperBound, BoundType boundType) {
/* 111 */     return getSubMultiset(0, this.elementSet.headIndex(upperBound, (Preconditions.checkNotNull(boundType) == BoundType.CLOSED)));
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSortedMultiset<E> tailMultiset(E lowerBound, BoundType boundType) {
/* 116 */     return getSubMultiset(this.elementSet
/* 117 */         .tailIndex(lowerBound, (Preconditions.checkNotNull(boundType) == BoundType.CLOSED)), this.length);
/*     */   }
/*     */   
/*     */   ImmutableSortedMultiset<E> getSubMultiset(int from, int to) {
/* 121 */     Preconditions.checkPositionIndexes(from, to, this.length);
/* 122 */     if (from == to)
/* 123 */       return emptyMultiset(comparator()); 
/* 124 */     if (from == 0 && to == this.length) {
/* 125 */       return this;
/*     */     }
/* 127 */     RegularImmutableSortedSet<E> subElementSet = this.elementSet.getSubSet(from, to);
/* 128 */     return new RegularImmutableSortedMultiset(subElementSet, this.cumulativeCounts, this.offset + from, to - from);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 135 */     return (this.offset > 0 || this.length < this.cumulativeCounts.length - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   Object writeReplace() {
/* 143 */     return super.writeReplace();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/RegularImmutableSortedMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */