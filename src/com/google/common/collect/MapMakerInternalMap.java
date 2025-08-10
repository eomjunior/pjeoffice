/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.J2ktIncompatible;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Equivalence;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.primitives.Ints;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import com.google.errorprone.annotations.concurrent.GuardedBy;
/*      */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*      */ import com.google.j2objc.annotations.Weak;
/*      */ import java.io.IOException;
/*      */ import java.io.InvalidObjectException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.lang.ref.Reference;
/*      */ import java.lang.ref.ReferenceQueue;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.AbstractMap;
/*      */ import java.util.AbstractSet;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.CancellationException;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.concurrent.atomic.AtomicReferenceArray;
/*      */ import java.util.concurrent.locks.ReentrantLock;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @J2ktIncompatible
/*      */ @GwtIncompatible
/*      */ class MapMakerInternalMap<K, V, E extends MapMakerInternalMap.InternalEntry<K, V, E>, S extends MapMakerInternalMap.Segment<K, V, E, S>>
/*      */   extends AbstractMap<K, V>
/*      */   implements ConcurrentMap<K, V>, Serializable
/*      */ {
/*      */   static final int MAXIMUM_CAPACITY = 1073741824;
/*      */   static final int MAX_SEGMENTS = 65536;
/*      */   static final int CONTAINS_VALUE_RETRIES = 3;
/*      */   static final int DRAIN_THRESHOLD = 63;
/*      */   static final int DRAIN_MAX = 16;
/*      */   final transient int segmentMask;
/*      */   final transient int segmentShift;
/*      */   final transient Segment<K, V, E, S>[] segments;
/*      */   final int concurrencyLevel;
/*      */   final Equivalence<Object> keyEquivalence;
/*      */   final transient InternalEntryHelper<K, V, E, S> entryHelper;
/*      */   
/*      */   private MapMakerInternalMap(MapMaker builder, InternalEntryHelper<K, V, E, S> entryHelper) {
/*  167 */     this.concurrencyLevel = Math.min(builder.getConcurrencyLevel(), 65536);
/*      */     
/*  169 */     this.keyEquivalence = builder.getKeyEquivalence();
/*  170 */     this.entryHelper = entryHelper;
/*      */     
/*  172 */     int initialCapacity = Math.min(builder.getInitialCapacity(), 1073741824);
/*      */ 
/*      */ 
/*      */     
/*  176 */     int segmentShift = 0;
/*  177 */     int segmentCount = 1;
/*  178 */     while (segmentCount < this.concurrencyLevel) {
/*  179 */       segmentShift++;
/*  180 */       segmentCount <<= 1;
/*      */     } 
/*  182 */     this.segmentShift = 32 - segmentShift;
/*  183 */     this.segmentMask = segmentCount - 1;
/*      */     
/*  185 */     this.segments = newSegmentArray(segmentCount);
/*      */     
/*  187 */     int segmentCapacity = initialCapacity / segmentCount;
/*  188 */     if (segmentCapacity * segmentCount < initialCapacity) {
/*  189 */       segmentCapacity++;
/*      */     }
/*      */     
/*  192 */     int segmentSize = 1;
/*  193 */     while (segmentSize < segmentCapacity) {
/*  194 */       segmentSize <<= 1;
/*      */     }
/*      */     
/*  197 */     for (int i = 0; i < this.segments.length; i++) {
/*  198 */       this.segments[i] = createSegment(segmentSize);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> MapMakerInternalMap<K, V, ? extends InternalEntry<K, V, ?>, ?> create(MapMaker builder) {
/*  205 */     if (builder.getKeyStrength() == Strength.STRONG && builder
/*  206 */       .getValueStrength() == Strength.STRONG) {
/*  207 */       return new MapMakerInternalMap<>(builder, (InternalEntryHelper)StrongKeyStrongValueEntry.Helper.instance());
/*      */     }
/*  209 */     if (builder.getKeyStrength() == Strength.STRONG && builder
/*  210 */       .getValueStrength() == Strength.WEAK) {
/*  211 */       return new MapMakerInternalMap<>(builder, (InternalEntryHelper)StrongKeyWeakValueEntry.Helper.instance());
/*      */     }
/*  213 */     if (builder.getKeyStrength() == Strength.WEAK && builder
/*  214 */       .getValueStrength() == Strength.STRONG) {
/*  215 */       return new MapMakerInternalMap<>(builder, (InternalEntryHelper)WeakKeyStrongValueEntry.Helper.instance());
/*      */     }
/*  217 */     if (builder.getKeyStrength() == Strength.WEAK && builder.getValueStrength() == Strength.WEAK) {
/*  218 */       return new MapMakerInternalMap<>(builder, (InternalEntryHelper)WeakKeyWeakValueEntry.Helper.instance());
/*      */     }
/*  220 */     throw new AssertionError();
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
/*      */   static <K> MapMakerInternalMap<K, MapMaker.Dummy, ? extends InternalEntry<K, MapMaker.Dummy, ?>, ?> createWithDummyValues(MapMaker builder) {
/*  236 */     if (builder.getKeyStrength() == Strength.STRONG && builder
/*  237 */       .getValueStrength() == Strength.STRONG) {
/*  238 */       return new MapMakerInternalMap<>(builder, (InternalEntryHelper)StrongKeyDummyValueEntry.Helper.instance());
/*      */     }
/*  240 */     if (builder.getKeyStrength() == Strength.WEAK && builder
/*  241 */       .getValueStrength() == Strength.STRONG) {
/*  242 */       return new MapMakerInternalMap<>(builder, (InternalEntryHelper)WeakKeyDummyValueEntry.Helper.instance());
/*      */     }
/*  244 */     if (builder.getValueStrength() == Strength.WEAK) {
/*  245 */       throw new IllegalArgumentException("Map cannot have both weak and dummy values");
/*      */     }
/*  247 */     throw new AssertionError();
/*      */   }
/*      */   
/*      */   enum Strength {
/*  251 */     STRONG
/*      */     {
/*      */       Equivalence<Object> defaultEquivalence() {
/*  254 */         return Equivalence.equals();
/*      */       }
/*      */     },
/*      */     
/*  258 */     WEAK
/*      */     {
/*      */       Equivalence<Object> defaultEquivalence() {
/*  261 */         return Equivalence.identity();
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract Equivalence<Object> defaultEquivalence();
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
/*      */   static abstract class AbstractStrongKeyEntry<K, V, E extends InternalEntry<K, V, E>>
/*      */     implements InternalEntry<K, V, E>
/*      */   {
/*      */     final K key;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final int hash;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     AbstractStrongKeyEntry(K key, int hash) {
/*  350 */       this.key = key;
/*  351 */       this.hash = hash;
/*      */     }
/*      */ 
/*      */     
/*      */     public final K getKey() {
/*  356 */       return this.key;
/*      */     }
/*      */ 
/*      */     
/*      */     public final int getHash() {
/*  361 */       return this.hash;
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public E getNext() {
/*  367 */       return null;
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
/*      */   static <K, V, E extends InternalEntry<K, V, E>> WeakValueReference<K, V, E> unsetWeakValueReference() {
/*  384 */     return (WeakValueReference)UNSET_WEAK_VALUE_REFERENCE;
/*      */   }
/*      */   
/*      */   static class StrongKeyStrongValueEntry<K, V>
/*      */     extends AbstractStrongKeyEntry<K, V, StrongKeyStrongValueEntry<K, V>>
/*      */     implements StrongValueEntry<K, V, StrongKeyStrongValueEntry<K, V>> {
/*      */     @CheckForNull
/*  391 */     private volatile V value = null;
/*      */     
/*      */     private StrongKeyStrongValueEntry(K key, int hash) {
/*  394 */       super(key, hash);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public final V getValue() {
/*  400 */       return this.value;
/*      */     }
/*      */     
/*      */     private static final class LinkedStrongKeyStrongValueEntry<K, V>
/*      */       extends StrongKeyStrongValueEntry<K, V> {
/*      */       private final MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> next;
/*      */       
/*      */       LinkedStrongKeyStrongValueEntry(K key, int hash, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> next) {
/*  408 */         super(key, hash);
/*  409 */         this.next = next;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> getNext() {
/*  414 */         return this.next;
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     static final class Helper<K, V>
/*      */       implements MapMakerInternalMap.InternalEntryHelper<K, V, StrongKeyStrongValueEntry<K, V>, MapMakerInternalMap.StrongKeyStrongValueSegment<K, V>>
/*      */     {
/*  422 */       private static final Helper<?, ?> INSTANCE = new Helper();
/*      */ 
/*      */       
/*      */       static <K, V> Helper<K, V> instance() {
/*  426 */         return (Helper)INSTANCE;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength keyStrength() {
/*  431 */         return MapMakerInternalMap.Strength.STRONG;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength valueStrength() {
/*  436 */         return MapMakerInternalMap.Strength.STRONG;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V>, MapMakerInternalMap.StrongKeyStrongValueSegment<K, V>> map, int initialCapacity) {
/*  445 */         return new MapMakerInternalMap.StrongKeyStrongValueSegment<>(map, initialCapacity);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> copy(MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> entry, @CheckForNull MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> newNext) {
/*  454 */         MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> newEntry = newEntry(segment, entry.key, entry.hash, newNext);
/*  455 */         newEntry.value = entry.value;
/*  456 */         return newEntry;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public void setValue(MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> entry, V value) {
/*  464 */         entry.value = value;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> newEntry(MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> segment, K key, int hash, @CheckForNull MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> next)
/*      */       {
/*  473 */         return (next == null) ? 
/*  474 */           new MapMakerInternalMap.StrongKeyStrongValueEntry<>(key, hash) : 
/*  475 */           new MapMakerInternalMap.StrongKeyStrongValueEntry.LinkedStrongKeyStrongValueEntry<>(key, hash, next); } } } static final class Helper<K, V> implements InternalEntryHelper<K, V, StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>> { private static final Helper<?, ?> INSTANCE = new Helper(); static <K, V> Helper<K, V> instance() { return (Helper)INSTANCE; } public MapMakerInternalMap.Strength keyStrength() { return MapMakerInternalMap.Strength.STRONG; } public MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> newEntry(MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> segment, K key, int hash, @CheckForNull MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> next) { return (next == null) ? new MapMakerInternalMap.StrongKeyStrongValueEntry<>(key, hash) : new MapMakerInternalMap.StrongKeyStrongValueEntry.LinkedStrongKeyStrongValueEntry<>(key, hash, next); } public MapMakerInternalMap.Strength valueStrength() {
/*      */       return MapMakerInternalMap.Strength.STRONG;
/*      */     } public MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V>, MapMakerInternalMap.StrongKeyStrongValueSegment<K, V>> map, int initialCapacity) {
/*      */       return new MapMakerInternalMap.StrongKeyStrongValueSegment<>(map, initialCapacity);
/*      */     } public MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> copy(MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> entry, @CheckForNull MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> newNext) {
/*      */       MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> newEntry = newEntry(segment, entry.key, entry.hash, newNext);
/*      */       newEntry.value = entry.value;
/*      */       return newEntry;
/*      */     } public void setValue(MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> entry, V value) {
/*      */       entry.value = value;
/*  485 */     } } static class StrongKeyWeakValueEntry<K, V> extends AbstractStrongKeyEntry<K, V, StrongKeyWeakValueEntry<K, V>> implements WeakValueEntry<K, V, StrongKeyWeakValueEntry<K, V>> { private volatile MapMakerInternalMap.WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> valueReference = MapMakerInternalMap.unsetWeakValueReference();
/*      */     
/*      */     private StrongKeyWeakValueEntry(K key, int hash) {
/*  488 */       super(key, hash);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public final V getValue() {
/*  494 */       return this.valueReference.get();
/*      */     }
/*      */ 
/*      */     
/*      */     public final MapMakerInternalMap.WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> getValueReference() {
/*  499 */       return this.valueReference;
/*      */     }
/*      */     
/*      */     private static final class LinkedStrongKeyWeakValueEntry<K, V>
/*      */       extends StrongKeyWeakValueEntry<K, V> {
/*      */       private final MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> next;
/*      */       
/*      */       LinkedStrongKeyWeakValueEntry(K key, int hash, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> next) {
/*  507 */         super(key, hash);
/*  508 */         this.next = next;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> getNext() {
/*  513 */         return this.next;
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     static final class Helper<K, V>
/*      */       implements MapMakerInternalMap.InternalEntryHelper<K, V, StrongKeyWeakValueEntry<K, V>, MapMakerInternalMap.StrongKeyWeakValueSegment<K, V>>
/*      */     {
/*  521 */       private static final Helper<?, ?> INSTANCE = new Helper();
/*      */ 
/*      */       
/*      */       static <K, V> Helper<K, V> instance() {
/*  525 */         return (Helper)INSTANCE;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength keyStrength() {
/*  530 */         return MapMakerInternalMap.Strength.STRONG;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength valueStrength() {
/*  535 */         return MapMakerInternalMap.Strength.WEAK;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>, MapMakerInternalMap.StrongKeyWeakValueSegment<K, V>> map, int initialCapacity) {
/*  543 */         return new MapMakerInternalMap.StrongKeyWeakValueSegment<>(map, initialCapacity);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       @CheckForNull
/*      */       public MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> copy(MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> entry, @CheckForNull MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> newNext) {
/*  552 */         if (MapMakerInternalMap.Segment.isCollected(entry)) {
/*  553 */           return null;
/*      */         }
/*  555 */         MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> newEntry = newEntry(segment, entry.key, entry.hash, newNext);
/*  556 */         newEntry.valueReference = entry.valueReference.copyFor(segment.queueForValues, newEntry);
/*  557 */         return newEntry;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public void setValue(MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> entry, V value) {
/*  563 */         MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>> previous = entry.valueReference;
/*  564 */         entry.valueReference = new MapMakerInternalMap.WeakValueReferenceImpl<>(segment.queueForValues, value, entry);
/*  565 */         previous.clear();
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> newEntry(MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> segment, K key, int hash, @CheckForNull MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> next)
/*      */       {
/*  574 */         return (next == null) ? 
/*  575 */           new MapMakerInternalMap.StrongKeyWeakValueEntry<>(key, hash) : 
/*  576 */           new MapMakerInternalMap.StrongKeyWeakValueEntry.LinkedStrongKeyWeakValueEntry<>(key, hash, next); } } } static final class Helper<K, V> implements InternalEntryHelper<K, V, StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>> { private static final Helper<?, ?> INSTANCE = new Helper(); static <K, V> Helper<K, V> instance() { return (Helper)INSTANCE; } public MapMakerInternalMap.Strength keyStrength() { return MapMakerInternalMap.Strength.STRONG; } public MapMakerInternalMap.Strength valueStrength() { return MapMakerInternalMap.Strength.WEAK; } public MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>, MapMakerInternalMap.StrongKeyWeakValueSegment<K, V>> map, int initialCapacity) { return new MapMakerInternalMap.StrongKeyWeakValueSegment<>(map, initialCapacity); } public MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> newEntry(MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> segment, K key, int hash, @CheckForNull MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> next) { return (next == null) ? new MapMakerInternalMap.StrongKeyWeakValueEntry<>(key, hash) : new MapMakerInternalMap.StrongKeyWeakValueEntry.LinkedStrongKeyWeakValueEntry<>(key, hash, next); } @CheckForNull
/*      */     public MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> copy(MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> entry, @CheckForNull MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> newNext) { if (MapMakerInternalMap.Segment.isCollected(entry))
/*      */         return null; 
/*      */       MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> newEntry = newEntry(segment, entry.key, entry.hash, newNext);
/*      */       newEntry.valueReference = entry.valueReference.copyFor(segment.queueForValues, newEntry);
/*      */       return newEntry; } public void setValue(MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> entry, V value) {
/*      */       MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>> previous = entry.valueReference;
/*      */       entry.valueReference = new MapMakerInternalMap.WeakValueReferenceImpl<>(segment.queueForValues, value, entry);
/*      */       previous.clear();
/*      */     } }
/*      */    static class StrongKeyDummyValueEntry<K> extends AbstractStrongKeyEntry<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>> implements StrongValueEntry<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>> { private StrongKeyDummyValueEntry(K key, int hash) {
/*  587 */       super(key, hash);
/*      */     }
/*      */ 
/*      */     
/*      */     public final MapMaker.Dummy getValue() {
/*  592 */       return MapMaker.Dummy.VALUE;
/*      */     }
/*      */     
/*      */     private static final class LinkedStrongKeyDummyValueEntry<K>
/*      */       extends StrongKeyDummyValueEntry<K> {
/*      */       private final MapMakerInternalMap.StrongKeyDummyValueEntry<K> next;
/*      */       
/*      */       LinkedStrongKeyDummyValueEntry(K key, int hash, MapMakerInternalMap.StrongKeyDummyValueEntry<K> next) {
/*  600 */         super(key, hash);
/*  601 */         this.next = next;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.StrongKeyDummyValueEntry<K> getNext() {
/*  606 */         return this.next;
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static final class Helper<K>
/*      */       implements MapMakerInternalMap.InternalEntryHelper<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>, MapMakerInternalMap.StrongKeyDummyValueSegment<K>>
/*      */     {
/*  617 */       private static final Helper<?> INSTANCE = new Helper();
/*      */ 
/*      */       
/*      */       static <K> Helper<K> instance() {
/*  621 */         return (Helper)INSTANCE;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength keyStrength() {
/*  626 */         return MapMakerInternalMap.Strength.STRONG;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength valueStrength() {
/*  631 */         return MapMakerInternalMap.Strength.STRONG;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.StrongKeyDummyValueSegment<K> newSegment(MapMakerInternalMap<K, MapMaker.Dummy, MapMakerInternalMap.StrongKeyDummyValueEntry<K>, MapMakerInternalMap.StrongKeyDummyValueSegment<K>> map, int initialCapacity) {
/*  639 */         return new MapMakerInternalMap.StrongKeyDummyValueSegment<>(map, initialCapacity);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.StrongKeyDummyValueEntry<K> copy(MapMakerInternalMap.StrongKeyDummyValueSegment<K> segment, MapMakerInternalMap.StrongKeyDummyValueEntry<K> entry, @CheckForNull MapMakerInternalMap.StrongKeyDummyValueEntry<K> newNext) {
/*  647 */         return newEntry(segment, entry.key, entry.hash, newNext);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public void setValue(MapMakerInternalMap.StrongKeyDummyValueSegment<K> segment, MapMakerInternalMap.StrongKeyDummyValueEntry<K> entry, MapMaker.Dummy value) {}
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.StrongKeyDummyValueEntry<K> newEntry(MapMakerInternalMap.StrongKeyDummyValueSegment<K> segment, K key, int hash, @CheckForNull MapMakerInternalMap.StrongKeyDummyValueEntry<K> next)
/*      */       {
/*  660 */         return (next == null) ? 
/*  661 */           new MapMakerInternalMap.StrongKeyDummyValueEntry<>(key, hash) : 
/*  662 */           new MapMakerInternalMap.StrongKeyDummyValueEntry.LinkedStrongKeyDummyValueEntry<>(key, hash, next); } } } static final class Helper<K> implements InternalEntryHelper<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>, StrongKeyDummyValueSegment<K>> { private static final Helper<?> INSTANCE = new Helper(); static <K> Helper<K> instance() { return (Helper)INSTANCE; } public MapMakerInternalMap.Strength keyStrength() { return MapMakerInternalMap.Strength.STRONG; } public MapMakerInternalMap.StrongKeyDummyValueEntry<K> newEntry(MapMakerInternalMap.StrongKeyDummyValueSegment<K> segment, K key, int hash, @CheckForNull MapMakerInternalMap.StrongKeyDummyValueEntry<K> next) { return (next == null) ? new MapMakerInternalMap.StrongKeyDummyValueEntry<>(key, hash) : new MapMakerInternalMap.StrongKeyDummyValueEntry.LinkedStrongKeyDummyValueEntry<>(key, hash, next); }
/*      */      public MapMakerInternalMap.Strength valueStrength() {
/*      */       return MapMakerInternalMap.Strength.STRONG;
/*      */     } public MapMakerInternalMap.StrongKeyDummyValueSegment<K> newSegment(MapMakerInternalMap<K, MapMaker.Dummy, MapMakerInternalMap.StrongKeyDummyValueEntry<K>, MapMakerInternalMap.StrongKeyDummyValueSegment<K>> map, int initialCapacity) {
/*      */       return new MapMakerInternalMap.StrongKeyDummyValueSegment<>(map, initialCapacity);
/*      */     } public MapMakerInternalMap.StrongKeyDummyValueEntry<K> copy(MapMakerInternalMap.StrongKeyDummyValueSegment<K> segment, MapMakerInternalMap.StrongKeyDummyValueEntry<K> entry, @CheckForNull MapMakerInternalMap.StrongKeyDummyValueEntry<K> newNext) {
/*      */       return newEntry(segment, entry.key, entry.hash, newNext);
/*      */     }
/*      */     public void setValue(MapMakerInternalMap.StrongKeyDummyValueSegment<K> segment, MapMakerInternalMap.StrongKeyDummyValueEntry<K> entry, MapMaker.Dummy value) {} }
/*      */   static abstract class AbstractWeakKeyEntry<K, V, E extends InternalEntry<K, V, E>> extends WeakReference<K> implements InternalEntry<K, V, E> { final int hash;
/*      */     AbstractWeakKeyEntry(ReferenceQueue<K> queue, K key, int hash) {
/*  673 */       super(key, queue);
/*  674 */       this.hash = hash;
/*      */     }
/*      */ 
/*      */     
/*      */     public final K getKey() {
/*  679 */       return get();
/*      */     }
/*      */ 
/*      */     
/*      */     public final int getHash() {
/*  684 */       return this.hash;
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public E getNext() {
/*  690 */       return null;
/*      */     } }
/*      */ 
/*      */ 
/*      */   
/*      */   static class WeakKeyDummyValueEntry<K>
/*      */     extends AbstractWeakKeyEntry<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>>
/*      */     implements StrongValueEntry<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>>
/*      */   {
/*      */     private WeakKeyDummyValueEntry(ReferenceQueue<K> queue, K key, int hash) {
/*  700 */       super(queue, key, hash);
/*      */     }
/*      */ 
/*      */     
/*      */     public final MapMaker.Dummy getValue() {
/*  705 */       return MapMaker.Dummy.VALUE;
/*      */     }
/*      */     
/*      */     private static final class LinkedWeakKeyDummyValueEntry<K>
/*      */       extends WeakKeyDummyValueEntry<K> {
/*      */       private final MapMakerInternalMap.WeakKeyDummyValueEntry<K> next;
/*      */       
/*      */       private LinkedWeakKeyDummyValueEntry(ReferenceQueue<K> queue, K key, int hash, MapMakerInternalMap.WeakKeyDummyValueEntry<K> next) {
/*  713 */         super(queue, key, hash);
/*  714 */         this.next = next;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakKeyDummyValueEntry<K> getNext() {
/*  719 */         return this.next;
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static final class Helper<K>
/*      */       implements MapMakerInternalMap.InternalEntryHelper<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>, MapMakerInternalMap.WeakKeyDummyValueSegment<K>>
/*      */     {
/*  730 */       private static final Helper<?> INSTANCE = new Helper();
/*      */ 
/*      */       
/*      */       static <K> Helper<K> instance() {
/*  734 */         return (Helper)INSTANCE;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength keyStrength() {
/*  739 */         return MapMakerInternalMap.Strength.WEAK;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength valueStrength() {
/*  744 */         return MapMakerInternalMap.Strength.STRONG;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakKeyDummyValueSegment<K> newSegment(MapMakerInternalMap<K, MapMaker.Dummy, MapMakerInternalMap.WeakKeyDummyValueEntry<K>, MapMakerInternalMap.WeakKeyDummyValueSegment<K>> map, int initialCapacity) {
/*  751 */         return new MapMakerInternalMap.WeakKeyDummyValueSegment<>(map, initialCapacity);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       @CheckForNull
/*      */       public MapMakerInternalMap.WeakKeyDummyValueEntry<K> copy(MapMakerInternalMap.WeakKeyDummyValueSegment<K> segment, MapMakerInternalMap.WeakKeyDummyValueEntry<K> entry, @CheckForNull MapMakerInternalMap.WeakKeyDummyValueEntry<K> newNext) {
/*  760 */         K key = entry.getKey();
/*  761 */         if (key == null)
/*      */         {
/*  763 */           return null;
/*      */         }
/*  765 */         return newEntry(segment, key, entry.hash, newNext);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public void setValue(MapMakerInternalMap.WeakKeyDummyValueSegment<K> segment, MapMakerInternalMap.WeakKeyDummyValueEntry<K> entry, MapMaker.Dummy value) {}
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakKeyDummyValueEntry<K> newEntry(MapMakerInternalMap.WeakKeyDummyValueSegment<K> segment, K key, int hash, @CheckForNull MapMakerInternalMap.WeakKeyDummyValueEntry<K> next)
/*      */       {
/*  778 */         return (next == null) ? 
/*  779 */           new MapMakerInternalMap.WeakKeyDummyValueEntry<>(segment.queueForKeys, key, hash) : 
/*  780 */           new MapMakerInternalMap.WeakKeyDummyValueEntry.LinkedWeakKeyDummyValueEntry<>(segment.queueForKeys, key, hash, next); } } } static final class Helper<K> implements InternalEntryHelper<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>, WeakKeyDummyValueSegment<K>> { private static final Helper<?> INSTANCE = new Helper(); static <K> Helper<K> instance() { return (Helper)INSTANCE; } public MapMakerInternalMap.Strength keyStrength() { return MapMakerInternalMap.Strength.WEAK; } public MapMakerInternalMap.WeakKeyDummyValueEntry<K> newEntry(MapMakerInternalMap.WeakKeyDummyValueSegment<K> segment, K key, int hash, @CheckForNull MapMakerInternalMap.WeakKeyDummyValueEntry<K> next) { return (next == null) ? new MapMakerInternalMap.WeakKeyDummyValueEntry<>(segment.queueForKeys, key, hash) : new MapMakerInternalMap.WeakKeyDummyValueEntry.LinkedWeakKeyDummyValueEntry<>(segment.queueForKeys, key, hash, next, null); } public MapMakerInternalMap.Strength valueStrength() { return MapMakerInternalMap.Strength.STRONG; } public MapMakerInternalMap.WeakKeyDummyValueSegment<K> newSegment(MapMakerInternalMap<K, MapMaker.Dummy, MapMakerInternalMap.WeakKeyDummyValueEntry<K>, MapMakerInternalMap.WeakKeyDummyValueSegment<K>> map, int initialCapacity) { return new MapMakerInternalMap.WeakKeyDummyValueSegment<>(map, initialCapacity); } @CheckForNull
/*      */     public MapMakerInternalMap.WeakKeyDummyValueEntry<K> copy(MapMakerInternalMap.WeakKeyDummyValueSegment<K> segment, MapMakerInternalMap.WeakKeyDummyValueEntry<K> entry, @CheckForNull MapMakerInternalMap.WeakKeyDummyValueEntry<K> newNext) {
/*      */       K key = entry.getKey();
/*      */       if (key == null)
/*      */         return null; 
/*      */       return newEntry(segment, key, entry.hash, newNext);
/*      */     }
/*      */     public void setValue(MapMakerInternalMap.WeakKeyDummyValueSegment<K> segment, MapMakerInternalMap.WeakKeyDummyValueEntry<K> entry, MapMaker.Dummy value) {} }
/*      */   static class WeakKeyStrongValueEntry<K, V> extends AbstractWeakKeyEntry<K, V, WeakKeyStrongValueEntry<K, V>> implements StrongValueEntry<K, V, WeakKeyStrongValueEntry<K, V>> { @CheckForNull
/*  789 */     private volatile V value = null;
/*      */     
/*      */     private WeakKeyStrongValueEntry(ReferenceQueue<K> queue, K key, int hash) {
/*  792 */       super(queue, key, hash);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public final V getValue() {
/*  798 */       return this.value;
/*      */     }
/*      */     
/*      */     private static final class LinkedWeakKeyStrongValueEntry<K, V>
/*      */       extends WeakKeyStrongValueEntry<K, V>
/*      */     {
/*      */       private final MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> next;
/*      */       
/*      */       private LinkedWeakKeyStrongValueEntry(ReferenceQueue<K> queue, K key, int hash, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> next) {
/*  807 */         super(queue, key, hash);
/*  808 */         this.next = next;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> getNext() {
/*  813 */         return this.next;
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     static final class Helper<K, V>
/*      */       implements MapMakerInternalMap.InternalEntryHelper<K, V, WeakKeyStrongValueEntry<K, V>, MapMakerInternalMap.WeakKeyStrongValueSegment<K, V>>
/*      */     {
/*  821 */       private static final Helper<?, ?> INSTANCE = new Helper();
/*      */ 
/*      */       
/*      */       static <K, V> Helper<K, V> instance() {
/*  825 */         return (Helper)INSTANCE;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength keyStrength() {
/*  830 */         return MapMakerInternalMap.Strength.WEAK;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength valueStrength() {
/*  835 */         return MapMakerInternalMap.Strength.STRONG;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V>, MapMakerInternalMap.WeakKeyStrongValueSegment<K, V>> map, int initialCapacity) {
/*  843 */         return new MapMakerInternalMap.WeakKeyStrongValueSegment<>(map, initialCapacity);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       @CheckForNull
/*      */       public MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> copy(MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> entry, @CheckForNull MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> newNext) {
/*  852 */         K key = entry.getKey();
/*  853 */         if (key == null)
/*      */         {
/*  855 */           return null;
/*      */         }
/*  857 */         MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> newEntry = newEntry(segment, key, entry.hash, newNext);
/*  858 */         newEntry.value = entry.value;
/*  859 */         return newEntry;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public void setValue(MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> entry, V value) {
/*  865 */         entry.value = value;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> newEntry(MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> segment, K key, int hash, @CheckForNull MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> next)
/*      */       {
/*  874 */         return (next == null) ? 
/*  875 */           new MapMakerInternalMap.WeakKeyStrongValueEntry<>(segment.queueForKeys, key, hash) : 
/*  876 */           new MapMakerInternalMap.WeakKeyStrongValueEntry.LinkedWeakKeyStrongValueEntry<>(segment.queueForKeys, key, hash, next); } } } static final class Helper<K, V> implements InternalEntryHelper<K, V, WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>> { private static final Helper<?, ?> INSTANCE = new Helper(); static <K, V> Helper<K, V> instance() { return (Helper)INSTANCE; } public MapMakerInternalMap.Strength keyStrength() { return MapMakerInternalMap.Strength.WEAK; } public MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> newEntry(MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> segment, K key, int hash, @CheckForNull MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> next) { return (next == null) ? new MapMakerInternalMap.WeakKeyStrongValueEntry<>(segment.queueForKeys, key, hash) : new MapMakerInternalMap.WeakKeyStrongValueEntry.LinkedWeakKeyStrongValueEntry<>(segment.queueForKeys, key, hash, next, null); } public MapMakerInternalMap.Strength valueStrength() { return MapMakerInternalMap.Strength.STRONG; }
/*      */     public MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V>, MapMakerInternalMap.WeakKeyStrongValueSegment<K, V>> map, int initialCapacity) { return new MapMakerInternalMap.WeakKeyStrongValueSegment<>(map, initialCapacity); }
/*      */     @CheckForNull
/*      */     public MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> copy(MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> entry, @CheckForNull MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> newNext) { K key = entry.getKey();
/*      */       if (key == null)
/*      */         return null; 
/*      */       MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> newEntry = newEntry(segment, key, entry.hash, newNext);
/*      */       newEntry.value = entry.value;
/*      */       return newEntry; }
/*      */     public void setValue(MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> entry, V value) { entry.value = value; } }
/*  886 */   static class WeakKeyWeakValueEntry<K, V> extends AbstractWeakKeyEntry<K, V, WeakKeyWeakValueEntry<K, V>> implements WeakValueEntry<K, V, WeakKeyWeakValueEntry<K, V>> { private volatile MapMakerInternalMap.WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> valueReference = MapMakerInternalMap.unsetWeakValueReference();
/*      */     
/*      */     WeakKeyWeakValueEntry(ReferenceQueue<K> queue, K key, int hash) {
/*  889 */       super(queue, key, hash);
/*      */     }
/*      */ 
/*      */     
/*      */     public final V getValue() {
/*  894 */       return this.valueReference.get();
/*      */     }
/*      */ 
/*      */     
/*      */     public final MapMakerInternalMap.WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> getValueReference() {
/*  899 */       return this.valueReference;
/*      */     }
/*      */     
/*      */     private static final class LinkedWeakKeyWeakValueEntry<K, V>
/*      */       extends WeakKeyWeakValueEntry<K, V>
/*      */     {
/*      */       private final MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> next;
/*      */       
/*      */       LinkedWeakKeyWeakValueEntry(ReferenceQueue<K> queue, K key, int hash, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> next) {
/*  908 */         super(queue, key, hash);
/*  909 */         this.next = next;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> getNext() {
/*  914 */         return this.next;
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     static final class Helper<K, V>
/*      */       implements MapMakerInternalMap.InternalEntryHelper<K, V, WeakKeyWeakValueEntry<K, V>, MapMakerInternalMap.WeakKeyWeakValueSegment<K, V>>
/*      */     {
/*  922 */       private static final Helper<?, ?> INSTANCE = new Helper();
/*      */ 
/*      */       
/*      */       static <K, V> Helper<K, V> instance() {
/*  926 */         return (Helper)INSTANCE;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength keyStrength() {
/*  931 */         return MapMakerInternalMap.Strength.WEAK;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength valueStrength() {
/*  936 */         return MapMakerInternalMap.Strength.WEAK;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>, MapMakerInternalMap.WeakKeyWeakValueSegment<K, V>> map, int initialCapacity) {
/*  943 */         return new MapMakerInternalMap.WeakKeyWeakValueSegment<>(map, initialCapacity);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       @CheckForNull
/*      */       public MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> copy(MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> entry, @CheckForNull MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> newNext) {
/*  952 */         K key = entry.getKey();
/*  953 */         if (key == null)
/*      */         {
/*  955 */           return null;
/*      */         }
/*  957 */         if (MapMakerInternalMap.Segment.isCollected(entry)) {
/*  958 */           return null;
/*      */         }
/*  960 */         MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> newEntry = newEntry(segment, key, entry.hash, newNext);
/*  961 */         newEntry.valueReference = entry.valueReference.copyFor(segment.queueForValues, newEntry);
/*  962 */         return newEntry;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public void setValue(MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> entry, V value) {
/*  968 */         MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>> previous = entry.valueReference;
/*  969 */         entry.valueReference = new MapMakerInternalMap.WeakValueReferenceImpl<>(segment.queueForValues, value, entry);
/*  970 */         previous.clear();
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> newEntry(MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> segment, K key, int hash, @CheckForNull MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> next)
/*      */       {
/*  979 */         return (next == null) ? 
/*  980 */           new MapMakerInternalMap.WeakKeyWeakValueEntry<>(segment.queueForKeys, key, hash) : 
/*  981 */           new MapMakerInternalMap.WeakKeyWeakValueEntry.LinkedWeakKeyWeakValueEntry<>(segment.queueForKeys, key, hash, next); } } } static final class Helper<K, V> implements InternalEntryHelper<K, V, WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>> { private static final Helper<?, ?> INSTANCE = new Helper(); static <K, V> Helper<K, V> instance() { return (Helper)INSTANCE; } public MapMakerInternalMap.Strength keyStrength() { return MapMakerInternalMap.Strength.WEAK; } public MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> newEntry(MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> segment, K key, int hash, @CheckForNull MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> next) { return (next == null) ? new MapMakerInternalMap.WeakKeyWeakValueEntry<>(segment.queueForKeys, key, hash) : new MapMakerInternalMap.WeakKeyWeakValueEntry.LinkedWeakKeyWeakValueEntry<>(segment.queueForKeys, key, hash, next); }
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.Strength valueStrength() {
/*      */       return MapMakerInternalMap.Strength.WEAK;
/*      */     }
/*      */     
/*      */     public MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>, MapMakerInternalMap.WeakKeyWeakValueSegment<K, V>> map, int initialCapacity) {
/*      */       return new MapMakerInternalMap.WeakKeyWeakValueSegment<>(map, initialCapacity);
/*      */     }
/*      */     
/*      */     @CheckForNull
/*      */     public MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> copy(MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> entry, @CheckForNull MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> newNext) {
/*      */       K key = entry.getKey();
/*      */       if (key == null)
/*      */         return null; 
/*      */       if (MapMakerInternalMap.Segment.isCollected(entry))
/*      */         return null; 
/*      */       MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> newEntry = newEntry(segment, key, entry.hash, newNext);
/*      */       newEntry.valueReference = entry.valueReference.copyFor(segment.queueForValues, newEntry);
/*      */       return newEntry;
/*      */     }
/*      */     
/*      */     public void setValue(MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> entry, V value) {
/*      */       MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>> previous = entry.valueReference;
/*      */       entry.valueReference = new MapMakerInternalMap.WeakValueReferenceImpl<>(segment.queueForValues, value, entry);
/*      */       previous.clear();
/*      */     } }
/*      */ 
/*      */   
/*      */   static final class DummyInternalEntry
/*      */     implements InternalEntry<Object, Object, DummyInternalEntry>
/*      */   {
/*      */     private DummyInternalEntry() {
/* 1015 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */     
/*      */     public DummyInternalEntry getNext() {
/* 1020 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */     
/*      */     public int getHash() {
/* 1025 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getKey() {
/* 1030 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getValue() {
/* 1035 */       throw new AssertionError();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1043 */   static final WeakValueReference<Object, Object, DummyInternalEntry> UNSET_WEAK_VALUE_REFERENCE = new WeakValueReference<Object, Object, DummyInternalEntry>()
/*      */     {
/*      */       @CheckForNull
/*      */       public MapMakerInternalMap.DummyInternalEntry getEntry()
/*      */       {
/* 1048 */         return null;
/*      */       }
/*      */ 
/*      */       
/*      */       public void clear() {}
/*      */ 
/*      */       
/*      */       @CheckForNull
/*      */       public Object get() {
/* 1057 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakValueReference<Object, Object, MapMakerInternalMap.DummyInternalEntry> copyFor(ReferenceQueue<Object> queue, MapMakerInternalMap.DummyInternalEntry entry) {
/* 1063 */         return this;
/*      */       }
/*      */     }; @LazyInit
/*      */   @CheckForNull
/*      */   transient Set<K> keySet; @LazyInit
/*      */   @CheckForNull
/*      */   transient Collection<V> values; @LazyInit
/*      */   @CheckForNull
/*      */   transient Set<Map.Entry<K, V>> entrySet; private static final long serialVersionUID = 5L; static final class WeakValueReferenceImpl<K, V, E extends InternalEntry<K, V, E>> extends WeakReference<V> implements WeakValueReference<K, V, E> { @Weak
/*      */     final E entry; WeakValueReferenceImpl(ReferenceQueue<V> queue, V referent, E entry) {
/* 1073 */       super(referent, queue);
/* 1074 */       this.entry = entry;
/*      */     }
/*      */ 
/*      */     
/*      */     public E getEntry() {
/* 1079 */       return this.entry;
/*      */     }
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakValueReference<K, V, E> copyFor(ReferenceQueue<V> queue, E entry) {
/* 1084 */       return new WeakValueReferenceImpl(queue, get(), entry);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/* 1100 */     h += h << 15 ^ 0xFFFFCD7D;
/* 1101 */     h ^= h >>> 10;
/* 1102 */     h += h << 3;
/* 1103 */     h ^= h >>> 6;
/* 1104 */     h += (h << 2) + (h << 14);
/* 1105 */     return h ^ h >>> 16;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   E copyEntry(E original, E newNext) {
/* 1114 */     int hash = original.getHash();
/* 1115 */     return segmentFor(hash).copyEntry(original, newNext);
/*      */   }
/*      */   
/*      */   int hash(Object key) {
/* 1119 */     int h = this.keyEquivalence.hash(key);
/* 1120 */     return rehash(h);
/*      */   }
/*      */   
/*      */   void reclaimValue(WeakValueReference<K, V, E> valueReference) {
/* 1124 */     E entry = valueReference.getEntry();
/* 1125 */     int hash = entry.getHash();
/* 1126 */     segmentFor(hash).reclaimValue((K)entry.getKey(), hash, valueReference);
/*      */   }
/*      */   
/*      */   void reclaimKey(E entry) {
/* 1130 */     int hash = entry.getHash();
/* 1131 */     segmentFor(hash).reclaimKey(entry, hash);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   boolean isLiveForTesting(InternalEntry<K, V, ?> entry) {
/* 1140 */     return (segmentFor(entry.getHash()).getLiveValueForTesting(entry) != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Segment<K, V, E, S> segmentFor(int hash) {
/* 1151 */     return this.segments[hash >>> this.segmentShift & this.segmentMask];
/*      */   }
/*      */   
/*      */   Segment<K, V, E, S> createSegment(int initialCapacity) {
/* 1155 */     return (Segment<K, V, E, S>)this.entryHelper.newSegment(this, initialCapacity);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   V getLiveValue(E entry) {
/* 1164 */     if (entry.getKey() == null) {
/* 1165 */       return null;
/*      */     }
/* 1167 */     return (V)entry.getValue();
/*      */   }
/*      */ 
/*      */   
/*      */   final Segment<K, V, E, S>[] newSegmentArray(int ssize) {
/* 1172 */     return (Segment<K, V, E, S>[])new Segment[ssize];
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
/*      */   static abstract class Segment<K, V, E extends InternalEntry<K, V, E>, S extends Segment<K, V, E, S>>
/*      */     extends ReentrantLock
/*      */   {
/*      */     @Weak
/*      */     final MapMakerInternalMap<K, V, E, S> map;
/*      */ 
/*      */ 
/*      */ 
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
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     volatile AtomicReferenceArray<E> table;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1243 */     final AtomicInteger readCount = new AtomicInteger();
/*      */     
/*      */     Segment(MapMakerInternalMap<K, V, E, S> map, int initialCapacity) {
/* 1246 */       this.map = map;
/* 1247 */       initTable(newEntryArray(initialCapacity));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract S self();
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void maybeDrainReferenceQueues() {}
/*      */ 
/*      */ 
/*      */     
/*      */     void maybeClearReferenceQueues() {}
/*      */ 
/*      */ 
/*      */     
/*      */     void setValue(E entry, V value) {
/* 1267 */       this.map.entryHelper.setValue(self(), entry, value);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     E copyEntry(E original, E newNext) {
/* 1273 */       return this.map.entryHelper.copy(self(), original, newNext);
/*      */     }
/*      */     
/*      */     AtomicReferenceArray<E> newEntryArray(int size) {
/* 1277 */       return new AtomicReferenceArray<>(size);
/*      */     }
/*      */     
/*      */     void initTable(AtomicReferenceArray<E> newTable) {
/* 1281 */       this.threshold = newTable.length() * 3 / 4;
/* 1282 */       this.table = newTable;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract E castForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> param1InternalEntry);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     ReferenceQueue<K> getKeyReferenceQueueForTesting() {
/* 1298 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */     
/*      */     ReferenceQueue<V> getValueReferenceQueueForTesting() {
/* 1303 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */     
/*      */     MapMakerInternalMap.WeakValueReference<K, V, E> getWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 1308 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     MapMakerInternalMap.WeakValueReference<K, V, E> newWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry, V value) {
/* 1317 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void setWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry, MapMakerInternalMap.WeakValueReference<K, V, ? extends MapMakerInternalMap.InternalEntry<K, V, ?>> valueReference) {
/* 1327 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void setTableEntryForTesting(int i, MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 1334 */       this.table.set(i, castForTesting(entry));
/*      */     }
/*      */ 
/*      */     
/*      */     E copyForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry, @CheckForNull MapMakerInternalMap.InternalEntry<K, V, ?> newNext) {
/* 1339 */       return this.map.entryHelper.copy(self(), castForTesting(entry), castForTesting(newNext));
/*      */     }
/*      */ 
/*      */     
/*      */     void setValueForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry, V value) {
/* 1344 */       this.map.entryHelper.setValue(self(), castForTesting(entry), value);
/*      */     }
/*      */ 
/*      */     
/*      */     E newEntryForTesting(K key, int hash, @CheckForNull MapMakerInternalMap.InternalEntry<K, V, ?> next) {
/* 1349 */       return this.map.entryHelper.newEntry(self(), key, hash, castForTesting(next));
/*      */     }
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     boolean removeTableEntryForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 1355 */       return removeEntryForTesting(castForTesting(entry));
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     E removeFromChainForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> first, MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 1361 */       return removeFromChain(castForTesting(first), castForTesting(entry));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     V getLiveValueForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 1369 */       return getLiveValue(castForTesting(entry));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void tryDrainReferenceQueues() {
/* 1376 */       if (tryLock()) {
/*      */         try {
/* 1378 */           maybeDrainReferenceQueues();
/*      */         } finally {
/* 1380 */           unlock();
/*      */         } 
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void drainKeyReferenceQueue(ReferenceQueue<K> keyReferenceQueue) {
/* 1388 */       int i = 0; Reference<? extends K> ref;
/* 1389 */       while ((ref = keyReferenceQueue.poll()) != null) {
/*      */         
/* 1391 */         MapMakerInternalMap.InternalEntry internalEntry = (MapMakerInternalMap.InternalEntry)ref;
/* 1392 */         this.map.reclaimKey((E)internalEntry);
/* 1393 */         if (++i == 16) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void drainValueReferenceQueue(ReferenceQueue<V> valueReferenceQueue) {
/* 1402 */       int i = 0; Reference<? extends V> ref;
/* 1403 */       while ((ref = valueReferenceQueue.poll()) != null) {
/*      */         
/* 1405 */         MapMakerInternalMap.WeakValueReference<K, V, E> valueReference = (MapMakerInternalMap.WeakValueReference)ref;
/* 1406 */         this.map.reclaimValue(valueReference);
/* 1407 */         if (++i == 16) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     <T> void clearReferenceQueue(ReferenceQueue<T> referenceQueue) {
/* 1414 */       while (referenceQueue.poll() != null);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     E getFirst(int hash) {
/* 1421 */       AtomicReferenceArray<E> table = this.table;
/* 1422 */       return table.get(hash & table.length() - 1);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     E getEntry(Object key, int hash) {
/* 1429 */       if (this.count != 0) {
/* 1430 */         for (E e = getFirst(hash); e != null; e = (E)e.getNext()) {
/* 1431 */           if (e.getHash() == hash) {
/*      */ 
/*      */ 
/*      */             
/* 1435 */             K entryKey = (K)e.getKey();
/* 1436 */             if (entryKey == null) {
/* 1437 */               tryDrainReferenceQueues();
/*      */ 
/*      */             
/*      */             }
/* 1441 */             else if (this.map.keyEquivalence.equivalent(key, entryKey)) {
/* 1442 */               return e;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       }
/* 1447 */       return null;
/*      */     }
/*      */     
/*      */     @CheckForNull
/*      */     E getLiveEntry(Object key, int hash) {
/* 1452 */       return getEntry(key, hash);
/*      */     }
/*      */     
/*      */     @CheckForNull
/*      */     V get(Object key, int hash) {
/*      */       try {
/* 1458 */         E e = getLiveEntry(key, hash);
/* 1459 */         if (e == null) {
/* 1460 */           return null;
/*      */         }
/*      */         
/* 1463 */         V value = (V)e.getValue();
/* 1464 */         if (value == null) {
/* 1465 */           tryDrainReferenceQueues();
/*      */         }
/* 1467 */         return value;
/*      */       } finally {
/* 1469 */         postReadCleanup();
/*      */       } 
/*      */     }
/*      */     
/*      */     boolean containsKey(Object key, int hash) {
/*      */       try {
/* 1475 */         if (this.count != 0) {
/* 1476 */           E e = getLiveEntry(key, hash);
/* 1477 */           return (e != null && e.getValue() != null);
/*      */         } 
/*      */         
/* 1480 */         return false;
/*      */       } finally {
/* 1482 */         postReadCleanup();
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
/* 1493 */         if (this.count != 0) {
/* 1494 */           AtomicReferenceArray<E> table = this.table;
/* 1495 */           int length = table.length();
/* 1496 */           for (int i = 0; i < length; i++) {
/* 1497 */             for (MapMakerInternalMap.InternalEntry internalEntry = (MapMakerInternalMap.InternalEntry)table.get(i); internalEntry != null; internalEntry = (MapMakerInternalMap.InternalEntry)internalEntry.getNext()) {
/* 1498 */               V entryValue = getLiveValue((E)internalEntry);
/* 1499 */               if (entryValue != null)
/*      */               {
/*      */                 
/* 1502 */                 if (this.map.valueEquivalence().equivalent(value, entryValue)) {
/* 1503 */                   return true;
/*      */                 }
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } 
/* 1509 */         return false;
/*      */       } finally {
/* 1511 */         postReadCleanup();
/*      */       } 
/*      */     }
/*      */     
/*      */     @CheckForNull
/*      */     V put(K key, int hash, V value, boolean onlyIfAbsent) {
/* 1517 */       lock();
/*      */       try {
/* 1519 */         preWriteCleanup();
/*      */         
/* 1521 */         int newCount = this.count + 1;
/* 1522 */         if (newCount > this.threshold) {
/* 1523 */           expand();
/* 1524 */           newCount = this.count + 1;
/*      */         } 
/*      */         
/* 1527 */         AtomicReferenceArray<E> table = this.table;
/* 1528 */         int index = hash & table.length() - 1;
/* 1529 */         MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */ 
/*      */         
/* 1532 */         for (MapMakerInternalMap.InternalEntry internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1533 */           K entryKey = (K)internalEntry2.getKey();
/* 1534 */           if (internalEntry2.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 1536 */             .equivalent(key, entryKey)) {
/*      */ 
/*      */             
/* 1539 */             V entryValue = (V)internalEntry2.getValue();
/*      */             
/* 1541 */             if (entryValue == null) {
/* 1542 */               this.modCount++;
/* 1543 */               setValue((E)internalEntry2, value);
/* 1544 */               newCount = this.count;
/* 1545 */               this.count = newCount;
/* 1546 */               return null;
/* 1547 */             }  if (onlyIfAbsent)
/*      */             {
/*      */ 
/*      */               
/* 1551 */               return entryValue;
/*      */             }
/*      */             
/* 1554 */             this.modCount++;
/* 1555 */             setValue((E)internalEntry2, value);
/* 1556 */             return entryValue;
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 1562 */         this.modCount++;
/* 1563 */         E newEntry = this.map.entryHelper.newEntry(self(), key, hash, (E)internalEntry1);
/* 1564 */         setValue(newEntry, value);
/* 1565 */         table.set(index, newEntry);
/* 1566 */         this.count = newCount;
/* 1567 */         return null;
/*      */       } finally {
/* 1569 */         unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void expand() {
/* 1576 */       AtomicReferenceArray<E> oldTable = this.table;
/* 1577 */       int oldCapacity = oldTable.length();
/* 1578 */       if (oldCapacity >= 1073741824) {
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
/* 1592 */       int newCount = this.count;
/* 1593 */       AtomicReferenceArray<E> newTable = newEntryArray(oldCapacity << 1);
/* 1594 */       this.threshold = newTable.length() * 3 / 4;
/* 1595 */       int newMask = newTable.length() - 1;
/* 1596 */       for (int oldIndex = 0; oldIndex < oldCapacity; oldIndex++) {
/*      */ 
/*      */         
/* 1599 */         MapMakerInternalMap.InternalEntry internalEntry = (MapMakerInternalMap.InternalEntry)oldTable.get(oldIndex);
/*      */         
/* 1601 */         if (internalEntry != null) {
/* 1602 */           E next = (E)internalEntry.getNext();
/* 1603 */           int headIndex = internalEntry.getHash() & newMask;
/*      */ 
/*      */           
/* 1606 */           if (next == null) {
/* 1607 */             newTable.set(headIndex, (E)internalEntry);
/*      */           } else {
/*      */             E e1;
/*      */ 
/*      */             
/* 1612 */             MapMakerInternalMap.InternalEntry internalEntry1 = internalEntry;
/* 1613 */             int tailIndex = headIndex;
/* 1614 */             for (E e = next; e != null; e = (E)e.getNext()) {
/* 1615 */               int newIndex = e.getHash() & newMask;
/* 1616 */               if (newIndex != tailIndex) {
/*      */                 
/* 1618 */                 tailIndex = newIndex;
/* 1619 */                 e1 = e;
/*      */               } 
/*      */             } 
/* 1622 */             newTable.set(tailIndex, e1);
/*      */ 
/*      */             
/* 1625 */             for (MapMakerInternalMap.InternalEntry internalEntry2 = internalEntry; internalEntry2 != e1; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1626 */               int newIndex = internalEntry2.getHash() & newMask;
/* 1627 */               MapMakerInternalMap.InternalEntry internalEntry3 = (MapMakerInternalMap.InternalEntry)newTable.get(newIndex);
/* 1628 */               E newFirst = copyEntry((E)internalEntry2, (E)internalEntry3);
/* 1629 */               if (newFirst != null) {
/* 1630 */                 newTable.set(newIndex, newFirst);
/*      */               } else {
/* 1632 */                 newCount--;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/* 1638 */       this.table = newTable;
/* 1639 */       this.count = newCount;
/*      */     }
/*      */     
/*      */     boolean replace(K key, int hash, V oldValue, V newValue) {
/* 1643 */       lock();
/*      */       try {
/* 1645 */         preWriteCleanup();
/*      */         
/* 1647 */         AtomicReferenceArray<E> table = this.table;
/* 1648 */         int index = hash & table.length() - 1;
/* 1649 */         MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         
/* 1651 */         for (MapMakerInternalMap.InternalEntry internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1652 */           K entryKey = (K)internalEntry2.getKey();
/* 1653 */           if (internalEntry2.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 1655 */             .equivalent(key, entryKey)) {
/*      */ 
/*      */             
/* 1658 */             V entryValue = (V)internalEntry2.getValue();
/* 1659 */             if (entryValue == null) {
/* 1660 */               if (isCollected(internalEntry2)) {
/* 1661 */                 int newCount = this.count - 1;
/* 1662 */                 this.modCount++;
/* 1663 */                 E newFirst = removeFromChain((E)internalEntry1, (E)internalEntry2);
/* 1664 */                 newCount = this.count - 1;
/* 1665 */                 table.set(index, newFirst);
/* 1666 */                 this.count = newCount;
/*      */               } 
/* 1668 */               return false;
/*      */             } 
/*      */             
/* 1671 */             if (this.map.valueEquivalence().equivalent(oldValue, entryValue)) {
/* 1672 */               this.modCount++;
/* 1673 */               setValue((E)internalEntry2, newValue);
/* 1674 */               return true;
/*      */             } 
/*      */ 
/*      */             
/* 1678 */             return false;
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/* 1683 */         return false;
/*      */       } finally {
/* 1685 */         unlock();
/*      */       } 
/*      */     }
/*      */     
/*      */     @CheckForNull
/*      */     V replace(K key, int hash, V newValue) {
/* 1691 */       lock();
/*      */       try {
/* 1693 */         preWriteCleanup();
/*      */         
/* 1695 */         AtomicReferenceArray<E> table = this.table;
/* 1696 */         int index = hash & table.length() - 1;
/* 1697 */         MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         MapMakerInternalMap.InternalEntry internalEntry2;
/* 1699 */         for (internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1700 */           K entryKey = (K)internalEntry2.getKey();
/* 1701 */           if (internalEntry2.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 1703 */             .equivalent(key, entryKey)) {
/*      */ 
/*      */             
/* 1706 */             V entryValue = (V)internalEntry2.getValue();
/* 1707 */             if (entryValue == null) {
/* 1708 */               if (isCollected(internalEntry2)) {
/* 1709 */                 int newCount = this.count - 1;
/* 1710 */                 this.modCount++;
/* 1711 */                 E newFirst = removeFromChain((E)internalEntry1, (E)internalEntry2);
/* 1712 */                 newCount = this.count - 1;
/* 1713 */                 table.set(index, newFirst);
/* 1714 */                 this.count = newCount;
/*      */               } 
/* 1716 */               return null;
/*      */             } 
/*      */             
/* 1719 */             this.modCount++;
/* 1720 */             setValue((E)internalEntry2, newValue);
/* 1721 */             return entryValue;
/*      */           } 
/*      */         } 
/*      */         
/* 1725 */         internalEntry2 = null; return (V)internalEntry2;
/*      */       } finally {
/* 1727 */         unlock();
/*      */       } 
/*      */     }
/*      */     
/*      */     @CheckForNull
/*      */     @CanIgnoreReturnValue
/*      */     V remove(Object key, int hash) {
/* 1734 */       lock();
/*      */       try {
/* 1736 */         preWriteCleanup();
/*      */         
/* 1738 */         int newCount = this.count - 1;
/* 1739 */         AtomicReferenceArray<E> table = this.table;
/* 1740 */         int index = hash & table.length() - 1;
/* 1741 */         MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         MapMakerInternalMap.InternalEntry internalEntry2;
/* 1743 */         for (internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1744 */           K entryKey = (K)internalEntry2.getKey();
/* 1745 */           if (internalEntry2.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 1747 */             .equivalent(key, entryKey)) {
/* 1748 */             V entryValue = (V)internalEntry2.getValue();
/*      */             
/* 1750 */             if (entryValue == null)
/*      */             {
/* 1752 */               if (!isCollected(internalEntry2))
/*      */               {
/*      */                 
/* 1755 */                 return null;
/*      */               }
/*      */             }
/* 1758 */             this.modCount++;
/* 1759 */             E newFirst = removeFromChain((E)internalEntry1, (E)internalEntry2);
/* 1760 */             newCount = this.count - 1;
/* 1761 */             table.set(index, newFirst);
/* 1762 */             this.count = newCount;
/* 1763 */             return entryValue;
/*      */           } 
/*      */         } 
/*      */         
/* 1767 */         internalEntry2 = null; return (V)internalEntry2;
/*      */       } finally {
/* 1769 */         unlock();
/*      */       } 
/*      */     }
/*      */     
/*      */     boolean remove(Object key, int hash, Object value) {
/* 1774 */       lock();
/*      */       try {
/* 1776 */         preWriteCleanup();
/*      */         
/* 1778 */         int newCount = this.count - 1;
/* 1779 */         AtomicReferenceArray<E> table = this.table;
/* 1780 */         int index = hash & table.length() - 1;
/* 1781 */         MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         
/* 1783 */         for (MapMakerInternalMap.InternalEntry internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1784 */           K entryKey = (K)internalEntry2.getKey();
/* 1785 */           if (internalEntry2.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 1787 */             .equivalent(key, entryKey)) {
/* 1788 */             V entryValue = (V)internalEntry2.getValue();
/*      */             
/* 1790 */             boolean explicitRemoval = false;
/* 1791 */             if (this.map.valueEquivalence().equivalent(value, entryValue)) {
/* 1792 */               explicitRemoval = true;
/* 1793 */             } else if (!isCollected(internalEntry2)) {
/*      */ 
/*      */               
/* 1796 */               return false;
/*      */             } 
/*      */             
/* 1799 */             this.modCount++;
/* 1800 */             E newFirst = removeFromChain((E)internalEntry1, (E)internalEntry2);
/* 1801 */             newCount = this.count - 1;
/* 1802 */             table.set(index, newFirst);
/* 1803 */             this.count = newCount;
/* 1804 */             return explicitRemoval;
/*      */           } 
/*      */         } 
/*      */         
/* 1808 */         return false;
/*      */       } finally {
/* 1810 */         unlock();
/*      */       } 
/*      */     }
/*      */     
/*      */     void clear() {
/* 1815 */       if (this.count != 0) {
/* 1816 */         lock();
/*      */         try {
/* 1818 */           AtomicReferenceArray<E> table = this.table;
/* 1819 */           for (int i = 0; i < table.length(); i++) {
/* 1820 */             table.set(i, null);
/*      */           }
/* 1822 */           maybeClearReferenceQueues();
/* 1823 */           this.readCount.set(0);
/*      */           
/* 1825 */           this.modCount++;
/* 1826 */           this.count = 0;
/*      */         } finally {
/* 1828 */           unlock();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     @GuardedBy("this")
/*      */     E removeFromChain(E first, E entry) {
/* 1848 */       int newCount = this.count;
/* 1849 */       E newFirst = (E)entry.getNext();
/* 1850 */       for (E e = first; e != entry; e = (E)e.getNext()) {
/* 1851 */         E next = copyEntry(e, newFirst);
/* 1852 */         if (next != null) {
/* 1853 */           newFirst = next;
/*      */         } else {
/* 1855 */           newCount--;
/*      */         } 
/*      */       } 
/* 1858 */       this.count = newCount;
/* 1859 */       return newFirst;
/*      */     }
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     boolean reclaimKey(E entry, int hash) {
/* 1865 */       lock();
/*      */       try {
/* 1867 */         int newCount = this.count - 1;
/* 1868 */         AtomicReferenceArray<E> table = this.table;
/* 1869 */         int index = hash & table.length() - 1;
/* 1870 */         MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         
/* 1872 */         for (MapMakerInternalMap.InternalEntry internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1873 */           if (internalEntry2 == entry) {
/* 1874 */             this.modCount++;
/* 1875 */             E newFirst = removeFromChain((E)internalEntry1, (E)internalEntry2);
/* 1876 */             newCount = this.count - 1;
/* 1877 */             table.set(index, newFirst);
/* 1878 */             this.count = newCount;
/* 1879 */             return true;
/*      */           } 
/*      */         } 
/*      */         
/* 1883 */         return false;
/*      */       } finally {
/* 1885 */         unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     boolean reclaimValue(K key, int hash, MapMakerInternalMap.WeakValueReference<K, V, E> valueReference) {
/* 1892 */       lock();
/*      */       try {
/* 1894 */         int newCount = this.count - 1;
/* 1895 */         AtomicReferenceArray<E> table = this.table;
/* 1896 */         int index = hash & table.length() - 1;
/* 1897 */         MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         
/* 1899 */         for (MapMakerInternalMap.InternalEntry internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1900 */           K entryKey = (K)internalEntry2.getKey();
/* 1901 */           if (internalEntry2.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 1903 */             .equivalent(key, entryKey)) {
/* 1904 */             MapMakerInternalMap.WeakValueReference<K, V, E> v = ((MapMakerInternalMap.WeakValueEntry<K, V, E>)internalEntry2).getValueReference();
/* 1905 */             if (v == valueReference) {
/* 1906 */               this.modCount++;
/* 1907 */               E newFirst = removeFromChain((E)internalEntry1, (E)internalEntry2);
/* 1908 */               newCount = this.count - 1;
/* 1909 */               table.set(index, newFirst);
/* 1910 */               this.count = newCount;
/* 1911 */               return true;
/*      */             } 
/* 1913 */             return false;
/*      */           } 
/*      */         } 
/*      */         
/* 1917 */         return false;
/*      */       } finally {
/* 1919 */         unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     boolean clearValueForTesting(K key, int hash, MapMakerInternalMap.WeakValueReference<K, V, ? extends MapMakerInternalMap.InternalEntry<K, V, ?>> valueReference) {
/* 1929 */       lock();
/*      */       try {
/* 1931 */         AtomicReferenceArray<E> table = this.table;
/* 1932 */         int index = hash & table.length() - 1;
/* 1933 */         MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         
/* 1935 */         for (MapMakerInternalMap.InternalEntry internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1936 */           K entryKey = (K)internalEntry2.getKey();
/* 1937 */           if (internalEntry2.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 1939 */             .equivalent(key, entryKey)) {
/* 1940 */             MapMakerInternalMap.WeakValueReference<K, V, E> v = ((MapMakerInternalMap.WeakValueEntry<K, V, E>)internalEntry2).getValueReference();
/* 1941 */             if (v == valueReference) {
/* 1942 */               E newFirst = removeFromChain((E)internalEntry1, (E)internalEntry2);
/* 1943 */               table.set(index, newFirst);
/* 1944 */               return true;
/*      */             } 
/* 1946 */             return false;
/*      */           } 
/*      */         } 
/*      */         
/* 1950 */         return false;
/*      */       } finally {
/* 1952 */         unlock();
/*      */       } 
/*      */     }
/*      */     
/*      */     @GuardedBy("this")
/*      */     boolean removeEntryForTesting(E entry) {
/* 1958 */       int hash = entry.getHash();
/* 1959 */       int newCount = this.count - 1;
/* 1960 */       AtomicReferenceArray<E> table = this.table;
/* 1961 */       int index = hash & table.length() - 1;
/* 1962 */       MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */       
/* 1964 */       for (MapMakerInternalMap.InternalEntry internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1965 */         if (internalEntry2 == entry) {
/* 1966 */           this.modCount++;
/* 1967 */           E newFirst = removeFromChain((E)internalEntry1, (E)internalEntry2);
/* 1968 */           newCount = this.count - 1;
/* 1969 */           table.set(index, newFirst);
/* 1970 */           this.count = newCount;
/* 1971 */           return true;
/*      */         } 
/*      */       } 
/*      */       
/* 1975 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static <K, V, E extends MapMakerInternalMap.InternalEntry<K, V, E>> boolean isCollected(E entry) {
/* 1983 */       return (entry.getValue() == null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     V getLiveValue(E entry) {
/* 1992 */       if (entry.getKey() == null) {
/* 1993 */         tryDrainReferenceQueues();
/* 1994 */         return null;
/*      */       } 
/* 1996 */       V value = (V)entry.getValue();
/* 1997 */       if (value == null) {
/* 1998 */         tryDrainReferenceQueues();
/* 1999 */         return null;
/*      */       } 
/*      */       
/* 2002 */       return value;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void postReadCleanup() {
/* 2011 */       if ((this.readCount.incrementAndGet() & 0x3F) == 0) {
/* 2012 */         runCleanup();
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void preWriteCleanup() {
/* 2022 */       runLockedCleanup();
/*      */     }
/*      */     
/*      */     void runCleanup() {
/* 2026 */       runLockedCleanup();
/*      */     }
/*      */     
/*      */     void runLockedCleanup() {
/* 2030 */       if (tryLock()) {
/*      */         try {
/* 2032 */           maybeDrainReferenceQueues();
/* 2033 */           this.readCount.set(0);
/*      */         } finally {
/* 2035 */           unlock();
/*      */         } 
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final class StrongKeyStrongValueSegment<K, V>
/*      */     extends Segment<K, V, StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>>
/*      */   {
/*      */     StrongKeyStrongValueSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>> map, int initialCapacity) {
/* 2049 */       super(map, initialCapacity);
/*      */     }
/*      */ 
/*      */     
/*      */     StrongKeyStrongValueSegment<K, V> self() {
/* 2054 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> castForTesting(@CheckForNull MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 2062 */       return (MapMakerInternalMap.StrongKeyStrongValueEntry)entry;
/*      */     }
/*      */   }
/*      */   
/*      */   static final class StrongKeyWeakValueSegment<K, V>
/*      */     extends Segment<K, V, StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>>
/*      */   {
/* 2069 */     private final ReferenceQueue<V> queueForValues = new ReferenceQueue<>();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     StrongKeyWeakValueSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>> map, int initialCapacity) {
/* 2075 */       super(map, initialCapacity);
/*      */     }
/*      */ 
/*      */     
/*      */     StrongKeyWeakValueSegment<K, V> self() {
/* 2080 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     ReferenceQueue<V> getValueReferenceQueueForTesting() {
/* 2085 */       return this.queueForValues;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> castForTesting(@CheckForNull MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 2093 */       return (MapMakerInternalMap.StrongKeyWeakValueEntry)entry;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>> getWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e) {
/* 2099 */       return castForTesting(e).getValueReference();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>> newWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e, V value) {
/* 2105 */       return new MapMakerInternalMap.WeakValueReferenceImpl<>(this.queueForValues, value, castForTesting(e));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e, MapMakerInternalMap.WeakValueReference<K, V, ? extends MapMakerInternalMap.InternalEntry<K, V, ?>> valueReference) {
/* 2112 */       MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> entry = castForTesting(e);
/*      */       
/* 2114 */       MapMakerInternalMap.WeakValueReference<K, V, ? extends MapMakerInternalMap.InternalEntry<K, V, ?>> weakValueReference = valueReference;
/*      */       
/* 2116 */       MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>> previous = entry.valueReference;
/* 2117 */       entry.valueReference = weakValueReference;
/* 2118 */       previous.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     void maybeDrainReferenceQueues() {
/* 2123 */       drainValueReferenceQueue(this.queueForValues);
/*      */     }
/*      */ 
/*      */     
/*      */     void maybeClearReferenceQueues() {
/* 2128 */       clearReferenceQueue(this.queueForValues);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final class StrongKeyDummyValueSegment<K>
/*      */     extends Segment<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>, StrongKeyDummyValueSegment<K>>
/*      */   {
/*      */     StrongKeyDummyValueSegment(MapMakerInternalMap<K, MapMaker.Dummy, MapMakerInternalMap.StrongKeyDummyValueEntry<K>, StrongKeyDummyValueSegment<K>> map, int initialCapacity) {
/* 2139 */       super(map, initialCapacity);
/*      */     }
/*      */ 
/*      */     
/*      */     StrongKeyDummyValueSegment<K> self() {
/* 2144 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.StrongKeyDummyValueEntry<K> castForTesting(MapMakerInternalMap.InternalEntry<K, MapMaker.Dummy, ?> entry) {
/* 2150 */       return (MapMakerInternalMap.StrongKeyDummyValueEntry)entry;
/*      */     }
/*      */   }
/*      */   
/*      */   static final class WeakKeyStrongValueSegment<K, V>
/*      */     extends Segment<K, V, WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>>
/*      */   {
/* 2157 */     private final ReferenceQueue<K> queueForKeys = new ReferenceQueue<>();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     WeakKeyStrongValueSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>> map, int initialCapacity) {
/* 2163 */       super(map, initialCapacity);
/*      */     }
/*      */ 
/*      */     
/*      */     WeakKeyStrongValueSegment<K, V> self() {
/* 2168 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     ReferenceQueue<K> getKeyReferenceQueueForTesting() {
/* 2173 */       return this.queueForKeys;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> castForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 2179 */       return (MapMakerInternalMap.WeakKeyStrongValueEntry)entry;
/*      */     }
/*      */ 
/*      */     
/*      */     void maybeDrainReferenceQueues() {
/* 2184 */       drainKeyReferenceQueue(this.queueForKeys);
/*      */     }
/*      */ 
/*      */     
/*      */     void maybeClearReferenceQueues() {
/* 2189 */       clearReferenceQueue(this.queueForKeys);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class WeakKeyWeakValueSegment<K, V>
/*      */     extends Segment<K, V, WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>>
/*      */   {
/* 2196 */     private final ReferenceQueue<K> queueForKeys = new ReferenceQueue<>();
/* 2197 */     private final ReferenceQueue<V> queueForValues = new ReferenceQueue<>();
/*      */ 
/*      */ 
/*      */     
/*      */     WeakKeyWeakValueSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>> map, int initialCapacity) {
/* 2202 */       super(map, initialCapacity);
/*      */     }
/*      */ 
/*      */     
/*      */     WeakKeyWeakValueSegment<K, V> self() {
/* 2207 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     ReferenceQueue<K> getKeyReferenceQueueForTesting() {
/* 2212 */       return this.queueForKeys;
/*      */     }
/*      */ 
/*      */     
/*      */     ReferenceQueue<V> getValueReferenceQueueForTesting() {
/* 2217 */       return this.queueForValues;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> castForTesting(@CheckForNull MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 2224 */       return (MapMakerInternalMap.WeakKeyWeakValueEntry)entry;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>> getWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e) {
/* 2230 */       return castForTesting(e).getValueReference();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>> newWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e, V value) {
/* 2236 */       return new MapMakerInternalMap.WeakValueReferenceImpl<>(this.queueForValues, value, castForTesting(e));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e, MapMakerInternalMap.WeakValueReference<K, V, ? extends MapMakerInternalMap.InternalEntry<K, V, ?>> valueReference) {
/* 2243 */       MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> entry = castForTesting(e);
/*      */       
/* 2245 */       MapMakerInternalMap.WeakValueReference<K, V, ? extends MapMakerInternalMap.InternalEntry<K, V, ?>> weakValueReference = valueReference;
/*      */       
/* 2247 */       MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>> previous = entry.valueReference;
/* 2248 */       entry.valueReference = weakValueReference;
/* 2249 */       previous.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     void maybeDrainReferenceQueues() {
/* 2254 */       drainKeyReferenceQueue(this.queueForKeys);
/* 2255 */       drainValueReferenceQueue(this.queueForValues);
/*      */     }
/*      */ 
/*      */     
/*      */     void maybeClearReferenceQueues() {
/* 2260 */       clearReferenceQueue(this.queueForKeys);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class WeakKeyDummyValueSegment<K>
/*      */     extends Segment<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>, WeakKeyDummyValueSegment<K>>
/*      */   {
/* 2267 */     private final ReferenceQueue<K> queueForKeys = new ReferenceQueue<>();
/*      */ 
/*      */ 
/*      */     
/*      */     WeakKeyDummyValueSegment(MapMakerInternalMap<K, MapMaker.Dummy, MapMakerInternalMap.WeakKeyDummyValueEntry<K>, WeakKeyDummyValueSegment<K>> map, int initialCapacity) {
/* 2272 */       super(map, initialCapacity);
/*      */     }
/*      */ 
/*      */     
/*      */     WeakKeyDummyValueSegment<K> self() {
/* 2277 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     ReferenceQueue<K> getKeyReferenceQueueForTesting() {
/* 2282 */       return this.queueForKeys;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakKeyDummyValueEntry<K> castForTesting(MapMakerInternalMap.InternalEntry<K, MapMaker.Dummy, ?> entry) {
/* 2288 */       return (MapMakerInternalMap.WeakKeyDummyValueEntry)entry;
/*      */     }
/*      */ 
/*      */     
/*      */     void maybeDrainReferenceQueues() {
/* 2293 */       drainKeyReferenceQueue(this.queueForKeys);
/*      */     }
/*      */ 
/*      */     
/*      */     void maybeClearReferenceQueues() {
/* 2298 */       clearReferenceQueue(this.queueForKeys);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class CleanupMapTask implements Runnable {
/*      */     final WeakReference<MapMakerInternalMap<?, ?, ?, ?>> mapReference;
/*      */     
/*      */     public CleanupMapTask(MapMakerInternalMap<?, ?, ?, ?> map) {
/* 2306 */       this.mapReference = new WeakReference<>(map);
/*      */     }
/*      */ 
/*      */     
/*      */     public void run() {
/* 2311 */       MapMakerInternalMap<?, ?, ?, ?> map = this.mapReference.get();
/* 2312 */       if (map == null) {
/* 2313 */         throw new CancellationException();
/*      */       }
/*      */       
/* 2316 */       for (MapMakerInternalMap.Segment<?, ?, ?, ?> segment : map.segments) {
/* 2317 */         segment.runCleanup();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   Strength keyStrength() {
/* 2324 */     return this.entryHelper.keyStrength();
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   Strength valueStrength() {
/* 2329 */     return this.entryHelper.valueStrength();
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   Equivalence<Object> valueEquivalence() {
/* 2334 */     return this.entryHelper.valueStrength().defaultEquivalence();
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
/* 2348 */     long sum = 0L;
/* 2349 */     Segment<K, V, E, S>[] segments = this.segments; int i;
/* 2350 */     for (i = 0; i < segments.length; i++) {
/* 2351 */       if ((segments[i]).count != 0) {
/* 2352 */         return false;
/*      */       }
/* 2354 */       sum += (segments[i]).modCount;
/*      */     } 
/*      */     
/* 2357 */     if (sum != 0L) {
/* 2358 */       for (i = 0; i < segments.length; i++) {
/* 2359 */         if ((segments[i]).count != 0) {
/* 2360 */           return false;
/*      */         }
/* 2362 */         sum -= (segments[i]).modCount;
/*      */       } 
/* 2364 */       return (sum == 0L);
/*      */     } 
/* 2366 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public int size() {
/* 2371 */     Segment<K, V, E, S>[] segments = this.segments;
/* 2372 */     long sum = 0L;
/* 2373 */     for (int i = 0; i < segments.length; i++) {
/* 2374 */       sum += (segments[i]).count;
/*      */     }
/* 2376 */     return Ints.saturatedCast(sum);
/*      */   }
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   public V get(@CheckForNull Object key) {
/* 2382 */     if (key == null) {
/* 2383 */       return null;
/*      */     }
/* 2385 */     int hash = hash(key);
/* 2386 */     return segmentFor(hash).get(key, hash);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   E getEntry(@CheckForNull Object key) {
/* 2395 */     if (key == null) {
/* 2396 */       return null;
/*      */     }
/* 2398 */     int hash = hash(key);
/* 2399 */     return segmentFor(hash).getEntry(key, hash);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsKey(@CheckForNull Object key) {
/* 2404 */     if (key == null) {
/* 2405 */       return false;
/*      */     }
/* 2407 */     int hash = hash(key);
/* 2408 */     return segmentFor(hash).containsKey(key, hash);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsValue(@CheckForNull Object value) {
/* 2413 */     if (value == null) {
/* 2414 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2422 */     Segment<K, V, E, S>[] segments = this.segments;
/* 2423 */     long last = -1L;
/* 2424 */     for (int i = 0; i < 3; i++) {
/* 2425 */       long sum = 0L;
/* 2426 */       for (Segment<K, V, E, S> segment : segments) {
/*      */         
/* 2428 */         int unused = segment.count;
/*      */         
/* 2430 */         AtomicReferenceArray<E> table = segment.table;
/* 2431 */         for (int j = 0; j < table.length(); j++) {
/* 2432 */           for (InternalEntry internalEntry = (InternalEntry)table.get(j); internalEntry != null; internalEntry = (InternalEntry)internalEntry.getNext()) {
/* 2433 */             V v = segment.getLiveValue((E)internalEntry);
/* 2434 */             if (v != null && valueEquivalence().equivalent(value, v)) {
/* 2435 */               return true;
/*      */             }
/*      */           } 
/*      */         } 
/* 2439 */         sum += segment.modCount;
/*      */       } 
/* 2441 */       if (sum == last) {
/*      */         break;
/*      */       }
/* 2444 */       last = sum;
/*      */     } 
/* 2446 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   @CanIgnoreReturnValue
/*      */   public V put(K key, V value) {
/* 2453 */     Preconditions.checkNotNull(key);
/* 2454 */     Preconditions.checkNotNull(value);
/* 2455 */     int hash = hash(key);
/* 2456 */     return segmentFor(hash).put(key, hash, value, false);
/*      */   }
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   @CanIgnoreReturnValue
/*      */   public V putIfAbsent(K key, V value) {
/* 2463 */     Preconditions.checkNotNull(key);
/* 2464 */     Preconditions.checkNotNull(value);
/* 2465 */     int hash = hash(key);
/* 2466 */     return segmentFor(hash).put(key, hash, value, true);
/*      */   }
/*      */ 
/*      */   
/*      */   public void putAll(Map<? extends K, ? extends V> m) {
/* 2471 */     for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
/* 2472 */       put(e.getKey(), e.getValue());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   @CanIgnoreReturnValue
/*      */   public V remove(@CheckForNull Object key) {
/* 2480 */     if (key == null) {
/* 2481 */       return null;
/*      */     }
/* 2483 */     int hash = hash(key);
/* 2484 */     return segmentFor(hash).remove(key, hash);
/*      */   }
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public boolean remove(@CheckForNull Object key, @CheckForNull Object value) {
/* 2490 */     if (key == null || value == null) {
/* 2491 */       return false;
/*      */     }
/* 2493 */     int hash = hash(key);
/* 2494 */     return segmentFor(hash).remove(key, hash, value);
/*      */   }
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public boolean replace(K key, @CheckForNull V oldValue, V newValue) {
/* 2500 */     Preconditions.checkNotNull(key);
/* 2501 */     Preconditions.checkNotNull(newValue);
/* 2502 */     if (oldValue == null) {
/* 2503 */       return false;
/*      */     }
/* 2505 */     int hash = hash(key);
/* 2506 */     return segmentFor(hash).replace(key, hash, oldValue, newValue);
/*      */   }
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   @CanIgnoreReturnValue
/*      */   public V replace(K key, V value) {
/* 2513 */     Preconditions.checkNotNull(key);
/* 2514 */     Preconditions.checkNotNull(value);
/* 2515 */     int hash = hash(key);
/* 2516 */     return segmentFor(hash).replace(key, hash, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public void clear() {
/* 2521 */     for (Segment<K, V, E, S> segment : this.segments) {
/* 2522 */       segment.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<K> keySet() {
/* 2530 */     Set<K> ks = this.keySet;
/* 2531 */     return (ks != null) ? ks : (this.keySet = new KeySet());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<V> values() {
/* 2538 */     Collection<V> vs = this.values;
/* 2539 */     return (vs != null) ? vs : (this.values = new Values());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<Map.Entry<K, V>> entrySet() {
/* 2546 */     Set<Map.Entry<K, V>> es = this.entrySet;
/* 2547 */     return (es != null) ? es : (this.entrySet = new EntrySet());
/*      */   }
/*      */   abstract class HashIterator<T> implements Iterator<T> { int nextSegmentIndex;
/*      */     int nextTableIndex;
/*      */     @CheckForNull
/*      */     MapMakerInternalMap.Segment<K, V, E, S> currentSegment;
/*      */     @CheckForNull
/*      */     AtomicReferenceArray<E> currentTable;
/*      */     @CheckForNull
/*      */     E nextEntry;
/*      */     @CheckForNull
/*      */     MapMakerInternalMap<K, V, E, S>.WriteThroughEntry nextExternal;
/*      */     @CheckForNull
/*      */     MapMakerInternalMap<K, V, E, S>.WriteThroughEntry lastReturned;
/*      */     
/*      */     HashIterator() {
/* 2563 */       this.nextSegmentIndex = MapMakerInternalMap.this.segments.length - 1;
/* 2564 */       this.nextTableIndex = -1;
/* 2565 */       advance();
/*      */     }
/*      */ 
/*      */     
/*      */     public abstract T next();
/*      */     
/*      */     final void advance() {
/* 2572 */       this.nextExternal = null;
/*      */       
/* 2574 */       if (nextInChain()) {
/*      */         return;
/*      */       }
/*      */       
/* 2578 */       if (nextInTable()) {
/*      */         return;
/*      */       }
/*      */       
/* 2582 */       while (this.nextSegmentIndex >= 0) {
/* 2583 */         this.currentSegment = MapMakerInternalMap.this.segments[this.nextSegmentIndex--];
/* 2584 */         if (this.currentSegment.count != 0) {
/* 2585 */           this.currentTable = this.currentSegment.table;
/* 2586 */           this.nextTableIndex = this.currentTable.length() - 1;
/* 2587 */           if (nextInTable()) {
/*      */             return;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     boolean nextInChain() {
/* 2596 */       if (this.nextEntry != null) {
/* 2597 */         for (this.nextEntry = (E)this.nextEntry.getNext(); this.nextEntry != null; this.nextEntry = (E)this.nextEntry.getNext()) {
/* 2598 */           if (advanceTo(this.nextEntry)) {
/* 2599 */             return true;
/*      */           }
/*      */         } 
/*      */       }
/* 2603 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     boolean nextInTable() {
/* 2608 */       while (this.nextTableIndex >= 0) {
/* 2609 */         if ((this.nextEntry = this.currentTable.get(this.nextTableIndex--)) != null && (
/* 2610 */           advanceTo(this.nextEntry) || nextInChain())) {
/* 2611 */           return true;
/*      */         }
/*      */       } 
/*      */       
/* 2615 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean advanceTo(E entry) {
/*      */       try {
/* 2624 */         K key = (K)entry.getKey();
/* 2625 */         V value = (V)MapMakerInternalMap.this.getLiveValue(entry);
/* 2626 */         if (value != null) {
/* 2627 */           this.nextExternal = new MapMakerInternalMap.WriteThroughEntry(key, value);
/* 2628 */           return true;
/*      */         } 
/*      */         
/* 2631 */         return false;
/*      */       } finally {
/*      */         
/* 2634 */         this.currentSegment.postReadCleanup();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/* 2640 */       return (this.nextExternal != null);
/*      */     }
/*      */     
/*      */     MapMakerInternalMap<K, V, E, S>.WriteThroughEntry nextEntry() {
/* 2644 */       if (this.nextExternal == null) {
/* 2645 */         throw new NoSuchElementException();
/*      */       }
/* 2647 */       this.lastReturned = this.nextExternal;
/* 2648 */       advance();
/* 2649 */       return this.lastReturned;
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/* 2654 */       CollectPreconditions.checkRemove((this.lastReturned != null));
/* 2655 */       MapMakerInternalMap.this.remove(this.lastReturned.getKey());
/* 2656 */       this.lastReturned = null;
/*      */     } }
/*      */   
/*      */   final class KeyIterator extends HashIterator<K> { KeyIterator(MapMakerInternalMap this$0) {
/* 2660 */       super(this$0);
/*      */     }
/*      */     
/*      */     public K next() {
/* 2664 */       return nextEntry().getKey();
/*      */     } }
/*      */   
/*      */   final class ValueIterator extends HashIterator<V> { ValueIterator(MapMakerInternalMap this$0) {
/* 2668 */       super(this$0);
/*      */     }
/*      */     
/*      */     public V next() {
/* 2672 */       return nextEntry().getValue();
/*      */     } }
/*      */ 
/*      */ 
/*      */   
/*      */   final class WriteThroughEntry
/*      */     extends AbstractMapEntry<K, V>
/*      */   {
/*      */     final K key;
/*      */     
/*      */     V value;
/*      */     
/*      */     WriteThroughEntry(K key, V value) {
/* 2685 */       this.key = key;
/* 2686 */       this.value = value;
/*      */     }
/*      */ 
/*      */     
/*      */     public K getKey() {
/* 2691 */       return this.key;
/*      */     }
/*      */ 
/*      */     
/*      */     public V getValue() {
/* 2696 */       return this.value;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(@CheckForNull Object object) {
/* 2702 */       if (object instanceof Map.Entry) {
/* 2703 */         Map.Entry<?, ?> that = (Map.Entry<?, ?>)object;
/* 2704 */         return (this.key.equals(that.getKey()) && this.value.equals(that.getValue()));
/*      */       } 
/* 2706 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 2712 */       return this.key.hashCode() ^ this.value.hashCode();
/*      */     }
/*      */ 
/*      */     
/*      */     public V setValue(V newValue) {
/* 2717 */       V oldValue = (V)MapMakerInternalMap.this.put(this.key, newValue);
/* 2718 */       this.value = newValue;
/* 2719 */       return oldValue;
/*      */     } }
/*      */   
/*      */   final class EntryIterator extends HashIterator<Map.Entry<K, V>> { EntryIterator(MapMakerInternalMap this$0) {
/* 2723 */       super(this$0);
/*      */     }
/*      */     
/*      */     public Map.Entry<K, V> next() {
/* 2727 */       return nextEntry();
/*      */     } }
/*      */ 
/*      */ 
/*      */   
/*      */   final class KeySet
/*      */     extends SafeToArraySet<K>
/*      */   {
/*      */     public Iterator<K> iterator() {
/* 2736 */       return new MapMakerInternalMap.KeyIterator(MapMakerInternalMap.this);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 2741 */       return MapMakerInternalMap.this.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 2746 */       return MapMakerInternalMap.this.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 2751 */       return MapMakerInternalMap.this.containsKey(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 2756 */       return (MapMakerInternalMap.this.remove(o) != null);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 2761 */       MapMakerInternalMap.this.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   final class Values
/*      */     extends AbstractCollection<V>
/*      */   {
/*      */     public Iterator<V> iterator() {
/* 2770 */       return new MapMakerInternalMap.ValueIterator(MapMakerInternalMap.this);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 2775 */       return MapMakerInternalMap.this.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 2780 */       return MapMakerInternalMap.this.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 2785 */       return MapMakerInternalMap.this.containsValue(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 2790 */       MapMakerInternalMap.this.clear();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/* 2798 */       return MapMakerInternalMap.toArrayList(this).toArray();
/*      */     }
/*      */ 
/*      */     
/*      */     public <T> T[] toArray(T[] a) {
/* 2803 */       return (T[])MapMakerInternalMap.toArrayList(this).toArray((Object[])a);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   final class EntrySet
/*      */     extends SafeToArraySet<Map.Entry<K, V>>
/*      */   {
/*      */     public Iterator<Map.Entry<K, V>> iterator() {
/* 2812 */       return new MapMakerInternalMap.EntryIterator(MapMakerInternalMap.this);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 2817 */       if (!(o instanceof Map.Entry)) {
/* 2818 */         return false;
/*      */       }
/* 2820 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 2821 */       Object key = e.getKey();
/* 2822 */       if (key == null) {
/* 2823 */         return false;
/*      */       }
/* 2825 */       V v = (V)MapMakerInternalMap.this.get(key);
/*      */       
/* 2827 */       return (v != null && MapMakerInternalMap.this.valueEquivalence().equivalent(e.getValue(), v));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 2832 */       if (!(o instanceof Map.Entry)) {
/* 2833 */         return false;
/*      */       }
/* 2835 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 2836 */       Object key = e.getKey();
/* 2837 */       return (key != null && MapMakerInternalMap.this.remove(key, e.getValue()));
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 2842 */       return MapMakerInternalMap.this.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 2847 */       return MapMakerInternalMap.this.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 2852 */       MapMakerInternalMap.this.clear();
/*      */     }
/*      */   }
/*      */   
/*      */   private static abstract class SafeToArraySet<E>
/*      */     extends AbstractSet<E>
/*      */   {
/*      */     private SafeToArraySet() {}
/*      */     
/*      */     public Object[] toArray() {
/* 2862 */       return MapMakerInternalMap.toArrayList(this).toArray();
/*      */     }
/*      */ 
/*      */     
/*      */     public <T> T[] toArray(T[] a) {
/* 2867 */       return (T[])MapMakerInternalMap.toArrayList(this).toArray((Object[])a);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static <E> ArrayList<E> toArrayList(Collection<E> c) {
/* 2873 */     ArrayList<E> result = new ArrayList<>(c.size());
/* 2874 */     Iterators.addAll(result, c.iterator());
/* 2875 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Object writeReplace() {
/* 2883 */     return new SerializationProxy<>(this.entryHelper
/* 2884 */         .keyStrength(), this.entryHelper
/* 2885 */         .valueStrength(), this.keyEquivalence, this.entryHelper
/*      */         
/* 2887 */         .valueStrength().defaultEquivalence(), this.concurrencyLevel, this);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @J2ktIncompatible
/*      */   private void readObject(ObjectInputStream in) throws InvalidObjectException {
/* 2894 */     throw new InvalidObjectException("Use SerializationProxy");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static abstract class AbstractSerializationProxy<K, V>
/*      */     extends ForwardingConcurrentMap<K, V>
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 3L;
/*      */ 
/*      */     
/*      */     final MapMakerInternalMap.Strength keyStrength;
/*      */     
/*      */     final MapMakerInternalMap.Strength valueStrength;
/*      */     
/*      */     final Equivalence<Object> keyEquivalence;
/*      */     
/*      */     final Equivalence<Object> valueEquivalence;
/*      */     
/*      */     final int concurrencyLevel;
/*      */     
/*      */     transient ConcurrentMap<K, V> delegate;
/*      */ 
/*      */     
/*      */     AbstractSerializationProxy(MapMakerInternalMap.Strength keyStrength, MapMakerInternalMap.Strength valueStrength, Equivalence<Object> keyEquivalence, Equivalence<Object> valueEquivalence, int concurrencyLevel, ConcurrentMap<K, V> delegate) {
/* 2920 */       this.keyStrength = keyStrength;
/* 2921 */       this.valueStrength = valueStrength;
/* 2922 */       this.keyEquivalence = keyEquivalence;
/* 2923 */       this.valueEquivalence = valueEquivalence;
/* 2924 */       this.concurrencyLevel = concurrencyLevel;
/* 2925 */       this.delegate = delegate;
/*      */     }
/*      */ 
/*      */     
/*      */     protected ConcurrentMap<K, V> delegate() {
/* 2930 */       return this.delegate;
/*      */     }
/*      */     
/*      */     void writeMapTo(ObjectOutputStream out) throws IOException {
/* 2934 */       out.writeInt(this.delegate.size());
/* 2935 */       for (Map.Entry<K, V> entry : this.delegate.entrySet()) {
/* 2936 */         out.writeObject(entry.getKey());
/* 2937 */         out.writeObject(entry.getValue());
/*      */       } 
/* 2939 */       out.writeObject(null);
/*      */     }
/*      */ 
/*      */     
/*      */     @J2ktIncompatible
/*      */     MapMaker readMapMaker(ObjectInputStream in) throws IOException {
/* 2945 */       int size = in.readInt();
/* 2946 */       return (new MapMaker())
/* 2947 */         .initialCapacity(size)
/* 2948 */         .setKeyStrength(this.keyStrength)
/* 2949 */         .setValueStrength(this.valueStrength)
/* 2950 */         .keyEquivalence(this.keyEquivalence)
/* 2951 */         .concurrencyLevel(this.concurrencyLevel);
/*      */     }
/*      */ 
/*      */     
/*      */     @J2ktIncompatible
/*      */     void readEntries(ObjectInputStream in) throws IOException, ClassNotFoundException {
/*      */       while (true) {
/* 2958 */         K key = (K)in.readObject();
/* 2959 */         if (key == null) {
/*      */           break;
/*      */         }
/* 2962 */         V value = (V)in.readObject();
/* 2963 */         this.delegate.put(key, value);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class SerializationProxy<K, V>
/*      */     extends AbstractSerializationProxy<K, V>
/*      */   {
/*      */     private static final long serialVersionUID = 3L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     SerializationProxy(MapMakerInternalMap.Strength keyStrength, MapMakerInternalMap.Strength valueStrength, Equivalence<Object> keyEquivalence, Equivalence<Object> valueEquivalence, int concurrencyLevel, ConcurrentMap<K, V> delegate) {
/* 2982 */       super(keyStrength, valueStrength, keyEquivalence, valueEquivalence, concurrencyLevel, delegate);
/*      */     }
/*      */ 
/*      */     
/*      */     private void writeObject(ObjectOutputStream out) throws IOException {
/* 2987 */       out.defaultWriteObject();
/* 2988 */       writeMapTo(out);
/*      */     }
/*      */     
/*      */     @J2ktIncompatible
/*      */     private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 2993 */       in.defaultReadObject();
/* 2994 */       MapMaker mapMaker = readMapMaker(in);
/* 2995 */       this.delegate = mapMaker.makeMap();
/* 2996 */       readEntries(in);
/*      */     }
/*      */     
/*      */     private Object readResolve() {
/* 3000 */       return this.delegate;
/*      */     }
/*      */   }
/*      */   
/*      */   static interface WeakValueReference<K, V, E extends InternalEntry<K, V, E>> {
/*      */     @CheckForNull
/*      */     V get();
/*      */     
/*      */     E getEntry();
/*      */     
/*      */     void clear();
/*      */     
/*      */     WeakValueReference<K, V, E> copyFor(ReferenceQueue<V> param1ReferenceQueue, E param1E);
/*      */   }
/*      */   
/*      */   static interface WeakValueEntry<K, V, E extends InternalEntry<K, V, E>> extends InternalEntry<K, V, E> {
/*      */     MapMakerInternalMap.WeakValueReference<K, V, E> getValueReference();
/*      */   }
/*      */   
/*      */   static interface StrongValueEntry<K, V, E extends InternalEntry<K, V, E>> extends InternalEntry<K, V, E> {}
/*      */   
/*      */   static interface InternalEntry<K, V, E extends InternalEntry<K, V, E>> {
/*      */     E getNext();
/*      */     
/*      */     int getHash();
/*      */     
/*      */     K getKey();
/*      */     
/*      */     V getValue();
/*      */   }
/*      */   
/*      */   static interface InternalEntryHelper<K, V, E extends InternalEntry<K, V, E>, S extends Segment<K, V, E, S>> {
/*      */     MapMakerInternalMap.Strength keyStrength();
/*      */     
/*      */     MapMakerInternalMap.Strength valueStrength();
/*      */     
/*      */     S newSegment(MapMakerInternalMap<K, V, E, S> param1MapMakerInternalMap, int param1Int);
/*      */     
/*      */     E newEntry(S param1S, K param1K, int param1Int, @CheckForNull E param1E);
/*      */     
/*      */     E copy(S param1S, E param1E1, @CheckForNull E param1E2);
/*      */     
/*      */     void setValue(S param1S, E param1E, V param1V);
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/MapMakerInternalMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */