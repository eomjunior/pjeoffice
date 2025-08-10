/*      */ package com.google.common.util.concurrent;
/*      */ 
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.J2ktIncompatible;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.primitives.Longs;
/*      */ import com.google.errorprone.annotations.concurrent.GuardedBy;
/*      */ import com.google.j2objc.annotations.Weak;
/*      */ import java.time.Duration;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.locks.Condition;
/*      */ import java.util.concurrent.locks.ReentrantLock;
/*      */ import java.util.function.BooleanSupplier;
/*      */ import javax.annotation.CheckForNull;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @ElementTypesAreNonnullByDefault
/*      */ @J2ktIncompatible
/*      */ @GwtIncompatible
/*      */ public final class Monitor
/*      */ {
/*      */   private final boolean fair;
/*      */   private final ReentrantLock lock;
/*      */   
/*      */   public static abstract class Guard
/*      */   {
/*      */     @Weak
/*      */     final Monitor monitor;
/*      */     final Condition condition;
/*      */     @GuardedBy("monitor.lock")
/*  312 */     int waiterCount = 0;
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     @GuardedBy("monitor.lock")
/*      */     Guard next;
/*      */ 
/*      */     
/*      */     protected Guard(Monitor monitor) {
/*  321 */       this.monitor = (Monitor)Preconditions.checkNotNull(monitor, "monitor");
/*  322 */       this.condition = monitor.lock.newCondition();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public abstract boolean isSatisfied();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   @GuardedBy("lock")
/*  343 */   private Guard activeGuards = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Monitor() {
/*  352 */     this(false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Monitor(boolean fair) {
/*  362 */     this.fair = fair;
/*  363 */     this.lock = new ReentrantLock(fair);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Guard newGuard(final BooleanSupplier isSatisfied) {
/*  374 */     Preconditions.checkNotNull(isSatisfied, "isSatisfied");
/*  375 */     return new Guard(this, this)
/*      */       {
/*      */         public boolean isSatisfied() {
/*  378 */           return isSatisfied.getAsBoolean();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   public void enter() {
/*  385 */     this.lock.lock();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean enter(Duration time) {
/*  395 */     return enter(Internal.toNanosSaturated(time), TimeUnit.NANOSECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean enter(long time, TimeUnit unit) {
/*  405 */     long timeoutNanos = toSafeNanos(time, unit);
/*  406 */     ReentrantLock lock = this.lock;
/*  407 */     if (!this.fair && lock.tryLock()) {
/*  408 */       return true;
/*      */     }
/*  410 */     boolean interrupted = Thread.interrupted();
/*      */     try {
/*  412 */       long startTime = System.nanoTime();
/*  413 */       long remainingNanos = timeoutNanos; while (true) {
/*      */         try {
/*  415 */           return lock.tryLock(remainingNanos, TimeUnit.NANOSECONDS);
/*  416 */         } catch (InterruptedException interrupt) {
/*  417 */           interrupted = true;
/*  418 */           remainingNanos = remainingNanos(startTime, timeoutNanos);
/*      */         } 
/*      */       } 
/*      */     } finally {
/*  422 */       if (interrupted) {
/*  423 */         Thread.currentThread().interrupt();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void enterInterruptibly() throws InterruptedException {
/*  434 */     this.lock.lockInterruptibly();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean enterInterruptibly(Duration time) throws InterruptedException {
/*  445 */     return enterInterruptibly(Internal.toNanosSaturated(time), TimeUnit.NANOSECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean enterInterruptibly(long time, TimeUnit unit) throws InterruptedException {
/*  456 */     return this.lock.tryLock(time, unit);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean tryEnter() {
/*  467 */     return this.lock.tryLock();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void enterWhen(Guard guard) throws InterruptedException {
/*  476 */     if (guard.monitor != this) {
/*  477 */       throw new IllegalMonitorStateException();
/*      */     }
/*  479 */     ReentrantLock lock = this.lock;
/*  480 */     boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
/*  481 */     lock.lockInterruptibly();
/*      */     
/*  483 */     boolean satisfied = false;
/*      */     try {
/*  485 */       if (!guard.isSatisfied()) {
/*  486 */         await(guard, signalBeforeWaiting);
/*      */       }
/*  488 */       satisfied = true;
/*      */     } finally {
/*  490 */       if (!satisfied) {
/*  491 */         leave();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean enterWhen(Guard guard, Duration time) throws InterruptedException {
/*  506 */     return enterWhen(guard, Internal.toNanosSaturated(time), TimeUnit.NANOSECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean enterWhen(Guard guard, long time, TimeUnit unit) throws InterruptedException {
/*      */     // Byte code:
/*      */     //   0: lload_2
/*      */     //   1: aload #4
/*      */     //   3: invokestatic toSafeNanos : (JLjava/util/concurrent/TimeUnit;)J
/*      */     //   6: lstore #5
/*      */     //   8: aload_1
/*      */     //   9: getfield monitor : Lcom/google/common/util/concurrent/Monitor;
/*      */     //   12: aload_0
/*      */     //   13: if_acmpeq -> 24
/*      */     //   16: new java/lang/IllegalMonitorStateException
/*      */     //   19: dup
/*      */     //   20: invokespecial <init> : ()V
/*      */     //   23: athrow
/*      */     //   24: aload_0
/*      */     //   25: getfield lock : Ljava/util/concurrent/locks/ReentrantLock;
/*      */     //   28: astore #7
/*      */     //   30: aload #7
/*      */     //   32: invokevirtual isHeldByCurrentThread : ()Z
/*      */     //   35: istore #8
/*      */     //   37: lconst_0
/*      */     //   38: lstore #9
/*      */     //   40: aload_0
/*      */     //   41: getfield fair : Z
/*      */     //   44: ifne -> 72
/*      */     //   47: invokestatic interrupted : ()Z
/*      */     //   50: ifeq -> 61
/*      */     //   53: new java/lang/InterruptedException
/*      */     //   56: dup
/*      */     //   57: invokespecial <init> : ()V
/*      */     //   60: athrow
/*      */     //   61: aload #7
/*      */     //   63: invokevirtual tryLock : ()Z
/*      */     //   66: ifeq -> 72
/*      */     //   69: goto -> 92
/*      */     //   72: lload #5
/*      */     //   74: invokestatic initNanoTime : (J)J
/*      */     //   77: lstore #9
/*      */     //   79: aload #7
/*      */     //   81: lload_2
/*      */     //   82: aload #4
/*      */     //   84: invokevirtual tryLock : (JLjava/util/concurrent/TimeUnit;)Z
/*      */     //   87: ifne -> 92
/*      */     //   90: iconst_0
/*      */     //   91: ireturn
/*      */     //   92: iconst_0
/*      */     //   93: istore #11
/*      */     //   95: iconst_1
/*      */     //   96: istore #12
/*      */     //   98: aload_1
/*      */     //   99: invokevirtual isSatisfied : ()Z
/*      */     //   102: ifne -> 134
/*      */     //   105: aload_0
/*      */     //   106: aload_1
/*      */     //   107: lload #9
/*      */     //   109: lconst_0
/*      */     //   110: lcmp
/*      */     //   111: ifne -> 119
/*      */     //   114: lload #5
/*      */     //   116: goto -> 126
/*      */     //   119: lload #9
/*      */     //   121: lload #5
/*      */     //   123: invokestatic remainingNanos : (JJ)J
/*      */     //   126: iload #8
/*      */     //   128: invokespecial awaitNanos : (Lcom/google/common/util/concurrent/Monitor$Guard;JZ)Z
/*      */     //   131: ifeq -> 138
/*      */     //   134: iconst_1
/*      */     //   135: goto -> 139
/*      */     //   138: iconst_0
/*      */     //   139: istore #11
/*      */     //   141: iconst_0
/*      */     //   142: istore #12
/*      */     //   144: iload #11
/*      */     //   146: istore #13
/*      */     //   148: iload #11
/*      */     //   150: ifne -> 185
/*      */     //   153: iload #12
/*      */     //   155: ifeq -> 167
/*      */     //   158: iload #8
/*      */     //   160: ifne -> 167
/*      */     //   163: aload_0
/*      */     //   164: invokespecial signalNextWaiter : ()V
/*      */     //   167: aload #7
/*      */     //   169: invokevirtual unlock : ()V
/*      */     //   172: goto -> 185
/*      */     //   175: astore #14
/*      */     //   177: aload #7
/*      */     //   179: invokevirtual unlock : ()V
/*      */     //   182: aload #14
/*      */     //   184: athrow
/*      */     //   185: iload #13
/*      */     //   187: ireturn
/*      */     //   188: astore #15
/*      */     //   190: iload #11
/*      */     //   192: ifne -> 227
/*      */     //   195: iload #12
/*      */     //   197: ifeq -> 209
/*      */     //   200: iload #8
/*      */     //   202: ifne -> 209
/*      */     //   205: aload_0
/*      */     //   206: invokespecial signalNextWaiter : ()V
/*      */     //   209: aload #7
/*      */     //   211: invokevirtual unlock : ()V
/*      */     //   214: goto -> 227
/*      */     //   217: astore #16
/*      */     //   219: aload #7
/*      */     //   221: invokevirtual unlock : ()V
/*      */     //   224: aload #16
/*      */     //   226: athrow
/*      */     //   227: aload #15
/*      */     //   229: athrow
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #519	-> 0
/*      */     //   #520	-> 8
/*      */     //   #521	-> 16
/*      */     //   #523	-> 24
/*      */     //   #524	-> 30
/*      */     //   #525	-> 37
/*      */     //   #529	-> 40
/*      */     //   #531	-> 47
/*      */     //   #532	-> 53
/*      */     //   #534	-> 61
/*      */     //   #535	-> 69
/*      */     //   #538	-> 72
/*      */     //   #539	-> 79
/*      */     //   #540	-> 90
/*      */     //   #544	-> 92
/*      */     //   #545	-> 95
/*      */     //   #547	-> 98
/*      */     //   #548	-> 99
/*      */     //   #551	-> 107
/*      */     //   #549	-> 128
/*      */     //   #553	-> 141
/*      */     //   #554	-> 144
/*      */     //   #556	-> 148
/*      */     //   #559	-> 153
/*      */     //   #560	-> 163
/*      */     //   #563	-> 167
/*      */     //   #564	-> 172
/*      */     //   #563	-> 175
/*      */     //   #564	-> 182
/*      */     //   #554	-> 185
/*      */     //   #556	-> 188
/*      */     //   #559	-> 195
/*      */     //   #560	-> 205
/*      */     //   #563	-> 209
/*      */     //   #564	-> 214
/*      */     //   #563	-> 217
/*      */     //   #564	-> 224
/*      */     //   #566	-> 227
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	230	0	this	Lcom/google/common/util/concurrent/Monitor;
/*      */     //   0	230	1	guard	Lcom/google/common/util/concurrent/Monitor$Guard;
/*      */     //   0	230	2	time	J
/*      */     //   0	230	4	unit	Ljava/util/concurrent/TimeUnit;
/*      */     //   8	222	5	timeoutNanos	J
/*      */     //   30	200	7	lock	Ljava/util/concurrent/locks/ReentrantLock;
/*      */     //   37	193	8	reentrant	Z
/*      */     //   40	190	9	startTime	J
/*      */     //   95	135	11	satisfied	Z
/*      */     //   98	132	12	threw	Z
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   98	148	188	finally
/*      */     //   153	167	175	finally
/*      */     //   175	177	175	finally
/*      */     //   188	190	188	finally
/*      */     //   195	209	217	finally
/*      */     //   217	219	217	finally
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void enterWhenUninterruptibly(Guard guard) {
/*  571 */     if (guard.monitor != this) {
/*  572 */       throw new IllegalMonitorStateException();
/*      */     }
/*  574 */     ReentrantLock lock = this.lock;
/*  575 */     boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
/*  576 */     lock.lock();
/*      */     
/*  578 */     boolean satisfied = false;
/*      */     try {
/*  580 */       if (!guard.isSatisfied()) {
/*  581 */         awaitUninterruptibly(guard, signalBeforeWaiting);
/*      */       }
/*  583 */       satisfied = true;
/*      */     } finally {
/*  585 */       if (!satisfied) {
/*  586 */         leave();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean enterWhenUninterruptibly(Guard guard, Duration time) {
/*  599 */     return enterWhenUninterruptibly(guard, Internal.toNanosSaturated(time), TimeUnit.NANOSECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean enterWhenUninterruptibly(Guard guard, long time, TimeUnit unit) {
/*  610 */     long timeoutNanos = toSafeNanos(time, unit);
/*  611 */     if (guard.monitor != this) {
/*  612 */       throw new IllegalMonitorStateException();
/*      */     }
/*  614 */     ReentrantLock lock = this.lock;
/*  615 */     long startTime = 0L;
/*  616 */     boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
/*  617 */     boolean interrupted = Thread.interrupted();
/*      */     try {
/*  619 */       if (this.fair || !lock.tryLock()) {
/*  620 */         startTime = initNanoTime(timeoutNanos);
/*  621 */         long remainingNanos = timeoutNanos; while (true) {
/*      */           try {
/*  623 */             if (lock.tryLock(remainingNanos, TimeUnit.NANOSECONDS)) {
/*      */               break;
/*      */             }
/*  626 */             return false;
/*      */           }
/*  628 */           catch (InterruptedException interrupt) {
/*  629 */             interrupted = true;
/*  630 */             remainingNanos = remainingNanos(startTime, timeoutNanos);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  635 */       boolean satisfied = false;
/*      */       
/*      */       while (true) {
/*      */         try {
/*  639 */           if (guard.isSatisfied()) {
/*  640 */             satisfied = true;
/*      */           } else {
/*      */             long remainingNanos;
/*  643 */             if (startTime == 0L) {
/*  644 */               startTime = initNanoTime(timeoutNanos);
/*  645 */               remainingNanos = timeoutNanos;
/*      */             } else {
/*  647 */               remainingNanos = remainingNanos(startTime, timeoutNanos);
/*      */             } 
/*  649 */             satisfied = awaitNanos(guard, remainingNanos, signalBeforeWaiting);
/*      */           } 
/*  651 */           return satisfied;
/*  652 */         } catch (InterruptedException interrupt) {
/*  653 */           interrupted = true;
/*      */         
/*      */         }
/*      */         finally {
/*      */           
/*  658 */           if (!satisfied)
/*  659 */             lock.unlock(); 
/*      */         } 
/*      */       } 
/*      */     } finally {
/*  663 */       if (interrupted) {
/*  664 */         Thread.currentThread().interrupt();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean enterIf(Guard guard) {
/*  676 */     if (guard.monitor != this) {
/*  677 */       throw new IllegalMonitorStateException();
/*      */     }
/*  679 */     ReentrantLock lock = this.lock;
/*  680 */     lock.lock();
/*      */     
/*  682 */     boolean satisfied = false;
/*      */     try {
/*  684 */       return satisfied = guard.isSatisfied();
/*      */     } finally {
/*  686 */       if (!satisfied) {
/*  687 */         lock.unlock();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean enterIf(Guard guard, Duration time) {
/*  700 */     return enterIf(guard, Internal.toNanosSaturated(time), TimeUnit.NANOSECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean enterIf(Guard guard, long time, TimeUnit unit) {
/*  711 */     if (guard.monitor != this) {
/*  712 */       throw new IllegalMonitorStateException();
/*      */     }
/*  714 */     if (!enter(time, unit)) {
/*  715 */       return false;
/*      */     }
/*      */     
/*  718 */     boolean satisfied = false;
/*      */     try {
/*  720 */       return satisfied = guard.isSatisfied();
/*      */     } finally {
/*  722 */       if (!satisfied) {
/*  723 */         this.lock.unlock();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean enterIfInterruptibly(Guard guard) throws InterruptedException {
/*  736 */     if (guard.monitor != this) {
/*  737 */       throw new IllegalMonitorStateException();
/*      */     }
/*  739 */     ReentrantLock lock = this.lock;
/*  740 */     lock.lockInterruptibly();
/*      */     
/*  742 */     boolean satisfied = false;
/*      */     try {
/*  744 */       return satisfied = guard.isSatisfied();
/*      */     } finally {
/*  746 */       if (!satisfied) {
/*  747 */         lock.unlock();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean enterIfInterruptibly(Guard guard, Duration time) throws InterruptedException {
/*  760 */     return enterIfInterruptibly(guard, Internal.toNanosSaturated(time), TimeUnit.NANOSECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean enterIfInterruptibly(Guard guard, long time, TimeUnit unit) throws InterruptedException {
/*  772 */     if (guard.monitor != this) {
/*  773 */       throw new IllegalMonitorStateException();
/*      */     }
/*  775 */     ReentrantLock lock = this.lock;
/*  776 */     if (!lock.tryLock(time, unit)) {
/*  777 */       return false;
/*      */     }
/*      */     
/*  780 */     boolean satisfied = false;
/*      */     try {
/*  782 */       return satisfied = guard.isSatisfied();
/*      */     } finally {
/*  784 */       if (!satisfied) {
/*  785 */         lock.unlock();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean tryEnterIf(Guard guard) {
/*  799 */     if (guard.monitor != this) {
/*  800 */       throw new IllegalMonitorStateException();
/*      */     }
/*  802 */     ReentrantLock lock = this.lock;
/*  803 */     if (!lock.tryLock()) {
/*  804 */       return false;
/*      */     }
/*      */     
/*  807 */     boolean satisfied = false;
/*      */     try {
/*  809 */       return satisfied = guard.isSatisfied();
/*      */     } finally {
/*  811 */       if (!satisfied) {
/*  812 */         lock.unlock();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void waitFor(Guard guard) throws InterruptedException {
/*  824 */     if (guard.monitor != this || !this.lock.isHeldByCurrentThread()) {
/*  825 */       throw new IllegalMonitorStateException();
/*      */     }
/*  827 */     if (!guard.isSatisfied()) {
/*  828 */       await(guard, true);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean waitFor(Guard guard, Duration time) throws InterruptedException {
/*  841 */     return waitFor(guard, Internal.toNanosSaturated(time), TimeUnit.NANOSECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean waitFor(Guard guard, long time, TimeUnit unit) throws InterruptedException {
/*  853 */     long timeoutNanos = toSafeNanos(time, unit);
/*  854 */     if (guard.monitor != this || !this.lock.isHeldByCurrentThread()) {
/*  855 */       throw new IllegalMonitorStateException();
/*      */     }
/*  857 */     if (guard.isSatisfied()) {
/*  858 */       return true;
/*      */     }
/*  860 */     if (Thread.interrupted()) {
/*  861 */       throw new InterruptedException();
/*      */     }
/*  863 */     return awaitNanos(guard, timeoutNanos, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void waitForUninterruptibly(Guard guard) {
/*  871 */     if (guard.monitor != this || !this.lock.isHeldByCurrentThread()) {
/*  872 */       throw new IllegalMonitorStateException();
/*      */     }
/*  874 */     if (!guard.isSatisfied()) {
/*  875 */       awaitUninterruptibly(guard, true);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean waitForUninterruptibly(Guard guard, Duration time) {
/*  887 */     return waitForUninterruptibly(guard, Internal.toNanosSaturated(time), TimeUnit.NANOSECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean waitForUninterruptibly(Guard guard, long time, TimeUnit unit) {
/*  898 */     long timeoutNanos = toSafeNanos(time, unit);
/*  899 */     if (guard.monitor != this || !this.lock.isHeldByCurrentThread()) {
/*  900 */       throw new IllegalMonitorStateException();
/*      */     }
/*  902 */     if (guard.isSatisfied()) {
/*  903 */       return true;
/*      */     }
/*  905 */     boolean signalBeforeWaiting = true;
/*  906 */     long startTime = initNanoTime(timeoutNanos);
/*  907 */     boolean interrupted = Thread.interrupted();
/*      */     try {
/*  909 */       long remainingNanos = timeoutNanos; while (true) {
/*      */         try {
/*  911 */           return awaitNanos(guard, remainingNanos, signalBeforeWaiting);
/*  912 */         } catch (InterruptedException interrupt) {
/*  913 */           interrupted = true;
/*  914 */           if (guard.isSatisfied()) {
/*  915 */             return true;
/*      */           }
/*  917 */           signalBeforeWaiting = false;
/*  918 */           remainingNanos = remainingNanos(startTime, timeoutNanos);
/*      */         } 
/*      */       } 
/*      */     } finally {
/*  922 */       if (interrupted) {
/*  923 */         Thread.currentThread().interrupt();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void leave() {
/*  930 */     ReentrantLock lock = this.lock;
/*      */     
/*      */     try {
/*  933 */       if (lock.getHoldCount() == 1) {
/*  934 */         signalNextWaiter();
/*      */       }
/*      */     } finally {
/*  937 */       lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFair() {
/*  943 */     return this.fair;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isOccupied() {
/*  951 */     return this.lock.isLocked();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isOccupiedByCurrentThread() {
/*  959 */     return this.lock.isHeldByCurrentThread();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getOccupiedDepth() {
/*  967 */     return this.lock.getHoldCount();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getQueueLength() {
/*  977 */     return this.lock.getQueueLength();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasQueuedThreads() {
/*  987 */     return this.lock.hasQueuedThreads();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasQueuedThread(Thread thread) {
/*  997 */     return this.lock.hasQueuedThread(thread);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasWaiters(Guard guard) {
/* 1007 */     return (getWaitQueueLength(guard) > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getWaitQueueLength(Guard guard) {
/* 1017 */     if (guard.monitor != this) {
/* 1018 */       throw new IllegalMonitorStateException();
/*      */     }
/* 1020 */     this.lock.lock();
/*      */     try {
/* 1022 */       return guard.waiterCount;
/*      */     } finally {
/* 1024 */       this.lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static long toSafeNanos(long time, TimeUnit unit) {
/* 1034 */     long timeoutNanos = unit.toNanos(time);
/* 1035 */     return Longs.constrainToRange(timeoutNanos, 0L, 6917529027641081853L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static long initNanoTime(long timeoutNanos) {
/* 1043 */     if (timeoutNanos <= 0L) {
/* 1044 */       return 0L;
/*      */     }
/* 1046 */     long startTime = System.nanoTime();
/* 1047 */     return (startTime == 0L) ? 1L : startTime;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static long remainingNanos(long startTime, long timeoutNanos) {
/* 1063 */     return (timeoutNanos <= 0L) ? 0L : (timeoutNanos - System.nanoTime() - startTime);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("lock")
/*      */   private void signalNextWaiter() {
/* 1092 */     for (Guard guard = this.activeGuards; guard != null; guard = guard.next) {
/* 1093 */       if (isSatisfied(guard)) {
/* 1094 */         guard.condition.signal();
/*      */         break;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("lock")
/*      */   private boolean isSatisfied(Guard guard) {
/*      */     try {
/* 1125 */       return guard.isSatisfied();
/* 1126 */     } catch (Throwable throwable) {
/*      */       
/* 1128 */       signalAllWaiters();
/* 1129 */       throw throwable;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   @GuardedBy("lock")
/*      */   private void signalAllWaiters() {
/* 1136 */     for (Guard guard = this.activeGuards; guard != null; guard = guard.next) {
/* 1137 */       guard.condition.signalAll();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   @GuardedBy("lock")
/*      */   private void beginWaitingFor(Guard guard) {
/* 1144 */     int waiters = guard.waiterCount++;
/* 1145 */     if (waiters == 0) {
/*      */       
/* 1147 */       guard.next = this.activeGuards;
/* 1148 */       this.activeGuards = guard;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   @GuardedBy("lock")
/*      */   private void endWaitingFor(Guard guard) {
/* 1155 */     int waiters = --guard.waiterCount;
/* 1156 */     if (waiters == 0)
/*      */     {
/* 1158 */       for (Guard p = this.activeGuards, pred = null;; pred = p, p = p.next) {
/* 1159 */         if (p == guard) {
/* 1160 */           if (pred == null) {
/* 1161 */             this.activeGuards = p.next;
/*      */           } else {
/* 1163 */             pred.next = p.next;
/*      */           } 
/* 1165 */           p.next = null;
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("lock")
/*      */   private void await(Guard guard, boolean signalBeforeWaiting) throws InterruptedException {
/* 1180 */     if (signalBeforeWaiting) {
/* 1181 */       signalNextWaiter();
/*      */     }
/* 1183 */     beginWaitingFor(guard);
/*      */     try {
/*      */       do {
/* 1186 */         guard.condition.await();
/* 1187 */       } while (!guard.isSatisfied());
/*      */     } finally {
/* 1189 */       endWaitingFor(guard);
/*      */     } 
/*      */   }
/*      */   
/*      */   @GuardedBy("lock")
/*      */   private void awaitUninterruptibly(Guard guard, boolean signalBeforeWaiting) {
/* 1195 */     if (signalBeforeWaiting) {
/* 1196 */       signalNextWaiter();
/*      */     }
/* 1198 */     beginWaitingFor(guard);
/*      */     try {
/*      */       do {
/* 1201 */         guard.condition.awaitUninterruptibly();
/* 1202 */       } while (!guard.isSatisfied());
/*      */     } finally {
/* 1204 */       endWaitingFor(guard);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("lock")
/*      */   private boolean awaitNanos(Guard guard, long nanos, boolean signalBeforeWaiting) throws InterruptedException {
/* 1212 */     boolean firstTime = true;
/*      */     
/*      */     try { while (true) {
/* 1215 */         if (nanos <= 0L) {
/* 1216 */           return false;
/*      */         }
/* 1218 */         if (firstTime) {
/* 1219 */           if (signalBeforeWaiting) {
/* 1220 */             signalNextWaiter();
/*      */           }
/* 1222 */           beginWaitingFor(guard);
/* 1223 */           firstTime = false;
/*      */         } 
/* 1225 */         nanos = guard.condition.awaitNanos(nanos);
/* 1226 */         if (guard.isSatisfied())
/* 1227 */           return true; 
/*      */       }  }
/* 1229 */     finally { if (!firstTime)
/* 1230 */         endWaitingFor(guard);  }
/*      */   
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/Monitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */