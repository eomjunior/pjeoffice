/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import java.text.ChoiceFormat;
/*     */ import java.text.DateFormat;
/*     */ import java.text.MessageFormat;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   private static final int ONE_SECOND = 1000;
/*     */   private static final int ONE_MINUTE = 60;
/*     */   private static final int ONE_HOUR = 60;
/*     */   private static final int TEN = 10;
/*     */   public static final String ISO8601_DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
/*     */   public static final String ISO8601_DATE_PATTERN = "yyyy-MM-dd";
/*     */   public static final String ISO8601_TIME_PATTERN = "HH:mm:ss";
/*     */   @Deprecated
/*  72 */   public static final DateFormat DATE_HEADER_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ", Locale.US);
/*     */ 
/*     */   
/*  75 */   private static final DateFormat DATE_HEADER_FORMAT_INT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ", Locale.US);
/*     */ 
/*     */ 
/*     */   
/*  79 */   private static final MessageFormat MINUTE_SECONDS = new MessageFormat("{0}{1}");
/*     */ 
/*     */   
/*  82 */   private static final double[] LIMITS = new double[] { 0.0D, 1.0D, 2.0D };
/*     */   
/*  84 */   private static final String[] MINUTES_PART = new String[] { "", "1 minute ", "{0,number,###############} minutes " };
/*     */   
/*  86 */   private static final String[] SECONDS_PART = new String[] { "0 seconds", "1 second", "{1,number} seconds" };
/*     */   
/*  88 */   private static final ChoiceFormat MINUTES_FORMAT = new ChoiceFormat(LIMITS, MINUTES_PART);
/*     */ 
/*     */   
/*  91 */   private static final ChoiceFormat SECONDS_FORMAT = new ChoiceFormat(LIMITS, SECONDS_PART);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   public static final ThreadLocal<DateFormat> EN_US_DATE_FORMAT_MIN = ThreadLocal.withInitial(() -> new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 110 */   public static final ThreadLocal<DateFormat> EN_US_DATE_FORMAT_SEC = ThreadLocal.withInitial(() -> new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.US));
/*     */   
/*     */   static {
/* 113 */     MINUTE_SECONDS.setFormat(0, MINUTES_FORMAT);
/* 114 */     MINUTE_SECONDS.setFormat(1, SECONDS_FORMAT);
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
/*     */   public static String format(long date, String pattern) {
/* 129 */     return format(new Date(date), pattern);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String format(Date date, String pattern) {
/* 140 */     DateFormat df = createDateFormat(pattern);
/* 141 */     return df.format(date);
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
/*     */   public static String formatElapsedTime(long millis) {
/* 158 */     long seconds = millis / 1000L;
/* 159 */     long minutes = seconds / 60L;
/* 160 */     return MINUTE_SECONDS.format(new Object[] { Long.valueOf(minutes), Long.valueOf(seconds % 60L) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static DateFormat createDateFormat(String pattern) {
/* 169 */     SimpleDateFormat sdf = new SimpleDateFormat(pattern);
/* 170 */     TimeZone gmt = TimeZone.getTimeZone("GMT");
/* 171 */     sdf.setTimeZone(gmt);
/* 172 */     sdf.setLenient(true);
/* 173 */     return sdf;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getPhaseOfMoon(Calendar cal) {
/* 212 */     int dayOfTheYear = cal.get(6);
/* 213 */     int yearInMetonicCycle = (cal.get(1) - 1900) % 19 + 1;
/* 214 */     int epact = (11 * yearInMetonicCycle + 18) % 30;
/* 215 */     if ((epact == 25 && yearInMetonicCycle > 11) || epact == 24) {
/* 216 */       epact++;
/*     */     }
/* 218 */     return ((dayOfTheYear + epact) * 6 + 11) % 177 / 22 & 0x7;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getDateForHeader() {
/* 229 */     Calendar cal = Calendar.getInstance();
/* 230 */     TimeZone tz = cal.getTimeZone();
/* 231 */     int offset = tz.getOffset(cal.get(0), cal
/* 232 */         .get(1), cal
/* 233 */         .get(2), cal
/* 234 */         .get(5), cal
/* 235 */         .get(7), cal
/* 236 */         .get(14));
/* 237 */     StringBuilder tzMarker = new StringBuilder((offset < 0) ? "-" : "+");
/* 238 */     offset = Math.abs(offset);
/* 239 */     int hours = offset / 3600000;
/* 240 */     int minutes = offset / 60000 - 60 * hours;
/* 241 */     if (hours < 10) {
/* 242 */       tzMarker.append("0");
/*     */     }
/* 244 */     tzMarker.append(hours);
/* 245 */     if (minutes < 10) {
/* 246 */       tzMarker.append("0");
/*     */     }
/* 248 */     tzMarker.append(minutes);
/* 249 */     synchronized (DATE_HEADER_FORMAT_INT) {
/* 250 */       return DATE_HEADER_FORMAT_INT.format(cal.getTime()) + tzMarker.toString();
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
/*     */   public static Date parseDateFromHeader(String datestr) throws ParseException {
/* 265 */     synchronized (DATE_HEADER_FORMAT_INT) {
/* 266 */       return DATE_HEADER_FORMAT_INT.parse(datestr);
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
/*     */   public static Date parseIso8601DateTime(String datestr) throws ParseException {
/* 283 */     return (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")).parse(datestr);
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
/*     */   public static Date parseIso8601Date(String datestr) throws ParseException {
/* 298 */     return (new SimpleDateFormat("yyyy-MM-dd")).parse(datestr);
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
/*     */   public static Date parseIso8601DateTimeOrDate(String datestr) throws ParseException {
/*     */     try {
/* 315 */       return parseIso8601DateTime(datestr);
/* 316 */     } catch (ParseException px) {
/* 317 */       return parseIso8601Date(datestr);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 322 */   private static final ThreadLocal<DateFormat> iso8601WithTimeZone = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z"));
/*     */ 
/*     */ 
/*     */   
/* 326 */   private static final Pattern iso8601normalizer = Pattern.compile("^(\\d{4,}-\\d{2}-\\d{2})[Tt ](\\d{2}:\\d{2}(:\\d{2}(\\.\\d{3})?)?) ?(?:Z|([+-]\\d{2})(?::?(\\d{2}))?)?$");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Date parseLenientDateTime(String dateStr) throws ParseException {
/*     */     try {
/* 349 */       return new Date(Long.parseLong(dateStr));
/* 350 */     } catch (NumberFormatException numberFormatException) {
/*     */ 
/*     */       
/*     */       try {
/* 354 */         return ((DateFormat)EN_US_DATE_FORMAT_MIN.get()).parse(dateStr);
/* 355 */       } catch (ParseException parseException) {
/*     */ 
/*     */         
/*     */         try {
/* 359 */           return ((DateFormat)EN_US_DATE_FORMAT_SEC.get()).parse(dateStr);
/* 360 */         } catch (ParseException parseException1) {
/*     */ 
/*     */           
/* 363 */           Matcher m = iso8601normalizer.matcher(dateStr);
/* 364 */           if (!m.find()) {
/* 365 */             throw new ParseException(dateStr, 0);
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 371 */           String normISO = m.group(1) + " " + ((m.group(3) == null) ? (m.group(2) + ":00") : m.group(2)) + ((m.group(4) == null) ? ".000 " : " ") + ((m.group(5) == null) ? "+00" : m.group(5)) + ((m.group(6) == null) ? "00" : m.group(6));
/* 372 */           return ((DateFormat)iso8601WithTimeZone.get()).parse(normISO);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/DateUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */