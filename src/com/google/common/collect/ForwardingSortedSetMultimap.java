/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Collection;
/*    */ import java.util.Comparator;
/*    */ import java.util.Set;
/*    */ import java.util.SortedSet;
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
/*    */ public abstract class ForwardingSortedSetMultimap<K, V>
/*    */   extends ForwardingSetMultimap<K, V>
/*    */   implements SortedSetMultimap<K, V>
/*    */ {
/*    */   public SortedSet<V> get(@ParametricNullness K key) {
/* 51 */     return delegate().get(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedSet<V> removeAll(@CheckForNull Object key) {
/* 56 */     return delegate().removeAll(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedSet<V> replaceValues(@ParametricNullness K key, Iterable<? extends V> values) {
/* 61 */     return delegate().replaceValues(key, values);
/*    */   }
/*    */ 
/*    */   
/*    */   @CheckForNull
/*    */   public Comparator<? super V> valueComparator() {
/* 67 */     return delegate().valueComparator();
/*    */   }
/*    */   
/*    */   protected abstract SortedSetMultimap<K, V> delegate();
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ForwardingSortedSetMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */