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
/*     */ public final class ObservableAll<T>
/*     */   extends AbstractObservableWithUpstream<T, Boolean>
/*     */ {
/*     */   final Predicate<? super T> predicate;
/*     */   
/*     */   public ObservableAll(ObservableSource<T> source, Predicate<? super T> predicate) {
/*  25 */     super(source);
/*  26 */     this.predicate = predicate;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super Boolean> t) {
/*  31 */     this.source.subscribe(new AllObserver<T>(t, this.predicate));
/*     */   }
/*     */   
/*     */   static final class AllObserver<T>
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     final Observer<? super Boolean> downstream;
/*     */     final Predicate<? super T> predicate;
/*     */     Disposable upstream;
/*     */     boolean done;
/*     */     
/*     */     AllObserver(Observer<? super Boolean> actual, Predicate<? super T> predicate) {
/*  43 */       this.downstream = actual;
/*  44 */       this.predicate = predicate;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  49 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  50 */         this.upstream = d;
/*  51 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/*     */       boolean b;
/*  57 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       
/*     */       try {
/*  62 */         b = this.predicate.test(t);
/*  63 */       } catch (Throwable e) {
/*  64 */         Exceptions.throwIfFatal(e);
/*  65 */         this.upstream.dispose();
/*  66 */         onError(e);
/*     */         return;
/*     */       } 
/*  69 */       if (!b) {
/*  70 */         this.done = true;
/*  71 */         this.upstream.dispose();
/*  72 */         this.downstream.onNext(Boolean.valueOf(false));
/*  73 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  79 */       if (this.done) {
/*  80 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/*  83 */       this.done = true;
/*  84 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  89 */       if (this.done) {
/*     */         return;
/*     */       }
/*  92 */       this.done = true;
/*  93 */       this.downstream.onNext(Boolean.valueOf(true));
/*  94 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  99 */       this.upstream.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 104 */       return this.upstream.isDisposed();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableAll.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */