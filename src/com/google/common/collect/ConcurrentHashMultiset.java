/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.math.IntMath;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ public final class ConcurrentHashMultiset<E>
/*     */   extends AbstractMultiset<E>
/*     */   implements Serializable
/*     */ {
/*     */   private final transient ConcurrentMap<E, AtomicInteger> countMap;
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   private static class FieldSettersHolder
/*     */   {
/*  81 */     static final Serialization.FieldSetter<ConcurrentHashMultiset> COUNT_MAP_FIELD_SETTER = Serialization.getFieldSetter(ConcurrentHashMultiset.class, "countMap");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ConcurrentHashMultiset<E> create() {
/*  92 */     return new ConcurrentHashMultiset<>(new ConcurrentHashMap<>());
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
/*     */   public static <E> ConcurrentHashMultiset<E> create(Iterable<? extends E> elements) {
/* 104 */     ConcurrentHashMultiset<E> multiset = create();
/* 105 */     Iterables.addAll(multiset, elements);
/* 106 */     return multiset;
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
/*     */   public static <E> ConcurrentHashMultiset<E> create(ConcurrentMap<E, AtomicInteger> countMap) {
/* 124 */     return new ConcurrentHashMultiset<>(countMap);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   ConcurrentHashMultiset(ConcurrentMap<E, AtomicInteger> countMap) {
/* 129 */     Preconditions.checkArgument(countMap.isEmpty(), "the backing map (%s) must be empty", countMap);
/* 130 */     this.countMap = countMap;
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
/*     */   public int count(@CheckForNull Object element) {
/* 143 */     AtomicInteger existingCounter = Maps.<AtomicInteger>safeGet(this.countMap, element);
/* 144 */     return (existingCounter == null) ? 0 : existingCounter.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 155 */     long sum = 0L;
/* 156 */     for (AtomicInteger value : this.countMap.values()) {
/* 157 */       sum += value.get();
/*     */     }
/* 159 */     return Ints.saturatedCast(sum);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 169 */     return snapshot().toArray();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T[] toArray(T[] array) {
/* 175 */     return snapshot().toArray(array);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<E> snapshot() {
/* 183 */     List<E> list = Lists.newArrayListWithExpectedSize(size());
/* 184 */     for (Multiset.Entry<E> entry : (Iterable<Multiset.Entry<E>>)entrySet()) {
/* 185 */       E element = entry.getElement();
/* 186 */       for (int i = entry.getCount(); i > 0; i--) {
/* 187 */         list.add(element);
/*     */       }
/*     */     } 
/* 190 */     return list;
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
/*     */   public int add(E element, int occurrences) {
/*     */     AtomicInteger existingCounter, newCounter;
/* 207 */     Preconditions.checkNotNull(element);
/* 208 */     if (occurrences == 0) {
/* 209 */       return count(element);
/*     */     }
/* 211 */     CollectPreconditions.checkPositive(occurrences, "occurrences");
/*     */     
/*     */     do {
/* 214 */       existingCounter = Maps.<AtomicInteger>safeGet(this.countMap, element);
/* 215 */       if (existingCounter == null) {
/* 216 */         existingCounter = this.countMap.putIfAbsent(element, new AtomicInteger(occurrences));
/* 217 */         if (existingCounter == null) {
/* 218 */           return 0;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/*     */       while (true) {
/* 224 */         int oldValue = existingCounter.get();
/* 225 */         if (oldValue != 0) {
/*     */           try {
/* 227 */             int newValue = IntMath.checkedAdd(oldValue, occurrences);
/* 228 */             if (existingCounter.compareAndSet(oldValue, newValue))
/*     */             {
/* 230 */               return oldValue;
/*     */             }
/* 232 */           } catch (ArithmeticException overflow) {
/* 233 */             throw new IllegalArgumentException("Overflow adding " + occurrences + " occurrences to a count of " + oldValue);
/*     */           } 
/*     */           
/*     */           continue;
/*     */         } 
/*     */         break;
/*     */       } 
/* 240 */       newCounter = new AtomicInteger(occurrences);
/* 241 */     } while (this.countMap.putIfAbsent(element, newCounter) != null && 
/* 242 */       !this.countMap.replace(element, existingCounter, newCounter));
/* 243 */     return 0;
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
/*     */   @CanIgnoreReturnValue
/*     */   public int remove(@CheckForNull Object element, int occurrences) {
/* 274 */     if (occurrences == 0) {
/* 275 */       return count(element);
/*     */     }
/* 277 */     CollectPreconditions.checkPositive(occurrences, "occurrences");
/*     */     
/* 279 */     AtomicInteger existingCounter = Maps.<AtomicInteger>safeGet(this.countMap, element);
/* 280 */     if (existingCounter == null) {
/* 281 */       return 0;
/*     */     }
/*     */     while (true) {
/* 284 */       int oldValue = existingCounter.get();
/* 285 */       if (oldValue != 0) {
/* 286 */         int newValue = Math.max(0, oldValue - occurrences);
/* 287 */         if (existingCounter.compareAndSet(oldValue, newValue)) {
/* 288 */           if (newValue == 0)
/*     */           {
/*     */             
/* 291 */             this.countMap.remove(element, existingCounter);
/*     */           }
/* 293 */           return oldValue;
/*     */         }  continue;
/*     */       }  break;
/* 296 */     }  return 0;
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
/*     */   @CanIgnoreReturnValue
/*     */   public boolean removeExactly(@CheckForNull Object element, int occurrences) {
/* 315 */     if (occurrences == 0) {
/* 316 */       return true;
/*     */     }
/* 318 */     CollectPreconditions.checkPositive(occurrences, "occurrences");
/*     */     
/* 320 */     AtomicInteger existingCounter = Maps.<AtomicInteger>safeGet(this.countMap, element);
/* 321 */     if (existingCounter == null) {
/* 322 */       return false;
/*     */     }
/*     */     while (true) {
/* 325 */       int oldValue = existingCounter.get();
/* 326 */       if (oldValue < occurrences) {
/* 327 */         return false;
/*     */       }
/* 329 */       int newValue = oldValue - occurrences;
/* 330 */       if (existingCounter.compareAndSet(oldValue, newValue)) {
/* 331 */         if (newValue == 0)
/*     */         {
/*     */           
/* 334 */           this.countMap.remove(element, existingCounter);
/*     */         }
/* 336 */         return true;
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
/*     */   @CanIgnoreReturnValue
/*     */   public int setCount(E element, int count) {
/* 351 */     Preconditions.checkNotNull(element);
/* 352 */     CollectPreconditions.checkNonnegative(count, "count");
/*     */     label26: while (true) {
/* 354 */       AtomicInteger existingCounter = Maps.<AtomicInteger>safeGet(this.countMap, element);
/* 355 */       if (existingCounter == null) {
/* 356 */         if (count == 0) {
/* 357 */           return 0;
/*     */         }
/* 359 */         existingCounter = this.countMap.putIfAbsent(element, new AtomicInteger(count));
/* 360 */         if (existingCounter == null) {
/* 361 */           return 0;
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 368 */         int oldValue = existingCounter.get();
/* 369 */         if (oldValue == 0) {
/* 370 */           if (count == 0) {
/* 371 */             return 0;
/*     */           }
/* 373 */           AtomicInteger newCounter = new AtomicInteger(count);
/* 374 */           if (this.countMap.putIfAbsent(element, newCounter) == null || this.countMap
/* 375 */             .replace(element, existingCounter, newCounter)) {
/* 376 */             return 0;
/*     */           }
/*     */           
/*     */           continue label26;
/*     */         } 
/* 381 */         if (existingCounter.compareAndSet(oldValue, count)) {
/* 382 */           if (count == 0)
/*     */           {
/*     */             
/* 385 */             this.countMap.remove(element, existingCounter);
/*     */           }
/* 387 */           return oldValue;
/*     */         } 
/*     */       } 
/*     */       break;
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
/*     */   @CanIgnoreReturnValue
/*     */   public boolean setCount(E element, int expectedOldCount, int newCount) {
/* 407 */     Preconditions.checkNotNull(element);
/* 408 */     CollectPreconditions.checkNonnegative(expectedOldCount, "oldCount");
/* 409 */     CollectPreconditions.checkNonnegative(newCount, "newCount");
/*     */     
/* 411 */     AtomicInteger existingCounter = Maps.<AtomicInteger>safeGet(this.countMap, element);
/* 412 */     if (existingCounter == null) {
/* 413 */       if (expectedOldCount != 0)
/* 414 */         return false; 
/* 415 */       if (newCount == 0) {
/* 416 */         return true;
/*     */       }
/*     */       
/* 419 */       return (this.countMap.putIfAbsent(element, new AtomicInteger(newCount)) == null);
/*     */     } 
/*     */     
/* 422 */     int oldValue = existingCounter.get();
/* 423 */     if (oldValue == expectedOldCount) {
/* 424 */       if (oldValue == 0) {
/* 425 */         if (newCount == 0) {
/*     */           
/* 427 */           this.countMap.remove(element, existingCounter);
/* 428 */           return true;
/*     */         } 
/* 430 */         AtomicInteger newCounter = new AtomicInteger(newCount);
/* 431 */         return (this.countMap.putIfAbsent(element, newCounter) == null || this.countMap
/* 432 */           .replace(element, existingCounter, newCounter));
/*     */       } 
/*     */       
/* 435 */       if (existingCounter.compareAndSet(oldValue, newCount)) {
/* 436 */         if (newCount == 0)
/*     */         {
/*     */           
/* 439 */           this.countMap.remove(element, existingCounter);
/*     */         }
/* 441 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 445 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Set<E> createElementSet() {
/* 452 */     final Set<E> delegate = this.countMap.keySet();
/* 453 */     return new ForwardingSet<E>(this)
/*     */       {
/*     */         protected Set<E> delegate() {
/* 456 */           return delegate;
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean contains(@CheckForNull Object object) {
/* 461 */           return (object != null && Collections2.safeContains(delegate, object));
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean containsAll(Collection<?> collection) {
/* 466 */           return standardContainsAll(collection);
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean remove(@CheckForNull Object object) {
/* 471 */           return (object != null && Collections2.safeRemove(delegate, object));
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean removeAll(Collection<?> c) {
/* 476 */           return standardRemoveAll(c);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<E> elementIterator() {
/* 483 */     throw new AssertionError("should never be called");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Set<Multiset.Entry<E>> createEntrySet() {
/* 490 */     return new EntrySet();
/*     */   }
/*     */ 
/*     */   
/*     */   int distinctElements() {
/* 495 */     return this.countMap.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 500 */     return this.countMap.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Iterator<Multiset.Entry<E>> entryIterator() {
/* 507 */     final Iterator<Multiset.Entry<E>> readOnlyIterator = new AbstractIterator<Multiset.Entry<E>>()
/*     */       {
/* 509 */         private final Iterator<Map.Entry<E, AtomicInteger>> mapEntries = ConcurrentHashMultiset.this
/* 510 */           .countMap.entrySet().iterator();
/*     */ 
/*     */         
/*     */         @CheckForNull
/*     */         protected Multiset.Entry<E> computeNext() {
/*     */           while (true) {
/* 516 */             if (!this.mapEntries.hasNext()) {
/* 517 */               return endOfData();
/*     */             }
/* 519 */             Map.Entry<E, AtomicInteger> mapEntry = this.mapEntries.next();
/* 520 */             int count = ((AtomicInteger)mapEntry.getValue()).get();
/* 521 */             if (count != 0) {
/* 522 */               return Multisets.immutableEntry(mapEntry.getKey(), count);
/*     */             }
/*     */           } 
/*     */         }
/*     */       };
/*     */     
/* 528 */     return new ForwardingIterator<Multiset.Entry<E>>() {
/*     */         @CheckForNull
/*     */         private Multiset.Entry<E> last;
/*     */         
/*     */         protected Iterator<Multiset.Entry<E>> delegate() {
/* 533 */           return readOnlyIterator;
/*     */         }
/*     */ 
/*     */         
/*     */         public Multiset.Entry<E> next() {
/* 538 */           this.last = super.next();
/* 539 */           return this.last;
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 544 */           Preconditions.checkState((this.last != null), "no calls to next() since the last call to remove()");
/* 545 */           ConcurrentHashMultiset.this.setCount(this.last.getElement(), 0);
/* 546 */           this.last = null;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/* 553 */     return Multisets.iteratorImpl(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 558 */     this.countMap.clear();
/*     */   }
/*     */   
/*     */   private class EntrySet extends AbstractMultiset<E>.EntrySet {
/*     */     private EntrySet() {}
/*     */     
/*     */     ConcurrentHashMultiset<E> multiset() {
/* 565 */       return ConcurrentHashMultiset.this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object[] toArray() {
/* 575 */       return snapshot().toArray();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> T[] toArray(T[] array) {
/* 581 */       return snapshot().toArray(array);
/*     */     }
/*     */     
/*     */     private List<Multiset.Entry<E>> snapshot() {
/* 585 */       List<Multiset.Entry<E>> list = Lists.newArrayListWithExpectedSize(size());
/*     */       
/* 587 */       Iterators.addAll(list, iterator());
/* 588 */       return list;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 594 */     stream.defaultWriteObject();
/* 595 */     stream.writeObject(this.countMap);
/*     */   }
/*     */   
/*     */   @J2ktIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 600 */     stream.defaultReadObject();
/*     */ 
/*     */     
/* 603 */     ConcurrentMap<E, Integer> deserializedCountMap = (ConcurrentMap<E, Integer>)Objects.<Object>requireNonNull(stream.readObject());
/* 604 */     FieldSettersHolder.COUNT_MAP_FIELD_SETTER.set(this, deserializedCountMap);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ConcurrentHashMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */