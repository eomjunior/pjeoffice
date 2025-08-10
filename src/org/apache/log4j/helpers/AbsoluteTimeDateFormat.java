/*     */ package org.apache.log4j.helpers;
/*     */ 
/*     */ import java.text.DateFormat;
/*     */ import java.text.FieldPosition;
/*     */ import java.text.ParsePosition;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.TimeZone;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AbsoluteTimeDateFormat
/*     */   extends DateFormat
/*     */ {
/*     */   private static final long serialVersionUID = -388856345976723342L;
/*     */   public static final String ABS_TIME_DATE_FORMAT = "ABSOLUTE";
/*     */   public static final String DATE_AND_TIME_DATE_FORMAT = "DATE";
/*     */   public static final String ISO8601_DATE_FORMAT = "ISO8601";
/*     */   private static long previousTime;
/*     */   
/*     */   public AbsoluteTimeDateFormat() {
/*  61 */     setCalendar(Calendar.getInstance());
/*     */   }
/*     */   
/*     */   public AbsoluteTimeDateFormat(TimeZone timeZone) {
/*  65 */     setCalendar(Calendar.getInstance(timeZone));
/*     */   }
/*     */ 
/*     */   
/*  69 */   private static char[] previousTimeWithoutMillis = new char[9];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringBuffer format(Date date, StringBuffer sbuf, FieldPosition fieldPosition) {
/*  81 */     long now = date.getTime();
/*  82 */     int millis = (int)(now % 1000L);
/*     */     
/*  84 */     if (now - millis != previousTime || previousTimeWithoutMillis[0] == '\000') {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  89 */       this.calendar.setTime(date);
/*     */       
/*  91 */       int start = sbuf.length();
/*     */       
/*  93 */       int hour = this.calendar.get(11);
/*  94 */       if (hour < 10) {
/*  95 */         sbuf.append('0');
/*     */       }
/*  97 */       sbuf.append(hour);
/*  98 */       sbuf.append(':');
/*     */       
/* 100 */       int mins = this.calendar.get(12);
/* 101 */       if (mins < 10) {
/* 102 */         sbuf.append('0');
/*     */       }
/* 104 */       sbuf.append(mins);
/* 105 */       sbuf.append(':');
/*     */       
/* 107 */       int secs = this.calendar.get(13);
/* 108 */       if (secs < 10) {
/* 109 */         sbuf.append('0');
/*     */       }
/* 111 */       sbuf.append(secs);
/* 112 */       sbuf.append(',');
/*     */ 
/*     */       
/* 115 */       sbuf.getChars(start, sbuf.length(), previousTimeWithoutMillis, 0);
/*     */       
/* 117 */       previousTime = now - millis;
/*     */     } else {
/* 119 */       sbuf.append(previousTimeWithoutMillis);
/*     */     } 
/*     */     
/* 122 */     if (millis < 100)
/* 123 */       sbuf.append('0'); 
/* 124 */     if (millis < 10) {
/* 125 */       sbuf.append('0');
/*     */     }
/* 127 */     sbuf.append(millis);
/* 128 */     return sbuf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date parse(String s, ParsePosition pos) {
/* 135 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/helpers/AbsoluteTimeDateFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */