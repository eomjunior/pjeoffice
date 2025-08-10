/*     */ package com.github.videohandler4j.imp;
/*     */ 
/*     */ import com.github.filehandler4j.IInputFile;
/*     */ import com.github.utils4j.IConstants;
/*     */ import com.github.utils4j.gui.imp.ThrowableTracker;
/*     */ import com.github.utils4j.imp.Directory;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import com.github.utils4j.imp.Threads;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import com.github.videohandler4j.IVideoInfoEvent;
/*     */ import com.github.videohandler4j.imp.event.VideoInfoEvent;
/*     */ import com.github.videohandler4j.imp.event.VideoOutputEvent;
/*     */ import io.reactivex.Emitter;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
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
/*     */ 
/*     */ 
/*     */ class FFMPEGProcessor
/*     */ {
/*     */   private final File outputVideo;
/*     */   private final List<String> commandLine;
/*     */   private final Function<File, Boolean> accepter;
/*     */   
/*     */   FFMPEGProcessor(List<String> commandLine, File outputVideo) {
/*  58 */     this(commandLine, outputVideo, f -> Boolean.valueOf(true));
/*     */   }
/*     */   
/*     */   FFMPEGProcessor(List<String> commandLine, File outputVideo, Function<File, Boolean> accepter) {
/*  62 */     this.commandLine = commandLine;
/*  63 */     this.outputVideo = outputVideo;
/*  64 */     this.accepter = accepter;
/*     */   }
/*     */   
/*     */   private void interruptAndWait(Thread reader) throws InterruptedException {
/*  68 */     reader.interrupt();
/*  69 */     reader.join(3000L);
/*     */   }
/*     */ 
/*     */   
/*     */   final boolean proccess(IInputFile file, Emitter<IVideoInfoEvent> emitter) throws Exception {
/*  74 */     String outputPath = Directory.stringPath(this.outputVideo);
/*  75 */     this.commandLine.add(outputPath);
/*     */     
/*  77 */     Thread current = Thread.currentThread();
/*  78 */     Process process = (new ProcessBuilder(this.commandLine)).redirectErrorStream(true).start();
/*     */     
/*  80 */     emitter.onNext(new VideoInfoEvent("Processing file: " + file.getName() + " output: " + outputPath));
/*     */     
/*  82 */     boolean success = false;
/*  83 */     boolean exit0 = false;
/*     */     
/*  85 */     try (InputStream input = process.getInputStream()) {
/*  86 */       Thread reader = Threads.startDaemon("ffmpeg output reader", () -> {
/*     */             Thread io = Thread.currentThread();
/*     */             try {
/*     */               BufferedReader br = new BufferedReader(new InputStreamReader(input, IConstants.CP_850));
/*     */               String inputLine;
/*     */               while (!io.isInterrupted() && (inputLine = br.readLine()) != null) {
/*     */                 emitter.onNext(new VideoInfoEvent(Strings.replace(inputLine, '%', '#')));
/*     */               }
/*  94 */             } catch (Exception e) {
/*     */               emitter.onNext(new VideoInfoEvent("Fail in thread: " + io.getName() + ": " + e.getMessage()));
/*     */             } 
/*     */           });
/*     */       
/*     */       try {
/*     */         try {
/* 101 */           exit0 = (process.waitFor() == 0);
/* 102 */           success = (exit0 && ((Boolean)this.accepter.apply(this.outputVideo)).booleanValue());
/* 103 */           interruptAndWait(reader);
/* 104 */           if (success) {
/* 105 */             emitter.onNext(new VideoOutputEvent("Generated file " + outputPath, this.outputVideo, file.length()));
/*     */           }
/*     */         } finally {
/* 108 */           Throwables.quietly(process.destroyForcibly()::waitFor);
/*     */         } 
/* 110 */       } catch (InterruptedException e) {
/*     */         try {
/* 112 */           interruptAndWait(reader);
/*     */         } finally {
/* 114 */           current.interrupt();
/*     */         } 
/* 116 */         throw e;
/*     */       } finally {
/* 118 */         reader = null;
/*     */       } 
/*     */     } finally {
/* 121 */       if (!success) {
/* 122 */         this.outputVideo.delete();
/* 123 */         if (!current.isInterrupted() && !exit0) {
/* 124 */           String message = "FFMPEG não processou este vídeo: " + file.getAbsolutePath() + "\n";
/* 125 */           String explainMessage = (outputPath.length() >= 255) ? "O caminho dos arquivos ultrapassa 256 caracteres. Tente diminuir o comprimento do nome do arquivo ou a hierarquia de pastas!" : "Aparentemente o arquivo de vídeo não tem o formato aceito Mp4!";
/*     */ 
/*     */           
/* 128 */           emitter.onNext(new VideoInfoEvent(message + explainMessage));
/* 129 */           throw new Exception(message + ThrowableTracker.DEFAULT.mark(explainMessage));
/*     */         } 
/*     */       } 
/*     */     } 
/* 133 */     return success;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/videohandler4j/imp/FFMPEGProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */