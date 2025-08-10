/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.math.IntMath;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.j2objc.annotations.Weak;
/*     */ import java.util.AbstractQueue;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Objects;
/*     */ import java.util.Queue;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ public final class MinMaxPriorityQueue<E>
/*     */   extends AbstractQueue<E>
/*     */ {
/*     */   private final Heap minHeap;
/*     */   private final Heap maxHeap;
/*     */   @VisibleForTesting
/*     */   final int maximumSize;
/*     */   private Object[] queue;
/*     */   private int size;
/*     */   private int modCount;
/*     */   private static final int EVEN_POWERS_OF_TWO = 1431655765;
/*     */   private static final int ODD_POWERS_OF_TWO = -1431655766;
/*     */   private static final int DEFAULT_CAPACITY = 11;
/*     */   
/*     */   public static <E extends Comparable<E>> MinMaxPriorityQueue<E> create() {
/* 110 */     return (new Builder(Ordering.natural())).create();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Comparable<E>> MinMaxPriorityQueue<E> create(Iterable<? extends E> initialContents) {
/* 119 */     return (new Builder(Ordering.natural())).create(initialContents);
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
/*     */   public static <B> Builder<B> orderedBy(Comparator<B> comparator) {
/* 133 */     return new Builder<>(comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Builder<Comparable> expectedSize(int expectedSize) {
/* 141 */     return (new Builder<>(Ordering.natural())).expectedSize(expectedSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Builder<Comparable> maximumSize(int maximumSize) {
/* 151 */     return (new Builder<>(Ordering.natural())).maximumSize(maximumSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Builder<B>
/*     */   {
/*     */     private static final int UNSET_EXPECTED_SIZE = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Comparator<B> comparator;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 172 */     private int expectedSize = -1;
/* 173 */     private int maximumSize = Integer.MAX_VALUE;
/*     */     
/*     */     private Builder(Comparator<B> comparator) {
/* 176 */       this.comparator = (Comparator<B>)Preconditions.checkNotNull(comparator);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<B> expectedSize(int expectedSize) {
/* 185 */       Preconditions.checkArgument((expectedSize >= 0));
/* 186 */       this.expectedSize = expectedSize;
/* 187 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<B> maximumSize(int maximumSize) {
/* 198 */       Preconditions.checkArgument((maximumSize > 0));
/* 199 */       this.maximumSize = maximumSize;
/* 200 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <T extends B> MinMaxPriorityQueue<T> create() {
/* 208 */       return create(Collections.emptySet());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <T extends B> MinMaxPriorityQueue<T> create(Iterable<? extends T> initialContents) {
/* 218 */       MinMaxPriorityQueue<T> queue = new MinMaxPriorityQueue<>(this, MinMaxPriorityQueue.initialQueueSize(this.expectedSize, this.maximumSize, initialContents));
/* 219 */       for (T element : initialContents) {
/* 220 */         queue.offer(element);
/*     */       }
/* 222 */       return queue;
/*     */     }
/*     */ 
/*     */     
/*     */     private <T extends B> Ordering<T> ordering() {
/* 227 */       return Ordering.from(this.comparator);
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
/*     */   private MinMaxPriorityQueue(Builder<? super E> builder, int queueSize) {
/* 239 */     Ordering<E> ordering = builder.ordering();
/* 240 */     this.minHeap = new Heap(ordering);
/* 241 */     this.maxHeap = new Heap(ordering.reverse());
/* 242 */     this.minHeap.otherHeap = this.maxHeap;
/* 243 */     this.maxHeap.otherHeap = this.minHeap;
/*     */     
/* 245 */     this.maximumSize = builder.maximumSize;
/*     */     
/* 247 */     this.queue = new Object[queueSize];
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 252 */     return this.size;
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
/*     */   @CanIgnoreReturnValue
/*     */   public boolean add(E element) {
/* 265 */     offer(element);
/* 266 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean addAll(Collection<? extends E> newElements) {
/* 272 */     boolean modified = false;
/* 273 */     for (E element : newElements) {
/* 274 */       offer(element);
/* 275 */       modified = true;
/*     */     } 
/* 277 */     return modified;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean offer(E element) {
/* 288 */     Preconditions.checkNotNull(element);
/* 289 */     this.modCount++;
/* 290 */     int insertIndex = this.size++;
/*     */     
/* 292 */     growIfNeeded();
/*     */ 
/*     */ 
/*     */     
/* 296 */     heapForIndex(insertIndex).bubbleUp(insertIndex, element);
/* 297 */     return (this.size <= this.maximumSize || pollLast() != element);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   public E poll() {
/* 304 */     return isEmpty() ? null : removeAndGet(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   E elementData(int index) {
/* 313 */     return Objects.requireNonNull((E)this.queue[index]);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public E peek() {
/* 319 */     return isEmpty() ? null : elementData(0);
/*     */   }
/*     */ 
/*     */   
/*     */   private int getMaxElementIndex() {
/* 324 */     switch (this.size) {
/*     */       case 1:
/* 326 */         return 0;
/*     */       case 2:
/* 328 */         return 1;
/*     */     } 
/*     */ 
/*     */     
/* 332 */     return (this.maxHeap.compareElements(1, 2) <= 0) ? 1 : 2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   public E pollFirst() {
/* 343 */     return poll();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E removeFirst() {
/* 353 */     return remove();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public E peekFirst() {
/* 362 */     return peek();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   public E pollLast() {
/* 372 */     return isEmpty() ? null : removeAndGet(getMaxElementIndex());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E removeLast() {
/* 382 */     if (isEmpty()) {
/* 383 */       throw new NoSuchElementException();
/*     */     }
/* 385 */     return removeAndGet(getMaxElementIndex());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public E peekLast() {
/* 394 */     return isEmpty() ? null : elementData(getMaxElementIndex());
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
/*     */   @CheckForNull
/*     */   @VisibleForTesting
/*     */   @CanIgnoreReturnValue
/*     */   MoveDesc<E> removeAt(int index) {
/* 414 */     Preconditions.checkPositionIndex(index, this.size);
/* 415 */     this.modCount++;
/* 416 */     this.size--;
/* 417 */     if (this.size == index) {
/* 418 */       this.queue[this.size] = null;
/* 419 */       return null;
/*     */     } 
/* 421 */     E actualLastElement = elementData(this.size);
/* 422 */     int lastElementAt = heapForIndex(this.size).swapWithConceptuallyLastElement(actualLastElement);
/* 423 */     if (lastElementAt == index) {
/*     */ 
/*     */ 
/*     */       
/* 427 */       this.queue[this.size] = null;
/* 428 */       return null;
/*     */     } 
/* 430 */     E toTrickle = elementData(this.size);
/* 431 */     this.queue[this.size] = null;
/* 432 */     MoveDesc<E> changes = fillHole(index, toTrickle);
/* 433 */     if (lastElementAt < index) {
/*     */       
/* 435 */       if (changes == null)
/*     */       {
/* 437 */         return new MoveDesc<>(actualLastElement, toTrickle);
/*     */       }
/*     */ 
/*     */       
/* 441 */       return new MoveDesc<>(actualLastElement, changes.replaced);
/*     */     } 
/*     */ 
/*     */     
/* 445 */     return changes;
/*     */   }
/*     */   
/*     */   @CheckForNull
/*     */   private MoveDesc<E> fillHole(int index, E toTrickle) {
/* 450 */     Heap heap = heapForIndex(index);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 458 */     int vacated = heap.fillHoleAt(index);
/*     */     
/* 460 */     int bubbledTo = heap.bubbleUpAlternatingLevels(vacated, toTrickle);
/* 461 */     if (bubbledTo == vacated)
/*     */     {
/*     */ 
/*     */       
/* 465 */       return heap.tryCrossOverAndBubbleUp(index, vacated, toTrickle);
/*     */     }
/* 467 */     return (bubbledTo < index) ? new MoveDesc<>(toTrickle, elementData(index)) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   static class MoveDesc<E>
/*     */   {
/*     */     final E toTrickle;
/*     */     final E replaced;
/*     */     
/*     */     MoveDesc(E toTrickle, E replaced) {
/* 477 */       this.toTrickle = toTrickle;
/* 478 */       this.replaced = replaced;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private E removeAndGet(int index) {
/* 484 */     E value = elementData(index);
/* 485 */     removeAt(index);
/* 486 */     return value;
/*     */   }
/*     */   
/*     */   private Heap heapForIndex(int i) {
/* 490 */     return isEvenLevel(i) ? this.minHeap : this.maxHeap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static boolean isEvenLevel(int index) {
/* 498 */     int oneBased = index + 1 ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
/* 499 */     Preconditions.checkState((oneBased > 0), "negative index");
/* 500 */     return ((oneBased & 0x55555555) > (oneBased & 0xAAAAAAAA));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   boolean isIntact() {
/* 510 */     for (int i = 1; i < this.size; i++) {
/* 511 */       if (!heapForIndex(i).verifyIndex(i)) {
/* 512 */         return false;
/*     */       }
/*     */     } 
/* 515 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   class Heap
/*     */   {
/*     */     final Ordering<E> ordering;
/*     */ 
/*     */     
/*     */     @Weak
/*     */     Heap otherHeap;
/*     */ 
/*     */ 
/*     */     
/*     */     Heap(Ordering<E> ordering) {
/* 532 */       this.ordering = ordering;
/*     */     }
/*     */     
/*     */     int compareElements(int a, int b) {
/* 536 */       return this.ordering.compare(MinMaxPriorityQueue.this.elementData(a), MinMaxPriorityQueue.this.elementData(b));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     MinMaxPriorityQueue.MoveDesc<E> tryCrossOverAndBubbleUp(int removeIndex, int vacated, E toTrickle) {
/*     */       E parent;
/* 545 */       int crossOver = crossOver(vacated, toTrickle);
/* 546 */       if (crossOver == vacated) {
/* 547 */         return null;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 555 */       if (crossOver < removeIndex) {
/*     */ 
/*     */         
/* 558 */         parent = MinMaxPriorityQueue.this.elementData(removeIndex);
/*     */       } else {
/* 560 */         parent = MinMaxPriorityQueue.this.elementData(getParentIndex(removeIndex));
/*     */       } 
/*     */       
/* 563 */       if (this.otherHeap.bubbleUpAlternatingLevels(crossOver, toTrickle) < removeIndex) {
/* 564 */         return new MinMaxPriorityQueue.MoveDesc<>(toTrickle, parent);
/*     */       }
/* 566 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     void bubbleUp(int index, E x) {
/*     */       Heap heap;
/* 572 */       int crossOver = crossOverUp(index, x);
/*     */ 
/*     */       
/* 575 */       if (crossOver == index) {
/* 576 */         heap = this;
/*     */       } else {
/* 578 */         index = crossOver;
/* 579 */         heap = this.otherHeap;
/*     */       } 
/* 581 */       heap.bubbleUpAlternatingLevels(index, x);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     int bubbleUpAlternatingLevels(int index, E x) {
/* 590 */       while (index > 2) {
/* 591 */         int grandParentIndex = getGrandparentIndex(index);
/* 592 */         E e = MinMaxPriorityQueue.this.elementData(grandParentIndex);
/* 593 */         if (this.ordering.compare(e, x) <= 0) {
/*     */           break;
/*     */         }
/* 596 */         MinMaxPriorityQueue.this.queue[index] = e;
/* 597 */         index = grandParentIndex;
/*     */       } 
/* 599 */       MinMaxPriorityQueue.this.queue[index] = x;
/* 600 */       return index;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int findMin(int index, int len) {
/* 608 */       if (index >= MinMaxPriorityQueue.this.size) {
/* 609 */         return -1;
/*     */       }
/* 611 */       Preconditions.checkState((index > 0));
/* 612 */       int limit = Math.min(index, MinMaxPriorityQueue.this.size - len) + len;
/* 613 */       int minIndex = index;
/* 614 */       for (int i = index + 1; i < limit; i++) {
/* 615 */         if (compareElements(i, minIndex) < 0) {
/* 616 */           minIndex = i;
/*     */         }
/*     */       } 
/* 619 */       return minIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     int findMinChild(int index) {
/* 624 */       return findMin(getLeftChildIndex(index), 2);
/*     */     }
/*     */ 
/*     */     
/*     */     int findMinGrandChild(int index) {
/* 629 */       int leftChildIndex = getLeftChildIndex(index);
/* 630 */       if (leftChildIndex < 0) {
/* 631 */         return -1;
/*     */       }
/* 633 */       return findMin(getLeftChildIndex(leftChildIndex), 4);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int crossOverUp(int index, E x) {
/* 641 */       if (index == 0) {
/* 642 */         MinMaxPriorityQueue.this.queue[0] = x;
/* 643 */         return 0;
/*     */       } 
/* 645 */       int parentIndex = getParentIndex(index);
/* 646 */       E parentElement = MinMaxPriorityQueue.this.elementData(parentIndex);
/* 647 */       if (parentIndex != 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 653 */         int grandparentIndex = getParentIndex(parentIndex);
/* 654 */         int auntIndex = getRightChildIndex(grandparentIndex);
/* 655 */         if (auntIndex != parentIndex && getLeftChildIndex(auntIndex) >= MinMaxPriorityQueue.this.size) {
/* 656 */           E auntElement = MinMaxPriorityQueue.this.elementData(auntIndex);
/* 657 */           if (this.ordering.compare(auntElement, parentElement) < 0) {
/* 658 */             parentIndex = auntIndex;
/* 659 */             parentElement = auntElement;
/*     */           } 
/*     */         } 
/*     */       } 
/* 663 */       if (this.ordering.compare(parentElement, x) < 0) {
/* 664 */         MinMaxPriorityQueue.this.queue[index] = parentElement;
/* 665 */         MinMaxPriorityQueue.this.queue[parentIndex] = x;
/* 666 */         return parentIndex;
/*     */       } 
/* 668 */       MinMaxPriorityQueue.this.queue[index] = x;
/* 669 */       return index;
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
/*     */     
/*     */     int swapWithConceptuallyLastElement(E actualLastElement) {
/* 686 */       int parentIndex = getParentIndex(MinMaxPriorityQueue.this.size);
/* 687 */       if (parentIndex != 0) {
/* 688 */         int grandparentIndex = getParentIndex(parentIndex);
/* 689 */         int auntIndex = getRightChildIndex(grandparentIndex);
/* 690 */         if (auntIndex != parentIndex && getLeftChildIndex(auntIndex) >= MinMaxPriorityQueue.this.size) {
/* 691 */           E auntElement = MinMaxPriorityQueue.this.elementData(auntIndex);
/* 692 */           if (this.ordering.compare(auntElement, actualLastElement) < 0) {
/* 693 */             MinMaxPriorityQueue.this.queue[auntIndex] = actualLastElement;
/* 694 */             MinMaxPriorityQueue.this.queue[MinMaxPriorityQueue.this.size] = auntElement;
/* 695 */             return auntIndex;
/*     */           } 
/*     */         } 
/*     */       } 
/* 699 */       return MinMaxPriorityQueue.this.size;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int crossOver(int index, E x) {
/* 709 */       int minChildIndex = findMinChild(index);
/*     */ 
/*     */       
/* 712 */       if (minChildIndex > 0 && this.ordering.compare(MinMaxPriorityQueue.this.elementData(minChildIndex), x) < 0) {
/* 713 */         MinMaxPriorityQueue.this.queue[index] = MinMaxPriorityQueue.this.elementData(minChildIndex);
/* 714 */         MinMaxPriorityQueue.this.queue[minChildIndex] = x;
/* 715 */         return minChildIndex;
/*     */       } 
/* 717 */       return crossOverUp(index, x);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int fillHoleAt(int index) {
/*     */       int minGrandchildIndex;
/* 729 */       while ((minGrandchildIndex = findMinGrandChild(index)) > 0) {
/* 730 */         MinMaxPriorityQueue.this.queue[index] = MinMaxPriorityQueue.this.elementData(minGrandchildIndex);
/* 731 */         index = minGrandchildIndex;
/*     */       } 
/* 733 */       return index;
/*     */     }
/*     */     
/*     */     private boolean verifyIndex(int i) {
/* 737 */       if (getLeftChildIndex(i) < MinMaxPriorityQueue.this.size && compareElements(i, getLeftChildIndex(i)) > 0) {
/* 738 */         return false;
/*     */       }
/* 740 */       if (getRightChildIndex(i) < MinMaxPriorityQueue.this.size && compareElements(i, getRightChildIndex(i)) > 0) {
/* 741 */         return false;
/*     */       }
/* 743 */       if (i > 0 && compareElements(i, getParentIndex(i)) > 0) {
/* 744 */         return false;
/*     */       }
/* 746 */       if (i > 2 && compareElements(getGrandparentIndex(i), i) > 0) {
/* 747 */         return false;
/*     */       }
/* 749 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private int getLeftChildIndex(int i) {
/* 755 */       return i * 2 + 1;
/*     */     }
/*     */     
/*     */     private int getRightChildIndex(int i) {
/* 759 */       return i * 2 + 2;
/*     */     }
/*     */     
/*     */     private int getParentIndex(int i) {
/* 763 */       return (i - 1) / 2;
/*     */     }
/*     */     
/*     */     private int getGrandparentIndex(int i) {
/* 767 */       return getParentIndex(getParentIndex(i));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class QueueIterator
/*     */     implements Iterator<E>
/*     */   {
/* 777 */     private int cursor = -1;
/* 778 */     private int nextCursor = -1;
/* 779 */     private int expectedModCount = MinMaxPriorityQueue.this.modCount;
/*     */     @CheckForNull
/*     */     private Queue<E> forgetMeNot;
/*     */     @CheckForNull
/*     */     private List<E> skipMe;
/*     */     @CheckForNull
/*     */     private E lastFromForgetMeNot;
/*     */     private boolean canRemove;
/*     */     
/*     */     public boolean hasNext() {
/* 789 */       checkModCount();
/* 790 */       nextNotInSkipMe(this.cursor + 1);
/* 791 */       return (this.nextCursor < MinMaxPriorityQueue.this.size() || (this.forgetMeNot != null && !this.forgetMeNot.isEmpty()));
/*     */     }
/*     */ 
/*     */     
/*     */     public E next() {
/* 796 */       checkModCount();
/* 797 */       nextNotInSkipMe(this.cursor + 1);
/* 798 */       if (this.nextCursor < MinMaxPriorityQueue.this.size()) {
/* 799 */         this.cursor = this.nextCursor;
/* 800 */         this.canRemove = true;
/* 801 */         return MinMaxPriorityQueue.this.elementData(this.cursor);
/* 802 */       }  if (this.forgetMeNot != null) {
/* 803 */         this.cursor = MinMaxPriorityQueue.this.size();
/* 804 */         this.lastFromForgetMeNot = this.forgetMeNot.poll();
/* 805 */         if (this.lastFromForgetMeNot != null) {
/* 806 */           this.canRemove = true;
/* 807 */           return this.lastFromForgetMeNot;
/*     */         } 
/*     */       } 
/* 810 */       throw new NoSuchElementException("iterator moved past last element in queue.");
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 815 */       CollectPreconditions.checkRemove(this.canRemove);
/* 816 */       checkModCount();
/* 817 */       this.canRemove = false;
/* 818 */       this.expectedModCount++;
/* 819 */       if (this.cursor < MinMaxPriorityQueue.this.size()) {
/* 820 */         MinMaxPriorityQueue.MoveDesc<E> moved = MinMaxPriorityQueue.this.removeAt(this.cursor);
/* 821 */         if (moved != null) {
/*     */           
/* 823 */           if (this.forgetMeNot == null || this.skipMe == null) {
/* 824 */             this.forgetMeNot = new ArrayDeque<>();
/* 825 */             this.skipMe = new ArrayList<>(3);
/*     */           } 
/* 827 */           if (!foundAndRemovedExactReference(this.skipMe, moved.toTrickle)) {
/* 828 */             this.forgetMeNot.add(moved.toTrickle);
/*     */           }
/* 830 */           if (!foundAndRemovedExactReference(this.forgetMeNot, moved.replaced)) {
/* 831 */             this.skipMe.add(moved.replaced);
/*     */           }
/*     */         } 
/* 834 */         this.cursor--;
/* 835 */         this.nextCursor--;
/*     */       } else {
/* 837 */         Preconditions.checkState(removeExact(Objects.requireNonNull(this.lastFromForgetMeNot)));
/* 838 */         this.lastFromForgetMeNot = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean foundAndRemovedExactReference(Iterable<E> elements, E target) {
/* 844 */       for (Iterator<E> it = elements.iterator(); it.hasNext(); ) {
/* 845 */         E element = it.next();
/* 846 */         if (element == target) {
/* 847 */           it.remove();
/* 848 */           return true;
/*     */         } 
/*     */       } 
/* 851 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean removeExact(Object target) {
/* 856 */       for (int i = 0; i < MinMaxPriorityQueue.this.size; i++) {
/* 857 */         if (MinMaxPriorityQueue.this.queue[i] == target) {
/* 858 */           MinMaxPriorityQueue.this.removeAt(i);
/* 859 */           return true;
/*     */         } 
/*     */       } 
/* 862 */       return false;
/*     */     }
/*     */     
/*     */     private void checkModCount() {
/* 866 */       if (MinMaxPriorityQueue.this.modCount != this.expectedModCount) {
/* 867 */         throw new ConcurrentModificationException();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void nextNotInSkipMe(int c) {
/* 876 */       if (this.nextCursor < c) {
/* 877 */         if (this.skipMe != null) {
/* 878 */           while (c < MinMaxPriorityQueue.this.size() && foundAndRemovedExactReference(this.skipMe, MinMaxPriorityQueue.this.elementData(c))) {
/* 879 */             c++;
/*     */           }
/*     */         }
/* 882 */         this.nextCursor = c;
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
/*     */     
/*     */     private QueueIterator() {}
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
/*     */   public Iterator<E> iterator() {
/* 908 */     return new QueueIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 913 */     for (int i = 0; i < this.size; i++) {
/* 914 */       this.queue[i] = null;
/*     */     }
/* 916 */     this.size = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   public Object[] toArray() {
/* 922 */     Object[] copyTo = new Object[this.size];
/* 923 */     System.arraycopy(this.queue, 0, copyTo, 0, this.size);
/* 924 */     return copyTo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Comparator<? super E> comparator() {
/* 933 */     return this.minHeap.ordering;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   int capacity() {
/* 938 */     return this.queue.length;
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
/*     */   @VisibleForTesting
/*     */   static int initialQueueSize(int configuredExpectedSize, int maximumSize, Iterable<?> initialContents) {
/* 952 */     int result = (configuredExpectedSize == -1) ? 11 : configuredExpectedSize;
/*     */ 
/*     */     
/* 955 */     if (initialContents instanceof Collection) {
/* 956 */       int initialSize = ((Collection)initialContents).size();
/* 957 */       result = Math.max(result, initialSize);
/*     */     } 
/*     */ 
/*     */     
/* 961 */     return capAtMaximumSize(result, maximumSize);
/*     */   }
/*     */   
/*     */   private void growIfNeeded() {
/* 965 */     if (this.size > this.queue.length) {
/* 966 */       int newCapacity = calculateNewCapacity();
/* 967 */       Object[] newQueue = new Object[newCapacity];
/* 968 */       System.arraycopy(this.queue, 0, newQueue, 0, this.queue.length);
/* 969 */       this.queue = newQueue;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private int calculateNewCapacity() {
/* 975 */     int oldCapacity = this.queue.length;
/*     */     
/* 977 */     int newCapacity = (oldCapacity < 64) ? ((oldCapacity + 1) * 2) : IntMath.checkedMultiply(oldCapacity / 2, 3);
/* 978 */     return capAtMaximumSize(newCapacity, this.maximumSize);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int capAtMaximumSize(int queueSize, int maximumSize) {
/* 983 */     return Math.min(queueSize - 1, maximumSize) + 1;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/MinMaxPriorityQueue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */