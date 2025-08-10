/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Notification;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.internal.util.BlockingHelper;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import io.reactivex.observers.DisposableObserver;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.concurrent.ArrayBlockingQueue;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ public final class BlockingObservableNext<T>
/*     */   implements Iterable<T>
/*     */ {
/*     */   final ObservableSource<T> source;
/*     */   
/*     */   public BlockingObservableNext(ObservableSource<T> source) {
/*  37 */     this.source = source;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<T> iterator() {
/*  42 */     NextObserver<T> nextObserver = new NextObserver<T>();
/*  43 */     return new NextIterator<T>(this.source, nextObserver);
/*     */   }
/*     */   
/*     */   static final class NextIterator<T>
/*     */     implements Iterator<T>
/*     */   {
/*     */     private final BlockingObservableNext.NextObserver<T> observer;
/*     */     private final ObservableSource<T> items;
/*     */     private T next;
/*     */     private boolean hasNext = true;
/*     */     private boolean isNextConsumed = true;
/*     */     private Throwable error;
/*     */     private boolean started;
/*     */     
/*     */     NextIterator(ObservableSource<T> items, BlockingObservableNext.NextObserver<T> observer) {
/*  58 */       this.items = items;
/*  59 */       this.observer = observer;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/*  64 */       if (this.error != null)
/*     */       {
/*  66 */         throw ExceptionHelper.wrapOrThrow(this.error);
/*     */       }
/*     */ 
/*     */       
/*  70 */       if (!this.hasNext)
/*     */       {
/*  72 */         return false;
/*     */       }
/*     */       
/*  75 */       return (!this.isNextConsumed || moveToNext());
/*     */     }
/*     */     private boolean moveToNext() {
/*     */       Notification<T> nextNotification;
/*  79 */       if (!this.started) {
/*  80 */         this.started = true;
/*     */         
/*  82 */         this.observer.setWaiting();
/*  83 */         (new ObservableMaterialize(this.items)).subscribe((Observer)this.observer);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/*  89 */         nextNotification = this.observer.takeNext();
/*  90 */       } catch (InterruptedException e) {
/*  91 */         this.observer.dispose();
/*  92 */         this.error = e;
/*  93 */         throw ExceptionHelper.wrapOrThrow(e);
/*     */       } 
/*     */       
/*  96 */       if (nextNotification.isOnNext()) {
/*  97 */         this.isNextConsumed = false;
/*  98 */         this.next = (T)nextNotification.getValue();
/*  99 */         return true;
/*     */       } 
/*     */ 
/*     */       
/* 103 */       this.hasNext = false;
/* 104 */       if (nextNotification.isOnComplete()) {
/* 105 */         return false;
/*     */       }
/* 107 */       this.error = nextNotification.getError();
/* 108 */       throw ExceptionHelper.wrapOrThrow(this.error);
/*     */     }
/*     */ 
/*     */     
/*     */     public T next() {
/* 113 */       if (this.error != null)
/*     */       {
/* 115 */         throw ExceptionHelper.wrapOrThrow(this.error);
/*     */       }
/* 117 */       if (hasNext()) {
/* 118 */         this.isNextConsumed = true;
/* 119 */         return this.next;
/*     */       } 
/*     */       
/* 122 */       throw new NoSuchElementException("No more elements");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void remove() {
/* 128 */       throw new UnsupportedOperationException("Read only iterator");
/*     */     }
/*     */   }
/*     */   
/*     */   static final class NextObserver<T> extends DisposableObserver<Notification<T>> {
/* 133 */     private final BlockingQueue<Notification<T>> buf = new ArrayBlockingQueue<Notification<T>>(1);
/* 134 */     final AtomicInteger waiting = new AtomicInteger();
/*     */ 
/*     */ 
/*     */     
/*     */     public void onComplete() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 143 */       RxJavaPlugins.onError(e);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onNext(Notification<T> args) {
/* 149 */       if (this.waiting.getAndSet(0) == 1 || !args.isOnNext()) {
/* 150 */         Notification<T> toOffer = args;
/* 151 */         while (!this.buf.offer(toOffer)) {
/* 152 */           Notification<T> concurrentItem = this.buf.poll();
/*     */ 
/*     */           
/* 155 */           if (concurrentItem != null && !concurrentItem.isOnNext()) {
/* 156 */             toOffer = concurrentItem;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Notification<T> takeNext() throws InterruptedException {
/* 164 */       setWaiting();
/* 165 */       BlockingHelper.verifyNonBlocking();
/* 166 */       return this.buf.take();
/*     */     }
/*     */     void setWaiting() {
/* 169 */       this.waiting.set(1);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/BlockingObservableNext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */