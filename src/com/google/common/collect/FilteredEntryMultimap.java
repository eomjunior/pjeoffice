/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ class FilteredEntryMultimap<K, V>
/*     */   extends AbstractMultimap<K, V>
/*     */   implements FilteredMultimap<K, V>
/*     */ {
/*     */   final Multimap<K, V> unfiltered;
/*     */   final Predicate<? super Map.Entry<K, V>> predicate;
/*     */   
/*     */   FilteredEntryMultimap(Multimap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> predicate) {
/*  53 */     this.unfiltered = (Multimap<K, V>)Preconditions.checkNotNull(unfiltered);
/*  54 */     this.predicate = (Predicate<? super Map.Entry<K, V>>)Preconditions.checkNotNull(predicate);
/*     */   }
/*     */ 
/*     */   
/*     */   public Multimap<K, V> unfiltered() {
/*  59 */     return this.unfiltered;
/*     */   }
/*     */ 
/*     */   
/*     */   public Predicate<? super Map.Entry<K, V>> entryPredicate() {
/*  64 */     return this.predicate;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  69 */     return entries().size();
/*     */   }
/*     */   
/*     */   private boolean satisfies(@ParametricNullness K key, @ParametricNullness V value) {
/*  73 */     return this.predicate.apply(Maps.immutableEntry(key, value));
/*     */   }
/*     */   
/*     */   final class ValuePredicate
/*     */     implements Predicate<V>
/*     */   {
/*     */     ValuePredicate(K key) {
/*  80 */       this.key = key;
/*     */     }
/*     */     @ParametricNullness
/*     */     private final K key;
/*     */     public boolean apply(@ParametricNullness V value) {
/*  85 */       return FilteredEntryMultimap.this.satisfies(this.key, value);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static <E> Collection<E> filterCollection(Collection<E> collection, Predicate<? super E> predicate) {
/*  91 */     if (collection instanceof Set) {
/*  92 */       return Sets.filter((Set<E>)collection, predicate);
/*     */     }
/*  94 */     return Collections2.filter(collection, predicate);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsKey(@CheckForNull Object key) {
/* 100 */     return (asMap().get(key) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<V> removeAll(@CheckForNull Object key) {
/* 105 */     return (Collection<V>)MoreObjects.firstNonNull(asMap().remove(key), unmodifiableEmptyCollection());
/*     */   }
/*     */ 
/*     */   
/*     */   Collection<V> unmodifiableEmptyCollection() {
/* 110 */     return (this.unfiltered instanceof SetMultimap) ? 
/* 111 */       Collections.<V>emptySet() : 
/* 112 */       Collections.<V>emptyList();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 117 */     entries().clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<V> get(@ParametricNullness K key) {
/* 122 */     return filterCollection(this.unfiltered.get(key), new ValuePredicate(key));
/*     */   }
/*     */ 
/*     */   
/*     */   Collection<Map.Entry<K, V>> createEntries() {
/* 127 */     return filterCollection(this.unfiltered.entries(), this.predicate);
/*     */   }
/*     */ 
/*     */   
/*     */   Collection<V> createValues() {
/* 132 */     return new FilteredMultimapValues<>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<Map.Entry<K, V>> entryIterator() {
/* 137 */     throw new AssertionError("should never be called");
/*     */   }
/*     */ 
/*     */   
/*     */   Map<K, Collection<V>> createAsMap() {
/* 142 */     return new AsMap();
/*     */   }
/*     */ 
/*     */   
/*     */   Set<K> createKeySet() {
/* 147 */     return asMap().keySet();
/*     */   }
/*     */   
/*     */   boolean removeEntriesIf(Predicate<? super Map.Entry<K, Collection<V>>> predicate) {
/* 151 */     Iterator<Map.Entry<K, Collection<V>>> entryIterator = this.unfiltered.asMap().entrySet().iterator();
/* 152 */     boolean changed = false;
/* 153 */     while (entryIterator.hasNext()) {
/* 154 */       Map.Entry<K, Collection<V>> entry = entryIterator.next();
/* 155 */       K key = entry.getKey();
/* 156 */       Collection<V> collection = filterCollection(entry.getValue(), new ValuePredicate(key));
/* 157 */       if (!collection.isEmpty() && predicate.apply(Maps.immutableEntry(key, collection))) {
/* 158 */         if (collection.size() == ((Collection)entry.getValue()).size()) {
/* 159 */           entryIterator.remove();
/*     */         } else {
/* 161 */           collection.clear();
/*     */         } 
/* 163 */         changed = true;
/*     */       } 
/*     */     } 
/* 166 */     return changed;
/*     */   }
/*     */   
/*     */   class AsMap
/*     */     extends Maps.ViewCachingAbstractMap<K, Collection<V>>
/*     */   {
/*     */     public boolean containsKey(@CheckForNull Object key) {
/* 173 */       return (get(key) != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 178 */       FilteredEntryMultimap.this.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public Collection<V> get(@CheckForNull Object key) {
/* 184 */       Collection<V> result = (Collection<V>)FilteredEntryMultimap.this.unfiltered.asMap().get(key);
/* 185 */       if (result == null) {
/* 186 */         return null;
/*     */       }
/*     */       
/* 189 */       K k = (K)key;
/* 190 */       result = FilteredEntryMultimap.filterCollection(result, new FilteredEntryMultimap.ValuePredicate(k));
/* 191 */       return result.isEmpty() ? null : result;
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public Collection<V> remove(@CheckForNull Object key) {
/* 197 */       Collection<V> collection = (Collection<V>)FilteredEntryMultimap.this.unfiltered.asMap().get(key);
/* 198 */       if (collection == null) {
/* 199 */         return null;
/*     */       }
/*     */       
/* 202 */       K k = (K)key;
/* 203 */       List<V> result = Lists.newArrayList();
/* 204 */       Iterator<V> itr = collection.iterator();
/* 205 */       while (itr.hasNext()) {
/* 206 */         V v = itr.next();
/* 207 */         if (FilteredEntryMultimap.this.satisfies(k, v)) {
/* 208 */           itr.remove();
/* 209 */           result.add(v);
/*     */         } 
/*     */       } 
/* 212 */       if (result.isEmpty())
/* 213 */         return null; 
/* 214 */       if (FilteredEntryMultimap.this.unfiltered instanceof SetMultimap) {
/* 215 */         return Collections.unmodifiableSet(Sets.newLinkedHashSet(result));
/*     */       }
/* 217 */       return Collections.unmodifiableList(result);
/*     */     }
/*     */ 
/*     */     
/*     */     Set<K> createKeySet() {
/*     */       class KeySetImpl
/*     */         extends Maps.KeySet<K, Collection<V>>
/*     */       {
/*     */         KeySetImpl() {
/* 226 */           super(FilteredEntryMultimap.AsMap.this);
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean removeAll(Collection<?> c) {
/* 231 */           return FilteredEntryMultimap.this.removeEntriesIf(Maps.keyPredicateOnEntries(Predicates.in(c)));
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean retainAll(Collection<?> c) {
/* 236 */           return FilteredEntryMultimap.this.removeEntriesIf(Maps.keyPredicateOnEntries(Predicates.not(Predicates.in(c))));
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean remove(@CheckForNull Object o) {
/* 241 */           return (FilteredEntryMultimap.AsMap.this.remove(o) != null);
/*     */         }
/*     */       };
/* 244 */       return new KeySetImpl();
/*     */     }
/*     */ 
/*     */     
/*     */     Set<Map.Entry<K, Collection<V>>> createEntrySet() {
/*     */       class EntrySetImpl
/*     */         extends Maps.EntrySet<K, Collection<V>>
/*     */       {
/*     */         Map<K, Collection<V>> map() {
/* 253 */           return FilteredEntryMultimap.AsMap.this;
/*     */         }
/*     */ 
/*     */         
/*     */         public Iterator<Map.Entry<K, Collection<V>>> iterator() {
/* 258 */           return new AbstractIterator<Map.Entry<K, Collection<V>>>() {
/* 259 */               final Iterator<Map.Entry<K, Collection<V>>> backingIterator = FilteredEntryMultimap.this.unfiltered
/* 260 */                 .asMap().entrySet().iterator();
/*     */ 
/*     */               
/*     */               @CheckForNull
/*     */               protected Map.Entry<K, Collection<V>> computeNext() {
/* 265 */                 while (this.backingIterator.hasNext()) {
/* 266 */                   Map.Entry<K, Collection<V>> entry = this.backingIterator.next();
/* 267 */                   K key = entry.getKey();
/*     */                   
/* 269 */                   Collection<V> collection = FilteredEntryMultimap.filterCollection(entry.getValue(), new FilteredEntryMultimap.ValuePredicate(key));
/* 270 */                   if (!collection.isEmpty()) {
/* 271 */                     return Maps.immutableEntry(key, collection);
/*     */                   }
/*     */                 } 
/* 274 */                 return endOfData();
/*     */               }
/*     */             };
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean removeAll(Collection<?> c) {
/* 281 */           return FilteredEntryMultimap.this.removeEntriesIf(Predicates.in(c));
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean retainAll(Collection<?> c) {
/* 286 */           return FilteredEntryMultimap.this.removeEntriesIf(Predicates.not(Predicates.in(c)));
/*     */         }
/*     */ 
/*     */         
/*     */         public int size() {
/* 291 */           return Iterators.size(iterator());
/*     */         }
/*     */       };
/* 294 */       return new EntrySetImpl();
/*     */     }
/*     */     
/*     */     Collection<Collection<V>> createValues() {
/*     */       class ValuesImpl
/*     */         extends Maps.Values<K, Collection<V>>
/*     */       {
/*     */         ValuesImpl() {
/* 302 */           super(FilteredEntryMultimap.AsMap.this);
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean remove(@CheckForNull Object o) {
/* 307 */           if (o instanceof Collection) {
/* 308 */             Collection<?> c = (Collection)o;
/*     */             
/* 310 */             Iterator<Map.Entry<K, Collection<V>>> entryIterator = FilteredEntryMultimap.this.unfiltered.asMap().entrySet().iterator();
/* 311 */             while (entryIterator.hasNext()) {
/* 312 */               Map.Entry<K, Collection<V>> entry = entryIterator.next();
/* 313 */               K key = entry.getKey();
/*     */               
/* 315 */               Collection<V> collection = FilteredEntryMultimap.filterCollection(entry.getValue(), new FilteredEntryMultimap.ValuePredicate(key));
/* 316 */               if (!collection.isEmpty() && c.equals(collection)) {
/* 317 */                 if (collection.size() == ((Collection)entry.getValue()).size()) {
/* 318 */                   entryIterator.remove();
/*     */                 } else {
/* 320 */                   collection.clear();
/*     */                 } 
/* 322 */                 return true;
/*     */               } 
/*     */             } 
/*     */           } 
/* 326 */           return false;
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean removeAll(Collection<?> c) {
/* 331 */           return FilteredEntryMultimap.this.removeEntriesIf(Maps.valuePredicateOnEntries(Predicates.in(c)));
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean retainAll(Collection<?> c) {
/* 336 */           return FilteredEntryMultimap.this.removeEntriesIf(Maps.valuePredicateOnEntries(Predicates.not(Predicates.in(c))));
/*     */         }
/*     */       };
/* 339 */       return new ValuesImpl();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   Multiset<K> createKeys() {
/* 345 */     return new Keys();
/*     */   }
/*     */   
/*     */   class Keys
/*     */     extends Multimaps.Keys<K, V> {
/*     */     Keys() {
/* 351 */       super(FilteredEntryMultimap.this);
/*     */     }
/*     */ 
/*     */     
/*     */     public int remove(@CheckForNull Object key, int occurrences) {
/* 356 */       CollectPreconditions.checkNonnegative(occurrences, "occurrences");
/* 357 */       if (occurrences == 0) {
/* 358 */         return count(key);
/*     */       }
/* 360 */       Collection<V> collection = (Collection<V>)FilteredEntryMultimap.this.unfiltered.asMap().get(key);
/* 361 */       if (collection == null) {
/* 362 */         return 0;
/*     */       }
/*     */       
/* 365 */       K k = (K)key;
/* 366 */       int oldCount = 0;
/* 367 */       Iterator<V> itr = collection.iterator();
/* 368 */       while (itr.hasNext()) {
/* 369 */         V v = itr.next();
/*     */         
/* 371 */         oldCount++;
/* 372 */         if (FilteredEntryMultimap.this.satisfies(k, v) && oldCount <= occurrences) {
/* 373 */           itr.remove();
/*     */         }
/*     */       } 
/*     */       
/* 377 */       return oldCount;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<Multiset.Entry<K>> entrySet() {
/* 382 */       return new Multisets.EntrySet<K>()
/*     */         {
/*     */           Multiset<K> multiset()
/*     */           {
/* 386 */             return FilteredEntryMultimap.Keys.this;
/*     */           }
/*     */ 
/*     */           
/*     */           public Iterator<Multiset.Entry<K>> iterator() {
/* 391 */             return FilteredEntryMultimap.Keys.this.entryIterator();
/*     */           }
/*     */ 
/*     */           
/*     */           public int size() {
/* 396 */             return FilteredEntryMultimap.this.keySet().size();
/*     */           }
/*     */           
/*     */           private boolean removeEntriesIf(Predicate<? super Multiset.Entry<K>> predicate) {
/* 400 */             return FilteredEntryMultimap.this.removeEntriesIf(entry -> predicate.apply(Multisets.immutableEntry(entry.getKey(), ((Collection)entry.getValue()).size())));
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public boolean removeAll(Collection<?> c) {
/* 408 */             return removeEntriesIf(Predicates.in(c));
/*     */           }
/*     */ 
/*     */           
/*     */           public boolean retainAll(Collection<?> c) {
/* 413 */             return removeEntriesIf(Predicates.not(Predicates.in(c)));
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/FilteredEntryMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */