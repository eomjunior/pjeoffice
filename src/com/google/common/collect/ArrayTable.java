/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.DoNotCall;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class ArrayTable<R, C, V>
/*     */   extends AbstractTable<R, C, V>
/*     */   implements Serializable
/*     */ {
/*     */   private final ImmutableList<R> rowList;
/*     */   private final ImmutableList<C> columnList;
/*     */   private final ImmutableMap<R, Integer> rowKeyToIndex;
/*     */   private final ImmutableMap<C, Integer> columnKeyToIndex;
/*     */   private final V[][] array;
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   private transient ColumnMap columnMap;
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   private transient RowMap rowMap;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <R, C, V> ArrayTable<R, C, V> create(Iterable<? extends R> rowKeys, Iterable<? extends C> columnKeys) {
/* 108 */     return new ArrayTable<>(rowKeys, columnKeys);
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
/*     */   public static <R, C, V> ArrayTable<R, C, V> create(Table<R, C, ? extends V> table) {
/* 136 */     return (table instanceof ArrayTable) ? 
/* 137 */       new ArrayTable<>((ArrayTable)table) : 
/* 138 */       new ArrayTable<>(table);
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
/*     */   private ArrayTable(Iterable<? extends R> rowKeys, Iterable<? extends C> columnKeys) {
/* 150 */     this.rowList = ImmutableList.copyOf(rowKeys);
/* 151 */     this.columnList = ImmutableList.copyOf(columnKeys);
/* 152 */     Preconditions.checkArgument((this.rowList.isEmpty() == this.columnList.isEmpty()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 160 */     this.rowKeyToIndex = Maps.indexMap(this.rowList);
/* 161 */     this.columnKeyToIndex = Maps.indexMap(this.columnList);
/*     */ 
/*     */ 
/*     */     
/* 165 */     V[][] tmpArray = (V[][])new Object[this.rowList.size()][this.columnList.size()];
/* 166 */     this.array = tmpArray;
/*     */     
/* 168 */     eraseAll();
/*     */   }
/*     */   
/*     */   private ArrayTable(Table<R, C, ? extends V> table) {
/* 172 */     this(table.rowKeySet(), table.columnKeySet());
/* 173 */     putAll(table);
/*     */   }
/*     */   
/*     */   private ArrayTable(ArrayTable<R, C, V> table) {
/* 177 */     this.rowList = table.rowList;
/* 178 */     this.columnList = table.columnList;
/* 179 */     this.rowKeyToIndex = table.rowKeyToIndex;
/* 180 */     this.columnKeyToIndex = table.columnKeyToIndex;
/*     */ 
/*     */     
/* 183 */     V[][] copy = (V[][])new Object[this.rowList.size()][this.columnList.size()];
/* 184 */     this.array = copy;
/* 185 */     for (int i = 0; i < this.rowList.size(); i++)
/* 186 */       System.arraycopy(table.array[i], 0, copy[i], 0, (table.array[i]).length); 
/*     */   }
/*     */   
/*     */   private static abstract class ArrayMap<K, V>
/*     */     extends Maps.IteratorBasedAbstractMap<K, V>
/*     */   {
/*     */     private final ImmutableMap<K, Integer> keyIndex;
/*     */     
/*     */     private ArrayMap(ImmutableMap<K, Integer> keyIndex) {
/* 195 */       this.keyIndex = keyIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<K> keySet() {
/* 200 */       return this.keyIndex.keySet();
/*     */     }
/*     */     
/*     */     K getKey(int index) {
/* 204 */       return this.keyIndex.keySet().asList().get(index);
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
/*     */     public int size() {
/* 217 */       return this.keyIndex.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 222 */       return this.keyIndex.isEmpty();
/*     */     }
/*     */     
/*     */     Map.Entry<K, V> getEntry(final int index) {
/* 226 */       Preconditions.checkElementIndex(index, size());
/* 227 */       return new AbstractMapEntry<K, V>()
/*     */         {
/*     */           public K getKey() {
/* 230 */             return (K)ArrayTable.ArrayMap.this.getKey(index);
/*     */           }
/*     */ 
/*     */           
/*     */           @ParametricNullness
/*     */           public V getValue() {
/* 236 */             return (V)ArrayTable.ArrayMap.this.getValue(index);
/*     */           }
/*     */ 
/*     */           
/*     */           @ParametricNullness
/*     */           public V setValue(@ParametricNullness V value) {
/* 242 */             return (V)ArrayTable.ArrayMap.this.setValue(index, value);
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     Iterator<Map.Entry<K, V>> entryIterator() {
/* 249 */       return new AbstractIndexedListIterator<Map.Entry<K, V>>(size())
/*     */         {
/*     */           protected Map.Entry<K, V> get(int index) {
/* 252 */             return ArrayTable.ArrayMap.this.getEntry(index);
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     Spliterator<Map.Entry<K, V>> entrySpliterator() {
/* 259 */       return CollectSpliterators.indexed(size(), 16, this::getEntry);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean containsKey(@CheckForNull Object key) {
/* 266 */       return this.keyIndex.containsKey(key);
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public V get(@CheckForNull Object key) {
/* 272 */       Integer index = this.keyIndex.get(key);
/* 273 */       if (index == null) {
/* 274 */         return null;
/*     */       }
/* 276 */       return getValue(index.intValue());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public V put(K key, @ParametricNullness V value) {
/* 283 */       Integer index = this.keyIndex.get(key);
/* 284 */       if (index == null) {
/* 285 */         throw new IllegalArgumentException(
/* 286 */             getKeyRole() + " " + key + " not in " + this.keyIndex.keySet());
/*     */       }
/* 288 */       return setValue(index.intValue(), value);
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public V remove(@CheckForNull Object key) {
/* 294 */       throw new UnsupportedOperationException();
/*     */     } abstract String getKeyRole();
/*     */     @ParametricNullness
/*     */     abstract V getValue(int param1Int);
/*     */     public void clear() {
/* 299 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     @ParametricNullness
/*     */     abstract V setValue(int param1Int, @ParametricNullness V param1V);
/*     */   }
/*     */   
/*     */   public ImmutableList<R> rowKeyList() {
/* 308 */     return this.rowList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableList<C> columnKeyList() {
/* 316 */     return this.columnList;
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
/*     */   @CheckForNull
/*     */   public V at(int rowIndex, int columnIndex) {
/* 334 */     Preconditions.checkElementIndex(rowIndex, this.rowList.size());
/* 335 */     Preconditions.checkElementIndex(columnIndex, this.columnList.size());
/* 336 */     return this.array[rowIndex][columnIndex];
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
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   public V set(int rowIndex, int columnIndex, @CheckForNull V value) {
/* 356 */     Preconditions.checkElementIndex(rowIndex, this.rowList.size());
/* 357 */     Preconditions.checkElementIndex(columnIndex, this.columnList.size());
/* 358 */     V oldValue = this.array[rowIndex][columnIndex];
/* 359 */     this.array[rowIndex][columnIndex] = value;
/* 360 */     return oldValue;
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
/*     */   @GwtIncompatible
/*     */   public V[][] toArray(Class<V> valueClass) {
/* 376 */     V[][] copy = (V[][])Array.newInstance(valueClass, new int[] { this.rowList.size(), this.columnList.size() });
/* 377 */     for (int i = 0; i < this.rowList.size(); i++) {
/* 378 */       System.arraycopy(this.array[i], 0, copy[i], 0, (this.array[i]).length);
/*     */     }
/* 380 */     return copy;
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
/*     */   public void clear() {
/* 393 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void eraseAll() {
/* 398 */     for (V[] row : this.array) {
/* 399 */       Arrays.fill((Object[])row, (Object)null);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
/* 409 */     return (containsRow(rowKey) && containsColumn(columnKey));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsColumn(@CheckForNull Object columnKey) {
/* 418 */     return this.columnKeyToIndex.containsKey(columnKey);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsRow(@CheckForNull Object rowKey) {
/* 427 */     return this.rowKeyToIndex.containsKey(rowKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(@CheckForNull Object value) {
/* 432 */     for (V[] row : this.array) {
/* 433 */       for (V element : row) {
/* 434 */         if (Objects.equal(value, element)) {
/* 435 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 439 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public V get(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
/* 445 */     Integer rowIndex = this.rowKeyToIndex.get(rowKey);
/* 446 */     Integer columnIndex = this.columnKeyToIndex.get(columnKey);
/* 447 */     return (rowIndex == null || columnIndex == null) ? null : at(rowIndex.intValue(), columnIndex.intValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 455 */     return (this.rowList.isEmpty() || this.columnList.isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   public V put(R rowKey, C columnKey, @CheckForNull V value) {
/* 468 */     Preconditions.checkNotNull(rowKey);
/* 469 */     Preconditions.checkNotNull(columnKey);
/* 470 */     Integer rowIndex = this.rowKeyToIndex.get(rowKey);
/* 471 */     Preconditions.checkArgument((rowIndex != null), "Row %s not in %s", rowKey, this.rowList);
/* 472 */     Integer columnIndex = this.columnKeyToIndex.get(columnKey);
/* 473 */     Preconditions.checkArgument((columnIndex != null), "Column %s not in %s", columnKey, this.columnList);
/* 474 */     return set(rowIndex.intValue(), columnIndex.intValue(), value);
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
/*     */   public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
/* 494 */     super.putAll(table);
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
/*     */   @DoNotCall("Always throws UnsupportedOperationException")
/*     */   @CanIgnoreReturnValue
/*     */   public V remove(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
/* 509 */     throw new UnsupportedOperationException();
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
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   public V erase(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
/* 528 */     Integer rowIndex = this.rowKeyToIndex.get(rowKey);
/* 529 */     Integer columnIndex = this.columnKeyToIndex.get(columnKey);
/* 530 */     if (rowIndex == null || columnIndex == null) {
/* 531 */       return null;
/*     */     }
/* 533 */     return set(rowIndex.intValue(), columnIndex.intValue(), null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 540 */     return this.rowList.size() * this.columnList.size();
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
/*     */   public Set<Table.Cell<R, C, V>> cellSet() {
/* 556 */     return super.cellSet();
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<Table.Cell<R, C, V>> cellIterator() {
/* 561 */     return new AbstractIndexedListIterator<Table.Cell<R, C, V>>(size())
/*     */       {
/*     */         protected Table.Cell<R, C, V> get(int index) {
/* 564 */           return ArrayTable.this.getCell(index);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   Spliterator<Table.Cell<R, C, V>> cellSpliterator() {
/* 571 */     return CollectSpliterators.indexed(
/* 572 */         size(), 273, this::getCell);
/*     */   }
/*     */   
/*     */   private Table.Cell<R, C, V> getCell(final int index) {
/* 576 */     return new Tables.AbstractCell<R, C, V>() {
/* 577 */         final int rowIndex = index / ArrayTable.this.columnList.size();
/* 578 */         final int columnIndex = index % ArrayTable.this.columnList.size();
/*     */ 
/*     */         
/*     */         public R getRowKey() {
/* 582 */           return (R)ArrayTable.this.rowList.get(this.rowIndex);
/*     */         }
/*     */ 
/*     */         
/*     */         public C getColumnKey() {
/* 587 */           return (C)ArrayTable.this.columnList.get(this.columnIndex);
/*     */         }
/*     */ 
/*     */         
/*     */         @CheckForNull
/*     */         public V getValue() {
/* 593 */           return (V)ArrayTable.this.at(this.rowIndex, this.columnIndex);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   @CheckForNull
/*     */   private V getValue(int index) {
/* 600 */     int rowIndex = index / this.columnList.size();
/* 601 */     int columnIndex = index % this.columnList.size();
/* 602 */     return at(rowIndex, columnIndex);
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
/*     */   public Map<R, V> column(C columnKey) {
/* 618 */     Preconditions.checkNotNull(columnKey);
/* 619 */     Integer columnIndex = this.columnKeyToIndex.get(columnKey);
/* 620 */     if (columnIndex == null) {
/* 621 */       return Collections.emptyMap();
/*     */     }
/* 623 */     return new Column(columnIndex.intValue());
/*     */   }
/*     */   
/*     */   private class Column
/*     */     extends ArrayMap<R, V> {
/*     */     final int columnIndex;
/*     */     
/*     */     Column(int columnIndex) {
/* 631 */       super(ArrayTable.this.rowKeyToIndex);
/* 632 */       this.columnIndex = columnIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     String getKeyRole() {
/* 637 */       return "Row";
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     V getValue(int index) {
/* 643 */       return (V)ArrayTable.this.at(index, this.columnIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     V setValue(int index, @CheckForNull V newValue) {
/* 649 */       return (V)ArrayTable.this.set(index, this.columnIndex, newValue);
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
/*     */   public ImmutableSet<C> columnKeySet() {
/* 661 */     return this.columnKeyToIndex.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<C, Map<R, V>> columnMap() {
/* 668 */     ColumnMap map = this.columnMap;
/* 669 */     return (map == null) ? (this.columnMap = new ColumnMap()) : map;
/*     */   }
/*     */   
/*     */   private class ColumnMap
/*     */     extends ArrayMap<C, Map<R, V>> {
/*     */     private ColumnMap() {
/* 675 */       super(ArrayTable.this.columnKeyToIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     String getKeyRole() {
/* 680 */       return "Column";
/*     */     }
/*     */ 
/*     */     
/*     */     Map<R, V> getValue(int index) {
/* 685 */       return new ArrayTable.Column(index);
/*     */     }
/*     */ 
/*     */     
/*     */     Map<R, V> setValue(int index, Map<R, V> newValue) {
/* 690 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public Map<R, V> put(C key, Map<R, V> value) {
/* 696 */       throw new UnsupportedOperationException();
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
/*     */   public Map<C, V> row(R rowKey) {
/* 713 */     Preconditions.checkNotNull(rowKey);
/* 714 */     Integer rowIndex = this.rowKeyToIndex.get(rowKey);
/* 715 */     if (rowIndex == null) {
/* 716 */       return Collections.emptyMap();
/*     */     }
/* 718 */     return new Row(rowIndex.intValue());
/*     */   }
/*     */   
/*     */   private class Row
/*     */     extends ArrayMap<C, V> {
/*     */     final int rowIndex;
/*     */     
/*     */     Row(int rowIndex) {
/* 726 */       super(ArrayTable.this.columnKeyToIndex);
/* 727 */       this.rowIndex = rowIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     String getKeyRole() {
/* 732 */       return "Column";
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     V getValue(int index) {
/* 738 */       return (V)ArrayTable.this.at(this.rowIndex, index);
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     V setValue(int index, @CheckForNull V newValue) {
/* 744 */       return (V)ArrayTable.this.set(this.rowIndex, index, newValue);
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
/*     */   public ImmutableSet<R> rowKeySet() {
/* 756 */     return this.rowKeyToIndex.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<R, Map<C, V>> rowMap() {
/* 763 */     RowMap map = this.rowMap;
/* 764 */     return (map == null) ? (this.rowMap = new RowMap()) : map;
/*     */   }
/*     */   
/*     */   private class RowMap
/*     */     extends ArrayMap<R, Map<C, V>> {
/*     */     private RowMap() {
/* 770 */       super(ArrayTable.this.rowKeyToIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     String getKeyRole() {
/* 775 */       return "Row";
/*     */     }
/*     */ 
/*     */     
/*     */     Map<C, V> getValue(int index) {
/* 780 */       return new ArrayTable.Row(index);
/*     */     }
/*     */ 
/*     */     
/*     */     Map<C, V> setValue(int index, Map<C, V> newValue) {
/* 785 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public Map<C, V> put(R key, Map<C, V> value) {
/* 791 */       throw new UnsupportedOperationException();
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
/*     */   public Collection<V> values() {
/* 806 */     return super.values();
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<V> valuesIterator() {
/* 811 */     return new AbstractIndexedListIterator<V>(size())
/*     */       {
/*     */         @CheckForNull
/*     */         protected V get(int index) {
/* 815 */           return ArrayTable.this.getValue(index);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   Spliterator<V> valuesSpliterator() {
/* 822 */     return CollectSpliterators.indexed(size(), 16, this::getValue);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ArrayTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */