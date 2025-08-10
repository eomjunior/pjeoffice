/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import com.google.common.math.IntMath;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ public final class Collections2
/*     */ {
/*     */   public static <E> Collection<E> filter(Collection<E> unfiltered, Predicate<? super E> predicate) {
/*  90 */     if (unfiltered instanceof FilteredCollection)
/*     */     {
/*     */       
/*  93 */       return ((FilteredCollection<E>)unfiltered).createCombined(predicate);
/*     */     }
/*     */     
/*  96 */     return new FilteredCollection<>((Collection<E>)Preconditions.checkNotNull(unfiltered), (Predicate<? super E>)Preconditions.checkNotNull(predicate));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean safeContains(Collection<?> collection, @CheckForNull Object object) {
/* 104 */     Preconditions.checkNotNull(collection);
/*     */     try {
/* 106 */       return collection.contains(object);
/* 107 */     } catch (ClassCastException|NullPointerException e) {
/* 108 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean safeRemove(Collection<?> collection, @CheckForNull Object object) {
/* 117 */     Preconditions.checkNotNull(collection);
/*     */     try {
/* 119 */       return collection.remove(object);
/* 120 */     } catch (ClassCastException|NullPointerException e) {
/* 121 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   static class FilteredCollection<E> extends AbstractCollection<E> {
/*     */     final Collection<E> unfiltered;
/*     */     final Predicate<? super E> predicate;
/*     */     
/*     */     FilteredCollection(Collection<E> unfiltered, Predicate<? super E> predicate) {
/* 130 */       this.unfiltered = unfiltered;
/* 131 */       this.predicate = predicate;
/*     */     }
/*     */     
/*     */     FilteredCollection<E> createCombined(Predicate<? super E> newPredicate) {
/* 135 */       return new FilteredCollection(this.unfiltered, Predicates.and(this.predicate, newPredicate));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean add(@ParametricNullness E element) {
/* 141 */       Preconditions.checkArgument(this.predicate.apply(element));
/* 142 */       return this.unfiltered.add(element);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean addAll(Collection<? extends E> collection) {
/* 147 */       for (E element : collection) {
/* 148 */         Preconditions.checkArgument(this.predicate.apply(element));
/*     */       }
/* 150 */       return this.unfiltered.addAll(collection);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 155 */       Iterables.removeIf(this.unfiltered, this.predicate);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(@CheckForNull Object element) {
/* 160 */       if (Collections2.safeContains(this.unfiltered, element)) {
/*     */         
/* 162 */         E e = (E)element;
/* 163 */         return this.predicate.apply(e);
/*     */       } 
/* 165 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsAll(Collection<?> collection) {
/* 170 */       return Collections2.containsAllImpl(this, collection);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 175 */       return !Iterables.any(this.unfiltered, this.predicate);
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<E> iterator() {
/* 180 */       return Iterators.filter(this.unfiltered.iterator(), this.predicate);
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator<E> spliterator() {
/* 185 */       return CollectSpliterators.filter(this.unfiltered.spliterator(), (Predicate<? super E>)this.predicate);
/*     */     }
/*     */ 
/*     */     
/*     */     public void forEach(Consumer<? super E> action) {
/* 190 */       Preconditions.checkNotNull(action);
/* 191 */       this.unfiltered.forEach(e -> {
/*     */             if (this.predicate.test(e)) {
/*     */               action.accept(e);
/*     */             }
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean remove(@CheckForNull Object element) {
/* 201 */       return (contains(element) && this.unfiltered.remove(element));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeAll(Collection<?> collection) {
/* 206 */       Objects.requireNonNull(collection); return removeIf(collection::contains);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean retainAll(Collection<?> collection) {
/* 211 */       return removeIf(element -> !collection.contains(element));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeIf(Predicate<? super E> filter) {
/* 216 */       Preconditions.checkNotNull(filter);
/* 217 */       return this.unfiltered.removeIf(element -> (this.predicate.apply(element) && filter.test(element)));
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 222 */       int size = 0;
/* 223 */       for (E e : this.unfiltered) {
/* 224 */         if (this.predicate.apply(e)) {
/* 225 */           size++;
/*     */         }
/*     */       } 
/* 228 */       return size;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Object[] toArray() {
/* 234 */       return Lists.<E>newArrayList(iterator()).toArray();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> T[] toArray(T[] array) {
/* 240 */       return (T[])Lists.<E>newArrayList(iterator()).toArray((Object[])array);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static <F, T> Collection<T> transform(Collection<F> fromCollection, Function<? super F, T> function) {
/* 265 */     return new TransformedCollection<>(fromCollection, function);
/*     */   }
/*     */   
/*     */   static class TransformedCollection<F, T>
/*     */     extends AbstractCollection<T> {
/*     */     final Collection<F> fromCollection;
/*     */     final Function<? super F, ? extends T> function;
/*     */     
/*     */     TransformedCollection(Collection<F> fromCollection, Function<? super F, ? extends T> function) {
/* 274 */       this.fromCollection = (Collection<F>)Preconditions.checkNotNull(fromCollection);
/* 275 */       this.function = (Function<? super F, ? extends T>)Preconditions.checkNotNull(function);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 280 */       this.fromCollection.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 285 */       return this.fromCollection.isEmpty();
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<T> iterator() {
/* 290 */       return Iterators.transform(this.fromCollection.iterator(), this.function);
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator<T> spliterator() {
/* 295 */       return CollectSpliterators.map(this.fromCollection.spliterator(), (Function<? super F, ? extends T>)this.function);
/*     */     }
/*     */ 
/*     */     
/*     */     public void forEach(Consumer<? super T> action) {
/* 300 */       Preconditions.checkNotNull(action);
/* 301 */       this.fromCollection.forEach(f -> action.accept(this.function.apply(f)));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeIf(Predicate<? super T> filter) {
/* 306 */       Preconditions.checkNotNull(filter);
/* 307 */       return this.fromCollection.removeIf(element -> filter.test(this.function.apply(element)));
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 312 */       return this.fromCollection.size();
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
/*     */   static boolean containsAllImpl(Collection<?> self, Collection<?> c) {
/* 328 */     for (Object o : c) {
/* 329 */       if (!self.contains(o)) {
/* 330 */         return false;
/*     */       }
/*     */     } 
/* 333 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   static String toStringImpl(Collection<?> collection) {
/* 338 */     StringBuilder sb = newStringBuilderForCollection(collection.size()).append('[');
/* 339 */     boolean first = true;
/* 340 */     for (Object o : collection) {
/* 341 */       if (!first) {
/* 342 */         sb.append(", ");
/*     */       }
/* 344 */       first = false;
/* 345 */       if (o == collection) {
/* 346 */         sb.append("(this Collection)"); continue;
/*     */       } 
/* 348 */       sb.append(o);
/*     */     } 
/*     */     
/* 351 */     return sb.append(']').toString();
/*     */   }
/*     */ 
/*     */   
/*     */   static StringBuilder newStringBuilderForCollection(int size) {
/* 356 */     CollectPreconditions.checkNonnegative(size, "size");
/* 357 */     return new StringBuilder((int)Math.min(size * 8L, 1073741824L));
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
/*     */   public static <E extends Comparable<? super E>> Collection<List<E>> orderedPermutations(Iterable<E> elements) {
/* 384 */     return orderedPermutations(elements, Ordering.natural());
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Collection<List<E>> orderedPermutations(Iterable<E> elements, Comparator<? super E> comparator) {
/* 435 */     return new OrderedPermutationCollection<>(elements, comparator);
/*     */   }
/*     */   
/*     */   private static final class OrderedPermutationCollection<E> extends AbstractCollection<List<E>> {
/*     */     final ImmutableList<E> inputList;
/*     */     final Comparator<? super E> comparator;
/*     */     final int size;
/*     */     
/*     */     OrderedPermutationCollection(Iterable<E> input, Comparator<? super E> comparator) {
/* 444 */       this.inputList = ImmutableList.sortedCopyOf(comparator, input);
/* 445 */       this.comparator = comparator;
/* 446 */       this.size = calculateSize(this.inputList, comparator);
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
/*     */     private static <E> int calculateSize(List<E> sortedInputList, Comparator<? super E> comparator) {
/* 460 */       int permutations = 1;
/* 461 */       int n = 1;
/* 462 */       int r = 1;
/* 463 */       while (n < sortedInputList.size()) {
/* 464 */         int comparison = comparator.compare(sortedInputList.get(n - 1), sortedInputList.get(n));
/* 465 */         if (comparison < 0) {
/*     */           
/* 467 */           permutations = IntMath.saturatedMultiply(permutations, IntMath.binomial(n, r));
/* 468 */           r = 0;
/* 469 */           if (permutations == Integer.MAX_VALUE) {
/* 470 */             return Integer.MAX_VALUE;
/*     */           }
/*     */         } 
/* 473 */         n++;
/* 474 */         r++;
/*     */       } 
/* 476 */       return IntMath.saturatedMultiply(permutations, IntMath.binomial(n, r));
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 481 */       return this.size;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 486 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<List<E>> iterator() {
/* 491 */       return new Collections2.OrderedPermutationIterator<>(this.inputList, this.comparator);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(@CheckForNull Object obj) {
/* 496 */       if (obj instanceof List) {
/* 497 */         List<?> list = (List)obj;
/* 498 */         return Collections2.isPermutation(this.inputList, list);
/*     */       } 
/* 500 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 505 */       return "orderedPermutationCollection(" + this.inputList + ")";
/*     */     } }
/*     */   
/*     */   private static final class OrderedPermutationIterator<E> extends AbstractIterator<List<E>> {
/*     */     @CheckForNull
/*     */     List<E> nextPermutation;
/*     */     final Comparator<? super E> comparator;
/*     */     
/*     */     OrderedPermutationIterator(List<E> list, Comparator<? super E> comparator) {
/* 514 */       this.nextPermutation = Lists.newArrayList(list);
/* 515 */       this.comparator = comparator;
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     protected List<E> computeNext() {
/* 521 */       if (this.nextPermutation == null) {
/* 522 */         return endOfData();
/*     */       }
/* 524 */       ImmutableList<E> next = ImmutableList.copyOf(this.nextPermutation);
/* 525 */       calculateNextPermutation();
/* 526 */       return next;
/*     */     }
/*     */     
/*     */     void calculateNextPermutation() {
/* 530 */       int j = findNextJ();
/* 531 */       if (j == -1) {
/* 532 */         this.nextPermutation = null;
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */ 
/*     */       
/* 539 */       Objects.requireNonNull(this.nextPermutation);
/*     */       
/* 541 */       int l = findNextL(j);
/* 542 */       Collections.swap(this.nextPermutation, j, l);
/* 543 */       int n = this.nextPermutation.size();
/* 544 */       Collections.reverse(this.nextPermutation.subList(j + 1, n));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int findNextJ() {
/* 552 */       Objects.requireNonNull(this.nextPermutation);
/* 553 */       for (int k = this.nextPermutation.size() - 2; k >= 0; k--) {
/* 554 */         if (this.comparator.compare(this.nextPermutation.get(k), this.nextPermutation.get(k + 1)) < 0) {
/* 555 */           return k;
/*     */         }
/*     */       } 
/* 558 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int findNextL(int j) {
/* 566 */       Objects.requireNonNull(this.nextPermutation);
/* 567 */       E ak = this.nextPermutation.get(j);
/* 568 */       for (int l = this.nextPermutation.size() - 1; l > j; l--) {
/* 569 */         if (this.comparator.compare(ak, this.nextPermutation.get(l)) < 0) {
/* 570 */           return l;
/*     */         }
/*     */       } 
/* 573 */       throw new AssertionError("this statement should be unreachable");
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
/*     */   public static <E> Collection<List<E>> permutations(Collection<E> elements) {
/* 595 */     return new PermutationCollection<>(ImmutableList.copyOf(elements));
/*     */   }
/*     */   
/*     */   private static final class PermutationCollection<E> extends AbstractCollection<List<E>> {
/*     */     final ImmutableList<E> inputList;
/*     */     
/*     */     PermutationCollection(ImmutableList<E> input) {
/* 602 */       this.inputList = input;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 607 */       return IntMath.factorial(this.inputList.size());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 612 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<List<E>> iterator() {
/* 617 */       return new Collections2.PermutationIterator<>(this.inputList);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(@CheckForNull Object obj) {
/* 622 */       if (obj instanceof List) {
/* 623 */         List<?> list = (List)obj;
/* 624 */         return Collections2.isPermutation(this.inputList, list);
/*     */       } 
/* 626 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 631 */       return "permutations(" + this.inputList + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   private static class PermutationIterator<E> extends AbstractIterator<List<E>> {
/*     */     final List<E> list;
/*     */     final int[] c;
/*     */     final int[] o;
/*     */     int j;
/*     */     
/*     */     PermutationIterator(List<E> list) {
/* 642 */       this.list = new ArrayList<>(list);
/* 643 */       int n = list.size();
/* 644 */       this.c = new int[n];
/* 645 */       this.o = new int[n];
/* 646 */       Arrays.fill(this.c, 0);
/* 647 */       Arrays.fill(this.o, 1);
/* 648 */       this.j = Integer.MAX_VALUE;
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     protected List<E> computeNext() {
/* 654 */       if (this.j <= 0) {
/* 655 */         return endOfData();
/*     */       }
/* 657 */       ImmutableList<E> next = ImmutableList.copyOf(this.list);
/* 658 */       calculateNextPermutation();
/* 659 */       return next;
/*     */     }
/*     */     
/*     */     void calculateNextPermutation() {
/* 663 */       this.j = this.list.size() - 1;
/* 664 */       int s = 0;
/*     */ 
/*     */ 
/*     */       
/* 668 */       if (this.j == -1) {
/*     */         return;
/*     */       }
/*     */       
/*     */       while (true) {
/* 673 */         int q = this.c[this.j] + this.o[this.j];
/* 674 */         if (q < 0) {
/* 675 */           switchDirection();
/*     */           continue;
/*     */         } 
/* 678 */         if (q == this.j + 1) {
/* 679 */           if (this.j == 0) {
/*     */             break;
/*     */           }
/* 682 */           s++;
/* 683 */           switchDirection();
/*     */           
/*     */           continue;
/*     */         } 
/* 687 */         Collections.swap(this.list, this.j - this.c[this.j] + s, this.j - q + s);
/* 688 */         this.c[this.j] = q;
/*     */         break;
/*     */       } 
/*     */     }
/*     */     
/*     */     void switchDirection() {
/* 694 */       this.o[this.j] = -this.o[this.j];
/* 695 */       this.j--;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isPermutation(List<?> first, List<?> second) {
/* 701 */     if (first.size() != second.size()) {
/* 702 */       return false;
/*     */     }
/* 704 */     Multiset<?> firstMultiset = HashMultiset.create(first);
/* 705 */     Multiset<?> secondMultiset = HashMultiset.create(second);
/* 706 */     return firstMultiset.equals(secondMultiset);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/Collections2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */