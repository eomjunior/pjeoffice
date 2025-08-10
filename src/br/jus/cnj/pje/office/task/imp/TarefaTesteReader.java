/*    */ package br.jus.cnj.pje.office.task.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.task.ITarefaTeste;
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
/*    */ final class TarefaTesteReader
/*    */   extends RequestReader<Params, ITarefaTeste>
/*    */ {
/* 42 */   static final TarefaTesteReader INSTANCE = new TarefaTesteReader();
/*    */   
/*    */   static final class TarefaTeste
/*    */     implements ITarefaTeste {
/*    */     private String message;
/*    */     
/*    */     public Optional<String> getMessage() {
/* 49 */       return Strings.optional(this.message);
/*    */     }
/*    */   }
/*    */   
/*    */   private TarefaTesteReader() {
/* 54 */     super(TarefaTeste.class);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ITask<?> createTask(Params params, ITarefaTeste pojo) throws IOException {
/* 59 */     return (ITask<?>)new PjeTestTask(params, pojo);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/TarefaTesteReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */