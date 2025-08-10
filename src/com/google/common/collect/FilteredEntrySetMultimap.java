/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Predicate;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import javax.annotation.CheckForNull;
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
/*    */ final class FilteredEntrySetMultimap<K, V>
/*    */   extends FilteredEntryMultimap<K, V>
/*    */   implements FilteredSetMultimap<K, V>
/*    */ {
/*    */   FilteredEntrySetMultimap(SetMultimap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> predicate) {
/* 37 */     super(unfiltered, predicate);
/*    */   }
/*    */ 
/*    */   
/*    */   public SetMultimap<K, V> unfiltered() {
/* 42 */     return (SetMultimap<K, V>)this.unfiltered;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<V> get(@ParametricNullness K key) {
/* 47 */     return (Set<V>)super.get(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<V> removeAll(@CheckForNull Object key) {
/* 52 */     return (Set<V>)super.removeAll(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<V> replaceValues(@ParametricNullness K key, Iterable<? extends V> values) {
/* 57 */     return (Set<V>)super.replaceValues(key, values);
/*    */   }
/*    */ 
/*    */   
/*    */   Set<Map.Entry<K, V>> createEntries() {
/* 62 */     return Sets.filter(unfiltered().entries(), entryPredicate());
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<Map.Entry<K, V>> entries() {
/* 67 */     return (Set<Map.Entry<K, V>>)super.entries();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/FilteredEntrySetMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */