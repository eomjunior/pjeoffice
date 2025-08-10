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
/*    */ public class WebmConverter
/*    */   extends FFMPEGHandler
/*    */ {
/*    */   protected final void fillParameters(List<String> commandLine) {
/* 36 */     commandLine.add("-map");
/* 37 */     commandLine.add("0:v:0");
/* 38 */     commandLine.add("-map");
/* 39 */     commandLine.add("0:a:0");
/* 40 */     commandLine.add("-c:v");
/* 41 */     commandLine.add("libvpx");
/* 42 */     commandLine.add("-b:v");
/* 43 */     commandLine.add("512k");
/* 44 */     commandLine.add("-crf");
/* 45 */     commandLine.add("40");
/* 46 */     commandLine.add("-max_muxing_queue_size");
/* 47 */     commandLine.add("89478485");
/* 48 */     commandLine.add("-c:a");
/* 49 */     commandLine.add("libvorbis");
/* 50 */     commandLine.add("-ac");
/* 51 */     commandLine.add("2");
/* 52 */     commandLine.add("-strict");
/* 53 */     commandLine.add("experimental");
/* 54 */     commandLine.add("-vf");
/* 55 */     commandLine.add("scale='-1:min(ih,320)'");
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/videohandler4j/imp/WebmConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */