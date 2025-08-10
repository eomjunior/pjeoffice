/*     */ package io.reactivex.internal.operators.completable;
/*     */ 
/*     */ import io.reactivex.Completable;
/*     */ import io.reactivex.CompletableObserver;
/*     */ import io.reactivex.CompletableSource;
/*     */ import io.reactivex.disposables.CompositeDisposable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
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
/*     */ 
/*     */ 
/*     */ public final class CompletableMergeDelayErrorIterable
/*     */   extends Completable
/*     */ {
/*     */   final Iterable<? extends CompletableSource> sources;
/*     */   
/*     */   public CompletableMergeDelayErrorIterable(Iterable<? extends CompletableSource> sources) {
/*  31 */     this.sources = sources;
/*     */   }
/*     */   
/*     */   public void subscribeActual(CompletableObserver observer) {
/*     */     Iterator<? extends CompletableSource> iterator;
/*  36 */     CompositeDisposable set = new CompositeDisposable();
/*     */     
/*  38 */     observer.onSubscribe((Disposable)set);
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  43 */       iterator = (Iterator<? extends CompletableSource>)ObjectHelper.requireNonNull(this.sources.iterator(), "The source iterator returned is null");
/*  44 */     } catch (Throwable e) {
/*  45 */       Exceptions.throwIfFatal(e);
/*  46 */       observer.onError(e);
/*     */       
/*     */       return;
/*     */     } 
/*  50 */     AtomicInteger wip = new AtomicInteger(1);
/*     */     
/*  52 */     AtomicThrowable error = new AtomicThrowable(); while (true) {
/*     */       boolean b;
/*     */       CompletableSource c;
/*  55 */       if (set.isDisposed()) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/*  61 */         b = iterator.hasNext();
/*  62 */       } catch (Throwable e) {
/*  63 */         Exceptions.throwIfFatal(e);
/*  64 */         error.addThrowable(e);
/*     */         
/*     */         break;
/*     */       } 
/*  68 */       if (!b) {
/*     */         break;
/*     */       }
/*     */       
/*  72 */       if (set.isDisposed()) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/*  79 */         c = (CompletableSource)ObjectHelper.requireNonNull(iterator.next(), "The iterator returned a null CompletableSource");
/*  80 */       } catch (Throwable e) {
/*  81 */         Exceptions.throwIfFatal(e);
/*  82 */         error.addThrowable(e);
/*     */         
/*     */         break;
/*     */       } 
/*  86 */       if (set.isDisposed()) {
/*     */         return;
/*     */       }
/*     */       
/*  90 */       wip.getAndIncrement();
/*     */       
/*  92 */       c.subscribe(new CompletableMergeDelayErrorArray.MergeInnerCompletableObserver(observer, set, error, wip));
/*     */     } 
/*     */     
/*  95 */     if (wip.decrementAndGet() == 0) {
/*  96 */       Throwable ex = error.terminate();
/*  97 */       if (ex == null) {
/*  98 */         observer.onComplete();
/*     */       } else {
/* 100 */         observer.onError(ex);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableMergeDelayErrorIterable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */