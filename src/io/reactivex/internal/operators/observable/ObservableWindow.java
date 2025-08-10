/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.subjects.UnicastSubject;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ public final class ObservableWindow<T>
/*     */   extends AbstractObservableWithUpstream<T, Observable<T>>
/*     */ {
/*     */   final long count;
/*     */   final long skip;
/*     */   final int capacityHint;
/*     */   
/*     */   public ObservableWindow(ObservableSource<T> source, long count, long skip, int capacityHint) {
/*  30 */     super(source);
/*  31 */     this.count = count;
/*  32 */     this.skip = skip;
/*  33 */     this.capacityHint = capacityHint;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super Observable<T>> t) {
/*  38 */     if (this.count == this.skip) {
/*  39 */       this.source.subscribe(new WindowExactObserver<T>(t, this.count, this.capacityHint));
/*     */     } else {
/*  41 */       this.source.subscribe(new WindowSkipObserver<T>(t, this.count, this.skip, this.capacityHint));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static final class WindowExactObserver<T>
/*     */     extends AtomicInteger
/*     */     implements Observer<T>, Disposable, Runnable
/*     */   {
/*     */     private static final long serialVersionUID = -7481782523886138128L;
/*     */     
/*     */     final Observer<? super Observable<T>> downstream;
/*     */     
/*     */     final long count;
/*     */     
/*     */     final int capacityHint;
/*     */     long size;
/*     */     Disposable upstream;
/*     */     UnicastSubject<T> window;
/*     */     volatile boolean cancelled;
/*     */     
/*     */     WindowExactObserver(Observer<? super Observable<T>> actual, long count, int capacityHint) {
/*  63 */       this.downstream = actual;
/*  64 */       this.count = count;
/*  65 */       this.capacityHint = capacityHint;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  70 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  71 */         this.upstream = d;
/*     */         
/*  73 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  79 */       UnicastSubject<T> w = this.window;
/*  80 */       if (w == null && !this.cancelled) {
/*  81 */         w = UnicastSubject.create(this.capacityHint, this);
/*  82 */         this.window = w;
/*  83 */         this.downstream.onNext(w);
/*     */       } 
/*     */ 
/*     */       
/*  87 */       w.onNext(t);
/*  88 */       if (w != null && ++this.size >= this.count) {
/*  89 */         this.size = 0L;
/*  90 */         this.window = null;
/*  91 */         w.onComplete();
/*  92 */         if (this.cancelled) {
/*  93 */           this.upstream.dispose();
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 101 */       UnicastSubject<T> w = this.window;
/* 102 */       if (w != null) {
/* 103 */         this.window = null;
/* 104 */         w.onError(t);
/*     */       } 
/* 106 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 111 */       UnicastSubject<T> w = this.window;
/* 112 */       if (w != null) {
/* 113 */         this.window = null;
/* 114 */         w.onComplete();
/*     */       } 
/* 116 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 121 */       this.cancelled = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 126 */       return this.cancelled;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 131 */       if (this.cancelled) {
/* 132 */         this.upstream.dispose();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class WindowSkipObserver<T>
/*     */     extends AtomicBoolean
/*     */     implements Observer<T>, Disposable, Runnable
/*     */   {
/*     */     private static final long serialVersionUID = 3366976432059579510L;
/*     */     
/*     */     final Observer<? super Observable<T>> downstream;
/*     */     
/*     */     final long count;
/*     */     
/*     */     final long skip;
/*     */     
/*     */     final int capacityHint;
/*     */     final ArrayDeque<UnicastSubject<T>> windows;
/*     */     long index;
/*     */     volatile boolean cancelled;
/*     */     long firstEmission;
/*     */     Disposable upstream;
/* 156 */     final AtomicInteger wip = new AtomicInteger();
/*     */     
/*     */     WindowSkipObserver(Observer<? super Observable<T>> actual, long count, long skip, int capacityHint) {
/* 159 */       this.downstream = actual;
/* 160 */       this.count = count;
/* 161 */       this.skip = skip;
/* 162 */       this.capacityHint = capacityHint;
/* 163 */       this.windows = new ArrayDeque<UnicastSubject<T>>();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 168 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 169 */         this.upstream = d;
/*     */         
/* 171 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 177 */       ArrayDeque<UnicastSubject<T>> ws = this.windows;
/*     */       
/* 179 */       long i = this.index;
/*     */       
/* 181 */       long s = this.skip;
/*     */       
/* 183 */       if (i % s == 0L && !this.cancelled) {
/* 184 */         this.wip.getAndIncrement();
/* 185 */         UnicastSubject<T> w = UnicastSubject.create(this.capacityHint, this);
/* 186 */         ws.offer(w);
/* 187 */         this.downstream.onNext(w);
/*     */       } 
/*     */       
/* 190 */       long c = this.firstEmission + 1L;
/*     */       
/* 192 */       for (UnicastSubject<T> w : ws) {
/* 193 */         w.onNext(t);
/*     */       }
/*     */       
/* 196 */       if (c >= this.count) {
/* 197 */         ((UnicastSubject)ws.poll()).onComplete();
/* 198 */         if (ws.isEmpty() && this.cancelled) {
/* 199 */           this.upstream.dispose();
/*     */           return;
/*     */         } 
/* 202 */         this.firstEmission = c - s;
/*     */       } else {
/* 204 */         this.firstEmission = c;
/*     */       } 
/*     */       
/* 207 */       this.index = i + 1L;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 212 */       ArrayDeque<UnicastSubject<T>> ws = this.windows;
/* 213 */       while (!ws.isEmpty()) {
/* 214 */         ((UnicastSubject)ws.poll()).onError(t);
/*     */       }
/* 216 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 221 */       ArrayDeque<UnicastSubject<T>> ws = this.windows;
/* 222 */       while (!ws.isEmpty()) {
/* 223 */         ((UnicastSubject)ws.poll()).onComplete();
/*     */       }
/* 225 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 230 */       this.cancelled = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 235 */       return this.cancelled;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 240 */       if (this.wip.decrementAndGet() == 0 && 
/* 241 */         this.cancelled)
/* 242 */         this.upstream.dispose(); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableWindow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */