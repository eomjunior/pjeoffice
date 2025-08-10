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
/*     */ 
/*     */ public final class ObservableTakeWhile<T>
/*     */   extends AbstractObservableWithUpstream<T, T>
/*     */ {
/*     */   final Predicate<? super T> predicate;
/*     */   
/*     */   public ObservableTakeWhile(ObservableSource<T> source, Predicate<? super T> predicate) {
/*  26 */     super(source);
/*  27 */     this.predicate = predicate;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super T> t) {
/*  32 */     this.source.subscribe(new TakeWhileObserver<T>(t, this.predicate));
/*     */   }
/*     */   
/*     */   static final class TakeWhileObserver<T>
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     final Observer<? super T> downstream;
/*     */     final Predicate<? super T> predicate;
/*     */     Disposable upstream;
/*     */     boolean done;
/*     */     
/*     */     TakeWhileObserver(Observer<? super T> actual, Predicate<? super T> predicate) {
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
/*     */     
/*     */     public void dispose() {
/*  58 */       this.upstream.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  63 */       return this.upstream.isDisposed();
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/*     */       boolean b;
/*  68 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       
/*     */       try {
/*  73 */         b = this.predicate.test(t);
/*  74 */       } catch (Throwable e) {
/*  75 */         Exceptions.throwIfFatal(e);
/*  76 */         this.upstream.dispose();
/*  77 */         onError(e);
/*     */         
/*     */         return;
/*     */       } 
/*  81 */       if (!b) {
/*  82 */         this.done = true;
/*  83 */         this.upstream.dispose();
/*  84 */         this.downstream.onComplete();
/*     */         
/*     */         return;
/*     */       } 
/*  88 */       this.downstream.onNext(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  93 */       if (this.done) {
/*  94 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/*  97 */       this.done = true;
/*  98 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 103 */       if (this.done) {
/*     */         return;
/*     */       }
/* 106 */       this.done = true;
/* 107 */       this.downstream.onComplete();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableTakeWhile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */