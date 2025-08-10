/*    */ package com.github.videohandler4j.imp;
/*    */ 
/*    */ import java.util.List;
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
/*    */ public class Mp3AudioExtractor
/*    */   extends FFMPEGHandler
/*    */ {
/*    */   protected final void fillParameters(List<String> commandLine) {
/* 36 */     commandLine.add("-q:a");
/* 37 */     commandLine.add("0");
/* 38 */     commandLine.add("-map");
/* 39 */     commandLine.add("a");
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/videohandler4j/imp/Mp3AudioExtractor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */