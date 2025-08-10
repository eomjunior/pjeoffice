/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
/*     */ import java.util.IdentityHashMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ final class RegularImmutableMap<K, V>
/*     */   extends ImmutableMap<K, V>
/*     */ {
/*  48 */   static final ImmutableMap<Object, Object> EMPTY = new RegularImmutableMap((Map.Entry[])ImmutableMap.EMPTY_ENTRY_ARRAY, null, 0);
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static final double MAX_LOAD_FACTOR = 1.2D;
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static final double HASH_FLOODING_FPP = 0.001D;
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static final int MAX_HASH_BUCKET_LENGTH = 8;
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   final transient Map.Entry<K, V>[] entries;
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   private final transient ImmutableMapEntry<K, V>[] table;
/*     */ 
/*     */   
/*     */   private final transient int mask;
/*     */   
/*     */   @J2ktIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */ 
/*     */   
/*     */   static <K, V> ImmutableMap<K, V> fromEntries(Map.Entry<K, V>... entries) {
/*  78 */     return fromEntryArray(entries.length, entries, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> ImmutableMap<K, V> fromEntryArray(int n, Map.Entry<K, V>[] entryArray, boolean throwIfDuplicateKeys) {
/*  88 */     Preconditions.checkPositionIndex(n, entryArray.length);
/*  89 */     if (n == 0)
/*     */     {
/*  91 */       return (ImmutableMap)EMPTY;
/*     */     }
/*     */     
/*     */     try {
/*  95 */       return fromEntryArrayCheckingBucketOverflow(n, entryArray, throwIfDuplicateKeys);
/*  96 */     } catch (BucketOverflowException e) {
/*     */ 
/*     */       
/*  99 */       return JdkBackedImmutableMap.create(n, entryArray, throwIfDuplicateKeys);
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
/*     */   private static <K, V> ImmutableMap<K, V> fromEntryArrayCheckingBucketOverflow(int n, Map.Entry<K, V>[] entryArray, boolean throwIfDuplicateKeys) throws BucketOverflowException {
/*     */     Map.Entry[] arrayOfEntry;
/* 113 */     Map.Entry<K, V>[] entries = (n == entryArray.length) ? entryArray : (Map.Entry[])ImmutableMapEntry.createEntryArray(n);
/* 114 */     int tableSize = Hashing.closedTableSize(n, 1.2D);
/* 115 */     ImmutableMapEntry[] arrayOfImmutableMapEntry = (ImmutableMapEntry[])ImmutableMapEntry.createEntryArray(tableSize);
/* 116 */     int mask = tableSize - 1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 122 */     IdentityHashMap<Map.Entry<K, V>, Boolean> duplicates = null;
/* 123 */     int dupCount = 0;
/* 124 */     for (int entryIndex = n - 1; entryIndex >= 0; entryIndex--) {
/*     */       
/* 126 */       Map.Entry<K, V> entry = Objects.<Map.Entry<K, V>>requireNonNull(entryArray[entryIndex]);
/* 127 */       K key = entry.getKey();
/* 128 */       V value = entry.getValue();
/* 129 */       CollectPreconditions.checkEntryNotNull(key, value);
/* 130 */       int tableIndex = Hashing.smear(key.hashCode()) & mask;
/* 131 */       ImmutableMapEntry<K, V> keyBucketHead = arrayOfImmutableMapEntry[tableIndex];
/*     */       
/* 133 */       ImmutableMapEntry<K, V> effectiveEntry = checkNoConflictInKeyBucket(key, value, keyBucketHead, throwIfDuplicateKeys);
/* 134 */       if (effectiveEntry == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 139 */         effectiveEntry = (keyBucketHead == null) ? makeImmutable(entry, key, value) : new ImmutableMapEntry.NonTerminalImmutableMapEntry<>(key, value, keyBucketHead);
/* 140 */         arrayOfImmutableMapEntry[tableIndex] = effectiveEntry;
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 145 */         if (duplicates == null) {
/* 146 */           duplicates = new IdentityHashMap<>();
/*     */         }
/* 148 */         duplicates.put(effectiveEntry, Boolean.valueOf(true));
/* 149 */         dupCount++;
/*     */ 
/*     */         
/* 152 */         if (entries == entryArray) {
/*     */ 
/*     */           
/* 155 */           Map.Entry<K, V>[] originalEntries = entries;
/* 156 */           arrayOfEntry = (Map.Entry[])originalEntries.clone();
/*     */         } 
/*     */       } 
/* 159 */       arrayOfEntry[entryIndex] = effectiveEntry;
/*     */     } 
/* 161 */     if (duplicates != null) {
/*     */       
/* 163 */       arrayOfEntry = removeDuplicates((Map.Entry<K, V>[])arrayOfEntry, n, n - dupCount, duplicates);
/* 164 */       int newTableSize = Hashing.closedTableSize(arrayOfEntry.length, 1.2D);
/* 165 */       if (newTableSize != tableSize) {
/* 166 */         return fromEntryArrayCheckingBucketOverflow(arrayOfEntry.length, (Map.Entry<K, V>[])arrayOfEntry, true);
/*     */       }
/*     */     } 
/*     */     
/* 170 */     return new RegularImmutableMap<>((Map.Entry<K, V>[])arrayOfEntry, (ImmutableMapEntry<K, V>[])arrayOfImmutableMapEntry, mask);
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
/*     */   static <K, V> Map.Entry<K, V>[] removeDuplicates(Map.Entry<K, V>[] entries, int n, int newN, IdentityHashMap<Map.Entry<K, V>, Boolean> duplicates) {
/* 187 */     ImmutableMapEntry[] arrayOfImmutableMapEntry = (ImmutableMapEntry[])ImmutableMapEntry.createEntryArray(newN);
/* 188 */     for (int in = 0, out = 0; in < n; in++) {
/* 189 */       Map.Entry<K, V> entry = entries[in];
/* 190 */       Boolean status = duplicates.get(entry);
/*     */       
/* 192 */       if (status != null) {
/* 193 */         if (status.booleanValue()) {
/* 194 */           duplicates.put(entry, Boolean.valueOf(false));
/*     */         } else {
/*     */           continue;
/*     */         } 
/*     */       }
/* 199 */       arrayOfImmutableMapEntry[out++] = (ImmutableMapEntry)entry; continue;
/*     */     } 
/* 201 */     return (Map.Entry<K, V>[])arrayOfImmutableMapEntry;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> ImmutableMapEntry<K, V> makeImmutable(Map.Entry<K, V> entry, K key, V value) {
/* 207 */     boolean reusable = (entry instanceof ImmutableMapEntry && ((ImmutableMapEntry)entry).isReusable());
/* 208 */     return reusable ? (ImmutableMapEntry<K, V>)entry : new ImmutableMapEntry<>(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   static <K, V> ImmutableMapEntry<K, V> makeImmutable(Map.Entry<K, V> entry) {
/* 213 */     return makeImmutable(entry, entry.getKey(), entry.getValue());
/*     */   }
/*     */ 
/*     */   
/*     */   private RegularImmutableMap(Map.Entry<K, V>[] entries, @CheckForNull ImmutableMapEntry<K, V>[] table, int mask) {
/* 218 */     this.entries = entries;
/* 219 */     this.table = table;
/* 220 */     this.mask = mask;
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
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   static <K, V> ImmutableMapEntry<K, V> checkNoConflictInKeyBucket(Object key, Object newValue, @CheckForNull ImmutableMapEntry<K, V> keyBucketHead, boolean throwIfDuplicateKeys) throws BucketOverflowException {
/* 242 */     int bucketSize = 0;
/* 243 */     for (; keyBucketHead != null; keyBucketHead = keyBucketHead.getNextInKeyBucket()) {
/* 244 */       if (keyBucketHead.getKey().equals(key)) {
/* 245 */         if (throwIfDuplicateKeys) {
/* 246 */           checkNoConflict(false, "key", keyBucketHead, key + "=" + newValue);
/*     */         } else {
/* 248 */           return keyBucketHead;
/*     */         } 
/*     */       }
/* 251 */       if (++bucketSize > 8) {
/* 252 */         throw new BucketOverflowException();
/*     */       }
/*     */     } 
/* 255 */     return null;
/*     */   }
/*     */   
/*     */   static class BucketOverflowException
/*     */     extends Exception {}
/*     */   
/*     */   @CheckForNull
/*     */   public V get(@CheckForNull Object key) {
/* 263 */     return get(key, (ImmutableMapEntry<?, V>[])this.table, this.mask);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   static <V> V get(@CheckForNull Object key, @CheckForNull ImmutableMapEntry<?, V>[] keyTable, int mask) {
/* 271 */     if (key == null || keyTable == null) {
/* 272 */       return null;
/*     */     }
/* 274 */     int index = Hashing.smear(key.hashCode()) & mask;
/* 275 */     ImmutableMapEntry<?, V> entry = keyTable[index];
/* 276 */     for (; entry != null; 
/* 277 */       entry = entry.getNextInKeyBucket()) {
/* 278 */       Object candidateKey = entry.getKey();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 286 */       if (key.equals(candidateKey)) {
/* 287 */         return entry.getValue();
/*     */       }
/*     */     } 
/* 290 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(BiConsumer<? super K, ? super V> action) {
/* 295 */     Preconditions.checkNotNull(action);
/* 296 */     for (Map.Entry<K, V> entry : this.entries) {
/* 297 */       action.accept(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 303 */     return this.entries.length;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 308 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSet<Map.Entry<K, V>> createEntrySet() {
/* 313 */     return new ImmutableMapEntrySet.RegularEntrySet<>(this, this.entries);
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSet<K> createKeySet() {
/* 318 */     return new KeySet<>(this);
/*     */   }
/*     */   
/*     */   @GwtCompatible(emulated = true)
/*     */   private static final class KeySet<K> extends IndexedImmutableSet<K> {
/*     */     private final RegularImmutableMap<K, ?> map;
/*     */     
/*     */     KeySet(RegularImmutableMap<K, ?> map) {
/* 326 */       this.map = map;
/*     */     }
/*     */ 
/*     */     
/*     */     K get(int index) {
/* 331 */       return this.map.entries[index].getKey();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(@CheckForNull Object object) {
/* 336 */       return this.map.containsKey(object);
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 341 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 346 */       return this.map.size();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @J2ktIncompatible
/*     */     @GwtIncompatible
/*     */     Object writeReplace() {
/* 355 */       return super.writeReplace();
/*     */     }
/*     */     
/*     */     @GwtIncompatible
/*     */     @J2ktIncompatible
/*     */     private static class SerializedForm<K> implements Serializable {
/*     */       final ImmutableMap<K, ?> map;
/*     */       @J2ktIncompatible
/*     */       private static final long serialVersionUID = 0L;
/*     */       
/*     */       SerializedForm(ImmutableMap<K, ?> map) {
/* 366 */         this.map = map;
/*     */       }
/*     */       
/*     */       Object readResolve() {
/* 370 */         return this.map.keySet();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableCollection<V> createValues() {
/* 380 */     return new Values<>(this);
/*     */   }
/*     */   
/*     */   @GwtCompatible(emulated = true)
/*     */   private static final class Values<K, V> extends ImmutableList<V> {
/*     */     final RegularImmutableMap<K, V> map;
/*     */     
/*     */     Values(RegularImmutableMap<K, V> map) {
/* 388 */       this.map = map;
/*     */     }
/*     */ 
/*     */     
/*     */     public V get(int index) {
/* 393 */       return this.map.entries[index].getValue();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 398 */       return this.map.size();
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 403 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @J2ktIncompatible
/*     */     @GwtIncompatible
/*     */     Object writeReplace() {
/* 412 */       return super.writeReplace();
/*     */     }
/*     */     
/*     */     @GwtIncompatible
/*     */     @J2ktIncompatible
/*     */     private static class SerializedForm<V> implements Serializable {
/*     */       final ImmutableMap<?, V> map;
/*     */       @J2ktIncompatible
/*     */       private static final long serialVersionUID = 0L;
/*     */       
/*     */       SerializedForm(ImmutableMap<?, V> map) {
/* 423 */         this.map = map;
/*     */       }
/*     */       
/*     */       Object readResolve() {
/* 427 */         return this.map.values();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   Object writeReplace() {
/* 441 */     return super.writeReplace();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/RegularImmutableMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */