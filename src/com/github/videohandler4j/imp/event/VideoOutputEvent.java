/*    */ package com.github.videohandler4j.imp.event;
/*    */ 
/*    */ import com.github.filehandler4j.imp.event.FileOutputEvent;
/*    */ import com.github.videohandler4j.IVideoOutputEvent;
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
/*    */ public class VideoOutputEvent
/*    */   extends FileOutputEvent
/*    */   implements IVideoOutputEvent
/*    */ {
/*    */   private final long totalTime;
/*    */   
/*    */   public VideoOutputEvent(String message, File output, long totalTime) {
/* 40 */     super(message, output);
/* 41 */     this.totalTime = totalTime;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getTotalTime() {
/* 46 */     return this.totalTime;
/*    */   }
/*    */ 
/*    */   
/*    */   public final String toString() {
/* 51 */     return super.toString() + " tamanho: " + this.totalTime;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/videohandler4j/imp/event/VideoOutputEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */