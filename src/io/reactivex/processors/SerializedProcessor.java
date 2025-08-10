/*     */ package io.reactivex.processors;
/*     */ 
/*     */ import io.reactivex.annotations.Nullable;
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
/*     */ final class SerializedProcessor<T>
/*     */   extends FlowableProcessor<T>
/*     */ {
/*     */   final FlowableProcessor<T> actual;
/*     */   boolean emitting;
/*     */   AppendOnlyLinkedArrayList<Object> queue;
/*     */   volatile boolean done;
/*     */   
/*     */   SerializedProcessor(FlowableProcessor<T> actual) {
/*  43 */     this.actual = actual;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  48 */     this.actual.subscribe(s);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSubscribe(Subscription s) {
/*     */     boolean cancel;
/*  54 */     if (!this.done) {
/*  55 */       synchronized (this) {
/*  56 */         if (this.done) {
/*  57 */           cancel = true;
/*     */         } else {
/*  59 */           if (this.emitting) {
/*  60 */             AppendOnlyLinkedArrayList<Object> q = this.queue;
/*  61 */             if (q == null) {
/*  62 */               q = new AppendOnlyLinkedArrayList(4);
/*  63 */               this.queue = q;
/*     */             } 
/*  65 */             q.add(NotificationLite.subscription(s));
/*     */             return;
/*     */           } 
/*  68 */           this.emitting = true;
/*  69 */           cancel = false;
/*     */         } 
/*     */       } 
/*     */     } else {
/*  73 */       cancel = true;
/*     */     } 
/*  75 */     if (cancel) {
/*  76 */       s.cancel();
/*     */     } else {
/*  78 */       this.actual.onSubscribe(s);
/*  79 */       emitLoop();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNext(T t) {
/*  85 */     if (this.done) {
/*     */       return;
/*     */     }
/*  88 */     synchronized (this) {
/*  89 */       if (this.done) {
/*     */         return;
/*     */       }
/*  92 */       if (this.emitting) {
/*  93 */         AppendOnlyLinkedArrayList<Object> q = this.queue;
/*  94 */         if (q == null) {
/*  95 */           q = new AppendOnlyLinkedArrayList(4);
/*  96 */           this.queue = q;
/*     */         } 
/*  98 */         q.add(NotificationLite.next(t));
/*     */         return;
/*     */       } 
/* 101 */       this.emitting = true;
/*     */     } 
/* 103 */     this.actual.onNext(t);
/* 104 */     emitLoop();
/*     */   }
/*     */   
/*     */   public void onError(Throwable t) {
/*     */     boolean reportError;
/* 109 */     if (this.done) {
/* 110 */       RxJavaPlugins.onError(t);
/*     */       
/*     */       return;
/*     */     } 
/* 114 */     synchronized (this) {
/* 115 */       if (this.done) {
/* 116 */         reportError = true;
/*     */       } else {
/* 118 */         this.done = true;
/* 119 */         if (this.emitting) {
/* 120 */           AppendOnlyLinkedArrayList<Object> q = this.queue;
/* 121 */           if (q == null) {
/* 122 */             q = new AppendOnlyLinkedArrayList(4);
/* 123 */             this.queue = q;
/*     */           } 
/* 125 */           q.setFirst(NotificationLite.error(t));
/*     */           return;
/*     */         } 
/* 128 */         reportError = false;
/* 129 */         this.emitting = true;
/*     */       } 
/*     */     } 
/* 132 */     if (reportError) {
/* 133 */       RxJavaPlugins.onError(t);
/*     */       return;
/*     */     } 
/* 136 */     this.actual.onError(t);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onComplete() {
/* 141 */     if (this.done) {
/*     */       return;
/*     */     }
/* 144 */     synchronized (this) {
/* 145 */       if (this.done) {
/*     */         return;
/*     */       }
/* 148 */       this.done = true;
/* 149 */       if (this.emitting) {
/* 150 */         AppendOnlyLinkedArrayList<Object> q = this.queue;
/* 151 */         if (q == null) {
/* 152 */           q = new AppendOnlyLinkedArrayList(4);
/* 153 */           this.queue = q;
/*     */         } 
/* 155 */         q.add(NotificationLite.complete());
/*     */         return;
/*     */       } 
/* 158 */       this.emitting = true;
/*     */     } 
/* 160 */     this.actual.onComplete();
/*     */   }
/*     */ 
/*     */   
/*     */   void emitLoop() {
/*     */     while (true) {
/*     */       AppendOnlyLinkedArrayList<Object> q;
/* 167 */       synchronized (this) {
/* 168 */         q = this.queue;
/* 169 */         if (q == null) {
/* 170 */           this.emitting = false;
/*     */           return;
/*     */         } 
/* 173 */         this.queue = null;
/*     */       } 
/*     */       
/* 176 */       q.accept((Subscriber)this.actual);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasSubscribers() {
/* 182 */     return this.actual.hasSubscribers();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasThrowable() {
/* 187 */     return this.actual.hasThrowable();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Throwable getThrowable() {
/* 193 */     return this.actual.getThrowable();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasComplete() {
/* 198 */     return this.actual.hasComplete();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/processors/SerializedProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */