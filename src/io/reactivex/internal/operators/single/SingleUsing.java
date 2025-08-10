/*     */ package io.reactivex.internal.operators.single;
/*     */ 
/*     */ import io.reactivex.Single;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.SingleSource;
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
/*     */ 
/*     */ public final class SingleUsing<T, U>
/*     */   extends Single<T>
/*     */ {
/*     */   final Callable<U> resourceSupplier;
/*     */   final Function<? super U, ? extends SingleSource<? extends T>> singleFunction;
/*     */   final Consumer<? super U> disposer;
/*     */   final boolean eager;
/*     */   
/*     */   public SingleUsing(Callable<U> resourceSupplier, Function<? super U, ? extends SingleSource<? extends T>> singleFunction, Consumer<? super U> disposer, boolean eager) {
/*  38 */     this.resourceSupplier = resourceSupplier;
/*  39 */     this.singleFunction = singleFunction;
/*  40 */     this.disposer = disposer;
/*  41 */     this.eager = eager;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void subscribeActual(SingleObserver<? super T> observer) {
/*     */     U resource;
/*     */     SingleSource<? extends T> source;
/*     */     try {
/*  50 */       resource = this.resourceSupplier.call();
/*  51 */     } catch (Throwable ex) {
/*  52 */       Exceptions.throwIfFatal(ex);
/*  53 */       EmptyDisposable.error(ex, observer);
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*     */     try {
/*  60 */       source = (SingleSource<? extends T>)ObjectHelper.requireNonNull(this.singleFunction.apply(resource), "The singleFunction returned a null SingleSource");
/*  61 */     } catch (Throwable ex) {
/*  62 */       CompositeException compositeException; Exceptions.throwIfFatal(ex);
/*     */       
/*  64 */       if (this.eager) {
/*     */         try {
/*  66 */           this.disposer.accept(resource);
/*  67 */         } catch (Throwable exc) {
/*  68 */           Exceptions.throwIfFatal(exc);
/*  69 */           compositeException = new CompositeException(new Throwable[] { ex, exc });
/*     */         } 
/*     */       }
/*  72 */       EmptyDisposable.error((Throwable)compositeException, observer);
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
/*  84 */     source.subscribe(new UsingSingleObserver<T, U>(observer, resource, this.eager, this.disposer));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class UsingSingleObserver<T, U>
/*     */     extends AtomicReference<Object>
/*     */     implements SingleObserver<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -5331524057054083935L;
/*     */     
/*     */     final SingleObserver<? super T> downstream;
/*     */     
/*     */     final Consumer<? super U> disposer;
/*     */     
/*     */     final boolean eager;
/*     */     Disposable upstream;
/*     */     
/*     */     UsingSingleObserver(SingleObserver<? super T> actual, U resource, boolean eager, Consumer<? super U> disposer) {
/* 102 */       super(resource);
/* 103 */       this.downstream = actual;
/* 104 */       this.eager = eager;
/* 105 */       this.disposer = disposer;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 110 */       this.upstream.dispose();
/* 111 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 112 */       disposeAfter();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 117 */       return this.upstream.isDisposed();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 122 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 123 */         this.upstream = d;
/*     */         
/* 125 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/* 132 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*     */       
/* 134 */       if (this.eager) {
/* 135 */         Object u = getAndSet(this);
/* 136 */         if (u != this) {
/*     */           try {
/* 138 */             this.disposer.accept(u);
/* 139 */           } catch (Throwable ex) {
/* 140 */             Exceptions.throwIfFatal(ex);
/* 141 */             this.downstream.onError(ex);
/*     */             
/*     */             return;
/*     */           } 
/*     */         } else {
/*     */           return;
/*     */         } 
/*     */       } 
/* 149 */       this.downstream.onSuccess(value);
/*     */       
/* 151 */       if (!this.eager) {
/* 152 */         disposeAfter();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*     */       CompositeException compositeException;
/* 159 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*     */       
/* 161 */       if (this.eager) {
/* 162 */         Object u = getAndSet(this);
/* 163 */         if (u != this) {
/*     */           try {
/* 165 */             this.disposer.accept(u);
/* 166 */           } catch (Throwable ex) {
/* 167 */             Exceptions.throwIfFatal(ex);
/* 168 */             compositeException = new CompositeException(new Throwable[] { e, ex });
/*     */           } 
/*     */         } else {
/*     */           return;
/*     */         } 
/*     */       } 
/*     */       
/* 175 */       this.downstream.onError((Throwable)compositeException);
/*     */       
/* 177 */       if (!this.eager) {
/* 178 */         disposeAfter();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     void disposeAfter() {
/* 184 */       Object u = getAndSet(this);
/* 185 */       if (u != this)
/*     */         try {
/* 187 */           this.disposer.accept(u);
/* 188 */         } catch (Throwable ex) {
/* 189 */           Exceptions.throwIfFatal(ex);
/* 190 */           RxJavaPlugins.onError(ex);
/*     */         }  
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleUsing.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */