/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.Image;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import java.io.IOException;
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
/*     */ public class PdfImportedPage
/*     */   extends PdfTemplate
/*     */ {
/*     */   PdfReaderInstance readerInstance;
/*     */   int pageNumber;
/*     */   int rotation;
/*     */   protected boolean toCopy = true;
/*     */   
/*     */   PdfImportedPage(PdfReaderInstance readerInstance, PdfWriter writer, int pageNumber) {
/*  69 */     this.readerInstance = readerInstance;
/*  70 */     this.pageNumber = pageNumber;
/*  71 */     this.writer = writer;
/*  72 */     this.rotation = readerInstance.getReader().getPageRotation(pageNumber);
/*  73 */     this.bBox = readerInstance.getReader().getPageSize(pageNumber);
/*  74 */     setMatrix(1.0F, 0.0F, 0.0F, 1.0F, -this.bBox.getLeft(), -this.bBox.getBottom());
/*  75 */     this.type = 2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfImportedPage getFromReader() {
/*  84 */     return this;
/*     */   }
/*     */   
/*     */   public int getPageNumber() {
/*  88 */     return this.pageNumber;
/*     */   }
/*     */   
/*     */   public int getRotation() {
/*  92 */     return this.rotation;
/*     */   }
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
/*     */   public void addImage(Image image, float a, float b, float c, float d, float e, float f) throws DocumentException {
/* 106 */     throwError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTemplate(PdfTemplate template, float a, float b, float c, float d, float e, float f) {
/* 118 */     throwError();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfContentByte getDuplicate() {
/* 124 */     throwError();
/* 125 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfStream getFormXObject(int compressionLevel) throws IOException {
/* 136 */     return this.readerInstance.getFormXObject(this.pageNumber, compressionLevel);
/*     */   }
/*     */   
/*     */   public void setColorFill(PdfSpotColor sp, float tint) {
/* 140 */     throwError();
/*     */   }
/*     */   
/*     */   public void setColorStroke(PdfSpotColor sp, float tint) {
/* 144 */     throwError();
/*     */   }
/*     */   
/*     */   PdfObject getResources() {
/* 148 */     return this.readerInstance.getResources(this.pageNumber);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFontAndSize(BaseFont bf, float size) {
/* 155 */     throwError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGroup(PdfTransparencyGroup group) {
/* 164 */     throwError();
/*     */   }
/*     */   
/*     */   void throwError() {
/* 168 */     throw new RuntimeException(MessageLocalization.getComposedMessage("content.can.not.be.added.to.a.pdfimportedpage", new Object[0]));
/*     */   }
/*     */   
/*     */   PdfReaderInstance getPdfReaderInstance() {
/* 172 */     return this.readerInstance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isToCopy() {
/* 181 */     return this.toCopy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCopied() {
/* 189 */     this.toCopy = false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfImportedPage.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */