/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtIncompatible
/*     */ class CompactLinkedHashSet<E>
/*     */   extends CompactHashSet<E>
/*     */ {
/*     */   private static final int ENDPOINT = -2;
/*     */   @CheckForNull
/*     */   private transient int[] predecessor;
/*     */   @CheckForNull
/*     */   private transient int[] successor;
/*     */   private transient int firstEntry;
/*     */   private transient int lastEntry;
/*     */   
/*     */   public static <E> CompactLinkedHashSet<E> create() {
/*  59 */     return new CompactLinkedHashSet<>();
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
/*     */   public static <E> CompactLinkedHashSet<E> create(Collection<? extends E> collection) {
/*  71 */     CompactLinkedHashSet<E> set = createWithExpectedSize(collection.size());
/*  72 */     set.addAll(collection);
/*  73 */     return set;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SafeVarargs
/*     */   public static <E> CompactLinkedHashSet<E> create(E... elements) {
/*  85 */     CompactLinkedHashSet<E> set = createWithExpectedSize(elements.length);
/*  86 */     Collections.addAll(set, elements);
/*  87 */     return set;
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
/*     */   public static <E> CompactLinkedHashSet<E> createWithExpectedSize(int expectedSize) {
/* 101 */     return new CompactLinkedHashSet<>(expectedSize);
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
/*     */   CompactLinkedHashSet() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   CompactLinkedHashSet(int expectedSize) {
/* 133 */     super(expectedSize);
/*     */   }
/*     */ 
/*     */   
/*     */   void init(int expectedSize) {
/* 138 */     super.init(expectedSize);
/* 139 */     this.firstEntry = -2;
/* 140 */     this.lastEntry = -2;
/*     */   }
/*     */ 
/*     */   
/*     */   int allocArrays() {
/* 145 */     int expectedSize = super.allocArrays();
/* 146 */     this.predecessor = new int[expectedSize];
/* 147 */     this.successor = new int[expectedSize];
/* 148 */     return expectedSize;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   Set<E> convertToHashFloodingResistantImplementation() {
/* 154 */     Set<E> result = super.convertToHashFloodingResistantImplementation();
/* 155 */     this.predecessor = null;
/* 156 */     this.successor = null;
/* 157 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getPredecessor(int entry) {
/* 167 */     return requirePredecessors()[entry] - 1;
/*     */   }
/*     */ 
/*     */   
/*     */   int getSuccessor(int entry) {
/* 172 */     return requireSuccessors()[entry] - 1;
/*     */   }
/*     */   
/*     */   private void setSuccessor(int entry, int succ) {
/* 176 */     requireSuccessors()[entry] = succ + 1;
/*     */   }
/*     */   
/*     */   private void setPredecessor(int entry, int pred) {
/* 180 */     requirePredecessors()[entry] = pred + 1;
/*     */   }
/*     */   
/*     */   private void setSucceeds(int pred, int succ) {
/* 184 */     if (pred == -2) {
/* 185 */       this.firstEntry = succ;
/*     */     } else {
/* 187 */       setSuccessor(pred, succ);
/*     */     } 
/*     */     
/* 190 */     if (succ == -2) {
/* 191 */       this.lastEntry = pred;
/*     */     } else {
/* 193 */       setPredecessor(succ, pred);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void insertEntry(int entryIndex, @ParametricNullness E object, int hash, int mask) {
/* 199 */     super.insertEntry(entryIndex, object, hash, mask);
/* 200 */     setSucceeds(this.lastEntry, entryIndex);
/* 201 */     setSucceeds(entryIndex, -2);
/*     */   }
/*     */ 
/*     */   
/*     */   void moveLastEntry(int dstIndex, int mask) {
/* 206 */     int srcIndex = size() - 1;
/* 207 */     super.moveLastEntry(dstIndex, mask);
/*     */     
/* 209 */     setSucceeds(getPredecessor(dstIndex), getSuccessor(dstIndex));
/* 210 */     if (dstIndex < srcIndex) {
/* 211 */       setSucceeds(getPredecessor(srcIndex), dstIndex);
/* 212 */       setSucceeds(dstIndex, getSuccessor(srcIndex));
/*     */     } 
/* 214 */     requirePredecessors()[srcIndex] = 0;
/* 215 */     requireSuccessors()[srcIndex] = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   void resizeEntries(int newCapacity) {
/* 220 */     super.resizeEntries(newCapacity);
/* 221 */     this.predecessor = Arrays.copyOf(requirePredecessors(), newCapacity);
/* 222 */     this.successor = Arrays.copyOf(requireSuccessors(), newCapacity);
/*     */   }
/*     */ 
/*     */   
/*     */   int firstEntryIndex() {
/* 227 */     return this.firstEntry;
/*     */   }
/*     */ 
/*     */   
/*     */   int adjustAfterRemove(int indexBeforeRemove, int indexRemoved) {
/* 232 */     return (indexBeforeRemove >= size()) ? indexRemoved : indexBeforeRemove;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 237 */     return ObjectArrays.toArrayImpl(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T[] toArray(T[] a) {
/* 243 */     return ObjectArrays.toArrayImpl(this, a);
/*     */   }
/*     */ 
/*     */   
/*     */   public Spliterator<E> spliterator() {
/* 248 */     return Spliterators.spliterator(this, 17);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 253 */     if (needsAllocArrays()) {
/*     */       return;
/*     */     }
/* 256 */     this.firstEntry = -2;
/* 257 */     this.lastEntry = -2;
/*     */     
/* 259 */     if (this.predecessor != null && this.successor != null) {
/* 260 */       Arrays.fill(this.predecessor, 0, size(), 0);
/* 261 */       Arrays.fill(this.successor, 0, size(), 0);
/*     */     } 
/* 263 */     super.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int[] requirePredecessors() {
/* 272 */     return Objects.<int[]>requireNonNull(this.predecessor);
/*     */   }
/*     */   
/*     */   private int[] requireSuccessors() {
/* 276 */     return Objects.<int[]>requireNonNull(this.successor);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/CompactLinkedHashSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */