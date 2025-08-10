/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.J2ktIncompatible;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.math.IntMath;
/*      */ import com.google.common.primitives.Ints;
/*      */ import java.io.Serializable;
/*      */ import java.math.RoundingMode;
/*      */ import java.util.AbstractList;
/*      */ import java.util.AbstractSequentialList;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.RandomAccess;
/*      */ import java.util.concurrent.CopyOnWriteArrayList;
/*      */ import java.util.function.Predicate;
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
/*      */ @ElementTypesAreNonnullByDefault
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class Lists
/*      */ {
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> ArrayList<E> newArrayList() {
/*   85 */     return new ArrayList<>();
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
/*      */   @SafeVarargs
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> ArrayList<E> newArrayList(E... elements) {
/*  105 */     Preconditions.checkNotNull(elements);
/*      */     
/*  107 */     int capacity = computeArrayListCapacity(elements.length);
/*  108 */     ArrayList<E> list = new ArrayList<>(capacity);
/*  109 */     Collections.addAll(list, elements);
/*  110 */     return list;
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
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> ArrayList<E> newArrayList(Iterable<? extends E> elements) {
/*  128 */     Preconditions.checkNotNull(elements);
/*      */     
/*  130 */     return (elements instanceof Collection) ? 
/*  131 */       new ArrayList<>((Collection<? extends E>)elements) : 
/*  132 */       newArrayList(elements.iterator());
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
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> ArrayList<E> newArrayList(Iterator<? extends E> elements) {
/*  145 */     ArrayList<E> list = newArrayList();
/*  146 */     Iterators.addAll(list, elements);
/*  147 */     return list;
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   static int computeArrayListCapacity(int arraySize) {
/*  152 */     CollectPreconditions.checkNonnegative(arraySize, "arraySize");
/*      */ 
/*      */     
/*  155 */     return Ints.saturatedCast(5L + arraySize + (arraySize / 10));
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
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> ArrayList<E> newArrayListWithCapacity(int initialArraySize) {
/*  177 */     CollectPreconditions.checkNonnegative(initialArraySize, "initialArraySize");
/*  178 */     return new ArrayList<>(initialArraySize);
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
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> ArrayList<E> newArrayListWithExpectedSize(int estimatedSize) {
/*  197 */     return new ArrayList<>(computeArrayListCapacity(estimatedSize));
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
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> LinkedList<E> newLinkedList() {
/*  218 */     return new LinkedList<>();
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
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> LinkedList<E> newLinkedList(Iterable<? extends E> elements) {
/*  240 */     LinkedList<E> list = newLinkedList();
/*  241 */     Iterables.addAll(list, elements);
/*  242 */     return list;
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
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList() {
/*  257 */     return new CopyOnWriteArrayList<>();
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
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList(Iterable<? extends E> elements) {
/*  276 */     Collection<? extends E> elementsCollection = (elements instanceof Collection) ? (Collection<? extends E>)elements : newArrayList(elements);
/*  277 */     return new CopyOnWriteArrayList<>(elementsCollection);
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
/*      */   public static <E> List<E> asList(@ParametricNullness E first, E[] rest) {
/*  295 */     return new OnePlusArrayList<>(first, rest);
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
/*      */   public static <E> List<E> asList(@ParametricNullness E first, @ParametricNullness E second, E[] rest) {
/*  316 */     return new TwoPlusArrayList<>(first, second, rest);
/*      */   }
/*      */   
/*      */   private static class OnePlusArrayList<E>
/*      */     extends AbstractList<E>
/*      */     implements Serializable, RandomAccess {
/*      */     @ParametricNullness
/*      */     final E first;
/*      */     
/*      */     OnePlusArrayList(@ParametricNullness E first, E[] rest) {
/*  326 */       this.first = first;
/*  327 */       this.rest = (E[])Preconditions.checkNotNull(rest);
/*      */     }
/*      */     final E[] rest; @J2ktIncompatible
/*      */     private static final long serialVersionUID = 0L;
/*      */     public int size() {
/*  332 */       return IntMath.saturatedAdd(this.rest.length, 1);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public E get(int index) {
/*  339 */       Preconditions.checkElementIndex(index, size());
/*  340 */       return (index == 0) ? this.first : this.rest[index - 1];
/*      */     }
/*      */   }
/*      */   
/*      */   private static class TwoPlusArrayList<E> extends AbstractList<E> implements Serializable, RandomAccess {
/*      */     @ParametricNullness
/*      */     final E first;
/*      */     @ParametricNullness
/*      */     final E second;
/*      */     final E[] rest;
/*      */     @J2ktIncompatible
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     TwoPlusArrayList(@ParametricNullness E first, @ParametricNullness E second, E[] rest) {
/*  354 */       this.first = first;
/*  355 */       this.second = second;
/*  356 */       this.rest = (E[])Preconditions.checkNotNull(rest);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  361 */       return IntMath.saturatedAdd(this.rest.length, 2);
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public E get(int index) {
/*  367 */       switch (index) {
/*      */         case 0:
/*  369 */           return this.first;
/*      */         case 1:
/*  371 */           return this.second;
/*      */       } 
/*      */       
/*  374 */       Preconditions.checkElementIndex(index, size());
/*  375 */       return this.rest[index - 2];
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <B> List<List<B>> cartesianProduct(List<? extends List<? extends B>> lists) {
/*  437 */     return CartesianList.create(lists);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <B> List<List<B>> cartesianProduct(List<? extends B>... lists) {
/*  496 */     return cartesianProduct(Arrays.asList(lists));
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
/*      */   public static <F, T> List<T> transform(List<F> fromList, Function<? super F, ? extends T> function) {
/*  533 */     return (fromList instanceof RandomAccess) ? 
/*  534 */       new TransformingRandomAccessList<>(fromList, function) : 
/*  535 */       new TransformingSequentialList<>(fromList, function);
/*      */   }
/*      */ 
/*      */   
/*      */   private static class TransformingSequentialList<F, T>
/*      */     extends AbstractSequentialList<T>
/*      */     implements Serializable
/*      */   {
/*      */     final List<F> fromList;
/*      */     
/*      */     final Function<? super F, ? extends T> function;
/*      */     
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     TransformingSequentialList(List<F> fromList, Function<? super F, ? extends T> function) {
/*  550 */       this.fromList = (List<F>)Preconditions.checkNotNull(fromList);
/*  551 */       this.function = (Function<? super F, ? extends T>)Preconditions.checkNotNull(function);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected void removeRange(int fromIndex, int toIndex) {
/*  560 */       this.fromList.subList(fromIndex, toIndex).clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  565 */       return this.fromList.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public ListIterator<T> listIterator(int index) {
/*  570 */       return new TransformedListIterator<F, T>(this.fromList.listIterator(index))
/*      */         {
/*      */           @ParametricNullness
/*      */           T transform(@ParametricNullness F from) {
/*  574 */             return (T)Lists.TransformingSequentialList.this.function.apply(from);
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeIf(Predicate<? super T> filter) {
/*  581 */       Preconditions.checkNotNull(filter);
/*  582 */       return this.fromList.removeIf(element -> filter.test(this.function.apply(element)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TransformingRandomAccessList<F, T>
/*      */     extends AbstractList<T>
/*      */     implements RandomAccess, Serializable
/*      */   {
/*      */     final List<F> fromList;
/*      */ 
/*      */     
/*      */     final Function<? super F, ? extends T> function;
/*      */ 
/*      */     
/*      */     private static final long serialVersionUID = 0L;
/*      */ 
/*      */     
/*      */     TransformingRandomAccessList(List<F> fromList, Function<? super F, ? extends T> function) {
/*  602 */       this.fromList = (List<F>)Preconditions.checkNotNull(fromList);
/*  603 */       this.function = (Function<? super F, ? extends T>)Preconditions.checkNotNull(function);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected void removeRange(int fromIndex, int toIndex) {
/*  612 */       this.fromList.subList(fromIndex, toIndex).clear();
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public T get(int index) {
/*  618 */       return (T)this.function.apply(this.fromList.get(index));
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<T> iterator() {
/*  623 */       return listIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public ListIterator<T> listIterator(int index) {
/*  628 */       return new TransformedListIterator<F, T>(this.fromList.listIterator(index))
/*      */         {
/*      */           T transform(F from) {
/*  631 */             return (T)Lists.TransformingRandomAccessList.this.function.apply(from);
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/*  640 */       return this.fromList.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeIf(Predicate<? super T> filter) {
/*  645 */       Preconditions.checkNotNull(filter);
/*  646 */       return this.fromList.removeIf(element -> filter.test(this.function.apply(element)));
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public T remove(int index) {
/*  652 */       return (T)this.function.apply(this.fromList.remove(index));
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  657 */       return this.fromList.size();
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
/*      */   public static <T> List<List<T>> partition(List<T> list, int size) {
/*  679 */     Preconditions.checkNotNull(list);
/*  680 */     Preconditions.checkArgument((size > 0));
/*  681 */     return (list instanceof RandomAccess) ? 
/*  682 */       new RandomAccessPartition<>(list, size) : 
/*  683 */       new Partition<>(list, size);
/*      */   }
/*      */   
/*      */   private static class Partition<T> extends AbstractList<List<T>> {
/*      */     final List<T> list;
/*      */     final int size;
/*      */     
/*      */     Partition(List<T> list, int size) {
/*  691 */       this.list = list;
/*  692 */       this.size = size;
/*      */     }
/*      */ 
/*      */     
/*      */     public List<T> get(int index) {
/*  697 */       Preconditions.checkElementIndex(index, size());
/*  698 */       int start = index * this.size;
/*  699 */       int end = Math.min(start + this.size, this.list.size());
/*  700 */       return this.list.subList(start, end);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  705 */       return IntMath.divide(this.list.size(), this.size, RoundingMode.CEILING);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/*  710 */       return this.list.isEmpty();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class RandomAccessPartition<T>
/*      */     extends Partition<T> implements RandomAccess {
/*      */     RandomAccessPartition(List<T> list, int size) {
/*  717 */       super(list, size);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ImmutableList<Character> charactersOf(String string) {
/*  727 */     return new StringAsImmutableList((String)Preconditions.checkNotNull(string));
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
/*      */   public static List<Character> charactersOf(CharSequence sequence) {
/*  740 */     return new CharSequenceAsList((CharSequence)Preconditions.checkNotNull(sequence));
/*      */   }
/*      */   
/*      */   private static final class StringAsImmutableList
/*      */     extends ImmutableList<Character>
/*      */   {
/*      */     private final String string;
/*      */     
/*      */     StringAsImmutableList(String string) {
/*  749 */       this.string = string;
/*      */     }
/*      */ 
/*      */     
/*      */     public int indexOf(@CheckForNull Object object) {
/*  754 */       return (object instanceof Character) ? this.string.indexOf(((Character)object).charValue()) : -1;
/*      */     }
/*      */ 
/*      */     
/*      */     public int lastIndexOf(@CheckForNull Object object) {
/*  759 */       return (object instanceof Character) ? this.string.lastIndexOf(((Character)object).charValue()) : -1;
/*      */     }
/*      */ 
/*      */     
/*      */     public ImmutableList<Character> subList(int fromIndex, int toIndex) {
/*  764 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
/*  765 */       return Lists.charactersOf(this.string.substring(fromIndex, toIndex));
/*      */     }
/*      */ 
/*      */     
/*      */     boolean isPartialView() {
/*  770 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public Character get(int index) {
/*  775 */       Preconditions.checkElementIndex(index, size());
/*  776 */       return Character.valueOf(this.string.charAt(index));
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  781 */       return this.string.length();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @J2ktIncompatible
/*      */     @GwtIncompatible
/*      */     Object writeReplace() {
/*  790 */       return super.writeReplace();
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class CharSequenceAsList extends AbstractList<Character> {
/*      */     private final CharSequence sequence;
/*      */     
/*      */     CharSequenceAsList(CharSequence sequence) {
/*  798 */       this.sequence = sequence;
/*      */     }
/*      */ 
/*      */     
/*      */     public Character get(int index) {
/*  803 */       Preconditions.checkElementIndex(index, size());
/*  804 */       return Character.valueOf(this.sequence.charAt(index));
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  809 */       return this.sequence.length();
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
/*      */   public static <T> List<T> reverse(List<T> list) {
/*  825 */     if (list instanceof ImmutableList) {
/*      */       
/*  827 */       List<?> reversed = ((ImmutableList)list).reverse();
/*      */       
/*  829 */       List<T> result = (List)reversed;
/*  830 */       return result;
/*  831 */     }  if (list instanceof ReverseList)
/*  832 */       return ((ReverseList<T>)list).getForwardList(); 
/*  833 */     if (list instanceof RandomAccess) {
/*  834 */       return new RandomAccessReverseList<>(list);
/*      */     }
/*  836 */     return new ReverseList<>(list);
/*      */   }
/*      */   
/*      */   private static class ReverseList<T>
/*      */     extends AbstractList<T> {
/*      */     private final List<T> forwardList;
/*      */     
/*      */     ReverseList(List<T> forwardList) {
/*  844 */       this.forwardList = (List<T>)Preconditions.checkNotNull(forwardList);
/*      */     }
/*      */     
/*      */     List<T> getForwardList() {
/*  848 */       return this.forwardList;
/*      */     }
/*      */     
/*      */     private int reverseIndex(int index) {
/*  852 */       int size = size();
/*  853 */       Preconditions.checkElementIndex(index, size);
/*  854 */       return size - 1 - index;
/*      */     }
/*      */     
/*      */     private int reversePosition(int index) {
/*  858 */       int size = size();
/*  859 */       Preconditions.checkPositionIndex(index, size);
/*  860 */       return size - index;
/*      */     }
/*      */ 
/*      */     
/*      */     public void add(int index, @ParametricNullness T element) {
/*  865 */       this.forwardList.add(reversePosition(index), element);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  870 */       this.forwardList.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public T remove(int index) {
/*  876 */       return this.forwardList.remove(reverseIndex(index));
/*      */     }
/*      */ 
/*      */     
/*      */     protected void removeRange(int fromIndex, int toIndex) {
/*  881 */       subList(fromIndex, toIndex).clear();
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public T set(int index, @ParametricNullness T element) {
/*  887 */       return this.forwardList.set(reverseIndex(index), element);
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public T get(int index) {
/*  893 */       return this.forwardList.get(reverseIndex(index));
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  898 */       return this.forwardList.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public List<T> subList(int fromIndex, int toIndex) {
/*  903 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
/*  904 */       return Lists.reverse(this.forwardList.subList(reversePosition(toIndex), reversePosition(fromIndex)));
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<T> iterator() {
/*  909 */       return listIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public ListIterator<T> listIterator(int index) {
/*  914 */       int start = reversePosition(index);
/*  915 */       final ListIterator<T> forwardIterator = this.forwardList.listIterator(start);
/*  916 */       return new ListIterator<T>()
/*      */         {
/*      */           boolean canRemoveOrSet;
/*      */ 
/*      */           
/*      */           public void add(@ParametricNullness T e) {
/*  922 */             forwardIterator.add(e);
/*  923 */             forwardIterator.previous();
/*  924 */             this.canRemoveOrSet = false;
/*      */           }
/*      */ 
/*      */           
/*      */           public boolean hasNext() {
/*  929 */             return forwardIterator.hasPrevious();
/*      */           }
/*      */ 
/*      */           
/*      */           public boolean hasPrevious() {
/*  934 */             return forwardIterator.hasNext();
/*      */           }
/*      */ 
/*      */           
/*      */           @ParametricNullness
/*      */           public T next() {
/*  940 */             if (!hasNext()) {
/*  941 */               throw new NoSuchElementException();
/*      */             }
/*  943 */             this.canRemoveOrSet = true;
/*  944 */             return forwardIterator.previous();
/*      */           }
/*      */ 
/*      */           
/*      */           public int nextIndex() {
/*  949 */             return Lists.ReverseList.this.reversePosition(forwardIterator.nextIndex());
/*      */           }
/*      */ 
/*      */           
/*      */           @ParametricNullness
/*      */           public T previous() {
/*  955 */             if (!hasPrevious()) {
/*  956 */               throw new NoSuchElementException();
/*      */             }
/*  958 */             this.canRemoveOrSet = true;
/*  959 */             return forwardIterator.next();
/*      */           }
/*      */ 
/*      */           
/*      */           public int previousIndex() {
/*  964 */             return nextIndex() - 1;
/*      */           }
/*      */ 
/*      */           
/*      */           public void remove() {
/*  969 */             CollectPreconditions.checkRemove(this.canRemoveOrSet);
/*  970 */             forwardIterator.remove();
/*  971 */             this.canRemoveOrSet = false;
/*      */           }
/*      */ 
/*      */           
/*      */           public void set(@ParametricNullness T e) {
/*  976 */             Preconditions.checkState(this.canRemoveOrSet);
/*  977 */             forwardIterator.set(e);
/*      */           }
/*      */         };
/*      */     }
/*      */   }
/*      */   
/*      */   private static class RandomAccessReverseList<T>
/*      */     extends ReverseList<T> implements RandomAccess {
/*      */     RandomAccessReverseList(List<T> forwardList) {
/*  986 */       super(forwardList);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static int hashCodeImpl(List<?> list) {
/*  993 */     int hashCode = 1;
/*  994 */     for (Object o : list) {
/*  995 */       hashCode = 31 * hashCode + ((o == null) ? 0 : o.hashCode());
/*      */       
/*  997 */       hashCode = hashCode ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
/*      */     } 
/*      */     
/* 1000 */     return hashCode;
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean equalsImpl(List<?> thisList, @CheckForNull Object other) {
/* 1005 */     if (other == Preconditions.checkNotNull(thisList)) {
/* 1006 */       return true;
/*      */     }
/* 1008 */     if (!(other instanceof List)) {
/* 1009 */       return false;
/*      */     }
/* 1011 */     List<?> otherList = (List)other;
/* 1012 */     int size = thisList.size();
/* 1013 */     if (size != otherList.size()) {
/* 1014 */       return false;
/*      */     }
/* 1016 */     if (thisList instanceof RandomAccess && otherList instanceof RandomAccess) {
/*      */       
/* 1018 */       for (int i = 0; i < size; i++) {
/* 1019 */         if (!Objects.equal(thisList.get(i), otherList.get(i))) {
/* 1020 */           return false;
/*      */         }
/*      */       } 
/* 1023 */       return true;
/*      */     } 
/* 1025 */     return Iterators.elementsEqual(thisList.iterator(), otherList.iterator());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> boolean addAllImpl(List<E> list, int index, Iterable<? extends E> elements) {
/* 1032 */     boolean changed = false;
/* 1033 */     ListIterator<E> listIterator = list.listIterator(index);
/* 1034 */     for (E e : elements) {
/* 1035 */       listIterator.add(e);
/* 1036 */       changed = true;
/*      */     } 
/* 1038 */     return changed;
/*      */   }
/*      */ 
/*      */   
/*      */   static int indexOfImpl(List<?> list, @CheckForNull Object element) {
/* 1043 */     if (list instanceof RandomAccess) {
/* 1044 */       return indexOfRandomAccess(list, element);
/*      */     }
/* 1046 */     ListIterator<?> listIterator = list.listIterator();
/* 1047 */     while (listIterator.hasNext()) {
/* 1048 */       if (Objects.equal(element, listIterator.next())) {
/* 1049 */         return listIterator.previousIndex();
/*      */       }
/*      */     } 
/* 1052 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int indexOfRandomAccess(List<?> list, @CheckForNull Object element) {
/* 1057 */     int size = list.size();
/* 1058 */     if (element == null) {
/* 1059 */       for (int i = 0; i < size; i++) {
/* 1060 */         if (list.get(i) == null) {
/* 1061 */           return i;
/*      */         }
/*      */       } 
/*      */     } else {
/* 1065 */       for (int i = 0; i < size; i++) {
/* 1066 */         if (element.equals(list.get(i))) {
/* 1067 */           return i;
/*      */         }
/*      */       } 
/*      */     } 
/* 1071 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   static int lastIndexOfImpl(List<?> list, @CheckForNull Object element) {
/* 1076 */     if (list instanceof RandomAccess) {
/* 1077 */       return lastIndexOfRandomAccess(list, element);
/*      */     }
/* 1079 */     ListIterator<?> listIterator = list.listIterator(list.size());
/* 1080 */     while (listIterator.hasPrevious()) {
/* 1081 */       if (Objects.equal(element, listIterator.previous())) {
/* 1082 */         return listIterator.nextIndex();
/*      */       }
/*      */     } 
/* 1085 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int lastIndexOfRandomAccess(List<?> list, @CheckForNull Object element) {
/* 1090 */     if (element == null) {
/* 1091 */       for (int i = list.size() - 1; i >= 0; i--) {
/* 1092 */         if (list.get(i) == null) {
/* 1093 */           return i;
/*      */         }
/*      */       } 
/*      */     } else {
/* 1097 */       for (int i = list.size() - 1; i >= 0; i--) {
/* 1098 */         if (element.equals(list.get(i))) {
/* 1099 */           return i;
/*      */         }
/*      */       } 
/*      */     } 
/* 1103 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   static <E> ListIterator<E> listIteratorImpl(List<E> list, int index) {
/* 1108 */     return (new AbstractListWrapper<>(list)).listIterator(index);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> List<E> subListImpl(List<E> list, int fromIndex, int toIndex) {
/*      */     List<E> wrapper;
/* 1115 */     if (list instanceof RandomAccess) {
/* 1116 */       wrapper = new RandomAccessListWrapper<E>(list) { @J2ktIncompatible
/*      */           private static final long serialVersionUID = 0L;
/*      */           
/*      */           public ListIterator<E> listIterator(int index) {
/* 1120 */             return this.backingList.listIterator(index);
/*      */           } }
/*      */         ;
/*      */     }
/*      */     else {
/*      */       
/* 1126 */       wrapper = new AbstractListWrapper<E>(list) { @J2ktIncompatible
/*      */           private static final long serialVersionUID = 0L;
/*      */           
/*      */           public ListIterator<E> listIterator(int index) {
/* 1130 */             return this.backingList.listIterator(index);
/*      */           } }
/*      */         ;
/*      */     } 
/*      */ 
/*      */     
/* 1136 */     return wrapper.subList(fromIndex, toIndex);
/*      */   }
/*      */   
/*      */   private static class AbstractListWrapper<E> extends AbstractList<E> {
/*      */     final List<E> backingList;
/*      */     
/*      */     AbstractListWrapper(List<E> backingList) {
/* 1143 */       this.backingList = (List<E>)Preconditions.checkNotNull(backingList);
/*      */     }
/*      */ 
/*      */     
/*      */     public void add(int index, @ParametricNullness E element) {
/* 1148 */       this.backingList.add(index, element);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean addAll(int index, Collection<? extends E> c) {
/* 1153 */       return this.backingList.addAll(index, c);
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public E get(int index) {
/* 1159 */       return this.backingList.get(index);
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public E remove(int index) {
/* 1165 */       return this.backingList.remove(index);
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public E set(int index, @ParametricNullness E element) {
/* 1171 */       return this.backingList.set(index, element);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(@CheckForNull Object o) {
/* 1176 */       return this.backingList.contains(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1181 */       return this.backingList.size();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class RandomAccessListWrapper<E>
/*      */     extends AbstractListWrapper<E> implements RandomAccess {
/*      */     RandomAccessListWrapper(List<E> backingList) {
/* 1188 */       super(backingList);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static <T> List<T> cast(Iterable<T> iterable) {
/* 1194 */     return (List<T>)iterable;
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/Lists.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */