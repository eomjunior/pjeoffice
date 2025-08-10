/*    */ package com.github.pdfhandler4j.imp.event;
/*    */ 
/*    */ import com.github.filehandler4j.imp.event.FileInfoEvent;
/*    */ import com.github.pdfhandler4j.IPdfInfoEvent;
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
/*    */ public class PdfReadingEnd
/*    */   extends FileInfoEvent
/*    */   implements IPdfInfoEvent
/*    */ {
/*    */   public PdfReadingEnd(String message) {
/* 35 */     super(message);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/pdfhandler4j/imp/event/PdfReadingEnd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */