/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Predicate;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
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
/*     */ public final class ObservableAny<T>
/*     */   extends AbstractObservableWithUpstream<T, Boolean>
/*     */ {
/*     */   final Predicate<? super T> predicate;
/*     */   
/*     */   public ObservableAny(ObservableSource<T> source, Predicate<? super T> predicate) {
/*  25 */     super(source);
/*  26 */     this.predicate = predicate;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super Boolean> t) {
/*  31 */     this.source.subscribe(new AnyObserver<T>(t, this.predicate));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class AnyObserver<T>
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     final Observer<? super Boolean> downstream;
/*     */     final Predicate<? super T> predicate;
/*     */     Disposable upstream;
/*     */     boolean done;
/*     */     
/*     */     AnyObserver(Observer<? super Boolean> actual, Predicate<? super T> predicate) {
/*  44 */       this.downstream = actual;
/*  45 */       this.predicate = predicate;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  50 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  51 */         this.upstream = d;
/*  52 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/*     */       boolean b;
/*  58 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       
/*     */       try {
/*  63 */         b = this.predicate.test(t);
/*  64 */       } catch (Throwable e) {
/*  65 */         Exceptions.throwIfFatal(e);
/*  66 */         this.upstream.dispose();
/*  67 */         onError(e);
/*     */         return;
/*     */       } 
/*  70 */       if (b) {
/*  71 */         this.done = true;
/*  72 */         this.upstream.dispose();
/*  73 */         this.downstream.onNext(Boolean.valueOf(true));
/*  74 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  80 */       if (this.done) {
/*  81 */         RxJavaPlugins.onError(t);
/*     */         
/*     */         return;
/*     */       } 
/*  85 */       this.done = true;
/*  86 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  91 */       if (!this.done) {
/*  92 */         this.done = true;
/*  93 */         this.downstream.onNext(Boolean.valueOf(false));
/*  94 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 100 */       this.upstream.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 105 */       return this.upstream.isDisposed();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableAny.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */