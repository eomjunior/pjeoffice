/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.ForOverride;
/*     */ import com.google.errorprone.annotations.concurrent.GuardedBy;
/*     */ import java.time.Duration;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtIncompatible
/*     */ @J2ktIncompatible
/*     */ public abstract class AbstractService
/*     */   implements Service
/*     */ {
/*  57 */   private static final ListenerCallQueue.Event<Service.Listener> STARTING_EVENT = new ListenerCallQueue.Event<Service.Listener>()
/*     */     {
/*     */       public void call(Service.Listener listener)
/*     */       {
/*  61 */         listener.starting();
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/*  66 */         return "starting()";
/*     */       }
/*     */     };
/*  69 */   private static final ListenerCallQueue.Event<Service.Listener> RUNNING_EVENT = new ListenerCallQueue.Event<Service.Listener>()
/*     */     {
/*     */       public void call(Service.Listener listener)
/*     */       {
/*  73 */         listener.running();
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/*  78 */         return "running()";
/*     */       }
/*     */     };
/*     */   
/*  82 */   private static final ListenerCallQueue.Event<Service.Listener> STOPPING_FROM_STARTING_EVENT = stoppingEvent(Service.State.STARTING);
/*     */   
/*  84 */   private static final ListenerCallQueue.Event<Service.Listener> STOPPING_FROM_RUNNING_EVENT = stoppingEvent(Service.State.RUNNING);
/*     */ 
/*     */   
/*  87 */   private static final ListenerCallQueue.Event<Service.Listener> TERMINATED_FROM_NEW_EVENT = terminatedEvent(Service.State.NEW);
/*     */   
/*  89 */   private static final ListenerCallQueue.Event<Service.Listener> TERMINATED_FROM_STARTING_EVENT = terminatedEvent(Service.State.STARTING);
/*     */   
/*  91 */   private static final ListenerCallQueue.Event<Service.Listener> TERMINATED_FROM_RUNNING_EVENT = terminatedEvent(Service.State.RUNNING);
/*     */   
/*  93 */   private static final ListenerCallQueue.Event<Service.Listener> TERMINATED_FROM_STOPPING_EVENT = terminatedEvent(Service.State.STOPPING);
/*     */   
/*     */   private static ListenerCallQueue.Event<Service.Listener> terminatedEvent(final Service.State from) {
/*  96 */     return new ListenerCallQueue.Event<Service.Listener>()
/*     */       {
/*     */         public void call(Service.Listener listener) {
/*  99 */           listener.terminated(from);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 104 */           return "terminated({from = " + from + "})";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static ListenerCallQueue.Event<Service.Listener> stoppingEvent(final Service.State from) {
/* 110 */     return new ListenerCallQueue.Event<Service.Listener>()
/*     */       {
/*     */         public void call(Service.Listener listener) {
/* 113 */           listener.stopping(from);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 118 */           return "stopping({from = " + from + "})";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/* 123 */   private final Monitor monitor = new Monitor();
/*     */   
/* 125 */   private final Monitor.Guard isStartable = new IsStartableGuard();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final class IsStartableGuard
/*     */     extends Monitor.Guard
/*     */   {
/*     */     public boolean isSatisfied() {
/* 135 */       return (AbstractService.this.state() == Service.State.NEW);
/*     */     }
/*     */   }
/*     */   
/* 139 */   private final Monitor.Guard isStoppable = new IsStoppableGuard();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final class IsStoppableGuard
/*     */     extends Monitor.Guard
/*     */   {
/*     */     public boolean isSatisfied() {
/* 149 */       return (AbstractService.this.state().compareTo(Service.State.RUNNING) <= 0);
/*     */     }
/*     */   }
/*     */   
/* 153 */   private final Monitor.Guard hasReachedRunning = new HasReachedRunningGuard();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final class HasReachedRunningGuard
/*     */     extends Monitor.Guard
/*     */   {
/*     */     public boolean isSatisfied() {
/* 163 */       return (AbstractService.this.state().compareTo(Service.State.RUNNING) >= 0);
/*     */     }
/*     */   }
/*     */   
/* 167 */   private final Monitor.Guard isStopped = new IsStoppedGuard();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final class IsStoppedGuard
/*     */     extends Monitor.Guard
/*     */   {
/*     */     public boolean isSatisfied() {
/* 177 */       return (AbstractService.this.state().compareTo(Service.State.TERMINATED) >= 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 182 */   private final ListenerCallQueue<Service.Listener> listeners = new ListenerCallQueue<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 193 */   private volatile StateSnapshot snapshot = new StateSnapshot(Service.State.NEW);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ForOverride
/*     */   protected void doCancelStart() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final Service startAsync() {
/* 248 */     if (this.monitor.enterIf(this.isStartable)) {
/*     */       try {
/* 250 */         this.snapshot = new StateSnapshot(Service.State.STARTING);
/* 251 */         enqueueStartingEvent();
/* 252 */         doStart();
/* 253 */       } catch (Throwable startupFailure) {
/* 254 */         Platform.restoreInterruptIfIsInterruptedException(startupFailure);
/* 255 */         notifyFailed(startupFailure);
/*     */       } finally {
/* 257 */         this.monitor.leave();
/* 258 */         dispatchListenerEvents();
/*     */       } 
/*     */     } else {
/* 261 */       throw new IllegalStateException("Service " + this + " has already been started");
/*     */     } 
/* 263 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final Service stopAsync() {
/* 269 */     if (this.monitor.enterIf(this.isStoppable)) {
/*     */       try {
/* 271 */         Service.State previous = state();
/* 272 */         switch (previous) {
/*     */           case NEW:
/* 274 */             this.snapshot = new StateSnapshot(Service.State.TERMINATED);
/* 275 */             enqueueTerminatedEvent(Service.State.NEW);
/*     */             break;
/*     */           case STARTING:
/* 278 */             this.snapshot = new StateSnapshot(Service.State.STARTING, true, null);
/* 279 */             enqueueStoppingEvent(Service.State.STARTING);
/* 280 */             doCancelStart();
/*     */             break;
/*     */           case RUNNING:
/* 283 */             this.snapshot = new StateSnapshot(Service.State.STOPPING);
/* 284 */             enqueueStoppingEvent(Service.State.RUNNING);
/* 285 */             doStop();
/*     */             break;
/*     */           
/*     */           case STOPPING:
/*     */           case TERMINATED:
/*     */           case FAILED:
/* 291 */             throw new AssertionError("isStoppable is incorrectly implemented, saw: " + previous);
/*     */         } 
/* 293 */       } catch (Throwable shutdownFailure) {
/* 294 */         Platform.restoreInterruptIfIsInterruptedException(shutdownFailure);
/* 295 */         notifyFailed(shutdownFailure);
/*     */       } finally {
/* 297 */         this.monitor.leave();
/* 298 */         dispatchListenerEvents();
/*     */       } 
/*     */     }
/* 301 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void awaitRunning() {
/* 306 */     this.monitor.enterWhenUninterruptibly(this.hasReachedRunning);
/*     */     try {
/* 308 */       checkCurrentState(Service.State.RUNNING);
/*     */     } finally {
/* 310 */       this.monitor.leave();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void awaitRunning(Duration timeout) throws TimeoutException {
/* 317 */     super.awaitRunning(timeout);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void awaitRunning(long timeout, TimeUnit unit) throws TimeoutException {
/* 322 */     if (this.monitor.enterWhenUninterruptibly(this.hasReachedRunning, timeout, unit)) {
/*     */       try {
/* 324 */         checkCurrentState(Service.State.RUNNING);
/*     */       } finally {
/* 326 */         this.monitor.leave();
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 333 */       throw new TimeoutException("Timed out waiting for " + this + " to reach the RUNNING state.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void awaitTerminated() {
/* 339 */     this.monitor.enterWhenUninterruptibly(this.isStopped);
/*     */     try {
/* 341 */       checkCurrentState(Service.State.TERMINATED);
/*     */     } finally {
/* 343 */       this.monitor.leave();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void awaitTerminated(Duration timeout) throws TimeoutException {
/* 350 */     super.awaitTerminated(timeout);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void awaitTerminated(long timeout, TimeUnit unit) throws TimeoutException {
/* 355 */     if (this.monitor.enterWhenUninterruptibly(this.isStopped, timeout, unit)) {
/*     */       try {
/* 357 */         checkCurrentState(Service.State.TERMINATED);
/*     */       } finally {
/* 359 */         this.monitor.leave();
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 366 */       throw new TimeoutException("Timed out waiting for " + this + " to reach a terminal state. Current state: " + 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 371 */           state());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @GuardedBy("monitor")
/*     */   private void checkCurrentState(Service.State expected) {
/* 378 */     Service.State actual = state();
/* 379 */     if (actual != expected) {
/* 380 */       if (actual == Service.State.FAILED)
/*     */       {
/* 382 */         throw new IllegalStateException("Expected the service " + this + " to be " + expected + ", but the service has FAILED", 
/*     */             
/* 384 */             failureCause());
/*     */       }
/* 386 */       throw new IllegalStateException("Expected the service " + this + " to be " + expected + ", but was " + actual);
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
/*     */   protected final void notifyStarted() {
/* 398 */     this.monitor.enter();
/*     */ 
/*     */     
/*     */     try {
/* 402 */       if (this.snapshot.state != Service.State.STARTING) {
/* 403 */         IllegalStateException failure = new IllegalStateException("Cannot notifyStarted() when the service is " + this.snapshot.state);
/*     */ 
/*     */         
/* 406 */         notifyFailed(failure);
/* 407 */         throw failure;
/*     */       } 
/*     */       
/* 410 */       if (this.snapshot.shutdownWhenStartupFinishes) {
/* 411 */         this.snapshot = new StateSnapshot(Service.State.STOPPING);
/*     */ 
/*     */         
/* 414 */         doStop();
/*     */       } else {
/* 416 */         this.snapshot = new StateSnapshot(Service.State.RUNNING);
/* 417 */         enqueueRunningEvent();
/*     */       } 
/*     */     } finally {
/* 420 */       this.monitor.leave();
/* 421 */       dispatchListenerEvents();
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
/*     */   protected final void notifyStopped() {
/* 434 */     this.monitor.enter();
/*     */     try {
/* 436 */       Service.State previous = state();
/* 437 */       switch (previous) {
/*     */         case NEW:
/*     */         case TERMINATED:
/*     */         case FAILED:
/* 441 */           throw new IllegalStateException("Cannot notifyStopped() when the service is " + previous);
/*     */         case STARTING:
/*     */         case RUNNING:
/*     */         case STOPPING:
/* 445 */           this.snapshot = new StateSnapshot(Service.State.TERMINATED);
/* 446 */           enqueueTerminatedEvent(previous);
/*     */           break;
/*     */       } 
/*     */     } finally {
/* 450 */       this.monitor.leave();
/* 451 */       dispatchListenerEvents();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void notifyFailed(Throwable cause) {
/* 461 */     Preconditions.checkNotNull(cause);
/*     */     
/* 463 */     this.monitor.enter();
/*     */     try {
/* 465 */       Service.State previous = state();
/* 466 */       switch (previous) {
/*     */         case NEW:
/*     */         case TERMINATED:
/* 469 */           throw new IllegalStateException("Failed while in state:" + previous, cause);
/*     */         case STARTING:
/*     */         case RUNNING:
/*     */         case STOPPING:
/* 473 */           this.snapshot = new StateSnapshot(Service.State.FAILED, false, cause);
/* 474 */           enqueueFailedEvent(previous, cause);
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */     
/*     */     } finally {
/* 481 */       this.monitor.leave();
/* 482 */       dispatchListenerEvents();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isRunning() {
/* 488 */     return (state() == Service.State.RUNNING);
/*     */   }
/*     */ 
/*     */   
/*     */   public final Service.State state() {
/* 493 */     return this.snapshot.externalState();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Throwable failureCause() {
/* 499 */     return this.snapshot.failureCause();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void addListener(Service.Listener listener, Executor executor) {
/* 505 */     this.listeners.addListener(listener, executor);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 510 */     return getClass().getSimpleName() + " [" + state() + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void dispatchListenerEvents() {
/* 518 */     if (!this.monitor.isOccupiedByCurrentThread()) {
/* 519 */       this.listeners.dispatch();
/*     */     }
/*     */   }
/*     */   
/*     */   private void enqueueStartingEvent() {
/* 524 */     this.listeners.enqueue(STARTING_EVENT);
/*     */   }
/*     */   
/*     */   private void enqueueRunningEvent() {
/* 528 */     this.listeners.enqueue(RUNNING_EVENT);
/*     */   }
/*     */   
/*     */   private void enqueueStoppingEvent(Service.State from) {
/* 532 */     if (from == Service.State.STARTING) {
/* 533 */       this.listeners.enqueue(STOPPING_FROM_STARTING_EVENT);
/* 534 */     } else if (from == Service.State.RUNNING) {
/* 535 */       this.listeners.enqueue(STOPPING_FROM_RUNNING_EVENT);
/*     */     } else {
/* 537 */       throw new AssertionError();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void enqueueTerminatedEvent(Service.State from) {
/* 542 */     switch (from) {
/*     */       case NEW:
/* 544 */         this.listeners.enqueue(TERMINATED_FROM_NEW_EVENT);
/*     */         break;
/*     */       case STARTING:
/* 547 */         this.listeners.enqueue(TERMINATED_FROM_STARTING_EVENT);
/*     */         break;
/*     */       case RUNNING:
/* 550 */         this.listeners.enqueue(TERMINATED_FROM_RUNNING_EVENT);
/*     */         break;
/*     */       case STOPPING:
/* 553 */         this.listeners.enqueue(TERMINATED_FROM_STOPPING_EVENT);
/*     */         break;
/*     */       case TERMINATED:
/*     */       case FAILED:
/* 557 */         throw new AssertionError();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void enqueueFailedEvent(final Service.State from, final Throwable cause) {
/* 563 */     this.listeners.enqueue(new ListenerCallQueue.Event<Service.Listener>(this)
/*     */         {
/*     */           public void call(Service.Listener listener)
/*     */           {
/* 567 */             listener.failed(from, cause);
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/* 572 */             return "failed({from = " + from + ", cause = " + cause + "})";
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @ForOverride
/*     */   protected abstract void doStart();
/*     */ 
/*     */ 
/*     */   
/*     */   @ForOverride
/*     */   protected abstract void doStop();
/*     */ 
/*     */   
/*     */   private static final class StateSnapshot
/*     */   {
/*     */     final Service.State state;
/*     */     
/*     */     final boolean shutdownWhenStartupFinishes;
/*     */     
/*     */     @CheckForNull
/*     */     final Throwable failure;
/*     */ 
/*     */     
/*     */     StateSnapshot(Service.State internalState) {
/* 599 */       this(internalState, false, null);
/*     */     }
/*     */ 
/*     */     
/*     */     StateSnapshot(Service.State internalState, boolean shutdownWhenStartupFinishes, @CheckForNull Throwable failure) {
/* 604 */       Preconditions.checkArgument((!shutdownWhenStartupFinishes || internalState == Service.State.STARTING), "shutdownWhenStartupFinishes can only be set if state is STARTING. Got %s instead.", internalState);
/*     */ 
/*     */ 
/*     */       
/* 608 */       Preconditions.checkArgument((((failure != null) ? true : false) == ((internalState == Service.State.FAILED) ? true : false)), "A failure cause should be set if and only if the state is failed.  Got %s and %s instead.", internalState, failure);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 614 */       this.state = internalState;
/* 615 */       this.shutdownWhenStartupFinishes = shutdownWhenStartupFinishes;
/* 616 */       this.failure = failure;
/*     */     }
/*     */ 
/*     */     
/*     */     Service.State externalState() {
/* 621 */       if (this.shutdownWhenStartupFinishes && this.state == Service.State.STARTING) {
/* 622 */         return Service.State.STOPPING;
/*     */       }
/* 624 */       return this.state;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Throwable failureCause() {
/* 630 */       Preconditions.checkState((this.state == Service.State.FAILED), "failureCause() is only valid if the service has failed, service is %s", this.state);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 635 */       return Objects.<Throwable>requireNonNull(this.failure);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/AbstractService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */