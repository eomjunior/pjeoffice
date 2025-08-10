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
/*    */ 
/*    */ final class TarefaVideoDivisaoSliceReader
/*    */   extends TarefaMediaReader<ITarefaMedia>
/*    */ {
/* 46 */   static final TarefaVideoDivisaoSliceReader INSTANCE = new TarefaVideoDivisaoSliceReader();
/*    */   
/*    */   private TarefaVideoDivisaoSliceReader() {
/* 49 */     super(TarefaMediaReader.TarefaMedia.class);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ITask<?> createTask(Params output, ITarefaMedia pojo) throws IOException {
/* 54 */     return (ITask<?>)new PjeBySliceVideoSplitterTask(output, pojo);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getTarefaId() {
/* 59 */     return PjeTaskReader.VIDEO_SPLIT_BY_SLICE.getId();
/*    */   }
/*    */ 
/*    */   
/*    */   protected ITarefaMedia getTarefa(Params param) {
/* 64 */     TarefaMediaReader.TarefaMedia tarefaSlice = new TarefaMediaReader.TarefaMedia();
/* 65 */     tarefaSlice.arquivos = (List<String>)param.getValue("arquivos");
/* 66 */     return tarefaSlice;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/TarefaVideoDivisaoSliceReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */