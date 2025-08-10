/*     */ package org.apache.hc.core5.util;
/*     */ 
/*     */ import java.text.ParseException;
/*     */ import java.time.Duration;
/*     */ import java.time.temporal.ChronoUnit;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*     */ public class TimeValue
/*     */   implements Comparable<TimeValue>
/*     */ {
/*     */   static final int INT_UNDEFINED = -1;
/*  53 */   public static final TimeValue MAX_VALUE = ofDays(Long.MAX_VALUE);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  58 */   public static final TimeValue NEG_ONE_MILLISECOND = of(-1L, TimeUnit.MILLISECONDS);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   public static final TimeValue NEG_ONE_SECOND = of(-1L, TimeUnit.SECONDS);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   public static final TimeValue ZERO_MILLISECONDS = of(0L, TimeUnit.MILLISECONDS);
/*     */ 
/*     */ 
/*     */   
/*     */   private final long duration;
/*     */ 
/*     */ 
/*     */   
/*     */   private final TimeUnit timeUnit;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int asBoundInt(long value) {
/*  82 */     if (value > 2147483647L)
/*  83 */       return Integer.MAX_VALUE; 
/*  84 */     if (value < -2147483648L) {
/*  85 */       return Integer.MIN_VALUE;
/*     */     }
/*  87 */     return (int)value;
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
/*     */   public static <T extends TimeValue> T defaultsTo(T timeValue, T defaultValue) {
/*  99 */     return (timeValue != null) ? timeValue : defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TimeValue defaultsToNegativeOneMillisecond(TimeValue timeValue) {
/* 110 */     return defaultsTo(timeValue, NEG_ONE_MILLISECOND);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TimeValue defaultsToNegativeOneSecond(TimeValue timeValue) {
/* 121 */     return defaultsTo(timeValue, NEG_ONE_SECOND);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TimeValue defaultsToZeroMilliseconds(TimeValue timeValue) {
/* 132 */     return defaultsTo(timeValue, ZERO_MILLISECONDS);
/*     */   }
/*     */   
/*     */   public static boolean isNonNegative(TimeValue timeValue) {
/* 136 */     return (timeValue != null && timeValue.getDuration() >= 0L);
/*     */   }
/*     */   
/*     */   public static boolean isPositive(TimeValue timeValue) {
/* 140 */     return (timeValue != null && timeValue.getDuration() > 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TimeValue of(long duration, TimeUnit timeUnit) {
/* 151 */     return new TimeValue(duration, timeUnit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TimeValue of(Duration duration) {
/* 162 */     long seconds = duration.getSeconds();
/* 163 */     long nanoOfSecond = duration.getNano();
/* 164 */     if (seconds == 0L)
/*     */     {
/* 166 */       return of(nanoOfSecond, TimeUnit.NANOSECONDS); } 
/* 167 */     if (nanoOfSecond == 0L)
/*     */     {
/* 169 */       return of(seconds, TimeUnit.SECONDS);
/*     */     }
/*     */     
/*     */     try {
/* 173 */       return of(duration.toNanos(), TimeUnit.NANOSECONDS);
/* 174 */     } catch (ArithmeticException e) {
/*     */       try {
/* 176 */         return of(duration.toMillis(), TimeUnit.MILLISECONDS);
/* 177 */       } catch (ArithmeticException e1) {
/*     */         
/* 179 */         return of(seconds, TimeUnit.SECONDS);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static TimeValue ofDays(long days) {
/* 185 */     return of(days, TimeUnit.DAYS);
/*     */   }
/*     */   
/*     */   public static TimeValue ofHours(long hours) {
/* 189 */     return of(hours, TimeUnit.HOURS);
/*     */   }
/*     */   
/*     */   public static TimeValue ofMicroseconds(long microseconds) {
/* 193 */     return of(microseconds, TimeUnit.MICROSECONDS);
/*     */   }
/*     */   
/*     */   public static TimeValue ofMilliseconds(long millis) {
/* 197 */     return of(millis, TimeUnit.MILLISECONDS);
/*     */   }
/*     */   
/*     */   public static TimeValue ofMinutes(long minutes) {
/* 201 */     return of(minutes, TimeUnit.MINUTES);
/*     */   }
/*     */   
/*     */   public static TimeValue ofNanoseconds(long nanoseconds) {
/* 205 */     return of(nanoseconds, TimeUnit.NANOSECONDS);
/*     */   }
/*     */   
/*     */   public static TimeValue ofSeconds(long seconds) {
/* 209 */     return of(seconds, TimeUnit.SECONDS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static ChronoUnit toChronoUnit(TimeUnit timeUnit) {
/* 218 */     switch ((TimeUnit)Objects.requireNonNull((T)timeUnit)) {
/*     */       case NANOSECONDS:
/* 220 */         return ChronoUnit.NANOS;
/*     */       case MICROSECONDS:
/* 222 */         return ChronoUnit.MICROS;
/*     */       case MILLISECONDS:
/* 224 */         return ChronoUnit.MILLIS;
/*     */       case SECONDS:
/* 226 */         return ChronoUnit.SECONDS;
/*     */       case MINUTES:
/* 228 */         return ChronoUnit.MINUTES;
/*     */       case HOURS:
/* 230 */         return ChronoUnit.HOURS;
/*     */       case DAYS:
/* 232 */         return ChronoUnit.DAYS;
/*     */     } 
/* 234 */     throw new IllegalArgumentException(timeUnit.toString());
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
/*     */   public static TimeValue parse(String value) throws ParseException {
/* 256 */     String[] split = value.trim().split("\\s+");
/* 257 */     if (split.length < 2) {
/* 258 */       throw new IllegalArgumentException(
/* 259 */           String.format("Expected format for <Long><SPACE><java.util.concurrent.TimeUnit>: %s", new Object[] { value }));
/*     */     }
/* 261 */     String clean0 = split[0].trim();
/* 262 */     String clean1 = split[1].trim().toUpperCase(Locale.ROOT);
/* 263 */     String timeUnitStr = clean1.endsWith("S") ? clean1 : (clean1 + "S");
/* 264 */     return of(Long.parseLong(clean0), TimeUnit.valueOf(timeUnitStr));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   TimeValue(long duration, TimeUnit timeUnit) {
/* 273 */     this.duration = duration;
/* 274 */     this.timeUnit = Args.<TimeUnit>notNull(timeUnit, "timeUnit");
/*     */   }
/*     */   
/*     */   public long convert(TimeUnit targetTimeUnit) {
/* 278 */     Args.notNull(targetTimeUnit, "timeUnit");
/* 279 */     return targetTimeUnit.convert(this.duration, this.timeUnit);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 284 */     if (this == obj) {
/* 285 */       return true;
/*     */     }
/* 287 */     if (obj instanceof TimeValue) {
/* 288 */       TimeValue that = (TimeValue)obj;
/* 289 */       long thisDuration = convert(TimeUnit.NANOSECONDS);
/* 290 */       long thatDuration = that.convert(TimeUnit.NANOSECONDS);
/* 291 */       return (thisDuration == thatDuration);
/*     */     } 
/* 293 */     return false;
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
/*     */   public TimeValue divide(long divisor) {
/* 306 */     long newDuration = this.duration / divisor;
/* 307 */     return of(newDuration, this.timeUnit);
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
/*     */   public TimeValue divide(long divisor, TimeUnit targetTimeUnit) {
/* 322 */     return of(convert(targetTimeUnit) / divisor, targetTimeUnit);
/*     */   }
/*     */   
/*     */   public long getDuration() {
/* 326 */     return this.duration;
/*     */   }
/*     */   
/*     */   public TimeUnit getTimeUnit() {
/* 330 */     return this.timeUnit;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 335 */     int hash = 17;
/* 336 */     hash = LangUtils.hashCode(hash, Long.valueOf(convert(TimeUnit.NANOSECONDS)));
/* 337 */     return hash;
/*     */   }
/*     */   
/*     */   public TimeValue min(TimeValue other) {
/* 341 */     return (compareTo(other) > 0) ? other : this;
/*     */   }
/*     */   
/*     */   private TimeUnit min(TimeUnit other) {
/* 345 */     return (scale() > scale(other)) ? other : getTimeUnit();
/*     */   }
/*     */   
/*     */   private int scale() {
/* 349 */     return scale(this.timeUnit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int scale(TimeUnit tUnit) {
/* 360 */     switch (tUnit) {
/*     */       case NANOSECONDS:
/* 362 */         return 1;
/*     */       case MICROSECONDS:
/* 364 */         return 2;
/*     */       case MILLISECONDS:
/* 366 */         return 3;
/*     */       case SECONDS:
/* 368 */         return 4;
/*     */       case MINUTES:
/* 370 */         return 5;
/*     */       case HOURS:
/* 372 */         return 6;
/*     */       case DAYS:
/* 374 */         return 7;
/*     */     } 
/*     */     
/* 377 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void sleep() throws InterruptedException {
/* 382 */     this.timeUnit.sleep(this.duration);
/*     */   }
/*     */   
/*     */   public void timedJoin(Thread thread) throws InterruptedException {
/* 386 */     this.timeUnit.timedJoin(thread, this.duration);
/*     */   }
/*     */   
/*     */   public void timedWait(Object obj) throws InterruptedException {
/* 390 */     this.timeUnit.timedWait(obj, this.duration);
/*     */   }
/*     */   
/*     */   public long toDays() {
/* 394 */     return this.timeUnit.toDays(this.duration);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Duration toDuration() {
/* 404 */     return (this.duration == 0L) ? Duration.ZERO : Duration.of(this.duration, toChronoUnit(this.timeUnit));
/*     */   }
/*     */   
/*     */   public long toHours() {
/* 408 */     return this.timeUnit.toHours(this.duration);
/*     */   }
/*     */   
/*     */   public long toMicroseconds() {
/* 412 */     return this.timeUnit.toMicros(this.duration);
/*     */   }
/*     */   
/*     */   public long toMilliseconds() {
/* 416 */     return this.timeUnit.toMillis(this.duration);
/*     */   }
/*     */   
/*     */   public int toMillisecondsIntBound() {
/* 420 */     return asBoundInt(toMilliseconds());
/*     */   }
/*     */   
/*     */   public long toMinutes() {
/* 424 */     return this.timeUnit.toMinutes(this.duration);
/*     */   }
/*     */   
/*     */   public long toNanoseconds() {
/* 428 */     return this.timeUnit.toNanos(this.duration);
/*     */   }
/*     */   
/*     */   public long toSeconds() {
/* 432 */     return this.timeUnit.toSeconds(this.duration);
/*     */   }
/*     */   
/*     */   public int toSecondsIntBound() {
/* 436 */     return asBoundInt(toSeconds());
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(TimeValue other) {
/* 441 */     TimeUnit targetTimeUnit = min(other.getTimeUnit());
/* 442 */     return Long.compare(convert(targetTimeUnit), other.convert(targetTimeUnit));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 447 */     return String.format("%d %s", new Object[] { Long.valueOf(this.duration), this.timeUnit });
/*     */   }
/*     */   
/*     */   public Timeout toTimeout() {
/* 451 */     return Timeout.of(this.duration, this.timeUnit);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/util/TimeValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */