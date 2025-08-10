/*     */ package br.jus.cnj.pje.office.task.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.imp.PjeConfig;
/*     */ import br.jus.cnj.pje.office.task.ITarefaMedia;
/*     */ import com.github.filehandler4j.IInputDescriptor;
/*     */ import com.github.filehandler4j.IInputFile;
/*     */ import com.github.filehandler4j.imp.FileWrapper;
/*     */ import com.github.filehandler4j.imp.InputDescriptor;
/*     */ import com.github.pdfhandler4j.IPdfInfoEvent;
/*     */ import com.github.pdfhandler4j.imp.ByPagesPdfSplitter;
/*     */ import com.github.pdfhandler4j.imp.DefaultPagesSlice;
/*     */ import com.github.pdfhandler4j.imp.PdfInputDescriptor;
/*     */ import com.github.pdfhandler4j.imp.event.PdfStartEvent;
/*     */ import com.github.progress4j.IQuietlyProgress;
/*     */ import com.github.taskresolver4j.exception.TaskException;
/*     */ import com.github.utils4j.gui.imp.PrintStyleDialog;
/*     */ import com.github.utils4j.gui.imp.SwingTools;
/*     */ import com.github.utils4j.imp.Directory;
/*     */ import com.github.utils4j.imp.Params;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ class PjeByPagesPdfSplitterTask
/*     */   extends PjeMediaProcessingTask<ITarefaMedia>
/*     */ {
/*  63 */   private List<DefaultPagesSlice> slices = new ArrayList<>();
/*     */   
/*     */   protected PjeByPagesPdfSplitterTask(Params request, ITarefaMedia pojo) {
/*  66 */     super(request, pojo);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doValidateTaskParams() throws TaskException, InterruptedException {
/*  71 */     Optional<String> intervals = SwingTools.invokeAndWaitT(new PrintStyleDialog(PjeConfig.getIcon())::getPagesInterval);
/*  72 */     buildSlices(intervals.<Throwable>orElseThrow(InterruptedException::new));
/*  73 */     if (this.slices.isEmpty()) {
/*  74 */       throw new TaskException("Intervalo de páginas incorreto");
/*     */     }
/*     */   }
/*     */   
/*     */   private void buildSlices(String text) {
/*  79 */     String[] parts = text.split(";");
/*  80 */     for (String part : parts) {
/*  81 */       String[] pages = part.split("-");
/*  82 */       int start = -1;
/*  83 */       DefaultPagesSlice dp = null;
/*  84 */       for (String page : pages) {
/*  85 */         page = Strings.trim(page);
/*  86 */         int p = "*".equals(page) ? Integer.MAX_VALUE : Strings.toInt(page, -1);
/*  87 */         if (start < 0) {
/*  88 */           start = p;
/*     */         } else {
/*  90 */           this.slices.add(dp = new DefaultPagesSlice(start, p));
/*     */         } 
/*     */       } 
/*  93 */       if (dp == null) {
/*  94 */         this.slices.add(dp = new DefaultPagesSlice(start, start));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean process(Path file, IQuietlyProgress progress) {
/*     */     InputDescriptor desc;
/* 102 */     Path parentFolder = file.getParent();
/* 103 */     FileWrapper fileWrapper = new FileWrapper(file.toFile());
/*     */     
/* 105 */     Path outputFolder = parentFolder.resolve(fileWrapper.getShortName() + "_(VOLUMES)");
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 110 */       desc = (InputDescriptor)(new PdfInputDescriptor.Builder()).add((IInputFile)fileWrapper).output(outputFolder).build();
/* 111 */     } catch (IOException e1) {
/* 112 */       progress.abort(e1);
/* 113 */       LOGGER.error("Não foi possível criar pasta " + parentFolder.toString(), e1);
/* 114 */       return false;
/*     */     } 
/*     */     
/* 117 */     ByPagesPdfSplitter splitter = new ByPagesPdfSplitter(this.slices.<DefaultPagesSlice>toArray(new DefaultPagesSlice[this.slices.size()]));
/*     */     
/* 119 */     AtomicBoolean success = new AtomicBoolean(true);
/*     */     
/* 121 */     splitter.apply((IInputDescriptor)desc).subscribe(e -> {
/*     */           if (e instanceof com.github.pdfhandler4j.imp.event.PdfReadingStart) {
/*     */             progress.begin(PjeMediaProcessingTask.SplitterStage.READING);
/*     */           } else if (e instanceof PdfStartEvent) {
/*     */             progress.begin(PjeMediaProcessingTask.SplitterStage.SPLITING, ((PdfStartEvent)e).getTotalPages());
/*     */           } else if (e instanceof com.github.pdfhandler4j.imp.event.PdfReadingEnd || e instanceof com.github.pdfhandler4j.imp.event.PdfEndEvent) {
/*     */             progress.end();
/*     */           } else if (e instanceof com.github.pdfhandler4j.imp.event.PdfPageEvent || e instanceof com.github.pdfhandler4j.imp.event.PdfOutputEvent) {
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
/* 141 */     return success.get();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/PjeByPagesPdfSplitterTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */