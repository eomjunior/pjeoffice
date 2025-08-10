/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Verify;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.time.Duration;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.Semaphore;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.locks.Condition;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Uninterruptibles
/*     */ {
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static void awaitUninterruptibly(CountDownLatch latch) {
/*  59 */     boolean interrupted = false;
/*     */     
/*     */     while (true) {
/*     */       try {
/*  63 */         latch.await();
/*     */         return;
/*  65 */       } catch (InterruptedException e) {
/*     */ 
/*     */       
/*     */       } finally {
/*     */         
/*  70 */         if (interrupted) {
/*  71 */           Thread.currentThread().interrupt();
/*     */         }
/*     */       } 
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
/*     */   public static boolean awaitUninterruptibly(CountDownLatch latch, Duration timeout) {
/*  85 */     return awaitUninterruptibly(latch, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static boolean awaitUninterruptibly(CountDownLatch latch, long timeout, TimeUnit unit) {
/*  96 */     boolean interrupted = false;
/*     */     try {
/*  98 */       long remainingNanos = unit.toNanos(timeout);
/*  99 */       long end = System.nanoTime() + remainingNanos;
/*     */ 
/*     */       
/*     */       while (true) {
/*     */         try {
/* 104 */           return latch.await(remainingNanos, TimeUnit.NANOSECONDS);
/* 105 */         } catch (InterruptedException e) {
/* 106 */           interrupted = true;
/* 107 */           remainingNanos = end - System.nanoTime();
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 111 */       if (interrupted) {
/* 112 */         Thread.currentThread().interrupt();
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
/*     */   public static boolean awaitUninterruptibly(Condition condition, Duration timeout) {
/* 126 */     return awaitUninterruptibly(condition, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
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
/*     */   public static boolean awaitUninterruptibly(Condition condition, long timeout, TimeUnit unit) {
/* 139 */     boolean interrupted = false;
/*     */     try {
/* 141 */       long remainingNanos = unit.toNanos(timeout);
/* 142 */       long end = System.nanoTime() + remainingNanos;
/*     */       
/*     */       while (true) {
/*     */         try {
/* 146 */           return condition.await(remainingNanos, TimeUnit.NANOSECONDS);
/* 147 */         } catch (InterruptedException e) {
/* 148 */           interrupted = true;
/* 149 */           remainingNanos = end - System.nanoTime();
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 153 */       if (interrupted) {
/* 154 */         Thread.currentThread().interrupt();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static void joinUninterruptibly(Thread toJoin) {
/* 163 */     boolean interrupted = false;
/*     */     
/*     */     while (true) {
/*     */       try {
/* 167 */         toJoin.join();
/*     */         return;
/* 169 */       } catch (InterruptedException e) {
/*     */ 
/*     */       
/*     */       } finally {
/*     */         
/* 174 */         if (interrupted) {
/* 175 */           Thread.currentThread().interrupt();
/*     */         }
/*     */       } 
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
/*     */   public static void joinUninterruptibly(Thread toJoin, Duration timeout) {
/* 189 */     joinUninterruptibly(toJoin, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static void joinUninterruptibly(Thread toJoin, long timeout, TimeUnit unit) {
/* 200 */     Preconditions.checkNotNull(toJoin);
/* 201 */     boolean interrupted = false;
/*     */     try {
/* 203 */       long remainingNanos = unit.toNanos(timeout);
/* 204 */       long end = System.nanoTime() + remainingNanos;
/*     */       
/*     */       while (true) {
/*     */         try {
/* 208 */           TimeUnit.NANOSECONDS.timedJoin(toJoin, remainingNanos);
/*     */           return;
/* 210 */         } catch (InterruptedException e) {
/* 211 */           interrupted = true;
/* 212 */           remainingNanos = end - System.nanoTime();
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 216 */       if (interrupted) {
/* 217 */         Thread.currentThread().interrupt();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   @CanIgnoreReturnValue
/*     */   public static <V> V getUninterruptibly(Future<V> future) throws ExecutionException {
/* 243 */     boolean interrupted = false;
/*     */     
/*     */     while (true) {
/*     */       try {
/* 247 */         return future.get();
/* 248 */       } catch (InterruptedException e) {
/*     */ 
/*     */       
/*     */       } finally {
/*     */         
/* 253 */         if (interrupted) {
/* 254 */           Thread.currentThread().interrupt();
/*     */         }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   @CanIgnoreReturnValue
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static <V> V getUninterruptibly(Future<V> future, Duration timeout) throws ExecutionException, TimeoutException {
/* 284 */     return getUninterruptibly(future, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
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
/*     */   @ParametricNullness
/*     */   @CanIgnoreReturnValue
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static <V> V getUninterruptibly(Future<V> future, long timeout, TimeUnit unit) throws ExecutionException, TimeoutException {
/* 312 */     boolean interrupted = false;
/*     */     try {
/* 314 */       long remainingNanos = unit.toNanos(timeout);
/* 315 */       long end = System.nanoTime() + remainingNanos;
/*     */ 
/*     */       
/*     */       while (true) {
/*     */         try {
/* 320 */           return future.get(remainingNanos, TimeUnit.NANOSECONDS);
/* 321 */         } catch (InterruptedException e) {
/* 322 */           interrupted = true;
/* 323 */           remainingNanos = end - System.nanoTime();
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 327 */       if (interrupted) {
/* 328 */         Thread.currentThread().interrupt();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static <E> E takeUninterruptibly(BlockingQueue<E> queue) {
/* 337 */     boolean interrupted = false;
/*     */     
/*     */     while (true) {
/*     */       try {
/* 341 */         return queue.take();
/* 342 */       } catch (InterruptedException e) {
/*     */ 
/*     */       
/*     */       } finally {
/*     */         
/* 347 */         if (interrupted) {
/* 348 */           Thread.currentThread().interrupt();
/*     */         }
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
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static <E> void putUninterruptibly(BlockingQueue<E> queue, E element) {
/* 364 */     boolean interrupted = false;
/*     */     
/*     */     while (true) {
/*     */       try {
/* 368 */         queue.put(element);
/*     */         return;
/* 370 */       } catch (InterruptedException e) {
/*     */ 
/*     */       
/*     */       } finally {
/*     */         
/* 375 */         if (interrupted) {
/* 376 */           Thread.currentThread().interrupt();
/*     */         }
/*     */       } 
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
/*     */   public static void sleepUninterruptibly(Duration sleepFor) {
/* 390 */     sleepUninterruptibly(Internal.toNanosSaturated(sleepFor), TimeUnit.NANOSECONDS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static void sleepUninterruptibly(long sleepFor, TimeUnit unit) {
/* 399 */     boolean interrupted = false;
/*     */     try {
/* 401 */       long remainingNanos = unit.toNanos(sleepFor);
/* 402 */       long end = System.nanoTime() + remainingNanos;
/*     */       
/*     */       while (true) {
/*     */         try {
/* 406 */           TimeUnit.NANOSECONDS.sleep(remainingNanos);
/*     */           return;
/* 408 */         } catch (InterruptedException e) {
/* 409 */           interrupted = true;
/* 410 */           remainingNanos = end - System.nanoTime();
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 414 */       if (interrupted) {
/* 415 */         Thread.currentThread().interrupt();
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
/*     */   public static boolean tryAcquireUninterruptibly(Semaphore semaphore, Duration timeout) {
/* 429 */     return tryAcquireUninterruptibly(semaphore, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
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
/*     */   public static boolean tryAcquireUninterruptibly(Semaphore semaphore, long timeout, TimeUnit unit) {
/* 443 */     return tryAcquireUninterruptibly(semaphore, 1, timeout, unit);
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
/*     */   public static boolean tryAcquireUninterruptibly(Semaphore semaphore, int permits, Duration timeout) {
/* 456 */     return tryAcquireUninterruptibly(semaphore, permits, 
/* 457 */         Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
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
/*     */   public static boolean tryAcquireUninterruptibly(Semaphore semaphore, int permits, long timeout, TimeUnit unit) {
/* 471 */     boolean interrupted = false;
/*     */     try {
/* 473 */       long remainingNanos = unit.toNanos(timeout);
/* 474 */       long end = System.nanoTime() + remainingNanos;
/*     */ 
/*     */       
/*     */       while (true) {
/*     */         try {
/* 479 */           return semaphore.tryAcquire(permits, remainingNanos, TimeUnit.NANOSECONDS);
/* 480 */         } catch (InterruptedException e) {
/* 481 */           interrupted = true;
/* 482 */           remainingNanos = end - System.nanoTime();
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 486 */       if (interrupted) {
/* 487 */         Thread.currentThread().interrupt();
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
/*     */   public static boolean tryLockUninterruptibly(Lock lock, Duration timeout) {
/* 501 */     return tryLockUninterruptibly(lock, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
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
/*     */   public static boolean tryLockUninterruptibly(Lock lock, long timeout, TimeUnit unit) {
/* 514 */     boolean interrupted = false;
/*     */     try {
/* 516 */       long remainingNanos = unit.toNanos(timeout);
/* 517 */       long end = System.nanoTime() + remainingNanos;
/*     */       
/*     */       while (true) {
/*     */         try {
/* 521 */           return lock.tryLock(remainingNanos, TimeUnit.NANOSECONDS);
/* 522 */         } catch (InterruptedException e) {
/* 523 */           interrupted = true;
/* 524 */           remainingNanos = end - System.nanoTime();
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 528 */       if (interrupted) {
/* 529 */         Thread.currentThread().interrupt();
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
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static void awaitTerminationUninterruptibly(ExecutorService executor) {
/* 544 */     Verify.verify(awaitTerminationUninterruptibly(executor, Long.MAX_VALUE, TimeUnit.NANOSECONDS));
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
/*     */   public static boolean awaitTerminationUninterruptibly(ExecutorService executor, Duration timeout) {
/* 557 */     return awaitTerminationUninterruptibly(executor, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
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
/*     */   public static boolean awaitTerminationUninterruptibly(ExecutorService executor, long timeout, TimeUnit unit) {
/* 571 */     boolean interrupted = false;
/*     */     try {
/* 573 */       long remainingNanos = unit.toNanos(timeout);
/* 574 */       long end = System.nanoTime() + remainingNanos;
/*     */       
/*     */       while (true) {
/*     */         try {
/* 578 */           return executor.awaitTermination(remainingNanos, TimeUnit.NANOSECONDS);
/* 579 */         } catch (InterruptedException e) {
/* 580 */           interrupted = true;
/* 581 */           remainingNanos = end - System.nanoTime();
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 585 */       if (interrupted)
/* 586 */         Thread.currentThread().interrupt(); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/Uninterruptibles.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */