/*     */ package com.github.pdfhandler4j.imp;
/*     */ 
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.States;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import com.itextpdf.text.Document;
/*     */ import com.itextpdf.text.pdf.PdfCopy;
/*     */ import com.itextpdf.text.pdf.PdfSmartCopy;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.OutputStream;
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
/*     */ final class CloseableITextDocumentImp
/*     */   implements ICloseablePdfDocument
/*     */ {
/*     */   private PdfCopy output;
/*     */   private Document document;
/*     */   private OutputStream outputStream;
/*     */   
/*     */   CloseableITextDocumentImp(File outputFile) throws Exception {
/*  51 */     Args.requireNonNull(outputFile, "current output is null");
/*     */     try {
/*  53 */       this.outputStream = new FileOutputStream(outputFile);
/*  54 */       this.document = new Document();
/*  55 */       this.output = (PdfCopy)new PdfSmartCopy(this.document, this.outputStream);
/*  56 */       this.output.setFullCompression();
/*  57 */       this.output.setCompressionLevel(9);
/*  58 */       this.document.open();
/*  59 */     } catch (Exception e) {
/*  60 */       close();
/*  61 */       throw e;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void checkState() {
/*  66 */     States.requireNonNull(this.output, "output closed");
/*     */   }
/*     */ 
/*     */   
/*     */   public final long getCurrentDocumentSize() {
/*  71 */     checkState();
/*  72 */     return this.output.getCurrentDocumentSize();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void addDocument(ICloseablePdfReader reader) throws Exception {
/*  77 */     checkState();
/*  78 */     Args.requireNonNull(reader, "reader is null");
/*  79 */     reader.addDocument(this.output);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void addPage(ICloseablePdfReader reader, int pageNumber) throws Exception {
/*  84 */     checkState();
/*  85 */     Args.requireNonNull(reader, "reader is null");
/*  86 */     reader.addPage(this.output, pageNumber);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void freeReader(ICloseablePdfReader reader) throws Exception {
/*  91 */     checkState();
/*  92 */     Args.requireNonNull(reader, "reader is null");
/*  93 */     reader.freeReader(this.output);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void close() {
/*  98 */     if (this.output != null) {
/*  99 */       Throwables.quietly(this.output::close);
/* 100 */       this.output = null;
/*     */     } 
/* 102 */     if (this.outputStream != null) {
/* 103 */       Throwables.quietly(this.outputStream::close);
/* 104 */       this.outputStream = null;
/*     */     } 
/* 106 */     if (this.document != null) {
/* 107 */       Throwables.quietly(this.document::close);
/* 108 */       this.document = null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/pdfhandler4j/imp/CloseableITextDocumentImp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */