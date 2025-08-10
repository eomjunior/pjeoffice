/*     */ package org.apache.log4j.helpers;
/*     */ 
/*     */ import java.text.FieldPosition;
/*     */ import java.text.ParsePosition;
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
/*     */ public class ISO8601DateFormat
/*     */   extends AbsoluteTimeDateFormat
/*     */ {
/*     */   private static final long serialVersionUID = -759840745298755296L;
/*     */   private static long lastTime;
/*     */   
/*     */   public ISO8601DateFormat() {}
/*     */   
/*     */   public ISO8601DateFormat(TimeZone timeZone) {
/*  49 */     super(timeZone);
/*     */   }
/*     */ 
/*     */   
/*  53 */   private static char[] lastTimeString = new char[20];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringBuffer format(Date date, StringBuffer sbuf, FieldPosition fieldPosition) {
/*  63 */     long now = date.getTime();
/*  64 */     int millis = (int)(now % 1000L);
/*     */     
/*  66 */     if (now - millis != lastTime || lastTimeString[0] == '\000') {
/*     */       String month;
/*     */ 
/*     */ 
/*     */       
/*  71 */       this.calendar.setTime(date);
/*     */       
/*  73 */       int start = sbuf.length();
/*     */       
/*  75 */       int year = this.calendar.get(1);
/*  76 */       sbuf.append(year);
/*     */ 
/*     */       
/*  79 */       switch (this.calendar.get(2)) {
/*     */         case 0:
/*  81 */           month = "-01-";
/*     */           break;
/*     */         case 1:
/*  84 */           month = "-02-";
/*     */           break;
/*     */         case 2:
/*  87 */           month = "-03-";
/*     */           break;
/*     */         case 3:
/*  90 */           month = "-04-";
/*     */           break;
/*     */         case 4:
/*  93 */           month = "-05-";
/*     */           break;
/*     */         case 5:
/*  96 */           month = "-06-";
/*     */           break;
/*     */         case 6:
/*  99 */           month = "-07-";
/*     */           break;
/*     */         case 7:
/* 102 */           month = "-08-";
/*     */           break;
/*     */         case 8:
/* 105 */           month = "-09-";
/*     */           break;
/*     */         case 9:
/* 108 */           month = "-10-";
/*     */           break;
/*     */         case 10:
/* 111 */           month = "-11-";
/*     */           break;
/*     */         case 11:
/* 114 */           month = "-12-";
/*     */           break;
/*     */         default:
/* 117 */           month = "-NA-";
/*     */           break;
/*     */       } 
/* 120 */       sbuf.append(month);
/*     */       
/* 122 */       int day = this.calendar.get(5);
/* 123 */       if (day < 10)
/* 124 */         sbuf.append('0'); 
/* 125 */       sbuf.append(day);
/*     */       
/* 127 */       sbuf.append(' ');
/*     */       
/* 129 */       int hour = this.calendar.get(11);
/* 130 */       if (hour < 10) {
/* 131 */         sbuf.append('0');
/*     */       }
/* 133 */       sbuf.append(hour);
/* 134 */       sbuf.append(':');
/*     */       
/* 136 */       int mins = this.calendar.get(12);
/* 137 */       if (mins < 10) {
/* 138 */         sbuf.append('0');
/*     */       }
/* 140 */       sbuf.append(mins);
/* 141 */       sbuf.append(':');
/*     */       
/* 143 */       int secs = this.calendar.get(13);
/* 144 */       if (secs < 10) {
/* 145 */         sbuf.append('0');
/*     */       }
/* 147 */       sbuf.append(secs);
/*     */       
/* 149 */       sbuf.append(',');
/*     */ 
/*     */       
/* 152 */       sbuf.getChars(start, sbuf.length(), lastTimeString, 0);
/* 153 */       lastTime = now - millis;
/*     */     } else {
/* 155 */       sbuf.append(lastTimeString);
/*     */     } 
/*     */     
/* 158 */     if (millis < 100)
/* 159 */       sbuf.append('0'); 
/* 160 */     if (millis < 10) {
/* 161 */       sbuf.append('0');
/*     */     }
/* 163 */     sbuf.append(millis);
/* 164 */     return sbuf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date parse(String s, ParsePosition pos) {
/* 171 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/helpers/ISO8601DateFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */