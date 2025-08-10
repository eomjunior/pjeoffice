/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.DocWriter;
/*     */ import com.itextpdf.text.Document;
/*     */ import com.itextpdf.text.Rectangle;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.zip.Deflater;
/*     */ import java.util.zip.DeflaterOutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class PdfContents
/*     */   extends PdfStream
/*     */ {
/*  61 */   static final byte[] SAVESTATE = DocWriter.getISOBytes("q\n");
/*  62 */   static final byte[] RESTORESTATE = DocWriter.getISOBytes("Q\n");
/*  63 */   static final byte[] ROTATE90 = DocWriter.getISOBytes("0 1 -1 0 ");
/*  64 */   static final byte[] ROTATE180 = DocWriter.getISOBytes("-1 0 0 -1 ");
/*  65 */   static final byte[] ROTATE270 = DocWriter.getISOBytes("0 -1 1 0 ");
/*  66 */   static final byte[] ROTATEFINAL = DocWriter.getISOBytes(" cm\n");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PdfContents(PdfContentByte under, PdfContentByte content, PdfContentByte text, PdfContentByte secondContent, Rectangle page) throws BadPdfFormatException {
/*     */     try {
/*  82 */       OutputStream out = null;
/*  83 */       Deflater deflater = null;
/*  84 */       this.streamBytes = new ByteArrayOutputStream();
/*  85 */       if (Document.compress) {
/*     */         
/*  87 */         this.compressed = true;
/*  88 */         if (text != null) {
/*  89 */           this.compressionLevel = text.getPdfWriter().getCompressionLevel();
/*  90 */         } else if (content != null) {
/*  91 */           this.compressionLevel = content.getPdfWriter().getCompressionLevel();
/*  92 */         }  deflater = new Deflater(this.compressionLevel);
/*  93 */         out = new DeflaterOutputStream(this.streamBytes, deflater);
/*     */       } else {
/*     */         
/*  96 */         out = this.streamBytes;
/*  97 */       }  int rotation = page.getRotation();
/*  98 */       switch (rotation) {
/*     */         case 90:
/* 100 */           out.write(ROTATE90);
/* 101 */           out.write(DocWriter.getISOBytes(ByteBuffer.formatDouble(page.getTop())));
/* 102 */           out.write(32);
/* 103 */           out.write(48);
/* 104 */           out.write(ROTATEFINAL);
/*     */           break;
/*     */         case 180:
/* 107 */           out.write(ROTATE180);
/* 108 */           out.write(DocWriter.getISOBytes(ByteBuffer.formatDouble(page.getRight())));
/* 109 */           out.write(32);
/* 110 */           out.write(DocWriter.getISOBytes(ByteBuffer.formatDouble(page.getTop())));
/* 111 */           out.write(ROTATEFINAL);
/*     */           break;
/*     */         case 270:
/* 114 */           out.write(ROTATE270);
/* 115 */           out.write(48);
/* 116 */           out.write(32);
/* 117 */           out.write(DocWriter.getISOBytes(ByteBuffer.formatDouble(page.getRight())));
/* 118 */           out.write(ROTATEFINAL);
/*     */           break;
/*     */       } 
/* 121 */       if (under.size() > 0) {
/* 122 */         out.write(SAVESTATE);
/* 123 */         under.getInternalBuffer().writeTo(out);
/* 124 */         out.write(RESTORESTATE);
/*     */       } 
/* 126 */       if (content.size() > 0) {
/* 127 */         out.write(SAVESTATE);
/* 128 */         content.getInternalBuffer().writeTo(out);
/* 129 */         out.write(RESTORESTATE);
/*     */       } 
/* 131 */       if (text != null) {
/* 132 */         out.write(SAVESTATE);
/* 133 */         text.getInternalBuffer().writeTo(out);
/* 134 */         out.write(RESTORESTATE);
/*     */       } 
/* 136 */       if (secondContent.size() > 0) {
/* 137 */         secondContent.getInternalBuffer().writeTo(out);
/*     */       }
/* 139 */       out.close();
/* 140 */       if (deflater != null) {
/* 141 */         deflater.end();
/*     */       }
/*     */     }
/* 144 */     catch (Exception e) {
/* 145 */       throw new BadPdfFormatException(e.getMessage());
/*     */     } 
/* 147 */     put(PdfName.LENGTH, new PdfNumber(this.streamBytes.size()));
/* 148 */     if (this.compressed)
/* 149 */       put(PdfName.FILTER, PdfName.FLATEDECODE); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfContents.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */