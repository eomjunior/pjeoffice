/*     */ package br.jus.cnj.pje.office.task.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.IPjeClient;
/*     */ import br.jus.cnj.pje.office.core.imp.PjeClientException;
/*     */ import br.jus.cnj.pje.office.core.imp.PjeTaskResponse;
/*     */ import br.jus.cnj.pje.office.task.IPjeEndpoint;
/*     */ import br.jus.cnj.pje.office.task.ISignableURLDocument;
/*     */ import br.jus.cnj.pje.office.task.ISignedURLDocument;
/*     */ import br.jus.cnj.pje.office.task.ITarefaAssinador;
/*     */ import br.jus.cnj.pje.office.task.IURLOutputDocument;
/*     */ import com.github.progress4j.IProgress;
/*     */ import com.github.progress4j.IProgressView;
/*     */ import com.github.progress4j.IStage;
/*     */ import com.github.taskresolver4j.exception.TaskException;
/*     */ import com.github.utils4j.IContentType;
/*     */ import com.github.utils4j.IDisposable;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Params;
/*     */ import java.io.File;
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
/*     */ 
/*     */ class PjeRemoteSigningTask
/*     */   extends PjeSigningTask
/*     */ {
/*     */   private enum Stage
/*     */     implements IStage
/*     */   {
/*  52 */     DOWNLOADING_FILES;
/*     */ 
/*     */     
/*     */     public String toString() {
/*  56 */       return "Download dos arquivos";
/*     */     }
/*     */   }
/*     */   
/*  60 */   private final List<ISignableURLDocument> tempFiles = new ArrayList<>();
/*     */   
/*     */   private List<IURLOutputDocument> arquivos;
/*     */   
/*     */   private String enviarPara;
/*     */   
/*     */   PjeRemoteSigningTask(Params request, ITarefaAssinador pojo) {
/*  67 */     super(request, pojo, false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void validateTaskParams() throws TaskException {
/*  72 */     super.validateTaskParams();
/*  73 */     ITarefaAssinador params = getPojoParams();
/*  74 */     this.arquivos = PjeTaskChecker.<List<IURLOutputDocument>>checkIfNull(params.getArquivos(), "arquivos");
/*  75 */     this.enviarPara = PjeTaskChecker.<String>checkIfPresent(params.getEnviarPara(), "enviarPara");
/*     */   }
/*     */ 
/*     */   
/*     */   protected ISignedURLDocument[] selectFiles() throws TaskException, InterruptedException {
/*  80 */     if (this.arquivos.isEmpty()) {
/*  81 */       throw new TaskException("A requisição não informou qualquer URL para download dos arquivos");
/*     */     }
/*     */     
/*  84 */     int size = this.arquivos.size();
/*     */     
/*  86 */     IProgressView iProgressView = getProgress();
/*     */     
/*  88 */     iProgressView.begin(Stage.DOWNLOADING_FILES, size);
/*     */     
/*  90 */     int i = 0;
/*     */     do {
/*  92 */       IURLOutputDocument arquivo = this.arquivos.get(i);
/*     */       
/*  94 */       Optional<String> oUrl = arquivo.getUrl();
/*     */       
/*  96 */       if (!oUrl.isPresent()) {
/*  97 */         LOGGER.warn("Detectado arquivo com URL para download VAZIA");
/*  98 */         iProgressView.step("Decartado arquivo com url vazia", new Object[0]);
/*     */       }
/*     */       else {
/*     */         
/* 102 */         String url = oUrl.get();
/*     */         
/* 104 */         IPjeEndpoint target = getTarget(url);
/*     */         
/* 106 */         iProgressView.step("URL: %s", new Object[] { target.getPath() });
/*     */         
/* 108 */         File downloaded = download(target).<Throwable>orElseThrow(() -> showFail("Não foi possível baixar o arquivo.", "URL: " + url, progress.getAbortCause()));
/*     */ 
/*     */ 
/*     */         
/* 112 */         this.tempFiles.add(new SignedURLDocument(arquivo, downloaded)
/*     */             {
/*     */               public void dispose() {
/* 115 */                 super.dispose();
/* 116 */                 this.notSignedFile.delete();
/*     */               }
/*     */             });
/*     */       } 
/* 120 */     } while (++i < size);
/*     */     
/* 122 */     iProgressView.end();
/* 123 */     return this.tempFiles.<ISignedURLDocument>toArray(new ISignedURLDocument[this.tempFiles.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 128 */     this.tempFiles.forEach(IDisposable::dispose);
/* 129 */     this.tempFiles.clear();
/* 130 */     super.dispose();
/*     */   }
/*     */ 
/*     */   
/*     */   protected PjeTaskResponse send(ISignableURLDocument arquivo) throws TaskException, InterruptedException {
/* 135 */     Args.requireNonNull(arquivo, "arquivo is null");
/* 136 */     IPjeEndpoint target = getTarget(this.enviarPara);
/*     */     try {
/* 138 */       return (PjeTaskResponse)withClient(c -> c.send(target, arquivo, (IContentType)this.padraoAssinatura));
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 143 */     catch (PjeClientException e) {
/* 144 */       throw new TaskException("Não foi possível enviar o arquivo para o servidor: " + target.getPath(), e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/PjeRemoteSigningTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */