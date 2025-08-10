/*    */ package br.jus.cnj.pje.office.task.imp;
/*    */ 
/*    */ import com.github.taskresolver4j.exception.TaskException;
/*    */ 
/*    */ public class TaskExhaustedConnectionException
/*    */   extends TaskException {
/*    */   private static final long serialVersionUID = -1446350763332784117L;
/*    */   
/*    */   public TaskExhaustedConnectionException(Throwable cause) {
/* 10 */     super("Conexão limitada", cause);
/*    */   }
/*    */   
/*    */   public TaskExhaustedConnectionException(String message, Throwable cause) {
/* 14 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/TaskExhaustedConnectionException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */