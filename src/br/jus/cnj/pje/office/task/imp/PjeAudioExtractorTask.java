/*     */ package br.jus.cnj.pje.office.task.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.task.ITarefaVideoExtracaoAudio;
/*     */ import com.github.filehandler4j.IFileHandler;
/*     */ import com.github.progress4j.IQuietlyProgress;
/*     */ import com.github.progress4j.IStage;
/*     */ import com.github.progress4j.imp.Stage;
/*     */ import com.github.taskresolver4j.exception.TaskException;
/*     */ import com.github.taskresolver4j.exception.TaskParameterInvalidException;
/*     */ import com.github.utils4j.gui.imp.Dialogs;
/*     */ import com.github.utils4j.gui.imp.SwingTools;
/*     */ import com.github.utils4j.imp.Params;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import com.github.videohandler4j.IVideoInfoEvent;
/*     */ import com.github.videohandler4j.imp.Mp3AudioExtractor;
/*     */ import com.github.videohandler4j.imp.OggAudioExtractor;
/*     */ import com.github.videohandler4j.imp.VideoDescriptor;
/*     */ import io.reactivex.Observable;
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
/*     */ class PjeAudioExtractorTask
/*     */   extends PjeBasicConverterTask<ITarefaVideoExtracaoAudio>
/*     */ {
/*     */   private Media tipo;
/*     */   
/*     */   private enum Media
/*     */     implements Supplier<IFileHandler<IVideoInfoEvent>>
/*     */   {
/*  56 */     MP3("MP3")
/*     */     {
/*     */       public IFileHandler<IVideoInfoEvent> get() {
/*  59 */         return (IFileHandler<IVideoInfoEvent>)new Mp3AudioExtractor();
/*     */       }
/*     */     },
/*  62 */     OGG("OGG")
/*     */     {
/*     */       public IFileHandler<IVideoInfoEvent> get() {
/*  65 */         return (IFileHandler<IVideoInfoEvent>)new OggAudioExtractor();
/*     */       }
/*     */     };
/*     */     
/*     */     private final String key;
/*     */     
/*     */     Media(String key) {
/*  72 */       this.key = key;
/*     */     }
/*     */ 
/*     */     
/*     */     public final String toString() {
/*  77 */       return this.key;
/*     */     }
/*     */     
/*     */     static Media of(String media) throws TaskParameterInvalidException {
/*  81 */       media = Strings.trim(media).toUpperCase();
/*  82 */       for (Media m : values()) {
/*  83 */         if (media.equals(m.key))
/*  84 */           return m; 
/*  85 */       }  throw new TaskParameterInvalidException("Media '" + media + "' é inválida!");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected PjeAudioExtractorTask(Params request, ITarefaVideoExtracaoAudio pojo) {
/*  92 */     super(request, pojo, "Audio-");
/*     */   }
/*     */ 
/*     */   
/*     */   protected IStage getBeginStage() {
/*  97 */     return (IStage)new Stage("Extraindo áudio...");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doValidateTaskParams() throws TaskException, InterruptedException {
/*     */     Optional<Media> ot;
/*     */     try {
/* 104 */       ot = Optional.of(Media.of(PjeTaskChecker.<String>checkIfPresent(getPojoParams().getTipo(), "tipo")));
/* 105 */     } catch (Exception e) {
/* 106 */       ot = SwingTools.invokeAndWaitT(() -> Dialogs.getOption("Escolha um formato de áudio:", (Object[])Media.values()));
/*     */     } 
/* 108 */     if (!ot.isPresent()) {
/* 109 */       throwCancel();
/*     */     }
/* 111 */     this.tipo = ot.get();
/*     */   }
/*     */ 
/*     */   
/*     */   protected final String getExtension() {
/* 116 */     return "." + this.tipo.name().toLowerCase();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void execute(IQuietlyProgress progress, AtomicBoolean success, VideoDescriptor desc, long duration) {
/* 121 */     ((Observable)this.tipo.get().apply(desc)).subscribe(e -> status(progress, duration, e), e -> {
/*     */           success.set(false);
/*     */           progress.abort(e);
/*     */         });
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/PjeAudioExtractorTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */