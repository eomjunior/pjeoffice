/*    */ package br.jus.cnj.pje.office.task.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeClient;
/*    */ import br.jus.cnj.pje.office.core.imp.PjeClientException;
/*    */ import br.jus.cnj.pje.office.core.imp.PjeTaskResponse;
/*    */ import br.jus.cnj.pje.office.task.ISSOPayload;
/*    */ import br.jus.cnj.pje.office.task.ITarefaAutenticador;
/*    */ import com.github.signer4j.ISignedData;
/*    */ import com.github.taskresolver4j.exception.TaskException;
/*    */ import com.github.utils4j.imp.Params;
/*    */ import java.io.Serializable;
/*    */ import java.security.cert.CertificateException;
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
/*    */ class PjeSSOAuthenticatorTask
/*    */   extends PjeAuthenticatorTask
/*    */ {
/*    */   protected String token;
/*    */   
/*    */   public PjeSSOAuthenticatorTask(Params request, ITarefaAutenticador pojo) {
/* 44 */     super(request, pojo);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void validateTaskParams() throws TaskException {
/* 51 */     super.validateTaskParams();
/* 52 */     this.token = PjeTaskChecker.<String>checkIfPresent(getPojoParams().getToken(), "token");
/*    */   }
/*    */ 
/*    */   
/*    */   protected PjeTaskResponse send(ISignedData signedData) throws Exception {
/* 57 */     SSOPayload payload = new SSOPayload(this.token, this.mensagem, signedData);
/* 58 */     return (PjeTaskResponse)withClient(c -> c.send(getTarget(this.enviarPara), payload));
/*    */   }
/*    */ 
/*    */   
/*    */   private static class SSOPayload
/*    */     implements Serializable, ISSOPayload
/*    */   {
/*    */     private static final long serialVersionUID = 1L;
/*    */     
/*    */     private String uuid;
/*    */     
/*    */     private String mensagem;
/*    */     
/*    */     private String assinatura;
/*    */     private String certChain;
/*    */     
/*    */     public SSOPayload(String token, String mensagem, ISignedData signedData) throws CertificateException {
/* 75 */       this.uuid = token;
/* 76 */       this.mensagem = mensagem;
/* 77 */       this.assinatura = signedData.getSignature64();
/* 78 */       this.certChain = signedData.getCertificateChain64();
/*    */     }
/*    */ 
/*    */     
/*    */     public String getUuid() {
/* 83 */       return this.uuid;
/*    */     }
/*    */ 
/*    */     
/*    */     public String getMensagem() {
/* 88 */       return this.mensagem;
/*    */     }
/*    */ 
/*    */     
/*    */     public String getAssinatura() {
/* 93 */       return this.assinatura;
/*    */     }
/*    */ 
/*    */     
/*    */     public String getCertChain() {
/* 98 */       return this.certChain;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/PjeSSOAuthenticatorTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */