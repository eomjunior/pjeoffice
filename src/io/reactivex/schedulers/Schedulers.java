/*     */ package io.reactivex.schedulers;
/*     */ 
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.annotations.Experimental;
/*     */ import io.reactivex.annotations.NonNull;
/*     */ import io.reactivex.internal.schedulers.ComputationScheduler;
/*     */ import io.reactivex.internal.schedulers.ExecutorScheduler;
/*     */ import io.reactivex.internal.schedulers.IoScheduler;
/*     */ import io.reactivex.internal.schedulers.NewThreadScheduler;
/*     */ import io.reactivex.internal.schedulers.SchedulerPoolFactory;
/*     */ import io.reactivex.internal.schedulers.SingleScheduler;
/*     */ import io.reactivex.internal.schedulers.TrampolineScheduler;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Executor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Schedulers
/*     */ {
/*     */   static final class SingleHolder
/*     */   {
/*  59 */     static final Scheduler DEFAULT = (Scheduler)new SingleScheduler();
/*     */   }
/*     */   
/*     */   static final class ComputationHolder {
/*  63 */     static final Scheduler DEFAULT = (Scheduler)new ComputationScheduler();
/*     */   }
/*     */   
/*     */   static final class IoHolder {
/*  67 */     static final Scheduler DEFAULT = (Scheduler)new IoScheduler();
/*     */   }
/*     */   
/*     */   static final class NewThreadHolder {
/*  71 */     static final Scheduler DEFAULT = (Scheduler)new NewThreadScheduler();
/*     */   }
/*     */   
/*     */   @NonNull
/*  75 */   static final Scheduler SINGLE = RxJavaPlugins.initSingleScheduler(new SingleTask());
/*     */   @NonNull
/*  77 */   static final Scheduler COMPUTATION = RxJavaPlugins.initComputationScheduler(new ComputationTask());
/*     */   @NonNull
/*  79 */   static final Scheduler IO = RxJavaPlugins.initIoScheduler(new IOTask());
/*     */   @NonNull
/*  81 */   static final Scheduler TRAMPOLINE = (Scheduler)TrampolineScheduler.instance();
/*     */   @NonNull
/*  83 */   static final Scheduler NEW_THREAD = RxJavaPlugins.initNewThreadScheduler(new NewThreadTask());
/*     */ 
/*     */ 
/*     */   
/*     */   private Schedulers() {
/*  88 */     throw new IllegalStateException("No instances!");
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
/*     */   @NonNull
/*     */   public static Scheduler computation() {
/* 136 */     return RxJavaPlugins.onComputationScheduler(COMPUTATION);
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
/*     */   @NonNull
/*     */   public static Scheduler io() {
/* 181 */     return RxJavaPlugins.onIoScheduler(IO);
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
/*     */   @NonNull
/*     */   public static Scheduler trampoline() {
/* 200 */     return TRAMPOLINE;
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
/*     */   @NonNull
/*     */   public static Scheduler newThread() {
/* 239 */     return RxJavaPlugins.onNewThreadScheduler(NEW_THREAD);
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
/*     */   @NonNull
/*     */   public static Scheduler single() {
/* 289 */     return RxJavaPlugins.onSingleScheduler(SINGLE);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public static Scheduler from(@NonNull Executor executor) {
/* 346 */     return (Scheduler)new ExecutorScheduler(executor, false);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   @Experimental
/*     */   public static Scheduler from(@NonNull Executor executor, boolean interruptibleWorker) {
/* 406 */     return (Scheduler)new ExecutorScheduler(executor, interruptibleWorker);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void shutdown() {
/* 414 */     computation().shutdown();
/* 415 */     io().shutdown();
/* 416 */     newThread().shutdown();
/* 417 */     single().shutdown();
/* 418 */     trampoline().shutdown();
/* 419 */     SchedulerPoolFactory.shutdown();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void start() {
/* 427 */     computation().start();
/* 428 */     io().start();
/* 429 */     newThread().start();
/* 430 */     single().start();
/* 431 */     trampoline().start();
/* 432 */     SchedulerPoolFactory.start();
/*     */   }
/*     */   
/*     */   static final class IOTask
/*     */     implements Callable<Scheduler> {
/*     */     public Scheduler call() throws Exception {
/* 438 */       return Schedulers.IoHolder.DEFAULT;
/*     */     }
/*     */   }
/*     */   
/*     */   static final class NewThreadTask
/*     */     implements Callable<Scheduler> {
/*     */     public Scheduler call() throws Exception {
/* 445 */       return Schedulers.NewThreadHolder.DEFAULT;
/*     */     }
/*     */   }
/*     */   
/*     */   static final class SingleTask
/*     */     implements Callable<Scheduler> {
/*     */     public Scheduler call() throws Exception {
/* 452 */       return Schedulers.SingleHolder.DEFAULT;
/*     */     }
/*     */   }
/*     */   
/*     */   static final class ComputationTask
/*     */     implements Callable<Scheduler> {
/*     */     public Scheduler call() throws Exception {
/* 459 */       return Schedulers.ComputationHolder.DEFAULT;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/schedulers/Schedulers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */