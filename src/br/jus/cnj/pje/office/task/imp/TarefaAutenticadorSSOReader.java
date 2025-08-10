/*    */ package br.jus.cnj.pje.office.task.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.task.ITarefaAutenticador;
/*    */ import com.github.taskresolver4j.ITask;
/*    */ import com.github.utils4j.imp.Params;
/*    */ import java.io.IOException;
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
/*    */ class TarefaAutenticadorSSOReader
/*    */   extends TarefaAutenticadorReader
/*    */ {
/* 39 */   static final TarefaAutenticadorSSOReader INSTANCE = new TarefaAutenticadorSSOReader();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected ITask<?> createTask(Params output, ITarefaAutenticador pojo) throws IOException {
/* 46 */     return (ITask<?>)new PjeSSOAuthenticatorTask(output, pojo);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/TarefaAutenticadorSSOReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */