/*    */ package org.slf4j.helpers;
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
/*    */ 
/*    */ public class FormattingTuple
/*    */ {
/* 34 */   public static FormattingTuple NULL = new FormattingTuple(null);
/*    */   
/*    */   private final String message;
/*    */   private final Throwable throwable;
/*    */   private final Object[] argArray;
/*    */   
/*    */   public FormattingTuple(String message) {
/* 41 */     this(message, null, null);
/*    */   }
/*    */   
/*    */   public FormattingTuple(String message, Object[] argArray, Throwable throwable) {
/* 45 */     this.message = message;
/* 46 */     this.throwable = throwable;
/* 47 */     this.argArray = argArray;
/*    */   }
/*    */   
/*    */   public String getMessage() {
/* 51 */     return this.message;
/*    */   }
/*    */   
/*    */   public Object[] getArgArray() {
/* 55 */     return this.argArray;
/*    */   }
/*    */   
/*    */   public Throwable getThrowable() {
/* 59 */     return this.throwable;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/slf4j/helpers/FormattingTuple.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */