/*     */ package org.apache.hc.core5.util;
/*     */ 
/*     */ import java.text.ParseException;
/*     */ import java.time.Duration;
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
/*     */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*     */ public class Timeout
/*     */   extends TimeValue
/*     */ {
/*  48 */   public static final Timeout ZERO_MILLISECONDS = of(0L, TimeUnit.MILLISECONDS);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   public static final Timeout ONE_MILLISECOND = of(1L, TimeUnit.MILLISECONDS);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  58 */   public static final Timeout DISABLED = ZERO_MILLISECONDS;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Timeout defaultsToDisabled(Timeout timeout) {
/*  67 */     return defaultsTo(timeout, DISABLED);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Timeout of(Duration duration) {
/*  78 */     long seconds = duration.getSeconds();
/*  79 */     long nanoOfSecond = duration.getNano();
/*  80 */     if (seconds == 0L)
/*     */     {
/*  82 */       return of(nanoOfSecond, TimeUnit.NANOSECONDS); } 
/*  83 */     if (nanoOfSecond == 0L)
/*     */     {
/*  85 */       return of(seconds, TimeUnit.SECONDS);
/*     */     }
/*     */     
/*     */     try {
/*  89 */       return of(duration.toNanos(), TimeUnit.NANOSECONDS);
/*  90 */     } catch (ArithmeticException e) {
/*     */       try {
/*  92 */         return of(duration.toMillis(), TimeUnit.MILLISECONDS);
/*  93 */       } catch (ArithmeticException e1) {
/*     */         
/*  95 */         return of(seconds, TimeUnit.SECONDS);
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
/*     */   public static Timeout of(long duration, TimeUnit timeUnit) {
/* 108 */     return new Timeout(duration, timeUnit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Timeout ofDays(long days) {
/* 118 */     return of(days, TimeUnit.DAYS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Timeout ofHours(long hours) {
/* 128 */     return of(hours, TimeUnit.HOURS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Timeout ofMicroseconds(long microseconds) {
/* 138 */     return of(microseconds, TimeUnit.MICROSECONDS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Timeout ofMilliseconds(long milliseconds) {
/* 148 */     return of(milliseconds, TimeUnit.MILLISECONDS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Timeout ofMinutes(long minutes) {
/* 158 */     return of(minutes, TimeUnit.MINUTES);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Timeout ofNanoseconds(long nanoseconds) {
/* 168 */     return of(nanoseconds, TimeUnit.NANOSECONDS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Timeout ofSeconds(long seconds) {
/* 178 */     return of(seconds, TimeUnit.SECONDS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Timeout parse(String value) throws ParseException {
/* 189 */     return TimeValue.parse(value).toTimeout();
/*     */   }
/*     */   
/*     */   Timeout(long duration, TimeUnit timeUnit) {
/* 193 */     super(Args.notNegative(duration, "duration"), Args.<TimeUnit>notNull(timeUnit, "timeUnit"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDisabled() {
/* 202 */     return (getDuration() == 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled() {
/* 211 */     return !isDisabled();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/util/Timeout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */