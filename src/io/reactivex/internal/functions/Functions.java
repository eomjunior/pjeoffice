/*     */ package io.reactivex.internal.functions;
/*     */ import io.reactivex.Notification;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.functions.Action;
/*     */ import io.reactivex.functions.BiConsumer;
/*     */ import io.reactivex.functions.BiFunction;
/*     */ import io.reactivex.functions.BooleanSupplier;
/*     */ import io.reactivex.functions.Consumer;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.functions.Function3;
/*     */ import io.reactivex.functions.Function4;
/*     */ import io.reactivex.functions.Function5;
/*     */ import io.reactivex.functions.Function6;
/*     */ import io.reactivex.functions.Function7;
/*     */ import io.reactivex.functions.Function8;
/*     */ import io.reactivex.functions.Function9;
/*     */ import io.reactivex.functions.LongConsumer;
/*     */ import io.reactivex.functions.Predicate;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import io.reactivex.schedulers.Timed;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.reactivestreams.Subscription;
/*     */ 
/*     */ public final class Functions {
/*     */   private Functions() {
/*  33 */     throw new IllegalStateException("No instances!");
/*     */   }
/*     */   
/*     */   public static <T1, T2, R> Function<Object[], R> toFunction(BiFunction<? super T1, ? super T2, ? extends R> f) {
/*  37 */     ObjectHelper.requireNonNull(f, "f is null");
/*  38 */     return new Array2Func<T1, T2, R>(f);
/*     */   }
/*     */   
/*     */   public static <T1, T2, T3, R> Function<Object[], R> toFunction(Function3<T1, T2, T3, R> f) {
/*  42 */     ObjectHelper.requireNonNull(f, "f is null");
/*  43 */     return new Array3Func<T1, T2, T3, R>(f);
/*     */   }
/*     */   
/*     */   public static <T1, T2, T3, T4, R> Function<Object[], R> toFunction(Function4<T1, T2, T3, T4, R> f) {
/*  47 */     ObjectHelper.requireNonNull(f, "f is null");
/*  48 */     return new Array4Func<T1, T2, T3, T4, R>(f);
/*     */   }
/*     */   
/*     */   public static <T1, T2, T3, T4, T5, R> Function<Object[], R> toFunction(Function5<T1, T2, T3, T4, T5, R> f) {
/*  52 */     ObjectHelper.requireNonNull(f, "f is null");
/*  53 */     return new Array5Func<T1, T2, T3, T4, T5, R>(f);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T1, T2, T3, T4, T5, T6, R> Function<Object[], R> toFunction(Function6<T1, T2, T3, T4, T5, T6, R> f) {
/*  58 */     ObjectHelper.requireNonNull(f, "f is null");
/*  59 */     return new Array6Func<T1, T2, T3, T4, T5, T6, R>(f);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T1, T2, T3, T4, T5, T6, T7, R> Function<Object[], R> toFunction(Function7<T1, T2, T3, T4, T5, T6, T7, R> f) {
/*  64 */     ObjectHelper.requireNonNull(f, "f is null");
/*  65 */     return new Array7Func<T1, T2, T3, T4, T5, T6, T7, R>(f);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Function<Object[], R> toFunction(Function8<T1, T2, T3, T4, T5, T6, T7, T8, R> f) {
/*  70 */     ObjectHelper.requireNonNull(f, "f is null");
/*  71 */     return new Array8Func<T1, T2, T3, T4, T5, T6, T7, T8, R>(f);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Function<Object[], R> toFunction(Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> f) {
/*  76 */     ObjectHelper.requireNonNull(f, "f is null");
/*  77 */     return new Array9Func<T1, T2, T3, T4, T5, T6, T7, T8, T9, R>(f);
/*     */   }
/*     */ 
/*     */   
/*  81 */   static final Function<Object, Object> IDENTITY = new Identity();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Function<T, T> identity() {
/*  90 */     return (Function)IDENTITY;
/*     */   }
/*     */   
/*  93 */   public static final Runnable EMPTY_RUNNABLE = new EmptyRunnable();
/*     */   
/*  95 */   public static final Action EMPTY_ACTION = new EmptyAction();
/*     */   
/*  97 */   static final Consumer<Object> EMPTY_CONSUMER = new EmptyConsumer();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Consumer<T> emptyConsumer() {
/* 106 */     return (Consumer)EMPTY_CONSUMER;
/*     */   }
/*     */   
/* 109 */   public static final Consumer<Throwable> ERROR_CONSUMER = new ErrorConsumer();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   public static final Consumer<Throwable> ON_ERROR_MISSING = new OnErrorMissingConsumer();
/*     */   
/* 117 */   public static final LongConsumer EMPTY_LONG_CONSUMER = new EmptyLongConsumer();
/*     */   
/* 119 */   static final Predicate<Object> ALWAYS_TRUE = new TruePredicate();
/*     */   
/* 121 */   static final Predicate<Object> ALWAYS_FALSE = new FalsePredicate();
/*     */   
/* 123 */   static final Callable<Object> NULL_SUPPLIER = new NullCallable();
/*     */   
/* 125 */   static final Comparator<Object> NATURAL_COMPARATOR = new NaturalObjectComparator();
/*     */ 
/*     */   
/*     */   public static <T> Predicate<T> alwaysTrue() {
/* 129 */     return (Predicate)ALWAYS_TRUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> Predicate<T> alwaysFalse() {
/* 134 */     return (Predicate)ALWAYS_FALSE;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> Callable<T> nullSupplier() {
/* 139 */     return (Callable)NULL_SUPPLIER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Comparator<T> naturalOrder() {
/* 149 */     return (Comparator)NATURAL_COMPARATOR;
/*     */   }
/*     */   
/*     */   static final class FutureAction implements Action {
/*     */     final Future<?> future;
/*     */     
/*     */     FutureAction(Future<?> future) {
/* 156 */       this.future = future;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() throws Exception {
/* 161 */       this.future.get();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Action futureAction(Future<?> future) {
/* 171 */     return new FutureAction(future);
/*     */   }
/*     */   
/*     */   static final class JustValue<T, U> implements Callable<U>, Function<T, U> {
/*     */     final U value;
/*     */     
/*     */     JustValue(U value) {
/* 178 */       this.value = value;
/*     */     }
/*     */ 
/*     */     
/*     */     public U call() throws Exception {
/* 183 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public U apply(T t) throws Exception {
/* 188 */       return this.value;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Callable<T> justCallable(T value) {
/* 199 */     return new JustValue<Object, T>(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T, U> Function<T, U> justFunction(U value) {
/* 210 */     return new JustValue<T, U>(value);
/*     */   }
/*     */   
/*     */   static final class CastToClass<T, U> implements Function<T, U> {
/*     */     final Class<U> clazz;
/*     */     
/*     */     CastToClass(Class<U> clazz) {
/* 217 */       this.clazz = clazz;
/*     */     }
/*     */ 
/*     */     
/*     */     public U apply(T t) throws Exception {
/* 222 */       return this.clazz.cast(t);
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
/*     */   public static <T, U> Function<T, U> castFunction(Class<U> target) {
/* 234 */     return new CastToClass<T, U>(target);
/*     */   }
/*     */   
/*     */   static final class ArrayListCapacityCallable<T> implements Callable<List<T>> {
/*     */     final int capacity;
/*     */     
/*     */     ArrayListCapacityCallable(int capacity) {
/* 241 */       this.capacity = capacity;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<T> call() throws Exception {
/* 246 */       return new ArrayList<T>(this.capacity);
/*     */     }
/*     */   }
/*     */   
/*     */   public static <T> Callable<List<T>> createArrayList(int capacity) {
/* 251 */     return new ArrayListCapacityCallable<T>(capacity);
/*     */   }
/*     */   
/*     */   static final class EqualsPredicate<T> implements Predicate<T> {
/*     */     final T value;
/*     */     
/*     */     EqualsPredicate(T value) {
/* 258 */       this.value = value;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean test(T t) throws Exception {
/* 263 */       return ObjectHelper.equals(t, this.value);
/*     */     }
/*     */   }
/*     */   
/*     */   public static <T> Predicate<T> equalsWith(T value) {
/* 268 */     return new EqualsPredicate<T>(value);
/*     */   }
/*     */   
/*     */   enum HashSetCallable implements Callable<Set<Object>> {
/* 272 */     INSTANCE;
/*     */     
/*     */     public Set<Object> call() throws Exception {
/* 275 */       return new HashSet();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> Callable<Set<T>> createHashSet() {
/* 281 */     return HashSetCallable.INSTANCE;
/*     */   }
/*     */   
/*     */   static final class NotificationOnNext<T> implements Consumer<T> {
/*     */     final Consumer<? super Notification<T>> onNotification;
/*     */     
/*     */     NotificationOnNext(Consumer<? super Notification<T>> onNotification) {
/* 288 */       this.onNotification = onNotification;
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept(T v) throws Exception {
/* 293 */       this.onNotification.accept(Notification.createOnNext(v));
/*     */     }
/*     */   }
/*     */   
/*     */   static final class NotificationOnError<T> implements Consumer<Throwable> {
/*     */     final Consumer<? super Notification<T>> onNotification;
/*     */     
/*     */     NotificationOnError(Consumer<? super Notification<T>> onNotification) {
/* 301 */       this.onNotification = onNotification;
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept(Throwable v) throws Exception {
/* 306 */       this.onNotification.accept(Notification.createOnError(v));
/*     */     }
/*     */   }
/*     */   
/*     */   static final class NotificationOnComplete<T> implements Action {
/*     */     final Consumer<? super Notification<T>> onNotification;
/*     */     
/*     */     NotificationOnComplete(Consumer<? super Notification<T>> onNotification) {
/* 314 */       this.onNotification = onNotification;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() throws Exception {
/* 319 */       this.onNotification.accept(Notification.createOnComplete());
/*     */     }
/*     */   }
/*     */   
/*     */   public static <T> Consumer<T> notificationOnNext(Consumer<? super Notification<T>> onNotification) {
/* 324 */     return new NotificationOnNext<T>(onNotification);
/*     */   }
/*     */   
/*     */   public static <T> Consumer<Throwable> notificationOnError(Consumer<? super Notification<T>> onNotification) {
/* 328 */     return new NotificationOnError<T>(onNotification);
/*     */   }
/*     */   
/*     */   public static <T> Action notificationOnComplete(Consumer<? super Notification<T>> onNotification) {
/* 332 */     return new NotificationOnComplete<T>(onNotification);
/*     */   }
/*     */   
/*     */   static final class ActionConsumer<T> implements Consumer<T> {
/*     */     final Action action;
/*     */     
/*     */     ActionConsumer(Action action) {
/* 339 */       this.action = action;
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept(T t) throws Exception {
/* 344 */       this.action.run();
/*     */     }
/*     */   }
/*     */   
/*     */   public static <T> Consumer<T> actionConsumer(Action action) {
/* 349 */     return new ActionConsumer<T>(action);
/*     */   }
/*     */   
/*     */   static final class ClassFilter<T, U> implements Predicate<T> {
/*     */     final Class<U> clazz;
/*     */     
/*     */     ClassFilter(Class<U> clazz) {
/* 356 */       this.clazz = clazz;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean test(T t) throws Exception {
/* 361 */       return this.clazz.isInstance(t);
/*     */     }
/*     */   }
/*     */   
/*     */   public static <T, U> Predicate<T> isInstanceOf(Class<U> clazz) {
/* 366 */     return new ClassFilter<T, U>(clazz);
/*     */   }
/*     */   
/*     */   static final class BooleanSupplierPredicateReverse<T> implements Predicate<T> {
/*     */     final BooleanSupplier supplier;
/*     */     
/*     */     BooleanSupplierPredicateReverse(BooleanSupplier supplier) {
/* 373 */       this.supplier = supplier;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean test(T t) throws Exception {
/* 378 */       return !this.supplier.getAsBoolean();
/*     */     }
/*     */   }
/*     */   
/*     */   public static <T> Predicate<T> predicateReverseFor(BooleanSupplier supplier) {
/* 383 */     return new BooleanSupplierPredicateReverse<T>(supplier);
/*     */   }
/*     */   
/*     */   static final class TimestampFunction<T>
/*     */     implements Function<T, Timed<T>> {
/*     */     final TimeUnit unit;
/*     */     final Scheduler scheduler;
/*     */     
/*     */     TimestampFunction(TimeUnit unit, Scheduler scheduler) {
/* 392 */       this.unit = unit;
/* 393 */       this.scheduler = scheduler;
/*     */     }
/*     */ 
/*     */     
/*     */     public Timed<T> apply(T t) throws Exception {
/* 398 */       return new Timed(t, this.scheduler.now(this.unit), this.unit);
/*     */     }
/*     */   }
/*     */   
/*     */   public static <T> Function<T, Timed<T>> timestampWith(TimeUnit unit, Scheduler scheduler) {
/* 403 */     return new TimestampFunction<T>(unit, scheduler);
/*     */   }
/*     */   
/*     */   static final class ToMapKeySelector<K, T> implements BiConsumer<Map<K, T>, T> {
/*     */     private final Function<? super T, ? extends K> keySelector;
/*     */     
/*     */     ToMapKeySelector(Function<? super T, ? extends K> keySelector) {
/* 410 */       this.keySelector = keySelector;
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept(Map<K, T> m, T t) throws Exception {
/* 415 */       K key = (K)this.keySelector.apply(t);
/* 416 */       m.put(key, t);
/*     */     }
/*     */   }
/*     */   
/*     */   public static <T, K> BiConsumer<Map<K, T>, T> toMapKeySelector(Function<? super T, ? extends K> keySelector) {
/* 421 */     return new ToMapKeySelector<K, T>(keySelector);
/*     */   }
/*     */   
/*     */   static final class ToMapKeyValueSelector<K, V, T>
/*     */     implements BiConsumer<Map<K, V>, T> {
/*     */     private final Function<? super T, ? extends V> valueSelector;
/*     */     private final Function<? super T, ? extends K> keySelector;
/*     */     
/*     */     ToMapKeyValueSelector(Function<? super T, ? extends V> valueSelector, Function<? super T, ? extends K> keySelector) {
/* 430 */       this.valueSelector = valueSelector;
/* 431 */       this.keySelector = keySelector;
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept(Map<K, V> m, T t) throws Exception {
/* 436 */       K key = (K)this.keySelector.apply(t);
/* 437 */       V value = (V)this.valueSelector.apply(t);
/* 438 */       m.put(key, value);
/*     */     }
/*     */   }
/*     */   
/*     */   public static <T, K, V> BiConsumer<Map<K, V>, T> toMapKeyValueSelector(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector) {
/* 443 */     return new ToMapKeyValueSelector<K, V, T>(valueSelector, keySelector);
/*     */   }
/*     */   
/*     */   static final class ToMultimapKeyValueSelector<K, V, T>
/*     */     implements BiConsumer<Map<K, Collection<V>>, T> {
/*     */     private final Function<? super K, ? extends Collection<? super V>> collectionFactory;
/*     */     private final Function<? super T, ? extends V> valueSelector;
/*     */     private final Function<? super T, ? extends K> keySelector;
/*     */     
/*     */     ToMultimapKeyValueSelector(Function<? super K, ? extends Collection<? super V>> collectionFactory, Function<? super T, ? extends V> valueSelector, Function<? super T, ? extends K> keySelector) {
/* 453 */       this.collectionFactory = collectionFactory;
/* 454 */       this.valueSelector = valueSelector;
/* 455 */       this.keySelector = keySelector;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void accept(Map<K, Collection<V>> m, T t) throws Exception {
/* 461 */       K key = (K)this.keySelector.apply(t);
/*     */       
/* 463 */       Collection<V> coll = m.get(key);
/* 464 */       if (coll == null) {
/* 465 */         coll = (Collection<V>)this.collectionFactory.apply(key);
/* 466 */         m.put(key, coll);
/*     */       } 
/*     */       
/* 469 */       V value = (V)this.valueSelector.apply(t);
/*     */       
/* 471 */       coll.add(value);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T, K, V> BiConsumer<Map<K, Collection<V>>, T> toMultimapKeyValueSelector(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector, Function<? super K, ? extends Collection<? super V>> collectionFactory) {
/* 478 */     return new ToMultimapKeyValueSelector<K, V, T>(collectionFactory, valueSelector, keySelector);
/*     */   }
/*     */   
/*     */   enum NaturalComparator implements Comparator<Object> {
/* 482 */     INSTANCE;
/*     */ 
/*     */ 
/*     */     
/*     */     public int compare(Object o1, Object o2) {
/* 487 */       return ((Comparable<Object>)o1).compareTo(o2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> Comparator<T> naturalComparator() {
/* 493 */     return NaturalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   static final class ListSorter<T> implements Function<List<T>, List<T>> {
/*     */     final Comparator<? super T> comparator;
/*     */     
/*     */     ListSorter(Comparator<? super T> comparator) {
/* 500 */       this.comparator = comparator;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<T> apply(List<T> v) {
/* 505 */       Collections.sort(v, this.comparator);
/* 506 */       return v;
/*     */     }
/*     */   }
/*     */   
/*     */   public static <T> Function<List<T>, List<T>> listSorter(Comparator<? super T> comparator) {
/* 511 */     return new ListSorter<T>(comparator);
/*     */   }
/*     */   
/* 514 */   public static final Consumer<Subscription> REQUEST_MAX = new MaxRequestSubscription();
/*     */   
/*     */   static final class Array2Func<T1, T2, R> implements Function<Object[], R> {
/*     */     final BiFunction<? super T1, ? super T2, ? extends R> f;
/*     */     
/*     */     Array2Func(BiFunction<? super T1, ? super T2, ? extends R> f) {
/* 520 */       this.f = f;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public R apply(Object[] a) throws Exception {
/* 526 */       if (a.length != 2) {
/* 527 */         throw new IllegalArgumentException("Array of size 2 expected but got " + a.length);
/*     */       }
/* 529 */       return (R)this.f.apply(a[0], a[1]);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class Array3Func<T1, T2, T3, R> implements Function<Object[], R> {
/*     */     final Function3<T1, T2, T3, R> f;
/*     */     
/*     */     Array3Func(Function3<T1, T2, T3, R> f) {
/* 537 */       this.f = f;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public R apply(Object[] a) throws Exception {
/* 543 */       if (a.length != 3) {
/* 544 */         throw new IllegalArgumentException("Array of size 3 expected but got " + a.length);
/*     */       }
/* 546 */       return (R)this.f.apply(a[0], a[1], a[2]);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class Array4Func<T1, T2, T3, T4, R> implements Function<Object[], R> {
/*     */     final Function4<T1, T2, T3, T4, R> f;
/*     */     
/*     */     Array4Func(Function4<T1, T2, T3, T4, R> f) {
/* 554 */       this.f = f;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public R apply(Object[] a) throws Exception {
/* 560 */       if (a.length != 4) {
/* 561 */         throw new IllegalArgumentException("Array of size 4 expected but got " + a.length);
/*     */       }
/* 563 */       return (R)this.f.apply(a[0], a[1], a[2], a[3]);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class Array5Func<T1, T2, T3, T4, T5, R> implements Function<Object[], R> {
/*     */     private final Function5<T1, T2, T3, T4, T5, R> f;
/*     */     
/*     */     Array5Func(Function5<T1, T2, T3, T4, T5, R> f) {
/* 571 */       this.f = f;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public R apply(Object[] a) throws Exception {
/* 577 */       if (a.length != 5) {
/* 578 */         throw new IllegalArgumentException("Array of size 5 expected but got " + a.length);
/*     */       }
/* 580 */       return (R)this.f.apply(a[0], a[1], a[2], a[3], a[4]);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class Array6Func<T1, T2, T3, T4, T5, T6, R> implements Function<Object[], R> {
/*     */     final Function6<T1, T2, T3, T4, T5, T6, R> f;
/*     */     
/*     */     Array6Func(Function6<T1, T2, T3, T4, T5, T6, R> f) {
/* 588 */       this.f = f;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public R apply(Object[] a) throws Exception {
/* 594 */       if (a.length != 6) {
/* 595 */         throw new IllegalArgumentException("Array of size 6 expected but got " + a.length);
/*     */       }
/* 597 */       return (R)this.f.apply(a[0], a[1], a[2], a[3], a[4], a[5]);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class Array7Func<T1, T2, T3, T4, T5, T6, T7, R> implements Function<Object[], R> {
/*     */     final Function7<T1, T2, T3, T4, T5, T6, T7, R> f;
/*     */     
/*     */     Array7Func(Function7<T1, T2, T3, T4, T5, T6, T7, R> f) {
/* 605 */       this.f = f;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public R apply(Object[] a) throws Exception {
/* 611 */       if (a.length != 7) {
/* 612 */         throw new IllegalArgumentException("Array of size 7 expected but got " + a.length);
/*     */       }
/* 614 */       return (R)this.f.apply(a[0], a[1], a[2], a[3], a[4], a[5], a[6]);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class Array8Func<T1, T2, T3, T4, T5, T6, T7, T8, R> implements Function<Object[], R> {
/*     */     final Function8<T1, T2, T3, T4, T5, T6, T7, T8, R> f;
/*     */     
/*     */     Array8Func(Function8<T1, T2, T3, T4, T5, T6, T7, T8, R> f) {
/* 622 */       this.f = f;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public R apply(Object[] a) throws Exception {
/* 628 */       if (a.length != 8) {
/* 629 */         throw new IllegalArgumentException("Array of size 8 expected but got " + a.length);
/*     */       }
/* 631 */       return (R)this.f.apply(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7]);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class Array9Func<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> implements Function<Object[], R> {
/*     */     final Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> f;
/*     */     
/*     */     Array9Func(Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> f) {
/* 639 */       this.f = f;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public R apply(Object[] a) throws Exception {
/* 645 */       if (a.length != 9) {
/* 646 */         throw new IllegalArgumentException("Array of size 9 expected but got " + a.length);
/*     */       }
/* 648 */       return (R)this.f.apply(a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8]);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class Identity
/*     */     implements Function<Object, Object> {
/*     */     public Object apply(Object v) {
/* 655 */       return v;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 660 */       return "IdentityFunction";
/*     */     }
/*     */   }
/*     */   
/*     */   static final class EmptyRunnable
/*     */     implements Runnable
/*     */   {
/*     */     public void run() {}
/*     */     
/*     */     public String toString() {
/* 670 */       return "EmptyRunnable";
/*     */     }
/*     */   }
/*     */   
/*     */   static final class EmptyAction
/*     */     implements Action
/*     */   {
/*     */     public void run() {}
/*     */     
/*     */     public String toString() {
/* 680 */       return "EmptyAction";
/*     */     }
/*     */   }
/*     */   
/*     */   static final class EmptyConsumer
/*     */     implements Consumer<Object>
/*     */   {
/*     */     public void accept(Object v) {}
/*     */     
/*     */     public String toString() {
/* 690 */       return "EmptyConsumer";
/*     */     }
/*     */   }
/*     */   
/*     */   static final class ErrorConsumer
/*     */     implements Consumer<Throwable> {
/*     */     public void accept(Throwable error) {
/* 697 */       RxJavaPlugins.onError(error);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class OnErrorMissingConsumer
/*     */     implements Consumer<Throwable> {
/*     */     public void accept(Throwable error) {
/* 704 */       RxJavaPlugins.onError((Throwable)new OnErrorNotImplementedException(error));
/*     */     }
/*     */   }
/*     */   
/*     */   static final class EmptyLongConsumer
/*     */     implements LongConsumer {
/*     */     public void accept(long v) {}
/*     */   }
/*     */   
/*     */   static final class TruePredicate
/*     */     implements Predicate<Object> {
/*     */     public boolean test(Object o) {
/* 716 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   static final class FalsePredicate
/*     */     implements Predicate<Object> {
/*     */     public boolean test(Object o) {
/* 723 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   static final class NullCallable
/*     */     implements Callable<Object> {
/*     */     public Object call() {
/* 730 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   static final class NaturalObjectComparator
/*     */     implements Comparator<Object>
/*     */   {
/*     */     public int compare(Object a, Object b) {
/* 738 */       return ((Comparable<Object>)a).compareTo(b);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class MaxRequestSubscription
/*     */     implements Consumer<Subscription> {
/*     */     public void accept(Subscription t) throws Exception {
/* 745 */       t.request(Long.MAX_VALUE);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> Consumer<T> boundedConsumer(int bufferSize) {
/* 751 */     return new BoundedConsumer(bufferSize);
/*     */   }
/*     */   
/*     */   public static class BoundedConsumer
/*     */     implements Consumer<Subscription> {
/*     */     final int bufferSize;
/*     */     
/*     */     BoundedConsumer(int bufferSize) {
/* 759 */       this.bufferSize = bufferSize;
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept(Subscription s) throws Exception {
/* 764 */       s.request(this.bufferSize);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/functions/Functions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */