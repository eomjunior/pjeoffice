/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Map;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ final class MapRetrievalCache<K, V>
/*     */   extends MapIteratorCache<K, V>
/*     */ {
/*     */   @CheckForNull
/*     */   private volatile transient CacheEntry<K, V> cacheEntry1;
/*     */   @CheckForNull
/*     */   private volatile transient CacheEntry<K, V> cacheEntry2;
/*     */   
/*     */   MapRetrievalCache(Map<K, V> backingMap) {
/*  37 */     super(backingMap);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   V get(Object key) {
/*  44 */     Preconditions.checkNotNull(key);
/*  45 */     V value = getIfCached(key);
/*  46 */     if (value != null) {
/*  47 */       return value;
/*     */     }
/*     */     
/*  50 */     value = getWithoutCaching(key);
/*  51 */     if (value != null) {
/*  52 */       addToCache((K)key, value);
/*     */     }
/*  54 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   V getIfCached(@CheckForNull Object key) {
/*  62 */     V value = super.getIfCached(key);
/*  63 */     if (value != null) {
/*  64 */       return value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  72 */     CacheEntry<K, V> entry = this.cacheEntry1;
/*  73 */     if (entry != null && entry.key == key) {
/*  74 */       return entry.value;
/*     */     }
/*  76 */     entry = this.cacheEntry2;
/*  77 */     if (entry != null && entry.key == key) {
/*     */ 
/*     */       
/*  80 */       addToCache(entry);
/*  81 */       return entry.value;
/*     */     } 
/*  83 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   void clearCache() {
/*  88 */     super.clearCache();
/*  89 */     this.cacheEntry1 = null;
/*  90 */     this.cacheEntry2 = null;
/*     */   }
/*     */   
/*     */   private void addToCache(K key, V value) {
/*  94 */     addToCache(new CacheEntry<>(key, value));
/*     */   }
/*     */ 
/*     */   
/*     */   private void addToCache(CacheEntry<K, V> entry) {
/*  99 */     this.cacheEntry2 = this.cacheEntry1;
/* 100 */     this.cacheEntry1 = entry;
/*     */   }
/*     */   
/*     */   private static final class CacheEntry<K, V> {
/*     */     final K key;
/*     */     final V value;
/*     */     
/*     */     CacheEntry(K key, V value) {
/* 108 */       this.key = key;
/* 109 */       this.value = value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/MapRetrievalCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */