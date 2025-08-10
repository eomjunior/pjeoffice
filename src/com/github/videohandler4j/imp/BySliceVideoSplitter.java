/*    */ package com.github.videohandler4j.imp;
/*    */ 
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
/*    */ public class BySliceVideoSplitter
/*    */   extends AbstractVideoSplitter
/*    */ {
/*    */   public BySliceVideoSplitter(IVideoSlice... slices) {
/* 35 */     this(true, slices);
/*    */   }
/*    */   
/*    */   public BySliceVideoSplitter(boolean partPrefix, IVideoSlice... slices) {
/* 39 */     super(partPrefix, slices);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/videohandler4j/imp/BySliceVideoSplitter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */