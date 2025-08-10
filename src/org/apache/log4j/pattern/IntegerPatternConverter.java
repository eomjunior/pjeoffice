/*    */ package org.apache.log4j.pattern;
/*    */ 
/*    */ import java.util.Date;
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
/*    */ public final class IntegerPatternConverter
/*    */   extends PatternConverter
/*    */ {
/* 31 */   private static final IntegerPatternConverter INSTANCE = new IntegerPatternConverter();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private IntegerPatternConverter() {
/* 37 */     super("Integer", "integer");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static IntegerPatternConverter newInstance(String[] options) {
/* 47 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(Object obj, StringBuffer toAppendTo) {
/* 54 */     if (obj instanceof Integer) {
/* 55 */       toAppendTo.append(obj.toString());
/*    */     }
/*    */     
/* 58 */     if (obj instanceof Date)
/* 59 */       toAppendTo.append(Long.toString(((Date)obj).getTime())); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/pattern/IntegerPatternConverter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */