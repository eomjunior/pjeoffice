/*    */ package com.github.videohandler4j.imp;
/*    */ 
/*    */ import com.github.filehandler4j.imp.DefaultFileSlice;
/*    */ import com.github.utils4j.IHasDuration;
/*    */ import com.github.utils4j.imp.DurationTools;
/*    */ import com.github.videohandler4j.IVideoSlice;
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
/*    */ public final class DefaultVideoSlice
/*    */   extends DefaultFileSlice
/*    */   implements IVideoSlice
/*    */ {
/*    */   public DefaultVideoSlice() {
/* 40 */     this(0L);
/*    */   }
/*    */   
/*    */   public DefaultVideoSlice(long startTime) {
/* 44 */     this(startTime, Long.MAX_VALUE);
/*    */   }
/*    */   
/*    */   public DefaultVideoSlice(long startTime, long endTime) {
/* 48 */     super(startTime, endTime);
/*    */   }
/*    */ 
/*    */   
/*    */   public long getTime() {
/* 53 */     return end() - start();
/*    */   }
/*    */ 
/*    */   
/*    */   public String outputFileName() {
/* 58 */     return DurationTools.toHmsString(start()) + "_até_" + DurationTools.toHmsString(end());
/*    */   }
/*    */ 
/*    */   
/*    */   public long getTime(IHasDuration file) {
/* 63 */     return end(file) - start();
/*    */   }
/*    */ 
/*    */   
/*    */   public long end(IHasDuration file) {
/* 68 */     return Math.min(end(), file.getDuration().toMillis());
/*    */   }
/*    */ 
/*    */   
/*    */   public String outputFileName(IHasDuration file) {
/* 73 */     return DurationTools.toHmsString(start()) + "_até_" + DurationTools.toHmsString(end(file));
/*    */   }
/*    */ 
/*    */   
/*    */   public String endString() {
/* 78 */     long end = end();
/* 79 */     return (Long.MAX_VALUE == end) ? "__:__:__" : DurationTools.toString(end);
/*    */   }
/*    */ 
/*    */   
/*    */   public String startString() {
/* 84 */     return DurationTools.toString(start());
/*    */   }
/*    */ 
/*    */   
/*    */   public String timeString() {
/* 89 */     long end = end();
/* 90 */     return (end == Long.MAX_VALUE) ? "__:__:__" : DurationTools.toString(getTime());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/videohandler4j/imp/DefaultVideoSlice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */