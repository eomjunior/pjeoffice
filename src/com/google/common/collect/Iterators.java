/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Optional;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import com.google.common.primitives.Ints;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import java.util.ArrayDeque;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.Deque;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Objects;
/*      */ import java.util.PriorityQueue;
/*      */ import java.util.Queue;
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
/*      */ 
/*      */ 
/*      */ @ElementTypesAreNonnullByDefault
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class Iterators
/*      */ {
/*      */   static <T> UnmodifiableIterator<T> emptyIterator() {
/*   80 */     return emptyListIterator();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <T> UnmodifiableListIterator<T> emptyListIterator() {
/*   91 */     return (UnmodifiableListIterator)ArrayItr.EMPTY;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private enum EmptyModifiableIterator
/*      */     implements Iterator<Object>
/*      */   {
/*   99 */     INSTANCE;
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/*  103 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public Object next() {
/*  108 */       throw new NoSuchElementException();
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/*  113 */       CollectPreconditions.checkRemove(false);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <T> Iterator<T> emptyModifiableIterator() {
/*  124 */     return EmptyModifiableIterator.INSTANCE;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> UnmodifiableIterator<T> unmodifiableIterator(final Iterator<? extends T> iterator) {
/*  130 */     Preconditions.checkNotNull(iterator);
/*  131 */     if (iterator instanceof UnmodifiableIterator) {
/*      */       
/*  133 */       UnmodifiableIterator<T> result = (UnmodifiableIterator)iterator;
/*  134 */       return result;
/*      */     } 
/*  136 */     return new UnmodifiableIterator<T>()
/*      */       {
/*      */         public boolean hasNext() {
/*  139 */           return iterator.hasNext();
/*      */         }
/*      */ 
/*      */         
/*      */         @ParametricNullness
/*      */         public T next() {
/*  145 */           return iterator.next();
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
/*      */   @Deprecated
/*      */   public static <T> UnmodifiableIterator<T> unmodifiableIterator(UnmodifiableIterator<T> iterator) {
/*  159 */     return (UnmodifiableIterator<T>)Preconditions.checkNotNull(iterator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int size(Iterator<?> iterator) {
/*  167 */     long count = 0L;
/*  168 */     while (iterator.hasNext()) {
/*  169 */       iterator.next();
/*  170 */       count++;
/*      */     } 
/*  172 */     return Ints.saturatedCast(count);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean contains(Iterator<?> iterator, @CheckForNull Object element) {
/*  177 */     if (element == null) {
/*  178 */       while (iterator.hasNext()) {
/*  179 */         if (iterator.next() == null) {
/*  180 */           return true;
/*      */         }
/*      */       } 
/*      */     } else {
/*  184 */       while (iterator.hasNext()) {
/*  185 */         if (element.equals(iterator.next())) {
/*  186 */           return true;
/*      */         }
/*      */       } 
/*      */     } 
/*  190 */     return false;
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
/*      */   @CanIgnoreReturnValue
/*      */   public static boolean removeAll(Iterator<?> removeFrom, Collection<?> elementsToRemove) {
/*  203 */     Preconditions.checkNotNull(elementsToRemove);
/*  204 */     boolean result = false;
/*  205 */     while (removeFrom.hasNext()) {
/*  206 */       if (elementsToRemove.contains(removeFrom.next())) {
/*  207 */         removeFrom.remove();
/*  208 */         result = true;
/*      */       } 
/*      */     } 
/*  211 */     return result;
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
/*      */   public static <T> boolean removeIf(Iterator<T> removeFrom, Predicate<? super T> predicate) {
/*  226 */     Preconditions.checkNotNull(predicate);
/*  227 */     boolean modified = false;
/*  228 */     while (removeFrom.hasNext()) {
/*  229 */       if (predicate.apply(removeFrom.next())) {
/*  230 */         removeFrom.remove();
/*  231 */         modified = true;
/*      */       } 
/*      */     } 
/*  234 */     return modified;
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
/*      */   @CanIgnoreReturnValue
/*      */   public static boolean retainAll(Iterator<?> removeFrom, Collection<?> elementsToRetain) {
/*  248 */     Preconditions.checkNotNull(elementsToRetain);
/*  249 */     boolean result = false;
/*  250 */     while (removeFrom.hasNext()) {
/*  251 */       if (!elementsToRetain.contains(removeFrom.next())) {
/*  252 */         removeFrom.remove();
/*  253 */         result = true;
/*      */       } 
/*      */     } 
/*  256 */     return result;
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
/*      */   public static boolean elementsEqual(Iterator<?> iterator1, Iterator<?> iterator2) {
/*  269 */     while (iterator1.hasNext()) {
/*  270 */       if (!iterator2.hasNext()) {
/*  271 */         return false;
/*      */       }
/*  273 */       Object o1 = iterator1.next();
/*  274 */       Object o2 = iterator2.next();
/*  275 */       if (!Objects.equal(o1, o2)) {
/*  276 */         return false;
/*      */       }
/*      */     } 
/*  279 */     return !iterator2.hasNext();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(Iterator<?> iterator) {
/*  287 */     StringBuilder sb = (new StringBuilder()).append('[');
/*  288 */     boolean first = true;
/*  289 */     while (iterator.hasNext()) {
/*  290 */       if (!first) {
/*  291 */         sb.append(", ");
/*      */       }
/*  293 */       first = false;
/*  294 */       sb.append(iterator.next());
/*      */     } 
/*  296 */     return sb.append(']').toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @ParametricNullness
/*      */   public static <T> T getOnlyElement(Iterator<T> iterator) {
/*  308 */     T first = iterator.next();
/*  309 */     if (!iterator.hasNext()) {
/*  310 */       return first;
/*      */     }
/*      */     
/*  313 */     StringBuilder sb = (new StringBuilder()).append("expected one element but was: <").append(first);
/*  314 */     for (int i = 0; i < 4 && iterator.hasNext(); i++) {
/*  315 */       sb.append(", ").append(iterator.next());
/*      */     }
/*  317 */     if (iterator.hasNext()) {
/*  318 */       sb.append(", ...");
/*      */     }
/*  320 */     sb.append('>');
/*      */     
/*  322 */     throw new IllegalArgumentException(sb.toString());
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
/*      */   @ParametricNullness
/*      */   public static <T> T getOnlyElement(Iterator<? extends T> iterator, @ParametricNullness T defaultValue) {
/*  335 */     return iterator.hasNext() ? getOnlyElement((Iterator)iterator) : defaultValue;
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
/*      */   @GwtIncompatible
/*      */   public static <T> T[] toArray(Iterator<? extends T> iterator, Class<T> type) {
/*  349 */     List<T> list = Lists.newArrayList(iterator);
/*  350 */     return Iterables.toArray(list, type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> boolean addAll(Collection<T> addTo, Iterator<? extends T> iterator) {
/*  362 */     Preconditions.checkNotNull(addTo);
/*  363 */     Preconditions.checkNotNull(iterator);
/*  364 */     boolean wasModified = false;
/*  365 */     while (iterator.hasNext()) {
/*  366 */       wasModified |= addTo.add(iterator.next());
/*      */     }
/*  368 */     return wasModified;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int frequency(Iterator<?> iterator, @CheckForNull Object element) {
/*  378 */     int count = 0;
/*  379 */     while (contains(iterator, element))
/*      */     {
/*      */       
/*  382 */       count++;
/*      */     }
/*  384 */     return count;
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
/*      */   public static <T> Iterator<T> cycle(final Iterable<T> iterable) {
/*  400 */     Preconditions.checkNotNull(iterable);
/*  401 */     return new Iterator<T>() {
/*  402 */         Iterator<T> iterator = Iterators.emptyModifiableIterator();
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
/*      */         public boolean hasNext() {
/*  415 */           return (this.iterator.hasNext() || iterable.iterator().hasNext());
/*      */         }
/*      */ 
/*      */         
/*      */         @ParametricNullness
/*      */         public T next() {
/*  421 */           if (!this.iterator.hasNext()) {
/*  422 */             this.iterator = iterable.iterator();
/*  423 */             if (!this.iterator.hasNext()) {
/*  424 */               throw new NoSuchElementException();
/*      */             }
/*      */           } 
/*  427 */           return this.iterator.next();
/*      */         }
/*      */ 
/*      */         
/*      */         public void remove() {
/*  432 */           this.iterator.remove();
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
/*      */   @SafeVarargs
/*      */   public static <T> Iterator<T> cycle(T... elements) {
/*  451 */     return cycle(Lists.newArrayList(elements));
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
/*      */   private static <I extends Iterator<?>> Iterator<I> consumingForArray(I... elements) {
/*  465 */     return new UnmodifiableIterator<I>() {
/*  466 */         int index = 0;
/*      */ 
/*      */         
/*      */         public boolean hasNext() {
/*  470 */           return (this.index < elements.length);
/*      */         }
/*      */ 
/*      */         
/*      */         public I next() {
/*  475 */           if (!hasNext()) {
/*  476 */             throw new NoSuchElementException();
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  482 */           Iterator iterator = Objects.<Iterator>requireNonNull(elements[this.index]);
/*  483 */           elements[this.index] = null;
/*  484 */           this.index++;
/*  485 */           return (I)iterator;
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
/*      */   public static <T> Iterator<T> concat(Iterator<? extends T> a, Iterator<? extends T> b) {
/*  500 */     Preconditions.checkNotNull(a);
/*  501 */     Preconditions.checkNotNull(b);
/*  502 */     return concat(consumingForArray((Iterator<? extends T>[])new Iterator[] { a, b }));
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
/*      */   public static <T> Iterator<T> concat(Iterator<? extends T> a, Iterator<? extends T> b, Iterator<? extends T> c) {
/*  515 */     Preconditions.checkNotNull(a);
/*  516 */     Preconditions.checkNotNull(b);
/*  517 */     Preconditions.checkNotNull(c);
/*  518 */     return concat(consumingForArray((Iterator<? extends T>[])new Iterator[] { a, b, c }));
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
/*      */   public static <T> Iterator<T> concat(Iterator<? extends T> a, Iterator<? extends T> b, Iterator<? extends T> c, Iterator<? extends T> d) {
/*  535 */     Preconditions.checkNotNull(a);
/*  536 */     Preconditions.checkNotNull(b);
/*  537 */     Preconditions.checkNotNull(c);
/*  538 */     Preconditions.checkNotNull(d);
/*  539 */     return concat(consumingForArray((Iterator<? extends T>[])new Iterator[] { a, b, c, d }));
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
/*      */   public static <T> Iterator<T> concat(Iterator<? extends T>... inputs) {
/*  553 */     return concatNoDefensiveCopy(Arrays.<Iterator<? extends T>>copyOf(inputs, inputs.length));
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
/*      */   public static <T> Iterator<T> concat(Iterator<? extends Iterator<? extends T>> inputs) {
/*  567 */     return new ConcatenatedIterator<>(inputs);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <T> Iterator<T> concatNoDefensiveCopy(Iterator<? extends T>... inputs) {
/*  573 */     for (Iterator<? extends T> input : (Iterator[])Preconditions.checkNotNull(inputs)) {
/*  574 */       Preconditions.checkNotNull(input);
/*      */     }
/*  576 */     return concat(consumingForArray(inputs));
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
/*      */   public static <T> UnmodifiableIterator<List<T>> partition(Iterator<T> iterator, int size) {
/*  599 */     return partitionImpl(iterator, size, false);
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
/*      */   public static <T> UnmodifiableIterator<List<T>> paddedPartition(Iterator<T> iterator, int size) {
/*  618 */     return partitionImpl(iterator, size, true);
/*      */   }
/*      */ 
/*      */   
/*      */   private static <T> UnmodifiableIterator<List<T>> partitionImpl(final Iterator<T> iterator, final int size, final boolean pad) {
/*  623 */     Preconditions.checkNotNull(iterator);
/*  624 */     Preconditions.checkArgument((size > 0));
/*  625 */     return new UnmodifiableIterator<List<T>>()
/*      */       {
/*      */         public boolean hasNext() {
/*  628 */           return iterator.hasNext();
/*      */         }
/*      */ 
/*      */         
/*      */         public List<T> next() {
/*  633 */           if (!hasNext()) {
/*  634 */             throw new NoSuchElementException();
/*      */           }
/*      */ 
/*      */           
/*  638 */           T[] array = (T[])new Object[size];
/*  639 */           int count = 0;
/*  640 */           for (; count < size && iterator.hasNext(); count++) {
/*  641 */             array[count] = iterator.next();
/*      */           }
/*  643 */           for (int i = count; i < size; i++) {
/*  644 */             array[i] = null;
/*      */           }
/*      */           
/*  647 */           List<T> list = Collections.unmodifiableList(Arrays.asList(array));
/*      */           
/*  649 */           if (pad || count == size) {
/*  650 */             return list;
/*      */           }
/*  652 */           return list.subList(0, count);
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
/*      */   public static <T> UnmodifiableIterator<T> filter(final Iterator<T> unfiltered, final Predicate<? super T> retainIfTrue) {
/*  664 */     Preconditions.checkNotNull(unfiltered);
/*  665 */     Preconditions.checkNotNull(retainIfTrue);
/*  666 */     return new AbstractIterator<T>()
/*      */       {
/*      */         @CheckForNull
/*      */         protected T computeNext() {
/*  670 */           while (unfiltered.hasNext()) {
/*  671 */             T element = unfiltered.next();
/*  672 */             if (retainIfTrue.apply(element)) {
/*  673 */               return element;
/*      */             }
/*      */           } 
/*  676 */           return endOfData();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static <T> UnmodifiableIterator<T> filter(Iterator<?> unfiltered, Class<T> desiredType) {
/*  688 */     return filter((Iterator)unfiltered, Predicates.instanceOf(desiredType));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> boolean any(Iterator<T> iterator, Predicate<? super T> predicate) {
/*  697 */     return (indexOf(iterator, predicate) != -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> boolean all(Iterator<T> iterator, Predicate<? super T> predicate) {
/*  706 */     Preconditions.checkNotNull(predicate);
/*  707 */     while (iterator.hasNext()) {
/*  708 */       T element = iterator.next();
/*  709 */       if (!predicate.apply(element)) {
/*  710 */         return false;
/*      */       }
/*      */     } 
/*  713 */     return true;
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
/*      */   public static <T> T find(Iterator<T> iterator, Predicate<? super T> predicate) {
/*  728 */     Preconditions.checkNotNull(iterator);
/*  729 */     Preconditions.checkNotNull(predicate);
/*  730 */     while (iterator.hasNext()) {
/*  731 */       T t = iterator.next();
/*  732 */       if (predicate.apply(t)) {
/*  733 */         return t;
/*      */       }
/*      */     } 
/*  736 */     throw new NoSuchElementException();
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
/*      */   @CheckForNull
/*      */   public static <T> T find(Iterator<? extends T> iterator, Predicate<? super T> predicate, @CheckForNull T defaultValue) {
/*  753 */     Preconditions.checkNotNull(iterator);
/*  754 */     Preconditions.checkNotNull(predicate);
/*  755 */     while (iterator.hasNext()) {
/*  756 */       T t = iterator.next();
/*  757 */       if (predicate.apply(t)) {
/*  758 */         return t;
/*      */       }
/*      */     } 
/*  761 */     return defaultValue;
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
/*      */   public static <T> Optional<T> tryFind(Iterator<T> iterator, Predicate<? super T> predicate) {
/*  776 */     Preconditions.checkNotNull(iterator);
/*  777 */     Preconditions.checkNotNull(predicate);
/*  778 */     while (iterator.hasNext()) {
/*  779 */       T t = iterator.next();
/*  780 */       if (predicate.apply(t)) {
/*  781 */         return Optional.of(t);
/*      */       }
/*      */     } 
/*  784 */     return Optional.absent();
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
/*      */   public static <T> int indexOf(Iterator<T> iterator, Predicate<? super T> predicate) {
/*  803 */     Preconditions.checkNotNull(predicate, "predicate");
/*  804 */     for (int i = 0; iterator.hasNext(); i++) {
/*  805 */       T current = iterator.next();
/*  806 */       if (predicate.apply(current)) {
/*  807 */         return i;
/*      */       }
/*      */     } 
/*  810 */     return -1;
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
/*      */   public static <F, T> Iterator<T> transform(Iterator<F> fromIterator, final Function<? super F, ? extends T> function) {
/*  823 */     Preconditions.checkNotNull(function);
/*  824 */     return new TransformedIterator<F, T>(fromIterator)
/*      */       {
/*      */         @ParametricNullness
/*      */         T transform(@ParametricNullness F from) {
/*  828 */           return (T)function.apply(from);
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
/*      */   @ParametricNullness
/*      */   public static <T> T get(Iterator<T> iterator, int position) {
/*  844 */     checkNonnegative(position);
/*  845 */     int skipped = advance(iterator, position);
/*  846 */     if (!iterator.hasNext()) {
/*  847 */       throw new IndexOutOfBoundsException("position (" + position + ") must be less than the number of elements that remained (" + skipped + ")");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  854 */     return iterator.next();
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
/*      */   @ParametricNullness
/*      */   public static <T> T get(Iterator<? extends T> iterator, int position, @ParametricNullness T defaultValue) {
/*  872 */     checkNonnegative(position);
/*  873 */     advance(iterator, position);
/*  874 */     return getNext(iterator, defaultValue);
/*      */   }
/*      */   
/*      */   static void checkNonnegative(int position) {
/*  878 */     if (position < 0) {
/*  879 */       throw new IndexOutOfBoundsException("position (" + position + ") must not be negative");
/*      */     }
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
/*      */   public static <T> T getNext(Iterator<? extends T> iterator, @ParametricNullness T defaultValue) {
/*  894 */     return iterator.hasNext() ? iterator.next() : defaultValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @ParametricNullness
/*      */   public static <T> T getLast(Iterator<T> iterator) {
/*      */     while (true) {
/*  906 */       T current = iterator.next();
/*  907 */       if (!iterator.hasNext()) {
/*  908 */         return current;
/*      */       }
/*      */     } 
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
/*      */   public static <T> T getLast(Iterator<? extends T> iterator, @ParametricNullness T defaultValue) {
/*  924 */     return iterator.hasNext() ? getLast((Iterator)iterator) : defaultValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static int advance(Iterator<?> iterator, int numberToAdvance) {
/*  936 */     Preconditions.checkNotNull(iterator);
/*  937 */     Preconditions.checkArgument((numberToAdvance >= 0), "numberToAdvance must be nonnegative");
/*      */     
/*      */     int i;
/*  940 */     for (i = 0; i < numberToAdvance && iterator.hasNext(); i++) {
/*  941 */       iterator.next();
/*      */     }
/*  943 */     return i;
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
/*      */   public static <T> Iterator<T> limit(final Iterator<T> iterator, final int limitSize) {
/*  958 */     Preconditions.checkNotNull(iterator);
/*  959 */     Preconditions.checkArgument((limitSize >= 0), "limit is negative");
/*  960 */     return new Iterator<T>()
/*      */       {
/*      */         private int count;
/*      */         
/*      */         public boolean hasNext() {
/*  965 */           return (this.count < limitSize && iterator.hasNext());
/*      */         }
/*      */ 
/*      */         
/*      */         @ParametricNullness
/*      */         public T next() {
/*  971 */           if (!hasNext()) {
/*  972 */             throw new NoSuchElementException();
/*      */           }
/*  974 */           this.count++;
/*  975 */           return iterator.next();
/*      */         }
/*      */ 
/*      */         
/*      */         public void remove() {
/*  980 */           iterator.remove();
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
/*      */   public static <T> Iterator<T> consumingIterator(final Iterator<T> iterator) {
/*  998 */     Preconditions.checkNotNull(iterator);
/*  999 */     return new UnmodifiableIterator<T>()
/*      */       {
/*      */         public boolean hasNext() {
/* 1002 */           return iterator.hasNext();
/*      */         }
/*      */ 
/*      */         
/*      */         @ParametricNullness
/*      */         public T next() {
/* 1008 */           T next = iterator.next();
/* 1009 */           iterator.remove();
/* 1010 */           return next;
/*      */         }
/*      */ 
/*      */         
/*      */         public String toString() {
/* 1015 */           return "Iterators.consumingIterator(...)";
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   static <T> T pollNext(Iterator<T> iterator) {
/* 1026 */     if (iterator.hasNext()) {
/* 1027 */       T result = iterator.next();
/* 1028 */       iterator.remove();
/* 1029 */       return result;
/*      */     } 
/* 1031 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void clear(Iterator<?> iterator) {
/* 1039 */     Preconditions.checkNotNull(iterator);
/* 1040 */     while (iterator.hasNext()) {
/* 1041 */       iterator.next();
/* 1042 */       iterator.remove();
/*      */     } 
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
/*      */   @SafeVarargs
/*      */   public static <T> UnmodifiableIterator<T> forArray(T... array) {
/* 1058 */     return forArrayWithPosition(array, 0);
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
/*      */   static <T> UnmodifiableListIterator<T> forArrayWithPosition(T[] array, int position) {
/* 1070 */     if (array.length == 0) {
/* 1071 */       Preconditions.checkPositionIndex(position, array.length);
/* 1072 */       return emptyListIterator();
/*      */     } 
/* 1074 */     return new ArrayItr<>(array, position);
/*      */   }
/*      */   
/*      */   private static final class ArrayItr<T>
/*      */     extends AbstractIndexedListIterator<T> {
/* 1079 */     static final UnmodifiableListIterator<Object> EMPTY = new ArrayItr((T[])new Object[0], 0);
/*      */     
/*      */     private final T[] array;
/*      */     
/*      */     ArrayItr(T[] array, int position) {
/* 1084 */       super(array.length, position);
/* 1085 */       this.array = array;
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     protected T get(int index) {
/* 1091 */       return this.array[index];
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> UnmodifiableIterator<T> singletonIterator(@ParametricNullness T value) {
/* 1102 */     return new SingletonIterator<>(value);
/*      */   }
/*      */   
/*      */   private static final class SingletonIterator<T>
/*      */     extends UnmodifiableIterator<T> {
/* 1107 */     private static final Object SENTINEL = new Object();
/*      */     
/*      */     private Object valueOrSentinel;
/*      */     
/*      */     SingletonIterator(T value) {
/* 1112 */       this.valueOrSentinel = value;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/* 1117 */       return (this.valueOrSentinel != SENTINEL);
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public T next() {
/* 1123 */       if (this.valueOrSentinel == SENTINEL) {
/* 1124 */         throw new NoSuchElementException();
/*      */       }
/*      */ 
/*      */       
/* 1128 */       T t = (T)this.valueOrSentinel;
/* 1129 */       this.valueOrSentinel = SENTINEL;
/* 1130 */       return t;
/*      */     }
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
/*      */   public static <T> UnmodifiableIterator<T> forEnumeration(final Enumeration<T> enumeration) {
/* 1146 */     Preconditions.checkNotNull(enumeration);
/* 1147 */     return new UnmodifiableIterator<T>()
/*      */       {
/*      */         public boolean hasNext() {
/* 1150 */           return enumeration.hasMoreElements();
/*      */         }
/*      */ 
/*      */         
/*      */         @ParametricNullness
/*      */         public T next() {
/* 1156 */           return enumeration.nextElement();
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
/*      */   public static <T> Enumeration<T> asEnumeration(final Iterator<T> iterator) {
/* 1168 */     Preconditions.checkNotNull(iterator);
/* 1169 */     return new Enumeration<T>()
/*      */       {
/*      */         public boolean hasMoreElements() {
/* 1172 */           return iterator.hasNext();
/*      */         }
/*      */ 
/*      */         
/*      */         @ParametricNullness
/*      */         public T nextElement() {
/* 1178 */           return iterator.next();
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   private static class PeekingImpl<E>
/*      */     implements PeekingIterator<E> {
/*      */     private final Iterator<? extends E> iterator;
/*      */     private boolean hasPeeked;
/*      */     @CheckForNull
/*      */     private E peekedElement;
/*      */     
/*      */     public PeekingImpl(Iterator<? extends E> iterator) {
/* 1191 */       this.iterator = (Iterator<? extends E>)Preconditions.checkNotNull(iterator);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/* 1196 */       return (this.hasPeeked || this.iterator.hasNext());
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public E next() {
/* 1202 */       if (!this.hasPeeked) {
/* 1203 */         return this.iterator.next();
/*      */       }
/*      */       
/* 1206 */       E result = NullnessCasts.uncheckedCastNullableTToT(this.peekedElement);
/* 1207 */       this.hasPeeked = false;
/* 1208 */       this.peekedElement = null;
/* 1209 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/* 1214 */       Preconditions.checkState(!this.hasPeeked, "Can't remove after you've peeked at next");
/* 1215 */       this.iterator.remove();
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public E peek() {
/* 1221 */       if (!this.hasPeeked) {
/* 1222 */         this.peekedElement = this.iterator.next();
/* 1223 */         this.hasPeeked = true;
/*      */       } 
/*      */       
/* 1226 */       return NullnessCasts.uncheckedCastNullableTToT(this.peekedElement);
/*      */     }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> PeekingIterator<T> peekingIterator(Iterator<? extends T> iterator) {
/* 1268 */     if (iterator instanceof PeekingImpl) {
/*      */ 
/*      */ 
/*      */       
/* 1272 */       PeekingImpl<T> peeking = (PeekingImpl)iterator;
/* 1273 */       return peeking;
/*      */     } 
/* 1275 */     return new PeekingImpl<>(iterator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <T> PeekingIterator<T> peekingIterator(PeekingIterator<T> iterator) {
/* 1287 */     return (PeekingIterator<T>)Preconditions.checkNotNull(iterator);
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
/*      */   public static <T> UnmodifiableIterator<T> mergeSorted(Iterable<? extends Iterator<? extends T>> iterators, Comparator<? super T> comparator) {
/* 1304 */     Preconditions.checkNotNull(iterators, "iterators");
/* 1305 */     Preconditions.checkNotNull(comparator, "comparator");
/*      */     
/* 1307 */     return new MergingIterator<>(iterators, comparator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class MergingIterator<T>
/*      */     extends UnmodifiableIterator<T>
/*      */   {
/*      */     final Queue<PeekingIterator<T>> queue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public MergingIterator(Iterable<? extends Iterator<? extends T>> iterators, Comparator<? super T> itemComparator) {
/* 1326 */       Comparator<PeekingIterator<T>> heapComparator = (o1, o2) -> itemComparator.compare(o1.peek(), o2.peek());
/*      */ 
/*      */ 
/*      */       
/* 1330 */       this.queue = new PriorityQueue<>(2, heapComparator);
/*      */       
/* 1332 */       for (Iterator<? extends T> iterator : iterators) {
/* 1333 */         if (iterator.hasNext()) {
/* 1334 */           this.queue.add(Iterators.peekingIterator(iterator));
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/* 1341 */       return !this.queue.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public T next() {
/* 1347 */       PeekingIterator<T> nextIter = this.queue.remove();
/* 1348 */       T next = nextIter.next();
/* 1349 */       if (nextIter.hasNext()) {
/* 1350 */         this.queue.add(nextIter);
/*      */       }
/* 1352 */       return next;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class ConcatenatedIterator<T>
/*      */     implements Iterator<T>
/*      */   {
/*      */     @CheckForNull
/*      */     private Iterator<? extends T> toRemove;
/*      */ 
/*      */     
/*      */     private Iterator<? extends T> iterator;
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     private Iterator<? extends Iterator<? extends T>> topMetaIterator;
/*      */     
/*      */     @CheckForNull
/*      */     private Deque<Iterator<? extends Iterator<? extends T>>> metaIterators;
/*      */ 
/*      */     
/*      */     ConcatenatedIterator(Iterator<? extends Iterator<? extends T>> metaIterator) {
/* 1376 */       this.iterator = Iterators.emptyIterator();
/* 1377 */       this.topMetaIterator = (Iterator<? extends Iterator<? extends T>>)Preconditions.checkNotNull(metaIterator);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     private Iterator<? extends Iterator<? extends T>> getTopMetaIterator() {
/* 1383 */       while (this.topMetaIterator == null || !this.topMetaIterator.hasNext()) {
/* 1384 */         if (this.metaIterators != null && !this.metaIterators.isEmpty()) {
/* 1385 */           this.topMetaIterator = this.metaIterators.removeFirst(); continue;
/*      */         } 
/* 1387 */         return null;
/*      */       } 
/*      */       
/* 1390 */       return this.topMetaIterator;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/* 1395 */       while (!((Iterator)Preconditions.checkNotNull(this.iterator)).hasNext()) {
/*      */ 
/*      */ 
/*      */         
/* 1399 */         this.topMetaIterator = getTopMetaIterator();
/* 1400 */         if (this.topMetaIterator == null) {
/* 1401 */           return false;
/*      */         }
/*      */         
/* 1404 */         this.iterator = this.topMetaIterator.next();
/*      */         
/* 1406 */         if (this.iterator instanceof ConcatenatedIterator) {
/*      */ 
/*      */ 
/*      */           
/* 1410 */           ConcatenatedIterator<T> topConcat = (ConcatenatedIterator)this.iterator;
/* 1411 */           this.iterator = topConcat.iterator;
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1416 */           if (this.metaIterators == null) {
/* 1417 */             this.metaIterators = new ArrayDeque<>();
/*      */           }
/* 1419 */           this.metaIterators.addFirst(this.topMetaIterator);
/* 1420 */           if (topConcat.metaIterators != null) {
/* 1421 */             while (!topConcat.metaIterators.isEmpty()) {
/* 1422 */               this.metaIterators.addFirst(topConcat.metaIterators.removeLast());
/*      */             }
/*      */           }
/* 1425 */           this.topMetaIterator = topConcat.topMetaIterator;
/*      */         } 
/*      */       } 
/* 1428 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public T next() {
/* 1434 */       if (hasNext()) {
/* 1435 */         this.toRemove = this.iterator;
/* 1436 */         return this.iterator.next();
/*      */       } 
/* 1438 */       throw new NoSuchElementException();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void remove() {
/* 1444 */       if (this.toRemove == null) {
/* 1445 */         throw new IllegalStateException("no calls to next() since the last call to remove()");
/*      */       }
/* 1447 */       this.toRemove.remove();
/* 1448 */       this.toRemove = null;
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/Iterators.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */