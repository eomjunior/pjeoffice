/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.DoNotCall;
/*     */ import com.google.errorprone.annotations.InlineMe;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Objects;
/*     */ import java.util.RandomAccess;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.UnaryOperator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public abstract class ImmutableList<E>
/*     */   extends ImmutableCollection<E>
/*     */   implements List<E>, RandomAccess
/*     */ {
/*     */   private static final long serialVersionUID = -889275714L;
/*     */   
/*     */   public static <E> Collector<E, ?, ImmutableList<E>> toImmutableList() {
/*  77 */     return CollectCollectors.toImmutableList();
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
/*     */   public static <E> ImmutableList<E> of() {
/*  90 */     return (ImmutableList)RegularImmutableList.EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E element) {
/* 101 */     return new SingletonImmutableList<>(element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2) {
/* 110 */     return construct(new Object[] { e1, e2 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3) {
/* 119 */     return construct(new Object[] { e1, e2, e3 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4) {
/* 128 */     return construct(new Object[] { e1, e2, e3, e4 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5) {
/* 137 */     return construct(new Object[] { e1, e2, e3, e4, e5 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6) {
/* 146 */     return construct(new Object[] { e1, e2, e3, e4, e5, e6 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7) {
/* 155 */     return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8) {
/* 164 */     return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7, e8 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9) {
/* 173 */     return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7, e8, e9 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10) {
/* 183 */     return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7, e8, e9, e10 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10, E e11) {
/* 193 */     return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11 });
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
/*     */   @SafeVarargs
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10, E e11, E e12, E... others) {
/* 210 */     Preconditions.checkArgument((others.length <= 2147483635), "the total number of elements must fit in an int");
/*     */     
/* 212 */     Object[] array = new Object[12 + others.length];
/* 213 */     array[0] = e1;
/* 214 */     array[1] = e2;
/* 215 */     array[2] = e3;
/* 216 */     array[3] = e4;
/* 217 */     array[4] = e5;
/* 218 */     array[5] = e6;
/* 219 */     array[6] = e7;
/* 220 */     array[7] = e8;
/* 221 */     array[8] = e9;
/* 222 */     array[9] = e10;
/* 223 */     array[10] = e11;
/* 224 */     array[11] = e12;
/* 225 */     System.arraycopy(others, 0, array, 12, others.length);
/* 226 */     return construct(array);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> copyOf(Iterable<? extends E> elements) {
/* 237 */     Preconditions.checkNotNull(elements);
/* 238 */     return (elements instanceof Collection) ? 
/* 239 */       copyOf((Collection<? extends E>)elements) : 
/* 240 */       copyOf(elements.iterator());
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
/*     */   public static <E> ImmutableList<E> copyOf(Collection<? extends E> elements) {
/* 261 */     if (elements instanceof ImmutableCollection) {
/*     */       
/* 263 */       ImmutableList<E> list = ((ImmutableCollection)elements).asList();
/* 264 */       return list.isPartialView() ? asImmutableList(list.toArray()) : list;
/*     */     } 
/* 266 */     return construct(elements.toArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> copyOf(Iterator<? extends E> elements) {
/* 276 */     if (!elements.hasNext()) {
/* 277 */       return of();
/*     */     }
/* 279 */     E first = elements.next();
/* 280 */     if (!elements.hasNext()) {
/* 281 */       return of(first);
/*     */     }
/* 283 */     return (new Builder<>()).add(first).addAll(elements).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> copyOf(E[] elements) {
/* 294 */     switch (elements.length) {
/*     */       case 0:
/* 296 */         return of();
/*     */       case 1:
/* 298 */         return of(elements[0]);
/*     */     } 
/* 300 */     return construct((Object[])elements.clone());
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
/*     */   public static <E extends Comparable<? super E>> ImmutableList<E> sortedCopyOf(Iterable<? extends E> elements) {
/* 321 */     Comparable[] arrayOfComparable = Iterables.<Comparable>toArray(elements, new Comparable[0]);
/* 322 */     ObjectArrays.checkElementsNotNull((Object[])arrayOfComparable);
/* 323 */     Arrays.sort((Object[])arrayOfComparable);
/* 324 */     return asImmutableList((Object[])arrayOfComparable);
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
/*     */   public static <E> ImmutableList<E> sortedCopyOf(Comparator<? super E> comparator, Iterable<? extends E> elements) {
/* 344 */     Preconditions.checkNotNull(comparator);
/*     */     
/* 346 */     E[] array = (E[])Iterables.toArray(elements);
/* 347 */     ObjectArrays.checkElementsNotNull((Object[])array);
/* 348 */     Arrays.sort(array, comparator);
/* 349 */     return asImmutableList((Object[])array);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <E> ImmutableList<E> construct(Object... elements) {
/* 354 */     return asImmutableList(ObjectArrays.checkElementsNotNull(elements));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <E> ImmutableList<E> asImmutableList(Object[] elements) {
/* 363 */     return asImmutableList(elements, elements.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <E> ImmutableList<E> asImmutableList(Object[] elements, int length) {
/*     */     E onlyElement;
/* 371 */     switch (length) {
/*     */       case 0:
/* 373 */         return of();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 1:
/* 380 */         onlyElement = Objects.requireNonNull((E)elements[0]);
/* 381 */         return of(onlyElement);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 389 */     Object[] elementsWithoutTrailingNulls = (length < elements.length) ? Arrays.<Object>copyOf(elements, length) : elements;
/* 390 */     return new RegularImmutableList<>(elementsWithoutTrailingNulls);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnmodifiableIterator<E> iterator() {
/* 400 */     return listIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public UnmodifiableListIterator<E> listIterator() {
/* 405 */     return listIterator(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public UnmodifiableListIterator<E> listIterator(int index) {
/* 410 */     return new AbstractIndexedListIterator<E>(size(), index)
/*     */       {
/*     */         protected E get(int index) {
/* 413 */           return ImmutableList.this.get(index);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(Consumer<? super E> consumer) {
/* 420 */     Preconditions.checkNotNull(consumer);
/* 421 */     int n = size();
/* 422 */     for (int i = 0; i < n; i++) {
/* 423 */       consumer.accept(get(i));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int indexOf(@CheckForNull Object object) {
/* 429 */     return (object == null) ? -1 : Lists.indexOfImpl(this, object);
/*     */   }
/*     */ 
/*     */   
/*     */   public int lastIndexOf(@CheckForNull Object object) {
/* 434 */     return (object == null) ? -1 : Lists.lastIndexOfImpl(this, object);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(@CheckForNull Object object) {
/* 439 */     return (indexOf(object) >= 0);
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
/*     */   public ImmutableList<E> subList(int fromIndex, int toIndex) {
/* 457 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
/* 458 */     int length = toIndex - fromIndex;
/* 459 */     if (length == size())
/* 460 */       return this; 
/* 461 */     if (length == 0)
/* 462 */       return of(); 
/* 463 */     if (length == 1) {
/* 464 */       return of(get(fromIndex));
/*     */     }
/* 466 */     return subListUnchecked(fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableList<E> subListUnchecked(int fromIndex, int toIndex) {
/* 475 */     return new SubList(fromIndex, toIndex - fromIndex);
/*     */   }
/*     */   
/*     */   class SubList extends ImmutableList<E> {
/*     */     final transient int offset;
/*     */     final transient int length;
/*     */     
/*     */     SubList(int offset, int length) {
/* 483 */       this.offset = offset;
/* 484 */       this.length = length;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 489 */       return this.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public E get(int index) {
/* 494 */       Preconditions.checkElementIndex(index, this.length);
/* 495 */       return ImmutableList.this.get(index + this.offset);
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableList<E> subList(int fromIndex, int toIndex) {
/* 500 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, this.length);
/* 501 */       return ImmutableList.this.subList(fromIndex + this.offset, toIndex + this.offset);
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 506 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @J2ktIncompatible
/*     */     @GwtIncompatible
/*     */     Object writeReplace() {
/* 515 */       return super.writeReplace();
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
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   @DoNotCall("Always throws UnsupportedOperationException")
/*     */   public final boolean addAll(int index, Collection<? extends E> newElements) {
/* 530 */     throw new UnsupportedOperationException();
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
/*     */   @CanIgnoreReturnValue
/*     */   @DoNotCall("Always throws UnsupportedOperationException")
/*     */   public final E set(int index, E element) {
/* 544 */     throw new UnsupportedOperationException();
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
/*     */   public final void add(int index, E element) {
/* 557 */     throw new UnsupportedOperationException();
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
/*     */   @CanIgnoreReturnValue
/*     */   @DoNotCall("Always throws UnsupportedOperationException")
/*     */   public final E remove(int index) {
/* 571 */     throw new UnsupportedOperationException();
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
/*     */   public final void replaceAll(UnaryOperator<E> operator) {
/* 584 */     throw new UnsupportedOperationException();
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
/*     */   public final void sort(Comparator<? super E> c) {
/* 597 */     throw new UnsupportedOperationException();
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
/*     */   @InlineMe(replacement = "this")
/*     */   public final ImmutableList<E> asList() {
/* 610 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Spliterator<E> spliterator() {
/* 615 */     return CollectSpliterators.indexed(size(), 1296, this::get);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   int copyIntoArray(Object[] dst, int offset) {
/* 621 */     int size = size();
/* 622 */     for (int i = 0; i < size; i++) {
/* 623 */       dst[offset + i] = get(i);
/*     */     }
/* 625 */     return offset + size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableList<E> reverse() {
/* 636 */     return (size() <= 1) ? this : new ReverseImmutableList<>(this);
/*     */   }
/*     */   
/*     */   private static class ReverseImmutableList<E> extends ImmutableList<E> {
/*     */     private final transient ImmutableList<E> forwardList;
/*     */     
/*     */     ReverseImmutableList(ImmutableList<E> backingList) {
/* 643 */       this.forwardList = backingList;
/*     */     }
/*     */     
/*     */     private int reverseIndex(int index) {
/* 647 */       return size() - 1 - index;
/*     */     }
/*     */     
/*     */     private int reversePosition(int index) {
/* 651 */       return size() - index;
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableList<E> reverse() {
/* 656 */       return this.forwardList;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(@CheckForNull Object object) {
/* 661 */       return this.forwardList.contains(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int indexOf(@CheckForNull Object object) {
/* 666 */       int index = this.forwardList.lastIndexOf(object);
/* 667 */       return (index >= 0) ? reverseIndex(index) : -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public int lastIndexOf(@CheckForNull Object object) {
/* 672 */       int index = this.forwardList.indexOf(object);
/* 673 */       return (index >= 0) ? reverseIndex(index) : -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableList<E> subList(int fromIndex, int toIndex) {
/* 678 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
/* 679 */       return this.forwardList.subList(reversePosition(toIndex), reversePosition(fromIndex)).reverse();
/*     */     }
/*     */ 
/*     */     
/*     */     public E get(int index) {
/* 684 */       Preconditions.checkElementIndex(index, size());
/* 685 */       return this.forwardList.get(reverseIndex(index));
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 690 */       return this.forwardList.size();
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 695 */       return this.forwardList.isPartialView();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @J2ktIncompatible
/*     */     @GwtIncompatible
/*     */     Object writeReplace() {
/* 704 */       return super.writeReplace();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@CheckForNull Object obj) {
/* 710 */     return Lists.equalsImpl(this, obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 715 */     int hashCode = 1;
/* 716 */     int n = size();
/* 717 */     for (int i = 0; i < n; i++) {
/* 718 */       hashCode = 31 * hashCode + get(i).hashCode();
/*     */       
/* 720 */       hashCode = hashCode ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
/*     */     } 
/*     */     
/* 723 */     return hashCode;
/*     */   }
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   static class SerializedForm
/*     */     implements Serializable
/*     */   {
/*     */     final Object[] elements;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SerializedForm(Object[] elements) {
/* 735 */       this.elements = elements;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 739 */       return ImmutableList.copyOf(this.elements);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 747 */     throw new InvalidObjectException("Use SerializedForm");
/*     */   }
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   Object writeReplace() {
/* 754 */     return new SerializedForm(toArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Builder<E> builder() {
/* 762 */     return new Builder<>();
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
/*     */   public static <E> Builder<E> builderWithExpectedSize(int expectedSize) {
/* 778 */     CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
/* 779 */     return new Builder<>(expectedSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Builder<E>
/*     */     extends ImmutableCollection.Builder<E>
/*     */   {
/*     */     @VisibleForTesting
/*     */     Object[] contents;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int size;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean forceCopy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder() {
/* 813 */       this(4);
/*     */     }
/*     */     
/*     */     Builder(int capacity) {
/* 817 */       this.contents = new Object[capacity];
/* 818 */       this.size = 0;
/*     */     }
/*     */     
/*     */     private void getReadyToExpandTo(int minCapacity) {
/* 822 */       if (this.contents.length < minCapacity) {
/* 823 */         this.contents = Arrays.copyOf(this.contents, expandedCapacity(this.contents.length, minCapacity));
/* 824 */         this.forceCopy = false;
/* 825 */       } else if (this.forceCopy) {
/* 826 */         this.contents = Arrays.copyOf(this.contents, this.contents.length);
/* 827 */         this.forceCopy = false;
/*     */       } 
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
/* 841 */       Preconditions.checkNotNull(element);
/* 842 */       getReadyToExpandTo(this.size + 1);
/* 843 */       this.contents[this.size++] = element;
/* 844 */       return this;
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
/* 857 */       ObjectArrays.checkElementsNotNull((Object[])elements);
/* 858 */       add((Object[])elements, elements.length);
/* 859 */       return this;
/*     */     }
/*     */     
/*     */     private void add(Object[] elements, int n) {
/* 863 */       getReadyToExpandTo(this.size + n);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 872 */       System.arraycopy(elements, 0, this.contents, this.size, n);
/* 873 */       this.size += n;
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
/* 886 */       Preconditions.checkNotNull(elements);
/* 887 */       if (elements instanceof Collection) {
/* 888 */         Collection<?> collection = (Collection)elements;
/* 889 */         getReadyToExpandTo(this.size + collection.size());
/* 890 */         if (collection instanceof ImmutableCollection) {
/* 891 */           ImmutableCollection<?> immutableCollection = (ImmutableCollection)collection;
/* 892 */           this.size = immutableCollection.copyIntoArray(this.contents, this.size);
/* 893 */           return this;
/*     */         } 
/*     */       } 
/* 896 */       super.addAll(elements);
/* 897 */       return this;
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
/* 910 */       super.addAll(elements);
/* 911 */       return this;
/*     */     }
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<E> combine(Builder<E> builder) {
/* 916 */       Preconditions.checkNotNull(builder);
/* 917 */       add(builder.contents, builder.size);
/* 918 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableList<E> build() {
/* 926 */       this.forceCopy = true;
/* 927 */       return ImmutableList.asImmutableList(this.contents, this.size);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ImmutableList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */