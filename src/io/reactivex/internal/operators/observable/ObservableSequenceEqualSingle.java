/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.Single;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.BiPredicate;
/*     */ import io.reactivex.internal.disposables.ArrayCompositeDisposable;
/*     */ import io.reactivex.internal.fuseable.FuseToObservable;
/*     */ import io.reactivex.internal.queue.SpscLinkedArrayQueue;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
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
/*     */ public final class ObservableSequenceEqualSingle<T>
/*     */   extends Single<Boolean>
/*     */   implements FuseToObservable<Boolean>
/*     */ {
/*     */   final ObservableSource<? extends T> first;
/*     */   final ObservableSource<? extends T> second;
/*     */   final BiPredicate<? super T, ? super T> comparer;
/*     */   final int bufferSize;
/*     */   
/*     */   public ObservableSequenceEqualSingle(ObservableSource<? extends T> first, ObservableSource<? extends T> second, BiPredicate<? super T, ? super T> comparer, int bufferSize) {
/*  35 */     this.first = first;
/*  36 */     this.second = second;
/*  37 */     this.comparer = comparer;
/*  38 */     this.bufferSize = bufferSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(SingleObserver<? super Boolean> observer) {
/*  43 */     EqualCoordinator<T> ec = new EqualCoordinator<T>(observer, this.bufferSize, this.first, this.second, this.comparer);
/*  44 */     observer.onSubscribe(ec);
/*  45 */     ec.subscribe();
/*     */   }
/*     */ 
/*     */   
/*     */   public Observable<Boolean> fuseToObservable() {
/*  50 */     return RxJavaPlugins.onAssembly(new ObservableSequenceEqual<T>(this.first, this.second, this.comparer, this.bufferSize));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class EqualCoordinator<T>
/*     */     extends AtomicInteger
/*     */     implements Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -6178010334400373240L;
/*     */     
/*     */     final SingleObserver<? super Boolean> downstream;
/*     */     
/*     */     final BiPredicate<? super T, ? super T> comparer;
/*     */     final ArrayCompositeDisposable resources;
/*     */     final ObservableSource<? extends T> first;
/*     */     final ObservableSource<? extends T> second;
/*     */     final ObservableSequenceEqualSingle.EqualObserver<T>[] observers;
/*     */     volatile boolean cancelled;
/*     */     T v1;
/*     */     T v2;
/*     */     
/*     */     EqualCoordinator(SingleObserver<? super Boolean> actual, int bufferSize, ObservableSource<? extends T> first, ObservableSource<? extends T> second, BiPredicate<? super T, ? super T> comparer) {
/*  72 */       this.downstream = actual;
/*  73 */       this.first = first;
/*  74 */       this.second = second;
/*  75 */       this.comparer = comparer;
/*     */       
/*  77 */       ObservableSequenceEqualSingle.EqualObserver[] arrayOfEqualObserver = new ObservableSequenceEqualSingle.EqualObserver[2];
/*  78 */       this.observers = (ObservableSequenceEqualSingle.EqualObserver<T>[])arrayOfEqualObserver;
/*  79 */       arrayOfEqualObserver[0] = new ObservableSequenceEqualSingle.EqualObserver<T>(this, 0, bufferSize);
/*  80 */       arrayOfEqualObserver[1] = new ObservableSequenceEqualSingle.EqualObserver<T>(this, 1, bufferSize);
/*  81 */       this.resources = new ArrayCompositeDisposable(2);
/*     */     }
/*     */     
/*     */     boolean setDisposable(Disposable d, int index) {
/*  85 */       return this.resources.setResource(index, d);
/*     */     }
/*     */     
/*     */     void subscribe() {
/*  89 */       ObservableSequenceEqualSingle.EqualObserver<T>[] as = this.observers;
/*  90 */       this.first.subscribe(as[0]);
/*  91 */       this.second.subscribe(as[1]);
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  96 */       if (!this.cancelled) {
/*  97 */         this.cancelled = true;
/*  98 */         this.resources.dispose();
/*     */         
/* 100 */         if (getAndIncrement() == 0) {
/* 101 */           ObservableSequenceEqualSingle.EqualObserver<T>[] as = this.observers;
/* 102 */           (as[0]).queue.clear();
/* 103 */           (as[1]).queue.clear();
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 110 */       return this.cancelled;
/*     */     }
/*     */     
/*     */     void cancel(SpscLinkedArrayQueue<T> q1, SpscLinkedArrayQueue<T> q2) {
/* 114 */       this.cancelled = true;
/* 115 */       q1.clear();
/* 116 */       q2.clear();
/*     */     }
/*     */     
/*     */     void drain() {
/* 120 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 124 */       int missed = 1;
/* 125 */       ObservableSequenceEqualSingle.EqualObserver<T>[] as = this.observers;
/*     */       
/* 127 */       ObservableSequenceEqualSingle.EqualObserver<T> observer1 = as[0];
/* 128 */       SpscLinkedArrayQueue<T> q1 = observer1.queue;
/* 129 */       ObservableSequenceEqualSingle.EqualObserver<T> observer2 = as[1];
/* 130 */       SpscLinkedArrayQueue<T> q2 = observer2.queue;
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 135 */         if (this.cancelled) {
/* 136 */           q1.clear();
/* 137 */           q2.clear();
/*     */           
/*     */           return;
/*     */         } 
/* 141 */         boolean d1 = observer1.done;
/*     */         
/* 143 */         if (d1) {
/* 144 */           Throwable e = observer1.error;
/* 145 */           if (e != null) {
/* 146 */             cancel(q1, q2);
/*     */             
/* 148 */             this.downstream.onError(e);
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 153 */         boolean d2 = observer2.done;
/* 154 */         if (d2) {
/* 155 */           Throwable e = observer2.error;
/* 156 */           if (e != null) {
/* 157 */             cancel(q1, q2);
/*     */             
/* 159 */             this.downstream.onError(e);
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 164 */         if (this.v1 == null) {
/* 165 */           this.v1 = (T)q1.poll();
/*     */         }
/* 167 */         boolean e1 = (this.v1 == null);
/*     */         
/* 169 */         if (this.v2 == null) {
/* 170 */           this.v2 = (T)q2.poll();
/*     */         }
/* 172 */         boolean e2 = (this.v2 == null);
/*     */         
/* 174 */         if (d1 && d2 && e1 && e2) {
/* 175 */           this.downstream.onSuccess(Boolean.valueOf(true));
/*     */           return;
/*     */         } 
/* 178 */         if (d1 && d2 && e1 != e2) {
/* 179 */           cancel(q1, q2);
/*     */           
/* 181 */           this.downstream.onSuccess(Boolean.valueOf(false));
/*     */           
/*     */           return;
/*     */         } 
/* 185 */         if (!e1 && !e2) {
/*     */           boolean c;
/*     */           
/*     */           try {
/* 189 */             c = this.comparer.test(this.v1, this.v2);
/* 190 */           } catch (Throwable ex) {
/* 191 */             Exceptions.throwIfFatal(ex);
/* 192 */             cancel(q1, q2);
/*     */             
/* 194 */             this.downstream.onError(ex);
/*     */             
/*     */             return;
/*     */           } 
/* 198 */           if (!c) {
/* 199 */             cancel(q1, q2);
/*     */             
/* 201 */             this.downstream.onSuccess(Boolean.valueOf(false));
/*     */             
/*     */             return;
/*     */           } 
/* 205 */           this.v1 = null;
/* 206 */           this.v2 = null;
/*     */         } 
/*     */         
/* 209 */         if (e1 || e2) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 214 */           missed = addAndGet(-missed);
/* 215 */           if (missed == 0)
/*     */             break; 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static final class EqualObserver<T>
/*     */     implements Observer<T> {
/*     */     final ObservableSequenceEqualSingle.EqualCoordinator<T> parent;
/*     */     final SpscLinkedArrayQueue<T> queue;
/*     */     final int index;
/*     */     volatile boolean done;
/*     */     Throwable error;
/*     */     
/*     */     EqualObserver(ObservableSequenceEqualSingle.EqualCoordinator<T> parent, int index, int bufferSize) {
/* 231 */       this.parent = parent;
/* 232 */       this.index = index;
/* 233 */       this.queue = new SpscLinkedArrayQueue(bufferSize);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 238 */       this.parent.setDisposable(d, this.index);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 243 */       this.queue.offer(t);
/* 244 */       this.parent.drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 249 */       this.error = t;
/* 250 */       this.done = true;
/* 251 */       this.parent.drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 256 */       this.done = true;
/* 257 */       this.parent.drain();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableSequenceEqualSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */