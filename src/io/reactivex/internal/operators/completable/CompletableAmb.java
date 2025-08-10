/*     */ package io.reactivex.internal.operators.completable;
/*     */ 
/*     */ import io.reactivex.Completable;
/*     */ import io.reactivex.CompletableObserver;
/*     */ import io.reactivex.CompletableSource;
/*     */ import io.reactivex.disposables.CompositeDisposable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ public final class CompletableAmb
/*     */   extends Completable
/*     */ {
/*     */   private final CompletableSource[] sources;
/*     */   private final Iterable<? extends CompletableSource> sourcesIterable;
/*     */   
/*     */   public CompletableAmb(CompletableSource[] sources, Iterable<? extends CompletableSource> sourcesIterable) {
/*  29 */     this.sources = sources;
/*  30 */     this.sourcesIterable = sourcesIterable;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(CompletableObserver observer) {
/*  35 */     CompletableSource[] sources = this.sources;
/*  36 */     int count = 0;
/*  37 */     if (sources == null) {
/*  38 */       sources = new CompletableSource[8];
/*     */       try {
/*  40 */         for (CompletableSource element : this.sourcesIterable) {
/*  41 */           if (element == null) {
/*  42 */             EmptyDisposable.error(new NullPointerException("One of the sources is null"), observer);
/*     */             return;
/*     */           } 
/*  45 */           if (count == sources.length) {
/*  46 */             CompletableSource[] b = new CompletableSource[count + (count >> 2)];
/*  47 */             System.arraycopy(sources, 0, b, 0, count);
/*  48 */             sources = b;
/*     */           } 
/*  50 */           sources[count++] = element;
/*     */         } 
/*  52 */       } catch (Throwable e) {
/*  53 */         Exceptions.throwIfFatal(e);
/*  54 */         EmptyDisposable.error(e, observer);
/*     */         return;
/*     */       } 
/*     */     } else {
/*  58 */       count = sources.length;
/*     */     } 
/*     */     
/*  61 */     CompositeDisposable set = new CompositeDisposable();
/*  62 */     observer.onSubscribe((Disposable)set);
/*     */     
/*  64 */     AtomicBoolean once = new AtomicBoolean();
/*     */     
/*  66 */     for (int i = 0; i < count; i++) {
/*  67 */       CompletableSource c = sources[i];
/*  68 */       if (set.isDisposed()) {
/*     */         return;
/*     */       }
/*  71 */       if (c == null) {
/*  72 */         NullPointerException npe = new NullPointerException("One of the sources is null");
/*  73 */         if (once.compareAndSet(false, true)) {
/*  74 */           set.dispose();
/*  75 */           observer.onError(npe);
/*     */         } else {
/*  77 */           RxJavaPlugins.onError(npe);
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/*  83 */       c.subscribe(new Amb(once, set, observer));
/*     */     } 
/*     */     
/*  86 */     if (count == 0) {
/*  87 */       observer.onComplete();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class Amb
/*     */     implements CompletableObserver
/*     */   {
/*     */     final AtomicBoolean once;
/*     */     
/*     */     final CompositeDisposable set;
/*     */     final CompletableObserver downstream;
/*     */     Disposable upstream;
/*     */     
/*     */     Amb(AtomicBoolean once, CompositeDisposable set, CompletableObserver observer) {
/* 102 */       this.once = once;
/* 103 */       this.set = set;
/* 104 */       this.downstream = observer;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 109 */       if (this.once.compareAndSet(false, true)) {
/* 110 */         this.set.delete(this.upstream);
/* 111 */         this.set.dispose();
/* 112 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 118 */       if (this.once.compareAndSet(false, true)) {
/* 119 */         this.set.delete(this.upstream);
/* 120 */         this.set.dispose();
/* 121 */         this.downstream.onError(e);
/*     */       } else {
/* 123 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 129 */       this.upstream = d;
/* 130 */       this.set.add(d);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableAmb.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */