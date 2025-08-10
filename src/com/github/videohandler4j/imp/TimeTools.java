/*    */ package com.github.videohandler4j.imp;
/*    */ 
/*    */ import com.github.utils4j.IHasDuration;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import com.github.videohandler4j.IVideoFile;
/*    */ import com.github.videohandler4j.IVideoSlice;
/*    */ import java.time.Duration;
/*    */ import java.util.ArrayList;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TimeTools
/*    */ {
/*    */   public static IVideoSlice[] slices(IVideoFile file, long maxSliceFileSize) {
/* 44 */     return slices(file, maxSliceFileSize, 0L);
/*    */   }
/*    */   
/*    */   public static IVideoSlice[] slices(IVideoFile file, long maxSliceFileSize, long sliceStart) {
/* 48 */     return slices(file, maxSliceFileSize, sliceStart, 0L);
/*    */   }
/*    */   
/*    */   public static IVideoSlice[] slices(IVideoFile file, long maxSliceFileSize, long sliceStart, long previousMarging) {
/* 52 */     Args.requireNonNull(file, "file is null");
/* 53 */     Args.requireZeroPositive(maxSliceFileSize, "maxSize < 0");
/* 54 */     Args.requireZeroPositive(sliceStart, "sliceStart < 0");
/* 55 */     long fileDurationMillis = file.getDuration().toMillis();
/* 56 */     long fileSize = file.length();
/* 57 */     long sizePerDuration = fileSize / fileDurationMillis;
/* 58 */     long sliceDurationMillis = maxSliceFileSize / sizePerDuration;
/* 59 */     return slices((IHasDuration)file, Duration.ofMillis(sliceDurationMillis), sliceStart, previousMarging);
/*    */   }
/*    */   
/*    */   public static IVideoSlice[] slices(IHasDuration file, Duration maxDurationSlice) {
/* 63 */     return slices(file, maxDurationSlice, 0L);
/*    */   }
/*    */   
/*    */   public static IVideoSlice[] slices(IHasDuration file, Duration maxDurationSlice, long sliceStart) {
/* 67 */     return slices(file, maxDurationSlice, sliceStart, 0L);
/*    */   }
/*    */   
/*    */   public static IVideoSlice[] slices(IHasDuration file, Duration maxDurationSlice, long sliceStart, long previousMarging) {
/* 71 */     Args.requireNonNull(file, "file is null");
/* 72 */     Args.requireNonNull(maxDurationSlice, "maxDurationSlice is null");
/* 73 */     Args.requireZeroPositive(sliceStart, "sliceStart < 0");
/* 74 */     List<IVideoSlice> slices = new ArrayList<>();
/* 75 */     long durationVideoMillis = file.getDuration().toMillis();
/* 76 */     long durationSliceMillis = maxDurationSlice.toMillis(); long start;
/* 77 */     for (start = sliceStart; start < durationVideoMillis; start += durationSliceMillis) {
/* 78 */       slices.add(new DefaultVideoSlice(Math.max(0L, start - previousMarging), Math.min(durationVideoMillis, start + durationSliceMillis)));
/*    */     }
/* 80 */     return slices.<IVideoSlice>toArray(new IVideoSlice[slices.size()]);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/videohandler4j/imp/TimeTools.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */