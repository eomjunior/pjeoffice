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
/*     */ public final class SpscLinkedArrayQueue<T>
/*     */   implements SimplePlainQueue<T>
/*     */ {
/*  33 */   static final int MAX_LOOK_AHEAD_STEP = Integer.getInteger("jctools.spsc.max.lookahead.step", 4096).intValue();
/*  34 */   final AtomicLong producerIndex = new AtomicLong();
/*     */   
/*     */   int producerLookAheadStep;
/*     */   
/*     */   long producerLookAhead;
/*     */   
/*     */   final int producerMask;
/*     */   AtomicReferenceArray<Object> producerBuffer;
/*     */   final int consumerMask;
/*     */   AtomicReferenceArray<Object> consumerBuffer;
/*  44 */   final AtomicLong consumerIndex = new AtomicLong();
/*     */   
/*  46 */   private static final Object HAS_NEXT = new Object();
/*     */   
/*     */   public SpscLinkedArrayQueue(int bufferSize) {
/*  49 */     int p2capacity = Pow2.roundToPowerOfTwo(Math.max(8, bufferSize));
/*  50 */     int mask = p2capacity - 1;
/*  51 */     AtomicReferenceArray<Object> buffer = new AtomicReferenceArray(p2capacity + 1);
/*  52 */     this.producerBuffer = buffer;
/*  53 */     this.producerMask = mask;
/*  54 */     adjustLookAheadStep(p2capacity);
/*  55 */     this.consumerBuffer = buffer;
/*  56 */     this.consumerMask = mask;
/*  57 */     this.producerLookAhead = (mask - 1);
/*  58 */     soProducerIndex(0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean offer(T e) {
/*  68 */     if (null == e) {
/*  69 */       throw new NullPointerException("Null is not a valid element");
/*     */     }
/*     */     
/*  72 */     AtomicReferenceArray<Object> buffer = this.producerBuffer;
/*  73 */     long index = lpProducerIndex();
/*  74 */     int mask = this.producerMask;
/*  75 */     int offset = calcWrappedOffset(index, mask);
/*  76 */     if (index < this.producerLookAhead) {
/*  77 */       return writeToQueue(buffer, e, index, offset);
/*     */     }
/*  79 */     int lookAheadStep = this.producerLookAheadStep;
/*     */     
/*  81 */     int lookAheadElementOffset = calcWrappedOffset(index + lookAheadStep, mask);
/*  82 */     if (null == lvElement(buffer, lookAheadElementOffset)) {
/*  83 */       this.producerLookAhead = index + lookAheadStep - 1L;
/*  84 */       return writeToQueue(buffer, e, index, offset);
/*  85 */     }  if (null == lvElement(buffer, calcWrappedOffset(index + 1L, mask))) {
/*  86 */       return writeToQueue(buffer, e, index, offset);
/*     */     }
/*  88 */     resize(buffer, index, offset, e, mask);
/*  89 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean writeToQueue(AtomicReferenceArray<Object> buffer, T e, long index, int offset) {
/*  95 */     soElement(buffer, offset, e);
/*  96 */     soProducerIndex(index + 1L);
/*  97 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private void resize(AtomicReferenceArray<Object> oldBuffer, long currIndex, int offset, T e, long mask) {
/* 102 */     int capacity = oldBuffer.length();
/* 103 */     AtomicReferenceArray<Object> newBuffer = new AtomicReferenceArray(capacity);
/* 104 */     this.producerBuffer = newBuffer;
/* 105 */     this.producerLookAhead = currIndex + mask - 1L;
/* 106 */     soElement(newBuffer, offset, e);
/* 107 */     soNext(oldBuffer, newBuffer);
/* 108 */     soElement(oldBuffer, offset, HAS_NEXT);
/*     */     
/* 110 */     soProducerIndex(currIndex + 1L);
/*     */   }
/*     */   
/*     */   private void soNext(AtomicReferenceArray<Object> curr, AtomicReferenceArray<Object> next) {
/* 114 */     soElement(curr, calcDirectOffset(curr.length() - 1), next);
/*     */   }
/*     */ 
/*     */   
/*     */   private AtomicReferenceArray<Object> lvNextBufferAndUnlink(AtomicReferenceArray<Object> curr, int nextIndex) {
/* 119 */     int nextOffset = calcDirectOffset(nextIndex);
/* 120 */     AtomicReferenceArray<Object> nextBuffer = (AtomicReferenceArray<Object>)lvElement(curr, nextOffset);
/* 121 */     soElement(curr, nextOffset, null);
/* 122 */     return nextBuffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public T poll() {
/* 134 */     AtomicReferenceArray<Object> buffer = this.consumerBuffer;
/* 135 */     long index = lpConsumerIndex();
/* 136 */     int mask = this.consumerMask;
/* 137 */     int offset = calcWrappedOffset(index, mask);
/* 138 */     Object e = lvElement(buffer, offset);
/* 139 */     boolean isNextBuffer = (e == HAS_NEXT);
/* 140 */     if (null != e && !isNextBuffer) {
/* 141 */       soElement(buffer, offset, null);
/* 142 */       soConsumerIndex(index + 1L);
/* 143 */       return (T)e;
/* 144 */     }  if (isNextBuffer) {
/* 145 */       return newBufferPoll(lvNextBufferAndUnlink(buffer, mask + 1), index, mask);
/*     */     }
/*     */     
/* 148 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private T newBufferPoll(AtomicReferenceArray<Object> nextBuffer, long index, int mask) {
/* 153 */     this.consumerBuffer = nextBuffer;
/* 154 */     int offsetInNew = calcWrappedOffset(index, mask);
/* 155 */     T n = (T)lvElement(nextBuffer, offsetInNew);
/* 156 */     if (null != n) {
/* 157 */       soElement(nextBuffer, offsetInNew, null);
/* 158 */       soConsumerIndex(index + 1L);
/*     */     } 
/* 160 */     return n;
/*     */   }
/*     */ 
/*     */   
/*     */   public T peek() {
/* 165 */     AtomicReferenceArray<Object> buffer = this.consumerBuffer;
/* 166 */     long index = lpConsumerIndex();
/* 167 */     int mask = this.consumerMask;
/* 168 */     int offset = calcWrappedOffset(index, mask);
/* 169 */     Object e = lvElement(buffer, offset);
/* 170 */     if (e == HAS_NEXT) {
/* 171 */       return newBufferPeek(lvNextBufferAndUnlink(buffer, mask + 1), index, mask);
/*     */     }
/*     */     
/* 174 */     return (T)e;
/*     */   }
/*     */ 
/*     */   
/*     */   private T newBufferPeek(AtomicReferenceArray<Object> nextBuffer, long index, int mask) {
/* 179 */     this.consumerBuffer = nextBuffer;
/* 180 */     int offsetInNew = calcWrappedOffset(index, mask);
/* 181 */     return (T)lvElement(nextBuffer, offsetInNew);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 186 */     while (poll() != null || !isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 196 */     long after = lvConsumerIndex();
/*     */     while (true) {
/* 198 */       long before = after;
/* 199 */       long currentProducerIndex = lvProducerIndex();
/* 200 */       after = lvConsumerIndex();
/* 201 */       if (before == after) {
/* 202 */         return (int)(currentProducerIndex - after);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 209 */     return (lvProducerIndex() == lvConsumerIndex());
/*     */   }
/*     */   
/*     */   private void adjustLookAheadStep(int capacity) {
/* 213 */     this.producerLookAheadStep = Math.min(capacity / 4, MAX_LOOK_AHEAD_STEP);
/*     */   }
/*     */   
/*     */   private long lvProducerIndex() {
/* 217 */     return this.producerIndex.get();
/*     */   }
/*     */   
/*     */   private long lvConsumerIndex() {
/* 221 */     return this.consumerIndex.get();
/*     */   }
/*     */   
/*     */   private long lpProducerIndex() {
/* 225 */     return this.producerIndex.get();
/*     */   }
/*     */   
/*     */   private long lpConsumerIndex() {
/* 229 */     return this.consumerIndex.get();
/*     */   }
/*     */   
/*     */   private void soProducerIndex(long v) {
/* 233 */     this.producerIndex.lazySet(v);
/*     */   }
/*     */   
/*     */   private void soConsumerIndex(long v) {
/* 237 */     this.consumerIndex.lazySet(v);
/*     */   }
/*     */   
/*     */   private static int calcWrappedOffset(long index, int mask) {
/* 241 */     return calcDirectOffset((int)index & mask);
/*     */   }
/*     */   private static int calcDirectOffset(int index) {
/* 244 */     return index;
/*     */   }
/*     */   private static void soElement(AtomicReferenceArray<Object> buffer, int offset, Object e) {
/* 247 */     buffer.lazySet(offset, e);
/*     */   }
/*     */   
/*     */   private static <E> Object lvElement(AtomicReferenceArray<Object> buffer, int offset) {
/* 251 */     return buffer.get(offset);
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
/*     */   public boolean offer(T first, T second) {
/* 263 */     AtomicReferenceArray<Object> buffer = this.producerBuffer;
/* 264 */     long p = lvProducerIndex();
/* 265 */     int m = this.producerMask;
/*     */     
/* 267 */     int pi = calcWrappedOffset(p + 2L, m);
/*     */     
/* 269 */     if (null == lvElement(buffer, pi)) {
/* 270 */       pi = calcWrappedOffset(p, m);
/* 271 */       soElement(buffer, pi + 1, second);
/* 272 */       soElement(buffer, pi, first);
/* 273 */       soProducerIndex(p + 2L);
/*     */     } else {
/* 275 */       int capacity = buffer.length();
/* 276 */       AtomicReferenceArray<Object> newBuffer = new AtomicReferenceArray(capacity);
/* 277 */       this.producerBuffer = newBuffer;
/*     */       
/* 279 */       pi = calcWrappedOffset(p, m);
/* 280 */       soElement(newBuffer, pi + 1, second);
/* 281 */       soElement(newBuffer, pi, first);
/* 282 */       soNext(buffer, newBuffer);
/*     */       
/* 284 */       soElement(buffer, pi, HAS_NEXT);
/*     */       
/* 286 */       soProducerIndex(p + 2L);
/*     */     } 
/*     */     
/* 289 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/queue/SpscLinkedArrayQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */