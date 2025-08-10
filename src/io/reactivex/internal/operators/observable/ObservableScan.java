/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.BiFunction;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
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
/*     */ public final class ObservableScan<T>
/*     */   extends AbstractObservableWithUpstream<T, T>
/*     */ {
/*     */   final BiFunction<T, T, T> accumulator;
/*     */   
/*     */   public ObservableScan(ObservableSource<T> source, BiFunction<T, T, T> accumulator) {
/*  27 */     super(source);
/*  28 */     this.accumulator = accumulator;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super T> t) {
/*  33 */     this.source.subscribe(new ScanObserver<T>(t, this.accumulator));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ScanObserver<T>
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     final Observer<? super T> downstream;
/*     */     final BiFunction<T, T, T> accumulator;
/*     */     Disposable upstream;
/*     */     T value;
/*     */     boolean done;
/*     */     
/*     */     ScanObserver(Observer<? super T> actual, BiFunction<T, T, T> accumulator) {
/*  47 */       this.downstream = actual;
/*  48 */       this.accumulator = accumulator;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  53 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  54 */         this.upstream = d;
/*  55 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  61 */       this.upstream.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  66 */       return this.upstream.isDisposed();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  71 */       if (this.done) {
/*     */         return;
/*     */       }
/*  74 */       Observer<? super T> a = this.downstream;
/*  75 */       T v = this.value;
/*  76 */       if (v == null) {
/*  77 */         this.value = t;
/*  78 */         a.onNext(t);
/*     */       } else {
/*     */         T u;
/*     */         
/*     */         try {
/*  83 */           u = (T)ObjectHelper.requireNonNull(this.accumulator.apply(v, t), "The value returned by the accumulator is null");
/*  84 */         } catch (Throwable e) {
/*  85 */           Exceptions.throwIfFatal(e);
/*  86 */           this.upstream.dispose();
/*  87 */           onError(e);
/*     */           
/*     */           return;
/*     */         } 
/*  91 */         this.value = u;
/*  92 */         a.onNext(u);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  98 */       if (this.done) {
/*  99 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 102 */       this.done = true;
/* 103 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 108 */       if (this.done) {
/*     */         return;
/*     */       }
/* 111 */       this.done = true;
/* 112 */       this.downstream.onComplete();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableScan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */