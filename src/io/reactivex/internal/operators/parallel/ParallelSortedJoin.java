/*     */ package io.reactivex.internal.operators.parallel;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.parallel.ParallelFlowable;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicLong;
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
/*     */ public final class ParallelSortedJoin<T>
/*     */   extends Flowable<T>
/*     */ {
/*     */   final ParallelFlowable<List<T>> source;
/*     */   final Comparator<? super T> comparator;
/*     */   
/*     */   public ParallelSortedJoin(ParallelFlowable<List<T>> source, Comparator<? super T> comparator) {
/*  43 */     this.source = source;
/*  44 */     this.comparator = comparator;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  49 */     SortedJoinSubscription<T> parent = new SortedJoinSubscription<T>(s, this.source.parallelism(), this.comparator);
/*  50 */     s.onSubscribe(parent);
/*     */     
/*  52 */     this.source.subscribe((Subscriber[])parent.subscribers);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class SortedJoinSubscription<T>
/*     */     extends AtomicInteger
/*     */     implements Subscription
/*     */   {
/*     */     private static final long serialVersionUID = 3481980673745556697L;
/*     */     
/*     */     final Subscriber<? super T> downstream;
/*     */     
/*     */     final ParallelSortedJoin.SortedJoinInnerSubscriber<T>[] subscribers;
/*     */     
/*     */     final List<T>[] lists;
/*     */     
/*     */     final int[] indexes;
/*     */     
/*     */     final Comparator<? super T> comparator;
/*  71 */     final AtomicLong requested = new AtomicLong();
/*     */     
/*     */     volatile boolean cancelled;
/*     */     
/*  75 */     final AtomicInteger remaining = new AtomicInteger();
/*     */     
/*  77 */     final AtomicReference<Throwable> error = new AtomicReference<Throwable>();
/*     */ 
/*     */     
/*     */     SortedJoinSubscription(Subscriber<? super T> actual, int n, Comparator<? super T> comparator) {
/*  81 */       this.downstream = actual;
/*  82 */       this.comparator = comparator;
/*     */       
/*  84 */       ParallelSortedJoin.SortedJoinInnerSubscriber[] arrayOfSortedJoinInnerSubscriber = new ParallelSortedJoin.SortedJoinInnerSubscriber[n];
/*     */       
/*  86 */       for (int i = 0; i < n; i++) {
/*  87 */         arrayOfSortedJoinInnerSubscriber[i] = new ParallelSortedJoin.SortedJoinInnerSubscriber<T>(this, i);
/*     */       }
/*  89 */       this.subscribers = (ParallelSortedJoin.SortedJoinInnerSubscriber<T>[])arrayOfSortedJoinInnerSubscriber;
/*  90 */       this.lists = (List<T>[])new List[n];
/*  91 */       this.indexes = new int[n];
/*  92 */       this.remaining.lazySet(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/*  97 */       if (SubscriptionHelper.validate(n)) {
/*  98 */         BackpressureHelper.add(this.requested, n);
/*  99 */         if (this.remaining.get() == 0) {
/* 100 */           drain();
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 107 */       if (!this.cancelled) {
/* 108 */         this.cancelled = true;
/* 109 */         cancelAll();
/* 110 */         if (getAndIncrement() == 0) {
/* 111 */           Arrays.fill((Object[])this.lists, (Object)null);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     void cancelAll() {
/* 117 */       for (ParallelSortedJoin.SortedJoinInnerSubscriber<T> s : this.subscribers) {
/* 118 */         s.cancel();
/*     */       }
/*     */     }
/*     */     
/*     */     void innerNext(List<T> value, int index) {
/* 123 */       this.lists[index] = value;
/* 124 */       if (this.remaining.decrementAndGet() == 0) {
/* 125 */         drain();
/*     */       }
/*     */     }
/*     */     
/*     */     void innerError(Throwable e) {
/* 130 */       if (this.error.compareAndSet(null, e)) {
/* 131 */         drain();
/*     */       }
/* 133 */       else if (e != this.error.get()) {
/* 134 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     void drain() {
/* 140 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 144 */       int missed = 1;
/* 145 */       Subscriber<? super T> a = this.downstream;
/* 146 */       List<T>[] lists = this.lists;
/* 147 */       int[] indexes = this.indexes;
/* 148 */       int n = indexes.length;
/*     */ 
/*     */       
/*     */       while (true) {
/* 152 */         long r = this.requested.get();
/* 153 */         long e = 0L;
/*     */         
/* 155 */         while (e != r) {
/* 156 */           if (this.cancelled) {
/* 157 */             Arrays.fill((Object[])lists, (Object)null);
/*     */             
/*     */             return;
/*     */           } 
/* 161 */           Throwable ex = this.error.get();
/* 162 */           if (ex != null) {
/* 163 */             cancelAll();
/* 164 */             Arrays.fill((Object[])lists, (Object)null);
/* 165 */             a.onError(ex);
/*     */             
/*     */             return;
/*     */           } 
/* 169 */           T min = null;
/* 170 */           int minIndex = -1;
/*     */           
/* 172 */           for (int i = 0; i < n; i++) {
/* 173 */             List<T> list = lists[i];
/* 174 */             int index = indexes[i];
/*     */             
/* 176 */             if (list.size() != index) {
/* 177 */               if (min == null) {
/* 178 */                 min = list.get(index);
/* 179 */                 minIndex = i;
/*     */               } else {
/* 181 */                 boolean smaller; T b = list.get(index);
/*     */ 
/*     */ 
/*     */                 
/*     */                 try {
/* 186 */                   smaller = (this.comparator.compare(min, b) > 0);
/* 187 */                 } catch (Throwable exc) {
/* 188 */                   Exceptions.throwIfFatal(exc);
/* 189 */                   cancelAll();
/* 190 */                   Arrays.fill((Object[])lists, (Object)null);
/* 191 */                   if (!this.error.compareAndSet(null, exc)) {
/* 192 */                     RxJavaPlugins.onError(exc);
/*     */                   }
/* 194 */                   a.onError(this.error.get());
/*     */                   return;
/*     */                 } 
/* 197 */                 if (smaller) {
/* 198 */                   min = b;
/* 199 */                   minIndex = i;
/*     */                 } 
/*     */               } 
/*     */             }
/*     */           } 
/*     */           
/* 205 */           if (min == null) {
/* 206 */             Arrays.fill((Object[])lists, (Object)null);
/* 207 */             a.onComplete();
/*     */             
/*     */             return;
/*     */           } 
/* 211 */           a.onNext(min);
/*     */           
/* 213 */           indexes[minIndex] = indexes[minIndex] + 1;
/*     */           
/* 215 */           e++;
/*     */         } 
/*     */         
/* 218 */         if (e == r) {
/* 219 */           if (this.cancelled) {
/* 220 */             Arrays.fill((Object[])lists, (Object)null);
/*     */             
/*     */             return;
/*     */           } 
/* 224 */           Throwable ex = this.error.get();
/* 225 */           if (ex != null) {
/* 226 */             cancelAll();
/* 227 */             Arrays.fill((Object[])lists, (Object)null);
/* 228 */             a.onError(ex);
/*     */             
/*     */             return;
/*     */           } 
/* 232 */           boolean empty = true;
/*     */           
/* 234 */           for (int i = 0; i < n; i++) {
/* 235 */             if (indexes[i] != lists[i].size()) {
/* 236 */               empty = false;
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/* 241 */           if (empty) {
/* 242 */             Arrays.fill((Object[])lists, (Object)null);
/* 243 */             a.onComplete();
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 248 */         if (e != 0L && r != Long.MAX_VALUE) {
/* 249 */           this.requested.addAndGet(-e);
/*     */         }
/*     */         
/* 252 */         int w = get();
/* 253 */         if (w == missed) {
/* 254 */           missed = addAndGet(-missed);
/* 255 */           if (missed == 0)
/*     */             break; 
/*     */           continue;
/*     */         } 
/* 259 */         missed = w;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class SortedJoinInnerSubscriber<T>
/*     */     extends AtomicReference<Subscription>
/*     */     implements FlowableSubscriber<List<T>>
/*     */   {
/*     */     private static final long serialVersionUID = 6751017204873808094L;
/*     */     
/*     */     final ParallelSortedJoin.SortedJoinSubscription<T> parent;
/*     */     
/*     */     final int index;
/*     */     
/*     */     SortedJoinInnerSubscriber(ParallelSortedJoin.SortedJoinSubscription<T> parent, int index) {
/* 276 */       this.parent = parent;
/* 277 */       this.index = index;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 282 */       SubscriptionHelper.setOnce(this, s, Long.MAX_VALUE);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(List<T> t) {
/* 287 */       this.parent.innerNext(t, this.index);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 292 */       this.parent.innerError(t);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onComplete() {}
/*     */ 
/*     */     
/*     */     void cancel() {
/* 301 */       SubscriptionHelper.cancel(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/parallel/ParallelSortedJoin.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */