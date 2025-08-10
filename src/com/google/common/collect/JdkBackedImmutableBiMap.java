/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import com.google.j2objc.annotations.RetainedWith;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(emulated = true)
/*     */ final class JdkBackedImmutableBiMap<K, V>
/*     */   extends ImmutableBiMap<K, V>
/*     */ {
/*     */   private final transient ImmutableList<Map.Entry<K, V>> entries;
/*     */   private final Map<K, V> forwardDelegate;
/*     */   private final Map<V, K> backwardDelegate;
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   @RetainedWith
/*     */   private transient JdkBackedImmutableBiMap<V, K> inverse;
/*     */   
/*     */   @VisibleForTesting
/*     */   static <K, V> ImmutableBiMap<K, V> create(int n, Map.Entry<K, V>[] entryArray) {
/*  40 */     Map<K, V> forwardDelegate = Maps.newHashMapWithExpectedSize(n);
/*  41 */     Map<V, K> backwardDelegate = Maps.newHashMapWithExpectedSize(n);
/*  42 */     for (int i = 0; i < n; i++) {
/*     */       
/*  44 */       Map.Entry<K, V> e = RegularImmutableMap.makeImmutable(Objects.<Map.Entry<K, V>>requireNonNull(entryArray[i]));
/*  45 */       entryArray[i] = e;
/*  46 */       V oldValue = forwardDelegate.putIfAbsent(e.getKey(), e.getValue());
/*  47 */       if (oldValue != null) {
/*  48 */         throw conflictException("key", (new StringBuilder()).append(e.getKey()).append("=").append(oldValue).toString(), entryArray[i]);
/*     */       }
/*  50 */       K oldKey = backwardDelegate.putIfAbsent(e.getValue(), e.getKey());
/*  51 */       if (oldKey != null) {
/*  52 */         throw conflictException("value", (new StringBuilder()).append(oldKey).append("=").append(e.getValue()).toString(), entryArray[i]);
/*     */       }
/*     */     } 
/*  55 */     ImmutableList<Map.Entry<K, V>> entryList = ImmutableList.asImmutableList((Object[])entryArray, n);
/*  56 */     return new JdkBackedImmutableBiMap<>(entryList, forwardDelegate, backwardDelegate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JdkBackedImmutableBiMap(ImmutableList<Map.Entry<K, V>> entries, Map<K, V> forwardDelegate, Map<V, K> backwardDelegate) {
/*  65 */     this.entries = entries;
/*  66 */     this.forwardDelegate = forwardDelegate;
/*  67 */     this.backwardDelegate = backwardDelegate;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  72 */     return this.entries.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableBiMap<V, K> inverse() {
/*  79 */     JdkBackedImmutableBiMap<V, K> result = this.inverse;
/*  80 */     if (result == null) {
/*  81 */       this.inverse = result = new JdkBackedImmutableBiMap(new InverseEntries(), this.backwardDelegate, this.forwardDelegate);
/*     */ 
/*     */ 
/*     */       
/*  85 */       result.inverse = this;
/*     */     } 
/*  87 */     return result;
/*     */   }
/*     */   
/*     */   private final class InverseEntries extends ImmutableList<Map.Entry<V, K>> {
/*     */     private InverseEntries() {}
/*     */     
/*     */     public Map.Entry<V, K> get(int index) {
/*  94 */       Map.Entry<K, V> entry = JdkBackedImmutableBiMap.this.entries.get(index);
/*  95 */       return Maps.immutableEntry(entry.getValue(), entry.getKey());
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 100 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 105 */       return JdkBackedImmutableBiMap.this.entries.size();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @J2ktIncompatible
/*     */     @GwtIncompatible
/*     */     Object writeReplace() {
/* 114 */       return super.writeReplace();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public V get(@CheckForNull Object key) {
/* 121 */     return this.forwardDelegate.get(key);
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSet<Map.Entry<K, V>> createEntrySet() {
/* 126 */     return new ImmutableMapEntrySet.RegularEntrySet<>(this, this.entries);
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSet<K> createKeySet() {
/* 131 */     return new ImmutableMapKeySet<>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 136 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   Object writeReplace() {
/* 145 */     return super.writeReplace();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/JdkBackedImmutableBiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */