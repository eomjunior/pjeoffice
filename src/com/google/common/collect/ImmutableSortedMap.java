/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.J2ktIncompatible;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import com.google.errorprone.annotations.DoNotCall;
/*      */ import java.io.InvalidObjectException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.util.AbstractMap;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.NavigableMap;
/*      */ import java.util.NavigableSet;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.SortedMap;
/*      */ import java.util.Spliterator;
/*      */ import java.util.function.BiConsumer;
/*      */ import java.util.function.BinaryOperator;
/*      */ import java.util.function.Consumer;
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
/*      */ @ElementTypesAreNonnullByDefault
/*      */ @GwtCompatible(serializable = true, emulated = true)
/*      */ public final class ImmutableSortedMap<K, V>
/*      */   extends ImmutableMap<K, V>
/*      */   implements NavigableMap<K, V>
/*      */ {
/*      */   public static <T, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(Comparator<? super K> comparator, Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/*   87 */     return CollectCollectors.toImmutableSortedMap(comparator, keyFunction, valueFunction);
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
/*      */   public static <T, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(Comparator<? super K> comparator, Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction) {
/*  107 */     return CollectCollectors.toImmutableSortedMap(comparator, keyFunction, valueFunction, mergeFunction);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  115 */   private static final Comparator<Comparable> NATURAL_ORDER = Ordering.natural();
/*      */   
/*  117 */   private static final ImmutableSortedMap<Comparable, Object> NATURAL_EMPTY_MAP = new ImmutableSortedMap(
/*      */       
/*  119 */       ImmutableSortedSet.emptySet(Ordering.natural()), ImmutableList.of());
/*      */   
/*      */   static <K, V> ImmutableSortedMap<K, V> emptyMap(Comparator<? super K> comparator) {
/*  122 */     if (Ordering.<Comparable>natural().equals(comparator)) {
/*  123 */       return of();
/*      */     }
/*  125 */     return new ImmutableSortedMap<>(
/*  126 */         ImmutableSortedSet.emptySet(comparator), ImmutableList.of());
/*      */   }
/*      */ 
/*      */   
/*      */   private final transient RegularImmutableSortedSet<K> keySet;
/*      */   
/*      */   private final transient ImmutableList<V> valueList;
/*      */   
/*      */   @CheckForNull
/*      */   private transient ImmutableSortedMap<K, V> descendingMap;
/*      */   private static final long serialVersionUID = 0L;
/*      */   
/*      */   public static <K, V> ImmutableSortedMap<K, V> of() {
/*  139 */     return (ImmutableSortedMap)NATURAL_EMPTY_MAP;
/*      */   }
/*      */ 
/*      */   
/*      */   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1) {
/*  144 */     return of(Ordering.natural(), k1, v1);
/*      */   }
/*      */ 
/*      */   
/*      */   private static <K, V> ImmutableSortedMap<K, V> of(Comparator<? super K> comparator, K k1, V v1) {
/*  149 */     return new ImmutableSortedMap<>(new RegularImmutableSortedSet<>(
/*  150 */           ImmutableList.of(k1), (Comparator<? super K>)Preconditions.checkNotNull(comparator)), 
/*  151 */         ImmutableList.of(v1));
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
/*      */   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2) {
/*  163 */     return fromEntries((Map.Entry<K, V>[])new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2) });
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
/*      */   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
/*  175 */     return fromEntries((Map.Entry<K, V>[])new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3) });
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
/*      */   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
/*  187 */     return fromEntries((Map.Entry<K, V>[])new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4) });
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
/*      */   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
/*  199 */     return fromEntries((Map.Entry<K, V>[])new Map.Entry[] {
/*  200 */           entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5)
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
/*      */   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
/*  213 */     return fromEntries((Map.Entry<K, V>[])new Map.Entry[] {
/*  214 */           entryOf(k1, v1), 
/*  215 */           entryOf(k2, v2), 
/*  216 */           entryOf(k3, v3), 
/*  217 */           entryOf(k4, v4), 
/*  218 */           entryOf(k5, v5), 
/*  219 */           entryOf(k6, v6)
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
/*      */   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
/*  232 */     return fromEntries((Map.Entry<K, V>[])new Map.Entry[] {
/*  233 */           entryOf(k1, v1), 
/*  234 */           entryOf(k2, v2), 
/*  235 */           entryOf(k3, v3), 
/*  236 */           entryOf(k4, v4), 
/*  237 */           entryOf(k5, v5), 
/*  238 */           entryOf(k6, v6), 
/*  239 */           entryOf(k7, v7)
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
/*      */   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
/*  267 */     return fromEntries((Map.Entry<K, V>[])new Map.Entry[] {
/*  268 */           entryOf(k1, v1), 
/*  269 */           entryOf(k2, v2), 
/*  270 */           entryOf(k3, v3), 
/*  271 */           entryOf(k4, v4), 
/*  272 */           entryOf(k5, v5), 
/*  273 */           entryOf(k6, v6), 
/*  274 */           entryOf(k7, v7), 
/*  275 */           entryOf(k8, v8)
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
/*      */   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
/*  305 */     return fromEntries((Map.Entry<K, V>[])new Map.Entry[] {
/*  306 */           entryOf(k1, v1), 
/*  307 */           entryOf(k2, v2), 
/*  308 */           entryOf(k3, v3), 
/*  309 */           entryOf(k4, v4), 
/*  310 */           entryOf(k5, v5), 
/*  311 */           entryOf(k6, v6), 
/*  312 */           entryOf(k7, v7), 
/*  313 */           entryOf(k8, v8), 
/*  314 */           entryOf(k9, v9)
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
/*      */ 
/*      */   
/*      */   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
/*  346 */     return fromEntries((Map.Entry<K, V>[])new Map.Entry[] {
/*  347 */           entryOf(k1, v1), 
/*  348 */           entryOf(k2, v2), 
/*  349 */           entryOf(k3, v3), 
/*  350 */           entryOf(k4, v4), 
/*  351 */           entryOf(k5, v5), 
/*  352 */           entryOf(k6, v6), 
/*  353 */           entryOf(k7, v7), 
/*  354 */           entryOf(k8, v8), 
/*  355 */           entryOf(k9, v9), 
/*  356 */           entryOf(k10, v10)
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
/*      */   public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
/*  378 */     Ordering<K> naturalOrder = (Ordering)NATURAL_ORDER;
/*  379 */     return copyOfInternal(map, naturalOrder);
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
/*      */   public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> map, Comparator<? super K> comparator) {
/*  395 */     return copyOfInternal(map, (Comparator<? super K>)Preconditions.checkNotNull(comparator));
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
/*      */   public static <K, V> ImmutableSortedMap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/*  414 */     Ordering<K> naturalOrder = (Ordering)NATURAL_ORDER;
/*  415 */     return copyOf(entries, naturalOrder);
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
/*      */   public static <K, V> ImmutableSortedMap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries, Comparator<? super K> comparator) {
/*  429 */     return fromEntries((Comparator<? super K>)Preconditions.checkNotNull(comparator), false, entries);
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
/*      */   public static <K, V> ImmutableSortedMap<K, V> copyOfSorted(SortedMap<K, ? extends V> map) {
/*      */     Comparator<Comparable> comparator1;
/*  444 */     Comparator<? super K> comparator = map.comparator();
/*  445 */     if (comparator == null)
/*      */     {
/*      */       
/*  448 */       comparator1 = NATURAL_ORDER;
/*      */     }
/*  450 */     if (map instanceof ImmutableSortedMap) {
/*      */ 
/*      */ 
/*      */       
/*  454 */       ImmutableSortedMap<K, V> kvMap = (ImmutableSortedMap)map;
/*  455 */       if (!kvMap.isPartialView()) {
/*  456 */         return kvMap;
/*      */       }
/*      */     } 
/*  459 */     return fromEntries((Comparator)comparator1, true, map.entrySet());
/*      */   }
/*      */ 
/*      */   
/*      */   private static <K, V> ImmutableSortedMap<K, V> copyOfInternal(Map<? extends K, ? extends V> map, Comparator<? super K> comparator) {
/*  464 */     boolean sameComparator = false;
/*  465 */     if (map instanceof SortedMap) {
/*  466 */       SortedMap<?, ?> sortedMap = (SortedMap<?, ?>)map;
/*  467 */       Comparator<?> comparator2 = sortedMap.comparator();
/*      */       
/*  469 */       sameComparator = (comparator2 == null) ? ((comparator == NATURAL_ORDER)) : comparator.equals(comparator2);
/*      */     } 
/*      */     
/*  472 */     if (sameComparator && map instanceof ImmutableSortedMap) {
/*      */ 
/*      */ 
/*      */       
/*  476 */       ImmutableSortedMap<K, V> kvMap = (ImmutableSortedMap)map;
/*  477 */       if (!kvMap.isPartialView()) {
/*  478 */         return kvMap;
/*      */       }
/*      */     } 
/*  481 */     return fromEntries(comparator, sameComparator, map.entrySet());
/*      */   }
/*      */ 
/*      */   
/*      */   private static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> fromEntries(Map.Entry<K, V>... entries) {
/*  486 */     return fromEntries(Ordering.natural(), false, entries, entries.length);
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
/*      */   private static <K, V> ImmutableSortedMap<K, V> fromEntries(Comparator<? super K> comparator, boolean sameComparator, Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/*  501 */     Map.Entry[] arrayOfEntry = Iterables.<Map.Entry>toArray((Iterable)entries, (Map.Entry[])EMPTY_ENTRY_ARRAY);
/*  502 */     return fromEntries(comparator, sameComparator, (Map.Entry<K, V>[])arrayOfEntry, arrayOfEntry.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <K, V> ImmutableSortedMap<K, V> fromEntries(Comparator<? super K> comparator, boolean sameComparator, Map.Entry<K, V>[] entryArray, int size) {
/*      */     Map.Entry<K, V> onlyEntry;
/*  510 */     switch (size) {
/*      */       case 0:
/*  512 */         return emptyMap(comparator);
/*      */       
/*      */       case 1:
/*  515 */         onlyEntry = Objects.<Map.Entry<K, V>>requireNonNull(entryArray[0]);
/*  516 */         return of(comparator, onlyEntry.getKey(), onlyEntry.getValue());
/*      */     } 
/*  518 */     Object[] keys = new Object[size];
/*  519 */     Object[] values = new Object[size];
/*  520 */     if (sameComparator) {
/*      */       
/*  522 */       for (int i = 0; i < size; i++)
/*      */       {
/*  524 */         Map.Entry<K, V> entry = Objects.<Map.Entry<K, V>>requireNonNull(entryArray[i]);
/*  525 */         Object key = entry.getKey();
/*  526 */         Object value = entry.getValue();
/*  527 */         CollectPreconditions.checkEntryNotNull(key, value);
/*  528 */         keys[i] = key;
/*  529 */         values[i] = value;
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  535 */       Arrays.sort(entryArray, 0, size, (e1, e2) -> {
/*      */             Objects.requireNonNull(e1);
/*      */ 
/*      */             
/*      */             Objects.requireNonNull(e2);
/*      */ 
/*      */             
/*      */             return comparator.compare(e1.getKey(), e2.getKey());
/*      */           });
/*      */ 
/*      */       
/*  546 */       Map.Entry<K, V> firstEntry = Objects.<Map.Entry<K, V>>requireNonNull(entryArray[0]);
/*  547 */       K prevKey = firstEntry.getKey();
/*  548 */       keys[0] = prevKey;
/*  549 */       values[0] = firstEntry.getValue();
/*  550 */       CollectPreconditions.checkEntryNotNull(keys[0], values[0]);
/*  551 */       for (int i = 1; i < size; i++) {
/*      */         
/*  553 */         Map.Entry<K, V> prevEntry = Objects.<Map.Entry<K, V>>requireNonNull(entryArray[i - 1]);
/*  554 */         Map.Entry<K, V> entry = Objects.<Map.Entry<K, V>>requireNonNull(entryArray[i]);
/*  555 */         K key = entry.getKey();
/*  556 */         V value = entry.getValue();
/*  557 */         CollectPreconditions.checkEntryNotNull(key, value);
/*  558 */         keys[i] = key;
/*  559 */         values[i] = value;
/*  560 */         checkNoConflict((comparator.compare(prevKey, key) != 0), "key", prevEntry, entry);
/*  561 */         prevKey = key;
/*      */       } 
/*      */     } 
/*  564 */     return new ImmutableSortedMap<>(new RegularImmutableSortedSet<>(new RegularImmutableList<>(keys), comparator), new RegularImmutableList<>(values));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K extends Comparable<?>, V> Builder<K, V> naturalOrder() {
/*  575 */     return new Builder<>(Ordering.natural());
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
/*      */   public static <K, V> Builder<K, V> orderedBy(Comparator<K> comparator) {
/*  587 */     return new Builder<>(comparator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K extends Comparable<?>, V> Builder<K, V> reverseOrder() {
/*  595 */     return new Builder<>(Ordering.<Comparable>natural().reverse());
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
/*      */   public static class Builder<K, V>
/*      */     extends ImmutableMap.Builder<K, V>
/*      */   {
/*      */     private final Comparator<? super K> comparator;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder(Comparator<? super K> comparator) {
/*  628 */       this.comparator = (Comparator<? super K>)Preconditions.checkNotNull(comparator);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     public Builder<K, V> put(K key, V value) {
/*  639 */       super.put(key, value);
/*  640 */       return this;
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
/*      */     public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry) {
/*  653 */       super.put(entry);
/*  654 */       return this;
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
/*      */     public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
/*  667 */       super.putAll(map);
/*  668 */       return this;
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
/*      */     @CanIgnoreReturnValue
/*      */     public Builder<K, V> putAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/*  682 */       super.putAll(entries);
/*  683 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     @CanIgnoreReturnValue
/*      */     @DoNotCall("Always throws UnsupportedOperationException")
/*      */     public final Builder<K, V> orderEntriesByValue(Comparator<? super V> valueComparator) {
/*  697 */       throw new UnsupportedOperationException("Not available on ImmutableSortedMap.Builder");
/*      */     }
/*      */ 
/*      */     
/*      */     Builder<K, V> combine(ImmutableMap.Builder<K, V> other) {
/*  702 */       super.combine(other);
/*  703 */       return this;
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
/*      */     public ImmutableSortedMap<K, V> build() {
/*  718 */       return buildOrThrow();
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
/*      */     public ImmutableSortedMap<K, V> buildOrThrow() {
/*      */       Map.Entry<K, V> onlyEntry;
/*  731 */       switch (this.size) {
/*      */         case 0:
/*  733 */           return ImmutableSortedMap.emptyMap(this.comparator);
/*      */         
/*      */         case 1:
/*  736 */           onlyEntry = Objects.<Map.Entry<K, V>>requireNonNull(this.entries[0]);
/*  737 */           return ImmutableSortedMap.of(this.comparator, onlyEntry.getKey(), onlyEntry.getValue());
/*      */       } 
/*  739 */       return ImmutableSortedMap.fromEntries(this.comparator, false, this.entries, this.size);
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
/*      */     @Deprecated
/*      */     @DoNotCall
/*      */     public final ImmutableSortedMap<K, V> buildKeepingLast() {
/*  757 */       throw new UnsupportedOperationException("ImmutableSortedMap.Builder does not yet implement buildKeepingLast()");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   ImmutableSortedMap(RegularImmutableSortedSet<K> keySet, ImmutableList<V> valueList) {
/*  767 */     this(keySet, valueList, (ImmutableSortedMap<K, V>)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   ImmutableSortedMap(RegularImmutableSortedSet<K> keySet, ImmutableList<V> valueList, @CheckForNull ImmutableSortedMap<K, V> descendingMap) {
/*  774 */     this.keySet = keySet;
/*  775 */     this.valueList = valueList;
/*  776 */     this.descendingMap = descendingMap;
/*      */   }
/*      */ 
/*      */   
/*      */   public int size() {
/*  781 */     return this.valueList.size();
/*      */   }
/*      */ 
/*      */   
/*      */   public void forEach(BiConsumer<? super K, ? super V> action) {
/*  786 */     Preconditions.checkNotNull(action);
/*  787 */     ImmutableList<K> keyList = this.keySet.asList();
/*  788 */     for (int i = 0; i < size(); i++) {
/*  789 */       action.accept(keyList.get(i), this.valueList.get(i));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   public V get(@CheckForNull Object key) {
/*  796 */     int index = this.keySet.indexOf(key);
/*  797 */     return (index == -1) ? null : this.valueList.get(index);
/*      */   }
/*      */ 
/*      */   
/*      */   boolean isPartialView() {
/*  802 */     return (this.keySet.isPartialView() || this.valueList.isPartialView());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ImmutableSet<Map.Entry<K, V>> entrySet() {
/*  808 */     return super.entrySet();
/*      */   }
/*      */   
/*      */   ImmutableSet<Map.Entry<K, V>> createEntrySet() {
/*      */     class EntrySet
/*      */       extends ImmutableMapEntrySet<K, V>
/*      */     {
/*      */       public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
/*  816 */         return asList().iterator();
/*      */       }
/*      */ 
/*      */       
/*      */       public Spliterator<Map.Entry<K, V>> spliterator() {
/*  821 */         return asList().spliterator();
/*      */       }
/*      */ 
/*      */       
/*      */       public void forEach(Consumer<? super Map.Entry<K, V>> action) {
/*  826 */         asList().forEach(action);
/*      */       }
/*      */ 
/*      */       
/*      */       ImmutableList<Map.Entry<K, V>> createAsList() {
/*  831 */         return new ImmutableAsList<Map.Entry<K, V>>()
/*      */           {
/*      */             public Map.Entry<K, V> get(int index) {
/*  834 */               return new AbstractMap.SimpleImmutableEntry<>(ImmutableSortedMap.this
/*  835 */                   .keySet.asList().get(index), (V)ImmutableSortedMap.this.valueList.get(index));
/*      */             }
/*      */ 
/*      */             
/*      */             public Spliterator<Map.Entry<K, V>> spliterator() {
/*  840 */               return CollectSpliterators.indexed(
/*  841 */                   size(), 1297, this::get);
/*      */             }
/*      */ 
/*      */             
/*      */             ImmutableCollection<Map.Entry<K, V>> delegateCollection() {
/*  846 */               return ImmutableSortedMap.EntrySet.this;
/*      */             }
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             @J2ktIncompatible
/*      */             @GwtIncompatible
/*      */             Object writeReplace() {
/*  855 */               return super.writeReplace();
/*      */             }
/*      */           };
/*      */       }
/*      */ 
/*      */       
/*      */       ImmutableMap<K, V> map() {
/*  862 */         return ImmutableSortedMap.this;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       @J2ktIncompatible
/*      */       @GwtIncompatible
/*      */       Object writeReplace() {
/*  871 */         return super.writeReplace();
/*      */       }
/*      */     };
/*  874 */     return isEmpty() ? ImmutableSet.<Map.Entry<K, V>>of() : new EntrySet();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ImmutableSortedSet<K> keySet() {
/*  880 */     return this.keySet;
/*      */   }
/*      */ 
/*      */   
/*      */   ImmutableSet<K> createKeySet() {
/*  885 */     throw new AssertionError("should never be called");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ImmutableCollection<V> values() {
/*  894 */     return this.valueList;
/*      */   }
/*      */ 
/*      */   
/*      */   ImmutableCollection<V> createValues() {
/*  899 */     throw new AssertionError("should never be called");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Comparator<? super K> comparator() {
/*  909 */     return keySet().comparator();
/*      */   }
/*      */ 
/*      */   
/*      */   public K firstKey() {
/*  914 */     return keySet().first();
/*      */   }
/*      */ 
/*      */   
/*      */   public K lastKey() {
/*  919 */     return keySet().last();
/*      */   }
/*      */   
/*      */   private ImmutableSortedMap<K, V> getSubMap(int fromIndex, int toIndex) {
/*  923 */     if (fromIndex == 0 && toIndex == size())
/*  924 */       return this; 
/*  925 */     if (fromIndex == toIndex) {
/*  926 */       return emptyMap(comparator());
/*      */     }
/*  928 */     return new ImmutableSortedMap(this.keySet
/*  929 */         .getSubSet(fromIndex, toIndex), this.valueList.subList(fromIndex, toIndex));
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
/*      */   public ImmutableSortedMap<K, V> headMap(K toKey) {
/*  944 */     return headMap(toKey, false);
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
/*      */   public ImmutableSortedMap<K, V> headMap(K toKey, boolean inclusive) {
/*  960 */     return getSubMap(0, this.keySet.headIndex((K)Preconditions.checkNotNull(toKey), inclusive));
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
/*      */   public ImmutableSortedMap<K, V> subMap(K fromKey, K toKey) {
/*  975 */     return subMap(fromKey, true, toKey, false);
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
/*      */   public ImmutableSortedMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
/*  994 */     Preconditions.checkNotNull(fromKey);
/*  995 */     Preconditions.checkNotNull(toKey);
/*  996 */     Preconditions.checkArgument(
/*  997 */         (comparator().compare(fromKey, toKey) <= 0), "expected fromKey <= toKey but %s > %s", fromKey, toKey);
/*      */ 
/*      */ 
/*      */     
/* 1001 */     return headMap(toKey, toInclusive).tailMap(fromKey, fromInclusive);
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
/*      */   public ImmutableSortedMap<K, V> tailMap(K fromKey) {
/* 1015 */     return tailMap(fromKey, true);
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
/*      */   public ImmutableSortedMap<K, V> tailMap(K fromKey, boolean inclusive) {
/* 1031 */     return getSubMap(this.keySet.tailIndex((K)Preconditions.checkNotNull(fromKey), inclusive), size());
/*      */   }
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   public Map.Entry<K, V> lowerEntry(K key) {
/* 1037 */     return headMap(key, false).lastEntry();
/*      */   }
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   public K lowerKey(K key) {
/* 1043 */     return Maps.keyOrNull(lowerEntry(key));
/*      */   }
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   public Map.Entry<K, V> floorEntry(K key) {
/* 1049 */     return headMap(key, true).lastEntry();
/*      */   }
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   public K floorKey(K key) {
/* 1055 */     return Maps.keyOrNull(floorEntry(key));
/*      */   }
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   public Map.Entry<K, V> ceilingEntry(K key) {
/* 1061 */     return tailMap(key, true).firstEntry();
/*      */   }
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   public K ceilingKey(K key) {
/* 1067 */     return Maps.keyOrNull(ceilingEntry(key));
/*      */   }
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   public Map.Entry<K, V> higherEntry(K key) {
/* 1073 */     return tailMap(key, false).firstEntry();
/*      */   }
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   public K higherKey(K key) {
/* 1079 */     return Maps.keyOrNull(higherEntry(key));
/*      */   }
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   public Map.Entry<K, V> firstEntry() {
/* 1085 */     return isEmpty() ? null : entrySet().asList().get(0);
/*      */   }
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   public Map.Entry<K, V> lastEntry() {
/* 1091 */     return isEmpty() ? null : entrySet().asList().get(size() - 1);
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
/*      */   public final Map.Entry<K, V> pollFirstEntry() {
/* 1106 */     throw new UnsupportedOperationException();
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
/*      */   public final Map.Entry<K, V> pollLastEntry() {
/* 1121 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ImmutableSortedMap<K, V> descendingMap() {
/* 1131 */     ImmutableSortedMap<K, V> result = this.descendingMap;
/* 1132 */     if (result == null) {
/* 1133 */       if (isEmpty()) {
/* 1134 */         return emptyMap(Ordering.<K>from(comparator()).reverse());
/*      */       }
/* 1136 */       return new ImmutableSortedMap((RegularImmutableSortedSet<K>)this.keySet
/* 1137 */           .descendingSet(), this.valueList.reverse(), this);
/*      */     } 
/*      */     
/* 1140 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public ImmutableSortedSet<K> navigableKeySet() {
/* 1145 */     return this.keySet;
/*      */   }
/*      */ 
/*      */   
/*      */   public ImmutableSortedSet<K> descendingKeySet() {
/* 1150 */     return this.keySet.descendingSet();
/*      */   }
/*      */ 
/*      */   
/*      */   @J2ktIncompatible
/*      */   private static class SerializedForm<K, V>
/*      */     extends ImmutableMap.SerializedForm<K, V>
/*      */   {
/*      */     private final Comparator<? super K> comparator;
/*      */     
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SerializedForm(ImmutableSortedMap<K, V> sortedMap) {
/* 1163 */       super(sortedMap);
/* 1164 */       this.comparator = sortedMap.comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     ImmutableSortedMap.Builder<K, V> makeBuilder(int size) {
/* 1169 */       return new ImmutableSortedMap.Builder<>(this.comparator);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @J2ktIncompatible
/*      */   Object writeReplace() {
/* 1178 */     return new SerializedForm<>(this);
/*      */   }
/*      */   
/*      */   @J2ktIncompatible
/*      */   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 1183 */     throw new InvalidObjectException("Use SerializedForm");
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
/*      */   @Deprecated
/*      */   @DoNotCall("Use toImmutableSortedMap")
/*      */   public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/* 1204 */     throw new UnsupportedOperationException();
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
/*      */   @Deprecated
/*      */   @DoNotCall("Use toImmutableSortedMap")
/*      */   public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction) {
/* 1222 */     throw new UnsupportedOperationException();
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
/*      */   @DoNotCall("Use naturalOrder")
/*      */   public static <K, V> Builder<K, V> builder() {
/* 1235 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   @DoNotCall("Use naturalOrder (which does not accept an expected size)")
/*      */   public static <K, V> Builder<K, V> builderWithExpectedSize(int expectedSize) {
/* 1247 */     throw new UnsupportedOperationException();
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
/*      */   @Deprecated
/*      */   @DoNotCall("Pass a key of type Comparable")
/*      */   public static <K, V> ImmutableSortedMap<K, V> of(K k1, V v1) {
/* 1262 */     throw new UnsupportedOperationException();
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
/*      */   @Deprecated
/*      */   @DoNotCall("Pass keys of type Comparable")
/*      */   public static <K, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2) {
/* 1277 */     throw new UnsupportedOperationException();
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
/*      */   @Deprecated
/*      */   @DoNotCall("Pass keys of type Comparable")
/*      */   public static <K, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
/* 1292 */     throw new UnsupportedOperationException();
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
/*      */   @Deprecated
/*      */   @DoNotCall("Pass keys of type Comparable")
/*      */   public static <K, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
/* 1308 */     throw new UnsupportedOperationException();
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
/*      */   @Deprecated
/*      */   @DoNotCall("Pass keys of type Comparable")
/*      */   public static <K, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
/* 1325 */     throw new UnsupportedOperationException();
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
/*      */   @Deprecated
/*      */   @DoNotCall("Pass keys of type Comparable")
/*      */   public static <K, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
/* 1342 */     throw new UnsupportedOperationException();
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
/*      */   @Deprecated
/*      */   @DoNotCall("Pass keys of type Comparable")
/*      */   public static <K, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
/* 1359 */     throw new UnsupportedOperationException();
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
/*      */   @Deprecated
/*      */   @DoNotCall("Pass keys of type Comparable")
/*      */   public static <K, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
/* 1391 */     throw new UnsupportedOperationException();
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
/*      */   @Deprecated
/*      */   @DoNotCall("Pass keys of type Comparable")
/*      */   public static <K, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
/* 1425 */     throw new UnsupportedOperationException();
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
/*      */   @Deprecated
/*      */   @DoNotCall("Pass keys of type Comparable")
/*      */   public static <K, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
/* 1461 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   @DoNotCall("ImmutableSortedMap.ofEntries not currently available; use ImmutableSortedMap.copyOf")
/*      */   public static <K, V> ImmutableSortedMap<K, V> ofEntries(Map.Entry<? extends K, ? extends V>... entries) {
/* 1473 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ImmutableSortedMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */