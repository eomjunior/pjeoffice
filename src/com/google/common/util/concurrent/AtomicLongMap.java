/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.function.LongBinaryOperator;
/*     */ import java.util.function.LongUnaryOperator;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ @J2ktIncompatible
/*     */ public final class AtomicLongMap<K>
/*     */   implements Serializable
/*     */ {
/*     */   private final ConcurrentHashMap<K, Long> map;
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   private transient Map<K, Long> asMap;
/*     */   
/*     */   private AtomicLongMap(ConcurrentHashMap<K, Long> map) {
/*  68 */     this.map = (ConcurrentHashMap<K, Long>)Preconditions.checkNotNull(map);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K> AtomicLongMap<K> create() {
/*  73 */     return new AtomicLongMap<>(new ConcurrentHashMap<>());
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K> AtomicLongMap<K> create(Map<? extends K, ? extends Long> m) {
/*  78 */     AtomicLongMap<K> result = create();
/*  79 */     result.putAll(m);
/*  80 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long get(K key) {
/*  88 */     return ((Long)this.map.getOrDefault(key, Long.valueOf(0L))).longValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public long incrementAndGet(K key) {
/*  96 */     return addAndGet(key, 1L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public long decrementAndGet(K key) {
/* 104 */     return addAndGet(key, -1L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public long addAndGet(K key, long delta) {
/* 113 */     return accumulateAndGet(key, delta, Long::sum);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public long getAndIncrement(K key) {
/* 121 */     return getAndAdd(key, 1L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public long getAndDecrement(K key) {
/* 129 */     return getAndAdd(key, -1L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public long getAndAdd(K key, long delta) {
/* 138 */     return getAndAccumulate(key, delta, Long::sum);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public long updateAndGet(K key, LongUnaryOperator updaterFunction) {
/* 150 */     Preconditions.checkNotNull(updaterFunction);
/*     */     
/* 152 */     Long result = this.map.compute(key, (k, value) -> Long.valueOf(updaterFunction.applyAsLong((value == null) ? 0L : value.longValue())));
/*     */ 
/*     */     
/* 155 */     return ((Long)Objects.<Long>requireNonNull(result)).longValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public long getAndUpdate(K key, LongUnaryOperator updaterFunction) {
/* 167 */     Preconditions.checkNotNull(updaterFunction);
/* 168 */     AtomicLong holder = new AtomicLong();
/* 169 */     this.map.compute(key, (k, value) -> {
/*     */           long oldValue = (value == null) ? 0L : value.longValue();
/*     */           
/*     */           holder.set(oldValue);
/*     */           
/*     */           return Long.valueOf(updaterFunction.applyAsLong(oldValue));
/*     */         });
/* 176 */     return holder.get();
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
/*     */   @CanIgnoreReturnValue
/*     */   public long accumulateAndGet(K key, long x, LongBinaryOperator accumulatorFunction) {
/* 189 */     Preconditions.checkNotNull(accumulatorFunction);
/* 190 */     return updateAndGet(key, oldValue -> accumulatorFunction.applyAsLong(oldValue, x));
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
/*     */   @CanIgnoreReturnValue
/*     */   public long getAndAccumulate(K key, long x, LongBinaryOperator accumulatorFunction) {
/* 203 */     Preconditions.checkNotNull(accumulatorFunction);
/* 204 */     return getAndUpdate(key, oldValue -> accumulatorFunction.applyAsLong(oldValue, x));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public long put(K key, long newValue) {
/* 213 */     return getAndUpdate(key, x -> newValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends Long> m) {
/* 223 */     m.forEach(this::put);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public long remove(K key) {
/* 232 */     Long result = this.map.remove(key);
/* 233 */     return (result == null) ? 0L : result.longValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean remove(K key, long value) {
/* 241 */     return this.map.remove(key, Long.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean removeIfZero(K key) {
/* 251 */     return remove(key, 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeAllZeros() {
/* 261 */     this.map.values().removeIf(x -> (x.longValue() == 0L));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long sum() {
/* 270 */     return this.map.values().stream().mapToLong(Long::longValue).sum();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<K, Long> asMap() {
/* 277 */     Map<K, Long> result = this.asMap;
/* 278 */     return (result == null) ? (this.asMap = createAsMap()) : result;
/*     */   }
/*     */   
/*     */   private Map<K, Long> createAsMap() {
/* 282 */     return Collections.unmodifiableMap(this.map);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 287 */     return this.map.containsKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 295 */     return this.map.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 300 */     return this.map.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 310 */     this.map.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 315 */     return this.map.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long putIfAbsent(K key, long newValue) {
/* 324 */     AtomicBoolean noValue = new AtomicBoolean(false);
/*     */     
/* 326 */     Long result = this.map.compute(key, (k, oldValue) -> {
/*     */           if (oldValue == null || oldValue.longValue() == 0L) {
/*     */             noValue.set(true);
/*     */             
/*     */             return Long.valueOf(newValue);
/*     */           } 
/*     */           
/*     */           return oldValue;
/*     */         });
/*     */     
/* 336 */     return noValue.get() ? 0L : ((Long)Objects.<Long>requireNonNull(result)).longValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean replace(K key, long expectedOldValue, long newValue) {
/* 347 */     if (expectedOldValue == 0L) {
/* 348 */       return (putIfAbsent(key, newValue) == 0L);
/*     */     }
/* 350 */     return this.map.replace(key, Long.valueOf(expectedOldValue), Long.valueOf(newValue));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/AtomicLongMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */