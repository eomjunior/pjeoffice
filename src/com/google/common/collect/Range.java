/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.io.Serializable;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable(containerOf = {"C"})
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ public final class Range<C extends Comparable>
/*     */   extends RangeGwtSerializationDependencies
/*     */   implements Predicate<C>, Serializable
/*     */ {
/*     */   static class LowerBoundFn
/*     */     implements Function<Range, Cut>
/*     */   {
/* 129 */     static final LowerBoundFn INSTANCE = new LowerBoundFn();
/*     */ 
/*     */     
/*     */     public Cut apply(Range range) {
/* 133 */       return range.lowerBound;
/*     */     }
/*     */   }
/*     */   
/*     */   static class UpperBoundFn implements Function<Range, Cut> {
/* 138 */     static final UpperBoundFn INSTANCE = new UpperBoundFn();
/*     */ 
/*     */     
/*     */     public Cut apply(Range range) {
/* 142 */       return range.upperBound;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static <C extends Comparable<?>> Function<Range<C>, Cut<C>> lowerBoundFn() {
/* 148 */     return LowerBoundFn.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   static <C extends Comparable<?>> Function<Range<C>, Cut<C>> upperBoundFn() {
/* 153 */     return UpperBoundFn.INSTANCE;
/*     */   }
/*     */   
/*     */   static <C extends Comparable<?>> Ordering<Range<C>> rangeLexOrdering() {
/* 157 */     return (Ordering)RangeLexOrdering.INSTANCE;
/*     */   }
/*     */   
/*     */   static <C extends Comparable<?>> Range<C> create(Cut<C> lowerBound, Cut<C> upperBound) {
/* 161 */     return (Range)new Range<>(lowerBound, upperBound);
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
/*     */   public static <C extends Comparable<?>> Range<C> open(C lower, C upper) {
/* 174 */     return create((Cut)Cut.aboveValue((Comparable)lower), (Cut)Cut.belowValue((Comparable)upper));
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
/*     */   public static <C extends Comparable<?>> Range<C> closed(C lower, C upper) {
/* 186 */     return create((Cut)Cut.belowValue((Comparable)lower), (Cut)Cut.aboveValue((Comparable)upper));
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
/*     */   public static <C extends Comparable<?>> Range<C> closedOpen(C lower, C upper) {
/* 198 */     return create((Cut)Cut.belowValue((Comparable)lower), (Cut)Cut.belowValue((Comparable)upper));
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
/*     */   public static <C extends Comparable<?>> Range<C> openClosed(C lower, C upper) {
/* 210 */     return create((Cut)Cut.aboveValue((Comparable)lower), (Cut)Cut.aboveValue((Comparable)upper));
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
/*     */   public static <C extends Comparable<?>> Range<C> range(C lower, BoundType lowerType, C upper, BoundType upperType) {
/* 223 */     Preconditions.checkNotNull(lowerType);
/* 224 */     Preconditions.checkNotNull(upperType);
/*     */ 
/*     */     
/* 227 */     Cut<C> lowerBound = (lowerType == BoundType.OPEN) ? (Cut)Cut.<Comparable>aboveValue((Comparable)lower) : (Cut)Cut.<Comparable>belowValue((Comparable)lower);
/*     */     
/* 229 */     Cut<C> upperBound = (upperType == BoundType.OPEN) ? (Cut)Cut.<Comparable>belowValue((Comparable)upper) : (Cut)Cut.<Comparable>aboveValue((Comparable)upper);
/* 230 */     return create(lowerBound, upperBound);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> lessThan(C endpoint) {
/* 239 */     return create((Cut)Cut.belowAll(), (Cut)Cut.belowValue((Comparable)endpoint));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> atMost(C endpoint) {
/* 248 */     return create((Cut)Cut.belowAll(), (Cut)Cut.aboveValue((Comparable)endpoint));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> upTo(C endpoint, BoundType boundType) {
/* 258 */     switch (boundType) {
/*     */       case OPEN:
/* 260 */         return lessThan(endpoint);
/*     */       case CLOSED:
/* 262 */         return atMost(endpoint);
/*     */     } 
/* 264 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> greaterThan(C endpoint) {
/* 274 */     return create((Cut)Cut.aboveValue((Comparable)endpoint), (Cut)Cut.aboveAll());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> atLeast(C endpoint) {
/* 283 */     return create((Cut)Cut.belowValue((Comparable)endpoint), (Cut)Cut.aboveAll());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> downTo(C endpoint, BoundType boundType) {
/* 293 */     switch (boundType) {
/*     */       case OPEN:
/* 295 */         return greaterThan(endpoint);
/*     */       case CLOSED:
/* 297 */         return atLeast(endpoint);
/*     */     } 
/* 299 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */   
/* 303 */   private static final Range<Comparable> ALL = new Range((Cut)Cut.belowAll(), (Cut)Cut.aboveAll());
/*     */   
/*     */   final Cut<C> lowerBound;
/*     */   
/*     */   final Cut<C> upperBound;
/*     */   
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> all() {
/* 312 */     return (Range)ALL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> singleton(C value) {
/* 322 */     return closed(value, value);
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
/*     */   public static <C extends Comparable<?>> Range<C> encloseAll(Iterable<C> values) {
/* 335 */     Preconditions.checkNotNull(values);
/* 336 */     if (values instanceof SortedSet) {
/* 337 */       SortedSet<C> set = (SortedSet<C>)values;
/* 338 */       Comparator<?> comparator = set.comparator();
/* 339 */       if (Ordering.<Comparable>natural().equals(comparator) || comparator == null) {
/* 340 */         return closed(set.first(), set.last());
/*     */       }
/*     */     } 
/* 343 */     Iterator<C> valueIterator = values.iterator();
/* 344 */     Comparable comparable1 = (Comparable)Preconditions.checkNotNull((Comparable)valueIterator.next());
/* 345 */     Comparable comparable2 = comparable1;
/* 346 */     while (valueIterator.hasNext()) {
/* 347 */       Comparable comparable = (Comparable)Preconditions.checkNotNull((Comparable)valueIterator.next());
/* 348 */       comparable1 = (Comparable)Ordering.<Comparable>natural().min(comparable1, comparable);
/* 349 */       comparable2 = (Comparable)Ordering.<Comparable>natural().max(comparable2, comparable);
/*     */     } 
/* 351 */     return closed((C)comparable1, (C)comparable2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Range(Cut<C> lowerBound, Cut<C> upperBound) {
/* 358 */     this.lowerBound = (Cut<C>)Preconditions.checkNotNull(lowerBound);
/* 359 */     this.upperBound = (Cut<C>)Preconditions.checkNotNull(upperBound);
/* 360 */     if (lowerBound.compareTo(upperBound) > 0 || lowerBound == 
/* 361 */       Cut.aboveAll() || upperBound == 
/* 362 */       Cut.belowAll()) {
/* 363 */       throw new IllegalArgumentException("Invalid range: " + toString(lowerBound, upperBound));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasLowerBound() {
/* 369 */     return (this.lowerBound != Cut.belowAll());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public C lowerEndpoint() {
/* 379 */     return this.lowerBound.endpoint();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundType lowerBoundType() {
/* 390 */     return this.lowerBound.typeAsLowerBound();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasUpperBound() {
/* 395 */     return (this.upperBound != Cut.aboveAll());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public C upperEndpoint() {
/* 405 */     return this.upperBound.endpoint();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundType upperBoundType() {
/* 416 */     return this.upperBound.typeAsUpperBound();
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
/*     */   public boolean isEmpty() {
/* 429 */     return this.lowerBound.equals(this.upperBound);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(C value) {
/* 438 */     Preconditions.checkNotNull(value);
/*     */     
/* 440 */     return (this.lowerBound.isLessThan(value) && !this.upperBound.isLessThan(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public boolean apply(C input) {
/* 450 */     return contains(input);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsAll(Iterable<? extends C> values) {
/* 458 */     if (Iterables.isEmpty(values)) {
/* 459 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 463 */     if (values instanceof SortedSet) {
/* 464 */       SortedSet<? extends C> set = (SortedSet<? extends C>)values;
/* 465 */       Comparator<?> comparator = set.comparator();
/* 466 */       if (Ordering.<Comparable>natural().equals(comparator) || comparator == null) {
/* 467 */         return (contains(set.first()) && contains(set.last()));
/*     */       }
/*     */     } 
/*     */     
/* 471 */     for (Comparable comparable : values) {
/* 472 */       if (!contains((C)comparable)) {
/* 473 */         return false;
/*     */       }
/*     */     } 
/* 476 */     return true;
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
/*     */   public boolean encloses(Range<C> other) {
/* 503 */     return (this.lowerBound.compareTo(other.lowerBound) <= 0 && this.upperBound
/* 504 */       .compareTo(other.upperBound) >= 0);
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
/*     */   public boolean isConnected(Range<C> other) {
/* 533 */     return (this.lowerBound.compareTo(other.upperBound) <= 0 && other.lowerBound
/* 534 */       .compareTo(this.upperBound) <= 0);
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
/*     */   public Range<C> intersection(Range<C> connectedRange) {
/* 554 */     int lowerCmp = this.lowerBound.compareTo(connectedRange.lowerBound);
/* 555 */     int upperCmp = this.upperBound.compareTo(connectedRange.upperBound);
/* 556 */     if (lowerCmp >= 0 && upperCmp <= 0)
/* 557 */       return this; 
/* 558 */     if (lowerCmp <= 0 && upperCmp >= 0) {
/* 559 */       return connectedRange;
/*     */     }
/* 561 */     Cut<C> newLower = (lowerCmp >= 0) ? this.lowerBound : connectedRange.lowerBound;
/* 562 */     Cut<C> newUpper = (upperCmp <= 0) ? this.upperBound : connectedRange.upperBound;
/*     */ 
/*     */     
/* 565 */     Preconditions.checkArgument(
/* 566 */         (newLower.compareTo(newUpper) <= 0), "intersection is undefined for disconnected ranges %s and %s", this, connectedRange);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 572 */     return (Range)create(newLower, newUpper);
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
/*     */   public Range<C> gap(Range<C> otherRange) {
/* 604 */     if (this.lowerBound.compareTo(otherRange.upperBound) < 0 && otherRange.lowerBound
/* 605 */       .compareTo(this.upperBound) < 0) {
/* 606 */       throw new IllegalArgumentException("Ranges have a nonempty intersection: " + this + ", " + otherRange);
/*     */     }
/*     */ 
/*     */     
/* 610 */     boolean isThisFirst = (this.lowerBound.compareTo(otherRange.lowerBound) < 0);
/* 611 */     Range<C> firstRange = isThisFirst ? this : otherRange;
/* 612 */     Range<C> secondRange = isThisFirst ? otherRange : this;
/* 613 */     return (Range)create(firstRange.upperBound, secondRange.lowerBound);
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
/*     */   public Range<C> span(Range<C> other) {
/* 628 */     int lowerCmp = this.lowerBound.compareTo(other.lowerBound);
/* 629 */     int upperCmp = this.upperBound.compareTo(other.upperBound);
/* 630 */     if (lowerCmp <= 0 && upperCmp >= 0)
/* 631 */       return this; 
/* 632 */     if (lowerCmp >= 0 && upperCmp <= 0) {
/* 633 */       return other;
/*     */     }
/* 635 */     Cut<C> newLower = (lowerCmp <= 0) ? this.lowerBound : other.lowerBound;
/* 636 */     Cut<C> newUpper = (upperCmp >= 0) ? this.upperBound : other.upperBound;
/* 637 */     return (Range)create(newLower, newUpper);
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
/*     */   public Range<C> canonical(DiscreteDomain<C> domain) {
/* 666 */     Preconditions.checkNotNull(domain);
/* 667 */     Cut<C> lower = this.lowerBound.canonical(domain);
/* 668 */     Cut<C> upper = this.upperBound.canonical(domain);
/* 669 */     return (lower == this.lowerBound && upper == this.upperBound) ? this : (Range)create(lower, upper);
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
/*     */   public boolean equals(@CheckForNull Object object) {
/* 681 */     if (object instanceof Range) {
/* 682 */       Range<?> other = (Range)object;
/* 683 */       return (this.lowerBound.equals(other.lowerBound) && this.upperBound.equals(other.upperBound));
/*     */     } 
/* 685 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 691 */     return this.lowerBound.hashCode() * 31 + this.upperBound.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 700 */     return toString(this.lowerBound, this.upperBound);
/*     */   }
/*     */   
/*     */   private static String toString(Cut<?> lowerBound, Cut<?> upperBound) {
/* 704 */     StringBuilder sb = new StringBuilder(16);
/* 705 */     lowerBound.describeAsLowerBound(sb);
/* 706 */     sb.append("..");
/* 707 */     upperBound.describeAsUpperBound(sb);
/* 708 */     return sb.toString();
/*     */   }
/*     */   
/*     */   Object readResolve() {
/* 712 */     if (equals(ALL)) {
/* 713 */       return all();
/*     */     }
/* 715 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static int compareOrThrow(Comparable<Comparable> left, Comparable right) {
/* 721 */     return left.compareTo(right);
/*     */   }
/*     */   
/*     */   private static class RangeLexOrdering
/*     */     extends Ordering<Range<?>> implements Serializable {
/* 726 */     static final Ordering<Range<?>> INSTANCE = new RangeLexOrdering();
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public int compare(Range<?> left, Range<?> right) {
/* 730 */       return ComparisonChain.start()
/* 731 */         .compare(left.lowerBound, right.lowerBound)
/* 732 */         .compare(left.upperBound, right.upperBound)
/* 733 */         .result();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/Range.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */