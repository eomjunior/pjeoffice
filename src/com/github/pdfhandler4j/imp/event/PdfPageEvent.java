/*    */ package com.github.pdfhandler4j.imp.event;
/*    */ 
/*    */ import com.github.filehandler4j.imp.event.FileInfoEvent;
/*    */ import com.github.pdfhandler4j.IPdfPageEvent;
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
/*    */ public class PdfPageEvent
/*    */   extends FileInfoEvent
/*    */   implements IPdfPageEvent
/*    */ {
/*    */   private final long currentPage;
/*    */   private final long totalPages;
/*    */   
/*    */   public PdfPageEvent(String message, long currentPage, long totalPages) {
/* 39 */     super(message);
/* 40 */     this.currentPage = currentPage;
/* 41 */     this.totalPages = totalPages;
/*    */   }
/*    */ 
/*    */   
/*    */   public final long geCurrentPage() {
/* 46 */     return this.currentPage;
/*    */   }
/*    */ 
/*    */   
/*    */   public final long getTotalPages() {
/* 51 */     return this.totalPages;
/*    */   }
/*    */ 
/*    */   
/*    */   public final String toString() {
/* 56 */     return getMessage() + " pg: " + this.currentPage + " of " + this.totalPages;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/pdfhandler4j/imp/event/PdfPageEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */