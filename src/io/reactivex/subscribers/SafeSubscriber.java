/*     */ package io.reactivex.subscribers;
/*     */ 
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.CompositeException;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.internal.subscriptions.EmptySubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
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
/*     */ public final class SafeSubscriber<T>
/*     */   implements FlowableSubscriber<T>, Subscription
/*     */ {
/*     */   final Subscriber<? super T> downstream;
/*     */   Subscription upstream;
/*     */   boolean done;
/*     */   
/*     */   public SafeSubscriber(Subscriber<? super T> downstream) {
/*  41 */     this.downstream = downstream;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSubscribe(Subscription s) {
/*  46 */     if (SubscriptionHelper.validate(this.upstream, s)) {
/*  47 */       this.upstream = s;
/*     */       try {
/*  49 */         this.downstream.onSubscribe(this);
/*  50 */       } catch (Throwable e) {
/*  51 */         Exceptions.throwIfFatal(e);
/*  52 */         this.done = true;
/*     */         
/*     */         try {
/*  55 */           s.cancel();
/*  56 */         } catch (Throwable e1) {
/*  57 */           Exceptions.throwIfFatal(e1);
/*  58 */           RxJavaPlugins.onError((Throwable)new CompositeException(new Throwable[] { e, e1 }));
/*     */           return;
/*     */         } 
/*  61 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNext(T t) {
/*  68 */     if (this.done) {
/*     */       return;
/*     */     }
/*  71 */     if (this.upstream == null) {
/*  72 */       onNextNoSubscription();
/*     */       
/*     */       return;
/*     */     } 
/*  76 */     if (t == null) {
/*  77 */       Throwable ex = new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
/*     */       try {
/*  79 */         this.upstream.cancel();
/*  80 */       } catch (Throwable e1) {
/*  81 */         Exceptions.throwIfFatal(e1);
/*  82 */         onError((Throwable)new CompositeException(new Throwable[] { ex, e1 }));
/*     */         return;
/*     */       } 
/*  85 */       onError(ex);
/*     */       
/*     */       return;
/*     */     } 
/*     */     try {
/*  90 */       this.downstream.onNext(t);
/*  91 */     } catch (Throwable e) {
/*  92 */       Exceptions.throwIfFatal(e);
/*     */       try {
/*  94 */         this.upstream.cancel();
/*  95 */       } catch (Throwable e1) {
/*  96 */         Exceptions.throwIfFatal(e1);
/*  97 */         onError((Throwable)new CompositeException(new Throwable[] { e, e1 }));
/*     */         return;
/*     */       } 
/* 100 */       onError(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   void onNextNoSubscription() {
/* 105 */     this.done = true;
/* 106 */     Throwable ex = new NullPointerException("Subscription not set!");
/*     */     
/*     */     try {
/* 109 */       this.downstream.onSubscribe((Subscription)EmptySubscription.INSTANCE);
/* 110 */     } catch (Throwable e) {
/* 111 */       Exceptions.throwIfFatal(e);
/*     */       
/* 113 */       RxJavaPlugins.onError((Throwable)new CompositeException(new Throwable[] { ex, e }));
/*     */       return;
/*     */     } 
/*     */     try {
/* 117 */       this.downstream.onError(ex);
/* 118 */     } catch (Throwable e) {
/* 119 */       Exceptions.throwIfFatal(e);
/*     */       
/* 121 */       RxJavaPlugins.onError((Throwable)new CompositeException(new Throwable[] { ex, e }));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onError(Throwable t) {
/* 127 */     if (this.done) {
/* 128 */       RxJavaPlugins.onError(t);
/*     */       return;
/*     */     } 
/* 131 */     this.done = true;
/*     */     
/* 133 */     if (this.upstream == null) {
/* 134 */       Throwable npe = new NullPointerException("Subscription not set!");
/*     */       
/*     */       try {
/* 137 */         this.downstream.onSubscribe((Subscription)EmptySubscription.INSTANCE);
/* 138 */       } catch (Throwable e) {
/* 139 */         Exceptions.throwIfFatal(e);
/*     */         
/* 141 */         RxJavaPlugins.onError((Throwable)new CompositeException(new Throwable[] { t, npe, e }));
/*     */         return;
/*     */       } 
/*     */       try {
/* 145 */         this.downstream.onError((Throwable)new CompositeException(new Throwable[] { t, npe }));
/* 146 */       } catch (Throwable e) {
/* 147 */         Exceptions.throwIfFatal(e);
/*     */         
/* 149 */         RxJavaPlugins.onError((Throwable)new CompositeException(new Throwable[] { t, npe, e }));
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 154 */     if (t == null) {
/* 155 */       t = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
/*     */     }
/*     */     
/*     */     try {
/* 159 */       this.downstream.onError(t);
/* 160 */     } catch (Throwable ex) {
/* 161 */       Exceptions.throwIfFatal(ex);
/*     */       
/* 163 */       RxJavaPlugins.onError((Throwable)new CompositeException(new Throwable[] { t, ex }));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onComplete() {
/* 169 */     if (this.done) {
/*     */       return;
/*     */     }
/* 172 */     this.done = true;
/*     */     
/* 174 */     if (this.upstream == null) {
/* 175 */       onCompleteNoSubscription();
/*     */       
/*     */       return;
/*     */     } 
/*     */     try {
/* 180 */       this.downstream.onComplete();
/* 181 */     } catch (Throwable e) {
/* 182 */       Exceptions.throwIfFatal(e);
/* 183 */       RxJavaPlugins.onError(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void onCompleteNoSubscription() {
/* 189 */     Throwable ex = new NullPointerException("Subscription not set!");
/*     */     
/*     */     try {
/* 192 */       this.downstream.onSubscribe((Subscription)EmptySubscription.INSTANCE);
/* 193 */     } catch (Throwable e) {
/* 194 */       Exceptions.throwIfFatal(e);
/*     */       
/* 196 */       RxJavaPlugins.onError((Throwable)new CompositeException(new Throwable[] { ex, e }));
/*     */       return;
/*     */     } 
/*     */     try {
/* 200 */       this.downstream.onError(ex);
/* 201 */     } catch (Throwable e) {
/* 202 */       Exceptions.throwIfFatal(e);
/*     */       
/* 204 */       RxJavaPlugins.onError((Throwable)new CompositeException(new Throwable[] { ex, e }));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void request(long n) {
/*     */     try {
/* 211 */       this.upstream.request(n);
/* 212 */     } catch (Throwable e) {
/* 213 */       Exceptions.throwIfFatal(e);
/*     */       try {
/* 215 */         this.upstream.cancel();
/* 216 */       } catch (Throwable e1) {
/* 217 */         Exceptions.throwIfFatal(e1);
/* 218 */         RxJavaPlugins.onError((Throwable)new CompositeException(new Throwable[] { e, e1 }));
/*     */         return;
/*     */       } 
/* 221 */       RxJavaPlugins.onError(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void cancel() {
/*     */     try {
/* 228 */       this.upstream.cancel();
/* 229 */     } catch (Throwable e1) {
/* 230 */       Exceptions.throwIfFatal(e1);
/* 231 */       RxJavaPlugins.onError(e1);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/subscribers/SafeSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */