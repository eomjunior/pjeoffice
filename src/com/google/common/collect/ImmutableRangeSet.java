/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.DoNotCall;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collector;
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
/*     */ @GwtIncompatible
/*     */ public final class ImmutableRangeSet<C extends Comparable>
/*     */   extends AbstractRangeSet<C>
/*     */   implements Serializable
/*     */ {
/*  56 */   private static final ImmutableRangeSet<Comparable<?>> EMPTY = new ImmutableRangeSet(
/*  57 */       ImmutableList.of());
/*     */   
/*  59 */   private static final ImmutableRangeSet<Comparable<?>> ALL = new ImmutableRangeSet(
/*  60 */       ImmutableList.of((Range)Range.all()));
/*     */ 
/*     */   
/*     */   private final transient ImmutableList<Range<C>> ranges;
/*     */   
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   private transient ImmutableRangeSet<C> complement;
/*     */ 
/*     */   
/*     */   public static <E extends Comparable<? super E>> Collector<Range<E>, ?, ImmutableRangeSet<E>> toImmutableRangeSet() {
/*  71 */     return CollectCollectors.toImmutableRangeSet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable> ImmutableRangeSet<C> of() {
/*  81 */     return (ImmutableRangeSet)EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable> ImmutableRangeSet<C> of(Range<C> range) {
/*  89 */     Preconditions.checkNotNull(range);
/*  90 */     if (range.isEmpty())
/*  91 */       return of(); 
/*  92 */     if (range.equals(Range.all())) {
/*  93 */       return all();
/*     */     }
/*  95 */     return new ImmutableRangeSet<>(ImmutableList.of(range));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <C extends Comparable> ImmutableRangeSet<C> all() {
/* 102 */     return (ImmutableRangeSet)ALL;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <C extends Comparable> ImmutableRangeSet<C> copyOf(RangeSet<C> rangeSet) {
/* 107 */     Preconditions.checkNotNull(rangeSet);
/* 108 */     if (rangeSet.isEmpty())
/* 109 */       return of(); 
/* 110 */     if (rangeSet.encloses((Range)Range.all())) {
/* 111 */       return all();
/*     */     }
/*     */     
/* 114 */     if (rangeSet instanceof ImmutableRangeSet) {
/* 115 */       ImmutableRangeSet<C> immutableRangeSet = (ImmutableRangeSet<C>)rangeSet;
/* 116 */       if (!immutableRangeSet.isPartialView()) {
/* 117 */         return immutableRangeSet;
/*     */       }
/*     */     } 
/* 120 */     return new ImmutableRangeSet<>(ImmutableList.copyOf(rangeSet.asRanges()));
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
/*     */   public static <C extends Comparable<?>> ImmutableRangeSet<C> copyOf(Iterable<Range<C>> ranges) {
/* 132 */     return (new Builder<>()).addAll(ranges).build();
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
/*     */   public static <C extends Comparable<?>> ImmutableRangeSet<C> unionOf(Iterable<Range<C>> ranges) {
/* 144 */     return (ImmutableRangeSet)copyOf(TreeRangeSet.create(ranges));
/*     */   }
/*     */   
/*     */   ImmutableRangeSet(ImmutableList<Range<C>> ranges) {
/* 148 */     this.ranges = ranges;
/*     */   }
/*     */   
/*     */   private ImmutableRangeSet(ImmutableList<Range<C>> ranges, ImmutableRangeSet<C> complement) {
/* 152 */     this.ranges = ranges;
/* 153 */     this.complement = complement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean intersects(Range<C> otherRange) {
/* 161 */     int ceilingIndex = SortedLists.binarySearch(this.ranges, 
/*     */         
/* 163 */         (Function)Range.lowerBoundFn(), otherRange.lowerBound, 
/*     */         
/* 165 */         Ordering.natural(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
/*     */ 
/*     */     
/* 168 */     if (ceilingIndex < this.ranges.size() && ((Range<C>)this.ranges
/* 169 */       .get(ceilingIndex)).isConnected(otherRange) && 
/* 170 */       !((Range<C>)this.ranges.get(ceilingIndex)).intersection(otherRange).isEmpty()) {
/* 171 */       return true;
/*     */     }
/* 173 */     return (ceilingIndex > 0 && ((Range<C>)this.ranges
/* 174 */       .get(ceilingIndex - 1)).isConnected(otherRange) && 
/* 175 */       !((Range<C>)this.ranges.get(ceilingIndex - 1)).intersection(otherRange).isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean encloses(Range<C> otherRange) {
/* 181 */     int index = SortedLists.binarySearch(this.ranges, 
/*     */         
/* 183 */         (Function)Range.lowerBoundFn(), otherRange.lowerBound, 
/*     */         
/* 185 */         Ordering.natural(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
/*     */ 
/*     */     
/* 188 */     return (index != -1 && ((Range<C>)this.ranges.get(index)).encloses(otherRange));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Range<C> rangeContaining(C value) {
/* 195 */     int index = SortedLists.binarySearch(this.ranges, 
/*     */         
/* 197 */         (Function)Range.lowerBoundFn(), 
/* 198 */         Cut.belowValue(value), 
/* 199 */         Ordering.natural(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
/*     */ 
/*     */     
/* 202 */     if (index != -1) {
/* 203 */       Range<C> range = this.ranges.get(index);
/* 204 */       return range.contains(value) ? range : null;
/*     */     } 
/* 206 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Range<C> span() {
/* 211 */     if (this.ranges.isEmpty()) {
/* 212 */       throw new NoSuchElementException();
/*     */     }
/* 214 */     return (Range)Range.create(((Range)this.ranges.get(0)).lowerBound, ((Range)this.ranges.get(this.ranges.size() - 1)).upperBound);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 219 */     return this.ranges.isEmpty();
/*     */   }
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
/*     */   public void add(Range<C> range) {
/* 232 */     throw new UnsupportedOperationException();
/*     */   }
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
/*     */   public void addAll(RangeSet<C> other) {
/* 245 */     throw new UnsupportedOperationException();
/*     */   }
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
/*     */   public void addAll(Iterable<Range<C>> other) {
/* 258 */     throw new UnsupportedOperationException();
/*     */   }
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
/*     */   public void remove(Range<C> range) {
/* 271 */     throw new UnsupportedOperationException();
/*     */   }
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
/*     */   public void removeAll(RangeSet<C> other) {
/* 284 */     throw new UnsupportedOperationException();
/*     */   }
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
/*     */   public void removeAll(Iterable<Range<C>> other) {
/* 297 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<Range<C>> asRanges() {
/* 302 */     if (this.ranges.isEmpty()) {
/* 303 */       return ImmutableSet.of();
/*     */     }
/* 305 */     return new RegularImmutableSortedSet<>(this.ranges, (Comparator)Range.rangeLexOrdering());
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<Range<C>> asDescendingSetOfRanges() {
/* 310 */     if (this.ranges.isEmpty()) {
/* 311 */       return ImmutableSet.of();
/*     */     }
/* 313 */     return new RegularImmutableSortedSet<>(this.ranges.reverse(), Range.<Comparable<?>>rangeLexOrdering().reverse());
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
/*     */   private final class ComplementRanges
/*     */     extends ImmutableList<Range<C>>
/*     */   {
/* 328 */     private final boolean positiveBoundedBelow = ((Range)ImmutableRangeSet.this.ranges.get(0)).hasLowerBound();
/* 329 */     private final boolean positiveBoundedAbove = ((Range)Iterables.<Range>getLast(ImmutableRangeSet.this.ranges)).hasUpperBound();
/*     */     ComplementRanges() {
/* 331 */       int size = ImmutableRangeSet.this.ranges.size() - 1;
/* 332 */       if (this.positiveBoundedBelow) {
/* 333 */         size++;
/*     */       }
/* 335 */       if (this.positiveBoundedAbove) {
/* 336 */         size++;
/*     */       }
/* 338 */       this.size = size;
/*     */     }
/*     */     private final int size;
/*     */     
/*     */     public int size() {
/* 343 */       return this.size;
/*     */     }
/*     */     
/*     */     public Range<C> get(int index) {
/*     */       Cut<C> lowerBound, upperBound;
/* 348 */       Preconditions.checkElementIndex(index, this.size);
/*     */ 
/*     */       
/* 351 */       if (this.positiveBoundedBelow) {
/* 352 */         lowerBound = (index == 0) ? Cut.<C>belowAll() : ((Range)ImmutableRangeSet.this.ranges.get(index - 1)).upperBound;
/*     */       } else {
/* 354 */         lowerBound = ((Range)ImmutableRangeSet.this.ranges.get(index)).upperBound;
/*     */       } 
/*     */ 
/*     */       
/* 358 */       if (this.positiveBoundedAbove && index == this.size - 1) {
/* 359 */         upperBound = Cut.aboveAll();
/*     */       } else {
/* 361 */         upperBound = ((Range)ImmutableRangeSet.this.ranges.get(index + (this.positiveBoundedBelow ? 0 : 1))).lowerBound;
/*     */       } 
/*     */       
/* 364 */       return (Range)Range.create(lowerBound, upperBound);
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 369 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @J2ktIncompatible
/*     */     Object writeReplace() {
/* 377 */       return super.writeReplace();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableRangeSet<C> complement() {
/* 383 */     ImmutableRangeSet<C> result = this.complement;
/* 384 */     if (result != null)
/* 385 */       return result; 
/* 386 */     if (this.ranges.isEmpty())
/* 387 */       return this.complement = all(); 
/* 388 */     if (this.ranges.size() == 1 && ((Range)this.ranges.get(0)).equals(Range.all())) {
/* 389 */       return this.complement = of();
/*     */     }
/* 391 */     ImmutableList<Range<C>> complementRanges = new ComplementRanges();
/* 392 */     result = this.complement = new ImmutableRangeSet(complementRanges, this);
/*     */     
/* 394 */     return result;
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
/*     */   public ImmutableRangeSet<C> union(RangeSet<C> other) {
/* 406 */     return (ImmutableRangeSet)unionOf((Iterable)Iterables.concat(asRanges(), other.asRanges()));
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
/*     */   public ImmutableRangeSet<C> intersection(RangeSet<C> other) {
/* 419 */     RangeSet<C> copy = (RangeSet)TreeRangeSet.create(this);
/* 420 */     copy.removeAll(other.complement());
/* 421 */     return copyOf(copy);
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
/*     */   public ImmutableRangeSet<C> difference(RangeSet<C> other) {
/* 433 */     RangeSet<C> copy = (RangeSet)TreeRangeSet.create(this);
/* 434 */     copy.removeAll(other);
/* 435 */     return copyOf(copy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ImmutableList<Range<C>> intersectRanges(final Range<C> range) {
/*     */     final int fromIndex, toIndex;
/* 443 */     if (this.ranges.isEmpty() || range.isEmpty())
/* 444 */       return ImmutableList.of(); 
/* 445 */     if (range.encloses(span())) {
/* 446 */       return this.ranges;
/*     */     }
/*     */ 
/*     */     
/* 450 */     if (range.hasLowerBound()) {
/*     */       
/* 452 */       fromIndex = SortedLists.binarySearch(this.ranges, 
/*     */           
/* 454 */           (Function)Range.upperBoundFn(), range.lowerBound, SortedLists.KeyPresentBehavior.FIRST_AFTER, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 459 */       fromIndex = 0;
/*     */     } 
/*     */ 
/*     */     
/* 463 */     if (range.hasUpperBound()) {
/*     */       
/* 465 */       toIndex = SortedLists.binarySearch(this.ranges, 
/*     */           
/* 467 */           (Function)Range.lowerBoundFn(), range.upperBound, SortedLists.KeyPresentBehavior.FIRST_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 472 */       toIndex = this.ranges.size();
/*     */     } 
/* 474 */     final int length = toIndex - fromIndex;
/* 475 */     if (length == 0) {
/* 476 */       return ImmutableList.of();
/*     */     }
/* 478 */     return new ImmutableList<Range<C>>()
/*     */       {
/*     */         public int size() {
/* 481 */           return length;
/*     */         }
/*     */ 
/*     */         
/*     */         public Range<C> get(int index) {
/* 486 */           Preconditions.checkElementIndex(index, length);
/* 487 */           if (index == 0 || index == length - 1) {
/* 488 */             return ((Range<C>)ImmutableRangeSet.this.ranges.get(index + fromIndex)).intersection(range);
/*     */           }
/* 490 */           return ImmutableRangeSet.this.ranges.get(index + fromIndex);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         boolean isPartialView() {
/* 496 */           return true;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         @J2ktIncompatible
/*     */         @GwtIncompatible
/*     */         Object writeReplace() {
/* 505 */           return super.writeReplace();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableRangeSet<C> subRangeSet(Range<C> range) {
/* 514 */     if (!isEmpty()) {
/* 515 */       Range<C> span = span();
/* 516 */       if (range.encloses(span))
/* 517 */         return this; 
/* 518 */       if (range.isConnected(span)) {
/* 519 */         return new ImmutableRangeSet(intersectRanges(range));
/*     */       }
/*     */     } 
/* 522 */     return of();
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
/*     */   public ImmutableSortedSet<C> asSet(DiscreteDomain<C> domain) {
/* 545 */     Preconditions.checkNotNull(domain);
/* 546 */     if (isEmpty()) {
/* 547 */       return ImmutableSortedSet.of();
/*     */     }
/* 549 */     Range<C> span = span().canonical(domain);
/* 550 */     if (!span.hasLowerBound())
/*     */     {
/*     */       
/* 553 */       throw new IllegalArgumentException("Neither the DiscreteDomain nor this range set are bounded below");
/*     */     }
/* 555 */     if (!span.hasUpperBound()) {
/*     */       try {
/* 557 */         domain.maxValue();
/* 558 */       } catch (NoSuchElementException e) {
/* 559 */         throw new IllegalArgumentException("Neither the DiscreteDomain nor this range set are bounded above");
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 564 */     return new AsSet(domain);
/*     */   }
/*     */   
/*     */   private final class AsSet extends ImmutableSortedSet<C> {
/*     */     private final DiscreteDomain<C> domain;
/*     */     
/*     */     AsSet(DiscreteDomain<C> domain) {
/* 571 */       super(Ordering.natural());
/* 572 */       this.domain = domain;
/*     */     }
/*     */     
/*     */     @LazyInit
/*     */     @CheckForNull
/*     */     private transient Integer size;
/*     */     
/*     */     public int size() {
/* 580 */       Integer result = this.size;
/* 581 */       if (result == null) {
/* 582 */         long total = 0L;
/* 583 */         for (UnmodifiableIterator<Range<C>> unmodifiableIterator = ImmutableRangeSet.this.ranges.iterator(); unmodifiableIterator.hasNext(); ) { Range<C> range = unmodifiableIterator.next();
/* 584 */           total += ContiguousSet.<C>create(range, this.domain).size();
/* 585 */           if (total >= 2147483647L) {
/*     */             break;
/*     */           } }
/*     */         
/* 589 */         result = this.size = Integer.valueOf(Ints.saturatedCast(total));
/*     */       } 
/* 591 */       return result.intValue();
/*     */     }
/*     */ 
/*     */     
/*     */     public UnmodifiableIterator<C> iterator() {
/* 596 */       return new AbstractIterator<C>() {
/* 597 */           final Iterator<Range<C>> rangeItr = ImmutableRangeSet.this.ranges.iterator();
/* 598 */           Iterator<C> elemItr = Iterators.emptyIterator();
/*     */ 
/*     */           
/*     */           @CheckForNull
/*     */           protected C computeNext() {
/* 603 */             while (!this.elemItr.hasNext()) {
/* 604 */               if (this.rangeItr.hasNext()) {
/* 605 */                 this.elemItr = ContiguousSet.<C>create(this.rangeItr.next(), ImmutableRangeSet.AsSet.this.domain).iterator(); continue;
/*     */               } 
/* 607 */               return endOfData();
/*     */             } 
/*     */             
/* 610 */             return this.elemItr.next();
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     @GwtIncompatible("NavigableSet")
/*     */     public UnmodifiableIterator<C> descendingIterator() {
/* 618 */       return new AbstractIterator<C>() {
/* 619 */           final Iterator<Range<C>> rangeItr = ImmutableRangeSet.this.ranges.reverse().iterator();
/* 620 */           Iterator<C> elemItr = Iterators.emptyIterator();
/*     */ 
/*     */           
/*     */           @CheckForNull
/*     */           protected C computeNext() {
/* 625 */             while (!this.elemItr.hasNext()) {
/* 626 */               if (this.rangeItr.hasNext()) {
/* 627 */                 this.elemItr = ContiguousSet.<C>create(this.rangeItr.next(), ImmutableRangeSet.AsSet.this.domain).descendingIterator(); continue;
/*     */               } 
/* 629 */               return endOfData();
/*     */             } 
/*     */             
/* 632 */             return this.elemItr.next();
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     ImmutableSortedSet<C> subSet(Range<C> range) {
/* 638 */       return ImmutableRangeSet.this.subRangeSet(range).asSet(this.domain);
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSortedSet<C> headSetImpl(C toElement, boolean inclusive) {
/* 643 */       return subSet((Range)Range.upTo((Comparable<?>)toElement, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     ImmutableSortedSet<C> subSetImpl(C fromElement, boolean fromInclusive, C toElement, boolean toInclusive) {
/* 649 */       if (!fromInclusive && !toInclusive && Range.compareOrThrow((Comparable)fromElement, (Comparable)toElement) == 0) {
/* 650 */         return ImmutableSortedSet.of();
/*     */       }
/* 652 */       return subSet(
/* 653 */           (Range)Range.range((Comparable<?>)fromElement, 
/* 654 */             BoundType.forBoolean(fromInclusive), (Comparable<?>)toElement, 
/* 655 */             BoundType.forBoolean(toInclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSortedSet<C> tailSetImpl(C fromElement, boolean inclusive) {
/* 660 */       return subSet((Range)Range.downTo((Comparable<?>)fromElement, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(@CheckForNull Object o) {
/* 665 */       if (o == null) {
/* 666 */         return false;
/*     */       }
/*     */       
/*     */       try {
/* 670 */         Comparable comparable = (Comparable)o;
/* 671 */         return ImmutableRangeSet.this.contains(comparable);
/* 672 */       } catch (ClassCastException e) {
/* 673 */         return false;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     int indexOf(@CheckForNull Object target) {
/* 679 */       if (contains(target)) {
/*     */         
/* 681 */         Comparable comparable = (Comparable)Objects.<Object>requireNonNull(target);
/* 682 */         long total = 0L;
/* 683 */         for (UnmodifiableIterator<Range<C>> unmodifiableIterator = ImmutableRangeSet.this.ranges.iterator(); unmodifiableIterator.hasNext(); ) { Range<C> range = unmodifiableIterator.next();
/* 684 */           if (range.contains((C)comparable)) {
/* 685 */             return Ints.saturatedCast(total + ContiguousSet.<C>create(range, this.domain).indexOf(comparable));
/*     */           }
/* 687 */           total += ContiguousSet.<C>create(range, this.domain).size(); }
/*     */ 
/*     */         
/* 690 */         throw new AssertionError("impossible");
/*     */       } 
/* 692 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSortedSet<C> createDescendingSet() {
/* 697 */       return new DescendingImmutableSortedSet<>(this);
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 702 */       return ImmutableRangeSet.this.ranges.isPartialView();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 707 */       return ImmutableRangeSet.this.ranges.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     @J2ktIncompatible
/*     */     Object writeReplace() {
/* 713 */       return new ImmutableRangeSet.AsSetSerializedForm<>(ImmutableRangeSet.this.ranges, this.domain);
/*     */     }
/*     */     
/*     */     @J2ktIncompatible
/*     */     private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 718 */       throw new InvalidObjectException("Use SerializedForm");
/*     */     }
/*     */   }
/*     */   
/*     */   private static class AsSetSerializedForm<C extends Comparable> implements Serializable {
/*     */     private final ImmutableList<Range<C>> ranges;
/*     */     private final DiscreteDomain<C> domain;
/*     */     
/*     */     AsSetSerializedForm(ImmutableList<Range<C>> ranges, DiscreteDomain<C> domain) {
/* 727 */       this.ranges = ranges;
/* 728 */       this.domain = domain;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 732 */       return (new ImmutableRangeSet<>(this.ranges)).asSet(this.domain);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 743 */     return this.ranges.isPartialView();
/*     */   }
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Builder<C> builder() {
/* 748 */     return new Builder<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder<C extends Comparable<?>>
/*     */   {
/* 760 */     private final List<Range<C>> ranges = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<C> add(Range<C> range) {
/* 773 */       Preconditions.checkArgument(!range.isEmpty(), "range must not be empty, but was %s", range);
/* 774 */       this.ranges.add(range);
/* 775 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<C> addAll(RangeSet<C> ranges) {
/* 785 */       return addAll(ranges.asRanges());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<C> addAll(Iterable<Range<C>> ranges) {
/* 797 */       for (Range<C> range : ranges) {
/* 798 */         add(range);
/*     */       }
/* 800 */       return this;
/*     */     }
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<C> combine(Builder<C> builder) {
/* 805 */       addAll(builder.ranges);
/* 806 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableRangeSet<C> build() {
/* 816 */       ImmutableList.Builder<Range<C>> mergedRangesBuilder = new ImmutableList.Builder<>(this.ranges.size());
/* 817 */       Collections.sort(this.ranges, (Comparator)Range.rangeLexOrdering());
/* 818 */       PeekingIterator<Range<C>> peekingItr = Iterators.peekingIterator(this.ranges.iterator());
/* 819 */       while (peekingItr.hasNext()) {
/* 820 */         Range<C> range = peekingItr.next();
/* 821 */         while (peekingItr.hasNext()) {
/* 822 */           Range<C> nextRange = peekingItr.peek();
/* 823 */           if (range.isConnected(nextRange)) {
/* 824 */             Preconditions.checkArgument(range
/* 825 */                 .intersection(nextRange).isEmpty(), "Overlapping ranges not permitted but found %s overlapping %s", range, nextRange);
/*     */ 
/*     */ 
/*     */             
/* 829 */             range = range.span(peekingItr.next());
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 834 */         mergedRangesBuilder.add(range);
/*     */       } 
/* 836 */       ImmutableList<Range<C>> mergedRanges = mergedRangesBuilder.build();
/* 837 */       if (mergedRanges.isEmpty())
/* 838 */         return (ImmutableRangeSet)ImmutableRangeSet.of(); 
/* 839 */       if (mergedRanges.size() == 1 && (
/* 840 */         (Range)Iterables.<Range>getOnlyElement((Iterable)mergedRanges)).equals(Range.all())) {
/* 841 */         return (ImmutableRangeSet)ImmutableRangeSet.all();
/*     */       }
/* 843 */       return (ImmutableRangeSet)new ImmutableRangeSet<>(mergedRanges);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class SerializedForm<C extends Comparable>
/*     */     implements Serializable {
/*     */     private final ImmutableList<Range<C>> ranges;
/*     */     
/*     */     SerializedForm(ImmutableList<Range<C>> ranges) {
/* 852 */       this.ranges = ranges;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 856 */       if (this.ranges.isEmpty())
/* 857 */         return ImmutableRangeSet.of(); 
/* 858 */       if (this.ranges.equals(ImmutableList.of(Range.all()))) {
/* 859 */         return ImmutableRangeSet.all();
/*     */       }
/* 861 */       return new ImmutableRangeSet<>(this.ranges);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   Object writeReplace() {
/* 868 */     return new SerializedForm<>(this.ranges);
/*     */   }
/*     */   
/*     */   @J2ktIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 873 */     throw new InvalidObjectException("Use SerializedForm");
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ImmutableRangeSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */