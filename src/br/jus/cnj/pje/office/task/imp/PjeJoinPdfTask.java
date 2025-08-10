/*     */ package br.jus.cnj.pje.office.task.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.IPjeResponse;
/*     */ import br.jus.cnj.pje.office.task.ITarefaMedia;
/*     */ import com.github.filehandler4j.IInputDescriptor;
/*     */ import com.github.filehandler4j.imp.InputDescriptor;
/*     */ import com.github.pdfhandler4j.IPdfInfoEvent;
/*     */ import com.github.pdfhandler4j.imp.JoinPdfHandler;
/*     */ import com.github.pdfhandler4j.imp.PdfInputDescriptor;
/*     */ import com.github.progress4j.IProgress;
/*     */ import com.github.progress4j.IProgressView;
/*     */ import com.github.progress4j.IQuietlyProgress;
/*     */ import com.github.progress4j.IStage;
/*     */ import com.github.progress4j.imp.QuietlyProgress;
/*     */ import com.github.taskresolver4j.ITaskResponse;
/*     */ import com.github.taskresolver4j.exception.TaskException;
/*     */ import com.github.utils4j.gui.imp.Dialogs;
/*     */ import com.github.utils4j.gui.imp.FileListWindow;
/*     */ import com.github.utils4j.gui.imp.SwingTools;
/*     */ import com.github.utils4j.imp.Directory;
/*     */ import com.github.utils4j.imp.Media;
/*     */ import com.github.utils4j.imp.Params;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.stream.Collectors;
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
/*     */ class PjeJoinPdfTask
/*     */   extends PjeAbstractMediaTask<ITarefaMedia>
/*     */ {
/*     */   private enum Stage
/*     */     implements IStage
/*     */   {
/*  63 */     MERGING;
/*     */     
/*     */     public final String toString() {
/*  66 */       return "Unindo arquivos";
/*     */     }
/*     */   }
/*     */   
/*     */   protected PjeJoinPdfTask(Params request, ITarefaMedia pojo) {
/*  71 */     super(request, pojo);
/*     */   }
/*     */   
/*     */   protected ITaskResponse<IPjeResponse> doGet() throws TaskException, InterruptedException {
/*     */     File outputFile;
/*     */     InputDescriptor desc;
/*  77 */     IProgressView iProgressView = getProgress();
/*     */     
/*  79 */     iProgressView.info("Aguardando ordenação dos arquivos", new Object[0]);
/*     */     
/*  81 */     AtomicReference<Path> parent = new AtomicReference<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  89 */     List<File> files = (List<File>)this.arquivos.stream().filter(Strings::hasText).map(s -> new File(s)).filter(File::exists).peek(f -> parent.set(f.toPath().getParent())).sorted((a, b) -> a.getName().compareTo(b.getName())).collect(Collectors.toList());
/*     */     
/*  91 */     int size = files.size();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/*  97 */       Optional<File> outputFileOpt = SwingTools.invokeAndWaitT(new FileListWindow(
/*  98 */             getIcon(), files, Media.PDF, ((Path)parent
/*     */ 
/*     */             
/* 101 */             .get()).toFile())::getOutputFile);
/*     */ 
/*     */ 
/*     */       
/* 105 */       if (!outputFileOpt.isPresent()) {
/* 106 */         throwCancel();
/*     */       }
/*     */       
/* 109 */       outputFile = outputFileOpt.get();
/*     */       
/* 111 */       if (!outputFile.exists()) {
/*     */         break;
/*     */       }
/* 114 */       Path outFilePath = outputFile.toPath();
/*     */       
/* 116 */       if (files.stream().filter(f -> Directory.isSameFile(f.toPath(), outFilePath)).findAny().isPresent()) {
/* 117 */         Dialogs.info("Arquivo '" + outputFile.getName() + "' não pode ser usado\ncomo arquivo final porque já é utilizado para união/mesclagem.");
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 122 */       Dialogs.Choice override = Dialogs.getBoolean("Arquivo '" + outputFile.getName() + "' já existe!\nDeseja sobrescrever?", "Atenção!", false);
/*     */ 
/*     */       
/* 125 */       if (override == Dialogs.Choice.YES) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/* 130 */     Path outputFolder = outputFile.getParentFile().toPath();
/*     */     
/* 132 */     iProgressView.info("Arquivos ordenados", new Object[0]);
/*     */     
/* 134 */     PdfInputDescriptor.Builder builder = new PdfInputDescriptor.Builder();
/*     */     
/* 136 */     files.forEach(builder::add);
/*     */     
/* 138 */     iProgressView.begin(Stage.MERGING, 4 * size + 1);
/*     */ 
/*     */     
/*     */     try {
/* 142 */       desc = (InputDescriptor)builder.output(outputFolder).build();
/* 143 */     } catch (IOException e) {
/* 144 */       throw (TaskException)iProgressView.abort(showFail("Não foi possível gerar arquivo de saída.", outputFolder + " é pasta válida com permissão de escrita?", e));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 150 */     AtomicBoolean success = new AtomicBoolean(true);
/* 151 */     String simpleName = outputFile.getName();
/* 152 */     simpleName = simpleName.substring(0, simpleName.lastIndexOf('.'));
/*     */     
/* 154 */     IQuietlyProgress iQuietlyProgress = QuietlyProgress.wrap((IProgress)iProgressView);
/* 155 */     (new JoinPdfHandler(simpleName))
/* 156 */       .apply((IInputDescriptor)desc)
/* 157 */       .subscribe(e -> quietly.step(e.getMessage(), new Object[0]), e -> {
/*     */           quietly.abort(e);
/*     */ 
/*     */           
/*     */           success.set(false);
/*     */         });
/*     */ 
/*     */     
/* 165 */     if (!success.get()) {
/* 166 */       iProgressView.throwIfInterrupted();
/* 167 */       throw showFail("Não foi possível unir os arquivos.", iProgressView.getAbortCause());
/*     */     } 
/*     */     
/* 170 */     iProgressView.info("Unidos " + size + " arquivos", new Object[0]);
/*     */     
/* 172 */     iProgressView.end();
/*     */     
/* 174 */     showInfo("Arquivos unidos com sucesso.", "Ótimo!");
/* 175 */     return (ITaskResponse<IPjeResponse>)success();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/PjeJoinPdfTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */