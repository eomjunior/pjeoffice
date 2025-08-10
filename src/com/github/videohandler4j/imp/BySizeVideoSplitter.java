/*    */ package com.github.videohandler4j.imp;
/*    */ 
/*    */ import com.github.utils4j.ISmartIterator;
/*    */ import com.github.utils4j.imp.ArrayIterator;
/*    */ import com.github.videohandler4j.IVideoFile;
/*    */ import com.github.videohandler4j.IVideoSlice;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BySizeVideoSplitter
/*    */   extends AbstractVideoSplitter
/*    */ {
/*    */   private final long maxSliceFileSize;
/*    */   private final IVideoFile file;
/* 44 */   private float percent = 0.95F;
/*    */   
/*    */   public BySizeVideoSplitter(IVideoFile file, long maxSliceFileSize) {
/* 47 */     this(file, maxSliceFileSize, true);
/*    */   }
/*    */   
/*    */   public BySizeVideoSplitter(IVideoFile file, long maxSliceFileSize, boolean partPrefix) {
/* 51 */     super(partPrefix, TimeTools.slices(file, maxSliceFileSize, 0L, 0L));
/* 52 */     this.maxSliceFileSize = maxSliceFileSize;
/* 53 */     this.file = file;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean forceCopy(IVideoFile file) {
/* 58 */     return (file.length() <= this.maxSliceFileSize);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean accept(File sliceFile, IVideoSlice slice) {
/* 64 */     if (this.percent <= 0.05D || slice.getTime() <= 5000L || sliceFile.length() <= this.maxSliceFileSize) {
/* 65 */       return true;
/*    */     }
/* 67 */     long smallerSize = (long)(this.percent * (float)this.maxSliceFileSize);
/* 68 */     this.percent = (float)(this.percent - 0.05D);
/* 69 */     setIterator((ISmartIterator)new ArrayIterator((Object[])TimeTools.slices(this.file, smallerSize, slice.start(), 0L)));
/* 70 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/videohandler4j/imp/BySizeVideoSplitter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */