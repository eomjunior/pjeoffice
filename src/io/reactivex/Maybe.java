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
/*      */ import io.reactivex.internal.fuseable.FuseToObservable;
/*      */ import io.reactivex.internal.observers.BlockingMultiObserver;
/*      */ import io.reactivex.internal.operators.flowable.FlowableConcatMapPublisher;
/*      */ import io.reactivex.internal.operators.flowable.FlowableFlatMapPublisher;
/*      */ import io.reactivex.internal.operators.maybe.MaybeAmb;
/*      */ import io.reactivex.internal.operators.maybe.MaybeCache;
/*      */ import io.reactivex.internal.operators.maybe.MaybeCallbackObserver;
/*      */ import io.reactivex.internal.operators.maybe.MaybeConcatArray;
/*      */ import io.reactivex.internal.operators.maybe.MaybeConcatArrayDelayError;
/*      */ import io.reactivex.internal.operators.maybe.MaybeConcatIterable;
/*      */ import io.reactivex.internal.operators.maybe.MaybeContains;
/*      */ import io.reactivex.internal.operators.maybe.MaybeCount;
/*      */ import io.reactivex.internal.operators.maybe.MaybeCreate;
/*      */ import io.reactivex.internal.operators.maybe.MaybeDefer;
/*      */ import io.reactivex.internal.operators.maybe.MaybeDelay;
/*      */ import io.reactivex.internal.operators.maybe.MaybeDelayOtherPublisher;
/*      */ import io.reactivex.internal.operators.maybe.MaybeDelaySubscriptionOtherPublisher;
/*      */ import io.reactivex.internal.operators.maybe.MaybeDetach;
/*      */ import io.reactivex.internal.operators.maybe.MaybeDoAfterSuccess;
/*      */ import io.reactivex.internal.operators.maybe.MaybeDoFinally;
/*      */ import io.reactivex.internal.operators.maybe.MaybeDoOnEvent;
/*      */ import io.reactivex.internal.operators.maybe.MaybeDoOnTerminate;
/*      */ import io.reactivex.internal.operators.maybe.MaybeEmpty;
/*      */ import io.reactivex.internal.operators.maybe.MaybeEqualSingle;
/*      */ import io.reactivex.internal.operators.maybe.MaybeError;
/*      */ import io.reactivex.internal.operators.maybe.MaybeErrorCallable;
/*      */ import io.reactivex.internal.operators.maybe.MaybeFilter;
/*      */ import io.reactivex.internal.operators.maybe.MaybeFlatMapBiSelector;
/*      */ import io.reactivex.internal.operators.maybe.MaybeFlatMapCompletable;
/*      */ import io.reactivex.internal.operators.maybe.MaybeFlatMapIterableFlowable;
/*      */ import io.reactivex.internal.operators.maybe.MaybeFlatMapIterableObservable;
/*      */ import io.reactivex.internal.operators.maybe.MaybeFlatMapNotification;
/*      */ import io.reactivex.internal.operators.maybe.MaybeFlatMapSingle;
/*      */ import io.reactivex.internal.operators.maybe.MaybeFlatMapSingleElement;
/*      */ import io.reactivex.internal.operators.maybe.MaybeFlatten;
/*      */ import io.reactivex.internal.operators.maybe.MaybeFromAction;
/*      */ import io.reactivex.internal.operators.maybe.MaybeFromCallable;
/*      */ import io.reactivex.internal.operators.maybe.MaybeFromCompletable;
/*      */ import io.reactivex.internal.operators.maybe.MaybeFromFuture;
/*      */ import io.reactivex.internal.operators.maybe.MaybeFromRunnable;
/*      */ import io.reactivex.internal.operators.maybe.MaybeFromSingle;
/*      */ import io.reactivex.internal.operators.maybe.MaybeHide;
/*      */ import io.reactivex.internal.operators.maybe.MaybeIgnoreElementCompletable;
/*      */ import io.reactivex.internal.operators.maybe.MaybeIsEmptySingle;
/*      */ import io.reactivex.internal.operators.maybe.MaybeJust;
/*      */ import io.reactivex.internal.operators.maybe.MaybeLift;
/*      */ import io.reactivex.internal.operators.maybe.MaybeMap;
/*      */ import io.reactivex.internal.operators.maybe.MaybeMaterialize;
/*      */ import io.reactivex.internal.operators.maybe.MaybeMergeArray;
/*      */ import io.reactivex.internal.operators.maybe.MaybeNever;
/*      */ import io.reactivex.internal.operators.maybe.MaybeObserveOn;
/*      */ import io.reactivex.internal.operators.maybe.MaybeOnErrorComplete;
/*      */ import io.reactivex.internal.operators.maybe.MaybeOnErrorNext;
/*      */ import io.reactivex.internal.operators.maybe.MaybeOnErrorReturn;
/*      */ import io.reactivex.internal.operators.maybe.MaybePeek;
/*      */ import io.reactivex.internal.operators.maybe.MaybeSubscribeOn;
/*      */ import io.reactivex.internal.operators.maybe.MaybeSwitchIfEmpty;
/*      */ import io.reactivex.internal.operators.maybe.MaybeSwitchIfEmptySingle;
/*      */ import io.reactivex.internal.operators.maybe.MaybeTakeUntilMaybe;
/*      */ import io.reactivex.internal.operators.maybe.MaybeTakeUntilPublisher;
/*      */ import io.reactivex.internal.operators.maybe.MaybeTimeoutMaybe;
/*      */ import io.reactivex.internal.operators.maybe.MaybeTimeoutPublisher;
/*      */ import io.reactivex.internal.operators.maybe.MaybeTimer;
/*      */ import io.reactivex.internal.operators.maybe.MaybeToFlowable;
/*      */ import io.reactivex.internal.operators.maybe.MaybeToObservable;
/*      */ import io.reactivex.internal.operators.maybe.MaybeToPublisher;
/*      */ import io.reactivex.internal.operators.maybe.MaybeToSingle;
/*      */ import io.reactivex.internal.operators.maybe.MaybeUnsafeCreate;
/*      */ import io.reactivex.internal.operators.maybe.MaybeUnsubscribeOn;
/*      */ import io.reactivex.internal.operators.maybe.MaybeUsing;
/*      */ import io.reactivex.internal.operators.maybe.MaybeZipArray;
/*      */ import io.reactivex.internal.operators.maybe.MaybeZipIterable;
/*      */ import io.reactivex.internal.operators.mixed.MaybeFlatMapObservable;
/*      */ import io.reactivex.internal.operators.mixed.MaybeFlatMapPublisher;
/*      */ import io.reactivex.internal.util.ErrorMode;
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
/*      */ public abstract class Maybe<T>
/*      */   implements MaybeSource<T>
/*      */ {
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   @NonNull
/*      */   public static <T> Maybe<T> amb(Iterable<? extends MaybeSource<? extends T>> sources) {
/*  130 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  131 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeAmb(null, sources));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Maybe<T> ambArray(MaybeSource<? extends T>... sources) {
/*  152 */     if (sources.length == 0) {
/*  153 */       return empty();
/*      */     }
/*  155 */     if (sources.length == 1) {
/*  156 */       return wrap((MaybeSource)sources[0]);
/*      */     }
/*  158 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeAmb((MaybeSource[])sources, null));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Flowable<T> concat(Iterable<? extends MaybeSource<? extends T>> sources) {
/*  181 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  182 */     return RxJavaPlugins.onAssembly((Flowable)new MaybeConcatIterable(sources));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Flowable<T> concat(MaybeSource<? extends T> source1, MaybeSource<? extends T> source2) {
/*  210 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  211 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  212 */     return concatArray((MaybeSource<? extends T>[])new MaybeSource[] { source1, source2 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Flowable<T> concat(MaybeSource<? extends T> source1, MaybeSource<? extends T> source2, MaybeSource<? extends T> source3) {
/*  243 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  244 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  245 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*  246 */     return concatArray((MaybeSource<? extends T>[])new MaybeSource[] { source1, source2, source3 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Flowable<T> concat(MaybeSource<? extends T> source1, MaybeSource<? extends T> source2, MaybeSource<? extends T> source3, MaybeSource<? extends T> source4) {
/*  279 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  280 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  281 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*  282 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/*  283 */     return concatArray((MaybeSource<? extends T>[])new MaybeSource[] { source1, source2, source3, source4 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Flowable<T> concat(Publisher<? extends MaybeSource<? extends T>> sources) {
/*  307 */     return concat(sources, 2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Flowable<T> concat(Publisher<? extends MaybeSource<? extends T>> sources, int prefetch) {
/*  334 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  335 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/*  336 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableConcatMapPublisher(sources, MaybeToPublisher.instance(), prefetch, ErrorMode.IMMEDIATE));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Flowable<T> concatArray(MaybeSource<? extends T>... sources) {
/*  359 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  360 */     if (sources.length == 0) {
/*  361 */       return Flowable.empty();
/*      */     }
/*  363 */     if (sources.length == 1) {
/*  364 */       return RxJavaPlugins.onAssembly((Flowable)new MaybeToFlowable(sources[0]));
/*      */     }
/*  366 */     return RxJavaPlugins.onAssembly((Flowable)new MaybeConcatArray((MaybeSource[])sources));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Flowable<T> concatArrayDelayError(MaybeSource<? extends T>... sources) {
/*  390 */     if (sources.length == 0) {
/*  391 */       return Flowable.empty();
/*      */     }
/*  393 */     if (sources.length == 1) {
/*  394 */       return RxJavaPlugins.onAssembly((Flowable)new MaybeToFlowable(sources[0]));
/*      */     }
/*  396 */     return RxJavaPlugins.onAssembly((Flowable)new MaybeConcatArrayDelayError((MaybeSource[])sources));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Flowable<T> concatArrayEager(MaybeSource<? extends T>... sources) {
/*  422 */     return Flowable.<MaybeSource<? extends T>>fromArray(sources).concatMapEager(MaybeToPublisher.instance());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Flowable<T> concatDelayError(Iterable<? extends MaybeSource<? extends T>> sources) {
/*  447 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  448 */     return Flowable.<MaybeSource<? extends T>>fromIterable(sources).concatMapDelayError(MaybeToPublisher.instance());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Flowable<T> concatDelayError(Publisher<? extends MaybeSource<? extends T>> sources) {
/*  472 */     return Flowable.<MaybeSource<? extends T>>fromPublisher(sources).concatMapDelayError(MaybeToPublisher.instance());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Flowable<T> concatEager(Iterable<? extends MaybeSource<? extends T>> sources) {
/*  498 */     return Flowable.<MaybeSource<? extends T>>fromIterable(sources).concatMapEager(MaybeToPublisher.instance());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Flowable<T> concatEager(Publisher<? extends MaybeSource<? extends T>> sources) {
/*  526 */     return Flowable.<MaybeSource<? extends T>>fromPublisher(sources).concatMapEager(MaybeToPublisher.instance());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Maybe<T> create(MaybeOnSubscribe<T> onSubscribe) {
/*  571 */     ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");
/*  572 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeCreate(onSubscribe));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Maybe<T> defer(Callable<? extends MaybeSource<? extends T>> maybeSupplier) {
/*  591 */     ObjectHelper.requireNonNull(maybeSupplier, "maybeSupplier is null");
/*  592 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeDefer(maybeSupplier));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Maybe<T> empty() {
/*  611 */     return RxJavaPlugins.onAssembly((Maybe)MaybeEmpty.INSTANCE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Maybe<T> error(Throwable exception) {
/*  636 */     ObjectHelper.requireNonNull(exception, "exception is null");
/*  637 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeError(exception));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Maybe<T> error(Callable<? extends Throwable> supplier) {
/*  662 */     ObjectHelper.requireNonNull(supplier, "errorSupplier is null");
/*  663 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeErrorCallable(supplier));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Maybe<T> fromAction(Action run) {
/*  689 */     ObjectHelper.requireNonNull(run, "run is null");
/*  690 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeFromAction(run));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Maybe<T> fromCompletable(CompletableSource completableSource) {
/*  709 */     ObjectHelper.requireNonNull(completableSource, "completableSource is null");
/*  710 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeFromCompletable(completableSource));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Maybe<T> fromSingle(SingleSource<T> singleSource) {
/*  729 */     ObjectHelper.requireNonNull(singleSource, "singleSource is null");
/*  730 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeFromSingle(singleSource));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Maybe<T> fromCallable(@NonNull Callable<? extends T> callable) {
/*  771 */     ObjectHelper.requireNonNull(callable, "callable is null");
/*  772 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeFromCallable(callable));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Maybe<T> fromFuture(Future<? extends T> future) {
/*  805 */     ObjectHelper.requireNonNull(future, "future is null");
/*  806 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeFromFuture(future, 0L, null));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Maybe<T> fromFuture(Future<? extends T> future, long timeout, TimeUnit unit) {
/*  843 */     ObjectHelper.requireNonNull(future, "future is null");
/*  844 */     ObjectHelper.requireNonNull(unit, "unit is null");
/*  845 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeFromFuture(future, timeout, unit));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Maybe<T> fromRunnable(Runnable run) {
/*  864 */     ObjectHelper.requireNonNull(run, "run is null");
/*  865 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeFromRunnable(run));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Maybe<T> just(T item) {
/*  891 */     ObjectHelper.requireNonNull(item, "item is null");
/*  892 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeJust(item));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Flowable<T> merge(Iterable<? extends MaybeSource<? extends T>> sources) {
/*  926 */     return merge(Flowable.fromIterable(sources));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Flowable<T> merge(Publisher<? extends MaybeSource<? extends T>> sources) {
/*  960 */     return merge(sources, 2147483647);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Flowable<T> merge(Publisher<? extends MaybeSource<? extends T>> sources, int maxConcurrency) {
/*  997 */     ObjectHelper.requireNonNull(sources, "source is null");
/*  998 */     ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
/*  999 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableFlatMapPublisher(sources, MaybeToPublisher.instance(), false, maxConcurrency, 1));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Maybe<T> merge(MaybeSource<? extends MaybeSource<? extends T>> source) {
/* 1030 */     ObjectHelper.requireNonNull(source, "source is null");
/* 1031 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeFlatten(source, Functions.identity()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Flowable<T> merge(MaybeSource<? extends T> source1, MaybeSource<? extends T> source2) {
/* 1078 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/* 1079 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/* 1080 */     return mergeArray((MaybeSource<? extends T>[])new MaybeSource[] { source1, source2 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Flowable<T> merge(MaybeSource<? extends T> source1, MaybeSource<? extends T> source2, MaybeSource<? extends T> source3) {
/* 1130 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/* 1131 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/* 1132 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/* 1133 */     return mergeArray((MaybeSource<? extends T>[])new MaybeSource[] { source1, source2, source3 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Flowable<T> merge(MaybeSource<? extends T> source1, MaybeSource<? extends T> source2, MaybeSource<? extends T> source3, MaybeSource<? extends T> source4) {
/* 1185 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/* 1186 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/* 1187 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/* 1188 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/* 1189 */     return mergeArray((MaybeSource<? extends T>[])new MaybeSource[] { source1, source2, source3, source4 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Flowable<T> mergeArray(MaybeSource<? extends T>... sources) {
/* 1225 */     ObjectHelper.requireNonNull(sources, "sources is null");
/* 1226 */     if (sources.length == 0) {
/* 1227 */       return Flowable.empty();
/*      */     }
/* 1229 */     if (sources.length == 1) {
/* 1230 */       return RxJavaPlugins.onAssembly((Flowable)new MaybeToFlowable(sources[0]));
/*      */     }
/* 1232 */     return RxJavaPlugins.onAssembly((Flowable)new MaybeMergeArray((MaybeSource[])sources));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Flowable<T> mergeArrayDelayError(MaybeSource<? extends T>... sources) {
/* 1267 */     if (sources.length == 0) {
/* 1268 */       return Flowable.empty();
/*      */     }
/* 1270 */     return Flowable.<MaybeSource<? extends T>>fromArray(sources).flatMap(MaybeToPublisher.instance(), true, sources.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Flowable<T> mergeDelayError(Iterable<? extends MaybeSource<? extends T>> sources) {
/* 1305 */     return Flowable.<MaybeSource<? extends T>>fromIterable(sources).flatMap(MaybeToPublisher.instance(), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Flowable<T> mergeDelayError(Publisher<? extends MaybeSource<? extends T>> sources) {
/* 1340 */     return mergeDelayError(sources, 2147483647);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Flowable<T> mergeDelayError(Publisher<? extends MaybeSource<? extends T>> sources, int maxConcurrency) {
/* 1379 */     ObjectHelper.requireNonNull(sources, "source is null");
/* 1380 */     ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
/* 1381 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableFlatMapPublisher(sources, MaybeToPublisher.instance(), true, maxConcurrency, 1));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Flowable<T> mergeDelayError(MaybeSource<? extends T> source1, MaybeSource<? extends T> source2) {
/* 1418 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/* 1419 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/* 1420 */     return mergeArrayDelayError((MaybeSource<? extends T>[])new MaybeSource[] { source1, source2 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Flowable<T> mergeDelayError(MaybeSource<? extends T> source1, MaybeSource<? extends T> source2, MaybeSource<? extends T> source3) {
/* 1461 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/* 1462 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/* 1463 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/* 1464 */     return mergeArrayDelayError((MaybeSource<? extends T>[])new MaybeSource[] { source1, source2, source3 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Flowable<T> mergeDelayError(MaybeSource<? extends T> source1, MaybeSource<? extends T> source2, MaybeSource<? extends T> source3, MaybeSource<? extends T> source4) {
/* 1508 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/* 1509 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/* 1510 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/* 1511 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/* 1512 */     return mergeArrayDelayError((MaybeSource<? extends T>[])new MaybeSource[] { source1, source2, source3, source4 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Maybe<T> never() {
/* 1535 */     return RxJavaPlugins.onAssembly((Maybe)MaybeNever.INSTANCE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Single<Boolean> sequenceEqual(MaybeSource<? extends T> source1, MaybeSource<? extends T> source2) {
/* 1560 */     return sequenceEqual(source1, source2, ObjectHelper.equalsPredicate());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Single<Boolean> sequenceEqual(MaybeSource<? extends T> source1, MaybeSource<? extends T> source2, BiPredicate<? super T, ? super T> isEqual) {
/* 1591 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/* 1592 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/* 1593 */     ObjectHelper.requireNonNull(isEqual, "isEqual is null");
/* 1594 */     return RxJavaPlugins.onAssembly((Single)new MaybeEqualSingle(source1, source2, isEqual));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static Maybe<Long> timer(long delay, TimeUnit unit) {
/* 1616 */     return timer(delay, unit, Schedulers.computation());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static Maybe<Long> timer(long delay, TimeUnit unit, Scheduler scheduler) {
/* 1641 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 1642 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/*      */     
/* 1644 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeTimer(Math.max(0L, delay), unit, scheduler));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Maybe<T> unsafeCreate(MaybeSource<T> onSubscribe) {
/* 1662 */     if (onSubscribe instanceof Maybe) {
/* 1663 */       throw new IllegalArgumentException("unsafeCreate(Maybe) should be upgraded");
/*      */     }
/* 1665 */     ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");
/* 1666 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeUnsafeCreate(onSubscribe));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T, D> Maybe<T> using(Callable<? extends D> resourceSupplier, Function<? super D, ? extends MaybeSource<? extends T>> sourceSupplier, Consumer<? super D> resourceDisposer) {
/* 1695 */     return using(resourceSupplier, sourceSupplier, resourceDisposer, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T, D> Maybe<T> using(Callable<? extends D> resourceSupplier, Function<? super D, ? extends MaybeSource<? extends T>> sourceSupplier, Consumer<? super D> resourceDisposer, boolean eager) {
/* 1731 */     ObjectHelper.requireNonNull(resourceSupplier, "resourceSupplier is null");
/* 1732 */     ObjectHelper.requireNonNull(sourceSupplier, "sourceSupplier is null");
/* 1733 */     ObjectHelper.requireNonNull(resourceDisposer, "disposer is null");
/* 1734 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeUsing(resourceSupplier, sourceSupplier, resourceDisposer, eager));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> Maybe<T> wrap(MaybeSource<T> source) {
/* 1752 */     if (source instanceof Maybe) {
/* 1753 */       return RxJavaPlugins.onAssembly((Maybe)source);
/*      */     }
/* 1755 */     ObjectHelper.requireNonNull(source, "onSubscribe is null");
/* 1756 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeUnsafeCreate(source));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T, R> Maybe<R> zip(Iterable<? extends MaybeSource<? extends T>> sources, Function<? super Object[], ? extends R> zipper) {
/* 1790 */     ObjectHelper.requireNonNull(zipper, "zipper is null");
/* 1791 */     ObjectHelper.requireNonNull(sources, "sources is null");
/* 1792 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeZipIterable(sources, zipper));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T1, T2, R> Maybe<R> zip(MaybeSource<? extends T1> source1, MaybeSource<? extends T2> source2, BiFunction<? super T1, ? super T2, ? extends R> zipper) {
/* 1827 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/* 1828 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/* 1829 */     return zipArray(Functions.toFunction(zipper), (MaybeSource<?>[])new MaybeSource[] { source1, source2 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T1, T2, T3, R> Maybe<R> zip(MaybeSource<? extends T1> source1, MaybeSource<? extends T2> source2, MaybeSource<? extends T3> source3, Function3<? super T1, ? super T2, ? super T3, ? extends R> zipper) {
/* 1867 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/* 1868 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/* 1869 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/* 1870 */     return zipArray(Functions.toFunction(zipper), (MaybeSource<?>[])new MaybeSource[] { source1, source2, source3 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T1, T2, T3, T4, R> Maybe<R> zip(MaybeSource<? extends T1> source1, MaybeSource<? extends T2> source2, MaybeSource<? extends T3> source3, MaybeSource<? extends T4> source4, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> zipper) {
/* 1912 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/* 1913 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/* 1914 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/* 1915 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/* 1916 */     return zipArray(Functions.toFunction(zipper), (MaybeSource<?>[])new MaybeSource[] { source1, source2, source3, source4 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T1, T2, T3, T4, T5, R> Maybe<R> zip(MaybeSource<? extends T1> source1, MaybeSource<? extends T2> source2, MaybeSource<? extends T3> source3, MaybeSource<? extends T4> source4, MaybeSource<? extends T5> source5, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> zipper) {
/* 1961 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/* 1962 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/* 1963 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/* 1964 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/* 1965 */     ObjectHelper.requireNonNull(source5, "source5 is null");
/* 1966 */     return zipArray(Functions.toFunction(zipper), (MaybeSource<?>[])new MaybeSource[] { source1, source2, source3, source4, source5 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T1, T2, T3, T4, T5, T6, R> Maybe<R> zip(MaybeSource<? extends T1> source1, MaybeSource<? extends T2> source2, MaybeSource<? extends T3> source3, MaybeSource<? extends T4> source4, MaybeSource<? extends T5> source5, MaybeSource<? extends T6> source6, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> zipper) {
/* 2014 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/* 2015 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/* 2016 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/* 2017 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/* 2018 */     ObjectHelper.requireNonNull(source5, "source5 is null");
/* 2019 */     ObjectHelper.requireNonNull(source6, "source6 is null");
/* 2020 */     return zipArray(Functions.toFunction(zipper), (MaybeSource<?>[])new MaybeSource[] { source1, source2, source3, source4, source5, source6 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T1, T2, T3, T4, T5, T6, T7, R> Maybe<R> zip(MaybeSource<? extends T1> source1, MaybeSource<? extends T2> source2, MaybeSource<? extends T3> source3, MaybeSource<? extends T4> source4, MaybeSource<? extends T5> source5, MaybeSource<? extends T6> source6, MaybeSource<? extends T7> source7, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> zipper) {
/* 2072 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/* 2073 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/* 2074 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/* 2075 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/* 2076 */     ObjectHelper.requireNonNull(source5, "source5 is null");
/* 2077 */     ObjectHelper.requireNonNull(source6, "source6 is null");
/* 2078 */     ObjectHelper.requireNonNull(source7, "source7 is null");
/* 2079 */     return zipArray(Functions.toFunction(zipper), (MaybeSource<?>[])new MaybeSource[] { source1, source2, source3, source4, source5, source6, source7 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Maybe<R> zip(MaybeSource<? extends T1> source1, MaybeSource<? extends T2> source2, MaybeSource<? extends T3> source3, MaybeSource<? extends T4> source4, MaybeSource<? extends T5> source5, MaybeSource<? extends T6> source6, MaybeSource<? extends T7> source7, MaybeSource<? extends T8> source8, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> zipper) {
/* 2134 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/* 2135 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/* 2136 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/* 2137 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/* 2138 */     ObjectHelper.requireNonNull(source5, "source5 is null");
/* 2139 */     ObjectHelper.requireNonNull(source6, "source6 is null");
/* 2140 */     ObjectHelper.requireNonNull(source7, "source7 is null");
/* 2141 */     ObjectHelper.requireNonNull(source8, "source8 is null");
/* 2142 */     return zipArray(Functions.toFunction(zipper), (MaybeSource<?>[])new MaybeSource[] { source1, source2, source3, source4, source5, source6, source7, source8 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Maybe<R> zip(MaybeSource<? extends T1> source1, MaybeSource<? extends T2> source2, MaybeSource<? extends T3> source3, MaybeSource<? extends T4> source4, MaybeSource<? extends T5> source5, MaybeSource<? extends T6> source6, MaybeSource<? extends T7> source7, MaybeSource<? extends T8> source8, MaybeSource<? extends T9> source9, Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> zipper) {
/* 2201 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/* 2202 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/* 2203 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/* 2204 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/* 2205 */     ObjectHelper.requireNonNull(source5, "source5 is null");
/* 2206 */     ObjectHelper.requireNonNull(source6, "source6 is null");
/* 2207 */     ObjectHelper.requireNonNull(source7, "source7 is null");
/* 2208 */     ObjectHelper.requireNonNull(source8, "source8 is null");
/* 2209 */     ObjectHelper.requireNonNull(source9, "source9 is null");
/* 2210 */     return zipArray(Functions.toFunction(zipper), (MaybeSource<?>[])new MaybeSource[] { source1, source2, source3, source4, source5, source6, source7, source8, source9 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T, R> Maybe<R> zipArray(Function<? super Object[], ? extends R> zipper, MaybeSource<? extends T>... sources) {
/* 2245 */     ObjectHelper.requireNonNull(sources, "sources is null");
/* 2246 */     if (sources.length == 0) {
/* 2247 */       return empty();
/*      */     }
/* 2249 */     ObjectHelper.requireNonNull(zipper, "zipper is null");
/* 2250 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeZipArray((MaybeSource[])sources, zipper));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> ambWith(MaybeSource<? extends T> other) {
/* 2278 */     ObjectHelper.requireNonNull(other, "other is null");
/* 2279 */     return ambArray((MaybeSource<? extends T>[])new MaybeSource[] { this, other });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final <R> R as(@NonNull MaybeConverter<T, ? extends R> converter) {
/* 2300 */     return ((MaybeConverter<T, R>)ObjectHelper.requireNonNull(converter, "converter is null")).apply(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/* 2319 */     BlockingMultiObserver<T> observer = new BlockingMultiObserver();
/* 2320 */     subscribe((MaybeObserver<? super T>)observer);
/* 2321 */     return (T)observer.blockingGet();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final T blockingGet(T defaultValue) {
/* 2341 */     ObjectHelper.requireNonNull(defaultValue, "defaultValue is null");
/* 2342 */     BlockingMultiObserver<T> observer = new BlockingMultiObserver();
/* 2343 */     subscribe((MaybeObserver<? super T>)observer);
/* 2344 */     return (T)observer.blockingGet(defaultValue);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> cache() {
/* 2369 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeCache(this));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final <U> Maybe<U> cast(Class<? extends U> clazz) {
/* 2387 */     ObjectHelper.requireNonNull(clazz, "clazz is null");
/* 2388 */     return map(Functions.castFunction(clazz));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final <R> Maybe<R> compose(MaybeTransformer<? super T, ? extends R> transformer) {
/* 2413 */     return wrap(((MaybeTransformer<T, R>)ObjectHelper.requireNonNull(transformer, "transformer is null")).apply(this));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final <R> Maybe<R> concatMap(Function<? super T, ? extends MaybeSource<? extends R>> mapper) {
/* 2436 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 2437 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeFlatten(this, mapper));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Flowable<T> concatWith(MaybeSource<? extends T> other) {
/* 2463 */     ObjectHelper.requireNonNull(other, "other is null");
/* 2464 */     return concat(this, other);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Single<Boolean> contains(Object item) {
/* 2487 */     ObjectHelper.requireNonNull(item, "item is null");
/* 2488 */     return RxJavaPlugins.onAssembly((Single)new MaybeContains(this, item));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Single<Long> count() {
/* 2509 */     return RxJavaPlugins.onAssembly((Single)new MaybeCount(this));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> defaultIfEmpty(T defaultItem) {
/* 2536 */     ObjectHelper.requireNonNull(defaultItem, "defaultItem is null");
/* 2537 */     return switchIfEmpty(just(defaultItem));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> delay(long delay, TimeUnit unit) {
/* 2560 */     return delay(delay, unit, Schedulers.computation());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> delay(long delay, TimeUnit unit, Scheduler scheduler) {
/* 2586 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 2587 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 2588 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeDelay(this, Math.max(0L, delay), unit, scheduler));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   @NonNull
/*      */   public final <U, V> Maybe<T> delay(Publisher<U> delayIndicator) {
/* 2618 */     ObjectHelper.requireNonNull(delayIndicator, "delayIndicator is null");
/* 2619 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeDelayOtherPublisher(this, delayIndicator));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final <U> Maybe<T> delaySubscription(Publisher<U> subscriptionIndicator) {
/* 2643 */     ObjectHelper.requireNonNull(subscriptionIndicator, "subscriptionIndicator is null");
/* 2644 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeDelaySubscriptionOtherPublisher(this, subscriptionIndicator));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> delaySubscription(long delay, TimeUnit unit) {
/* 2666 */     return delaySubscription(delay, unit, Schedulers.computation());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> delaySubscription(long delay, TimeUnit unit, Scheduler scheduler) {
/* 2692 */     return delaySubscription(Flowable.timer(delay, unit, scheduler));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> doAfterSuccess(Consumer<? super T> onAfterSuccess) {
/* 2712 */     ObjectHelper.requireNonNull(onAfterSuccess, "onAfterSuccess is null");
/* 2713 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeDoAfterSuccess(this, onAfterSuccess));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> doAfterTerminate(Action onAfterTerminate) {
/* 2737 */     return RxJavaPlugins.onAssembly((Maybe)new MaybePeek(this, 
/* 2738 */           Functions.emptyConsumer(), 
/* 2739 */           Functions.emptyConsumer(), 
/* 2740 */           Functions.emptyConsumer(), Functions.EMPTY_ACTION, 
/*      */           
/* 2742 */           (Action)ObjectHelper.requireNonNull(onAfterTerminate, "onAfterTerminate is null"), Functions.EMPTY_ACTION));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> doFinally(Action onFinally) {
/* 2767 */     ObjectHelper.requireNonNull(onFinally, "onFinally is null");
/* 2768 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeDoFinally(this, onFinally));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> doOnDispose(Action onDispose) {
/* 2786 */     return RxJavaPlugins.onAssembly((Maybe)new MaybePeek(this, 
/* 2787 */           Functions.emptyConsumer(), 
/* 2788 */           Functions.emptyConsumer(), 
/* 2789 */           Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, 
/*      */ 
/*      */           
/* 2792 */           (Action)ObjectHelper.requireNonNull(onDispose, "onDispose is null")));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> doOnComplete(Action onComplete) {
/* 2814 */     return RxJavaPlugins.onAssembly((Maybe)new MaybePeek(this, 
/* 2815 */           Functions.emptyConsumer(), 
/* 2816 */           Functions.emptyConsumer(), 
/* 2817 */           Functions.emptyConsumer(), 
/* 2818 */           (Action)ObjectHelper.requireNonNull(onComplete, "onComplete is null"), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> doOnError(Consumer<? super Throwable> onError) {
/* 2840 */     return RxJavaPlugins.onAssembly((Maybe)new MaybePeek(this, 
/* 2841 */           Functions.emptyConsumer(), 
/* 2842 */           Functions.emptyConsumer(), 
/* 2843 */           (Consumer)ObjectHelper.requireNonNull(onError, "onError is null"), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> doOnEvent(BiConsumer<? super T, ? super Throwable> onEvent) {
/* 2867 */     ObjectHelper.requireNonNull(onEvent, "onEvent is null");
/* 2868 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeDoOnEvent(this, onEvent));
/*      */   }
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> doOnSubscribe(Consumer<? super Disposable> onSubscribe) {
/* 2885 */     return RxJavaPlugins.onAssembly((Maybe)new MaybePeek(this, 
/* 2886 */           (Consumer)ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null"), 
/* 2887 */           Functions.emptyConsumer(), 
/* 2888 */           Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> doOnTerminate(Action onTerminate) {
/* 2918 */     ObjectHelper.requireNonNull(onTerminate, "onTerminate is null");
/* 2919 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeDoOnTerminate(this, onTerminate));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> doOnSuccess(Consumer<? super T> onSuccess) {
/* 2938 */     return RxJavaPlugins.onAssembly((Maybe)new MaybePeek(this, 
/* 2939 */           Functions.emptyConsumer(), 
/* 2940 */           (Consumer)ObjectHelper.requireNonNull(onSuccess, "onSuccess is null"), 
/* 2941 */           Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/* 2969 */     ObjectHelper.requireNonNull(predicate, "predicate is null");
/* 2970 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeFilter(this, predicate));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final <R> Maybe<R> flatMap(Function<? super T, ? extends MaybeSource<? extends R>> mapper) {
/* 2994 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 2995 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeFlatten(this, mapper));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final <R> Maybe<R> flatMap(Function<? super T, ? extends MaybeSource<? extends R>> onSuccessMapper, Function<? super Throwable, ? extends MaybeSource<? extends R>> onErrorMapper, Callable<? extends MaybeSource<? extends R>> onCompleteSupplier) {
/* 3026 */     ObjectHelper.requireNonNull(onSuccessMapper, "onSuccessMapper is null");
/* 3027 */     ObjectHelper.requireNonNull(onErrorMapper, "onErrorMapper is null");
/* 3028 */     ObjectHelper.requireNonNull(onCompleteSupplier, "onCompleteSupplier is null");
/* 3029 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeFlatMapNotification(this, onSuccessMapper, onErrorMapper, onCompleteSupplier));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final <U, R> Maybe<R> flatMap(Function<? super T, ? extends MaybeSource<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends R> resultSelector) {
/* 3059 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 3060 */     ObjectHelper.requireNonNull(resultSelector, "resultSelector is null");
/* 3061 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeFlatMapBiSelector(this, mapper, resultSelector));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/* 3089 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 3090 */     return RxJavaPlugins.onAssembly((Flowable)new MaybeFlatMapIterableFlowable(this, mapper));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/* 3115 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 3116 */     return RxJavaPlugins.onAssembly((Observable)new MaybeFlatMapIterableObservable(this, mapper));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/* 3139 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 3140 */     return RxJavaPlugins.onAssembly((Observable)new MaybeFlatMapObservable(this, mapper));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/* 3167 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 3168 */     return RxJavaPlugins.onAssembly((Flowable)new MaybeFlatMapPublisher(this, mapper));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final <R> Single<R> flatMapSingle(Function<? super T, ? extends SingleSource<? extends R>> mapper) {
/* 3193 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 3194 */     return RxJavaPlugins.onAssembly((Single)new MaybeFlatMapSingle(this, mapper));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final <R> Maybe<R> flatMapSingleElement(Function<? super T, ? extends SingleSource<? extends R>> mapper) {
/* 3221 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 3222 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeFlatMapSingleElement(this, mapper));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/* 3245 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 3246 */     return RxJavaPlugins.onAssembly((Completable)new MaybeFlatMapCompletable(this, mapper));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> hide() {
/* 3264 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeHide(this));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/* 3283 */     return RxJavaPlugins.onAssembly((Completable)new MaybeIgnoreElementCompletable(this));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Single<Boolean> isEmpty() {
/* 3301 */     return RxJavaPlugins.onAssembly((Single)new MaybeIsEmptySingle(this));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final <R> Maybe<R> lift(MaybeOperator<? extends R, ? super T> lift) {
/* 3455 */     ObjectHelper.requireNonNull(lift, "lift is null");
/* 3456 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeLift(this, lift));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final <R> Maybe<R> map(Function<? super T, ? extends R> mapper) {
/* 3479 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 3480 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeMap(this, mapper));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/* 3500 */     return RxJavaPlugins.onAssembly((Single)new MaybeMaterialize(this));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Flowable<T> mergeWith(MaybeSource<? extends T> other) {
/* 3527 */     ObjectHelper.requireNonNull(other, "other is null");
/* 3528 */     return merge(this, other);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> observeOn(Scheduler scheduler) {
/* 3553 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 3554 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeObserveOn(this, scheduler));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final <U> Maybe<U> ofType(Class<U> clazz) {
/* 3577 */     ObjectHelper.requireNonNull(clazz, "clazz is null");
/* 3578 */     return filter(Functions.isInstanceOf(clazz)).cast(clazz);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final <R> R to(Function<? super Maybe<T>, R> convert) {
/*      */     try {
/* 3599 */       return (R)((Function)ObjectHelper.requireNonNull(convert, "convert is null")).apply(this);
/* 3600 */     } catch (Throwable ex) {
/* 3601 */       Exceptions.throwIfFatal(ex);
/* 3602 */       throw ExceptionHelper.wrapOrThrow(ex);
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
/*      */   @BackpressureSupport(BackpressureKind.FULL)
/*      */   @CheckReturnValue
/*      */   @SchedulerSupport("none")
/*      */   public final Flowable<T> toFlowable() {
/* 3622 */     if (this instanceof FuseToFlowable) {
/* 3623 */       return ((FuseToFlowable)this).fuseToFlowable();
/*      */     }
/* 3625 */     return RxJavaPlugins.onAssembly((Flowable)new MaybeToFlowable(this));
/*      */   }
/*      */ 
/*      */ 
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
/* 3641 */     if (this instanceof FuseToObservable) {
/* 3642 */       return ((FuseToObservable)this).fuseToObservable();
/*      */     }
/* 3644 */     return RxJavaPlugins.onAssembly((Observable)new MaybeToObservable(this));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Single<T> toSingle(T defaultValue) {
/* 3662 */     ObjectHelper.requireNonNull(defaultValue, "defaultValue is null");
/* 3663 */     return RxJavaPlugins.onAssembly((Single)new MaybeToSingle(this, defaultValue));
/*      */   }
/*      */ 
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
/*      */   public final Single<T> toSingle() {
/* 3678 */     return RxJavaPlugins.onAssembly((Single)new MaybeToSingle(this, null));
/*      */   }
/*      */ 
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
/*      */   public final Maybe<T> onErrorComplete() {
/* 3693 */     return onErrorComplete(Functions.alwaysTrue());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> onErrorComplete(Predicate<? super Throwable> predicate) {
/* 3711 */     ObjectHelper.requireNonNull(predicate, "predicate is null");
/*      */     
/* 3713 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeOnErrorComplete(this, predicate));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> onErrorResumeNext(MaybeSource<? extends T> next) {
/* 3739 */     ObjectHelper.requireNonNull(next, "next is null");
/* 3740 */     return onErrorResumeNext(Functions.justFunction(next));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> onErrorResumeNext(Function<? super Throwable, ? extends MaybeSource<? extends T>> resumeFunction) {
/* 3766 */     ObjectHelper.requireNonNull(resumeFunction, "resumeFunction is null");
/* 3767 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeOnErrorNext(this, resumeFunction, true));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> onErrorReturn(Function<? super Throwable, ? extends T> valueSupplier) {
/* 3793 */     ObjectHelper.requireNonNull(valueSupplier, "valueSupplier is null");
/* 3794 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeOnErrorReturn(this, valueSupplier));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> onErrorReturnItem(T item) {
/* 3819 */     ObjectHelper.requireNonNull(item, "item is null");
/* 3820 */     return onErrorReturn(Functions.justFunction(item));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> onExceptionResumeNext(MaybeSource<? extends T> next) {
/* 3849 */     ObjectHelper.requireNonNull(next, "next is null");
/* 3850 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeOnErrorNext(this, Functions.justFunction(next), false));
/*      */   }
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> onTerminateDetach() {
/* 3866 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeDetach(this));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/* 3887 */     return repeat(Long.MAX_VALUE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/* 3915 */     return toFlowable().repeat(times);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/* 3942 */     return toFlowable().repeatUntil(stop);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/* 3971 */     return toFlowable().repeatWhen(handler);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> retry() {
/* 3993 */     return retry(Long.MAX_VALUE, Functions.alwaysTrue());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> retry(BiPredicate<? super Integer, ? super Throwable> predicate) {
/* 4016 */     return toFlowable().retry(predicate).singleElement();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> retry(long count) {
/* 4041 */     return retry(count, Functions.alwaysTrue());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> retry(long times, Predicate<? super Throwable> predicate) {
/* 4058 */     return toFlowable().retry(times, predicate).singleElement();
/*      */   }
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> retry(Predicate<? super Throwable> predicate) {
/* 4074 */     return retry(Long.MAX_VALUE, predicate);
/*      */   }
/*      */ 
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
/*      */   public final Maybe<T> retryUntil(BooleanSupplier stop) {
/* 4090 */     ObjectHelper.requireNonNull(stop, "stop is null");
/* 4091 */     return retry(Long.MAX_VALUE, Functions.predicateReverseFor(stop));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> retryWhen(Function<? super Flowable<Throwable>, ? extends Publisher<?>> handler) {
/* 4171 */     return toFlowable().retryWhen(handler).singleElement();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/* 4191 */     return subscribe(Functions.emptyConsumer(), Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/* 4216 */     return subscribe(onSuccess, Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Disposable subscribe(Consumer<? super T> onSuccess, Consumer<? super Throwable> onError) {
/* 4242 */     return subscribe(onSuccess, onError, Functions.EMPTY_ACTION);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Disposable subscribe(Consumer<? super T> onSuccess, Consumer<? super Throwable> onError, Action onComplete) {
/* 4274 */     ObjectHelper.requireNonNull(onSuccess, "onSuccess is null");
/* 4275 */     ObjectHelper.requireNonNull(onError, "onError is null");
/* 4276 */     ObjectHelper.requireNonNull(onComplete, "onComplete is null");
/* 4277 */     return (Disposable)subscribeWith(new MaybeCallbackObserver(onSuccess, onError, onComplete));
/*      */   }
/*      */ 
/*      */   
/*      */   @SchedulerSupport("none")
/*      */   public final void subscribe(MaybeObserver<? super T> observer) {
/* 4283 */     ObjectHelper.requireNonNull(observer, "observer is null");
/*      */     
/* 4285 */     observer = RxJavaPlugins.onSubscribe(this, observer);
/*      */     
/* 4287 */     ObjectHelper.requireNonNull(observer, "The RxJavaPlugins.onSubscribe hook returned a null MaybeObserver. Please check the handler provided to RxJavaPlugins.setOnMaybeSubscribe for invalid null returns. Further reading: https://github.com/ReactiveX/RxJava/wiki/Plugins");
/*      */     
/*      */     try {
/* 4290 */       subscribeActual(observer);
/* 4291 */     } catch (NullPointerException ex) {
/* 4292 */       throw ex;
/* 4293 */     } catch (Throwable ex) {
/* 4294 */       Exceptions.throwIfFatal(ex);
/* 4295 */       NullPointerException npe = new NullPointerException("subscribeActual failed");
/* 4296 */       npe.initCause(ex);
/* 4297 */       throw npe;
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
/*      */   protected abstract void subscribeActual(MaybeObserver<? super T> paramMaybeObserver);
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> subscribeOn(Scheduler scheduler) {
/* 4330 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 4331 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeSubscribeOn(this, scheduler));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final <E extends MaybeObserver<? super T>> E subscribeWith(E observer) {
/* 4360 */     subscribe((MaybeObserver<? super T>)observer);
/* 4361 */     return observer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> switchIfEmpty(MaybeSource<? extends T> other) {
/* 4383 */     ObjectHelper.requireNonNull(other, "other is null");
/* 4384 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeSwitchIfEmpty(this, other));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Single<T> switchIfEmpty(SingleSource<? extends T> other) {
/* 4407 */     ObjectHelper.requireNonNull(other, "other is null");
/* 4408 */     return RxJavaPlugins.onAssembly((Single)new MaybeSwitchIfEmptySingle(this, other));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final <U> Maybe<T> takeUntil(MaybeSource<U> other) {
/* 4433 */     ObjectHelper.requireNonNull(other, "other is null");
/* 4434 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeTakeUntilMaybe(this, other));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final <U> Maybe<T> takeUntil(Publisher<U> other) {
/* 4463 */     ObjectHelper.requireNonNull(other, "other is null");
/* 4464 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeTakeUntilPublisher(this, other));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> timeout(long timeout, TimeUnit timeUnit) {
/* 4488 */     return timeout(timeout, timeUnit, Schedulers.computation());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> timeout(long timeout, TimeUnit timeUnit, MaybeSource<? extends T> fallback) {
/* 4515 */     ObjectHelper.requireNonNull(fallback, "fallback is null");
/* 4516 */     return timeout(timeout, timeUnit, Schedulers.computation(), fallback);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> timeout(long timeout, TimeUnit timeUnit, Scheduler scheduler, MaybeSource<? extends T> fallback) {
/* 4546 */     ObjectHelper.requireNonNull(fallback, "fallback is null");
/* 4547 */     return timeout(timer(timeout, timeUnit, scheduler), fallback);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> timeout(long timeout, TimeUnit timeUnit, Scheduler scheduler) {
/* 4574 */     return timeout(timer(timeout, timeUnit, scheduler));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final <U> Maybe<T> timeout(MaybeSource<U> timeoutIndicator) {
/* 4593 */     ObjectHelper.requireNonNull(timeoutIndicator, "timeoutIndicator is null");
/* 4594 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeTimeoutMaybe(this, timeoutIndicator, null));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final <U> Maybe<T> timeout(MaybeSource<U> timeoutIndicator, MaybeSource<? extends T> fallback) {
/* 4615 */     ObjectHelper.requireNonNull(timeoutIndicator, "timeoutIndicator is null");
/* 4616 */     ObjectHelper.requireNonNull(fallback, "fallback is null");
/* 4617 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeTimeoutMaybe(this, timeoutIndicator, fallback));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final <U> Maybe<T> timeout(Publisher<U> timeoutIndicator) {
/* 4640 */     ObjectHelper.requireNonNull(timeoutIndicator, "timeoutIndicator is null");
/* 4641 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeTimeoutPublisher(this, timeoutIndicator, null));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final <U> Maybe<T> timeout(Publisher<U> timeoutIndicator, MaybeSource<? extends T> fallback) {
/* 4666 */     ObjectHelper.requireNonNull(timeoutIndicator, "timeoutIndicator is null");
/* 4667 */     ObjectHelper.requireNonNull(fallback, "fallback is null");
/* 4668 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeTimeoutPublisher(this, timeoutIndicator, fallback));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final Maybe<T> unsubscribeOn(Scheduler scheduler) {
/* 4686 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 4687 */     return RxJavaPlugins.onAssembly((Maybe)new MaybeUnsubscribeOn(this, scheduler));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public final <U, R> Maybe<R> zipWith(MaybeSource<? extends U> other, BiFunction<? super T, ? super U, ? extends R> zipper) {
/* 4720 */     ObjectHelper.requireNonNull(other, "other is null");
/* 4721 */     return zip(this, other, zipper);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/* 4740 */     TestObserver<T> to = new TestObserver();
/* 4741 */     subscribe((MaybeObserver<? super T>)to);
/* 4742 */     return to;
/*      */   }
/*      */ 
/*      */ 
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
/* 4758 */     TestObserver<T> to = new TestObserver();
/*      */     
/* 4760 */     if (cancelled) {
/* 4761 */       to.cancel();
/*      */     }
/*      */     
/* 4764 */     subscribe((MaybeObserver<? super T>)to);
/* 4765 */     return to;
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/Maybe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */