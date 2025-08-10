/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.DoNotCall;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.BinaryOperator;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collector;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public abstract class ImmutableBiMap<K, V>
/*     */   extends ImmutableMap<K, V>
/*     */   implements BiMap<K, V>
/*     */ {
/*     */   private static final long serialVersionUID = -889275714L;
/*     */   
/*     */   public static <T, K, V> Collector<T, ?, ImmutableBiMap<K, V>> toImmutableBiMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/*  67 */     return CollectCollectors.toImmutableBiMap(keyFunction, valueFunction);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> of() {
/*  78 */     return (ImmutableBiMap)RegularImmutableBiMap.EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1) {
/*  83 */     return new SingletonImmutableBiMap<>(k1, v1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2) {
/*  92 */     return RegularImmutableBiMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
/* 101 */     return RegularImmutableBiMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
/* 110 */     return RegularImmutableBiMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] {
/* 111 */           entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4)
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
/* 121 */     return RegularImmutableBiMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] {
/* 122 */           entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5)
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
/* 133 */     return RegularImmutableBiMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] {
/* 134 */           entryOf(k1, v1), 
/* 135 */           entryOf(k2, v2), 
/* 136 */           entryOf(k3, v3), 
/* 137 */           entryOf(k4, v4), 
/* 138 */           entryOf(k5, v5), 
/* 139 */           entryOf(k6, v6)
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
/* 150 */     return RegularImmutableBiMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] {
/* 151 */           entryOf(k1, v1), 
/* 152 */           entryOf(k2, v2), 
/* 153 */           entryOf(k3, v3), 
/* 154 */           entryOf(k4, v4), 
/* 155 */           entryOf(k5, v5), 
/* 156 */           entryOf(k6, v6), 
/* 157 */           entryOf(k7, v7)
/*     */         });
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
/*     */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
/* 183 */     return RegularImmutableBiMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] {
/* 184 */           entryOf(k1, v1), 
/* 185 */           entryOf(k2, v2), 
/* 186 */           entryOf(k3, v3), 
/* 187 */           entryOf(k4, v4), 
/* 188 */           entryOf(k5, v5), 
/* 189 */           entryOf(k6, v6), 
/* 190 */           entryOf(k7, v7), 
/* 191 */           entryOf(k8, v8)
/*     */         });
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
/*     */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
/* 219 */     return RegularImmutableBiMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] {
/* 220 */           entryOf(k1, v1), 
/* 221 */           entryOf(k2, v2), 
/* 222 */           entryOf(k3, v3), 
/* 223 */           entryOf(k4, v4), 
/* 224 */           entryOf(k5, v5), 
/* 225 */           entryOf(k6, v6), 
/* 226 */           entryOf(k7, v7), 
/* 227 */           entryOf(k8, v8), 
/* 228 */           entryOf(k9, v9)
/*     */         });
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
/*     */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
/* 257 */     return RegularImmutableBiMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] {
/* 258 */           entryOf(k1, v1), 
/* 259 */           entryOf(k2, v2), 
/* 260 */           entryOf(k3, v3), 
/* 261 */           entryOf(k4, v4), 
/* 262 */           entryOf(k5, v5), 
/* 263 */           entryOf(k6, v6), 
/* 264 */           entryOf(k7, v7), 
/* 265 */           entryOf(k8, v8), 
/* 266 */           entryOf(k9, v9), 
/* 267 */           entryOf(k10, v10)
/*     */         });
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
/*     */   @SafeVarargs
/*     */   public static <K, V> ImmutableBiMap<K, V> ofEntries(Map.Entry<? extends K, ? extends V>... entries) {
/* 281 */     Map.Entry<? extends K, ? extends V>[] arrayOfEntry = entries;
/* 282 */     return RegularImmutableBiMap.fromEntries((Map.Entry[])arrayOfEntry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> Builder<K, V> builder() {
/* 290 */     return new Builder<>();
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
/*     */   public static <K, V> Builder<K, V> builderWithExpectedSize(int expectedSize) {
/* 306 */     CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
/* 307 */     return new Builder<>(expectedSize);
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
/*     */   public static final class Builder<K, V>
/*     */     extends ImmutableMap.Builder<K, V>
/*     */   {
/*     */     public Builder() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Builder(int size) {
/* 347 */       super(size);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(K key, V value) {
/* 357 */       super.put(key, value);
/* 358 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry) {
/* 370 */       super.put(entry);
/* 371 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
/* 383 */       super.putAll(map);
/* 384 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/* 397 */       super.putAll(entries);
/* 398 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> orderEntriesByValue(Comparator<? super V> valueComparator) {
/* 414 */       super.orderEntriesByValue(valueComparator);
/* 415 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<K, V> combine(ImmutableMap.Builder<K, V> builder) {
/* 421 */       super.combine(builder);
/* 422 */       return this;
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
/*     */     public ImmutableBiMap<K, V> build() {
/* 438 */       return buildOrThrow();
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
/*     */     public ImmutableBiMap<K, V> buildOrThrow() {
/*     */       Map.Entry<K, V> onlyEntry;
/* 452 */       switch (this.size) {
/*     */         case 0:
/* 454 */           return ImmutableBiMap.of();
/*     */         
/*     */         case 1:
/* 457 */           onlyEntry = Objects.<Map.Entry<K, V>>requireNonNull(this.entries[0]);
/* 458 */           return ImmutableBiMap.of(onlyEntry.getKey(), onlyEntry.getValue());
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 467 */       if (this.valueComparator != null) {
/* 468 */         if (this.entriesUsed) {
/* 469 */           this.entries = Arrays.<Map.Entry<K, V>>copyOf(this.entries, this.size);
/*     */         }
/* 471 */         Arrays.sort(this.entries, 0, this.size, 
/*     */ 
/*     */ 
/*     */             
/* 475 */             Ordering.<V>from(this.valueComparator).onResultOf(Maps.valueFunction()));
/*     */       } 
/* 477 */       this.entriesUsed = true;
/* 478 */       return RegularImmutableBiMap.fromEntryArray(this.size, this.entries);
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
/*     */     @Deprecated
/*     */     @DoNotCall
/*     */     public ImmutableBiMap<K, V> buildKeepingLast() {
/* 494 */       throw new UnsupportedOperationException("Not supported for bimaps");
/*     */     }
/*     */     
/*     */     @VisibleForTesting
/*     */     ImmutableBiMap<K, V> buildJdkBacked() {
/*     */       Map.Entry<K, V> onlyEntry;
/* 500 */       Preconditions.checkState((this.valueComparator == null), "buildJdkBacked is for tests only, doesn't support orderEntriesByValue");
/*     */ 
/*     */       
/* 503 */       switch (this.size) {
/*     */         case 0:
/* 505 */           return ImmutableBiMap.of();
/*     */         
/*     */         case 1:
/* 508 */           onlyEntry = Objects.<Map.Entry<K, V>>requireNonNull(this.entries[0]);
/* 509 */           return ImmutableBiMap.of(onlyEntry.getKey(), onlyEntry.getValue());
/*     */       } 
/* 511 */       this.entriesUsed = true;
/* 512 */       return RegularImmutableBiMap.fromEntryArray(this.size, this.entries);
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
/*     */   public static <K, V> ImmutableBiMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
/* 534 */     if (map instanceof ImmutableBiMap) {
/*     */       
/* 536 */       ImmutableBiMap<K, V> bimap = (ImmutableBiMap)map;
/*     */ 
/*     */       
/* 539 */       if (!bimap.isPartialView()) {
/* 540 */         return bimap;
/*     */       }
/*     */     } 
/* 543 */     return copyOf(map.entrySet());
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
/*     */   public static <K, V> ImmutableBiMap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/*     */     Map.Entry<K, V> entry;
/* 558 */     Map.Entry[] arrayOfEntry = Iterables.<Map.Entry>toArray((Iterable)entries, (Map.Entry[])EMPTY_ENTRY_ARRAY);
/* 559 */     switch (arrayOfEntry.length) {
/*     */       case 0:
/* 561 */         return of();
/*     */       case 1:
/* 563 */         entry = arrayOfEntry[0];
/* 564 */         return of(entry.getKey(), entry.getValue());
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 570 */     return RegularImmutableBiMap.fromEntries((Map.Entry<K, V>[])arrayOfEntry);
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
/*     */   public ImmutableSet<V> values() {
/* 590 */     return inverse().keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   final ImmutableSet<V> createValues() {
/* 595 */     throw new AssertionError("should never be called");
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
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   @DoNotCall("Always throws UnsupportedOperationException")
/*     */   public final V forcePut(K key, V value) {
/* 610 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   private static class SerializedForm<K, V>
/*     */     extends ImmutableMap.SerializedForm<K, V>
/*     */   {
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */ 
/*     */     
/*     */     SerializedForm(ImmutableBiMap<K, V> bimap) {
/* 624 */       super(bimap);
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableBiMap.Builder<K, V> makeBuilder(int size) {
/* 629 */       return new ImmutableBiMap.Builder<>(size);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   Object writeReplace() {
/* 638 */     return new SerializedForm<>(this);
/*     */   }
/*     */   
/*     */   @J2ktIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 643 */     throw new InvalidObjectException("Use SerializedForm");
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
/*     */   @Deprecated
/*     */   @DoNotCall("Use toImmutableBiMap")
/*     */   public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/* 659 */     throw new UnsupportedOperationException();
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
/*     */   @Deprecated
/*     */   @DoNotCall("Use toImmutableBiMap")
/*     */   public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction) {
/* 677 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public abstract ImmutableBiMap<V, K> inverse();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ImmutableBiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */