/*    */ package br.jus.cnj.pje.office.task.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.task.ITarefaVideoDivisaoTamanho;
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
/*    */ 
/*    */ final class TarefaVideoDivisaoTamanhoReader
/*    */   extends TarefaMediaReader<ITarefaVideoDivisaoTamanho>
/*    */ {
/* 46 */   static final TarefaVideoDivisaoTamanhoReader INSTANCE = new TarefaVideoDivisaoTamanhoReader();
/*    */   
/*    */   protected static final class TarefaVideoDivisaoTamanho extends TarefaMediaReader.TarefaMedia implements ITarefaVideoDivisaoTamanho {
/*    */     private double tamanho;
/*    */     
/*    */     public final double getTamanho() {
/* 52 */       return this.tamanho;
/*    */     }
/*    */   }
/*    */   
/*    */   private TarefaVideoDivisaoTamanhoReader() {
/* 57 */     super(TarefaVideoDivisaoTamanho.class);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ITask<?> createTask(Params output, ITarefaVideoDivisaoTamanho pojo) throws IOException {
/* 62 */     return (ITask<?>)new PjeBySizeVideoSplitterTask(output, pojo);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getTarefaId() {
/* 67 */     return PjeTaskReader.VIDEO_SPLIT_BY_SIZE.getId();
/*    */   }
/*    */ 
/*    */   
/*    */   protected ITarefaVideoDivisaoTamanho getTarefa(Params param) {
/* 72 */     TarefaVideoDivisaoTamanho tarefaTamanho = new TarefaVideoDivisaoTamanho();
/* 73 */     tarefaTamanho.tamanho = Double.parseDouble((String)param.getValue("tamanho"));
/* 74 */     tarefaTamanho.arquivos = (List<String>)param.getValue("arquivos");
/* 75 */     return tarefaTamanho;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/TarefaVideoDivisaoTamanhoReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */