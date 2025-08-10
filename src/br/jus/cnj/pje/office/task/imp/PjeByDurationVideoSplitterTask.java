/*     */ package br.jus.cnj.pje.office.task.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.task.ITarefaVideoDivisaoDuracao;
/*     */ import com.github.filehandler4j.IInputDescriptor;
/*     */ import com.github.progress4j.IQuietlyProgress;
/*     */ import com.github.taskresolver4j.exception.TaskException;
/*     */ import com.github.utils4j.IHasDuration;
/*     */ import com.github.utils4j.gui.imp.Dialogs;
/*     */ import com.github.utils4j.gui.imp.SwingTools;
/*     */ import com.github.utils4j.imp.Directory;
/*     */ import com.github.utils4j.imp.Params;
/*     */ import com.github.videohandler4j.IVideoFile;
/*     */ import com.github.videohandler4j.IVideoInfoEvent;
/*     */ import com.github.videohandler4j.imp.ByDurationVideoSplitter;
/*     */ import com.github.videohandler4j.imp.VideoDescriptor;
/*     */ import com.github.videohandler4j.imp.VideoTools;
/*     */ import com.github.videohandler4j.imp.exception.VideoDurationNotFound;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.time.Duration;
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
/*     */ class PjeByDurationVideoSplitterTask
/*     */   extends PjeMediaProcessingTask<ITarefaVideoDivisaoDuracao>
/*     */ {
/*     */   private long duracao;
/*     */   
/*     */   protected PjeByDurationVideoSplitterTask(Params request, ITarefaVideoDivisaoDuracao pojo) {
/*  56 */     super(request, pojo);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doValidateTaskParams() throws TaskException, InterruptedException {
/*  61 */     ITarefaVideoDivisaoDuracao pojo = getPojoParams();
/*  62 */     this.duracao = pojo.getDuracao();
/*  63 */     if (this.duracao <= 0L) {
/*  64 */       Optional<Integer> total = SwingTools.invokeAndWait(() -> Dialogs.getInteger("O tempo máximo do vídeo (minutos):", Integer.valueOf(10), Integer.valueOf(1), Integer.valueOf(2147483646)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  72 */       this.duracao = ((Integer)total.<Throwable>orElseThrow(InterruptedException::new)).intValue();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean process(Path file, IQuietlyProgress progress) throws InterruptedException {
/*     */     IVideoFile video;
/*     */     VideoDescriptor desc;
/*  79 */     progress.begin(PjeMediaProcessingTask.SplitterStage.SPLITTING_PATIENT);
/*     */     
/*  81 */     Path output = file.getParent();
/*     */     
/*     */     try {
/*  84 */       video = VideoTools.FFMPEG.create(file.toFile());
/*  85 */     } catch (VideoDurationNotFound e) {
/*  86 */       LOGGER.error("Não foi possível encontrar duração do vídeo ", (Throwable)e);
/*  87 */       progress.abort((Throwable)e);
/*  88 */       return false;
/*     */     } 
/*  90 */     Path folder = output.resolve(video.getShortName() + "_(VÍDEOS DE ATÉ " + this.duracao + " MINUTO" + ((this.duracao > 1L) ? "S)" : ")"));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  96 */       desc = (VideoDescriptor)(new VideoDescriptor.Builder(".mp4")).add(video).output(folder).build();
/*  97 */     } catch (IOException e1) {
/*  98 */       LOGGER.error("Não foi possível criar pasta " + output.toString(), e1);
/*  99 */       progress.abort(e1);
/* 100 */       return false;
/*     */     } 
/*     */     
/* 103 */     AtomicBoolean success = new AtomicBoolean(true);
/* 104 */     (new ByDurationVideoSplitter((IHasDuration)video, Duration.ofMinutes(this.duracao)))
/* 105 */       .apply((IInputDescriptor)desc)
/* 106 */       .subscribe(e -> progress.info(e.getMessage(), new Object[0]), e -> {
/*     */           success.set(false);
/*     */ 
/*     */           
/*     */           Directory.deleteQuietly(folder.toFile());
/*     */           
/*     */           progress.abort(e);
/*     */         });
/*     */     
/* 115 */     progress.end();
/*     */     
/* 117 */     return success.get();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/PjeByDurationVideoSplitterTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */