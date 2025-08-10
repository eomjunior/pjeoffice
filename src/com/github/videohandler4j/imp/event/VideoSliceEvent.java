/*    */ package com.github.videohandler4j.imp.event;
/*    */ 
/*    */ import com.github.filehandler4j.imp.event.FileInfoEvent;
/*    */ import com.github.utils4j.imp.DurationTools;
/*    */ import com.github.videohandler4j.IVideoSliceEvent;
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
/*    */ public class VideoSliceEvent
/*    */   extends FileInfoEvent
/*    */   implements IVideoSliceEvent
/*    */ {
/*    */   private final long startTime;
/*    */   private final long totalTime;
/*    */   
/*    */   public VideoSliceEvent(String message, long startTime, long totalTime) {
/* 40 */     super(message);
/* 41 */     this.startTime = startTime;
/* 42 */     this.totalTime = totalTime;
/*    */   }
/*    */ 
/*    */   
/*    */   public final long getStartTime() {
/* 47 */     return this.startTime;
/*    */   }
/*    */ 
/*    */   
/*    */   public final long getTotalTime() {
/* 52 */     return this.totalTime;
/*    */   }
/*    */ 
/*    */   
/*    */   public final String toString() {
/* 57 */     return getMessage() + " startTime: " + this.startTime + " of " + this.totalTime;
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getStartTimeString() {
/* 62 */     return DurationTools.toHmsString(this.startTime);
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getTotalTimeString() {
/* 67 */     return DurationTools.toHmsString(this.totalTime);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/videohandler4j/imp/event/VideoSliceEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */