/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.J2ktIncompatible;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import com.google.errorprone.annotations.DoNotCall;
/*      */ import com.google.errorprone.annotations.DoNotMock;
/*      */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*      */ import com.google.j2objc.annotations.RetainedWith;
/*      */ import java.io.InvalidObjectException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.Arrays;
/*      */ import java.util.BitSet;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.EnumMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.Spliterator;
/*      */ import java.util.Spliterators;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.BinaryOperator;
/*      */ import java.util.function.Function;
/*      */ import java.util.stream.Collector;
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
/*      */ @DoNotMock("Use ImmutableMap.of or another implementation")
/*      */ @ElementTypesAreNonnullByDefault
/*      */ @GwtCompatible(serializable = true, emulated = true)
/*      */ public abstract class ImmutableMap<K, V>
/*      */   implements Map<K, V>, Serializable
/*      */ {
/*      */   public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/*   92 */     return CollectCollectors.toImmutableMap(keyFunction, valueFunction);
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
/*      */   public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction) {
/*  113 */     return CollectCollectors.toImmutableMap(keyFunction, valueFunction, mergeFunction);
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
/*      */   public static <K, V> ImmutableMap<K, V> of() {
/*  125 */     return (ImmutableMap)RegularImmutableMap.EMPTY;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1) {
/*  134 */     return ImmutableBiMap.of(k1, v1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2) {
/*  143 */     return RegularImmutableMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2) });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
/*  152 */     return RegularImmutableMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3) });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
/*  161 */     return RegularImmutableMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] {
/*  162 */           entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4)
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
/*  172 */     return RegularImmutableMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] {
/*  173 */           entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5)
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
/*  184 */     return RegularImmutableMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] {
/*  185 */           entryOf(k1, v1), 
/*  186 */           entryOf(k2, v2), 
/*  187 */           entryOf(k3, v3), 
/*  188 */           entryOf(k4, v4), 
/*  189 */           entryOf(k5, v5), 
/*  190 */           entryOf(k6, v6)
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
/*  201 */     return RegularImmutableMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] {
/*  202 */           entryOf(k1, v1), 
/*  203 */           entryOf(k2, v2), 
/*  204 */           entryOf(k3, v3), 
/*  205 */           entryOf(k4, v4), 
/*  206 */           entryOf(k5, v5), 
/*  207 */           entryOf(k6, v6), 
/*  208 */           entryOf(k7, v7)
/*      */         });
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
/*      */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
/*  234 */     return RegularImmutableMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] {
/*  235 */           entryOf(k1, v1), 
/*  236 */           entryOf(k2, v2), 
/*  237 */           entryOf(k3, v3), 
/*  238 */           entryOf(k4, v4), 
/*  239 */           entryOf(k5, v5), 
/*  240 */           entryOf(k6, v6), 
/*  241 */           entryOf(k7, v7), 
/*  242 */           entryOf(k8, v8)
/*      */         });
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
/*      */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
/*  270 */     return RegularImmutableMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] {
/*  271 */           entryOf(k1, v1), 
/*  272 */           entryOf(k2, v2), 
/*  273 */           entryOf(k3, v3), 
/*  274 */           entryOf(k4, v4), 
/*  275 */           entryOf(k5, v5), 
/*  276 */           entryOf(k6, v6), 
/*  277 */           entryOf(k7, v7), 
/*  278 */           entryOf(k8, v8), 
/*  279 */           entryOf(k9, v9)
/*      */         });
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
/*      */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
/*  309 */     return RegularImmutableMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] {
/*  310 */           entryOf(k1, v1), 
/*  311 */           entryOf(k2, v2), 
/*  312 */           entryOf(k3, v3), 
/*  313 */           entryOf(k4, v4), 
/*  314 */           entryOf(k5, v5), 
/*  315 */           entryOf(k6, v6), 
/*  316 */           entryOf(k7, v7), 
/*  317 */           entryOf(k8, v8), 
/*  318 */           entryOf(k9, v9), 
/*  319 */           entryOf(k10, v10)
/*      */         });
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
/*      */   @SafeVarargs
/*      */   public static <K, V> ImmutableMap<K, V> ofEntries(Map.Entry<? extends K, ? extends V>... entries) {
/*  333 */     Map.Entry<? extends K, ? extends V>[] arrayOfEntry = entries;
/*  334 */     return RegularImmutableMap.fromEntries((Map.Entry[])arrayOfEntry);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> Map.Entry<K, V> entryOf(K key, V value) {
/*  345 */     return new ImmutableMapEntry<>(key, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Builder<K, V> builder() {
/*  353 */     return new Builder<>();
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
/*      */   public static <K, V> Builder<K, V> builderWithExpectedSize(int expectedSize) {
/*  369 */     CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
/*  370 */     return new Builder<>(expectedSize);
/*      */   }
/*      */ 
/*      */   
/*      */   static void checkNoConflict(boolean safe, String conflictDescription, Object entry1, Object entry2) {
/*  375 */     if (!safe) {
/*  376 */       throw conflictException(conflictDescription, entry1, entry2);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static IllegalArgumentException conflictException(String conflictDescription, Object entry1, Object entry2) {
/*  382 */     return new IllegalArgumentException("Multiple entries with same " + conflictDescription + ": " + entry1 + " and " + entry2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @DoNotMock
/*      */   public static class Builder<K, V>
/*      */   {
/*      */     @CheckForNull
/*      */     Comparator<? super V> valueComparator;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Map.Entry<K, V>[] entries;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int size;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean entriesUsed;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder() {
/*  427 */       this(4);
/*      */     }
/*      */ 
/*      */     
/*      */     Builder(int initialCapacity) {
/*  432 */       this.entries = (Map.Entry<K, V>[])new Map.Entry[initialCapacity];
/*  433 */       this.size = 0;
/*  434 */       this.entriesUsed = false;
/*      */     }
/*      */     
/*      */     private void ensureCapacity(int minCapacity) {
/*  438 */       if (minCapacity > this.entries.length) {
/*  439 */         this
/*  440 */           .entries = Arrays.<Map.Entry<K, V>>copyOf(this.entries, 
/*  441 */             ImmutableCollection.Builder.expandedCapacity(this.entries.length, minCapacity));
/*  442 */         this.entriesUsed = false;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     public Builder<K, V> put(K key, V value) {
/*  453 */       ensureCapacity(this.size + 1);
/*  454 */       Map.Entry<K, V> entry = ImmutableMap.entryOf(key, value);
/*      */       
/*  456 */       this.entries[this.size++] = entry;
/*  457 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry) {
/*  469 */       return put(entry.getKey(), entry.getValue());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
/*  481 */       return putAll(map.entrySet());
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
/*      */     @CanIgnoreReturnValue
/*      */     public Builder<K, V> putAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/*  494 */       if (entries instanceof Collection) {
/*  495 */         ensureCapacity(this.size + ((Collection)entries).size());
/*      */       }
/*  497 */       for (Map.Entry<? extends K, ? extends V> entry : entries) {
/*  498 */         put(entry);
/*      */       }
/*  500 */       return this;
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
/*      */     @CanIgnoreReturnValue
/*      */     public Builder<K, V> orderEntriesByValue(Comparator<? super V> valueComparator) {
/*  515 */       Preconditions.checkState((this.valueComparator == null), "valueComparator was already set");
/*  516 */       this.valueComparator = (Comparator<? super V>)Preconditions.checkNotNull(valueComparator, "valueComparator");
/*  517 */       return this;
/*      */     }
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     Builder<K, V> combine(Builder<K, V> other) {
/*  522 */       Preconditions.checkNotNull(other);
/*  523 */       ensureCapacity(this.size + other.size);
/*  524 */       System.arraycopy(other.entries, 0, this.entries, this.size, other.size);
/*  525 */       this.size += other.size;
/*  526 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private ImmutableMap<K, V> build(boolean throwIfDuplicateKeys) {
/*      */       Map.Entry<K, V> onlyEntry;
/*      */       Map.Entry[] arrayOfEntry;
/*  537 */       switch (this.size) {
/*      */         case 0:
/*  539 */           return ImmutableMap.of();
/*      */         
/*      */         case 1:
/*  542 */           onlyEntry = Objects.<Map.Entry<K, V>>requireNonNull(this.entries[0]);
/*  543 */           return ImmutableMap.of(onlyEntry.getKey(), onlyEntry.getValue());
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  553 */       int localSize = this.size;
/*  554 */       if (this.valueComparator == null) {
/*  555 */         Map.Entry<K, V>[] localEntries = this.entries;
/*      */       } else {
/*  557 */         Map.Entry[] arrayOfEntry1; if (this.entriesUsed) {
/*  558 */           this.entries = Arrays.<Map.Entry<K, V>>copyOf(this.entries, this.size);
/*      */         }
/*      */         
/*  561 */         Map.Entry<K, V>[] nonNullEntries = this.entries;
/*  562 */         if (!throwIfDuplicateKeys) {
/*      */ 
/*      */           
/*  565 */           arrayOfEntry1 = lastEntryForEachKey(nonNullEntries, this.size);
/*  566 */           localSize = arrayOfEntry1.length;
/*      */         } 
/*  568 */         Arrays.sort(arrayOfEntry1, 0, localSize, 
/*      */ 
/*      */ 
/*      */             
/*  572 */             Ordering.<V>from(this.valueComparator).onResultOf(Maps.valueFunction()));
/*  573 */         arrayOfEntry = arrayOfEntry1;
/*      */       } 
/*  575 */       this.entriesUsed = true;
/*  576 */       return RegularImmutableMap.fromEntryArray(localSize, (Map.Entry<K, V>[])arrayOfEntry, throwIfDuplicateKeys);
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
/*      */     public ImmutableMap<K, V> build() {
/*  591 */       return buildOrThrow();
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
/*      */     public ImmutableMap<K, V> buildOrThrow() {
/*  604 */       return build(true);
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
/*      */     public ImmutableMap<K, V> buildKeepingLast() {
/*  623 */       return build(false);
/*      */     }
/*      */     @VisibleForTesting
/*      */     ImmutableMap<K, V> buildJdkBacked() {
/*      */       Map.Entry<K, V> onlyEntry;
/*  628 */       Preconditions.checkState((this.valueComparator == null), "buildJdkBacked is only for testing; can't use valueComparator");
/*      */       
/*  630 */       switch (this.size) {
/*      */         case 0:
/*  632 */           return ImmutableMap.of();
/*      */         
/*      */         case 1:
/*  635 */           onlyEntry = Objects.<Map.Entry<K, V>>requireNonNull(this.entries[0]);
/*  636 */           return ImmutableMap.of(onlyEntry.getKey(), onlyEntry.getValue());
/*      */       } 
/*  638 */       this.entriesUsed = true;
/*  639 */       return JdkBackedImmutableMap.create(this.size, this.entries, true);
/*      */     }
/*      */ 
/*      */     
/*      */     private static <K, V> Map.Entry<K, V>[] lastEntryForEachKey(Map.Entry<K, V>[] entries, int size) {
/*  644 */       Set<K> seen = new HashSet<>();
/*  645 */       BitSet dups = new BitSet();
/*  646 */       for (int i = size - 1; i >= 0; i--) {
/*  647 */         if (!seen.add(entries[i].getKey())) {
/*  648 */           dups.set(i);
/*      */         }
/*      */       } 
/*  651 */       if (dups.isEmpty()) {
/*  652 */         return entries;
/*      */       }
/*      */       
/*  655 */       Map.Entry[] arrayOfEntry = new Map.Entry[size - dups.cardinality()];
/*  656 */       for (int inI = 0, outI = 0; inI < size; inI++) {
/*  657 */         if (!dups.get(inI)) {
/*  658 */           arrayOfEntry[outI++] = entries[inI];
/*      */         }
/*      */       } 
/*  661 */       return (Map.Entry<K, V>[])arrayOfEntry;
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
/*      */   public static <K, V> ImmutableMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
/*  678 */     if (map instanceof ImmutableMap && !(map instanceof java.util.SortedMap)) {
/*      */       
/*  680 */       ImmutableMap<K, V> kvMap = (ImmutableMap)map;
/*  681 */       if (!kvMap.isPartialView()) {
/*  682 */         return kvMap;
/*      */       }
/*  684 */     } else if (map instanceof EnumMap) {
/*      */ 
/*      */ 
/*      */       
/*  688 */       ImmutableMap<K, V> kvMap = (ImmutableMap)copyOfEnumMap((EnumMap<?, ? extends V>)map);
/*      */       
/*  690 */       return kvMap;
/*      */     } 
/*  692 */     return copyOf(map.entrySet());
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
/*      */   public static <K, V> ImmutableMap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/*      */     Map.Entry<K, V> onlyEntry;
/*  706 */     Map.Entry[] arrayOfEntry = Iterables.<Map.Entry>toArray((Iterable)entries, (Map.Entry[])EMPTY_ENTRY_ARRAY);
/*  707 */     switch (arrayOfEntry.length) {
/*      */       case 0:
/*  709 */         return of();
/*      */       
/*      */       case 1:
/*  712 */         onlyEntry = Objects.<Map.Entry<K, V>>requireNonNull(arrayOfEntry[0]);
/*  713 */         return of(onlyEntry.getKey(), onlyEntry.getValue());
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  719 */     return RegularImmutableMap.fromEntries((Map.Entry<K, V>[])arrayOfEntry);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static <K extends Enum<K>, V> ImmutableMap<K, ? extends V> copyOfEnumMap(EnumMap<?, ? extends V> original) {
/*  725 */     EnumMap<K, V> copy = new EnumMap<>((EnumMap)original);
/*  726 */     for (Map.Entry<K, V> entry : copy.entrySet()) {
/*  727 */       CollectPreconditions.checkEntryNotNull(entry.getKey(), entry.getValue());
/*      */     }
/*  729 */     return ImmutableEnumMap.asImmutable(copy); } @LazyInit @CheckForNull @RetainedWith private transient ImmutableSet<Map.Entry<K, V>> entrySet; @LazyInit @CheckForNull @RetainedWith private transient ImmutableSet<K> keySet; @LazyInit
/*      */   @CheckForNull
/*      */   @RetainedWith
/*  732 */   private transient ImmutableCollection<V> values; static final Map.Entry<?, ?>[] EMPTY_ENTRY_ARRAY = (Map.Entry<?, ?>[])new Map.Entry[0]; @LazyInit
/*      */   @CheckForNull
/*      */   private transient ImmutableSetMultimap<K, V> multimapView;
/*      */   private static final long serialVersionUID = -889275714L;
/*      */   
/*      */   static abstract class IteratorBasedImmutableMap<K, V> extends ImmutableMap<K, V> { Spliterator<Map.Entry<K, V>> entrySpliterator() {
/*  738 */       return Spliterators.spliterator(
/*  739 */           entryIterator(), 
/*  740 */           size(), 1297);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     ImmutableSet<K> createKeySet() {
/*  746 */       return new ImmutableMapKeySet<>(this);
/*      */     }
/*      */     
/*      */     ImmutableSet<Map.Entry<K, V>> createEntrySet() {
/*      */       class EntrySetImpl
/*      */         extends ImmutableMapEntrySet<K, V>
/*      */       {
/*      */         ImmutableMap<K, V> map() {
/*  754 */           return ImmutableMap.IteratorBasedImmutableMap.this;
/*      */         }
/*      */ 
/*      */         
/*      */         public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
/*  759 */           return ImmutableMap.IteratorBasedImmutableMap.this.entryIterator();
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         @J2ktIncompatible
/*      */         @GwtIncompatible
/*      */         Object writeReplace() {
/*  768 */           return super.writeReplace();
/*      */         }
/*      */       };
/*  771 */       return new EntrySetImpl();
/*      */     }
/*      */ 
/*      */     
/*      */     ImmutableCollection<V> createValues() {
/*  776 */       return new ImmutableMapValues<>(this);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @J2ktIncompatible
/*      */     @GwtIncompatible
/*      */     Object writeReplace() {
/*  785 */       return super.writeReplace();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract UnmodifiableIterator<Map.Entry<K, V>> entryIterator(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   @CheckForNull
/*      */   @CanIgnoreReturnValue
/*      */   @DoNotCall("Always throws UnsupportedOperationException")
/*      */   public final V put(K k, V v) {
/*  803 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   @CheckForNull
/*      */   @CanIgnoreReturnValue
/*      */   @DoNotCall("Always throws UnsupportedOperationException")
/*      */   public final V putIfAbsent(K key, V value) {
/*  818 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   @DoNotCall("Always throws UnsupportedOperationException")
/*      */   public final boolean replace(K key, V oldValue, V newValue) {
/*  831 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   @CheckForNull
/*      */   @DoNotCall("Always throws UnsupportedOperationException")
/*      */   public final V replace(K key, V value) {
/*  845 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   @DoNotCall("Always throws UnsupportedOperationException")
/*      */   public final V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
/*  858 */     throw new UnsupportedOperationException();
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
/*      */   @Deprecated
/*      */   @CheckForNull
/*      */   @DoNotCall("Always throws UnsupportedOperationException")
/*      */   public final V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
/*  873 */     throw new UnsupportedOperationException();
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
/*      */   @Deprecated
/*      */   @CheckForNull
/*      */   @DoNotCall("Always throws UnsupportedOperationException")
/*      */   public final V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
/*  888 */     throw new UnsupportedOperationException();
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
/*      */   @Deprecated
/*      */   @CheckForNull
/*      */   @DoNotCall("Always throws UnsupportedOperationException")
/*      */   public final V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> function) {
/*  903 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   @DoNotCall("Always throws UnsupportedOperationException")
/*      */   public final void putAll(Map<? extends K, ? extends V> map) {
/*  916 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   @DoNotCall("Always throws UnsupportedOperationException")
/*      */   public final void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
/*  929 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   @CheckForNull
/*      */   @DoNotCall("Always throws UnsupportedOperationException")
/*      */   public final V remove(@CheckForNull Object o) {
/*  943 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   @DoNotCall("Always throws UnsupportedOperationException")
/*      */   public final boolean remove(@CheckForNull Object key, @CheckForNull Object value) {
/*  956 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   @DoNotCall("Always throws UnsupportedOperationException")
/*      */   public final void clear() {
/*  969 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isEmpty() {
/*  974 */     return (size() == 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsKey(@CheckForNull Object key) {
/*  979 */     return (get(key) != null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsValue(@CheckForNull Object value) {
/*  984 */     return values().contains(value);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   public final V getOrDefault(@CheckForNull Object key, @CheckForNull V defaultValue) {
/* 1025 */     V result = get(key);
/*      */     
/* 1027 */     if (result != null) {
/* 1028 */       return result;
/*      */     }
/* 1030 */     return defaultValue;
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
/*      */   public ImmutableSet<Map.Entry<K, V>> entrySet() {
/* 1042 */     ImmutableSet<Map.Entry<K, V>> result = this.entrySet;
/* 1043 */     return (result == null) ? (this.entrySet = createEntrySet()) : result;
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
/*      */   public ImmutableSet<K> keySet() {
/* 1056 */     ImmutableSet<K> result = this.keySet;
/* 1057 */     return (result == null) ? (this.keySet = createKeySet()) : result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   UnmodifiableIterator<K> keyIterator() {
/* 1068 */     final UnmodifiableIterator<Map.Entry<K, V>> entryIterator = entrySet().iterator();
/* 1069 */     return new UnmodifiableIterator<K>(this)
/*      */       {
/*      */         public boolean hasNext() {
/* 1072 */           return entryIterator.hasNext();
/*      */         }
/*      */ 
/*      */         
/*      */         public K next() {
/* 1077 */           return (K)((Map.Entry)entryIterator.next()).getKey();
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   Spliterator<K> keySpliterator() {
/* 1083 */     return CollectSpliterators.map(entrySet().spliterator(), Map.Entry::getKey);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ImmutableCollection<V> values() {
/* 1094 */     ImmutableCollection<V> result = this.values;
/* 1095 */     return (result == null) ? (this.values = createValues()) : result;
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
/*      */   public ImmutableSetMultimap<K, V> asMultimap() {
/* 1114 */     if (isEmpty()) {
/* 1115 */       return ImmutableSetMultimap.of();
/*      */     }
/* 1117 */     ImmutableSetMultimap<K, V> result = this.multimapView;
/* 1118 */     return (result == null) ? (
/* 1119 */       this
/* 1120 */       .multimapView = new ImmutableSetMultimap<>(new MapViewOfValuesAsSingletonSets(), size(), null)) : 
/* 1121 */       result;
/*      */   }
/*      */   
/*      */   private final class MapViewOfValuesAsSingletonSets
/*      */     extends IteratorBasedImmutableMap<K, ImmutableSet<V>>
/*      */   {
/*      */     private MapViewOfValuesAsSingletonSets() {}
/*      */     
/*      */     public int size() {
/* 1130 */       return ImmutableMap.this.size();
/*      */     }
/*      */ 
/*      */     
/*      */     ImmutableSet<K> createKeySet() {
/* 1135 */       return ImmutableMap.this.keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(@CheckForNull Object key) {
/* 1140 */       return ImmutableMap.this.containsKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public ImmutableSet<V> get(@CheckForNull Object key) {
/* 1146 */       V outerValue = (V)ImmutableMap.this.get(key);
/* 1147 */       return (outerValue == null) ? null : ImmutableSet.<V>of(outerValue);
/*      */     }
/*      */ 
/*      */     
/*      */     boolean isPartialView() {
/* 1152 */       return ImmutableMap.this.isPartialView();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1158 */       return ImmutableMap.this.hashCode();
/*      */     }
/*      */ 
/*      */     
/*      */     boolean isHashCodeFast() {
/* 1163 */       return ImmutableMap.this.isHashCodeFast();
/*      */     }
/*      */ 
/*      */     
/*      */     UnmodifiableIterator<Map.Entry<K, ImmutableSet<V>>> entryIterator() {
/* 1168 */       final Iterator<Map.Entry<K, V>> backingIterator = ImmutableMap.this.entrySet().iterator();
/* 1169 */       return new UnmodifiableIterator<Map.Entry<K, ImmutableSet<V>>>(this)
/*      */         {
/*      */           public boolean hasNext() {
/* 1172 */             return backingIterator.hasNext();
/*      */           }
/*      */ 
/*      */           
/*      */           public Map.Entry<K, ImmutableSet<V>> next() {
/* 1177 */             final Map.Entry<K, V> backingEntry = backingIterator.next();
/* 1178 */             return (Map.Entry)new AbstractMapEntry<K, ImmutableSet<ImmutableSet<V>>>(this)
/*      */               {
/*      */                 public K getKey() {
/* 1181 */                   return (K)backingEntry.getKey();
/*      */                 }
/*      */ 
/*      */                 
/*      */                 public ImmutableSet<V> getValue() {
/* 1186 */                   return ImmutableSet.of((V)backingEntry.getValue());
/*      */                 }
/*      */               };
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @J2ktIncompatible
/*      */     @GwtIncompatible
/*      */     Object writeReplace() {
/* 1199 */       return super.writeReplace();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean equals(@CheckForNull Object object) {
/* 1205 */     return Maps.equalsImpl(this, object);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int hashCode() {
/* 1212 */     return Sets.hashCodeImpl(entrySet());
/*      */   }
/*      */   
/*      */   boolean isHashCodeFast() {
/* 1216 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1221 */     return Maps.toStringImpl(this);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @J2ktIncompatible
/*      */   static class SerializedForm<K, V>
/*      */     implements Serializable
/*      */   {
/*      */     private static final boolean USE_LEGACY_SERIALIZATION = true;
/*      */ 
/*      */     
/*      */     private final Object keys;
/*      */ 
/*      */     
/*      */     private final Object values;
/*      */ 
/*      */     
/*      */     private static final long serialVersionUID = 0L;
/*      */ 
/*      */     
/*      */     SerializedForm(ImmutableMap<K, V> map) {
/* 1243 */       Object[] keys = new Object[map.size()];
/* 1244 */       Object[] values = new Object[map.size()];
/* 1245 */       int i = 0;
/*      */       
/* 1247 */       for (UnmodifiableIterator<Map.Entry<? extends Object, ? extends Object>> unmodifiableIterator = map.entrySet().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<? extends Object, ? extends Object> entry = unmodifiableIterator.next();
/* 1248 */         keys[i] = entry.getKey();
/* 1249 */         values[i] = entry.getValue();
/* 1250 */         i++; }
/*      */       
/* 1252 */       this.keys = keys;
/* 1253 */       this.values = values;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final Object readResolve() {
/* 1262 */       if (!(this.keys instanceof ImmutableSet)) {
/* 1263 */         return legacyReadResolve();
/*      */       }
/*      */       
/* 1266 */       ImmutableSet<K> keySet = (ImmutableSet<K>)this.keys;
/* 1267 */       ImmutableCollection<V> values = (ImmutableCollection<V>)this.values;
/*      */       
/* 1269 */       ImmutableMap.Builder<K, V> builder = makeBuilder(keySet.size());
/*      */       
/* 1271 */       UnmodifiableIterator<K> keyIter = keySet.iterator();
/* 1272 */       UnmodifiableIterator<V> valueIter = values.iterator();
/*      */       
/* 1274 */       while (keyIter.hasNext()) {
/* 1275 */         builder.put(keyIter.next(), valueIter.next());
/*      */       }
/*      */       
/* 1278 */       return builder.buildOrThrow();
/*      */     }
/*      */ 
/*      */     
/*      */     final Object legacyReadResolve() {
/* 1283 */       K[] keys = (K[])this.keys;
/* 1284 */       V[] values = (V[])this.values;
/*      */       
/* 1286 */       ImmutableMap.Builder<K, V> builder = makeBuilder(keys.length);
/*      */       
/* 1288 */       for (int i = 0; i < keys.length; i++) {
/* 1289 */         builder.put(keys[i], values[i]);
/*      */       }
/* 1291 */       return builder.buildOrThrow();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     ImmutableMap.Builder<K, V> makeBuilder(int size) {
/* 1298 */       return new ImmutableMap.Builder<>(size);
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
/*      */   @J2ktIncompatible
/*      */   Object writeReplace() {
/* 1311 */     return new SerializedForm<>(this);
/*      */   }
/*      */   
/*      */   @J2ktIncompatible
/*      */   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 1316 */     throw new InvalidObjectException("Use SerializedForm");
/*      */   }
/*      */   
/*      */   @CheckForNull
/*      */   public abstract V get(@CheckForNull Object paramObject);
/*      */   
/*      */   abstract ImmutableSet<Map.Entry<K, V>> createEntrySet();
/*      */   
/*      */   abstract ImmutableSet<K> createKeySet();
/*      */   
/*      */   abstract ImmutableCollection<V> createValues();
/*      */   
/*      */   abstract boolean isPartialView();
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ImmutableMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */