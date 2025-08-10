/*     */ package br.jus.cnj.pje.office.task.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.IPjeClient;
/*     */ import br.jus.cnj.pje.office.core.IPjeResponse;
/*     */ import br.jus.cnj.pje.office.core.imp.PjeClientException;
/*     */ import br.jus.cnj.pje.office.core.imp.PjeTaskResponse;
/*     */ import br.jus.cnj.pje.office.signer4j.IPjeToken;
/*     */ import br.jus.cnj.pje.office.task.IInputDocument64;
/*     */ import br.jus.cnj.pje.office.task.ISignedOutputDocument64;
/*     */ import br.jus.cnj.pje.office.task.ITarefaAssinadorBase64;
/*     */ import com.github.progress4j.IProgressView;
/*     */ import com.github.progress4j.IStage;
/*     */ import com.github.signer4j.IByteProcessor;
/*     */ import com.github.signer4j.ISignatureAlgorithm;
/*     */ import com.github.signer4j.ISignerBuilder;
/*     */ import com.github.taskresolver4j.ITaskResponse;
/*     */ import com.github.taskresolver4j.exception.TaskException;
/*     */ import com.github.utils4j.IDisposable;
/*     */ import com.github.utils4j.imp.Base64;
/*     */ import com.github.utils4j.imp.Params;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class PjeBase64SignerTask
/*     */   extends PjeAbstractTask<ITarefaAssinadorBase64>
/*     */ {
/*     */   private ISignatureAlgorithm algoritmoAssinatura;
/*     */   private String uploadUrl;
/*     */   private List<IInputDocument64> arquivos;
/*     */   
/*     */   private enum Stage
/*     */     implements IStage
/*     */   {
/*  56 */     FILE_SIGNING("Assinatura de arquivos"),
/*     */     
/*  58 */     FILE_SENDING("Envio de arquivos");
/*     */     
/*     */     private final String message;
/*     */     
/*     */     Stage(String message) {
/*  63 */       this.message = message;
/*     */     }
/*     */ 
/*     */     
/*     */     public final String toString() {
/*  68 */       return this.message;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PjeBase64SignerTask(Params request, ITarefaAssinadorBase64 pojo) {
/*  79 */     super(request, pojo);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void validateTaskParams() throws TaskException {
/*  84 */     ITarefaAssinadorBase64 pojo = getPojoParams();
/*  85 */     this.algoritmoAssinatura = PjeTaskChecker.checkIfSupportedSig(pojo.getAlgoritmoAssinatura(), "algoritmoAssinatura");
/*  86 */     this.uploadUrl = PjeTaskChecker.<String>checkIfPresent(pojo.getUploadUrl(), "uploadUrl");
/*  87 */     this.arquivos = PjeTaskChecker.checkIfNotEmpty(pojo.getArquivos(), "arquivos");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected ITaskResponse<IPjeResponse> doGet() throws TaskException, InterruptedException {
/*  93 */     IProgressView iProgressView = getProgress();
/*     */     
/*  95 */     IPjeToken token = loginToken(); try {
/*     */       PjeTaskResponse response;
/*  97 */       IByteProcessor processor = ((ISignerBuilder)token.signerBuilder().usingLock(tokenLock())).usingAlgorithm(this.algoritmoAssinatura).build();
/*     */ 
/*     */       
/* 100 */       int docfail = -1; int total;
/* 101 */       iProgressView.begin(Stage.FILE_SIGNING, total = this.arquivos.size());
/*     */       
/* 103 */       List<ISignedOutputDocument64> output = new ArrayList<>(total);
/*     */       
/* 105 */       for (int index = 0; index < total; index++) {
/* 106 */         IInputDocument64 arquivo = this.arquivos.get(index);
/* 107 */         Optional<String> hashDoc = arquivo.getHashDoc();
/* 108 */         if (!hashDoc.isPresent()) {
/* 109 */           iProgressView.step("Ignorada entrada %s", new Object[] { Integer.valueOf(index) });
/* 110 */           LOGGER.warn("'hashDoc' nao encontrado na lista vinda do servidor. Entrada ignorada");
/*     */         }
/*     */         else {
/*     */           
/* 114 */           Optional<String> base64 = arquivo.getConteudoBase64();
/* 115 */           if (!base64.isPresent()) {
/* 116 */             iProgressView.step("Ignorada entrada %s", new Object[] { Integer.valueOf(index) });
/* 117 */             LOGGER.warn("'conteudoBase64' não encontrado na lista vinda do servidor. Entrada ignorada");
/*     */           }
/*     */           else {
/*     */             
/* 121 */             byte[] input = Base64.base64Decode(base64.get());
/* 122 */             iProgressView.step("Assinando arquivo %s de tamanho %s", new Object[] { Integer.valueOf(index), Integer.valueOf(input.length) });
/*     */           } 
/*     */         } 
/*     */       } 
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
/*     */ 
/*     */ 
/*     */       
/* 165 */       iProgressView.end();
/*     */       
/* 167 */       if (output.isEmpty()) {
/* 168 */         throw showFail("Nenhum documento pôde ser assinado de um total de " + total);
/*     */       }
/*     */       
/* 171 */       iProgressView.begin(Stage.FILE_SENDING);
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 176 */         response = (PjeTaskResponse)withClient(c -> c.send(getTarget(this.uploadUrl), output));
/*     */ 
/*     */       
/*     */       }
/* 180 */       catch (Exception e) {
/* 181 */         throw (TaskException)iProgressView.abort(showFail("Não foi possível enviar os dados ao servidor.", e));
/*     */       } finally {
/* 183 */         output.forEach(IDisposable::dispose);
/* 184 */         output.clear();
/*     */       } 
/* 186 */       iProgressView.end();
/* 187 */       return (ITaskResponse<IPjeResponse>)response;
/*     */     } finally {
/* 189 */       token.logout();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/PjeBase64SignerTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */