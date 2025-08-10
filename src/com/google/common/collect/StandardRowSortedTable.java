/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ class StandardRowSortedTable<R, C, V>
/*     */   extends StandardTable<R, C, V>
/*     */   implements RowSortedTable<R, C, V>
/*     */ {
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   StandardRowSortedTable(SortedMap<R, Map<C, V>> backingMap, Supplier<? extends Map<C, V>> factory) {
/*  60 */     super(backingMap, factory);
/*     */   }
/*     */   
/*     */   private SortedMap<R, Map<C, V>> sortedBackingMap() {
/*  64 */     return (SortedMap<R, Map<C, V>>)this.backingMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedSet<R> rowKeySet() {
/*  75 */     return (SortedSet<R>)rowMap().keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedMap<R, Map<C, V>> rowMap() {
/*  86 */     return (SortedMap<R, Map<C, V>>)super.rowMap();
/*     */   }
/*     */ 
/*     */   
/*     */   SortedMap<R, Map<C, V>> createRowMap() {
/*  91 */     return new RowSortedMap();
/*     */   }
/*     */   
/*     */   private class RowSortedMap extends StandardTable<R, C, V>.RowMap implements SortedMap<R, Map<C, V>> {
/*     */     private RowSortedMap() {}
/*     */     
/*     */     public SortedSet<R> keySet() {
/*  98 */       return (SortedSet<R>)super.keySet();
/*     */     }
/*     */ 
/*     */     
/*     */     SortedSet<R> createKeySet() {
/* 103 */       return new Maps.SortedKeySet<>(this);
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public Comparator<? super R> comparator() {
/* 109 */       return StandardRowSortedTable.this.sortedBackingMap().comparator();
/*     */     }
/*     */ 
/*     */     
/*     */     public R firstKey() {
/* 114 */       return (R)StandardRowSortedTable.this.sortedBackingMap().firstKey();
/*     */     }
/*     */ 
/*     */     
/*     */     public R lastKey() {
/* 119 */       return (R)StandardRowSortedTable.this.sortedBackingMap().lastKey();
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedMap<R, Map<C, V>> headMap(R toKey) {
/* 124 */       Preconditions.checkNotNull(toKey);
/* 125 */       return (new StandardRowSortedTable<>(StandardRowSortedTable.this.sortedBackingMap().headMap(toKey), StandardRowSortedTable.this.factory))
/* 126 */         .rowMap();
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedMap<R, Map<C, V>> subMap(R fromKey, R toKey) {
/* 131 */       Preconditions.checkNotNull(fromKey);
/* 132 */       Preconditions.checkNotNull(toKey);
/* 133 */       return (new StandardRowSortedTable<>(StandardRowSortedTable.this.sortedBackingMap().subMap(fromKey, toKey), StandardRowSortedTable.this.factory))
/* 134 */         .rowMap();
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedMap<R, Map<C, V>> tailMap(R fromKey) {
/* 139 */       Preconditions.checkNotNull(fromKey);
/* 140 */       return (new StandardRowSortedTable<>(StandardRowSortedTable.this.sortedBackingMap().tailMap(fromKey), StandardRowSortedTable.this.factory))
/* 141 */         .rowMap();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/StandardRowSortedTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */