/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
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
/*     */ @Immutable(containerOf = {"R", "C", "V"})
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ final class DenseImmutableTable<R, C, V>
/*     */   extends RegularImmutableTable<R, C, V>
/*     */ {
/*     */   private final ImmutableMap<R, Integer> rowKeyToIndex;
/*     */   private final ImmutableMap<C, Integer> columnKeyToIndex;
/*     */   private final ImmutableMap<R, ImmutableMap<C, V>> rowMap;
/*     */   private final ImmutableMap<C, ImmutableMap<R, V>> columnMap;
/*     */   private final int[] rowCounts;
/*     */   private final int[] columnCounts;
/*     */   private final V[][] values;
/*     */   private final int[] cellRowIndices;
/*     */   private final int[] cellColumnIndices;
/*     */   
/*     */   DenseImmutableTable(ImmutableList<Table.Cell<R, C, V>> cellList, ImmutableSet<R> rowSpace, ImmutableSet<C> columnSpace) {
/*  62 */     V[][] array = (V[][])new Object[rowSpace.size()][columnSpace.size()];
/*  63 */     this.values = array;
/*  64 */     this.rowKeyToIndex = Maps.indexMap(rowSpace);
/*  65 */     this.columnKeyToIndex = Maps.indexMap(columnSpace);
/*  66 */     this.rowCounts = new int[this.rowKeyToIndex.size()];
/*  67 */     this.columnCounts = new int[this.columnKeyToIndex.size()];
/*  68 */     int[] cellRowIndices = new int[cellList.size()];
/*  69 */     int[] cellColumnIndices = new int[cellList.size()];
/*  70 */     for (int i = 0; i < cellList.size(); i++) {
/*  71 */       Table.Cell<R, C, V> cell = cellList.get(i);
/*  72 */       R rowKey = cell.getRowKey();
/*  73 */       C columnKey = cell.getColumnKey();
/*     */       
/*  75 */       int rowIndex = ((Integer)Objects.<Integer>requireNonNull(this.rowKeyToIndex.get(rowKey))).intValue();
/*  76 */       int columnIndex = ((Integer)Objects.<Integer>requireNonNull(this.columnKeyToIndex.get(columnKey))).intValue();
/*  77 */       V existingValue = this.values[rowIndex][columnIndex];
/*  78 */       checkNoDuplicate(rowKey, columnKey, existingValue, cell.getValue());
/*  79 */       this.values[rowIndex][columnIndex] = cell.getValue();
/*  80 */       this.rowCounts[rowIndex] = this.rowCounts[rowIndex] + 1;
/*  81 */       this.columnCounts[columnIndex] = this.columnCounts[columnIndex] + 1;
/*  82 */       cellRowIndices[i] = rowIndex;
/*  83 */       cellColumnIndices[i] = columnIndex;
/*     */     } 
/*  85 */     this.cellRowIndices = cellRowIndices;
/*  86 */     this.cellColumnIndices = cellColumnIndices;
/*  87 */     this.rowMap = new RowMap();
/*  88 */     this.columnMap = new ColumnMap();
/*     */   }
/*     */   
/*     */   private static abstract class ImmutableArrayMap<K, V>
/*     */     extends ImmutableMap.IteratorBasedImmutableMap<K, V> {
/*     */     private final int size;
/*     */     
/*     */     ImmutableArrayMap(int size) {
/*  96 */       this.size = size;
/*     */     }
/*     */ 
/*     */     
/*     */     abstract ImmutableMap<K, Integer> keyToIndex();
/*     */     
/*     */     private boolean isFull() {
/* 103 */       return (this.size == keyToIndex().size());
/*     */     }
/*     */     
/*     */     K getKey(int index) {
/* 107 */       return keyToIndex().keySet().asList().get(index);
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     abstract V getValue(int param1Int);
/*     */     
/*     */     ImmutableSet<K> createKeySet() {
/* 115 */       return isFull() ? keyToIndex().keySet() : super.createKeySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 120 */       return this.size;
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public V get(@CheckForNull Object key) {
/* 126 */       Integer keyIndex = keyToIndex().get(key);
/* 127 */       return (keyIndex == null) ? null : getValue(keyIndex.intValue());
/*     */     }
/*     */ 
/*     */     
/*     */     UnmodifiableIterator<Map.Entry<K, V>> entryIterator() {
/* 132 */       return new AbstractIterator<Map.Entry<K, V>>() {
/* 133 */           private int index = -1;
/* 134 */           private final int maxIndex = DenseImmutableTable.ImmutableArrayMap.this.keyToIndex().size();
/*     */ 
/*     */           
/*     */           @CheckForNull
/*     */           protected Map.Entry<K, V> computeNext() {
/* 139 */             this.index++; for (; this.index < this.maxIndex; this.index++) {
/* 140 */               V value = (V)DenseImmutableTable.ImmutableArrayMap.this.getValue(this.index);
/* 141 */               if (value != null) {
/* 142 */                 return Maps.immutableEntry((K)DenseImmutableTable.ImmutableArrayMap.this.getKey(this.index), value);
/*     */               }
/*     */             } 
/* 145 */             return endOfData();
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @J2ktIncompatible
/*     */     @GwtIncompatible
/*     */     Object writeReplace() {
/* 156 */       return super.writeReplace();
/*     */     }
/*     */   }
/*     */   
/*     */   private final class Row extends ImmutableArrayMap<C, V> {
/*     */     private final int rowIndex;
/*     */     
/*     */     Row(int rowIndex) {
/* 164 */       super(DenseImmutableTable.this.rowCounts[rowIndex]);
/* 165 */       this.rowIndex = rowIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableMap<C, Integer> keyToIndex() {
/* 170 */       return DenseImmutableTable.this.columnKeyToIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     V getValue(int keyIndex) {
/* 176 */       return (V)DenseImmutableTable.this.values[this.rowIndex][keyIndex];
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 181 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @J2ktIncompatible
/*     */     @GwtIncompatible
/*     */     Object writeReplace() {
/* 190 */       return super.writeReplace();
/*     */     }
/*     */   }
/*     */   
/*     */   private final class Column extends ImmutableArrayMap<R, V> {
/*     */     private final int columnIndex;
/*     */     
/*     */     Column(int columnIndex) {
/* 198 */       super(DenseImmutableTable.this.columnCounts[columnIndex]);
/* 199 */       this.columnIndex = columnIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableMap<R, Integer> keyToIndex() {
/* 204 */       return DenseImmutableTable.this.rowKeyToIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     V getValue(int keyIndex) {
/* 210 */       return (V)DenseImmutableTable.this.values[keyIndex][this.columnIndex];
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 215 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @J2ktIncompatible
/*     */     @GwtIncompatible
/*     */     Object writeReplace() {
/* 224 */       return super.writeReplace();
/*     */     }
/*     */   }
/*     */   
/*     */   private final class RowMap
/*     */     extends ImmutableArrayMap<R, ImmutableMap<C, V>> {
/*     */     private RowMap() {
/* 231 */       super(DenseImmutableTable.this.rowCounts.length);
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableMap<R, Integer> keyToIndex() {
/* 236 */       return DenseImmutableTable.this.rowKeyToIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableMap<C, V> getValue(int keyIndex) {
/* 241 */       return new DenseImmutableTable.Row(keyIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 246 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @J2ktIncompatible
/*     */     @GwtIncompatible
/*     */     Object writeReplace() {
/* 255 */       return super.writeReplace();
/*     */     }
/*     */   }
/*     */   
/*     */   private final class ColumnMap
/*     */     extends ImmutableArrayMap<C, ImmutableMap<R, V>> {
/*     */     private ColumnMap() {
/* 262 */       super(DenseImmutableTable.this.columnCounts.length);
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableMap<C, Integer> keyToIndex() {
/* 267 */       return DenseImmutableTable.this.columnKeyToIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableMap<R, V> getValue(int keyIndex) {
/* 272 */       return new DenseImmutableTable.Column(keyIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 277 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @J2ktIncompatible
/*     */     @GwtIncompatible
/*     */     Object writeReplace() {
/* 286 */       return super.writeReplace();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMap<C, Map<R, V>> columnMap() {
/* 293 */     ImmutableMap<C, ImmutableMap<R, V>> columnMap = this.columnMap;
/* 294 */     return ImmutableMap.copyOf((Map)columnMap);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMap<R, Map<C, V>> rowMap() {
/* 300 */     ImmutableMap<R, ImmutableMap<C, V>> rowMap = this.rowMap;
/* 301 */     return ImmutableMap.copyOf((Map)rowMap);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public V get(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
/* 307 */     Integer rowIndex = this.rowKeyToIndex.get(rowKey);
/* 308 */     Integer columnIndex = this.columnKeyToIndex.get(columnKey);
/* 309 */     return (rowIndex == null || columnIndex == null) ? null : this.values[rowIndex.intValue()][columnIndex.intValue()];
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 314 */     return this.cellRowIndices.length;
/*     */   }
/*     */ 
/*     */   
/*     */   Table.Cell<R, C, V> getCell(int index) {
/* 319 */     int rowIndex = this.cellRowIndices[index];
/* 320 */     int columnIndex = this.cellColumnIndices[index];
/* 321 */     R rowKey = rowKeySet().asList().get(rowIndex);
/* 322 */     C columnKey = columnKeySet().asList().get(columnIndex);
/*     */     
/* 324 */     V value = Objects.requireNonNull(this.values[rowIndex][columnIndex]);
/* 325 */     return cellOf(rowKey, columnKey, value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   V getValue(int index) {
/* 331 */     return Objects.requireNonNull(this.values[this.cellRowIndices[index]][this.cellColumnIndices[index]]);
/*     */   }
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   Object writeReplace() {
/* 338 */     return ImmutableTable.SerializedForm.create(this, this.cellRowIndices, this.cellColumnIndices);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/DenseImmutableTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */