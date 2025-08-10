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
/*    */ public class SequenceNumberPatternConverter
/*    */   extends LoggingEventPatternConverter
/*    */ {
/* 31 */   private static final SequenceNumberPatternConverter INSTANCE = new SequenceNumberPatternConverter();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private SequenceNumberPatternConverter() {
/* 37 */     super("Sequence Number", "sn");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static SequenceNumberPatternConverter newInstance(String[] options) {
/* 47 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LoggingEvent event, StringBuffer toAppendTo) {
/* 54 */     toAppendTo.append("0");
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/pattern/SequenceNumberPatternConverter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */