/*      */ package io.reactivex;
/*      */ 
/*      */ import io.reactivex.annotations.BackpressureKind;
/*      */ import io.reactivex.annotations.BackpressureSupport;
/*      */ import io.reactivex.annotations.CheckReturnValue;
/*      */ import io.reactivex.annotations.Experimental;
/*      */ import io.reactivex.annotations.NonNull;
/*      */ import io.reactivex.annotations.Nullable;
/*      */ import io.reactivex.annotations.SchedulerSupport;
/*      */ import io.reactivex.disposables.Disposable;
/*      */ import io.reactivex.exceptions.Exceptions;
/*      */ import io.reactivex.functions.Action;
/*      */ import io.reactivex.functions.BiPredicate;
/*      */ import io.reactivex.functions.BooleanSupplier;
/*      */ import io.reactivex.functions.Consumer;
/*      */ import io.reactivex.functions.Function;
/*      */ import io.reactivex.functions.Predicate;
/*      */ import io.reactivex.internal.functions.Functions;
/*      */ import io.reactivex.internal.functions.ObjectHelper;
/*      */ import io.reactivex.internal.fuseable.FuseToFlowable;
/*      */ import io.reactivex.internal.fuseable.FuseToMaybe;
/*      */ import io.reactivex.internal.fuseable.FuseToObservable;
/*      */ import io.reactivex.internal.observers.BlockingMultiObserver;
/*      */ import io.reactivex.internal.observers.CallbackCompletableObserver;
/*      */ import io.reactivex.internal.observers.EmptyCompletableObserver;
/*      */ import io.reactivex.internal.operators.completable.CompletableAmb;
/*      */ import io.reactivex.internal.operators.completable.CompletableAndThenCompletable;
/*      */ import io.reactivex.internal.operators.completable.CompletableCache;
/*      */ import io.reactivex.internal.operators.completable.CompletableConcat;
/*      */ import io.reactivex.internal.operators.completable.CompletableConcatArray;
/*      */ import io.reactivex.internal.operators.completable.CompletableConcatIterable;
/*      */ import io.reactivex.internal.operators.completable.CompletableCreate;
/*      */ import io.reactivex.internal.operators.completable.CompletableDefer;
/*      */ import io.reactivex.internal.operators.completable.CompletableDelay;
/*      */ import io.reactivex.internal.operators.completable.CompletableDetach;
/*      */ import io.reactivex.internal.operators.completable.CompletableDisposeOn;
/*      */ import io.reactivex.internal.operators.completable.CompletableDoFinally;
/*      */ import io.reactivex.internal.operators.completable.CompletableDoOnEvent;
/*      */ import io.reactivex.internal.operators.completable.CompletableEmpty;
/*      */ import io.reactivex.internal.operators.completable.CompletableError;
/*      */ import io.reactivex.internal.operators.completable.CompletableErrorSupplier;
/*      */ import io.reactivex.internal.operators.completable.CompletableFromAction;
/*      */ import io.reactivex.internal.operators.completable.CompletableFromCallable;
/*      */ import io.reactivex.internal.operators.completable.CompletableFromObservable;
/*      */ import io.reactivex.internal.operators.completable.CompletableFromPublisher;
/*      */ import io.reactivex.internal.operators.completable.CompletableFromRunnable;
/*      */ import io.reactivex.internal.operators.completable.CompletableFromSingle;
/*      */ import io.reactivex.internal.operators.completable.CompletableFromUnsafeSource;
/*      */ import io.reactivex.internal.operators.completable.CompletableHide;
/*      */ import io.reactivex.internal.operators.completable.CompletableLift;
/*      */ import io.reactivex.internal.operators.completable.CompletableMaterialize;
/*      */ import io.reactivex.internal.operators.completable.CompletableMerge;
/*      */ import io.reactivex.internal.operators.completable.CompletableMergeArray;
/*      */ import io.reactivex.internal.operators.completable.CompletableMergeDelayErrorArray;
/*      */ import io.reactivex.internal.operators.completable.CompletableMergeDelayErrorIterable;
/*      */ import io.reactivex.internal.operators.completable.CompletableMergeIterable;
/*      */ import io.reactivex.internal.operators.completable.CompletableNever;
/*      */ import io.reactivex.internal.operators.completable.CompletableObserveOn;
/*      */ import io.reactivex.internal.operators.completable.CompletableOnErrorComplete;
/*      */ import io.reactivex.internal.operators.completable.CompletablePeek;
/*      */ import io.reactivex.internal.operators.completable.CompletableResumeNext;
/*      */ import io.reactivex.internal.operators.completable.CompletableSubscribeOn;
/*      */ import io.reactivex.internal.operators.completable.CompletableTakeUntilCompletable;
/*      */ import io.reactivex.internal.operators.completable.CompletableTimeout;
/*      */ import io.reactivex.internal.operators.completable.CompletableTimer;
/*      */ import io.reactivex.internal.operators.completable.CompletableToFlowable;
/*      */ import io.reactivex.internal.operators.completable.CompletableToObservable;
/*      */ import io.reactivex.internal.operators.completable.CompletableToSingle;
/*      */ import io.reactivex.internal.operators.completable.CompletableUsing;
/*      */ import io.reactivex.internal.operators.maybe.MaybeDelayWithCompletable;
/*      */ import io.reactivex.internal.operators.maybe.MaybeFromCompletable;
/*      */ import io.reactivex.internal.operators.maybe.MaybeIgnoreElementCompletable;
/*      */ import io.reactivex.internal.operators.mixed.CompletableAndThenObservable;
/*      */ import io.reactivex.internal.operators.mixed.CompletableAndThenPublisher;
/*      */ import io.reactivex.internal.operators.single.SingleDelayWithCompletable;
/*      */ import io.reactivex.internal.util.ExceptionHelper;
/*      */ import io.reactivex.observers.TestObserver;
/*      */ import io.reactivex.plugins.RxJavaPlugins;
/*      */ import io.reactivex.schedulers.Schedulers;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.concurrent.Future;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import org.reactivestreams.Publisher;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class Completable
/*      */   implements CompletableSource
/*      */ {
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static Completable ambArray(CompletableSource... sources) {
/*  124 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  125 */     if (sources.length == 0) {
/*  126 */       return complete();
/*      */     }
/*  128 */     if (sources.length == 1) {
/*  129 */       return wrap(sources[0]);
/*      */     }
/*      */     
/*  132 */     return RxJavaPlugins.onAssembly((Completable)new CompletableAmb(sources, null));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static Completable amb(Iterable<? extends CompletableSource> sources) {
/*  153 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*      */     
/*  155 */     return RxJavaPlugins.onAssembly((Completable)new CompletableAmb(null, sources));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static Completable complete() {
/*  172 */     return RxJavaPlugins.onAssembly(CompletableEmpty.INSTANCE);
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static Completable concatArray(CompletableSource... sources) {
/*  191 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  192 */     if (sources.length == 0) {
/*  193 */       return complete();
/*      */     }
/*  195 */     if (sources.length == 1) {
/*  196 */       return wrap(sources[0]);
/*      */     }
/*  198 */     return RxJavaPlugins.onAssembly((Completable)new CompletableConcatArray(sources));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static Completable concat(Iterable<? extends CompletableSource> sources) {
/*  217 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*      */     
/*  219 */     return RxJavaPlugins.onAssembly((Completable)new CompletableConcatIterable(sources));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   public static Completable concat(Publisher<? extends CompletableSource> sources) {
/*  241 */     return concat(sources, 2);
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   @NonNull
/*      */   public static Completable concat(Publisher<? extends CompletableSource> sources, int prefetch) {
/*  265 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  266 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/*  267 */     return RxJavaPlugins.onAssembly((Completable)new CompletableConcat(sources, prefetch));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static Completable create(CompletableOnSubscribe source) {
/*  309 */     ObjectHelper.requireNonNull(source, "source is null");
/*  310 */     return RxJavaPlugins.onAssembly((Completable)new CompletableCreate(source));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static Completable unsafeCreate(CompletableSource source) {
/*  332 */     ObjectHelper.requireNonNull(source, "source is null");
/*  333 */     if (source instanceof Completable) {
/*  334 */       throw new IllegalArgumentException("Use of unsafeCreate(Completable)!");
/*      */     }
/*  336 */     return RxJavaPlugins.onAssembly((Completable)new CompletableFromUnsafeSource(source));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static Completable defer(Callable<? extends CompletableSource> completableSupplier) {
/*  354 */     ObjectHelper.requireNonNull(completableSupplier, "completableSupplier");
/*  355 */     return RxJavaPlugins.onAssembly((Completable)new CompletableDefer(completableSupplier));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static Completable error(Callable<? extends Throwable> errorSupplier) {
/*  378 */     ObjectHelper.requireNonNull(errorSupplier, "errorSupplier is null");
/*  379 */     return RxJavaPlugins.onAssembly((Completable)new CompletableErrorSupplier(errorSupplier));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static Completable error(Throwable error) {
/*  398 */     ObjectHelper.requireNonNull(error, "error is null");
/*  399 */     return RxJavaPlugins.onAssembly((Completable)new CompletableError(error));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static Completable fromAction(Action run) {
/*  426 */     ObjectHelper.requireNonNull(run, "run is null");
/*  427 */     return RxJavaPlugins.onAssembly((Completable)new CompletableFromAction(run));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static Completable fromCallable(Callable<?> callable) {
/*  453 */     ObjectHelper.requireNonNull(callable, "callable is null");
/*  454 */     return RxJavaPlugins.onAssembly((Completable)new CompletableFromCallable(callable));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static Completable fromFuture(Future<?> future) {
/*  474 */     ObjectHelper.requireNonNull(future, "future is null");
/*  475 */     return fromAction(Functions.futureAction(future));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <T> Completable fromMaybe(MaybeSource<T> maybe) {
/*  499 */     ObjectHelper.requireNonNull(maybe, "maybe is null");
/*  500 */     return RxJavaPlugins.onAssembly((Completable)new MaybeIgnoreElementCompletable(maybe));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static Completable fromRunnable(Runnable run) {
/*  527 */     ObjectHelper.requireNonNull(run, "run is null");
/*  528 */     return RxJavaPlugins.onAssembly((Completable)new CompletableFromRunnable(run));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <T> Completable fromObservable(ObservableSource<T> observable) {
/*  549 */     ObjectHelper.requireNonNull(observable, "observable is null");
/*  550 */     return RxJavaPlugins.onAssembly((Completable)new CompletableFromObservable(observable));
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
/*      */   @CheckReturnValue
/*      */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <T> Completable fromPublisher(Publisher<T> publisher) {
/*  587 */     ObjectHelper.requireNonNull(publisher, "publisher is null");
/*  588 */     return RxJavaPlugins.onAssembly((Completable)new CompletableFromPublisher(publisher));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <T> Completable fromSingle(SingleSource<T> single) {
/*  609 */     ObjectHelper.requireNonNull(single, "single is null");
/*  610 */     return RxJavaPlugins.onAssembly((Completable)new CompletableFromSingle(single));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static Completable mergeArray(CompletableSource... sources) {
/*  644 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  645 */     if (sources.length == 0) {
/*  646 */       return complete();
/*      */     }
/*  648 */     if (sources.length == 1) {
/*  649 */       return wrap(sources[0]);
/*      */     }
/*  651 */     return RxJavaPlugins.onAssembly((Completable)new CompletableMergeArray(sources));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static Completable merge(Iterable<? extends CompletableSource> sources) {
/*  685 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  686 */     return RxJavaPlugins.onAssembly((Completable)new CompletableMergeIterable(sources));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*      */   public static Completable merge(Publisher<? extends CompletableSource> sources) {
/*  723 */     return merge0(sources, 2147483647, false);
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   public static Completable merge(Publisher<? extends CompletableSource> sources, int maxConcurrency) {
/*  762 */     return merge0(sources, maxConcurrency, false);
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   @NonNull
/*      */   private static Completable merge0(Publisher<? extends CompletableSource> sources, int maxConcurrency, boolean delayErrors) {
/*  788 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  789 */     ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
/*  790 */     return RxJavaPlugins.onAssembly((Completable)new CompletableMerge(sources, maxConcurrency, delayErrors));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static Completable mergeArrayDelayError(CompletableSource... sources) {
/*  811 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  812 */     return RxJavaPlugins.onAssembly((Completable)new CompletableMergeDelayErrorArray(sources));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static Completable mergeDelayError(Iterable<? extends CompletableSource> sources) {
/*  833 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  834 */     return RxJavaPlugins.onAssembly((Completable)new CompletableMergeDelayErrorIterable(sources));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*      */   public static Completable mergeDelayError(Publisher<? extends CompletableSource> sources) {
/*  858 */     return merge0(sources, 2147483647, true);
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   public static Completable mergeDelayError(Publisher<? extends CompletableSource> sources, int maxConcurrency) {
/*  884 */     return merge0(sources, maxConcurrency, true);
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public static Completable never() {
/*  900 */     return RxJavaPlugins.onAssembly(CompletableNever.INSTANCE);
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("io.reactivex:computation")
/*      */   public static Completable timer(long delay, TimeUnit unit) {
/*  918 */     return timer(delay, unit, Schedulers.computation());
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("custom")
/*      */   @NonNull
/*      */   public static Completable timer(long delay, TimeUnit unit, Scheduler scheduler) {
/*  939 */     ObjectHelper.requireNonNull(unit, "unit is null");
/*  940 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/*  941 */     return RxJavaPlugins.onAssembly((Completable)new CompletableTimer(delay, unit, scheduler));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static NullPointerException toNpe(Throwable ex) {
/*  950 */     NullPointerException npe = new NullPointerException("Actually not, but can't pass out an exception otherwise...");
/*  951 */     npe.initCause(ex);
/*  952 */     return npe;
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public static <R> Completable using(Callable<R> resourceSupplier, Function<? super R, ? extends CompletableSource> completableFunction, Consumer<? super R> disposer) {
/*  977 */     return using(resourceSupplier, completableFunction, disposer, true);
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <R> Completable using(Callable<R> resourceSupplier, Function<? super R, ? extends CompletableSource> completableFunction, Consumer<? super R> disposer, boolean eager) {
/* 1010 */     ObjectHelper.requireNonNull(resourceSupplier, "resourceSupplier is null");
/* 1011 */     ObjectHelper.requireNonNull(completableFunction, "completableFunction is null");
/* 1012 */     ObjectHelper.requireNonNull(disposer, "disposer is null");
/*      */     
/* 1014 */     return RxJavaPlugins.onAssembly((Completable)new CompletableUsing(resourceSupplier, completableFunction, disposer, eager));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static Completable wrap(CompletableSource source) {
/* 1034 */     ObjectHelper.requireNonNull(source, "source is null");
/* 1035 */     if (source instanceof Completable) {
/* 1036 */       return RxJavaPlugins.onAssembly((Completable)source);
/*      */     }
/* 1038 */     return RxJavaPlugins.onAssembly((Completable)new CompletableFromUnsafeSource(source));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public final Completable ambWith(CompletableSource other) {
/* 1059 */     ObjectHelper.requireNonNull(other, "other is null");
/* 1060 */     return ambArray(new CompletableSource[] { this, other });
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public final <T> Observable<T> andThen(ObservableSource<T> next) {
/* 1083 */     ObjectHelper.requireNonNull(next, "next is null");
/* 1084 */     return RxJavaPlugins.onAssembly((Observable)new CompletableAndThenObservable(this, next));
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
/*      */   @CheckReturnValue
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public final <T> Flowable<T> andThen(Publisher<T> next) {
/* 1111 */     ObjectHelper.requireNonNull(next, "next is null");
/* 1112 */     return RxJavaPlugins.onAssembly((Flowable)new CompletableAndThenPublisher(this, next));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public final <T> Single<T> andThen(SingleSource<T> next) {
/* 1135 */     ObjectHelper.requireNonNull(next, "next is null");
/* 1136 */     return RxJavaPlugins.onAssembly((Single)new SingleDelayWithCompletable(next, this));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public final <T> Maybe<T> andThen(MaybeSource<T> next) {
/* 1159 */     ObjectHelper.requireNonNull(next, "next is null");
/* 1160 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeDelayWithCompletable(next, this));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final Completable andThen(CompletableSource next) {
/* 1181 */     ObjectHelper.requireNonNull(next, "next is null");
/* 1182 */     return RxJavaPlugins.onAssembly((Completable)new CompletableAndThenCompletable(this, next));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final <R> R as(@NonNull CompletableConverter<? extends R> converter) {
/* 1205 */     return ((CompletableConverter<R>)ObjectHelper.requireNonNull(converter, "converter is null")).apply(this);
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
/*      */   @SchedulerSupport("none")
/*      */   public final void blockingAwait() {
/* 1225 */     BlockingMultiObserver<Void> observer = new BlockingMultiObserver();
/* 1226 */     subscribe((CompletableObserver)observer);
/* 1227 */     observer.blockingGet();
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public final boolean blockingAwait(long timeout, TimeUnit unit) {
/* 1253 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 1254 */     BlockingMultiObserver<Void> observer = new BlockingMultiObserver();
/* 1255 */     subscribe((CompletableObserver)observer);
/* 1256 */     return observer.blockingAwait(timeout, unit);
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @Nullable
/*      */   public final Throwable blockingGet() {
/* 1275 */     BlockingMultiObserver<Void> observer = new BlockingMultiObserver();
/* 1276 */     subscribe((CompletableObserver)observer);
/* 1277 */     return observer.blockingGetError();
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @Nullable
/*      */   public final Throwable blockingGet(long timeout, TimeUnit unit) {
/* 1299 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 1300 */     BlockingMultiObserver<Void> observer = new BlockingMultiObserver();
/* 1301 */     subscribe((CompletableObserver)observer);
/* 1302 */     return observer.blockingGetError(timeout, unit);
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final Completable cache() {
/* 1325 */     return RxJavaPlugins.onAssembly((Completable)new CompletableCache(this));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final Completable compose(CompletableTransformer transformer) {
/* 1344 */     return wrap(((CompletableTransformer)ObjectHelper.requireNonNull(transformer, "transformer is null")).apply(this));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public final Completable concatWith(CompletableSource other) {
/* 1367 */     ObjectHelper.requireNonNull(other, "other is null");
/* 1368 */     return RxJavaPlugins.onAssembly((Completable)new CompletableAndThenCompletable(this, other));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("io.reactivex:computation")
/*      */   public final Completable delay(long delay, TimeUnit unit) {
/* 1387 */     return delay(delay, unit, Schedulers.computation(), false);
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("custom")
/*      */   public final Completable delay(long delay, TimeUnit unit, Scheduler scheduler) {
/* 1408 */     return delay(delay, unit, scheduler, false);
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("custom")
/*      */   @NonNull
/*      */   public final Completable delay(long delay, TimeUnit unit, Scheduler scheduler, boolean delayError) {
/* 1431 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 1432 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 1433 */     return RxJavaPlugins.onAssembly((Completable)new CompletableDelay(this, delay, unit, scheduler, delayError));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("io.reactivex:computation")
/*      */   @Experimental
/*      */   public final Completable delaySubscription(long delay, TimeUnit unit) {
/* 1455 */     return delaySubscription(delay, unit, Schedulers.computation());
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("custom")
/*      */   @Experimental
/*      */   public final Completable delaySubscription(long delay, TimeUnit unit, Scheduler scheduler) {
/* 1480 */     return timer(delay, unit, scheduler).andThen(this);
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final Completable doOnComplete(Action onComplete) {
/* 1499 */     return doOnLifecycle(Functions.emptyConsumer(), Functions.emptyConsumer(), onComplete, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final Completable doOnDispose(Action onDispose) {
/* 1520 */     return doOnLifecycle(Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, onDispose);
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final Completable doOnError(Consumer<? super Throwable> onError) {
/* 1541 */     return doOnLifecycle(Functions.emptyConsumer(), onError, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public final Completable doOnEvent(Consumer<? super Throwable> onEvent) {
/* 1563 */     ObjectHelper.requireNonNull(onEvent, "onEvent is null");
/* 1564 */     return RxJavaPlugins.onAssembly((Completable)new CompletableDoOnEvent(this, onEvent));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   private Completable doOnLifecycle(Consumer<? super Disposable> onSubscribe, Consumer<? super Throwable> onError, Action onComplete, Action onTerminate, Action onAfterTerminate, Action onDispose) {
/* 1591 */     ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");
/* 1592 */     ObjectHelper.requireNonNull(onError, "onError is null");
/* 1593 */     ObjectHelper.requireNonNull(onComplete, "onComplete is null");
/* 1594 */     ObjectHelper.requireNonNull(onTerminate, "onTerminate is null");
/* 1595 */     ObjectHelper.requireNonNull(onAfterTerminate, "onAfterTerminate is null");
/* 1596 */     ObjectHelper.requireNonNull(onDispose, "onDispose is null");
/* 1597 */     return RxJavaPlugins.onAssembly((Completable)new CompletablePeek(this, onSubscribe, onError, onComplete, onTerminate, onAfterTerminate, onDispose));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final Completable doOnSubscribe(Consumer<? super Disposable> onSubscribe) {
/* 1616 */     return doOnLifecycle(onSubscribe, Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final Completable doOnTerminate(Action onTerminate) {
/* 1637 */     return doOnLifecycle(Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, onTerminate, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final Completable doAfterTerminate(Action onAfterTerminate) {
/* 1658 */     return doOnLifecycle(
/* 1659 */         Functions.emptyConsumer(), 
/* 1660 */         Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, onAfterTerminate, Functions.EMPTY_ACTION);
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public final Completable doFinally(Action onFinally) {
/* 1690 */     ObjectHelper.requireNonNull(onFinally, "onFinally is null");
/* 1691 */     return RxJavaPlugins.onAssembly((Completable)new CompletableDoFinally(this, onFinally));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public final Completable lift(CompletableOperator onLift) {
/* 1828 */     ObjectHelper.requireNonNull(onLift, "onLift is null");
/* 1829 */     return RxJavaPlugins.onAssembly((Completable)new CompletableLift(this, onLift));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @Experimental
/*      */   public final <T> Single<Notification<T>> materialize() {
/* 1850 */     return RxJavaPlugins.onAssembly((Single)new CompletableMaterialize(this));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public final Completable mergeWith(CompletableSource other) {
/* 1870 */     ObjectHelper.requireNonNull(other, "other is null");
/* 1871 */     return mergeArray(new CompletableSource[] { this, other });
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("custom")
/*      */   @NonNull
/*      */   public final Completable observeOn(Scheduler scheduler) {
/* 1890 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 1891 */     return RxJavaPlugins.onAssembly((Completable)new CompletableObserveOn(this, scheduler));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final Completable onErrorComplete() {
/* 1908 */     return onErrorComplete(Functions.alwaysTrue());
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public final Completable onErrorComplete(Predicate<? super Throwable> predicate) {
/* 1928 */     ObjectHelper.requireNonNull(predicate, "predicate is null");
/*      */     
/* 1930 */     return RxJavaPlugins.onAssembly((Completable)new CompletableOnErrorComplete(this, predicate));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public final Completable onErrorResumeNext(Function<? super Throwable, ? extends CompletableSource> errorMapper) {
/* 1951 */     ObjectHelper.requireNonNull(errorMapper, "errorMapper is null");
/* 1952 */     return RxJavaPlugins.onAssembly((Completable)new CompletableResumeNext(this, errorMapper));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final Completable onTerminateDetach() {
/* 1972 */     return RxJavaPlugins.onAssembly((Completable)new CompletableDetach(this));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final Completable repeat() {
/* 1988 */     return fromPublisher(toFlowable().repeat());
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final Completable repeat(long times) {
/* 2006 */     return fromPublisher(toFlowable().repeat(times));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final Completable repeatUntil(BooleanSupplier stop) {
/* 2025 */     return fromPublisher(toFlowable().repeatUntil(stop));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final Completable repeatWhen(Function<? super Flowable<Object>, ? extends Publisher<?>> handler) {
/* 2046 */     return fromPublisher(toFlowable().repeatWhen(handler));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final Completable retry() {
/* 2062 */     return fromPublisher(toFlowable().retry());
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final Completable retry(BiPredicate<? super Integer, ? super Throwable> predicate) {
/* 2081 */     return fromPublisher(toFlowable().retry(predicate));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final Completable retry(long times) {
/* 2100 */     return fromPublisher(toFlowable().retry(times));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final Completable retry(long times, Predicate<? super Throwable> predicate) {
/* 2124 */     return fromPublisher(toFlowable().retry(times, predicate));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final Completable retry(Predicate<? super Throwable> predicate) {
/* 2144 */     return fromPublisher(toFlowable().retry(predicate));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final Completable retryWhen(Function<? super Flowable<Throwable>, ? extends Publisher<?>> handler) {
/* 2190 */     return fromPublisher(toFlowable().retryWhen(handler));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public final Completable startWith(CompletableSource other) {
/* 2210 */     ObjectHelper.requireNonNull(other, "other is null");
/* 2211 */     return concatArray(new CompletableSource[] { other, this });
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public final <T> Observable<T> startWith(Observable<T> other) {
/* 2232 */     ObjectHelper.requireNonNull(other, "other is null");
/* 2233 */     return other.concatWith(toObservable());
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
/*      */   @CheckReturnValue
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public final <T> Flowable<T> startWith(Publisher<T> other) {
/* 2257 */     ObjectHelper.requireNonNull(other, "other is null");
/* 2258 */     return toFlowable().startWith(other);
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final Completable hide() {
/* 2278 */     return RxJavaPlugins.onAssembly((Completable)new CompletableHide(this));
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
/*      */   @SchedulerSupport("none")
/*      */   public final Disposable subscribe() {
/* 2294 */     EmptyCompletableObserver observer = new EmptyCompletableObserver();
/* 2295 */     subscribe((CompletableObserver)observer);
/* 2296 */     return (Disposable)observer;
/*      */   }
/*      */ 
/*      */   
/*      */   @SchedulerSupport("none")
/*      */   public final void subscribe(CompletableObserver observer) {
/* 2302 */     ObjectHelper.requireNonNull(observer, "observer is null");
/*      */     
/*      */     try {
/* 2305 */       observer = RxJavaPlugins.onSubscribe(this, observer);
/*      */       
/* 2307 */       ObjectHelper.requireNonNull(observer, "The RxJavaPlugins.onSubscribe hook returned a null CompletableObserver. Please check the handler provided to RxJavaPlugins.setOnCompletableSubscribe for invalid null returns. Further reading: https://github.com/ReactiveX/RxJava/wiki/Plugins");
/*      */       
/* 2309 */       subscribeActual(observer);
/* 2310 */     } catch (NullPointerException ex) {
/* 2311 */       throw ex;
/* 2312 */     } catch (Throwable ex) {
/* 2313 */       Exceptions.throwIfFatal(ex);
/* 2314 */       RxJavaPlugins.onError(ex);
/* 2315 */       throw toNpe(ex);
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
/*      */   protected abstract void subscribeActual(CompletableObserver paramCompletableObserver);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final <E extends CompletableObserver> E subscribeWith(E observer) {
/* 2358 */     subscribe((CompletableObserver)observer);
/* 2359 */     return observer;
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public final Disposable subscribe(Action onComplete, Consumer<? super Throwable> onError) {
/* 2379 */     ObjectHelper.requireNonNull(onError, "onError is null");
/* 2380 */     ObjectHelper.requireNonNull(onComplete, "onComplete is null");
/*      */     
/* 2382 */     CallbackCompletableObserver observer = new CallbackCompletableObserver(onError, onComplete);
/* 2383 */     subscribe((CompletableObserver)observer);
/* 2384 */     return (Disposable)observer;
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public final Disposable subscribe(Action onComplete) {
/* 2407 */     ObjectHelper.requireNonNull(onComplete, "onComplete is null");
/*      */     
/* 2409 */     CallbackCompletableObserver observer = new CallbackCompletableObserver(onComplete);
/* 2410 */     subscribe((CompletableObserver)observer);
/* 2411 */     return (Disposable)observer;
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("custom")
/*      */   @NonNull
/*      */   public final Completable subscribeOn(Scheduler scheduler) {
/* 2431 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/*      */     
/* 2433 */     return RxJavaPlugins.onAssembly((Completable)new CompletableSubscribeOn(this, scheduler));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public final Completable takeUntil(CompletableSource other) {
/* 2458 */     ObjectHelper.requireNonNull(other, "other is null");
/*      */     
/* 2460 */     return RxJavaPlugins.onAssembly((Completable)new CompletableTakeUntilCompletable(this, other));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("io.reactivex:computation")
/*      */   public final Completable timeout(long timeout, TimeUnit unit) {
/* 2480 */     return timeout0(timeout, unit, Schedulers.computation(), null);
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("io.reactivex:computation")
/*      */   @NonNull
/*      */   public final Completable timeout(long timeout, TimeUnit unit, CompletableSource other) {
/* 2503 */     ObjectHelper.requireNonNull(other, "other is null");
/* 2504 */     return timeout0(timeout, unit, Schedulers.computation(), other);
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("custom")
/*      */   public final Completable timeout(long timeout, TimeUnit unit, Scheduler scheduler) {
/* 2526 */     return timeout0(timeout, unit, scheduler, null);
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("custom")
/*      */   @NonNull
/*      */   public final Completable timeout(long timeout, TimeUnit unit, Scheduler scheduler, CompletableSource other) {
/* 2551 */     ObjectHelper.requireNonNull(other, "other is null");
/* 2552 */     return timeout0(timeout, unit, scheduler, other);
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("custom")
/*      */   @NonNull
/*      */   private Completable timeout0(long timeout, TimeUnit unit, Scheduler scheduler, CompletableSource other) {
/* 2575 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 2576 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 2577 */     return RxJavaPlugins.onAssembly((Completable)new CompletableTimeout(this, timeout, unit, scheduler, other));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final <U> U to(Function<? super Completable, U> converter) {
/*      */     try {
/* 2597 */       return (U)((Function)ObjectHelper.requireNonNull(converter, "converter is null")).apply(this);
/* 2598 */     } catch (Throwable ex) {
/* 2599 */       Exceptions.throwIfFatal(ex);
/* 2600 */       throw ExceptionHelper.wrapOrThrow(ex);
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
/*      */   @CheckReturnValue
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   @SchedulerSupport("none")
/*      */   public final <T> Flowable<T> toFlowable() {
/* 2623 */     if (this instanceof FuseToFlowable) {
/* 2624 */       return ((FuseToFlowable)this).fuseToFlowable();
/*      */     }
/* 2626 */     return RxJavaPlugins.onAssembly((Flowable)new CompletableToFlowable(this));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final <T> Maybe<T> toMaybe() {
/* 2646 */     if (this instanceof FuseToMaybe) {
/* 2647 */       return ((FuseToMaybe)this).fuseToMaybe();
/*      */     }
/* 2649 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeFromCompletable(this));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final <T> Observable<T> toObservable() {
/* 2668 */     if (this instanceof FuseToObservable) {
/* 2669 */       return ((FuseToObservable)this).fuseToObservable();
/*      */     }
/* 2671 */     return RxJavaPlugins.onAssembly((Observable)new CompletableToObservable(this));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public final <T> Single<T> toSingle(Callable<? extends T> completionValueSupplier) {
/* 2692 */     ObjectHelper.requireNonNull(completionValueSupplier, "completionValueSupplier is null");
/* 2693 */     return RxJavaPlugins.onAssembly((Single)new CompletableToSingle(this, completionValueSupplier, null));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public final <T> Single<T> toSingleDefault(T completionValue) {
/* 2714 */     ObjectHelper.requireNonNull(completionValue, "completionValue is null");
/* 2715 */     return RxJavaPlugins.onAssembly((Single)new CompletableToSingle(this, null, completionValue));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("custom")
/*      */   @NonNull
/*      */   public final Completable unsubscribeOn(Scheduler scheduler) {
/* 2735 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 2736 */     return RxJavaPlugins.onAssembly((Completable)new CompletableDisposeOn(this, scheduler));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final TestObserver<Void> test() {
/* 2757 */     TestObserver<Void> to = new TestObserver();
/* 2758 */     subscribe((CompletableObserver)to);
/* 2759 */     return to;
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final TestObserver<Void> test(boolean cancelled) {
/* 2778 */     TestObserver<Void> to = new TestObserver();
/*      */     
/* 2780 */     if (cancelled) {
/* 2781 */       to.cancel();
/*      */     }
/* 2783 */     subscribe((CompletableObserver)to);
/* 2784 */     return to;
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/Completable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */