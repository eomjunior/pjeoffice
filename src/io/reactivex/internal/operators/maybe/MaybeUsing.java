/*     */ package io.reactivex.internal.operators.maybe;
/*     */ 
/*     */ import io.reactivex.Maybe;
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
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
/*     */ public final class MaybeUsing<T, D>
/*     */   extends Maybe<T>
/*     */ {
/*     */   final Callable<? extends D> resourceSupplier;
/*     */   final Function<? super D, ? extends MaybeSource<? extends T>> sourceSupplier;
/*     */   final Consumer<? super D> resourceDisposer;
/*     */   final boolean eager;
/*     */   
/*     */   public MaybeUsing(Callable<? extends D> resourceSupplier, Function<? super D, ? extends MaybeSource<? extends T>> sourceSupplier, Consumer<? super D> resourceDisposer, boolean eager) {
/*  48 */     this.resourceSupplier = resourceSupplier;
/*  49 */     this.sourceSupplier = sourceSupplier;
/*  50 */     this.resourceDisposer = resourceDisposer;
/*  51 */     this.eager = eager;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/*     */     D resource;
/*     */     MaybeSource<? extends T> source;
/*     */     try {
/*  59 */       resource = this.resourceSupplier.call();
/*  60 */     } catch (Throwable ex) {
/*  61 */       Exceptions.throwIfFatal(ex);
/*  62 */       EmptyDisposable.error(ex, observer);
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*     */     try {
/*  69 */       source = (MaybeSource<? extends T>)ObjectHelper.requireNonNull(this.sourceSupplier.apply(resource), "The sourceSupplier returned a null MaybeSource");
/*  70 */     } catch (Throwable ex) {
/*  71 */       Exceptions.throwIfFatal(ex);
/*  72 */       if (this.eager) {
/*     */         try {
/*  74 */           this.resourceDisposer.accept(resource);
/*  75 */         } catch (Throwable exc) {
/*  76 */           Exceptions.throwIfFatal(exc);
/*  77 */           EmptyDisposable.error((Throwable)new CompositeException(new Throwable[] { ex, exc }, ), observer);
/*     */           
/*     */           return;
/*     */         } 
/*     */       }
/*  82 */       EmptyDisposable.error(ex, observer);
/*     */       
/*  84 */       if (!this.eager) {
/*     */         try {
/*  86 */           this.resourceDisposer.accept(resource);
/*  87 */         } catch (Throwable exc) {
/*  88 */           Exceptions.throwIfFatal(exc);
/*  89 */           RxJavaPlugins.onError(exc);
/*     */         } 
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/*  95 */     source.subscribe(new UsingObserver<T, D>(observer, resource, this.resourceDisposer, this.eager));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class UsingObserver<T, D>
/*     */     extends AtomicReference<Object>
/*     */     implements MaybeObserver<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -674404550052917487L;
/*     */     
/*     */     final MaybeObserver<? super T> downstream;
/*     */     
/*     */     final Consumer<? super D> disposer;
/*     */     
/*     */     final boolean eager;
/*     */     Disposable upstream;
/*     */     
/*     */     UsingObserver(MaybeObserver<? super T> actual, D resource, Consumer<? super D> disposer, boolean eager) {
/* 113 */       super(resource);
/* 114 */       this.downstream = actual;
/* 115 */       this.disposer = disposer;
/* 116 */       this.eager = eager;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 121 */       this.upstream.dispose();
/* 122 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 123 */       disposeResourceAfter();
/*     */     }
/*     */ 
/*     */     
/*     */     void disposeResourceAfter() {
/* 128 */       Object resource = getAndSet(this);
/* 129 */       if (resource != this) {
/*     */         try {
/* 131 */           this.disposer.accept(resource);
/* 132 */         } catch (Throwable ex) {
/* 133 */           Exceptions.throwIfFatal(ex);
/* 134 */           RxJavaPlugins.onError(ex);
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 141 */       return this.upstream.isDisposed();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 146 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 147 */         this.upstream = d;
/*     */         
/* 149 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/* 156 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 157 */       if (this.eager) {
/* 158 */         Object resource = getAndSet(this);
/* 159 */         if (resource != this) {
/*     */           try {
/* 161 */             this.disposer.accept(resource);
/* 162 */           } catch (Throwable ex) {
/* 163 */             Exceptions.throwIfFatal(ex);
/* 164 */             this.downstream.onError(ex);
/*     */             
/*     */             return;
/*     */           } 
/*     */         } else {
/*     */           return;
/*     */         } 
/*     */       } 
/* 172 */       this.downstream.onSuccess(value);
/*     */       
/* 174 */       if (!this.eager) {
/* 175 */         disposeResourceAfter();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*     */       CompositeException compositeException;
/* 182 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 183 */       if (this.eager) {
/* 184 */         Object resource = getAndSet(this);
/* 185 */         if (resource != this) {
/*     */           try {
/* 187 */             this.disposer.accept(resource);
/* 188 */           } catch (Throwable ex) {
/* 189 */             Exceptions.throwIfFatal(ex);
/* 190 */             compositeException = new CompositeException(new Throwable[] { e, ex });
/*     */           } 
/*     */         } else {
/*     */           return;
/*     */         } 
/*     */       } 
/*     */       
/* 197 */       this.downstream.onError((Throwable)compositeException);
/*     */       
/* 199 */       if (!this.eager) {
/* 200 */         disposeResourceAfter();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 207 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 208 */       if (this.eager) {
/* 209 */         Object resource = getAndSet(this);
/* 210 */         if (resource != this) {
/*     */           try {
/* 212 */             this.disposer.accept(resource);
/* 213 */           } catch (Throwable ex) {
/* 214 */             Exceptions.throwIfFatal(ex);
/* 215 */             this.downstream.onError(ex);
/*     */             
/*     */             return;
/*     */           } 
/*     */         } else {
/*     */           return;
/*     */         } 
/*     */       } 
/* 223 */       this.downstream.onComplete();
/*     */       
/* 225 */       if (!this.eager)
/* 226 */         disposeResourceAfter(); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeUsing.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */