/*     */ package org.apache.log4j.pattern;
/*     */ 
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
/*     */ public class RelativeTimePatternConverter
/*     */   extends LoggingEventPatternConverter
/*     */ {
/*  32 */   private CachedTimestamp lastTimestamp = new CachedTimestamp(0L, "");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RelativeTimePatternConverter() {
/*  38 */     super("Time", "time");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RelativeTimePatternConverter newInstance(String[] options) {
/*  48 */     return new RelativeTimePatternConverter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(LoggingEvent event, StringBuffer toAppendTo) {
/*  55 */     long timestamp = event.timeStamp;
/*     */     
/*  57 */     if (!this.lastTimestamp.format(timestamp, toAppendTo)) {
/*  58 */       String formatted = Long.toString(timestamp - LoggingEvent.getStartTime());
/*  59 */       toAppendTo.append(formatted);
/*  60 */       this.lastTimestamp = new CachedTimestamp(timestamp, formatted);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class CachedTimestamp
/*     */   {
/*     */     private final long timestamp;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String formatted;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CachedTimestamp(long timestamp, String formatted) {
/*  85 */       this.timestamp = timestamp;
/*  86 */       this.formatted = formatted;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean format(long newTimestamp, StringBuffer toAppendTo) {
/*  97 */       if (newTimestamp == this.timestamp) {
/*  98 */         toAppendTo.append(this.formatted);
/*     */         
/* 100 */         return true;
/*     */       } 
/*     */       
/* 103 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/pattern/RelativeTimePatternConverter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */