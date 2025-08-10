/*     */ package com.itextpdf.text.pdf.events;
/*     */ 
/*     */ import com.itextpdf.text.pdf.PdfContentByte;
/*     */ import com.itextpdf.text.pdf.PdfPRow;
/*     */ import com.itextpdf.text.pdf.PdfPTable;
/*     */ import com.itextpdf.text.pdf.PdfPTableEvent;
/*     */ import com.itextpdf.text.pdf.PdfPTableEventAfterSplit;
/*     */ import com.itextpdf.text.pdf.PdfPTableEventSplit;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfPTableEventForwarder
/*     */   implements PdfPTableEventAfterSplit
/*     */ {
/*  65 */   protected ArrayList<PdfPTableEvent> events = new ArrayList<PdfPTableEvent>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTableEvent(PdfPTableEvent event) {
/*  72 */     this.events.add(event);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void tableLayout(PdfPTable table, float[][] widths, float[] heights, int headerRows, int rowStart, PdfContentByte[] canvases) {
/*  79 */     for (PdfPTableEvent event : this.events) {
/*  80 */       event.tableLayout(table, widths, heights, headerRows, rowStart, canvases);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void splitTable(PdfPTable table) {
/*  89 */     for (PdfPTableEvent event : this.events) {
/*  90 */       if (event instanceof PdfPTableEventSplit) {
/*  91 */         ((PdfPTableEventSplit)event).splitTable(table);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterSplitTable(PdfPTable table, PdfPRow startRow, int startIdx) {
/* 100 */     for (PdfPTableEvent event : this.events) {
/* 101 */       if (event instanceof PdfPTableEventAfterSplit)
/* 102 */         ((PdfPTableEventAfterSplit)event).afterSplitTable(table, startRow, startIdx); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/events/PdfPTableEventForwarder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */