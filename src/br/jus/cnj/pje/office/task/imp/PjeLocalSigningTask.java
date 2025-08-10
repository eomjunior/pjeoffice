/*     */ package br.jus.cnj.pje.office.task.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.IPjeResponse;
/*     */ import br.jus.cnj.pje.office.core.imp.PjeTaskResponse;
/*     */ import br.jus.cnj.pje.office.task.ISignableURLDocument;
/*     */ import br.jus.cnj.pje.office.task.ISignedURLDocument;
/*     */ import br.jus.cnj.pje.office.task.ITarefaAssinador;
/*     */ import com.github.progress4j.IProgressView;
/*     */ import com.github.progress4j.IState;
/*     */ import com.github.signer4j.ISignedData;
/*     */ import com.github.taskresolver4j.ITaskResponse;
/*     */ import com.github.taskresolver4j.exception.TaskException;
/*     */ import com.github.utils4j.gui.imp.DefaultFileChooser;
/*     */ import com.github.utils4j.gui.imp.ExceptionAlert;
/*     */ import com.github.utils4j.gui.imp.SwingTools;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Directory;
/*     */ import com.github.utils4j.imp.Params;
/*     */ import com.github.utils4j.imp.Threads;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class PjeLocalSigningTask
/*     */   extends PjeSigningTask
/*     */ {
/*  61 */   private static final String PJE_DESTINATION_PARAM = PjeLocalSigningTask.class.getSimpleName() + ".destinationDir";
/*     */   
/*     */   PjeLocalSigningTask(Params request, ITarefaAssinador pojo) {
/*  64 */     super(request, pojo, true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void validateTaskParams() throws TaskException {
/*  69 */     super.validateTaskParams();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dispose() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ITaskResponse<IPjeResponse> doGet() throws TaskException {
/*  81 */     runAsync(() -> {
/*     */           Threads.sleep(100L);
/*     */           IProgressView progress = getProgress();
/*     */           try {
/*     */             progress.display();
/*     */             super.doGet();
/*  87 */           } catch (InterruptedException|com.github.signer4j.imp.exception.InterruptedOperationException e) {
/*     */             LOGGER.warn("Tarefa foi interrompida", e);
/*     */             progress.abort(e);
/*     */             showCancel();
/*  91 */           } catch (TaskException e) {
/*     */             LOGGER.error("Falha na execução da tarefa", (Throwable)e);
/*  93 */           } catch (Throwable e) {
/*     */             String message = "Houve uma falha inexperada durante o processo de assinatura! ";
/*     */             LOGGER.warn(message, e);
/*     */             ifNotClosing(());
/*     */           } finally {
/*     */             try {
/*     */               progress.undisplay();
/*     */               progress.stackTracer(());
/*     */             } finally {
/*     */               progress.dispose();
/*     */               progress = null;
/*     */               super.dispose();
/*     */             } 
/*     */           } 
/*     */         });
/* 108 */     return (ITaskResponse<IPjeResponse>)success();
/*     */   }
/*     */ 
/*     */   
/*     */   protected ISignedURLDocument[] selectFiles() throws TaskException, InterruptedException {
/* 113 */     return collectFiles(selectFilesFromDialog("Selecione o(s) arquivo(s) a ser(em) assinado(s)"));
/*     */   }
/*     */   
/*     */   protected ISignedURLDocument[] collectFiles(File[] files) throws TaskException {
/*     */     int size;
/* 118 */     if (files == null || (size = files.length) == 0) {
/* 119 */       throw new TaskException("Nenhum arquivo selecionado");
/*     */     }
/* 121 */     ISignedURLDocument[] filesToSign = new ISignedURLDocument[size];
/* 122 */     int i = 0;
/* 123 */     for (File file : files) {
/* 124 */       filesToSign[i++] = new SignedURLDocument(TarefaAssinadorReader.URLOutputDocument.newInstance(file), file);
/*     */     }
/* 126 */     return filesToSign;
/*     */   }
/*     */   
/*     */   protected File chooseDestination() throws InterruptedException {
/* 130 */     Optional<File> file = SwingTools.invokeAndWait(() -> {
/*     */           DefaultFileChooser defaultFileChooser = new DefaultFileChooser();
/*     */           defaultFileChooser.setFileSelectionMode(1);
/*     */           defaultFileChooser.setDialogTitle("Selecione onde será(ão) gravado(s) o(s) arquivo(s) assinado(s)");
/*     */           switch (defaultFileChooser.showOpenDialog(null)) {
/*     */             case 0:
/*     */               return defaultFileChooser.getSelectedFile();
/*     */           } 
/*     */           
/*     */           return null;
/*     */         });
/* 141 */     if (!file.isPresent())
/* 142 */       throwCancel(); 
/* 143 */     return file.get();
/*     */   }
/*     */   
/*     */   protected PjeTaskResponse send(ISignableURLDocument document) throws TaskException, InterruptedException {
/*     */     File destination;
/* 148 */     Args.requireNonNull(document, "arquivo is null");
/*     */     
/* 150 */     ISignedData signedData = (ISignedData)document.getSignedData().orElseThrow(() -> new TaskException("Arquivo não foi assinado!"));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 155 */     Params params = getParams();
/*     */ 
/*     */     
/*     */     while (true) {
/* 159 */       destination = params.isPresent(PJE_DESTINATION_PARAM) ? (File)params.getValue(PJE_DESTINATION_PARAM) : chooseDestination();
/* 160 */       if (destination.canWrite())
/*     */         break; 
/* 162 */       showCanNotWriteMessage(destination);
/* 163 */       params.of(PJE_DESTINATION_PARAM, Optional.empty());
/*     */     } 
/*     */     
/* 166 */     params.of(PJE_DESTINATION_PARAM, destination);
/*     */     
/* 168 */     File saved = new File(destination, document.getNome().get());
/* 169 */     try (OutputStream output = new FileOutputStream(saved)) {
/* 170 */       signedData.writeTo(output);
/* 171 */     } catch (IOException e) {
/* 172 */       saved.delete();
/* 173 */       throw new TaskException("Não foi possível salvar o arquivo assinado.", e);
/*     */     } 
/*     */     
/* 176 */     return success(document.getUrl().get());
/*     */   }
/*     */   
/*     */   protected void showCanNotWriteMessage(File destination) {
/* 180 */     showInfo("Não há permissão de escrita na pasta:\n" + Directory.stringPath(destination) + "\nEscolha uma nova!");
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/PjeLocalSigningTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */