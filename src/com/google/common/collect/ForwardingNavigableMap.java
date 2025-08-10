/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NavigableMap;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.SortedMap;
/*     */ import java.util.function.BiFunction;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtIncompatible
/*     */ public abstract class ForwardingNavigableMap<K, V>
/*     */   extends ForwardingSortedMap<K, V>
/*     */   implements NavigableMap<K, V>
/*     */ {
/*     */   @CheckForNull
/*     */   public Map.Entry<K, V> lowerEntry(@ParametricNullness K key) {
/*  71 */     return delegate().lowerEntry(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   protected Map.Entry<K, V> standardLowerEntry(@ParametricNullness K key) {
/*  81 */     return headMap(key, false).lastEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public K lowerKey(@ParametricNullness K key) {
/*  87 */     return delegate().lowerKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   protected K standardLowerKey(@ParametricNullness K key) {
/*  97 */     return Maps.keyOrNull(lowerEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Map.Entry<K, V> floorEntry(@ParametricNullness K key) {
/* 103 */     return delegate().floorEntry(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   protected Map.Entry<K, V> standardFloorEntry(@ParametricNullness K key) {
/* 113 */     return headMap(key, true).lastEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public K floorKey(@ParametricNullness K key) {
/* 119 */     return delegate().floorKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   protected K standardFloorKey(@ParametricNullness K key) {
/* 129 */     return Maps.keyOrNull(floorEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Map.Entry<K, V> ceilingEntry(@ParametricNullness K key) {
/* 135 */     return delegate().ceilingEntry(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   protected Map.Entry<K, V> standardCeilingEntry(@ParametricNullness K key) {
/* 145 */     return tailMap(key, true).firstEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public K ceilingKey(@ParametricNullness K key) {
/* 151 */     return delegate().ceilingKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   protected K standardCeilingKey(@ParametricNullness K key) {
/* 161 */     return Maps.keyOrNull(ceilingEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Map.Entry<K, V> higherEntry(@ParametricNullness K key) {
/* 167 */     return delegate().higherEntry(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   protected Map.Entry<K, V> standardHigherEntry(@ParametricNullness K key) {
/* 177 */     return tailMap(key, false).firstEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public K higherKey(@ParametricNullness K key) {
/* 183 */     return delegate().higherKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   protected K standardHigherKey(@ParametricNullness K key) {
/* 193 */     return Maps.keyOrNull(higherEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Map.Entry<K, V> firstEntry() {
/* 199 */     return delegate().firstEntry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   protected Map.Entry<K, V> standardFirstEntry() {
/* 209 */     return Iterables.<Map.Entry<K, V>>getFirst(entrySet(), null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected K standardFirstKey() {
/* 218 */     Map.Entry<K, V> entry = firstEntry();
/* 219 */     if (entry == null) {
/* 220 */       throw new NoSuchElementException();
/*     */     }
/* 222 */     return entry.getKey();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Map.Entry<K, V> lastEntry() {
/* 229 */     return delegate().lastEntry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   protected Map.Entry<K, V> standardLastEntry() {
/* 239 */     return Iterables.<Map.Entry<K, V>>getFirst(descendingMap().entrySet(), null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected K standardLastKey() {
/* 247 */     Map.Entry<K, V> entry = lastEntry();
/* 248 */     if (entry == null) {
/* 249 */       throw new NoSuchElementException();
/*     */     }
/* 251 */     return entry.getKey();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Map.Entry<K, V> pollFirstEntry() {
/* 258 */     return delegate().pollFirstEntry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   protected Map.Entry<K, V> standardPollFirstEntry() {
/* 268 */     return Iterators.<Map.Entry<K, V>>pollNext(entrySet().iterator());
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Map.Entry<K, V> pollLastEntry() {
/* 274 */     return delegate().pollLastEntry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   protected Map.Entry<K, V> standardPollLastEntry() {
/* 284 */     return Iterators.<Map.Entry<K, V>>pollNext(descendingMap().entrySet().iterator());
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableMap<K, V> descendingMap() {
/* 289 */     return delegate().descendingMap();
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
/*     */   protected class StandardDescendingMap
/*     */     extends Maps.DescendingMap<K, V>
/*     */   {
/*     */     NavigableMap<K, V> forward() {
/* 309 */       return ForwardingNavigableMap.this;
/*     */     }
/*     */ 
/*     */     
/*     */     public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
/* 314 */       forward().replaceAll(function);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Iterator<Map.Entry<K, V>> entryIterator() {
/* 319 */       return new Iterator<Map.Entry<K, V>>() { @CheckForNull
/* 320 */           private Map.Entry<K, V> toRemove = null; @CheckForNull
/* 321 */           private Map.Entry<K, V> nextOrNull = ForwardingNavigableMap.StandardDescendingMap.this.forward().lastEntry();
/*     */ 
/*     */           
/*     */           public boolean hasNext() {
/* 325 */             return (this.nextOrNull != null);
/*     */           }
/*     */ 
/*     */           
/*     */           public Map.Entry<K, V> next() {
/* 330 */             if (this.nextOrNull == null) {
/* 331 */               throw new NoSuchElementException();
/*     */             }
/*     */             try {
/* 334 */               return this.nextOrNull;
/*     */             } finally {
/* 336 */               this.toRemove = this.nextOrNull;
/* 337 */               this.nextOrNull = ForwardingNavigableMap.StandardDescendingMap.this.forward().lowerEntry(this.nextOrNull.getKey());
/*     */             } 
/*     */           }
/*     */ 
/*     */           
/*     */           public void remove() {
/* 343 */             if (this.toRemove == null) {
/* 344 */               throw new IllegalStateException("no calls to next() since the last call to remove()");
/*     */             }
/* 346 */             ForwardingNavigableMap.StandardDescendingMap.this.forward().remove(this.toRemove.getKey());
/* 347 */             this.toRemove = null;
/*     */           } }
/*     */         ;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<K> navigableKeySet() {
/* 355 */     return delegate().navigableKeySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class StandardNavigableKeySet
/*     */     extends Maps.NavigableKeySet<K, V>
/*     */   {
/*     */     public StandardNavigableKeySet(ForwardingNavigableMap<K, V> this$0) {
/* 369 */       super(this$0);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<K> descendingKeySet() {
/* 375 */     return delegate().descendingKeySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected NavigableSet<K> standardDescendingKeySet() {
/* 386 */     return descendingMap().navigableKeySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SortedMap<K, V> standardSubMap(@ParametricNullness K fromKey, @ParametricNullness K toKey) {
/* 397 */     return subMap(fromKey, true, toKey, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NavigableMap<K, V> subMap(@ParametricNullness K fromKey, boolean fromInclusive, @ParametricNullness K toKey, boolean toInclusive) {
/* 406 */     return delegate().subMap(fromKey, fromInclusive, toKey, toInclusive);
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableMap<K, V> headMap(@ParametricNullness K toKey, boolean inclusive) {
/* 411 */     return delegate().headMap(toKey, inclusive);
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableMap<K, V> tailMap(@ParametricNullness K fromKey, boolean inclusive) {
/* 416 */     return delegate().tailMap(fromKey, inclusive);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SortedMap<K, V> standardHeadMap(@ParametricNullness K toKey) {
/* 425 */     return headMap(toKey, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SortedMap<K, V> standardTailMap(@ParametricNullness K fromKey) {
/* 434 */     return tailMap(fromKey, true);
/*     */   }
/*     */   
/*     */   protected abstract NavigableMap<K, V> delegate();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ForwardingNavigableMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */