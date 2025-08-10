/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.Notification;
/*     */ import io.reactivex.internal.util.BlockingHelper;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import io.reactivex.subscribers.DisposableSubscriber;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.concurrent.ArrayBlockingQueue;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.reactivestreams.Publisher;
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
/*     */ public final class BlockingFlowableNext<T>
/*     */   implements Iterable<T>
/*     */ {
/*     */   final Publisher<? extends T> source;
/*     */   
/*     */   public BlockingFlowableNext(Publisher<? extends T> source) {
/*  39 */     this.source = source;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<T> iterator() {
/*  44 */     NextSubscriber<T> nextSubscriber = new NextSubscriber<T>();
/*  45 */     return new NextIterator<T>(this.source, nextSubscriber);
/*     */   }
/*     */   
/*     */   static final class NextIterator<T>
/*     */     implements Iterator<T>
/*     */   {
/*     */     private final BlockingFlowableNext.NextSubscriber<T> subscriber;
/*     */     private final Publisher<? extends T> items;
/*     */     private T next;
/*     */     private boolean hasNext = true;
/*     */     private boolean isNextConsumed = true;
/*     */     private Throwable error;
/*     */     private boolean started;
/*     */     
/*     */     NextIterator(Publisher<? extends T> items, BlockingFlowableNext.NextSubscriber<T> subscriber) {
/*  60 */       this.items = items;
/*  61 */       this.subscriber = subscriber;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/*  66 */       if (this.error != null)
/*     */       {
/*  68 */         throw ExceptionHelper.wrapOrThrow(this.error);
/*     */       }
/*     */ 
/*     */       
/*  72 */       if (!this.hasNext)
/*     */       {
/*  74 */         return false;
/*     */       }
/*     */       
/*  77 */       return (!this.isNextConsumed || moveToNext());
/*     */     }
/*     */     
/*     */     private boolean moveToNext() {
/*     */       try {
/*  82 */         if (!this.started) {
/*  83 */           this.started = true;
/*     */           
/*  85 */           this.subscriber.setWaiting();
/*  86 */           Flowable.fromPublisher(this.items)
/*  87 */             .materialize().subscribe((FlowableSubscriber)this.subscriber);
/*     */         } 
/*     */         
/*  90 */         Notification<T> nextNotification = this.subscriber.takeNext();
/*  91 */         if (nextNotification.isOnNext()) {
/*  92 */           this.isNextConsumed = false;
/*  93 */           this.next = (T)nextNotification.getValue();
/*  94 */           return true;
/*     */         } 
/*     */ 
/*     */         
/*  98 */         this.hasNext = false;
/*  99 */         if (nextNotification.isOnComplete()) {
/* 100 */           return false;
/*     */         }
/* 102 */         if (nextNotification.isOnError()) {
/* 103 */           this.error = nextNotification.getError();
/* 104 */           throw ExceptionHelper.wrapOrThrow(this.error);
/*     */         } 
/* 106 */         throw new IllegalStateException("Should not reach here");
/* 107 */       } catch (InterruptedException e) {
/* 108 */         this.subscriber.dispose();
/* 109 */         this.error = e;
/* 110 */         throw ExceptionHelper.wrapOrThrow(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public T next() {
/* 116 */       if (this.error != null)
/*     */       {
/* 118 */         throw ExceptionHelper.wrapOrThrow(this.error);
/*     */       }
/* 120 */       if (hasNext()) {
/* 121 */         this.isNextConsumed = true;
/* 122 */         return this.next;
/*     */       } 
/*     */       
/* 125 */       throw new NoSuchElementException("No more elements");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void remove() {
/* 131 */       throw new UnsupportedOperationException("Read only iterator");
/*     */     }
/*     */   }
/*     */   
/*     */   static final class NextSubscriber<T> extends DisposableSubscriber<Notification<T>> {
/* 136 */     private final BlockingQueue<Notification<T>> buf = new ArrayBlockingQueue<Notification<T>>(1);
/* 137 */     final AtomicInteger waiting = new AtomicInteger();
/*     */ 
/*     */ 
/*     */     
/*     */     public void onComplete() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 146 */       RxJavaPlugins.onError(e);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onNext(Notification<T> args) {
/* 152 */       if (this.waiting.getAndSet(0) == 1 || !args.isOnNext()) {
/* 153 */         Notification<T> toOffer = args;
/* 154 */         while (!this.buf.offer(toOffer)) {
/* 155 */           Notification<T> concurrentItem = this.buf.poll();
/*     */ 
/*     */           
/* 158 */           if (concurrentItem != null && !concurrentItem.isOnNext()) {
/* 159 */             toOffer = concurrentItem;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Notification<T> takeNext() throws InterruptedException {
/* 167 */       setWaiting();
/* 168 */       BlockingHelper.verifyNonBlocking();
/* 169 */       return this.buf.take();
/*     */     }
/*     */     void setWaiting() {
/* 172 */       this.waiting.set(1);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/BlockingFlowableNext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */