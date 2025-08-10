/*    */ package com.github.videohandler4j.imp;
/*    */ 
/*    */ import com.github.utils4j.IHasDuration;
/*    */ import com.github.videohandler4j.IVideoFile;
/*    */ import java.time.Duration;
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
/*    */ 
/*    */ 
/*    */ public class ByDurationVideoSplitter
/*    */   extends AbstractVideoSplitter
/*    */ {
/*    */   private Duration maxSliceDuration;
/*    */   
/*    */   public ByDurationVideoSplitter(IHasDuration file, Duration maxSliceDuration) {
/* 42 */     super(true, TimeTools.slices(file, maxSliceDuration, 0L, 0L));
/* 43 */     this.maxSliceDuration = maxSliceDuration;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean forceCopy(IVideoFile file) {
/* 48 */     return (file.getDuration().toMillis() <= this.maxSliceDuration.toMillis());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/videohandler4j/imp/ByDurationVideoSplitter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */