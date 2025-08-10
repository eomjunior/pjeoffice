/*    */ package org.apache.tools.ant.filters;
/*    */ 
/*    */ import org.apache.tools.ant.util.Native2AsciiUtils;
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
/*    */ public class Native2AsciiFilter
/*    */   extends TokenFilter.ChainableReaderFilter
/*    */ {
/*    */   private boolean reverse;
/*    */   
/*    */   public void setReverse(boolean reverse) {
/* 39 */     this.reverse = reverse;
/*    */   }
/*    */ 
/*    */   
/*    */   public String filter(String line) {
/* 44 */     return this.reverse ? 
/* 45 */       Native2AsciiUtils.ascii2native(line) : 
/* 46 */       Native2AsciiUtils.native2ascii(line);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/filters/Native2AsciiFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */