/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.time.Instant;
/*    */ import java.time.LocalDate;
/*    */ import java.time.LocalDateTime;
/*    */ import java.time.LocalTime;
/*    */ import java.time.ZoneId;
/*    */ import java.time.ZoneOffset;
/*    */ import java.util.Date;
/*    */ import java.util.Locale;
/*    */ import java.util.TimeZone;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Dates
/*    */ {
/* 46 */   public static final Locale BRAZIL = new Locale("pt", "BR");
/*    */   
/* 48 */   public static final TimeZone UTC_TIMEZONE = TimeZone.getTimeZone("UTC");
/*    */   
/* 50 */   public static final ZoneId UTC_ZONEID = UTC_TIMEZONE.toZoneId();
/*    */   
/* 52 */   public static final ZoneOffset UTC_ZONEOFFSET = ZoneOffset.UTC;
/*    */   
/*    */   public static boolean isBetween(LocalTime target, LocalTime begin, LocalTime end) {
/* 55 */     return (target.isAfter(begin) && target.isBefore(end));
/*    */   }
/*    */   
/*    */   public static LocalDate localDate(long date, ZoneId zoneId) {
/* 59 */     return localDateTime(date, zoneId).toLocalDate();
/*    */   }
/*    */   
/*    */   public static Date toDate(LocalDate date, ZoneId zoneId) {
/* 63 */     return Date.from(date.atStartOfDay(zoneId).toInstant());
/*    */   }
/*    */   
/*    */   public static LocalDateTime localDateTime(long date, ZoneId zoneId) {
/* 67 */     return LocalDateTime.ofInstant(Instant.ofEpochMilli(date), zoneId);
/*    */   }
/*    */   
/*    */   public static LocalDateTime LocalDateTime(long date) {
/* 71 */     return localDateTime(date, UTC_ZONEID);
/*    */   }
/*    */   
/*    */   public static String stringNow() {
/* 75 */     return format("yyyy-MM-dd_HH'h'mm'm'ss's'S'ms'", new Date());
/*    */   }
/*    */   
/*    */   public static String timeNow() {
/* 79 */     return format("HH'h'mm'm'ss's'S'ms'", new Date());
/*    */   }
/*    */   
/*    */   public static String defaultFormat(Date date) {
/* 83 */     return (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", BRAZIL)).format(date);
/*    */   }
/*    */   
/*    */   public static String defaultFormat(LocalDate date) {
/* 87 */     return Strings.padStart(date.getDayOfMonth(), 2) + "/" + Strings.padStart(date.getMonthValue(), 2) + "/" + Strings.padStart(date.getYear(), 4);
/*    */   }
/*    */   
/*    */   public static String format(String format, Date date) {
/* 91 */     return (new SimpleDateFormat(format, BRAZIL)).format(date);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/Dates.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */