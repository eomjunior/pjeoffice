/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Stopwatch;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.time.Duration;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @Beta
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ public abstract class RateLimiter
/*     */ {
/*     */   private final SleepingStopwatch stopwatch;
/*     */   @CheckForNull
/*     */   private volatile Object mutexDoNotUseDirectly;
/*     */   
/*     */   public static RateLimiter create(double permitsPerSecond) {
/* 132 */     return create(permitsPerSecond, SleepingStopwatch.createFromSystemTimer());
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static RateLimiter create(double permitsPerSecond, SleepingStopwatch stopwatch) {
/* 137 */     RateLimiter rateLimiter = new SmoothRateLimiter.SmoothBursty(stopwatch, 1.0D);
/* 138 */     rateLimiter.setRate(permitsPerSecond);
/* 139 */     return rateLimiter;
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
/*     */   public static RateLimiter create(double permitsPerSecond, Duration warmupPeriod) {
/* 167 */     return create(permitsPerSecond, Internal.toNanosSaturated(warmupPeriod), TimeUnit.NANOSECONDS);
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
/*     */   public static RateLimiter create(double permitsPerSecond, long warmupPeriod, TimeUnit unit) {
/* 196 */     Preconditions.checkArgument((warmupPeriod >= 0L), "warmupPeriod must not be negative: %s", warmupPeriod);
/* 197 */     return create(permitsPerSecond, warmupPeriod, unit, 3.0D, 
/* 198 */         SleepingStopwatch.createFromSystemTimer());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static RateLimiter create(double permitsPerSecond, long warmupPeriod, TimeUnit unit, double coldFactor, SleepingStopwatch stopwatch) {
/* 208 */     RateLimiter rateLimiter = new SmoothRateLimiter.SmoothWarmingUp(stopwatch, warmupPeriod, unit, coldFactor);
/* 209 */     rateLimiter.setRate(permitsPerSecond);
/* 210 */     return rateLimiter;
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
/*     */   private Object mutex() {
/* 223 */     Object mutex = this.mutexDoNotUseDirectly;
/* 224 */     if (mutex == null) {
/* 225 */       synchronized (this) {
/* 226 */         mutex = this.mutexDoNotUseDirectly;
/* 227 */         if (mutex == null) {
/* 228 */           this.mutexDoNotUseDirectly = mutex = new Object();
/*     */         }
/*     */       } 
/*     */     }
/* 232 */     return mutex;
/*     */   }
/*     */   
/*     */   RateLimiter(SleepingStopwatch stopwatch) {
/* 236 */     this.stopwatch = (SleepingStopwatch)Preconditions.checkNotNull(stopwatch);
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
/*     */   public final void setRate(double permitsPerSecond) {
/* 258 */     Preconditions.checkArgument((permitsPerSecond > 0.0D && 
/* 259 */         !Double.isNaN(permitsPerSecond)), "rate must be positive");
/* 260 */     synchronized (mutex()) {
/* 261 */       doSetRate(permitsPerSecond, this.stopwatch.readMicros());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract void doSetRate(double paramDouble, long paramLong);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double getRate() {
/* 274 */     synchronized (mutex()) {
/* 275 */       return doGetRate();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract double doGetRate();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public double acquire() {
/* 292 */     return acquire(1);
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
/*     */   @CanIgnoreReturnValue
/*     */   public double acquire(int permits) {
/* 306 */     long microsToWait = reserve(permits);
/* 307 */     this.stopwatch.sleepMicrosUninterruptibly(microsToWait);
/* 308 */     return 1.0D * microsToWait / TimeUnit.SECONDS.toMicros(1L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final long reserve(int permits) {
/* 318 */     checkPermits(permits);
/* 319 */     synchronized (mutex()) {
/* 320 */       return reserveAndGetWaitLength(permits, this.stopwatch.readMicros());
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
/*     */   public boolean tryAcquire(Duration timeout) {
/* 337 */     return tryAcquire(1, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
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
/*     */   public boolean tryAcquire(long timeout, TimeUnit unit) {
/* 354 */     return tryAcquire(1, timeout, unit);
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
/*     */   public boolean tryAcquire(int permits) {
/* 368 */     return tryAcquire(permits, 0L, TimeUnit.MICROSECONDS);
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
/*     */   public boolean tryAcquire() {
/* 381 */     return tryAcquire(1, 0L, TimeUnit.MICROSECONDS);
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
/*     */   public boolean tryAcquire(int permits, Duration timeout) {
/* 396 */     return tryAcquire(permits, Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
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
/*     */   public boolean tryAcquire(int permits, long timeout, TimeUnit unit) {
/* 412 */     long microsToWait, timeoutMicros = Math.max(unit.toMicros(timeout), 0L);
/* 413 */     checkPermits(permits);
/*     */     
/* 415 */     synchronized (mutex()) {
/* 416 */       long nowMicros = this.stopwatch.readMicros();
/* 417 */       if (!canAcquire(nowMicros, timeoutMicros)) {
/* 418 */         return false;
/*     */       }
/* 420 */       microsToWait = reserveAndGetWaitLength(permits, nowMicros);
/*     */     } 
/*     */     
/* 423 */     this.stopwatch.sleepMicrosUninterruptibly(microsToWait);
/* 424 */     return true;
/*     */   }
/*     */   
/*     */   private boolean canAcquire(long nowMicros, long timeoutMicros) {
/* 428 */     return (queryEarliestAvailable(nowMicros) - timeoutMicros <= nowMicros);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final long reserveAndGetWaitLength(int permits, long nowMicros) {
/* 437 */     long momentAvailable = reserveEarliestAvailable(permits, nowMicros);
/* 438 */     return Math.max(momentAvailable - nowMicros, 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract long queryEarliestAvailable(long paramLong);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract long reserveEarliestAvailable(int paramInt, long paramLong);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 460 */     return String.format(Locale.ROOT, "RateLimiter[stableRate=%3.1fqps]", new Object[] { Double.valueOf(getRate()) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static abstract class SleepingStopwatch
/*     */   {
/*     */     protected abstract long readMicros();
/*     */ 
/*     */ 
/*     */     
/*     */     protected abstract void sleepMicrosUninterruptibly(long param1Long);
/*     */ 
/*     */ 
/*     */     
/*     */     public static SleepingStopwatch createFromSystemTimer() {
/* 477 */       return new SleepingStopwatch() {
/* 478 */           final Stopwatch stopwatch = Stopwatch.createStarted();
/*     */ 
/*     */           
/*     */           protected long readMicros() {
/* 482 */             return this.stopwatch.elapsed(TimeUnit.MICROSECONDS);
/*     */           }
/*     */ 
/*     */           
/*     */           protected void sleepMicrosUninterruptibly(long micros) {
/* 487 */             if (micros > 0L) {
/* 488 */               Uninterruptibles.sleepUninterruptibly(micros, TimeUnit.MICROSECONDS);
/*     */             }
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */   
/*     */   private static void checkPermits(int permits) {
/* 496 */     Preconditions.checkArgument((permits > 0), "Requested permits (%s) must be positive", permits);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/RateLimiter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */