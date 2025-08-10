/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import com.google.j2objc.annotations.RetainedWith;
/*     */ import com.google.j2objc.annotations.Weak;
/*     */ import java.io.IOException;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiFunction;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class HashBiMap<K, V>
/*     */   extends Maps.IteratorBasedAbstractMap<K, V>
/*     */   implements BiMap<K, V>, Serializable
/*     */ {
/*     */   private static final double LOAD_FACTOR = 1.0D;
/*     */   private transient BiEntry<K, V>[] hashTableKToV;
/*     */   private transient BiEntry<K, V>[] hashTableVToK;
/*     */   @CheckForNull
/*     */   @Weak
/*     */   private transient BiEntry<K, V> firstInKeyInsertionOrder;
/*     */   @CheckForNull
/*     */   @Weak
/*     */   private transient BiEntry<K, V> lastInKeyInsertionOrder;
/*     */   private transient int size;
/*     */   private transient int mask;
/*     */   private transient int modCount;
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   @RetainedWith
/*     */   private transient BiMap<V, K> inverse;
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <K, V> HashBiMap<K, V> create() {
/*  68 */     return create(16);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> HashBiMap<K, V> create(int expectedSize) {
/*  79 */     return new HashBiMap<>(expectedSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> HashBiMap<K, V> create(Map<? extends K, ? extends V> map) {
/*  88 */     HashBiMap<K, V> bimap = create(map.size());
/*  89 */     bimap.putAll(map);
/*  90 */     return bimap;
/*     */   }
/*     */   
/*     */   static final class BiEntry<K, V>
/*     */     extends ImmutableEntry<K, V>
/*     */   {
/*     */     final int keyHash;
/*     */     final int valueHash;
/*     */     @CheckForNull
/*     */     BiEntry<K, V> nextInKToVBucket;
/*     */     @CheckForNull
/*     */     @Weak
/*     */     BiEntry<K, V> nextInVToKBucket;
/*     */     @CheckForNull
/*     */     @Weak
/*     */     BiEntry<K, V> nextInKeyInsertionOrder;
/*     */     @CheckForNull
/*     */     @Weak
/*     */     BiEntry<K, V> prevInKeyInsertionOrder;
/*     */     
/*     */     BiEntry(@ParametricNullness K key, int keyHash, @ParametricNullness V value, int valueHash) {
/* 111 */       super(key, value);
/* 112 */       this.keyHash = keyHash;
/* 113 */       this.valueHash = valueHash;
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
/*     */   private HashBiMap(int expectedSize) {
/* 137 */     init(expectedSize);
/*     */   }
/*     */   
/*     */   private void init(int expectedSize) {
/* 141 */     CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
/* 142 */     int tableSize = Hashing.closedTableSize(expectedSize, 1.0D);
/* 143 */     this.hashTableKToV = createTable(tableSize);
/* 144 */     this.hashTableVToK = createTable(tableSize);
/* 145 */     this.firstInKeyInsertionOrder = null;
/* 146 */     this.lastInKeyInsertionOrder = null;
/* 147 */     this.size = 0;
/* 148 */     this.mask = tableSize - 1;
/* 149 */     this.modCount = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void delete(BiEntry<K, V> entry) {
/* 157 */     int keyBucket = entry.keyHash & this.mask;
/* 158 */     BiEntry<K, V> prevBucketEntry = null;
/* 159 */     BiEntry<K, V> bucketEntry = this.hashTableKToV[keyBucket];
/*     */     
/* 161 */     for (;; bucketEntry = bucketEntry.nextInKToVBucket) {
/* 162 */       if (bucketEntry == entry) {
/* 163 */         if (prevBucketEntry == null) {
/* 164 */           this.hashTableKToV[keyBucket] = entry.nextInKToVBucket; break;
/*     */         } 
/* 166 */         prevBucketEntry.nextInKToVBucket = entry.nextInKToVBucket;
/*     */         
/*     */         break;
/*     */       } 
/* 170 */       prevBucketEntry = bucketEntry;
/*     */     } 
/*     */     
/* 173 */     int valueBucket = entry.valueHash & this.mask;
/* 174 */     prevBucketEntry = null;
/* 175 */     BiEntry<K, V> biEntry1 = this.hashTableVToK[valueBucket];
/*     */     
/* 177 */     for (;; biEntry1 = biEntry1.nextInVToKBucket) {
/* 178 */       if (biEntry1 == entry) {
/* 179 */         if (prevBucketEntry == null) {
/* 180 */           this.hashTableVToK[valueBucket] = entry.nextInVToKBucket; break;
/*     */         } 
/* 182 */         prevBucketEntry.nextInVToKBucket = entry.nextInVToKBucket;
/*     */         
/*     */         break;
/*     */       } 
/* 186 */       prevBucketEntry = biEntry1;
/*     */     } 
/*     */     
/* 189 */     if (entry.prevInKeyInsertionOrder == null) {
/* 190 */       this.firstInKeyInsertionOrder = entry.nextInKeyInsertionOrder;
/*     */     } else {
/* 192 */       entry.prevInKeyInsertionOrder.nextInKeyInsertionOrder = entry.nextInKeyInsertionOrder;
/*     */     } 
/*     */     
/* 195 */     if (entry.nextInKeyInsertionOrder == null) {
/* 196 */       this.lastInKeyInsertionOrder = entry.prevInKeyInsertionOrder;
/*     */     } else {
/* 198 */       entry.nextInKeyInsertionOrder.prevInKeyInsertionOrder = entry.prevInKeyInsertionOrder;
/*     */     } 
/*     */     
/* 201 */     this.size--;
/* 202 */     this.modCount++;
/*     */   }
/*     */   
/*     */   private void insert(BiEntry<K, V> entry, @CheckForNull BiEntry<K, V> oldEntryForKey) {
/* 206 */     int keyBucket = entry.keyHash & this.mask;
/* 207 */     entry.nextInKToVBucket = this.hashTableKToV[keyBucket];
/* 208 */     this.hashTableKToV[keyBucket] = entry;
/*     */     
/* 210 */     int valueBucket = entry.valueHash & this.mask;
/* 211 */     entry.nextInVToKBucket = this.hashTableVToK[valueBucket];
/* 212 */     this.hashTableVToK[valueBucket] = entry;
/*     */     
/* 214 */     if (oldEntryForKey == null) {
/* 215 */       entry.prevInKeyInsertionOrder = this.lastInKeyInsertionOrder;
/* 216 */       entry.nextInKeyInsertionOrder = null;
/* 217 */       if (this.lastInKeyInsertionOrder == null) {
/* 218 */         this.firstInKeyInsertionOrder = entry;
/*     */       } else {
/* 220 */         this.lastInKeyInsertionOrder.nextInKeyInsertionOrder = entry;
/*     */       } 
/* 222 */       this.lastInKeyInsertionOrder = entry;
/*     */     } else {
/* 224 */       entry.prevInKeyInsertionOrder = oldEntryForKey.prevInKeyInsertionOrder;
/* 225 */       if (entry.prevInKeyInsertionOrder == null) {
/* 226 */         this.firstInKeyInsertionOrder = entry;
/*     */       } else {
/* 228 */         entry.prevInKeyInsertionOrder.nextInKeyInsertionOrder = entry;
/*     */       } 
/* 230 */       entry.nextInKeyInsertionOrder = oldEntryForKey.nextInKeyInsertionOrder;
/* 231 */       if (entry.nextInKeyInsertionOrder == null) {
/* 232 */         this.lastInKeyInsertionOrder = entry;
/*     */       } else {
/* 234 */         entry.nextInKeyInsertionOrder.prevInKeyInsertionOrder = entry;
/*     */       } 
/*     */     } 
/*     */     
/* 238 */     this.size++;
/* 239 */     this.modCount++;
/*     */   }
/*     */   
/*     */   @CheckForNull
/*     */   private BiEntry<K, V> seekByKey(@CheckForNull Object key, int keyHash) {
/* 244 */     BiEntry<K, V> entry = this.hashTableKToV[keyHash & this.mask];
/* 245 */     for (; entry != null; 
/* 246 */       entry = entry.nextInKToVBucket) {
/* 247 */       if (keyHash == entry.keyHash && Objects.equal(key, entry.key)) {
/* 248 */         return entry;
/*     */       }
/*     */     } 
/* 251 */     return null;
/*     */   }
/*     */   
/*     */   @CheckForNull
/*     */   private BiEntry<K, V> seekByValue(@CheckForNull Object value, int valueHash) {
/* 256 */     BiEntry<K, V> entry = this.hashTableVToK[valueHash & this.mask];
/* 257 */     for (; entry != null; 
/* 258 */       entry = entry.nextInVToKBucket) {
/* 259 */       if (valueHash == entry.valueHash && Objects.equal(value, entry.value)) {
/* 260 */         return entry;
/*     */       }
/*     */     } 
/* 263 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(@CheckForNull Object key) {
/* 268 */     return (seekByKey(key, Hashing.smearedHash(key)) != null);
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
/*     */   public boolean containsValue(@CheckForNull Object value) {
/* 283 */     return (seekByValue(value, Hashing.smearedHash(value)) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public V get(@CheckForNull Object key) {
/* 289 */     return Maps.valueOrNull(seekByKey(key, Hashing.smearedHash(key)));
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   public V put(@ParametricNullness K key, @ParametricNullness V value) {
/* 296 */     return put(key, value, false);
/*     */   }
/*     */   
/*     */   @CheckForNull
/*     */   private V put(@ParametricNullness K key, @ParametricNullness V value, boolean force) {
/* 301 */     int keyHash = Hashing.smearedHash(key);
/* 302 */     int valueHash = Hashing.smearedHash(value);
/*     */     
/* 304 */     BiEntry<K, V> oldEntryForKey = seekByKey(key, keyHash);
/* 305 */     if (oldEntryForKey != null && valueHash == oldEntryForKey.valueHash && 
/*     */       
/* 307 */       Objects.equal(value, oldEntryForKey.value)) {
/* 308 */       return value;
/*     */     }
/*     */     
/* 311 */     BiEntry<K, V> oldEntryForValue = seekByValue(value, valueHash);
/* 312 */     if (oldEntryForValue != null) {
/* 313 */       if (force) {
/* 314 */         delete(oldEntryForValue);
/*     */       } else {
/* 316 */         throw new IllegalArgumentException("value already present: " + value);
/*     */       } 
/*     */     }
/*     */     
/* 320 */     BiEntry<K, V> newEntry = new BiEntry<>(key, keyHash, value, valueHash);
/* 321 */     if (oldEntryForKey != null) {
/* 322 */       delete(oldEntryForKey);
/* 323 */       insert(newEntry, oldEntryForKey);
/* 324 */       oldEntryForKey.prevInKeyInsertionOrder = null;
/* 325 */       oldEntryForKey.nextInKeyInsertionOrder = null;
/* 326 */       return oldEntryForKey.value;
/*     */     } 
/* 328 */     insert(newEntry, (BiEntry<K, V>)null);
/* 329 */     rehashIfNecessary();
/* 330 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   public V forcePut(@ParametricNullness K key, @ParametricNullness V value) {
/* 338 */     return put(key, value, true);
/*     */   }
/*     */   
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   private K putInverse(@ParametricNullness V value, @ParametricNullness K key, boolean force) {
/* 344 */     int valueHash = Hashing.smearedHash(value);
/* 345 */     int keyHash = Hashing.smearedHash(key);
/*     */     
/* 347 */     BiEntry<K, V> oldEntryForValue = seekByValue(value, valueHash);
/* 348 */     BiEntry<K, V> oldEntryForKey = seekByKey(key, keyHash);
/* 349 */     if (oldEntryForValue != null && keyHash == oldEntryForValue.keyHash && 
/*     */       
/* 351 */       Objects.equal(key, oldEntryForValue.key))
/* 352 */       return key; 
/* 353 */     if (oldEntryForKey != null && !force) {
/* 354 */       throw new IllegalArgumentException("key already present: " + key);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 364 */     if (oldEntryForValue != null) {
/* 365 */       delete(oldEntryForValue);
/*     */     }
/*     */     
/* 368 */     if (oldEntryForKey != null) {
/* 369 */       delete(oldEntryForKey);
/*     */     }
/*     */     
/* 372 */     BiEntry<K, V> newEntry = new BiEntry<>(key, keyHash, value, valueHash);
/* 373 */     insert(newEntry, oldEntryForKey);
/*     */     
/* 375 */     if (oldEntryForKey != null) {
/* 376 */       oldEntryForKey.prevInKeyInsertionOrder = null;
/* 377 */       oldEntryForKey.nextInKeyInsertionOrder = null;
/*     */     } 
/* 379 */     if (oldEntryForValue != null) {
/* 380 */       oldEntryForValue.prevInKeyInsertionOrder = null;
/* 381 */       oldEntryForValue.nextInKeyInsertionOrder = null;
/*     */     } 
/* 383 */     rehashIfNecessary();
/* 384 */     return Maps.keyOrNull(oldEntryForValue);
/*     */   }
/*     */   
/*     */   private void rehashIfNecessary() {
/* 388 */     BiEntry<K, V>[] oldKToV = this.hashTableKToV;
/* 389 */     if (Hashing.needsResizing(this.size, oldKToV.length, 1.0D)) {
/* 390 */       int newTableSize = oldKToV.length * 2;
/*     */       
/* 392 */       this.hashTableKToV = createTable(newTableSize);
/* 393 */       this.hashTableVToK = createTable(newTableSize);
/* 394 */       this.mask = newTableSize - 1;
/* 395 */       this.size = 0;
/*     */       
/* 397 */       BiEntry<K, V> entry = this.firstInKeyInsertionOrder;
/* 398 */       for (; entry != null; 
/* 399 */         entry = entry.nextInKeyInsertionOrder) {
/* 400 */         insert(entry, entry);
/*     */       }
/* 402 */       this.modCount++;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private BiEntry<K, V>[] createTable(int length) {
/* 408 */     return (BiEntry<K, V>[])new BiEntry[length];
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   public V remove(@CheckForNull Object key) {
/* 415 */     BiEntry<K, V> entry = seekByKey(key, Hashing.smearedHash(key));
/* 416 */     if (entry == null) {
/* 417 */       return null;
/*     */     }
/* 419 */     delete(entry);
/* 420 */     entry.prevInKeyInsertionOrder = null;
/* 421 */     entry.nextInKeyInsertionOrder = null;
/* 422 */     return entry.value;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 428 */     this.size = 0;
/* 429 */     Arrays.fill((Object[])this.hashTableKToV, (Object)null);
/* 430 */     Arrays.fill((Object[])this.hashTableVToK, (Object)null);
/* 431 */     this.firstInKeyInsertionOrder = null;
/* 432 */     this.lastInKeyInsertionOrder = null;
/* 433 */     this.modCount++;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 438 */     return this.size;
/*     */   }
/*     */   
/*     */   private abstract class Itr<T> implements Iterator<T> { @CheckForNull
/* 442 */     HashBiMap.BiEntry<K, V> next = HashBiMap.this.firstInKeyInsertionOrder; @CheckForNull
/* 443 */     HashBiMap.BiEntry<K, V> toRemove = null;
/* 444 */     int expectedModCount = HashBiMap.this.modCount;
/* 445 */     int remaining = HashBiMap.this.size();
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 449 */       if (HashBiMap.this.modCount != this.expectedModCount) {
/* 450 */         throw new ConcurrentModificationException();
/*     */       }
/* 452 */       return (this.next != null && this.remaining > 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public T next() {
/* 457 */       if (!hasNext()) {
/* 458 */         throw new NoSuchElementException();
/*     */       }
/*     */ 
/*     */       
/* 462 */       HashBiMap.BiEntry<K, V> entry = Objects.<HashBiMap.BiEntry<K, V>>requireNonNull(this.next);
/* 463 */       this.next = entry.nextInKeyInsertionOrder;
/* 464 */       this.toRemove = entry;
/* 465 */       this.remaining--;
/* 466 */       return output(entry);
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 471 */       if (HashBiMap.this.modCount != this.expectedModCount) {
/* 472 */         throw new ConcurrentModificationException();
/*     */       }
/* 474 */       if (this.toRemove == null) {
/* 475 */         throw new IllegalStateException("no calls to next() since the last call to remove()");
/*     */       }
/* 477 */       HashBiMap.this.delete(this.toRemove);
/* 478 */       this.expectedModCount = HashBiMap.this.modCount;
/* 479 */       this.toRemove = null;
/*     */     }
/*     */     
/*     */     private Itr() {}
/*     */     
/*     */     abstract T output(HashBiMap.BiEntry<K, V> param1BiEntry); }
/*     */   
/*     */   public Set<K> keySet() {
/* 487 */     return new KeySet();
/*     */   }
/*     */   
/*     */   private final class KeySet extends Maps.KeySet<K, V> {
/*     */     KeySet() {
/* 492 */       super(HashBiMap.this);
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<K> iterator() {
/* 497 */       return new HashBiMap<K, V>.Itr<K>(this)
/*     */         {
/*     */           @ParametricNullness
/*     */           K output(HashBiMap.BiEntry<K, V> entry) {
/* 501 */             return entry.key;
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(@CheckForNull Object o) {
/* 508 */       HashBiMap.BiEntry<K, V> entry = HashBiMap.this.seekByKey(o, Hashing.smearedHash(o));
/* 509 */       if (entry == null) {
/* 510 */         return false;
/*     */       }
/* 512 */       HashBiMap.this.delete(entry);
/* 513 */       entry.prevInKeyInsertionOrder = null;
/* 514 */       entry.nextInKeyInsertionOrder = null;
/* 515 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<V> values() {
/* 522 */     return inverse().keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<Map.Entry<K, V>> entryIterator() {
/* 527 */     return new Itr<Map.Entry<K, V>>()
/*     */       {
/*     */         Map.Entry<K, V> output(HashBiMap.BiEntry<K, V> entry) {
/* 530 */           return new MapEntry(entry);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         class MapEntry
/*     */           extends AbstractMapEntry<K, V>
/*     */         {
/*     */           private HashBiMap.BiEntry<K, V> delegate;
/*     */ 
/*     */           
/*     */           @ParametricNullness
/*     */           public K getKey() {
/* 543 */             return this.delegate.key;
/*     */           }
/*     */ 
/*     */           
/*     */           @ParametricNullness
/*     */           public V getValue() {
/* 549 */             return this.delegate.value;
/*     */           }
/*     */ 
/*     */           
/*     */           @ParametricNullness
/*     */           public V setValue(@ParametricNullness V value) {
/* 555 */             V oldValue = this.delegate.value;
/* 556 */             int valueHash = Hashing.smearedHash(value);
/* 557 */             if (valueHash == this.delegate.valueHash && Objects.equal(value, oldValue)) {
/* 558 */               return value;
/*     */             }
/* 560 */             Preconditions.checkArgument((HashBiMap.this.seekByValue(value, valueHash) == null), "value already present: %s", value);
/* 561 */             HashBiMap.this.delete(this.delegate);
/* 562 */             HashBiMap.BiEntry<K, V> newEntry = new HashBiMap.BiEntry<>(this.delegate.key, this.delegate.keyHash, value, valueHash);
/* 563 */             HashBiMap.this.insert(newEntry, this.delegate);
/* 564 */             this.delegate.prevInKeyInsertionOrder = null;
/* 565 */             this.delegate.nextInKeyInsertionOrder = null;
/* 566 */             HashBiMap.null.this.expectedModCount = HashBiMap.this.modCount;
/* 567 */             if (HashBiMap.null.this.toRemove == this.delegate) {
/* 568 */               HashBiMap.null.this.toRemove = newEntry;
/*     */             }
/* 570 */             this.delegate = newEntry;
/* 571 */             return oldValue;
/*     */           }
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(BiConsumer<? super K, ? super V> action) {
/* 579 */     Preconditions.checkNotNull(action);
/* 580 */     BiEntry<K, V> entry = this.firstInKeyInsertionOrder;
/* 581 */     for (; entry != null; 
/* 582 */       entry = entry.nextInKeyInsertionOrder) {
/* 583 */       action.accept(entry.key, entry.value);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
/* 589 */     Preconditions.checkNotNull(function);
/* 590 */     BiEntry<K, V> oldFirst = this.firstInKeyInsertionOrder;
/* 591 */     clear();
/* 592 */     for (BiEntry<K, V> entry = oldFirst; entry != null; entry = entry.nextInKeyInsertionOrder) {
/* 593 */       put(entry.key, function.apply(entry.key, entry.value));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BiMap<V, K> inverse() {
/* 601 */     BiMap<V, K> result = this.inverse;
/* 602 */     return (result == null) ? (this.inverse = new Inverse()) : result;
/*     */   }
/*     */   
/*     */   private final class Inverse extends Maps.IteratorBasedAbstractMap<V, K> implements BiMap<V, K>, Serializable { private Inverse() {}
/*     */     
/*     */     BiMap<K, V> forward() {
/* 608 */       return HashBiMap.this;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 613 */       return HashBiMap.this.size;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 618 */       forward().clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(@CheckForNull Object value) {
/* 623 */       return forward().containsValue(value);
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public K get(@CheckForNull Object value) {
/* 629 */       return Maps.keyOrNull(HashBiMap.this.seekByValue(value, Hashing.smearedHash(value)));
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     @CanIgnoreReturnValue
/*     */     public K put(@ParametricNullness V value, @ParametricNullness K key) {
/* 636 */       return HashBiMap.this.putInverse(value, key, false);
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public K forcePut(@ParametricNullness V value, @ParametricNullness K key) {
/* 642 */       return HashBiMap.this.putInverse(value, key, true);
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public K remove(@CheckForNull Object value) {
/* 648 */       HashBiMap.BiEntry<K, V> entry = HashBiMap.this.seekByValue(value, Hashing.smearedHash(value));
/* 649 */       if (entry == null) {
/* 650 */         return null;
/*     */       }
/* 652 */       HashBiMap.this.delete(entry);
/* 653 */       entry.prevInKeyInsertionOrder = null;
/* 654 */       entry.nextInKeyInsertionOrder = null;
/* 655 */       return entry.key;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public BiMap<K, V> inverse() {
/* 661 */       return forward();
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<V> keySet() {
/* 666 */       return new InverseKeySet();
/*     */     }
/*     */     
/*     */     private final class InverseKeySet extends Maps.KeySet<V, K> {
/*     */       InverseKeySet() {
/* 671 */         super(HashBiMap.Inverse.this);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean remove(@CheckForNull Object o) {
/* 676 */         HashBiMap.BiEntry<K, V> entry = HashBiMap.this.seekByValue(o, Hashing.smearedHash(o));
/* 677 */         if (entry == null) {
/* 678 */           return false;
/*     */         }
/* 680 */         HashBiMap.this.delete(entry);
/* 681 */         return true;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public Iterator<V> iterator() {
/* 687 */         return new HashBiMap<K, V>.Itr<V>(this)
/*     */           {
/*     */             @ParametricNullness
/*     */             V output(HashBiMap.BiEntry<K, V> entry) {
/* 691 */               return entry.value;
/*     */             }
/*     */           };
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<K> values() {
/* 699 */       return forward().keySet();
/*     */     }
/*     */ 
/*     */     
/*     */     Iterator<Map.Entry<V, K>> entryIterator() {
/* 704 */       return new HashBiMap<K, V>.Itr<Map.Entry<V, K>>()
/*     */         {
/*     */           Map.Entry<V, K> output(HashBiMap.BiEntry<K, V> entry) {
/* 707 */             return new InverseEntry(entry);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           class InverseEntry
/*     */             extends AbstractMapEntry<V, K>
/*     */           {
/*     */             private HashBiMap.BiEntry<K, V> delegate;
/*     */ 
/*     */             
/*     */             @ParametricNullness
/*     */             public V getKey() {
/* 720 */               return this.delegate.value;
/*     */             }
/*     */ 
/*     */             
/*     */             @ParametricNullness
/*     */             public K getValue() {
/* 726 */               return this.delegate.key;
/*     */             }
/*     */ 
/*     */             
/*     */             @ParametricNullness
/*     */             public K setValue(@ParametricNullness K key) {
/* 732 */               K oldKey = this.delegate.key;
/* 733 */               int keyHash = Hashing.smearedHash(key);
/* 734 */               if (keyHash == this.delegate.keyHash && Objects.equal(key, oldKey)) {
/* 735 */                 return key;
/*     */               }
/* 737 */               Preconditions.checkArgument((HashBiMap.this.seekByKey(key, keyHash) == null), "value already present: %s", key);
/* 738 */               HashBiMap.this.delete(this.delegate);
/* 739 */               HashBiMap.BiEntry<K, V> newEntry = new HashBiMap.BiEntry<>(key, keyHash, this.delegate.value, this.delegate.valueHash);
/*     */               
/* 741 */               this.delegate = newEntry;
/* 742 */               HashBiMap.this.insert(newEntry, (HashBiMap.BiEntry<K, V>)null);
/* 743 */               HashBiMap.Inverse.null.this.expectedModCount = HashBiMap.this.modCount;
/* 744 */               return oldKey;
/*     */             }
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public void forEach(BiConsumer<? super V, ? super K> action) {
/* 752 */       Preconditions.checkNotNull(action);
/* 753 */       HashBiMap.this.forEach((k, v) -> action.accept(v, k));
/*     */     }
/*     */ 
/*     */     
/*     */     public void replaceAll(BiFunction<? super V, ? super K, ? extends K> function) {
/* 758 */       Preconditions.checkNotNull(function);
/* 759 */       HashBiMap.BiEntry<K, V> oldFirst = HashBiMap.this.firstInKeyInsertionOrder;
/* 760 */       clear();
/* 761 */       for (HashBiMap.BiEntry<K, V> entry = oldFirst; entry != null; entry = entry.nextInKeyInsertionOrder) {
/* 762 */         put(entry.value, function.apply(entry.value, entry.key));
/*     */       }
/*     */     }
/*     */     
/*     */     Object writeReplace() {
/* 767 */       return new HashBiMap.InverseSerializedForm<>(HashBiMap.this);
/*     */     }
/*     */     
/*     */     @GwtIncompatible
/*     */     @J2ktIncompatible
/*     */     private void readObject(ObjectInputStream in) throws InvalidObjectException {
/* 773 */       throw new InvalidObjectException("Use InverseSerializedForm");
/*     */     } }
/*     */ 
/*     */   
/*     */   private static final class InverseSerializedForm<K, V>
/*     */     implements Serializable
/*     */   {
/*     */     private final HashBiMap<K, V> bimap;
/*     */     
/*     */     InverseSerializedForm(HashBiMap<K, V> bimap) {
/* 783 */       this.bimap = bimap;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 787 */       return this.bimap.inverse();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 797 */     stream.defaultWriteObject();
/* 798 */     Serialization.writeMap(this, stream);
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 804 */     stream.defaultReadObject();
/* 805 */     int size = Serialization.readCount(stream);
/* 806 */     init(16);
/* 807 */     Serialization.populateMap(this, stream, size);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/HashBiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */