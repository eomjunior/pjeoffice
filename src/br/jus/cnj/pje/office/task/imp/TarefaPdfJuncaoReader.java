/*    */ package br.jus.cnj.pje.office.task.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.task.ITarefaMedia;
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
/*    */ final class TarefaPdfJuncaoReader
/*    */   extends TarefaMediaReader<ITarefaMedia>
/*    */ {
/* 45 */   static final TarefaPdfJuncaoReader INSTANCE = new TarefaPdfJuncaoReader();
/*    */   
/*    */   private TarefaPdfJuncaoReader() {
/* 48 */     super(TarefaMediaReader.TarefaMedia.class);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ITask<?> createTask(Params output, ITarefaMedia pojo) throws IOException {
/* 53 */     return (ITask<?>)new PjeJoinPdfTask(output, pojo);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getTarefaId() {
/* 58 */     return PjeTaskReader.PDF_JOIN.getId();
/*    */   }
/*    */ 
/*    */   
/*    */   protected ITarefaMedia getTarefa(Params param) {
/* 63 */     TarefaMediaReader.TarefaMedia juncao = new TarefaMediaReader.TarefaMedia();
/* 64 */     juncao.arquivos = (List<String>)param.getValue("arquivos");
/* 65 */     return juncao;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/TarefaPdfJuncaoReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */