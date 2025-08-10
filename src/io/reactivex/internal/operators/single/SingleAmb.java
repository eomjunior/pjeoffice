/*     */ package io.reactivex.internal.operators.single;
/*     */ 
/*     */ import io.reactivex.Single;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.SingleSource;
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
/*     */ public final class SingleAmb<T>
/*     */   extends Single<T>
/*     */ {
/*     */   private final SingleSource<? extends T>[] sources;
/*     */   private final Iterable<? extends SingleSource<? extends T>> sourcesIterable;
/*     */   
/*     */   public SingleAmb(SingleSource<? extends T>[] sources, Iterable<? extends SingleSource<? extends T>> sourcesIterable) {
/*  29 */     this.sources = sources;
/*  30 */     this.sourcesIterable = sourcesIterable;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(SingleObserver<? super T> observer) {
/*     */     SingleSource[] arrayOfSingleSource;
/*  36 */     SingleSource<? extends T>[] sources = this.sources;
/*  37 */     int count = 0;
/*  38 */     if (sources == null) {
/*  39 */       arrayOfSingleSource = new SingleSource[8];
/*     */       try {
/*  41 */         for (SingleSource<? extends T> element : this.sourcesIterable) {
/*  42 */           if (element == null) {
/*  43 */             EmptyDisposable.error(new NullPointerException("One of the sources is null"), observer);
/*     */             return;
/*     */           } 
/*  46 */           if (count == arrayOfSingleSource.length) {
/*  47 */             SingleSource[] arrayOfSingleSource1 = new SingleSource[count + (count >> 2)];
/*  48 */             System.arraycopy(arrayOfSingleSource, 0, arrayOfSingleSource1, 0, count);
/*  49 */             arrayOfSingleSource = arrayOfSingleSource1;
/*     */           } 
/*  51 */           arrayOfSingleSource[count++] = element;
/*     */         } 
/*  53 */       } catch (Throwable e) {
/*  54 */         Exceptions.throwIfFatal(e);
/*  55 */         EmptyDisposable.error(e, observer);
/*     */         return;
/*     */       } 
/*     */     } else {
/*  59 */       count = arrayOfSingleSource.length;
/*     */     } 
/*     */     
/*  62 */     AtomicBoolean winner = new AtomicBoolean();
/*  63 */     CompositeDisposable set = new CompositeDisposable();
/*     */     
/*  65 */     observer.onSubscribe((Disposable)set);
/*     */     
/*  67 */     for (int i = 0; i < count; i++) {
/*  68 */       SingleSource<? extends T> s1 = arrayOfSingleSource[i];
/*  69 */       if (set.isDisposed()) {
/*     */         return;
/*     */       }
/*     */       
/*  73 */       if (s1 == null) {
/*  74 */         set.dispose();
/*  75 */         Throwable e = new NullPointerException("One of the sources is null");
/*  76 */         if (winner.compareAndSet(false, true)) {
/*  77 */           observer.onError(e);
/*     */         } else {
/*  79 */           RxJavaPlugins.onError(e);
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/*  84 */       s1.subscribe(new AmbSingleObserver<T>(observer, set, winner));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static final class AmbSingleObserver<T>
/*     */     implements SingleObserver<T>
/*     */   {
/*     */     final CompositeDisposable set;
/*     */     
/*     */     final SingleObserver<? super T> downstream;
/*     */     final AtomicBoolean winner;
/*     */     Disposable upstream;
/*     */     
/*     */     AmbSingleObserver(SingleObserver<? super T> observer, CompositeDisposable set, AtomicBoolean winner) {
/*  99 */       this.downstream = observer;
/* 100 */       this.set = set;
/* 101 */       this.winner = winner;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 106 */       this.upstream = d;
/* 107 */       this.set.add(d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/* 112 */       if (this.winner.compareAndSet(false, true)) {
/* 113 */         this.set.delete(this.upstream);
/* 114 */         this.set.dispose();
/* 115 */         this.downstream.onSuccess(value);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 121 */       if (this.winner.compareAndSet(false, true)) {
/* 122 */         this.set.delete(this.upstream);
/* 123 */         this.set.dispose();
/* 124 */         this.downstream.onError(e);
/*     */       } else {
/* 126 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleAmb.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */