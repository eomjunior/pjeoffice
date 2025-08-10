/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.BiFunction;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.Callable;
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
/*     */ public final class ObservableScanSeed<T, R>
/*     */   extends AbstractObservableWithUpstream<T, R>
/*     */ {
/*     */   final BiFunction<R, ? super T, R> accumulator;
/*     */   final Callable<R> seedSupplier;
/*     */   
/*     */   public ObservableScanSeed(ObservableSource<T> source, Callable<R> seedSupplier, BiFunction<R, ? super T, R> accumulator) {
/*  30 */     super(source);
/*  31 */     this.accumulator = accumulator;
/*  32 */     this.seedSupplier = seedSupplier;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super R> t) {
/*     */     R r;
/*     */     try {
/*  40 */       r = (R)ObjectHelper.requireNonNull(this.seedSupplier.call(), "The seed supplied is null");
/*  41 */     } catch (Throwable e) {
/*  42 */       Exceptions.throwIfFatal(e);
/*  43 */       EmptyDisposable.error(e, t);
/*     */       
/*     */       return;
/*     */     } 
/*  47 */     this.source.subscribe(new ScanSeedObserver<T, R>(t, this.accumulator, r));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ScanSeedObserver<T, R>
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     final Observer<? super R> downstream;
/*     */     final BiFunction<R, ? super T, R> accumulator;
/*     */     R value;
/*     */     Disposable upstream;
/*     */     boolean done;
/*     */     
/*     */     ScanSeedObserver(Observer<? super R> actual, BiFunction<R, ? super T, R> accumulator, R value) {
/*  61 */       this.downstream = actual;
/*  62 */       this.accumulator = accumulator;
/*  63 */       this.value = value;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  68 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  69 */         this.upstream = d;
/*     */         
/*  71 */         this.downstream.onSubscribe(this);
/*     */         
/*  73 */         this.downstream.onNext(this.value);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  79 */       this.upstream.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  84 */       return this.upstream.isDisposed();
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/*     */       R u;
/*  89 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       
/*  93 */       R v = this.value;
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/*  98 */         u = (R)ObjectHelper.requireNonNull(this.accumulator.apply(v, t), "The accumulator returned a null value");
/*  99 */       } catch (Throwable e) {
/* 100 */         Exceptions.throwIfFatal(e);
/* 101 */         this.upstream.dispose();
/* 102 */         onError(e);
/*     */         
/*     */         return;
/*     */       } 
/* 106 */       this.value = u;
/*     */       
/* 108 */       this.downstream.onNext(u);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 113 */       if (this.done) {
/* 114 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 117 */       this.done = true;
/* 118 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 123 */       if (this.done) {
/*     */         return;
/*     */       }
/* 126 */       this.done = true;
/* 127 */       this.downstream.onComplete();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableScanSeed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */