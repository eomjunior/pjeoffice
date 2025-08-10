/*     */ package io.reactivex.internal.operators.maybe;
/*     */ 
/*     */ import io.reactivex.Maybe;
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MaybeAmb<T>
/*     */   extends Maybe<T>
/*     */ {
/*     */   private final MaybeSource<? extends T>[] sources;
/*     */   private final Iterable<? extends MaybeSource<? extends T>> sourcesIterable;
/*     */   
/*     */   public MaybeAmb(MaybeSource<? extends T>[] sources, Iterable<? extends MaybeSource<? extends T>> sourcesIterable) {
/*  34 */     this.sources = sources;
/*  35 */     this.sourcesIterable = sourcesIterable;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/*     */     MaybeSource[] arrayOfMaybeSource;
/*  41 */     MaybeSource<? extends T>[] sources = this.sources;
/*  42 */     int count = 0;
/*  43 */     if (sources == null) {
/*  44 */       arrayOfMaybeSource = new MaybeSource[8];
/*     */       try {
/*  46 */         for (MaybeSource<? extends T> element : this.sourcesIterable) {
/*  47 */           if (element == null) {
/*  48 */             EmptyDisposable.error(new NullPointerException("One of the sources is null"), observer);
/*     */             return;
/*     */           } 
/*  51 */           if (count == arrayOfMaybeSource.length) {
/*  52 */             MaybeSource[] arrayOfMaybeSource1 = new MaybeSource[count + (count >> 2)];
/*  53 */             System.arraycopy(arrayOfMaybeSource, 0, arrayOfMaybeSource1, 0, count);
/*  54 */             arrayOfMaybeSource = arrayOfMaybeSource1;
/*     */           } 
/*  56 */           arrayOfMaybeSource[count++] = element;
/*     */         } 
/*  58 */       } catch (Throwable e) {
/*  59 */         Exceptions.throwIfFatal(e);
/*  60 */         EmptyDisposable.error(e, observer);
/*     */         return;
/*     */       } 
/*     */     } else {
/*  64 */       count = arrayOfMaybeSource.length;
/*     */     } 
/*     */     
/*  67 */     CompositeDisposable set = new CompositeDisposable();
/*  68 */     observer.onSubscribe((Disposable)set);
/*     */     
/*  70 */     AtomicBoolean winner = new AtomicBoolean();
/*     */     
/*  72 */     for (int i = 0; i < count; i++) {
/*  73 */       MaybeSource<? extends T> s = arrayOfMaybeSource[i];
/*  74 */       if (set.isDisposed()) {
/*     */         return;
/*     */       }
/*     */       
/*  78 */       if (s == null) {
/*  79 */         set.dispose();
/*  80 */         NullPointerException ex = new NullPointerException("One of the MaybeSources is null");
/*  81 */         if (winner.compareAndSet(false, true)) {
/*  82 */           observer.onError(ex);
/*     */         } else {
/*  84 */           RxJavaPlugins.onError(ex);
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/*  89 */       s.subscribe(new AmbMaybeObserver<T>(observer, set, winner));
/*     */     } 
/*     */     
/*  92 */     if (count == 0) {
/*  93 */       observer.onComplete();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class AmbMaybeObserver<T>
/*     */     implements MaybeObserver<T>
/*     */   {
/*     */     final MaybeObserver<? super T> downstream;
/*     */     
/*     */     final AtomicBoolean winner;
/*     */     
/*     */     final CompositeDisposable set;
/*     */     Disposable upstream;
/*     */     
/*     */     AmbMaybeObserver(MaybeObserver<? super T> downstream, CompositeDisposable set, AtomicBoolean winner) {
/* 109 */       this.downstream = downstream;
/* 110 */       this.set = set;
/* 111 */       this.winner = winner;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 116 */       this.upstream = d;
/* 117 */       this.set.add(d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/* 122 */       if (this.winner.compareAndSet(false, true)) {
/* 123 */         this.set.delete(this.upstream);
/* 124 */         this.set.dispose();
/*     */         
/* 126 */         this.downstream.onSuccess(value);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 132 */       if (this.winner.compareAndSet(false, true)) {
/* 133 */         this.set.delete(this.upstream);
/* 134 */         this.set.dispose();
/*     */         
/* 136 */         this.downstream.onError(e);
/*     */       } else {
/* 138 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 144 */       if (this.winner.compareAndSet(false, true)) {
/* 145 */         this.set.delete(this.upstream);
/* 146 */         this.set.dispose();
/*     */         
/* 148 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeAmb.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */