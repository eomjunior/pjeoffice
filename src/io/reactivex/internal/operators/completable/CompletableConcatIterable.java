/*     */ package io.reactivex.internal.operators.completable;
/*     */ 
/*     */ import io.reactivex.Completable;
/*     */ import io.reactivex.CompletableObserver;
/*     */ import io.reactivex.CompletableSource;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
/*     */ import io.reactivex.internal.disposables.SequentialDisposable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import java.util.Iterator;
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
/*     */ public final class CompletableConcatIterable
/*     */   extends Completable
/*     */ {
/*     */   final Iterable<? extends CompletableSource> sources;
/*     */   
/*     */   public CompletableConcatIterable(Iterable<? extends CompletableSource> sources) {
/*  29 */     this.sources = sources;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void subscribeActual(CompletableObserver observer) {
/*     */     Iterator<? extends CompletableSource> it;
/*     */     try {
/*  38 */       it = (Iterator<? extends CompletableSource>)ObjectHelper.requireNonNull(this.sources.iterator(), "The iterator returned is null");
/*  39 */     } catch (Throwable e) {
/*  40 */       Exceptions.throwIfFatal(e);
/*  41 */       EmptyDisposable.error(e, observer);
/*     */       
/*     */       return;
/*     */     } 
/*  45 */     ConcatInnerObserver inner = new ConcatInnerObserver(observer, it);
/*  46 */     observer.onSubscribe((Disposable)inner.sd);
/*  47 */     inner.next();
/*     */   }
/*     */   
/*     */   static final class ConcatInnerObserver
/*     */     extends AtomicInteger
/*     */     implements CompletableObserver
/*     */   {
/*     */     private static final long serialVersionUID = -7965400327305809232L;
/*     */     final CompletableObserver downstream;
/*     */     final Iterator<? extends CompletableSource> sources;
/*     */     final SequentialDisposable sd;
/*     */     
/*     */     ConcatInnerObserver(CompletableObserver actual, Iterator<? extends CompletableSource> sources) {
/*  60 */       this.downstream = actual;
/*  61 */       this.sources = sources;
/*  62 */       this.sd = new SequentialDisposable();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  67 */       this.sd.replace(d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*  72 */       this.downstream.onError(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  77 */       next();
/*     */     }
/*     */     
/*     */     void next() {
/*  81 */       if (this.sd.isDisposed()) {
/*     */         return;
/*     */       }
/*     */       
/*  85 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/*  89 */       Iterator<? extends CompletableSource> a = this.sources; do {
/*     */         boolean b; CompletableSource c;
/*  91 */         if (this.sd.isDisposed()) {
/*     */           return;
/*     */         }
/*     */ 
/*     */         
/*     */         try {
/*  97 */           b = a.hasNext();
/*  98 */         } catch (Throwable ex) {
/*  99 */           Exceptions.throwIfFatal(ex);
/* 100 */           this.downstream.onError(ex);
/*     */           
/*     */           return;
/*     */         } 
/* 104 */         if (!b) {
/* 105 */           this.downstream.onComplete();
/*     */ 
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/*     */         try {
/* 112 */           c = (CompletableSource)ObjectHelper.requireNonNull(a.next(), "The CompletableSource returned is null");
/* 113 */         } catch (Throwable ex) {
/* 114 */           Exceptions.throwIfFatal(ex);
/* 115 */           this.downstream.onError(ex);
/*     */           
/*     */           return;
/*     */         } 
/* 119 */         c.subscribe(this);
/* 120 */       } while (decrementAndGet() != 0);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableConcatIterable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */