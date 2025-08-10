/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ import java.util.function.BiConsumer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class LinkedHashMultimap<K, V>
/*     */   extends LinkedHashMultimapGwtSerializationDependencies<K, V>
/*     */ {
/*     */   private static final int DEFAULT_KEY_CAPACITY = 16;
/*     */   private static final int DEFAULT_VALUE_SET_CAPACITY = 2;
/*     */   @VisibleForTesting
/*     */   static final double VALUE_SET_LOAD_FACTOR = 1.0D;
/*     */   
/*     */   public static <K, V> LinkedHashMultimap<K, V> create() {
/*  94 */     return new LinkedHashMultimap<>(16, 2);
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
/*     */   public static <K, V> LinkedHashMultimap<K, V> create(int expectedKeys, int expectedValuesPerKey) {
/* 108 */     return new LinkedHashMultimap<>(
/* 109 */         Maps.capacity(expectedKeys), Maps.capacity(expectedValuesPerKey));
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
/*     */   public static <K, V> LinkedHashMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
/* 122 */     LinkedHashMultimap<K, V> result = create(multimap.keySet().size(), 2);
/* 123 */     result.putAll(multimap);
/* 124 */     return result;
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
/*     */   private static <K, V> void succeedsInValueSet(ValueSetLink<K, V> pred, ValueSetLink<K, V> succ) {
/* 139 */     pred.setSuccessorInValueSet(succ);
/* 140 */     succ.setPredecessorInValueSet(pred);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <K, V> void succeedsInMultimap(ValueEntry<K, V> pred, ValueEntry<K, V> succ) {
/* 145 */     pred.setSuccessorInMultimap(succ);
/* 146 */     succ.setPredecessorInMultimap(pred);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <K, V> void deleteFromValueSet(ValueSetLink<K, V> entry) {
/* 151 */     succeedsInValueSet(entry.getPredecessorInValueSet(), entry.getSuccessorInValueSet());
/*     */   }
/*     */ 
/*     */   
/*     */   private static <K, V> void deleteFromMultimap(ValueEntry<K, V> entry) {
/* 156 */     succeedsInMultimap(entry.getPredecessorInMultimap(), entry.getSuccessorInMultimap());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static final class ValueEntry<K, V>
/*     */     extends ImmutableEntry<K, V>
/*     */     implements ValueSetLink<K, V>
/*     */   {
/*     */     final int smearedValueHash;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     ValueEntry<K, V> nextInValueBucket;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     private LinkedHashMultimap.ValueSetLink<K, V> predecessorInValueSet;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     private LinkedHashMultimap.ValueSetLink<K, V> successorInValueSet;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     private ValueEntry<K, V> predecessorInMultimap;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     private ValueEntry<K, V> successorInMultimap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     ValueEntry(@ParametricNullness K key, @ParametricNullness V value, int smearedValueHash, @CheckForNull ValueEntry<K, V> nextInValueBucket) {
/* 207 */       super(key, value);
/* 208 */       this.smearedValueHash = smearedValueHash;
/* 209 */       this.nextInValueBucket = nextInValueBucket;
/*     */     }
/*     */ 
/*     */     
/*     */     static <K, V> ValueEntry<K, V> newHeader() {
/* 214 */       return new ValueEntry<>(null, null, 0, null);
/*     */     }
/*     */     
/*     */     boolean matchesValue(@CheckForNull Object v, int smearedVHash) {
/* 218 */       return (this.smearedValueHash == smearedVHash && Objects.equal(getValue(), v));
/*     */     }
/*     */ 
/*     */     
/*     */     public LinkedHashMultimap.ValueSetLink<K, V> getPredecessorInValueSet() {
/* 223 */       return Objects.<LinkedHashMultimap.ValueSetLink<K, V>>requireNonNull(this.predecessorInValueSet);
/*     */     }
/*     */ 
/*     */     
/*     */     public LinkedHashMultimap.ValueSetLink<K, V> getSuccessorInValueSet() {
/* 228 */       return Objects.<LinkedHashMultimap.ValueSetLink<K, V>>requireNonNull(this.successorInValueSet);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setPredecessorInValueSet(LinkedHashMultimap.ValueSetLink<K, V> entry) {
/* 233 */       this.predecessorInValueSet = entry;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setSuccessorInValueSet(LinkedHashMultimap.ValueSetLink<K, V> entry) {
/* 238 */       this.successorInValueSet = entry;
/*     */     }
/*     */     
/*     */     public ValueEntry<K, V> getPredecessorInMultimap() {
/* 242 */       return Objects.<ValueEntry<K, V>>requireNonNull(this.predecessorInMultimap);
/*     */     }
/*     */     
/*     */     public ValueEntry<K, V> getSuccessorInMultimap() {
/* 246 */       return Objects.<ValueEntry<K, V>>requireNonNull(this.successorInMultimap);
/*     */     }
/*     */     
/*     */     public void setSuccessorInMultimap(ValueEntry<K, V> multimapSuccessor) {
/* 250 */       this.successorInMultimap = multimapSuccessor;
/*     */     }
/*     */     
/*     */     public void setPredecessorInMultimap(ValueEntry<K, V> multimapPredecessor) {
/* 254 */       this.predecessorInMultimap = multimapPredecessor;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/* 262 */   transient int valueSetCapacity = 2;
/*     */   private transient ValueEntry<K, V> multimapHeaderEntry;
/*     */   
/*     */   private LinkedHashMultimap(int keyCapacity, int valueSetCapacity) {
/* 266 */     super(Platform.newLinkedHashMapWithExpectedSize(keyCapacity));
/* 267 */     CollectPreconditions.checkNonnegative(valueSetCapacity, "expectedValuesPerKey");
/*     */     
/* 269 */     this.valueSetCapacity = valueSetCapacity;
/* 270 */     this.multimapHeaderEntry = ValueEntry.newHeader();
/* 271 */     succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   private static final long serialVersionUID = 1L;
/*     */ 
/*     */ 
/*     */   
/*     */   Set<V> createCollection() {
/* 283 */     return Platform.newLinkedHashSetWithExpectedSize(this.valueSetCapacity);
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
/*     */   Collection<V> createCollection(@ParametricNullness K key) {
/* 297 */     return new ValueSet(key, this.valueSetCapacity);
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
/*     */   public Set<V> replaceValues(@ParametricNullness K key, Iterable<? extends V> values) {
/* 310 */     return super.replaceValues(key, values);
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
/*     */   public Set<Map.Entry<K, V>> entries() {
/* 326 */     return super.entries();
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
/*     */   public Set<K> keySet() {
/* 341 */     return super.keySet();
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
/*     */   public Collection<V> values() {
/* 353 */     return super.values();
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   final class ValueSet
/*     */     extends Sets.ImprovedAbstractSet<V>
/*     */     implements ValueSetLink<K, V>
/*     */   {
/*     */     @ParametricNullness
/*     */     private final K key;
/*     */     @VisibleForTesting
/*     */     LinkedHashMultimap.ValueEntry<K, V>[] hashTable;
/* 366 */     private int size = 0;
/* 367 */     private int modCount = 0;
/*     */     
/*     */     private LinkedHashMultimap.ValueSetLink<K, V> firstEntry;
/*     */     
/*     */     private LinkedHashMultimap.ValueSetLink<K, V> lastEntry;
/*     */ 
/*     */     
/*     */     ValueSet(K key, int expectedValues) {
/* 375 */       this.key = key;
/* 376 */       this.firstEntry = this;
/* 377 */       this.lastEntry = this;
/*     */       
/* 379 */       int tableSize = Hashing.closedTableSize(expectedValues, 1.0D);
/*     */ 
/*     */ 
/*     */       
/* 383 */       LinkedHashMultimap.ValueEntry[] arrayOfValueEntry = new LinkedHashMultimap.ValueEntry[tableSize];
/* 384 */       this.hashTable = (LinkedHashMultimap.ValueEntry<K, V>[])arrayOfValueEntry;
/*     */     }
/*     */     
/*     */     private int mask() {
/* 388 */       return this.hashTable.length - 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public LinkedHashMultimap.ValueSetLink<K, V> getPredecessorInValueSet() {
/* 393 */       return this.lastEntry;
/*     */     }
/*     */ 
/*     */     
/*     */     public LinkedHashMultimap.ValueSetLink<K, V> getSuccessorInValueSet() {
/* 398 */       return this.firstEntry;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setPredecessorInValueSet(LinkedHashMultimap.ValueSetLink<K, V> entry) {
/* 403 */       this.lastEntry = entry;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setSuccessorInValueSet(LinkedHashMultimap.ValueSetLink<K, V> entry) {
/* 408 */       this.firstEntry = entry;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<V> iterator() {
/* 413 */       return new Iterator<V>() {
/* 414 */           LinkedHashMultimap.ValueSetLink<K, V> nextEntry = LinkedHashMultimap.ValueSet.this.firstEntry;
/*     */           
/* 416 */           int expectedModCount = LinkedHashMultimap.ValueSet.this.modCount; @CheckForNull
/*     */           LinkedHashMultimap.ValueEntry<K, V> toRemove;
/*     */           private void checkForComodification() {
/* 419 */             if (LinkedHashMultimap.ValueSet.this.modCount != this.expectedModCount) {
/* 420 */               throw new ConcurrentModificationException();
/*     */             }
/*     */           }
/*     */ 
/*     */           
/*     */           public boolean hasNext() {
/* 426 */             checkForComodification();
/* 427 */             return (this.nextEntry != LinkedHashMultimap.ValueSet.this);
/*     */           }
/*     */ 
/*     */           
/*     */           @ParametricNullness
/*     */           public V next() {
/* 433 */             if (!hasNext()) {
/* 434 */               throw new NoSuchElementException();
/*     */             }
/* 436 */             LinkedHashMultimap.ValueEntry<K, V> entry = (LinkedHashMultimap.ValueEntry<K, V>)this.nextEntry;
/* 437 */             V result = entry.getValue();
/* 438 */             this.toRemove = entry;
/* 439 */             this.nextEntry = entry.getSuccessorInValueSet();
/* 440 */             return result;
/*     */           }
/*     */ 
/*     */           
/*     */           public void remove() {
/* 445 */             checkForComodification();
/* 446 */             Preconditions.checkState((this.toRemove != null), "no calls to next() since the last call to remove()");
/* 447 */             LinkedHashMultimap.ValueSet.this.remove(this.toRemove.getValue());
/* 448 */             this.expectedModCount = LinkedHashMultimap.ValueSet.this.modCount;
/* 449 */             this.toRemove = null;
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public void forEach(Consumer<? super V> action) {
/* 456 */       Preconditions.checkNotNull(action);
/* 457 */       LinkedHashMultimap.ValueSetLink<K, V> entry = this.firstEntry;
/* 458 */       for (; entry != this; 
/* 459 */         entry = entry.getSuccessorInValueSet()) {
/* 460 */         action.accept((V)((LinkedHashMultimap.ValueEntry)entry).getValue());
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 466 */       return this.size;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(@CheckForNull Object o) {
/* 471 */       int smearedHash = Hashing.smearedHash(o);
/* 472 */       LinkedHashMultimap.ValueEntry<K, V> entry = this.hashTable[smearedHash & mask()];
/* 473 */       for (; entry != null; 
/* 474 */         entry = entry.nextInValueBucket) {
/* 475 */         if (entry.matchesValue(o, smearedHash)) {
/* 476 */           return true;
/*     */         }
/*     */       } 
/* 479 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean add(@ParametricNullness V value) {
/* 484 */       int smearedHash = Hashing.smearedHash(value);
/* 485 */       int bucket = smearedHash & mask();
/* 486 */       LinkedHashMultimap.ValueEntry<K, V> rowHead = this.hashTable[bucket];
/* 487 */       for (LinkedHashMultimap.ValueEntry<K, V> entry = rowHead; entry != null; entry = entry.nextInValueBucket) {
/* 488 */         if (entry.matchesValue(value, smearedHash)) {
/* 489 */           return false;
/*     */         }
/*     */       } 
/*     */       
/* 493 */       LinkedHashMultimap.ValueEntry<K, V> newEntry = new LinkedHashMultimap.ValueEntry<>(this.key, value, smearedHash, rowHead);
/* 494 */       LinkedHashMultimap.succeedsInValueSet(this.lastEntry, newEntry);
/* 495 */       LinkedHashMultimap.succeedsInValueSet(newEntry, this);
/* 496 */       LinkedHashMultimap.succeedsInMultimap(LinkedHashMultimap.this.multimapHeaderEntry.getPredecessorInMultimap(), newEntry);
/* 497 */       LinkedHashMultimap.succeedsInMultimap(newEntry, LinkedHashMultimap.this.multimapHeaderEntry);
/* 498 */       this.hashTable[bucket] = newEntry;
/* 499 */       this.size++;
/* 500 */       this.modCount++;
/* 501 */       rehashIfNecessary();
/* 502 */       return true;
/*     */     }
/*     */     
/*     */     private void rehashIfNecessary() {
/* 506 */       if (Hashing.needsResizing(this.size, this.hashTable.length, 1.0D)) {
/*     */         
/* 508 */         LinkedHashMultimap.ValueEntry[] arrayOfValueEntry = new LinkedHashMultimap.ValueEntry[this.hashTable.length * 2];
/* 509 */         this.hashTable = (LinkedHashMultimap.ValueEntry<K, V>[])arrayOfValueEntry;
/* 510 */         int mask = arrayOfValueEntry.length - 1;
/* 511 */         LinkedHashMultimap.ValueSetLink<K, V> entry = this.firstEntry;
/* 512 */         for (; entry != this; 
/* 513 */           entry = entry.getSuccessorInValueSet()) {
/* 514 */           LinkedHashMultimap.ValueEntry<K, V> valueEntry = (LinkedHashMultimap.ValueEntry<K, V>)entry;
/* 515 */           int bucket = valueEntry.smearedValueHash & mask;
/* 516 */           valueEntry.nextInValueBucket = arrayOfValueEntry[bucket];
/* 517 */           arrayOfValueEntry[bucket] = valueEntry;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public boolean remove(@CheckForNull Object o) {
/* 525 */       int smearedHash = Hashing.smearedHash(o);
/* 526 */       int bucket = smearedHash & mask();
/* 527 */       LinkedHashMultimap.ValueEntry<K, V> prev = null;
/* 528 */       LinkedHashMultimap.ValueEntry<K, V> entry = this.hashTable[bucket];
/* 529 */       for (; entry != null; 
/* 530 */         prev = entry, entry = entry.nextInValueBucket) {
/* 531 */         if (entry.matchesValue(o, smearedHash)) {
/* 532 */           if (prev == null) {
/*     */             
/* 534 */             this.hashTable[bucket] = entry.nextInValueBucket;
/*     */           } else {
/* 536 */             prev.nextInValueBucket = entry.nextInValueBucket;
/*     */           } 
/* 538 */           LinkedHashMultimap.deleteFromValueSet(entry);
/* 539 */           LinkedHashMultimap.deleteFromMultimap(entry);
/* 540 */           this.size--;
/* 541 */           this.modCount++;
/* 542 */           return true;
/*     */         } 
/*     */       } 
/* 545 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 550 */       Arrays.fill((Object[])this.hashTable, (Object)null);
/* 551 */       this.size = 0;
/* 552 */       LinkedHashMultimap.ValueSetLink<K, V> entry = this.firstEntry;
/* 553 */       for (; entry != this; 
/* 554 */         entry = entry.getSuccessorInValueSet()) {
/* 555 */         LinkedHashMultimap.ValueEntry<K, V> valueEntry = (LinkedHashMultimap.ValueEntry<K, V>)entry;
/* 556 */         LinkedHashMultimap.deleteFromMultimap(valueEntry);
/*     */       } 
/* 558 */       LinkedHashMultimap.succeedsInValueSet(this, this);
/* 559 */       this.modCount++;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<Map.Entry<K, V>> entryIterator() {
/* 565 */     return new Iterator<Map.Entry<K, V>>() {
/* 566 */         LinkedHashMultimap.ValueEntry<K, V> nextEntry = LinkedHashMultimap.this.multimapHeaderEntry.getSuccessorInMultimap();
/*     */         @CheckForNull
/*     */         LinkedHashMultimap.ValueEntry<K, V> toRemove;
/*     */         
/*     */         public boolean hasNext() {
/* 571 */           return (this.nextEntry != LinkedHashMultimap.this.multimapHeaderEntry);
/*     */         }
/*     */ 
/*     */         
/*     */         public Map.Entry<K, V> next() {
/* 576 */           if (!hasNext()) {
/* 577 */             throw new NoSuchElementException();
/*     */           }
/* 579 */           LinkedHashMultimap.ValueEntry<K, V> result = this.nextEntry;
/* 580 */           this.toRemove = result;
/* 581 */           this.nextEntry = this.nextEntry.getSuccessorInMultimap();
/* 582 */           return result;
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 587 */           Preconditions.checkState((this.toRemove != null), "no calls to next() since the last call to remove()");
/* 588 */           LinkedHashMultimap.this.remove(this.toRemove.getKey(), this.toRemove.getValue());
/* 589 */           this.toRemove = null;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   Spliterator<Map.Entry<K, V>> entrySpliterator() {
/* 596 */     return Spliterators.spliterator(entries(), 17);
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<V> valueIterator() {
/* 601 */     return Maps.valueIterator(entryIterator());
/*     */   }
/*     */ 
/*     */   
/*     */   Spliterator<V> valueSpliterator() {
/* 606 */     return CollectSpliterators.map(entrySpliterator(), Map.Entry::getValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 611 */     super.clear();
/* 612 */     succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 622 */     stream.defaultWriteObject();
/* 623 */     stream.writeInt(keySet().size());
/* 624 */     for (K key : keySet()) {
/* 625 */       stream.writeObject(key);
/*     */     }
/* 627 */     stream.writeInt(size());
/* 628 */     for (Map.Entry<K, V> entry : entries()) {
/* 629 */       stream.writeObject(entry.getKey());
/* 630 */       stream.writeObject(entry.getValue());
/*     */     } 
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 637 */     stream.defaultReadObject();
/* 638 */     this.multimapHeaderEntry = ValueEntry.newHeader();
/* 639 */     succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
/* 640 */     this.valueSetCapacity = 2;
/* 641 */     int distinctKeys = stream.readInt();
/* 642 */     Map<K, Collection<V>> map = Platform.newLinkedHashMapWithExpectedSize(12);
/* 643 */     for (int i = 0; i < distinctKeys; i++) {
/*     */       
/* 645 */       K key = (K)stream.readObject();
/* 646 */       map.put(key, createCollection(key));
/*     */     } 
/* 648 */     int entries = stream.readInt();
/* 649 */     for (int j = 0; j < entries; j++) {
/*     */       
/* 651 */       K key = (K)stream.readObject();
/*     */       
/* 653 */       V value = (V)stream.readObject();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 658 */       ((Collection<V>)Objects.<Collection<V>>requireNonNull(map.get(key))).add(value);
/*     */     } 
/* 660 */     setMap(map);
/*     */   }
/*     */   
/*     */   private static interface ValueSetLink<K, V> {
/*     */     ValueSetLink<K, V> getPredecessorInValueSet();
/*     */     
/*     */     ValueSetLink<K, V> getSuccessorInValueSet();
/*     */     
/*     */     void setPredecessorInValueSet(ValueSetLink<K, V> param1ValueSetLink);
/*     */     
/*     */     void setSuccessorInValueSet(ValueSetLink<K, V> param1ValueSetLink);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/LinkedHashMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */