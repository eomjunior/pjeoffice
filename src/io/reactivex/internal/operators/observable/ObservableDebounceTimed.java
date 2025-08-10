/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.observers.SerializedObserver;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ public final class ObservableDebounceTimed<T>
/*     */   extends AbstractObservableWithUpstream<T, T>
/*     */ {
/*     */   final long timeout;
/*     */   final TimeUnit unit;
/*     */   final Scheduler scheduler;
/*     */   
/*     */   public ObservableDebounceTimed(ObservableSource<T> source, long timeout, TimeUnit unit, Scheduler scheduler) {
/*  32 */     super(source);
/*  33 */     this.timeout = timeout;
/*  34 */     this.unit = unit;
/*  35 */     this.scheduler = scheduler;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super T> t) {
/*  40 */     this.source.subscribe(new DebounceTimedObserver((Observer<?>)new SerializedObserver(t), this.timeout, this.unit, this.scheduler
/*     */           
/*  42 */           .createWorker()));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class DebounceTimedObserver<T>
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     final Observer<? super T> downstream;
/*     */     
/*     */     final long timeout;
/*     */     
/*     */     final TimeUnit unit;
/*     */     final Scheduler.Worker worker;
/*     */     Disposable upstream;
/*     */     Disposable timer;
/*     */     volatile long index;
/*     */     boolean done;
/*     */     
/*     */     DebounceTimedObserver(Observer<? super T> actual, long timeout, TimeUnit unit, Scheduler.Worker worker) {
/*  61 */       this.downstream = actual;
/*  62 */       this.timeout = timeout;
/*  63 */       this.unit = unit;
/*  64 */       this.worker = worker;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  69 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  70 */         this.upstream = d;
/*  71 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  77 */       if (this.done) {
/*     */         return;
/*     */       }
/*  80 */       long idx = this.index + 1L;
/*  81 */       this.index = idx;
/*     */       
/*  83 */       Disposable d = this.timer;
/*  84 */       if (d != null) {
/*  85 */         d.dispose();
/*     */       }
/*     */       
/*  88 */       ObservableDebounceTimed.DebounceEmitter<T> de = new ObservableDebounceTimed.DebounceEmitter<T>(t, idx, this);
/*  89 */       this.timer = de;
/*  90 */       d = this.worker.schedule(de, this.timeout, this.unit);
/*  91 */       de.setResource(d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  96 */       if (this.done) {
/*  97 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 100 */       Disposable d = this.timer;
/* 101 */       if (d != null) {
/* 102 */         d.dispose();
/*     */       }
/* 104 */       this.done = true;
/* 105 */       this.downstream.onError(t);
/* 106 */       this.worker.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 111 */       if (this.done) {
/*     */         return;
/*     */       }
/* 114 */       this.done = true;
/*     */       
/* 116 */       Disposable d = this.timer;
/* 117 */       if (d != null) {
/* 118 */         d.dispose();
/*     */       }
/*     */ 
/*     */       
/* 122 */       ObservableDebounceTimed.DebounceEmitter<T> de = (ObservableDebounceTimed.DebounceEmitter<T>)d;
/* 123 */       if (de != null) {
/* 124 */         de.run();
/*     */       }
/* 126 */       this.downstream.onComplete();
/* 127 */       this.worker.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 132 */       this.upstream.dispose();
/* 133 */       this.worker.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 138 */       return this.worker.isDisposed();
/*     */     }
/*     */     
/*     */     void emit(long idx, T t, ObservableDebounceTimed.DebounceEmitter<T> emitter) {
/* 142 */       if (idx == this.index) {
/* 143 */         this.downstream.onNext(t);
/* 144 */         emitter.dispose();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static final class DebounceEmitter<T>
/*     */     extends AtomicReference<Disposable>
/*     */     implements Runnable, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 6812032969491025141L;
/*     */     final T value;
/*     */     final long idx;
/*     */     final ObservableDebounceTimed.DebounceTimedObserver<T> parent;
/* 157 */     final AtomicBoolean once = new AtomicBoolean();
/*     */     
/*     */     DebounceEmitter(T value, long idx, ObservableDebounceTimed.DebounceTimedObserver<T> parent) {
/* 160 */       this.value = value;
/* 161 */       this.idx = idx;
/* 162 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 167 */       if (this.once.compareAndSet(false, true)) {
/* 168 */         this.parent.emit(this.idx, this.value, this);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 174 */       DisposableHelper.dispose(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 179 */       return (get() == DisposableHelper.DISPOSED);
/*     */     }
/*     */     
/*     */     public void setResource(Disposable d) {
/* 183 */       DisposableHelper.replace(this, d);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableDebounceTimed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */