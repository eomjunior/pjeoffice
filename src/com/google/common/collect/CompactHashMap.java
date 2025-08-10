/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.J2ktIncompatible;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.primitives.Ints;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*      */ import java.io.IOException;
/*      */ import java.io.InvalidObjectException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.AbstractMap;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.ConcurrentModificationException;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.Spliterator;
/*      */ import java.util.Spliterators;
/*      */ import java.util.function.BiConsumer;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.Consumer;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @ElementTypesAreNonnullByDefault
/*      */ @GwtIncompatible
/*      */ class CompactHashMap<K, V>
/*      */   extends AbstractMap<K, V>
/*      */   implements Serializable
/*      */ {
/*      */   public static <K, V> CompactHashMap<K, V> create() {
/*   99 */     return new CompactHashMap<>();
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
/*      */   public static <K, V> CompactHashMap<K, V> createWithExpectedSize(int expectedSize) {
/*  113 */     return new CompactHashMap<>(expectedSize);
/*      */   }
/*      */   
/*  116 */   private static final Object NOT_FOUND = new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   static final double HASH_FLOODING_FPP = 0.001D;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int MAX_HASH_BUCKET_LENGTH = 9;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   private transient Object table;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   @VisibleForTesting
/*      */   transient int[] entries;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   @VisibleForTesting
/*      */   transient Object[] keys;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   @VisibleForTesting
/*      */   transient Object[] values;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private transient int metadata;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private transient int size;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @LazyInit
/*      */   @CheckForNull
/*      */   private transient Set<K> keySetView;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @LazyInit
/*      */   @CheckForNull
/*      */   private transient Set<Map.Entry<K, V>> entrySetView;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @LazyInit
/*      */   @CheckForNull
/*      */   private transient Collection<V> valuesView;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   CompactHashMap() {
/*  241 */     init(3);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   CompactHashMap(int expectedSize) {
/*  250 */     init(expectedSize);
/*      */   }
/*      */ 
/*      */   
/*      */   void init(int expectedSize) {
/*  255 */     Preconditions.checkArgument((expectedSize >= 0), "Expected size must be >= 0");
/*      */ 
/*      */     
/*  258 */     this.metadata = Ints.constrainToRange(expectedSize, 1, 1073741823);
/*      */   }
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   boolean needsAllocArrays() {
/*  264 */     return (this.table == null);
/*      */   }
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   int allocArrays() {
/*  270 */     Preconditions.checkState(needsAllocArrays(), "Arrays already allocated");
/*      */     
/*  272 */     int expectedSize = this.metadata;
/*  273 */     int buckets = CompactHashing.tableSize(expectedSize);
/*  274 */     this.table = CompactHashing.createTable(buckets);
/*  275 */     setHashTableMask(buckets - 1);
/*      */     
/*  277 */     this.entries = new int[expectedSize];
/*  278 */     this.keys = new Object[expectedSize];
/*  279 */     this.values = new Object[expectedSize];
/*      */     
/*  281 */     return expectedSize;
/*      */   }
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   @VisibleForTesting
/*      */   Map<K, V> delegateOrNull() {
/*  288 */     if (this.table instanceof Map) {
/*  289 */       return (Map<K, V>)this.table;
/*      */     }
/*  291 */     return null;
/*      */   }
/*      */   
/*      */   Map<K, V> createHashFloodingResistantDelegate(int tableSize) {
/*  295 */     return new LinkedHashMap<>(tableSize, 1.0F);
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   @CanIgnoreReturnValue
/*      */   Map<K, V> convertToHashFloodingResistantImplementation() {
/*  301 */     Map<K, V> newDelegate = createHashFloodingResistantDelegate(hashTableMask() + 1);
/*  302 */     for (int i = firstEntryIndex(); i >= 0; i = getSuccessor(i)) {
/*  303 */       newDelegate.put(key(i), value(i));
/*      */     }
/*  305 */     this.table = newDelegate;
/*  306 */     this.entries = null;
/*  307 */     this.keys = null;
/*  308 */     this.values = null;
/*  309 */     incrementModCount();
/*  310 */     return newDelegate;
/*      */   }
/*      */ 
/*      */   
/*      */   private void setHashTableMask(int mask) {
/*  315 */     int hashTableBits = 32 - Integer.numberOfLeadingZeros(mask);
/*  316 */     this
/*  317 */       .metadata = CompactHashing.maskCombine(this.metadata, hashTableBits, 31);
/*      */   }
/*      */ 
/*      */   
/*      */   private int hashTableMask() {
/*  322 */     return (1 << (this.metadata & 0x1F)) - 1;
/*      */   }
/*      */   
/*      */   void incrementModCount() {
/*  326 */     this.metadata += 32;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void accessEntry(int index) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   @CanIgnoreReturnValue
/*      */   public V put(@ParametricNullness K key, @ParametricNullness V value) {
/*  341 */     if (needsAllocArrays()) {
/*  342 */       allocArrays();
/*      */     }
/*  344 */     Map<K, V> delegate = delegateOrNull();
/*  345 */     if (delegate != null) {
/*  346 */       return delegate.put(key, value);
/*      */     }
/*  348 */     int[] entries = requireEntries();
/*  349 */     Object[] keys = requireKeys();
/*  350 */     Object[] values = requireValues();
/*      */     
/*  352 */     int newEntryIndex = this.size;
/*  353 */     int newSize = newEntryIndex + 1;
/*  354 */     int hash = Hashing.smearedHash(key);
/*  355 */     int mask = hashTableMask();
/*  356 */     int tableIndex = hash & mask;
/*  357 */     int next = CompactHashing.tableGet(requireTable(), tableIndex);
/*  358 */     if (next == 0) {
/*  359 */       if (newSize > mask) {
/*      */         
/*  361 */         mask = resizeTable(mask, CompactHashing.newCapacity(mask), hash, newEntryIndex);
/*      */       } else {
/*  363 */         CompactHashing.tableSet(requireTable(), tableIndex, newEntryIndex + 1);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  368 */       int entryIndex, entry, hashPrefix = CompactHashing.getHashPrefix(hash, mask);
/*  369 */       int bucketLength = 0;
/*      */       do {
/*  371 */         entryIndex = next - 1;
/*  372 */         entry = entries[entryIndex];
/*  373 */         if (CompactHashing.getHashPrefix(entry, mask) == hashPrefix && 
/*  374 */           Objects.equal(key, keys[entryIndex])) {
/*      */           
/*  376 */           V oldValue = (V)values[entryIndex];
/*      */           
/*  378 */           values[entryIndex] = value;
/*  379 */           accessEntry(entryIndex);
/*  380 */           return oldValue;
/*      */         } 
/*  382 */         next = CompactHashing.getNext(entry, mask);
/*  383 */         bucketLength++;
/*  384 */       } while (next != 0);
/*      */       
/*  386 */       if (bucketLength >= 9) {
/*  387 */         return convertToHashFloodingResistantImplementation().put(key, value);
/*      */       }
/*      */       
/*  390 */       if (newSize > mask) {
/*      */         
/*  392 */         mask = resizeTable(mask, CompactHashing.newCapacity(mask), hash, newEntryIndex);
/*      */       } else {
/*  394 */         entries[entryIndex] = CompactHashing.maskCombine(entry, newEntryIndex + 1, mask);
/*      */       } 
/*      */     } 
/*  397 */     resizeMeMaybe(newSize);
/*  398 */     insertEntry(newEntryIndex, key, value, hash, mask);
/*  399 */     this.size = newSize;
/*  400 */     incrementModCount();
/*  401 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void insertEntry(int entryIndex, @ParametricNullness K key, @ParametricNullness V value, int hash, int mask) {
/*  409 */     setEntry(entryIndex, CompactHashing.maskCombine(hash, 0, mask));
/*  410 */     setKey(entryIndex, key);
/*  411 */     setValue(entryIndex, value);
/*      */   }
/*      */ 
/*      */   
/*      */   private void resizeMeMaybe(int newSize) {
/*  416 */     int entriesSize = (requireEntries()).length;
/*  417 */     if (newSize > entriesSize) {
/*      */ 
/*      */       
/*  420 */       int newCapacity = Math.min(1073741823, entriesSize + Math.max(1, entriesSize >>> 1) | 0x1);
/*  421 */       if (newCapacity != entriesSize) {
/*  422 */         resizeEntries(newCapacity);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void resizeEntries(int newCapacity) {
/*  432 */     this.entries = Arrays.copyOf(requireEntries(), newCapacity);
/*  433 */     this.keys = Arrays.copyOf(requireKeys(), newCapacity);
/*  434 */     this.values = Arrays.copyOf(requireValues(), newCapacity);
/*      */   }
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   private int resizeTable(int oldMask, int newCapacity, int targetHash, int targetEntryIndex) {
/*  439 */     Object newTable = CompactHashing.createTable(newCapacity);
/*  440 */     int newMask = newCapacity - 1;
/*      */     
/*  442 */     if (targetEntryIndex != 0)
/*      */     {
/*  444 */       CompactHashing.tableSet(newTable, targetHash & newMask, targetEntryIndex + 1);
/*      */     }
/*      */     
/*  447 */     Object oldTable = requireTable();
/*  448 */     int[] entries = requireEntries();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  455 */     for (int oldTableIndex = 0; oldTableIndex <= oldMask; oldTableIndex++) {
/*  456 */       int oldNext = CompactHashing.tableGet(oldTable, oldTableIndex);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  463 */       while (oldNext != 0) {
/*  464 */         int entryIndex = oldNext - 1;
/*  465 */         int oldEntry = entries[entryIndex];
/*      */ 
/*      */         
/*  468 */         int hash = CompactHashing.getHashPrefix(oldEntry, oldMask) | oldTableIndex;
/*      */         
/*  470 */         int newTableIndex = hash & newMask;
/*  471 */         int newNext = CompactHashing.tableGet(newTable, newTableIndex);
/*  472 */         CompactHashing.tableSet(newTable, newTableIndex, oldNext);
/*  473 */         entries[entryIndex] = CompactHashing.maskCombine(hash, newNext, newMask);
/*      */         
/*  475 */         oldNext = CompactHashing.getNext(oldEntry, oldMask);
/*      */       } 
/*      */     } 
/*      */     
/*  479 */     this.table = newTable;
/*  480 */     setHashTableMask(newMask);
/*  481 */     return newMask;
/*      */   }
/*      */   
/*      */   private int indexOf(@CheckForNull Object key) {
/*  485 */     if (needsAllocArrays()) {
/*  486 */       return -1;
/*      */     }
/*  488 */     int hash = Hashing.smearedHash(key);
/*  489 */     int mask = hashTableMask();
/*  490 */     int next = CompactHashing.tableGet(requireTable(), hash & mask);
/*  491 */     if (next == 0) {
/*  492 */       return -1;
/*      */     }
/*  494 */     int hashPrefix = CompactHashing.getHashPrefix(hash, mask);
/*      */     while (true) {
/*  496 */       int entryIndex = next - 1;
/*  497 */       int entry = entry(entryIndex);
/*  498 */       if (CompactHashing.getHashPrefix(entry, mask) == hashPrefix && 
/*  499 */         Objects.equal(key, key(entryIndex))) {
/*  500 */         return entryIndex;
/*      */       }
/*  502 */       next = CompactHashing.getNext(entry, mask);
/*  503 */       if (next == 0)
/*  504 */         return -1; 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsKey(@CheckForNull Object key) {
/*  509 */     Map<K, V> delegate = delegateOrNull();
/*  510 */     return (delegate != null) ? delegate.containsKey(key) : ((indexOf(key) != -1));
/*      */   }
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   public V get(@CheckForNull Object key) {
/*  516 */     Map<K, V> delegate = delegateOrNull();
/*  517 */     if (delegate != null) {
/*  518 */       return delegate.get(key);
/*      */     }
/*  520 */     int index = indexOf(key);
/*  521 */     if (index == -1) {
/*  522 */       return null;
/*      */     }
/*  524 */     accessEntry(index);
/*  525 */     return value(index);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   @CanIgnoreReturnValue
/*      */   public V remove(@CheckForNull Object key) {
/*  533 */     Map<K, V> delegate = delegateOrNull();
/*  534 */     if (delegate != null) {
/*  535 */       return delegate.remove(key);
/*      */     }
/*  537 */     Object oldValue = removeHelper(key);
/*  538 */     return (oldValue == NOT_FOUND) ? null : (V)oldValue;
/*      */   }
/*      */   
/*      */   private Object removeHelper(@CheckForNull Object key) {
/*  542 */     if (needsAllocArrays()) {
/*  543 */       return NOT_FOUND;
/*      */     }
/*  545 */     int mask = hashTableMask();
/*      */     
/*  547 */     int index = CompactHashing.remove(key, null, mask, 
/*      */ 
/*      */ 
/*      */         
/*  551 */         requireTable(), 
/*  552 */         requireEntries(), 
/*  553 */         requireKeys(), null);
/*      */     
/*  555 */     if (index == -1) {
/*  556 */       return NOT_FOUND;
/*      */     }
/*      */     
/*  559 */     Object oldValue = value(index);
/*      */     
/*  561 */     moveLastEntry(index, mask);
/*  562 */     this.size--;
/*  563 */     incrementModCount();
/*      */     
/*  565 */     return oldValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void moveLastEntry(int dstIndex, int mask) {
/*  572 */     Object table = requireTable();
/*  573 */     int[] entries = requireEntries();
/*  574 */     Object[] keys = requireKeys();
/*  575 */     Object[] values = requireValues();
/*  576 */     int srcIndex = size() - 1;
/*  577 */     if (dstIndex < srcIndex) {
/*      */       
/*  579 */       Object key = keys[srcIndex];
/*  580 */       keys[dstIndex] = key;
/*  581 */       values[dstIndex] = values[srcIndex];
/*  582 */       keys[srcIndex] = null;
/*  583 */       values[srcIndex] = null;
/*      */ 
/*      */       
/*  586 */       entries[dstIndex] = entries[srcIndex];
/*  587 */       entries[srcIndex] = 0;
/*      */ 
/*      */       
/*  590 */       int tableIndex = Hashing.smearedHash(key) & mask;
/*  591 */       int next = CompactHashing.tableGet(table, tableIndex);
/*  592 */       int srcNext = srcIndex + 1;
/*  593 */       if (next == srcNext) {
/*      */         
/*  595 */         CompactHashing.tableSet(table, tableIndex, dstIndex + 1);
/*      */       } else {
/*      */         int entryIndex, entry;
/*      */ 
/*      */         
/*      */         do {
/*  601 */           entryIndex = next - 1;
/*  602 */           entry = entries[entryIndex];
/*  603 */           next = CompactHashing.getNext(entry, mask);
/*  604 */         } while (next != srcNext);
/*      */         
/*  606 */         entries[entryIndex] = CompactHashing.maskCombine(entry, dstIndex + 1, mask);
/*      */       } 
/*      */     } else {
/*  609 */       keys[dstIndex] = null;
/*  610 */       values[dstIndex] = null;
/*  611 */       entries[dstIndex] = 0;
/*      */     } 
/*      */   }
/*      */   
/*      */   int firstEntryIndex() {
/*  616 */     return isEmpty() ? -1 : 0;
/*      */   }
/*      */   
/*      */   int getSuccessor(int entryIndex) {
/*  620 */     return (entryIndex + 1 < this.size) ? (entryIndex + 1) : -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   int adjustAfterRemove(int indexBeforeRemove, int indexRemoved) {
/*  629 */     return indexBeforeRemove - 1;
/*      */   }
/*      */   
/*      */   private abstract class Itr<T> implements Iterator<T> {
/*  633 */     int expectedMetadata = CompactHashMap.this.metadata;
/*  634 */     int currentIndex = CompactHashMap.this.firstEntryIndex();
/*  635 */     int indexToRemove = -1;
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/*  639 */       return (this.currentIndex >= 0);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public T next() {
/*  648 */       checkForConcurrentModification();
/*  649 */       if (!hasNext()) {
/*  650 */         throw new NoSuchElementException();
/*      */       }
/*  652 */       this.indexToRemove = this.currentIndex;
/*  653 */       T result = getOutput(this.currentIndex);
/*  654 */       this.currentIndex = CompactHashMap.this.getSuccessor(this.currentIndex);
/*  655 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/*  660 */       checkForConcurrentModification();
/*  661 */       CollectPreconditions.checkRemove((this.indexToRemove >= 0));
/*  662 */       incrementExpectedModCount();
/*  663 */       CompactHashMap.this.remove(CompactHashMap.this.key(this.indexToRemove));
/*  664 */       this.currentIndex = CompactHashMap.this.adjustAfterRemove(this.currentIndex, this.indexToRemove);
/*  665 */       this.indexToRemove = -1;
/*      */     }
/*      */     
/*      */     void incrementExpectedModCount() {
/*  669 */       this.expectedMetadata += 32;
/*      */     }
/*      */     private Itr() {}
/*      */     private void checkForConcurrentModification() {
/*  673 */       if (CompactHashMap.this.metadata != this.expectedMetadata)
/*  674 */         throw new ConcurrentModificationException(); 
/*      */     }
/*      */     
/*      */     @ParametricNullness
/*      */     abstract T getOutput(int param1Int); }
/*      */   
/*      */   public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
/*  681 */     Preconditions.checkNotNull(function);
/*  682 */     Map<K, V> delegate = delegateOrNull();
/*  683 */     if (delegate != null) {
/*  684 */       delegate.replaceAll(function);
/*      */     } else {
/*  686 */       for (int i = 0; i < this.size; i++) {
/*  687 */         setValue(i, function.apply(key(i), value(i)));
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<K> keySet() {
/*  696 */     return (this.keySetView == null) ? (this.keySetView = createKeySet()) : this.keySetView;
/*      */   }
/*      */   
/*      */   Set<K> createKeySet() {
/*  700 */     return new KeySetView();
/*      */   }
/*      */   
/*      */   class KeySetView
/*      */     extends Maps.KeySet<K, V> {
/*      */     KeySetView() {
/*  706 */       super(CompactHashMap.this);
/*      */     }
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/*  711 */       if (CompactHashMap.this.needsAllocArrays()) {
/*  712 */         return new Object[0];
/*      */       }
/*  714 */       Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
/*  715 */       return (delegate != null) ? 
/*  716 */         delegate.keySet().toArray() : 
/*  717 */         ObjectArrays.copyAsObjectArray(CompactHashMap.this.requireKeys(), 0, CompactHashMap.this.size);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public <T> T[] toArray(T[] a) {
/*  723 */       if (CompactHashMap.this.needsAllocArrays()) {
/*  724 */         if (a.length > 0) {
/*  725 */           T[] arrayOfT = a;
/*  726 */           arrayOfT[0] = null;
/*      */         } 
/*  728 */         return a;
/*      */       } 
/*  730 */       Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
/*  731 */       return (delegate != null) ? 
/*  732 */         (T[])delegate.keySet().toArray((Object[])a) : 
/*  733 */         ObjectArrays.<T>toArrayImpl(CompactHashMap.this.requireKeys(), 0, CompactHashMap.this.size, a);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(@CheckForNull Object o) {
/*  738 */       Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
/*  739 */       return (delegate != null) ? 
/*  740 */         delegate.keySet().remove(o) : (
/*  741 */         (CompactHashMap.this.removeHelper(o) != CompactHashMap.NOT_FOUND));
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<K> iterator() {
/*  746 */       return CompactHashMap.this.keySetIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public Spliterator<K> spliterator() {
/*  751 */       if (CompactHashMap.this.needsAllocArrays()) {
/*  752 */         return Spliterators.spliterator(new Object[0], 17);
/*      */       }
/*  754 */       Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
/*  755 */       return (delegate != null) ? 
/*  756 */         delegate.keySet().spliterator() : 
/*  757 */         Spliterators.<K>spliterator(CompactHashMap.this
/*  758 */           .requireKeys(), 0, CompactHashMap.this.size, 17);
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super K> action) {
/*  763 */       Preconditions.checkNotNull(action);
/*  764 */       Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
/*  765 */       if (delegate != null) {
/*  766 */         delegate.keySet().forEach(action);
/*      */       } else {
/*  768 */         for (int i = CompactHashMap.this.firstEntryIndex(); i >= 0; i = CompactHashMap.this.getSuccessor(i)) {
/*  769 */           action.accept(CompactHashMap.this.key(i));
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   Iterator<K> keySetIterator() {
/*  776 */     Map<K, V> delegate = delegateOrNull();
/*  777 */     if (delegate != null) {
/*  778 */       return delegate.keySet().iterator();
/*      */     }
/*  780 */     return new Itr<K>()
/*      */       {
/*      */         @ParametricNullness
/*      */         K getOutput(int entry) {
/*  784 */           return CompactHashMap.this.key(entry);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   public void forEach(BiConsumer<? super K, ? super V> action) {
/*  791 */     Preconditions.checkNotNull(action);
/*  792 */     Map<K, V> delegate = delegateOrNull();
/*  793 */     if (delegate != null) {
/*  794 */       delegate.forEach(action);
/*      */     } else {
/*  796 */       for (int i = firstEntryIndex(); i >= 0; i = getSuccessor(i)) {
/*  797 */         action.accept(key(i), value(i));
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<Map.Entry<K, V>> entrySet() {
/*  806 */     return (this.entrySetView == null) ? (this.entrySetView = createEntrySet()) : this.entrySetView;
/*      */   }
/*      */   
/*      */   Set<Map.Entry<K, V>> createEntrySet() {
/*  810 */     return new EntrySetView();
/*      */   }
/*      */   
/*      */   class EntrySetView
/*      */     extends Maps.EntrySet<K, V>
/*      */   {
/*      */     Map<K, V> map() {
/*  817 */       return CompactHashMap.this;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<Map.Entry<K, V>> iterator() {
/*  822 */       return CompactHashMap.this.entrySetIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public Spliterator<Map.Entry<K, V>> spliterator() {
/*  827 */       Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
/*  828 */       return (delegate != null) ? 
/*  829 */         delegate.entrySet().spliterator() : 
/*  830 */         CollectSpliterators.<Map.Entry<K, V>>indexed(CompactHashMap.this
/*  831 */           .size, 17, x$0 -> new CompactHashMap.MapEntry(x$0));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(@CheckForNull Object o) {
/*  836 */       Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
/*  837 */       if (delegate != null)
/*  838 */         return delegate.entrySet().contains(o); 
/*  839 */       if (o instanceof Map.Entry) {
/*  840 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/*  841 */         int index = CompactHashMap.this.indexOf(entry.getKey());
/*  842 */         return (index != -1 && Objects.equal(CompactHashMap.this.value(index), entry.getValue()));
/*      */       } 
/*  844 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(@CheckForNull Object o) {
/*  849 */       Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
/*  850 */       if (delegate != null)
/*  851 */         return delegate.entrySet().remove(o); 
/*  852 */       if (o instanceof Map.Entry) {
/*  853 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/*  854 */         if (CompactHashMap.this.needsAllocArrays()) {
/*  855 */           return false;
/*      */         }
/*  857 */         int mask = CompactHashMap.this.hashTableMask();
/*      */         
/*  859 */         int index = CompactHashing.remove(entry
/*  860 */             .getKey(), entry
/*  861 */             .getValue(), mask, CompactHashMap.this
/*      */             
/*  863 */             .requireTable(), CompactHashMap.this
/*  864 */             .requireEntries(), CompactHashMap.this
/*  865 */             .requireKeys(), CompactHashMap.this
/*  866 */             .requireValues());
/*  867 */         if (index == -1) {
/*  868 */           return false;
/*      */         }
/*      */         
/*  871 */         CompactHashMap.this.moveLastEntry(index, mask);
/*  872 */         CompactHashMap.this.size--;
/*  873 */         CompactHashMap.this.incrementModCount();
/*      */         
/*  875 */         return true;
/*      */       } 
/*  877 */       return false;
/*      */     }
/*      */   }
/*      */   
/*      */   Iterator<Map.Entry<K, V>> entrySetIterator() {
/*  882 */     Map<K, V> delegate = delegateOrNull();
/*  883 */     if (delegate != null) {
/*  884 */       return delegate.entrySet().iterator();
/*      */     }
/*  886 */     return new Itr<Map.Entry<K, V>>()
/*      */       {
/*      */         Map.Entry<K, V> getOutput(int entry) {
/*  889 */           return new CompactHashMap.MapEntry(entry);
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   final class MapEntry extends AbstractMapEntry<K, V> {
/*      */     @ParametricNullness
/*      */     private final K key;
/*      */     private int lastKnownIndex;
/*      */     
/*      */     MapEntry(int index) {
/*  900 */       this.key = CompactHashMap.this.key(index);
/*  901 */       this.lastKnownIndex = index;
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public K getKey() {
/*  907 */       return this.key;
/*      */     }
/*      */     
/*      */     private void updateLastKnownIndex() {
/*  911 */       if (this.lastKnownIndex == -1 || this.lastKnownIndex >= CompactHashMap.this
/*  912 */         .size() || 
/*  913 */         !Objects.equal(this.key, CompactHashMap.this.key(this.lastKnownIndex))) {
/*  914 */         this.lastKnownIndex = CompactHashMap.this.indexOf(this.key);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public V getValue() {
/*  921 */       Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
/*  922 */       if (delegate != null)
/*      */       {
/*      */ 
/*      */ 
/*      */         
/*  927 */         return NullnessCasts.uncheckedCastNullableTToT(delegate.get(this.key));
/*      */       }
/*  929 */       updateLastKnownIndex();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  938 */       return (this.lastKnownIndex == -1) ? NullnessCasts.<V>unsafeNull() : CompactHashMap.this.value(this.lastKnownIndex);
/*      */     }
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     public V setValue(@ParametricNullness V value) {
/*  944 */       Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
/*  945 */       if (delegate != null) {
/*  946 */         return NullnessCasts.uncheckedCastNullableTToT(delegate.put(this.key, value));
/*      */       }
/*  948 */       updateLastKnownIndex();
/*  949 */       if (this.lastKnownIndex == -1) {
/*  950 */         CompactHashMap.this.put(this.key, value);
/*  951 */         return NullnessCasts.unsafeNull();
/*      */       } 
/*  953 */       V old = CompactHashMap.this.value(this.lastKnownIndex);
/*  954 */       CompactHashMap.this.setValue(this.lastKnownIndex, value);
/*  955 */       return old;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int size() {
/*  962 */     Map<K, V> delegate = delegateOrNull();
/*  963 */     return (delegate != null) ? delegate.size() : this.size;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isEmpty() {
/*  968 */     return (size() == 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsValue(@CheckForNull Object value) {
/*  973 */     Map<K, V> delegate = delegateOrNull();
/*  974 */     if (delegate != null) {
/*  975 */       return delegate.containsValue(value);
/*      */     }
/*  977 */     for (int i = 0; i < this.size; i++) {
/*  978 */       if (Objects.equal(value, value(i))) {
/*  979 */         return true;
/*      */       }
/*      */     } 
/*  982 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<V> values() {
/*  989 */     return (this.valuesView == null) ? (this.valuesView = createValues()) : this.valuesView;
/*      */   }
/*      */   
/*      */   Collection<V> createValues() {
/*  993 */     return new ValuesView();
/*      */   }
/*      */   
/*      */   class ValuesView
/*      */     extends Maps.Values<K, V> {
/*      */     ValuesView() {
/*  999 */       super(CompactHashMap.this);
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<V> iterator() {
/* 1004 */       return CompactHashMap.this.valuesIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super V> action) {
/* 1009 */       Preconditions.checkNotNull(action);
/* 1010 */       Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
/* 1011 */       if (delegate != null) {
/* 1012 */         delegate.values().forEach(action);
/*      */       } else {
/* 1014 */         for (int i = CompactHashMap.this.firstEntryIndex(); i >= 0; i = CompactHashMap.this.getSuccessor(i)) {
/* 1015 */           action.accept(CompactHashMap.this.value(i));
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Spliterator<V> spliterator() {
/* 1022 */       if (CompactHashMap.this.needsAllocArrays()) {
/* 1023 */         return Spliterators.spliterator(new Object[0], 16);
/*      */       }
/* 1025 */       Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
/* 1026 */       return (delegate != null) ? 
/* 1027 */         delegate.values().spliterator() : 
/* 1028 */         Spliterators.<V>spliterator(CompactHashMap.this.requireValues(), 0, CompactHashMap.this.size, 16);
/*      */     }
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/* 1033 */       if (CompactHashMap.this.needsAllocArrays()) {
/* 1034 */         return new Object[0];
/*      */       }
/* 1036 */       Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
/* 1037 */       return (delegate != null) ? 
/* 1038 */         delegate.values().toArray() : 
/* 1039 */         ObjectArrays.copyAsObjectArray(CompactHashMap.this.requireValues(), 0, CompactHashMap.this.size);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public <T> T[] toArray(T[] a) {
/* 1045 */       if (CompactHashMap.this.needsAllocArrays()) {
/* 1046 */         if (a.length > 0) {
/* 1047 */           T[] arrayOfT = a;
/* 1048 */           arrayOfT[0] = null;
/*      */         } 
/* 1050 */         return a;
/*      */       } 
/* 1052 */       Map<K, V> delegate = CompactHashMap.this.delegateOrNull();
/* 1053 */       return (delegate != null) ? 
/* 1054 */         (T[])delegate.values().toArray((Object[])a) : 
/* 1055 */         ObjectArrays.<T>toArrayImpl(CompactHashMap.this.requireValues(), 0, CompactHashMap.this.size, a);
/*      */     }
/*      */   }
/*      */   
/*      */   Iterator<V> valuesIterator() {
/* 1060 */     Map<K, V> delegate = delegateOrNull();
/* 1061 */     if (delegate != null) {
/* 1062 */       return delegate.values().iterator();
/*      */     }
/* 1064 */     return new Itr<V>()
/*      */       {
/*      */         @ParametricNullness
/*      */         V getOutput(int entry) {
/* 1068 */           return CompactHashMap.this.value(entry);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void trimToSize() {
/* 1078 */     if (needsAllocArrays()) {
/*      */       return;
/*      */     }
/* 1081 */     Map<K, V> delegate = delegateOrNull();
/* 1082 */     if (delegate != null) {
/* 1083 */       Map<K, V> newDelegate = createHashFloodingResistantDelegate(size());
/* 1084 */       newDelegate.putAll(delegate);
/* 1085 */       this.table = newDelegate;
/*      */       return;
/*      */     } 
/* 1088 */     int size = this.size;
/* 1089 */     if (size < (requireEntries()).length) {
/* 1090 */       resizeEntries(size);
/*      */     }
/* 1092 */     int minimumTableSize = CompactHashing.tableSize(size);
/* 1093 */     int mask = hashTableMask();
/* 1094 */     if (minimumTableSize < mask) {
/* 1095 */       resizeTable(mask, minimumTableSize, 0, 0);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void clear() {
/* 1101 */     if (needsAllocArrays()) {
/*      */       return;
/*      */     }
/* 1104 */     incrementModCount();
/* 1105 */     Map<K, V> delegate = delegateOrNull();
/* 1106 */     if (delegate != null) {
/* 1107 */       this
/* 1108 */         .metadata = Ints.constrainToRange(size(), 3, 1073741823);
/* 1109 */       delegate.clear();
/* 1110 */       this.table = null;
/* 1111 */       this.size = 0;
/*      */     } else {
/* 1113 */       Arrays.fill(requireKeys(), 0, this.size, (Object)null);
/* 1114 */       Arrays.fill(requireValues(), 0, this.size, (Object)null);
/* 1115 */       CompactHashing.tableClear(requireTable());
/* 1116 */       Arrays.fill(requireEntries(), 0, this.size, 0);
/* 1117 */       this.size = 0;
/*      */     } 
/*      */   }
/*      */   
/*      */   @J2ktIncompatible
/*      */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 1123 */     stream.defaultWriteObject();
/* 1124 */     stream.writeInt(size());
/* 1125 */     Iterator<Map.Entry<K, V>> entryIterator = entrySetIterator();
/* 1126 */     while (entryIterator.hasNext()) {
/* 1127 */       Map.Entry<K, V> e = entryIterator.next();
/* 1128 */       stream.writeObject(e.getKey());
/* 1129 */       stream.writeObject(e.getValue());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   @J2ktIncompatible
/*      */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 1136 */     stream.defaultReadObject();
/* 1137 */     int elementCount = stream.readInt();
/* 1138 */     if (elementCount < 0) {
/* 1139 */       throw new InvalidObjectException("Invalid size: " + elementCount);
/*      */     }
/* 1141 */     init(elementCount);
/* 1142 */     for (int i = 0; i < elementCount; i++) {
/* 1143 */       K key = (K)stream.readObject();
/* 1144 */       V value = (V)stream.readObject();
/* 1145 */       put(key, value);
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
/*      */   private Object requireTable() {
/* 1162 */     return Objects.requireNonNull(this.table);
/*      */   }
/*      */   
/*      */   private int[] requireEntries() {
/* 1166 */     return Objects.<int[]>requireNonNull(this.entries);
/*      */   }
/*      */   
/*      */   private Object[] requireKeys() {
/* 1170 */     return Objects.<Object[]>requireNonNull(this.keys);
/*      */   }
/*      */   
/*      */   private Object[] requireValues() {
/* 1174 */     return Objects.<Object[]>requireNonNull(this.values);
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
/*      */   private K key(int i) {
/* 1187 */     return (K)requireKeys()[i];
/*      */   }
/*      */ 
/*      */   
/*      */   private V value(int i) {
/* 1192 */     return (V)requireValues()[i];
/*      */   }
/*      */   
/*      */   private int entry(int i) {
/* 1196 */     return requireEntries()[i];
/*      */   }
/*      */   
/*      */   private void setKey(int i, K key) {
/* 1200 */     requireKeys()[i] = key;
/*      */   }
/*      */   
/*      */   private void setValue(int i, V value) {
/* 1204 */     requireValues()[i] = value;
/*      */   }
/*      */   
/*      */   private void setEntry(int i, int value) {
/* 1208 */     requireEntries()[i] = value;
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/CompactHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */