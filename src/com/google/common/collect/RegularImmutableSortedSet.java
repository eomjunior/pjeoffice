/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.Consumer;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ final class RegularImmutableSortedSet<E>
/*     */   extends ImmutableSortedSet<E>
/*     */ {
/*  46 */   static final RegularImmutableSortedSet<Comparable> NATURAL_EMPTY_SET = new RegularImmutableSortedSet(
/*  47 */       ImmutableList.of(), Ordering.natural());
/*     */   
/*     */   private final transient ImmutableList<E> elements;
/*     */   
/*     */   RegularImmutableSortedSet(ImmutableList<E> elements, Comparator<? super E> comparator) {
/*  52 */     super(comparator);
/*  53 */     this.elements = elements;
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   Object[] internalArray() {
/*  59 */     return this.elements.internalArray();
/*     */   }
/*     */ 
/*     */   
/*     */   int internalArrayStart() {
/*  64 */     return this.elements.internalArrayStart();
/*     */   }
/*     */ 
/*     */   
/*     */   int internalArrayEnd() {
/*  69 */     return this.elements.internalArrayEnd();
/*     */   }
/*     */ 
/*     */   
/*     */   public UnmodifiableIterator<E> iterator() {
/*  74 */     return this.elements.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public UnmodifiableIterator<E> descendingIterator() {
/*  80 */     return this.elements.reverse().iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public Spliterator<E> spliterator() {
/*  85 */     return asList().spliterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(Consumer<? super E> action) {
/*  90 */     this.elements.forEach(action);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  95 */     return this.elements.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(@CheckForNull Object o) {
/*     */     try {
/* 101 */       return (o != null && unsafeBinarySearch(o) >= 0);
/* 102 */     } catch (ClassCastException e) {
/* 103 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> targets) {
/* 113 */     if (targets instanceof Multiset) {
/* 114 */       targets = ((Multiset)targets).elementSet();
/*     */     }
/* 116 */     if (!SortedIterables.hasSameComparator(comparator(), targets) || targets.size() <= 1) {
/* 117 */       return super.containsAll(targets);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 124 */     Iterator<E> thisIterator = iterator();
/*     */     
/* 126 */     Iterator<?> thatIterator = targets.iterator();
/*     */ 
/*     */     
/* 129 */     if (!thisIterator.hasNext()) {
/* 130 */       return false;
/*     */     }
/*     */     
/* 133 */     Object target = thatIterator.next();
/* 134 */     E current = thisIterator.next();
/*     */     try {
/*     */       while (true) {
/* 137 */         int cmp = unsafeCompare(current, target);
/*     */         
/* 139 */         if (cmp < 0) {
/* 140 */           if (!thisIterator.hasNext()) {
/* 141 */             return false;
/*     */           }
/* 143 */           current = thisIterator.next(); continue;
/* 144 */         }  if (cmp == 0) {
/* 145 */           if (!thatIterator.hasNext()) {
/* 146 */             return true;
/*     */           }
/* 148 */           target = thatIterator.next(); continue;
/*     */         } 
/* 150 */         if (cmp > 0) {
/* 151 */           return false;
/*     */         }
/*     */       } 
/* 154 */     } catch (NullPointerException|ClassCastException e) {
/* 155 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private int unsafeBinarySearch(Object key) throws ClassCastException {
/* 160 */     return Collections.binarySearch(this.elements, (E)key, (Comparator)unsafeComparator());
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 165 */     return this.elements.isPartialView();
/*     */   }
/*     */ 
/*     */   
/*     */   int copyIntoArray(Object[] dst, int offset) {
/* 170 */     return this.elements.copyIntoArray(dst, offset);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@CheckForNull Object object) {
/* 175 */     if (object == this) {
/* 176 */       return true;
/*     */     }
/* 178 */     if (!(object instanceof Set)) {
/* 179 */       return false;
/*     */     }
/*     */     
/* 182 */     Set<?> that = (Set)object;
/* 183 */     if (size() != that.size())
/* 184 */       return false; 
/* 185 */     if (isEmpty()) {
/* 186 */       return true;
/*     */     }
/*     */     
/* 189 */     if (SortedIterables.hasSameComparator(this.comparator, that)) {
/* 190 */       Iterator<?> otherIterator = that.iterator();
/*     */       try {
/* 192 */         Iterator<E> iterator = iterator();
/* 193 */         while (iterator.hasNext()) {
/* 194 */           Object element = iterator.next();
/* 195 */           Object otherElement = otherIterator.next();
/* 196 */           if (otherElement == null || unsafeCompare(element, otherElement) != 0) {
/* 197 */             return false;
/*     */           }
/*     */         } 
/* 200 */         return true;
/* 201 */       } catch (ClassCastException e) {
/* 202 */         return false;
/* 203 */       } catch (NoSuchElementException e) {
/* 204 */         return false;
/*     */       } 
/*     */     } 
/* 207 */     return containsAll(that);
/*     */   }
/*     */ 
/*     */   
/*     */   public E first() {
/* 212 */     if (isEmpty()) {
/* 213 */       throw new NoSuchElementException();
/*     */     }
/* 215 */     return this.elements.get(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public E last() {
/* 220 */     if (isEmpty()) {
/* 221 */       throw new NoSuchElementException();
/*     */     }
/* 223 */     return this.elements.get(size() - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public E lower(E element) {
/* 229 */     int index = headIndex(element, false) - 1;
/* 230 */     return (index == -1) ? null : this.elements.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public E floor(E element) {
/* 236 */     int index = headIndex(element, true) - 1;
/* 237 */     return (index == -1) ? null : this.elements.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public E ceiling(E element) {
/* 243 */     int index = tailIndex(element, true);
/* 244 */     return (index == size()) ? null : this.elements.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public E higher(E element) {
/* 250 */     int index = tailIndex(element, false);
/* 251 */     return (index == size()) ? null : this.elements.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSortedSet<E> headSetImpl(E toElement, boolean inclusive) {
/* 256 */     return getSubSet(0, headIndex(toElement, inclusive));
/*     */   }
/*     */   
/*     */   int headIndex(E toElement, boolean inclusive) {
/* 260 */     int index = Collections.binarySearch(this.elements, (E)Preconditions.checkNotNull(toElement), comparator());
/* 261 */     if (index >= 0) {
/* 262 */       return inclusive ? (index + 1) : index;
/*     */     }
/* 264 */     return index ^ 0xFFFFFFFF;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableSortedSet<E> subSetImpl(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 271 */     return tailSetImpl(fromElement, fromInclusive).headSetImpl(toElement, toInclusive);
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSortedSet<E> tailSetImpl(E fromElement, boolean inclusive) {
/* 276 */     return getSubSet(tailIndex(fromElement, inclusive), size());
/*     */   }
/*     */   
/*     */   int tailIndex(E fromElement, boolean inclusive) {
/* 280 */     int index = Collections.binarySearch(this.elements, (E)Preconditions.checkNotNull(fromElement), comparator());
/* 281 */     if (index >= 0) {
/* 282 */       return inclusive ? index : (index + 1);
/*     */     }
/* 284 */     return index ^ 0xFFFFFFFF;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Comparator<Object> unsafeComparator() {
/* 293 */     return (Comparator)this.comparator;
/*     */   }
/*     */   
/*     */   RegularImmutableSortedSet<E> getSubSet(int newFromIndex, int newToIndex) {
/* 297 */     if (newFromIndex == 0 && newToIndex == size())
/* 298 */       return this; 
/* 299 */     if (newFromIndex < newToIndex) {
/* 300 */       return new RegularImmutableSortedSet(this.elements
/* 301 */           .subList(newFromIndex, newToIndex), this.comparator);
/*     */     }
/* 303 */     return emptySet(this.comparator);
/*     */   }
/*     */ 
/*     */   
/*     */   int indexOf(@CheckForNull Object target) {
/*     */     int position;
/* 309 */     if (target == null) {
/* 310 */       return -1;
/*     */     }
/*     */     
/*     */     try {
/* 314 */       position = Collections.binarySearch(this.elements, (E)target, (Comparator)unsafeComparator());
/* 315 */     } catch (ClassCastException e) {
/* 316 */       return -1;
/*     */     } 
/* 318 */     return (position >= 0) ? position : -1;
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableList<E> createAsList() {
/* 323 */     return (size() <= 1) ? this.elements : new ImmutableSortedAsList<>(this, this.elements);
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSortedSet<E> createDescendingSet() {
/* 328 */     Comparator<? super E> reversedOrder = Collections.reverseOrder(this.comparator);
/* 329 */     return isEmpty() ? 
/* 330 */       emptySet(reversedOrder) : 
/* 331 */       new RegularImmutableSortedSet(this.elements.reverse(), reversedOrder);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   Object writeReplace() {
/* 340 */     return super.writeReplace();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/RegularImmutableSortedSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */