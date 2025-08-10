/*     */ package io.reactivex.internal.operators.parallel;
/*     */ 
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.CompositeException;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Action;
/*     */ import io.reactivex.functions.Consumer;
/*     */ import io.reactivex.functions.LongConsumer;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.subscriptions.EmptySubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.parallel.ParallelFlowable;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ParallelPeek<T>
/*     */   extends ParallelFlowable<T>
/*     */ {
/*     */   final ParallelFlowable<T> source;
/*     */   final Consumer<? super T> onNext;
/*     */   final Consumer<? super T> onAfterNext;
/*     */   final Consumer<? super Throwable> onError;
/*     */   final Action onComplete;
/*     */   final Action onAfterTerminated;
/*     */   final Consumer<? super Subscription> onSubscribe;
/*     */   final LongConsumer onRequest;
/*     */   final Action onCancel;
/*     */   
/*     */   public ParallelPeek(ParallelFlowable<T> source, Consumer<? super T> onNext, Consumer<? super T> onAfterNext, Consumer<? super Throwable> onError, Action onComplete, Action onAfterTerminated, Consumer<? super Subscription> onSubscribe, LongConsumer onRequest, Action onCancel) {
/*  54 */     this.source = source;
/*     */     
/*  56 */     this.onNext = (Consumer<? super T>)ObjectHelper.requireNonNull(onNext, "onNext is null");
/*  57 */     this.onAfterNext = (Consumer<? super T>)ObjectHelper.requireNonNull(onAfterNext, "onAfterNext is null");
/*  58 */     this.onError = (Consumer<? super Throwable>)ObjectHelper.requireNonNull(onError, "onError is null");
/*  59 */     this.onComplete = (Action)ObjectHelper.requireNonNull(onComplete, "onComplete is null");
/*  60 */     this.onAfterTerminated = (Action)ObjectHelper.requireNonNull(onAfterTerminated, "onAfterTerminated is null");
/*  61 */     this.onSubscribe = (Consumer<? super Subscription>)ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");
/*  62 */     this.onRequest = (LongConsumer)ObjectHelper.requireNonNull(onRequest, "onRequest is null");
/*  63 */     this.onCancel = (Action)ObjectHelper.requireNonNull(onCancel, "onCancel is null");
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribe(Subscriber<? super T>[] subscribers) {
/*  68 */     if (!validate((Subscriber[])subscribers)) {
/*     */       return;
/*     */     }
/*     */     
/*  72 */     int n = subscribers.length;
/*     */     
/*  74 */     Subscriber[] arrayOfSubscriber = new Subscriber[n];
/*     */     
/*  76 */     for (int i = 0; i < n; i++) {
/*  77 */       arrayOfSubscriber[i] = (Subscriber)new ParallelPeekSubscriber<T>(subscribers[i], this);
/*     */     }
/*     */     
/*  80 */     this.source.subscribe(arrayOfSubscriber);
/*     */   }
/*     */ 
/*     */   
/*     */   public int parallelism() {
/*  85 */     return this.source.parallelism();
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ParallelPeekSubscriber<T>
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     final Subscriber<? super T> downstream;
/*     */     
/*     */     final ParallelPeek<T> parent;
/*     */     Subscription upstream;
/*     */     boolean done;
/*     */     
/*     */     ParallelPeekSubscriber(Subscriber<? super T> actual, ParallelPeek<T> parent) {
/*  99 */       this.downstream = actual;
/* 100 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/*     */       try {
/* 106 */         this.parent.onRequest.accept(n);
/* 107 */       } catch (Throwable ex) {
/* 108 */         Exceptions.throwIfFatal(ex);
/* 109 */         RxJavaPlugins.onError(ex);
/*     */       } 
/* 111 */       this.upstream.request(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/*     */       try {
/* 117 */         this.parent.onCancel.run();
/* 118 */       } catch (Throwable ex) {
/* 119 */         Exceptions.throwIfFatal(ex);
/* 120 */         RxJavaPlugins.onError(ex);
/*     */       } 
/* 122 */       this.upstream.cancel();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 127 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 128 */         this.upstream = s;
/*     */         
/*     */         try {
/* 131 */           this.parent.onSubscribe.accept(s);
/* 132 */         } catch (Throwable ex) {
/* 133 */           Exceptions.throwIfFatal(ex);
/* 134 */           s.cancel();
/* 135 */           this.downstream.onSubscribe((Subscription)EmptySubscription.INSTANCE);
/* 136 */           onError(ex);
/*     */           
/*     */           return;
/*     */         } 
/* 140 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 146 */       if (!this.done) {
/*     */         try {
/* 148 */           this.parent.onNext.accept(t);
/* 149 */         } catch (Throwable ex) {
/* 150 */           Exceptions.throwIfFatal(ex);
/* 151 */           onError(ex);
/*     */           
/*     */           return;
/*     */         } 
/* 155 */         this.downstream.onNext(t);
/*     */         
/*     */         try {
/* 158 */           this.parent.onAfterNext.accept(t);
/* 159 */         } catch (Throwable ex) {
/* 160 */           Exceptions.throwIfFatal(ex);
/* 161 */           onError(ex);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public void onError(Throwable t) {
/*     */       CompositeException compositeException;
/* 168 */       if (this.done) {
/* 169 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 172 */       this.done = true;
/*     */       
/*     */       try {
/* 175 */         this.parent.onError.accept(t);
/* 176 */       } catch (Throwable ex) {
/* 177 */         Exceptions.throwIfFatal(ex);
/* 178 */         compositeException = new CompositeException(new Throwable[] { t, ex });
/*     */       } 
/* 180 */       this.downstream.onError((Throwable)compositeException);
/*     */       
/*     */       try {
/* 183 */         this.parent.onAfterTerminated.run();
/* 184 */       } catch (Throwable ex) {
/* 185 */         Exceptions.throwIfFatal(ex);
/* 186 */         RxJavaPlugins.onError(ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 192 */       if (!this.done) {
/* 193 */         this.done = true;
/*     */         try {
/* 195 */           this.parent.onComplete.run();
/* 196 */         } catch (Throwable ex) {
/* 197 */           Exceptions.throwIfFatal(ex);
/* 198 */           this.downstream.onError(ex);
/*     */           return;
/*     */         } 
/* 201 */         this.downstream.onComplete();
/*     */         
/*     */         try {
/* 204 */           this.parent.onAfterTerminated.run();
/* 205 */         } catch (Throwable ex) {
/* 206 */           Exceptions.throwIfFatal(ex);
/* 207 */           RxJavaPlugins.onError(ex);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/parallel/ParallelPeek.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */