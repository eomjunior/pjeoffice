/*     */ package br.jus.cnj.pje.office.task.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.task.ITarefaPdfDivisaoTamanho;
/*     */ import com.github.filehandler4j.IInputDescriptor;
/*     */ import com.github.filehandler4j.IInputFile;
/*     */ import com.github.filehandler4j.imp.FileWrapper;
/*     */ import com.github.filehandler4j.imp.InputDescriptor;
/*     */ import com.github.pdfhandler4j.IPdfInfoEvent;
/*     */ import com.github.pdfhandler4j.imp.BySizePdfSplitter;
/*     */ import com.github.pdfhandler4j.imp.PdfInputDescriptor;
/*     */ import com.github.pdfhandler4j.imp.event.PdfStartEvent;
/*     */ import com.github.progress4j.IQuietlyProgress;
/*     */ import com.github.taskresolver4j.exception.TaskException;
/*     */ import com.github.utils4j.gui.imp.Dialogs;
/*     */ import com.github.utils4j.gui.imp.SwingTools;
/*     */ import com.github.utils4j.imp.Directory;
/*     */ import com.github.utils4j.imp.Params;
/*     */ import com.github.utils4j.imp.Sizes;
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
/*     */ 
/*     */ class PjeBySizePdfSplitterTask
/*     */   extends PjeMediaProcessingTask<ITarefaPdfDivisaoTamanho>
/*     */ {
/*     */   private double tamanho;
/*     */   
/*     */   protected PjeBySizePdfSplitterTask(Params request, ITarefaPdfDivisaoTamanho pojo) {
/*  63 */     super(request, pojo);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doValidateTaskParams() throws TaskException, InterruptedException {
/*  68 */     this.tamanho = getPojoParams().getTamanho();
/*  69 */     if (this.tamanho == 0.0D) {
/*  70 */       Optional<Double> total = SwingTools.invokeAndWait(() -> Dialogs.getDouble("Tamanho máximo do arquivo (MB):", Double.valueOf(10.0D), Double.valueOf(0.1D), Double.valueOf(Sizes.TB.toBytes(1.0D))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  78 */       this.tamanho = ((Double)total.<Throwable>orElseThrow(InterruptedException::new)).doubleValue();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean process(Path file, IQuietlyProgress progress) {
/*     */     InputDescriptor desc;
/*  84 */     Path parent = file.getParent();
/*  85 */     FileWrapper fileWrapper = new FileWrapper(file.toFile());
/*  86 */     Path output = parent.resolve(fileWrapper.getShortName() + "_(VOLUMES DE ATÉ " + Sizes.defaultFormat(Sizes.MB.toBytes(this.tamanho)) + ")");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  92 */       desc = (InputDescriptor)(new PdfInputDescriptor.Builder()).add((IInputFile)fileWrapper).output(output).build();
/*  93 */     } catch (IOException e1) {
/*  94 */       progress.abort(e1);
/*  95 */       LOGGER.error("Não foi possível criar pasta " + output.toString(), e1);
/*  96 */       return false;
/*     */     } 
/*     */     
/*  99 */     AtomicBoolean success = new AtomicBoolean(true);
/*     */     
/* 101 */     (new BySizePdfSplitter(Sizes.MB.toBytes(this.tamanho))).apply((IInputDescriptor)desc).subscribe(e -> {
/*     */           if (e instanceof com.github.pdfhandler4j.imp.event.PdfReadingStart) {
/*     */             progress.begin(PjeMediaProcessingTask.SplitterStage.READING);
/*     */           } else if (e instanceof PdfStartEvent) {
/*     */             progress.begin(PjeMediaProcessingTask.SplitterStage.SPLITING, ((PdfStartEvent)e).getTotalPages());
/*     */           } else if (e instanceof com.github.pdfhandler4j.imp.event.PdfReadingEnd || e instanceof com.github.pdfhandler4j.imp.event.PdfEndEvent) {
/*     */             progress.end();
/*     */           } else if (e instanceof com.github.pdfhandler4j.imp.event.PdfPageEvent) {
/*     */             progress.step(e.getMessage(), new Object[0]);
/*     */           } else {
/*     */             progress.info(e.getMessage(), new Object[0]);
/*     */           } 
/*     */         }e -> {
/*     */           success.set(false);
/*     */ 
/*     */           
/*     */           Directory.deleteQuietly(output.toFile());
/*     */           
/*     */           progress.abort(e);
/*     */         });
/*     */     
/* 122 */     return success.get();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/PjeBySizePdfSplitterTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */