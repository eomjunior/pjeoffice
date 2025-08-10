/*    */ package com.itextpdf.awt;
/*    */ 
/*    */ import com.itextpdf.text.pdf.PdfContentByte;
/*    */ import java.awt.print.PrinterGraphics;
/*    */ import java.awt.print.PrinterJob;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PdfPrinterGraphics2D
/*    */   extends PdfGraphics2D
/*    */   implements PrinterGraphics
/*    */ {
/*    */   private PrinterJob printerJob;
/*    */   
/*    */   public PdfPrinterGraphics2D(PdfContentByte cb, float width, float height, PrinterJob printerJob) {
/* 60 */     super(cb, width, height);
/* 61 */     this.printerJob = printerJob;
/*    */   }
/*    */   
/*    */   public PdfPrinterGraphics2D(PdfContentByte cb, float width, float height, boolean onlyShapes, PrinterJob printerJob) {
/* 65 */     super(cb, width, height, onlyShapes);
/* 66 */     this.printerJob = printerJob;
/*    */   }
/*    */   public PdfPrinterGraphics2D(PdfContentByte cb, float width, float height, FontMapper fontMapper, PrinterJob printerJob) {
/* 69 */     super(cb, width, height, fontMapper, false, false, 0.0F);
/* 70 */     this.printerJob = printerJob;
/*    */   }
/*    */   
/*    */   public PdfPrinterGraphics2D(PdfContentByte cb, float width, float height, FontMapper fontMapper, boolean onlyShapes, boolean convertImagesToJPEG, float quality, PrinterJob printerJob) {
/* 74 */     super(cb, width, height, fontMapper, onlyShapes, convertImagesToJPEG, quality);
/* 75 */     this.printerJob = printerJob;
/*    */   }
/*    */   
/*    */   public PrinterJob getPrinterJob() {
/* 79 */     return this.printerJob;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/awt/PdfPrinterGraphics2D.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */