/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.annotations.J2ktIncompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @GwtCompatible
/*    */ class SingletonImmutableTable<R, C, V>
/*    */   extends ImmutableTable<R, C, V>
/*    */ {
/*    */   final R singleRowKey;
/*    */   final C singleColumnKey;
/*    */   final V singleValue;
/*    */   
/*    */   SingletonImmutableTable(R rowKey, C columnKey, V value) {
/* 39 */     this.singleRowKey = (R)Preconditions.checkNotNull(rowKey);
/* 40 */     this.singleColumnKey = (C)Preconditions.checkNotNull(columnKey);
/* 41 */     this.singleValue = (V)Preconditions.checkNotNull(value);
/*    */   }
/*    */   
/*    */   SingletonImmutableTable(Table.Cell<R, C, V> cell) {
/* 45 */     this(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
/*    */   }
/*    */ 
/*    */   
/*    */   public ImmutableMap<R, V> column(C columnKey) {
/* 50 */     Preconditions.checkNotNull(columnKey);
/* 51 */     return containsColumn(columnKey) ? 
/* 52 */       ImmutableMap.<R, V>of(this.singleRowKey, this.singleValue) : 
/* 53 */       ImmutableMap.<R, V>of();
/*    */   }
/*    */ 
/*    */   
/*    */   public ImmutableMap<C, Map<R, V>> columnMap() {
/* 58 */     return ImmutableMap.of(this.singleColumnKey, ImmutableMap.of(this.singleRowKey, this.singleValue));
/*    */   }
/*    */ 
/*    */   
/*    */   public ImmutableMap<R, Map<C, V>> rowMap() {
/* 63 */     return ImmutableMap.of(this.singleRowKey, ImmutableMap.of(this.singleColumnKey, this.singleValue));
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 68 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   ImmutableSet<Table.Cell<R, C, V>> createCellSet() {
/* 73 */     return ImmutableSet.of(cellOf(this.singleRowKey, this.singleColumnKey, this.singleValue));
/*    */   }
/*    */ 
/*    */   
/*    */   ImmutableCollection<V> createValues() {
/* 78 */     return ImmutableSet.of(this.singleValue);
/*    */   }
/*    */ 
/*    */   
/*    */   @J2ktIncompatible
/*    */   @GwtIncompatible
/*    */   Object writeReplace() {
/* 85 */     return ImmutableTable.SerializedForm.create(this, new int[] { 0 }, new int[] { 0 });
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/SingletonImmutableTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */