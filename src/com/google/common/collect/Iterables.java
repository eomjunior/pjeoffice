/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Optional;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Queue;
/*      */ import java.util.Set;
/*      */ import java.util.Spliterator;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.stream.Stream;
/*      */ import javax.annotation.CheckForNull;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @ElementTypesAreNonnullByDefault
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class Iterables
/*      */ {
/*      */   public static <T> Iterable<T> unmodifiableIterable(Iterable<? extends T> iterable) {
/*   75 */     Preconditions.checkNotNull(iterable);
/*   76 */     if (iterable instanceof UnmodifiableIterable || iterable instanceof ImmutableCollection)
/*      */     {
/*   78 */       return (Iterable)iterable;
/*      */     }
/*      */     
/*   81 */     return new UnmodifiableIterable<>(iterable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <E> Iterable<E> unmodifiableIterable(ImmutableCollection<E> iterable) {
/*   92 */     return (Iterable<E>)Preconditions.checkNotNull(iterable);
/*      */   }
/*      */   
/*      */   private static final class UnmodifiableIterable<T>
/*      */     extends FluentIterable<T> {
/*      */     private final Iterable<? extends T> iterable;
/*      */     
/*      */     private UnmodifiableIterable(Iterable<? extends T> iterable) {
/*  100 */       this.iterable = iterable;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<T> iterator() {
/*  105 */       return Iterators.unmodifiableIterator(this.iterable.iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super T> action) {
/*  110 */       this.iterable.forEach(action);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Spliterator<T> spliterator() {
/*  116 */       return (Spliterator)this.iterable.spliterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  121 */       return this.iterable.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int size(Iterable<?> iterable) {
/*  128 */     return (iterable instanceof Collection) ? (
/*  129 */       (Collection)iterable).size() : 
/*  130 */       Iterators.size(iterable.iterator());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean contains(Iterable<? extends Object> iterable, @CheckForNull Object element) {
/*  142 */     if (iterable instanceof Collection) {
/*  143 */       Collection<?> collection = (Collection)iterable;
/*  144 */       return Collections2.safeContains(collection, element);
/*      */     } 
/*  146 */     return Iterators.contains(iterable.iterator(), element);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static boolean removeAll(Iterable<?> removeFrom, Collection<?> elementsToRemove) {
/*  161 */     return (removeFrom instanceof Collection) ? (
/*  162 */       (Collection)removeFrom).removeAll((Collection)Preconditions.checkNotNull(elementsToRemove)) : 
/*  163 */       Iterators.removeAll(removeFrom.iterator(), elementsToRemove);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static boolean retainAll(Iterable<?> removeFrom, Collection<?> elementsToRetain) {
/*  178 */     return (removeFrom instanceof Collection) ? (
/*  179 */       (Collection)removeFrom).retainAll((Collection)Preconditions.checkNotNull(elementsToRetain)) : 
/*  180 */       Iterators.retainAll(removeFrom.iterator(), elementsToRetain);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> boolean removeIf(Iterable<T> removeFrom, Predicate<? super T> predicate) {
/*  202 */     if (removeFrom instanceof Collection) {
/*  203 */       return ((Collection<T>)removeFrom).removeIf((Predicate<? super T>)predicate);
/*      */     }
/*  205 */     return Iterators.removeIf(removeFrom.iterator(), predicate);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   static <T> T removeFirstMatching(Iterable<T> removeFrom, Predicate<? super T> predicate) {
/*  212 */     Preconditions.checkNotNull(predicate);
/*  213 */     Iterator<T> iterator = removeFrom.iterator();
/*  214 */     while (iterator.hasNext()) {
/*  215 */       T next = iterator.next();
/*  216 */       if (predicate.apply(next)) {
/*  217 */         iterator.remove();
/*  218 */         return next;
/*      */       } 
/*      */     } 
/*  221 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean elementsEqual(Iterable<?> iterable1, Iterable<?> iterable2) {
/*  231 */     if (iterable1 instanceof Collection && iterable2 instanceof Collection) {
/*  232 */       Collection<?> collection1 = (Collection)iterable1;
/*  233 */       Collection<?> collection2 = (Collection)iterable2;
/*  234 */       if (collection1.size() != collection2.size()) {
/*  235 */         return false;
/*      */       }
/*      */     } 
/*  238 */     return Iterators.elementsEqual(iterable1.iterator(), iterable2.iterator());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(Iterable<?> iterable) {
/*  249 */     return Iterators.toString(iterable.iterator());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @ParametricNullness
/*      */   public static <T> T getOnlyElement(Iterable<T> iterable) {
/*  263 */     return Iterators.getOnlyElement(iterable.iterator());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @ParametricNullness
/*      */   public static <T> T getOnlyElement(Iterable<? extends T> iterable, @ParametricNullness T defaultValue) {
/*  278 */     return Iterators.getOnlyElement(iterable.iterator(), defaultValue);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static <T> T[] toArray(Iterable<? extends T> iterable, Class<T> type) {
/*  291 */     return toArray(iterable, ObjectArrays.newArray(type, 0));
/*      */   }
/*      */   
/*      */   static <T> T[] toArray(Iterable<? extends T> iterable, T[] array) {
/*  295 */     Collection<? extends T> collection = castOrCopyToCollection(iterable);
/*  296 */     return collection.toArray(array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Object[] toArray(Iterable<?> iterable) {
/*  306 */     return castOrCopyToCollection(iterable).toArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <E> Collection<E> castOrCopyToCollection(Iterable<E> iterable) {
/*  316 */     return (iterable instanceof Collection) ? 
/*  317 */       (Collection<E>)iterable : 
/*  318 */       Lists.<E>newArrayList(iterable.iterator());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> boolean addAll(Collection<T> addTo, Iterable<? extends T> elementsToAdd) {
/*  329 */     if (elementsToAdd instanceof Collection) {
/*  330 */       Collection<? extends T> c = (Collection<? extends T>)elementsToAdd;
/*  331 */       return addTo.addAll(c);
/*      */     } 
/*  333 */     return Iterators.addAll(addTo, ((Iterable<? extends T>)Preconditions.checkNotNull(elementsToAdd)).iterator());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int frequency(Iterable<?> iterable, @CheckForNull Object element) {
/*  348 */     if (iterable instanceof Multiset)
/*  349 */       return ((Multiset)iterable).count(element); 
/*  350 */     if (iterable instanceof Set) {
/*  351 */       return ((Set)iterable).contains(element) ? 1 : 0;
/*      */     }
/*  353 */     return Iterators.frequency(iterable.iterator(), element);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Iterable<T> cycle(final Iterable<T> iterable) {
/*  375 */     Preconditions.checkNotNull(iterable);
/*  376 */     return new FluentIterable<T>()
/*      */       {
/*      */         public Iterator<T> iterator() {
/*  379 */           return Iterators.cycle(iterable);
/*      */         }
/*      */ 
/*      */         
/*      */         public Spliterator<T> spliterator() {
/*  384 */           return Stream.generate(() -> iterable).flatMap(Streams::stream).spliterator();
/*      */         }
/*      */ 
/*      */         
/*      */         public String toString() {
/*  389 */           return iterable.toString() + " (cycled)";
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @SafeVarargs
/*      */   public static <T> Iterable<T> cycle(T... elements) {
/*  416 */     return cycle(Lists.newArrayList(elements));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Iterable<T> concat(Iterable<? extends T> a, Iterable<? extends T> b) {
/*  432 */     return FluentIterable.concat(a, b);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Iterable<T> concat(Iterable<? extends T> a, Iterable<? extends T> b, Iterable<? extends T> c) {
/*  448 */     return FluentIterable.concat(a, b, c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Iterable<T> concat(Iterable<? extends T> a, Iterable<? extends T> b, Iterable<? extends T> c, Iterable<? extends T> d) {
/*  468 */     return FluentIterable.concat(a, b, c, d);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @SafeVarargs
/*      */   public static <T> Iterable<T> concat(Iterable<? extends T>... inputs) {
/*  486 */     return FluentIterable.concat(inputs);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Iterable<T> concat(Iterable<? extends Iterable<? extends T>> inputs) {
/*  503 */     return FluentIterable.concat(inputs);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Iterable<List<T>> partition(final Iterable<T> iterable, final int size) {
/*  530 */     Preconditions.checkNotNull(iterable);
/*  531 */     Preconditions.checkArgument((size > 0));
/*  532 */     return new FluentIterable<List<T>>()
/*      */       {
/*      */         public Iterator<List<T>> iterator() {
/*  535 */           return Iterators.partition(iterable.iterator(), size);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Iterable<List<T>> paddedPartition(final Iterable<T> iterable, final int size) {
/*  557 */     Preconditions.checkNotNull(iterable);
/*  558 */     Preconditions.checkArgument((size > 0));
/*  559 */     return new FluentIterable<List<T>>()
/*      */       {
/*      */         public Iterator<List<T>> iterator() {
/*  562 */           return Iterators.paddedPartition(iterable.iterator(), size);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Iterable<T> filter(final Iterable<T> unfiltered, final Predicate<? super T> retainIfTrue) {
/*  575 */     Preconditions.checkNotNull(unfiltered);
/*  576 */     Preconditions.checkNotNull(retainIfTrue);
/*  577 */     return new FluentIterable<T>()
/*      */       {
/*      */         public Iterator<T> iterator() {
/*  580 */           return Iterators.filter(unfiltered.iterator(), retainIfTrue);
/*      */         }
/*      */ 
/*      */         
/*      */         public void forEach(Consumer<? super T> action) {
/*  585 */           Preconditions.checkNotNull(action);
/*  586 */           unfiltered.forEach(a -> {
/*      */                 if (retainIfTrue.test(a)) {
/*      */                   action.accept(a);
/*      */                 }
/*      */               });
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*      */         public Spliterator<T> spliterator() {
/*  596 */           return CollectSpliterators.filter(unfiltered.spliterator(), (Predicate<? super T>)retainIfTrue);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static <T> Iterable<T> filter(Iterable<?> unfiltered, Class<T> desiredType) {
/*  618 */     Preconditions.checkNotNull(unfiltered);
/*  619 */     Preconditions.checkNotNull(desiredType);
/*  620 */     return filter((Iterable)unfiltered, Predicates.instanceOf(desiredType));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> boolean any(Iterable<T> iterable, Predicate<? super T> predicate) {
/*  630 */     return Iterators.any(iterable.iterator(), predicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> boolean all(Iterable<T> iterable, Predicate<? super T> predicate) {
/*  641 */     return Iterators.all(iterable.iterator(), predicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @ParametricNullness
/*      */   public static <T> T find(Iterable<T> iterable, Predicate<? super T> predicate) {
/*  656 */     return Iterators.find(iterable.iterator(), predicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   public static <T> T find(Iterable<? extends T> iterable, Predicate<? super T> predicate, @CheckForNull T defaultValue) {
/*  689 */     return Iterators.find(iterable.iterator(), predicate, defaultValue);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Optional<T> tryFind(Iterable<T> iterable, Predicate<? super T> predicate) {
/*  704 */     return Iterators.tryFind(iterable.iterator(), predicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> int indexOf(Iterable<T> iterable, Predicate<? super T> predicate) {
/*  719 */     return Iterators.indexOf(iterable.iterator(), predicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <F, T> Iterable<T> transform(final Iterable<F> fromIterable, final Function<? super F, ? extends T> function) {
/*  737 */     Preconditions.checkNotNull(fromIterable);
/*  738 */     Preconditions.checkNotNull(function);
/*  739 */     return new FluentIterable<T>()
/*      */       {
/*      */         public Iterator<T> iterator() {
/*  742 */           return Iterators.transform(fromIterable.iterator(), function);
/*      */         }
/*      */ 
/*      */         
/*      */         public void forEach(Consumer<? super T> action) {
/*  747 */           Preconditions.checkNotNull(action);
/*  748 */           fromIterable.forEach(f -> action.accept(function.apply(f)));
/*      */         }
/*      */ 
/*      */         
/*      */         public Spliterator<T> spliterator() {
/*  753 */           return CollectSpliterators.map(fromIterable.spliterator(), (Function<?, ? extends T>)function);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @ParametricNullness
/*      */   public static <T> T get(Iterable<T> iterable, int position) {
/*  771 */     Preconditions.checkNotNull(iterable);
/*  772 */     return (iterable instanceof List) ? (
/*  773 */       (List<T>)iterable).get(position) : 
/*  774 */       Iterators.<T>get(iterable.iterator(), position);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @ParametricNullness
/*      */   public static <T> T get(Iterable<? extends T> iterable, int position, @ParametricNullness T defaultValue) {
/*  795 */     Preconditions.checkNotNull(iterable);
/*  796 */     Iterators.checkNonnegative(position);
/*  797 */     if (iterable instanceof List) {
/*  798 */       List<? extends T> list = Lists.cast(iterable);
/*  799 */       return (position < list.size()) ? list.get(position) : defaultValue;
/*      */     } 
/*  801 */     Iterator<? extends T> iterator = iterable.iterator();
/*  802 */     Iterators.advance(iterator, position);
/*  803 */     return Iterators.getNext(iterator, defaultValue);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @ParametricNullness
/*      */   public static <T> T getFirst(Iterable<? extends T> iterable, @ParametricNullness T defaultValue) {
/*  827 */     return Iterators.getNext(iterable.iterator(), defaultValue);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @ParametricNullness
/*      */   public static <T> T getLast(Iterable<T> iterable) {
/*  842 */     if (iterable instanceof List) {
/*  843 */       List<T> list = (List<T>)iterable;
/*  844 */       if (list.isEmpty()) {
/*  845 */         throw new NoSuchElementException();
/*      */       }
/*  847 */       return getLastInNonemptyList(list);
/*      */     } 
/*      */     
/*  850 */     return Iterators.getLast(iterable.iterator());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @ParametricNullness
/*      */   public static <T> T getLast(Iterable<? extends T> iterable, @ParametricNullness T defaultValue) {
/*  867 */     if (iterable instanceof Collection) {
/*  868 */       Collection<? extends T> c = (Collection<? extends T>)iterable;
/*  869 */       if (c.isEmpty())
/*  870 */         return defaultValue; 
/*  871 */       if (iterable instanceof List) {
/*  872 */         return getLastInNonemptyList(Lists.cast((Iterable)iterable));
/*      */       }
/*      */     } 
/*      */     
/*  876 */     return Iterators.getLast(iterable.iterator(), defaultValue);
/*      */   }
/*      */   
/*      */   @ParametricNullness
/*      */   private static <T> T getLastInNonemptyList(List<T> list) {
/*  881 */     return list.get(list.size() - 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Iterable<T> skip(final Iterable<T> iterable, final int numberToSkip) {
/*  905 */     Preconditions.checkNotNull(iterable);
/*  906 */     Preconditions.checkArgument((numberToSkip >= 0), "number to skip cannot be negative");
/*      */     
/*  908 */     return new FluentIterable<T>()
/*      */       {
/*      */         public Iterator<T> iterator() {
/*  911 */           if (iterable instanceof List) {
/*  912 */             List<T> list = (List<T>)iterable;
/*  913 */             int toSkip = Math.min(list.size(), numberToSkip);
/*  914 */             return list.subList(toSkip, list.size()).iterator();
/*      */           } 
/*  916 */           final Iterator<T> iterator = iterable.iterator();
/*      */           
/*  918 */           Iterators.advance(iterator, numberToSkip);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  925 */           return new Iterator(this)
/*      */             {
/*      */               boolean atStart = true;
/*      */               
/*      */               public boolean hasNext() {
/*  930 */                 return iterator.hasNext();
/*      */               }
/*      */ 
/*      */               
/*      */               @ParametricNullness
/*      */               public T next() {
/*  936 */                 T result = iterator.next();
/*  937 */                 this.atStart = false;
/*  938 */                 return result;
/*      */               }
/*      */ 
/*      */               
/*      */               public void remove() {
/*  943 */                 CollectPreconditions.checkRemove(!this.atStart);
/*  944 */                 iterator.remove();
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */         
/*      */         public Spliterator<T> spliterator() {
/*  951 */           if (iterable instanceof List) {
/*  952 */             List<T> list = (List<T>)iterable;
/*  953 */             int toSkip = Math.min(list.size(), numberToSkip);
/*  954 */             return list.subList(toSkip, list.size()).spliterator();
/*      */           } 
/*  956 */           return Streams.<T>stream(iterable).skip(numberToSkip).spliterator();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Iterable<T> limit(final Iterable<T> iterable, final int limitSize) {
/*  977 */     Preconditions.checkNotNull(iterable);
/*  978 */     Preconditions.checkArgument((limitSize >= 0), "limit is negative");
/*  979 */     return new FluentIterable<T>()
/*      */       {
/*      */         public Iterator<T> iterator() {
/*  982 */           return Iterators.limit(iterable.iterator(), limitSize);
/*      */         }
/*      */ 
/*      */         
/*      */         public Spliterator<T> spliterator() {
/*  987 */           return Streams.<T>stream(iterable).limit(limitSize).spliterator();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Iterable<T> consumingIterable(final Iterable<T> iterable) {
/* 1013 */     Preconditions.checkNotNull(iterable);
/*      */     
/* 1015 */     return new FluentIterable<T>()
/*      */       {
/*      */         public Iterator<T> iterator() {
/* 1018 */           return (iterable instanceof Queue) ? 
/* 1019 */             new ConsumingQueueIterator<>((Queue<T>)iterable) : 
/* 1020 */             Iterators.<T>consumingIterator(iterable.iterator());
/*      */         }
/*      */ 
/*      */         
/*      */         public String toString() {
/* 1025 */           return "Iterables.consumingIterable(...)";
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEmpty(Iterable<?> iterable) {
/* 1044 */     if (iterable instanceof Collection) {
/* 1045 */       return ((Collection)iterable).isEmpty();
/*      */     }
/* 1047 */     return !iterable.iterator().hasNext();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Iterable<T> mergeSorted(final Iterable<? extends Iterable<? extends T>> iterables, final Comparator<? super T> comparator) {
/* 1065 */     Preconditions.checkNotNull(iterables, "iterables");
/* 1066 */     Preconditions.checkNotNull(comparator, "comparator");
/* 1067 */     Iterable<T> iterable = new FluentIterable<T>()
/*      */       {
/*      */         public Iterator<T> iterator()
/*      */         {
/* 1071 */           return Iterators.mergeSorted(
/* 1072 */               Iterables.transform(iterables, Iterable::iterator), comparator);
/*      */         }
/*      */       };
/* 1075 */     return new UnmodifiableIterable<>(iterable);
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/Iterables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */