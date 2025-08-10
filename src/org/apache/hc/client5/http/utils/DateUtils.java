/*     */ package org.apache.hc.client5.http.utils;
/*     */ 
/*     */ import java.time.Instant;
/*     */ import java.time.LocalDateTime;
/*     */ import java.time.ZoneId;
/*     */ import java.time.ZoneOffset;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.time.format.DateTimeFormatterBuilder;
/*     */ import java.time.format.DateTimeParseException;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.MessageHeaders;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DateUtils
/*     */ {
/*     */   public static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";
/*  64 */   public static final DateTimeFormatter FORMATTER_RFC1123 = (new DateTimeFormatterBuilder())
/*  65 */     .parseLenient()
/*  66 */     .parseCaseInsensitive()
/*  67 */     .appendPattern("EEE, dd MMM yyyy HH:mm:ss zzz")
/*  68 */     .toFormatter(Locale.ENGLISH);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String PATTERN_RFC1036 = "EEE, dd-MMM-yy HH:mm:ss zzz";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   public static final DateTimeFormatter FORMATTER_RFC1036 = (new DateTimeFormatterBuilder())
/*  81 */     .parseLenient()
/*  82 */     .parseCaseInsensitive()
/*  83 */     .appendPattern("EEE, dd-MMM-yy HH:mm:ss zzz")
/*  84 */     .toFormatter(Locale.ENGLISH);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String PATTERN_ASCTIME = "EEE MMM d HH:mm:ss yyyy";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   public static final DateTimeFormatter FORMATTER_ASCTIME = (new DateTimeFormatterBuilder())
/*  98 */     .parseLenient()
/*  99 */     .parseCaseInsensitive()
/* 100 */     .appendPattern("EEE MMM d HH:mm:ss yyyy")
/* 101 */     .toFormatter(Locale.ENGLISH);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   public static final DateTimeFormatter[] STANDARD_PATTERNS = new DateTimeFormatter[] { FORMATTER_RFC1123, FORMATTER_RFC1036, FORMATTER_ASCTIME };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 114 */   static final ZoneId GMT_ID = ZoneId.of("GMT");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Date toDate(Instant instant) {
/* 120 */     return (instant != null) ? new Date(instant.toEpochMilli()) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Instant toInstant(Date date) {
/* 127 */     return (date != null) ? Instant.ofEpochMilli(date.getTime()) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LocalDateTime toUTC(Instant instant) {
/* 134 */     return (instant != null) ? instant.atZone(ZoneOffset.UTC).toLocalDateTime() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LocalDateTime toUTC(Date date) {
/* 141 */     return toUTC(toInstant(date));
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
/*     */   public static Instant parseDate(String dateValue, DateTimeFormatter... dateFormatters) {
/* 155 */     Args.notNull(dateValue, "Date value");
/* 156 */     String v = dateValue;
/*     */ 
/*     */     
/* 159 */     if (v.length() > 1 && v.startsWith("'") && v.endsWith("'")) {
/* 160 */       v = v.substring(1, v.length() - 1);
/*     */     }
/*     */     
/* 163 */     for (DateTimeFormatter dateFormatter : dateFormatters) {
/*     */       try {
/* 165 */         return Instant.from(dateFormatter.parse(v));
/* 166 */       } catch (DateTimeParseException dateTimeParseException) {}
/*     */     } 
/*     */     
/* 169 */     return null;
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
/*     */   public static Instant parseStandardDate(String dateValue) {
/* 183 */     return parseDate(dateValue, STANDARD_PATTERNS);
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
/*     */   public static Instant parseStandardDate(MessageHeaders headers, String headerName) {
/* 197 */     if (headers == null) {
/* 198 */       return null;
/*     */     }
/* 200 */     Header header = headers.getFirstHeader(headerName);
/* 201 */     if (header == null) {
/* 202 */       return null;
/*     */     }
/* 204 */     return parseStandardDate(header.getValue());
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
/*     */   public static String formatStandardDate(Instant instant) {
/* 218 */     return formatDate(instant, FORMATTER_RFC1123);
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
/*     */   public static String formatDate(Instant instant, DateTimeFormatter dateTimeFormatter) {
/* 233 */     Args.notNull(instant, "Instant");
/* 234 */     Args.notNull(dateTimeFormatter, "DateTimeFormatter");
/* 235 */     return dateTimeFormatter.format(instant.atZone(GMT_ID));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 242 */   public static final TimeZone GMT = TimeZone.getTimeZone("GMT");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static Date parseDate(String dateValue) {
/* 256 */     return parseDate(dateValue, null, null);
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
/*     */   @Deprecated
/*     */   public static Date parseDate(MessageHeaders headers, String headerName) {
/* 273 */     return toDate(parseStandardDate(headers, headerName));
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
/*     */   @Deprecated
/*     */   public static boolean isAfter(MessageHeaders message1, MessageHeaders message2, String headerName) {
/* 297 */     if (message1 != null && message2 != null) {
/* 298 */       Header dateHeader1 = message1.getFirstHeader(headerName);
/* 299 */       if (dateHeader1 != null) {
/* 300 */         Header dateHeader2 = message2.getFirstHeader(headerName);
/* 301 */         if (dateHeader2 != null) {
/* 302 */           Date date1 = parseDate(dateHeader1.getValue());
/* 303 */           if (date1 != null) {
/* 304 */             Date date2 = parseDate(dateHeader2.getValue());
/* 305 */             if (date2 != null) {
/* 306 */               return date1.after(date2);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 312 */     return false;
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
/*     */   @Deprecated
/*     */   public static boolean isBefore(MessageHeaders message1, MessageHeaders message2, String headerName) {
/* 336 */     if (message1 != null && message2 != null) {
/* 337 */       Header dateHeader1 = message1.getFirstHeader(headerName);
/* 338 */       if (dateHeader1 != null) {
/* 339 */         Header dateHeader2 = message2.getFirstHeader(headerName);
/* 340 */         if (dateHeader2 != null) {
/* 341 */           Date date1 = parseDate(dateHeader1.getValue());
/* 342 */           if (date1 != null) {
/* 343 */             Date date2 = parseDate(dateHeader2.getValue());
/* 344 */             if (date2 != null) {
/* 345 */               return date1.before(date2);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 351 */     return false;
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
/*     */   @Deprecated
/*     */   public static Date parseDate(String dateValue, String[] dateFormats) {
/* 366 */     return parseDate(dateValue, dateFormats, null);
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
/*     */   @Deprecated
/*     */   public static Date parseDate(String dateValue, String[] dateFormats, Date startDate) {
/*     */     DateTimeFormatter[] dateTimeFormatters;
/* 389 */     if (dateFormats != null) {
/* 390 */       dateTimeFormatters = new DateTimeFormatter[dateFormats.length];
/* 391 */       for (int i = 0; i < dateFormats.length; i++) {
/* 392 */         dateTimeFormatters[i] = (new DateTimeFormatterBuilder())
/* 393 */           .parseLenient()
/* 394 */           .parseCaseInsensitive()
/* 395 */           .appendPattern(dateFormats[i])
/* 396 */           .toFormatter();
/*     */       }
/*     */     } else {
/* 399 */       dateTimeFormatters = STANDARD_PATTERNS;
/*     */     } 
/* 401 */     return toDate(parseDate(dateValue, dateTimeFormatters));
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
/*     */   @Deprecated
/*     */   public static String formatDate(Date date) {
/* 416 */     return formatStandardDate(toInstant(date));
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
/*     */   @Deprecated
/*     */   public static String formatDate(Date date, String pattern) {
/* 432 */     Args.notNull(date, "Date");
/* 433 */     Args.notNull(pattern, "Pattern");
/* 434 */     return DateTimeFormatter.ofPattern(pattern).format(toInstant(date).atZone(GMT_ID));
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public static void clearThreadLocal() {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/utils/DateUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */