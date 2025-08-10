/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import java.util.Arrays;
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
/*     */ 
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
/*     */ final class CompactHashing
/*     */ {
/*     */   static final byte UNSET = 0;
/*     */   private static final int HASH_TABLE_BITS_MAX_BITS = 5;
/*     */   static final int MODIFICATION_COUNT_INCREMENT = 32;
/*     */   static final int HASH_TABLE_BITS_MASK = 31;
/*     */   static final int MAX_SIZE = 1073741823;
/*     */   static final int DEFAULT_SIZE = 3;
/*     */   private static final int MIN_HASH_TABLE_SIZE = 4;
/*     */   private static final int BYTE_MAX_SIZE = 256;
/*     */   private static final int BYTE_MASK = 255;
/*     */   private static final int SHORT_MAX_SIZE = 65536;
/*     */   private static final int SHORT_MASK = 65535;
/*     */   
/*     */   static int tableSize(int expectedSize) {
/*  72 */     return Math.max(4, Hashing.closedTableSize(expectedSize + 1, 1.0D));
/*     */   }
/*     */ 
/*     */   
/*     */   static Object createTable(int buckets) {
/*  77 */     if (buckets < 2 || buckets > 1073741824 || 
/*     */       
/*  79 */       Integer.highestOneBit(buckets) != buckets) {
/*  80 */       throw new IllegalArgumentException("must be power of 2 between 2^1 and 2^30: " + buckets);
/*     */     }
/*  82 */     if (buckets <= 256)
/*  83 */       return new byte[buckets]; 
/*  84 */     if (buckets <= 65536) {
/*  85 */       return new short[buckets];
/*     */     }
/*  87 */     return new int[buckets];
/*     */   }
/*     */ 
/*     */   
/*     */   static void tableClear(Object table) {
/*  92 */     if (table instanceof byte[]) {
/*  93 */       Arrays.fill((byte[])table, (byte)0);
/*  94 */     } else if (table instanceof short[]) {
/*  95 */       Arrays.fill((short[])table, (short)0);
/*     */     } else {
/*  97 */       Arrays.fill((int[])table, 0);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int tableGet(Object table, int index) {
/* 107 */     if (table instanceof byte[])
/* 108 */       return ((byte[])table)[index] & 0xFF; 
/* 109 */     if (table instanceof short[]) {
/* 110 */       return ((short[])table)[index] & 0xFFFF;
/*     */     }
/* 112 */     return ((int[])table)[index];
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
/*     */   static void tableSet(Object table, int index, int entry) {
/* 124 */     if (table instanceof byte[]) {
/* 125 */       ((byte[])table)[index] = (byte)entry;
/* 126 */     } else if (table instanceof short[]) {
/* 127 */       ((short[])table)[index] = (short)entry;
/*     */     } else {
/* 129 */       ((int[])table)[index] = entry;
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
/*     */   static int newCapacity(int mask) {
/* 141 */     return ((mask < 32) ? 4 : 2) * (mask + 1);
/*     */   }
/*     */ 
/*     */   
/*     */   static int getHashPrefix(int value, int mask) {
/* 146 */     return value & (mask ^ 0xFFFFFFFF);
/*     */   }
/*     */ 
/*     */   
/*     */   static int getNext(int entry, int mask) {
/* 151 */     return entry & mask;
/*     */   }
/*     */ 
/*     */   
/*     */   static int maskCombine(int prefix, int suffix, int mask) {
/* 156 */     return prefix & (mask ^ 0xFFFFFFFF) | suffix & mask;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int remove(@CheckForNull Object key, @CheckForNull Object value, int mask, Object table, int[] entries, Object[] keys, @CheckForNull Object[] values) {
/* 167 */     int hash = Hashing.smearedHash(key);
/* 168 */     int tableIndex = hash & mask;
/* 169 */     int next = tableGet(table, tableIndex);
/* 170 */     if (next == 0) {
/* 171 */       return -1;
/*     */     }
/* 173 */     int hashPrefix = getHashPrefix(hash, mask);
/* 174 */     int lastEntryIndex = -1;
/*     */     while (true) {
/* 176 */       int entryIndex = next - 1;
/* 177 */       int entry = entries[entryIndex];
/* 178 */       if (getHashPrefix(entry, mask) == hashPrefix && 
/* 179 */         Objects.equal(key, keys[entryIndex]) && (values == null || 
/* 180 */         Objects.equal(value, values[entryIndex]))) {
/* 181 */         int newNext = getNext(entry, mask);
/* 182 */         if (lastEntryIndex == -1) {
/*     */           
/* 184 */           tableSet(table, tableIndex, newNext);
/*     */         } else {
/*     */           
/* 187 */           entries[lastEntryIndex] = maskCombine(entries[lastEntryIndex], newNext, mask);
/*     */         } 
/*     */         
/* 190 */         return entryIndex;
/*     */       } 
/* 192 */       lastEntryIndex = entryIndex;
/* 193 */       next = getNext(entry, mask);
/* 194 */       if (next == 0)
/* 195 */         return -1; 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/CompactHashing.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */