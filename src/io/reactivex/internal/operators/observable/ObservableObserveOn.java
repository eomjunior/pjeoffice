/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.fuseable.QueueDisposable;
/*     */ import io.reactivex.internal.fuseable.SimpleQueue;
/*     */ import io.reactivex.internal.observers.BasicIntQueueDisposable;
/*     */ import io.reactivex.internal.queue.SpscLinkedArrayQueue;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ObservableObserveOn<T>
/*     */   extends AbstractObservableWithUpstream<T, T>
/*     */ {
/*     */   final Scheduler scheduler;
/*     */   final boolean delayError;
/*     */   final int bufferSize;
/*     */   
/*     */   public ObservableObserveOn(ObservableSource<T> source, Scheduler scheduler, boolean delayError, int bufferSize) {
/*  32 */     super(source);
/*  33 */     this.scheduler = scheduler;
/*  34 */     this.delayError = delayError;
/*  35 */     this.bufferSize = bufferSize;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super T> observer) {
/*  40 */     if (this.scheduler instanceof io.reactivex.internal.schedulers.TrampolineScheduler) {
/*  41 */       this.source.subscribe(observer);
/*     */     } else {
/*  43 */       Scheduler.Worker w = this.scheduler.createWorker();
/*     */       
/*  45 */       this.source.subscribe(new ObserveOnObserver<T>(observer, w, this.delayError, this.bufferSize));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ObserveOnObserver<T>
/*     */     extends BasicIntQueueDisposable<T>
/*     */     implements Observer<T>, Runnable
/*     */   {
/*     */     private static final long serialVersionUID = 6576896619930983584L;
/*     */     
/*     */     final Observer<? super T> downstream;
/*     */     
/*     */     final Scheduler.Worker worker;
/*     */     
/*     */     final boolean delayError;
/*     */     
/*     */     final int bufferSize;
/*     */     SimpleQueue<T> queue;
/*     */     Disposable upstream;
/*     */     Throwable error;
/*     */     volatile boolean done;
/*     */     volatile boolean disposed;
/*     */     int sourceMode;
/*     */     boolean outputFused;
/*     */     
/*     */     ObserveOnObserver(Observer<? super T> actual, Scheduler.Worker worker, boolean delayError, int bufferSize) {
/*  72 */       this.downstream = actual;
/*  73 */       this.worker = worker;
/*  74 */       this.delayError = delayError;
/*  75 */       this.bufferSize = bufferSize;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  80 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  81 */         this.upstream = d;
/*  82 */         if (d instanceof QueueDisposable) {
/*     */           
/*  84 */           QueueDisposable<T> qd = (QueueDisposable<T>)d;
/*     */           
/*  86 */           int m = qd.requestFusion(7);
/*     */           
/*  88 */           if (m == 1) {
/*  89 */             this.sourceMode = m;
/*  90 */             this.queue = (SimpleQueue<T>)qd;
/*  91 */             this.done = true;
/*  92 */             this.downstream.onSubscribe((Disposable)this);
/*  93 */             schedule();
/*     */             return;
/*     */           } 
/*  96 */           if (m == 2) {
/*  97 */             this.sourceMode = m;
/*  98 */             this.queue = (SimpleQueue<T>)qd;
/*  99 */             this.downstream.onSubscribe((Disposable)this);
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 104 */         this.queue = (SimpleQueue<T>)new SpscLinkedArrayQueue(this.bufferSize);
/*     */         
/* 106 */         this.downstream.onSubscribe((Disposable)this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 112 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       
/* 116 */       if (this.sourceMode != 2) {
/* 117 */         this.queue.offer(t);
/*     */       }
/* 119 */       schedule();
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
/* 130 */       schedule();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 135 */       if (this.done) {
/*     */         return;
/*     */       }
/* 138 */       this.done = true;
/* 139 */       schedule();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 144 */       if (!this.disposed) {
/* 145 */         this.disposed = true;
/* 146 */         this.upstream.dispose();
/* 147 */         this.worker.dispose();
/* 148 */         if (!this.outputFused && getAndIncrement() == 0) {
/* 149 */           this.queue.clear();
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 156 */       return this.disposed;
/*     */     }
/*     */     
/*     */     void schedule() {
/* 160 */       if (getAndIncrement() == 0) {
/* 161 */         this.worker.schedule(this);
/*     */       }
/*     */     }
/*     */     
/*     */     void drainNormal() {
/* 166 */       int missed = 1;
/*     */       
/* 168 */       SimpleQueue<T> q = this.queue;
/* 169 */       Observer<? super T> a = this.downstream;
/*     */       
/*     */       do {
/* 172 */         if (checkTerminated(this.done, q.isEmpty(), a)) {
/*     */           return;
/*     */         }
/*     */         while (true) {
/*     */           T v;
/* 177 */           boolean d = this.done;
/*     */ 
/*     */           
/*     */           try {
/* 181 */             v = (T)q.poll();
/* 182 */           } catch (Throwable ex) {
/* 183 */             Exceptions.throwIfFatal(ex);
/* 184 */             this.disposed = true;
/* 185 */             this.upstream.dispose();
/* 186 */             q.clear();
/* 187 */             a.onError(ex);
/* 188 */             this.worker.dispose();
/*     */             return;
/*     */           } 
/* 191 */           boolean empty = (v == null);
/*     */           
/* 193 */           if (checkTerminated(d, empty, a)) {
/*     */             return;
/*     */           }
/*     */           
/* 197 */           if (empty) {
/*     */             break;
/*     */           }
/*     */           
/* 201 */           a.onNext(v);
/*     */         } 
/*     */         
/* 204 */         missed = addAndGet(-missed);
/* 205 */       } while (missed != 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void drainFused() {
/* 212 */       int missed = 1;
/*     */       
/*     */       do {
/* 215 */         if (this.disposed) {
/*     */           return;
/*     */         }
/*     */         
/* 219 */         boolean d = this.done;
/* 220 */         Throwable ex = this.error;
/*     */         
/* 222 */         if (!this.delayError && d && ex != null) {
/* 223 */           this.disposed = true;
/* 224 */           this.downstream.onError(this.error);
/* 225 */           this.worker.dispose();
/*     */           
/*     */           return;
/*     */         } 
/* 229 */         this.downstream.onNext(null);
/*     */         
/* 231 */         if (d) {
/* 232 */           this.disposed = true;
/* 233 */           ex = this.error;
/* 234 */           if (ex != null) {
/* 235 */             this.downstream.onError(ex);
/*     */           } else {
/* 237 */             this.downstream.onComplete();
/*     */           } 
/* 239 */           this.worker.dispose();
/*     */           
/*     */           return;
/*     */         } 
/* 243 */         missed = addAndGet(-missed);
/* 244 */       } while (missed != 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/* 252 */       if (this.outputFused) {
/* 253 */         drainFused();
/*     */       } else {
/* 255 */         drainNormal();
/*     */       } 
/*     */     }
/*     */     
/*     */     boolean checkTerminated(boolean d, boolean empty, Observer<? super T> a) {
/* 260 */       if (this.disposed) {
/* 261 */         this.queue.clear();
/* 262 */         return true;
/*     */       } 
/* 264 */       if (d) {
/* 265 */         Throwable e = this.error;
/* 266 */         if (this.delayError) {
/* 267 */           if (empty) {
/* 268 */             this.disposed = true;
/* 269 */             if (e != null) {
/* 270 */               a.onError(e);
/*     */             } else {
/* 272 */               a.onComplete();
/*     */             } 
/* 274 */             this.worker.dispose();
/* 275 */             return true;
/*     */           } 
/*     */         } else {
/* 278 */           if (e != null) {
/* 279 */             this.disposed = true;
/* 280 */             this.queue.clear();
/* 281 */             a.onError(e);
/* 282 */             this.worker.dispose();
/* 283 */             return true;
/*     */           } 
/* 285 */           if (empty) {
/* 286 */             this.disposed = true;
/* 287 */             a.onComplete();
/* 288 */             this.worker.dispose();
/* 289 */             return true;
/*     */           } 
/*     */         } 
/*     */       } 
/* 293 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int requestFusion(int mode) {
/* 298 */       if ((mode & 0x2) != 0) {
/* 299 */         this.outputFused = true;
/* 300 */         return 2;
/*     */       } 
/* 302 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public T poll() throws Exception {
/* 308 */       return (T)this.queue.poll();
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 313 */       this.queue.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 318 */       return this.queue.isEmpty();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableObserveOn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */