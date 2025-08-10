/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NavigableMap;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtIncompatible
/*     */ abstract class AbstractNavigableMap<K, V>
/*     */   extends Maps.IteratorBasedAbstractMap<K, V>
/*     */   implements NavigableMap<K, V>
/*     */ {
/*     */   @CheckForNull
/*     */   public abstract V get(@CheckForNull Object paramObject);
/*     */   
/*     */   @CheckForNull
/*     */   public Map.Entry<K, V> firstEntry() {
/*  47 */     return Iterators.<Map.Entry<K, V>>getNext(entryIterator(), null);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Map.Entry<K, V> lastEntry() {
/*  53 */     return Iterators.<Map.Entry<K, V>>getNext(descendingEntryIterator(), null);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Map.Entry<K, V> pollFirstEntry() {
/*  59 */     return Iterators.<Map.Entry<K, V>>pollNext(entryIterator());
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Map.Entry<K, V> pollLastEntry() {
/*  65 */     return Iterators.<Map.Entry<K, V>>pollNext(descendingEntryIterator());
/*     */   }
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   public K firstKey() {
/*  71 */     Map.Entry<K, V> entry = firstEntry();
/*  72 */     if (entry == null) {
/*  73 */       throw new NoSuchElementException();
/*     */     }
/*  75 */     return entry.getKey();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   public K lastKey() {
/*  82 */     Map.Entry<K, V> entry = lastEntry();
/*  83 */     if (entry == null) {
/*  84 */       throw new NoSuchElementException();
/*     */     }
/*  86 */     return entry.getKey();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Map.Entry<K, V> lowerEntry(@ParametricNullness K key) {
/*  93 */     return headMap(key, false).lastEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Map.Entry<K, V> floorEntry(@ParametricNullness K key) {
/*  99 */     return headMap(key, true).lastEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Map.Entry<K, V> ceilingEntry(@ParametricNullness K key) {
/* 105 */     return tailMap(key, true).firstEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public Map.Entry<K, V> higherEntry(@ParametricNullness K key) {
/* 111 */     return tailMap(key, false).firstEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public K lowerKey(@ParametricNullness K key) {
/* 117 */     return Maps.keyOrNull(lowerEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public K floorKey(@ParametricNullness K key) {
/* 123 */     return Maps.keyOrNull(floorEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public K ceilingKey(@ParametricNullness K key) {
/* 129 */     return Maps.keyOrNull(ceilingEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public K higherKey(@ParametricNullness K key) {
/* 135 */     return Maps.keyOrNull(higherEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   abstract Iterator<Map.Entry<K, V>> descendingEntryIterator();
/*     */   
/*     */   public SortedMap<K, V> subMap(@ParametricNullness K fromKey, @ParametricNullness K toKey) {
/* 142 */     return subMap(fromKey, true, toKey, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMap<K, V> headMap(@ParametricNullness K toKey) {
/* 147 */     return headMap(toKey, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMap<K, V> tailMap(@ParametricNullness K fromKey) {
/* 152 */     return tailMap(fromKey, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<K> navigableKeySet() {
/* 157 */     return new Maps.NavigableKeySet<>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 162 */     return navigableKeySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<K> descendingKeySet() {
/* 167 */     return descendingMap().navigableKeySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableMap<K, V> descendingMap() {
/* 172 */     return new DescendingMap();
/*     */   }
/*     */   
/*     */   private final class DescendingMap
/*     */     extends Maps.DescendingMap<K, V> {
/*     */     NavigableMap<K, V> forward() {
/* 178 */       return AbstractNavigableMap.this;
/*     */     }
/*     */     private DescendingMap() {}
/*     */     
/*     */     Iterator<Map.Entry<K, V>> entryIterator() {
/* 183 */       return AbstractNavigableMap.this.descendingEntryIterator();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/AbstractNavigableMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */