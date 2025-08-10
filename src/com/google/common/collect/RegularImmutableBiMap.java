/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import com.google.j2objc.annotations.RetainedWith;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
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
/*     */ class RegularImmutableBiMap<K, V>
/*     */   extends ImmutableBiMap<K, V>
/*     */ {
/*  52 */   static final RegularImmutableBiMap<Object, Object> EMPTY = new RegularImmutableBiMap(null, null, (Map.Entry[])ImmutableMap.EMPTY_ENTRY_ARRAY, 0, 0);
/*     */ 
/*     */   
/*     */   static final double MAX_LOAD_FACTOR = 1.2D;
/*     */   
/*     */   @CheckForNull
/*     */   private final transient ImmutableMapEntry<K, V>[] keyTable;
/*     */   
/*     */   @CheckForNull
/*     */   private final transient ImmutableMapEntry<K, V>[] valueTable;
/*     */ 
/*     */   
/*     */   static <K, V> ImmutableBiMap<K, V> fromEntries(Map.Entry<K, V>... entries) {
/*  65 */     return fromEntryArray(entries.length, entries); } @VisibleForTesting
/*     */   final transient Map.Entry<K, V>[] entries; private final transient int mask; private final transient int hashCode; @LazyInit
/*     */   @CheckForNull
/*     */   @RetainedWith
/*  69 */   private transient ImmutableBiMap<V, K> inverse; static <K, V> ImmutableBiMap<K, V> fromEntryArray(int n, Map.Entry<K, V>[] entryArray) { Preconditions.checkPositionIndex(n, entryArray.length);
/*  70 */     int tableSize = Hashing.closedTableSize(n, 1.2D);
/*  71 */     int mask = tableSize - 1;
/*  72 */     ImmutableMapEntry[] arrayOfImmutableMapEntry1 = (ImmutableMapEntry[])ImmutableMapEntry.createEntryArray(tableSize);
/*  73 */     ImmutableMapEntry[] arrayOfImmutableMapEntry2 = (ImmutableMapEntry[])ImmutableMapEntry.createEntryArray(tableSize);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  81 */     Map.Entry<K, V>[] entries = (n == entryArray.length) ? entryArray : (Map.Entry[])ImmutableMapEntry.createEntryArray(n);
/*  82 */     int hashCode = 0;
/*     */     
/*  84 */     for (int i = 0; i < n; i++) {
/*     */       
/*  86 */       Map.Entry<K, V> entry = Objects.<Map.Entry<K, V>>requireNonNull(entryArray[i]);
/*  87 */       K key = entry.getKey();
/*  88 */       V value = entry.getValue();
/*  89 */       CollectPreconditions.checkEntryNotNull(key, value);
/*  90 */       int keyHash = key.hashCode();
/*  91 */       int valueHash = value.hashCode();
/*  92 */       int keyBucket = Hashing.smear(keyHash) & mask;
/*  93 */       int valueBucket = Hashing.smear(valueHash) & mask;
/*     */       
/*  95 */       ImmutableMapEntry<K, V> nextInKeyBucket = arrayOfImmutableMapEntry1[keyBucket];
/*  96 */       ImmutableMapEntry<K, V> nextInValueBucket = arrayOfImmutableMapEntry2[valueBucket];
/*     */       try {
/*  98 */         RegularImmutableMap.checkNoConflictInKeyBucket(key, value, nextInKeyBucket, true);
/*  99 */         checkNoConflictInValueBucket(value, entry, nextInValueBucket);
/* 100 */       } catch (BucketOverflowException e) {
/* 101 */         return JdkBackedImmutableBiMap.create(n, entryArray);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 106 */       ImmutableMapEntry<K, V> newEntry = (nextInValueBucket == null && nextInKeyBucket == null) ? RegularImmutableMap.<K, V>makeImmutable(entry, key, value) : new ImmutableMapEntry.NonTerminalImmutableBiMapEntry<>(key, value, nextInKeyBucket, nextInValueBucket);
/*     */       
/* 108 */       arrayOfImmutableMapEntry1[keyBucket] = newEntry;
/* 109 */       arrayOfImmutableMapEntry2[valueBucket] = newEntry;
/* 110 */       entries[i] = newEntry;
/* 111 */       hashCode += keyHash ^ valueHash;
/*     */     } 
/* 113 */     return new RegularImmutableBiMap<>((ImmutableMapEntry<K, V>[])arrayOfImmutableMapEntry1, (ImmutableMapEntry<K, V>[])arrayOfImmutableMapEntry2, entries, mask, hashCode); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private RegularImmutableBiMap(@CheckForNull ImmutableMapEntry<K, V>[] keyTable, @CheckForNull ImmutableMapEntry<K, V>[] valueTable, Map.Entry<K, V>[] entries, int mask, int hashCode) {
/* 122 */     this.keyTable = keyTable;
/* 123 */     this.valueTable = valueTable;
/* 124 */     this.entries = entries;
/* 125 */     this.mask = mask;
/* 126 */     this.hashCode = hashCode;
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
/*     */   private static void checkNoConflictInValueBucket(Object value, Map.Entry<?, ?> entry, @CheckForNull ImmutableMapEntry<?, ?> valueBucketHead) throws RegularImmutableMap.BucketOverflowException {
/* 139 */     int bucketSize = 0;
/* 140 */     for (; valueBucketHead != null; valueBucketHead = valueBucketHead.getNextInValueBucket()) {
/* 141 */       checkNoConflict(!value.equals(valueBucketHead.getValue()), "value", entry, valueBucketHead);
/* 142 */       if (++bucketSize > 8) {
/* 143 */         throw new RegularImmutableMap.BucketOverflowException();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public V get(@CheckForNull Object key) {
/* 151 */     return RegularImmutableMap.get(key, (ImmutableMapEntry<?, V>[])this.keyTable, this.mask);
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSet<Map.Entry<K, V>> createEntrySet() {
/* 156 */     return isEmpty() ? 
/* 157 */       ImmutableSet.<Map.Entry<K, V>>of() : 
/* 158 */       new ImmutableMapEntrySet.RegularEntrySet<>(this, this.entries);
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSet<K> createKeySet() {
/* 163 */     return new ImmutableMapKeySet<>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(BiConsumer<? super K, ? super V> action) {
/* 168 */     Preconditions.checkNotNull(action);
/* 169 */     for (Map.Entry<K, V> entry : this.entries) {
/* 170 */       action.accept(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isHashCodeFast() {
/* 176 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 181 */     return this.hashCode;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 186 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 191 */     return this.entries.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableBiMap<V, K> inverse() {
/* 198 */     if (isEmpty()) {
/* 199 */       return ImmutableBiMap.of();
/*     */     }
/* 201 */     ImmutableBiMap<V, K> result = this.inverse;
/* 202 */     return (result == null) ? (this.inverse = new Inverse()) : result;
/*     */   }
/*     */   
/*     */   private final class Inverse extends ImmutableBiMap<V, K> {
/*     */     private Inverse() {}
/*     */     
/*     */     public int size() {
/* 209 */       return inverse().size();
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableBiMap<K, V> inverse() {
/* 214 */       return RegularImmutableBiMap.this;
/*     */     }
/*     */ 
/*     */     
/*     */     public void forEach(BiConsumer<? super V, ? super K> action) {
/* 219 */       Preconditions.checkNotNull(action);
/* 220 */       RegularImmutableBiMap.this.forEach((k, v) -> action.accept(v, k));
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public K get(@CheckForNull Object value) {
/* 226 */       if (value == null || RegularImmutableBiMap.this.valueTable == null) {
/* 227 */         return null;
/*     */       }
/* 229 */       int bucket = Hashing.smear(value.hashCode()) & RegularImmutableBiMap.this.mask;
/* 230 */       ImmutableMapEntry<K, V> entry = RegularImmutableBiMap.this.valueTable[bucket];
/* 231 */       for (; entry != null; 
/* 232 */         entry = entry.getNextInValueBucket()) {
/* 233 */         if (value.equals(entry.getValue())) {
/* 234 */           return entry.getKey();
/*     */         }
/*     */       } 
/* 237 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSet<V> createKeySet() {
/* 242 */       return new ImmutableMapKeySet<>(this);
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSet<Map.Entry<V, K>> createEntrySet() {
/* 247 */       return new InverseEntrySet();
/*     */     }
/*     */     
/*     */     final class InverseEntrySet
/*     */       extends ImmutableMapEntrySet<V, K> {
/*     */       ImmutableMap<V, K> map() {
/* 253 */         return RegularImmutableBiMap.Inverse.this;
/*     */       }
/*     */ 
/*     */       
/*     */       boolean isHashCodeFast() {
/* 258 */         return true;
/*     */       }
/*     */ 
/*     */       
/*     */       public int hashCode() {
/* 263 */         return RegularImmutableBiMap.this.hashCode;
/*     */       }
/*     */ 
/*     */       
/*     */       public UnmodifiableIterator<Map.Entry<V, K>> iterator() {
/* 268 */         return asList().iterator();
/*     */       }
/*     */ 
/*     */       
/*     */       public void forEach(Consumer<? super Map.Entry<V, K>> action) {
/* 273 */         asList().forEach(action);
/*     */       }
/*     */ 
/*     */       
/*     */       ImmutableList<Map.Entry<V, K>> createAsList() {
/* 278 */         return new ImmutableAsList<Map.Entry<V, K>>()
/*     */           {
/*     */             public Map.Entry<V, K> get(int index) {
/* 281 */               Map.Entry<K, V> entry = RegularImmutableBiMap.this.entries[index];
/* 282 */               return Maps.immutableEntry(entry.getValue(), entry.getKey());
/*     */             }
/*     */ 
/*     */             
/*     */             ImmutableCollection<Map.Entry<V, K>> delegateCollection() {
/* 287 */               return RegularImmutableBiMap.Inverse.InverseEntrySet.this;
/*     */             }
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             @J2ktIncompatible
/*     */             @GwtIncompatible
/*     */             Object writeReplace() {
/* 296 */               return super.writeReplace();
/*     */             }
/*     */           };
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       @J2ktIncompatible
/*     */       @GwtIncompatible
/*     */       Object writeReplace() {
/* 307 */         return super.writeReplace();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 313 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     @J2ktIncompatible
/*     */     @GwtIncompatible
/*     */     Object writeReplace() {
/* 320 */       return new RegularImmutableBiMap.InverseSerializedForm<>(RegularImmutableBiMap.this);
/*     */     }
/*     */     
/*     */     @J2ktIncompatible
/*     */     private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 325 */       throw new InvalidObjectException("Use InverseSerializedForm");
/*     */     } }
/*     */   
/*     */   @J2ktIncompatible
/*     */   private static class InverseSerializedForm<K, V> implements Serializable {
/*     */     private final ImmutableBiMap<K, V> forward;
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     InverseSerializedForm(ImmutableBiMap<K, V> forward) {
/* 334 */       this.forward = forward;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 338 */       return this.forward.inverse();
/*     */     }
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
/* 350 */     return super.writeReplace();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/RegularImmutableBiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */