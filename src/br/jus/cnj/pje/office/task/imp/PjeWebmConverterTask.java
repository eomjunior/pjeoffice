/*    */ package br.jus.cnj.pje.office.task.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.task.ITarefaMedia;
/*    */ import com.github.filehandler4j.IInputDescriptor;
/*    */ import com.github.progress4j.IQuietlyProgress;
/*    */ import com.github.progress4j.IStage;
/*    */ import com.github.progress4j.imp.Stage;
/*    */ import com.github.utils4j.imp.Params;
/*    */ import com.github.videohandler4j.IVideoInfoEvent;
/*    */ import com.github.videohandler4j.imp.VideoDescriptor;
/*    */ import com.github.videohandler4j.imp.WebmConverter;
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
/*    */ 
/*    */ 
/*    */ class PjeWebmConverterTask
/*    */   extends PjeBasicConverterTask<ITarefaMedia>
/*    */ {
/*    */   protected PjeWebmConverterTask(Params request, ITarefaMedia pojo) {
/* 44 */     super(request, pojo, "WEBM-");
/*    */   }
/*    */ 
/*    */   
/*    */   protected IStage getBeginStage() {
/* 49 */     return (IStage)new Stage("Convertendo para webm...");
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getExtension() {
/* 54 */     return ".webm";
/*    */   }
/*    */ 
/*    */   
/*    */   protected void execute(IQuietlyProgress progress, AtomicBoolean success, VideoDescriptor desc, long duration) {
/* 59 */     (new WebmConverter())
/* 60 */       .apply((IInputDescriptor)desc)
/* 61 */       .subscribe(e -> status(progress, duration, e), e -> {
/*    */           success.set(false);
/*    */           progress.abort(e);
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/PjeWebmConverterTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */