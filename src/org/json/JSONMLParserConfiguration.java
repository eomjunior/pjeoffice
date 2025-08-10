/*    */ package org.json;
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
/*    */ public class JSONMLParserConfiguration
/*    */   extends ParserConfiguration
/*    */ {
/*    */   public static final int DEFAULT_MAXIMUM_NESTING_DEPTH = 512;
/* 18 */   public static final JSONMLParserConfiguration ORIGINAL = new JSONMLParserConfiguration();
/*    */ 
/*    */   
/* 21 */   public static final JSONMLParserConfiguration KEEP_STRINGS = (new JSONMLParserConfiguration())
/* 22 */     .withKeepStrings(true);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JSONMLParserConfiguration() {
/* 29 */     this.maxNestingDepth = 512;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected JSONMLParserConfiguration(boolean keepStrings, int maxNestingDepth) {
/* 39 */     super(keepStrings, maxNestingDepth);
/*    */   }
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
/*    */   protected JSONMLParserConfiguration clone() {
/* 52 */     return new JSONMLParserConfiguration(this.keepStrings, this.maxNestingDepth);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JSONMLParserConfiguration withKeepStrings(boolean newVal) {
/* 60 */     return super.<JSONMLParserConfiguration>withKeepStrings(newVal);
/*    */   }
/*    */ 
/*    */   
/*    */   public JSONMLParserConfiguration withMaxNestingDepth(int maxNestingDepth) {
/* 65 */     return super.<JSONMLParserConfiguration>withMaxNestingDepth(maxNestingDepth);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/json/JSONMLParserConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */