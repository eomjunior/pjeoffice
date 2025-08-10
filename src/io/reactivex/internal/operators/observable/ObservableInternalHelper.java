/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Emitter;
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.functions.Action;
/*     */ import io.reactivex.functions.BiConsumer;
/*     */ import io.reactivex.functions.BiFunction;
/*     */ import io.reactivex.functions.Consumer;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.functions.Functions;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.observables.ConnectableObservable;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ObservableInternalHelper
/*     */ {
/*     */   private ObservableInternalHelper() {
/*  29 */     throw new IllegalStateException("No instances!");
/*     */   }
/*     */   
/*     */   static final class SimpleGenerator<T, S> implements BiFunction<S, Emitter<T>, S> {
/*     */     final Consumer<Emitter<T>> consumer;
/*     */     
/*     */     SimpleGenerator(Consumer<Emitter<T>> consumer) {
/*  36 */       this.consumer = consumer;
/*     */     }
/*     */ 
/*     */     
/*     */     public S apply(S t1, Emitter<T> t2) throws Exception {
/*  41 */       this.consumer.accept(t2);
/*  42 */       return t1;
/*     */     }
/*     */   }
/*     */   
/*     */   public static <T, S> BiFunction<S, Emitter<T>, S> simpleGenerator(Consumer<Emitter<T>> consumer) {
/*  47 */     return new SimpleGenerator<T, S>(consumer);
/*     */   }
/*     */   
/*     */   static final class SimpleBiGenerator<T, S> implements BiFunction<S, Emitter<T>, S> {
/*     */     final BiConsumer<S, Emitter<T>> consumer;
/*     */     
/*     */     SimpleBiGenerator(BiConsumer<S, Emitter<T>> consumer) {
/*  54 */       this.consumer = consumer;
/*     */     }
/*     */ 
/*     */     
/*     */     public S apply(S t1, Emitter<T> t2) throws Exception {
/*  59 */       this.consumer.accept(t1, t2);
/*  60 */       return t1;
/*     */     }
/*     */   }
/*     */   
/*     */   public static <T, S> BiFunction<S, Emitter<T>, S> simpleBiGenerator(BiConsumer<S, Emitter<T>> consumer) {
/*  65 */     return new SimpleBiGenerator<T, S>(consumer);
/*     */   }
/*     */   
/*     */   static final class ItemDelayFunction<T, U> implements Function<T, ObservableSource<T>> {
/*     */     final Function<? super T, ? extends ObservableSource<U>> itemDelay;
/*     */     
/*     */     ItemDelayFunction(Function<? super T, ? extends ObservableSource<U>> itemDelay) {
/*  72 */       this.itemDelay = itemDelay;
/*     */     }
/*     */ 
/*     */     
/*     */     public ObservableSource<T> apply(T v) throws Exception {
/*  77 */       ObservableSource<U> o = (ObservableSource<U>)ObjectHelper.requireNonNull(this.itemDelay.apply(v), "The itemDelay returned a null ObservableSource");
/*  78 */       return (ObservableSource<T>)(new ObservableTake(o, 1L)).map(Functions.justFunction(v)).defaultIfEmpty(v);
/*     */     }
/*     */   }
/*     */   
/*     */   public static <T, U> Function<T, ObservableSource<T>> itemDelay(Function<? super T, ? extends ObservableSource<U>> itemDelay) {
/*  83 */     return new ItemDelayFunction<T, U>(itemDelay);
/*     */   }
/*     */   
/*     */   static final class ObserverOnNext<T> implements Consumer<T> {
/*     */     final Observer<T> observer;
/*     */     
/*     */     ObserverOnNext(Observer<T> observer) {
/*  90 */       this.observer = observer;
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept(T v) throws Exception {
/*  95 */       this.observer.onNext(v);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class ObserverOnError<T> implements Consumer<Throwable> {
/*     */     final Observer<T> observer;
/*     */     
/*     */     ObserverOnError(Observer<T> observer) {
/* 103 */       this.observer = observer;
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept(Throwable v) throws Exception {
/* 108 */       this.observer.onError(v);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class ObserverOnComplete<T> implements Action {
/*     */     final Observer<T> observer;
/*     */     
/*     */     ObserverOnComplete(Observer<T> observer) {
/* 116 */       this.observer = observer;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() throws Exception {
/* 121 */       this.observer.onComplete();
/*     */     }
/*     */   }
/*     */   
/*     */   public static <T> Consumer<T> observerOnNext(Observer<T> observer) {
/* 126 */     return new ObserverOnNext<T>(observer);
/*     */   }
/*     */   
/*     */   public static <T> Consumer<Throwable> observerOnError(Observer<T> observer) {
/* 130 */     return new ObserverOnError<T>(observer);
/*     */   }
/*     */   
/*     */   public static <T> Action observerOnComplete(Observer<T> observer) {
/* 134 */     return new ObserverOnComplete<T>(observer);
/*     */   }
/*     */   
/*     */   static final class FlatMapWithCombinerInner<U, R, T> implements Function<U, R> {
/*     */     private final BiFunction<? super T, ? super U, ? extends R> combiner;
/*     */     private final T t;
/*     */     
/*     */     FlatMapWithCombinerInner(BiFunction<? super T, ? super U, ? extends R> combiner, T t) {
/* 142 */       this.combiner = combiner;
/* 143 */       this.t = t;
/*     */     }
/*     */ 
/*     */     
/*     */     public R apply(U w) throws Exception {
/* 148 */       return (R)this.combiner.apply(this.t, w);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class FlatMapWithCombinerOuter<T, R, U>
/*     */     implements Function<T, ObservableSource<R>> {
/*     */     private final BiFunction<? super T, ? super U, ? extends R> combiner;
/*     */     private final Function<? super T, ? extends ObservableSource<? extends U>> mapper;
/*     */     
/*     */     FlatMapWithCombinerOuter(BiFunction<? super T, ? super U, ? extends R> combiner, Function<? super T, ? extends ObservableSource<? extends U>> mapper) {
/* 158 */       this.combiner = combiner;
/* 159 */       this.mapper = mapper;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public ObservableSource<R> apply(T t) throws Exception {
/* 165 */       ObservableSource<U> u = (ObservableSource<U>)ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null ObservableSource");
/* 166 */       return (ObservableSource)new ObservableMap<U, Object>(u, new ObservableInternalHelper.FlatMapWithCombinerInner<U, Object, T>(this.combiner, t));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T, U, R> Function<T, ObservableSource<R>> flatMapWithCombiner(Function<? super T, ? extends ObservableSource<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends R> combiner) {
/* 173 */     return new FlatMapWithCombinerOuter<T, R, U>(combiner, mapper);
/*     */   }
/*     */   
/*     */   static final class FlatMapIntoIterable<T, U> implements Function<T, ObservableSource<U>> {
/*     */     private final Function<? super T, ? extends Iterable<? extends U>> mapper;
/*     */     
/*     */     FlatMapIntoIterable(Function<? super T, ? extends Iterable<? extends U>> mapper) {
/* 180 */       this.mapper = mapper;
/*     */     }
/*     */ 
/*     */     
/*     */     public ObservableSource<U> apply(T t) throws Exception {
/* 185 */       return (ObservableSource<U>)new ObservableFromIterable((Iterable)ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null Iterable"));
/*     */     }
/*     */   }
/*     */   
/*     */   public static <T, U> Function<T, ObservableSource<U>> flatMapIntoIterable(Function<? super T, ? extends Iterable<? extends U>> mapper) {
/* 190 */     return new FlatMapIntoIterable<T, U>(mapper);
/*     */   }
/*     */   
/*     */   enum MapToInt implements Function<Object, Object> {
/* 194 */     INSTANCE;
/*     */     
/*     */     public Object apply(Object t) throws Exception {
/* 197 */       return Integer.valueOf(0);
/*     */     }
/*     */   }
/*     */   
/*     */   public static <T> Callable<ConnectableObservable<T>> replayCallable(Observable<T> parent) {
/* 202 */     return new ReplayCallable<T>(parent);
/*     */   }
/*     */   
/*     */   public static <T> Callable<ConnectableObservable<T>> replayCallable(Observable<T> parent, int bufferSize) {
/* 206 */     return new BufferedReplayCallable<T>(parent, bufferSize);
/*     */   }
/*     */   
/*     */   public static <T> Callable<ConnectableObservable<T>> replayCallable(Observable<T> parent, int bufferSize, long time, TimeUnit unit, Scheduler scheduler) {
/* 210 */     return new BufferedTimedReplayCallable<T>(parent, bufferSize, time, unit, scheduler);
/*     */   }
/*     */   
/*     */   public static <T> Callable<ConnectableObservable<T>> replayCallable(Observable<T> parent, long time, TimeUnit unit, Scheduler scheduler) {
/* 214 */     return new TimedReplayCallable<T>(parent, time, unit, scheduler);
/*     */   }
/*     */   
/*     */   public static <T, R> Function<Observable<T>, ObservableSource<R>> replayFunction(Function<? super Observable<T>, ? extends ObservableSource<R>> selector, Scheduler scheduler) {
/* 218 */     return new ReplayFunction<T, R>(selector, scheduler);
/*     */   }
/*     */   
/*     */   static final class ZipIterableFunction<T, R>
/*     */     implements Function<List<ObservableSource<? extends T>>, ObservableSource<? extends R>> {
/*     */     private final Function<? super Object[], ? extends R> zipper;
/*     */     
/*     */     ZipIterableFunction(Function<? super Object[], ? extends R> zipper) {
/* 226 */       this.zipper = zipper;
/*     */     }
/*     */ 
/*     */     
/*     */     public ObservableSource<? extends R> apply(List<ObservableSource<? extends T>> list) {
/* 231 */       return (ObservableSource<? extends R>)Observable.zipIterable(list, this.zipper, false, Observable.bufferSize());
/*     */     }
/*     */   }
/*     */   
/*     */   public static <T, R> Function<List<ObservableSource<? extends T>>, ObservableSource<? extends R>> zipIterable(Function<? super Object[], ? extends R> zipper) {
/* 236 */     return new ZipIterableFunction<T, R>(zipper);
/*     */   }
/*     */   
/*     */   static final class ReplayCallable<T> implements Callable<ConnectableObservable<T>> {
/*     */     private final Observable<T> parent;
/*     */     
/*     */     ReplayCallable(Observable<T> parent) {
/* 243 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public ConnectableObservable<T> call() {
/* 248 */       return this.parent.replay();
/*     */     }
/*     */   }
/*     */   
/*     */   static final class BufferedReplayCallable<T> implements Callable<ConnectableObservable<T>> {
/*     */     private final Observable<T> parent;
/*     */     private final int bufferSize;
/*     */     
/*     */     BufferedReplayCallable(Observable<T> parent, int bufferSize) {
/* 257 */       this.parent = parent;
/* 258 */       this.bufferSize = bufferSize;
/*     */     }
/*     */ 
/*     */     
/*     */     public ConnectableObservable<T> call() {
/* 263 */       return this.parent.replay(this.bufferSize);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class BufferedTimedReplayCallable<T> implements Callable<ConnectableObservable<T>> {
/*     */     private final Observable<T> parent;
/*     */     private final int bufferSize;
/*     */     private final long time;
/*     */     private final TimeUnit unit;
/*     */     private final Scheduler scheduler;
/*     */     
/*     */     BufferedTimedReplayCallable(Observable<T> parent, int bufferSize, long time, TimeUnit unit, Scheduler scheduler) {
/* 275 */       this.parent = parent;
/* 276 */       this.bufferSize = bufferSize;
/* 277 */       this.time = time;
/* 278 */       this.unit = unit;
/* 279 */       this.scheduler = scheduler;
/*     */     }
/*     */ 
/*     */     
/*     */     public ConnectableObservable<T> call() {
/* 284 */       return this.parent.replay(this.bufferSize, this.time, this.unit, this.scheduler);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class TimedReplayCallable<T> implements Callable<ConnectableObservable<T>> {
/*     */     private final Observable<T> parent;
/*     */     private final long time;
/*     */     private final TimeUnit unit;
/*     */     private final Scheduler scheduler;
/*     */     
/*     */     TimedReplayCallable(Observable<T> parent, long time, TimeUnit unit, Scheduler scheduler) {
/* 295 */       this.parent = parent;
/* 296 */       this.time = time;
/* 297 */       this.unit = unit;
/* 298 */       this.scheduler = scheduler;
/*     */     }
/*     */ 
/*     */     
/*     */     public ConnectableObservable<T> call() {
/* 303 */       return this.parent.replay(this.time, this.unit, this.scheduler);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class ReplayFunction<T, R> implements Function<Observable<T>, ObservableSource<R>> {
/*     */     private final Function<? super Observable<T>, ? extends ObservableSource<R>> selector;
/*     */     private final Scheduler scheduler;
/*     */     
/*     */     ReplayFunction(Function<? super Observable<T>, ? extends ObservableSource<R>> selector, Scheduler scheduler) {
/* 312 */       this.selector = selector;
/* 313 */       this.scheduler = scheduler;
/*     */     }
/*     */ 
/*     */     
/*     */     public ObservableSource<R> apply(Observable<T> t) throws Exception {
/* 318 */       ObservableSource<R> apply = (ObservableSource<R>)ObjectHelper.requireNonNull(this.selector.apply(t), "The selector returned a null ObservableSource");
/* 319 */       return (ObservableSource<R>)Observable.wrap(apply).observeOn(this.scheduler);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableInternalHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */