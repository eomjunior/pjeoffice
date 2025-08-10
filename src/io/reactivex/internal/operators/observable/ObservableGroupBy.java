/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.queue.SpscLinkedArrayQueue;
/*     */ import io.reactivex.observables.GroupedObservable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ public final class ObservableGroupBy<T, K, V>
/*     */   extends AbstractObservableWithUpstream<T, GroupedObservable<K, V>>
/*     */ {
/*     */   final Function<? super T, ? extends K> keySelector;
/*     */   final Function<? super T, ? extends V> valueSelector;
/*     */   final int bufferSize;
/*     */   final boolean delayError;
/*     */   
/*     */   public ObservableGroupBy(ObservableSource<T> source, Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector, int bufferSize, boolean delayError) {
/*  39 */     super(source);
/*  40 */     this.keySelector = keySelector;
/*  41 */     this.valueSelector = valueSelector;
/*  42 */     this.bufferSize = bufferSize;
/*  43 */     this.delayError = delayError;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super GroupedObservable<K, V>> t) {
/*  48 */     this.source.subscribe(new GroupByObserver<T, K, V>(t, this.keySelector, this.valueSelector, this.bufferSize, this.delayError));
/*     */   }
/*     */   
/*     */   public static final class GroupByObserver<T, K, V>
/*     */     extends AtomicInteger
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -3688291656102519502L;
/*     */     final Observer<? super GroupedObservable<K, V>> downstream;
/*     */     final Function<? super T, ? extends K> keySelector;
/*     */     final Function<? super T, ? extends V> valueSelector;
/*     */     final int bufferSize;
/*     */     final boolean delayError;
/*     */     final Map<Object, ObservableGroupBy.GroupedUnicast<K, V>> groups;
/*  62 */     static final Object NULL_KEY = new Object();
/*     */     
/*     */     Disposable upstream;
/*     */     
/*  66 */     final AtomicBoolean cancelled = new AtomicBoolean();
/*     */     
/*     */     public GroupByObserver(Observer<? super GroupedObservable<K, V>> actual, Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector, int bufferSize, boolean delayError) {
/*  69 */       this.downstream = actual;
/*  70 */       this.keySelector = keySelector;
/*  71 */       this.valueSelector = valueSelector;
/*  72 */       this.bufferSize = bufferSize;
/*  73 */       this.delayError = delayError;
/*  74 */       this.groups = new ConcurrentHashMap<Object, ObservableGroupBy.GroupedUnicast<K, V>>();
/*  75 */       lazySet(1);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  80 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  81 */         this.upstream = d;
/*  82 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/*     */       K key;
/*     */       V v;
/*     */       try {
/*  90 */         key = (K)this.keySelector.apply(t);
/*  91 */       } catch (Throwable e) {
/*  92 */         Exceptions.throwIfFatal(e);
/*  93 */         this.upstream.dispose();
/*  94 */         onError(e);
/*     */         
/*     */         return;
/*     */       } 
/*  98 */       Object mapKey = (key != null) ? key : NULL_KEY;
/*  99 */       ObservableGroupBy.GroupedUnicast<K, V> group = this.groups.get(mapKey);
/* 100 */       if (group == null) {
/*     */ 
/*     */         
/* 103 */         if (this.cancelled.get()) {
/*     */           return;
/*     */         }
/*     */         
/* 107 */         group = ObservableGroupBy.GroupedUnicast.createWith(key, this.bufferSize, this, this.delayError);
/* 108 */         this.groups.put(mapKey, group);
/*     */         
/* 110 */         getAndIncrement();
/*     */         
/* 112 */         this.downstream.onNext(group);
/*     */       } 
/*     */ 
/*     */       
/*     */       try {
/* 117 */         v = (V)ObjectHelper.requireNonNull(this.valueSelector.apply(t), "The value supplied is null");
/* 118 */       } catch (Throwable e) {
/* 119 */         Exceptions.throwIfFatal(e);
/* 120 */         this.upstream.dispose();
/* 121 */         onError(e);
/*     */         
/*     */         return;
/*     */       } 
/* 125 */       group.onNext(v);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 130 */       List<ObservableGroupBy.GroupedUnicast<K, V>> list = new ArrayList<ObservableGroupBy.GroupedUnicast<K, V>>(this.groups.values());
/* 131 */       this.groups.clear();
/*     */       
/* 133 */       for (ObservableGroupBy.GroupedUnicast<K, V> e : list) {
/* 134 */         e.onError(t);
/*     */       }
/*     */       
/* 137 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 142 */       List<ObservableGroupBy.GroupedUnicast<K, V>> list = new ArrayList<ObservableGroupBy.GroupedUnicast<K, V>>(this.groups.values());
/* 143 */       this.groups.clear();
/*     */       
/* 145 */       for (ObservableGroupBy.GroupedUnicast<K, V> e : list) {
/* 146 */         e.onComplete();
/*     */       }
/*     */       
/* 149 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 156 */       if (this.cancelled.compareAndSet(false, true) && 
/* 157 */         decrementAndGet() == 0) {
/* 158 */         this.upstream.dispose();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 165 */       return this.cancelled.get();
/*     */     }
/*     */     
/*     */     public void cancel(K key) {
/* 169 */       Object mapKey = (key != null) ? key : NULL_KEY;
/* 170 */       this.groups.remove(mapKey);
/* 171 */       if (decrementAndGet() == 0)
/* 172 */         this.upstream.dispose(); 
/*     */     }
/*     */   }
/*     */   
/*     */   static final class GroupedUnicast<K, T>
/*     */     extends GroupedObservable<K, T>
/*     */   {
/*     */     final ObservableGroupBy.State<T, K> state;
/*     */     
/*     */     public static <T, K> GroupedUnicast<K, T> createWith(K key, int bufferSize, ObservableGroupBy.GroupByObserver<?, K, T> parent, boolean delayError) {
/* 182 */       ObservableGroupBy.State<T, K> state = new ObservableGroupBy.State<T, K>(bufferSize, parent, key, delayError);
/* 183 */       return new GroupedUnicast<K, T>(key, state);
/*     */     }
/*     */     
/*     */     protected GroupedUnicast(K key, ObservableGroupBy.State<T, K> state) {
/* 187 */       super(key);
/* 188 */       this.state = state;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void subscribeActual(Observer<? super T> observer) {
/* 193 */       this.state.subscribe(observer);
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/* 197 */       this.state.onNext(t);
/*     */     }
/*     */     
/*     */     public void onError(Throwable e) {
/* 201 */       this.state.onError(e);
/*     */     }
/*     */     
/*     */     public void onComplete() {
/* 205 */       this.state.onComplete();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class State<T, K>
/*     */     extends AtomicInteger
/*     */     implements Disposable, ObservableSource<T>
/*     */   {
/*     */     private static final long serialVersionUID = -3852313036005250360L;
/*     */     final K key;
/*     */     final SpscLinkedArrayQueue<T> queue;
/*     */     final ObservableGroupBy.GroupByObserver<?, K, T> parent;
/*     */     final boolean delayError;
/*     */     volatile boolean done;
/*     */     Throwable error;
/* 221 */     final AtomicBoolean cancelled = new AtomicBoolean();
/*     */     
/* 223 */     final AtomicBoolean once = new AtomicBoolean();
/*     */     
/* 225 */     final AtomicReference<Observer<? super T>> actual = new AtomicReference<Observer<? super T>>();
/*     */     
/*     */     State(int bufferSize, ObservableGroupBy.GroupByObserver<?, K, T> parent, K key, boolean delayError) {
/* 228 */       this.queue = new SpscLinkedArrayQueue(bufferSize);
/* 229 */       this.parent = parent;
/* 230 */       this.key = key;
/* 231 */       this.delayError = delayError;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 236 */       if (this.cancelled.compareAndSet(false, true) && 
/* 237 */         getAndIncrement() == 0) {
/* 238 */         this.actual.lazySet(null);
/* 239 */         this.parent.cancel(this.key);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 246 */       return this.cancelled.get();
/*     */     }
/*     */ 
/*     */     
/*     */     public void subscribe(Observer<? super T> observer) {
/* 251 */       if (this.once.compareAndSet(false, true)) {
/* 252 */         observer.onSubscribe(this);
/* 253 */         this.actual.lazySet(observer);
/* 254 */         if (this.cancelled.get()) {
/* 255 */           this.actual.lazySet(null);
/*     */         } else {
/* 257 */           drain();
/*     */         } 
/*     */       } else {
/* 260 */         EmptyDisposable.error(new IllegalStateException("Only one Observer allowed!"), observer);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/* 265 */       this.queue.offer(t);
/* 266 */       drain();
/*     */     }
/*     */     
/*     */     public void onError(Throwable e) {
/* 270 */       this.error = e;
/* 271 */       this.done = true;
/* 272 */       drain();
/*     */     }
/*     */     
/*     */     public void onComplete() {
/* 276 */       this.done = true;
/* 277 */       drain();
/*     */     }
/*     */     
/*     */     void drain() {
/* 281 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/* 284 */       int missed = 1;
/*     */       
/* 286 */       SpscLinkedArrayQueue<T> q = this.queue;
/* 287 */       boolean delayError = this.delayError;
/* 288 */       Observer<? super T> a = this.actual.get();
/*     */       while (true) {
/* 290 */         if (a != null) {
/*     */           while (true) {
/* 292 */             boolean d = this.done;
/* 293 */             T v = (T)q.poll();
/* 294 */             boolean empty = (v == null);
/*     */             
/* 296 */             if (checkTerminated(d, empty, a, delayError)) {
/*     */               return;
/*     */             }
/*     */             
/* 300 */             if (empty) {
/*     */               break;
/*     */             }
/*     */             
/* 304 */             a.onNext(v);
/*     */           } 
/*     */         }
/*     */         
/* 308 */         missed = addAndGet(-missed);
/* 309 */         if (missed == 0) {
/*     */           break;
/*     */         }
/* 312 */         if (a == null) {
/* 313 */           a = this.actual.get();
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     boolean checkTerminated(boolean d, boolean empty, Observer<? super T> a, boolean delayError) {
/* 319 */       if (this.cancelled.get()) {
/* 320 */         this.queue.clear();
/* 321 */         this.parent.cancel(this.key);
/* 322 */         this.actual.lazySet(null);
/* 323 */         return true;
/*     */       } 
/*     */       
/* 326 */       if (d) {
/* 327 */         if (delayError) {
/* 328 */           if (empty) {
/* 329 */             Throwable e = this.error;
/* 330 */             this.actual.lazySet(null);
/* 331 */             if (e != null) {
/* 332 */               a.onError(e);
/*     */             } else {
/* 334 */               a.onComplete();
/*     */             } 
/* 336 */             return true;
/*     */           } 
/*     */         } else {
/* 339 */           Throwable e = this.error;
/* 340 */           if (e != null) {
/* 341 */             this.queue.clear();
/* 342 */             this.actual.lazySet(null);
/* 343 */             a.onError(e);
/* 344 */             return true;
/*     */           } 
/* 346 */           if (empty) {
/* 347 */             this.actual.lazySet(null);
/* 348 */             a.onComplete();
/* 349 */             return true;
/*     */           } 
/*     */         } 
/*     */       }
/*     */       
/* 354 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableGroupBy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */