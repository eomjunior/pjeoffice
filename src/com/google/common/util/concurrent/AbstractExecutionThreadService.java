/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.time.Duration;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.logging.Level;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtIncompatible
/*     */ @J2ktIncompatible
/*     */ public abstract class AbstractExecutionThreadService
/*     */   implements Service
/*     */ {
/*  40 */   private static final LazyLogger logger = new LazyLogger(AbstractExecutionThreadService.class);
/*     */ 
/*     */   
/*  43 */   private final Service delegate = new AbstractService()
/*     */     {
/*     */       protected final void doStart()
/*     */       {
/*  47 */         Executor executor = MoreExecutors.renamingDecorator(AbstractExecutionThreadService.this.executor(), () -> AbstractExecutionThreadService.this.serviceName());
/*  48 */         executor.execute(() -> {
/*     */               try {
/*     */                 AbstractExecutionThreadService.this.startUp();
/*     */ 
/*     */                 
/*     */                 notifyStarted();
/*     */                 
/*     */                 if (isRunning()) {
/*     */                   try {
/*     */                     AbstractExecutionThreadService.this.run();
/*  58 */                   } catch (Throwable t) {
/*     */                     Platform.restoreInterruptIfIsInterruptedException(t);
/*     */                     try {
/*     */                       AbstractExecutionThreadService.this.shutDown();
/*  62 */                     } catch (Exception ignored) {
/*     */                       Platform.restoreInterruptIfIsInterruptedException(ignored);
/*     */ 
/*     */                       
/*     */                       AbstractExecutionThreadService.logger.get().log(Level.WARNING, "Error while attempting to shut down the service after failure.", ignored);
/*     */                     } 
/*     */ 
/*     */                     
/*     */                     notifyFailed(t);
/*     */ 
/*     */                     
/*     */                     return;
/*     */                   } 
/*     */                 }
/*     */ 
/*     */                 
/*     */                 AbstractExecutionThreadService.this.shutDown();
/*     */                 
/*     */                 notifyStopped();
/*  81 */               } catch (Throwable t) {
/*     */                 Platform.restoreInterruptIfIsInterruptedException(t);
/*     */                 notifyFailed(t);
/*     */               } 
/*     */             });
/*     */       }
/*     */ 
/*     */       
/*     */       protected void doStop() {
/*  90 */         AbstractExecutionThreadService.this.triggerShutdown();
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/*  95 */         return AbstractExecutionThreadService.this.toString();
/*     */       }
/*     */     };
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
/*     */   protected void startUp() throws Exception {}
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
/*     */   protected void shutDown() throws Exception {}
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
/*     */   protected void triggerShutdown() {}
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
/*     */   protected Executor executor() {
/* 158 */     return command -> MoreExecutors.newThread(serviceName(), command).start();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 163 */     return serviceName() + " [" + state() + "]";
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isRunning() {
/* 168 */     return this.delegate.isRunning();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Service.State state() {
/* 173 */     return this.delegate.state();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void addListener(Service.Listener listener, Executor executor) {
/* 179 */     this.delegate.addListener(listener, executor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Throwable failureCause() {
/* 185 */     return this.delegate.failureCause();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final Service startAsync() {
/* 192 */     this.delegate.startAsync();
/* 193 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final Service stopAsync() {
/* 200 */     this.delegate.stopAsync();
/* 201 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void awaitRunning() {
/* 207 */     this.delegate.awaitRunning();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void awaitRunning(Duration timeout) throws TimeoutException {
/* 213 */     super.awaitRunning(timeout);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void awaitRunning(long timeout, TimeUnit unit) throws TimeoutException {
/* 219 */     this.delegate.awaitRunning(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void awaitTerminated() {
/* 225 */     this.delegate.awaitTerminated();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void awaitTerminated(Duration timeout) throws TimeoutException {
/* 231 */     super.awaitTerminated(timeout);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void awaitTerminated(long timeout, TimeUnit unit) throws TimeoutException {
/* 237 */     this.delegate.awaitTerminated(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String serviceName() {
/* 249 */     return getClass().getSimpleName();
/*     */   }
/*     */   
/*     */   protected abstract void run() throws Exception;
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/AbstractExecutionThreadService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */