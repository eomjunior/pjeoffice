/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ 
/*     */ public final class ObservableCache<T>
/*     */   extends AbstractObservableWithUpstream<T, T>
/*     */   implements Observer<T>
/*     */ {
/*     */   final AtomicBoolean once;
/*     */   final int capacityHint;
/*     */   final AtomicReference<CacheDisposable<T>[]> observers;
/*  50 */   static final CacheDisposable[] EMPTY = new CacheDisposable[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   static final CacheDisposable[] TERMINATED = new CacheDisposable[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   volatile long size;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final Node<T> head;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Node<T> tail;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int tailOffset;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Throwable error;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   volatile boolean done;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObservableCache(Observable<T> source, int capacityHint) {
/*  95 */     super((ObservableSource<T>)source);
/*  96 */     this.capacityHint = capacityHint;
/*  97 */     this.once = new AtomicBoolean();
/*  98 */     Node<T> n = new Node<T>(capacityHint);
/*  99 */     this.head = n;
/* 100 */     this.tail = n;
/* 101 */     this.observers = new AtomicReference(EMPTY);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super T> t) {
/* 106 */     CacheDisposable<T> consumer = new CacheDisposable<T>(t, this);
/* 107 */     t.onSubscribe(consumer);
/* 108 */     add(consumer);
/*     */     
/* 110 */     if (!this.once.get() && this.once.compareAndSet(false, true)) {
/* 111 */       this.source.subscribe(this);
/*     */     } else {
/* 113 */       replay(consumer);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isConnected() {
/* 122 */     return this.once.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean hasObservers() {
/* 130 */     return (((CacheDisposable[])this.observers.get()).length != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long cachedEventCount() {
/* 138 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void add(CacheDisposable<T> consumer) {
/*     */     CacheDisposable[] arrayOfCacheDisposable1;
/*     */     CacheDisposable[] arrayOfCacheDisposable2;
/*     */     do {
/* 148 */       arrayOfCacheDisposable1 = (CacheDisposable[])this.observers.get();
/* 149 */       if (arrayOfCacheDisposable1 == TERMINATED) {
/*     */         return;
/*     */       }
/* 152 */       int n = arrayOfCacheDisposable1.length;
/*     */ 
/*     */       
/* 155 */       arrayOfCacheDisposable2 = new CacheDisposable[n + 1];
/* 156 */       System.arraycopy(arrayOfCacheDisposable1, 0, arrayOfCacheDisposable2, 0, n);
/* 157 */       arrayOfCacheDisposable2[n] = consumer;
/*     */     }
/* 159 */     while (!this.observers.compareAndSet(arrayOfCacheDisposable1, arrayOfCacheDisposable2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void remove(CacheDisposable<T> consumer) {
/*     */     CacheDisposable[] arrayOfCacheDisposable1;
/*     */     CacheDisposable[] arrayOfCacheDisposable2;
/*     */     do {
/* 172 */       arrayOfCacheDisposable1 = (CacheDisposable[])this.observers.get();
/* 173 */       int n = arrayOfCacheDisposable1.length;
/* 174 */       if (n == 0) {
/*     */         return;
/*     */       }
/*     */       
/* 178 */       int j = -1;
/* 179 */       for (int i = 0; i < n; i++) {
/* 180 */         if (arrayOfCacheDisposable1[i] == consumer) {
/* 181 */           j = i;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 186 */       if (j < 0) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 191 */       if (n == 1) {
/* 192 */         arrayOfCacheDisposable2 = EMPTY;
/*     */       } else {
/* 194 */         arrayOfCacheDisposable2 = new CacheDisposable[n - 1];
/* 195 */         System.arraycopy(arrayOfCacheDisposable1, 0, arrayOfCacheDisposable2, 0, j);
/* 196 */         System.arraycopy(arrayOfCacheDisposable1, j + 1, arrayOfCacheDisposable2, j, n - j - 1);
/*     */       }
/*     */     
/* 199 */     } while (!this.observers.compareAndSet(arrayOfCacheDisposable1, arrayOfCacheDisposable2));
/*     */   }
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
/*     */   void replay(CacheDisposable<T> consumer) {
/* 212 */     if (consumer.getAndIncrement() != 0) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 217 */     int missed = 1;
/*     */     
/* 219 */     long index = consumer.index;
/* 220 */     int offset = consumer.offset;
/* 221 */     Node<T> node = consumer.node;
/* 222 */     Observer<? super T> downstream = consumer.downstream;
/* 223 */     int capacity = this.capacityHint;
/*     */ 
/*     */     
/*     */     while (true) {
/* 227 */       if (consumer.disposed) {
/* 228 */         consumer.node = null;
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 233 */       boolean sourceDone = this.done;
/*     */       
/* 235 */       boolean empty = (this.size == index);
/*     */ 
/*     */       
/* 238 */       if (sourceDone && empty) {
/*     */         
/* 240 */         consumer.node = null;
/*     */         
/* 242 */         Throwable ex = this.error;
/* 243 */         if (ex != null) {
/* 244 */           downstream.onError(ex);
/*     */         } else {
/* 246 */           downstream.onComplete();
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 252 */       if (!empty) {
/*     */         
/* 254 */         if (offset == capacity) {
/*     */           
/* 256 */           node = node.next;
/*     */           
/* 258 */           offset = 0;
/*     */         } 
/*     */ 
/*     */         
/* 262 */         downstream.onNext(node.values[offset]);
/*     */ 
/*     */         
/* 265 */         offset++;
/*     */         
/* 267 */         index++;
/*     */ 
/*     */         
/*     */         continue;
/*     */       } 
/*     */ 
/*     */       
/* 274 */       consumer.index = index;
/* 275 */       consumer.offset = offset;
/* 276 */       consumer.node = node;
/*     */       
/* 278 */       missed = consumer.addAndGet(-missed);
/* 279 */       if (missed == 0) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onSubscribe(Disposable d) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onNext(T t) {
/* 292 */     int tailOffset = this.tailOffset;
/*     */     
/* 294 */     if (tailOffset == this.capacityHint) {
/* 295 */       Node<T> n = new Node<T>(tailOffset);
/* 296 */       n.values[0] = t;
/* 297 */       this.tailOffset = 1;
/* 298 */       this.tail.next = n;
/* 299 */       this.tail = n;
/*     */     } else {
/* 301 */       this.tail.values[tailOffset] = t;
/* 302 */       this.tailOffset = tailOffset + 1;
/*     */     } 
/* 304 */     this.size++;
/* 305 */     for (CacheDisposable<T> consumer : (CacheDisposable[])this.observers.get()) {
/* 306 */       replay(consumer);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onError(Throwable t) {
/* 313 */     this.error = t;
/* 314 */     this.done = true;
/* 315 */     for (CacheDisposable<T> consumer : (CacheDisposable[])this.observers.getAndSet(TERMINATED)) {
/* 316 */       replay(consumer);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onComplete() {
/* 323 */     this.done = true;
/* 324 */     for (CacheDisposable<T> consumer : (CacheDisposable[])this.observers.getAndSet(TERMINATED)) {
/* 325 */       replay(consumer);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class CacheDisposable<T>
/*     */     extends AtomicInteger
/*     */     implements Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 6770240836423125754L;
/*     */ 
/*     */     
/*     */     final Observer<? super T> downstream;
/*     */ 
/*     */     
/*     */     final ObservableCache<T> parent;
/*     */ 
/*     */     
/*     */     ObservableCache.Node<T> node;
/*     */ 
/*     */     
/*     */     int offset;
/*     */ 
/*     */     
/*     */     long index;
/*     */ 
/*     */     
/*     */     volatile boolean disposed;
/*     */ 
/*     */ 
/*     */     
/*     */     CacheDisposable(Observer<? super T> downstream, ObservableCache<T> parent) {
/* 358 */       this.downstream = downstream;
/* 359 */       this.parent = parent;
/* 360 */       this.node = parent.head;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 365 */       if (!this.disposed) {
/* 366 */         this.disposed = true;
/* 367 */         this.parent.remove(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 373 */       return this.disposed;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class Node<T>
/*     */   {
/*     */     final T[] values;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     volatile Node<T> next;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Node(int capacityHint) {
/* 396 */       this.values = (T[])new Object[capacityHint];
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */