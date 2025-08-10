/*     */ package com.itextpdf.xmp.impl;
/*     */ 
/*     */ import com.itextpdf.xmp.XMPDateTime;
/*     */ import com.itextpdf.xmp.XMPException;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.Locale;
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
/*     */ public class XMPDateTimeImpl
/*     */   implements XMPDateTime
/*     */ {
/*  53 */   private int year = 0;
/*     */   
/*  55 */   private int month = 0;
/*     */   
/*  57 */   private int day = 0;
/*     */   
/*  59 */   private int hour = 0;
/*     */   
/*  61 */   private int minute = 0;
/*     */   
/*  63 */   private int second = 0;
/*     */   
/*  65 */   private TimeZone timeZone = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int nanoSeconds;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasDate = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasTime = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasTimeZone = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XMPDateTimeImpl() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XMPDateTimeImpl(Calendar calendar) {
/*  96 */     Date date = calendar.getTime();
/*  97 */     TimeZone zone = calendar.getTimeZone();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 102 */     GregorianCalendar intCalendar = (GregorianCalendar)Calendar.getInstance(Locale.US);
/* 103 */     intCalendar.setGregorianChange(new Date(Long.MIN_VALUE));
/* 104 */     intCalendar.setTimeZone(zone);
/* 105 */     intCalendar.setTime(date);
/*     */     
/* 107 */     this.year = intCalendar.get(1);
/* 108 */     this.month = intCalendar.get(2) + 1;
/* 109 */     this.day = intCalendar.get(5);
/* 110 */     this.hour = intCalendar.get(11);
/* 111 */     this.minute = intCalendar.get(12);
/* 112 */     this.second = intCalendar.get(13);
/* 113 */     this.nanoSeconds = intCalendar.get(14) * 1000000;
/* 114 */     this.timeZone = intCalendar.getTimeZone();
/*     */ 
/*     */     
/* 117 */     this.hasDate = this.hasTime = this.hasTimeZone = true;
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
/*     */   public XMPDateTimeImpl(Date date, TimeZone timeZone) {
/* 130 */     GregorianCalendar calendar = new GregorianCalendar(timeZone);
/* 131 */     calendar.setTime(date);
/*     */     
/* 133 */     this.year = calendar.get(1);
/* 134 */     this.month = calendar.get(2) + 1;
/* 135 */     this.day = calendar.get(5);
/* 136 */     this.hour = calendar.get(11);
/* 137 */     this.minute = calendar.get(12);
/* 138 */     this.second = calendar.get(13);
/* 139 */     this.nanoSeconds = calendar.get(14) * 1000000;
/* 140 */     this.timeZone = timeZone;
/*     */ 
/*     */     
/* 143 */     this.hasDate = this.hasTime = this.hasTimeZone = true;
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
/*     */   public XMPDateTimeImpl(String strValue) throws XMPException {
/* 155 */     ISO8601Converter.parse(strValue, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getYear() {
/* 164 */     return this.year;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setYear(int year) {
/* 173 */     this.year = Math.min(Math.abs(year), 9999);
/* 174 */     this.hasDate = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMonth() {
/* 183 */     return this.month;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMonth(int month) {
/* 192 */     if (month < 1) {
/*     */       
/* 194 */       this.month = 1;
/*     */     }
/* 196 */     else if (month > 12) {
/*     */       
/* 198 */       this.month = 12;
/*     */     }
/*     */     else {
/*     */       
/* 202 */       this.month = month;
/*     */     } 
/*     */     
/* 205 */     this.hasDate = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDay() {
/* 214 */     return this.day;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDay(int day) {
/* 223 */     if (day < 1) {
/*     */       
/* 225 */       this.day = 1;
/*     */     }
/* 227 */     else if (day > 31) {
/*     */       
/* 229 */       this.day = 31;
/*     */     }
/*     */     else {
/*     */       
/* 233 */       this.day = day;
/*     */     } 
/*     */     
/* 236 */     this.hasDate = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getHour() {
/* 245 */     return this.hour;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHour(int hour) {
/* 254 */     this.hour = Math.min(Math.abs(hour), 23);
/* 255 */     this.hasTime = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMinute() {
/* 264 */     return this.minute;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMinute(int minute) {
/* 273 */     this.minute = Math.min(Math.abs(minute), 59);
/* 274 */     this.hasTime = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSecond() {
/* 283 */     return this.second;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSecond(int second) {
/* 292 */     this.second = Math.min(Math.abs(second), 59);
/* 293 */     this.hasTime = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNanoSecond() {
/* 302 */     return this.nanoSeconds;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNanoSecond(int nanoSecond) {
/* 311 */     this.nanoSeconds = nanoSecond;
/* 312 */     this.hasTime = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(Object dt) {
/* 322 */     long d = getCalendar().getTimeInMillis() - ((XMPDateTime)dt).getCalendar().getTimeInMillis();
/* 323 */     if (d != 0L)
/*     */     {
/* 325 */       return (int)Math.signum((float)d);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 330 */     d = (this.nanoSeconds - ((XMPDateTime)dt).getNanoSecond());
/* 331 */     return (int)Math.signum((float)d);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeZone getTimeZone() {
/* 341 */     return this.timeZone;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeZone(TimeZone timeZone) {
/* 350 */     this.timeZone = timeZone;
/* 351 */     this.hasTime = true;
/* 352 */     this.hasTimeZone = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasDate() {
/* 361 */     return this.hasDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasTime() {
/* 370 */     return this.hasTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasTimeZone() {
/* 379 */     return this.hasTimeZone;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Calendar getCalendar() {
/* 388 */     GregorianCalendar calendar = (GregorianCalendar)Calendar.getInstance(Locale.US);
/* 389 */     calendar.setGregorianChange(new Date(Long.MIN_VALUE));
/* 390 */     if (this.hasTimeZone)
/*     */     {
/* 392 */       calendar.setTimeZone(this.timeZone);
/*     */     }
/* 394 */     calendar.set(1, this.year);
/* 395 */     calendar.set(2, this.month - 1);
/* 396 */     calendar.set(5, this.day);
/* 397 */     calendar.set(11, this.hour);
/* 398 */     calendar.set(12, this.minute);
/* 399 */     calendar.set(13, this.second);
/* 400 */     calendar.set(14, this.nanoSeconds / 1000000);
/*     */     
/* 402 */     return calendar;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getISO8601String() {
/* 411 */     return ISO8601Converter.render(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 420 */     return getISO8601String();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/xmp/impl/XMPDateTimeImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */