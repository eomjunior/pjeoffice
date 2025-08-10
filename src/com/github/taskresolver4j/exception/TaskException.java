/*    */ package com.github.taskresolver4j.exception;
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
/*    */ public class TaskException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 5036913751425298195L;
/*    */   
/*    */   public TaskException(Exception cause) {
/* 34 */     super(cause);
/*    */   }
/*    */   
/*    */   public TaskException(String message) {
/* 38 */     super(message);
/*    */   }
/*    */   
/*    */   public TaskException(String message, Throwable cause) {
/* 42 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/taskresolver4j/exception/TaskException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */