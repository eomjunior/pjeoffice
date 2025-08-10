/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.J2ktIncompatible;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import com.google.errorprone.annotations.DoNotCall;
/*      */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*      */ import java.io.InvalidObjectException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.Iterator;
/*      */ import java.util.NavigableSet;
/*      */ import java.util.SortedSet;
/*      */ import java.util.Spliterator;
/*      */ import java.util.Spliterators;
/*      */ import java.util.function.Consumer;
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
/*      */ @ElementTypesAreNonnullByDefault
/*      */ @GwtCompatible(serializable = true, emulated = true)
/*      */ public abstract class ImmutableSortedSet<E>
/*      */   extends ImmutableSet.CachingAsList<E>
/*      */   implements NavigableSet<E>, SortedIterable<E>
/*      */ {
/*      */   static final int SPLITERATOR_CHARACTERISTICS = 1301;
/*      */   final transient Comparator<? super E> comparator;
/*      */   @LazyInit
/*      */   @CheckForNull
/*      */   @GwtIncompatible
/*      */   transient ImmutableSortedSet<E> descendingSet;
/*      */   private static final long serialVersionUID = -889275714L;
/*      */   
/*      */   public static <E> Collector<E, ?, ImmutableSortedSet<E>> toImmutableSortedSet(Comparator<? super E> comparator) {
/*   83 */     return CollectCollectors.toImmutableSortedSet(comparator);
/*      */   }
/*      */   
/*      */   static <E> RegularImmutableSortedSet<E> emptySet(Comparator<? super E> comparator) {
/*   87 */     if (Ordering.<Comparable>natural().equals(comparator)) {
/*   88 */       return (RegularImmutableSortedSet)RegularImmutableSortedSet.NATURAL_EMPTY_SET;
/*      */     }
/*   90 */     return new RegularImmutableSortedSet<>(ImmutableList.of(), comparator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> ImmutableSortedSet<E> of() {
/*  100 */     return (ImmutableSortedSet)RegularImmutableSortedSet.NATURAL_EMPTY_SET;
/*      */   }
/*      */ 
/*      */   
/*      */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E element) {
/*  105 */     return new RegularImmutableSortedSet<>(ImmutableList.of(element), Ordering.natural());
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
/*      */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2) {
/*  117 */     return construct(Ordering.natural(), 2, (E[])new Comparable[] { (Comparable)e1, (Comparable)e2 });
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
/*      */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3) {
/*  129 */     return construct(Ordering.natural(), 3, (E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3 });
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
/*      */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4) {
/*  141 */     return construct(Ordering.natural(), 4, (E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3, (Comparable)e4 });
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
/*      */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4, E e5) {
/*  154 */     return construct(Ordering.natural(), 5, (E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3, (Comparable)e4, (Comparable)e5 });
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
/*      */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... remaining) {
/*  168 */     Comparable[] contents = new Comparable[6 + remaining.length];
/*  169 */     contents[0] = (Comparable)e1;
/*  170 */     contents[1] = (Comparable)e2;
/*  171 */     contents[2] = (Comparable)e3;
/*  172 */     contents[3] = (Comparable)e4;
/*  173 */     contents[4] = (Comparable)e5;
/*  174 */     contents[5] = (Comparable)e6;
/*  175 */     System.arraycopy(remaining, 0, contents, 6, remaining.length);
/*  176 */     return construct(Ordering.natural(), contents.length, (E[])contents);
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
/*      */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> copyOf(E[] elements) {
/*  190 */     return construct(Ordering.natural(), elements.length, (E[])elements.clone());
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
/*      */   public static <E> ImmutableSortedSet<E> copyOf(Iterable<? extends E> elements) {
/*  218 */     Ordering<E> naturalOrder = Ordering.natural();
/*  219 */     return copyOf(naturalOrder, elements);
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
/*      */   public static <E> ImmutableSortedSet<E> copyOf(Collection<? extends E> elements) {
/*  250 */     Ordering<E> naturalOrder = Ordering.natural();
/*  251 */     return copyOf(naturalOrder, elements);
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
/*      */   public static <E> ImmutableSortedSet<E> copyOf(Iterator<? extends E> elements) {
/*  269 */     Ordering<E> naturalOrder = Ordering.natural();
/*  270 */     return copyOf(naturalOrder, elements);
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
/*      */   public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Iterator<? extends E> elements) {
/*  282 */     return (new Builder<>(comparator)).addAll(elements).build();
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
/*      */   public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Iterable<? extends E> elements) {
/*  298 */     Preconditions.checkNotNull(comparator);
/*  299 */     boolean hasSameComparator = SortedIterables.hasSameComparator(comparator, elements);
/*      */     
/*  301 */     if (hasSameComparator && elements instanceof ImmutableSortedSet) {
/*      */       
/*  303 */       ImmutableSortedSet<E> original = (ImmutableSortedSet)elements;
/*  304 */       if (!original.isPartialView()) {
/*  305 */         return original;
/*      */       }
/*      */     } 
/*      */     
/*  309 */     E[] array = (E[])Iterables.toArray(elements);
/*  310 */     return construct(comparator, array.length, array);
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
/*      */   public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Collection<? extends E> elements) {
/*  330 */     return copyOf(comparator, elements);
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
/*      */   public static <E> ImmutableSortedSet<E> copyOfSorted(SortedSet<E> sortedSet) {
/*  348 */     Comparator<? super E> comparator = SortedIterables.comparator(sortedSet);
/*  349 */     ImmutableList<E> list = ImmutableList.copyOf(sortedSet);
/*  350 */     if (list.isEmpty()) {
/*  351 */       return emptySet(comparator);
/*      */     }
/*  353 */     return new RegularImmutableSortedSet<>(list, comparator);
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
/*      */   static <E> ImmutableSortedSet<E> construct(Comparator<? super E> comparator, int n, E... contents) {
/*  370 */     if (n == 0) {
/*  371 */       return emptySet(comparator);
/*      */     }
/*  373 */     ObjectArrays.checkElementsNotNull((Object[])contents, n);
/*  374 */     Arrays.sort(contents, 0, n, comparator);
/*  375 */     int uniques = 1;
/*  376 */     for (int i = 1; i < n; i++) {
/*  377 */       E cur = contents[i];
/*  378 */       E prev = contents[uniques - 1];
/*  379 */       if (comparator.compare(cur, prev) != 0) {
/*  380 */         contents[uniques++] = cur;
/*      */       }
/*      */     } 
/*  383 */     Arrays.fill((Object[])contents, uniques, n, (Object)null);
/*  384 */     return new RegularImmutableSortedSet<>(
/*  385 */         ImmutableList.asImmutableList((Object[])contents, uniques), comparator);
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
/*      */   public static <E> Builder<E> orderedBy(Comparator<E> comparator) {
/*  397 */     return new Builder<>(comparator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E extends Comparable<?>> Builder<E> reverseOrder() {
/*  405 */     return new Builder<>(Collections.reverseOrder());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E extends Comparable<?>> Builder<E> naturalOrder() {
/*  415 */     return new Builder<>(Ordering.natural());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final class Builder<E>
/*      */     extends ImmutableSet.Builder<E>
/*      */   {
/*      */     private final Comparator<? super E> comparator;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private E[] elements;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int n;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder(Comparator<? super E> comparator) {
/*  445 */       super(true);
/*  446 */       this.comparator = (Comparator<? super E>)Preconditions.checkNotNull(comparator);
/*  447 */       this.elements = (E[])new Object[4];
/*  448 */       this.n = 0;
/*      */     }
/*      */ 
/*      */     
/*      */     void copy() {
/*  453 */       this.elements = Arrays.copyOf(this.elements, this.elements.length);
/*      */     }
/*      */     
/*      */     private void sortAndDedup() {
/*  457 */       if (this.n == 0) {
/*      */         return;
/*      */       }
/*  460 */       Arrays.sort(this.elements, 0, this.n, this.comparator);
/*  461 */       int unique = 1;
/*  462 */       for (int i = 1; i < this.n; i++) {
/*  463 */         int cmp = this.comparator.compare(this.elements[unique - 1], this.elements[i]);
/*  464 */         if (cmp < 0) {
/*  465 */           this.elements[unique++] = this.elements[i];
/*  466 */         } else if (cmp > 0) {
/*  467 */           throw new AssertionError("Comparator " + this.comparator + " compare method violates its contract");
/*      */         } 
/*      */       } 
/*      */       
/*  471 */       Arrays.fill((Object[])this.elements, unique, this.n, (Object)null);
/*  472 */       this.n = unique;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     public Builder<E> add(E element) {
/*  487 */       Preconditions.checkNotNull(element);
/*  488 */       copyIfNecessary();
/*  489 */       if (this.n == this.elements.length) {
/*  490 */         sortAndDedup();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  496 */         int newLength = ImmutableCollection.Builder.expandedCapacity(this.n, this.n + 1);
/*  497 */         if (newLength > this.elements.length) {
/*  498 */           this.elements = Arrays.copyOf(this.elements, newLength);
/*      */         }
/*      */       } 
/*  501 */       this.elements[this.n++] = element;
/*  502 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     public Builder<E> add(E... elements) {
/*  516 */       ObjectArrays.checkElementsNotNull((Object[])elements);
/*  517 */       for (E e : elements) {
/*  518 */         add(e);
/*      */       }
/*  520 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     public Builder<E> addAll(Iterable<? extends E> elements) {
/*  534 */       super.addAll(elements);
/*  535 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     public Builder<E> addAll(Iterator<? extends E> elements) {
/*  549 */       super.addAll(elements);
/*  550 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     Builder<E> combine(ImmutableSet.Builder<E> builder) {
/*  556 */       copyIfNecessary();
/*  557 */       Builder<E> other = (Builder<E>)builder;
/*  558 */       for (int i = 0; i < other.n; i++) {
/*  559 */         add(other.elements[i]);
/*      */       }
/*  561 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ImmutableSortedSet<E> build() {
/*  570 */       sortAndDedup();
/*  571 */       if (this.n == 0) {
/*  572 */         return ImmutableSortedSet.emptySet(this.comparator);
/*      */       }
/*  574 */       this.forceCopy = true;
/*  575 */       return new RegularImmutableSortedSet<>(
/*  576 */           ImmutableList.asImmutableList((Object[])this.elements, this.n), this.comparator);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   int unsafeCompare(Object a, @CheckForNull Object b) {
/*  582 */     return unsafeCompare(this.comparator, a, b);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int unsafeCompare(Comparator<?> comparator, Object a, @CheckForNull Object b) {
/*  590 */     Comparator<Object> unsafeComparator = (Comparator)comparator;
/*  591 */     return unsafeComparator.compare(a, b);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   ImmutableSortedSet(Comparator<? super E> comparator) {
/*  597 */     this.comparator = comparator;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Comparator<? super E> comparator() {
/*  607 */     return this.comparator;
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
/*      */   public ImmutableSortedSet<E> headSet(E toElement) {
/*  625 */     return headSet(toElement, false);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ImmutableSortedSet<E> headSet(E toElement, boolean inclusive) {
/*  631 */     return headSetImpl((E)Preconditions.checkNotNull(toElement), inclusive);
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
/*      */   public ImmutableSortedSet<E> subSet(E fromElement, E toElement) {
/*  648 */     return subSet(fromElement, true, toElement, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public ImmutableSortedSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/*  656 */     Preconditions.checkNotNull(fromElement);
/*  657 */     Preconditions.checkNotNull(toElement);
/*  658 */     Preconditions.checkArgument((this.comparator.compare(fromElement, toElement) <= 0));
/*  659 */     return subSetImpl(fromElement, fromInclusive, toElement, toInclusive);
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
/*      */   public ImmutableSortedSet<E> tailSet(E fromElement) {
/*  674 */     return tailSet(fromElement, true);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ImmutableSortedSet<E> tailSet(E fromElement, boolean inclusive) {
/*  680 */     return tailSetImpl((E)Preconditions.checkNotNull(fromElement), inclusive);
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
/*      */   @CheckForNull
/*      */   @GwtIncompatible
/*      */   public E lower(E e) {
/*  699 */     return Iterators.getNext(headSet(e, false).descendingIterator(), null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   public E floor(E e) {
/*  706 */     return Iterators.getNext(headSet(e, true).descendingIterator(), null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   public E ceiling(E e) {
/*  713 */     return Iterables.getFirst(tailSet(e, true), null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   @GwtIncompatible
/*      */   public E higher(E e) {
/*  721 */     return Iterables.getFirst(tailSet(e, false), null);
/*      */   }
/*      */ 
/*      */   
/*      */   public E first() {
/*  726 */     return iterator().next();
/*      */   }
/*      */ 
/*      */   
/*      */   public E last() {
/*  731 */     return descendingIterator().next();
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
/*      */   @Deprecated
/*      */   @CheckForNull
/*      */   @CanIgnoreReturnValue
/*      */   @GwtIncompatible
/*      */   @DoNotCall("Always throws UnsupportedOperationException")
/*      */   public final E pollFirst() {
/*  748 */     throw new UnsupportedOperationException();
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
/*      */   @Deprecated
/*      */   @CheckForNull
/*      */   @CanIgnoreReturnValue
/*      */   @GwtIncompatible
/*      */   @DoNotCall("Always throws UnsupportedOperationException")
/*      */   public final E pollLast() {
/*  765 */     throw new UnsupportedOperationException();
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
/*      */   public ImmutableSortedSet<E> descendingSet() {
/*  778 */     ImmutableSortedSet<E> result = this.descendingSet;
/*  779 */     if (result == null) {
/*  780 */       result = this.descendingSet = createDescendingSet();
/*  781 */       result.descendingSet = this;
/*      */     } 
/*  783 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Spliterator<E> spliterator() {
/*  794 */     return new Spliterators.AbstractSpliterator<E>(
/*  795 */         size(), 1365) {
/*  796 */         final UnmodifiableIterator<E> iterator = ImmutableSortedSet.this.iterator();
/*      */ 
/*      */         
/*      */         public boolean tryAdvance(Consumer<? super E> action) {
/*  800 */           if (this.iterator.hasNext()) {
/*  801 */             action.accept(this.iterator.next());
/*  802 */             return true;
/*      */           } 
/*  804 */           return false;
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*      */         public Comparator<? super E> getComparator() {
/*  810 */           return ImmutableSortedSet.this.comparator;
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @J2ktIncompatible
/*      */   private static class SerializedForm<E>
/*      */     implements Serializable
/*      */   {
/*      */     final Comparator<? super E> comparator;
/*      */ 
/*      */ 
/*      */     
/*      */     final Object[] elements;
/*      */ 
/*      */ 
/*      */     
/*      */     private static final long serialVersionUID = 0L;
/*      */ 
/*      */ 
/*      */     
/*      */     public SerializedForm(Comparator<? super E> comparator, Object[] elements) {
/*  835 */       this.comparator = comparator;
/*  836 */       this.elements = elements;
/*      */     }
/*      */ 
/*      */     
/*      */     Object readResolve() {
/*  841 */       return (new ImmutableSortedSet.Builder((Comparator)this.comparator)).add(this.elements).build();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @J2ktIncompatible
/*      */   private void readObject(ObjectInputStream unused) throws InvalidObjectException {
/*  849 */     throw new InvalidObjectException("Use SerializedForm");
/*      */   }
/*      */ 
/*      */   
/*      */   @J2ktIncompatible
/*      */   Object writeReplace() {
/*  855 */     return new SerializedForm<>(this.comparator, toArray());
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
/*      */   @Deprecated
/*      */   @DoNotCall("Use toImmutableSortedSet")
/*      */   public static <E> Collector<E, ?, ImmutableSet<E>> toImmutableSet() {
/*  869 */     throw new UnsupportedOperationException();
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
/*      */   @DoNotCall("Use naturalOrder")
/*      */   public static <E> Builder<E> builder() {
/*  882 */     throw new UnsupportedOperationException();
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
/*      */   @DoNotCall("Use naturalOrder (which does not accept an expected size)")
/*      */   public static <E> Builder<E> builderWithExpectedSize(int expectedSize) {
/*  895 */     throw new UnsupportedOperationException();
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
/*      */   @Deprecated
/*      */   @DoNotCall("Pass a parameter of type Comparable")
/*      */   public static <E> ImmutableSortedSet<E> of(E element) {
/*  910 */     throw new UnsupportedOperationException();
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
/*      */   @Deprecated
/*      */   @DoNotCall("Pass parameters of type Comparable")
/*      */   public static <E> ImmutableSortedSet<E> of(E e1, E e2) {
/*  925 */     throw new UnsupportedOperationException();
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
/*      */   @Deprecated
/*      */   @DoNotCall("Pass parameters of type Comparable")
/*      */   public static <E> ImmutableSortedSet<E> of(E e1, E e2, E e3) {
/*  940 */     throw new UnsupportedOperationException();
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
/*      */   @Deprecated
/*      */   @DoNotCall("Pass parameters of type Comparable")
/*      */   public static <E> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4) {
/*  955 */     throw new UnsupportedOperationException();
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
/*      */   @Deprecated
/*      */   @DoNotCall("Pass parameters of type Comparable")
/*      */   public static <E> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4, E e5) {
/*  970 */     throw new UnsupportedOperationException();
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
/*      */   @Deprecated
/*      */   @DoNotCall("Pass parameters of type Comparable")
/*      */   public static <E> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... remaining) {
/*  986 */     throw new UnsupportedOperationException();
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
/*      */   @Deprecated
/*      */   @DoNotCall("Pass parameters of type Comparable")
/*      */   public static <Z> ImmutableSortedSet<Z> copyOf(Z[] elements) {
/* 1002 */     throw new UnsupportedOperationException();
/*      */   }
/*      */   
/*      */   public abstract UnmodifiableIterator<E> iterator();
/*      */   
/*      */   abstract ImmutableSortedSet<E> headSetImpl(E paramE, boolean paramBoolean);
/*      */   
/*      */   abstract ImmutableSortedSet<E> subSetImpl(E paramE1, boolean paramBoolean1, E paramE2, boolean paramBoolean2);
/*      */   
/*      */   abstract ImmutableSortedSet<E> tailSetImpl(E paramE, boolean paramBoolean);
/*      */   
/*      */   @GwtIncompatible
/*      */   abstract ImmutableSortedSet<E> createDescendingSet();
/*      */   
/*      */   @GwtIncompatible
/*      */   public abstract UnmodifiableIterator<E> descendingIterator();
/*      */   
/*      */   abstract int indexOf(@CheckForNull Object paramObject);
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ImmutableSortedSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */