/*     */ package io.reactivex.subscribers;
/*     */ 
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.AppendOnlyLinkedArrayList;
/*     */ import io.reactivex.internal.util.NotificationLite;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SerializedSubscriber<T>
/*     */   implements FlowableSubscriber<T>, Subscription
/*     */ {
/*     */   final Subscriber<? super T> downstream;
/*     */   final boolean delayError;
/*     */   static final int QUEUE_LINK_SIZE = 4;
/*     */   Subscription upstream;
/*     */   boolean emitting;
/*     */   AppendOnlyLinkedArrayList<Object> queue;
/*     */   volatile boolean done;
/*     */   
/*     */   public SerializedSubscriber(Subscriber<? super T> downstream) {
/*  51 */     this(downstream, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SerializedSubscriber(Subscriber<? super T> actual, boolean delayError) {
/*  62 */     this.downstream = actual;
/*  63 */     this.delayError = delayError;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSubscribe(Subscription s) {
/*  68 */     if (SubscriptionHelper.validate(this.upstream, s)) {
/*  69 */       this.upstream = s;
/*  70 */       this.downstream.onSubscribe(this);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNext(T t) {
/*  76 */     if (this.done) {
/*     */       return;
/*     */     }
/*  79 */     if (t == null) {
/*  80 */       this.upstream.cancel();
/*  81 */       onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
/*     */       return;
/*     */     } 
/*  84 */     synchronized (this) {
/*  85 */       if (this.done) {
/*     */         return;
/*     */       }
/*  88 */       if (this.emitting) {
/*  89 */         AppendOnlyLinkedArrayList<Object> q = this.queue;
/*  90 */         if (q == null) {
/*  91 */           q = new AppendOnlyLinkedArrayList(4);
/*  92 */           this.queue = q;
/*     */         } 
/*  94 */         q.add(NotificationLite.next(t));
/*     */         return;
/*     */       } 
/*  97 */       this.emitting = true;
/*     */     } 
/*     */     
/* 100 */     this.downstream.onNext(t);
/*     */     
/* 102 */     emitLoop();
/*     */   }
/*     */   
/*     */   public void onError(Throwable t) {
/*     */     boolean reportError;
/* 107 */     if (this.done) {
/* 108 */       RxJavaPlugins.onError(t);
/*     */       
/*     */       return;
/*     */     } 
/* 112 */     synchronized (this) {
/* 113 */       if (this.done) {
/* 114 */         reportError = true;
/*     */       } else {
/* 116 */         if (this.emitting) {
/* 117 */           this.done = true;
/* 118 */           AppendOnlyLinkedArrayList<Object> q = this.queue;
/* 119 */           if (q == null) {
/* 120 */             q = new AppendOnlyLinkedArrayList(4);
/* 121 */             this.queue = q;
/*     */           } 
/* 123 */           Object err = NotificationLite.error(t);
/* 124 */           if (this.delayError) {
/* 125 */             q.add(err);
/*     */           } else {
/* 127 */             q.setFirst(err);
/*     */           } 
/*     */           return;
/*     */         } 
/* 131 */         this.done = true;
/* 132 */         this.emitting = true;
/* 133 */         reportError = false;
/*     */       } 
/*     */     } 
/*     */     
/* 137 */     if (reportError) {
/* 138 */       RxJavaPlugins.onError(t);
/*     */       
/*     */       return;
/*     */     } 
/* 142 */     this.downstream.onError(t);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onComplete() {
/* 148 */     if (this.done) {
/*     */       return;
/*     */     }
/* 151 */     synchronized (this) {
/* 152 */       if (this.done) {
/*     */         return;
/*     */       }
/* 155 */       if (this.emitting) {
/* 156 */         AppendOnlyLinkedArrayList<Object> q = this.queue;
/* 157 */         if (q == null) {
/* 158 */           q = new AppendOnlyLinkedArrayList(4);
/* 159 */           this.queue = q;
/*     */         } 
/* 161 */         q.add(NotificationLite.complete());
/*     */         return;
/*     */       } 
/* 164 */       this.done = true;
/* 165 */       this.emitting = true;
/*     */     } 
/*     */     
/* 168 */     this.downstream.onComplete();
/*     */   }
/*     */ 
/*     */   
/*     */   void emitLoop() {
/*     */     AppendOnlyLinkedArrayList<Object> q;
/*     */     do {
/* 175 */       synchronized (this) {
/* 176 */         q = this.queue;
/* 177 */         if (q == null) {
/* 178 */           this.emitting = false;
/*     */           return;
/*     */         } 
/* 181 */         this.queue = null;
/*     */       }
/*     */     
/* 184 */     } while (!q.accept(this.downstream));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void request(long n) {
/* 192 */     this.upstream.request(n);
/*     */   }
/*     */ 
/*     */   
/*     */   public void cancel() {
/* 197 */     this.upstream.cancel();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/subscribers/SerializedSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */