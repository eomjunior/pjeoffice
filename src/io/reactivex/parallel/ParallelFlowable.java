/*     */ package io.reactivex.parallel;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.annotations.BackpressureKind;
/*     */ import io.reactivex.annotations.BackpressureSupport;
/*     */ import io.reactivex.annotations.CheckReturnValue;
/*     */ import io.reactivex.annotations.NonNull;
/*     */ import io.reactivex.annotations.SchedulerSupport;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Action;
/*     */ import io.reactivex.functions.BiConsumer;
/*     */ import io.reactivex.functions.BiFunction;
/*     */ import io.reactivex.functions.Consumer;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.functions.LongConsumer;
/*     */ import io.reactivex.functions.Predicate;
/*     */ import io.reactivex.internal.functions.Functions;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.operators.parallel.ParallelCollect;
/*     */ import io.reactivex.internal.operators.parallel.ParallelConcatMap;
/*     */ import io.reactivex.internal.operators.parallel.ParallelDoOnNextTry;
/*     */ import io.reactivex.internal.operators.parallel.ParallelFilter;
/*     */ import io.reactivex.internal.operators.parallel.ParallelFilterTry;
/*     */ import io.reactivex.internal.operators.parallel.ParallelFlatMap;
/*     */ import io.reactivex.internal.operators.parallel.ParallelFromArray;
/*     */ import io.reactivex.internal.operators.parallel.ParallelFromPublisher;
/*     */ import io.reactivex.internal.operators.parallel.ParallelJoin;
/*     */ import io.reactivex.internal.operators.parallel.ParallelMap;
/*     */ import io.reactivex.internal.operators.parallel.ParallelMapTry;
/*     */ import io.reactivex.internal.operators.parallel.ParallelPeek;
/*     */ import io.reactivex.internal.operators.parallel.ParallelReduce;
/*     */ import io.reactivex.internal.operators.parallel.ParallelReduceFull;
/*     */ import io.reactivex.internal.operators.parallel.ParallelRunOn;
/*     */ import io.reactivex.internal.operators.parallel.ParallelSortedJoin;
/*     */ import io.reactivex.internal.subscriptions.EmptySubscription;
/*     */ import io.reactivex.internal.util.ErrorMode;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import io.reactivex.internal.util.ListAddBiConsumer;
/*     */ import io.reactivex.internal.util.MergerBiFunction;
/*     */ import io.reactivex.internal.util.SorterFunction;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Callable;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.reactivestreams.Subscriber;
/*     */ import org.reactivestreams.Subscription;
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
/*     */ public abstract class ParallelFlowable<T>
/*     */ {
/*     */   public abstract void subscribe(@NonNull Subscriber<? super T>[] paramArrayOfSubscriber);
/*     */   
/*     */   public abstract int parallelism();
/*     */   
/*     */   protected final boolean validate(@NonNull Subscriber<?>[] subscribers) {
/*  67 */     int p = parallelism();
/*  68 */     if (subscribers.length != p) {
/*  69 */       Throwable iae = new IllegalArgumentException("parallelism = " + p + ", subscribers = " + subscribers.length);
/*  70 */       for (Subscriber<?> s : subscribers) {
/*  71 */         EmptySubscription.error(iae, s);
/*     */       }
/*  73 */       return false;
/*     */     } 
/*  75 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   public static <T> ParallelFlowable<T> from(@NonNull Publisher<? extends T> source) {
/*  87 */     return from(source, Runtime.getRuntime().availableProcessors(), Flowable.bufferSize());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   public static <T> ParallelFlowable<T> from(@NonNull Publisher<? extends T> source, int parallelism) {
/*  99 */     return from(source, parallelism, Flowable.bufferSize());
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public static <T> ParallelFlowable<T> from(@NonNull Publisher<? extends T> source, int parallelism, int prefetch) {
/* 117 */     ObjectHelper.requireNonNull(source, "source");
/* 118 */     ObjectHelper.verifyPositive(parallelism, "parallelism");
/* 119 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/*     */     
/* 121 */     return RxJavaPlugins.onAssembly((ParallelFlowable)new ParallelFromPublisher(source, parallelism, prefetch));
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final <R> R as(@NonNull ParallelFlowableConverter<T, R> converter) {
/* 138 */     return ((ParallelFlowableConverter<T, R>)ObjectHelper.requireNonNull(converter, "converter is null")).apply(this);
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final <R> ParallelFlowable<R> map(@NonNull Function<? super T, ? extends R> mapper) {
/* 152 */     ObjectHelper.requireNonNull(mapper, "mapper");
/* 153 */     return RxJavaPlugins.onAssembly((ParallelFlowable)new ParallelMap(this, mapper));
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final <R> ParallelFlowable<R> map(@NonNull Function<? super T, ? extends R> mapper, @NonNull ParallelFailureHandling errorHandler) {
/* 172 */     ObjectHelper.requireNonNull(mapper, "mapper");
/* 173 */     ObjectHelper.requireNonNull(errorHandler, "errorHandler is null");
/* 174 */     return RxJavaPlugins.onAssembly((ParallelFlowable)new ParallelMapTry(this, mapper, errorHandler));
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final <R> ParallelFlowable<R> map(@NonNull Function<? super T, ? extends R> mapper, @NonNull BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler) {
/* 194 */     ObjectHelper.requireNonNull(mapper, "mapper");
/* 195 */     ObjectHelper.requireNonNull(errorHandler, "errorHandler is null");
/* 196 */     return RxJavaPlugins.onAssembly((ParallelFlowable)new ParallelMapTry(this, mapper, errorHandler));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   public final ParallelFlowable<T> filter(@NonNull Predicate<? super T> predicate) {
/* 208 */     ObjectHelper.requireNonNull(predicate, "predicate");
/* 209 */     return RxJavaPlugins.onAssembly((ParallelFlowable)new ParallelFilter(this, predicate));
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
/*     */   @CheckReturnValue
/*     */   public final ParallelFlowable<T> filter(@NonNull Predicate<? super T> predicate, @NonNull ParallelFailureHandling errorHandler) {
/* 226 */     ObjectHelper.requireNonNull(predicate, "predicate");
/* 227 */     ObjectHelper.requireNonNull(errorHandler, "errorHandler is null");
/* 228 */     return RxJavaPlugins.onAssembly((ParallelFlowable)new ParallelFilterTry(this, predicate, errorHandler));
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
/*     */   @CheckReturnValue
/*     */   public final ParallelFlowable<T> filter(@NonNull Predicate<? super T> predicate, @NonNull BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler) {
/* 246 */     ObjectHelper.requireNonNull(predicate, "predicate");
/* 247 */     ObjectHelper.requireNonNull(errorHandler, "errorHandler is null");
/* 248 */     return RxJavaPlugins.onAssembly((ParallelFlowable)new ParallelFilterTry(this, predicate, errorHandler));
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final ParallelFlowable<T> runOn(@NonNull Scheduler scheduler) {
/* 273 */     return runOn(scheduler, Flowable.bufferSize());
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final ParallelFlowable<T> runOn(@NonNull Scheduler scheduler, int prefetch) {
/* 300 */     ObjectHelper.requireNonNull(scheduler, "scheduler");
/* 301 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/* 302 */     return RxJavaPlugins.onAssembly((ParallelFlowable)new ParallelRunOn(this, scheduler, prefetch));
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final Flowable<T> reduce(@NonNull BiFunction<T, T, T> reducer) {
/* 316 */     ObjectHelper.requireNonNull(reducer, "reducer");
/* 317 */     return RxJavaPlugins.onAssembly((Flowable)new ParallelReduceFull(this, reducer));
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final <R> ParallelFlowable<R> reduce(@NonNull Callable<R> initialSupplier, @NonNull BiFunction<R, ? super T, R> reducer) {
/* 334 */     ObjectHelper.requireNonNull(initialSupplier, "initialSupplier");
/* 335 */     ObjectHelper.requireNonNull(reducer, "reducer");
/* 336 */     return RxJavaPlugins.onAssembly((ParallelFlowable)new ParallelReduce(this, initialSupplier, reducer));
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
/*     */   @BackpressureSupport(BackpressureKind.FULL)
/*     */   @SchedulerSupport("none")
/*     */   @CheckReturnValue
/*     */   public final Flowable<T> sequential() {
/* 360 */     return sequential(Flowable.bufferSize());
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
/*     */   @BackpressureSupport(BackpressureKind.FULL)
/*     */   @SchedulerSupport("none")
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final Flowable<T> sequential(int prefetch) {
/* 384 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/* 385 */     return RxJavaPlugins.onAssembly((Flowable)new ParallelJoin(this, prefetch, false));
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
/*     */   @BackpressureSupport(BackpressureKind.FULL)
/*     */   @SchedulerSupport("none")
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final Flowable<T> sequentialDelayError() {
/* 412 */     return sequentialDelayError(Flowable.bufferSize());
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
/*     */   @BackpressureSupport(BackpressureKind.FULL)
/*     */   @SchedulerSupport("none")
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final Flowable<T> sequentialDelayError(int prefetch) {
/* 438 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/* 439 */     return RxJavaPlugins.onAssembly((Flowable)new ParallelJoin(this, prefetch, true));
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final Flowable<T> sorted(@NonNull Comparator<? super T> comparator) {
/* 454 */     return sorted(comparator, 16);
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final Flowable<T> sorted(@NonNull Comparator<? super T> comparator, int capacityHint) {
/* 470 */     ObjectHelper.requireNonNull(comparator, "comparator is null");
/* 471 */     ObjectHelper.verifyPositive(capacityHint, "capacityHint");
/* 472 */     int ch = capacityHint / parallelism() + 1;
/* 473 */     ParallelFlowable<List<T>> railReduced = reduce(Functions.createArrayList(ch), ListAddBiConsumer.instance());
/* 474 */     ParallelFlowable<List<T>> railSorted = railReduced.map((Function<? super List<T>, ? extends List<T>>)new SorterFunction(comparator));
/*     */     
/* 476 */     return RxJavaPlugins.onAssembly((Flowable)new ParallelSortedJoin(railSorted, comparator));
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final Flowable<List<T>> toSortedList(@NonNull Comparator<? super T> comparator) {
/* 490 */     return toSortedList(comparator, 16);
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final Flowable<List<T>> toSortedList(@NonNull Comparator<? super T> comparator, int capacityHint) {
/* 504 */     ObjectHelper.requireNonNull(comparator, "comparator is null");
/* 505 */     ObjectHelper.verifyPositive(capacityHint, "capacityHint");
/*     */     
/* 507 */     int ch = capacityHint / parallelism() + 1;
/* 508 */     ParallelFlowable<List<T>> railReduced = reduce(Functions.createArrayList(ch), ListAddBiConsumer.instance());
/* 509 */     ParallelFlowable<List<T>> railSorted = railReduced.map((Function<? super List<T>, ? extends List<T>>)new SorterFunction(comparator));
/*     */     
/* 511 */     Flowable<List<T>> merged = railSorted.reduce((BiFunction<List<T>, List<T>, List<T>>)new MergerBiFunction(comparator));
/*     */     
/* 513 */     return RxJavaPlugins.onAssembly(merged);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final ParallelFlowable<T> doOnNext(@NonNull Consumer<? super T> onNext) {
/* 525 */     ObjectHelper.requireNonNull(onNext, "onNext is null");
/* 526 */     return RxJavaPlugins.onAssembly((ParallelFlowable)new ParallelPeek(this, onNext, 
/*     */           
/* 528 */           Functions.emptyConsumer(), 
/* 529 */           Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, 
/*     */ 
/*     */           
/* 532 */           Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION));
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final ParallelFlowable<T> doOnNext(@NonNull Consumer<? super T> onNext, @NonNull ParallelFailureHandling errorHandler) {
/* 551 */     ObjectHelper.requireNonNull(onNext, "onNext is null");
/* 552 */     ObjectHelper.requireNonNull(errorHandler, "errorHandler is null");
/* 553 */     return RxJavaPlugins.onAssembly((ParallelFlowable)new ParallelDoOnNextTry(this, onNext, errorHandler));
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final ParallelFlowable<T> doOnNext(@NonNull Consumer<? super T> onNext, @NonNull BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler) {
/* 570 */     ObjectHelper.requireNonNull(onNext, "onNext is null");
/* 571 */     ObjectHelper.requireNonNull(errorHandler, "errorHandler is null");
/* 572 */     return RxJavaPlugins.onAssembly((ParallelFlowable)new ParallelDoOnNextTry(this, onNext, errorHandler));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final ParallelFlowable<T> doAfterNext(@NonNull Consumer<? super T> onAfterNext) {
/* 585 */     ObjectHelper.requireNonNull(onAfterNext, "onAfterNext is null");
/* 586 */     return RxJavaPlugins.onAssembly((ParallelFlowable)new ParallelPeek(this, 
/* 587 */           Functions.emptyConsumer(), onAfterNext, 
/*     */           
/* 589 */           Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, 
/*     */ 
/*     */           
/* 592 */           Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION));
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final ParallelFlowable<T> doOnError(@NonNull Consumer<Throwable> onError) {
/* 607 */     ObjectHelper.requireNonNull(onError, "onError is null");
/* 608 */     return RxJavaPlugins.onAssembly((ParallelFlowable)new ParallelPeek(this, 
/* 609 */           Functions.emptyConsumer(), 
/* 610 */           Functions.emptyConsumer(), onError, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, 
/*     */ 
/*     */ 
/*     */           
/* 614 */           Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION));
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final ParallelFlowable<T> doOnComplete(@NonNull Action onComplete) {
/* 629 */     ObjectHelper.requireNonNull(onComplete, "onComplete is null");
/* 630 */     return RxJavaPlugins.onAssembly((ParallelFlowable)new ParallelPeek(this, 
/* 631 */           Functions.emptyConsumer(), 
/* 632 */           Functions.emptyConsumer(), 
/* 633 */           Functions.emptyConsumer(), onComplete, Functions.EMPTY_ACTION, 
/*     */ 
/*     */           
/* 636 */           Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION));
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final ParallelFlowable<T> doAfterTerminated(@NonNull Action onAfterTerminate) {
/* 651 */     ObjectHelper.requireNonNull(onAfterTerminate, "onAfterTerminate is null");
/* 652 */     return RxJavaPlugins.onAssembly((ParallelFlowable)new ParallelPeek(this, 
/* 653 */           Functions.emptyConsumer(), 
/* 654 */           Functions.emptyConsumer(), 
/* 655 */           Functions.emptyConsumer(), Functions.EMPTY_ACTION, onAfterTerminate, 
/*     */ 
/*     */           
/* 658 */           Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION));
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final ParallelFlowable<T> doOnSubscribe(@NonNull Consumer<? super Subscription> onSubscribe) {
/* 673 */     ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");
/* 674 */     return RxJavaPlugins.onAssembly((ParallelFlowable)new ParallelPeek(this, 
/* 675 */           Functions.emptyConsumer(), 
/* 676 */           Functions.emptyConsumer(), 
/* 677 */           Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, onSubscribe, Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION));
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final ParallelFlowable<T> doOnRequest(@NonNull LongConsumer onRequest) {
/* 695 */     ObjectHelper.requireNonNull(onRequest, "onRequest is null");
/* 696 */     return RxJavaPlugins.onAssembly((ParallelFlowable)new ParallelPeek(this, 
/* 697 */           Functions.emptyConsumer(), 
/* 698 */           Functions.emptyConsumer(), 
/* 699 */           Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, 
/*     */ 
/*     */           
/* 702 */           Functions.emptyConsumer(), onRequest, Functions.EMPTY_ACTION));
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final ParallelFlowable<T> doOnCancel(@NonNull Action onCancel) {
/* 717 */     ObjectHelper.requireNonNull(onCancel, "onCancel is null");
/* 718 */     return RxJavaPlugins.onAssembly((ParallelFlowable)new ParallelPeek(this, 
/* 719 */           Functions.emptyConsumer(), 
/* 720 */           Functions.emptyConsumer(), 
/* 721 */           Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, 
/*     */ 
/*     */           
/* 724 */           Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, onCancel));
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final <C> ParallelFlowable<C> collect(@NonNull Callable<? extends C> collectionSupplier, @NonNull BiConsumer<? super C, ? super T> collector) {
/* 742 */     ObjectHelper.requireNonNull(collectionSupplier, "collectionSupplier is null");
/* 743 */     ObjectHelper.requireNonNull(collector, "collector is null");
/* 744 */     return RxJavaPlugins.onAssembly((ParallelFlowable)new ParallelCollect(this, collectionSupplier, collector));
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public static <T> ParallelFlowable<T> fromArray(@NonNull Publisher<T>... publishers) {
/* 758 */     if (publishers.length == 0) {
/* 759 */       throw new IllegalArgumentException("Zero publishers not supported");
/*     */     }
/* 761 */     return RxJavaPlugins.onAssembly((ParallelFlowable)new ParallelFromArray((Publisher[])publishers));
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final <U> U to(@NonNull Function<? super ParallelFlowable<T>, U> converter) {
/*     */     try {
/* 776 */       return (U)((Function)ObjectHelper.requireNonNull(converter, "converter is null")).apply(this);
/* 777 */     } catch (Throwable ex) {
/* 778 */       Exceptions.throwIfFatal(ex);
/* 779 */       throw ExceptionHelper.wrapOrThrow(ex);
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final <U> ParallelFlowable<U> compose(@NonNull ParallelTransformer<T, U> composer) {
/* 794 */     return RxJavaPlugins.onAssembly(((ParallelTransformer)ObjectHelper.requireNonNull(composer, "composer is null")).apply(this));
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final <R> ParallelFlowable<R> flatMap(@NonNull Function<? super T, ? extends Publisher<? extends R>> mapper) {
/* 809 */     return flatMap(mapper, false, 2147483647, Flowable.bufferSize());
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final <R> ParallelFlowable<R> flatMap(@NonNull Function<? super T, ? extends Publisher<? extends R>> mapper, boolean delayError) {
/* 826 */     return flatMap(mapper, delayError, 2147483647, Flowable.bufferSize());
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final <R> ParallelFlowable<R> flatMap(@NonNull Function<? super T, ? extends Publisher<? extends R>> mapper, boolean delayError, int maxConcurrency) {
/* 845 */     return flatMap(mapper, delayError, maxConcurrency, Flowable.bufferSize());
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final <R> ParallelFlowable<R> flatMap(@NonNull Function<? super T, ? extends Publisher<? extends R>> mapper, boolean delayError, int maxConcurrency, int prefetch) {
/* 865 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 866 */     ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
/* 867 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/* 868 */     return RxJavaPlugins.onAssembly((ParallelFlowable)new ParallelFlatMap(this, mapper, delayError, maxConcurrency, prefetch));
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final <R> ParallelFlowable<R> concatMap(@NonNull Function<? super T, ? extends Publisher<? extends R>> mapper) {
/* 884 */     return concatMap(mapper, 2);
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final <R> ParallelFlowable<R> concatMap(@NonNull Function<? super T, ? extends Publisher<? extends R>> mapper, int prefetch) {
/* 902 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 903 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/* 904 */     return RxJavaPlugins.onAssembly((ParallelFlowable)new ParallelConcatMap(this, mapper, prefetch, ErrorMode.IMMEDIATE));
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final <R> ParallelFlowable<R> concatMapDelayError(@NonNull Function<? super T, ? extends Publisher<? extends R>> mapper, boolean tillTheEnd) {
/* 923 */     return concatMapDelayError(mapper, 2, tillTheEnd);
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public final <R> ParallelFlowable<R> concatMapDelayError(@NonNull Function<? super T, ? extends Publisher<? extends R>> mapper, int prefetch, boolean tillTheEnd) {
/* 942 */     ObjectHelper.requireNonNull(mapper, "mapper is null");
/* 943 */     ObjectHelper.verifyPositive(prefetch, "prefetch");
/* 944 */     return RxJavaPlugins.onAssembly((ParallelFlowable)new ParallelConcatMap(this, mapper, prefetch, tillTheEnd ? ErrorMode.END : ErrorMode.BOUNDARY));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/parallel/ParallelFlowable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */