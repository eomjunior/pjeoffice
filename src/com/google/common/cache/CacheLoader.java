/*     */ package com.google.common.cache;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.common.util.concurrent.Futures;
/*     */ import com.google.common.util.concurrent.ListenableFuture;
/*     */ import com.google.common.util.concurrent.ListenableFutureTask;
/*     */ import java.io.Serializable;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Executor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(emulated = true)
/*     */ public abstract class CacheLoader<K, V>
/*     */ {
/*     */   public abstract V load(K paramK) throws Exception;
/*     */   
/*     */   @GwtIncompatible
/*     */   public ListenableFuture<V> reload(K key, V oldValue) throws Exception {
/*  98 */     Preconditions.checkNotNull(key);
/*  99 */     Preconditions.checkNotNull(oldValue);
/* 100 */     return Futures.immediateFuture(load(key));
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
/*     */   public Map<K, V> loadAll(Iterable<? extends K> keys) throws Exception {
/* 128 */     throw new UnsupportedLoadingOperationException();
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
/*     */   public static <K, V> CacheLoader<K, V> from(Function<K, V> function) {
/* 141 */     return new FunctionToCacheLoader<>(function);
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
/*     */   public static <V> CacheLoader<Object, V> from(Supplier<V> supplier) {
/* 156 */     return new SupplierToCacheLoader<>(supplier);
/*     */   }
/*     */   
/*     */   private static final class FunctionToCacheLoader<K, V> extends CacheLoader<K, V> implements Serializable {
/*     */     private final Function<K, V> computingFunction;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public FunctionToCacheLoader(Function<K, V> computingFunction) {
/* 164 */       this.computingFunction = (Function<K, V>)Preconditions.checkNotNull(computingFunction);
/*     */     }
/*     */ 
/*     */     
/*     */     public V load(K key) {
/* 169 */       return (V)this.computingFunction.apply(Preconditions.checkNotNull(key));
/*     */     }
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
/*     */   @GwtIncompatible
/*     */   public static <K, V> CacheLoader<K, V> asyncReloading(final CacheLoader<K, V> loader, final Executor executor) {
/* 187 */     Preconditions.checkNotNull(loader);
/* 188 */     Preconditions.checkNotNull(executor);
/* 189 */     return new CacheLoader<K, V>()
/*     */       {
/*     */         public V load(K key) throws Exception {
/* 192 */           return loader.load(key);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public ListenableFuture<V> reload(K key, V oldValue) {
/* 198 */           ListenableFutureTask<V> task = ListenableFutureTask.create(() -> loader.reload(key, (V)oldValue).get());
/* 199 */           executor.execute((Runnable)task);
/* 200 */           return (ListenableFuture<V>)task;
/*     */         }
/*     */ 
/*     */         
/*     */         public Map<K, V> loadAll(Iterable<? extends K> keys) throws Exception {
/* 205 */           return loader.loadAll(keys);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static final class SupplierToCacheLoader<V> extends CacheLoader<Object, V> implements Serializable {
/*     */     private final Supplier<V> computingSupplier;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public SupplierToCacheLoader(Supplier<V> computingSupplier) {
/* 215 */       this.computingSupplier = (Supplier<V>)Preconditions.checkNotNull(computingSupplier);
/*     */     }
/*     */ 
/*     */     
/*     */     public V load(Object key) {
/* 220 */       Preconditions.checkNotNull(key);
/* 221 */       return (V)this.computingSupplier.get();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class UnsupportedLoadingOperationException
/*     */     extends UnsupportedOperationException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class InvalidCacheLoadException
/*     */     extends RuntimeException
/*     */   {
/*     */     public InvalidCacheLoadException(String message) {
/* 246 */       super(message);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/cache/CacheLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */