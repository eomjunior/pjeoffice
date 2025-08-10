/*    */ package com.github.pdfhandler4j.imp.event;
/*    */ 
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
/*    */ 
/*    */ public enum PdfEndEvent
/*    */   implements IPdfInfoEvent
/*    */ {
/* 33 */   INSTANCE;
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 37 */     return "end";
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/pdfhandler4j/imp/event/PdfEndEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */