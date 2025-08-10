/*    */ package com.github.utils4j;
/*    */ 
/*    */ public interface IResultChecker {
/*    */   public static final IResultChecker NOTHING = (r, code) -> {
/*    */     
/*    */     };
/*    */   
/*    */   void handle(String paramString, int paramInt) throws Exception;
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/IResultChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */