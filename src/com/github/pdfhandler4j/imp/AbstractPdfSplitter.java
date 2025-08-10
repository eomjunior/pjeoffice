/*     */ package com.github.pdfhandler4j.imp;
/*     */ 
/*     */ import com.github.filehandler4j.IInputDescriptor;
/*     */ import com.github.filehandler4j.IInputFile;
/*     */ import com.github.filehandler4j.imp.AbstractFileRageHandler;
/*     */ import com.github.filehandler4j.imp.InputDescriptor;
/*     */ import com.github.pdfhandler4j.IPagesSlice;
/*     */ import com.github.pdfhandler4j.IPdfInfoEvent;
/*     */ import com.github.pdfhandler4j.imp.event.PdfEndEvent;
/*     */ import com.github.pdfhandler4j.imp.event.PdfOutputEvent;
/*     */ import com.github.pdfhandler4j.imp.event.PdfPageEvent;
/*     */ import com.github.pdfhandler4j.imp.event.PdfReadingEnd;
/*     */ import com.github.pdfhandler4j.imp.event.PdfReadingStart;
/*     */ import com.github.pdfhandler4j.imp.event.PdfStartEvent;
/*     */ import com.github.utils4j.ISmartIterator;
/*     */ import com.github.utils4j.imp.ArrayIterator;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import io.reactivex.Emitter;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ abstract class AbstractPdfSplitter
/*     */   extends AbstractFileRageHandler<IPdfInfoEvent, IPagesSlice>
/*     */ {
/*  57 */   private int currentPageNumber = 0;
/*     */   
/*  59 */   private File currentOutput = null;
/*     */   
/*     */   public AbstractPdfSplitter() {
/*  62 */     this(new IPagesSlice[] { new DefaultPagesSlice() });
/*     */   }
/*     */   
/*     */   public AbstractPdfSplitter(IPagesSlice... ranges) {
/*  66 */     this((ISmartIterator<IPagesSlice>)new ArrayIterator((Object[])ranges));
/*     */   }
/*     */   
/*     */   public AbstractPdfSplitter(ISmartIterator<IPagesSlice> iterator) {
/*  70 */     super(iterator);
/*  71 */     reset();
/*     */   }
/*     */   
/*     */   protected long combinedStart(IPagesSlice range) {
/*  75 */     return 0L;
/*     */   }
/*     */   
/*     */   protected long combinedIncrement(long currentCombined, Supplier<Long> currentDocumentSize) {
/*  79 */     return currentCombined + 1L;
/*     */   }
/*     */   
/*     */   protected String computeFileName(String originalName, long beginPage) {
/*  83 */     return originalName + " pg_" + ((beginPage == this.currentPageNumber) ? (String)Long.valueOf(beginPage) : (beginPage + "_até_" + this.currentPageNumber));
/*     */   }
/*     */   
/*     */   protected int getEndReference(int totalPages) {
/*  87 */     return totalPages;
/*     */   }
/*     */   
/*     */   protected final boolean isEnd(int totalPages) {
/*  91 */     return (this.currentPageNumber >= getEndReference(totalPages));
/*     */   }
/*     */   
/*     */   protected final boolean hasNext(int totalPages) {
/*  95 */     return (this.currentPageNumber < getEndReference(totalPages));
/*     */   }
/*     */   
/*     */   protected long nextIncrement() {
/*  99 */     return 1L;
/*     */   }
/*     */   
/*     */   protected boolean breakOnSplit() {
/* 103 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean forceCopy(IInputFile file) {
/* 107 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean accept(long finalFileSize, long currentPageNumber, long currentTotalPages, long totalPages) {
/* 111 */     return true;
/*     */   }
/*     */   
/*     */   protected final File toFile() {
/* 115 */     return this.currentOutput;
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() {
/* 120 */     this.currentPageNumber = 0;
/* 121 */     super.reset();
/*     */   }
/*     */   
/*     */   protected final IPagesSlice nextPagesSlice(long totalPages) {
/* 125 */     IPagesSlice s = (IPagesSlice)nextSlice();
/* 126 */     return (s == null) ? null : new BoundedPagesSlice(s, totalPages);
/*     */   }
/*     */   
/*     */   private ICloseablePdfDocument newDocument(String originalName) throws Exception {
/* 130 */     this.currentOutput = resolveOutput(computeFileName(originalName, this.currentPageNumber));
/* 131 */     this.currentOutput.delete();
/* 132 */     return PdfLibMode.DEFAULT.createDocument(this.currentOutput);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void handleError(Throwable e) {
/* 137 */     if (this.currentOutput != null) {
/* 138 */       this.currentOutput.delete();
/* 139 */       this.currentOutput = null;
/*     */     } 
/* 141 */     super.handleError(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void removeLastPage(long currentTotalPages) throws Exception {
/* 148 */     InputDescriptor desc = (InputDescriptor)(new PdfInputDescriptor.Builder()).add(this.currentOutput).output(this.currentOutput.getParentFile().toPath()).build();
/*     */     
/* 150 */     AtomicReference<Throwable> failed = new AtomicReference<>();
/* 151 */     ByPagesPdfSplitter s = new ByPagesPdfSplitter(new DefaultPagesSlice[] { new DefaultPagesSlice(1L, currentTotalPages - 1L) });
/* 152 */     s.apply((IInputDescriptor)desc).subscribe(e -> {  }failed::set);
/*     */     
/* 154 */     String failMessage = "Falha na remoção de página excedente";
/* 155 */     Throwables.throwIf((failed.get() != null), failMessage, failed.get());
/*     */     
/* 157 */     File newOutput = s.toFile();
/* 158 */     Throwables.throwIf(() -> Boolean.valueOf((newOutput == null || !newOutput.exists())), failMessage);
/*     */     
/* 160 */     this.currentOutput.delete();
/* 161 */     Throwables.throwIf(this.currentOutput::exists, failMessage);
/* 162 */     Throwables.throwIf(!newOutput.renameTo(this.currentOutput), failMessage);
/* 163 */     this.currentOutput = newOutput;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handle(IInputFile file, Emitter<IPdfInfoEvent> emitter) throws Exception {
/* 170 */     emitter.onNext(new PdfReadingStart("Lendo arquivo " + file.getName() + " (seja paciente...)"));
/*     */     
/* 172 */     try (ICloseablePdfReader inputPdf = PdfLibMode.DEFAULT.createReader(file.toPath().toFile())) {
/*     */       
/* 174 */       emitter.onNext(new PdfReadingEnd("Lidos " + file.length() + " bytes "));
/* 175 */       int totalPages = inputPdf.getNumberOfPages();
/* 176 */       emitter.onNext(new PdfStartEvent(totalPages));
/*     */       
/*     */       try {
/* 179 */         String originalName = file.getShortName();
/*     */         
/* 181 */         if (totalPages <= 1 || forceCopy(file)) {
/* 182 */           this.currentOutput = resolveOutput(originalName + " (PÁGINA ÚNICA)");
/* 183 */           try (OutputStream out = new FileOutputStream(this.currentOutput)) {
/* 184 */             Files.copy(file.toPath(), out);
/*     */           } 
/* 186 */           emitter.onNext(new PdfOutputEvent("Gerado arquivo " + this.currentOutput.getName(), this.currentOutput, totalPages));
/*     */         } else {
/* 188 */           IPagesSlice currentSlice = nextPagesSlice(totalPages);
/*     */           
/* 190 */           if (currentSlice == null) {
/*     */             return;
/*     */           }
/* 193 */           long maxIncrement = -2147483648L;
/*     */           
/*     */           do {
/* 196 */             checkInterrupted();
/* 197 */             this.currentPageNumber = (int)currentSlice.start();
/*     */             
/* 199 */             ICloseablePdfDocument outputDocument = newDocument(originalName);
/*     */             
/*     */             try {
/* 202 */               long currentTotalPages = 0L;
/* 203 */               long currentCombinedPages = combinedStart(currentSlice);
/*     */               
/*     */               while (true) {
/* 206 */                 long combinedBefore = currentCombinedPages;
/* 207 */                 outputDocument.addPage(inputPdf, this.currentPageNumber);
/*     */                 
/* 209 */                 Supplier<Long> currentDocumentSize = outputDocument::getCurrentDocumentSize;
/*     */                 
/* 211 */                 currentTotalPages++;
/* 212 */                 currentCombinedPages = combinedIncrement(currentCombinedPages, currentDocumentSize);
/* 213 */                 maxIncrement = Math.max(currentCombinedPages - combinedBefore, maxIncrement);
/*     */                 
/* 215 */                 if (mustSplit(currentCombinedPages, currentSlice, maxIncrement, totalPages) || isEnd(totalPages)) {
/* 216 */                   long cds = ((Long)currentDocumentSize.get()).longValue();
/*     */                   
/* 218 */                   outputDocument.close();
/* 219 */                   currentCombinedPages = 0L;
/*     */                   
/* 221 */                   boolean breakLooping = breakOnSplit();
/*     */                   
/* 223 */                   if (!accept(cds, this.currentPageNumber, currentTotalPages, totalPages)) {
/* 224 */                     breakLooping = true;
/* 225 */                     removeLastPage(currentTotalPages);
/*     */                   } 
/*     */                   
/* 228 */                   emitter.onNext(new PdfOutputEvent("Gerado arquivo " + this.currentOutput
/* 229 */                         .getName(), this.currentOutput, currentTotalPages));
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/* 234 */                   if (breakLooping) {
/*     */                     break;
/*     */                   }
/*     */                 } else {
/*     */                   
/* 239 */                   emitter.onNext(new PdfPageEvent("Adicionada página " + this.currentPageNumber, this.currentPageNumber, 
/*     */ 
/*     */                         
/* 242 */                         getEndReference(totalPages)));
/*     */                 } 
/*     */ 
/*     */ 
/*     */                 
/* 247 */                 checkInterrupted();
/* 248 */                 if (!hasNext(totalPages)) {
/*     */                   break;
/*     */                 }
/* 251 */                 this.currentPageNumber = (int)(this.currentPageNumber + nextIncrement());
/*     */                 
/* 253 */                 if (currentCombinedPages == 0L) {
/* 254 */                   outputDocument = newDocument(originalName);
/* 255 */                   currentTotalPages = 0L;
/*     */                 }
/*     */               
/*     */               } 
/*     */             } finally {
/*     */               
/* 261 */               outputDocument.close();
/*     */             }
/*     */           
/* 264 */           } while ((currentSlice = nextPagesSlice(totalPages)) != null);
/*     */         } 
/*     */       } finally {
/* 267 */         emitter.onNext(PdfEndEvent.INSTANCE);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract boolean mustSplit(long paramLong1, IPagesSlice paramIPagesSlice, long paramLong2, long paramLong3);
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/pdfhandler4j/imp/AbstractPdfSplitter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */