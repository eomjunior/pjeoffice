/*     */ package br.jus.cnj.pje.office.task.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.task.ITarefaPdfDivisaoContagem;
/*     */ import com.github.filehandler4j.IInputDescriptor;
/*     */ import com.github.filehandler4j.IInputFile;
/*     */ import com.github.filehandler4j.imp.FileWrapper;
/*     */ import com.github.filehandler4j.imp.InputDescriptor;
/*     */ import com.github.pdfhandler4j.IPdfInfoEvent;
/*     */ import com.github.pdfhandler4j.imp.ByCountPdfSplitter;
/*     */ import com.github.pdfhandler4j.imp.BySinglePagePdfSplitter;
/*     */ import com.github.pdfhandler4j.imp.PdfInputDescriptor;
/*     */ import com.github.pdfhandler4j.imp.event.PdfStartEvent;
/*     */ import com.github.progress4j.IQuietlyProgress;
/*     */ import com.github.taskresolver4j.exception.TaskException;
/*     */ import com.github.utils4j.gui.imp.Dialogs;
/*     */ import com.github.utils4j.gui.imp.SwingTools;
/*     */ import com.github.utils4j.imp.Directory;
/*     */ import com.github.utils4j.imp.Params;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.function.Supplier;
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
/*     */ class PjeByCountPdfSplitterTask
/*     */   extends PjeMediaProcessingTask<ITarefaPdfDivisaoContagem>
/*     */ {
/*     */   private long totalPaginas;
/*     */   
/*     */   protected PjeByCountPdfSplitterTask(Params request, ITarefaPdfDivisaoContagem pojo) {
/*  62 */     super(request, pojo);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doValidateTaskParams() throws TaskException, InterruptedException {
/*  67 */     this.totalPaginas = getPojoParams().getTotalPaginas();
/*  68 */     if (this.totalPaginas <= 0L) {
/*  69 */       Optional<Integer> total = SwingTools.invokeAndWait(() -> Dialogs.getInteger("Número máximo de páginas:", Integer.valueOf(10), Integer.valueOf(1), Integer.valueOf(2147483646)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  77 */       this.totalPaginas = ((Integer)total.<Throwable>orElseThrow(InterruptedException::new)).intValue();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean process(Path file, IQuietlyProgress progress) {
/*     */     InputDescriptor desc;
/*  83 */     Path parentFolder = file.getParent();
/*  84 */     FileWrapper fileWrapper = new FileWrapper(file.toFile());
/*     */     
/*  86 */     Path outputFolder = parentFolder.resolve(fileWrapper.getShortName() + "_(VOLUMES DE " + this.totalPaginas + " PÁGINA" + ((this.totalPaginas > 1L) ? "S)" : ")"));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  92 */       desc = (InputDescriptor)(new PdfInputDescriptor.Builder()).add((IInputFile)fileWrapper).output(outputFolder).build();
/*  93 */     } catch (IOException e1) {
/*  94 */       progress.abort(e1);
/*  95 */       LOGGER.error("Não foi possível criar pasta " + parentFolder.toString(), e1);
/*  96 */       return false;
/*     */     } 
/*     */     
/*  99 */     ByCountPdfSplitter splitter = (this.totalPaginas == 1L) ? (ByCountPdfSplitter)new BySinglePagePdfSplitter() : new ByCountPdfSplitter(this.totalPaginas);
/*     */ 
/*     */ 
/*     */     
/* 103 */     AtomicBoolean success = new AtomicBoolean(true);
/*     */     
/* 105 */     splitter.apply((IInputDescriptor)desc).subscribe(e -> {
/*     */           if (e instanceof com.github.pdfhandler4j.imp.event.PdfReadingStart) {
/*     */             progress.begin(PjeMediaProcessingTask.SplitterStage.READING);
/*     */           } else if (e instanceof PdfStartEvent) {
/*     */             progress.begin(PjeMediaProcessingTask.SplitterStage.SPLITING, ((PdfStartEvent)e).getTotalPages());
/*     */           } else if (e instanceof com.github.pdfhandler4j.imp.event.PdfReadingEnd || e instanceof com.github.pdfhandler4j.imp.event.PdfEndEvent) {
/*     */             progress.end();
/*     */           } else if (e instanceof com.github.pdfhandler4j.imp.event.PdfPageEvent) {
/*     */             progress.step(e.getMessage(), new Object[0]);
/*     */           } else if (e instanceof com.github.pdfhandler4j.imp.event.PdfOutputEvent && this.totalPaginas == 1L) {
/*     */             progress.step(e.getMessage(), new Object[0]);
/*     */           } else {
/*     */             progress.info(e.getMessage(), new Object[0]);
/*     */           } 
/*     */         }e -> {
/*     */           success.set(false);
/*     */           
/*     */           Directory.deleteQuietly(outputFolder.toFile());
/*     */           
/*     */           progress.abort(e);
/*     */         });
/*     */     
/* 127 */     return success.get();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/PjeByCountPdfSplitterTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */