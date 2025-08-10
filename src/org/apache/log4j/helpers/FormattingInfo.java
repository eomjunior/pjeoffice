/*    */ package org.apache.log4j.helpers;
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
/*    */ public class FormattingInfo
/*    */ {
/* 30 */   int min = -1;
/* 31 */   int max = Integer.MAX_VALUE;
/*    */   boolean leftAlign = false;
/*    */   
/*    */   void reset() {
/* 35 */     this.min = -1;
/* 36 */     this.max = Integer.MAX_VALUE;
/* 37 */     this.leftAlign = false;
/*    */   }
/*    */   
/*    */   void dump() {
/* 41 */     LogLog.debug("min=" + this.min + ", max=" + this.max + ", leftAlign=" + this.leftAlign);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/helpers/FormattingInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */