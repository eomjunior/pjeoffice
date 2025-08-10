/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import javax.annotation.CheckForNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @J2ktIncompatible
/*     */ public final class ExecutionSequencer
/*     */ {
/*     */   public static ExecutionSequencer create() {
/*  96 */     return new ExecutionSequencer();
/*     */   }
/*     */ 
/*     */   
/* 100 */   private final AtomicReference<ListenableFuture<Void>> ref = new AtomicReference<>(
/* 101 */       Futures.immediateVoidFuture());
/*     */   
/* 103 */   private ThreadConfinedTaskQueue latestTaskQueue = new ThreadConfinedTaskQueue();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ThreadConfinedTaskQueue
/*     */   {
/*     */     @CheckForNull
/*     */     Thread thread;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     Runnable nextTask;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     Executor nextExecutor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private ThreadConfinedTaskQueue() {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<T> submit(final Callable<T> callable, Executor executor) {
/* 150 */     Preconditions.checkNotNull(callable);
/* 151 */     Preconditions.checkNotNull(executor);
/* 152 */     return submitAsync(new AsyncCallable<T>(this)
/*     */         {
/*     */           public ListenableFuture<T> call() throws Exception
/*     */           {
/* 156 */             return Futures.immediateFuture(callable.call());
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/* 161 */             return callable.toString();
/*     */           }
/*     */         }executor);
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
/*     */   public <T> ListenableFuture<T> submitAsync(final AsyncCallable<T> callable, Executor executor) {
/* 176 */     Preconditions.checkNotNull(callable);
/* 177 */     Preconditions.checkNotNull(executor);
/* 178 */     final TaskNonReentrantExecutor taskExecutor = new TaskNonReentrantExecutor(executor, this);
/* 179 */     AsyncCallable<T> task = new AsyncCallable<T>(this)
/*     */       {
/*     */         public ListenableFuture<T> call() throws Exception
/*     */         {
/* 183 */           if (!taskExecutor.trySetStarted()) {
/* 184 */             return Futures.immediateCancelledFuture();
/*     */           }
/* 186 */           return callable.call();
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 191 */           return callable.toString();
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 205 */     SettableFuture<Void> newFuture = SettableFuture.create();
/*     */     
/* 207 */     ListenableFuture<Void> oldFuture = this.ref.getAndSet(newFuture);
/*     */ 
/*     */     
/* 210 */     TrustedListenableFutureTask<T> taskFuture = TrustedListenableFutureTask.create(task);
/* 211 */     oldFuture.addListener(taskFuture, taskExecutor);
/*     */     
/* 213 */     ListenableFuture<T> outputFuture = Futures.nonCancellationPropagating(taskFuture);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 219 */     Runnable listener = () -> {
/*     */         if (taskFuture.isDone()) {
/*     */           newFuture.setFuture(oldFuture);
/*     */         } else if (outputFuture.isCancelled() && taskExecutor.trySetCancelled()) {
/*     */           taskFuture.cancel(false);
/*     */         } 
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 259 */     outputFuture.addListener(listener, MoreExecutors.directExecutor());
/* 260 */     taskFuture.addListener(listener, MoreExecutors.directExecutor());
/*     */     
/* 262 */     return outputFuture;
/*     */   }
/*     */   
/*     */   enum RunningState {
/* 266 */     NOT_RUN,
/* 267 */     CANCELLED,
/* 268 */     STARTED;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class TaskNonReentrantExecutor
/*     */     extends AtomicReference<RunningState>
/*     */     implements Executor, Runnable
/*     */   {
/*     */     @CheckForNull
/*     */     ExecutionSequencer sequencer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     Executor delegate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     Runnable task;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     Thread submitting;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private TaskNonReentrantExecutor(Executor delegate, ExecutionSequencer sequencer) {
/* 314 */       super(ExecutionSequencer.RunningState.NOT_RUN);
/* 315 */       this.delegate = delegate;
/* 316 */       this.sequencer = sequencer;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void execute(Runnable task) {
/* 330 */       if (get() == ExecutionSequencer.RunningState.CANCELLED) {
/* 331 */         this.delegate = null;
/* 332 */         this.sequencer = null;
/*     */         return;
/*     */       } 
/* 335 */       this.submitting = Thread.currentThread();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 347 */         ExecutionSequencer.ThreadConfinedTaskQueue submittingTaskQueue = (Objects.<ExecutionSequencer>requireNonNull(this.sequencer)).latestTaskQueue;
/* 348 */         if (submittingTaskQueue.thread == this.submitting) {
/* 349 */           this.sequencer = null;
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 354 */           Preconditions.checkState((submittingTaskQueue.nextTask == null));
/* 355 */           submittingTaskQueue.nextTask = task;
/*     */           
/* 357 */           submittingTaskQueue.nextExecutor = Objects.<Executor>requireNonNull(this.delegate);
/* 358 */           this.delegate = null;
/*     */         } else {
/*     */           
/* 361 */           Executor localDelegate = Objects.<Executor>requireNonNull(this.delegate);
/* 362 */           this.delegate = null;
/* 363 */           this.task = task;
/* 364 */           localDelegate.execute(this);
/*     */         
/*     */         }
/*     */       
/*     */       }
/*     */       finally {
/*     */         
/* 371 */         this.submitting = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/* 378 */       Thread currentThread = Thread.currentThread();
/* 379 */       if (currentThread != this.submitting) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 384 */         Runnable localTask = Objects.<Runnable>requireNonNull(this.task);
/* 385 */         this.task = null;
/* 386 */         localTask.run();
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 392 */       ExecutionSequencer.ThreadConfinedTaskQueue executingTaskQueue = new ExecutionSequencer.ThreadConfinedTaskQueue();
/* 393 */       executingTaskQueue.thread = currentThread;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 415 */       (Objects.<ExecutionSequencer>requireNonNull(this.sequencer)).latestTaskQueue = executingTaskQueue;
/* 416 */       this.sequencer = null;
/*     */       
/*     */       try {
/* 419 */         Runnable localTask = Objects.<Runnable>requireNonNull(this.task);
/* 420 */         this.task = null;
/* 421 */         localTask.run();
/*     */         
/*     */         Runnable queuedTask;
/*     */         
/*     */         Executor queuedExecutor;
/* 426 */         while ((queuedTask = executingTaskQueue.nextTask) != null && (queuedExecutor = executingTaskQueue.nextExecutor) != null)
/*     */         {
/* 428 */           executingTaskQueue.nextTask = null;
/* 429 */           executingTaskQueue.nextExecutor = null;
/* 430 */           queuedExecutor.execute(queuedTask);
/*     */         
/*     */         }
/*     */ 
/*     */       
/*     */       }
/*     */       finally {
/*     */ 
/*     */         
/* 439 */         executingTaskQueue.thread = null;
/*     */       } 
/*     */     }
/*     */     
/*     */     private boolean trySetStarted() {
/* 444 */       return compareAndSet(ExecutionSequencer.RunningState.NOT_RUN, ExecutionSequencer.RunningState.STARTED);
/*     */     }
/*     */     
/*     */     private boolean trySetCancelled() {
/* 448 */       return compareAndSet(ExecutionSequencer.RunningState.NOT_RUN, ExecutionSequencer.RunningState.CANCELLED);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/ExecutionSequencer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */