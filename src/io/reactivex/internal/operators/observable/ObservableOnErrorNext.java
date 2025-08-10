/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.CompositeException;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.SequentialDisposable;
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
/*     */ 
/*     */ public final class ObservableOnErrorNext<T>
/*     */   extends AbstractObservableWithUpstream<T, T>
/*     */ {
/*     */   final Function<? super Throwable, ? extends ObservableSource<? extends T>> nextSupplier;
/*     */   final boolean allowFatal;
/*     */   
/*     */   public ObservableOnErrorNext(ObservableSource<T> source, Function<? super Throwable, ? extends ObservableSource<? extends T>> nextSupplier, boolean allowFatal) {
/*  29 */     super(source);
/*  30 */     this.nextSupplier = nextSupplier;
/*  31 */     this.allowFatal = allowFatal;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super T> t) {
/*  36 */     OnErrorNextObserver<T> parent = new OnErrorNextObserver<T>(t, this.nextSupplier, this.allowFatal);
/*  37 */     t.onSubscribe((Disposable)parent.arbiter);
/*  38 */     this.source.subscribe(parent);
/*     */   }
/*     */   
/*     */   static final class OnErrorNextObserver<T>
/*     */     implements Observer<T>
/*     */   {
/*     */     final Observer<? super T> downstream;
/*     */     final Function<? super Throwable, ? extends ObservableSource<? extends T>> nextSupplier;
/*     */     final boolean allowFatal;
/*     */     final SequentialDisposable arbiter;
/*     */     boolean once;
/*     */     boolean done;
/*     */     
/*     */     OnErrorNextObserver(Observer<? super T> actual, Function<? super Throwable, ? extends ObservableSource<? extends T>> nextSupplier, boolean allowFatal) {
/*  52 */       this.downstream = actual;
/*  53 */       this.nextSupplier = nextSupplier;
/*  54 */       this.allowFatal = allowFatal;
/*  55 */       this.arbiter = new SequentialDisposable();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  60 */       this.arbiter.replace(d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  65 */       if (this.done) {
/*     */         return;
/*     */       }
/*  68 */       this.downstream.onNext(t);
/*     */     }
/*     */     
/*     */     public void onError(Throwable t) {
/*     */       ObservableSource<? extends T> p;
/*  73 */       if (this.once) {
/*  74 */         if (this.done) {
/*  75 */           RxJavaPlugins.onError(t);
/*     */           return;
/*     */         } 
/*  78 */         this.downstream.onError(t);
/*     */         return;
/*     */       } 
/*  81 */       this.once = true;
/*     */       
/*  83 */       if (this.allowFatal && !(t instanceof Exception)) {
/*  84 */         this.downstream.onError(t);
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/*     */       try {
/*  91 */         p = (ObservableSource<? extends T>)this.nextSupplier.apply(t);
/*  92 */       } catch (Throwable e) {
/*  93 */         Exceptions.throwIfFatal(e);
/*  94 */         this.downstream.onError((Throwable)new CompositeException(new Throwable[] { t, e }));
/*     */         
/*     */         return;
/*     */       } 
/*  98 */       if (p == null) {
/*  99 */         NullPointerException npe = new NullPointerException("Observable is null");
/* 100 */         npe.initCause(t);
/* 101 */         this.downstream.onError(npe);
/*     */         
/*     */         return;
/*     */       } 
/* 105 */       p.subscribe(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 110 */       if (this.done) {
/*     */         return;
/*     */       }
/* 113 */       this.done = true;
/* 114 */       this.once = true;
/* 115 */       this.downstream.onComplete();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableOnErrorNext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */