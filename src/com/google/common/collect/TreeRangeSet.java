/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NavigableMap;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtIncompatible
/*     */ public class TreeRangeSet<C extends Comparable<?>>
/*     */   extends AbstractRangeSet<C>
/*     */   implements Serializable
/*     */ {
/*     */   @VisibleForTesting
/*     */   final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   private transient Set<Range<C>> asRanges;
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   private transient Set<Range<C>> asDescendingSetOfRanges;
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   private transient RangeSet<C> complement;
/*     */   
/*     */   public static <C extends Comparable<?>> TreeRangeSet<C> create() {
/*  50 */     return new TreeRangeSet<>(new TreeMap<>());
/*     */   }
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> TreeRangeSet<C> create(RangeSet<C> rangeSet) {
/*  55 */     TreeRangeSet<C> result = create();
/*  56 */     result.addAll(rangeSet);
/*  57 */     return result;
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
/*     */   public static <C extends Comparable<?>> TreeRangeSet<C> create(Iterable<Range<C>> ranges) {
/*  70 */     TreeRangeSet<C> result = create();
/*  71 */     result.addAll(ranges);
/*  72 */     return result;
/*     */   }
/*     */   
/*     */   private TreeRangeSet(NavigableMap<Cut<C>, Range<C>> rangesByLowerCut) {
/*  76 */     this.rangesByLowerBound = rangesByLowerCut;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Range<C>> asRanges() {
/*  84 */     Set<Range<C>> result = this.asRanges;
/*  85 */     return (result == null) ? (this.asRanges = new AsRanges(this, this.rangesByLowerBound.values())) : result;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Range<C>> asDescendingSetOfRanges() {
/*  90 */     Set<Range<C>> result = this.asDescendingSetOfRanges;
/*  91 */     return (result == null) ? (
/*  92 */       this.asDescendingSetOfRanges = new AsRanges(this, this.rangesByLowerBound.descendingMap().values())) : 
/*  93 */       result;
/*     */   }
/*     */   
/*     */   final class AsRanges
/*     */     extends ForwardingCollection<Range<C>> implements Set<Range<C>> {
/*     */     final Collection<Range<C>> delegate;
/*     */     
/*     */     AsRanges(TreeRangeSet this$0, Collection<Range<C>> delegate) {
/* 101 */       this.delegate = delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Collection<Range<C>> delegate() {
/* 106 */       return this.delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 111 */       return Sets.hashCodeImpl(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object o) {
/* 116 */       return Sets.equalsImpl(this, o);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Range<C> rangeContaining(C value) {
/* 123 */     Preconditions.checkNotNull(value);
/* 124 */     Map.Entry<Cut<C>, Range<C>> floorEntry = this.rangesByLowerBound.floorEntry((Cut)Cut.belowValue((Comparable)value));
/* 125 */     if (floorEntry != null && ((Range)floorEntry.getValue()).contains(value)) {
/* 126 */       return floorEntry.getValue();
/*     */     }
/*     */     
/* 129 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean intersects(Range<C> range) {
/* 135 */     Preconditions.checkNotNull(range);
/* 136 */     Map.Entry<Cut<C>, Range<C>> ceilingEntry = this.rangesByLowerBound.ceilingEntry(range.lowerBound);
/* 137 */     if (ceilingEntry != null && ((Range)ceilingEntry
/* 138 */       .getValue()).isConnected(range) && 
/* 139 */       !((Range)ceilingEntry.getValue()).intersection(range).isEmpty()) {
/* 140 */       return true;
/*     */     }
/* 142 */     Map.Entry<Cut<C>, Range<C>> priorEntry = this.rangesByLowerBound.lowerEntry(range.lowerBound);
/* 143 */     return (priorEntry != null && ((Range)priorEntry
/* 144 */       .getValue()).isConnected(range) && 
/* 145 */       !((Range)priorEntry.getValue()).intersection(range).isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean encloses(Range<C> range) {
/* 150 */     Preconditions.checkNotNull(range);
/* 151 */     Map.Entry<Cut<C>, Range<C>> floorEntry = this.rangesByLowerBound.floorEntry(range.lowerBound);
/* 152 */     return (floorEntry != null && ((Range)floorEntry.getValue()).encloses(range));
/*     */   }
/*     */   
/*     */   @CheckForNull
/*     */   private Range<C> rangeEnclosing(Range<C> range) {
/* 157 */     Preconditions.checkNotNull(range);
/* 158 */     Map.Entry<Cut<C>, Range<C>> floorEntry = this.rangesByLowerBound.floorEntry(range.lowerBound);
/* 159 */     return (floorEntry != null && ((Range)floorEntry.getValue()).encloses(range)) ? 
/* 160 */       floorEntry.getValue() : 
/* 161 */       null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Range<C> span() {
/* 166 */     Map.Entry<Cut<C>, Range<C>> firstEntry = this.rangesByLowerBound.firstEntry();
/* 167 */     Map.Entry<Cut<C>, Range<C>> lastEntry = this.rangesByLowerBound.lastEntry();
/* 168 */     if (firstEntry == null || lastEntry == null)
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 173 */       throw new NoSuchElementException();
/*     */     }
/* 175 */     return Range.create(((Range)firstEntry.getValue()).lowerBound, ((Range)lastEntry.getValue()).upperBound);
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(Range<C> rangeToAdd) {
/* 180 */     Preconditions.checkNotNull(rangeToAdd);
/*     */     
/* 182 */     if (rangeToAdd.isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 188 */     Cut<C> lbToAdd = rangeToAdd.lowerBound;
/* 189 */     Cut<C> ubToAdd = rangeToAdd.upperBound;
/*     */     
/* 191 */     Map.Entry<Cut<C>, Range<C>> entryBelowLB = this.rangesByLowerBound.lowerEntry(lbToAdd);
/* 192 */     if (entryBelowLB != null) {
/*     */       
/* 194 */       Range<C> rangeBelowLB = entryBelowLB.getValue();
/* 195 */       if (rangeBelowLB.upperBound.compareTo(lbToAdd) >= 0) {
/*     */         
/* 197 */         if (rangeBelowLB.upperBound.compareTo(ubToAdd) >= 0)
/*     */         {
/* 199 */           ubToAdd = rangeBelowLB.upperBound;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 205 */         lbToAdd = rangeBelowLB.lowerBound;
/*     */       } 
/*     */     } 
/*     */     
/* 209 */     Map.Entry<Cut<C>, Range<C>> entryBelowUB = this.rangesByLowerBound.floorEntry(ubToAdd);
/* 210 */     if (entryBelowUB != null) {
/*     */       
/* 212 */       Range<C> rangeBelowUB = entryBelowUB.getValue();
/* 213 */       if (rangeBelowUB.upperBound.compareTo(ubToAdd) >= 0)
/*     */       {
/* 215 */         ubToAdd = rangeBelowUB.upperBound;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 220 */     this.rangesByLowerBound.subMap(lbToAdd, ubToAdd).clear();
/*     */     
/* 222 */     replaceRangeWithSameLowerBound(Range.create(lbToAdd, ubToAdd));
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Range<C> rangeToRemove) {
/* 227 */     Preconditions.checkNotNull(rangeToRemove);
/*     */     
/* 229 */     if (rangeToRemove.isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 236 */     Map.Entry<Cut<C>, Range<C>> entryBelowLB = this.rangesByLowerBound.lowerEntry(rangeToRemove.lowerBound);
/* 237 */     if (entryBelowLB != null) {
/*     */       
/* 239 */       Range<C> rangeBelowLB = entryBelowLB.getValue();
/* 240 */       if (rangeBelowLB.upperBound.compareTo(rangeToRemove.lowerBound) >= 0) {
/*     */         
/* 242 */         if (rangeToRemove.hasUpperBound() && rangeBelowLB.upperBound
/* 243 */           .compareTo(rangeToRemove.upperBound) >= 0)
/*     */         {
/* 245 */           replaceRangeWithSameLowerBound(
/* 246 */               Range.create(rangeToRemove.upperBound, rangeBelowLB.upperBound));
/*     */         }
/* 248 */         replaceRangeWithSameLowerBound(
/* 249 */             Range.create(rangeBelowLB.lowerBound, rangeToRemove.lowerBound));
/*     */       } 
/*     */     } 
/*     */     
/* 253 */     Map.Entry<Cut<C>, Range<C>> entryBelowUB = this.rangesByLowerBound.floorEntry(rangeToRemove.upperBound);
/* 254 */     if (entryBelowUB != null) {
/*     */       
/* 256 */       Range<C> rangeBelowUB = entryBelowUB.getValue();
/* 257 */       if (rangeToRemove.hasUpperBound() && rangeBelowUB.upperBound
/* 258 */         .compareTo(rangeToRemove.upperBound) >= 0)
/*     */       {
/* 260 */         replaceRangeWithSameLowerBound(
/* 261 */             Range.create(rangeToRemove.upperBound, rangeBelowUB.upperBound));
/*     */       }
/*     */     } 
/*     */     
/* 265 */     this.rangesByLowerBound.subMap(rangeToRemove.lowerBound, rangeToRemove.upperBound).clear();
/*     */   }
/*     */   
/*     */   private void replaceRangeWithSameLowerBound(Range<C> range) {
/* 269 */     if (range.isEmpty()) {
/* 270 */       this.rangesByLowerBound.remove(range.lowerBound);
/*     */     } else {
/* 272 */       this.rangesByLowerBound.put(range.lowerBound, range);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RangeSet<C> complement() {
/* 280 */     RangeSet<C> result = this.complement;
/* 281 */     return (result == null) ? (this.complement = new Complement()) : result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static final class RangesByUpperBound<C extends Comparable<?>>
/*     */     extends AbstractNavigableMap<Cut<C>, Range<C>>
/*     */   {
/*     */     private final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;
/*     */     
/*     */     private final Range<Cut<C>> upperBoundWindow;
/*     */ 
/*     */     
/*     */     RangesByUpperBound(NavigableMap<Cut<C>, Range<C>> rangesByLowerBound) {
/* 296 */       this.rangesByLowerBound = rangesByLowerBound;
/* 297 */       this.upperBoundWindow = Range.all();
/*     */     }
/*     */ 
/*     */     
/*     */     private RangesByUpperBound(NavigableMap<Cut<C>, Range<C>> rangesByLowerBound, Range<Cut<C>> upperBoundWindow) {
/* 302 */       this.rangesByLowerBound = rangesByLowerBound;
/* 303 */       this.upperBoundWindow = upperBoundWindow;
/*     */     }
/*     */     
/*     */     private NavigableMap<Cut<C>, Range<C>> subMap(Range<Cut<C>> window) {
/* 307 */       if (window.isConnected(this.upperBoundWindow)) {
/* 308 */         return new RangesByUpperBound(this.rangesByLowerBound, window.intersection(this.upperBoundWindow));
/*     */       }
/* 310 */       return ImmutableSortedMap.of();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> subMap(Cut<C> fromKey, boolean fromInclusive, Cut<C> toKey, boolean toInclusive) {
/* 317 */       return subMap(
/* 318 */           Range.range(fromKey, 
/* 319 */             BoundType.forBoolean(fromInclusive), toKey, 
/* 320 */             BoundType.forBoolean(toInclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> headMap(Cut<C> toKey, boolean inclusive) {
/* 325 */       return subMap(Range.upTo(toKey, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> tailMap(Cut<C> fromKey, boolean inclusive) {
/* 330 */       return subMap(Range.downTo(fromKey, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public Comparator<? super Cut<C>> comparator() {
/* 335 */       return Ordering.natural();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(@CheckForNull Object key) {
/* 340 */       return (get(key) != null);
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public Range<C> get(@CheckForNull Object key) {
/* 346 */       if (key instanceof Cut) {
/*     */         
/*     */         try {
/* 349 */           Cut<C> cut = (Cut<C>)key;
/* 350 */           if (!this.upperBoundWindow.contains(cut)) {
/* 351 */             return null;
/*     */           }
/* 353 */           Map.Entry<Cut<C>, Range<C>> candidate = this.rangesByLowerBound.lowerEntry(cut);
/* 354 */           if (candidate != null && ((Range)candidate.getValue()).upperBound.equals(cut)) {
/* 355 */             return candidate.getValue();
/*     */           }
/* 357 */         } catch (ClassCastException e) {
/* 358 */           return null;
/*     */         } 
/*     */       }
/* 361 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Iterator<Map.Entry<Cut<C>, Range<C>>> entryIterator() {
/*     */       final Iterator<Range<C>> backingItr;
/* 371 */       if (!this.upperBoundWindow.hasLowerBound()) {
/* 372 */         backingItr = this.rangesByLowerBound.values().iterator();
/*     */       } else {
/*     */         
/* 375 */         Map.Entry<Cut<C>, Range<C>> lowerEntry = this.rangesByLowerBound.lowerEntry(this.upperBoundWindow.lowerEndpoint());
/* 376 */         if (lowerEntry == null) {
/* 377 */           backingItr = this.rangesByLowerBound.values().iterator();
/* 378 */         } else if (this.upperBoundWindow.lowerBound.isLessThan(((Range)lowerEntry.getValue()).upperBound)) {
/* 379 */           backingItr = this.rangesByLowerBound.tailMap(lowerEntry.getKey(), true).values().iterator();
/*     */         
/*     */         }
/*     */         else {
/*     */ 
/*     */           
/* 385 */           backingItr = this.rangesByLowerBound.tailMap(this.upperBoundWindow.lowerEndpoint(), true).values().iterator();
/*     */         } 
/*     */       } 
/* 388 */       return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>()
/*     */         {
/*     */           @CheckForNull
/*     */           protected Map.Entry<Cut<C>, Range<C>> computeNext() {
/* 392 */             if (!backingItr.hasNext()) {
/* 393 */               return endOfData();
/*     */             }
/* 395 */             Range<C> range = backingItr.next();
/* 396 */             if (TreeRangeSet.RangesByUpperBound.this.upperBoundWindow.upperBound.isLessThan(range.upperBound)) {
/* 397 */               return endOfData();
/*     */             }
/* 399 */             return Maps.immutableEntry(range.upperBound, range);
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Iterator<Map.Entry<Cut<C>, Range<C>>> descendingEntryIterator() {
/*     */       Collection<Range<C>> candidates;
/* 408 */       if (this.upperBoundWindow.hasUpperBound()) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 413 */         candidates = this.rangesByLowerBound.headMap(this.upperBoundWindow.upperEndpoint(), false).descendingMap().values();
/*     */       } else {
/* 415 */         candidates = this.rangesByLowerBound.descendingMap().values();
/*     */       } 
/* 417 */       final PeekingIterator<Range<C>> backingItr = Iterators.peekingIterator(candidates.iterator());
/* 418 */       if (backingItr.hasNext() && this.upperBoundWindow.upperBound
/* 419 */         .isLessThan(((Range)backingItr.peek()).upperBound)) {
/* 420 */         backingItr.next();
/*     */       }
/* 422 */       return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>()
/*     */         {
/*     */           @CheckForNull
/*     */           protected Map.Entry<Cut<C>, Range<C>> computeNext() {
/* 426 */             if (!backingItr.hasNext()) {
/* 427 */               return endOfData();
/*     */             }
/* 429 */             Range<C> range = backingItr.next();
/* 430 */             return TreeRangeSet.RangesByUpperBound.this.upperBoundWindow.lowerBound.isLessThan(range.upperBound) ? 
/* 431 */               Maps.<Cut<C>, Range<C>>immutableEntry(range.upperBound, range) : 
/* 432 */               endOfData();
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 439 */       if (this.upperBoundWindow.equals(Range.all())) {
/* 440 */         return this.rangesByLowerBound.size();
/*     */       }
/* 442 */       return Iterators.size(entryIterator());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 447 */       return this.upperBoundWindow.equals(Range.all()) ? 
/* 448 */         this.rangesByLowerBound.isEmpty() : (
/* 449 */         !entryIterator().hasNext());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ComplementRangesByLowerBound<C extends Comparable<?>>
/*     */     extends AbstractNavigableMap<Cut<C>, Range<C>>
/*     */   {
/*     */     private final NavigableMap<Cut<C>, Range<C>> positiveRangesByLowerBound;
/*     */     
/*     */     private final NavigableMap<Cut<C>, Range<C>> positiveRangesByUpperBound;
/*     */     
/*     */     private final Range<Cut<C>> complementLowerBoundWindow;
/*     */ 
/*     */     
/*     */     ComplementRangesByLowerBound(NavigableMap<Cut<C>, Range<C>> positiveRangesByLowerBound) {
/* 466 */       this(positiveRangesByLowerBound, Range.all());
/*     */     }
/*     */ 
/*     */     
/*     */     private ComplementRangesByLowerBound(NavigableMap<Cut<C>, Range<C>> positiveRangesByLowerBound, Range<Cut<C>> window) {
/* 471 */       this.positiveRangesByLowerBound = positiveRangesByLowerBound;
/* 472 */       this.positiveRangesByUpperBound = new TreeRangeSet.RangesByUpperBound<>(positiveRangesByLowerBound);
/* 473 */       this.complementLowerBoundWindow = window;
/*     */     }
/*     */     
/*     */     private NavigableMap<Cut<C>, Range<C>> subMap(Range<Cut<C>> subWindow) {
/* 477 */       if (!this.complementLowerBoundWindow.isConnected(subWindow)) {
/* 478 */         return ImmutableSortedMap.of();
/*     */       }
/* 480 */       subWindow = subWindow.intersection(this.complementLowerBoundWindow);
/* 481 */       return new ComplementRangesByLowerBound(this.positiveRangesByLowerBound, subWindow);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> subMap(Cut<C> fromKey, boolean fromInclusive, Cut<C> toKey, boolean toInclusive) {
/* 488 */       return subMap(
/* 489 */           Range.range(fromKey, 
/* 490 */             BoundType.forBoolean(fromInclusive), toKey, 
/* 491 */             BoundType.forBoolean(toInclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> headMap(Cut<C> toKey, boolean inclusive) {
/* 496 */       return subMap(Range.upTo(toKey, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> tailMap(Cut<C> fromKey, boolean inclusive) {
/* 501 */       return subMap(Range.downTo(fromKey, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public Comparator<? super Cut<C>> comparator() {
/* 506 */       return Ordering.natural();
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
/*     */     Iterator<Map.Entry<Cut<C>, Range<C>>> entryIterator() {
/*     */       Collection<Range<C>> positiveRanges;
/*     */       final Cut<C> firstComplementRangeLowerBound;
/* 521 */       if (this.complementLowerBoundWindow.hasLowerBound()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 527 */         positiveRanges = this.positiveRangesByUpperBound.tailMap(this.complementLowerBoundWindow.lowerEndpoint(), (this.complementLowerBoundWindow.lowerBoundType() == BoundType.CLOSED)).values();
/*     */       } else {
/* 529 */         positiveRanges = this.positiveRangesByUpperBound.values();
/*     */       } 
/* 531 */       final PeekingIterator<Range<C>> positiveItr = Iterators.peekingIterator(positiveRanges.iterator());
/*     */       
/* 533 */       if (this.complementLowerBoundWindow.contains((Cut)Cut.belowAll()) && (
/* 534 */         !positiveItr.hasNext() || ((Range)positiveItr.peek()).lowerBound != Cut.belowAll())) {
/* 535 */         firstComplementRangeLowerBound = Cut.belowAll();
/* 536 */       } else if (positiveItr.hasNext()) {
/* 537 */         firstComplementRangeLowerBound = ((Range)positiveItr.next()).upperBound;
/*     */       } else {
/* 539 */         return Iterators.emptyIterator();
/*     */       } 
/* 541 */       return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>() {
/* 542 */           Cut<C> nextComplementRangeLowerBound = firstComplementRangeLowerBound;
/*     */           
/*     */           @CheckForNull
/*     */           protected Map.Entry<Cut<C>, Range<C>> computeNext() {
/*     */             Range<C> negativeRange;
/* 547 */             if (TreeRangeSet.ComplementRangesByLowerBound.this.complementLowerBoundWindow.upperBound.isLessThan(this.nextComplementRangeLowerBound) || this.nextComplementRangeLowerBound == 
/* 548 */               Cut.aboveAll()) {
/* 549 */               return endOfData();
/*     */             }
/*     */             
/* 552 */             if (positiveItr.hasNext()) {
/* 553 */               Range<C> positiveRange = positiveItr.next();
/* 554 */               negativeRange = Range.create(this.nextComplementRangeLowerBound, positiveRange.lowerBound);
/* 555 */               this.nextComplementRangeLowerBound = positiveRange.upperBound;
/*     */             } else {
/* 557 */               negativeRange = Range.create(this.nextComplementRangeLowerBound, (Cut)Cut.aboveAll());
/* 558 */               this.nextComplementRangeLowerBound = Cut.aboveAll();
/*     */             } 
/* 560 */             return Maps.immutableEntry(negativeRange.lowerBound, negativeRange);
/*     */           }
/*     */         };
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
/*     */     Iterator<Map.Entry<Cut<C>, Range<C>>> descendingEntryIterator() {
/* 578 */       Cut<C> cut, startingPoint = this.complementLowerBoundWindow.hasUpperBound() ? this.complementLowerBoundWindow.upperEndpoint() : (Cut)Cut.<Comparable>aboveAll();
/*     */ 
/*     */       
/* 581 */       boolean inclusive = (this.complementLowerBoundWindow.hasUpperBound() && this.complementLowerBoundWindow.upperBoundType() == BoundType.CLOSED);
/*     */       
/* 583 */       final PeekingIterator<Range<C>> positiveItr = Iterators.peekingIterator(this.positiveRangesByUpperBound
/*     */           
/* 585 */           .headMap(startingPoint, inclusive)
/* 586 */           .descendingMap()
/* 587 */           .values()
/* 588 */           .iterator());
/*     */       
/* 590 */       if (positiveItr.hasNext())
/*     */       
/*     */       { 
/*     */         
/* 594 */         cut = (((Range)positiveItr.peek()).upperBound == Cut.aboveAll()) ? ((Range)positiveItr.next()).lowerBound : this.positiveRangesByLowerBound.higherKey(((Range)positiveItr.peek()).upperBound); }
/* 595 */       else { if (!this.complementLowerBoundWindow.contains((Cut)Cut.belowAll()) || this.positiveRangesByLowerBound
/* 596 */           .containsKey(Cut.belowAll())) {
/* 597 */           return Iterators.emptyIterator();
/*     */         }
/* 599 */         cut = this.positiveRangesByLowerBound.higherKey((Cut)Cut.belowAll()); }
/*     */       
/* 601 */       final Cut<C> firstComplementRangeUpperBound = (Cut<C>)MoreObjects.firstNonNull(cut, Cut.aboveAll());
/* 602 */       return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>() {
/* 603 */           Cut<C> nextComplementRangeUpperBound = firstComplementRangeUpperBound;
/*     */ 
/*     */           
/*     */           @CheckForNull
/*     */           protected Map.Entry<Cut<C>, Range<C>> computeNext() {
/* 608 */             if (this.nextComplementRangeUpperBound == Cut.belowAll())
/* 609 */               return endOfData(); 
/* 610 */             if (positiveItr.hasNext()) {
/* 611 */               Range<C> positiveRange = positiveItr.next();
/*     */               
/* 613 */               Range<C> negativeRange = Range.create(positiveRange.upperBound, this.nextComplementRangeUpperBound);
/* 614 */               this.nextComplementRangeUpperBound = positiveRange.lowerBound;
/* 615 */               if (TreeRangeSet.ComplementRangesByLowerBound.this.complementLowerBoundWindow.lowerBound.isLessThan(negativeRange.lowerBound)) {
/* 616 */                 return Maps.immutableEntry(negativeRange.lowerBound, negativeRange);
/*     */               }
/* 618 */             } else if (TreeRangeSet.ComplementRangesByLowerBound.this.complementLowerBoundWindow.lowerBound.isLessThan(Cut.belowAll())) {
/* 619 */               Range<C> negativeRange = Range.create((Cut)Cut.belowAll(), this.nextComplementRangeUpperBound);
/* 620 */               this.nextComplementRangeUpperBound = Cut.belowAll();
/* 621 */               return Maps.immutableEntry((Cut)Cut.belowAll(), negativeRange);
/*     */             } 
/* 623 */             return endOfData();
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 630 */       return Iterators.size(entryIterator());
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public Range<C> get(@CheckForNull Object key) {
/* 636 */       if (key instanceof Cut) {
/*     */         
/*     */         try {
/* 639 */           Cut<C> cut = (Cut<C>)key;
/*     */           
/* 641 */           Map.Entry<Cut<C>, Range<C>> firstEntry = tailMap(cut, true).firstEntry();
/* 642 */           if (firstEntry != null && ((Cut)firstEntry.getKey()).equals(cut)) {
/* 643 */             return firstEntry.getValue();
/*     */           }
/* 645 */         } catch (ClassCastException e) {
/* 646 */           return null;
/*     */         } 
/*     */       }
/* 649 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(@CheckForNull Object key) {
/* 654 */       return (get(key) != null);
/*     */     }
/*     */   }
/*     */   
/*     */   private final class Complement extends TreeRangeSet<C> {
/*     */     Complement() {
/* 660 */       super(new TreeRangeSet.ComplementRangesByLowerBound<>(TreeRangeSet.this.rangesByLowerBound));
/*     */     }
/*     */ 
/*     */     
/*     */     public void add(Range<C> rangeToAdd) {
/* 665 */       TreeRangeSet.this.remove(rangeToAdd);
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove(Range<C> rangeToRemove) {
/* 670 */       TreeRangeSet.this.add(rangeToRemove);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(C value) {
/* 675 */       return !TreeRangeSet.this.contains((Comparable)value);
/*     */     }
/*     */ 
/*     */     
/*     */     public RangeSet<C> complement() {
/* 680 */       return TreeRangeSet.this;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class SubRangeSetRangesByLowerBound<C extends Comparable<?>>
/*     */     extends AbstractNavigableMap<Cut<C>, Range<C>>
/*     */   {
/*     */     private final Range<Cut<C>> lowerBoundWindow;
/*     */ 
/*     */ 
/*     */     
/*     */     private final Range<C> restriction;
/*     */ 
/*     */     
/*     */     private final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;
/*     */ 
/*     */     
/*     */     private final NavigableMap<Cut<C>, Range<C>> rangesByUpperBound;
/*     */ 
/*     */ 
/*     */     
/*     */     private SubRangeSetRangesByLowerBound(Range<Cut<C>> lowerBoundWindow, Range<C> restriction, NavigableMap<Cut<C>, Range<C>> rangesByLowerBound) {
/* 705 */       this.lowerBoundWindow = (Range<Cut<C>>)Preconditions.checkNotNull(lowerBoundWindow);
/* 706 */       this.restriction = (Range<C>)Preconditions.checkNotNull(restriction);
/* 707 */       this.rangesByLowerBound = (NavigableMap<Cut<C>, Range<C>>)Preconditions.checkNotNull(rangesByLowerBound);
/* 708 */       this.rangesByUpperBound = new TreeRangeSet.RangesByUpperBound<>(rangesByLowerBound);
/*     */     }
/*     */     
/*     */     private NavigableMap<Cut<C>, Range<C>> subMap(Range<Cut<C>> window) {
/* 712 */       if (!window.isConnected(this.lowerBoundWindow)) {
/* 713 */         return ImmutableSortedMap.of();
/*     */       }
/* 715 */       return new SubRangeSetRangesByLowerBound(this.lowerBoundWindow
/* 716 */           .intersection(window), this.restriction, this.rangesByLowerBound);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> subMap(Cut<C> fromKey, boolean fromInclusive, Cut<C> toKey, boolean toInclusive) {
/* 723 */       return subMap(
/* 724 */           Range.range(fromKey, 
/*     */             
/* 726 */             BoundType.forBoolean(fromInclusive), toKey, 
/*     */             
/* 728 */             BoundType.forBoolean(toInclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> headMap(Cut<C> toKey, boolean inclusive) {
/* 733 */       return subMap(Range.upTo(toKey, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public NavigableMap<Cut<C>, Range<C>> tailMap(Cut<C> fromKey, boolean inclusive) {
/* 738 */       return subMap(Range.downTo(fromKey, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public Comparator<? super Cut<C>> comparator() {
/* 743 */       return Ordering.natural();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(@CheckForNull Object key) {
/* 748 */       return (get(key) != null);
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public Range<C> get(@CheckForNull Object key) {
/* 754 */       if (key instanceof Cut) {
/*     */         
/*     */         try {
/* 757 */           Cut<C> cut = (Cut<C>)key;
/* 758 */           if (!this.lowerBoundWindow.contains(cut) || cut
/* 759 */             .compareTo(this.restriction.lowerBound) < 0 || cut
/* 760 */             .compareTo(this.restriction.upperBound) >= 0)
/* 761 */             return null; 
/* 762 */           if (cut.equals(this.restriction.lowerBound)) {
/*     */             
/* 764 */             Range<C> candidate = Maps.<Range<C>>valueOrNull(this.rangesByLowerBound.floorEntry(cut));
/* 765 */             if (candidate != null && candidate.upperBound.compareTo(this.restriction.lowerBound) > 0) {
/* 766 */               return candidate.intersection(this.restriction);
/*     */             }
/*     */           } else {
/* 769 */             Range<C> result = this.rangesByLowerBound.get(cut);
/* 770 */             if (result != null) {
/* 771 */               return result.intersection(this.restriction);
/*     */             }
/*     */           } 
/* 774 */         } catch (ClassCastException e) {
/* 775 */           return null;
/*     */         } 
/*     */       }
/* 778 */       return null;
/*     */     }
/*     */     
/*     */     Iterator<Map.Entry<Cut<C>, Range<C>>> entryIterator() {
/*     */       final Iterator<Range<C>> completeRangeItr;
/* 783 */       if (this.restriction.isEmpty()) {
/* 784 */         return Iterators.emptyIterator();
/*     */       }
/*     */       
/* 787 */       if (this.lowerBoundWindow.upperBound.isLessThan(this.restriction.lowerBound))
/* 788 */         return Iterators.emptyIterator(); 
/* 789 */       if (this.lowerBoundWindow.lowerBound.isLessThan(this.restriction.lowerBound)) {
/*     */ 
/*     */         
/* 792 */         completeRangeItr = this.rangesByUpperBound.tailMap(this.restriction.lowerBound, false).values().iterator();
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */ 
/*     */         
/* 801 */         completeRangeItr = this.rangesByLowerBound.tailMap(this.lowerBoundWindow.lowerBound.endpoint(), (this.lowerBoundWindow.lowerBoundType() == BoundType.CLOSED)).values().iterator();
/*     */       } 
/*     */ 
/*     */       
/* 805 */       final Cut<Cut<C>> upperBoundOnLowerBounds = (Cut<Cut<C>>)Ordering.<Comparable>natural().min(this.lowerBoundWindow.upperBound, Cut.belowValue(this.restriction.upperBound));
/* 806 */       return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>()
/*     */         {
/*     */           @CheckForNull
/*     */           protected Map.Entry<Cut<C>, Range<C>> computeNext() {
/* 810 */             if (!completeRangeItr.hasNext()) {
/* 811 */               return endOfData();
/*     */             }
/* 813 */             Range<C> nextRange = completeRangeItr.next();
/* 814 */             if (upperBoundOnLowerBounds.isLessThan(nextRange.lowerBound)) {
/* 815 */               return endOfData();
/*     */             }
/* 817 */             nextRange = nextRange.intersection(TreeRangeSet.SubRangeSetRangesByLowerBound.this.restriction);
/* 818 */             return Maps.immutableEntry(nextRange.lowerBound, nextRange);
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Iterator<Map.Entry<Cut<C>, Range<C>>> descendingEntryIterator() {
/* 826 */       if (this.restriction.isEmpty()) {
/* 827 */         return Iterators.emptyIterator();
/*     */       }
/*     */ 
/*     */       
/* 831 */       Cut<Cut<C>> upperBoundOnLowerBounds = (Cut<Cut<C>>)Ordering.<Comparable>natural().min(this.lowerBoundWindow.upperBound, Cut.belowValue(this.restriction.upperBound));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 839 */       final Iterator<Range<C>> completeRangeItr = this.rangesByLowerBound.headMap(upperBoundOnLowerBounds.endpoint(), (upperBoundOnLowerBounds.typeAsUpperBound() == BoundType.CLOSED)).descendingMap().values().iterator();
/* 840 */       return new AbstractIterator<Map.Entry<Cut<C>, Range<C>>>()
/*     */         {
/*     */           @CheckForNull
/*     */           protected Map.Entry<Cut<C>, Range<C>> computeNext() {
/* 844 */             if (!completeRangeItr.hasNext()) {
/* 845 */               return endOfData();
/*     */             }
/* 847 */             Range<C> nextRange = completeRangeItr.next();
/* 848 */             if (TreeRangeSet.SubRangeSetRangesByLowerBound.this.restriction.lowerBound.compareTo(nextRange.upperBound) >= 0) {
/* 849 */               return endOfData();
/*     */             }
/* 851 */             nextRange = nextRange.intersection(TreeRangeSet.SubRangeSetRangesByLowerBound.this.restriction);
/* 852 */             if (TreeRangeSet.SubRangeSetRangesByLowerBound.this.lowerBoundWindow.contains(nextRange.lowerBound)) {
/* 853 */               return Maps.immutableEntry(nextRange.lowerBound, nextRange);
/*     */             }
/* 855 */             return endOfData();
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int size() {
/* 863 */       return Iterators.size(entryIterator());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public RangeSet<C> subRangeSet(Range<C> view) {
/* 869 */     return view.equals(Range.all()) ? this : new SubRangeSet(view);
/*     */   }
/*     */   
/*     */   private final class SubRangeSet extends TreeRangeSet<C> {
/*     */     private final Range<C> restriction;
/*     */     
/*     */     SubRangeSet(Range<C> restriction) {
/* 876 */       super(new TreeRangeSet.SubRangeSetRangesByLowerBound<>(
/*     */             
/* 878 */             Range.all(), restriction, TreeRangeSet.this.rangesByLowerBound, null));
/* 879 */       this.restriction = restriction;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean encloses(Range<C> range) {
/* 884 */       if (!this.restriction.isEmpty() && this.restriction.encloses(range)) {
/* 885 */         Range<C> enclosing = TreeRangeSet.this.rangeEnclosing(range);
/* 886 */         return (enclosing != null && !enclosing.intersection(this.restriction).isEmpty());
/*     */       } 
/* 888 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public Range<C> rangeContaining(C value) {
/* 894 */       if (!this.restriction.contains(value)) {
/* 895 */         return null;
/*     */       }
/* 897 */       Range<C> result = TreeRangeSet.this.rangeContaining(value);
/* 898 */       return (result == null) ? null : result.intersection(this.restriction);
/*     */     }
/*     */ 
/*     */     
/*     */     public void add(Range<C> rangeToAdd) {
/* 903 */       Preconditions.checkArgument(this.restriction
/* 904 */           .encloses(rangeToAdd), "Cannot add range %s to subRangeSet(%s)", rangeToAdd, this.restriction);
/*     */ 
/*     */ 
/*     */       
/* 908 */       TreeRangeSet.this.add(rangeToAdd);
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove(Range<C> rangeToRemove) {
/* 913 */       if (rangeToRemove.isConnected(this.restriction)) {
/* 914 */         TreeRangeSet.this.remove(rangeToRemove.intersection(this.restriction));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(C value) {
/* 920 */       return (this.restriction.contains(value) && TreeRangeSet.this.contains((Comparable)value));
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 925 */       TreeRangeSet.this.remove(this.restriction);
/*     */     }
/*     */ 
/*     */     
/*     */     public RangeSet<C> subRangeSet(Range<C> view) {
/* 930 */       if (view.encloses(this.restriction))
/* 931 */         return this; 
/* 932 */       if (view.isConnected(this.restriction)) {
/* 933 */         return new SubRangeSet(this.restriction.intersection(view));
/*     */       }
/* 935 */       return (RangeSet)ImmutableRangeSet.of();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/TreeRangeSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */