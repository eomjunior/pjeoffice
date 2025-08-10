/*     */ package io.reactivex.observers;
/*     */ 
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.annotations.NonNull;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.util.AppendOnlyLinkedArrayList;
/*     */ import io.reactivex.internal.util.NotificationLite;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
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
/*     */ public final class SerializedObserver<T>
/*     */   implements Observer<T>, Disposable
/*     */ {
/*     */   final Observer<? super T> downstream;
/*     */   final boolean delayError;
/*     */   static final int QUEUE_LINK_SIZE = 4;
/*     */   Disposable upstream;
/*     */   boolean emitting;
/*     */   AppendOnlyLinkedArrayList<Object> queue;
/*     */   volatile boolean done;
/*     */   
/*     */   public SerializedObserver(@NonNull Observer<? super T> downstream) {
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
/*     */   public SerializedObserver(@NonNull Observer<? super T> actual, boolean delayError) {
/*  62 */     this.downstream = actual;
/*  63 */     this.delayError = delayError;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSubscribe(@NonNull Disposable d) {
/*  68 */     if (DisposableHelper.validate(this.upstream, d)) {
/*  69 */       this.upstream = d;
/*     */       
/*  71 */       this.downstream.onSubscribe(this);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/*  77 */     this.upstream.dispose();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDisposed() {
/*  82 */     return this.upstream.isDisposed();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNext(@NonNull T t) {
/*  87 */     if (this.done) {
/*     */       return;
/*     */     }
/*  90 */     if (t == null) {
/*  91 */       this.upstream.dispose();
/*  92 */       onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
/*     */       return;
/*     */     } 
/*  95 */     synchronized (this) {
/*  96 */       if (this.done) {
/*     */         return;
/*     */       }
/*  99 */       if (this.emitting) {
/* 100 */         AppendOnlyLinkedArrayList<Object> q = this.queue;
/* 101 */         if (q == null) {
/* 102 */           q = new AppendOnlyLinkedArrayList(4);
/* 103 */           this.queue = q;
/*     */         } 
/* 105 */         q.add(NotificationLite.next(t));
/*     */         return;
/*     */       } 
/* 108 */       this.emitting = true;
/*     */     } 
/*     */     
/* 111 */     this.downstream.onNext(t);
/*     */     
/* 113 */     emitLoop();
/*     */   }
/*     */   
/*     */   public void onError(@NonNull Throwable t) {
/*     */     boolean reportError;
/* 118 */     if (this.done) {
/* 119 */       RxJavaPlugins.onError(t);
/*     */       
/*     */       return;
/*     */     } 
/* 123 */     synchronized (this) {
/* 124 */       if (this.done) {
/* 125 */         reportError = true;
/*     */       } else {
/* 127 */         if (this.emitting) {
/* 128 */           this.done = true;
/* 129 */           AppendOnlyLinkedArrayList<Object> q = this.queue;
/* 130 */           if (q == null) {
/* 131 */             q = new AppendOnlyLinkedArrayList(4);
/* 132 */             this.queue = q;
/*     */           } 
/* 134 */           Object err = NotificationLite.error(t);
/* 135 */           if (this.delayError) {
/* 136 */             q.add(err);
/*     */           } else {
/* 138 */             q.setFirst(err);
/*     */           } 
/*     */           return;
/*     */         } 
/* 142 */         this.done = true;
/* 143 */         this.emitting = true;
/* 144 */         reportError = false;
/*     */       } 
/*     */     } 
/*     */     
/* 148 */     if (reportError) {
/* 149 */       RxJavaPlugins.onError(t);
/*     */       
/*     */       return;
/*     */     } 
/* 153 */     this.downstream.onError(t);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onComplete() {
/* 159 */     if (this.done) {
/*     */       return;
/*     */     }
/* 162 */     synchronized (this) {
/* 163 */       if (this.done) {
/*     */         return;
/*     */       }
/* 166 */       if (this.emitting) {
/* 167 */         AppendOnlyLinkedArrayList<Object> q = this.queue;
/* 168 */         if (q == null) {
/* 169 */           q = new AppendOnlyLinkedArrayList(4);
/* 170 */           this.queue = q;
/*     */         } 
/* 172 */         q.add(NotificationLite.complete());
/*     */         return;
/*     */       } 
/* 175 */       this.done = true;
/* 176 */       this.emitting = true;
/*     */     } 
/*     */     
/* 179 */     this.downstream.onComplete();
/*     */   }
/*     */ 
/*     */   
/*     */   void emitLoop() {
/*     */     AppendOnlyLinkedArrayList<Object> q;
/*     */     do {
/* 186 */       synchronized (this) {
/* 187 */         q = this.queue;
/* 188 */         if (q == null) {
/* 189 */           this.emitting = false;
/*     */           return;
/*     */         } 
/* 192 */         this.queue = null;
/*     */       }
/*     */     
/* 195 */     } while (!q.accept(this.downstream));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/observers/SerializedObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */