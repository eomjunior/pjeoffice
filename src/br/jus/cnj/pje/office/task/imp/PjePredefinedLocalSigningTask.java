/*     */ package br.jus.cnj.pje.office.task.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.task.ISignableURLDocument;
/*     */ import br.jus.cnj.pje.office.task.ISignedURLDocument;
/*     */ import br.jus.cnj.pje.office.task.ITarefaAssinador;
/*     */ import br.jus.cnj.pje.office.task.IURLOutputDocument;
/*     */ import com.github.progress4j.IProgressView;
/*     */ import com.github.progress4j.IStage;
/*     */ import com.github.taskresolver4j.exception.TaskException;
/*     */ import com.github.utils4j.IConstants;
/*     */ import com.github.utils4j.imp.Dates;
/*     */ import com.github.utils4j.imp.Params;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import java.io.File;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URLDecoder;
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
/*     */ 
/*     */ class PjePredefinedLocalSigningTask
/*     */   extends PjeLocalSigningTask
/*     */ {
/*     */   private String enviarPara;
/*     */   private List<IURLOutputDocument> arquivos;
/*     */   
/*     */   private enum Stage
/*     */     implements IStage
/*     */   {
/*  53 */     SELECTING_FILE;
/*     */     
/*     */     public String toString() {
/*  56 */       return "Selecionando arquivos";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PjePredefinedLocalSigningTask(Params request, ITarefaAssinador pojo) {
/*  65 */     super(request, pojo);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void validateTaskParams() throws TaskException {
/*  70 */     super.validateTaskParams();
/*  71 */     ITarefaAssinador pojo = getPojoParams();
/*  72 */     this.arquivos = PjeTaskChecker.checkIfNotEmpty(pojo.getArquivos(), "arquivos");
/*  73 */     this.enviarPara = PjeTaskChecker.<String>checkIfPresent(pojo.getEnviarPara(), "enviarPara");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected ISignedURLDocument[] selectFiles() throws TaskException, InterruptedException {
/*  79 */     int size = this.arquivos.size();
/*     */     
/*  81 */     List<File> inputFiles = new ArrayList<>();
/*     */     
/*  83 */     IProgressView iProgressView = getProgress();
/*     */     
/*  85 */     iProgressView.begin(Stage.SELECTING_FILE, size);
/*     */     
/*  87 */     int i = 0;
/*     */     do {
/*  89 */       IURLOutputDocument arquivo = this.arquivos.get(i);
/*  90 */       Optional<String> oUrl = arquivo.getUrl();
/*  91 */       if (!oUrl.isPresent())
/*  92 */       { LOGGER.warn("Detectado arquivo com caminho vazio");
/*  93 */         iProgressView.step("Decartado arquivo com url vazia", new Object[0]); }
/*     */       else
/*     */       
/*  96 */       { File file = new File((String)Throwables.call(() -> URLDecoder.decode(oUrl.get(), IConstants.UTF_8.name()), oUrl.get()));
/*  97 */         if (!file.exists())
/*  98 */         { String fullPath = file.getAbsolutePath();
/*  99 */           LOGGER.warn("Detectado arquivo com caminho inexistente {}", fullPath);
/* 100 */           iProgressView.step("Descartado arquivo não localizado '%s'", new Object[] { fullPath }); }
/*     */         else
/*     */         
/* 103 */         { iProgressView.step("Selecionando arquivo: %s", new Object[] { file });
/* 104 */           inputFiles.add(file); }  } 
/* 105 */     } while (++i < size);
/*     */     
/* 107 */     iProgressView.end();
/* 108 */     return collectFiles(inputFiles.<File>toArray(new File[inputFiles.size()]));
/*     */   }
/*     */ 
/*     */   
/*     */   protected File chooseDestination() throws InterruptedException {
/*     */     File folderReference;
/* 114 */     if ("selectfolder".equals(this.enviarPara)) {
/* 115 */       return super.chooseDestination();
/*     */     }
/*     */ 
/*     */     
/* 119 */     Optional<ISignableURLDocument> primeiro = getFirst();
/* 120 */     if (primeiro.isPresent()) {
/* 121 */       folderReference = (new File(((ISignableURLDocument)primeiro.get()).getUrl().get())).getParentFile();
/*     */     } else {
/* 123 */       return super.chooseDestination();
/*     */     } 
/*     */     
/* 126 */     if ("newfolder".equals(this.enviarPara)) {
/* 127 */       folderReference = new File(folderReference, "ASSINADOS_EM_" + Dates.stringNow());
/* 128 */       folderReference.mkdirs();
/*     */     } 
/*     */     
/*     */     while (true) {
/* 132 */       if (folderReference.exists() && folderReference.canWrite())
/* 133 */         return folderReference; 
/* 134 */       showCanNotWriteMessage(folderReference);
/* 135 */       folderReference = super.chooseDestination();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/PjePredefinedLocalSigningTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */