/*     */ package io.reactivex.subjects;
/*     */ 
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.disposables.Disposable;
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
/*     */ final class SerializedSubject<T>
/*     */   extends Subject<T>
/*     */   implements AppendOnlyLinkedArrayList.NonThrowingPredicate<Object>
/*     */ {
/*     */   final Subject<T> actual;
/*     */   boolean emitting;
/*     */   AppendOnlyLinkedArrayList<Object> queue;
/*     */   volatile boolean done;
/*     */   
/*     */   SerializedSubject(Subject<T> actual) {
/*  44 */     this.actual = actual;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super T> observer) {
/*  49 */     this.actual.subscribe(observer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSubscribe(Disposable d) {
/*     */     boolean cancel;
/*  55 */     if (!this.done) {
/*  56 */       synchronized (this) {
/*  57 */         if (this.done) {
/*  58 */           cancel = true;
/*     */         } else {
/*  60 */           if (this.emitting) {
/*  61 */             AppendOnlyLinkedArrayList<Object> q = this.queue;
/*  62 */             if (q == null) {
/*  63 */               q = new AppendOnlyLinkedArrayList(4);
/*  64 */               this.queue = q;
/*     */             } 
/*  66 */             q.add(NotificationLite.disposable(d));
/*     */             return;
/*     */           } 
/*  69 */           this.emitting = true;
/*  70 */           cancel = false;
/*     */         } 
/*     */       } 
/*     */     } else {
/*  74 */       cancel = true;
/*     */     } 
/*  76 */     if (cancel) {
/*  77 */       d.dispose();
/*     */     } else {
/*  79 */       this.actual.onSubscribe(d);
/*  80 */       emitLoop();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNext(T t) {
/*  86 */     if (this.done) {
/*     */       return;
/*     */     }
/*  89 */     synchronized (this) {
/*  90 */       if (this.done) {
/*     */         return;
/*     */       }
/*  93 */       if (this.emitting) {
/*  94 */         AppendOnlyLinkedArrayList<Object> q = this.queue;
/*  95 */         if (q == null) {
/*  96 */           q = new AppendOnlyLinkedArrayList(4);
/*  97 */           this.queue = q;
/*     */         } 
/*  99 */         q.add(NotificationLite.next(t));
/*     */         return;
/*     */       } 
/* 102 */       this.emitting = true;
/*     */     } 
/* 104 */     this.actual.onNext(t);
/* 105 */     emitLoop();
/*     */   }
/*     */   
/*     */   public void onError(Throwable t) {
/*     */     boolean reportError;
/* 110 */     if (this.done) {
/* 111 */       RxJavaPlugins.onError(t);
/*     */       
/*     */       return;
/*     */     } 
/* 115 */     synchronized (this) {
/* 116 */       if (this.done) {
/* 117 */         reportError = true;
/*     */       } else {
/* 119 */         this.done = true;
/* 120 */         if (this.emitting) {
/* 121 */           AppendOnlyLinkedArrayList<Object> q = this.queue;
/* 122 */           if (q == null) {
/* 123 */             q = new AppendOnlyLinkedArrayList(4);
/* 124 */             this.queue = q;
/*     */           } 
/* 126 */           q.setFirst(NotificationLite.error(t));
/*     */           return;
/*     */         } 
/* 129 */         reportError = false;
/* 130 */         this.emitting = true;
/*     */       } 
/*     */     } 
/* 133 */     if (reportError) {
/* 134 */       RxJavaPlugins.onError(t);
/*     */       return;
/*     */     } 
/* 137 */     this.actual.onError(t);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onComplete() {
/* 142 */     if (this.done) {
/*     */       return;
/*     */     }
/* 145 */     synchronized (this) {
/* 146 */       if (this.done) {
/*     */         return;
/*     */       }
/* 149 */       this.done = true;
/* 150 */       if (this.emitting) {
/* 151 */         AppendOnlyLinkedArrayList<Object> q = this.queue;
/* 152 */         if (q == null) {
/* 153 */           q = new AppendOnlyLinkedArrayList(4);
/* 154 */           this.queue = q;
/*     */         } 
/* 156 */         q.add(NotificationLite.complete());
/*     */         return;
/*     */       } 
/* 159 */       this.emitting = true;
/*     */     } 
/* 161 */     this.actual.onComplete();
/*     */   }
/*     */ 
/*     */   
/*     */   void emitLoop() {
/*     */     while (true) {
/*     */       AppendOnlyLinkedArrayList<Object> q;
/* 168 */       synchronized (this) {
/* 169 */         q = this.queue;
/* 170 */         if (q == null) {
/* 171 */           this.emitting = false;
/*     */           return;
/*     */         } 
/* 174 */         this.queue = null;
/*     */       } 
/* 176 */       q.forEachWhile(this);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean test(Object o) {
/* 182 */     return NotificationLite.acceptFull(o, this.actual);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasObservers() {
/* 187 */     return this.actual.hasObservers();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasThrowable() {
/* 192 */     return this.actual.hasThrowable();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Throwable getThrowable() {
/* 198 */     return this.actual.getThrowable();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasComplete() {
/* 203 */     return this.actual.hasComplete();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/subjects/SerializedSubject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */