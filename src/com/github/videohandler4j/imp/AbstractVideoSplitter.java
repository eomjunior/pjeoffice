/*     */ package com.github.videohandler4j.imp;
/*     */ 
/*     */ import com.github.filehandler4j.IInputFile;
/*     */ import com.github.filehandler4j.imp.AbstractFileRageHandler;
/*     */ import com.github.utils4j.IHasDuration;
/*     */ import com.github.utils4j.ISmartIterator;
/*     */ import com.github.utils4j.imp.ArrayIterator;
/*     */ import com.github.utils4j.imp.Containers;
/*     */ import com.github.utils4j.imp.Directory;
/*     */ import com.github.utils4j.imp.DurationTools;
/*     */ import com.github.utils4j.imp.States;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import com.github.videohandler4j.IVideoFile;
/*     */ import com.github.videohandler4j.IVideoInfoEvent;
/*     */ import com.github.videohandler4j.IVideoSlice;
/*     */ import com.github.videohandler4j.imp.event.VideoOutputEvent;
/*     */ import io.reactivex.Emitter;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.time.Duration;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractVideoSplitter
/*     */   extends AbstractFileRageHandler<IVideoInfoEvent, IVideoSlice>
/*     */ {
/*     */   protected static final long DEFAULT_PREVIOUS_MARGING = 0L;
/*  62 */   private File currentOutput = null;
/*     */   private final boolean partPrefix;
/*     */   
/*     */   public AbstractVideoSplitter() {
/*  66 */     this(true);
/*     */   }
/*     */   
/*     */   public AbstractVideoSplitter(boolean partPrefix) {
/*  70 */     this(partPrefix, new IVideoSlice[] { new DefaultVideoSlice() });
/*     */   }
/*     */   
/*     */   public AbstractVideoSplitter(boolean partPrefix, IVideoSlice... ranges) {
/*  74 */     this(partPrefix, (ISmartIterator<IVideoSlice>)new ArrayIterator((Object[])ranges));
/*     */   }
/*     */   
/*     */   public AbstractVideoSplitter(boolean partPrefix, ISmartIterator<IVideoSlice> iterator) {
/*  78 */     super(iterator);
/*  79 */     reset();
/*  80 */     this.partPrefix = partPrefix;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void handleError(Throwable e) {
/*  85 */     clearOutput();
/*  86 */     super.handleError(e);
/*     */   }
/*     */   
/*     */   private void clearOutput() {
/*  90 */     if (this.currentOutput != null) {
/*  91 */       this.currentOutput.delete();
/*  92 */       this.currentOutput = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() {
/*  98 */     this.currentOutput = null;
/*  99 */     super.reset();
/*     */   }
/*     */   
/*     */   private String computeOutputFileName(IVideoFile inputFile, long sliceId, IVideoSlice videoSlice) {
/* 103 */     StringBuilder fileName = new StringBuilder();
/* 104 */     if (this.partPrefix) {
/* 105 */       fileName.append("Parte " + Strings.padStart(sliceId, 2) + " - ");
/*     */     } else {
/* 107 */       fileName.append(inputFile.getShortName()).append('_');
/* 108 */     }  fileName.append(videoSlice.outputFileName((IHasDuration)inputFile));
/* 109 */     if (this.partPrefix)
/* 110 */       fileName.append('_').append(inputFile.getShortName()); 
/* 111 */     return fileName.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void handle(IInputFile f, Emitter<IVideoInfoEvent> emitter) throws Exception {
/* 116 */     States.requireTrue(f instanceof IVideoFile, "file is not instance of VideoFile, please use VideoDescriptor instead");
/* 117 */     IVideoFile file = (IVideoFile)f;
/*     */     
/* 119 */     int sliceId = 1;
/*     */     
/* 121 */     if (forceCopy(file)) {
/* 122 */       this.currentOutput = resolveOutput(computeOutputFileName(file, sliceId, new DefaultVideoSlice(0L)));
/* 123 */       try (OutputStream out = new FileOutputStream(this.currentOutput)) {
/* 124 */         Files.copy(file.toPath(), out);
/*     */       } 
/* 126 */       emitter.onNext(new VideoOutputEvent("Generated file " + this.currentOutput, this.currentOutput, file.getDuration().toMillis()));
/*     */       
/*     */       return;
/*     */     } 
/* 130 */     IVideoSlice next = (IVideoSlice)nextSlice();
/*     */     
/* 132 */     if (next != null) {
/* 133 */       File ffmpegHome = ((Path)VideoTools.FFMPEG.fullPath().<Throwable>orElseThrow(com.github.videohandler4j.imp.exception.FFMpegNotFoundException::new)).toFile();
/*     */       
/*     */       do {
/* 136 */         checkInterrupted();
/*     */         
/* 138 */         this.currentOutput = resolveOutput(computeOutputFileName(file, sliceId, next));
/*     */         
/* 140 */         this.currentOutput.delete();
/*     */         
/* 142 */         long start = next.start();
/*     */         
/* 144 */         Duration duration = Duration.ofMillis(next.end((IHasDuration)file) - start);
/*     */         
/* 146 */         List<String> commandLine = Containers.arrayList((Object[])new String[] { 
/* 147 */               Directory.stringPath(ffmpegHome), "-y", "-nostdin", "-threads", 
/*     */ 
/*     */ 
/*     */               
/* 151 */               Long.toString(Math.max(Runtime.getRuntime().availableProcessors() - 1, 1)), "-hide_banner", "-ss", 
/*     */ 
/*     */               
/* 154 */               DurationTools.toString(start) + ".000", "-i", file
/*     */               
/* 156 */               .getAbsolutePath(), "-max_muxing_queue_size", "89478485" });
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 161 */         commandLine.add("-codec");
/* 162 */         commandLine.add("copy");
/* 163 */         commandLine.add("-t");
/* 164 */         commandLine.add(Long.toString(duration.getSeconds()));
/*     */         
/* 166 */         IVideoSlice slice = next;
/*     */         
/* 168 */         FFMPEGProcessor processor = new FFMPEGProcessor(commandLine, this.currentOutput, o -> Boolean.valueOf(accept(o, slice)));
/*     */         
/* 170 */         if (processor.proccess((IInputFile)file, emitter)) {
/* 171 */           sliceId++;
/*     */         } else {
/* 173 */           checkInterrupted();
/*     */         }
/*     */       
/* 176 */       } while ((next = (IVideoSlice)nextSlice()) != null);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean forceCopy(IVideoFile file) {
/* 181 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean accept(File outputFile, IVideoSlice slice) {
/* 185 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/videohandler4j/imp/AbstractVideoSplitter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */