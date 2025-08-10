/*    */ package com.github.videohandler4j.imp;
/*    */ 
/*    */ import com.github.utils4j.imp.Args;
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
/*    */ public class VideoOptimizer
/*    */   extends FFMPEGHandler
/*    */ {
/*    */   private final int crf;
/*    */   
/*    */   public VideoOptimizer() {
/* 39 */     this(40);
/*    */   }
/*    */   
/*    */   public VideoOptimizer(int crf) {
/* 43 */     this.crf = Args.requirePositive(crf, "crf <= 0");
/*    */   }
/*    */ 
/*    */   
/*    */   protected final void fillParameters(List<String> commandLine) {
/* 48 */     commandLine.add("-crf");
/* 49 */     commandLine.add(Integer.toString(this.crf));
/* 50 */     commandLine.add("-vf");
/* 51 */     commandLine.add("scale=trunc(iw/4)*2:trunc(ih/4)*2");
/* 52 */     commandLine.add("-max_muxing_queue_size");
/* 53 */     commandLine.add("89478485");
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/videohandler4j/imp/VideoOptimizer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */