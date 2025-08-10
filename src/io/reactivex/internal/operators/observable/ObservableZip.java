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
/*     */ import java.util.Arrays;
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
/*     */ 
/*     */ 
/*     */ public final class ObservableZip<T, R>
/*     */   extends Observable<R>
/*     */ {
/*     */   final ObservableSource<? extends T>[] sources;
/*     */   final Iterable<? extends ObservableSource<? extends T>> sourcesIterable;
/*     */   final Function<? super Object[], ? extends R> zipper;
/*     */   final int bufferSize;
/*     */   final boolean delayError;
/*     */   
/*     */   public ObservableZip(ObservableSource<? extends T>[] sources, Iterable<? extends ObservableSource<? extends T>> sourcesIterable, Function<? super Object[], ? extends R> zipper, int bufferSize, boolean delayError) {
/*  40 */     this.sources = sources;
/*  41 */     this.sourcesIterable = sourcesIterable;
/*  42 */     this.zipper = zipper;
/*  43 */     this.bufferSize = bufferSize;
/*  44 */     this.delayError = delayError;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super R> observer) {
/*     */     ObservableSource[] arrayOfObservableSource;
/*  50 */     ObservableSource<? extends T>[] sources = this.sources;
/*  51 */     int count = 0;
/*  52 */     if (sources == null) {
/*  53 */       arrayOfObservableSource = new ObservableSource[8];
/*  54 */       for (ObservableSource<? extends T> p : this.sourcesIterable) {
/*  55 */         if (count == arrayOfObservableSource.length) {
/*  56 */           ObservableSource[] arrayOfObservableSource1 = new ObservableSource[count + (count >> 2)];
/*  57 */           System.arraycopy(arrayOfObservableSource, 0, arrayOfObservableSource1, 0, count);
/*  58 */           arrayOfObservableSource = arrayOfObservableSource1;
/*     */         } 
/*  60 */         arrayOfObservableSource[count++] = p;
/*     */       } 
/*     */     } else {
/*  63 */       count = arrayOfObservableSource.length;
/*     */     } 
/*     */     
/*  66 */     if (count == 0) {
/*  67 */       EmptyDisposable.complete(observer);
/*     */       
/*     */       return;
/*     */     } 
/*  71 */     ZipCoordinator<T, R> zc = new ZipCoordinator<T, R>(observer, this.zipper, count, this.delayError);
/*  72 */     zc.subscribe((ObservableSource<? extends T>[])arrayOfObservableSource, this.bufferSize);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ZipCoordinator<T, R>
/*     */     extends AtomicInteger
/*     */     implements Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 2983708048395377667L;
/*     */     
/*     */     final Observer<? super R> downstream;
/*     */     final Function<? super Object[], ? extends R> zipper;
/*     */     final ObservableZip.ZipObserver<T, R>[] observers;
/*     */     final T[] row;
/*     */     final boolean delayError;
/*     */     volatile boolean cancelled;
/*     */     
/*     */     ZipCoordinator(Observer<? super R> actual, Function<? super Object[], ? extends R> zipper, int count, boolean delayError) {
/*  90 */       this.downstream = actual;
/*  91 */       this.zipper = zipper;
/*  92 */       this.observers = (ObservableZip.ZipObserver<T, R>[])new ObservableZip.ZipObserver[count];
/*  93 */       this.row = (T[])new Object[count];
/*  94 */       this.delayError = delayError;
/*     */     }
/*     */     
/*     */     public void subscribe(ObservableSource<? extends T>[] sources, int bufferSize) {
/*  98 */       ObservableZip.ZipObserver<T, R>[] s = this.observers;
/*  99 */       int len = s.length; int i;
/* 100 */       for (i = 0; i < len; i++) {
/* 101 */         s[i] = new ObservableZip.ZipObserver<T, R>(this, bufferSize);
/*     */       }
/*     */       
/* 104 */       lazySet(0);
/* 105 */       this.downstream.onSubscribe(this);
/* 106 */       for (i = 0; i < len; i++) {
/* 107 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/* 110 */         sources[i].subscribe(s[i]);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 116 */       if (!this.cancelled) {
/* 117 */         this.cancelled = true;
/* 118 */         cancelSources();
/* 119 */         if (getAndIncrement() == 0) {
/* 120 */           clear();
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 127 */       return this.cancelled;
/*     */     }
/*     */     
/*     */     void cancel() {
/* 131 */       clear();
/* 132 */       cancelSources();
/*     */     }
/*     */     
/*     */     void cancelSources() {
/* 136 */       for (ObservableZip.ZipObserver<?, ?> zs : this.observers) {
/* 137 */         zs.dispose();
/*     */       }
/*     */     }
/*     */     
/*     */     void clear() {
/* 142 */       for (ObservableZip.ZipObserver<?, ?> zs : this.observers) {
/* 143 */         zs.queue.clear();
/*     */       }
/*     */     }
/*     */     
/*     */     public void drain() {
/* 148 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 152 */       int missing = 1;
/*     */       
/* 154 */       ObservableZip.ZipObserver<T, R>[] zs = this.observers;
/* 155 */       Observer<? super R> a = this.downstream;
/* 156 */       T[] os = this.row;
/* 157 */       boolean delayError = this.delayError;
/*     */       
/*     */       while (true) {
/*     */         R v;
/*     */         
/* 162 */         int i = 0;
/* 163 */         int emptyCount = 0;
/* 164 */         for (ObservableZip.ZipObserver<T, R> z : zs) {
/* 165 */           if (os[i] == null) {
/* 166 */             boolean d = z.done;
/* 167 */             T t = (T)z.queue.poll();
/* 168 */             boolean empty = (t == null);
/*     */             
/* 170 */             if (checkTerminated(d, empty, a, delayError, z)) {
/*     */               return;
/*     */             }
/* 173 */             if (!empty) {
/* 174 */               os[i] = t;
/*     */             } else {
/* 176 */               emptyCount++;
/*     */             }
/*     */           
/* 179 */           } else if (z.done && !delayError) {
/* 180 */             Throwable ex = z.error;
/* 181 */             if (ex != null) {
/* 182 */               this.cancelled = true;
/* 183 */               cancel();
/* 184 */               a.onError(ex);
/*     */               
/*     */               return;
/*     */             } 
/*     */           } 
/* 189 */           i++;
/*     */         } 
/*     */         
/* 192 */         if (emptyCount != 0) {
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
/* 211 */           missing = addAndGet(-missing);
/* 212 */           if (missing == 0)
/*     */             break;  continue;
/*     */         }  try {
/*     */           v = (R)ObjectHelper.requireNonNull(this.zipper.apply(os.clone()), "The zipper returned a null value");
/*     */         } catch (Throwable ex) {
/*     */           Exceptions.throwIfFatal(ex); cancel(); a.onError(ex); return;
/*     */         }  a.onNext(v); Arrays.fill((Object[])os, (Object)null);
/* 219 */       }  } boolean checkTerminated(boolean d, boolean empty, Observer<? super R> a, boolean delayError, ObservableZip.ZipObserver<?, ?> source) { if (this.cancelled) {
/* 220 */         cancel();
/* 221 */         return true;
/*     */       } 
/*     */       
/* 224 */       if (d) {
/* 225 */         if (delayError) {
/* 226 */           if (empty) {
/* 227 */             Throwable e = source.error;
/* 228 */             this.cancelled = true;
/* 229 */             cancel();
/* 230 */             if (e != null) {
/* 231 */               a.onError(e);
/*     */             } else {
/* 233 */               a.onComplete();
/*     */             } 
/* 235 */             return true;
/*     */           } 
/*     */         } else {
/* 238 */           Throwable e = source.error;
/* 239 */           if (e != null) {
/* 240 */             this.cancelled = true;
/* 241 */             cancel();
/* 242 */             a.onError(e);
/* 243 */             return true;
/*     */           } 
/* 245 */           if (empty) {
/* 246 */             this.cancelled = true;
/* 247 */             cancel();
/* 248 */             a.onComplete();
/* 249 */             return true;
/*     */           } 
/*     */         } 
/*     */       }
/*     */       
/* 254 */       return false; }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ZipObserver<T, R>
/*     */     implements Observer<T>
/*     */   {
/*     */     final ObservableZip.ZipCoordinator<T, R> parent;
/*     */     final SpscLinkedArrayQueue<T> queue;
/*     */     volatile boolean done;
/*     */     Throwable error;
/* 266 */     final AtomicReference<Disposable> upstream = new AtomicReference<Disposable>();
/*     */     
/*     */     ZipObserver(ObservableZip.ZipCoordinator<T, R> parent, int bufferSize) {
/* 269 */       this.parent = parent;
/* 270 */       this.queue = new SpscLinkedArrayQueue(bufferSize);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 275 */       DisposableHelper.setOnce(this.upstream, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 280 */       this.queue.offer(t);
/* 281 */       this.parent.drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 286 */       this.error = t;
/* 287 */       this.done = true;
/* 288 */       this.parent.drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 293 */       this.done = true;
/* 294 */       this.parent.drain();
/*     */     }
/*     */     
/*     */     public void dispose() {
/* 298 */       DisposableHelper.dispose(this.upstream);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableZip.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */