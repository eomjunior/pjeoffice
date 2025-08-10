/*     */ package org.apache.hc.core5.util;
/*     */ 
/*     */ import java.text.ParseException;
/*     */ import java.time.Instant;
/*     */ import java.time.ZoneOffset;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.time.format.DateTimeFormatterBuilder;
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
/*     */ public class Deadline
/*     */ {
/*     */   public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
/*     */   private static final long INTERNAL_MAX_VALUE = 9223372036854775807L;
/*     */   private static final long INTERNAL_MIN_VALUE = 0L;
/*  63 */   public static Deadline MAX_VALUE = new Deadline(Long.MAX_VALUE);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   public static Deadline MIN_VALUE = new Deadline(0L);
/*     */   
/*  70 */   private static final DateTimeFormatter DATE_TIME_FORMATTER = (new DateTimeFormatterBuilder())
/*  71 */     .parseLenient()
/*  72 */     .parseCaseInsensitive()
/*  73 */     .appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
/*  74 */     .toFormatter();
/*     */ 
/*     */   
/*     */   private volatile boolean frozen;
/*     */   
/*     */   private volatile long lastCheck;
/*     */   
/*     */   private final long value;
/*     */ 
/*     */   
/*     */   public static Deadline calculate(long timeMillis, TimeValue timeValue) {
/*  85 */     if (TimeValue.isPositive(timeValue)) {
/*     */       
/*  87 */       long deadline = timeMillis + timeValue.toMilliseconds();
/*  88 */       return (deadline < 0L) ? MAX_VALUE : fromUnixMilliseconds(deadline);
/*     */     } 
/*  90 */     return MAX_VALUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Deadline calculate(TimeValue timeValue) {
/* 101 */     return calculate(System.currentTimeMillis(), timeValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Deadline fromUnixMilliseconds(long value) {
/* 111 */     if (value == Long.MAX_VALUE) {
/* 112 */       return MAX_VALUE;
/*     */     }
/* 114 */     if (value == 0L) {
/* 115 */       return MIN_VALUE;
/*     */     }
/* 117 */     return new Deadline(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Deadline parse(String source) throws ParseException {
/* 128 */     if (source == null) {
/* 129 */       return null;
/*     */     }
/* 131 */     Instant instant = Instant.from(DATE_TIME_FORMATTER.parse(source));
/* 132 */     return fromUnixMilliseconds(instant.toEpochMilli());
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
/*     */   private Deadline(long deadlineMillis) {
/* 151 */     this.value = deadlineMillis;
/* 152 */     setLastCheck();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 158 */     if (this == obj) {
/* 159 */       return true;
/*     */     }
/* 161 */     if (obj == null) {
/* 162 */       return false;
/*     */     }
/* 164 */     if (getClass() != obj.getClass()) {
/* 165 */       return false;
/*     */     }
/* 167 */     Deadline other = (Deadline)obj;
/* 168 */     return (this.value == other.value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 174 */     return Long.hashCode(this.value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String format(TimeUnit overdueTimeUnit) {
/* 184 */     return String.format("Deadline: %s, %s overdue", new Object[] { formatTarget(), TimeValue.of(remaining(), overdueTimeUnit) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String formatTarget() {
/* 193 */     return DATE_TIME_FORMATTER.format(Instant.ofEpochMilli(this.value).atOffset(ZoneOffset.UTC));
/*     */   }
/*     */   
/*     */   public Deadline freeze() {
/* 197 */     this.frozen = true;
/* 198 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long getLastCheck() {
/* 207 */     return this.lastCheck;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getValue() {
/* 216 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBefore(long millis) {
/* 226 */     return (this.value < millis);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isExpired() {
/* 235 */     setLastCheck();
/* 236 */     return (this.value < this.lastCheck);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMax() {
/* 245 */     return (this.value == Long.MAX_VALUE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMin() {
/* 254 */     return (this.value == 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNotExpired() {
/* 263 */     setLastCheck();
/* 264 */     return (this.value >= this.lastCheck);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Deadline min(Deadline other) {
/* 274 */     return (this.value <= other.value) ? this : other;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long remaining() {
/* 283 */     setLastCheck();
/* 284 */     return this.value - this.lastCheck;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeValue remainingTimeValue() {
/* 293 */     return TimeValue.of(remaining(), TimeUnit.MILLISECONDS);
/*     */   }
/*     */   
/*     */   private void setLastCheck() {
/* 297 */     if (!this.frozen) {
/* 298 */       this.lastCheck = System.currentTimeMillis();
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString() {
/* 303 */     return formatTarget();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/util/Deadline.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */