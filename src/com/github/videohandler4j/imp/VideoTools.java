/*     */ package com.github.videohandler4j.imp;
/*     */ 
/*     */ import com.github.filehandler4j.imp.FileWrapper;
/*     */ import com.github.utils4j.IConstants;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.DurationTools;
/*     */ import com.github.utils4j.imp.Environment;
/*     */ import com.github.utils4j.imp.Streams;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import com.github.videohandler4j.IVideoFile;
/*     */ import com.github.videohandler4j.imp.exception.VideoDurationNotFound;
/*     */ import java.io.File;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.Path;
/*     */ import java.time.Duration;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Supplier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum VideoTools
/*     */ {
/*  49 */   FFMPEG("ffmpeg.exe");
/*     */   
/*     */   private final String fileName;
/*     */   
/*     */   VideoTools(String fileName) {
/*  54 */     this.fileName = fileName;
/*     */   }
/*     */   
/*     */   public final Optional<Path> fullPath() {
/*  58 */     return Environment.resolveTo("FFMPEG_HOME", this.fileName, true, true);
/*     */   }
/*     */   
/*     */   public IVideoFile create(File file) throws VideoDurationNotFound, InterruptedException {
/*  62 */     Args.requireNonNull(file, "input is null"); try {
/*     */       String output;
/*  64 */       Path ffmpeg = fullPath().<Throwable>orElseThrow(com.github.videohandler4j.imp.exception.FFMpegNotFoundException::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  70 */       Process process = (new ProcessBuilder(new String[] { ffmpeg.toFile().getCanonicalPath(), "-i", file.getAbsolutePath(), "-hide_banner" })).redirectErrorStream(true).start();
/*     */ 
/*     */       
/*  73 */       try (InputStream input = process.getInputStream()) {
/*  74 */         output = Streams.readOutStream(input, IConstants.CP_850).get();
/*  75 */         process.waitFor();
/*     */       } finally {
/*  77 */         Throwables.quietly(process.destroyForcibly()::waitFor);
/*     */       } 
/*     */       
/*  80 */       String durationPrefix = "Duration: ";
/*  81 */       int idx = output.indexOf("Duration: ");
/*  82 */       if (idx < 0) {
/*  83 */         throw new VideoDurationNotFound(output);
/*     */       }
/*  85 */       int length = output.length();
/*  86 */       int start = idx += "Duration: ".length();
/*     */       char chr;
/*  88 */       while (idx < length && (Character.isDigit(chr = output.charAt(idx)) || chr == ':')) {
/*  89 */         idx++;
/*     */       }
/*  91 */       String durationText = output.substring(start, idx);
/*  92 */       Duration duration = (Duration)DurationTools.parse(durationText).orElseThrow(VideoDurationNotFound::new);
/*  93 */       return new VideoFile(file, duration);
/*  94 */     } catch (InterruptedException e) {
/*  95 */       throw e;
/*  96 */     } catch (VideoDurationNotFound e) {
/*  97 */       throw e;
/*  98 */     } catch (Exception e) {
/*  99 */       throw new VideoDurationNotFound(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static class VideoFile
/*     */     extends FileWrapper implements IVideoFile {
/*     */     private final Duration duration;
/*     */     
/*     */     private VideoFile(File file, Duration duration) {
/* 108 */       super(file);
/* 109 */       this.duration = duration;
/*     */     }
/*     */ 
/*     */     
/*     */     public Duration getDuration() {
/* 114 */       return this.duration;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/videohandler4j/imp/VideoTools.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */