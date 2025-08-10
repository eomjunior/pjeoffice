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
/*    */ public class OggAudioExtractor
/*    */   extends FFMPEGHandler
/*    */ {
/*    */   protected final void fillParameters(List<String> commandLine) {
/* 36 */     commandLine.add("-acodec");
/* 37 */     commandLine.add("libvorbis");
/* 38 */     commandLine.add("-aq");
/* 39 */     commandLine.add("3");
/* 40 */     commandLine.add("-vn");
/* 41 */     commandLine.add("-ac");
/* 42 */     commandLine.add("2");
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/videohandler4j/imp/OggAudioExtractor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */