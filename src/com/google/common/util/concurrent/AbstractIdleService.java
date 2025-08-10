/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.time.Duration;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtIncompatible
/*     */ @J2ktIncompatible
/*     */ public abstract class AbstractIdleService
/*     */   implements Service
/*     */ {
/*  43 */   private final Supplier<String> threadNameSupplier = new ThreadNameSupplier();
/*     */   
/*     */   private final class ThreadNameSupplier implements Supplier<String> {
/*     */     private ThreadNameSupplier() {}
/*     */     
/*     */     public String get() {
/*  49 */       return AbstractIdleService.this.serviceName() + " " + AbstractIdleService.this.state();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  54 */   private final Service delegate = new DelegateService();
/*     */   
/*     */   private final class DelegateService extends AbstractService {
/*     */     private DelegateService() {}
/*     */     
/*     */     protected final void doStart() {
/*  60 */       MoreExecutors.renamingDecorator(AbstractIdleService.this.executor(), AbstractIdleService.this.threadNameSupplier)
/*  61 */         .execute(() -> {
/*     */             try {
/*     */               AbstractIdleService.this.startUp();
/*     */               
/*     */               notifyStarted();
/*  66 */             } catch (Throwable t) {
/*     */               Platform.restoreInterruptIfIsInterruptedException(t);
/*     */               notifyFailed(t);
/*     */             } 
/*     */           });
/*     */     }
/*     */ 
/*     */     
/*     */     protected final void doStop() {
/*  75 */       MoreExecutors.renamingDecorator(AbstractIdleService.this.executor(), AbstractIdleService.this.threadNameSupplier)
/*  76 */         .execute(() -> {
/*     */             try {
/*     */               AbstractIdleService.this.shutDown();
/*     */               
/*     */               notifyStopped();
/*  81 */             } catch (Throwable t) {
/*     */               Platform.restoreInterruptIfIsInterruptedException(t);
/*     */               notifyFailed(t);
/*     */             } 
/*     */           });
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  90 */       return AbstractIdleService.this.toString();
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Executor executor() {
/* 111 */     return command -> MoreExecutors.newThread((String)this.threadNameSupplier.get(), command).start();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 116 */     return serviceName() + " [" + state() + "]";
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isRunning() {
/* 121 */     return this.delegate.isRunning();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Service.State state() {
/* 126 */     return this.delegate.state();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void addListener(Service.Listener listener, Executor executor) {
/* 132 */     this.delegate.addListener(listener, executor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Throwable failureCause() {
/* 138 */     return this.delegate.failureCause();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final Service startAsync() {
/* 145 */     this.delegate.startAsync();
/* 146 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final Service stopAsync() {
/* 153 */     this.delegate.stopAsync();
/* 154 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void awaitRunning() {
/* 160 */     this.delegate.awaitRunning();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void awaitRunning(Duration timeout) throws TimeoutException {
/* 166 */     super.awaitRunning(timeout);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void awaitRunning(long timeout, TimeUnit unit) throws TimeoutException {
/* 172 */     this.delegate.awaitRunning(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void awaitTerminated() {
/* 178 */     this.delegate.awaitTerminated();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void awaitTerminated(Duration timeout) throws TimeoutException {
/* 184 */     super.awaitTerminated(timeout);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void awaitTerminated(long timeout, TimeUnit unit) throws TimeoutException {
/* 190 */     this.delegate.awaitTerminated(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String serviceName() {
/* 200 */     return getClass().getSimpleName();
/*     */   }
/*     */   
/*     */   protected abstract void startUp() throws Exception;
/*     */   
/*     */   protected abstract void shutDown() throws Exception;
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/AbstractIdleService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */