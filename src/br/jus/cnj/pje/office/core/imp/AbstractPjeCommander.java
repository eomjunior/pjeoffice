/*     */ package br.jus.cnj.pje.office.core.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.IBootable;
/*     */ import br.jus.cnj.pje.office.core.IPjeCommander;
/*     */ import br.jus.cnj.pje.office.core.IPjeLifeCycle;
/*     */ import br.jus.cnj.pje.office.core.IPjeRequest;
/*     */ import br.jus.cnj.pje.office.core.IPjeResponse;
/*     */ import com.github.taskresolver4j.ITaskRequestExecutor;
/*     */ import com.github.taskresolver4j.ITaskResponse;
/*     */ import com.github.taskresolver4j.exception.TaskExecutorDiscartingException;
/*     */ import com.github.taskresolver4j.exception.TaskExecutorFinishingException;
/*     */ import com.github.utils4j.gui.imp.ExceptionAlert;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.subjects.BehaviorSubject;
/*     */ import java.io.IOException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractPjeCommander<I extends IPjeRequest, O extends IPjeResponse>
/*     */   implements IPjeCommander<I, O>
/*     */ {
/*  56 */   protected static final Logger LOGGER = LoggerFactory.getLogger(IPjeCommander.class);
/*     */   
/*     */   protected final IBootable boot;
/*     */   
/*     */   private final String serverEndpoint;
/*     */   
/*     */   private final ITaskRequestExecutor<IPjeRequest, IPjeResponse> executor;
/*     */   
/*  64 */   private final BehaviorSubject<IPjeLifeCycle.LifeCycle> startup = BehaviorSubject.create();
/*     */   
/*     */   private boolean started = false;
/*     */   
/*     */   protected AbstractPjeCommander(ITaskRequestExecutor<IPjeRequest, IPjeResponse> executor, IBootable boot, String serverEndpoint) {
/*  69 */     this.executor = (ITaskRequestExecutor<IPjeRequest, IPjeResponse>)Args.requireNonNull(executor, "executor is null");
/*  70 */     this.boot = (IBootable)Args.requireNonNull(boot, "boot is null");
/*  71 */     this.serverEndpoint = Args.requireText(serverEndpoint, "serverEndpoint is empty");
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getServerEndpoint() {
/*  76 */     return this.serverEndpoint;
/*     */   }
/*     */   
/*     */   protected final ITaskRequestExecutor<IPjeRequest, IPjeResponse> getExecutor() {
/*  80 */     return this.executor;
/*     */   }
/*     */   
/*     */   protected final String getServerEndpoint(String path) {
/*  84 */     return this.serverEndpoint + Strings.trim(path);
/*     */   }
/*     */   
/*     */   private final void notifyShutdown() {
/*  88 */     this.startup.onNext(IPjeLifeCycle.LifeCycle.SHUTDOWN);
/*     */   }
/*     */   
/*     */   private final void notifyStartup() {
/*  92 */     this.startup.onNext(IPjeLifeCycle.LifeCycle.STARTUP);
/*     */   }
/*     */   
/*     */   private final void notifyKill() {
/*  96 */     this.startup.onNext(IPjeLifeCycle.LifeCycle.KILL);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isStarted() {
/* 101 */     return this.started;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Observable<IPjeLifeCycle.LifeCycle> lifeCycle() {
/* 106 */     return (Observable<IPjeLifeCycle.LifeCycle>)this.startup;
/*     */   }
/*     */   
/*     */   public final void exit() {
/* 110 */     this.boot.exit(1500L);
/*     */   }
/*     */   
/*     */   public final void logout() {
/* 114 */     this.boot.logout();
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute(I request, O response) {
/*     */     try {
/* 120 */       this.executor.execute(request, response);
/* 121 */     } catch (TaskExecutorFinishingException e) {
/* 122 */       LOGGER.warn("Requisição ignorada", (Throwable)e);
/* 123 */     } catch (TaskExecutorDiscartingException e) {
/* 124 */       Throwables.quietly(() -> getDefaultDiscardResponse((I)request, (O)response, e).processResponse(response));
/* 125 */       LOGGER.warn("Descartes de requisições de lote cancelado", (Throwable)e);
/* 126 */     } catch (Throwable e) {
/* 127 */       handleException(request, response, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void async(Runnable runnable) {
/* 134 */     this.executor.async(runnable);
/*     */   }
/*     */   
/*     */   protected void handleException(I request, O response, Throwable e) {
/* 138 */     String errorMessage = "Exceção no ciclo de vida da requisição";
/* 139 */     LOGGER.error(errorMessage, e);
/* 140 */     ExceptionAlert.show(errorMessage, e);
/*     */   }
/*     */ 
/*     */   
/*     */   public final synchronized void start() throws IOException {
/* 145 */     if (!isStarted()) {
/* 146 */       LOGGER.info("Iniciando " + getClass().getSimpleName());
/* 147 */       this.executor.notifyOpening();
/* 148 */       doStart();
/* 149 */       this.started = true;
/* 150 */       Throwables.quietly(this::notifyStartup);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void stop() throws IOException {
/* 156 */     stop(false);
/*     */   }
/*     */   
/*     */   protected void doStart() throws IOException {}
/*     */   
/*     */   public final synchronized void stop(boolean kill) throws IOException {
/* 162 */     if (isStarted()) {
/* 163 */       LOGGER.info("Parando " + getClass().getSimpleName());
/* 164 */       Throwables.quietly(this.executor::notifyClosing);
/* 165 */       doStop(kill);
/* 166 */       this.started = false;
/* 167 */       Throwables.quietly(this.executor::close);
/* 168 */       Throwables.quietly(this::notifyShutdown);
/* 169 */       if (kill) {
/* 170 */         Throwables.quietly(this::notifyKill);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doStop(boolean kill) throws IOException {}
/*     */   
/*     */   public final void execute(String uri) {
/* 179 */     if (!Strings.hasText(uri = Strings.trim(uri)))
/*     */       return; 
/* 181 */     String request = uri;
/* 182 */     async(() -> openRequest(request));
/*     */   }
/*     */   
/*     */   protected abstract ITaskResponse<O> getDefaultDiscardResponse(I paramI, O paramO, TaskExecutorDiscartingException paramTaskExecutorDiscartingException);
/*     */   
/*     */   protected abstract void openRequest(String paramString);
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/AbstractPjeCommander.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */