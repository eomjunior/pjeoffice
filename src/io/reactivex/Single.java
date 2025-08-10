/*      */ package io.reactivex;
/*      */ 
/*      */ import io.reactivex.annotations.BackpressureKind;
/*      */ import io.reactivex.annotations.BackpressureSupport;
/*      */ import io.reactivex.annotations.CheckReturnValue;
/*      */ import io.reactivex.annotations.Experimental;
/*      */ import io.reactivex.annotations.NonNull;
/*      */ import io.reactivex.annotations.SchedulerSupport;
/*      */ import io.reactivex.disposables.Disposable;
/*      */ import io.reactivex.exceptions.Exceptions;
/*      */ import io.reactivex.functions.Action;
/*      */ import io.reactivex.functions.BiConsumer;
/*      */ import io.reactivex.functions.BiFunction;
/*      */ import io.reactivex.functions.BiPredicate;
/*      */ import io.reactivex.functions.BooleanSupplier;
/*      */ import io.reactivex.functions.Consumer;
/*      */ import io.reactivex.functions.Function;
/*      */ import io.reactivex.functions.Function3;
/*      */ import io.reactivex.functions.Function4;
/*      */ import io.reactivex.functions.Function5;
/*      */ import io.reactivex.functions.Function6;
/*      */ import io.reactivex.functions.Function7;
/*      */ import io.reactivex.functions.Function8;
/*      */ import io.reactivex.functions.Function9;
/*      */ import io.reactivex.functions.Predicate;
/*      */ import io.reactivex.internal.functions.Functions;
/*      */ import io.reactivex.internal.functions.ObjectHelper;
/*      */ import io.reactivex.internal.fuseable.FuseToFlowable;
/*      */ import io.reactivex.internal.fuseable.FuseToMaybe;
/*      */ import io.reactivex.internal.fuseable.FuseToObservable;
/*      */ import io.reactivex.internal.observers.BiConsumerSingleObserver;
/*      */ import io.reactivex.internal.observers.BlockingMultiObserver;
/*      */ import io.reactivex.internal.observers.ConsumerSingleObserver;
/*      */ import io.reactivex.internal.observers.FutureSingleObserver;
/*      */ import io.reactivex.internal.operators.completable.CompletableFromSingle;
/*      */ import io.reactivex.internal.operators.completable.CompletableToFlowable;
/*      */ import io.reactivex.internal.operators.flowable.FlowableConcatMap;
/*      */ import io.reactivex.internal.operators.flowable.FlowableConcatMapPublisher;
/*      */ import io.reactivex.internal.operators.flowable.FlowableFlatMapPublisher;
/*      */ import io.reactivex.internal.operators.flowable.FlowableSingleSingle;
/*      */ import io.reactivex.internal.operators.maybe.MaybeFilterSingle;
/*      */ import io.reactivex.internal.operators.maybe.MaybeFromSingle;
/*      */ import io.reactivex.internal.operators.mixed.SingleFlatMapObservable;
/*      */ import io.reactivex.internal.operators.observable.ObservableConcatMap;
/*      */ import io.reactivex.internal.operators.observable.ObservableSingleSingle;
/*      */ import io.reactivex.internal.operators.single.SingleAmb;
/*      */ import io.reactivex.internal.operators.single.SingleCache;
/*      */ import io.reactivex.internal.operators.single.SingleContains;
/*      */ import io.reactivex.internal.operators.single.SingleCreate;
/*      */ import io.reactivex.internal.operators.single.SingleDefer;
/*      */ import io.reactivex.internal.operators.single.SingleDelay;
/*      */ import io.reactivex.internal.operators.single.SingleDelayWithCompletable;
/*      */ import io.reactivex.internal.operators.single.SingleDelayWithObservable;
/*      */ import io.reactivex.internal.operators.single.SingleDelayWithPublisher;
/*      */ import io.reactivex.internal.operators.single.SingleDelayWithSingle;
/*      */ import io.reactivex.internal.operators.single.SingleDematerialize;
/*      */ import io.reactivex.internal.operators.single.SingleDetach;
/*      */ import io.reactivex.internal.operators.single.SingleDoAfterSuccess;
/*      */ import io.reactivex.internal.operators.single.SingleDoAfterTerminate;
/*      */ import io.reactivex.internal.operators.single.SingleDoFinally;
/*      */ import io.reactivex.internal.operators.single.SingleDoOnDispose;
/*      */ import io.reactivex.internal.operators.single.SingleDoOnError;
/*      */ import io.reactivex.internal.operators.single.SingleDoOnEvent;
/*      */ import io.reactivex.internal.operators.single.SingleDoOnSubscribe;
/*      */ import io.reactivex.internal.operators.single.SingleDoOnSuccess;
/*      */ import io.reactivex.internal.operators.single.SingleDoOnTerminate;
/*      */ import io.reactivex.internal.operators.single.SingleEquals;
/*      */ import io.reactivex.internal.operators.single.SingleError;
/*      */ import io.reactivex.internal.operators.single.SingleFlatMap;
/*      */ import io.reactivex.internal.operators.single.SingleFlatMapCompletable;
/*      */ import io.reactivex.internal.operators.single.SingleFlatMapIterableFlowable;
/*      */ import io.reactivex.internal.operators.single.SingleFlatMapIterableObservable;
/*      */ import io.reactivex.internal.operators.single.SingleFlatMapMaybe;
/*      */ import io.reactivex.internal.operators.single.SingleFlatMapPublisher;
/*      */ import io.reactivex.internal.operators.single.SingleFromCallable;
/*      */ import io.reactivex.internal.operators.single.SingleFromPublisher;
/*      */ import io.reactivex.internal.operators.single.SingleFromUnsafeSource;
/*      */ import io.reactivex.internal.operators.single.SingleHide;
/*      */ import io.reactivex.internal.operators.single.SingleInternalHelper;
/*      */ import io.reactivex.internal.operators.single.SingleJust;
/*      */ import io.reactivex.internal.operators.single.SingleLift;
/*      */ import io.reactivex.internal.operators.single.SingleMap;
/*      */ import io.reactivex.internal.operators.single.SingleMaterialize;
/*      */ import io.reactivex.internal.operators.single.SingleNever;
/*      */ import io.reactivex.internal.operators.single.SingleObserveOn;
/*      */ import io.reactivex.internal.operators.single.SingleOnErrorReturn;
/*      */ import io.reactivex.internal.operators.single.SingleResumeNext;
/*      */ import io.reactivex.internal.operators.single.SingleSubscribeOn;
/*      */ import io.reactivex.internal.operators.single.SingleTakeUntil;
/*      */ import io.reactivex.internal.operators.single.SingleTimeout;
/*      */ import io.reactivex.internal.operators.single.SingleTimer;
/*      */ import io.reactivex.internal.operators.single.SingleToFlowable;
/*      */ import io.reactivex.internal.operators.single.SingleToObservable;
/*      */ import io.reactivex.internal.operators.single.SingleUnsubscribeOn;
/*      */ import io.reactivex.internal.operators.single.SingleUsing;
/*      */ import io.reactivex.internal.operators.single.SingleZipArray;
/*      */ import io.reactivex.internal.operators.single.SingleZipIterable;
/*      */ import io.reactivex.internal.util.ErrorMode;
/*      */ import io.reactivex.internal.util.ExceptionHelper;
/*      */ import io.reactivex.observers.TestObserver;
/*      */ import io.reactivex.plugins.RxJavaPlugins;
/*      */ import io.reactivex.schedulers.Schedulers;
/*      */ import java.util.NoSuchElementException;
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
/*      */ public abstract class Single<T>
/*      */   implements SingleSource<T>
/*      */ {
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <T> Single<T> amb(Iterable<? extends SingleSource<? extends T>> sources) {
/*  135 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  136 */     return RxJavaPlugins.onAssembly((Single)new SingleAmb(null, sources));
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
/*      */   public static <T> Single<T> ambArray(SingleSource<? extends T>... sources) {
/*  158 */     if (sources.length == 0) {
/*  159 */       return error(SingleInternalHelper.emptyThrower());
/*      */     }
/*  161 */     if (sources.length == 1) {
/*  162 */       return wrap((SingleSource)sources[0]);
/*      */     }
/*  164 */     return RxJavaPlugins.onAssembly((Single)new SingleAmb((SingleSource[])sources, null));
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
/*      */   public static <T> Flowable<T> concat(Iterable<? extends SingleSource<? extends T>> sources) {
/*  188 */     return concat(Flowable.fromIterable(sources));
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
/*      */   public static <T> Observable<T> concat(ObservableSource<? extends SingleSource<? extends T>> sources) {
/*  210 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  211 */     return RxJavaPlugins.onAssembly((Observable)new ObservableConcatMap(sources, SingleInternalHelper.toObservable(), 2, ErrorMode.IMMEDIATE));
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
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <T> Flowable<T> concat(Publisher<? extends SingleSource<? extends T>> sources) {
/*  236 */     return concat(sources, 2);
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
/*      */   public static <T> Flowable<T> concat(Publisher<? extends SingleSource<? extends T>> sources, int prefetch) {
/*  263 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  264 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/*  265 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableConcatMapPublisher(sources, SingleInternalHelper.toFlowable(), prefetch, ErrorMode.IMMEDIATE));
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
/*      */   @CheckReturnValue
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <T> Flowable<T> concat(SingleSource<? extends T> source1, SingleSource<? extends T> source2) {
/*  295 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  296 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  297 */     return concat(Flowable.fromArray((SingleSource<? extends T>[])new SingleSource[] { source1, source2 }));
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
/*      */   @CheckReturnValue
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <T> Flowable<T> concat(SingleSource<? extends T> source1, SingleSource<? extends T> source2, SingleSource<? extends T> source3) {
/*  330 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  331 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  332 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*  333 */     return concat(Flowable.fromArray((SingleSource<? extends T>[])new SingleSource[] { source1, source2, source3 }));
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
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <T> Flowable<T> concat(SingleSource<? extends T> source1, SingleSource<? extends T> source2, SingleSource<? extends T> source3, SingleSource<? extends T> source4) {
/*  368 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  369 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  370 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*  371 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/*  372 */     return concat(Flowable.fromArray((SingleSource<? extends T>[])new SingleSource[] { source1, source2, source3, source4 }));
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
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <T> Flowable<T> concatArray(SingleSource<? extends T>... sources) {
/*  397 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableConcatMap(Flowable.fromArray(sources), SingleInternalHelper.toFlowable(), 2, ErrorMode.BOUNDARY));
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
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <T> Flowable<T> concatArrayEager(SingleSource<? extends T>... sources) {
/*  423 */     return Flowable.<SingleSource<? extends T>>fromArray(sources).concatMapEager(SingleInternalHelper.toFlowable());
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
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <T> Flowable<T> concatEager(Publisher<? extends SingleSource<? extends T>> sources) {
/*  451 */     return Flowable.<SingleSource<? extends T>>fromPublisher(sources).concatMapEager(SingleInternalHelper.toFlowable());
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
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <T> Flowable<T> concatEager(Iterable<? extends SingleSource<? extends T>> sources) {
/*  477 */     return Flowable.<SingleSource<? extends T>>fromIterable(sources).concatMapEager(SingleInternalHelper.toFlowable());
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <T> Single<T> create(SingleOnSubscribe<T> source) {
/*  520 */     ObjectHelper.requireNonNull(source, "source is null");
/*  521 */     return RxJavaPlugins.onAssembly((Single)new SingleCreate(source));
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
/*      */   public static <T> Single<T> defer(Callable<? extends SingleSource<? extends T>> singleSupplier) {
/*  542 */     ObjectHelper.requireNonNull(singleSupplier, "singleSupplier is null");
/*  543 */     return RxJavaPlugins.onAssembly((Single)new SingleDefer(singleSupplier));
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
/*      */   public static <T> Single<T> error(Callable<? extends Throwable> errorSupplier) {
/*  563 */     ObjectHelper.requireNonNull(errorSupplier, "errorSupplier is null");
/*  564 */     return RxJavaPlugins.onAssembly((Single)new SingleError(errorSupplier));
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
/*      */   public static <T> Single<T> error(Throwable exception) {
/*  589 */     ObjectHelper.requireNonNull(exception, "exception is null");
/*  590 */     return error(Functions.justCallable(exception));
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
/*      */   public static <T> Single<T> fromCallable(Callable<? extends T> callable) {
/*  623 */     ObjectHelper.requireNonNull(callable, "callable is null");
/*  624 */     return RxJavaPlugins.onAssembly((Single)new SingleFromCallable(callable));
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
/*      */   public static <T> Single<T> fromFuture(Future<? extends T> future) {
/*  653 */     return toSingle(Flowable.fromFuture(future));
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
/*      */   public static <T> Single<T> fromFuture(Future<? extends T> future, long timeout, TimeUnit unit) {
/*  686 */     return toSingle(Flowable.fromFuture(future, timeout, unit));
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
/*      */   @SchedulerSupport("custom")
/*      */   public static <T> Single<T> fromFuture(Future<? extends T> future, long timeout, TimeUnit unit, Scheduler scheduler) {
/*  721 */     return toSingle(Flowable.fromFuture(future, timeout, unit, scheduler));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("custom")
/*      */   public static <T> Single<T> fromFuture(Future<? extends T> future, Scheduler scheduler) {
/*  751 */     return toSingle(Flowable.fromFuture(future, scheduler));
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
/*      */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <T> Single<T> fromPublisher(Publisher<? extends T> publisher) {
/*  788 */     ObjectHelper.requireNonNull(publisher, "publisher is null");
/*  789 */     return RxJavaPlugins.onAssembly((Single)new SingleFromPublisher(publisher));
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
/*      */   public static <T> Single<T> fromObservable(ObservableSource<? extends T> observableSource) {
/*  812 */     ObjectHelper.requireNonNull(observableSource, "observableSource is null");
/*  813 */     return RxJavaPlugins.onAssembly((Single)new ObservableSingleSingle(observableSource, null));
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
/*      */   public static <T> Single<T> just(T item) {
/*  839 */     ObjectHelper.requireNonNull(item, "item is null");
/*  840 */     return RxJavaPlugins.onAssembly((Single)new SingleJust(item));
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
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <T> Flowable<T> merge(Iterable<? extends SingleSource<? extends T>> sources) {
/*  878 */     return merge(Flowable.fromIterable(sources));
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
/*      */   @CheckReturnValue
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <T> Flowable<T> merge(Publisher<? extends SingleSource<? extends T>> sources) {
/*  917 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  918 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableFlatMapPublisher(sources, SingleInternalHelper.toFlowable(), false, 2147483647, Flowable.bufferSize()));
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
/*      */   public static <T> Single<T> merge(SingleSource<? extends SingleSource<? extends T>> source) {
/*  948 */     ObjectHelper.requireNonNull(source, "source is null");
/*  949 */     return RxJavaPlugins.onAssembly((Single)new SingleFlatMap(source, Functions.identity()));
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
/*      */   @CheckReturnValue
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <T> Flowable<T> merge(SingleSource<? extends T> source1, SingleSource<? extends T> source2) {
/*  996 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  997 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  998 */     return merge(Flowable.fromArray((SingleSource<? extends T>[])new SingleSource[] { source1, source2 }));
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
/*      */   @CheckReturnValue
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <T> Flowable<T> merge(SingleSource<? extends T> source1, SingleSource<? extends T> source2, SingleSource<? extends T> source3) {
/* 1048 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/* 1049 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/* 1050 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/* 1051 */     return merge(Flowable.fromArray((SingleSource<? extends T>[])new SingleSource[] { source1, source2, source3 }));
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
/*      */   @CheckReturnValue
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <T> Flowable<T> merge(SingleSource<? extends T> source1, SingleSource<? extends T> source2, SingleSource<? extends T> source3, SingleSource<? extends T> source4) {
/* 1103 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/* 1104 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/* 1105 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/* 1106 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/* 1107 */     return merge(Flowable.fromArray((SingleSource<? extends T>[])new SingleSource[] { source1, source2, source3, source4 }));
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
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <T> Flowable<T> mergeDelayError(Iterable<? extends SingleSource<? extends T>> sources) {
/* 1133 */     return mergeDelayError(Flowable.fromIterable(sources));
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
/*      */   public static <T> Flowable<T> mergeDelayError(Publisher<? extends SingleSource<? extends T>> sources) {
/* 1160 */     ObjectHelper.requireNonNull(sources, "sources is null");
/* 1161 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableFlatMapPublisher(sources, SingleInternalHelper.toFlowable(), true, 2147483647, Flowable.bufferSize()));
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
/*      */   @CheckReturnValue
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <T> Flowable<T> mergeDelayError(SingleSource<? extends T> source1, SingleSource<? extends T> source2) {
/* 1197 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/* 1198 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/* 1199 */     return mergeDelayError(Flowable.fromArray((SingleSource<? extends T>[])new SingleSource[] { source1, source2 }));
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
/*      */   @CheckReturnValue
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <T> Flowable<T> mergeDelayError(SingleSource<? extends T> source1, SingleSource<? extends T> source2, SingleSource<? extends T> source3) {
/* 1238 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/* 1239 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/* 1240 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/* 1241 */     return mergeDelayError(Flowable.fromArray((SingleSource<? extends T>[])new SingleSource[] { source1, source2, source3 }));
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
/*      */   @CheckReturnValue
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <T> Flowable<T> mergeDelayError(SingleSource<? extends T> source1, SingleSource<? extends T> source2, SingleSource<? extends T> source3, SingleSource<? extends T> source4) {
/* 1282 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/* 1283 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/* 1284 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/* 1285 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/* 1286 */     return mergeDelayError(Flowable.fromArray((SingleSource<? extends T>[])new SingleSource[] { source1, source2, source3, source4 }));
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
/*      */   public static <T> Single<T> never() {
/* 1305 */     return RxJavaPlugins.onAssembly(SingleNever.INSTANCE);
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
/*      */   public static Single<Long> timer(long delay, TimeUnit unit) {
/* 1324 */     return timer(delay, unit, Schedulers.computation());
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
/*      */   @SchedulerSupport("custom")
/*      */   @NonNull
/*      */   public static Single<Long> timer(long delay, TimeUnit unit, Scheduler scheduler) {
/* 1348 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 1349 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 1350 */     return RxJavaPlugins.onAssembly((Single)new SingleTimer(delay, unit, scheduler));
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
/*      */   public static <T> Single<Boolean> equals(SingleSource<? extends T> first, SingleSource<? extends T> second) {
/* 1371 */     ObjectHelper.requireNonNull(first, "first is null");
/* 1372 */     ObjectHelper.requireNonNull(second, "second is null");
/* 1373 */     return RxJavaPlugins.onAssembly((Single)new SingleEquals(first, second));
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
/*      */   public static <T> Single<T> unsafeCreate(SingleSource<T> onSubscribe) {
/* 1397 */     ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");
/* 1398 */     if (onSubscribe instanceof Single) {
/* 1399 */       throw new IllegalArgumentException("unsafeCreate(Single) should be upgraded");
/*      */     }
/* 1401 */     return RxJavaPlugins.onAssembly((Single)new SingleFromUnsafeSource(onSubscribe));
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
/*      */   public static <T, U> Single<T> using(Callable<U> resourceSupplier, Function<? super U, ? extends SingleSource<? extends T>> singleFunction, Consumer<? super U> disposer) {
/* 1430 */     return using(resourceSupplier, singleFunction, disposer, true);
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <T, U> Single<T> using(Callable<U> resourceSupplier, Function<? super U, ? extends SingleSource<? extends T>> singleFunction, Consumer<? super U> disposer, boolean eager) {
/* 1465 */     ObjectHelper.requireNonNull(resourceSupplier, "resourceSupplier is null");
/* 1466 */     ObjectHelper.requireNonNull(singleFunction, "singleFunction is null");
/* 1467 */     ObjectHelper.requireNonNull(disposer, "disposer is null");
/*      */     
/* 1469 */     return RxJavaPlugins.onAssembly((Single)new SingleUsing(resourceSupplier, singleFunction, disposer, eager));
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
/*      */   public static <T> Single<T> wrap(SingleSource<T> source) {
/* 1489 */     ObjectHelper.requireNonNull(source, "source is null");
/* 1490 */     if (source instanceof Single) {
/* 1491 */       return RxJavaPlugins.onAssembly((Single)source);
/*      */     }
/* 1493 */     return RxJavaPlugins.onAssembly((Single)new SingleFromUnsafeSource(source));
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
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <T, R> Single<R> zip(Iterable<? extends SingleSource<? extends T>> sources, Function<? super Object[], ? extends R> zipper) {
/* 1529 */     ObjectHelper.requireNonNull(zipper, "zipper is null");
/* 1530 */     ObjectHelper.requireNonNull(sources, "sources is null");
/* 1531 */     return RxJavaPlugins.onAssembly((Single)new SingleZipIterable(sources, zipper));
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
/*      */   public static <T1, T2, R> Single<R> zip(SingleSource<? extends T1> source1, SingleSource<? extends T2> source2, BiFunction<? super T1, ? super T2, ? extends R> zipper) {
/* 1565 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/* 1566 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/* 1567 */     return zipArray(Functions.toFunction(zipper), (SingleSource<?>[])new SingleSource[] { source1, source2 });
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <T1, T2, T3, R> Single<R> zip(SingleSource<? extends T1> source1, SingleSource<? extends T2> source2, SingleSource<? extends T3> source3, Function3<? super T1, ? super T2, ? super T3, ? extends R> zipper) {
/* 1605 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/* 1606 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/* 1607 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/* 1608 */     return zipArray(Functions.toFunction(zipper), (SingleSource<?>[])new SingleSource[] { source1, source2, source3 });
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <T1, T2, T3, T4, R> Single<R> zip(SingleSource<? extends T1> source1, SingleSource<? extends T2> source2, SingleSource<? extends T3> source3, SingleSource<? extends T4> source4, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> zipper) {
/* 1649 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/* 1650 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/* 1651 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/* 1652 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/* 1653 */     return zipArray(Functions.toFunction(zipper), (SingleSource<?>[])new SingleSource[] { source1, source2, source3, source4 });
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <T1, T2, T3, T4, T5, R> Single<R> zip(SingleSource<? extends T1> source1, SingleSource<? extends T2> source2, SingleSource<? extends T3> source3, SingleSource<? extends T4> source4, SingleSource<? extends T5> source5, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> zipper) {
/* 1698 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/* 1699 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/* 1700 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/* 1701 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/* 1702 */     ObjectHelper.requireNonNull(source5, "source5 is null");
/* 1703 */     return zipArray(Functions.toFunction(zipper), (SingleSource<?>[])new SingleSource[] { source1, source2, source3, source4, source5 });
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <T1, T2, T3, T4, T5, T6, R> Single<R> zip(SingleSource<? extends T1> source1, SingleSource<? extends T2> source2, SingleSource<? extends T3> source3, SingleSource<? extends T4> source4, SingleSource<? extends T5> source5, SingleSource<? extends T6> source6, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> zipper) {
/* 1751 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/* 1752 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/* 1753 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/* 1754 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/* 1755 */     ObjectHelper.requireNonNull(source5, "source5 is null");
/* 1756 */     ObjectHelper.requireNonNull(source6, "source6 is null");
/* 1757 */     return zipArray(Functions.toFunction(zipper), (SingleSource<?>[])new SingleSource[] { source1, source2, source3, source4, source5, source6 });
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <T1, T2, T3, T4, T5, T6, T7, R> Single<R> zip(SingleSource<? extends T1> source1, SingleSource<? extends T2> source2, SingleSource<? extends T3> source3, SingleSource<? extends T4> source4, SingleSource<? extends T5> source5, SingleSource<? extends T6> source6, SingleSource<? extends T7> source7, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> zipper) {
/* 1809 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/* 1810 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/* 1811 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/* 1812 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/* 1813 */     ObjectHelper.requireNonNull(source5, "source5 is null");
/* 1814 */     ObjectHelper.requireNonNull(source6, "source6 is null");
/* 1815 */     ObjectHelper.requireNonNull(source7, "source7 is null");
/* 1816 */     return zipArray(Functions.toFunction(zipper), (SingleSource<?>[])new SingleSource[] { source1, source2, source3, source4, source5, source6, source7 });
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Single<R> zip(SingleSource<? extends T1> source1, SingleSource<? extends T2> source2, SingleSource<? extends T3> source3, SingleSource<? extends T4> source4, SingleSource<? extends T5> source5, SingleSource<? extends T6> source6, SingleSource<? extends T7> source7, SingleSource<? extends T8> source8, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> zipper) {
/* 1871 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/* 1872 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/* 1873 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/* 1874 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/* 1875 */     ObjectHelper.requireNonNull(source5, "source5 is null");
/* 1876 */     ObjectHelper.requireNonNull(source6, "source6 is null");
/* 1877 */     ObjectHelper.requireNonNull(source7, "source7 is null");
/* 1878 */     ObjectHelper.requireNonNull(source8, "source8 is null");
/* 1879 */     return zipArray(Functions.toFunction(zipper), (SingleSource<?>[])new SingleSource[] { source1, source2, source3, source4, source5, source6, source7, source8 });
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Single<R> zip(SingleSource<? extends T1> source1, SingleSource<? extends T2> source2, SingleSource<? extends T3> source3, SingleSource<? extends T4> source4, SingleSource<? extends T5> source5, SingleSource<? extends T6> source6, SingleSource<? extends T7> source7, SingleSource<? extends T8> source8, SingleSource<? extends T9> source9, Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> zipper) {
/* 1938 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/* 1939 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/* 1940 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/* 1941 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/* 1942 */     ObjectHelper.requireNonNull(source5, "source5 is null");
/* 1943 */     ObjectHelper.requireNonNull(source6, "source6 is null");
/* 1944 */     ObjectHelper.requireNonNull(source7, "source7 is null");
/* 1945 */     ObjectHelper.requireNonNull(source8, "source8 is null");
/* 1946 */     ObjectHelper.requireNonNull(source9, "source9 is null");
/* 1947 */     return zipArray(Functions.toFunction(zipper), (SingleSource<?>[])new SingleSource[] { source1, source2, source3, source4, source5, source6, source7, source8, source9 });
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
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <T, R> Single<R> zipArray(Function<? super Object[], ? extends R> zipper, SingleSource<? extends T>... sources) {
/* 1983 */     ObjectHelper.requireNonNull(zipper, "zipper is null");
/* 1984 */     ObjectHelper.requireNonNull(sources, "sources is null");
/* 1985 */     if (sources.length == 0) {
/* 1986 */       return error(new NoSuchElementException());
/*      */     }
/* 1988 */     return RxJavaPlugins.onAssembly((Single)new SingleZipArray((SingleSource[])sources, zipper));
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
/*      */   public final Single<T> ambWith(SingleSource<? extends T> other) {
/* 2009 */     ObjectHelper.requireNonNull(other, "other is null");
/* 2010 */     return ambArray((SingleSource<? extends T>[])new SingleSource[] { this, other });
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
/*      */   public final <R> R as(@NonNull SingleConverter<T, ? extends R> converter) {
/* 2033 */     return ((SingleConverter<T, R>)ObjectHelper.requireNonNull(converter, "converter is null")).apply(this);
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
/*      */   public final Single<T> hide() {
/* 2051 */     return RxJavaPlugins.onAssembly((Single)new SingleHide(this));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final <R> Single<R> compose(SingleTransformer<? super T, ? extends R> transformer) {
/* 2078 */     return wrap(((SingleTransformer<T, R>)ObjectHelper.requireNonNull(transformer, "transformer is null")).apply(this));
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
/*      */   public final Single<T> cache() {
/* 2097 */     return RxJavaPlugins.onAssembly((Single)new SingleCache(this));
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
/*      */   public final <U> Single<U> cast(Class<? extends U> clazz) {
/* 2118 */     ObjectHelper.requireNonNull(clazz, "clazz is null");
/* 2119 */     return map(Functions.castFunction(clazz));
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
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final Flowable<T> concatWith(SingleSource<? extends T> other) {
/* 2144 */     return concat(this, other);
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
/*      */   public final Single<T> delay(long time, TimeUnit unit) {
/* 2165 */     return delay(time, unit, Schedulers.computation(), false);
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
/*      */   public final Single<T> delay(long time, TimeUnit unit, boolean delayError) {
/* 2186 */     return delay(time, unit, Schedulers.computation(), delayError);
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
/*      */   @SchedulerSupport("custom")
/*      */   public final Single<T> delay(long time, TimeUnit unit, Scheduler scheduler) {
/* 2211 */     return delay(time, unit, scheduler, false);
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
/*      */   @SchedulerSupport("custom")
/*      */   @NonNull
/*      */   public final Single<T> delay(long time, TimeUnit unit, Scheduler scheduler, boolean delayError) {
/* 2237 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 2238 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 2239 */     return RxJavaPlugins.onAssembly((Single)new SingleDelay(this, time, unit, scheduler, delayError));
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
/*      */   public final Single<T> delaySubscription(CompletableSource other) {
/* 2262 */     ObjectHelper.requireNonNull(other, "other is null");
/* 2263 */     return RxJavaPlugins.onAssembly((Single)new SingleDelayWithCompletable(this, other));
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
/*      */   public final <U> Single<T> delaySubscription(SingleSource<U> other) {
/* 2287 */     ObjectHelper.requireNonNull(other, "other is null");
/* 2288 */     return RxJavaPlugins.onAssembly((Single)new SingleDelayWithSingle(this, other));
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
/*      */   public final <U> Single<T> delaySubscription(ObservableSource<U> other) {
/* 2312 */     ObjectHelper.requireNonNull(other, "other is null");
/* 2313 */     return RxJavaPlugins.onAssembly((Single)new SingleDelayWithObservable(this, other));
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
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public final <U> Single<T> delaySubscription(Publisher<U> other) {
/* 2342 */     ObjectHelper.requireNonNull(other, "other is null");
/* 2343 */     return RxJavaPlugins.onAssembly((Single)new SingleDelayWithPublisher(this, other));
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
/*      */   public final Single<T> delaySubscription(long time, TimeUnit unit) {
/* 2363 */     return delaySubscription(time, unit, Schedulers.computation());
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
/*      */   public final Single<T> delaySubscription(long time, TimeUnit unit, Scheduler scheduler) {
/* 2384 */     return delaySubscription(Observable.timer(time, unit, scheduler));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   @Experimental
/*      */   public final <R> Maybe<R> dematerialize(Function<? super T, Notification<R>> selector) {
/* 2423 */     ObjectHelper.requireNonNull(selector, "selector is null");
/* 2424 */     return RxJavaPlugins.onAssembly((Maybe)new SingleDematerialize(this, selector));
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
/*      */   public final Single<T> doAfterSuccess(Consumer<? super T> onAfterSuccess) {
/* 2447 */     ObjectHelper.requireNonNull(onAfterSuccess, "onAfterSuccess is null");
/* 2448 */     return RxJavaPlugins.onAssembly((Single)new SingleDoAfterSuccess(this, onAfterSuccess));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public final Single<T> doAfterTerminate(Action onAfterTerminate) {
/* 2476 */     ObjectHelper.requireNonNull(onAfterTerminate, "onAfterTerminate is null");
/* 2477 */     return RxJavaPlugins.onAssembly((Single)new SingleDoAfterTerminate(this, onAfterTerminate));
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
/*      */   public final Single<T> doFinally(Action onFinally) {
/* 2503 */     ObjectHelper.requireNonNull(onFinally, "onFinally is null");
/* 2504 */     return RxJavaPlugins.onAssembly((Single)new SingleDoFinally(this, onFinally));
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
/*      */   public final Single<T> doOnSubscribe(Consumer<? super Disposable> onSubscribe) {
/* 2525 */     ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");
/* 2526 */     return RxJavaPlugins.onAssembly((Single)new SingleDoOnSubscribe(this, onSubscribe));
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
/*      */   @Experimental
/*      */   @NonNull
/*      */   public final Single<T> doOnTerminate(Action onTerminate) {
/* 2552 */     ObjectHelper.requireNonNull(onTerminate, "onTerminate is null");
/* 2553 */     return RxJavaPlugins.onAssembly((Single)new SingleDoOnTerminate(this, onTerminate));
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
/*      */   public final Single<T> doOnSuccess(Consumer<? super T> onSuccess) {
/* 2574 */     ObjectHelper.requireNonNull(onSuccess, "onSuccess is null");
/* 2575 */     return RxJavaPlugins.onAssembly((Single)new SingleDoOnSuccess(this, onSuccess));
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
/*      */   public final Single<T> doOnEvent(BiConsumer<? super T, ? super Throwable> onEvent) {
/* 2595 */     ObjectHelper.requireNonNull(onEvent, "onEvent is null");
/* 2596 */     return RxJavaPlugins.onAssembly((Single)new SingleDoOnEvent(this, onEvent));
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
/*      */   public final Single<T> doOnError(Consumer<? super Throwable> onError) {
/* 2617 */     ObjectHelper.requireNonNull(onError, "onError is null");
/* 2618 */     return RxJavaPlugins.onAssembly((Single)new SingleDoOnError(this, onError));
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
/*      */   public final Single<T> doOnDispose(Action onDispose) {
/* 2640 */     ObjectHelper.requireNonNull(onDispose, "onDispose is null");
/* 2641 */     return RxJavaPlugins.onAssembly((Single)new SingleDoOnDispose(this, onDispose));
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
/*      */   public final Maybe<T> filter(Predicate<? super T> predicate) {
/* 2665 */     ObjectHelper.requireNonNull(predicate, "predicate is null");
/* 2666 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeFilterSingle(this, predicate));
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
/*      */   public final <R> Single<R> flatMap(Function<? super T, ? extends SingleSource<? extends R>> mapper) {
/* 2689 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 2690 */     return RxJavaPlugins.onAssembly((Single)new SingleFlatMap(this, mapper));
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
/*      */   public final <R> Maybe<R> flatMapMaybe(Function<? super T, ? extends MaybeSource<? extends R>> mapper) {
/* 2713 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 2714 */     return RxJavaPlugins.onAssembly((Maybe)new SingleFlatMapMaybe(this, mapper));
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
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public final <R> Flowable<R> flatMapPublisher(Function<? super T, ? extends Publisher<? extends R>> mapper) {
/* 2742 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 2743 */     return RxJavaPlugins.onAssembly((Flowable)new SingleFlatMapPublisher(this, mapper));
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
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public final <U> Flowable<U> flattenAsFlowable(Function<? super T, ? extends Iterable<? extends U>> mapper) {
/* 2771 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 2772 */     return RxJavaPlugins.onAssembly((Flowable)new SingleFlatMapIterableFlowable(this, mapper));
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
/*      */   public final <U> Observable<U> flattenAsObservable(Function<? super T, ? extends Iterable<? extends U>> mapper) {
/* 2797 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 2798 */     return RxJavaPlugins.onAssembly((Observable)new SingleFlatMapIterableObservable(this, mapper));
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
/*      */   public final <R> Observable<R> flatMapObservable(Function<? super T, ? extends ObservableSource<? extends R>> mapper) {
/* 2821 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 2822 */     return RxJavaPlugins.onAssembly((Observable)new SingleFlatMapObservable(this, mapper));
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
/*      */   public final Completable flatMapCompletable(Function<? super T, ? extends CompletableSource> mapper) {
/* 2846 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 2847 */     return RxJavaPlugins.onAssembly((Completable)new SingleFlatMapCompletable(this, mapper));
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
/*      */   public final T blockingGet() {
/* 2868 */     BlockingMultiObserver<T> observer = new BlockingMultiObserver();
/* 2869 */     subscribe((SingleObserver<? super T>)observer);
/* 2870 */     return (T)observer.blockingGet();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final <R> Single<R> lift(SingleOperator<? extends R, ? super T> lift) {
/* 3020 */     ObjectHelper.requireNonNull(lift, "lift is null");
/* 3021 */     return RxJavaPlugins.onAssembly((Single)new SingleLift(this, lift));
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
/*      */   public final <R> Single<R> map(Function<? super T, ? extends R> mapper) {
/* 3044 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 3045 */     return RxJavaPlugins.onAssembly((Single)new SingleMap(this, mapper));
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
/*      */   @Experimental
/*      */   public final Single<Notification<T>> materialize() {
/* 3065 */     return RxJavaPlugins.onAssembly((Single)new SingleMaterialize(this));
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
/*      */   public final Single<Boolean> contains(Object value) {
/* 3084 */     return contains(value, ObjectHelper.equalsPredicate());
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
/*      */   public final Single<Boolean> contains(Object value, BiPredicate<Object, Object> comparer) {
/* 3106 */     ObjectHelper.requireNonNull(value, "value is null");
/* 3107 */     ObjectHelper.requireNonNull(comparer, "comparer is null");
/* 3108 */     return RxJavaPlugins.onAssembly((Single)new SingleContains(this, value, comparer));
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
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final Flowable<T> mergeWith(SingleSource<? extends T> other) {
/* 3134 */     return merge(this, other);
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
/*      */   @SchedulerSupport("custom")
/*      */   @NonNull
/*      */   public final Single<T> observeOn(Scheduler scheduler) {
/* 3160 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 3161 */     return RxJavaPlugins.onAssembly((Single)new SingleObserveOn(this, scheduler));
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
/*      */   public final Single<T> onErrorReturn(Function<Throwable, ? extends T> resumeFunction) {
/* 3194 */     ObjectHelper.requireNonNull(resumeFunction, "resumeFunction is null");
/* 3195 */     return RxJavaPlugins.onAssembly((Single)new SingleOnErrorReturn(this, resumeFunction, null));
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
/*      */   public final Single<T> onErrorReturnItem(T value) {
/* 3214 */     ObjectHelper.requireNonNull(value, "value is null");
/* 3215 */     return RxJavaPlugins.onAssembly((Single)new SingleOnErrorReturn(this, null, value));
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
/*      */   public final Single<T> onErrorResumeNext(Single<? extends T> resumeSingleInCaseOfError) {
/* 3249 */     ObjectHelper.requireNonNull(resumeSingleInCaseOfError, "resumeSingleInCaseOfError is null");
/* 3250 */     return onErrorResumeNext(Functions.justFunction(resumeSingleInCaseOfError));
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
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public final Single<T> onErrorResumeNext(Function<? super Throwable, ? extends SingleSource<? extends T>> resumeFunctionInCaseOfError) {
/* 3286 */     ObjectHelper.requireNonNull(resumeFunctionInCaseOfError, "resumeFunctionInCaseOfError is null");
/* 3287 */     return RxJavaPlugins.onAssembly((Single)new SingleResumeNext(this, resumeFunctionInCaseOfError));
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
/*      */   public final Single<T> onTerminateDetach() {
/* 3307 */     return RxJavaPlugins.onAssembly((Single)new SingleDetach(this));
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
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final Flowable<T> repeat() {
/* 3327 */     return toFlowable().repeat();
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
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final Flowable<T> repeat(long times) {
/* 3348 */     return toFlowable().repeat(times);
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
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final Flowable<T> repeatWhen(Function<? super Flowable<Object>, ? extends Publisher<?>> handler) {
/* 3375 */     return toFlowable().repeatWhen(handler);
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
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final Flowable<T> repeatUntil(BooleanSupplier stop) {
/* 3397 */     return toFlowable().repeatUntil(stop);
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
/*      */   public final Single<T> retry() {
/* 3414 */     return toSingle(toFlowable().retry());
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
/*      */   public final Single<T> retry(long times) {
/* 3433 */     return toSingle(toFlowable().retry(times));
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
/*      */   public final Single<T> retry(BiPredicate<? super Integer, ? super Throwable> predicate) {
/* 3453 */     return toSingle(toFlowable().retry(predicate));
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
/*      */   public final Single<T> retry(long times, Predicate<? super Throwable> predicate) {
/* 3475 */     return toSingle(toFlowable().retry(times, predicate));
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
/*      */   public final Single<T> retry(Predicate<? super Throwable> predicate) {
/* 3495 */     return toSingle(toFlowable().retry(predicate));
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final Single<T> retryWhen(Function<? super Flowable<Throwable>, ? extends Publisher<?>> handler) {
/* 3544 */     return toSingle(toFlowable().retryWhen(handler));
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
/*      */   @SchedulerSupport("none")
/*      */   public final Disposable subscribe() {
/* 3565 */     return subscribe(Functions.emptyConsumer(), Functions.ON_ERROR_MISSING);
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
/*      */   public final Disposable subscribe(BiConsumer<? super T, ? super Throwable> onCallback) {
/* 3590 */     ObjectHelper.requireNonNull(onCallback, "onCallback is null");
/*      */     
/* 3592 */     BiConsumerSingleObserver<T> observer = new BiConsumerSingleObserver(onCallback);
/* 3593 */     subscribe((SingleObserver<? super T>)observer);
/* 3594 */     return (Disposable)observer;
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
/*      */   public final Disposable subscribe(Consumer<? super T> onSuccess) {
/* 3620 */     return subscribe(onSuccess, Functions.ON_ERROR_MISSING);
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public final Disposable subscribe(Consumer<? super T> onSuccess, Consumer<? super Throwable> onError) {
/* 3648 */     ObjectHelper.requireNonNull(onSuccess, "onSuccess is null");
/* 3649 */     ObjectHelper.requireNonNull(onError, "onError is null");
/*      */     
/* 3651 */     ConsumerSingleObserver<T> observer = new ConsumerSingleObserver(onSuccess, onError);
/* 3652 */     subscribe((SingleObserver<? super T>)observer);
/* 3653 */     return (Disposable)observer;
/*      */   }
/*      */ 
/*      */   
/*      */   @SchedulerSupport("none")
/*      */   public final void subscribe(SingleObserver<? super T> observer) {
/* 3659 */     ObjectHelper.requireNonNull(observer, "observer is null");
/*      */     
/* 3661 */     observer = RxJavaPlugins.onSubscribe(this, observer);
/*      */     
/* 3663 */     ObjectHelper.requireNonNull(observer, "The RxJavaPlugins.onSubscribe hook returned a null SingleObserver. Please check the handler provided to RxJavaPlugins.setOnSingleSubscribe for invalid null returns. Further reading: https://github.com/ReactiveX/RxJava/wiki/Plugins");
/*      */     
/*      */     try {
/* 3666 */       subscribeActual(observer);
/* 3667 */     } catch (NullPointerException ex) {
/* 3668 */       throw ex;
/* 3669 */     } catch (Throwable ex) {
/* 3670 */       Exceptions.throwIfFatal(ex);
/* 3671 */       NullPointerException npe = new NullPointerException("subscribeActual failed");
/* 3672 */       npe.initCause(ex);
/* 3673 */       throw npe;
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
/*      */   protected abstract void subscribeActual(@NonNull SingleObserver<? super T> paramSingleObserver);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final <E extends SingleObserver<? super T>> E subscribeWith(E observer) {
/* 3715 */     subscribe((SingleObserver<? super T>)observer);
/* 3716 */     return observer;
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
/*      */   public final Single<T> subscribeOn(Scheduler scheduler) {
/* 3739 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 3740 */     return RxJavaPlugins.onAssembly((Single)new SingleSubscribeOn(this, scheduler));
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
/*      */   public final Single<T> takeUntil(CompletableSource other) {
/* 3764 */     ObjectHelper.requireNonNull(other, "other is null");
/* 3765 */     return takeUntil((Publisher<?>)new CompletableToFlowable(other));
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
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public final <E> Single<T> takeUntil(Publisher<E> other) {
/* 3796 */     ObjectHelper.requireNonNull(other, "other is null");
/* 3797 */     return RxJavaPlugins.onAssembly((Single)new SingleTakeUntil(this, other));
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
/*      */   public final <E> Single<T> takeUntil(SingleSource<? extends E> other) {
/* 3822 */     ObjectHelper.requireNonNull(other, "other is null");
/* 3823 */     return takeUntil((Publisher<?>)new SingleToFlowable(other));
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
/*      */   public final Single<T> timeout(long timeout, TimeUnit unit) {
/* 3843 */     return timeout0(timeout, unit, Schedulers.computation(), null);
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
/*      */   public final Single<T> timeout(long timeout, TimeUnit unit, Scheduler scheduler) {
/* 3865 */     return timeout0(timeout, unit, scheduler, null);
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
/*      */   public final Single<T> timeout(long timeout, TimeUnit unit, Scheduler scheduler, SingleSource<? extends T> other) {
/* 3888 */     ObjectHelper.requireNonNull(other, "other is null");
/* 3889 */     return timeout0(timeout, unit, scheduler, other);
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
/*      */   @SchedulerSupport("io.reactivex:computation")
/*      */   @NonNull
/*      */   public final Single<T> timeout(long timeout, TimeUnit unit, SingleSource<? extends T> other) {
/* 3916 */     ObjectHelper.requireNonNull(other, "other is null");
/* 3917 */     return timeout0(timeout, unit, Schedulers.computation(), other);
/*      */   }
/*      */   
/*      */   private Single<T> timeout0(long timeout, TimeUnit unit, Scheduler scheduler, SingleSource<? extends T> other) {
/* 3921 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 3922 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 3923 */     return RxJavaPlugins.onAssembly((Single)new SingleTimeout(this, timeout, unit, scheduler, other));
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
/*      */   public final <R> R to(Function<? super Single<T>, R> convert) {
/*      */     try {
/* 3945 */       return (R)((Function)ObjectHelper.requireNonNull(convert, "convert is null")).apply(this);
/* 3946 */     } catch (Throwable ex) {
/* 3947 */       Exceptions.throwIfFatal(ex);
/* 3948 */       throw ExceptionHelper.wrapOrThrow(ex);
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
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @Deprecated
/*      */   public final Completable toCompletable() {
/* 3972 */     return RxJavaPlugins.onAssembly((Completable)new CompletableFromSingle(this));
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
/*      */   public final Completable ignoreElement() {
/* 3992 */     return RxJavaPlugins.onAssembly((Completable)new CompletableFromSingle(this));
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
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final Flowable<T> toFlowable() {
/* 4013 */     if (this instanceof FuseToFlowable) {
/* 4014 */       return ((FuseToFlowable)this).fuseToFlowable();
/*      */     }
/* 4016 */     return RxJavaPlugins.onAssembly((Flowable)new SingleToFlowable(this));
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
/*      */   public final Future<T> toFuture() {
/* 4034 */     return (Future<T>)subscribeWith(new FutureSingleObserver());
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
/*      */   public final Maybe<T> toMaybe() {
/* 4052 */     if (this instanceof FuseToMaybe) {
/* 4053 */       return ((FuseToMaybe)this).fuseToMaybe();
/*      */     }
/* 4055 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeFromSingle(this));
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
/*      */   public final Observable<T> toObservable() {
/* 4072 */     if (this instanceof FuseToObservable) {
/* 4073 */       return ((FuseToObservable)this).fuseToObservable();
/*      */     }
/* 4075 */     return RxJavaPlugins.onAssembly((Observable)new SingleToObservable(this));
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
/*      */   @NonNull
/*      */   public final Single<T> unsubscribeOn(Scheduler scheduler) {
/* 4097 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 4098 */     return RxJavaPlugins.onAssembly((Single)new SingleUnsubscribeOn(this, scheduler));
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
/*      */   public final <U, R> Single<R> zipWith(SingleSource<U> other, BiFunction<? super T, ? super U, ? extends R> zipper) {
/* 4127 */     return zip(this, other, zipper);
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
/*      */   public final TestObserver<T> test() {
/* 4148 */     TestObserver<T> to = new TestObserver();
/* 4149 */     subscribe((SingleObserver<? super T>)to);
/* 4150 */     return to;
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
/*      */   public final TestObserver<T> test(boolean cancelled) {
/* 4169 */     TestObserver<T> to = new TestObserver();
/*      */     
/* 4171 */     if (cancelled) {
/* 4172 */       to.cancel();
/*      */     }
/*      */     
/* 4175 */     subscribe((SingleObserver<? super T>)to);
/* 4176 */     return to;
/*      */   }
/*      */   
/*      */   private static <T> Single<T> toSingle(Flowable<T> source) {
/* 4180 */     return RxJavaPlugins.onAssembly((Single)new FlowableSingleSingle(source, null));
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/Single.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */