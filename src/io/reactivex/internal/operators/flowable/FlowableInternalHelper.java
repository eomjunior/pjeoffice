/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Emitter;
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.flowables.ConnectableFlowable;
/*     */ import io.reactivex.functions.Action;
/*     */ import io.reactivex.functions.BiConsumer;
/*     */ import io.reactivex.functions.BiFunction;
/*     */ import io.reactivex.functions.Consumer;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.functions.Functions;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ public final class FlowableInternalHelper
/*     */ {
/*     */   private FlowableInternalHelper() {
/*  32 */     throw new IllegalStateException("No instances!");
/*     */   }
/*     */   
/*     */   static final class SimpleGenerator<T, S> implements BiFunction<S, Emitter<T>, S> {
/*     */     final Consumer<Emitter<T>> consumer;
/*     */     
/*     */     SimpleGenerator(Consumer<Emitter<T>> consumer) {
/*  39 */       this.consumer = consumer;
/*     */     }
/*     */ 
/*     */     
/*     */     public S apply(S t1, Emitter<T> t2) throws Exception {
/*  44 */       this.consumer.accept(t2);
/*  45 */       return t1;
/*     */     }
/*     */   }
/*     */   
/*     */   public static <T, S> BiFunction<S, Emitter<T>, S> simpleGenerator(Consumer<Emitter<T>> consumer) {
/*  50 */     return new SimpleGenerator<T, S>(consumer);
/*     */   }
/*     */   
/*     */   static final class SimpleBiGenerator<T, S> implements BiFunction<S, Emitter<T>, S> {
/*     */     final BiConsumer<S, Emitter<T>> consumer;
/*     */     
/*     */     SimpleBiGenerator(BiConsumer<S, Emitter<T>> consumer) {
/*  57 */       this.consumer = consumer;
/*     */     }
/*     */ 
/*     */     
/*     */     public S apply(S t1, Emitter<T> t2) throws Exception {
/*  62 */       this.consumer.accept(t1, t2);
/*  63 */       return t1;
/*     */     }
/*     */   }
/*     */   
/*     */   public static <T, S> BiFunction<S, Emitter<T>, S> simpleBiGenerator(BiConsumer<S, Emitter<T>> consumer) {
/*  68 */     return new SimpleBiGenerator<T, S>(consumer);
/*     */   }
/*     */   
/*     */   static final class ItemDelayFunction<T, U> implements Function<T, Publisher<T>> {
/*     */     final Function<? super T, ? extends Publisher<U>> itemDelay;
/*     */     
/*     */     ItemDelayFunction(Function<? super T, ? extends Publisher<U>> itemDelay) {
/*  75 */       this.itemDelay = itemDelay;
/*     */     }
/*     */ 
/*     */     
/*     */     public Publisher<T> apply(T v) throws Exception {
/*  80 */       Publisher<U> p = (Publisher<U>)ObjectHelper.requireNonNull(this.itemDelay.apply(v), "The itemDelay returned a null Publisher");
/*  81 */       return (Publisher<T>)(new FlowableTakePublisher(p, 1L)).map(Functions.justFunction(v)).defaultIfEmpty(v);
/*     */     }
/*     */   }
/*     */   
/*     */   public static <T, U> Function<T, Publisher<T>> itemDelay(Function<? super T, ? extends Publisher<U>> itemDelay) {
/*  86 */     return new ItemDelayFunction<T, U>(itemDelay);
/*     */   }
/*     */   
/*     */   static final class SubscriberOnNext<T> implements Consumer<T> {
/*     */     final Subscriber<T> subscriber;
/*     */     
/*     */     SubscriberOnNext(Subscriber<T> subscriber) {
/*  93 */       this.subscriber = subscriber;
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept(T v) throws Exception {
/*  98 */       this.subscriber.onNext(v);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class SubscriberOnError<T> implements Consumer<Throwable> {
/*     */     final Subscriber<T> subscriber;
/*     */     
/*     */     SubscriberOnError(Subscriber<T> subscriber) {
/* 106 */       this.subscriber = subscriber;
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept(Throwable v) throws Exception {
/* 111 */       this.subscriber.onError(v);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class SubscriberOnComplete<T> implements Action {
/*     */     final Subscriber<T> subscriber;
/*     */     
/*     */     SubscriberOnComplete(Subscriber<T> subscriber) {
/* 119 */       this.subscriber = subscriber;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() throws Exception {
/* 124 */       this.subscriber.onComplete();
/*     */     }
/*     */   }
/*     */   
/*     */   public static <T> Consumer<T> subscriberOnNext(Subscriber<T> subscriber) {
/* 129 */     return new SubscriberOnNext<T>(subscriber);
/*     */   }
/*     */   
/*     */   public static <T> Consumer<Throwable> subscriberOnError(Subscriber<T> subscriber) {
/* 133 */     return new SubscriberOnError<T>(subscriber);
/*     */   }
/*     */   
/*     */   public static <T> Action subscriberOnComplete(Subscriber<T> subscriber) {
/* 137 */     return new SubscriberOnComplete<T>(subscriber);
/*     */   }
/*     */   
/*     */   static final class FlatMapWithCombinerInner<U, R, T> implements Function<U, R> {
/*     */     private final BiFunction<? super T, ? super U, ? extends R> combiner;
/*     */     private final T t;
/*     */     
/*     */     FlatMapWithCombinerInner(BiFunction<? super T, ? super U, ? extends R> combiner, T t) {
/* 145 */       this.combiner = combiner;
/* 146 */       this.t = t;
/*     */     }
/*     */ 
/*     */     
/*     */     public R apply(U w) throws Exception {
/* 151 */       return (R)this.combiner.apply(this.t, w);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class FlatMapWithCombinerOuter<T, R, U>
/*     */     implements Function<T, Publisher<R>> {
/*     */     private final BiFunction<? super T, ? super U, ? extends R> combiner;
/*     */     private final Function<? super T, ? extends Publisher<? extends U>> mapper;
/*     */     
/*     */     FlatMapWithCombinerOuter(BiFunction<? super T, ? super U, ? extends R> combiner, Function<? super T, ? extends Publisher<? extends U>> mapper) {
/* 161 */       this.combiner = combiner;
/* 162 */       this.mapper = mapper;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Publisher<R> apply(T t) throws Exception {
/* 168 */       Publisher<U> u = (Publisher<U>)ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null Publisher");
/* 169 */       return (Publisher)new FlowableMapPublisher<U, Object>(u, new FlowableInternalHelper.FlatMapWithCombinerInner<U, Object, T>(this.combiner, t));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T, U, R> Function<T, Publisher<R>> flatMapWithCombiner(Function<? super T, ? extends Publisher<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends R> combiner) {
/* 176 */     return new FlatMapWithCombinerOuter<T, R, U>(combiner, mapper);
/*     */   }
/*     */   
/*     */   static final class FlatMapIntoIterable<T, U> implements Function<T, Publisher<U>> {
/*     */     private final Function<? super T, ? extends Iterable<? extends U>> mapper;
/*     */     
/*     */     FlatMapIntoIterable(Function<? super T, ? extends Iterable<? extends U>> mapper) {
/* 183 */       this.mapper = mapper;
/*     */     }
/*     */ 
/*     */     
/*     */     public Publisher<U> apply(T t) throws Exception {
/* 188 */       return (Publisher<U>)new FlowableFromIterable((Iterable)ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null Iterable"));
/*     */     }
/*     */   }
/*     */   
/*     */   public static <T, U> Function<T, Publisher<U>> flatMapIntoIterable(Function<? super T, ? extends Iterable<? extends U>> mapper) {
/* 193 */     return new FlatMapIntoIterable<T, U>(mapper);
/*     */   }
/*     */   
/*     */   public static <T> Callable<ConnectableFlowable<T>> replayCallable(Flowable<T> parent) {
/* 197 */     return new ReplayCallable<T>(parent);
/*     */   }
/*     */   
/*     */   public static <T> Callable<ConnectableFlowable<T>> replayCallable(Flowable<T> parent, int bufferSize) {
/* 201 */     return new BufferedReplayCallable<T>(parent, bufferSize);
/*     */   }
/*     */   
/*     */   public static <T> Callable<ConnectableFlowable<T>> replayCallable(Flowable<T> parent, int bufferSize, long time, TimeUnit unit, Scheduler scheduler) {
/* 205 */     return new BufferedTimedReplay<T>(parent, bufferSize, time, unit, scheduler);
/*     */   }
/*     */   
/*     */   public static <T> Callable<ConnectableFlowable<T>> replayCallable(Flowable<T> parent, long time, TimeUnit unit, Scheduler scheduler) {
/* 209 */     return new TimedReplay<T>(parent, time, unit, scheduler);
/*     */   }
/*     */   
/*     */   public static <T, R> Function<Flowable<T>, Publisher<R>> replayFunction(Function<? super Flowable<T>, ? extends Publisher<R>> selector, Scheduler scheduler) {
/* 213 */     return new ReplayFunction<T, R>(selector, scheduler);
/*     */   }
/*     */   
/*     */   public enum RequestMax implements Consumer<Subscription> {
/* 217 */     INSTANCE;
/*     */     
/*     */     public void accept(Subscription t) throws Exception {
/* 220 */       t.request(Long.MAX_VALUE);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class ZipIterableFunction<T, R>
/*     */     implements Function<List<Publisher<? extends T>>, Publisher<? extends R>> {
/*     */     private final Function<? super Object[], ? extends R> zipper;
/*     */     
/*     */     ZipIterableFunction(Function<? super Object[], ? extends R> zipper) {
/* 229 */       this.zipper = zipper;
/*     */     }
/*     */ 
/*     */     
/*     */     public Publisher<? extends R> apply(List<Publisher<? extends T>> list) {
/* 234 */       return (Publisher<? extends R>)Flowable.zipIterable(list, this.zipper, false, Flowable.bufferSize());
/*     */     }
/*     */   }
/*     */   
/*     */   public static <T, R> Function<List<Publisher<? extends T>>, Publisher<? extends R>> zipIterable(Function<? super Object[], ? extends R> zipper) {
/* 239 */     return new ZipIterableFunction<T, R>(zipper);
/*     */   }
/*     */   
/*     */   static final class ReplayCallable<T> implements Callable<ConnectableFlowable<T>> {
/*     */     private final Flowable<T> parent;
/*     */     
/*     */     ReplayCallable(Flowable<T> parent) {
/* 246 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public ConnectableFlowable<T> call() {
/* 251 */       return this.parent.replay();
/*     */     }
/*     */   }
/*     */   
/*     */   static final class BufferedReplayCallable<T> implements Callable<ConnectableFlowable<T>> {
/*     */     private final Flowable<T> parent;
/*     */     private final int bufferSize;
/*     */     
/*     */     BufferedReplayCallable(Flowable<T> parent, int bufferSize) {
/* 260 */       this.parent = parent;
/* 261 */       this.bufferSize = bufferSize;
/*     */     }
/*     */ 
/*     */     
/*     */     public ConnectableFlowable<T> call() {
/* 266 */       return this.parent.replay(this.bufferSize);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class BufferedTimedReplay<T> implements Callable<ConnectableFlowable<T>> {
/*     */     private final Flowable<T> parent;
/*     */     private final int bufferSize;
/*     */     private final long time;
/*     */     private final TimeUnit unit;
/*     */     private final Scheduler scheduler;
/*     */     
/*     */     BufferedTimedReplay(Flowable<T> parent, int bufferSize, long time, TimeUnit unit, Scheduler scheduler) {
/* 278 */       this.parent = parent;
/* 279 */       this.bufferSize = bufferSize;
/* 280 */       this.time = time;
/* 281 */       this.unit = unit;
/* 282 */       this.scheduler = scheduler;
/*     */     }
/*     */ 
/*     */     
/*     */     public ConnectableFlowable<T> call() {
/* 287 */       return this.parent.replay(this.bufferSize, this.time, this.unit, this.scheduler);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class TimedReplay<T> implements Callable<ConnectableFlowable<T>> {
/*     */     private final Flowable<T> parent;
/*     */     private final long time;
/*     */     private final TimeUnit unit;
/*     */     private final Scheduler scheduler;
/*     */     
/*     */     TimedReplay(Flowable<T> parent, long time, TimeUnit unit, Scheduler scheduler) {
/* 298 */       this.parent = parent;
/* 299 */       this.time = time;
/* 300 */       this.unit = unit;
/* 301 */       this.scheduler = scheduler;
/*     */     }
/*     */ 
/*     */     
/*     */     public ConnectableFlowable<T> call() {
/* 306 */       return this.parent.replay(this.time, this.unit, this.scheduler);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class ReplayFunction<T, R> implements Function<Flowable<T>, Publisher<R>> {
/*     */     private final Function<? super Flowable<T>, ? extends Publisher<R>> selector;
/*     */     private final Scheduler scheduler;
/*     */     
/*     */     ReplayFunction(Function<? super Flowable<T>, ? extends Publisher<R>> selector, Scheduler scheduler) {
/* 315 */       this.selector = selector;
/* 316 */       this.scheduler = scheduler;
/*     */     }
/*     */ 
/*     */     
/*     */     public Publisher<R> apply(Flowable<T> t) throws Exception {
/* 321 */       Publisher<R> p = (Publisher<R>)ObjectHelper.requireNonNull(this.selector.apply(t), "The selector returned a null Publisher");
/* 322 */       return (Publisher<R>)Flowable.fromPublisher(p).observeOn(this.scheduler);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableInternalHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */