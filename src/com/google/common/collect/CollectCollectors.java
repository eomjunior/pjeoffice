/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.EnumMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Objects;
/*     */ import java.util.TreeMap;
/*     */ import java.util.function.BinaryOperator;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.function.ToIntFunction;
/*     */ import java.util.stream.Collector;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ final class CollectCollectors
/*     */ {
/*  49 */   private static final Collector<Object, ?, ImmutableList<Object>> TO_IMMUTABLE_LIST = Collector.of(ImmutableList::builder, ImmutableList.Builder::add, ImmutableList.Builder::combine, ImmutableList.Builder::build, new Collector.Characteristics[0]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   private static final Collector<Object, ?, ImmutableSet<Object>> TO_IMMUTABLE_SET = Collector.of(ImmutableSet::builder, ImmutableSet.Builder::add, ImmutableSet.Builder::combine, ImmutableSet.Builder::build, new Collector.Characteristics[0]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*  65 */   private static final Collector<Range<Comparable<?>>, ?, ImmutableRangeSet<Comparable<?>>> TO_IMMUTABLE_RANGE_SET = Collector.of(ImmutableRangeSet::builder, ImmutableRangeSet.Builder::add, ImmutableRangeSet.Builder::combine, ImmutableRangeSet.Builder::build, new Collector.Characteristics[0]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <E> Collector<E, ?, ImmutableList<E>> toImmutableList() {
/*  75 */     return (Collector)TO_IMMUTABLE_LIST;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <E> Collector<E, ?, ImmutableSet<E>> toImmutableSet() {
/*  82 */     return (Collector)TO_IMMUTABLE_SET;
/*     */   }
/*     */ 
/*     */   
/*     */   static <E> Collector<E, ?, ImmutableSortedSet<E>> toImmutableSortedSet(Comparator<? super E> comparator) {
/*  87 */     Preconditions.checkNotNull(comparator);
/*  88 */     return Collector.of(() -> new ImmutableSortedSet.Builder(comparator), ImmutableSortedSet.Builder::add, ImmutableSortedSet.Builder::combine, ImmutableSortedSet.Builder::build, new Collector.Characteristics[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <E extends Enum<E>> Collector<E, ?, ImmutableSet<E>> toImmutableEnumSet() {
/*  97 */     return (Collector)EnumSetAccumulator.TO_IMMUTABLE_ENUM_SET;
/*     */   }
/*     */ 
/*     */   
/*     */   private static <E extends Enum<E>> Collector<E, EnumSetAccumulator<E>, ImmutableSet<E>> toImmutableEnumSetGeneric() {
/* 102 */     return Collector.of(() -> new EnumSetAccumulator<>(), EnumSetAccumulator::add, EnumSetAccumulator::combine, EnumSetAccumulator::toImmutableSet, new Collector.Characteristics[] { Collector.Characteristics.UNORDERED });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class EnumSetAccumulator<E extends Enum<E>>
/*     */   {
/*     */     private EnumSetAccumulator() {}
/*     */ 
/*     */ 
/*     */     
/* 113 */     static final Collector<Enum<?>, ?, ImmutableSet<? extends Enum<?>>> TO_IMMUTABLE_ENUM_SET = CollectCollectors.toImmutableEnumSetGeneric();
/*     */     @CheckForNull
/*     */     private EnumSet<E> set;
/*     */     
/*     */     void add(E e) {
/* 118 */       if (this.set == null) {
/* 119 */         this.set = EnumSet.of(e);
/*     */       } else {
/* 121 */         this.set.add(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     EnumSetAccumulator<E> combine(EnumSetAccumulator<E> other) {
/* 126 */       if (this.set == null)
/* 127 */         return other; 
/* 128 */       if (other.set == null) {
/* 129 */         return this;
/*     */       }
/* 131 */       this.set.addAll(other.set);
/* 132 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSet<E> toImmutableSet() {
/* 137 */       if (this.set == null) {
/* 138 */         return ImmutableSet.of();
/*     */       }
/* 140 */       ImmutableSet<E> ret = ImmutableEnumSet.asImmutable(this.set);
/* 141 */       this.set = null;
/* 142 */       return ret;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   static <E extends Comparable<? super E>> Collector<Range<E>, ?, ImmutableRangeSet<E>> toImmutableRangeSet() {
/* 150 */     return (Collector)TO_IMMUTABLE_RANGE_SET;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T, E> Collector<T, ?, ImmutableMultiset<E>> toImmutableMultiset(Function<? super T, ? extends E> elementFunction, ToIntFunction<? super T> countFunction) {
/* 157 */     Preconditions.checkNotNull(elementFunction);
/* 158 */     Preconditions.checkNotNull(countFunction);
/* 159 */     return Collector.of(LinkedHashMultiset::create, (multiset, t) -> multiset.add(Preconditions.checkNotNull(elementFunction.apply(t)), countFunction.applyAsInt(t)), (multiset1, multiset2) -> { multiset1.addAll(multiset2); return multiset1; }multiset -> ImmutableMultiset.copyFromEntries(multiset.entrySet()), new Collector.Characteristics[0]);
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
/*     */   static <T, E, M extends Multiset<E>> Collector<T, ?, M> toMultiset(Function<? super T, E> elementFunction, ToIntFunction<? super T> countFunction, Supplier<M> multisetSupplier) {
/* 175 */     Preconditions.checkNotNull(elementFunction);
/* 176 */     Preconditions.checkNotNull(countFunction);
/* 177 */     Preconditions.checkNotNull(multisetSupplier);
/* 178 */     return (Collector)Collector.of(multisetSupplier, (ms, t) -> ms.add(elementFunction.apply(t), countFunction.applyAsInt(t)), (ms1, ms2) -> { ms1.addAll(ms2); return ms1; }new Collector.Characteristics[0]);
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
/*     */   static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/* 192 */     Preconditions.checkNotNull(keyFunction);
/* 193 */     Preconditions.checkNotNull(valueFunction);
/* 194 */     return Collector.of(Builder::new, (builder, input) -> builder.put(keyFunction.apply(input), valueFunction.apply(input)), ImmutableMap.Builder::combine, ImmutableMap.Builder::buildOrThrow, new Collector.Characteristics[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction) {
/* 205 */     Preconditions.checkNotNull(keyFunction);
/* 206 */     Preconditions.checkNotNull(valueFunction);
/* 207 */     Preconditions.checkNotNull(mergeFunction);
/* 208 */     return Collectors.collectingAndThen(
/* 209 */         Collectors.toMap(keyFunction, valueFunction, mergeFunction, java.util.LinkedHashMap::new), ImmutableMap::copyOf);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(Comparator<? super K> comparator, Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/* 218 */     Preconditions.checkNotNull(comparator);
/* 219 */     Preconditions.checkNotNull(keyFunction);
/* 220 */     Preconditions.checkNotNull(valueFunction);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 225 */     return Collector.of(() -> new ImmutableSortedMap.Builder<>(comparator), (builder, input) -> builder.put(keyFunction.apply(input), valueFunction.apply(input)), ImmutableSortedMap.Builder::combine, ImmutableSortedMap.Builder::buildOrThrow, new Collector.Characteristics[] { Collector.Characteristics.UNORDERED });
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
/*     */   static <T, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(Comparator<? super K> comparator, Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction) {
/* 239 */     Preconditions.checkNotNull(comparator);
/* 240 */     Preconditions.checkNotNull(keyFunction);
/* 241 */     Preconditions.checkNotNull(valueFunction);
/* 242 */     Preconditions.checkNotNull(mergeFunction);
/* 243 */     return Collectors.collectingAndThen(
/* 244 */         Collectors.toMap(keyFunction, valueFunction, mergeFunction, () -> new TreeMap<>(comparator)), ImmutableSortedMap::copyOfSorted);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T, K, V> Collector<T, ?, ImmutableBiMap<K, V>> toImmutableBiMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/* 252 */     Preconditions.checkNotNull(keyFunction);
/* 253 */     Preconditions.checkNotNull(valueFunction);
/* 254 */     return Collector.of(Builder::new, (builder, input) -> builder.put(keyFunction.apply(input), valueFunction.apply(input)), ImmutableBiMap.Builder::combine, ImmutableBiMap.Builder::buildOrThrow, new Collector.Characteristics[0]);
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
/*     */   @J2ktIncompatible
/*     */   static <T, K extends Enum<K>, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableEnumMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/* 267 */     Preconditions.checkNotNull(keyFunction);
/* 268 */     Preconditions.checkNotNull(valueFunction);
/* 269 */     return Collector.of(() -> new EnumMapAccumulator<>(()), (accum, t) -> { Enum enum_ = keyFunction.apply(t); V newValue = valueFunction.apply(t); accum.put((Enum)Preconditions.checkNotNull(enum_, "Null key for input %s", t), Preconditions.checkNotNull(newValue, "Null value for input %s", t)); }EnumMapAccumulator::combine, EnumMapAccumulator::toImmutableMap, new Collector.Characteristics[] { Collector.Characteristics.UNORDERED });
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
/*     */   @J2ktIncompatible
/*     */   static <T, K extends Enum<K>, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableEnumMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction) {
/* 297 */     Preconditions.checkNotNull(keyFunction);
/* 298 */     Preconditions.checkNotNull(valueFunction);
/* 299 */     Preconditions.checkNotNull(mergeFunction);
/*     */     
/* 301 */     return Collector.of(() -> new EnumMapAccumulator<>(mergeFunction), (accum, t) -> { Enum enum_ = keyFunction.apply(t); V newValue = valueFunction.apply(t); accum.put((Enum)Preconditions.checkNotNull(enum_, "Null key for input %s", t), Preconditions.checkNotNull(newValue, "Null value for input %s", t)); }EnumMapAccumulator::combine, EnumMapAccumulator::toImmutableMap, new Collector.Characteristics[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   private static class EnumMapAccumulator<K extends Enum<K>, V>
/*     */   {
/*     */     private final BinaryOperator<V> mergeFunction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CheckForNull
/* 321 */     private EnumMap<K, V> map = null;
/*     */     
/*     */     EnumMapAccumulator(BinaryOperator<V> mergeFunction) {
/* 324 */       this.mergeFunction = mergeFunction;
/*     */     }
/*     */     
/*     */     void put(K key, V value) {
/* 328 */       if (this.map == null) {
/* 329 */         this.map = new EnumMap<>(Collections.singletonMap(key, value));
/*     */       } else {
/* 331 */         this.map.merge(key, value, this.mergeFunction);
/*     */       } 
/*     */     }
/*     */     
/*     */     EnumMapAccumulator<K, V> combine(EnumMapAccumulator<K, V> other) {
/* 336 */       if (this.map == null)
/* 337 */         return other; 
/* 338 */       if (other.map == null) {
/* 339 */         return this;
/*     */       }
/* 341 */       other.map.forEach(this::put);
/* 342 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableMap<K, V> toImmutableMap() {
/* 347 */       return (this.map == null) ? ImmutableMap.<K, V>of() : ImmutableEnumMap.<K, V>asImmutable(this.map);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   static <T, K extends Comparable<? super K>, V> Collector<T, ?, ImmutableRangeMap<K, V>> toImmutableRangeMap(Function<? super T, Range<K>> keyFunction, Function<? super T, ? extends V> valueFunction) {
/* 356 */     Preconditions.checkNotNull(keyFunction);
/* 357 */     Preconditions.checkNotNull(valueFunction);
/* 358 */     return Collector.of(ImmutableRangeMap::builder, (builder, input) -> builder.put(keyFunction.apply(input), valueFunction.apply(input)), ImmutableRangeMap.Builder::combine, ImmutableRangeMap.Builder::build, new Collector.Characteristics[0]);
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
/*     */   static <T, K, V> Collector<T, ?, ImmutableListMultimap<K, V>> toImmutableListMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/* 371 */     Preconditions.checkNotNull(keyFunction, "keyFunction");
/* 372 */     Preconditions.checkNotNull(valueFunction, "valueFunction");
/* 373 */     return Collector.of(ImmutableListMultimap::builder, (builder, t) -> builder.put(keyFunction.apply(t), valueFunction.apply(t)), ImmutableListMultimap.Builder::combine, ImmutableListMultimap.Builder::build, new Collector.Characteristics[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T, K, V> Collector<T, ?, ImmutableListMultimap<K, V>> flatteningToImmutableListMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends Stream<? extends V>> valuesFunction) {
/* 384 */     Preconditions.checkNotNull(keyFunction);
/* 385 */     Preconditions.checkNotNull(valuesFunction);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 390 */     Objects.requireNonNull(MultimapBuilder.linkedHashKeys().arrayListValues()); return Collectors.collectingAndThen(flatteningToMultimap(input -> Preconditions.checkNotNull(keyFunction.apply(input)), input -> ((Stream)valuesFunction.apply(input)).peek(Preconditions::checkNotNull), MultimapBuilder.linkedHashKeys().arrayListValues()::build), ImmutableListMultimap::copyOf);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T, K, V> Collector<T, ?, ImmutableSetMultimap<K, V>> toImmutableSetMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/* 398 */     Preconditions.checkNotNull(keyFunction, "keyFunction");
/* 399 */     Preconditions.checkNotNull(valueFunction, "valueFunction");
/* 400 */     return Collector.of(ImmutableSetMultimap::builder, (builder, t) -> builder.put(keyFunction.apply(t), valueFunction.apply(t)), ImmutableSetMultimap.Builder::combine, ImmutableSetMultimap.Builder::build, new Collector.Characteristics[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T, K, V> Collector<T, ?, ImmutableSetMultimap<K, V>> flatteningToImmutableSetMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends Stream<? extends V>> valuesFunction) {
/* 411 */     Preconditions.checkNotNull(keyFunction);
/* 412 */     Preconditions.checkNotNull(valuesFunction);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 417 */     Objects.requireNonNull(MultimapBuilder.linkedHashKeys().linkedHashSetValues()); return Collectors.collectingAndThen(flatteningToMultimap(input -> Preconditions.checkNotNull(keyFunction.apply(input)), input -> ((Stream)valuesFunction.apply(input)).peek(Preconditions::checkNotNull), MultimapBuilder.linkedHashKeys().linkedHashSetValues()::build), ImmutableSetMultimap::copyOf);
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
/*     */   static <T, K, V, M extends Multimap<K, V>> Collector<T, ?, M> toMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, Supplier<M> multimapSupplier) {
/* 430 */     Preconditions.checkNotNull(keyFunction);
/* 431 */     Preconditions.checkNotNull(valueFunction);
/* 432 */     Preconditions.checkNotNull(multimapSupplier);
/* 433 */     return (Collector)Collector.of(multimapSupplier, (multimap, input) -> multimap.put(keyFunction.apply(input), valueFunction.apply(input)), (multimap1, multimap2) -> { multimap1.putAll(multimap2); return multimap1; }new Collector.Characteristics[0]);
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
/*     */   static <T, K, V, M extends Multimap<K, V>> Collector<T, ?, M> flatteningToMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends Stream<? extends V>> valueFunction, Supplier<M> multimapSupplier) {
/* 451 */     Preconditions.checkNotNull(keyFunction);
/* 452 */     Preconditions.checkNotNull(valueFunction);
/* 453 */     Preconditions.checkNotNull(multimapSupplier);
/* 454 */     return (Collector)Collector.of(multimapSupplier, (multimap, input) -> { K key = keyFunction.apply(input); Collection<V> valuesForKey = multimap.get(key); Objects.requireNonNull(valuesForKey); ((Stream)valueFunction.apply(input)).forEachOrdered(valuesForKey::add); }(multimap1, multimap2) -> { multimap1.putAll(multimap2); return multimap1; }new Collector.Characteristics[0]);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/CollectCollectors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */