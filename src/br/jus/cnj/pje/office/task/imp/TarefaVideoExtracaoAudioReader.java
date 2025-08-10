/*    */ package br.jus.cnj.pje.office.task.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.task.ITarefaVideoExtracaoAudio;
/*    */ import com.github.taskresolver4j.ITask;
/*    */ import com.github.utils4j.imp.Params;
/*    */ import com.github.utils4j.imp.Strings;
/*    */ import java.io.IOException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class TarefaVideoExtracaoAudioReader
/*    */   extends TarefaMediaReader<ITarefaVideoExtracaoAudio>
/*    */ {
/* 48 */   static final TarefaVideoExtracaoAudioReader INSTANCE = new TarefaVideoExtracaoAudioReader();
/*    */   
/*    */   protected static final class TarefaVideoExtracaoAudio
/*    */     extends TarefaMediaReader.TarefaMedia implements ITarefaVideoExtracaoAudio {
/*    */     private String tipo;
/*    */     
/*    */     public Optional<String> getTipo() {
/* 55 */       return Strings.optional(this.tipo);
/*    */     }
/*    */   }
/*    */   
/*    */   private TarefaVideoExtracaoAudioReader() {
/* 60 */     super(TarefaVideoExtracaoAudio.class);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ITask<?> createTask(Params output, ITarefaVideoExtracaoAudio pojo) throws IOException {
/* 65 */     return (ITask<?>)new PjeAudioExtractorTask(output, pojo);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getTarefaId() {
/* 70 */     return PjeTaskReader.VIDEO_EXTRACT_AUDIO.getId();
/*    */   }
/*    */ 
/*    */   
/*    */   protected ITarefaVideoExtracaoAudio getTarefa(Params param) {
/* 75 */     TarefaVideoExtracaoAudio tarefa = new TarefaVideoExtracaoAudio();
/* 76 */     tarefa.arquivos = (List<String>)param.getValue("arquivos");
/* 77 */     tarefa.tipo = (String)param.getValue("tipo");
/* 78 */     return tarefa;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/TarefaVideoExtracaoAudioReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */