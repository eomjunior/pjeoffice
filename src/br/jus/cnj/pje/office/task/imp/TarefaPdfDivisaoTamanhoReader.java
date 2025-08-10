/*    */ package br.jus.cnj.pje.office.task.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.task.ITarefaPdfDivisaoTamanho;
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
/*    */ final class TarefaPdfDivisaoTamanhoReader
/*    */   extends TarefaMediaReader<ITarefaPdfDivisaoTamanho>
/*    */ {
/* 45 */   static final TarefaPdfDivisaoTamanhoReader INSTANCE = new TarefaPdfDivisaoTamanhoReader();
/*    */   
/*    */   protected static final class TarefaPdfDivisaoTamanho extends TarefaMediaReader.TarefaMedia implements ITarefaPdfDivisaoTamanho {
/*    */     private double tamanho;
/*    */     
/*    */     public final double getTamanho() {
/* 51 */       return this.tamanho;
/*    */     }
/*    */   }
/*    */   
/*    */   private TarefaPdfDivisaoTamanhoReader() {
/* 56 */     super(TarefaPdfDivisaoTamanho.class);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ITask<?> createTask(Params output, ITarefaPdfDivisaoTamanho pojo) throws IOException {
/* 61 */     return (ITask<?>)new PjeBySizePdfSplitterTask(output, pojo);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getTarefaId() {
/* 66 */     return PjeTaskReader.PDF_SPLIT_BY_SIZE.getId();
/*    */   }
/*    */ 
/*    */   
/*    */   protected ITarefaPdfDivisaoTamanho getTarefa(Params param) {
/* 71 */     TarefaPdfDivisaoTamanho tarefaTamanho = new TarefaPdfDivisaoTamanho();
/* 72 */     tarefaTamanho.tamanho = Double.parseDouble((String)param.getValue("tamanho"));
/* 73 */     tarefaTamanho.arquivos = (List<String>)param.getValue("arquivos");
/* 74 */     return tarefaTamanho;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/TarefaPdfDivisaoTamanhoReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */