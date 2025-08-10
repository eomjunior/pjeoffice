/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import com.google.common.math.IntMath;
/*      */ import com.google.common.primitives.Ints;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*      */ import java.io.Serializable;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.Iterator;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.Spliterator;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.function.Supplier;
/*      */ import java.util.function.ToIntFunction;
/*      */ import java.util.stream.Collector;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @ElementTypesAreNonnullByDefault
/*      */ @GwtCompatible
/*      */ public final class Multisets
/*      */ {
/*      */   public static <T, E, M extends Multiset<E>> Collector<T, ?, M> toMultiset(Function<? super T, E> elementFunction, ToIntFunction<? super T> countFunction, Supplier<M> multisetSupplier) {
/*   89 */     return CollectCollectors.toMultiset(elementFunction, countFunction, multisetSupplier);
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
/*      */   public static <E> Multiset<E> unmodifiableMultiset(Multiset<? extends E> multiset) {
/*  104 */     if (multiset instanceof UnmodifiableMultiset || multiset instanceof ImmutableMultiset)
/*      */     {
/*  106 */       return (Multiset)multiset;
/*      */     }
/*      */     
/*  109 */     return new UnmodifiableMultiset<>((Multiset<? extends E>)Preconditions.checkNotNull(multiset));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <E> Multiset<E> unmodifiableMultiset(ImmutableMultiset<E> multiset) {
/*  120 */     return (Multiset<E>)Preconditions.checkNotNull(multiset);
/*      */   }
/*      */   
/*      */   static class UnmodifiableMultiset<E>
/*      */     extends ForwardingMultiset<E> implements Serializable {
/*      */     final Multiset<? extends E> delegate;
/*      */     
/*      */     UnmodifiableMultiset(Multiset<? extends E> delegate) {
/*  128 */       this.delegate = delegate;
/*      */     } @LazyInit
/*      */     @CheckForNull
/*      */     transient Set<E> elementSet; @LazyInit
/*      */     @CheckForNull
/*      */     transient Set<Multiset.Entry<E>> entrySet; private static final long serialVersionUID = 0L;
/*      */     protected Multiset<E> delegate() {
/*  135 */       return (Multiset)this.delegate;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Set<E> createElementSet() {
/*  141 */       return Collections.unmodifiableSet(this.delegate.elementSet());
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<E> elementSet() {
/*  146 */       Set<E> es = this.elementSet;
/*  147 */       return (es == null) ? (this.elementSet = createElementSet()) : es;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Multiset.Entry<E>> entrySet() {
/*  155 */       Set<Multiset.Entry<E>> es = this.entrySet;
/*  156 */       return (es == null) ? (
/*      */ 
/*      */         
/*  159 */         this.entrySet = Collections.unmodifiableSet(this.delegate.entrySet())) : 
/*  160 */         es;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> iterator() {
/*  165 */       return Iterators.unmodifiableIterator(this.delegate.iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean add(@ParametricNullness E element) {
/*  170 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public int add(@ParametricNullness E element, int occurrences) {
/*  175 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean addAll(Collection<? extends E> elementsToAdd) {
/*  180 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(@CheckForNull Object element) {
/*  185 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public int remove(@CheckForNull Object element, int occurrences) {
/*  190 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> elementsToRemove) {
/*  195 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeIf(Predicate<? super E> filter) {
/*  200 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> elementsToRetain) {
/*  205 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  210 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public int setCount(@ParametricNullness E element, int count) {
/*  215 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean setCount(@ParametricNullness E element, int oldCount, int newCount) {
/*  220 */       throw new UnsupportedOperationException();
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
/*      */   public static <E> SortedMultiset<E> unmodifiableSortedMultiset(SortedMultiset<E> sortedMultiset) {
/*  240 */     return new UnmodifiableSortedMultiset<>((SortedMultiset<E>)Preconditions.checkNotNull(sortedMultiset));
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
/*      */   public static <E> Multiset.Entry<E> immutableEntry(@ParametricNullness E e, int n) {
/*  253 */     return new ImmutableEntry<>(e, n);
/*      */   }
/*      */   
/*      */   static class ImmutableEntry<E> extends AbstractEntry<E> implements Serializable { @ParametricNullness
/*      */     private final E element;
/*      */     private final int count;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     ImmutableEntry(@ParametricNullness E element, int count) {
/*  262 */       this.element = element;
/*  263 */       this.count = count;
/*  264 */       CollectPreconditions.checkNonnegative(count, "count");
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public final E getElement() {
/*  270 */       return this.element;
/*      */     }
/*      */ 
/*      */     
/*      */     public final int getCount() {
/*  275 */       return this.count;
/*      */     }
/*      */     
/*      */     @CheckForNull
/*      */     public ImmutableEntry<E> nextInBucket() {
/*  280 */       return null;
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> Multiset<E> filter(Multiset<E> unfiltered, Predicate<? super E> predicate) {
/*  313 */     if (unfiltered instanceof FilteredMultiset) {
/*      */ 
/*      */       
/*  316 */       FilteredMultiset<E> filtered = (FilteredMultiset<E>)unfiltered;
/*  317 */       Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
/*  318 */       return new FilteredMultiset<>(filtered.unfiltered, combinedPredicate);
/*      */     } 
/*  320 */     return new FilteredMultiset<>(unfiltered, predicate);
/*      */   }
/*      */   
/*      */   private static final class FilteredMultiset<E> extends ViewMultiset<E> {
/*      */     final Multiset<E> unfiltered;
/*      */     final Predicate<? super E> predicate;
/*      */     
/*      */     FilteredMultiset(Multiset<E> unfiltered, Predicate<? super E> predicate) {
/*  328 */       this.unfiltered = (Multiset<E>)Preconditions.checkNotNull(unfiltered);
/*  329 */       this.predicate = (Predicate<? super E>)Preconditions.checkNotNull(predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public UnmodifiableIterator<E> iterator() {
/*  334 */       return Iterators.filter(this.unfiltered.iterator(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     Set<E> createElementSet() {
/*  339 */       return Sets.filter(this.unfiltered.elementSet(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<E> elementIterator() {
/*  344 */       throw new AssertionError("should never be called");
/*      */     }
/*      */ 
/*      */     
/*      */     Set<Multiset.Entry<E>> createEntrySet() {
/*  349 */       return Sets.filter(this.unfiltered
/*  350 */           .entrySet(), new Predicate<Multiset.Entry<E>>()
/*      */           {
/*      */             public boolean apply(Multiset.Entry<E> entry)
/*      */             {
/*  354 */               return Multisets.FilteredMultiset.this.predicate.apply(entry.getElement());
/*      */             }
/*      */           });
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Multiset.Entry<E>> entryIterator() {
/*  361 */       throw new AssertionError("should never be called");
/*      */     }
/*      */ 
/*      */     
/*      */     public int count(@CheckForNull Object element) {
/*  366 */       int count = this.unfiltered.count(element);
/*  367 */       if (count > 0) {
/*      */         
/*  369 */         E e = (E)element;
/*  370 */         return this.predicate.apply(e) ? count : 0;
/*      */       } 
/*  372 */       return 0;
/*      */     }
/*      */ 
/*      */     
/*      */     public int add(@ParametricNullness E element, int occurrences) {
/*  377 */       Preconditions.checkArgument(this.predicate
/*  378 */           .apply(element), "Element %s does not match predicate %s", element, this.predicate);
/*  379 */       return this.unfiltered.add(element, occurrences);
/*      */     }
/*      */ 
/*      */     
/*      */     public int remove(@CheckForNull Object element, int occurrences) {
/*  384 */       CollectPreconditions.checkNonnegative(occurrences, "occurrences");
/*  385 */       if (occurrences == 0) {
/*  386 */         return count(element);
/*      */       }
/*  388 */       return contains(element) ? this.unfiltered.remove(element, occurrences) : 0;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int inferDistinctElements(Iterable<?> elements) {
/*  399 */     if (elements instanceof Multiset) {
/*  400 */       return ((Multiset)elements).elementSet().size();
/*      */     }
/*  402 */     return 11;
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
/*      */   public static <E> Multiset<E> union(final Multiset<? extends E> multiset1, final Multiset<? extends E> multiset2) {
/*  419 */     Preconditions.checkNotNull(multiset1);
/*  420 */     Preconditions.checkNotNull(multiset2);
/*      */     
/*  422 */     return new ViewMultiset<E>()
/*      */       {
/*      */         public boolean contains(@CheckForNull Object element) {
/*  425 */           return (multiset1.contains(element) || multiset2.contains(element));
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isEmpty() {
/*  430 */           return (multiset1.isEmpty() && multiset2.isEmpty());
/*      */         }
/*      */ 
/*      */         
/*      */         public int count(@CheckForNull Object element) {
/*  435 */           return Math.max(multiset1.count(element), multiset2.count(element));
/*      */         }
/*      */ 
/*      */         
/*      */         Set<E> createElementSet() {
/*  440 */           return Sets.union(multiset1.elementSet(), multiset2.elementSet());
/*      */         }
/*      */ 
/*      */         
/*      */         Iterator<E> elementIterator() {
/*  445 */           throw new AssertionError("should never be called");
/*      */         }
/*      */ 
/*      */         
/*      */         Iterator<Multiset.Entry<E>> entryIterator() {
/*  450 */           final Iterator<? extends Multiset.Entry<? extends E>> iterator1 = multiset1.entrySet().iterator();
/*  451 */           final Iterator<? extends Multiset.Entry<? extends E>> iterator2 = multiset2.entrySet().iterator();
/*      */           
/*  453 */           return new AbstractIterator()
/*      */             {
/*      */               @CheckForNull
/*      */               protected Multiset.Entry<E> computeNext() {
/*  457 */                 if (iterator1.hasNext()) {
/*  458 */                   Multiset.Entry<? extends E> entry1 = iterator1.next();
/*  459 */                   E element = entry1.getElement();
/*  460 */                   int count = Math.max(entry1.getCount(), multiset2.count(element));
/*  461 */                   return Multisets.immutableEntry(element, count);
/*      */                 } 
/*  463 */                 while (iterator2.hasNext()) {
/*  464 */                   Multiset.Entry<? extends E> entry2 = iterator2.next();
/*  465 */                   E element = entry2.getElement();
/*  466 */                   if (!multiset1.contains(element)) {
/*  467 */                     return Multisets.immutableEntry(element, entry2.getCount());
/*      */                   }
/*      */                 } 
/*  470 */                 return endOfData();
/*      */               }
/*      */             };
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
/*      */   public static <E> Multiset<E> intersection(final Multiset<E> multiset1, final Multiset<?> multiset2) {
/*  491 */     Preconditions.checkNotNull(multiset1);
/*  492 */     Preconditions.checkNotNull(multiset2);
/*      */     
/*  494 */     return new ViewMultiset<E>()
/*      */       {
/*      */         public int count(@CheckForNull Object element) {
/*  497 */           int count1 = multiset1.count(element);
/*  498 */           return (count1 == 0) ? 0 : Math.min(count1, multiset2.count(element));
/*      */         }
/*      */ 
/*      */         
/*      */         Set<E> createElementSet() {
/*  503 */           return Sets.intersection(multiset1.elementSet(), multiset2.elementSet());
/*      */         }
/*      */ 
/*      */         
/*      */         Iterator<E> elementIterator() {
/*  508 */           throw new AssertionError("should never be called");
/*      */         }
/*      */ 
/*      */         
/*      */         Iterator<Multiset.Entry<E>> entryIterator() {
/*  513 */           final Iterator<Multiset.Entry<E>> iterator1 = multiset1.entrySet().iterator();
/*      */           
/*  515 */           return new AbstractIterator()
/*      */             {
/*      */               @CheckForNull
/*      */               protected Multiset.Entry<E> computeNext() {
/*  519 */                 while (iterator1.hasNext()) {
/*  520 */                   Multiset.Entry<E> entry1 = iterator1.next();
/*  521 */                   E element = entry1.getElement();
/*  522 */                   int count = Math.min(entry1.getCount(), multiset2.count(element));
/*  523 */                   if (count > 0) {
/*  524 */                     return Multisets.immutableEntry(element, count);
/*      */                   }
/*      */                 } 
/*  527 */                 return endOfData();
/*      */               }
/*      */             };
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
/*      */   public static <E> Multiset<E> sum(final Multiset<? extends E> multiset1, final Multiset<? extends E> multiset2) {
/*  548 */     Preconditions.checkNotNull(multiset1);
/*  549 */     Preconditions.checkNotNull(multiset2);
/*      */ 
/*      */     
/*  552 */     return new ViewMultiset<E>()
/*      */       {
/*      */         public boolean contains(@CheckForNull Object element) {
/*  555 */           return (multiset1.contains(element) || multiset2.contains(element));
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isEmpty() {
/*  560 */           return (multiset1.isEmpty() && multiset2.isEmpty());
/*      */         }
/*      */ 
/*      */         
/*      */         public int size() {
/*  565 */           return IntMath.saturatedAdd(multiset1.size(), multiset2.size());
/*      */         }
/*      */ 
/*      */         
/*      */         public int count(@CheckForNull Object element) {
/*  570 */           return multiset1.count(element) + multiset2.count(element);
/*      */         }
/*      */ 
/*      */         
/*      */         Set<E> createElementSet() {
/*  575 */           return Sets.union(multiset1.elementSet(), multiset2.elementSet());
/*      */         }
/*      */ 
/*      */         
/*      */         Iterator<E> elementIterator() {
/*  580 */           throw new AssertionError("should never be called");
/*      */         }
/*      */ 
/*      */         
/*      */         Iterator<Multiset.Entry<E>> entryIterator() {
/*  585 */           final Iterator<? extends Multiset.Entry<? extends E>> iterator1 = multiset1.entrySet().iterator();
/*  586 */           final Iterator<? extends Multiset.Entry<? extends E>> iterator2 = multiset2.entrySet().iterator();
/*  587 */           return new AbstractIterator()
/*      */             {
/*      */               @CheckForNull
/*      */               protected Multiset.Entry<E> computeNext() {
/*  591 */                 if (iterator1.hasNext()) {
/*  592 */                   Multiset.Entry<? extends E> entry1 = iterator1.next();
/*  593 */                   E element = entry1.getElement();
/*  594 */                   int count = entry1.getCount() + multiset2.count(element);
/*  595 */                   return Multisets.immutableEntry(element, count);
/*      */                 } 
/*  597 */                 while (iterator2.hasNext()) {
/*  598 */                   Multiset.Entry<? extends E> entry2 = iterator2.next();
/*  599 */                   E element = entry2.getElement();
/*  600 */                   if (!multiset1.contains(element)) {
/*  601 */                     return Multisets.immutableEntry(element, entry2.getCount());
/*      */                   }
/*      */                 } 
/*  604 */                 return endOfData();
/*      */               }
/*      */             };
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
/*      */   public static <E> Multiset<E> difference(final Multiset<E> multiset1, final Multiset<?> multiset2) {
/*  625 */     Preconditions.checkNotNull(multiset1);
/*  626 */     Preconditions.checkNotNull(multiset2);
/*      */ 
/*      */     
/*  629 */     return new ViewMultiset<E>()
/*      */       {
/*      */         public int count(@CheckForNull Object element) {
/*  632 */           int count1 = multiset1.count(element);
/*  633 */           return (count1 == 0) ? 0 : Math.max(0, count1 - multiset2.count(element));
/*      */         }
/*      */ 
/*      */         
/*      */         public void clear() {
/*  638 */           throw new UnsupportedOperationException();
/*      */         }
/*      */ 
/*      */         
/*      */         Iterator<E> elementIterator() {
/*  643 */           final Iterator<Multiset.Entry<E>> iterator1 = multiset1.entrySet().iterator();
/*  644 */           return new AbstractIterator()
/*      */             {
/*      */               @CheckForNull
/*      */               protected E computeNext() {
/*  648 */                 while (iterator1.hasNext()) {
/*  649 */                   Multiset.Entry<E> entry1 = iterator1.next();
/*  650 */                   E element = entry1.getElement();
/*  651 */                   if (entry1.getCount() > multiset2.count(element)) {
/*  652 */                     return element;
/*      */                   }
/*      */                 } 
/*  655 */                 return endOfData();
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */         
/*      */         Iterator<Multiset.Entry<E>> entryIterator() {
/*  662 */           final Iterator<Multiset.Entry<E>> iterator1 = multiset1.entrySet().iterator();
/*  663 */           return new AbstractIterator()
/*      */             {
/*      */               @CheckForNull
/*      */               protected Multiset.Entry<E> computeNext() {
/*  667 */                 while (iterator1.hasNext()) {
/*  668 */                   Multiset.Entry<E> entry1 = iterator1.next();
/*  669 */                   E element = entry1.getElement();
/*  670 */                   int count = entry1.getCount() - multiset2.count(element);
/*  671 */                   if (count > 0) {
/*  672 */                     return Multisets.immutableEntry(element, count);
/*      */                   }
/*      */                 } 
/*  675 */                 return endOfData();
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */         
/*      */         int distinctElements() {
/*  682 */           return Iterators.size(entryIterator());
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
/*      */   @CanIgnoreReturnValue
/*      */   public static boolean containsOccurrences(Multiset<?> superMultiset, Multiset<?> subMultiset) {
/*  695 */     Preconditions.checkNotNull(superMultiset);
/*  696 */     Preconditions.checkNotNull(subMultiset);
/*  697 */     for (Multiset.Entry<?> entry : subMultiset.entrySet()) {
/*  698 */       int superCount = superMultiset.count(entry.getElement());
/*  699 */       if (superCount < entry.getCount()) {
/*  700 */         return false;
/*      */       }
/*      */     } 
/*  703 */     return true;
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
/*      */   public static boolean retainOccurrences(Multiset<?> multisetToModify, Multiset<?> multisetToRetain) {
/*  725 */     return retainOccurrencesImpl(multisetToModify, multisetToRetain);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static <E> boolean retainOccurrencesImpl(Multiset<E> multisetToModify, Multiset<?> occurrencesToRetain) {
/*  731 */     Preconditions.checkNotNull(multisetToModify);
/*  732 */     Preconditions.checkNotNull(occurrencesToRetain);
/*      */     
/*  734 */     Iterator<Multiset.Entry<E>> entryIterator = multisetToModify.entrySet().iterator();
/*  735 */     boolean changed = false;
/*  736 */     while (entryIterator.hasNext()) {
/*  737 */       Multiset.Entry<E> entry = entryIterator.next();
/*  738 */       int retainCount = occurrencesToRetain.count(entry.getElement());
/*  739 */       if (retainCount == 0) {
/*  740 */         entryIterator.remove();
/*  741 */         changed = true; continue;
/*  742 */       }  if (retainCount < entry.getCount()) {
/*  743 */         multisetToModify.setCount(entry.getElement(), retainCount);
/*  744 */         changed = true;
/*      */       } 
/*      */     } 
/*  747 */     return changed;
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
/*      */   @CanIgnoreReturnValue
/*      */   public static boolean removeOccurrences(Multiset<?> multisetToModify, Iterable<?> occurrencesToRemove) {
/*  776 */     if (occurrencesToRemove instanceof Multiset) {
/*  777 */       return removeOccurrences(multisetToModify, (Multiset)occurrencesToRemove);
/*      */     }
/*  779 */     Preconditions.checkNotNull(multisetToModify);
/*  780 */     Preconditions.checkNotNull(occurrencesToRemove);
/*  781 */     boolean changed = false;
/*  782 */     for (Object o : occurrencesToRemove) {
/*  783 */       changed |= multisetToModify.remove(o);
/*      */     }
/*  785 */     return changed;
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
/*      */   @CanIgnoreReturnValue
/*      */   public static boolean removeOccurrences(Multiset<?> multisetToModify, Multiset<?> occurrencesToRemove) {
/*  814 */     Preconditions.checkNotNull(multisetToModify);
/*  815 */     Preconditions.checkNotNull(occurrencesToRemove);
/*      */     
/*  817 */     boolean changed = false;
/*  818 */     Iterator<? extends Multiset.Entry<?>> entryIterator = multisetToModify.entrySet().iterator();
/*  819 */     while (entryIterator.hasNext()) {
/*  820 */       Multiset.Entry<?> entry = entryIterator.next();
/*  821 */       int removeCount = occurrencesToRemove.count(entry.getElement());
/*  822 */       if (removeCount >= entry.getCount()) {
/*  823 */         entryIterator.remove();
/*  824 */         changed = true; continue;
/*  825 */       }  if (removeCount > 0) {
/*  826 */         multisetToModify.remove(entry.getElement(), removeCount);
/*  827 */         changed = true;
/*      */       } 
/*      */     } 
/*  830 */     return changed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static abstract class AbstractEntry<E>
/*      */     implements Multiset.Entry<E>
/*      */   {
/*      */     public boolean equals(@CheckForNull Object object) {
/*  844 */       if (object instanceof Multiset.Entry) {
/*  845 */         Multiset.Entry<?> that = (Multiset.Entry)object;
/*  846 */         return (getCount() == that.getCount() && 
/*  847 */           Objects.equal(getElement(), that.getElement()));
/*      */       } 
/*  849 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  858 */       E e = getElement();
/*  859 */       return ((e == null) ? 0 : e.hashCode()) ^ getCount();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  870 */       String text = String.valueOf(getElement());
/*  871 */       int n = getCount();
/*  872 */       return (n == 1) ? text : (text + " x " + n);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean equalsImpl(Multiset<?> multiset, @CheckForNull Object object) {
/*  878 */     if (object == multiset) {
/*  879 */       return true;
/*      */     }
/*  881 */     if (object instanceof Multiset) {
/*  882 */       Multiset<?> that = (Multiset)object;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  889 */       if (multiset.size() != that.size() || multiset.entrySet().size() != that.entrySet().size()) {
/*  890 */         return false;
/*      */       }
/*  892 */       for (Multiset.Entry<?> entry : that.entrySet()) {
/*  893 */         if (multiset.count(entry.getElement()) != entry.getCount()) {
/*  894 */           return false;
/*      */         }
/*      */       } 
/*  897 */       return true;
/*      */     } 
/*  899 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> boolean addAllImpl(Multiset<E> self, Collection<? extends E> elements) {
/*  905 */     Preconditions.checkNotNull(self);
/*  906 */     Preconditions.checkNotNull(elements);
/*  907 */     if (elements instanceof Multiset)
/*  908 */       return addAllImpl(self, cast(elements)); 
/*  909 */     if (elements.isEmpty()) {
/*  910 */       return false;
/*      */     }
/*  912 */     return Iterators.addAll(self, elements.iterator());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <E> boolean addAllImpl(Multiset<E> self, Multiset<? extends E> elements) {
/*  919 */     if (elements.isEmpty()) {
/*  920 */       return false;
/*      */     }
/*  922 */     Objects.requireNonNull(self); elements.forEachEntry(self::add);
/*  923 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean removeAllImpl(Multiset<?> self, Collection<?> elementsToRemove) {
/*  931 */     Collection<?> collection = (elementsToRemove instanceof Multiset) ? ((Multiset)elementsToRemove).elementSet() : elementsToRemove;
/*      */     
/*  933 */     return self.elementSet().removeAll(collection);
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean retainAllImpl(Multiset<?> self, Collection<?> elementsToRetain) {
/*  938 */     Preconditions.checkNotNull(elementsToRetain);
/*      */ 
/*      */ 
/*      */     
/*  942 */     Collection<?> collection = (elementsToRetain instanceof Multiset) ? ((Multiset)elementsToRetain).elementSet() : elementsToRetain;
/*      */     
/*  944 */     return self.elementSet().retainAll(collection);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> int setCountImpl(Multiset<E> self, @ParametricNullness E element, int count) {
/*  950 */     CollectPreconditions.checkNonnegative(count, "count");
/*      */     
/*  952 */     int oldCount = self.count(element);
/*      */     
/*  954 */     int delta = count - oldCount;
/*  955 */     if (delta > 0) {
/*  956 */       self.add(element, delta);
/*  957 */     } else if (delta < 0) {
/*  958 */       self.remove(element, -delta);
/*      */     } 
/*      */     
/*  961 */     return oldCount;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> boolean setCountImpl(Multiset<E> self, @ParametricNullness E element, int oldCount, int newCount) {
/*  967 */     CollectPreconditions.checkNonnegative(oldCount, "oldCount");
/*  968 */     CollectPreconditions.checkNonnegative(newCount, "newCount");
/*      */     
/*  970 */     if (self.count(element) == oldCount) {
/*  971 */       self.setCount(element, newCount);
/*  972 */       return true;
/*      */     } 
/*  974 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> Iterator<E> elementIterator(Iterator<Multiset.Entry<E>> entryIterator) {
/*  980 */     return new TransformedIterator<Multiset.Entry<E>, E>(entryIterator)
/*      */       {
/*      */         @ParametricNullness
/*      */         E transform(Multiset.Entry<E> entry) {
/*  984 */           return entry.getElement();
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   static abstract class ElementSet<E>
/*      */     extends Sets.ImprovedAbstractSet<E> {
/*      */     abstract Multiset<E> multiset();
/*      */     
/*      */     public void clear() {
/*  994 */       multiset().clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(@CheckForNull Object o) {
/*  999 */       return multiset().contains(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsAll(Collection<?> c) {
/* 1004 */       return multiset().containsAll(c);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 1009 */       return multiset().isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public abstract Iterator<E> iterator();
/*      */ 
/*      */     
/*      */     public boolean remove(@CheckForNull Object o) {
/* 1017 */       return (multiset().remove(o, 2147483647) > 0);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1022 */       return multiset().entrySet().size();
/*      */     }
/*      */   }
/*      */   
/*      */   static abstract class EntrySet<E>
/*      */     extends Sets.ImprovedAbstractSet<Multiset.Entry<E>>
/*      */   {
/*      */     abstract Multiset<E> multiset();
/*      */     
/*      */     public boolean contains(@CheckForNull Object o) {
/* 1032 */       if (o instanceof Multiset.Entry) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1037 */         Multiset.Entry<?> entry = (Multiset.Entry)o;
/* 1038 */         if (entry.getCount() <= 0) {
/* 1039 */           return false;
/*      */         }
/* 1041 */         int count = multiset().count(entry.getElement());
/* 1042 */         return (count == entry.getCount());
/*      */       } 
/* 1044 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean remove(@CheckForNull Object object) {
/* 1051 */       if (object instanceof Multiset.Entry) {
/* 1052 */         Multiset.Entry<?> entry = (Multiset.Entry)object;
/* 1053 */         Object element = entry.getElement();
/* 1054 */         int entryCount = entry.getCount();
/* 1055 */         if (entryCount != 0) {
/*      */ 
/*      */ 
/*      */           
/* 1059 */           Multiset<Object> multiset = (Multiset)multiset();
/* 1060 */           return multiset.setCount(element, entryCount, 0);
/*      */         } 
/*      */       } 
/* 1063 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1068 */       multiset().clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static <E> Iterator<E> iteratorImpl(Multiset<E> multiset) {
/* 1074 */     return new MultisetIteratorImpl<>(multiset, multiset.entrySet().iterator());
/*      */   }
/*      */ 
/*      */   
/*      */   static final class MultisetIteratorImpl<E>
/*      */     implements Iterator<E>
/*      */   {
/*      */     private final Multiset<E> multiset;
/*      */     
/*      */     private final Iterator<Multiset.Entry<E>> entryIterator;
/*      */     @CheckForNull
/*      */     private Multiset.Entry<E> currentEntry;
/*      */     private int laterCount;
/*      */     private int totalCount;
/*      */     private boolean canRemove;
/*      */     
/*      */     MultisetIteratorImpl(Multiset<E> multiset, Iterator<Multiset.Entry<E>> entryIterator) {
/* 1091 */       this.multiset = multiset;
/* 1092 */       this.entryIterator = entryIterator;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/* 1097 */       return (this.laterCount > 0 || this.entryIterator.hasNext());
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public E next() {
/* 1103 */       if (!hasNext()) {
/* 1104 */         throw new NoSuchElementException();
/*      */       }
/* 1106 */       if (this.laterCount == 0) {
/* 1107 */         this.currentEntry = this.entryIterator.next();
/* 1108 */         this.totalCount = this.laterCount = this.currentEntry.getCount();
/*      */       } 
/* 1110 */       this.laterCount--;
/* 1111 */       this.canRemove = true;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1116 */       return ((Multiset.Entry<E>)Objects.<Multiset.Entry<E>>requireNonNull(this.currentEntry)).getElement();
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/* 1121 */       CollectPreconditions.checkRemove(this.canRemove);
/* 1122 */       if (this.totalCount == 1) {
/* 1123 */         this.entryIterator.remove();
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */         
/* 1129 */         this.multiset.remove(((Multiset.Entry)Objects.<Multiset.Entry>requireNonNull(this.currentEntry)).getElement());
/*      */       } 
/* 1131 */       this.totalCount--;
/* 1132 */       this.canRemove = false;
/*      */     }
/*      */   }
/*      */   
/*      */   static <E> Spliterator<E> spliteratorImpl(Multiset<E> multiset) {
/* 1137 */     Spliterator<Multiset.Entry<E>> entrySpliterator = multiset.entrySet().spliterator();
/* 1138 */     return CollectSpliterators.flatMap(entrySpliterator, entry -> Collections.nCopies(entry.getCount(), entry.getElement()).spliterator(), 0x40 | entrySpliterator
/*      */ 
/*      */ 
/*      */         
/* 1142 */         .characteristics() & 0x510, multiset
/*      */         
/* 1144 */         .size());
/*      */   }
/*      */ 
/*      */   
/*      */   static int linearTimeSizeImpl(Multiset<?> multiset) {
/* 1149 */     long size = 0L;
/* 1150 */     for (Multiset.Entry<?> entry : multiset.entrySet()) {
/* 1151 */       size += entry.getCount();
/*      */     }
/* 1153 */     return Ints.saturatedCast(size);
/*      */   }
/*      */ 
/*      */   
/*      */   static <T> Multiset<T> cast(Iterable<T> iterable) {
/* 1158 */     return (Multiset<T>)iterable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> ImmutableMultiset<E> copyHighestCountFirst(Multiset<E> multiset) {
/* 1168 */     Multiset.Entry[] arrayOfEntry = (Multiset.Entry[])multiset.entrySet().toArray((Object[])new Multiset.Entry[0]);
/* 1169 */     Arrays.sort(arrayOfEntry, (Comparator)DecreasingCount.INSTANCE);
/* 1170 */     return ImmutableMultiset.copyFromEntries(Arrays.asList((Multiset.Entry<? extends E>[])arrayOfEntry));
/*      */   }
/*      */   
/*      */   private static final class DecreasingCount implements Comparator<Multiset.Entry<?>> {
/* 1174 */     static final Comparator<Multiset.Entry<?>> INSTANCE = new DecreasingCount();
/*      */ 
/*      */     
/*      */     public int compare(Multiset.Entry<?> entry1, Multiset.Entry<?> entry2) {
/* 1178 */       return entry2.getCount() - entry1.getCount();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static abstract class ViewMultiset<E>
/*      */     extends AbstractMultiset<E>
/*      */   {
/*      */     private ViewMultiset() {}
/*      */ 
/*      */     
/*      */     public int size() {
/* 1190 */       return Multisets.linearTimeSizeImpl(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1195 */       elementSet().clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> iterator() {
/* 1200 */       return Multisets.iteratorImpl(this);
/*      */     }
/*      */ 
/*      */     
/*      */     int distinctElements() {
/* 1205 */       return elementSet().size();
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/Multisets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */