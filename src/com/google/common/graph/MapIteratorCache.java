/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Iterator;
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
/*     */ class MapIteratorCache<K, V>
/*     */ {
/*     */   private final Map<K, V> backingMap;
/*     */   @CheckForNull
/*     */   private volatile transient Map.Entry<K, V> cacheEntry;
/*     */   
/*     */   MapIteratorCache(Map<K, V> backingMap) {
/*  60 */     this.backingMap = (Map<K, V>)Preconditions.checkNotNull(backingMap);
/*     */   }
/*     */   
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   final V put(K key, V value) {
/*  66 */     Preconditions.checkNotNull(key);
/*  67 */     Preconditions.checkNotNull(value);
/*  68 */     clearCache();
/*  69 */     return this.backingMap.put(key, value);
/*     */   }
/*     */   
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   final V remove(Object key) {
/*  75 */     Preconditions.checkNotNull(key);
/*  76 */     clearCache();
/*  77 */     return this.backingMap.remove(key);
/*     */   }
/*     */   
/*     */   final void clear() {
/*  81 */     clearCache();
/*  82 */     this.backingMap.clear();
/*     */   }
/*     */   
/*     */   @CheckForNull
/*     */   V get(Object key) {
/*  87 */     Preconditions.checkNotNull(key);
/*  88 */     V value = getIfCached(key);
/*     */     
/*  90 */     if (value == null) {
/*  91 */       return getWithoutCaching(key);
/*     */     }
/*  93 */     return value;
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   final V getWithoutCaching(Object key) {
/*  99 */     Preconditions.checkNotNull(key);
/* 100 */     return this.backingMap.get(key);
/*     */   }
/*     */   
/*     */   final boolean containsKey(@CheckForNull Object key) {
/* 104 */     return (getIfCached(key) != null || this.backingMap.containsKey(key));
/*     */   }
/*     */   
/*     */   final Set<K> unmodifiableKeySet() {
/* 108 */     return new AbstractSet<K>()
/*     */       {
/*     */         public UnmodifiableIterator<K> iterator() {
/* 111 */           final Iterator<Map.Entry<K, V>> entryIterator = MapIteratorCache.this.backingMap.entrySet().iterator();
/*     */           
/* 113 */           return new UnmodifiableIterator<K>()
/*     */             {
/*     */               public boolean hasNext() {
/* 116 */                 return entryIterator.hasNext();
/*     */               }
/*     */ 
/*     */               
/*     */               public K next() {
/* 121 */                 Map.Entry<K, V> entry = entryIterator.next();
/* 122 */                 MapIteratorCache.this.cacheEntry = entry;
/* 123 */                 return entry.getKey();
/*     */               }
/*     */             };
/*     */         }
/*     */ 
/*     */         
/*     */         public int size() {
/* 130 */           return MapIteratorCache.this.backingMap.size();
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean contains(@CheckForNull Object key) {
/* 135 */           return MapIteratorCache.this.containsKey(key);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   V getIfCached(@CheckForNull Object key) {
/* 144 */     Map.Entry<K, V> entry = this.cacheEntry;
/*     */ 
/*     */     
/* 147 */     if (entry != null && entry.getKey() == key) {
/* 148 */       return entry.getValue();
/*     */     }
/* 150 */     return null;
/*     */   }
/*     */   
/*     */   void clearCache() {
/* 154 */     this.cacheEntry = null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/MapIteratorCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */