/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.concurrent.GuardedBy;
/*     */ import com.google.j2objc.annotations.RetainedWith;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.logging.Level;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ final class SequentialExecutor
/*     */   implements Executor
/*     */ {
/*  54 */   private static final LazyLogger log = new LazyLogger(SequentialExecutor.class);
/*     */   private final Executor executor;
/*     */   
/*     */   enum WorkerRunningState {
/*  58 */     IDLE,
/*     */     
/*  60 */     QUEUING,
/*     */     
/*  62 */     QUEUED,
/*  63 */     RUNNING;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("queue")
/*  69 */   private final Deque<Runnable> queue = new ArrayDeque<>();
/*     */ 
/*     */   
/*     */   @GuardedBy("queue")
/*  73 */   private WorkerRunningState workerRunningState = WorkerRunningState.IDLE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("queue")
/*  83 */   private long workerRunCount = 0L;
/*     */   
/*     */   @RetainedWith
/*  86 */   private final QueueWorker worker = new QueueWorker();
/*     */ 
/*     */   
/*     */   SequentialExecutor(Executor executor) {
/*  90 */     this.executor = (Executor)Preconditions.checkNotNull(executor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(final Runnable task) {
/*     */     Runnable submittedTask;
/*     */     long oldRunCount;
/* 101 */     Preconditions.checkNotNull(task);
/*     */ 
/*     */     
/* 104 */     synchronized (this.queue) {
/*     */ 
/*     */       
/* 107 */       if (this.workerRunningState == WorkerRunningState.RUNNING || this.workerRunningState == WorkerRunningState.QUEUED) {
/* 108 */         this.queue.add(task);
/*     */         
/*     */         return;
/*     */       } 
/* 112 */       oldRunCount = this.workerRunCount;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 120 */       submittedTask = new Runnable(this)
/*     */         {
/*     */           public void run()
/*     */           {
/* 124 */             task.run();
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/* 129 */             return task.toString();
/*     */           }
/*     */         };
/* 132 */       this.queue.add(submittedTask);
/* 133 */       this.workerRunningState = WorkerRunningState.QUEUING;
/*     */     } 
/*     */     
/*     */     try {
/* 137 */       this.executor.execute(this.worker);
/* 138 */     } catch (Throwable t) {
/*     */       
/* 140 */       synchronized (this.queue) {
/*     */ 
/*     */         
/* 143 */         boolean removed = ((this.workerRunningState == WorkerRunningState.IDLE || this.workerRunningState == WorkerRunningState.QUEUING) && this.queue.removeLastOccurrence(submittedTask));
/*     */ 
/*     */         
/* 146 */         if (!(t instanceof java.util.concurrent.RejectedExecutionException) || removed) {
/* 147 */           throw t;
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 164 */     boolean alreadyMarkedQueued = (this.workerRunningState != WorkerRunningState.QUEUING);
/* 165 */     if (alreadyMarkedQueued) {
/*     */       return;
/*     */     }
/* 168 */     synchronized (this.queue) {
/* 169 */       if (this.workerRunCount == oldRunCount && this.workerRunningState == WorkerRunningState.QUEUING)
/* 170 */         this.workerRunningState = WorkerRunningState.QUEUED; 
/*     */     } 
/*     */   }
/*     */   
/*     */   private final class QueueWorker implements Runnable {
/*     */     @CheckForNull
/*     */     Runnable task;
/*     */     
/*     */     private QueueWorker() {}
/*     */     
/*     */     public void run() {
/*     */       try {
/* 182 */         workOnQueue();
/* 183 */       } catch (Error e) {
/* 184 */         synchronized (SequentialExecutor.this.queue) {
/* 185 */           SequentialExecutor.this.workerRunningState = SequentialExecutor.WorkerRunningState.IDLE;
/*     */         } 
/* 187 */         throw e;
/*     */       } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void workOnQueue() {
/* 207 */       boolean interruptedDuringTask = false;
/* 208 */       boolean hasSetRunning = false;
/*     */       try {
/*     */         while (true) {
/* 211 */           synchronized (SequentialExecutor.this.queue) {
/*     */ 
/*     */             
/* 214 */             if (!hasSetRunning) {
/* 215 */               if (SequentialExecutor.this.workerRunningState == SequentialExecutor.WorkerRunningState.RUNNING) {
/*     */                 return;
/*     */               }
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 222 */               SequentialExecutor.this.workerRunCount++;
/* 223 */               SequentialExecutor.this.workerRunningState = SequentialExecutor.WorkerRunningState.RUNNING;
/* 224 */               hasSetRunning = true;
/*     */             } 
/*     */             
/* 227 */             this.task = SequentialExecutor.this.queue.poll();
/* 228 */             if (this.task == null) {
/* 229 */               SequentialExecutor.this.workerRunningState = SequentialExecutor.WorkerRunningState.IDLE;
/*     */ 
/*     */               
/*     */               return;
/*     */             } 
/*     */           } 
/*     */           
/* 236 */           interruptedDuringTask |= Thread.interrupted();
/*     */           try {
/* 238 */             this.task.run();
/* 239 */           } catch (Exception e) {
/* 240 */             SequentialExecutor.log.get().log(Level.SEVERE, "Exception while executing runnable " + this.task, e);
/*     */           } finally {
/* 242 */             this.task = null;
/*     */           }
/*     */         
/*     */         }
/*     */       
/*     */       } finally {
/*     */         
/* 249 */         if (interruptedDuringTask) {
/* 250 */           Thread.currentThread().interrupt();
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 258 */       Runnable currentlyRunning = this.task;
/* 259 */       if (currentlyRunning != null) {
/* 260 */         return "SequentialExecutorWorker{running=" + currentlyRunning + "}";
/*     */       }
/* 262 */       return "SequentialExecutorWorker{state=" + SequentialExecutor.this.workerRunningState + "}";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 268 */     return "SequentialExecutor@" + System.identityHashCode(this) + "{" + this.executor + "}";
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/SequentialExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */