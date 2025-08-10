/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.time.Duration;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Collection;
/*     */ import java.util.Deque;
/*     */ import java.util.PriorityQueue;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ArrayBlockingQueue;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.LinkedBlockingDeque;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.PriorityBlockingQueue;
/*     */ import java.util.concurrent.SynchronousQueue;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Queues
/*     */ {
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static <E> ArrayBlockingQueue<E> newArrayBlockingQueue(int capacity) {
/*  58 */     return new ArrayBlockingQueue<>(capacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ArrayDeque<E> newArrayDeque() {
/*  69 */     return new ArrayDeque<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ArrayDeque<E> newArrayDeque(Iterable<? extends E> elements) {
/*  79 */     if (elements instanceof Collection) {
/*  80 */       return new ArrayDeque<>((Collection<? extends E>)elements);
/*     */     }
/*  82 */     ArrayDeque<E> deque = new ArrayDeque<>();
/*  83 */     Iterables.addAll(deque, elements);
/*  84 */     return deque;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static <E> ConcurrentLinkedQueue<E> newConcurrentLinkedQueue() {
/*  93 */     return new ConcurrentLinkedQueue<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static <E> ConcurrentLinkedQueue<E> newConcurrentLinkedQueue(Iterable<? extends E> elements) {
/* 104 */     if (elements instanceof Collection) {
/* 105 */       return new ConcurrentLinkedQueue<>((Collection<? extends E>)elements);
/*     */     }
/* 107 */     ConcurrentLinkedQueue<E> queue = new ConcurrentLinkedQueue<>();
/* 108 */     Iterables.addAll(queue, elements);
/* 109 */     return queue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque() {
/* 122 */     return new LinkedBlockingDeque<>();
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
/*     */   public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque(int capacity) {
/* 134 */     return new LinkedBlockingDeque<>(capacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque(Iterable<? extends E> elements) {
/* 147 */     if (elements instanceof Collection) {
/* 148 */       return new LinkedBlockingDeque<>((Collection<? extends E>)elements);
/*     */     }
/* 150 */     LinkedBlockingDeque<E> deque = new LinkedBlockingDeque<>();
/* 151 */     Iterables.addAll(deque, elements);
/* 152 */     return deque;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue() {
/* 161 */     return new LinkedBlockingQueue<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(int capacity) {
/* 172 */     return new LinkedBlockingQueue<>(capacity);
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
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(Iterable<? extends E> elements) {
/* 186 */     if (elements instanceof Collection) {
/* 187 */       return new LinkedBlockingQueue<>((Collection<? extends E>)elements);
/*     */     }
/* 189 */     LinkedBlockingQueue<E> queue = new LinkedBlockingQueue<>();
/* 190 */     Iterables.addAll(queue, elements);
/* 191 */     return queue;
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
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static <E extends Comparable> PriorityBlockingQueue<E> newPriorityBlockingQueue() {
/* 208 */     return new PriorityBlockingQueue<>();
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
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static <E extends Comparable> PriorityBlockingQueue<E> newPriorityBlockingQueue(Iterable<? extends E> elements) {
/* 224 */     if (elements instanceof Collection) {
/* 225 */       return new PriorityBlockingQueue<>((Collection<? extends E>)elements);
/*     */     }
/* 227 */     PriorityBlockingQueue<E> queue = new PriorityBlockingQueue<>();
/* 228 */     Iterables.addAll(queue, elements);
/* 229 */     return queue;
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
/*     */   public static <E extends Comparable> PriorityQueue<E> newPriorityQueue() {
/* 242 */     return new PriorityQueue<>();
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
/*     */   public static <E extends Comparable> PriorityQueue<E> newPriorityQueue(Iterable<? extends E> elements) {
/* 256 */     if (elements instanceof Collection) {
/* 257 */       return new PriorityQueue<>((Collection<? extends E>)elements);
/*     */     }
/* 259 */     PriorityQueue<E> queue = new PriorityQueue<>();
/* 260 */     Iterables.addAll(queue, elements);
/* 261 */     return queue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static <E> SynchronousQueue<E> newSynchronousQueue() {
/* 270 */     return new SynchronousQueue<>();
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
/*     */   @CanIgnoreReturnValue
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static <E> int drain(BlockingQueue<E> q, Collection<? super E> buffer, int numElements, Duration timeout) throws InterruptedException {
/* 292 */     return drain(q, buffer, numElements, timeout.toNanos(), TimeUnit.NANOSECONDS);
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
/*     */   @CanIgnoreReturnValue
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static <E> int drain(BlockingQueue<E> q, Collection<? super E> buffer, int numElements, long timeout, TimeUnit unit) throws InterruptedException {
/* 318 */     Preconditions.checkNotNull(buffer);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 324 */     long deadline = System.nanoTime() + unit.toNanos(timeout);
/* 325 */     int added = 0;
/* 326 */     while (added < numElements) {
/*     */ 
/*     */       
/* 329 */       added += q.drainTo(buffer, numElements - added);
/* 330 */       if (added < numElements) {
/* 331 */         E e = q.poll(deadline - System.nanoTime(), TimeUnit.NANOSECONDS);
/* 332 */         if (e == null) {
/*     */           break;
/*     */         }
/* 335 */         buffer.add(e);
/* 336 */         added++;
/*     */       } 
/*     */     } 
/* 339 */     return added;
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
/*     */   @CanIgnoreReturnValue
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static <E> int drainUninterruptibly(BlockingQueue<E> q, Collection<? super E> buffer, int numElements, Duration timeout) {
/* 364 */     return drainUninterruptibly(q, buffer, numElements, timeout.toNanos(), TimeUnit.NANOSECONDS);
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
/*     */   @CanIgnoreReturnValue
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static <E> int drainUninterruptibly(BlockingQueue<E> q, Collection<? super E> buffer, int numElements, long timeout, TimeUnit unit) {
/* 390 */     Preconditions.checkNotNull(buffer);
/* 391 */     long deadline = System.nanoTime() + unit.toNanos(timeout);
/* 392 */     int added = 0;
/* 393 */     boolean interrupted = false;
/*     */     try {
/* 395 */       while (added < numElements) {
/*     */ 
/*     */         
/* 398 */         added += q.drainTo(buffer, numElements - added);
/* 399 */         if (added < numElements) {
/*     */           E e;
/*     */           while (true) {
/*     */             try {
/* 403 */               e = q.poll(deadline - System.nanoTime(), TimeUnit.NANOSECONDS);
/*     */               break;
/* 405 */             } catch (InterruptedException ex) {
/* 406 */               interrupted = true;
/*     */             } 
/*     */           } 
/* 409 */           if (e == null) {
/*     */             break;
/*     */           }
/* 412 */           buffer.add(e);
/* 413 */           added++;
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 417 */       if (interrupted) {
/* 418 */         Thread.currentThread().interrupt();
/*     */       }
/*     */     } 
/* 421 */     return added;
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
/*     */   public static <E> Queue<E> synchronizedQueue(Queue<E> queue) {
/* 454 */     return Synchronized.queue(queue, null);
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
/*     */   public static <E> Deque<E> synchronizedDeque(Deque<E> deque) {
/* 487 */     return Synchronized.deque(deque, null);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/Queues.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */