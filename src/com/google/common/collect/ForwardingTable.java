/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ public abstract class ForwardingTable<R, C, V>
/*     */   extends ForwardingObject
/*     */   implements Table<R, C, V>
/*     */ {
/*     */   public Set<Table.Cell<R, C, V>> cellSet() {
/*  48 */     return delegate().cellSet();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  53 */     delegate().clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<R, V> column(@ParametricNullness C columnKey) {
/*  58 */     return delegate().column(columnKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<C> columnKeySet() {
/*  63 */     return delegate().columnKeySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<C, Map<R, V>> columnMap() {
/*  68 */     return delegate().columnMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
/*  73 */     return delegate().contains(rowKey, columnKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsColumn(@CheckForNull Object columnKey) {
/*  78 */     return delegate().containsColumn(columnKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsRow(@CheckForNull Object rowKey) {
/*  83 */     return delegate().containsRow(rowKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(@CheckForNull Object value) {
/*  88 */     return delegate().containsValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public V get(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
/*  94 */     return delegate().get(rowKey, columnKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  99 */     return delegate().isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   public V put(@ParametricNullness R rowKey, @ParametricNullness C columnKey, @ParametricNullness V value) {
/* 107 */     return delegate().put(rowKey, columnKey, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
/* 112 */     delegate().putAll(table);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   public V remove(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
/* 119 */     return delegate().remove(rowKey, columnKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<C, V> row(@ParametricNullness R rowKey) {
/* 124 */     return delegate().row(rowKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<R> rowKeySet() {
/* 129 */     return delegate().rowKeySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<R, Map<C, V>> rowMap() {
/* 134 */     return delegate().rowMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 139 */     return delegate().size();
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 144 */     return delegate().values();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@CheckForNull Object obj) {
/* 149 */     return (obj == this || delegate().equals(obj));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 154 */     return delegate().hashCode();
/*     */   }
/*     */   
/*     */   protected abstract Table<R, C, V> delegate();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ForwardingTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */