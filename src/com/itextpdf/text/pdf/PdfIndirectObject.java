/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.DocWriter;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfIndirectObject
/*     */ {
/*     */   protected int number;
/*  74 */   protected int generation = 0;
/*     */   
/*  76 */   static final byte[] STARTOBJ = DocWriter.getISOBytes(" obj\n");
/*  77 */   static final byte[] ENDOBJ = DocWriter.getISOBytes("\nendobj\n");
/*  78 */   static final int SIZEOBJ = STARTOBJ.length + ENDOBJ.length;
/*     */ 
/*     */ 
/*     */   
/*     */   protected PdfObject object;
/*     */ 
/*     */ 
/*     */   
/*     */   protected PdfWriter writer;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected PdfIndirectObject(int number, PdfObject object, PdfWriter writer) {
/*  92 */     this(number, 0, object, writer);
/*     */   }
/*     */   
/*     */   PdfIndirectObject(PdfIndirectReference ref, PdfObject object, PdfWriter writer) {
/*  96 */     this(ref.getNumber(), ref.getGeneration(), object, writer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PdfIndirectObject(int number, int generation, PdfObject object, PdfWriter writer) {
/* 107 */     this.writer = writer;
/* 108 */     this.number = number;
/* 109 */     this.generation = generation;
/* 110 */     this.object = object;
/* 111 */     PdfEncryption crypto = null;
/* 112 */     if (writer != null)
/* 113 */       crypto = writer.getEncryption(); 
/* 114 */     if (crypto != null) {
/* 115 */       crypto.setHashKey(number, generation);
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfIndirectReference getIndirectReference() {
/* 142 */     return new PdfIndirectReference(this.object.type(), this.number, this.generation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeTo(OutputStream os) throws IOException {
/* 153 */     os.write(DocWriter.getISOBytes(String.valueOf(this.number)));
/* 154 */     os.write(32);
/* 155 */     os.write(DocWriter.getISOBytes(String.valueOf(this.generation)));
/* 156 */     os.write(STARTOBJ);
/* 157 */     this.object.toPdf(this.writer, os);
/* 158 */     os.write(ENDOBJ);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 163 */     return this.number + ' ' + this.generation + " R: " + ((this.object != null) ? this.object.toString() : "null");
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfIndirectObject.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */