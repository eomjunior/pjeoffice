/*     */ package br.jus.cnj.pje.office.task.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.IPjeResponse;
/*     */ import br.jus.cnj.pje.office.core.imp.PjeTaskResponse;
/*     */ import br.jus.cnj.pje.office.core.imp.PjeTaskResponses;
/*     */ import br.jus.cnj.pje.office.core.imp.UnsupportedCosignException;
/*     */ import br.jus.cnj.pje.office.signer4j.IPjeToken;
/*     */ import br.jus.cnj.pje.office.task.ICosignChecker;
/*     */ import br.jus.cnj.pje.office.task.IPjeSignMode;
/*     */ import br.jus.cnj.pje.office.task.ISignableURLDocument;
/*     */ import br.jus.cnj.pje.office.task.ISignedURLDocument;
/*     */ import br.jus.cnj.pje.office.task.IStandardSignature;
/*     */ import br.jus.cnj.pje.office.task.ITarefaAssinador;
/*     */ import com.github.progress4j.IProgressView;
/*     */ import com.github.progress4j.IStage;
/*     */ import com.github.signer4j.IByteProcessor;
/*     */ import com.github.signer4j.gui.alert.ExpiredPasswordAlert;
/*     */ import com.github.signer4j.gui.alert.NoTokenPresentAlert;
/*     */ import com.github.signer4j.gui.alert.TokenLockedAlert;
/*     */ import com.github.signer4j.imp.exception.ExpiredCredentialException;
/*     */ import com.github.signer4j.imp.exception.InterruptedOperationException;
/*     */ import com.github.signer4j.imp.exception.NoTokenPresentException;
/*     */ import com.github.signer4j.imp.exception.Signer4JException;
/*     */ import com.github.signer4j.imp.exception.TokenLockedException;
/*     */ import com.github.taskresolver4j.ITaskResponse;
/*     */ import com.github.taskresolver4j.exception.TaskEscapeException;
/*     */ import com.github.taskresolver4j.exception.TaskException;
/*     */ import com.github.utils4j.imp.Params;
/*     */ import com.github.utils4j.imp.TemporaryException;
/*     */ import java.io.IOException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class PjeSigningTask
/*     */   extends PjeAbstractTask<ITarefaAssinador>
/*     */ {
/*     */   private static final int MAX_SEQUENTIAL_FAILURE_COUNT = 5;
/*     */   protected IPjeSignMode modo;
/*     */   protected IStandardSignature padraoAssinatura;
/*     */   protected ISignableURLDocument first;
/*     */   
/*     */   private enum Stage
/*     */     implements IStage
/*     */   {
/*  68 */     SELECTING_FILES("Seleção de arquivos"),
/*     */     
/*  70 */     PROCESSING_FILES("Processamento de arquivos");
/*     */     
/*     */     private final String message;
/*     */     
/*     */     Stage(String message) {
/*  75 */       this.message = message;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  80 */       return this.message;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PjeSigningTask(Params request, ITarefaAssinador pojo, boolean internalTask) {
/*  91 */     super(request, pojo, internalTask);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void validateTaskParams() throws TaskException {
/*  96 */     ITarefaAssinador params = getPojoParams();
/*  97 */     this.modo = PjeTaskChecker.<IPjeSignMode>checkIfPresent(params.getModo(), "modo");
/*  98 */     this.padraoAssinatura = ((IStandardSignature)PjeTaskChecker.<IStandardSignature>checkIfPresent(params.getPadraoAssinatura(), "padraoAssinatura")).check(params);
/*     */   }
/*     */   
/*     */   protected final Optional<ISignableURLDocument> getFirst() {
/* 102 */     return Optional.ofNullable(this.first);
/*     */   }
/*     */   
/*     */   protected ITaskResponse<IPjeResponse> doGet() throws TaskException, InterruptedException {
/*     */     TemporaryException temporaryException;
/* 107 */     ITarefaAssinador params = getPojoParams();
/* 108 */     IProgressView iProgressView = getProgress();
/*     */     
/* 110 */     int success = 0;
/*     */     
/* 112 */     iProgressView.begin(Stage.SELECTING_FILES);
/* 113 */     ISignedURLDocument[] documents = selectFiles();
/* 114 */     int size = documents.length;
/* 115 */     this.first = (ISignableURLDocument)documents[0];
/* 116 */     iProgressView.step("Selecionados '%s' arquivo(s)", new Object[] { Integer.valueOf(size) });
/* 117 */     iProgressView.end();
/*     */     
/* 119 */     PjeTaskResponses responses = new PjeTaskResponses();
/* 120 */     Throwable fail = null;
/* 121 */     int index = 0;
/*     */     
/* 123 */     IPjeToken token = loginToken();
/*     */     
/*     */     try {
/* 126 */       IByteProcessor processor = this.padraoAssinatura.processorBuilder(token, params).usingLock(tokenLock()).build();
/*     */       
/* 128 */       ICosignChecker checker = this.padraoAssinatura.cosignChecker();
/*     */       
/* 130 */       iProgressView.begin(Stage.PROCESSING_FILES, 2 * size);
/*     */       
/* 132 */       int consecutiveFailsCount = 0, docfail = -1;
/*     */       do {
/* 134 */         ISignedURLDocument document = documents[index];
/*     */         
/*     */         try {
/* 137 */           String fileName = document.getNome().orElse(Integer.toString(index));
/*     */           
/* 139 */           iProgressView.step("Assinando arquivo '%s'", new Object[] { fileName });
/*     */           try {
/* 141 */             document.sign(processor, checker);
/* 142 */             consecutiveFailsCount = 0; docfail = -1;
/*     */           }
/* 144 */           catch (TokenLockedException e) {
/* 145 */             TokenLockedAlert.show();
/* 146 */             throw new TaskEscapeException("Token bloqueado!", e);
/*     */           }
/* 148 */           catch (ExpiredCredentialException e) {
/* 149 */             ExpiredPasswordAlert.show();
/* 150 */             throw new TaskEscapeException("Credenciais expiradas!", e);
/*     */           }
/* 152 */           catch (NoTokenPresentException e) {
/* 153 */             if (NoTokenPresentAlert.isNo()) {
/* 154 */               throw new InterruptedOperationException(e);
/*     */             }
/*     */             
/* 157 */             String message = "Arquivo ignorado. Token não encontrado quando da assinatura do documento: ";
/* 158 */             LOGGER.warn(message + document.toString(), (Throwable)e);
/* 159 */             iProgressView.info(message + e.getMessage(), new Object[0]);
/* 160 */             throw new TemporaryException(e);
/*     */           }
/* 162 */           catch (IOException e) {
/* 163 */             String message = "Arquivo ignorado. Não foi possível ler os bytes do arquivo temporário: ";
/* 164 */             LOGGER.warn(message + document.toString(), e);
/* 165 */             iProgressView.info(message + e.getMessage(), new Object[0]);
/* 166 */             throw new TemporaryException(e);
/*     */           }
/* 168 */           catch (UnsupportedCosignException e) {
/* 169 */             String message = "Arquivo ignorado. Co-assinatura não é suportada: ";
/* 170 */             LOGGER.warn(message + document.toString(), (Throwable)e);
/* 171 */             iProgressView.info(message + e.getMessage(), new Object[0]);
/* 172 */             throw new TemporaryException(e);
/*     */           }
/* 174 */           catch (Signer4JException e) {
/* 175 */             String message = "Arquivo ignorado:  " + document.toString();
/* 176 */             LOGGER.warn(message, (Throwable)e);
/* 177 */             iProgressView.info(message + " -> " + e.getMessage(), new Object[0]);
/* 178 */             throw new TemporaryException(e);
/*     */           } 
/*     */           
/*     */           try {
/* 182 */             iProgressView.step("Enviando arquivo '%s'", new Object[] { fileName });
/* 183 */             responses.add((ITaskResponse)send((ISignableURLDocument)document));
/* 184 */             success++;
/* 185 */             document.dispose();
/* 186 */           } catch (TaskExhaustedConnectionException e) {
/* 187 */             throw e;
/*     */           }
/* 189 */           catch (TaskException e) {
/* 190 */             String message = "Arquivo ignorado:  " + document.toString();
/* 191 */             LOGGER.warn(message, (Throwable)e);
/* 192 */             iProgressView.info(message + " -> " + e.getMessage(), new Object[0]);
/* 193 */             throw new TemporaryException(e);
/*     */           }
/*     */         
/* 196 */         } catch (TemporaryException e) {
/* 197 */           if (++consecutiveFailsCount == 5) {
/* 198 */             iProgressView.throwIfInterrupted();
/* 199 */             throw showFail("O processo de assinatura foi interrompido!", "Foi alcançado o número máximo de falhas de assinatura consecutivas (" + consecutiveFailsCount + "). Possíveis causas do problema:\n \tO token/certificado não está conectado/disponível;\n\tOs arquivos são muito grandes;\n\tOs documentos já se encontram assinados;\n\tO seu dispositivo/token não suporta o algoritmo necessário para assinatura\nDetalhes:", e);
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 208 */           iProgressView.abort((Throwable)e);
/*     */           
/* 210 */           if (fail == null) {
/* 211 */             temporaryException = e;
/*     */           } else {
/* 213 */             temporaryException.addSuppressed((Throwable)e);
/*     */           } 
/* 215 */           int remainder = size - index - 1;
/* 216 */           if (!token.isAuthenticated()) {
/* 217 */             if (docfail < 0) {
/* 218 */               docfail = index;
/* 219 */               index--;
/* 220 */               remainder++;
/*     */             } else {
/* 222 */               docfail = -1;
/* 223 */               document.dispose();
/*     */             } 
/* 225 */             if (remainder > 0) {
/* 226 */               iProgressView.info("Retomando a assinatura por perda de autenticação. Falta(m) %s arquivo(s) ", new Object[] { Integer.valueOf(remainder) });
/*     */               try {
/* 228 */                 token = loginToken();
/* 229 */               } catch (InterruptedOperationException ex) {
/* 230 */                 iProgressView.abort((Throwable)e);
/* 231 */                 ex.addSuppressed((Throwable)e);
/* 232 */                 throw ex;
/*     */               } 
/* 234 */               processor = this.padraoAssinatura.processorBuilder(token, params).usingLock(tokenLock()).build();
/*     */             } 
/*     */           } else {
/* 237 */             docfail = -1;
/*     */           } 
/* 239 */           if (remainder > 0) {
/* 240 */             iProgressView.end();
/* 241 */             iProgressView.begin(Stage.PROCESSING_FILES, 2 * remainder);
/*     */           } 
/*     */         } 
/* 244 */       } while (++index < size);
/*     */       
/* 246 */       iProgressView.end();
/*     */     } finally {
/*     */       
/* 249 */       token.logout();
/*     */     } 
/*     */     
/* 252 */     if (success != size) {
/* 253 */       iProgressView.throwIfInterrupted();
/* 254 */       throw showFail("Alguns arquivos não puderam ser assinados.", temporaryException);
/*     */     } 
/*     */     
/* 257 */     if (isInternal()) {
/* 258 */       showInfo("Arquivos assinados com sucesso!", "Ótimo!");
/*     */     }
/*     */     
/* 261 */     return (ITaskResponse<IPjeResponse>)responses;
/*     */   }
/*     */   
/*     */   protected abstract ISignedURLDocument[] selectFiles() throws TaskException, InterruptedException;
/*     */   
/*     */   protected abstract PjeTaskResponse send(ISignableURLDocument paramISignableURLDocument) throws TaskException, InterruptedException;
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/PjeSigningTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */