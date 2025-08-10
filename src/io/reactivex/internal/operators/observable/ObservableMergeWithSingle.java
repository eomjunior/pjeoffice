/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.SingleSource;
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
/*     */ public final class ObservableMergeWithSingle<T>
/*     */   extends AbstractObservableWithUpstream<T, T>
/*     */ {
/*     */   final SingleSource<? extends T> other;
/*     */   
/*     */   public ObservableMergeWithSingle(Observable<T> source, SingleSource<? extends T> other) {
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
/*     */     SimplePlainQueue<T> getOrCreateQueue() {
/*     */       SpscLinkedArrayQueue spscLinkedArrayQueue;
/* 162 */       SimplePlainQueue<T> q = this.queue;
/* 163 */       if (q == null) {
/* 164 */         spscLinkedArrayQueue = new SpscLinkedArrayQueue(Observable.bufferSize());
/* 165 */         this.queue = (SimplePlainQueue<T>)spscLinkedArrayQueue;
/*     */       } 
/* 167 */       return (SimplePlainQueue<T>)spscLinkedArrayQueue;
/*     */     }
/*     */     
/*     */     void drain() {
/* 171 */       if (getAndIncrement() == 0) {
/* 172 */         drainLoop();
/*     */       }
/*     */     }
/*     */     
/*     */     void drainLoop() {
/* 177 */       Observer<? super T> actual = this.downstream;
/* 178 */       int missed = 1;
/*     */ 
/*     */       
/*     */       while (true) {
/* 182 */         if (this.disposed) {
/* 183 */           this.singleItem = null;
/* 184 */           this.queue = null;
/*     */           
/*     */           return;
/*     */         } 
/* 188 */         if (this.error.get() != null) {
/* 189 */           this.singleItem = null;
/* 190 */           this.queue = null;
/* 191 */           actual.onError(this.error.terminate());
/*     */           
/*     */           return;
/*     */         } 
/* 195 */         int os = this.otherState;
/* 196 */         if (os == 1) {
/* 197 */           T t = this.singleItem;
/* 198 */           this.singleItem = null;
/* 199 */           this.otherState = 2;
/* 200 */           os = 2;
/* 201 */           actual.onNext(t);
/*     */         } 
/*     */         
/* 204 */         boolean d = this.mainDone;
/* 205 */         SimplePlainQueue<T> q = this.queue;
/* 206 */         T v = (q != null) ? (T)q.poll() : null;
/* 207 */         boolean empty = (v == null);
/*     */         
/* 209 */         if (d && empty && os == 2) {
/* 210 */           this.queue = null;
/* 211 */           actual.onComplete();
/*     */           
/*     */           return;
/*     */         } 
/* 215 */         if (empty) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 222 */           missed = addAndGet(-missed);
/* 223 */           if (missed == 0)
/*     */             break; 
/*     */           continue;
/*     */         } 
/*     */         actual.onNext(v);
/*     */       } 
/*     */     }
/*     */     
/*     */     static final class OtherObserver<T>
/*     */       extends AtomicReference<Disposable> implements SingleObserver<T> {
/*     */       private static final long serialVersionUID = -2935427570954647017L;
/*     */       final ObservableMergeWithSingle.MergeWithObserver<T> parent;
/*     */       
/*     */       OtherObserver(ObservableMergeWithSingle.MergeWithObserver<T> parent) {
/* 237 */         this.parent = parent;
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 242 */         DisposableHelper.setOnce(this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSuccess(T t) {
/* 247 */         this.parent.otherSuccess(t);
/*     */       }
/*     */       
/*     */       public void onError(Throwable e)
/*     */       {
/* 252 */         this.parent.otherError(e); } } } static final class OtherObserver<T> extends AtomicReference<Disposable> implements SingleObserver<T> { public void onError(Throwable e) { this.parent.otherError(e); }
/*     */ 
/*     */     
/*     */     private static final long serialVersionUID = -2935427570954647017L;
/*     */     final ObservableMergeWithSingle.MergeWithObserver<T> parent;
/*     */     
/*     */     OtherObserver(ObservableMergeWithSingle.MergeWithObserver<T> parent) {
/*     */       this.parent = parent;
/*     */     }
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*     */       DisposableHelper.setOnce(this, d);
/*     */     }
/*     */     
/*     */     public void onSuccess(T t) {
/*     */       this.parent.otherSuccess(t);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableMergeWithSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */