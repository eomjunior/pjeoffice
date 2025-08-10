/*     */ package com.github.pdfhandler4j.imp;
/*     */ 
/*     */ import com.github.filehandler4j.IInputFile;
/*     */ import com.github.filehandler4j.imp.AbstractFileHandler;
/*     */ import com.github.pdfhandler4j.IPdfInfoEvent;
/*     */ import com.github.pdfhandler4j.imp.event.PdfInfoEvent;
/*     */ import com.github.pdfhandler4j.imp.event.PdfOutputEvent;
/*     */ import com.github.pdfhandler4j.imp.event.PdfReadingEnd;
/*     */ import com.github.pdfhandler4j.imp.event.PdfReadingStart;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.StopWatch;
/*     */ import io.reactivex.Emitter;
/*     */ import java.io.File;
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
/*     */ public class JoinPdfHandler
/*     */   extends AbstractFileHandler<IPdfInfoEvent>
/*     */ {
/*     */   private File output;
/*     */   private int totalPages;
/*     */   private final String mergedFileName;
/*     */   private ICloseablePdfDocument outputDocument;
/*  54 */   private StopWatch stopWatch = new StopWatch();
/*     */   
/*     */   public JoinPdfHandler(String mergeFileName) {
/*  57 */     this.mergedFileName = Args.requireText(mergeFileName, "mergeFileName is empty");
/*     */   }
/*     */   
/*     */   private void close() {
/*  61 */     if (this.outputDocument != null) {
/*  62 */       this.outputDocument.close();
/*  63 */       this.outputDocument = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() {
/*  69 */     close();
/*  70 */     this.output = null;
/*  71 */     this.totalPages = 0;
/*  72 */     this.stopWatch.reset();
/*  73 */     super.reset();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void beforeHandle(Emitter<IPdfInfoEvent> emitter) throws Exception {
/*  78 */     this.outputDocument = PdfLibMode.DEFAULT.createDocument(this.output = resolveOutput(this.mergedFileName));
/*  79 */     this.totalPages = 0;
/*  80 */     this.stopWatch.reset();
/*  81 */     this.stopWatch.start();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void handle(IInputFile file, Emitter<IPdfInfoEvent> emitter) throws Exception {
/*  86 */     StopWatch handleWatch = new StopWatch();
/*  87 */     emitter.onNext(new PdfReadingStart("Lendo arquivo " + file.getName() + " (seja paciente...)"));
/*  88 */     handleWatch.start();
/*  89 */     try (ICloseablePdfReader reader = PdfLibMode.DEFAULT.createReader(file.toPath().toFile())) {
/*  90 */       long time = handleWatch.stop();
/*  91 */       emitter.onNext(new PdfReadingEnd("Lidos " + file.length() + " bytes em " + ((float)time / 1000.0F) + " segundos"));
/*  92 */       checkInterrupted();
/*  93 */       this.totalPages += reader.getNumberOfPages();
/*     */       try {
/*  95 */         handleWatch.start();
/*  96 */         emitter.onNext(new PdfInfoEvent("Mesclando " + this.totalPages + " páginas (seja paciente...)"));
/*  97 */         this.outputDocument.addDocument(reader);
/*  98 */         time = handleWatch.stop();
/*  99 */         emitter.onNext(new PdfInfoEvent("Mesclagem concluida em " + ((float)time / 1000.0F) + " segundos"));
/*     */       } finally {
/* 101 */         this.outputDocument.freeReader(reader);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void handleError(Throwable e) {
/* 108 */     close();
/* 109 */     cleanOutput();
/* 110 */     super.handleError(e);
/*     */   }
/*     */   
/*     */   private void cleanOutput() {
/* 114 */     if (this.output != null) {
/* 115 */       this.output.delete();
/* 116 */       this.output = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void afterHandle(Emitter<IPdfInfoEvent> emitter) {
/* 122 */     close();
/* 123 */     long time = this.stopWatch.stop();
/* 124 */     emitter.onNext(new PdfOutputEvent("Gerado arquivo " + this.output.getName() + " em " + ((float)time / 1000.0F) + " segundos ", this.output, this.totalPages));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/pdfhandler4j/imp/JoinPdfHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */