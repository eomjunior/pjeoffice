/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ abstract class RegularImmutableTable<R, C, V>
/*     */   extends ImmutableTable<R, C, V>
/*     */ {
/*     */   final ImmutableSet<Table.Cell<R, C, V>> createCellSet() {
/*  45 */     return isEmpty() ? ImmutableSet.<Table.Cell<R, C, V>>of() : new CellSet();
/*     */   }
/*     */   
/*     */   private final class CellSet extends IndexedImmutableSet<Table.Cell<R, C, V>> {
/*     */     private CellSet() {}
/*     */     
/*     */     public int size() {
/*  52 */       return RegularImmutableTable.this.size();
/*     */     }
/*     */ 
/*     */     
/*     */     Table.Cell<R, C, V> get(int index) {
/*  57 */       return RegularImmutableTable.this.getCell(index);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(@CheckForNull Object object) {
/*  62 */       if (object instanceof Table.Cell) {
/*  63 */         Table.Cell<?, ?, ?> cell = (Table.Cell<?, ?, ?>)object;
/*  64 */         Object value = RegularImmutableTable.this.get(cell.getRowKey(), cell.getColumnKey());
/*  65 */         return (value != null && value.equals(cell.getValue()));
/*     */       } 
/*  67 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/*  72 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @J2ktIncompatible
/*     */     @GwtIncompatible
/*     */     Object writeReplace() {
/*  81 */       return super.writeReplace();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final ImmutableCollection<V> createValues() {
/*  89 */     return isEmpty() ? ImmutableList.<V>of() : new Values();
/*     */   }
/*     */   
/*     */   private final class Values extends ImmutableList<V> {
/*     */     private Values() {}
/*     */     
/*     */     public int size() {
/*  96 */       return RegularImmutableTable.this.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public V get(int index) {
/* 101 */       return (V)RegularImmutableTable.this.getValue(index);
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 106 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @J2ktIncompatible
/*     */     @GwtIncompatible
/*     */     Object writeReplace() {
/* 115 */       return super.writeReplace();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <R, C, V> RegularImmutableTable<R, C, V> forCells(List<Table.Cell<R, C, V>> cells, @CheckForNull Comparator<? super R> rowComparator, @CheckForNull Comparator<? super C> columnComparator) {
/* 123 */     Preconditions.checkNotNull(cells);
/* 124 */     if (rowComparator != null || columnComparator != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 132 */       Comparator<Table.Cell<R, C, V>> comparator = (cell1, cell2) -> {
/*     */           int rowCompare = (rowComparator == null) ? 0 : rowComparator.compare(cell1.getRowKey(), cell2.getRowKey());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           return (rowCompare != 0) ? rowCompare : ((columnComparator == null) ? 0 : columnComparator.compare(cell1.getColumnKey(), cell2.getColumnKey()));
/*     */         };
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 145 */       Collections.sort(cells, comparator);
/*     */     } 
/* 147 */     return forCellsInternal(cells, rowComparator, columnComparator);
/*     */   }
/*     */   
/*     */   static <R, C, V> RegularImmutableTable<R, C, V> forCells(Iterable<Table.Cell<R, C, V>> cells) {
/* 151 */     return forCellsInternal(cells, (Comparator<? super R>)null, (Comparator<? super C>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <R, C, V> RegularImmutableTable<R, C, V> forCellsInternal(Iterable<Table.Cell<R, C, V>> cells, @CheckForNull Comparator<? super R> rowComparator, @CheckForNull Comparator<? super C> columnComparator) {
/* 158 */     Set<R> rowSpaceBuilder = new LinkedHashSet<>();
/* 159 */     Set<C> columnSpaceBuilder = new LinkedHashSet<>();
/* 160 */     ImmutableList<Table.Cell<R, C, V>> cellList = ImmutableList.copyOf(cells);
/* 161 */     for (Table.Cell<R, C, V> cell : cells) {
/* 162 */       rowSpaceBuilder.add(cell.getRowKey());
/* 163 */       columnSpaceBuilder.add(cell.getColumnKey());
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 169 */     ImmutableSet<R> rowSpace = (rowComparator == null) ? ImmutableSet.<R>copyOf(rowSpaceBuilder) : ImmutableSet.<R>copyOf(ImmutableList.sortedCopyOf(rowComparator, rowSpaceBuilder));
/*     */ 
/*     */ 
/*     */     
/* 173 */     ImmutableSet<C> columnSpace = (columnComparator == null) ? ImmutableSet.<C>copyOf(columnSpaceBuilder) : ImmutableSet.<C>copyOf(ImmutableList.sortedCopyOf(columnComparator, columnSpaceBuilder));
/*     */     
/* 175 */     return forOrderedComponents(cellList, rowSpace, columnSpace);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <R, C, V> RegularImmutableTable<R, C, V> forOrderedComponents(ImmutableList<Table.Cell<R, C, V>> cellList, ImmutableSet<R> rowSpace, ImmutableSet<C> columnSpace) {
/* 185 */     return (cellList.size() > rowSpace.size() * columnSpace.size() / 2L) ? 
/* 186 */       new DenseImmutableTable<>(cellList, rowSpace, columnSpace) : 
/* 187 */       new SparseImmutableTable<>(cellList, rowSpace, columnSpace);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void checkNoDuplicate(R rowKey, C columnKey, @CheckForNull V existingValue, V newValue) {
/* 196 */     Preconditions.checkArgument((existingValue == null), "Duplicate key: (row=%s, column=%s), values: [%s, %s].", rowKey, columnKey, newValue, existingValue);
/*     */   }
/*     */   
/*     */   abstract Table.Cell<R, C, V> getCell(int paramInt);
/*     */   
/*     */   abstract V getValue(int paramInt);
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   abstract Object writeReplace();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/RegularImmutableTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */