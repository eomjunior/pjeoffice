/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import java.time.Duration;
/*    */ import java.util.Optional;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DurationTools
/*    */ {
/* 15 */   public static final Duration ONE_HOUR = Duration.ofHours(1L);
/* 16 */   public static final Duration ONE_MINUTE = Duration.ofMinutes(1L);
/* 17 */   public static final Duration ONE_SECOND = Duration.ofSeconds(1L);
/* 18 */   public static final Duration ONE_MILLISECOND = Duration.ofMillis(1L);
/*    */ 
/*    */ 
/*    */   
/*    */   public static String stringNow() {
/* 23 */     return toHmsString(System.currentTimeMillis());
/*    */   }
/*    */   
/*    */   public static String toString(long timeMillis) {
/* 27 */     return toString(timeMillis, ':', ':');
/*    */   }
/*    */   
/*    */   public static String toHmsString(long timeMillis) {
/* 31 */     return toString(timeMillis, 'h', 'm') + 's';
/*    */   }
/*    */   
/*    */   public static String toString(long timeMillis, char hseparator, char mseparator) {
/* 35 */     Args.requireZeroPositive(timeMillis, "timiMillis < 0");
/* 36 */     long div = timeMillis / ONE_HOUR.toMillis();
/* 37 */     long mod = timeMillis % ONE_HOUR.toMillis();
/* 38 */     if (mod == 0L) {
/* 39 */       return Strings.padStart(div, 2) + hseparator + "00" + mseparator + "00";
/*    */     }
/* 41 */     long hours = div;
/* 42 */     timeMillis -= hours * ONE_HOUR.toMillis();
/* 43 */     div = timeMillis / ONE_MINUTE.toMillis();
/* 44 */     mod = timeMillis % ONE_MINUTE.toMillis();
/* 45 */     if (mod == 0L) {
/* 46 */       return Strings.padStart(hours, 2) + hseparator + Strings.padStart(div, 2) + mseparator + "00";
/*    */     }
/* 48 */     long minutes = div;
/* 49 */     timeMillis -= minutes * ONE_MINUTE.toMillis();
/* 50 */     div = timeMillis / ONE_SECOND.toMillis();
/* 51 */     return Strings.padStart(hours, 2) + hseparator + Strings.padStart(minutes, 2) + mseparator + Strings.padStart(div, 2);
/*    */   }
/*    */   
/*    */   public static Optional<Duration> parse(String duration) {
/* 55 */     if (!Strings.hasText(duration))
/* 56 */       return Optional.empty(); 
/* 57 */     int length = duration.length();
/* 58 */     int start = 0;
/* 59 */     int next = duration.indexOf(':');
/* 60 */     if (next <= 0)
/* 61 */       return Optional.empty(); 
/* 62 */     int hour = Strings.toInt(duration.substring(start, next), -1);
/* 63 */     if (hour < 0)
/* 64 */       return Optional.empty(); 
/* 65 */     Duration h = Duration.ofHours(hour);
/* 66 */     start = next + 1;
/* 67 */     if (start >= length)
/* 68 */       return Optional.empty(); 
/* 69 */     next = duration.indexOf(':', start);
/* 70 */     if (next < 0)
/* 71 */       return Optional.empty(); 
/* 72 */     int minutes = Strings.toInt(duration.substring(start, next), -1);
/* 73 */     if (minutes < 0)
/* 74 */       return Optional.empty(); 
/* 75 */     Duration m = Duration.ofMinutes(minutes);
/* 76 */     start = next + 1;
/* 77 */     if (start >= length)
/* 78 */       return Optional.empty(); 
/* 79 */     int sec = Strings.toInt(duration.substring(start), -1);
/* 80 */     if (sec < 0)
/* 81 */       return Optional.empty(); 
/* 82 */     Duration s = Duration.ofSeconds(sec);
/* 83 */     return Optional.of(Duration.ofMillis(h.toMillis() + m.toMillis() + s.toMillis()));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/DurationTools.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */