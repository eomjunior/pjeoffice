/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.DoNotCall;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import com.google.j2objc.annotations.RetainedWith;
/*     */ import java.io.IOException;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
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
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public class ImmutableListMultimap<K, V>
/*     */   extends ImmutableMultimap<K, V>
/*     */   implements ListMultimap<K, V>
/*     */ {
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   @RetainedWith
/*     */   private transient ImmutableListMultimap<V, K> inverse;
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <T, K, V> Collector<T, ?, ImmutableListMultimap<K, V>> toImmutableListMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/*  88 */     return CollectCollectors.toImmutableListMultimap(keyFunction, valueFunction);
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
/*     */   public static <T, K, V> Collector<T, ?, ImmutableListMultimap<K, V>> flatteningToImmutableListMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends Stream<? extends V>> valuesFunction) {
/* 126 */     return CollectCollectors.flatteningToImmutableListMultimap(keyFunction, valuesFunction);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of() {
/* 137 */     return EmptyImmutableListMultimap.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1) {
/* 142 */     Builder<K, V> builder = builder();
/* 143 */     builder.put(k1, v1);
/* 144 */     return builder.build();
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1, K k2, V v2) {
/* 149 */     Builder<K, V> builder = builder();
/* 150 */     builder.put(k1, v1);
/* 151 */     builder.put(k2, v2);
/* 152 */     return builder.build();
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
/* 157 */     Builder<K, V> builder = builder();
/* 158 */     builder.put(k1, v1);
/* 159 */     builder.put(k2, v2);
/* 160 */     builder.put(k3, v3);
/* 161 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
/* 167 */     Builder<K, V> builder = builder();
/* 168 */     builder.put(k1, v1);
/* 169 */     builder.put(k2, v2);
/* 170 */     builder.put(k3, v3);
/* 171 */     builder.put(k4, v4);
/* 172 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
/* 178 */     Builder<K, V> builder = builder();
/* 179 */     builder.put(k1, v1);
/* 180 */     builder.put(k2, v2);
/* 181 */     builder.put(k3, v3);
/* 182 */     builder.put(k4, v4);
/* 183 */     builder.put(k5, v5);
/* 184 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> Builder<K, V> builder() {
/* 194 */     return new Builder<>();
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
/*     */   public static final class Builder<K, V>
/*     */     extends ImmutableMultimap.Builder<K, V>
/*     */   {
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(K key, V value) {
/* 226 */       super.put(key, value);
/* 227 */       return this;
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
/* 238 */       super.put(entry);
/* 239 */       return this;
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
/* 250 */       super.putAll(entries);
/* 251 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(K key, Iterable<? extends V> values) {
/* 257 */       super.putAll(key, values);
/* 258 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(K key, V... values) {
/* 264 */       super.putAll(key, values);
/* 265 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(Multimap<? extends K, ? extends V> multimap) {
/* 271 */       super.putAll(multimap);
/* 272 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<K, V> combine(ImmutableMultimap.Builder<K, V> other) {
/* 278 */       super.combine(other);
/* 279 */       return this;
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
/* 290 */       super.orderKeysBy(keyComparator);
/* 291 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> orderValuesBy(Comparator<? super V> valueComparator) {
/* 302 */       super.orderValuesBy(valueComparator);
/* 303 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableListMultimap<K, V> build() {
/* 309 */       return (ImmutableListMultimap<K, V>)super.build();
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
/*     */   public static <K, V> ImmutableListMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap) {
/* 326 */     if (multimap.isEmpty()) {
/* 327 */       return of();
/*     */     }
/*     */ 
/*     */     
/* 331 */     if (multimap instanceof ImmutableListMultimap) {
/*     */       
/* 333 */       ImmutableListMultimap<K, V> kvMultimap = (ImmutableListMultimap)multimap;
/* 334 */       if (!kvMultimap.isPartialView()) {
/* 335 */         return kvMultimap;
/*     */       }
/*     */     } 
/*     */     
/* 339 */     return fromMapEntries(multimap.asMap().entrySet(), (Comparator<? super V>)null);
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
/*     */   public static <K, V> ImmutableListMultimap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/* 352 */     return (new Builder<>()).putAll(entries).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> ImmutableListMultimap<K, V> fromMapEntries(Collection<? extends Map.Entry<? extends K, ? extends Collection<? extends V>>> mapEntries, @CheckForNull Comparator<? super V> valueComparator) {
/* 359 */     if (mapEntries.isEmpty()) {
/* 360 */       return of();
/*     */     }
/*     */     
/* 363 */     ImmutableMap.Builder<K, ImmutableList<V>> builder = new ImmutableMap.Builder<>(mapEntries.size());
/* 364 */     int size = 0;
/*     */     
/* 366 */     for (Map.Entry<? extends K, ? extends Collection<? extends V>> entry : mapEntries) {
/* 367 */       K key = entry.getKey();
/* 368 */       Collection<? extends V> values = entry.getValue();
/*     */ 
/*     */ 
/*     */       
/* 372 */       ImmutableList<V> list = (valueComparator == null) ? ImmutableList.<V>copyOf(values) : ImmutableList.<V>sortedCopyOf(valueComparator, values);
/* 373 */       if (!list.isEmpty()) {
/* 374 */         builder.put(key, list);
/* 375 */         size += list.size();
/*     */       } 
/*     */     } 
/*     */     
/* 379 */     return new ImmutableListMultimap<>(builder.buildOrThrow(), size);
/*     */   }
/*     */   
/*     */   ImmutableListMultimap(ImmutableMap<K, ImmutableList<V>> map, int size) {
/* 383 */     super((ImmutableMap)map, size);
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
/*     */   public ImmutableList<V> get(K key) {
/* 396 */     ImmutableList<V> list = (ImmutableList<V>)this.map.get(key);
/* 397 */     return (list == null) ? ImmutableList.<V>of() : list;
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
/*     */   public ImmutableListMultimap<V, K> inverse() {
/* 413 */     ImmutableListMultimap<V, K> result = this.inverse;
/* 414 */     return (result == null) ? (this.inverse = invert()) : result;
/*     */   }
/*     */   
/*     */   private ImmutableListMultimap<V, K> invert() {
/* 418 */     Builder<V, K> builder = builder();
/* 419 */     for (UnmodifiableIterator<Map.Entry<K, V>> unmodifiableIterator = entries().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<K, V> entry = unmodifiableIterator.next();
/* 420 */       builder.put(entry.getValue(), entry.getKey()); }
/*     */     
/* 422 */     ImmutableListMultimap<V, K> invertedMultimap = builder.build();
/* 423 */     invertedMultimap.inverse = this;
/* 424 */     return invertedMultimap;
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
/*     */   public final ImmutableList<V> removeAll(@CheckForNull Object key) {
/* 438 */     throw new UnsupportedOperationException();
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
/*     */   public final ImmutableList<V> replaceValues(K key, Iterable<? extends V> values) {
/* 452 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 462 */     stream.defaultWriteObject();
/* 463 */     Serialization.writeMultimap(this, stream);
/*     */   }
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*     */     ImmutableMap<Object, ImmutableList<Object>> tmpMap;
/* 469 */     stream.defaultReadObject();
/* 470 */     int keyCount = stream.readInt();
/* 471 */     if (keyCount < 0) {
/* 472 */       throw new InvalidObjectException("Invalid key count " + keyCount);
/*     */     }
/* 474 */     ImmutableMap.Builder<Object, ImmutableList<Object>> builder = ImmutableMap.builder();
/* 475 */     int tmpSize = 0;
/*     */     
/* 477 */     for (int i = 0; i < keyCount; i++) {
/* 478 */       Object key = Objects.requireNonNull(stream.readObject());
/* 479 */       int valueCount = stream.readInt();
/* 480 */       if (valueCount <= 0) {
/* 481 */         throw new InvalidObjectException("Invalid value count " + valueCount);
/*     */       }
/*     */       
/* 484 */       ImmutableList.Builder<Object> valuesBuilder = ImmutableList.builder();
/* 485 */       for (int j = 0; j < valueCount; j++) {
/* 486 */         valuesBuilder.add(Objects.requireNonNull(stream.readObject()));
/*     */       }
/* 488 */       builder.put(key, valuesBuilder.build());
/* 489 */       tmpSize += valueCount;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 494 */       tmpMap = builder.buildOrThrow();
/* 495 */     } catch (IllegalArgumentException e) {
/* 496 */       throw (InvalidObjectException)(new InvalidObjectException(e.getMessage())).initCause(e);
/*     */     } 
/*     */     
/* 499 */     ImmutableMultimap.FieldSettersHolder.MAP_FIELD_SETTER.set(this, tmpMap);
/* 500 */     ImmutableMultimap.FieldSettersHolder.SIZE_FIELD_SETTER.set(this, tmpSize);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ImmutableListMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */