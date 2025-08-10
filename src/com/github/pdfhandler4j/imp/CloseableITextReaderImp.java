/*     */ package com.github.pdfhandler4j.imp;
/*     */ 
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.States;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import com.itextpdf.text.io.RandomAccessSource;
/*     */ import com.itextpdf.text.io.RandomAccessSourceFactory;
/*     */ import com.itextpdf.text.pdf.PdfCopy;
/*     */ import com.itextpdf.text.pdf.PdfReader;
/*     */ import com.itextpdf.text.pdf.RandomAccessFileOrArray;
/*     */ import com.itextpdf.text.pdf.ReaderProperties;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
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
/*     */ final class CloseableITextReaderImp
/*     */   implements ICloseablePdfReader
/*     */ {
/*     */   private PdfReader reader;
/*     */   private RandomAccessFile raf;
/*     */   private RandomAccessSource rasf;
/*     */   private RandomAccessFileOrArray rasfa;
/*     */   
/*     */   CloseableITextReaderImp(File file) throws IOException {
/*  56 */     Args.requireNonNull(file, "file is null");
/*  57 */     this.raf = new RandomAccessFile(file, "rw");
/*     */     try {
/*  59 */       this
/*     */         
/*  61 */         .rasf = (new RandomAccessSourceFactory()).setForceRead((file.length() <= 6553600L)).createBestSource(this.raf);
/*  62 */       this.rasfa = new RandomAccessFileOrArray(this.rasf);
/*  63 */       this
/*  64 */         .reader = new PdfReader((new ReaderProperties()).setCloseSourceOnconstructorError(true), this.rasfa);
/*     */ 
/*     */       
/*  67 */       this.reader.removeUnusedObjects();
/*  68 */       this.reader.consolidateNamedDestinations();
/*  69 */     } catch (IOException e) {
/*  70 */       close();
/*  71 */       throw e;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void checkState() {
/*  76 */     States.requireTrue((this.reader != null), "reader is closed");
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getNumberOfPages() {
/*  81 */     checkState();
/*  82 */     return this.reader.getNumberOfPages();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void addPage(Object output, int pageNumber) throws Exception {
/*  87 */     checkState();
/*  88 */     Args.requireNonNull(output, "output is null");
/*  89 */     Args.requirePositive(pageNumber, "pageNumber <= 0");
/*  90 */     PdfCopy copy = (PdfCopy)output;
/*  91 */     copy.addPage(copy.getImportedPage(this.reader, pageNumber));
/*     */   }
/*     */ 
/*     */   
/*     */   public final void addDocument(Object output) throws Exception {
/*  96 */     checkState();
/*  97 */     Args.requireNonNull(output, "output is null");
/*  98 */     PdfCopy copy = (PdfCopy)output;
/*  99 */     copy.addDocument(this.reader);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void freeReader(Object output) throws Exception {
/* 104 */     checkState();
/* 105 */     Args.requireNonNull(output, "output is null");
/* 106 */     PdfCopy copy = (PdfCopy)output;
/* 107 */     copy.freeReader(this.reader);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void close() {
/* 112 */     if (this.reader != null) {
/* 113 */       Throwables.quietly(this.reader::close);
/* 114 */       this.reader = null;
/*     */     } 
/*     */     
/* 117 */     if (this.rasfa != null) {
/* 118 */       Throwables.quietly(this.rasfa::close);
/* 119 */       this.rasfa = null;
/*     */     } 
/*     */     
/* 122 */     if (this.rasf != null) {
/* 123 */       Throwables.quietly(this.rasf::close);
/* 124 */       this.rasf = null;
/*     */     } 
/*     */     
/* 127 */     if (this.raf != null) {
/* 128 */       Throwables.quietly(this.raf::close);
/* 129 */       this.raf = null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/pdfhandler4j/imp/CloseableITextReaderImp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */