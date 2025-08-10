/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ class CompactLinkedHashMap<K, V>
/*     */   extends CompactHashMap<K, V>
/*     */ {
/*     */   private static final int ENDPOINT = -2;
/*     */   @CheckForNull
/*     */   @VisibleForTesting
/*     */   transient long[] links;
/*     */   private transient int firstEntry;
/*     */   private transient int lastEntry;
/*     */   private final boolean accessOrder;
/*     */   
/*     */   public static <K, V> CompactLinkedHashMap<K, V> create() {
/*  66 */     return new CompactLinkedHashMap<>();
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
/*     */   public static <K, V> CompactLinkedHashMap<K, V> createWithExpectedSize(int expectedSize) {
/*  80 */     return new CompactLinkedHashMap<>(expectedSize);
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
/*     */   CompactLinkedHashMap() {
/* 105 */     this(3);
/*     */   }
/*     */   
/*     */   CompactLinkedHashMap(int expectedSize) {
/* 109 */     this(expectedSize, false);
/*     */   }
/*     */   
/*     */   CompactLinkedHashMap(int expectedSize, boolean accessOrder) {
/* 113 */     super(expectedSize);
/* 114 */     this.accessOrder = accessOrder;
/*     */   }
/*     */ 
/*     */   
/*     */   void init(int expectedSize) {
/* 119 */     super.init(expectedSize);
/* 120 */     this.firstEntry = -2;
/* 121 */     this.lastEntry = -2;
/*     */   }
/*     */ 
/*     */   
/*     */   int allocArrays() {
/* 126 */     int expectedSize = super.allocArrays();
/* 127 */     this.links = new long[expectedSize];
/* 128 */     return expectedSize;
/*     */   }
/*     */ 
/*     */   
/*     */   Map<K, V> createHashFloodingResistantDelegate(int tableSize) {
/* 133 */     return new LinkedHashMap<>(tableSize, 1.0F, this.accessOrder);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   Map<K, V> convertToHashFloodingResistantImplementation() {
/* 139 */     Map<K, V> result = super.convertToHashFloodingResistantImplementation();
/* 140 */     this.links = null;
/* 141 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getPredecessor(int entry) {
/* 151 */     return (int)(link(entry) >>> 32L) - 1;
/*     */   }
/*     */ 
/*     */   
/*     */   int getSuccessor(int entry) {
/* 156 */     return (int)link(entry) - 1;
/*     */   }
/*     */   
/*     */   private void setSuccessor(int entry, int succ) {
/* 160 */     long succMask = 4294967295L;
/* 161 */     setLink(entry, link(entry) & (succMask ^ 0xFFFFFFFFFFFFFFFFL) | (succ + 1) & succMask);
/*     */   }
/*     */   
/*     */   private void setPredecessor(int entry, int pred) {
/* 165 */     long predMask = -4294967296L;
/* 166 */     setLink(entry, link(entry) & (predMask ^ 0xFFFFFFFFFFFFFFFFL) | (pred + 1) << 32L);
/*     */   }
/*     */   
/*     */   private void setSucceeds(int pred, int succ) {
/* 170 */     if (pred == -2) {
/* 171 */       this.firstEntry = succ;
/*     */     } else {
/* 173 */       setSuccessor(pred, succ);
/*     */     } 
/*     */     
/* 176 */     if (succ == -2) {
/* 177 */       this.lastEntry = pred;
/*     */     } else {
/* 179 */       setPredecessor(succ, pred);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void insertEntry(int entryIndex, @ParametricNullness K key, @ParametricNullness V value, int hash, int mask) {
/* 186 */     super.insertEntry(entryIndex, key, value, hash, mask);
/* 187 */     setSucceeds(this.lastEntry, entryIndex);
/* 188 */     setSucceeds(entryIndex, -2);
/*     */   }
/*     */ 
/*     */   
/*     */   void accessEntry(int index) {
/* 193 */     if (this.accessOrder) {
/*     */       
/* 195 */       setSucceeds(getPredecessor(index), getSuccessor(index));
/*     */       
/* 197 */       setSucceeds(this.lastEntry, index);
/* 198 */       setSucceeds(index, -2);
/* 199 */       incrementModCount();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void moveLastEntry(int dstIndex, int mask) {
/* 205 */     int srcIndex = size() - 1;
/* 206 */     super.moveLastEntry(dstIndex, mask);
/*     */     
/* 208 */     setSucceeds(getPredecessor(dstIndex), getSuccessor(dstIndex));
/* 209 */     if (dstIndex < srcIndex) {
/* 210 */       setSucceeds(getPredecessor(srcIndex), dstIndex);
/* 211 */       setSucceeds(dstIndex, getSuccessor(srcIndex));
/*     */     } 
/* 213 */     setLink(srcIndex, 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   void resizeEntries(int newCapacity) {
/* 218 */     super.resizeEntries(newCapacity);
/* 219 */     this.links = Arrays.copyOf(requireLinks(), newCapacity);
/*     */   }
/*     */ 
/*     */   
/*     */   int firstEntryIndex() {
/* 224 */     return this.firstEntry;
/*     */   }
/*     */ 
/*     */   
/*     */   int adjustAfterRemove(int indexBeforeRemove, int indexRemoved) {
/* 229 */     return (indexBeforeRemove >= size()) ? indexRemoved : indexBeforeRemove;
/*     */   }
/*     */   
/*     */   Set<Map.Entry<K, V>> createEntrySet() {
/*     */     class EntrySetImpl extends CompactHashMap<K, V>.EntrySetView {
/*     */       EntrySetImpl(CompactLinkedHashMap this$0) {
/* 235 */         super(this$0);
/*     */       }
/*     */       public Spliterator<Map.Entry<K, V>> spliterator() {
/* 238 */         return Spliterators.spliterator(this, 17);
/*     */       }
/*     */     };
/* 241 */     return new EntrySetImpl(this);
/*     */   }
/*     */   
/*     */   Set<K> createKeySet() {
/*     */     class KeySetImpl extends CompactHashMap<K, V>.KeySetView {
/*     */       KeySetImpl(CompactLinkedHashMap this$0) {
/* 247 */         super(this$0);
/*     */       }
/*     */       public Object[] toArray() {
/* 250 */         return ObjectArrays.toArrayImpl(this);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public <T> T[] toArray(T[] a) {
/* 256 */         return ObjectArrays.toArrayImpl(this, a);
/*     */       }
/*     */ 
/*     */       
/*     */       public Spliterator<K> spliterator() {
/* 261 */         return Spliterators.spliterator(this, 17);
/*     */       }
/*     */     };
/* 264 */     return new KeySetImpl(this);
/*     */   }
/*     */   
/*     */   Collection<V> createValues() {
/*     */     class ValuesImpl extends CompactHashMap<K, V>.ValuesView {
/*     */       ValuesImpl(CompactLinkedHashMap this$0) {
/* 270 */         super(this$0);
/*     */       }
/*     */       public Object[] toArray() {
/* 273 */         return ObjectArrays.toArrayImpl(this);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public <T> T[] toArray(T[] a) {
/* 279 */         return ObjectArrays.toArrayImpl(this, a);
/*     */       }
/*     */ 
/*     */       
/*     */       public Spliterator<V> spliterator() {
/* 284 */         return Spliterators.spliterator(this, 16);
/*     */       }
/*     */     };
/* 287 */     return new ValuesImpl(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 292 */     if (needsAllocArrays()) {
/*     */       return;
/*     */     }
/* 295 */     this.firstEntry = -2;
/* 296 */     this.lastEntry = -2;
/* 297 */     if (this.links != null) {
/* 298 */       Arrays.fill(this.links, 0, size(), 0L);
/*     */     }
/* 300 */     super.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long[] requireLinks() {
/* 309 */     return Objects.<long[]>requireNonNull(this.links);
/*     */   }
/*     */   
/*     */   private long link(int i) {
/* 313 */     return requireLinks()[i];
/*     */   }
/*     */   
/*     */   private void setLink(int i, long value) {
/* 317 */     requireLinks()[i] = value;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/CompactLinkedHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */