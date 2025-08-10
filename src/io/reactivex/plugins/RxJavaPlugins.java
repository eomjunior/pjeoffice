/*      */ package io.reactivex.plugins;
/*      */ 
/*      */ import io.reactivex.Completable;
/*      */ import io.reactivex.CompletableObserver;
/*      */ import io.reactivex.Flowable;
/*      */ import io.reactivex.Maybe;
/*      */ import io.reactivex.MaybeObserver;
/*      */ import io.reactivex.Observable;
/*      */ import io.reactivex.Observer;
/*      */ import io.reactivex.Scheduler;
/*      */ import io.reactivex.Single;
/*      */ import io.reactivex.SingleObserver;
/*      */ import io.reactivex.annotations.NonNull;
/*      */ import io.reactivex.annotations.Nullable;
/*      */ import io.reactivex.exceptions.UndeliverableException;
/*      */ import io.reactivex.flowables.ConnectableFlowable;
/*      */ import io.reactivex.functions.BiFunction;
/*      */ import io.reactivex.functions.BooleanSupplier;
/*      */ import io.reactivex.functions.Consumer;
/*      */ import io.reactivex.functions.Function;
/*      */ import io.reactivex.internal.functions.ObjectHelper;
/*      */ import io.reactivex.internal.schedulers.ComputationScheduler;
/*      */ import io.reactivex.internal.schedulers.IoScheduler;
/*      */ import io.reactivex.internal.schedulers.NewThreadScheduler;
/*      */ import io.reactivex.internal.schedulers.SingleScheduler;
/*      */ import io.reactivex.internal.util.ExceptionHelper;
/*      */ import io.reactivex.observables.ConnectableObservable;
/*      */ import io.reactivex.parallel.ParallelFlowable;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.concurrent.ThreadFactory;
/*      */ import org.reactivestreams.Subscriber;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class RxJavaPlugins
/*      */ {
/*      */   @Nullable
/*      */   static volatile Consumer<? super Throwable> errorHandler;
/*      */   @Nullable
/*      */   static volatile Function<? super Runnable, ? extends Runnable> onScheduleHandler;
/*      */   @Nullable
/*      */   static volatile Function<? super Callable<Scheduler>, ? extends Scheduler> onInitComputationHandler;
/*      */   @Nullable
/*      */   static volatile Function<? super Callable<Scheduler>, ? extends Scheduler> onInitSingleHandler;
/*      */   @Nullable
/*      */   static volatile Function<? super Callable<Scheduler>, ? extends Scheduler> onInitIoHandler;
/*      */   @Nullable
/*      */   static volatile Function<? super Callable<Scheduler>, ? extends Scheduler> onInitNewThreadHandler;
/*      */   @Nullable
/*      */   static volatile Function<? super Scheduler, ? extends Scheduler> onComputationHandler;
/*      */   @Nullable
/*      */   static volatile Function<? super Scheduler, ? extends Scheduler> onSingleHandler;
/*      */   @Nullable
/*      */   static volatile Function<? super Scheduler, ? extends Scheduler> onIoHandler;
/*      */   @Nullable
/*      */   static volatile Function<? super Scheduler, ? extends Scheduler> onNewThreadHandler;
/*      */   @Nullable
/*      */   static volatile Function<? super Flowable, ? extends Flowable> onFlowableAssembly;
/*      */   @Nullable
/*      */   static volatile Function<? super ConnectableFlowable, ? extends ConnectableFlowable> onConnectableFlowableAssembly;
/*      */   @Nullable
/*      */   static volatile Function<? super Observable, ? extends Observable> onObservableAssembly;
/*      */   @Nullable
/*      */   static volatile Function<? super ConnectableObservable, ? extends ConnectableObservable> onConnectableObservableAssembly;
/*      */   @Nullable
/*      */   static volatile Function<? super Maybe, ? extends Maybe> onMaybeAssembly;
/*      */   @Nullable
/*      */   static volatile Function<? super Single, ? extends Single> onSingleAssembly;
/*      */   @Nullable
/*      */   static volatile Function<? super Completable, ? extends Completable> onCompletableAssembly;
/*      */   @Nullable
/*      */   static volatile Function<? super ParallelFlowable, ? extends ParallelFlowable> onParallelAssembly;
/*      */   @Nullable
/*      */   static volatile BiFunction<? super Flowable, ? super Subscriber, ? extends Subscriber> onFlowableSubscribe;
/*      */   @Nullable
/*      */   static volatile BiFunction<? super Maybe, ? super MaybeObserver, ? extends MaybeObserver> onMaybeSubscribe;
/*      */   @Nullable
/*      */   static volatile BiFunction<? super Observable, ? super Observer, ? extends Observer> onObservableSubscribe;
/*      */   @Nullable
/*      */   static volatile BiFunction<? super Single, ? super SingleObserver, ? extends SingleObserver> onSingleSubscribe;
/*      */   @Nullable
/*      */   static volatile BiFunction<? super Completable, ? super CompletableObserver, ? extends CompletableObserver> onCompletableSubscribe;
/*      */   @Nullable
/*      */   static volatile BooleanSupplier onBeforeBlocking;
/*      */   static volatile boolean lockdown;
/*      */   static volatile boolean failNonBlockingScheduler;
/*      */   
/*      */   public static void lockdown() {
/*  133 */     lockdown = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isLockdown() {
/*  141 */     return lockdown;
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
/*      */   public static void setFailOnNonBlockingScheduler(boolean enable) {
/*  153 */     if (lockdown) {
/*  154 */       throw new IllegalStateException("Plugins can't be changed anymore");
/*      */     }
/*  156 */     failNonBlockingScheduler = enable;
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
/*      */   public static boolean isFailOnNonBlockingScheduler() {
/*  168 */     return failNonBlockingScheduler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Function<? super Scheduler, ? extends Scheduler> getComputationSchedulerHandler() {
/*  177 */     return onComputationHandler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Consumer<? super Throwable> getErrorHandler() {
/*  186 */     return errorHandler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Function<? super Callable<Scheduler>, ? extends Scheduler> getInitComputationSchedulerHandler() {
/*  195 */     return onInitComputationHandler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Function<? super Callable<Scheduler>, ? extends Scheduler> getInitIoSchedulerHandler() {
/*  204 */     return onInitIoHandler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Function<? super Callable<Scheduler>, ? extends Scheduler> getInitNewThreadSchedulerHandler() {
/*  213 */     return onInitNewThreadHandler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Function<? super Callable<Scheduler>, ? extends Scheduler> getInitSingleSchedulerHandler() {
/*  222 */     return onInitSingleHandler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Function<? super Scheduler, ? extends Scheduler> getIoSchedulerHandler() {
/*  231 */     return onIoHandler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Function<? super Scheduler, ? extends Scheduler> getNewThreadSchedulerHandler() {
/*  240 */     return onNewThreadHandler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Function<? super Runnable, ? extends Runnable> getScheduleHandler() {
/*  249 */     return onScheduleHandler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Function<? super Scheduler, ? extends Scheduler> getSingleSchedulerHandler() {
/*  258 */     return onSingleHandler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NonNull
/*      */   public static Scheduler initComputationScheduler(@NonNull Callable<Scheduler> defaultScheduler) {
/*  269 */     ObjectHelper.requireNonNull(defaultScheduler, "Scheduler Callable can't be null");
/*  270 */     Function<? super Callable<Scheduler>, ? extends Scheduler> f = onInitComputationHandler;
/*  271 */     if (f == null) {
/*  272 */       return callRequireNonNull(defaultScheduler);
/*      */     }
/*  274 */     return applyRequireNonNull(f, defaultScheduler);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NonNull
/*      */   public static Scheduler initIoScheduler(@NonNull Callable<Scheduler> defaultScheduler) {
/*  285 */     ObjectHelper.requireNonNull(defaultScheduler, "Scheduler Callable can't be null");
/*  286 */     Function<? super Callable<Scheduler>, ? extends Scheduler> f = onInitIoHandler;
/*  287 */     if (f == null) {
/*  288 */       return callRequireNonNull(defaultScheduler);
/*      */     }
/*  290 */     return applyRequireNonNull(f, defaultScheduler);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NonNull
/*      */   public static Scheduler initNewThreadScheduler(@NonNull Callable<Scheduler> defaultScheduler) {
/*  301 */     ObjectHelper.requireNonNull(defaultScheduler, "Scheduler Callable can't be null");
/*  302 */     Function<? super Callable<Scheduler>, ? extends Scheduler> f = onInitNewThreadHandler;
/*  303 */     if (f == null) {
/*  304 */       return callRequireNonNull(defaultScheduler);
/*      */     }
/*  306 */     return applyRequireNonNull(f, defaultScheduler);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NonNull
/*      */   public static Scheduler initSingleScheduler(@NonNull Callable<Scheduler> defaultScheduler) {
/*  317 */     ObjectHelper.requireNonNull(defaultScheduler, "Scheduler Callable can't be null");
/*  318 */     Function<? super Callable<Scheduler>, ? extends Scheduler> f = onInitSingleHandler;
/*  319 */     if (f == null) {
/*  320 */       return callRequireNonNull(defaultScheduler);
/*      */     }
/*  322 */     return applyRequireNonNull(f, defaultScheduler);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NonNull
/*      */   public static Scheduler onComputationScheduler(@NonNull Scheduler defaultScheduler) {
/*  332 */     Function<? super Scheduler, ? extends Scheduler> f = onComputationHandler;
/*  333 */     if (f == null) {
/*  334 */       return defaultScheduler;
/*      */     }
/*  336 */     return apply((Function)f, defaultScheduler);
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
/*      */   public static void onError(@NonNull Throwable error) {
/*      */     UndeliverableException undeliverableException;
/*  361 */     Consumer<? super Throwable> f = errorHandler;
/*      */     
/*  363 */     if (error == null) {
/*  364 */       error = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
/*      */     }
/*  366 */     else if (!isBug(error)) {
/*  367 */       undeliverableException = new UndeliverableException(error);
/*      */     } 
/*      */ 
/*      */     
/*  371 */     if (f != null) {
/*      */       try {
/*  373 */         f.accept(undeliverableException);
/*      */         return;
/*  375 */       } catch (Throwable e) {
/*      */         
/*  377 */         e.printStackTrace();
/*  378 */         uncaught(e);
/*      */       } 
/*      */     }
/*      */     
/*  382 */     undeliverableException.printStackTrace();
/*  383 */     uncaught((Throwable)undeliverableException);
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
/*      */   static boolean isBug(Throwable error) {
/*  396 */     if (error instanceof io.reactivex.exceptions.OnErrorNotImplementedException) {
/*  397 */       return true;
/*      */     }
/*      */ 
/*      */     
/*  401 */     if (error instanceof io.reactivex.exceptions.MissingBackpressureException) {
/*  402 */       return true;
/*      */     }
/*      */ 
/*      */     
/*  406 */     if (error instanceof IllegalStateException) {
/*  407 */       return true;
/*      */     }
/*      */ 
/*      */     
/*  411 */     if (error instanceof NullPointerException) {
/*  412 */       return true;
/*      */     }
/*      */     
/*  415 */     if (error instanceof IllegalArgumentException) {
/*  416 */       return true;
/*      */     }
/*      */     
/*  419 */     if (error instanceof io.reactivex.exceptions.CompositeException) {
/*  420 */       return true;
/*      */     }
/*      */     
/*  423 */     return false;
/*      */   }
/*      */   
/*      */   static void uncaught(@NonNull Throwable error) {
/*  427 */     Thread currentThread = Thread.currentThread();
/*  428 */     Thread.UncaughtExceptionHandler handler = currentThread.getUncaughtExceptionHandler();
/*  429 */     handler.uncaughtException(currentThread, error);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NonNull
/*      */   public static Scheduler onIoScheduler(@NonNull Scheduler defaultScheduler) {
/*  439 */     Function<? super Scheduler, ? extends Scheduler> f = onIoHandler;
/*  440 */     if (f == null) {
/*  441 */       return defaultScheduler;
/*      */     }
/*  443 */     return apply((Function)f, defaultScheduler);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NonNull
/*      */   public static Scheduler onNewThreadScheduler(@NonNull Scheduler defaultScheduler) {
/*  453 */     Function<? super Scheduler, ? extends Scheduler> f = onNewThreadHandler;
/*  454 */     if (f == null) {
/*  455 */       return defaultScheduler;
/*      */     }
/*  457 */     return apply((Function)f, defaultScheduler);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NonNull
/*      */   public static Runnable onSchedule(@NonNull Runnable run) {
/*  467 */     ObjectHelper.requireNonNull(run, "run is null");
/*      */     
/*  469 */     Function<? super Runnable, ? extends Runnable> f = onScheduleHandler;
/*  470 */     if (f == null) {
/*  471 */       return run;
/*      */     }
/*  473 */     return apply((Function)f, run);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NonNull
/*      */   public static Scheduler onSingleScheduler(@NonNull Scheduler defaultScheduler) {
/*  483 */     Function<? super Scheduler, ? extends Scheduler> f = onSingleHandler;
/*  484 */     if (f == null) {
/*  485 */       return defaultScheduler;
/*      */     }
/*  487 */     return apply((Function)f, defaultScheduler);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void reset() {
/*  494 */     setErrorHandler(null);
/*  495 */     setScheduleHandler(null);
/*      */     
/*  497 */     setComputationSchedulerHandler(null);
/*  498 */     setInitComputationSchedulerHandler(null);
/*      */     
/*  500 */     setIoSchedulerHandler(null);
/*  501 */     setInitIoSchedulerHandler(null);
/*      */     
/*  503 */     setSingleSchedulerHandler(null);
/*  504 */     setInitSingleSchedulerHandler(null);
/*      */     
/*  506 */     setNewThreadSchedulerHandler(null);
/*  507 */     setInitNewThreadSchedulerHandler(null);
/*      */     
/*  509 */     setOnFlowableAssembly(null);
/*  510 */     setOnFlowableSubscribe(null);
/*      */     
/*  512 */     setOnObservableAssembly(null);
/*  513 */     setOnObservableSubscribe(null);
/*      */     
/*  515 */     setOnSingleAssembly(null);
/*  516 */     setOnSingleSubscribe(null);
/*      */     
/*  518 */     setOnCompletableAssembly(null);
/*  519 */     setOnCompletableSubscribe(null);
/*      */     
/*  521 */     setOnConnectableFlowableAssembly(null);
/*  522 */     setOnConnectableObservableAssembly(null);
/*      */     
/*  524 */     setOnMaybeAssembly(null);
/*  525 */     setOnMaybeSubscribe(null);
/*      */     
/*  527 */     setOnParallelAssembly(null);
/*      */     
/*  529 */     setFailOnNonBlockingScheduler(false);
/*  530 */     setOnBeforeBlocking(null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setComputationSchedulerHandler(@Nullable Function<? super Scheduler, ? extends Scheduler> handler) {
/*  538 */     if (lockdown) {
/*  539 */       throw new IllegalStateException("Plugins can't be changed anymore");
/*      */     }
/*  541 */     onComputationHandler = handler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setErrorHandler(@Nullable Consumer<? super Throwable> handler) {
/*  549 */     if (lockdown) {
/*  550 */       throw new IllegalStateException("Plugins can't be changed anymore");
/*      */     }
/*  552 */     errorHandler = handler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setInitComputationSchedulerHandler(@Nullable Function<? super Callable<Scheduler>, ? extends Scheduler> handler) {
/*  560 */     if (lockdown) {
/*  561 */       throw new IllegalStateException("Plugins can't be changed anymore");
/*      */     }
/*  563 */     onInitComputationHandler = handler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setInitIoSchedulerHandler(@Nullable Function<? super Callable<Scheduler>, ? extends Scheduler> handler) {
/*  571 */     if (lockdown) {
/*  572 */       throw new IllegalStateException("Plugins can't be changed anymore");
/*      */     }
/*  574 */     onInitIoHandler = handler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setInitNewThreadSchedulerHandler(@Nullable Function<? super Callable<Scheduler>, ? extends Scheduler> handler) {
/*  582 */     if (lockdown) {
/*  583 */       throw new IllegalStateException("Plugins can't be changed anymore");
/*      */     }
/*  585 */     onInitNewThreadHandler = handler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setInitSingleSchedulerHandler(@Nullable Function<? super Callable<Scheduler>, ? extends Scheduler> handler) {
/*  593 */     if (lockdown) {
/*  594 */       throw new IllegalStateException("Plugins can't be changed anymore");
/*      */     }
/*  596 */     onInitSingleHandler = handler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setIoSchedulerHandler(@Nullable Function<? super Scheduler, ? extends Scheduler> handler) {
/*  604 */     if (lockdown) {
/*  605 */       throw new IllegalStateException("Plugins can't be changed anymore");
/*      */     }
/*  607 */     onIoHandler = handler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setNewThreadSchedulerHandler(@Nullable Function<? super Scheduler, ? extends Scheduler> handler) {
/*  615 */     if (lockdown) {
/*  616 */       throw new IllegalStateException("Plugins can't be changed anymore");
/*      */     }
/*  618 */     onNewThreadHandler = handler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setScheduleHandler(@Nullable Function<? super Runnable, ? extends Runnable> handler) {
/*  626 */     if (lockdown) {
/*  627 */       throw new IllegalStateException("Plugins can't be changed anymore");
/*      */     }
/*  629 */     onScheduleHandler = handler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setSingleSchedulerHandler(@Nullable Function<? super Scheduler, ? extends Scheduler> handler) {
/*  637 */     if (lockdown) {
/*  638 */       throw new IllegalStateException("Plugins can't be changed anymore");
/*      */     }
/*  640 */     onSingleHandler = handler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void unlock() {
/*  647 */     lockdown = false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Function<? super Completable, ? extends Completable> getOnCompletableAssembly() {
/*  656 */     return onCompletableAssembly;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static BiFunction<? super Completable, ? super CompletableObserver, ? extends CompletableObserver> getOnCompletableSubscribe() {
/*  665 */     return onCompletableSubscribe;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Function<? super Flowable, ? extends Flowable> getOnFlowableAssembly() {
/*  675 */     return onFlowableAssembly;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Function<? super ConnectableFlowable, ? extends ConnectableFlowable> getOnConnectableFlowableAssembly() {
/*  685 */     return onConnectableFlowableAssembly;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static BiFunction<? super Flowable, ? super Subscriber, ? extends Subscriber> getOnFlowableSubscribe() {
/*  695 */     return onFlowableSubscribe;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static BiFunction<? super Maybe, ? super MaybeObserver, ? extends MaybeObserver> getOnMaybeSubscribe() {
/*  705 */     return onMaybeSubscribe;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Function<? super Maybe, ? extends Maybe> getOnMaybeAssembly() {
/*  715 */     return onMaybeAssembly;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Function<? super Single, ? extends Single> getOnSingleAssembly() {
/*  725 */     return onSingleAssembly;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static BiFunction<? super Single, ? super SingleObserver, ? extends SingleObserver> getOnSingleSubscribe() {
/*  735 */     return onSingleSubscribe;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Function<? super Observable, ? extends Observable> getOnObservableAssembly() {
/*  745 */     return onObservableAssembly;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Function<? super ConnectableObservable, ? extends ConnectableObservable> getOnConnectableObservableAssembly() {
/*  755 */     return onConnectableObservableAssembly;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static BiFunction<? super Observable, ? super Observer, ? extends Observer> getOnObservableSubscribe() {
/*  765 */     return onObservableSubscribe;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setOnCompletableAssembly(@Nullable Function<? super Completable, ? extends Completable> onCompletableAssembly) {
/*  773 */     if (lockdown) {
/*  774 */       throw new IllegalStateException("Plugins can't be changed anymore");
/*      */     }
/*  776 */     RxJavaPlugins.onCompletableAssembly = onCompletableAssembly;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setOnCompletableSubscribe(@Nullable BiFunction<? super Completable, ? super CompletableObserver, ? extends CompletableObserver> onCompletableSubscribe) {
/*  785 */     if (lockdown) {
/*  786 */       throw new IllegalStateException("Plugins can't be changed anymore");
/*      */     }
/*  788 */     RxJavaPlugins.onCompletableSubscribe = onCompletableSubscribe;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setOnFlowableAssembly(@Nullable Function<? super Flowable, ? extends Flowable> onFlowableAssembly) {
/*  797 */     if (lockdown) {
/*  798 */       throw new IllegalStateException("Plugins can't be changed anymore");
/*      */     }
/*  800 */     RxJavaPlugins.onFlowableAssembly = onFlowableAssembly;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setOnMaybeAssembly(@Nullable Function<? super Maybe, ? extends Maybe> onMaybeAssembly) {
/*  809 */     if (lockdown) {
/*  810 */       throw new IllegalStateException("Plugins can't be changed anymore");
/*      */     }
/*  812 */     RxJavaPlugins.onMaybeAssembly = onMaybeAssembly;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setOnConnectableFlowableAssembly(@Nullable Function<? super ConnectableFlowable, ? extends ConnectableFlowable> onConnectableFlowableAssembly) {
/*  821 */     if (lockdown) {
/*  822 */       throw new IllegalStateException("Plugins can't be changed anymore");
/*      */     }
/*  824 */     RxJavaPlugins.onConnectableFlowableAssembly = onConnectableFlowableAssembly;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setOnFlowableSubscribe(@Nullable BiFunction<? super Flowable, ? super Subscriber, ? extends Subscriber> onFlowableSubscribe) {
/*  833 */     if (lockdown) {
/*  834 */       throw new IllegalStateException("Plugins can't be changed anymore");
/*      */     }
/*  836 */     RxJavaPlugins.onFlowableSubscribe = onFlowableSubscribe;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setOnMaybeSubscribe(@Nullable BiFunction<? super Maybe, MaybeObserver, ? extends MaybeObserver> onMaybeSubscribe) {
/*  845 */     if (lockdown) {
/*  846 */       throw new IllegalStateException("Plugins can't be changed anymore");
/*      */     }
/*  848 */     RxJavaPlugins.onMaybeSubscribe = onMaybeSubscribe;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setOnObservableAssembly(@Nullable Function<? super Observable, ? extends Observable> onObservableAssembly) {
/*  857 */     if (lockdown) {
/*  858 */       throw new IllegalStateException("Plugins can't be changed anymore");
/*      */     }
/*  860 */     RxJavaPlugins.onObservableAssembly = onObservableAssembly;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setOnConnectableObservableAssembly(@Nullable Function<? super ConnectableObservable, ? extends ConnectableObservable> onConnectableObservableAssembly) {
/*  869 */     if (lockdown) {
/*  870 */       throw new IllegalStateException("Plugins can't be changed anymore");
/*      */     }
/*  872 */     RxJavaPlugins.onConnectableObservableAssembly = onConnectableObservableAssembly;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setOnObservableSubscribe(@Nullable BiFunction<? super Observable, ? super Observer, ? extends Observer> onObservableSubscribe) {
/*  882 */     if (lockdown) {
/*  883 */       throw new IllegalStateException("Plugins can't be changed anymore");
/*      */     }
/*  885 */     RxJavaPlugins.onObservableSubscribe = onObservableSubscribe;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setOnSingleAssembly(@Nullable Function<? super Single, ? extends Single> onSingleAssembly) {
/*  894 */     if (lockdown) {
/*  895 */       throw new IllegalStateException("Plugins can't be changed anymore");
/*      */     }
/*  897 */     RxJavaPlugins.onSingleAssembly = onSingleAssembly;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setOnSingleSubscribe(@Nullable BiFunction<? super Single, ? super SingleObserver, ? extends SingleObserver> onSingleSubscribe) {
/*  906 */     if (lockdown) {
/*  907 */       throw new IllegalStateException("Plugins can't be changed anymore");
/*      */     }
/*  909 */     RxJavaPlugins.onSingleSubscribe = onSingleSubscribe;
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
/*      */   @NonNull
/*      */   public static <T> Subscriber<? super T> onSubscribe(@NonNull Flowable<T> source, @NonNull Subscriber<? super T> subscriber) {
/*  922 */     BiFunction<? super Flowable, ? super Subscriber, ? extends Subscriber> f = onFlowableSubscribe;
/*  923 */     if (f != null) {
/*  924 */       return apply((BiFunction)f, source, subscriber);
/*      */     }
/*  926 */     return subscriber;
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
/*      */   @NonNull
/*      */   public static <T> Observer<? super T> onSubscribe(@NonNull Observable<T> source, @NonNull Observer<? super T> observer) {
/*  939 */     BiFunction<? super Observable, ? super Observer, ? extends Observer> f = onObservableSubscribe;
/*  940 */     if (f != null) {
/*  941 */       return apply((BiFunction)f, source, observer);
/*      */     }
/*  943 */     return observer;
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
/*      */   @NonNull
/*      */   public static <T> SingleObserver<? super T> onSubscribe(@NonNull Single<T> source, @NonNull SingleObserver<? super T> observer) {
/*  956 */     BiFunction<? super Single, ? super SingleObserver, ? extends SingleObserver> f = onSingleSubscribe;
/*  957 */     if (f != null) {
/*  958 */       return apply((BiFunction)f, source, observer);
/*      */     }
/*  960 */     return observer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NonNull
/*      */   public static CompletableObserver onSubscribe(@NonNull Completable source, @NonNull CompletableObserver observer) {
/*  971 */     BiFunction<? super Completable, ? super CompletableObserver, ? extends CompletableObserver> f = onCompletableSubscribe;
/*  972 */     if (f != null) {
/*  973 */       return apply((BiFunction)f, source, observer);
/*      */     }
/*  975 */     return observer;
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
/*      */   @NonNull
/*      */   public static <T> MaybeObserver<? super T> onSubscribe(@NonNull Maybe<T> source, @NonNull MaybeObserver<? super T> observer) {
/*  988 */     BiFunction<? super Maybe, ? super MaybeObserver, ? extends MaybeObserver> f = onMaybeSubscribe;
/*  989 */     if (f != null) {
/*  990 */       return apply((BiFunction)f, source, observer);
/*      */     }
/*  992 */     return observer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NonNull
/*      */   public static <T> Maybe<T> onAssembly(@NonNull Maybe<T> source) {
/* 1004 */     Function<? super Maybe, ? extends Maybe> f = onMaybeAssembly;
/* 1005 */     if (f != null) {
/* 1006 */       return apply((Function)f, source);
/*      */     }
/* 1008 */     return source;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NonNull
/*      */   public static <T> Flowable<T> onAssembly(@NonNull Flowable<T> source) {
/* 1020 */     Function<? super Flowable, ? extends Flowable> f = onFlowableAssembly;
/* 1021 */     if (f != null) {
/* 1022 */       return apply((Function)f, source);
/*      */     }
/* 1024 */     return source;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NonNull
/*      */   public static <T> ConnectableFlowable<T> onAssembly(@NonNull ConnectableFlowable<T> source) {
/* 1036 */     Function<? super ConnectableFlowable, ? extends ConnectableFlowable> f = onConnectableFlowableAssembly;
/* 1037 */     if (f != null) {
/* 1038 */       return apply((Function)f, source);
/*      */     }
/* 1040 */     return source;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NonNull
/*      */   public static <T> Observable<T> onAssembly(@NonNull Observable<T> source) {
/* 1052 */     Function<? super Observable, ? extends Observable> f = onObservableAssembly;
/* 1053 */     if (f != null) {
/* 1054 */       return apply((Function)f, source);
/*      */     }
/* 1056 */     return source;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NonNull
/*      */   public static <T> ConnectableObservable<T> onAssembly(@NonNull ConnectableObservable<T> source) {
/* 1068 */     Function<? super ConnectableObservable, ? extends ConnectableObservable> f = onConnectableObservableAssembly;
/* 1069 */     if (f != null) {
/* 1070 */       return apply((Function)f, source);
/*      */     }
/* 1072 */     return source;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NonNull
/*      */   public static <T> Single<T> onAssembly(@NonNull Single<T> source) {
/* 1084 */     Function<? super Single, ? extends Single> f = onSingleAssembly;
/* 1085 */     if (f != null) {
/* 1086 */       return apply((Function)f, source);
/*      */     }
/* 1088 */     return source;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @NonNull
/*      */   public static Completable onAssembly(@NonNull Completable source) {
/* 1098 */     Function<? super Completable, ? extends Completable> f = onCompletableAssembly;
/* 1099 */     if (f != null) {
/* 1100 */       return apply((Function)f, source);
/*      */     }
/* 1102 */     return source;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setOnParallelAssembly(@Nullable Function<? super ParallelFlowable, ? extends ParallelFlowable> handler) {
/* 1113 */     if (lockdown) {
/* 1114 */       throw new IllegalStateException("Plugins can't be changed anymore");
/*      */     }
/* 1116 */     onParallelAssembly = handler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Function<? super ParallelFlowable, ? extends ParallelFlowable> getOnParallelAssembly() {
/* 1128 */     return onParallelAssembly;
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
/*      */   @NonNull
/*      */   public static <T> ParallelFlowable<T> onAssembly(@NonNull ParallelFlowable<T> source) {
/* 1142 */     Function<? super ParallelFlowable, ? extends ParallelFlowable> f = onParallelAssembly;
/* 1143 */     if (f != null) {
/* 1144 */       return apply((Function)f, source);
/*      */     }
/* 1146 */     return source;
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
/*      */   public static boolean onBeforeBlocking() {
/* 1160 */     BooleanSupplier f = onBeforeBlocking;
/* 1161 */     if (f != null) {
/*      */       try {
/* 1163 */         return f.getAsBoolean();
/* 1164 */       } catch (Throwable ex) {
/* 1165 */         throw ExceptionHelper.wrapOrThrow(ex);
/*      */       } 
/*      */     }
/* 1168 */     return false;
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
/*      */   public static void setOnBeforeBlocking(@Nullable BooleanSupplier handler) {
/* 1182 */     if (lockdown) {
/* 1183 */       throw new IllegalStateException("Plugins can't be changed anymore");
/*      */     }
/* 1185 */     onBeforeBlocking = handler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static BooleanSupplier getOnBeforeBlocking() {
/* 1197 */     return onBeforeBlocking;
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
/*      */   @NonNull
/*      */   public static Scheduler createComputationScheduler(@NonNull ThreadFactory threadFactory) {
/* 1211 */     return (Scheduler)new ComputationScheduler((ThreadFactory)ObjectHelper.requireNonNull(threadFactory, "threadFactory is null"));
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
/*      */   @NonNull
/*      */   public static Scheduler createIoScheduler(@NonNull ThreadFactory threadFactory) {
/* 1225 */     return (Scheduler)new IoScheduler((ThreadFactory)ObjectHelper.requireNonNull(threadFactory, "threadFactory is null"));
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
/*      */   @NonNull
/*      */   public static Scheduler createNewThreadScheduler(@NonNull ThreadFactory threadFactory) {
/* 1239 */     return (Scheduler)new NewThreadScheduler((ThreadFactory)ObjectHelper.requireNonNull(threadFactory, "threadFactory is null"));
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
/*      */   @NonNull
/*      */   public static Scheduler createSingleScheduler(@NonNull ThreadFactory threadFactory) {
/* 1253 */     return (Scheduler)new SingleScheduler((ThreadFactory)ObjectHelper.requireNonNull(threadFactory, "threadFactory is null"));
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
/*      */   @NonNull
/*      */   static <T, R> R apply(@NonNull Function<T, R> f, @NonNull T t) {
/*      */     try {
/* 1268 */       return (R)f.apply(t);
/* 1269 */     } catch (Throwable ex) {
/* 1270 */       throw ExceptionHelper.wrapOrThrow(ex);
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
/*      */   @NonNull
/*      */   static <T, U, R> R apply(@NonNull BiFunction<T, U, R> f, @NonNull T t, @NonNull U u) {
/*      */     try {
/* 1288 */       return (R)f.apply(t, u);
/* 1289 */     } catch (Throwable ex) {
/* 1290 */       throw ExceptionHelper.wrapOrThrow(ex);
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
/*      */   @NonNull
/*      */   static Scheduler callRequireNonNull(@NonNull Callable<Scheduler> s) {
/*      */     try {
/* 1304 */       return (Scheduler)ObjectHelper.requireNonNull(s.call(), "Scheduler Callable result can't be null");
/* 1305 */     } catch (Throwable ex) {
/* 1306 */       throw ExceptionHelper.wrapOrThrow(ex);
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
/*      */   @NonNull
/*      */   static Scheduler applyRequireNonNull(@NonNull Function<? super Callable<Scheduler>, ? extends Scheduler> f, Callable<Scheduler> s) {
/* 1320 */     return (Scheduler)ObjectHelper.requireNonNull(apply(f, s), "Scheduler Callable result can't be null");
/*      */   }
/*      */ 
/*      */   
/*      */   private RxJavaPlugins() {
/* 1325 */     throw new IllegalStateException("No instances!");
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/plugins/RxJavaPlugins.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */