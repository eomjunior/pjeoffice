/*    */ package com.github.videohandler4j.imp;
/*    */ 
/*    */ import com.github.filehandler4j.IFileSlice;
/*    */ import com.github.filehandler4j.imp.FileSliceWrapper;
/*    */ import com.github.utils4j.IHasDuration;
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
/*    */ public class VideoSliceWrapper
/*    */   extends FileSliceWrapper
/*    */   implements IVideoSlice
/*    */ {
/*    */   public VideoSliceWrapper(IVideoSlice slice) {
/* 36 */     super((IFileSlice)slice);
/*    */   }
/*    */   
/*    */   protected final IVideoSlice slice() {
/* 40 */     return (IVideoSlice)getSlice();
/*    */   }
/*    */ 
/*    */   
/*    */   public long getTime() {
/* 45 */     return slice().getTime();
/*    */   }
/*    */ 
/*    */   
/*    */   public long getTime(IHasDuration file) {
/* 50 */     return slice().getTime(file);
/*    */   }
/*    */ 
/*    */   
/*    */   public long end(IHasDuration file) {
/* 55 */     return slice().end(file);
/*    */   }
/*    */ 
/*    */   
/*    */   public String startString() {
/* 60 */     return slice().startString();
/*    */   }
/*    */ 
/*    */   
/*    */   public String endString() {
/* 65 */     return slice().endString();
/*    */   }
/*    */ 
/*    */   
/*    */   public String timeString() {
/* 70 */     return slice().timeString();
/*    */   }
/*    */ 
/*    */   
/*    */   public String outputFileName() {
/* 75 */     return slice().outputFileName();
/*    */   }
/*    */ 
/*    */   
/*    */   public String outputFileName(IHasDuration file) {
/* 80 */     return slice().outputFileName(file);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/videohandler4j/imp/VideoSliceWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */