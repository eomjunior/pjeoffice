/*    */ package com.github.videohandler4j.imp;
/*    */ 
/*    */ import com.github.filehandler4j.IInputDescriptor;
/*    */ import com.github.filehandler4j.IInputFile;
/*    */ import com.github.filehandler4j.imp.InputDescriptor;
/*    */ import com.github.videohandler4j.IVideoFile;
/*    */ import java.io.IOException;
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
/*    */ public class VideoDescriptor
/*    */   extends InputDescriptor
/*    */ {
/*    */   private VideoDescriptor() {}
/*    */   
/*    */   public static class Builder
/*    */     extends InputDescriptor.Builder
/*    */   {
/*    */     public Builder(String extension) {
/* 43 */       super(extension);
/*    */     }
/*    */     
/*    */     public Builder add(IVideoFile input) {
/* 47 */       add((IInputFile)input);
/* 48 */       return this;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public VideoDescriptor build() throws IOException {
/* 54 */       return (VideoDescriptor)super.build();
/*    */     }
/*    */ 
/*    */     
/*    */     protected VideoDescriptor createDescriptor() {
/* 59 */       return new VideoDescriptor();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/videohandler4j/imp/VideoDescriptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */