/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.Single;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.BiPredicate;
/*     */ import io.reactivex.internal.fuseable.FuseToFlowable;
/*     */ import io.reactivex.internal.fuseable.SimpleQueue;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.reactivestreams.Subscriber;
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
/*     */ public final class FlowableSequenceEqualSingle<T>
/*     */   extends Single<Boolean>
/*     */   implements FuseToFlowable<Boolean>
/*     */ {
/*     */   final Publisher<? extends T> first;
/*     */   final Publisher<? extends T> second;
/*     */   final BiPredicate<? super T, ? super T> comparer;
/*     */   final int prefetch;
/*     */   
/*     */   public FlowableSequenceEqualSingle(Publisher<? extends T> first, Publisher<? extends T> second, BiPredicate<? super T, ? super T> comparer, int prefetch) {
/*  38 */     this.first = first;
/*  39 */     this.second = second;
/*  40 */     this.comparer = comparer;
/*  41 */     this.prefetch = prefetch;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(SingleObserver<? super Boolean> observer) {
/*  46 */     EqualCoordinator<T> parent = new EqualCoordinator<T>(observer, this.prefetch, this.comparer);
/*  47 */     observer.onSubscribe(parent);
/*  48 */     parent.subscribe(this.first, this.second);
/*     */   }
/*     */ 
/*     */   
/*     */   public Flowable<Boolean> fuseToFlowable() {
/*  53 */     return RxJavaPlugins.onAssembly(new FlowableSequenceEqual<T>(this.first, this.second, this.comparer, this.prefetch));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class EqualCoordinator<T>
/*     */     extends AtomicInteger
/*     */     implements Disposable, FlowableSequenceEqual.EqualCoordinatorHelper
/*     */   {
/*     */     private static final long serialVersionUID = -6178010334400373240L;
/*     */     
/*     */     final SingleObserver<? super Boolean> downstream;
/*     */     
/*     */     final BiPredicate<? super T, ? super T> comparer;
/*     */     
/*     */     final FlowableSequenceEqual.EqualSubscriber<T> first;
/*     */     
/*     */     final FlowableSequenceEqual.EqualSubscriber<T> second;
/*     */     
/*     */     final AtomicThrowable error;
/*     */     
/*     */     T v1;
/*     */     T v2;
/*     */     
/*     */     EqualCoordinator(SingleObserver<? super Boolean> actual, int prefetch, BiPredicate<? super T, ? super T> comparer) {
/*  77 */       this.downstream = actual;
/*  78 */       this.comparer = comparer;
/*  79 */       this.first = new FlowableSequenceEqual.EqualSubscriber<T>(this, prefetch);
/*  80 */       this.second = new FlowableSequenceEqual.EqualSubscriber<T>(this, prefetch);
/*  81 */       this.error = new AtomicThrowable();
/*     */     }
/*     */     
/*     */     void subscribe(Publisher<? extends T> source1, Publisher<? extends T> source2) {
/*  85 */       source1.subscribe((Subscriber)this.first);
/*  86 */       source2.subscribe((Subscriber)this.second);
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  91 */       this.first.cancel();
/*  92 */       this.second.cancel();
/*  93 */       if (getAndIncrement() == 0) {
/*  94 */         this.first.clear();
/*  95 */         this.second.clear();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 101 */       return (this.first.get() == SubscriptionHelper.CANCELLED);
/*     */     }
/*     */     
/*     */     void cancelAndClear() {
/* 105 */       this.first.cancel();
/* 106 */       this.first.clear();
/* 107 */       this.second.cancel();
/* 108 */       this.second.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public void drain() {
/* 113 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 117 */       int missed = 1;
/*     */       
/*     */       do {
/* 120 */         SimpleQueue<T> q1 = this.first.queue;
/* 121 */         SimpleQueue<T> q2 = this.second.queue;
/*     */         
/* 123 */         if (q1 != null && q2 != null) {
/*     */           while (true) {
/* 125 */             boolean c; if (isDisposed()) {
/* 126 */               this.first.clear();
/* 127 */               this.second.clear();
/*     */               
/*     */               return;
/*     */             } 
/* 131 */             Throwable ex = (Throwable)this.error.get();
/* 132 */             if (ex != null) {
/* 133 */               cancelAndClear();
/*     */               
/* 135 */               this.downstream.onError(this.error.terminate());
/*     */               
/*     */               return;
/*     */             } 
/* 139 */             boolean d1 = this.first.done;
/*     */             
/* 141 */             T a = this.v1;
/* 142 */             if (a == null) {
/*     */               try {
/* 144 */                 a = (T)q1.poll();
/* 145 */               } catch (Throwable exc) {
/* 146 */                 Exceptions.throwIfFatal(exc);
/* 147 */                 cancelAndClear();
/* 148 */                 this.error.addThrowable(exc);
/* 149 */                 this.downstream.onError(this.error.terminate());
/*     */                 return;
/*     */               } 
/* 152 */               this.v1 = a;
/*     */             } 
/* 154 */             boolean e1 = (a == null);
/*     */             
/* 156 */             boolean d2 = this.second.done;
/* 157 */             T b = this.v2;
/* 158 */             if (b == null) {
/*     */               try {
/* 160 */                 b = (T)q2.poll();
/* 161 */               } catch (Throwable exc) {
/* 162 */                 Exceptions.throwIfFatal(exc);
/* 163 */                 cancelAndClear();
/* 164 */                 this.error.addThrowable(exc);
/* 165 */                 this.downstream.onError(this.error.terminate());
/*     */                 return;
/*     */               } 
/* 168 */               this.v2 = b;
/*     */             } 
/*     */             
/* 171 */             boolean e2 = (b == null);
/*     */             
/* 173 */             if (d1 && d2 && e1 && e2) {
/* 174 */               this.downstream.onSuccess(Boolean.valueOf(true));
/*     */               return;
/*     */             } 
/* 177 */             if (d1 && d2 && e1 != e2) {
/* 178 */               cancelAndClear();
/* 179 */               this.downstream.onSuccess(Boolean.valueOf(false));
/*     */               
/*     */               return;
/*     */             } 
/* 183 */             if (e1 || e2) {
/*     */               break;
/*     */             }
/*     */ 
/*     */ 
/*     */             
/*     */             try {
/* 190 */               c = this.comparer.test(a, b);
/* 191 */             } catch (Throwable exc) {
/* 192 */               Exceptions.throwIfFatal(exc);
/* 193 */               cancelAndClear();
/* 194 */               this.error.addThrowable(exc);
/* 195 */               this.downstream.onError(this.error.terminate());
/*     */               
/*     */               return;
/*     */             } 
/* 199 */             if (!c) {
/* 200 */               cancelAndClear();
/* 201 */               this.downstream.onSuccess(Boolean.valueOf(false));
/*     */               
/*     */               return;
/*     */             } 
/* 205 */             this.v1 = null;
/* 206 */             this.v2 = null;
/*     */             
/* 208 */             this.first.request();
/* 209 */             this.second.request();
/*     */           } 
/*     */         } else {
/*     */           
/* 213 */           if (isDisposed()) {
/* 214 */             this.first.clear();
/* 215 */             this.second.clear();
/*     */             
/*     */             return;
/*     */           } 
/* 219 */           Throwable ex = (Throwable)this.error.get();
/* 220 */           if (ex != null) {
/* 221 */             cancelAndClear();
/*     */             
/* 223 */             this.downstream.onError(this.error.terminate());
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 228 */         missed = addAndGet(-missed);
/* 229 */       } while (missed != 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void innerError(Throwable t) {
/* 237 */       if (this.error.addThrowable(t)) {
/* 238 */         drain();
/*     */       } else {
/* 240 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableSequenceEqualSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */