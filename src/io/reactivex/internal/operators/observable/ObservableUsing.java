/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.CompositeException;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Consumer;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.Callable;
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
/*     */ public final class ObservableUsing<T, D>
/*     */   extends Observable<T>
/*     */ {
/*     */   final Callable<? extends D> resourceSupplier;
/*     */   final Function<? super D, ? extends ObservableSource<? extends T>> sourceSupplier;
/*     */   final Consumer<? super D> disposer;
/*     */   final boolean eager;
/*     */   
/*     */   public ObservableUsing(Callable<? extends D> resourceSupplier, Function<? super D, ? extends ObservableSource<? extends T>> sourceSupplier, Consumer<? super D> disposer, boolean eager) {
/*  37 */     this.resourceSupplier = resourceSupplier;
/*  38 */     this.sourceSupplier = sourceSupplier;
/*  39 */     this.disposer = disposer;
/*  40 */     this.eager = eager;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super T> observer) {
/*     */     D resource;
/*     */     ObservableSource<? extends T> source;
/*     */     try {
/*  48 */       resource = this.resourceSupplier.call();
/*  49 */     } catch (Throwable e) {
/*  50 */       Exceptions.throwIfFatal(e);
/*  51 */       EmptyDisposable.error(e, observer);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*     */     try {
/*  57 */       source = (ObservableSource<? extends T>)ObjectHelper.requireNonNull(this.sourceSupplier.apply(resource), "The sourceSupplier returned a null ObservableSource");
/*  58 */     } catch (Throwable e) {
/*  59 */       Exceptions.throwIfFatal(e);
/*     */       try {
/*  61 */         this.disposer.accept(resource);
/*  62 */       } catch (Throwable ex) {
/*  63 */         Exceptions.throwIfFatal(ex);
/*  64 */         EmptyDisposable.error((Throwable)new CompositeException(new Throwable[] { e, ex }, ), observer);
/*     */         return;
/*     */       } 
/*  67 */       EmptyDisposable.error(e, observer);
/*     */       
/*     */       return;
/*     */     } 
/*  71 */     UsingObserver<T, D> us = new UsingObserver<T, D>(observer, resource, this.disposer, this.eager);
/*     */     
/*  73 */     source.subscribe(us);
/*     */   }
/*     */   
/*     */   static final class UsingObserver<T, D>
/*     */     extends AtomicBoolean
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 5904473792286235046L;
/*     */     final Observer<? super T> downstream;
/*     */     final D resource;
/*     */     final Consumer<? super D> disposer;
/*     */     final boolean eager;
/*     */     Disposable upstream;
/*     */     
/*     */     UsingObserver(Observer<? super T> actual, D resource, Consumer<? super D> disposer, boolean eager) {
/*  88 */       this.downstream = actual;
/*  89 */       this.resource = resource;
/*  90 */       this.disposer = disposer;
/*  91 */       this.eager = eager;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  96 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  97 */         this.upstream = d;
/*  98 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 104 */       this.downstream.onNext(t);
/*     */     }
/*     */     
/*     */     public void onError(Throwable t) {
/*     */       CompositeException compositeException;
/* 109 */       if (this.eager) {
/* 110 */         if (compareAndSet(false, true)) {
/*     */           try {
/* 112 */             this.disposer.accept(this.resource);
/* 113 */           } catch (Throwable e) {
/* 114 */             Exceptions.throwIfFatal(e);
/* 115 */             compositeException = new CompositeException(new Throwable[] { t, e });
/*     */           } 
/*     */         }
/*     */         
/* 119 */         this.upstream.dispose();
/* 120 */         this.downstream.onError((Throwable)compositeException);
/*     */       } else {
/* 122 */         this.downstream.onError((Throwable)compositeException);
/* 123 */         this.upstream.dispose();
/* 124 */         disposeAfter();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 130 */       if (this.eager) {
/* 131 */         if (compareAndSet(false, true)) {
/*     */           try {
/* 133 */             this.disposer.accept(this.resource);
/* 134 */           } catch (Throwable e) {
/* 135 */             Exceptions.throwIfFatal(e);
/* 136 */             this.downstream.onError(e);
/*     */             
/*     */             return;
/*     */           } 
/*     */         }
/* 141 */         this.upstream.dispose();
/* 142 */         this.downstream.onComplete();
/*     */       } else {
/* 144 */         this.downstream.onComplete();
/* 145 */         this.upstream.dispose();
/* 146 */         disposeAfter();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 152 */       disposeAfter();
/* 153 */       this.upstream.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 158 */       return get();
/*     */     }
/*     */     
/*     */     void disposeAfter() {
/* 162 */       if (compareAndSet(false, true))
/*     */         try {
/* 164 */           this.disposer.accept(this.resource);
/* 165 */         } catch (Throwable e) {
/* 166 */           Exceptions.throwIfFatal(e);
/*     */           
/* 168 */           RxJavaPlugins.onError(e);
/*     */         }  
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableUsing.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */