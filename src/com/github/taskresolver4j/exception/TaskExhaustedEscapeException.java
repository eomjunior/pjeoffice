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
/*    */ public class TaskExhaustedEscapeException
/*    */   extends TaskEscapeException
/*    */ {
/*    */   private static final long serialVersionUID = 6429982826070354933L;
/*    */   
/*    */   public TaskExhaustedEscapeException(Exception cause) {
/* 35 */     super("Exhausted escape exception", cause);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/taskresolver4j/exception/TaskExhaustedEscapeException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */