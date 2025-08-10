/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.BinaryOperator;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Tables
/*     */ {
/*     */   public static <T, R, C, V, I extends Table<R, C, V>> Collector<T, ?, I> toTable(Function<? super T, ? extends R> rowFunction, Function<? super T, ? extends C> columnFunction, Function<? super T, ? extends V> valueFunction, Supplier<I> tableSupplier) {
/*  80 */     return TableCollectors.toTable(rowFunction, columnFunction, valueFunction, tableSupplier);
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
/*     */   
/*     */   public static <T, R, C, V, I extends Table<R, C, V>> Collector<T, ?, I> toTable(Function<? super T, ? extends R> rowFunction, Function<? super T, ? extends C> columnFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction, Supplier<I> tableSupplier) {
/* 110 */     return TableCollectors.toTable(rowFunction, columnFunction, valueFunction, mergeFunction, tableSupplier);
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
/*     */   public static <R, C, V> Table.Cell<R, C, V> immutableCell(@ParametricNullness R rowKey, @ParametricNullness C columnKey, @ParametricNullness V value) {
/* 128 */     return new ImmutableCell<>(rowKey, columnKey, value);
/*     */   }
/*     */   
/*     */   static final class ImmutableCell<R, C, V>
/*     */     extends AbstractCell<R, C, V> implements Serializable {
/*     */     @ParametricNullness
/*     */     private final R rowKey;
/*     */     @ParametricNullness
/*     */     private final C columnKey;
/*     */     @ParametricNullness
/*     */     private final V value;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ImmutableCell(@ParametricNullness R rowKey, @ParametricNullness C columnKey, @ParametricNullness V value) {
/* 142 */       this.rowKey = rowKey;
/* 143 */       this.columnKey = columnKey;
/* 144 */       this.value = value;
/*     */     }
/*     */ 
/*     */     
/*     */     @ParametricNullness
/*     */     public R getRowKey() {
/* 150 */       return this.rowKey;
/*     */     }
/*     */ 
/*     */     
/*     */     @ParametricNullness
/*     */     public C getColumnKey() {
/* 156 */       return this.columnKey;
/*     */     }
/*     */ 
/*     */     
/*     */     @ParametricNullness
/*     */     public V getValue() {
/* 162 */       return this.value;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static abstract class AbstractCell<R, C, V>
/*     */     implements Table.Cell<R, C, V>
/*     */   {
/*     */     public boolean equals(@CheckForNull Object obj) {
/* 176 */       if (obj == this) {
/* 177 */         return true;
/*     */       }
/* 179 */       if (obj instanceof Table.Cell) {
/* 180 */         Table.Cell<?, ?, ?> other = (Table.Cell<?, ?, ?>)obj;
/* 181 */         return (Objects.equal(getRowKey(), other.getRowKey()) && 
/* 182 */           Objects.equal(getColumnKey(), other.getColumnKey()) && 
/* 183 */           Objects.equal(getValue(), other.getValue()));
/*     */       } 
/* 185 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 190 */       return Objects.hashCode(new Object[] { getRowKey(), getColumnKey(), getValue() });
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 195 */       return "(" + getRowKey() + "," + getColumnKey() + ")=" + getValue();
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
/*     */   public static <R, C, V> Table<C, R, V> transpose(Table<R, C, V> table) {
/* 213 */     return (table instanceof TransposeTable) ? 
/* 214 */       ((TransposeTable)table).original : 
/* 215 */       new TransposeTable<>(table);
/*     */   }
/*     */   
/*     */   private static class TransposeTable<C, R, V>
/*     */     extends AbstractTable<C, R, V>
/*     */   {
/*     */     final Table<R, C, V> original;
/*     */     
/*     */     TransposeTable(Table<R, C, V> original) {
/* 224 */       this.original = (Table<R, C, V>)Preconditions.checkNotNull(original);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 229 */       this.original.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, V> column(@ParametricNullness R columnKey) {
/* 234 */       return this.original.row(columnKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<R> columnKeySet() {
/* 239 */       return this.original.rowKeySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<R, Map<C, V>> columnMap() {
/* 244 */       return this.original.rowMap();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
/* 249 */       return this.original.contains(columnKey, rowKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsColumn(@CheckForNull Object columnKey) {
/* 254 */       return this.original.containsRow(columnKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsRow(@CheckForNull Object rowKey) {
/* 259 */       return this.original.containsColumn(rowKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsValue(@CheckForNull Object value) {
/* 264 */       return this.original.containsValue(value);
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public V get(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
/* 270 */       return this.original.get(columnKey, rowKey);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public V put(@ParametricNullness C rowKey, @ParametricNullness R columnKey, @ParametricNullness V value) {
/* 279 */       return this.original.put(columnKey, rowKey, value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void putAll(Table<? extends C, ? extends R, ? extends V> table) {
/* 284 */       this.original.putAll(Tables.transpose(table));
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public V remove(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
/* 290 */       return this.original.remove(columnKey, rowKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<R, V> row(@ParametricNullness C rowKey) {
/* 295 */       return this.original.column(rowKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<C> rowKeySet() {
/* 300 */       return this.original.columnKeySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, Map<R, V>> rowMap() {
/* 305 */       return this.original.columnMap();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 310 */       return this.original.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public Collection<V> values() {
/* 315 */       return this.original.values();
/*     */     }
/*     */ 
/*     */     
/* 319 */     private static final Function TRANSPOSE_CELL = new Function<Table.Cell<?, ?, ?>, Table.Cell<?, ?, ?>>()
/*     */       {
/*     */         public Table.Cell<?, ?, ?> apply(Table.Cell<?, ?, ?> cell)
/*     */         {
/* 323 */           return Tables.immutableCell(cell.getColumnKey(), cell.getRowKey(), cell.getValue());
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */     
/*     */     Iterator<Table.Cell<C, R, V>> cellIterator() {
/* 330 */       return Iterators.transform(this.original
/* 331 */           .cellSet().iterator(), TRANSPOSE_CELL);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Spliterator<Table.Cell<C, R, V>> cellSpliterator() {
/* 337 */       return CollectSpliterators.map(this.original
/* 338 */           .cellSet().spliterator(), (Function<?, ? extends Table.Cell<C, R, V>>)TRANSPOSE_CELL);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <R, C, V> Table<R, C, V> newCustomTable(Map<R, Map<C, V>> backingMap, Supplier<? extends Map<C, V>> factory) {
/* 383 */     Preconditions.checkArgument(backingMap.isEmpty());
/* 384 */     Preconditions.checkNotNull(factory);
/*     */     
/* 386 */     return new StandardTable<>(backingMap, factory);
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
/*     */ 
/*     */   
/*     */   public static <R, C, V1, V2> Table<R, C, V2> transformValues(Table<R, C, V1> fromTable, Function<? super V1, V2> function) {
/* 417 */     return new TransformedTable<>(fromTable, function);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class TransformedTable<R, C, V1, V2>
/*     */     extends AbstractTable<R, C, V2>
/*     */   {
/*     */     final Table<R, C, V1> fromTable;
/*     */     
/*     */     final Function<? super V1, V2> function;
/*     */ 
/*     */     
/*     */     TransformedTable(Table<R, C, V1> fromTable, Function<? super V1, V2> function) {
/* 430 */       this.fromTable = (Table<R, C, V1>)Preconditions.checkNotNull(fromTable);
/* 431 */       this.function = (Function<? super V1, V2>)Preconditions.checkNotNull(function);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
/* 436 */       return this.fromTable.contains(rowKey, columnKey);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public V2 get(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
/* 445 */       return contains(rowKey, columnKey) ? 
/* 446 */         (V2)this.function.apply(NullnessCasts.uncheckedCastNullableTToT(this.fromTable.get(rowKey, columnKey))) : 
/* 447 */         null;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 452 */       return this.fromTable.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 457 */       this.fromTable.clear();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public V2 put(@ParametricNullness R rowKey, @ParametricNullness C columnKey, @ParametricNullness V2 value) {
/* 466 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public void putAll(Table<? extends R, ? extends C, ? extends V2> table) {
/* 471 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public V2 remove(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
/* 477 */       return contains(rowKey, columnKey) ? 
/*     */         
/* 479 */         (V2)this.function.apply(NullnessCasts.uncheckedCastNullableTToT(this.fromTable.remove(rowKey, columnKey))) : 
/* 480 */         null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, V2> row(@ParametricNullness R rowKey) {
/* 485 */       return Maps.transformValues(this.fromTable.row(rowKey), this.function);
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<R, V2> column(@ParametricNullness C columnKey) {
/* 490 */       return Maps.transformValues(this.fromTable.column(columnKey), this.function);
/*     */     }
/*     */     
/*     */     Function<Table.Cell<R, C, V1>, Table.Cell<R, C, V2>> cellFunction() {
/* 494 */       return new Function<Table.Cell<R, C, V1>, Table.Cell<R, C, V2>>()
/*     */         {
/*     */           public Table.Cell<R, C, V2> apply(Table.Cell<R, C, V1> cell) {
/* 497 */             return Tables.immutableCell(cell
/* 498 */                 .getRowKey(), cell.getColumnKey(), (V2)Tables.TransformedTable.this.function.apply(cell.getValue()));
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     Iterator<Table.Cell<R, C, V2>> cellIterator() {
/* 505 */       return Iterators.transform(this.fromTable.cellSet().iterator(), cellFunction());
/*     */     }
/*     */ 
/*     */     
/*     */     Spliterator<Table.Cell<R, C, V2>> cellSpliterator() {
/* 510 */       return CollectSpliterators.map(this.fromTable.cellSet().spliterator(), (Function<? super Table.Cell<R, C, V1>, ? extends Table.Cell<R, C, V2>>)cellFunction());
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<R> rowKeySet() {
/* 515 */       return this.fromTable.rowKeySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<C> columnKeySet() {
/* 520 */       return this.fromTable.columnKeySet();
/*     */     }
/*     */ 
/*     */     
/*     */     Collection<V2> createValues() {
/* 525 */       return Collections2.transform(this.fromTable.values(), this.function);
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<R, Map<C, V2>> rowMap() {
/* 530 */       Function<Map<C, V1>, Map<C, V2>> rowFunction = new Function<Map<C, V1>, Map<C, V2>>()
/*     */         {
/*     */           public Map<C, V2> apply(Map<C, V1> row)
/*     */           {
/* 534 */             return Maps.transformValues(row, Tables.TransformedTable.this.function);
/*     */           }
/*     */         };
/* 537 */       return Maps.transformValues(this.fromTable.rowMap(), rowFunction);
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, Map<R, V2>> columnMap() {
/* 542 */       Function<Map<R, V1>, Map<R, V2>> columnFunction = new Function<Map<R, V1>, Map<R, V2>>()
/*     */         {
/*     */           public Map<R, V2> apply(Map<R, V1> column)
/*     */           {
/* 546 */             return Maps.transformValues(column, Tables.TransformedTable.this.function);
/*     */           }
/*     */         };
/* 549 */       return Maps.transformValues(this.fromTable.columnMap(), columnFunction);
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
/*     */   public static <R, C, V> Table<R, C, V> unmodifiableTable(Table<? extends R, ? extends C, ? extends V> table) {
/* 567 */     return new UnmodifiableTable<>(table);
/*     */   }
/*     */   
/*     */   private static class UnmodifiableTable<R, C, V>
/*     */     extends ForwardingTable<R, C, V> implements Serializable {
/*     */     final Table<? extends R, ? extends C, ? extends V> delegate;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     UnmodifiableTable(Table<? extends R, ? extends C, ? extends V> delegate) {
/* 576 */       this.delegate = (Table<? extends R, ? extends C, ? extends V>)Preconditions.checkNotNull(delegate);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected Table<R, C, V> delegate() {
/* 582 */       return (Table)this.delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<Table.Cell<R, C, V>> cellSet() {
/* 587 */       return Collections.unmodifiableSet(super.cellSet());
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 592 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<R, V> column(@ParametricNullness C columnKey) {
/* 597 */       return Collections.unmodifiableMap(super.column(columnKey));
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<C> columnKeySet() {
/* 602 */       return Collections.unmodifiableSet(super.columnKeySet());
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, Map<R, V>> columnMap() {
/* 607 */       Function<Map<R, V>, Map<R, V>> wrapper = Tables.unmodifiableWrapper();
/* 608 */       return Collections.unmodifiableMap(Maps.transformValues(super.columnMap(), wrapper));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public V put(@ParametricNullness R rowKey, @ParametricNullness C columnKey, @ParametricNullness V value) {
/* 617 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
/* 622 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public V remove(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
/* 628 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, V> row(@ParametricNullness R rowKey) {
/* 633 */       return Collections.unmodifiableMap(super.row(rowKey));
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<R> rowKeySet() {
/* 638 */       return Collections.unmodifiableSet(super.rowKeySet());
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<R, Map<C, V>> rowMap() {
/* 643 */       Function<Map<C, V>, Map<C, V>> wrapper = Tables.unmodifiableWrapper();
/* 644 */       return Collections.unmodifiableMap(Maps.transformValues(super.rowMap(), wrapper));
/*     */     }
/*     */ 
/*     */     
/*     */     public Collection<V> values() {
/* 649 */       return Collections.unmodifiableCollection(super.values());
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <R, C, V> RowSortedTable<R, C, V> unmodifiableRowSortedTable(RowSortedTable<R, ? extends C, ? extends V> table) {
/* 675 */     return new UnmodifiableRowSortedMap<>(table);
/*     */   }
/*     */   
/*     */   private static final class UnmodifiableRowSortedMap<R, C, V>
/*     */     extends UnmodifiableTable<R, C, V> implements RowSortedTable<R, C, V> {
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public UnmodifiableRowSortedMap(RowSortedTable<R, ? extends C, ? extends V> delegate) {
/* 683 */       super(delegate);
/*     */     }
/*     */ 
/*     */     
/*     */     protected RowSortedTable<R, C, V> delegate() {
/* 688 */       return (RowSortedTable<R, C, V>)super.delegate();
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedMap<R, Map<C, V>> rowMap() {
/* 693 */       Function<Map<C, V>, Map<C, V>> wrapper = Tables.unmodifiableWrapper();
/* 694 */       return Collections.unmodifiableSortedMap(Maps.transformValues(delegate().rowMap(), wrapper));
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedSet<R> rowKeySet() {
/* 699 */       return Collections.unmodifiableSortedSet(delegate().rowKeySet());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <K, V> Function<Map<K, V>, Map<K, V>> unmodifiableWrapper() {
/* 708 */     return (Function)UNMODIFIABLE_WRAPPER;
/*     */   }
/*     */   
/* 711 */   private static final Function<? extends Map<?, ?>, ? extends Map<?, ?>> UNMODIFIABLE_WRAPPER = new Function<Map<Object, Object>, Map<Object, Object>>()
/*     */     {
/*     */       public Map<Object, Object> apply(Map<Object, Object> input)
/*     */       {
/* 715 */         return Collections.unmodifiableMap(input);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <R, C, V> Table<R, C, V> synchronizedTable(Table<R, C, V> table) {
/* 750 */     return Synchronized.table(table, null);
/*     */   }
/*     */   
/*     */   static boolean equalsImpl(Table<?, ?, ?> table, @CheckForNull Object obj) {
/* 754 */     if (obj == table)
/* 755 */       return true; 
/* 756 */     if (obj instanceof Table) {
/* 757 */       Table<?, ?, ?> that = (Table<?, ?, ?>)obj;
/* 758 */       return table.cellSet().equals(that.cellSet());
/*     */     } 
/* 760 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/Tables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */