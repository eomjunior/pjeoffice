/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.DoNotCall;
/*     */ import com.google.errorprone.annotations.DoNotMock;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
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
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ public abstract class ImmutableTable<R, C, V>
/*     */   extends AbstractTable<R, C, V>
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -889275714L;
/*     */   
/*     */   public static <T, R, C, V> Collector<T, ?, ImmutableTable<R, C, V>> toImmutableTable(Function<? super T, ? extends R> rowFunction, Function<? super T, ? extends C> columnFunction, Function<? super T, ? extends V> valueFunction) {
/*  72 */     return TableCollectors.toImmutableTable(rowFunction, columnFunction, valueFunction);
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
/*     */   public static <T, R, C, V> Collector<T, ?, ImmutableTable<R, C, V>> toImmutableTable(Function<? super T, ? extends R> rowFunction, Function<? super T, ? extends C> columnFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction) {
/*  92 */     return TableCollectors.toImmutableTable(rowFunction, columnFunction, valueFunction, mergeFunction);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <R, C, V> ImmutableTable<R, C, V> of() {
/* 103 */     return (ImmutableTable)SparseImmutableTable.EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <R, C, V> ImmutableTable<R, C, V> of(R rowKey, C columnKey, V value) {
/* 108 */     return new SingletonImmutableTable<>(rowKey, columnKey, value);
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
/*     */   public static <R, C, V> ImmutableTable<R, C, V> copyOf(Table<? extends R, ? extends C, ? extends V> table) {
/* 126 */     if (table instanceof ImmutableTable) {
/*     */       
/* 128 */       ImmutableTable<R, C, V> parameterizedTable = (ImmutableTable)table;
/* 129 */       return parameterizedTable;
/*     */     } 
/* 131 */     return copyOf(table.cellSet());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static <R, C, V> ImmutableTable<R, C, V> copyOf(Iterable<? extends Table.Cell<? extends R, ? extends C, ? extends V>> cells) {
/* 137 */     Builder<R, C, V> builder = builder();
/* 138 */     for (Table.Cell<? extends R, ? extends C, ? extends V> cell : cells) {
/* 139 */       builder.put(cell);
/*     */     }
/* 141 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <R, C, V> Builder<R, C, V> builder() {
/* 149 */     return new Builder<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <R, C, V> Table.Cell<R, C, V> cellOf(R rowKey, C columnKey, V value) {
/* 157 */     return Tables.immutableCell(
/* 158 */         (R)Preconditions.checkNotNull(rowKey, "rowKey"), 
/* 159 */         (C)Preconditions.checkNotNull(columnKey, "columnKey"), 
/* 160 */         (V)Preconditions.checkNotNull(value, "value"));
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
/*     */   @DoNotMock
/*     */   public static final class Builder<R, C, V>
/*     */   {
/* 191 */     private final List<Table.Cell<R, C, V>> cells = Lists.newArrayList();
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     private Comparator<? super R> rowComparator;
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     private Comparator<? super C> columnComparator;
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<R, C, V> orderRowsBy(Comparator<? super R> rowComparator) {
/* 204 */       this.rowComparator = (Comparator<? super R>)Preconditions.checkNotNull(rowComparator, "rowComparator");
/* 205 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<R, C, V> orderColumnsBy(Comparator<? super C> columnComparator) {
/* 211 */       this.columnComparator = (Comparator<? super C>)Preconditions.checkNotNull(columnComparator, "columnComparator");
/* 212 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<R, C, V> put(R rowKey, C columnKey, V value) {
/* 221 */       this.cells.add(ImmutableTable.cellOf(rowKey, columnKey, value));
/* 222 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<R, C, V> put(Table.Cell<? extends R, ? extends C, ? extends V> cell) {
/* 231 */       if (cell instanceof Tables.ImmutableCell) {
/* 232 */         Preconditions.checkNotNull(cell.getRowKey(), "row");
/* 233 */         Preconditions.checkNotNull(cell.getColumnKey(), "column");
/* 234 */         Preconditions.checkNotNull(cell.getValue(), "value");
/*     */         
/* 236 */         Table.Cell<? extends R, ? extends C, ? extends V> cell1 = cell;
/* 237 */         this.cells.add(cell1);
/*     */       } else {
/* 239 */         put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
/*     */       } 
/* 241 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<R, C, V> putAll(Table<? extends R, ? extends C, ? extends V> table) {
/* 252 */       for (Table.Cell<? extends R, ? extends C, ? extends V> cell : table.cellSet()) {
/* 253 */         put(cell);
/*     */       }
/* 255 */       return this;
/*     */     }
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<R, C, V> combine(Builder<R, C, V> other) {
/* 260 */       this.cells.addAll(other.cells);
/* 261 */       return this;
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
/*     */     public ImmutableTable<R, C, V> build() {
/* 274 */       return buildOrThrow();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableTable<R, C, V> buildOrThrow() {
/* 285 */       int size = this.cells.size();
/* 286 */       switch (size) {
/*     */         case 0:
/* 288 */           return ImmutableTable.of();
/*     */         case 1:
/* 290 */           return new SingletonImmutableTable<>(Iterables.<Table.Cell<R, C, V>>getOnlyElement(this.cells));
/*     */       } 
/* 292 */       return RegularImmutableTable.forCells(this.cells, this.rowComparator, this.columnComparator);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<Table.Cell<R, C, V>> cellSet() {
/* 301 */     return (ImmutableSet<Table.Cell<R, C, V>>)super.cellSet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final UnmodifiableIterator<Table.Cell<R, C, V>> cellIterator() {
/* 309 */     throw new AssertionError("should never be called");
/*     */   }
/*     */ 
/*     */   
/*     */   final Spliterator<Table.Cell<R, C, V>> cellSpliterator() {
/* 314 */     throw new AssertionError("should never be called");
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableCollection<V> values() {
/* 319 */     return (ImmutableCollection<V>)super.values();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final Iterator<V> valuesIterator() {
/* 327 */     throw new AssertionError("should never be called");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMap<R, V> column(C columnKey) {
/* 337 */     Preconditions.checkNotNull(columnKey, "columnKey");
/* 338 */     return (ImmutableMap<R, V>)MoreObjects.firstNonNull(
/* 339 */         columnMap().get(columnKey), ImmutableMap.of());
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<C> columnKeySet() {
/* 344 */     return columnMap().keySet();
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
/*     */   public ImmutableMap<C, V> row(R rowKey) {
/* 363 */     Preconditions.checkNotNull(rowKey, "rowKey");
/* 364 */     return (ImmutableMap<C, V>)MoreObjects.firstNonNull(
/* 365 */         rowMap().get(rowKey), ImmutableMap.of());
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<R> rowKeySet() {
/* 370 */     return rowMap().keySet();
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
/*     */   public boolean contains(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
/* 384 */     return (get(rowKey, columnKey) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(@CheckForNull Object value) {
/* 389 */     return values().contains(value);
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
/* 402 */     throw new UnsupportedOperationException();
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
/*     */   public final V put(R rowKey, C columnKey, V value) {
/* 417 */     throw new UnsupportedOperationException();
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
/*     */   public final void putAll(Table<? extends R, ? extends C, ? extends V> table) {
/* 430 */     throw new UnsupportedOperationException();
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
/*     */   public final V remove(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
/* 445 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   static final class SerializedForm
/*     */     implements Serializable
/*     */   {
/*     */     private final Object[] rowKeys;
/*     */     
/*     */     private final Object[] columnKeys;
/*     */     
/*     */     private final Object[] cellValues;
/*     */     
/*     */     private final int[] cellRowIndices;
/*     */     
/*     */     private final int[] cellColumnIndices;
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */     
/*     */     private SerializedForm(Object[] rowKeys, Object[] columnKeys, Object[] cellValues, int[] cellRowIndices, int[] cellColumnIndices) {
/* 466 */       this.rowKeys = rowKeys;
/* 467 */       this.columnKeys = columnKeys;
/* 468 */       this.cellValues = cellValues;
/* 469 */       this.cellRowIndices = cellRowIndices;
/* 470 */       this.cellColumnIndices = cellColumnIndices;
/*     */     }
/*     */ 
/*     */     
/*     */     static SerializedForm create(ImmutableTable<?, ?, ?> table, int[] cellRowIndices, int[] cellColumnIndices) {
/* 475 */       return new SerializedForm(table
/* 476 */           .rowKeySet().toArray(), table
/* 477 */           .columnKeySet().toArray(), table
/* 478 */           .values().toArray(), cellRowIndices, cellColumnIndices);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Object readResolve() {
/* 484 */       if (this.cellValues.length == 0) {
/* 485 */         return ImmutableTable.of();
/*     */       }
/* 487 */       if (this.cellValues.length == 1) {
/* 488 */         return ImmutableTable.of(this.rowKeys[0], this.columnKeys[0], this.cellValues[0]);
/*     */       }
/* 490 */       ImmutableList.Builder<Table.Cell<Object, Object, Object>> cellListBuilder = new ImmutableList.Builder<>(this.cellValues.length);
/*     */       
/* 492 */       for (int i = 0; i < this.cellValues.length; i++) {
/* 493 */         cellListBuilder.add(
/* 494 */             ImmutableTable.cellOf(this.rowKeys[this.cellRowIndices[i]], this.columnKeys[this.cellColumnIndices[i]], this.cellValues[i]));
/*     */       }
/* 496 */       return RegularImmutableTable.forOrderedComponents(cellListBuilder
/* 497 */           .build(), ImmutableSet.copyOf(this.rowKeys), ImmutableSet.copyOf(this.columnKeys));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 510 */     throw new InvalidObjectException("Use SerializedForm");
/*     */   }
/*     */   
/*     */   abstract ImmutableSet<Table.Cell<R, C, V>> createCellSet();
/*     */   
/*     */   abstract ImmutableCollection<V> createValues();
/*     */   
/*     */   public abstract ImmutableMap<C, Map<R, V>> columnMap();
/*     */   
/*     */   public abstract ImmutableMap<R, Map<C, V>> rowMap();
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   abstract Object writeReplace();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ImmutableTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */