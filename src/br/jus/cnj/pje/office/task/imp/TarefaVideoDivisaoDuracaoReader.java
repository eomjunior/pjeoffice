/*    */ package br.jus.cnj.pje.office.task.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.task.ITarefaVideoDivisaoDuracao;
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
/*    */ final class TarefaVideoDivisaoDuracaoReader
/*    */   extends TarefaMediaReader<ITarefaVideoDivisaoDuracao>
/*    */ {
/* 40 */   static final TarefaVideoDivisaoDuracaoReader INSTANCE = new TarefaVideoDivisaoDuracaoReader();
/*    */   
/*    */   protected static final class TarefaVideoDivisaoDuracao
/*    */     extends TarefaMediaReader.TarefaMedia implements ITarefaVideoDivisaoDuracao {
/*    */     private long duracao;
/*    */     
/*    */     public long getDuracao() {
/* 47 */       return this.duracao;
/*    */     }
/*    */   }
/*    */   
/*    */   private TarefaVideoDivisaoDuracaoReader() {
/* 52 */     super(TarefaVideoDivisaoDuracao.class);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ITask<?> createTask(Params output, ITarefaVideoDivisaoDuracao pojo) throws IOException {
/* 57 */     return (ITask<?>)new PjeByDurationVideoSplitterTask(output, pojo);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getTarefaId() {
/* 62 */     return PjeTaskReader.VIDEO_SPLIT_BY_DURATION.getId();
/*    */   }
/*    */ 
/*    */   
/*    */   protected ITarefaVideoDivisaoDuracao getTarefa(Params param) {
/* 67 */     TarefaVideoDivisaoDuracao tarefaDuracao = new TarefaVideoDivisaoDuracao();
/* 68 */     tarefaDuracao.duracao = Long.parseLong((String)param.getValue("duracao"));
/* 69 */     tarefaDuracao.arquivos = (List<String>)param.getValue("arquivos");
/* 70 */     return tarefaDuracao;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/TarefaVideoDivisaoDuracaoReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */