/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ abstract class AbstractTable<R, C, V>
/*     */   implements Table<R, C, V>
/*     */ {
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   private transient Set<Table.Cell<R, C, V>> cellSet;
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   private transient Collection<V> values;
/*     */   
/*     */   public boolean containsRow(@CheckForNull Object rowKey) {
/*  44 */     return Maps.safeContainsKey(rowMap(), rowKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsColumn(@CheckForNull Object columnKey) {
/*  49 */     return Maps.safeContainsKey(columnMap(), columnKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<R> rowKeySet() {
/*  54 */     return rowMap().keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<C> columnKeySet() {
/*  59 */     return columnMap().keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(@CheckForNull Object value) {
/*  64 */     for (Map<C, V> row : rowMap().values()) {
/*  65 */       if (row.containsValue(value)) {
/*  66 */         return true;
/*     */       }
/*     */     } 
/*  69 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
/*  74 */     Map<C, V> row = Maps.<Map<C, V>>safeGet(rowMap(), rowKey);
/*  75 */     return (row != null && Maps.safeContainsKey(row, columnKey));
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public V get(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
/*  81 */     Map<C, V> row = Maps.<Map<C, V>>safeGet(rowMap(), rowKey);
/*  82 */     return (row == null) ? null : Maps.<V>safeGet(row, columnKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  87 */     return (size() == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  92 */     Iterators.clear(cellSet().iterator());
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   public V remove(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
/*  99 */     Map<C, V> row = Maps.<Map<C, V>>safeGet(rowMap(), rowKey);
/* 100 */     return (row == null) ? null : Maps.<V>safeRemove(row, columnKey);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   public V put(@ParametricNullness R rowKey, @ParametricNullness C columnKey, @ParametricNullness V value) {
/* 108 */     return row(rowKey).put(columnKey, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
/* 113 */     for (Table.Cell<? extends R, ? extends C, ? extends V> cell : table.cellSet()) {
/* 114 */       put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Table.Cell<R, C, V>> cellSet() {
/* 122 */     Set<Table.Cell<R, C, V>> result = this.cellSet;
/* 123 */     return (result == null) ? (this.cellSet = createCellSet()) : result;
/*     */   }
/*     */   
/*     */   Set<Table.Cell<R, C, V>> createCellSet() {
/* 127 */     return new CellSet();
/*     */   }
/*     */   
/*     */   abstract Iterator<Table.Cell<R, C, V>> cellIterator();
/*     */   
/*     */   abstract Spliterator<Table.Cell<R, C, V>> cellSpliterator();
/*     */   
/*     */   class CellSet
/*     */     extends AbstractSet<Table.Cell<R, C, V>>
/*     */   {
/*     */     public boolean contains(@CheckForNull Object o) {
/* 138 */       if (o instanceof Table.Cell) {
/* 139 */         Table.Cell<?, ?, ?> cell = (Table.Cell<?, ?, ?>)o;
/* 140 */         Map<C, V> row = Maps.<Map<C, V>>safeGet(AbstractTable.this.rowMap(), cell.getRowKey());
/* 141 */         return (row != null && 
/* 142 */           Collections2.safeContains(row
/* 143 */             .entrySet(), Maps.immutableEntry(cell.getColumnKey(), cell.getValue())));
/*     */       } 
/* 145 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(@CheckForNull Object o) {
/* 150 */       if (o instanceof Table.Cell) {
/* 151 */         Table.Cell<?, ?, ?> cell = (Table.Cell<?, ?, ?>)o;
/* 152 */         Map<C, V> row = Maps.<Map<C, V>>safeGet(AbstractTable.this.rowMap(), cell.getRowKey());
/* 153 */         return (row != null && 
/* 154 */           Collections2.safeRemove(row
/* 155 */             .entrySet(), Maps.immutableEntry(cell.getColumnKey(), cell.getValue())));
/*     */       } 
/* 157 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 162 */       AbstractTable.this.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<Table.Cell<R, C, V>> iterator() {
/* 167 */       return AbstractTable.this.cellIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator<Table.Cell<R, C, V>> spliterator() {
/* 172 */       return AbstractTable.this.cellSpliterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 177 */       return AbstractTable.this.size();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 185 */     Collection<V> result = this.values;
/* 186 */     return (result == null) ? (this.values = createValues()) : result;
/*     */   }
/*     */   
/*     */   Collection<V> createValues() {
/* 190 */     return new Values();
/*     */   }
/*     */   
/*     */   Iterator<V> valuesIterator() {
/* 194 */     return new TransformedIterator<Table.Cell<R, C, V>, V>(this, cellSet().iterator())
/*     */       {
/*     */         @ParametricNullness
/*     */         V transform(Table.Cell<R, C, V> cell) {
/* 198 */           return cell.getValue();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   Spliterator<V> valuesSpliterator() {
/* 204 */     return CollectSpliterators.map(cellSpliterator(), Table.Cell::getValue);
/*     */   }
/*     */   
/*     */   class Values
/*     */     extends AbstractCollection<V>
/*     */   {
/*     */     public Iterator<V> iterator() {
/* 211 */       return AbstractTable.this.valuesIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator<V> spliterator() {
/* 216 */       return AbstractTable.this.valuesSpliterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(@CheckForNull Object o) {
/* 221 */       return AbstractTable.this.containsValue(o);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 226 */       AbstractTable.this.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 231 */       return AbstractTable.this.size();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@CheckForNull Object obj) {
/* 237 */     return Tables.equalsImpl(this, obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 242 */     return cellSet().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 248 */     return rowMap().toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/AbstractTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */