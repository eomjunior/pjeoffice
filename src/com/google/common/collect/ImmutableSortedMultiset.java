/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.DoNotCall;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.ToIntFunction;
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
/*     */ public abstract class ImmutableSortedMultiset<E>
/*     */   extends ImmutableMultiset<E>
/*     */   implements SortedMultiset<E>
/*     */ {
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   transient ImmutableSortedMultiset<E> descendingMultiset;
/*     */   private static final long serialVersionUID = -889275714L;
/*     */   
/*     */   public static <E> Collector<E, ?, ImmutableSortedMultiset<E>> toImmutableSortedMultiset(Comparator<? super E> comparator) {
/*  73 */     return toImmutableSortedMultiset(comparator, (Function)Function.identity(), e -> 1);
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
/*     */   public static <T, E> Collector<T, ?, ImmutableSortedMultiset<E>> toImmutableSortedMultiset(Comparator<? super E> comparator, Function<? super T, ? extends E> elementFunction, ToIntFunction<? super T> countFunction) {
/*  92 */     Preconditions.checkNotNull(comparator);
/*  93 */     Preconditions.checkNotNull(elementFunction);
/*  94 */     Preconditions.checkNotNull(countFunction);
/*  95 */     return Collector.of(() -> TreeMultiset.create(comparator), (multiset, t) -> multiset.add(Preconditions.checkNotNull(elementFunction.apply(t)), countFunction.applyAsInt(t)), (multiset1, multiset2) -> { multiset1.addAll(multiset2); return multiset1; }multiset -> copyOfSortedEntries(comparator, multiset.entrySet()), new Collector.Characteristics[0]);
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
/*     */   public static <E> ImmutableSortedMultiset<E> of() {
/* 113 */     return (ImmutableSortedMultiset)RegularImmutableSortedMultiset.NATURAL_EMPTY_MULTISET;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E element) {
/* 119 */     RegularImmutableSortedSet<E> elementSet = (RegularImmutableSortedSet<E>)ImmutableSortedSet.<E>of(element);
/* 120 */     long[] cumulativeCounts = { 0L, 1L };
/* 121 */     return new RegularImmutableSortedMultiset<>(elementSet, cumulativeCounts, 0, 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e1, E e2) {
/* 131 */     return copyOf(Ordering.natural(), Arrays.asList((E[])new Comparable[] { (Comparable)e1, (Comparable)e2 }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e1, E e2, E e3) {
/* 141 */     return copyOf(Ordering.natural(), Arrays.asList((E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3 }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e1, E e2, E e3, E e4) {
/* 152 */     return copyOf(Ordering.natural(), Arrays.asList((E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3, (Comparable)e4 }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e1, E e2, E e3, E e4, E e5) {
/* 163 */     return copyOf(Ordering.natural(), Arrays.asList((E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3, (Comparable)e4, (Comparable)e5 }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... remaining) {
/* 174 */     int size = remaining.length + 6;
/* 175 */     List<E> all = Lists.newArrayListWithCapacity(size);
/* 176 */     Collections.addAll(all, (E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3, (Comparable)e4, (Comparable)e5, (Comparable)e6 });
/* 177 */     Collections.addAll(all, remaining);
/* 178 */     return copyOf(Ordering.natural(), all);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> copyOf(E[] elements) {
/* 188 */     return copyOf(Ordering.natural(), Arrays.asList(elements));
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
/*     */   public static <E> ImmutableSortedMultiset<E> copyOf(Iterable<? extends E> elements) {
/* 216 */     Ordering<E> naturalOrder = Ordering.natural();
/* 217 */     return copyOf(naturalOrder, elements);
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
/*     */   public static <E> ImmutableSortedMultiset<E> copyOf(Iterator<? extends E> elements) {
/* 234 */     Ordering<E> naturalOrder = Ordering.natural();
/* 235 */     return copyOf(naturalOrder, elements);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSortedMultiset<E> copyOf(Comparator<? super E> comparator, Iterator<? extends E> elements) {
/* 246 */     Preconditions.checkNotNull(comparator);
/* 247 */     return (new Builder<>(comparator)).addAll(elements).build();
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
/*     */   public static <E> ImmutableSortedMultiset<E> copyOf(Comparator<? super E> comparator, Iterable<? extends E> elements) {
/* 262 */     if (elements instanceof ImmutableSortedMultiset) {
/*     */       
/* 264 */       ImmutableSortedMultiset<E> multiset = (ImmutableSortedMultiset)elements;
/* 265 */       if (comparator.equals(multiset.comparator())) {
/* 266 */         if (multiset.isPartialView()) {
/* 267 */           return copyOfSortedEntries(comparator, multiset.entrySet().asList());
/*     */         }
/* 269 */         return multiset;
/*     */       } 
/*     */     } 
/*     */     
/* 273 */     elements = Lists.newArrayList(elements);
/* 274 */     TreeMultiset<E> sortedCopy = TreeMultiset.create((Comparator<? super E>)Preconditions.checkNotNull(comparator));
/* 275 */     Iterables.addAll(sortedCopy, elements);
/* 276 */     return copyOfSortedEntries(comparator, sortedCopy.entrySet());
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
/*     */   public static <E> ImmutableSortedMultiset<E> copyOfSorted(SortedMultiset<E> sortedMultiset) {
/* 294 */     return copyOfSortedEntries(sortedMultiset
/* 295 */         .comparator(), Lists.newArrayList(sortedMultiset.entrySet()));
/*     */   }
/*     */ 
/*     */   
/*     */   private static <E> ImmutableSortedMultiset<E> copyOfSortedEntries(Comparator<? super E> comparator, Collection<Multiset.Entry<E>> entries) {
/* 300 */     if (entries.isEmpty()) {
/* 301 */       return emptyMultiset(comparator);
/*     */     }
/* 303 */     ImmutableList.Builder<E> elementsBuilder = new ImmutableList.Builder<>(entries.size());
/* 304 */     long[] cumulativeCounts = new long[entries.size() + 1];
/* 305 */     int i = 0;
/* 306 */     for (Multiset.Entry<E> entry : entries) {
/* 307 */       elementsBuilder.add(entry.getElement());
/* 308 */       cumulativeCounts[i + 1] = cumulativeCounts[i] + entry.getCount();
/* 309 */       i++;
/*     */     } 
/* 311 */     return new RegularImmutableSortedMultiset<>(new RegularImmutableSortedSet<>(elementsBuilder
/* 312 */           .build(), comparator), cumulativeCounts, 0, entries
/*     */ 
/*     */         
/* 315 */         .size());
/*     */   }
/*     */ 
/*     */   
/*     */   static <E> ImmutableSortedMultiset<E> emptyMultiset(Comparator<? super E> comparator) {
/* 320 */     if (Ordering.<Comparable>natural().equals(comparator)) {
/* 321 */       return (ImmutableSortedMultiset)RegularImmutableSortedMultiset.NATURAL_EMPTY_MULTISET;
/*     */     }
/* 323 */     return new RegularImmutableSortedMultiset<>(comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Comparator<? super E> comparator() {
/* 331 */     return elementSet().comparator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSortedMultiset<E> descendingMultiset() {
/* 341 */     ImmutableSortedMultiset<E> result = this.descendingMultiset;
/* 342 */     if (result == null) {
/* 343 */       return this
/*     */ 
/*     */         
/* 346 */         .descendingMultiset = isEmpty() ? emptyMultiset(Ordering.from(comparator()).reverse()) : new DescendingImmutableSortedMultiset<>(this);
/*     */     }
/* 348 */     return result;
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
/*     */   @Deprecated
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   @DoNotCall("Always throws UnsupportedOperationException")
/*     */   public final Multiset.Entry<E> pollFirstEntry() {
/* 365 */     throw new UnsupportedOperationException();
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
/*     */   @Deprecated
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   @DoNotCall("Always throws UnsupportedOperationException")
/*     */   public final Multiset.Entry<E> pollLastEntry() {
/* 382 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSortedMultiset<E> subMultiset(E lowerBound, BoundType lowerBoundType, E upperBound, BoundType upperBoundType) {
/* 391 */     Preconditions.checkArgument(
/* 392 */         (comparator().compare(lowerBound, upperBound) <= 0), "Expected lowerBound <= upperBound but %s > %s", lowerBound, upperBound);
/*     */ 
/*     */ 
/*     */     
/* 396 */     return tailMultiset(lowerBound, lowerBoundType).headMultiset(upperBound, upperBoundType);
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
/*     */   public static <E> Builder<E> orderedBy(Comparator<E> comparator) {
/* 411 */     return new Builder<>(comparator);
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
/*     */   public static <E extends Comparable<?>> Builder<E> reverseOrder() {
/* 423 */     return new Builder<>(Ordering.<Comparable>natural().reverse());
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
/*     */   public static <E extends Comparable<?>> Builder<E> naturalOrder() {
/* 437 */     return new Builder<>(Ordering.natural());
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
/*     */   public static class Builder<E>
/*     */     extends ImmutableMultiset.Builder<E>
/*     */   {
/*     */     public Builder(Comparator<? super E> comparator) {
/* 465 */       super(TreeMultiset.create((Comparator<? super E>)Preconditions.checkNotNull(comparator)));
/*     */     }
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
/*     */     public Builder<E> add(E element) {
/* 478 */       super.add(element);
/* 479 */       return this;
/*     */     }
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
/*     */     public Builder<E> add(E... elements) {
/* 492 */       super.add(elements);
/* 493 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */     public Builder<E> addCopies(E element, int occurrences) {
/* 510 */       super.addCopies(element, occurrences);
/* 511 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
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
/*     */     public Builder<E> setCount(E element, int count) {
/* 527 */       super.setCount(element, count);
/* 528 */       return this;
/*     */     }
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
/*     */     public Builder<E> addAll(Iterable<? extends E> elements) {
/* 541 */       super.addAll(elements);
/* 542 */       return this;
/*     */     }
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
/*     */     public Builder<E> addAll(Iterator<? extends E> elements) {
/* 555 */       super.addAll(elements);
/* 556 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableSortedMultiset<E> build() {
/* 565 */       return ImmutableSortedMultiset.copyOfSorted((SortedMultiset<E>)this.contents);
/*     */     }
/*     */   }
/*     */   
/*     */   @J2ktIncompatible
/*     */   private static final class SerializedForm<E>
/*     */     implements Serializable {
/*     */     final Comparator<? super E> comparator;
/*     */     final E[] elements;
/*     */     final int[] counts;
/*     */     
/*     */     SerializedForm(SortedMultiset<E> multiset) {
/* 577 */       this.comparator = multiset.comparator();
/* 578 */       int n = multiset.entrySet().size();
/* 579 */       this.elements = (E[])new Object[n];
/* 580 */       this.counts = new int[n];
/* 581 */       int i = 0;
/* 582 */       for (Multiset.Entry<E> entry : multiset.entrySet()) {
/* 583 */         this.elements[i] = entry.getElement();
/* 584 */         this.counts[i] = entry.getCount();
/* 585 */         i++;
/*     */       } 
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 590 */       int n = this.elements.length;
/* 591 */       ImmutableSortedMultiset.Builder<E> builder = new ImmutableSortedMultiset.Builder<>(this.comparator);
/* 592 */       for (int i = 0; i < n; i++) {
/* 593 */         builder.addCopies(this.elements[i], this.counts[i]);
/*     */       }
/* 595 */       return builder.build();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   Object writeReplace() {
/* 602 */     return new SerializedForm<>(this);
/*     */   }
/*     */   
/*     */   @J2ktIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 607 */     throw new InvalidObjectException("Use SerializedForm");
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
/*     */   @Deprecated
/*     */   @DoNotCall("Use toImmutableSortedMultiset.")
/*     */   public static <E> Collector<E, ?, ImmutableMultiset<E>> toImmutableMultiset() {
/* 622 */     throw new UnsupportedOperationException();
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
/*     */   @Deprecated
/*     */   @DoNotCall("Use toImmutableSortedMultiset.")
/*     */   public static <T, E> Collector<T, ?, ImmutableMultiset<E>> toImmutableMultiset(Function<? super T, ? extends E> elementFunction, ToIntFunction<? super T> countFunction) {
/* 640 */     throw new UnsupportedOperationException();
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
/*     */   @DoNotCall("Use naturalOrder.")
/*     */   public static <E> Builder<E> builder() {
/* 654 */     throw new UnsupportedOperationException();
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
/*     */   @Deprecated
/*     */   @DoNotCall("Elements must be Comparable. (Or, pass a Comparator to orderedBy or copyOf.)")
/*     */   public static <E> ImmutableSortedMultiset<E> of(E element) {
/* 669 */     throw new UnsupportedOperationException();
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
/*     */   @Deprecated
/*     */   @DoNotCall("Elements must be Comparable. (Or, pass a Comparator to orderedBy or copyOf.)")
/*     */   public static <E> ImmutableSortedMultiset<E> of(E e1, E e2) {
/* 684 */     throw new UnsupportedOperationException();
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
/*     */   @Deprecated
/*     */   @DoNotCall("Elements must be Comparable. (Or, pass a Comparator to orderedBy or copyOf.)")
/*     */   public static <E> ImmutableSortedMultiset<E> of(E e1, E e2, E e3) {
/* 699 */     throw new UnsupportedOperationException();
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
/*     */   @Deprecated
/*     */   @DoNotCall("Elements must be Comparable. (Or, pass a Comparator to orderedBy or copyOf.)")
/*     */   public static <E> ImmutableSortedMultiset<E> of(E e1, E e2, E e3, E e4) {
/* 714 */     throw new UnsupportedOperationException();
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
/*     */   @Deprecated
/*     */   @DoNotCall("Elements must be Comparable. (Or, pass a Comparator to orderedBy or copyOf.)")
/*     */   public static <E> ImmutableSortedMultiset<E> of(E e1, E e2, E e3, E e4, E e5) {
/* 730 */     throw new UnsupportedOperationException();
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
/*     */   @Deprecated
/*     */   @DoNotCall("Elements must be Comparable. (Or, pass a Comparator to orderedBy or copyOf.)")
/*     */   public static <E> ImmutableSortedMultiset<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... remaining) {
/* 747 */     throw new UnsupportedOperationException();
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
/*     */   @Deprecated
/*     */   @DoNotCall("Elements must be Comparable. (Or, pass a Comparator to orderedBy or copyOf.)")
/*     */   public static <Z> ImmutableSortedMultiset<Z> copyOf(Z[] elements) {
/* 763 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public abstract ImmutableSortedSet<E> elementSet();
/*     */   
/*     */   public abstract ImmutableSortedMultiset<E> headMultiset(E paramE, BoundType paramBoundType);
/*     */   
/*     */   public abstract ImmutableSortedMultiset<E> tailMultiset(E paramE, BoundType paramBoundType);
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ImmutableSortedMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */