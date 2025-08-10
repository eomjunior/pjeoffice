/*     */ package br.jus.cnj.pje.office.task.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.IPjeClient;
/*     */ import br.jus.cnj.pje.office.core.IPjeResponse;
/*     */ import br.jus.cnj.pje.office.core.imp.PjeClientException;
/*     */ import br.jus.cnj.pje.office.core.imp.PjeTaskResponse;
/*     */ import br.jus.cnj.pje.office.core.imp.PjeTaskResponses;
/*     */ import br.jus.cnj.pje.office.signer4j.IPjeToken;
/*     */ import br.jus.cnj.pje.office.task.IHashedOutputDocument;
/*     */ import br.jus.cnj.pje.office.task.IOutputDocument;
/*     */ import br.jus.cnj.pje.office.task.IPjeEndpoint;
/*     */ import br.jus.cnj.pje.office.task.ITarefaAssinadorHash;
/*     */ import com.github.progress4j.IProgressView;
/*     */ import com.github.progress4j.IStage;
/*     */ import com.github.signer4j.ISignatureAlgorithm;
/*     */ import com.github.signer4j.ISignedData;
/*     */ import com.github.signer4j.ISignerBuilder;
/*     */ import com.github.signer4j.ISimpleSigner;
/*     */ import com.github.signer4j.imp.SignedData;
/*     */ import com.github.signer4j.imp.exception.InterruptedOperationException;
/*     */ import com.github.signer4j.imp.exception.Signer4JException;
/*     */ import com.github.taskresolver4j.IExceptionContext;
/*     */ import com.github.taskresolver4j.ITaskResponse;
/*     */ import com.github.taskresolver4j.exception.ExceptionContext;
/*     */ import com.github.taskresolver4j.exception.TaskException;
/*     */ import com.github.utils4j.imp.Params;
/*     */ import java.util.List;
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
/*     */ class PjeHashSigningTask
/*     */   extends PjeAbstractTask<ITarefaAssinadorHash>
/*     */ {
/*     */   private boolean modoTeste;
/*     */   private String uploadUrl;
/*     */   private ISignatureAlgorithm algorithm;
/*     */   private List<IHashedOutputDocument> arquivos;
/*     */   
/*     */   private enum Stage
/*     */     implements IStage
/*     */   {
/*  59 */     HASH_SIGNING;
/*     */ 
/*     */     
/*     */     public final String toString() {
/*  63 */       return "Assinatura de HASH's";
/*     */     }
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
/*     */   public PjeHashSigningTask(Params request, ITarefaAssinadorHash pojo) {
/*  76 */     super(request, pojo);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void validateTaskParams() throws TaskException {
/*  81 */     ITarefaAssinadorHash params = getPojoParams();
/*  82 */     this.algorithm = PjeTaskChecker.checkIfSupportedSig(params.getAlgoritmoAssinatura(), "algoritmoAssinatura");
/*  83 */     this.uploadUrl = PjeTaskChecker.<String>checkIfPresent(params.getUploadUrl(), "uploadUrl");
/*  84 */     this.modoTeste = params.isModoTeste();
/*  85 */     this.arquivos = params.getArquivos();
/*     */   }
/*     */   
/*     */   private static byte[] hashToBytes(String hash) {
/*  89 */     int mid = hash.length() / 2;
/*  90 */     byte[] b = new byte[mid];
/*  91 */     for (int i = 0; i < mid; i++) {
/*  92 */       b[i] = (byte)(Integer.parseInt(hash.substring(i << 1, i + 1 << 1), 16) & 0xFF);
/*     */     }
/*  94 */     return b;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected ITaskResponse<IPjeResponse> doGet() throws TaskException, InterruptedException {
/* 100 */     int size = this.arquivos.size();
/* 101 */     if (size == 0) {
/* 102 */       throw new TaskException("Não há dados a serem assinados");
/*     */     }
/*     */     
/* 105 */     IPjeEndpoint target = getTarget(this.uploadUrl);
/* 106 */     IProgressView iProgressView = getProgress();
/*     */     
/* 108 */     int failCount = this.arquivos.size();
/* 109 */     int docfail = -1;
/*     */     
/* 111 */     PjeTaskResponses response = new PjeTaskResponses();
/*     */     
/* 113 */     IPjeToken token = loginToken();
/*     */     
/*     */     try {
/* 116 */       ISimpleSigner signer = (ISimpleSigner)((ISignerBuilder)token.signerBuilder().usingAlgorithm(this.algorithm).usingLock(tokenLock())).build();
/* 117 */       iProgressView.begin(Stage.HASH_SIGNING, size);
/*     */       
/* 119 */       int index = 0;
/*     */       do {
/*     */         try {
/*     */           ISignedData signedData;
/* 123 */           IHashedOutputDocument document = this.arquivos.get(index);
/*     */           
/* 125 */           String id = document.getId().orElse("[index: " + index + "]");
/*     */           
/* 127 */           iProgressView.step("Documento Id: %s", new Object[] { id });
/*     */ 
/*     */           
/* 130 */           if (this.modoTeste) {
/* 131 */             signedData = SignedData.forTest();
/*     */           } else {
/*     */             try {
/* 134 */               signedData = signer.process(hashToBytes(PjeTaskChecker.<String>checkIfPresent(document.getHash(), "hash")));
/* 135 */             } catch (Signer4JException e) {
/* 136 */               throw new TaskException("Não foi possível assinar o arquivo id: " + id, e);
/*     */             } 
/*     */           } 
/*     */           
/* 140 */           iProgressView.info("O documento %s foi assinado. Tentativa de envio...", new Object[] { id });
/*     */           
/*     */           try {
/* 143 */             response.add((ITaskResponse)withClient(c -> c.send(target, signedData, (IOutputDocument)document)));
/* 144 */             failCount--; docfail = -1;
/* 145 */           } catch (InterruptedException e) {
/* 146 */             throw e;
/*     */           }
/* 148 */           catch (PjeClientException e) {
/* 149 */             throw new TaskException("Não foi possível enviar o arquivo id: " + id, e);
/*     */           } 
/*     */           
/* 152 */           iProgressView.info("Enviado documento: %s", new Object[] { id });
/* 153 */         } catch (InterruptedException|TaskExhaustedConnectionException|InterruptedOperationException|com.github.taskresolver4j.exception.TaskDiscardException e) {
/* 154 */           throw e;
/*     */         }
/* 156 */         catch (Exception e) {
/* 157 */           iProgressView.abort(e);
/* 158 */           int remainder = size - index - 1;
/*     */           
/* 160 */           if (!token.isAuthenticated()) {
/* 161 */             if (docfail < 0) {
/* 162 */               docfail = index;
/* 163 */               index--;
/* 164 */               remainder++;
/*     */             } else {
/* 166 */               docfail = -1;
/*     */             } 
/* 168 */             if (remainder > 0) {
/* 169 */               iProgressView.info("Retomando a assinatura por perda de autenticação. Falta(m) %s arquivo(s) ", new Object[] { Integer.valueOf(remainder) });
/*     */               try {
/* 171 */                 token = loginToken();
/* 172 */               } catch (InterruptedOperationException ex) {
/* 173 */                 ISignedData signedData; iProgressView.abort(e);
/* 174 */                 signedData.addSuppressed(e);
/* 175 */                 throw signedData;
/*     */               } 
/* 177 */               signer = (ISimpleSigner)((ISignerBuilder)token.signerBuilder().usingAlgorithm(this.algorithm).usingLock(tokenLock())).build();
/*     */             } 
/*     */           } else {
/* 180 */             docfail = -1;
/*     */           } 
/* 182 */           if (remainder > 0) {
/* 183 */             iProgressView.end();
/* 184 */             iProgressView.begin(Stage.HASH_SIGNING, remainder);
/*     */           }
/*     */         
/*     */         } 
/* 188 */       } while (++index < size);
/*     */       
/* 190 */       iProgressView.end();
/*     */     } finally {
/*     */       
/* 193 */       token.logout();
/*     */     } 
/*     */     
/* 196 */     if (failCount > 0) {
/* 197 */       iProgressView.throwIfInterrupted();
/* 198 */       throw showFail("Não foi possível assinar os arquivos", iProgressView.getAbortCause());
/*     */     } 
/*     */     
/* 201 */     return (ITaskResponse<IPjeResponse>)response;
/*     */   }
/*     */ 
/*     */   
/*     */   protected final Runnable getAlertFailCode(String message, String detail, Throwable cause) {
/* 206 */     return () -> getExecutorContext().alert((IExceptionContext)new ExceptionContext(message, detail, cause));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/PjeHashSigningTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */