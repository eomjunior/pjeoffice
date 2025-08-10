/*     */ package br.jus.cnj.pje.office.task.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.task.ITarefaPdfDivisaoParidade;
/*     */ import com.github.filehandler4j.IInputDescriptor;
/*     */ import com.github.filehandler4j.IInputFile;
/*     */ import com.github.filehandler4j.imp.FileWrapper;
/*     */ import com.github.filehandler4j.imp.InputDescriptor;
/*     */ import com.github.pdfhandler4j.IPdfInfoEvent;
/*     */ import com.github.pdfhandler4j.imp.ByEvenPagesPdfSplitter;
/*     */ import com.github.pdfhandler4j.imp.ByOddPagesPdfSplitter;
/*     */ import com.github.pdfhandler4j.imp.ByParityPdfSplitter;
/*     */ import com.github.pdfhandler4j.imp.PdfInputDescriptor;
/*     */ import com.github.pdfhandler4j.imp.event.PdfStartEvent;
/*     */ import com.github.progress4j.IQuietlyProgress;
/*     */ import com.github.progress4j.IStage;
/*     */ import com.github.taskresolver4j.exception.TaskException;
/*     */ import com.github.utils4j.imp.Params;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ class PjeByParityPdfSplitterTask
/*     */   extends PjeMediaProcessingTask<ITarefaPdfDivisaoParidade>
/*     */ {
/*     */   private boolean paridade;
/*     */   
/*     */   private enum Stage
/*     */     implements IStage
/*     */   {
/*  56 */     SPLITING_EVEN("Descartando páginas ímpares"),
/*  57 */     SPLITING_ODD("Descartando páginas pares");
/*     */     
/*     */     private final String message;
/*     */     
/*     */     Stage(String message) {
/*  62 */       this.message = message;
/*     */     }
/*     */ 
/*     */     
/*     */     public final String toString() {
/*  67 */       return this.message;
/*     */     }
/*     */     
/*     */     static Stage of(boolean parity) {
/*  71 */       return parity ? SPLITING_EVEN : SPLITING_ODD;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected PjeByParityPdfSplitterTask(Params request, ITarefaPdfDivisaoParidade pojo) {
/*  78 */     super(request, pojo);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doValidateTaskParams() throws TaskException, InterruptedException {
/*  83 */     this.paridade = getPojoParams().isParidade();
/*     */   }
/*     */   
/*     */   protected boolean process(Path file, IQuietlyProgress progress) {
/*     */     InputDescriptor desc;
/*  88 */     Path output = file.getParent();
/*  89 */     FileWrapper fileWrapper = new FileWrapper(file.toFile());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  95 */       desc = (InputDescriptor)(new PdfInputDescriptor.Builder()).add((IInputFile)fileWrapper).output(output).build();
/*  96 */     } catch (IOException e1) {
/*  97 */       progress.abort(e1);
/*  98 */       LOGGER.error("Não foi possível criar pasta " + output.toString(), e1);
/*  99 */       return false;
/*     */     } 
/*     */     
/* 102 */     ByParityPdfSplitter splitter = this.paridade ? (ByParityPdfSplitter)new ByEvenPagesPdfSplitter() : (ByParityPdfSplitter)new ByOddPagesPdfSplitter();
/*     */ 
/*     */ 
/*     */     
/* 106 */     AtomicBoolean success = new AtomicBoolean(true);
/*     */     
/* 108 */     splitter.apply((IInputDescriptor)desc).subscribe(e -> {
/*     */           if (e instanceof com.github.pdfhandler4j.imp.event.PdfReadingStart) {
/*     */             progress.begin(PjeMediaProcessingTask.SplitterStage.READING);
/*     */           } else if (e instanceof PdfStartEvent) {
/*     */             progress.begin(Stage.of(this.paridade), ((PdfStartEvent)e).getTotalPages() / 2);
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
/*     */           progress.abort(e);
/*     */         });
/*     */ 
/*     */     
/* 128 */     return success.get();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/PjeByParityPdfSplitterTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */