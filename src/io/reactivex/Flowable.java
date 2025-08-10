/*       */ package io.reactivex;
/*       */ import io.reactivex.annotations.BackpressureKind;
/*       */ import io.reactivex.annotations.BackpressureSupport;
/*       */ import io.reactivex.annotations.CheckReturnValue;
/*       */ import io.reactivex.annotations.NonNull;
/*       */ import io.reactivex.annotations.SchedulerSupport;
/*       */ import io.reactivex.disposables.Disposable;
/*       */ import io.reactivex.exceptions.Exceptions;
/*       */ import io.reactivex.flowables.ConnectableFlowable;
/*       */ import io.reactivex.flowables.GroupedFlowable;
/*       */ import io.reactivex.functions.Action;
/*       */ import io.reactivex.functions.BiConsumer;
/*       */ import io.reactivex.functions.BiFunction;
/*       */ import io.reactivex.functions.BiPredicate;
/*       */ import io.reactivex.functions.BooleanSupplier;
/*       */ import io.reactivex.functions.Consumer;
/*       */ import io.reactivex.functions.Function;
/*       */ import io.reactivex.functions.Function3;
/*       */ import io.reactivex.functions.Function4;
/*       */ import io.reactivex.functions.Function5;
/*       */ import io.reactivex.functions.Function6;
/*       */ import io.reactivex.functions.Function7;
/*       */ import io.reactivex.functions.Function8;
/*       */ import io.reactivex.functions.Function9;
/*       */ import io.reactivex.functions.LongConsumer;
/*       */ import io.reactivex.functions.Predicate;
/*       */ import io.reactivex.internal.functions.Functions;
/*       */ import io.reactivex.internal.functions.ObjectHelper;
/*       */ import io.reactivex.internal.fuseable.ScalarCallable;
/*       */ import io.reactivex.internal.operators.flowable.BlockingFlowableLatest;
/*       */ import io.reactivex.internal.operators.flowable.BlockingFlowableMostRecent;
/*       */ import io.reactivex.internal.operators.flowable.BlockingFlowableNext;
/*       */ import io.reactivex.internal.operators.flowable.FlowableAmb;
/*       */ import io.reactivex.internal.operators.flowable.FlowableBlockingSubscribe;
/*       */ import io.reactivex.internal.operators.flowable.FlowableBufferBoundary;
/*       */ import io.reactivex.internal.operators.flowable.FlowableBufferBoundarySupplier;
/*       */ import io.reactivex.internal.operators.flowable.FlowableBufferTimed;
/*       */ import io.reactivex.internal.operators.flowable.FlowableCache;
/*       */ import io.reactivex.internal.operators.flowable.FlowableCollectSingle;
/*       */ import io.reactivex.internal.operators.flowable.FlowableCombineLatest;
/*       */ import io.reactivex.internal.operators.flowable.FlowableConcatArray;
/*       */ import io.reactivex.internal.operators.flowable.FlowableConcatMap;
/*       */ import io.reactivex.internal.operators.flowable.FlowableConcatMapEager;
/*       */ import io.reactivex.internal.operators.flowable.FlowableConcatMapEagerPublisher;
/*       */ import io.reactivex.internal.operators.flowable.FlowableConcatWithCompletable;
/*       */ import io.reactivex.internal.operators.flowable.FlowableConcatWithMaybe;
/*       */ import io.reactivex.internal.operators.flowable.FlowableConcatWithSingle;
/*       */ import io.reactivex.internal.operators.flowable.FlowableCountSingle;
/*       */ import io.reactivex.internal.operators.flowable.FlowableDebounceTimed;
/*       */ import io.reactivex.internal.operators.flowable.FlowableDefer;
/*       */ import io.reactivex.internal.operators.flowable.FlowableDelay;
/*       */ import io.reactivex.internal.operators.flowable.FlowableDematerialize;
/*       */ import io.reactivex.internal.operators.flowable.FlowableDetach;
/*       */ import io.reactivex.internal.operators.flowable.FlowableDistinct;
/*       */ import io.reactivex.internal.operators.flowable.FlowableDistinctUntilChanged;
/*       */ import io.reactivex.internal.operators.flowable.FlowableDoFinally;
/*       */ import io.reactivex.internal.operators.flowable.FlowableDoOnEach;
/*       */ import io.reactivex.internal.operators.flowable.FlowableElementAtMaybe;
/*       */ import io.reactivex.internal.operators.flowable.FlowableElementAtSingle;
/*       */ import io.reactivex.internal.operators.flowable.FlowableEmpty;
/*       */ import io.reactivex.internal.operators.flowable.FlowableError;
/*       */ import io.reactivex.internal.operators.flowable.FlowableFilter;
/*       */ import io.reactivex.internal.operators.flowable.FlowableFlatMap;
/*       */ import io.reactivex.internal.operators.flowable.FlowableFlatMapMaybe;
/*       */ import io.reactivex.internal.operators.flowable.FlowableFlattenIterable;
/*       */ import io.reactivex.internal.operators.flowable.FlowableFromArray;
/*       */ import io.reactivex.internal.operators.flowable.FlowableFromFuture;
/*       */ import io.reactivex.internal.operators.flowable.FlowableFromIterable;
/*       */ import io.reactivex.internal.operators.flowable.FlowableFromPublisher;
/*       */ import io.reactivex.internal.operators.flowable.FlowableGenerate;
/*       */ import io.reactivex.internal.operators.flowable.FlowableGroupBy;
/*       */ import io.reactivex.internal.operators.flowable.FlowableGroupJoin;
/*       */ import io.reactivex.internal.operators.flowable.FlowableIgnoreElements;
/*       */ import io.reactivex.internal.operators.flowable.FlowableInternalHelper;
/*       */ import io.reactivex.internal.operators.flowable.FlowableIntervalRange;
/*       */ import io.reactivex.internal.operators.flowable.FlowableJust;
/*       */ import io.reactivex.internal.operators.flowable.FlowableLastSingle;
/*       */ import io.reactivex.internal.operators.flowable.FlowableLift;
/*       */ import io.reactivex.internal.operators.flowable.FlowableLimit;
/*       */ import io.reactivex.internal.operators.flowable.FlowableMapNotification;
/*       */ import io.reactivex.internal.operators.flowable.FlowableMaterialize;
/*       */ import io.reactivex.internal.operators.flowable.FlowableMergeWithMaybe;
/*       */ import io.reactivex.internal.operators.flowable.FlowableOnBackpressureBuffer;
/*       */ import io.reactivex.internal.operators.flowable.FlowableOnBackpressureBufferStrategy;
/*       */ import io.reactivex.internal.operators.flowable.FlowableOnBackpressureDrop;
/*       */ import io.reactivex.internal.operators.flowable.FlowableOnBackpressureLatest;
/*       */ import io.reactivex.internal.operators.flowable.FlowableOnErrorNext;
/*       */ import io.reactivex.internal.operators.flowable.FlowableOnErrorReturn;
/*       */ import io.reactivex.internal.operators.flowable.FlowablePublishMulticast;
/*       */ import io.reactivex.internal.operators.flowable.FlowableReduceMaybe;
/*       */ import io.reactivex.internal.operators.flowable.FlowableReduceSeedSingle;
/*       */ import io.reactivex.internal.operators.flowable.FlowableRepeat;
/*       */ import io.reactivex.internal.operators.flowable.FlowableRepeatWhen;
/*       */ import io.reactivex.internal.operators.flowable.FlowableReplay;
/*       */ import io.reactivex.internal.operators.flowable.FlowableRetryPredicate;
/*       */ import io.reactivex.internal.operators.flowable.FlowableRetryWhen;
/*       */ import io.reactivex.internal.operators.flowable.FlowableSamplePublisher;
/*       */ import io.reactivex.internal.operators.flowable.FlowableSampleTimed;
/*       */ import io.reactivex.internal.operators.flowable.FlowableScalarXMap;
/*       */ import io.reactivex.internal.operators.flowable.FlowableScan;
/*       */ import io.reactivex.internal.operators.flowable.FlowableScanSeed;
/*       */ import io.reactivex.internal.operators.flowable.FlowableSingleSingle;
/*       */ import io.reactivex.internal.operators.flowable.FlowableSkipLastTimed;
/*       */ import io.reactivex.internal.operators.flowable.FlowableSkipUntil;
/*       */ import io.reactivex.internal.operators.flowable.FlowableSwitchMap;
/*       */ import io.reactivex.internal.operators.flowable.FlowableTake;
/*       */ import io.reactivex.internal.operators.flowable.FlowableTakeLastOne;
/*       */ import io.reactivex.internal.operators.flowable.FlowableTakeLastTimed;
/*       */ import io.reactivex.internal.operators.flowable.FlowableTakeUntil;
/*       */ import io.reactivex.internal.operators.flowable.FlowableTakeUntilPredicate;
/*       */ import io.reactivex.internal.operators.flowable.FlowableTakeWhile;
/*       */ import io.reactivex.internal.operators.flowable.FlowableThrottleLatest;
/*       */ import io.reactivex.internal.operators.flowable.FlowableTimeout;
/*       */ import io.reactivex.internal.operators.flowable.FlowableToListSingle;
/*       */ import io.reactivex.internal.operators.flowable.FlowableUsing;
/*       */ import io.reactivex.internal.operators.flowable.FlowableWindow;
/*       */ import io.reactivex.internal.operators.flowable.FlowableWindowBoundarySupplier;
/*       */ import io.reactivex.internal.operators.flowable.FlowableWindowTimed;
/*       */ import io.reactivex.internal.operators.flowable.FlowableWithLatestFromMany;
/*       */ import io.reactivex.internal.operators.flowable.FlowableZip;
/*       */ import io.reactivex.internal.operators.mixed.FlowableConcatMapCompletable;
/*       */ import io.reactivex.internal.operators.mixed.FlowableConcatMapMaybe;
/*       */ import io.reactivex.internal.operators.mixed.FlowableConcatMapSingle;
/*       */ import io.reactivex.internal.operators.mixed.FlowableSwitchMapCompletable;
/*       */ import io.reactivex.internal.operators.mixed.FlowableSwitchMapMaybe;
/*       */ import io.reactivex.internal.operators.mixed.FlowableSwitchMapSingle;
/*       */ import io.reactivex.internal.schedulers.ImmediateThinScheduler;
/*       */ import io.reactivex.internal.subscribers.BlockingFirstSubscriber;
/*       */ import io.reactivex.internal.subscribers.BlockingLastSubscriber;
/*       */ import io.reactivex.internal.subscribers.ForEachWhileSubscriber;
/*       */ import io.reactivex.internal.subscribers.LambdaSubscriber;
/*       */ import io.reactivex.internal.subscribers.StrictSubscriber;
/*       */ import io.reactivex.internal.util.ArrayListSupplier;
/*       */ import io.reactivex.internal.util.ErrorMode;
/*       */ import io.reactivex.internal.util.ExceptionHelper;
/*       */ import io.reactivex.internal.util.HashMapSupplier;
/*       */ import io.reactivex.parallel.ParallelFlowable;
/*       */ import io.reactivex.plugins.RxJavaPlugins;
/*       */ import io.reactivex.schedulers.Schedulers;
/*       */ import io.reactivex.schedulers.Timed;
/*       */ import io.reactivex.subscribers.TestSubscriber;
/*       */ import java.util.Collection;
/*       */ import java.util.Comparator;
/*       */ import java.util.Iterator;
/*       */ import java.util.List;
/*       */ import java.util.Map;
/*       */ import java.util.NoSuchElementException;
/*       */ import java.util.concurrent.Callable;
/*       */ import java.util.concurrent.Future;
/*       */ import java.util.concurrent.TimeUnit;
/*       */ import org.reactivestreams.Publisher;
/*       */ import org.reactivestreams.Subscriber;
/*       */ import org.reactivestreams.Subscription;
/*       */ 
/*       */ public abstract class Flowable<T> implements Publisher<T> {
/*   156 */   static final int BUFFER_SIZE = Math.max(1, Integer.getInteger("rx2.buffer-size", 128).intValue());
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> amb(Iterable<? extends Publisher<? extends T>> sources) {
/*   185 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*   186 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableAmb(null, sources));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> ambArray(Publisher<? extends T>... sources) {
/*   215 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*   216 */     int len = sources.length;
/*   217 */     if (len == 0) {
/*   218 */       return empty();
/*       */     }
/*   220 */     if (len == 1) {
/*   221 */       return fromPublisher(sources[0]);
/*       */     }
/*   223 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableAmb((Publisher[])sources, null));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public static int bufferSize() {
/*   233 */     return BUFFER_SIZE;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @SchedulerSupport("none")
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   public static <T, R> Flowable<R> combineLatest(Publisher<? extends T>[] sources, Function<? super Object[], ? extends R> combiner) {
/*   277 */     return combineLatest(sources, combiner, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @SchedulerSupport("none")
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   public static <T, R> Flowable<R> combineLatest(Function<? super Object[], ? extends R> combiner, Publisher<? extends T>... sources) {
/*   321 */     return combineLatest(sources, combiner, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @SchedulerSupport("none")
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @NonNull
/*       */   public static <T, R> Flowable<R> combineLatest(Publisher<? extends T>[] sources, Function<? super Object[], ? extends R> combiner, int bufferSize) {
/*   368 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*   369 */     if (sources.length == 0) {
/*   370 */       return empty();
/*       */     }
/*   372 */     ObjectHelper.requireNonNull(combiner, "combiner is null");
/*   373 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/*   374 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableCombineLatest((Publisher[])sources, combiner, bufferSize, false));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @SchedulerSupport("none")
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   public static <T, R> Flowable<R> combineLatest(Iterable<? extends Publisher<? extends T>> sources, Function<? super Object[], ? extends R> combiner) {
/*   419 */     return combineLatest(sources, combiner, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @SchedulerSupport("none")
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @NonNull
/*       */   public static <T, R> Flowable<R> combineLatest(Iterable<? extends Publisher<? extends T>> sources, Function<? super Object[], ? extends R> combiner, int bufferSize) {
/*   467 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*   468 */     ObjectHelper.requireNonNull(combiner, "combiner is null");
/*   469 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/*   470 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableCombineLatest(sources, combiner, bufferSize, false));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @SchedulerSupport("none")
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   public static <T, R> Flowable<R> combineLatestDelayError(Publisher<? extends T>[] sources, Function<? super Object[], ? extends R> combiner) {
/*   515 */     return combineLatestDelayError(sources, combiner, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @SchedulerSupport("none")
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   public static <T, R> Flowable<R> combineLatestDelayError(Function<? super Object[], ? extends R> combiner, Publisher<? extends T>... sources) {
/*   561 */     return combineLatestDelayError(sources, combiner, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @SchedulerSupport("none")
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   public static <T, R> Flowable<R> combineLatestDelayError(Function<? super Object[], ? extends R> combiner, int bufferSize, Publisher<? extends T>... sources) {
/*   609 */     return combineLatestDelayError(sources, combiner, bufferSize);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @SchedulerSupport("none")
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @NonNull
/*       */   public static <T, R> Flowable<R> combineLatestDelayError(Publisher<? extends T>[] sources, Function<? super Object[], ? extends R> combiner, int bufferSize) {
/*   658 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*   659 */     ObjectHelper.requireNonNull(combiner, "combiner is null");
/*   660 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/*   661 */     if (sources.length == 0) {
/*   662 */       return empty();
/*       */     }
/*   664 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableCombineLatest((Publisher[])sources, combiner, bufferSize, true));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @SchedulerSupport("none")
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   public static <T, R> Flowable<R> combineLatestDelayError(Iterable<? extends Publisher<? extends T>> sources, Function<? super Object[], ? extends R> combiner) {
/*   710 */     return combineLatestDelayError(sources, combiner, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @SchedulerSupport("none")
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   public static <T, R> Flowable<R> combineLatestDelayError(Iterable<? extends Publisher<? extends T>> sources, Function<? super Object[], ? extends R> combiner, int bufferSize) {
/*   758 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*   759 */     ObjectHelper.requireNonNull(combiner, "combiner is null");
/*   760 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/*   761 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableCombineLatest(sources, combiner, bufferSize, true));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public static <T1, T2, R> Flowable<R> combineLatest(Publisher<? extends T1> source1, Publisher<? extends T2> source2, BiFunction<? super T1, ? super T2, ? extends R> combiner) {
/*   803 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*   804 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*   805 */     Function<Object[], R> f = Functions.toFunction(combiner);
/*   806 */     return combineLatest(f, (Publisher<?>[])new Publisher[] { source1, source2 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T1, T2, T3, R> Flowable<R> combineLatest(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Function3<? super T1, ? super T2, ? super T3, ? extends R> combiner) {
/*   853 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*   854 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*   855 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*   856 */     return combineLatest(Functions.toFunction(combiner), (Publisher<?>[])new Publisher[] { source1, source2, source3 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T1, T2, T3, T4, R> Flowable<R> combineLatest(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> combiner) {
/*   906 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*   907 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*   908 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*   909 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/*   910 */     return combineLatest(Functions.toFunction(combiner), (Publisher<?>[])new Publisher[] { source1, source2, source3, source4 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T1, T2, T3, T4, T5, R> Flowable<R> combineLatest(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Publisher<? extends T5> source5, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> combiner) {
/*   964 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*   965 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*   966 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*   967 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/*   968 */     ObjectHelper.requireNonNull(source5, "source5 is null");
/*   969 */     return combineLatest(Functions.toFunction(combiner), (Publisher<?>[])new Publisher[] { source1, source2, source3, source4, source5 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T1, T2, T3, T4, T5, T6, R> Flowable<R> combineLatest(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Publisher<? extends T5> source5, Publisher<? extends T6> source6, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> combiner) {
/*  1026 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  1027 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  1028 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*  1029 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/*  1030 */     ObjectHelper.requireNonNull(source5, "source5 is null");
/*  1031 */     ObjectHelper.requireNonNull(source6, "source6 is null");
/*  1032 */     return combineLatest(Functions.toFunction(combiner), (Publisher<?>[])new Publisher[] { source1, source2, source3, source4, source5, source6 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T1, T2, T3, T4, T5, T6, T7, R> Flowable<R> combineLatest(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Publisher<? extends T5> source5, Publisher<? extends T6> source6, Publisher<? extends T7> source7, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> combiner) {
/*  1093 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  1094 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  1095 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*  1096 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/*  1097 */     ObjectHelper.requireNonNull(source5, "source5 is null");
/*  1098 */     ObjectHelper.requireNonNull(source6, "source6 is null");
/*  1099 */     ObjectHelper.requireNonNull(source7, "source7 is null");
/*  1100 */     return combineLatest(Functions.toFunction(combiner), (Publisher<?>[])new Publisher[] { source1, source2, source3, source4, source5, source6, source7 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Flowable<R> combineLatest(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Publisher<? extends T5> source5, Publisher<? extends T6> source6, Publisher<? extends T7> source7, Publisher<? extends T8> source8, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> combiner) {
/*  1164 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  1165 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  1166 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*  1167 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/*  1168 */     ObjectHelper.requireNonNull(source5, "source5 is null");
/*  1169 */     ObjectHelper.requireNonNull(source6, "source6 is null");
/*  1170 */     ObjectHelper.requireNonNull(source7, "source7 is null");
/*  1171 */     ObjectHelper.requireNonNull(source8, "source8 is null");
/*  1172 */     return combineLatest(Functions.toFunction(combiner), (Publisher<?>[])new Publisher[] { source1, source2, source3, source4, source5, source6, source7, source8 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Flowable<R> combineLatest(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Publisher<? extends T5> source5, Publisher<? extends T6> source6, Publisher<? extends T7> source7, Publisher<? extends T8> source8, Publisher<? extends T9> source9, Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> combiner) {
/*  1240 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  1241 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  1242 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*  1243 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/*  1244 */     ObjectHelper.requireNonNull(source5, "source5 is null");
/*  1245 */     ObjectHelper.requireNonNull(source6, "source6 is null");
/*  1246 */     ObjectHelper.requireNonNull(source7, "source7 is null");
/*  1247 */     ObjectHelper.requireNonNull(source8, "source8 is null");
/*  1248 */     ObjectHelper.requireNonNull(source9, "source9 is null");
/*  1249 */     return combineLatest(Functions.toFunction(combiner), (Publisher<?>[])new Publisher[] { source1, source2, source3, source4, source5, source6, source7, source8, source9 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> concat(Iterable<? extends Publisher<? extends T>> sources) {
/*  1276 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*       */     
/*  1278 */     return fromIterable(sources).concatMapDelayError(Functions.identity(), 2, false);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public static <T> Flowable<T> concat(Publisher<? extends Publisher<? extends T>> sources) {
/*  1307 */     return concat(sources, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public static <T> Flowable<T> concat(Publisher<? extends Publisher<? extends T>> sources, int prefetch) {
/*  1339 */     return fromPublisher(sources).concatMap(Functions.identity(), prefetch);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> concat(Publisher<? extends T> source1, Publisher<? extends T> source2) {
/*  1372 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  1373 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  1374 */     return concatArray((Publisher<? extends T>[])new Publisher[] { source1, source2 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> concat(Publisher<? extends T> source1, Publisher<? extends T> source2, Publisher<? extends T> source3) {
/*  1411 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  1412 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  1413 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*  1414 */     return concatArray((Publisher<? extends T>[])new Publisher[] { source1, source2, source3 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> concat(Publisher<? extends T> source1, Publisher<? extends T> source2, Publisher<? extends T> source3, Publisher<? extends T> source4) {
/*  1453 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  1454 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  1455 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*  1456 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/*  1457 */     return concatArray((Publisher<? extends T>[])new Publisher[] { source1, source2, source3, source4 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public static <T> Flowable<T> concatArray(Publisher<? extends T>... sources) {
/*  1484 */     if (sources.length == 0) {
/*  1485 */       return empty();
/*       */     }
/*  1487 */     if (sources.length == 1) {
/*  1488 */       return fromPublisher(sources[0]);
/*       */     }
/*  1490 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableConcatArray((Publisher[])sources, false));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public static <T> Flowable<T> concatArrayDelayError(Publisher<? extends T>... sources) {
/*  1516 */     if (sources.length == 0) {
/*  1517 */       return empty();
/*       */     }
/*  1519 */     if (sources.length == 1) {
/*  1520 */       return fromPublisher(sources[0]);
/*       */     }
/*  1522 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableConcatArray((Publisher[])sources, true));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public static <T> Flowable<T> concatArrayEager(Publisher<? extends T>... sources) {
/*  1551 */     return concatArrayEager(bufferSize(), bufferSize(), sources);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> concatArrayEager(int maxConcurrency, int prefetch, Publisher<? extends T>... sources) {
/*  1585 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  1586 */     ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
/*  1587 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/*  1588 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableConcatMapEager((Flowable)new FlowableFromArray((Object[])sources), Functions.identity(), maxConcurrency, prefetch, ErrorMode.IMMEDIATE));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("none")
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   public static <T> Flowable<T> concatArrayEagerDelayError(Publisher<? extends T>... sources) {
/*  1618 */     return concatArrayEagerDelayError(bufferSize(), bufferSize(), sources);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("none")
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   public static <T> Flowable<T> concatArrayEagerDelayError(int maxConcurrency, int prefetch, Publisher<? extends T>... sources) {
/*  1652 */     return fromArray(sources).concatMapEagerDelayError(Functions.identity(), maxConcurrency, prefetch, true);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> concatDelayError(Iterable<? extends Publisher<? extends T>> sources) {
/*  1679 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  1680 */     return fromIterable(sources).concatMapDelayError(Functions.identity());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public static <T> Flowable<T> concatDelayError(Publisher<? extends Publisher<? extends T>> sources) {
/*  1702 */     return concatDelayError(sources, bufferSize(), true);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public static <T> Flowable<T> concatDelayError(Publisher<? extends Publisher<? extends T>> sources, int prefetch, boolean tillTheEnd) {
/*  1728 */     return fromPublisher(sources).concatMapDelayError(Functions.identity(), prefetch, tillTheEnd);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public static <T> Flowable<T> concatEager(Publisher<? extends Publisher<? extends T>> sources) {
/*  1754 */     return concatEager(sources, bufferSize(), bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> concatEager(Publisher<? extends Publisher<? extends T>> sources, int maxConcurrency, int prefetch) {
/*  1785 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  1786 */     ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
/*  1787 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/*  1788 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableConcatMapEagerPublisher(sources, Functions.identity(), maxConcurrency, prefetch, ErrorMode.IMMEDIATE));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public static <T> Flowable<T> concatEager(Iterable<? extends Publisher<? extends T>> sources) {
/*  1814 */     return concatEager(sources, bufferSize(), bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> concatEager(Iterable<? extends Publisher<? extends T>> sources, int maxConcurrency, int prefetch) {
/*  1845 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  1846 */     ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
/*  1847 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/*  1848 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableConcatMapEager((Flowable)new FlowableFromIterable(sources), Functions.identity(), maxConcurrency, prefetch, ErrorMode.IMMEDIATE));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.SPECIAL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> create(FlowableOnSubscribe<T> source, BackpressureStrategy mode) {
/*  1902 */     ObjectHelper.requireNonNull(source, "source is null");
/*  1903 */     ObjectHelper.requireNonNull(mode, "mode is null");
/*  1904 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableCreate(source, mode));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> defer(Callable<? extends Publisher<? extends T>> supplier) {
/*  1939 */     ObjectHelper.requireNonNull(supplier, "supplier is null");
/*  1940 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableDefer(supplier));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   public static <T> Flowable<T> empty() {
/*  1966 */     return RxJavaPlugins.onAssembly(FlowableEmpty.INSTANCE);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> error(Callable<? extends Throwable> supplier) {
/*  1994 */     ObjectHelper.requireNonNull(supplier, "supplier is null");
/*  1995 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableError(supplier));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> error(Throwable throwable) {
/*  2023 */     ObjectHelper.requireNonNull(throwable, "throwable is null");
/*  2024 */     return error(Functions.justCallable(throwable));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> fromArray(T... items) {
/*  2051 */     ObjectHelper.requireNonNull(items, "items is null");
/*  2052 */     if (items.length == 0) {
/*  2053 */       return empty();
/*       */     }
/*  2055 */     if (items.length == 1) {
/*  2056 */       return just(items[0]);
/*       */     }
/*  2058 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableFromArray((Object[])items));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> fromCallable(Callable<? extends T> supplier) {
/*  2097 */     ObjectHelper.requireNonNull(supplier, "supplier is null");
/*  2098 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableFromCallable(supplier));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> fromFuture(Future<? extends T> future) {
/*  2134 */     ObjectHelper.requireNonNull(future, "future is null");
/*  2135 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableFromFuture(future, 0L, null));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> fromFuture(Future<? extends T> future, long timeout, TimeUnit unit) {
/*  2175 */     ObjectHelper.requireNonNull(future, "future is null");
/*  2176 */     ObjectHelper.requireNonNull(unit, "unit is null");
/*  2177 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableFromFuture(future, timeout, unit));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("custom")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> fromFuture(Future<? extends T> future, long timeout, TimeUnit unit, Scheduler scheduler) {
/*  2221 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/*  2222 */     return fromFuture(future, timeout, unit).subscribeOn(scheduler);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("custom")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> fromFuture(Future<? extends T> future, Scheduler scheduler) {
/*  2260 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/*  2261 */     return fromFuture(future).subscribeOn(scheduler);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> fromIterable(Iterable<? extends T> source) {
/*  2289 */     ObjectHelper.requireNonNull(source, "source is null");
/*  2290 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableFromIterable(source));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> fromPublisher(Publisher<? extends T> source) {
/*  2326 */     if (source instanceof Flowable) {
/*  2327 */       return RxJavaPlugins.onAssembly((Flowable)source);
/*       */     }
/*  2329 */     ObjectHelper.requireNonNull(source, "source is null");
/*       */     
/*  2331 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableFromPublisher(source));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> generate(Consumer<Emitter<T>> generator) {
/*  2360 */     ObjectHelper.requireNonNull(generator, "generator is null");
/*  2361 */     return generate(Functions.nullSupplier(), 
/*  2362 */         FlowableInternalHelper.simpleGenerator(generator), 
/*  2363 */         Functions.emptyConsumer());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T, S> Flowable<T> generate(Callable<S> initialState, BiConsumer<S, Emitter<T>> generator) {
/*  2394 */     ObjectHelper.requireNonNull(generator, "generator is null");
/*  2395 */     return generate(initialState, FlowableInternalHelper.simpleBiGenerator(generator), 
/*  2396 */         Functions.emptyConsumer());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T, S> Flowable<T> generate(Callable<S> initialState, BiConsumer<S, Emitter<T>> generator, Consumer<? super S> disposeState) {
/*  2430 */     ObjectHelper.requireNonNull(generator, "generator is null");
/*  2431 */     return generate(initialState, FlowableInternalHelper.simpleBiGenerator(generator), disposeState);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public static <T, S> Flowable<T> generate(Callable<S> initialState, BiFunction<S, Emitter<T>, S> generator) {
/*  2462 */     return generate(initialState, generator, Functions.emptyConsumer());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T, S> Flowable<T> generate(Callable<S> initialState, BiFunction<S, Emitter<T>, S> generator, Consumer<? super S> disposeState) {
/*  2496 */     ObjectHelper.requireNonNull(initialState, "initialState is null");
/*  2497 */     ObjectHelper.requireNonNull(generator, "generator is null");
/*  2498 */     ObjectHelper.requireNonNull(disposeState, "disposeState is null");
/*  2499 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableGenerate(initialState, generator, disposeState));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public static Flowable<Long> interval(long initialDelay, long period, TimeUnit unit) {
/*  2531 */     return interval(initialDelay, period, unit, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("custom")
/*       */   @NonNull
/*       */   public static Flowable<Long> interval(long initialDelay, long period, TimeUnit unit, Scheduler scheduler) {
/*  2566 */     ObjectHelper.requireNonNull(unit, "unit is null");
/*  2567 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/*  2568 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableInterval(Math.max(0L, initialDelay), Math.max(0L, period), unit, scheduler));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public static Flowable<Long> interval(long period, TimeUnit unit) {
/*  2594 */     return interval(period, period, unit, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("custom")
/*       */   public static Flowable<Long> interval(long period, TimeUnit unit, Scheduler scheduler) {
/*  2624 */     return interval(period, period, unit, scheduler);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public static Flowable<Long> intervalRange(long start, long count, long initialDelay, long period, TimeUnit unit) {
/*  2648 */     return intervalRange(start, count, initialDelay, period, unit, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("custom")
/*       */   @NonNull
/*       */   public static Flowable<Long> intervalRange(long start, long count, long initialDelay, long period, TimeUnit unit, Scheduler scheduler) {
/*  2674 */     if (count < 0L) {
/*  2675 */       throw new IllegalArgumentException("count >= 0 required but it was " + count);
/*       */     }
/*  2677 */     if (count == 0L) {
/*  2678 */       return empty().delay(initialDelay, unit, scheduler);
/*       */     }
/*       */     
/*  2681 */     long end = start + count - 1L;
/*  2682 */     if (start > 0L && end < 0L) {
/*  2683 */       throw new IllegalArgumentException("Overflow! start + count is bigger than Long.MAX_VALUE");
/*       */     }
/*  2685 */     ObjectHelper.requireNonNull(unit, "unit is null");
/*  2686 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/*       */     
/*  2688 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableIntervalRange(start, end, Math.max(0L, initialDelay), Math.max(0L, period), unit, scheduler));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> just(T item) {
/*  2726 */     ObjectHelper.requireNonNull(item, "item is null");
/*  2727 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableJust(item));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> just(T item1, T item2) {
/*  2756 */     ObjectHelper.requireNonNull(item1, "item1 is null");
/*  2757 */     ObjectHelper.requireNonNull(item2, "item2 is null");
/*       */     
/*  2759 */     return fromArray((T[])new Object[] { item1, item2 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> just(T item1, T item2, T item3) {
/*  2790 */     ObjectHelper.requireNonNull(item1, "item1 is null");
/*  2791 */     ObjectHelper.requireNonNull(item2, "item2 is null");
/*  2792 */     ObjectHelper.requireNonNull(item3, "item3 is null");
/*       */     
/*  2794 */     return fromArray((T[])new Object[] { item1, item2, item3 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> just(T item1, T item2, T item3, T item4) {
/*  2827 */     ObjectHelper.requireNonNull(item1, "item1 is null");
/*  2828 */     ObjectHelper.requireNonNull(item2, "item2 is null");
/*  2829 */     ObjectHelper.requireNonNull(item3, "item3 is null");
/*  2830 */     ObjectHelper.requireNonNull(item4, "item4 is null");
/*       */     
/*  2832 */     return fromArray((T[])new Object[] { item1, item2, item3, item4 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> just(T item1, T item2, T item3, T item4, T item5) {
/*  2867 */     ObjectHelper.requireNonNull(item1, "item1 is null");
/*  2868 */     ObjectHelper.requireNonNull(item2, "item2 is null");
/*  2869 */     ObjectHelper.requireNonNull(item3, "item3 is null");
/*  2870 */     ObjectHelper.requireNonNull(item4, "item4 is null");
/*  2871 */     ObjectHelper.requireNonNull(item5, "item5 is null");
/*       */     
/*  2873 */     return fromArray((T[])new Object[] { item1, item2, item3, item4, item5 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> just(T item1, T item2, T item3, T item4, T item5, T item6) {
/*  2910 */     ObjectHelper.requireNonNull(item1, "item1 is null");
/*  2911 */     ObjectHelper.requireNonNull(item2, "item2 is null");
/*  2912 */     ObjectHelper.requireNonNull(item3, "item3 is null");
/*  2913 */     ObjectHelper.requireNonNull(item4, "item4 is null");
/*  2914 */     ObjectHelper.requireNonNull(item5, "item5 is null");
/*  2915 */     ObjectHelper.requireNonNull(item6, "item6 is null");
/*       */     
/*  2917 */     return fromArray((T[])new Object[] { item1, item2, item3, item4, item5, item6 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> just(T item1, T item2, T item3, T item4, T item5, T item6, T item7) {
/*  2956 */     ObjectHelper.requireNonNull(item1, "item1 is null");
/*  2957 */     ObjectHelper.requireNonNull(item2, "item2 is null");
/*  2958 */     ObjectHelper.requireNonNull(item3, "item3 is null");
/*  2959 */     ObjectHelper.requireNonNull(item4, "item4 is null");
/*  2960 */     ObjectHelper.requireNonNull(item5, "item5 is null");
/*  2961 */     ObjectHelper.requireNonNull(item6, "item6 is null");
/*  2962 */     ObjectHelper.requireNonNull(item7, "item7 is null");
/*       */     
/*  2964 */     return fromArray((T[])new Object[] { item1, item2, item3, item4, item5, item6, item7 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> just(T item1, T item2, T item3, T item4, T item5, T item6, T item7, T item8) {
/*  3005 */     ObjectHelper.requireNonNull(item1, "item1 is null");
/*  3006 */     ObjectHelper.requireNonNull(item2, "item2 is null");
/*  3007 */     ObjectHelper.requireNonNull(item3, "item3 is null");
/*  3008 */     ObjectHelper.requireNonNull(item4, "item4 is null");
/*  3009 */     ObjectHelper.requireNonNull(item5, "item5 is null");
/*  3010 */     ObjectHelper.requireNonNull(item6, "item6 is null");
/*  3011 */     ObjectHelper.requireNonNull(item7, "item7 is null");
/*  3012 */     ObjectHelper.requireNonNull(item8, "item8 is null");
/*       */     
/*  3014 */     return fromArray((T[])new Object[] { item1, item2, item3, item4, item5, item6, item7, item8 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> just(T item1, T item2, T item3, T item4, T item5, T item6, T item7, T item8, T item9) {
/*  3057 */     ObjectHelper.requireNonNull(item1, "item1 is null");
/*  3058 */     ObjectHelper.requireNonNull(item2, "item2 is null");
/*  3059 */     ObjectHelper.requireNonNull(item3, "item3 is null");
/*  3060 */     ObjectHelper.requireNonNull(item4, "item4 is null");
/*  3061 */     ObjectHelper.requireNonNull(item5, "item5 is null");
/*  3062 */     ObjectHelper.requireNonNull(item6, "item6 is null");
/*  3063 */     ObjectHelper.requireNonNull(item7, "item7 is null");
/*  3064 */     ObjectHelper.requireNonNull(item8, "item8 is null");
/*  3065 */     ObjectHelper.requireNonNull(item9, "item9 is null");
/*       */     
/*  3067 */     return fromArray((T[])new Object[] { item1, item2, item3, item4, item5, item6, item7, item8, item9 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> just(T item1, T item2, T item3, T item4, T item5, T item6, T item7, T item8, T item9, T item10) {
/*  3112 */     ObjectHelper.requireNonNull(item1, "item1 is null");
/*  3113 */     ObjectHelper.requireNonNull(item2, "item2 is null");
/*  3114 */     ObjectHelper.requireNonNull(item3, "item3 is null");
/*  3115 */     ObjectHelper.requireNonNull(item4, "item4 is null");
/*  3116 */     ObjectHelper.requireNonNull(item5, "item5 is null");
/*  3117 */     ObjectHelper.requireNonNull(item6, "item6 is null");
/*  3118 */     ObjectHelper.requireNonNull(item7, "item7 is null");
/*  3119 */     ObjectHelper.requireNonNull(item8, "item8 is null");
/*  3120 */     ObjectHelper.requireNonNull(item9, "item9 is null");
/*  3121 */     ObjectHelper.requireNonNull(item10, "item10 is null");
/*       */     
/*  3123 */     return fromArray((T[])new Object[] { item1, item2, item3, item4, item5, item6, item7, item8, item9, item10 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public static <T> Flowable<T> merge(Iterable<? extends Publisher<? extends T>> sources, int maxConcurrency, int bufferSize) {
/*  3174 */     return fromIterable(sources).flatMap(Functions.identity(), false, maxConcurrency, bufferSize);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public static <T> Flowable<T> mergeArray(int maxConcurrency, int bufferSize, Publisher<? extends T>... sources) {
/*  3225 */     return fromArray(sources).flatMap(Functions.identity(), false, maxConcurrency, bufferSize);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public static <T> Flowable<T> merge(Iterable<? extends Publisher<? extends T>> sources) {
/*  3269 */     return fromIterable(sources).flatMap(Functions.identity());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public static <T> Flowable<T> merge(Iterable<? extends Publisher<? extends T>> sources, int maxConcurrency) {
/*  3318 */     return fromIterable(sources).flatMap(Functions.identity(), maxConcurrency);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public static <T> Flowable<T> merge(Publisher<? extends Publisher<? extends T>> sources) {
/*  3363 */     return merge(sources, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public static <T> Flowable<T> merge(Publisher<? extends Publisher<? extends T>> sources, int maxConcurrency) {
/*  3414 */     return fromPublisher(sources).flatMap(Functions.identity(), maxConcurrency);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public static <T> Flowable<T> mergeArray(Publisher<? extends T>... sources) {
/*  3457 */     return fromArray(sources).flatMap(Functions.identity(), sources.length);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> merge(Publisher<? extends T> source1, Publisher<? extends T> source2) {
/*  3503 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  3504 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  3505 */     return fromArray(new Publisher[] { source1, source2 }).flatMap(Functions.identity(), false, 2);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> merge(Publisher<? extends T> source1, Publisher<? extends T> source2, Publisher<? extends T> source3) {
/*  3553 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  3554 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  3555 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*  3556 */     return fromArray(new Publisher[] { source1, source2, source3 }).flatMap(Functions.identity(), false, 3);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> merge(Publisher<? extends T> source1, Publisher<? extends T> source2, Publisher<? extends T> source3, Publisher<? extends T> source4) {
/*  3608 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  3609 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  3610 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*  3611 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/*  3612 */     return fromArray(new Publisher[] { source1, source2, source3, source4 }).flatMap(Functions.identity(), false, 4);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public static <T> Flowable<T> mergeDelayError(Iterable<? extends Publisher<? extends T>> sources) {
/*  3648 */     return fromIterable(sources).flatMap(Functions.identity(), true);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public static <T> Flowable<T> mergeDelayError(Iterable<? extends Publisher<? extends T>> sources, int maxConcurrency, int bufferSize) {
/*  3688 */     return fromIterable(sources).flatMap(Functions.identity(), true, maxConcurrency, bufferSize);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public static <T> Flowable<T> mergeArrayDelayError(int maxConcurrency, int bufferSize, Publisher<? extends T>... sources) {
/*  3728 */     return fromArray(sources).flatMap(Functions.identity(), true, maxConcurrency, bufferSize);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public static <T> Flowable<T> mergeDelayError(Iterable<? extends Publisher<? extends T>> sources, int maxConcurrency) {
/*  3766 */     return fromIterable(sources).flatMap(Functions.identity(), true, maxConcurrency);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public static <T> Flowable<T> mergeDelayError(Publisher<? extends Publisher<? extends T>> sources) {
/*  3802 */     return mergeDelayError(sources, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public static <T> Flowable<T> mergeDelayError(Publisher<? extends Publisher<? extends T>> sources, int maxConcurrency) {
/*  3842 */     return fromPublisher(sources).flatMap(Functions.identity(), true, maxConcurrency);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public static <T> Flowable<T> mergeArrayDelayError(Publisher<? extends T>... sources) {
/*  3878 */     return fromArray(sources).flatMap(Functions.identity(), true, sources.length);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> mergeDelayError(Publisher<? extends T> source1, Publisher<? extends T> source2) {
/*  3916 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  3917 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  3918 */     return fromArray(new Publisher[] { source1, source2 }).flatMap(Functions.identity(), true, 2);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> mergeDelayError(Publisher<? extends T> source1, Publisher<? extends T> source2, Publisher<? extends T> source3) {
/*  3959 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  3960 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  3961 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*  3962 */     return fromArray(new Publisher[] { source1, source2, source3 }).flatMap(Functions.identity(), true, 3);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> mergeDelayError(Publisher<? extends T> source1, Publisher<? extends T> source2, Publisher<? extends T> source3, Publisher<? extends T> source4) {
/*  4007 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  4008 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  4009 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*  4010 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/*  4011 */     return fromArray(new Publisher[] { source1, source2, source3, source4 }).flatMap(Functions.identity(), true, 4);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   public static <T> Flowable<T> never() {
/*  4037 */     return RxJavaPlugins.onAssembly(FlowableNever.INSTANCE);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public static Flowable<Integer> range(int start, int count) {
/*  4065 */     if (count < 0) {
/*  4066 */       throw new IllegalArgumentException("count >= 0 required but it was " + count);
/*       */     }
/*  4068 */     if (count == 0) {
/*  4069 */       return empty();
/*       */     }
/*  4071 */     if (count == 1) {
/*  4072 */       return just(Integer.valueOf(start));
/*       */     }
/*  4074 */     if (start + (count - 1) > 2147483647L) {
/*  4075 */       throw new IllegalArgumentException("Integer overflow");
/*       */     }
/*  4077 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableRange(start, count));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public static Flowable<Long> rangeLong(long start, long count) {
/*  4105 */     if (count < 0L) {
/*  4106 */       throw new IllegalArgumentException("count >= 0 required but it was " + count);
/*       */     }
/*       */     
/*  4109 */     if (count == 0L) {
/*  4110 */       return empty();
/*       */     }
/*       */     
/*  4113 */     if (count == 1L) {
/*  4114 */       return just(Long.valueOf(start));
/*       */     }
/*       */     
/*  4117 */     long end = start + count - 1L;
/*  4118 */     if (start > 0L && end < 0L) {
/*  4119 */       throw new IllegalArgumentException("Overflow! start + count is bigger than Long.MAX_VALUE");
/*       */     }
/*       */     
/*  4122 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableRangeLong(start, count));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public static <T> Single<Boolean> sequenceEqual(Publisher<? extends T> source1, Publisher<? extends T> source2) {
/*  4151 */     return sequenceEqual(source1, source2, ObjectHelper.equalsPredicate(), bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public static <T> Single<Boolean> sequenceEqual(Publisher<? extends T> source1, Publisher<? extends T> source2, BiPredicate<? super T, ? super T> isEqual) {
/*  4185 */     return sequenceEqual(source1, source2, isEqual, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Single<Boolean> sequenceEqual(Publisher<? extends T> source1, Publisher<? extends T> source2, BiPredicate<? super T, ? super T> isEqual, int bufferSize) {
/*  4222 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  4223 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  4224 */     ObjectHelper.requireNonNull(isEqual, "isEqual is null");
/*  4225 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/*  4226 */     return RxJavaPlugins.onAssembly((Single)new FlowableSequenceEqualSingle(source1, source2, isEqual, bufferSize));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public static <T> Single<Boolean> sequenceEqual(Publisher<? extends T> source1, Publisher<? extends T> source2, int bufferSize) {
/*  4257 */     return sequenceEqual(source1, source2, ObjectHelper.equalsPredicate(), bufferSize);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public static <T> Flowable<T> switchOnNext(Publisher<? extends Publisher<? extends T>> sources, int bufferSize) {
/*  4297 */     return fromPublisher(sources).switchMap(Functions.identity(), bufferSize);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public static <T> Flowable<T> switchOnNext(Publisher<? extends Publisher<? extends T>> sources) {
/*  4335 */     return fromPublisher(sources).switchMap(Functions.identity());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public static <T> Flowable<T> switchOnNextDelayError(Publisher<? extends Publisher<? extends T>> sources) {
/*  4374 */     return switchOnNextDelayError(sources, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public static <T> Flowable<T> switchOnNextDelayError(Publisher<? extends Publisher<? extends T>> sources, int prefetch) {
/*  4415 */     return fromPublisher(sources).switchMapDelayError(Functions.identity(), prefetch);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public static Flowable<Long> timer(long delay, TimeUnit unit) {
/*  4441 */     return timer(delay, unit, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("custom")
/*       */   @NonNull
/*       */   public static Flowable<Long> timer(long delay, TimeUnit unit, Scheduler scheduler) {
/*  4472 */     ObjectHelper.requireNonNull(unit, "unit is null");
/*  4473 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/*       */     
/*  4475 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableTimer(Math.max(0L, delay), unit, scheduler));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.NONE)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Flowable<T> unsafeCreate(Publisher<T> onSubscribe) {
/*  4501 */     ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");
/*  4502 */     if (onSubscribe instanceof Flowable) {
/*  4503 */       throw new IllegalArgumentException("unsafeCreate(Flowable) should be upgraded");
/*       */     }
/*  4505 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableFromPublisher(onSubscribe));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   public static <T, D> Flowable<T> using(Callable<? extends D> resourceSupplier, Function<? super D, ? extends Publisher<? extends T>> sourceSupplier, Consumer<? super D> resourceDisposer) {
/*  4536 */     return using(resourceSupplier, sourceSupplier, resourceDisposer, true);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T, D> Flowable<T> using(Callable<? extends D> resourceSupplier, Function<? super D, ? extends Publisher<? extends T>> sourceSupplier, Consumer<? super D> resourceDisposer, boolean eager) {
/*  4577 */     ObjectHelper.requireNonNull(resourceSupplier, "resourceSupplier is null");
/*  4578 */     ObjectHelper.requireNonNull(sourceSupplier, "sourceSupplier is null");
/*  4579 */     ObjectHelper.requireNonNull(resourceDisposer, "resourceDisposer is null");
/*  4580 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableUsing(resourceSupplier, sourceSupplier, resourceDisposer, eager));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T, R> Flowable<R> zip(Iterable<? extends Publisher<? extends T>> sources, Function<? super Object[], ? extends R> zipper) {
/*  4632 */     ObjectHelper.requireNonNull(zipper, "zipper is null");
/*  4633 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  4634 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableZip(null, sources, zipper, bufferSize(), false));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T, R> Flowable<R> zip(Publisher<? extends Publisher<? extends T>> sources, Function<? super Object[], ? extends R> zipper) {
/*  4688 */     ObjectHelper.requireNonNull(zipper, "zipper is null");
/*  4689 */     return fromPublisher(sources).toList().flatMapPublisher(FlowableInternalHelper.zipIterable(zipper));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T1, T2, R> Flowable<R> zip(Publisher<? extends T1> source1, Publisher<? extends T2> source2, BiFunction<? super T1, ? super T2, ? extends R> zipper) {
/*  4748 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  4749 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  4750 */     return zipArray(Functions.toFunction(zipper), false, bufferSize(), (Publisher<?>[])new Publisher[] { source1, source2 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T1, T2, R> Flowable<R> zip(Publisher<? extends T1> source1, Publisher<? extends T2> source2, BiFunction<? super T1, ? super T2, ? extends R> zipper, boolean delayError) {
/*  4810 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  4811 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  4812 */     return zipArray(Functions.toFunction(zipper), delayError, bufferSize(), (Publisher<?>[])new Publisher[] { source1, source2 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T1, T2, R> Flowable<R> zip(Publisher<? extends T1> source1, Publisher<? extends T2> source2, BiFunction<? super T1, ? super T2, ? extends R> zipper, boolean delayError, int bufferSize) {
/*  4873 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  4874 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  4875 */     return zipArray(Functions.toFunction(zipper), delayError, bufferSize, (Publisher<?>[])new Publisher[] { source1, source2 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T1, T2, T3, R> Flowable<R> zip(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Function3<? super T1, ? super T2, ? super T3, ? extends R> zipper) {
/*  4938 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  4939 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  4940 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*  4941 */     return zipArray(Functions.toFunction(zipper), false, bufferSize(), (Publisher<?>[])new Publisher[] { source1, source2, source3 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T1, T2, T3, T4, R> Flowable<R> zip(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> zipper) {
/*  5008 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  5009 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  5010 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*  5011 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/*  5012 */     return zipArray(Functions.toFunction(zipper), false, bufferSize(), (Publisher<?>[])new Publisher[] { source1, source2, source3, source4 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T1, T2, T3, T4, T5, R> Flowable<R> zip(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Publisher<? extends T5> source5, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> zipper) {
/*  5082 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  5083 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  5084 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*  5085 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/*  5086 */     ObjectHelper.requireNonNull(source5, "source5 is null");
/*  5087 */     return zipArray(Functions.toFunction(zipper), false, bufferSize(), (Publisher<?>[])new Publisher[] { source1, source2, source3, source4, source5 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T1, T2, T3, T4, T5, T6, R> Flowable<R> zip(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Publisher<? extends T5> source5, Publisher<? extends T6> source6, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> zipper) {
/*  5159 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  5160 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  5161 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*  5162 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/*  5163 */     ObjectHelper.requireNonNull(source5, "source5 is null");
/*  5164 */     ObjectHelper.requireNonNull(source6, "source6 is null");
/*  5165 */     return zipArray(Functions.toFunction(zipper), false, bufferSize(), (Publisher<?>[])new Publisher[] { source1, source2, source3, source4, source5, source6 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T1, T2, T3, T4, T5, T6, T7, R> Flowable<R> zip(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Publisher<? extends T5> source5, Publisher<? extends T6> source6, Publisher<? extends T7> source7, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> zipper) {
/*  5241 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  5242 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  5243 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*  5244 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/*  5245 */     ObjectHelper.requireNonNull(source5, "source5 is null");
/*  5246 */     ObjectHelper.requireNonNull(source6, "source6 is null");
/*  5247 */     ObjectHelper.requireNonNull(source7, "source7 is null");
/*  5248 */     return zipArray(Functions.toFunction(zipper), false, bufferSize(), (Publisher<?>[])new Publisher[] { source1, source2, source3, source4, source5, source6, source7 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Flowable<R> zip(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Publisher<? extends T5> source5, Publisher<? extends T6> source6, Publisher<? extends T7> source7, Publisher<? extends T8> source8, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> zipper) {
/*  5327 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  5328 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  5329 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*  5330 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/*  5331 */     ObjectHelper.requireNonNull(source5, "source5 is null");
/*  5332 */     ObjectHelper.requireNonNull(source6, "source6 is null");
/*  5333 */     ObjectHelper.requireNonNull(source7, "source7 is null");
/*  5334 */     ObjectHelper.requireNonNull(source8, "source8 is null");
/*  5335 */     return zipArray(Functions.toFunction(zipper), false, bufferSize(), (Publisher<?>[])new Publisher[] { source1, source2, source3, source4, source5, source6, source7, source8 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Flowable<R> zip(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Publisher<? extends T5> source5, Publisher<? extends T6> source6, Publisher<? extends T7> source7, Publisher<? extends T8> source8, Publisher<? extends T9> source9, Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> zipper) {
/*  5418 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  5419 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  5420 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*  5421 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/*  5422 */     ObjectHelper.requireNonNull(source5, "source5 is null");
/*  5423 */     ObjectHelper.requireNonNull(source6, "source6 is null");
/*  5424 */     ObjectHelper.requireNonNull(source7, "source7 is null");
/*  5425 */     ObjectHelper.requireNonNull(source8, "source8 is null");
/*  5426 */     ObjectHelper.requireNonNull(source9, "source9 is null");
/*  5427 */     return zipArray(Functions.toFunction(zipper), false, bufferSize(), (Publisher<?>[])new Publisher[] { source1, source2, source3, source4, source5, source6, source7, source8, source9 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T, R> Flowable<R> zipArray(Function<? super Object[], ? extends R> zipper, boolean delayError, int bufferSize, Publisher<? extends T>... sources) {
/*  5485 */     if (sources.length == 0) {
/*  5486 */       return empty();
/*       */     }
/*  5488 */     ObjectHelper.requireNonNull(zipper, "zipper is null");
/*  5489 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/*  5490 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableZip((Publisher[])sources, null, zipper, bufferSize, delayError));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T, R> Flowable<R> zipIterable(Iterable<? extends Publisher<? extends T>> sources, Function<? super Object[], ? extends R> zipper, boolean delayError, int bufferSize) {
/*  5549 */     ObjectHelper.requireNonNull(zipper, "zipper is null");
/*  5550 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  5551 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/*  5552 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableZip(null, sources, zipper, bufferSize, delayError));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Single<Boolean> all(Predicate<? super T> predicate) {
/*  5583 */     ObjectHelper.requireNonNull(predicate, "predicate is null");
/*  5584 */     return RxJavaPlugins.onAssembly((Single)new FlowableAllSingle(this, predicate));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Flowable<T> ambWith(Publisher<? extends T> other) {
/*  5613 */     ObjectHelper.requireNonNull(other, "other is null");
/*  5614 */     return ambArray((Publisher<? extends T>[])new Publisher[] { this, other });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Single<Boolean> any(Predicate<? super T> predicate) {
/*  5645 */     ObjectHelper.requireNonNull(predicate, "predicate is null");
/*  5646 */     return RxJavaPlugins.onAssembly((Single)new FlowableAnySingle(this, predicate));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.SPECIAL)
/*       */   @SchedulerSupport("none")
/*       */   public final <R> R as(@NonNull FlowableConverter<T, ? extends R> converter) {
/*  5670 */     return ((FlowableConverter<T, R>)ObjectHelper.requireNonNull(converter, "converter is null")).apply(this);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final T blockingFirst() {
/*  5697 */     BlockingFirstSubscriber<T> s = new BlockingFirstSubscriber();
/*  5698 */     subscribe((FlowableSubscriber<? super T>)s);
/*  5699 */     T v = (T)s.blockingGet();
/*  5700 */     if (v != null) {
/*  5701 */       return v;
/*       */     }
/*  5703 */     throw new NoSuchElementException();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final T blockingFirst(T defaultItem) {
/*  5731 */     BlockingFirstSubscriber<T> s = new BlockingFirstSubscriber();
/*  5732 */     subscribe((FlowableSubscriber<? super T>)s);
/*  5733 */     T v = (T)s.blockingGet();
/*  5734 */     return (v != null) ? v : defaultItem;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final void blockingForEach(Consumer<? super T> onNext) {
/*  5772 */     Iterator<T> it = blockingIterable().iterator();
/*  5773 */     while (it.hasNext()) {
/*       */       try {
/*  5775 */         onNext.accept(it.next());
/*  5776 */       } catch (Throwable e) {
/*  5777 */         Exceptions.throwIfFatal(e);
/*  5778 */         ((Disposable)it).dispose();
/*  5779 */         throw ExceptionHelper.wrapOrThrow(e);
/*       */       } 
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final Iterable<T> blockingIterable() {
/*  5803 */     return blockingIterable(bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final Iterable<T> blockingIterable(int bufferSize) {
/*  5827 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/*  5828 */     return (Iterable<T>)new BlockingFlowableIterable(this, bufferSize);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final T blockingLast() {
/*  5857 */     BlockingLastSubscriber<T> s = new BlockingLastSubscriber();
/*  5858 */     subscribe((FlowableSubscriber<? super T>)s);
/*  5859 */     T v = (T)s.blockingGet();
/*  5860 */     if (v != null) {
/*  5861 */       return v;
/*       */     }
/*  5863 */     throw new NoSuchElementException();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final T blockingLast(T defaultItem) {
/*  5893 */     BlockingLastSubscriber<T> s = new BlockingLastSubscriber();
/*  5894 */     subscribe((FlowableSubscriber<? super T>)s);
/*  5895 */     T v = (T)s.blockingGet();
/*  5896 */     return (v != null) ? v : defaultItem;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final Iterable<T> blockingLatest() {
/*  5923 */     return (Iterable<T>)new BlockingFlowableLatest(this);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final Iterable<T> blockingMostRecent(T initialItem) {
/*  5950 */     return (Iterable<T>)new BlockingFlowableMostRecent(this, initialItem);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final Iterable<T> blockingNext() {
/*  5974 */     return (Iterable<T>)new BlockingFlowableNext(this);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final T blockingSingle() {
/*  6001 */     return singleOrError().blockingGet();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final T blockingSingle(T defaultItem) {
/*  6032 */     return single(defaultItem).blockingGet();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final Future<T> toFuture() {
/*  6061 */     return (Future<T>)subscribeWith(new FutureSubscriber());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final void blockingSubscribe() {
/*  6085 */     FlowableBlockingSubscribe.subscribe(this);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final void blockingSubscribe(Consumer<? super T> onNext) {
/*  6115 */     FlowableBlockingSubscribe.subscribe(this, onNext, Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final void blockingSubscribe(Consumer<? super T> onNext, int bufferSize) {
/*  6147 */     FlowableBlockingSubscribe.subscribe(this, onNext, Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION, bufferSize);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final void blockingSubscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError) {
/*  6171 */     FlowableBlockingSubscribe.subscribe(this, onNext, onError, Functions.EMPTY_ACTION);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final void blockingSubscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, int bufferSize) {
/*  6198 */     FlowableBlockingSubscribe.subscribe(this, onNext, onError, Functions.EMPTY_ACTION, bufferSize);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final void blockingSubscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete) {
/*  6222 */     FlowableBlockingSubscribe.subscribe(this, onNext, onError, onComplete);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final void blockingSubscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete, int bufferSize) {
/*  6249 */     FlowableBlockingSubscribe.subscribe(this, onNext, onError, onComplete, bufferSize);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @BackpressureSupport(BackpressureKind.SPECIAL)
/*       */   @SchedulerSupport("none")
/*       */   public final void blockingSubscribe(Subscriber<? super T> subscriber) {
/*  6273 */     FlowableBlockingSubscribe.subscribe(this, subscriber);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<List<T>> buffer(int count) {
/*  6303 */     return buffer(count, count);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<List<T>> buffer(int count, int skip) {
/*  6337 */     return buffer(count, skip, ArrayListSupplier.asCallable());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <U extends Collection<? super T>> Flowable<U> buffer(int count, int skip, Callable<U> bufferSupplier) {
/*  6376 */     ObjectHelper.verifyPositive(count, "count");
/*  6377 */     ObjectHelper.verifyPositive(skip, "skip");
/*  6378 */     ObjectHelper.requireNonNull(bufferSupplier, "bufferSupplier is null");
/*  6379 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableBuffer(this, count, skip, bufferSupplier));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final <U extends Collection<? super T>> Flowable<U> buffer(int count, Callable<U> bufferSupplier) {
/*  6413 */     return buffer(count, count, bufferSupplier);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Flowable<List<T>> buffer(long timespan, long timeskip, TimeUnit unit) {
/*  6447 */     return buffer(timespan, timeskip, unit, Schedulers.computation(), ArrayListSupplier.asCallable());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("custom")
/*       */   public final Flowable<List<T>> buffer(long timespan, long timeskip, TimeUnit unit, Scheduler scheduler) {
/*  6484 */     return buffer(timespan, timeskip, unit, scheduler, ArrayListSupplier.asCallable());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("custom")
/*       */   @NonNull
/*       */   public final <U extends Collection<? super T>> Flowable<U> buffer(long timespan, long timeskip, TimeUnit unit, Scheduler scheduler, Callable<U> bufferSupplier) {
/*  6527 */     ObjectHelper.requireNonNull(unit, "unit is null");
/*  6528 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/*  6529 */     ObjectHelper.requireNonNull(bufferSupplier, "bufferSupplier is null");
/*  6530 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableBufferTimed(this, timespan, timeskip, unit, scheduler, bufferSupplier, 2147483647, false));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Flowable<List<T>> buffer(long timespan, TimeUnit unit) {
/*  6563 */     return buffer(timespan, unit, Schedulers.computation(), 2147483647);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Flowable<List<T>> buffer(long timespan, TimeUnit unit, int count) {
/*  6599 */     return buffer(timespan, unit, Schedulers.computation(), count);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("custom")
/*       */   public final Flowable<List<T>> buffer(long timespan, TimeUnit unit, Scheduler scheduler, int count) {
/*  6638 */     return buffer(timespan, unit, scheduler, count, ArrayListSupplier.asCallable(), false);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("custom")
/*       */   public final <U extends Collection<? super T>> Flowable<U> buffer(long timespan, TimeUnit unit, Scheduler scheduler, int count, Callable<U> bufferSupplier, boolean restartTimerOnMaxSize) {
/*  6687 */     ObjectHelper.requireNonNull(unit, "unit is null");
/*  6688 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/*  6689 */     ObjectHelper.requireNonNull(bufferSupplier, "bufferSupplier is null");
/*  6690 */     ObjectHelper.verifyPositive(count, "count");
/*  6691 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableBufferTimed(this, timespan, timespan, unit, scheduler, bufferSupplier, count, restartTimerOnMaxSize));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("custom")
/*       */   public final Flowable<List<T>> buffer(long timespan, TimeUnit unit, Scheduler scheduler) {
/*  6726 */     return buffer(timespan, unit, scheduler, 2147483647, ArrayListSupplier.asCallable(), false);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("none")
/*       */   public final <TOpening, TClosing> Flowable<List<T>> buffer(Flowable<? extends TOpening> openingIndicator, Function<? super TOpening, ? extends Publisher<? extends TClosing>> closingIndicator) {
/*  6762 */     return buffer(openingIndicator, closingIndicator, ArrayListSupplier.asCallable());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("none")
/*       */   public final <TOpening, TClosing, U extends Collection<? super T>> Flowable<U> buffer(Flowable<? extends TOpening> openingIndicator, Function<? super TOpening, ? extends Publisher<? extends TClosing>> closingIndicator, Callable<U> bufferSupplier) {
/*  6803 */     ObjectHelper.requireNonNull(openingIndicator, "openingIndicator is null");
/*  6804 */     ObjectHelper.requireNonNull(closingIndicator, "closingIndicator is null");
/*  6805 */     ObjectHelper.requireNonNull(bufferSupplier, "bufferSupplier is null");
/*  6806 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableBufferBoundary(this, openingIndicator, closingIndicator, bufferSupplier));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("none")
/*       */   public final <B> Flowable<List<T>> buffer(Publisher<B> boundaryIndicator) {
/*  6840 */     return buffer(boundaryIndicator, ArrayListSupplier.asCallable());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("none")
/*       */   public final <B> Flowable<List<T>> buffer(Publisher<B> boundaryIndicator, int initialCapacity) {
/*  6876 */     ObjectHelper.verifyPositive(initialCapacity, "initialCapacity");
/*  6877 */     return buffer(boundaryIndicator, Functions.createArrayList(initialCapacity));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("none")
/*       */   public final <B, U extends Collection<? super T>> Flowable<U> buffer(Publisher<B> boundaryIndicator, Callable<U> bufferSupplier) {
/*  6915 */     ObjectHelper.requireNonNull(boundaryIndicator, "boundaryIndicator is null");
/*  6916 */     ObjectHelper.requireNonNull(bufferSupplier, "bufferSupplier is null");
/*  6917 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableBufferExactBoundary(this, boundaryIndicator, bufferSupplier));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("none")
/*       */   public final <B> Flowable<List<T>> buffer(Callable<? extends Publisher<B>> boundaryIndicatorSupplier) {
/*  6950 */     return buffer(boundaryIndicatorSupplier, ArrayListSupplier.asCallable());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("none")
/*       */   public final <B, U extends Collection<? super T>> Flowable<U> buffer(Callable<? extends Publisher<B>> boundaryIndicatorSupplier, Callable<U> bufferSupplier) {
/*  6988 */     ObjectHelper.requireNonNull(boundaryIndicatorSupplier, "boundaryIndicatorSupplier is null");
/*  6989 */     ObjectHelper.requireNonNull(bufferSupplier, "bufferSupplier is null");
/*  6990 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableBufferBoundarySupplier(this, boundaryIndicatorSupplier, bufferSupplier));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> cache() {
/*  7048 */     return cacheWithInitialCapacity(16);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> cacheWithInitialCapacity(int initialCapacity) {
/*  7110 */     ObjectHelper.verifyPositive(initialCapacity, "initialCapacity");
/*  7111 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableCache(this, initialCapacity));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <U> Flowable<U> cast(Class<U> clazz) {
/*  7140 */     ObjectHelper.requireNonNull(clazz, "clazz is null");
/*  7141 */     return map(Functions.castFunction(clazz));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <U> Single<U> collect(Callable<? extends U> initialItemSupplier, BiConsumer<? super U, ? super T> collector) {
/*  7178 */     ObjectHelper.requireNonNull(initialItemSupplier, "initialItemSupplier is null");
/*  7179 */     ObjectHelper.requireNonNull(collector, "collector is null");
/*  7180 */     return RxJavaPlugins.onAssembly((Single)new FlowableCollectSingle(this, initialItemSupplier, collector));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <U> Single<U> collectInto(U initialItem, BiConsumer<? super U, ? super T> collector) {
/*  7217 */     ObjectHelper.requireNonNull(initialItem, "initialItem is null");
/*  7218 */     return collect(Functions.justCallable(initialItem), collector);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   public final <R> Flowable<R> compose(FlowableTransformer<? super T, ? extends R> composer) {
/*  7248 */     return fromPublisher(((FlowableTransformer<T, ? extends R>)ObjectHelper.requireNonNull(composer, "composer is null")).apply(this));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final <R> Flowable<R> concatMap(Function<? super T, ? extends Publisher<? extends R>> mapper) {
/*  7280 */     return concatMap(mapper, 2);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <R> Flowable<R> concatMap(Function<? super T, ? extends Publisher<? extends R>> mapper, int prefetch) {
/*  7315 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/*  7316 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/*  7317 */     if (this instanceof ScalarCallable) {
/*       */       
/*  7319 */       T v = (T)((ScalarCallable)this).call();
/*  7320 */       if (v == null) {
/*  7321 */         return empty();
/*       */       }
/*  7323 */       return FlowableScalarXMap.scalarXMap(v, mapper);
/*       */     } 
/*  7325 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableConcatMap(this, mapper, prefetch, ErrorMode.IMMEDIATE));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("none")
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   public final Completable concatMapCompletable(Function<? super T, ? extends CompletableSource> mapper) {
/*  7352 */     return concatMapCompletable(mapper, 2);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("none")
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @NonNull
/*       */   public final Completable concatMapCompletable(Function<? super T, ? extends CompletableSource> mapper, int prefetch) {
/*  7384 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/*  7385 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/*  7386 */     return RxJavaPlugins.onAssembly((Completable)new FlowableConcatMapCompletable(this, mapper, ErrorMode.IMMEDIATE, prefetch));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("none")
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   public final Completable concatMapCompletableDelayError(Function<? super T, ? extends CompletableSource> mapper) {
/*  7414 */     return concatMapCompletableDelayError(mapper, true, 2);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("none")
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   public final Completable concatMapCompletableDelayError(Function<? super T, ? extends CompletableSource> mapper, boolean tillTheEnd) {
/*  7448 */     return concatMapCompletableDelayError(mapper, tillTheEnd, 2);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("none")
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @NonNull
/*       */   public final Completable concatMapCompletableDelayError(Function<? super T, ? extends CompletableSource> mapper, boolean tillTheEnd, int prefetch) {
/*  7487 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/*  7488 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/*  7489 */     return RxJavaPlugins.onAssembly((Completable)new FlowableConcatMapCompletable(this, mapper, tillTheEnd ? ErrorMode.END : ErrorMode.BOUNDARY, prefetch));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final <R> Flowable<R> concatMapDelayError(Function<? super T, ? extends Publisher<? extends R>> mapper) {
/*  7517 */     return concatMapDelayError(mapper, 2, true);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <R> Flowable<R> concatMapDelayError(Function<? super T, ? extends Publisher<? extends R>> mapper, int prefetch, boolean tillTheEnd) {
/*  7552 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/*  7553 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/*  7554 */     if (this instanceof ScalarCallable) {
/*       */       
/*  7556 */       T v = (T)((ScalarCallable)this).call();
/*  7557 */       if (v == null) {
/*  7558 */         return empty();
/*       */       }
/*  7560 */       return FlowableScalarXMap.scalarXMap(v, mapper);
/*       */     } 
/*  7562 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableConcatMap(this, mapper, prefetch, tillTheEnd ? ErrorMode.END : ErrorMode.BOUNDARY));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final <R> Flowable<R> concatMapEager(Function<? super T, ? extends Publisher<? extends R>> mapper) {
/*  7589 */     return concatMapEager(mapper, bufferSize(), bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <R> Flowable<R> concatMapEager(Function<? super T, ? extends Publisher<? extends R>> mapper, int maxConcurrency, int prefetch) {
/*  7620 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/*  7621 */     ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
/*  7622 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/*  7623 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableConcatMapEager(this, mapper, maxConcurrency, prefetch, ErrorMode.IMMEDIATE));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final <R> Flowable<R> concatMapEagerDelayError(Function<? super T, ? extends Publisher<? extends R>> mapper, boolean tillTheEnd) {
/*  7654 */     return concatMapEagerDelayError(mapper, bufferSize(), bufferSize(), tillTheEnd);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <R> Flowable<R> concatMapEagerDelayError(Function<? super T, ? extends Publisher<? extends R>> mapper, int maxConcurrency, int prefetch, boolean tillTheEnd) {
/*  7690 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/*  7691 */     ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
/*  7692 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/*  7693 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableConcatMapEager(this, mapper, maxConcurrency, prefetch, tillTheEnd ? ErrorMode.END : ErrorMode.BOUNDARY));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final <U> Flowable<U> concatMapIterable(Function<? super T, ? extends Iterable<? extends U>> mapper) {
/*  7722 */     return concatMapIterable(mapper, 2);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <U> Flowable<U> concatMapIterable(Function<? super T, ? extends Iterable<? extends U>> mapper, int prefetch) {
/*  7754 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/*  7755 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/*  7756 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableFlattenIterable(this, mapper, prefetch));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final <R> Flowable<R> concatMapMaybe(Function<? super T, ? extends MaybeSource<? extends R>> mapper) {
/*  7787 */     return concatMapMaybe(mapper, 2);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <R> Flowable<R> concatMapMaybe(Function<? super T, ? extends MaybeSource<? extends R>> mapper, int prefetch) {
/*  7823 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/*  7824 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/*  7825 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableConcatMapMaybe(this, mapper, ErrorMode.IMMEDIATE, prefetch));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final <R> Flowable<R> concatMapMaybeDelayError(Function<? super T, ? extends MaybeSource<? extends R>> mapper) {
/*  7856 */     return concatMapMaybeDelayError(mapper, true, 2);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final <R> Flowable<R> concatMapMaybeDelayError(Function<? super T, ? extends MaybeSource<? extends R>> mapper, boolean tillTheEnd) {
/*  7893 */     return concatMapMaybeDelayError(mapper, tillTheEnd, 2);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <R> Flowable<R> concatMapMaybeDelayError(Function<? super T, ? extends MaybeSource<? extends R>> mapper, boolean tillTheEnd, int prefetch) {
/*  7934 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/*  7935 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/*  7936 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableConcatMapMaybe(this, mapper, tillTheEnd ? ErrorMode.END : ErrorMode.BOUNDARY, prefetch));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final <R> Flowable<R> concatMapSingle(Function<? super T, ? extends SingleSource<? extends R>> mapper) {
/*  7967 */     return concatMapSingle(mapper, 2);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <R> Flowable<R> concatMapSingle(Function<? super T, ? extends SingleSource<? extends R>> mapper, int prefetch) {
/*  8003 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/*  8004 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/*  8005 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableConcatMapSingle(this, mapper, ErrorMode.IMMEDIATE, prefetch));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final <R> Flowable<R> concatMapSingleDelayError(Function<? super T, ? extends SingleSource<? extends R>> mapper) {
/*  8036 */     return concatMapSingleDelayError(mapper, true, 2);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final <R> Flowable<R> concatMapSingleDelayError(Function<? super T, ? extends SingleSource<? extends R>> mapper, boolean tillTheEnd) {
/*  8073 */     return concatMapSingleDelayError(mapper, tillTheEnd, 2);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <R> Flowable<R> concatMapSingleDelayError(Function<? super T, ? extends SingleSource<? extends R>> mapper, boolean tillTheEnd, int prefetch) {
/*  8114 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/*  8115 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/*  8116 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableConcatMapSingle(this, mapper, tillTheEnd ? ErrorMode.END : ErrorMode.BOUNDARY, prefetch));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Flowable<T> concatWith(Publisher<? extends T> other) {
/*  8144 */     ObjectHelper.requireNonNull(other, "other is null");
/*  8145 */     return concat(this, other);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> concatWith(@NonNull SingleSource<? extends T> other) {
/*  8169 */     ObjectHelper.requireNonNull(other, "other is null");
/*  8170 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableConcatWithSingle(this, other));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> concatWith(@NonNull MaybeSource<? extends T> other) {
/*  8194 */     ObjectHelper.requireNonNull(other, "other is null");
/*  8195 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableConcatWithMaybe(this, other));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> concatWith(@NonNull CompletableSource other) {
/*  8221 */     ObjectHelper.requireNonNull(other, "other is null");
/*  8222 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableConcatWithCompletable(this, other));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Single<Boolean> contains(Object item) {
/*  8249 */     ObjectHelper.requireNonNull(item, "item is null");
/*  8250 */     return any(Functions.equalsWith(item));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final Single<Long> count() {
/*  8274 */     return RxJavaPlugins.onAssembly((Single)new FlowableCountSingle(this));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <U> Flowable<T> debounce(Function<? super T, ? extends Publisher<U>> debounceIndicator) {
/*  8312 */     ObjectHelper.requireNonNull(debounceIndicator, "debounceIndicator is null");
/*  8313 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableDebounce(this, debounceIndicator));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Flowable<T> debounce(long timeout, TimeUnit unit) {
/*  8355 */     return debounce(timeout, unit, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("custom")
/*       */   @NonNull
/*       */   public final Flowable<T> debounce(long timeout, TimeUnit unit, Scheduler scheduler) {
/*  8400 */     ObjectHelper.requireNonNull(unit, "unit is null");
/*  8401 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/*  8402 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableDebounceTimed(this, timeout, unit, scheduler));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Flowable<T> defaultIfEmpty(T defaultItem) {
/*  8431 */     ObjectHelper.requireNonNull(defaultItem, "defaultItem is null");
/*  8432 */     return switchIfEmpty(just(defaultItem));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <U> Flowable<T> delay(Function<? super T, ? extends Publisher<U>> itemDelayIndicator) {
/*  8467 */     ObjectHelper.requireNonNull(itemDelayIndicator, "itemDelayIndicator is null");
/*  8468 */     return flatMap(FlowableInternalHelper.itemDelay(itemDelayIndicator));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Flowable<T> delay(long delay, TimeUnit unit) {
/*  8494 */     return delay(delay, unit, Schedulers.computation(), false);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Flowable<T> delay(long delay, TimeUnit unit, boolean delayError) {
/*  8523 */     return delay(delay, unit, Schedulers.computation(), delayError);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("custom")
/*       */   public final Flowable<T> delay(long delay, TimeUnit unit, Scheduler scheduler) {
/*  8551 */     return delay(delay, unit, scheduler, false);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("custom")
/*       */   @NonNull
/*       */   public final Flowable<T> delay(long delay, TimeUnit unit, Scheduler scheduler, boolean delayError) {
/*  8583 */     ObjectHelper.requireNonNull(unit, "unit is null");
/*  8584 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/*       */     
/*  8586 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableDelay(this, Math.max(0L, delay), unit, scheduler, delayError));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final <U, V> Flowable<T> delay(Publisher<U> subscriptionIndicator, Function<? super T, ? extends Publisher<V>> itemDelayIndicator) {
/*  8626 */     return delaySubscription(subscriptionIndicator).delay(itemDelayIndicator);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <U> Flowable<T> delaySubscription(Publisher<U> subscriptionIndicator) {
/*  8652 */     ObjectHelper.requireNonNull(subscriptionIndicator, "subscriptionIndicator is null");
/*  8653 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableDelaySubscriptionOther(this, subscriptionIndicator));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Flowable<T> delaySubscription(long delay, TimeUnit unit) {
/*  8678 */     return delaySubscription(delay, unit, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("custom")
/*       */   public final Flowable<T> delaySubscription(long delay, TimeUnit unit, Scheduler scheduler) {
/*  8707 */     return delaySubscription(timer(delay, unit, scheduler));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("none")
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @Deprecated
/*       */   public final <T2> Flowable<T2> dematerialize() {
/*  8758 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableDematerialize(this, Functions.identity()));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("none")
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @Experimental
/*       */   @NonNull
/*       */   public final <R> Flowable<R> dematerialize(Function<? super T, Notification<R>> selector) {
/*  8816 */     ObjectHelper.requireNonNull(selector, "selector is null");
/*  8817 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableDematerialize(this, selector));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> distinct() {
/*  8858 */     return distinct(Functions.identity(), Functions.createHashSet());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final <K> Flowable<T> distinct(Function<? super T, K> keySelector) {
/*  8901 */     return distinct(keySelector, Functions.createHashSet());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final <K> Flowable<T> distinct(Function<? super T, K> keySelector, Callable<? extends Collection<? super K>> collectionSupplier) {
/*  8936 */     ObjectHelper.requireNonNull(keySelector, "keySelector is null");
/*  8937 */     ObjectHelper.requireNonNull(collectionSupplier, "collectionSupplier is null");
/*  8938 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableDistinct(this, keySelector, collectionSupplier));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> distinctUntilChanged() {
/*  8979 */     return distinctUntilChanged(Functions.identity());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final <K> Flowable<T> distinctUntilChanged(Function<? super T, K> keySelector) {
/*  9025 */     ObjectHelper.requireNonNull(keySelector, "keySelector is null");
/*  9026 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableDistinctUntilChanged(this, keySelector, ObjectHelper.equalsPredicate()));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> distinctUntilChanged(BiPredicate<? super T, ? super T> comparer) {
/*  9063 */     ObjectHelper.requireNonNull(comparer, "comparer is null");
/*  9064 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableDistinctUntilChanged(this, Functions.identity(), comparer));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> doFinally(Action onFinally) {
/*  9093 */     ObjectHelper.requireNonNull(onFinally, "onFinally is null");
/*  9094 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableDoFinally(this, onFinally));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> doAfterNext(Consumer<? super T> onAfterNext) {
/*  9120 */     ObjectHelper.requireNonNull(onAfterNext, "onAfterNext is null");
/*  9121 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableDoAfterNext(this, onAfterNext));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> doAfterTerminate(Action onAfterTerminate) {
/*  9148 */     return doOnEach(Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, onAfterTerminate);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> doOnCancel(Action onCancel) {
/*  9181 */     return doOnLifecycle(Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, onCancel);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> doOnComplete(Action onComplete) {
/*  9205 */     return doOnEach(Functions.emptyConsumer(), Functions.emptyConsumer(), onComplete, Functions.EMPTY_ACTION);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   private Flowable<T> doOnEach(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete, Action onAfterTerminate) {
/*  9231 */     ObjectHelper.requireNonNull(onNext, "onNext is null");
/*  9232 */     ObjectHelper.requireNonNull(onError, "onError is null");
/*  9233 */     ObjectHelper.requireNonNull(onComplete, "onComplete is null");
/*  9234 */     ObjectHelper.requireNonNull(onAfterTerminate, "onAfterTerminate is null");
/*  9235 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableDoOnEach(this, onNext, onError, onComplete, onAfterTerminate));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Flowable<T> doOnEach(Consumer<? super Notification<T>> onNotification) {
/*  9260 */     ObjectHelper.requireNonNull(onNotification, "onNotification is null");
/*  9261 */     return doOnEach(
/*  9262 */         Functions.notificationOnNext(onNotification), 
/*  9263 */         Functions.notificationOnError(onNotification), 
/*  9264 */         Functions.notificationOnComplete(onNotification), Functions.EMPTY_ACTION);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Flowable<T> doOnEach(Subscriber<? super T> subscriber) {
/*  9297 */     ObjectHelper.requireNonNull(subscriber, "subscriber is null");
/*  9298 */     return doOnEach(
/*  9299 */         FlowableInternalHelper.subscriberOnNext(subscriber), 
/*  9300 */         FlowableInternalHelper.subscriberOnError(subscriber), 
/*  9301 */         FlowableInternalHelper.subscriberOnComplete(subscriber), Functions.EMPTY_ACTION);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> doOnError(Consumer<? super Throwable> onError) {
/*  9329 */     return doOnEach(Functions.emptyConsumer(), onError, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Flowable<T> doOnLifecycle(Consumer<? super Subscription> onSubscribe, LongConsumer onRequest, Action onCancel) {
/*  9361 */     ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");
/*  9362 */     ObjectHelper.requireNonNull(onRequest, "onRequest is null");
/*  9363 */     ObjectHelper.requireNonNull(onCancel, "onCancel is null");
/*  9364 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableDoOnLifecycle(this, onSubscribe, onRequest, onCancel));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> doOnNext(Consumer<? super T> onNext) {
/*  9388 */     return doOnEach(onNext, Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> doOnRequest(LongConsumer onRequest) {
/*  9418 */     return doOnLifecycle(Functions.emptyConsumer(), onRequest, Functions.EMPTY_ACTION);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> doOnSubscribe(Consumer<? super Subscription> onSubscribe) {
/*  9445 */     return doOnLifecycle(onSubscribe, Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> doOnTerminate(Action onTerminate) {
/*  9474 */     return doOnEach(Functions.emptyConsumer(), Functions.actionConsumer(onTerminate), onTerminate, Functions.EMPTY_ACTION);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final Maybe<T> elementAt(long index) {
/*  9501 */     if (index < 0L) {
/*  9502 */       throw new IndexOutOfBoundsException("index >= 0 required but it was " + index);
/*       */     }
/*  9504 */     return RxJavaPlugins.onAssembly((Maybe)new FlowableElementAtMaybe(this, index));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Single<T> elementAt(long index, T defaultItem) {
/*  9535 */     if (index < 0L) {
/*  9536 */       throw new IndexOutOfBoundsException("index >= 0 required but it was " + index);
/*       */     }
/*  9538 */     ObjectHelper.requireNonNull(defaultItem, "defaultItem is null");
/*  9539 */     return RxJavaPlugins.onAssembly((Single)new FlowableElementAtSingle(this, index, defaultItem));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final Single<T> elementAtOrError(long index) {
/*  9567 */     if (index < 0L) {
/*  9568 */       throw new IndexOutOfBoundsException("index >= 0 required but it was " + index);
/*       */     }
/*  9570 */     return RxJavaPlugins.onAssembly((Single)new FlowableElementAtSingle(this, index, null));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Flowable<T> filter(Predicate<? super T> predicate) {
/*  9597 */     ObjectHelper.requireNonNull(predicate, "predicate is null");
/*  9598 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableFilter(this, predicate));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.SPECIAL)
/*       */   @SchedulerSupport("none")
/*       */   public final Maybe<T> firstElement() {
/*  9621 */     return elementAt(0L);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.SPECIAL)
/*       */   @SchedulerSupport("none")
/*       */   public final Single<T> first(T defaultItem) {
/*  9647 */     return elementAt(0L, defaultItem);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.SPECIAL)
/*       */   @SchedulerSupport("none")
/*       */   public final Single<T> firstOrError() {
/*  9670 */     return elementAtOrError(0L);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final <R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> mapper) {
/*  9702 */     return flatMap(mapper, false, bufferSize(), bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final <R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> mapper, boolean delayErrors) {
/*  9737 */     return flatMap(mapper, delayErrors, bufferSize(), bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final <R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> mapper, int maxConcurrency) {
/*  9773 */     return flatMap(mapper, false, maxConcurrency, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final <R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> mapper, boolean delayErrors, int maxConcurrency) {
/*  9812 */     return flatMap(mapper, delayErrors, maxConcurrency, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> mapper, boolean delayErrors, int maxConcurrency, int bufferSize) {
/*  9855 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/*  9856 */     ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
/*  9857 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/*  9858 */     if (this instanceof ScalarCallable) {
/*       */       
/*  9860 */       T v = (T)((ScalarCallable)this).call();
/*  9861 */       if (v == null) {
/*  9862 */         return empty();
/*       */       }
/*  9864 */       return FlowableScalarXMap.scalarXMap(v, mapper);
/*       */     } 
/*  9866 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableFlatMap(this, mapper, delayErrors, maxConcurrency, bufferSize));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> onNextMapper, Function<? super Throwable, ? extends Publisher<? extends R>> onErrorMapper, Callable<? extends Publisher<? extends R>> onCompleteSupplier) {
/*  9906 */     ObjectHelper.requireNonNull(onNextMapper, "onNextMapper is null");
/*  9907 */     ObjectHelper.requireNonNull(onErrorMapper, "onErrorMapper is null");
/*  9908 */     ObjectHelper.requireNonNull(onCompleteSupplier, "onCompleteSupplier is null");
/*  9909 */     return merge((Publisher<? extends Publisher<? extends R>>)new FlowableMapNotification(this, onNextMapper, onErrorMapper, onCompleteSupplier));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> onNextMapper, Function<Throwable, ? extends Publisher<? extends R>> onErrorMapper, Callable<? extends Publisher<? extends R>> onCompleteSupplier, int maxConcurrency) {
/*  9954 */     ObjectHelper.requireNonNull(onNextMapper, "onNextMapper is null");
/*  9955 */     ObjectHelper.requireNonNull(onErrorMapper, "onErrorMapper is null");
/*  9956 */     ObjectHelper.requireNonNull(onCompleteSupplier, "onCompleteSupplier is null");
/*  9957 */     return merge((Publisher<? extends Publisher<? extends R>>)new FlowableMapNotification(this, onNextMapper, onErrorMapper, onCompleteSupplier), maxConcurrency);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final <U, R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends R> combiner) {
/*  9994 */     return flatMap(mapper, combiner, false, bufferSize(), bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final <U, R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends R> combiner, boolean delayErrors) {
/* 10033 */     return flatMap(mapper, combiner, delayErrors, bufferSize(), bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final <U, R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends R> combiner, boolean delayErrors, int maxConcurrency) {
/* 10076 */     return flatMap(mapper, combiner, delayErrors, maxConcurrency, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <U, R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends R> combiner, boolean delayErrors, int maxConcurrency, int bufferSize) {
/* 10122 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 10123 */     ObjectHelper.requireNonNull(combiner, "combiner is null");
/* 10124 */     ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
/* 10125 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 10126 */     return flatMap(FlowableInternalHelper.flatMapWithCombiner(mapper, combiner), delayErrors, maxConcurrency, bufferSize);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final <U, R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends R> combiner, int maxConcurrency) {
/* 10166 */     return flatMap(mapper, combiner, false, maxConcurrency, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final Completable flatMapCompletable(Function<? super T, ? extends CompletableSource> mapper) {
/* 10185 */     return flatMapCompletable(mapper, false, 2147483647);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Completable flatMapCompletable(Function<? super T, ? extends CompletableSource> mapper, boolean delayErrors, int maxConcurrency) {
/* 10210 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 10211 */     ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
/* 10212 */     return RxJavaPlugins.onAssembly((Completable)new FlowableFlatMapCompletableCompletable(this, mapper, delayErrors, maxConcurrency));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final <U> Flowable<U> flatMapIterable(Function<? super T, ? extends Iterable<? extends U>> mapper) {
/* 10242 */     return flatMapIterable(mapper, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <U> Flowable<U> flatMapIterable(Function<? super T, ? extends Iterable<? extends U>> mapper, int bufferSize) {
/* 10275 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 10276 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 10277 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableFlattenIterable(this, mapper, bufferSize));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <U, V> Flowable<V> flatMapIterable(Function<? super T, ? extends Iterable<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends V> resultSelector) {
/* 10313 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 10314 */     ObjectHelper.requireNonNull(resultSelector, "resultSelector is null");
/* 10315 */     return flatMap(FlowableInternalHelper.flatMapIntoIterable(mapper), resultSelector, false, bufferSize(), bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <U, V> Flowable<V> flatMapIterable(Function<? super T, ? extends Iterable<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends V> resultSelector, int prefetch) {
/* 10356 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 10357 */     ObjectHelper.requireNonNull(resultSelector, "resultSelector is null");
/* 10358 */     return flatMap(FlowableInternalHelper.flatMapIntoIterable(mapper), resultSelector, false, bufferSize(), prefetch);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final <R> Flowable<R> flatMapMaybe(Function<? super T, ? extends MaybeSource<? extends R>> mapper) {
/* 10378 */     return flatMapMaybe(mapper, false, 2147483647);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <R> Flowable<R> flatMapMaybe(Function<? super T, ? extends MaybeSource<? extends R>> mapper, boolean delayErrors, int maxConcurrency) {
/* 10405 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 10406 */     ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
/* 10407 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableFlatMapMaybe(this, mapper, delayErrors, maxConcurrency));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final <R> Flowable<R> flatMapSingle(Function<? super T, ? extends SingleSource<? extends R>> mapper) {
/* 10427 */     return flatMapSingle(mapper, false, 2147483647);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <R> Flowable<R> flatMapSingle(Function<? super T, ? extends SingleSource<? extends R>> mapper, boolean delayErrors, int maxConcurrency) {
/* 10454 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 10455 */     ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
/* 10456 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableFlatMapSingle(this, mapper, delayErrors, maxConcurrency));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.NONE)
/*       */   @SchedulerSupport("none")
/*       */   public final Disposable forEach(Consumer<? super T> onNext) {
/* 10483 */     return subscribe(onNext);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.NONE)
/*       */   @SchedulerSupport("none")
/*       */   public final Disposable forEachWhile(Predicate<? super T> onNext) {
/* 10513 */     return forEachWhile(onNext, Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.NONE)
/*       */   @SchedulerSupport("none")
/*       */   public final Disposable forEachWhile(Predicate<? super T> onNext, Consumer<? super Throwable> onError) {
/* 10542 */     return forEachWhile(onNext, onError, Functions.EMPTY_ACTION);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.NONE)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Disposable forEachWhile(Predicate<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete) {
/* 10576 */     ObjectHelper.requireNonNull(onNext, "onNext is null");
/* 10577 */     ObjectHelper.requireNonNull(onError, "onError is null");
/* 10578 */     ObjectHelper.requireNonNull(onComplete, "onComplete is null");
/*       */     
/* 10580 */     ForEachWhileSubscriber<T> s = new ForEachWhileSubscriber(onNext, onError, onComplete);
/* 10581 */     subscribe((FlowableSubscriber<? super T>)s);
/* 10582 */     return (Disposable)s;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final <K> Flowable<GroupedFlowable<K, T>> groupBy(Function<? super T, ? extends K> keySelector) {
/* 10630 */     return groupBy(keySelector, Functions.identity(), false, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final <K> Flowable<GroupedFlowable<K, T>> groupBy(Function<? super T, ? extends K> keySelector, boolean delayError) {
/* 10680 */     return groupBy(keySelector, Functions.identity(), delayError, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final <K, V> Flowable<GroupedFlowable<K, V>> groupBy(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector) {
/* 10733 */     return groupBy(keySelector, valueSelector, false, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final <K, V> Flowable<GroupedFlowable<K, V>> groupBy(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector, boolean delayError) {
/* 10789 */     return groupBy(keySelector, valueSelector, delayError, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <K, V> Flowable<GroupedFlowable<K, V>> groupBy(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector, boolean delayError, int bufferSize) {
/* 10849 */     ObjectHelper.requireNonNull(keySelector, "keySelector is null");
/* 10850 */     ObjectHelper.requireNonNull(valueSelector, "valueSelector is null");
/* 10851 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/*       */     
/* 10853 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableGroupBy(this, keySelector, valueSelector, bufferSize, delayError, null));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <K, V> Flowable<GroupedFlowable<K, V>> groupBy(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector, boolean delayError, int bufferSize, Function<? super Consumer<Object>, ? extends Map<K, Object>> evictingMapFactory) {
/* 10963 */     ObjectHelper.requireNonNull(keySelector, "keySelector is null");
/* 10964 */     ObjectHelper.requireNonNull(valueSelector, "valueSelector is null");
/* 10965 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 10966 */     ObjectHelper.requireNonNull(evictingMapFactory, "evictingMapFactory is null");
/*       */     
/* 10968 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableGroupBy(this, keySelector, valueSelector, bufferSize, delayError, evictingMapFactory));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <TRight, TLeftEnd, TRightEnd, R> Flowable<R> groupJoin(Publisher<? extends TRight> other, Function<? super T, ? extends Publisher<TLeftEnd>> leftEnd, Function<? super TRight, ? extends Publisher<TRightEnd>> rightEnd, BiFunction<? super T, ? super Flowable<TRight>, ? extends R> resultSelector) {
/* 11014 */     ObjectHelper.requireNonNull(other, "other is null");
/* 11015 */     ObjectHelper.requireNonNull(leftEnd, "leftEnd is null");
/* 11016 */     ObjectHelper.requireNonNull(rightEnd, "rightEnd is null");
/* 11017 */     ObjectHelper.requireNonNull(resultSelector, "resultSelector is null");
/* 11018 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableGroupJoin(this, other, leftEnd, rightEnd, resultSelector));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> hide() {
/* 11042 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableHide(this));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final Completable ignoreElements() {
/* 11065 */     return RxJavaPlugins.onAssembly((Completable)new FlowableIgnoreElementsCompletable(this));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final Single<Boolean> isEmpty() {
/* 11090 */     return all(Functions.alwaysFalse());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <TRight, TLeftEnd, TRightEnd, R> Flowable<R> join(Publisher<? extends TRight> other, Function<? super T, ? extends Publisher<TLeftEnd>> leftEnd, Function<? super TRight, ? extends Publisher<TRightEnd>> rightEnd, BiFunction<? super T, ? super TRight, ? extends R> resultSelector) {
/* 11136 */     ObjectHelper.requireNonNull(other, "other is null");
/* 11137 */     ObjectHelper.requireNonNull(leftEnd, "leftEnd is null");
/* 11138 */     ObjectHelper.requireNonNull(rightEnd, "rightEnd is null");
/* 11139 */     ObjectHelper.requireNonNull(resultSelector, "resultSelector is null");
/* 11140 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableJoin(this, other, leftEnd, rightEnd, resultSelector));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final Maybe<T> lastElement() {
/* 11164 */     return RxJavaPlugins.onAssembly((Maybe)new FlowableLastMaybe(this));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Single<T> last(T defaultItem) {
/* 11190 */     ObjectHelper.requireNonNull(defaultItem, "defaultItem");
/* 11191 */     return RxJavaPlugins.onAssembly((Single)new FlowableLastSingle(this, defaultItem));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final Single<T> lastOrError() {
/* 11214 */     return RxJavaPlugins.onAssembly((Single)new FlowableLastSingle(this, null));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.SPECIAL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <R> Flowable<R> lift(FlowableOperator<? extends R, ? super T> lifter) {
/* 11368 */     ObjectHelper.requireNonNull(lifter, "lifter is null");
/* 11369 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableLift(this, lifter));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @BackpressureSupport(BackpressureKind.SPECIAL)
/*       */   @SchedulerSupport("none")
/*       */   @CheckReturnValue
/*       */   public final Flowable<T> limit(long count) {
/* 11412 */     if (count < 0L) {
/* 11413 */       throw new IllegalArgumentException("count >= 0 required but it was " + count);
/*       */     }
/* 11415 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableLimit(this, count));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <R> Flowable<R> map(Function<? super T, ? extends R> mapper) {
/* 11443 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 11444 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableMap(this, mapper));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<Notification<T>> materialize() {
/* 11469 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableMaterialize(this));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Flowable<T> mergeWith(Publisher<? extends T> other) {
/* 11497 */     ObjectHelper.requireNonNull(other, "other is null");
/* 11498 */     return merge(this, other);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Flowable<T> mergeWith(@NonNull SingleSource<? extends T> other) {
/* 11525 */     ObjectHelper.requireNonNull(other, "other is null");
/* 11526 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableMergeWithSingle(this, other));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Flowable<T> mergeWith(@NonNull MaybeSource<? extends T> other) {
/* 11554 */     ObjectHelper.requireNonNull(other, "other is null");
/* 11555 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableMergeWithMaybe(this, other));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Flowable<T> mergeWith(@NonNull CompletableSource other) {
/* 11580 */     ObjectHelper.requireNonNull(other, "other is null");
/* 11581 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableMergeWithCompletable(this, other));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("custom")
/*       */   public final Flowable<T> observeOn(Scheduler scheduler) {
/* 11623 */     return observeOn(scheduler, false, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("custom")
/*       */   public final Flowable<T> observeOn(Scheduler scheduler, boolean delayError) {
/* 11666 */     return observeOn(scheduler, delayError, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("custom")
/*       */   @NonNull
/*       */   public final Flowable<T> observeOn(Scheduler scheduler, boolean delayError, int bufferSize) {
/* 11711 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 11712 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 11713 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableObserveOn(this, scheduler, delayError, bufferSize));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <U> Flowable<U> ofType(Class<U> clazz) {
/* 11739 */     ObjectHelper.requireNonNull(clazz, "clazz is null");
/* 11740 */     return filter(Functions.isInstanceOf(clazz)).cast(clazz);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> onBackpressureBuffer() {
/* 11763 */     return onBackpressureBuffer(bufferSize(), false, true);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> onBackpressureBuffer(boolean delayError) {
/* 11789 */     return onBackpressureBuffer(bufferSize(), delayError, true);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> onBackpressureBuffer(int capacity) {
/* 11816 */     return onBackpressureBuffer(capacity, false, false);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> onBackpressureBuffer(int capacity, boolean delayError) {
/* 11847 */     return onBackpressureBuffer(capacity, delayError, false);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.SPECIAL)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> onBackpressureBuffer(int capacity, boolean delayError, boolean unbounded) {
/* 11880 */     ObjectHelper.verifyPositive(capacity, "capacity");
/* 11881 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableOnBackpressureBuffer(this, capacity, unbounded, delayError, Functions.EMPTY_ACTION));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.SPECIAL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Flowable<T> onBackpressureBuffer(int capacity, boolean delayError, boolean unbounded, Action onOverflow) {
/* 11917 */     ObjectHelper.requireNonNull(onOverflow, "onOverflow is null");
/* 11918 */     ObjectHelper.verifyPositive(capacity, "capacity");
/* 11919 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableOnBackpressureBuffer(this, capacity, unbounded, delayError, onOverflow));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> onBackpressureBuffer(int capacity, Action onOverflow) {
/* 11947 */     return onBackpressureBuffer(capacity, false, false, onOverflow);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.SPECIAL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Flowable<T> onBackpressureBuffer(long capacity, Action onOverflow, BackpressureOverflowStrategy overflowStrategy) {
/* 11988 */     ObjectHelper.requireNonNull(overflowStrategy, "overflowStrategy is null");
/* 11989 */     ObjectHelper.verifyPositive(capacity, "capacity");
/* 11990 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableOnBackpressureBufferStrategy(this, capacity, onOverflow, overflowStrategy));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> onBackpressureDrop() {
/* 12016 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableOnBackpressureDrop(this));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Flowable<T> onBackpressureDrop(Consumer<? super T> onDrop) {
/* 12045 */     ObjectHelper.requireNonNull(onDrop, "onDrop is null");
/* 12046 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableOnBackpressureDrop(this, onDrop));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> onBackpressureLatest() {
/* 12078 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableOnBackpressureLatest(this));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Flowable<T> onErrorResumeNext(Function<? super Throwable, ? extends Publisher<? extends T>> resumeFunction) {
/* 12121 */     ObjectHelper.requireNonNull(resumeFunction, "resumeFunction is null");
/* 12122 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableOnErrorNext(this, resumeFunction, false));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Flowable<T> onErrorResumeNext(Publisher<? extends T> next) {
/* 12165 */     ObjectHelper.requireNonNull(next, "next is null");
/* 12166 */     return onErrorResumeNext(Functions.justFunction(next));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Flowable<T> onErrorReturn(Function<? super Throwable, ? extends T> valueSupplier) {
/* 12205 */     ObjectHelper.requireNonNull(valueSupplier, "valueSupplier is null");
/* 12206 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableOnErrorReturn(this, valueSupplier));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Flowable<T> onErrorReturnItem(T item) {
/* 12245 */     ObjectHelper.requireNonNull(item, "item is null");
/* 12246 */     return onErrorReturn(Functions.justFunction(item));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Flowable<T> onExceptionResumeNext(Publisher<? extends T> next) {
/* 12292 */     ObjectHelper.requireNonNull(next, "next is null");
/* 12293 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableOnErrorNext(this, Functions.justFunction(next), true));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> onTerminateDetach() {
/* 12314 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableDetach(this));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @CheckReturnValue
/*       */   public final ParallelFlowable<T> parallel() {
/* 12343 */     return ParallelFlowable.from(this);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @CheckReturnValue
/*       */   public final ParallelFlowable<T> parallel(int parallelism) {
/* 12373 */     ObjectHelper.verifyPositive(parallelism, "parallelism");
/* 12374 */     return ParallelFlowable.from(this, parallelism);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @CheckReturnValue
/*       */   public final ParallelFlowable<T> parallel(int parallelism, int prefetch) {
/* 12406 */     ObjectHelper.verifyPositive(parallelism, "parallelism");
/* 12407 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/* 12408 */     return ParallelFlowable.from(this, parallelism, prefetch);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final ConnectableFlowable<T> publish() {
/* 12434 */     return publish(bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final <R> Flowable<R> publish(Function<? super Flowable<T>, ? extends Publisher<R>> selector) {
/* 12466 */     return publish(selector, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <R> Flowable<R> publish(Function<? super Flowable<T>, ? extends Publisher<? extends R>> selector, int prefetch) {
/* 12501 */     ObjectHelper.requireNonNull(selector, "selector is null");
/* 12502 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/* 12503 */     return RxJavaPlugins.onAssembly((Flowable)new FlowablePublishMulticast(this, selector, prefetch, false));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final ConnectableFlowable<T> publish(int bufferSize) {
/* 12531 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 12532 */     return FlowablePublish.create(this, bufferSize);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> rebatchRequests(int n) {
/* 12557 */     return observeOn(ImmediateThinScheduler.INSTANCE, true, n);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Maybe<T> reduce(BiFunction<T, T, T> reducer) {
/* 12596 */     ObjectHelper.requireNonNull(reducer, "reducer is null");
/* 12597 */     return RxJavaPlugins.onAssembly((Maybe)new FlowableReduceMaybe(this, reducer));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <R> Single<R> reduce(R seed, BiFunction<R, ? super T, R> reducer) {
/* 12658 */     ObjectHelper.requireNonNull(seed, "seed is null");
/* 12659 */     ObjectHelper.requireNonNull(reducer, "reducer is null");
/* 12660 */     return RxJavaPlugins.onAssembly((Single)new FlowableReduceSeedSingle(this, seed, reducer));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <R> Single<R> reduceWith(Callable<R> seedSupplier, BiFunction<R, ? super T, R> reducer) {
/* 12703 */     ObjectHelper.requireNonNull(seedSupplier, "seedSupplier is null");
/* 12704 */     ObjectHelper.requireNonNull(reducer, "reducer is null");
/* 12705 */     return RxJavaPlugins.onAssembly((Single)new FlowableReduceWithSingle(this, seedSupplier, reducer));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> repeat() {
/* 12727 */     return repeat(Long.MAX_VALUE);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> repeat(long times) {
/* 12756 */     if (times < 0L) {
/* 12757 */       throw new IllegalArgumentException("times >= 0 required but it was " + times);
/*       */     }
/* 12759 */     if (times == 0L) {
/* 12760 */       return empty();
/*       */     }
/* 12762 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableRepeat(this, times));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Flowable<T> repeatUntil(BooleanSupplier stop) {
/* 12791 */     ObjectHelper.requireNonNull(stop, "stop is null");
/* 12792 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableRepeatUntil(this, stop));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Flowable<T> repeatWhen(Function<? super Flowable<Object>, ? extends Publisher<?>> handler) {
/* 12822 */     ObjectHelper.requireNonNull(handler, "handler is null");
/* 12823 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableRepeatWhen(this, handler));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final ConnectableFlowable<T> replay() {
/* 12850 */     return FlowableReplay.createFrom(this);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> selector) {
/* 12881 */     ObjectHelper.requireNonNull(selector, "selector is null");
/* 12882 */     return FlowableReplay.multicastSelector(FlowableInternalHelper.replayCallable(this), selector);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> selector, int bufferSize) {
/* 12920 */     ObjectHelper.requireNonNull(selector, "selector is null");
/* 12921 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 12922 */     return FlowableReplay.multicastSelector(FlowableInternalHelper.replayCallable(this, bufferSize), selector);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> selector, int bufferSize, long time, TimeUnit unit) {
/* 12964 */     return replay(selector, bufferSize, time, unit, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("custom")
/*       */   @NonNull
/*       */   public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> selector, int bufferSize, long time, TimeUnit unit, Scheduler scheduler) {
/* 13011 */     ObjectHelper.requireNonNull(selector, "selector is null");
/* 13012 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 13013 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 13014 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 13015 */     return FlowableReplay.multicastSelector(
/* 13016 */         FlowableInternalHelper.replayCallable(this, bufferSize, time, unit, scheduler), selector);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("custom")
/*       */   @NonNull
/*       */   public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> selector, int bufferSize, Scheduler scheduler) {
/* 13056 */     ObjectHelper.requireNonNull(selector, "selector is null");
/* 13057 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 13058 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 13059 */     return FlowableReplay.multicastSelector(FlowableInternalHelper.replayCallable(this, bufferSize), 
/* 13060 */         FlowableInternalHelper.replayFunction(selector, scheduler));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> selector, long time, TimeUnit unit) {
/* 13097 */     return replay(selector, time, unit, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("custom")
/*       */   @NonNull
/*       */   public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> selector, long time, TimeUnit unit, Scheduler scheduler) {
/* 13136 */     ObjectHelper.requireNonNull(selector, "selector is null");
/* 13137 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 13138 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 13139 */     return FlowableReplay.multicastSelector(FlowableInternalHelper.replayCallable(this, time, unit, scheduler), selector);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("custom")
/*       */   @NonNull
/*       */   public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> selector, Scheduler scheduler) {
/* 13173 */     ObjectHelper.requireNonNull(selector, "selector is null");
/* 13174 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 13175 */     return FlowableReplay.multicastSelector(FlowableInternalHelper.replayCallable(this), 
/* 13176 */         FlowableInternalHelper.replayFunction(selector, scheduler));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final ConnectableFlowable<T> replay(int bufferSize) {
/* 13208 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 13209 */     return FlowableReplay.create(this, bufferSize);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final ConnectableFlowable<T> replay(int bufferSize, long time, TimeUnit unit) {
/* 13246 */     return replay(bufferSize, time, unit, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("custom")
/*       */   public final ConnectableFlowable<T> replay(int bufferSize, long time, TimeUnit unit, Scheduler scheduler) {
/* 13287 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 13288 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 13289 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 13290 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 13291 */     return FlowableReplay.create(this, time, unit, scheduler, bufferSize);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("custom")
/*       */   public final ConnectableFlowable<T> replay(int bufferSize, Scheduler scheduler) {
/* 13325 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 13326 */     return FlowableReplay.observeOn(replay(bufferSize), scheduler);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final ConnectableFlowable<T> replay(long time, TimeUnit unit) {
/* 13357 */     return replay(time, unit, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("custom")
/*       */   public final ConnectableFlowable<T> replay(long time, TimeUnit unit, Scheduler scheduler) {
/* 13390 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 13391 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 13392 */     return FlowableReplay.create(this, time, unit, scheduler);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("custom")
/*       */   public final ConnectableFlowable<T> replay(Scheduler scheduler) {
/* 13422 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 13423 */     return FlowableReplay.observeOn(replay(), scheduler);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> retry() {
/* 13454 */     return retry(Long.MAX_VALUE, Functions.alwaysTrue());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Flowable<T> retry(BiPredicate<? super Integer, ? super Throwable> predicate) {
/* 13482 */     ObjectHelper.requireNonNull(predicate, "predicate is null");
/*       */     
/* 13484 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableRetryBiPredicate(this, predicate));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> retry(long count) {
/* 13518 */     return retry(count, Functions.alwaysTrue());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Flowable<T> retry(long times, Predicate<? super Throwable> predicate) {
/* 13540 */     if (times < 0L) {
/* 13541 */       throw new IllegalArgumentException("times >= 0 required but it was " + times);
/*       */     }
/* 13543 */     ObjectHelper.requireNonNull(predicate, "predicate is null");
/*       */     
/* 13545 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableRetryPredicate(this, times, predicate));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> retry(Predicate<? super Throwable> predicate) {
/* 13565 */     return retry(Long.MAX_VALUE, predicate);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Flowable<T> retryUntil(BooleanSupplier stop) {
/* 13585 */     ObjectHelper.requireNonNull(stop, "stop is null");
/* 13586 */     return retry(Long.MAX_VALUE, Functions.predicateReverseFor(stop));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Flowable<T> retryWhen(Function<? super Flowable<Throwable>, ? extends Publisher<?>> handler) {
/* 13672 */     ObjectHelper.requireNonNull(handler, "handler is null");
/*       */     
/* 13674 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableRetryWhen(this, handler));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   public final void safeSubscribe(Subscriber<? super T> s) {
/* 13694 */     ObjectHelper.requireNonNull(s, "s is null");
/* 13695 */     if (s instanceof SafeSubscriber) {
/* 13696 */       subscribe((FlowableSubscriber<? super T>)s);
/*       */     } else {
/* 13698 */       subscribe((FlowableSubscriber<? super T>)new SafeSubscriber(s));
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Flowable<T> sample(long period, TimeUnit unit) {
/* 13728 */     return sample(period, unit, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Flowable<T> sample(long period, TimeUnit unit, boolean emitLast) {
/* 13763 */     return sample(period, unit, Schedulers.computation(), emitLast);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("custom")
/*       */   @NonNull
/*       */   public final Flowable<T> sample(long period, TimeUnit unit, Scheduler scheduler) {
/* 13795 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 13796 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 13797 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableSampleTimed(this, period, unit, scheduler, false));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("custom")
/*       */   @NonNull
/*       */   public final Flowable<T> sample(long period, TimeUnit unit, Scheduler scheduler, boolean emitLast) {
/* 13836 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 13837 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 13838 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableSampleTimed(this, period, unit, scheduler, emitLast));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <U> Flowable<T> sample(Publisher<U> sampler) {
/* 13868 */     ObjectHelper.requireNonNull(sampler, "sampler is null");
/* 13869 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableSamplePublisher(this, sampler, false));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <U> Flowable<T> sample(Publisher<U> sampler, boolean emitLast) {
/* 13906 */     ObjectHelper.requireNonNull(sampler, "sampler is null");
/* 13907 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableSamplePublisher(this, sampler, emitLast));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Flowable<T> scan(BiFunction<T, T, T> accumulator) {
/* 13939 */     ObjectHelper.requireNonNull(accumulator, "accumulator is null");
/* 13940 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableScan(this, accumulator));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <R> Flowable<R> scan(R initialValue, BiFunction<R, ? super T, R> accumulator) {
/* 13993 */     ObjectHelper.requireNonNull(initialValue, "initialValue is null");
/* 13994 */     return scanWith(Functions.justCallable(initialValue), accumulator);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <R> Flowable<R> scanWith(Callable<R> seedSupplier, BiFunction<R, ? super T, R> accumulator) {
/* 14033 */     ObjectHelper.requireNonNull(seedSupplier, "seedSupplier is null");
/* 14034 */     ObjectHelper.requireNonNull(accumulator, "accumulator is null");
/* 14035 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableScanSeed(this, seedSupplier, accumulator));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> serialize() {
/* 14065 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableSerialized(this));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> share() {
/* 14093 */     return publish().refCount();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final Maybe<T> singleElement() {
/* 14117 */     return RxJavaPlugins.onAssembly((Maybe)new FlowableSingleMaybe(this));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Single<T> single(T defaultItem) {
/* 14145 */     ObjectHelper.requireNonNull(defaultItem, "defaultItem is null");
/* 14146 */     return RxJavaPlugins.onAssembly((Single)new FlowableSingleSingle(this, defaultItem));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final Single<T> singleOrError() {
/* 14171 */     return RxJavaPlugins.onAssembly((Single)new FlowableSingleSingle(this, null));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> skip(long count) {
/* 14197 */     if (count <= 0L) {
/* 14198 */       return RxJavaPlugins.onAssembly(this);
/*       */     }
/* 14200 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableSkip(this, count));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> skip(long time, TimeUnit unit) {
/* 14229 */     return skipUntil(timer(time, unit));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("custom")
/*       */   public final Flowable<T> skip(long time, TimeUnit unit, Scheduler scheduler) {
/* 14259 */     return skipUntil(timer(time, unit, scheduler));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> skipLast(int count) {
/* 14291 */     if (count < 0) {
/* 14292 */       throw new IndexOutOfBoundsException("count >= 0 required but it was " + count);
/*       */     }
/* 14294 */     if (count == 0) {
/* 14295 */       return RxJavaPlugins.onAssembly(this);
/*       */     }
/* 14297 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableSkipLast(this, count));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> skipLast(long time, TimeUnit unit) {
/* 14328 */     return skipLast(time, unit, Schedulers.computation(), false, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> skipLast(long time, TimeUnit unit, boolean delayError) {
/* 14362 */     return skipLast(time, unit, Schedulers.computation(), delayError, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("custom")
/*       */   public final Flowable<T> skipLast(long time, TimeUnit unit, Scheduler scheduler) {
/* 14394 */     return skipLast(time, unit, scheduler, false, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("custom")
/*       */   public final Flowable<T> skipLast(long time, TimeUnit unit, Scheduler scheduler, boolean delayError) {
/* 14429 */     return skipLast(time, unit, scheduler, delayError, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("custom")
/*       */   @NonNull
/*       */   public final Flowable<T> skipLast(long time, TimeUnit unit, Scheduler scheduler, boolean delayError, int bufferSize) {
/* 14467 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 14468 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 14469 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/*       */     
/* 14471 */     int s = bufferSize << 1;
/* 14472 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableSkipLastTimed(this, time, unit, scheduler, s, delayError));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <U> Flowable<T> skipUntil(Publisher<U> other) {
/* 14501 */     ObjectHelper.requireNonNull(other, "other is null");
/* 14502 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableSkipUntil(this, other));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Flowable<T> skipWhile(Predicate<? super T> predicate) {
/* 14529 */     ObjectHelper.requireNonNull(predicate, "predicate is null");
/* 14530 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableSkipWhile(this, predicate));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> sorted() {
/* 14557 */     return toList().toFlowable().map(Functions.listSorter(Functions.naturalComparator())).flatMapIterable(Functions.identity());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Flowable<T> sorted(Comparator<? super T> sortFunction) {
/* 14585 */     ObjectHelper.requireNonNull(sortFunction, "sortFunction");
/* 14586 */     return toList().toFlowable().map(Functions.listSorter(sortFunction)).flatMapIterable(Functions.identity());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> startWith(Iterable<? extends T> items) {
/* 14614 */     return concatArray((Publisher<? extends T>[])new Publisher[] { fromIterable(items), this });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Flowable<T> startWith(Publisher<? extends T> other) {
/* 14643 */     ObjectHelper.requireNonNull(other, "other is null");
/* 14644 */     return concatArray((Publisher<? extends T>[])new Publisher[] { other, this });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Flowable<T> startWith(T value) {
/* 14673 */     ObjectHelper.requireNonNull(value, "value is null");
/* 14674 */     return concatArray((Publisher<? extends T>[])new Publisher[] { just(value), this });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> startWithArray(T... items) {
/* 14702 */     Flowable<T> fromArray = fromArray(items);
/* 14703 */     if (fromArray == empty()) {
/* 14704 */       return RxJavaPlugins.onAssembly(this);
/*       */     }
/* 14706 */     return concatArray((Publisher<? extends T>[])new Publisher[] { fromArray, this });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final Disposable subscribe() {
/* 14730 */     return subscribe(Functions.emptyConsumer(), Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION, (Consumer<? super Subscription>)FlowableInternalHelper.RequestMax.INSTANCE);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final Disposable subscribe(Consumer<? super T> onNext) {
/* 14760 */     return subscribe(onNext, Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION, (Consumer<? super Subscription>)FlowableInternalHelper.RequestMax.INSTANCE);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError) {
/* 14791 */     return subscribe(onNext, onError, Functions.EMPTY_ACTION, (Consumer<? super Subscription>)FlowableInternalHelper.RequestMax.INSTANCE);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete) {
/* 14826 */     return subscribe(onNext, onError, onComplete, (Consumer<? super Subscription>)FlowableInternalHelper.RequestMax.INSTANCE);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.SPECIAL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete, Consumer<? super Subscription> onSubscribe) {
/* 14865 */     ObjectHelper.requireNonNull(onNext, "onNext is null");
/* 14866 */     ObjectHelper.requireNonNull(onError, "onError is null");
/* 14867 */     ObjectHelper.requireNonNull(onComplete, "onComplete is null");
/* 14868 */     ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");
/*       */     
/* 14870 */     LambdaSubscriber<T> ls = new LambdaSubscriber(onNext, onError, onComplete, onSubscribe);
/*       */     
/* 14872 */     subscribe((FlowableSubscriber<? super T>)ls);
/*       */     
/* 14874 */     return (Disposable)ls;
/*       */   }
/*       */ 
/*       */   
/*       */   @BackpressureSupport(BackpressureKind.SPECIAL)
/*       */   @SchedulerSupport("none")
/*       */   public final void subscribe(Subscriber<? super T> s) {
/* 14881 */     if (s instanceof FlowableSubscriber) {
/* 14882 */       subscribe((FlowableSubscriber<? super T>)s);
/*       */     } else {
/* 14884 */       ObjectHelper.requireNonNull(s, "s is null");
/* 14885 */       subscribe((FlowableSubscriber<? super T>)new StrictSubscriber(s));
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @BackpressureSupport(BackpressureKind.SPECIAL)
/*       */   @SchedulerSupport("none")
/*       */   public final void subscribe(FlowableSubscriber<? super T> s) {
/* 14929 */     ObjectHelper.requireNonNull(s, "s is null");
/*       */     try {
/* 14931 */       Subscriber<? super T> z = RxJavaPlugins.onSubscribe(this, s);
/*       */       
/* 14933 */       ObjectHelper.requireNonNull(z, "The RxJavaPlugins.onSubscribe hook returned a null FlowableSubscriber. Please check the handler provided to RxJavaPlugins.setOnFlowableSubscribe for invalid null returns. Further reading: https://github.com/ReactiveX/RxJava/wiki/Plugins");
/*       */       
/* 14935 */       subscribeActual(z);
/* 14936 */     } catch (NullPointerException e) {
/* 14937 */       throw e;
/* 14938 */     } catch (Throwable e) {
/* 14939 */       Exceptions.throwIfFatal(e);
/*       */ 
/*       */       
/* 14942 */       RxJavaPlugins.onError(e);
/*       */       
/* 14944 */       NullPointerException npe = new NullPointerException("Actually not, but can't throw other exceptions due to RS");
/* 14945 */       npe.initCause(e);
/* 14946 */       throw npe;
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   protected abstract void subscribeActual(Subscriber<? super T> paramSubscriber);
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.SPECIAL)
/*       */   @SchedulerSupport("none")
/*       */   public final <E extends Subscriber<? super T>> E subscribeWith(E subscriber) {
/* 14991 */     subscribe((Subscriber<? super T>)subscriber);
/* 14992 */     return subscriber;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("custom")
/*       */   @NonNull
/*       */   public final Flowable<T> subscribeOn(@NonNull Scheduler scheduler) {
/* 15025 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 15026 */     return subscribeOn(scheduler, !(this instanceof FlowableCreate));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("custom")
/*       */   @NonNull
/*       */   public final Flowable<T> subscribeOn(@NonNull Scheduler scheduler, boolean requestOn) {
/* 15063 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 15064 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableSubscribeOn(this, scheduler, requestOn));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Flowable<T> switchIfEmpty(Publisher<? extends T> other) {
/* 15094 */     ObjectHelper.requireNonNull(other, "other is null");
/* 15095 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableSwitchIfEmpty(this, other));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final <R> Flowable<R> switchMap(Function<? super T, ? extends Publisher<? extends R>> mapper) {
/* 15129 */     return switchMap(mapper, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final <R> Flowable<R> switchMap(Function<? super T, ? extends Publisher<? extends R>> mapper, int bufferSize) {
/* 15165 */     return switchMap0(mapper, bufferSize, false);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Completable switchMapCompletable(@NonNull Function<? super T, ? extends CompletableSource> mapper) {
/* 15210 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 15211 */     return RxJavaPlugins.onAssembly((Completable)new FlowableSwitchMapCompletable(this, mapper, false));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Completable switchMapCompletableDelayError(@NonNull Function<? super T, ? extends CompletableSource> mapper) {
/* 15257 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 15258 */     return RxJavaPlugins.onAssembly((Completable)new FlowableSwitchMapCompletable(this, mapper, true));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.SPECIAL)
/*       */   @SchedulerSupport("none")
/*       */   public final <R> Flowable<R> switchMapDelayError(Function<? super T, ? extends Publisher<? extends R>> mapper) {
/* 15294 */     return switchMapDelayError(mapper, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.SPECIAL)
/*       */   @SchedulerSupport("none")
/*       */   public final <R> Flowable<R> switchMapDelayError(Function<? super T, ? extends Publisher<? extends R>> mapper, int bufferSize) {
/* 15332 */     return switchMap0(mapper, bufferSize, true);
/*       */   }
/*       */   
/*       */   <R> Flowable<R> switchMap0(Function<? super T, ? extends Publisher<? extends R>> mapper, int bufferSize, boolean delayError) {
/* 15336 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 15337 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 15338 */     if (this instanceof ScalarCallable) {
/*       */       
/* 15340 */       T v = (T)((ScalarCallable)this).call();
/* 15341 */       if (v == null) {
/* 15342 */         return empty();
/*       */       }
/* 15344 */       return FlowableScalarXMap.scalarXMap(v, mapper);
/*       */     } 
/* 15346 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableSwitchMap(this, mapper, bufferSize, delayError));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <R> Flowable<R> switchMapMaybe(@NonNull Function<? super T, ? extends MaybeSource<? extends R>> mapper) {
/* 15387 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 15388 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableSwitchMapMaybe(this, mapper, false));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <R> Flowable<R> switchMapMaybeDelayError(@NonNull Function<? super T, ? extends MaybeSource<? extends R>> mapper) {
/* 15418 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 15419 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableSwitchMapMaybe(this, mapper, true));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <R> Flowable<R> switchMapSingle(@NonNull Function<? super T, ? extends SingleSource<? extends R>> mapper) {
/* 15459 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 15460 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableSwitchMapSingle(this, mapper, false));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <R> Flowable<R> switchMapSingleDelayError(@NonNull Function<? super T, ? extends SingleSource<? extends R>> mapper) {
/* 15490 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 15491 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableSwitchMapSingle(this, mapper, true));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.SPECIAL)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> take(long count) {
/* 15522 */     if (count < 0L) {
/* 15523 */       throw new IllegalArgumentException("count >= 0 required but it was " + count);
/*       */     }
/* 15525 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableTake(this, count));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Flowable<T> take(long time, TimeUnit unit) {
/* 15555 */     return takeUntil(timer(time, unit));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("custom")
/*       */   public final Flowable<T> take(long time, TimeUnit unit, Scheduler scheduler) {
/* 15588 */     return takeUntil(timer(time, unit, scheduler));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> takeLast(int count) {
/* 15616 */     if (count < 0) {
/* 15617 */       throw new IndexOutOfBoundsException("count >= 0 required but it was " + count);
/*       */     }
/* 15619 */     if (count == 0) {
/* 15620 */       return RxJavaPlugins.onAssembly((Flowable)new FlowableIgnoreElements(this));
/*       */     }
/* 15622 */     if (count == 1) {
/* 15623 */       return RxJavaPlugins.onAssembly((Flowable)new FlowableTakeLastOne(this));
/*       */     }
/* 15625 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableTakeLast(this, count));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> takeLast(long count, long time, TimeUnit unit) {
/* 15656 */     return takeLast(count, time, unit, Schedulers.computation(), false, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("custom")
/*       */   public final Flowable<T> takeLast(long count, long time, TimeUnit unit, Scheduler scheduler) {
/* 15692 */     return takeLast(count, time, unit, scheduler, false, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("custom")
/*       */   @NonNull
/*       */   public final Flowable<T> takeLast(long count, long time, TimeUnit unit, Scheduler scheduler, boolean delayError, int bufferSize) {
/* 15734 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 15735 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 15736 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 15737 */     if (count < 0L) {
/* 15738 */       throw new IndexOutOfBoundsException("count >= 0 required but it was " + count);
/*       */     }
/* 15740 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableTakeLastTimed(this, count, time, unit, scheduler, bufferSize, delayError));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Flowable<T> takeLast(long time, TimeUnit unit) {
/* 15770 */     return takeLast(time, unit, Schedulers.computation(), false, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Flowable<T> takeLast(long time, TimeUnit unit, boolean delayError) {
/* 15803 */     return takeLast(time, unit, Schedulers.computation(), delayError, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("custom")
/*       */   public final Flowable<T> takeLast(long time, TimeUnit unit, Scheduler scheduler) {
/* 15837 */     return takeLast(time, unit, scheduler, false, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("custom")
/*       */   public final Flowable<T> takeLast(long time, TimeUnit unit, Scheduler scheduler, boolean delayError) {
/* 15874 */     return takeLast(time, unit, scheduler, delayError, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("custom")
/*       */   public final Flowable<T> takeLast(long time, TimeUnit unit, Scheduler scheduler, boolean delayError, int bufferSize) {
/* 15913 */     return takeLast(Long.MAX_VALUE, time, unit, scheduler, delayError, bufferSize);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Flowable<T> takeUntil(Predicate<? super T> stopPredicate) {
/* 15946 */     ObjectHelper.requireNonNull(stopPredicate, "stopPredicate is null");
/* 15947 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableTakeUntilPredicate(this, stopPredicate));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <U> Flowable<T> takeUntil(Publisher<U> other) {
/* 15976 */     ObjectHelper.requireNonNull(other, "other is null");
/* 15977 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableTakeUntil(this, other));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Flowable<T> takeWhile(Predicate<? super T> predicate) {
/* 16005 */     ObjectHelper.requireNonNull(predicate, "predicate is null");
/* 16006 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableTakeWhile(this, predicate));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Flowable<T> throttleFirst(long windowDuration, TimeUnit unit) {
/* 16036 */     return throttleFirst(windowDuration, unit, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("custom")
/*       */   @NonNull
/*       */   public final Flowable<T> throttleFirst(long skipDuration, TimeUnit unit, Scheduler scheduler) {
/* 16070 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 16071 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 16072 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableThrottleFirstTimed(this, skipDuration, unit, scheduler));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Flowable<T> throttleLast(long intervalDuration, TimeUnit unit) {
/* 16104 */     return sample(intervalDuration, unit);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("custom")
/*       */   public final Flowable<T> throttleLast(long intervalDuration, TimeUnit unit, Scheduler scheduler) {
/* 16139 */     return sample(intervalDuration, unit, scheduler);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Flowable<T> throttleLatest(long timeout, TimeUnit unit) {
/* 16176 */     return throttleLatest(timeout, unit, Schedulers.computation(), false);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Flowable<T> throttleLatest(long timeout, TimeUnit unit, boolean emitLast) {
/* 16213 */     return throttleLatest(timeout, unit, Schedulers.computation(), emitLast);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("custom")
/*       */   public final Flowable<T> throttleLatest(long timeout, TimeUnit unit, Scheduler scheduler) {
/* 16251 */     return throttleLatest(timeout, unit, scheduler, false);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("custom")
/*       */   @NonNull
/*       */   public final Flowable<T> throttleLatest(long timeout, TimeUnit unit, Scheduler scheduler, boolean emitLast) {
/* 16290 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 16291 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 16292 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableThrottleLatest(this, timeout, unit, scheduler, emitLast));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Flowable<T> throttleWithTimeout(long timeout, TimeUnit unit) {
/* 16327 */     return debounce(timeout, unit);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("custom")
/*       */   public final Flowable<T> throttleWithTimeout(long timeout, TimeUnit unit, Scheduler scheduler) {
/* 16365 */     return debounce(timeout, unit, scheduler);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<Timed<T>> timeInterval() {
/* 16389 */     return timeInterval(TimeUnit.MILLISECONDS, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<Timed<T>> timeInterval(Scheduler scheduler) {
/* 16415 */     return timeInterval(TimeUnit.MILLISECONDS, scheduler);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<Timed<T>> timeInterval(TimeUnit unit) {
/* 16440 */     return timeInterval(unit, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<Timed<T>> timeInterval(TimeUnit unit, Scheduler scheduler) {
/* 16467 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 16468 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 16469 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableTimeInterval(this, unit, scheduler));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   public final <V> Flowable<T> timeout(Function<? super T, ? extends Publisher<V>> itemTimeoutIndicator) {
/* 16505 */     return timeout0(null, itemTimeoutIndicator, null);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <V> Flowable<T> timeout(Function<? super T, ? extends Publisher<V>> itemTimeoutIndicator, Flowable<? extends T> other) {
/* 16544 */     ObjectHelper.requireNonNull(other, "other is null");
/* 16545 */     return timeout0(null, itemTimeoutIndicator, other);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Flowable<T> timeout(long timeout, TimeUnit timeUnit) {
/* 16574 */     return timeout0(timeout, timeUnit, null, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   @NonNull
/*       */   public final Flowable<T> timeout(long timeout, TimeUnit timeUnit, Publisher<? extends T> other) {
/* 16607 */     ObjectHelper.requireNonNull(other, "other is null");
/* 16608 */     return timeout0(timeout, timeUnit, other, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("custom")
/*       */   @NonNull
/*       */   public final Flowable<T> timeout(long timeout, TimeUnit timeUnit, Scheduler scheduler, Publisher<? extends T> other) {
/* 16645 */     ObjectHelper.requireNonNull(other, "other is null");
/* 16646 */     return timeout0(timeout, timeUnit, other, scheduler);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("custom")
/*       */   public final Flowable<T> timeout(long timeout, TimeUnit timeUnit, Scheduler scheduler) {
/* 16678 */     return timeout0(timeout, timeUnit, null, scheduler);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <U, V> Flowable<T> timeout(Publisher<U> firstTimeoutIndicator, Function<? super T, ? extends Publisher<V>> itemTimeoutIndicator) {
/* 16718 */     ObjectHelper.requireNonNull(firstTimeoutIndicator, "firstTimeoutIndicator is null");
/* 16719 */     return timeout0(firstTimeoutIndicator, itemTimeoutIndicator, null);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <U, V> Flowable<T> timeout(Publisher<U> firstTimeoutIndicator, Function<? super T, ? extends Publisher<V>> itemTimeoutIndicator, Publisher<? extends T> other) {
/* 16766 */     ObjectHelper.requireNonNull(firstTimeoutIndicator, "firstTimeoutSelector is null");
/* 16767 */     ObjectHelper.requireNonNull(other, "other is null");
/* 16768 */     return timeout0(firstTimeoutIndicator, itemTimeoutIndicator, other);
/*       */   }
/*       */ 
/*       */   
/*       */   private Flowable<T> timeout0(long timeout, TimeUnit timeUnit, Publisher<? extends T> other, Scheduler scheduler) {
/* 16773 */     ObjectHelper.requireNonNull(timeUnit, "timeUnit is null");
/* 16774 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 16775 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableTimeoutTimed(this, timeout, timeUnit, scheduler, other));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   private <U, V> Flowable<T> timeout0(Publisher<U> firstTimeoutIndicator, Function<? super T, ? extends Publisher<V>> itemTimeoutIndicator, Publisher<? extends T> other) {
/* 16782 */     ObjectHelper.requireNonNull(itemTimeoutIndicator, "itemTimeoutIndicator is null");
/* 16783 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableTimeout(this, firstTimeoutIndicator, itemTimeoutIndicator, other));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<Timed<T>> timestamp() {
/* 16807 */     return timestamp(TimeUnit.MILLISECONDS, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<Timed<T>> timestamp(Scheduler scheduler) {
/* 16834 */     return timestamp(TimeUnit.MILLISECONDS, scheduler);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<Timed<T>> timestamp(TimeUnit unit) {
/* 16859 */     return timestamp(unit, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Flowable<Timed<T>> timestamp(TimeUnit unit, Scheduler scheduler) {
/* 16888 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 16889 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 16890 */     return map(Functions.timestampWith(unit, scheduler));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.SPECIAL)
/*       */   @SchedulerSupport("none")
/*       */   public final <R> R to(Function<? super Flowable<T>, R> converter) {
/*       */     try {
/* 16912 */       return (R)((Function)ObjectHelper.requireNonNull(converter, "converter is null")).apply(this);
/* 16913 */     } catch (Throwable ex) {
/* 16914 */       Exceptions.throwIfFatal(ex);
/* 16915 */       throw ExceptionHelper.wrapOrThrow(ex);
/*       */     } 
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final Single<List<T>> toList() {
/* 16950 */     return RxJavaPlugins.onAssembly((Single)new FlowableToListSingle(this));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final Single<List<T>> toList(int capacityHint) {
/* 16986 */     ObjectHelper.verifyPositive(capacityHint, "capacityHint");
/* 16987 */     return RxJavaPlugins.onAssembly((Single)new FlowableToListSingle(this, Functions.createArrayList(capacityHint)));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final <U extends Collection<? super T>> Single<U> toList(Callable<U> collectionSupplier) {
/* 17024 */     ObjectHelper.requireNonNull(collectionSupplier, "collectionSupplier is null");
/* 17025 */     return RxJavaPlugins.onAssembly((Single)new FlowableToListSingle(this, collectionSupplier));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <K> Single<Map<K, T>> toMap(Function<? super T, ? extends K> keySelector) {
/* 17059 */     ObjectHelper.requireNonNull(keySelector, "keySelector is null");
/* 17060 */     return collect(HashMapSupplier.asCallable(), Functions.toMapKeySelector(keySelector));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <K, V> Single<Map<K, V>> toMap(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector) {
/* 17098 */     ObjectHelper.requireNonNull(keySelector, "keySelector is null");
/* 17099 */     ObjectHelper.requireNonNull(valueSelector, "valueSelector is null");
/* 17100 */     return collect(HashMapSupplier.asCallable(), Functions.toMapKeyValueSelector(keySelector, valueSelector));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <K, V> Single<Map<K, V>> toMap(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector, Callable<? extends Map<K, V>> mapSupplier) {
/* 17139 */     ObjectHelper.requireNonNull(keySelector, "keySelector is null");
/* 17140 */     ObjectHelper.requireNonNull(valueSelector, "valueSelector is null");
/* 17141 */     return collect(mapSupplier, Functions.toMapKeyValueSelector(keySelector, valueSelector));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final <K> Single<Map<K, Collection<T>>> toMultimap(Function<? super T, ? extends K> keySelector) {
/* 17171 */     Function<T, T> valueSelector = Functions.identity();
/* 17172 */     Callable<Map<K, Collection<T>>> mapSupplier = HashMapSupplier.asCallable();
/* 17173 */     Function<K, List<T>> collectionFactory = ArrayListSupplier.asFunction();
/* 17174 */     return toMultimap(keySelector, valueSelector, mapSupplier, (Function)collectionFactory);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final <K, V> Single<Map<K, Collection<V>>> toMultimap(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector) {
/* 17209 */     Callable<Map<K, Collection<V>>> mapSupplier = HashMapSupplier.asCallable();
/* 17210 */     Function<K, List<V>> collectionFactory = ArrayListSupplier.asFunction();
/* 17211 */     return toMultimap(keySelector, valueSelector, mapSupplier, (Function)collectionFactory);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <K, V> Single<Map<K, Collection<V>>> toMultimap(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector, Callable<? extends Map<K, Collection<V>>> mapSupplier, Function<? super K, ? extends Collection<? super V>> collectionFactory) {
/* 17255 */     ObjectHelper.requireNonNull(keySelector, "keySelector is null");
/* 17256 */     ObjectHelper.requireNonNull(valueSelector, "valueSelector is null");
/* 17257 */     ObjectHelper.requireNonNull(mapSupplier, "mapSupplier is null");
/* 17258 */     ObjectHelper.requireNonNull(collectionFactory, "collectionFactory is null");
/* 17259 */     return collect(mapSupplier, Functions.toMultimapKeyValueSelector(keySelector, valueSelector, collectionFactory));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final <K, V> Single<Map<K, Collection<V>>> toMultimap(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector, Callable<Map<K, Collection<V>>> mapSupplier) {
/* 17300 */     return toMultimap(keySelector, valueSelector, mapSupplier, ArrayListSupplier.asFunction());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final Observable<T> toObservable() {
/* 17319 */     return RxJavaPlugins.onAssembly((Observable)new ObservableFromPublisher(this));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final Single<List<T>> toSortedList() {
/* 17351 */     return toSortedList(Functions.naturalComparator());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Single<List<T>> toSortedList(Comparator<? super T> comparator) {
/* 17383 */     ObjectHelper.requireNonNull(comparator, "comparator is null");
/* 17384 */     return toList().map(Functions.listSorter(comparator));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final Single<List<T>> toSortedList(Comparator<? super T> comparator, int capacityHint) {
/* 17419 */     ObjectHelper.requireNonNull(comparator, "comparator is null");
/* 17420 */     return toList(capacityHint).map(Functions.listSorter(comparator));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final Single<List<T>> toSortedList(int capacityHint) {
/* 17456 */     return toSortedList(Functions.naturalComparator(), capacityHint);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("custom")
/*       */   @NonNull
/*       */   public final Flowable<T> unsubscribeOn(Scheduler scheduler) {
/* 17481 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 17482 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableUnsubscribeOn(this, scheduler));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<Flowable<T>> window(long count) {
/* 17511 */     return window(count, count, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<Flowable<T>> window(long count, long skip) {
/* 17543 */     return window(count, skip, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<Flowable<T>> window(long count, long skip, int bufferSize) {
/* 17577 */     ObjectHelper.verifyPositive(skip, "skip");
/* 17578 */     ObjectHelper.verifyPositive(count, "count");
/* 17579 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 17580 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableWindow(this, count, skip, bufferSize));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Flowable<Flowable<T>> window(long timespan, long timeskip, TimeUnit unit) {
/* 17615 */     return window(timespan, timeskip, unit, Schedulers.computation(), bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("custom")
/*       */   public final Flowable<Flowable<T>> window(long timespan, long timeskip, TimeUnit unit, Scheduler scheduler) {
/* 17652 */     return window(timespan, timeskip, unit, scheduler, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("custom")
/*       */   @NonNull
/*       */   public final Flowable<Flowable<T>> window(long timespan, long timeskip, TimeUnit unit, Scheduler scheduler, int bufferSize) {
/* 17692 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 17693 */     ObjectHelper.verifyPositive(timespan, "timespan");
/* 17694 */     ObjectHelper.verifyPositive(timeskip, "timeskip");
/* 17695 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 17696 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 17697 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableWindowTimed(this, timespan, timeskip, unit, scheduler, Long.MAX_VALUE, bufferSize, false));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Flowable<Flowable<T>> window(long timespan, TimeUnit unit) {
/* 17730 */     return window(timespan, unit, Schedulers.computation(), Long.MAX_VALUE, false);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Flowable<Flowable<T>> window(long timespan, TimeUnit unit, long count) {
/* 17768 */     return window(timespan, unit, Schedulers.computation(), count, false);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Flowable<Flowable<T>> window(long timespan, TimeUnit unit, long count, boolean restart) {
/* 17808 */     return window(timespan, unit, Schedulers.computation(), count, restart);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("custom")
/*       */   public final Flowable<Flowable<T>> window(long timespan, TimeUnit unit, Scheduler scheduler) {
/* 17845 */     return window(timespan, unit, scheduler, Long.MAX_VALUE, false);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("custom")
/*       */   public final Flowable<Flowable<T>> window(long timespan, TimeUnit unit, Scheduler scheduler, long count) {
/* 17885 */     return window(timespan, unit, scheduler, count, false);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("custom")
/*       */   public final Flowable<Flowable<T>> window(long timespan, TimeUnit unit, Scheduler scheduler, long count, boolean restart) {
/* 17927 */     return window(timespan, unit, scheduler, count, restart, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("custom")
/*       */   @NonNull
/*       */   public final Flowable<Flowable<T>> window(long timespan, TimeUnit unit, Scheduler scheduler, long count, boolean restart, int bufferSize) {
/* 17973 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 17974 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 17975 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 17976 */     ObjectHelper.verifyPositive(count, "count");
/* 17977 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableWindowTimed(this, timespan, timespan, unit, scheduler, count, bufferSize, restart));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("none")
/*       */   public final <B> Flowable<Flowable<T>> window(Publisher<B> boundaryIndicator) {
/* 18007 */     return window(boundaryIndicator, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <B> Flowable<Flowable<T>> window(Publisher<B> boundaryIndicator, int bufferSize) {
/* 18040 */     ObjectHelper.requireNonNull(boundaryIndicator, "boundaryIndicator is null");
/* 18041 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 18042 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableWindowBoundary(this, boundaryIndicator, bufferSize));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("none")
/*       */   public final <U, V> Flowable<Flowable<T>> window(Publisher<U> openingIndicator, Function<? super U, ? extends Publisher<V>> closingIndicator) {
/* 18079 */     return window(openingIndicator, closingIndicator, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <U, V> Flowable<Flowable<T>> window(Publisher<U> openingIndicator, Function<? super U, ? extends Publisher<V>> closingIndicator, int bufferSize) {
/* 18119 */     ObjectHelper.requireNonNull(openingIndicator, "openingIndicator is null");
/* 18120 */     ObjectHelper.requireNonNull(closingIndicator, "closingIndicator is null");
/* 18121 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 18122 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableWindowBoundarySelector(this, openingIndicator, closingIndicator, bufferSize));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("none")
/*       */   public final <B> Flowable<Flowable<T>> window(Callable<? extends Publisher<B>> boundaryIndicatorSupplier) {
/* 18155 */     return window(boundaryIndicatorSupplier, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.ERROR)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <B> Flowable<Flowable<T>> window(Callable<? extends Publisher<B>> boundaryIndicatorSupplier, int bufferSize) {
/* 18191 */     ObjectHelper.requireNonNull(boundaryIndicatorSupplier, "boundaryIndicatorSupplier is null");
/* 18192 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 18193 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableWindowBoundarySupplier(this, boundaryIndicatorSupplier, bufferSize));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <U, R> Flowable<R> withLatestFrom(Publisher<? extends U> other, BiFunction<? super T, ? super U, ? extends R> combiner) {
/* 18230 */     ObjectHelper.requireNonNull(other, "other is null");
/* 18231 */     ObjectHelper.requireNonNull(combiner, "combiner is null");
/*       */     
/* 18233 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableWithLatestFrom(this, combiner, other));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <T1, T2, R> Flowable<R> withLatestFrom(Publisher<T1> source1, Publisher<T2> source2, Function3<? super T, ? super T1, ? super T2, R> combiner) {
/* 18268 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/* 18269 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/* 18270 */     Function<Object[], R> f = Functions.toFunction(combiner);
/* 18271 */     return withLatestFrom((Publisher<?>[])new Publisher[] { source1, source2 }, f);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <T1, T2, T3, R> Flowable<R> withLatestFrom(Publisher<T1> source1, Publisher<T2> source2, Publisher<T3> source3, Function4<? super T, ? super T1, ? super T2, ? super T3, R> combiner) {
/* 18310 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/* 18311 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/* 18312 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/* 18313 */     Function<Object[], R> f = Functions.toFunction(combiner);
/* 18314 */     return withLatestFrom((Publisher<?>[])new Publisher[] { source1, source2, source3 }, f);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <T1, T2, T3, T4, R> Flowable<R> withLatestFrom(Publisher<T1> source1, Publisher<T2> source2, Publisher<T3> source3, Publisher<T4> source4, Function5<? super T, ? super T1, ? super T2, ? super T3, ? super T4, R> combiner) {
/* 18355 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/* 18356 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/* 18357 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/* 18358 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/* 18359 */     Function<Object[], R> f = Functions.toFunction(combiner);
/* 18360 */     return withLatestFrom((Publisher<?>[])new Publisher[] { source1, source2, source3, source4 }, f);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <R> Flowable<R> withLatestFrom(Publisher<?>[] others, Function<? super Object[], R> combiner) {
/* 18391 */     ObjectHelper.requireNonNull(others, "others is null");
/* 18392 */     ObjectHelper.requireNonNull(combiner, "combiner is null");
/* 18393 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableWithLatestFromMany(this, (Publisher[])others, combiner));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.PASS_THROUGH)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <R> Flowable<R> withLatestFrom(Iterable<? extends Publisher<?>> others, Function<? super Object[], R> combiner) {
/* 18424 */     ObjectHelper.requireNonNull(others, "others is null");
/* 18425 */     ObjectHelper.requireNonNull(combiner, "combiner is null");
/* 18426 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableWithLatestFromMany(this, others, combiner));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <U, R> Flowable<R> zipWith(Iterable<U> other, BiFunction<? super T, ? super U, ? extends R> zipper) {
/* 18464 */     ObjectHelper.requireNonNull(other, "other is null");
/* 18465 */     ObjectHelper.requireNonNull(zipper, "zipper is null");
/* 18466 */     return RxJavaPlugins.onAssembly((Flowable)new FlowableZipIterable(this, other, zipper));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public final <U, R> Flowable<R> zipWith(Publisher<? extends U> other, BiFunction<? super T, ? super U, ? extends R> zipper) {
/* 18513 */     ObjectHelper.requireNonNull(other, "other is null");
/* 18514 */     return zip(this, other, zipper);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final <U, R> Flowable<R> zipWith(Publisher<? extends U> other, BiFunction<? super T, ? super U, ? extends R> zipper, boolean delayError) {
/* 18564 */     return zip(this, other, zipper, delayError);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final <U, R> Flowable<R> zipWith(Publisher<? extends U> other, BiFunction<? super T, ? super U, ? extends R> zipper, boolean delayError, int bufferSize) {
/* 18616 */     return zip(this, other, zipper, delayError, bufferSize);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
/*       */   @SchedulerSupport("none")
/*       */   public final TestSubscriber<T> test() {
/* 18638 */     TestSubscriber<T> ts = new TestSubscriber();
/* 18639 */     subscribe((FlowableSubscriber<? super T>)ts);
/* 18640 */     return ts;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final TestSubscriber<T> test(long initialRequest) {
/* 18660 */     TestSubscriber<T> ts = new TestSubscriber(initialRequest);
/* 18661 */     subscribe((FlowableSubscriber<? super T>)ts);
/* 18662 */     return ts;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @BackpressureSupport(BackpressureKind.FULL)
/*       */   @SchedulerSupport("none")
/*       */   public final TestSubscriber<T> test(long initialRequest, boolean cancel) {
/* 18684 */     TestSubscriber<T> ts = new TestSubscriber(initialRequest);
/* 18685 */     if (cancel) {
/* 18686 */       ts.cancel();
/*       */     }
/* 18688 */     subscribe((FlowableSubscriber<? super T>)ts);
/* 18689 */     return ts;
/*       */   }
/*       */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/Flowable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */