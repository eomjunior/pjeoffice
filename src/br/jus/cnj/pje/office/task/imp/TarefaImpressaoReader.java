/*    */ package br.jus.cnj.pje.office.task.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.task.ITarefaImpressao;
/*    */ import com.github.taskresolver4j.ITask;
/*    */ import com.github.taskresolver4j.imp.RequestReader;
/*    */ import com.github.utils4j.imp.Params;
/*    */ import com.github.utils4j.imp.Strings;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
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
/*    */ 
/*    */ final class TarefaImpressaoReader
/*    */   extends RequestReader<Params, ITarefaImpressao>
/*    */ {
/* 47 */   static final TarefaImpressaoReader INSTANCE = new TarefaImpressaoReader();
/*    */   
/*    */   protected static final class TarefaImpressao implements ITarefaImpressao {
/* 50 */     private List<String> conteudo = new ArrayList<>();
/*    */     
/* 52 */     private String porta = "LPT1";
/*    */ 
/*    */     
/*    */     public final List<String> getConteudo() {
/* 56 */       return (this.conteudo == null) ? Collections.<String>emptyList() : Collections.<String>unmodifiableList(this.conteudo);
/*    */     }
/*    */ 
/*    */     
/*    */     public Optional<String> getPorta() {
/* 61 */       return Strings.optional(this.porta);
/*    */     }
/*    */   }
/*    */   
/*    */   private TarefaImpressaoReader() {
/* 66 */     super(TarefaImpressao.class);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ITask<?> createTask(Params output, ITarefaImpressao pojo) throws IOException {
/* 71 */     return (ITask<?>)new PjePrintingTask(output, pojo);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/TarefaImpressaoReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */