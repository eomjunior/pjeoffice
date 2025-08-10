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
/*     */ import io.reactivex.internal.fuseable.SimplePlainQueue;
/*     */ import io.reactivex.internal.observers.QueueDrainObserver;
/*     */ import io.reactivex.internal.queue.MpscLinkedQueue;
/*     */ import io.reactivex.internal.util.NotificationLite;
/*     */ import io.reactivex.observers.DisposableObserver;
/*     */ import io.reactivex.observers.SerializedObserver;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import io.reactivex.subjects.UnicastSubject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicLong;
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
/*     */ public final class ObservableWindowBoundarySelector<T, B, V>
/*     */   extends AbstractObservableWithUpstream<T, Observable<T>>
/*     */ {
/*     */   final ObservableSource<B> open;
/*     */   final Function<? super B, ? extends ObservableSource<V>> close;
/*     */   final int bufferSize;
/*     */   
/*     */   public ObservableWindowBoundarySelector(ObservableSource<T> source, ObservableSource<B> open, Function<? super B, ? extends ObservableSource<V>> close, int bufferSize) {
/*  43 */     super(source);
/*  44 */     this.open = open;
/*  45 */     this.close = close;
/*  46 */     this.bufferSize = bufferSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super Observable<T>> t) {
/*  51 */     this.source.subscribe((Observer)new WindowBoundaryMainObserver<Object, B, V>((Observer<? super Observable<?>>)new SerializedObserver(t), this.open, this.close, this.bufferSize));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class WindowBoundaryMainObserver<T, B, V>
/*     */     extends QueueDrainObserver<T, Object, Observable<T>>
/*     */     implements Disposable
/*     */   {
/*     */     final ObservableSource<B> open;
/*     */     
/*     */     final Function<? super B, ? extends ObservableSource<V>> close;
/*     */     
/*     */     final int bufferSize;
/*     */     final CompositeDisposable resources;
/*     */     Disposable upstream;
/*  66 */     final AtomicReference<Disposable> boundary = new AtomicReference<Disposable>();
/*     */     
/*     */     final List<UnicastSubject<T>> ws;
/*     */     
/*  70 */     final AtomicLong windows = new AtomicLong();
/*     */     
/*  72 */     final AtomicBoolean stopWindows = new AtomicBoolean();
/*     */ 
/*     */     
/*     */     WindowBoundaryMainObserver(Observer<? super Observable<T>> actual, ObservableSource<B> open, Function<? super B, ? extends ObservableSource<V>> close, int bufferSize) {
/*  76 */       super(actual, (SimplePlainQueue)new MpscLinkedQueue());
/*  77 */       this.open = open;
/*  78 */       this.close = close;
/*  79 */       this.bufferSize = bufferSize;
/*  80 */       this.resources = new CompositeDisposable();
/*  81 */       this.ws = new ArrayList<UnicastSubject<T>>();
/*  82 */       this.windows.lazySet(1L);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  87 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  88 */         this.upstream = d;
/*     */         
/*  90 */         this.downstream.onSubscribe(this);
/*     */         
/*  92 */         if (this.stopWindows.get()) {
/*     */           return;
/*     */         }
/*     */         
/*  96 */         ObservableWindowBoundarySelector.OperatorWindowBoundaryOpenObserver<T, B> os = new ObservableWindowBoundarySelector.OperatorWindowBoundaryOpenObserver<T, B>(this);
/*     */         
/*  98 */         if (this.boundary.compareAndSet(null, os)) {
/*  99 */           this.open.subscribe((Observer)os);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 106 */       if (fastEnter()) {
/* 107 */         for (UnicastSubject<T> w : this.ws) {
/* 108 */           w.onNext(t);
/*     */         }
/* 110 */         if (leave(-1) == 0) {
/*     */           return;
/*     */         }
/*     */       } else {
/* 114 */         this.queue.offer(NotificationLite.next(t));
/* 115 */         if (!enter()) {
/*     */           return;
/*     */         }
/*     */       } 
/* 119 */       drainLoop();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 124 */       if (this.done) {
/* 125 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 128 */       this.error = t;
/* 129 */       this.done = true;
/*     */       
/* 131 */       if (enter()) {
/* 132 */         drainLoop();
/*     */       }
/*     */       
/* 135 */       if (this.windows.decrementAndGet() == 0L) {
/* 136 */         this.resources.dispose();
/*     */       }
/*     */       
/* 139 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 144 */       if (this.done) {
/*     */         return;
/*     */       }
/* 147 */       this.done = true;
/*     */       
/* 149 */       if (enter()) {
/* 150 */         drainLoop();
/*     */       }
/*     */       
/* 153 */       if (this.windows.decrementAndGet() == 0L) {
/* 154 */         this.resources.dispose();
/*     */       }
/*     */       
/* 157 */       this.downstream.onComplete();
/*     */     }
/*     */     
/*     */     void error(Throwable t) {
/* 161 */       this.upstream.dispose();
/* 162 */       this.resources.dispose();
/* 163 */       onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 168 */       if (this.stopWindows.compareAndSet(false, true)) {
/* 169 */         DisposableHelper.dispose(this.boundary);
/* 170 */         if (this.windows.decrementAndGet() == 0L) {
/* 171 */           this.upstream.dispose();
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 178 */       return this.stopWindows.get();
/*     */     }
/*     */     
/*     */     void disposeBoundary() {
/* 182 */       this.resources.dispose();
/* 183 */       DisposableHelper.dispose(this.boundary);
/*     */     }
/*     */     
/*     */     void drainLoop() {
/* 187 */       MpscLinkedQueue<Object> q = (MpscLinkedQueue<Object>)this.queue;
/* 188 */       Observer<? super Observable<T>> a = this.downstream;
/* 189 */       List<UnicastSubject<T>> ws = this.ws;
/* 190 */       int missed = 1;
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 195 */         boolean d = this.done;
/*     */         
/* 197 */         Object o = q.poll();
/*     */         
/* 199 */         boolean empty = (o == null);
/*     */         
/* 201 */         if (d && empty) {
/* 202 */           disposeBoundary();
/* 203 */           Throwable e = this.error;
/* 204 */           if (e != null) {
/* 205 */             for (UnicastSubject<T> w : ws) {
/* 206 */               w.onError(e);
/*     */             }
/*     */           } else {
/* 209 */             for (UnicastSubject<T> w : ws) {
/* 210 */               w.onComplete();
/*     */             }
/*     */           } 
/* 213 */           ws.clear();
/*     */           
/*     */           return;
/*     */         } 
/* 217 */         if (empty)
/*     */         
/*     */         { 
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
/* 274 */           missed = leave(-missed);
/* 275 */           if (missed == 0)
/*     */             break;  continue; }  if (o instanceof ObservableWindowBoundarySelector.WindowOperation) { ObservableSource<V> p; ObservableWindowBoundarySelector.WindowOperation<T, B> wo = (ObservableWindowBoundarySelector.WindowOperation<T, B>)o; UnicastSubject<T> w = wo.w; if (w != null) { if (ws.remove(wo.w)) { wo.w.onComplete(); if (this.windows.decrementAndGet() == 0L) { disposeBoundary(); return; }
/*     */                }
/*     */              continue; }
/*     */            if (this.stopWindows.get())
/*     */             continue;  w = UnicastSubject.create(this.bufferSize); ws.add(w); a.onNext(w); try { p = (ObservableSource<V>)ObjectHelper.requireNonNull(this.close.apply(wo.open), "The ObservableSource supplied is null"); }
/*     */           catch (Throwable e) { Exceptions.throwIfFatal(e); this.stopWindows.set(true); a.onError(e); continue; }
/*     */            ObservableWindowBoundarySelector.OperatorWindowBoundaryCloseObserver<T, V> cl = new ObservableWindowBoundarySelector.OperatorWindowBoundaryCloseObserver<T, V>(this, w); if (this.resources.add((Disposable)cl)) { this.windows.getAndIncrement(); p.subscribe((Observer)cl); }
/*     */            continue; }
/*     */          for (UnicastSubject<T> w : ws)
/*     */           w.onNext(NotificationLite.getValue(o)); 
/* 286 */       }  } void open(B b) { this.queue.offer(new ObservableWindowBoundarySelector.WindowOperation<Object, B>(null, b));
/* 287 */       if (enter())
/* 288 */         drainLoop();  }
/*     */     
/*     */     public void accept(Observer<? super Observable<T>> a, Object v) {}
/*     */     
/*     */     void close(ObservableWindowBoundarySelector.OperatorWindowBoundaryCloseObserver<T, V> w) {
/* 293 */       this.resources.delete((Disposable)w);
/* 294 */       this.queue.offer(new ObservableWindowBoundarySelector.WindowOperation<T, Object>(w.w, null));
/* 295 */       if (enter())
/* 296 */         drainLoop(); 
/*     */     }
/*     */   }
/*     */   
/*     */   static final class WindowOperation<T, B> {
/*     */     final UnicastSubject<T> w;
/*     */     final B open;
/*     */     
/*     */     WindowOperation(UnicastSubject<T> w, B open) {
/* 305 */       this.w = w;
/* 306 */       this.open = open;
/*     */     }
/*     */   }
/*     */   
/*     */   static final class OperatorWindowBoundaryOpenObserver<T, B> extends DisposableObserver<B> {
/*     */     final ObservableWindowBoundarySelector.WindowBoundaryMainObserver<T, B, ?> parent;
/*     */     
/*     */     OperatorWindowBoundaryOpenObserver(ObservableWindowBoundarySelector.WindowBoundaryMainObserver<T, B, ?> parent) {
/* 314 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(B t) {
/* 319 */       this.parent.open(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 324 */       this.parent.error(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 329 */       this.parent.onComplete();
/*     */     }
/*     */   }
/*     */   
/*     */   static final class OperatorWindowBoundaryCloseObserver<T, V>
/*     */     extends DisposableObserver<V> {
/*     */     final ObservableWindowBoundarySelector.WindowBoundaryMainObserver<T, ?, V> parent;
/*     */     final UnicastSubject<T> w;
/*     */     boolean done;
/*     */     
/*     */     OperatorWindowBoundaryCloseObserver(ObservableWindowBoundarySelector.WindowBoundaryMainObserver<T, ?, V> parent, UnicastSubject<T> w) {
/* 340 */       this.parent = parent;
/* 341 */       this.w = w;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(V t) {
/* 346 */       dispose();
/* 347 */       onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 352 */       if (this.done) {
/* 353 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 356 */       this.done = true;
/* 357 */       this.parent.error(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 362 */       if (this.done) {
/*     */         return;
/*     */       }
/* 365 */       this.done = true;
/* 366 */       this.parent.close(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableWindowBoundarySelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */