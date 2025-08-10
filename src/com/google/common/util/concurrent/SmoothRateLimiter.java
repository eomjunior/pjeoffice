/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.math.LongMath;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ abstract class SmoothRateLimiter
/*     */   extends RateLimiter
/*     */ {
/*     */   double storedPermits;
/*     */   double maxPermits;
/*     */   double stableIntervalMicros;
/*     */   
/*     */   static final class SmoothWarmingUp
/*     */     extends SmoothRateLimiter
/*     */   {
/*     */     private final long warmupPeriodMicros;
/*     */     private double slope;
/*     */     private double thresholdPermits;
/*     */     private double coldFactor;
/*     */     
/*     */     SmoothWarmingUp(RateLimiter.SleepingStopwatch stopwatch, long warmupPeriod, TimeUnit timeUnit, double coldFactor) {
/* 220 */       super(stopwatch);
/* 221 */       this.warmupPeriodMicros = timeUnit.toMicros(warmupPeriod);
/* 222 */       this.coldFactor = coldFactor;
/*     */     }
/*     */ 
/*     */     
/*     */     void doSetRate(double permitsPerSecond, double stableIntervalMicros) {
/* 227 */       double oldMaxPermits = this.maxPermits;
/* 228 */       double coldIntervalMicros = stableIntervalMicros * this.coldFactor;
/* 229 */       this.thresholdPermits = 0.5D * this.warmupPeriodMicros / stableIntervalMicros;
/* 230 */       this.maxPermits = this.thresholdPermits + 2.0D * this.warmupPeriodMicros / (stableIntervalMicros + coldIntervalMicros);
/*     */       
/* 232 */       this.slope = (coldIntervalMicros - stableIntervalMicros) / (this.maxPermits - this.thresholdPermits);
/* 233 */       if (oldMaxPermits == Double.POSITIVE_INFINITY) {
/*     */         
/* 235 */         this.storedPermits = 0.0D;
/*     */       } else {
/* 237 */         this
/*     */ 
/*     */           
/* 240 */           .storedPermits = (oldMaxPermits == 0.0D) ? this.maxPermits : (this.storedPermits * this.maxPermits / oldMaxPermits);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     long storedPermitsToWaitTime(double storedPermits, double permitsToTake) {
/* 246 */       double availablePermitsAboveThreshold = storedPermits - this.thresholdPermits;
/* 247 */       long micros = 0L;
/*     */       
/* 249 */       if (availablePermitsAboveThreshold > 0.0D) {
/* 250 */         double permitsAboveThresholdToTake = Math.min(availablePermitsAboveThreshold, permitsToTake);
/*     */ 
/*     */ 
/*     */         
/* 254 */         double length = permitsToTime(availablePermitsAboveThreshold) + permitsToTime(availablePermitsAboveThreshold - permitsAboveThresholdToTake);
/* 255 */         micros = (long)(permitsAboveThresholdToTake * length / 2.0D);
/* 256 */         permitsToTake -= permitsAboveThresholdToTake;
/*     */       } 
/*     */       
/* 259 */       micros += (long)(this.stableIntervalMicros * permitsToTake);
/* 260 */       return micros;
/*     */     }
/*     */     
/*     */     private double permitsToTime(double permits) {
/* 264 */       return this.stableIntervalMicros + permits * this.slope;
/*     */     }
/*     */ 
/*     */     
/*     */     double coolDownIntervalMicros() {
/* 269 */       return this.warmupPeriodMicros / this.maxPermits;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class SmoothBursty
/*     */     extends SmoothRateLimiter
/*     */   {
/*     */     final double maxBurstSeconds;
/*     */ 
/*     */ 
/*     */     
/*     */     SmoothBursty(RateLimiter.SleepingStopwatch stopwatch, double maxBurstSeconds) {
/* 284 */       super(stopwatch);
/* 285 */       this.maxBurstSeconds = maxBurstSeconds;
/*     */     }
/*     */ 
/*     */     
/*     */     void doSetRate(double permitsPerSecond, double stableIntervalMicros) {
/* 290 */       double oldMaxPermits = this.maxPermits;
/* 291 */       this.maxPermits = this.maxBurstSeconds * permitsPerSecond;
/* 292 */       if (oldMaxPermits == Double.POSITIVE_INFINITY) {
/*     */         
/* 294 */         this.storedPermits = this.maxPermits;
/*     */       } else {
/* 296 */         this
/*     */ 
/*     */           
/* 299 */           .storedPermits = (oldMaxPermits == 0.0D) ? 0.0D : (this.storedPermits * this.maxPermits / oldMaxPermits);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     long storedPermitsToWaitTime(double storedPermits, double permitsToTake) {
/* 305 */       return 0L;
/*     */     }
/*     */ 
/*     */     
/*     */     double coolDownIntervalMicros() {
/* 310 */       return this.stableIntervalMicros;
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
/* 330 */   private long nextFreeTicketMicros = 0L;
/*     */   
/*     */   private SmoothRateLimiter(RateLimiter.SleepingStopwatch stopwatch) {
/* 333 */     super(stopwatch);
/*     */   }
/*     */ 
/*     */   
/*     */   final void doSetRate(double permitsPerSecond, long nowMicros) {
/* 338 */     resync(nowMicros);
/* 339 */     double stableIntervalMicros = TimeUnit.SECONDS.toMicros(1L) / permitsPerSecond;
/* 340 */     this.stableIntervalMicros = stableIntervalMicros;
/* 341 */     doSetRate(permitsPerSecond, stableIntervalMicros);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final double doGetRate() {
/* 348 */     return TimeUnit.SECONDS.toMicros(1L) / this.stableIntervalMicros;
/*     */   }
/*     */ 
/*     */   
/*     */   final long queryEarliestAvailable(long nowMicros) {
/* 353 */     return this.nextFreeTicketMicros;
/*     */   }
/*     */ 
/*     */   
/*     */   final long reserveEarliestAvailable(int requiredPermits, long nowMicros) {
/* 358 */     resync(nowMicros);
/* 359 */     long returnValue = this.nextFreeTicketMicros;
/* 360 */     double storedPermitsToSpend = Math.min(requiredPermits, this.storedPermits);
/* 361 */     double freshPermits = requiredPermits - storedPermitsToSpend;
/*     */     
/* 363 */     long waitMicros = storedPermitsToWaitTime(this.storedPermits, storedPermitsToSpend) + (long)(freshPermits * this.stableIntervalMicros);
/*     */ 
/*     */     
/* 366 */     this.nextFreeTicketMicros = LongMath.saturatedAdd(this.nextFreeTicketMicros, waitMicros);
/* 367 */     this.storedPermits -= storedPermitsToSpend;
/* 368 */     return returnValue;
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
/*     */   void resync(long nowMicros) {
/* 388 */     if (nowMicros > this.nextFreeTicketMicros) {
/* 389 */       double newPermits = (nowMicros - this.nextFreeTicketMicros) / coolDownIntervalMicros();
/* 390 */       this.storedPermits = Math.min(this.maxPermits, this.storedPermits + newPermits);
/* 391 */       this.nextFreeTicketMicros = nowMicros;
/*     */     } 
/*     */   }
/*     */   
/*     */   abstract void doSetRate(double paramDouble1, double paramDouble2);
/*     */   
/*     */   abstract long storedPermitsToWaitTime(double paramDouble1, double paramDouble2);
/*     */   
/*     */   abstract double coolDownIntervalMicros();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/SmoothRateLimiter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */