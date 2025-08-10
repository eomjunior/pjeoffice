/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.DoNotCall;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Objects;
/*     */ import java.util.SortedSet;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public abstract class ContiguousSet<C extends Comparable>
/*     */   extends ImmutableSortedSet<C>
/*     */ {
/*     */   final DiscreteDomain<C> domain;
/*     */   
/*     */   public static <C extends Comparable> ContiguousSet<C> create(Range<C> range, DiscreteDomain<C> domain) {
/*     */     boolean empty;
/*  65 */     Preconditions.checkNotNull(range);
/*  66 */     Preconditions.checkNotNull(domain);
/*  67 */     Range<C> effectiveRange = range;
/*     */     try {
/*  69 */       if (!range.hasLowerBound()) {
/*  70 */         effectiveRange = effectiveRange.intersection((Range)Range.atLeast((Comparable<?>)domain.minValue()));
/*     */       }
/*  72 */       if (!range.hasUpperBound()) {
/*  73 */         effectiveRange = effectiveRange.intersection((Range)Range.atMost((Comparable<?>)domain.maxValue()));
/*     */       }
/*  75 */     } catch (NoSuchElementException e) {
/*  76 */       throw new IllegalArgumentException(e);
/*     */     } 
/*     */ 
/*     */     
/*  80 */     if (effectiveRange.isEmpty()) {
/*  81 */       empty = true;
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/*  87 */       Comparable comparable1 = Objects.<Comparable>requireNonNull((Comparable)range.lowerBound.leastValueAbove(domain));
/*  88 */       Comparable comparable2 = Objects.<Comparable>requireNonNull((Comparable)range.upperBound.greatestValueBelow(domain));
/*     */       
/*  90 */       empty = (Range.compareOrThrow(comparable1, comparable2) > 0);
/*     */     } 
/*     */     
/*  93 */     return empty ? 
/*  94 */       new EmptyContiguousSet<>(domain) : 
/*  95 */       new RegularContiguousSet<>(effectiveRange, domain);
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
/*     */   public static ContiguousSet<Integer> closed(int lower, int upper) {
/* 107 */     return create(Range.closed(Integer.valueOf(lower), Integer.valueOf(upper)), DiscreteDomain.integers());
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
/*     */   public static ContiguousSet<Long> closed(long lower, long upper) {
/* 119 */     return create(Range.closed(Long.valueOf(lower), Long.valueOf(upper)), DiscreteDomain.longs());
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
/*     */   public static ContiguousSet<Integer> closedOpen(int lower, int upper) {
/* 131 */     return create(Range.closedOpen(Integer.valueOf(lower), Integer.valueOf(upper)), DiscreteDomain.integers());
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
/*     */   public static ContiguousSet<Long> closedOpen(long lower, long upper) {
/* 143 */     return create(Range.closedOpen(Long.valueOf(lower), Long.valueOf(upper)), DiscreteDomain.longs());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   ContiguousSet(DiscreteDomain<C> domain) {
/* 149 */     super(Ordering.natural());
/* 150 */     this.domain = domain;
/*     */   }
/*     */ 
/*     */   
/*     */   public ContiguousSet<C> headSet(C toElement) {
/* 155 */     return headSetImpl((C)Preconditions.checkNotNull(toElement), false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public ContiguousSet<C> headSet(C toElement, boolean inclusive) {
/* 162 */     return headSetImpl((C)Preconditions.checkNotNull(toElement), inclusive);
/*     */   }
/*     */ 
/*     */   
/*     */   public ContiguousSet<C> subSet(C fromElement, C toElement) {
/* 167 */     Preconditions.checkNotNull(fromElement);
/* 168 */     Preconditions.checkNotNull(toElement);
/* 169 */     Preconditions.checkArgument((comparator().compare(fromElement, toElement) <= 0));
/* 170 */     return subSetImpl(fromElement, true, toElement, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public ContiguousSet<C> subSet(C fromElement, boolean fromInclusive, C toElement, boolean toInclusive) {
/* 178 */     Preconditions.checkNotNull(fromElement);
/* 179 */     Preconditions.checkNotNull(toElement);
/* 180 */     Preconditions.checkArgument((comparator().compare(fromElement, toElement) <= 0));
/* 181 */     return subSetImpl(fromElement, fromInclusive, toElement, toInclusive);
/*     */   }
/*     */ 
/*     */   
/*     */   public ContiguousSet<C> tailSet(C fromElement) {
/* 186 */     return tailSetImpl((C)Preconditions.checkNotNull(fromElement), true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public ContiguousSet<C> tailSet(C fromElement, boolean inclusive) {
/* 193 */     return tailSetImpl((C)Preconditions.checkNotNull(fromElement), inclusive);
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
/*     */   @GwtIncompatible
/*     */   ImmutableSortedSet<C> createDescendingSet() {
/* 241 */     return new DescendingImmutableSortedSet<>(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 247 */     return range().toString();
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
/*     */   @Deprecated
/*     */   @DoNotCall("Always throws UnsupportedOperationException")
/*     */   public static <E> ImmutableSortedSet.Builder<E> builder() {
/* 261 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   Object writeReplace() {
/* 270 */     return super.writeReplace();
/*     */   }
/*     */   
/*     */   abstract ContiguousSet<C> headSetImpl(C paramC, boolean paramBoolean);
/*     */   
/*     */   abstract ContiguousSet<C> subSetImpl(C paramC1, boolean paramBoolean1, C paramC2, boolean paramBoolean2);
/*     */   
/*     */   abstract ContiguousSet<C> tailSetImpl(C paramC, boolean paramBoolean);
/*     */   
/*     */   public abstract ContiguousSet<C> intersection(ContiguousSet<C> paramContiguousSet);
/*     */   
/*     */   public abstract Range<C> range();
/*     */   
/*     */   public abstract Range<C> range(BoundType paramBoundType1, BoundType paramBoundType2);
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ContiguousSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */