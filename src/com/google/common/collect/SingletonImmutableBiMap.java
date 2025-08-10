/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import com.google.j2objc.annotations.RetainedWith;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiConsumer;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ final class SingletonImmutableBiMap<K, V>
/*     */   extends ImmutableBiMap<K, V>
/*     */ {
/*     */   final transient K singleKey;
/*     */   final transient V singleValue;
/*     */   @CheckForNull
/*     */   private final transient ImmutableBiMap<V, K> inverse;
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   @RetainedWith
/*     */   private transient ImmutableBiMap<V, K> lazyInverse;
/*     */   
/*     */   SingletonImmutableBiMap(K singleKey, V singleValue) {
/*  45 */     CollectPreconditions.checkEntryNotNull(singleKey, singleValue);
/*  46 */     this.singleKey = singleKey;
/*  47 */     this.singleValue = singleValue;
/*  48 */     this.inverse = null;
/*     */   }
/*     */   
/*     */   private SingletonImmutableBiMap(K singleKey, V singleValue, ImmutableBiMap<V, K> inverse) {
/*  52 */     this.singleKey = singleKey;
/*  53 */     this.singleValue = singleValue;
/*  54 */     this.inverse = inverse;
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public V get(@CheckForNull Object key) {
/*  60 */     return this.singleKey.equals(key) ? this.singleValue : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  65 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(BiConsumer<? super K, ? super V> action) {
/*  70 */     ((BiConsumer<K, V>)Preconditions.checkNotNull(action)).accept(this.singleKey, this.singleValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(@CheckForNull Object key) {
/*  75 */     return this.singleKey.equals(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(@CheckForNull Object value) {
/*  80 */     return this.singleValue.equals(value);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/*  85 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSet<Map.Entry<K, V>> createEntrySet() {
/*  90 */     return ImmutableSet.of(Maps.immutableEntry(this.singleKey, this.singleValue));
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSet<K> createKeySet() {
/*  95 */     return ImmutableSet.of(this.singleKey);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableBiMap<V, K> inverse() {
/* 103 */     if (this.inverse != null) {
/* 104 */       return this.inverse;
/*     */     }
/*     */     
/* 107 */     ImmutableBiMap<V, K> result = this.lazyInverse;
/* 108 */     if (result == null) {
/* 109 */       return this.lazyInverse = new SingletonImmutableBiMap((K)this.singleValue, (V)this.singleKey, this);
/*     */     }
/* 111 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   Object writeReplace() {
/* 122 */     return super.writeReplace();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/SingletonImmutableBiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */