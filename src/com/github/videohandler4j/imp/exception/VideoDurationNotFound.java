/*    */ package com.github.videohandler4j.imp.exception;
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
/*    */ public class VideoDurationNotFound
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public VideoDurationNotFound() {
/* 35 */     this("");
/*    */   }
/*    */   
/*    */   public VideoDurationNotFound(String message) {
/* 39 */     super("Could not compute video duration: " + message);
/*    */   }
/*    */   
/*    */   public VideoDurationNotFound(Exception cause) {
/* 43 */     super(cause);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/videohandler4j/imp/exception/VideoDurationNotFound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */