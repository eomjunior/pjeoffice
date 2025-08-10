/*    */ package org.apache.hc.core5.http.protocol;
/*    */ 
/*    */ import java.time.Instant;
/*    */ import java.time.ZoneId;
/*    */ import java.time.format.DateTimeFormatter;
/*    */ import java.time.format.DateTimeFormatterBuilder;
/*    */ import java.util.TimeZone;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
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
/*    */ @Contract(threading = ThreadingBehavior.SAFE)
/*    */ public class HttpDateGenerator
/*    */ {
/*    */   private static final int GRANULARITY_MILLIS = 1000;
/*    */   public static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";
/*    */   @Deprecated
/* 57 */   public static final TimeZone GMT = TimeZone.getTimeZone("GMT");
/*    */   
/* 59 */   public static final ZoneId GMT_ID = ZoneId.of("GMT");
/*    */ 
/*    */   
/* 62 */   public static final HttpDateGenerator INSTANCE = new HttpDateGenerator("EEE, dd MMM yyyy HH:mm:ss zzz", GMT_ID);
/*    */   
/*    */   private final DateTimeFormatter dateTimeFormatter;
/*    */   private long dateAsMillis;
/*    */   private String dateAsText;
/*    */   private ZoneId zoneId;
/*    */   
/*    */   HttpDateGenerator() {
/* 70 */     this
/*    */ 
/*    */ 
/*    */       
/* 74 */       .dateTimeFormatter = (new DateTimeFormatterBuilder()).parseLenient().parseCaseInsensitive().appendPattern("EEE, dd MMM yyyy HH:mm:ss zzz").toFormatter();
/* 75 */     this.zoneId = GMT_ID;
/*    */   }
/*    */ 
/*    */   
/*    */   private HttpDateGenerator(String pattern, ZoneId zoneId) {
/* 80 */     this
/*    */ 
/*    */ 
/*    */       
/* 84 */       .dateTimeFormatter = (new DateTimeFormatterBuilder()).parseLenient().parseCaseInsensitive().appendPattern(pattern).toFormatter();
/* 85 */     this.zoneId = zoneId;
/*    */   }
/*    */   
/*    */   public synchronized String getCurrentDate() {
/* 89 */     long now = System.currentTimeMillis();
/* 90 */     if (now - this.dateAsMillis > 1000L) {
/*    */       
/* 92 */       this.dateAsText = this.dateTimeFormatter.format(Instant.now().atZone(this.zoneId));
/* 93 */       this.dateAsMillis = now;
/*    */     } 
/* 95 */     return this.dateAsText;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/protocol/HttpDateGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */