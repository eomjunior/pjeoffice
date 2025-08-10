/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.queue.SpscLinkedArrayQueue;
/*     */ import io.reactivex.internal.util.BlockingHelper;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.concurrent.locks.Condition;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BlockingObservableIterable<T>
/*     */   implements Iterable<T>
/*     */ {
/*     */   final ObservableSource<? extends T> source;
/*     */   final int bufferSize;
/*     */   
/*     */   public BlockingObservableIterable(ObservableSource<? extends T> source, int bufferSize) {
/*  32 */     this.source = source;
/*  33 */     this.bufferSize = bufferSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<T> iterator() {
/*  38 */     BlockingObservableIterator<T> it = new BlockingObservableIterator<T>(this.bufferSize);
/*  39 */     this.source.subscribe(it);
/*  40 */     return it;
/*     */   }
/*     */ 
/*     */   
/*     */   static final class BlockingObservableIterator<T>
/*     */     extends AtomicReference<Disposable>
/*     */     implements Observer<T>, Iterator<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 6695226475494099826L;
/*     */     
/*     */     final SpscLinkedArrayQueue<T> queue;
/*     */     
/*     */     final Lock lock;
/*     */     
/*     */     final Condition condition;
/*     */     volatile boolean done;
/*     */     volatile Throwable error;
/*     */     
/*     */     BlockingObservableIterator(int batchSize) {
/*  59 */       this.queue = new SpscLinkedArrayQueue(batchSize);
/*  60 */       this.lock = new ReentrantLock();
/*  61 */       this.condition = this.lock.newCondition();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/*     */       while (true) {
/*  67 */         if (isDisposed()) {
/*  68 */           Throwable e = this.error;
/*  69 */           if (e != null) {
/*  70 */             throw ExceptionHelper.wrapOrThrow(e);
/*     */           }
/*  72 */           return false;
/*     */         } 
/*  74 */         boolean d = this.done;
/*  75 */         boolean empty = this.queue.isEmpty();
/*  76 */         if (d) {
/*  77 */           Throwable e = this.error;
/*  78 */           if (e != null) {
/*  79 */             throw ExceptionHelper.wrapOrThrow(e);
/*     */           }
/*  81 */           if (empty) {
/*  82 */             return false;
/*     */           }
/*     */         } 
/*  85 */         if (empty) {
/*     */           try {
/*  87 */             BlockingHelper.verifyNonBlocking();
/*  88 */             this.lock.lock();
/*     */             try {
/*  90 */               while (!this.done && this.queue.isEmpty() && !isDisposed()) {
/*  91 */                 this.condition.await();
/*     */               }
/*     */             } finally {
/*  94 */               this.lock.unlock();
/*     */             } 
/*  96 */           } catch (InterruptedException ex) {
/*  97 */             DisposableHelper.dispose(this);
/*  98 */             signalConsumer();
/*  99 */             throw ExceptionHelper.wrapOrThrow(ex);
/*     */           }  continue;
/*     */         }  break;
/* 102 */       }  return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public T next() {
/* 109 */       if (hasNext()) {
/* 110 */         return (T)this.queue.poll();
/*     */       }
/* 112 */       throw new NoSuchElementException();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 117 */       DisposableHelper.setOnce(this, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 122 */       this.queue.offer(t);
/* 123 */       signalConsumer();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 128 */       this.error = t;
/* 129 */       this.done = true;
/* 130 */       signalConsumer();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 135 */       this.done = true;
/* 136 */       signalConsumer();
/*     */     }
/*     */     
/*     */     void signalConsumer() {
/* 140 */       this.lock.lock();
/*     */       try {
/* 142 */         this.condition.signalAll();
/*     */       } finally {
/* 144 */         this.lock.unlock();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 150 */       throw new UnsupportedOperationException("remove");
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 155 */       DisposableHelper.dispose(this);
/* 156 */       signalConsumer();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 161 */       return DisposableHelper.isDisposed(get());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/BlockingObservableIterable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */