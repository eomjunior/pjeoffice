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
/*    */ public class TaskDiscardException
/*    */   extends TaskException
/*    */ {
/*    */   private static final long serialVersionUID = 5036913751425298195L;
/*    */   
/*    */   public TaskDiscardException(String message) {
/* 34 */     super(message);
/*    */   }
/*    */   
/*    */   public TaskDiscardException(String message, Exception cause) {
/* 38 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/taskresolver4j/exception/TaskDiscardException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */