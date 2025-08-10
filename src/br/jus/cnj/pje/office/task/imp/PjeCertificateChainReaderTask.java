/*    */ package br.jus.cnj.pje.office.task.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeClient;
/*    */ import br.jus.cnj.pje.office.core.IPjeResponse;
/*    */ import br.jus.cnj.pje.office.core.imp.PjeClientException;
/*    */ import br.jus.cnj.pje.office.core.imp.PjeTaskResponse;
/*    */ import br.jus.cnj.pje.office.signer4j.IPjeToken;
/*    */ import br.jus.cnj.pje.office.task.ITarefaCertChain;
/*    */ import com.github.taskresolver4j.ITaskResponse;
/*    */ import com.github.taskresolver4j.exception.TaskException;
/*    */ import com.github.utils4j.imp.Params;
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
/*    */ class PjeCertificateChainReaderTask
/*    */   extends PjeAbstractTask<ITarefaCertChain>
/*    */ {
/*    */   private String uploadUrl;
/*    */   
/*    */   public PjeCertificateChainReaderTask(Params params, ITarefaCertChain pojo) {
/* 44 */     super(params, pojo);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void validateTaskParams() throws TaskException {
/* 49 */     ITarefaCertChain pojo = getPojoParams();
/* 50 */     this.uploadUrl = PjeTaskChecker.<String>checkIfPresent(pojo.getUploadUrl(), "uploadUrl");
/*    */   }
/*    */ 
/*    */   
/*    */   protected ITaskResponse<IPjeResponse> doGet() throws TaskException, InterruptedException {
/* 55 */     IPjeToken token = loginToken();
/*    */     try {
/*    */       String certificateChain64;
/*    */       try {
/* 59 */         certificateChain64 = token.createChooser().choose().getCertificateChain64();
/* 60 */       } catch (Exception e) {
/* 61 */         throw new TaskException("Escolha do certificado cancelada", e);
/*    */ 
/*    */       
/*    */       }
/*    */ 
/*    */ 
/*    */     
/*    */     }
/*    */     finally {
/*    */ 
/*    */ 
/*    */       
/* 73 */       token.logout();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/PjeCertificateChainReaderTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */