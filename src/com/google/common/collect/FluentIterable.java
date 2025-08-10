/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.InlineMe;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.SortedSet;
/*     */ import java.util.stream.Stream;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(emulated = true)
/*     */ public abstract class FluentIterable<E>
/*     */   implements Iterable<E>
/*     */ {
/*     */   private final Optional<Iterable<E>> iterableDelegate;
/*     */   
/*     */   protected FluentIterable() {
/* 123 */     this.iterableDelegate = Optional.absent();
/*     */   }
/*     */   
/*     */   FluentIterable(Iterable<E> iterable) {
/* 127 */     this.iterableDelegate = Optional.of(iterable);
/*     */   }
/*     */   
/*     */   private Iterable<E> getDelegate() {
/* 131 */     return (Iterable<E>)this.iterableDelegate.or(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> FluentIterable<E> from(final Iterable<E> iterable) {
/* 142 */     return (iterable instanceof FluentIterable) ? 
/* 143 */       (FluentIterable<E>)iterable : 
/* 144 */       new FluentIterable<E>(iterable)
/*     */       {
/*     */         public Iterator<E> iterator() {
/* 147 */           return iterable.iterator();
/*     */         }
/*     */       };
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
/*     */   public static <E> FluentIterable<E> from(E[] elements) {
/* 163 */     return from(Arrays.asList(elements));
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
/*     */   @InlineMe(replacement = "checkNotNull(iterable)", staticImports = {"com.google.common.base.Preconditions.checkNotNull"})
/*     */   public static <E> FluentIterable<E> from(FluentIterable<E> iterable) {
/* 179 */     return (FluentIterable<E>)Preconditions.checkNotNull(iterable);
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
/*     */   public static <T> FluentIterable<T> concat(Iterable<? extends T> a, Iterable<? extends T> b) {
/* 196 */     return concatNoDefensiveCopy((Iterable<? extends T>[])new Iterable[] { a, b });
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
/*     */   public static <T> FluentIterable<T> concat(Iterable<? extends T> a, Iterable<? extends T> b, Iterable<? extends T> c) {
/* 214 */     return concatNoDefensiveCopy((Iterable<? extends T>[])new Iterable[] { a, b, c });
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
/*     */   public static <T> FluentIterable<T> concat(Iterable<? extends T> a, Iterable<? extends T> b, Iterable<? extends T> c, Iterable<? extends T> d) {
/* 236 */     return concatNoDefensiveCopy((Iterable<? extends T>[])new Iterable[] { a, b, c, d });
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
/*     */   public static <T> FluentIterable<T> concat(Iterable<? extends T>... inputs) {
/* 256 */     return concatNoDefensiveCopy(Arrays.<Iterable<? extends T>>copyOf(inputs, inputs.length));
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
/*     */   public static <T> FluentIterable<T> concat(final Iterable<? extends Iterable<? extends T>> inputs) {
/* 275 */     Preconditions.checkNotNull(inputs);
/* 276 */     return new FluentIterable<T>()
/*     */       {
/*     */         public Iterator<T> iterator() {
/* 279 */           return Iterators.concat(Iterators.transform(inputs.iterator(), Iterable::iterator));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> FluentIterable<T> concatNoDefensiveCopy(Iterable<? extends T>... inputs) {
/* 287 */     for (Iterable<? extends T> input : inputs) {
/* 288 */       Preconditions.checkNotNull(input);
/*     */     }
/* 290 */     return new FluentIterable<T>()
/*     */       {
/*     */         public Iterator<T> iterator() {
/* 293 */           return Iterators.concat(new AbstractIndexedListIterator(inputs.length)
/*     */               {
/*     */                 
/*     */                 public Iterator<? extends T> get(int i)
/*     */                 {
/* 298 */                   return inputs[i].iterator();
/*     */                 }
/*     */               });
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> FluentIterable<E> of() {
/* 313 */     return from(Collections.emptyList());
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
/*     */   public static <E> FluentIterable<E> of(@ParametricNullness E element, E... elements) {
/* 326 */     return from(Lists.asList(element, elements));
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
/*     */   public String toString() {
/* 338 */     return Iterables.toString(getDelegate());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int size() {
/* 347 */     return Iterables.size(getDelegate());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean contains(@CheckForNull Object target) {
/* 357 */     return Iterables.contains((Iterable)getDelegate(), target);
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
/*     */   public final FluentIterable<E> cycle() {
/* 378 */     return from(Iterables.cycle(getDelegate()));
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
/*     */   public final FluentIterable<E> append(Iterable<? extends E> other) {
/* 393 */     return concat(getDelegate(), other);
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
/*     */   public final FluentIterable<E> append(E... elements) {
/* 405 */     return concat(getDelegate(), Arrays.asList(elements));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final FluentIterable<E> filter(Predicate<? super E> predicate) {
/* 415 */     return from(Iterables.filter(getDelegate(), predicate));
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
/*     */   @GwtIncompatible
/*     */   public final <T> FluentIterable<T> filter(Class<T> type) {
/* 433 */     return from(Iterables.filter(getDelegate(), type));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean anyMatch(Predicate<? super E> predicate) {
/* 442 */     return Iterables.any(getDelegate(), predicate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean allMatch(Predicate<? super E> predicate) {
/* 452 */     return Iterables.all(getDelegate(), predicate);
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
/*     */   public final Optional<E> firstMatch(Predicate<? super E> predicate) {
/* 466 */     return Iterables.tryFind(getDelegate(), predicate);
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
/*     */   public final <T> FluentIterable<T> transform(Function<? super E, T> function) {
/* 481 */     return from(Iterables.transform(getDelegate(), function));
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
/*     */   public <T> FluentIterable<T> transformAndConcat(Function<? super E, ? extends Iterable<? extends T>> function) {
/* 499 */     return concat(transform(function));
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
/*     */   public final Optional<E> first() {
/* 514 */     Iterator<E> iterator = getDelegate().iterator();
/* 515 */     return iterator.hasNext() ? Optional.of(iterator.next()) : Optional.absent();
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
/*     */   public final Optional<E> last() {
/* 534 */     Iterable<E> iterable = getDelegate();
/* 535 */     if (iterable instanceof List) {
/* 536 */       List<E> list = (List<E>)iterable;
/* 537 */       if (list.isEmpty()) {
/* 538 */         return Optional.absent();
/*     */       }
/* 540 */       return Optional.of(list.get(list.size() - 1));
/*     */     } 
/* 542 */     Iterator<E> iterator = iterable.iterator();
/* 543 */     if (!iterator.hasNext()) {
/* 544 */       return Optional.absent();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 551 */     if (iterable instanceof SortedSet) {
/* 552 */       SortedSet<E> sortedSet = (SortedSet<E>)iterable;
/* 553 */       return Optional.of(sortedSet.last());
/*     */     } 
/*     */     
/*     */     while (true) {
/* 557 */       E current = iterator.next();
/* 558 */       if (!iterator.hasNext()) {
/* 559 */         return Optional.of(current);
/*     */       }
/*     */     } 
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
/*     */   public final FluentIterable<E> skip(int numberToSkip) {
/* 582 */     return from(Iterables.skip(getDelegate(), numberToSkip));
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
/*     */   public final FluentIterable<E> limit(int maxSize) {
/* 597 */     return from(Iterables.limit(getDelegate(), maxSize));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isEmpty() {
/* 606 */     return !getDelegate().iterator().hasNext();
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
/*     */   public final ImmutableList<E> toList() {
/* 621 */     return ImmutableList.copyOf(getDelegate());
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
/*     */   public final ImmutableList<E> toSortedList(Comparator<? super E> comparator) {
/* 638 */     return Ordering.<E>from(comparator).immutableSortedCopy(getDelegate());
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
/*     */   public final ImmutableSet<E> toSet() {
/* 653 */     return ImmutableSet.copyOf(getDelegate());
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
/*     */   public final ImmutableSortedSet<E> toSortedSet(Comparator<? super E> comparator) {
/* 671 */     return ImmutableSortedSet.copyOf(comparator, getDelegate());
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
/*     */   public final ImmutableMultiset<E> toMultiset() {
/* 685 */     return ImmutableMultiset.copyOf(getDelegate());
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
/*     */   public final <V> ImmutableMap<E, V> toMap(Function<? super E, V> valueFunction) {
/* 706 */     return Maps.toMap(getDelegate(), valueFunction);
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
/*     */   public final <K> ImmutableListMultimap<K, E> index(Function<? super E, K> keyFunction) {
/* 729 */     return Multimaps.index(getDelegate(), keyFunction);
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
/*     */   public final <K> ImmutableMap<K, E> uniqueIndex(Function<? super E, K> keyFunction) {
/* 764 */     return Maps.uniqueIndex(getDelegate(), keyFunction);
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
/*     */   @GwtIncompatible
/*     */   public final E[] toArray(Class<E> type) {
/* 781 */     return Iterables.toArray(getDelegate(), type);
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
/*     */   @CanIgnoreReturnValue
/*     */   public final <C extends Collection<? super E>> C copyInto(C collection) {
/* 797 */     Preconditions.checkNotNull(collection);
/* 798 */     Iterable<E> iterable = getDelegate();
/* 799 */     if (iterable instanceof Collection) {
/* 800 */       collection.addAll((Collection)iterable);
/*     */     } else {
/* 802 */       for (E item : iterable) {
/* 803 */         collection.add(item);
/*     */       }
/*     */     } 
/* 806 */     return collection;
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
/*     */   public final String join(Joiner joiner) {
/* 820 */     return joiner.join(this);
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
/*     */   @ParametricNullness
/*     */   public final E get(int position) {
/* 837 */     return Iterables.get(getDelegate(), position);
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
/*     */   public final Stream<E> stream() {
/* 851 */     return Streams.stream(getDelegate());
/*     */   }
/*     */ 
/*     */   
/*     */   private static class FromIterableFunction<E>
/*     */     implements Function<Iterable<E>, FluentIterable<E>>
/*     */   {
/*     */     public FluentIterable<E> apply(Iterable<E> fromObject) {
/* 859 */       return FluentIterable.from(fromObject);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/FluentIterable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */