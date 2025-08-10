/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.ConditionalSubscriber;
/*     */ import io.reactivex.internal.subscriptions.BasicQueueSubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
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
/*     */ public final class FlowableFromArray<T>
/*     */   extends Flowable<T>
/*     */ {
/*     */   final T[] array;
/*     */   
/*     */   public FlowableFromArray(T[] array) {
/*  29 */     this.array = array;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Subscriber<? super T> s) {
/*  34 */     if (s instanceof ConditionalSubscriber) {
/*  35 */       s.onSubscribe((Subscription)new ArrayConditionalSubscription<T>((ConditionalSubscriber<? super T>)s, this.array));
/*     */     } else {
/*     */       
/*  38 */       s.onSubscribe((Subscription)new ArraySubscription<T>(s, this.array));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static abstract class BaseArraySubscription<T>
/*     */     extends BasicQueueSubscription<T>
/*     */   {
/*     */     private static final long serialVersionUID = -2252972430506210021L;
/*     */     final T[] array;
/*     */     int index;
/*     */     volatile boolean cancelled;
/*     */     
/*     */     BaseArraySubscription(T[] array) {
/*  52 */       this.array = array;
/*     */     }
/*     */ 
/*     */     
/*     */     public final int requestFusion(int mode) {
/*  57 */       return mode & 0x1;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public final T poll() {
/*  63 */       int i = this.index;
/*  64 */       T[] arr = this.array;
/*  65 */       if (i == arr.length) {
/*  66 */         return null;
/*     */       }
/*     */       
/*  69 */       this.index = i + 1;
/*  70 */       return (T)ObjectHelper.requireNonNull(arr[i], "array element is null");
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isEmpty() {
/*  75 */       return (this.index == this.array.length);
/*     */     }
/*     */ 
/*     */     
/*     */     public final void clear() {
/*  80 */       this.index = this.array.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public final void request(long n) {
/*  85 */       if (SubscriptionHelper.validate(n) && 
/*  86 */         BackpressureHelper.add((AtomicLong)this, n) == 0L) {
/*  87 */         if (n == Long.MAX_VALUE) {
/*  88 */           fastPath();
/*     */         } else {
/*  90 */           slowPath(n);
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public final void cancel() {
/*  98 */       this.cancelled = true;
/*     */     }
/*     */     
/*     */     abstract void fastPath();
/*     */     
/*     */     abstract void slowPath(long param1Long);
/*     */   }
/*     */   
/*     */   static final class ArraySubscription<T>
/*     */     extends BaseArraySubscription<T>
/*     */   {
/*     */     private static final long serialVersionUID = 2587302975077663557L;
/*     */     final Subscriber<? super T> downstream;
/*     */     
/*     */     ArraySubscription(Subscriber<? super T> actual, T[] array) {
/* 113 */       super(array);
/* 114 */       this.downstream = actual;
/*     */     }
/*     */ 
/*     */     
/*     */     void fastPath() {
/* 119 */       T[] arr = this.array;
/* 120 */       int f = arr.length;
/* 121 */       Subscriber<? super T> a = this.downstream;
/*     */       
/* 123 */       for (int i = this.index; i != f; i++) {
/* 124 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/* 127 */         T t = arr[i];
/* 128 */         if (t == null) {
/* 129 */           a.onError(new NullPointerException("The element at index " + i + " is null"));
/*     */           return;
/*     */         } 
/* 132 */         a.onNext(t);
/*     */       } 
/*     */       
/* 135 */       if (this.cancelled) {
/*     */         return;
/*     */       }
/* 138 */       a.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     void slowPath(long r) {
/* 143 */       long e = 0L;
/* 144 */       T[] arr = this.array;
/* 145 */       int f = arr.length;
/* 146 */       int i = this.index;
/* 147 */       Subscriber<? super T> a = this.downstream;
/*     */ 
/*     */       
/*     */       while (true) {
/* 151 */         if (e != r && i != f) {
/* 152 */           if (this.cancelled) {
/*     */             return;
/*     */           }
/*     */           
/* 156 */           T t = arr[i];
/*     */           
/* 158 */           if (t == null) {
/* 159 */             a.onError(new NullPointerException("The element at index " + i + " is null"));
/*     */             return;
/*     */           } 
/* 162 */           a.onNext(t);
/*     */ 
/*     */           
/* 165 */           e++;
/* 166 */           i++;
/*     */           continue;
/*     */         } 
/* 169 */         if (i == f) {
/* 170 */           if (!this.cancelled) {
/* 171 */             a.onComplete();
/*     */           }
/*     */           
/*     */           return;
/*     */         } 
/* 176 */         r = get();
/* 177 */         if (e == r) {
/* 178 */           this.index = i;
/* 179 */           r = addAndGet(-e);
/* 180 */           if (r == 0L) {
/*     */             return;
/*     */           }
/* 183 */           e = 0L;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static final class ArrayConditionalSubscription<T>
/*     */     extends BaseArraySubscription<T>
/*     */   {
/*     */     private static final long serialVersionUID = 2587302975077663557L;
/*     */     final ConditionalSubscriber<? super T> downstream;
/*     */     
/*     */     ArrayConditionalSubscription(ConditionalSubscriber<? super T> actual, T[] array) {
/* 196 */       super(array);
/* 197 */       this.downstream = actual;
/*     */     }
/*     */ 
/*     */     
/*     */     void fastPath() {
/* 202 */       T[] arr = this.array;
/* 203 */       int f = arr.length;
/* 204 */       ConditionalSubscriber<? super T> a = this.downstream;
/*     */       
/* 206 */       for (int i = this.index; i != f; i++) {
/* 207 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/* 210 */         T t = arr[i];
/* 211 */         if (t == null) {
/* 212 */           a.onError(new NullPointerException("The element at index " + i + " is null"));
/*     */           return;
/*     */         } 
/* 215 */         a.tryOnNext(t);
/*     */       } 
/*     */       
/* 218 */       if (this.cancelled) {
/*     */         return;
/*     */       }
/* 221 */       a.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     void slowPath(long r) {
/* 226 */       long e = 0L;
/* 227 */       T[] arr = this.array;
/* 228 */       int f = arr.length;
/* 229 */       int i = this.index;
/* 230 */       ConditionalSubscriber<? super T> a = this.downstream;
/*     */ 
/*     */       
/*     */       while (true) {
/* 234 */         if (e != r && i != f) {
/* 235 */           if (this.cancelled) {
/*     */             return;
/*     */           }
/*     */           
/* 239 */           T t = arr[i];
/*     */           
/* 241 */           if (t == null) {
/* 242 */             a.onError(new NullPointerException("The element at index " + i + " is null"));
/*     */             return;
/*     */           } 
/* 245 */           if (a.tryOnNext(t)) {
/* 246 */             e++;
/*     */           }
/*     */           
/* 249 */           i++;
/*     */           
/*     */           continue;
/*     */         } 
/* 253 */         if (i == f) {
/* 254 */           if (!this.cancelled) {
/* 255 */             a.onComplete();
/*     */           }
/*     */           
/*     */           return;
/*     */         } 
/* 260 */         r = get();
/* 261 */         if (e == r) {
/* 262 */           this.index = i;
/* 263 */           r = addAndGet(-e);
/* 264 */           if (r == 0L) {
/*     */             return;
/*     */           }
/* 267 */           e = 0L;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableFromArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */