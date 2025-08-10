/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable(containerOf = {"R", "C", "V"})
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ final class SparseImmutableTable<R, C, V>
/*     */   extends RegularImmutableTable<R, C, V>
/*     */ {
/*  32 */   static final ImmutableTable<Object, Object, Object> EMPTY = new SparseImmutableTable(
/*     */       
/*  34 */       ImmutableList.of(), ImmutableSet.of(), ImmutableSet.of());
/*     */ 
/*     */ 
/*     */   
/*     */   private final ImmutableMap<R, ImmutableMap<C, V>> rowMap;
/*     */ 
/*     */   
/*     */   private final ImmutableMap<C, ImmutableMap<R, V>> columnMap;
/*     */ 
/*     */   
/*     */   private final int[] cellRowIndices;
/*     */ 
/*     */   
/*     */   private final int[] cellColumnInRowIndices;
/*     */ 
/*     */ 
/*     */   
/*     */   SparseImmutableTable(ImmutableList<Table.Cell<R, C, V>> cellList, ImmutableSet<R> rowSpace, ImmutableSet<C> columnSpace) {
/*  52 */     Map<R, Integer> rowIndex = Maps.indexMap(rowSpace);
/*  53 */     Map<R, Map<C, V>> rows = Maps.newLinkedHashMap();
/*  54 */     for (UnmodifiableIterator<R> unmodifiableIterator = rowSpace.iterator(); unmodifiableIterator.hasNext(); ) { R row = unmodifiableIterator.next();
/*  55 */       rows.put(row, new LinkedHashMap<>()); }
/*     */     
/*  57 */     Map<C, Map<R, V>> columns = Maps.newLinkedHashMap();
/*  58 */     for (UnmodifiableIterator<C> unmodifiableIterator1 = columnSpace.iterator(); unmodifiableIterator1.hasNext(); ) { C col = unmodifiableIterator1.next();
/*  59 */       columns.put(col, new LinkedHashMap<>()); }
/*     */     
/*  61 */     int[] cellRowIndices = new int[cellList.size()];
/*  62 */     int[] cellColumnInRowIndices = new int[cellList.size()];
/*  63 */     for (int i = 0; i < cellList.size(); i++) {
/*  64 */       Table.Cell<R, C, V> cell = cellList.get(i);
/*  65 */       R rowKey = cell.getRowKey();
/*  66 */       C columnKey = cell.getColumnKey();
/*  67 */       V value = cell.getValue();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  73 */       cellRowIndices[i] = ((Integer)Objects.requireNonNull((T)rowIndex.get(rowKey))).intValue();
/*  74 */       Map<C, V> thisRow = Objects.<Map<C, V>>requireNonNull(rows.get(rowKey));
/*  75 */       cellColumnInRowIndices[i] = thisRow.size();
/*  76 */       V oldValue = thisRow.put(columnKey, value);
/*  77 */       checkNoDuplicate(rowKey, columnKey, oldValue, value);
/*  78 */       ((Map<R, V>)Objects.<Map<R, V>>requireNonNull(columns.get(columnKey))).put(rowKey, value);
/*     */     } 
/*  80 */     this.cellRowIndices = cellRowIndices;
/*  81 */     this.cellColumnInRowIndices = cellColumnInRowIndices;
/*     */     
/*  83 */     ImmutableMap.Builder<R, ImmutableMap<C, V>> rowBuilder = new ImmutableMap.Builder<>(rows.size());
/*  84 */     for (Map.Entry<R, Map<C, V>> row : rows.entrySet()) {
/*  85 */       rowBuilder.put(row.getKey(), ImmutableMap.copyOf(row.getValue()));
/*     */     }
/*  87 */     this.rowMap = rowBuilder.buildOrThrow();
/*     */ 
/*     */     
/*  90 */     ImmutableMap.Builder<C, ImmutableMap<R, V>> columnBuilder = new ImmutableMap.Builder<>(columns.size());
/*  91 */     for (Map.Entry<C, Map<R, V>> col : columns.entrySet()) {
/*  92 */       columnBuilder.put(col.getKey(), ImmutableMap.copyOf(col.getValue()));
/*     */     }
/*  94 */     this.columnMap = columnBuilder.buildOrThrow();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMap<C, Map<R, V>> columnMap() {
/* 100 */     ImmutableMap<C, ImmutableMap<R, V>> columnMap = this.columnMap;
/* 101 */     return ImmutableMap.copyOf((Map)columnMap);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMap<R, Map<C, V>> rowMap() {
/* 107 */     ImmutableMap<R, ImmutableMap<C, V>> rowMap = this.rowMap;
/* 108 */     return ImmutableMap.copyOf((Map)rowMap);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 113 */     return this.cellRowIndices.length;
/*     */   }
/*     */ 
/*     */   
/*     */   Table.Cell<R, C, V> getCell(int index) {
/* 118 */     int rowIndex = this.cellRowIndices[index];
/* 119 */     Map.Entry<R, ImmutableMap<C, V>> rowEntry = this.rowMap.entrySet().asList().get(rowIndex);
/* 120 */     ImmutableMap<C, V> row = rowEntry.getValue();
/* 121 */     int columnIndex = this.cellColumnInRowIndices[index];
/* 122 */     Map.Entry<C, V> colEntry = row.entrySet().asList().get(columnIndex);
/* 123 */     return cellOf(rowEntry.getKey(), colEntry.getKey(), colEntry.getValue());
/*     */   }
/*     */ 
/*     */   
/*     */   V getValue(int index) {
/* 128 */     int rowIndex = this.cellRowIndices[index];
/* 129 */     ImmutableMap<C, V> row = this.rowMap.values().asList().get(rowIndex);
/* 130 */     int columnIndex = this.cellColumnInRowIndices[index];
/* 131 */     return row.values().asList().get(columnIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   Object writeReplace() {
/* 138 */     Map<C, Integer> columnKeyToIndex = Maps.indexMap(columnKeySet());
/* 139 */     int[] cellColumnIndices = new int[cellSet().size()];
/* 140 */     int i = 0;
/* 141 */     for (UnmodifiableIterator<Table.Cell<R, C, V>> unmodifiableIterator = cellSet().iterator(); unmodifiableIterator.hasNext(); ) { Table.Cell<R, C, V> cell = unmodifiableIterator.next();
/*     */       
/* 143 */       cellColumnIndices[i++] = ((Integer)Objects.requireNonNull((T)columnKeyToIndex.get(cell.getColumnKey()))).intValue(); }
/*     */     
/* 145 */     return ImmutableTable.SerializedForm.create(this, this.cellRowIndices, cellColumnIndices);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/SparseImmutableTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */