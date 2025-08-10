/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NavigableMap;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class TreeRangeMap<K extends Comparable, V>
/*     */   implements RangeMap<K, V>
/*     */ {
/*     */   private final NavigableMap<Cut<K>, RangeMapEntry<K, V>> entriesByLowerBound;
/*     */   
/*     */   public static <K extends Comparable, V> TreeRangeMap<K, V> create() {
/*  60 */     return new TreeRangeMap<>();
/*     */   }
/*     */   
/*     */   private TreeRangeMap() {
/*  64 */     this.entriesByLowerBound = Maps.newTreeMap();
/*     */   }
/*     */   
/*     */   private static final class RangeMapEntry<K extends Comparable, V>
/*     */     extends AbstractMapEntry<Range<K>, V> {
/*     */     private final Range<K> range;
/*     */     private final V value;
/*     */     
/*     */     RangeMapEntry(Cut<K> lowerBound, Cut<K> upperBound, V value) {
/*  73 */       this(Range.create(lowerBound, upperBound), value);
/*     */     }
/*     */     
/*     */     RangeMapEntry(Range<K> range, V value) {
/*  77 */       this.range = range;
/*  78 */       this.value = value;
/*     */     }
/*     */ 
/*     */     
/*     */     public Range<K> getKey() {
/*  83 */       return this.range;
/*     */     }
/*     */ 
/*     */     
/*     */     public V getValue() {
/*  88 */       return this.value;
/*     */     }
/*     */     
/*     */     public boolean contains(K value) {
/*  92 */       return this.range.contains(value);
/*     */     }
/*     */     
/*     */     Cut<K> getLowerBound() {
/*  96 */       return this.range.lowerBound;
/*     */     }
/*     */     
/*     */     Cut<K> getUpperBound() {
/* 100 */       return this.range.upperBound;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public V get(K key) {
/* 107 */     Map.Entry<Range<K>, V> entry = getEntry(key);
/* 108 */     return (entry == null) ? null : entry.getValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Map.Entry<Range<K>, V> getEntry(K key) {
/* 115 */     Map.Entry<Cut<K>, RangeMapEntry<K, V>> mapEntry = this.entriesByLowerBound.floorEntry(Cut.belowValue(key));
/* 116 */     if (mapEntry != null && ((RangeMapEntry)mapEntry.getValue()).contains(key)) {
/* 117 */       return mapEntry.getValue();
/*     */     }
/* 119 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(Range<K> range, V value) {
/* 125 */     if (!range.isEmpty()) {
/* 126 */       Preconditions.checkNotNull(value);
/* 127 */       remove(range);
/* 128 */       this.entriesByLowerBound.put(range.lowerBound, new RangeMapEntry<>(range, value));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void putCoalescing(Range<K> range, V value) {
/* 135 */     if (this.entriesByLowerBound.isEmpty()) {
/* 136 */       put(range, value);
/*     */       
/*     */       return;
/*     */     } 
/* 140 */     Range<K> coalescedRange = coalescedRange(range, (V)Preconditions.checkNotNull(value));
/* 141 */     put(coalescedRange, value);
/*     */   }
/*     */ 
/*     */   
/*     */   private Range<K> coalescedRange(Range<K> range, V value) {
/* 146 */     Range<K> coalescedRange = range;
/*     */     
/* 148 */     Map.Entry<Cut<K>, RangeMapEntry<K, V>> lowerEntry = this.entriesByLowerBound.lowerEntry(range.lowerBound);
/* 149 */     coalescedRange = coalesce(coalescedRange, value, lowerEntry);
/*     */ 
/*     */     
/* 152 */     Map.Entry<Cut<K>, RangeMapEntry<K, V>> higherEntry = this.entriesByLowerBound.floorEntry(range.upperBound);
/* 153 */     coalescedRange = coalesce(coalescedRange, value, higherEntry);
/*     */     
/* 155 */     return coalescedRange;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <K extends Comparable, V> Range<K> coalesce(Range<K> range, V value, @CheckForNull Map.Entry<Cut<K>, RangeMapEntry<K, V>> entry) {
/* 161 */     if (entry != null && ((RangeMapEntry)entry
/* 162 */       .getValue()).getKey().isConnected(range) && ((RangeMapEntry)entry
/* 163 */       .getValue()).getValue().equals(value)) {
/* 164 */       return range.span(((RangeMapEntry)entry.getValue()).getKey());
/*     */     }
/* 166 */     return range;
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(RangeMap<K, ? extends V> rangeMap) {
/* 171 */     for (Map.Entry<Range<K>, ? extends V> entry : (Iterable<Map.Entry<Range<K>, ? extends V>>)rangeMap.asMapOfRanges().entrySet()) {
/* 172 */       put(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 178 */     this.entriesByLowerBound.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public Range<K> span() {
/* 183 */     Map.Entry<Cut<K>, RangeMapEntry<K, V>> firstEntry = this.entriesByLowerBound.firstEntry();
/* 184 */     Map.Entry<Cut<K>, RangeMapEntry<K, V>> lastEntry = this.entriesByLowerBound.lastEntry();
/*     */     
/* 186 */     if (firstEntry == null || lastEntry == null) {
/* 187 */       throw new NoSuchElementException();
/*     */     }
/* 189 */     return Range.create(
/* 190 */         (((RangeMapEntry)firstEntry.getValue()).getKey()).lowerBound, (((RangeMapEntry)lastEntry.getValue()).getKey()).upperBound);
/*     */   }
/*     */   
/*     */   private void putRangeMapEntry(Cut<K> lowerBound, Cut<K> upperBound, V value) {
/* 194 */     this.entriesByLowerBound.put(lowerBound, new RangeMapEntry<>(lowerBound, upperBound, value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Range<K> rangeToRemove) {
/* 199 */     if (rangeToRemove.isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 208 */     Map.Entry<Cut<K>, RangeMapEntry<K, V>> mapEntryBelowToTruncate = this.entriesByLowerBound.lowerEntry(rangeToRemove.lowerBound);
/* 209 */     if (mapEntryBelowToTruncate != null) {
/*     */       
/* 211 */       RangeMapEntry<K, V> rangeMapEntry = mapEntryBelowToTruncate.getValue();
/* 212 */       if (rangeMapEntry.getUpperBound().compareTo(rangeToRemove.lowerBound) > 0) {
/*     */         
/* 214 */         if (rangeMapEntry.getUpperBound().compareTo(rangeToRemove.upperBound) > 0)
/*     */         {
/*     */           
/* 217 */           putRangeMapEntry(rangeToRemove.upperBound, rangeMapEntry
/*     */               
/* 219 */               .getUpperBound(), (V)((RangeMapEntry)mapEntryBelowToTruncate
/* 220 */               .getValue()).getValue());
/*     */         }
/*     */         
/* 223 */         putRangeMapEntry(rangeMapEntry
/* 224 */             .getLowerBound(), rangeToRemove.lowerBound, (V)((RangeMapEntry)mapEntryBelowToTruncate
/*     */             
/* 226 */             .getValue()).getValue());
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 231 */     Map.Entry<Cut<K>, RangeMapEntry<K, V>> mapEntryAboveToTruncate = this.entriesByLowerBound.lowerEntry(rangeToRemove.upperBound);
/* 232 */     if (mapEntryAboveToTruncate != null) {
/*     */       
/* 234 */       RangeMapEntry<K, V> rangeMapEntry = mapEntryAboveToTruncate.getValue();
/* 235 */       if (rangeMapEntry.getUpperBound().compareTo(rangeToRemove.upperBound) > 0)
/*     */       {
/*     */         
/* 238 */         putRangeMapEntry(rangeToRemove.upperBound, rangeMapEntry
/*     */             
/* 240 */             .getUpperBound(), (V)((RangeMapEntry)mapEntryAboveToTruncate
/* 241 */             .getValue()).getValue());
/*     */       }
/*     */     } 
/* 244 */     this.entriesByLowerBound.subMap(rangeToRemove.lowerBound, rangeToRemove.upperBound).clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void split(Cut<K> cut) {
/* 252 */     Map.Entry<Cut<K>, RangeMapEntry<K, V>> mapEntryToSplit = this.entriesByLowerBound.lowerEntry(cut);
/* 253 */     if (mapEntryToSplit == null) {
/*     */       return;
/*     */     }
/*     */     
/* 257 */     RangeMapEntry<K, V> rangeMapEntry = mapEntryToSplit.getValue();
/* 258 */     if (rangeMapEntry.getUpperBound().compareTo(cut) <= 0) {
/*     */       return;
/*     */     }
/*     */     
/* 262 */     putRangeMapEntry(rangeMapEntry.getLowerBound(), cut, rangeMapEntry.getValue());
/* 263 */     putRangeMapEntry(cut, rangeMapEntry.getUpperBound(), rangeMapEntry.getValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void merge(Range<K> range, @CheckForNull V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
/* 271 */     Preconditions.checkNotNull(range);
/* 272 */     Preconditions.checkNotNull(remappingFunction);
/*     */     
/* 274 */     if (range.isEmpty()) {
/*     */       return;
/*     */     }
/* 277 */     split(range.lowerBound);
/* 278 */     split(range.upperBound);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 283 */     Set<Map.Entry<Cut<K>, RangeMapEntry<K, V>>> entriesInMergeRange = this.entriesByLowerBound.subMap(range.lowerBound, range.upperBound).entrySet();
/*     */ 
/*     */     
/* 286 */     ImmutableMap.Builder<Cut<K>, RangeMapEntry<K, V>> gaps = ImmutableMap.builder();
/* 287 */     if (value != null) {
/*     */       
/* 289 */       Iterator<Map.Entry<Cut<K>, RangeMapEntry<K, V>>> iterator = entriesInMergeRange.iterator();
/* 290 */       Cut<K> lowerBound = range.lowerBound;
/* 291 */       while (iterator.hasNext()) {
/* 292 */         RangeMapEntry<K, V> entry = (RangeMapEntry<K, V>)((Map.Entry)iterator.next()).getValue();
/* 293 */         Cut<K> upperBound = entry.getLowerBound();
/* 294 */         if (!lowerBound.equals(upperBound)) {
/* 295 */           gaps.put(lowerBound, new RangeMapEntry<>(lowerBound, upperBound, value));
/*     */         }
/* 297 */         lowerBound = entry.getUpperBound();
/*     */       } 
/* 299 */       if (!lowerBound.equals(range.upperBound)) {
/* 300 */         gaps.put(lowerBound, new RangeMapEntry<>(lowerBound, range.upperBound, value));
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 305 */     Iterator<Map.Entry<Cut<K>, RangeMapEntry<K, V>>> backingItr = entriesInMergeRange.iterator();
/* 306 */     while (backingItr.hasNext()) {
/* 307 */       Map.Entry<Cut<K>, RangeMapEntry<K, V>> entry = backingItr.next();
/* 308 */       V newValue = remappingFunction.apply((V)((RangeMapEntry)entry.getValue()).getValue(), value);
/* 309 */       if (newValue == null) {
/* 310 */         backingItr.remove(); continue;
/*     */       } 
/* 312 */       entry.setValue(new RangeMapEntry<>(((RangeMapEntry)entry
/*     */             
/* 314 */             .getValue()).getLowerBound(), ((RangeMapEntry)entry.getValue()).getUpperBound(), newValue));
/*     */     } 
/*     */ 
/*     */     
/* 318 */     this.entriesByLowerBound.putAll(gaps.build());
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<Range<K>, V> asMapOfRanges() {
/* 323 */     return new AsMapOfRanges(this.entriesByLowerBound.values());
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<Range<K>, V> asDescendingMapOfRanges() {
/* 328 */     return new AsMapOfRanges(this.entriesByLowerBound.descendingMap().values());
/*     */   }
/*     */   
/*     */   private final class AsMapOfRanges
/*     */     extends Maps.IteratorBasedAbstractMap<Range<K>, V>
/*     */   {
/*     */     final Iterable<Map.Entry<Range<K>, V>> entryIterable;
/*     */     
/*     */     AsMapOfRanges(Iterable<TreeRangeMap.RangeMapEntry<K, V>> entryIterable) {
/* 337 */       this.entryIterable = (Iterable)entryIterable;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(@CheckForNull Object key) {
/* 342 */       return (get(key) != null);
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public V get(@CheckForNull Object key) {
/* 348 */       if (key instanceof Range) {
/* 349 */         Range<?> range = (Range)key;
/* 350 */         TreeRangeMap.RangeMapEntry<K, V> rangeMapEntry = (TreeRangeMap.RangeMapEntry<K, V>)TreeRangeMap.this.entriesByLowerBound.get(range.lowerBound);
/* 351 */         if (rangeMapEntry != null && rangeMapEntry.getKey().equals(range)) {
/* 352 */           return rangeMapEntry.getValue();
/*     */         }
/*     */       } 
/* 355 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 360 */       return TreeRangeMap.this.entriesByLowerBound.size();
/*     */     }
/*     */ 
/*     */     
/*     */     Iterator<Map.Entry<Range<K>, V>> entryIterator() {
/* 365 */       return this.entryIterable.iterator();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public RangeMap<K, V> subRangeMap(Range<K> subRange) {
/* 371 */     if (subRange.equals(Range.all())) {
/* 372 */       return this;
/*     */     }
/* 374 */     return new SubRangeMap(subRange);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private RangeMap<K, V> emptySubRangeMap() {
/* 380 */     return (RangeMap)EMPTY_SUB_RANGE_MAP;
/*     */   }
/*     */ 
/*     */   
/* 384 */   private static final RangeMap<Comparable<?>, Object> EMPTY_SUB_RANGE_MAP = new RangeMap<Comparable<?>, Object>()
/*     */     {
/*     */       @CheckForNull
/*     */       public Object get(Comparable<?> key)
/*     */       {
/* 389 */         return null;
/*     */       }
/*     */ 
/*     */       
/*     */       @CheckForNull
/*     */       public Map.Entry<Range<Comparable<?>>, Object> getEntry(Comparable<?> key) {
/* 395 */         return null;
/*     */       }
/*     */ 
/*     */       
/*     */       public Range<Comparable<?>> span() {
/* 400 */         throw new NoSuchElementException();
/*     */       }
/*     */ 
/*     */       
/*     */       public void put(Range<Comparable<?>> range, Object value) {
/* 405 */         Preconditions.checkNotNull(range);
/* 406 */         throw new IllegalArgumentException("Cannot insert range " + range + " into an empty subRangeMap");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void putCoalescing(Range<Comparable<?>> range, Object value) {
/* 412 */         Preconditions.checkNotNull(range);
/* 413 */         throw new IllegalArgumentException("Cannot insert range " + range + " into an empty subRangeMap");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void putAll(RangeMap<Comparable<?>, ? extends Object> rangeMap) {
/* 419 */         if (!rangeMap.asMapOfRanges().isEmpty()) {
/* 420 */           throw new IllegalArgumentException("Cannot putAll(nonEmptyRangeMap) into an empty subRangeMap");
/*     */         }
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void clear() {}
/*     */ 
/*     */       
/*     */       public void remove(Range<Comparable<?>> range) {
/* 430 */         Preconditions.checkNotNull(range);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void merge(Range<Comparable<?>> range, @CheckForNull Object value, BiFunction<? super Object, ? super Object, ? extends Object> remappingFunction) {
/* 439 */         Preconditions.checkNotNull(range);
/* 440 */         throw new IllegalArgumentException("Cannot merge range " + range + " into an empty subRangeMap");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public Map<Range<Comparable<?>>, Object> asMapOfRanges() {
/* 446 */         return Collections.emptyMap();
/*     */       }
/*     */ 
/*     */       
/*     */       public Map<Range<Comparable<?>>, Object> asDescendingMapOfRanges() {
/* 451 */         return Collections.emptyMap();
/*     */       }
/*     */ 
/*     */       
/*     */       public RangeMap<Comparable<?>, Object> subRangeMap(Range<Comparable<?>> range) {
/* 456 */         Preconditions.checkNotNull(range);
/* 457 */         return this;
/*     */       }
/*     */     };
/*     */   
/*     */   private class SubRangeMap
/*     */     implements RangeMap<K, V> {
/*     */     private final Range<K> subRange;
/*     */     
/*     */     SubRangeMap(Range<K> subRange) {
/* 466 */       this.subRange = subRange;
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public V get(K key) {
/* 472 */       return this.subRange.contains(key) ? TreeRangeMap.this.get(key) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public Map.Entry<Range<K>, V> getEntry(K key) {
/* 478 */       if (this.subRange.contains(key)) {
/* 479 */         Map.Entry<Range<K>, V> entry = TreeRangeMap.this.getEntry(key);
/* 480 */         if (entry != null) {
/* 481 */           return Maps.immutableEntry(((Range<K>)entry.getKey()).intersection(this.subRange), entry.getValue());
/*     */         }
/*     */       } 
/* 484 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Range<K> span() {
/*     */       Cut<K> lowerBound, upperBound;
/* 491 */       Map.Entry<Cut<K>, TreeRangeMap.RangeMapEntry<K, V>> lowerEntry = TreeRangeMap.this.entriesByLowerBound.floorEntry(this.subRange.lowerBound);
/* 492 */       if (lowerEntry != null && ((TreeRangeMap.RangeMapEntry)lowerEntry
/* 493 */         .getValue()).getUpperBound().compareTo(this.subRange.lowerBound) > 0) {
/* 494 */         lowerBound = this.subRange.lowerBound;
/*     */       } else {
/* 496 */         lowerBound = (Cut<K>)TreeRangeMap.this.entriesByLowerBound.ceilingKey(this.subRange.lowerBound);
/* 497 */         if (lowerBound == null || lowerBound.compareTo(this.subRange.upperBound) >= 0) {
/* 498 */           throw new NoSuchElementException();
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 504 */       Map.Entry<Cut<K>, TreeRangeMap.RangeMapEntry<K, V>> upperEntry = TreeRangeMap.this.entriesByLowerBound.lowerEntry(this.subRange.upperBound);
/* 505 */       if (upperEntry == null)
/* 506 */         throw new NoSuchElementException(); 
/* 507 */       if (((TreeRangeMap.RangeMapEntry)upperEntry.getValue()).getUpperBound().compareTo(this.subRange.upperBound) >= 0) {
/* 508 */         upperBound = this.subRange.upperBound;
/*     */       } else {
/* 510 */         upperBound = ((TreeRangeMap.RangeMapEntry)upperEntry.getValue()).getUpperBound();
/*     */       } 
/* 512 */       return Range.create(lowerBound, upperBound);
/*     */     }
/*     */ 
/*     */     
/*     */     public void put(Range<K> range, V value) {
/* 517 */       Preconditions.checkArgument(this.subRange
/* 518 */           .encloses(range), "Cannot put range %s into a subRangeMap(%s)", range, this.subRange);
/* 519 */       TreeRangeMap.this.put(range, value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void putCoalescing(Range<K> range, V value) {
/* 524 */       if (TreeRangeMap.this.entriesByLowerBound.isEmpty() || !this.subRange.encloses(range)) {
/* 525 */         put(range, value);
/*     */         
/*     */         return;
/*     */       } 
/* 529 */       Range<K> coalescedRange = TreeRangeMap.this.coalescedRange(range, (V)Preconditions.checkNotNull(value));
/*     */       
/* 531 */       put(coalescedRange.intersection(this.subRange), value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void putAll(RangeMap<K, ? extends V> rangeMap) {
/* 536 */       if (rangeMap.asMapOfRanges().isEmpty()) {
/*     */         return;
/*     */       }
/* 539 */       Range<K> span = rangeMap.span();
/* 540 */       Preconditions.checkArgument(this.subRange
/* 541 */           .encloses(span), "Cannot putAll rangeMap with span %s into a subRangeMap(%s)", span, this.subRange);
/*     */ 
/*     */ 
/*     */       
/* 545 */       TreeRangeMap.this.putAll(rangeMap);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 550 */       TreeRangeMap.this.remove(this.subRange);
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove(Range<K> range) {
/* 555 */       if (range.isConnected(this.subRange)) {
/* 556 */         TreeRangeMap.this.remove(range.intersection(this.subRange));
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void merge(Range<K> range, @CheckForNull V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
/* 565 */       Preconditions.checkArgument(this.subRange
/* 566 */           .encloses(range), "Cannot merge range %s into a subRangeMap(%s)", range, this.subRange);
/*     */ 
/*     */ 
/*     */       
/* 570 */       TreeRangeMap.this.merge(range, value, remappingFunction);
/*     */     }
/*     */ 
/*     */     
/*     */     public RangeMap<K, V> subRangeMap(Range<K> range) {
/* 575 */       if (!range.isConnected(this.subRange)) {
/* 576 */         return TreeRangeMap.this.emptySubRangeMap();
/*     */       }
/* 578 */       return TreeRangeMap.this.subRangeMap(range.intersection(this.subRange));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<Range<K>, V> asMapOfRanges() {
/* 584 */       return new SubRangeMapAsMap();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<Range<K>, V> asDescendingMapOfRanges() {
/* 589 */       return new SubRangeMapAsMap()
/*     */         {
/*     */           Iterator<Map.Entry<Range<K>, V>> entryIterator()
/*     */           {
/* 593 */             if (TreeRangeMap.SubRangeMap.this.subRange.isEmpty()) {
/* 594 */               return Iterators.emptyIterator();
/*     */             }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 601 */             final Iterator<TreeRangeMap.RangeMapEntry<K, V>> backingItr = TreeRangeMap.this.entriesByLowerBound.headMap(TreeRangeMap.SubRangeMap.this.subRange.upperBound, false).descendingMap().values().iterator();
/* 602 */             return new AbstractIterator<Map.Entry<Range<K>, V>>()
/*     */               {
/*     */                 @CheckForNull
/*     */                 protected Map.Entry<Range<K>, V> computeNext()
/*     */                 {
/* 607 */                   if (backingItr.hasNext()) {
/* 608 */                     TreeRangeMap.RangeMapEntry<K, V> entry = backingItr.next();
/* 609 */                     if (entry.getUpperBound().compareTo(TreeRangeMap.SubRangeMap.this.subRange.lowerBound) <= 0) {
/* 610 */                       return endOfData();
/*     */                     }
/* 612 */                     return Maps.immutableEntry(entry.getKey().intersection(TreeRangeMap.SubRangeMap.this.subRange), entry.getValue());
/*     */                   } 
/* 614 */                   return endOfData();
/*     */                 }
/*     */               };
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object o) {
/* 623 */       if (o instanceof RangeMap) {
/* 624 */         RangeMap<?, ?> rangeMap = (RangeMap<?, ?>)o;
/* 625 */         return asMapOfRanges().equals(rangeMap.asMapOfRanges());
/*     */       } 
/* 627 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 632 */       return asMapOfRanges().hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 637 */       return asMapOfRanges().toString();
/*     */     }
/*     */     
/*     */     class SubRangeMapAsMap
/*     */       extends AbstractMap<Range<K>, V>
/*     */     {
/*     */       public boolean containsKey(@CheckForNull Object key) {
/* 644 */         return (get(key) != null);
/*     */       }
/*     */ 
/*     */       
/*     */       @CheckForNull
/*     */       public V get(@CheckForNull Object key) {
/*     */         try {
/* 651 */           if (key instanceof Range) {
/*     */             
/* 653 */             Range<K> r = (Range<K>)key;
/* 654 */             if (!TreeRangeMap.SubRangeMap.this.subRange.encloses(r) || r.isEmpty()) {
/* 655 */               return null;
/*     */             }
/* 657 */             TreeRangeMap.RangeMapEntry<K, V> candidate = null;
/* 658 */             if (r.lowerBound.compareTo(TreeRangeMap.SubRangeMap.this.subRange.lowerBound) == 0) {
/*     */ 
/*     */               
/* 661 */               Map.Entry<Cut<K>, TreeRangeMap.RangeMapEntry<K, V>> entry = TreeRangeMap.this.entriesByLowerBound.floorEntry(r.lowerBound);
/* 662 */               if (entry != null) {
/* 663 */                 candidate = entry.getValue();
/*     */               }
/*     */             } else {
/* 666 */               candidate = (TreeRangeMap.RangeMapEntry<K, V>)TreeRangeMap.this.entriesByLowerBound.get(r.lowerBound);
/*     */             } 
/*     */             
/* 669 */             if (candidate != null && candidate
/* 670 */               .getKey().isConnected(TreeRangeMap.SubRangeMap.this.subRange) && candidate
/* 671 */               .getKey().intersection(TreeRangeMap.SubRangeMap.this.subRange).equals(r)) {
/* 672 */               return candidate.getValue();
/*     */             }
/*     */           } 
/* 675 */         } catch (ClassCastException e) {
/* 676 */           return null;
/*     */         } 
/* 678 */         return null;
/*     */       }
/*     */ 
/*     */       
/*     */       @CheckForNull
/*     */       public V remove(@CheckForNull Object key) {
/* 684 */         V value = get(key);
/* 685 */         if (value != null) {
/*     */ 
/*     */           
/* 688 */           Range<K> range = (Range<K>)Objects.<Object>requireNonNull(key);
/* 689 */           TreeRangeMap.this.remove(range);
/* 690 */           return value;
/*     */         } 
/* 692 */         return null;
/*     */       }
/*     */ 
/*     */       
/*     */       public void clear() {
/* 697 */         TreeRangeMap.SubRangeMap.this.clear();
/*     */       }
/*     */       
/*     */       private boolean removeEntryIf(Predicate<? super Map.Entry<Range<K>, V>> predicate) {
/* 701 */         List<Range<K>> toRemove = Lists.newArrayList();
/* 702 */         for (Map.Entry<Range<K>, V> entry : entrySet()) {
/* 703 */           if (predicate.apply(entry)) {
/* 704 */             toRemove.add(entry.getKey());
/*     */           }
/*     */         } 
/* 707 */         for (Range<K> range : toRemove) {
/* 708 */           TreeRangeMap.this.remove(range);
/*     */         }
/* 710 */         return !toRemove.isEmpty();
/*     */       }
/*     */ 
/*     */       
/*     */       public Set<Range<K>> keySet() {
/* 715 */         return (Set)new Maps.KeySet<Range<Range<K>>, V>(this)
/*     */           {
/*     */             public boolean remove(@CheckForNull Object o) {
/* 718 */               return (TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this.remove(o) != null);
/*     */             }
/*     */ 
/*     */             
/*     */             public boolean retainAll(Collection<?> c) {
/* 723 */               return TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this.removeEntryIf(Predicates.compose(Predicates.not(Predicates.in(c)), Maps.keyFunction()));
/*     */             }
/*     */           };
/*     */       }
/*     */ 
/*     */       
/*     */       public Set<Map.Entry<Range<K>, V>> entrySet() {
/* 730 */         return (Set)new Maps.EntrySet<Range<Range<K>>, V>()
/*     */           {
/*     */             Map<Range<K>, V> map() {
/* 733 */               return TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this;
/*     */             }
/*     */ 
/*     */             
/*     */             public Iterator<Map.Entry<Range<K>, V>> iterator() {
/* 738 */               return TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this.entryIterator();
/*     */             }
/*     */ 
/*     */             
/*     */             public boolean retainAll(Collection<?> c) {
/* 743 */               return TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this.removeEntryIf(Predicates.not(Predicates.in(c)));
/*     */             }
/*     */ 
/*     */             
/*     */             public int size() {
/* 748 */               return Iterators.size(iterator());
/*     */             }
/*     */ 
/*     */             
/*     */             public boolean isEmpty() {
/* 753 */               return !iterator().hasNext();
/*     */             }
/*     */           };
/*     */       }
/*     */       
/*     */       Iterator<Map.Entry<Range<K>, V>> entryIterator() {
/* 759 */         if (TreeRangeMap.SubRangeMap.this.subRange.isEmpty()) {
/* 760 */           return Iterators.emptyIterator();
/*     */         }
/*     */         
/* 763 */         Cut<K> cutToStart = (Cut<K>)MoreObjects.firstNonNull(TreeRangeMap.this
/* 764 */             .entriesByLowerBound.floorKey(TreeRangeMap.SubRangeMap.this.subRange.lowerBound), TreeRangeMap.SubRangeMap.this.subRange.lowerBound);
/*     */         
/* 766 */         final Iterator<TreeRangeMap.RangeMapEntry<K, V>> backingItr = TreeRangeMap.this.entriesByLowerBound.tailMap(cutToStart, true).values().iterator();
/* 767 */         return new AbstractIterator<Map.Entry<Range<K>, V>>()
/*     */           {
/*     */             @CheckForNull
/*     */             protected Map.Entry<Range<K>, V> computeNext()
/*     */             {
/* 772 */               while (backingItr.hasNext()) {
/* 773 */                 TreeRangeMap.RangeMapEntry<K, V> entry = backingItr.next();
/* 774 */                 if (entry.getLowerBound().compareTo(TreeRangeMap.SubRangeMap.this.subRange.upperBound) >= 0)
/* 775 */                   return endOfData(); 
/* 776 */                 if (entry.getUpperBound().compareTo(TreeRangeMap.SubRangeMap.this.subRange.lowerBound) > 0)
/*     */                 {
/* 778 */                   return Maps.immutableEntry(entry.getKey().intersection(TreeRangeMap.SubRangeMap.this.subRange), entry.getValue());
/*     */                 }
/*     */               } 
/* 781 */               return endOfData();
/*     */             }
/*     */           };
/*     */       }
/*     */ 
/*     */       
/*     */       public Collection<V> values() {
/* 788 */         return new Maps.Values<Range<Range<K>>, V>(this)
/*     */           {
/*     */             public boolean removeAll(Collection<?> c) {
/* 791 */               return TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this.removeEntryIf(Predicates.compose(Predicates.in(c), Maps.valueFunction()));
/*     */             }
/*     */ 
/*     */             
/*     */             public boolean retainAll(Collection<?> c) {
/* 796 */               return TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this.removeEntryIf(Predicates.compose(Predicates.not(Predicates.in(c)), Maps.valueFunction()));
/*     */             }
/*     */           };
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@CheckForNull Object o) {
/* 805 */     if (o instanceof RangeMap) {
/* 806 */       RangeMap<?, ?> rangeMap = (RangeMap<?, ?>)o;
/* 807 */       return asMapOfRanges().equals(rangeMap.asMapOfRanges());
/*     */     } 
/* 809 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 814 */     return asMapOfRanges().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 819 */     return this.entriesByLowerBound.values().toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/TreeRangeMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */