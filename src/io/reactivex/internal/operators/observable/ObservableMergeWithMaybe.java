/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.fuseable.SimplePlainQueue;
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
/*     */ 
/*     */ public final class ObservableMergeWithMaybe<T>
/*     */   extends AbstractObservableWithUpstream<T, T>
/*     */ {
/*     */   final MaybeSource<? extends T> other;
/*     */   
/*     */   public ObservableMergeWithMaybe(Observable<T> source, MaybeSource<? extends T> other) {
/*  38 */     super((ObservableSource<T>)source);
/*  39 */     this.other = other;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super T> observer) {
/*  44 */     MergeWithObserver<T> parent = new MergeWithObserver<T>(observer);
/*  45 */     observer.onSubscribe(parent);
/*  46 */     this.source.subscribe(parent);
/*  47 */     this.other.subscribe(parent.otherObserver);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class MergeWithObserver<T>
/*     */     extends AtomicInteger
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -4592979584110982903L;
/*     */     
/*     */     final Observer<? super T> downstream;
/*     */     
/*     */     final AtomicReference<Disposable> mainDisposable;
/*     */     
/*     */     final OtherObserver<T> otherObserver;
/*     */     
/*     */     final AtomicThrowable error;
/*     */     
/*     */     volatile SimplePlainQueue<T> queue;
/*     */     
/*     */     T singleItem;
/*     */     
/*     */     volatile boolean disposed;
/*     */     
/*     */     volatile boolean mainDone;
/*     */     
/*     */     volatile int otherState;
/*     */     static final int OTHER_STATE_HAS_VALUE = 1;
/*     */     static final int OTHER_STATE_CONSUMED_OR_EMPTY = 2;
/*     */     
/*     */     MergeWithObserver(Observer<? super T> downstream) {
/*  78 */       this.downstream = downstream;
/*  79 */       this.mainDisposable = new AtomicReference<Disposable>();
/*  80 */       this.otherObserver = new OtherObserver<T>(this);
/*  81 */       this.error = new AtomicThrowable();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  86 */       DisposableHelper.setOnce(this.mainDisposable, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  91 */       if (compareAndSet(0, 1)) {
/*  92 */         this.downstream.onNext(t);
/*  93 */         if (decrementAndGet() == 0) {
/*     */           return;
/*     */         }
/*     */       } else {
/*  97 */         SimplePlainQueue<T> q = getOrCreateQueue();
/*  98 */         q.offer(t);
/*  99 */         if (getAndIncrement() != 0) {
/*     */           return;
/*     */         }
/*     */       } 
/* 103 */       drainLoop();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable ex) {
/* 108 */       if (this.error.addThrowable(ex)) {
/* 109 */         DisposableHelper.dispose(this.otherObserver);
/* 110 */         drain();
/*     */       } else {
/* 112 */         RxJavaPlugins.onError(ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 118 */       this.mainDone = true;
/* 119 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 124 */       return DisposableHelper.isDisposed(this.mainDisposable.get());
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 129 */       this.disposed = true;
/* 130 */       DisposableHelper.dispose(this.mainDisposable);
/* 131 */       DisposableHelper.dispose(this.otherObserver);
/* 132 */       if (getAndIncrement() == 0) {
/* 133 */         this.queue = null;
/* 134 */         this.singleItem = null;
/*     */       } 
/*     */     }
/*     */     
/*     */     void otherSuccess(T value) {
/* 139 */       if (compareAndSet(0, 1)) {
/* 140 */         this.downstream.onNext(value);
/* 141 */         this.otherState = 2;
/*     */       } else {
/* 143 */         this.singleItem = value;
/* 144 */         this.otherState = 1;
/* 145 */         if (getAndIncrement() != 0) {
/*     */           return;
/*     */         }
/*     */       } 
/* 149 */       drainLoop();
/*     */     }
/*     */     
/*     */     void otherError(Throwable ex) {
/* 153 */       if (this.error.addThrowable(ex)) {
/* 154 */         DisposableHelper.dispose(this.mainDisposable);
/* 155 */         drain();
/*     */       } else {
/* 157 */         RxJavaPlugins.onError(ex);
/*     */       } 
/*     */     }
/*     */     
/*     */     void otherComplete() {
/* 162 */       this.otherState = 2;
/* 163 */       drain();
/*     */     }
/*     */     SimplePlainQueue<T> getOrCreateQueue() {
/*     */       SpscLinkedArrayQueue spscLinkedArrayQueue;
/* 167 */       SimplePlainQueue<T> q = this.queue;
/* 168 */       if (q == null) {
/* 169 */         spscLinkedArrayQueue = new SpscLinkedArrayQueue(Observable.bufferSize());
/* 170 */         this.queue = (SimplePlainQueue<T>)spscLinkedArrayQueue;
/*     */       } 
/* 172 */       return (SimplePlainQueue<T>)spscLinkedArrayQueue;
/*     */     }
/*     */     
/*     */     void drain() {
/* 176 */       if (getAndIncrement() == 0) {
/* 177 */         drainLoop();
/*     */       }
/*     */     }
/*     */     
/*     */     void drainLoop() {
/* 182 */       Observer<? super T> actual = this.downstream;
/* 183 */       int missed = 1;
/*     */ 
/*     */       
/*     */       while (true) {
/* 187 */         if (this.disposed) {
/* 188 */           this.singleItem = null;
/* 189 */           this.queue = null;
/*     */           
/*     */           return;
/*     */         } 
/* 193 */         if (this.error.get() != null) {
/* 194 */           this.singleItem = null;
/* 195 */           this.queue = null;
/* 196 */           actual.onError(this.error.terminate());
/*     */           
/*     */           return;
/*     */         } 
/* 200 */         int os = this.otherState;
/* 201 */         if (os == 1) {
/* 202 */           T t = this.singleItem;
/* 203 */           this.singleItem = null;
/* 204 */           this.otherState = 2;
/* 205 */           os = 2;
/* 206 */           actual.onNext(t);
/*     */         } 
/*     */         
/* 209 */         boolean d = this.mainDone;
/* 210 */         SimplePlainQueue<T> q = this.queue;
/* 211 */         T v = (q != null) ? (T)q.poll() : null;
/* 212 */         boolean empty = (v == null);
/*     */         
/* 214 */         if (d && empty && os == 2) {
/* 215 */           this.queue = null;
/* 216 */           actual.onComplete();
/*     */           
/*     */           return;
/*     */         } 
/* 220 */         if (empty) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 227 */           missed = addAndGet(-missed);
/* 228 */           if (missed == 0)
/*     */             break; 
/*     */           continue;
/*     */         } 
/*     */         actual.onNext(v);
/*     */       } 
/*     */     }
/*     */     
/*     */     static final class OtherObserver<T>
/*     */       extends AtomicReference<Disposable> implements MaybeObserver<T> {
/*     */       private static final long serialVersionUID = -2935427570954647017L;
/*     */       final ObservableMergeWithMaybe.MergeWithObserver<T> parent;
/*     */       
/*     */       OtherObserver(ObservableMergeWithMaybe.MergeWithObserver<T> parent) {
/* 242 */         this.parent = parent;
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 247 */         DisposableHelper.setOnce(this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSuccess(T t) {
/* 252 */         this.parent.otherSuccess(t);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 257 */         this.parent.otherError(e);
/*     */       }
/*     */       
/*     */       public void onComplete()
/*     */       {
/* 262 */         this.parent.otherComplete(); } } } static final class OtherObserver<T> extends AtomicReference<Disposable> implements MaybeObserver<T> { public void onComplete() { this.parent.otherComplete(); }
/*     */ 
/*     */     
/*     */     private static final long serialVersionUID = -2935427570954647017L;
/*     */     final ObservableMergeWithMaybe.MergeWithObserver<T> parent;
/*     */     
/*     */     OtherObserver(ObservableMergeWithMaybe.MergeWithObserver<T> parent) {
/*     */       this.parent = parent;
/*     */     }
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*     */       DisposableHelper.setOnce(this, d);
/*     */     }
/*     */     
/*     */     public void onSuccess(T t) {
/*     */       this.parent.otherSuccess(t);
/*     */     }
/*     */     
/*     */     public void onError(Throwable e) {
/*     */       this.parent.otherError(e);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableMergeWithMaybe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */