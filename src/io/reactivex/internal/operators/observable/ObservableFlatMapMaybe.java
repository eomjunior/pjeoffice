/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
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
/*     */ 
/*     */ public final class ObservableFlatMapMaybe<T, R>
/*     */   extends AbstractObservableWithUpstream<T, R>
/*     */ {
/*     */   final Function<? super T, ? extends MaybeSource<? extends R>> mapper;
/*     */   final boolean delayErrors;
/*     */   
/*     */   public ObservableFlatMapMaybe(ObservableSource<T> source, Function<? super T, ? extends MaybeSource<? extends R>> mapper, boolean delayError) {
/*  41 */     super(source);
/*  42 */     this.mapper = mapper;
/*  43 */     this.delayErrors = delayError;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super R> observer) {
/*  48 */     this.source.subscribe(new FlatMapMaybeObserver<T, R>(observer, this.mapper, this.delayErrors));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class FlatMapMaybeObserver<T, R>
/*     */     extends AtomicInteger
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 8600231336733376951L;
/*     */     
/*     */     final Observer<? super R> downstream;
/*     */     
/*     */     final boolean delayErrors;
/*     */     
/*     */     final CompositeDisposable set;
/*     */     
/*     */     final AtomicInteger active;
/*     */     
/*     */     final AtomicThrowable errors;
/*     */     
/*     */     final Function<? super T, ? extends MaybeSource<? extends R>> mapper;
/*     */     
/*     */     final AtomicReference<SpscLinkedArrayQueue<R>> queue;
/*     */     
/*     */     Disposable upstream;
/*     */     
/*     */     volatile boolean cancelled;
/*     */     
/*     */     FlatMapMaybeObserver(Observer<? super R> actual, Function<? super T, ? extends MaybeSource<? extends R>> mapper, boolean delayErrors) {
/*  77 */       this.downstream = actual;
/*  78 */       this.mapper = mapper;
/*  79 */       this.delayErrors = delayErrors;
/*  80 */       this.set = new CompositeDisposable();
/*  81 */       this.errors = new AtomicThrowable();
/*  82 */       this.active = new AtomicInteger(1);
/*  83 */       this.queue = new AtomicReference<SpscLinkedArrayQueue<R>>();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  88 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  89 */         this.upstream = d;
/*     */         
/*  91 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*     */       MaybeSource<? extends R> ms;
/*     */       try {
/* 100 */         ms = (MaybeSource<? extends R>)ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null MaybeSource");
/* 101 */       } catch (Throwable ex) {
/* 102 */         Exceptions.throwIfFatal(ex);
/* 103 */         this.upstream.dispose();
/* 104 */         onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/* 108 */       this.active.getAndIncrement();
/*     */       
/* 110 */       InnerObserver inner = new InnerObserver();
/*     */       
/* 112 */       if (!this.cancelled && this.set.add(inner)) {
/* 113 */         ms.subscribe(inner);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 119 */       this.active.decrementAndGet();
/* 120 */       if (this.errors.addThrowable(t)) {
/* 121 */         if (!this.delayErrors) {
/* 122 */           this.set.dispose();
/*     */         }
/* 124 */         drain();
/*     */       } else {
/* 126 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 132 */       this.active.decrementAndGet();
/* 133 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 138 */       this.cancelled = true;
/* 139 */       this.upstream.dispose();
/* 140 */       this.set.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 145 */       return this.cancelled;
/*     */     }
/*     */     
/*     */     void innerSuccess(InnerObserver inner, R value) {
/* 149 */       this.set.delete(inner);
/* 150 */       if (get() == 0 && compareAndSet(0, 1)) {
/* 151 */         this.downstream.onNext(value);
/*     */         
/* 153 */         boolean d = (this.active.decrementAndGet() == 0);
/* 154 */         SpscLinkedArrayQueue<R> q = this.queue.get();
/*     */         
/* 156 */         if (d && (q == null || q.isEmpty())) {
/* 157 */           Throwable ex = this.errors.terminate();
/* 158 */           if (ex != null) {
/* 159 */             this.downstream.onError(ex);
/*     */           } else {
/* 161 */             this.downstream.onComplete();
/*     */           } 
/*     */           return;
/*     */         } 
/* 165 */         if (decrementAndGet() == 0) {
/*     */           return;
/*     */         }
/*     */       } else {
/* 169 */         SpscLinkedArrayQueue<R> q = getOrCreateQueue();
/* 170 */         synchronized (q) {
/* 171 */           q.offer(value);
/*     */         } 
/* 173 */         this.active.decrementAndGet();
/* 174 */         if (getAndIncrement() != 0) {
/*     */           return;
/*     */         }
/*     */       } 
/* 178 */       drainLoop();
/*     */     }
/*     */     
/*     */     SpscLinkedArrayQueue<R> getOrCreateQueue() {
/*     */       while (true) {
/* 183 */         SpscLinkedArrayQueue<R> current = this.queue.get();
/* 184 */         if (current != null) {
/* 185 */           return current;
/*     */         }
/* 187 */         current = new SpscLinkedArrayQueue(Observable.bufferSize());
/* 188 */         if (this.queue.compareAndSet(null, current)) {
/* 189 */           return current;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     void innerError(InnerObserver inner, Throwable e) {
/* 195 */       this.set.delete(inner);
/* 196 */       if (this.errors.addThrowable(e)) {
/* 197 */         if (!this.delayErrors) {
/* 198 */           this.upstream.dispose();
/* 199 */           this.set.dispose();
/*     */         } 
/* 201 */         this.active.decrementAndGet();
/* 202 */         drain();
/*     */       } else {
/* 204 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     void innerComplete(InnerObserver inner) {
/* 209 */       this.set.delete(inner);
/*     */       
/* 211 */       if (get() == 0 && compareAndSet(0, 1)) {
/* 212 */         boolean d = (this.active.decrementAndGet() == 0);
/* 213 */         SpscLinkedArrayQueue<R> q = this.queue.get();
/*     */         
/* 215 */         if (d && (q == null || q.isEmpty())) {
/* 216 */           Throwable ex = this.errors.terminate();
/* 217 */           if (ex != null) {
/* 218 */             this.downstream.onError(ex);
/*     */           } else {
/* 220 */             this.downstream.onComplete();
/*     */           } 
/*     */           return;
/*     */         } 
/* 224 */         if (decrementAndGet() == 0) {
/*     */           return;
/*     */         }
/* 227 */         drainLoop();
/*     */       } else {
/* 229 */         this.active.decrementAndGet();
/* 230 */         drain();
/*     */       } 
/*     */     }
/*     */     
/*     */     void drain() {
/* 235 */       if (getAndIncrement() == 0) {
/* 236 */         drainLoop();
/*     */       }
/*     */     }
/*     */     
/*     */     void clear() {
/* 241 */       SpscLinkedArrayQueue<R> q = this.queue.get();
/* 242 */       if (q != null) {
/* 243 */         q.clear();
/*     */       }
/*     */     }
/*     */     
/*     */     void drainLoop() {
/* 248 */       int missed = 1;
/* 249 */       Observer<? super R> a = this.downstream;
/* 250 */       AtomicInteger n = this.active;
/* 251 */       AtomicReference<SpscLinkedArrayQueue<R>> qr = this.queue;
/*     */ 
/*     */       
/*     */       while (true) {
/* 255 */         if (this.cancelled) {
/* 256 */           clear();
/*     */           
/*     */           return;
/*     */         } 
/* 260 */         if (!this.delayErrors) {
/* 261 */           Throwable ex = (Throwable)this.errors.get();
/* 262 */           if (ex != null) {
/* 263 */             ex = this.errors.terminate();
/* 264 */             clear();
/* 265 */             a.onError(ex);
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 270 */         boolean d = (n.get() == 0);
/* 271 */         SpscLinkedArrayQueue<R> q = qr.get();
/* 272 */         R v = (q != null) ? (R)q.poll() : null;
/* 273 */         boolean empty = (v == null);
/*     */         
/* 275 */         if (d && empty) {
/* 276 */           Throwable ex = this.errors.terminate();
/* 277 */           if (ex != null) {
/* 278 */             a.onError(ex);
/*     */           } else {
/* 280 */             a.onComplete();
/*     */           } 
/*     */           
/*     */           return;
/*     */         } 
/* 285 */         if (empty) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 292 */           missed = addAndGet(-missed);
/* 293 */           if (missed == 0)
/*     */             break; 
/*     */           continue;
/*     */         } 
/*     */         a.onNext(v);
/*     */       } 
/*     */     }
/*     */     
/*     */     final class InnerObserver extends AtomicReference<Disposable> implements MaybeObserver<R>, Disposable {
/*     */       private static final long serialVersionUID = -502562646270949838L;
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 305 */         DisposableHelper.setOnce(this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSuccess(R value) {
/* 310 */         ObservableFlatMapMaybe.FlatMapMaybeObserver.this.innerSuccess(this, value);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 315 */         ObservableFlatMapMaybe.FlatMapMaybeObserver.this.innerError(this, e);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onComplete() {
/* 320 */         ObservableFlatMapMaybe.FlatMapMaybeObserver.this.innerComplete(this);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean isDisposed() {
/* 325 */         return DisposableHelper.isDisposed(get());
/*     */       }
/*     */ 
/*     */       
/*     */       public void dispose() {
/* 330 */         DisposableHelper.dispose(this);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableFlatMapMaybe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */