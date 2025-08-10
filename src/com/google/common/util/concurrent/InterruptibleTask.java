/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.j2objc.annotations.ReflectionSupport;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.concurrent.locks.AbstractOwnableSynchronizer;
/*     */ import java.util.concurrent.locks.LockSupport;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(emulated = true)
/*     */ @ReflectionSupport(ReflectionSupport.Level.FULL)
/*     */ abstract class InterruptibleTask<T>
/*     */   extends AtomicReference<Runnable>
/*     */   implements Runnable
/*     */ {
/*     */   static {
/*  43 */     Class<LockSupport> clazz = LockSupport.class;
/*     */   }
/*     */   
/*     */   private static final class DoNothingRunnable implements Runnable {
/*     */     private DoNothingRunnable() {}
/*     */     
/*     */     public void run() {}
/*     */   }
/*     */   
/*  52 */   private static final Runnable DONE = new DoNothingRunnable();
/*  53 */   private static final Runnable PARKED = new DoNothingRunnable();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MAX_BUSY_WAIT_SPINS = 1000;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void run() {
/*  66 */     Thread currentThread = Thread.currentThread();
/*  67 */     if (!compareAndSet(null, currentThread)) {
/*     */       return;
/*     */     }
/*     */     
/*  71 */     boolean run = !isDone();
/*  72 */     T result = null;
/*  73 */     Throwable error = null;
/*     */     try {
/*  75 */       if (run) {
/*  76 */         result = runInterruptibly();
/*     */       }
/*  78 */     } catch (Throwable t) {
/*  79 */       Platform.restoreInterruptIfIsInterruptedException(t);
/*  80 */       error = t;
/*     */     } finally {
/*     */       
/*  83 */       if (!compareAndSet(currentThread, DONE)) {
/*  84 */         waitForInterrupt(currentThread);
/*     */       }
/*  86 */       if (run) {
/*  87 */         if (error == null) {
/*     */           
/*  89 */           afterRanInterruptiblySuccess(NullnessCasts.uncheckedCastNullableTToT(result));
/*     */         } else {
/*  91 */           afterRanInterruptiblyFailure(error);
/*     */         } 
/*     */       }
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
/*     */   private void waitForInterrupt(Thread currentThread) {
/* 107 */     boolean restoreInterruptedBit = false;
/* 108 */     int spinCount = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 118 */     Runnable state = get();
/* 119 */     Blocker blocker = null;
/* 120 */     while (state instanceof Blocker || state == PARKED) {
/* 121 */       if (state instanceof Blocker) {
/* 122 */         blocker = (Blocker)state;
/*     */       }
/* 124 */       spinCount++;
/* 125 */       if (spinCount > 1000) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 133 */         if (state == PARKED || compareAndSet(state, PARKED)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 145 */           restoreInterruptedBit = (Thread.interrupted() || restoreInterruptedBit);
/* 146 */           LockSupport.park(blocker);
/*     */         } 
/*     */       } else {
/* 149 */         Thread.yield();
/*     */       } 
/* 151 */       state = get();
/*     */     } 
/* 153 */     if (restoreInterruptedBit) {
/* 154 */       currentThread.interrupt();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void interruptTask() {
/* 196 */     Runnable currentRunner = get();
/* 197 */     if (currentRunner instanceof Thread) {
/* 198 */       Blocker blocker = new Blocker(this);
/* 199 */       blocker.setOwner(Thread.currentThread());
/* 200 */       if (compareAndSet(currentRunner, blocker)) {
/*     */         
/*     */         try {
/*     */ 
/*     */ 
/*     */           
/* 206 */           ((Thread)currentRunner).interrupt();
/*     */         } finally {
/* 208 */           Runnable prev = getAndSet(DONE);
/* 209 */           if (prev == PARKED) {
/* 210 */             LockSupport.unpark((Thread)currentRunner);
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static final class Blocker
/*     */     extends AbstractOwnableSynchronizer
/*     */     implements Runnable
/*     */   {
/*     */     private final InterruptibleTask<?> task;
/*     */ 
/*     */     
/*     */     private Blocker(InterruptibleTask<?> task) {
/* 227 */       this.task = task;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {}
/*     */     
/*     */     private void setOwner(Thread thread) {
/* 234 */       setExclusiveOwnerThread(thread);
/*     */     }
/*     */     
/*     */     @CheckForNull
/*     */     @VisibleForTesting
/*     */     Thread getOwner() {
/* 240 */       return getExclusiveOwnerThread();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 245 */       return this.task.toString();
/*     */     }
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     String result;
/* 251 */     Runnable state = get();
/*     */     
/* 253 */     if (state == DONE) {
/* 254 */       result = "running=[DONE]";
/* 255 */     } else if (state instanceof Blocker) {
/* 256 */       result = "running=[INTERRUPTED]";
/* 257 */     } else if (state instanceof Thread) {
/*     */       
/* 259 */       result = "running=[RUNNING ON " + ((Thread)state).getName() + "]";
/*     */     } else {
/* 261 */       result = "running=[NOT STARTED YET]";
/*     */     } 
/* 263 */     return result + ", " + toPendingString();
/*     */   }
/*     */   
/*     */   abstract boolean isDone();
/*     */   
/*     */   @ParametricNullness
/*     */   abstract T runInterruptibly() throws Exception;
/*     */   
/*     */   abstract void afterRanInterruptiblySuccess(@ParametricNullness T paramT);
/*     */   
/*     */   abstract void afterRanInterruptiblyFailure(Throwable paramThrowable);
/*     */   
/*     */   abstract String toPendingString();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/InterruptibleTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */