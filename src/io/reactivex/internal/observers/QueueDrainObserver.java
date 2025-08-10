/*     */ package io.reactivex.internal.observers;
/*     */ 
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.fuseable.SimplePlainQueue;
/*     */ import io.reactivex.internal.util.ObservableQueueDrain;
/*     */ import io.reactivex.internal.util.QueueDrainHelper;
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
/*     */ public abstract class QueueDrainObserver<T, U, V>
/*     */   extends QueueDrainSubscriberPad2
/*     */   implements Observer<T>, ObservableQueueDrain<U, V>
/*     */ {
/*     */   protected final Observer<? super V> downstream;
/*     */   protected final SimplePlainQueue<U> queue;
/*     */   protected volatile boolean cancelled;
/*     */   protected volatile boolean done;
/*     */   protected Throwable error;
/*     */   
/*     */   public QueueDrainObserver(Observer<? super V> actual, SimplePlainQueue<U> queue) {
/*  41 */     this.downstream = actual;
/*  42 */     this.queue = queue;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean cancelled() {
/*  47 */     return this.cancelled;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean done() {
/*  52 */     return this.done;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean enter() {
/*  57 */     return (this.wip.getAndIncrement() == 0);
/*     */   }
/*     */   
/*     */   public final boolean fastEnter() {
/*  61 */     return (this.wip.get() == 0 && this.wip.compareAndSet(0, 1));
/*     */   }
/*     */   
/*     */   protected final void fastPathEmit(U value, boolean delayError, Disposable dispose) {
/*  65 */     Observer<? super V> observer = this.downstream;
/*  66 */     SimplePlainQueue<U> q = this.queue;
/*     */     
/*  68 */     if (this.wip.get() == 0 && this.wip.compareAndSet(0, 1)) {
/*  69 */       accept(observer, value);
/*  70 */       if (leave(-1) == 0) {
/*     */         return;
/*     */       }
/*     */     } else {
/*  74 */       q.offer(value);
/*  75 */       if (!enter()) {
/*     */         return;
/*     */       }
/*     */     } 
/*  79 */     QueueDrainHelper.drainLoop(q, observer, delayError, dispose, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void fastPathOrderedEmit(U value, boolean delayError, Disposable disposable) {
/*  89 */     Observer<? super V> observer = this.downstream;
/*  90 */     SimplePlainQueue<U> q = this.queue;
/*     */     
/*  92 */     if (this.wip.get() == 0 && this.wip.compareAndSet(0, 1)) {
/*  93 */       if (q.isEmpty()) {
/*  94 */         accept(observer, value);
/*  95 */         if (leave(-1) == 0) {
/*     */           return;
/*     */         }
/*     */       } else {
/*  99 */         q.offer(value);
/*     */       } 
/*     */     } else {
/* 102 */       q.offer(value);
/* 103 */       if (!enter()) {
/*     */         return;
/*     */       }
/*     */     } 
/* 107 */     QueueDrainHelper.drainLoop(q, observer, delayError, disposable, this);
/*     */   }
/*     */ 
/*     */   
/*     */   public final Throwable error() {
/* 112 */     return this.error;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int leave(int m) {
/* 117 */     return this.wip.addAndGet(m);
/*     */   }
/*     */   
/*     */   public void accept(Observer<? super V> a, U v) {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/observers/QueueDrainObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */