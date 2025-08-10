/*     */ package io.reactivex.internal.schedulers;
/*     */ 
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.annotations.NonNull;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.disposables.Disposables;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ImmediateThinScheduler
/*     */   extends Scheduler
/*     */ {
/*  34 */   public static final Scheduler INSTANCE = new ImmediateThinScheduler();
/*     */   
/*  36 */   static final Scheduler.Worker WORKER = new ImmediateThinWorker();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  41 */   static final Disposable DISPOSED = Disposables.empty(); static {
/*  42 */     DISPOSED.dispose();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public Disposable scheduleDirect(@NonNull Runnable run) {
/*  52 */     run.run();
/*  53 */     return DISPOSED;
/*     */   }
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public Disposable scheduleDirect(@NonNull Runnable run, long delay, TimeUnit unit) {
/*  59 */     throw new UnsupportedOperationException("This scheduler doesn't support delayed execution");
/*     */   }
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public Disposable schedulePeriodicallyDirect(@NonNull Runnable run, long initialDelay, long period, TimeUnit unit) {
/*  65 */     throw new UnsupportedOperationException("This scheduler doesn't support periodic execution");
/*     */   }
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public Scheduler.Worker createWorker() {
/*  71 */     return WORKER;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class ImmediateThinWorker
/*     */     extends Scheduler.Worker
/*     */   {
/*     */     public void dispose() {}
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  83 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     @NonNull
/*     */     public Disposable schedule(@NonNull Runnable run) {
/*  89 */       run.run();
/*  90 */       return ImmediateThinScheduler.DISPOSED;
/*     */     }
/*     */ 
/*     */     
/*     */     @NonNull
/*     */     public Disposable schedule(@NonNull Runnable run, long delay, @NonNull TimeUnit unit) {
/*  96 */       throw new UnsupportedOperationException("This scheduler doesn't support delayed execution");
/*     */     }
/*     */ 
/*     */     
/*     */     @NonNull
/*     */     public Disposable schedulePeriodically(@NonNull Runnable run, long initialDelay, long period, TimeUnit unit) {
/* 102 */       throw new UnsupportedOperationException("This scheduler doesn't support periodic execution");
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/schedulers/ImmediateThinScheduler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */