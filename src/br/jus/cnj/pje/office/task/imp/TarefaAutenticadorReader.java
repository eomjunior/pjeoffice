/*    */ package br.jus.cnj.pje.office.task.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.task.ITarefaAutenticador;
/*    */ import com.github.taskresolver4j.ITask;
/*    */ import com.github.taskresolver4j.imp.RequestReader;
/*    */ import com.github.utils4j.imp.Params;
/*    */ import com.github.utils4j.imp.Strings;
/*    */ import java.io.IOException;
/*    */ import java.util.Optional;
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
/*    */ class TarefaAutenticadorReader
/*    */   extends RequestReader<Params, ITarefaAutenticador>
/*    */ {
/* 43 */   static final TarefaAutenticadorReader INSTANCE = new TarefaAutenticadorReader();
/*    */   
/*    */   static final class TarefaAutenticador
/*    */     implements ITarefaAutenticador {
/*    */     private String enviarPara;
/*    */     private String mensagem;
/*    */     private String token;
/*    */     private String algoritmoAssinatura;
/*    */     
/*    */     public final Optional<String> getAlgoritmoAssinatura() {
/* 53 */       return Strings.optional(this.algoritmoAssinatura);
/*    */     }
/*    */ 
/*    */     
/*    */     public final Optional<String> getEnviarPara() {
/* 58 */       return Strings.optional(this.enviarPara);
/*    */     }
/*    */ 
/*    */     
/*    */     public final Optional<String> getMensagem() {
/* 63 */       return Strings.optional(this.mensagem);
/*    */     }
/*    */ 
/*    */     
/*    */     public final Optional<String> getToken() {
/* 68 */       return Strings.optional(this.token);
/*    */     }
/*    */   }
/*    */   
/*    */   protected TarefaAutenticadorReader() {
/* 73 */     super(TarefaAutenticador.class);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ITask<?> createTask(Params output, ITarefaAutenticador pojo) throws IOException {
/* 78 */     return (ITask<?>)new PjeAuthenticatorTask(output, pojo);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/TarefaAutenticadorReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */