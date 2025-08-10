/*    */ package com.github.pdfhandler4j.imp.event;
/*    */ 
/*    */ import com.github.filehandler4j.imp.event.FileOutputEvent;
/*    */ import com.github.pdfhandler4j.IPdfOutputEvent;
/*    */ import java.io.File;
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
/*    */ public class PdfOutputEvent
/*    */   extends FileOutputEvent
/*    */   implements IPdfOutputEvent
/*    */ {
/*    */   private final long totalPages;
/*    */   
/*    */   public PdfOutputEvent(String message, File output, long totalPages) {
/* 40 */     super(message, output);
/* 41 */     this.totalPages = totalPages;
/*    */   }
/*    */ 
/*    */   
/*    */   public final long getTotalPages() {
/* 46 */     return this.totalPages;
/*    */   }
/*    */ 
/*    */   
/*    */   public final String toString() {
/* 51 */     return super.toString() + " totalPages: " + this.totalPages;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/pdfhandler4j/imp/event/PdfOutputEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */