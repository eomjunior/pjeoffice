/*    */ package br.jus.cnj.pje.office.task.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.task.ITarefaPdfDivisaoContagem;
/*    */ import com.github.taskresolver4j.ITask;
/*    */ import com.github.utils4j.imp.Params;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ final class TarefaPdfDivisaoContagemReader
/*    */   extends TarefaMediaReader<ITarefaPdfDivisaoContagem>
/*    */ {
/* 45 */   static final TarefaPdfDivisaoContagemReader INSTANCE = new TarefaPdfDivisaoContagemReader();
/*    */   
/*    */   protected static final class TarefaPdfDivisaoContagem
/*    */     extends TarefaMediaReader.TarefaMedia implements ITarefaPdfDivisaoContagem {
/*    */     private long totalPaginas;
/*    */     
/*    */     public long getTotalPaginas() {
/* 52 */       return this.totalPaginas;
/*    */     }
/*    */   }
/*    */   
/*    */   private TarefaPdfDivisaoContagemReader() {
/* 57 */     super(TarefaPdfDivisaoContagem.class);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ITask<?> createTask(Params output, ITarefaPdfDivisaoContagem pojo) throws IOException {
/* 62 */     return (ITask<?>)new PjeByCountPdfSplitterTask(output, pojo);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getTarefaId() {
/* 67 */     return PjeTaskReader.PDF_SPLIT_BY_COUNT.getId();
/*    */   }
/*    */ 
/*    */   
/*    */   protected ITarefaPdfDivisaoContagem getTarefa(Params param) {
/* 72 */     TarefaPdfDivisaoContagem tarefaTamanho = new TarefaPdfDivisaoContagem();
/* 73 */     tarefaTamanho.totalPaginas = Long.parseLong((String)param.getValue("totalPaginas"));
/* 74 */     tarefaTamanho.arquivos = (List<String>)param.getValue("arquivos");
/* 75 */     return tarefaTamanho;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/TarefaPdfDivisaoContagemReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */