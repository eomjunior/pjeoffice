/*    */ package org.apache.tools.ant.filters;
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
/*    */ public class UniqFilter
/*    */   extends TokenFilter.ChainableReaderFilter
/*    */ {
/* 31 */   private String lastLine = null;
/*    */   
/*    */   public String filter(String string) {
/* 34 */     if (this.lastLine == null || !this.lastLine.equals(string)) {
/* 35 */       this.lastLine = string;
/* 36 */       return this.lastLine;
/*    */     } 
/* 38 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/filters/UniqFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */