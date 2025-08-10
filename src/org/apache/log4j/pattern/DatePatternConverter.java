/*     */ package org.apache.log4j.pattern;
/*     */ 
/*     */ import java.text.DateFormat;
/*     */ import java.text.FieldPosition;
/*     */ import java.text.ParsePosition;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.TimeZone;
/*     */ import org.apache.log4j.helpers.LogLog;
/*     */ import org.apache.log4j.spi.LoggingEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DatePatternConverter
/*     */   extends LoggingEventPatternConverter
/*     */ {
/*     */   private static final String ABSOLUTE_FORMAT = "ABSOLUTE";
/*     */   private static final String ABSOLUTE_TIME_PATTERN = "HH:mm:ss,SSS";
/*     */   private static final String DATE_AND_TIME_FORMAT = "DATE";
/*     */   private static final String DATE_AND_TIME_PATTERN = "dd MMM yyyy HH:mm:ss,SSS";
/*     */   private static final String ISO8601_FORMAT = "ISO8601";
/*     */   private static final String ISO8601_PATTERN = "yyyy-MM-dd HH:mm:ss,SSS";
/*     */   private final CachedDateFormat df;
/*     */   
/*     */   private static class DefaultZoneDateFormat
/*     */     extends DateFormat
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     private final DateFormat dateFormat;
/*     */     
/*     */     public DefaultZoneDateFormat(DateFormat format) {
/*  87 */       this.dateFormat = format;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
/*  94 */       this.dateFormat.setTimeZone(TimeZone.getDefault());
/*  95 */       return this.dateFormat.format(date, toAppendTo, fieldPosition);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Date parse(String source, ParsePosition pos) {
/* 102 */       this.dateFormat.setTimeZone(TimeZone.getDefault());
/* 103 */       return this.dateFormat.parse(source, pos);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DatePatternConverter(String[] options) {
/* 113 */     super("Date", "date");
/*     */     
/*     */     String patternOption, pattern;
/*     */     
/* 117 */     if (options == null || options.length == 0) {
/*     */ 
/*     */       
/* 120 */       patternOption = null;
/*     */     } else {
/* 122 */       patternOption = options[0];
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 127 */     if (patternOption == null || patternOption.equalsIgnoreCase("ISO8601")) {
/* 128 */       pattern = "yyyy-MM-dd HH:mm:ss,SSS";
/* 129 */     } else if (patternOption.equalsIgnoreCase("ABSOLUTE")) {
/* 130 */       pattern = "HH:mm:ss,SSS";
/* 131 */     } else if (patternOption.equalsIgnoreCase("DATE")) {
/* 132 */       pattern = "dd MMM yyyy HH:mm:ss,SSS";
/*     */     } else {
/* 134 */       pattern = patternOption;
/*     */     } 
/*     */     
/* 137 */     int maximumCacheValidity = 1000;
/* 138 */     DateFormat simpleFormat = null;
/*     */     
/*     */     try {
/* 141 */       simpleFormat = new SimpleDateFormat(pattern);
/* 142 */       maximumCacheValidity = CachedDateFormat.getMaximumCacheValidity(pattern);
/* 143 */     } catch (IllegalArgumentException e) {
/* 144 */       LogLog.warn("Could not instantiate SimpleDateFormat with pattern " + patternOption, e);
/*     */ 
/*     */       
/* 147 */       simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
/*     */     } 
/*     */ 
/*     */     
/* 151 */     if (options != null && options.length > 1) {
/* 152 */       TimeZone tz = TimeZone.getTimeZone(options[1]);
/* 153 */       simpleFormat.setTimeZone(tz);
/*     */     } else {
/* 155 */       simpleFormat = new DefaultZoneDateFormat(simpleFormat);
/*     */     } 
/*     */     
/* 158 */     this.df = new CachedDateFormat(simpleFormat, maximumCacheValidity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DatePatternConverter newInstance(String[] options) {
/* 168 */     return new DatePatternConverter(options);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(LoggingEvent event, StringBuffer output) {
/* 175 */     synchronized (this) {
/* 176 */       this.df.format(event.timeStamp, output);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(Object obj, StringBuffer output) {
/* 184 */     if (obj instanceof Date) {
/* 185 */       format((Date)obj, output);
/*     */     }
/*     */     
/* 188 */     super.format(obj, output);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(Date date, StringBuffer toAppendTo) {
/* 198 */     synchronized (this) {
/* 199 */       this.df.format(date.getTime(), toAppendTo);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/pattern/DatePatternConverter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */