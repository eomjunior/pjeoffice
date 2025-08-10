/*     */ package br.jus.cnj.pje.office.task.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.IPjeClient;
/*     */ import br.jus.cnj.pje.office.core.IPjeResponse;
/*     */ import br.jus.cnj.pje.office.core.imp.PjeClientException;
/*     */ import br.jus.cnj.pje.office.core.imp.PjeTaskResponse;
/*     */ import br.jus.cnj.pje.office.signer4j.IPjeToken;
/*     */ import br.jus.cnj.pje.office.task.ITarefaAutenticador;
/*     */ import com.github.progress4j.IProgressView;
/*     */ import com.github.progress4j.IStage;
/*     */ import com.github.signer4j.ISignatureAlgorithm;
/*     */ import com.github.signer4j.ISignedData;
/*     */ import com.github.signer4j.ISignerBuilder;
/*     */ import com.github.signer4j.ISimpleSigner;
/*     */ import com.github.signer4j.imp.SignatureAlgorithm;
/*     */ import com.github.signer4j.imp.exception.Signer4JException;
/*     */ import com.github.taskresolver4j.ITaskResponse;
/*     */ import com.github.taskresolver4j.exception.TaskDiscardException;
/*     */ import com.github.taskresolver4j.exception.TaskException;
/*     */ import com.github.utils4j.IConstants;
/*     */ import com.github.utils4j.imp.Params;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class PjeAuthenticatorTask
/*     */   extends PjeAbstractTask<ITarefaAutenticador>
/*     */ {
/*     */   private ISignatureAlgorithm algorithm;
/*     */   protected String enviarPara;
/*     */   protected String mensagem;
/*     */   
/*     */   private enum Stage
/*     */     implements IStage
/*     */   {
/*  50 */     AUTHENTICATING_USER;
/*     */ 
/*     */     
/*     */     public final String toString() {
/*  54 */       return "Aguardando autenticação do usuário";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PjeAuthenticatorTask(Params request, ITarefaAutenticador pojo) {
/*  65 */     super(request, pojo);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void validateTaskParams() throws TaskException {
/*  70 */     ITarefaAutenticador params = getPojoParams();
/*  71 */     this.enviarPara = PjeTaskChecker.<String>checkIfPresent(params.getEnviarPara(), "enviarPara");
/*  72 */     this.mensagem = PjeTaskChecker.<String>checkIfPresent(params.getMensagem(), "mensagem (challengePhrase)");
/*  73 */     this.algorithm = PjeTaskChecker.checkIfSupportedSig(params
/*  74 */         .getAlgoritmoAssinatura()
/*  75 */         .orElse(SignatureAlgorithm.MD5withRSA.getName()), "algoritmoAssinatura");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void onBeforeDoGet() throws TaskDiscardException, InterruptedException {
/*  88 */     forceLogout();
/*     */   }
/*     */   
/*     */   protected ITaskResponse<IPjeResponse> doGet() throws TaskException, InterruptedException {
/*     */     ISignedData signedData;
/*     */     PjeTaskResponse response;
/*  94 */     IProgressView iProgressView = getProgress();
/*     */     
/*  96 */     iProgressView.begin(Stage.AUTHENTICATING_USER, 3);
/*     */     
/*  98 */     iProgressView.step("Recebida a frase desafio", new Object[0]);
/*     */     
/* 100 */     byte[] content = this.mensagem.getBytes(IConstants.DEFAULT_CHARSET);
/*     */     
/* 102 */     iProgressView.step("Assinando o conteúdo. Algoritmo: '%s'", new Object[] { this.algorithm.getName() });
/*     */ 
/*     */     
/* 105 */     IPjeToken token = loginToken();
/*     */     try {
/* 107 */       signedData = ((ISimpleSigner)((ISignerBuilder)token.signerBuilder().usingAlgorithm(this.algorithm).usingLock(tokenLock())).build()).process(content);
/* 108 */     } catch (Signer4JException e) {
/* 109 */       throw (TaskException)iProgressView.abort(showFail("Não foi possível assinar a mensagem.", e));
/*     */     } finally {
/* 111 */       token.logout();
/*     */     } 
/*     */     
/* 114 */     iProgressView.step("Enviando assinatura para o servidor.", new Object[0]);
/*     */     
/*     */     try {
/* 117 */       response = send(signedData);
/* 118 */     } catch (Exception e) {
/* 119 */       throw (TaskException)iProgressView.abort(showFail("Não foi possível enviar os dados ao servidor.", e));
/*     */     } 
/* 121 */     iProgressView.end();
/* 122 */     return (ITaskResponse<IPjeResponse>)response;
/*     */   }
/*     */   
/*     */   protected PjeTaskResponse send(ISignedData signedData) throws Exception {
/* 126 */     return (PjeTaskResponse)withClient(c -> c.send(getTarget(this.enviarPara), signedData));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/PjeAuthenticatorTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */