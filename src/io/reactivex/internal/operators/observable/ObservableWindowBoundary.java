/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.queue.MpscLinkedQueue;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.observers.DisposableObserver;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import io.reactivex.subjects.UnicastSubject;
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
/*     */ public final class ObservableWindowBoundary<T, B>
/*     */   extends AbstractObservableWithUpstream<T, Observable<T>>
/*     */ {
/*     */   final ObservableSource<B> other;
/*     */   final int capacityHint;
/*     */   
/*     */   public ObservableWindowBoundary(ObservableSource<T> source, ObservableSource<B> other, int capacityHint) {
/*  32 */     super(source);
/*  33 */     this.other = other;
/*  34 */     this.capacityHint = capacityHint;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super Observable<T>> observer) {
/*  39 */     WindowBoundaryMainObserver<T, B> parent = new WindowBoundaryMainObserver<T, B>(observer, this.capacityHint);
/*     */     
/*  41 */     observer.onSubscribe(parent);
/*  42 */     this.other.subscribe((Observer)parent.boundaryObserver);
/*     */     
/*  44 */     this.source.subscribe(parent);
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
/*     */     final ObservableWindowBoundary.WindowBoundaryInnerObserver<T, B> boundaryObserver;
/*     */     
/*     */     final AtomicReference<Disposable> upstream;
/*     */     
/*     */     final AtomicInteger windows;
/*     */     
/*     */     final MpscLinkedQueue<Object> queue;
/*     */     
/*     */     final AtomicThrowable errors;
/*     */     
/*     */     final AtomicBoolean stopWindows;
/*  69 */     static final Object NEXT_WINDOW = new Object();
/*     */     
/*     */     volatile boolean done;
/*     */     
/*     */     UnicastSubject<T> window;
/*     */     
/*     */     WindowBoundaryMainObserver(Observer<? super Observable<T>> downstream, int capacityHint) {
/*  76 */       this.downstream = downstream;
/*  77 */       this.capacityHint = capacityHint;
/*  78 */       this.boundaryObserver = new ObservableWindowBoundary.WindowBoundaryInnerObserver<T, B>(this);
/*  79 */       this.upstream = new AtomicReference<Disposable>();
/*  80 */       this.windows = new AtomicInteger(1);
/*  81 */       this.queue = new MpscLinkedQueue();
/*  82 */       this.errors = new AtomicThrowable();
/*  83 */       this.stopWindows = new AtomicBoolean();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  88 */       if (DisposableHelper.setOnce(this.upstream, d))
/*     */       {
/*  90 */         innerNext();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  96 */       this.queue.offer(t);
/*  97 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 102 */       this.boundaryObserver.dispose();
/* 103 */       if (this.errors.addThrowable(e)) {
/* 104 */         this.done = true;
/* 105 */         drain();
/*     */       } else {
/* 107 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 113 */       this.boundaryObserver.dispose();
/* 114 */       this.done = true;
/* 115 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 120 */       if (this.stopWindows.compareAndSet(false, true)) {
/* 121 */         this.boundaryObserver.dispose();
/* 122 */         if (this.windows.decrementAndGet() == 0) {
/* 123 */           DisposableHelper.dispose(this.upstream);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 130 */       return this.stopWindows.get();
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 135 */       if (this.windows.decrementAndGet() == 0) {
/* 136 */         DisposableHelper.dispose(this.upstream);
/*     */       }
/*     */     }
/*     */     
/*     */     void innerNext() {
/* 141 */       this.queue.offer(NEXT_WINDOW);
/* 142 */       drain();
/*     */     }
/*     */     
/*     */     void innerError(Throwable e) {
/* 146 */       DisposableHelper.dispose(this.upstream);
/* 147 */       if (this.errors.addThrowable(e)) {
/* 148 */         this.done = true;
/* 149 */         drain();
/*     */       } else {
/* 151 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     void innerComplete() {
/* 156 */       DisposableHelper.dispose(this.upstream);
/* 157 */       this.done = true;
/* 158 */       drain();
/*     */     }
/*     */     
/*     */     void drain()
/*     */     {
/* 163 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 167 */       int missed = 1;
/* 168 */       Observer<? super Observable<T>> downstream = this.downstream;
/* 169 */       MpscLinkedQueue<Object> queue = this.queue;
/* 170 */       AtomicThrowable errors = this.errors;
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 175 */         if (this.windows.get() == 0) {
/* 176 */           queue.clear();
/* 177 */           this.window = null;
/*     */           
/*     */           return;
/*     */         } 
/* 181 */         UnicastSubject<T> w = this.window;
/*     */         
/* 183 */         boolean d = this.done;
/*     */         
/* 185 */         if (d && errors.get() != null) {
/* 186 */           queue.clear();
/* 187 */           Throwable ex = errors.terminate();
/* 188 */           if (w != null) {
/* 189 */             this.window = null;
/* 190 */             w.onError(ex);
/*     */           } 
/* 192 */           downstream.onError(ex);
/*     */           
/*     */           return;
/*     */         } 
/* 196 */         Object v = queue.poll();
/*     */         
/* 198 */         boolean empty = (v == null);
/*     */         
/* 200 */         if (d && empty) {
/* 201 */           Throwable ex = errors.terminate();
/* 202 */           if (ex == null) {
/* 203 */             if (w != null) {
/* 204 */               this.window = null;
/* 205 */               w.onComplete();
/*     */             } 
/* 207 */             downstream.onComplete();
/*     */           } else {
/* 209 */             if (w != null) {
/* 210 */               this.window = null;
/* 211 */               w.onError(ex);
/*     */             } 
/* 213 */             downstream.onError(ex);
/*     */           } 
/*     */           
/*     */           return;
/*     */         } 
/* 218 */         if (empty) {
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
/* 241 */           missed = addAndGet(-missed);
/* 242 */           if (missed == 0)
/*     */             break;  continue;
/*     */         }  if (v != NEXT_WINDOW) {
/*     */           w.onNext(v); continue;
/*     */         }  if (w != null) {
/*     */           this.window = null;
/*     */           w.onComplete();
/*     */         } 
/*     */         if (!this.stopWindows.get()) {
/*     */           w = UnicastSubject.create(this.capacityHint, this);
/*     */           this.window = w;
/*     */           this.windows.getAndIncrement();
/*     */           downstream.onNext(w);
/*     */         } 
/* 256 */       }  } } static final class WindowBoundaryInnerObserver<T, B> extends DisposableObserver<B> { final ObservableWindowBoundary.WindowBoundaryMainObserver<T, B> parent; WindowBoundaryInnerObserver(ObservableWindowBoundary.WindowBoundaryMainObserver<T, B> parent) { this.parent = parent; }
/*     */     
/*     */     boolean done;
/*     */     
/*     */     public void onNext(B t) {
/* 261 */       if (this.done) {
/*     */         return;
/*     */       }
/* 264 */       this.parent.innerNext();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 269 */       if (this.done) {
/* 270 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 273 */       this.done = true;
/* 274 */       this.parent.innerError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 279 */       if (this.done) {
/*     */         return;
/*     */       }
/* 282 */       this.done = true;
/* 283 */       this.parent.innerComplete();
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableWindowBoundary.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */