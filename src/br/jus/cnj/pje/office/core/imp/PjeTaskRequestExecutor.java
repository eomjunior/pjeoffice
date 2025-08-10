/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeRequest;
/*    */ import br.jus.cnj.pje.office.core.IPjeResponse;
/*    */ import br.jus.cnj.pje.office.core.imp.sec.PjeSecurity;
/*    */ import br.jus.cnj.pje.office.signer4j.imp.PjeTokenDevice;
/*    */ import br.jus.cnj.pje.office.task.ITaskExecutorParams;
/*    */ import com.github.progress4j.IProgressFactory;
/*    */ import com.github.progress4j.IProgressView;
/*    */ import com.github.progress4j.imp.ProgressFactories;
/*    */ import com.github.taskresolver4j.IExceptionContext;
/*    */ import com.github.taskresolver4j.ITaskRequest;
/*    */ import com.github.taskresolver4j.imp.TaskRequestExecutor;
/*    */ import com.github.utils4j.gui.IThrowableTracker;
/*    */ import com.github.utils4j.gui.imp.ExceptionAlert;
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
/*    */ class PjeTaskRequestExecutor
/*    */   extends TaskRequestExecutor<IPjeRequest, IPjeResponse, PjeTaskRequest>
/*    */ {
/*    */   public PjeTaskRequestExecutor() {
/* 45 */     super(PjeRequestResolver.INSTANCE, (IProgressFactory)ProgressFactories.THREAD);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void beginExecution(IProgressView progress) {
/* 50 */     progress.display();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void showFailure(IExceptionContext context) {
/* 55 */     ExceptionAlert.show(context.getMessage(), context.getDetail(), context.getCause(), (IThrowableTracker)PjeServerTracker.RESPONSE);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void handleExhaustedState() {
/* 60 */     PjeWebGlobal.recycleAll();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onRequestResolved(PjeTaskRequest request) {
/* 65 */     request.of(ITaskExecutorParams.PJE_REQUEST_EXECUTOR, this.executor);
/* 66 */     request.of(ITaskExecutorParams.PJE_TOKEN_ACCESSOR, PjeTokenDevice.ACCESSOR);
/* 67 */     request.of(ITaskExecutorParams.PJE_SECURITY_GRANTOR, PjeSecurity.GRANTOR);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeTaskRequestExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */