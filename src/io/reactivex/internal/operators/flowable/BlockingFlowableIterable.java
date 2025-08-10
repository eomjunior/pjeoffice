/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.internal.queue.SpscArrayQueue;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BlockingHelper;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.concurrent.locks.Condition;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
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
/*     */ public final class BlockingFlowableIterable<T>
/*     */   implements Iterable<T>
/*     */ {
/*     */   final Flowable<T> source;
/*     */   final int bufferSize;
/*     */   
/*     */   public BlockingFlowableIterable(Flowable<T> source, int bufferSize) {
/*  35 */     this.source = source;
/*  36 */     this.bufferSize = bufferSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<T> iterator() {
/*  41 */     BlockingFlowableIterator<T> it = new BlockingFlowableIterator<T>(this.bufferSize);
/*  42 */     this.source.subscribe(it);
/*  43 */     return it;
/*     */   }
/*     */ 
/*     */   
/*     */   static final class BlockingFlowableIterator<T>
/*     */     extends AtomicReference<Subscription>
/*     */     implements FlowableSubscriber<T>, Iterator<T>, Runnable, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 6695226475494099826L;
/*     */     
/*     */     final SpscArrayQueue<T> queue;
/*     */     
/*     */     final long batchSize;
/*     */     
/*     */     final long limit;
/*     */     
/*     */     final Lock lock;
/*     */     
/*     */     final Condition condition;
/*     */     
/*     */     long produced;
/*     */     volatile boolean done;
/*     */     volatile Throwable error;
/*     */     
/*     */     BlockingFlowableIterator(int batchSize) {
/*  68 */       this.queue = new SpscArrayQueue(batchSize);
/*  69 */       this.batchSize = batchSize;
/*  70 */       this.limit = (batchSize - (batchSize >> 2));
/*  71 */       this.lock = new ReentrantLock();
/*  72 */       this.condition = this.lock.newCondition();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/*     */       while (true) {
/*  78 */         if (isDisposed()) {
/*  79 */           Throwable e = this.error;
/*  80 */           if (e != null) {
/*  81 */             throw ExceptionHelper.wrapOrThrow(e);
/*     */           }
/*  83 */           return false;
/*     */         } 
/*  85 */         boolean d = this.done;
/*  86 */         boolean empty = this.queue.isEmpty();
/*  87 */         if (d) {
/*  88 */           Throwable e = this.error;
/*  89 */           if (e != null) {
/*  90 */             throw ExceptionHelper.wrapOrThrow(e);
/*     */           }
/*  92 */           if (empty) {
/*  93 */             return false;
/*     */           }
/*     */         } 
/*  96 */         if (empty) {
/*  97 */           BlockingHelper.verifyNonBlocking();
/*  98 */           this.lock.lock();
/*     */           try {
/* 100 */             while (!this.done && this.queue.isEmpty() && !isDisposed()) {
/* 101 */               this.condition.await();
/*     */             }
/* 103 */           } catch (InterruptedException ex) {
/* 104 */             run();
/* 105 */             throw ExceptionHelper.wrapOrThrow(ex);
/*     */           } finally {
/* 107 */             this.lock.unlock();
/*     */           }  continue;
/*     */         }  break;
/* 110 */       }  return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public T next() {
/* 117 */       if (hasNext()) {
/* 118 */         T v = (T)this.queue.poll();
/*     */         
/* 120 */         long p = this.produced + 1L;
/* 121 */         if (p == this.limit) {
/* 122 */           this.produced = 0L;
/* 123 */           get().request(p);
/*     */         } else {
/* 125 */           this.produced = p;
/*     */         } 
/*     */         
/* 128 */         return v;
/*     */       } 
/* 130 */       throw new NoSuchElementException();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 135 */       SubscriptionHelper.setOnce(this, s, this.batchSize);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 140 */       if (!this.queue.offer(t)) {
/* 141 */         SubscriptionHelper.cancel(this);
/*     */         
/* 143 */         onError((Throwable)new MissingBackpressureException("Queue full?!"));
/*     */       } else {
/* 145 */         signalConsumer();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 151 */       this.error = t;
/* 152 */       this.done = true;
/* 153 */       signalConsumer();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 158 */       this.done = true;
/* 159 */       signalConsumer();
/*     */     }
/*     */     
/*     */     void signalConsumer() {
/* 163 */       this.lock.lock();
/*     */       try {
/* 165 */         this.condition.signalAll();
/*     */       } finally {
/* 167 */         this.lock.unlock();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 173 */       SubscriptionHelper.cancel(this);
/* 174 */       signalConsumer();
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 179 */       throw new UnsupportedOperationException("remove");
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 184 */       SubscriptionHelper.cancel(this);
/* 185 */       signalConsumer();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 190 */       return (get() == SubscriptionHelper.CANCELLED);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/BlockingFlowableIterable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */