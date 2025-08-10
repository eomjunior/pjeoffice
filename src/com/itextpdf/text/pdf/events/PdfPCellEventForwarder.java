/*    */ package com.itextpdf.text.pdf.events;
/*    */ 
/*    */ import com.itextpdf.text.Rectangle;
/*    */ import com.itextpdf.text.pdf.PdfContentByte;
/*    */ import com.itextpdf.text.pdf.PdfPCell;
/*    */ import com.itextpdf.text.pdf.PdfPCellEvent;
/*    */ import java.util.ArrayList;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PdfPCellEventForwarder
/*    */   implements PdfPCellEvent
/*    */ {
/* 63 */   protected ArrayList<PdfPCellEvent> events = new ArrayList<PdfPCellEvent>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addCellEvent(PdfPCellEvent event) {
/* 70 */     this.events.add(event);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
/* 77 */     for (PdfPCellEvent event : this.events)
/* 78 */       event.cellLayout(cell, position, canvases); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/events/PdfPCellEventForwarder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */