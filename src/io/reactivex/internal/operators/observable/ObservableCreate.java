/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.ObservableEmitter;
/*     */ import io.reactivex.ObservableOnSubscribe;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Cancellable;
/*     */ import io.reactivex.internal.disposables.CancellableDisposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
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
/*     */ public final class ObservableCreate<T>
/*     */   extends Observable<T>
/*     */ {
/*     */   final ObservableOnSubscribe<T> source;
/*     */   
/*     */   public ObservableCreate(ObservableOnSubscribe<T> source) {
/*  31 */     this.source = source;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super T> observer) {
/*  36 */     CreateEmitter<T> parent = new CreateEmitter<T>(observer);
/*  37 */     observer.onSubscribe(parent);
/*     */     
/*     */     try {
/*  40 */       this.source.subscribe(parent);
/*  41 */     } catch (Throwable ex) {
/*  42 */       Exceptions.throwIfFatal(ex);
/*  43 */       parent.onError(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static final class CreateEmitter<T>
/*     */     extends AtomicReference<Disposable>
/*     */     implements ObservableEmitter<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -3434801548987643227L;
/*     */     final Observer<? super T> observer;
/*     */     
/*     */     CreateEmitter(Observer<? super T> observer) {
/*  56 */       this.observer = observer;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  61 */       if (t == null) {
/*  62 */         onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
/*     */         return;
/*     */       } 
/*  65 */       if (!isDisposed()) {
/*  66 */         this.observer.onNext(t);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  72 */       if (!tryOnError(t)) {
/*  73 */         RxJavaPlugins.onError(t);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryOnError(Throwable t) {
/*  79 */       if (t == null) {
/*  80 */         t = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
/*     */       }
/*  82 */       if (!isDisposed()) {
/*     */         try {
/*  84 */           this.observer.onError(t);
/*     */         } finally {
/*  86 */           dispose();
/*     */         } 
/*  88 */         return true;
/*     */       } 
/*  90 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  95 */       if (!isDisposed()) {
/*     */         try {
/*  97 */           this.observer.onComplete();
/*     */         } finally {
/*  99 */           dispose();
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void setDisposable(Disposable d) {
/* 106 */       DisposableHelper.set(this, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setCancellable(Cancellable c) {
/* 111 */       setDisposable((Disposable)new CancellableDisposable(c));
/*     */     }
/*     */ 
/*     */     
/*     */     public ObservableEmitter<T> serialize() {
/* 116 */       return new ObservableCreate.SerializedEmitter<T>(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 121 */       DisposableHelper.dispose(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 126 */       return DisposableHelper.isDisposed(get());
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 131 */       return String.format("%s{%s}", new Object[] { getClass().getSimpleName(), super.toString() });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class SerializedEmitter<T>
/*     */     extends AtomicInteger
/*     */     implements ObservableEmitter<T>
/*     */   {
/*     */     private static final long serialVersionUID = 4883307006032401862L;
/*     */ 
/*     */     
/*     */     final ObservableEmitter<T> emitter;
/*     */ 
/*     */     
/*     */     final AtomicThrowable error;
/*     */     
/*     */     final SpscLinkedArrayQueue<T> queue;
/*     */     
/*     */     volatile boolean done;
/*     */ 
/*     */     
/*     */     SerializedEmitter(ObservableEmitter<T> emitter) {
/* 155 */       this.emitter = emitter;
/* 156 */       this.error = new AtomicThrowable();
/* 157 */       this.queue = new SpscLinkedArrayQueue(16);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 162 */       if (this.emitter.isDisposed() || this.done) {
/*     */         return;
/*     */       }
/* 165 */       if (t == null) {
/* 166 */         onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
/*     */         return;
/*     */       } 
/* 169 */       if (get() == 0 && compareAndSet(0, 1)) {
/* 170 */         this.emitter.onNext(t);
/* 171 */         if (decrementAndGet() == 0) {
/*     */           return;
/*     */         }
/*     */       } else {
/* 175 */         SpscLinkedArrayQueue<T> spscLinkedArrayQueue = this.queue;
/* 176 */         synchronized (spscLinkedArrayQueue) {
/* 177 */           spscLinkedArrayQueue.offer(t);
/*     */         } 
/* 179 */         if (getAndIncrement() != 0) {
/*     */           return;
/*     */         }
/*     */       } 
/* 183 */       drainLoop();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 188 */       if (!tryOnError(t)) {
/* 189 */         RxJavaPlugins.onError(t);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryOnError(Throwable t) {
/* 195 */       if (this.emitter.isDisposed() || this.done) {
/* 196 */         return false;
/*     */       }
/* 198 */       if (t == null) {
/* 199 */         t = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
/*     */       }
/* 201 */       if (this.error.addThrowable(t)) {
/* 202 */         this.done = true;
/* 203 */         drain();
/* 204 */         return true;
/*     */       } 
/* 206 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 211 */       if (this.emitter.isDisposed() || this.done) {
/*     */         return;
/*     */       }
/* 214 */       this.done = true;
/* 215 */       drain();
/*     */     }
/*     */     
/*     */     void drain() {
/* 219 */       if (getAndIncrement() == 0) {
/* 220 */         drainLoop();
/*     */       }
/*     */     }
/*     */     
/*     */     void drainLoop() {
/* 225 */       ObservableEmitter<T> e = this.emitter;
/* 226 */       SpscLinkedArrayQueue<T> q = this.queue;
/* 227 */       AtomicThrowable error = this.error;
/* 228 */       int missed = 1;
/*     */ 
/*     */       
/*     */       while (true) {
/* 232 */         if (e.isDisposed()) {
/* 233 */           q.clear();
/*     */           
/*     */           return;
/*     */         } 
/* 237 */         if (error.get() != null) {
/* 238 */           q.clear();
/* 239 */           e.onError(error.terminate());
/*     */           
/*     */           return;
/*     */         } 
/* 243 */         boolean d = this.done;
/* 244 */         T v = (T)q.poll();
/*     */         
/* 246 */         boolean empty = (v == null);
/*     */         
/* 248 */         if (d && empty) {
/* 249 */           e.onComplete();
/*     */           
/*     */           return;
/*     */         } 
/* 253 */         if (empty) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 260 */           missed = addAndGet(-missed);
/* 261 */           if (missed == 0)
/*     */             break; 
/*     */           continue;
/*     */         } 
/*     */         e.onNext(v);
/*     */       } 
/*     */     }
/*     */     public void setDisposable(Disposable d) {
/* 269 */       this.emitter.setDisposable(d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setCancellable(Cancellable c) {
/* 274 */       this.emitter.setCancellable(c);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 279 */       return this.emitter.isDisposed();
/*     */     }
/*     */ 
/*     */     
/*     */     public ObservableEmitter<T> serialize() {
/* 284 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 289 */       return this.emitter.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableCreate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */