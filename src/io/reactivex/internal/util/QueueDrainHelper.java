/*     */ package io.reactivex.internal.util;
/*     */ 
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.functions.BooleanSupplier;
/*     */ import io.reactivex.internal.fuseable.SimplePlainQueue;
/*     */ import io.reactivex.internal.fuseable.SimpleQueue;
/*     */ import io.reactivex.internal.queue.SpscArrayQueue;
/*     */ import io.reactivex.internal.queue.SpscLinkedArrayQueue;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import org.reactivestreams.Subscriber;
/*     */ import org.reactivestreams.Subscription;
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
/*     */ public final class QueueDrainHelper
/*     */ {
/*     */   static final long COMPLETED_MASK = -9223372036854775808L;
/*     */   static final long REQUESTED_MASK = 9223372036854775807L;
/*     */   
/*     */   private QueueDrainHelper() {
/*  33 */     throw new IllegalStateException("No instances!");
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
/*     */ 
/*     */   
/*     */   public static <T, U> void drainMaxLoop(SimplePlainQueue<T> q, Subscriber<? super U> a, boolean delayError, Disposable dispose, QueueDrain<T, U> qd) {
/*  48 */     int missed = 1;
/*     */ 
/*     */     
/*     */     while (true) {
/*  52 */       boolean d = qd.done();
/*     */       
/*  54 */       T v = (T)q.poll();
/*     */       
/*  56 */       boolean empty = (v == null);
/*     */       
/*  58 */       if (checkTerminated(d, empty, a, delayError, (SimpleQueue<?>)q, qd)) {
/*  59 */         if (dispose != null) {
/*  60 */           dispose.dispose();
/*     */         }
/*     */         
/*     */         return;
/*     */       } 
/*  65 */       if (empty) {
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
/*  86 */         missed = qd.leave(-missed);
/*  87 */         if (missed == 0)
/*     */           break;  continue;
/*     */       }  long r = qd.requested(); if (r != 0L) {
/*     */         if (qd.accept(a, v) && r != Long.MAX_VALUE)
/*     */           qd.produced(1L);  continue;
/*     */       }  q.clear(); if (dispose != null)
/*     */         dispose.dispose();  a.onError((Throwable)new MissingBackpressureException("Could not emit value due to lack of requests."));
/*     */       return;
/*  95 */     }  } public static <T, U> boolean checkTerminated(boolean d, boolean empty, Subscriber<?> s, boolean delayError, SimpleQueue<?> q, QueueDrain<T, U> qd) { if (qd.cancelled()) {
/*  96 */       q.clear();
/*  97 */       return true;
/*     */     } 
/*     */     
/* 100 */     if (d) {
/* 101 */       if (delayError) {
/* 102 */         if (empty) {
/* 103 */           Throwable err = qd.error();
/* 104 */           if (err != null) {
/* 105 */             s.onError(err);
/*     */           } else {
/* 107 */             s.onComplete();
/*     */           } 
/* 109 */           return true;
/*     */         } 
/*     */       } else {
/* 112 */         Throwable err = qd.error();
/* 113 */         if (err != null) {
/* 114 */           q.clear();
/* 115 */           s.onError(err);
/* 116 */           return true;
/*     */         } 
/* 118 */         if (empty) {
/* 119 */           s.onComplete();
/* 120 */           return true;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 125 */     return false; }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T, U> void drainLoop(SimplePlainQueue<T> q, Observer<? super U> a, boolean delayError, Disposable dispose, ObservableQueueDrain<T, U> qd) {
/* 130 */     int missed = 1;
/*     */     
/*     */     do {
/* 133 */       if (checkTerminated(qd.done(), q.isEmpty(), a, delayError, (SimpleQueue<?>)q, dispose, qd)) {
/*     */         return;
/*     */       }
/*     */       
/*     */       while (true) {
/* 138 */         boolean d = qd.done();
/* 139 */         T v = (T)q.poll();
/* 140 */         boolean empty = (v == null);
/*     */         
/* 142 */         if (checkTerminated(d, empty, a, delayError, (SimpleQueue<?>)q, dispose, qd)) {
/*     */           return;
/*     */         }
/*     */         
/* 146 */         if (empty) {
/*     */           break;
/*     */         }
/*     */         
/* 150 */         qd.accept(a, v);
/*     */       } 
/*     */       
/* 153 */       missed = qd.leave(-missed);
/* 154 */     } while (missed != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T, U> boolean checkTerminated(boolean d, boolean empty, Observer<?> observer, boolean delayError, SimpleQueue<?> q, Disposable disposable, ObservableQueueDrain<T, U> qd) {
/* 162 */     if (qd.cancelled()) {
/* 163 */       q.clear();
/* 164 */       disposable.dispose();
/* 165 */       return true;
/*     */     } 
/*     */     
/* 168 */     if (d) {
/* 169 */       if (delayError) {
/* 170 */         if (empty) {
/* 171 */           if (disposable != null) {
/* 172 */             disposable.dispose();
/*     */           }
/* 174 */           Throwable err = qd.error();
/* 175 */           if (err != null) {
/* 176 */             observer.onError(err);
/*     */           } else {
/* 178 */             observer.onComplete();
/*     */           } 
/* 180 */           return true;
/*     */         } 
/*     */       } else {
/* 183 */         Throwable err = qd.error();
/* 184 */         if (err != null) {
/* 185 */           q.clear();
/* 186 */           if (disposable != null) {
/* 187 */             disposable.dispose();
/*     */           }
/* 189 */           observer.onError(err);
/* 190 */           return true;
/*     */         } 
/* 192 */         if (empty) {
/* 193 */           if (disposable != null) {
/* 194 */             disposable.dispose();
/*     */           }
/* 196 */           observer.onComplete();
/* 197 */           return true;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 202 */     return false;
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
/*     */   public static <T> SimpleQueue<T> createQueue(int capacityHint) {
/* 214 */     if (capacityHint < 0) {
/* 215 */       return (SimpleQueue<T>)new SpscLinkedArrayQueue(-capacityHint);
/*     */     }
/* 217 */     return (SimpleQueue<T>)new SpscArrayQueue(capacityHint);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void request(Subscription s, int prefetch) {
/* 227 */     s.request((prefetch < 0) ? Long.MAX_VALUE : prefetch);
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
/*     */   public static <T> boolean postCompleteRequest(long n, Subscriber<? super T> actual, Queue<T> queue, AtomicLong state, BooleanSupplier isCancelled) {
/*     */     long r;
/*     */     long u;
/*     */     do {
/* 258 */       r = state.get();
/*     */ 
/*     */       
/* 261 */       long r0 = r & Long.MAX_VALUE;
/*     */ 
/*     */       
/* 264 */       u = r & Long.MIN_VALUE | BackpressureHelper.addCap(r0, n);
/*     */     }
/* 266 */     while (!state.compareAndSet(r, u));
/*     */     
/* 268 */     if (r == Long.MIN_VALUE) {
/*     */       
/* 270 */       postCompleteDrain(n | Long.MIN_VALUE, actual, queue, state, isCancelled);
/*     */       
/* 272 */       return true;
/*     */     } 
/*     */     
/* 275 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isCancelled(BooleanSupplier cancelled) {
/*     */     try {
/* 283 */       return cancelled.getAsBoolean();
/* 284 */     } catch (Throwable ex) {
/* 285 */       Exceptions.throwIfFatal(ex);
/* 286 */       return true;
/*     */     } 
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
/*     */   static <T> boolean postCompleteDrain(long n, Subscriber<? super T> actual, Queue<T> queue, AtomicLong state, BooleanSupplier isCancelled) {
/* 326 */     long e = n & Long.MIN_VALUE;
/*     */ 
/*     */     
/*     */     while (true) {
/* 330 */       while (e != n) {
/* 331 */         if (isCancelled(isCancelled)) {
/* 332 */           return true;
/*     */         }
/*     */         
/* 335 */         T t = queue.poll();
/*     */         
/* 337 */         if (t == null) {
/* 338 */           actual.onComplete();
/* 339 */           return true;
/*     */         } 
/*     */         
/* 342 */         actual.onNext(t);
/* 343 */         e++;
/*     */       } 
/*     */       
/* 346 */       if (isCancelled(isCancelled)) {
/* 347 */         return true;
/*     */       }
/*     */       
/* 350 */       if (queue.isEmpty()) {
/* 351 */         actual.onComplete();
/* 352 */         return true;
/*     */       } 
/*     */       
/* 355 */       n = state.get();
/*     */       
/* 357 */       if (n == e) {
/*     */         
/* 359 */         n = state.addAndGet(-(e & Long.MAX_VALUE));
/*     */         
/* 361 */         if ((n & Long.MAX_VALUE) == 0L) {
/* 362 */           return false;
/*     */         }
/*     */         
/* 365 */         e = n & Long.MIN_VALUE;
/*     */       } 
/*     */     } 
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
/*     */   public static <T> void postComplete(Subscriber<? super T> actual, Queue<T> queue, AtomicLong state, BooleanSupplier isCancelled) {
/*     */     long r;
/*     */     long u;
/* 400 */     if (queue.isEmpty()) {
/* 401 */       actual.onComplete();
/*     */       
/*     */       return;
/*     */     } 
/* 405 */     if (postCompleteDrain(state.get(), actual, queue, state, isCancelled)) {
/*     */       return;
/*     */     }
/*     */     
/*     */     do {
/* 410 */       r = state.get();
/*     */       
/* 412 */       if ((r & Long.MIN_VALUE) != 0L) {
/*     */         return;
/*     */       }
/*     */       
/* 416 */       u = r | Long.MIN_VALUE;
/*     */     }
/* 418 */     while (!state.compareAndSet(r, u));
/*     */     
/* 420 */     if (r != 0L)
/* 421 */       postCompleteDrain(u, actual, queue, state, isCancelled); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/util/QueueDrainHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */