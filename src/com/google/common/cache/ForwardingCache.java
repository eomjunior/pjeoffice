/*     */ package com.google.common.cache;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ForwardingObject;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.ExecutionException;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtIncompatible
/*     */ public abstract class ForwardingCache<K, V>
/*     */   extends ForwardingObject
/*     */   implements Cache<K, V>
/*     */ {
/*     */   @CheckForNull
/*     */   public V getIfPresent(Object key) {
/*  49 */     return delegate().getIfPresent(key);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public V get(K key, Callable<? extends V> valueLoader) throws ExecutionException {
/*  55 */     return delegate().get(key, valueLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMap<K, V> getAllPresent(Iterable<? extends Object> keys) {
/*  65 */     return delegate().getAllPresent(keys);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(K key, V value) {
/*  71 */     delegate().put(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> m) {
/*  77 */     delegate().putAll(m);
/*     */   }
/*     */ 
/*     */   
/*     */   public void invalidate(Object key) {
/*  82 */     delegate().invalidate(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void invalidateAll(Iterable<? extends Object> keys) {
/*  89 */     delegate().invalidateAll(keys);
/*     */   }
/*     */ 
/*     */   
/*     */   public void invalidateAll() {
/*  94 */     delegate().invalidateAll();
/*     */   }
/*     */ 
/*     */   
/*     */   public long size() {
/*  99 */     return delegate().size();
/*     */   }
/*     */ 
/*     */   
/*     */   public CacheStats stats() {
/* 104 */     return delegate().stats();
/*     */   }
/*     */ 
/*     */   
/*     */   public ConcurrentMap<K, V> asMap() {
/* 109 */     return delegate().asMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public void cleanUp() {
/* 114 */     delegate().cleanUp();
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract Cache<K, V> delegate();
/*     */ 
/*     */   
/*     */   public static abstract class SimpleForwardingCache<K, V>
/*     */     extends ForwardingCache<K, V>
/*     */   {
/*     */     private final Cache<K, V> delegate;
/*     */     
/*     */     protected SimpleForwardingCache(Cache<K, V> delegate) {
/* 127 */       this.delegate = (Cache<K, V>)Preconditions.checkNotNull(delegate);
/*     */     }
/*     */ 
/*     */     
/*     */     protected final Cache<K, V> delegate() {
/* 132 */       return this.delegate;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/cache/ForwardingCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */