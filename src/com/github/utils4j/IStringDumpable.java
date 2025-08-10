/*    */ package com.github.utils4j;
/*    */ 
/*    */ import com.github.utils4j.imp.Strings;
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
/*    */ public interface IStringDumpable
/*    */ {
/*    */   static IStringDumpable from(Object a) {
/* 34 */     return () -> Strings.trim(a);
/*    */   }
/*    */   
/*    */   String dump();
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/IStringDumpable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */