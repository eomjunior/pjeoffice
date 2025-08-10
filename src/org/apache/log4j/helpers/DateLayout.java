/*     */ package org.apache.log4j.helpers;
/*     */ 
/*     */ import java.text.DateFormat;
/*     */ import java.text.FieldPosition;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.TimeZone;
/*     */ import org.apache.log4j.Layout;
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
/*     */ public abstract class DateLayout
/*     */   extends Layout
/*     */ {
/*     */   public static final String NULL_DATE_FORMAT = "NULL";
/*     */   public static final String RELATIVE_TIME_DATE_FORMAT = "RELATIVE";
/*  50 */   protected FieldPosition pos = new FieldPosition(0);
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String DATE_FORMAT_OPTION = "DateFormat";
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String TIMEZONE_OPTION = "TimeZone";
/*     */ 
/*     */   
/*     */   private String timeZoneID;
/*     */ 
/*     */   
/*     */   private String dateFormatOption;
/*     */ 
/*     */   
/*     */   protected DateFormat dateFormat;
/*     */ 
/*     */   
/*  70 */   protected Date date = new Date();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getOptionStrings() {
/*  77 */     return new String[] { "DateFormat", "TimeZone" };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOption(String option, String value) {
/*  85 */     if (option.equalsIgnoreCase("DateFormat")) {
/*  86 */       this.dateFormatOption = value.toUpperCase();
/*  87 */     } else if (option.equalsIgnoreCase("TimeZone")) {
/*  88 */       this.timeZoneID = value;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDateFormat(String dateFormat) {
/*  98 */     if (dateFormat != null) {
/*  99 */       this.dateFormatOption = dateFormat;
/*     */     }
/* 101 */     setDateFormat(this.dateFormatOption, TimeZone.getDefault());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDateFormat() {
/* 108 */     return this.dateFormatOption;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeZone(String timeZone) {
/* 116 */     this.timeZoneID = timeZone;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTimeZone() {
/* 123 */     return this.timeZoneID;
/*     */   }
/*     */   
/*     */   public void activateOptions() {
/* 127 */     setDateFormat(this.dateFormatOption);
/* 128 */     if (this.timeZoneID != null && this.dateFormat != null) {
/* 129 */       this.dateFormat.setTimeZone(TimeZone.getTimeZone(this.timeZoneID));
/*     */     }
/*     */   }
/*     */   
/*     */   public void dateFormat(StringBuffer buf, LoggingEvent event) {
/* 134 */     if (this.dateFormat != null) {
/* 135 */       this.date.setTime(event.timeStamp);
/* 136 */       this.dateFormat.format(this.date, buf, this.pos);
/* 137 */       buf.append(' ');
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDateFormat(DateFormat dateFormat, TimeZone timeZone) {
/* 146 */     this.dateFormat = dateFormat;
/* 147 */     this.dateFormat.setTimeZone(timeZone);
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
/*     */   public void setDateFormat(String dateFormatType, TimeZone timeZone) {
/* 165 */     if (dateFormatType == null) {
/* 166 */       this.dateFormat = null;
/*     */       
/*     */       return;
/*     */     } 
/* 170 */     if (dateFormatType.equalsIgnoreCase("NULL")) {
/* 171 */       this.dateFormat = null;
/* 172 */     } else if (dateFormatType.equalsIgnoreCase("RELATIVE")) {
/* 173 */       this.dateFormat = new RelativeTimeDateFormat();
/* 174 */     } else if (dateFormatType.equalsIgnoreCase("ABSOLUTE")) {
/* 175 */       this.dateFormat = new AbsoluteTimeDateFormat(timeZone);
/* 176 */     } else if (dateFormatType.equalsIgnoreCase("DATE")) {
/* 177 */       this.dateFormat = new DateTimeDateFormat(timeZone);
/* 178 */     } else if (dateFormatType.equalsIgnoreCase("ISO8601")) {
/* 179 */       this.dateFormat = new ISO8601DateFormat(timeZone);
/*     */     } else {
/* 181 */       this.dateFormat = new SimpleDateFormat(dateFormatType);
/* 182 */       this.dateFormat.setTimeZone(timeZone);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/helpers/DateLayout.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */