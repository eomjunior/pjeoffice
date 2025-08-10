/*     */ package br.jus.cnj.pje.office.task.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.task.ITarefaVideoDivisaoTamanho;
/*     */ import com.github.filehandler4j.IInputDescriptor;
/*     */ import com.github.progress4j.IQuietlyProgress;
/*     */ import com.github.taskresolver4j.exception.TaskException;
/*     */ import com.github.utils4j.gui.imp.Dialogs;
/*     */ import com.github.utils4j.gui.imp.SwingTools;
/*     */ import com.github.utils4j.imp.Directory;
/*     */ import com.github.utils4j.imp.Params;
/*     */ import com.github.utils4j.imp.Sizes;
/*     */ import com.github.videohandler4j.IVideoFile;
/*     */ import com.github.videohandler4j.IVideoInfoEvent;
/*     */ import com.github.videohandler4j.imp.BySizeVideoSplitter;
/*     */ import com.github.videohandler4j.imp.VideoDescriptor;
/*     */ import com.github.videohandler4j.imp.VideoTools;
/*     */ import com.github.videohandler4j.imp.exception.VideoDurationNotFound;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ class PjeBySizeVideoSplitterTask
/*     */   extends PjeMediaProcessingTask<ITarefaVideoDivisaoTamanho>
/*     */ {
/*     */   private double tamanho;
/*     */   
/*     */   protected PjeBySizeVideoSplitterTask(Params request, ITarefaVideoDivisaoTamanho pojo) {
/*  57 */     super(request, pojo);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doValidateTaskParams() throws TaskException, InterruptedException {
/*  62 */     ITarefaVideoDivisaoTamanho pojo = getPojoParams();
/*  63 */     this.tamanho = pojo.getTamanho();
/*  64 */     if (this.tamanho == 0.0D) {
/*  65 */       Optional<Double> total = SwingTools.invokeAndWait(() -> Dialogs.getDouble("Tamanho máximo do arquivo (MB):", Double.valueOf(90.0D), Double.valueOf(2.0D), Double.valueOf(Double.MAX_VALUE)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  73 */       this.tamanho = ((Double)total.<Throwable>orElseThrow(InterruptedException::new)).doubleValue();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean process(Path file, IQuietlyProgress progress) throws InterruptedException {
/*     */     IVideoFile video;
/*     */     VideoDescriptor desc;
/*  80 */     progress.begin(PjeMediaProcessingTask.SplitterStage.SPLITTING_PATIENT);
/*     */ 
/*     */     
/*     */     try {
/*  84 */       video = VideoTools.FFMPEG.create(file.toFile());
/*  85 */     } catch (VideoDurationNotFound e) {
/*  86 */       LOGGER.error("Não foi possível encontrar duração do vídeo ", (Throwable)e);
/*  87 */       progress.abort((Throwable)e);
/*  88 */       return false;
/*     */     } 
/*     */     
/*  91 */     Path output = file.getParent().resolve(video.getShortName() + "_(VÍDEOS DE ATÉ " + Sizes.defaultFormat(Sizes.MB.toBytes(this.tamanho)) + ")");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  98 */       desc = (VideoDescriptor)(new VideoDescriptor.Builder(".mp4")).add(video).output(output).build();
/*  99 */     } catch (IOException e1) {
/* 100 */       LOGGER.error("Não foi possível criar pasta " + output.toString(), e1);
/* 101 */       progress.abort(e1);
/* 102 */       return false;
/*     */     } 
/*     */     
/* 105 */     AtomicBoolean success = new AtomicBoolean(true);
/* 106 */     (new BySizeVideoSplitter(video, Sizes.MB.toBytes(this.tamanho)))
/* 107 */       .apply((IInputDescriptor)desc)
/* 108 */       .subscribe(e -> progress.info(e.getMessage(), new Object[0]), e -> {
/*     */           success.set(false);
/*     */ 
/*     */           
/*     */           Directory.deleteQuietly(output.toFile());
/*     */           
/*     */           progress.abort(e);
/*     */         });
/*     */     
/* 117 */     progress.end();
/*     */     
/* 119 */     return success.get();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/PjeBySizeVideoSplitterTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */