/*     */ package com.itextpdf.text.pdf;
/*     */ 
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
/*     */ public class PdfGState
/*     */   extends PdfDictionary
/*     */ {
/*  57 */   public static final PdfName BM_NORMAL = new PdfName("Normal");
/*     */   
/*  59 */   public static final PdfName BM_COMPATIBLE = new PdfName("Compatible");
/*     */   
/*  61 */   public static final PdfName BM_MULTIPLY = new PdfName("Multiply");
/*     */   
/*  63 */   public static final PdfName BM_SCREEN = new PdfName("Screen");
/*     */   
/*  65 */   public static final PdfName BM_OVERLAY = new PdfName("Overlay");
/*     */   
/*  67 */   public static final PdfName BM_DARKEN = new PdfName("Darken");
/*     */   
/*  69 */   public static final PdfName BM_LIGHTEN = new PdfName("Lighten");
/*     */   
/*  71 */   public static final PdfName BM_COLORDODGE = new PdfName("ColorDodge");
/*     */   
/*  73 */   public static final PdfName BM_COLORBURN = new PdfName("ColorBurn");
/*     */   
/*  75 */   public static final PdfName BM_HARDLIGHT = new PdfName("HardLight");
/*     */   
/*  77 */   public static final PdfName BM_SOFTLIGHT = new PdfName("SoftLight");
/*     */   
/*  79 */   public static final PdfName BM_DIFFERENCE = new PdfName("Difference");
/*     */   
/*  81 */   public static final PdfName BM_EXCLUSION = new PdfName("Exclusion");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOverPrintStroking(boolean op) {
/*  88 */     put(PdfName.OP, op ? PdfBoolean.PDFTRUE : PdfBoolean.PDFFALSE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOverPrintNonStroking(boolean op) {
/*  96 */     put(PdfName.op, op ? PdfBoolean.PDFTRUE : PdfBoolean.PDFFALSE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOverPrintMode(int opm) {
/* 104 */     put(PdfName.OPM, new PdfNumber((opm == 0) ? 0 : 1));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStrokeOpacity(float ca) {
/* 114 */     put(PdfName.CA, new PdfNumber(ca));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFillOpacity(float ca) {
/* 124 */     put(PdfName.ca, new PdfNumber(ca));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAlphaIsShape(boolean ais) {
/* 134 */     put(PdfName.AIS, ais ? PdfBoolean.PDFTRUE : PdfBoolean.PDFFALSE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTextKnockout(boolean tk) {
/* 143 */     put(PdfName.TK, tk ? PdfBoolean.PDFTRUE : PdfBoolean.PDFFALSE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBlendMode(PdfName bm) {
/* 151 */     put(PdfName.BM, bm);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRenderingIntent(PdfName ri) {
/* 161 */     put(PdfName.RI, ri);
/*     */   }
/*     */ 
/*     */   
/*     */   public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
/* 166 */     PdfWriter.checkPdfIsoConformance(writer, 6, this);
/* 167 */     super.toPdf(writer, os);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfGState.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */