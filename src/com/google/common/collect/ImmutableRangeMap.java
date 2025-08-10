/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.DoNotCall;
/*     */ import com.google.errorprone.annotations.DoNotMock;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.function.BiFunction;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtIncompatible
/*     */ public class ImmutableRangeMap<K extends Comparable<?>, V>
/*     */   implements RangeMap<K, V>, Serializable
/*     */ {
/*  53 */   private static final ImmutableRangeMap<Comparable<?>, Object> EMPTY = new ImmutableRangeMap(
/*  54 */       ImmutableList.of(), ImmutableList.of());
/*     */ 
/*     */   
/*     */   private final transient ImmutableList<Range<K>> ranges;
/*     */ 
/*     */   
/*     */   private final transient ImmutableList<V> values;
/*     */   
/*     */   private static final long serialVersionUID = 0L;
/*     */ 
/*     */   
/*     */   public static <T, K extends Comparable<? super K>, V> Collector<T, ?, ImmutableRangeMap<K, V>> toImmutableRangeMap(Function<? super T, Range<K>> keyFunction, Function<? super T, ? extends V> valueFunction) {
/*  66 */     return CollectCollectors.toImmutableRangeMap(keyFunction, valueFunction);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> of() {
/*  76 */     return (ImmutableRangeMap)EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> of(Range<K> range, V value) {
/*  81 */     return new ImmutableRangeMap<>(ImmutableList.of(range), ImmutableList.of(value));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> copyOf(RangeMap<K, ? extends V> rangeMap) {
/*  87 */     if (rangeMap instanceof ImmutableRangeMap) {
/*  88 */       return (ImmutableRangeMap)rangeMap;
/*     */     }
/*  90 */     Map<Range<K>, ? extends V> map = rangeMap.asMapOfRanges();
/*  91 */     ImmutableList.Builder<Range<K>> rangesBuilder = new ImmutableList.Builder<>(map.size());
/*  92 */     ImmutableList.Builder<V> valuesBuilder = new ImmutableList.Builder<>(map.size());
/*  93 */     for (Map.Entry<Range<K>, ? extends V> entry : map.entrySet()) {
/*  94 */       rangesBuilder.add(entry.getKey());
/*  95 */       valuesBuilder.add(entry.getValue());
/*     */     } 
/*  97 */     return new ImmutableRangeMap<>(rangesBuilder.build(), valuesBuilder.build());
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K extends Comparable<?>, V> Builder<K, V> builder() {
/* 102 */     return new Builder<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @DoNotMock
/*     */   public static final class Builder<K extends Comparable<?>, V>
/*     */   {
/* 115 */     private final List<Map.Entry<Range<K>, V>> entries = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(Range<K> range, V value) {
/* 125 */       Preconditions.checkNotNull(range);
/* 126 */       Preconditions.checkNotNull(value);
/* 127 */       Preconditions.checkArgument(!range.isEmpty(), "Range must not be empty, but was %s", range);
/* 128 */       this.entries.add(Maps.immutableEntry(range, value));
/* 129 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(RangeMap<K, ? extends V> rangeMap) {
/* 135 */       for (Map.Entry<Range<K>, ? extends V> entry : (Iterable<Map.Entry<Range<K>, ? extends V>>)rangeMap.asMapOfRanges().entrySet()) {
/* 136 */         put(entry.getKey(), entry.getValue());
/*     */       }
/* 138 */       return this;
/*     */     }
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<K, V> combine(Builder<K, V> builder) {
/* 143 */       this.entries.addAll(builder.entries);
/* 144 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableRangeMap<K, V> build() {
/* 154 */       Collections.sort(this.entries, Range.<Comparable<?>>rangeLexOrdering().onKeys());
/* 155 */       ImmutableList.Builder<Range<K>> rangesBuilder = new ImmutableList.Builder<>(this.entries.size());
/* 156 */       ImmutableList.Builder<V> valuesBuilder = new ImmutableList.Builder<>(this.entries.size());
/* 157 */       for (int i = 0; i < this.entries.size(); i++) {
/* 158 */         Range<K> range = (Range<K>)((Map.Entry)this.entries.get(i)).getKey();
/* 159 */         if (i > 0) {
/* 160 */           Range<K> prevRange = (Range<K>)((Map.Entry)this.entries.get(i - 1)).getKey();
/* 161 */           if (range.isConnected(prevRange) && !range.intersection(prevRange).isEmpty()) {
/* 162 */             throw new IllegalArgumentException("Overlapping ranges: range " + prevRange + " overlaps with entry " + range);
/*     */           }
/*     */         } 
/*     */         
/* 166 */         rangesBuilder.add(range);
/* 167 */         valuesBuilder.add((V)((Map.Entry)this.entries.get(i)).getValue());
/*     */       } 
/* 169 */       return new ImmutableRangeMap<>(rangesBuilder.build(), valuesBuilder.build());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableRangeMap(ImmutableList<Range<K>> ranges, ImmutableList<V> values) {
/* 177 */     this.ranges = ranges;
/* 178 */     this.values = values;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public V get(K key) {
/* 185 */     int index = SortedLists.binarySearch(this.ranges, 
/*     */         
/* 187 */         (Function)Range.lowerBoundFn(), 
/* 188 */         Cut.belowValue(key), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
/*     */ 
/*     */     
/* 191 */     if (index == -1) {
/* 192 */       return null;
/*     */     }
/* 194 */     Range<K> range = this.ranges.get(index);
/* 195 */     return range.contains(key) ? this.values.get(index) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Map.Entry<Range<K>, V> getEntry(K key) {
/* 203 */     int index = SortedLists.binarySearch(this.ranges, 
/*     */         
/* 205 */         (Function)Range.lowerBoundFn(), 
/* 206 */         Cut.belowValue(key), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
/*     */ 
/*     */     
/* 209 */     if (index == -1) {
/* 210 */       return null;
/*     */     }
/* 212 */     Range<K> range = this.ranges.get(index);
/* 213 */     return range.contains(key) ? Maps.<Range<K>, V>immutableEntry(range, this.values.get(index)) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Range<K> span() {
/* 219 */     if (this.ranges.isEmpty()) {
/* 220 */       throw new NoSuchElementException();
/*     */     }
/* 222 */     Range<K> firstRange = this.ranges.get(0);
/* 223 */     Range<K> lastRange = this.ranges.get(this.ranges.size() - 1);
/* 224 */     return Range.create(firstRange.lowerBound, lastRange.upperBound);
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
/*     */   @DoNotCall("Always throws UnsupportedOperationException")
/*     */   public final void put(Range<K> range, V value) {
/* 237 */     throw new UnsupportedOperationException();
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
/*     */   @DoNotCall("Always throws UnsupportedOperationException")
/*     */   public final void putCoalescing(Range<K> range, V value) {
/* 250 */     throw new UnsupportedOperationException();
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
/*     */   @DoNotCall("Always throws UnsupportedOperationException")
/*     */   public final void putAll(RangeMap<K, ? extends V> rangeMap) {
/* 263 */     throw new UnsupportedOperationException();
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
/*     */   @DoNotCall("Always throws UnsupportedOperationException")
/*     */   public final void clear() {
/* 276 */     throw new UnsupportedOperationException();
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
/*     */   @DoNotCall("Always throws UnsupportedOperationException")
/*     */   public final void remove(Range<K> range) {
/* 289 */     throw new UnsupportedOperationException();
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
/*     */   @DoNotCall("Always throws UnsupportedOperationException")
/*     */   public final void merge(Range<K> range, @CheckForNull V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
/* 305 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableMap<Range<K>, V> asMapOfRanges() {
/* 310 */     if (this.ranges.isEmpty()) {
/* 311 */       return ImmutableMap.of();
/*     */     }
/*     */     
/* 314 */     RegularImmutableSortedSet<Range<K>> rangeSet = new RegularImmutableSortedSet<>(this.ranges, (Comparator)Range.rangeLexOrdering());
/* 315 */     return new ImmutableSortedMap<>(rangeSet, this.values);
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableMap<Range<K>, V> asDescendingMapOfRanges() {
/* 320 */     if (this.ranges.isEmpty()) {
/* 321 */       return ImmutableMap.of();
/*     */     }
/*     */     
/* 324 */     RegularImmutableSortedSet<Range<K>> rangeSet = new RegularImmutableSortedSet<>(this.ranges.reverse(), Range.<Comparable<?>>rangeLexOrdering().reverse());
/* 325 */     return new ImmutableSortedMap<>(rangeSet, this.values.reverse());
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableRangeMap<K, V> subRangeMap(final Range<K> range) {
/* 330 */     if (((Range)Preconditions.checkNotNull(range)).isEmpty())
/* 331 */       return of(); 
/* 332 */     if (this.ranges.isEmpty() || range.encloses(span())) {
/* 333 */       return this;
/*     */     }
/*     */     
/* 336 */     int lowerIndex = SortedLists.binarySearch(this.ranges, 
/*     */         
/* 338 */         Range.upperBoundFn(), range.lowerBound, SortedLists.KeyPresentBehavior.FIRST_AFTER, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 343 */     int upperIndex = SortedLists.binarySearch(this.ranges, 
/*     */         
/* 345 */         Range.lowerBoundFn(), range.upperBound, SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
/*     */ 
/*     */ 
/*     */     
/* 349 */     if (lowerIndex >= upperIndex) {
/* 350 */       return of();
/*     */     }
/* 352 */     final int off = lowerIndex;
/* 353 */     final int len = upperIndex - lowerIndex;
/* 354 */     ImmutableList<Range<K>> subRanges = new ImmutableList<Range<K>>()
/*     */       {
/*     */         public int size()
/*     */         {
/* 358 */           return len;
/*     */         }
/*     */ 
/*     */         
/*     */         public Range<K> get(int index) {
/* 363 */           Preconditions.checkElementIndex(index, len);
/* 364 */           if (index == 0 || index == len - 1) {
/* 365 */             return ((Range<K>)ImmutableRangeMap.this.ranges.get(index + off)).intersection(range);
/*     */           }
/* 367 */           return ImmutableRangeMap.this.ranges.get(index + off);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         boolean isPartialView() {
/* 373 */           return true;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         @J2ktIncompatible
/*     */         Object writeReplace() {
/* 381 */           return super.writeReplace();
/*     */         }
/*     */       };
/* 384 */     final ImmutableRangeMap<K, V> outer = this;
/* 385 */     return new ImmutableRangeMap<K, V>(this, subRanges, this.values.subList(lowerIndex, upperIndex))
/*     */       {
/*     */         public ImmutableRangeMap<K, V> subRangeMap(Range<K> subRange) {
/* 388 */           if (range.isConnected(subRange)) {
/* 389 */             return outer.subRangeMap(subRange.intersection(range));
/*     */           }
/* 391 */           return ImmutableRangeMap.of();
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         @J2ktIncompatible
/*     */         Object writeReplace() {
/* 400 */           return super.writeReplace();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 407 */     return asMapOfRanges().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@CheckForNull Object o) {
/* 412 */     if (o instanceof RangeMap) {
/* 413 */       RangeMap<?, ?> rangeMap = (RangeMap<?, ?>)o;
/* 414 */       return asMapOfRanges().equals(rangeMap.asMapOfRanges());
/*     */     } 
/* 416 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 421 */     return asMapOfRanges().toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static class SerializedForm<K extends Comparable<?>, V>
/*     */     implements Serializable
/*     */   {
/*     */     private final ImmutableMap<Range<K>, V> mapOfRanges;
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SerializedForm(ImmutableMap<Range<K>, V> mapOfRanges) {
/* 433 */       this.mapOfRanges = mapOfRanges;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 437 */       if (this.mapOfRanges.isEmpty()) {
/* 438 */         return ImmutableRangeMap.of();
/*     */       }
/* 440 */       return createRangeMap();
/*     */     }
/*     */ 
/*     */     
/*     */     Object createRangeMap() {
/* 445 */       ImmutableRangeMap.Builder<K, V> builder = new ImmutableRangeMap.Builder<>();
/* 446 */       for (UnmodifiableIterator<Map.Entry<Range<K>, V>> unmodifiableIterator = this.mapOfRanges.entrySet().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<Range<K>, V> entry = unmodifiableIterator.next();
/* 447 */         builder.put(entry.getKey(), entry.getValue()); }
/*     */       
/* 449 */       return builder.build();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 456 */     return new SerializedForm<>(asMapOfRanges());
/*     */   }
/*     */   
/*     */   @J2ktIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 461 */     throw new InvalidObjectException("Use SerializedForm");
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ImmutableRangeMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */