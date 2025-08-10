/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.Document;
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import java.io.IOException;
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
/*     */ public class PdfConcatenate
/*     */ {
/*     */   public PdfConcatenate(OutputStream os) throws DocumentException {
/*  67 */     this(os, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   protected Document document = new Document(); public PdfConcatenate(OutputStream os, boolean smart) throws DocumentException {
/*  77 */     if (smart) {
/*  78 */       this.copy = new PdfSmartCopy(this.document, os);
/*     */     } else {
/*  80 */       this.copy = new PdfCopy(this.document, os);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected PdfCopy copy;
/*     */ 
/*     */ 
/*     */   
/*     */   public int addPages(PdfReader reader) throws DocumentException, IOException {
/*  91 */     open();
/*  92 */     int n = reader.getNumberOfPages();
/*  93 */     for (int i = 1; i <= n; i++) {
/*  94 */       this.copy.addPage(this.copy.getImportedPage(reader, i));
/*     */     }
/*  96 */     this.copy.freeReader(reader);
/*  97 */     reader.close();
/*  98 */     return n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfCopy getWriter() {
/* 105 */     return this.copy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void open() {
/* 113 */     if (!this.document.isOpen()) {
/* 114 */       this.document.open();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 122 */     this.document.close();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfConcatenate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */