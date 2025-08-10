/*     */ package com.github.taskresolver4j.imp;
/*     */ 
/*     */ import com.github.progress4j.IProgressFactory;
/*     */ import com.github.progress4j.IProgressView;
/*     */ import com.github.progress4j.IStage;
/*     */ import com.github.progress4j.IState;
/*     */ import com.github.taskresolver4j.IExceptionContext;
/*     */ import com.github.taskresolver4j.IExecutorContext;
/*     */ import com.github.taskresolver4j.IFailureAlerter;
/*     */ import com.github.taskresolver4j.IRequestResolver;
/*     */ import com.github.taskresolver4j.ITask;
/*     */ import com.github.taskresolver4j.ITaskRequest;
/*     */ import com.github.taskresolver4j.ITaskRequestExecutor;
/*     */ import com.github.taskresolver4j.ITaskResponse;
/*     */ import com.github.taskresolver4j.exception.TaskDiscardException;
/*     */ import com.github.taskresolver4j.exception.TaskEscapeException;
/*     */ import com.github.taskresolver4j.exception.TaskExecutorDiscartingException;
/*     */ import com.github.taskresolver4j.exception.TaskExecutorException;
/*     */ import com.github.taskresolver4j.exception.TaskExecutorFinishingException;
/*     */ import com.github.taskresolver4j.exception.TaskExhaustedEscapeException;
/*     */ import com.github.taskresolver4j.exception.TaskResolverException;
/*     */ import com.github.utils4j.gui.imp.ExceptionAlert;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.BooleanTimeout;
/*     */ import com.github.utils4j.imp.Services;
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ public class TaskRequestExecutor<I, O, R extends ITaskRequest<O>>
/*     */   implements ITaskRequestExecutor<I, O>, IExecutorContext
/*     */ {
/*  63 */   protected static final Logger LOGGER = LoggerFactory.getLogger(TaskRequestExecutor.class);
/*     */   
/*     */   private final IProgressFactory factory;
/*     */   
/*     */   private volatile boolean closing = false;
/*     */   
/*     */   protected final ExecutorService executor;
/*     */   
/*     */   private final IRequestResolver<I, O, R> resolver;
/*     */   
/*  73 */   private final AtomicInteger runningTasks = new AtomicInteger(0);
/*     */   
/*  75 */   private final BooleanTimeout discarding = new BooleanTimeout("task-executor");
/*     */   
/*  77 */   private final IFailureAlerter alerter = new AsyncFailureAlerter(this::showFailure, this::isRunningInBatch);
/*     */   
/*     */   private enum Stage implements IStage {
/*  80 */     REQUEST_HANDLING("Tratando requisição"),
/*     */     
/*  82 */     PROCESSING_TASK("Processando a tarefa");
/*     */     
/*     */     private final String message;
/*     */     
/*     */     Stage(String message) {
/*  87 */       this.message = message;
/*     */     }
/*     */ 
/*     */     
/*     */     public final String toString() {
/*  92 */       return this.message;
/*     */     }
/*     */   }
/*     */   
/*     */   protected TaskRequestExecutor(IRequestResolver<I, O, R> resolver, IProgressFactory factory) {
/*  97 */     this(resolver, factory, Executors.newCachedThreadPool());
/*     */   }
/*     */   
/*     */   protected TaskRequestExecutor(IRequestResolver<I, O, R> resolver, IProgressFactory factory, ExecutorService executor) {
/* 101 */     this.resolver = (IRequestResolver<I, O, R>)Args.requireNonNull(resolver, "resolver is null");
/* 102 */     this.factory = (IProgressFactory)Args.requireNonNull(factory, "factory is null");
/* 103 */     this.executor = (ExecutorService)Args.requireNonNull(executor, "executor is null");
/*     */   }
/*     */ 
/*     */   
/*     */   public final void notifyClosing() {
/* 108 */     this.closing = true;
/* 109 */     this.factory.interrupt();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void notifyOpening() {
/* 114 */     this.closing = false;
/* 115 */     bindCanceller();
/*     */   }
/*     */   
/*     */   private void bindCanceller() {
/* 119 */     this.factory.ifCanceller(this.discarding::setTrue);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void close() throws InterruptedException {
/* 124 */     Services.shutdownNow(this.executor, 2L, 3);
/* 125 */     this.closing = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isClosing() {
/* 130 */     return this.closing;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isRunningInBatch() {
/* 135 */     return (getRunningTasks() > 1L);
/*     */   }
/*     */ 
/*     */   
/*     */   public final long getRunningTasks() {
/* 140 */     return this.runningTasks.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isBatchState() {
/* 145 */     return (this.discarding.isTrue() || isRunningInBatch());
/*     */   }
/*     */ 
/*     */   
/*     */   public final void async(Runnable runnable) {
/* 150 */     this.executor.execute(runnable);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onRequestResolved(R taskRequest) {}
/*     */   
/*     */   protected void beginExecution(IProgressView progress) {
/* 157 */     progress.display();
/*     */   }
/*     */   
/*     */   protected void showFailure(IExceptionContext context) {
/* 161 */     ExceptionAlert.show(context.getMessage(), context.getDetail(), context.getCause());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void handleExhaustedState() {}
/*     */ 
/*     */   
/*     */   public final void alert(IExceptionContext context) {
/* 169 */     this.alerter.alert(context);
/*     */   }
/*     */   
/*     */   private void throwExecutorDiscartingException(Throwable cause) throws TaskExecutorDiscartingException {
/* 173 */     this.factory.cancel(Thread.currentThread());
/* 174 */     throw new TaskExecutorDiscartingException(cause);
/*     */   }
/*     */   
/*     */   protected void endExecution(IProgressView progress) {
/*     */     try {
/* 179 */       progress.undisplay();
/* 180 */       progress.stackTracer(s -> LOGGER.info(s.toString()));
/*     */     } finally {
/* 182 */       progress.dispose();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void checkAvailability() throws TaskExecutorException {
/* 187 */     if (this.closing) {
/* 188 */       throw new TaskExecutorFinishingException();
/*     */     }
/* 190 */     if (this.discarding.isTrue()) {
/* 191 */       throw new TaskExecutorDiscartingException();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final void execute(I request, O response) throws TaskExecutorException {
/* 197 */     checkAvailability();
/* 198 */     this.runningTasks.incrementAndGet();
/*     */     try {
/* 200 */       IProgressView progress = (IProgressView)this.factory.get();
/*     */       try {
/*     */         ITaskRequest iTaskRequest;
/* 203 */         beginExecution(progress);
/*     */         
/* 205 */         progress.begin(Stage.REQUEST_HANDLING, 2);
/* 206 */         progress.step("Resolvendo URL", new Object[0]);
/*     */ 
/*     */         
/*     */         try {
/* 210 */           iTaskRequest = this.resolver.resolve(request);
/* 211 */         } catch (TaskResolverException e) {
/* 212 */           throw new TaskExecutorException("Não foi possível resolver a requisição", e);
/*     */         } 
/*     */         
/* 215 */         progress.step("Notificando criação de requisção", new Object[0]);
/* 216 */         onRequestResolved((R)iTaskRequest);
/* 217 */         progress.end();
/*     */         
/* 219 */         ITask<O> task = iTaskRequest.getTask(this.factory, this);
/*     */         
/*     */         try {
/* 222 */           progress.begin(Stage.PROCESSING_TASK);
/* 223 */           progress.info("Tarefa '%s'", new Object[] { task.getId() });
/*     */           
/* 225 */           ITaskResponse<O> output = (ITaskResponse<O>)task.get();
/*     */           
/*     */           try {
/* 228 */             output.processResponse(response);
/* 229 */           } catch (IOException e) {
/* 230 */             LOGGER.warn("Exceção no processamento da resposta", e);
/* 231 */             progress.abort(e);
/*     */             
/*     */             return;
/*     */           } 
/* 235 */           progress.end();
/*     */         } finally {
/*     */           
/* 238 */           task.dispose();
/*     */         }
/*     */       
/* 241 */       } catch (TaskExhaustedEscapeException e) {
/* 242 */         R taskRequest; this.discarding.setTrue(this::handleExhaustedState);
/* 243 */         throwExecutorDiscartingException((Throwable)taskRequest);
/*     */       }
/* 245 */       catch (TaskEscapeException e) {
/* 246 */         this.discarding.setTrue();
/* 247 */         throwExecutorDiscartingException((Throwable)e);
/*     */       }
/* 249 */       catch (TaskDiscardException e) {
/* 250 */         throwExecutorDiscartingException((Throwable)e);
/*     */       }
/* 252 */       catch (Exception e) {
/* 253 */         progress.abort(e);
/*     */       } finally {
/*     */         
/* 256 */         endExecution(progress);
/*     */       }
/*     */     
/* 259 */     } catch (TaskExecutorException e) {
/* 260 */       throw e;
/*     */     }
/* 262 */     catch (Throwable e) {
/* 263 */       throw new TaskExecutorException("Exceção inesperada na execução da tarefa", e);
/*     */     } finally {
/*     */       
/* 266 */       this.runningTasks.decrementAndGet();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/taskresolver4j/imp/TaskRequestExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */