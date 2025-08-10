/*     */ package io.reactivex.internal.operators.completable;
/*     */ 
/*     */ import io.reactivex.Completable;
/*     */ import io.reactivex.CompletableObserver;
/*     */ import io.reactivex.CompletableSource;
/*     */ import io.reactivex.disposables.CompositeDisposable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.Iterator;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ public final class CompletableMergeIterable
/*     */   extends Completable
/*     */ {
/*     */   final Iterable<? extends CompletableSource> sources;
/*     */   
/*     */   public CompletableMergeIterable(Iterable<? extends CompletableSource> sources) {
/*  29 */     this.sources = sources;
/*     */   }
/*     */   
/*     */   public void subscribeActual(CompletableObserver observer) {
/*     */     Iterator<? extends CompletableSource> iterator;
/*  34 */     CompositeDisposable set = new CompositeDisposable();
/*     */     
/*  36 */     observer.onSubscribe((Disposable)set);
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  41 */       iterator = (Iterator<? extends CompletableSource>)ObjectHelper.requireNonNull(this.sources.iterator(), "The source iterator returned is null");
/*  42 */     } catch (Throwable e) {
/*  43 */       Exceptions.throwIfFatal(e);
/*  44 */       observer.onError(e);
/*     */       
/*     */       return;
/*     */     } 
/*  48 */     AtomicInteger wip = new AtomicInteger(1);
/*     */     
/*  50 */     MergeCompletableObserver shared = new MergeCompletableObserver(observer, set, wip); while (true) {
/*     */       boolean b; CompletableSource c;
/*  52 */       if (set.isDisposed()) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/*  58 */         b = iterator.hasNext();
/*  59 */       } catch (Throwable e) {
/*  60 */         Exceptions.throwIfFatal(e);
/*  61 */         set.dispose();
/*  62 */         shared.onError(e);
/*     */         
/*     */         return;
/*     */       } 
/*  66 */       if (!b) {
/*     */         break;
/*     */       }
/*     */       
/*  70 */       if (set.isDisposed()) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/*  77 */         c = (CompletableSource)ObjectHelper.requireNonNull(iterator.next(), "The iterator returned a null CompletableSource");
/*  78 */       } catch (Throwable e) {
/*  79 */         Exceptions.throwIfFatal(e);
/*  80 */         set.dispose();
/*  81 */         shared.onError(e);
/*     */         
/*     */         return;
/*     */       } 
/*  85 */       if (set.isDisposed()) {
/*     */         return;
/*     */       }
/*     */       
/*  89 */       wip.getAndIncrement();
/*     */       
/*  91 */       c.subscribe(shared);
/*     */     } 
/*     */     
/*  94 */     shared.onComplete();
/*     */   }
/*     */ 
/*     */   
/*     */   static final class MergeCompletableObserver
/*     */     extends AtomicBoolean
/*     */     implements CompletableObserver
/*     */   {
/*     */     private static final long serialVersionUID = -7730517613164279224L;
/*     */     final CompositeDisposable set;
/*     */     final CompletableObserver downstream;
/*     */     final AtomicInteger wip;
/*     */     
/*     */     MergeCompletableObserver(CompletableObserver actual, CompositeDisposable set, AtomicInteger wip) {
/* 108 */       this.downstream = actual;
/* 109 */       this.set = set;
/* 110 */       this.wip = wip;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 115 */       this.set.add(d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 120 */       this.set.dispose();
/* 121 */       if (compareAndSet(false, true)) {
/* 122 */         this.downstream.onError(e);
/*     */       } else {
/* 124 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 130 */       if (this.wip.decrementAndGet() == 0 && 
/* 131 */         compareAndSet(false, true))
/* 132 */         this.downstream.onComplete(); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableMergeIterable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */