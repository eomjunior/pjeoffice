/*    */ package br.jus.cnj.pje.office.task.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.task.ITarefaMedia;
/*    */ import com.github.filehandler4j.IInputDescriptor;
/*    */ import com.github.progress4j.IQuietlyProgress;
/*    */ import com.github.progress4j.IStage;
/*    */ import com.github.progress4j.imp.Stage;
/*    */ import com.github.taskresolver4j.exception.TaskException;
/*    */ import com.github.utils4j.gui.imp.Dialogs;
/*    */ import com.github.utils4j.gui.imp.SwingTools;
/*    */ import com.github.utils4j.imp.Params;
/*    */ import com.github.videohandler4j.IVideoInfoEvent;
/*    */ import com.github.videohandler4j.imp.VideoDescriptor;
/*    */ import com.github.videohandler4j.imp.VideoOptimizer;
/*    */ import java.util.Optional;
/*    */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*    */ class PjeVideoOptimizerTask
/*    */   extends PjeBasicConverterTask<ITarefaMedia>
/*    */ {
/*    */   private Optimization compressType;
/*    */   
/*    */   private enum Optimization
/*    */   {
/* 49 */     TEAMS("Priorizar menor tamanho sobre a qualidade (TEAMS)", 40),
/* 50 */     CISCO("Priorizar qualidade sobre menor tamanho (CISCO)", 20),
/* 51 */     AGRESSIVE("Diminuir tamanho e qualidade (AGRESSIVO)", 44);
/*    */     
/*    */     private final String description;
/*    */     private final int crf;
/*    */     
/*    */     Optimization(String description, int crf) {
/* 57 */       this.description = description;
/* 58 */       this.crf = crf;
/*    */     }
/*    */ 
/*    */     
/*    */     public String toString() {
/* 63 */       return this.description;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected PjeVideoOptimizerTask(Params request, ITarefaMedia pojo) {
/* 70 */     super(request, pojo, "Otimizado-");
/*    */   }
/*    */ 
/*    */   
/*    */   protected IStage getBeginStage() {
/* 75 */     return (IStage)new Stage("Otimizando vídeo...");
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doValidateTaskParams() throws TaskException, InterruptedException {
/* 80 */     Optional<Optimization> ot = SwingTools.invokeAndWaitT(() -> Dialogs.getOption("Na otimização:", (Object[])Optimization.values(), Optimization.TEAMS));
/* 81 */     if (!ot.isPresent()) {
/* 82 */       throwCancel();
/*    */     }
/* 84 */     this.compressType = ot.get();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void execute(IQuietlyProgress progress, AtomicBoolean success, VideoDescriptor desc, long duration) {
/* 89 */     (new VideoOptimizer(this.compressType.crf))
/* 90 */       .apply((IInputDescriptor)desc)
/* 91 */       .subscribe(e -> status(progress, duration, e), e -> {
/*    */           success.set(false);
/*    */           progress.abort(e);
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/PjeVideoOptimizerTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */