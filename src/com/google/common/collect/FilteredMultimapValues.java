/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Objects;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.base.Predicate;
/*    */ import com.google.common.base.Predicates;
/*    */ import com.google.j2objc.annotations.Weak;
/*    */ import java.util.AbstractCollection;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
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
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @GwtCompatible
/*    */ final class FilteredMultimapValues<K, V>
/*    */   extends AbstractCollection<V>
/*    */ {
/*    */   @Weak
/*    */   private final FilteredMultimap<K, V> multimap;
/*    */   
/*    */   FilteredMultimapValues(FilteredMultimap<K, V> multimap) {
/* 43 */     this.multimap = (FilteredMultimap<K, V>)Preconditions.checkNotNull(multimap);
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator<V> iterator() {
/* 48 */     return Maps.valueIterator(this.multimap.entries().iterator());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean contains(@CheckForNull Object o) {
/* 53 */     return this.multimap.containsValue(o);
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 58 */     return this.multimap.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean remove(@CheckForNull Object o) {
/* 63 */     Predicate<? super Map.Entry<K, V>> entryPredicate = this.multimap.entryPredicate();
/* 64 */     Iterator<Map.Entry<K, V>> unfilteredItr = this.multimap.unfiltered().entries().iterator();
/* 65 */     while (unfilteredItr.hasNext()) {
/* 66 */       Map.Entry<K, V> entry = unfilteredItr.next();
/* 67 */       if (entryPredicate.apply(entry) && Objects.equal(entry.getValue(), o)) {
/* 68 */         unfilteredItr.remove();
/* 69 */         return true;
/*    */       } 
/*    */     } 
/* 72 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean removeAll(Collection<?> c) {
/* 77 */     return Iterables.removeIf(this.multimap
/* 78 */         .unfiltered().entries(), 
/*    */         
/* 80 */         Predicates.and(this.multimap
/* 81 */           .entryPredicate(), Maps.valuePredicateOnEntries(Predicates.in(c))));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean retainAll(Collection<?> c) {
/* 86 */     return Iterables.removeIf(this.multimap
/* 87 */         .unfiltered().entries(), 
/*    */         
/* 89 */         Predicates.and(this.multimap
/* 90 */           .entryPredicate(), 
/* 91 */           Maps.valuePredicateOnEntries(Predicates.not(Predicates.in(c)))));
/*    */   }
/*    */ 
/*    */   
/*    */   public void clear() {
/* 96 */     this.multimap.clear();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/FilteredMultimapValues.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */