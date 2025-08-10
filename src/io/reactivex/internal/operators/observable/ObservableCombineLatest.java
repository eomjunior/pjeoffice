/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.queue.SpscLinkedArrayQueue;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
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
/*     */ 
/*     */ 
/*     */ public final class ObservableCombineLatest<T, R>
/*     */   extends Observable<R>
/*     */ {
/*     */   final ObservableSource<? extends T>[] sources;
/*     */   final Iterable<? extends ObservableSource<? extends T>> sourcesIterable;
/*     */   final Function<? super Object[], ? extends R> combiner;
/*     */   final int bufferSize;
/*     */   final boolean delayError;
/*     */   
/*     */   public ObservableCombineLatest(ObservableSource<? extends T>[] sources, Iterable<? extends ObservableSource<? extends T>> sourcesIterable, Function<? super Object[], ? extends R> combiner, int bufferSize, boolean delayError) {
/*  39 */     this.sources = sources;
/*  40 */     this.sourcesIterable = sourcesIterable;
/*  41 */     this.combiner = combiner;
/*  42 */     this.bufferSize = bufferSize;
/*  43 */     this.delayError = delayError;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super R> observer) {
/*     */     ObservableSource[] arrayOfObservableSource;
/*  49 */     ObservableSource<? extends T>[] sources = this.sources;
/*  50 */     int count = 0;
/*  51 */     if (sources == null) {
/*  52 */       arrayOfObservableSource = new ObservableSource[8];
/*  53 */       for (ObservableSource<? extends T> p : this.sourcesIterable) {
/*  54 */         if (count == arrayOfObservableSource.length) {
/*  55 */           ObservableSource[] arrayOfObservableSource1 = new ObservableSource[count + (count >> 2)];
/*  56 */           System.arraycopy(arrayOfObservableSource, 0, arrayOfObservableSource1, 0, count);
/*  57 */           arrayOfObservableSource = arrayOfObservableSource1;
/*     */         } 
/*  59 */         arrayOfObservableSource[count++] = p;
/*     */       } 
/*     */     } else {
/*  62 */       count = arrayOfObservableSource.length;
/*     */     } 
/*     */     
/*  65 */     if (count == 0) {
/*  66 */       EmptyDisposable.complete(observer);
/*     */       
/*     */       return;
/*     */     } 
/*  70 */     LatestCoordinator<T, R> lc = new LatestCoordinator<T, R>(observer, this.combiner, count, this.bufferSize, this.delayError);
/*  71 */     lc.subscribe((ObservableSource<? extends T>[])arrayOfObservableSource);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class LatestCoordinator<T, R>
/*     */     extends AtomicInteger
/*     */     implements Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 8567835998786448817L;
/*     */     final Observer<? super R> downstream;
/*     */     final Function<? super Object[], ? extends R> combiner;
/*     */     final ObservableCombineLatest.CombinerObserver<T, R>[] observers;
/*     */     Object[] latest;
/*     */     final SpscLinkedArrayQueue<Object[]> queue;
/*     */     final boolean delayError;
/*     */     volatile boolean cancelled;
/*     */     volatile boolean done;
/*  88 */     final AtomicThrowable errors = new AtomicThrowable();
/*     */ 
/*     */     
/*     */     int active;
/*     */     
/*     */     int complete;
/*     */ 
/*     */     
/*     */     LatestCoordinator(Observer<? super R> actual, Function<? super Object[], ? extends R> combiner, int count, int bufferSize, boolean delayError) {
/*  97 */       this.downstream = actual;
/*  98 */       this.combiner = combiner;
/*  99 */       this.delayError = delayError;
/* 100 */       this.latest = new Object[count];
/* 101 */       ObservableCombineLatest.CombinerObserver[] arrayOfCombinerObserver = new ObservableCombineLatest.CombinerObserver[count];
/* 102 */       for (int i = 0; i < count; i++) {
/* 103 */         arrayOfCombinerObserver[i] = new ObservableCombineLatest.CombinerObserver<T, R>(this, i);
/*     */       }
/* 105 */       this.observers = (ObservableCombineLatest.CombinerObserver<T, R>[])arrayOfCombinerObserver;
/* 106 */       this.queue = new SpscLinkedArrayQueue(bufferSize);
/*     */     }
/*     */     
/*     */     public void subscribe(ObservableSource<? extends T>[] sources) {
/* 110 */       ObservableCombineLatest.CombinerObserver<T, R>[] arrayOfCombinerObserver = this.observers;
/* 111 */       int len = arrayOfCombinerObserver.length;
/* 112 */       this.downstream.onSubscribe(this);
/* 113 */       for (int i = 0; i < len; i++) {
/* 114 */         if (this.done || this.cancelled) {
/*     */           return;
/*     */         }
/* 117 */         sources[i].subscribe(arrayOfCombinerObserver[i]);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 123 */       if (!this.cancelled) {
/* 124 */         this.cancelled = true;
/* 125 */         cancelSources();
/* 126 */         if (getAndIncrement() == 0) {
/* 127 */           clear(this.queue);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 134 */       return this.cancelled;
/*     */     }
/*     */     
/*     */     void cancelSources() {
/* 138 */       for (ObservableCombineLatest.CombinerObserver<T, R> observer : this.observers) {
/* 139 */         observer.dispose();
/*     */       }
/*     */     }
/*     */     
/*     */     void clear(SpscLinkedArrayQueue<?> q) {
/* 144 */       synchronized (this) {
/* 145 */         this.latest = null;
/*     */       } 
/* 147 */       q.clear();
/*     */     }
/*     */     
/*     */     void drain() {
/* 151 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 155 */       SpscLinkedArrayQueue<Object[]> q = this.queue;
/* 156 */       Observer<? super R> a = this.downstream;
/* 157 */       boolean delayError = this.delayError;
/*     */       
/* 159 */       int missed = 1;
/*     */       
/*     */       while (true) {
/*     */         R v;
/* 163 */         if (this.cancelled) {
/* 164 */           clear(q);
/*     */           
/*     */           return;
/*     */         } 
/* 168 */         if (!delayError && this.errors.get() != null) {
/* 169 */           cancelSources();
/* 170 */           clear(q);
/* 171 */           a.onError(this.errors.terminate());
/*     */           
/*     */           return;
/*     */         } 
/* 175 */         boolean d = this.done;
/* 176 */         Object[] s = (Object[])q.poll();
/* 177 */         boolean empty = (s == null);
/*     */         
/* 179 */         if (d && empty) {
/* 180 */           clear(q);
/* 181 */           Throwable ex = this.errors.terminate();
/* 182 */           if (ex == null) {
/* 183 */             a.onComplete();
/*     */           } else {
/* 185 */             a.onError(ex);
/*     */           } 
/*     */           
/*     */           return;
/*     */         } 
/* 190 */         if (empty) {
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
/* 211 */           missed = addAndGet(-missed);
/* 212 */           if (missed == 0)
/*     */             break;  continue;
/*     */         }  try {
/*     */           v = (R)ObjectHelper.requireNonNull(this.combiner.apply(s), "The combiner returned a null value");
/*     */         } catch (Throwable ex) {
/*     */           Exceptions.throwIfFatal(ex); this.errors.addThrowable(ex); cancelSources(); clear(q); ex = this.errors.terminate(); a.onError(ex); return;
/*     */         }  a.onNext(v);
/* 219 */       }  } void innerNext(int index, T item) { boolean shouldDrain = false;
/* 220 */       synchronized (this) {
/* 221 */         Object[] latest = this.latest;
/* 222 */         if (latest == null) {
/*     */           return;
/*     */         }
/* 225 */         Object o = latest[index];
/* 226 */         int a = this.active;
/* 227 */         if (o == null) {
/* 228 */           this.active = ++a;
/*     */         }
/* 230 */         latest[index] = item;
/* 231 */         if (a == latest.length) {
/* 232 */           this.queue.offer(latest.clone());
/* 233 */           shouldDrain = true;
/*     */         } 
/*     */       } 
/* 236 */       if (shouldDrain) {
/* 237 */         drain();
/*     */       } }
/*     */ 
/*     */     
/*     */     void innerError(int index, Throwable ex) {
/* 242 */       if (this.errors.addThrowable(ex)) {
/* 243 */         boolean cancelOthers = true;
/* 244 */         if (this.delayError) {
/* 245 */           synchronized (this) {
/* 246 */             Object[] latest = this.latest;
/* 247 */             if (latest == null) {
/*     */               return;
/*     */             }
/*     */             
/* 251 */             cancelOthers = (latest[index] == null);
/* 252 */             if (cancelOthers || ++this.complete == latest.length) {
/* 253 */               this.done = true;
/*     */             }
/*     */           } 
/*     */         }
/* 257 */         if (cancelOthers) {
/* 258 */           cancelSources();
/*     */         }
/* 260 */         drain();
/*     */       } else {
/* 262 */         RxJavaPlugins.onError(ex);
/*     */       } 
/*     */     }
/*     */     
/*     */     void innerComplete(int index) {
/* 267 */       boolean cancelOthers = false;
/* 268 */       synchronized (this) {
/* 269 */         Object[] latest = this.latest;
/* 270 */         if (latest == null) {
/*     */           return;
/*     */         }
/*     */         
/* 274 */         cancelOthers = (latest[index] == null);
/* 275 */         if (cancelOthers || ++this.complete == latest.length) {
/* 276 */           this.done = true;
/*     */         }
/*     */       } 
/* 279 */       if (cancelOthers) {
/* 280 */         cancelSources();
/*     */       }
/* 282 */       drain();
/*     */     }
/*     */   }
/*     */   
/*     */   static final class CombinerObserver<T, R>
/*     */     extends AtomicReference<Disposable>
/*     */     implements Observer<T>
/*     */   {
/*     */     private static final long serialVersionUID = -4823716997131257941L;
/*     */     final ObservableCombineLatest.LatestCoordinator<T, R> parent;
/*     */     final int index;
/*     */     
/*     */     CombinerObserver(ObservableCombineLatest.LatestCoordinator<T, R> parent, int index) {
/* 295 */       this.parent = parent;
/* 296 */       this.index = index;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 301 */       DisposableHelper.setOnce(this, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 306 */       this.parent.innerNext(this.index, t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 311 */       this.parent.innerError(this.index, t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 316 */       this.parent.innerComplete(this.index);
/*     */     }
/*     */     
/*     */     public void dispose() {
/* 320 */       DisposableHelper.dispose(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableCombineLatest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */