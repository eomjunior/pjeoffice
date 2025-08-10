/*      */ package com.google.common.util.concurrent;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Strings;
/*      */ import com.google.common.util.concurrent.internal.InternalFutureFailureAccess;
/*      */ import com.google.common.util.concurrent.internal.InternalFutures;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import com.google.errorprone.annotations.ForOverride;
/*      */ import com.google.j2objc.annotations.ReflectionSupport;
/*      */ import java.lang.reflect.Field;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.util.Locale;
/*      */ import java.util.Objects;
/*      */ import java.util.concurrent.CancellationException;
/*      */ import java.util.concurrent.ExecutionException;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.Future;
/*      */ import java.util.concurrent.ScheduledFuture;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.TimeoutException;
/*      */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
/*      */ import java.util.concurrent.locks.LockSupport;
/*      */ import java.util.logging.Level;
/*      */ import javax.annotation.CheckForNull;
/*      */ import sun.misc.Unsafe;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */ @GwtCompatible(emulated = true)
/*      */ @ReflectionSupport(ReflectionSupport.Level.FULL)
/*      */ public abstract class AbstractFuture<V>
/*      */   extends InternalFutureFailureAccess
/*      */   implements ListenableFuture<V>
/*      */ {
/*      */   static final boolean GENERATE_CANCELLATION_CAUSES;
/*      */   
/*      */   static {
/*      */     boolean generateCancellationCauses;
/*      */     AtomicHelper helper;
/*      */     try {
/*   85 */       generateCancellationCauses = Boolean.parseBoolean(
/*   86 */           System.getProperty("guava.concurrent.generate_cancellation_cause", "false"));
/*   87 */     } catch (SecurityException e) {
/*   88 */       generateCancellationCauses = false;
/*      */     } 
/*   90 */     GENERATE_CANCELLATION_CAUSES = generateCancellationCauses;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static interface Trusted<V>
/*      */     extends ListenableFuture<V> {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static abstract class TrustedFuture<V>
/*      */     extends AbstractFuture<V>
/*      */     implements Trusted<V>
/*      */   {
/*      */     @ParametricNullness
/*      */     @CanIgnoreReturnValue
/*      */     public final V get() throws InterruptedException, ExecutionException {
/*  110 */       return super.get();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     @CanIgnoreReturnValue
/*      */     public final V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/*  118 */       return super.get(timeout, unit);
/*      */     }
/*      */ 
/*      */     
/*      */     public final boolean isDone() {
/*  123 */       return super.isDone();
/*      */     }
/*      */ 
/*      */     
/*      */     public final boolean isCancelled() {
/*  128 */       return super.isCancelled();
/*      */     }
/*      */ 
/*      */     
/*      */     public final void addListener(Runnable listener, Executor executor) {
/*  133 */       super.addListener(listener, executor);
/*      */     }
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     public final boolean cancel(boolean mayInterruptIfRunning) {
/*  139 */       return super.cancel(mayInterruptIfRunning);
/*      */     }
/*      */   }
/*      */   
/*  143 */   static final LazyLogger log = new LazyLogger(AbstractFuture.class);
/*      */ 
/*      */   
/*      */   private static final long SPIN_THRESHOLD_NANOS = 1000L;
/*      */ 
/*      */   
/*      */   private static final AtomicHelper ATOMIC_HELPER;
/*      */ 
/*      */   
/*      */   static {
/*  153 */     Throwable thrownUnsafeFailure = null;
/*  154 */     Throwable thrownAtomicReferenceFieldUpdaterFailure = null;
/*      */     
/*      */     try {
/*  157 */       helper = new UnsafeAtomicHelper();
/*  158 */     } catch (Exception|Error unsafeFailure) {
/*  159 */       thrownUnsafeFailure = unsafeFailure;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/*  170 */         helper = new SafeAtomicHelper(AtomicReferenceFieldUpdater.newUpdater(Waiter.class, Thread.class, "thread"), AtomicReferenceFieldUpdater.newUpdater(Waiter.class, Waiter.class, "next"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Waiter.class, "waiters"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Listener.class, "listeners"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Object.class, "value"));
/*  171 */       } catch (Exception|Error atomicReferenceFieldUpdaterFailure) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  177 */         thrownAtomicReferenceFieldUpdaterFailure = atomicReferenceFieldUpdaterFailure;
/*  178 */         helper = new SynchronizedHelper();
/*      */       } 
/*      */     } 
/*  181 */     ATOMIC_HELPER = helper;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  186 */     Class<?> ensureLoaded = LockSupport.class;
/*      */ 
/*      */ 
/*      */     
/*  190 */     if (thrownAtomicReferenceFieldUpdaterFailure != null) {
/*  191 */       log.get().log(Level.SEVERE, "UnsafeAtomicHelper is broken!", thrownUnsafeFailure);
/*  192 */       log.get()
/*  193 */         .log(Level.SEVERE, "SafeAtomicHelper is broken!", thrownAtomicReferenceFieldUpdaterFailure);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class Waiter
/*      */   {
/*  202 */     static final Waiter TOMBSTONE = new Waiter(false);
/*      */     
/*      */     @CheckForNull
/*      */     volatile Thread thread;
/*      */     
/*      */     @CheckForNull
/*      */     volatile Waiter next;
/*      */ 
/*      */     
/*      */     Waiter(boolean unused) {}
/*      */ 
/*      */     
/*      */     Waiter() {
/*  215 */       AbstractFuture.ATOMIC_HELPER.putThread(this, Thread.currentThread());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     void setNext(@CheckForNull Waiter next) {
/*  221 */       AbstractFuture.ATOMIC_HELPER.putNext(this, next);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void unpark() {
/*  228 */       Thread w = this.thread;
/*  229 */       if (w != null) {
/*  230 */         this.thread = null;
/*  231 */         LockSupport.unpark(w);
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
/*      */   private void removeWaiter(Waiter node) {
/*  248 */     node.thread = null;
/*      */     
/*      */     label22: while (true) {
/*  251 */       Waiter pred = null;
/*  252 */       Waiter curr = this.waiters;
/*  253 */       if (curr == Waiter.TOMBSTONE) {
/*      */         return;
/*      */       }
/*      */       
/*  257 */       while (curr != null) {
/*  258 */         Waiter succ = curr.next;
/*  259 */         if (curr.thread != null) {
/*  260 */           pred = curr;
/*  261 */         } else if (pred != null) {
/*  262 */           pred.next = succ;
/*  263 */           if (pred.thread == null) {
/*      */             continue label22;
/*      */           }
/*  266 */         } else if (!ATOMIC_HELPER.casWaiters(this, curr, succ)) {
/*      */           continue label22;
/*      */         } 
/*  269 */         curr = succ;
/*      */       } 
/*      */       break;
/*      */     } 
/*      */   }
/*      */   
/*      */   private static final class Listener
/*      */   {
/*  277 */     static final Listener TOMBSTONE = new Listener();
/*      */     
/*      */     @CheckForNull
/*      */     final Runnable task;
/*      */     @CheckForNull
/*      */     final Executor executor;
/*      */     @CheckForNull
/*      */     Listener next;
/*      */     
/*      */     Listener(Runnable task, Executor executor) {
/*  287 */       this.task = task;
/*  288 */       this.executor = executor;
/*      */     }
/*      */     
/*      */     Listener() {
/*  292 */       this.task = null;
/*  293 */       this.executor = null;
/*      */     }
/*      */   }
/*      */   @CheckForNull
/*      */   private volatile Object value;
/*  298 */   private static final Object NULL = new Object(); @CheckForNull
/*      */   private volatile Listener listeners; @CheckForNull
/*      */   private volatile Waiter waiters;
/*      */   
/*  302 */   private static final class Failure { static final Failure FALLBACK_INSTANCE = new Failure(new Throwable("Failure occurred while trying to finish a future.")
/*      */         {
/*      */           
/*      */           public synchronized Throwable fillInStackTrace()
/*      */           {
/*  307 */             return this;
/*      */           }
/*      */         });
/*      */     final Throwable exception;
/*      */     
/*      */     Failure(Throwable exception) {
/*  313 */       this.exception = (Throwable)Preconditions.checkNotNull(exception);
/*      */     } }
/*      */ 
/*      */   
/*      */   private static final class Cancellation {
/*      */     @CheckForNull
/*      */     static final Cancellation CAUSELESS_INTERRUPTED;
/*      */     @CheckForNull
/*      */     static final Cancellation CAUSELESS_CANCELLED;
/*      */     
/*      */     static {
/*  324 */       if (AbstractFuture.GENERATE_CANCELLATION_CAUSES) {
/*  325 */         CAUSELESS_CANCELLED = null;
/*  326 */         CAUSELESS_INTERRUPTED = null;
/*      */       } else {
/*  328 */         CAUSELESS_CANCELLED = new Cancellation(false, null);
/*  329 */         CAUSELESS_INTERRUPTED = new Cancellation(true, null);
/*      */       } 
/*      */     }
/*      */     final boolean wasInterrupted;
/*      */     @CheckForNull
/*      */     final Throwable cause;
/*      */     
/*      */     Cancellation(boolean wasInterrupted, @CheckForNull Throwable cause) {
/*  337 */       this.wasInterrupted = wasInterrupted;
/*  338 */       this.cause = cause;
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class SetFuture<V>
/*      */     implements Runnable {
/*      */     final AbstractFuture<V> owner;
/*      */     final ListenableFuture<? extends V> future;
/*      */     
/*      */     SetFuture(AbstractFuture<V> owner, ListenableFuture<? extends V> future) {
/*  348 */       this.owner = owner;
/*  349 */       this.future = future;
/*      */     }
/*      */ 
/*      */     
/*      */     public void run() {
/*  354 */       if (this.owner.value != this) {
/*      */         return;
/*      */       }
/*      */       
/*  358 */       Object valueToSet = AbstractFuture.getFutureValue(this.future);
/*  359 */       if (AbstractFuture.ATOMIC_HELPER.casValue(this.owner, this, valueToSet)) {
/*  360 */         AbstractFuture.complete(this.owner, false);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @ParametricNullness
/*      */   @CanIgnoreReturnValue
/*      */   public V get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException, ExecutionException {
/*  438 */     long timeoutNanos = unit.toNanos(timeout);
/*  439 */     long remainingNanos = timeoutNanos;
/*  440 */     if (Thread.interrupted()) {
/*  441 */       throw new InterruptedException();
/*      */     }
/*  443 */     Object localValue = this.value;
/*  444 */     if ((((localValue != null) ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0)) != 0) {
/*  445 */       return getDoneValue(localValue);
/*      */     }
/*      */     
/*  448 */     long endNanos = (remainingNanos > 0L) ? (System.nanoTime() + remainingNanos) : 0L;
/*      */     
/*  450 */     if (remainingNanos >= 1000L) {
/*  451 */       Waiter oldHead = this.waiters;
/*  452 */       if (oldHead != Waiter.TOMBSTONE) {
/*  453 */         Waiter node = new Waiter();
/*      */         label77: while (true) {
/*  455 */           node.setNext(oldHead);
/*  456 */           if (ATOMIC_HELPER.casWaiters(this, oldHead, node)) {
/*      */             do {
/*  458 */               OverflowAvoidingLockSupport.parkNanos(this, remainingNanos);
/*      */               
/*  460 */               if (Thread.interrupted()) {
/*  461 */                 removeWaiter(node);
/*  462 */                 throw new InterruptedException();
/*      */               } 
/*      */ 
/*      */ 
/*      */               
/*  467 */               localValue = this.value;
/*  468 */               if ((((localValue != null) ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0)) != 0) {
/*  469 */                 return getDoneValue(localValue);
/*      */               }
/*      */ 
/*      */               
/*  473 */               remainingNanos = endNanos - System.nanoTime();
/*  474 */             } while (remainingNanos >= 1000L);
/*      */             
/*  476 */             removeWaiter(node);
/*      */             
/*      */             break;
/*      */           } 
/*      */           
/*  481 */           oldHead = this.waiters;
/*  482 */           if (oldHead == Waiter.TOMBSTONE) {
/*      */             break label77;
/*      */           }
/*      */         } 
/*      */       } else {
/*  487 */         return getDoneValue(Objects.requireNonNull(this.value));
/*      */       } 
/*      */     } 
/*      */     
/*  491 */     while (remainingNanos > 0L) {
/*  492 */       localValue = this.value;
/*  493 */       if ((((localValue != null) ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0)) != 0) {
/*  494 */         return getDoneValue(localValue);
/*      */       }
/*  496 */       if (Thread.interrupted()) {
/*  497 */         throw new InterruptedException();
/*      */       }
/*  499 */       remainingNanos = endNanos - System.nanoTime();
/*      */     } 
/*      */     
/*  502 */     String futureToString = toString();
/*  503 */     String unitString = unit.toString().toLowerCase(Locale.ROOT);
/*  504 */     String message = "Waited " + timeout + " " + unit.toString().toLowerCase(Locale.ROOT);
/*      */     
/*  506 */     if (remainingNanos + 1000L < 0L) {
/*      */       
/*  508 */       message = message + " (plus ";
/*  509 */       long overWaitNanos = -remainingNanos;
/*  510 */       long overWaitUnits = unit.convert(overWaitNanos, TimeUnit.NANOSECONDS);
/*  511 */       long overWaitLeftoverNanos = overWaitNanos - unit.toNanos(overWaitUnits);
/*  512 */       boolean shouldShowExtraNanos = (overWaitUnits == 0L || overWaitLeftoverNanos > 1000L);
/*      */       
/*  514 */       if (overWaitUnits > 0L) {
/*  515 */         message = message + overWaitUnits + " " + unitString;
/*  516 */         if (shouldShowExtraNanos) {
/*  517 */           message = message + ",";
/*      */         }
/*  519 */         message = message + " ";
/*      */       } 
/*  521 */       if (shouldShowExtraNanos) {
/*  522 */         message = message + overWaitLeftoverNanos + " nanoseconds ";
/*      */       }
/*      */       
/*  525 */       message = message + "delay)";
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  530 */     if (isDone()) {
/*  531 */       throw new TimeoutException(message + " but future completed as timeout expired");
/*      */     }
/*  533 */     throw new TimeoutException(message + " for " + futureToString);
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
/*      */   @ParametricNullness
/*      */   @CanIgnoreReturnValue
/*      */   public V get() throws InterruptedException, ExecutionException {
/*  548 */     if (Thread.interrupted()) {
/*  549 */       throw new InterruptedException();
/*      */     }
/*  551 */     Object localValue = this.value;
/*  552 */     if ((((localValue != null) ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0)) != 0) {
/*  553 */       return getDoneValue(localValue);
/*      */     }
/*  555 */     Waiter oldHead = this.waiters;
/*  556 */     if (oldHead != Waiter.TOMBSTONE) {
/*  557 */       Waiter node = new Waiter();
/*      */       do {
/*  559 */         node.setNext(oldHead);
/*  560 */         if (ATOMIC_HELPER.casWaiters(this, oldHead, node)) {
/*      */           while (true) {
/*      */             
/*  563 */             LockSupport.park(this);
/*      */             
/*  565 */             if (Thread.interrupted()) {
/*  566 */               removeWaiter(node);
/*  567 */               throw new InterruptedException();
/*      */             } 
/*      */ 
/*      */             
/*  571 */             localValue = this.value;
/*  572 */             if ((((localValue != null) ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0)) != 0) {
/*  573 */               return getDoneValue(localValue);
/*      */             }
/*      */           } 
/*      */         }
/*  577 */         oldHead = this.waiters;
/*  578 */       } while (oldHead != Waiter.TOMBSTONE);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  583 */     return getDoneValue(Objects.requireNonNull(this.value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @ParametricNullness
/*      */   private V getDoneValue(Object obj) throws ExecutionException {
/*  591 */     if (obj instanceof Cancellation)
/*  592 */       throw cancellationExceptionWithCause("Task was cancelled.", ((Cancellation)obj).cause); 
/*  593 */     if (obj instanceof Failure)
/*  594 */       throw new ExecutionException(((Failure)obj).exception); 
/*  595 */     if (obj == NULL)
/*      */     {
/*      */ 
/*      */ 
/*      */       
/*  600 */       return NullnessCasts.uncheckedNull();
/*      */     }
/*      */     
/*  603 */     V asV = (V)obj;
/*  604 */     return asV;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDone() {
/*  610 */     Object localValue = this.value;
/*  611 */     return ((localValue != null)) & (!(localValue instanceof SetFuture));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isCancelled() {
/*  616 */     Object localValue = this.value;
/*  617 */     return localValue instanceof Cancellation;
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
/*      */   @CanIgnoreReturnValue
/*      */   public boolean cancel(boolean mayInterruptIfRunning) {
/*  639 */     Object localValue = this.value;
/*  640 */     boolean rValue = false;
/*  641 */     if ((((localValue == null) ? 1 : 0) | localValue instanceof SetFuture) != 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  658 */       Object valueToSet = GENERATE_CANCELLATION_CAUSES ? new Cancellation(mayInterruptIfRunning, new CancellationException("Future.cancel() was called.")) : Objects.<Cancellation>requireNonNull(
/*  659 */           mayInterruptIfRunning ? 
/*  660 */           Cancellation.CAUSELESS_INTERRUPTED : 
/*  661 */           Cancellation.CAUSELESS_CANCELLED);
/*  662 */       AbstractFuture<?> abstractFuture = this;
/*      */       do {
/*  664 */         while (ATOMIC_HELPER.casValue(abstractFuture, localValue, valueToSet)) {
/*  665 */           rValue = true;
/*  666 */           complete(abstractFuture, mayInterruptIfRunning);
/*  667 */           if (localValue instanceof SetFuture) {
/*      */ 
/*      */             
/*  670 */             ListenableFuture<?> futureToPropagateTo = ((SetFuture)localValue).future;
/*  671 */             if (futureToPropagateTo instanceof Trusted) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*  679 */               AbstractFuture<?> trusted = (AbstractFuture)futureToPropagateTo;
/*  680 */               localValue = trusted.value;
/*  681 */               if ((((localValue == null) ? 1 : 0) | localValue instanceof SetFuture) != 0) {
/*  682 */                 abstractFuture = trusted;
/*      */                 continue;
/*      */               } 
/*      */               // Byte code: goto -> 185
/*      */             } 
/*  687 */             futureToPropagateTo.cancel(mayInterruptIfRunning);
/*      */             
/*      */             break;
/*      */           } 
/*      */           // Byte code: goto -> 185
/*      */         } 
/*  693 */         localValue = abstractFuture.value;
/*  694 */       } while (localValue instanceof SetFuture);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  702 */     return rValue;
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
/*      */   protected void interruptTask() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final boolean wasInterrupted() {
/*  725 */     Object localValue = this.value;
/*  726 */     return (localValue instanceof Cancellation && ((Cancellation)localValue).wasInterrupted);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addListener(Runnable listener, Executor executor) {
/*  736 */     Preconditions.checkNotNull(listener, "Runnable was null.");
/*  737 */     Preconditions.checkNotNull(executor, "Executor was null.");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  747 */     if (!isDone()) {
/*  748 */       Listener oldHead = this.listeners;
/*  749 */       if (oldHead != Listener.TOMBSTONE) {
/*  750 */         Listener newNode = new Listener(listener, executor);
/*      */         do {
/*  752 */           newNode.next = oldHead;
/*  753 */           if (ATOMIC_HELPER.casListeners(this, oldHead, newNode)) {
/*      */             return;
/*      */           }
/*  756 */           oldHead = this.listeners;
/*  757 */         } while (oldHead != Listener.TOMBSTONE);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  762 */     executeListener(listener, executor);
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
/*      */   @CanIgnoreReturnValue
/*      */   protected boolean set(@ParametricNullness V value) {
/*  782 */     Object valueToSet = (value == null) ? NULL : value;
/*  783 */     if (ATOMIC_HELPER.casValue(this, null, valueToSet)) {
/*  784 */       complete(this, false);
/*  785 */       return true;
/*      */     } 
/*  787 */     return false;
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
/*      */   @CanIgnoreReturnValue
/*      */   protected boolean setException(Throwable throwable) {
/*  807 */     Object valueToSet = new Failure((Throwable)Preconditions.checkNotNull(throwable));
/*  808 */     if (ATOMIC_HELPER.casValue(this, null, valueToSet)) {
/*  809 */       complete(this, false);
/*  810 */       return true;
/*      */     } 
/*  812 */     return false;
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
/*      */   @CanIgnoreReturnValue
/*      */   protected boolean setFuture(ListenableFuture<? extends V> future) {
/*  846 */     Preconditions.checkNotNull(future);
/*  847 */     Object localValue = this.value;
/*  848 */     if (localValue == null) {
/*  849 */       if (future.isDone()) {
/*  850 */         Object value = getFutureValue(future);
/*  851 */         if (ATOMIC_HELPER.casValue(this, null, value)) {
/*  852 */           complete(this, false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  859 */           return true;
/*      */         } 
/*  861 */         return false;
/*      */       } 
/*  863 */       SetFuture<V> valueToSet = new SetFuture<>(this, future);
/*  864 */       if (ATOMIC_HELPER.casValue(this, null, valueToSet)) {
/*      */ 
/*      */         
/*      */         try {
/*  868 */           future.addListener(valueToSet, DirectExecutor.INSTANCE);
/*  869 */         } catch (Throwable t) {
/*      */           Failure failure;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           try {
/*  877 */             failure = new Failure(t);
/*  878 */           } catch (Exception|Error oomMostLikely) {
/*  879 */             failure = Failure.FALLBACK_INSTANCE;
/*      */           } 
/*      */           
/*  882 */           boolean bool = ATOMIC_HELPER.casValue(this, valueToSet, failure);
/*      */         } 
/*  884 */         return true;
/*      */       } 
/*  886 */       localValue = this.value;
/*      */     } 
/*      */ 
/*      */     
/*  890 */     if (localValue instanceof Cancellation)
/*      */     {
/*  892 */       future.cancel(((Cancellation)localValue).wasInterrupted);
/*      */     }
/*  894 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Object getFutureValue(ListenableFuture<?> future) {
/*  904 */     if (future instanceof Trusted) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  909 */       Object v = ((AbstractFuture)future).value;
/*  910 */       if (v instanceof Cancellation) {
/*      */ 
/*      */ 
/*      */         
/*  914 */         Cancellation c = (Cancellation)v;
/*  915 */         if (c.wasInterrupted)
/*      */         {
/*      */ 
/*      */           
/*  919 */           v = (c.cause != null) ? new Cancellation(false, c.cause) : Cancellation.CAUSELESS_CANCELLED;
/*      */         }
/*      */       } 
/*      */       
/*  923 */       return Objects.requireNonNull(v);
/*      */     } 
/*  925 */     if (future instanceof InternalFutureFailureAccess) {
/*      */       
/*  927 */       Throwable throwable = InternalFutures.tryInternalFastPathGetFailure((InternalFutureFailureAccess)future);
/*  928 */       if (throwable != null) {
/*  929 */         return new Failure(throwable);
/*      */       }
/*      */     } 
/*  932 */     boolean wasCancelled = future.isCancelled();
/*      */     
/*  934 */     if (((!GENERATE_CANCELLATION_CAUSES ? 1 : 0) & wasCancelled) != 0)
/*      */     {
/*      */ 
/*      */ 
/*      */       
/*  939 */       return Objects.requireNonNull(Cancellation.CAUSELESS_CANCELLED);
/*      */     }
/*      */     
/*      */     try {
/*  943 */       Object v = getUninterruptibly(future);
/*  944 */       if (wasCancelled) {
/*  945 */         return new Cancellation(false, new IllegalArgumentException("get() did not throw CancellationException, despite reporting isCancelled() == true: " + future));
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  952 */       return (v == null) ? NULL : v;
/*  953 */     } catch (ExecutionException exception) {
/*  954 */       if (wasCancelled) {
/*  955 */         return new Cancellation(false, new IllegalArgumentException("get() did not throw CancellationException, despite reporting isCancelled() == true: " + future, exception));
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  963 */       return new Failure(exception.getCause());
/*  964 */     } catch (CancellationException cancellation) {
/*  965 */       if (!wasCancelled) {
/*  966 */         return new Failure(new IllegalArgumentException("get() threw CancellationException, despite reporting isCancelled() == false: " + future, cancellation));
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  972 */       return new Cancellation(false, cancellation);
/*  973 */     } catch (Exception|Error t) {
/*  974 */       return new Failure(t);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @ParametricNullness
/*      */   private static <V> V getUninterruptibly(Future<V> future) throws ExecutionException {
/*  985 */     boolean interrupted = false;
/*      */     
/*      */     while (true) {
/*      */       try {
/*  989 */         return future.get();
/*  990 */       } catch (InterruptedException e) {
/*      */ 
/*      */       
/*      */       } finally {
/*      */         
/*  995 */         if (interrupted) {
/*  996 */           Thread.currentThread().interrupt();
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static void complete(AbstractFuture<?> param, boolean callInterruptTask) {
/* 1004 */     AbstractFuture<?> future = param;
/*      */     
/* 1006 */     Listener next = null;
/*      */     
/*      */     label20: while (true) {
/* 1009 */       future.releaseWaiters();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1014 */       if (callInterruptTask) {
/* 1015 */         future.interruptTask();
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1020 */         callInterruptTask = false;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1027 */       future.afterDone();
/*      */       
/* 1029 */       next = future.clearListeners(next);
/* 1030 */       future = null;
/* 1031 */       while (next != null) {
/* 1032 */         Listener curr = next;
/* 1033 */         next = next.next;
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1038 */         Runnable task = Objects.<Runnable>requireNonNull(curr.task);
/* 1039 */         if (task instanceof SetFuture) {
/* 1040 */           SetFuture<?> setFuture = (SetFuture)task;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1046 */           future = setFuture.owner;
/* 1047 */           if (future.value == setFuture) {
/* 1048 */             Object valueToSet = getFutureValue(setFuture.future);
/* 1049 */             if (ATOMIC_HELPER.casValue(future, setFuture, valueToSet)) {
/*      */               continue label20;
/*      */             }
/*      */           } 
/*      */ 
/*      */           
/*      */           continue;
/*      */         } 
/*      */ 
/*      */         
/* 1059 */         executeListener(task, Objects.<Executor>requireNonNull(curr.executor));
/*      */       } 
/*      */       break;
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
/*      */   @ForOverride
/*      */   protected void afterDone() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   protected final Throwable tryInternalFastPathGetFailure() {
/* 1109 */     if (this instanceof Trusted) {
/* 1110 */       Object obj = this.value;
/* 1111 */       if (obj instanceof Failure) {
/* 1112 */         return ((Failure)obj).exception;
/*      */       }
/*      */     } 
/* 1115 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void maybePropagateCancellationTo(@CheckForNull Future<?> related) {
/* 1123 */     if ((((related != null) ? 1 : 0) & isCancelled()) != 0) {
/* 1124 */       related.cancel(wasInterrupted());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void releaseWaiters() {
/* 1130 */     Waiter head = ATOMIC_HELPER.gasWaiters(this, Waiter.TOMBSTONE);
/* 1131 */     for (Waiter currentWaiter = head; currentWaiter != null; currentWaiter = currentWaiter.next) {
/* 1132 */       currentWaiter.unpark();
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
/*      */   @CheckForNull
/*      */   private Listener clearListeners(@CheckForNull Listener onto) {
/* 1148 */     Listener head = ATOMIC_HELPER.gasListeners(this, Listener.TOMBSTONE);
/* 1149 */     Listener reversedList = onto;
/* 1150 */     while (head != null) {
/* 1151 */       Listener tmp = head;
/* 1152 */       head = head.next;
/* 1153 */       tmp.next = reversedList;
/* 1154 */       reversedList = tmp;
/*      */     } 
/* 1156 */     return reversedList;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1163 */     StringBuilder builder = new StringBuilder();
/* 1164 */     if (getClass().getName().startsWith("com.google.common.util.concurrent.")) {
/* 1165 */       builder.append(getClass().getSimpleName());
/*      */     } else {
/* 1167 */       builder.append(getClass().getName());
/*      */     } 
/* 1169 */     builder.append('@').append(Integer.toHexString(System.identityHashCode(this))).append("[status=");
/* 1170 */     if (isCancelled()) {
/* 1171 */       builder.append("CANCELLED");
/* 1172 */     } else if (isDone()) {
/* 1173 */       addDoneString(builder);
/*      */     } else {
/* 1175 */       addPendingString(builder);
/*      */     } 
/* 1177 */     return builder.append("]").toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   protected String pendingToString() {
/* 1189 */     if (this instanceof ScheduledFuture) {
/* 1190 */       return "remaining delay=[" + ((ScheduledFuture)this)
/* 1191 */         .getDelay(TimeUnit.MILLISECONDS) + " ms]";
/*      */     }
/*      */     
/* 1194 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addPendingString(StringBuilder builder) {
/* 1201 */     int truncateLength = builder.length();
/*      */     
/* 1203 */     builder.append("PENDING");
/*      */     
/* 1205 */     Object localValue = this.value;
/* 1206 */     if (localValue instanceof SetFuture) {
/* 1207 */       builder.append(", setFuture=[");
/* 1208 */       appendUserObject(builder, ((SetFuture)localValue).future);
/* 1209 */       builder.append("]");
/*      */     } else {
/*      */       String pendingDescription;
/*      */       try {
/* 1213 */         pendingDescription = Strings.emptyToNull(pendingToString());
/* 1214 */       } catch (Exception|StackOverflowError e) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1219 */         pendingDescription = "Exception thrown from implementation: " + e.getClass();
/*      */       } 
/* 1221 */       if (pendingDescription != null) {
/* 1222 */         builder.append(", info=[").append(pendingDescription).append("]");
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1228 */     if (isDone()) {
/*      */       
/* 1230 */       builder.delete(truncateLength, builder.length());
/* 1231 */       addDoneString(builder);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void addDoneString(StringBuilder builder) {
/*      */     try {
/* 1238 */       V value = getUninterruptibly(this);
/* 1239 */       builder.append("SUCCESS, result=[");
/* 1240 */       appendResultObject(builder, value);
/* 1241 */       builder.append("]");
/* 1242 */     } catch (ExecutionException e) {
/* 1243 */       builder.append("FAILURE, cause=[").append(e.getCause()).append("]");
/* 1244 */     } catch (CancellationException e) {
/* 1245 */       builder.append("CANCELLED");
/* 1246 */     } catch (Exception e) {
/* 1247 */       builder.append("UNKNOWN, cause=[").append(e.getClass()).append(" thrown from get()]");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void appendResultObject(StringBuilder builder, @CheckForNull Object o) {
/* 1257 */     if (o == null) {
/* 1258 */       builder.append("null");
/* 1259 */     } else if (o == this) {
/* 1260 */       builder.append("this future");
/*      */     } else {
/* 1262 */       builder
/* 1263 */         .append(o.getClass().getName())
/* 1264 */         .append("@")
/* 1265 */         .append(Integer.toHexString(System.identityHashCode(o)));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void appendUserObject(StringBuilder builder, @CheckForNull Object o) {
/*      */     try {
/* 1277 */       if (o == this) {
/* 1278 */         builder.append("this future");
/*      */       } else {
/* 1280 */         builder.append(o);
/*      */       } 
/* 1282 */     } catch (Exception|StackOverflowError e) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1287 */       builder.append("Exception thrown from implementation: ").append(e.getClass());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void executeListener(Runnable runnable, Executor executor) {
/*      */     try {
/* 1298 */       executor.execute(runnable);
/* 1299 */     } catch (Exception e) {
/*      */ 
/*      */ 
/*      */       
/* 1303 */       log.get()
/* 1304 */         .log(Level.SEVERE, "RuntimeException while executing runnable " + runnable + " with executor " + executor, e);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static abstract class AtomicHelper
/*      */   {
/*      */     private AtomicHelper() {}
/*      */ 
/*      */     
/*      */     abstract void putThread(AbstractFuture.Waiter param1Waiter, Thread param1Thread);
/*      */ 
/*      */     
/*      */     abstract void putNext(AbstractFuture.Waiter param1Waiter1, @CheckForNull AbstractFuture.Waiter param1Waiter2);
/*      */ 
/*      */     
/*      */     abstract boolean casWaiters(AbstractFuture<?> param1AbstractFuture, @CheckForNull AbstractFuture.Waiter param1Waiter1, @CheckForNull AbstractFuture.Waiter param1Waiter2);
/*      */ 
/*      */     
/*      */     abstract boolean casListeners(AbstractFuture<?> param1AbstractFuture, @CheckForNull AbstractFuture.Listener param1Listener1, AbstractFuture.Listener param1Listener2);
/*      */ 
/*      */     
/*      */     abstract AbstractFuture.Waiter gasWaiters(AbstractFuture<?> param1AbstractFuture, AbstractFuture.Waiter param1Waiter);
/*      */ 
/*      */     
/*      */     abstract AbstractFuture.Listener gasListeners(AbstractFuture<?> param1AbstractFuture, AbstractFuture.Listener param1Listener);
/*      */ 
/*      */     
/*      */     abstract boolean casValue(AbstractFuture<?> param1AbstractFuture, @CheckForNull Object param1Object1, Object param1Object2);
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class UnsafeAtomicHelper
/*      */     extends AtomicHelper
/*      */   {
/*      */     static final Unsafe UNSAFE;
/*      */     
/*      */     static final long LISTENERS_OFFSET;
/*      */     
/*      */     static final long WAITERS_OFFSET;
/*      */     
/*      */     static final long VALUE_OFFSET;
/*      */     
/*      */     static final long WAITER_THREAD_OFFSET;
/*      */     
/*      */     static final long WAITER_NEXT_OFFSET;
/*      */ 
/*      */     
/*      */     private UnsafeAtomicHelper() {}
/*      */     
/*      */     static {
/* 1355 */       Unsafe unsafe = null;
/*      */       try {
/* 1357 */         unsafe = Unsafe.getUnsafe();
/* 1358 */       } catch (SecurityException tryReflectionInstead) {
/*      */         
/*      */         try {
/* 1361 */           unsafe = AccessController.<Unsafe>doPrivileged(new PrivilegedExceptionAction<Unsafe>()
/*      */               {
/*      */                 public Unsafe run() throws Exception
/*      */                 {
/* 1365 */                   Class<Unsafe> k = Unsafe.class;
/* 1366 */                   for (Field f : k.getDeclaredFields()) {
/* 1367 */                     f.setAccessible(true);
/* 1368 */                     Object x = f.get(null);
/* 1369 */                     if (k.isInstance(x)) {
/* 1370 */                       return k.cast(x);
/*      */                     }
/*      */                   } 
/* 1373 */                   throw new NoSuchFieldError("the Unsafe");
/*      */                 }
/*      */               });
/* 1376 */         } catch (PrivilegedActionException e) {
/* 1377 */           throw new RuntimeException("Could not initialize intrinsics", e.getCause());
/*      */         } 
/*      */       } 
/*      */       try {
/* 1381 */         Class<?> abstractFuture = AbstractFuture.class;
/* 1382 */         WAITERS_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("waiters"));
/* 1383 */         LISTENERS_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("listeners"));
/* 1384 */         VALUE_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("value"));
/* 1385 */         WAITER_THREAD_OFFSET = unsafe.objectFieldOffset(AbstractFuture.Waiter.class.getDeclaredField("thread"));
/* 1386 */         WAITER_NEXT_OFFSET = unsafe.objectFieldOffset(AbstractFuture.Waiter.class.getDeclaredField("next"));
/* 1387 */         UNSAFE = unsafe;
/* 1388 */       } catch (NoSuchFieldException e) {
/* 1389 */         throw new RuntimeException(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     void putThread(AbstractFuture.Waiter waiter, Thread newValue) {
/* 1395 */       UNSAFE.putObject(waiter, WAITER_THREAD_OFFSET, newValue);
/*      */     }
/*      */ 
/*      */     
/*      */     void putNext(AbstractFuture.Waiter waiter, @CheckForNull AbstractFuture.Waiter newValue) {
/* 1400 */       UNSAFE.putObject(waiter, WAITER_NEXT_OFFSET, newValue);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean casWaiters(AbstractFuture<?> future, @CheckForNull AbstractFuture.Waiter expect, @CheckForNull AbstractFuture.Waiter update) {
/* 1407 */       return UNSAFE.compareAndSwapObject(future, WAITERS_OFFSET, expect, update);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     boolean casListeners(AbstractFuture<?> future, @CheckForNull AbstractFuture.Listener expect, AbstractFuture.Listener update) {
/* 1413 */       return UNSAFE.compareAndSwapObject(future, LISTENERS_OFFSET, expect, update);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     AbstractFuture.Listener gasListeners(AbstractFuture<?> future, AbstractFuture.Listener update) {
/* 1419 */       return (AbstractFuture.Listener)UNSAFE.getAndSetObject(future, LISTENERS_OFFSET, update);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     AbstractFuture.Waiter gasWaiters(AbstractFuture<?> future, AbstractFuture.Waiter update) {
/* 1425 */       return (AbstractFuture.Waiter)UNSAFE.getAndSetObject(future, WAITERS_OFFSET, update);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     boolean casValue(AbstractFuture<?> future, @CheckForNull Object expect, Object update) {
/* 1431 */       return UNSAFE.compareAndSwapObject(future, VALUE_OFFSET, expect, update);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class SafeAtomicHelper
/*      */     extends AtomicHelper
/*      */   {
/*      */     final AtomicReferenceFieldUpdater<AbstractFuture.Waiter, Thread> waiterThreadUpdater;
/*      */     
/*      */     final AtomicReferenceFieldUpdater<AbstractFuture.Waiter, AbstractFuture.Waiter> waiterNextUpdater;
/*      */     
/*      */     final AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture.Waiter> waitersUpdater;
/*      */     
/*      */     final AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture.Listener> listenersUpdater;
/*      */     
/*      */     final AtomicReferenceFieldUpdater<AbstractFuture, Object> valueUpdater;
/*      */     
/*      */     SafeAtomicHelper(AtomicReferenceFieldUpdater<AbstractFuture.Waiter, Thread> waiterThreadUpdater, AtomicReferenceFieldUpdater<AbstractFuture.Waiter, AbstractFuture.Waiter> waiterNextUpdater, AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture.Waiter> waitersUpdater, AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture.Listener> listenersUpdater, AtomicReferenceFieldUpdater<AbstractFuture, Object> valueUpdater) {
/* 1450 */       this.waiterThreadUpdater = waiterThreadUpdater;
/* 1451 */       this.waiterNextUpdater = waiterNextUpdater;
/* 1452 */       this.waitersUpdater = waitersUpdater;
/* 1453 */       this.listenersUpdater = listenersUpdater;
/* 1454 */       this.valueUpdater = valueUpdater;
/*      */     }
/*      */ 
/*      */     
/*      */     void putThread(AbstractFuture.Waiter waiter, Thread newValue) {
/* 1459 */       this.waiterThreadUpdater.lazySet(waiter, newValue);
/*      */     }
/*      */ 
/*      */     
/*      */     void putNext(AbstractFuture.Waiter waiter, @CheckForNull AbstractFuture.Waiter newValue) {
/* 1464 */       this.waiterNextUpdater.lazySet(waiter, newValue);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     boolean casWaiters(AbstractFuture<?> future, @CheckForNull AbstractFuture.Waiter expect, @CheckForNull AbstractFuture.Waiter update) {
/* 1470 */       return this.waitersUpdater.compareAndSet(future, expect, update);
/*      */     }
/*      */ 
/*      */     
/*      */     boolean casListeners(AbstractFuture<?> future, @CheckForNull AbstractFuture.Listener expect, AbstractFuture.Listener update) {
/* 1475 */       return this.listenersUpdater.compareAndSet(future, expect, update);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     AbstractFuture.Listener gasListeners(AbstractFuture<?> future, AbstractFuture.Listener update) {
/* 1481 */       return this.listenersUpdater.getAndSet(future, update);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     AbstractFuture.Waiter gasWaiters(AbstractFuture<?> future, AbstractFuture.Waiter update) {
/* 1487 */       return this.waitersUpdater.getAndSet(future, update);
/*      */     }
/*      */ 
/*      */     
/*      */     boolean casValue(AbstractFuture<?> future, @CheckForNull Object expect, Object update) {
/* 1492 */       return this.valueUpdater.compareAndSet(future, expect, update);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class SynchronizedHelper
/*      */     extends AtomicHelper
/*      */   {
/*      */     private SynchronizedHelper() {}
/*      */ 
/*      */     
/*      */     void putThread(AbstractFuture.Waiter waiter, Thread newValue) {
/* 1505 */       waiter.thread = newValue;
/*      */     }
/*      */ 
/*      */     
/*      */     void putNext(AbstractFuture.Waiter waiter, @CheckForNull AbstractFuture.Waiter newValue) {
/* 1510 */       waiter.next = newValue;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     boolean casWaiters(AbstractFuture<?> future, @CheckForNull AbstractFuture.Waiter expect, @CheckForNull AbstractFuture.Waiter update) {
/* 1516 */       synchronized (future) {
/* 1517 */         if (future.waiters == expect) {
/* 1518 */           future.waiters = update;
/* 1519 */           return true;
/*      */         } 
/* 1521 */         return false;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     boolean casListeners(AbstractFuture<?> future, @CheckForNull AbstractFuture.Listener expect, AbstractFuture.Listener update) {
/* 1527 */       synchronized (future) {
/* 1528 */         if (future.listeners == expect) {
/* 1529 */           future.listeners = update;
/* 1530 */           return true;
/*      */         } 
/* 1532 */         return false;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     AbstractFuture.Listener gasListeners(AbstractFuture<?> future, AbstractFuture.Listener update) {
/* 1539 */       synchronized (future) {
/* 1540 */         AbstractFuture.Listener old = future.listeners;
/* 1541 */         if (old != update) {
/* 1542 */           future.listeners = update;
/*      */         }
/* 1544 */         return old;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     AbstractFuture.Waiter gasWaiters(AbstractFuture<?> future, AbstractFuture.Waiter update) {
/* 1551 */       synchronized (future) {
/* 1552 */         AbstractFuture.Waiter old = future.waiters;
/* 1553 */         if (old != update) {
/* 1554 */           future.waiters = update;
/*      */         }
/* 1556 */         return old;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     boolean casValue(AbstractFuture<?> future, @CheckForNull Object expect, Object update) {
/* 1562 */       synchronized (future) {
/* 1563 */         if (future.value == expect) {
/* 1564 */           future.value = update;
/* 1565 */           return true;
/*      */         } 
/* 1567 */         return false;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static CancellationException cancellationExceptionWithCause(String message, @CheckForNull Throwable cause) {
/* 1574 */     CancellationException exception = new CancellationException(message);
/* 1575 */     exception.initCause(cause);
/* 1576 */     return exception;
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/AbstractFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */