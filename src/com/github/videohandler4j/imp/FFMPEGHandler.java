/*    */ package com.github.videohandler4j.imp;
/*    */ 
/*    */ import com.github.filehandler4j.IInputFile;
/*    */ import com.github.filehandler4j.imp.AbstractFileHandler;
/*    */ import com.github.utils4j.imp.Containers;
/*    */ import com.github.utils4j.imp.Directory;
/*    */ import com.github.videohandler4j.IVideoInfoEvent;
/*    */ import io.reactivex.Emitter;
/*    */ import java.io.File;
/*    */ import java.nio.file.Path;
/*    */ import java.util.List;
/*    */ import java.util.function.Supplier;
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
/*    */ 
/*    */ public abstract class FFMPEGHandler
/*    */   extends AbstractFileHandler<IVideoInfoEvent>
/*    */ {
/*    */   protected final void handle(IInputFile file, Emitter<IVideoInfoEvent> emitter) throws Exception {
/* 49 */     File ffmpegHome = ((Path)VideoTools.FFMPEG.fullPath().<Throwable>orElseThrow(com.github.videohandler4j.imp.exception.FFMpegNotFoundException::new)).toFile();
/* 50 */     File outputVideo = resolveOutput(file.getShortName() + ".mp4");
/* 51 */     outputVideo.delete();
/*    */     
/* 53 */     List<String> commandLine = Containers.arrayList((Object[])new String[] {
/* 54 */           Directory.stringPath(ffmpegHome), "-y", "-i", file
/*    */ 
/*    */           
/* 57 */           .getAbsolutePath(), "-stats_period", "1.5", "-hide_banner", "-nostdin"
/*    */         });
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 63 */     fillParameters(commandLine);
/*    */     
/* 65 */     (new FFMPEGProcessor(commandLine, outputVideo)).proccess(file, emitter);
/*    */   }
/*    */   
/*    */   protected abstract void fillParameters(List<String> paramList);
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/videohandler4j/imp/FFMPEGHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */