/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(emulated = true)
/*     */ final class JdkBackedImmutableMap<K, V>
/*     */   extends ImmutableMap<K, V>
/*     */ {
/*     */   private final transient Map<K, V> delegateMap;
/*     */   private final transient ImmutableList<Map.Entry<K, V>> entries;
/*     */   
/*     */   static <K, V> ImmutableMap<K, V> create(int n, Map.Entry<K, V>[] entryArray, boolean throwIfDuplicateKeys) {
/*     */     Map.Entry[] arrayOfEntry;
/*  46 */     Map<K, V> delegateMap = Maps.newHashMapWithExpectedSize(n);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  51 */     Map<K, V> duplicates = null;
/*  52 */     int dupCount = 0;
/*  53 */     for (int i = 0; i < n; i++) {
/*     */       
/*  55 */       entryArray[i] = RegularImmutableMap.makeImmutable(Objects.<Map.Entry<K, V>>requireNonNull(entryArray[i]));
/*  56 */       K key = entryArray[i].getKey();
/*  57 */       V value = entryArray[i].getValue();
/*  58 */       V oldValue = delegateMap.put(key, value);
/*  59 */       if (oldValue != null) {
/*  60 */         if (throwIfDuplicateKeys) {
/*  61 */           throw conflictException("key", entryArray[i], (new StringBuilder()).append(entryArray[i].getKey()).append("=").append(oldValue).toString());
/*     */         }
/*  63 */         if (duplicates == null) {
/*  64 */           duplicates = new HashMap<>();
/*     */         }
/*  66 */         duplicates.put(key, value);
/*  67 */         dupCount++;
/*     */       } 
/*     */     } 
/*  70 */     if (duplicates != null) {
/*     */       
/*  72 */       Map.Entry[] arrayOfEntry1 = new Map.Entry[n - dupCount];
/*  73 */       for (int inI = 0, outI = 0; inI < n; inI++) {
/*  74 */         Map.Entry<K, V> entry = Objects.<Map.Entry<K, V>>requireNonNull(entryArray[inI]);
/*  75 */         K key = entry.getKey();
/*  76 */         if (duplicates.containsKey(key)) {
/*  77 */           V value = duplicates.get(key);
/*  78 */           if (value == null) {
/*     */             continue;
/*     */           }
/*  81 */           entry = new ImmutableMapEntry<>(key, value);
/*  82 */           duplicates.put(key, null);
/*     */         } 
/*  84 */         arrayOfEntry1[outI++] = entry; continue;
/*     */       } 
/*  86 */       arrayOfEntry = arrayOfEntry1;
/*     */     } 
/*  88 */     return new JdkBackedImmutableMap<>(delegateMap, ImmutableList.asImmutableList((Object[])arrayOfEntry, n));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   JdkBackedImmutableMap(Map<K, V> delegateMap, ImmutableList<Map.Entry<K, V>> entries) {
/*  95 */     this.delegateMap = delegateMap;
/*  96 */     this.entries = entries;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 101 */     return this.entries.size();
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public V get(@CheckForNull Object key) {
/* 107 */     return this.delegateMap.get(key);
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSet<Map.Entry<K, V>> createEntrySet() {
/* 112 */     return new ImmutableMapEntrySet.RegularEntrySet<>(this, this.entries);
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(BiConsumer<? super K, ? super V> action) {
/* 117 */     Preconditions.checkNotNull(action);
/* 118 */     this.entries.forEach(e -> action.accept(e.getKey(), e.getValue()));
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSet<K> createKeySet() {
/* 123 */     return new ImmutableMapKeySet<>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableCollection<V> createValues() {
/* 128 */     return new ImmutableMapValues<>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 133 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   Object writeReplace() {
/* 142 */     return super.writeReplace();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/JdkBackedImmutableMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */