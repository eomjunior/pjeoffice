/*    */ package com.github.pdfhandler4j.imp.event;
/*    */ 
/*    */ import com.github.pdfhandler4j.IPdfStartEvent;
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
/*    */ public class PdfStartEvent
/*    */   extends PdfInfoEvent
/*    */   implements IPdfStartEvent
/*    */ {
/*    */   private int totalPages;
/*    */   
/*    */   public PdfStartEvent(int totalPages) {
/* 36 */     super("start");
/* 37 */     this.totalPages = totalPages;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getTotalPages() {
/* 42 */     return this.totalPages;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/pdfhandler4j/imp/event/PdfStartEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */