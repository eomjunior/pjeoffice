/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.BiPredicate;
/*     */ import io.reactivex.internal.disposables.ArrayCompositeDisposable;
/*     */ import io.reactivex.internal.queue.SpscLinkedArrayQueue;
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
/*     */ 
/*     */ 
/*     */ public final class ObservableSequenceEqual<T>
/*     */   extends Observable<Boolean>
/*     */ {
/*     */   final ObservableSource<? extends T> first;
/*     */   final ObservableSource<? extends T> second;
/*     */   final BiPredicate<? super T, ? super T> comparer;
/*     */   final int bufferSize;
/*     */   
/*     */   public ObservableSequenceEqual(ObservableSource<? extends T> first, ObservableSource<? extends T> second, BiPredicate<? super T, ? super T> comparer, int bufferSize) {
/*  33 */     this.first = first;
/*  34 */     this.second = second;
/*  35 */     this.comparer = comparer;
/*  36 */     this.bufferSize = bufferSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super Boolean> observer) {
/*  41 */     EqualCoordinator<T> ec = new EqualCoordinator<T>(observer, this.bufferSize, this.first, this.second, this.comparer);
/*  42 */     observer.onSubscribe(ec);
/*  43 */     ec.subscribe();
/*     */   }
/*     */ 
/*     */   
/*     */   static final class EqualCoordinator<T>
/*     */     extends AtomicInteger
/*     */     implements Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -6178010334400373240L;
/*     */     
/*     */     final Observer<? super Boolean> downstream;
/*     */     
/*     */     final BiPredicate<? super T, ? super T> comparer;
/*     */     final ArrayCompositeDisposable resources;
/*     */     final ObservableSource<? extends T> first;
/*     */     final ObservableSource<? extends T> second;
/*     */     final ObservableSequenceEqual.EqualObserver<T>[] observers;
/*     */     volatile boolean cancelled;
/*     */     T v1;
/*     */     T v2;
/*     */     
/*     */     EqualCoordinator(Observer<? super Boolean> actual, int bufferSize, ObservableSource<? extends T> first, ObservableSource<? extends T> second, BiPredicate<? super T, ? super T> comparer) {
/*  65 */       this.downstream = actual;
/*  66 */       this.first = first;
/*  67 */       this.second = second;
/*  68 */       this.comparer = comparer;
/*     */       
/*  70 */       ObservableSequenceEqual.EqualObserver[] arrayOfEqualObserver = new ObservableSequenceEqual.EqualObserver[2];
/*  71 */       this.observers = (ObservableSequenceEqual.EqualObserver<T>[])arrayOfEqualObserver;
/*  72 */       arrayOfEqualObserver[0] = new ObservableSequenceEqual.EqualObserver<T>(this, 0, bufferSize);
/*  73 */       arrayOfEqualObserver[1] = new ObservableSequenceEqual.EqualObserver<T>(this, 1, bufferSize);
/*  74 */       this.resources = new ArrayCompositeDisposable(2);
/*     */     }
/*     */     
/*     */     boolean setDisposable(Disposable d, int index) {
/*  78 */       return this.resources.setResource(index, d);
/*     */     }
/*     */     
/*     */     void subscribe() {
/*  82 */       ObservableSequenceEqual.EqualObserver<T>[] as = this.observers;
/*  83 */       this.first.subscribe(as[0]);
/*  84 */       this.second.subscribe(as[1]);
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  89 */       if (!this.cancelled) {
/*  90 */         this.cancelled = true;
/*  91 */         this.resources.dispose();
/*     */         
/*  93 */         if (getAndIncrement() == 0) {
/*  94 */           ObservableSequenceEqual.EqualObserver<T>[] as = this.observers;
/*  95 */           (as[0]).queue.clear();
/*  96 */           (as[1]).queue.clear();
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 103 */       return this.cancelled;
/*     */     }
/*     */     
/*     */     void cancel(SpscLinkedArrayQueue<T> q1, SpscLinkedArrayQueue<T> q2) {
/* 107 */       this.cancelled = true;
/* 108 */       q1.clear();
/* 109 */       q2.clear();
/*     */     }
/*     */     
/*     */     void drain() {
/* 113 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 117 */       int missed = 1;
/* 118 */       ObservableSequenceEqual.EqualObserver<T>[] as = this.observers;
/*     */       
/* 120 */       ObservableSequenceEqual.EqualObserver<T> observer1 = as[0];
/* 121 */       SpscLinkedArrayQueue<T> q1 = observer1.queue;
/* 122 */       ObservableSequenceEqual.EqualObserver<T> observer2 = as[1];
/* 123 */       SpscLinkedArrayQueue<T> q2 = observer2.queue;
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 128 */         if (this.cancelled) {
/* 129 */           q1.clear();
/* 130 */           q2.clear();
/*     */           
/*     */           return;
/*     */         } 
/* 134 */         boolean d1 = observer1.done;
/*     */         
/* 136 */         if (d1) {
/* 137 */           Throwable e = observer1.error;
/* 138 */           if (e != null) {
/* 139 */             cancel(q1, q2);
/*     */             
/* 141 */             this.downstream.onError(e);
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 146 */         boolean d2 = observer2.done;
/* 147 */         if (d2) {
/* 148 */           Throwable e = observer2.error;
/* 149 */           if (e != null) {
/* 150 */             cancel(q1, q2);
/*     */             
/* 152 */             this.downstream.onError(e);
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 157 */         if (this.v1 == null) {
/* 158 */           this.v1 = (T)q1.poll();
/*     */         }
/* 160 */         boolean e1 = (this.v1 == null);
/*     */         
/* 162 */         if (this.v2 == null) {
/* 163 */           this.v2 = (T)q2.poll();
/*     */         }
/* 165 */         boolean e2 = (this.v2 == null);
/*     */         
/* 167 */         if (d1 && d2 && e1 && e2) {
/* 168 */           this.downstream.onNext(Boolean.valueOf(true));
/* 169 */           this.downstream.onComplete();
/*     */           return;
/*     */         } 
/* 172 */         if (d1 && d2 && e1 != e2) {
/* 173 */           cancel(q1, q2);
/*     */           
/* 175 */           this.downstream.onNext(Boolean.valueOf(false));
/* 176 */           this.downstream.onComplete();
/*     */           
/*     */           return;
/*     */         } 
/* 180 */         if (!e1 && !e2) {
/*     */           boolean c;
/*     */           
/*     */           try {
/* 184 */             c = this.comparer.test(this.v1, this.v2);
/* 185 */           } catch (Throwable ex) {
/* 186 */             Exceptions.throwIfFatal(ex);
/* 187 */             cancel(q1, q2);
/*     */             
/* 189 */             this.downstream.onError(ex);
/*     */             
/*     */             return;
/*     */           } 
/* 193 */           if (!c) {
/* 194 */             cancel(q1, q2);
/*     */             
/* 196 */             this.downstream.onNext(Boolean.valueOf(false));
/* 197 */             this.downstream.onComplete();
/*     */             
/*     */             return;
/*     */           } 
/* 201 */           this.v1 = null;
/* 202 */           this.v2 = null;
/*     */         } 
/*     */         
/* 205 */         if (e1 || e2) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 210 */           missed = addAndGet(-missed);
/* 211 */           if (missed == 0)
/*     */             break; 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static final class EqualObserver<T>
/*     */     implements Observer<T> {
/*     */     final ObservableSequenceEqual.EqualCoordinator<T> parent;
/*     */     final SpscLinkedArrayQueue<T> queue;
/*     */     final int index;
/*     */     volatile boolean done;
/*     */     Throwable error;
/*     */     
/*     */     EqualObserver(ObservableSequenceEqual.EqualCoordinator<T> parent, int index, int bufferSize) {
/* 227 */       this.parent = parent;
/* 228 */       this.index = index;
/* 229 */       this.queue = new SpscLinkedArrayQueue(bufferSize);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 234 */       this.parent.setDisposable(d, this.index);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 239 */       this.queue.offer(t);
/* 240 */       this.parent.drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 245 */       this.error = t;
/* 246 */       this.done = true;
/* 247 */       this.parent.drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 252 */       this.done = true;
/* 253 */       this.parent.drain();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableSequenceEqual.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */