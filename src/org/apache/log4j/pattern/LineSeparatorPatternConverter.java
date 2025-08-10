/*    */ package org.apache.log4j.pattern;
/*    */ 
/*    */ import org.apache.log4j.Layout;
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
/*    */ public final class LineSeparatorPatternConverter
/*    */   extends LoggingEventPatternConverter
/*    */ {
/* 32 */   private static final LineSeparatorPatternConverter INSTANCE = new LineSeparatorPatternConverter();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private final String lineSep;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private LineSeparatorPatternConverter() {
/* 43 */     super("Line Sep", "lineSep");
/* 44 */     this.lineSep = Layout.LINE_SEP;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static LineSeparatorPatternConverter newInstance(String[] options) {
/* 54 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LoggingEvent event, StringBuffer toAppendTo) {
/* 61 */     toAppendTo.append(this.lineSep);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(Object obj, StringBuffer toAppendTo) {
/* 68 */     toAppendTo.append(this.lineSep);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/pattern/LineSeparatorPatternConverter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */