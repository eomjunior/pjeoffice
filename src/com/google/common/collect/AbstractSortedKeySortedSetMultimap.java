/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.SortedMap;
/*    */ import java.util.SortedSet;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @GwtCompatible
/*    */ abstract class AbstractSortedKeySortedSetMultimap<K, V>
/*    */   extends AbstractSortedSetMultimap<K, V>
/*    */ {
/*    */   AbstractSortedKeySortedSetMultimap(SortedMap<K, Collection<V>> map) {
/* 41 */     super(map);
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedMap<K, Collection<V>> asMap() {
/* 46 */     return (SortedMap<K, Collection<V>>)super.asMap();
/*    */   }
/*    */ 
/*    */   
/*    */   SortedMap<K, Collection<V>> backingMap() {
/* 51 */     return (SortedMap<K, Collection<V>>)super.backingMap();
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedSet<K> keySet() {
/* 56 */     return (SortedSet<K>)super.keySet();
/*    */   }
/*    */ 
/*    */   
/*    */   Set<K> createKeySet() {
/* 61 */     return createMaybeNavigableKeySet();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/AbstractSortedKeySortedSetMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */