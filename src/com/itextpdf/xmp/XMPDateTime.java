package com.itextpdf.xmp;

import java.util.Calendar;
import java.util.TimeZone;

public interface XMPDateTime extends Comparable {
  int getYear();
  
  void setYear(int paramInt);
  
  int getMonth();
  
  void setMonth(int paramInt);
  
  int getDay();
  
  void setDay(int paramInt);
  
  int getHour();
  
  void setHour(int paramInt);
  
  int getMinute();
  
  void setMinute(int paramInt);
  
  int getSecond();
  
  void setSecond(int paramInt);
  
  int getNanoSecond();
  
  void setNanoSecond(int paramInt);
  
  TimeZone getTimeZone();
  
  void setTimeZone(TimeZone paramTimeZone);
  
  boolean hasDate();
  
  boolean hasTime();
  
  boolean hasTimeZone();
  
  Calendar getCalendar();
  
  String getISO8601String();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/xmp/XMPDateTime.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */