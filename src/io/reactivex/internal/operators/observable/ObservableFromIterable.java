/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.observers.BasicQueueDisposable;
/*     */ import java.util.Iterator;
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
/*     */ public final class ObservableFromIterable<T>
/*     */   extends Observable<T>
/*     */ {
/*     */   final Iterable<? extends T> source;
/*     */   
/*     */   public ObservableFromIterable(Iterable<? extends T> source) {
/*  28 */     this.source = source;
/*     */   }
/*     */   
/*     */   public void subscribeActual(Observer<? super T> observer) {
/*     */     Iterator<? extends T> it;
/*     */     boolean hasNext;
/*     */     try {
/*  35 */       it = this.source.iterator();
/*  36 */     } catch (Throwable e) {
/*  37 */       Exceptions.throwIfFatal(e);
/*  38 */       EmptyDisposable.error(e, observer);
/*     */       
/*     */       return;
/*     */     } 
/*     */     try {
/*  43 */       hasNext = it.hasNext();
/*  44 */     } catch (Throwable e) {
/*  45 */       Exceptions.throwIfFatal(e);
/*  46 */       EmptyDisposable.error(e, observer);
/*     */       return;
/*     */     } 
/*  49 */     if (!hasNext) {
/*  50 */       EmptyDisposable.complete(observer);
/*     */       
/*     */       return;
/*     */     } 
/*  54 */     FromIterableDisposable<T> d = new FromIterableDisposable<T>(observer, it);
/*  55 */     observer.onSubscribe((Disposable)d);
/*     */     
/*  57 */     if (!d.fusionMode) {
/*  58 */       d.run();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class FromIterableDisposable<T>
/*     */     extends BasicQueueDisposable<T>
/*     */   {
/*     */     final Observer<? super T> downstream;
/*     */     
/*     */     final Iterator<? extends T> it;
/*     */     
/*     */     volatile boolean disposed;
/*     */     
/*     */     boolean fusionMode;
/*     */     boolean done;
/*     */     boolean checkNext;
/*     */     
/*     */     FromIterableDisposable(Observer<? super T> actual, Iterator<? extends T> it) {
/*  77 */       this.downstream = actual;
/*  78 */       this.it = it;
/*     */     }
/*     */     
/*     */     void run() {
/*     */       boolean hasNext;
/*     */       do {
/*     */         T v;
/*  85 */         if (isDisposed()) {
/*     */           return;
/*     */         }
/*     */ 
/*     */         
/*     */         try {
/*  91 */           v = (T)ObjectHelper.requireNonNull(this.it.next(), "The iterator returned a null value");
/*  92 */         } catch (Throwable e) {
/*  93 */           Exceptions.throwIfFatal(e);
/*  94 */           this.downstream.onError(e);
/*     */           
/*     */           return;
/*     */         } 
/*  98 */         this.downstream.onNext(v);
/*     */         
/* 100 */         if (isDisposed()) {
/*     */           return;
/*     */         }
/*     */         try {
/* 104 */           hasNext = this.it.hasNext();
/* 105 */         } catch (Throwable e) {
/* 106 */           Exceptions.throwIfFatal(e);
/* 107 */           this.downstream.onError(e);
/*     */           return;
/*     */         } 
/* 110 */       } while (hasNext);
/*     */       
/* 112 */       if (!isDisposed()) {
/* 113 */         this.downstream.onComplete();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public int requestFusion(int mode) {
/* 119 */       if ((mode & 0x1) != 0) {
/* 120 */         this.fusionMode = true;
/* 121 */         return 1;
/*     */       } 
/* 123 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public T poll() {
/* 129 */       if (this.done) {
/* 130 */         return null;
/*     */       }
/* 132 */       if (this.checkNext) {
/* 133 */         if (!this.it.hasNext()) {
/* 134 */           this.done = true;
/* 135 */           return null;
/*     */         } 
/*     */       } else {
/* 138 */         this.checkNext = true;
/*     */       } 
/*     */       
/* 141 */       return (T)ObjectHelper.requireNonNull(this.it.next(), "The iterator returned a null value");
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 146 */       return this.done;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 151 */       this.done = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 156 */       this.disposed = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 161 */       return this.disposed;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableFromIterable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */