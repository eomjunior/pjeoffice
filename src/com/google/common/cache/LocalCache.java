/*      */ package com.google.common.cache;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Equivalence;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Stopwatch;
/*      */ import com.google.common.base.Ticker;
/*      */ import com.google.common.collect.AbstractSequentialIterator;
/*      */ import com.google.common.collect.ImmutableMap;
/*      */ import com.google.common.collect.ImmutableSet;
/*      */ import com.google.common.collect.Iterators;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.common.collect.Sets;
/*      */ import com.google.common.primitives.Ints;
/*      */ import com.google.common.util.concurrent.ExecutionError;
/*      */ import com.google.common.util.concurrent.Futures;
/*      */ import com.google.common.util.concurrent.ListenableFuture;
/*      */ import com.google.common.util.concurrent.MoreExecutors;
/*      */ import com.google.common.util.concurrent.SettableFuture;
/*      */ import com.google.common.util.concurrent.UncheckedExecutionException;
/*      */ import com.google.common.util.concurrent.Uninterruptibles;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import com.google.errorprone.annotations.concurrent.GuardedBy;
/*      */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*      */ import com.google.j2objc.annotations.RetainedWith;
/*      */ import com.google.j2objc.annotations.Weak;
/*      */ import java.io.IOException;
/*      */ import java.io.InvalidObjectException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.Serializable;
/*      */ import java.lang.ref.Reference;
/*      */ import java.lang.ref.ReferenceQueue;
/*      */ import java.lang.ref.SoftReference;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.AbstractMap;
/*      */ import java.util.AbstractQueue;
/*      */ import java.util.AbstractSet;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Queue;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.concurrent.ConcurrentLinkedQueue;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.concurrent.ExecutionException;
/*      */ import java.util.concurrent.Future;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.concurrent.atomic.AtomicReferenceArray;
/*      */ import java.util.concurrent.locks.ReentrantLock;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.BiPredicate;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible(emulated = true)
/*      */ class LocalCache<K, V>
/*      */   extends AbstractMap<K, V>
/*      */   implements ConcurrentMap<K, V>
/*      */ {
/*      */   static final int MAXIMUM_CAPACITY = 1073741824;
/*      */   static final int MAX_SEGMENTS = 65536;
/*      */   static final int CONTAINS_VALUE_RETRIES = 3;
/*      */   static final int DRAIN_THRESHOLD = 63;
/*      */   static final int DRAIN_MAX = 16;
/*  170 */   static final Logger logger = Logger.getLogger(LocalCache.class.getName());
/*      */ 
/*      */ 
/*      */   
/*      */   final int segmentMask;
/*      */ 
/*      */ 
/*      */   
/*      */   final int segmentShift;
/*      */ 
/*      */ 
/*      */   
/*      */   final Segment<K, V>[] segments;
/*      */ 
/*      */ 
/*      */   
/*      */   final int concurrencyLevel;
/*      */ 
/*      */ 
/*      */   
/*      */   final Equivalence<Object> keyEquivalence;
/*      */ 
/*      */ 
/*      */   
/*      */   final Equivalence<Object> valueEquivalence;
/*      */ 
/*      */ 
/*      */   
/*      */   final Strength keyStrength;
/*      */ 
/*      */ 
/*      */   
/*      */   final Strength valueStrength;
/*      */ 
/*      */ 
/*      */   
/*      */   final long maxWeight;
/*      */ 
/*      */ 
/*      */   
/*      */   final Weigher<K, V> weigher;
/*      */ 
/*      */ 
/*      */   
/*      */   final long expireAfterAccessNanos;
/*      */ 
/*      */ 
/*      */   
/*      */   final long expireAfterWriteNanos;
/*      */ 
/*      */ 
/*      */   
/*      */   final long refreshNanos;
/*      */ 
/*      */ 
/*      */   
/*      */   final Queue<RemovalNotification<K, V>> removalNotificationQueue;
/*      */ 
/*      */   
/*      */   final RemovalListener<K, V> removalListener;
/*      */ 
/*      */   
/*      */   final Ticker ticker;
/*      */ 
/*      */   
/*      */   final EntryFactory entryFactory;
/*      */ 
/*      */   
/*      */   final AbstractCache.StatsCounter globalStatsCounter;
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   final CacheLoader<? super K, V> defaultLoader;
/*      */ 
/*      */ 
/*      */   
/*      */   LocalCache(CacheBuilder<? super K, ? super V> builder, @CheckForNull CacheLoader<? super K, V> loader) {
/*  247 */     this.concurrencyLevel = Math.min(builder.getConcurrencyLevel(), 65536);
/*      */     
/*  249 */     this.keyStrength = builder.getKeyStrength();
/*  250 */     this.valueStrength = builder.getValueStrength();
/*      */     
/*  252 */     this.keyEquivalence = builder.getKeyEquivalence();
/*  253 */     this.valueEquivalence = builder.getValueEquivalence();
/*      */     
/*  255 */     this.maxWeight = builder.getMaximumWeight();
/*  256 */     this.weigher = builder.getWeigher();
/*  257 */     this.expireAfterAccessNanos = builder.getExpireAfterAccessNanos();
/*  258 */     this.expireAfterWriteNanos = builder.getExpireAfterWriteNanos();
/*  259 */     this.refreshNanos = builder.getRefreshNanos();
/*      */     
/*  261 */     this.removalListener = builder.getRemovalListener();
/*  262 */     this
/*      */ 
/*      */       
/*  265 */       .removalNotificationQueue = (this.removalListener == CacheBuilder.NullListener.INSTANCE) ? discardingQueue() : new ConcurrentLinkedQueue<>();
/*      */     
/*  267 */     this.ticker = builder.getTicker(recordsTime());
/*  268 */     this.entryFactory = EntryFactory.getFactory(this.keyStrength, usesAccessEntries(), usesWriteEntries());
/*  269 */     this.globalStatsCounter = (AbstractCache.StatsCounter)builder.getStatsCounterSupplier().get();
/*  270 */     this.defaultLoader = loader;
/*      */     
/*  272 */     int initialCapacity = Math.min(builder.getInitialCapacity(), 1073741824);
/*  273 */     if (evictsBySize() && !customWeigher()) {
/*  274 */       initialCapacity = (int)Math.min(initialCapacity, this.maxWeight);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  282 */     int segmentShift = 0;
/*  283 */     int segmentCount = 1;
/*  284 */     while (segmentCount < this.concurrencyLevel && (
/*  285 */       !evictsBySize() || segmentCount * 20L <= this.maxWeight)) {
/*  286 */       segmentShift++;
/*  287 */       segmentCount <<= 1;
/*      */     } 
/*  289 */     this.segmentShift = 32 - segmentShift;
/*  290 */     this.segmentMask = segmentCount - 1;
/*      */     
/*  292 */     this.segments = newSegmentArray(segmentCount);
/*      */     
/*  294 */     int segmentCapacity = initialCapacity / segmentCount;
/*  295 */     if (segmentCapacity * segmentCount < initialCapacity) {
/*  296 */       segmentCapacity++;
/*      */     }
/*      */     
/*  299 */     int segmentSize = 1;
/*  300 */     while (segmentSize < segmentCapacity) {
/*  301 */       segmentSize <<= 1;
/*      */     }
/*      */     
/*  304 */     if (evictsBySize()) {
/*      */       
/*  306 */       long maxSegmentWeight = this.maxWeight / segmentCount + 1L;
/*  307 */       long remainder = this.maxWeight % segmentCount;
/*  308 */       for (int i = 0; i < this.segments.length; i++) {
/*  309 */         if (i == remainder) {
/*  310 */           maxSegmentWeight--;
/*      */         }
/*  312 */         this.segments[i] = 
/*  313 */           createSegment(segmentSize, maxSegmentWeight, (AbstractCache.StatsCounter)builder.getStatsCounterSupplier().get());
/*      */       } 
/*      */     } else {
/*  316 */       for (int i = 0; i < this.segments.length; i++) {
/*  317 */         this.segments[i] = 
/*  318 */           createSegment(segmentSize, -1L, (AbstractCache.StatsCounter)builder.getStatsCounterSupplier().get());
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   boolean evictsBySize() {
/*  324 */     return (this.maxWeight >= 0L);
/*      */   }
/*      */   
/*      */   boolean customWeigher() {
/*  328 */     return (this.weigher != CacheBuilder.OneWeigher.INSTANCE);
/*      */   }
/*      */   
/*      */   boolean expires() {
/*  332 */     return (expiresAfterWrite() || expiresAfterAccess());
/*      */   }
/*      */   
/*      */   boolean expiresAfterWrite() {
/*  336 */     return (this.expireAfterWriteNanos > 0L);
/*      */   }
/*      */   
/*      */   boolean expiresAfterAccess() {
/*  340 */     return (this.expireAfterAccessNanos > 0L);
/*      */   }
/*      */   
/*      */   boolean refreshes() {
/*  344 */     return (this.refreshNanos > 0L);
/*      */   }
/*      */   
/*      */   boolean usesAccessQueue() {
/*  348 */     return (expiresAfterAccess() || evictsBySize());
/*      */   }
/*      */   
/*      */   boolean usesWriteQueue() {
/*  352 */     return expiresAfterWrite();
/*      */   }
/*      */   
/*      */   boolean recordsWrite() {
/*  356 */     return (expiresAfterWrite() || refreshes());
/*      */   }
/*      */   
/*      */   boolean recordsAccess() {
/*  360 */     return expiresAfterAccess();
/*      */   }
/*      */   
/*      */   boolean recordsTime() {
/*  364 */     return (recordsWrite() || recordsAccess());
/*      */   }
/*      */   
/*      */   boolean usesWriteEntries() {
/*  368 */     return (usesWriteQueue() || recordsWrite());
/*      */   }
/*      */   
/*      */   boolean usesAccessEntries() {
/*  372 */     return (usesAccessQueue() || recordsAccess());
/*      */   }
/*      */   
/*      */   boolean usesKeyReferences() {
/*  376 */     return (this.keyStrength != Strength.STRONG);
/*      */   }
/*      */   
/*      */   boolean usesValueReferences() {
/*  380 */     return (this.valueStrength != Strength.STRONG);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   enum Strength
/*      */   {
/*  389 */     STRONG
/*      */     {
/*      */       <K, V> LocalCache.ValueReference<K, V> referenceValue(LocalCache.Segment<K, V> segment, ReferenceEntry<K, V> entry, V value, int weight)
/*      */       {
/*  393 */         return (weight == 1) ? 
/*  394 */           new LocalCache.StrongValueReference<>(value) : 
/*  395 */           new LocalCache.WeightedStrongValueReference<>(value, weight);
/*      */       }
/*      */ 
/*      */       
/*      */       Equivalence<Object> defaultEquivalence() {
/*  400 */         return Equivalence.equals();
/*      */       }
/*      */     },
/*  403 */     SOFT
/*      */     {
/*      */       <K, V> LocalCache.ValueReference<K, V> referenceValue(LocalCache.Segment<K, V> segment, ReferenceEntry<K, V> entry, V value, int weight)
/*      */       {
/*  407 */         return (weight == 1) ? 
/*  408 */           new LocalCache.SoftValueReference<>(segment.valueReferenceQueue, value, entry) : 
/*  409 */           new LocalCache.WeightedSoftValueReference<>(segment.valueReferenceQueue, value, entry, weight);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       Equivalence<Object> defaultEquivalence() {
/*  415 */         return Equivalence.identity();
/*      */       }
/*      */     },
/*  418 */     WEAK
/*      */     {
/*      */       <K, V> LocalCache.ValueReference<K, V> referenceValue(LocalCache.Segment<K, V> segment, ReferenceEntry<K, V> entry, V value, int weight)
/*      */       {
/*  422 */         return (weight == 1) ? 
/*  423 */           new LocalCache.WeakValueReference<>(segment.valueReferenceQueue, value, entry) : 
/*  424 */           new LocalCache.WeightedWeakValueReference<>(segment.valueReferenceQueue, value, entry, weight);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       Equivalence<Object> defaultEquivalence() {
/*  430 */         return Equivalence.identity();
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract <K, V> LocalCache.ValueReference<K, V> referenceValue(LocalCache.Segment<K, V> param1Segment, ReferenceEntry<K, V> param1ReferenceEntry, V param1V, int param1Int);
/*      */ 
/*      */ 
/*      */     
/*      */     abstract Equivalence<Object> defaultEquivalence();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   enum EntryFactory
/*      */   {
/*  448 */     STRONG
/*      */     {
/*      */       <K, V> ReferenceEntry<K, V> newEntry(LocalCache.Segment<K, V> segment, K key, int hash, @CheckForNull ReferenceEntry<K, V> next)
/*      */       {
/*  452 */         return new LocalCache.StrongEntry<>(key, hash, next);
/*      */       }
/*      */     },
/*  455 */     STRONG_ACCESS
/*      */     {
/*      */       <K, V> ReferenceEntry<K, V> newEntry(LocalCache.Segment<K, V> segment, K key, int hash, @CheckForNull ReferenceEntry<K, V> next)
/*      */       {
/*  459 */         return new LocalCache.StrongAccessEntry<>(key, hash, next);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       <K, V> ReferenceEntry<K, V> copyEntry(LocalCache.Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext, K key) {
/*  468 */         ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext, key);
/*  469 */         copyAccessEntry(original, newEntry);
/*  470 */         return newEntry;
/*      */       }
/*      */     },
/*  473 */     STRONG_WRITE
/*      */     {
/*      */       <K, V> ReferenceEntry<K, V> newEntry(LocalCache.Segment<K, V> segment, K key, int hash, @CheckForNull ReferenceEntry<K, V> next)
/*      */       {
/*  477 */         return new LocalCache.StrongWriteEntry<>(key, hash, next);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       <K, V> ReferenceEntry<K, V> copyEntry(LocalCache.Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext, K key) {
/*  486 */         ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext, key);
/*  487 */         copyWriteEntry(original, newEntry);
/*  488 */         return newEntry;
/*      */       }
/*      */     },
/*  491 */     STRONG_ACCESS_WRITE
/*      */     {
/*      */       <K, V> ReferenceEntry<K, V> newEntry(LocalCache.Segment<K, V> segment, K key, int hash, @CheckForNull ReferenceEntry<K, V> next)
/*      */       {
/*  495 */         return new LocalCache.StrongAccessWriteEntry<>(key, hash, next);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       <K, V> ReferenceEntry<K, V> copyEntry(LocalCache.Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext, K key) {
/*  504 */         ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext, key);
/*  505 */         copyAccessEntry(original, newEntry);
/*  506 */         copyWriteEntry(original, newEntry);
/*  507 */         return newEntry;
/*      */       }
/*      */     },
/*  510 */     WEAK
/*      */     {
/*      */       <K, V> ReferenceEntry<K, V> newEntry(LocalCache.Segment<K, V> segment, K key, int hash, @CheckForNull ReferenceEntry<K, V> next)
/*      */       {
/*  514 */         return new LocalCache.WeakEntry<>(segment.keyReferenceQueue, key, hash, next);
/*      */       }
/*      */     },
/*  517 */     WEAK_ACCESS
/*      */     {
/*      */       <K, V> ReferenceEntry<K, V> newEntry(LocalCache.Segment<K, V> segment, K key, int hash, @CheckForNull ReferenceEntry<K, V> next)
/*      */       {
/*  521 */         return new LocalCache.WeakAccessEntry<>(segment.keyReferenceQueue, key, hash, next);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       <K, V> ReferenceEntry<K, V> copyEntry(LocalCache.Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext, K key) {
/*  530 */         ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext, key);
/*  531 */         copyAccessEntry(original, newEntry);
/*  532 */         return newEntry;
/*      */       }
/*      */     },
/*  535 */     WEAK_WRITE
/*      */     {
/*      */       <K, V> ReferenceEntry<K, V> newEntry(LocalCache.Segment<K, V> segment, K key, int hash, @CheckForNull ReferenceEntry<K, V> next)
/*      */       {
/*  539 */         return new LocalCache.WeakWriteEntry<>(segment.keyReferenceQueue, key, hash, next);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       <K, V> ReferenceEntry<K, V> copyEntry(LocalCache.Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext, K key) {
/*  548 */         ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext, key);
/*  549 */         copyWriteEntry(original, newEntry);
/*  550 */         return newEntry;
/*      */       }
/*      */     },
/*  553 */     WEAK_ACCESS_WRITE
/*      */     {
/*      */       <K, V> ReferenceEntry<K, V> newEntry(LocalCache.Segment<K, V> segment, K key, int hash, @CheckForNull ReferenceEntry<K, V> next)
/*      */       {
/*  557 */         return new LocalCache.WeakAccessWriteEntry<>(segment.keyReferenceQueue, key, hash, next);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       <K, V> ReferenceEntry<K, V> copyEntry(LocalCache.Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext, K key) {
/*  566 */         ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext, key);
/*  567 */         copyAccessEntry(original, newEntry);
/*  568 */         copyWriteEntry(original, newEntry);
/*  569 */         return newEntry;
/*      */       }
/*      */     };
/*      */ 
/*      */     
/*      */     static final int ACCESS_MASK = 1;
/*      */     
/*      */     static final int WRITE_MASK = 2;
/*      */     
/*      */     static final int WEAK_MASK = 4;
/*      */     
/*  580 */     static final EntryFactory[] factories = new EntryFactory[] { STRONG, STRONG_ACCESS, STRONG_WRITE, STRONG_ACCESS_WRITE, WEAK, WEAK_ACCESS, WEAK_WRITE, WEAK_ACCESS_WRITE };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static {
/*      */     
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static EntryFactory getFactory(LocalCache.Strength keyStrength, boolean usesAccessQueue, boolean usesWriteQueue) {
/*  596 */       int flags = ((keyStrength == LocalCache.Strength.WEAK) ? 4 : 0) | (usesAccessQueue ? 1 : 0) | (usesWriteQueue ? 2 : 0);
/*  597 */       return factories[flags];
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     <K, V> ReferenceEntry<K, V> copyEntry(LocalCache.Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext, K key) {
/*  625 */       return newEntry(segment, key, original.getHash(), newNext);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     <K, V> void copyAccessEntry(ReferenceEntry<K, V> original, ReferenceEntry<K, V> newEntry) {
/*  632 */       newEntry.setAccessTime(original.getAccessTime());
/*      */       
/*  634 */       LocalCache.connectAccessOrder(original.getPreviousInAccessQueue(), newEntry);
/*  635 */       LocalCache.connectAccessOrder(newEntry, original.getNextInAccessQueue());
/*      */       
/*  637 */       LocalCache.nullifyAccessOrder(original);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     <K, V> void copyWriteEntry(ReferenceEntry<K, V> original, ReferenceEntry<K, V> newEntry) {
/*  644 */       newEntry.setWriteTime(original.getWriteTime());
/*      */       
/*  646 */       LocalCache.connectWriteOrder(original.getPreviousInWriteQueue(), newEntry);
/*  647 */       LocalCache.connectWriteOrder(newEntry, original.getNextInWriteQueue());
/*      */       
/*  649 */       LocalCache.nullifyWriteOrder(original);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract <K, V> ReferenceEntry<K, V> newEntry(LocalCache.Segment<K, V> param1Segment, K param1K, int param1Int, @CheckForNull ReferenceEntry<K, V> param1ReferenceEntry);
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
/*  710 */   static final ValueReference<Object, Object> UNSET = new ValueReference<Object, Object>()
/*      */     {
/*      */       @CheckForNull
/*      */       public Object get()
/*      */       {
/*  715 */         return null;
/*      */       }
/*      */ 
/*      */       
/*      */       public int getWeight() {
/*  720 */         return 0;
/*      */       }
/*      */ 
/*      */       
/*      */       @CheckForNull
/*      */       public ReferenceEntry<Object, Object> getEntry() {
/*  726 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public LocalCache.ValueReference<Object, Object> copyFor(ReferenceQueue<Object> queue, @CheckForNull Object value, ReferenceEntry<Object, Object> entry) {
/*  734 */         return this;
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean isLoading() {
/*  739 */         return false;
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean isActive() {
/*  744 */         return false;
/*      */       }
/*      */ 
/*      */       
/*      */       @CheckForNull
/*      */       public Object waitForValue() {
/*  750 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public void notifyNewValue(Object newValue) {}
/*      */     };
/*      */ 
/*      */   
/*      */   static <K, V> ValueReference<K, V> unset() {
/*  760 */     return (ValueReference)UNSET;
/*      */   }
/*      */   
/*      */   private enum NullEntry implements ReferenceEntry<Object, Object> {
/*  764 */     INSTANCE;
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public LocalCache.ValueReference<Object, Object> getValueReference() {
/*  769 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     public void setValueReference(LocalCache.ValueReference<Object, Object> valueReference) {}
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public ReferenceEntry<Object, Object> getNext() {
/*  778 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getHash() {
/*  783 */       return 0;
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Object getKey() {
/*  789 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     public long getAccessTime() {
/*  794 */       return 0L;
/*      */     }
/*      */ 
/*      */     
/*      */     public void setAccessTime(long time) {}
/*      */ 
/*      */     
/*      */     public ReferenceEntry<Object, Object> getNextInAccessQueue() {
/*  802 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public void setNextInAccessQueue(ReferenceEntry<Object, Object> next) {}
/*      */ 
/*      */     
/*      */     public ReferenceEntry<Object, Object> getPreviousInAccessQueue() {
/*  810 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public void setPreviousInAccessQueue(ReferenceEntry<Object, Object> previous) {}
/*      */ 
/*      */     
/*      */     public long getWriteTime() {
/*  818 */       return 0L;
/*      */     }
/*      */ 
/*      */     
/*      */     public void setWriteTime(long time) {}
/*      */ 
/*      */     
/*      */     public ReferenceEntry<Object, Object> getNextInWriteQueue() {
/*  826 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public void setNextInWriteQueue(ReferenceEntry<Object, Object> next) {}
/*      */ 
/*      */     
/*      */     public ReferenceEntry<Object, Object> getPreviousInWriteQueue() {
/*  834 */       return this;
/*      */     }
/*      */     
/*      */     public void setPreviousInWriteQueue(ReferenceEntry<Object, Object> previous) {}
/*      */   }
/*      */   
/*      */   static abstract class AbstractReferenceEntry<K, V>
/*      */     implements ReferenceEntry<K, V>
/*      */   {
/*      */     public LocalCache.ValueReference<K, V> getValueReference() {
/*  844 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public void setValueReference(LocalCache.ValueReference<K, V> valueReference) {
/*  849 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public ReferenceEntry<K, V> getNext() {
/*  854 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public int getHash() {
/*  859 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public K getKey() {
/*  864 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public long getAccessTime() {
/*  869 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public void setAccessTime(long time) {
/*  874 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public ReferenceEntry<K, V> getNextInAccessQueue() {
/*  879 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public void setNextInAccessQueue(ReferenceEntry<K, V> next) {
/*  884 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public ReferenceEntry<K, V> getPreviousInAccessQueue() {
/*  889 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public void setPreviousInAccessQueue(ReferenceEntry<K, V> previous) {
/*  894 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public long getWriteTime() {
/*  899 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public void setWriteTime(long time) {
/*  904 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public ReferenceEntry<K, V> getNextInWriteQueue() {
/*  909 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public void setNextInWriteQueue(ReferenceEntry<K, V> next) {
/*  914 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public ReferenceEntry<K, V> getPreviousInWriteQueue() {
/*  919 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public void setPreviousInWriteQueue(ReferenceEntry<K, V> previous) {
/*  924 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static <K, V> ReferenceEntry<K, V> nullEntry() {
/*  930 */     return NullEntry.INSTANCE;
/*      */   }
/*      */   
/*  933 */   static final Queue<?> DISCARDING_QUEUE = new AbstractQueue()
/*      */     {
/*      */       public boolean offer(Object o)
/*      */       {
/*  937 */         return true;
/*      */       }
/*      */ 
/*      */       
/*      */       @CheckForNull
/*      */       public Object peek() {
/*  943 */         return null;
/*      */       }
/*      */ 
/*      */       
/*      */       @CheckForNull
/*      */       public Object poll() {
/*  949 */         return null;
/*      */       }
/*      */ 
/*      */       
/*      */       public int size() {
/*  954 */         return 0;
/*      */       }
/*      */       
/*      */       public Iterator<Object> iterator()
/*      */       {
/*  959 */         return (Iterator<Object>)ImmutableSet.of().iterator(); } }; @LazyInit @CheckForNull @RetainedWith
/*      */   Set<K> keySet; @LazyInit
/*      */   @CheckForNull
/*      */   @RetainedWith
/*      */   Collection<V> values; @LazyInit
/*      */   @CheckForNull
/*      */   @RetainedWith
/*  966 */   Set<Map.Entry<K, V>> entrySet; static <E> Queue<E> discardingQueue() { return (Queue)DISCARDING_QUEUE; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class StrongEntry<K, V>
/*      */     extends AbstractReferenceEntry<K, V>
/*      */   {
/*      */     final K key;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final int hash;
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     final ReferenceEntry<K, V> next;
/*      */ 
/*      */ 
/*      */     
/*      */     volatile LocalCache.ValueReference<K, V> valueReference;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     StrongEntry(K key, int hash, @CheckForNull ReferenceEntry<K, V> next) {
/*  996 */       this.valueReference = LocalCache.unset();
/*      */       this.key = key;
/*      */       this.hash = hash;
/*      */       this.next = next;
/* 1000 */     } public K getKey() { return this.key; } public LocalCache.ValueReference<K, V> getValueReference() { return this.valueReference; }
/*      */ 
/*      */ 
/*      */     
/*      */     public void setValueReference(LocalCache.ValueReference<K, V> valueReference) {
/* 1005 */       this.valueReference = valueReference;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getHash() {
/* 1010 */       return this.hash;
/*      */     }
/*      */ 
/*      */     
/*      */     public ReferenceEntry<K, V> getNext() {
/* 1015 */       return this.next;
/*      */     } }
/*      */   static final class StrongAccessEntry<K, V> extends StrongEntry<K, V> { volatile long accessTime; @Weak
/*      */     ReferenceEntry<K, V> nextAccess; @Weak
/*      */     ReferenceEntry<K, V> previousAccess;
/*      */     
/* 1021 */     StrongAccessEntry(K key, int hash, @CheckForNull ReferenceEntry<K, V> next) { super(key, hash, next);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1026 */       this.accessTime = Long.MAX_VALUE;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1039 */       this.nextAccess = LocalCache.nullEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1052 */       this.previousAccess = LocalCache.nullEntry(); } public long getAccessTime() { return this.accessTime; }
/*      */     public void setAccessTime(long time) { this.accessTime = time; }
/*      */     public ReferenceEntry<K, V> getNextInAccessQueue() { return this.nextAccess; }
/*      */     public void setNextInAccessQueue(ReferenceEntry<K, V> next) { this.nextAccess = next; }
/* 1056 */     public ReferenceEntry<K, V> getPreviousInAccessQueue() { return this.previousAccess; }
/*      */ 
/*      */ 
/*      */     
/*      */     public void setPreviousInAccessQueue(ReferenceEntry<K, V> previous) {
/* 1061 */       this.previousAccess = previous;
/*      */     } }
/*      */   static final class StrongWriteEntry<K, V> extends StrongEntry<K, V> { volatile long writeTime; @Weak
/*      */     ReferenceEntry<K, V> nextWrite; @Weak
/*      */     ReferenceEntry<K, V> previousWrite;
/*      */     
/* 1067 */     StrongWriteEntry(K key, int hash, @CheckForNull ReferenceEntry<K, V> next) { super(key, hash, next);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1072 */       this.writeTime = Long.MAX_VALUE;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1085 */       this.nextWrite = LocalCache.nullEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1098 */       this.previousWrite = LocalCache.nullEntry(); } public long getWriteTime() { return this.writeTime; }
/*      */     public void setWriteTime(long time) { this.writeTime = time; }
/*      */     public ReferenceEntry<K, V> getNextInWriteQueue() { return this.nextWrite; }
/*      */     public void setNextInWriteQueue(ReferenceEntry<K, V> next) { this.nextWrite = next; }
/* 1102 */     public ReferenceEntry<K, V> getPreviousInWriteQueue() { return this.previousWrite; }
/*      */ 
/*      */ 
/*      */     
/*      */     public void setPreviousInWriteQueue(ReferenceEntry<K, V> previous) {
/* 1107 */       this.previousWrite = previous; } }
/*      */    static final class StrongAccessWriteEntry<K, V> extends StrongEntry<K, V>
/*      */   {
/*      */     volatile long accessTime; @Weak ReferenceEntry<K, V> nextAccess; @Weak
/*      */     ReferenceEntry<K, V> previousAccess; volatile long writeTime; @Weak
/*      */     ReferenceEntry<K, V> nextWrite; @Weak
/* 1113 */     ReferenceEntry<K, V> previousWrite; public long getAccessTime() { return this.accessTime; } public void setAccessTime(long time) { this.accessTime = time; } public ReferenceEntry<K, V> getNextInAccessQueue() { return this.nextAccess; } StrongAccessWriteEntry(K key, int hash, @CheckForNull ReferenceEntry<K, V> next) { super(key, hash, next);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1118 */       this.accessTime = Long.MAX_VALUE;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1131 */       this.nextAccess = LocalCache.nullEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1144 */       this.previousAccess = LocalCache.nullEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1158 */       this.writeTime = Long.MAX_VALUE;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1171 */       this.nextWrite = LocalCache.nullEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1184 */       this.previousWrite = LocalCache.nullEntry(); }
/*      */     public void setNextInAccessQueue(ReferenceEntry<K, V> next) { this.nextAccess = next; } public ReferenceEntry<K, V> getPreviousInAccessQueue() {
/*      */       return this.previousAccess;
/*      */     } public ReferenceEntry<K, V> getPreviousInWriteQueue() {
/* 1188 */       return this.previousWrite; } public void setPreviousInAccessQueue(ReferenceEntry<K, V> previous) { this.previousAccess = previous; }
/*      */     public long getWriteTime() { return this.writeTime; }
/*      */     public void setWriteTime(long time) { this.writeTime = time; }
/*      */     public ReferenceEntry<K, V> getNextInWriteQueue() { return this.nextWrite; }
/*      */     public void setNextInWriteQueue(ReferenceEntry<K, V> next) { this.nextWrite = next; }
/* 1193 */     public void setPreviousInWriteQueue(ReferenceEntry<K, V> previous) { this.previousWrite = previous; } } static class WeakEntry<K, V> extends WeakReference<K> implements ReferenceEntry<K, V> { final int hash; @CheckForNull
/*      */     final ReferenceEntry<K, V> next; volatile LocalCache.ValueReference<K, V> valueReference; public K getKey() { return get(); } public long getAccessTime() {
/*      */       throw new UnsupportedOperationException();
/*      */     }
/*      */     public void setAccessTime(long time) {
/*      */       throw new UnsupportedOperationException();
/*      */     }
/* 1200 */     WeakEntry(ReferenceQueue<K> queue, K key, int hash, @CheckForNull ReferenceEntry<K, V> next) { super(key, queue);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1283 */       this.valueReference = LocalCache.unset(); this.hash = hash;
/*      */       this.next = next; } public ReferenceEntry<K, V> getNextInAccessQueue() { throw new UnsupportedOperationException(); }
/*      */     public void setNextInAccessQueue(ReferenceEntry<K, V> next) { throw new UnsupportedOperationException(); }
/*      */     public ReferenceEntry<K, V> getPreviousInAccessQueue() { throw new UnsupportedOperationException(); }
/* 1287 */     public LocalCache.ValueReference<K, V> getValueReference() { return this.valueReference; } public void setPreviousInAccessQueue(ReferenceEntry<K, V> previous) { throw new UnsupportedOperationException(); }
/*      */     public long getWriteTime() { throw new UnsupportedOperationException(); }
/*      */     public void setWriteTime(long time) {
/*      */       throw new UnsupportedOperationException();
/*      */     }
/* 1292 */     public void setValueReference(LocalCache.ValueReference<K, V> valueReference) { this.valueReference = valueReference; } public ReferenceEntry<K, V> getNextInWriteQueue() { throw new UnsupportedOperationException(); }
/*      */     public void setNextInWriteQueue(ReferenceEntry<K, V> next) { throw new UnsupportedOperationException(); }
/*      */     public ReferenceEntry<K, V> getPreviousInWriteQueue() { throw new UnsupportedOperationException(); }
/*      */     public void setPreviousInWriteQueue(ReferenceEntry<K, V> previous) { throw new UnsupportedOperationException(); }
/*      */     public int getHash() {
/* 1297 */       return this.hash;
/*      */     }
/*      */ 
/*      */     
/*      */     public ReferenceEntry<K, V> getNext() {
/* 1302 */       return this.next;
/*      */     } }
/*      */   static final class WeakAccessEntry<K, V> extends WeakEntry<K, V> { volatile long accessTime; @Weak
/*      */     ReferenceEntry<K, V> nextAccess;
/*      */     @Weak
/*      */     ReferenceEntry<K, V> previousAccess;
/*      */     
/* 1309 */     WeakAccessEntry(ReferenceQueue<K> queue, K key, int hash, @CheckForNull ReferenceEntry<K, V> next) { super(queue, key, hash, next);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1314 */       this.accessTime = Long.MAX_VALUE;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1327 */       this.nextAccess = LocalCache.nullEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1340 */       this.previousAccess = LocalCache.nullEntry(); } public long getAccessTime() { return this.accessTime; }
/*      */     public void setAccessTime(long time) { this.accessTime = time; }
/*      */     public ReferenceEntry<K, V> getNextInAccessQueue() { return this.nextAccess; }
/*      */     public void setNextInAccessQueue(ReferenceEntry<K, V> next) { this.nextAccess = next; }
/* 1344 */     public ReferenceEntry<K, V> getPreviousInAccessQueue() { return this.previousAccess; }
/*      */ 
/*      */ 
/*      */     
/*      */     public void setPreviousInAccessQueue(ReferenceEntry<K, V> previous) {
/* 1349 */       this.previousAccess = previous;
/*      */     } }
/*      */   static final class WeakWriteEntry<K, V> extends WeakEntry<K, V> { volatile long writeTime; @Weak
/*      */     ReferenceEntry<K, V> nextWrite;
/*      */     @Weak
/*      */     ReferenceEntry<K, V> previousWrite;
/*      */     
/* 1356 */     WeakWriteEntry(ReferenceQueue<K> queue, K key, int hash, @CheckForNull ReferenceEntry<K, V> next) { super(queue, key, hash, next);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1361 */       this.writeTime = Long.MAX_VALUE;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1374 */       this.nextWrite = LocalCache.nullEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1387 */       this.previousWrite = LocalCache.nullEntry(); } public long getWriteTime() { return this.writeTime; }
/*      */     public void setWriteTime(long time) { this.writeTime = time; }
/*      */     public ReferenceEntry<K, V> getNextInWriteQueue() { return this.nextWrite; }
/*      */     public void setNextInWriteQueue(ReferenceEntry<K, V> next) { this.nextWrite = next; }
/* 1391 */     public ReferenceEntry<K, V> getPreviousInWriteQueue() { return this.previousWrite; }
/*      */ 
/*      */ 
/*      */     
/*      */     public void setPreviousInWriteQueue(ReferenceEntry<K, V> previous) {
/* 1396 */       this.previousWrite = previous;
/*      */     } }
/*      */   static final class WeakAccessWriteEntry<K, V> extends WeakEntry<K, V> { volatile long accessTime; @Weak
/*      */     ReferenceEntry<K, V> nextAccess; @Weak
/*      */     ReferenceEntry<K, V> previousAccess; volatile long writeTime; @Weak
/*      */     ReferenceEntry<K, V> nextWrite; @Weak
/*      */     ReferenceEntry<K, V> previousWrite;
/* 1403 */     WeakAccessWriteEntry(ReferenceQueue<K> queue, K key, int hash, @CheckForNull ReferenceEntry<K, V> next) { super(queue, key, hash, next);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1408 */       this.accessTime = Long.MAX_VALUE;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1421 */       this.nextAccess = LocalCache.nullEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1434 */       this.previousAccess = LocalCache.nullEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1448 */       this.writeTime = Long.MAX_VALUE;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1461 */       this.nextWrite = LocalCache.nullEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1474 */       this.previousWrite = LocalCache.nullEntry(); } public long getAccessTime() { return this.accessTime; } public void setAccessTime(long time) { this.accessTime = time; }
/*      */     public ReferenceEntry<K, V> getNextInAccessQueue() { return this.nextAccess; }
/*      */     public void setNextInAccessQueue(ReferenceEntry<K, V> next) { this.nextAccess = next; }
/*      */     public ReferenceEntry<K, V> getPreviousInAccessQueue() { return this.previousAccess; }
/* 1478 */     public ReferenceEntry<K, V> getPreviousInWriteQueue() { return this.previousWrite; } public void setPreviousInAccessQueue(ReferenceEntry<K, V> previous) { this.previousAccess = previous; }
/*      */     public long getWriteTime() { return this.writeTime; }
/*      */     public void setWriteTime(long time) { this.writeTime = time; }
/*      */     public ReferenceEntry<K, V> getNextInWriteQueue() { return this.nextWrite; }
/*      */     public void setNextInWriteQueue(ReferenceEntry<K, V> next) { this.nextWrite = next; }
/* 1483 */     public void setPreviousInWriteQueue(ReferenceEntry<K, V> previous) { this.previousWrite = previous; }
/*      */      }
/*      */ 
/*      */   
/*      */   static class WeakValueReference<K, V>
/*      */     extends WeakReference<V> implements ValueReference<K, V> {
/*      */     final ReferenceEntry<K, V> entry;
/*      */     
/*      */     WeakValueReference(ReferenceQueue<V> queue, V referent, ReferenceEntry<K, V> entry) {
/* 1492 */       super(referent, queue);
/* 1493 */       this.entry = entry;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getWeight() {
/* 1498 */       return 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public ReferenceEntry<K, V> getEntry() {
/* 1503 */       return this.entry;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void notifyNewValue(V newValue) {}
/*      */ 
/*      */     
/*      */     public LocalCache.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, ReferenceEntry<K, V> entry) {
/* 1512 */       return new WeakValueReference(queue, value, entry);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isLoading() {
/* 1517 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isActive() {
/* 1522 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public V waitForValue() {
/* 1527 */       return get();
/*      */     }
/*      */   }
/*      */   
/*      */   static class SoftValueReference<K, V>
/*      */     extends SoftReference<V> implements ValueReference<K, V> {
/*      */     final ReferenceEntry<K, V> entry;
/*      */     
/*      */     SoftValueReference(ReferenceQueue<V> queue, V referent, ReferenceEntry<K, V> entry) {
/* 1536 */       super(referent, queue);
/* 1537 */       this.entry = entry;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getWeight() {
/* 1542 */       return 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public ReferenceEntry<K, V> getEntry() {
/* 1547 */       return this.entry;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void notifyNewValue(V newValue) {}
/*      */ 
/*      */     
/*      */     public LocalCache.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, ReferenceEntry<K, V> entry) {
/* 1556 */       return new SoftValueReference(queue, value, entry);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isLoading() {
/* 1561 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isActive() {
/* 1566 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public V waitForValue() {
/* 1571 */       return get();
/*      */     }
/*      */   }
/*      */   
/*      */   static class StrongValueReference<K, V>
/*      */     implements ValueReference<K, V> {
/*      */     final V referent;
/*      */     
/*      */     StrongValueReference(V referent) {
/* 1580 */       this.referent = referent;
/*      */     }
/*      */ 
/*      */     
/*      */     public V get() {
/* 1585 */       return this.referent;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getWeight() {
/* 1590 */       return 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public ReferenceEntry<K, V> getEntry() {
/* 1595 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public LocalCache.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, ReferenceEntry<K, V> entry) {
/* 1601 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isLoading() {
/* 1606 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isActive() {
/* 1611 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public V waitForValue() {
/* 1616 */       return get();
/*      */     }
/*      */ 
/*      */     
/*      */     public void notifyNewValue(V newValue) {}
/*      */   }
/*      */   
/*      */   static final class WeightedWeakValueReference<K, V>
/*      */     extends WeakValueReference<K, V>
/*      */   {
/*      */     final int weight;
/*      */     
/*      */     WeightedWeakValueReference(ReferenceQueue<V> queue, V referent, ReferenceEntry<K, V> entry, int weight) {
/* 1629 */       super(queue, referent, entry);
/* 1630 */       this.weight = weight;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getWeight() {
/* 1635 */       return this.weight;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public LocalCache.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, ReferenceEntry<K, V> entry) {
/* 1641 */       return new WeightedWeakValueReference(queue, value, entry, this.weight);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class WeightedSoftValueReference<K, V>
/*      */     extends SoftValueReference<K, V>
/*      */   {
/*      */     final int weight;
/*      */     
/*      */     WeightedSoftValueReference(ReferenceQueue<V> queue, V referent, ReferenceEntry<K, V> entry, int weight) {
/* 1651 */       super(queue, referent, entry);
/* 1652 */       this.weight = weight;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getWeight() {
/* 1657 */       return this.weight;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public LocalCache.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, ReferenceEntry<K, V> entry) {
/* 1663 */       return new WeightedSoftValueReference(queue, value, entry, this.weight);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class WeightedStrongValueReference<K, V>
/*      */     extends StrongValueReference<K, V> {
/*      */     final int weight;
/*      */     
/*      */     WeightedStrongValueReference(V referent, int weight) {
/* 1672 */       super(referent);
/* 1673 */       this.weight = weight;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getWeight() {
/* 1678 */       return this.weight;
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
/*      */   static int rehash(int h) {
/* 1694 */     h += h << 15 ^ 0xFFFFCD7D;
/* 1695 */     h ^= h >>> 10;
/* 1696 */     h += h << 3;
/* 1697 */     h ^= h >>> 6;
/* 1698 */     h += (h << 2) + (h << 14);
/* 1699 */     return h ^ h >>> 16;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   ReferenceEntry<K, V> newEntry(K key, int hash, @CheckForNull ReferenceEntry<K, V> next) {
/* 1707 */     Segment<K, V> segment = segmentFor(hash);
/* 1708 */     segment.lock();
/*      */     try {
/* 1710 */       return segment.newEntry(key, hash, next);
/*      */     } finally {
/* 1712 */       segment.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   ReferenceEntry<K, V> copyEntry(ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
/* 1723 */     int hash = original.getHash();
/* 1724 */     return segmentFor(hash).copyEntry(original, newNext);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   ValueReference<K, V> newValueReference(ReferenceEntry<K, V> entry, V value, int weight) {
/* 1733 */     int hash = entry.getHash();
/* 1734 */     return this.valueStrength.referenceValue(segmentFor(hash), entry, (V)Preconditions.checkNotNull(value), weight);
/*      */   }
/*      */   
/*      */   int hash(@CheckForNull Object key) {
/* 1738 */     int h = this.keyEquivalence.hash(key);
/* 1739 */     return rehash(h);
/*      */   }
/*      */   
/*      */   void reclaimValue(ValueReference<K, V> valueReference) {
/* 1743 */     ReferenceEntry<K, V> entry = valueReference.getEntry();
/* 1744 */     int hash = entry.getHash();
/* 1745 */     segmentFor(hash).reclaimValue(entry.getKey(), hash, valueReference);
/*      */   }
/*      */   
/*      */   void reclaimKey(ReferenceEntry<K, V> entry) {
/* 1749 */     int hash = entry.getHash();
/* 1750 */     segmentFor(hash).reclaimKey(entry, hash);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   boolean isLive(ReferenceEntry<K, V> entry, long now) {
/* 1759 */     return (segmentFor(entry.getHash()).getLiveValue(entry, now) != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Segment<K, V> segmentFor(int hash) {
/* 1770 */     return this.segments[hash >>> this.segmentShift & this.segmentMask];
/*      */   }
/*      */ 
/*      */   
/*      */   Segment<K, V> createSegment(int initialCapacity, long maxSegmentWeight, AbstractCache.StatsCounter statsCounter) {
/* 1775 */     return new Segment<>(this, initialCapacity, maxSegmentWeight, statsCounter);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   V getLiveValue(ReferenceEntry<K, V> entry, long now) {
/* 1786 */     if (entry.getKey() == null) {
/* 1787 */       return null;
/*      */     }
/* 1789 */     V value = (V)entry.getValueReference().get();
/* 1790 */     if (value == null) {
/* 1791 */       return null;
/*      */     }
/*      */     
/* 1794 */     if (isExpired(entry, now)) {
/* 1795 */       return null;
/*      */     }
/* 1797 */     return value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isExpired(ReferenceEntry<K, V> entry, long now) {
/* 1804 */     Preconditions.checkNotNull(entry);
/* 1805 */     if (expiresAfterAccess() && now - entry.getAccessTime() >= this.expireAfterAccessNanos) {
/* 1806 */       return true;
/*      */     }
/* 1808 */     if (expiresAfterWrite() && now - entry.getWriteTime() >= this.expireAfterWriteNanos) {
/* 1809 */       return true;
/*      */     }
/* 1811 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> void connectAccessOrder(ReferenceEntry<K, V> previous, ReferenceEntry<K, V> next) {
/* 1818 */     previous.setNextInAccessQueue(next);
/* 1819 */     next.setPreviousInAccessQueue(previous);
/*      */   }
/*      */ 
/*      */   
/*      */   static <K, V> void nullifyAccessOrder(ReferenceEntry<K, V> nulled) {
/* 1824 */     ReferenceEntry<K, V> nullEntry = nullEntry();
/* 1825 */     nulled.setNextInAccessQueue(nullEntry);
/* 1826 */     nulled.setPreviousInAccessQueue(nullEntry);
/*      */   }
/*      */ 
/*      */   
/*      */   static <K, V> void connectWriteOrder(ReferenceEntry<K, V> previous, ReferenceEntry<K, V> next) {
/* 1831 */     previous.setNextInWriteQueue(next);
/* 1832 */     next.setPreviousInWriteQueue(previous);
/*      */   }
/*      */ 
/*      */   
/*      */   static <K, V> void nullifyWriteOrder(ReferenceEntry<K, V> nulled) {
/* 1837 */     ReferenceEntry<K, V> nullEntry = nullEntry();
/* 1838 */     nulled.setNextInWriteQueue(nullEntry);
/* 1839 */     nulled.setPreviousInWriteQueue(nullEntry);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void processPendingNotifications() {
/*      */     RemovalNotification<K, V> notification;
/* 1849 */     while ((notification = this.removalNotificationQueue.poll()) != null) {
/*      */       try {
/* 1851 */         this.removalListener.onRemoval(notification);
/* 1852 */       } catch (Throwable e) {
/* 1853 */         logger.log(Level.WARNING, "Exception thrown by removal listener", e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   final Segment<K, V>[] newSegmentArray(int ssize) {
/* 1860 */     return (Segment<K, V>[])new Segment[ssize];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class Segment<K, V>
/*      */     extends ReentrantLock
/*      */   {
/*      */     @Weak
/*      */     final LocalCache<K, V> map;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     volatile int count;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     long totalWeight;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int modCount;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int threshold;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     volatile AtomicReferenceArray<ReferenceEntry<K, V>> table;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final long maxSegmentWeight;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     final ReferenceQueue<K> keyReferenceQueue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     final ReferenceQueue<V> valueReferenceQueue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final Queue<ReferenceEntry<K, V>> recencyQueue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1957 */     final AtomicInteger readCount = new AtomicInteger();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     final Queue<ReferenceEntry<K, V>> writeQueue;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     final Queue<ReferenceEntry<K, V>> accessQueue;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final AbstractCache.StatsCounter statsCounter;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Segment(LocalCache<K, V> map, int initialCapacity, long maxSegmentWeight, AbstractCache.StatsCounter statsCounter) {
/* 1981 */       this.map = map;
/* 1982 */       this.maxSegmentWeight = maxSegmentWeight;
/* 1983 */       this.statsCounter = (AbstractCache.StatsCounter)Preconditions.checkNotNull(statsCounter);
/* 1984 */       initTable(newEntryArray(initialCapacity));
/*      */       
/* 1986 */       this.keyReferenceQueue = map.usesKeyReferences() ? new ReferenceQueue<>() : null;
/*      */       
/* 1988 */       this.valueReferenceQueue = map.usesValueReferences() ? new ReferenceQueue<>() : null;
/*      */       
/* 1990 */       this
/* 1991 */         .recencyQueue = map.usesAccessQueue() ? new ConcurrentLinkedQueue<>() : LocalCache.<ReferenceEntry<K, V>>discardingQueue();
/*      */       
/* 1993 */       this.writeQueue = map.usesWriteQueue() ? new LocalCache.WriteQueue<>() : LocalCache.<ReferenceEntry<K, V>>discardingQueue();
/*      */       
/* 1995 */       this.accessQueue = map.usesAccessQueue() ? new LocalCache.AccessQueue<>() : LocalCache.<ReferenceEntry<K, V>>discardingQueue();
/*      */     }
/*      */     
/*      */     AtomicReferenceArray<ReferenceEntry<K, V>> newEntryArray(int size) {
/* 1999 */       return new AtomicReferenceArray<>(size);
/*      */     }
/*      */     
/*      */     void initTable(AtomicReferenceArray<ReferenceEntry<K, V>> newTable) {
/* 2003 */       this.threshold = newTable.length() * 3 / 4;
/* 2004 */       if (!this.map.customWeigher() && this.threshold == this.maxSegmentWeight)
/*      */       {
/* 2006 */         this.threshold++;
/*      */       }
/* 2008 */       this.table = newTable;
/*      */     }
/*      */     
/*      */     @GuardedBy("this")
/*      */     ReferenceEntry<K, V> newEntry(K key, int hash, @CheckForNull ReferenceEntry<K, V> next) {
/* 2013 */       return this.map.entryFactory.newEntry(this, (K)Preconditions.checkNotNull(key), hash, next);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     @GuardedBy("this")
/*      */     ReferenceEntry<K, V> copyEntry(ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
/* 2023 */       K key = original.getKey();
/* 2024 */       if (key == null)
/*      */       {
/* 2026 */         return null;
/*      */       }
/*      */       
/* 2029 */       LocalCache.ValueReference<K, V> valueReference = original.getValueReference();
/* 2030 */       V value = valueReference.get();
/* 2031 */       if (value == null && valueReference.isActive())
/*      */       {
/* 2033 */         return null;
/*      */       }
/*      */       
/* 2036 */       ReferenceEntry<K, V> newEntry = this.map.entryFactory.copyEntry(this, original, newNext, key);
/* 2037 */       newEntry.setValueReference(valueReference.copyFor(this.valueReferenceQueue, value, newEntry));
/* 2038 */       return newEntry;
/*      */     }
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void setValue(ReferenceEntry<K, V> entry, K key, V value, long now) {
/* 2044 */       LocalCache.ValueReference<K, V> previous = entry.getValueReference();
/* 2045 */       int weight = this.map.weigher.weigh(key, value);
/* 2046 */       Preconditions.checkState((weight >= 0), "Weights must be non-negative");
/*      */ 
/*      */       
/* 2049 */       LocalCache.ValueReference<K, V> valueReference = this.map.valueStrength.referenceValue(this, entry, value, weight);
/* 2050 */       entry.setValueReference(valueReference);
/* 2051 */       recordWrite(entry, weight, now);
/* 2052 */       previous.notifyNewValue(value);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     V get(K key, int hash, CacheLoader<? super K, V> loader) throws ExecutionException {
/* 2059 */       Preconditions.checkNotNull(key);
/* 2060 */       Preconditions.checkNotNull(loader);
/*      */       try {
/* 2062 */         if (this.count != 0) {
/*      */           
/* 2064 */           ReferenceEntry<K, V> e = getEntry(key, hash);
/* 2065 */           if (e != null) {
/* 2066 */             long now = this.map.ticker.read();
/* 2067 */             V value = getLiveValue(e, now);
/* 2068 */             if (value != null) {
/* 2069 */               recordRead(e, now);
/* 2070 */               this.statsCounter.recordHits(1);
/* 2071 */               return scheduleRefresh(e, key, hash, value, now, loader);
/*      */             } 
/* 2073 */             LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
/* 2074 */             if (valueReference.isLoading()) {
/* 2075 */               return waitForLoadingValue(e, key, valueReference);
/*      */             }
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/* 2081 */         return lockedGetOrLoad(key, hash, loader);
/* 2082 */       } catch (ExecutionException ee) {
/* 2083 */         Throwable cause = ee.getCause();
/* 2084 */         if (cause instanceof Error)
/* 2085 */           throw new ExecutionError((Error)cause); 
/* 2086 */         if (cause instanceof RuntimeException) {
/* 2087 */           throw new UncheckedExecutionException(cause);
/*      */         }
/* 2089 */         throw ee;
/*      */       } finally {
/* 2091 */         postReadCleanup();
/*      */       } 
/*      */     }
/*      */     
/*      */     @CheckForNull
/*      */     V get(Object key, int hash) {
/*      */       try {
/* 2098 */         if (this.count != 0) {
/* 2099 */           long now = this.map.ticker.read();
/* 2100 */           ReferenceEntry<K, V> e = getLiveEntry(key, hash, now);
/* 2101 */           if (e == null) {
/* 2102 */             return null;
/*      */           }
/*      */           
/* 2105 */           V value = (V)e.getValueReference().get();
/* 2106 */           if (value != null) {
/* 2107 */             recordRead(e, now);
/* 2108 */             return scheduleRefresh(e, e.getKey(), hash, value, now, this.map.defaultLoader);
/*      */           } 
/* 2110 */           tryDrainReferenceQueues();
/*      */         } 
/* 2112 */         return null;
/*      */       } finally {
/* 2114 */         postReadCleanup();
/*      */       } 
/*      */     }
/*      */     
/*      */     V lockedGetOrLoad(K key, int hash, CacheLoader<? super K, V> loader) throws ExecutionException {
/*      */       ReferenceEntry<K, V> e;
/* 2120 */       LocalCache.ValueReference<K, V> valueReference = null;
/* 2121 */       LocalCache.LoadingValueReference<K, V> loadingValueReference = null;
/* 2122 */       boolean createNewEntry = true;
/*      */       
/* 2124 */       lock();
/*      */       
/*      */       try {
/* 2127 */         long now = this.map.ticker.read();
/* 2128 */         preWriteCleanup(now);
/*      */         
/* 2130 */         int newCount = this.count - 1;
/* 2131 */         AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
/* 2132 */         int index = hash & table.length() - 1;
/* 2133 */         ReferenceEntry<K, V> first = table.get(index);
/*      */         
/* 2135 */         for (e = first; e != null; e = e.getNext()) {
/* 2136 */           K entryKey = e.getKey();
/* 2137 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 2139 */             .equivalent(key, entryKey)) {
/* 2140 */             valueReference = e.getValueReference();
/* 2141 */             if (valueReference.isLoading()) {
/* 2142 */               createNewEntry = false; break;
/*      */             } 
/* 2144 */             V value = valueReference.get();
/* 2145 */             if (value == null) {
/* 2146 */               enqueueNotification(entryKey, hash, value, valueReference
/* 2147 */                   .getWeight(), RemovalCause.COLLECTED);
/* 2148 */             } else if (this.map.isExpired(e, now)) {
/*      */ 
/*      */               
/* 2151 */               enqueueNotification(entryKey, hash, value, valueReference
/* 2152 */                   .getWeight(), RemovalCause.EXPIRED);
/*      */             } else {
/* 2154 */               recordLockedRead(e, now);
/* 2155 */               this.statsCounter.recordHits(1);
/*      */               
/* 2157 */               return value;
/*      */             } 
/*      */ 
/*      */             
/* 2161 */             this.writeQueue.remove(e);
/* 2162 */             this.accessQueue.remove(e);
/* 2163 */             this.count = newCount;
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/*      */         
/* 2169 */         if (createNewEntry) {
/* 2170 */           loadingValueReference = new LocalCache.LoadingValueReference<>();
/*      */           
/* 2172 */           if (e == null) {
/* 2173 */             e = newEntry(key, hash, first);
/* 2174 */             e.setValueReference(loadingValueReference);
/* 2175 */             table.set(index, e);
/*      */           } else {
/* 2177 */             e.setValueReference(loadingValueReference);
/*      */           } 
/*      */         } 
/*      */       } finally {
/* 2181 */         unlock();
/* 2182 */         postWriteCleanup();
/*      */       } 
/*      */       
/* 2185 */       if (createNewEntry) {
/*      */         try {
/* 2187 */           return loadSync(key, hash, loadingValueReference, loader);
/*      */         } finally {
/* 2189 */           this.statsCounter.recordMisses(1);
/*      */         } 
/*      */       }
/*      */       
/* 2193 */       return waitForLoadingValue(e, key, valueReference);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     V waitForLoadingValue(ReferenceEntry<K, V> e, K key, LocalCache.ValueReference<K, V> valueReference) throws ExecutionException {
/* 2199 */       if (!valueReference.isLoading()) {
/* 2200 */         throw new AssertionError();
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2208 */       if (valueReference instanceof LocalCache.LoadingValueReference)
/*      */       {
/*      */ 
/*      */         
/* 2212 */         Preconditions.checkState(
/*      */             
/* 2214 */             (((LocalCache.LoadingValueReference)valueReference).getLoadingThread() != Thread.currentThread()), "Recursive load of: %s", key);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/* 2221 */         V value = valueReference.waitForValue();
/* 2222 */         if (value == null) {
/* 2223 */           throw new CacheLoader.InvalidCacheLoadException("CacheLoader returned null for key " + key + ".");
/*      */         }
/*      */         
/* 2226 */         long now = this.map.ticker.read();
/* 2227 */         recordRead(e, now);
/* 2228 */         return value;
/*      */       } finally {
/* 2230 */         this.statsCounter.recordMisses(1);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     V compute(K key, int hash, BiFunction<? super K, ? super V, ? extends V> function) {
/* 2240 */       LocalCache.ValueReference<K, V> valueReference = null;
/* 2241 */       LocalCache.ComputingValueReference<K, V> computingValueReference = null;
/* 2242 */       boolean createNewEntry = true;
/*      */ 
/*      */       
/* 2245 */       lock();
/*      */       
/*      */       try {
/* 2248 */         long now = this.map.ticker.read();
/* 2249 */         preWriteCleanup(now);
/*      */         
/* 2251 */         AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
/* 2252 */         int index = hash & table.length() - 1;
/* 2253 */         ReferenceEntry<K, V> first = table.get(index);
/*      */         ReferenceEntry<K, V> e;
/* 2255 */         for (e = first; e != null; e = e.getNext()) {
/* 2256 */           K entryKey = e.getKey();
/* 2257 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 2259 */             .equivalent(key, entryKey)) {
/* 2260 */             valueReference = e.getValueReference();
/* 2261 */             if (this.map.isExpired(e, now))
/*      */             {
/*      */               
/* 2264 */               enqueueNotification(entryKey, hash, valueReference
/*      */ 
/*      */                   
/* 2267 */                   .get(), valueReference
/* 2268 */                   .getWeight(), RemovalCause.EXPIRED);
/*      */             }
/*      */ 
/*      */ 
/*      */             
/* 2273 */             this.writeQueue.remove(e);
/* 2274 */             this.accessQueue.remove(e);
/* 2275 */             createNewEntry = false;
/*      */ 
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/*      */         
/* 2282 */         computingValueReference = new LocalCache.ComputingValueReference<>(valueReference);
/*      */         
/* 2284 */         if (e == null) {
/* 2285 */           createNewEntry = true;
/* 2286 */           e = newEntry(key, hash, first);
/* 2287 */           e.setValueReference(computingValueReference);
/* 2288 */           table.set(index, e);
/*      */         } else {
/* 2290 */           e.setValueReference(computingValueReference);
/*      */         } 
/*      */         
/* 2293 */         V newValue = computingValueReference.compute(key, function);
/* 2294 */         if (newValue != null) {
/* 2295 */           if (valueReference != null && newValue == valueReference.get()) {
/* 2296 */             computingValueReference.set(newValue);
/* 2297 */             e.setValueReference(valueReference);
/* 2298 */             recordWrite(e, 0, now);
/* 2299 */             return newValue;
/*      */           } 
/*      */           try {
/* 2302 */             return getAndRecordStats(key, hash, computingValueReference, 
/* 2303 */                 Futures.immediateFuture(newValue));
/* 2304 */           } catch (ExecutionException exception) {
/* 2305 */             throw new AssertionError("impossible; Futures.immediateFuture can't throw");
/*      */           } 
/* 2307 */         }  if (createNewEntry || valueReference.isLoading()) {
/* 2308 */           removeLoadingValue(key, hash, computingValueReference);
/* 2309 */           return null;
/*      */         } 
/* 2311 */         removeEntry(e, hash, RemovalCause.EXPLICIT);
/* 2312 */         return null;
/*      */       } finally {
/*      */         
/* 2315 */         unlock();
/* 2316 */         postWriteCleanup();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     V loadSync(K key, int hash, LocalCache.LoadingValueReference<K, V> loadingValueReference, CacheLoader<? super K, V> loader) throws ExecutionException {
/* 2328 */       ListenableFuture<V> loadingFuture = loadingValueReference.loadFuture(key, loader);
/* 2329 */       return getAndRecordStats(key, hash, loadingValueReference, loadingFuture);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     ListenableFuture<V> loadAsync(K key, int hash, LocalCache.LoadingValueReference<K, V> loadingValueReference, CacheLoader<? super K, V> loader) {
/* 2337 */       ListenableFuture<V> loadingFuture = loadingValueReference.loadFuture(key, loader);
/* 2338 */       loadingFuture.addListener(() -> {
/*      */             
/*      */             try {
/*      */               getAndRecordStats((K)key, hash, loadingValueReference, loadingFuture);
/* 2342 */             } catch (Throwable t) {
/*      */               LocalCache.logger.log(Level.WARNING, "Exception thrown during refresh", t);
/*      */               
/*      */               loadingValueReference.setException(t);
/*      */             } 
/* 2347 */           }MoreExecutors.directExecutor());
/* 2348 */       return loadingFuture;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     V getAndRecordStats(K key, int hash, LocalCache.LoadingValueReference<K, V> loadingValueReference, ListenableFuture<V> newValue) throws ExecutionException {
/* 2359 */       V value = null;
/*      */       try {
/* 2361 */         value = (V)Uninterruptibles.getUninterruptibly((Future)newValue);
/* 2362 */         if (value == null) {
/* 2363 */           throw new CacheLoader.InvalidCacheLoadException("CacheLoader returned null for key " + key + ".");
/*      */         }
/* 2365 */         this.statsCounter.recordLoadSuccess(loadingValueReference.elapsedNanos());
/* 2366 */         storeLoadedValue(key, hash, loadingValueReference, value);
/* 2367 */         return value;
/*      */       } finally {
/* 2369 */         if (value == null) {
/* 2370 */           this.statsCounter.recordLoadException(loadingValueReference.elapsedNanos());
/* 2371 */           removeLoadingValue(key, hash, loadingValueReference);
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     V scheduleRefresh(ReferenceEntry<K, V> entry, K key, int hash, V oldValue, long now, CacheLoader<? super K, V> loader) {
/* 2383 */       if (this.map.refreshes() && now - entry
/* 2384 */         .getWriteTime() > this.map.refreshNanos && 
/* 2385 */         !entry.getValueReference().isLoading()) {
/* 2386 */         V newValue = refresh(key, hash, loader, true);
/* 2387 */         if (newValue != null) {
/* 2388 */           return newValue;
/*      */         }
/*      */       } 
/* 2391 */       return oldValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     @CanIgnoreReturnValue
/*      */     V refresh(K key, int hash, CacheLoader<? super K, V> loader, boolean checkTime) {
/* 2404 */       LocalCache.LoadingValueReference<K, V> loadingValueReference = insertLoadingValueReference(key, hash, checkTime);
/* 2405 */       if (loadingValueReference == null) {
/* 2406 */         return null;
/*      */       }
/*      */       
/* 2409 */       ListenableFuture<V> result = loadAsync(key, hash, loadingValueReference, loader);
/* 2410 */       if (result.isDone()) {
/*      */         try {
/* 2412 */           return (V)Uninterruptibles.getUninterruptibly((Future)result);
/* 2413 */         } catch (Throwable throwable) {}
/*      */       }
/*      */ 
/*      */       
/* 2417 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     LocalCache.LoadingValueReference<K, V> insertLoadingValueReference(K key, int hash, boolean checkTime) {
/* 2427 */       ReferenceEntry<K, V> e = null;
/* 2428 */       lock();
/*      */       try {
/* 2430 */         long now = this.map.ticker.read();
/* 2431 */         preWriteCleanup(now);
/*      */         
/* 2433 */         AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
/* 2434 */         int index = hash & table.length() - 1;
/* 2435 */         ReferenceEntry<K, V> first = table.get(index);
/*      */ 
/*      */         
/* 2438 */         for (e = first; e != null; e = e.getNext()) {
/* 2439 */           K entryKey = e.getKey();
/* 2440 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 2442 */             .equivalent(key, entryKey)) {
/*      */ 
/*      */             
/* 2445 */             LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
/* 2446 */             if (valueReference.isLoading() || (checkTime && now - e
/* 2447 */               .getWriteTime() < this.map.refreshNanos))
/*      */             {
/*      */ 
/*      */               
/* 2451 */               return null;
/*      */             }
/*      */ 
/*      */             
/* 2455 */             this.modCount++;
/* 2456 */             LocalCache.LoadingValueReference<K, V> loadingValueReference1 = new LocalCache.LoadingValueReference<>(valueReference);
/*      */             
/* 2458 */             e.setValueReference(loadingValueReference1);
/* 2459 */             return loadingValueReference1;
/*      */           } 
/*      */         } 
/*      */         
/* 2463 */         this.modCount++;
/* 2464 */         LocalCache.LoadingValueReference<K, V> loadingValueReference = new LocalCache.LoadingValueReference<>();
/* 2465 */         e = newEntry(key, hash, first);
/* 2466 */         e.setValueReference(loadingValueReference);
/* 2467 */         table.set(index, e);
/* 2468 */         return loadingValueReference;
/*      */       } finally {
/* 2470 */         unlock();
/* 2471 */         postWriteCleanup();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void tryDrainReferenceQueues() {
/* 2479 */       if (tryLock()) {
/*      */         try {
/* 2481 */           drainReferenceQueues();
/*      */         } finally {
/* 2483 */           unlock();
/*      */         } 
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void drainReferenceQueues() {
/* 2494 */       if (this.map.usesKeyReferences()) {
/* 2495 */         drainKeyReferenceQueue();
/*      */       }
/* 2497 */       if (this.map.usesValueReferences()) {
/* 2498 */         drainValueReferenceQueue();
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void drainKeyReferenceQueue() {
/* 2505 */       int i = 0; Reference<? extends K> ref;
/* 2506 */       while ((ref = this.keyReferenceQueue.poll()) != null) {
/*      */         
/* 2508 */         ReferenceEntry<K, V> entry = (ReferenceEntry)ref;
/* 2509 */         this.map.reclaimKey(entry);
/* 2510 */         if (++i == 16) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void drainValueReferenceQueue() {
/* 2519 */       int i = 0; Reference<? extends V> ref;
/* 2520 */       while ((ref = this.valueReferenceQueue.poll()) != null) {
/*      */         
/* 2522 */         LocalCache.ValueReference<K, V> valueReference = (LocalCache.ValueReference)ref;
/* 2523 */         this.map.reclaimValue(valueReference);
/* 2524 */         if (++i == 16) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     void clearReferenceQueues() {
/* 2532 */       if (this.map.usesKeyReferences()) {
/* 2533 */         clearKeyReferenceQueue();
/*      */       }
/* 2535 */       if (this.map.usesValueReferences()) {
/* 2536 */         clearValueReferenceQueue();
/*      */       }
/*      */     }
/*      */     
/*      */     void clearKeyReferenceQueue() {
/* 2541 */       while (this.keyReferenceQueue.poll() != null);
/*      */     }
/*      */     
/*      */     void clearValueReferenceQueue() {
/* 2545 */       while (this.valueReferenceQueue.poll() != null);
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
/*      */     void recordRead(ReferenceEntry<K, V> entry, long now) {
/* 2558 */       if (this.map.recordsAccess()) {
/* 2559 */         entry.setAccessTime(now);
/*      */       }
/* 2561 */       this.recencyQueue.add(entry);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void recordLockedRead(ReferenceEntry<K, V> entry, long now) {
/* 2573 */       if (this.map.recordsAccess()) {
/* 2574 */         entry.setAccessTime(now);
/*      */       }
/* 2576 */       this.accessQueue.add(entry);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void recordWrite(ReferenceEntry<K, V> entry, int weight, long now) {
/* 2586 */       drainRecencyQueue();
/* 2587 */       this.totalWeight += weight;
/*      */       
/* 2589 */       if (this.map.recordsAccess()) {
/* 2590 */         entry.setAccessTime(now);
/*      */       }
/* 2592 */       if (this.map.recordsWrite()) {
/* 2593 */         entry.setWriteTime(now);
/*      */       }
/* 2595 */       this.accessQueue.add(entry);
/* 2596 */       this.writeQueue.add(entry);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void drainRecencyQueue() {
/*      */       ReferenceEntry<K, V> e;
/* 2608 */       while ((e = this.recencyQueue.poll()) != null) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2613 */         if (this.accessQueue.contains(e)) {
/* 2614 */           this.accessQueue.add(e);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void tryExpireEntries(long now) {
/* 2623 */       if (tryLock()) {
/*      */         try {
/* 2625 */           expireEntries(now);
/*      */         } finally {
/* 2627 */           unlock();
/*      */         } 
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void expireEntries(long now) {
/* 2635 */       drainRecencyQueue();
/*      */       
/*      */       ReferenceEntry<K, V> e;
/* 2638 */       while ((e = this.writeQueue.peek()) != null && this.map.isExpired(e, now)) {
/* 2639 */         if (!removeEntry(e, e.getHash(), RemovalCause.EXPIRED)) {
/* 2640 */           throw new AssertionError();
/*      */         }
/*      */       } 
/* 2643 */       while ((e = this.accessQueue.peek()) != null && this.map.isExpired(e, now)) {
/* 2644 */         if (!removeEntry(e, e.getHash(), RemovalCause.EXPIRED)) {
/* 2645 */           throw new AssertionError();
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void enqueueNotification(@CheckForNull K key, int hash, @CheckForNull V value, int weight, RemovalCause cause) {
/* 2655 */       this.totalWeight -= weight;
/* 2656 */       if (cause.wasEvicted()) {
/* 2657 */         this.statsCounter.recordEviction();
/*      */       }
/* 2659 */       if (this.map.removalNotificationQueue != LocalCache.DISCARDING_QUEUE) {
/* 2660 */         RemovalNotification<K, V> notification = RemovalNotification.create(key, value, cause);
/* 2661 */         this.map.removalNotificationQueue.offer(notification);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void evictEntries(ReferenceEntry<K, V> newest) {
/* 2673 */       if (!this.map.evictsBySize()) {
/*      */         return;
/*      */       }
/*      */       
/* 2677 */       drainRecencyQueue();
/*      */ 
/*      */ 
/*      */       
/* 2681 */       if (newest.getValueReference().getWeight() > this.maxSegmentWeight && 
/* 2682 */         !removeEntry(newest, newest.getHash(), RemovalCause.SIZE)) {
/* 2683 */         throw new AssertionError();
/*      */       }
/*      */ 
/*      */       
/* 2687 */       while (this.totalWeight > this.maxSegmentWeight) {
/* 2688 */         ReferenceEntry<K, V> e = getNextEvictable();
/* 2689 */         if (!removeEntry(e, e.getHash(), RemovalCause.SIZE)) {
/* 2690 */           throw new AssertionError();
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     ReferenceEntry<K, V> getNextEvictable() {
/* 2698 */       for (ReferenceEntry<K, V> e : this.accessQueue) {
/* 2699 */         int weight = e.getValueReference().getWeight();
/* 2700 */         if (weight > 0) {
/* 2701 */           return e;
/*      */         }
/*      */       } 
/* 2704 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     ReferenceEntry<K, V> getFirst(int hash) {
/* 2710 */       AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
/* 2711 */       return table.get(hash & table.length() - 1);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     ReferenceEntry<K, V> getEntry(Object key, int hash) {
/* 2718 */       for (ReferenceEntry<K, V> e = getFirst(hash); e != null; e = e.getNext()) {
/* 2719 */         if (e.getHash() == hash) {
/*      */ 
/*      */ 
/*      */           
/* 2723 */           K entryKey = e.getKey();
/* 2724 */           if (entryKey == null) {
/* 2725 */             tryDrainReferenceQueues();
/*      */ 
/*      */           
/*      */           }
/* 2729 */           else if (this.map.keyEquivalence.equivalent(key, entryKey)) {
/* 2730 */             return e;
/*      */           } 
/*      */         } 
/*      */       } 
/* 2734 */       return null;
/*      */     }
/*      */     
/*      */     @CheckForNull
/*      */     ReferenceEntry<K, V> getLiveEntry(Object key, int hash, long now) {
/* 2739 */       ReferenceEntry<K, V> e = getEntry(key, hash);
/* 2740 */       if (e == null)
/* 2741 */         return null; 
/* 2742 */       if (this.map.isExpired(e, now)) {
/* 2743 */         tryExpireEntries(now);
/* 2744 */         return null;
/*      */       } 
/* 2746 */       return e;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     V getLiveValue(ReferenceEntry<K, V> entry, long now) {
/* 2754 */       if (entry.getKey() == null) {
/* 2755 */         tryDrainReferenceQueues();
/* 2756 */         return null;
/*      */       } 
/* 2758 */       V value = (V)entry.getValueReference().get();
/* 2759 */       if (value == null) {
/* 2760 */         tryDrainReferenceQueues();
/* 2761 */         return null;
/*      */       } 
/*      */       
/* 2764 */       if (this.map.isExpired(entry, now)) {
/* 2765 */         tryExpireEntries(now);
/* 2766 */         return null;
/*      */       } 
/* 2768 */       return value;
/*      */     }
/*      */     
/*      */     boolean containsKey(Object key, int hash) {
/*      */       try {
/* 2773 */         if (this.count != 0) {
/* 2774 */           long now = this.map.ticker.read();
/* 2775 */           ReferenceEntry<K, V> e = getLiveEntry(key, hash, now);
/* 2776 */           if (e == null) {
/* 2777 */             return false;
/*      */           }
/* 2779 */           return (e.getValueReference().get() != null);
/*      */         } 
/*      */         
/* 2782 */         return false;
/*      */       } finally {
/* 2784 */         postReadCleanup();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @VisibleForTesting
/*      */     boolean containsValue(Object value) {
/*      */       try {
/* 2795 */         if (this.count != 0) {
/* 2796 */           long now = this.map.ticker.read();
/* 2797 */           AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
/* 2798 */           int length = table.length();
/* 2799 */           for (int i = 0; i < length; i++) {
/* 2800 */             for (ReferenceEntry<K, V> e = table.get(i); e != null; e = e.getNext()) {
/* 2801 */               V entryValue = getLiveValue(e, now);
/* 2802 */               if (entryValue != null)
/*      */               {
/*      */                 
/* 2805 */                 if (this.map.valueEquivalence.equivalent(value, entryValue)) {
/* 2806 */                   return true;
/*      */                 }
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } 
/* 2812 */         return false;
/*      */       } finally {
/* 2814 */         postReadCleanup();
/*      */       } 
/*      */     }
/*      */     
/*      */     @CheckForNull
/*      */     @CanIgnoreReturnValue
/*      */     V put(K key, int hash, V value, boolean onlyIfAbsent) {
/* 2821 */       lock();
/*      */       try {
/* 2823 */         long now = this.map.ticker.read();
/* 2824 */         preWriteCleanup(now);
/*      */         
/* 2826 */         int newCount = this.count + 1;
/* 2827 */         if (newCount > this.threshold) {
/* 2828 */           expand();
/* 2829 */           newCount = this.count + 1;
/*      */         } 
/*      */         
/* 2832 */         AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
/* 2833 */         int index = hash & table.length() - 1;
/* 2834 */         ReferenceEntry<K, V> first = table.get(index);
/*      */ 
/*      */         
/* 2837 */         for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
/* 2838 */           K entryKey = e.getKey();
/* 2839 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 2841 */             .equivalent(key, entryKey)) {
/*      */ 
/*      */             
/* 2844 */             LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
/* 2845 */             V entryValue = valueReference.get();
/*      */             
/* 2847 */             if (entryValue == null) {
/* 2848 */               this.modCount++;
/* 2849 */               if (valueReference.isActive()) {
/* 2850 */                 enqueueNotification(key, hash, entryValue, valueReference
/* 2851 */                     .getWeight(), RemovalCause.COLLECTED);
/* 2852 */                 setValue(e, key, value, now);
/* 2853 */                 newCount = this.count;
/*      */               } else {
/* 2855 */                 setValue(e, key, value, now);
/* 2856 */                 newCount = this.count + 1;
/*      */               } 
/* 2858 */               this.count = newCount;
/* 2859 */               evictEntries(e);
/* 2860 */               return null;
/* 2861 */             }  if (onlyIfAbsent) {
/*      */ 
/*      */ 
/*      */               
/* 2865 */               recordLockedRead(e, now);
/* 2866 */               return entryValue;
/*      */             } 
/*      */             
/* 2869 */             this.modCount++;
/* 2870 */             enqueueNotification(key, hash, entryValue, valueReference
/* 2871 */                 .getWeight(), RemovalCause.REPLACED);
/* 2872 */             setValue(e, key, value, now);
/* 2873 */             evictEntries(e);
/* 2874 */             return entryValue;
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 2880 */         this.modCount++;
/* 2881 */         ReferenceEntry<K, V> newEntry = newEntry(key, hash, first);
/* 2882 */         setValue(newEntry, key, value, now);
/* 2883 */         table.set(index, newEntry);
/* 2884 */         newCount = this.count + 1;
/* 2885 */         this.count = newCount;
/* 2886 */         evictEntries(newEntry);
/* 2887 */         return null;
/*      */       } finally {
/* 2889 */         unlock();
/* 2890 */         postWriteCleanup();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void expand() {
/* 2897 */       AtomicReferenceArray<ReferenceEntry<K, V>> oldTable = this.table;
/* 2898 */       int oldCapacity = oldTable.length();
/* 2899 */       if (oldCapacity >= 1073741824) {
/*      */         return;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2913 */       int newCount = this.count;
/* 2914 */       AtomicReferenceArray<ReferenceEntry<K, V>> newTable = newEntryArray(oldCapacity << 1);
/* 2915 */       this.threshold = newTable.length() * 3 / 4;
/* 2916 */       int newMask = newTable.length() - 1;
/* 2917 */       for (int oldIndex = 0; oldIndex < oldCapacity; oldIndex++) {
/*      */ 
/*      */         
/* 2920 */         ReferenceEntry<K, V> head = oldTable.get(oldIndex);
/*      */         
/* 2922 */         if (head != null) {
/* 2923 */           ReferenceEntry<K, V> next = head.getNext();
/* 2924 */           int headIndex = head.getHash() & newMask;
/*      */ 
/*      */           
/* 2927 */           if (next == null) {
/* 2928 */             newTable.set(headIndex, head);
/*      */           
/*      */           }
/*      */           else {
/*      */             
/* 2933 */             ReferenceEntry<K, V> tail = head;
/* 2934 */             int tailIndex = headIndex; ReferenceEntry<K, V> e;
/* 2935 */             for (e = next; e != null; e = e.getNext()) {
/* 2936 */               int newIndex = e.getHash() & newMask;
/* 2937 */               if (newIndex != tailIndex) {
/*      */                 
/* 2939 */                 tailIndex = newIndex;
/* 2940 */                 tail = e;
/*      */               } 
/*      */             } 
/* 2943 */             newTable.set(tailIndex, tail);
/*      */ 
/*      */             
/* 2946 */             for (e = head; e != tail; e = e.getNext()) {
/* 2947 */               int newIndex = e.getHash() & newMask;
/* 2948 */               ReferenceEntry<K, V> newNext = newTable.get(newIndex);
/* 2949 */               ReferenceEntry<K, V> newFirst = copyEntry(e, newNext);
/* 2950 */               if (newFirst != null) {
/* 2951 */                 newTable.set(newIndex, newFirst);
/*      */               } else {
/* 2953 */                 removeCollectedEntry(e);
/* 2954 */                 newCount--;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/* 2960 */       this.table = newTable;
/* 2961 */       this.count = newCount;
/*      */     }
/*      */     
/*      */     boolean replace(K key, int hash, V oldValue, V newValue) {
/* 2965 */       lock();
/*      */       try {
/* 2967 */         long now = this.map.ticker.read();
/* 2968 */         preWriteCleanup(now);
/*      */         
/* 2970 */         AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
/* 2971 */         int index = hash & table.length() - 1;
/* 2972 */         ReferenceEntry<K, V> first = table.get(index);
/*      */         
/* 2974 */         for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
/* 2975 */           K entryKey = e.getKey();
/* 2976 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 2978 */             .equivalent(key, entryKey)) {
/* 2979 */             LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
/* 2980 */             V entryValue = valueReference.get();
/* 2981 */             if (entryValue == null) {
/* 2982 */               if (valueReference.isActive()) {
/*      */                 
/* 2984 */                 int newCount = this.count - 1;
/* 2985 */                 this.modCount++;
/*      */                 
/* 2987 */                 ReferenceEntry<K, V> newFirst = removeValueFromChain(first, e, entryKey, hash, entryValue, valueReference, RemovalCause.COLLECTED);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/* 2995 */                 newCount = this.count - 1;
/* 2996 */                 table.set(index, newFirst);
/* 2997 */                 this.count = newCount;
/*      */               } 
/* 2999 */               return false;
/*      */             } 
/*      */             
/* 3002 */             if (this.map.valueEquivalence.equivalent(oldValue, entryValue)) {
/* 3003 */               this.modCount++;
/* 3004 */               enqueueNotification(key, hash, entryValue, valueReference
/* 3005 */                   .getWeight(), RemovalCause.REPLACED);
/* 3006 */               setValue(e, key, newValue, now);
/* 3007 */               evictEntries(e);
/* 3008 */               return true;
/*      */             } 
/*      */ 
/*      */             
/* 3012 */             recordLockedRead(e, now);
/* 3013 */             return false;
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/* 3018 */         return false;
/*      */       } finally {
/* 3020 */         unlock();
/* 3021 */         postWriteCleanup();
/*      */       } 
/*      */     }
/*      */     
/*      */     @CheckForNull
/*      */     V replace(K key, int hash, V newValue) {
/* 3027 */       lock();
/*      */       try {
/* 3029 */         long now = this.map.ticker.read();
/* 3030 */         preWriteCleanup(now);
/*      */         
/* 3032 */         AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
/* 3033 */         int index = hash & table.length() - 1;
/* 3034 */         ReferenceEntry<K, V> first = table.get(index);
/*      */         ReferenceEntry<K, V> e;
/* 3036 */         for (e = first; e != null; e = e.getNext()) {
/* 3037 */           K entryKey = e.getKey();
/* 3038 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 3040 */             .equivalent(key, entryKey)) {
/* 3041 */             LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
/* 3042 */             V entryValue = valueReference.get();
/* 3043 */             if (entryValue == null) {
/* 3044 */               if (valueReference.isActive()) {
/*      */                 
/* 3046 */                 int newCount = this.count - 1;
/* 3047 */                 this.modCount++;
/*      */                 
/* 3049 */                 ReferenceEntry<K, V> newFirst = removeValueFromChain(first, e, entryKey, hash, entryValue, valueReference, RemovalCause.COLLECTED);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/* 3057 */                 newCount = this.count - 1;
/* 3058 */                 table.set(index, newFirst);
/* 3059 */                 this.count = newCount;
/*      */               } 
/* 3061 */               return null;
/*      */             } 
/*      */             
/* 3064 */             this.modCount++;
/* 3065 */             enqueueNotification(key, hash, entryValue, valueReference
/* 3066 */                 .getWeight(), RemovalCause.REPLACED);
/* 3067 */             setValue(e, key, newValue, now);
/* 3068 */             evictEntries(e);
/* 3069 */             return entryValue;
/*      */           } 
/*      */         } 
/*      */         
/* 3073 */         e = null; return (V)e;
/*      */       } finally {
/* 3075 */         unlock();
/* 3076 */         postWriteCleanup();
/*      */       } 
/*      */     }
/*      */     
/*      */     @CheckForNull
/*      */     V remove(Object key, int hash) {
/* 3082 */       lock();
/*      */       try {
/* 3084 */         long now = this.map.ticker.read();
/* 3085 */         preWriteCleanup(now);
/*      */         
/* 3087 */         int newCount = this.count - 1;
/* 3088 */         AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
/* 3089 */         int index = hash & table.length() - 1;
/* 3090 */         ReferenceEntry<K, V> first = table.get(index);
/*      */         ReferenceEntry<K, V> e;
/* 3092 */         for (e = first; e != null; e = e.getNext()) {
/* 3093 */           K entryKey = e.getKey();
/* 3094 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 3096 */             .equivalent(key, entryKey)) {
/* 3097 */             RemovalCause cause; LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
/* 3098 */             V entryValue = valueReference.get();
/*      */ 
/*      */             
/* 3101 */             if (entryValue != null) {
/* 3102 */               cause = RemovalCause.EXPLICIT;
/* 3103 */             } else if (valueReference.isActive()) {
/* 3104 */               cause = RemovalCause.COLLECTED;
/*      */             } else {
/*      */               
/* 3107 */               return null;
/*      */             } 
/*      */             
/* 3110 */             this.modCount++;
/*      */             
/* 3112 */             ReferenceEntry<K, V> newFirst = removeValueFromChain(first, e, entryKey, hash, entryValue, valueReference, cause);
/* 3113 */             newCount = this.count - 1;
/* 3114 */             table.set(index, newFirst);
/* 3115 */             this.count = newCount;
/* 3116 */             return entryValue;
/*      */           } 
/*      */         } 
/*      */         
/* 3120 */         e = null; return (V)e;
/*      */       } finally {
/* 3122 */         unlock();
/* 3123 */         postWriteCleanup();
/*      */       } 
/*      */     }
/*      */     
/*      */     boolean remove(Object key, int hash, Object value) {
/* 3128 */       lock();
/*      */       try {
/* 3130 */         long now = this.map.ticker.read();
/* 3131 */         preWriteCleanup(now);
/*      */         
/* 3133 */         int newCount = this.count - 1;
/* 3134 */         AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
/* 3135 */         int index = hash & table.length() - 1;
/* 3136 */         ReferenceEntry<K, V> first = table.get(index);
/*      */         
/* 3138 */         for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
/* 3139 */           K entryKey = e.getKey();
/* 3140 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 3142 */             .equivalent(key, entryKey)) {
/* 3143 */             RemovalCause cause; LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
/* 3144 */             V entryValue = valueReference.get();
/*      */ 
/*      */             
/* 3147 */             if (this.map.valueEquivalence.equivalent(value, entryValue)) {
/* 3148 */               cause = RemovalCause.EXPLICIT;
/* 3149 */             } else if (entryValue == null && valueReference.isActive()) {
/* 3150 */               cause = RemovalCause.COLLECTED;
/*      */             } else {
/*      */               
/* 3153 */               return false;
/*      */             } 
/*      */             
/* 3156 */             this.modCount++;
/*      */             
/* 3158 */             ReferenceEntry<K, V> newFirst = removeValueFromChain(first, e, entryKey, hash, entryValue, valueReference, cause);
/* 3159 */             newCount = this.count - 1;
/* 3160 */             table.set(index, newFirst);
/* 3161 */             this.count = newCount;
/* 3162 */             return (cause == RemovalCause.EXPLICIT);
/*      */           } 
/*      */         } 
/*      */         
/* 3166 */         return false;
/*      */       } finally {
/* 3168 */         unlock();
/* 3169 */         postWriteCleanup();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     boolean storeLoadedValue(K key, int hash, LocalCache.LoadingValueReference<K, V> oldValueReference, V newValue) {
/* 3176 */       lock();
/*      */       try {
/* 3178 */         long now = this.map.ticker.read();
/* 3179 */         preWriteCleanup(now);
/*      */         
/* 3181 */         int newCount = this.count + 1;
/* 3182 */         if (newCount > this.threshold) {
/* 3183 */           expand();
/* 3184 */           newCount = this.count + 1;
/*      */         } 
/*      */         
/* 3187 */         AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
/* 3188 */         int index = hash & table.length() - 1;
/* 3189 */         ReferenceEntry<K, V> first = table.get(index);
/*      */         
/* 3191 */         for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
/* 3192 */           K entryKey = e.getKey();
/* 3193 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 3195 */             .equivalent(key, entryKey)) {
/* 3196 */             LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
/* 3197 */             V entryValue = valueReference.get();
/*      */ 
/*      */             
/* 3200 */             if (oldValueReference == valueReference || (entryValue == null && valueReference != LocalCache.UNSET)) {
/*      */               
/* 3202 */               this.modCount++;
/* 3203 */               if (oldValueReference.isActive()) {
/*      */                 
/* 3205 */                 RemovalCause cause = (entryValue == null) ? RemovalCause.COLLECTED : RemovalCause.REPLACED;
/* 3206 */                 enqueueNotification(key, hash, entryValue, oldValueReference.getWeight(), cause);
/* 3207 */                 newCount--;
/*      */               } 
/* 3209 */               setValue(e, key, newValue, now);
/* 3210 */               this.count = newCount;
/* 3211 */               evictEntries(e);
/* 3212 */               return true;
/*      */             } 
/*      */ 
/*      */             
/* 3216 */             enqueueNotification(key, hash, newValue, 0, RemovalCause.REPLACED);
/* 3217 */             return false;
/*      */           } 
/*      */         } 
/*      */         
/* 3221 */         this.modCount++;
/* 3222 */         ReferenceEntry<K, V> newEntry = newEntry(key, hash, first);
/* 3223 */         setValue(newEntry, key, newValue, now);
/* 3224 */         table.set(index, newEntry);
/* 3225 */         this.count = newCount;
/* 3226 */         evictEntries(newEntry);
/* 3227 */         return true;
/*      */       } finally {
/* 3229 */         unlock();
/* 3230 */         postWriteCleanup();
/*      */       } 
/*      */     }
/*      */     
/*      */     void clear() {
/* 3235 */       if (this.count != 0) {
/* 3236 */         lock();
/*      */         try {
/* 3238 */           long now = this.map.ticker.read();
/* 3239 */           preWriteCleanup(now);
/*      */           
/* 3241 */           AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table; int i;
/* 3242 */           for (i = 0; i < table.length(); i++) {
/* 3243 */             for (ReferenceEntry<K, V> e = table.get(i); e != null; e = e.getNext()) {
/*      */               
/* 3245 */               if (e.getValueReference().isActive()) {
/* 3246 */                 K key = e.getKey();
/* 3247 */                 V value = (V)e.getValueReference().get();
/*      */                 
/* 3249 */                 RemovalCause cause = (key == null || value == null) ? RemovalCause.COLLECTED : RemovalCause.EXPLICIT;
/* 3250 */                 enqueueNotification(key, e
/* 3251 */                     .getHash(), value, e.getValueReference().getWeight(), cause);
/*      */               } 
/*      */             } 
/*      */           } 
/* 3255 */           for (i = 0; i < table.length(); i++) {
/* 3256 */             table.set(i, null);
/*      */           }
/* 3258 */           clearReferenceQueues();
/* 3259 */           this.writeQueue.clear();
/* 3260 */           this.accessQueue.clear();
/* 3261 */           this.readCount.set(0);
/*      */           
/* 3263 */           this.modCount++;
/* 3264 */           this.count = 0;
/*      */         } finally {
/* 3266 */           unlock();
/* 3267 */           postWriteCleanup();
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     @GuardedBy("this")
/*      */     ReferenceEntry<K, V> removeValueFromChain(ReferenceEntry<K, V> first, ReferenceEntry<K, V> entry, @CheckForNull K key, int hash, V value, LocalCache.ValueReference<K, V> valueReference, RemovalCause cause) {
/* 3282 */       enqueueNotification(key, hash, value, valueReference.getWeight(), cause);
/* 3283 */       this.writeQueue.remove(entry);
/* 3284 */       this.accessQueue.remove(entry);
/*      */       
/* 3286 */       if (valueReference.isLoading()) {
/* 3287 */         valueReference.notifyNewValue(null);
/* 3288 */         return first;
/*      */       } 
/* 3290 */       return removeEntryFromChain(first, entry);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     @GuardedBy("this")
/*      */     ReferenceEntry<K, V> removeEntryFromChain(ReferenceEntry<K, V> first, ReferenceEntry<K, V> entry) {
/* 3298 */       int newCount = this.count;
/* 3299 */       ReferenceEntry<K, V> newFirst = entry.getNext();
/* 3300 */       for (ReferenceEntry<K, V> e = first; e != entry; e = e.getNext()) {
/* 3301 */         ReferenceEntry<K, V> next = copyEntry(e, newFirst);
/* 3302 */         if (next != null) {
/* 3303 */           newFirst = next;
/*      */         } else {
/* 3305 */           removeCollectedEntry(e);
/* 3306 */           newCount--;
/*      */         } 
/*      */       } 
/* 3309 */       this.count = newCount;
/* 3310 */       return newFirst;
/*      */     }
/*      */     
/*      */     @GuardedBy("this")
/*      */     void removeCollectedEntry(ReferenceEntry<K, V> entry) {
/* 3315 */       enqueueNotification(entry
/* 3316 */           .getKey(), entry
/* 3317 */           .getHash(), (V)entry
/* 3318 */           .getValueReference().get(), entry
/* 3319 */           .getValueReference().getWeight(), RemovalCause.COLLECTED);
/*      */       
/* 3321 */       this.writeQueue.remove(entry);
/* 3322 */       this.accessQueue.remove(entry);
/*      */     }
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     boolean reclaimKey(ReferenceEntry<K, V> entry, int hash) {
/* 3328 */       lock();
/*      */       try {
/* 3330 */         int newCount = this.count - 1;
/* 3331 */         AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
/* 3332 */         int index = hash & table.length() - 1;
/* 3333 */         ReferenceEntry<K, V> first = table.get(index);
/*      */         
/* 3335 */         for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
/* 3336 */           if (e == entry) {
/* 3337 */             this.modCount++;
/*      */             
/* 3339 */             ReferenceEntry<K, V> newFirst = removeValueFromChain(first, e, e
/*      */ 
/*      */                 
/* 3342 */                 .getKey(), hash, (V)e
/*      */                 
/* 3344 */                 .getValueReference().get(), e
/* 3345 */                 .getValueReference(), RemovalCause.COLLECTED);
/*      */             
/* 3347 */             newCount = this.count - 1;
/* 3348 */             table.set(index, newFirst);
/* 3349 */             this.count = newCount;
/* 3350 */             return true;
/*      */           } 
/*      */         } 
/*      */         
/* 3354 */         return false;
/*      */       } finally {
/* 3356 */         unlock();
/* 3357 */         postWriteCleanup();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     boolean reclaimValue(K key, int hash, LocalCache.ValueReference<K, V> valueReference) {
/* 3364 */       lock();
/*      */       try {
/* 3366 */         int newCount = this.count - 1;
/* 3367 */         AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
/* 3368 */         int index = hash & table.length() - 1;
/* 3369 */         ReferenceEntry<K, V> first = table.get(index);
/*      */         
/* 3371 */         for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
/* 3372 */           K entryKey = e.getKey();
/* 3373 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 3375 */             .equivalent(key, entryKey)) {
/* 3376 */             LocalCache.ValueReference<K, V> v = e.getValueReference();
/* 3377 */             if (v == valueReference) {
/* 3378 */               this.modCount++;
/*      */               
/* 3380 */               ReferenceEntry<K, V> newFirst = removeValueFromChain(first, e, entryKey, hash, valueReference
/*      */ 
/*      */ 
/*      */ 
/*      */                   
/* 3385 */                   .get(), valueReference, RemovalCause.COLLECTED);
/*      */ 
/*      */               
/* 3388 */               newCount = this.count - 1;
/* 3389 */               table.set(index, newFirst);
/* 3390 */               this.count = newCount;
/* 3391 */               return true;
/*      */             } 
/* 3393 */             return false;
/*      */           } 
/*      */         } 
/*      */         
/* 3397 */         return false;
/*      */       } finally {
/* 3399 */         unlock();
/* 3400 */         if (!isHeldByCurrentThread()) {
/* 3401 */           postWriteCleanup();
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     boolean removeLoadingValue(K key, int hash, LocalCache.LoadingValueReference<K, V> valueReference) {
/* 3408 */       lock();
/*      */       try {
/* 3410 */         AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
/* 3411 */         int index = hash & table.length() - 1;
/* 3412 */         ReferenceEntry<K, V> first = table.get(index);
/*      */         
/* 3414 */         for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
/* 3415 */           K entryKey = e.getKey();
/* 3416 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 3418 */             .equivalent(key, entryKey)) {
/* 3419 */             LocalCache.ValueReference<K, V> v = e.getValueReference();
/* 3420 */             if (v == valueReference) {
/* 3421 */               if (valueReference.isActive()) {
/* 3422 */                 e.setValueReference(valueReference.getOldValue());
/*      */               } else {
/* 3424 */                 ReferenceEntry<K, V> newFirst = removeEntryFromChain(first, e);
/* 3425 */                 table.set(index, newFirst);
/*      */               } 
/* 3427 */               return true;
/*      */             } 
/* 3429 */             return false;
/*      */           } 
/*      */         } 
/*      */         
/* 3433 */         return false;
/*      */       } finally {
/* 3435 */         unlock();
/* 3436 */         postWriteCleanup();
/*      */       } 
/*      */     }
/*      */     
/*      */     @VisibleForTesting
/*      */     @GuardedBy("this")
/*      */     @CanIgnoreReturnValue
/*      */     boolean removeEntry(ReferenceEntry<K, V> entry, int hash, RemovalCause cause) {
/* 3444 */       int newCount = this.count - 1;
/* 3445 */       AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
/* 3446 */       int index = hash & table.length() - 1;
/* 3447 */       ReferenceEntry<K, V> first = table.get(index);
/*      */       
/* 3449 */       for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
/* 3450 */         if (e == entry) {
/* 3451 */           this.modCount++;
/*      */           
/* 3453 */           ReferenceEntry<K, V> newFirst = removeValueFromChain(first, e, e
/*      */ 
/*      */               
/* 3456 */               .getKey(), hash, (V)e
/*      */               
/* 3458 */               .getValueReference().get(), e
/* 3459 */               .getValueReference(), cause);
/*      */           
/* 3461 */           newCount = this.count - 1;
/* 3462 */           table.set(index, newFirst);
/* 3463 */           this.count = newCount;
/* 3464 */           return true;
/*      */         } 
/*      */       } 
/*      */       
/* 3468 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void postReadCleanup() {
/* 3476 */       if ((this.readCount.incrementAndGet() & 0x3F) == 0) {
/* 3477 */         cleanUp();
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void preWriteCleanup(long now) {
/* 3489 */       runLockedCleanup(now);
/*      */     }
/*      */ 
/*      */     
/*      */     void postWriteCleanup() {
/* 3494 */       runUnlockedCleanup();
/*      */     }
/*      */     
/*      */     void cleanUp() {
/* 3498 */       long now = this.map.ticker.read();
/* 3499 */       runLockedCleanup(now);
/* 3500 */       runUnlockedCleanup();
/*      */     }
/*      */     
/*      */     void runLockedCleanup(long now) {
/* 3504 */       if (tryLock()) {
/*      */         try {
/* 3506 */           drainReferenceQueues();
/* 3507 */           expireEntries(now);
/* 3508 */           this.readCount.set(0);
/*      */         } finally {
/* 3510 */           unlock();
/*      */         } 
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     void runUnlockedCleanup() {
/* 3517 */       if (!isHeldByCurrentThread()) {
/* 3518 */         this.map.processPendingNotifications();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   static class LoadingValueReference<K, V>
/*      */     implements ValueReference<K, V>
/*      */   {
/*      */     volatile LocalCache.ValueReference<K, V> oldValue;
/* 3527 */     final SettableFuture<V> futureValue = SettableFuture.create();
/* 3528 */     final Stopwatch stopwatch = Stopwatch.createUnstarted();
/*      */     
/*      */     final Thread loadingThread;
/*      */     
/*      */     public LoadingValueReference() {
/* 3533 */       this(null);
/*      */     }
/*      */     
/*      */     public LoadingValueReference(@CheckForNull LocalCache.ValueReference<K, V> oldValue) {
/* 3537 */       this.oldValue = (oldValue == null) ? LocalCache.<K, V>unset() : oldValue;
/* 3538 */       this.loadingThread = Thread.currentThread();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isLoading() {
/* 3543 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isActive() {
/* 3548 */       return this.oldValue.isActive();
/*      */     }
/*      */ 
/*      */     
/*      */     public int getWeight() {
/* 3553 */       return this.oldValue.getWeight();
/*      */     }
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     public boolean set(@CheckForNull V newValue) {
/* 3558 */       return this.futureValue.set(newValue);
/*      */     }
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     public boolean setException(Throwable t) {
/* 3563 */       return this.futureValue.setException(t);
/*      */     }
/*      */     
/*      */     private ListenableFuture<V> fullyFailedFuture(Throwable t) {
/* 3567 */       return Futures.immediateFailedFuture(t);
/*      */     }
/*      */ 
/*      */     
/*      */     public void notifyNewValue(@CheckForNull V newValue) {
/* 3572 */       if (newValue != null) {
/*      */ 
/*      */         
/* 3575 */         set(newValue);
/*      */       } else {
/*      */         
/* 3578 */         this.oldValue = LocalCache.unset();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public ListenableFuture<V> loadFuture(K key, CacheLoader<? super K, V> loader) {
/*      */       try {
/* 3586 */         this.stopwatch.start();
/* 3587 */         V previousValue = this.oldValue.get();
/* 3588 */         if (previousValue == null) {
/* 3589 */           V v = loader.load(key);
/* 3590 */           return set(v) ? (ListenableFuture<V>)this.futureValue : Futures.immediateFuture(v);
/*      */         } 
/* 3592 */         ListenableFuture<V> newValue = loader.reload(key, previousValue);
/* 3593 */         if (newValue == null) {
/* 3594 */           return Futures.immediateFuture(null);
/*      */         }
/*      */ 
/*      */         
/* 3598 */         return Futures.transform(newValue, newResult -> {
/*      */               set((V)newResult);
/*      */ 
/*      */ 
/*      */               
/*      */               return newResult;
/* 3604 */             }MoreExecutors.directExecutor());
/* 3605 */       } catch (Throwable t) {
/* 3606 */         ListenableFuture<V> result = setException(t) ? (ListenableFuture<V>)this.futureValue : fullyFailedFuture(t);
/* 3607 */         if (t instanceof InterruptedException) {
/* 3608 */           Thread.currentThread().interrupt();
/*      */         }
/* 3610 */         return result;
/*      */       } 
/*      */     }
/*      */     
/*      */     @CheckForNull
/*      */     public V compute(K key, BiFunction<? super K, ? super V, ? extends V> function) {
/*      */       V previousValue, newValue;
/* 3617 */       this.stopwatch.start();
/*      */       
/*      */       try {
/* 3620 */         previousValue = this.oldValue.waitForValue();
/* 3621 */       } catch (ExecutionException e) {
/* 3622 */         previousValue = null;
/*      */       } 
/*      */       
/*      */       try {
/* 3626 */         newValue = function.apply(key, previousValue);
/* 3627 */       } catch (Throwable th) {
/* 3628 */         setException(th);
/* 3629 */         throw th;
/*      */       } 
/* 3631 */       set(newValue);
/* 3632 */       return newValue;
/*      */     }
/*      */     
/*      */     public long elapsedNanos() {
/* 3636 */       return this.stopwatch.elapsed(TimeUnit.NANOSECONDS);
/*      */     }
/*      */ 
/*      */     
/*      */     public V waitForValue() throws ExecutionException {
/* 3641 */       return (V)Uninterruptibles.getUninterruptibly((Future)this.futureValue);
/*      */     }
/*      */ 
/*      */     
/*      */     public V get() {
/* 3646 */       return this.oldValue.get();
/*      */     }
/*      */     
/*      */     public LocalCache.ValueReference<K, V> getOldValue() {
/* 3650 */       return this.oldValue;
/*      */     }
/*      */ 
/*      */     
/*      */     public ReferenceEntry<K, V> getEntry() {
/* 3655 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public LocalCache.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, @CheckForNull V value, ReferenceEntry<K, V> entry) {
/* 3661 */       return this;
/*      */     }
/*      */     
/*      */     Thread getLoadingThread() {
/* 3665 */       return this.loadingThread;
/*      */     }
/*      */   }
/*      */   
/*      */   static class ComputingValueReference<K, V> extends LoadingValueReference<K, V> {
/*      */     ComputingValueReference(LocalCache.ValueReference<K, V> oldValue) {
/* 3671 */       super(oldValue);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isLoading() {
/* 3676 */       return false;
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
/*      */   static final class WriteQueue<K, V>
/*      */     extends AbstractQueue<ReferenceEntry<K, V>>
/*      */   {
/* 3694 */     final ReferenceEntry<K, V> head = new LocalCache.AbstractReferenceEntry<K, V>(this)
/*      */       {
/*      */         
/*      */         public long getWriteTime()
/*      */         {
/* 3699 */           return Long.MAX_VALUE;
/*      */         }
/*      */         
/*      */         public void setWriteTime(long time) {}
/*      */         
/*      */         @Weak
/* 3705 */         ReferenceEntry<K, V> nextWrite = this;
/*      */ 
/*      */         
/*      */         public ReferenceEntry<K, V> getNextInWriteQueue() {
/* 3709 */           return this.nextWrite;
/*      */         }
/*      */ 
/*      */         
/*      */         public void setNextInWriteQueue(ReferenceEntry<K, V> next) {
/* 3714 */           this.nextWrite = next;
/*      */         }
/*      */         @Weak
/* 3717 */         ReferenceEntry<K, V> previousWrite = this;
/*      */ 
/*      */         
/*      */         public ReferenceEntry<K, V> getPreviousInWriteQueue() {
/* 3721 */           return this.previousWrite;
/*      */         }
/*      */ 
/*      */         
/*      */         public void setPreviousInWriteQueue(ReferenceEntry<K, V> previous) {
/* 3726 */           this.previousWrite = previous;
/*      */         }
/*      */       };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean offer(ReferenceEntry<K, V> entry) {
/* 3735 */       LocalCache.connectWriteOrder(entry.getPreviousInWriteQueue(), entry.getNextInWriteQueue());
/*      */ 
/*      */       
/* 3738 */       LocalCache.connectWriteOrder(this.head.getPreviousInWriteQueue(), entry);
/* 3739 */       LocalCache.connectWriteOrder(entry, this.head);
/*      */       
/* 3741 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public ReferenceEntry<K, V> peek() {
/* 3747 */       ReferenceEntry<K, V> next = this.head.getNextInWriteQueue();
/* 3748 */       return (next == this.head) ? null : next;
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public ReferenceEntry<K, V> poll() {
/* 3754 */       ReferenceEntry<K, V> next = this.head.getNextInWriteQueue();
/* 3755 */       if (next == this.head) {
/* 3756 */         return null;
/*      */       }
/*      */       
/* 3759 */       remove(next);
/* 3760 */       return next;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     public boolean remove(Object o) {
/* 3767 */       ReferenceEntry<K, V> e = (ReferenceEntry<K, V>)o;
/* 3768 */       ReferenceEntry<K, V> previous = e.getPreviousInWriteQueue();
/* 3769 */       ReferenceEntry<K, V> next = e.getNextInWriteQueue();
/* 3770 */       LocalCache.connectWriteOrder(previous, next);
/* 3771 */       LocalCache.nullifyWriteOrder(e);
/*      */       
/* 3773 */       return (next != LocalCache.NullEntry.INSTANCE);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 3779 */       ReferenceEntry<K, V> e = (ReferenceEntry<K, V>)o;
/* 3780 */       return (e.getNextInWriteQueue() != LocalCache.NullEntry.INSTANCE);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 3785 */       return (this.head.getNextInWriteQueue() == this.head);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 3790 */       int size = 0;
/* 3791 */       ReferenceEntry<K, V> e = this.head.getNextInWriteQueue();
/* 3792 */       for (; e != this.head; 
/* 3793 */         e = e.getNextInWriteQueue()) {
/* 3794 */         size++;
/*      */       }
/* 3796 */       return size;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 3801 */       ReferenceEntry<K, V> e = this.head.getNextInWriteQueue();
/* 3802 */       while (e != this.head) {
/* 3803 */         ReferenceEntry<K, V> next = e.getNextInWriteQueue();
/* 3804 */         LocalCache.nullifyWriteOrder(e);
/* 3805 */         e = next;
/*      */       } 
/*      */       
/* 3808 */       this.head.setNextInWriteQueue(this.head);
/* 3809 */       this.head.setPreviousInWriteQueue(this.head);
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<ReferenceEntry<K, V>> iterator() {
/* 3814 */       return (Iterator<ReferenceEntry<K, V>>)new AbstractSequentialIterator<ReferenceEntry<K, V>>(peek())
/*      */         {
/*      */           @CheckForNull
/*      */           protected ReferenceEntry<K, V> computeNext(ReferenceEntry<K, V> previous) {
/* 3818 */             ReferenceEntry<K, V> next = previous.getNextInWriteQueue();
/* 3819 */             return (next == LocalCache.WriteQueue.this.head) ? null : next;
/*      */           }
/*      */         };
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
/*      */   static final class AccessQueue<K, V>
/*      */     extends AbstractQueue<ReferenceEntry<K, V>>
/*      */   {
/* 3837 */     final ReferenceEntry<K, V> head = new LocalCache.AbstractReferenceEntry<K, V>(this)
/*      */       {
/*      */         
/*      */         public long getAccessTime()
/*      */         {
/* 3842 */           return Long.MAX_VALUE;
/*      */         }
/*      */         
/*      */         public void setAccessTime(long time) {}
/*      */         
/*      */         @Weak
/* 3848 */         ReferenceEntry<K, V> nextAccess = this;
/*      */ 
/*      */         
/*      */         public ReferenceEntry<K, V> getNextInAccessQueue() {
/* 3852 */           return this.nextAccess;
/*      */         }
/*      */ 
/*      */         
/*      */         public void setNextInAccessQueue(ReferenceEntry<K, V> next) {
/* 3857 */           this.nextAccess = next;
/*      */         }
/*      */         @Weak
/* 3860 */         ReferenceEntry<K, V> previousAccess = this;
/*      */ 
/*      */         
/*      */         public ReferenceEntry<K, V> getPreviousInAccessQueue() {
/* 3864 */           return this.previousAccess;
/*      */         }
/*      */ 
/*      */         
/*      */         public void setPreviousInAccessQueue(ReferenceEntry<K, V> previous) {
/* 3869 */           this.previousAccess = previous;
/*      */         }
/*      */       };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean offer(ReferenceEntry<K, V> entry) {
/* 3878 */       LocalCache.connectAccessOrder(entry.getPreviousInAccessQueue(), entry.getNextInAccessQueue());
/*      */ 
/*      */       
/* 3881 */       LocalCache.connectAccessOrder(this.head.getPreviousInAccessQueue(), entry);
/* 3882 */       LocalCache.connectAccessOrder(entry, this.head);
/*      */       
/* 3884 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public ReferenceEntry<K, V> peek() {
/* 3890 */       ReferenceEntry<K, V> next = this.head.getNextInAccessQueue();
/* 3891 */       return (next == this.head) ? null : next;
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public ReferenceEntry<K, V> poll() {
/* 3897 */       ReferenceEntry<K, V> next = this.head.getNextInAccessQueue();
/* 3898 */       if (next == this.head) {
/* 3899 */         return null;
/*      */       }
/*      */       
/* 3902 */       remove(next);
/* 3903 */       return next;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     public boolean remove(Object o) {
/* 3910 */       ReferenceEntry<K, V> e = (ReferenceEntry<K, V>)o;
/* 3911 */       ReferenceEntry<K, V> previous = e.getPreviousInAccessQueue();
/* 3912 */       ReferenceEntry<K, V> next = e.getNextInAccessQueue();
/* 3913 */       LocalCache.connectAccessOrder(previous, next);
/* 3914 */       LocalCache.nullifyAccessOrder(e);
/*      */       
/* 3916 */       return (next != LocalCache.NullEntry.INSTANCE);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 3922 */       ReferenceEntry<K, V> e = (ReferenceEntry<K, V>)o;
/* 3923 */       return (e.getNextInAccessQueue() != LocalCache.NullEntry.INSTANCE);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 3928 */       return (this.head.getNextInAccessQueue() == this.head);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 3933 */       int size = 0;
/* 3934 */       ReferenceEntry<K, V> e = this.head.getNextInAccessQueue();
/* 3935 */       for (; e != this.head; 
/* 3936 */         e = e.getNextInAccessQueue()) {
/* 3937 */         size++;
/*      */       }
/* 3939 */       return size;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 3944 */       ReferenceEntry<K, V> e = this.head.getNextInAccessQueue();
/* 3945 */       while (e != this.head) {
/* 3946 */         ReferenceEntry<K, V> next = e.getNextInAccessQueue();
/* 3947 */         LocalCache.nullifyAccessOrder(e);
/* 3948 */         e = next;
/*      */       } 
/*      */       
/* 3951 */       this.head.setNextInAccessQueue(this.head);
/* 3952 */       this.head.setPreviousInAccessQueue(this.head);
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<ReferenceEntry<K, V>> iterator() {
/* 3957 */       return (Iterator<ReferenceEntry<K, V>>)new AbstractSequentialIterator<ReferenceEntry<K, V>>(peek())
/*      */         {
/*      */           @CheckForNull
/*      */           protected ReferenceEntry<K, V> computeNext(ReferenceEntry<K, V> previous) {
/* 3961 */             ReferenceEntry<K, V> next = previous.getNextInAccessQueue();
/* 3962 */             return (next == LocalCache.AccessQueue.this.head) ? null : next;
/*      */           }
/*      */         };
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void cleanUp() {
/* 3971 */     for (Segment<?, ?> segment : this.segments) {
/* 3972 */       segment.cleanUp();
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
/*      */   public boolean isEmpty() {
/* 3987 */     long sum = 0L;
/* 3988 */     Segment<K, V>[] segments = this.segments;
/* 3989 */     for (Segment<K, V> segment : segments) {
/* 3990 */       if (segment.count != 0) {
/* 3991 */         return false;
/*      */       }
/* 3993 */       sum += segment.modCount;
/*      */     } 
/*      */     
/* 3996 */     if (sum != 0L) {
/* 3997 */       for (Segment<K, V> segment : segments) {
/* 3998 */         if (segment.count != 0) {
/* 3999 */           return false;
/*      */         }
/* 4001 */         sum -= segment.modCount;
/*      */       } 
/* 4003 */       return (sum == 0L);
/*      */     } 
/* 4005 */     return true;
/*      */   }
/*      */   
/*      */   long longSize() {
/* 4009 */     Segment<K, V>[] segments = this.segments;
/* 4010 */     long sum = 0L;
/* 4011 */     for (Segment<K, V> segment : segments) {
/* 4012 */       sum += segment.count;
/*      */     }
/* 4014 */     return sum;
/*      */   }
/*      */ 
/*      */   
/*      */   public int size() {
/* 4019 */     return Ints.saturatedCast(longSize());
/*      */   }
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   @CanIgnoreReturnValue
/*      */   public V get(@CheckForNull Object key) {
/* 4026 */     if (key == null) {
/* 4027 */       return null;
/*      */     }
/* 4029 */     int hash = hash(key);
/* 4030 */     return segmentFor(hash).get(key, hash);
/*      */   }
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   V get(K key, CacheLoader<? super K, V> loader) throws ExecutionException {
/* 4035 */     int hash = hash(Preconditions.checkNotNull(key));
/* 4036 */     return segmentFor(hash).get(key, hash, loader);
/*      */   }
/*      */   
/*      */   @CheckForNull
/*      */   public V getIfPresent(Object key) {
/* 4041 */     int hash = hash(Preconditions.checkNotNull(key));
/* 4042 */     V value = segmentFor(hash).get(key, hash);
/* 4043 */     if (value == null) {
/* 4044 */       this.globalStatsCounter.recordMisses(1);
/*      */     } else {
/* 4046 */       this.globalStatsCounter.recordHits(1);
/*      */     } 
/* 4048 */     return value;
/*      */   }
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   public V getOrDefault(@CheckForNull Object key, @CheckForNull V defaultValue) {
/* 4054 */     V result = get(key);
/* 4055 */     return (result != null) ? result : defaultValue;
/*      */   }
/*      */   
/*      */   V getOrLoad(K key) throws ExecutionException {
/* 4059 */     return get(key, this.defaultLoader);
/*      */   }
/*      */   
/*      */   ImmutableMap<K, V> getAllPresent(Iterable<?> keys) {
/* 4063 */     int hits = 0;
/* 4064 */     int misses = 0;
/*      */     
/* 4066 */     ImmutableMap.Builder<K, V> result = ImmutableMap.builder();
/* 4067 */     for (Object key : keys) {
/* 4068 */       V value = get(key);
/* 4069 */       if (value == null) {
/* 4070 */         misses++;
/*      */         
/*      */         continue;
/*      */       } 
/* 4074 */       K castKey = (K)key;
/* 4075 */       result.put(castKey, value);
/* 4076 */       hits++;
/*      */     } 
/*      */     
/* 4079 */     this.globalStatsCounter.recordHits(hits);
/* 4080 */     this.globalStatsCounter.recordMisses(misses);
/* 4081 */     return result.buildKeepingLast();
/*      */   }
/*      */   
/*      */   ImmutableMap<K, V> getAll(Iterable<? extends K> keys) throws ExecutionException {
/* 4085 */     int hits = 0;
/* 4086 */     int misses = 0;
/*      */     
/* 4088 */     Map<K, V> result = Maps.newLinkedHashMap();
/* 4089 */     Set<K> keysToLoad = Sets.newLinkedHashSet();
/* 4090 */     for (K key : keys) {
/* 4091 */       V value = get(key);
/* 4092 */       if (!result.containsKey(key)) {
/* 4093 */         result.put(key, value);
/* 4094 */         if (value == null) {
/* 4095 */           misses++;
/* 4096 */           keysToLoad.add(key); continue;
/*      */         } 
/* 4098 */         hits++;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/* 4104 */       if (!keysToLoad.isEmpty()) {
/*      */         try {
/* 4106 */           Map<K, V> newEntries = loadAll(Collections.unmodifiableSet(keysToLoad), this.defaultLoader);
/* 4107 */           for (K key : keysToLoad) {
/* 4108 */             V value = newEntries.get(key);
/* 4109 */             if (value == null) {
/* 4110 */               throw new CacheLoader.InvalidCacheLoadException("loadAll failed to return a value for " + key);
/*      */             }
/* 4112 */             result.put(key, value);
/*      */           } 
/* 4114 */         } catch (UnsupportedLoadingOperationException e) {
/*      */           
/* 4116 */           for (K key : keysToLoad) {
/* 4117 */             misses--;
/* 4118 */             result.put(key, get(key, this.defaultLoader));
/*      */           } 
/*      */         } 
/*      */       }
/* 4122 */       return ImmutableMap.copyOf(result);
/*      */     } finally {
/* 4124 */       this.globalStatsCounter.recordHits(hits);
/* 4125 */       this.globalStatsCounter.recordMisses(misses);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   Map<K, V> loadAll(Set<? extends K> keys, CacheLoader<? super K, V> loader) throws ExecutionException {
/*      */     Map<K, V> result;
/* 4136 */     Preconditions.checkNotNull(loader);
/* 4137 */     Preconditions.checkNotNull(keys);
/* 4138 */     Stopwatch stopwatch = Stopwatch.createStarted();
/*      */     
/* 4140 */     boolean success = false;
/*      */     
/*      */     try {
/* 4143 */       Map<K, V> map = (Map)loader.loadAll(keys);
/* 4144 */       result = map;
/* 4145 */       success = true;
/* 4146 */     } catch (UnsupportedLoadingOperationException e) {
/* 4147 */       success = true;
/* 4148 */       throw e;
/* 4149 */     } catch (InterruptedException e) {
/* 4150 */       Thread.currentThread().interrupt();
/* 4151 */       throw new ExecutionException(e);
/* 4152 */     } catch (RuntimeException e) {
/* 4153 */       throw new UncheckedExecutionException(e);
/* 4154 */     } catch (Exception e) {
/* 4155 */       throw new ExecutionException(e);
/* 4156 */     } catch (Error e) {
/* 4157 */       throw new ExecutionError(e);
/*      */     } finally {
/* 4159 */       if (!success) {
/* 4160 */         this.globalStatsCounter.recordLoadException(stopwatch.elapsed(TimeUnit.NANOSECONDS));
/*      */       }
/*      */     } 
/*      */     
/* 4164 */     if (result == null) {
/* 4165 */       this.globalStatsCounter.recordLoadException(stopwatch.elapsed(TimeUnit.NANOSECONDS));
/* 4166 */       throw new CacheLoader.InvalidCacheLoadException(loader + " returned null map from loadAll");
/*      */     } 
/*      */     
/* 4169 */     stopwatch.stop();
/*      */     
/* 4171 */     boolean nullsPresent = false;
/* 4172 */     for (Map.Entry<K, V> entry : result.entrySet()) {
/* 4173 */       K key = entry.getKey();
/* 4174 */       V value = entry.getValue();
/* 4175 */       if (key == null || value == null) {
/*      */         
/* 4177 */         nullsPresent = true; continue;
/*      */       } 
/* 4179 */       put(key, value);
/*      */     } 
/*      */ 
/*      */     
/* 4183 */     if (nullsPresent) {
/* 4184 */       this.globalStatsCounter.recordLoadException(stopwatch.elapsed(TimeUnit.NANOSECONDS));
/* 4185 */       throw new CacheLoader.InvalidCacheLoadException(loader + " returned null keys or values from loadAll");
/*      */     } 
/*      */ 
/*      */     
/* 4189 */     this.globalStatsCounter.recordLoadSuccess(stopwatch.elapsed(TimeUnit.NANOSECONDS));
/* 4190 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   ReferenceEntry<K, V> getEntry(@CheckForNull Object key) {
/* 4200 */     if (key == null) {
/* 4201 */       return null;
/*      */     }
/* 4203 */     int hash = hash(key);
/* 4204 */     return segmentFor(hash).getEntry(key, hash);
/*      */   }
/*      */   
/*      */   void refresh(K key) {
/* 4208 */     int hash = hash(Preconditions.checkNotNull(key));
/* 4209 */     segmentFor(hash).refresh(key, hash, this.defaultLoader, false);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean containsKey(@CheckForNull Object key) {
/* 4215 */     if (key == null) {
/* 4216 */       return false;
/*      */     }
/* 4218 */     int hash = hash(key);
/* 4219 */     return segmentFor(hash).containsKey(key, hash);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean containsValue(@CheckForNull Object value) {
/* 4225 */     if (value == null) {
/* 4226 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4234 */     long now = this.ticker.read();
/* 4235 */     Segment<K, V>[] segments = this.segments;
/* 4236 */     long last = -1L;
/* 4237 */     for (int i = 0; i < 3; i++) {
/* 4238 */       long sum = 0L;
/* 4239 */       for (Segment<K, V> segment : segments) {
/*      */         
/* 4241 */         int unused = segment.count;
/*      */         
/* 4243 */         AtomicReferenceArray<ReferenceEntry<K, V>> table = segment.table;
/* 4244 */         for (int j = 0; j < table.length(); j++) {
/* 4245 */           for (ReferenceEntry<K, V> e = table.get(j); e != null; e = e.getNext()) {
/* 4246 */             V v = segment.getLiveValue(e, now);
/* 4247 */             if (v != null && this.valueEquivalence.equivalent(value, v)) {
/* 4248 */               return true;
/*      */             }
/*      */           } 
/*      */         } 
/* 4252 */         sum += segment.modCount;
/*      */       } 
/* 4254 */       if (sum == last) {
/*      */         break;
/*      */       }
/* 4257 */       last = sum;
/*      */     } 
/* 4259 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   @CanIgnoreReturnValue
/*      */   public V put(K key, V value) {
/* 4266 */     Preconditions.checkNotNull(key);
/* 4267 */     Preconditions.checkNotNull(value);
/* 4268 */     int hash = hash(key);
/* 4269 */     return segmentFor(hash).put(key, hash, value, false);
/*      */   }
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   public V putIfAbsent(K key, V value) {
/* 4275 */     Preconditions.checkNotNull(key);
/* 4276 */     Preconditions.checkNotNull(value);
/* 4277 */     int hash = hash(key);
/* 4278 */     return segmentFor(hash).put(key, hash, value, true);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   public V compute(K key, BiFunction<? super K, ? super V, ? extends V> function) {
/* 4285 */     Preconditions.checkNotNull(key);
/* 4286 */     Preconditions.checkNotNull(function);
/* 4287 */     int hash = hash(key);
/* 4288 */     return segmentFor(hash).compute(key, hash, function);
/*      */   }
/*      */ 
/*      */   
/*      */   public V computeIfAbsent(K key, Function<? super K, ? extends V> function) {
/* 4293 */     Preconditions.checkNotNull(key);
/* 4294 */     Preconditions.checkNotNull(function);
/* 4295 */     return compute(key, (k, oldValue) -> (oldValue == null) ? function.apply(key) : oldValue);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> function) {
/* 4302 */     Preconditions.checkNotNull(key);
/* 4303 */     Preconditions.checkNotNull(function);
/* 4304 */     return compute(key, (k, oldValue) -> (oldValue == null) ? null : function.apply(k, oldValue));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   public V merge(K key, V newValue, BiFunction<? super V, ? super V, ? extends V> function) {
/* 4311 */     Preconditions.checkNotNull(key);
/* 4312 */     Preconditions.checkNotNull(newValue);
/* 4313 */     Preconditions.checkNotNull(function);
/* 4314 */     return compute(key, (k, oldValue) -> (oldValue == null) ? newValue : function.apply(oldValue, newValue));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void putAll(Map<? extends K, ? extends V> m) {
/* 4320 */     for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
/* 4321 */       put(e.getKey(), e.getValue());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   @CanIgnoreReturnValue
/*      */   public V remove(@CheckForNull Object key) {
/* 4329 */     if (key == null) {
/* 4330 */       return null;
/*      */     }
/* 4332 */     int hash = hash(key);
/* 4333 */     return segmentFor(hash).remove(key, hash);
/*      */   }
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public boolean remove(@CheckForNull Object key, @CheckForNull Object value) {
/* 4339 */     if (key == null || value == null) {
/* 4340 */       return false;
/*      */     }
/* 4342 */     int hash = hash(key);
/* 4343 */     return segmentFor(hash).remove(key, hash, value);
/*      */   }
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public boolean replace(K key, @CheckForNull V oldValue, V newValue) {
/* 4349 */     Preconditions.checkNotNull(key);
/* 4350 */     Preconditions.checkNotNull(newValue);
/* 4351 */     if (oldValue == null) {
/* 4352 */       return false;
/*      */     }
/* 4354 */     int hash = hash(key);
/* 4355 */     return segmentFor(hash).replace(key, hash, oldValue, newValue);
/*      */   }
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   @CanIgnoreReturnValue
/*      */   public V replace(K key, V value) {
/* 4362 */     Preconditions.checkNotNull(key);
/* 4363 */     Preconditions.checkNotNull(value);
/* 4364 */     int hash = hash(key);
/* 4365 */     return segmentFor(hash).replace(key, hash, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public void clear() {
/* 4370 */     for (Segment<K, V> segment : this.segments) {
/* 4371 */       segment.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   void invalidateAll(Iterable<?> keys) {
/* 4377 */     for (Object key : keys) {
/* 4378 */       remove(key);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<K> keySet() {
/* 4387 */     Set<K> ks = this.keySet;
/* 4388 */     return (ks != null) ? ks : (this.keySet = new KeySet());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<V> values() {
/* 4396 */     Collection<V> vs = this.values;
/* 4397 */     return (vs != null) ? vs : (this.values = new Values());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public Set<Map.Entry<K, V>> entrySet() {
/* 4406 */     Set<Map.Entry<K, V>> es = this.entrySet;
/* 4407 */     return (es != null) ? es : (this.entrySet = new EntrySet());
/*      */   }
/*      */   abstract class HashIterator<T> implements Iterator<T> { int nextSegmentIndex;
/*      */     int nextTableIndex;
/*      */     @CheckForNull
/*      */     LocalCache.Segment<K, V> currentSegment;
/*      */     @CheckForNull
/*      */     AtomicReferenceArray<ReferenceEntry<K, V>> currentTable;
/*      */     @CheckForNull
/*      */     ReferenceEntry<K, V> nextEntry;
/*      */     @CheckForNull
/*      */     LocalCache<K, V>.WriteThroughEntry nextExternal;
/*      */     @CheckForNull
/*      */     LocalCache<K, V>.WriteThroughEntry lastReturned;
/*      */     
/*      */     HashIterator() {
/* 4423 */       this.nextSegmentIndex = LocalCache.this.segments.length - 1;
/* 4424 */       this.nextTableIndex = -1;
/* 4425 */       advance();
/*      */     }
/*      */ 
/*      */     
/*      */     public abstract T next();
/*      */     
/*      */     final void advance() {
/* 4432 */       this.nextExternal = null;
/*      */       
/* 4434 */       if (nextInChain()) {
/*      */         return;
/*      */       }
/*      */       
/* 4438 */       if (nextInTable()) {
/*      */         return;
/*      */       }
/*      */       
/* 4442 */       while (this.nextSegmentIndex >= 0) {
/* 4443 */         this.currentSegment = LocalCache.this.segments[this.nextSegmentIndex--];
/* 4444 */         if (this.currentSegment.count != 0) {
/* 4445 */           this.currentTable = this.currentSegment.table;
/* 4446 */           this.nextTableIndex = this.currentTable.length() - 1;
/* 4447 */           if (nextInTable()) {
/*      */             return;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     boolean nextInChain() {
/* 4456 */       if (this.nextEntry != null) {
/* 4457 */         for (this.nextEntry = this.nextEntry.getNext(); this.nextEntry != null; this.nextEntry = this.nextEntry.getNext()) {
/* 4458 */           if (advanceTo(this.nextEntry)) {
/* 4459 */             return true;
/*      */           }
/*      */         } 
/*      */       }
/* 4463 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     boolean nextInTable() {
/* 4468 */       while (this.nextTableIndex >= 0) {
/* 4469 */         if ((this.nextEntry = this.currentTable.get(this.nextTableIndex--)) != null && (
/* 4470 */           advanceTo(this.nextEntry) || nextInChain())) {
/* 4471 */           return true;
/*      */         }
/*      */       } 
/*      */       
/* 4475 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean advanceTo(ReferenceEntry<K, V> entry) {
/*      */       try {
/* 4484 */         long now = LocalCache.this.ticker.read();
/* 4485 */         K key = entry.getKey();
/* 4486 */         V value = LocalCache.this.getLiveValue(entry, now);
/* 4487 */         if (value != null) {
/* 4488 */           this.nextExternal = new LocalCache.WriteThroughEntry(key, value);
/* 4489 */           return true;
/*      */         } 
/*      */         
/* 4492 */         return false;
/*      */       } finally {
/*      */         
/* 4495 */         this.currentSegment.postReadCleanup();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/* 4501 */       return (this.nextExternal != null);
/*      */     }
/*      */     
/*      */     LocalCache<K, V>.WriteThroughEntry nextEntry() {
/* 4505 */       if (this.nextExternal == null) {
/* 4506 */         throw new NoSuchElementException();
/*      */       }
/* 4508 */       this.lastReturned = this.nextExternal;
/* 4509 */       advance();
/* 4510 */       return this.lastReturned;
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/* 4515 */       Preconditions.checkState((this.lastReturned != null));
/* 4516 */       LocalCache.this.remove(this.lastReturned.getKey());
/* 4517 */       this.lastReturned = null;
/*      */     } }
/*      */   
/*      */   final class KeyIterator extends HashIterator<K> { KeyIterator(LocalCache this$0) {
/* 4521 */       super(this$0);
/*      */     }
/*      */     
/*      */     public K next() {
/* 4525 */       return nextEntry().getKey();
/*      */     } }
/*      */   
/*      */   final class ValueIterator extends HashIterator<V> { ValueIterator(LocalCache this$0) {
/* 4529 */       super(this$0);
/*      */     }
/*      */     
/*      */     public V next() {
/* 4533 */       return nextEntry().getValue();
/*      */     } }
/*      */ 
/*      */ 
/*      */   
/*      */   final class WriteThroughEntry
/*      */     implements Map.Entry<K, V>
/*      */   {
/*      */     final K key;
/*      */     
/*      */     V value;
/*      */     
/*      */     WriteThroughEntry(K key, V value) {
/* 4546 */       this.key = key;
/* 4547 */       this.value = value;
/*      */     }
/*      */ 
/*      */     
/*      */     public K getKey() {
/* 4552 */       return this.key;
/*      */     }
/*      */ 
/*      */     
/*      */     public V getValue() {
/* 4557 */       return this.value;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(@CheckForNull Object object) {
/* 4563 */       if (object instanceof Map.Entry) {
/* 4564 */         Map.Entry<?, ?> that = (Map.Entry<?, ?>)object;
/* 4565 */         return (this.key.equals(that.getKey()) && this.value.equals(that.getValue()));
/*      */       } 
/* 4567 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 4573 */       return this.key.hashCode() ^ this.value.hashCode();
/*      */     }
/*      */ 
/*      */     
/*      */     public V setValue(V newValue) {
/* 4578 */       V oldValue = LocalCache.this.put(this.key, newValue);
/* 4579 */       this.value = newValue;
/* 4580 */       return oldValue;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 4585 */       return (new StringBuilder()).append(getKey()).append("=").append(getValue()).toString();
/*      */     } }
/*      */   
/*      */   final class EntryIterator extends HashIterator<Map.Entry<K, V>> { EntryIterator(LocalCache this$0) {
/* 4589 */       super(this$0);
/*      */     }
/*      */     
/*      */     public Map.Entry<K, V> next() {
/* 4593 */       return nextEntry();
/*      */     } }
/*      */ 
/*      */   
/*      */   abstract class AbstractCacheSet<T>
/*      */     extends AbstractSet<T> {
/*      */     public int size() {
/* 4600 */       return LocalCache.this.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 4605 */       return LocalCache.this.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 4610 */       LocalCache.this.clear();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/* 4618 */       return LocalCache.toArrayList(this).toArray();
/*      */     }
/*      */ 
/*      */     
/*      */     public <E> E[] toArray(E[] a) {
/* 4623 */       return (E[])LocalCache.toArrayList(this).toArray((Object[])a);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static <E> ArrayList<E> toArrayList(Collection<E> c) {
/* 4629 */     ArrayList<E> result = new ArrayList<>(c.size());
/* 4630 */     Iterators.addAll(result, c.iterator());
/* 4631 */     return result;
/*      */   }
/*      */   
/*      */   boolean removeIf(BiPredicate<? super K, ? super V> filter) {
/* 4635 */     Preconditions.checkNotNull(filter);
/* 4636 */     boolean changed = false;
/* 4637 */     label17: for (K key : keySet()) {
/*      */       while (true) {
/* 4639 */         V value = get(key);
/* 4640 */         if (value != null) { if (!filter.test(key, value))
/*      */             continue label17; 
/* 4642 */           if (remove(key, value))
/* 4643 */             changed = true;  continue; }
/*      */         
/*      */         continue label17;
/*      */       } 
/*      */     } 
/* 4648 */     return changed;
/*      */   }
/*      */   
/*      */   final class KeySet
/*      */     extends AbstractCacheSet<K>
/*      */   {
/*      */     public Iterator<K> iterator() {
/* 4655 */       return new LocalCache.KeyIterator(LocalCache.this);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 4660 */       return LocalCache.this.containsKey(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 4665 */       return (LocalCache.this.remove(o) != null);
/*      */     }
/*      */   }
/*      */   
/*      */   final class Values
/*      */     extends AbstractCollection<V> {
/*      */     public int size() {
/* 4672 */       return LocalCache.this.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 4677 */       return LocalCache.this.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 4682 */       LocalCache.this.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<V> iterator() {
/* 4687 */       return new LocalCache.ValueIterator(LocalCache.this);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeIf(Predicate<? super V> filter) {
/* 4692 */       Preconditions.checkNotNull(filter);
/* 4693 */       return LocalCache.this.removeIf((k, v) -> filter.test(v));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 4698 */       return LocalCache.this.containsValue(o);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/* 4706 */       return LocalCache.toArrayList(this).toArray();
/*      */     }
/*      */ 
/*      */     
/*      */     public <E> E[] toArray(E[] a) {
/* 4711 */       return (E[])LocalCache.toArrayList(this).toArray((Object[])a);
/*      */     }
/*      */   }
/*      */   
/*      */   final class EntrySet
/*      */     extends AbstractCacheSet<Map.Entry<K, V>>
/*      */   {
/*      */     public Iterator<Map.Entry<K, V>> iterator() {
/* 4719 */       return new LocalCache.EntryIterator(LocalCache.this);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeIf(Predicate<? super Map.Entry<K, V>> filter) {
/* 4724 */       Preconditions.checkNotNull(filter);
/* 4725 */       return LocalCache.this.removeIf((k, v) -> filter.test(Maps.immutableEntry(k, v)));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 4730 */       if (!(o instanceof Map.Entry)) {
/* 4731 */         return false;
/*      */       }
/* 4733 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 4734 */       Object key = e.getKey();
/* 4735 */       if (key == null) {
/* 4736 */         return false;
/*      */       }
/* 4738 */       V v = (V)LocalCache.this.get(key);
/*      */       
/* 4740 */       return (v != null && LocalCache.this.valueEquivalence.equivalent(e.getValue(), v));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 4745 */       if (!(o instanceof Map.Entry)) {
/* 4746 */         return false;
/*      */       }
/* 4748 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 4749 */       Object key = e.getKey();
/* 4750 */       return (key != null && LocalCache.this.remove(key, e.getValue()));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static class ManualSerializationProxy<K, V>
/*      */     extends ForwardingCache<K, V>
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */     
/*      */     final LocalCache.Strength keyStrength;
/*      */     
/*      */     final LocalCache.Strength valueStrength;
/*      */     
/*      */     final Equivalence<Object> keyEquivalence;
/*      */     
/*      */     final Equivalence<Object> valueEquivalence;
/*      */     
/*      */     final long expireAfterWriteNanos;
/*      */     
/*      */     final long expireAfterAccessNanos;
/*      */     
/*      */     final long maxWeight;
/*      */     final Weigher<K, V> weigher;
/*      */     final int concurrencyLevel;
/*      */     final RemovalListener<? super K, ? super V> removalListener;
/*      */     @CheckForNull
/*      */     final Ticker ticker;
/*      */     final CacheLoader<? super K, V> loader;
/*      */     @CheckForNull
/*      */     transient Cache<K, V> delegate;
/*      */     
/*      */     ManualSerializationProxy(LocalCache<K, V> cache) {
/* 4784 */       this(cache.keyStrength, cache.valueStrength, cache.keyEquivalence, cache.valueEquivalence, cache.expireAfterWriteNanos, cache.expireAfterAccessNanos, cache.maxWeight, cache.weigher, cache.concurrencyLevel, cache.removalListener, cache.ticker, cache.defaultLoader);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private ManualSerializationProxy(LocalCache.Strength keyStrength, LocalCache.Strength valueStrength, Equivalence<Object> keyEquivalence, Equivalence<Object> valueEquivalence, long expireAfterWriteNanos, long expireAfterAccessNanos, long maxWeight, Weigher<K, V> weigher, int concurrencyLevel, RemovalListener<? super K, ? super V> removalListener, Ticker ticker, CacheLoader<? super K, V> loader) {
/* 4812 */       this.keyStrength = keyStrength;
/* 4813 */       this.valueStrength = valueStrength;
/* 4814 */       this.keyEquivalence = keyEquivalence;
/* 4815 */       this.valueEquivalence = valueEquivalence;
/* 4816 */       this.expireAfterWriteNanos = expireAfterWriteNanos;
/* 4817 */       this.expireAfterAccessNanos = expireAfterAccessNanos;
/* 4818 */       this.maxWeight = maxWeight;
/* 4819 */       this.weigher = weigher;
/* 4820 */       this.concurrencyLevel = concurrencyLevel;
/* 4821 */       this.removalListener = removalListener;
/* 4822 */       this.ticker = (ticker == Ticker.systemTicker() || ticker == CacheBuilder.NULL_TICKER) ? null : ticker;
/* 4823 */       this.loader = loader;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     CacheBuilder<K, V> recreateCacheBuilder() {
/* 4834 */       CacheBuilder<K, V> builder = CacheBuilder.newBuilder().setKeyStrength(this.keyStrength).setValueStrength(this.valueStrength).keyEquivalence(this.keyEquivalence).valueEquivalence(this.valueEquivalence).concurrencyLevel(this.concurrencyLevel).removalListener(this.removalListener);
/* 4835 */       builder.strictParsing = false;
/* 4836 */       if (this.expireAfterWriteNanos > 0L) {
/* 4837 */         builder.expireAfterWrite(this.expireAfterWriteNanos, TimeUnit.NANOSECONDS);
/*      */       }
/* 4839 */       if (this.expireAfterAccessNanos > 0L) {
/* 4840 */         builder.expireAfterAccess(this.expireAfterAccessNanos, TimeUnit.NANOSECONDS);
/*      */       }
/* 4842 */       if (this.weigher != CacheBuilder.OneWeigher.INSTANCE) {
/* 4843 */         Object<K, V> unused = (Object<K, V>)builder.weigher(this.weigher);
/* 4844 */         if (this.maxWeight != -1L) {
/* 4845 */           builder.maximumWeight(this.maxWeight);
/*      */         }
/*      */       }
/* 4848 */       else if (this.maxWeight != -1L) {
/* 4849 */         builder.maximumSize(this.maxWeight);
/*      */       } 
/*      */       
/* 4852 */       if (this.ticker != null) {
/* 4853 */         builder.ticker(this.ticker);
/*      */       }
/* 4855 */       return builder;
/*      */     }
/*      */     
/*      */     private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 4859 */       in.defaultReadObject();
/* 4860 */       CacheBuilder<K, V> builder = recreateCacheBuilder();
/* 4861 */       this.delegate = builder.build();
/*      */     }
/*      */     
/*      */     private Object readResolve() {
/* 4865 */       return this.delegate;
/*      */     }
/*      */ 
/*      */     
/*      */     protected Cache<K, V> delegate() {
/* 4870 */       return this.delegate;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final class LoadingSerializationProxy<K, V>
/*      */     extends ManualSerializationProxy<K, V>
/*      */     implements LoadingCache<K, V>, Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     transient LoadingCache<K, V> autoDelegate;
/*      */ 
/*      */ 
/*      */     
/*      */     LoadingSerializationProxy(LocalCache<K, V> cache) {
/* 4889 */       super(cache);
/*      */     }
/*      */     
/*      */     private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 4893 */       in.defaultReadObject();
/* 4894 */       CacheBuilder<K, V> builder = recreateCacheBuilder();
/* 4895 */       this.autoDelegate = builder.build(this.loader);
/*      */     }
/*      */ 
/*      */     
/*      */     public V get(K key) throws ExecutionException {
/* 4900 */       return this.autoDelegate.get(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public V getUnchecked(K key) {
/* 4905 */       return this.autoDelegate.getUnchecked(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public ImmutableMap<K, V> getAll(Iterable<? extends K> keys) throws ExecutionException {
/* 4910 */       return this.autoDelegate.getAll(keys);
/*      */     }
/*      */ 
/*      */     
/*      */     public V apply(K key) {
/* 4915 */       return this.autoDelegate.apply(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public void refresh(K key) {
/* 4920 */       this.autoDelegate.refresh(key);
/*      */     }
/*      */     
/*      */     private Object readResolve() {
/* 4924 */       return this.autoDelegate;
/*      */     } }
/*      */   
/*      */   static class LocalManualCache<K, V> implements Cache<K, V>, Serializable {
/*      */     final LocalCache<K, V> localCache;
/*      */     private static final long serialVersionUID = 1L;
/*      */     
/*      */     LocalManualCache(CacheBuilder<? super K, ? super V> builder) {
/* 4932 */       this(new LocalCache<>(builder, null));
/*      */     }
/*      */     
/*      */     private LocalManualCache(LocalCache<K, V> localCache) {
/* 4936 */       this.localCache = localCache;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V getIfPresent(Object key) {
/* 4944 */       return this.localCache.getIfPresent(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public V get(K key, final Callable<? extends V> valueLoader) throws ExecutionException {
/* 4949 */       Preconditions.checkNotNull(valueLoader);
/* 4950 */       return this.localCache.get(key, (CacheLoader)new CacheLoader<Object, V>(this)
/*      */           {
/*      */             
/*      */             public V load(Object key) throws Exception
/*      */             {
/* 4955 */               return valueLoader.call();
/*      */             }
/*      */           });
/*      */     }
/*      */ 
/*      */     
/*      */     public ImmutableMap<K, V> getAllPresent(Iterable<?> keys) {
/* 4962 */       return this.localCache.getAllPresent(keys);
/*      */     }
/*      */ 
/*      */     
/*      */     public void put(K key, V value) {
/* 4967 */       this.localCache.put(key, value);
/*      */     }
/*      */ 
/*      */     
/*      */     public void putAll(Map<? extends K, ? extends V> m) {
/* 4972 */       this.localCache.putAll(m);
/*      */     }
/*      */ 
/*      */     
/*      */     public void invalidate(Object key) {
/* 4977 */       Preconditions.checkNotNull(key);
/* 4978 */       this.localCache.remove(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public void invalidateAll(Iterable<?> keys) {
/* 4983 */       this.localCache.invalidateAll(keys);
/*      */     }
/*      */ 
/*      */     
/*      */     public void invalidateAll() {
/* 4988 */       this.localCache.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public long size() {
/* 4993 */       return this.localCache.longSize();
/*      */     }
/*      */ 
/*      */     
/*      */     public ConcurrentMap<K, V> asMap() {
/* 4998 */       return this.localCache;
/*      */     }
/*      */ 
/*      */     
/*      */     public CacheStats stats() {
/* 5003 */       AbstractCache.SimpleStatsCounter aggregator = new AbstractCache.SimpleStatsCounter();
/* 5004 */       aggregator.incrementBy(this.localCache.globalStatsCounter);
/* 5005 */       for (LocalCache.Segment<K, V> segment : this.localCache.segments) {
/* 5006 */         aggregator.incrementBy(segment.statsCounter);
/*      */       }
/* 5008 */       return aggregator.snapshot();
/*      */     }
/*      */ 
/*      */     
/*      */     public void cleanUp() {
/* 5013 */       this.localCache.cleanUp();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Object writeReplace() {
/* 5021 */       return new LocalCache.ManualSerializationProxy<>(this.localCache);
/*      */     }
/*      */     
/*      */     private void readObject(ObjectInputStream in) throws InvalidObjectException {
/* 5025 */       throw new InvalidObjectException("Use ManualSerializationProxy");
/*      */     }
/*      */   }
/*      */   
/*      */   static class LocalLoadingCache<K, V>
/*      */     extends LocalManualCache<K, V> implements LoadingCache<K, V> {
/*      */     private static final long serialVersionUID = 1L;
/*      */     
/*      */     LocalLoadingCache(CacheBuilder<? super K, ? super V> builder, CacheLoader<? super K, V> loader) {
/* 5034 */       super(new LocalCache<>(builder, (CacheLoader<? super K, V>)Preconditions.checkNotNull(loader)));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public V get(K key) throws ExecutionException {
/* 5041 */       return this.localCache.getOrLoad(key);
/*      */     }
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     public V getUnchecked(K key) {
/*      */       try {
/* 5048 */         return get(key);
/* 5049 */       } catch (ExecutionException e) {
/* 5050 */         throw new UncheckedExecutionException(e.getCause());
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public ImmutableMap<K, V> getAll(Iterable<? extends K> keys) throws ExecutionException {
/* 5056 */       return this.localCache.getAll(keys);
/*      */     }
/*      */ 
/*      */     
/*      */     public void refresh(K key) {
/* 5061 */       this.localCache.refresh(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public final V apply(K key) {
/* 5066 */       return getUnchecked(key);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Object writeReplace() {
/* 5075 */       return new LocalCache.LoadingSerializationProxy<>(this.localCache);
/*      */     }
/*      */     
/*      */     private void readObject(ObjectInputStream in) throws InvalidObjectException {
/* 5079 */       throw new InvalidObjectException("Use LoadingSerializationProxy");
/*      */     }
/*      */   }
/*      */   
/*      */   static interface ValueReference<K, V> {
/*      */     @CheckForNull
/*      */     V get();
/*      */     
/*      */     V waitForValue() throws ExecutionException;
/*      */     
/*      */     int getWeight();
/*      */     
/*      */     @CheckForNull
/*      */     ReferenceEntry<K, V> getEntry();
/*      */     
/*      */     ValueReference<K, V> copyFor(ReferenceQueue<V> param1ReferenceQueue, @CheckForNull V param1V, ReferenceEntry<K, V> param1ReferenceEntry);
/*      */     
/*      */     void notifyNewValue(@CheckForNull V param1V);
/*      */     
/*      */     boolean isLoading();
/*      */     
/*      */     boolean isActive();
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/cache/LocalCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */