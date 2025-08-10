/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.time.Duration;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Stopwatch
/*     */ {
/*     */   private final Ticker ticker;
/*     */   private boolean isRunning;
/*     */   private long elapsedNanos;
/*     */   private long startTick;
/*     */   
/*     */   public static Stopwatch createUnstarted() {
/* 112 */     return new Stopwatch();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stopwatch createUnstarted(Ticker ticker) {
/* 121 */     return new Stopwatch(ticker);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stopwatch createStarted() {
/* 130 */     return (new Stopwatch()).start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stopwatch createStarted(Ticker ticker) {
/* 139 */     return (new Stopwatch(ticker)).start();
/*     */   }
/*     */   
/*     */   Stopwatch() {
/* 143 */     this.ticker = Ticker.systemTicker();
/*     */   }
/*     */   
/*     */   Stopwatch(Ticker ticker) {
/* 147 */     this.ticker = Preconditions.<Ticker>checkNotNull(ticker, "ticker");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRunning() {
/* 155 */     return this.isRunning;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Stopwatch start() {
/* 166 */     Preconditions.checkState(!this.isRunning, "This stopwatch is already running.");
/* 167 */     this.isRunning = true;
/* 168 */     this.startTick = this.ticker.read();
/* 169 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Stopwatch stop() {
/* 181 */     long tick = this.ticker.read();
/* 182 */     Preconditions.checkState(this.isRunning, "This stopwatch is already stopped.");
/* 183 */     this.isRunning = false;
/* 184 */     this.elapsedNanos += tick - this.startTick;
/* 185 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Stopwatch reset() {
/* 195 */     this.elapsedNanos = 0L;
/* 196 */     this.isRunning = false;
/* 197 */     return this;
/*     */   }
/*     */   
/*     */   private long elapsedNanos() {
/* 201 */     return this.isRunning ? (this.ticker.read() - this.startTick + this.elapsedNanos) : this.elapsedNanos;
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
/*     */   public long elapsed(TimeUnit desiredUnit) {
/* 218 */     return desiredUnit.convert(elapsedNanos(), TimeUnit.NANOSECONDS);
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
/*     */   public Duration elapsed() {
/* 231 */     return Duration.ofNanos(elapsedNanos());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 237 */     long nanos = elapsedNanos();
/*     */     
/* 239 */     TimeUnit unit = chooseUnit(nanos);
/* 240 */     double value = nanos / TimeUnit.NANOSECONDS.convert(1L, unit);
/*     */ 
/*     */     
/* 243 */     return Platform.formatCompact4Digits(value) + " " + abbreviate(unit);
/*     */   }
/*     */   
/*     */   private static TimeUnit chooseUnit(long nanos) {
/* 247 */     if (TimeUnit.DAYS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
/* 248 */       return TimeUnit.DAYS;
/*     */     }
/* 250 */     if (TimeUnit.HOURS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
/* 251 */       return TimeUnit.HOURS;
/*     */     }
/* 253 */     if (TimeUnit.MINUTES.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
/* 254 */       return TimeUnit.MINUTES;
/*     */     }
/* 256 */     if (TimeUnit.SECONDS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
/* 257 */       return TimeUnit.SECONDS;
/*     */     }
/* 259 */     if (TimeUnit.MILLISECONDS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
/* 260 */       return TimeUnit.MILLISECONDS;
/*     */     }
/* 262 */     if (TimeUnit.MICROSECONDS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
/* 263 */       return TimeUnit.MICROSECONDS;
/*     */     }
/* 265 */     return TimeUnit.NANOSECONDS;
/*     */   }
/*     */   
/*     */   private static String abbreviate(TimeUnit unit) {
/* 269 */     switch (unit) {
/*     */       case NANOSECONDS:
/* 271 */         return "ns";
/*     */       case MICROSECONDS:
/* 273 */         return "μs";
/*     */       case MILLISECONDS:
/* 275 */         return "ms";
/*     */       case SECONDS:
/* 277 */         return "s";
/*     */       case MINUTES:
/* 279 */         return "min";
/*     */       case HOURS:
/* 281 */         return "h";
/*     */       case DAYS:
/* 283 */         return "d";
/*     */     } 
/* 285 */     throw new AssertionError();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/base/Stopwatch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */