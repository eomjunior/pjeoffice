/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.BinaryOperator;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ final class TableCollectors
/*     */ {
/*     */   static <T, R, C, V> Collector<T, ?, ImmutableTable<R, C, V>> toImmutableTable(Function<? super T, ? extends R> rowFunction, Function<? super T, ? extends C> columnFunction, Function<? super T, ? extends V> valueFunction) {
/*  41 */     Preconditions.checkNotNull(rowFunction, "rowFunction");
/*  42 */     Preconditions.checkNotNull(columnFunction, "columnFunction");
/*  43 */     Preconditions.checkNotNull(valueFunction, "valueFunction");
/*  44 */     return Collector.of(Builder::new, (builder, t) -> builder.put(rowFunction.apply(t), columnFunction.apply(t), valueFunction.apply(t)), ImmutableTable.Builder::combine, ImmutableTable.Builder::build, new Collector.Characteristics[0]);
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
/*     */   static <T, R, C, V> Collector<T, ?, ImmutableTable<R, C, V>> toImmutableTable(Function<? super T, ? extends R> rowFunction, Function<? super T, ? extends C> columnFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction) {
/*  59 */     Preconditions.checkNotNull(rowFunction, "rowFunction");
/*  60 */     Preconditions.checkNotNull(columnFunction, "columnFunction");
/*  61 */     Preconditions.checkNotNull(valueFunction, "valueFunction");
/*  62 */     Preconditions.checkNotNull(mergeFunction, "mergeFunction");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  70 */     return Collector.of(() -> new ImmutableTableCollectorState<>(), (state, input) -> state.put(rowFunction.apply(input), columnFunction.apply(input), valueFunction.apply(input), mergeFunction), (s1, s2) -> s1.combine(s2, mergeFunction), state -> state.toTable(), new Collector.Characteristics[0]);
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
/*     */   static <T, R, C, V, I extends Table<R, C, V>> Collector<T, ?, I> toTable(Function<? super T, ? extends R> rowFunction, Function<? super T, ? extends C> columnFunction, Function<? super T, ? extends V> valueFunction, Supplier<I> tableSupplier) {
/*  93 */     return toTable(rowFunction, columnFunction, valueFunction, (v1, v2) -> { throw new IllegalStateException("Conflicting values " + v1 + " and " + v2); }tableSupplier);
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
/*     */   static <T, R, C, V, I extends Table<R, C, V>> Collector<T, ?, I> toTable(Function<? super T, ? extends R> rowFunction, Function<? super T, ? extends C> columnFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction, Supplier<I> tableSupplier) {
/* 115 */     Preconditions.checkNotNull(rowFunction);
/* 116 */     Preconditions.checkNotNull(columnFunction);
/* 117 */     Preconditions.checkNotNull(valueFunction);
/* 118 */     Preconditions.checkNotNull(mergeFunction);
/* 119 */     Preconditions.checkNotNull(tableSupplier);
/* 120 */     return (Collector)Collector.of(tableSupplier, (table, input) -> mergeTables(table, rowFunction.apply(input), columnFunction.apply(input), valueFunction.apply(input), mergeFunction), (table1, table2) -> { for (Table.Cell<R, C, V> cell2 : (Iterable<Table.Cell<R, C, V>>)table2.cellSet()) mergeTables(table1, cell2.getRowKey(), cell2.getColumnKey(), cell2.getValue(), mergeFunction);  return table1; }new Collector.Characteristics[0]);
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
/*     */   private static final class ImmutableTableCollectorState<R, C, V>
/*     */   {
/* 139 */     final List<TableCollectors.MutableCell<R, C, V>> insertionOrder = new ArrayList<>();
/* 140 */     final Table<R, C, TableCollectors.MutableCell<R, C, V>> table = HashBasedTable.create();
/*     */     
/*     */     void put(R row, C column, V value, BinaryOperator<V> merger) {
/* 143 */       TableCollectors.MutableCell<R, C, V> oldCell = this.table.get(row, column);
/* 144 */       if (oldCell == null) {
/* 145 */         TableCollectors.MutableCell<R, C, V> cell = new TableCollectors.MutableCell<>(row, column, value);
/* 146 */         this.insertionOrder.add(cell);
/* 147 */         this.table.put(row, column, cell);
/*     */       } else {
/* 149 */         oldCell.merge(value, merger);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableTableCollectorState<R, C, V> combine(ImmutableTableCollectorState<R, C, V> other, BinaryOperator<V> merger) {
/* 155 */       for (TableCollectors.MutableCell<R, C, V> cell : other.insertionOrder) {
/* 156 */         put(cell.getRowKey(), cell.getColumnKey(), cell.getValue(), merger);
/*     */       }
/* 158 */       return this;
/*     */     }
/*     */     
/*     */     ImmutableTable<R, C, V> toTable() {
/* 162 */       return ImmutableTable.copyOf((Iterable)this.insertionOrder);
/*     */     }
/*     */     
/*     */     private ImmutableTableCollectorState() {} }
/*     */   
/*     */   private static final class MutableCell<R, C, V> extends Tables.AbstractCell<R, C, V> { private final R row;
/*     */     private final C column;
/*     */     private V value;
/*     */     
/*     */     MutableCell(R row, C column, V value) {
/* 172 */       this.row = (R)Preconditions.checkNotNull(row, "row");
/* 173 */       this.column = (C)Preconditions.checkNotNull(column, "column");
/* 174 */       this.value = (V)Preconditions.checkNotNull(value, "value");
/*     */     }
/*     */ 
/*     */     
/*     */     public R getRowKey() {
/* 179 */       return this.row;
/*     */     }
/*     */ 
/*     */     
/*     */     public C getColumnKey() {
/* 184 */       return this.column;
/*     */     }
/*     */ 
/*     */     
/*     */     public V getValue() {
/* 189 */       return this.value;
/*     */     }
/*     */     
/*     */     void merge(V value, BinaryOperator<V> mergeFunction) {
/* 193 */       Preconditions.checkNotNull(value, "value");
/* 194 */       this.value = (V)Preconditions.checkNotNull(mergeFunction.apply(this.value, value), "mergeFunction.apply");
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <R, C, V> void mergeTables(Table<R, C, V> table, @ParametricNullness R row, @ParametricNullness C column, @ParametricNullness V value, BinaryOperator<V> mergeFunction) {
/* 206 */     Preconditions.checkNotNull(value);
/* 207 */     V oldValue = table.get(row, column);
/* 208 */     if (oldValue == null) {
/* 209 */       table.put(row, column, value);
/*     */     } else {
/* 211 */       V newValue = mergeFunction.apply(oldValue, value);
/* 212 */       if (newValue == null) {
/* 213 */         table.remove(row, column);
/*     */       } else {
/* 215 */         table.put(row, column, newValue);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/TableCollectors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */