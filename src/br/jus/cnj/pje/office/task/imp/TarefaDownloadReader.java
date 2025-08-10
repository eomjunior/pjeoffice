/*    */ package br.jus.cnj.pje.office.task.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.task.ITarefaDownload;
/*    */ import com.github.taskresolver4j.ITask;
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
/*    */ 
/*    */ 
/*    */ final class TarefaDownloadReader
/*    */   extends TarefaMediaReader<ITarefaDownload>
/*    */ {
/* 43 */   static final TarefaDownloadReader INSTANCE = new TarefaDownloadReader();
/*    */   
/*    */   protected static final class TarefaDownload
/*    */     implements ITarefaDownload {
/*    */     private String url;
/*    */     private String enviarPara;
/*    */     
/*    */     public final Optional<String> getUrl() {
/* 51 */       return Strings.optional(this.url);
/*    */     }
/*    */ 
/*    */     
/*    */     public Optional<String> getEnviarPara() {
/* 56 */       return Strings.optional(this.enviarPara);
/*    */     }
/*    */   }
/*    */   
/*    */   private TarefaDownloadReader() {
/* 61 */     super(TarefaDownload.class);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ITask<?> createTask(Params output, ITarefaDownload pojo) throws IOException {
/* 66 */     return (ITask<?>)new PjeDownloadTask(output, pojo);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getTarefaId() {
/* 71 */     return PjeTaskReader.UTIL_DOWNLOADER.getId();
/*    */   }
/*    */ 
/*    */   
/*    */   protected ITarefaDownload getTarefa(Params input) {
/* 76 */     TarefaDownload td = new TarefaDownload();
/* 77 */     td.url = (String)input.getValue("url");
/* 78 */     td.enviarPara = (String)input.getValue("enviarPara");
/* 79 */     return td;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/TarefaDownloadReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */