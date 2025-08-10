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
/*    */ 
/*    */ public class TaskExecutorDiscartingException
/*    */   extends TaskExecutorException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private static final String DEFAULT_MESSAGE = "Descarte de requisição obsoleta!";
/*    */   
/*    */   public TaskExecutorDiscartingException() {
/* 36 */     super("Descarte de requisição obsoleta!");
/*    */   }
/*    */   
/*    */   public TaskExecutorDiscartingException(Throwable cause) {
/* 40 */     super("Descarte de requisição obsoleta!", cause);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/taskresolver4j/exception/TaskExecutorDiscartingException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */