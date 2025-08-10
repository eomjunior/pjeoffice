/*    */ package br.jus.cnj.pje.office.task.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeResponse;
/*    */ import br.jus.cnj.pje.office.task.ITarefaDownload;
/*    */ import com.github.taskresolver4j.ITaskResponse;
/*    */ import com.github.taskresolver4j.exception.TaskException;
/*    */ import com.github.utils4j.IConstants;
/*    */ import com.github.utils4j.imp.Params;
/*    */ import com.github.utils4j.imp.Throwables;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.UnsupportedEncodingException;
/*    */ import java.net.URLDecoder;
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
/*    */ class PjeDownloadTask
/*    */   extends PjeAbstractTask<ITarefaDownload>
/*    */ {
/*    */   private String url;
/*    */   private String enviarPara;
/*    */   
/*    */   protected PjeDownloadTask(Params request, ITarefaDownload pojo) {
/* 52 */     super(request, pojo, true);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void validateTaskParams() throws TaskException {
/* 57 */     ITarefaDownload pojo = getPojoParams();
/* 58 */     String urlurl = PjeTaskChecker.<String>checkIfPresent(pojo.getUrl(), "url");
/* 59 */     this.url = (String)Throwables.call(() -> URLDecoder.decode(urlurl, IConstants.UTF_8.name()), urlurl);
/* 60 */     this.enviarPara = PjeTaskChecker.<String>checkIfPresent(pojo.getEnviarPara(), "enviarPara");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected ITaskResponse<IPjeResponse> doGet() throws TaskException, InterruptedException {
/* 66 */     download(getExternalTarget(this.url), new File(this.enviarPara))
/* 67 */       .orElseThrow(() -> showFail("Não foi possível baixar o arquivo. ", "URL: " + this.url, Optional.<Throwable>ofNullable(getProgress().getAbortCause()).orElse(new IOException("Arquivo vazio (length = 0)"))));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 74 */     showInfo("Download concluído!");
/*    */     
/* 76 */     return (ITaskResponse<IPjeResponse>)success();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/PjeDownloadTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */