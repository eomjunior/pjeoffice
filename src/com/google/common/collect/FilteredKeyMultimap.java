/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ class FilteredKeyMultimap<K, V>
/*     */   extends AbstractMultimap<K, V>
/*     */   implements FilteredMultimap<K, V>
/*     */ {
/*     */   final Multimap<K, V> unfiltered;
/*     */   final Predicate<? super K> keyPredicate;
/*     */   
/*     */   FilteredKeyMultimap(Multimap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/*  49 */     this.unfiltered = (Multimap<K, V>)Preconditions.checkNotNull(unfiltered);
/*  50 */     this.keyPredicate = (Predicate<? super K>)Preconditions.checkNotNull(keyPredicate);
/*     */   }
/*     */ 
/*     */   
/*     */   public Multimap<K, V> unfiltered() {
/*  55 */     return this.unfiltered;
/*     */   }
/*     */ 
/*     */   
/*     */   public Predicate<? super Map.Entry<K, V>> entryPredicate() {
/*  60 */     return (Predicate)Maps.keyPredicateOnEntries(this.keyPredicate);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  65 */     int size = 0;
/*  66 */     for (Collection<V> collection : asMap().values()) {
/*  67 */       size += collection.size();
/*     */     }
/*  69 */     return size;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(@CheckForNull Object key) {
/*  74 */     if (this.unfiltered.containsKey(key)) {
/*     */       
/*  76 */       K k = (K)key;
/*  77 */       return this.keyPredicate.apply(k);
/*     */     } 
/*  79 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<V> removeAll(@CheckForNull Object key) {
/*  84 */     return containsKey(key) ? this.unfiltered.removeAll(key) : unmodifiableEmptyCollection();
/*     */   }
/*     */   
/*     */   Collection<V> unmodifiableEmptyCollection() {
/*  88 */     if (this.unfiltered instanceof SetMultimap) {
/*  89 */       return Collections.emptySet();
/*     */     }
/*  91 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/*  97 */     keySet().clear();
/*     */   }
/*     */ 
/*     */   
/*     */   Set<K> createKeySet() {
/* 102 */     return Sets.filter(this.unfiltered.keySet(), this.keyPredicate);
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<V> get(@ParametricNullness K key) {
/* 107 */     if (this.keyPredicate.apply(key))
/* 108 */       return this.unfiltered.get(key); 
/* 109 */     if (this.unfiltered instanceof SetMultimap) {
/* 110 */       return new AddRejectingSet<>(key);
/*     */     }
/* 112 */     return new AddRejectingList<>(key);
/*     */   }
/*     */   
/*     */   static class AddRejectingSet<K, V>
/*     */     extends ForwardingSet<V> {
/*     */     @ParametricNullness
/*     */     final K key;
/*     */     
/*     */     AddRejectingSet(@ParametricNullness K key) {
/* 121 */       this.key = key;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean add(@ParametricNullness V element) {
/* 126 */       throw new IllegalArgumentException("Key does not satisfy predicate: " + this.key);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean addAll(Collection<? extends V> collection) {
/* 131 */       Preconditions.checkNotNull(collection);
/* 132 */       throw new IllegalArgumentException("Key does not satisfy predicate: " + this.key);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Set<V> delegate() {
/* 137 */       return Collections.emptySet();
/*     */     }
/*     */   }
/*     */   
/*     */   static class AddRejectingList<K, V> extends ForwardingList<V> {
/*     */     @ParametricNullness
/*     */     final K key;
/*     */     
/*     */     AddRejectingList(@ParametricNullness K key) {
/* 146 */       this.key = key;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean add(@ParametricNullness V v) {
/* 151 */       add(0, v);
/* 152 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void add(int index, @ParametricNullness V element) {
/* 157 */       Preconditions.checkPositionIndex(index, 0);
/* 158 */       throw new IllegalArgumentException("Key does not satisfy predicate: " + this.key);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean addAll(Collection<? extends V> collection) {
/* 163 */       addAll(0, collection);
/* 164 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public boolean addAll(int index, Collection<? extends V> elements) {
/* 170 */       Preconditions.checkNotNull(elements);
/* 171 */       Preconditions.checkPositionIndex(index, 0);
/* 172 */       throw new IllegalArgumentException("Key does not satisfy predicate: " + this.key);
/*     */     }
/*     */ 
/*     */     
/*     */     protected List<V> delegate() {
/* 177 */       return Collections.emptyList();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<Map.Entry<K, V>> entryIterator() {
/* 183 */     throw new AssertionError("should never be called");
/*     */   }
/*     */ 
/*     */   
/*     */   Collection<Map.Entry<K, V>> createEntries() {
/* 188 */     return new Entries();
/*     */   }
/*     */   
/*     */   class Entries
/*     */     extends ForwardingCollection<Map.Entry<K, V>>
/*     */   {
/*     */     protected Collection<Map.Entry<K, V>> delegate() {
/* 195 */       return Collections2.filter(FilteredKeyMultimap.this.unfiltered.entries(), FilteredKeyMultimap.this.entryPredicate());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean remove(@CheckForNull Object o) {
/* 201 */       if (o instanceof Map.Entry) {
/* 202 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 203 */         if (FilteredKeyMultimap.this.unfiltered.containsKey(entry.getKey()) && FilteredKeyMultimap.this.keyPredicate
/*     */           
/* 205 */           .apply(entry.getKey())) {
/* 206 */           return FilteredKeyMultimap.this.unfiltered.remove(entry.getKey(), entry.getValue());
/*     */         }
/*     */       } 
/* 209 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   Collection<V> createValues() {
/* 215 */     return new FilteredMultimapValues<>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   Map<K, Collection<V>> createAsMap() {
/* 220 */     return Maps.filterKeys(this.unfiltered.asMap(), this.keyPredicate);
/*     */   }
/*     */ 
/*     */   
/*     */   Multiset<K> createKeys() {
/* 225 */     return Multisets.filter(this.unfiltered.keys(), this.keyPredicate);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/FilteredKeyMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */