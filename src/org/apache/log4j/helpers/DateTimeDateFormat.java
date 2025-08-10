/*    */ package org.apache.log4j.helpers;
/*    */ 
/*    */ import java.text.DateFormatSymbols;
/*    */ import java.text.FieldPosition;
/*    */ import java.text.ParsePosition;
/*    */ import java.util.Calendar;
/*    */ import java.util.Date;
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
/*    */ public class DateTimeDateFormat
/*    */   extends AbsoluteTimeDateFormat
/*    */ {
/*    */   private static final long serialVersionUID = 5547637772208514971L;
/* 41 */   String[] shortMonths = (new DateFormatSymbols()).getShortMonths();
/*    */ 
/*    */   
/*    */   public DateTimeDateFormat(TimeZone timeZone) {
/* 45 */     this();
/* 46 */     setCalendar(Calendar.getInstance(timeZone));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DateTimeDateFormat() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public StringBuffer format(Date date, StringBuffer sbuf, FieldPosition fieldPosition) {
/* 57 */     this.calendar.setTime(date);
/*    */     
/* 59 */     int day = this.calendar.get(5);
/* 60 */     if (day < 10)
/* 61 */       sbuf.append('0'); 
/* 62 */     sbuf.append(day);
/* 63 */     sbuf.append(' ');
/* 64 */     sbuf.append(this.shortMonths[this.calendar.get(2)]);
/* 65 */     sbuf.append(' ');
/*    */     
/* 67 */     int year = this.calendar.get(1);
/* 68 */     sbuf.append(year);
/* 69 */     sbuf.append(' ');
/*    */     
/* 71 */     return super.format(date, sbuf, fieldPosition);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Date parse(String s, ParsePosition pos) {
/* 78 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/helpers/DateTimeDateFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */