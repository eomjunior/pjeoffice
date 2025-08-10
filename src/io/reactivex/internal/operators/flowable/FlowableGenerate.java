/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Emitter;
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.BiFunction;
/*     */ import io.reactivex.functions.Consumer;
/*     */ import io.reactivex.internal.subscriptions.EmptySubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.Callable;
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
/*     */ public final class FlowableGenerate<T, S>
/*     */   extends Flowable<T>
/*     */ {
/*     */   final Callable<S> stateSupplier;
/*     */   final BiFunction<S, Emitter<T>, S> generator;
/*     */   final Consumer<? super S> disposeState;
/*     */   
/*     */   public FlowableGenerate(Callable<S> stateSupplier, BiFunction<S, Emitter<T>, S> generator, Consumer<? super S> disposeState) {
/*  35 */     this.stateSupplier = stateSupplier;
/*  36 */     this.generator = generator;
/*  37 */     this.disposeState = disposeState;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void subscribeActual(Subscriber<? super T> s) {
/*     */     S state;
/*     */     try {
/*  45 */       state = this.stateSupplier.call();
/*  46 */     } catch (Throwable e) {
/*  47 */       Exceptions.throwIfFatal(e);
/*  48 */       EmptySubscription.error(e, s);
/*     */       
/*     */       return;
/*     */     } 
/*  52 */     s.onSubscribe(new GeneratorSubscription<T, S>(s, this.generator, this.disposeState, state));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class GeneratorSubscription<T, S>
/*     */     extends AtomicLong
/*     */     implements Emitter<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = 7565982551505011832L;
/*     */     
/*     */     final Subscriber<? super T> downstream;
/*     */     
/*     */     final BiFunction<S, ? super Emitter<T>, S> generator;
/*     */     
/*     */     final Consumer<? super S> disposeState;
/*     */     
/*     */     S state;
/*     */     
/*     */     volatile boolean cancelled;
/*     */     
/*     */     boolean terminate;
/*     */     boolean hasNext;
/*     */     
/*     */     GeneratorSubscription(Subscriber<? super T> actual, BiFunction<S, ? super Emitter<T>, S> generator, Consumer<? super S> disposeState, S initialState) {
/*  76 */       this.downstream = actual;
/*  77 */       this.generator = generator;
/*  78 */       this.disposeState = disposeState;
/*  79 */       this.state = initialState;
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/*  84 */       if (!SubscriptionHelper.validate(n)) {
/*     */         return;
/*     */       }
/*  87 */       if (BackpressureHelper.add(this, n) != 0L) {
/*     */         return;
/*     */       }
/*     */       
/*  91 */       long e = 0L;
/*     */       
/*  93 */       S s = this.state;
/*     */       
/*  95 */       BiFunction<S, ? super Emitter<T>, S> f = this.generator;
/*     */       
/*     */       while (true) {
/*  98 */         while (e != n) {
/*     */           
/* 100 */           if (this.cancelled) {
/* 101 */             this.state = null;
/* 102 */             dispose(s);
/*     */             
/*     */             return;
/*     */           } 
/* 106 */           this.hasNext = false;
/*     */           
/*     */           try {
/* 109 */             s = (S)f.apply(s, this);
/* 110 */           } catch (Throwable ex) {
/* 111 */             Exceptions.throwIfFatal(ex);
/* 112 */             this.cancelled = true;
/* 113 */             this.state = null;
/* 114 */             onError(ex);
/* 115 */             dispose(s);
/*     */             
/*     */             return;
/*     */           } 
/* 119 */           if (this.terminate) {
/* 120 */             this.cancelled = true;
/* 121 */             this.state = null;
/* 122 */             dispose(s);
/*     */             
/*     */             return;
/*     */           } 
/* 126 */           e++;
/*     */         } 
/*     */         
/* 129 */         n = get();
/* 130 */         if (e == n) {
/* 131 */           this.state = s;
/* 132 */           n = addAndGet(-e);
/* 133 */           if (n == 0L) {
/*     */             break;
/*     */           }
/* 136 */           e = 0L;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private void dispose(S s) {
/*     */       try {
/* 143 */         this.disposeState.accept(s);
/* 144 */       } catch (Throwable ex) {
/* 145 */         Exceptions.throwIfFatal(ex);
/* 146 */         RxJavaPlugins.onError(ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 152 */       if (!this.cancelled) {
/* 153 */         this.cancelled = true;
/*     */ 
/*     */         
/* 156 */         if (BackpressureHelper.add(this, 1L) == 0L) {
/* 157 */           S s = this.state;
/* 158 */           this.state = null;
/* 159 */           dispose(s);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 166 */       if (!this.terminate) {
/* 167 */         if (this.hasNext) {
/* 168 */           onError(new IllegalStateException("onNext already called in this generate turn"));
/*     */         }
/* 170 */         else if (t == null) {
/* 171 */           onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
/*     */         } else {
/* 173 */           this.hasNext = true;
/* 174 */           this.downstream.onNext(t);
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 182 */       if (this.terminate) {
/* 183 */         RxJavaPlugins.onError(t);
/*     */       } else {
/* 185 */         if (t == null) {
/* 186 */           t = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
/*     */         }
/* 188 */         this.terminate = true;
/* 189 */         this.downstream.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 195 */       if (!this.terminate) {
/* 196 */         this.terminate = true;
/* 197 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableGenerate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */