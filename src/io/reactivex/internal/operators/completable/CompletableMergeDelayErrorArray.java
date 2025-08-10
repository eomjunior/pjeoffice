/*     */ package io.reactivex.internal.operators.completable;
/*     */ 
/*     */ import io.reactivex.Completable;
/*     */ import io.reactivex.CompletableObserver;
/*     */ import io.reactivex.CompletableSource;
/*     */ import io.reactivex.disposables.CompositeDisposable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
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
/*     */ public final class CompletableMergeDelayErrorArray
/*     */   extends Completable
/*     */ {
/*     */   final CompletableSource[] sources;
/*     */   
/*     */   public CompletableMergeDelayErrorArray(CompletableSource[] sources) {
/*  28 */     this.sources = sources;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(CompletableObserver observer) {
/*  33 */     CompositeDisposable set = new CompositeDisposable();
/*  34 */     AtomicInteger wip = new AtomicInteger(this.sources.length + 1);
/*     */     
/*  36 */     AtomicThrowable error = new AtomicThrowable();
/*     */     
/*  38 */     observer.onSubscribe((Disposable)set);
/*     */     
/*  40 */     for (CompletableSource c : this.sources) {
/*  41 */       if (set.isDisposed()) {
/*     */         return;
/*     */       }
/*     */       
/*  45 */       if (c == null) {
/*  46 */         Throwable ex = new NullPointerException("A completable source is null");
/*  47 */         error.addThrowable(ex);
/*  48 */         wip.decrementAndGet();
/*     */       }
/*     */       else {
/*     */         
/*  52 */         c.subscribe(new MergeInnerCompletableObserver(observer, set, error, wip));
/*     */       } 
/*     */     } 
/*  55 */     if (wip.decrementAndGet() == 0) {
/*  56 */       Throwable ex = error.terminate();
/*  57 */       if (ex == null) {
/*  58 */         observer.onComplete();
/*     */       } else {
/*  60 */         observer.onError(ex);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   static final class MergeInnerCompletableObserver
/*     */     implements CompletableObserver
/*     */   {
/*     */     final CompletableObserver downstream;
/*     */     final CompositeDisposable set;
/*     */     final AtomicThrowable error;
/*     */     final AtomicInteger wip;
/*     */     
/*     */     MergeInnerCompletableObserver(CompletableObserver observer, CompositeDisposable set, AtomicThrowable error, AtomicInteger wip) {
/*  74 */       this.downstream = observer;
/*  75 */       this.set = set;
/*  76 */       this.error = error;
/*  77 */       this.wip = wip;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  82 */       this.set.add(d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*  87 */       if (this.error.addThrowable(e)) {
/*  88 */         tryTerminate();
/*     */       } else {
/*  90 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  96 */       tryTerminate();
/*     */     }
/*     */     
/*     */     void tryTerminate() {
/* 100 */       if (this.wip.decrementAndGet() == 0) {
/* 101 */         Throwable ex = this.error.terminate();
/* 102 */         if (ex == null) {
/* 103 */           this.downstream.onComplete();
/*     */         } else {
/* 105 */           this.downstream.onError(ex);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableMergeDelayErrorArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */