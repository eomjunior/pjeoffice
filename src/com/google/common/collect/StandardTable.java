/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import com.google.common.base.Supplier;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*      */ import java.io.Serializable;
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.Spliterator;
/*      */ import java.util.Spliterators;
/*      */ import javax.annotation.CheckForNull;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @ElementTypesAreNonnullByDefault
/*      */ @GwtCompatible
/*      */ class StandardTable<R, C, V>
/*      */   extends AbstractTable<R, C, V>
/*      */   implements Serializable
/*      */ {
/*      */   @GwtTransient
/*      */   final Map<R, Map<C, V>> backingMap;
/*      */   @GwtTransient
/*      */   final Supplier<? extends Map<C, V>> factory;
/*      */   @LazyInit
/*      */   @CheckForNull
/*      */   private transient Set<C> columnKeySet;
/*      */   @LazyInit
/*      */   @CheckForNull
/*      */   private transient Map<R, Map<C, V>> rowMap;
/*      */   @LazyInit
/*      */   @CheckForNull
/*      */   private transient ColumnMap columnMap;
/*      */   private static final long serialVersionUID = 0L;
/*      */   
/*      */   StandardTable(Map<R, Map<C, V>> backingMap, Supplier<? extends Map<C, V>> factory) {
/*   76 */     this.backingMap = backingMap;
/*   77 */     this.factory = factory;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean contains(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
/*   84 */     return (rowKey != null && columnKey != null && super.contains(rowKey, columnKey));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsColumn(@CheckForNull Object columnKey) {
/*   89 */     if (columnKey == null) {
/*   90 */       return false;
/*      */     }
/*   92 */     for (Map<C, V> map : this.backingMap.values()) {
/*   93 */       if (Maps.safeContainsKey(map, columnKey)) {
/*   94 */         return true;
/*      */       }
/*      */     } 
/*   97 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsRow(@CheckForNull Object rowKey) {
/*  102 */     return (rowKey != null && Maps.safeContainsKey(this.backingMap, rowKey));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsValue(@CheckForNull Object value) {
/*  107 */     return (value != null && super.containsValue(value));
/*      */   }
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   public V get(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
/*  113 */     return (rowKey == null || columnKey == null) ? null : super.get(rowKey, columnKey);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isEmpty() {
/*  118 */     return this.backingMap.isEmpty();
/*      */   }
/*      */ 
/*      */   
/*      */   public int size() {
/*  123 */     int size = 0;
/*  124 */     for (Map<C, V> map : this.backingMap.values()) {
/*  125 */       size += map.size();
/*      */     }
/*  127 */     return size;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clear() {
/*  134 */     this.backingMap.clear();
/*      */   }
/*      */   
/*      */   private Map<C, V> getOrCreate(R rowKey) {
/*  138 */     Map<C, V> map = this.backingMap.get(rowKey);
/*  139 */     if (map == null) {
/*  140 */       map = (Map<C, V>)this.factory.get();
/*  141 */       this.backingMap.put(rowKey, map);
/*      */     } 
/*  143 */     return map;
/*      */   }
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   @CanIgnoreReturnValue
/*      */   public V put(R rowKey, C columnKey, V value) {
/*  150 */     Preconditions.checkNotNull(rowKey);
/*  151 */     Preconditions.checkNotNull(columnKey);
/*  152 */     Preconditions.checkNotNull(value);
/*  153 */     return getOrCreate(rowKey).put(columnKey, value);
/*      */   }
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   @CanIgnoreReturnValue
/*      */   public V remove(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
/*  160 */     if (rowKey == null || columnKey == null) {
/*  161 */       return null;
/*      */     }
/*  163 */     Map<C, V> map = Maps.<Map<C, V>>safeGet(this.backingMap, rowKey);
/*  164 */     if (map == null) {
/*  165 */       return null;
/*      */     }
/*  167 */     V value = map.remove(columnKey);
/*  168 */     if (map.isEmpty()) {
/*  169 */       this.backingMap.remove(rowKey);
/*      */     }
/*  171 */     return value;
/*      */   }
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   private Map<R, V> removeColumn(@CheckForNull Object column) {
/*  176 */     Map<R, V> output = new LinkedHashMap<>();
/*  177 */     Iterator<Map.Entry<R, Map<C, V>>> iterator = this.backingMap.entrySet().iterator();
/*  178 */     while (iterator.hasNext()) {
/*  179 */       Map.Entry<R, Map<C, V>> entry = iterator.next();
/*  180 */       V value = (V)((Map)entry.getValue()).remove(column);
/*  181 */       if (value != null) {
/*  182 */         output.put(entry.getKey(), value);
/*  183 */         if (((Map)entry.getValue()).isEmpty()) {
/*  184 */           iterator.remove();
/*      */         }
/*      */       } 
/*      */     } 
/*  188 */     return output;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean containsMapping(@CheckForNull Object rowKey, @CheckForNull Object columnKey, @CheckForNull Object value) {
/*  193 */     return (value != null && value.equals(get(rowKey, columnKey)));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean removeMapping(@CheckForNull Object rowKey, @CheckForNull Object columnKey, @CheckForNull Object value) {
/*  199 */     if (containsMapping(rowKey, columnKey, value)) {
/*  200 */       remove(rowKey, columnKey);
/*  201 */       return true;
/*      */     } 
/*  203 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private abstract class TableSet<T>
/*      */     extends Sets.ImprovedAbstractSet<T>
/*      */   {
/*      */     private TableSet() {}
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/*  216 */       return StandardTable.this.backingMap.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  221 */       StandardTable.this.backingMap.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<Table.Cell<R, C, V>> cellSet() {
/*  236 */     return super.cellSet();
/*      */   }
/*      */ 
/*      */   
/*      */   Iterator<Table.Cell<R, C, V>> cellIterator() {
/*  241 */     return new CellIterator();
/*      */   }
/*      */   
/*      */   private class CellIterator implements Iterator<Table.Cell<R, C, V>> {
/*  245 */     final Iterator<Map.Entry<R, Map<C, V>>> rowIterator = StandardTable.this.backingMap.entrySet().iterator(); @CheckForNull
/*      */     Map.Entry<R, Map<C, V>> rowEntry;
/*  247 */     Iterator<Map.Entry<C, V>> columnIterator = Iterators.emptyModifiableIterator();
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/*  251 */       return (this.rowIterator.hasNext() || this.columnIterator.hasNext());
/*      */     }
/*      */ 
/*      */     
/*      */     public Table.Cell<R, C, V> next() {
/*  256 */       if (!this.columnIterator.hasNext()) {
/*  257 */         this.rowEntry = this.rowIterator.next();
/*  258 */         this.columnIterator = ((Map<C, V>)this.rowEntry.getValue()).entrySet().iterator();
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  273 */       Objects.requireNonNull(this.rowEntry);
/*  274 */       Map.Entry<C, V> columnEntry = this.columnIterator.next();
/*  275 */       return Tables.immutableCell(this.rowEntry.getKey(), columnEntry.getKey(), columnEntry.getValue());
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/*  280 */       this.columnIterator.remove();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  291 */       if (((Map)((Map.Entry)Objects.<Map.Entry>requireNonNull(this.rowEntry)).getValue()).isEmpty()) {
/*  292 */         this.rowIterator.remove();
/*  293 */         this.rowEntry = null;
/*      */       } 
/*      */     }
/*      */     
/*      */     private CellIterator() {} }
/*      */   
/*      */   Spliterator<Table.Cell<R, C, V>> cellSpliterator() {
/*  300 */     return CollectSpliterators.flatMap(this.backingMap
/*  301 */         .entrySet().spliterator(), rowEntry -> CollectSpliterators.map(((Map)rowEntry.getValue()).entrySet().spliterator(), ()), 65, 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  309 */         size());
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<C, V> row(R rowKey) {
/*  314 */     return new Row(rowKey);
/*      */   }
/*      */   class Row extends Maps.IteratorBasedAbstractMap<C, V> { final R rowKey;
/*      */     @CheckForNull
/*      */     Map<C, V> backingRowMap;
/*      */     
/*      */     Row(R rowKey) {
/*  321 */       this.rowKey = (R)Preconditions.checkNotNull(rowKey);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     final void updateBackingRowMapField() {
/*  327 */       if (this.backingRowMap == null || (this.backingRowMap.isEmpty() && StandardTable.this.backingMap.containsKey(this.rowKey))) {
/*  328 */         this.backingRowMap = computeBackingRowMap();
/*      */       }
/*      */     }
/*      */     
/*      */     @CheckForNull
/*      */     Map<C, V> computeBackingRowMap() {
/*  334 */       return (Map<C, V>)StandardTable.this.backingMap.get(this.rowKey);
/*      */     }
/*      */ 
/*      */     
/*      */     void maintainEmptyInvariant() {
/*  339 */       updateBackingRowMapField();
/*  340 */       if (this.backingRowMap != null && this.backingRowMap.isEmpty()) {
/*  341 */         StandardTable.this.backingMap.remove(this.rowKey);
/*  342 */         this.backingRowMap = null;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(@CheckForNull Object key) {
/*  348 */       updateBackingRowMapField();
/*  349 */       return (key != null && this.backingRowMap != null && Maps.safeContainsKey(this.backingRowMap, key));
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V get(@CheckForNull Object key) {
/*  355 */       updateBackingRowMapField();
/*  356 */       return (key != null && this.backingRowMap != null) ? Maps.<V>safeGet(this.backingRowMap, key) : null;
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V put(C key, V value) {
/*  362 */       Preconditions.checkNotNull(key);
/*  363 */       Preconditions.checkNotNull(value);
/*  364 */       if (this.backingRowMap != null && !this.backingRowMap.isEmpty()) {
/*  365 */         return this.backingRowMap.put(key, value);
/*      */       }
/*  367 */       return StandardTable.this.put(this.rowKey, key, value);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V remove(@CheckForNull Object key) {
/*  373 */       updateBackingRowMapField();
/*  374 */       if (this.backingRowMap == null) {
/*  375 */         return null;
/*      */       }
/*  377 */       V result = Maps.safeRemove(this.backingRowMap, key);
/*  378 */       maintainEmptyInvariant();
/*  379 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  384 */       updateBackingRowMapField();
/*  385 */       if (this.backingRowMap != null) {
/*  386 */         this.backingRowMap.clear();
/*      */       }
/*  388 */       maintainEmptyInvariant();
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  393 */       updateBackingRowMapField();
/*  394 */       return (this.backingRowMap == null) ? 0 : this.backingRowMap.size();
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Map.Entry<C, V>> entryIterator() {
/*  399 */       updateBackingRowMapField();
/*  400 */       if (this.backingRowMap == null) {
/*  401 */         return Iterators.emptyModifiableIterator();
/*      */       }
/*  403 */       final Iterator<Map.Entry<C, V>> iterator = this.backingRowMap.entrySet().iterator();
/*  404 */       return new Iterator<Map.Entry<C, V>>()
/*      */         {
/*      */           public boolean hasNext() {
/*  407 */             return iterator.hasNext();
/*      */           }
/*      */ 
/*      */           
/*      */           public Map.Entry<C, V> next() {
/*  412 */             return StandardTable.Row.this.wrapEntry(iterator.next());
/*      */           }
/*      */ 
/*      */           
/*      */           public void remove() {
/*  417 */             iterator.remove();
/*  418 */             StandardTable.Row.this.maintainEmptyInvariant();
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     Spliterator<Map.Entry<C, V>> entrySpliterator() {
/*  425 */       updateBackingRowMapField();
/*  426 */       if (this.backingRowMap == null) {
/*  427 */         return Spliterators.emptySpliterator();
/*      */       }
/*  429 */       return CollectSpliterators.map(this.backingRowMap.entrySet().spliterator(), this::wrapEntry);
/*      */     }
/*      */     
/*      */     Map.Entry<C, V> wrapEntry(final Map.Entry<C, V> entry) {
/*  433 */       return new ForwardingMapEntry<C, V>(this)
/*      */         {
/*      */           protected Map.Entry<C, V> delegate() {
/*  436 */             return entry;
/*      */           }
/*      */ 
/*      */           
/*      */           public V setValue(V value) {
/*  441 */             return super.setValue((V)Preconditions.checkNotNull(value));
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*      */           public boolean equals(@CheckForNull Object object) {
/*  447 */             return standardEquals(object);
/*      */           }
/*      */         };
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<R, V> column(C columnKey) {
/*  460 */     return new Column(columnKey);
/*      */   }
/*      */   
/*      */   private class Column extends Maps.ViewCachingAbstractMap<R, V> {
/*      */     final C columnKey;
/*      */     
/*      */     Column(C columnKey) {
/*  467 */       this.columnKey = (C)Preconditions.checkNotNull(columnKey);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V put(R key, V value) {
/*  473 */       return StandardTable.this.put(key, this.columnKey, value);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V get(@CheckForNull Object key) {
/*  479 */       return (V)StandardTable.this.get(key, this.columnKey);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(@CheckForNull Object key) {
/*  484 */       return StandardTable.this.contains(key, this.columnKey);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public V remove(@CheckForNull Object key) {
/*  490 */       return (V)StandardTable.this.remove(key, this.columnKey);
/*      */     }
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     boolean removeFromColumnIf(Predicate<? super Map.Entry<R, V>> predicate) {
/*  496 */       boolean changed = false;
/*  497 */       Iterator<Map.Entry<R, Map<C, V>>> iterator = StandardTable.this.backingMap.entrySet().iterator();
/*  498 */       while (iterator.hasNext()) {
/*  499 */         Map.Entry<R, Map<C, V>> entry = iterator.next();
/*  500 */         Map<C, V> map = entry.getValue();
/*  501 */         V value = map.get(this.columnKey);
/*  502 */         if (value != null && predicate.apply(Maps.immutableEntry(entry.getKey(), value))) {
/*  503 */           map.remove(this.columnKey);
/*  504 */           changed = true;
/*  505 */           if (map.isEmpty()) {
/*  506 */             iterator.remove();
/*      */           }
/*      */         } 
/*      */       } 
/*  510 */       return changed;
/*      */     }
/*      */ 
/*      */     
/*      */     Set<Map.Entry<R, V>> createEntrySet() {
/*  515 */       return new EntrySet();
/*      */     }
/*      */     
/*      */     private class EntrySet extends Sets.ImprovedAbstractSet<Map.Entry<R, V>> {
/*      */       private EntrySet() {}
/*      */       
/*      */       public Iterator<Map.Entry<R, V>> iterator() {
/*  522 */         return new StandardTable.Column.EntrySetIterator();
/*      */       }
/*      */ 
/*      */       
/*      */       public int size() {
/*  527 */         int size = 0;
/*  528 */         for (Map<C, V> map : (Iterable<Map<C, V>>)StandardTable.this.backingMap.values()) {
/*  529 */           if (map.containsKey(StandardTable.Column.this.columnKey)) {
/*  530 */             size++;
/*      */           }
/*      */         } 
/*  533 */         return size;
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean isEmpty() {
/*  538 */         return !StandardTable.this.containsColumn(StandardTable.Column.this.columnKey);
/*      */       }
/*      */ 
/*      */       
/*      */       public void clear() {
/*  543 */         StandardTable.Column.this.removeFromColumnIf(Predicates.alwaysTrue());
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean contains(@CheckForNull Object o) {
/*  548 */         if (o instanceof Map.Entry) {
/*  549 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/*  550 */           return StandardTable.this.containsMapping(entry.getKey(), StandardTable.Column.this.columnKey, entry.getValue());
/*      */         } 
/*  552 */         return false;
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean remove(@CheckForNull Object obj) {
/*  557 */         if (obj instanceof Map.Entry) {
/*  558 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/*  559 */           return StandardTable.this.removeMapping(entry.getKey(), StandardTable.Column.this.columnKey, entry.getValue());
/*      */         } 
/*  561 */         return false;
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean retainAll(Collection<?> c) {
/*  566 */         return StandardTable.Column.this.removeFromColumnIf(Predicates.not(Predicates.in(c)));
/*      */       }
/*      */     }
/*      */     
/*      */     private class EntrySetIterator extends AbstractIterator<Map.Entry<R, V>> {
/*  571 */       final Iterator<Map.Entry<R, Map<C, V>>> iterator = StandardTable.this.backingMap.entrySet().iterator();
/*      */ 
/*      */       
/*      */       @CheckForNull
/*      */       protected Map.Entry<R, V> computeNext() {
/*  576 */         while (this.iterator.hasNext())
/*  577 */         { final Map.Entry<R, Map<C, V>> entry = this.iterator.next();
/*  578 */           if (((Map)entry.getValue()).containsKey(StandardTable.Column.this.columnKey))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  611 */             return new EntryImpl(); }  }  class EntryImpl extends AbstractMapEntry<R, V> {
/*      */           public R getKey() { return (R)entry.getKey(); }
/*      */           public V getValue() { return (V)((Map)entry.getValue()).get(StandardTable.Column.this.columnKey); } public V setValue(V value) { return NullnessCasts.uncheckedCastNullableTToT(((Map<C, V>)entry.getValue()).put(StandardTable.Column.this.columnKey, (V)Preconditions.checkNotNull(value))); }
/*  614 */         }; return endOfData();
/*      */       }
/*      */       
/*      */       private EntrySetIterator() {} }
/*      */     
/*      */     Set<R> createKeySet() {
/*  620 */       return new KeySet();
/*      */     }
/*      */     
/*      */     private class KeySet
/*      */       extends Maps.KeySet<R, V> {
/*      */       KeySet() {
/*  626 */         super(StandardTable.Column.this);
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean contains(@CheckForNull Object obj) {
/*  631 */         return StandardTable.this.contains(obj, StandardTable.Column.this.columnKey);
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean remove(@CheckForNull Object obj) {
/*  636 */         return (StandardTable.this.remove(obj, StandardTable.Column.this.columnKey) != null);
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean retainAll(Collection<?> c) {
/*  641 */         return StandardTable.Column.this.removeFromColumnIf((Predicate)Maps.keyPredicateOnEntries(Predicates.not(Predicates.in(c))));
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     Collection<V> createValues() {
/*  647 */       return new Values();
/*      */     }
/*      */     
/*      */     private class Values
/*      */       extends Maps.Values<R, V> {
/*      */       Values() {
/*  653 */         super(StandardTable.Column.this);
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean remove(@CheckForNull Object obj) {
/*  658 */         return (obj != null && StandardTable.Column.this.removeFromColumnIf((Predicate)Maps.valuePredicateOnEntries(Predicates.equalTo(obj))));
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean removeAll(Collection<?> c) {
/*  663 */         return StandardTable.Column.this.removeFromColumnIf((Predicate)Maps.valuePredicateOnEntries(Predicates.in(c)));
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean retainAll(Collection<?> c) {
/*  668 */         return StandardTable.Column.this.removeFromColumnIf((Predicate)Maps.valuePredicateOnEntries(Predicates.not(Predicates.in(c))));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<R> rowKeySet() {
/*  675 */     return rowMap().keySet();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<C> columnKeySet() {
/*  690 */     Set<C> result = this.columnKeySet;
/*  691 */     return (result == null) ? (this.columnKeySet = new ColumnKeySet()) : result;
/*      */   }
/*      */   
/*      */   private class ColumnKeySet extends TableSet<C> {
/*      */     private ColumnKeySet() {}
/*      */     
/*      */     public Iterator<C> iterator() {
/*  698 */       return StandardTable.this.createColumnKeyIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  703 */       return Iterators.size(iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(@CheckForNull Object obj) {
/*  708 */       if (obj == null) {
/*  709 */         return false;
/*      */       }
/*  711 */       boolean changed = false;
/*  712 */       Iterator<Map<C, V>> iterator = StandardTable.this.backingMap.values().iterator();
/*  713 */       while (iterator.hasNext()) {
/*  714 */         Map<C, V> map = iterator.next();
/*  715 */         if (map.keySet().remove(obj)) {
/*  716 */           changed = true;
/*  717 */           if (map.isEmpty()) {
/*  718 */             iterator.remove();
/*      */           }
/*      */         } 
/*      */       } 
/*  722 */       return changed;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/*  727 */       Preconditions.checkNotNull(c);
/*  728 */       boolean changed = false;
/*  729 */       Iterator<Map<C, V>> iterator = StandardTable.this.backingMap.values().iterator();
/*  730 */       while (iterator.hasNext()) {
/*  731 */         Map<C, V> map = iterator.next();
/*      */ 
/*      */         
/*  734 */         if (Iterators.removeAll(map.keySet().iterator(), c)) {
/*  735 */           changed = true;
/*  736 */           if (map.isEmpty()) {
/*  737 */             iterator.remove();
/*      */           }
/*      */         } 
/*      */       } 
/*  741 */       return changed;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/*  746 */       Preconditions.checkNotNull(c);
/*  747 */       boolean changed = false;
/*  748 */       Iterator<Map<C, V>> iterator = StandardTable.this.backingMap.values().iterator();
/*  749 */       while (iterator.hasNext()) {
/*  750 */         Map<C, V> map = iterator.next();
/*  751 */         if (map.keySet().retainAll(c)) {
/*  752 */           changed = true;
/*  753 */           if (map.isEmpty()) {
/*  754 */             iterator.remove();
/*      */           }
/*      */         } 
/*      */       } 
/*  758 */       return changed;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(@CheckForNull Object obj) {
/*  763 */       return StandardTable.this.containsColumn(obj);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   Iterator<C> createColumnKeyIterator() {
/*  769 */     return new ColumnKeyIterator();
/*      */   }
/*      */   
/*      */   private class ColumnKeyIterator
/*      */     extends AbstractIterator<C>
/*      */   {
/*  775 */     final Map<C, V> seen = (Map<C, V>)StandardTable.this.factory.get();
/*  776 */     final Iterator<Map<C, V>> mapIterator = StandardTable.this.backingMap.values().iterator();
/*  777 */     Iterator<Map.Entry<C, V>> entryIterator = Iterators.emptyIterator();
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     protected C computeNext() {
/*      */       while (true) {
/*  783 */         while (this.entryIterator.hasNext()) {
/*  784 */           Map.Entry<C, V> entry = this.entryIterator.next();
/*  785 */           if (!this.seen.containsKey(entry.getKey())) {
/*  786 */             this.seen.put(entry.getKey(), entry.getValue());
/*  787 */             return entry.getKey();
/*      */           } 
/*  789 */         }  if (this.mapIterator.hasNext()) {
/*  790 */           this.entryIterator = ((Map<C, V>)this.mapIterator.next()).entrySet().iterator(); continue;
/*      */         }  break;
/*  792 */       }  return endOfData();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private ColumnKeyIterator() {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<V> values() {
/*  806 */     return super.values();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<R, Map<C, V>> rowMap() {
/*  813 */     Map<R, Map<C, V>> result = this.rowMap;
/*  814 */     return (result == null) ? (this.rowMap = createRowMap()) : result;
/*      */   }
/*      */   
/*      */   Map<R, Map<C, V>> createRowMap() {
/*  818 */     return new RowMap();
/*      */   }
/*      */   
/*      */   class RowMap
/*      */     extends Maps.ViewCachingAbstractMap<R, Map<C, V>>
/*      */   {
/*      */     public boolean containsKey(@CheckForNull Object key) {
/*  825 */       return StandardTable.this.containsRow(key);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map<C, V> get(@CheckForNull Object key) {
/*  834 */       return StandardTable.this.containsRow(key) ? StandardTable.this.row(Objects.requireNonNull(key)) : null;
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map<C, V> remove(@CheckForNull Object key) {
/*  840 */       return (key == null) ? null : (Map<C, V>)StandardTable.this.backingMap.remove(key);
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<Map.Entry<R, Map<C, V>>> createEntrySet() {
/*  845 */       return new EntrySet();
/*      */     }
/*      */     
/*      */     private final class EntrySet extends StandardTable<R, C, V>.TableSet<Map.Entry<R, Map<C, V>>> {
/*      */       private EntrySet() {}
/*      */       
/*      */       public Iterator<Map.Entry<R, Map<C, V>>> iterator() {
/*  852 */         return Maps.asMapEntryIterator(StandardTable.this.backingMap
/*  853 */             .keySet(), new Function<R, Map<C, V>>()
/*      */             {
/*      */               public Map<C, V> apply(R rowKey)
/*      */               {
/*  857 */                 return StandardTable.this.row(rowKey);
/*      */               }
/*      */             });
/*      */       }
/*      */ 
/*      */       
/*      */       public int size() {
/*  864 */         return StandardTable.this.backingMap.size();
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean contains(@CheckForNull Object obj) {
/*  869 */         if (obj instanceof Map.Entry) {
/*  870 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/*  871 */           return (entry.getKey() != null && entry
/*  872 */             .getValue() instanceof Map && 
/*  873 */             Collections2.safeContains(StandardTable.this.backingMap.entrySet(), entry));
/*      */         } 
/*  875 */         return false;
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean remove(@CheckForNull Object obj) {
/*  880 */         if (obj instanceof Map.Entry) {
/*  881 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/*  882 */           return (entry.getKey() != null && entry
/*  883 */             .getValue() instanceof Map && StandardTable.this.backingMap
/*  884 */             .entrySet().remove(entry));
/*      */         } 
/*  886 */         return false;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<C, Map<R, V>> columnMap() {
/*  895 */     ColumnMap result = this.columnMap;
/*  896 */     return (result == null) ? (this.columnMap = new ColumnMap()) : result;
/*      */   }
/*      */ 
/*      */   
/*      */   private class ColumnMap
/*      */     extends Maps.ViewCachingAbstractMap<C, Map<R, V>>
/*      */   {
/*      */     private ColumnMap() {}
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map<R, V> get(@CheckForNull Object key) {
/*  908 */       return StandardTable.this.containsColumn(key) ? StandardTable.this.column(Objects.requireNonNull(key)) : null;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(@CheckForNull Object key) {
/*  913 */       return StandardTable.this.containsColumn(key);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     public Map<R, V> remove(@CheckForNull Object key) {
/*  919 */       return StandardTable.this.containsColumn(key) ? StandardTable.this.removeColumn(key) : null;
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<C, Map<R, V>>> createEntrySet() {
/*  924 */       return new ColumnMapEntrySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<C> keySet() {
/*  929 */       return StandardTable.this.columnKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     Collection<Map<R, V>> createValues() {
/*  934 */       return new ColumnMapValues();
/*      */     }
/*      */     
/*      */     private final class ColumnMapEntrySet extends StandardTable<R, C, V>.TableSet<Map.Entry<C, Map<R, V>>> {
/*      */       private ColumnMapEntrySet() {}
/*      */       
/*      */       public Iterator<Map.Entry<C, Map<R, V>>> iterator() {
/*  941 */         return Maps.asMapEntryIterator(StandardTable.this
/*  942 */             .columnKeySet(), new Function<C, Map<R, V>>()
/*      */             {
/*      */               public Map<R, V> apply(C columnKey)
/*      */               {
/*  946 */                 return StandardTable.this.column(columnKey);
/*      */               }
/*      */             });
/*      */       }
/*      */ 
/*      */       
/*      */       public int size() {
/*  953 */         return StandardTable.this.columnKeySet().size();
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean contains(@CheckForNull Object obj) {
/*  958 */         if (obj instanceof Map.Entry) {
/*  959 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/*  960 */           if (StandardTable.this.containsColumn(entry.getKey()))
/*      */           {
/*  962 */             return ((Map)Objects.<Map>requireNonNull(StandardTable.ColumnMap.this.get(entry.getKey()))).equals(entry.getValue());
/*      */           }
/*      */         } 
/*  965 */         return false;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public boolean remove(@CheckForNull Object obj) {
/*  974 */         if (contains(obj) && obj instanceof Map.Entry) {
/*  975 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/*  976 */           StandardTable.this.removeColumn(entry.getKey());
/*  977 */           return true;
/*      */         } 
/*  979 */         return false;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public boolean removeAll(Collection<?> c) {
/*  990 */         Preconditions.checkNotNull(c);
/*  991 */         return Sets.removeAllImpl(this, c.iterator());
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean retainAll(Collection<?> c) {
/*  996 */         Preconditions.checkNotNull(c);
/*  997 */         boolean changed = false;
/*  998 */         for (C columnKey : Lists.newArrayList(StandardTable.this.columnKeySet().iterator())) {
/*  999 */           if (!c.contains(Maps.immutableEntry(columnKey, StandardTable.this.column(columnKey)))) {
/* 1000 */             StandardTable.this.removeColumn(columnKey);
/* 1001 */             changed = true;
/*      */           } 
/*      */         } 
/* 1004 */         return changed;
/*      */       }
/*      */     }
/*      */     
/*      */     private class ColumnMapValues
/*      */       extends Maps.Values<C, Map<R, V>> {
/*      */       ColumnMapValues() {
/* 1011 */         super(StandardTable.ColumnMap.this);
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean remove(@CheckForNull Object obj) {
/* 1016 */         for (Map.Entry<C, Map<R, V>> entry : StandardTable.ColumnMap.this.entrySet()) {
/* 1017 */           if (((Map)entry.getValue()).equals(obj)) {
/* 1018 */             StandardTable.this.removeColumn(entry.getKey());
/* 1019 */             return true;
/*      */           } 
/*      */         } 
/* 1022 */         return false;
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean removeAll(Collection<?> c) {
/* 1027 */         Preconditions.checkNotNull(c);
/* 1028 */         boolean changed = false;
/* 1029 */         for (C columnKey : Lists.newArrayList(StandardTable.this.columnKeySet().iterator())) {
/* 1030 */           if (c.contains(StandardTable.this.column(columnKey))) {
/* 1031 */             StandardTable.this.removeColumn(columnKey);
/* 1032 */             changed = true;
/*      */           } 
/*      */         } 
/* 1035 */         return changed;
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean retainAll(Collection<?> c) {
/* 1040 */         Preconditions.checkNotNull(c);
/* 1041 */         boolean changed = false;
/* 1042 */         for (C columnKey : Lists.newArrayList(StandardTable.this.columnKeySet().iterator())) {
/* 1043 */           if (!c.contains(StandardTable.this.column(columnKey))) {
/* 1044 */             StandardTable.this.removeColumn(columnKey);
/* 1045 */             changed = true;
/*      */           } 
/*      */         } 
/* 1048 */         return changed;
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/StandardTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */