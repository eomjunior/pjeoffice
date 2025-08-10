/*    */ package org.apache.log4j.pattern;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class LevelPatternConverter
/*    */   extends LoggingEventPatternConverter
/*    */ {
/*    */   private static final int TRACE_INT = 5000;
/* 37 */   private static final LevelPatternConverter INSTANCE = new LevelPatternConverter();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private LevelPatternConverter() {
/* 43 */     super("Level", "level");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static LevelPatternConverter newInstance(String[] options) {
/* 53 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LoggingEvent event, StringBuffer output) {
/* 60 */     output.append(event.getLevel().toString());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getStyleClass(Object e) {
/* 67 */     if (e instanceof LoggingEvent) {
/* 68 */       int lint = ((LoggingEvent)e).getLevel().toInt();
/*    */       
/* 70 */       switch (lint) {
/*    */         case 5000:
/* 72 */           return "level trace";
/*    */         
/*    */         case 10000:
/* 75 */           return "level debug";
/*    */         
/*    */         case 20000:
/* 78 */           return "level info";
/*    */         
/*    */         case 30000:
/* 81 */           return "level warn";
/*    */         
/*    */         case 40000:
/* 84 */           return "level error";
/*    */         
/*    */         case 50000:
/* 87 */           return "level fatal";
/*    */       } 
/*    */       
/* 90 */       return "level " + ((LoggingEvent)e).getLevel().toString();
/*    */     } 
/*    */ 
/*    */     
/* 94 */     return "level";
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/pattern/LevelPatternConverter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */