/*    */ package org.apache.log4j.pattern;
/*    */ 
/*    */ import org.apache.log4j.spi.LocationInfo;
/*    */ import org.apache.log4j.spi.LoggingEvent;
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
/*    */ public final class FullLocationPatternConverter
/*    */   extends LoggingEventPatternConverter
/*    */ {
/* 32 */   private static final FullLocationPatternConverter INSTANCE = new FullLocationPatternConverter();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private FullLocationPatternConverter() {
/* 38 */     super("Full Location", "fullLocation");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static FullLocationPatternConverter newInstance(String[] options) {
/* 48 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LoggingEvent event, StringBuffer output) {
/* 55 */     LocationInfo locationInfo = event.getLocationInformation();
/*    */     
/* 57 */     if (locationInfo != null)
/* 58 */       output.append(locationInfo.fullInfo); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/pattern/FullLocationPatternConverter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */