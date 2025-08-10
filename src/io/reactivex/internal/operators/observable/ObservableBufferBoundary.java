/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.CompositeDisposable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.queue.SpscLinkedArrayQueue;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Callable;
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
/*     */ public final class ObservableBufferBoundary<T, U extends Collection<? super T>, Open, Close>
/*     */   extends AbstractObservableWithUpstream<T, U>
/*     */ {
/*     */   final Callable<U> bufferSupplier;
/*     */   final ObservableSource<? extends Open> bufferOpen;
/*     */   final Function<? super Open, ? extends ObservableSource<? extends Close>> bufferClose;
/*     */   
/*     */   public ObservableBufferBoundary(ObservableSource<T> source, ObservableSource<? extends Open> bufferOpen, Function<? super Open, ? extends ObservableSource<? extends Close>> bufferClose, Callable<U> bufferSupplier) {
/*  39 */     super(source);
/*  40 */     this.bufferOpen = bufferOpen;
/*  41 */     this.bufferClose = bufferClose;
/*  42 */     this.bufferSupplier = bufferSupplier;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super U> t) {
/*  47 */     BufferBoundaryObserver<T, U, Open, Close> parent = new BufferBoundaryObserver<T, U, Open, Close>(t, this.bufferOpen, this.bufferClose, this.bufferSupplier);
/*     */ 
/*     */ 
/*     */     
/*  51 */     t.onSubscribe(parent);
/*  52 */     this.source.subscribe(parent);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class BufferBoundaryObserver<T, C extends Collection<? super T>, Open, Close>
/*     */     extends AtomicInteger
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -8466418554264089604L;
/*     */     
/*     */     final Observer<? super C> downstream;
/*     */     
/*     */     final Callable<C> bufferSupplier;
/*     */     
/*     */     final ObservableSource<? extends Open> bufferOpen;
/*     */     
/*     */     final Function<? super Open, ? extends ObservableSource<? extends Close>> bufferClose;
/*     */     
/*     */     final CompositeDisposable observers;
/*     */     
/*     */     final AtomicReference<Disposable> upstream;
/*     */     
/*     */     final AtomicThrowable errors;
/*     */     
/*     */     volatile boolean done;
/*     */     
/*     */     final SpscLinkedArrayQueue<C> queue;
/*     */     
/*     */     volatile boolean cancelled;
/*     */     
/*     */     long index;
/*     */     
/*     */     Map<Long, C> buffers;
/*     */ 
/*     */     
/*     */     BufferBoundaryObserver(Observer<? super C> actual, ObservableSource<? extends Open> bufferOpen, Function<? super Open, ? extends ObservableSource<? extends Close>> bufferClose, Callable<C> bufferSupplier) {
/*  89 */       this.downstream = actual;
/*  90 */       this.bufferSupplier = bufferSupplier;
/*  91 */       this.bufferOpen = bufferOpen;
/*  92 */       this.bufferClose = bufferClose;
/*  93 */       this.queue = new SpscLinkedArrayQueue(Observable.bufferSize());
/*  94 */       this.observers = new CompositeDisposable();
/*  95 */       this.upstream = new AtomicReference<Disposable>();
/*  96 */       this.buffers = new LinkedHashMap<Long, C>();
/*  97 */       this.errors = new AtomicThrowable();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 102 */       if (DisposableHelper.setOnce(this.upstream, d)) {
/*     */         
/* 104 */         BufferOpenObserver<Open> open = new BufferOpenObserver<Open>(this);
/* 105 */         this.observers.add(open);
/*     */         
/* 107 */         this.bufferOpen.subscribe(open);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 113 */       synchronized (this) {
/* 114 */         Map<Long, C> bufs = this.buffers;
/* 115 */         if (bufs == null) {
/*     */           return;
/*     */         }
/* 118 */         for (Collection<T> collection : bufs.values()) {
/* 119 */           collection.add(t);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 126 */       if (this.errors.addThrowable(t)) {
/* 127 */         this.observers.dispose();
/* 128 */         synchronized (this) {
/* 129 */           this.buffers = null;
/*     */         } 
/* 131 */         this.done = true;
/* 132 */         drain();
/*     */       } else {
/* 134 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 140 */       this.observers.dispose();
/* 141 */       synchronized (this) {
/* 142 */         Map<Long, C> bufs = this.buffers;
/* 143 */         if (bufs == null) {
/*     */           return;
/*     */         }
/* 146 */         for (Collection collection : bufs.values()) {
/* 147 */           this.queue.offer(collection);
/*     */         }
/* 149 */         this.buffers = null;
/*     */       } 
/* 151 */       this.done = true;
/* 152 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 157 */       if (DisposableHelper.dispose(this.upstream)) {
/* 158 */         this.cancelled = true;
/* 159 */         this.observers.dispose();
/* 160 */         synchronized (this) {
/* 161 */           this.buffers = null;
/*     */         } 
/* 163 */         if (getAndIncrement() != 0) {
/* 164 */           this.queue.clear();
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 171 */       return DisposableHelper.isDisposed(this.upstream.get());
/*     */     }
/*     */     
/*     */     void open(Open token) {
/*     */       ObservableSource<? extends Close> p;
/*     */       Collection collection;
/*     */       try {
/* 178 */         collection = (Collection)ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The bufferSupplier returned a null Collection");
/* 179 */         p = (ObservableSource<? extends Close>)ObjectHelper.requireNonNull(this.bufferClose.apply(token), "The bufferClose returned a null ObservableSource");
/* 180 */       } catch (Throwable ex) {
/* 181 */         Exceptions.throwIfFatal(ex);
/* 182 */         DisposableHelper.dispose(this.upstream);
/* 183 */         onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/* 187 */       long idx = this.index;
/* 188 */       this.index = idx + 1L;
/* 189 */       synchronized (this) {
/* 190 */         Map<Long, C> bufs = this.buffers;
/* 191 */         if (bufs == null) {
/*     */           return;
/*     */         }
/* 194 */         bufs.put(Long.valueOf(idx), (C)collection);
/*     */       } 
/*     */       
/* 197 */       ObservableBufferBoundary.BufferCloseObserver<T, C> bc = new ObservableBufferBoundary.BufferCloseObserver<T, C>(this, idx);
/* 198 */       this.observers.add(bc);
/* 199 */       p.subscribe(bc);
/*     */     }
/*     */     
/*     */     void openComplete(BufferOpenObserver<Open> os) {
/* 203 */       this.observers.delete(os);
/* 204 */       if (this.observers.size() == 0) {
/* 205 */         DisposableHelper.dispose(this.upstream);
/* 206 */         this.done = true;
/* 207 */         drain();
/*     */       } 
/*     */     }
/*     */     
/*     */     void close(ObservableBufferBoundary.BufferCloseObserver<T, C> closer, long idx) {
/* 212 */       this.observers.delete(closer);
/* 213 */       boolean makeDone = false;
/* 214 */       if (this.observers.size() == 0) {
/* 215 */         makeDone = true;
/* 216 */         DisposableHelper.dispose(this.upstream);
/*     */       } 
/* 218 */       synchronized (this) {
/* 219 */         Map<Long, C> bufs = this.buffers;
/* 220 */         if (bufs == null) {
/*     */           return;
/*     */         }
/* 223 */         this.queue.offer(this.buffers.remove(Long.valueOf(idx)));
/*     */       } 
/* 225 */       if (makeDone) {
/* 226 */         this.done = true;
/*     */       }
/* 228 */       drain();
/*     */     }
/*     */     
/*     */     void boundaryError(Disposable observer, Throwable ex) {
/* 232 */       DisposableHelper.dispose(this.upstream);
/* 233 */       this.observers.delete(observer);
/* 234 */       onError(ex);
/*     */     }
/*     */     
/*     */     void drain() {
/* 238 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 242 */       int missed = 1;
/* 243 */       Observer<? super C> a = this.downstream;
/* 244 */       SpscLinkedArrayQueue<C> q = this.queue;
/*     */ 
/*     */       
/*     */       while (true) {
/* 248 */         if (this.cancelled) {
/* 249 */           q.clear();
/*     */           
/*     */           return;
/*     */         } 
/* 253 */         boolean d = this.done;
/* 254 */         if (d && this.errors.get() != null) {
/* 255 */           q.clear();
/* 256 */           Throwable ex = this.errors.terminate();
/* 257 */           a.onError(ex);
/*     */           
/*     */           return;
/*     */         } 
/* 261 */         Collection collection = (Collection)q.poll();
/* 262 */         boolean empty = (collection == null);
/*     */         
/* 264 */         if (d && empty) {
/* 265 */           a.onComplete();
/*     */           
/*     */           return;
/*     */         } 
/* 269 */         if (empty) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 276 */           missed = addAndGet(-missed);
/* 277 */           if (missed == 0)
/*     */             break; 
/*     */           continue;
/*     */         } 
/*     */         a.onNext(collection);
/*     */       } 
/*     */     }
/*     */     
/*     */     static final class BufferOpenObserver<Open>
/*     */       extends AtomicReference<Disposable>
/*     */       implements Observer<Open>, Disposable {
/*     */       private static final long serialVersionUID = -8498650778633225126L;
/*     */       final ObservableBufferBoundary.BufferBoundaryObserver<?, ?, Open, ?> parent;
/*     */       
/*     */       BufferOpenObserver(ObservableBufferBoundary.BufferBoundaryObserver<?, ?, Open, ?> parent) {
/* 292 */         this.parent = parent;
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 297 */         DisposableHelper.setOnce(this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onNext(Open t) {
/* 302 */         this.parent.open(t);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable t) {
/* 307 */         lazySet((Disposable)DisposableHelper.DISPOSED);
/* 308 */         this.parent.boundaryError(this, t);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onComplete() {
/* 313 */         lazySet((Disposable)DisposableHelper.DISPOSED);
/* 314 */         this.parent.openComplete(this);
/*     */       }
/*     */ 
/*     */       
/*     */       public void dispose() {
/* 319 */         DisposableHelper.dispose(this);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean isDisposed() {
/* 324 */         return (get() == DisposableHelper.DISPOSED);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class BufferCloseObserver<T, C extends Collection<? super T>>
/*     */     extends AtomicReference<Disposable>
/*     */     implements Observer<Object>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -8498650778633225126L;
/*     */     
/*     */     final ObservableBufferBoundary.BufferBoundaryObserver<T, C, ?, ?> parent;
/*     */     final long index;
/*     */     
/*     */     BufferCloseObserver(ObservableBufferBoundary.BufferBoundaryObserver<T, C, ?, ?> parent, long index) {
/* 340 */       this.parent = parent;
/* 341 */       this.index = index;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 346 */       DisposableHelper.setOnce(this, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(Object t) {
/* 351 */       Disposable upstream = get();
/* 352 */       if (upstream != DisposableHelper.DISPOSED) {
/* 353 */         lazySet((Disposable)DisposableHelper.DISPOSED);
/* 354 */         upstream.dispose();
/* 355 */         this.parent.close(this, this.index);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 361 */       if (get() != DisposableHelper.DISPOSED) {
/* 362 */         lazySet((Disposable)DisposableHelper.DISPOSED);
/* 363 */         this.parent.boundaryError(this, t);
/*     */       } else {
/* 365 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 371 */       if (get() != DisposableHelper.DISPOSED) {
/* 372 */         lazySet((Disposable)DisposableHelper.DISPOSED);
/* 373 */         this.parent.close(this, this.index);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 379 */       DisposableHelper.dispose(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 384 */       return (get() == DisposableHelper.DISPOSED);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableBufferBoundary.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */