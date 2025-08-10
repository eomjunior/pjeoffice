/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.DoNotCall;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import com.google.j2objc.annotations.RetainedWith;
/*     */ import com.google.j2objc.annotations.Weak;
/*     */ import java.io.IOException;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collector;
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
/*     */ public class ImmutableSetMultimap<K, V>
/*     */   extends ImmutableMultimap<K, V>
/*     */   implements SetMultimap<K, V>
/*     */ {
/*     */   private final transient ImmutableSet<V> emptySet;
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   @RetainedWith
/*     */   private transient ImmutableSetMultimap<V, K> inverse;
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   @RetainedWith
/*     */   private transient ImmutableSet<Map.Entry<K, V>> entries;
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <T, K, V> Collector<T, ?, ImmutableSetMultimap<K, V>> toImmutableSetMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/*  96 */     return CollectCollectors.toImmutableSetMultimap(keyFunction, valueFunction);
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
/*     */   public static <T, K, V> Collector<T, ?, ImmutableSetMultimap<K, V>> flatteningToImmutableSetMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends Stream<? extends V>> valuesFunction) {
/* 143 */     return CollectCollectors.flatteningToImmutableSetMultimap(keyFunction, valuesFunction);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableSetMultimap<K, V> of() {
/* 154 */     return EmptyImmutableSetMultimap.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1) {
/* 159 */     Builder<K, V> builder = builder();
/* 160 */     builder.put(k1, v1);
/* 161 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2) {
/* 169 */     Builder<K, V> builder = builder();
/* 170 */     builder.put(k1, v1);
/* 171 */     builder.put(k2, v2);
/* 172 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
/* 180 */     Builder<K, V> builder = builder();
/* 181 */     builder.put(k1, v1);
/* 182 */     builder.put(k2, v2);
/* 183 */     builder.put(k3, v3);
/* 184 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
/* 193 */     Builder<K, V> builder = builder();
/* 194 */     builder.put(k1, v1);
/* 195 */     builder.put(k2, v2);
/* 196 */     builder.put(k3, v3);
/* 197 */     builder.put(k4, v4);
/* 198 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
/* 207 */     Builder<K, V> builder = builder();
/* 208 */     builder.put(k1, v1);
/* 209 */     builder.put(k2, v2);
/* 210 */     builder.put(k3, v3);
/* 211 */     builder.put(k4, v4);
/* 212 */     builder.put(k5, v5);
/* 213 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> Builder<K, V> builder() {
/* 220 */     return new Builder<>();
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
/*     */   public static final class Builder<K, V>
/*     */     extends ImmutableMultimap.Builder<K, V>
/*     */   {
/*     */     Collection<V> newMutableValueCollection() {
/* 253 */       return Platform.preservesInsertionOrderOnAddsSet();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(K key, V value) {
/* 260 */       super.put(key, value);
/* 261 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry) {
/* 272 */       super.put(entry);
/* 273 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/* 284 */       super.putAll(entries);
/* 285 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(K key, Iterable<? extends V> values) {
/* 291 */       super.putAll(key, values);
/* 292 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(K key, V... values) {
/* 298 */       return putAll(key, Arrays.asList(values));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(Multimap<? extends K, ? extends V> multimap) {
/* 305 */       for (Map.Entry<? extends K, ? extends Collection<? extends V>> entry : (Iterable<Map.Entry<? extends K, ? extends Collection<? extends V>>>)multimap.asMap().entrySet()) {
/* 306 */         putAll(entry.getKey(), entry.getValue());
/*     */       }
/* 308 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<K, V> combine(ImmutableMultimap.Builder<K, V> other) {
/* 314 */       super.combine(other);
/* 315 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> orderKeysBy(Comparator<? super K> keyComparator) {
/* 326 */       super.orderKeysBy(keyComparator);
/* 327 */       return this;
/*     */     }
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> orderValuesBy(Comparator<? super V> valueComparator) {
/* 344 */       super.orderValuesBy(valueComparator);
/* 345 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableSetMultimap<K, V> build() {
/* 351 */       Collection<Map.Entry<K, Collection<V>>> mapEntries = this.builderMap.entrySet();
/* 352 */       if (this.keyComparator != null) {
/* 353 */         mapEntries = Ordering.<K>from(this.keyComparator).onKeys().immutableSortedCopy(mapEntries);
/*     */       }
/* 355 */       return ImmutableSetMultimap.fromMapEntries(mapEntries, this.valueComparator);
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
/*     */   public static <K, V> ImmutableSetMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap) {
/* 373 */     return copyOf(multimap, (Comparator<? super V>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <K, V> ImmutableSetMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap, @CheckForNull Comparator<? super V> valueComparator) {
/* 379 */     Preconditions.checkNotNull(multimap);
/* 380 */     if (multimap.isEmpty() && valueComparator == null) {
/* 381 */       return of();
/*     */     }
/*     */     
/* 384 */     if (multimap instanceof ImmutableSetMultimap) {
/*     */       
/* 386 */       ImmutableSetMultimap<K, V> kvMultimap = (ImmutableSetMultimap)multimap;
/* 387 */       if (!kvMultimap.isPartialView()) {
/* 388 */         return kvMultimap;
/*     */       }
/*     */     } 
/*     */     
/* 392 */     return fromMapEntries(multimap.asMap().entrySet(), valueComparator);
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
/*     */   public static <K, V> ImmutableSetMultimap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/* 406 */     return (new Builder<>()).putAll(entries).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> ImmutableSetMultimap<K, V> fromMapEntries(Collection<? extends Map.Entry<? extends K, ? extends Collection<? extends V>>> mapEntries, @CheckForNull Comparator<? super V> valueComparator) {
/* 413 */     if (mapEntries.isEmpty()) {
/* 414 */       return of();
/*     */     }
/*     */     
/* 417 */     ImmutableMap.Builder<K, ImmutableSet<V>> builder = new ImmutableMap.Builder<>(mapEntries.size());
/* 418 */     int size = 0;
/*     */     
/* 420 */     for (Map.Entry<? extends K, ? extends Collection<? extends V>> entry : mapEntries) {
/* 421 */       K key = entry.getKey();
/* 422 */       Collection<? extends V> values = entry.getValue();
/* 423 */       ImmutableSet<V> set = valueSet(valueComparator, values);
/* 424 */       if (!set.isEmpty()) {
/* 425 */         builder.put(key, set);
/* 426 */         size += set.size();
/*     */       } 
/*     */     } 
/*     */     
/* 430 */     return new ImmutableSetMultimap<>(builder.buildOrThrow(), size, valueComparator);
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
/*     */   ImmutableSetMultimap(ImmutableMap<K, ImmutableSet<V>> map, int size, @CheckForNull Comparator<? super V> valueComparator) {
/* 443 */     super((ImmutableMap)map, size);
/* 444 */     this.emptySet = emptySet(valueComparator);
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
/*     */   public ImmutableSet<V> get(K key) {
/* 457 */     ImmutableSet<V> set = (ImmutableSet<V>)this.map.get(key);
/* 458 */     return (ImmutableSet<V>)MoreObjects.firstNonNull(set, this.emptySet);
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
/*     */   public ImmutableSetMultimap<V, K> inverse() {
/* 472 */     ImmutableSetMultimap<V, K> result = this.inverse;
/* 473 */     return (result == null) ? (this.inverse = invert()) : result;
/*     */   }
/*     */   
/*     */   private ImmutableSetMultimap<V, K> invert() {
/* 477 */     Builder<V, K> builder = builder();
/* 478 */     for (UnmodifiableIterator<Map.Entry<K, V>> unmodifiableIterator = entries().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<K, V> entry = unmodifiableIterator.next();
/* 479 */       builder.put(entry.getValue(), entry.getKey()); }
/*     */     
/* 481 */     ImmutableSetMultimap<V, K> invertedMultimap = builder.build();
/* 482 */     invertedMultimap.inverse = this;
/* 483 */     return invertedMultimap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   @DoNotCall("Always throws UnsupportedOperationException")
/*     */   public final ImmutableSet<V> removeAll(@CheckForNull Object key) {
/* 497 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   @DoNotCall("Always throws UnsupportedOperationException")
/*     */   public final ImmutableSet<V> replaceValues(K key, Iterable<? extends V> values) {
/* 511 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<Map.Entry<K, V>> entries() {
/* 522 */     ImmutableSet<Map.Entry<K, V>> result = this.entries;
/* 523 */     return (result == null) ? (this.entries = new EntrySet<>(this)) : result;
/*     */   }
/*     */   
/*     */   private static final class EntrySet<K, V> extends ImmutableSet<Map.Entry<K, V>> { @Weak
/*     */     private final transient ImmutableSetMultimap<K, V> multimap;
/*     */     
/*     */     EntrySet(ImmutableSetMultimap<K, V> multimap) {
/* 530 */       this.multimap = multimap;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(@CheckForNull Object object) {
/* 535 */       if (object instanceof Map.Entry) {
/* 536 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
/* 537 */         return this.multimap.containsEntry(entry.getKey(), entry.getValue());
/*     */       } 
/* 539 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 544 */       return this.multimap.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
/* 549 */       return this.multimap.entryIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 554 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @J2ktIncompatible
/*     */     @GwtIncompatible
/*     */     Object writeReplace() {
/* 563 */       return super.writeReplace();
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <V> ImmutableSet<V> valueSet(@CheckForNull Comparator<? super V> valueComparator, Collection<? extends V> values) {
/* 569 */     return (valueComparator == null) ? 
/* 570 */       ImmutableSet.<V>copyOf(values) : 
/* 571 */       ImmutableSortedSet.<V>copyOf(valueComparator, values);
/*     */   }
/*     */   
/*     */   private static <V> ImmutableSet<V> emptySet(@CheckForNull Comparator<? super V> valueComparator) {
/* 575 */     return (valueComparator == null) ? 
/* 576 */       ImmutableSet.<V>of() : 
/* 577 */       ImmutableSortedSet.<V>emptySet(valueComparator);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <V> ImmutableSet.Builder<V> valuesBuilder(@CheckForNull Comparator<? super V> valueComparator) {
/* 582 */     return (valueComparator == null) ? 
/* 583 */       new ImmutableSet.Builder<>() : 
/* 584 */       new ImmutableSortedSet.Builder<>(valueComparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 594 */     stream.defaultWriteObject();
/* 595 */     stream.writeObject(valueComparator());
/* 596 */     Serialization.writeMultimap(this, stream);
/*     */   }
/*     */   
/*     */   @CheckForNull
/*     */   Comparator<? super V> valueComparator() {
/* 601 */     return (this.emptySet instanceof ImmutableSortedSet) ? (
/* 602 */       (ImmutableSortedSet<V>)this.emptySet).comparator() : 
/* 603 */       null;
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   private static final class SetFieldSettersHolder
/*     */   {
/* 610 */     static final Serialization.FieldSetter<ImmutableSetMultimap> EMPTY_SET_FIELD_SETTER = Serialization.getFieldSetter(ImmutableSetMultimap.class, "emptySet");
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*     */     ImmutableMap<Object, ImmutableSet<Object>> tmpMap;
/* 618 */     stream.defaultReadObject();
/* 619 */     Comparator<Object> valueComparator = (Comparator<Object>)stream.readObject();
/* 620 */     int keyCount = stream.readInt();
/* 621 */     if (keyCount < 0) {
/* 622 */       throw new InvalidObjectException("Invalid key count " + keyCount);
/*     */     }
/* 624 */     ImmutableMap.Builder<Object, ImmutableSet<Object>> builder = ImmutableMap.builder();
/* 625 */     int tmpSize = 0;
/*     */     
/* 627 */     for (int i = 0; i < keyCount; i++) {
/* 628 */       Object key = Objects.requireNonNull(stream.readObject());
/* 629 */       int valueCount = stream.readInt();
/* 630 */       if (valueCount <= 0) {
/* 631 */         throw new InvalidObjectException("Invalid value count " + valueCount);
/*     */       }
/*     */       
/* 634 */       ImmutableSet.Builder<Object> valuesBuilder = valuesBuilder(valueComparator);
/* 635 */       for (int j = 0; j < valueCount; j++) {
/* 636 */         valuesBuilder.add(Objects.requireNonNull(stream.readObject()));
/*     */       }
/* 638 */       ImmutableSet<Object> valueSet = valuesBuilder.build();
/* 639 */       if (valueSet.size() != valueCount) {
/* 640 */         throw new InvalidObjectException("Duplicate key-value pairs exist for key " + key);
/*     */       }
/* 642 */       builder.put(key, valueSet);
/* 643 */       tmpSize += valueCount;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 648 */       tmpMap = builder.buildOrThrow();
/* 649 */     } catch (IllegalArgumentException e) {
/* 650 */       throw (InvalidObjectException)(new InvalidObjectException(e.getMessage())).initCause(e);
/*     */     } 
/*     */     
/* 653 */     ImmutableMultimap.FieldSettersHolder.MAP_FIELD_SETTER.set(this, tmpMap);
/* 654 */     ImmutableMultimap.FieldSettersHolder.SIZE_FIELD_SETTER.set(this, tmpSize);
/* 655 */     SetFieldSettersHolder.EMPTY_SET_FIELD_SETTER.set(this, emptySet(valueComparator));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ImmutableSetMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */