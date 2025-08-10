/*     */ package com.github.taskresolver4j.imp;
/*     */ 
/*     */ import com.github.taskresolver4j.IExecutorContext;
/*     */ import com.github.taskresolver4j.exception.TaskDiscardException;
/*     */ import com.github.taskresolver4j.exception.TaskException;
/*     */ import com.github.utils4j.gui.imp.CancelAlert;
/*     */ import com.github.utils4j.gui.imp.ExceptionAlert;
/*     */ import com.github.utils4j.gui.imp.MessageAlert;
/*     */ import com.github.utils4j.imp.Params;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import java.awt.Image;
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
/*     */ public abstract class TaskBase<T>
/*     */   extends ParamBasedTask<T>
/*     */ {
/*     */   public TaskBase(Params params) {
/*  44 */     super(params);
/*     */   }
/*     */   
/*     */   protected final void throwCancel(String message) throws InterruptedException {
/*  48 */     throwCancel(message, true);
/*     */   }
/*     */   
/*     */   protected final void throwCancel() throws InterruptedException {
/*  52 */     throwCancel(CancelAlert.CANCELED_OPERATION_MESSAGE);
/*     */   }
/*     */   
/*     */   protected final void throwCancel(boolean interrupt) throws InterruptedException {
/*  56 */     throwCancel(CancelAlert.CANCELED_OPERATION_MESSAGE, interrupt);
/*     */   }
/*     */   
/*     */   protected final void throwCancel(String message, boolean interrupt) throws InterruptedException {
/*  60 */     if (interrupt)
/*  61 */       Thread.currentThread().interrupt(); 
/*  62 */     throw (InterruptedException)getProgress().abort(new InterruptedException(message));
/*     */   }
/*     */   
/*     */   protected final void showCancel() {
/*  66 */     ifNotClosing(() -> CancelAlert.show(getIcon()));
/*     */   }
/*     */   
/*     */   protected final IExecutorContext getExecutorContext() {
/*  70 */     return (IExecutorContext)getParameterValue(DefaultTaskRequest.PARAM_EXECUTOR_CONTEXT);
/*     */   }
/*     */   
/*     */   private final boolean isClosing() {
/*  74 */     return getExecutorContext().isClosing();
/*     */   }
/*     */   
/*     */   protected final boolean isBatchState() {
/*  78 */     return getExecutorContext().isBatchState();
/*     */   }
/*     */   
/*     */   protected final void ifNotClosing(Runnable code) {
/*  82 */     if (!isClosing()) {
/*  83 */       code.run();
/*     */     }
/*     */   }
/*     */   
/*     */   protected final void ifBatchStateThrowDiscard(String message) throws TaskDiscardException {
/*  88 */     if (isBatchState()) {
/*  89 */       throw new TaskDiscardException(message);
/*     */     }
/*     */   }
/*     */   
/*     */   protected final void showInfo(String message) {
/*  94 */     ifNotClosing(() -> MessageAlert.showInfo(message, getIcon()));
/*     */   }
/*     */   
/*     */   protected final void showInfo(String message, String textButton) {
/*  98 */     ifNotClosing(() -> MessageAlert.showInfo(message, textButton, getIcon()));
/*     */   }
/*     */   
/*     */   protected final TaskException showFail(String message) {
/* 102 */     return showFail(message, message, (Throwable)null);
/*     */   }
/*     */   
/*     */   protected final TaskException showFail(String message, Throwable cause) {
/* 106 */     return showFail(message, Strings.empty(), cause);
/*     */   }
/*     */   
/*     */   protected final TaskException showFail(String message, String detail) {
/* 110 */     return showFail(message, detail, (Throwable)null);
/*     */   }
/*     */   
/*     */   protected final TaskException showFail(String message, String detail, Throwable cause) {
/* 114 */     ifNotClosing(getAlertFailCode(message, detail, cause));
/* 115 */     return (cause instanceof TaskException) ? (TaskException)cause : new TaskException(message + "\n" + detail, cause);
/*     */   }
/*     */   
/*     */   protected Runnable getAlertFailCode(String message, String detail, Throwable cause) {
/* 119 */     return () -> ExceptionAlert.show(getIcon(), message, detail, cause);
/*     */   }
/*     */   
/*     */   protected String getSupport() {
/* 123 */     return "Informe os detalhes técnicos abaixo ao suporte/callcenter";
/*     */   }
/*     */   
/*     */   protected abstract Image getIcon();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/taskresolver4j/imp/TaskBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */