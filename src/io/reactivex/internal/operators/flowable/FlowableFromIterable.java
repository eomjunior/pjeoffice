/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.ConditionalSubscriber;
/*     */ import io.reactivex.internal.subscriptions.BasicQueueSubscription;
/*     */ import io.reactivex.internal.subscriptions.EmptySubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import java.util.Iterator;
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
/*     */ public final class FlowableFromIterable<T>
/*     */   extends Flowable<T>
/*     */ {
/*     */   final Iterable<? extends T> source;
/*     */   
/*     */   public FlowableFromIterable(Iterable<? extends T> source) {
/*  33 */     this.source = source;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Subscriber<? super T> s) {
/*     */     Iterator<? extends T> it;
/*     */     try {
/*  40 */       it = this.source.iterator();
/*  41 */     } catch (Throwable e) {
/*  42 */       Exceptions.throwIfFatal(e);
/*  43 */       EmptySubscription.error(e, s);
/*     */       
/*     */       return;
/*     */     } 
/*  47 */     subscribe(s, it);
/*     */   }
/*     */   
/*     */   public static <T> void subscribe(Subscriber<? super T> s, Iterator<? extends T> it) {
/*     */     boolean hasNext;
/*     */     try {
/*  53 */       hasNext = it.hasNext();
/*  54 */     } catch (Throwable e) {
/*  55 */       Exceptions.throwIfFatal(e);
/*  56 */       EmptySubscription.error(e, s);
/*     */       
/*     */       return;
/*     */     } 
/*  60 */     if (!hasNext) {
/*  61 */       EmptySubscription.complete(s);
/*     */       
/*     */       return;
/*     */     } 
/*  65 */     if (s instanceof ConditionalSubscriber) {
/*  66 */       s.onSubscribe((Subscription)new IteratorConditionalSubscription<T>((ConditionalSubscriber<? super T>)s, it));
/*     */     } else {
/*     */       
/*  69 */       s.onSubscribe((Subscription)new IteratorSubscription<T>(s, it));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static abstract class BaseRangeSubscription<T>
/*     */     extends BasicQueueSubscription<T>
/*     */   {
/*     */     private static final long serialVersionUID = -2252972430506210021L;
/*     */     Iterator<? extends T> it;
/*     */     volatile boolean cancelled;
/*     */     boolean once;
/*     */     
/*     */     BaseRangeSubscription(Iterator<? extends T> it) {
/*  83 */       this.it = it;
/*     */     }
/*     */ 
/*     */     
/*     */     public final int requestFusion(int mode) {
/*  88 */       return mode & 0x1;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public final T poll() {
/*  94 */       if (this.it == null) {
/*  95 */         return null;
/*     */       }
/*  97 */       if (!this.once) {
/*  98 */         this.once = true;
/*     */       }
/* 100 */       else if (!this.it.hasNext()) {
/* 101 */         return null;
/*     */       } 
/*     */       
/* 104 */       return (T)ObjectHelper.requireNonNull(this.it.next(), "Iterator.next() returned a null value");
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isEmpty() {
/* 109 */       return (this.it == null || !this.it.hasNext());
/*     */     }
/*     */ 
/*     */     
/*     */     public final void clear() {
/* 114 */       this.it = null;
/*     */     }
/*     */ 
/*     */     
/*     */     public final void request(long n) {
/* 119 */       if (SubscriptionHelper.validate(n) && 
/* 120 */         BackpressureHelper.add((AtomicLong)this, n) == 0L) {
/* 121 */         if (n == Long.MAX_VALUE) {
/* 122 */           fastPath();
/*     */         } else {
/* 124 */           slowPath(n);
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public final void cancel() {
/* 132 */       this.cancelled = true;
/*     */     }
/*     */     
/*     */     abstract void fastPath();
/*     */     
/*     */     abstract void slowPath(long param1Long);
/*     */   }
/*     */   
/*     */   static final class IteratorSubscription<T>
/*     */     extends BaseRangeSubscription<T>
/*     */   {
/*     */     private static final long serialVersionUID = -6022804456014692607L;
/*     */     final Subscriber<? super T> downstream;
/*     */     
/*     */     IteratorSubscription(Subscriber<? super T> actual, Iterator<? extends T> it) {
/* 147 */       super(it);
/* 148 */       this.downstream = actual;
/*     */     }
/*     */ 
/*     */     
/*     */     void fastPath() {
/* 153 */       Iterator<? extends T> it = this.it;
/* 154 */       Subscriber<? super T> a = this.downstream; while (true) {
/*     */         T t; boolean b;
/* 156 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 163 */           t = it.next();
/* 164 */         } catch (Throwable ex) {
/* 165 */           Exceptions.throwIfFatal(ex);
/* 166 */           a.onError(ex);
/*     */           
/*     */           return;
/*     */         } 
/* 170 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/*     */         
/* 174 */         if (t == null) {
/* 175 */           a.onError(new NullPointerException("Iterator.next() returned a null value"));
/*     */           return;
/*     */         } 
/* 178 */         a.onNext(t);
/*     */ 
/*     */         
/* 181 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 188 */           b = it.hasNext();
/* 189 */         } catch (Throwable ex) {
/* 190 */           Exceptions.throwIfFatal(ex);
/* 191 */           a.onError(ex);
/*     */           
/*     */           return;
/*     */         } 
/* 195 */         if (!b) {
/* 196 */           if (!this.cancelled) {
/* 197 */             a.onComplete();
/*     */           }
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     void slowPath(long r) {
/* 206 */       long e = 0L;
/* 207 */       Iterator<? extends T> it = this.it;
/* 208 */       Subscriber<? super T> a = this.downstream;
/*     */ 
/*     */       
/*     */       while (true) {
/* 212 */         while (e != r) {
/*     */           T t; boolean b;
/* 214 */           if (this.cancelled) {
/*     */             return;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           try {
/* 221 */             t = it.next();
/* 222 */           } catch (Throwable ex) {
/* 223 */             Exceptions.throwIfFatal(ex);
/* 224 */             a.onError(ex);
/*     */             
/*     */             return;
/*     */           } 
/* 228 */           if (this.cancelled) {
/*     */             return;
/*     */           }
/*     */           
/* 232 */           if (t == null) {
/* 233 */             a.onError(new NullPointerException("Iterator.next() returned a null value"));
/*     */             return;
/*     */           } 
/* 236 */           a.onNext(t);
/*     */ 
/*     */           
/* 239 */           if (this.cancelled) {
/*     */             return;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           try {
/* 246 */             b = it.hasNext();
/* 247 */           } catch (Throwable ex) {
/* 248 */             Exceptions.throwIfFatal(ex);
/* 249 */             a.onError(ex);
/*     */             
/*     */             return;
/*     */           } 
/* 253 */           if (!b) {
/* 254 */             if (!this.cancelled) {
/* 255 */               a.onComplete();
/*     */             }
/*     */             
/*     */             return;
/*     */           } 
/* 260 */           e++;
/*     */         } 
/*     */         
/* 263 */         r = get();
/* 264 */         if (e == r) {
/* 265 */           r = addAndGet(-e);
/* 266 */           if (r == 0L) {
/*     */             return;
/*     */           }
/* 269 */           e = 0L;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class IteratorConditionalSubscription<T>
/*     */     extends BaseRangeSubscription<T>
/*     */   {
/*     */     private static final long serialVersionUID = -6022804456014692607L;
/*     */     final ConditionalSubscriber<? super T> downstream;
/*     */     
/*     */     IteratorConditionalSubscription(ConditionalSubscriber<? super T> actual, Iterator<? extends T> it) {
/* 283 */       super(it);
/* 284 */       this.downstream = actual;
/*     */     }
/*     */ 
/*     */     
/*     */     void fastPath() {
/* 289 */       Iterator<? extends T> it = this.it;
/* 290 */       ConditionalSubscriber<? super T> a = this.downstream; while (true) {
/*     */         T t; boolean b;
/* 292 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 299 */           t = it.next();
/* 300 */         } catch (Throwable ex) {
/* 301 */           Exceptions.throwIfFatal(ex);
/* 302 */           a.onError(ex);
/*     */           
/*     */           return;
/*     */         } 
/* 306 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/*     */         
/* 310 */         if (t == null) {
/* 311 */           a.onError(new NullPointerException("Iterator.next() returned a null value"));
/*     */           return;
/*     */         } 
/* 314 */         a.tryOnNext(t);
/*     */ 
/*     */         
/* 317 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 324 */           b = it.hasNext();
/* 325 */         } catch (Throwable ex) {
/* 326 */           Exceptions.throwIfFatal(ex);
/* 327 */           a.onError(ex);
/*     */           
/*     */           return;
/*     */         } 
/* 331 */         if (!b) {
/* 332 */           if (!this.cancelled) {
/* 333 */             a.onComplete();
/*     */           }
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     void slowPath(long r) {
/* 342 */       long e = 0L;
/* 343 */       Iterator<? extends T> it = this.it;
/* 344 */       ConditionalSubscriber<? super T> a = this.downstream;
/*     */ 
/*     */       
/*     */       while (true) {
/* 348 */         while (e != r) {
/*     */           T t; boolean hasNext;
/* 350 */           if (this.cancelled) {
/*     */             return;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           try {
/* 357 */             t = it.next();
/* 358 */           } catch (Throwable ex) {
/* 359 */             Exceptions.throwIfFatal(ex);
/* 360 */             a.onError(ex);
/*     */             
/*     */             return;
/*     */           } 
/* 364 */           if (this.cancelled) {
/*     */             return;
/*     */           }
/*     */ 
/*     */           
/* 369 */           if (t == null) {
/* 370 */             a.onError(new NullPointerException("Iterator.next() returned a null value"));
/*     */             return;
/*     */           } 
/* 373 */           boolean b = a.tryOnNext(t);
/*     */ 
/*     */           
/* 376 */           if (this.cancelled) {
/*     */             return;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           try {
/* 383 */             hasNext = it.hasNext();
/* 384 */           } catch (Throwable ex) {
/* 385 */             Exceptions.throwIfFatal(ex);
/* 386 */             a.onError(ex);
/*     */             
/*     */             return;
/*     */           } 
/* 390 */           if (!hasNext) {
/* 391 */             if (!this.cancelled) {
/* 392 */               a.onComplete();
/*     */             }
/*     */             
/*     */             return;
/*     */           } 
/* 397 */           if (b) {
/* 398 */             e++;
/*     */           }
/*     */         } 
/*     */         
/* 402 */         r = get();
/* 403 */         if (e == r) {
/* 404 */           r = addAndGet(-e);
/* 405 */           if (r == 0L) {
/*     */             return;
/*     */           }
/* 408 */           e = 0L;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableFromIterable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */