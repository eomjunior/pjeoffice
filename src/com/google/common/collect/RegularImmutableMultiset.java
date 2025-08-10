/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Set;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(emulated = true, serializable = true)
/*     */ class RegularImmutableMultiset<E>
/*     */   extends ImmutableMultiset<E>
/*     */ {
/*  42 */   private static final Multisets.ImmutableEntry<?>[] EMPTY_ARRAY = (Multisets.ImmutableEntry<?>[])new Multisets.ImmutableEntry[0];
/*  43 */   static final ImmutableMultiset<Object> EMPTY = create(ImmutableList.of()); @VisibleForTesting
/*     */   static final double MAX_LOAD_FACTOR = 1.0D; @VisibleForTesting
/*     */   static final double HASH_FLOODING_FPP = 0.001D; @VisibleForTesting
/*  46 */   static final int MAX_HASH_BUCKET_LENGTH = 9; private final transient Multisets.ImmutableEntry<E>[] entries; static <E> ImmutableMultiset<E> create(Collection<? extends Multiset.Entry<? extends E>> entries) { int distinct = entries.size();
/*     */     
/*  48 */     Multisets.ImmutableEntry[] arrayOfImmutableEntry1 = new Multisets.ImmutableEntry[distinct];
/*  49 */     if (distinct == 0) {
/*  50 */       return new RegularImmutableMultiset<>((Multisets.ImmutableEntry<E>[])arrayOfImmutableEntry1, EMPTY_ARRAY, 0, 0, ImmutableSet.of());
/*     */     }
/*  52 */     int tableSize = Hashing.closedTableSize(distinct, 1.0D);
/*  53 */     int mask = tableSize - 1;
/*     */ 
/*     */     
/*  56 */     Multisets.ImmutableEntry[] arrayOfImmutableEntry2 = new Multisets.ImmutableEntry[tableSize];
/*     */     
/*  58 */     int index = 0;
/*  59 */     int hashCode = 0;
/*  60 */     long size = 0L;
/*  61 */     for (Multiset.Entry<? extends E> entryWithWildcard : entries) {
/*     */       Multisets.ImmutableEntry<E> newEntry;
/*  63 */       Multiset.Entry<? extends E> entry1 = entryWithWildcard;
/*  64 */       E element = (E)Preconditions.checkNotNull(entry1.getElement());
/*  65 */       int count = entry1.getCount();
/*  66 */       int hash = element.hashCode();
/*  67 */       int bucket = Hashing.smear(hash) & mask;
/*  68 */       Multisets.ImmutableEntry<E> bucketHead = arrayOfImmutableEntry2[bucket];
/*     */       
/*  70 */       if (bucketHead == null) {
/*  71 */         boolean canReuseEntry = (entry1 instanceof Multisets.ImmutableEntry && !(entry1 instanceof NonTerminalEntry));
/*     */ 
/*     */         
/*  74 */         newEntry = canReuseEntry ? (Multisets.ImmutableEntry)entry1 : new Multisets.ImmutableEntry<>(element, count);
/*     */       } else {
/*  76 */         newEntry = new NonTerminalEntry<>(element, count, bucketHead);
/*     */       } 
/*  78 */       hashCode += hash ^ count;
/*  79 */       arrayOfImmutableEntry1[index++] = newEntry;
/*  80 */       arrayOfImmutableEntry2[bucket] = newEntry;
/*  81 */       size += count;
/*     */     } 
/*     */     
/*  84 */     return hashFloodingDetected((Multisets.ImmutableEntry<?>[])arrayOfImmutableEntry2) ? 
/*  85 */       JdkBackedImmutableMultiset.<E>create(ImmutableList.asImmutableList((Object[])arrayOfImmutableEntry1)) : 
/*  86 */       new RegularImmutableMultiset<>((Multisets.ImmutableEntry<E>[])arrayOfImmutableEntry1, (Multisets.ImmutableEntry<?>[])arrayOfImmutableEntry2, 
/*  87 */         Ints.saturatedCast(size), hashCode, null); }
/*     */    private final transient Multisets.ImmutableEntry<?>[] hashTable; private final transient int size; private final transient int hashCode; @LazyInit
/*     */   @CheckForNull
/*     */   private transient ImmutableSet<E> elementSet; private static boolean hashFloodingDetected(Multisets.ImmutableEntry<?>[] hashTable) {
/*  91 */     for (int i = 0; i < hashTable.length; i++) {
/*  92 */       int bucketLength = 0;
/*  93 */       for (Multisets.ImmutableEntry<?> entry = hashTable[i]; entry != null; entry = entry.nextInBucket()) {
/*  94 */         bucketLength++;
/*  95 */         if (bucketLength > 9) {
/*  96 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 100 */     return false;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private RegularImmutableMultiset(Multisets.ImmutableEntry<E>[] entries, Multisets.ImmutableEntry<?>[] hashTable, int size, int hashCode, @CheckForNull ImmutableSet<E> elementSet) {
/* 135 */     this.entries = entries;
/* 136 */     this.hashTable = hashTable;
/* 137 */     this.size = size;
/* 138 */     this.hashCode = hashCode;
/* 139 */     this.elementSet = elementSet;
/*     */   }
/*     */   
/*     */   private static final class NonTerminalEntry<E> extends Multisets.ImmutableEntry<E> {
/*     */     private final Multisets.ImmutableEntry<E> nextInBucket;
/*     */     
/*     */     NonTerminalEntry(E element, int count, Multisets.ImmutableEntry<E> nextInBucket) {
/* 146 */       super(element, count);
/* 147 */       this.nextInBucket = nextInBucket;
/*     */     }
/*     */ 
/*     */     
/*     */     public Multisets.ImmutableEntry<E> nextInBucket() {
/* 152 */       return this.nextInBucket;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 158 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int count(@CheckForNull Object element) {
/* 163 */     Multisets.ImmutableEntry<?>[] hashTable = this.hashTable;
/* 164 */     if (element == null || hashTable.length == 0) {
/* 165 */       return 0;
/*     */     }
/* 167 */     int hash = Hashing.smearedHash(element);
/* 168 */     int mask = hashTable.length - 1;
/* 169 */     Multisets.ImmutableEntry<?> entry = hashTable[hash & mask];
/* 170 */     for (; entry != null; 
/* 171 */       entry = entry.nextInBucket()) {
/* 172 */       if (Objects.equal(element, entry.getElement())) {
/* 173 */         return entry.getCount();
/*     */       }
/*     */     } 
/* 176 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 181 */     return this.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<E> elementSet() {
/* 186 */     ImmutableSet<E> result = this.elementSet;
/* 187 */     return (result == null) ? (this.elementSet = new ImmutableMultiset.ElementSet<>(Arrays.asList((Multiset.Entry[])this.entries), this)) : result;
/*     */   }
/*     */ 
/*     */   
/*     */   Multiset.Entry<E> getEntry(int index) {
/* 192 */     return this.entries[index];
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 197 */     return this.hashCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   Object writeReplace() {
/* 206 */     return super.writeReplace();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/RegularImmutableMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */