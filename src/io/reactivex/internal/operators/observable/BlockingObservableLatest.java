/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Notification;
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.internal.util.BlockingHelper;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import io.reactivex.observers.DisposableObserver;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.concurrent.Semaphore;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ public final class BlockingObservableLatest<T>
/*     */   implements Iterable<T>
/*     */ {
/*     */   final ObservableSource<T> source;
/*     */   
/*     */   public BlockingObservableLatest(ObservableSource<T> source) {
/*  36 */     this.source = source;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<T> iterator() {
/*  41 */     BlockingObservableLatestIterator<T> lio = new BlockingObservableLatestIterator<T>();
/*     */     
/*  43 */     Observable<Notification<T>> materialized = Observable.wrap(this.source).materialize();
/*     */     
/*  45 */     materialized.subscribe((Observer)lio);
/*  46 */     return lio;
/*     */   }
/*     */   
/*     */   static final class BlockingObservableLatestIterator<T>
/*     */     extends DisposableObserver<Notification<T>>
/*     */     implements Iterator<T> {
/*     */     Notification<T> iteratorNotification;
/*  53 */     final Semaphore notify = new Semaphore(0);
/*     */     
/*  55 */     final AtomicReference<Notification<T>> value = new AtomicReference<Notification<T>>();
/*     */ 
/*     */     
/*     */     public void onNext(Notification<T> args) {
/*  59 */       boolean wasNotAvailable = (this.value.getAndSet(args) == null);
/*  60 */       if (wasNotAvailable) {
/*  61 */         this.notify.release();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*  67 */       RxJavaPlugins.onError(e);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onComplete() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/*  77 */       if (this.iteratorNotification != null && this.iteratorNotification.isOnError()) {
/*  78 */         throw ExceptionHelper.wrapOrThrow(this.iteratorNotification.getError());
/*     */       }
/*  80 */       if (this.iteratorNotification == null) {
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
/*  96 */       return this.iteratorNotification.isOnNext();
/*     */     }
/*     */ 
/*     */     
/*     */     public T next() {
/* 101 */       if (hasNext()) {
/* 102 */         T v = (T)this.iteratorNotification.getValue();
/* 103 */         this.iteratorNotification = null;
/* 104 */         return v;
/*     */       } 
/* 106 */       throw new NoSuchElementException();
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 111 */       throw new UnsupportedOperationException("Read-only iterator.");
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/BlockingObservableLatest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */