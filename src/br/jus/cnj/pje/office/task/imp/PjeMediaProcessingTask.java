/*    */ package br.jus.cnj.pje.office.task.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeResponse;
/*    */ import br.jus.cnj.pje.office.task.ITarefaMedia;
/*    */ import com.github.progress4j.IProgress;
/*    */ import com.github.progress4j.IProgressView;
/*    */ import com.github.progress4j.IQuietlyProgress;
/*    */ import com.github.progress4j.IStage;
/*    */ import com.github.progress4j.imp.QuietlyProgress;
/*    */ import com.github.taskresolver4j.ITaskResponse;
/*    */ import com.github.taskresolver4j.exception.TaskException;
/*    */ import com.github.utils4j.imp.Params;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.Paths;
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
/*    */ abstract class PjeMediaProcessingTask<T extends ITarefaMedia>
/*    */   extends PjeAbstractMediaTask<T>
/*    */ {
/*    */   public enum SplitterStage
/*    */     implements IStage
/*    */   {
/* 47 */     PROCESSING("Processando arquivos"),
/* 48 */     READING("Lendo o arquivo (seja paciente...)"),
/* 49 */     SPLITING("Dividindo arquivos"),
/* 50 */     SPLITTING_PATIENT("Dividindo arquivos (seja paciente...)");
/*    */     
/*    */     private final String message;
/*    */     
/*    */     SplitterStage(String message) {
/* 55 */       this.message = message;
/*    */     }
/*    */ 
/*    */     
/*    */     public final String toString() {
/* 60 */       return this.message;
/*    */     }
/*    */   }
/*    */   
/*    */   protected PjeMediaProcessingTask(Params request, T pojo) {
/* 65 */     super(request, pojo);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ITaskResponse<IPjeResponse> doGet() throws TaskException, InterruptedException {
/* 70 */     IProgressView iProgressView = getProgress();
/* 71 */     IQuietlyProgress quietly = QuietlyProgress.wrap((IProgress)iProgressView);
/* 72 */     int size = this.arquivos.size();
/*    */     
/* 74 */     boolean success = true;
/*    */     
/* 76 */     iProgressView.begin(SplitterStage.PROCESSING, size);
/* 77 */     for (int i = 0; i < size; i++) {
/* 78 */       Path file = Paths.get(this.arquivos.get(i), new String[0]);
/*    */       
/* 80 */       success &= process(file, quietly);
/*    */       
/* 82 */       iProgressView.step("Gerado arquivo %s", new Object[] { file });
/*    */     } 
/*    */     
/* 85 */     if (!success) {
/* 86 */       iProgressView.throwIfInterrupted();
/* 87 */       throw showFail("Alguns arquivos não puderam ser gerados.", iProgressView.getAbortCause());
/*    */     } 
/*    */     
/* 90 */     iProgressView.end();
/*    */     
/* 92 */     showInfo("Arquivos gerados com sucesso.", "Ótimo!");
/* 93 */     return (ITaskResponse<IPjeResponse>)success();
/*    */   }
/*    */   
/*    */   protected abstract boolean process(Path paramPath, IQuietlyProgress paramIQuietlyProgress) throws InterruptedException;
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/PjeMediaProcessingTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */