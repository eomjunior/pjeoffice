/*     */ package br.jus.cnj.pje.office.task.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.task.ITarefaMedia;
/*     */ import com.github.progress4j.IQuietlyProgress;
/*     */ import com.github.progress4j.IStage;
/*     */ import com.github.utils4j.imp.DurationTools;
/*     */ import com.github.utils4j.imp.Params;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import com.github.videohandler4j.IVideoFile;
/*     */ import com.github.videohandler4j.IVideoInfoEvent;
/*     */ import com.github.videohandler4j.imp.VideoDescriptor;
/*     */ import com.github.videohandler4j.imp.VideoTools;
/*     */ import com.github.videohandler4j.imp.exception.VideoDurationNotFound;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.time.Duration;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ abstract class PjeBasicConverterTask<T extends ITarefaMedia>
/*     */   extends PjeMediaProcessingTask<T>
/*     */ {
/*     */   private final String prefix;
/*     */   
/*     */   private enum Stage
/*     */     implements IStage
/*     */   {
/*  53 */     DURATION("Calculando duração do vídeo");
/*     */     
/*     */     private final String message;
/*     */     
/*     */     Stage(String message) {
/*  58 */       this.message = message;
/*     */     }
/*     */ 
/*     */     
/*     */     public final String toString() {
/*  63 */       return this.message;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  68 */   private int increment = 1;
/*     */   
/*     */   protected PjeBasicConverterTask(Params request, T pojo) {
/*  71 */     this(request, pojo, Strings.empty());
/*     */   }
/*     */   
/*     */   protected PjeBasicConverterTask(Params request, T pojo, String prefix) {
/*  75 */     super(request, pojo);
/*  76 */     this.prefix = prefix;
/*     */   }
/*     */   
/*     */   protected abstract IStage getBeginStage();
/*     */   
/*     */   protected boolean process(Path file, IQuietlyProgress progress) throws InterruptedException {
/*     */     IVideoFile video;
/*     */     VideoDescriptor desc;
/*     */     try {
/*  85 */       progress.begin(Stage.DURATION);
/*  86 */       video = VideoTools.FFMPEG.create(file.toFile());
/*  87 */       progress.end();
/*  88 */     } catch (VideoDurationNotFound e) {
/*  89 */       LOGGER.error("Não foi possível encontrar duração do vídeo ", (Throwable)e);
/*  90 */       progress.abort((Throwable)e);
/*  91 */       return false;
/*     */     } 
/*     */     
/*  94 */     long duration = video.getDuration().toMillis();
/*     */     
/*  96 */     Path output = file.getParent();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 104 */       desc = (VideoDescriptor)(new VideoDescriptor.Builder(getExtension())).add(video).output(output).namePrefix(this.prefix).build();
/* 105 */     } catch (IOException e1) {
/* 106 */       LOGGER.error("Não foi possível criar pasta " + output.toString(), e1);
/* 107 */       progress.abort(e1);
/* 108 */       return false;
/*     */     } 
/*     */     
/* 111 */     AtomicBoolean success = new AtomicBoolean(true);
/*     */     
/* 113 */     progress.begin(getBeginStage(), 100);
/*     */     
/* 115 */     this.increment = 1;
/*     */     
/* 117 */     execute(progress, success, desc, duration);
/*     */     
/* 119 */     progress.end();
/*     */     
/* 121 */     return success.get();
/*     */   }
/*     */   
/*     */   protected final void status(IQuietlyProgress progress, long duration, IVideoInfoEvent e) {
/* 125 */     String line = Strings.replace(e.getMessage(), '%', '#');
/* 126 */     String mark = "time=";
/* 127 */     int idx = line.indexOf("time=");
/* 128 */     if (idx < 0) {
/* 129 */       progress.info(line, new Object[0]);
/*     */       return;
/*     */     } 
/* 132 */     idx += "time=".length();
/* 133 */     int end = line.indexOf('.', idx);
/* 134 */     if (end < 0) {
/* 135 */       progress.info(line, new Object[0]);
/*     */       return;
/*     */     } 
/* 138 */     String time = line.substring(idx, end);
/* 139 */     Optional<Duration> dtime = DurationTools.parse(time);
/* 140 */     if (!dtime.isPresent()) {
/* 141 */       progress.info(line, new Object[0]);
/*     */       return;
/*     */     } 
/* 144 */     long written = ((Duration)dtime.get()).toMillis();
/* 145 */     float percent = 100.0F * (float)written / (float)duration;
/* 146 */     if (percent < this.increment) {
/* 147 */       progress.info(line, new Object[0]);
/*     */       return;
/*     */     } 
/* 150 */     if (this.increment <= percent)
/* 151 */       progress.step(line, new Object[] { Integer.valueOf(this.increment++) }); 
/* 152 */     if (this.increment <= percent) {
/* 153 */       int diff = (int)(percent - this.increment + 1.0F);
/* 154 */       progress.skip(diff);
/* 155 */       this.increment += diff;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected String getExtension() {
/* 160 */     return Strings.empty();
/*     */   }
/*     */   
/*     */   protected abstract void execute(IQuietlyProgress paramIQuietlyProgress, AtomicBoolean paramAtomicBoolean, VideoDescriptor paramVideoDescriptor, long paramLong);
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/PjeBasicConverterTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */