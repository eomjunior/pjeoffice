/*     */ package io.reactivex.internal.operators.completable;
/*     */ 
/*     */ import io.reactivex.Completable;
/*     */ import io.reactivex.CompletableObserver;
/*     */ import io.reactivex.CompletableSource;
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
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ public final class CompletableUsing<R>
/*     */   extends Completable
/*     */ {
/*     */   final Callable<R> resourceSupplier;
/*     */   final Function<? super R, ? extends CompletableSource> completableFunction;
/*     */   final Consumer<? super R> disposer;
/*     */   final boolean eager;
/*     */   
/*     */   public CompletableUsing(Callable<R> resourceSupplier, Function<? super R, ? extends CompletableSource> completableFunction, Consumer<? super R> disposer, boolean eager) {
/*  37 */     this.resourceSupplier = resourceSupplier;
/*  38 */     this.completableFunction = completableFunction;
/*  39 */     this.disposer = disposer;
/*  40 */     this.eager = eager;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(CompletableObserver observer) {
/*     */     R resource;
/*     */     CompletableSource source;
/*     */     try {
/*  48 */       resource = this.resourceSupplier.call();
/*  49 */     } catch (Throwable ex) {
/*  50 */       Exceptions.throwIfFatal(ex);
/*  51 */       EmptyDisposable.error(ex, observer);
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*     */     try {
/*  58 */       source = (CompletableSource)ObjectHelper.requireNonNull(this.completableFunction.apply(resource), "The completableFunction returned a null CompletableSource");
/*  59 */     } catch (Throwable ex) {
/*  60 */       Exceptions.throwIfFatal(ex);
/*  61 */       if (this.eager) {
/*     */         try {
/*  63 */           this.disposer.accept(resource);
/*  64 */         } catch (Throwable exc) {
/*  65 */           Exceptions.throwIfFatal(exc);
/*  66 */           EmptyDisposable.error((Throwable)new CompositeException(new Throwable[] { ex, exc }, ), observer);
/*     */           
/*     */           return;
/*     */         } 
/*     */       }
/*  71 */       EmptyDisposable.error(ex, observer);
/*     */       
/*  73 */       if (!this.eager) {
/*     */         try {
/*  75 */           this.disposer.accept(resource);
/*  76 */         } catch (Throwable exc) {
/*  77 */           Exceptions.throwIfFatal(exc);
/*  78 */           RxJavaPlugins.onError(exc);
/*     */         } 
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/*  84 */     source.subscribe(new UsingObserver<R>(observer, resource, this.disposer, this.eager));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class UsingObserver<R>
/*     */     extends AtomicReference<Object>
/*     */     implements CompletableObserver, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -674404550052917487L;
/*     */     
/*     */     final CompletableObserver downstream;
/*     */     
/*     */     final Consumer<? super R> disposer;
/*     */     
/*     */     final boolean eager;
/*     */     Disposable upstream;
/*     */     
/*     */     UsingObserver(CompletableObserver actual, R resource, Consumer<? super R> disposer, boolean eager) {
/* 102 */       super(resource);
/* 103 */       this.downstream = actual;
/* 104 */       this.disposer = disposer;
/* 105 */       this.eager = eager;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 110 */       this.upstream.dispose();
/* 111 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 112 */       disposeResourceAfter();
/*     */     }
/*     */ 
/*     */     
/*     */     void disposeResourceAfter() {
/* 117 */       Object resource = getAndSet(this);
/* 118 */       if (resource != this) {
/*     */         try {
/* 120 */           this.disposer.accept(resource);
/* 121 */         } catch (Throwable ex) {
/* 122 */           Exceptions.throwIfFatal(ex);
/* 123 */           RxJavaPlugins.onError(ex);
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 130 */       return this.upstream.isDisposed();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 135 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 136 */         this.upstream = d;
/*     */         
/* 138 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*     */       CompositeException compositeException;
/* 145 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 146 */       if (this.eager) {
/* 147 */         Object resource = getAndSet(this);
/* 148 */         if (resource != this) {
/*     */           try {
/* 150 */             this.disposer.accept(resource);
/* 151 */           } catch (Throwable ex) {
/* 152 */             Exceptions.throwIfFatal(ex);
/* 153 */             compositeException = new CompositeException(new Throwable[] { e, ex });
/*     */           } 
/*     */         } else {
/*     */           return;
/*     */         } 
/*     */       } 
/*     */       
/* 160 */       this.downstream.onError((Throwable)compositeException);
/*     */       
/* 162 */       if (!this.eager) {
/* 163 */         disposeResourceAfter();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 170 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 171 */       if (this.eager) {
/* 172 */         Object resource = getAndSet(this);
/* 173 */         if (resource != this) {
/*     */           try {
/* 175 */             this.disposer.accept(resource);
/* 176 */           } catch (Throwable ex) {
/* 177 */             Exceptions.throwIfFatal(ex);
/* 178 */             this.downstream.onError(ex);
/*     */             
/*     */             return;
/*     */           } 
/*     */         } else {
/*     */           return;
/*     */         } 
/*     */       } 
/* 186 */       this.downstream.onComplete();
/*     */       
/* 188 */       if (!this.eager)
/* 189 */         disposeResourceAfter(); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableUsing.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */