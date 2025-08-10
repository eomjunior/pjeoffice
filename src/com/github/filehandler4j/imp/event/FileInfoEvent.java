/*    */ package com.github.filehandler4j.imp.event;
/*    */ 
/*    */ import com.github.filehandler4j.IFileInfoEvent;
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
/*    */ public class FileInfoEvent
/*    */   implements IFileInfoEvent
/*    */ {
/*    */   private final String message;
/*    */   
/*    */   public FileInfoEvent(String message) {
/* 37 */     this.message = message;
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getMessage() {
/* 42 */     return this.message;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 47 */     return this.message;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/filehandler4j/imp/event/FileInfoEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */