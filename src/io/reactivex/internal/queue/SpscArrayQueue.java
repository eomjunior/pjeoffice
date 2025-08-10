/*     */ package io.reactivex.internal.queue;
/*     */ 
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.internal.fuseable.SimplePlainQueue;
/*     */ import io.reactivex.internal.util.Pow2;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.concurrent.atomic.AtomicReferenceArray;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SpscArrayQueue<E>
/*     */   extends AtomicReferenceArray<E>
/*     */   implements SimplePlainQueue<E>
/*     */ {
/*     */   private static final long serialVersionUID = -1296597691183856449L;
/*  43 */   private static final Integer MAX_LOOK_AHEAD_STEP = Integer.getInteger("jctools.spsc.max.lookahead.step", 4096);
/*     */   final int mask;
/*     */   final AtomicLong producerIndex;
/*     */   long producerLookAhead;
/*     */   final AtomicLong consumerIndex;
/*     */   final int lookAheadStep;
/*     */   
/*     */   public SpscArrayQueue(int capacity) {
/*  51 */     super(Pow2.roundToPowerOfTwo(capacity));
/*  52 */     this.mask = length() - 1;
/*  53 */     this.producerIndex = new AtomicLong();
/*  54 */     this.consumerIndex = new AtomicLong();
/*  55 */     this.lookAheadStep = Math.min(capacity / 4, MAX_LOOK_AHEAD_STEP.intValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean offer(E e) {
/*  60 */     if (null == e) {
/*  61 */       throw new NullPointerException("Null is not a valid element");
/*     */     }
/*     */     
/*  64 */     int mask = this.mask;
/*  65 */     long index = this.producerIndex.get();
/*  66 */     int offset = calcElementOffset(index, mask);
/*  67 */     if (index >= this.producerLookAhead) {
/*  68 */       int step = this.lookAheadStep;
/*  69 */       if (null == lvElement(calcElementOffset(index + step, mask))) {
/*  70 */         this.producerLookAhead = index + step;
/*  71 */       } else if (null != lvElement(offset)) {
/*  72 */         return false;
/*     */       } 
/*     */     } 
/*  75 */     soElement(offset, e);
/*  76 */     soProducerIndex(index + 1L);
/*  77 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean offer(E v1, E v2) {
/*  83 */     return (offer(v1) && offer(v2));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public E poll() {
/*  89 */     long index = this.consumerIndex.get();
/*  90 */     int offset = calcElementOffset(index);
/*     */     
/*  92 */     E e = lvElement(offset);
/*  93 */     if (null == e) {
/*  94 */       return null;
/*     */     }
/*  96 */     soConsumerIndex(index + 1L);
/*  97 */     soElement(offset, (E)null);
/*  98 */     return e;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 103 */     return (this.producerIndex.get() == this.consumerIndex.get());
/*     */   }
/*     */   
/*     */   void soProducerIndex(long newIndex) {
/* 107 */     this.producerIndex.lazySet(newIndex);
/*     */   }
/*     */   
/*     */   void soConsumerIndex(long newIndex) {
/* 111 */     this.consumerIndex.lazySet(newIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 117 */     while (poll() != null || !isEmpty());
/*     */   }
/*     */   
/*     */   int calcElementOffset(long index, int mask) {
/* 121 */     return (int)index & mask;
/*     */   }
/*     */   
/*     */   int calcElementOffset(long index) {
/* 125 */     return (int)index & this.mask;
/*     */   }
/*     */   
/*     */   void soElement(int offset, E value) {
/* 129 */     lazySet(offset, value);
/*     */   }
/*     */   
/*     */   E lvElement(int offset) {
/* 133 */     return get(offset);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/queue/SpscArrayQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */