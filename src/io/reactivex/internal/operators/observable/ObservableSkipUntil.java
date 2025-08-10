/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.ArrayCompositeDisposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.observers.SerializedObserver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ObservableSkipUntil<T, U>
/*     */   extends AbstractObservableWithUpstream<T, T>
/*     */ {
/*     */   final ObservableSource<U> other;
/*     */   
/*     */   public ObservableSkipUntil(ObservableSource<T> source, ObservableSource<U> other) {
/*  24 */     super(source);
/*  25 */     this.other = other;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super T> child) {
/*  31 */     SerializedObserver<T> serial = new SerializedObserver(child);
/*     */     
/*  33 */     ArrayCompositeDisposable frc = new ArrayCompositeDisposable(2);
/*     */     
/*  35 */     serial.onSubscribe((Disposable)frc);
/*     */     
/*  37 */     SkipUntilObserver<T> sus = new SkipUntilObserver<T>((Observer<? super T>)serial, frc);
/*     */     
/*  39 */     this.other.subscribe(new SkipUntil(frc, sus, serial));
/*     */     
/*  41 */     this.source.subscribe(sus);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class SkipUntilObserver<T>
/*     */     implements Observer<T>
/*     */   {
/*     */     final Observer<? super T> downstream;
/*     */     final ArrayCompositeDisposable frc;
/*     */     Disposable upstream;
/*     */     volatile boolean notSkipping;
/*     */     boolean notSkippingLocal;
/*     */     
/*     */     SkipUntilObserver(Observer<? super T> actual, ArrayCompositeDisposable frc) {
/*  55 */       this.downstream = actual;
/*  56 */       this.frc = frc;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  61 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  62 */         this.upstream = d;
/*  63 */         this.frc.setResource(0, d);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  69 */       if (this.notSkippingLocal) {
/*  70 */         this.downstream.onNext(t);
/*     */       }
/*  72 */       else if (this.notSkipping) {
/*  73 */         this.notSkippingLocal = true;
/*  74 */         this.downstream.onNext(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  80 */       this.frc.dispose();
/*  81 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  86 */       this.frc.dispose();
/*  87 */       this.downstream.onComplete();
/*     */     }
/*     */   }
/*     */   
/*     */   final class SkipUntil implements Observer<U> {
/*     */     final ArrayCompositeDisposable frc;
/*     */     final ObservableSkipUntil.SkipUntilObserver<T> sus;
/*     */     final SerializedObserver<T> serial;
/*     */     Disposable upstream;
/*     */     
/*     */     SkipUntil(ArrayCompositeDisposable frc, ObservableSkipUntil.SkipUntilObserver<T> sus, SerializedObserver<T> serial) {
/*  98 */       this.frc = frc;
/*  99 */       this.sus = sus;
/* 100 */       this.serial = serial;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 105 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 106 */         this.upstream = d;
/* 107 */         this.frc.setResource(1, d);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(U t) {
/* 113 */       this.upstream.dispose();
/* 114 */       this.sus.notSkipping = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 119 */       this.frc.dispose();
/* 120 */       this.serial.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 125 */       this.sus.notSkipping = true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableSkipUntil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */