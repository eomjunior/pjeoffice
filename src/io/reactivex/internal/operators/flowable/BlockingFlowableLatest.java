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
/*     */ import java.util.concurrent.Semaphore;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ public final class BlockingFlowableLatest<T>
/*     */   implements Iterable<T>
/*     */ {
/*     */   final Publisher<? extends T> source;
/*     */   
/*     */   public BlockingFlowableLatest(Publisher<? extends T> source) {
/*  37 */     this.source = source;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<T> iterator() {
/*  42 */     LatestSubscriberIterator<T> lio = new LatestSubscriberIterator<T>();
/*  43 */     Flowable.fromPublisher(this.source).materialize().subscribe((FlowableSubscriber)lio);
/*  44 */     return lio;
/*     */   }
/*     */   
/*     */   static final class LatestSubscriberIterator<T>
/*     */     extends DisposableSubscriber<Notification<T>> implements Iterator<T> {
/*  49 */     final Semaphore notify = new Semaphore(0);
/*     */     
/*  51 */     final AtomicReference<Notification<T>> value = new AtomicReference<Notification<T>>();
/*     */ 
/*     */     
/*     */     Notification<T> iteratorNotification;
/*     */ 
/*     */     
/*     */     public void onNext(Notification<T> args) {
/*  58 */       boolean wasNotAvailable = (this.value.getAndSet(args) == null);
/*  59 */       if (wasNotAvailable) {
/*  60 */         this.notify.release();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*  66 */       RxJavaPlugins.onError(e);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onComplete() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/*  76 */       if (this.iteratorNotification != null && this.iteratorNotification.isOnError()) {
/*  77 */         throw ExceptionHelper.wrapOrThrow(this.iteratorNotification.getError());
/*     */       }
/*  79 */       if ((this.iteratorNotification == null || this.iteratorNotification.isOnNext()) && 
/*  80 */         this.iteratorNotification == null) {
/*     */         try {
/*  82 */           BlockingHelper.verifyNonBlocking();
/*  83 */           this.notify.acquire();
/*  84 */         } catch (InterruptedException ex) {
/*  85 */           dispose();
/*  86 */           this.iteratorNotification = Notification.createOnError(ex);
/*  87 */           throw ExceptionHelper.wrapOrThrow(ex);
/*     */         } 
/*     */         
/*  90 */         Notification<T> n = this.value.getAndSet(null);
/*  91 */         this.iteratorNotification = n;
/*  92 */         if (n.isOnError()) {
/*  93 */           throw ExceptionHelper.wrapOrThrow(n.getError());
/*     */         }
/*     */       } 
/*     */       
/*  97 */       return this.iteratorNotification.isOnNext();
/*     */     }
/*     */ 
/*     */     
/*     */     public T next() {
/* 102 */       if (hasNext() && 
/* 103 */         this.iteratorNotification.isOnNext()) {
/* 104 */         T v = (T)this.iteratorNotification.getValue();
/* 105 */         this.iteratorNotification = null;
/* 106 */         return v;
/*     */       } 
/*     */       
/* 109 */       throw new NoSuchElementException();
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 114 */       throw new UnsupportedOperationException("Read-only iterator.");
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/BlockingFlowableLatest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */