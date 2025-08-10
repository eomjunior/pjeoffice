/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.MapMaker;
/*     */ import com.google.common.math.IntMath;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.Semaphore;
/*     */ import java.util.concurrent.atomic.AtomicReferenceArray;
/*     */ import java.util.concurrent.locks.Condition;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class Striped<L>
/*     */ {
/*     */   private static final int LARGE_LAZY_CUTOFF = 1024;
/*     */   private static final int ALL_SET = -1;
/*     */   
/*     */   private Striped() {}
/*     */   
/*     */   public Iterable<L> bulkGet(Iterable<? extends Object> keys) {
/* 144 */     List<Object> result = Lists.newArrayList(keys);
/* 145 */     if (result.isEmpty()) {
/* 146 */       return (Iterable<L>)ImmutableList.of();
/*     */     }
/* 148 */     int[] stripes = new int[result.size()];
/* 149 */     for (int i = 0; i < result.size(); i++) {
/* 150 */       stripes[i] = indexFor(result.get(i));
/*     */     }
/* 152 */     Arrays.sort(stripes);
/*     */     
/* 154 */     int previousStripe = stripes[0];
/* 155 */     result.set(0, getAt(previousStripe));
/* 156 */     for (int j = 1; j < result.size(); j++) {
/* 157 */       int currentStripe = stripes[j];
/* 158 */       if (currentStripe == previousStripe) {
/* 159 */         result.set(j, result.get(j - 1));
/*     */       } else {
/* 161 */         result.set(j, getAt(currentStripe));
/* 162 */         previousStripe = currentStripe;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 183 */     List<Object> list1 = result;
/* 184 */     return Collections.unmodifiableList((List)list1);
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
/*     */   static <L> Striped<L> custom(int stripes, Supplier<L> supplier) {
/* 198 */     return new CompactStriped<>(stripes, supplier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Striped<Lock> lock(int stripes) {
/* 209 */     return custom(stripes, PaddedLock::new);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Striped<Lock> lazyWeakLock(int stripes) {
/* 220 */     return lazy(stripes, () -> new ReentrantLock(false));
/*     */   }
/*     */   
/*     */   private static <L> Striped<L> lazy(int stripes, Supplier<L> supplier) {
/* 224 */     return (stripes < 1024) ? 
/* 225 */       new SmallLazyStriped<>(stripes, supplier) : 
/* 226 */       new LargeLazyStriped<>(stripes, supplier);
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
/*     */   public static Striped<Semaphore> semaphore(int stripes, int permits) {
/* 238 */     return custom(stripes, () -> new PaddedSemaphore(permits));
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
/*     */   public static Striped<Semaphore> lazyWeakSemaphore(int stripes, int permits) {
/* 250 */     return lazy(stripes, () -> new Semaphore(permits, false));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Striped<ReadWriteLock> readWriteLock(int stripes) {
/* 261 */     return custom(stripes, ReentrantReadWriteLock::new);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Striped<ReadWriteLock> lazyWeakReadWriteLock(int stripes) {
/* 272 */     return lazy(stripes, WeakSafeReadWriteLock::new);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class WeakSafeReadWriteLock
/*     */     implements ReadWriteLock
/*     */   {
/* 283 */     private final ReadWriteLock delegate = new ReentrantReadWriteLock();
/*     */ 
/*     */ 
/*     */     
/*     */     public Lock readLock() {
/* 288 */       return new Striped.WeakSafeLock(this.delegate.readLock(), this);
/*     */     }
/*     */ 
/*     */     
/*     */     public Lock writeLock() {
/* 293 */       return new Striped.WeakSafeLock(this.delegate.writeLock(), this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class WeakSafeLock
/*     */     extends ForwardingLock
/*     */   {
/*     */     private final Lock delegate;
/*     */     private final Striped.WeakSafeReadWriteLock strongReference;
/*     */     
/*     */     WeakSafeLock(Lock delegate, Striped.WeakSafeReadWriteLock strongReference) {
/* 305 */       this.delegate = delegate;
/* 306 */       this.strongReference = strongReference;
/*     */     }
/*     */ 
/*     */     
/*     */     Lock delegate() {
/* 311 */       return this.delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public Condition newCondition() {
/* 316 */       return new Striped.WeakSafeCondition(this.delegate.newCondition(), this.strongReference);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class WeakSafeCondition
/*     */     extends ForwardingCondition
/*     */   {
/*     */     private final Condition delegate;
/*     */     private final Striped.WeakSafeReadWriteLock strongReference;
/*     */     
/*     */     WeakSafeCondition(Condition delegate, Striped.WeakSafeReadWriteLock strongReference) {
/* 328 */       this.delegate = delegate;
/* 329 */       this.strongReference = strongReference;
/*     */     }
/*     */ 
/*     */     
/*     */     Condition delegate() {
/* 334 */       return this.delegate;
/*     */     }
/*     */   }
/*     */   
/*     */   private static abstract class PowerOfTwoStriped<L>
/*     */     extends Striped<L> {
/*     */     final int mask;
/*     */     
/*     */     PowerOfTwoStriped(int stripes) {
/* 343 */       Preconditions.checkArgument((stripes > 0), "Stripes must be positive");
/* 344 */       this.mask = (stripes > 1073741824) ? -1 : (Striped.ceilToPowerOfTwo(stripes) - 1);
/*     */     }
/*     */ 
/*     */     
/*     */     final int indexFor(Object key) {
/* 349 */       int hash = Striped.smear(key.hashCode());
/* 350 */       return hash & this.mask;
/*     */     }
/*     */ 
/*     */     
/*     */     public final L get(Object key) {
/* 355 */       return getAt(indexFor(key));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class CompactStriped<L>
/*     */     extends PowerOfTwoStriped<L>
/*     */   {
/*     */     private final Object[] array;
/*     */ 
/*     */     
/*     */     private CompactStriped(int stripes, Supplier<L> supplier) {
/* 368 */       super(stripes);
/* 369 */       Preconditions.checkArgument((stripes <= 1073741824), "Stripes must be <= 2^30)");
/*     */       
/* 371 */       this.array = new Object[this.mask + 1];
/* 372 */       for (int i = 0; i < this.array.length; i++) {
/* 373 */         this.array[i] = supplier.get();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public L getAt(int index) {
/* 380 */       return (L)this.array[index];
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 385 */       return this.array.length;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static class SmallLazyStriped<L>
/*     */     extends PowerOfTwoStriped<L>
/*     */   {
/*     */     final AtomicReferenceArray<ArrayReference<? extends L>> locks;
/*     */     
/*     */     final Supplier<L> supplier;
/*     */     
/*     */     final int size;
/* 399 */     final ReferenceQueue<L> queue = new ReferenceQueue<>();
/*     */     
/*     */     SmallLazyStriped(int stripes, Supplier<L> supplier) {
/* 402 */       super(stripes);
/* 403 */       this.size = (this.mask == -1) ? Integer.MAX_VALUE : (this.mask + 1);
/* 404 */       this.locks = new AtomicReferenceArray<>(this.size);
/* 405 */       this.supplier = supplier;
/*     */     }
/*     */ 
/*     */     
/*     */     public L getAt(int index) {
/* 410 */       if (this.size != Integer.MAX_VALUE) {
/* 411 */         Preconditions.checkElementIndex(index, size());
/*     */       }
/* 413 */       ArrayReference<? extends L> existingRef = this.locks.get(index);
/* 414 */       L existing = (existingRef == null) ? null : existingRef.get();
/* 415 */       if (existing != null) {
/* 416 */         return existing;
/*     */       }
/* 418 */       L created = (L)this.supplier.get();
/* 419 */       ArrayReference<L> newRef = new ArrayReference<>(created, index, this.queue);
/* 420 */       while (!this.locks.compareAndSet(index, existingRef, newRef)) {
/*     */         
/* 422 */         existingRef = this.locks.get(index);
/* 423 */         existing = (existingRef == null) ? null : existingRef.get();
/* 424 */         if (existing != null) {
/* 425 */           return existing;
/*     */         }
/*     */       } 
/* 428 */       drainQueue();
/* 429 */       return created;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void drainQueue() {
/*     */       Reference<? extends L> ref;
/* 437 */       while ((ref = this.queue.poll()) != null) {
/*     */         
/* 439 */         ArrayReference<? extends L> arrayRef = (ArrayReference<? extends L>)ref;
/*     */ 
/*     */         
/* 442 */         this.locks.compareAndSet(arrayRef.index, arrayRef, null);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 448 */       return this.size;
/*     */     }
/*     */     
/*     */     private static final class ArrayReference<L> extends WeakReference<L> {
/*     */       final int index;
/*     */       
/*     */       ArrayReference(L referent, int index, ReferenceQueue<L> queue) {
/* 455 */         super(referent, queue);
/* 456 */         this.index = index;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static class LargeLazyStriped<L>
/*     */     extends PowerOfTwoStriped<L>
/*     */   {
/*     */     final ConcurrentMap<Integer, L> locks;
/*     */     
/*     */     final Supplier<L> supplier;
/*     */     
/*     */     final int size;
/*     */     
/*     */     LargeLazyStriped(int stripes, Supplier<L> supplier) {
/* 473 */       super(stripes);
/* 474 */       this.size = (this.mask == -1) ? Integer.MAX_VALUE : (this.mask + 1);
/* 475 */       this.supplier = supplier;
/* 476 */       this.locks = (new MapMaker()).weakValues().makeMap();
/*     */     }
/*     */ 
/*     */     
/*     */     public L getAt(int index) {
/* 481 */       if (this.size != Integer.MAX_VALUE) {
/* 482 */         Preconditions.checkElementIndex(index, size());
/*     */       }
/* 484 */       L existing = this.locks.get(Integer.valueOf(index));
/* 485 */       if (existing != null) {
/* 486 */         return existing;
/*     */       }
/* 488 */       L created = (L)this.supplier.get();
/* 489 */       existing = this.locks.putIfAbsent(Integer.valueOf(index), created);
/* 490 */       return (L)MoreObjects.firstNonNull(existing, created);
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 495 */       return this.size;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int ceilToPowerOfTwo(int x) {
/* 503 */     return 1 << IntMath.log2(x, RoundingMode.CEILING);
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
/*     */   private static int smear(int hashCode) {
/* 516 */     hashCode ^= hashCode >>> 20 ^ hashCode >>> 12;
/* 517 */     return hashCode ^ hashCode >>> 7 ^ hashCode >>> 4;
/*     */   }
/*     */   
/*     */   public abstract L get(Object paramObject);
/*     */   
/*     */   public abstract L getAt(int paramInt);
/*     */   
/*     */   abstract int indexFor(Object paramObject);
/*     */   
/*     */   public abstract int size();
/*     */   
/*     */   private static class PaddedLock extends ReentrantLock { long unused1;
/*     */     
/*     */     PaddedLock() {
/* 531 */       super(false);
/*     */     }
/*     */     
/*     */     long unused2;
/*     */     long unused3; }
/*     */   
/*     */   private static class PaddedSemaphore extends Semaphore { long unused1;
/*     */     long unused2;
/*     */     long unused3;
/*     */     
/*     */     PaddedSemaphore(int permits) {
/* 542 */       super(permits, false);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/Striped.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */