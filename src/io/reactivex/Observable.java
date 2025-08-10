/*       */ package io.reactivex;
/*       */ import io.reactivex.annotations.BackpressureKind;
/*       */ import io.reactivex.annotations.BackpressureSupport;
/*       */ import io.reactivex.annotations.CheckReturnValue;
/*       */ import io.reactivex.annotations.NonNull;
/*       */ import io.reactivex.annotations.SchedulerSupport;
/*       */ import io.reactivex.disposables.Disposable;
/*       */ import io.reactivex.exceptions.Exceptions;
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
/*       */ import io.reactivex.functions.Predicate;
/*       */ import io.reactivex.internal.functions.Functions;
/*       */ import io.reactivex.internal.functions.ObjectHelper;
/*       */ import io.reactivex.internal.fuseable.ScalarCallable;
/*       */ import io.reactivex.internal.observers.BlockingFirstObserver;
/*       */ import io.reactivex.internal.observers.BlockingLastObserver;
/*       */ import io.reactivex.internal.observers.ForEachWhileObserver;
/*       */ import io.reactivex.internal.observers.LambdaObserver;
/*       */ import io.reactivex.internal.operators.flowable.FlowableFromObservable;
/*       */ import io.reactivex.internal.operators.mixed.ObservableConcatMapCompletable;
/*       */ import io.reactivex.internal.operators.mixed.ObservableConcatMapMaybe;
/*       */ import io.reactivex.internal.operators.mixed.ObservableConcatMapSingle;
/*       */ import io.reactivex.internal.operators.mixed.ObservableSwitchMapCompletable;
/*       */ import io.reactivex.internal.operators.mixed.ObservableSwitchMapMaybe;
/*       */ import io.reactivex.internal.operators.mixed.ObservableSwitchMapSingle;
/*       */ import io.reactivex.internal.operators.observable.ObservableAmb;
/*       */ import io.reactivex.internal.operators.observable.ObservableAnySingle;
/*       */ import io.reactivex.internal.operators.observable.ObservableBlockingSubscribe;
/*       */ import io.reactivex.internal.operators.observable.ObservableBufferExactBoundary;
/*       */ import io.reactivex.internal.operators.observable.ObservableBufferTimed;
/*       */ import io.reactivex.internal.operators.observable.ObservableCollectSingle;
/*       */ import io.reactivex.internal.operators.observable.ObservableCombineLatest;
/*       */ import io.reactivex.internal.operators.observable.ObservableConcatMap;
/*       */ import io.reactivex.internal.operators.observable.ObservableConcatMapEager;
/*       */ import io.reactivex.internal.operators.observable.ObservableConcatWithCompletable;
/*       */ import io.reactivex.internal.operators.observable.ObservableCountSingle;
/*       */ import io.reactivex.internal.operators.observable.ObservableCreate;
/*       */ import io.reactivex.internal.operators.observable.ObservableDematerialize;
/*       */ import io.reactivex.internal.operators.observable.ObservableDistinctUntilChanged;
/*       */ import io.reactivex.internal.operators.observable.ObservableDoOnEach;
/*       */ import io.reactivex.internal.operators.observable.ObservableElementAtMaybe;
/*       */ import io.reactivex.internal.operators.observable.ObservableElementAtSingle;
/*       */ import io.reactivex.internal.operators.observable.ObservableFilter;
/*       */ import io.reactivex.internal.operators.observable.ObservableFlatMap;
/*       */ import io.reactivex.internal.operators.observable.ObservableFlatMapCompletableCompletable;
/*       */ import io.reactivex.internal.operators.observable.ObservableFlatMapMaybe;
/*       */ import io.reactivex.internal.operators.observable.ObservableFlattenIterable;
/*       */ import io.reactivex.internal.operators.observable.ObservableFromCallable;
/*       */ import io.reactivex.internal.operators.observable.ObservableFromFuture;
/*       */ import io.reactivex.internal.operators.observable.ObservableFromUnsafeSource;
/*       */ import io.reactivex.internal.operators.observable.ObservableGroupJoin;
/*       */ import io.reactivex.internal.operators.observable.ObservableInternalHelper;
/*       */ import io.reactivex.internal.operators.observable.ObservableLastSingle;
/*       */ import io.reactivex.internal.operators.observable.ObservableMapNotification;
/*       */ import io.reactivex.internal.operators.observable.ObservableOnErrorNext;
/*       */ import io.reactivex.internal.operators.observable.ObservableOnErrorReturn;
/*       */ import io.reactivex.internal.operators.observable.ObservableRangeLong;
/*       */ import io.reactivex.internal.operators.observable.ObservableReduceMaybe;
/*       */ import io.reactivex.internal.operators.observable.ObservableReduceSeedSingle;
/*       */ import io.reactivex.internal.operators.observable.ObservableRepeatUntil;
/*       */ import io.reactivex.internal.operators.observable.ObservableReplay;
/*       */ import io.reactivex.internal.operators.observable.ObservableRetryPredicate;
/*       */ import io.reactivex.internal.operators.observable.ObservableSampleTimed;
/*       */ import io.reactivex.internal.operators.observable.ObservableSampleWithObservable;
/*       */ import io.reactivex.internal.operators.observable.ObservableScalarXMap;
/*       */ import io.reactivex.internal.operators.observable.ObservableScanSeed;
/*       */ import io.reactivex.internal.operators.observable.ObservableSequenceEqualSingle;
/*       */ import io.reactivex.internal.operators.observable.ObservableSingleSingle;
/*       */ import io.reactivex.internal.operators.observable.ObservableSkip;
/*       */ import io.reactivex.internal.operators.observable.ObservableSkipUntil;
/*       */ import io.reactivex.internal.operators.observable.ObservableSwitchIfEmpty;
/*       */ import io.reactivex.internal.operators.observable.ObservableSwitchMap;
/*       */ import io.reactivex.internal.operators.observable.ObservableTakeLast;
/*       */ import io.reactivex.internal.operators.observable.ObservableTakeUntil;
/*       */ import io.reactivex.internal.operators.observable.ObservableTimeoutTimed;
/*       */ import io.reactivex.internal.operators.observable.ObservableTimer;
/*       */ import io.reactivex.internal.operators.observable.ObservableToListSingle;
/*       */ import io.reactivex.internal.operators.observable.ObservableWindowBoundary;
/*       */ import io.reactivex.internal.operators.observable.ObservableWindowBoundarySelector;
/*       */ import io.reactivex.internal.operators.observable.ObservableWindowTimed;
/*       */ import io.reactivex.internal.operators.observable.ObservableWithLatestFromMany;
/*       */ import io.reactivex.internal.operators.observable.ObservableZip;
/*       */ import io.reactivex.internal.util.ArrayListSupplier;
/*       */ import io.reactivex.internal.util.ErrorMode;
/*       */ import io.reactivex.internal.util.ExceptionHelper;
/*       */ import io.reactivex.internal.util.HashMapSupplier;
/*       */ import io.reactivex.observables.ConnectableObservable;
/*       */ import io.reactivex.observables.GroupedObservable;
/*       */ import io.reactivex.observers.SafeObserver;
/*       */ import io.reactivex.observers.TestObserver;
/*       */ import io.reactivex.plugins.RxJavaPlugins;
/*       */ import io.reactivex.schedulers.Schedulers;
/*       */ import io.reactivex.schedulers.Timed;
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
/*       */ 
/*       */ public abstract class Observable<T> implements ObservableSource<T> {
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Observable<T> amb(Iterable<? extends ObservableSource<? extends T>> sources) {
/*   122 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*   123 */     return RxJavaPlugins.onAssembly((Observable)new ObservableAmb(null, sources));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T> Observable<T> ambArray(ObservableSource<? extends T>... sources) {
/*   149 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*   150 */     int len = sources.length;
/*   151 */     if (len == 0) {
/*   152 */       return empty();
/*       */     }
/*   154 */     if (len == 1) {
/*   155 */       return wrap((ObservableSource)sources[0]);
/*       */     }
/*   157 */     return RxJavaPlugins.onAssembly((Observable)new ObservableAmb((ObservableSource[])sources, null));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   public static int bufferSize() {
/*   168 */     return Flowable.bufferSize();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T, R> Observable<R> combineLatest(Function<? super Object[], ? extends R> combiner, int bufferSize, ObservableSource<? extends T>... sources) {
/*   211 */     return combineLatest(sources, combiner, bufferSize);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T, R> Observable<R> combineLatest(Iterable<? extends ObservableSource<? extends T>> sources, Function<? super Object[], ? extends R> combiner) {
/*   253 */     return combineLatest(sources, combiner, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T, R> Observable<R> combineLatest(Iterable<? extends ObservableSource<? extends T>> sources, Function<? super Object[], ? extends R> combiner, int bufferSize) {
/*   298 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*   299 */     ObjectHelper.requireNonNull(combiner, "combiner is null");
/*   300 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/*       */ 
/*       */     
/*   303 */     int s = bufferSize << 1;
/*   304 */     return RxJavaPlugins.onAssembly((Observable)new ObservableCombineLatest(null, sources, combiner, s, false));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T, R> Observable<R> combineLatest(ObservableSource<? extends T>[] sources, Function<? super Object[], ? extends R> combiner) {
/*   346 */     return combineLatest(sources, combiner, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T, R> Observable<R> combineLatest(ObservableSource<? extends T>[] sources, Function<? super Object[], ? extends R> combiner, int bufferSize) {
/*   391 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*   392 */     if (sources.length == 0) {
/*   393 */       return empty();
/*       */     }
/*   395 */     ObjectHelper.requireNonNull(combiner, "combiner is null");
/*   396 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/*       */ 
/*       */     
/*   399 */     int s = bufferSize << 1;
/*   400 */     return RxJavaPlugins.onAssembly((Observable)new ObservableCombineLatest((ObservableSource[])sources, null, combiner, s, false));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T1, T2, R> Observable<R> combineLatest(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, BiFunction<? super T1, ? super T2, ? extends R> combiner) {
/*   438 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*   439 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*   440 */     return combineLatest(Functions.toFunction(combiner), bufferSize(), (ObservableSource<?>[])new ObservableSource[] { source1, source2 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T1, T2, T3, R> Observable<R> combineLatest(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, ObservableSource<? extends T3> source3, Function3<? super T1, ? super T2, ? super T3, ? extends R> combiner) {
/*   482 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*   483 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*   484 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*   485 */     return combineLatest(Functions.toFunction(combiner), bufferSize(), (ObservableSource<?>[])new ObservableSource[] { source1, source2, source3 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T1, T2, T3, T4, R> Observable<R> combineLatest(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, ObservableSource<? extends T3> source3, ObservableSource<? extends T4> source4, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> combiner) {
/*   530 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*   531 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*   532 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*   533 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/*   534 */     return combineLatest(Functions.toFunction(combiner), bufferSize(), (ObservableSource<?>[])new ObservableSource[] { source1, source2, source3, source4 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T1, T2, T3, T4, T5, R> Observable<R> combineLatest(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, ObservableSource<? extends T3> source3, ObservableSource<? extends T4> source4, ObservableSource<? extends T5> source5, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> combiner) {
/*   583 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*   584 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*   585 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*   586 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/*   587 */     ObjectHelper.requireNonNull(source5, "source5 is null");
/*   588 */     return combineLatest(Functions.toFunction(combiner), bufferSize(), (ObservableSource<?>[])new ObservableSource[] { source1, source2, source3, source4, source5 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T1, T2, T3, T4, T5, T6, R> Observable<R> combineLatest(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, ObservableSource<? extends T3> source3, ObservableSource<? extends T4> source4, ObservableSource<? extends T5> source5, ObservableSource<? extends T6> source6, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> combiner) {
/*   640 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*   641 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*   642 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*   643 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/*   644 */     ObjectHelper.requireNonNull(source5, "source5 is null");
/*   645 */     ObjectHelper.requireNonNull(source6, "source6 is null");
/*   646 */     return combineLatest(Functions.toFunction(combiner), bufferSize(), (ObservableSource<?>[])new ObservableSource[] { source1, source2, source3, source4, source5, source6 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T1, T2, T3, T4, T5, T6, T7, R> Observable<R> combineLatest(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, ObservableSource<? extends T3> source3, ObservableSource<? extends T4> source4, ObservableSource<? extends T5> source5, ObservableSource<? extends T6> source6, ObservableSource<? extends T7> source7, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> combiner) {
/*   702 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*   703 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*   704 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*   705 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/*   706 */     ObjectHelper.requireNonNull(source5, "source5 is null");
/*   707 */     ObjectHelper.requireNonNull(source6, "source6 is null");
/*   708 */     ObjectHelper.requireNonNull(source7, "source7 is null");
/*   709 */     return combineLatest(Functions.toFunction(combiner), bufferSize(), (ObservableSource<?>[])new ObservableSource[] { source1, source2, source3, source4, source5, source6, source7 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Observable<R> combineLatest(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, ObservableSource<? extends T3> source3, ObservableSource<? extends T4> source4, ObservableSource<? extends T5> source5, ObservableSource<? extends T6> source6, ObservableSource<? extends T7> source7, ObservableSource<? extends T8> source8, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> combiner) {
/*   768 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*   769 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*   770 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*   771 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/*   772 */     ObjectHelper.requireNonNull(source5, "source5 is null");
/*   773 */     ObjectHelper.requireNonNull(source6, "source6 is null");
/*   774 */     ObjectHelper.requireNonNull(source7, "source7 is null");
/*   775 */     ObjectHelper.requireNonNull(source8, "source8 is null");
/*   776 */     return combineLatest(Functions.toFunction(combiner), bufferSize(), (ObservableSource<?>[])new ObservableSource[] { source1, source2, source3, source4, source5, source6, source7, source8 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Observable<R> combineLatest(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, ObservableSource<? extends T3> source3, ObservableSource<? extends T4> source4, ObservableSource<? extends T5> source5, ObservableSource<? extends T6> source6, ObservableSource<? extends T7> source7, ObservableSource<? extends T8> source8, ObservableSource<? extends T9> source9, Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> combiner) {
/*   839 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*   840 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*   841 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*   842 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/*   843 */     ObjectHelper.requireNonNull(source5, "source5 is null");
/*   844 */     ObjectHelper.requireNonNull(source6, "source6 is null");
/*   845 */     ObjectHelper.requireNonNull(source7, "source7 is null");
/*   846 */     ObjectHelper.requireNonNull(source8, "source8 is null");
/*   847 */     ObjectHelper.requireNonNull(source9, "source9 is null");
/*   848 */     return combineLatest(Functions.toFunction(combiner), bufferSize(), (ObservableSource<?>[])new ObservableSource[] { source1, source2, source3, source4, source5, source6, source7, source8, source9 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T, R> Observable<R> combineLatestDelayError(ObservableSource<? extends T>[] sources, Function<? super Object[], ? extends R> combiner) {
/*   890 */     return combineLatestDelayError(sources, combiner, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T, R> Observable<R> combineLatestDelayError(Function<? super Object[], ? extends R> combiner, int bufferSize, ObservableSource<? extends T>... sources) {
/*   935 */     return combineLatestDelayError(sources, combiner, bufferSize);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T, R> Observable<R> combineLatestDelayError(ObservableSource<? extends T>[] sources, Function<? super Object[], ? extends R> combiner, int bufferSize) {
/*   981 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/*   982 */     ObjectHelper.requireNonNull(combiner, "combiner is null");
/*   983 */     if (sources.length == 0) {
/*   984 */       return empty();
/*       */     }
/*       */     
/*   987 */     int s = bufferSize << 1;
/*   988 */     return RxJavaPlugins.onAssembly((Observable)new ObservableCombineLatest((ObservableSource[])sources, null, combiner, s, true));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T, R> Observable<R> combineLatestDelayError(Iterable<? extends ObservableSource<? extends T>> sources, Function<? super Object[], ? extends R> combiner) {
/*  1031 */     return combineLatestDelayError(sources, combiner, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T, R> Observable<R> combineLatestDelayError(Iterable<? extends ObservableSource<? extends T>> sources, Function<? super Object[], ? extends R> combiner, int bufferSize) {
/*  1077 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  1078 */     ObjectHelper.requireNonNull(combiner, "combiner is null");
/*  1079 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/*       */ 
/*       */     
/*  1082 */     int s = bufferSize << 1;
/*  1083 */     return RxJavaPlugins.onAssembly((Observable)new ObservableCombineLatest(null, sources, combiner, s, true));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T> Observable<T> concat(Iterable<? extends ObservableSource<? extends T>> sources) {
/*  1104 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  1105 */     return fromIterable(sources).concatMapDelayError(Functions.identity(), bufferSize(), false);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> concat(ObservableSource<? extends ObservableSource<? extends T>> sources) {
/*  1128 */     return concat(sources, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T> Observable<T> concat(ObservableSource<? extends ObservableSource<? extends T>> sources, int prefetch) {
/*  1155 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  1156 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/*  1157 */     return RxJavaPlugins.onAssembly((Observable)new ObservableConcatMap(sources, Functions.identity(), prefetch, ErrorMode.IMMEDIATE));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T> Observable<T> concat(ObservableSource<? extends T> source1, ObservableSource<? extends T> source2) {
/*  1184 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  1185 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  1186 */     return concatArray((ObservableSource<? extends T>[])new ObservableSource[] { source1, source2 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T> Observable<T> concat(ObservableSource<? extends T> source1, ObservableSource<? extends T> source2, ObservableSource<? extends T> source3) {
/*  1217 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  1218 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  1219 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*  1220 */     return concatArray((ObservableSource<? extends T>[])new ObservableSource[] { source1, source2, source3 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T> Observable<T> concat(ObservableSource<? extends T> source1, ObservableSource<? extends T> source2, ObservableSource<? extends T> source3, ObservableSource<? extends T> source4) {
/*  1253 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  1254 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  1255 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*  1256 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/*  1257 */     return concatArray((ObservableSource<? extends T>[])new ObservableSource[] { source1, source2, source3, source4 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> concatArray(ObservableSource<? extends T>... sources) {
/*  1279 */     if (sources.length == 0) {
/*  1280 */       return empty();
/*       */     }
/*  1282 */     if (sources.length == 1) {
/*  1283 */       return wrap((ObservableSource)sources[0]);
/*       */     }
/*  1285 */     return RxJavaPlugins.onAssembly((Observable)new ObservableConcatMap(fromArray(sources), Functions.identity(), bufferSize(), ErrorMode.BOUNDARY));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> concatArrayDelayError(ObservableSource<? extends T>... sources) {
/*  1306 */     if (sources.length == 0) {
/*  1307 */       return empty();
/*       */     }
/*  1309 */     if (sources.length == 1) {
/*  1310 */       return wrap((ObservableSource)sources[0]);
/*       */     }
/*  1312 */     return concatDelayError(fromArray(sources));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> concatArrayEager(ObservableSource<? extends T>... sources) {
/*  1335 */     return concatArrayEager(bufferSize(), bufferSize(), sources);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> concatArrayEager(int maxConcurrency, int prefetch, ObservableSource<? extends T>... sources) {
/*  1362 */     return fromArray(sources).concatMapEagerDelayError(Functions.identity(), maxConcurrency, prefetch, false);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> concatArrayEagerDelayError(ObservableSource<? extends T>... sources) {
/*  1386 */     return concatArrayEagerDelayError(bufferSize(), bufferSize(), sources);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> concatArrayEagerDelayError(int maxConcurrency, int prefetch, ObservableSource<? extends T>... sources) {
/*  1414 */     return fromArray(sources).concatMapEagerDelayError(Functions.identity(), maxConcurrency, prefetch, true);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T> Observable<T> concatDelayError(Iterable<? extends ObservableSource<? extends T>> sources) {
/*  1435 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  1436 */     return concatDelayError(fromIterable(sources));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> concatDelayError(ObservableSource<? extends ObservableSource<? extends T>> sources) {
/*  1456 */     return concatDelayError(sources, bufferSize(), true);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T> Observable<T> concatDelayError(ObservableSource<? extends ObservableSource<? extends T>> sources, int prefetch, boolean tillTheEnd) {
/*  1481 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  1482 */     ObjectHelper.verifyPositive(prefetch, "prefetch is null");
/*  1483 */     return RxJavaPlugins.onAssembly((Observable)new ObservableConcatMap(sources, Functions.identity(), prefetch, tillTheEnd ? ErrorMode.END : ErrorMode.BOUNDARY));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> concatEager(ObservableSource<? extends ObservableSource<? extends T>> sources) {
/*  1506 */     return concatEager(sources, bufferSize(), bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> concatEager(ObservableSource<? extends ObservableSource<? extends T>> sources, int maxConcurrency, int prefetch) {
/*  1533 */     return wrap(sources).concatMapEager(Functions.identity(), maxConcurrency, prefetch);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> concatEager(Iterable<? extends ObservableSource<? extends T>> sources) {
/*  1556 */     return concatEager(sources, bufferSize(), bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> concatEager(Iterable<? extends ObservableSource<? extends T>> sources, int maxConcurrency, int prefetch) {
/*  1583 */     return fromIterable(sources).concatMapEagerDelayError(Functions.identity(), maxConcurrency, prefetch, false);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T> Observable<T> create(ObservableOnSubscribe<T> source) {
/*  1634 */     ObjectHelper.requireNonNull(source, "source is null");
/*  1635 */     return RxJavaPlugins.onAssembly((Observable)new ObservableCreate(source));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T> Observable<T> defer(Callable<? extends ObservableSource<? extends T>> supplier) {
/*  1666 */     ObjectHelper.requireNonNull(supplier, "supplier is null");
/*  1667 */     return RxJavaPlugins.onAssembly((Observable)new ObservableDefer(supplier));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> empty() {
/*  1690 */     return RxJavaPlugins.onAssembly(ObservableEmpty.INSTANCE);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T> Observable<T> error(Callable<? extends Throwable> errorSupplier) {
/*  1715 */     ObjectHelper.requireNonNull(errorSupplier, "errorSupplier is null");
/*  1716 */     return RxJavaPlugins.onAssembly((Observable)new ObservableError(errorSupplier));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T> Observable<T> error(Throwable exception) {
/*  1741 */     ObjectHelper.requireNonNull(exception, "exception is null");
/*  1742 */     return error(Functions.justCallable(exception));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T> Observable<T> fromArray(T... items) {
/*  1765 */     ObjectHelper.requireNonNull(items, "items is null");
/*  1766 */     if (items.length == 0) {
/*  1767 */       return empty();
/*       */     }
/*  1769 */     if (items.length == 1) {
/*  1770 */       return just(items[0]);
/*       */     }
/*  1772 */     return RxJavaPlugins.onAssembly((Observable)new ObservableFromArray((Object[])items));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T> Observable<T> fromCallable(Callable<? extends T> supplier) {
/*  1807 */     ObjectHelper.requireNonNull(supplier, "supplier is null");
/*  1808 */     return RxJavaPlugins.onAssembly((Observable)new ObservableFromCallable(supplier));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T> Observable<T> fromFuture(Future<? extends T> future) {
/*  1841 */     ObjectHelper.requireNonNull(future, "future is null");
/*  1842 */     return RxJavaPlugins.onAssembly((Observable)new ObservableFromFuture(future, 0L, null));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T> Observable<T> fromFuture(Future<? extends T> future, long timeout, TimeUnit unit) {
/*  1879 */     ObjectHelper.requireNonNull(future, "future is null");
/*  1880 */     ObjectHelper.requireNonNull(unit, "unit is null");
/*  1881 */     return RxJavaPlugins.onAssembly((Observable)new ObservableFromFuture(future, timeout, unit));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   @NonNull
/*       */   public static <T> Observable<T> fromFuture(Future<? extends T> future, long timeout, TimeUnit unit, Scheduler scheduler) {
/*  1921 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/*  1922 */     Observable<T> o = fromFuture(future, timeout, unit);
/*  1923 */     return o.subscribeOn(scheduler);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   @NonNull
/*       */   public static <T> Observable<T> fromFuture(Future<? extends T> future, Scheduler scheduler) {
/*  1957 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/*  1958 */     Observable<T> o = fromFuture(future);
/*  1959 */     return o.subscribeOn(scheduler);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T> Observable<T> fromIterable(Iterable<? extends T> source) {
/*  1983 */     ObjectHelper.requireNonNull(source, "source is null");
/*  1984 */     return RxJavaPlugins.onAssembly((Observable)new ObservableFromIterable(source));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("none")
/*       */   @NonNull
/*       */   public static <T> Observable<T> fromPublisher(Publisher<? extends T> publisher) {
/*  2020 */     ObjectHelper.requireNonNull(publisher, "publisher is null");
/*  2021 */     return RxJavaPlugins.onAssembly((Observable)new ObservableFromPublisher(publisher));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T> Observable<T> generate(Consumer<Emitter<T>> generator) {
/*  2049 */     ObjectHelper.requireNonNull(generator, "generator is null");
/*  2050 */     return generate(Functions.nullSupplier(), 
/*  2051 */         ObservableInternalHelper.simpleGenerator(generator), Functions.emptyConsumer());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T, S> Observable<T> generate(Callable<S> initialState, BiConsumer<S, Emitter<T>> generator) {
/*  2081 */     ObjectHelper.requireNonNull(generator, "generator is null");
/*  2082 */     return generate(initialState, ObservableInternalHelper.simpleBiGenerator(generator), Functions.emptyConsumer());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T, S> Observable<T> generate(Callable<S> initialState, BiConsumer<S, Emitter<T>> generator, Consumer<? super S> disposeState) {
/*  2117 */     ObjectHelper.requireNonNull(generator, "generator is null");
/*  2118 */     return generate(initialState, ObservableInternalHelper.simpleBiGenerator(generator), disposeState);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T, S> Observable<T> generate(Callable<S> initialState, BiFunction<S, Emitter<T>, S> generator) {
/*  2148 */     return generate(initialState, generator, Functions.emptyConsumer());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T, S> Observable<T> generate(Callable<S> initialState, BiFunction<S, Emitter<T>, S> generator, Consumer<? super S> disposeState) {
/*  2182 */     ObjectHelper.requireNonNull(initialState, "initialState is null");
/*  2183 */     ObjectHelper.requireNonNull(generator, "generator is null");
/*  2184 */     ObjectHelper.requireNonNull(disposeState, "disposeState is null");
/*  2185 */     return RxJavaPlugins.onAssembly((Observable)new ObservableGenerate(initialState, generator, disposeState));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public static Observable<Long> interval(long initialDelay, long period, TimeUnit unit) {
/*  2212 */     return interval(initialDelay, period, unit, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   @NonNull
/*       */   public static Observable<Long> interval(long initialDelay, long period, TimeUnit unit, Scheduler scheduler) {
/*  2242 */     ObjectHelper.requireNonNull(unit, "unit is null");
/*  2243 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/*       */     
/*  2245 */     return RxJavaPlugins.onAssembly((Observable)new ObservableInterval(Math.max(0L, initialDelay), Math.max(0L, period), unit, scheduler));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public static Observable<Long> interval(long period, TimeUnit unit) {
/*  2267 */     return interval(period, period, unit, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public static Observable<Long> interval(long period, TimeUnit unit, Scheduler scheduler) {
/*  2292 */     return interval(period, period, unit, scheduler);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public static Observable<Long> intervalRange(long start, long count, long initialDelay, long period, TimeUnit unit) {
/*  2315 */     return intervalRange(start, count, initialDelay, period, unit, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   @NonNull
/*       */   public static Observable<Long> intervalRange(long start, long count, long initialDelay, long period, TimeUnit unit, Scheduler scheduler) {
/*  2339 */     if (count < 0L) {
/*  2340 */       throw new IllegalArgumentException("count >= 0 required but it was " + count);
/*       */     }
/*       */     
/*  2343 */     if (count == 0L) {
/*  2344 */       return empty().delay(initialDelay, unit, scheduler);
/*       */     }
/*       */     
/*  2347 */     long end = start + count - 1L;
/*  2348 */     if (start > 0L && end < 0L) {
/*  2349 */       throw new IllegalArgumentException("Overflow! start + count is bigger than Long.MAX_VALUE");
/*       */     }
/*  2351 */     ObjectHelper.requireNonNull(unit, "unit is null");
/*  2352 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/*       */     
/*  2354 */     return RxJavaPlugins.onAssembly((Observable)new ObservableIntervalRange(start, end, Math.max(0L, initialDelay), Math.max(0L, period), unit, scheduler));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T> Observable<T> just(T item) {
/*  2389 */     ObjectHelper.requireNonNull(item, "item is null");
/*  2390 */     return RxJavaPlugins.onAssembly((Observable)new ObservableJust(item));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T> Observable<T> just(T item1, T item2) {
/*  2416 */     ObjectHelper.requireNonNull(item1, "item1 is null");
/*  2417 */     ObjectHelper.requireNonNull(item2, "item2 is null");
/*       */     
/*  2419 */     return fromArray((T[])new Object[] { item1, item2 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T> Observable<T> just(T item1, T item2, T item3) {
/*  2447 */     ObjectHelper.requireNonNull(item1, "item1 is null");
/*  2448 */     ObjectHelper.requireNonNull(item2, "item2 is null");
/*  2449 */     ObjectHelper.requireNonNull(item3, "item3 is null");
/*       */     
/*  2451 */     return fromArray((T[])new Object[] { item1, item2, item3 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T> Observable<T> just(T item1, T item2, T item3, T item4) {
/*  2481 */     ObjectHelper.requireNonNull(item1, "item1 is null");
/*  2482 */     ObjectHelper.requireNonNull(item2, "item2 is null");
/*  2483 */     ObjectHelper.requireNonNull(item3, "item3 is null");
/*  2484 */     ObjectHelper.requireNonNull(item4, "item4 is null");
/*       */     
/*  2486 */     return fromArray((T[])new Object[] { item1, item2, item3, item4 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T> Observable<T> just(T item1, T item2, T item3, T item4, T item5) {
/*  2518 */     ObjectHelper.requireNonNull(item1, "item1 is null");
/*  2519 */     ObjectHelper.requireNonNull(item2, "item2 is null");
/*  2520 */     ObjectHelper.requireNonNull(item3, "item3 is null");
/*  2521 */     ObjectHelper.requireNonNull(item4, "item4 is null");
/*  2522 */     ObjectHelper.requireNonNull(item5, "item5 is null");
/*       */     
/*  2524 */     return fromArray((T[])new Object[] { item1, item2, item3, item4, item5 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T> Observable<T> just(T item1, T item2, T item3, T item4, T item5, T item6) {
/*  2558 */     ObjectHelper.requireNonNull(item1, "item1 is null");
/*  2559 */     ObjectHelper.requireNonNull(item2, "item2 is null");
/*  2560 */     ObjectHelper.requireNonNull(item3, "item3 is null");
/*  2561 */     ObjectHelper.requireNonNull(item4, "item4 is null");
/*  2562 */     ObjectHelper.requireNonNull(item5, "item5 is null");
/*  2563 */     ObjectHelper.requireNonNull(item6, "item6 is null");
/*       */     
/*  2565 */     return fromArray((T[])new Object[] { item1, item2, item3, item4, item5, item6 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T> Observable<T> just(T item1, T item2, T item3, T item4, T item5, T item6, T item7) {
/*  2601 */     ObjectHelper.requireNonNull(item1, "item1 is null");
/*  2602 */     ObjectHelper.requireNonNull(item2, "item2 is null");
/*  2603 */     ObjectHelper.requireNonNull(item3, "item3 is null");
/*  2604 */     ObjectHelper.requireNonNull(item4, "item4 is null");
/*  2605 */     ObjectHelper.requireNonNull(item5, "item5 is null");
/*  2606 */     ObjectHelper.requireNonNull(item6, "item6 is null");
/*  2607 */     ObjectHelper.requireNonNull(item7, "item7 is null");
/*       */     
/*  2609 */     return fromArray((T[])new Object[] { item1, item2, item3, item4, item5, item6, item7 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T> Observable<T> just(T item1, T item2, T item3, T item4, T item5, T item6, T item7, T item8) {
/*  2647 */     ObjectHelper.requireNonNull(item1, "item1 is null");
/*  2648 */     ObjectHelper.requireNonNull(item2, "item2 is null");
/*  2649 */     ObjectHelper.requireNonNull(item3, "item3 is null");
/*  2650 */     ObjectHelper.requireNonNull(item4, "item4 is null");
/*  2651 */     ObjectHelper.requireNonNull(item5, "item5 is null");
/*  2652 */     ObjectHelper.requireNonNull(item6, "item6 is null");
/*  2653 */     ObjectHelper.requireNonNull(item7, "item7 is null");
/*  2654 */     ObjectHelper.requireNonNull(item8, "item8 is null");
/*       */     
/*  2656 */     return fromArray((T[])new Object[] { item1, item2, item3, item4, item5, item6, item7, item8 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T> Observable<T> just(T item1, T item2, T item3, T item4, T item5, T item6, T item7, T item8, T item9) {
/*  2696 */     ObjectHelper.requireNonNull(item1, "item1 is null");
/*  2697 */     ObjectHelper.requireNonNull(item2, "item2 is null");
/*  2698 */     ObjectHelper.requireNonNull(item3, "item3 is null");
/*  2699 */     ObjectHelper.requireNonNull(item4, "item4 is null");
/*  2700 */     ObjectHelper.requireNonNull(item5, "item5 is null");
/*  2701 */     ObjectHelper.requireNonNull(item6, "item6 is null");
/*  2702 */     ObjectHelper.requireNonNull(item7, "item7 is null");
/*  2703 */     ObjectHelper.requireNonNull(item8, "item8 is null");
/*  2704 */     ObjectHelper.requireNonNull(item9, "item9 is null");
/*       */     
/*  2706 */     return fromArray((T[])new Object[] { item1, item2, item3, item4, item5, item6, item7, item8, item9 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public static <T> Observable<T> just(T item1, T item2, T item3, T item4, T item5, T item6, T item7, T item8, T item9, T item10) {
/*  2748 */     ObjectHelper.requireNonNull(item1, "item1 is null");
/*  2749 */     ObjectHelper.requireNonNull(item2, "item2 is null");
/*  2750 */     ObjectHelper.requireNonNull(item3, "item3 is null");
/*  2751 */     ObjectHelper.requireNonNull(item4, "item4 is null");
/*  2752 */     ObjectHelper.requireNonNull(item5, "item5 is null");
/*  2753 */     ObjectHelper.requireNonNull(item6, "item6 is null");
/*  2754 */     ObjectHelper.requireNonNull(item7, "item7 is null");
/*  2755 */     ObjectHelper.requireNonNull(item8, "item8 is null");
/*  2756 */     ObjectHelper.requireNonNull(item9, "item9 is null");
/*  2757 */     ObjectHelper.requireNonNull(item10, "item10 is null");
/*       */     
/*  2759 */     return fromArray((T[])new Object[] { item1, item2, item3, item4, item5, item6, item7, item8, item9, item10 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> merge(Iterable<? extends ObservableSource<? extends T>> sources, int maxConcurrency, int bufferSize) {
/*  2806 */     return fromIterable(sources).flatMap(Functions.identity(), false, maxConcurrency, bufferSize);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> mergeArray(int maxConcurrency, int bufferSize, ObservableSource<? extends T>... sources) {
/*  2853 */     return fromArray(sources).flatMap(Functions.identity(), false, maxConcurrency, bufferSize);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> merge(Iterable<? extends ObservableSource<? extends T>> sources) {
/*  2893 */     return fromIterable(sources).flatMap(Functions.identity());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> merge(Iterable<? extends ObservableSource<? extends T>> sources, int maxConcurrency) {
/*  2938 */     return fromIterable(sources).flatMap(Functions.identity(), maxConcurrency);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> merge(ObservableSource<? extends ObservableSource<? extends T>> sources) {
/*  2979 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  2980 */     return RxJavaPlugins.onAssembly((Observable)new ObservableFlatMap(sources, Functions.identity(), false, 2147483647, bufferSize()));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> merge(ObservableSource<? extends ObservableSource<? extends T>> sources, int maxConcurrency) {
/*  3027 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  3028 */     ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
/*  3029 */     return RxJavaPlugins.onAssembly((Observable)new ObservableFlatMap(sources, Functions.identity(), false, maxConcurrency, bufferSize()));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> merge(ObservableSource<? extends T> source1, ObservableSource<? extends T> source2) {
/*  3070 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  3071 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  3072 */     return fromArray(new ObservableSource[] { source1, source2 }).flatMap(Functions.identity(), false, 2);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> merge(ObservableSource<? extends T> source1, ObservableSource<? extends T> source2, ObservableSource<? extends T> source3) {
/*  3115 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  3116 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  3117 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*  3118 */     return fromArray(new ObservableSource[] { source1, source2, source3 }).flatMap(Functions.identity(), false, 3);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> merge(ObservableSource<? extends T> source1, ObservableSource<? extends T> source2, ObservableSource<? extends T> source3, ObservableSource<? extends T> source4) {
/*  3165 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  3166 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  3167 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*  3168 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/*  3169 */     return fromArray(new ObservableSource[] { source1, source2, source3, source4 }).flatMap(Functions.identity(), false, 4);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> mergeArray(ObservableSource<? extends T>... sources) {
/*  3208 */     return fromArray(sources).flatMap(Functions.identity(), sources.length);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> mergeDelayError(Iterable<? extends ObservableSource<? extends T>> sources) {
/*  3240 */     return fromIterable(sources).flatMap(Functions.identity(), true);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> mergeDelayError(Iterable<? extends ObservableSource<? extends T>> sources, int maxConcurrency, int bufferSize) {
/*  3276 */     return fromIterable(sources).flatMap(Functions.identity(), true, maxConcurrency, bufferSize);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> mergeArrayDelayError(int maxConcurrency, int bufferSize, ObservableSource<? extends T>... sources) {
/*  3312 */     return fromArray(sources).flatMap(Functions.identity(), true, maxConcurrency, bufferSize);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> mergeDelayError(Iterable<? extends ObservableSource<? extends T>> sources, int maxConcurrency) {
/*  3346 */     return fromIterable(sources).flatMap(Functions.identity(), true, maxConcurrency);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> mergeDelayError(ObservableSource<? extends ObservableSource<? extends T>> sources) {
/*  3378 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  3379 */     return RxJavaPlugins.onAssembly((Observable)new ObservableFlatMap(sources, Functions.identity(), true, 2147483647, bufferSize()));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> mergeDelayError(ObservableSource<? extends ObservableSource<? extends T>> sources, int maxConcurrency) {
/*  3415 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  3416 */     ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
/*  3417 */     return RxJavaPlugins.onAssembly((Observable)new ObservableFlatMap(sources, Functions.identity(), true, maxConcurrency, bufferSize()));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> mergeDelayError(ObservableSource<? extends T> source1, ObservableSource<? extends T> source2) {
/*  3450 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  3451 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  3452 */     return fromArray(new ObservableSource[] { source1, source2 }).flatMap(Functions.identity(), true, 2);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> mergeDelayError(ObservableSource<? extends T> source1, ObservableSource<? extends T> source2, ObservableSource<? extends T> source3) {
/*  3488 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  3489 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  3490 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*  3491 */     return fromArray(new ObservableSource[] { source1, source2, source3 }).flatMap(Functions.identity(), true, 3);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> mergeDelayError(ObservableSource<? extends T> source1, ObservableSource<? extends T> source2, ObservableSource<? extends T> source3, ObservableSource<? extends T> source4) {
/*  3531 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  3532 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  3533 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*  3534 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/*  3535 */     return fromArray(new ObservableSource[] { source1, source2, source3, source4 }).flatMap(Functions.identity(), true, 4);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> mergeArrayDelayError(ObservableSource<? extends T>... sources) {
/*  3567 */     return fromArray(sources).flatMap(Functions.identity(), true, sources.length);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> never() {
/*  3590 */     return RxJavaPlugins.onAssembly(ObservableNever.INSTANCE);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static Observable<Integer> range(int start, int count) {
/*  3615 */     if (count < 0) {
/*  3616 */       throw new IllegalArgumentException("count >= 0 required but it was " + count);
/*       */     }
/*  3618 */     if (count == 0) {
/*  3619 */       return empty();
/*       */     }
/*  3621 */     if (count == 1) {
/*  3622 */       return just(Integer.valueOf(start));
/*       */     }
/*  3624 */     if (start + (count - 1) > 2147483647L) {
/*  3625 */       throw new IllegalArgumentException("Integer overflow");
/*       */     }
/*  3627 */     return RxJavaPlugins.onAssembly((Observable)new ObservableRange(start, count));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static Observable<Long> rangeLong(long start, long count) {
/*  3652 */     if (count < 0L) {
/*  3653 */       throw new IllegalArgumentException("count >= 0 required but it was " + count);
/*       */     }
/*       */     
/*  3656 */     if (count == 0L) {
/*  3657 */       return empty();
/*       */     }
/*       */     
/*  3660 */     if (count == 1L) {
/*  3661 */       return just(Long.valueOf(start));
/*       */     }
/*       */     
/*  3664 */     long end = start + count - 1L;
/*  3665 */     if (start > 0L && end < 0L) {
/*  3666 */       throw new IllegalArgumentException("Overflow! start + count is bigger than Long.MAX_VALUE");
/*       */     }
/*       */     
/*  3669 */     return RxJavaPlugins.onAssembly((Observable)new ObservableRangeLong(start, count));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Single<Boolean> sequenceEqual(ObservableSource<? extends T> source1, ObservableSource<? extends T> source2) {
/*  3694 */     return sequenceEqual(source1, source2, ObjectHelper.equalsPredicate(), bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Single<Boolean> sequenceEqual(ObservableSource<? extends T> source1, ObservableSource<? extends T> source2, BiPredicate<? super T, ? super T> isEqual) {
/*  3724 */     return sequenceEqual(source1, source2, isEqual, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Single<Boolean> sequenceEqual(ObservableSource<? extends T> source1, ObservableSource<? extends T> source2, BiPredicate<? super T, ? super T> isEqual, int bufferSize) {
/*  3756 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  3757 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  3758 */     ObjectHelper.requireNonNull(isEqual, "isEqual is null");
/*  3759 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/*  3760 */     return RxJavaPlugins.onAssembly((Single)new ObservableSequenceEqualSingle(source1, source2, isEqual, bufferSize));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Single<Boolean> sequenceEqual(ObservableSource<? extends T> source1, ObservableSource<? extends T> source2, int bufferSize) {
/*  3788 */     return sequenceEqual(source1, source2, ObjectHelper.equalsPredicate(), bufferSize);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> switchOnNext(ObservableSource<? extends ObservableSource<? extends T>> sources, int bufferSize) {
/*  3822 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  3823 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/*  3824 */     return RxJavaPlugins.onAssembly((Observable)new ObservableSwitchMap(sources, Functions.identity(), bufferSize, false));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> switchOnNext(ObservableSource<? extends ObservableSource<? extends T>> sources) {
/*  3855 */     return switchOnNext(sources, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> switchOnNextDelayError(ObservableSource<? extends ObservableSource<? extends T>> sources) {
/*  3888 */     return switchOnNextDelayError(sources, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> switchOnNextDelayError(ObservableSource<? extends ObservableSource<? extends T>> sources, int prefetch) {
/*  3924 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  3925 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/*  3926 */     return RxJavaPlugins.onAssembly((Observable)new ObservableSwitchMap(sources, Functions.identity(), prefetch, true));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public static Observable<Long> timer(long delay, TimeUnit unit) {
/*  3948 */     return timer(delay, unit, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public static Observable<Long> timer(long delay, TimeUnit unit, Scheduler scheduler) {
/*  3977 */     ObjectHelper.requireNonNull(unit, "unit is null");
/*  3978 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/*       */     
/*  3980 */     return RxJavaPlugins.onAssembly((Observable)new ObservableTimer(Math.max(delay, 0L), unit, scheduler));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> unsafeCreate(ObservableSource<T> onSubscribe) {
/*  3998 */     ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");
/*  3999 */     if (onSubscribe instanceof Observable) {
/*  4000 */       throw new IllegalArgumentException("unsafeCreate(Observable) should be upgraded");
/*       */     }
/*  4002 */     return RxJavaPlugins.onAssembly((Observable)new ObservableFromUnsafeSource(onSubscribe));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T, D> Observable<T> using(Callable<? extends D> resourceSupplier, Function<? super D, ? extends ObservableSource<? extends T>> sourceSupplier, Consumer<? super D> disposer) {
/*  4029 */     return using(resourceSupplier, sourceSupplier, disposer, true);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T, D> Observable<T> using(Callable<? extends D> resourceSupplier, Function<? super D, ? extends ObservableSource<? extends T>> sourceSupplier, Consumer<? super D> disposer, boolean eager) {
/*  4063 */     ObjectHelper.requireNonNull(resourceSupplier, "resourceSupplier is null");
/*  4064 */     ObjectHelper.requireNonNull(sourceSupplier, "sourceSupplier is null");
/*  4065 */     ObjectHelper.requireNonNull(disposer, "disposer is null");
/*  4066 */     return RxJavaPlugins.onAssembly((Observable)new ObservableUsing(resourceSupplier, sourceSupplier, disposer, eager));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T> Observable<T> wrap(ObservableSource<T> source) {
/*  4085 */     ObjectHelper.requireNonNull(source, "source is null");
/*  4086 */     if (source instanceof Observable) {
/*  4087 */       return RxJavaPlugins.onAssembly((Observable)source);
/*       */     }
/*  4089 */     return RxJavaPlugins.onAssembly((Observable)new ObservableFromUnsafeSource(source));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T, R> Observable<R> zip(Iterable<? extends ObservableSource<? extends T>> sources, Function<? super Object[], ? extends R> zipper) {
/*  4140 */     ObjectHelper.requireNonNull(zipper, "zipper is null");
/*  4141 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  4142 */     return RxJavaPlugins.onAssembly((Observable)new ObservableZip(null, sources, zipper, bufferSize(), false));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T, R> Observable<R> zip(ObservableSource<? extends ObservableSource<? extends T>> sources, Function<? super Object[], ? extends R> zipper) {
/*  4194 */     ObjectHelper.requireNonNull(zipper, "zipper is null");
/*  4195 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  4196 */     return RxJavaPlugins.onAssembly((new ObservableToList(sources, 16))
/*  4197 */         .flatMap(ObservableInternalHelper.zipIterable(zipper)));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T1, T2, R> Observable<R> zip(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, BiFunction<? super T1, ? super T2, ? extends R> zipper) {
/*  4250 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  4251 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  4252 */     return zipArray(Functions.toFunction(zipper), false, bufferSize(), (ObservableSource<?>[])new ObservableSource[] { source1, source2 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T1, T2, R> Observable<R> zip(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, BiFunction<? super T1, ? super T2, ? extends R> zipper, boolean delayError) {
/*  4306 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  4307 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  4308 */     return zipArray(Functions.toFunction(zipper), delayError, bufferSize(), (ObservableSource<?>[])new ObservableSource[] { source1, source2 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T1, T2, R> Observable<R> zip(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, BiFunction<? super T1, ? super T2, ? extends R> zipper, boolean delayError, int bufferSize) {
/*  4363 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  4364 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  4365 */     return zipArray(Functions.toFunction(zipper), delayError, bufferSize, (ObservableSource<?>[])new ObservableSource[] { source1, source2 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T1, T2, T3, R> Observable<R> zip(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, ObservableSource<? extends T3> source3, Function3<? super T1, ? super T2, ? super T3, ? extends R> zipper) {
/*  4422 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  4423 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  4424 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*  4425 */     return zipArray(Functions.toFunction(zipper), false, bufferSize(), (ObservableSource<?>[])new ObservableSource[] { source1, source2, source3 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T1, T2, T3, T4, R> Observable<R> zip(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, ObservableSource<? extends T3> source3, ObservableSource<? extends T4> source4, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> zipper) {
/*  4486 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  4487 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  4488 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*  4489 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/*  4490 */     return zipArray(Functions.toFunction(zipper), false, bufferSize(), (ObservableSource<?>[])new ObservableSource[] { source1, source2, source3, source4 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T1, T2, T3, T4, T5, R> Observable<R> zip(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, ObservableSource<? extends T3> source3, ObservableSource<? extends T4> source4, ObservableSource<? extends T5> source5, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> zipper) {
/*  4554 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  4555 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  4556 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*  4557 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/*  4558 */     ObjectHelper.requireNonNull(source5, "source5 is null");
/*  4559 */     return zipArray(Functions.toFunction(zipper), false, bufferSize(), (ObservableSource<?>[])new ObservableSource[] { source1, source2, source3, source4, source5 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T1, T2, T3, T4, T5, T6, R> Observable<R> zip(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, ObservableSource<? extends T3> source3, ObservableSource<? extends T4> source4, ObservableSource<? extends T5> source5, ObservableSource<? extends T6> source6, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> zipper) {
/*  4625 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  4626 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  4627 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*  4628 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/*  4629 */     ObjectHelper.requireNonNull(source5, "source5 is null");
/*  4630 */     ObjectHelper.requireNonNull(source6, "source6 is null");
/*  4631 */     return zipArray(Functions.toFunction(zipper), false, bufferSize(), (ObservableSource<?>[])new ObservableSource[] { source1, source2, source3, source4, source5, source6 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T1, T2, T3, T4, T5, T6, T7, R> Observable<R> zip(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, ObservableSource<? extends T3> source3, ObservableSource<? extends T4> source4, ObservableSource<? extends T5> source5, ObservableSource<? extends T6> source6, ObservableSource<? extends T7> source7, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> zipper) {
/*  4701 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  4702 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  4703 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*  4704 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/*  4705 */     ObjectHelper.requireNonNull(source5, "source5 is null");
/*  4706 */     ObjectHelper.requireNonNull(source6, "source6 is null");
/*  4707 */     ObjectHelper.requireNonNull(source7, "source7 is null");
/*  4708 */     return zipArray(Functions.toFunction(zipper), false, bufferSize(), (ObservableSource<?>[])new ObservableSource[] { source1, source2, source3, source4, source5, source6, source7 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Observable<R> zip(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, ObservableSource<? extends T3> source3, ObservableSource<? extends T4> source4, ObservableSource<? extends T5> source5, ObservableSource<? extends T6> source6, ObservableSource<? extends T7> source7, ObservableSource<? extends T8> source8, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> zipper) {
/*  4781 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  4782 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  4783 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*  4784 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/*  4785 */     ObjectHelper.requireNonNull(source5, "source5 is null");
/*  4786 */     ObjectHelper.requireNonNull(source6, "source6 is null");
/*  4787 */     ObjectHelper.requireNonNull(source7, "source7 is null");
/*  4788 */     ObjectHelper.requireNonNull(source8, "source8 is null");
/*  4789 */     return zipArray(Functions.toFunction(zipper), false, bufferSize(), (ObservableSource<?>[])new ObservableSource[] { source1, source2, source3, source4, source5, source6, source7, source8 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Observable<R> zip(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2, ObservableSource<? extends T3> source3, ObservableSource<? extends T4> source4, ObservableSource<? extends T5> source5, ObservableSource<? extends T6> source6, ObservableSource<? extends T7> source7, ObservableSource<? extends T8> source8, ObservableSource<? extends T9> source9, Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> zipper) {
/*  4865 */     ObjectHelper.requireNonNull(source1, "source1 is null");
/*  4866 */     ObjectHelper.requireNonNull(source2, "source2 is null");
/*  4867 */     ObjectHelper.requireNonNull(source3, "source3 is null");
/*  4868 */     ObjectHelper.requireNonNull(source4, "source4 is null");
/*  4869 */     ObjectHelper.requireNonNull(source5, "source5 is null");
/*  4870 */     ObjectHelper.requireNonNull(source6, "source6 is null");
/*  4871 */     ObjectHelper.requireNonNull(source7, "source7 is null");
/*  4872 */     ObjectHelper.requireNonNull(source8, "source8 is null");
/*  4873 */     ObjectHelper.requireNonNull(source9, "source9 is null");
/*  4874 */     return zipArray(Functions.toFunction(zipper), false, bufferSize(), (ObservableSource<?>[])new ObservableSource[] { source1, source2, source3, source4, source5, source6, source7, source8, source9 });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T, R> Observable<R> zipArray(Function<? super Object[], ? extends R> zipper, boolean delayError, int bufferSize, ObservableSource<? extends T>... sources) {
/*  4931 */     if (sources.length == 0) {
/*  4932 */       return empty();
/*       */     }
/*  4934 */     ObjectHelper.requireNonNull(zipper, "zipper is null");
/*  4935 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/*  4936 */     return RxJavaPlugins.onAssembly((Observable)new ObservableZip((ObservableSource[])sources, null, zipper, bufferSize, delayError));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public static <T, R> Observable<R> zipIterable(Iterable<? extends ObservableSource<? extends T>> sources, Function<? super Object[], ? extends R> zipper, boolean delayError, int bufferSize) {
/*  4994 */     ObjectHelper.requireNonNull(zipper, "zipper is null");
/*  4995 */     ObjectHelper.requireNonNull(sources, "sources is null");
/*  4996 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/*  4997 */     return RxJavaPlugins.onAssembly((Observable)new ObservableZip(null, sources, zipper, bufferSize, delayError));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Single<Boolean> all(Predicate<? super T> predicate) {
/*  5023 */     ObjectHelper.requireNonNull(predicate, "predicate is null");
/*  5024 */     return RxJavaPlugins.onAssembly((Single)new ObservableAllSingle(this, predicate));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> ambWith(ObservableSource<? extends T> other) {
/*  5048 */     ObjectHelper.requireNonNull(other, "other is null");
/*  5049 */     return ambArray((ObservableSource<? extends T>[])new ObservableSource[] { this, other });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Single<Boolean> any(Predicate<? super T> predicate) {
/*  5075 */     ObjectHelper.requireNonNull(predicate, "predicate is null");
/*  5076 */     return RxJavaPlugins.onAssembly((Single)new ObservableAnySingle(this, predicate));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> R as(@NonNull ObservableConverter<T, ? extends R> converter) {
/*  5097 */     return ((ObservableConverter<T, R>)ObjectHelper.requireNonNull(converter, "converter is null")).apply(this);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final T blockingFirst() {
/*  5118 */     BlockingFirstObserver<T> observer = new BlockingFirstObserver();
/*  5119 */     subscribe((Observer<? super T>)observer);
/*  5120 */     T v = (T)observer.blockingGet();
/*  5121 */     if (v != null) {
/*  5122 */       return v;
/*       */     }
/*  5124 */     throw new NoSuchElementException();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final T blockingFirst(T defaultItem) {
/*  5146 */     BlockingFirstObserver<T> observer = new BlockingFirstObserver();
/*  5147 */     subscribe((Observer<? super T>)observer);
/*  5148 */     T v = (T)observer.blockingGet();
/*  5149 */     return (v != null) ? v : defaultItem;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final void blockingForEach(Consumer<? super T> onNext) {
/*  5183 */     Iterator<T> it = blockingIterable().iterator();
/*  5184 */     while (it.hasNext()) {
/*       */       try {
/*  5186 */         onNext.accept(it.next());
/*  5187 */       } catch (Throwable e) {
/*  5188 */         Exceptions.throwIfFatal(e);
/*  5189 */         ((Disposable)it).dispose();
/*  5190 */         throw ExceptionHelper.wrapOrThrow(e);
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
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("none")
/*       */   public final Iterable<T> blockingIterable() {
/*  5210 */     return blockingIterable(bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Iterable<T> blockingIterable(int bufferSize) {
/*  5229 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/*  5230 */     return (Iterable<T>)new BlockingObservableIterable(this, bufferSize);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final T blockingLast() {
/*  5255 */     BlockingLastObserver<T> observer = new BlockingLastObserver();
/*  5256 */     subscribe((Observer<? super T>)observer);
/*  5257 */     T v = (T)observer.blockingGet();
/*  5258 */     if (v != null) {
/*  5259 */       return v;
/*       */     }
/*  5261 */     throw new NoSuchElementException();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final T blockingLast(T defaultItem) {
/*  5287 */     BlockingLastObserver<T> observer = new BlockingLastObserver();
/*  5288 */     subscribe((Observer<? super T>)observer);
/*  5289 */     T v = (T)observer.blockingGet();
/*  5290 */     return (v != null) ? v : defaultItem;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Iterable<T> blockingLatest() {
/*  5315 */     return (Iterable<T>)new BlockingObservableLatest(this);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Iterable<T> blockingMostRecent(T initialValue) {
/*  5338 */     return (Iterable<T>)new BlockingObservableMostRecent(this, initialValue);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Iterable<T> blockingNext() {
/*  5358 */     return (Iterable<T>)new BlockingObservableNext(this);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final T blockingSingle() {
/*  5381 */     T v = singleElement().blockingGet();
/*  5382 */     if (v == null) {
/*  5383 */       throw new NoSuchElementException();
/*       */     }
/*  5385 */     return v;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final T blockingSingle(T defaultItem) {
/*  5412 */     return single(defaultItem).blockingGet();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Future<T> toFuture() {
/*  5437 */     return (Future<T>)subscribeWith(new FutureObserver());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final void blockingSubscribe() {
/*  5459 */     ObservableBlockingSubscribe.subscribe(this);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final void blockingSubscribe(Consumer<? super T> onNext) {
/*  5487 */     ObservableBlockingSubscribe.subscribe(this, onNext, Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final void blockingSubscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError) {
/*  5509 */     ObservableBlockingSubscribe.subscribe(this, onNext, onError, Functions.EMPTY_ACTION);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final void blockingSubscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete) {
/*  5531 */     ObservableBlockingSubscribe.subscribe(this, onNext, onError, onComplete);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final void blockingSubscribe(Observer<? super T> observer) {
/*  5552 */     ObservableBlockingSubscribe.subscribe(this, observer);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<List<T>> buffer(int count) {
/*  5577 */     return buffer(count, count);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<List<T>> buffer(int count, int skip) {
/*  5606 */     return buffer(count, skip, ArrayListSupplier.asCallable());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <U extends Collection<? super T>> Observable<U> buffer(int count, int skip, Callable<U> bufferSupplier) {
/*  5639 */     ObjectHelper.verifyPositive(count, "count");
/*  5640 */     ObjectHelper.verifyPositive(skip, "skip");
/*  5641 */     ObjectHelper.requireNonNull(bufferSupplier, "bufferSupplier is null");
/*  5642 */     return RxJavaPlugins.onAssembly((Observable)new ObservableBuffer(this, count, skip, bufferSupplier));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <U extends Collection<? super T>> Observable<U> buffer(int count, Callable<U> bufferSupplier) {
/*  5671 */     return buffer(count, count, bufferSupplier);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Observable<List<T>> buffer(long timespan, long timeskip, TimeUnit unit) {
/*  5701 */     return buffer(timespan, timeskip, unit, Schedulers.computation(), ArrayListSupplier.asCallable());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<List<T>> buffer(long timespan, long timeskip, TimeUnit unit, Scheduler scheduler) {
/*  5734 */     return buffer(timespan, timeskip, unit, scheduler, ArrayListSupplier.asCallable());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final <U extends Collection<? super T>> Observable<U> buffer(long timespan, long timeskip, TimeUnit unit, Scheduler scheduler, Callable<U> bufferSupplier) {
/*  5771 */     ObjectHelper.requireNonNull(unit, "unit is null");
/*  5772 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/*  5773 */     ObjectHelper.requireNonNull(bufferSupplier, "bufferSupplier is null");
/*  5774 */     return RxJavaPlugins.onAssembly((Observable)new ObservableBufferTimed(this, timespan, timeskip, unit, scheduler, bufferSupplier, 2147483647, false));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Observable<List<T>> buffer(long timespan, TimeUnit unit) {
/*  5803 */     return buffer(timespan, unit, Schedulers.computation(), 2147483647);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Observable<List<T>> buffer(long timespan, TimeUnit unit, int count) {
/*  5836 */     return buffer(timespan, unit, Schedulers.computation(), count);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<List<T>> buffer(long timespan, TimeUnit unit, Scheduler scheduler, int count) {
/*  5871 */     return buffer(timespan, unit, scheduler, count, ArrayListSupplier.asCallable(), false);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final <U extends Collection<? super T>> Observable<U> buffer(long timespan, TimeUnit unit, Scheduler scheduler, int count, Callable<U> bufferSupplier, boolean restartTimerOnMaxSize) {
/*  5916 */     ObjectHelper.requireNonNull(unit, "unit is null");
/*  5917 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/*  5918 */     ObjectHelper.requireNonNull(bufferSupplier, "bufferSupplier is null");
/*  5919 */     ObjectHelper.verifyPositive(count, "count");
/*  5920 */     return RxJavaPlugins.onAssembly((Observable)new ObservableBufferTimed(this, timespan, timespan, unit, scheduler, bufferSupplier, count, restartTimerOnMaxSize));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<List<T>> buffer(long timespan, TimeUnit unit, Scheduler scheduler) {
/*  5951 */     return buffer(timespan, unit, scheduler, 2147483647, ArrayListSupplier.asCallable(), false);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <TOpening, TClosing> Observable<List<T>> buffer(ObservableSource<? extends TOpening> openingIndicator, Function<? super TOpening, ? extends ObservableSource<? extends TClosing>> closingIndicator) {
/*  5983 */     return buffer(openingIndicator, closingIndicator, ArrayListSupplier.asCallable());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <TOpening, TClosing, U extends Collection<? super T>> Observable<U> buffer(ObservableSource<? extends TOpening> openingIndicator, Function<? super TOpening, ? extends ObservableSource<? extends TClosing>> closingIndicator, Callable<U> bufferSupplier) {
/*  6020 */     ObjectHelper.requireNonNull(openingIndicator, "openingIndicator is null");
/*  6021 */     ObjectHelper.requireNonNull(closingIndicator, "closingIndicator is null");
/*  6022 */     ObjectHelper.requireNonNull(bufferSupplier, "bufferSupplier is null");
/*  6023 */     return RxJavaPlugins.onAssembly((Observable)new ObservableBufferBoundary(this, openingIndicator, closingIndicator, bufferSupplier));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <B> Observable<List<T>> buffer(ObservableSource<B> boundary) {
/*  6053 */     return buffer(boundary, ArrayListSupplier.asCallable());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <B> Observable<List<T>> buffer(ObservableSource<B> boundary, int initialCapacity) {
/*  6085 */     ObjectHelper.verifyPositive(initialCapacity, "initialCapacity");
/*  6086 */     return buffer(boundary, Functions.createArrayList(initialCapacity));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <B, U extends Collection<? super T>> Observable<U> buffer(ObservableSource<B> boundary, Callable<U> bufferSupplier) {
/*  6120 */     ObjectHelper.requireNonNull(boundary, "boundary is null");
/*  6121 */     ObjectHelper.requireNonNull(bufferSupplier, "bufferSupplier is null");
/*  6122 */     return RxJavaPlugins.onAssembly((Observable)new ObservableBufferExactBoundary(this, boundary, bufferSupplier));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <B> Observable<List<T>> buffer(Callable<? extends ObservableSource<B>> boundarySupplier) {
/*  6151 */     return buffer(boundarySupplier, ArrayListSupplier.asCallable());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <B, U extends Collection<? super T>> Observable<U> buffer(Callable<? extends ObservableSource<B>> boundarySupplier, Callable<U> bufferSupplier) {
/*  6184 */     ObjectHelper.requireNonNull(boundarySupplier, "boundarySupplier is null");
/*  6185 */     ObjectHelper.requireNonNull(bufferSupplier, "bufferSupplier is null");
/*  6186 */     return RxJavaPlugins.onAssembly((Observable)new ObservableBufferBoundarySupplier(this, boundarySupplier, bufferSupplier));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> cache() {
/*  6240 */     return cacheWithInitialCapacity(16);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> cacheWithInitialCapacity(int initialCapacity) {
/*  6298 */     ObjectHelper.verifyPositive(initialCapacity, "initialCapacity");
/*  6299 */     return RxJavaPlugins.onAssembly((Observable)new ObservableCache(this, initialCapacity));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <U> Observable<U> cast(Class<U> clazz) {
/*  6323 */     ObjectHelper.requireNonNull(clazz, "clazz is null");
/*  6324 */     return map(Functions.castFunction(clazz));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <U> Single<U> collect(Callable<? extends U> initialValueSupplier, BiConsumer<? super U, ? super T> collector) {
/*  6356 */     ObjectHelper.requireNonNull(initialValueSupplier, "initialValueSupplier is null");
/*  6357 */     ObjectHelper.requireNonNull(collector, "collector is null");
/*  6358 */     return RxJavaPlugins.onAssembly((Single)new ObservableCollectSingle(this, initialValueSupplier, collector));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <U> Single<U> collectInto(U initialValue, BiConsumer<? super U, ? super T> collector) {
/*  6390 */     ObjectHelper.requireNonNull(initialValue, "initialValue is null");
/*  6391 */     return collect(Functions.justCallable(initialValue), collector);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> compose(ObservableTransformer<? super T, ? extends R> composer) {
/*  6417 */     return wrap(((ObservableTransformer<T, R>)ObjectHelper.requireNonNull(composer, "composer is null")).apply(this));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> concatMap(Function<? super T, ? extends ObservableSource<? extends R>> mapper) {
/*  6442 */     return concatMap(mapper, 2);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> concatMap(Function<? super T, ? extends ObservableSource<? extends R>> mapper, int prefetch) {
/*  6469 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/*  6470 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/*  6471 */     if (this instanceof ScalarCallable) {
/*       */       
/*  6473 */       T v = (T)((ScalarCallable)this).call();
/*  6474 */       if (v == null) {
/*  6475 */         return empty();
/*       */       }
/*  6477 */       return ObservableScalarXMap.scalarXMap(v, mapper);
/*       */     } 
/*  6479 */     return RxJavaPlugins.onAssembly((Observable)new ObservableConcatMap(this, mapper, prefetch, ErrorMode.IMMEDIATE));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> concatMapDelayError(Function<? super T, ? extends ObservableSource<? extends R>> mapper) {
/*  6501 */     return concatMapDelayError(mapper, bufferSize(), true);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> concatMapDelayError(Function<? super T, ? extends ObservableSource<? extends R>> mapper, int prefetch, boolean tillTheEnd) {
/*  6529 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/*  6530 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/*  6531 */     if (this instanceof ScalarCallable) {
/*       */       
/*  6533 */       T v = (T)((ScalarCallable)this).call();
/*  6534 */       if (v == null) {
/*  6535 */         return empty();
/*       */       }
/*  6537 */       return ObservableScalarXMap.scalarXMap(v, mapper);
/*       */     } 
/*  6539 */     return RxJavaPlugins.onAssembly((Observable)new ObservableConcatMap(this, mapper, prefetch, tillTheEnd ? ErrorMode.END : ErrorMode.BOUNDARY));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> concatMapEager(Function<? super T, ? extends ObservableSource<? extends R>> mapper) {
/*  6564 */     return concatMapEager(mapper, 2147483647, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> concatMapEager(Function<? super T, ? extends ObservableSource<? extends R>> mapper, int maxConcurrency, int prefetch) {
/*  6592 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/*  6593 */     ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
/*  6594 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/*  6595 */     return RxJavaPlugins.onAssembly((Observable)new ObservableConcatMapEager(this, mapper, ErrorMode.IMMEDIATE, maxConcurrency, prefetch));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> concatMapEagerDelayError(Function<? super T, ? extends ObservableSource<? extends R>> mapper, boolean tillTheEnd) {
/*  6624 */     return concatMapEagerDelayError(mapper, 2147483647, bufferSize(), tillTheEnd);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> concatMapEagerDelayError(Function<? super T, ? extends ObservableSource<? extends R>> mapper, int maxConcurrency, int prefetch, boolean tillTheEnd) {
/*  6657 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/*  6658 */     ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
/*  6659 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/*  6660 */     return RxJavaPlugins.onAssembly((Observable)new ObservableConcatMapEager(this, mapper, tillTheEnd ? ErrorMode.END : ErrorMode.BOUNDARY, maxConcurrency, prefetch));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Completable concatMapCompletable(Function<? super T, ? extends CompletableSource> mapper) {
/*  6681 */     return concatMapCompletable(mapper, 2);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Completable concatMapCompletable(Function<? super T, ? extends CompletableSource> mapper, int capacityHint) {
/*  6706 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/*  6707 */     ObjectHelper.verifyPositive(capacityHint, "capacityHint");
/*  6708 */     return RxJavaPlugins.onAssembly((Completable)new ObservableConcatMapCompletable(this, mapper, ErrorMode.IMMEDIATE, capacityHint));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Completable concatMapCompletableDelayError(Function<? super T, ? extends CompletableSource> mapper) {
/*  6732 */     return concatMapCompletableDelayError(mapper, true, 2);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Completable concatMapCompletableDelayError(Function<? super T, ? extends CompletableSource> mapper, boolean tillTheEnd) {
/*  6762 */     return concatMapCompletableDelayError(mapper, tillTheEnd, 2);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Completable concatMapCompletableDelayError(Function<? super T, ? extends CompletableSource> mapper, boolean tillTheEnd, int prefetch) {
/*  6796 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/*  6797 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/*  6798 */     return RxJavaPlugins.onAssembly((Completable)new ObservableConcatMapCompletable(this, mapper, tillTheEnd ? ErrorMode.END : ErrorMode.BOUNDARY, prefetch));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <U> Observable<U> concatMapIterable(Function<? super T, ? extends Iterable<? extends U>> mapper) {
/*  6824 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/*  6825 */     return RxJavaPlugins.onAssembly((Observable)new ObservableFlattenIterable(this, mapper));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <U> Observable<U> concatMapIterable(Function<? super T, ? extends Iterable<? extends U>> mapper, int prefetch) {
/*  6853 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/*  6854 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/*  6855 */     return concatMap(ObservableInternalHelper.flatMapIntoIterable(mapper), prefetch);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> concatMapMaybe(Function<? super T, ? extends MaybeSource<? extends R>> mapper) {
/*  6881 */     return concatMapMaybe(mapper, 2);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> concatMapMaybe(Function<? super T, ? extends MaybeSource<? extends R>> mapper, int prefetch) {
/*  6911 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/*  6912 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/*  6913 */     return RxJavaPlugins.onAssembly((Observable)new ObservableConcatMapMaybe(this, mapper, ErrorMode.IMMEDIATE, prefetch));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> concatMapMaybeDelayError(Function<? super T, ? extends MaybeSource<? extends R>> mapper) {
/*  6939 */     return concatMapMaybeDelayError(mapper, true, 2);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> concatMapMaybeDelayError(Function<? super T, ? extends MaybeSource<? extends R>> mapper, boolean tillTheEnd) {
/*  6971 */     return concatMapMaybeDelayError(mapper, tillTheEnd, 2);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> concatMapMaybeDelayError(Function<? super T, ? extends MaybeSource<? extends R>> mapper, boolean tillTheEnd, int prefetch) {
/*  7006 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/*  7007 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/*  7008 */     return RxJavaPlugins.onAssembly((Observable)new ObservableConcatMapMaybe(this, mapper, tillTheEnd ? ErrorMode.END : ErrorMode.BOUNDARY, prefetch));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> concatMapSingle(Function<? super T, ? extends SingleSource<? extends R>> mapper) {
/*  7034 */     return concatMapSingle(mapper, 2);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> concatMapSingle(Function<? super T, ? extends SingleSource<? extends R>> mapper, int prefetch) {
/*  7064 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/*  7065 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/*  7066 */     return RxJavaPlugins.onAssembly((Observable)new ObservableConcatMapSingle(this, mapper, ErrorMode.IMMEDIATE, prefetch));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> concatMapSingleDelayError(Function<? super T, ? extends SingleSource<? extends R>> mapper) {
/*  7092 */     return concatMapSingleDelayError(mapper, true, 2);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> concatMapSingleDelayError(Function<? super T, ? extends SingleSource<? extends R>> mapper, boolean tillTheEnd) {
/*  7124 */     return concatMapSingleDelayError(mapper, tillTheEnd, 2);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> concatMapSingleDelayError(Function<? super T, ? extends SingleSource<? extends R>> mapper, boolean tillTheEnd, int prefetch) {
/*  7159 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/*  7160 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/*  7161 */     return RxJavaPlugins.onAssembly((Observable)new ObservableConcatMapSingle(this, mapper, tillTheEnd ? ErrorMode.END : ErrorMode.BOUNDARY, prefetch));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> concatWith(ObservableSource<? extends T> other) {
/*  7183 */     ObjectHelper.requireNonNull(other, "other is null");
/*  7184 */     return concat(this, other);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> concatWith(@NonNull SingleSource<? extends T> other) {
/*  7204 */     ObjectHelper.requireNonNull(other, "other is null");
/*  7205 */     return RxJavaPlugins.onAssembly((Observable)new ObservableConcatWithSingle(this, other));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> concatWith(@NonNull MaybeSource<? extends T> other) {
/*  7225 */     ObjectHelper.requireNonNull(other, "other is null");
/*  7226 */     return RxJavaPlugins.onAssembly((Observable)new ObservableConcatWithMaybe(this, other));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> concatWith(@NonNull CompletableSource other) {
/*  7246 */     ObjectHelper.requireNonNull(other, "other is null");
/*  7247 */     return RxJavaPlugins.onAssembly((Observable)new ObservableConcatWithCompletable(this, other));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Single<Boolean> contains(Object element) {
/*  7269 */     ObjectHelper.requireNonNull(element, "element is null");
/*  7270 */     return any(Functions.equalsWith(element));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Single<Long> count() {
/*  7290 */     return RxJavaPlugins.onAssembly((Single)new ObservableCountSingle(this));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <U> Observable<T> debounce(Function<? super T, ? extends ObservableSource<U>> debounceSelector) {
/*  7322 */     ObjectHelper.requireNonNull(debounceSelector, "debounceSelector is null");
/*  7323 */     return RxJavaPlugins.onAssembly((Observable)new ObservableDebounce(this, debounceSelector));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Observable<T> debounce(long timeout, TimeUnit unit) {
/*  7361 */     return debounce(timeout, unit, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<T> debounce(long timeout, TimeUnit unit, Scheduler scheduler) {
/*  7401 */     ObjectHelper.requireNonNull(unit, "unit is null");
/*  7402 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/*  7403 */     return RxJavaPlugins.onAssembly((Observable)new ObservableDebounceTimed(this, timeout, unit, scheduler));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> defaultIfEmpty(T defaultItem) {
/*  7425 */     ObjectHelper.requireNonNull(defaultItem, "defaultItem is null");
/*  7426 */     return switchIfEmpty(just(defaultItem));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <U> Observable<T> delay(Function<? super T, ? extends ObservableSource<U>> itemDelay) {
/*  7455 */     ObjectHelper.requireNonNull(itemDelay, "itemDelay is null");
/*  7456 */     return flatMap(ObservableInternalHelper.itemDelay(itemDelay));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Observable<T> delay(long delay, TimeUnit unit) {
/*  7479 */     return delay(delay, unit, Schedulers.computation(), false);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Observable<T> delay(long delay, TimeUnit unit, boolean delayError) {
/*  7505 */     return delay(delay, unit, Schedulers.computation(), delayError);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<T> delay(long delay, TimeUnit unit, Scheduler scheduler) {
/*  7530 */     return delay(delay, unit, scheduler, false);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<T> delay(long delay, TimeUnit unit, Scheduler scheduler, boolean delayError) {
/*  7558 */     ObjectHelper.requireNonNull(unit, "unit is null");
/*  7559 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/*       */     
/*  7561 */     return RxJavaPlugins.onAssembly((Observable)new ObservableDelay(this, delay, unit, scheduler, delayError));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <U, V> Observable<T> delay(ObservableSource<U> subscriptionDelay, Function<? super T, ? extends ObservableSource<V>> itemDelay) {
/*  7596 */     return delaySubscription(subscriptionDelay).delay(itemDelay);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <U> Observable<T> delaySubscription(ObservableSource<U> other) {
/*  7619 */     ObjectHelper.requireNonNull(other, "other is null");
/*  7620 */     return RxJavaPlugins.onAssembly((Observable)new ObservableDelaySubscriptionOther(this, other));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Observable<T> delaySubscription(long delay, TimeUnit unit) {
/*  7642 */     return delaySubscription(delay, unit, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<T> delaySubscription(long delay, TimeUnit unit, Scheduler scheduler) {
/*  7668 */     return delaySubscription(timer(delay, unit, scheduler));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @Deprecated
/*       */   public final <T2> Observable<T2> dematerialize() {
/*  7715 */     return RxJavaPlugins.onAssembly((Observable)new ObservableDematerialize(this, Functions.identity()));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @Experimental
/*       */   public final <R> Observable<R> dematerialize(Function<? super T, Notification<R>> selector) {
/*  7768 */     ObjectHelper.requireNonNull(selector, "selector is null");
/*  7769 */     return RxJavaPlugins.onAssembly((Observable)new ObservableDematerialize(this, selector));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> distinct() {
/*  7806 */     return distinct(Functions.identity(), Functions.createHashSet());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <K> Observable<T> distinct(Function<? super T, K> keySelector) {
/*  7846 */     return distinct(keySelector, Functions.createHashSet());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <K> Observable<T> distinct(Function<? super T, K> keySelector, Callable<? extends Collection<? super K>> collectionSupplier) {
/*  7877 */     ObjectHelper.requireNonNull(keySelector, "keySelector is null");
/*  7878 */     ObjectHelper.requireNonNull(collectionSupplier, "collectionSupplier is null");
/*  7879 */     return RxJavaPlugins.onAssembly((Observable)new ObservableDistinct(this, keySelector, collectionSupplier));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> distinctUntilChanged() {
/*  7916 */     return distinctUntilChanged(Functions.identity());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <K> Observable<T> distinctUntilChanged(Function<? super T, K> keySelector) {
/*  7958 */     ObjectHelper.requireNonNull(keySelector, "keySelector is null");
/*  7959 */     return RxJavaPlugins.onAssembly((Observable)new ObservableDistinctUntilChanged(this, keySelector, ObjectHelper.equalsPredicate()));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> distinctUntilChanged(BiPredicate<? super T, ? super T> comparer) {
/*  7992 */     ObjectHelper.requireNonNull(comparer, "comparer is null");
/*  7993 */     return RxJavaPlugins.onAssembly((Observable)new ObservableDistinctUntilChanged(this, Functions.identity(), comparer));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> doAfterNext(Consumer<? super T> onAfterNext) {
/*  8016 */     ObjectHelper.requireNonNull(onAfterNext, "onAfterNext is null");
/*  8017 */     return RxJavaPlugins.onAssembly((Observable)new ObservableDoAfterNext(this, onAfterNext));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> doAfterTerminate(Action onFinally) {
/*  8040 */     ObjectHelper.requireNonNull(onFinally, "onFinally is null");
/*  8041 */     return doOnEach(Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, onFinally);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> doFinally(Action onFinally) {
/*  8067 */     ObjectHelper.requireNonNull(onFinally, "onFinally is null");
/*  8068 */     return RxJavaPlugins.onAssembly((Observable)new ObservableDoFinally(this, onFinally));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> doOnDispose(Action onDispose) {
/*  8095 */     return doOnLifecycle(Functions.emptyConsumer(), onDispose);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> doOnComplete(Action onComplete) {
/*  8115 */     return doOnEach(Functions.emptyConsumer(), Functions.emptyConsumer(), onComplete, Functions.EMPTY_ACTION);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   private Observable<T> doOnEach(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete, Action onAfterTerminate) {
/*  8134 */     ObjectHelper.requireNonNull(onNext, "onNext is null");
/*  8135 */     ObjectHelper.requireNonNull(onError, "onError is null");
/*  8136 */     ObjectHelper.requireNonNull(onComplete, "onComplete is null");
/*  8137 */     ObjectHelper.requireNonNull(onAfterTerminate, "onAfterTerminate is null");
/*  8138 */     return RxJavaPlugins.onAssembly((Observable)new ObservableDoOnEach(this, onNext, onError, onComplete, onAfterTerminate));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> doOnEach(Consumer<? super Notification<T>> onNotification) {
/*  8158 */     ObjectHelper.requireNonNull(onNotification, "onNotification is null");
/*  8159 */     return doOnEach(
/*  8160 */         Functions.notificationOnNext(onNotification), 
/*  8161 */         Functions.notificationOnError(onNotification), 
/*  8162 */         Functions.notificationOnComplete(onNotification), Functions.EMPTY_ACTION);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> doOnEach(Observer<? super T> observer) {
/*  8190 */     ObjectHelper.requireNonNull(observer, "observer is null");
/*  8191 */     return doOnEach(
/*  8192 */         ObservableInternalHelper.observerOnNext(observer), 
/*  8193 */         ObservableInternalHelper.observerOnError(observer), 
/*  8194 */         ObservableInternalHelper.observerOnComplete(observer), Functions.EMPTY_ACTION);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> doOnError(Consumer<? super Throwable> onError) {
/*  8218 */     return doOnEach(Functions.emptyConsumer(), onError, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> doOnLifecycle(Consumer<? super Disposable> onSubscribe, Action onDispose) {
/*  8241 */     ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");
/*  8242 */     ObjectHelper.requireNonNull(onDispose, "onDispose is null");
/*  8243 */     return RxJavaPlugins.onAssembly((Observable)new ObservableDoOnLifecycle(this, onSubscribe, onDispose));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> doOnNext(Consumer<? super T> onNext) {
/*  8263 */     return doOnEach(onNext, Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> doOnSubscribe(Consumer<? super Disposable> onSubscribe) {
/*  8286 */     return doOnLifecycle(onSubscribe, Functions.EMPTY_ACTION);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> doOnTerminate(Action onTerminate) {
/*  8311 */     ObjectHelper.requireNonNull(onTerminate, "onTerminate is null");
/*  8312 */     return doOnEach(Functions.emptyConsumer(), 
/*  8313 */         Functions.actionConsumer(onTerminate), onTerminate, Functions.EMPTY_ACTION);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Maybe<T> elementAt(long index) {
/*  8338 */     if (index < 0L) {
/*  8339 */       throw new IndexOutOfBoundsException("index >= 0 required but it was " + index);
/*       */     }
/*  8341 */     return RxJavaPlugins.onAssembly((Maybe)new ObservableElementAtMaybe(this, index));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Single<T> elementAt(long index, T defaultItem) {
/*  8367 */     if (index < 0L) {
/*  8368 */       throw new IndexOutOfBoundsException("index >= 0 required but it was " + index);
/*       */     }
/*  8370 */     ObjectHelper.requireNonNull(defaultItem, "defaultItem is null");
/*  8371 */     return RxJavaPlugins.onAssembly((Single)new ObservableElementAtSingle(this, index, defaultItem));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Single<T> elementAtOrError(long index) {
/*  8395 */     if (index < 0L) {
/*  8396 */       throw new IndexOutOfBoundsException("index >= 0 required but it was " + index);
/*       */     }
/*  8398 */     return RxJavaPlugins.onAssembly((Single)new ObservableElementAtSingle(this, index, null));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> filter(Predicate<? super T> predicate) {
/*  8420 */     ObjectHelper.requireNonNull(predicate, "predicate is null");
/*  8421 */     return RxJavaPlugins.onAssembly((Observable)new ObservableFilter(this, predicate));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Maybe<T> firstElement() {
/*  8440 */     return elementAt(0L);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Single<T> first(T defaultItem) {
/*  8461 */     return elementAt(0L, defaultItem);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Single<T> firstOrError() {
/*  8480 */     return elementAtOrError(0L);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends R>> mapper) {
/*  8506 */     return flatMap(mapper, false);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends R>> mapper, boolean delayErrors) {
/*  8535 */     return flatMap(mapper, delayErrors, 2147483647);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends R>> mapper, boolean delayErrors, int maxConcurrency) {
/*  8568 */     return flatMap(mapper, delayErrors, maxConcurrency, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends R>> mapper, boolean delayErrors, int maxConcurrency, int bufferSize) {
/*  8604 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/*  8605 */     ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
/*  8606 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/*  8607 */     if (this instanceof ScalarCallable) {
/*       */       
/*  8609 */       T v = (T)((ScalarCallable)this).call();
/*  8610 */       if (v == null) {
/*  8611 */         return empty();
/*       */       }
/*  8613 */       return ObservableScalarXMap.scalarXMap(v, mapper);
/*       */     } 
/*  8615 */     return RxJavaPlugins.onAssembly((Observable)new ObservableFlatMap(this, mapper, delayErrors, maxConcurrency, bufferSize));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends R>> onNextMapper, Function<? super Throwable, ? extends ObservableSource<? extends R>> onErrorMapper, Callable<? extends ObservableSource<? extends R>> onCompleteSupplier) {
/*  8648 */     ObjectHelper.requireNonNull(onNextMapper, "onNextMapper is null");
/*  8649 */     ObjectHelper.requireNonNull(onErrorMapper, "onErrorMapper is null");
/*  8650 */     ObjectHelper.requireNonNull(onCompleteSupplier, "onCompleteSupplier is null");
/*  8651 */     return merge((ObservableSource<? extends ObservableSource<? extends R>>)new ObservableMapNotification(this, onNextMapper, onErrorMapper, onCompleteSupplier));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends R>> onNextMapper, Function<Throwable, ? extends ObservableSource<? extends R>> onErrorMapper, Callable<? extends ObservableSource<? extends R>> onCompleteSupplier, int maxConcurrency) {
/*  8689 */     ObjectHelper.requireNonNull(onNextMapper, "onNextMapper is null");
/*  8690 */     ObjectHelper.requireNonNull(onErrorMapper, "onErrorMapper is null");
/*  8691 */     ObjectHelper.requireNonNull(onCompleteSupplier, "onCompleteSupplier is null");
/*  8692 */     return merge((ObservableSource<? extends ObservableSource<? extends R>>)new ObservableMapNotification(this, onNextMapper, onErrorMapper, onCompleteSupplier), maxConcurrency);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends R>> mapper, int maxConcurrency) {
/*  8722 */     return flatMap(mapper, false, maxConcurrency, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <U, R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends R> resultSelector) {
/*  8752 */     return flatMap(mapper, resultSelector, false, bufferSize(), bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <U, R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends R> combiner, boolean delayErrors) {
/*  8785 */     return flatMap(mapper, combiner, delayErrors, bufferSize(), bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <U, R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends R> combiner, boolean delayErrors, int maxConcurrency) {
/*  8822 */     return flatMap(mapper, combiner, delayErrors, maxConcurrency, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <U, R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends R> combiner, boolean delayErrors, int maxConcurrency, int bufferSize) {
/*  8861 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/*  8862 */     ObjectHelper.requireNonNull(combiner, "combiner is null");
/*  8863 */     return flatMap(ObservableInternalHelper.flatMapWithCombiner(mapper, combiner), delayErrors, maxConcurrency, bufferSize);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <U, R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends R> combiner, int maxConcurrency) {
/*  8897 */     return flatMap(mapper, combiner, false, maxConcurrency, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Completable flatMapCompletable(Function<? super T, ? extends CompletableSource> mapper) {
/*  8915 */     return flatMapCompletable(mapper, false);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Completable flatMapCompletable(Function<? super T, ? extends CompletableSource> mapper, boolean delayErrors) {
/*  8935 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/*  8936 */     return RxJavaPlugins.onAssembly((Completable)new ObservableFlatMapCompletableCompletable(this, mapper, delayErrors));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <U> Observable<U> flatMapIterable(Function<? super T, ? extends Iterable<? extends U>> mapper) {
/*  8961 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/*  8962 */     return RxJavaPlugins.onAssembly((Observable)new ObservableFlattenIterable(this, mapper));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <U, V> Observable<V> flatMapIterable(Function<? super T, ? extends Iterable<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends V> resultSelector) {
/*  8993 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/*  8994 */     ObjectHelper.requireNonNull(resultSelector, "resultSelector is null");
/*  8995 */     return flatMap(ObservableInternalHelper.flatMapIntoIterable(mapper), resultSelector, false, bufferSize(), bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> flatMapMaybe(Function<? super T, ? extends MaybeSource<? extends R>> mapper) {
/*  9014 */     return flatMapMaybe(mapper, false);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> flatMapMaybe(Function<? super T, ? extends MaybeSource<? extends R>> mapper, boolean delayErrors) {
/*  9036 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/*  9037 */     return RxJavaPlugins.onAssembly((Observable)new ObservableFlatMapMaybe(this, mapper, delayErrors));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> flatMapSingle(Function<? super T, ? extends SingleSource<? extends R>> mapper) {
/*  9056 */     return flatMapSingle(mapper, false);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> flatMapSingle(Function<? super T, ? extends SingleSource<? extends R>> mapper, boolean delayErrors) {
/*  9078 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/*  9079 */     return RxJavaPlugins.onAssembly((Observable)new ObservableFlatMapSingle(this, mapper, delayErrors));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Disposable forEach(Consumer<? super T> onNext) {
/*  9104 */     return subscribe(onNext);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Disposable forEachWhile(Predicate<? super T> onNext) {
/*  9132 */     return forEachWhile(onNext, Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Disposable forEachWhile(Predicate<? super T> onNext, Consumer<? super Throwable> onError) {
/*  9157 */     return forEachWhile(onNext, onError, Functions.EMPTY_ACTION);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Disposable forEachWhile(Predicate<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete) {
/*  9186 */     ObjectHelper.requireNonNull(onNext, "onNext is null");
/*  9187 */     ObjectHelper.requireNonNull(onError, "onError is null");
/*  9188 */     ObjectHelper.requireNonNull(onComplete, "onComplete is null");
/*       */     
/*  9190 */     ForEachWhileObserver<T> o = new ForEachWhileObserver(onNext, onError, onComplete);
/*  9191 */     subscribe((Observer<? super T>)o);
/*  9192 */     return (Disposable)o;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <K> Observable<GroupedObservable<K, T>> groupBy(Function<? super T, ? extends K> keySelector) {
/*  9226 */     return groupBy(keySelector, Functions.identity(), false, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <K> Observable<GroupedObservable<K, T>> groupBy(Function<? super T, ? extends K> keySelector, boolean delayError) {
/*  9263 */     return groupBy(keySelector, Functions.identity(), delayError, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <K, V> Observable<GroupedObservable<K, V>> groupBy(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector) {
/*  9301 */     return groupBy(keySelector, valueSelector, false, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <K, V> Observable<GroupedObservable<K, V>> groupBy(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector, boolean delayError) {
/*  9342 */     return groupBy(keySelector, valueSelector, delayError, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <K, V> Observable<GroupedObservable<K, V>> groupBy(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector, boolean delayError, int bufferSize) {
/*  9386 */     ObjectHelper.requireNonNull(keySelector, "keySelector is null");
/*  9387 */     ObjectHelper.requireNonNull(valueSelector, "valueSelector is null");
/*  9388 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/*       */     
/*  9390 */     return RxJavaPlugins.onAssembly((Observable)new ObservableGroupBy(this, keySelector, valueSelector, bufferSize, delayError));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <TRight, TLeftEnd, TRightEnd, R> Observable<R> groupJoin(ObservableSource<? extends TRight> other, Function<? super T, ? extends ObservableSource<TLeftEnd>> leftEnd, Function<? super TRight, ? extends ObservableSource<TRightEnd>> rightEnd, BiFunction<? super T, ? super Observable<TRight>, ? extends R> resultSelector) {
/*  9432 */     ObjectHelper.requireNonNull(other, "other is null");
/*  9433 */     ObjectHelper.requireNonNull(leftEnd, "leftEnd is null");
/*  9434 */     ObjectHelper.requireNonNull(rightEnd, "rightEnd is null");
/*  9435 */     ObjectHelper.requireNonNull(resultSelector, "resultSelector is null");
/*  9436 */     return RxJavaPlugins.onAssembly((Observable)new ObservableGroupJoin(this, other, leftEnd, rightEnd, resultSelector));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> hide() {
/*  9458 */     return RxJavaPlugins.onAssembly((Observable)new ObservableHide(this));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Completable ignoreElements() {
/*  9476 */     return RxJavaPlugins.onAssembly((Completable)new ObservableIgnoreElementsCompletable(this));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Single<Boolean> isEmpty() {
/*  9497 */     return all(Functions.alwaysFalse());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <TRight, TLeftEnd, TRightEnd, R> Observable<R> join(ObservableSource<? extends TRight> other, Function<? super T, ? extends ObservableSource<TLeftEnd>> leftEnd, Function<? super TRight, ? extends ObservableSource<TRightEnd>> rightEnd, BiFunction<? super T, ? super TRight, ? extends R> resultSelector) {
/*  9539 */     ObjectHelper.requireNonNull(other, "other is null");
/*  9540 */     ObjectHelper.requireNonNull(leftEnd, "leftEnd is null");
/*  9541 */     ObjectHelper.requireNonNull(rightEnd, "rightEnd is null");
/*  9542 */     ObjectHelper.requireNonNull(resultSelector, "resultSelector is null");
/*  9543 */     return RxJavaPlugins.onAssembly((Observable)new ObservableJoin(this, other, leftEnd, rightEnd, resultSelector));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Maybe<T> lastElement() {
/*  9564 */     return RxJavaPlugins.onAssembly((Maybe)new ObservableLastMaybe(this));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Single<T> last(T defaultItem) {
/*  9586 */     ObjectHelper.requireNonNull(defaultItem, "defaultItem is null");
/*  9587 */     return RxJavaPlugins.onAssembly((Single)new ObservableLastSingle(this, defaultItem));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Single<T> lastOrError() {
/*  9607 */     return RxJavaPlugins.onAssembly((Single)new ObservableLastSingle(this, null));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> lift(ObservableOperator<? extends R, ? super T> lifter) {
/*  9755 */     ObjectHelper.requireNonNull(lifter, "lifter is null");
/*  9756 */     return RxJavaPlugins.onAssembly((Observable)new ObservableLift(this, lifter));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> map(Function<? super T, ? extends R> mapper) {
/*  9779 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/*  9780 */     return RxJavaPlugins.onAssembly((Observable)new ObservableMap(this, mapper));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<Notification<T>> materialize() {
/*  9801 */     return RxJavaPlugins.onAssembly((Observable)new ObservableMaterialize(this));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> mergeWith(ObservableSource<? extends T> other) {
/*  9824 */     ObjectHelper.requireNonNull(other, "other is null");
/*  9825 */     return merge(this, other);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> mergeWith(@NonNull SingleSource<? extends T> other) {
/*  9847 */     ObjectHelper.requireNonNull(other, "other is null");
/*  9848 */     return RxJavaPlugins.onAssembly((Observable)new ObservableMergeWithSingle(this, other));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> mergeWith(@NonNull MaybeSource<? extends T> other) {
/*  9871 */     ObjectHelper.requireNonNull(other, "other is null");
/*  9872 */     return RxJavaPlugins.onAssembly((Observable)new ObservableMergeWithMaybe(this, other));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> mergeWith(@NonNull CompletableSource other) {
/*  9892 */     ObjectHelper.requireNonNull(other, "other is null");
/*  9893 */     return RxJavaPlugins.onAssembly((Observable)new ObservableMergeWithCompletable(this, other));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<T> observeOn(Scheduler scheduler) {
/*  9930 */     return observeOn(scheduler, false, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<T> observeOn(Scheduler scheduler, boolean delayError) {
/*  9968 */     return observeOn(scheduler, delayError, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<T> observeOn(Scheduler scheduler, boolean delayError, int bufferSize) {
/* 10007 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 10008 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 10009 */     return RxJavaPlugins.onAssembly((Observable)new ObservableObserveOn(this, scheduler, delayError, bufferSize));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <U> Observable<U> ofType(Class<U> clazz) {
/* 10030 */     ObjectHelper.requireNonNull(clazz, "clazz is null");
/* 10031 */     return filter(Functions.isInstanceOf(clazz)).cast(clazz);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> onErrorResumeNext(Function<? super Throwable, ? extends ObservableSource<? extends T>> resumeFunction) {
/* 10066 */     ObjectHelper.requireNonNull(resumeFunction, "resumeFunction is null");
/* 10067 */     return RxJavaPlugins.onAssembly((Observable)new ObservableOnErrorNext(this, resumeFunction, false));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> onErrorResumeNext(ObservableSource<? extends T> next) {
/* 10102 */     ObjectHelper.requireNonNull(next, "next is null");
/* 10103 */     return onErrorResumeNext(Functions.justFunction(next));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> onErrorReturn(Function<? super Throwable, ? extends T> valueSupplier) {
/* 10135 */     ObjectHelper.requireNonNull(valueSupplier, "valueSupplier is null");
/* 10136 */     return RxJavaPlugins.onAssembly((Observable)new ObservableOnErrorReturn(this, valueSupplier));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> onErrorReturnItem(T item) {
/* 10168 */     ObjectHelper.requireNonNull(item, "item is null");
/* 10169 */     return onErrorReturn(Functions.justFunction(item));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> onExceptionResumeNext(ObservableSource<? extends T> next) {
/* 10207 */     ObjectHelper.requireNonNull(next, "next is null");
/* 10208 */     return RxJavaPlugins.onAssembly((Observable)new ObservableOnErrorNext(this, Functions.justFunction(next), true));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> onTerminateDetach() {
/* 10227 */     return RxJavaPlugins.onAssembly((Observable)new ObservableDetach(this));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final ConnectableObservable<T> publish() {
/* 10248 */     return ObservablePublish.create(this);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> publish(Function<? super Observable<T>, ? extends ObservableSource<R>> selector) {
/* 10273 */     ObjectHelper.requireNonNull(selector, "selector is null");
/* 10274 */     return RxJavaPlugins.onAssembly((Observable)new ObservablePublishSelector(this, selector));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Maybe<T> reduce(BiFunction<T, T, T> reducer) {
/* 10308 */     ObjectHelper.requireNonNull(reducer, "reducer is null");
/* 10309 */     return RxJavaPlugins.onAssembly((Maybe)new ObservableReduceMaybe(this, reducer));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Single<R> reduce(R seed, BiFunction<R, ? super T, R> reducer) {
/* 10365 */     ObjectHelper.requireNonNull(seed, "seed is null");
/* 10366 */     ObjectHelper.requireNonNull(reducer, "reducer is null");
/* 10367 */     return RxJavaPlugins.onAssembly((Single)new ObservableReduceSeedSingle(this, seed, reducer));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Single<R> reduceWith(Callable<R> seedSupplier, BiFunction<R, ? super T, R> reducer) {
/* 10405 */     ObjectHelper.requireNonNull(seedSupplier, "seedSupplier is null");
/* 10406 */     ObjectHelper.requireNonNull(reducer, "reducer is null");
/* 10407 */     return RxJavaPlugins.onAssembly((Single)new ObservableReduceWithSingle(this, seedSupplier, reducer));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> repeat() {
/* 10425 */     return repeat(Long.MAX_VALUE);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> repeat(long times) {
/* 10450 */     if (times < 0L) {
/* 10451 */       throw new IllegalArgumentException("times >= 0 required but it was " + times);
/*       */     }
/* 10453 */     if (times == 0L) {
/* 10454 */       return empty();
/*       */     }
/* 10456 */     return RxJavaPlugins.onAssembly((Observable)new ObservableRepeat(this, times));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> repeatUntil(BooleanSupplier stop) {
/* 10481 */     ObjectHelper.requireNonNull(stop, "stop is null");
/* 10482 */     return RxJavaPlugins.onAssembly((Observable)new ObservableRepeatUntil(this, stop));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> repeatWhen(Function<? super Observable<Object>, ? extends ObservableSource<?>> handler) {
/* 10507 */     ObjectHelper.requireNonNull(handler, "handler is null");
/* 10508 */     return RxJavaPlugins.onAssembly((Observable)new ObservableRepeatWhen(this, handler));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final ConnectableObservable<T> replay() {
/* 10530 */     return ObservableReplay.createFrom(this);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> replay(Function<? super Observable<T>, ? extends ObservableSource<R>> selector) {
/* 10555 */     ObjectHelper.requireNonNull(selector, "selector is null");
/* 10556 */     return ObservableReplay.multicastSelector(ObservableInternalHelper.replayCallable(this), selector);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> replay(Function<? super Observable<T>, ? extends ObservableSource<R>> selector, int bufferSize) {
/* 10588 */     ObjectHelper.requireNonNull(selector, "selector is null");
/* 10589 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 10590 */     return ObservableReplay.multicastSelector(ObservableInternalHelper.replayCallable(this, bufferSize), selector);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final <R> Observable<R> replay(Function<? super Observable<T>, ? extends ObservableSource<R>> selector, int bufferSize, long time, TimeUnit unit) {
/* 10627 */     return replay(selector, bufferSize, time, unit, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final <R> Observable<R> replay(Function<? super Observable<T>, ? extends ObservableSource<R>> selector, int bufferSize, long time, TimeUnit unit, Scheduler scheduler) {
/* 10668 */     ObjectHelper.requireNonNull(selector, "selector is null");
/* 10669 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 10670 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 10671 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 10672 */     return ObservableReplay.multicastSelector(
/* 10673 */         ObservableInternalHelper.replayCallable(this, bufferSize, time, unit, scheduler), selector);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final <R> Observable<R> replay(Function<? super Observable<T>, ? extends ObservableSource<R>> selector, int bufferSize, Scheduler scheduler) {
/* 10707 */     ObjectHelper.requireNonNull(selector, "selector is null");
/* 10708 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 10709 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 10710 */     return ObservableReplay.multicastSelector(ObservableInternalHelper.replayCallable(this, bufferSize), 
/* 10711 */         ObservableInternalHelper.replayFunction(selector, scheduler));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final <R> Observable<R> replay(Function<? super Observable<T>, ? extends ObservableSource<R>> selector, long time, TimeUnit unit) {
/* 10742 */     return replay(selector, time, unit, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final <R> Observable<R> replay(Function<? super Observable<T>, ? extends ObservableSource<R>> selector, long time, TimeUnit unit, Scheduler scheduler) {
/* 10775 */     ObjectHelper.requireNonNull(selector, "selector is null");
/* 10776 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 10777 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 10778 */     return ObservableReplay.multicastSelector(ObservableInternalHelper.replayCallable(this, time, unit, scheduler), selector);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final <R> Observable<R> replay(Function<? super Observable<T>, ? extends ObservableSource<R>> selector, Scheduler scheduler) {
/* 10806 */     ObjectHelper.requireNonNull(selector, "selector is null");
/* 10807 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 10808 */     return ObservableReplay.multicastSelector(ObservableInternalHelper.replayCallable(this), 
/* 10809 */         ObservableInternalHelper.replayFunction(selector, scheduler));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final ConnectableObservable<T> replay(int bufferSize) {
/* 10836 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 10837 */     return ObservableReplay.create(this, bufferSize);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final ConnectableObservable<T> replay(int bufferSize, long time, TimeUnit unit) {
/* 10869 */     return replay(bufferSize, time, unit, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final ConnectableObservable<T> replay(int bufferSize, long time, TimeUnit unit, Scheduler scheduler) {
/* 10905 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 10906 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 10907 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 10908 */     return ObservableReplay.create(this, time, unit, scheduler, bufferSize);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final ConnectableObservable<T> replay(int bufferSize, Scheduler scheduler) {
/* 10937 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 10938 */     return ObservableReplay.observeOn(replay(bufferSize), scheduler);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final ConnectableObservable<T> replay(long time, TimeUnit unit) {
/* 10964 */     return replay(time, unit, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final ConnectableObservable<T> replay(long time, TimeUnit unit, Scheduler scheduler) {
/* 10992 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 10993 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 10994 */     return ObservableReplay.create(this, time, unit, scheduler);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final ConnectableObservable<T> replay(Scheduler scheduler) {
/* 11019 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 11020 */     return ObservableReplay.observeOn(replay(), scheduler);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> retry() {
/* 11047 */     return retry(Long.MAX_VALUE, Functions.alwaysTrue());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> retry(BiPredicate<? super Integer, ? super Throwable> predicate) {
/* 11070 */     ObjectHelper.requireNonNull(predicate, "predicate is null");
/*       */     
/* 11072 */     return RxJavaPlugins.onAssembly((Observable)new ObservableRetryBiPredicate(this, predicate));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> retry(long times) {
/* 11102 */     return retry(times, Functions.alwaysTrue());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> retry(long times, Predicate<? super Throwable> predicate) {
/* 11120 */     if (times < 0L) {
/* 11121 */       throw new IllegalArgumentException("times >= 0 required but it was " + times);
/*       */     }
/* 11123 */     ObjectHelper.requireNonNull(predicate, "predicate is null");
/*       */     
/* 11125 */     return RxJavaPlugins.onAssembly((Observable)new ObservableRetryPredicate(this, times, predicate));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> retry(Predicate<? super Throwable> predicate) {
/* 11143 */     return retry(Long.MAX_VALUE, predicate);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> retryUntil(BooleanSupplier stop) {
/* 11160 */     ObjectHelper.requireNonNull(stop, "stop is null");
/* 11161 */     return retry(Long.MAX_VALUE, Functions.predicateReverseFor(stop));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> retryWhen(Function<? super Observable<Throwable>, ? extends ObservableSource<?>> handler) {
/* 11241 */     ObjectHelper.requireNonNull(handler, "handler is null");
/* 11242 */     return RxJavaPlugins.onAssembly((Observable)new ObservableRetryWhen(this, handler));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final void safeSubscribe(Observer<? super T> observer) {
/* 11259 */     ObjectHelper.requireNonNull(observer, "observer is null");
/* 11260 */     if (observer instanceof SafeObserver) {
/* 11261 */       subscribe(observer);
/*       */     } else {
/* 11263 */       subscribe((Observer<? super T>)new SafeObserver(observer));
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
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Observable<T> sample(long period, TimeUnit unit) {
/* 11289 */     return sample(period, unit, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Observable<T> sample(long period, TimeUnit unit, boolean emitLast) {
/* 11320 */     return sample(period, unit, Schedulers.computation(), emitLast);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<T> sample(long period, TimeUnit unit, Scheduler scheduler) {
/* 11347 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 11348 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 11349 */     return RxJavaPlugins.onAssembly((Observable)new ObservableSampleTimed(this, period, unit, scheduler, false));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<T> sample(long period, TimeUnit unit, Scheduler scheduler, boolean emitLast) {
/* 11383 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 11384 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 11385 */     return RxJavaPlugins.onAssembly((Observable)new ObservableSampleTimed(this, period, unit, scheduler, emitLast));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <U> Observable<T> sample(ObservableSource<U> sampler) {
/* 11409 */     ObjectHelper.requireNonNull(sampler, "sampler is null");
/* 11410 */     return RxJavaPlugins.onAssembly((Observable)new ObservableSampleWithObservable(this, sampler, false));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <U> Observable<T> sample(ObservableSource<U> sampler, boolean emitLast) {
/* 11441 */     ObjectHelper.requireNonNull(sampler, "sampler is null");
/* 11442 */     return RxJavaPlugins.onAssembly((Observable)new ObservableSampleWithObservable(this, sampler, emitLast));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> scan(BiFunction<T, T, T> accumulator) {
/* 11469 */     ObjectHelper.requireNonNull(accumulator, "accumulator is null");
/* 11470 */     return RxJavaPlugins.onAssembly((Observable)new ObservableScan(this, accumulator));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> scan(R initialValue, BiFunction<R, ? super T, R> accumulator) {
/* 11518 */     ObjectHelper.requireNonNull(initialValue, "initialValue is null");
/* 11519 */     return scanWith(Functions.justCallable(initialValue), accumulator);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> scanWith(Callable<R> seedSupplier, BiFunction<R, ? super T, R> accumulator) {
/* 11553 */     ObjectHelper.requireNonNull(seedSupplier, "seedSupplier is null");
/* 11554 */     ObjectHelper.requireNonNull(accumulator, "accumulator is null");
/* 11555 */     return RxJavaPlugins.onAssembly((Observable)new ObservableScanSeed(this, seedSupplier, accumulator));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> serialize() {
/* 11581 */     return RxJavaPlugins.onAssembly((Observable)new ObservableSerialized(this));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> share() {
/* 11604 */     return publish().refCount();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Maybe<T> singleElement() {
/* 11623 */     return RxJavaPlugins.onAssembly((Maybe)new ObservableSingleMaybe(this));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Single<T> single(T defaultItem) {
/* 11645 */     ObjectHelper.requireNonNull(defaultItem, "defaultItem is null");
/* 11646 */     return RxJavaPlugins.onAssembly((Single)new ObservableSingleSingle(this, defaultItem));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Single<T> singleOrError() {
/* 11667 */     return RxJavaPlugins.onAssembly((Single)new ObservableSingleSingle(this, null));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> skip(long count) {
/* 11689 */     if (count <= 0L) {
/* 11690 */       return RxJavaPlugins.onAssembly(this);
/*       */     }
/* 11692 */     return RxJavaPlugins.onAssembly((Observable)new ObservableSkip(this, count));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Observable<T> skip(long time, TimeUnit unit) {
/* 11717 */     return skipUntil(timer(time, unit));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<T> skip(long time, TimeUnit unit, Scheduler scheduler) {
/* 11743 */     return skipUntil(timer(time, unit, scheduler));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> skipLast(int count) {
/* 11771 */     if (count < 0) {
/* 11772 */       throw new IndexOutOfBoundsException("count >= 0 required but it was " + count);
/*       */     }
/* 11774 */     if (count == 0) {
/* 11775 */       return RxJavaPlugins.onAssembly(this);
/*       */     }
/* 11777 */     return RxJavaPlugins.onAssembly((Observable)new ObservableSkipLast(this, count));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("io.reactivex:trampoline")
/*       */   public final Observable<T> skipLast(long time, TimeUnit unit) {
/* 11804 */     return skipLast(time, unit, Schedulers.trampoline(), false, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("io.reactivex:trampoline")
/*       */   public final Observable<T> skipLast(long time, TimeUnit unit, boolean delayError) {
/* 11834 */     return skipLast(time, unit, Schedulers.trampoline(), delayError, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<T> skipLast(long time, TimeUnit unit, Scheduler scheduler) {
/* 11862 */     return skipLast(time, unit, scheduler, false, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<T> skipLast(long time, TimeUnit unit, Scheduler scheduler, boolean delayError) {
/* 11893 */     return skipLast(time, unit, scheduler, delayError, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<T> skipLast(long time, TimeUnit unit, Scheduler scheduler, boolean delayError, int bufferSize) {
/* 11926 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 11927 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 11928 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/*       */     
/* 11930 */     int s = bufferSize << 1;
/* 11931 */     return RxJavaPlugins.onAssembly((Observable)new ObservableSkipLastTimed(this, time, unit, scheduler, s, delayError));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <U> Observable<T> skipUntil(ObservableSource<U> other) {
/* 11955 */     ObjectHelper.requireNonNull(other, "other is null");
/* 11956 */     return RxJavaPlugins.onAssembly((Observable)new ObservableSkipUntil(this, other));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> skipWhile(Predicate<? super T> predicate) {
/* 11978 */     ObjectHelper.requireNonNull(predicate, "predicate is null");
/* 11979 */     return RxJavaPlugins.onAssembly((Observable)new ObservableSkipWhile(this, predicate));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> sorted() {
/* 12005 */     return toList().toObservable().map(Functions.listSorter(Functions.naturalComparator())).flatMapIterable(Functions.identity());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> sorted(Comparator<? super T> sortFunction) {
/* 12028 */     ObjectHelper.requireNonNull(sortFunction, "sortFunction is null");
/* 12029 */     return toList().toObservable().map(Functions.listSorter(sortFunction)).flatMapIterable(Functions.identity());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> startWith(Iterable<? extends T> items) {
/* 12052 */     return concatArray((ObservableSource<? extends T>[])new ObservableSource[] { fromIterable(items), this });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> startWith(ObservableSource<? extends T> other) {
/* 12075 */     ObjectHelper.requireNonNull(other, "other is null");
/* 12076 */     return concatArray((ObservableSource<? extends T>[])new ObservableSource[] { other, this });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> startWith(T item) {
/* 12099 */     ObjectHelper.requireNonNull(item, "item is null");
/* 12100 */     return concatArray((ObservableSource<? extends T>[])new ObservableSource[] { just(item), this });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> startWithArray(T... items) {
/* 12123 */     Observable<T> fromArray = fromArray(items);
/* 12124 */     if (fromArray == empty()) {
/* 12125 */       return RxJavaPlugins.onAssembly(this);
/*       */     }
/* 12127 */     return concatArray((ObservableSource<? extends T>[])new ObservableSource[] { fromArray, this });
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Disposable subscribe() {
/* 12147 */     return subscribe(Functions.emptyConsumer(), Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION, Functions.emptyConsumer());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Disposable subscribe(Consumer<? super T> onNext) {
/* 12172 */     return subscribe(onNext, Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION, Functions.emptyConsumer());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError) {
/* 12198 */     return subscribe(onNext, onError, Functions.EMPTY_ACTION, Functions.emptyConsumer());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete) {
/* 12229 */     return subscribe(onNext, onError, onComplete, Functions.emptyConsumer());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete, Consumer<? super Disposable> onSubscribe) {
/* 12263 */     ObjectHelper.requireNonNull(onNext, "onNext is null");
/* 12264 */     ObjectHelper.requireNonNull(onError, "onError is null");
/* 12265 */     ObjectHelper.requireNonNull(onComplete, "onComplete is null");
/* 12266 */     ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");
/*       */     
/* 12268 */     LambdaObserver<T> ls = new LambdaObserver(onNext, onError, onComplete, onSubscribe);
/*       */     
/* 12270 */     subscribe((Observer<? super T>)ls);
/*       */     
/* 12272 */     return (Disposable)ls;
/*       */   }
/*       */ 
/*       */   
/*       */   @SchedulerSupport("none")
/*       */   public final void subscribe(Observer<? super T> observer) {
/* 12278 */     ObjectHelper.requireNonNull(observer, "observer is null");
/*       */     try {
/* 12280 */       observer = RxJavaPlugins.onSubscribe(this, observer);
/*       */       
/* 12282 */       ObjectHelper.requireNonNull(observer, "The RxJavaPlugins.onSubscribe hook returned a null Observer. Please change the handler provided to RxJavaPlugins.setOnObservableSubscribe for invalid null returns. Further reading: https://github.com/ReactiveX/RxJava/wiki/Plugins");
/*       */       
/* 12284 */       subscribeActual(observer);
/* 12285 */     } catch (NullPointerException e) {
/* 12286 */       throw e;
/* 12287 */     } catch (Throwable e) {
/* 12288 */       Exceptions.throwIfFatal(e);
/*       */ 
/*       */       
/* 12291 */       RxJavaPlugins.onError(e);
/*       */       
/* 12293 */       NullPointerException npe = new NullPointerException("Actually not, but can't throw other exceptions due to RS");
/* 12294 */       npe.initCause(e);
/* 12295 */       throw npe;
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
/*       */   protected abstract void subscribeActual(Observer<? super T> paramObserver);
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <E extends Observer<? super T>> E subscribeWith(E observer) {
/* 12336 */     subscribe((Observer<? super T>)observer);
/* 12337 */     return observer;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<T> subscribeOn(Scheduler scheduler) {
/* 12360 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 12361 */     return RxJavaPlugins.onAssembly((Observable)new ObservableSubscribeOn(this, scheduler));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> switchIfEmpty(ObservableSource<? extends T> other) {
/* 12383 */     ObjectHelper.requireNonNull(other, "other is null");
/* 12384 */     return RxJavaPlugins.onAssembly((Observable)new ObservableSwitchIfEmpty(this, other));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> switchMap(Function<? super T, ? extends ObservableSource<? extends R>> mapper) {
/* 12412 */     return switchMap(mapper, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> switchMap(Function<? super T, ? extends ObservableSource<? extends R>> mapper, int bufferSize) {
/* 12442 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 12443 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 12444 */     if (this instanceof ScalarCallable) {
/*       */       
/* 12446 */       T v = (T)((ScalarCallable)this).call();
/* 12447 */       if (v == null) {
/* 12448 */         return empty();
/*       */       }
/* 12450 */       return ObservableScalarXMap.scalarXMap(v, mapper);
/*       */     } 
/* 12452 */     return RxJavaPlugins.onAssembly((Observable)new ObservableSwitchMap(this, mapper, bufferSize, false));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Completable switchMapCompletable(@NonNull Function<? super T, ? extends CompletableSource> mapper) {
/* 12492 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 12493 */     return RxJavaPlugins.onAssembly((Completable)new ObservableSwitchMapCompletable(this, mapper, false));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Completable switchMapCompletableDelayError(@NonNull Function<? super T, ? extends CompletableSource> mapper) {
/* 12534 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 12535 */     return RxJavaPlugins.onAssembly((Completable)new ObservableSwitchMapCompletable(this, mapper, true));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> switchMapMaybe(@NonNull Function<? super T, ? extends MaybeSource<? extends R>> mapper) {
/* 12570 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 12571 */     return RxJavaPlugins.onAssembly((Observable)new ObservableSwitchMapMaybe(this, mapper, false));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> switchMapMaybeDelayError(@NonNull Function<? super T, ? extends MaybeSource<? extends R>> mapper) {
/* 12596 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 12597 */     return RxJavaPlugins.onAssembly((Observable)new ObservableSwitchMapMaybe(this, mapper, true));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public final <R> Observable<R> switchMapSingle(@NonNull Function<? super T, ? extends SingleSource<? extends R>> mapper) {
/* 12627 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 12628 */     return RxJavaPlugins.onAssembly((Observable)new ObservableSwitchMapSingle(this, mapper, false));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @NonNull
/*       */   public final <R> Observable<R> switchMapSingleDelayError(@NonNull Function<? super T, ? extends SingleSource<? extends R>> mapper) {
/* 12659 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 12660 */     return RxJavaPlugins.onAssembly((Observable)new ObservableSwitchMapSingle(this, mapper, true));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> switchMapDelayError(Function<? super T, ? extends ObservableSource<? extends R>> mapper) {
/* 12690 */     return switchMapDelayError(mapper, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> switchMapDelayError(Function<? super T, ? extends ObservableSource<? extends R>> mapper, int bufferSize) {
/* 12722 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 12723 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 12724 */     if (this instanceof ScalarCallable) {
/*       */       
/* 12726 */       T v = (T)((ScalarCallable)this).call();
/* 12727 */       if (v == null) {
/* 12728 */         return empty();
/*       */       }
/* 12730 */       return ObservableScalarXMap.scalarXMap(v, mapper);
/*       */     } 
/* 12732 */     return RxJavaPlugins.onAssembly((Observable)new ObservableSwitchMap(this, mapper, bufferSize, true));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> take(long count) {
/* 12758 */     if (count < 0L) {
/* 12759 */       throw new IllegalArgumentException("count >= 0 required but it was " + count);
/*       */     }
/* 12761 */     return RxJavaPlugins.onAssembly((Observable)new ObservableTake(this, count));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> take(long time, TimeUnit unit) {
/* 12787 */     return takeUntil(timer(time, unit));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<T> take(long time, TimeUnit unit, Scheduler scheduler) {
/* 12816 */     return takeUntil(timer(time, unit, scheduler));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> takeLast(int count) {
/* 12840 */     if (count < 0) {
/* 12841 */       throw new IndexOutOfBoundsException("count >= 0 required but it was " + count);
/*       */     }
/* 12843 */     if (count == 0) {
/* 12844 */       return RxJavaPlugins.onAssembly((Observable)new ObservableIgnoreElements(this));
/*       */     }
/* 12846 */     if (count == 1) {
/* 12847 */       return RxJavaPlugins.onAssembly((Observable)new ObservableTakeLastOne(this));
/*       */     }
/* 12849 */     return RxJavaPlugins.onAssembly((Observable)new ObservableTakeLast(this, count));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("io.reactivex:trampoline")
/*       */   public final Observable<T> takeLast(long count, long time, TimeUnit unit) {
/* 12876 */     return takeLast(count, time, unit, Schedulers.trampoline(), false, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<T> takeLast(long count, long time, TimeUnit unit, Scheduler scheduler) {
/* 12908 */     return takeLast(count, time, unit, scheduler, false, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<T> takeLast(long count, long time, TimeUnit unit, Scheduler scheduler, boolean delayError, int bufferSize) {
/* 12945 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 12946 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 12947 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 12948 */     if (count < 0L) {
/* 12949 */       throw new IndexOutOfBoundsException("count >= 0 required but it was " + count);
/*       */     }
/* 12951 */     return RxJavaPlugins.onAssembly((Observable)new ObservableTakeLastTimed(this, count, time, unit, scheduler, bufferSize, delayError));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("io.reactivex:trampoline")
/*       */   public final Observable<T> takeLast(long time, TimeUnit unit) {
/* 12975 */     return takeLast(time, unit, Schedulers.trampoline(), false, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("io.reactivex:trampoline")
/*       */   public final Observable<T> takeLast(long time, TimeUnit unit, boolean delayError) {
/* 13002 */     return takeLast(time, unit, Schedulers.trampoline(), delayError, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<T> takeLast(long time, TimeUnit unit, Scheduler scheduler) {
/* 13030 */     return takeLast(time, unit, scheduler, false, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<T> takeLast(long time, TimeUnit unit, Scheduler scheduler, boolean delayError) {
/* 13061 */     return takeLast(time, unit, scheduler, delayError, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<T> takeLast(long time, TimeUnit unit, Scheduler scheduler, boolean delayError, int bufferSize) {
/* 13094 */     return takeLast(Long.MAX_VALUE, time, unit, scheduler, delayError, bufferSize);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <U> Observable<T> takeUntil(ObservableSource<U> other) {
/* 13118 */     ObjectHelper.requireNonNull(other, "other is null");
/* 13119 */     return RxJavaPlugins.onAssembly((Observable)new ObservableTakeUntil(this, other));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> takeUntil(Predicate<? super T> stopPredicate) {
/* 13147 */     ObjectHelper.requireNonNull(stopPredicate, "stopPredicate is null");
/* 13148 */     return RxJavaPlugins.onAssembly((Observable)new ObservableTakeUntilPredicate(this, stopPredicate));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<T> takeWhile(Predicate<? super T> predicate) {
/* 13171 */     ObjectHelper.requireNonNull(predicate, "predicate is null");
/* 13172 */     return RxJavaPlugins.onAssembly((Observable)new ObservableTakeWhile(this, predicate));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Observable<T> throttleFirst(long windowDuration, TimeUnit unit) {
/* 13198 */     return throttleFirst(windowDuration, unit, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<T> throttleFirst(long skipDuration, TimeUnit unit, Scheduler scheduler) {
/* 13227 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 13228 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 13229 */     return RxJavaPlugins.onAssembly((Observable)new ObservableThrottleFirstTimed(this, skipDuration, unit, scheduler));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Observable<T> throttleLast(long intervalDuration, TimeUnit unit) {
/* 13257 */     return sample(intervalDuration, unit);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<T> throttleLast(long intervalDuration, TimeUnit unit, Scheduler scheduler) {
/* 13288 */     return sample(intervalDuration, unit, scheduler);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Observable<T> throttleLatest(long timeout, TimeUnit unit) {
/* 13319 */     return throttleLatest(timeout, unit, Schedulers.computation(), false);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Observable<T> throttleLatest(long timeout, TimeUnit unit, boolean emitLast) {
/* 13350 */     return throttleLatest(timeout, unit, Schedulers.computation(), emitLast);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<T> throttleLatest(long timeout, TimeUnit unit, Scheduler scheduler) {
/* 13382 */     return throttleLatest(timeout, unit, scheduler, false);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<T> throttleLatest(long timeout, TimeUnit unit, Scheduler scheduler, boolean emitLast) {
/* 13414 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 13415 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 13416 */     return RxJavaPlugins.onAssembly((Observable)new ObservableThrottleLatest(this, timeout, unit, scheduler, emitLast));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Observable<T> throttleWithTimeout(long timeout, TimeUnit unit) {
/* 13447 */     return debounce(timeout, unit);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<T> throttleWithTimeout(long timeout, TimeUnit unit, Scheduler scheduler) {
/* 13481 */     return debounce(timeout, unit, scheduler);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<Timed<T>> timeInterval() {
/* 13501 */     return timeInterval(TimeUnit.MILLISECONDS, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<Timed<T>> timeInterval(Scheduler scheduler) {
/* 13523 */     return timeInterval(TimeUnit.MILLISECONDS, scheduler);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<Timed<T>> timeInterval(TimeUnit unit) {
/* 13544 */     return timeInterval(unit, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<Timed<T>> timeInterval(TimeUnit unit, Scheduler scheduler) {
/* 13567 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 13568 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 13569 */     return RxJavaPlugins.onAssembly((Observable)new ObservableTimeInterval(this, unit, scheduler));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <V> Observable<T> timeout(Function<? super T, ? extends ObservableSource<V>> itemTimeoutIndicator) {
/* 13599 */     return timeout0(null, itemTimeoutIndicator, null);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <V> Observable<T> timeout(Function<? super T, ? extends ObservableSource<V>> itemTimeoutIndicator, ObservableSource<? extends T> other) {
/* 13632 */     ObjectHelper.requireNonNull(other, "other is null");
/* 13633 */     return timeout0(null, itemTimeoutIndicator, other);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Observable<T> timeout(long timeout, TimeUnit timeUnit) {
/* 13658 */     return timeout0(timeout, timeUnit, null, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Observable<T> timeout(long timeout, TimeUnit timeUnit, ObservableSource<? extends T> other) {
/* 13685 */     ObjectHelper.requireNonNull(other, "other is null");
/* 13686 */     return timeout0(timeout, timeUnit, other, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<T> timeout(long timeout, TimeUnit timeUnit, Scheduler scheduler, ObservableSource<? extends T> other) {
/* 13716 */     ObjectHelper.requireNonNull(other, "other is null");
/* 13717 */     return timeout0(timeout, timeUnit, other, scheduler);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<T> timeout(long timeout, TimeUnit timeUnit, Scheduler scheduler) {
/* 13745 */     return timeout0(timeout, timeUnit, null, scheduler);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <U, V> Observable<T> timeout(ObservableSource<U> firstTimeoutIndicator, Function<? super T, ? extends ObservableSource<V>> itemTimeoutIndicator) {
/* 13779 */     ObjectHelper.requireNonNull(firstTimeoutIndicator, "firstTimeoutIndicator is null");
/* 13780 */     return timeout0(firstTimeoutIndicator, itemTimeoutIndicator, null);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <U, V> Observable<T> timeout(ObservableSource<U> firstTimeoutIndicator, Function<? super T, ? extends ObservableSource<V>> itemTimeoutIndicator, ObservableSource<? extends T> other) {
/* 13821 */     ObjectHelper.requireNonNull(firstTimeoutIndicator, "firstTimeoutIndicator is null");
/* 13822 */     ObjectHelper.requireNonNull(other, "other is null");
/* 13823 */     return timeout0(firstTimeoutIndicator, itemTimeoutIndicator, other);
/*       */   }
/*       */ 
/*       */   
/*       */   private Observable<T> timeout0(long timeout, TimeUnit timeUnit, ObservableSource<? extends T> other, Scheduler scheduler) {
/* 13828 */     ObjectHelper.requireNonNull(timeUnit, "timeUnit is null");
/* 13829 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 13830 */     return RxJavaPlugins.onAssembly((Observable)new ObservableTimeoutTimed(this, timeout, timeUnit, scheduler, other));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   private <U, V> Observable<T> timeout0(ObservableSource<U> firstTimeoutIndicator, Function<? super T, ? extends ObservableSource<V>> itemTimeoutIndicator, ObservableSource<? extends T> other) {
/* 13837 */     ObjectHelper.requireNonNull(itemTimeoutIndicator, "itemTimeoutIndicator is null");
/* 13838 */     return RxJavaPlugins.onAssembly((Observable)new ObservableTimeout(this, firstTimeoutIndicator, itemTimeoutIndicator, other));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<Timed<T>> timestamp() {
/* 13858 */     return timestamp(TimeUnit.MILLISECONDS, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<Timed<T>> timestamp(Scheduler scheduler) {
/* 13881 */     return timestamp(TimeUnit.MILLISECONDS, scheduler);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<Timed<T>> timestamp(TimeUnit unit) {
/* 13902 */     return timestamp(unit, Schedulers.computation());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<Timed<T>> timestamp(TimeUnit unit, Scheduler scheduler) {
/* 13926 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 13927 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 13928 */     return map(Functions.timestampWith(unit, scheduler));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> R to(Function<? super Observable<T>, R> converter) {
/*       */     try {
/* 13947 */       return (R)((Function)ObjectHelper.requireNonNull(converter, "converter is null")).apply(this);
/* 13948 */     } catch (Throwable ex) {
/* 13949 */       Exceptions.throwIfFatal(ex);
/* 13950 */       throw ExceptionHelper.wrapOrThrow(ex);
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
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("none")
/*       */   public final Single<List<T>> toList() {
/* 13981 */     return toList(16);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Single<List<T>> toList(int capacityHint) {
/* 14013 */     ObjectHelper.verifyPositive(capacityHint, "capacityHint");
/* 14014 */     return RxJavaPlugins.onAssembly((Single)new ObservableToListSingle(this, capacityHint));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <U extends Collection<? super T>> Single<U> toList(Callable<U> collectionSupplier) {
/* 14047 */     ObjectHelper.requireNonNull(collectionSupplier, "collectionSupplier is null");
/* 14048 */     return RxJavaPlugins.onAssembly((Single)new ObservableToListSingle(this, collectionSupplier));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <K> Single<Map<K, T>> toMap(Function<? super T, ? extends K> keySelector) {
/* 14078 */     ObjectHelper.requireNonNull(keySelector, "keySelector is null");
/* 14079 */     return collect(HashMapSupplier.asCallable(), Functions.toMapKeySelector(keySelector));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <K, V> Single<Map<K, V>> toMap(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector) {
/* 14114 */     ObjectHelper.requireNonNull(keySelector, "keySelector is null");
/* 14115 */     ObjectHelper.requireNonNull(valueSelector, "valueSelector is null");
/* 14116 */     return collect(HashMapSupplier.asCallable(), Functions.toMapKeyValueSelector(keySelector, valueSelector));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <K, V> Single<Map<K, V>> toMap(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector, Callable<? extends Map<K, V>> mapSupplier) {
/* 14151 */     ObjectHelper.requireNonNull(keySelector, "keySelector is null");
/* 14152 */     ObjectHelper.requireNonNull(valueSelector, "valueSelector is null");
/* 14153 */     ObjectHelper.requireNonNull(mapSupplier, "mapSupplier is null");
/* 14154 */     return collect(mapSupplier, Functions.toMapKeyValueSelector(keySelector, valueSelector));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <K> Single<Map<K, Collection<T>>> toMultimap(Function<? super T, ? extends K> keySelector) {
/* 14182 */     Function<? super T, ? extends T> valueSelector = Functions.identity();
/* 14183 */     Callable<Map<K, Collection<T>>> mapSupplier = HashMapSupplier.asCallable();
/* 14184 */     Function<K, List<T>> collectionFactory = ArrayListSupplier.asFunction();
/* 14185 */     return toMultimap(keySelector, valueSelector, mapSupplier, (Function)collectionFactory);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <K, V> Single<Map<K, Collection<V>>> toMultimap(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector) {
/* 14216 */     Callable<Map<K, Collection<V>>> mapSupplier = HashMapSupplier.asCallable();
/* 14217 */     Function<K, List<V>> collectionFactory = ArrayListSupplier.asFunction();
/* 14218 */     return toMultimap(keySelector, valueSelector, mapSupplier, (Function)collectionFactory);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <K, V> Single<Map<K, Collection<V>>> toMultimap(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector, Callable<? extends Map<K, Collection<V>>> mapSupplier, Function<? super K, ? extends Collection<? super V>> collectionFactory) {
/* 14253 */     ObjectHelper.requireNonNull(keySelector, "keySelector is null");
/* 14254 */     ObjectHelper.requireNonNull(valueSelector, "valueSelector is null");
/* 14255 */     ObjectHelper.requireNonNull(mapSupplier, "mapSupplier is null");
/* 14256 */     ObjectHelper.requireNonNull(collectionFactory, "collectionFactory is null");
/* 14257 */     return collect(mapSupplier, Functions.toMultimapKeyValueSelector(keySelector, valueSelector, collectionFactory));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <K, V> Single<Map<K, Collection<V>>> toMultimap(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector, Callable<Map<K, Collection<V>>> mapSupplier) {
/* 14294 */     return toMultimap(keySelector, valueSelector, mapSupplier, ArrayListSupplier.asFunction());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("none")
/*       */   public final Flowable<T> toFlowable(BackpressureStrategy strategy) {
/* 14337 */     FlowableFromObservable<T> flowableFromObservable = new FlowableFromObservable(this);
/*       */     
/* 14339 */     switch (strategy) {
/*       */       case DROP:
/* 14341 */         return flowableFromObservable.onBackpressureDrop();
/*       */       case LATEST:
/* 14343 */         return flowableFromObservable.onBackpressureLatest();
/*       */       case MISSING:
/* 14345 */         return (Flowable<T>)flowableFromObservable;
/*       */       case ERROR:
/* 14347 */         return RxJavaPlugins.onAssembly((Flowable)new FlowableOnBackpressureError((Flowable)flowableFromObservable));
/*       */     } 
/* 14349 */     return flowableFromObservable.onBackpressureBuffer();
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Single<List<T>> toSortedList() {
/* 14378 */     return toSortedList(Functions.naturalOrder());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Single<List<T>> toSortedList(Comparator<? super T> comparator) {
/* 14405 */     ObjectHelper.requireNonNull(comparator, "comparator is null");
/* 14406 */     return toList().map(Functions.listSorter(comparator));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Single<List<T>> toSortedList(Comparator<? super T> comparator, int capacityHint) {
/* 14436 */     ObjectHelper.requireNonNull(comparator, "comparator is null");
/* 14437 */     return toList(capacityHint).map(Functions.listSorter(comparator));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Single<List<T>> toSortedList(int capacityHint) {
/* 14469 */     return toSortedList(Functions.naturalOrder(), capacityHint);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<T> unsubscribeOn(Scheduler scheduler) {
/* 14491 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 14492 */     return RxJavaPlugins.onAssembly((Observable)new ObservableUnsubscribeOn(this, scheduler));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<Observable<T>> window(long count) {
/* 14517 */     return window(count, count, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<Observable<T>> window(long count, long skip) {
/* 14545 */     return window(count, skip, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final Observable<Observable<T>> window(long count, long skip, int bufferSize) {
/* 14575 */     ObjectHelper.verifyPositive(count, "count");
/* 14576 */     ObjectHelper.verifyPositive(skip, "skip");
/* 14577 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 14578 */     return RxJavaPlugins.onAssembly((Observable)new ObservableWindow(this, count, skip, bufferSize));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Observable<Observable<T>> window(long timespan, long timeskip, TimeUnit unit) {
/* 14606 */     return window(timespan, timeskip, unit, Schedulers.computation(), bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<Observable<T>> window(long timespan, long timeskip, TimeUnit unit, Scheduler scheduler) {
/* 14636 */     return window(timespan, timeskip, unit, scheduler, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<Observable<T>> window(long timespan, long timeskip, TimeUnit unit, Scheduler scheduler, int bufferSize) {
/* 14668 */     ObjectHelper.verifyPositive(timespan, "timespan");
/* 14669 */     ObjectHelper.verifyPositive(timeskip, "timeskip");
/* 14670 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 14671 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 14672 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 14673 */     return RxJavaPlugins.onAssembly((Observable)new ObservableWindowTimed(this, timespan, timeskip, unit, scheduler, Long.MAX_VALUE, bufferSize, false));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Observable<Observable<T>> window(long timespan, TimeUnit unit) {
/* 14700 */     return window(timespan, unit, Schedulers.computation(), Long.MAX_VALUE, false);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Observable<Observable<T>> window(long timespan, TimeUnit unit, long count) {
/* 14732 */     return window(timespan, unit, Schedulers.computation(), count, false);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("io.reactivex:computation")
/*       */   public final Observable<Observable<T>> window(long timespan, TimeUnit unit, long count, boolean restart) {
/* 14766 */     return window(timespan, unit, Schedulers.computation(), count, restart);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<Observable<T>> window(long timespan, TimeUnit unit, Scheduler scheduler) {
/* 14796 */     return window(timespan, unit, scheduler, Long.MAX_VALUE, false);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<Observable<T>> window(long timespan, TimeUnit unit, Scheduler scheduler, long count) {
/* 14830 */     return window(timespan, unit, scheduler, count, false);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<Observable<T>> window(long timespan, TimeUnit unit, Scheduler scheduler, long count, boolean restart) {
/* 14866 */     return window(timespan, unit, scheduler, count, restart, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */   
/*       */   @CheckReturnValue
/*       */   @SchedulerSupport("custom")
/*       */   public final Observable<Observable<T>> window(long timespan, TimeUnit unit, Scheduler scheduler, long count, boolean restart, int bufferSize) {
/* 14905 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 14906 */     ObjectHelper.requireNonNull(scheduler, "scheduler is null");
/* 14907 */     ObjectHelper.requireNonNull(unit, "unit is null");
/* 14908 */     ObjectHelper.verifyPositive(count, "count");
/* 14909 */     return RxJavaPlugins.onAssembly((Observable)new ObservableWindowTimed(this, timespan, timespan, unit, scheduler, count, bufferSize, restart));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <B> Observable<Observable<T>> window(ObservableSource<B> boundary) {
/* 14935 */     return window(boundary, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <B> Observable<Observable<T>> window(ObservableSource<B> boundary, int bufferSize) {
/* 14963 */     ObjectHelper.requireNonNull(boundary, "boundary is null");
/* 14964 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 14965 */     return RxJavaPlugins.onAssembly((Observable)new ObservableWindowBoundary(this, boundary, bufferSize));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <U, V> Observable<Observable<T>> window(ObservableSource<U> openingIndicator, Function<? super U, ? extends ObservableSource<V>> closingIndicator) {
/* 14996 */     return window(openingIndicator, closingIndicator, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <U, V> Observable<Observable<T>> window(ObservableSource<U> openingIndicator, Function<? super U, ? extends ObservableSource<V>> closingIndicator, int bufferSize) {
/* 15029 */     ObjectHelper.requireNonNull(openingIndicator, "openingIndicator is null");
/* 15030 */     ObjectHelper.requireNonNull(closingIndicator, "closingIndicator is null");
/* 15031 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 15032 */     return RxJavaPlugins.onAssembly((Observable)new ObservableWindowBoundarySelector(this, openingIndicator, closingIndicator, bufferSize));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <B> Observable<Observable<T>> window(Callable<? extends ObservableSource<B>> boundary) {
/* 15058 */     return window(boundary, bufferSize());
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <B> Observable<Observable<T>> window(Callable<? extends ObservableSource<B>> boundary, int bufferSize) {
/* 15086 */     ObjectHelper.requireNonNull(boundary, "boundary is null");
/* 15087 */     ObjectHelper.verifyPositive(bufferSize, "bufferSize");
/* 15088 */     return RxJavaPlugins.onAssembly((Observable)new ObservableWindowBoundarySupplier(this, boundary, bufferSize));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <U, R> Observable<R> withLatestFrom(ObservableSource<? extends U> other, BiFunction<? super T, ? super U, ? extends R> combiner) {
/* 15118 */     ObjectHelper.requireNonNull(other, "other is null");
/* 15119 */     ObjectHelper.requireNonNull(combiner, "combiner is null");
/*       */     
/* 15121 */     return RxJavaPlugins.onAssembly((Observable)new ObservableWithLatestFrom(this, combiner, other));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <T1, T2, R> Observable<R> withLatestFrom(ObservableSource<T1> o1, ObservableSource<T2> o2, Function3<? super T, ? super T1, ? super T2, R> combiner) {
/* 15153 */     ObjectHelper.requireNonNull(o1, "o1 is null");
/* 15154 */     ObjectHelper.requireNonNull(o2, "o2 is null");
/* 15155 */     ObjectHelper.requireNonNull(combiner, "combiner is null");
/* 15156 */     Function<Object[], R> f = Functions.toFunction(combiner);
/* 15157 */     return withLatestFrom((ObservableSource<?>[])new ObservableSource[] { o1, o2 }, f);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <T1, T2, T3, R> Observable<R> withLatestFrom(ObservableSource<T1> o1, ObservableSource<T2> o2, ObservableSource<T3> o3, Function4<? super T, ? super T1, ? super T2, ? super T3, R> combiner) {
/* 15192 */     ObjectHelper.requireNonNull(o1, "o1 is null");
/* 15193 */     ObjectHelper.requireNonNull(o2, "o2 is null");
/* 15194 */     ObjectHelper.requireNonNull(o3, "o3 is null");
/* 15195 */     ObjectHelper.requireNonNull(combiner, "combiner is null");
/* 15196 */     Function<Object[], R> f = Functions.toFunction(combiner);
/* 15197 */     return withLatestFrom((ObservableSource<?>[])new ObservableSource[] { o1, o2, o3 }, f);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <T1, T2, T3, T4, R> Observable<R> withLatestFrom(ObservableSource<T1> o1, ObservableSource<T2> o2, ObservableSource<T3> o3, ObservableSource<T4> o4, Function5<? super T, ? super T1, ? super T2, ? super T3, ? super T4, R> combiner) {
/* 15234 */     ObjectHelper.requireNonNull(o1, "o1 is null");
/* 15235 */     ObjectHelper.requireNonNull(o2, "o2 is null");
/* 15236 */     ObjectHelper.requireNonNull(o3, "o3 is null");
/* 15237 */     ObjectHelper.requireNonNull(o4, "o4 is null");
/* 15238 */     ObjectHelper.requireNonNull(combiner, "combiner is null");
/* 15239 */     Function<Object[], R> f = Functions.toFunction(combiner);
/* 15240 */     return withLatestFrom((ObservableSource<?>[])new ObservableSource[] { o1, o2, o3, o4 }, f);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> withLatestFrom(ObservableSource<?>[] others, Function<? super Object[], R> combiner) {
/* 15267 */     ObjectHelper.requireNonNull(others, "others is null");
/* 15268 */     ObjectHelper.requireNonNull(combiner, "combiner is null");
/* 15269 */     return RxJavaPlugins.onAssembly((Observable)new ObservableWithLatestFromMany(this, (ObservableSource[])others, combiner));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <R> Observable<R> withLatestFrom(Iterable<? extends ObservableSource<?>> others, Function<? super Object[], R> combiner) {
/* 15296 */     ObjectHelper.requireNonNull(others, "others is null");
/* 15297 */     ObjectHelper.requireNonNull(combiner, "combiner is null");
/* 15298 */     return RxJavaPlugins.onAssembly((Observable)new ObservableWithLatestFromMany(this, others, combiner));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <U, R> Observable<R> zipWith(Iterable<U> other, BiFunction<? super T, ? super U, ? extends R> zipper) {
/* 15330 */     ObjectHelper.requireNonNull(other, "other is null");
/* 15331 */     ObjectHelper.requireNonNull(zipper, "zipper is null");
/* 15332 */     return RxJavaPlugins.onAssembly((Observable)new ObservableZipIterable(this, other, zipper));
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <U, R> Observable<R> zipWith(ObservableSource<? extends U> other, BiFunction<? super T, ? super U, ? extends R> zipper) {
/* 15374 */     ObjectHelper.requireNonNull(other, "other is null");
/* 15375 */     return zip(this, other, zipper);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <U, R> Observable<R> zipWith(ObservableSource<? extends U> other, BiFunction<? super T, ? super U, ? extends R> zipper, boolean delayError) {
/* 15420 */     return zip(this, other, zipper, delayError);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final <U, R> Observable<R> zipWith(ObservableSource<? extends U> other, BiFunction<? super T, ? super U, ? extends R> zipper, boolean delayError, int bufferSize) {
/* 15467 */     return zip(this, other, zipper, delayError, bufferSize);
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final TestObserver<T> test() {
/* 15486 */     TestObserver<T> to = new TestObserver();
/* 15487 */     subscribe((Observer<? super T>)to);
/* 15488 */     return to;
/*       */   }
/*       */ 
/*       */ 
/*       */ 
/*       */ 
/*       */ 
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
/*       */   public final TestObserver<T> test(boolean dispose) {
/* 15506 */     TestObserver<T> to = new TestObserver();
/* 15507 */     if (dispose) {
/* 15508 */       to.dispose();
/*       */     }
/* 15510 */     subscribe((Observer<? super T>)to);
/* 15511 */     return to;
/*       */   }
/*       */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/Observable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */