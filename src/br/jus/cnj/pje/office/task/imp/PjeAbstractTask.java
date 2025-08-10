/*     */ package br.jus.cnj.pje.office.task.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.IPjeClient;
/*     */ import br.jus.cnj.pje.office.core.IPjeRequest;
/*     */ import br.jus.cnj.pje.office.core.IPjeResponse;
/*     */ import br.jus.cnj.pje.office.core.IPjeSecurityGrantor;
/*     */ import br.jus.cnj.pje.office.core.imp.PjeClientConnectionTimeoutException;
/*     */ import br.jus.cnj.pje.office.core.imp.PjeClientException;
/*     */ import br.jus.cnj.pje.office.core.imp.PjeClientProtocol;
/*     */ import br.jus.cnj.pje.office.core.imp.PjeConfig;
/*     */ import br.jus.cnj.pje.office.core.imp.PjeServerTracker;
/*     */ import br.jus.cnj.pje.office.core.imp.PjeTaskResponse;
/*     */ import br.jus.cnj.pje.office.signer4j.IPjeToken;
/*     */ import br.jus.cnj.pje.office.signer4j.imp.PjeTokenDevice;
/*     */ import br.jus.cnj.pje.office.task.IPayload;
/*     */ import br.jus.cnj.pje.office.task.IPjeEndpoint;
/*     */ import br.jus.cnj.pje.office.task.ITaskExecutorParams;
/*     */ import com.github.progress4j.IProgress;
/*     */ import com.github.progress4j.IProgressView;
/*     */ import com.github.progress4j.IStage;
/*     */ import com.github.progress4j.imp.ProgressStatus;
/*     */ import com.github.signer4j.gui.alert.NoTokenPresentInfo;
/*     */ import com.github.signer4j.gui.alert.PermissionDeniedAlert;
/*     */ import com.github.signer4j.imp.Signer4jContext;
/*     */ import com.github.signer4j.imp.exception.CertificateAliasNotFoundException;
/*     */ import com.github.signer4j.imp.exception.InterruptedOperationException;
/*     */ import com.github.taskresolver4j.ITaskResponse;
/*     */ import com.github.taskresolver4j.exception.TaskDeniedException;
/*     */ import com.github.taskresolver4j.exception.TaskDiscardException;
/*     */ import com.github.taskresolver4j.exception.TaskEscapeException;
/*     */ import com.github.taskresolver4j.exception.TaskException;
/*     */ import com.github.taskresolver4j.exception.TaskExhaustedEscapeException;
/*     */ import com.github.taskresolver4j.exception.TaskParameterInvalidException;
/*     */ import com.github.taskresolver4j.imp.TaskBase;
/*     */ import com.github.utils4j.IDownloadStatus;
/*     */ import com.github.utils4j.gui.IThrowableTracker;
/*     */ import com.github.utils4j.gui.imp.CancelAlert;
/*     */ import com.github.utils4j.gui.imp.DefaultFileChooser;
/*     */ import com.github.utils4j.gui.imp.ExceptionAlert;
/*     */ import com.github.utils4j.gui.imp.SwingTools;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.DownloadStatus;
/*     */ import com.github.utils4j.imp.Params;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import java.awt.Image;
/*     */ import java.io.File;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ abstract class PjeAbstractTask<T>
/*     */   extends TaskBase<IPjeResponse>
/*     */ {
/*  86 */   protected static final Logger LOGGER = LoggerFactory.getLogger(PjeAbstractTask.class); private final boolean isInternalTask; protected static interface IClientConsumer<T> {
/*     */     T accept(IPjeClient param1IPjeClient) throws PjeClientException, InterruptedException; }
/*  88 */   private static final String POJO_REQUEST_PARAM_NAME = PjeAbstractTask.class.getSimpleName() + ".pojo";
/*     */   
/*     */   private enum Stage
/*     */     implements IStage {
/*  92 */     PREPARING_PAYLOAD_PARAMETERS("Validação dos parâmetros principais"),
/*     */     
/*  94 */     PREPARING_TASK_PARAMETERS("Validação dos parâmetros da tarefa"),
/*     */     
/*  96 */     PERMISSION_CHECKING("Checagem de permissões"),
/*     */     
/*  98 */     TASK_EXECUTION("Execução da tarefa"),
/*     */     
/* 100 */     DOWNLOADING("Baixando");
/*     */     
/*     */     private final String message;
/*     */     
/*     */     Stage(String message) {
/* 105 */       this.message = message;
/*     */     }
/*     */ 
/*     */     
/*     */     public final String toString() {
/* 110 */       return this.message;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected PjeAbstractTask(Params request, T pojo) {
/* 117 */     this(request, pojo, false);
/*     */   }
/*     */   
/*     */   protected PjeAbstractTask(Params request, T pojo, boolean isInternalTask) {
/* 121 */     super(request.of(POJO_REQUEST_PARAM_NAME, pojo));
/* 122 */     this.isInternalTask = isInternalTask;
/*     */   }
/*     */   
/*     */   protected boolean forTest() {
/* 126 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected final Image getIcon() {
/* 131 */     return PjeConfig.getIcon();
/*     */   }
/*     */   
/*     */   protected final boolean isInternal() {
/* 135 */     return this.isInternalTask;
/*     */   }
/*     */   
/*     */   private final boolean isPreflightableRequest() {
/* 139 */     return getPayload().isFromPreflightableRequest();
/*     */   }
/*     */   
/*     */   private final String getServerAddress() {
/* 143 */     return getPayload().getServidor().get();
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getId() {
/* 148 */     return getPayload().getTarefaId().get();
/*     */   }
/*     */   
/*     */   private final IPayload getPayload() {
/* 152 */     return (IPayload)getParameterValue(IPayload.PJE_PAYLOAD_REQUEST_PARAM);
/*     */   }
/*     */   
/*     */   private final String getEndpointFor(String sendTo) {
/* 156 */     return getServerAddress() + sendTo;
/*     */   }
/*     */   
/*     */   protected final IPjeEndpoint getTarget(String url) {
/* 160 */     return new PjeTarget(getEndpointFor(url), getSession());
/*     */   }
/*     */   
/*     */   protected final IPjeEndpoint getExternalTarget(String url) {
/* 164 */     return new PjeTarget(url, "");
/*     */   }
/*     */   
/*     */   private final String getSession() {
/* 168 */     return getPayload().getSessao().orElse(Strings.empty());
/*     */   }
/*     */   
/*     */   private final PjeTokenDevice getTokenDevice() {
/* 172 */     return (PjeTokenDevice)getParameterValue(ITaskExecutorParams.PJE_TOKEN_ACCESSOR);
/*     */   }
/*     */   
/*     */   protected final ReentrantLock tokenLock() {
/* 176 */     return getTokenDevice().getLock();
/*     */   }
/*     */   
/*     */   private ExecutorService getRequestExecutor() {
/* 180 */     return (ExecutorService)getParameterValue(ITaskExecutorParams.PJE_REQUEST_EXECUTOR);
/*     */   }
/*     */   
/*     */   protected final IPjeRequest getNativeRequest() {
/* 184 */     return (IPjeRequest)getParameterValue(IPjeRequest.PJE_REQUEST_INSTANCE);
/*     */   }
/*     */   
/*     */   protected final void runAsync(Runnable runnable) {
/* 188 */     getRequestExecutor().execute(runnable);
/*     */   }
/*     */   
/*     */   protected final IPjeSecurityGrantor getSecurityGrantor() {
/* 192 */     return (IPjeSecurityGrantor)getParameterValue(ITaskExecutorParams.PJE_SECURITY_GRANTOR);
/*     */   }
/*     */   
/*     */   protected final void forceLogout() throws InterruptedException {
/* 196 */     Signer4jContext.discardAndWaitIf(this::isBatchState);
/* 197 */     getTokenDevice().logout();
/*     */   }
/*     */   
/*     */   protected final IPjeToken loginToken() {
/* 201 */     return (IPjeToken)getTokenDevice().call();
/*     */   }
/*     */   
/*     */   private final IPjeClient getPjeClient() {
/* 205 */     return PjeClientProtocol.clientFrom(getServerAddress(), getCanceller());
/*     */   }
/*     */   
/*     */   protected final T getPojoParams() {
/* 209 */     return (T)getParameterValue(POJO_REQUEST_PARAM_NAME);
/*     */   }
/*     */   
/*     */   protected final ITaskResponse<IPjeResponse> fail(Throwable exception) {
/* 213 */     return PjeClientProtocol.failFrom(getServerAddress()).apply(exception);
/*     */   }
/*     */   
/*     */   protected final PjeTaskResponse success() {
/* 217 */     return PjeClientProtocol.successFrom(getServerAddress()).apply("success");
/*     */   }
/*     */   
/*     */   protected final PjeTaskResponse success(String output) {
/* 221 */     return PjeClientProtocol.successFrom(getServerAddress()).apply("success: " + output);
/*     */   }
/*     */   
/*     */   protected final Optional<File> download(IPjeEndpoint target) throws TaskException, InterruptedException {
/* 225 */     return download(target, (File)null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Runnable getAlertFailCode(String message, String detail, Throwable cause) {
/* 230 */     return () -> ExceptionAlert.show(getIcon(), message, detail, cause, (IThrowableTracker)PjeServerTracker.RESPONSE);
/*     */   }
/*     */   
/*     */   protected final void showNoTokenPresent() {
/* 234 */     ifNotClosing(NoTokenPresentInfo::showInfoOnly);
/*     */   }
/*     */   
/*     */   protected final Optional<File> download(IPjeEndpoint target, File saveHere) throws TaskException, InterruptedException {
/* 238 */     Args.requireNonNull(target, "target is null");
/* 239 */     IProgressView iProgressView = getProgress();
/* 240 */     ProgressStatus progressStatus = new ProgressStatus((IProgress)iProgressView, Stage.DOWNLOADING, saveHere);
/*     */     try {
/* 242 */       withClient(c -> {
/*     */             c.download(target, (IDownloadStatus)status);
/*     */             return null;
/*     */           });
/* 246 */     } catch (PjeClientException e) {
/* 247 */       iProgressView.abort((Throwable)e);
/*     */     } 
/* 249 */     return progressStatus.getDownloadedFile();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final <T> T withClient(IClientConsumer<T> consumer) throws TaskException, InterruptedException, PjeClientException {
/*     */     try {
/* 259 */       return consumer.accept(getPjeClient());
/* 260 */     } catch (InterruptedException e) {
/* 261 */       throw e;
/* 262 */     } catch (PjeClientConnectionTimeoutException e) {
/* 263 */       throw new TaskExhaustedConnectionException(e);
/* 264 */     } catch (PjeClientException e) {
/* 265 */       throw e;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void validatePayloadParams() throws TaskException, InterruptedException {
/* 270 */     if (!this.isInternalTask) {
/* 271 */       IPayload payload = getPayload();
/* 272 */       PjeTaskChecker.checkIfPresent(payload.getServidor(), "servidor");
/* 273 */       PjeTaskChecker.checkIfPresent(payload.getCodigoSeguranca(), "codigoSeguranca");
/* 274 */       PjeTaskChecker.checkIfPresent(payload.getAplicacao(), "aplicacao");
/*     */     } 
/*     */   }
/*     */   
/*     */   protected final File[] selectFilesFromDialog(String title) throws InterruptedException {
/* 279 */     Optional<File[]> files = SwingTools.invokeAndWait(() -> {
/*     */           DefaultFileChooser chooser = new DefaultFileChooser();
/*     */           
/*     */           chooser.setFileSelectionMode(0);
/*     */           
/*     */           chooser.setMultiSelectionEnabled(true);
/*     */           
/*     */           chooser.setDialogTitle(title);
/*     */           return (1 == chooser.showOpenDialog(null)) ? null : chooser.getSelectedFiles();
/*     */         });
/* 289 */     if (!files.isPresent())
/* 290 */       throwCancel(); 
/* 291 */     return files.get();
/*     */   }
/*     */   
/*     */   private final void checkServerPermission() throws TaskDeniedException {
/* 295 */     if (this.isInternalTask) {
/* 296 */       if (!forTest() && !getNativeRequest().isInternal()) {
/* 297 */         throw new TaskDeniedException("Permissão negada. Tarefa deve ser executada apenas em contexto interno/local.");
/*     */       }
/*     */       return;
/*     */     } 
/* 301 */     StringBuilder whyNot = new StringBuilder();
/* 302 */     if (!getSecurityGrantor().isPermitted(getPayload(), whyNot)) {
/* 303 */       String cause = whyNot.toString();
/* 304 */       if (!cause.isEmpty()) {
/* 305 */         LOGGER.warn(cause);
/* 306 */         ifNotClosing(() -> PermissionDeniedAlert.showInfo(cause));
/*     */       } 
/* 308 */       throw new TaskDeniedException("Permissão negada. " + cause);
/*     */     } 
/*     */   }
/*     */   
/*     */   public final ITaskResponse<IPjeResponse> get() throws TaskDiscardException {
/*     */     Throwable fail;
/* 314 */     IProgressView iProgressView = getProgress();
/*     */     
/*     */     try {
/* 317 */       iProgressView.begin(Stage.PREPARING_PAYLOAD_PARAMETERS, 2);
/* 318 */       iProgressView.step("Preparando parâmetros de execução principais", new Object[0]);
/* 319 */       validatePayloadParams();
/* 320 */       iProgressView.step("Principais parâmetros validados", new Object[0]);
/* 321 */       iProgressView.end();
/*     */       
/* 323 */       iProgressView.begin(Stage.PERMISSION_CHECKING, 2);
/* 324 */       iProgressView.step("Checando permissões de acesso ao servidor", new Object[0]);
/* 325 */       checkServerPermission();
/* 326 */       iProgressView.step("Acesso permitido", new Object[0]);
/* 327 */       iProgressView.end();
/*     */       
/* 329 */       iProgressView.begin(Stage.PREPARING_TASK_PARAMETERS, 2);
/* 330 */       iProgressView.step("Preparando parâmetros de execução da tarefa", new Object[0]);
/* 331 */       validateTaskParams();
/* 332 */       iProgressView.step("Parâmetros da tarefa validados", new Object[0]);
/* 333 */       iProgressView.end();
/*     */       
/* 335 */       iProgressView.begin(Stage.TASK_EXECUTION, 2);
/* 336 */       iProgressView.step("Executando a tarefa '%s'", new Object[] { getId() });
/*     */       
/* 338 */       onBeforeDoGet();
/*     */       
/* 340 */       ITaskResponse<IPjeResponse> response = doGet();
/* 341 */       iProgressView.step("Tarefa completa. Status de sucesso: %s", new Object[] { Boolean.valueOf(response.isSuccess()) });
/* 342 */       iProgressView.end();
/*     */       
/* 344 */       return isPreflightableRequest() ? response.asJson() : response;
/*     */     }
/* 346 */     catch (InterruptedOperationException e) {
/* 347 */       showCancel();
/* 348 */       throw new TaskEscapeException(CancelAlert.CANCELED_OPERATION_MESSAGE, e);
/*     */     }
/* 350 */     catch (TaskExhaustedConnectionException e) {
/* 351 */       showFail("Pool de conexões precisou ser reciclado", (Throwable)e);
/* 352 */       throw new TaskExhaustedEscapeException(e);
/*     */     }
/* 354 */     catch (InterruptedException e) {
/* 355 */       ifBatchStateThrowDiscard(CancelAlert.CANCELED_OPERATION_MESSAGE);
/* 356 */       fail = iProgressView.abort(e);
/* 357 */       showCancel();
/*     */     }
/* 359 */     catch (TaskDiscardException e) {
/* 360 */       throw e;
/*     */     }
/* 362 */     catch (TaskParameterInvalidException e) {
/* 363 */       String message = "Parâmetros inválidos. "; TaskParameterInvalidException taskParameterInvalidException1;
/* 364 */       LOGGER.warn(message, (Throwable)(taskParameterInvalidException1 = e));
/* 365 */       showFail(message + getSupport(), (Throwable)e);
/*     */     }
/* 367 */     catch (CertificateAliasNotFoundException e) {
/* 368 */       LOGGER.warn("Alias do certificado não encontrado", (Throwable)e);
/* 369 */       showNoTokenPresent();
/* 370 */       throw new TaskEscapeException(CancelAlert.CANCELED_OPERATION_MESSAGE, e);
/*     */     }
/* 372 */     catch (RuntimeException e) {
/* 373 */       String message = "Houve um erro na execução da tarefa. ";
/* 374 */       LOGGER.error(message, fail = e);
/* 375 */       showFail(message + getSupport(), e);
/*     */     }
/* 377 */     catch (Exception e) {
/* 378 */       fail = iProgressView.abort(e);
/* 379 */       LOGGER.warn("Falha na execução da tarefa" + getId(), fail);
/*     */     } 
/*     */     
/* 382 */     return isPreflightableRequest() ? fail(fail).asJson() : fail(fail);
/*     */   }
/*     */   
/*     */   protected void onBeforeDoGet() throws TaskException, InterruptedException {}
/*     */   
/*     */   protected abstract void validateTaskParams() throws TaskException, InterruptedException;
/*     */   
/*     */   protected abstract ITaskResponse<IPjeResponse> doGet() throws TaskException, InterruptedException;
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/PjeAbstractTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */