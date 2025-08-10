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
/*    */ public class TaskExecutorException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public TaskExecutorException(String message) {
/* 34 */     super(message);
/*    */   }
/*    */   
/*    */   public TaskExecutorException(String message, Throwable e) {
/* 38 */     super(message, e);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/taskresolver4j/exception/TaskExecutorException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */