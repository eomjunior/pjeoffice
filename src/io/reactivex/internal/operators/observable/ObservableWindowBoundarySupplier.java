/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.queue.MpscLinkedQueue;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.observers.DisposableObserver;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import io.reactivex.subjects.UnicastSubject;
/*     */ import java.util.concurrent.Callable;
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
/*     */ 
/*     */ public final class ObservableWindowBoundarySupplier<T, B>
/*     */   extends AbstractObservableWithUpstream<T, Observable<T>>
/*     */ {
/*     */   final Callable<? extends ObservableSource<B>> other;
/*     */   final int capacityHint;
/*     */   
/*     */   public ObservableWindowBoundarySupplier(ObservableSource<T> source, Callable<? extends ObservableSource<B>> other, int capacityHint) {
/*  37 */     super(source);
/*  38 */     this.other = other;
/*  39 */     this.capacityHint = capacityHint;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super Observable<T>> observer) {
/*  44 */     WindowBoundaryMainObserver<T, B> parent = new WindowBoundaryMainObserver<T, B>(observer, this.capacityHint, this.other);
/*     */     
/*  46 */     this.source.subscribe(parent);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class WindowBoundaryMainObserver<T, B>
/*     */     extends AtomicInteger
/*     */     implements Observer<T>, Disposable, Runnable
/*     */   {
/*     */     private static final long serialVersionUID = 2233020065421370272L;
/*     */     
/*     */     final Observer<? super Observable<T>> downstream;
/*     */     
/*     */     final int capacityHint;
/*     */     
/*     */     final AtomicReference<ObservableWindowBoundarySupplier.WindowBoundaryInnerObserver<T, B>> boundaryObserver;
/*  61 */     static final ObservableWindowBoundarySupplier.WindowBoundaryInnerObserver<Object, Object> BOUNDARY_DISPOSED = new ObservableWindowBoundarySupplier.WindowBoundaryInnerObserver<Object, Object>(null);
/*     */     
/*     */     final AtomicInteger windows;
/*     */     
/*     */     final MpscLinkedQueue<Object> queue;
/*     */     
/*     */     final AtomicThrowable errors;
/*     */     
/*     */     final AtomicBoolean stopWindows;
/*     */     
/*     */     final Callable<? extends ObservableSource<B>> other;
/*     */     
/*  73 */     static final Object NEXT_WINDOW = new Object();
/*     */     
/*     */     Disposable upstream;
/*     */     
/*     */     volatile boolean done;
/*     */     
/*     */     UnicastSubject<T> window;
/*     */     
/*     */     WindowBoundaryMainObserver(Observer<? super Observable<T>> downstream, int capacityHint, Callable<? extends ObservableSource<B>> other) {
/*  82 */       this.downstream = downstream;
/*  83 */       this.capacityHint = capacityHint;
/*  84 */       this.boundaryObserver = new AtomicReference<ObservableWindowBoundarySupplier.WindowBoundaryInnerObserver<T, B>>();
/*  85 */       this.windows = new AtomicInteger(1);
/*  86 */       this.queue = new MpscLinkedQueue();
/*  87 */       this.errors = new AtomicThrowable();
/*  88 */       this.stopWindows = new AtomicBoolean();
/*  89 */       this.other = other;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  94 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  95 */         this.upstream = d;
/*  96 */         this.downstream.onSubscribe(this);
/*  97 */         this.queue.offer(NEXT_WINDOW);
/*  98 */         drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 104 */       this.queue.offer(t);
/* 105 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 110 */       disposeBoundary();
/* 111 */       if (this.errors.addThrowable(e)) {
/* 112 */         this.done = true;
/* 113 */         drain();
/*     */       } else {
/* 115 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 121 */       disposeBoundary();
/* 122 */       this.done = true;
/* 123 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 128 */       if (this.stopWindows.compareAndSet(false, true)) {
/* 129 */         disposeBoundary();
/* 130 */         if (this.windows.decrementAndGet() == 0) {
/* 131 */           this.upstream.dispose();
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     void disposeBoundary() {
/* 138 */       Disposable d = (Disposable)this.boundaryObserver.getAndSet(BOUNDARY_DISPOSED);
/* 139 */       if (d != null && d != BOUNDARY_DISPOSED) {
/* 140 */         d.dispose();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 146 */       return this.stopWindows.get();
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 151 */       if (this.windows.decrementAndGet() == 0) {
/* 152 */         this.upstream.dispose();
/*     */       }
/*     */     }
/*     */     
/*     */     void innerNext(ObservableWindowBoundarySupplier.WindowBoundaryInnerObserver<T, B> sender) {
/* 157 */       this.boundaryObserver.compareAndSet(sender, null);
/* 158 */       this.queue.offer(NEXT_WINDOW);
/* 159 */       drain();
/*     */     }
/*     */     
/*     */     void innerError(Throwable e) {
/* 163 */       this.upstream.dispose();
/* 164 */       if (this.errors.addThrowable(e)) {
/* 165 */         this.done = true;
/* 166 */         drain();
/*     */       } else {
/* 168 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     void innerComplete() {
/* 173 */       this.upstream.dispose();
/* 174 */       this.done = true;
/* 175 */       drain();
/*     */     }
/*     */     
/*     */     void drain()
/*     */     {
/* 180 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 184 */       int missed = 1;
/* 185 */       Observer<? super Observable<T>> downstream = this.downstream;
/* 186 */       MpscLinkedQueue<Object> queue = this.queue;
/* 187 */       AtomicThrowable errors = this.errors;
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 192 */         if (this.windows.get() == 0) {
/* 193 */           queue.clear();
/* 194 */           this.window = null;
/*     */           
/*     */           return;
/*     */         } 
/* 198 */         UnicastSubject<T> w = this.window;
/*     */         
/* 200 */         boolean d = this.done;
/*     */         
/* 202 */         if (d && errors.get() != null) {
/* 203 */           queue.clear();
/* 204 */           Throwable ex = errors.terminate();
/* 205 */           if (w != null) {
/* 206 */             this.window = null;
/* 207 */             w.onError(ex);
/*     */           } 
/* 209 */           downstream.onError(ex);
/*     */           
/*     */           return;
/*     */         } 
/* 213 */         Object v = queue.poll();
/*     */         
/* 215 */         boolean empty = (v == null);
/*     */         
/* 217 */         if (d && empty) {
/* 218 */           Throwable ex = errors.terminate();
/* 219 */           if (ex == null) {
/* 220 */             if (w != null) {
/* 221 */               this.window = null;
/* 222 */               w.onComplete();
/*     */             } 
/* 224 */             downstream.onComplete();
/*     */           } else {
/* 226 */             if (w != null) {
/* 227 */               this.window = null;
/* 228 */               w.onError(ex);
/*     */             } 
/* 230 */             downstream.onError(ex);
/*     */           } 
/*     */           
/*     */           return;
/*     */         } 
/* 235 */         if (empty) {
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
/* 275 */           missed = addAndGet(-missed);
/* 276 */           if (missed == 0)
/*     */             break;  continue;
/*     */         }  if (v != NEXT_WINDOW) { w.onNext(v); continue; }
/*     */          if (w != null) { this.window = null; w.onComplete(); }
/*     */          if (!this.stopWindows.get()) {
/*     */           ObservableSource<B> otherSource; w = UnicastSubject.create(this.capacityHint, this); this.window = w; this.windows.getAndIncrement(); try {
/*     */             otherSource = (ObservableSource<B>)ObjectHelper.requireNonNull(this.other.call(), "The other Callable returned a null ObservableSource");
/*     */           } catch (Throwable ex) {
/*     */             Exceptions.throwIfFatal(ex); errors.addThrowable(ex); this.done = true; continue;
/*     */           }  ObservableWindowBoundarySupplier.WindowBoundaryInnerObserver<T, B> bo = new ObservableWindowBoundarySupplier.WindowBoundaryInnerObserver<T, B>(this); if (this.boundaryObserver.compareAndSet(null, bo)) {
/*     */             otherSource.subscribe((Observer)bo); downstream.onNext(w);
/*     */           } 
/*     */         } 
/* 289 */       }  } } static final class WindowBoundaryInnerObserver<T, B> extends DisposableObserver<B> { final ObservableWindowBoundarySupplier.WindowBoundaryMainObserver<T, B> parent; WindowBoundaryInnerObserver(ObservableWindowBoundarySupplier.WindowBoundaryMainObserver<T, B> parent) { this.parent = parent; }
/*     */     
/*     */     boolean done;
/*     */     
/*     */     public void onNext(B t) {
/* 294 */       if (this.done) {
/*     */         return;
/*     */       }
/* 297 */       this.done = true;
/* 298 */       dispose();
/* 299 */       this.parent.innerNext(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 304 */       if (this.done) {
/* 305 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 308 */       this.done = true;
/* 309 */       this.parent.innerError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 314 */       if (this.done) {
/*     */         return;
/*     */       }
/* 317 */       this.done = true;
/* 318 */       this.parent.innerComplete();
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableWindowBoundarySupplier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */