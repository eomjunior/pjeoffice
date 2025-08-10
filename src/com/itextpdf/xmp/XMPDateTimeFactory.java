/*     */ package com.itextpdf.xmp;
/*     */ 
/*     */ import com.itextpdf.xmp.impl.XMPDateTimeImpl;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
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
/*     */ public final class XMPDateTimeFactory
/*     */ {
/*  50 */   private static final TimeZone UTC = TimeZone.getTimeZone("UTC");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static XMPDateTime createFromCalendar(Calendar calendar) {
/*  69 */     return (XMPDateTime)new XMPDateTimeImpl(calendar);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static XMPDateTime create() {
/*  79 */     return (XMPDateTime)new XMPDateTimeImpl();
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
/*     */   public static XMPDateTime create(int year, int month, int day) {
/*  93 */     XMPDateTimeImpl xMPDateTimeImpl = new XMPDateTimeImpl();
/*  94 */     xMPDateTimeImpl.setYear(year);
/*  95 */     xMPDateTimeImpl.setMonth(month);
/*  96 */     xMPDateTimeImpl.setDay(day);
/*  97 */     return (XMPDateTime)xMPDateTimeImpl;
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
/*     */   public static XMPDateTime create(int year, int month, int day, int hour, int minute, int second, int nanoSecond) {
/* 116 */     XMPDateTimeImpl xMPDateTimeImpl = new XMPDateTimeImpl();
/* 117 */     xMPDateTimeImpl.setYear(year);
/* 118 */     xMPDateTimeImpl.setMonth(month);
/* 119 */     xMPDateTimeImpl.setDay(day);
/* 120 */     xMPDateTimeImpl.setHour(hour);
/* 121 */     xMPDateTimeImpl.setMinute(minute);
/* 122 */     xMPDateTimeImpl.setSecond(second);
/* 123 */     xMPDateTimeImpl.setNanoSecond(nanoSecond);
/* 124 */     return (XMPDateTime)xMPDateTimeImpl;
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
/*     */   public static XMPDateTime createFromISO8601(String strValue) throws XMPException {
/* 137 */     return (XMPDateTime)new XMPDateTimeImpl(strValue);
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
/*     */   public static XMPDateTime getCurrentDateTime() {
/* 149 */     return (XMPDateTime)new XMPDateTimeImpl(new GregorianCalendar());
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
/*     */   public static XMPDateTime setLocalTimeZone(XMPDateTime dateTime) {
/* 162 */     Calendar cal = dateTime.getCalendar();
/* 163 */     cal.setTimeZone(TimeZone.getDefault());
/* 164 */     return (XMPDateTime)new XMPDateTimeImpl(cal);
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
/*     */   public static XMPDateTime convertToUTCTime(XMPDateTime dateTime) {
/* 179 */     long timeInMillis = dateTime.getCalendar().getTimeInMillis();
/* 180 */     GregorianCalendar cal = new GregorianCalendar(UTC);
/* 181 */     cal.setGregorianChange(new Date(Long.MIN_VALUE));
/* 182 */     cal.setTimeInMillis(timeInMillis);
/* 183 */     return (XMPDateTime)new XMPDateTimeImpl(cal);
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
/*     */   public static XMPDateTime convertToLocalTime(XMPDateTime dateTime) {
/* 196 */     long timeInMillis = dateTime.getCalendar().getTimeInMillis();
/*     */     
/* 198 */     GregorianCalendar cal = new GregorianCalendar();
/* 199 */     cal.setTimeInMillis(timeInMillis);
/* 200 */     return (XMPDateTime)new XMPDateTimeImpl(cal);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/xmp/XMPDateTimeFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */