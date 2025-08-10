/*     */ package io.reactivex.internal.operators.parallel;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.BiFunction;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.parallel.ParallelFlowable;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ParallelReduceFull<T>
/*     */   extends Flowable<T>
/*     */ {
/*     */   final ParallelFlowable<? extends T> source;
/*     */   final BiFunction<T, T, T> reducer;
/*     */   
/*     */   public ParallelReduceFull(ParallelFlowable<? extends T> source, BiFunction<T, T, T> reducer) {
/*  41 */     this.source = source;
/*  42 */     this.reducer = reducer;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  47 */     ParallelReduceFullMainSubscriber<T> parent = new ParallelReduceFullMainSubscriber<T>(s, this.source.parallelism(), this.reducer);
/*  48 */     s.onSubscribe((Subscription)parent);
/*     */     
/*  50 */     this.source.subscribe((Subscriber[])parent.subscribers);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ParallelReduceFullMainSubscriber<T>
/*     */     extends DeferredScalarSubscription<T>
/*     */   {
/*     */     private static final long serialVersionUID = -5370107872170712765L;
/*     */     
/*     */     final ParallelReduceFull.ParallelReduceFullInnerSubscriber<T>[] subscribers;
/*     */     final BiFunction<T, T, T> reducer;
/*  61 */     final AtomicReference<ParallelReduceFull.SlotPair<T>> current = new AtomicReference<ParallelReduceFull.SlotPair<T>>();
/*     */     
/*  63 */     final AtomicInteger remaining = new AtomicInteger();
/*     */     
/*  65 */     final AtomicReference<Throwable> error = new AtomicReference<Throwable>();
/*     */     
/*     */     ParallelReduceFullMainSubscriber(Subscriber<? super T> subscriber, int n, BiFunction<T, T, T> reducer) {
/*  68 */       super(subscriber);
/*     */       
/*  70 */       ParallelReduceFull.ParallelReduceFullInnerSubscriber[] arrayOfParallelReduceFullInnerSubscriber = new ParallelReduceFull.ParallelReduceFullInnerSubscriber[n];
/*  71 */       for (int i = 0; i < n; i++) {
/*  72 */         arrayOfParallelReduceFullInnerSubscriber[i] = new ParallelReduceFull.ParallelReduceFullInnerSubscriber<T>(this, reducer);
/*     */       }
/*  74 */       this.subscribers = (ParallelReduceFull.ParallelReduceFullInnerSubscriber<T>[])arrayOfParallelReduceFullInnerSubscriber;
/*  75 */       this.reducer = reducer;
/*  76 */       this.remaining.lazySet(n);
/*     */     } ParallelReduceFull.SlotPair<T> addValue(T value) {
/*     */       ParallelReduceFull.SlotPair<T> curr;
/*     */       int c;
/*     */       while (true) {
/*  81 */         curr = this.current.get();
/*     */         
/*  83 */         if (curr == null) {
/*  84 */           curr = new ParallelReduceFull.SlotPair<T>();
/*  85 */           if (!this.current.compareAndSet(null, curr)) {
/*     */             continue;
/*     */           }
/*     */         } 
/*     */         
/*  90 */         c = curr.tryAcquireSlot();
/*  91 */         if (c < 0) {
/*  92 */           this.current.compareAndSet(curr, null); continue;
/*     */         }  break;
/*     */       } 
/*  95 */       if (c == 0) {
/*  96 */         curr.first = value;
/*     */       } else {
/*  98 */         curr.second = value;
/*     */       } 
/*     */       
/* 101 */       if (curr.releaseSlot()) {
/* 102 */         this.current.compareAndSet(curr, null);
/* 103 */         return curr;
/*     */       } 
/* 105 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 111 */       for (ParallelReduceFull.ParallelReduceFullInnerSubscriber<T> inner : this.subscribers) {
/* 112 */         inner.cancel();
/*     */       }
/*     */     }
/*     */     
/*     */     void innerError(Throwable ex) {
/* 117 */       if (this.error.compareAndSet(null, ex)) {
/* 118 */         cancel();
/* 119 */         this.downstream.onError(ex);
/*     */       }
/* 121 */       else if (ex != this.error.get()) {
/* 122 */         RxJavaPlugins.onError(ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     void innerComplete(T value) {
/* 128 */       if (value != null) {
/*     */         while (true) {
/* 130 */           ParallelReduceFull.SlotPair<T> sp = addValue(value);
/*     */           
/* 132 */           if (sp != null) {
/*     */             
/*     */             try {
/* 135 */               value = (T)ObjectHelper.requireNonNull(this.reducer.apply(sp.first, sp.second), "The reducer returned a null value");
/* 136 */             } catch (Throwable ex) {
/* 137 */               Exceptions.throwIfFatal(ex);
/* 138 */               innerError(ex);
/*     */               
/*     */               return;
/*     */             } 
/*     */             
/*     */             continue;
/*     */           } 
/*     */           break;
/*     */         } 
/*     */       }
/* 148 */       if (this.remaining.decrementAndGet() == 0) {
/* 149 */         ParallelReduceFull.SlotPair<T> sp = this.current.get();
/* 150 */         this.current.lazySet(null);
/*     */         
/* 152 */         if (sp != null) {
/* 153 */           complete(sp.first);
/*     */         } else {
/* 155 */           this.downstream.onComplete();
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ParallelReduceFullInnerSubscriber<T>
/*     */     extends AtomicReference<Subscription>
/*     */     implements FlowableSubscriber<T>
/*     */   {
/*     */     private static final long serialVersionUID = -7954444275102466525L;
/*     */     
/*     */     final ParallelReduceFull.ParallelReduceFullMainSubscriber<T> parent;
/*     */     
/*     */     final BiFunction<T, T, T> reducer;
/*     */     
/*     */     T value;
/*     */     boolean done;
/*     */     
/*     */     ParallelReduceFullInnerSubscriber(ParallelReduceFull.ParallelReduceFullMainSubscriber<T> parent, BiFunction<T, T, T> reducer) {
/* 176 */       this.parent = parent;
/* 177 */       this.reducer = reducer;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 182 */       SubscriptionHelper.setOnce(this, s, Long.MAX_VALUE);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 187 */       if (!this.done) {
/* 188 */         T v = this.value;
/*     */         
/* 190 */         if (v == null) {
/* 191 */           this.value = t;
/*     */         } else {
/*     */           
/*     */           try {
/* 195 */             v = (T)ObjectHelper.requireNonNull(this.reducer.apply(v, t), "The reducer returned a null value");
/* 196 */           } catch (Throwable ex) {
/* 197 */             Exceptions.throwIfFatal(ex);
/* 198 */             get().cancel();
/* 199 */             onError(ex);
/*     */             
/*     */             return;
/*     */           } 
/* 203 */           this.value = v;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 210 */       if (this.done) {
/* 211 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 214 */       this.done = true;
/* 215 */       this.parent.innerError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 220 */       if (!this.done) {
/* 221 */         this.done = true;
/* 222 */         this.parent.innerComplete(this.value);
/*     */       } 
/*     */     }
/*     */     
/*     */     void cancel() {
/* 227 */       SubscriptionHelper.cancel(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class SlotPair<T>
/*     */     extends AtomicInteger
/*     */   {
/*     */     private static final long serialVersionUID = 473971317683868662L;
/*     */     
/*     */     T first;
/*     */     T second;
/* 239 */     final AtomicInteger releaseIndex = new AtomicInteger();
/*     */     
/*     */     int tryAcquireSlot() {
/*     */       while (true) {
/* 243 */         int acquired = get();
/* 244 */         if (acquired >= 2) {
/* 245 */           return -1;
/*     */         }
/*     */         
/* 248 */         if (compareAndSet(acquired, acquired + 1)) {
/* 249 */           return acquired;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     boolean releaseSlot() {
/* 255 */       return (this.releaseIndex.incrementAndGet() == 2);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/parallel/ParallelReduceFull.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */