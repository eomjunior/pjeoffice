/*    */ package br.jus.cnj.pje.office.task.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.task.ITarefaPdfDivisaoParidade;
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
/*    */ final class TarefaPdfDivisaoParidadeReader
/*    */   extends TarefaMediaReader<ITarefaPdfDivisaoParidade>
/*    */ {
/* 45 */   static final TarefaPdfDivisaoParidadeReader INSTANCE = new TarefaPdfDivisaoParidadeReader();
/*    */   
/*    */   protected static final class TarefaPdfDivisaoParidade
/*    */     extends TarefaMediaReader.TarefaMedia implements ITarefaPdfDivisaoParidade {
/*    */     private boolean paridade;
/*    */     
/*    */     public boolean isParidade() {
/* 52 */       return this.paridade;
/*    */     }
/*    */   }
/*    */   
/*    */   private TarefaPdfDivisaoParidadeReader() {
/* 57 */     super(TarefaPdfDivisaoParidade.class);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ITask<?> createTask(Params output, ITarefaPdfDivisaoParidade pojo) throws IOException {
/* 62 */     return (ITask<?>)new PjeByParityPdfSplitterTask(output, pojo);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getTarefaId() {
/* 67 */     return PjeTaskReader.PDF_SPLIT_BY_PARITY.getId();
/*    */   }
/*    */ 
/*    */   
/*    */   protected ITarefaPdfDivisaoParidade getTarefa(Params param) {
/* 72 */     TarefaPdfDivisaoParidade tarefaTamanho = new TarefaPdfDivisaoParidade();
/* 73 */     tarefaTamanho.paridade = Boolean.parseBoolean((String)param.getValue("paridade"));
/* 74 */     tarefaTamanho.arquivos = (List<String>)param.getValue("arquivos");
/* 75 */     return tarefaTamanho;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/TarefaPdfDivisaoParidadeReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */