/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
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
/*     */ public final class ObservableAmb<T>
/*     */   extends Observable<T>
/*     */ {
/*     */   final ObservableSource<? extends T>[] sources;
/*     */   final Iterable<? extends ObservableSource<? extends T>> sourcesIterable;
/*     */   
/*     */   public ObservableAmb(ObservableSource<? extends T>[] sources, Iterable<? extends ObservableSource<? extends T>> sourcesIterable) {
/*  29 */     this.sources = sources;
/*  30 */     this.sourcesIterable = sourcesIterable;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super T> observer) {
/*     */     ObservableSource[] arrayOfObservableSource;
/*  36 */     ObservableSource<? extends T>[] sources = this.sources;
/*  37 */     int count = 0;
/*  38 */     if (sources == null) {
/*  39 */       arrayOfObservableSource = new ObservableSource[8];
/*     */       try {
/*  41 */         for (ObservableSource<? extends T> p : this.sourcesIterable) {
/*  42 */           if (p == null) {
/*  43 */             EmptyDisposable.error(new NullPointerException("One of the sources is null"), observer);
/*     */             return;
/*     */           } 
/*  46 */           if (count == arrayOfObservableSource.length) {
/*  47 */             ObservableSource[] arrayOfObservableSource1 = new ObservableSource[count + (count >> 2)];
/*  48 */             System.arraycopy(arrayOfObservableSource, 0, arrayOfObservableSource1, 0, count);
/*  49 */             arrayOfObservableSource = arrayOfObservableSource1;
/*     */           } 
/*  51 */           arrayOfObservableSource[count++] = p;
/*     */         } 
/*  53 */       } catch (Throwable e) {
/*  54 */         Exceptions.throwIfFatal(e);
/*  55 */         EmptyDisposable.error(e, observer);
/*     */         return;
/*     */       } 
/*     */     } else {
/*  59 */       count = arrayOfObservableSource.length;
/*     */     } 
/*     */     
/*  62 */     if (count == 0) {
/*  63 */       EmptyDisposable.complete(observer);
/*     */       return;
/*     */     } 
/*  66 */     if (count == 1) {
/*  67 */       arrayOfObservableSource[0].subscribe(observer);
/*     */       
/*     */       return;
/*     */     } 
/*  71 */     AmbCoordinator<T> ac = new AmbCoordinator<T>(observer, count);
/*  72 */     ac.subscribe((ObservableSource<? extends T>[])arrayOfObservableSource);
/*     */   }
/*     */   
/*     */   static final class AmbCoordinator<T>
/*     */     implements Disposable {
/*     */     final Observer<? super T> downstream;
/*     */     final ObservableAmb.AmbInnerObserver<T>[] observers;
/*  79 */     final AtomicInteger winner = new AtomicInteger();
/*     */ 
/*     */     
/*     */     AmbCoordinator(Observer<? super T> actual, int count) {
/*  83 */       this.downstream = actual;
/*  84 */       this.observers = (ObservableAmb.AmbInnerObserver<T>[])new ObservableAmb.AmbInnerObserver[count];
/*     */     }
/*     */     
/*     */     public void subscribe(ObservableSource<? extends T>[] sources) {
/*  88 */       ObservableAmb.AmbInnerObserver<T>[] as = this.observers;
/*  89 */       int len = as.length; int i;
/*  90 */       for (i = 0; i < len; i++) {
/*  91 */         as[i] = new ObservableAmb.AmbInnerObserver<T>(this, i + 1, this.downstream);
/*     */       }
/*  93 */       this.winner.lazySet(0);
/*  94 */       this.downstream.onSubscribe(this);
/*     */       
/*  96 */       for (i = 0; i < len; i++) {
/*  97 */         if (this.winner.get() != 0) {
/*     */           return;
/*     */         }
/*     */         
/* 101 */         sources[i].subscribe(as[i]);
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean win(int index) {
/* 106 */       int w = this.winner.get();
/* 107 */       if (w == 0) {
/* 108 */         if (this.winner.compareAndSet(0, index)) {
/* 109 */           ObservableAmb.AmbInnerObserver<T>[] a = this.observers;
/* 110 */           int n = a.length;
/* 111 */           for (int i = 0; i < n; i++) {
/* 112 */             if (i + 1 != index) {
/* 113 */               a[i].dispose();
/*     */             }
/*     */           } 
/* 116 */           return true;
/*     */         } 
/* 118 */         return false;
/*     */       } 
/* 120 */       return (w == index);
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 125 */       if (this.winner.get() != -1) {
/* 126 */         this.winner.lazySet(-1);
/*     */         
/* 128 */         for (ObservableAmb.AmbInnerObserver<T> a : this.observers) {
/* 129 */           a.dispose();
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 136 */       return (this.winner.get() == -1);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class AmbInnerObserver<T>
/*     */     extends AtomicReference<Disposable>
/*     */     implements Observer<T> {
/*     */     private static final long serialVersionUID = -1185974347409665484L;
/*     */     final ObservableAmb.AmbCoordinator<T> parent;
/*     */     final int index;
/*     */     final Observer<? super T> downstream;
/*     */     boolean won;
/*     */     
/*     */     AmbInnerObserver(ObservableAmb.AmbCoordinator<T> parent, int index, Observer<? super T> downstream) {
/* 150 */       this.parent = parent;
/* 151 */       this.index = index;
/* 152 */       this.downstream = downstream;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 157 */       DisposableHelper.setOnce(this, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 162 */       if (this.won) {
/* 163 */         this.downstream.onNext(t);
/*     */       }
/* 165 */       else if (this.parent.win(this.index)) {
/* 166 */         this.won = true;
/* 167 */         this.downstream.onNext(t);
/*     */       } else {
/* 169 */         get().dispose();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 176 */       if (this.won) {
/* 177 */         this.downstream.onError(t);
/*     */       }
/* 179 */       else if (this.parent.win(this.index)) {
/* 180 */         this.won = true;
/* 181 */         this.downstream.onError(t);
/*     */       } else {
/* 183 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 190 */       if (this.won) {
/* 191 */         this.downstream.onComplete();
/*     */       }
/* 193 */       else if (this.parent.win(this.index)) {
/* 194 */         this.won = true;
/* 195 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 201 */       DisposableHelper.dispose(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableAmb.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */