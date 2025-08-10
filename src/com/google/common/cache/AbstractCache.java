/*     */ package com.google.common.cache;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.ExecutionException;
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
/*     */ @GwtCompatible
/*     */ public abstract class AbstractCache<K, V>
/*     */   implements Cache<K, V>
/*     */ {
/*     */   public V get(K key, Callable<? extends V> valueLoader) throws ExecutionException {
/*  50 */     throw new UnsupportedOperationException();
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
/*     */   
/*     */   public ImmutableMap<K, V> getAllPresent(Iterable<? extends Object> keys) {
/*  68 */     Map<K, V> result = Maps.newLinkedHashMap();
/*  69 */     for (Object key : keys) {
/*  70 */       if (!result.containsKey(key)) {
/*     */         
/*  72 */         K castKey = (K)key;
/*  73 */         V value = getIfPresent(key);
/*  74 */         if (value != null) {
/*  75 */           result.put(castKey, value);
/*     */         }
/*     */       } 
/*     */     } 
/*  79 */     return ImmutableMap.copyOf(result);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(K key, V value) {
/*  85 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> m) {
/*  91 */     for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
/*  92 */       put(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void cleanUp() {}
/*     */ 
/*     */   
/*     */   public long size() {
/* 101 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void invalidate(Object key) {
/* 106 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void invalidateAll(Iterable<? extends Object> keys) {
/* 113 */     for (Object key : keys) {
/* 114 */       invalidate(key);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void invalidateAll() {
/* 120 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public CacheStats stats() {
/* 125 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ConcurrentMap<K, V> asMap() {
/* 130 */     throw new UnsupportedOperationException();
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class SimpleStatsCounter
/*     */     implements StatsCounter
/*     */   {
/* 203 */     private final LongAddable hitCount = LongAddables.create();
/* 204 */     private final LongAddable missCount = LongAddables.create();
/* 205 */     private final LongAddable loadSuccessCount = LongAddables.create();
/* 206 */     private final LongAddable loadExceptionCount = LongAddables.create();
/* 207 */     private final LongAddable totalLoadTime = LongAddables.create();
/* 208 */     private final LongAddable evictionCount = LongAddables.create();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void recordHits(int count) {
/* 216 */       this.hitCount.add(count);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void recordMisses(int count) {
/* 222 */       this.missCount.add(count);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void recordLoadSuccess(long loadTime) {
/* 228 */       this.loadSuccessCount.increment();
/* 229 */       this.totalLoadTime.add(loadTime);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void recordLoadException(long loadTime) {
/* 235 */       this.loadExceptionCount.increment();
/* 236 */       this.totalLoadTime.add(loadTime);
/*     */     }
/*     */ 
/*     */     
/*     */     public void recordEviction() {
/* 241 */       this.evictionCount.increment();
/*     */     }
/*     */ 
/*     */     
/*     */     public CacheStats snapshot() {
/* 246 */       return new CacheStats(
/* 247 */           negativeToMaxValue(this.hitCount.sum()), 
/* 248 */           negativeToMaxValue(this.missCount.sum()), 
/* 249 */           negativeToMaxValue(this.loadSuccessCount.sum()), 
/* 250 */           negativeToMaxValue(this.loadExceptionCount.sum()), 
/* 251 */           negativeToMaxValue(this.totalLoadTime.sum()), 
/* 252 */           negativeToMaxValue(this.evictionCount.sum()));
/*     */     }
/*     */ 
/*     */     
/*     */     private static long negativeToMaxValue(long value) {
/* 257 */       return (value >= 0L) ? value : Long.MAX_VALUE;
/*     */     }
/*     */ 
/*     */     
/*     */     public void incrementBy(AbstractCache.StatsCounter other) {
/* 262 */       CacheStats otherStats = other.snapshot();
/* 263 */       this.hitCount.add(otherStats.hitCount());
/* 264 */       this.missCount.add(otherStats.missCount());
/* 265 */       this.loadSuccessCount.add(otherStats.loadSuccessCount());
/* 266 */       this.loadExceptionCount.add(otherStats.loadExceptionCount());
/* 267 */       this.totalLoadTime.add(otherStats.totalLoadTime());
/* 268 */       this.evictionCount.add(otherStats.evictionCount());
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface StatsCounter {
/*     */     void recordHits(int param1Int);
/*     */     
/*     */     void recordMisses(int param1Int);
/*     */     
/*     */     void recordLoadSuccess(long param1Long);
/*     */     
/*     */     void recordLoadException(long param1Long);
/*     */     
/*     */     void recordEviction();
/*     */     
/*     */     CacheStats snapshot();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/cache/AbstractCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */