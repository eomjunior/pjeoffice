/*    */ package com.google.common.cache;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*    */ import java.util.concurrent.ExecutionException;
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
/*    */ @GwtIncompatible
/*    */ public abstract class ForwardingLoadingCache<K, V>
/*    */   extends ForwardingCache<K, V>
/*    */   implements LoadingCache<K, V>
/*    */ {
/*    */   @CanIgnoreReturnValue
/*    */   public V get(K key) throws ExecutionException {
/* 48 */     return delegate().get(key);
/*    */   }
/*    */ 
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   public V getUnchecked(K key) {
/* 54 */     return delegate().getUnchecked(key);
/*    */   }
/*    */ 
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   public ImmutableMap<K, V> getAll(Iterable<? extends K> keys) throws ExecutionException {
/* 60 */     return delegate().getAll(keys);
/*    */   }
/*    */ 
/*    */   
/*    */   public V apply(K key) {
/* 65 */     return delegate().apply(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public void refresh(K key) {
/* 70 */     delegate().refresh(key);
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract LoadingCache<K, V> delegate();
/*    */ 
/*    */   
/*    */   public static abstract class SimpleForwardingLoadingCache<K, V>
/*    */     extends ForwardingLoadingCache<K, V>
/*    */   {
/*    */     private final LoadingCache<K, V> delegate;
/*    */ 
/*    */     
/*    */     protected SimpleForwardingLoadingCache(LoadingCache<K, V> delegate) {
/* 84 */       this.delegate = (LoadingCache<K, V>)Preconditions.checkNotNull(delegate);
/*    */     }
/*    */ 
/*    */     
/*    */     protected final LoadingCache<K, V> delegate() {
/* 89 */       return this.delegate;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/cache/ForwardingLoadingCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */