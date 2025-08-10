/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @GwtCompatible(serializable = true)
/*     */ public class TreeBasedTable<R, C, V>
/*     */   extends StandardRowSortedTable<R, C, V>
/*     */ {
/*     */   private final Comparator<? super C> columnComparator;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   private static class Factory<C, V>
/*     */     implements Supplier<TreeMap<C, V>>, Serializable
/*     */   {
/*     */     final Comparator<? super C> comparator;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     Factory(Comparator<? super C> comparator) {
/*  77 */       this.comparator = comparator;
/*     */     }
/*     */ 
/*     */     
/*     */     public TreeMap<C, V> get() {
/*  82 */       return new TreeMap<>(this.comparator);
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
/*     */   public static <R extends Comparable, C extends Comparable, V> TreeBasedTable<R, C, V> create() {
/*  97 */     return new TreeBasedTable<>(Ordering.natural(), Ordering.natural());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <R, C, V> TreeBasedTable<R, C, V> create(Comparator<? super R> rowComparator, Comparator<? super C> columnComparator) {
/* 108 */     Preconditions.checkNotNull(rowComparator);
/* 109 */     Preconditions.checkNotNull(columnComparator);
/* 110 */     return new TreeBasedTable<>(rowComparator, columnComparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <R, C, V> TreeBasedTable<R, C, V> create(TreeBasedTable<R, C, ? extends V> table) {
/* 119 */     TreeBasedTable<R, C, V> result = new TreeBasedTable<>(table.rowComparator(), table.columnComparator());
/* 120 */     result.putAll(table);
/* 121 */     return result;
/*     */   }
/*     */   
/*     */   TreeBasedTable(Comparator<? super R> rowComparator, Comparator<? super C> columnComparator) {
/* 125 */     super(new TreeMap<>(rowComparator), (Supplier)new Factory<>(columnComparator));
/* 126 */     this.columnComparator = columnComparator;
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
/*     */   public Comparator<? super R> rowComparator() {
/* 143 */     return Objects.<Comparator<? super R>>requireNonNull(rowKeySet().comparator());
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
/*     */   @Deprecated
/*     */   public Comparator<? super C> columnComparator() {
/* 156 */     return this.columnComparator;
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
/*     */   public SortedMap<C, V> row(R rowKey) {
/* 173 */     return new TreeRow(rowKey);
/*     */   }
/*     */   
/*     */   private class TreeRow extends StandardTable<R, C, V>.Row implements SortedMap<C, V> {
/*     */     @CheckForNull
/*     */     final C lowerBound;
/*     */     
/*     */     TreeRow(R rowKey) {
/* 181 */       this(rowKey, null, null);
/*     */     } @CheckForNull
/*     */     final C upperBound; @CheckForNull
/*     */     transient SortedMap<C, V> wholeRow; TreeRow(@CheckForNull R rowKey, @CheckForNull C lowerBound, C upperBound) {
/* 185 */       super(rowKey);
/* 186 */       this.lowerBound = lowerBound;
/* 187 */       this.upperBound = upperBound;
/* 188 */       Preconditions.checkArgument((lowerBound == null || upperBound == null || 
/* 189 */           compare(lowerBound, upperBound) <= 0));
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedSet<C> keySet() {
/* 194 */       return new Maps.SortedKeySet<>(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public Comparator<? super C> comparator() {
/* 199 */       return TreeBasedTable.this.columnComparator();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     int compare(Object a, Object b) {
/* 205 */       Comparator<Object> cmp = (Comparator)comparator();
/* 206 */       return cmp.compare(a, b);
/*     */     }
/*     */     
/*     */     boolean rangeContains(@CheckForNull Object o) {
/* 210 */       return (o != null && (this.lowerBound == null || 
/* 211 */         compare(this.lowerBound, o) <= 0) && (this.upperBound == null || 
/* 212 */         compare(this.upperBound, o) > 0));
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedMap<C, V> subMap(C fromKey, C toKey) {
/* 217 */       Preconditions.checkArgument((rangeContains(Preconditions.checkNotNull(fromKey)) && rangeContains(Preconditions.checkNotNull(toKey))));
/* 218 */       return new TreeRow(this.rowKey, fromKey, toKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedMap<C, V> headMap(C toKey) {
/* 223 */       Preconditions.checkArgument(rangeContains(Preconditions.checkNotNull(toKey)));
/* 224 */       return new TreeRow(this.rowKey, this.lowerBound, toKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedMap<C, V> tailMap(C fromKey) {
/* 229 */       Preconditions.checkArgument(rangeContains(Preconditions.checkNotNull(fromKey)));
/* 230 */       return new TreeRow(this.rowKey, fromKey, this.upperBound);
/*     */     }
/*     */ 
/*     */     
/*     */     public C firstKey() {
/* 235 */       updateBackingRowMapField();
/* 236 */       if (this.backingRowMap == null) {
/* 237 */         throw new NoSuchElementException();
/*     */       }
/* 239 */       return (C)((SortedMap)this.backingRowMap).firstKey();
/*     */     }
/*     */ 
/*     */     
/*     */     public C lastKey() {
/* 244 */       updateBackingRowMapField();
/* 245 */       if (this.backingRowMap == null) {
/* 246 */         throw new NoSuchElementException();
/*     */       }
/* 248 */       return (C)((SortedMap)this.backingRowMap).lastKey();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void updateWholeRowField() {
/* 255 */       if (this.wholeRow == null || (this.wholeRow.isEmpty() && TreeBasedTable.this.backingMap.containsKey(this.rowKey))) {
/* 256 */         this.wholeRow = (SortedMap<C, V>)TreeBasedTable.this.backingMap.get(this.rowKey);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     SortedMap<C, V> computeBackingRowMap() {
/* 263 */       updateWholeRowField();
/* 264 */       SortedMap<C, V> map = this.wholeRow;
/* 265 */       if (map != null) {
/* 266 */         if (this.lowerBound != null) {
/* 267 */           map = map.tailMap(this.lowerBound);
/*     */         }
/* 269 */         if (this.upperBound != null) {
/* 270 */           map = map.headMap(this.upperBound);
/*     */         }
/* 272 */         return map;
/*     */       } 
/* 274 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     void maintainEmptyInvariant() {
/* 279 */       updateWholeRowField();
/* 280 */       if (this.wholeRow != null && this.wholeRow.isEmpty()) {
/* 281 */         TreeBasedTable.this.backingMap.remove(this.rowKey);
/* 282 */         this.wholeRow = null;
/* 283 */         this.backingRowMap = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(@CheckForNull Object key) {
/* 289 */       return (rangeContains(key) && super.containsKey(key));
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public V put(C key, V value) {
/* 295 */       Preconditions.checkArgument(rangeContains(Preconditions.checkNotNull(key)));
/* 296 */       return super.put(key, value);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedSet<R> rowKeySet() {
/* 304 */     return super.rowKeySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMap<R, Map<C, V>> rowMap() {
/* 309 */     return super.rowMap();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Iterator<C> createColumnKeyIterator() {
/* 315 */     final Comparator<? super C> comparator = columnComparator();
/*     */ 
/*     */     
/* 318 */     final Iterator<C> merged = Iterators.mergeSorted(
/* 319 */         Iterables.transform(this.backingMap
/* 320 */           .values(), input -> input.keySet().iterator()), comparator);
/*     */ 
/*     */     
/* 323 */     return new AbstractIterator<C>(this) {
/*     */         @CheckForNull
/*     */         C lastValue;
/*     */         
/*     */         @CheckForNull
/*     */         protected C computeNext() {
/* 329 */           while (merged.hasNext()) {
/* 330 */             C next = merged.next();
/* 331 */             boolean duplicate = (this.lastValue != null && comparator.compare(next, this.lastValue) == 0);
/*     */ 
/*     */             
/* 334 */             if (!duplicate) {
/* 335 */               this.lastValue = next;
/* 336 */               return this.lastValue;
/*     */             } 
/*     */           } 
/*     */           
/* 340 */           this.lastValue = null;
/* 341 */           return endOfData();
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/TreeBasedTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */